package com.innobiz.orange.web.cu.svc;

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
import com.innobiz.orange.web.cu.vo.CuTaskStatFileDVo;
import com.innobiz.orange.web.dm.vo.DmCommFileDVo;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.em.svc.EmFileUploadSvc;
import com.innobiz.orange.web.em.vo.EmTmpFileTVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

/** 파일 서비스 */
@Service
public class CuNausFileSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(CuNausFileSvc.class);

	/** 파일업로드 태그ID */
	public static final String FILES_ID = "cufiles";

	/** 공통 서비스 */
	@Autowired
	private CuNausSvc cuNausSvc;

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
	
	/** 문서뷰어 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** 파일업로드 서비스 */
	@Resource(name = "emFileUploadSvc")
	private EmFileUploadSvc emFileUploadSvc;
	
	/** 파일 업로드 (DISK) */
	public UploadHandler upload(HttpServletRequest request) throws CmException, IOException {
		UploadHandler uploadHandler = uploadManager.createHandler(request, "temp", "cu");
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
	public List<CommonFileVo> saveFile(HttpServletRequest request, String refId, QueryQueue queryQueue, String typCd) throws IOException, CmException, SQLException {
		
		// Multipart 파일 리스트
		List<MultipartFile> mpFileList = ((MultipartHttpServletRequest) request).getFiles(FILES_ID + "_file");

		// 파일 배포
		distribute(request, "/cu", mpFileList);

		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
		String[] fileIds = request.getParameterValues(FILES_ID + "_fileId");
		String[] valids = request.getParameterValues(FILES_ID + "_valid");  // 신규추가된 파일이면 Y
		String[] useYns = request.getParameterValues(FILES_ID + "_useYn");  // 삭제된 파일이면 N
		String[] tmpFileId = request.getParameterValues("tmpFileId");  // 임시파일ID
		
		// 임시파일VO
		EmTmpFileTVo emTmpFileTVo;
		CmMultipartFile file;
		int insertCnt = 0, dispOrdr = 0;
		if(valids!=null){
			for (int i = 0; i < valids.length; i++) {
				if ("N".equals(useYns[i])) {
					if ("Y".equals(valids[i])) continue;
	
					// 첨부파일(WH_REQ_FILE_D) 테이블 - DELETE
					CuTaskStatFileDVo cuTaskStatFileDVo = new CuTaskStatFileDVo();
					cuTaskStatFileDVo.setFileId(Integer.valueOf(fileIds[i]));
					// 테이블 구분(업무현황, 주간보고)
					if(typCd!=null) cuTaskStatFileDVo.setTypCd(typCd);
					queryQueue.delete(cuTaskStatFileDVo);
	
					// 첨부파일(WH_REQ_FILE_D) 테이블 - SELECT
					CuTaskStatFileDVo fileVo = (CuTaskStatFileDVo) commonDao.queryVo(cuTaskStatFileDVo);
					if (fileVo != null) deletedFileList.add(fileVo);
					continue;
				}
	
				dispOrdr++;
				if ("N".equals(valids[i])) {
					if ("".equals(fileIds[i])) continue;
					
					// 첨부파일(WH_REQ_FILE_D) 테이블 - UPDATE
					CuTaskStatFileDVo cuTaskStatFileDVo = new CuTaskStatFileDVo();
					cuTaskStatFileDVo.setFileId(Integer.valueOf(fileIds[i]));
					cuTaskStatFileDVo.setDispOrdr(dispOrdr);
					if(typCd!=null) cuTaskStatFileDVo.setTypCd(typCd);
					queryQueue.update(cuTaskStatFileDVo);
				} else {
					// 첨부파일(WH_REQ_FILE_D) 테이블 - INSERT
					CuTaskStatFileDVo cuTaskStatFileDVo = new CuTaskStatFileDVo();
					if(tmpFileId!=null && tmpFileId[i]!=null && !tmpFileId[i].isEmpty()){
						emTmpFileTVo = emFileUploadSvc.getEmTmpFileVo(Integer.parseInt(tmpFileId[i]));
						if(emTmpFileTVo==null) continue;
						
						// 파일 새이름으로 복사 후 파일 배포
						String newSavePath = emFileUploadSvc.copyAndDist(request, "/cu", emTmpFileTVo.getSavePath(), emTmpFileTVo.getFileExt());
						cuTaskStatFileDVo.setDispNm(emTmpFileTVo.getDispNm());
						cuTaskStatFileDVo.setFileExt(emTmpFileTVo.getFileExt());
						cuTaskStatFileDVo.setFileSize((long)emTmpFileTVo.getFileSize());
						cuTaskStatFileDVo.setSavePath(newSavePath);
					}else{
						file = (CmMultipartFile) mpFileList.get(insertCnt++);
						if(file==null) continue;
						cuTaskStatFileDVo.setDispNm(file.getOriginalFilename());
						cuTaskStatFileDVo.setFileExt(file.getExt());
						cuTaskStatFileDVo.setFileSize(file.getSize());
						cuTaskStatFileDVo.setSavePath(file.getSavePath());
					}
					if(typCd!=null) cuTaskStatFileDVo.setTypCd(typCd);
					cuTaskStatFileDVo.setFileId(cuNausSvc.createFileId(typCd));
					cuTaskStatFileDVo.setRefId(refId);					
					cuTaskStatFileDVo.setDispOrdr(dispOrdr);					
					cuTaskStatFileDVo.setUseYn("Y");
					cuTaskStatFileDVo.setRegrUid(userVo.getUserUid());
					cuTaskStatFileDVo.setRegDt("sysdate");
	
					queryQueue.insert(cuTaskStatFileDVo);
				}
			}
		}
		return deletedFileList;
	}

	/** 첨부파일 리턴 (DB) */
	public CommonFileVo getFileVo(Integer fileId, String typCd) throws SQLException {
		// 첨부파일(WH_REQ_FILE_D) 테이블 - SELECT
		CuTaskStatFileDVo cuTaskStatFileDVo = new CuTaskStatFileDVo();
		cuTaskStatFileDVo.setFileId(fileId);
		if(typCd!=null) cuTaskStatFileDVo.setTypCd(typCd);
		cuTaskStatFileDVo = (CuTaskStatFileDVo) commonDao.queryVo(cuTaskStatFileDVo);
		return cuTaskStatFileDVo;
	}

	/** 첨부파일 목록 리턴 (DB) */
	@SuppressWarnings("unchecked")
	public List<CommonFileVo> getFileVoList(String refId, String typCd) throws SQLException {
		if (refId == null) return new ArrayList<CommonFileVo>();
		
		// 첨부파일(WH_REQ_FILE_D) 테이블 - SELECT
		CuTaskStatFileDVo cuTaskStatFileDVo = new CuTaskStatFileDVo();
		cuTaskStatFileDVo.setRefId(refId);
		if(typCd!=null) cuTaskStatFileDVo.setTypCd(typCd);
		return (List<CommonFileVo>) commonDao.queryList(cuTaskStatFileDVo);
	}

	/** 첨부파일 리스트 model에 추가 */
	public void putFileListToModel(String statNo, ModelMap model, String compId, String typCd) throws SQLException {
		List<CommonFileVo> fileVoList = getFileVoList(statNo, typCd);
		model.put("fileVoList", fileVoList);
		model.put("filesId", FILES_ID);
		
		// 확장자 허용제한 
		ptSysSvc.setAttachExtMap(model, compId, "cu");
		
		// 다운로드|문서뷰어 사용여부
		emAttachViewSvc.chkAttachSetup(model, compId);
	}

	/** 첨부파일 삭제 (DB) */
	public List<CommonFileVo> deleteTaskFile(String refId, QueryQueue queryQueue, String typCd) throws SQLException {
		// 첨부파일(WH_REQ_FILE_D) 테이블 - DELETE
		CuTaskStatFileDVo cuTaskStatFileDVo = new CuTaskStatFileDVo();
		cuTaskStatFileDVo.setRefId(refId);
		if(typCd!=null) cuTaskStatFileDVo.setTypCd(typCd);
		queryQueue.delete(cuTaskStatFileDVo);

		return getFileVoList(refId, typCd);
	}

	/** 파일 참조ID 수정 (DB) */
	public void updateRefId(String refId, String newRefId, QueryQueue queryQueue, String typCd) {
		CuTaskStatFileDVo cuTaskStatFileDVo = new CuTaskStatFileDVo();
		cuTaskStatFileDVo.setRefId(refId);
		if(typCd!=null) cuTaskStatFileDVo.setTypCd(typCd);
		queryQueue.update(cuTaskStatFileDVo);
	}
	
	/** 파일ID 리턴 */
	public String getFilesId(){
		return FILES_ID;
	}
	
	/** 첨부파일 목록 건수 리턴 (DB) */
	public Integer getFileVoListCnt(String refId, String typCd) throws SQLException {
		// 첨부파일(WH_REQ_FILE_D) 테이블 - SELECT
		CuTaskStatFileDVo cuTaskStatFileDVo = new CuTaskStatFileDVo();
		cuTaskStatFileDVo.setRefId(refId);
		if(typCd!=null) cuTaskStatFileDVo.setTypCd(typCd);
		return commonDao.count(cuTaskStatFileDVo);
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
			QueryQueue queryQueue, String tgtTypCd,List<DmCommFileDVo> copyFileList, String typCd) throws SQLException, IOException, CmException {
		if(copyFileList == null) return;
		// 첨부파일 목록
		List<CommonFileVo> fileVoList = getFileVoList(refId, typCd);
		if(fileVoList==null)
			return;				
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}
		
		// 복사할 대상의 파일경로 prefix
		String path = "/cu";
				
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		DmCommFileDVo dmCommFileDVo;
		File file = null;
		for (CommonFileVo fileVo : fileVoList) {
			file = new File(wasCopyBaseDir+fileVo.getSavePath());
			if(!file.isFile()) continue;
			// 복사할 새파일이름 정의
			String newSavePath = getFileNewPath(request, path, fileVo.getSavePath(), fileVo.getFileExt());

			// 첨부파일(DM_FILE_D) 테이블 - INSERT
			CuTaskStatFileDVo cuTaskStatFileDVo = new CuTaskStatFileDVo();
			BeanUtils.copyProperties(fileVo, cuTaskStatFileDVo, new String[]{"refId", "savePath", "regrUid", "regDt"});
			cuTaskStatFileDVo.setFileId(cuNausSvc.createFileId(typCd));
			cuTaskStatFileDVo.setRefId(newRefId);
			cuTaskStatFileDVo.setSavePath(newSavePath);
			cuTaskStatFileDVo.setRegrUid(userVo.getUserUid());
			cuTaskStatFileDVo.setRegDt("sysdate");
			if(typCd!=null) cuTaskStatFileDVo.setTypCd(typCd);
			queryQueue.insert(cuTaskStatFileDVo);
			
			// db 저장 이후 실제파일 복사 준비
			dmCommFileDVo = new DmCommFileDVo();
			dmCommFileDVo.setOriginSavePath(fileVo.getSavePath());
			dmCommFileDVo.setMdPath("/cu");
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
