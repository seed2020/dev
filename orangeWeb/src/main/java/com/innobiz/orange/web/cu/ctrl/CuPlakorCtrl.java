package com.innobiz.orange.web.cu.ctrl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import com.innobiz.orange.web.cu.svc.CuPlakorSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.wc.svc.WcScdManagerSvc;
import com.innobiz.orange.web.wc.vo.WcSchdlBVo;

/** 물류마감 */
@Controller
public class CuPlakorCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(CuPlakorCtrl.class);

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Resource(name = "cuPlakorSvc")
	private CuPlakorSvc cuPlakorSvc;
	
	/** 일정 공통 서비스 */
	@Autowired
	private WcScdManagerSvc wcScdManagerSvc;
	
	/** 목록 - 일정 */
	@RequestMapping(value = {"/wc/task/listCalDeadline", "/cu/adm/task/listCalDeadline"})
	public String listCalDeadline(HttpServletRequest request,
			@RequestParam(value = "strtDt", required = false) String strtDt,
			@RequestParam Map<String,Object> params,
			ModelMap model) throws Exception {
		
		if(strtDt==null || strtDt.isEmpty() || !isDateChk(strtDt)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			GregorianCalendar cal = new GregorianCalendar();
			cal.add(Calendar.MONTH, -1);
			strtDt = sdf.format(cal.getTime()); //StringUtil.getCurrYmd();
		}
		params.put("yyyymm", strtDt.replaceAll("[-: ]", "").substring(0, 6));
		
		// 마감구분 목록 조회
		List<Map<String,String>> closeKindList=cuPlakorSvc.getCloseKndList(params);
		
		model.put("closeKindList", closeKindList);
		model.put("strtDt", strtDt);
		
		// print css 적용
		if(request.getAttribute("printView")==null){
			request.setAttribute("printView", "print100");
		}
				
		return LayoutUtil.getJspPath("/cu/plakor/task/listCalDeadline");
	}
	
	/** [AJAX] 업무마감 조회 - 일정 */
	@RequestMapping(value = "/wc/task/listCalDeadlineAjx")
	public String listCalDeadlineAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {
		
		try{
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			
			String start = (String) object.get("start");
			String end = (String) object.get("end");
			if (start == null || start.isEmpty()) {//|| end == null || end.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 파라미터 맵
			Map<String, Object> params = new HashMap<String, Object>();
						
			params.put("start", start.replaceAll("[-: ]", "").substring(0, 6));
			params.put("end", end.replaceAll("[-: ]", "").substring(0, 6));
			
			boolean isSearch=true;
			String closeKind = (String) object.get("closeKind");
			if(closeKind==null || closeKind.isEmpty()){				
				// 마감구분 목록 조회
				List<Map<String,String>> closeKindMapList=cuPlakorSvc.getCloseKndList(params);
				if(closeKindMapList==null || closeKindMapList.size()==0){
					isSearch=false;
				}else{
					List<String> closeKindList=new ArrayList<String>();
					for(Map<String,String> closeKindMap : closeKindMapList){
						closeKindList.add(closeKindMap.get("closeKind"));
					}
					// HashSet 데이터 형태로 생성되면서 중복 제거됨
					HashSet<String> hs = new HashSet<String>(closeKindList);
					// ArrayList 형태로 다시 생성
					closeKindList = new ArrayList<String>(hs);
					params.put("closeKindList", closeKindList);
				}
			}else{
				params.put("closeKind", closeKind);
			}
			
			List<Map<String,String>> deadlineList=isSearch ? cuPlakorSvc.getDeadlineList(params) : null;
			model.put("deadlineList", deadlineList);
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			//음력일자 계산을 위해 한달 전부터 조회
			String realStrtDt = wcScdManagerSvc.getDateOfDay(start, "month", "s", null, 1);
			realStrtDt = wcScdManagerSvc.getDateOfDay(realStrtDt, "month", "s", null, 1);
			
			// 공휴일 조회
			WcSchdlBVo wcSchdlBVo = new WcSchdlBVo();
			wcSchdlBVo.setInstanceQueryId("com.innobiz.orange.web.wc.dao.SchdlBDao.selectSchdlB");
			wcSchdlBVo.setQueryLang(langTypCd);
			wcSchdlBVo.setCompId(userVo.getCompId());
			wcSchdlBVo.setNatCd(wcScdManagerSvc.getNatCd(userVo)); // 국가코드
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
	
	/** 목록 */
	@RequestMapping(value = {"/wc/task/listDeadline", "/cu/adm/task/listDeadline"})
	public String listDeadline(HttpServletRequest request,
			@RequestParam(value = "strtDt", required = false) String strtDt,
			@RequestParam Map<String,Object> params,
			ModelMap model) throws Exception {
		
		/*if(strtDt==null || strtDt.isEmpty() || !isDateChk(strtDt)) strtDt = StringUtil.getCurrYmd();
		params.put("yyyymm", strtDt.replaceAll("[-: ]", "").substring(0, 6));*/
		
		if(strtDt==null || strtDt.isEmpty() || !isDateChk(strtDt)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			GregorianCalendar cal = new GregorianCalendar();
			cal.add(Calendar.MONTH, -1);
			strtDt = sdf.format(cal.getTime()); //StringUtil.getCurrYmd();
		}
		params.put("yyyymm", strtDt.replaceAll("[-: ]", "").substring(0, 6));
		
		
		// 마감구분 목록 조회
		List<Map<String,String>> closeKindList=cuPlakorSvc.getCloseKndList(params);
		
		Map<String,List<Map<String,String>>> deadlineListMap=null;
		if(closeKindList.size()>0){
			deadlineListMap=new HashMap<String,List<Map<String,String>>>();
			List<Map<String,String>> deadlineList=null;
			for(Map<String,String> closeKindMap : closeKindList){
				params.put("closeKind", closeKindMap.get("closeKind"));
				deadlineList=cuPlakorSvc.getDeadlineList(params);
				if(deadlineList!=null && deadlineList.size()>0)
					deadlineListMap.put("deadlineMap_"+closeKindMap.get("closeKind"), deadlineList);
			}
		}

		model.put("closeKindList", closeKindList);
		model.put("deadlineListMap", deadlineListMap);
		model.put("strtDt", strtDt);
		
		// print css 적용
		if(request.getAttribute("printView")==null){
			request.setAttribute("printView", "print100");
		}
				
		return LayoutUtil.getJspPath("/cu/plakor/task/listDeadline");
	}
	
	/** 날짜 형식 체크(yyyy-mm-dd) */
	public boolean isDateChk(String val){
		return Pattern.matches("^\\d{4}-\\d{2}-\\d{2}$", val);
	}
	
}
