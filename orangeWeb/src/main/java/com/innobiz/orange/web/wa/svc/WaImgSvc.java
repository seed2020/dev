package com.innobiz.orange.web.wa.svc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistHandler;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.svc.EmFileUploadSvc;
import com.innobiz.orange.web.em.vo.EmTmpFileTVo;
import com.innobiz.orange.web.wa.vo.WaCarImgDVo;
import com.innobiz.orange.web.wa.vo.WaCarKndLVo;

@Service
public class WaImgSvc {
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 파일업로드 서비스 */
	@Resource(name = "emFileUploadSvc")
	private EmFileUploadSvc emFileUploadSvc;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 이미지 세팅 */
	public void setCarImgDVo(HttpServletRequest request, QueryQueue queryQueue, String carKndNo, String tmpImgId) throws SQLException, IOException, CmException{
		EmTmpFileTVo emTmpFileTVo = emFileUploadSvc.getEmTmpFileVo(Integer.parseInt(tmpImgId));
		if(emTmpFileTVo==null) return;
		
		WaCarImgDVo waCarImgDVo = new WaCarImgDVo();
		waCarImgDVo.setCarKndNo(carKndNo);
		String filePath = emTmpFileTVo.getSavePath();
		String path = "images/upload/wa/img";
		
		// 파일 새이름으로 복사 후 파일 배포
		String newSavePath = copyAndWebDisk(request, path, filePath);
		waCarImgDVo.setImgPath(newSavePath);
		BufferedImage bimg = ImageIO.read(new File(filePath));
		waCarImgDVo.setImgWdth(Integer.parseInt(Integer.toString(bimg.getWidth())));
		waCarImgDVo.setImgHght(Integer.parseInt(Integer.toString(bimg.getHeight())));
		queryQueue.store(waCarImgDVo);
		
		// 임시파일 삭제
		
			
	}
	
	/** 이미지 세팅 */
	public void setCarImgDVo(WaCarKndLVo waCarKndLVo) throws SQLException, IOException, CmException{
		if(waCarKndLVo==null || waCarKndLVo.getCarKndNo()==null) return;
		WaCarImgDVo waCarImgDVo = new WaCarImgDVo();
		waCarImgDVo.setCarKndNo(waCarKndLVo.getCarKndNo());
		waCarImgDVo = (WaCarImgDVo)commonDao.queryVo(waCarImgDVo);
		if(waCarImgDVo!=null) waCarKndLVo.setWaCarImgDVo(waCarImgDVo);
	}
	
	/** 파일을 새이름으로 복사 후 파일 배포 (WEB DISK) */
	private String copyAndWebDisk(HttpServletRequest request, String path, String savePath) throws IOException, CmException {
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
	
}
