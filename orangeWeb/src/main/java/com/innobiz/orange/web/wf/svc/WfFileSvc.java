package com.innobiz.orange.web.wf.svc;

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
import com.innobiz.orange.web.wf.vo.WfWorksFileDVo;

/** 파일 서비스 */
@Service
public class WfFileSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WfFileSvc.class);

	/** 파일업로드 태그ID */
	public static final String FILES_ID = "wffiles";
	
	/** 공통 서비스 */
	@Autowired
	private WfCmSvc wfCmSvc;
	
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
	
	/** 보내기 서비스 */
	@Autowired
	private EmSendSvc emSendSvc;
	
	/** 파일 업로드 (DISK) */
	public UploadHandler upload(HttpServletRequest request) throws CmException, IOException {
		UploadHandler uploadHandler = uploadManager.createHandler(request, "temp", "wf");
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
	
	/** 파일 삭제 (Web) */
	public void deleteWebFiles(List<String> deleteFilePathList) {
		if (deleteFilePathList == null || deleteFilePathList.size() == 0) return;
		// baseDir
		String webCopyBaseDir = distManager.getWebCopyBaseDir();
		if (webCopyBaseDir == null) {
			distManager.init();
			webCopyBaseDir = distManager.getWebCopyBaseDir();
		}
		for (String savePath : deleteFilePathList) {
			boolean deleted = new File(webCopyBaseDir+savePath).delete();
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
	public List<CommonFileVo> saveFile(HttpServletRequest request, String formNo, String refId, QueryQueue queryQueue) throws IOException, CmException, SQLException {
		
		// Multipart 파일 리스트
		List<MultipartFile> mpFileList = ((MultipartHttpServletRequest) request).getFiles(FILES_ID + "_file");

		// 파일 배포
		distribute(request, "/wf", mpFileList);

		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
		String[] fileIds = request.getParameterValues(FILES_ID + "_fileId");
		String[] valids = request.getParameterValues(FILES_ID + "_valid");  // 신규추가된 파일이면 Y
		String[] useYns = request.getParameterValues(FILES_ID + "_useYn");  // 삭제된 파일이면 N
		String[] tmpFileId = request.getParameterValues("tmpFileId");  // 임시파일ID
		String sendNo = request.getParameter("sendNo"); // 보내기번호
		String emailNo = request.getParameter("emailNo"); // 이메일번호
		
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
		int insertCnt = 0, dispOrdr = 0;
		if(valids!=null){
			for (int i = 0; i < valids.length; i++) {
				if ("N".equals(useYns[i])) {
					if ("Y".equals(valids[i])) continue;
	
					// 첨부파일(BA_BULL_FILE_D) 테이블 - DELETE
					WfWorksFileDVo wfFormFileDVo = new WfWorksFileDVo(formNo);
					wfFormFileDVo.setFileId(Integer.valueOf(fileIds[i]));
					queryQueue.delete(wfFormFileDVo);
	
					// 첨부파일(BA_BULL_FILE_D) 테이블 - SELECT
					WfWorksFileDVo bullFileVo = (WfWorksFileDVo) commonDao.queryVo(wfFormFileDVo);
					if (bullFileVo != null) deletedFileList.add(bullFileVo);
					continue;
				}
	
				dispOrdr++;
				if ("N".equals(valids[i])) {
					if ("".equals(fileIds[i])) continue;
					if((sendNo!=null && !sendNo.isEmpty()) || (emailNo!=null && !emailNo.isEmpty())){ 
						// 첨부파일(BA_BULL_FILE_D) 테이블 - INSERT
						WfWorksFileDVo wfFormFileDVo = new WfWorksFileDVo(formNo);
						emSendFileDVo = emSendSvc.getFileVo(no, Integer.parseInt(fileIds[i]), isMail);
						if(emSendFileDVo==null) continue;
						// 저장경로
						String savePath=sendNo!=null && !sendNo.isEmpty() ? emSendFileDVo.getSavePath() : emSendSvc.saveByteToFile(tmpDir, emSendFileDVo.getFileCont(), emSendFileDVo.getFileExt());
						// 파일 새이름으로 복사 후 파일 배포
						String newSavePath = sendNo!=null && !sendNo.isEmpty() ? emSendSvc.copyAndDist(request, "/wf", savePath, emSendFileDVo.getFileExt()) 
								: emFileUploadSvc.copyAndDist(request, "/wf", savePath, emSendFileDVo.getFileExt());
						
						wfFormFileDVo.setDispNm(emSendFileDVo.getDispNm());
						wfFormFileDVo.setFileExt(emSendFileDVo.getFileExt());
						wfFormFileDVo.setFileSize((long)emSendFileDVo.getFileSize());
						wfFormFileDVo.setSavePath(newSavePath);
						wfFormFileDVo.setFileId(wfCmSvc.createFileId());
						wfFormFileDVo.setRefId(refId);					
						wfFormFileDVo.setDispOrdr(dispOrdr);					
						wfFormFileDVo.setUseYn("Y");
						wfFormFileDVo.setRegrUid(userVo.getUserUid());
						wfFormFileDVo.setRegDt("sysdate");
		
						queryQueue.insert(wfFormFileDVo);
					}else{
						// 첨부파일(BA_BULL_FILE_D) 테이블 - UPDATE
						WfWorksFileDVo wfFormFileDVo = new WfWorksFileDVo(formNo);
						wfFormFileDVo.setFileId(Integer.valueOf(fileIds[i]));
						wfFormFileDVo.setDispOrdr(dispOrdr);
						queryQueue.update(wfFormFileDVo);
					}
					
				} else {
					// 첨부파일(BA_BULL_FILE_D) 테이블 - INSERT
					WfWorksFileDVo wfFormFileDVo = new WfWorksFileDVo(formNo);
					if(tmpFileId!=null && tmpFileId[i]!=null && !tmpFileId[i].isEmpty()){
						emTmpFileTVo = emFileUploadSvc.getEmTmpFileVo(Integer.parseInt(tmpFileId[i]));
						if(emTmpFileTVo==null) continue;
						
						// 파일 새이름으로 복사 후 파일 배포
						String newSavePath = emFileUploadSvc.copyAndDist(request, "/wf", emTmpFileTVo.getSavePath(), emTmpFileTVo.getFileExt());
						wfFormFileDVo.setDispNm(emTmpFileTVo.getDispNm());
						wfFormFileDVo.setFileExt(emTmpFileTVo.getFileExt());
						wfFormFileDVo.setFileSize((long)emTmpFileTVo.getFileSize());
						wfFormFileDVo.setSavePath(newSavePath);
					}else{
						//if(mpFileList==null || mpFileList.size()==0 || mpFileList.size()<(insertCnt+1)) continue;
						file = (CmMultipartFile) mpFileList.get(insertCnt++);
						if(file==null) continue;
						wfFormFileDVo.setDispNm(file.getOriginalFilename());
						wfFormFileDVo.setFileExt(file.getExt());
						wfFormFileDVo.setFileSize(file.getSize());
						wfFormFileDVo.setSavePath(file.getSavePath());
					}
					wfFormFileDVo.setFileId(wfCmSvc.createFileId());
					wfFormFileDVo.setRefId(refId);					
					wfFormFileDVo.setDispOrdr(dispOrdr);					
					wfFormFileDVo.setUseYn("Y");
					wfFormFileDVo.setRegrUid(userVo.getUserUid());
					wfFormFileDVo.setRegDt("sysdate");
	
					queryQueue.insert(wfFormFileDVo);
				}
			}
		}
		return deletedFileList;
	}

	/** 첨부파일 리턴 (DB) */
	public CommonFileVo getFileVo(String formNo, Integer fileId) throws SQLException {
		// 첨부파일(BA_BULL_FILE_D) 테이블 - SELECT
		WfWorksFileDVo wfFormFileDVo = new WfWorksFileDVo(formNo);
		wfFormFileDVo.setFileId(fileId);
		wfFormFileDVo = (WfWorksFileDVo) commonDao.queryVo(wfFormFileDVo);
		return wfFormFileDVo;
	}

	/** 첨부파일 목록 리턴 (DB) */
	@SuppressWarnings("unchecked")
	public List<CommonFileVo> getFileVoList(String formNo, String refId) throws SQLException {
		if (refId == null) return new ArrayList<CommonFileVo>();
		
		// 첨부파일(BA_BULL_FILE_D) 테이블 - SELECT
		WfWorksFileDVo wfFormFileDVo = new WfWorksFileDVo(formNo);
		wfFormFileDVo.setRefId(refId);
		return (List<CommonFileVo>) commonDao.queryList(wfFormFileDVo);
	}
	
	/** 첨부파일 목록 건수 리턴 (DB) */
	public Integer getFileVoListCnt(String formNo, String refId) throws SQLException {
		if (refId == null) return 0;
		
		// 첨부파일(BA_BULL_FILE_D) 테이블 - SELECT
		WfWorksFileDVo wfFormFileDVo = new WfWorksFileDVo(formNo);
		wfFormFileDVo.setRefId(refId);
		return commonDao.count(wfFormFileDVo);
	}

	/** 첨부파일 리스트 model에 추가 */
	public void putFileListToModel(String formNo, String workNo, ModelMap model, String compId, String mdTypCd) throws SQLException {
		List<CommonFileVo> fileVoList = getFileVoList(formNo, workNo);
		model.put("fileVoList", fileVoList);
		model.put("filesId", FILES_ID);
		
		if(mdTypCd==null || mdTypCd.isEmpty() || "no".equals(mdTypCd)) mdTypCd="wf";
		// 확장자 허용제한 
		ptSysSvc.setAttachExtMap(model, compId, mdTypCd);
		
		// 다운로드|문서뷰어 사용여부
		emAttachViewSvc.chkAttachSetup(model, compId);
		
		model.put("mdTypCd", mdTypCd);
	}

	/** 첨부파일 삭제 (DB) */
	public List<CommonFileVo> deleteFile(String formNo, String refId, QueryQueue queryQueue) throws SQLException {
		// 첨부파일(BA_BULL_FILE_D) 테이블 - DELETE
		WfWorksFileDVo wfFormFileDVo = new WfWorksFileDVo(formNo);
		wfFormFileDVo.setRefId(refId);
		queryQueue.delete(wfFormFileDVo);

		return getFileVoList(formNo, refId);
	}

	/** 파일 참조ID 수정 (DB) */
	public void updateRefId(String formNo, String refId, String newRefId, QueryQueue queryQueue) {
		WfWorksFileDVo wfFormFileDVo = new WfWorksFileDVo(formNo);
		wfFormFileDVo.setRefId(refId);
		wfFormFileDVo.setNewRefId(newRefId);
		queryQueue.update(wfFormFileDVo);
	}

	/** 첨부파일 복사 (DB, DISK) */
	public void copyFile(HttpServletRequest request, String formNo, String refId, String newRefId, QueryQueue queryQueue) throws IOException, CmException, SQLException  {
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		// 첨부파일 목록
		List<CommonFileVo> fileVoList = getFileVoList(formNo, refId);

		File file = null;
		for (CommonFileVo fileVo : fileVoList) {
			file = new File(wasCopyBaseDir+fileVo.getSavePath());
			if(!file.isFile()) continue;

			// 첨부파일(BA_BULL_FILE_D) 테이블 - INSERT
			WfWorksFileDVo wfFormFileDVo = new WfWorksFileDVo(formNo);
			BeanUtils.copyProperties(fileVo, wfFormFileDVo, new String[]{"refId", "savePath", "regrUid", "regDt"});
			wfFormFileDVo.setFileId(wfCmSvc.createFileId());
			wfFormFileDVo.setRefId(newRefId);
			
			// 파일 새이름으로 복사 후 파일 배포
			String newSavePath = copyAndDist(request, "/wf", fileVo.getSavePath(), fileVo.getFileExt());
			
			wfFormFileDVo.setSavePath(newSavePath);
			//wfFormFileDVo.setSavePath(fileVo.getSavePath());
			wfFormFileDVo.setRegrUid(userVo.getUserUid());
			wfFormFileDVo.setRegDt("sysdate");
			queryQueue.insert(wfFormFileDVo);
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
	
	/** 파일을 새이름으로 복사 후 파일 배포 (WEB DISK) */
	public String copyAndWebDisk(HttpServletRequest request, String path, String savePath) throws IOException, CmException {
		DistHandler distHandler = distManager.createHandler(path, SessionUtil.getLocale(request));  // 업로드 경로 설정

		// 새이름
		String newSavePath = savePath.replace('\\', '/').substring(0, savePath.lastIndexOf('/')) + "/"+ getFileName('F' , savePath);
		// 파일복사
		distHandler.copyFile(savePath, newSavePath);
				
		String distPath = distHandler.addWebList(newSavePath);
		
		distHandler.distribute();
		//return webCopyBaseDir + distPath;
		return distPath;
	}
	
	/** 파일명 새로 생성 */
	public String getFileName(char prefix , String orginalDir){
		String fileName;
		int p = orginalDir.replace('\\', '/').lastIndexOf('/');
		if (p >= 0) fileName = orginalDir.substring(p + 1);
		else fileName = orginalDir;
		p = fileName.lastIndexOf('.');
		String ext = p <= 0 ? "" : fileName.substring(p);
		String newfileName = prefix + StringUtil.getNextHexa()+ext;
		
		return newfileName;
	}
	
	/** 파일ID 리턴 */
	public String getFilesId(){
		return FILES_ID;
	}

}
