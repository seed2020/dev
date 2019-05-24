package com.innobiz.orange.web.wb.ctrl;

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
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtLoutSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.vo.PtMnuDVo;
import com.innobiz.orange.web.pt.vo.PtMnuLoutCombDVo;
import com.innobiz.orange.web.wb.svc.WbBcSvc;
import com.innobiz.orange.web.wb.svc.WbCmSvc;
import com.innobiz.orange.web.wb.vo.WbBcBVo;
import com.innobiz.orange.web.wb.vo.WbBcBumkRVo;
import com.innobiz.orange.web.wb.vo.WbBcMetngDVo;
import com.innobiz.orange.web.wb.vo.WbBcPltSetupDVo;


/** 명함 포틀릿 */
@Controller
public class WbBcPortletCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WbBcPortletCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Autowired
	private WbBcSvc wbBcSvc;
	
//	/** 관련미팅 관리 */
//	@Autowired
//	private WbMetngSvc wbMetngSvc;
	
	/** 공통 서비스 */
	@Autowired
	private WbCmSvc wbCmSvc;
	
	/** 메뉴 레이아웃 서비스 */
	@Autowired
	private PtLoutSvc ptLoutSvc;
	
	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;

	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 포틀릿에서 보여줄 함 목록 - 개인명함, 공개명함, 대리명함, 즐겨찾는명함 */
	private static final String[] PLT_LIST = {"psn", "open", "bumk", "agnt",  "psnMetng", "agntMetng", "pub"};

