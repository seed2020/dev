package com.innobiz.orange.web.wv.svc;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.innobiz.orange.web.cm.files.ZipUtil;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;

/** 설문 공통 서비스(설문ID) */
@Service
public class WvCmSvc {
	
	/** 공통 DB 처리용 SVC */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 첨부파일 서비스 */
	@Autowired
	private WvFileSvc wvFileSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** ID 통합 생성 */
	public String createId(String tableName) throws SQLException {
		
		if ("WV_SURV_B".equals(tableName)) {
			return commonSvc.createId(tableName, 'V', 8);
		}
		
		if ("WV_SURV_QUES_D".equals(tableName)) {
			return commonSvc.createId(tableName, 'Q', 8);
		}
		
		return null;
		
		
	}
	
	/** 게시물첨부파일 ID 생성 */
	public Integer createFileId(String tableName) throws Exception {
		if(tableName != null ) return commonSvc.nextVal(tableName).intValue();
		return null;
	}
	
	/** 첨부파일 목록 조회 */
	public ModelAndView getFileList(HttpServletRequest request,String fileIds , String actionParam) throws Exception {
		List<CommonFileVo> fileVoList = new ArrayList<CommonFileVo>();
		// fileId
		String[] fileIdArray = fileIds.split(",");
		for (String fileId : fileIdArray) {
			// 첨부파일
			CommonFileVo fileVo = null; 
			if(actionParam != null && "metng".equals(actionParam)){
				fileVo = wvFileSvc.getFileVo(Integer.valueOf(fileId));
			}else{
				fileVo = wvFileSvc.getFileVo(Integer.valueOf(fileId));
			}
			
			if (fileVo != null) {
				File file = new File(fileVo.getSavePath());
				if (file.isFile()) {
					fileVoList.add(fileVo);
				}
			}
		}
		
		// 파일이 몇개인가
		if (fileVoList.size() == 0) {
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			Locale locale = SessionUtil.getLocale(request);
			// cm.msg.file.fileNotFound=요청한 파일을 찾을 수 없습니다.
			mv.addObject("message", messageProperties.getMessage("cm.msg.file.fileNotFound", locale));
			return mv;
			
		} else if (fileVoList.size() == 1) {
			CommonFileVo fileVoVo = fileVoList.get(0);
			String savePath = fileVoVo.getSavePath();
			File file = new File(savePath);
			ModelAndView mv = new ModelAndView("fileDownloadView");
			mv.addObject("downloadFile", file);
			mv.addObject("realFileName", fileVoVo.getDispNm());
			return mv;
			
		} else {
			File zipFile = new ZipUtil().makeZipFile(fileVoList, "files.zip");
			ModelAndView mv = new ModelAndView("fileDownloadView");
			mv.addObject("downloadFile", zipFile);
			mv.addObject("realFileName", zipFile.getName());
			return mv;
		}
					
	}

}
