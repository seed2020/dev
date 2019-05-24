package com.innobiz.orange.web.ap.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.ap.svc.ApCmSvc;
import com.innobiz.orange.web.ap.svc.ApDocTransSvc;
import com.innobiz.orange.web.ap.utils.SAXHandler;
import com.innobiz.orange.web.ap.utils.XMLElement;
import com.innobiz.orange.web.ap.vo.ApErpFormBVo;
import com.innobiz.orange.web.ap.vo.ApErpIntgBVo;
import com.innobiz.orange.web.ap.vo.ApErpIntgChitDVo;
import com.innobiz.orange.web.ap.vo.ApFormBVo;
import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;

/** ERP 문서 일괄 저장 - 중단/테스트 안됨 */
@Controller
public class ApErpBulkCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(ApDocTransSvc.class);

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 결재 공통 서비스 */
	@Autowired
	private ApCmSvc apCmSvc;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 문서 저장 서비스 */
	@Autowired
	private ApDocTransSvc apDocTransSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** ERP 일괄 임시 저장 */
	@RequestMapping(value = "/cm/ap/processErpTemp")
	public String processErpBulk(HttpServletRequest request, HttpServletResponse response,
			Locale locale, ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		QueryQueue queryQueue = new QueryQueue();
		
		String intgNos = (String)request.getSession().getAttribute("intgNos");
//		String langTypCd = (String)request.getSession().getAttribute("langTypCd");
		
		String[] intgNoArr = intgNos==null || intgNos.isEmpty() ? new String[]{} : intgNos.split(",");
		
		// [결재옵션] 세팅
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(model, userVo.getCompId());
		
		UploadHandler uploadHandler = uploadManager.createHandler(request, "temp", "ap");
		
		Map<String, String> paramMap = null;
		Map<String, List<String>> paramListMap = null;
		
		boolean logFirst = true;
		StringBuilder msgBuilder = new StringBuilder(64);
		
		String storedApvNo;
		ApErpIntgBVo apErpIntgBVo;
		ApFormBVo apFormBVo;
		List<String> apvLnList;
		for(String intgNo : intgNoArr){
			
			queryQueue.removeAll();
			
			// ERP연계기본(AP_ERP_INTG_B) 테이블
			apErpIntgBVo = new ApErpIntgBVo();
			apErpIntgBVo.setIntgNo(intgNo);
			apErpIntgBVo.setQueryLang(langTypCd);
			apErpIntgBVo = (ApErpIntgBVo)commonSvc.queryVo(apErpIntgBVo);
			
			if(apErpIntgBVo==null){
				
				// cm.msg.noData=해당하는 데이터가 없습니다.
				String message = messageProperties.getMessage("cm.msg.noData", locale)+" - intgNo:"+intgNo;
				LOGGER.error("[AP ERP TEMP SAVE] No AP_ERP_INTG_B data - intgNo : "+intgNo);
				
				if(logFirst) logFirst = false;
				else msgBuilder.append("\r\n");
				
				msgBuilder.append(intgNo).append(':').append(message);
				continue;
			}
			
			apFormBVo = new ApFormBVo();
			apFormBVo.setFormId(apErpIntgBVo.getFormId());
			apFormBVo = (ApFormBVo)commonSvc.queryVo(apFormBVo);
			if(apFormBVo==null){
				
				// cm.msg.noData=해당하는 데이터가 없습니다.
				String message = messageProperties.getMessage("cm.msg.noData", locale)+" - intgNo:"+intgNo + " formId:"+apErpIntgBVo.getFormId();
				LOGGER.error("[AP ERP TEMP SAVE] No AP_FORM_B data - intgNo:"+intgNo + "  formId:"+apErpIntgBVo.getFormId());
				
				if(logFirst) logFirst = false;
				else msgBuilder.append("\r\n");
				
				msgBuilder.append(intgNo).append(':').append(message);
				continue;
			}
			
			storedApvNo = apErpIntgBVo.getApvNo();
			if(storedApvNo!=null && !storedApvNo.isEmpty()){
				//ap.msg.alreadySaved=이미 저장된 문서 입니다.
				String message = messageProperties.getMessage("ap.msg.alreadySaved", locale)+" - intgNo:"+intgNo + " apvNo:"+storedApvNo;
				LOGGER.error("[AP ERP TEMP SAVE] No AP_FORM_B data - intgNo:"+intgNo + "  apvNo:"+storedApvNo);
				
				if(logFirst) logFirst = false;
				else msgBuilder.append("\r\n");
				
				msgBuilder.append(intgNo).append(':').append(message);
				continue;
			}
			
			paramMap = new HashMap<String, String>();
			paramListMap = new HashMap<String, List<String>>();
			
			// - submit 정보 생성
			paramMap.put("intgNo", intgNo);
			paramMap.put("docLangTypCd", langTypCd);
			
			paramMap.put("docSubj", apErpIntgBVo.getDocSubj());
			paramMap.put("bodyHtml", apErpIntgBVo.getBodyHtml());
			paramMap.put("formId", apErpIntgBVo.getFormId());
			
			// 양식명
			if(apErpIntgBVo.getFormNm()!=null && !apErpIntgBVo.getFormNm().isEmpty()){
				paramMap.put("formNm", apErpIntgBVo.getFormNm());
			} else {
				paramMap.put("formNm", apFormBVo.getRescNm());
			}
			
			paramMap.put("bodyHghtPx", apFormBVo.getBodyHghtPx());
			paramMap.put("formApvLnTypCd", apFormBVo.getFormApvLnTypCd());
			paramMap.put("formWdthTypCd", apFormBVo.getFormWdthTypCd());
			
			String docKeepPrdCd = apFormBVo.getDocKeepPrdCd();
			if(docKeepPrdCd==null || docKeepPrdCd.isEmpty()) docKeepPrdCd = "1Y";
			paramMap.put("docKeepPrdCd", docKeepPrdCd);
			
			paramMap.put("allReadYn", "N");
			paramMap.put("seculCd", "none");
			paramMap.put("regRecLstRegYn", "Y");
			paramMap.put("ugntDocYn", "N");
			paramMap.put("maxFileSeq", "0");
			paramMap.put("statCd", "temp");
			//paramMap.put("docTypCd", "intro");
			
			//양식구분코드 - intro:기안(내부문서), extro:기안(시행겸용), trans:시행변환용
			if("extro".equals(apFormBVo.getFormTypCd())){
				//문서구분코드 - intro:내부문서, extro:시행문서, paper:종이문서
				paramMap.put("docTypCd", "extro");
				// [옵션] bothSendEnab=대 내외 동시 시행
				if("Y".equals(optConfigMap.get("bothSendEnab"))){
					//시행범위코드 - dom:대내, for:대외, both:대내외
					paramMap.put("enfcScopCd", "both");
				} else {
					//시행범위코드 - dom:대내, for:대외, both:대내외
					paramMap.put("enfcScopCd", "dom");
				}
				paramMap.put("enfcDocKeepPrdCd", docKeepPrdCd);
			} else {
				//문서구분코드 - intro:내부문서, extro:시행문서, paper:종이문서
				paramMap.put("docTypCd", "intro");
			}
			
			paramMap.put("bxId", "myBx");
			
			apvLnList = new ArrayList<String>();
			apvLnList.add("{\"apvrRoleCd\":\"mak"
					+"\",\"dblApvTypCd\":\"" + ("apvLnDbl".equals(apFormBVo.getFormApvLnTypCd()) || "apvLnDblList".equals(apFormBVo.getFormApvLnTypCd()) ? "reqDept" : "")
					+"\",\"apvrUid\":\"" + userVo.getUserUid()
					+"\",\"apvStatCd\":\"inApv\",\"apvrDeptYn\":\"N\"}");
			paramListMap.put("apvLn", apvLnList);
			
			uploadHandler.setParamMap(paramMap, paramListMap);
			model.put("uploadHandler", uploadHandler);
			
			try {
				apDocTransSvc.processTrans(request, queryQueue, locale, model);
				
				if(logFirst) logFirst = false;
				else msgBuilder.append("\r\n");
				msgBuilder.append(intgNo).append(':').append("SUCCESS");
				
			} catch(Exception e){
				
				if(logFirst) logFirst = false;
				else msgBuilder.append("\r\n");
				
				msgBuilder.append(intgNo).append(':');
				if(e instanceof NullPointerException) {
					msgBuilder.append(NullPointerException.class.getCanonicalName());
				} else {
					msgBuilder.append(e.getMessage());
				}
			}
			
		}
		
		String charset = "EUC-KR";
		response.setCharacterEncoding(charset);
		ServletOutputStream out = response.getOutputStream();
		out.write(msgBuilder.toString().getBytes(charset));
		out.close();
		
		return null;
	}
	/** ERP 전표 삭제 - 문서 제목에 [ERP 전표삭제] 붙임 */
	@RequestMapping(value = "/cm/ap/processErpDel")
	public String processErpDel(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="intgNo", required=false) String intgNo,//연계번호 -ERP
			Locale locale, ModelMap model) throws Exception {
		
		String message = null;
		String apvNo = null;
		
		QueryQueue queryQueue = new QueryQueue();
		
		// ERP연계기본(AP_ERP_INTG_B) 테이블
		ApErpIntgBVo apErpIntgBVo = new ApErpIntgBVo();
		apErpIntgBVo.setIntgNo(intgNo);
		apErpIntgBVo = (ApErpIntgBVo)commonSvc.queryVo(apErpIntgBVo);
		if(apErpIntgBVo==null){
			message = "NO DATA(AP_ERP_INTG_B)";
		} else {
			apvNo = apErpIntgBVo.getApvNo();
		}
		
		ApOngdBVo apOngdBVo = null;
		if(apvNo==null || apvNo.isEmpty()){
			message = "NOT STARTED";
		} else {
			apOngdBVo = new ApOngdBVo();
			apOngdBVo.setApvNo(apvNo);
			apOngdBVo = (ApOngdBVo)commonSvc.queryVo(apOngdBVo);
		}
		
		if(apOngdBVo==null){
			message = "NO DATA(AP_ONGD_B)";
		} else {
			
			String langTypCd = apOngdBVo.getDocLangTypCd();
			Locale docLocale = langTypCd==null || langTypCd.isEmpty() ? locale : SessionUtil.toLocale(langTypCd);
			
			// ap.prefix.delErp=[ERP전표삭제]
			String prefix = messageProperties.getMessage("ap.prefix.delErp", docLocale);
			
			String docSubj = apOngdBVo.getDocSubj();
			String formNm  = apOngdBVo.getFormNm();
			
			boolean hasUpdatData = false;
			apOngdBVo = new ApOngdBVo();
			apOngdBVo.setApvNo(apvNo);
			
			// 결재 테이블 양식명, 제목 수정 - [ERP전표삭제]
			if(docSubj==null || docSubj.isEmpty()){
				apOngdBVo.setDocSubj(prefix);
				hasUpdatData = true;
			} else if(!docSubj.startsWith(prefix)){
				apOngdBVo.setDocSubj(prefix + docSubj);
				hasUpdatData = true;
			}
			if(formNm==null || formNm.isEmpty()){
				apOngdBVo.setFormNm(prefix);
				hasUpdatData = true;
			} else if(!formNm.startsWith(prefix)){
				apOngdBVo.setFormNm(prefix + formNm);
				hasUpdatData = true;
			}
			
			if(hasUpdatData){
				queryQueue.update(apOngdBVo);
			}
			
			
			// 전표용 양식명 수정 - [ERP전표삭제]
			ApErpIntgChitDVo apErpIntgChitDVo = new ApErpIntgChitDVo();
			apErpIntgChitDVo.setIntgNo(intgNo);
			apErpIntgChitDVo = (ApErpIntgChitDVo)commonSvc.queryVo(apErpIntgChitDVo);
			
			if(apErpIntgChitDVo!=null){
				
				formNm  = apErpIntgChitDVo.getFormNm();
				
				apErpIntgChitDVo = new ApErpIntgChitDVo();
				apErpIntgChitDVo.setIntgNo(intgNo);
				
				hasUpdatData = false;
				if(formNm==null || formNm.isEmpty()){
					apErpIntgChitDVo.setFormNm(prefix);
					hasUpdatData = true;
				} else if(!formNm.startsWith(prefix)){
					apErpIntgChitDVo.setFormNm(prefix + formNm);
					hasUpdatData = true;
				}
				
				if(hasUpdatData){
					queryQueue.update(apErpIntgChitDVo);
				}
			}
			
			if(!queryQueue.isEmpty()){
				commonSvc.execute(queryQueue);
			}
			
			message = "SUCCESS";
		}
		
		String charset = "EUC-KR";
		response.setCharacterEncoding(charset);
		ServletOutputStream out = response.getOutputStream();
		out.write(message.getBytes(charset));
		out.close();
		
		return null;
	}
	/** ERP 전표 취소 - 임시저장의 경우만 결재문서 삭제함 */
	@RequestMapping(value = "/cm/ap/processErpCancel")
	public String processErpCancel(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="intgNo", required=false) String intgNo,//연계번호 -ERP
			Locale locale, ModelMap model) throws Exception {
		
		String message = null;
		String apvNo = null;
		
		QueryQueue queryQueue = new QueryQueue();
		
		// ERP연계기본(AP_ERP_INTG_B) 테이블
		ApErpIntgBVo apErpIntgBVo = new ApErpIntgBVo();
		apErpIntgBVo.setIntgNo(intgNo);
		apErpIntgBVo = (ApErpIntgBVo)commonSvc.queryVo(apErpIntgBVo);
		if(apErpIntgBVo==null){
			message = "NO DATA(AP_ERP_INTG_B)";
		} else {
			apvNo = apErpIntgBVo.getApvNo();
		}
		
		ApOngdBVo apOngdBVo = null;
		if(apvNo==null || apvNo.isEmpty()){
			message = "NOT STARTED";
		} else {
			apOngdBVo = new ApOngdBVo();
			apOngdBVo.setApvNo(apvNo);
			apOngdBVo = (ApOngdBVo)commonSvc.queryVo(apOngdBVo);
		}
		
		if(apOngdBVo==null){
			message = "NO DATA(AP_ONGD_B)";
		} else if("temp".equals(apOngdBVo.getDocProsStatCd())){
			message = "NOT TEMP(AP_ONGD_B)";
		} else {
			
			apErpIntgBVo = new ApErpIntgBVo();
			apErpIntgBVo.setIntgNo(intgNo);
			apErpIntgBVo.setIntgStatCd("erpCncl");
			queryQueue.update(apErpIntgBVo);
			
			/*
			ApOngdErpFormDVo apOngdErpFormDVo = new ApOngdErpFormDVo();
			apOngdErpFormDVo.setApvNo(apvNo);
			if(commonSvc.count(apOngdErpFormDVo)>0){
				queryQueue.delete(apOngdErpFormDVo);
			}
			
			ApOngdExDVo apOngdExDVo = new ApOngdExDVo();
			apOngdExDVo.setApvNo(apvNo);
			if(commonSvc.count(apOngdExDVo)>0){
				queryQueue.delete(apOngdExDVo);
			}
			
			ApOngdExtnDocDVo ApOngdExtnDocDVo = new ApOngdExtnDocDVo();
			ApOngdExtnDocDVo.setApvNo(apvNo);
			if(commonSvc.count(ApOngdExtnDocDVo)>0){
				queryQueue.delete(ApOngdExtnDocDVo);
			}
			
			ApOngdSendDVo ApOngdSendDVo = new ApOngdSendDVo();
			ApOngdSendDVo.setApvNo(apvNo);
			if(commonSvc.count(ApOngdSendDVo)>0){
				queryQueue.delete(ApOngdSendDVo);
			}
			
			ApOngdPichDVo ApOngdPichDVo = new ApOngdPichDVo();
			ApOngdPichDVo.setApvNo(apvNo);
			if(commonSvc.count(ApOngdPichDVo)>0){
				queryQueue.delete(ApOngdPichDVo);
			}
			
			ApOngdRefDocLVo ApOngdRefDocLVo = new ApOngdRefDocLVo();
			ApOngdRefDocLVo.setApvNo(apvNo);
			if(commonSvc.count(ApOngdRefDocLVo)>0){
				queryQueue.delete(ApOngdRefDocLVo);
			}
			
			ApOngdRecvDeptLVo ApOngdRecvDeptLVo = new ApOngdRecvDeptLVo();
			ApOngdRecvDeptLVo.setApvNo(apvNo);
			if(commonSvc.count(ApOngdRecvDeptLVo)>0){
				queryQueue.delete(ApOngdRecvDeptLVo);
			}
			
			ApOngdAttFileLVo ApOngdAttFileLVo = new ApOngdAttFileLVo();
			ApOngdAttFileLVo.setApvNo(apvNo);
			if(commonSvc.count(ApOngdAttFileLVo)>0){
				queryQueue.delete(ApOngdAttFileLVo);
			}
			
			ApOngdBodyLVo ApOngdBodyLVo = new ApOngdBodyLVo();
			ApOngdBodyLVo.setApvNo(apvNo);
			if(commonSvc.count(ApOngdBodyLVo)>0){
				queryQueue.delete(ApOngdBodyLVo);
			}
			
			ApOngdApvLnDVo ApOngdApvLnDVo = new ApOngdApvLnDVo();
			ApOngdApvLnDVo.setApvNo(apvNo);
			if(commonSvc.count(ApOngdApvLnDVo)>0){
				queryQueue.delete(ApOngdApvLnDVo);
			}
			*/
			ApOngdBVo storedApOngdBVo = apOngdBVo;
			
			apOngdBVo = new ApOngdBVo();
			apOngdBVo.setApvNo(apvNo);
			//ap.prefix.canErp=[ERP전표취소]
			String prefix = messageProperties.getMessage("ap.prefix.canErp", locale);
			apOngdBVo.setDocSubj(prefix+storedApOngdBVo.getDocSubj());
			apOngdBVo.setFormNm(prefix+storedApOngdBVo.getFormNm());
			apOngdBVo.setDocStatCd("rejt");
			queryQueue.update(apOngdBVo);
			
			if("ERP_CHIT".equals(storedApOngdBVo.getIntgTypCd())){
				ApErpIntgChitDVo apErpIntgChitDVo = new ApErpIntgChitDVo();
				apErpIntgChitDVo.setIntgNo(intgNo);
				apErpIntgChitDVo = (ApErpIntgChitDVo)commonSvc.queryVo(apErpIntgChitDVo);
				if(apErpIntgChitDVo != null){
					apErpIntgChitDVo.setFormNm(prefix+apErpIntgChitDVo.getFormNm());
					apErpIntgChitDVo.setChitBodyHtml(null);
					apErpIntgChitDVo.setChitFormId(null);
					queryQueue.update(apErpIntgChitDVo);
				}
			}
			
			commonSvc.execute(queryQueue);
			
			message = "SUCCESS";
		}
		
		String charset = "EUC-KR";
		response.setCharacterEncoding(charset);
		ServletOutputStream out = response.getOutputStream();
		out.write(message.getBytes(charset));
		out.close();
		
		return null;
	}
	

	/** ERP XML 로 부터 HTML 생성 */
	@RequestMapping(value = "/ap/self/getHtmlFromErpXml")
	public String getHtmlFromErpXml(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="intgNo", required=false) String intgNo,//연계번호 -ERP
			@Parameter(name="erpFormId", required=false) String erpFormId,
			Locale locale, ModelMap model) throws Exception {
		
		String xml=null, regUrl=null;
		
		if(intgNo!=null && !intgNo.isEmpty()){
			ApErpIntgBVo apErpIntgBVo = new ApErpIntgBVo();
			apErpIntgBVo.setIntgNo(intgNo);
			apErpIntgBVo = (ApErpIntgBVo)commonSvc.queryVo(apErpIntgBVo);
			
			xml = apErpIntgBVo==null ? null : apErpIntgBVo.getBodyHtml();
		}
		
		if(erpFormId!=null && !erpFormId.isEmpty()){
			ApErpFormBVo apErpFormBVo = new ApErpFormBVo();
			apErpFormBVo.setErpFormId(erpFormId);
			apErpFormBVo = (ApErpFormBVo)commonSvc.queryVo(apErpFormBVo);
			
			regUrl = apErpFormBVo==null ? null : apErpFormBVo.getRegUrl();
		}
		
		if(xml==null || xml.isEmpty()){
			LOGGER.error("HTML FROM ERP XML - no xml(bodyHtml) at AP_ERP_INTG_B - intgNo:"+intgNo);
			return null;
		} else if(regUrl==null || regUrl.isEmpty() || !regUrl.endsWith(".jsp")){
			LOGGER.error("HTML FROM ERP XML - no regUrl at AP_ERP_FORM_B - erpFormId:"+erpFormId+"  regUrl:"+regUrl);
			return null;
		}
		
		XMLElement xmlElement = SAXHandler.parse(xml);
		model.put("formBodyMode", "view");
		model.put("formBodyXML", xmlElement);
		
		return regUrl.substring(0, regUrl.length()-4);
	}
}
