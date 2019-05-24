package com.innobiz.orange.web.bb.svc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.innobiz.orange.web.bb.vo.BaBullFileDVo;
import com.innobiz.orange.web.bb.vo.BaBullPhotoDVo;
import com.innobiz.orange.web.bb.vo.BbBullLVo;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.CmMultipartFile;
import com.innobiz.orange.web.cm.files.DistHandler;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;

/** 게시물사진 서비스 */
@Service
public class BbBullPhotoSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(BbBullPhotoSvc.class);

	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;

	/** 게시물사진 리턴 */
	public BaBullPhotoDVo getPhotoVo(Integer bullId) throws SQLException {
		// 게시물사진(BA_BULL_PHOTO_D) 테이블 - SELECT
		BaBullPhotoDVo baBullPhotoDVo = new BaBullPhotoDVo();
		baBullPhotoDVo.setBullId(bullId);
		return (BaBullPhotoDVo) commonDao.queryVo(baBullPhotoDVo);
	}

	/** 게시물사진 세팅 */
	public void setPhotoVo(List<BbBullLVo> bbBullLVoList) throws SQLException {
		for (BbBullLVo bbBullLVo : bbBullLVoList) {
			bbBullLVo.setPhotoVo(getPhotoVo(bbBullLVo.getBullId()));
		}
	}

	/** 게시물사진 저장 */
	public CommonFileVo savePhoto(HttpServletRequest request, Integer bullId, String photo, QueryQueue queryQueue) throws IOException, CmException, SQLException {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		// Multipart 파일 리스트
		CmMultipartFile mpFile = (CmMultipartFile) ((MultipartHttpServletRequest) request).getFile(photo);
		if (mpFile == null) return null;

		// 업로드 경로
		String path = "/images/upload/bb/photo";

		// 배포
		DistHandler distHandler = distManager.createHandler(path, SessionUtil.getLocale(request));  // 업로드 경로 설정
		String distPath = distHandler.addWebList(mpFile.getSavePath());                             // file-tag 의 name
		distHandler.distribute();

		// 게시물사진(BA_BULL_PHOTO_D) 테이블 - SELECT
		BaBullPhotoDVo baBullPhotoDVo = new BaBullPhotoDVo();
		baBullPhotoDVo.setBullId(bullId);
		baBullPhotoDVo = (BaBullPhotoDVo) commonDao.queryVo(baBullPhotoDVo);

		// DISK에서 삭제할 파일
		BaBullFileDVo baBullFileDVo = null;

		if (baBullPhotoDVo != null) {
			// 게시물사진(BA_BULL_PHOTO_D) 테이블 - DELETE
			queryQueue.delete(baBullPhotoDVo);
			// DISK에서 삭제할 파일
			baBullFileDVo = new BaBullFileDVo();
			baBullFileDVo.setSavePath(distManager.getWebCopyBaseDir() + baBullPhotoDVo.getSavePath());
		}

		// 게시물사진(BA_BULL_PHOTO_D) 테이블 - INSERT
		BaBullPhotoDVo newBullPhotoVo = new BaBullPhotoDVo();
		newBullPhotoVo.setBullId(bullId);
		newBullPhotoVo.setFileExt(mpFile.getExt());
		newBullPhotoVo.setFileSize(mpFile.getSize());
		newBullPhotoVo.setSavePath(distPath);

		BufferedImage bimg = ImageIO.read(new File(mpFile.getSavePath()));
		newBullPhotoVo.setImgWdth(bimg.getWidth());
		newBullPhotoVo.setImgHght(bimg.getHeight());

		newBullPhotoVo.setRegrUid(userVo.getUserUid());
		newBullPhotoVo.setRegDt("sysdate");

		queryQueue.insert(newBullPhotoVo);

		return baBullFileDVo;
	}

	/** 게시물사진 삭제 (DB) */
	public CommonFileVo deletePhoto(Integer bullId, QueryQueue queryQueue) throws SQLException {
		// 게시물사진(BA_BULL_PHOTO_D) 테이블 - SELECT
		BaBullPhotoDVo baBullPhotoDVo = new BaBullPhotoDVo();
		baBullPhotoDVo.setBullId(bullId);
		baBullPhotoDVo = (BaBullPhotoDVo) commonDao.queryVo(baBullPhotoDVo);
		// 게시물사진(BA_BULL_PHOTO_D) 테이블 - DELETE
		if(baBullPhotoDVo != null){
			queryQueue.delete(baBullPhotoDVo);

			BaBullFileDVo baBullFileDVo = new BaBullFileDVo();
			baBullFileDVo.setSavePath(distManager.getWebCopyBaseDir() + baBullPhotoDVo.getSavePath());
			return baBullFileDVo;
		}else{
			return null;
		}
	}

	/** 게시물첨부파일 복사 (DB, DISK) */
	public void copyPhoto(HttpServletRequest request, Integer bullId, Integer newBullId, QueryQueue queryQueue) throws SQLException, IOException, CmException {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		// 게시물사진(BA_BULL_PHOTO_D) 테이블 - SELECT
		BaBullPhotoDVo photoVo = new BaBullPhotoDVo();
		photoVo.setBullId(bullId);
		photoVo = (BaBullPhotoDVo) commonDao.queryVo(photoVo);

		if(photoVo != null)
		{
			// 파일 새이름으로 복사 후 파일 배포
			String newSavePath = copyAndDist(request, photoVo.getSavePath(), photoVo.getFileExt());
	
			// 게시물사진(BA_BULL_PHOTO_D) 테이블 - INSERT
			BaBullPhotoDVo baBullPhotoDVo = new BaBullPhotoDVo();
			BeanUtils.copyProperties(photoVo, baBullPhotoDVo, new String[]{"bullId", "savePath", "regrUid", "regDt"});
			baBullPhotoDVo.setBullId(newBullId);
			baBullPhotoDVo.setSavePath(newSavePath);
			baBullPhotoDVo.setRegrUid(userVo.getUserUid());
			baBullPhotoDVo.setRegDt("sysdate");
			queryQueue.insert(baBullPhotoDVo);
		}
	}

	/** 파일을 새이름으로 복사 후 파일 배포 (DISK) */
	private String copyAndDist(HttpServletRequest request, String savePath, String ext) throws IOException, CmException {
		// 업로드 경로
		String path = "/images/upload/bb/photo";
		
		DistHandler distHandler = distManager.createHandler(path, SessionUtil.getLocale(request));  // 업로드 경로 설정

		// 새이름
		String newSavePath = savePath.replace('\\', '/').substring(0, savePath.lastIndexOf('/')) + "/F" + StringUtil.getNextHexa() + "." + ext;

		// 파일복사
		String baseDir = distHandler.getBaseDir();
		if (new File(baseDir + savePath).exists()) {
			distHandler.copyFile(baseDir + savePath, baseDir + newSavePath);
		}

		// baseDir
		String distPath = distHandler.addWebList(newSavePath);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("distPath = " + distPath);
		}
		distHandler.distribute();

		return distPath;
	}

}

