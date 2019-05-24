package com.innobiz.orange.web.wr.ctrl;

import java.sql.SQLException;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.svc.EmailSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtLoutSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtMnuDVo;
import com.innobiz.orange.web.pt.vo.PtMnuLoutCombDVo;
import com.innobiz.orange.web.wc.svc.WcMailSvc;
import com.innobiz.orange.web.wc.svc.WcScdManagerSvc;
import com.innobiz.orange.web.wc.vo.WcPromGuestDVo;
import com.innobiz.orange.web.wc.vo.WcSchdlBVo;
import com.innobiz.orange.web.wr.svc.WrAdmSvc;
import com.innobiz.orange.web.wr.svc.WrCmSvc;
import com.innobiz.orange.web.wr.svc.WrMailSvc;
import com.innobiz.orange.web.wr.svc.WrRescMngSvc;
import com.innobiz.orange.web.wr.svc.WrRescSvc;
import com.innobiz.orange.web.wr.vo.WrRescMngBVo;
import com.innobiz.orange.web.wr.vo.WrRescPltSetupDVo;
import com.innobiz.orange.web.wr.vo.WrRezvBVo;
import com.innobiz.orange.web.wr.vo.WrWeekVo;

/** 자원관리 */
@Controller
public class WrRescPortletCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WrRescPortletCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 공통 서비스 */
	@Autowired
	private WrCmSvc wrCmSvc;
	
	/** 메뉴 레이아웃 서비스 */
	@Autowired
	private PtLoutSvc ptLoutSvc;
	
	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;
	
	/** 리소스 서비스 */
	@Autowired
	private WrRescSvc wrRescSvc;
	
	/** 리소스 기본 서비스 */
	@Autowired
	private WrRescMngSvc wrRescMngSvc;
	
	/** 이메일 서비스 */
	@Resource(name = "emEmailSvc")
	private EmailSvc emailSvc;
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 포틀릿에서 보여줄 함 목록 - 자원현황, 본인예약목록, 전체예약목록, 예약심의목록 */
	private static final String[] PLT_LIST = {"my", "all", "resc", "disc"};
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 메일 서비스 */
	@Autowired
	private WrMailSvc wrMailSvc;
	
	/** 일정 메일 서비스 */
	@Autowired
	private WcMailSvc wcMailSvc;
	
	/** 관리자 서비스 */
	@Autowired
	private WrAdmSvc wrAdmSvc;
	
	/** 일정관리 서비스 */
	@Autowired
	private WcScdManagerSvc wcScdManagerSvc;
	
	/** ------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
	
	/** 탭형 게시판 */
	@RequestMapping(value = "/wr/plt/listRescTabPlt")
	public String listRescTabPlt(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="pltId", required=false) String pltId,
			ModelMap model) throws Exception {

		// 목록 캐쉬 방지
		response.setHeader("cache-control","no-store"); // http 1.1   
		response.setHeader("Pragma","no-cache"); // http 1.0   
		response.setDateHeader("Expires",0); // proxy server 에 cache방지. 

		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		List<WrRescPltSetupDVo> storedWrRescPltSetupDVoList = queryWrRescPltSetupDVoList(userVo, pltId, langTypCd);
		List<WrRescPltSetupDVo> targetWrRescPltSetupDVoList = new ArrayList<WrRescPltSetupDVo>();  
		List<WrRescPltSetupDVo> wrRescPltSetupDVoList = new ArrayList<WrRescPltSetupDVo>(); 
		
		// 대상 탭메뉴 초기화
		for(String bxId : PLT_LIST){
			WrRescPltSetupDVo wrRescPltSetupDVo = new WrRescPltSetupDVo();
			wrRescPltSetupDVo.setBxId(bxId);
			targetWrRescPltSetupDVoList.add(wrRescPltSetupDVo);
		}
		
		// 최초 설정상태의 경우 기능메뉴(자원현황)를 디폴트로 설정하고 추가한다. 
		if(storedWrRescPltSetupDVoList==null || storedWrRescPltSetupDVoList.isEmpty()){
			for(WrRescPltSetupDVo wrRescPltSetupDVo  : targetWrRescPltSetupDVoList)
			{
				if(wrRescPltSetupDVo.getBxId().equals("resc") || wrRescPltSetupDVo.getBxId().equals("my") || wrRescPltSetupDVo.getBxId().equals("all"))
					storedWrRescPltSetupDVoList.add(wrRescPltSetupDVo);
			}
		}
		
		// 권한 있는 결재함 체크
		String url, menuId;
		PtMnuDVo ptMnuDVo;
		Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap = ptLoutSvc.getLoutCombByCombIdMap(userVo.getCompId(), langTypCd);
		
		List<PtCdBVo> wrCodeList = ptCmSvc.getCdList("WR_PLT", langTypCd, "Y");

		for(WrRescPltSetupDVo storedWrRescPltSetupDVo  : storedWrRescPltSetupDVoList)
		{
			for(WrRescPltSetupDVo targetWrRescPltSetupDVo  : targetWrRescPltSetupDVoList)
			{
				if(targetWrRescPltSetupDVo.getBxId().equals(storedWrRescPltSetupDVo.getBxId()))
				{
					url = wrRescSvc.getBxUrlByBxId(targetWrRescPltSetupDVo.getBxId());
					menuId = ptSecuSvc.getSecuMenuId(userVo, url);
					ptMnuDVo = getMenuByMenuId(menuId, loutCombByCombIdMap);
					if(ptMnuDVo != null){
						if(targetWrRescPltSetupDVo.getBxId().equals("my") || targetWrRescPltSetupDVo.getBxId().equals("all"))
						{
							for (PtCdBVo cdVo : wrCodeList) 
							{
								if(targetWrRescPltSetupDVo.getBxId().equals(cdVo.getRefVa1()))
								{
									targetWrRescPltSetupDVo.setBxNm(cdVo.getRescNm());
									break;
								}
							}
						}
						else
							targetWrRescPltSetupDVo.setBxNm(ptMnuDVo.getRescNm());
						
						targetWrRescPltSetupDVo.setMenuId(menuId);
						wrRescPltSetupDVoList.add(targetWrRescPltSetupDVo);
					}
					break;
				}
			}
		}
		
		model.put("menuId", wrRescPltSetupDVoList.size()==0?"":wrRescPltSetupDVoList.get(0).getMenuId());
		model.put("wrRescPltSetupDVoList", wrRescPltSetupDVoList);		
		model.put("paramsForList", ParamUtil.getQueryString(request, "tId", "hghtPx", "menuId"));
		return LayoutUtil.getJspPath("/wr/plt/listRescTabPlt");
	}
	
	/** [FRAME]탭형 게시판 */
	@RequestMapping(value = "/wr/listRescTabFrm")
	public String listRescTabFrm(HttpServletRequest request,
			@RequestParam(value = "bxId", required = false) String bxId,
			ModelMap model) throws Exception {
		
		// set, list 로 시작하는 경우 처리
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String compId = userVo.getCompId();
		// 시스템 관리자 여부
		boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		
		String pageRowCnt = ParamUtil.getRequestParam(request, "pageRowCnt", false);
		int rowCnt = 0;
		if(pageRowCnt!=null && !pageRowCnt.isEmpty()){
			try{
				rowCnt = Integer.parseInt(pageRowCnt);
				model.addAttribute("pageRowCnt", pageRowCnt);
			} catch(Exception ignore){}
		}
		if(rowCnt==0){
			String hghtPx = ParamUtil.getRequestParam(request, "hghtPx", true);
			String colYn = ParamUtil.getRequestParam(request, "colYn", true);
			// 한 페이지 레코드수 - 높이에 의한 계산
			int ptlHght = hghtPx==null || hghtPx.isEmpty() ? 0 : Integer.parseInt(hghtPx);
			int tabHght = ptlHght - 35 - (!"N".equals(colYn) ? 22 : 0 );
			rowCnt = Math.max(1, (int)Math.floor(tabHght / 22));
			model.addAttribute("pageRowCnt", Integer.valueOf(rowCnt));
		}
		
		if("resc".equals(bxId)){
			// 조회조건 매핑
			WrRescMngBVo searchVO = new WrRescMngBVo();
			VoUtil.bind(request, searchVO);
			searchVO.setQueryLang(langTypCd);
			if(compId != null ) searchVO.setCompId(compId);
			
			// 카운트 조회
			Integer recodeCount = commonSvc.count(searchVO);
			PersonalUtil.setFixedPaging(request, searchVO, rowCnt, recodeCount);
			
			//목록 조회
			@SuppressWarnings("unchecked")
			List<WrRescMngBVo> list = (List<WrRescMngBVo>)commonSvc.queryList(searchVO);
			
			Map<String, Object> listMap;
			List<Map<String, Object>> rsltMapList = new ArrayList<Map<String, Object>>();
			for(WrRescMngBVo storedWrRescMngBVo : list){
				listMap = VoUtil.toMap(storedWrRescMngBVo, null);
				rsltMapList.add(listMap);
			}
			model.put("rsltMapList", rsltMapList);
			model.put("recodeCount", recodeCount);
			model.put("listPage", "listResc");
			model.put("viewPage", "viewRescPop");
		}else{
			// 조회조건 매핑
			WrRezvBVo searchVO = new WrRezvBVo();
			VoUtil.bind(request, searchVO);
			searchVO.setQueryLang(langTypCd);
			if(compId != null ) searchVO.setCompId(compId);
			
			if("disc".equals(bxId)){
				// 메뉴 관리자 여부
				boolean isMngAdmin = SecuUtil.hasAuth(request, "A", null);
				if(!isSysAdmin && !isMngAdmin) searchVO.setDiscrUid(userVo.getUserUid());//현재 사용자에게 심의 요청된 항목 조회조건
				//심의여부가 있는 항목만 조회
				//searchVO.setWhereSqllet("AND DISC_STAT_CD IN('R','A','J')");
				searchVO.setDiscStatCd("R");
				//searchVO.setOrderBy("CASE WHEN DISC_STAT_CD = 'R' THEN 0 ELSE 1 END ASC , REZV_STRT_DT ASC");
				model.put("listPage", "listRezvDisc");
			}else{
				if("my".equals(bxId)){
					searchVO.setRegrUid(userVo.getUserUid());
				}
				// 현재 날짜 또는 조회한 날짜 세팅
				String paramDay = wrCmSvc.getDateOfDay(null, "week", null, null, 1);
				// 날짜를 기준으로 주간날짜 정보 조회
				WrWeekVo wrWeekVo = wrCmSvc.getWeek(paramDay);
				// 시작 일자와 종료일자 세팅
				searchVO.setDurCat("fromYmd");
				searchVO.setDurStrtDt(wrWeekVo.getStrtDt());
				searchVO.setDurEndDt(wrWeekVo.getEndDt());
								
				//심의없음 또는 승인대기,완료된 항목만 조회
				searchVO.setWhereSqllet("AND DISC_STAT_CD IN('B','R','A')");
				
				/*
				String today = wrCmSvc.getDateOfDay(null, null, null, null, 1);
				// 시작 일자와 종료일자 세팅
				searchVO.setDurCat("fromYmd");
				searchVO.setDurStrtDt(today);
				searchVO.setDurEndDt(today);
				*/
				model.put("listPage", "listRezv");
			}
			
			// 카운트 조회
			Integer recodeCount = commonSvc.count(searchVO);
			PersonalUtil.setFixedPaging(request, searchVO, rowCnt, recodeCount);
			//목록 조회
			@SuppressWarnings("unchecked")
			List<WrRezvBVo> list = (List<WrRezvBVo>)commonSvc.queryList(searchVO);
			// rezvDt:예약일 별 rowIndex:정렬순서, rowCnt:중복수 설정
			wrCmSvc.setRowIndex(list);
			Map<String, Object> listMap;
			List<Map<String, Object>> rsltMapList = new ArrayList<Map<String, Object>>();
			for(WrRezvBVo storedWrRezvBVo : list){
				listMap = VoUtil.toMap(storedWrRezvBVo, null);
				rsltMapList.add(listMap);
			}
			model.put("rsltMapList", rsltMapList);
			model.put("recodeCount", recodeCount);
			
			model.put("viewPage", "viewRezvPop");
		}
		
		model.put("params", ParamUtil.getQueryString(request));
		
		return LayoutUtil.getJspPath("/wr/plt/listRescTabFrm");
	}

	
	
	/** [AJAX]자원예약 수정 (저장) [승인,반려] */
	@RequestMapping(value = "/wr/transRezvDiscAjx")
	public String transRezvDiscAjx(HttpServletRequest request,
			@RequestParam(value = "rezvId", required = false) String rezvId,
			@RequestParam(value = "resEmailYn", required = false) String resEmailYn,
			ModelMap model) throws Exception {
		
		try {
			QueryQueue queryQueue = new QueryQueue();
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			// 조회조건 매핑
			WrRezvBVo wrRezvBVo = new WrRezvBVo();
			wrRezvBVo.setQueryLang(langTypCd);
			wrRezvBVo.setRezvId(rezvId);
			wrRezvBVo = (WrRezvBVo)commonSvc.queryVo(wrRezvBVo);
			
			// 시스템 관리자 여부
			boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
			
			// 메뉴 관리자 여부
			boolean isMngAdmin = SecuUtil.hasAuth(request, "A", null);
			
			//결재자와 같지 않은경우 리턴
			if(rezvId == null || rezvId.isEmpty() || ( !isSysAdmin && !isMngAdmin && !wrRezvBVo.getDiscrUid().equals(userVo.getUserUid()))){
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
			String schdlId = wrRezvBVo.getSchdlId();
			
			//승인시 일정에 등록
			if("A".equals(storedWrRezvBVo.getDiscStatCd())){
				
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
				
				Locale recvLocale = SessionUtil.toLocale(langTypCd);
				//이메일 발송
				String subj = "["+messageProperties.getMessage("wr.jsp.discStatCd"+storedWrRezvBVo.getDiscStatCd()+".title", request) + "]"+messageProperties.getMessage("wr.btn.rescRezv", request) + "-"+wrRezvBVo.getSubj();
				
				String[] toUids = new String[1]; toUids[0] = wrRezvBVo.getRegrUid();
				
				// 자원관리VO
				/*WrRescMngBVo wrRescMngBVo = new WrRescMngBVo();
				wrRescMngBVo.setKndNm(wrRezvBVo.getKndNm());
				wrRescMngBVo.setRescNm(wrRezvBVo.getRescNm());*/
				
				// 메일 내용
				String mailContent = wrMailSvc.getRescMailHTML(request, recvLocale, wrRescMngBVo, wrRezvBVo, null, null, true);
				
				emailSvc.sendMailSvc(userVo.getUserUid(), toUids, subj, mailContent, null, true, true, langTypCd);
			}
			
			// 설정 조회
			Map<String, String> envConfigMap = wrAdmSvc.getEnvConfigMap(model, userVo.getCompId());
			
			// 참석자 사용여부
			boolean isGuestUse=envConfigMap!=null && envConfigMap.containsKey("guestUseYn") && "Y".equals(envConfigMap.get("guestUseYn"));
	    	
			// 승인이면서 참석자사용여부'Y' 이면 메일발송
			if("A".equals(storedWrRezvBVo.getDiscStatCd()) && isGuestUse && schdlId!=null){
				WcPromGuestDVo wcPromGuestDVo = new WcPromGuestDVo();
				wcPromGuestDVo.setSchdlId(schdlId);
				// 참석자 목록
				List<WcPromGuestDVo> wcPromGuestDVoList = wcScdManagerSvc.getPromGuestLst(wcPromGuestDVo);
				
				// 일정VO
				WcSchdlBVo wcsVo = new WcSchdlBVo();
				wcsVo.setQueryLang(langTypCd);
				wcsVo.setSchdlId(schdlId);
				wcsVo =(WcSchdlBVo)commonSvc.queryVo(wcsVo);
				wcsVo.setEmailSendYn("Y");
		    	
				// 메일내용에 추가할 파라미터맵
				Map<String, String> paramMap = wrMailSvc.getParamMap(wrRescMngBVo);
				
		    	// 메일 발송
				wcMailSvc.sendEmail(request, userVo, wcsVo, wcPromGuestDVoList, paramMap);
			}	
						
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("result", "ok");
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}

	
	/** 자원관리 포틀릿 설정 */
	@RequestMapping(value = "/wr/plt/setWrRescBxPltSetupPop")
	public String setBaBxPltSetupPop(HttpServletRequest request,
			@Parameter(name="pltId", required=false) String pltId,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		List<WrRescPltSetupDVo> storedWrRescPltSetupDVoList = queryWrRescPltSetupDVoList(userVo, pltId, langTypCd);
		List<WrRescPltSetupDVo> targetWrRescPltSetupDVoList = new ArrayList<WrRescPltSetupDVo>();  
		List<WrRescPltSetupDVo> wrRescPltSetupDVoList = new ArrayList<WrRescPltSetupDVo>(); 
		List<String> storedBxIdList = new ArrayList<String>();
		
		// 대상 탭메뉴 초기화
		for(String bxId : PLT_LIST){
			WrRescPltSetupDVo wrRescPltSetupDVo = new WrRescPltSetupDVo();
			wrRescPltSetupDVo.setBxId(bxId);
			targetWrRescPltSetupDVoList.add(wrRescPltSetupDVo);
		}

		// 최초 설정상태의 경우 기능메뉴(자원현황)를 디폴트로 설정하고 추가한다. 
		if(storedWrRescPltSetupDVoList==null || storedWrRescPltSetupDVoList.isEmpty()){
			for(WrRescPltSetupDVo wrRescPltSetupDVo  : targetWrRescPltSetupDVoList)
			{
				if(wrRescPltSetupDVo.getBxId().equals("resc") || wrRescPltSetupDVo.getBxId().equals("my") || wrRescPltSetupDVo.getBxId().equals("all"))
					wrRescPltSetupDVo.setUseYn("Y");
				wrRescPltSetupDVoList.add(wrRescPltSetupDVo);
			}
		}
		else
		{
			// 저장목록이 있을경우 우선순위 정렬로 추가한다.
			for(WrRescPltSetupDVo storedWrRescPltSetupDVo  : storedWrRescPltSetupDVoList)
			{
				storedBxIdList.add(storedWrRescPltSetupDVo.getBxId());
				storedWrRescPltSetupDVo.setUseYn("Y");
				
				//대상목록에서 리소스명을 가져온다.
				for(WrRescPltSetupDVo targetWrRescPltSetupDVo  : targetWrRescPltSetupDVoList)
				{
					if(storedWrRescPltSetupDVo.getBxId().equals(targetWrRescPltSetupDVo.getBxId()))
					{
						storedWrRescPltSetupDVo.setBxNm(targetWrRescPltSetupDVo.getBxNm());
						break;
					}
				}

				wrRescPltSetupDVoList.add(storedWrRescPltSetupDVo);	
			}
			
			// 대상목록에서 저장목록을 제외하고 추가한다.
			for(WrRescPltSetupDVo targetWrRescPltSetupDVo  : targetWrRescPltSetupDVoList)
			{
				if(storedBxIdList.contains(targetWrRescPltSetupDVo.getBxId())) continue;
				wrRescPltSetupDVoList.add(targetWrRescPltSetupDVo);	
			}
		}
		
		// 권한 있는 결재함 체크
		String url, menuId;
		PtMnuDVo ptMnuDVo;
		Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap = ptLoutSvc.getLoutCombByCombIdMap(userVo.getCompId(), langTypCd);
		
		List<PtCdBVo> wrCodeList = ptCmSvc.getCdList("WR_PLT", langTypCd, "Y");
		
		List<WrRescPltSetupDVo> wbBcPltSetupDVoMnuList = new ArrayList<WrRescPltSetupDVo>(); 
		for(WrRescPltSetupDVo wrRescPltSetupDVo  : wrRescPltSetupDVoList)
		{
			url = wrRescSvc.getBxUrlByBxId(wrRescPltSetupDVo.getBxId());
			menuId = ptSecuSvc.getSecuMenuId(userVo, url);
			ptMnuDVo = getMenuByMenuId(menuId, loutCombByCombIdMap);
			if(ptMnuDVo != null){
				if(wrRescPltSetupDVo.getBxId().equals("my") || wrRescPltSetupDVo.getBxId().equals("all"))
				{
					for (PtCdBVo cdVo : wrCodeList) 
					{
						if(wrRescPltSetupDVo.getBxId().equals(cdVo.getRefVa1()))
						{
							wrRescPltSetupDVo.setBxNm(cdVo.getRescNm());
							break;
						}
					}
				}
				else
					wrRescPltSetupDVo.setBxNm(ptMnuDVo.getRescNm());
				wrRescPltSetupDVo.setMenuId(menuId);
				wbBcPltSetupDVoMnuList.add(wrRescPltSetupDVo);
			}
		}
		
		model.put("wrRescPltSetupDVoList", wbBcPltSetupDVoMnuList);
		
		return LayoutUtil.getJspPath("/wr/plt/setWrRescBxPltSetupPop");
	}
	
	/** 사용자별 결재 포틀릿 설정 조회 */
	private List<WrRescPltSetupDVo> queryWrRescPltSetupDVoList(UserVo userVo, String pltId, String langTypCd) throws SQLException{
		WrRescPltSetupDVo wrRescPltSetupDVo = new WrRescPltSetupDVo();
		wrRescPltSetupDVo.setUserUid(userVo.getUserUid());
		wrRescPltSetupDVo.setPltId(pltId);
		wrRescPltSetupDVo.setQueryLang(langTypCd);
		wrRescPltSetupDVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<WrRescPltSetupDVo> BaPltSetupDVoList = (List<WrRescPltSetupDVo>)commonSvc.queryList(wrRescPltSetupDVo);
		return BaPltSetupDVoList;
	}
	
	/** [AJAX]  포틀릿 설정 저장 */
	@RequestMapping(value = "/wr/plt/transWrRescBxPltSetupAjx")
	public String transWrRescBxPltSetupAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String bxIds = (String)jsonObject.get("bxIds");
		String pltId = (String)jsonObject.get("pltId");
		
		QueryQueue queryQueue = new QueryQueue();
		WrRescPltSetupDVo wrRescPltSetupDVo;
		if(bxIds!=null && !bxIds.isEmpty()){
			
			wrRescPltSetupDVo = new WrRescPltSetupDVo();
			wrRescPltSetupDVo.setUserUid(userVo.getUserUid());
			queryQueue.delete(wrRescPltSetupDVo);
			Integer sortOrdr = 1;
			
			for(String bxId : bxIds.split(",")){
				wrRescPltSetupDVo = new WrRescPltSetupDVo();
				wrRescPltSetupDVo.setUserUid(userVo.getUserUid());
				wrRescPltSetupDVo.setBxId(bxId);
				wrRescPltSetupDVo.setPltId(pltId);
				wrRescPltSetupDVo.setSortOrdr(sortOrdr.toString());
				sortOrdr++;
				queryQueue.insert(wrRescPltSetupDVo);
			}
		}

		try {
			
			commonSvc.execute(queryQueue);
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", locale));
			model.put("result", "ok");
			
		} catch(Exception e){
			String message = e.getMessage();
			model.put("message", (message==null || message.isEmpty() ? e.getClass().getCanonicalName() : message));
			/*LOGGER.error(e.getClass().getCanonicalName()
					+"\n"+e.getStackTrace()[0].toString()
					+(message==null || message.isEmpty() ? "" : "\n"+message));*/
		}
		
		return LayoutUtil.returnJson(model);
	}
	
	/** menuId로 메뉴 구하기 */
	private PtMnuDVo getMenuByMenuId(String menuId, Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap){
		if(menuId==null) return null;
		PtMnuLoutCombDVo ptMnuLoutCombDVo = loutCombByCombIdMap.get(Hash.hashId(menuId));
		return ptMnuLoutCombDVo==null ? null : ptMnuLoutCombDVo.getPtMnuDVo();
	}
	
	
	
}
