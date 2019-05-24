package com.innobiz.orange.web.wv.svc;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
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
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.em.svc.EmFileUploadSvc;
import com.innobiz.orange.web.em.vo.EmTmpFileTVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.wv.vo.WvSurvFileDVo;


/** 게시파일 서비스 */
@Service
public class WvFileSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WvFileSvc.class);

	/** 파일업로드 태그ID */
	public static final String FILES_ID = "wvfiles";

	/** 설문 공통 서비스 */
	@Autowired
	private WvCmSvc wvCmSvc;

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
		UploadHandler uploadHandler = uploadManager.createHandler(request, "temp", "wv");
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
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}
		for (MultipartFile mpFile : mpFileList) {
			CmMultipartFile file = (CmMultipartFile) mpFile;
			String distPath = distHandler.addWasList(file.getSavePath());
			file.setSavePath(wasCopyBaseDir + distPath);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("distPath = " + distPath);
			}
		}
		distHandler.distribute();
	}

	/** 설문첨부파일 저장 (DB) */
	public List<CommonFileVo> saveSurvFile(HttpServletRequest request, String refId,
			QueryQueue queryQueue) throws Exception {

		//repetFlag 반복 [파일설정] 개별반영(indiv)/전체반영(all)
		//savePath 반복파일 모두 삭제 하기위한 path명
		// Multipart 파일 리스트
 		List<MultipartFile> mpFileList = ((MultipartHttpServletRequest) request).getFiles(FILES_ID + "_file");

		// 파일 배포
		distribute(request, "/wv", mpFileList);
		
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
		//String savePath ="";
		
		int insertCnt = 0, dispOrdr = 0;
		for (int i = 0; i < valids.length; i++) {
			if ("N".equals(useYns[i])) {
				if ("Y".equals(valids[i])) continue;
				
				
					WvSurvFileDVo wvFindFile = new WvSurvFileDVo();
					wvFindFile.setFileId(Integer.valueOf(fileIds[i]));
					WvSurvFileDVo wvFileVo = (WvSurvFileDVo) commonDao.queryVo(wvFindFile);
					if(wvFileVo != null){
						//String[] splitData = wvFileVo.getSavePath().split("\\/");
						//savePath = splitData[(splitData.length)-1];
					}

					// 설문첨부파일(wv_SURV_FILE_D) 테이블 - DELETE
					WvSurvFileDVo wvSurvFileDVo = new WvSurvFileDVo();
					wvSurvFileDVo.setFileId(Integer.valueOf(fileIds[i]));
					queryQueue.delete(wvSurvFileDVo);
					
				// 설문첨부파일(wv_SURV_FILE_D) 테이블 - SELECT
				WvSurvFileDVo survFileVo = (WvSurvFileDVo) commonDao.queryVo(wvSurvFileDVo);
				if (survFileVo != null) deletedFileList.add(survFileVo);
				continue;
			}

			dispOrdr++;
			if ("N".equals(valids[i])) {
				if ("".equals(fileIds[i])) continue;

				// 설문첨부파일(WV_SURV_FILE_D) 테이블 - UPDATE
				WvSurvFileDVo scdlFileDVo = new WvSurvFileDVo();
				scdlFileDVo.setFileId(Integer.valueOf(fileIds[i]));
				scdlFileDVo.setDispOrdr(dispOrdr);
				queryQueue.update(scdlFileDVo);
			} else {

				// 설문첨부파일(WV_SURV_FILE_D) 테이블 - INSERT
				WvSurvFileDVo wvSurvFileDVo = new WvSurvFileDVo();
				
				if(tmpFileId!=null && tmpFileId[i]!=null && !tmpFileId[i].isEmpty()){
					emTmpFileTVo = emFileUploadSvc.getEmTmpFileVo(Integer.parseInt(tmpFileId[i]));
					if(emTmpFileTVo==null) continue;
					
					// 파일 새이름으로 복사 후 파일 배포
					String newSavePath = emFileUploadSvc.copyAndDist(request, "/wv", emTmpFileTVo.getSavePath(), emTmpFileTVo.getFileExt());
					wvSurvFileDVo.setDispNm(emTmpFileTVo.getDispNm());
					wvSurvFileDVo.setFileExt(emTmpFileTVo.getFileExt());
					wvSurvFileDVo.setFileSize((long)emTmpFileTVo.getFileSize());
					wvSurvFileDVo.setSavePath(newSavePath);
				}else{
					file = (CmMultipartFile) mpFileList.get(insertCnt++);
					if(file==null) continue;
					wvSurvFileDVo.setDispNm(file.getOriginalFilename());
					wvSurvFileDVo.setFileExt(file.getExt());
					wvSurvFileDVo.setFileSize(file.getSize());
					wvSurvFileDVo.setSavePath(file.getSavePath());
				}
				wvSurvFileDVo.setFileId(wvCmSvc.createFileId("WV_SURV_FILE_D"));
				wvSurvFileDVo.setRefId(refId);
				wvSurvFileDVo.setDispOrdr(dispOrdr);
				wvSurvFileDVo.setUseYn("Y");
				wvSurvFileDVo.setRegrUid(userVo.getUserUid());
				wvSurvFileDVo.setRegDt("sysdate");

				queryQueue.insert(wvSurvFileDVo);
			}
		}
		return deletedFileList;
	}

	/** 설문첨부파일 리턴 (DB) */
	public CommonFileVo getFileVo(Integer fileId) throws Exception {
		// 설문첨부파일(WV_SURV_FILE_D) 테이블 - SELECT
		WvSurvFileDVo schdlFileDVo = new WvSurvFileDVo();
		schdlFileDVo.setFileId(fileId);
		schdlFileDVo = (WvSurvFileDVo) commonDao.queryVo(schdlFileDVo);
		return schdlFileDVo;
	}

	/** 설문첨부파일 목록 리턴 (DB) */
	@SuppressWarnings("unchecked")
	public List<CommonFileVo> getFileVoList(String refId) throws Exception {
		if (refId == null) return new ArrayList<CommonFileVo>();
		
		// 설문첨부파일(WV_SURV_FILE_D) 테이블 - SELECT
		WvSurvFileDVo schdlFileDVo = new WvSurvFileDVo();
		schdlFileDVo.setRefId(refId);
		return (List<CommonFileVo>) commonDao.queryList(schdlFileDVo);
	}

	/** 설문첨부파일 리스트 model에 추가 */
	public void putFileListToModel(String refId, ModelMap model, String compId) throws Exception {
		List<CommonFileVo> fileVoList = getFileVoList(refId);
		model.put("fileVoList", fileVoList);
		model.put("filesId", FILES_ID);
		// 확장자 허용제한 
		ptSysSvc.setAttachExtMap(model, compId, "wv");
		
		// 다운로드|문서뷰어 사용여부
		emAttachViewSvc.chkAttachSetup(model, compId);
	}

	/** 설문첨부파일 삭제 (DB) */
	public List<CommonFileVo> deleteWcFile(String refId, QueryQueue queryQueue) throws Exception {
		// 설문첨부파일(WV_SURV_FILE_D) 테이블 - DELETE
		WvSurvFileDVo WvSurvFileDVo = new WvSurvFileDVo();
		WvSurvFileDVo.setRefId(refId);
		queryQueue.delete(WvSurvFileDVo);

		return getFileVoList(refId);
	}
	
	/** 사진 저장 */
	public CommonFileVo savePhoto(HttpServletRequest request, String photo, int imgfileId, 
			WvSurvFileDVo newWvSurvDVo , QueryQueue queryQueue) throws IOException, CmException, SQLException {
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);

		// Multipart 파일 리스트
		CmMultipartFile mpFile = (CmMultipartFile) ((MultipartHttpServletRequest) request).getFile(photo);
		if (mpFile == null) return null;

		// 업로드 경로
		String path = "/images/upload/wv/image";

		// 배포
		DistHandler distHandler = distManager.createHandler(path, SessionUtil.getLocale(request));  // 업로드 경로 설정
		String distPath = distHandler.addWebList(mpFile.getSavePath());// file-tag 의 name
		distHandler.distribute();
		
		if(imgfileId!=-1){
			WvSurvFileDVo wvSurvFileDVo = new WvSurvFileDVo();
			wvSurvFileDVo.setFileId(imgfileId);
			wvSurvFileDVo = (WvSurvFileDVo) commonDao.queryVo(wvSurvFileDVo);
			
			if (wvSurvFileDVo != null) {				
				queryQueue.delete(wvSurvFileDVo);			
			}
		}		
			
		try {
			newWvSurvDVo.setSavePath(distPath);		
			newWvSurvDVo.setDispOrdr(0);	
			newWvSurvDVo.setFileId(wvCmSvc.createFileId("WV_SURV_FILE_D"));
			newWvSurvDVo.setDispNm(mpFile.getOriginalFilename());
			newWvSurvDVo.setFileExt(mpFile.getExt());
			newWvSurvDVo.setFileSize(mpFile.getSize());
			newWvSurvDVo.setRefId("-1");
			newWvSurvDVo.setUseYn("Y");	
			newWvSurvDVo.setImgUrl("");
			queryQueue.insert(newWvSurvDVo);
		} catch (Exception e) {		
			e.printStackTrace();
		}		
		

		return newWvSurvDVo;
	}
}
