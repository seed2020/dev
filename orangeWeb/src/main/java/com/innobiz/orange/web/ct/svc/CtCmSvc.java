package com.innobiz.orange.web.ct.svc;

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

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.ct.vo.CtFncDVo;
import com.innobiz.orange.web.pt.secu.UserVo;

@Service
public class CtCmSvc {
	
	/** 공통 DB 처리용 SVC */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 첨부파일 서비스 */
	@Autowired
	private CtFileSvc ctFileSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** ID 통합 생성 */
	public String createId(String tableName) throws	SQLException{
		
		//커뮤니티 개설 기본 테이블 (CT_ID 생성)
		if("CT_ESTB_B".equals(tableName)){
			return commonSvc.createId(tableName, 'C', 8);
		}
		if("CT_CAT_B".equals(tableName)){
			return commonSvc.createId(tableName, 'K', 8);
		}
		if("CT_RESC_B".equals(tableName)){
			return commonSvc.createId(tableName, 'R', 8);
		}
		if("CT_FNC_MNG_B".equals(tableName)){
			return commonSvc.createId(tableName, 'F', 8);
		}
		if("CT_FNC_D".equals(tableName)){
			return commonSvc.createId(tableName, 'T', 7);
		}
		
		if("CT_DEBR_B".equals(tableName)){
			return commonSvc.createId(tableName, 'D', 8);
		}
		
		if("CT_SITE_B".equals(tableName)){
			return commonSvc.createId(tableName, 'S', 8);
		}
		
		if("CT_SITE_CAT_D".equals(tableName)){
			return commonSvc.createId(tableName, 'G', 8);
		}
		if("CT_SCHDL_B".equals(tableName)){
			return commonSvc.createId(tableName, 'H', 8);
		}
		
		if ("CT_SURV_B".equals(tableName)) {
			return commonSvc.createId(tableName, 'V', 8);
		}
		
		if ("CT_SURV_QUES_D".equals(tableName)) {
			return commonSvc.createId(tableName, 'Q', 8);
		}
		

		if ("CT_SCHDL_CAT_CLS_B".equals(tableName)) {
			return commonSvc.createId(tableName, 'W', 8);
		}
		
		return null;
		
	}
	
	/** 공지사항 게시물 ID 생성 */
	public Integer createBullId() throws Exception {
		return commonSvc.nextVal("CT_ADM_NOTC_B").intValue();
	}
	
	/** 게시물첨부파일 ID 생성 */
	public Integer createFileId(String tableName) throws SQLException {
		if(tableName != null ) return commonSvc.nextVal(tableName).intValue();
		return null;
	}
	
	/** 토론실의견 ID 생성 */
	public Integer createOpinOrdr(String tableName) throws Exception {
		if(tableName != null ) return commonSvc.nextVal(tableName).intValue();
		return null;
	}
	
	/** 게시판 한줄답변 ID 생성 */
	public Integer createBullCmtId() throws Exception {
		return commonSvc.nextVal("CT_BULL_MAST_CMD_D").intValue();
	}
	
	/** 자료실 한줄답변 ID 생성 */
	public Integer createRecCmtId() throws Exception {
		return commonSvc.nextVal("CT_REC_MAST_CMD_D").intValue();
	}
	
	/** 첨부파일 목록 조회 */
	public ModelAndView getFileList(HttpServletRequest request, String fileIds, String actioniParam) throws Exception{
		List<CommonFileVo> fileVoList = new ArrayList<CommonFileVo>();
		
		String[] fileIdArray = fileIds.split(",");
		for(String fileId : fileIdArray){
			
			CommonFileVo fileVo = null;
			if(actioniParam != null && "metng".equals(actioniParam)){
				fileVo = ctFileSvc.getFileVo(Integer.valueOf(fileId));
			}else{
				fileVo = ctFileSvc.getFileVo(Integer.valueOf(fileId));
			}
			
			if(fileVo != null){
				File file = new File(fileVo.getSavePath());
				if(file.isFile()){
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
//			File zipFile = ZipUtil.makeZipFile(fileVoList, "files.zip");
//			ModelAndView mv = new ModelAndView("fileDownloadView");
//			mv.addObject("downloadFile", zipFile);
//			mv.addObject("realFileName", zipFile.getName());
			return null;
		}
		
		
	}
	
	public CtFncDVo getCtFncDVo(UserVo userVo, String fncUid, String ctId) throws SQLException{

		CtFncDVo ctFncDVo = new CtFncDVo();
		//ctFncDVo.setCompId(userVo.getCompId());
		ctFncDVo.setCtFncUid(fncUid);
		ctFncDVo.setCtId(ctId);

		return ctFncDVo = (CtFncDVo) commonDao.queryVo(ctFncDVo);
	}

	/** bxId별 함 URL 리턴 - menuId 없는 URL 리턴 */
	public String getBxUrlByBxId(String bxId){
		if(bxId.equals("my")) return "/ct/listMyCm.do"; 
		else if(bxId.equals("mng")) return "/ct/listCmMngTgt.do";
		else if(bxId.equals("notc")) return "/ct/notc/listNotc.do";
		else if(bxId.equals("pr"))  return "/ct/pr/listPr.do";
		else
			return "";
	}
	
}
