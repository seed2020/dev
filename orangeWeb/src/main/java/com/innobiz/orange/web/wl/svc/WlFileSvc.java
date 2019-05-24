package com.innobiz.orange.web.wl.svc;

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
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.em.svc.EmFileUploadSvc;
import com.innobiz.orange.web.em.svc.EmSendSvc;
import com.innobiz.orange.web.em.vo.EmSendFileDVo;
import com.innobiz.orange.web.em.vo.EmTmpFileTVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.wl.vo.WlLogFileDVo;

/** 파일 서비스 */
@Service
public class WlFileSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WlFileSvc.class);

	/** 파일업로드 태그ID */
	public static final String FILES_ID = "wlfiles";

	/** 공통 서비스 */
	@Autowired
	private WlCmSvc wlCmSvc;

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
	
	/** 보내기 서비스 */
	@Autowired
	private EmSendSvc emSendSvc;
	
	/** 문서뷰어 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** 파일업로드 서비스 */
	@Resource(name = "emFileUploadSvc")
	private EmFileUploadSvc emFileUploadSvc;
	
	/** 파일 업로드 (DISK) */
	public UploadHandler upload(HttpServletRequest request) throws CmException, IOException {
		UploadHandler uploadHandler = uploadManager.createHandler(request, "temp", "wl");
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
	public List<CommonFileVo> saveLogFile(HttpServletRequest request, String refId, QueryQueue queryQueue) throws IOException, CmException, SQLException {
		
		// Multipart 파일 리스트
		List<MultipartFile> mpFileList = ((MultipartHttpServletRequest) request).getFiles(FILES_ID + "_file");

		// 파일 배포
		distribute(request, "/wl", mpFileList);

		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
		String[] fileIds = request.getParameterValues(FILES_ID + "_fileId");
		String[] valids = request.getParameterValues(FILES_ID + "_valid");  // 신규추가된 파일이면 Y
		String[] useYns = request.getParameterValues(FILES_ID + "_useYn");  // 삭제된 파일이면 N
		String[] tmpFileId = request.getParameterValues("tmpFileId");  // 임시파일ID
		String sendNo = request.getParameter("sendNo"); // 보내기번호
		String emailNo = request.getParameter("emailNo"); // 이메일번호
		String[] logFileIds = request.getParameterValues("fileId");  // 파일ID
		
		// 메일여부
		Boolean isMail=sendNo!=null && !sendNo.isEmpty() ? null : true;
		// 테이블번호
		String no=isMail==null ? sendNo : emailNo;
		
		// 임시저장폴더
		String tmpDir=uploadManager.getTempDir();
		// 임시파일VO
		EmTmpFileTVo emTmpFileTVo;
		// 보내기 파일VO
		EmSendFileDVo emSendFileDVo;
		CmMultipartFile file;
		CommonFileVo commonFileVo;
		String newSavePath; // 파일경로
		int insertCnt = 0, dispOrdr = 0;
		if(valids!=null){
			for (int i = 0; i < valids.length; i++) {
				if ("N".equals(useYns[i])) {
					if ("Y".equals(valids[i])) continue;
	
					// 첨부파일(WL_LOG_FILE_D) 테이블 - DELETE
					WlLogFileDVo wlLogFileDVo = new WlLogFileDVo();
					wlLogFileDVo.setFileId(Integer.valueOf(fileIds[i]));
					queryQueue.delete(wlLogFileDVo);
	
					// 첨부파일(WL_LOG_FILE_D) 테이블 - SELECT
					WlLogFileDVo bullFileVo = (WlLogFileDVo) commonDao.queryVo(wlLogFileDVo);
					if (bullFileVo != null) deletedFileList.add(bullFileVo);
					continue;
				}
	
				dispOrdr++;
				if ("N".equals(valids[i])) {
					if ("".equals(fileIds[i])) continue;
					if((sendNo!=null && !sendNo.isEmpty()) || (emailNo!=null && !emailNo.isEmpty())){ 
						// 첨부파일(WL_LOG_FILE_D) 테이블 - INSERT
						WlLogFileDVo wlLogFileDVo = new WlLogFileDVo();
						emSendFileDVo = emSendSvc.getFileVo(no, Integer.parseInt(fileIds[i]), isMail);
						if(emSendFileDVo==null) continue;
						// 저장경로
						String savePath=sendNo!=null && !sendNo.isEmpty() ? emSendFileDVo.getSavePath() : emSendSvc.saveByteToFile(tmpDir, emSendFileDVo.getFileCont(), emSendFileDVo.getFileExt());
						// 파일 새이름으로 복사 후 파일 배포
						newSavePath = sendNo!=null && !sendNo.isEmpty() ? emSendSvc.copyAndDist(request, "/wl", savePath, emSendFileDVo.getFileExt()) 
								: emFileUploadSvc.copyAndDist(request, "/wl", savePath, emSendFileDVo.getFileExt());;
						
						wlLogFileDVo.setDispNm(emSendFileDVo.getDispNm());
						wlLogFileDVo.setFileExt(emSendFileDVo.getFileExt());
						wlLogFileDVo.setFileSize((long)emSendFileDVo.getFileSize());
						wlLogFileDVo.setSavePath(newSavePath);
						wlLogFileDVo.setFileId(wlCmSvc.createFileId());
						wlLogFileDVo.setRefId(refId);					
						wlLogFileDVo.setDispOrdr(dispOrdr);					
						wlLogFileDVo.setUseYn("Y");
						wlLogFileDVo.setRegrUid(userVo.getUserUid());
						wlLogFileDVo.setRegDt("sysdate");
		
						queryQueue.insert(wlLogFileDVo);
					}else{
						// 첨부파일(WL_LOG_FILE_D) 테이블 - UPDATE
						WlLogFileDVo wlLogFileDVo = new WlLogFileDVo();
						wlLogFileDVo.setFileId(Integer.valueOf(fileIds[i]));
						wlLogFileDVo.setDispOrdr(dispOrdr);
						queryQueue.update(wlLogFileDVo);
					}
					
				} else {
					// 첨부파일(WL_LOG_FILE_D) 테이블 - INSERT
					WlLogFileDVo wlLogFileDVo = new WlLogFileDVo();
					if(tmpFileId!=null && tmpFileId[i]!=null && !tmpFileId[i].isEmpty()){
						emTmpFileTVo = emFileUploadSvc.getEmTmpFileVo(Integer.parseInt(tmpFileId[i]));
						if(emTmpFileTVo==null) continue;
						
						// 파일 새이름으로 복사 후 파일 배포
						newSavePath = emFileUploadSvc.copyAndDist(request, "/wl", emTmpFileTVo.getSavePath(), emTmpFileTVo.getFileExt());
						wlLogFileDVo.setDispNm(emTmpFileTVo.getDispNm());
						wlLogFileDVo.setFileExt(emTmpFileTVo.getFileExt());
						wlLogFileDVo.setFileSize((long)emTmpFileTVo.getFileSize());
						wlLogFileDVo.setSavePath(newSavePath);
					}else if(logFileIds!=null && logFileIds[i]!=null && !logFileIds[i].isEmpty()){
						commonFileVo=getFileVo(Integer.parseInt(logFileIds[i]));
						if(commonFileVo!=null){
							// 파일 새이름으로 복사 후 파일 배포
							newSavePath = copyAndDist(request, "/wl", commonFileVo.getSavePath(), commonFileVo.getFileExt());
							wlLogFileDVo.setDispNm(commonFileVo.getDispNm());
							wlLogFileDVo.setFileExt(commonFileVo.getFileExt());
							wlLogFileDVo.setFileSize((long)commonFileVo.getFileSize());
							wlLogFileDVo.setSavePath(newSavePath);
						}
					}else{
						//if(mpFileList==null || mpFileList.size()==0 || mpFileList.size()<(insertCnt+1)) continue;
						file = (CmMultipartFile) mpFileList.get(insertCnt++);
						if(file==null) continue;
						wlLogFileDVo.setDispNm(file.getOriginalFilename());
						wlLogFileDVo.setFileExt(file.getExt());
						wlLogFileDVo.setFileSize(file.getSize());
						wlLogFileDVo.setSavePath(file.getSavePath());
					}
					wlLogFileDVo.setFileId(wlCmSvc.createFileId());
					wlLogFileDVo.setRefId(refId);					
					wlLogFileDVo.setDispOrdr(dispOrdr);					
					wlLogFileDVo.setUseYn("Y");
					wlLogFileDVo.setRegrUid(userVo.getUserUid());
					wlLogFileDVo.setRegDt("sysdate");
	
					queryQueue.insert(wlLogFileDVo);
				}
			}
		}
		return deletedFileList;
	}

	/** 첨부파일 리턴 (DB) */
	public CommonFileVo getFileVo(Integer fileId) throws SQLException {
		// 첨부파일(WL_LOG_FILE_D) 테이블 - SELECT
		WlLogFileDVo wlLogFileDVo = new WlLogFileDVo();
		wlLogFileDVo.setFileId(fileId);
		wlLogFileDVo = (WlLogFileDVo) commonDao.queryVo(wlLogFileDVo);
		return wlLogFileDVo;
	}

	/** 첨부파일 목록 리턴 (DB) */
	@SuppressWarnings("unchecked")
	public List<CommonFileVo> getFileVoList(String refId) throws SQLException {
		if (refId == null) return new ArrayList<CommonFileVo>();
		
		// 첨부파일(WL_LOG_FILE_D) 테이블 - SELECT
		WlLogFileDVo wlLogFileDVo = new WlLogFileDVo();
		wlLogFileDVo.setRefId(refId);
		return (List<CommonFileVo>) commonDao.queryList(wlLogFileDVo);
	}

	/** 첨부파일 리스트 model에 추가 */
	public void putFileListToModel(String logNo, ModelMap model, String compId) throws SQLException {
		List<CommonFileVo> fileVoList = getFileVoList(logNo);
		model.put("fileVoList", fileVoList);
		model.put("filesId", FILES_ID);
		
		// 확장자 허용제한 
		ptSysSvc.setAttachExtMap(model, compId, "wl");
		
		// 다운로드|문서뷰어 사용여부
		emAttachViewSvc.chkAttachSetup(model, compId);
	}
	
	/** 첨부파일 리스트 model에 추가 - 취합 */
	public void putFileListToModel(List<String> logNoList, ModelMap model, String compId) throws SQLException {
		if(logNoList!=null){
			List<CommonFileVo> fileVoList = null;
			List<CommonFileVo> returnList=new ArrayList<CommonFileVo>();
			for(String logNo : logNoList){
				fileVoList = getFileVoList(logNo);
				if(fileVoList==null || fileVoList.size()==0) continue;
				returnList.addAll(fileVoList);
			}
			
			model.put("fileVoList", returnList);
		}
		
		model.put("filesId", FILES_ID);
		
		// 확장자 허용제한 
		ptSysSvc.setAttachExtMap(model, compId, "wl");
		
		// 다운로드|문서뷰어 사용여부
		emAttachViewSvc.chkAttachSetup(model, compId);
	}

	/** 첨부파일 삭제 (DB) */
	public List<CommonFileVo> deleteLogFile(String refId, QueryQueue queryQueue) throws SQLException {
		// 첨부파일(WL_LOG_FILE_D) 테이블 - DELETE
		WlLogFileDVo wlLogFileDVo = new WlLogFileDVo();
		wlLogFileDVo.setRefId(refId);
		queryQueue.delete(wlLogFileDVo);

		return getFileVoList(refId);
	}

	/** 파일 참조ID 수정 (DB) */
	public void updateRefId(String refId, String newRefId, QueryQueue queryQueue) {
		WlLogFileDVo wlLogFileDVo = new WlLogFileDVo();
		wlLogFileDVo.setRefId(refId);
		wlLogFileDVo.setNewRefId(newRefId);
		queryQueue.update(wlLogFileDVo);
	}

	/** 첨부파일 복사 (DB, DISK) */
	public void copyLogFile(HttpServletRequest request, String refId, String newRefId, QueryQueue queryQueue) throws IOException, CmException, SQLException  {
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		// 첨부파일 목록
		List<CommonFileVo> fileVoList = getFileVoList(refId);

		File file = null;
		for (CommonFileVo fileVo : fileVoList) {
			file = new File(wasCopyBaseDir+fileVo.getSavePath());
			if(!file.isFile()) continue;

			// 첨부파일(WL_LOG_FILE_D) 테이블 - INSERT
			WlLogFileDVo wlLogFileDVo = new WlLogFileDVo();
			BeanUtils.copyProperties(fileVo, wlLogFileDVo, new String[]{"refId", "savePath", "regrUid", "regDt"});
			wlLogFileDVo.setFileId(wlCmSvc.createFileId());
			wlLogFileDVo.setRefId(newRefId);
			
			// 파일 새이름으로 복사 후 파일 배포
			String newSavePath = copyAndDist(request, "/wl", fileVo.getSavePath(), fileVo.getFileExt());
			
			wlLogFileDVo.setSavePath(newSavePath);
			//wlLogFileDVo.setSavePath(fileVo.getSavePath());
			wlLogFileDVo.setRegrUid(userVo.getUserUid());
			wlLogFileDVo.setRegDt("sysdate");
			queryQueue.insert(wlLogFileDVo);
		}
	}
	
	/** 파일을 새이름으로 복사 후 파일 배포 (WAS DISK) */
	private String copyAndDist(HttpServletRequest request, String path, String savePath, String ext) throws IOException, CmException {
		DistHandler distHandler = distManager.createHandler(path, SessionUtil.getLocale(request));  // 업로드 경로 설정
		
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}
				
		// 새이름
		String newSavePath = savePath.replace('\\', '/').substring(0, savePath.lastIndexOf('/')) + "/F" + StringUtil.getNextHexa() + "." + ext;
		
		// 파일복사
		distHandler.copyFile(wasCopyBaseDir+savePath, wasCopyBaseDir+newSavePath);
		
		String distPath = distHandler.addWasList(newSavePath);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("distPath = " + distPath);
		}
		distHandler.distribute();

		return newSavePath;
	}
	
	/** 파일ID 리턴 */
	public String getFilesId(){
		return FILES_ID;
	}
	
	
}
