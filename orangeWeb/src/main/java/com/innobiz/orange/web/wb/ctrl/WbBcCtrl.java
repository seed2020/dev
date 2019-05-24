package com.innobiz.orange.web.wb.ctrl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.Format;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.em.svc.EmailSvc;
import com.innobiz.orange.web.em.vo.CmEmailBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.wb.svc.WbBcFileSvc;
import com.innobiz.orange.web.wb.svc.WbBcSvc;
import com.innobiz.orange.web.wb.svc.WbCmSvc;
import com.innobiz.orange.web.wb.vo.WbBcAgntAdmBVo;
import com.innobiz.orange.web.wb.vo.WbBcAgntSetupBVo;
import com.innobiz.orange.web.wb.vo.WbBcBVo;
import com.innobiz.orange.web.wb.vo.WbBcBumkRVo;
import com.innobiz.orange.web.wb.vo.WbBcClnsCVo;
import com.innobiz.orange.web.wb.vo.WbBcCntcDVo;
import com.innobiz.orange.web.wb.vo.WbBcFileDVo;
import com.innobiz.orange.web.wb.vo.WbBcFldBVo;
import com.innobiz.orange.web.wb.vo.WbBcImgDVo;
import com.innobiz.orange.web.wb.vo.WbBcLstSetupBVo;
import com.innobiz.orange.web.wb.vo.WbBcMetngDVo;
import com.innobiz.orange.web.wb.vo.WbBcUserLstSetupRVo;

