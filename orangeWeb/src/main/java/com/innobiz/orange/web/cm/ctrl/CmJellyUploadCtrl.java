package com.innobiz.orange.web.cm.ctrl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistHandler;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

/** Jelly Editor 업로드 처리용 */
@Controller
public class CmJellyUploadCtrl {
	
//	/** 메세지 처리용 프라퍼티 - 다국어 */
//	@Autowired
//	private MessageProperties messageProperties;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;

	
	/** [히든프레임] 사진,도장,사인 업로드 */
	@RequestMapping(value = "/cm/jelly/transImage", method = RequestMethod.POST)
	public String transImage(HttpServletRequest request,
			Locale locale,
			ModelMap model) throws Exception {
		
		UploadHandler uploadHandler = null;
		try{
			uploadHandler = uploadManager.createHandler(request, "temp", "or");
			Map<String, File> fileMap = uploadHandler.upload();//업로드 파일 정보
			Map<String, String> paramMap = uploadHandler.getParamMap();//파라미터 정보
//			Map<String, List<String>> paramListMap = uploadHandler.getParamListMap();//중복된 파라미터의 경우
			
			String module = paramMap.get("module");
			String path = (module==null || module.isEmpty()) ? "images/upload/editor/etc" : "images/upload/editor/"+module.replace('.', '/');
			DistHandler distHandler = distManager.createHandler(path, locale);//업로드 경로 설정
			String distPath = distHandler.addWebList(uploadHandler.getAbsolutePath("imgFile"));// file-tag 의 name
			distHandler.distribute();
			
			Map<String, String> resultMap = new HashMap<String, String>();
			
			// 서버 설정 목록 조회
			Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
			
			String domain = ServerConfig.IS_LOC ? "localhost:8080" : svrEnvMap.get("imgDomain");
			resultMap.put("path", (domain==null || domain.isEmpty()) ? distPath : "http://"+domain+distPath);
			
			String alt = paramMap.get("alt");
			if(alt!=null && !alt.isEmpty()){
				resultMap.put("alt", alt);
			}
			
			File imgFile = fileMap.get("imgFile");
			if(imgFile!=null){
				BufferedImage bimg = ImageIO.read(imgFile);
				resultMap.put("width", Integer.toString(bimg.getWidth()));
				resultMap.put("height", Integer.toString(bimg.getHeight()));
			}
			model.put("todo", "parent.setUploadResult("+JsonUtil.toJson(resultMap)+")");
		} catch(CmException e){
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		} finally {
			if(uploadHandler!=null) uploadHandler.removeTempDir();
		}
		return LayoutUtil.getResultJsp();
	}
	
}
