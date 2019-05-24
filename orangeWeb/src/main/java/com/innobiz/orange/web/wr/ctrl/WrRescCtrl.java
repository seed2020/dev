package com.innobiz.orange.web.wr.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.innobiz.orange.web.cm.config.CustConfig;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.svc.EmailSvc;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtPsnSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.vo.PtPushMsgDVo;
import com.innobiz.orange.web.wc.svc.WcCmSvc;
import com.innobiz.orange.web.wc.svc.WcMailSvc;
import com.innobiz.orange.web.wc.svc.WcScdManagerSvc;
import com.innobiz.orange.web.wc.vo.WcPromGuestDVo;
import com.innobiz.orange.web.wc.vo.WcSchdlBVo;
import com.innobiz.orange.web.wr.svc.WrAdmSvc;
import com.innobiz.orange.web.wr.svc.WrCmSvc;
import com.innobiz.orange.web.wr.svc.WrMailSvc;
import com.innobiz.orange.web.wr.svc.WrRescMngSvc;
import com.innobiz.orange.web.wr.svc.WrRescSvc;
import com.innobiz.orange.web.wr.vo.WrDayVo;
import com.innobiz.orange.web.wr.vo.WrMonthVo;
import com.innobiz.orange.web.wr.vo.WrRescKndBVo;
import com.innobiz.orange.web.wr.vo.WrRescMngBVo;
import com.innobiz.orange.web.wr.vo.WrRezvBVo;
import com.innobiz.orange.web.wr.vo.WrWeekVo;

/** 자원관리 */
@Controller
public class WrRescCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WrRescCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 공통 서비스 */
	@Autowired
	private WrCmSvc wrCmSvc;
	
	/** 리소스 서비스 */
	@Autowired
	private WrRescSvc wrRescSvc;
	
	/** 리소스 기본 서비스 */
	@Autowired
	private WrRescMngSvc wrRescMngSvc;
	
	/** 이메일 서비스 */
	@Resource(name = "emEmailSvc")
	private EmailSvc emailSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 관리자 서비스 */
	@Autowired
	private WrAdmSvc wrAdmSvc;
	
