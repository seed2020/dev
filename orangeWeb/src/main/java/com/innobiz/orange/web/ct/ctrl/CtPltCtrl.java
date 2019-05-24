package com.innobiz.orange.web.ct.ctrl;

import java.sql.SQLException;
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
import org.springframework.web.bind.annotation.RequestParam;

//import scala.annotation.cloneable;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.ct.svc.CtAdmNotcSvc;
import com.innobiz.orange.web.ct.svc.CtCmSvc;
import com.innobiz.orange.web.ct.svc.CtCmntSvc;
import com.innobiz.orange.web.ct.svc.CtPrSvc;
import com.innobiz.orange.web.ct.svc.CtSurvSvc;
import com.innobiz.orange.web.ct.vo.CtAdmNotcBVo;
import com.innobiz.orange.web.ct.vo.CtBullMastBVo;
import com.innobiz.orange.web.ct.vo.CtCatBVo;
import com.innobiz.orange.web.ct.vo.CtDebrBVo;
import com.innobiz.orange.web.ct.vo.CtEstbBVo;
import com.innobiz.orange.web.ct.vo.CtPltCmListDVo;
import com.innobiz.orange.web.ct.vo.CtPltFncListDVo;
import com.innobiz.orange.web.ct.vo.CtPltNoPrListDVo;
import com.innobiz.orange.web.ct.vo.CtPltSetupDVo;
import com.innobiz.orange.web.ct.vo.CtPrBVo;
import com.innobiz.orange.web.ct.vo.CtSchdlBVo;
import com.innobiz.orange.web.ct.vo.CtSiteBVo;
import com.innobiz.orange.web.ct.vo.CtSurvBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtLoutSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtMnuDVo;
import com.innobiz.orange.web.pt.vo.PtMnuLoutCombDVo;

/** 커뮤니티 */
@Controller
public class CtPltCtrl {
	
	/** 공통 서비스 */
	@Autowired
	private CtCmntSvc ctCmntSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Autowired
	private CtCmSvc ctCmSvc;
	
	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 포털 보안 서비스(레이아웃 포함) */
	@Autowired
	private PtSecuSvc ptSecuSvc; 
	
//	/** 커뮤니티 게시판 서비스 */
//	@Autowired
//	private CtBullMastSvc ctBullMastSvc;
	
//	/** 커뮤니티 토론실 서비스 */
//	@Autowired
//	private CtDebrSvc ctDebrSvc;
	
//	/** 커뮤니티 쿨사이트 서비스 */
//	@Autowired
//	private CtSiteSvc ctSiteSvc;
	
//	/** 커뮤니티  점수이력 서비스 */
//	@Autowired
//	private CtScreHstSvc ctScreHstSvc;
	
//	/** 커뮤니티  추천이력 서비스 */
//	@Autowired
//	private CtRecmdHstSvc ctRecmdHstSvc;
	
	/** 커뮤니티 홍보 서비스 */
	@Autowired
	private CtPrSvc ctPrSvc;
	
	
	/** 커뮤니티 홍보 서비스 */
	@Autowired
	private CtAdmNotcSvc ctAdmNotcSvc;
	
	/** 메뉴 레이아웃 서비스 */
	@Autowired
	private PtLoutSvc ptLoutSvc;
	
//	/** 조직 공통 서비스 */
//	@Autowired
//	private OrCmSvc orCmSvc;
	
//	/** 첨부파일 서비스 */
//	@Autowired
//	private CtFileSvc ctFileSvc;
	
//	/** 리소스 조회 저장용 서비스 */
//	@Autowired
//	private CtRescSvc ctRescSvc;
	
	/** 리소스 조회 저장용 서비스 */
	@Autowired
	private CtSurvSvc ctSurvSvc;
	
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(CtPltCtrl.class);
	
	/** 일정 포틀릿 */
	@RequestMapping(value = "/ct/plt/listSchdlCmTabPlt")
	public String listSchdlCmTabPlt(HttpServletRequest request,
		@RequestParam(value = "tId", required = false) String tId,
		@RequestParam(value = "tabId", required = false) String tabId,
		ModelMap model) throws Exception {
		if(tId == null || tId.isEmpty()) tId = "ctId";//개인명함
		//String[] tIds = tId.split(",");
	
		model.put("paramsForList", ParamUtil.getQueryString(request, "tId", "hghtPx"));
		
		return LayoutUtil.getJspPath("/ct/plt/listSchdlCmTabPlt");
	}
	
	/** 일정 프레임 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value= "/ct/plt/listSchdlCmTabFrm")
	public String listSchdlCmTabFrm(HttpServletRequest request,
			@RequestParam(value = "tId", required = false) String tId,
			@RequestParam(value = "hghtPx", required = false) String hghtPx,
			@RequestParam(value = "pageCnt", required = false) String pageCnt,
			@RequestParam(value = "titleYn", required = false) String titleYn,
			@RequestParam(value = "pagingYn", required = false) String pagingYn,
			@RequestParam(value = "colYn", required = false) String colYn,
			@RequestParam(value = "ranking", required = false) String ranking,
			ModelMap model) throws Exception{
		
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		/**
		 *  포틀릿의 height를 기준으로 rowcount를 계산한다.
		 */
		int ptlHght = hghtPx==null || hghtPx.isEmpty() ? 0 : Integer.parseInt(hghtPx);
		int tabHght = ptlHght - 35 - (!"N".equals(colYn) ? 22 : 0 );
		int rowCnt = Math.max(1, (int)Math.floor(tabHght / 22));
		request.setAttribute("pageRowCnt", rowCnt);//RowCnt 삽입
		
		CtPltSetupDVo ctPltSetupDVo = new CtPltSetupDVo();
		ctPltSetupDVo.setCtFncId("F000000N");
		ctPltSetupDVo.setUserUid(userVo.getUserUid());
		ctPltSetupDVo.setCompId(userVo.getCompId());
		
		List<CtPltSetupDVo> ctPltSetupList = (List<CtPltSetupDVo>) commonDao.queryList(ctPltSetupDVo); 
		List<String> ctIdArray = new ArrayList<String>();
		for(CtPltSetupDVo ctPltSetup: ctPltSetupList){
			ctIdArray.add(ctPltSetup.getCtId());
		}
		
