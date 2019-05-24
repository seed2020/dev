package com.innobiz.orange.web.cm.ctrl;

import java.io.File;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.MessageProperties;

/** 단순 파일 다운로드용 */
@Controller
public class CmResourceCtrl {
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 첨부파일 다운로드 (사용자) */
	@RequestMapping(value = "/cm/{module}/resource", method = RequestMethod.GET)
	public ModelAndView downFile(HttpServletRequest request,
			@RequestParam(value = "file", required = false) String fileName,
			@PathVariable("module") String module,
			ModelMap model, Locale locale) throws Exception {
		
		try {
			if (fileName==null || fileName.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			File file = new File(request.getSession().getServletContext().getRealPath(
					"/WEB-INF/classes/resources/"+module+"/"+fileName));
			if(!file.isFile()){
				// cm.msg.file.fileNotFound=요청한 파일을 찾을 수 없습니다.
				String message = messageProperties.getMessage("cm.msg.file.fileNotFound", locale);
				throw new CmException(message + " : "+fileName);
			}
			ModelAndView mv = new ModelAndView("fileDownloadView");
			mv.addObject("downloadFile", file);
			mv.addObject("realFileName", fileName);
			return mv;
		} catch (CmException e) {
			ModelAndView mv = new ModelAndView("/cm/result/commonResult");
			mv.addObject("message", e.getMessage());
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			ModelAndView mv = new ModelAndView("/cm/result/commonResult");
			mv.addObject("message", e.getClass().getCanonicalName()+" : "+ e.getMessage());
			return mv;
		}
	}
}