/** 개인명함 START */
	
	/** 탭형 게시판 */
	@RequestMapping(value = "/wb/plt/listBcTabPlt")
	public String listBullTabPlt(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="pltId", required=false) String pltId,
			ModelMap model) throws Exception {

		// 목록 캐쉬 방지
		response.setHeader("cache-control","no-store"); // http 1.1   
		response.setHeader("Pragma","no-cache"); // http 1.0   
		response.setDateHeader("Expires",0); // proxy server 에 cache방지. 

		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);

		List<WbBcPltSetupDVo> storedWbBcPltSetupDVoList = queryWbBcPltSetupDVoList(userVo, pltId, langTypCd);
		List<WbBcPltSetupDVo> targetWbBcPltSetupDVoList = new ArrayList<WbBcPltSetupDVo>();  
		List<WbBcPltSetupDVo> wbBcPltSetupDVoList = new ArrayList<WbBcPltSetupDVo>(); 
		
		// 대상 탭메뉴 초기화
		for(String bxId : PLT_LIST){
			WbBcPltSetupDVo wbBcPltSetupDVo = new WbBcPltSetupDVo();
			wbBcPltSetupDVo.setBxId(bxId);
			targetWbBcPltSetupDVoList.add(wbBcPltSetupDVo);
		}
		
		// 최초 설정상태의 경우 기능메뉴(개인현황)를 디폴트로 설정하고 추가한다. 
		if(storedWbBcPltSetupDVoList==null || storedWbBcPltSetupDVoList.isEmpty()){
			for(WbBcPltSetupDVo wbBcPltSetupDVo  : targetWbBcPltSetupDVoList)
			{
				if(wbBcPltSetupDVo.getBxId().equals("psn") || wbBcPltSetupDVo.getBxId().equals("open") || wbBcPltSetupDVo.getBxId().equals("bumk"))
					storedWbBcPltSetupDVoList.add(wbBcPltSetupDVo);
			}
		}

		// 권한 있는 결재함 체크
		String url, menuId;
		PtMnuDVo ptMnuDVo;
		Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap = ptLoutSvc.getLoutCombByCombIdMap(userVo.getCompId(), langTypCd);
		
		for(WbBcPltSetupDVo storedWbBcPltSetupDVo  : storedWbBcPltSetupDVoList)
		{
			for(WbBcPltSetupDVo targetWbBcPltSetupDVo  : targetWbBcPltSetupDVoList)
			{
				if(targetWbBcPltSetupDVo.getBxId().equals(storedWbBcPltSetupDVo.getBxId()))
				{
					url = wbBcSvc.getBxUrlByBxId(targetWbBcPltSetupDVo.getBxId());
					menuId = ptSecuSvc.getSecuMenuId(userVo, url);
					ptMnuDVo = getMenuByMenuId(menuId, loutCombByCombIdMap);
					if(ptMnuDVo != null){
						targetWbBcPltSetupDVo.setBxNm(ptMnuDVo.getRescNm());
						targetWbBcPltSetupDVo.setMenuId(menuId);
						wbBcPltSetupDVoList.add(targetWbBcPltSetupDVo);
					}
					break;
				}
			}
		}

		//model.put("tId", wbBcPltSetupDVoList.size()==0?"":wbBcPltSetupDVoList.get(0).getBxId());
		//model.put("menuId", wbBcPltSetupDVoList.size()==0?"":wbBcPltSetupDVoList.get(0).getMenuId());
		model.put("wbBcPltSetupDVoList", wbBcPltSetupDVoList);	
		//model.put("paramsForList", ParamUtil.getQueryString(request, "tId", "hghtPx", "menuId"));
		return LayoutUtil.getJspPath("/wb/plt/listBcTabPlt");
	}
	
	/** [FRAME]탭형 게시판 */
	@RequestMapping(value = "/wb/listBcTabFrm")
	public String listBcTabFrm(HttpServletRequest request,
			@RequestParam(value = "bxId", required = false) String bxId,
			@Parameter(name="schBcRegrUid", required=false) String schBcRegrUid,
			ModelMap model) throws Exception {
		
		// set, list 로 시작하는 경우 처리
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String compId = null;
		// 시스템 관리자 여부
		boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		// 시스템 관리자가 아닌 경우에는 - 자기 회사만 검색
		if(!isSysAdmin){
			compId = userVo.getCompId();
		}
		
		if(bxId == null || bxId.isEmpty()) bxId = "psn";//개인명함
		
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
		
		// 조회조건 매핑
		WbBcBVo wbBcBVo = new WbBcBVo();
		VoUtil.bind(request, wbBcBVo);
		wbBcBVo.setQueryLang(langTypCd);
		if(compId != null ) wbBcBVo.setCompId(compId);
		
		boolean flag = false;
		if("psn".equals(bxId)){
			wbBcBVo.setRegrUid(userVo.getUserUid());
			flag = true;
			model.put("listPage", "listBc");
			model.put("viewPage", "viewBc");
		}else if("open".equals(bxId)){
			wbBcBVo.setSchUserUid(userVo.getUserUid());//사용자UID
			wbBcBVo.setSchCompId(userVo.getCompId());//사용자 회사코드
			wbBcBVo.setSchDeptId(userVo.getDeptId());//사용자 부서코드
			wbBcBVo.setQueryLang(langTypCd);
			String[] schOpenTypCds = (String[])request.getParameterValues("schOpenTypCd");
			if(schOpenTypCds==null) schOpenTypCds = new String[]{"allPubl","deptPubl","apntrPubl"};
			if(schOpenTypCds != null && schOpenTypCds.length > 0 ){
				wbBcBVo.setSchOpenTypCds(schOpenTypCds);
				flag = true;
			}
			model.put("schOpenTypCds", schOpenTypCds);
			model.put("listPage", "listOpenBc");
			model.put("viewPage", "viewOpenBc");
		}else if("agnt".equals(bxId)){
			// 대리명함 조회UID가 있을경우 명함 등록자UID로 세팅한다.
			schBcRegrUid = wbCmSvc.getSchBcRegrUid(schBcRegrUid, userVo, langTypCd, model);
			if(schBcRegrUid != null && !schBcRegrUid.isEmpty() ) {
				wbBcBVo.setRegrUid(schBcRegrUid);
				model.put("schBcRegrUid", schBcRegrUid);
				flag = true;
			}
			// 대리명함 조회UID가 없을경우 대리관리자 목록이 없는 것이므로 명함 조회를 하지 않는다.
			model.put("listPage", "listAgntBc");
			model.put("viewPage", "viewAgntBc");
		}else if("all".equals(bxId)){
			flag = true;
			model.put("listPage", "listAllBc");
			model.put("viewPage", "viewAllBc");
		}else if("bumk".equals(bxId)){
			flag = true;
			model.put("listPage", "setBcBumk");
			model.put("viewPage", "viewBcPop");
		}else if("pub".equals(bxId)){
			wbBcBVo.setPub(true);
			flag = true;
			model.put("listPage", "pub/listPubBc");
			model.put("viewPage", "pub/viewPubBc");
		}
		
		if(flag){
			if("bumk".equals(bxId)){
				WbBcBumkRVo wbBcBumkRVo = new WbBcBumkRVo();
				//VoUtil.bind(request, wbBcBVo);
				wbBcBumkRVo.setRegrUid(userVo.getUserUid());
				wbBcBumkRVo.setQueryLang(langTypCd);
				wbBcBumkRVo.setSchBumkYn("Y");
				
				// 카운트 조회
				Integer recodeCount = commonSvc.count(wbBcBumkRVo);
				PersonalUtil.setFixedPaging(request, wbBcBumkRVo, rowCnt, recodeCount);
						
				// 즐겨찾기 등록된 명함 조회
				@SuppressWarnings("unchecked")
				List<WbBcBVo> list = (List<WbBcBVo>)commonSvc.queryList(wbBcBumkRVo);
				
				Map<String, Object> listMap;
				List<Map<String, Object>> rsltMapList = new ArrayList<Map<String, Object>>();
				//목록을 vo => map => list 형태로 변환
				if(list.size() > 0){
					for(WbBcBVo storedWbBcBVo : list){
						listMap = VoUtil.toMap(storedWbBcBVo, null);
						rsltMapList.add(listMap);
					}
					model.put("rsltMapList", rsltMapList);
					model.put("recodeCount", recodeCount);
				}
			}else{
				// 카운트 조회
				Integer recodeCount = commonSvc.count(wbBcBVo);
				PersonalUtil.setFixedPaging(request, wbBcBVo, rowCnt, recodeCount);
				
				//목록 조회
				@SuppressWarnings("unchecked")
				List<WbBcBVo> list = (List<WbBcBVo>)commonSvc.queryList(wbBcBVo);
				
				Map<String, Object> listMap;
				List<Map<String, Object>> rsltMapList = new ArrayList<Map<String, Object>>();
				for(WbBcBVo storedWbBcBVo : list){
					listMap = VoUtil.toMap(storedWbBcBVo, null);
					rsltMapList.add(listMap);
				}
				
				model.put("recodeCount", recodeCount);
				model.put("rsltMapList", rsltMapList);
			}
		}
		
		return LayoutUtil.getJspPath("/wb/plt/listBcTabFrm");
	}
	
	
	/** 개인 명함 등록 수정 화면 출력 */
	@RequestMapping(value = "/wb/plt/setBcPlt")
	public String setBcPlt(HttpServletRequest request,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/wb/plt/setBcPlt");
	}
	
	/** 개인 명함 등록 수정 (저장) */
	@RequestMapping(value = "/wb/plt/transBc")
	public String transBc(HttpServletRequest request,
			@Parameter(name="menuId", required=false) String menuId,
			ModelMap model) throws Exception {
		
		try {
			QueryQueue queryQueue = new QueryQueue();
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			WbBcBVo wbBcBVo = new WbBcBVo();
			VoUtil.bind(request, wbBcBVo);
			wbBcBVo.setCompId(userVo.getCompId());
			
			wbBcSvc.saveBc(request, queryQueue , wbBcBVo , userVo);
			
			commonSvc.execute(queryQueue);
						
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "location.replace('./setBcPlt.do?menuId="+menuId+"');");
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** 탭형 게시판 */
	@RequestMapping(value = "/wb/plt/listMetngTabPlt")
	public String listMetngTabPlt(HttpServletRequest request,
			@RequestParam(value = "tId", required = false) String tId,
			@RequestParam(value = "tabId", required = false) String tabId,
			ModelMap model) throws Exception {
		if(tId == null || tId.isEmpty()) tId = "psn";//개인명함
		String[] tIds = tId.split(",");
		
		List<String[]> tabList = new ArrayList<String[]>();
		String[] tabs = null;
		
		for(int i=0;i<tIds.length;i++){
			if("psn".equals(tIds[i])) { tabs = new String[2]; tabs[0] = tIds[i]; tabs[1] = "listMetng"; tabList.add(tabs);}
			else if("agnt".equals(tIds[i])) { tabs = new String[2]; tabs[0] = tIds[i]; tabs[1] = "listAgntMetng"; tabList.add(tabs);}
		}
		
		if(tabList.size() == 0 ){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		if(tabId == null || tabId.isEmpty()) tabId = tabList.get(0)[0];
		model.put("tId", tabId);
		model.put("tabList", tabList);
		model.put("paramsForList", ParamUtil.getQueryString(request, "tId", "hghtPx"));
		
		return LayoutUtil.getJspPath("/wb/plt/listMetngTabPlt");
	}
	
	/** 관련 미팅 목록 조회 */
	@RequestMapping(value = "/wb/listMetngTabFrm")
	public String listMetngTabFrm(HttpServletRequest request,
			@RequestParam(value = "bxId", required = false) String bxId,
			@RequestParam(value = "schOpenYn", required = false) String schOpenYn,
			@RequestParam(value = "schBcRegrUid", required=false) String schBcRegrUid,
			ModelMap model) throws Exception {
		
		if(bxId == null || bxId.isEmpty()) bxId = "psnMetng";//관련미팅
		
		//관리자 (사용자 권한 체크)
		if("all".equals(bxId) ) wbCmSvc.checkUserAuth(request, "A", null);
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
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
		
		// 조회조건 매핑
		WbBcMetngDVo wbBcMetngDVo = new WbBcMetngDVo();
		VoUtil.bind(request, wbBcMetngDVo);
		wbBcMetngDVo.setQueryLang(langTypCd);
		
		boolean flag = false;
		if("psnMetng".equals(bxId)){
			wbBcMetngDVo.setCompId(userVo.getCompId());//회사ID
			if(schOpenYn == null || "".equals(schOpenYn) || "N".equals(schOpenYn)){
				wbBcMetngDVo.setRegrUid(userVo.getUserUid());
			}
			flag = true;
			model.put("listPage", "listMetng");
			model.put("viewPage", "viewMetng");
		}else if("agntMetng".equals(bxId)){
			// 대리명함 조회UID가 있을경우 명함 등록자UID로 세팅한다.
			schBcRegrUid = wbCmSvc.getSchBcRegrUid(schBcRegrUid, userVo, langTypCd, model);
			if(schBcRegrUid != null && !schBcRegrUid.isEmpty() ) {
				/** 사용자의 명함 대리 관리자 정보 */
				Map<String,Object> agntInfoMap = wbCmSvc.getAgntInfoMap(request ,schBcRegrUid , userVo.getUserUid() , null );
				wbBcMetngDVo.setRegrUid(schBcRegrUid);
				//대리관리자일 경우 대리권한 부여자의 회사ID를 조회한다.
				wbBcMetngDVo.setCompId((String)agntInfoMap.get("compId"));
				model.put("schBcRegrUid", schBcRegrUid);
				flag = true;
			}
			// 대리명함 조회UID가 없을경우 대리관리자 목록이 없는 것이므로 명함 조회를 하지 않는다.
			model.put("listPage", "listAgntMetng");
			model.put("viewPage", "viewAgntMetng");
		}else if("all".equals(bxId)){
			wbBcMetngDVo.setCompId(userVo.getCompId());//회사ID
			flag = true;
			model.put("listPage", "listAllMetng");
			model.put("viewPage", "viewAllMetng");
		}
		if(flag){
			// 카운트 조회
			Integer recodeCount = commonSvc.count(wbBcMetngDVo);
			PersonalUtil.setFixedPaging(request, wbBcMetngDVo, rowCnt, recodeCount);
			
			@SuppressWarnings("unchecked")
			List<WbBcMetngDVo> wbBcMetngDVoList = (List<WbBcMetngDVo>)commonSvc.queryList(wbBcMetngDVo);
			
			Map<String, Object> listMap;
			List<Map<String, Object>> rsltMapList = new ArrayList<Map<String, Object>>();
			for(WbBcMetngDVo storedWbBcMetngDVo : wbBcMetngDVoList){
				listMap = VoUtil.toMap(storedWbBcMetngDVo, null);
				rsltMapList.add(listMap);
			}
			model.put("recodeCount", recodeCount);
			model.put("rsltMapList", rsltMapList);
		}
		return LayoutUtil.getJspPath("/wb/plt/listMetngTabFrm");
	}
	
	/** 명함관리 포틀릿 설정 */
	@RequestMapping(value = "/wb/plt/setBcBxPltSetupPop")
	public String setWbBcBxPltSetupPop(HttpServletRequest request,
			@Parameter(name="pltId", required=false) String pltId,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		List<WbBcPltSetupDVo> storedWbBcPltSetupDVoList = queryWbBcPltSetupDVoList(userVo, pltId, langTypCd);
		List<WbBcPltSetupDVo> targetWbBcPltSetupDVoList = new ArrayList<WbBcPltSetupDVo>();  
		List<WbBcPltSetupDVo> wbBcPltSetupDVoList = new ArrayList<WbBcPltSetupDVo>(); 
		List<String> storedBxIdList = new ArrayList<String>();
		
		// 대상 탭메뉴 초기화
		for(String bxId : PLT_LIST){
			WbBcPltSetupDVo wbBcPltSetupDVo = new WbBcPltSetupDVo();
			wbBcPltSetupDVo.setBxId(bxId);
			targetWbBcPltSetupDVoList.add(wbBcPltSetupDVo);
		}

		// 최초 설정상태의 경우 기능메뉴(개인명함)를 디폴트로 설정하고 추가한다. 
		if(storedWbBcPltSetupDVoList==null || storedWbBcPltSetupDVoList.isEmpty()){
			for(WbBcPltSetupDVo wbBcPltSetupDVo  : targetWbBcPltSetupDVoList)
			{
				if(wbBcPltSetupDVo.getBxId().equals("psn") || wbBcPltSetupDVo.getBxId().equals("open") || wbBcPltSetupDVo.getBxId().equals("bumk"))
					wbBcPltSetupDVo.setUseYn("Y");
				wbBcPltSetupDVoList.add(wbBcPltSetupDVo);
			}
		}
		else
		{
			// 저장목록이 있을경우 우선순위 정렬로 추가한다.
			for(WbBcPltSetupDVo storedWbBcPltSetupDVo  : storedWbBcPltSetupDVoList)
			{
				storedBxIdList.add(storedWbBcPltSetupDVo.getBxId());
				storedWbBcPltSetupDVo.setUseYn("Y");
				
				//대상목록에서 리소스명을 가져온다.
				for(WbBcPltSetupDVo targetWbBcPltSetupDVo  : targetWbBcPltSetupDVoList)
				{
					if(storedWbBcPltSetupDVo.getBxId().equals(targetWbBcPltSetupDVo.getBxId()))
					{
						storedWbBcPltSetupDVo.setBxNm(targetWbBcPltSetupDVo.getBxNm());
						break;
					}
				}

				wbBcPltSetupDVoList.add(storedWbBcPltSetupDVo);	
			}
			
			// 대상목록에서 저장목록을 제외하고 추가한다.
			for(WbBcPltSetupDVo targetWbBcPltSetupDVo  : targetWbBcPltSetupDVoList)
			{
				if(storedBxIdList.contains(targetWbBcPltSetupDVo.getBxId())) continue;
				wbBcPltSetupDVoList.add(targetWbBcPltSetupDVo);	
			}
		}
		
		// 권한 있는 결재함 체크
		String url, menuId;
		PtMnuDVo ptMnuDVo;
		Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap = ptLoutSvc.getLoutCombByCombIdMap(userVo.getCompId(), langTypCd);
		
		List<WbBcPltSetupDVo> wbBcPltSetupDVoMnuList = new ArrayList<WbBcPltSetupDVo>(); 
		for(WbBcPltSetupDVo wbBcPltSetupDVo  : wbBcPltSetupDVoList)
		{
			url = wbBcSvc.getBxUrlByBxId(wbBcPltSetupDVo.getBxId());
			menuId = ptSecuSvc.getSecuMenuId(userVo, url);
			ptMnuDVo = getMenuByMenuId(menuId, loutCombByCombIdMap);
			if(ptMnuDVo != null){
				wbBcPltSetupDVo.setBxNm(ptMnuDVo.getRescNm());
				wbBcPltSetupDVo.setMenuId(menuId);
				wbBcPltSetupDVoMnuList.add(wbBcPltSetupDVo);
			}
		}
		
		model.put("wbBcPltSetupDVoList", wbBcPltSetupDVoMnuList);
		
		return LayoutUtil.getJspPath("/wb/plt/setBcBxPltSetupPop");
	}
	
	/** 사용자별 결재 포틀릿 설정 조회 */
	private List<WbBcPltSetupDVo> queryWbBcPltSetupDVoList(UserVo userVo, String pltId, String langTypCd) throws SQLException{
		WbBcPltSetupDVo wbBcPltSetupDVo = new WbBcPltSetupDVo();
		wbBcPltSetupDVo.setUserUid(userVo.getUserUid());
		wbBcPltSetupDVo.setPltId(pltId);
		wbBcPltSetupDVo.setQueryLang(langTypCd);
		wbBcPltSetupDVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<WbBcPltSetupDVo> WbBcPltSetupDVoList = (List<WbBcPltSetupDVo>)commonSvc.queryList(wbBcPltSetupDVo);
		return WbBcPltSetupDVoList;
	}
	
	/** [AJAX]  포틀릿 설정 저장 */
	@RequestMapping(value = "/wb/plt/transBcBxPltSetupAjx")
	public String transWrRescBxPltSetupAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String pltId = (String)jsonObject.get("pltId");
		String bxIds = (String)jsonObject.get("bxIds");
		
		QueryQueue queryQueue = new QueryQueue();
		WbBcPltSetupDVo wbBcPltSetupDVo;
		if(bxIds!=null && !bxIds.isEmpty()){
			
			wbBcPltSetupDVo = new WbBcPltSetupDVo();
			wbBcPltSetupDVo.setUserUid(userVo.getUserUid());
			queryQueue.delete(wbBcPltSetupDVo);
			Integer sortOrdr = 1;
			
			for(String bxId : bxIds.split(",")){
				wbBcPltSetupDVo = new WbBcPltSetupDVo();
				wbBcPltSetupDVo.setUserUid(userVo.getUserUid());
				wbBcPltSetupDVo.setBxId(bxId);
				wbBcPltSetupDVo.setPltId(pltId);
				wbBcPltSetupDVo.setSortOrdr(sortOrdr.toString());
				sortOrdr++;
				queryQueue.insert(wbBcPltSetupDVo);
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