		if(ctIdArray.size()!=0){
			CtSchdlBVo ctSchdlBVo = new CtSchdlBVo();
			ctSchdlBVo.setCtIdList(ctIdArray);
			ctSchdlBVo.setCompId(userVo.getCompId());
			ctSchdlBVo.setLangTyp(langTypCd);
			
			if(ctPltSetupList.size() != 0){
				ctSchdlBVo.setSchdlStartDt(ctCmntSvc.currentYmdSet(- Integer.parseInt(String.valueOf(ctPltSetupList.get(0).getPerd()))));
				ctSchdlBVo.setSchdlEndDt(ctCmntSvc.currentYmdDay());
			}
			
			
			Integer recodeCount = commonDao.count(ctSchdlBVo);
				
			PersonalUtil.setPaging(request, ctSchdlBVo, recodeCount);
			
			List<CtSchdlBVo> ctSchdlList = new ArrayList<CtSchdlBVo>();
			ctSchdlList = (List<CtSchdlBVo>) commonDao.queryList(ctSchdlBVo);
			
			
			Map<String, Object> ctSchdlMap = null;
			List<Map<String, Object>> ctSchdlMapList = new ArrayList<Map<String, Object>>();
			for(CtSchdlBVo storedCtSiteBVo : ctSchdlList){
				ctSchdlMap = VoUtil.toMap(storedCtSiteBVo, null);
				ctSchdlMapList.add(ctSchdlMap);
			}
			if(recodeCount != 0){
				ctSchdlMap.put("recodeCount", recodeCount);
				ctSchdlMap.put("ctSiteMapList", ctSchdlMapList);
				
				model.put("recodeCount", ctSchdlMap.get("recodeCount"));
				model.put("rsltMapList", ctSchdlMap.get("ctSiteMapList"));
			}
		
		}
		return LayoutUtil.getJspPath("/ct/plt/listSchdlCmTabFrm");
		
	}
	
	/** 설문 포틀릿 */
	@RequestMapping(value = "/ct/plt/listSiteCmTabPlt")
	public String listSiteCmTabPlt(HttpServletRequest request,
		@RequestParam(value = "tId", required = false) String tId,
		@RequestParam(value = "tabId", required = false) String tabId,
		ModelMap model) throws Exception {
		if(tId == null || tId.isEmpty()) tId = "ctId";//개인명함
		//String[] tIds = tId.split(",");
	
		model.put("paramsForList", ParamUtil.getQueryString(request, "tId", "hghtPx"));
		
		return LayoutUtil.getJspPath("/ct/plt/listSiteCmTabPlt");
	}
	
	/** 설문 프레임 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value= "/ct/plt/listSiteCmTabFrm")
	public String listSiteCmTabFrm(HttpServletRequest request,
			@RequestParam(value = "tId", required = false) String tId,
			@RequestParam(value = "hghtPx", required = false) String hghtPx,
			@RequestParam(value = "pageCnt", required = false) String pageCnt,
			@RequestParam(value = "titleYn", required = false) String titleYn,
			@RequestParam(value = "pagingYn", required = false) String pagingYn,
			@RequestParam(value = "colYn", required = false) String colYn,
			@RequestParam(value = "ranking", required = false) String ranking,
			ModelMap model) throws Exception{
		
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		/**
		 *  포틀릿의 height를 기준으로 rowcount를 계산한다.
		 */
		int ptlHght = hghtPx==null || hghtPx.isEmpty() ? 0 : Integer.parseInt(hghtPx);
		int tabHght = ptlHght - 35 - (!"N".equals(colYn) ? 22 : 0 );
		int rowCnt = Math.max(1, (int)Math.floor(tabHght / 22));
		request.setAttribute("pageRowCnt", rowCnt);//RowCnt 삽입
		
		CtPltSetupDVo ctPltSetupDVo = new CtPltSetupDVo();
		ctPltSetupDVo.setCtFncId("F000000O");
		ctPltSetupDVo.setUserUid(userVo.getUserUid());
		ctPltSetupDVo.setCompId(userVo.getCompId());
		
		List<CtPltSetupDVo> ctPltSetupList = (List<CtPltSetupDVo>) commonDao.queryList(ctPltSetupDVo); 
		List<String> ctIdArray = new ArrayList<String>();
		for(CtPltSetupDVo ctPltSetup: ctPltSetupList){
			ctIdArray.add(ctPltSetup.getCtId());
		}
		
		if(ctIdArray.size()!=0){
			CtSiteBVo ctSiteBVo = new CtSiteBVo();
			ctSiteBVo.setCtIdList(ctIdArray);
			ctSiteBVo.setCompId(userVo.getCompId());
			ctSiteBVo.setLangTyp(langTypCd);
			
			if(ctPltSetupList.size() != 0){
				ctSiteBVo.setStrtDt(ctCmntSvc.currentYmdSet(- Integer.parseInt(String.valueOf(ctPltSetupList.get(0).getPerd()))));
				ctSiteBVo.setEndDt(ctCmntSvc.currentYmdDay());
			}
			
			
			Integer recodeCount = commonDao.count(ctSiteBVo);
				
			PersonalUtil.setPaging(request, ctSiteBVo, recodeCount);
			
			List<CtSiteBVo> ctSiteList = new ArrayList<CtSiteBVo>();
			ctSiteList = (List<CtSiteBVo>) commonDao.queryList(ctSiteBVo);
			
			
			Map<String, Object> ctSiteMap = null;
			List<Map<String, Object>> ctSiteMapList = new ArrayList<Map<String, Object>>();
			for(CtSiteBVo storedCtSiteBVo : ctSiteList){
				ctSiteMap = VoUtil.toMap(storedCtSiteBVo, null);
				ctSiteMapList.add(ctSiteMap);
			}
			
			if(recodeCount != 0){
				ctSiteMap.put("recodeCount", recodeCount);
				ctSiteMap.put("ctSiteMapList", ctSiteMapList);
				
				model.put("recodeCount", ctSiteMap.get("recodeCount"));
				model.put("rsltMapList", ctSiteMap.get("ctSiteMapList"));
			}
		
		}
		return LayoutUtil.getJspPath("/ct/plt/listSiteCmTabFrm");
		
	}
	
	/** 토론실 포틀릿 */
	@RequestMapping(value = "/ct/plt/listDebrCmTabPlt")
	public String listDebrCmTabPlt(HttpServletRequest request,
		@RequestParam(value = "tId", required = false) String tId,
		@RequestParam(value = "tabId", required = false) String tabId,
		ModelMap model) throws Exception {
		if(tId == null || tId.isEmpty()) tId = "ctId";//개인명함
		//String[] tIds = tId.split(",");
	
		model.put("paramsForList", ParamUtil.getQueryString(request, "tId", "hghtPx"));
		
		return LayoutUtil.getJspPath("/ct/plt/listDebrCmTabPlt");
	}
	
	/** 토론실 프레임 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value= "/ct/plt/listDebrCmTabFrm")
	public String listDebrCmTabFrm(HttpServletRequest request,
			@RequestParam(value = "tId", required = false) String tId,
			@RequestParam(value = "hghtPx", required = false) String hghtPx,
			@RequestParam(value = "pageCnt", required = false) String pageCnt,
			@RequestParam(value = "titleYn", required = false) String titleYn,
			@RequestParam(value = "pagingYn", required = false) String pagingYn,
			@RequestParam(value = "colYn", required = false) String colYn,
			@RequestParam(value = "ranking", required = false) String ranking,
			ModelMap model) throws Exception{
		
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		/**
		 *  포틀릿의 height를 기준으로 rowcount를 계산한다.
		 */
		int ptlHght = hghtPx==null || hghtPx.isEmpty() ? 0 : Integer.parseInt(hghtPx);
		int tabHght = ptlHght - 35 - (!"N".equals(colYn) ? 22 : 0 );
		int rowCnt = Math.max(1, (int)Math.floor(tabHght / 22));
		request.setAttribute("pageRowCnt", rowCnt);//RowCnt 삽입
		
		CtPltSetupDVo ctPltSetupDVo = new CtPltSetupDVo();
		ctPltSetupDVo.setCtFncId("F000000P");
		ctPltSetupDVo.setUserUid(userVo.getUserUid());
		ctPltSetupDVo.setCompId(userVo.getCompId());
		
		List<CtPltSetupDVo> ctPltSetupList = (List<CtPltSetupDVo>) commonDao.queryList(ctPltSetupDVo); 
		List<String> ctIdArray = new ArrayList<String>();
		for(CtPltSetupDVo ctPltSetup: ctPltSetupList){
			ctIdArray.add(ctPltSetup.getCtId());
		}
		
		if(ctIdArray.size()!=0){
			CtDebrBVo ctDebrBVo = new CtDebrBVo();
			ctDebrBVo.setCtIdList(ctIdArray);
			ctDebrBVo.setCompId(userVo.getCompId());
			ctDebrBVo.setLangTyp(langTypCd);
			
			if(ctPltSetupList.size() != 0){
				ctDebrBVo.setStrtDt(ctCmntSvc.currentYmdSet(- Integer.parseInt(String.valueOf(ctPltSetupList.get(0).getPerd()))));
				ctDebrBVo.setEndDt(ctCmntSvc.currentYmdDay());
			}
			
			
			Integer recodeCount = commonDao.count(ctDebrBVo);
				
			PersonalUtil.setPaging(request, ctDebrBVo, recodeCount);
			
			List<CtDebrBVo> ctDebrList = new ArrayList<CtDebrBVo>();
			ctDebrList = (List<CtDebrBVo>) commonDao.queryList(ctDebrBVo);
			
			
			Map<String, Object> ctDebrMap = null;
			List<Map<String, Object>> ctDebrMapList = new ArrayList<Map<String, Object>>();
			for(CtDebrBVo storedCtDebrBVo : ctDebrList){
				ctDebrMap = VoUtil.toMap(storedCtDebrBVo, null);
				ctDebrMapList.add(ctDebrMap);
			}
			
			if(recodeCount != 0){
				ctDebrMap.put("recodeCount", recodeCount);
				ctDebrMap.put("ctDebrMapList", ctDebrMapList);
				
				model.put("recodeCount", ctDebrMap.get("recodeCount"));
				model.put("rsltMapList", ctDebrMap.get("ctDebrMapList"));
			}
		
		}
		return LayoutUtil.getJspPath("/ct/plt/listDebrCmTabFrm");
		
	}

	
	/** 전체 커뮤니티 포틀릿 */
	@RequestMapping(value = "/ct/plt/listAllCmPlt")
	public String listCatPlt(HttpServletRequest request, ModelMap model) throws Exception{
		//조회조건 매핑
		CtCatBVo ctCatBVo = new CtCatBVo();
		VoUtil.bind(request, ctCatBVo);
		Map<String, Object> rsltMap = ctCmntSvc.getAllCtCatList(request, ctCatBVo);
		
		model.put("allCtCatFldList", rsltMap.get("ctCatFldList"));
		model.put("allCtCatClsList", rsltMap.get("catClsList"));
		
		return LayoutUtil.getJspPath("/ct/plt/listAllCmPlt");
	}
	
	/** 탭형 게시판 */
	@RequestMapping(value = "/ct/plt/listMyCmTabPlt")
	public String listMyCmTabPlt(HttpServletRequest request,
			@RequestParam(value = "tId", required = false) String tId,
			@RequestParam(value = "tabId", required = false) String tabId,
			ModelMap model) throws Exception {
		if(tId == null || tId.isEmpty()) tId = "ctId";//개인명함
		//String[] tIds = tId.split(",");
	
		model.put("paramsForList", ParamUtil.getQueryString(request, "tId", "hghtPx"));
		return LayoutUtil.getJspPath("/ct/plt/listMyCmTabPlt");
	}
	
	/** 나의 커뮤니티 프레임 */
	@RequestMapping(value= "/ct/plt/listMyCmTabFrm")
	public String listMyCm(HttpServletRequest request,
			@RequestParam(value = "tId", required = false) String tId,
			@RequestParam(value = "hghtPx", required = false) String hghtPx,
			@RequestParam(value = "pageCnt", required = false) String pageCnt,
			@RequestParam(value = "titleYn", required = false) String titleYn,
			@RequestParam(value = "pagingYn", required = false) String pagingYn,
			@RequestParam(value = "colYn", required = false) String colYn,
			ModelMap model) throws Exception{
		
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		/**
		 *  포틀릿의 height를 기준으로 rowcount를 계산한다.
		 */
		int ptlHght = hghtPx==null || hghtPx.isEmpty() ? 0 : Integer.parseInt(hghtPx);
		int tabHght = ptlHght - 35 - (!"N".equals(colYn) ? 22 : 0 );
		int rowCnt = Math.max(1, (int)Math.floor(tabHght / 22));
		request.setAttribute("pageRowCnt", rowCnt);//RowCnt 삽입
		
		//조회조건 매핑
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		VoUtil.bind(request, ctEstbBVo);
		//ctEstbBVo.setCompId(userVo.getCompId());
		ctEstbBVo.setLogUserUid(userVo.getUserUid());
		ctEstbBVo.setQueryLang(langTypCd);
		ctEstbBVo.setLangTyp(langTypCd);
		ctEstbBVo.setSignal("my");
		
		List<String> ctActList = new ArrayList<String>();
		ctActList.add("A");  // 활동중인 것만.

		ctEstbBVo.setCtActStatList(ctActList);
		Map<String, Object> rsltMap = ctCmntSvc.getMyCtMapList(request, ctEstbBVo);
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("rsltMapList", rsltMap.get("myCtMapList"));
//		model.put("rsltMapList", rsltMap);
		model.put("logUserUid", ctEstbBVo.getLogUserUid());
		
		return LayoutUtil.getJspPath("/ct/plt/listMyCmTabFrm");
		
	}
	
	/** 관리대상목록 포틀릿 */
	@RequestMapping(value = "/ct/plt/listCmMngTgtTabPlt")
	public String listCmMngTgtTabPlt(HttpServletRequest request,
			@RequestParam(value = "tId", required = false) String tId,
			ModelMap model) throws Exception {
		if(tId == null || tId.isEmpty()) tId = "ctId";//개인명함
		//String[] tIds = tId.split(",");
	
		model.put("paramsForList", ParamUtil.getQueryString(request, "tId", "hghtPx"));

		return LayoutUtil.getJspPath("/ct/plt/listCmMngTgtTabPlt");
	}
	
	/** 관리대상목록 프레임 */
	@RequestMapping(value= "/ct/plt/listCmMngTgtTabFrm")
	public String listCmMngTgtTabFrm(HttpServletRequest request,
			@RequestParam(value = "tId", required = false) String tId,
			@RequestParam(value = "hghtPx", required = false) String hghtPx,
			@RequestParam(value = "pageCnt", required = false) String pageCnt,
			@RequestParam(value = "titleYn", required = false) String titleYn,
			@RequestParam(value = "pagingYn", required = false) String pagingYn,
			@RequestParam(value = "colYn", required = false) String colYn,
			ModelMap model) throws Exception{
		
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		/**
		 *  포틀릿의 height를 기준으로 rowcount를 계산한다.
		 */
		int ptlHght = hghtPx==null || hghtPx.isEmpty() ? 0 : Integer.parseInt(hghtPx);
		int tabHght = ptlHght - 35 - (!"N".equals(colYn) ? 22 : 0 );
		int rowCnt = Math.max(1, (int)Math.floor(tabHght / 22));
		request.setAttribute("pageRowCnt", rowCnt);//RowCnt 삽입
		
		//조회조건 매핑
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		VoUtil.bind(request, ctEstbBVo);
		ctEstbBVo.setCompId(userVo.getCompId());
		ctEstbBVo.setQueryLang(langTypCd);
		ctEstbBVo.setLangTyp(langTypCd);
		//ctEstbBVo.setMngTgtYn("Y");
		ctEstbBVo.setWhereSqllet("AND ( (CEB_T.MNG_TGT_YN = 'Y' AND CEB_T.MODIFY_YN = 'N') OR (CEB_T.MNG_TGT_YN = 'N' AND CEB_T.MODIFY_YN = 'Y'))");
		ctEstbBVo.setCtActStat("A");
		Map<String, Object>	 rsltMap = ctCmntSvc.getCmMngTgtList(request, ctEstbBVo);
		
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("rsltMapList", rsltMap.get("ctMngTgtMapList"));
		
		
	
		return LayoutUtil.getJspPath("/ct/plt/listCmMngTgtTabFrm");
		
	}
	
	/**  신규 커뮤니티 포틀릿 */
	@RequestMapping(value = "/ct/plt/listNewCmTabPlt")
	public String listNewCmPlt(HttpServletRequest request,
			@RequestParam(value = "tId", required = false) String tId,
			ModelMap model) throws Exception {
		if(tId == null || tId.isEmpty()) tId = "ctId";//개인명함
		//String[] tIds = tId.split(",");
	
		model.put("paramsForList", ParamUtil.getQueryString(request, "tId", "hghtPx"));

		return LayoutUtil.getJspPath("/ct/plt/listNewCmTabPlt");
	}
	
	/** 신규 커뮤니티 프레임 */
	@RequestMapping(value= "/ct/plt/listNewCmTabFrm")
	public String listNewCmFrm(HttpServletRequest request,
			@RequestParam(value = "tId", required = false) String tId,
			@RequestParam(value = "hghtPx", required = false) String hghtPx,
			@RequestParam(value = "pageCnt", required = false) String pageCnt,
			@RequestParam(value = "titleYn", required = false) String titleYn,
			@RequestParam(value = "pagingYn", required = false) String pagingYn,
			@RequestParam(value = "colYn", required = false) String colYn,
			ModelMap model) throws Exception{
		
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		/**
		 *  포틀릿의 height를 기준으로 rowcount를 계산한다.
		 */
		int ptlHght = hghtPx==null || hghtPx.isEmpty() ? 0 : Integer.parseInt(hghtPx);
		int tabHght = ptlHght - 35 - (!"N".equals(colYn) ? 22 : 0 );
		int rowCnt = Math.max(1, (int)Math.floor(tabHght / 22));
		request.setAttribute("pageRowCnt", rowCnt);//RowCnt 삽입
		
		//조회조건 매핑
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
//		VoUtil.bind(request, ctEstbBVo);
		ctEstbBVo.setCompId(userVo.getCompId());
		ctEstbBVo.setQueryLang(langTypCd);
		ctEstbBVo.setLangTyp(langTypCd);
		ctEstbBVo.setStrtDt(ctCmntSvc.currentYmdSet(7));
		ctEstbBVo.setEndDt(ctCmntSvc.currentYmdDay());
		ctEstbBVo.setCtActStat("A");
		ctEstbBVo.setCtStat("A");
		
		Map<String, Object>	 rsltMap = ctCmntSvc.getCmMngTgtList(request, ctEstbBVo);
		
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("rsltMapList", rsltMap.get("ctMngTgtMapList"));
		
		
	
		return LayoutUtil.getJspPath("/ct/plt/listNewCmTabFrm");
		
	}
	
	/**  추천 커뮤니티 포틀릿 */
	@RequestMapping(value = "/ct/plt/listRecmdCmTabPlt")
	public String listRecmdCmTabPlt(HttpServletRequest request,
			@RequestParam(value = "tId", required = false) String tId,
			ModelMap model) throws Exception {
		if(tId == null || tId.isEmpty()) tId = "ctId";//개인명함
		//String[] tIds = tId.split(",");
	
		model.put("paramsForList", ParamUtil.getQueryString(request, "tId", "hghtPx"));

		return LayoutUtil.getJspPath("/ct/plt/listRecmdCmTabPlt");
	}
	
	/** 추천 커뮤니티 프레임 */
	@RequestMapping(value= "/ct/plt/listRecmdCmTabFrm")
	public String listRecmdCmTabFrm(HttpServletRequest request,
			@RequestParam(value = "tId", required = false) String tId,
			@RequestParam(value = "hghtPx", required = false) String hghtPx,
			@RequestParam(value = "pageCnt", required = false) String pageCnt,
			@RequestParam(value = "titleYn", required = false) String titleYn,
			@RequestParam(value = "pagingYn", required = false) String pagingYn,
			@RequestParam(value = "colYn", required = false) String colYn,
			ModelMap model) throws Exception{
		
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		/**
		 *  포틀릿의 height를 기준으로 rowcount를 계산한다.
		 */
		int ptlHght = hghtPx==null || hghtPx.isEmpty() ? 0 : Integer.parseInt(hghtPx);
		int tabHght = ptlHght - 35 - (!"N".equals(colYn) ? 22 : 0 );
		int rowCnt = Math.max(1, (int)Math.floor(tabHght / 22));
		request.setAttribute("pageRowCnt", rowCnt);//RowCnt 삽입
		
		//조회조건 매핑
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
//		VoUtil.bind(request, ctEstbBVo);
		ctEstbBVo.setCompId(userVo.getCompId());
		ctEstbBVo.setQueryLang(langTypCd);
		ctEstbBVo.setLangTyp(langTypCd);
//		ctEstbBVo.setStrtDt(ctCmntSvc.currentYmdSet(7));
//		ctEstbBVo.setEndDt(ctCmntSvc.currentYmdDay());
		ctEstbBVo.setRecmdYn("Y");
		ctEstbBVo.setCtActStat("A");
		ctEstbBVo.setCtStat("A");
		
		Map<String, Object>	 rsltMap = ctCmntSvc.getCmMngTgtList(request, ctEstbBVo);
		
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("rsltMapList", rsltMap.get("ctMngTgtMapList"));
		
	
		return LayoutUtil.getJspPath("/ct/plt/listRecmdCmTabFrm");
		
	}
	
	/**  추천 커뮤니티 포틀릿 */
	@RequestMapping(value = "/ct/plt/listPrTabPlt")
	public String listPrTabPlt(HttpServletRequest request,
			@RequestParam(value = "tId", required = false) String tId,
			ModelMap model) throws Exception {
		if(tId == null || tId.isEmpty()) tId = "ctId";//개인명함
		//String[] tIds = tId.split(",");
	
		model.put("paramsForList", ParamUtil.getQueryString(request, "tId", "hghtPx"));

		return LayoutUtil.getJspPath("/ct/plt/listPrTabPlt");
	}
	
	/** 추천 커뮤니티 프레임 */
	@RequestMapping(value= "/ct/pr/listPrTabFrm")
	public String listPrTabFrm(HttpServletRequest request,
			@RequestParam(value = "tId", required = false) String tId,
			@RequestParam(value = "hghtPx", required = false) String hghtPx,
			@RequestParam(value = "pageCnt", required = false) String pageCnt,
			@RequestParam(value = "titleYn", required = false) String titleYn,
			@RequestParam(value = "pagingYn", required = false) String pagingYn,
			@RequestParam(value = "colYn", required = false) String colYn,
			ModelMap model) throws Exception{
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		/**
		 *  포틀릿의 height를 기준으로 rowcount를 계산한다.
		 */
		int ptlHght = hghtPx==null || hghtPx.isEmpty() ? 0 : Integer.parseInt(hghtPx);
		int tabHght = ptlHght - 35 - (!"N".equals(colYn) ? 22 : 0 );
		int rowCnt = Math.max(1, (int)Math.floor(tabHght / 22));
		request.setAttribute("pageRowCnt", rowCnt);//RowCnt 삽입
		
		//조회조건 매핑
		CtPrBVo ctPrBVo = new CtPrBVo();
		ctPrBVo.setCompId(userVo.getCompId());
		VoUtil.bind(request, ctPrBVo);
		
		//커뮤니티 홍보마당 목록 - COUNT
		Integer recodeCount = commonDao.count(ctPrBVo);
		PersonalUtil.setPaging(request, ctPrBVo, recodeCount);
		
		Map<String,Object> rsltMap = new HashMap<String,Object>();
		
		//커뮤니티 홍보마당 목록
		List<CtPrBVo> ctPrList = ctPrSvc.getCtPrVoList(ctPrBVo);
		
		Map<String, Object> ctPrBInfoMap;
		List<Map<String, Object>> ctPrMapList = new ArrayList<Map<String, Object>>();
		for(CtPrBVo storedCtBcBVo : ctPrList){
			ctPrBInfoMap = VoUtil.toMap(storedCtBcBVo, null);
			ctPrMapList.add(ctPrBInfoMap);
		}
		
		rsltMap.put("ctPrMapList", ctPrMapList);
		rsltMap.put("recodeCount", recodeCount);
		
		
		model.put("rsltMapList", rsltMap.get("ctPrMapList"));
		model.put("recodeCount", rsltMap.get("recodeCount"));
		
	
		return LayoutUtil.getJspPath("/ct/plt/listPrTabFrm");
		
	}
	
	/**  관리자 공지사항  포틀릿 */
	@RequestMapping(value = "/ct/plt/listNotcTabPlt")
	public String listNotcTabPlt(HttpServletRequest request,
			@RequestParam(value = "tId", required = false) String tId,
			ModelMap model) throws Exception {
		if(tId == null || tId.isEmpty()) tId = "ctId";//개인명함
		//String[] tIds = tId.split(",");
	
		model.put("paramsForList", ParamUtil.getQueryString(request, "tId", "hghtPx"));

		return LayoutUtil.getJspPath("/ct/plt/listNotcTabPlt");
	}
	
	/** 관리자 공지사항  프레임 */
	@RequestMapping(value= "/ct/notc/listNotcTabFrm")
	public String listNotcTabFrm(HttpServletRequest request,
			@RequestParam(value = "tId", required = false) String tId,
			@RequestParam(value = "hghtPx", required = false) String hghtPx,
			@RequestParam(value = "pageCnt", required = false) String pageCnt,
			@RequestParam(value = "titleYn", required = false) String titleYn,
			@RequestParam(value = "pagingYn", required = false) String pagingYn,
			@RequestParam(value = "colYn", required = false) String colYn,
			ModelMap model) throws Exception{
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		/**
		 *  포틀릿의 height를 기준으로 rowcount를 계산한다.
		 */
		int ptlHght = hghtPx==null || hghtPx.isEmpty() ? 0 : Integer.parseInt(hghtPx);
		int tabHght = ptlHght - 35 - (!"N".equals(colYn) ? 22 : 0 );
		int rowCnt = Math.max(1, (int)Math.floor(tabHght / 22));
		request.setAttribute("pageRowCnt", rowCnt);//RowCnt 삽입
		
		//조회조건 매핑
		Map<String,Object> rsltMap = new HashMap<String,Object>();
		
		CtAdmNotcBVo ctAdmNotcBVo = new CtAdmNotcBVo();
		ctAdmNotcBVo.setCompId(userVo.getCompId());
		//만료된 공지사항 제외
		ctAdmNotcBVo.setSchExpr("N");
		VoUtil.bind(request, ctAdmNotcBVo);
		
		// 게시물(BB_X000X_L) 테이블 - COUNT
		Integer recodeCount = commonSvc.count(ctAdmNotcBVo);
		PersonalUtil.setPaging(request, ctAdmNotcBVo, recodeCount);
		
		// 게시물 목록
		List<CtAdmNotcBVo> ctAdmNotcMapList = ctAdmNotcSvc.getCtAdmNotcVoList(ctAdmNotcBVo);
		
		Map<String, Object> ctAdmNotcBInfoMap;
		List<Map<String, Object>> ctAdmNotcAddMapList = new ArrayList<Map<String, Object>>();
		for(CtAdmNotcBVo storedCtBcBVo : ctAdmNotcMapList){
			ctAdmNotcBInfoMap = VoUtil.toMap(storedCtBcBVo, null);
			ctAdmNotcAddMapList.add(ctAdmNotcBInfoMap);
		}
		
		rsltMap.put("ctAdmNotcAddMapList", ctAdmNotcAddMapList);
		rsltMap.put("recodeCount", recodeCount);
		
		
		model.put("rsltMapList", rsltMap.get("ctAdmNotcAddMapList"));
		model.put("recodeCount", rsltMap.get("recodeCount"));
		
		return LayoutUtil.getJspPath("/ct/plt/listNotcTabFrm");
		
	}
	
	
	/** 인기커뮤니티 포틀릿*/
	@RequestMapping(value = "/ct/plt/listExntCmTabPlt")
	public String listExntCmTabPlt(HttpServletRequest request,
			@RequestParam(value = "tId", required = false) String tId,
			@RequestParam(value = "tabId", required = false) String tabId,
			ModelMap model) throws Exception {
		if(tId == null || tId.isEmpty()) tId = "ctId";//개인명함
		
		String ranking = request.getParameter("ranking");
		
		
		
		model.put("paramsForList", ParamUtil.getQueryString(request, "tId", "hghtPx"));
		model.put("ranking", ranking);
		return LayoutUtil.getJspPath("/ct/plt/listExntCmTabPlt");
	}
	
	/** 인기커뮤니티  프레임 */
	@RequestMapping(value= "/ct/plt/listExntCmTabFrm")
	public String listExntCmTabFrm(HttpServletRequest request,
			@RequestParam(value = "tId", required = false) String tId,
			@RequestParam(value = "hghtPx", required = false) String hghtPx,
			@RequestParam(value = "pageCnt", required = false) String pageCnt,
			@RequestParam(value = "titleYn", required = false) String titleYn,
			@RequestParam(value = "pagingYn", required = false) String pagingYn,
			@RequestParam(value = "colYn", required = false) String colYn,
			@RequestParam(value = "ranking", required = false) String ranking,
			ModelMap model) throws Exception{
		
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		Integer rank;
		
		CtPltSetupDVo ctPltSetupDVo = new CtPltSetupDVo();
		ctPltSetupDVo.setCtFncId("99999");
		ctPltSetupDVo.setCtId("99999");
		ctPltSetupDVo.setCompId(userVo.getCompId());
		ctPltSetupDVo.setUserUid(userVo.getUserUid());
	
		ctPltSetupDVo = (CtPltSetupDVo) commonDao.queryVo(ctPltSetupDVo);
		
		if(ctPltSetupDVo != null){
			rank = Integer.parseInt(ctPltSetupDVo.getPerd());
		}else{
			rank = 10;
		}
		
		/**
		 *  포틀릿의 height를 기준으로 rowcount를 계산한다.
		 */
		int ptlHght = hghtPx==null || hghtPx.isEmpty() ? 0 : Integer.parseInt(hghtPx);
		int tabHght = ptlHght - 35 - (!"N".equals(colYn) ? 22 : 0 );
		int rowCnt = Math.max(1, (int)Math.floor(tabHght / 22));
		request.setAttribute("pageRowCnt", rowCnt);//RowCnt 삽입
		
		//조회조건 매핑
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		VoUtil.bind(request, ctEstbBVo);
		ctEstbBVo.setCompId(userVo.getCompId());
		ctEstbBVo.setQueryLang(langTypCd);
		ctEstbBVo.setLangTyp(langTypCd);
		ctEstbBVo.setOrderBy("MBSH_CNT DESC");
		ctEstbBVo.setRanking(rank);
		ctEstbBVo.setCtActStat("A");
		ctEstbBVo.setCtStat("A");
//		request.setAttribute("pageRowCnt", 5);//RowCnt 삽입
//		request.setAttribute("pageNo", 1);//RowCnt 삽입
//		ctEstbBVo.setPageRowCnt(5);
//		ctEstbBVo.setPageNo(1);
		
		
		Map<String, Object>	 rsltMap = ctCmntSvc.getCmMngTgtList(request, ctEstbBVo);
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("rsltMapList", rsltMap.get("ctMngTgtMapList"));
//		model.put("rsltMapList", rsltMap);

		
		return LayoutUtil.getJspPath("/ct/plt/listExntCmTabFrm");
		
	}
	
	/** 인기커뮤니티 포틀릿*/
	@RequestMapping(value = "/ct/plt/setExntCmTabPlt")
	public String setExntCmTabPlt(HttpServletRequest request,
			ModelMap model) throws Exception {
	
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		CtPltSetupDVo ctPltSetupDVo = new CtPltSetupDVo();
		ctPltSetupDVo.setCtFncId("99999");
		ctPltSetupDVo.setCtId("99999");
		ctPltSetupDVo.setCompId(userVo.getCompId());
		ctPltSetupDVo.setUserUid(userVo.getUserUid());
	
		ctPltSetupDVo = (CtPltSetupDVo) commonDao.queryVo(ctPltSetupDVo);
	
		if(ctPltSetupDVo == null){
			model.put("ranking", "10");
		}else{
			model.put("ranking", ctPltSetupDVo.getPerd());
		}
		
		
		return LayoutUtil.getJspPath("/ct/plt/setExntCmTabPlt");
	}
	
	/** 커뮤니티 랭크(순위) 저장  */
	@RequestMapping(value = "/ct/plt/transRankSave")
	public String transRankSave(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		
		try{
			
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String ranking  = (String) object.get("ranking");
			if (ranking == null || ranking.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			QueryQueue queryQueue = new QueryQueue();
			
			CtPltSetupDVo ctPltSetupDVo = new CtPltSetupDVo();
			ctPltSetupDVo.setCtFncId("99999");
			ctPltSetupDVo.setCompId(userVo.getCompId());
			ctPltSetupDVo.setUserUid(userVo.getUserUid());
			ctPltSetupDVo.setCtId("99999"); // 우수커뮤니티의 경우 커뮤니티id별로 처리하지 않음.
			int setCount =  commonDao.count(ctPltSetupDVo);
			
			
			if(setCount == 0){
				ctPltSetupDVo.setPerd(ranking);
				queryQueue.insert(ctPltSetupDVo);
			}else{
				ctPltSetupDVo.setPerd(ranking);
				queryQueue.update(ctPltSetupDVo);
			}
						
			commonDao.execute(queryQueue);
					
			//ct.msg.mbshApvd = 커뮤니티 가입이 승인 처리 되었습니다.
//			model.put("message", messageProperties.getMessage("ct.msg.mbshApvd", request));
			model.put("result", "ok");
				
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		return JsonUtil.returnJson(model);
	}
	
	
	/** 커뮤니티별 기능 설정 포틀릿*/
	@RequestMapping(value = "/ct/plt/setCtFuncPlt")
	public String setCtFuncPlt(HttpServletRequest request,
			ModelMap model) throws Exception {
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		
		//조회조건 매핑
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		VoUtil.bind(request, ctEstbBVo);
		ctEstbBVo.setCompId(userVo.getCompId());
		ctEstbBVo.setLogUserUid(userVo.getUserUid());
		ctEstbBVo.setQueryLang(langTypCd);
		ctEstbBVo.setLangTyp(langTypCd);
		ctEstbBVo.setSignal("my");
		
		List<String> ctActList = new ArrayList<String>();
		ctActList.add("A");  // 정상활동중
		//ctActList.add("S");
		//ctActList.add("C"); 
		
		ctEstbBVo.setCtActStatList(ctActList);
		
		//목록 조회
		List<CtEstbBVo> myCtVoList = ctCmntSvc.getCtCmntList(ctEstbBVo);
		
		CtPltSetupDVo pltSetupDvo=new CtPltSetupDVo();
		VoUtil.bind(request, pltSetupDvo);
		pltSetupDvo.setUserUid(userVo.getUserUid());
		if(pltSetupDvo.getCtFncId()!=null&&!pltSetupDvo.getCtFncId().equals("")){
			@SuppressWarnings("unchecked")
			List<CtPltSetupDVo> ctPltsetupList=(List<CtPltSetupDVo>) commonDao.queryList(pltSetupDvo);
			model.put("ctPltsetupList", ctPltsetupList);
			if(ctPltsetupList.size()>0)
				model.put("perd", ctPltsetupList.get(0).getPerd());
		}
		
		model.put("ctFncId", request.getParameter("ctFncId"));
		model.put("myCtVoList", myCtVoList);
	
		return LayoutUtil.getJspPath("/ct/plt/setCtFuncPlt");
	}
	
	
	/** 커뮤니티별 기능 설정 포틀릿 저장*/
	@RequestMapping(value = "/ct/plt/transCtFuncPlt")
	public String transCtFuncPlt(HttpServletRequest request,
			ModelMap model) throws Exception {
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		try{
			QueryQueue queue=new QueryQueue();
			CtPltSetupDVo delPltSetupDvo=new CtPltSetupDVo();
			//VoUtil.bind(request, delPltSetupDvo);
			delPltSetupDvo.setUserUid(userVo.getUserUid());
			delPltSetupDvo.setCtFncId(request.getParameter("ctFncId"));
			if(delPltSetupDvo.getCtFncId()!=null&&!delPltSetupDvo.getCtFncId().equals("")){
				commonDao.delete(delPltSetupDvo);
			}
			
			String ctIds[]=request.getParameterValues("ctId");
		
			if(ctIds!=null){
				for(String ctId:ctIds){
					CtPltSetupDVo pltSetupDvo=new CtPltSetupDVo();
					VoUtil.bind(request, pltSetupDvo);
					pltSetupDvo.setUserUid(userVo.getUserUid());
					pltSetupDvo.setCompId(userVo.getCompId());
					pltSetupDvo.setCtId(ctId);
					
					if(pltSetupDvo.getCtFncId()!=null&&!delPltSetupDvo.getCtFncId().equals("")){
						queue.insert(pltSetupDvo);
					}
				}
			}
			commonDao.execute(queue);
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.dialog.close('setupPltDialog');");
		}catch(Exception e)
		{
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}
		
	
		return LayoutUtil.getResultJsp();
		
	}
	
	/** 게시판커뮤니티 포틀릿*/
	@RequestMapping(value = "/ct/plt/listBoardCmTabPlt")
	public String listBoardCmTabPlt(HttpServletRequest request,
			@RequestParam(value = "tId", required = false) String tId,
			@RequestParam(value = "tabId", required = false) String tabId,
			ModelMap model) throws Exception {
		if(tId == null || tId.isEmpty()) tId = "ctId";//개인명함
		
		
		model.put("paramsForList", ParamUtil.getQueryString(request, "tId", "hghtPx"));
		return LayoutUtil.getJspPath("/ct/plt/listBoardCmTabPlt");
	}
	
	/** 게시판커뮤니티 프레임 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value= "/ct/plt/listBoardCmTabFrm")
	public String listBoardCmTabFrm(HttpServletRequest request,
			@RequestParam(value = "tId", required = false) String tId,
			@RequestParam(value = "hghtPx", required = false) String hghtPx,
			@RequestParam(value = "pageCnt", required = false) String pageCnt,
			@RequestParam(value = "titleYn", required = false) String titleYn,
			@RequestParam(value = "pagingYn", required = false) String pagingYn,
			@RequestParam(value = "colYn", required = false) String colYn,
			@RequestParam(value = "ranking", required = false) String ranking,
			ModelMap model) throws Exception{
		
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		/**
		 *  포틀릿의 height를 기준으로 rowcount를 계산한다.
		 */
		int ptlHght = hghtPx==null || hghtPx.isEmpty() ? 0 : Integer.parseInt(hghtPx);
		int tabHght = ptlHght - 35 - (!"N".equals(colYn) ? 22 : 0 );
		int rowCnt = Math.max(1, (int)Math.floor(tabHght / 22));
		request.setAttribute("pageRowCnt", rowCnt);//RowCnt 삽입
		
		CtPltSetupDVo ctPltSetupDVo = new CtPltSetupDVo();
		ctPltSetupDVo.setCtFncId("F000000L");
		ctPltSetupDVo.setUserUid(userVo.getUserUid());
		ctPltSetupDVo.setCompId(userVo.getCompId());
		
		List<CtPltSetupDVo> ctPltSetupList = (List<CtPltSetupDVo>) commonDao.queryList(ctPltSetupDVo); 
		List<String> ctIdArray = new ArrayList<String>();
		for(CtPltSetupDVo ctPltSetup: ctPltSetupList){
			ctIdArray.add(ctPltSetup.getCtId());
		}
		
		CtBullMastBVo ctBullMastBVo = new CtBullMastBVo();
		ctBullMastBVo.setCtIdList(ctIdArray);
		ctBullMastBVo.setCompId(userVo.getCompId());
		ctBullMastBVo.setLangTyp(langTypCd);
		if(ctPltSetupList.size() != 0){
			ctBullMastBVo.setStrtDt(ctCmntSvc.currentYmdSet(- Integer.parseInt(String.valueOf(ctPltSetupList.get(0).getPerd()))));
			ctBullMastBVo.setEndDt(ctCmntSvc.currentYmdDay());
		}
		
		Integer recodeCount;
		
		if(ctIdArray.size() != 0){
			recodeCount = commonDao.count(ctBullMastBVo);
		
			PersonalUtil.setPaging(request, ctBullMastBVo, recodeCount);
			
			List<CtBullMastBVo> ctBullMastList = new ArrayList<CtBullMastBVo>();
			ctBullMastList = (List<CtBullMastBVo>) commonDao.queryList(ctBullMastBVo);
			
			
			Map<String, Object> ctBullMap = new HashMap<String, Object>();
			List<Map<String, Object>> ctBullMapList = new ArrayList<Map<String, Object>>();
			for(CtBullMastBVo storedCtBcBVo : ctBullMastList){
				ctBullMap = VoUtil.toMap(storedCtBcBVo, null);
				ctBullMapList.add(ctBullMap);
			}
			
			ctBullMap.put("recodeCount", recodeCount);
			ctBullMap.put("ctBullMapList", ctBullMapList);
			
			model.put("recodeCount", ctBullMap.get("recodeCount"));
			model.put("rsltMapList", ctBullMap.get("ctBullMapList"));
		}
		
		return LayoutUtil.getJspPath("/ct/plt/listBoardCmTabFrm");
	}
	
	
	/** 설문 커뮤니티 포틀릿*/
	@RequestMapping(value = "/ct/plt/listSurvCmTabPlt")
	public String listSurvCmTabPlt(HttpServletRequest request,
			@RequestParam(value = "tId", required = false) String tId,
			@RequestParam(value = "tabId", required = false) String tabId,
			ModelMap model) throws Exception {
		if(tId == null || tId.isEmpty()) tId = "ctId";//개인명함
		
		
		model.put("paramsForList", ParamUtil.getQueryString(request, "tId", "hghtPx"));
		return LayoutUtil.getJspPath("/ct/plt/listSurvCmTabPlt");
	}
	
	/** 설문 커뮤니티 프레임 */
	@RequestMapping(value= "/ct/plt/listSurvCmTabFrm")
	public String listSurvCmTabFrm(HttpServletRequest request,
			@RequestParam(value = "tId", required = false) String tId,
			@RequestParam(value = "hghtPx", required = false) String hghtPx,
			@RequestParam(value = "pageCnt", required = false) String pageCnt,
			@RequestParam(value = "titleYn", required = false) String titleYn,
			@RequestParam(value = "pagingYn", required = false) String pagingYn,
			@RequestParam(value = "colYn", required = false) String colYn,
			@RequestParam(value = "ranking", required = false) String ranking,
			ModelMap model) throws Exception{
		
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		/**
		 *  포틀릿의 height를 기준으로 rowcount를 계산한다.
		 */
		int ptlHght = hghtPx==null || hghtPx.isEmpty() ? 0 : Integer.parseInt(hghtPx);
		int tabHght = ptlHght - 35 - (!"N".equals(colYn) ? 22 : 0 );
		int rowCnt = Math.max(1, (int)Math.floor(tabHght / 22));
		request.setAttribute("pageRowCnt", rowCnt);//RowCnt 삽입
		
		CtPltSetupDVo ctPltSetupDVo = new CtPltSetupDVo();
		ctPltSetupDVo.setCtFncId("F000000Q");
		ctPltSetupDVo.setUserUid(userVo.getUserUid());
		ctPltSetupDVo.setCompId(userVo.getCompId());
		@SuppressWarnings("unchecked")
		List<CtPltSetupDVo> ctPltSetupList = (List<CtPltSetupDVo>) commonDao.queryList(ctPltSetupDVo); 
		List<String> ctIdArray = new ArrayList<String>();
		for(CtPltSetupDVo ctPltSetup: ctPltSetupList){
			ctIdArray.add(ctPltSetup.getCtId());
		}
		
		// 조회조건 매핑
		CtSurvBVo ctSurvBVo = new CtSurvBVo();
		VoUtil.bind(request, ctSurvBVo);
		ctSurvBVo.setLogUserUid(userVo.getUserUid());
		ctSurvBVo.setLangTyp(langTypCd);
		ctSurvBVo.setSurvPrgStatCd("6");
		
		List<String> survList=new ArrayList<String>();
		//준비중, 진행중, 마감, 임시저장
		survList.add("1");
		survList.add("3");
		survList.add("4");
		ctSurvBVo.setSurvSearchList(survList);
		
		ctSurvBVo.setCtIdList(ctIdArray);
		
		ctSurvBVo.setCompId(userVo.getCompId());
		ctSurvBVo.setQueryLang(langTypCd);
		if(ctPltSetupList.size() != 0){
			ctSurvBVo.setStrtDt(ctCmntSvc.currentYmdSet(- Integer.parseInt(String.valueOf(ctPltSetupList.get(0).getPerd()))));
			ctSurvBVo.setEndDt(ctCmntSvc.currentYmdDay());
		}
		
		Integer recodeCount;
		
		if(ctIdArray.size() != 0){
			recodeCount = commonDao.count(ctSurvBVo);
			
			PersonalUtil.setPaging(request, ctSurvBVo, recodeCount);
			
			if(userVo.isAdmin() || userVo.isSysAdmin()){
				model.put("admin", "admin");
			}
			
			Map<String,Object> rsltMap = ctSurvSvc.getAdmSurvMapList(request, ctSurvBVo);
			model.put("recodeCount", rsltMap.get("recodeCount"));
			model.put("rsltMapList", rsltMap.get("ctSurvBMapList"));
			model.put("logUserUid", ctSurvBVo.getLogUserUid());
		}
		
		return LayoutUtil.getJspPath("/ct/plt/listSurvCmTabFrm");
	}
		
	
	
	/**
	 * ============================================================================
	 * ============================================================================
	 *  
	 *  																커뮤니티 포틀릿 통합
	 *  
	 *  ============================================================================
	 *  ============================================================================
	 */
	
	/** 커뮤니티 목록 포틀릿에서 보여줄 함 목록 - 나의커뮤니티, 관리대상 커뮤니티, 우수커뮤니티, 추천커뮤니티, 신규커뮤니티 */
	private static final String[] CM_LIST_PLT = {"my", "new", "mng", "exnt", "recmd"};

	/** 커뮤니티 목록 */
	@RequestMapping(value = "/ct/plt/listCommunityListTabPlt")
	public String communityListPlt(HttpServletRequest request,
			@Parameter(name="pltId", required=false) String pltId,
			ModelMap model) throws Exception {

		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		List<CtPltCmListDVo> storedCtPltCmListDVoList = queryCtPltCmListDVoList(userVo, pltId, langTypCd);
		List<CtPltCmListDVo> targetCtPltCmListDVoList = new ArrayList<CtPltCmListDVo>();  
		List<CtPltCmListDVo> ctPltCmListDVoList = new ArrayList<CtPltCmListDVo>(); 
		

		// 대상 탭메뉴 초기화
		for(String bxId : CM_LIST_PLT){
			CtPltCmListDVo ctPltCmListDVo = new CtPltCmListDVo();
			ctPltCmListDVo.setBxId(bxId);
			targetCtPltCmListDVoList.add(ctPltCmListDVo);
		}

		// 최초 설정상태의 경우 기능메뉴(나의커뮤니티)를 디폴트로 설정하고 추가한다. 
		if(storedCtPltCmListDVoList==null || storedCtPltCmListDVoList.isEmpty()){
			for(CtPltCmListDVo ctPltCmListDVo  : targetCtPltCmListDVoList)
			{
				if(ctPltCmListDVo.getBxId().equals("my") || ctPltCmListDVo.getBxId().equals("new") || ctPltCmListDVo.getBxId().equals("mng"))
					storedCtPltCmListDVoList.add(ctPltCmListDVo);
			}
		}

		for(CtPltCmListDVo storedCtPltCmListDVo  : storedCtPltCmListDVoList)
		{
			for(CtPltCmListDVo targetCtPltCmListDVo  : targetCtPltCmListDVoList)
			{
				if(targetCtPltCmListDVo.getBxId().equals(storedCtPltCmListDVo.getBxId()))
				{
					ctPltCmListDVoList.add(targetCtPltCmListDVo);
					break;
				}
			}
		}
		
		// 포틀렛설정 가능한 기능메뉴 
		List<PtCdBVo> brdFncList = ptCmSvc.getCdList("CT_FNC_MENU", langTypCd, "Y");

		// 권한 있는 결재함 체크
		String url, menuId;
		PtMnuDVo ptMnuDVo;
		Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap = ptLoutSvc.getLoutCombByCombIdMap(userVo.getCompId(), langTypCd);
		
		List<CtPltCmListDVo> ctPltCmListDVoMnuList = new ArrayList<CtPltCmListDVo>(); 
		for(CtPltCmListDVo ctPltCmListDVo  : ctPltCmListDVoList)
		{
			if(ctPltCmListDVo.getBxId().equals("my") || ctPltCmListDVo.getBxId().equals("mng"))
			{
				url = ctCmSvc.getBxUrlByBxId(ctPltCmListDVo.getBxId());
				menuId = ptSecuSvc.getSecuMenuId(userVo, url);
				ptMnuDVo = getMenuByMenuId(menuId, loutCombByCombIdMap);
				if(ptMnuDVo != null){
					ctPltCmListDVo.setBxNm(ptMnuDVo.getRescNm());
					ctPltCmListDVo.setMenuId(menuId);
					ctPltCmListDVoMnuList.add(ctPltCmListDVo);
				}
			}
			else
			{
				// 일반메뉴가 아닌 기능메뉴의 경우 기능메뉴목록에서 리소스명을 가져온다.
				for(PtCdBVo cdVo : brdFncList) 
				{
					if(cdVo.getRefVa1().equals(ctPltCmListDVo.getBxId()))
					{
						ctPltCmListDVo.setMenuId(pltId);
						ctPltCmListDVo.setBxNm(cdVo.getRescNm());
						break;
					}
				}
				ctPltCmListDVoMnuList.add(ctPltCmListDVo);
			}
		}
		
		model.put("tId", ctPltCmListDVoMnuList.size()==0?"":ctPltCmListDVoMnuList.get(0).getBxId());
		model.put("menuId", ctPltCmListDVoMnuList.size()==0?"":ctPltCmListDVoMnuList.get(0).getMenuId());
		model.put("ctPltCmListDVoList", ctPltCmListDVoMnuList);	
		model.put("paramsForList", ParamUtil.getQueryString(request, "tId", "hghtPx", "menuId"));
		return LayoutUtil.getJspPath("/ct/plt/listCommunityListTabPlt");
	}
	
	/** 사용자별 포틀릿 설정 조회 */
	private List<CtPltCmListDVo> queryCtPltCmListDVoList(UserVo userVo, String pltId, String langTypCd) throws SQLException{
		CtPltCmListDVo ctPltCmListDVo = new CtPltCmListDVo();
		ctPltCmListDVo.setUserUid(userVo.getUserUid());
		ctPltCmListDVo.setPltId(pltId);
		ctPltCmListDVo.setQueryLang(langTypCd);
		ctPltCmListDVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<CtPltCmListDVo> CtPltCmListDVoList = (List<CtPltCmListDVo>)commonSvc.queryList(ctPltCmListDVo);
		return CtPltCmListDVoList;
	}
	
	/** 포틀릿 설정 */
	@RequestMapping(value = "/ct/plt/setCommunityListBxPltSetupPop")
	public String setCommunityListBxPltSetupPop(HttpServletRequest request,
			@Parameter(name="pltId", required=false) String pltId,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		List<CtPltCmListDVo> storedCtPltCmListDVoList = queryCtPltCmListDVoList(userVo, pltId, langTypCd);
		List<CtPltCmListDVo> targetCtPltCmListDVoList = new ArrayList<CtPltCmListDVo>();  
		List<CtPltCmListDVo> ctPltCmListDVoList = new ArrayList<CtPltCmListDVo>(); 
		List<String> storedBxIdList = new ArrayList<String>();
		
		// 대상 탭메뉴 초기화
		for(String bxId : CM_LIST_PLT){
			CtPltCmListDVo ctPltCmListDVo = new CtPltCmListDVo();
			ctPltCmListDVo.setBxId(bxId);
			targetCtPltCmListDVoList.add(ctPltCmListDVo);
		}
		
		// 최초 설정상태의 경우 기능메뉴(개인명함)를 디폴트로 설정하고 추가한다. 
		if(storedCtPltCmListDVoList==null || storedCtPltCmListDVoList.isEmpty()){
			for(CtPltCmListDVo ctPltCmListDVo  : targetCtPltCmListDVoList)
			{
				if(ctPltCmListDVo.getBxId().equals("my") || ctPltCmListDVo.getBxId().equals("new") || ctPltCmListDVo.getBxId().equals("mng"))
					ctPltCmListDVo.setUseYn("Y");
				ctPltCmListDVoList.add(ctPltCmListDVo);
			}
		}
		else
		{
			// 저장목록이 있을경우 우선순위 정렬로 추가한다.
			for(CtPltCmListDVo storedCtPltCmListDVo  : storedCtPltCmListDVoList)
			{
				storedBxIdList.add(storedCtPltCmListDVo.getBxId());
				storedCtPltCmListDVo.setUseYn("Y");
				
				//대상목록에서 리소스명을 가져온다.
				for(CtPltCmListDVo targetCtPltCmListDVo  : targetCtPltCmListDVoList)
				{
					if(storedCtPltCmListDVo.getBxId().equals(targetCtPltCmListDVo.getBxId()))
					{
						storedCtPltCmListDVo.setBxNm(targetCtPltCmListDVo.getBxNm());
						break;
					}
				}

				ctPltCmListDVoList.add(storedCtPltCmListDVo);	
			}
			
			// 대상목록에서 저장목록을 제외하고 추가한다.
			for(CtPltCmListDVo targetCtPltCmListDVo  : targetCtPltCmListDVoList)
			{
				if(storedBxIdList.contains(targetCtPltCmListDVo.getBxId())) continue;
				ctPltCmListDVoList.add(targetCtPltCmListDVo);	
			}
		}
		
		// 포틀렛설정 가능한 기능메뉴 
		List<PtCdBVo> brdFncList = ptCmSvc.getCdList("CT_FNC_MENU", langTypCd, "Y");

		// 권한 있는 결재함 체크
		String url, menuId;
		PtMnuDVo ptMnuDVo;
		Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap = ptLoutSvc.getLoutCombByCombIdMap(userVo.getCompId(), langTypCd);
		
		List<CtPltCmListDVo> ctPltCmListDVoMnuList = new ArrayList<CtPltCmListDVo>(); 
		for(CtPltCmListDVo ctPltCmListDVo  : ctPltCmListDVoList)
		{
			if(ctPltCmListDVo.getBxId().equals("my") || ctPltCmListDVo.getBxId().equals("mng"))
			{
				url = ctCmSvc.getBxUrlByBxId(ctPltCmListDVo.getBxId());
				menuId = ptSecuSvc.getSecuMenuId(userVo, url);
				ptMnuDVo = getMenuByMenuId(menuId, loutCombByCombIdMap);
				if(ptMnuDVo != null){
					ctPltCmListDVo.setBxNm(ptMnuDVo.getRescNm());
					ctPltCmListDVo.setMenuId(menuId);
					ctPltCmListDVoMnuList.add(ctPltCmListDVo);
				}
			}
			else
			{
				// 일반메뉴가 아닌 기능메뉴의 경우 기능메뉴목록에서 리소스명을 가져온다.
				for(PtCdBVo cdVo : brdFncList) 
				{
					if(cdVo.getRefVa1().equals(ctPltCmListDVo.getBxId()))
					{
						ctPltCmListDVo.setMenuId(pltId);
						ctPltCmListDVo.setBxNm(cdVo.getRescNm());
						break;
					}
				}
				ctPltCmListDVoMnuList.add(ctPltCmListDVo);
			}
		}
		
		model.put("ctPltCmListDVoList", ctPltCmListDVoMnuList);
		
		return LayoutUtil.getJspPath("/ct/plt/setCommunityListBxPltSetupPop");
	}
	
	/** [AJAX]  포틀릿 설정 저장 */
	@RequestMapping(value = "/ct/plt/transCommunityListBxPltSetupAjx")
	public String transCommunityListBxPltSetupAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String pltId = (String)jsonObject.get("pltId");
		String bxIds = (String)jsonObject.get("bxIds");
		
		QueryQueue queryQueue = new QueryQueue();
		CtPltCmListDVo ctPltCmListDVo;
		if(bxIds!=null && !bxIds.isEmpty()){
			
			ctPltCmListDVo = new CtPltCmListDVo();
			ctPltCmListDVo.setUserUid(userVo.getUserUid());
			queryQueue.delete(ctPltCmListDVo);
			Integer sortOrdr = 1;
			
			for(String bxId : bxIds.split(",")){
				ctPltCmListDVo = new CtPltCmListDVo();
				ctPltCmListDVo.setUserUid(userVo.getUserUid());
				ctPltCmListDVo.setBxId(bxId);
				ctPltCmListDVo.setPltId(pltId);
				ctPltCmListDVo.setSortOrdr(sortOrdr.toString());
				sortOrdr++;
				queryQueue.insert(ctPltCmListDVo);
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
	
	
	
	/** 기능자료 목록 포틀릿에서 보여줄 함 목록 - 커뮤니티 투표, 커뮤니티 CoolSite, 커뮤니티 토론실, 커뮤니티 게시판, 커뮤니티 일정 */
	private static final String[] FNC_LIST_PLT = {"board", "schdl", "surv", "site", "debr"};

	/** 커뮤니티 목록 */
	@RequestMapping(value = "/ct/plt/listFncDataListTabPlt")
	public String listFncDataListTabPlt(HttpServletRequest request,
			@Parameter(name="pltId", required=false) String pltId,
			ModelMap model) throws Exception {

		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		List<CtPltFncListDVo> storedCtPltFncListDVoList = queryCtPltFncListDVoList(userVo, pltId, langTypCd);
		List<CtPltFncListDVo> targetCtPltFncListDVoList = new ArrayList<CtPltFncListDVo>();  
		List<CtPltFncListDVo> CtPltFncListDVoList = new ArrayList<CtPltFncListDVo>(); 
		
		// 대상 탭메뉴 초기화
		for(String bxId : FNC_LIST_PLT){
			CtPltFncListDVo CtPltFncListDVo = new CtPltFncListDVo();
			CtPltFncListDVo.setBxId(bxId);
			targetCtPltFncListDVoList.add(CtPltFncListDVo);
		}
		
		// 최초 설정상태의 경우 기능메뉴(커뮤니티게시판)를 디폴트로 설정하고 추가한다. 
		if(storedCtPltFncListDVoList==null || storedCtPltFncListDVoList.isEmpty()){
			for(CtPltFncListDVo CtPltFncListDVo  : targetCtPltFncListDVoList)
			{
				if(CtPltFncListDVo.getBxId().equals("board") || CtPltFncListDVo.getBxId().equals("schdl"))
					storedCtPltFncListDVoList.add(CtPltFncListDVo);
			}
		}

		for(CtPltFncListDVo storedCtPltFncListDVo  : storedCtPltFncListDVoList)
		{
			for(CtPltFncListDVo targetCtPltFncListDVo  : targetCtPltFncListDVoList)
			{
				if(targetCtPltFncListDVo.getBxId().equals(storedCtPltFncListDVo.getBxId()))
				{
					CtPltFncListDVoList.add(targetCtPltFncListDVo);
					break;
				}
			}
		}
		
		// 포틀렛설정 가능한 기능메뉴 
		List<PtCdBVo> brdFncList = ptCmSvc.getCdList("CT_DT_MENU", langTypCd, "Y");
		
		List<CtPltFncListDVo> CtPltFncListDVoMnuList = new ArrayList<CtPltFncListDVo>(); 
		for(CtPltFncListDVo ctPltFncListDVo  : CtPltFncListDVoList)
		{
			for(PtCdBVo cdVo : brdFncList) 
			{
				if(cdVo.getRefVa1().equals(ctPltFncListDVo.getBxId()))
				{
					ctPltFncListDVo.setMenuId(pltId);
					ctPltFncListDVo.setBxNm(cdVo.getRescNm());
					break;
				}
			}
			CtPltFncListDVoMnuList.add(ctPltFncListDVo);
		}
		
		model.put("tId", CtPltFncListDVoMnuList.size()==0?"":CtPltFncListDVoMnuList.get(0).getBxId());
		model.put("menuId", CtPltFncListDVoMnuList.size()==0?"":CtPltFncListDVoMnuList.get(0).getMenuId());
		model.put("CtPltFncListDVoList", CtPltFncListDVoMnuList);	
		model.put("paramsForList", ParamUtil.getQueryString(request, "tId", "hghtPx", "menuId"));
		return LayoutUtil.getJspPath("/ct/plt/listFncDataListTabPlt");
	}
	
	/** 사용자별 포틀릿 설정 조회 */
	private List<CtPltFncListDVo> queryCtPltFncListDVoList(UserVo userVo, String pltId, String langTypCd) throws SQLException{
		CtPltFncListDVo CtPltFncListDVo = new CtPltFncListDVo();
		CtPltFncListDVo.setUserUid(userVo.getUserUid());
		CtPltFncListDVo.setPltId(pltId);
		CtPltFncListDVo.setQueryLang(langTypCd);
		CtPltFncListDVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<CtPltFncListDVo> CtPltFncListDVoList = (List<CtPltFncListDVo>)commonSvc.queryList(CtPltFncListDVo);
		return CtPltFncListDVoList;
	}
	
	/** 포틀릿 설정 */
	@RequestMapping(value = "/ct/plt/setFncListBxPltSetupPop")
	public String setFncListBxPltSetupPop(HttpServletRequest request,
			@Parameter(name="pltId", required=false) String pltId,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		List<CtPltFncListDVo> storedCtPltFncListDVoList = queryCtPltFncListDVoList(userVo, pltId, langTypCd);
		List<CtPltFncListDVo> targetCtPltFncListDVoList = new ArrayList<CtPltFncListDVo>();  
		List<CtPltFncListDVo> CtPltFncListDVoList = new ArrayList<CtPltFncListDVo>(); 
		List<String> storedBxIdList = new ArrayList<String>();
		
		// 대상 탭메뉴 초기화
		for(String bxId : FNC_LIST_PLT){
			CtPltFncListDVo CtPltFncListDVo = new CtPltFncListDVo();
			CtPltFncListDVo.setBxId(bxId);
			targetCtPltFncListDVoList.add(CtPltFncListDVo);
		}

		// 최초 설정상태의 경우 기능메뉴(개인명함)를 디폴트로 설정하고 추가한다. 
		if(storedCtPltFncListDVoList==null || storedCtPltFncListDVoList.isEmpty()){
			for(CtPltFncListDVo CtPltFncListDVo  : targetCtPltFncListDVoList)
			{
				if(CtPltFncListDVo.getBxId().equals("board") || CtPltFncListDVo.getBxId().equals("schdl"))
					CtPltFncListDVo.setUseYn("Y");
				CtPltFncListDVoList.add(CtPltFncListDVo);
			}
		}
		else
		{
			// 저장목록이 있을경우 우선순위 정렬로 추가한다.
			for(CtPltFncListDVo storedCtPltFncListDVo  : storedCtPltFncListDVoList)
			{
				storedBxIdList.add(storedCtPltFncListDVo.getBxId());
				storedCtPltFncListDVo.setUseYn("Y");
				
				//대상목록에서 리소스명을 가져온다.
				for(CtPltFncListDVo targetCtPltFncListDVo  : targetCtPltFncListDVoList)
				{
					if(storedCtPltFncListDVo.getBxId().equals(targetCtPltFncListDVo.getBxId()))
					{
						storedCtPltFncListDVo.setBxNm(targetCtPltFncListDVo.getBxNm());
						break;
					}
				}

				CtPltFncListDVoList.add(storedCtPltFncListDVo);	
			}
			
			// 대상목록에서 저장목록을 제외하고 추가한다.
			for(CtPltFncListDVo targetCtPltFncListDVo  : targetCtPltFncListDVoList)
			{
				if(storedBxIdList.contains(targetCtPltFncListDVo.getBxId())) continue;
				CtPltFncListDVoList.add(targetCtPltFncListDVo);	
			}
		}
		
		// 포틀렛설정 가능한 기능메뉴 
		List<PtCdBVo> brdFncList = ptCmSvc.getCdList("CT_DT_MENU", langTypCd, "Y");
		
		List<CtPltFncListDVo> CtPltFncListDVoMnuList = new ArrayList<CtPltFncListDVo>(); 
		for(CtPltFncListDVo ctPltFncListDVo  : CtPltFncListDVoList)
		{
			for(PtCdBVo cdVo : brdFncList) 
			{
				if(cdVo.getRefVa1().equals(ctPltFncListDVo.getBxId()))
				{
					ctPltFncListDVo.setMenuId(pltId);
					ctPltFncListDVo.setBxNm(cdVo.getRescNm());
					break;
				}
			}
			CtPltFncListDVoMnuList.add(ctPltFncListDVo);
		}
		
		model.put("CtPltFncListDVoList", CtPltFncListDVoMnuList);
		
		return LayoutUtil.getJspPath("/ct/plt/setFncListBxPltSetupPop");
	}
	
	/** [AJAX]  포틀릿 설정 저장 */
	@RequestMapping(value = "/ct/plt/transFncListBxPltSetupAjx")
	public String transFncListBxPltSetupAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String pltId = (String)jsonObject.get("pltId");
		String bxIds = (String)jsonObject.get("bxIds");
		
		QueryQueue queryQueue = new QueryQueue();
		CtPltFncListDVo CtPltFncListDVo;
		if(bxIds!=null && !bxIds.isEmpty()){
			
			CtPltFncListDVo = new CtPltFncListDVo();
			CtPltFncListDVo.setUserUid(userVo.getUserUid());
			queryQueue.delete(CtPltFncListDVo);
			Integer sortOrdr = 1;
			
			for(String bxId : bxIds.split(",")){
				CtPltFncListDVo = new CtPltFncListDVo();
				CtPltFncListDVo.setUserUid(userVo.getUserUid());
				CtPltFncListDVo.setBxId(bxId);
				CtPltFncListDVo.setPltId(pltId);
				CtPltFncListDVo.setSortOrdr(sortOrdr.toString());
				sortOrdr++;
				queryQueue.insert(CtPltFncListDVo);
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
	
	/** 기능자료 목록 포틀릿에서 보여줄 함 목록 - 관리자공지사항, 커뮤니티홍보마당 */
	private static final String[] NOPR_LIST_PLT = {"notc", "pr"};

	/** 커뮤니티 목록 */
	@RequestMapping(value = "/ct/plt/listNotcPrListTabPlt")
	public String listNoPrDataListTabPlt(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="pltId", required=false) String pltId,
			ModelMap model) throws Exception {

		// 목록 캐쉬 방지
		response.setHeader("cache-control","no-store"); // http 1.1   
		response.setHeader("Pragma","no-cache"); // http 1.0   
		response.setDateHeader("Expires",0); // proxy server 에 cache방지. 


		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		List<CtPltNoPrListDVo> storedCtPltNoPrListDVoList = queryCtPltNoPrListDVoList(userVo, pltId, langTypCd);
		List<CtPltNoPrListDVo> targetCtPltNoPrListDVoList = new ArrayList<CtPltNoPrListDVo>();  
		List<CtPltNoPrListDVo> CtPltNoPrListDVoList = new ArrayList<CtPltNoPrListDVo>(); 
		
		// 대상 탭메뉴 초기화
		for(String bxId : NOPR_LIST_PLT){
			CtPltNoPrListDVo CtPltNoPrListDVo = new CtPltNoPrListDVo();
			CtPltNoPrListDVo.setBxId(bxId);
			targetCtPltNoPrListDVoList.add(CtPltNoPrListDVo);
		}
		
		// 최초 설정상태의 경우 기능메뉴(관리자공지사항, 커뮤니티홍보마당)를 디폴트로 설정하고 추가한다. 
		if(storedCtPltNoPrListDVoList==null || storedCtPltNoPrListDVoList.isEmpty()){
			for(CtPltNoPrListDVo CtPltNoPrListDVo  : targetCtPltNoPrListDVoList)
			{
				//if(CtPltNoPrListDVo.getBxId().equals("notc"))
				storedCtPltNoPrListDVoList.add(CtPltNoPrListDVo);
			}
		}

		for(CtPltNoPrListDVo storedCtPltNoPrListDVo  : storedCtPltNoPrListDVoList)
		{
			for(CtPltNoPrListDVo targetCtPltNoPrListDVo  : targetCtPltNoPrListDVoList)
			{
				if(targetCtPltNoPrListDVo.getBxId().equals(storedCtPltNoPrListDVo.getBxId()))
				{
					CtPltNoPrListDVoList.add(targetCtPltNoPrListDVo);
					break;
				}
			}
		}
		
		// 권한 있는 결재함 체크
		String url, menuId;
		PtMnuDVo ptMnuDVo;
		Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap = ptLoutSvc.getLoutCombByCombIdMap(userVo.getCompId(), langTypCd);
		
		List<CtPltNoPrListDVo> CtPltNoPrListDVoMnuList = new ArrayList<CtPltNoPrListDVo>(); 
		for(CtPltNoPrListDVo ctPltNoPrListDVo  : CtPltNoPrListDVoList)
		{
			url = ctCmSvc.getBxUrlByBxId(ctPltNoPrListDVo.getBxId());
			menuId = ptSecuSvc.getSecuMenuId(userVo, url);
			ptMnuDVo = getMenuByMenuId(menuId, loutCombByCombIdMap);
			if(ptMnuDVo != null){
				ctPltNoPrListDVo.setBxNm(ptMnuDVo.getRescNm());
				ctPltNoPrListDVo.setMenuId(menuId);
				CtPltNoPrListDVoMnuList.add(ctPltNoPrListDVo);
			}
		}
		
		model.put("tId", CtPltNoPrListDVoMnuList.size()==0?"":CtPltNoPrListDVoMnuList.get(0).getBxId());
		model.put("menuId", CtPltNoPrListDVoMnuList.size()==0?"":CtPltNoPrListDVoMnuList.get(0).getMenuId());
		model.put("CtPltNoPrListDVoList", CtPltNoPrListDVoMnuList);	
		model.put("paramsForList", ParamUtil.getQueryString(request, "tId", "hghtPx", "menuId"));
		return LayoutUtil.getJspPath("/ct/plt/listNotcPrListTabPlt");
	}
	
	/** 사용자별 포틀릿 설정 조회 */
	private List<CtPltNoPrListDVo> queryCtPltNoPrListDVoList(UserVo userVo, String pltId, String langTypCd) throws SQLException{
		CtPltNoPrListDVo CtPltNoPrListDVo = new CtPltNoPrListDVo();
		CtPltNoPrListDVo.setUserUid(userVo.getUserUid());
		CtPltNoPrListDVo.setPltId(pltId);
		CtPltNoPrListDVo.setQueryLang(langTypCd);
		CtPltNoPrListDVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<CtPltNoPrListDVo> CtPltNoPrListDVoList = (List<CtPltNoPrListDVo>)commonSvc.queryList(CtPltNoPrListDVo);
		return CtPltNoPrListDVoList;
	}
	
	/** 포틀릿 설정 */
	@RequestMapping(value = "/ct/plt/setNotcPrListBxPltSetupPop")
	public String setNoPrListBxPltSetupPop(HttpServletRequest request,
			@Parameter(name="pltId", required=false) String pltId,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		List<CtPltNoPrListDVo> storedCtPltNoPrListDVoList = queryCtPltNoPrListDVoList(userVo, pltId, langTypCd);
		List<CtPltNoPrListDVo> targetCtPltNoPrListDVoList = new ArrayList<CtPltNoPrListDVo>();  
		List<CtPltNoPrListDVo> CtPltNoPrListDVoList = new ArrayList<CtPltNoPrListDVo>(); 
		List<String> storedBxIdList = new ArrayList<String>();
		
		// 대상 탭메뉴 초기화
		for(String bxId : NOPR_LIST_PLT){
			CtPltNoPrListDVo CtPltNoPrListDVo = new CtPltNoPrListDVo();
			CtPltNoPrListDVo.setBxId(bxId);
			if(bxId.equals("notc")) CtPltNoPrListDVo.setBxNm("NotcCm");
			else if(bxId.equals("pr"))  CtPltNoPrListDVo.setBxNm("PrCm");
			targetCtPltNoPrListDVoList.add(CtPltNoPrListDVo);
		}

		// 최초 설정상태의 경우 기능메뉴(개인명함)를 디폴트로 설정하고 추가한다. 
		if(storedCtPltNoPrListDVoList==null || storedCtPltNoPrListDVoList.isEmpty()){
			for(CtPltNoPrListDVo CtPltNoPrListDVo  : targetCtPltNoPrListDVoList)
			{
				//if(CtPltNoPrListDVo.getBxId().equals("notc"))
					CtPltNoPrListDVo.setUseYn("Y");
				CtPltNoPrListDVoList.add(CtPltNoPrListDVo);
			}
		}
		else
		{
			// 저장목록이 있을경우 우선순위 정렬로 추가한다.
			for(CtPltNoPrListDVo storedCtPltNoPrListDVo  : storedCtPltNoPrListDVoList)
			{
				storedBxIdList.add(storedCtPltNoPrListDVo.getBxId());
				storedCtPltNoPrListDVo.setUseYn("Y");
				
				//대상목록에서 리소스명을 가져온다.
				for(CtPltNoPrListDVo targetCtPltNoPrListDVo  : targetCtPltNoPrListDVoList)
				{
					if(storedCtPltNoPrListDVo.getBxId().equals(targetCtPltNoPrListDVo.getBxId()))
					{
						storedCtPltNoPrListDVo.setBxNm(targetCtPltNoPrListDVo.getBxNm());
						break;
					}
				}

				CtPltNoPrListDVoList.add(storedCtPltNoPrListDVo);	
			}
			
			// 대상목록에서 저장목록을 제외하고 추가한다.
			for(CtPltNoPrListDVo targetCtPltNoPrListDVo  : targetCtPltNoPrListDVoList)
			{
				if(storedBxIdList.contains(targetCtPltNoPrListDVo.getBxId())) continue;
				CtPltNoPrListDVoList.add(targetCtPltNoPrListDVo);	
			}
		}
		
		// 권한 있는 결재함 체크
		String url, menuId;
		PtMnuDVo ptMnuDVo;
		Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap = ptLoutSvc.getLoutCombByCombIdMap(userVo.getCompId(), langTypCd);
		
		List<CtPltNoPrListDVo> CtPltNoPrListDVoMnuList = new ArrayList<CtPltNoPrListDVo>(); 
		for(CtPltNoPrListDVo ctPltNoPrListDVo  : CtPltNoPrListDVoList)
		{
			url = ctCmSvc.getBxUrlByBxId(ctPltNoPrListDVo.getBxId());
			menuId = ptSecuSvc.getSecuMenuId(userVo, url);
			ptMnuDVo = getMenuByMenuId(menuId, loutCombByCombIdMap);
			if(ptMnuDVo != null){
				ctPltNoPrListDVo.setBxNm(ptMnuDVo.getRescNm());
				ctPltNoPrListDVo.setMenuId(menuId);
				CtPltNoPrListDVoMnuList.add(ctPltNoPrListDVo);
			}
		}
		
		model.put("CtPltNoPrListDVoList", CtPltNoPrListDVoMnuList);
		
		return LayoutUtil.getJspPath("/ct/plt/setNotcPrListBxPltSetupPop");
	}
	
	/** [AJAX]  포틀릿 설정 저장 */
	@RequestMapping(value = "/ct/plt/transNoPrListBxPltSetupAjx")
	public String transNoPrListBxPltSetupAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String pltId = (String)jsonObject.get("pltId");
		String bxIds = (String)jsonObject.get("bxIds");
		
		QueryQueue queryQueue = new QueryQueue();
		CtPltNoPrListDVo CtPltNoPrListDVo;
		if(bxIds!=null && !bxIds.isEmpty()){
			
			CtPltNoPrListDVo = new CtPltNoPrListDVo();
			CtPltNoPrListDVo.setUserUid(userVo.getUserUid());
			queryQueue.delete(CtPltNoPrListDVo);
			Integer sortOrdr = 1;
			
			for(String bxId : bxIds.split(",")){
				CtPltNoPrListDVo = new CtPltNoPrListDVo();
				CtPltNoPrListDVo.setUserUid(userVo.getUserUid());
				CtPltNoPrListDVo.setBxId(bxId);
				CtPltNoPrListDVo.setPltId(pltId);
				CtPltNoPrListDVo.setSortOrdr(sortOrdr.toString());
				sortOrdr++;
				queryQueue.insert(CtPltNoPrListDVo);
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
		PtMnuLoutCombDVo ptMnuLoutCombDVo = loutCombByCombIdMap.get(Hash.hashId(menuId));
		return ptMnuLoutCombDVo==null ? null : ptMnuLoutCombDVo.getPtMnuDVo();
	}
	
}