/** 명함관리 */
@Controller
public class WbBcCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WbBcCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 공통 서비스 */
	@Autowired
	private WbBcSvc wbBcSvc;
	
	/** 공통 서비스 */
	@Autowired
	private WbCmSvc wbCmSvc;
	
	/** 첨부파일 서비스 */
	@Autowired
	private WbBcFileSvc wbBcFileSvc;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 이메일 서비스 */
	@Resource(name = "emEmailSvc")
	private EmailSvc emailSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 문서뷰어 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** 문서 목록 조회 */
	@RequestMapping(value = {"/wb/listBc","/wb/listOpenBc","/wb/listAgntBc","/wb/adm/listAllBc","/wb/pub/listPubBc"})
	public String listBc(HttpServletRequest request,
			@RequestParam(value = "lstTyp", required = false) String lstTyp,
			ModelMap model) throws Exception {
		
		// 요청경로 세팅
		String path = wbCmSvc.getRequestPath(request, model , null);
		
		if(path.startsWith("listOpenBc") || path.startsWith("listAllBc"))
			lstTyp = "L";
		// 목록 유형
		if(lstTyp==null || lstTyp.isEmpty()) lstTyp = "F";
				
		//목록형이면 문서 목록 페이지 호출
		if("L".equals(lstTyp)) return listBcFrm(request, model);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 대리자UID
		String schBcRegrUid = ParamUtil.getRequestParam(request, "schBcRegrUid", false);
		
		if(path.startsWith("listAgnt")){ // 대리명함
			// 대리명함 조회UID가 있을경우 명함 등록자UID로 세팅한다.
			schBcRegrUid = wbCmSvc.getSchBcRegrUid(schBcRegrUid, userVo, langTypCd, model);
			if(schBcRegrUid != null && !schBcRegrUid.isEmpty() ) {
				model.put("schBcRegrUid", schBcRegrUid);
			}
		}
		
		if(path.startsWith("listAllBc")){
			//기본 목록설정항목 조회
			WbBcLstSetupBVo wbBcLstSetupBVo = new WbBcLstSetupBVo();
			wbBcLstSetupBVo.setCompId(userVo.getCompId());
			@SuppressWarnings("unchecked")
			List<WbBcLstSetupBVo> wbBcLstSetupBVoList = (List<WbBcLstSetupBVo>)commonSvc.queryList(wbBcLstSetupBVo);
			//관리자 목록설정이 없을경우
			if(wbBcLstSetupBVoList.size() == 0 ){
				wbBcLstSetupBVoList = wbBcSvc.setWbBcLstSetupInit(wbBcLstSetupBVoList, "Y");
			}
			model.put("allVoList", wbBcLstSetupBVoList);
			model.put("wbBcUserLstSetupRVoList", wbBcSvc.setWbBcLstSetupInit(new ArrayList<WbBcLstSetupBVo>(), null));
		}else{
			/** 목록 설정 조회 */
			wbBcSvc.setWbBcSetupInit(request, userVo , model);
		}
		
		model.put("paramsForList", ParamUtil.getQueryString(request, "lstTyp", "bcId", "addUserUid"));
		// get 파라미터를 post 파라미터로 전달하기 위해
		model.put("paramEntryList", ParamUtil.getEntryMapList(request, "menuId", "lstTyp", "bcId", "addUserUid"));
		return LayoutUtil.getJspPath("/wb/bc/listBc");
	}
	
	/** [FRAME] 명함 목록 조회(개인, 공개, 대리, 전체, 공유) */
	@RequestMapping(value = {"/wb/listBcFrm","/wb/listOpenBcFrm","/wb/listAgntBcFrm","/wb/pub/listPubBcFrm"})
	public String listBcFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		
		// set, list 로 시작하는 경우 처리
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 요청경로 세팅
		String path = wbCmSvc.getRequestPath(request, model , null);	
		
		String compId = userVo.getCompId();	
		
		// 조회조건 매핑
		WbBcBVo wbBcBVo = new WbBcBVo();
		VoUtil.bind(request, wbBcBVo);
		wbBcBVo.setQueryLang(langTypCd);
		
		if(compId != null ) wbBcBVo.setCompId(compId);
		
		String schBcRegrUid = ParamUtil.getRequestParam(request, "schBcRegrUid", false);
		
		/** 공유명함여부 */
		boolean isPub = request.getRequestURI().startsWith("/wb/pub/");
		
		if(wbBcBVo.getSchFldId()!=null && !wbBcBVo.getSchFldId().isEmpty() && "ROOT".equals(wbBcBVo.getSchFldId())) wbBcBVo.setSchFldId(null);
		
		boolean flag = false;
		if(path.startsWith("listBc")){//개인명함
			wbBcBVo.setRegrUid(userVo.getUserUid());
			wbBcBVo.setCompId(null);
			flag = true;
		}else if(path.startsWith("listOpenBc")){//공개명함
			wbBcBVo.setSchUserUid(userVo.getUserUid());//사용자UID
			wbBcBVo.setSchCompId(userVo.getCompId());//사용자 회사코드
			wbBcBVo.setSchDeptId(userVo.getDeptId());//사용자 부서코드
			wbBcBVo.setQueryLang(langTypCd);
			String[] schOpenTypCds = (String[])request.getParameterValues("schOpenTypCd");
			if(schOpenTypCds==null) schOpenTypCds = new String[]{"allPubl"};
			if(schOpenTypCds != null && schOpenTypCds.length > 0 ){
				wbBcBVo.setSchOpenTypCds(schOpenTypCds);
				flag = true;
			}
			model.put("schOpenTypCds", schOpenTypCds);
		}else if(path.startsWith("listAgntBc")){//대리명함
			// 대리명함 조회UID가 있을경우 명함 등록자UID로 세팅한다.
			schBcRegrUid = wbCmSvc.getSchBcRegrUid(schBcRegrUid, userVo, langTypCd, model);
			if(schBcRegrUid != null && !schBcRegrUid.isEmpty() ) {
				wbBcBVo.setRegrUid(schBcRegrUid);
				wbBcBVo.setCompId(null);
				model.put("schBcRegrUid", schBcRegrUid);
				flag = true;
			}
			// 대리명함 조회UID가 없을경우 대리관리자 목록이 없는 것이므로 명함 조회를 하지 않는다.
		}else if(path.startsWith("listAllBc")){//관리자메뉴 명함관리			
			flag = true;
		}
		wbBcBVo.setPub(isPub); // 공유명함여부
		
		if(flag || isPub){
			Map<String,Object> rsltMap = wbBcSvc.getWbBcMapList(request , wbBcBVo );
			model.put("recodeCount", rsltMap.get("recodeCount"));
			model.put("wbBcBMapList", rsltMap.get("wbBcBMapList"));
		}
		if(path.startsWith("listAllBc")){
			//기본 목록설정항목 조회
			WbBcLstSetupBVo wbBcLstSetupBVo = new WbBcLstSetupBVo();
			if(compId != null){
				wbBcLstSetupBVo.setCompId(compId);
			}
			@SuppressWarnings("unchecked")
			List<WbBcLstSetupBVo> wbBcLstSetupBVoList = (List<WbBcLstSetupBVo>)commonSvc.queryList(wbBcLstSetupBVo);
			//관리자 목록설정이 없을경우
			if(wbBcLstSetupBVoList.size() == 0 ){
				wbBcLstSetupBVoList = wbBcSvc.setWbBcLstSetupInit(wbBcLstSetupBVoList, "Y");
			}
			model.put("allVoList", wbBcLstSetupBVoList);
			model.put("wbBcUserLstSetupRVoList", wbBcSvc.setWbBcLstSetupInit(new ArrayList<WbBcLstSetupBVo>(), null));
		}else{
			/** 목록 설정 조회 */
			wbBcSvc.setWbBcSetupInit(request, userVo , model);
		}
		
		model.put("paramsForList", ParamUtil.getQueryString(request, "typ","schBcRegrUid", "addUserUid"));
		model.put("params", ParamUtil.getQueryString(request));
		// get 파라미터를 post 파라미터로 전달하기 위해
		model.put("paramEntryList", ParamUtil.getEntryMapList(request));
		// print css 적용
		if(request.getAttribute("printView")==null){
			request.setAttribute("printView", "print100");
		}
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if("Y".equals(sysPlocMap.get("mailEnable"))){
			model.put("mailEnable", "Y");
		}
		// 타사 조직도 조회
		if("Y".equals(sysPlocMap.get("globalOrgChartEnable"))){
			request.setAttribute("globalOrgChartEnable", Boolean.TRUE);
		}
		if(path.endsWith("Frm")){// 프레임
			model.put("pageSuffix", "Frm");
			return LayoutUtil.getJspPath("/wb/bc/listBcCont", "Frm");
		}
		
		return LayoutUtil.getJspPath("/wb/bc/listBcCont");
	}
	
	/** [POPUP] 개인,공개 명함 목록 조회 */
	@RequestMapping(value = {"/wb/findBcPop", "/wb/adm/findBcPop", "/wb/pub/findBcPop"})
	public String findBcPop(HttpServletRequest request,
			ModelMap model) throws Exception {
				
		return LayoutUtil.getJspPath("/wb/findBcPop");
	}
	
	/** [FRAME] 개인,공개 명함 목록 조회 */
	@RequestMapping(value = {"/wb/findBcFrm", "/wb/adm/findBcFrm", "/wb/pub/findBcFrm"})
	public String findBcFrm(HttpServletRequest request,
			@Parameter(name="detlViewType", required=true) String detlViewType,
			@Parameter(name="schOpenTypCd", required=false) String schOpenTypCd,			
			@Parameter(name="schBcRegrUid", required=false) String schBcRegrUid,
			@Parameter(name="pagingYn", required=false) String pagingYn,
			ModelMap model) throws Exception {
		
		/** 공유명함여부 */
		boolean isPub = request.getRequestURI().startsWith("/wb/pub/");
		
		// set, list 로 시작하는 경우 처리
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 조회조건 매핑
		WbBcBVo wbBcBVo = new WbBcBVo();
		VoUtil.bind(request, wbBcBVo);
		wbBcBVo.setQueryLang(langTypCd);
		wbBcBVo.setCompId(userVo.getCompId());
		
		boolean flag = false;
		if("bcList".equals(detlViewType)){//개인명함
			String listPage = ParamUtil.getRequestParam(request, "listPage", false);
			if(listPage == null || !"listAllMetng".equals(listPage)){
				wbBcBVo.setRegrUid((schBcRegrUid != null && !"".equals(schBcRegrUid)) ? schBcRegrUid : userVo.getUserUid());
				wbBcBVo.setCompId(null);
			}			
			flag = true;
		}else if("bcOpenList".equals(detlViewType)){//공개명함
			wbBcBVo.setSchUserUid(userVo.getUserUid());//사용자UID
			wbBcBVo.setSchCompId(userVo.getCompId());//사용자 회사코드
			wbBcBVo.setSchDeptId(userVo.getDeptId());//사용자 부서코드
			String[] schOpenTypCds =new String[]{"allPubl","deptPubl","apntrPubl"};
			wbBcBVo.setSchOpenTypCds(schOpenTypCds);
			model.put("schOpenTypCds", schOpenTypCds);
			
			// 원본 , 복사원본 , 메인설정 상태 조건절 추가
			wbBcBVo.setWhereSqllet("AND MAIN_SETUP_YN IN ('O', 'C','Y')");
			flag = true;
		}else if("bcPubList".equals(detlViewType)){//공유명함
			isPub = true;			
			flag = true;
		}
		wbBcBVo.setPub(isPub); // 공유명함여부
		if(flag){
			if(pagingYn != null && "N".equals(pagingYn)){//페이징여부
				@SuppressWarnings("unchecked")
				List<WbBcBVo> wbBcBVoList = (List<WbBcBVo>)commonSvc.queryList(wbBcBVo);
				model.put("wbBcBMapList", wbBcBVoList);
			}else{
				Map<String,Object> rsltMap = wbBcSvc.getWbBcMapList(request , wbBcBVo );
				model.put("recodeCount", rsltMap.get("recodeCount"));
				model.put("wbBcBMapList", rsltMap.get("wbBcBMapList"));
			}
		}
				
		return LayoutUtil.getJspPath("/wb/findBcFrm");
	}
	
	/** 개인 명함 상세보기 */
	@RequestMapping(value = {"/wb/viewBc","/wb/viewOpenBc","/wb/viewAgntBc","/wb/adm/viewAllBc", "/wb/viewBcFrm","/wb/viewOpenBcFrm","/wb/viewAgntBcFrm",
			"/wb/pub/viewPubBc","/wb/pub/viewPubBcFrm"})
	public String viewBc(HttpServletRequest request,
			@RequestParam(value = "bcId", required = true) String bcId,
			ModelMap model) throws Exception {
		
		try{
			// 요청경로 세팅
			String path = wbCmSvc.getRequestPath(request, model , null);
			
			//관리자 (사용자 권한 체크)
			if("viewAllBc".equals(path) ) wbCmSvc.checkUserAuth(request, "A", null);
					
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			WbBcBVo wbBcBVo = new WbBcBVo();
			wbBcBVo.setQueryLang(langTypCd);
			// bcId가 있을경우 해당 명함 정보를 조회한다.
			wbBcBVo.setBcId(bcId);
			
			/** 공유명함여부 */
			boolean isPub = request.getRequestURI().startsWith("/wb/pub/");
			
			//개인명함일 경우 사용자UID를 조회조건에 포함한다.
			if(path.startsWith("viewBc")){
				wbBcBVo.setRegrUid(userVo.getUserUid());
				wbBcBVo.setSchCompId(userVo.getCompId());
			}
			
			wbBcBVo.setPub(isPub); // 공유명함여부
			//명함조회
			wbBcBVo = wbBcSvc.getWbBcInfo(request, wbBcBVo);
			if(!isPub && wbBcBVo != null){
				//관련미팅 정보
				WbBcMetngDVo wbBcMetngDVo = new WbBcMetngDVo();
				wbBcMetngDVo.setBcId(wbBcBVo.getBcId());
				if(!"priv".equals(wbBcBVo.getPublTypCd()) && !"viewAllBc".equals(path) ){
					wbBcMetngDVo.setWhereSqllet("AND ( WBM_T.REGR_UID = '"+userVo.getUserUid()+"' OR WBM_T.OPEN_YN = 'Y')");
				}
				model.put("wbBcMetngDVoList", wbBcSvc.getWbBcMetngList(wbBcMetngDVo));
			}
			
			// 첨부파일 리스트 model에 추가
			wbBcFileSvc.putFileListToModel(wbBcBVo.getBcId() != null ? wbBcBVo.getBcId() : null, model, userVo.getCompId());
			
			//대리명함
			if(path.startsWith("viewAgntBc")){
				//String schBcRegrUid = ParamUtil.getRequestParam(request, "schBcRegrUid", true);
				/** 사용자의 명함 대리 관리자 정보 */
				Map<String,Object> agntInfoMap = wbCmSvc.getAgntInfoMap(request ,wbBcBVo.getRegrUid() , userVo.getUserUid() , null );
				
				WbBcAgntAdmBVo wbBcAgntAdmBVo = (WbBcAgntAdmBVo)agntInfoMap.get("wbBcAgntAdmBVo");
				model.put("wbBcAgntAdmBVo", wbBcAgntAdmBVo);
				model.put("schBcRegrUid", wbBcAgntAdmBVo.getRegrUid());
			}
			
			model.put("wbBcBVo", wbBcBVo);
			model.put("params", ParamUtil.getQueryString(request));
			model.put("paramsForList", ParamUtil.getQueryString(request, "bcId","schBcRegrUid", "addUserUid"));
			
			// print css 적용
			if(request.getAttribute("printView")==null){
				request.setAttribute("printView", "print100");
			}
			
			// 프레임
			if(path.endsWith("Frm")) {
				model.put("pageSuffix", "Frm");
				return LayoutUtil.getJspPath("/wb/viewBc","Frm");
			}
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
			return LayoutUtil.getResultJsp();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
			return LayoutUtil.getResultJsp();
		}
				
		return LayoutUtil.getJspPath("/wb/viewBc");
	}
	
	/** [POPUP] 개인 명함 상세보기 */
	@RequestMapping(value = {"/wb/viewBcPop","/wb/adm/viewBcPop"})
	public String viewBcPop(HttpServletRequest request,
			@RequestParam(value = "bcId", required = true) String bcId,
			@Parameter(name="schBcRegrUid", required=false) String schBcRegrUid,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/wb/viewBcPop");
	}
	
	
	/** [FRAME] 개인 명함 상세보기 */
	//"/wb/viewBcFrm", 
	@RequestMapping(value = {"/wb/adm/viewBcFrmTEMP"})
	public String viewBcFrm(HttpServletRequest request,
			@RequestParam(value = "bcId", required = true) String bcId,
			@Parameter(name="schBcRegrUid", required=false) String schBcRegrUid,
			ModelMap model) throws Exception {
		
		try{
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			WbBcBVo wbBcBVo = new WbBcBVo();
			if(schBcRegrUid != null && !schBcRegrUid.isEmpty()){
				/** 사용자의 명함 대리 관리자 정보 */
				Map<String,Object> agntInfoMap = wbCmSvc.getAgntInfoMap(request,  schBcRegrUid , userVo.getUserUid() , null );
				
				//대리관리자일 경우 대리권한 부여자의 회사ID를 조회한다.
				wbBcBVo.setCompId((String)agntInfoMap.get("compId"));
				wbBcBVo.setRegrUid(schBcRegrUid);
			}else{
				wbBcBVo.setCompId(userVo.getCompId());
				//wbBcBVo.setRegrUid(userVo.getUserUid());
			}
			
			// bcId가 있을경우 해당 명함 정보를 조회한다.
			if (bcId != null && !bcId.isEmpty()) {
				wbBcBVo.setBcId(bcId);
				wbBcBVo.setQueryLang(langTypCd);
				wbBcBVo = wbBcSvc.getWbBcInfo(request, wbBcBVo);
				
				if(wbBcBVo != null){
					//관련미팅 정보
					WbBcMetngDVo wbBcMetngDVo = new WbBcMetngDVo();
					wbBcMetngDVo.setBcId(wbBcBVo.getBcId());
					if(!"priv".equals(wbBcBVo.getPublTypCd())){
						wbBcMetngDVo.setWhereSqllet("AND ( WBM_T.REGR_UID = '"+(schBcRegrUid != null && !schBcRegrUid.isEmpty() ? wbBcBVo.getRegrUid() : userVo.getUserUid())+"' OR WBM_T.OPEN_YN = 'Y')");
					}
					model.put("wbBcMetngDVoList", wbBcSvc.getWbBcMetngList(wbBcMetngDVo));
				}
				
				// 첨부파일 리스트 model에 추가
				wbBcFileSvc.putFileListToModel(wbBcBVo.getBcId() != null ? wbBcBVo.getBcId() : null, model, userVo.getCompId());
			}
			
			model.put("wbBcBVo", wbBcBVo);		
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
			return LayoutUtil.getResultJsp();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
			return LayoutUtil.getResultJsp();
		}
		return LayoutUtil.getJspPath("/wb/viewBcFrm");
	}
	
	/** 개인 명함 등록 수정 화면 출력 */
	@RequestMapping(value = {"/wb/setBc","/wb/setAgntBc","/wb/adm/setAllBc","/wb/setBcFrm","/wb/setAgntBcFrm",
			"/wb/pub/setPubBc", "/wb/pub/setPubBcFrm"})
	public String setBc(HttpServletRequest request,
			@RequestParam(value = "bcId", required = false) String bcId,
			@RequestParam(value = "toBcId", required = false) String toBcId,
			ModelMap model) throws Exception {
		
		try{
			// 요청경로 세팅
			String path = wbCmSvc.getRequestPath(request, model , null);
			
			//관리자 (사용자 권한 체크)
			if(path.startsWith("setAllBc")) wbCmSvc.checkUserAuth(request, "A", null);
					
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			String schBcRegrUid = "";
			//대리명함일시
			if(path.startsWith("setAgntBc")){
				schBcRegrUid = ParamUtil.getRequestParam(request, "schBcRegrUid", true);
				/** 사용자의 명함 대리 관리자 정보 */
				Map<String,Object> agntInfoMap = wbCmSvc.getAgntInfoMap(request , schBcRegrUid , userVo.getUserUid() , "RW" );
				WbBcAgntAdmBVo wbBcAgntAdmBVo = (WbBcAgntAdmBVo)agntInfoMap.get("wbBcAgntAdmBVo");
				model.put("wbBcAgntAdmBVo", wbBcAgntAdmBVo);
			}
			
			/** 공유명함여부 */
			boolean isPub = request.getRequestURI().startsWith("/wb/pub/");
			
			WbBcBVo wbBcBVo = new WbBcBVo();
			// 수정이거나 동명이인의 정보를 조회하는 경우
			if (( bcId != null && !bcId.isEmpty()) || ( toBcId != null && !toBcId.isEmpty() )) {
				wbBcBVo.setQueryLang(langTypCd);
				//wbBcBVo.setCompId(userVo.getCompId());
				wbBcBVo.setBcId(toBcId != null && !toBcId.isEmpty() ? toBcId: bcId);
				if("setBc".equals(path) && ( toBcId == null || toBcId.isEmpty() )){//개인명함
					wbBcBVo.setRegrUid(userVo.getUserUid());
				}else if("setAgntBc".equals(path)){//대리명함
					// 대리명함 여부를 판단해 등록자UID를 세팅한다.
					wbBcBVo.setRegrUid(schBcRegrUid);
				}
				wbBcBVo.setPub(isPub); // 공유명함여부
				wbBcBVo = wbBcSvc.getWbBcInfo(request, wbBcBVo);
				if(path.startsWith("setAllBc")) schBcRegrUid = wbBcBVo.getRegrUid();//등록자의 UID를 세팅한다.
				
				if(bcId != null && !"".equals(bcId)){
					wbBcBVo.setBcId(bcId);//수정이면서 동명이인의 명함 정보일 경우 해당 VO로 조회후 bcId를 원래대로 변경
				}
			}else{
				// 조직 사용자 추가
				String addUserUid = ParamUtil.getRequestParam(request, "addUserUid", false);
				if(addUserUid!=null && !addUserUid.isEmpty()){
					wbBcSvc.setUserToBc(wbBcBVo, addUserUid, langTypCd);
				}else{
					wbBcBVo.setWbBcCntcDVo(wbBcSvc.setCntcList(0 , "CNTC"));// 연락처목록 초기화
					wbBcBVo.setWbBcEmailDVo(wbBcSvc.setCntcList(1 , "EMAIL"));// 이메일목록 초기화
				}
				
				wbBcBVo.setNatyCd("ko".equals(langTypCd) ? "KR" : langTypCd);
				// 폴더ID
				String schFldId = ParamUtil.getRequestParam(request, "schFldId", false);
				if(schFldId != null && !schFldId.isEmpty()){
					// 폴더 기본 테이블
					WbBcFldBVo wbBcFldBVo = new WbBcFldBVo();
					wbBcFldBVo.setQueryLang(langTypCd);
					wbBcFldBVo.setBcFldId(schFldId);
					wbBcFldBVo.setPub(isPub); // 공유명함여부
					if(!isPub) wbBcFldBVo.setRegrUid(path.startsWith("setAgntBc") ? schBcRegrUid : userVo.getUserUid());
					wbBcFldBVo = (WbBcFldBVo)commonSvc.queryVo(wbBcFldBVo);
					if(wbBcFldBVo!=null){
						wbBcBVo.setFldId(schFldId);
						wbBcBVo.setFldNm(wbBcFldBVo.getFldNm());
					}
				}
				
			}
			model.put("schBcRegrUid", schBcRegrUid);
			model.put("wbBcBVo", wbBcBVo);
			
			// 첨부파일 리스트 model에 추가
			wbBcFileSvc.putFileListToModel(wbBcBVo.getBcId() != null ? wbBcBVo.getBcId() : null, model, userVo.getCompId());
			
			if(!isPub){
				/** 친밀도 목록 조회 */
				WbBcClnsCVo wbBcClnsCVo = new WbBcClnsCVo();
				wbBcClnsCVo.setRegrUid((schBcRegrUid != null && !"".equals(schBcRegrUid)) ? schBcRegrUid : userVo.getUserUid());
				@SuppressWarnings("unchecked")
				List<WbBcClnsCVo> wbBcClnsCVoList = (List<WbBcClnsCVo>)commonSvc.queryList(wbBcClnsCVo);
				model.put("wbBcClnsCVoList", wbBcClnsCVoList);
			}
			
			/** 성별코드 조회 */
			List<PtCdBVo> genCdList = ptCmSvc.getCdList("GEN_CD", langTypCd, "Y");
			model.put("genCdList", genCdList);
			
			/** 국적코드 조회 */
			List<PtCdBVo> natyCdList = wbCmSvc.getCdList("NATY_CD", langTypCd, "Y");
			model.put("natyCdList", natyCdList);
			
			model.put("params", ParamUtil.getQueryString(request));
			model.put("paramsForList", ParamUtil.getQueryString(request, "bcId","schBcRegrUid", "addUserUid"));
			
			// 프레임
			if(path.endsWith("Frm")) {
				model.put("pageSuffix", "Frm");
				return LayoutUtil.getJspPath("/wb/setBc","Frm");
			}
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
			return LayoutUtil.getResultJsp();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
			return LayoutUtil.getResultJsp();
		}		
		return LayoutUtil.getJspPath("/wb/setBc");
	}
	
	/** 명함 등록 수정 (저장) */
	@RequestMapping(value = {"/wb/transBc","/wb/transAgntBc","/wb/adm/transAllBc","/wb/transBcFrm","/wb/transAgntBcFrm","/wb/adm/transAllBcFrm",
			"/wb/pub/transPubBc","/wb/pub/transPubBcFrm"})
	public String transBc(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		UploadHandler uploadHandler = null;
		try {
			// Multipart 파일 업로드
			uploadHandler = wbBcFileSvc.upload(request);

			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			String path = wbCmSvc.getRequestPath(request, model , null);
			
			//관리자 (사용자 권한 체크)
			if(path.startsWith("transAllBc")) wbCmSvc.checkUserAuth(request, "A", null);
						
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			String viewPage = ParamUtil.getRequestParam(request, "viewPage", true);
			
			String bcId = ParamUtil.getRequestParam(request, "bcId", false);
			String delList = ParamUtil.getRequestParam(request, "delList", false);
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
						
			QueryQueue queryQueue = new QueryQueue();
			
			if (bcId == null || bcId.isEmpty()) {// 등록일 경우 목록 화면으로 이동
				model.put("todo", "parent.location.replace('" + listPage + "');");
			} else {// 수정일 경우 상세보기 화면으로 이동
				model.put("todo", "parent.location.replace('" + viewPage + "');");
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			WbBcBVo wbBcBVo = new WbBcBVo();
			VoUtil.bind(request, wbBcBVo);
			
			/** 공유명함여부 */
			boolean isPub = request.getRequestURI().startsWith("/wb/pub/");
			
			if(path.startsWith("transBc")){
				wbBcBVo.setCompId(userVo.getCompId());
			}else if(path.startsWith("transAgntBc")){
				String schBcRegrUid = ParamUtil.getRequestParam(request, "schBcRegrUid", true);
				/** 사용자의 명함 대리 관리자 정보 */
				Map<String,Object> agntInfoMap = wbCmSvc.getAgntInfoMap(request , schBcRegrUid , userVo.getUserUid() , "RW" );
				//대리관리자일 경우 대리권한 부여자의 회사ID를 조회한다.
				wbBcBVo.setCopyRegrUid(schBcRegrUid);
				wbBcBVo.setCompId((String)agntInfoMap.get("compId"));
			}else{
				wbBcBVo.setCompId(userVo.getCompId());
			}
			
			wbBcBVo.setPub(isPub); // 공유명함여부
			
			//수정이면서 관리자가 수정할 경우
			if ( bcId != null && !bcId.isEmpty() && path.startsWith("transAllBc")) {
				WbBcBVo storedWbBcBVo = new WbBcBVo();
				storedWbBcBVo.setCompId(userVo.getCompId());
				storedWbBcBVo.setBcId(bcId);
				storedWbBcBVo.setQueryLang(langTypCd);
				storedWbBcBVo = (WbBcBVo) commonSvc.queryVo(storedWbBcBVo);
				wbBcBVo.setCopyRegrUid(storedWbBcBVo.getRegrUid());
				wbBcBVo.setCompId(storedWbBcBVo.getCompId());
			}
			
			// 삭제
			String[] delIds = delList==null || delList.isEmpty() ? new String[]{} : delList.split(",");
			wbBcBVo.setDelList(delIds);
			
			wbBcSvc.saveBc(request, queryQueue , wbBcBVo , userVo);
			
			// 첨부파일 저장
			List<CommonFileVo> deletedFileList = wbBcFileSvc.saveBcFile(request, wbBcBVo.getBcId(), queryQueue);
			
			commonSvc.execute(queryQueue);
			
			// 파일 삭제
			wbBcFileSvc.deleteDiskFiles(deletedFileList);
						
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			
			String page = bcId == null || bcId.isEmpty() ? listPage : viewPage;
			
			if(path.endsWith("Frm"))
				model.put("todo", "parent.reloadBcFrm('"+page+"');");
			else
				model.put("todo", "parent.location.replace('" + page + "');");
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}finally {
			if (uploadHandler != null) try { uploadHandler.removeTempDir(); } catch (CmException ignored) {}
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] 이메일 초기정보 저장 (사용자) */
	@RequestMapping(value = {"/wb/transEmailAjx", "/wb/adm/transEmailAjx", "/wb/pub/transEmailAjx"})
	public String transEmailAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			
			JSONArray jsonArray = (JSONArray)object.get("bcIds");//명함 아이디
			
			if(jsonArray == null || jsonArray.size() == 0 ){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				LOGGER.error("jsonArray size : 0  msg:"+msg);
				model.put("message", msg);
				return LayoutUtil.getResultJsp();
			}
			
			// jsonArray size만큼 String[] 생성
			String[][] recvList = new String[jsonArray.size()][2];
			
			/** 공유명함여부 */
			boolean isPub = request.getRequestURI().startsWith("/wb/pub/");
			
			WbBcBVo wbBcBVo = null;
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
						
			// jsonArray를 String[]에 담는다.
			for(int i=0;i<jsonArray.size();i++){
				wbBcBVo = new WbBcBVo();
				wbBcBVo.setBcId((String)jsonArray.get(i));
				wbBcBVo.setQueryLang(langTypCd);
				wbBcBVo.setPub(isPub); // 공유명함여부
				wbBcBVo = wbBcSvc.getWbBcInfo(request, wbBcBVo);
				recvList[i][0] = wbBcBVo.getBcNm();
				List<WbBcCntcDVo> wbBcEmailDVo = wbBcBVo.getWbBcEmailDVo();
				recvList[i][1] = wbBcEmailDVo.get(0).getCntcCont();
			}
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 이메일 초기 정보 저장
			QueryQueue queryQueue = new QueryQueue();
			
			//이메일 Vo[업무별 정보 세팅-제목,내용]
			CmEmailBVo cmEmailBVo = new CmEmailBVo();
			cmEmailBVo.setCont("");
			
			//이메일 정보 저장
			Integer emailId = emailSvc.saveEmailInfo(request, cmEmailBVo , recvList , null , queryQueue , userVo);
			//Integer emailId = bbBullSvc.saveBullLEmail(bbBullLVo,queryQueue , userVo);
			
			commonSvc.execute(queryQueue);
			
			//메세지 처리
			emailSvc.setEmailMessage(model, request, emailId);
			
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
			return LayoutUtil.getResultJsp();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
			return LayoutUtil.getResultJsp();
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** 첨부파일 다운로드 (사용자) */
	@RequestMapping(value = {"/wb/downFile","/wb/preview/downFile", "/wb/pub/downFile"}, method = RequestMethod.POST)
	public ModelAndView downFile(HttpServletRequest request,
			@RequestParam(value = "fileIds", required = true) String fileIds,
			@RequestParam(value = "actionParam", required = false) String actionParam
			) throws Exception {
		
		try {
			if (fileIds.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 다운로드 체크
			emAttachViewSvc.chkAttachDown(request, userVo.getCompId());
			
			// 파일 목록조회
			ModelAndView mv = wbCmSvc.getFileList(request , fileIds , actionParam);
			
			return mv;
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
			return mv;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
		}
		return null;
	}
	
	
	/** 개인 명함 복사,이동 [FORM SUBMIT] */
	@RequestMapping(value = "/wb/transBcCopy")
	public String transBcCopy(HttpServletRequest request,
			@Parameter(name="menuId", required=true) String menuId,
			@Parameter(name="typ", required=true) String typ,
			@Parameter(name="copyBcId", required=true) String copyBcId,
			@Parameter(name="copyRegrUid", required=true) String copyRegrUid,
			ModelMap model) throws Exception {
		try {
			// 리소스기본(WB_BC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			// 세션의 사용자 정보
			//UserVo userVo = LoginSession.getUser(request);
			
			String[] copyBcIds = copyBcId.split(",");//복사 대상 명함
			String[] copyRegrUids = copyRegrUid.split(",");//복사 대상 사용자
			
			if(copyBcIds.length > 0 && copyRegrUids.length > 0){
				// 명함 기본(WB_BC_B) 테이블
				WbBcBVo wbBcBVo = new WbBcBVo();
				for(int i=0;i<copyBcIds.length;i++){
					wbBcBVo.setBcId(copyBcIds[i]);
					for(int j=0;j<copyRegrUids.length;j++){
						wbBcBVo.setCopyRegrUid(copyRegrUids[j]);
						wbBcSvc.copyBc(request,queryQueue , wbBcBVo , "copyBc" , "ROOT");
					}
				}
				commonSvc.execute(queryQueue);
			}
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.location.replace('./listAgntBc.do?menuId=" + menuId + "&typ="+typ+"');");
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX]대리 명함 복사,이동 */
	@RequestMapping(value = {"/wb/transBcContensCopy","/wb/adm/transAllBcContensCopy"})
	public String transBcContensCopy(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		try {
			// 리소스기본(WB_BC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			
			String mode = (String)jsonObject.get("mode");// 복사 또는 이동 모드
			
			JSONArray copyBcIds = (JSONArray)jsonObject.get("copyBcIds");//복사 대상 명함
			JSONArray copyRegrUids = (JSONArray)jsonObject.get("copyRegrUids");//복사 대상 사용자

			if(copyBcIds.size() > 0 && copyRegrUids.size() > 0){
				// 명함 기본(WB_BC_B) 테이블
				WbBcBVo wbBcBVo = new WbBcBVo();
				for(int i=0;i<copyBcIds.size();i++){
					wbBcBVo.setBcId((String)copyBcIds.get(i));
					for(int j=0;j<copyRegrUids.size();j++){
						wbBcBVo.setCopyRegrUid((String)copyRegrUids.get(j));
						wbBcSvc.copyBc(request,queryQueue , wbBcBVo , mode , "ROOT");
					}
				}
				commonSvc.execute(queryQueue);
			}
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
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
	
	/** [AJAX]개인 명함 복사,이동 [폴더] */
	@RequestMapping(value = {"/wb/transBcCopyAjx", "/wb/pub/transBcCopyAjx"})
	public String transBcCopyAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		try {
			/** 공유명함여부 */
			boolean isPub = request.getRequestURI().startsWith("/wb/pub/");
			
			// 리소스기본(WB_BC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String bcFldId = (String)jsonObject.get("bcFldId");// 복사 또는 이동 대상 아이디
			
			String mode = (String)jsonObject.get("mode");// 복사 또는 이동 모드
			
			// 대리자UID
			String schBcRegrUid = ParamUtil.getRequestParam(request, "schBcRegrUid", false);
						
			// 작성자UID
			String userUid = userVo.getUserUid();
			if(schBcRegrUid != null && !schBcRegrUid.isEmpty()){ // 대리명함
				// 세션의 언어코드
				String langTypCd = LoginSession.getLangTypCd(request);
				// 대리명함 조회UID가 있을경우 명함 등록자UID로 세팅한다.
				schBcRegrUid = wbCmSvc.getSchBcRegrUid(schBcRegrUid, userVo, langTypCd, model);
				if(schBcRegrUid != null && !schBcRegrUid.isEmpty()) userUid = schBcRegrUid;
			}
						
			JSONArray jsonArray = (JSONArray)jsonObject.get("toBcIds");// 복사 또는 이동 대상 명함 아이디
			WbBcBVo wbBcBVo = null;
			for(int i=0;i<jsonArray.size();i++){
				wbBcBVo = new WbBcBVo();
				wbBcBVo.setPub(isPub); // 공유명함여부
				wbBcBVo.setBcId((String)jsonArray.get(i));
				wbBcBVo.setCompId(userVo.getCompId());
				wbBcBVo.setCopyRegrUid(userUid);
				//wbBcBVo.setRegrUid(userVo.getUserUid());
				wbBcSvc.copyBc(request,queryQueue , wbBcBVo , mode , bcFldId);
			}
			commonSvc.execute(queryQueue);

			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
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
    
	/** 개인 명함 즐겨찾기 추가 및 제거 */
	@RequestMapping(value = {"/wb/transBumkBc", "/wb/transBumkBcFrm"})
	public String transBumkBc(HttpServletRequest request,
			@RequestParam(value = "bcId", required = true) String bcId,
			@Parameter(name="menuId", required=false) String menuId,
			@Parameter(name="typ", required=false) String typ,
			@Parameter(name="bumkYn", required=false) String bumkYn,			
			ModelMap model) throws Exception {
		
		try {
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);

			// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();

			WbBcBVo wbBcBVo = new WbBcBVo();
			VoUtil.bind(request, wbBcBVo);
			
			// 수정자, 수정일시
			wbBcBVo.setModrUid(userVo.getUserUid());
			wbBcBVo.setModDt("sysdate");
			wbBcBVo.setBumkYn(bumkYn);
			// 명함관리(WB_BC_B) 테이블 - UPDATE
			queryQueue.update(wbBcBVo);
			
			// 즐겨찾기 목록 건수 조회 : 신규 한건 생성시 마지막 번호로 저장
			WbBcBumkRVo storedWbBcBVo = new WbBcBumkRVo();
			storedWbBcBVo.setRegrUid(userVo.getUserUid());
			storedWbBcBVo.setBcId(bcId);
			if("Y".equals(bumkYn)){// 추가시 해당 즐겨찾기  저장
				WbBcBumkRVo wbBcBumkRVo = new WbBcBumkRVo();
				wbBcBumkRVo.setRegrUid(userVo.getUserUid());
				wbBcBumkRVo.setQueryLang(langTypCd);
				wbBcBumkRVo.setSchBumkYn("Y");
				Integer recodeCount = commonSvc.count(wbBcBumkRVo);// 즈
				storedWbBcBVo.setSortOrdr(String.valueOf((recodeCount.intValue()+1)));
				queryQueue.insert(storedWbBcBVo);
			}else{//제거시 해당 즐겨찾기 삭제
				queryQueue.delete(storedWbBcBVo);
			}
			
			commonSvc.execute(queryQueue);
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			
			if(request.getRequestURI().startsWith("/wb/transBumkBcFrm")) // 프레임
				model.put("todo", "parent.reloadBcFrm('./viewBcFrm.do?menuId=" + menuId + "&typ="+typ+"&bcId="+bcId+"');");
			else
				model.put("todo", "parent.location.replace('./viewBc.do?menuId=" + menuId + "&typ="+typ+"&bcId="+bcId+"');");
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** 개인 명함 삭제 */
	@RequestMapping(value = {"/wb/transBcDel","/wb/transAgntBcDel","/wb/adm/transAllBcDel", "/wb/pub/transPubBcDel",
			"/wb/transBcDelFrm","/wb/transAgntBcDelFrm","/wb/adm/transAllBcDelFrm", "/wb/pub/transPubBcDelFrm"})
	public String transBcDel(HttpServletRequest request,
			@RequestParam(value = "bcId", required = true) String bcId,
			ModelMap model) throws Exception {
		
		try {
			//목록 페이지
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			
			// 요청경로 세팅
			String path = wbCmSvc.getRequestPath(request, model , null);
			
			//관리자 (사용자 권한 체크)
			if(path.startsWith("transAllBcDel")) wbCmSvc.checkUserAuth(request, "A", null);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			WbBcBVo wbBcBVo = new WbBcBVo();
			
			/** 공유명함여부 */
			boolean isPub = request.getRequestURI().startsWith("/wb/pub/");
			
			if("transBcDel".equals(path)){
				wbBcBVo.setModrUid(userVo.getUserUid());
				//wbBcBVo.setCompId(userVo.getCompId());
			}else if("transAgntBcDel".equals(path)){
				String schBcRegrUid = ParamUtil.getRequestParam(request, "schBcRegrUid", true);
				/** 사용자의 명함 대리 관리자 정보 */
				Map<String,Object> agntInfoMap = wbCmSvc.getAgntInfoMap(request , schBcRegrUid , userVo.getUserUid() , "RW" );
				//대리관리자일 경우 대리권한 부여자의 회사ID를 조회한다.
				wbBcBVo.setModrUid(schBcRegrUid);
				wbBcBVo.setCompId((String)agntInfoMap.get("compId"));
			}else{
				wbBcBVo.setCompId(userVo.getCompId());
			}
			
			wbBcBVo.setPub(isPub); // 공유명함여부
			//삭제 id가 있을경우
			if(bcId != null && !bcId.isEmpty() ){
				wbBcBVo.setDelList(bcId.split(","));
				wbBcSvc.deleteBc(queryQueue , wbBcBVo);
				commonSvc.execute(queryQueue);
			}
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			
			if(path.endsWith("Frm")) // 프레임
				model.put("todo", "parent.reloadBcFrm('"+listPage+"');");
			else
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
	
	/** [AJAX] 개인 명함 삭제 */
	@RequestMapping(value = {"/wb/transBcDelAjx", "/wb/transPubBcDelAjx", "/wb/pub/transPubBcDelAjx"})
	public String transBcDelAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		
		try {
			// 요청경로 세팅
			wbCmSvc.getRequestPath(request, model , null);
			
			// 리소스기본(WB_BC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			JSONArray jsonArray = (JSONArray)jsonObject.get("toBcIds");// 복사 또는 이동 대상 명함 아이디
			
			if(jsonArray == null || jsonArray.size() == 0 ){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				LOGGER.error("jsonArray size : 0  msg:"+msg);
				model.put("message", msg);
				return LayoutUtil.getResultJsp();
			}
			
			// jsonArray size만큼 String[] 생성
			String[] delList = new String[jsonArray.size()];
			WbBcBVo wbBcBVo = new WbBcBVo();
			//wbBcBVo.setCompId(userVo.getCompId());
			// jsonArray를 String[]에 담는다.
			for(int i=0;i<jsonArray.size();i++){
				delList[i] = (String)jsonArray.get(i);
			}
			
			/** 공유명함여부 */
			boolean isPub = request.getRequestURI().startsWith("/wb/pub/");
			
			// 명함 ID가 있을경우 삭제
			if(delList.length > 0 ){
				wbBcBVo.setDelList(delList);
				if(isPub) wbBcBVo.setPub(isPub);
				else wbBcBVo.setModrUid(userVo.getUserUid());
				
				wbBcSvc.deleteBc(queryQueue , wbBcBVo);

				commonSvc.execute(queryQueue);
			}
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
//		} catch (CmException e) {
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return JsonUtil.returnJson(model);
	}
	
	/** 친밀도 관리 */
	/** 친밀도 등록 화면 출력 */
	@RequestMapping(value = {"/wb/setClnsPop", "/wb/adm/setClnsPop"})
	public String setClnsPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/wb/setClnsPop");
	}
	
	/** 친밀도 등록 수정 Ajax */
	@RequestMapping(value = {"/wb/transClns", "/wb/adm/transClns"})
	public String transClns(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		
		try {
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			// queryQueue 생성
			QueryQueue queryQueue = new QueryQueue();
			WbBcClnsCVo wbBcClnsCVo = new WbBcClnsCVo();

			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			//친밀도명
			String clnsNm = (String)jsonObject.get("clnsNm");
			
			/** 대리명함 일 경우 */
			String schBcRegrUid = (String)jsonObject.get("schBcRegrUid");
			
			wbBcClnsCVo.setRegrUid((schBcRegrUid != null && !"".equals(schBcRegrUid)) ? schBcRegrUid : userVo.getUserUid());
			
			if(clnsNm==null || clnsNm.isEmpty()){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				throw new CmException(msg);
			}
			String clnsId = wbCmSvc.createId("WB_BC_CLNS_C");
			
			wbBcClnsCVo.setClnsNm(clnsNm);
			wbBcClnsCVo.setClnsId(clnsId);
			queryQueue.insert(wbBcClnsCVo);			
			commonSvc.execute(queryQueue);
			
			/** 친밀도 목록 조회 */
			WbBcClnsCVo schWbBcClnsCVo = new WbBcClnsCVo();
			schWbBcClnsCVo.setRegrUid(wbBcClnsCVo.getRegrUid());
			@SuppressWarnings("unchecked")
			List<WbBcClnsCVo> wbBcClnsCVoList = (List<WbBcClnsCVo>)commonSvc.queryList(schWbBcClnsCVo);
			model.put("list", wbBcClnsCVoList);
			model.put("clnsId", clnsId);
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] 동명이인 중복 체크 */
	@RequestMapping(value = {"/wb/findPwsmCheck", "/wb/adm/findPwsmCheck", "/wb/pub/findPwsmCheck"})
	public String findPwsmCheck(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		
		try {
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			// queryQueue 생성
			WbBcClnsCVo wbBcClnsCVo = new WbBcClnsCVo();
			wbBcClnsCVo.setRegrUid(userVo.getUserUid());
			
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String schWord = (String)jsonObject.get("bcNm");
			
			/** 대리명함 일 경우 */
			String schBcRegrUid = (String)jsonObject.get("schBcRegrUid");
			
			if(schWord==null || schWord.isEmpty()){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				throw new CmException(msg);
			}
			
			/** 공유명함여부 */
			boolean isPub = request.getRequestURI().startsWith("/wb/pub/");
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			String schCat = (String)jsonObject.get("schCat") == null ? "pwsmName" : (String)jsonObject.get("schCat");				
			WbBcBVo wbBcBVo = new WbBcBVo();
			wbBcBVo.setRegrUid((schBcRegrUid != null && !"".equals(schBcRegrUid)) ? schBcRegrUid : userVo.getUserUid());
			wbBcBVo.setQueryLang(langTypCd);
			wbBcBVo.setSchCat(schCat);
			wbBcBVo.setSchWord(schWord);
			wbBcBVo.setPub(isPub); // 공유명함여부
			
			//관리자 일경우 회사 ID를 기준으로 조회
			if(schBcRegrUid != null && !"".equals(schBcRegrUid)){
				OrUserBVo orUserBVo = new OrUserBVo();
				orUserBVo.setUserUid(schBcRegrUid);
				orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
				wbBcBVo.setCompId(orUserBVo.getCompId());
			}else{
				wbBcBVo.setCompId(userVo.getCompId());
			}
			Integer recodeCount = commonSvc.count(wbBcBVo);
			//String message = messageProperties.getMessage("wb.msg.noDupBcNm", request);
			String result = "";
			if(recodeCount.intValue() == 0 ){
				//message = messageProperties.getMessage("wb.cfrm.dupBcNm", request);
				model.put("message", messageProperties.getMessage("wb.msg.noDupBcNm", request));
			}else{
				result = "popup";
				//model.put("message", message);
			}
			model.put("result", result);
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** 이미지에 대한 미리보기 */
	@RequestMapping(value = {"/wb/viewImage", "/wb/adm/viewImage", "/wb/pub/viewImage"})
    public void getTempImage(HttpServletRequest request, 
    		HttpServletResponse response) throws Exception {
		
		//uploadManager.init();
    	String fileDir = (String)request.getParameter("fileDir");
    	if(fileDir != null){
    		try {
    	    	File file = new File(fileDir);
    	    	if(file.isFile()){ 
    				FileInputStream fis = new FileInputStream(file);
    			
    				BufferedInputStream in = new BufferedInputStream(fis);
    				ByteArrayOutputStream bStream = new ByteArrayOutputStream();
    			
    				try{
    					int imgByte;
    					while ((imgByte = in.read()) != -1) {
    					    bStream.write(imgByte);
    					}
    					in.close();
    				}catch(IllegalArgumentException iae){
    					System.out.println("+ IllegalArgumentException : " + iae.toString());
    				}finally{
    					in.close();
    				}
    				
    				String type = "image/jpeg"; 
    			
    				response.setHeader("Content-Type", type);
    				response.setContentLength(bStream.size());
    				
    				bStream.writeTo(response.getOutputStream());
    				
    				response.getOutputStream().flush();
    				response.getOutputStream().close();
    	    	}
        	}catch(Exception e){
        		LOGGER.error(e.getMessage(), e);
        	}
    	}

    }
    
	/** [팝업] 사진 보기 */
    @RequestMapping(value = {"/wb/viewImagePop", "/wb/adm/viewImagePop", "/wb/pub/viewImagePop"})
	public String viewImagePop(HttpServletRequest request,
			@Parameter(name="bcId", required=false) String bcId,
			ModelMap model) throws Exception {
		
		// 명함이미지상세(WB_BC_IMG_D) 테이블
		WbBcImgDVo wbBcImgDVo = new WbBcImgDVo();
		
		wbBcImgDVo.setBcId(bcId);
		wbBcImgDVo = (WbBcImgDVo)commonSvc.queryVo(wbBcImgDVo);
		model.put("wbBcImgDVo", wbBcImgDVo);
		return LayoutUtil.getJspPath("/wb/viewImagePop");
	}
	
	/** [팝업] 사진 선택 */
	@RequestMapping(value = {"/wb/setImagePop", "/wb/adm/setImagePop", "/wb/pub/setImagePop"})
	public String setImagePop(HttpServletRequest request,
			@Parameter(name="bcId", required=false) String bcId,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/wb/setImagePop");
	}
	
	/** [히든프레임] 사진 업로드 */
	@RequestMapping(value = {"/wb/transImage", "/wb/adm/transImage", "/wb/pub/transImage"})
	public String transImage(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		UploadHandler uploadHandler = null;
		boolean tempFileDel = false;
		try{
			uploadHandler = uploadManager.createHandler(request, "temp", "wb");
			//Map<String, File> fileMap = uploadHandler.upload();//업로드 파일 정보
			//Map<String, String> param = uploadHandler.getParamMap();//파라미터 정보
			uploadHandler.upload();
			
			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			String bcId = ParamUtil.getRequestParam(request, "bcId", false);
			// 임시 경로
			
			// 등록화면에서 이미지를 등록한 경우 임시파일 정보를 제공한다.
			if(bcId == null || "".equals(bcId)){
				String tempPath = uploadHandler.getAbsolutePath("photo").replace('\\', '/');
				model.put("todo", "parent.setImageReview('"+tempPath+"');");
			}else{
				QueryQueue queryQueue = new QueryQueue();
				List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
				WbBcImgDVo wbBcImgDVo = new WbBcImgDVo();
				WbBcFileDVo wbBcFileDVo = (WbBcFileDVo)wbBcFileSvc.savePhoto(request, bcId, "photo", wbBcImgDVo , queryQueue);
				if(wbBcFileDVo != null){
					deletedFileList.add(wbBcFileDVo);
					
					// 파일 삭제
					wbBcFileSvc.deleteDiskFiles(deletedFileList);
				}
				commonSvc.execute(queryQueue);
				//String fileDir = distManager.getContextProperty("distribute.web.local.root")+distPath;
				model.put("todo", "parent.setImage('"+wbBcImgDVo.getImgPath()+"', "+wbBcImgDVo.getImgWdth()+", "+wbBcImgDVo.getImgHght()+");");
				tempFileDel = true;
			}
			
			//cm.msg.save.success=저장 되었습니다.
			//model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			
		} catch(CmException e){
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		} finally {
			if(uploadHandler!=null && tempFileDel) uploadHandler.removeTempDir();
		}
	
		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] 이미지 개별 삭제 */
	@RequestMapping(value = {"/wb/transImgDelAjx", "/wb/adm/transImgDelAjx"})
	public String transSiteImgDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String bcId = (String) object.get("bcId");
			
			if (bcId==null || bcId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 첨부파일
			List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
			
			//명함 이미지 삭제
			wbBcFileSvc.deletePhoto(bcId, deletedFileList, queryQueue);
			
			if(!queryQueue.isEmpty()){
				commonSvc.execute(queryQueue);
				
				if(deletedFileList.size() > 0 ){
					// 파일 삭제
					wbBcFileSvc.deleteDiskFiles(deletedFileList);
				}
			}
			
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
	
	/** [히든프레임] 사진 삭제 */
	@RequestMapping(value = {"/wb/transImageDel", "/wb/adm/transImageDel", "/wb/pub/transImageDel"})
	public String transImageDel(HttpServletRequest request,
			@Parameter(name="bcId", required=true) String bcId,
			ModelMap model) throws Exception {
		
		try{
			// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			
			// 명함 이미지 상세
			WbBcImgDVo wbBcImgDVo = new WbBcImgDVo();
			wbBcImgDVo.setBcId(bcId);
			queryQueue.delete(wbBcImgDVo);
			commonSvc.execute(queryQueue);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("todo", "parent.pageReload();");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}
	
		return LayoutUtil.getResultJsp();
	}
	
	/** [팝업] 동명이인 조회 출력 */
	@RequestMapping(value = {"/wb/findBcPwsmPop", "/wb/adm/findBcPwsmPop", "/wb/pub/findBcPwsmPop"})
	public String findBcPwsmPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/wb/findBcPwsmPop");
	}
	
	/** [팝업] 동명이인 조회 */
	@RequestMapping(value = {"/wb/findBcPwsmFrm", "/wb/adm/findBcPwsmFrm", "/wb/pub/findBcPwsmFrm"})
	public String findBcPwsmFrm(HttpServletRequest request,
			@Parameter(name="bcId", required=false) String bcId,
			@Parameter(name="schCat", required=false) String schCat,
			@Parameter(name="schWord", required=false) String schWord,
			@Parameter(name="schBcRegrUid", required=false) String schBcRegrUid,
			ModelMap model) throws Exception {
		
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		/** 공유명함여부 */
		boolean isPub = request.getRequestURI().startsWith("/wb/pub/");
		
		WbBcBVo wbBcBVo = new WbBcBVo();
		wbBcBVo.setPub(isPub); // 공유명함여부
		 // 대리명함 여부를 통해 등록자UID 세팅
		if(!isPub) wbBcBVo.setRegrUid((schBcRegrUid != null && !"".equals(schBcRegrUid)) ? schBcRegrUid : userVo.getUserUid());
		
		wbBcBVo.setQueryLang(langTypCd);
		if(schWord!=null && !schWord.isEmpty()){
			wbBcBVo.setSchCat(schCat);
			wbBcBVo.setSchWord(schWord);
			Integer recodeCount = commonSvc.count(wbBcBVo);
			PersonalUtil.setPaging(request, wbBcBVo, recodeCount);
			@SuppressWarnings("unchecked")
			List<WbBcBVo> wbBcBVoList = (List<WbBcBVo>)commonSvc.queryList(wbBcBVo);
			model.put("wbBcBVoList", wbBcBVoList);
			model.put("recodeCount", recodeCount);
		}
		
		return LayoutUtil.getJspPath("/wb/findBcPwsmFrm");
	}

	
/** 개인명함 END */	

/** 즐겨찾기명함 START */	
	/** 즐겨찾는 명함 목록 조회 */
	@RequestMapping(value = "/wb/setBcBumk")
	public String setBcBumk(HttpServletRequest request,
			@Parameter(name="schCat", required=false) String schCat,
			@Parameter(name="schWord", required=false) String schWord,
			ModelMap model) throws Exception {
		// set, list 로 시작하는 경우 처리
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		WbBcBumkRVo wbBcBumkRVo = new WbBcBumkRVo();
		//VoUtil.bind(request, wbBcBVo);
		wbBcBumkRVo.setRegrUid(userVo.getUserUid());
		wbBcBumkRVo.setQueryLang(langTypCd);
		wbBcBumkRVo.setSchBumkYn("Y");
		
		// 즐겨찾기 등록된 명함 조회
		@SuppressWarnings("unchecked")
		List<WbBcBVo> wbBcBVoBumkList = (List<WbBcBVo>)commonSvc.queryList(wbBcBumkRVo);
		
		// UI 구성용 - 빈 VO 하나 더함
		if(wbBcBVoBumkList==null) wbBcBVoBumkList = new ArrayList<WbBcBVo>();
		wbBcBVoBumkList.add(new WbBcBVo());
		model.put("wbBcBVoBumkList", wbBcBVoBumkList);
		
		/** 목록 설정 조회 */
		wbBcSvc.setWbBcSetupInit(request, userVo , model);
		
		// get 파라미터를 post 파라미터로 전달하기 위해
		model.put("paramEntryList", ParamUtil.getEntryMapList(request));
				
		return LayoutUtil.getJspPath("/wb/setBcBumk");
	}
	
	/** 즐겨찾는 명함 목록 조회 */
	@RequestMapping(value = "/wb/listBcBumkFrm")
	public String listBcBumkFrm(HttpServletRequest request,
			@Parameter(name="schCat", required=false) String schCat,
			@Parameter(name="schWord", required=false) String schWord,
			ModelMap model) throws Exception {
		// set, list 로 시작하는 경우 처리
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		WbBcBumkRVo wbBcBumkRVo = new WbBcBumkRVo();
		VoUtil.bind(request, wbBcBumkRVo);
		wbBcBumkRVo.setRegrUid(userVo.getUserUid());
		wbBcBumkRVo.setQueryLang(langTypCd);
		wbBcBumkRVo.setSchBumkYn("N");
		
		// 게시물(BB_X000X_L) 테이블 - COUNT
		Integer recodeCount = commonSvc.count(wbBcBumkRVo);
		PersonalUtil.setPaging(request, wbBcBumkRVo, recodeCount);
				
		// 즐겨찾기 등록할수 있는 명함 조회
		@SuppressWarnings("unchecked")
		List<WbBcBVo> wbBcBVoList = (List<WbBcBVo>)commonSvc.queryList(wbBcBumkRVo);
		model.put("wbBcBVoList", wbBcBVoList);
		model.put("recodeCount", recodeCount);
		
		/** 목록 설정 조회 */
		wbBcSvc.setWbBcSetupInit(request, userVo , model);
		
		// get 파라미터를 post 파라미터로 전달하기 위해
		model.put("paramEntryList", ParamUtil.getEntryMapList(request));
				
		return LayoutUtil.getJspPath("/wb/listBcBumk", "Frm");
	}
	
	/** 즐겨찾는 명함 저장 */
	@RequestMapping(value = "/wb/transBcBumk")
	public String transBcBumk(HttpServletRequest request,
			@Parameter(name="menuId", required=false) String menuId,
			@Parameter(name="schCat", required=false) String schCat,
			@Parameter(name="schWord", required=false) String schWord,
			ModelMap model) throws Exception {
		// 리소스기본(WB_BC_B) 테이블 - UPDATE or INSERT
		QueryQueue queryQueue = new QueryQueue();
				
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		WbBcBumkRVo wbBcBumkRVo = new WbBcBumkRVo();
		//VoUtil.bind(request, wbBcBVo);
		wbBcBumkRVo.setRegrUid(userVo.getUserUid());
		
		// 기존 즐겨찾기 초기화
		queryQueue.delete(wbBcBumkRVo);
		
		WbBcBVo wbBcBVo = new WbBcBVo();
		wbBcBVo.setModrUid(userVo.getUserUid());
		wbBcBVo.setBumkYn("N");
		//명함의 즐겨찾기 정보 초기화
		queryQueue.update(wbBcBVo);
		
		Integer sortOrdr = 1;
		// bcId를 기준으로 배열 정보 VO에 세팅
		@SuppressWarnings("unchecked")
		List<WbBcBumkRVo> list = (List<WbBcBumkRVo>)VoUtil.bindList(request, WbBcBumkRVo.class, new String[]{"bcId"});
		if(list != null){
			for(WbBcBumkRVo storedWbBcBumkRVo : list){
				// 즐겨찾기 테이블 저장
				storedWbBcBumkRVo.setRegrUid(userVo.getUserUid());
				storedWbBcBumkRVo.setSortOrdr(sortOrdr.toString());
				queryQueue.insert(storedWbBcBumkRVo);
				sortOrdr++;
				
				wbBcBVo = new WbBcBVo();
				// 수정자, 수정일시
				wbBcBVo.setModrUid(userVo.getUserUid());
				wbBcBVo.setModDt("sysdate");
				wbBcBVo.setBumkYn("Y");
				// 명함관리(WB_BC_B) 테이블 - UPDATE
				queryQueue.update(wbBcBVo);
			}
		}
		commonSvc.execute(queryQueue);
		model.put("todo", "parent.location.replace('./setBcBumk.do?menuId=" + menuId+"');");
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		return LayoutUtil.getResultJsp();
	}
	
	/** 즐겨찾는 명함 상세보기 */
	@RequestMapping(value = "/wb/viewBcBumk")
	public String viewBcBumk(HttpServletRequest request,
			@RequestParam(value = "bcId", required = true) String bcId,
			ModelMap model) throws Exception {
		
		try{
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			WbBcBVo wbBcBVo = new WbBcBVo();			
			if (bcId != null && !bcId.isEmpty()) {
				wbBcBVo.setBcId(bcId);
				wbBcBVo.setRegrUid(userVo.getUserUid());
				//wbBcBVo.setSchCompId(userVo.getCompId());
				wbBcBVo.setQueryLang(langTypCd);
				wbBcBVo = wbBcSvc.getWbBcInfo(request, wbBcBVo);
			}
			model.put("wbBcBVo", wbBcBVo);
		
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
			return LayoutUtil.getResultJsp();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
			return LayoutUtil.getResultJsp();
		}
		return LayoutUtil.getJspPath("/wb/viewBcBumk");
	}
/** 즐겨찾기명함 END */

/** 대리명함 START */
	/** 대리인 지정 목록 조회 */
	@RequestMapping(value = "/wb/setAgntAdm")
	public String setAgntAdm(HttpServletRequest request,
			ModelMap model) throws Exception {
		// set, list 로 시작하는 경우 처리
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		// 대리인 지정 목록
		WbBcAgntAdmBVo wbBcAgntAdmBVo = new WbBcAgntAdmBVo();
		wbBcAgntAdmBVo.setRegrUid(userVo.getUserUid());
		wbBcAgntAdmBVo.setQueryLang(langTypCd);
		Integer recodeCount = commonSvc.count(wbBcAgntAdmBVo);
		PersonalUtil.setPaging(request, wbBcAgntAdmBVo, recodeCount);
		model.put("recodeCount", recodeCount);
		//목록 조회
		@SuppressWarnings("unchecked")
		List<WbBcAgntAdmBVo> wbBcAgntAdmBVoList = (List<WbBcAgntAdmBVo>)commonSvc.queryList(wbBcAgntAdmBVo);
		
		// UI 구성용 - 빈 VO 하나 더함
		if(wbBcAgntAdmBVoList == null) wbBcAgntAdmBVoList = new ArrayList<WbBcAgntAdmBVo>();
		wbBcAgntAdmBVoList.add(new WbBcAgntAdmBVo());
		model.put("wbBcAgntAdmBVoList", wbBcAgntAdmBVoList);
		
		// 대리인 지정 대상 목록
		wbBcAgntAdmBVo = new WbBcAgntAdmBVo();
		wbBcAgntAdmBVo.setAgntAdmUid(userVo.getUserUid());
		wbBcAgntAdmBVo.setQueryLang(langTypCd);
		//목록 조회
		@SuppressWarnings("unchecked")
		List<WbBcAgntAdmBVo> agntSetupList = (List<WbBcAgntAdmBVo>)commonSvc.queryList(wbBcAgntAdmBVo);
		model.put("agntSetupList", agntSetupList);
		
		// 대리명함기본 설정 조회
		WbBcAgntSetupBVo wbBcAgntSetupBVo = new WbBcAgntSetupBVo();
		wbBcAgntSetupBVo.setRegrUid(userVo.getUserUid());
		wbBcAgntSetupBVo = (WbBcAgntSetupBVo)commonSvc.queryVo(wbBcAgntSetupBVo);
		model.put("wbBcAgntSetupBVo", wbBcAgntSetupBVo);
		
		return LayoutUtil.getJspPath("/wb/setAgntAdm");
	}
	
	/** 대리인 지정 정보 저장 */
	@RequestMapping(value = "/wb/transAgntAdm")
	public String transAgntAdm(HttpServletRequest request,
			@Parameter(name="menuId", required=false) String menuId,
			ModelMap model) throws Exception {
		// 리소스기본(WB_BC_B) 테이블 - UPDATE or INSERT
		QueryQueue queryQueue = new QueryQueue();
				
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		WbBcAgntAdmBVo wbBcAgntAdmBVo = new WbBcAgntAdmBVo();
		// 기존 정보 초기화
		wbBcAgntAdmBVo.setRegrUid(userVo.getUserUid());
		queryQueue.delete(wbBcAgntAdmBVo);
		
		// 대리인 UID 정보를 기준으로 VO 배열 세팅
		@SuppressWarnings("unchecked")
		List<WbBcAgntAdmBVo> list = (List<WbBcAgntAdmBVo>)VoUtil.bindList(request, WbBcAgntAdmBVo.class, new String[]{"agntAdmUid"});
		if(list != null){
			String authCd = "";
			for(WbBcAgntAdmBVo storeWbBcAgntAdmBVo : list){
				if(storeWbBcAgntAdmBVo.getAgntAdmUid() != null && !storeWbBcAgntAdmBVo.getAgntAdmUid().isEmpty()){
					authCd = request.getParameter("authCd_"+storeWbBcAgntAdmBVo.getAgntAdmUid()); // parameter:authCd 세팅 
					storeWbBcAgntAdmBVo.setAuthCd(authCd);
					storeWbBcAgntAdmBVo.setRegrUid(userVo.getUserUid());
					queryQueue.insert(storeWbBcAgntAdmBVo);
				}
			}
		}
		
		commonSvc.execute(queryQueue);
		model.put("todo", "parent.location.replace('./setAgntAdm.do?menuId=" + menuId+"');");
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		return LayoutUtil.getResultJsp();
	}
	
	/** 대리인 지정 정보 삭제 : 삭제만 Ajax 또는 직접 Url 호출하는 경우 [현재 적용 안됨: UI ]*/
	@RequestMapping(value = "/wb/transAgntAdmDel")
	public String transAgntAdmDel(HttpServletRequest request,
			@Parameter(name="menuId", required=false) String menuId,
			@Parameter(name="delUid", required=false) String delUid,
			ModelMap model) throws Exception {
		try {
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);

			// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			WbBcAgntAdmBVo wbBcAgntAdmBVo = new WbBcAgntAdmBVo();
			if(delUid != null && !delUid.isEmpty() ){
				wbBcAgntAdmBVo.setDelList(delUid.split(","));
				wbBcAgntAdmBVo.setRegrUid(userVo.getUserUid());
				queryQueue.delete(wbBcAgntAdmBVo);
				commonSvc.execute(queryQueue);
			}
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("todo", "parent.location.replace('./setAgntAdm.do?menuId=" + menuId +"');");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** 대리명함 기본값 설정 저장 */
	@RequestMapping(value = "/wb/transAgntSetup")
	public String transAgntSetup(HttpServletRequest request,
			@Parameter(name="menuId", required=false) String menuId,
			@Parameter(name="bcRegrUid", required=false) String bcRegrUid,
			ModelMap model) throws Exception {
		
		if(bcRegrUid==null || bcRegrUid.isEmpty()){
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			throw new CmException(msg);
		}
		
		// 리소스기본(WB_BC_B) 테이블 - UPDATE or INSERT
		QueryQueue queryQueue = new QueryQueue();
				
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		WbBcAgntSetupBVo wbBcAgntSetupBVo = new WbBcAgntSetupBVo();
		// 기존 정보 초기화
		wbBcAgntSetupBVo.setRegrUid(userVo.getUserUid());		
		queryQueue.delete(wbBcAgntSetupBVo);
		
		// 신규 정보 저장
		wbBcAgntSetupBVo = new WbBcAgntSetupBVo();
		wbBcAgntSetupBVo.setRegrUid(userVo.getUserUid());	
		wbBcAgntSetupBVo.setBcRegrUid(bcRegrUid);
		queryQueue.insert(wbBcAgntSetupBVo);			
		commonSvc.execute(queryQueue);
		
		model.put("todo", "parent.location.replace('./setAgntAdm.do?menuId=" + menuId+"');");
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		return LayoutUtil.getResultJsp();
	}
	
/** 대리명함 END */	
	
	/** [AJAX] 명함 건수 조회 */
	@RequestMapping(value = {"/wb/getTotalCntAjx","/wb/adm/getTotalCntAjx","/wb/pub/getTotalCntAjx"})
	public String getTotalCntAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String pubBcYn = (String) object.get("pubBcYn");
			if ( pubBcYn == null || pubBcYn.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 폴더ID
			String fldId = (String) object.get("fldId");
						
			/** 공유명함여부 */
			boolean isPub = request.getRequestURI().startsWith("/wb/pub/");
			// 관리자가 공유명함을 선택함
			if(request.getRequestURI().startsWith("/wb/adm/") && pubBcYn != null && !pubBcYn.isEmpty() && "Y".equals(pubBcYn)) isPub = true;
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 조회조건 매핑
			WbBcBVo wbBcBVo = new WbBcBVo();
			VoUtil.bind(request, wbBcBVo);
			
			wbBcBVo.setCompId(userVo.getCompId());
			if(!isPub && !request.getRequestURI().startsWith("/wb/adm/")) wbBcBVo.setRegrUid(userVo.getUserUid());
			wbBcBVo.setPub(isPub); // 공유명함여부
			if(fldId!=null && !fldId.isEmpty()) {
				wbBcBVo.setSchFldTypYn("F");
				wbBcBVo.setSchFldId(fldId);
			}
			model.put("totalCnt", commonSvc.count(wbBcBVo));
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	/** [POPUP] 명함 내보내기 */
	@RequestMapping(value = {"/wb/setBcOutPop", "/wb/adm/setBcOutPop", "/wb/pub/setBcOutPop"})
	public String setBcOutPop(HttpServletRequest request,
			@RequestParam(value = "pubBcYn", required = false) String pubBcYn,
			ModelMap model) throws Exception {
		
		/** 공유명함여부 */
		boolean isPub = request.getRequestURI().startsWith("/wb/pub/");
		/** 관리자 여부 */
		boolean isAdmin = request.getRequestURI().startsWith("/wb/adm/");
		// 관리자가 공유명함을 선택함
		if(isAdmin) isPub = true;
		
		
		// set, list 로 시작하는 경우 처리
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 조회조건 매핑
		WbBcBVo wbBcBVo = new WbBcBVo();
		VoUtil.bind(request, wbBcBVo);
		
		wbBcBVo.setQueryLang(langTypCd);
		if(!isPub && !isAdmin) {
			wbBcBVo.setRegrUid(userVo.getUserUid());
		}else{
			wbBcBVo.setCompId(userVo.getCompId());
		}
		wbBcBVo.setPub(isPub); // 공유명함여부
		
		Integer recodeCount = commonSvc.count(wbBcBVo);
		model.put("recodeCount", recodeCount);
		
		// 개인,공유 선택여부
		model.put("isSelect", isAdmin);
		
		// 폴더 목록 조회
		WbBcFldBVo wbBcFldBVo = new WbBcFldBVo();
		wbBcFldBVo.setCompId(userVo.getCompId());
		if(isAdmin) wbBcFldBVo.setOrderBy("FLD_ID, SORT_ORDR");
		else {
			wbBcFldBVo.setRegrUid(userVo.getUserUid());
			wbBcFldBVo.setOrderBy("BC_FLD_ID, SORT_ORDR");
		}
		wbBcFldBVo.setQueryLang(langTypCd);
		wbBcFldBVo.setPub(isAdmin); // 공유명함여부
		
		@SuppressWarnings("unchecked")
		List<WbBcFldBVo> wbBcFldBVoList = (List<WbBcFldBVo>)commonSvc.queryList(wbBcFldBVo);		
		model.put("wbBcFldBVoList", wbBcFldBVoList);
		
		return LayoutUtil.getJspPath("/wb/setBcOutPop");
	}
	
	/** 명함 (In/Out) */
	@RequestMapping(value = {"/wb/setBcInOut", "/wb/adm/setBcInOut", "/wb/pub/setBcInOut"})
	public String setBcInOut(HttpServletRequest request,
			ModelMap model) throws Exception {

		return LayoutUtil.getJspPath("/wb/setBcInOut");
	}
	
	/** [POPUP] 명함 가져오기 */
	@RequestMapping(value = {"/wb/setBcInPop", "/wb/adm/setBcInPop", "/wb/pub/setBcInPop"})
	public String setBcInPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		/** 관리자 여부 */
		boolean isAdmin = request.getRequestURI().startsWith("/wb/adm/");
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		WbBcFldBVo wbBcFldBVo = new WbBcFldBVo();
		wbBcFldBVo.setCompId(userVo.getCompId());
		if(isAdmin) wbBcFldBVo.setOrderBy("FLD_ID, SORT_ORDR");
		else {
			wbBcFldBVo.setRegrUid(userVo.getUserUid());
			wbBcFldBVo.setOrderBy("BC_FLD_ID, SORT_ORDR");
		}
		wbBcFldBVo.setQueryLang(langTypCd);
		wbBcFldBVo.setPub(isAdmin); // 공유명함여부
		
		@SuppressWarnings("unchecked")
		List<WbBcFldBVo> wbBcFldBVoList = (List<WbBcFldBVo>)commonSvc.queryList(wbBcFldBVo);		
		model.put("wbBcFldBVoList", wbBcFldBVoList);
		
		// 개인,공유 선택여부
		model.put("isSelect", isAdmin);
				
		return LayoutUtil.getJspPath("/wb/setBcInPop");
	}
	
	/** 명함 OUT (CSV 파일 쓰기) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/wb/transBcOut", "/wb/adm/transBcOut", "/wb/pub/transBcOut"})
	public ModelAndView transBcOut(HttpServletRequest request,
			@RequestParam(value = "csvTypCd", required = false) String csvTypCd,
			@RequestParam(value = "bcId", required = false) String bcId,
			@RequestParam(value = "pubBcYn", required = false) String pubBcYn,
			Locale locale,
			ModelMap model) throws Exception {
		CsvWriter csvOutput = null;
		try {
			/** 공유명함여부 */
			boolean isPub = request.getRequestURI().startsWith("/wb/pub/");
			// 관리자가 공유명함을 선택함
			if(request.getRequestURI().startsWith("/wb/adm/") && pubBcYn != null && !pubBcYn.isEmpty() && "Y".equals(pubBcYn)) isPub = true;
			// set, list 로 시작하는 경우 처리
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 조회조건 매핑
			WbBcBVo wbBcBVo = new WbBcBVo();
			VoUtil.bind(request, wbBcBVo);
			wbBcBVo.setQueryLang(langTypCd);
			
			if(!isPub && !request.getRequestURI().startsWith("/wb/adm/")) {
				wbBcBVo.setRegrUid(userVo.getUserUid());
			}else{
				wbBcBVo.setCompId(userVo.getCompId());
			}
			wbBcBVo.setPub(isPub); // 공유명함여부
			
			// 출력할 데이터
			List<WbBcBVo> data = new ArrayList<WbBcBVo>();
			
			if(bcId != null && !bcId.isEmpty()){
				String[] bcIds = bcId.split(",");
				WbBcBVo infoVo = null;
				for(String id : bcIds){
					wbBcBVo.setBcId(id);
					infoVo = (WbBcBVo)commonSvc.queryVo(wbBcBVo);
					data.add(infoVo);
				}
			}else{
				// 폴더ID
				String fldId = ParamUtil.getRequestParam(request, "fldId", false);
				if(fldId!=null && !fldId.isEmpty()){
					if(!"NONE".equals(fldId)){
						// 폴더 기본 테이블
						WbBcFldBVo wbBcFldBVo = new WbBcFldBVo();
						wbBcFldBVo.setCompId(userVo.getCompId());
						wbBcFldBVo.setPub(isPub); // 공유명함여부
						wbBcFldBVo.setBcFldId(fldId);
						if(commonSvc.count(wbBcFldBVo)==0){
							LOGGER.error("[ERROR] - folder is empty!!");
							//cm.msg.noSelectedItem=선택된 "폴더"(이)가 없습니다.
							model.put("message", messageProperties.getMessage("cm.msg.noSelectedItem", new String[]{"#wb.cols.fldNm"}, request));
							ModelAndView mv = new ModelAndView(LayoutUtil.getResultJsp());
							return mv;
						}
					}
					wbBcBVo.setSchFldTypYn("F");
					wbBcBVo.setSchFldId(fldId);
				}
				data = (List<WbBcBVo>)commonSvc.queryList(wbBcBVo);
			}
			
			if(data.size() == 0 ){
				//wb.msg.bcIn.NotList.msg1=내보내기할 명함이 없습니다.
				String msg = messageProperties.getMessage("wb.msg.bcOut.NotList.msg1", request);
				LOGGER.error("data size : 0  msg:"+msg);
				model.put("message", msg);
				ModelAndView mv = new ModelAndView(LayoutUtil.getResultJsp());
				return mv;
			}
			
			//연락처 컬럼
			String [] cntcTypes = {"Work","Home","Mobile","Work Fax","* Home"};
			/** 목록 설정 조회 */
			List<WbBcLstSetupBVo> wbBcLstSetupBVoList = wbBcSvc.getWbBcLstSetupInit(csvTypCd);
						
			String ext = "csv";
			distManager.init();
			
			String tempDir = distManager.getWasCopyBaseDir() + distManager.getDistDir("temp");
			File tempDirFile = new File(tempDir);
			if(!tempDirFile.exists()) {
				tempDirFile.mkdirs();
			}
			String newfileName = StringUtil.getNextHexa()+"."+ext;
			String outputFile = tempDir+"/"+newfileName;
			
			// before we open the file check to see if it already exists
			boolean alreadyExists = new File(outputFile).exists();
			
			// use FileWriter constructor that specifies open for appending
			csvOutput = new CsvWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "EUC-KR"), ',');
			//CsvWriter csvOutput = new CsvWriter(new FileWriter(outputFile, true), ',');
			//Charset cset = Charset.forName("google".equals(csvTypCd) ? "UTF-16LE" : "UTF-8");
			//CsvWriter csvOutput = new CsvWriter(new FileOutputStream(outputFile, true),',',cset);
			
			// if the file didn't already exist then we need to write out the header line
			if (!alreadyExists){
				for(WbBcLstSetupBVo wbBcLstSetupBVo : wbBcLstSetupBVoList){
					csvOutput.write(messageProperties.getMessage(wbBcLstSetupBVo.getMsgId(), locale));
					//csvOutput.write(new String((messageProperties.getMessage(wbBcLstSetupBVo.getMsgId(), locale)).getBytes(Charset.forName(("UTF-8")))));
				}
				csvOutput.endRecord();
			}
			// else assume that the file already has the correct header line
			Map<String, Object> wbBcBInfoMap;
			List<Map<String, Object>> wbBcBMapList = new ArrayList<Map<String, Object>>();
			
			// Map 으로 변환
			for(WbBcBVo storedWbBcBVo : data){
				wbBcBInfoMap = VoUtil.toMap(storedWbBcBVo, null);
				wbBcBMapList.add(wbBcBInfoMap);
			}
			
			// write out a few records
			for(Map<String,Object> storedWbBcBMap : wbBcBMapList){
				for(WbBcLstSetupBVo wbBcLstSetupBVo : wbBcLstSetupBVoList){
					if("google".equals(csvTypCd) && wbBcLstSetupBVo.getAtrbId().indexOf("cntcType") > -1 ){
						csvOutput.write(cntcTypes[Integer.parseInt(wbBcLstSetupBVo.getAtrbId().replaceAll("[^0-9]", ""))-1]);
					}else{
						csvOutput.write((String)storedWbBcBMap.get(wbBcLstSetupBVo.getAtrbId()));
					}
					
				}
				csvOutput.endRecord();
			}
			
			File file = new File(outputFile);
			ModelAndView mv = new ModelAndView("fileDownloadView");
			mv.addObject("downloadFile", file);
			
			Format sdf = FastDateFormat.getInstance( "yyyyMMddhhmm", Locale.getDefault());
			Calendar cal = Calendar.getInstance();
			//파일명 생성
			String fileNm = csvTypCd+(csvTypCd == null || "".equals(csvTypCd) ? "" : "_")+sdf.format(cal.getTime())+"."+ext;
			
			mv.addObject("realFileName", fileNm);
			
			return mv;
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		} finally {
			if (csvOutput != null) try { csvOutput.close(); } catch (Exception e) {}
		}

		return null;
	}
	
	/** 명함 In (CSV 파일 읽기) */
	@RequestMapping(value = {"/wb/transBcIn", "/wb/adm/transBcIn", "/wb/pub/transBcIn"})
	public String transBcIn(HttpServletRequest request,
			Locale locale,
			ModelMap model) throws Exception {
		
		UploadHandler uploadHandler = null;
		try{
			uploadHandler = uploadManager.createHandler(request, "temp", "wb");
			uploadHandler.upload();//업로드 파일 정보
			Map<String, String> param = uploadHandler.getParamMap();//파라미터 정보
			
			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			String pubBcYn = ParamUtil.getRequestParam(request, "pubBcYn", false);
			
			/** 공유명함여부 */
			boolean isPub = request.getRequestURI().startsWith("/wb/pub/");
			// 관리자가 공유명함을 선택함
			if(request.getRequestURI().startsWith("/wb/adm/") && pubBcYn != null && !pubBcYn.isEmpty() && "Y".equals(pubBcYn)) isPub = true;
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			// 폴더ID
			String fldId = ParamUtil.getRequestParam(request, "fldId", false);
			if(fldId!=null && !fldId.isEmpty()){
				// 폴더 기본 테이블
				WbBcFldBVo wbBcFldBVo = new WbBcFldBVo();
				wbBcFldBVo.setCompId(userVo.getCompId());
				wbBcFldBVo.setPub(isPub); // 공유명함여부
				wbBcFldBVo.setBcFldId(fldId);
				if(commonSvc.count(wbBcFldBVo)==0){
					//cm.msg.noSelectedItem=선택된 "폴더"(이)가 없습니다.
					LOGGER.error("[ERROR] - folder is empty!!");
					model.put("message", messageProperties.getMessage("cm.msg.noSelectedItem", new String[]{"#wb.cols.fldNm"}, request));
					model.put("todo", "parent.loading('uploadForm', false)");
					return LayoutUtil.getResultJsp();
				}
			}
			
			String csvTypCd = (String)param.get("csvTypCd");
			// 임시 경로
			String tempPath = uploadHandler.getAbsolutePath("file").replace('\\', '/');
			
			List<WbBcBVo> data = new ArrayList<WbBcBVo>();
			WbBcBVo wbBcBVo = null;
		
			CsvReader products = new CsvReader(new InputStreamReader(new FileInputStream(tempPath),("google".equals(csvTypCd) ? "UTF-16LE" : "MS949")));
			
			products.readHeaders();
			
			//연락처 객체명 세팅
			String cntcArrs = "compPhon,homePhon,mbno,email,cntcType,phonValue";
			
			String msgNm = "";
			
			//csv의 내용을 담기 위한 Map
			Map<String,Object> paramMap = null; 
			
			//연락처,이메일 배열
			List<WbBcCntcDVo> wbBcCntcDVoList = new ArrayList<WbBcCntcDVo>();
			WbBcCntcDVo wbBcCntcDVo = null;
			String msg = "";
			String[] atrbArrs = new String[3];
			
			//List<Map<String,String[]>> cntcMapList = new ArrayList<Map<String,String[]>>();
			//구글 csv 일경우 연락처를 담기 위한 객체
			Map<String,String[]> cntcArrsMap = new HashMap<String,String[]>();
			String prefixCntcKey = "";
			String[] cntcStrings = new String [3];
			String headerNm = "";
			while (products.readRecord()){
				paramMap = new HashMap<String,Object>();
				wbBcBVo = new WbBcBVo();
				wbBcCntcDVoList = new ArrayList<WbBcCntcDVo>();
				for(int i=0;i<products.getHeaders().length;i++){
					if("google".equals(csvTypCd) && i == 0){
						headerNm = "Name";
					}else{
						headerNm = products.getHeader(i);
					}
					
					
					atrbArrs = wbBcSvc.getWbBcLstAtrdInfo(request, headerNm, atrbArrs , csvTypCd);
					
					if(atrbArrs != null){
						msgNm = "google".equals(csvTypCd) && i == 0 ? products.getHeader(i) : atrbArrs[1];
						
						/*if("bcNm".equals(atrbArrs[0]) && ( products.get(msgNm) == null || "".equals(products.get(msgNm)) )){
							//wb.msg.bcIn.NotBcNm.msg1=CSV 내용중에 필수값인 명함명이 빠져있습니다.\n확인후 재업로드 해주세요.
							msg = messageProperties.getMessage("wb.msg.bcIn.NotBcNm.msg1",locale);
							break;
						}*/
						
						if(products.get(msgNm) != null && !"".equals(products.get(msgNm)) ){
							//데이터 길이 체크
							/*if(Integer.parseInt(atrbArrs[2]) < products.get(msgNm).getBytes().length){
								//wb.msg.bcIn.NotDataLength.msg1=이(가) 데이터 길이에 맞지 않습니다.
								msg = messageProperties.getMessage("wb.msg.bcIn.NotDataLength.msg1",new String[]{msgNm} ,locale);
								break;
							}*/
							
							//연락처 일경우
							if(cntcArrs.contains(atrbArrs[0])){
								if("google".equals(csvTypCd)){
									prefixCntcKey = msgNm.replaceAll(" - Type| - Value","");
									if(!cntcArrsMap.containsKey(prefixCntcKey)){
										cntcArrsMap.put(prefixCntcKey, new String [3]);
									}
									
									cntcStrings = (String[])cntcArrsMap.get(prefixCntcKey);
									if(msgNm.indexOf("Type") > -1 ){//Phone or Email Type
										cntcStrings[0] = wbBcSvc.getWbBcCsvCntcType(products.get(msgNm));
										cntcStrings[1] = atrbArrs[0];
									}else if(msgNm.indexOf("Value") > -1 ){										
										cntcStrings[2] = products.get(msgNm);
									}
									
									if(cntcStrings[0] != null && cntcStrings[1] != null && cntcStrings[2] != null ){
										/*if(chkMaxByte(atrbArrs[2], cntcStrings[2])){
											//wb.msg.bcIn.NotDataLength.msg1=이(가) 데이터 길이에 맞지 않습니다.
											msg = messageProperties.getMessage("wb.msg.bcIn.NotDataLength.msg1",new String[]{msgNm} ,locale);
											break;
										}*/
										if("fno".equals(cntcStrings[0])){
											paramMap.put(cntcStrings[0], cntcStrings[2]);
										}else{
											wbBcCntcDVo = new WbBcCntcDVo();
											wbBcCntcDVo.setCntcCont(cntcStrings[2]);
											wbBcCntcDVo.setCntcTypCd(cntcStrings[0]);
											wbBcCntcDVo.setCntcClsCd("email".equals(cntcStrings[0]) ? "EMAIL" : "CNTC");
											wbBcCntcDVoList.add(wbBcCntcDVo);
										}
									}
								}else{
									/*if(chkMaxByte(atrbArrs[2], products.get(msgNm))){
										//wb.msg.bcIn.NotDataLength.msg1=이(가) 데이터 길이에 맞지 않습니다.
										msg = messageProperties.getMessage("wb.msg.bcIn.NotDataLength.msg1",new String[]{msgNm} ,locale);
										break;
									}*/
									wbBcCntcDVo = new WbBcCntcDVo();
									wbBcCntcDVo.setCntcCont(products.get(msgNm));
									wbBcCntcDVo.setCntcTypCd(atrbArrs[0]);
									wbBcCntcDVo.setCntcClsCd("email".equals(atrbArrs[0]) ? "EMAIL" : "CNTC");
									wbBcCntcDVoList.add(wbBcCntcDVo);
								}
							}else{
								if(StringUtil.chkMaxByte(atrbArrs[2], StringUtil.getBytes(products.get(msgNm)))){
									String subMsg = "(actual: "+StringUtil.getBytes(products.get(msgNm))+", maximum: "+atrbArrs[2]+")";
									//LOGGER.error("[ERROR] transBcIn - value too large!!\t"+msgNm+subMsg);
									//wb.msg.bcIn.NotDataLength.msg1={0} 이(가) 데이터 길이에 맞지 않습니다. (maximum:{1})
									msg = messageProperties.getMessage("wb.msg.bcIn.NotDataLength.msg1", new String[]{msgNm, subMsg} ,locale);
									break;
								}
								paramMap.put(atrbArrs[0], products.get(msgNm));
							}
						}
					}
					
					//필수값 체크
					if(i == products.getHeaders().length-1 && !paramMap.containsKey("bcNm")){
						//wb.msg.bcIn.NotBcNm.msg1=CSV 내용중에 필수값인 명함명이 빠져있습니다.\n확인후 재업로드 해주세요.
						msg = messageProperties.getMessage("wb.msg.bcIn.NotBcNm.msg1",locale);
						break;
					}
					
				}
				wbBcBVo.setPub(isPub); // 공유명함여부
				VoUtil.fromMap(wbBcBVo, paramMap);//csv 파일을 읽어서 Map으로 변환후 Vo 객체에 세팅함
				wbBcBVo.setWbBcCntcDVo(wbBcCntcDVoList);//연락처를 세팅
				
				// perform program logic here
				data.add(wbBcBVo);
			}
			products.close();
			
			if(!"".equals(msg)){
				LOGGER.error("error msg:"+msg);
				model.put("message", msg);
				return LayoutUtil.getResultJsp();
			}
			
			if(data.size() == 0 ){
				//wb.msg.bcIn.NotList.msg1=가져오기할 명함이 없습니다.
				msg = messageProperties.getMessage("wb.msg.bcIn.NotList.msg1", request);
				LOGGER.error("data size : 0  msg:"+msg);
				model.put("message", msg);
				model.put("todo", "parent.loading('uploadForm', false)");
				return LayoutUtil.getResultJsp();
			}
			if(data.size() > 0){
				QueryQueue queryQueue = new QueryQueue();
				
				for(WbBcBVo storedWbBcBVo : data){
					if(storedWbBcBVo.getBcNm() != null && !"".equals(storedWbBcBVo.getBcNm())){
						if(fldId!=null && !fldId.isEmpty()) storedWbBcBVo.setFldId(fldId); // 폴더ID
						storedWbBcBVo.setCompId(userVo.getCompId());
						storedWbBcBVo.setPub(isPub); // 공유명함여부
						wbBcSvc.saveCsvBc(queryQueue , storedWbBcBVo , userVo);
					}
				}
				
				commonSvc.execute(queryQueue);
			}
			model.put("todo", "parent.setInOut('in');");
			msg = messageProperties.getMessage("cm.msg.save.success", request);
			model.put("message", msg);
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
			model.put("todo", "parent.loading('uploadForm', false)");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("todo", "parent.loading('uploadForm', false)");
		} catch (IOException e) {
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("todo", "parent.loading('uploadForm', false)");
		}finally {
			uploadHandler.removeTempDir();
			//if(fis!=null) fis.close();
	        //if(isr!=null) isr.close();
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** 명함 In (CSV 파일 읽기) 임시 */
	@RequestMapping(value = "/wb/transBcInTemp")
	public String transBcInTemp(HttpServletRequest request,
			Locale locale,
			ModelMap model) throws Exception {
		
		UploadHandler uploadHandler = null;
		try{
			uploadHandler = uploadManager.createHandler(request, "temp", "wb");
			uploadHandler.upload();//업로드 파일 정보
			// 임시 경로
			String tempPath = uploadHandler.getAbsolutePath("file").replace('\\', '/');
			
			
			List<WbBcBVo> data = new ArrayList<WbBcBVo>();
			WbBcBVo wbBcBVo = null;
			
			CsvReader products = new CsvReader(new InputStreamReader(new FileInputStream(tempPath),"MS949"));
			
			products.readHeaders();
			String[] headers = products.getHeaders();
			String clsMsg = "";
			//System.out.println("headers.length : "+headers.length);
			String[] cntcs = {"compPhon","homePhon","mbno","email"};
			Integer[] cntcNum = {0,0,0,0};//연락처의 중복 갯수
			for(int i=0;i<headers.length;i++){//csv 의 header를 조회해서 연락처,이메일의 header명을 재정의한다.(header명 중복되지 않게)
				for(int j=0;j<cntcs.length;j++){
					clsMsg = messageProperties.getMessage("wb.cols.", locale);
					if(clsMsg.equals(headers[i])){
						cntcNum[j] += 1;
						headers[i] = headers[i]+cntcNum[j];
						continue;
					}
				}
			}
			//재정의한 header 명을 세팅한다.
			products.setHeaders(headers);
			/*for(String header : headers){
				System.out.println("header : "+header);
			}*/
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			//기본 목록설정항목 조회
			List<WbBcLstSetupBVo> wbBcLstSetupBVoList = wbBcSvc.setWbBcLstSetupInit(null, "");
			
			//연락처 객체명 세팅
			String cntcArrs = "compPhon,homePhon,mbno,email";
			String msgNm = "";
			
			//csv의 내용을 담기 위한 Map
			Map<String,Object> paramMap = null; 
			
			//연락처,이메일 배열
			List<WbBcCntcDVo> wbBcCntcDVoList = null;
			WbBcCntcDVo wbBcCntcDVo = null;
			String msg = "";
			int cntcIndex = 0;
			while (products.readRecord()){
				paramMap = new HashMap<String,Object>();
				wbBcBVo = new WbBcBVo();
				wbBcCntcDVoList = new ArrayList<WbBcCntcDVo>();
				for(WbBcLstSetupBVo wbBcLstSetupBVo : wbBcLstSetupBVoList){
					msgNm = messageProperties.getMessage(wbBcLstSetupBVo.getMsgId(), locale);
					
					//필수값 체크
					if("bcNm".equals(wbBcLstSetupBVo.getAtrbId()) && ( products.get(msgNm) == null || "".equals(products.get(msgNm)) )){
						//wb.msg.bcIn.NotBcNm.msg1=CSV 내용중에 필수값인 명함명이 빠져있습니다.\n확인후 재업로드 해주세요.
						msg = messageProperties.getMessage("wb.msg.bcIn.NotBcNm.msg1",locale);
						break;
					}
					
					//연락처 일경우
					if(cntcArrs.contains(wbBcLstSetupBVo.getAtrbId())){
						for(int i=0;i<cntcs.length;i++){
							if(cntcs[i].equals(wbBcLstSetupBVo.getAtrbId())){
								cntcIndex = i;
								break;
							}
						}
						for(int k=1;k<=cntcNum[cntcIndex];k++){
							if(products.get(msgNm+k) != null && !"".equals(products.get(msgNm+k)) ){
								if(!"email".equals(wbBcLstSetupBVo.getAtrbId()) && !wbBcSvc.isCheckTellNo(products.get(msgNm+k))){
									//wb.msg.bcIn.NotDataTyp.msg1={0} 이(가) 데이터 형식에 맞지 않습니다.
									msg = messageProperties.getMessage("wb.msg.bcIn.NotDataTyp.msg1",new String[]{msgNm} ,locale);
									break;
								}
								wbBcCntcDVo = new WbBcCntcDVo();
								wbBcCntcDVo.setCntcCont(products.get(msgNm+k));
								wbBcCntcDVo.setCntcTypCd(wbBcLstSetupBVo.getAtrbId());
								wbBcCntcDVo.setCntcClsCd("email".equals(wbBcLstSetupBVo.getAtrbId()) ? "EMAIL" : "CNTC");
								wbBcCntcDVoList.add(wbBcCntcDVo);
							}
						}
					}else{//연락처를 제외한 데이터 세팅
						if(products.get(msgNm) != null && !"".equals(products.get(msgNm)) ){
							if(wbBcLstSetupBVo.getVaLen() < products.get(msgNm).getBytes().length){
								//wb.msg.bcIn.NotDataLength.msg1=이(가) 데이터 길이에 맞지 않습니다.
								msg = messageProperties.getMessage("wb.msg.bcIn.NotDataLength.msg1",new String[]{msgNm} ,locale);
								break;
							}
							paramMap.put(wbBcLstSetupBVo.getAtrbId(), products.get(msgNm));
						}
					}
					
				}
				VoUtil.fromMap(wbBcBVo, paramMap);//csv 파일을 읽어서 Map으로 변환후 Vo 객체에 세팅함
				wbBcBVo.setWbBcCntcDVo(wbBcCntcDVoList);//연락처를 세팅
				// perform program logic here
				data.add(wbBcBVo);
			}
	
			products.close();
			
			if(!"".equals(msg)){
				LOGGER.error("error msg:"+msg);
				model.put("message", msg);
				return LayoutUtil.getResultJsp();
			}
			
			if(data.size() == 0 ){
				//wb.msg.bcIn.NotList.msg1=가져오기할 명함이 없습니다.
				msg = messageProperties.getMessage("wb.msg.bcIn.NotList.msg1", request);
				LOGGER.error("data size : 0  msg:"+msg);
				model.put("message", msg);
				return LayoutUtil.getResultJsp();
			}
			
			if(data.size() > 0){
				QueryQueue queryQueue = new QueryQueue();
				
				for(WbBcBVo storedWbBcBVo : data){
					if(storedWbBcBVo.getBcNm() != null && !"".equals(storedWbBcBVo.getBcNm())){
						storedWbBcBVo.setCompId(userVo.getCompId());
						wbBcSvc.saveCsvBc(queryQueue , storedWbBcBVo , userVo);
					}
				}
				
				commonSvc.execute(queryQueue);
			}
			model.put("todo", "parent.setInOut('in');");
			msg = messageProperties.getMessage("cm.msg.save.success", request);
			model.put("message", msg);
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			uploadHandler.removeTempDir();
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** 국가별 GMT 목록 (추후 일정관리로 소스 이동 필요함) - 경로 변경 '/wb/' == 삭제 */
	/** GMT설정 */
	@RequestMapping(value = "/wb/wc/setGmt")
	public String setBcFld(HttpServletRequest request,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/wb/wc/setGmt");
	}
	
	/** [FRAME]트리 조회 */
	@RequestMapping(value = "/wb/wc/listGmtTreeFrm")
	public String listGmtTreeFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		PtCdBVo ptCdBVo = new PtCdBVo();
		ptCdBVo.setQueryLang(langTypCd);
		ptCdBVo.setFldYn("Y");
		ptCdBVo.setClsCd("GMT_CD");
		ptCdBVo.setOrderBy("CLS_CD, SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<PtCdBVo> ptCdBVoList = (List<PtCdBVo>)commonSvc.queryList(ptCdBVo);
		model.put("ptCdBVoList", ptCdBVoList);
		
		return LayoutUtil.getJspPath("/wb/wc/listGmtTreeFrm");
	}
	
	/** [FRAME]목록 조회 */
	@RequestMapping(value = "/wb/wc/listGmtFrm")
	public String listGmtFrm(HttpServletRequest request,
			@Parameter(name="clsCd", required=false) String clsCd,
			ModelMap model) throws Exception {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		PtCdBVo ptCdBVo = new PtCdBVo();
		ptCdBVo.setQueryLang(langTypCd);
		ptCdBVo.setFldYn("N");
		if(clsCd == null || clsCd.isEmpty() || "GMT_CD".equals(clsCd) || "ROOT".equals(clsCd)) ptCdBVo.setWhereSqllet("AND CLS_CD IN('GMT_AF_CD','GMT_AS_CD','GMT_AU_CD','GMT_CA_CD','GMT_EU_CD','GMT_ME_CD','GMT_NA_CD','GMT_RU_CD')");
		else ptCdBVo.setClsCd(clsCd);
		ptCdBVo.setOrderBy("CLS_CD, SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<PtCdBVo> ptCdBVoList = (List<PtCdBVo>)commonSvc.queryList(ptCdBVo);
		model.put("ptCdBVoList", ptCdBVoList);
		
		return LayoutUtil.getJspPath("/wb/wc/listGmtFrm");
	}
	
	
	/** GMT 저장 */
	@RequestMapping(value = "/wb/wc/transGmt")
	public String transGmt(HttpServletRequest request,
			@Parameter(name="menuId", required=false) String menuId,
			ModelMap model) throws Exception {
		
		try {
			String clsCd = ParamUtil.getRequestParam(request, "clsCd", true);
			String cd = ParamUtil.getRequestParam(request, "cd", true);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			System.out.println("설정사용자명 : "+userVo.getUserNm());
			System.out.println("구분코드(clsCd) : "+clsCd);
			System.out.println("코드값(cd) : "+cd);
			
			//저장 로직 구현?(세션 또는 db 및 쿠키 등 .. 관련 로직 구현			
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.location.replace('./setGmt.do?menuId=" + menuId + "&clsCd="+clsCd+"&cd="+cd+"');");
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	
	/** 엑셀파일 다운로드 (사용자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/wb/excelDownLoad", "/wb/pub/excelDownLoad", "/wb/adm/excelDownLoad"}, method = RequestMethod.POST)
	public ModelAndView excelDownLoad(HttpServletRequest request,
			@Parameter(name="schOpenTypCd", required=false) String schOpenTypCd,
			@Parameter(name="schBcRegrUid", required=false) String schBcRegrUid,
			@Parameter(name="ext", required=false) String ext,
			ModelMap model) throws Exception {
		
		try {
			
			/** 공유명함여부 */
			boolean isPub = request.getRequestURI().startsWith("/wb/pub/");
			
			String listPage = ParamUtil.getRequestParam(request, "listPage", false);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			String compId = null;
			// 시스템 관리자 여부
			//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
			// 시스템 관리자가 아닌 경우에는 - 자기 회사만 검색
			//if(!isSysAdmin){
				compId = userVo.getCompId();
			//}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			/** 목록 설정 조회 */
			wbBcSvc.setWbBcSetupInit(request, userVo , model);
			
			List<WbBcUserLstSetupRVo> wbBcUserLstSetupRVoList = (List<WbBcUserLstSetupRVo>)model.get("wbBcUserLstSetupRVoList");
			
			// 조회조건 매핑
			WbBcBVo wbBcBVo = new WbBcBVo();
			VoUtil.bind(request, wbBcBVo);
			wbBcBVo.setQueryLang(langTypCd);
			if(wbBcBVo.getSchFldId()!=null && "ROOT".equals(wbBcBVo.getSchFldId()))
				wbBcBVo.setSchFldId(null);
			
			if(compId != null ) wbBcBVo.setCompId(compId);
			
			boolean flag = false;
			if("listBc".equals(listPage)){
				wbBcBVo.setRegrUid(userVo.getUserUid());
				wbBcBVo.setCompId(null);
				flag = true;
			}else if("listOpenBc".equals(listPage)){
				wbBcBVo.setSchUserUid(userVo.getUserUid());//사용자UID
				wbBcBVo.setSchCompId(userVo.getCompId());//사용자 회사코드
				wbBcBVo.setSchDeptId(userVo.getDeptId());//사용자 부서코드
				wbBcBVo.setQueryLang(langTypCd);
				String[] schOpenTypCds = schOpenTypCd == null ? new String[]{"allPubl"} : schOpenTypCd.split(",");
				if(schOpenTypCds != null && schOpenTypCds.length > 0 ){
					wbBcBVo.setSchOpenTypCds(schOpenTypCds);
					flag = true;
				}
			}else if("listAgntBc".equals(listPage)){
				// 대리명함 조회UID가 있을경우 명함 등록자UID로 세팅한다.
				schBcRegrUid = wbCmSvc.getSchBcRegrUid(schBcRegrUid, userVo, langTypCd, model);
				if(schBcRegrUid != null && !schBcRegrUid.isEmpty() ) {
					wbBcBVo.setRegrUid(schBcRegrUid);
					wbBcBVo.setCompId(null);
					flag = true;
				}
				// 대리명함 조회UID가 없을경우 대리관리자 목록이 없는 것이므로 명함 조회를 하지 않는다.
			}else if("listAllBc".equals(listPage)){
				flag = true;
			}else if("setBcBumk".equals(listPage)){
				flag = true;
			}
			
			// 파일 목록조회
			ModelAndView mv = new ModelAndView("excelDownloadView");
			if(!flag && !isPub){
				mv = new ModelAndView("cm/result/commonResult");
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				mv.addObject("message",messageProperties.getMessage("cm.msg.notValidCall", request));
				return mv;
			}
			
			List<WbBcBVo> list = new ArrayList<WbBcBVo>();
			if("setBcBumk".equals(listPage)){
				WbBcBumkRVo wbBcBumkRVo = new WbBcBumkRVo();
				wbBcBumkRVo.setRegrUid(userVo.getUserUid());
				wbBcBumkRVo.setQueryLang(langTypCd);
				wbBcBumkRVo.setSchBumkYn("Y");
				
				// 즐겨찾기 등록된 명함 조회
				list = (List<WbBcBVo>)commonSvc.queryList(wbBcBumkRVo);
			}else{
				wbBcBVo.setPub(isPub); // 공유명함여부
				//목록 조회
				list = (List<WbBcBVo>)commonSvc.queryList(wbBcBVo);
			}
			
			if(list.size() == 0 ){
				mv = new ModelAndView("cm/result/commonResult");
				mv.addObject("message", messageProperties.getMessage("em.msg.noExcelData", request));
				return mv;
			}
			//컬럼명
			List<String> colNames = new ArrayList<String>();
			//데이터
			List<String> colValue = null;
			Map<String,Object> colValues = new HashMap<String,Object>();
			Map<String,Object> tempMap = null;
			for(int i=0;i<list.size();i++){
				tempMap = VoUtil.toMap(list.get(i), null);
				colValue = new ArrayList<String>();
				for(WbBcUserLstSetupRVo wbBcUserLstSetupRVo : wbBcUserLstSetupRVoList){
					colValue.add((String)tempMap.get(wbBcUserLstSetupRVo.getAtrbId()));
					if( i == 0 )	colNames.add(messageProperties.getMessage(wbBcUserLstSetupRVo.getMsgId(), request)); //컬럼명 세팅
				}
				colValues.put("col"+i,colValue);//데이터 세팅
			}
			
			//시트명 세팅
			String sheetNm = "wb.jsp.";
			if("listBc".equals(listPage)) { sheetNm += "listBc";}
			else if("listOpenBc".equals(listPage)) { sheetNm += "listOpenBc";}
			else if("listAgntBc".equals(listPage)) { sheetNm += "listBc.agnt";}
			else if("setBcBumk".equals(listPage)) { sheetNm += "setBcBumk";}
			else if("listPubBc".equals(listPage)) { sheetNm += "listPublBc";}
			else { sheetNm += "listBc";}
			sheetNm+=".title";
			
			mv.addObject("sheetName", messageProperties.getMessage(sheetNm, request));//시트명
			mv.addObject("colNames", colNames);//컬럼명
			mv.addObject("colValues", colValues);//데이터
			mv.addObject("fileName", messageProperties.getMessage(sheetNm, request));//파일명
			mv.addObject("ext", ext == null ? "xlsx" : ext);//파일 확장자(없으면 xls)
			
			return mv;
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
			return mv;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
		}
		return null;
	}
	
	/** [AJAX] 사용자 목록을 명함에 추가 */
	@RequestMapping(value = {"/wb/transBcUserListAjx", "/wb/adm/transBcUserListAjx", "/wb/pub/transBcUserListAjx"})
	public String transBcUserListAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			
			JSONArray jsonArray = (JSONArray)object.get("addList");
			
			if(jsonArray == null || jsonArray.size() == 0 ){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				LOGGER.error("jsonArray size : 0  msg:"+msg);
				model.put("message", msg);
				return LayoutUtil.getResultJsp();
			}
			
			List<String> userUidList = new ArrayList<String>();
			// jsonArray를 String[]에 담는다.
			for(int i=0;i<jsonArray.size();i++){
				userUidList.add((String)jsonArray.get(i));
			}
			if(userUidList.size()==0){
				//cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			
			// 폴더ID
			String fldId = (String)object.get("fldId");
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 저장
			wbBcSvc.saveBcUserList(request, queryQueue, userUidList, fldId);
			
			if(queryQueue.size()>0){
				// 일괄실행
				commonSvc.execute(queryQueue);
				
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
				model.put("result", "ok");
			}else{
				// cm.msg.nodata.toSave=저장할 데이터가 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.nodata.toSave", request));
			}
			
		} catch(CmException e){
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
}