//	/** 포털 보안 서비스 */
//	@Autowired
//	private PtSecuSvc ptSecuSvc;
	
	/** 사용자 개인 설정 서비스 */
	@Autowired
	private PtPsnSvc ptPsnSvc;
	
	/** 일정관리 서비스 */
	@Autowired
	private WcScdManagerSvc wcScdManagerSvc;
	
	/** 메일 서비스 */
	@Autowired
	private WrMailSvc wrMailSvc;
	
	/** schdlID생성 서비스*/
	@Resource(name = "wcCmSvc")
	private WcCmSvc wcCmSvc;
	
	/** 일정 메일 서비스 */
	@Autowired
	private WcMailSvc wcMailSvc;
	
	
	/** ------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
	
	/** 자원현황목록 조회 */
	@RequestMapping(value = "/wr/listResc")
	public String listResc(HttpServletRequest request,
			ModelMap model) throws Exception {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 조회조건 매핑
		WrRescMngBVo searchVO = new WrRescMngBVo();
		VoUtil.bind(request, searchVO);
		searchVO.setQueryLang(langTypCd);
		searchVO.setUseYn("Y");
		
		// 회사ID 세팅
		wrCmSvc.setCompIdList(request, model, userVo.getCompId(), langTypCd, searchVO, true);
		
		//목록 조회 건수
		Integer recodeCount = commonSvc.count(searchVO);
		PersonalUtil.setPaging(request, searchVO, recodeCount);
		
		//목록 조회
		@SuppressWarnings("unchecked")
		List<WrRescMngBVo> wrRescMngBVoList = (List<WrRescMngBVo>)commonSvc.queryList(searchVO);
		
		//이미지 정보 세팅
		wrRescMngSvc.setWrRescMngImg(wrRescMngBVoList);
		model.put("wrRescMngBVoList", wrRescMngBVoList);
		model.put("recodeCount", recodeCount);
		
		/** 종류코드 조회 */
		WrRescKndBVo wrRescKndBVo = new WrRescKndBVo();
		wrRescKndBVo.setQueryLang(langTypCd);
		wrRescKndBVo.setCompId(searchVO.getCompId());
		wrRescKndBVo.setUseYn("Y");//사용여부
		@SuppressWarnings("unchecked")
		List<WrRescKndBVo> wrRescKndBVoList = (List<WrRescKndBVo>)commonSvc.queryList(wrRescKndBVo);
		model.put("wrRescKndBVoList", wrRescKndBVoList);
		
		model.put("paramsForList", ParamUtil.getQueryString(request, "typ"));
		model.put("params", ParamUtil.getQueryString(request));
		
		// get 파라미터를 post 파라미터로 전달하기 위해
		model.put("paramEntryList", ParamUtil.getEntryMapList(request));
		model.put("listPage", "listResc");
		
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});
				
		return LayoutUtil.getJspPath("/wr/listResc");
	}
	
	/** 엑셀파일 다운로드 (사용자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/wr/excelDownLoad","/wr/adm/excelDownLoad"}, method = RequestMethod.POST)
	public ModelAndView excelDownLoad(HttpServletRequest request,
			@Parameter(name="ext", required=false) String ext,
			ModelMap model) throws Exception {
		
		try {
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			String uri = request.getRequestURI();
			boolean isAdmPage = uri.indexOf("/adm/") > 0; // 관리자 페이지 인지 여부
			boolean hasAdmAuth = isAdmPage && SecuUtil.hasAuth(request, "A", null);	
				
			List<Map<String, Object>> rsltMapList = new ArrayList<Map<String, Object>>();
			
			//보여줄 목록 정의
			String[][] columns = null;
			String fncMy = ParamUtil.getRequestParam(request, "fncMy", false);
			if("listResc".equals(listPage)){
				columns = new String[][]{{"kndNm","cols.rescKnd"},{"rescNm","cols.rescNm"},{"rescLoc","cols.rescLoc"},{"regDt","cols.regDt"}};
				
				// 조회조건 매핑
				WrRescMngBVo searchVO = new WrRescMngBVo();
				VoUtil.bind(request, searchVO);
				searchVO.setQueryLang(langTypCd);
				// 회사ID 세팅
				wrCmSvc.setCompIdList(request, model, userVo.getCompId(), langTypCd, searchVO, false);
				
				//목록 조회
				List<WrRescMngBVo> list = (List<WrRescMngBVo>)commonSvc.queryList(searchVO);
				
				Map<String, Object> listMap;
				for(WrRescMngBVo storedWrRescMngBVo : list){
					listMap = VoUtil.toMap(storedWrRescMngBVo, null);
					rsltMapList.add(listMap);
				}
			}else{
				columns = new String[][]{{"kndNm","cols.rescKnd"},{"rescNm","cols.rescNm"},{"rezvDt","cols.rezvDt"},{"subj","cols.subj"},{"regrNm","cols.rezvr"},{"discStatCd","cols.note"}};
				// 조회조건 매핑
				WrRezvBVo searchVO = new WrRezvBVo();
				VoUtil.bind(request, searchVO);
				searchVO.setQueryLang(langTypCd);
				// 회사ID 세팅
				wrCmSvc.setCompIdList(request, model, userVo.getCompId(), langTypCd, searchVO, false);
				
				//본인예약현황
				if(fncMy != null && "Y".equals(fncMy)){
					searchVO.setRegrUid(userVo.getUserUid());
				}
				
				// 메뉴 관리자 여부
				//boolean isMngAdmin = SecuUtil.hasAuth(request, "A", null);
				if("listRezvDisc".equals(listPage) ){//예약 심의인 경우
					if(!hasAdmAuth) searchVO.setDiscrUid(userVo.getUserUid());//현재 사용자에게 심의 요청된 항목 조회조건
					//심의여부가 있는 항목만 조회
					searchVO.setWhereSqllet("AND DISC_STAT_CD IN('R','A','J')");
					searchVO.setOrderBy("CASE WHEN DISC_STAT_CD = 'R' THEN 0 ELSE 1 END ASC , REZV_STRT_DT ASC");
				}
				
				//목록 조회
				List<WrRezvBVo> list = (List<WrRezvBVo>)commonSvc.queryList(searchVO);
				// rezvDt:예약일 별 rowIndex:정렬순서, rowCnt:중복수 설정
				wrCmSvc.setRowIndex(list);
				
				Map<String, Object> listMap;
				for(WrRezvBVo storedWrRezvBVo : list){
					listMap = VoUtil.toMap(storedWrRezvBVo, null);
					rsltMapList.add(listMap);
				}
			}
						
			if(rsltMapList.size() == 0 ){
				ModelAndView mv = new ModelAndView("cm/result/commonResult");
				mv.addObject("message", messageProperties.getMessage("em.msg.noExcelData", request));
				return mv;
			}
			
			// 파일 목록조회
			ModelAndView mv = new ModelAndView("excelDownloadView");
			
			//컬럼명
			List<String> colNames = new ArrayList<String>();
			//데이터
			List<String> colValue = null;
			Map<String,Object> colValues = new HashMap<String,Object>();
			Map<String,Object> tempMap = null;
			for(int i=0;i<rsltMapList.size();i++){
				tempMap = rsltMapList.get(i);
				colValue = new ArrayList<String>();
				for(String[] column : columns){
					if("rezvDt".equals(column[0])){
						colValue.add(tempMap.get("rezvStrtDt")+"~"+tempMap.get("rezvEndDt"));
					}else if("discStatCd".equals(column[0])){
						colValue.add(messageProperties.getMessage("wr.jsp.discStatCd"+tempMap.get(column[0])+".title", request));
					}else{
						colValue.add((String)tempMap.get(column[0]));
					}
					if( i == 0 )	colNames.add(messageProperties.getMessage(column[1], request)); //컬럼명 세팅
				}
				colValues.put("col"+i,colValue);//데이터 세팅
			}
			
			//시트명 세팅
			String sheetNm = "wr.jsp.";
			if(listPage.indexOf("Mng") > -1) listPage = listPage.replaceAll("Mng", "");
			sheetNm += listPage;
			if("listRezv".equals(listPage)){
				sheetNm += fncMy != null && "Y".equals(fncMy) ? ".my" : ".all";
			}
			sheetNm += ".title";
			mv.addObject("sheetName", messageProperties.getMessage(sheetNm, request));//시트명
			mv.addObject("colNames", colNames);//컬럼명
			mv.addObject("colValues", colValues);//데이터
			mv.addObject("fileName", messageProperties.getMessage(sheetNm, request));//파일명
			mv.addObject("ext", ext == null ? "xlsx" : ext);//파일 확장자(없으면 xls)
			
			return mv;
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
		}
		return null;
	}
	
	
	/** [POPUP] 자원현황상세보기 */
	@RequestMapping(value = {"/wr/viewRescPop","/wr/adm/viewRescPop", "/pt/lout/viewRescPop"})
	public String viewRescPop(HttpServletRequest request,
			@RequestParam(value = "rescMngId", required = true) String rescMngId,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		WrRescMngBVo searchVO = new WrRescMngBVo();
		searchVO.setQueryLang(langTypCd);
		searchVO.setRescMngId(rescMngId);
		
		WrRescMngBVo wrRescMngBVo = (WrRescMngBVo)wrRescMngSvc.getWbBcInfo(searchVO);
		
		if (wrRescMngBVo.getRescId() != null) {
			// 리소스기본(WR_RESC_B) 테이블 - 조회, 모델에 추가
			wrRescSvc.queryRescBVo(wrRescMngBVo.getRescId(), model);
		}
		
		model.put("wrRescMngBVo", wrRescMngBVo);
		
		model.put("paramsForList", ParamUtil.getQueryString(request, "data","rescMngId"));
		
		// print css 적용
		if(request.getAttribute("printView")==null){
			request.setAttribute("printView", "print100");
		}
				
		return LayoutUtil.getJspPath("/wr/viewRescPop");
	}
	
	/** [AJAX] 자원정보 조회 */
	@RequestMapping(value = {"/wr/selectRescMngAjx","/wr/adm/selectRescMngAjx", "/pt/lout/selectRescMngAjx"})
	public String selectRescMngAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		try {
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			
			String rescKndId = (String)jsonObject.get("rescKndId");
			String rescMngId = (String)jsonObject.get("rescMngId");
			
			WrRescMngBVo searchVO = new WrRescMngBVo();
			searchVO.setQueryLang(langTypCd);
			searchVO.setRescKndId(rescKndId);
			searchVO.setRescMngId(rescMngId);
			
			WrRescMngBVo wrRescMngBVo = (WrRescMngBVo)wrRescMngSvc.getWbBcInfo(searchVO);
			if(wrRescMngBVo!=null){
				if(wrRescMngBVo.getRescId() != null) {
					// 리소스기본(WR_RESC_B) 테이블 - 조회, 모델에 추가
					wrRescSvc.queryRescBVo(wrRescMngBVo.getRescId(), model);
				}
				model.put("wrRescMngBVo", JsonUtil.toJson(wrRescMngBVo));
			}
			model.put("result", "ok");
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return JsonUtil.returnJson(model);
	}
	
	/** ------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
	
	/** 자원예약목록 조회 */
	@RequestMapping(value = {"/wr/listRezv", "/wr/listRezvSrch", "/wr/listRezvDisc","/wr/listRezvSrchMng", "/wr/adm/listRezvDisc"})
	public String listRezv(HttpServletRequest request,
			@RequestParam(value = "rescKndId", required = false) String rescKndId,
			@RequestParam(value = "fncMy", required = false) String fncMy,
			@RequestParam(value = "rezvId", required = false) String rezvId,
			@RequestParam(value = "discStatCd", required = false) String discStatCd,
			ModelMap model) throws Exception {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String path = request.getRequestURI();
		path = path.substring(path.lastIndexOf("/")+1);
		path = path.substring(0,path.lastIndexOf("."));
		model.put("listPage", path);
		
		// 조회조건 매핑
		WrRezvBVo searchVO = new WrRezvBVo();
		VoUtil.bind(request, searchVO);
		searchVO.setQueryLang(langTypCd);
		
		// 타회사 적용 여부
		boolean isSetCompId=!(request.getRequestURI().startsWith("/wr/listRezvDisc") || request.getRequestURI().startsWith("/wr/adm/listRezvDisc"));
		// 회사ID 세팅
		String compId=isSetCompId ? wrCmSvc.setCompIdList(request, model, userVo.getCompId(), langTypCd, searchVO, true) : userVo.getCompId();
		
		if(!isSetCompId)
			searchVO.setCompId(compId);
		
		String uri = request.getRequestURI();
		boolean isAdmPage = uri.indexOf("/adm/") > 0; // 관리자 페이지 인지 여부
		boolean hasAdmAuth = isAdmPage && SecuUtil.hasAuth(request, "A", null);
		
		//본인예약현황
		if(fncMy != null && "Y".equals(fncMy)){
			searchVO.setRegrUid(userVo.getUserUid());
		}
		
		// 메뉴 관리자 여부
		//boolean isMngAdmin = SecuUtil.hasAuth(request, "A", null);
		if("listRezvDisc".equals(path) ){//예약 심의인 경우
			if(!hasAdmAuth) searchVO.setDiscrUid(userVo.getUserUid());//현재 사용자에게 심의 요청된 항목 조회조건
			//심의여부가 있는 항목만 조회
			searchVO.setWhereSqllet("AND DISC_STAT_CD IN('R','A','J')");
			searchVO.setOrderBy("CASE WHEN DISC_STAT_CD = 'R' THEN 0 ELSE 1 END ASC , REZV_STRT_DT ASC");
		}
		//목록 조회 건수
		Integer recodeCount = commonSvc.count(searchVO);
		PersonalUtil.setPaging(request, searchVO, recodeCount);
		
		if("listRezvDisc".equals(path) && rezvId!=null && discStatCd!=null && "R".equals(discStatCd)){//예약 심의인 경우
			if(recodeCount!=null && recodeCount.intValue()==1)
				model.put("autoPopup", Boolean.TRUE);
			else
				model.put("autoMsg", messageProperties.getMessage("wr.msg.disc.dup", request)); // wr.msg.disc.dup=이미 처리된 예약입니다.
		}
		//목록 조회
		@SuppressWarnings("unchecked")
		List<WrRezvBVo> wrRezvBVoList = (List<WrRezvBVo>)commonSvc.queryList(searchVO);
		// rezvDt:예약일 별 rowIndex:정렬순서, rowCnt:중복수 설정
		wrCmSvc.setRowIndex(wrRezvBVoList);
		
		model.put("wrRezvBVoList", wrRezvBVoList);
		model.put("recodeCount", recodeCount);
		
		//자원종류 및 자원목록 세팅
		wrRescSvc.setRescInfo(request, model, rescKndId, compId, false , null , "");
		
		model.put("paramsForList", ParamUtil.getQueryString(request, "typ"));
		model.put("params", ParamUtil.getQueryString(request));
				
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});
		
		// print css 적용
		if(request.getAttribute("printView")==null){
			request.setAttribute("printView", "print100");
		}
		
		// get 파라미터를 post 파라미터로 전달하기 위해
		model.put("paramEntryList", ParamUtil.getEntryMapList(request));
				
		return LayoutUtil.getJspPath("/wr/listRezvStat");
	}
	
	/** [POPUP]자원예약목록 조회 팝업*/
	@RequestMapping(value = {"/wr/listRezvStatPop","/wr/adm/listRezvStatPop", "/pt/lout/listRezvStatPop"})
	public String listRezvStatPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		model.put("params", ParamUtil.getQueryString(request));
		return LayoutUtil.getJspPath("/wr/listRezvStatPop");
	}
	
	/** [FRAME]자원예약목록 조회 */
	@RequestMapping(value = {"/wr/listRezvStatFrm","/wr/adm/listRezvStatFrm", "/pt/lout/listRezvStatFrm"})
	public String listRezvStatFrm(HttpServletRequest request,
			@RequestParam(value = "rescKndId", required = false) String rescKndId,
			@RequestParam(value = "rescMngId", required = false) String rescMngId,
			ModelMap model) throws Exception {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 조회조건 매핑
		WrRezvBVo searchVO = new WrRezvBVo();
		VoUtil.bind(request, searchVO);
		searchVO.setQueryLang(langTypCd);
		
		// 회사ID 세팅
		String compId = wrCmSvc.setCompIdList(request, model, userVo.getCompId(), langTypCd, searchVO, false);
		
		//자원종류 및 자원목록 세팅
		wrRescSvc.setRescInfo(request, model, rescKndId, compId, false , null , "");
		
		//심의없음 또는 승인대기,완료된 항목만 조회
		searchVO.setWhereSqllet("AND DISC_STAT_CD IN('B','R','A')");
				
		//목록 조회 건수
		Integer recodeCount = commonSvc.count(searchVO);
		PersonalUtil.setPaging(request, searchVO, recodeCount);
		
		//목록 조회
		@SuppressWarnings("unchecked")
		List<WrRezvBVo> wrRezvBVoList = (List<WrRezvBVo>)commonSvc.queryList(searchVO);
		// rezvDt:예약일 별 rowIndex:정렬순서, rowCnt:중복수 설정
		wrCmSvc.setRowIndex(wrRezvBVoList);
				
		model.put("wrRezvBVoList", wrRezvBVoList);
		model.put("recodeCount", recodeCount);
		
		return LayoutUtil.getJspPath("/wr/listRezvStatFrm");
	}
	
	/** 자원예약 현황 조회 */
	@RequestMapping(value = "/wr/listRezvStat")
	public String listRezvStat(HttpServletRequest request,
			@RequestParam(value = "rescKndId", required = false) String rescKndId,
			@RequestParam(value = "startDay", required = false) String startDay,
			@RequestParam(value = "schedulePmValue", required = false) String schedulePmValue,
			@RequestParam(value = "fncMy", required = false) String fncMy,
			@RequestParam(value = "schDay", required = false) String schDay,
			@RequestParam(value = "listType", required = false) String listType,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 조회조건 매핑
		WrRezvBVo searchVO = new WrRezvBVo();
		VoUtil.bind(request, searchVO);
		searchVO.setQueryLang(langTypCd);
		
		// 회사ID 세팅
		String compId=wrCmSvc.setCompIdList(request, model, userVo.getCompId(), langTypCd, searchVO, true);
		
		//본인예약현황
		if(fncMy != null && "Y".equals(fncMy)){
			searchVO.setRegrUid(userVo.getUserUid());
		}
		
		//특정일자
		if(schDay != null && !schDay.isEmpty()){
			startDay = schDay;
		}
		
		//오늘
		if(schedulePmValue != null && "t".equals(schedulePmValue)){
			startDay = null;
		}
		if(listType == null || listType.isEmpty()){
			listType = "week";
			searchVO.setListType(listType);
		}
		
		//하루의 시작시간, 종료시간
		if(searchVO.getStrtTime() == null || "".equals(searchVO.getStrtTime())) searchVO.setStrtTime("07:00");
		if(searchVO.getEndTime() == null || "".equals(searchVO.getEndTime())) searchVO.setEndTime("24:00");
		
		// 현재 날짜 또는 조회한 날짜 세팅
		String paramDay = wrCmSvc.getDateOfDay(startDay, listType, schedulePmValue, null, 1);
		
		String durStrtDt = "";
		String durEndDt = "";
		Object dayVo = null;
		if("month".equals(listType)){
			dayVo = wrCmSvc.getMonth(paramDay);
			model.put("wrMonthVo", (WrMonthVo)dayVo);
			durStrtDt = ((WrMonthVo)dayVo).getBeforeStrtDt();
			durEndDt = ((WrMonthVo)dayVo).getAfterEndDt();
			//일정관리에 등록되어 있는 기념일 조회
			wrCmSvc.setSpclDays(((WrMonthVo)dayVo), durStrtDt, durEndDt);
		}else if("day".equals(listType)){
			dayVo = wrCmSvc.getDay(paramDay);
			model.put("wrDayVo", (WrDayVo)dayVo);
			durStrtDt = paramDay;
			durEndDt = paramDay;
			//일정관리에 등록되어 있는 기념일 조회
			wrCmSvc.setSpclDays((WrDayVo)dayVo, paramDay, paramDay);
		}else{
			// 날짜를 기준으로 주간날짜 정보 조회
			dayVo = wrCmSvc.getWeek(paramDay);
			//Map<String,String> weekMonth = wrCmSvc.getDateWeek(paramDay);
			model.put("wrWeekVo", (WrWeekVo)dayVo);
			durStrtDt = ((WrWeekVo)dayVo).getStrtDt();
			durEndDt = ((WrWeekVo)dayVo).getEndDt();
			//일정관리에 등록되어 있는 기념일 조회
			wrCmSvc.setSpclDays((WrWeekVo)dayVo, durStrtDt, durEndDt);
		}
		
		// 시작 일자와 종료일자 세팅
		searchVO.setDurCat("fromYmd");
		searchVO.setDurStrtDt(durStrtDt);
		searchVO.setDurEndDt(durEndDt);
		
		//심의없음 또는 승인대기,완료된 항목만 조회
		searchVO.setWhereSqllet("AND DISC_STAT_CD IN('B','R','A')");
		//자원종류 및 자원목록 세팅
		wrRescSvc.setRescInfo(request, model, rescKndId, compId, "week".equals(listType) ? true : false , searchVO , "");
		
		//목록 조회
		@SuppressWarnings("unchecked")
		List<WrRezvBVo> wrRezvBVoList = (List<WrRezvBVo>)commonSvc.queryList(searchVO);
		if("month".equals(listType) || "weeks".equals(listType) || "day".equals(listType)){
			// 일정에 대한 순서 세팅
			wrCmSvc.setRowIndex(dayVo,wrRezvBVoList,model);
		}else{
			//wrCmSvc.setRowIndex(wrRezvBVoList);
			model.put("wrRezvBVoList", wrRezvBVoList);
		}
		model.put("paramsForList", ParamUtil.getQueryString(request, "typ"));
		model.put("params", ParamUtil.getQueryString(request));
				
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});
		
		model.put("listPage", "listRezvStat");
		model.put("paramDay", paramDay);
		
		return LayoutUtil.getJspPath("/wr/listRezvStat");
	}
	
	public Integer getYmdToInteger(String ymd){
		Integer result = Integer.parseInt(ymd.replaceAll("-", ""));
		return result.intValue();
	}
	
	/** [AJAX] 비어 있는 시간인지 조회  []*/
	@RequestMapping(value = {"/wr/selectRezvAjx","/wr/adm/selectRezvAjx", "/pt/lout/selectRezvAjx"})
	public String selectRezvAjx(HttpServletRequest request,
			HttpServletResponse response,
			@Parameter(name="rescKndId", required=false) String rescKndId,
			@Parameter(name="rescMngId", required=false) String rescMngId,
			@Parameter(name="rezvStrtDt", required=false) String rezvStrtDt,
			@Parameter(name="rezvEndDt", required=false) String rezvEndDt,
			@Parameter(name="rezvId", required=false) String rezvId,
			ModelMap model) throws Exception {
		
		try {
			if (rescKndId == null || rescKndId.isEmpty() || rescMngId == null || rescMngId.isEmpty() || rezvStrtDt == null || rezvStrtDt.isEmpty() || rezvEndDt == null || rezvEndDt.isEmpty()) {
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				LOGGER.error("parameter : null  msg:"+msg);
				model.put("message", msg);
				return JsonUtil.returnJson(model);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			WrRezvBVo searchVO = new WrRezvBVo();
			
			// 회사ID 세팅
			wrCmSvc.setCompIdList(request, model, userVo.getCompId(), langTypCd, searchVO, false);
					
			searchVO.setRescKndId(rescKndId);//자원종류
			searchVO.setRescMngId(rescMngId);//자원
			// 시작 일자와 종료일자 세팅
			searchVO.setDurCat("fromTime");
			searchVO.setDurStrtDt(rezvStrtDt);
			searchVO.setDurEndDt(rezvEndDt);
			
			// 심의없음 또는 승인대기,완료된 항목만 조회
			//searchVO.setWhereSqllet("AND DISC_STAT_CD IN('B','R','A')");
						
			String whereSql = "AND DISC_STAT_CD IN('B','R','A')";
			
			//수정일 경우 수정 정보를 제외한 중복 시간 체크
			if(rezvId != null && !"".equals(rezvId)){
				whereSql+=" AND REZV_ID != '"+rezvId+"'";
				//searchVO.setWhereSqllet("AND REZV_ID != '"+rezvId+"'");
			}
			
			searchVO.setWhereSqllet(whereSql);
			
			//목록 조회 건수
			Integer recodeCount = commonSvc.count(searchVO);
			model.put("count", recodeCount);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
        return JsonUtil.returnJson(model);
	}
	
	/** [POPUP] 자원예약상세보기 */
	@RequestMapping(value = {"/wr/viewRezvPop","/wr/adm/viewRezvPop"})
	public String viewRezvPop(HttpServletRequest request,
			@RequestParam(value = "rezvId", required = true) String rezvId,
			ModelMap model) throws Exception {
		
		String listPage = ParamUtil.getRequestParam(request, "listPage", true);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
					
		WrRezvBVo wrRezvBVo = new WrRezvBVo();
		wrRezvBVo.setQueryLang(langTypCd);
		wrRezvBVo.setRezvId(rezvId);
		
		// 회사ID 세팅
		wrCmSvc.setCompIdList(request, model, userVo.getCompId(), langTypCd, wrRezvBVo, false);
			
		String uri = request.getRequestURI();
		boolean isAdmPage = uri.indexOf("/adm/") > 0; // 관리자 페이지 인지 여부
		boolean hasAdmAuth = isAdmPage && SecuUtil.hasAuth(request, "A", null);	
		
		wrRezvBVo.setWithLob(true);//clob 데이터의 조회여부
		wrRezvBVo = (WrRezvBVo)commonSvc.queryVo(wrRezvBVo);
		model.put("paramsForList", ParamUtil.getQueryString(request, "data","rezvId","rezvStrtDt","rezvEndDt" , "listPage"));
		model.put("wrRezvBVo", wrRezvBVo);
		
		// 메뉴 관리자 여부
		//boolean isMngAdmin = SecuUtil.hasAuth(request, "A", null);
		
		//심의여부
		String discYn = "N"; 
		if("R".equals(wrRezvBVo.getDiscStatCd()) && (wrRezvBVo.getDiscrUid().equals(userVo.getUserUid()) || hasAdmAuth)){
			discYn = "Y";
		}
		model.put("discYn", discYn);
		
		model.put("listPage", listPage);
		
		// 설정 조회
		Map<String, String> envConfigMap = wrAdmSvc.getEnvConfigMap(model, userVo.getCompId());
		
		// 일정대상 목록
		if(envConfigMap!=null && envConfigMap.containsKey("tgtUseYn") && "Y".equals(envConfigMap.get("tgtUseYn"))){
			model.put("schdlKndCdList", wrAdmSvc.getSchdlTgtList(request));
		}
		
		// 참석자 사용여부
		if(envConfigMap!=null && envConfigMap.containsKey("guestUseYn") && "Y".equals(envConfigMap.get("guestUseYn")) && wrRezvBVo.getSchdlId()!=null && !wrRezvBVo.getSchdlId().isEmpty()){
			// 참석자 목록
			WcPromGuestDVo wcPromGuestDVo = new WcPromGuestDVo();
			wcPromGuestDVo.setSchdlId(wrRezvBVo.getSchdlId());
			List<WcPromGuestDVo> wcPromGuestDVoList = wcScdManagerSvc.getPromGuestLst(wcPromGuestDVo);
		
			model.put("wcPromGuestDVoList", wcPromGuestDVoList);
		}
				
		// print css 적용
		if(request.getAttribute("printView")==null){
			request.setAttribute("printView", "print100");
		}
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if("Y".equals(sysPlocMap.get("mailEnable"))){
			model.put("mailEnable", "Y");
		}
		
		return LayoutUtil.getJspPath("/wr/viewRezvPop");
	}
	
	/** [POPUP] 자원예약 등록 수정 화면 출력 */
	@RequestMapping(value = {"/wr/setRezvPop", "/wr/adm/setRezvPop", "/pt/lout/setRezvPop"})
	public String setRezvPop(HttpServletRequest request,
			@RequestParam(value = "rezvId", required = false) String rezvId,
			ModelMap model) throws Exception {
		
		String listPage     = ParamUtil.getRequestParam(request, "listPage", true);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		WrRezvBVo wrRezvBVo = new WrRezvBVo();
		// 회사ID 세팅
		String compId=wrCmSvc.setCompIdList(request, model, userVo.getCompId(), langTypCd, wrRezvBVo, false);
		if(rezvId != null && !rezvId.isEmpty()){
			wrRezvBVo.setQueryLang(langTypCd);
			wrRezvBVo.setRezvId(rezvId);
			
			wrRezvBVo.setWithLob(true);//clob 데이터의 조회여부
			wrRezvBVo = (WrRezvBVo)commonSvc.queryVo(wrRezvBVo);
			
		}else{
			VoUtil.bind(request, wrRezvBVo);
			
			//예약시간 초기화
			if(wrRezvBVo.getRezvStrtDt() != null && !"".equals(wrRezvBVo.getRezvStrtDt())){
				wrRezvBVo.setRezvEndDt(wrRescSvc.initBullRezvDt(wrRezvBVo.getRezvStrtDt()));
			}else{
				wrRezvBVo.setRezvStrtDt(wrRescSvc.initBullRezvDt(null));
				wrRezvBVo.setRezvEndDt(wrRescSvc.initBullRezvDt(wrRezvBVo.getRezvStrtDt()));
			}
			
			String paramRescKndId = ParamUtil.getRequestParam(request, "paramRescKndId", false);
			if(paramRescKndId!=null && !paramRescKndId.isEmpty()){
				wrRezvBVo.setRescKndId(paramRescKndId);
			}
			
			String paramRescMngId = ParamUtil.getRequestParam(request, "paramRescMngId", false);
			if(paramRescMngId!=null && !paramRescMngId.isEmpty()){
				wrRezvBVo.setRescMngId(paramRescMngId);
			}
			
		}
		model.put("paramsForList", ParamUtil.getQueryString(request, "data","rezvId","rezvStrtDt","rezvEndDt" , "listPage", "paramRescKndId", "paramRescMngId"));
		model.put("wrRezvBVo", wrRezvBVo);
		
		//자원종류 및 자원목록 세팅
		wrRescSvc.setRescInfo(request, model, wrRezvBVo.getRescKndId(), compId, true , null , "Y");
		
		// 설정 조회
		Map<String, String> envConfigMap = wrAdmSvc.getEnvConfigMap(model, userVo.getCompId());
		
		// 일정대상 목록
		if(envConfigMap!=null && envConfigMap.containsKey("tgtUseYn") && "Y".equals(envConfigMap.get("tgtUseYn"))){
			model.put("schdlKndCdList", wrAdmSvc.getSchdlTgtList(request));
		}
		
		// 참석자 사용여부
		if(envConfigMap!=null && envConfigMap.containsKey("guestUseYn") && "Y".equals(envConfigMap.get("guestUseYn"))){
			// 참석자 목록
			List<WcPromGuestDVo> wcPromGuestDVoList = null;
			
			if(rezvId != null && !rezvId.isEmpty() && wrRezvBVo!=null && wrRezvBVo.getSchdlId()!=null && !wrRezvBVo.getSchdlId().isEmpty()){
					WcPromGuestDVo wcPromGuestDVo = new WcPromGuestDVo();
					wcPromGuestDVo.setSchdlId(wrRezvBVo.getSchdlId());
					wcPromGuestDVoList = wcScdManagerSvc.getPromGuestLst(wcPromGuestDVo);
			}
			// UI 구성용 - 빈 VO 하나 더함
			if(wcPromGuestDVoList == null) wcPromGuestDVoList = new ArrayList<WcPromGuestDVo>();
			wcPromGuestDVoList.add(new WcPromGuestDVo());
			model.put("wcPromGuestDVoList", wcPromGuestDVoList);
		}
				
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});

		// 최대 본문 사이즈 model에 추가
		wrRescSvc.putBodySizeToModel(request, model);
		
		model.put("listPage", listPage);
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if("Y".equals(sysPlocMap.get("mailEnable"))){
			model.put("mailEnable", "Y");
		}
		
		return LayoutUtil.getJspPath("/wr/setRezvPop");
	}
	
	/** 자원예약  등록 수정 (저장) */
	@RequestMapping(value = {"/wr/transRezv","/wr/adm/transRezv", "/pt/lout/transRezv"})
	public String transRezv(HttpServletRequest request,
			@RequestParam(value = "menuId", required = false) String menuId,
			@RequestParam(value = "rezvId", required = false) String rezvId,
			@RequestParam(value="rescKndId", required=false) String rescKndId,
			@RequestParam(value="rescMngId", required=false) String rescMngId,
			ModelMap model) throws Exception {
		
		try {
			if (rescKndId == null || rescKndId.isEmpty() || rescMngId == null || rescMngId.isEmpty() ) {
				String message = "wr.msg.rezv.notValidRescMng";
				if( rescKndId == null || rescKndId.isEmpty() ) message = "wr.msg.rezv.notValidRescKnd";
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage(message, request);
				LOGGER.error("parameter : null  msg:"+msg);
				model.put("message", msg);
				return LayoutUtil.getResultJsp();
			}
			
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			// 조회조건 매핑
			WrRezvBVo wrRezvBVo = new WrRezvBVo();
			VoUtil.bind(request, wrRezvBVo);
			
			// 회사ID 세팅
			wrCmSvc.setCompIdList(request, model, userVo.getCompId(), langTypCd, wrRezvBVo, false);
			
			//자원정보 조회
			WrRescMngBVo searchVO = new WrRescMngBVo();
			searchVO.setQueryLang(langTypCd);
			searchVO.setRescMngId(wrRezvBVo.getRescMngId());
			WrRescMngBVo wrRescMngBVo = (WrRescMngBVo)wrRescMngSvc.getWbBcInfo(searchVO);
			if(wrRescMngBVo.getDiscYn() != null && "Y".equals(wrRescMngBVo.getDiscYn())){//자원정보의 심의여부가 'Y'인 경우
				wrRezvBVo.setDiscrUid(wrRescMngBVo.getRescAdmUid());
				wrRezvBVo.setDiscStatCd("R");//진행중
			}else{
				wrRezvBVo.setDiscStatCd("B");//심의없음
			}
			//배경색 삽입
			if(wrRescMngBVo.getBgcolCd() != null && !"".equals(wrRescMngBVo.getBgcolCd()))
				wrRezvBVo.setBgcolCd(wrRescMngBVo.getBgcolCd());
			
			//신규 등록일 경우 저장시점에 중복일정을 한번더 체크한다.
			WrRezvBVo storedSearchVO = new WrRezvBVo();
			storedSearchVO.setRescKndId(wrRezvBVo.getRescKndId());//자원종류
			storedSearchVO.setRescMngId(wrRezvBVo.getRescMngId());//자원
			// 시작 일자와 종료일자 세팅
			storedSearchVO.setDurCat("fromTime");
			storedSearchVO.setDurStrtDt(wrRezvBVo.getRezvStrtDt());
			storedSearchVO.setDurEndDt(wrRezvBVo.getRezvEndDt());
			
			String whereSql = "AND DISC_STAT_CD IN('B','R','A')";
			
			//수정일 경우 수정 정보를 제외한 중복 시간 체크
			if(rezvId != null && !"".equals(rezvId)){
				whereSql+=" AND REZV_ID != '"+rezvId+"'";
				// storedSearchVO.setWhereSqllet("AND REZV_ID != '"+rezvId+"'");
			}
			
			storedSearchVO.setWhereSqllet(whereSql);
			
			String reloadUrl=request.getRequestURI().startsWith("/pt/lout") ? "parent.dialog.close('setRezvDialog')" : "parent.location.replace('" + listPage + "');";
			//목록 조회 건수
			Integer recodeCount = commonSvc.count(storedSearchVO);
			if(recodeCount > 0 ){
				//wr.msg.noDupRezv=예약하려는 일정은 이미 등록되어 있는 일정입니다.
				String msg = messageProperties.getMessage("wr.msg.noDupRezv", request);
				LOGGER.error("parameter : null  msg:"+msg);
				model.put("message", msg);
				model.put("todo", reloadUrl);
				return LayoutUtil.getResultJsp();
			}
			
			// 참석자 목록
			List<WcPromGuestDVo> wcPromGuestDVoList = null;
			
			// 일정ID
			String schdlId=wrRezvBVo.getSchdlId();
			
			if(schdlId==null || schdlId.isEmpty()){
				schdlId=wcCmSvc.createId("WC_SCHDL_B");
				wrRezvBVo.setSchdlId(schdlId);
			}
			//심의없음으로 예약시 일정등록
			if("B".equals(wrRezvBVo.getDiscStatCd())){
				// 관리권한이 아니면 본인 예약건만 수정
				if(SecuUtil.hasAuth(request, "A", null) && rezvId != null && !rezvId.isEmpty()){
					WrRezvBVo vo = new WrRezvBVo();
					vo.setQueryLang(langTypCd);
					vo.setRezvId(rezvId);
					//vo.setCompId(userVo.getCompId());
					vo.setWithLob(false);//clob 데이터의 조회여부
					vo = (WrRezvBVo)commonSvc.queryVo(vo);
					if(vo!=null){
						wrRezvBVo.setRegrUid(vo.getRegrUid());
					}
				}else{
					wrRezvBVo.setRegrUid(userVo.getUserUid());
				}
				
				if(wrRezvBVo.getRegrUid()!=null){
					// 일정등록
					wrCmSvc.saveSchdl(request, queryQueue, wrRezvBVo, wrRescMngBVo);
				}
				
			//심의없음에서 수정을 통해 심의여부가 'Y'인 자원으로 변경할 경우 기 등록된 일정을 삭제한다.
			}else if("R".equals(wrRezvBVo.getDiscStatCd()) && schdlId != null && !"".equals(schdlId)){
				//일정 삭제
				WcSchdlBVo wcSchdlBVo = new WcSchdlBVo();
				wcSchdlBVo.setSchdlId(schdlId);
				queryQueue.delete(wcSchdlBVo);
				
				//wrRezvBVo.setSchdlId("");//일정ID 삭제
			}
			//등록
			if (rezvId == null || rezvId.isEmpty()) {
				rezvId=wrCmSvc.createId("WR_REZV_B");
				wrRezvBVo.setRezvId(rezvId);
				wrRezvBVo.setRegrUid(userVo.getUserUid());
				wrRezvBVo.setRegDt("sysdate");
				queryQueue.insert(wrRezvBVo);
				
			}else{
				wrRezvBVo.setRezvId(rezvId);
				wrRezvBVo.setModrUid(userVo.getUserUid());
				wrRezvBVo.setModDt("sysdate");
				queryQueue.update(wrRezvBVo);
			}
			
			// 설정 조회
			Map<String, String> envConfigMap = wrAdmSvc.getEnvConfigMap(model, userVo.getCompId());
			
			boolean isGuestUse=envConfigMap!=null && envConfigMap.containsKey("guestUseYn") && "Y".equals(envConfigMap.get("guestUseYn"));
			
			String emailSendYn = ParamUtil.getRequestParam(request, "emailSendYn", false);
			
			// 일정VO
			WcSchdlBVo wcsVo = null;
	    	
			// 참석자 사용여부
			if(isGuestUse && schdlId!=null){
				wcsVo = new WcSchdlBVo();
				wcsVo.setSchdlId(schdlId);
		    	wcsVo.setEmailSendYn(emailSendYn);
				wcPromGuestDVoList = wcScdManagerSvc.saveGuest(request, queryQueue, wcsVo);
			}	
						
			commonSvc.execute(queryQueue);
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			
			model.put("todo", reloadUrl);
			
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			
			//결과메일여부 체크시 메일 발송[심의가 있는 자원을 예약할 경우 : 'R']
			if(sysPlocMap!=null && "Y".equals(sysPlocMap.get("mailEnable")) &&
					"R".equals(wrRezvBVo.getDiscStatCd()) && "Y".equals(wrRezvBVo.getResqEmailYn())){
				
				String recvLangTypCd = ptPsnSvc.getLastLginLangTypCd(wrRescMngBVo.getRescAdmUid(), false);
				if(recvLangTypCd==null)
					recvLangTypCd=langTypCd;
				Locale recvLocale = SessionUtil.toLocale(recvLangTypCd);
				
				searchVO = new WrRescMngBVo();
				searchVO.setQueryLang(recvLangTypCd);
				searchVO.setRescMngId(wrRezvBVo.getRescMngId());
				wrRescMngBVo = (WrRescMngBVo)wrRescMngSvc.getWbBcInfo(searchVO);
				
				//이메일 발송
				String subj = "["+messageProperties.getMessage("wr.jsp.discStatCd"+wrRezvBVo.getDiscStatCd()+".title", recvLocale) + "]"+messageProperties.getMessage("wr.btn.rescRezv", recvLocale) + "-"+wrRezvBVo.getSubj();
				
				String[] toUids = new String[1]; toUids[0] = wrRescMngBVo.getRescAdmUid();
							
				// 시스템 정책 조회
				Map<String, String> sysEnvMap = ptSysSvc.getSvrEnvMap();
				
				String msgUrl=null;
				
				String userNm=null;
				if(sysEnvMap!=null && sysEnvMap.containsKey("webDomain")){
					
					String webDomain = sysEnvMap.get("webDomain");
					
//					boolean useSSL = "Y".equals(sysPlocMap.get("useSSL"));
//					String msgUrl = (useSSL ? "https://" : "http://")+webDomain+"/index.do?msgId=";
					msgUrl = "http://"+webDomain+"/index.do?msgId=";
					
					PtPushMsgDVo ptPushMsgDVo = new PtPushMsgDVo();
					String pushMsgId = StringUtil.getNextHexa(24);
					
					ptPushMsgDVo.setPushMsgId(pushMsgId);
					ptPushMsgDVo.setLangTypCd(langTypCd);
					ptPushMsgDVo.setMdRid("WR");
					ptPushMsgDVo.setMdId(rezvId);
					ptPushMsgDVo.setPushSubj(subj);
					
					ptPushMsgDVo.setWebUrl("/wr/listRezvDisc.do?discStatCd=R&rezvId="+rezvId);
					ptPushMsgDVo.setWebAuthUrl("/wr/listRezvDisc.do");
					ptPushMsgDVo.setMobUrl("/wr/viewRezv.do?listPage=listRezvDisc&rezvId="+rezvId);
					ptPushMsgDVo.setMobAuthUrl("/wr/listRezvDisc.do");
					
					ptPushMsgDVo.setUserUid(wrRescMngBVo.getRescAdmUid());
					
					ptPushMsgDVo.setIsuDt("sysdate");
					ptPushMsgDVo.setValdLginCnt("3");
					
					commonSvc.insert(ptPushMsgDVo);
					
					// 사용자기본(OR_USER_B) 테이블
					OrUserBVo orUserBVo = new OrUserBVo();
					orUserBVo.setUserUid(wrRescMngBVo.getRescAdmUid());
					orUserBVo.setQueryLang(recvLangTypCd);
					orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
					if(orUserBVo!=null)
						userNm=orUserBVo.getRescNm();
					
					msgUrl=msgUrl+pushMsgId;
					
				}
				
				// 메일 내용
				String mailContent = wrMailSvc.getRescMailHTML(request, recvLocale, wrRescMngBVo, wrRezvBVo, userNm, msgUrl, false);
				
				// 첨부파일
				String[][] fileList=null;
				emailSvc.sendMailSvc(userVo.getUserUid(), toUids, subj, mailContent, fileList, false, true, langTypCd);
			}
			
			// 심의없음 이면서 참석자여부'Y', 참석자 목록이 있으면 메일 발송
			if("B".equals(wrRezvBVo.getDiscStatCd()) && isGuestUse && wcPromGuestDVoList!=null && wcPromGuestDVoList.size()>0 ){
				wcsVo = new WcSchdlBVo();
				wcsVo.setQueryLang(langTypCd);
				wcsVo.setSchdlId(schdlId);
				wcsVo =(WcSchdlBVo)commonSvc.queryVo(wcsVo);
				wcsVo.setEmailSendYn(emailSendYn);
				
				// 메일내용에 추가할 파라미터맵
				Map<String, String> paramMap = wrMailSvc.getParamMap(wrRescMngBVo);
				
				// 메일 발송
				wcMailSvc.sendEmail(request, userVo, wcsVo, wcPromGuestDVoList, paramMap);
			}
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** 자원예약 수정 (저장) [승인,반려] */
	@RequestMapping(value = {"/wr/transRezvDisc","/wr/adm/transRezvDisc", "/pt/lout/transRezvDisc"})
	public String transRezvDisc(HttpServletRequest request,
			@RequestParam(value = "rezvId", required = false) String rezvId,
			@RequestParam(value = "resEmailYn", required = false) String resEmailYn,
			ModelMap model) throws Exception {
		
		try {
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			// 조회조건 매핑
			WrRezvBVo wrRezvBVo = new WrRezvBVo();
			wrRezvBVo.setQueryLang(langTypCd);
			wrRezvBVo.setRezvId(rezvId);
			wrRezvBVo.setWithLob(true);
			wrRezvBVo = (WrRezvBVo)commonSvc.queryVo(wrRezvBVo);
			
			String uri = request.getRequestURI();
			boolean isAdmPage = uri.indexOf("/adm/") > 0; // 관리자 페이지 인지 여부
			boolean hasAdmAuth = isAdmPage && SecuUtil.hasAuth(request, "A", null);
			
			//결재자와 같지 않은경우 리턴
			if(rezvId == null || rezvId.isEmpty() || ( !hasAdmAuth && !wrRezvBVo.getDiscrUid().equals(userVo.getUserUid()))){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				LOGGER.error("parameter : null  msg:"+msg);
				model.put("message", msg);
				return LayoutUtil.getResultJsp();
			}
			
			// 조회조건 매핑
			WrRezvBVo storedWrRezvBVo = new WrRezvBVo();
			VoUtil.bind(request, storedWrRezvBVo);
			storedWrRezvBVo.setDiscDt("sysdate");
			
			//자원정보 조회
			WrRescMngBVo searchVO = new WrRescMngBVo();
			searchVO.setQueryLang(langTypCd);
			searchVO.setRescMngId(wrRezvBVo.getRescMngId());
			WrRescMngBVo wrRescMngBVo = (WrRescMngBVo)wrRescMngSvc.getWbBcInfo(searchVO);
			
			// 일정ID
			String schdlId=wrRezvBVo.getSchdlId();
						
			//승인시 일정에 등록
			if("A".equals(storedWrRezvBVo.getDiscStatCd())){
				if(schdlId==null || schdlId.isEmpty()){
					schdlId=wcCmSvc.createId("WC_SCHDL_B");
					wrRezvBVo.setSchdlId(schdlId);
				}
				
				//일정등록
				wrCmSvc.saveSchdl(request, queryQueue, wrRezvBVo, wrRescMngBVo);
				storedWrRezvBVo.setSchdlId(schdlId);
			}
			
			queryQueue.update(storedWrRezvBVo);
			
			commonSvc.execute(queryQueue);
			
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			
			//결과메일여부 체크시 메일 발송
			if(sysPlocMap!=null && "Y".equals(sysPlocMap.get("mailEnable")) && "Y".equals(resEmailYn)){
				
				String recvLangTypCd = ptPsnSvc.getLastLginLangTypCd(wrRezvBVo.getRegrUid(), false);
				if(recvLangTypCd==null)
					recvLangTypCd=langTypCd;
				Locale recvLocale = SessionUtil.toLocale(recvLangTypCd);
				
				wrRezvBVo = new WrRezvBVo();
				wrRezvBVo.setQueryLang(recvLangTypCd);
				wrRezvBVo.setRezvId(rezvId);
				wrRezvBVo = (WrRezvBVo)commonSvc.queryVo(wrRezvBVo);
				
				//이메일 발송
				String subj = "["+messageProperties.getMessage("wr.jsp.discStatCd"+storedWrRezvBVo.getDiscStatCd()+".title", recvLocale) + "]"+messageProperties.getMessage("wr.btn.rescRezv", recvLocale) + "-"+wrRezvBVo.getSubj();
				
				String[] toUids = new String[1]; toUids[0] = wrRezvBVo.getRegrUid();
				
				// 자원관리VO
				/*WrRescMngBVo wrRescMngBVo = new WrRescMngBVo();
				wrRescMngBVo.setKndNm(wrRezvBVo.getKndNm());
				wrRescMngBVo.setRescNm(wrRezvBVo.getRescNm());*/
				
				// 메일 내용
				String mailContent = wrMailSvc.getRescMailHTML(request, recvLocale, wrRescMngBVo, wrRezvBVo, null, null, true);
				
				// 첨부파일
				String[][] fileList=null;
				
				if("A".equals(storedWrRezvBVo.getDiscStatCd())) {
					// 한화제약 || 개발서버
					if((CustConfig.CUST_HANWHA && ArrayUtil.isInArray(new String[] {"M000010D", "M000010E", "M000010F", "M000010G", "M000010H"}, wrRescMngBVo.getRescMngId())) || 
							(CustConfig.DEV_SVR && ArrayUtil.isInArray(new String[] {"M0000002", "M0000003", "M0000004"}, wrRescMngBVo.getRescMngId()))) {
						
						String brdId=CustConfig.CUST_HANWHA ? "B000010B" : "B0000002"; // 게시판ID
						String bullId=CustConfig.CUST_HANWHA ? "3322" : "878"; // 게시글ID
						
						// 추가할 메일내용
						String addMailHTML = wrMailSvc.getHanwhaMailHTML(recvLangTypCd, brdId, bullId);
						if(addMailHTML!=null && !addMailHTML.isEmpty()) {
							mailContent+="<p>&nbsp;</p><p>&nbsp;</p>";
							mailContent+=addMailHTML;
							//mailContent.append("<p></p><p></p>");
							//builder.append(addMailHTML);
						}
						// 파일 목록 조회
						List<CommonFileVo> fileVoList=wrMailSvc.getHanwhaMailFiles(bullId);
						if(fileVoList!=null) {
							// 파일 목록
							fileList=emailSvc.getFileToArrayList(fileVoList);
						}
						
					}
				}
				
				
				emailSvc.sendMailSvc(userVo.getUserUid(), toUids, subj, mailContent, fileList, true, true, langTypCd);
			}
			
			// 설정 조회
			Map<String, String> envConfigMap = wrAdmSvc.getEnvConfigMap(model, userVo.getCompId());
			
			// 참석자 사용여부
			boolean isGuestUse=envConfigMap!=null && envConfigMap.containsKey("guestUseYn") && "Y".equals(envConfigMap.get("guestUseYn"));
	    	
			// 승인이면서 참석자사용여부'Y' 이면 메일발송
			if("A".equals(storedWrRezvBVo.getDiscStatCd()) && isGuestUse && schdlId!=null){
				// 참석자 목록 조회
				List<WcPromGuestDVo> wcPromGuestDVoList = wcScdManagerSvc.getSchdlGuestList(schdlId);
				
				if(wcPromGuestDVoList!=null && wcPromGuestDVoList.size()>0){
					// 일정VO
					WcSchdlBVo wcsVo = new WcSchdlBVo();
					wcsVo.setQueryLang(langTypCd);
					wcsVo.setSchdlId(schdlId);
					wcsVo =(WcSchdlBVo)commonSvc.queryVo(wcsVo);
					if(wcsVo!=null){
						wcsVo.setEmailSendYn("Y");
				    	
						// 메일내용에 추가할 파라미터맵
						Map<String, String> paramMap = wrMailSvc.getParamMap(wrRescMngBVo);
						
				    	// 메일 발송
						wcMailSvc.sendEmail(request, userVo, wcsVo, wcPromGuestDVoList, paramMap);
					}
					
				}
				
			}	
						
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.location.replace('" + listPage + "');");
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** 자원예약 삭제 */
	@RequestMapping(value = {"/wr/transRezvDel","/wr/adm/transRezvDel"})
	public String transRezvDel(HttpServletRequest request,
			@RequestParam(value = "rezvId", required = true) String rezvId,
			ModelMap model) throws Exception {
		
		try {
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			
			// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			
			if(rezvId != null && !rezvId.isEmpty() ){
				String[] rezvIds = rezvId.split(",");
				for(String id : rezvIds){
					wrRescSvc.deleteRezv(queryQueue, id);
				}
				
				commonSvc.execute(queryQueue);
			}
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("todo", "parent.location.replace('" + listPage + "');");
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] 자원종류에 따른 자원정보 코드 조회  []*/
	@RequestMapping(value = {"/wr/selectRescAjx","/wr/adm/selectRescAjx", "/pt/lout/selectRescAjx"})
	public String selectRescAjx(HttpServletRequest request,
			HttpServletResponse response,
			@Parameter(name="rescKndId", required=true) String rescKndId,
			@Parameter(name="useYn", required=false) String useYn,
			ModelMap model) throws Exception {
		
		if (rescKndId == null || rescKndId.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		WrRescMngBVo wrRescMngBVo = new WrRescMngBVo();
		wrRescMngBVo.setQueryLang(langTypCd);
		wrRescMngBVo.setRescKndId(rescKndId);
		if(useYn != null && !"".equals(useYn)) wrRescMngBVo.setUseYn("Y");//사용여부
		
		//목록 조회
		@SuppressWarnings("unchecked")
		List<WrRescMngBVo> wrRescMngBVoList = (List<WrRescMngBVo>)commonSvc.queryList(wrRescMngBVo);
		model.put("list", wrRescMngBVoList);
        
        return JsonUtil.returnJson(model);
	}
	
	/** [팝업] 사진 보기 */
    @RequestMapping(value = {"/wr/viewImagePop", "/wr/adm/viewImagePop", "/pt/lout/viewImagePop"})
	public String viewImagePop(HttpServletRequest request,
			@Parameter(name="rescMngId", required=false) String rescMngId,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		WrRescMngBVo searchVO = new WrRescMngBVo();
		searchVO.setQueryLang(langTypCd);
		searchVO.setRescMngId(rescMngId);
		
		WrRescMngBVo wrRescMngBVo = (WrRescMngBVo)wrRescMngSvc.getWbBcInfo(searchVO);
		
		if(wrRescMngBVo!=null){
			if(wrRescMngBVo.getRescId() != null) {
				// 리소스기본(WR_RESC_B) 테이블 - 조회, 모델에 추가
				wrRescSvc.queryRescBVo(wrRescMngBVo.getRescId(), model);
			}
			model.put("wrRescMngBVo", wrRescMngBVo);
		}
		
		return LayoutUtil.getJspPath("/wr/viewImagePop");
	}

}
