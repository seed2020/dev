package com.innobiz.orange.web.sample.ctrl;

import java.io.File;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.innobiz.orange.web.cm.files.DistHandler;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.SessionUtil;

@Controller
public class SampleUploadCtrl {
	
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 업로드 화면 열기 */
	@RequestMapping(value = "/sample/upload/setUpload")
	public Model setUpload(Model model) throws Exception {
		return model;
	}
	
	/** 단순 업로드 예 */
	@RequestMapping(value = "/sample/upload/processSimpleUpload")
	public String processSimpleUpload(HttpServletRequest request, Model model) throws Exception {

		UploadHandler uploadHandler = uploadManager.createHandler(request, "org", "pt");
		Map<String, File> fileMap = uploadHandler.upload();//업로드 파일 정보
		Map<String, String> paramMap = uploadHandler.getParamMap();//파라미터 정보
//		Map<String, List<String>> paramListMap = uploadHandler.getParamListMap();//중복된 파라미터의 경우

System.out.println("simple - in");
System.out.println("fileMap >> "+fileMap);
System.out.println("paramMap >> "+paramMap);
		
		model.addAttribute("fileMap", fileMap);
		model.addAttribute("paramMap", paramMap);
		
		return "/sample/upload/processUpload";
	}
	
	/** 업로드 & 배포 예 */
	@RequestMapping(value = "/sample/upload/processDistributeUpload")
	public String processDistributeUpload(HttpServletRequest request, Model model) throws Exception {

		UploadHandler uploadHandler = uploadManager.createHandler(request, "temp", "pt");
		Map<String, File> fileMap = uploadHandler.upload();//업로드 파일 정보
		Map<String, String> paramMap = uploadHandler.getParamMap();//파라미터 정보
//		Map<String, List<String>> paramListMap = uploadHandler.getParamListMap();//중복된 파라미터의 경우
		
		// aaa : <input type="file"/> 의 name
		DistHandler distHandler = distManager.createHandler("org", SessionUtil.getLocale(request));
		String distPath = distHandler.addWebList(uploadHandler.getAbsolutePath("aaa"));
		distHandler.distribute();
		
		uploadHandler.removeTempDir();
		
		System.out.println("distPath : "+distPath);
		
		System.out.println("fileMap >> "+fileMap);
		System.out.println("paramMap >> "+paramMap);
		
		model.addAttribute("fileMap", fileMap);
		model.addAttribute("paramMap", paramMap);
		
		return "/sample/upload/processUpload";
	}
	
	
	@RequestMapping(value = "/sample/upload/downloadSample")
	public ModelAndView downloadSample(HttpServletRequest request, Model model) throws Exception {

		
		String path = request.getParameter("path");
		String name = request.getParameter("name");
		
		File file = new File(path);
		if(file.isFile()){
			ModelAndView mv = new ModelAndView("fileDownloadView");
			mv.addObject("downloadFile", file);
			mv.addObject("realFileName", name);
			return mv;
		} else {
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			Locale locale = SessionUtil.getLocale(request);
			mv.addObject("message", messageProperties.getMessage("cm.msg.file.fileNotFound", locale));//cm.msg.file.fileNotFound=요청한 파일을 찾을 수 없습니다.
			return mv;
		}
	}
	
}
