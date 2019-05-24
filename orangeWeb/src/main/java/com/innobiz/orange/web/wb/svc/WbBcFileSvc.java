package com.innobiz.orange.web.wb.svc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
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
import com.innobiz.orange.web.em.vo.EmTmpFileTVo;
import com.innobiz.orange.web.or.vo.OrUserImgDVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.wb.vo.WbBcFileDVo;
import com.innobiz.orange.web.wb.vo.WbBcImgDVo;

/** 파일 서비스 */
@Service
public class WbBcFileSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WbBcFileSvc.class);

	/** 파일업로드 태그ID */
	public static final String FILES_ID = "bcfiles";

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

	/** 파일 저장 (DB) */
	public List<CommonFileVo> saveBcFile(HttpServletRequest request, String refId, QueryQueue queryQueue) throws IOException, CmException, SQLException {

		// Multipart 파일 리스트
		List<MultipartFile> mpFileList = ((MultipartHttpServletRequest) request).getFiles(FILES_ID + "_file");
		
		// 파일 배포
		distribute(request, "/wb/bc", mpFileList);

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
					WbBcFileDVo wbBcFileDVo = new WbBcFileDVo();
					wbBcFileDVo.setFileId(Integer.valueOf(fileIds[i]));
					queryQueue.delete(wbBcFileDVo);
	
					// 첨부파일(WB_BC_FILE_D) 테이블 - SELECT
					WbBcFileDVo bcFileVo = (WbBcFileDVo) commonDao.queryVo(wbBcFileDVo);
					if (bcFileVo != null) deletedFileList.add(bcFileVo);
					continue;
				}
	
				dispOrdr++;
				if ("N".equals(valids[i])) {
					if ("".equals(fileIds[i])) continue;
	
					// 첨부파일(WB_BC_FILE_D) 테이블 - UPDATE
					WbBcFileDVo wbBcFileDVo = new WbBcFileDVo();
					wbBcFileDVo.setFileId(Integer.valueOf(fileIds[i]));
					wbBcFileDVo.setDispOrdr(dispOrdr);
					queryQueue.update(wbBcFileDVo);
				} else {
					// 첨부파일(WB_BC_FILE_D) 테이블 - INSERT
					WbBcFileDVo wbBcFileDVo = new WbBcFileDVo();
					
					if(tmpFileId!=null && tmpFileId[i]!=null && !tmpFileId[i].isEmpty()){
						emTmpFileTVo = emFileUploadSvc.getEmTmpFileVo(Integer.parseInt(tmpFileId[i]));
						if(emTmpFileTVo==null) continue;
						
						// 파일 새이름으로 복사 후 파일 배포
						String newSavePath = emFileUploadSvc.copyAndDist(request, "/wb/bc", emTmpFileTVo.getSavePath(), emTmpFileTVo.getFileExt());
						wbBcFileDVo.setDispNm(emTmpFileTVo.getDispNm());
						wbBcFileDVo.setFileExt(emTmpFileTVo.getFileExt());
						wbBcFileDVo.setFileSize((long)emTmpFileTVo.getFileSize());
						wbBcFileDVo.setSavePath(newSavePath);
					}else{
						file = (CmMultipartFile) mpFileList.get(insertCnt++);
						if(file==null) continue;
						wbBcFileDVo.setDispNm(file.getOriginalFilename());
						wbBcFileDVo.setFileExt(file.getExt());
						wbBcFileDVo.setFileSize(file.getSize());
						wbBcFileDVo.setSavePath(file.getSavePath());
					}
					
					wbBcFileDVo.setFileId(wbCmSvc.createFileId("WB_BC_FILE_D"));
					wbBcFileDVo.setRefId(refId);
					wbBcFileDVo.setDispOrdr(dispOrdr);
					wbBcFileDVo.setUseYn("Y");
					wbBcFileDVo.setRegrUid(userVo.getUserUid());
					wbBcFileDVo.setRegDt("sysdate");
	
					queryQueue.insert(wbBcFileDVo);
				}
			}
		}
		return deletedFileList;
	}

	/** 첨부파일 리턴 */
	public CommonFileVo getFileVo(Integer fileId) throws SQLException {
		// 첨부파일(WB_BC_FILE_D) 테이블 - SELECT
		WbBcFileDVo wbBcFileDVo = new WbBcFileDVo();
		wbBcFileDVo.setFileId(fileId);
		wbBcFileDVo = (WbBcFileDVo) commonDao.queryVo(wbBcFileDVo);
		return wbBcFileDVo;
	}

	/** 첨부파일 목록 리턴 */
	@SuppressWarnings("unchecked")
	private List<CommonFileVo> getFileVoList(String refId) throws SQLException {
		if (refId == null) return new ArrayList<CommonFileVo>();
		
		// 첨부파일(WB_BC_FILE_D) 테이블 - SELECT
		WbBcFileDVo wbBcFileDVo = new WbBcFileDVo();
		wbBcFileDVo.setRefId(refId);
		return (List<CommonFileVo>) commonDao.queryList(wbBcFileDVo);
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
		WbBcFileDVo wbBcFileDVo = new WbBcFileDVo();
		wbBcFileDVo.setRefId(refId);
		queryQueue.delete(wbBcFileDVo);

		return getFileVoList(refId);
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
	
	/** 명함이미지파일 복사 (TEMP <-->  DISK) */
	public void copyBcImgTempFile(HttpServletRequest request , String newRefId, String userUid , QueryQueue queryQueue, String addUserUid) throws SQLException, IOException, CmException {
		String savePath = (String)request.getParameter("tempDir");
		// 임시파일 여부
		boolean isTmp=savePath != null && !savePath.isEmpty();
		if(!isTmp && addUserUid!=null && !addUserUid.isEmpty())
			savePath=getOrgUserImgPath(addUserUid);
		if(savePath != null && !savePath.isEmpty()){
			String path = "images/upload/wb/photo";
			
			// baseDir
			File file = new File(savePath);
			if(file.isFile()) {
				// 세션의 사용자 정보
				UserVo userVo = LoginSession.getUser(request);
				
				// 파일 새이름으로 복사 후 파일 배포
				String newSavePath = copyAndWebDisk(request, "" , path, savePath);
				// 파일(WB_BC_IMG_D) 테이블 - INSERT
				WbBcImgDVo wbBcImgDVo = new WbBcImgDVo();
				wbBcImgDVo.setBcId(newRefId);
				BufferedImage bimg = ImageIO.read(new File(savePath));
				wbBcImgDVo.setImgWdth(Integer.toString(bimg.getWidth()));
				wbBcImgDVo.setImgHght(Integer.toString(bimg.getHeight()));
				wbBcImgDVo.setImgPath(newSavePath);
				wbBcImgDVo.setModrUid(userUid != null && !"".equals(userUid) ? userUid : userVo.getUserUid());
				wbBcImgDVo.setModDt("sysdate");
				queryQueue.store(wbBcImgDVo);
				
				if(isTmp){
					//temp 폴더에 복사한 파일이 있을경우 해당 파일을 삭제한다.
					String uploadDir = savePath.substring(0,savePath.lastIndexOf("/"));
					
					File dir = new File(uploadDir);
					File[] files = dir.listFiles();
					for(int i=0;files!=null && i<files.length;i++){
						files[i].delete();
					}
					dir.delete();
				}
			}
		}
		
	}
	
	/** 명함이미지파일 복사 (DB, DISK) */
	public void copyBcImgFile(HttpServletRequest request , String refId , String newRefId, String userUid , QueryQueue queryQueue) throws SQLException, IOException, CmException {
		WbBcImgDVo searchVO = new WbBcImgDVo();
		searchVO.setBcId(refId);
		searchVO = (WbBcImgDVo)commonDao.queryVo(searchVO);
		if(searchVO != null){
			String path = "images/upload/wb/photo";
			
			// baseDir
			String webCopyBaseDir = distManager.getWebCopyBaseDir();
			if (webCopyBaseDir == null) {
				distManager.init();
				webCopyBaseDir = distManager.getWebCopyBaseDir();
			}
			
			String savePath = searchVO.getImgPath();
			File file = new File(webCopyBaseDir+savePath);
			if(file.isFile()) {
				// 세션의 사용자 정보
				UserVo userVo = LoginSession.getUser(request);
				
				// 파일 새이름으로 복사 후 파일 배포
				String newSavePath = copyAndWebDisk(request, webCopyBaseDir , path, savePath);
				// 파일(WB_BC_IMG_D) 테이블 - INSERT
				WbBcImgDVo wbBcImgDVo = new WbBcImgDVo();
				wbBcImgDVo.setBcId(newRefId);
				wbBcImgDVo.setImgWdth(searchVO.getImgWdth());
				wbBcImgDVo.setImgHght(searchVO.getImgHght());
				wbBcImgDVo.setImgPath(newSavePath);
				wbBcImgDVo.setModrUid(userUid != null && !"".equals(userUid) ? userUid : userVo.getUserUid());
				wbBcImgDVo.setModDt("sysdate");
				queryQueue.store(wbBcImgDVo);
			}
		}
		
	}
	
	/** 파일을 새이름으로 복사 후 파일 배포 (WEB DISK) */
	private String copyAndWebDisk(HttpServletRequest request, String webCopyBaseDir , String path, String savePath) throws IOException, CmException {
		DistHandler distHandler = distManager.createHandler(path, SessionUtil.getLocale(request));  // 업로드 경로 설정
		// 새이름
		String newSavePath = savePath.replace('\\', '/').substring(0, savePath.lastIndexOf('/')) + "/"+ getFileName('F' , savePath);
		// 파일복사
		distHandler.copyFile(webCopyBaseDir+savePath, newSavePath);
				
		String distPath = distHandler.addWebList(newSavePath);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("distPath = " + distPath);
		}
		distHandler.distribute();
		//return webCopyBaseDir + distPath;
		return distPath;
	}
	
	/** 명함첨부파일 복사 (DB, DISK) */
	public void copyBcFile(HttpServletRequest request, String refId, String newRefId, QueryQueue queryQueue) throws SQLException, IOException, CmException {
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}
				
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		// 게시물첨부파일 목록
		List<CommonFileVo> fileVoList = getFileVoList(refId);
		
		File file = null;
		for (CommonFileVo fileVo : fileVoList) {
			file = new File(wasCopyBaseDir+fileVo.getSavePath());
			if(!file.isFile()) continue;
			// 파일 새이름으로 복사 후 파일 배포
			String newSavePath = copyAndDist(request, "/wb/bc", fileVo.getSavePath(), fileVo.getFileExt());

			// 게시물첨부파일(BA_BULL_FILE_D) 테이블 - INSERT
			WbBcFileDVo bcFileVo = new WbBcFileDVo();
			BeanUtils.copyProperties(fileVo, bcFileVo, new String[]{"refId", "savePath", "regrUid", "regDt"});
			bcFileVo.setFileId(wbCmSvc.createFileId("WB_BC_FILE_D"));
			bcFileVo.setRefId(newRefId);
			bcFileVo.setSavePath(newSavePath);
			bcFileVo.setRegrUid(userVo.getUserUid());
			bcFileVo.setRegDt("sysdate");
			queryQueue.insert(bcFileVo);
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
	
	/** 명함 이미지 삭제 */
	public void deletePhoto(String bcId , List<CommonFileVo> deletedFileList , QueryQueue queryQueue) throws SQLException{
		// 사진(WB_BC_IMG_D) 테이블 - SELECT
		WbBcImgDVo wbBcImgDVo = new WbBcImgDVo();
		wbBcImgDVo.setBcId(bcId);
		wbBcImgDVo = (WbBcImgDVo) commonDao.queryVo(wbBcImgDVo);

		if (wbBcImgDVo != null) {
			// 사진(WB_BC_IMG_D) 테이블 - DELETE
			queryQueue.delete(wbBcImgDVo);
			// DISK에서 삭제할 파일
			WbBcFileDVo wbBcFileDVo = new WbBcFileDVo();
			
			// baseDir
			String webCopyBaseDir = distManager.getWebCopyBaseDir();
			if (webCopyBaseDir == null) {
				distManager.init();
				webCopyBaseDir = distManager.getWebCopyBaseDir();
			}
			
			wbBcFileDVo.setSavePath(webCopyBaseDir + wbBcImgDVo.getImgPath());
			deletedFileList.add(wbBcFileDVo);
		}
	}
	
	
	/** 사진 저장 */
	public CommonFileVo savePhoto(HttpServletRequest request, String bcId, String photo, WbBcImgDVo newWbBcImgDVo , QueryQueue queryQueue) throws IOException, CmException, SQLException {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		// Multipart 파일 리스트
		CmMultipartFile mpFile = (CmMultipartFile) ((MultipartHttpServletRequest) request).getFile(photo);
		if (mpFile == null) return null;

		// 업로드 경로
		String path = "/images/upload/wb/photo";

		// 배포
		DistHandler distHandler = distManager.createHandler(path, SessionUtil.getLocale(request));  // 업로드 경로 설정
		String distPath = distHandler.addWebList(mpFile.getSavePath());// file-tag 의 name
		distHandler.distribute();

		// 사진(WB_BC_IMG_D) 테이블 - SELECT
		WbBcImgDVo wbBcImgDVo = new WbBcImgDVo();
		wbBcImgDVo.setBcId(bcId);
		wbBcImgDVo = (WbBcImgDVo) commonDao.queryVo(wbBcImgDVo);

		// DISK에서 삭제할 파일
		WbBcFileDVo wbBcFileDVo = null;

		if (wbBcImgDVo != null) {
			// 사진(WB_BC_IMG_D) 테이블 - DELETE
			queryQueue.delete(wbBcImgDVo);
			// DISK에서 삭제할 파일
			wbBcFileDVo = new WbBcFileDVo();
			wbBcFileDVo.setSavePath(distManager.getWebCopyBaseDir() + wbBcImgDVo.getImgPath());
		}

		// 사진(WB_BC_IMG_D) 테이블 - INSERT
		//WbBcImgDVo newWbBcImgDVo = new WbBcImgDVo();
		newWbBcImgDVo.setBcId(bcId);
		newWbBcImgDVo.setImgPath(distPath);

		BufferedImage bimg = ImageIO.read(new File(mpFile.getSavePath()));
		newWbBcImgDVo.setImgWdth(Integer.toString(bimg.getWidth()));
		newWbBcImgDVo.setImgHght(Integer.toString(bimg.getHeight()));

		newWbBcImgDVo.setModrUid(userVo.getUserUid());
		newWbBcImgDVo.setModDt("sysdate");
		
		queryQueue.insert(newWbBcImgDVo);

		return wbBcFileDVo;
	}
	
	/** 조직도 사용자 이미지 경로 조회 */
	public String getOrgUserImgPath(String userUid) throws SQLException{
		// 사용자이미지상세(OR_USER_IMG_D) 테이블
		OrUserImgDVo orUserImgDVo = new OrUserImgDVo();
		// 겸직자 이미지 조회
		orUserImgDVo.setUserUid(userUid);
		orUserImgDVo.setUserImgTypCd("03"); // 사진
		orUserImgDVo = (OrUserImgDVo)commonDao.queryVo(orUserImgDVo);
		if(orUserImgDVo!=null && orUserImgDVo.getImgPath()!=null && !orUserImgDVo.getImgPath().isEmpty()){
			// baseDir
			String webCopyBaseDir = distManager.getWebCopyBaseDir();
			if (webCopyBaseDir == null) {
				distManager.init();
				webCopyBaseDir = distManager.getWebCopyBaseDir();
			}
			return webCopyBaseDir+orUserImgDVo.getImgPath();
		}
		
		return null;
		
	}

}
