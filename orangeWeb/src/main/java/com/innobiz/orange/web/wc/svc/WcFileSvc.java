package com.innobiz.orange.web.wc.svc;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.CmMultipartFile;
import com.innobiz.orange.web.cm.files.DistHandler;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.dm.vo.DmCommFileDVo;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.em.svc.EmFileUploadSvc;
import com.innobiz.orange.web.em.vo.EmTmpFileTVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.wc.vo.WcSchdlFileDVo;

/** 게시파일 서비스 */
@Service
public class WcFileSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WcFileSvc.class);

	/** 파일업로드 태그ID */
	public static final String FILES_ID = "wcfiles";

	/** 일정 공통 서비스 */
	@Autowired
	private WcCmSvc wcCmSvc;

	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;

	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 첨부설정 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** 파일업로드 서비스 */
	@Resource(name = "emFileUploadSvc")
	private EmFileUploadSvc emFileUploadSvc;
	
	/** 파일 업로드 (DISK) */
	public UploadHandler upload(HttpServletRequest request) throws CmException, IOException {
		UploadHandler uploadHandler = uploadManager.createHandler(request, "temp", "wc");
		uploadHandler.upload(); // 업로드 파일 정보
		return uploadHandler;
	}

	/** 파일 삭제 (DISK) */
	public void deleteDiskFiles(List<CommonFileVo> deletedFileList) {
		if (deletedFileList == null || deletedFileList.size() == 0) return;

		for (CommonFileVo fileVo : deletedFileList) {
			if (fileVo == null) continue;
			String savePath = fileVo.getSavePath();
			boolean deleted = new File(savePath).delete();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("savePath = " + savePath + ", deleted = " + deleted);
			}
		}
	}

	/** 파일 배포 (DISK) */
	private void distribute(HttpServletRequest request, String path, List<MultipartFile> mpFileList) throws IOException, CmException {
		DistHandler distHandler = distManager.createHandler(path, SessionUtil.getLocale(request));  // 업로드 경로 설정
		for (MultipartFile mpFile : mpFileList) {
			CmMultipartFile file = (CmMultipartFile) mpFile;
			String distPath = distHandler.addWasList(file.getSavePath());
			file.setSavePath(distPath);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("distPath = " + distPath);
			}
		}
		distHandler.distribute();
	}

	/** 일정첨부파일 저장 (DB) */
	public List<CommonFileVo> saveScdlFile(HttpServletRequest request, String refId,
			QueryQueue queryQueue, String repetFlag) throws SQLException, CmException, IOException {

		//repetFlag 반복 [파일설정] 개별반영(indiv)/전체반영(all)
		//savePath 반복파일 모두 삭제 하기위한 path명
		// Multipart 파일 리스트
 		List<MultipartFile> mpFileList = ((MultipartHttpServletRequest) request).getFiles(FILES_ID + "_file");
 		
		// 파일 배포
		distribute(request, "/wc", mpFileList);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);

		List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
		String[] fileIds = request.getParameterValues(FILES_ID + "_fileId");
		String[] valids = request.getParameterValues(FILES_ID + "_valid");  // 신규추가된 파일이면 Y
		String[] useYns = request.getParameterValues(FILES_ID + "_useYn");  // 삭제된 파일이면 N
		String[] tmpFileId = request.getParameterValues("tmpFileId");  // 임시파일ID
		// 임시파일VO
		EmTmpFileTVo emTmpFileTVo;
		String savePath ="";
		CmMultipartFile file;
		int insertCnt = 0, dispOrdr = 0;
		for (int i = 0; i < valids.length; i++) {
			if ("N".equals(useYns[i])) {
				if ("Y".equals(valids[i])) continue;
				
				
					WcSchdlFileDVo wcFindFile = new WcSchdlFileDVo();
					wcFindFile.setFileId(Integer.valueOf(fileIds[i]));
					WcSchdlFileDVo wcFileVo = (WcSchdlFileDVo) commonDao.queryVo(wcFindFile);
					if(wcFileVo != null){
						String[] splitData = wcFileVo.getSavePath().split("\\/");
						savePath = splitData[(splitData.length)-1];
					}

					// 일정첨부파일(WC_SCHD_FILE_D) 테이블 - DELETE
					WcSchdlFileDVo wcSchdlFileDVo = new WcSchdlFileDVo();
					if(repetFlag.equalsIgnoreCase("indiv")){
						wcSchdlFileDVo.setFileId(Integer.valueOf(fileIds[i]));
					}else if(repetFlag.equalsIgnoreCase("all")){
						if(savePath == null || "".equals(savePath)){
							// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
							throw new CmException("pt.msg.nodata.passed", request);
						}
						wcSchdlFileDVo.setSavePath(savePath);
					}
					queryQueue.delete(wcSchdlFileDVo);
				//반복은 삭제 안함.
//				// 일정첨부파일(WC_SCHD_FILE_D) 테이블 - SELECT
//				WcSchdlFileDVo scdlFileVo = (WcSchdlFileDVo) commonDao.queryVo(wcSchdlFileDVo);
//				if (scdlFileVo != null) deletedFileList.add(scdlFileVo);
				continue;
			}

			dispOrdr++;
			if ("N".equals(valids[i])) {
				if ("".equals(fileIds[i])) continue;

				// 일정첨부파일(WC_SCDHL_FILE_D) 테이블 - UPDATE
				WcSchdlFileDVo scdlFileDVo = new WcSchdlFileDVo();
				scdlFileDVo.setFileId(Integer.valueOf(fileIds[i]));
				scdlFileDVo.setDispOrdr(dispOrdr);
				queryQueue.update(scdlFileDVo);
			} else {
				// 일정첨부파일(WC_SCDHL_FILE_D) 테이블 - INSERT
				WcSchdlFileDVo wcSchdlFileDVo = new WcSchdlFileDVo();
				if(tmpFileId!=null && tmpFileId[i]!=null && !tmpFileId[i].isEmpty()){
					emTmpFileTVo = emFileUploadSvc.getEmTmpFileVo(Integer.parseInt(tmpFileId[i]));
					if(emTmpFileTVo==null) continue;
					
					// 파일 새이름으로 복사 후 파일 배포
					String newSavePath = emFileUploadSvc.copyAndDist(request, "/wc", emTmpFileTVo.getSavePath(), emTmpFileTVo.getFileExt());
					wcSchdlFileDVo.setDispNm(emTmpFileTVo.getDispNm());
					wcSchdlFileDVo.setFileExt(emTmpFileTVo.getFileExt());
					wcSchdlFileDVo.setFileSize((long)emTmpFileTVo.getFileSize());
					wcSchdlFileDVo.setSavePath(newSavePath);
				}else{
					file = (CmMultipartFile) mpFileList.get(insertCnt++);
					if(file==null) continue;
					wcSchdlFileDVo.setDispNm(file.getOriginalFilename());
					wcSchdlFileDVo.setFileExt(file.getExt());
					wcSchdlFileDVo.setFileSize(file.getSize());
					wcSchdlFileDVo.setSavePath(file.getSavePath());
				}
				
				wcSchdlFileDVo.setFileId(wcCmSvc.createFileId("WC_SCHDL_FILE_D"));
				wcSchdlFileDVo.setRefId(refId);
				wcSchdlFileDVo.setDispOrdr(dispOrdr);
				wcSchdlFileDVo.setUseYn("Y");
				wcSchdlFileDVo.setRegrUid(userVo.getUserUid());
				wcSchdlFileDVo.setRegDt("sysdate");

				queryQueue.insert(wcSchdlFileDVo);
			}
		}
		return deletedFileList;
	}

	/** 일정첨부파일 리턴 (DB) */
	public CommonFileVo getFileVo(Integer fileId) throws SQLException {
		// 일정첨부파일(WC_SCDHL_FILE_D) 테이블 - SELECT
		WcSchdlFileDVo schdlFileDVo = new WcSchdlFileDVo();
		schdlFileDVo.setFileId(fileId);
		schdlFileDVo = (WcSchdlFileDVo) commonDao.queryVo(schdlFileDVo);
		return schdlFileDVo;
	}

	/** 일정첨부파일 목록 리턴 (DB) */
	@SuppressWarnings("unchecked")
	public List<CommonFileVo> getFileVoList(String refId) throws SQLException {
		if (refId == null) return new ArrayList<CommonFileVo>();
		
		// 일정첨부파일(WC_SCDHL_FILE_D) 테이블 - SELECT
		WcSchdlFileDVo schdlFileDVo = new WcSchdlFileDVo();
		schdlFileDVo.setRefId(refId);
		return (List<CommonFileVo>) commonDao.queryList(schdlFileDVo);
	}

	/** 일정첨부파일 리스트 model에 추가 */
	public void putFileListToModel(String refId, ModelMap model, String compId) throws SQLException {
		List<CommonFileVo> fileVoList = getFileVoList(refId);
		model.put("fileVoList", fileVoList);
		model.put("filesId", FILES_ID);
		// 확장자 허용제한 
		ptSysSvc.setAttachExtMap(model, compId, "wc");
		
		// 다운로드|문서뷰어 사용여부
		emAttachViewSvc.chkAttachSetup(model, compId);
	}

	/** 일정첨부파일 삭제 (DB) */
	public List<CommonFileVo> deleteWcFile(String refId, QueryQueue queryQueue) throws SQLException {
		// 일정첨부파일(WC_SCDHL_FILE_D) 테이블 - DELETE
		WcSchdlFileDVo wcSchdlFileDVo = new WcSchdlFileDVo();
		wcSchdlFileDVo.setRefId(refId);
		queryQueue.delete(wcSchdlFileDVo);

		return getFileVoList(refId);
	}
	
	/** 파일복사를 위한 경로 및 파일명 설정 및 파일 배포 (WAS DISK) */
	private String getFileNewPath(HttpServletRequest request, String path, String originPath, String savePath, String ext) throws IOException, CmException {
		DistHandler distHandler = distManager.createHandler(path, SessionUtil.getLocale(request));  // 업로드 경로 설정
		
		// 새이름
		String newSavePath = savePath.replace('\\', '/').substring(0, savePath.lastIndexOf('/')) + "/F" + StringUtil.getNextHexa() + "." + ext;
		
		// 원본경로 prefix가 있으면...
		if(originPath != null && !originPath.isEmpty()){
			// 신규 파일경로 prefix가 해당 업무 path 와 같지 않으면 동일한 path 로 맞춰주기 위해 replace 한다. 
			if(!newSavePath.startsWith(path)){
				newSavePath = newSavePath.replace(originPath,path);
			}
		}
		
		String distPath = distHandler.addWasList(newSavePath);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("distPath = " + distPath);
		}
		distHandler.distribute();
		
		return newSavePath;//distPath;
	}
	
	/** 파일 저장 - 기존일정 파일 복사 */
	public void saveFile(HttpServletRequest request, String refId, QueryQueue queryQueue, 
			List<DmCommFileDVo> copyFileList) throws IOException, CmException, SQLException {
		
		// 원본 파일경로 prefix
		String path = "/wc";
		// 원본 파일경로 prefix
		String originPath = path;
		
		// Multipart 파일 리스트
		List<MultipartFile> mpFileList = ((MultipartHttpServletRequest) request).getFiles(FILES_ID + "_file");
		
		// 파일 배포
		distribute(request, path, mpFileList);

		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		//List<CommonFileVo> fileVoList = new ArrayList<CommonFileVo>();
		String[] fileIds = request.getParameterValues(FILES_ID + "_fileId");
		String[] valids = request.getParameterValues(FILES_ID + "_valid");
		String[] useYns = request.getParameterValues(FILES_ID + "_useYn");
		String[] tmpFileId = request.getParameterValues("tmpFileId");  // 임시파일ID
		
		// 임시파일VO
		EmTmpFileTVo emTmpFileTVo;
		
		int insertCnt = 0, dispOrdr = 0;
		DmCommFileDVo dmCommFileDVo = null;
		CmMultipartFile file;
		if(valids != null){
			for (int i = 0; i < valids.length; i++) {
				// 삭제파일 제외
				if ("N".equals(useYns[i])) continue;
				dispOrdr++;
				if ("N".equals(valids[i])) {//기존 파일
					if ("".equals(fileIds[i])) continue;
					CommonFileVo fileVo = null;
					
					fileVo = getFileVo(Integer.parseInt(fileIds[i]));
					
					if(fileVo == null) continue;
					// 파일VO 생성
					WcSchdlFileDVo wcSchdlFileDVo = new WcSchdlFileDVo();
					// 복사 파일 경로
					String newSavePath = getFileNewPath(request, path, originPath, fileVo.getSavePath(), fileVo.getFileExt());
					
					BeanUtils.copyProperties(fileVo, wcSchdlFileDVo, new String[]{"refId", "savePath", "regrUid", "regDt"});
					wcSchdlFileDVo.setFileId(wcCmSvc.createFileId("WC_SCHDL_FILE_D"));
					wcSchdlFileDVo.setRefId(refId);
					wcSchdlFileDVo.setSavePath(newSavePath);
					wcSchdlFileDVo.setRegrUid(userVo.getUserUid());
					wcSchdlFileDVo.setRegDt("sysdate");
					
					// 순서 변경
					wcSchdlFileDVo.setDispOrdr(dispOrdr);
					
					// 실제파일 복사 준비
					dmCommFileDVo = new DmCommFileDVo();
					dmCommFileDVo.setOriginSavePath(fileVo.getSavePath());
					dmCommFileDVo.setMdPath(path);
					dmCommFileDVo.setNewSavePath(wcSchdlFileDVo.getSavePath());
					
					if(copyFileList!=null) copyFileList.add(dmCommFileDVo);
					
					queryQueue.insert(wcSchdlFileDVo);
					
				} else {// 신규파일
					// 일정첨부파일(WC_SCDHL_FILE_D) 테이블 - INSERT
					WcSchdlFileDVo wcSchdlFileDVo = new WcSchdlFileDVo();
					if(tmpFileId!=null && tmpFileId[i]!=null && !tmpFileId[i].isEmpty()){
						emTmpFileTVo = emFileUploadSvc.getEmTmpFileVo(Integer.parseInt(tmpFileId[i]));
						if(emTmpFileTVo==null) continue;
						
						// 파일 새이름으로 복사 후 파일 배포
						String newSavePath = emFileUploadSvc.copyAndDist(request, "/wc", emTmpFileTVo.getSavePath(), emTmpFileTVo.getFileExt());
						wcSchdlFileDVo.setDispNm(emTmpFileTVo.getDispNm());
						wcSchdlFileDVo.setFileExt(emTmpFileTVo.getFileExt());
						wcSchdlFileDVo.setFileSize((long)emTmpFileTVo.getFileSize());
						wcSchdlFileDVo.setSavePath(newSavePath);
					}else{
						file = (CmMultipartFile) mpFileList.get(insertCnt++);
						wcSchdlFileDVo.setDispNm(file.getOriginalFilename());
						wcSchdlFileDVo.setFileExt(file.getExt());
						wcSchdlFileDVo.setFileSize(file.getSize());
						wcSchdlFileDVo.setSavePath(file.getSavePath());
					}
					wcSchdlFileDVo.setFileId(wcCmSvc.createFileId("WC_SCHDL_FILE_D"));
					wcSchdlFileDVo.setRefId(refId);
					wcSchdlFileDVo.setDispOrdr(dispOrdr);
					wcSchdlFileDVo.setUseYn("Y");
					wcSchdlFileDVo.setRegrUid(userVo.getUserUid());
					wcSchdlFileDVo.setRegDt("sysdate");
					queryQueue.insert(wcSchdlFileDVo);
				}
			}
		}
	}
	
	/** 파일을 새이름으로 복사 후 파일 배포 (WAS DISK) */
	private void copyFileDist(HttpServletRequest request, String path, List<DmCommFileDVo> copyFileList) throws IOException, CmException {
		DistHandler distHandler = distManager.createHandler(path, SessionUtil.getLocale(request));  // 업로드 경로 설정
		
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}
		
		for(DmCommFileDVo fileVo : copyFileList){
			// 파일복사
			distHandler.copyFile(wasCopyBaseDir+fileVo.getOriginSavePath(), wasCopyBaseDir+fileVo.getNewSavePath());
		}
	}
	
	/** 실제 파일 복사*/
	public void copyFileList(HttpServletRequest request, List<DmCommFileDVo> copyFileList) throws IOException, CmException {
		copyFileDist(request, "/wc", copyFileList);
	}
	
	/** 첨부파일 복사 */
	public void copyFile(HttpServletRequest request, String refId, QueryQueue queryQueue, List<CommonFileVo> fileVoList, 
			List<DmCommFileDVo> copyFileList, String regrUid) throws SQLException, IOException, CmException {
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}
		
		// 복사할 대상의 파일경로 prefix
		String path = "/wc";
		// 원본 파일경로 prefix
		String originPath = path;
				
		UserVo userVo = LoginSession.getUser(request);

		// 첨부파일 목록
		DmCommFileDVo dmCommFileDVo;
		File file = null;
		for (CommonFileVo fileVo : fileVoList) {
			file = new File(wasCopyBaseDir+fileVo.getSavePath());
			if(!file.isFile()) continue;
			// 복사할 새파일이름 정의
			String newSavePath = getFileNewPath(request, path, originPath, fileVo.getSavePath(), fileVo.getFileExt());

			// 첨부파일(DM_FILE_D) 테이블 - INSERT
			WcSchdlFileDVo wcSchdlFileDVo = new WcSchdlFileDVo();			
			BeanUtils.copyProperties(fileVo, wcSchdlFileDVo, new String[]{"refId", "savePath", "regrUid", "regDt"});
			wcSchdlFileDVo.setFileId(wcCmSvc.createFileId("WC_SCHDL_FILE_D"));
			wcSchdlFileDVo.setRefId(refId);
			wcSchdlFileDVo.setSavePath(newSavePath);
			if(regrUid == null || regrUid.isEmpty()) wcSchdlFileDVo.setRegrUid(userVo.getUserUid());
			else wcSchdlFileDVo.setRegrUid(regrUid);
			wcSchdlFileDVo.setRegDt("sysdate");
			queryQueue.insert(wcSchdlFileDVo);
			
			// db 저장 이후 실제파일 복사 준비
			dmCommFileDVo = new DmCommFileDVo();
			dmCommFileDVo.setOriginSavePath(fileVo.getSavePath());
			dmCommFileDVo.setMdPath(path);
			dmCommFileDVo.setNewSavePath(newSavePath);
			
			copyFileList.add(dmCommFileDVo);
		}
	}
	
}
