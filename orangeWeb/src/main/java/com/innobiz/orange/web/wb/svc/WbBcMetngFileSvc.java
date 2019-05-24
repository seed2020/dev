package com.innobiz.orange.web.wb.svc;

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
import com.innobiz.orange.web.wb.vo.WbBcMetngFileDVo;

/** 게시파일 서비스 */
@Service
public class WbBcMetngFileSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WbBcFileSvc.class);

	/** 파일업로드 태그ID */
	public static final String FILES_ID = "mtfiles";

	/** 게시판 공통 서비스 */
	@Autowired
	private WbCmSvc wbCmSvc;

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
	
	/** 파일 업로드 */
	public UploadHandler upload(HttpServletRequest request) throws CmException, IOException {
		UploadHandler uploadHandler = uploadManager.createHandler(request, "temp", "wb");
		uploadHandler.upload(); // 업로드 파일 정보
		return uploadHandler;
	}

	/** 파일 삭제 (DISK) */
	public void deleteDiskFiles(List<CommonFileVo> deletedFileList) {
		if (deletedFileList == null || deletedFileList.size() == 0) return;

		for (CommonFileVo fileVo : deletedFileList) {
			String savePath = fileVo.getSavePath();
			boolean deleted = new File(savePath).delete();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("savePath = " + savePath + ", deleted = " + deleted);
			}
		}
	}

	/** 파일 배포 */
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

	/** 파일 저장 (DB) */
	public List<CommonFileVo> saveBcFile(HttpServletRequest request, String refId, QueryQueue queryQueue) throws SQLException, IOException, CmException {

		// Multipart 파일 리스트
		List<MultipartFile> mpFileList = ((MultipartHttpServletRequest) request).getFiles(FILES_ID + "_file");

		// 파일 배포
		distribute(request, "/wb/mt", mpFileList);

		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
		String[] fileIds = request.getParameterValues(FILES_ID + "_fileId");
		String[] valids = request.getParameterValues(FILES_ID + "_valid");
		String[] useYns = request.getParameterValues(FILES_ID + "_useYn");
		String[] tmpFileId = request.getParameterValues("tmpFileId");  // 임시파일ID
		// 임시파일VO
		EmTmpFileTVo emTmpFileTVo;
		CmMultipartFile file;
		int insertCnt = 0, dispOrdr = 0;
		if(valids != null){
			for (int i = 0; i < valids.length; i++) {
				if ("N".equals(useYns[i])) {
					if ("Y".equals(valids[i])) continue;
	
					// 첨부파일(WB_BC_FILE_D) 테이블 - DELETE
					WbBcMetngFileDVo wbBcMetngFileDVo = new WbBcMetngFileDVo();
					wbBcMetngFileDVo.setFileId(Integer.valueOf(fileIds[i]));
					queryQueue.delete(wbBcMetngFileDVo);
	
					// 첨부파일(WB_BC_FILE_D) 테이블 - SELECT
					WbBcMetngFileDVo bcFileVo = (WbBcMetngFileDVo) commonDao.queryVo(wbBcMetngFileDVo);
					if (bcFileVo != null) deletedFileList.add(bcFileVo);
					continue;
				}
	
				dispOrdr++;
				if ("N".equals(valids[i])) {
					if ("".equals(fileIds[i])) continue;
	
					// 첨부파일(WB_BC_FILE_D) 테이블 - UPDATE
					WbBcMetngFileDVo wbBcMetngFileDVo = new WbBcMetngFileDVo();
					wbBcMetngFileDVo.setFileId(Integer.valueOf(fileIds[i]));
					wbBcMetngFileDVo.setDispOrdr(dispOrdr);
					queryQueue.update(wbBcMetngFileDVo);
				} else {
					// 첨부파일(WB_BC_FILE_D) 테이블 - INSERT
					WbBcMetngFileDVo wbBcMetngFileDVo = new WbBcMetngFileDVo();
					
					if(tmpFileId!=null && tmpFileId[i]!=null && !tmpFileId[i].isEmpty()){
						emTmpFileTVo = emFileUploadSvc.getEmTmpFileVo(Integer.parseInt(tmpFileId[i]));
						if(emTmpFileTVo==null) continue;
						
						// 파일 새이름으로 복사 후 파일 배포
						String newSavePath = emFileUploadSvc.copyAndDist(request, "/wb/mt", emTmpFileTVo.getSavePath(), emTmpFileTVo.getFileExt());
						wbBcMetngFileDVo.setDispNm(emTmpFileTVo.getDispNm());
						wbBcMetngFileDVo.setFileExt(emTmpFileTVo.getFileExt());
						wbBcMetngFileDVo.setFileSize((long)emTmpFileTVo.getFileSize());
						wbBcMetngFileDVo.setSavePath(newSavePath);
					}else{
						file = (CmMultipartFile) mpFileList.get(insertCnt++);
						if(file==null) continue;
						wbBcMetngFileDVo.setDispNm(file.getOriginalFilename());
						wbBcMetngFileDVo.setFileExt(file.getExt());
						wbBcMetngFileDVo.setFileSize(file.getSize());
						wbBcMetngFileDVo.setSavePath(file.getSavePath());
					}
					wbBcMetngFileDVo.setFileId(wbCmSvc.createFileId("WB_BC_METNG_FLD_B"));
					wbBcMetngFileDVo.setRefId(refId);
					wbBcMetngFileDVo.setDispOrdr(dispOrdr);
					wbBcMetngFileDVo.setUseYn("Y");
					wbBcMetngFileDVo.setRegrUid(userVo.getUserUid());
					wbBcMetngFileDVo.setRegDt("sysdate");
	
					queryQueue.insert(wbBcMetngFileDVo);
				}
			}
		}
		return deletedFileList;
	}

	/** 첨부파일 리턴 */
	public CommonFileVo getFileVo(Integer fileId) throws SQLException {
		// 첨부파일(WB_BC_FILE_D) 테이블 - SELECT
		WbBcMetngFileDVo wbBcMetngFileDVo = new WbBcMetngFileDVo();
		wbBcMetngFileDVo.setFileId(fileId);
		wbBcMetngFileDVo = (WbBcMetngFileDVo) commonDao.queryVo(wbBcMetngFileDVo);
		return wbBcMetngFileDVo;
	}

	/** 첨부파일 목록 리턴 */
	@SuppressWarnings("unchecked")
	private List<CommonFileVo> getFileVoList(String refId) throws SQLException {
		if (refId == null) return new ArrayList<CommonFileVo>();
		
		// 첨부파일(WB_BC_FILE_D) 테이블 - SELECT
		WbBcMetngFileDVo wbBcMetngFileDVo = new WbBcMetngFileDVo();
		wbBcMetngFileDVo.setRefId(refId);
		return (List<CommonFileVo>) commonDao.queryList(wbBcMetngFileDVo);
	}

	/** 첨부파일 리스트 model에 추가 */
	public void putFileListToModel(String refId, ModelMap model, String compId) throws SQLException {
		List<CommonFileVo> fileVoList = getFileVoList(refId);
		model.put("fileVoList", fileVoList);
		model.put("filesId", FILES_ID);
		// 확장자 허용제한 
		ptSysSvc.setAttachExtMap(model, compId, "wb");
		
		// 다운로드|문서뷰어 사용여부
		emAttachViewSvc.chkAttachSetup(model, compId);
	}

	/** 첨부파일 삭제 (DB) */
	public List<CommonFileVo> deleteBcFile(String refId, QueryQueue queryQueue) throws SQLException {
		// 첨부파일(WB_BC_FILE_D) 테이블 - DELETE
		WbBcMetngFileDVo wbBcMetngFileDVo = new WbBcMetngFileDVo();
		wbBcMetngFileDVo.setRefId(refId);
		queryQueue.delete(wbBcMetngFileDVo);

		return getFileVoList(refId);
	}

}
