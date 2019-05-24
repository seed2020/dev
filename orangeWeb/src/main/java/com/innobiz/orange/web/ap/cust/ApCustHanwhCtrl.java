package com.innobiz.orange.web.ap.cust;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.ap.vo.ApErpIntgBVo;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;

@Controller
public class ApCustHanwhCtrl {
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;

	@RequestMapping(value = {"/cm/ap/hanwhaInterface", "/cm/ap/hanwhaInterface2"})
	public String hanwhaInterface(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="GUBUN", required=false) String gubun,	// [해당 문서 키] 한화 회사구분 : 10,20.. 
			@RequestParam(value="FID", required=false) String fid,		// [해당 문서 키] 양식번호
			@RequestParam(value="OID", required=false) String oid,		// [해당 문서 키] 오브잭트ID
			@RequestParam(value="MID", required=false) String mid,		// [해당 문서 키] 오브잭트ID - 추가(이전 그룹웨어의 연계키)
			@RequestParam(value="FNM", required=false) String fnm,			// 양식명
			@RequestParam(value="SUBJECT", required=false) String subject,	// 제목
			@RequestParam(value="CONTENT", required=false) String content,	// 내용
			// 교육 시청서 관련 추가
			@RequestParam(value="ERP_TYP_ID", required=false) String erpTypId,	// training(교육신청서), trainingResults(교육결과보고서)
			@RequestParam(value="ERP_START", required=false) String erpStart,	// 시작일
			@RequestParam(value="ERP_END", required=false) String erpEnd,	// 종료일
			@RequestParam(value="PURPOSE", required=false) String erpPurpose,	// 교육목적
			Locale locale,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		if(userVo==null){
			response.sendRedirect("/");
			return null;
		}
		
		boolean isInterface2 = request.getRequestURI().indexOf("hanwhaInterface2") > 0;
		
		if(gubun==null || gubun.isEmpty()
				|| fid==null || fid.isEmpty()
				|| oid==null || oid.isEmpty()){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			String message = messageProperties.getMessage("pt.msg.nodata.passed", locale);
			model.put("message", message);
			model.put("togo", "/");
			return LayoutUtil.getResultJsp();
		}
		
		if(isInterface2){
			if(erpTypId==null || erpTypId.isEmpty()
					|| erpStart==null || erpStart.isEmpty() || erpStart.length()!=10
					|| erpEnd==null || erpEnd.isEmpty() || erpEnd.length()!=10){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				String message = messageProperties.getMessage("pt.msg.nodata.passed", locale);
				model.put("message", message);
				model.put("togo", "/");
				return LayoutUtil.getResultJsp();
			}
		}
		
		// json 형태로 데이터 취합 : 키 데이터
		Map<String, String> refMap = new HashMap<String, String>();
		if(gubun!=null && !gubun.isEmpty()) refMap.put("gubun", gubun);
		if(fid!=null && !fid.isEmpty()) refMap.put("fid", fid);
		if(oid!=null && !oid.isEmpty()) refMap.put("oid", oid);
		if(mid!=null && !mid.isEmpty()) refMap.put("mid", mid);
		
		if(erpTypId!=null && !erpTypId.isEmpty()) refMap.put("erpTypId", erpTypId);
		if(erpStart!=null && !erpStart.isEmpty()) refMap.put("erpStart", erpStart);
		if(erpEnd!=null && !erpEnd.isEmpty()) refMap.put("erpEnd", erpEnd);
		if(erpPurpose!=null && !erpPurpose.isEmpty()) refMap.put("erpPurpose", erpPurpose);
		
		if(isInterface2) refMap.put("interfaceType", "2");
		String refVa = JsonUtil.toJson(refMap);
		
		// ERP연계기본(AP_ERP_INTG_B) 테이블
		ApErpIntgBVo apErpIntgBVo = new ApErpIntgBVo();
		apErpIntgBVo.setFormId(fid);
		apErpIntgBVo.setIntgStatCd("req");
		apErpIntgBVo.setDocSubj(subject);
		apErpIntgBVo.setRegDt("sysdate");
		apErpIntgBVo.setIntgTypCd("ERP_HANWHA");
		apErpIntgBVo.setFormNm(fnm);
		apErpIntgBVo.setRefVa(refVa);
		apErpIntgBVo.setBodyHtml(content);
		
		// 연계번호 최대값 조회
		ApErpIntgBVo maxApErpIntgBVo = new ApErpIntgBVo();
		maxApErpIntgBVo.setInstanceQueryId("com.innobiz.orange.web.ap.dao.ApErpIntgBDao.selectMaxApErpIntgB");
		
		Long intgNo = commonSvc.queryLong(maxApErpIntgBVo);
		apErpIntgBVo.setIntgNo(intgNo.toString());
		
		commonSvc.insert(apErpIntgBVo);
		
		String bxId = "myBx";
		String redirectUrl = "/ap/box/setDoc.do?bxId="+bxId + "&intgNo="+intgNo;
		redirectUrl = ptSecuSvc.toAuthMenuUrl(userVo, redirectUrl, "/ap/box/listApvBx.do?bxId="+bxId);
		
		response.sendRedirect(redirectUrl);
		return null;
	}
	
}
