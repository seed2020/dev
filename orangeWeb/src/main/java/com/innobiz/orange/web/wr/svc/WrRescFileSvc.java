package com.innobiz.orange.web.wr.svc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
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
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.wr.vo.WrRescImgDVo;

/** 게시파일 서비스 */
@Service
public class WrRescFileSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WrRescFileSvc.class);

	/** 파일업로드 태그ID */
	public static final String FILES_ID = "rescfiles";

	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;

	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;

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
	
	/** 사진 삭제 **/
	public void delPhoto(QueryQueue queryQueue , String rescMngId) throws SQLException{
		// 사진(WB_BC_IMG_D) 테이블 - SELECT
		WrRescImgDVo wrRescImgDVo = new WrRescImgDVo();
		wrRescImgDVo.setRescMngId(rescMngId);
		wrRescImgDVo = (WrRescImgDVo) commonDao.queryVo(wrRescImgDVo);
		
		// DISK에서 삭제할 파일
		if (wrRescImgDVo != null) {
			// 사진(WR_RESC_IMG_D) 테이블 - DELETE
			queryQueue.delete(wrRescImgDVo);
						
			// DISK에서 삭제할 파일
			// baseDir
			String webCopyBaseDir = distManager.getWebCopyBaseDir();
			if (webCopyBaseDir == null) {
				distManager.init();
				webCopyBaseDir = distManager.getWebCopyBaseDir();
			}
			String savePath = webCopyBaseDir+wrRescImgDVo.getImgPath();
			
			boolean deleted = new File(savePath).delete();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("savePath = " + savePath + ", deleted = " + deleted);
			}
		}
	}
	
	/** 사진 저장 */
	public void savePhoto(HttpServletRequest request, String rescMngId, String photo, WrRescImgDVo newWrRescImgDVo , QueryQueue queryQueue) throws IOException, CmException, SQLException {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		// Multipart 파일 리스트
		CmMultipartFile mpFile = (CmMultipartFile) ((MultipartHttpServletRequest) request).getFile(photo);
		
		if (mpFile != null){
			// 업로드 경로
			String path = "/images/upload/wr/photo";
	
			// 배포
			DistHandler distHandler = distManager.createHandler(path, SessionUtil.getLocale(request));  // 업로드 경로 설정
			String distPath = distHandler.addWebList(mpFile.getSavePath());// file-tag 의 name
			distHandler.distribute();
	
			this.delPhoto(queryQueue , rescMngId);
	
			// 사진(WR_RESC_IMG_D) 테이블 - INSERT
			newWrRescImgDVo.setRescMngId(rescMngId);
			newWrRescImgDVo.setImgPath(distPath);
	
			BufferedImage bimg = ImageIO.read(new File(mpFile.getSavePath()));
			newWrRescImgDVo.setImgWdth(bimg.getWidth());
			newWrRescImgDVo.setImgHght(bimg.getHeight());
	
			newWrRescImgDVo.setModrUid(userVo.getUserUid());
			newWrRescImgDVo.setModDt("sysdate");
			
			queryQueue.insert(newWrRescImgDVo);
		}
	}
}
