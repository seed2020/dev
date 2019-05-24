package com.innobiz.orange.web.wc.ctrl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtCompBVo;
import com.innobiz.orange.web.wc.svc.WcCmSvc;
import com.innobiz.orange.web.wc.svc.WcScdManagerSvc;
import com.innobiz.orange.web.wc.vo.WcSchdlBVo;
import com.innobiz.orange.web.wc.vo.WcWorkSchdlBVo;
import com.innobiz.orange.web.wr.svc.WrCmSvc;
import com.innobiz.orange.web.wr.vo.WrMonthVo;
import com.innobiz.orange.web.wr.vo.WrWeekVo;


/** ERP 일정 */
@Controller
public class WcWorkSchdlCtrl {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WcWorkSchdlCtrl.class);
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 공통 서비스 */
	@Autowired
	private WcScdManagerSvc wcScdManagerSvc;

	/** 일정 공통 서비스*/
	@Autowired
	private WcCmSvc wcCmSvc;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 자원예약 공통 서비스 */
	@Autowired
	private WrCmSvc wrCmSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
    /** 일정 보기 */
	@RequestMapping(value = "/wc/work/listCalendar")
	public String listCalendar(HttpServletRequest request,
			@RequestParam(value = "strtDt", required = false) String strtDt,
			@RequestParam(value = "viewTyp", required = false) String viewTyp,
			ModelMap model) throws Exception {
		
		try{
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			if(strtDt==null || strtDt.isEmpty() || !isDateChk(strtDt)) strtDt = StringUtil.getCurrYmd();
			
			model.put("strtDt", strtDt);
			
			if(viewTyp==null || viewTyp.isEmpty()) viewTyp = "month";
			
			String durStrtDt = "";
			String durEndDt = "";
			
			Object dayVo = null;
			if("month".equals(viewTyp)){
				dayVo = wrCmSvc.getMonth(strtDt);
				durStrtDt = ((WrMonthVo)dayVo).getBeforeStrtDt();
				durEndDt = ((WrMonthVo)dayVo).getAfterEndDt();
				model.put("wrMonthVo", (WrMonthVo)dayVo);
			}else{
				// 날짜를 기준으로 주간날짜 정보 조회
				dayVo = wrCmSvc.getWeek(strtDt);
				durStrtDt = ((WrWeekVo)dayVo).getStrtDt();
				durEndDt = ((WrWeekVo)dayVo).getEndDt();
				model.put("wrWeekVo", (WrWeekVo)dayVo);
			}
			
			model.put("start", durStrtDt);
			model.put("end", durEndDt);
			
			/** 일정구분코드 조회 */
			List<PtCdBVo> wcCatClsBVoList = ptCmSvc.getCdList("WORK_SCHDL_CD", langTypCd, "Y");
			model.put("wcCatClsBVoList", wcCatClsBVoList);
			
			// print css 적용
			if(request.getAttribute("printView")==null){
				request.setAttribute("printView", "print100");
			}
			
		}catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		model.put("params", ParamUtil.getQueryString(request));
		
		return LayoutUtil.getJspPath("/wc/work/listCalendar");
	}
	
	/** 날짜 형식 체크(yyyy-mm-dd) */
	public boolean isDateChk(String val){
		return Pattern.matches("^\\d{4}-\\d{2}-\\d{2}$", val);
	}
	
    /** [AJAX] 일정 조회 */
	@RequestMapping(value = {"/wc/work/listSchdlAjx", "/wc/plt/listWorkSchdlAjx"})
	public String listSchdlAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {
		
		try{
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String start = (String) object.get("start");
			String end = (String) object.get("end");
			if ( start == null || start.isEmpty() || end == null || end.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			String schdlTypCd = (String) object.get("schdlTypCd");
			
			String schUserUid = (String) object.get("schUserUid");
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 국가코드
			String natCd = (String) object.get("natCd");
			if(natCd==null || natCd.isEmpty()){
				natCd = wcScdManagerSvc.getNatCd(userVo);
			}
						
			//음력일자 계산을 위해 한달 전부터 조회
			String realStrtDt = wcScdManagerSvc.getDateOfDay(start, "month", "s", null, 1);
			realStrtDt = wcScdManagerSvc.getDateOfDay(realStrtDt, "month", "s", null, 1);
			// 조회
			WcWorkSchdlBVo wcWorkSchdlBVo = new WcWorkSchdlBVo();
			wcWorkSchdlBVo.setCompId(userVo.getCompId());
			
			//일정 조회
			wcWorkSchdlBVo.setStrtDt(realStrtDt);
			wcWorkSchdlBVo.setEndDt(end);
			if(schdlTypCd!=null && !schdlTypCd.isEmpty()) wcWorkSchdlBVo.setSchdlTypCd(schdlTypCd);
			if(schUserUid!=null && !schUserUid.isEmpty()) wcWorkSchdlBVo.setUserUid(schUserUid);
			
			@SuppressWarnings("unchecked")
			List<WcWorkSchdlBVo> wcWorkSchdlBVoList = (List<WcWorkSchdlBVo>)commonSvc.queryList(wcWorkSchdlBVo);
			model.put("wcSchdlBVoList", wcWorkSchdlBVoList);
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
						
			WcSchdlBVo wcSchdlBVo = new WcSchdlBVo();
			wcSchdlBVo.setInstanceQueryId("com.innobiz.orange.web.wc.dao.SchdlBDao.selectSchdlB");
			wcSchdlBVo.setQueryLang(langTypCd);
			wcSchdlBVo.setCompId(userVo.getCompId());
			wcSchdlBVo.setNatCd(natCd); // 국가코드
			// 기념일 조회(음력일정을 구하기 위해 한달전 일정까지 조회한다.)
			wcSchdlBVo.setSchdlStartDt(realStrtDt);
			wcSchdlBVo.setSchdlEndDt(end);
			wcSchdlBVo.setSchdlTypCd("5");//기념일
			
			@SuppressWarnings("unchecked")
			List<WcSchdlBVo> annvList = (List<WcSchdlBVo>)commonSvc.queryList(wcSchdlBVo);
			model.put("annvList", wcScdManagerSvc.getLunarSchdlList(annvList));
			
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);			
		
	}
	
	/** 등록 팝업 오픈
	 * @throws Exception */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/wc/work/setSchdlPop")
	public String setSchdlPop(HttpServletRequest request,
			@RequestParam(value="schdlId",required=false) String schdlId,
			@RequestParam(value="strtDt",required=false) String strtDt,
			ModelMap model) throws Exception{
				
		UserVo userVo = LoginSession.getUser(request);
		
		WcWorkSchdlBVo wcWorkSchdlBVo = new WcWorkSchdlBVo();
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
		//수정
		if(schdlId != null && !"".equals(schdlId)){
			wcWorkSchdlBVo.setQueryLang(langTypCd);
			wcWorkSchdlBVo.setSchdlId(schdlId);
			wcWorkSchdlBVo = (WcWorkSchdlBVo)commonSvc.queryVo(wcWorkSchdlBVo);
		}else{
			Calendar cal = Calendar.getInstance();
			Integer hour = null;
			if(strtDt != null && !"".equals(strtDt)){
				//String schdlStartDt = wcSchdlBVo.getSchdlStartDt(); 
				int year = Integer.parseInt(strtDt.substring(0, 4)); 
				int month = Integer.parseInt(strtDt.substring(4, 6)); 
				int day = Integer.parseInt(strtDt.substring(6,8));
				
	            cal.set(Calendar.YEAR, year);
	            cal.set(Calendar.MONTH, month-1); // 0 이 1월, 1 은 2월, .... 
	            cal.set(Calendar.DAY_OF_MONTH, day);
			}
			cal.add(Calendar.HOUR_OF_DAY, 1);
            cal.set(Calendar.MINUTE, 0);
            
			//시작일자 세팅
            wcWorkSchdlBVo.setStrtDt(new Timestamp(cal.getTimeInMillis()).toString());
            cal.add(Calendar.HOUR_OF_DAY, 1);
            //종료일자 세팅
            wcWorkSchdlBVo.setEndDt(new Timestamp(cal.getTimeInMillis()).toString());
            
		}
		
		model.put("wcWorkSchdlBVo", wcWorkSchdlBVo);
				
		/** 일정구분코드 조회 */
		List<PtCdBVo> wcCatClsBVoList = ptCmSvc.getCdList("WORK_SCHDL_CD", langTypCd, "Y");
		model.put("wcCatClsBVoList", wcCatClsBVoList);
		
		return LayoutUtil.getJspPath("/wc/work/setSchdlPop");
	}
	
	/** 일정 등록, 수정*/
	@RequestMapping(value = "/wc/work/transSchdl")
	public String transSchdl(HttpServletRequest request,
			@RequestParam(value = "schdlId", required = false) String schdlId,
			@RequestParam(value = "callback", required = false) String callback,
			ModelMap model) throws Exception{

		try {
			String startYmd = ParamUtil.getRequestParam(request, "startYmd", true);
			
			// 세션의 언어코드
			UserVo userVo = LoginSession.getUser(request);
						
			WcWorkSchdlBVo wcWorkSchdlBVo = new WcWorkSchdlBVo();
			VoUtil.bind(request, wcWorkSchdlBVo);
			wcWorkSchdlBVo.setCompId(userVo.getCompId());
			wcWorkSchdlBVo.setStrtDt(startYmd);
			wcWorkSchdlBVo.setEndDt(startYmd);
			
			if(schdlId==null || schdlId.isEmpty()){
				wcWorkSchdlBVo.setSchdlId(wcCmSvc.createId("WC_WORK_SCHDL_B"));
				wcWorkSchdlBVo.setAlldayYn("Y");
				// 등록자, 등록일시
				wcWorkSchdlBVo.setRegrUid(userVo.getUserUid());
				wcWorkSchdlBVo.setRegDt("sysdate");
				commonSvc.insert(wcWorkSchdlBVo);
			}else{
				// 수정자, 수정일시
				wcWorkSchdlBVo.setModrUid(userVo.getUserUid());
				wcWorkSchdlBVo.setModDt("sysdate");
				commonSvc.update(wcWorkSchdlBVo);
			}
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			if(callback==null || callback.isEmpty()) callback= "reloadCalendar";
			model.put("todo", "parent."+callback+"();");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}
		//공통 처리 페이지
		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] 일정 저장 */
	@RequestMapping(value = "/wc/work/transSchdlAjx")
	public String transSchdlAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String schdlId = (String) object.get("schdlId"); // 일정ID
			String schdlStartDt = (String) object.get("start"); // 시작일시
			String schdlEndDt = (String) object.get("end"); // 종료일시
			String alldayYn = (String) object.get("alldayYn"); // 종일여부
			
			if ( schdlId == null || schdlId.isEmpty() || schdlStartDt == null || schdlStartDt.isEmpty() || 
					schdlEndDt == null || schdlEndDt.isEmpty() || alldayYn == null || alldayYn.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			WcWorkSchdlBVo wcWorkSchdlBVo = new WcWorkSchdlBVo();
			wcWorkSchdlBVo.setCompId(userVo.getCompId());
			wcWorkSchdlBVo.setSchdlId(schdlId);
			wcWorkSchdlBVo.setStrtDt(schdlStartDt);
			wcWorkSchdlBVo.setEndDt(schdlEndDt);
			wcWorkSchdlBVo.setAlldayYn(alldayYn);
			commonSvc.update(wcWorkSchdlBVo);
			
			// dm.msg.dtlView.cancel.success=처리되었습니다.
			//model.put("message", messageProperties.getMessage("dm.msg.dtlView.cancel.success", request));
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	
	/** [AJAX] 일정 삭제 */
	@RequestMapping(value = "/wc/work/transSchdlDelAjx")
	public String transSchdlDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String schdlId = (String) object.get("schdlId"); // 일정ID
			
			if ( schdlId == null || schdlId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			WcWorkSchdlBVo wcWorkSchdlBVo = new WcWorkSchdlBVo();
			wcWorkSchdlBVo.setCompId(userVo.getCompId());
			wcWorkSchdlBVo.setSchdlId(schdlId);
			
			commonSvc.delete(wcWorkSchdlBVo);
			
			// cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	
	/** 목록 - 근태일정 */
	@RequestMapping(value = {"/wc/work/listWorkSchdule", "/wc/adm/work/listWorkSchdule"})
	public String listWorkSchdule(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		// 테이블관리 기본(WH_REQ_B) 테이블 - BIND
		WcWorkSchdlBVo wcWorkSchdlBVo = new WcWorkSchdlBVo();
		VoUtil.bind(request, wcWorkSchdlBVo);
		wcWorkSchdlBVo.setQueryLang(langTypCd);
		// 회사 ID 조회조건 추가[계열사 설정 확인]
		setCompAffiliateIdList(userVo.getCompId(), langTypCd, wcWorkSchdlBVo, false);
		
		//wcWorkSchdlBVo.setOrderBy(" T.STRT_DT DESC");
		
		Integer recodeCount = commonSvc.count(wcWorkSchdlBVo);
		PersonalUtil.setPaging(request, wcWorkSchdlBVo, recodeCount);
		
		@SuppressWarnings("unchecked")
		List<WcWorkSchdlBVo> list = (List<WcWorkSchdlBVo>) commonSvc.queryList(wcWorkSchdlBVo);
		
		model.put("recodeCount", recodeCount);
		model.put("wcWorkSchdlBVoList", list);
		
		/** 일정구분코드 조회 */
		List<PtCdBVo> wcCatClsBVoList = ptCmSvc.getCdList("WORK_SCHDL_CD", langTypCd, "Y");
		model.put("wcCatClsBVoList", wcCatClsBVoList);
		
		// print css 적용
		if(request.getAttribute("printView")==null){
			request.setAttribute("printView", "print100");
		}
		
		return LayoutUtil.getJspPath("/wc/work/listWorkSchdule");
	}
	
	/** 조회조건 추가 [회사 및 계열사]*/
	public void setCompAffiliateIdList(String compId, String langTypCd, CommonVo commonVo, boolean isAdmin) throws SQLException{
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if(sysPlocMap.get("affiliatesEnable")==null || !"Y".equals(sysPlocMap.get("affiliatesEnable"))){ // 계열사여부가 'N' 이면 전체 조회
			VoUtil.setValue(commonVo, "compId", null);
			return;
		}
		// 계열사 여부 'Y' 일 경우 계열사 데이터 조회(계열사 없으면 내 회사)
		PtCompBVo ptCompBVo = ptCmSvc.getPtCompBVo(compId, langTypCd);
		if(ptCompBVo!=null && ptCompBVo.getAffiliateIds()!=null){
			List<String> affiliateIds=ptCompBVo.getAffiliateIds();
			affiliateIds.add(compId);
			// HashSet 으로 중복ID 제거
			Set<String> hs = new HashSet<String>(affiliateIds);
			affiliateIds = new ArrayList<String>(hs);
			VoUtil.setValue(commonVo, "compId", null);
			VoUtil.setValue(commonVo, "compIdList", affiliateIds);
		}else{
			VoUtil.setValue(commonVo, "compId", compId);
		}
	}
	
}
