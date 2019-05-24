package com.innobiz.orange.web.wh.svc;

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
import com.innobiz.orange.web.cm.utils.ParamUtil;
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
import com.innobiz.orange.web.wh.vo.WhReqFileDVo;

/** 파일 서비스 */
@Service
public class WhFileSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WhFileSvc.class);

	/** 파일업로드 태그ID */
	public static final String FILES_ID = "whfiles";

	/** 공통 서비스 */
	@Autowired
	private WhCmSvc whCmSvc;

	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;

	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
//	/** 메세지 */
//	@Autowired
//    private MessageProperties messageProperties;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
//	/** 보내기 서비스 */
//	@Autowired
//	private EmSendSvc emSendSvc;
	
	/** 문서뷰어 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** 파일업로드 서비스 */
	@Resource(name = "emFileUploadSvc")
	private EmFileUploadSvc emFileUploadSvc;
	
	/** 파일 업로드 (DISK) */
	public UploadHandler upload(HttpServletRequest request) throws CmException, IOException {
		UploadHandler uploadHandler = uploadManager.createHandler(request, "temp", "wh");
		uploadHandler.upload(); // 업로드 파일 정보
		return uploadHandler;
	}

	
	/** 파일 삭제 (DISK) */
	public void deleteDiskFiles(List<CommonFileVo> deletedFileList) {
		if (deletedFileList == null || deletedFileList.size() == 0) return;
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}
		for (CommonFileVo fileVo : deletedFileList) {
			if(fileVo==null || fileVo.getSavePath()==null) continue;
			String savePath = fileVo.getSavePath();
			boolean deleted = new File(wasCopyBaseDir+savePath).delete();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("savePath = " + savePath + ", deleted = " + deleted);
			}
		}
	}

	/** 파일 배포 (DISK) */
	public void distribute(HttpServletRequest request, String path, List<MultipartFile> mpFileList) throws IOException, CmException {
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

	/** 첨부파일 저장 (DB) */
	public List<CommonFileVo> saveHpFile(HttpServletRequest request, String refId, QueryQueue queryQueue) throws IOException, CmException, SQLException {
		
		// Multipart 파일 리스트
		List<MultipartFile> mpFileList = ((MultipartHttpServletRequest) request).getFiles(FILES_ID + "_file");

		// 파일 배포
		distribute(request, "/wh", mpFileList);

		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
		String[] fileIds = request.getParameterValues(FILES_ID + "_fileId");
		String[] valids = request.getParameterValues(FILES_ID + "_valid");  // 신규추가된 파일이면 Y
		String[] useYns = request.getParameterValues(FILES_ID + "_useYn");  // 삭제된 파일이면 N
		String[] tmpFileId = request.getParameterValues("tmpFileId");  // 임시파일ID
		String statCd = ParamUtil.getRequestParam(request, "statCd", false); // 파일구분코드
		if(statCd==null || statCd.isEmpty() || "T".equals(statCd))
			statCd="R";
		
		// 임시파일VO
		EmTmpFileTVo emTmpFileTVo;
		CmMultipartFile file;
		int insertCnt = 0, dispOrdr = 0;
		if(valids!=null){
			for (int i = 0; i < valids.length; i++) {
				if ("N".equals(useYns[i])) {
					if ("Y".equals(valids[i])) continue;
	
					// 첨부파일(WH_REQ_FILE_D) 테이블 - DELETE
					WhReqFileDVo whReqFileDVo = new WhReqFileDVo();
					whReqFileDVo.setFileId(Integer.valueOf(fileIds[i]));
					queryQueue.delete(whReqFileDVo);
	
					// 첨부파일(WH_REQ_FILE_D) 테이블 - SELECT
					WhReqFileDVo fileVo = (WhReqFileDVo) commonDao.queryVo(whReqFileDVo);
					if (fileVo != null) deletedFileList.add(fileVo);
					continue;
				}
	
				dispOrdr++;
				if ("N".equals(valids[i])) {
					if ("".equals(fileIds[i])) continue;
					
					// 첨부파일(WH_REQ_FILE_D) 테이블 - UPDATE
					WhReqFileDVo whReqFileDVo = new WhReqFileDVo();
					whReqFileDVo.setFileId(Integer.valueOf(fileIds[i]));
					whReqFileDVo.setDispOrdr(dispOrdr);
					queryQueue.update(whReqFileDVo);
				} else {
					// 첨부파일(WH_REQ_FILE_D) 테이블 - INSERT
					WhReqFileDVo whReqFileDVo = new WhReqFileDVo();
					if(tmpFileId!=null && tmpFileId[i]!=null && !tmpFileId[i].isEmpty()){
						emTmpFileTVo = emFileUploadSvc.getEmTmpFileVo(Integer.parseInt(tmpFileId[i]));
						if(emTmpFileTVo==null) continue;
						
						// 파일 새이름으로 복사 후 파일 배포
						String newSavePath = emFileUploadSvc.copyAndDist(request, "/wh", emTmpFileTVo.getSavePath(), emTmpFileTVo.getFileExt());
						whReqFileDVo.setDispNm(emTmpFileTVo.getDispNm());
						whReqFileDVo.setFileExt(emTmpFileTVo.getFileExt());
						whReqFileDVo.setFileSize((long)emTmpFileTVo.getFileSize());
						whReqFileDVo.setSavePath(newSavePath);
					}else{
						file = (CmMultipartFile) mpFileList.get(insertCnt++);
						if(file==null) continue;
						whReqFileDVo.setDispNm(file.getOriginalFilename());
						whReqFileDVo.setFileExt(file.getExt());
						whReqFileDVo.setFileSize(file.getSize());
						whReqFileDVo.setSavePath(file.getSavePath());
					}
					whReqFileDVo.setFileId(whCmSvc.createFileId());
					whReqFileDVo.setTypCd(statCd);
					whReqFileDVo.setRefId(refId);					
					whReqFileDVo.setDispOrdr(dispOrdr);					
					whReqFileDVo.setUseYn("Y");
					whReqFileDVo.setRegrUid(userVo.getUserUid());
					whReqFileDVo.setRegDt("sysdate");
	
					queryQueue.insert(whReqFileDVo);
				}
			}
		}
		return deletedFileList;
	}

	/** 첨부파일 리턴 (DB) */
	public CommonFileVo getFileVo(Integer fileId) throws SQLException {
		// 첨부파일(WH_REQ_FILE_D) 테이블 - SELECT
		WhReqFileDVo whReqFileDVo = new WhReqFileDVo();
		whReqFileDVo.setFileId(fileId);
		whReqFileDVo = (WhReqFileDVo) commonDao.queryVo(whReqFileDVo);
		return whReqFileDVo;
	}

	/** 첨부파일 목록 리턴 (DB) */
	@SuppressWarnings("unchecked")
	public List<CommonFileVo> getFileVoList(String refId, String typCd) throws SQLException {
		if (refId == null) return new ArrayList<CommonFileVo>();
		
		// 첨부파일(WH_REQ_FILE_D) 테이블 - SELECT
		WhReqFileDVo whReqFileDVo = new WhReqFileDVo();
		whReqFileDVo.setRefId(refId);
		whReqFileDVo.setTypCd(typCd);
		return (List<CommonFileVo>) commonDao.queryList(whReqFileDVo);
	}

	/** 첨부파일 리스트 model에 추가 */
	public void putFileListToModel(String reqNo, ModelMap model, String compId, String typCd) throws SQLException {
		List<CommonFileVo> fileVoList = getFileVoList(reqNo, typCd);
		model.put("fileVoList", fileVoList);
		model.put("filesId", FILES_ID);
		
		// 확장자 허용제한 
		ptSysSvc.setAttachExtMap(model, compId, "wh");
		
		// 다운로드|문서뷰어 사용여부
		emAttachViewSvc.chkAttachSetup(model, compId);
	}

	/** 첨부파일 삭제 (DB) */
	public List<CommonFileVo> deleteReqFile(String refId, QueryQueue queryQueue, String typCd) throws SQLException {
		// 첨부파일(WH_REQ_FILE_D) 테이블 - DELETE
		WhReqFileDVo whReqFileDVo = new WhReqFileDVo();
		whReqFileDVo.setRefId(refId);
		whReqFileDVo.setTypCd(typCd);
		queryQueue.delete(whReqFileDVo);

		return getFileVoList(refId, typCd);
	}

	/** 파일 참조ID 수정 (DB) */
	public void updateRefId(String refId, String newRefId, QueryQueue queryQueue, String typCd) {
		WhReqFileDVo whReqFileDVo = new WhReqFileDVo();
		whReqFileDVo.setRefId(refId);
		whReqFileDVo.setTypCd(typCd);
		queryQueue.update(whReqFileDVo);
	}
	
	/** 파일ID 리턴 */
	public String getFilesId(){
		return FILES_ID;
	}
	
	/** 첨부파일 목록 건수 리턴 (DB) */
	public Integer getFileVoListCnt(String refId, String typCd) throws SQLException {
		// 첨부파일(WH_REQ_FILE_D) 테이블 - SELECT
		WhReqFileDVo whReqFileDVo = new WhReqFileDVo();
		whReqFileDVo.setRefId(refId);
		whReqFileDVo.setTypCd(typCd);
		return commonDao.count(whReqFileDVo);
	}
	
	/** 파일복사를 위한 경로 및 파일명 설정 및 파일 배포 (WAS DISK) */
	private String getFileNewPath(HttpServletRequest request, String path, String savePath, String ext) throws IOException, CmException {
		DistHandler distHandler = distManager.createHandler(path, SessionUtil.getLocale(request));  // 업로드 경로 설정
		
		// 새이름
		String newSavePath = savePath.replace('\\', '/').substring(0, savePath.lastIndexOf('/')) + "/F" + StringUtil.getNextHexa() + "." + ext;
		
		String distPath = distHandler.addWasList(newSavePath);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("distPath = " + distPath);
		}
		distHandler.distribute();
		
		return newSavePath;//distPath;
	}
	
	/** 첨부파일 복사 (DB, DISK) */
	public void copyFile(HttpServletRequest request, String refId, String newRefId, 
			QueryQueue queryQueue, String srcTypCd, String tgtTypCd,List<DmCommFileDVo> copyFileList) throws SQLException, IOException, CmException {
		if(copyFileList == null) return;
		// 첨부파일 목록
		List<CommonFileVo> fileVoList = getFileVoList(refId, srcTypCd);
		if(fileVoList==null)
			return;				
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}
		
		// 복사할 대상의 파일경로 prefix
		String path = "/wh";
				
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		DmCommFileDVo dmCommFileDVo;
		File file = null;
		for (CommonFileVo fileVo : fileVoList) {
			file = new File(wasCopyBaseDir+fileVo.getSavePath());
			if(!file.isFile()) continue;
			// 파일 새이름으로 복사 후 파일 배포
			//String newSavePath = copyAndDist(request, "/dm/doc", fileVo.getSavePath(), fileVo.getFileExt(), wasCopyBaseDir);
			// 복사할 새파일이름 정의
			String newSavePath = getFileNewPath(request, path, fileVo.getSavePath(), fileVo.getFileExt());

			// 첨부파일(DM_FILE_D) 테이블 - INSERT
			WhReqFileDVo whReqFileDVo = new WhReqFileDVo();
			BeanUtils.copyProperties(fileVo, whReqFileDVo, new String[]{"refId", "savePath", "regrUid", "regDt"});
			whReqFileDVo.setTypCd(tgtTypCd);
			whReqFileDVo.setFileId(whCmSvc.createFileId());
			whReqFileDVo.setRefId(newRefId);
			whReqFileDVo.setSavePath(newSavePath);
			whReqFileDVo.setRegrUid(userVo.getUserUid());
			whReqFileDVo.setRegDt("sysdate");
			queryQueue.insert(whReqFileDVo);
			
			// db 저장 이후 실제파일 복사 준비
			dmCommFileDVo = new DmCommFileDVo();
			dmCommFileDVo.setOriginSavePath(fileVo.getSavePath());
			dmCommFileDVo.setMdPath("/wh");
			dmCommFileDVo.setNewSavePath(newSavePath);
			
			copyFileList.add(dmCommFileDVo);
		}
	}
	
	/** 실제 파일 복사*/
	public void copyFileList(HttpServletRequest request, String path, List<DmCommFileDVo> copyFileList) throws IOException, CmException {
		copyFileDist(request, path, copyFileList);
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
	
}
