package com.innobiz.orange.mobile.wb.ctrl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import org.springframework.web.servlet.ModelAndView;

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.wb.svc.WbBcFileSvc;
import com.innobiz.orange.web.wb.svc.WbBcSvc;
import com.innobiz.orange.web.wb.svc.WbCmSvc;
import com.innobiz.orange.web.wb.vo.WbBcAgntAdmBVo;
import com.innobiz.orange.web.wb.vo.WbBcBVo;
import com.innobiz.orange.web.wb.vo.WbBcBumkRVo;
import com.innobiz.orange.web.wb.vo.WbBcClnsCVo;
import com.innobiz.orange.web.wb.vo.WbBcFileDVo;
import com.innobiz.orange.web.wb.vo.WbBcFldBVo;
import com.innobiz.orange.web.wb.vo.WbBcImgDVo;
import com.innobiz.orange.web.wb.vo.WbBcMetngDVo;

/** 명함관리 */
@Controller
public class MoWbBcCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(MoWbBcCtrl.class);
	
	/** 공통 서비스 */
	@Autowired
	private WbBcSvc wbBcSvc;
	
	/** 공통 서비스 */
	@Autowired
	private WbCmSvc wbCmSvc;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 첨부파일 서비스 */
	@Autowired
	private WbBcFileSvc wbBcFileSvc;
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 첨부설정 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 개인/공개,대리 명함 목록 조회 */
	@RequestMapping(value = {"/wb/listBc","/wb/listOpenBc","/wb/listAgntBc", "/wb/pub/listPubBc"})
	public String listBc(HttpServletRequest request,
			@Parameter(name="schOpenTypCd", required=false) String schOpenTypCd,
			@Parameter(name="schBcRegrUid", required=false) String schBcRegrUid,
			ModelMap model) throws Exception {
		
		/** 공유명함여부 */
		boolean isPub = request.getRequestURI().startsWith("/wb/pub/");
		
		// 요청경로 세팅
		String path = wbCmSvc.getRequestPath(request, model , null);

		// set, list 로 시작하는 경우 처리
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String compId = userVo.getCompId();
		// 시스템 관리자 여부
	/*	boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		// 시스템 관리자가 아닌 경우에는 - 자기 회사만 검색
		if(!isSysAdmin){
			compId = userVo.getCompId();
		}*/
		
		// 조회조건 매핑
		WbBcBVo wbBcBVo = new WbBcBVo();
		VoUtil.bind(request, wbBcBVo);
		wbBcBVo.setQueryLang(langTypCd);
		
		if(compId != null ) wbBcBVo.setCompId(compId);
		
		boolean flag = false;
		if("listBc".equals(path)){//개인명함
			wbBcBVo.setRegrUid(userVo.getUserUid());
			wbBcBVo.setCompId(null);
			flag = true;
		}else if("listOpenBc".equals(path)){//공개명함
			wbBcBVo.setSchUserUid(userVo.getUserUid());//사용자UID
			wbBcBVo.setSchCompId(userVo.getCompId());//사용자 회사코드
			wbBcBVo.setSchDeptId(userVo.getDeptId());//사용자 부서코드
			wbBcBVo.setQueryLang(langTypCd);
			String[] schOpenTypCds = schOpenTypCd == null ? new String[]{"allPubl"} : schOpenTypCd.split(",");
			if(schOpenTypCds != null && schOpenTypCds.length > 0 ){
				wbBcBVo.setSchOpenTypCds(schOpenTypCds);
				flag = true;
			}
			model.put("schOpenTypCds", schOpenTypCds);
		}else if("listAgntBc".equals(path)){//대리명함
			// 대리명함 조회UID가 있을경우 명함 등록자UID로 세팅한다.
			schBcRegrUid = wbCmSvc.getSchBcRegrUid(schBcRegrUid, userVo, langTypCd, model);
			if(schBcRegrUid != null && !schBcRegrUid.isEmpty() ) {
				wbBcBVo.setRegrUid(schBcRegrUid);
				wbBcBVo.setCompId(null);
				model.put("schBcRegrUid", schBcRegrUid);
				flag = true;
			}
			// 대리명함 조회UID가 없을경우 대리관리자 목록이 없는 것이므로 명함 조회를 하지 않는다.
		}
		wbBcBVo.setPub(isPub); // 공유명함여부
		if(flag || isPub){
			Map<String,Object> rsltMap = wbBcSvc.getWbBcMapList(request , wbBcBVo );
			model.put("recodeCount", rsltMap.get("recodeCount"));
			model.put("wbBcBMapList", rsltMap.get("wbBcBMapList"));
		}
		
		/** 목록 설정 조회 */
		wbBcSvc.setWbBcSetupInit(request, userVo , model);

		model.put("paramsForList", ParamUtil.getQueryString(request, "typ","schBcRegrUid", "noCache"));
		
		return MoLayoutUtil.getJspPath("/wb/listBc");
	}
	
	/** 개인 명함 상세보기 */
	@RequestMapping(value = {"/wb/viewBc","/wb/viewOpenBc","/wb/viewAgntBc","/wb/viewBumkBc", "/wb/pub/viewPubBc"})
	public String viewBc(HttpServletRequest request,
			@RequestParam(value = "bcId", required = true) String bcId,
			ModelMap model) throws Exception {
		try{
			// 요청경로 세팅
			String path = wbCmSvc.getRequestPath(request, model , null);
	
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
			if("viewBc".equals(path)){
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
			if("viewAgntBc".equals(path)){
				//String schBcRegrUid = ParamUtil.getRequestParam(request, "schBcRegrUid", true);
				/** 사용자의 명함 대리 관리자 정보 */
				Map<String,Object> agntInfoMap = wbCmSvc.getAgntInfoMap(request ,wbBcBVo.getRegrUid() , userVo.getUserUid() , null );
				
				WbBcAgntAdmBVo wbBcAgntAdmBVo = (WbBcAgntAdmBVo)agntInfoMap.get("wbBcAgntAdmBVo");
				model.put("wbBcAgntAdmBVo", wbBcAgntAdmBVo);
				model.put("schBcRegrUid", wbBcAgntAdmBVo.getRegrUid());
			}
			
			model.put("wbBcBVo", wbBcBVo);
			
			model.put("paramsForList", ParamUtil.getQueryString(request, "bcId","schBcRegrUid", "noCache"));
			
			if("viewBumkBc".equals(path))
				model.put("listPage", "setBcBumk");
		
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
			return MoLayoutUtil.getResultJsp();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
			return MoLayoutUtil.getResultJsp();
		}
			
		return MoLayoutUtil.getJspPath("/wb/viewBc");
	}
	
	/** 즐겨찾는 명함 목록 조회 */
	@RequestMapping(value = "/wb/setBcBumk")
	public String listBcBumk(HttpServletRequest request,
			ModelMap model) throws Exception {

		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		WbBcBumkRVo wbBcBumkRVo = new WbBcBumkRVo();
		VoUtil.bind(request, wbBcBumkRVo);
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
		
		model.put("paramsForList", ParamUtil.getQueryString(request, "bcId","schBcRegrUid", "noCache"));
	
		return MoLayoutUtil.getJspPath("/wb/setBcBumk");
	}
	
	/** 개인 명함 즐겨찾기 추가 및 제거 */
	@RequestMapping(value = "/wb/transBumkBc")
	public String transBumkBc(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String bumkYn	 = (String) object.get("bumkYn");
			String bcId = (String) object.get("bcId");
			
			if (bcId == null || bumkYn == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

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
			model.put("result", "ok");
		} catch (Exception e) {
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	
	/** 개인 명함 삭제 */
	@RequestMapping(value = {"/wb/transBcDel","/wb/transAgntBcDel", "/wb/pub/transPubBcDel"})
	public String transBcDel(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String bcId = (String) object.get("bcId");
			String schBcRegrUid = (String) object.get("schBcRegrUid");
			
			if (bcId == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 요청경로 세팅
			String path = wbCmSvc.getRequestPath(request, model , null);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			WbBcBVo wbBcBVo = new WbBcBVo();
			
			if("transBcDel".equals(path)){
				wbBcBVo.setModrUid(userVo.getUserUid());
				//wbBcBVo.setCompId(userVo.getCompId());
			}else if("transAgntBcDel".equals(path)){
				/** 사용자의 명함 대리 관리자 정보 */
				Map<String,Object> agntInfoMap = wbCmSvc.getAgntInfoMap(request , schBcRegrUid , userVo.getUserUid() , "RW" );
				//대리관리자일 경우 대리권한 부여자의 회사ID를 조회한다.
				wbBcBVo.setModrUid(schBcRegrUid);
				wbBcBVo.setCompId((String)agntInfoMap.get("compId"));
			}
			//삭제 id가 있을경우
			if(bcId != null && !bcId.isEmpty() ){
				wbBcBVo.setDelList(bcId.split(","));
				wbBcSvc.deleteBc(queryQueue , wbBcBVo);
				commonSvc.execute(queryQueue);
			}
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	/** 첨부파일 다운로드 (사용자) */
	@RequestMapping(value = {"/wb/downFile","/wb/preview/downFile"})
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
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
			return mv;
		} catch (Exception e) {
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
		}
		return null;
	}
	
	/** 개인 명함 등록 수정 화면 출력 */
	@RequestMapping(value = {"/wb/setBc","/wb/setAgntBc", "/wb/pub/setPubBc"})
	public String setBc(HttpServletRequest request,
			@RequestParam(value = "bcId", required = false) String bcId,
			@RequestParam(value = "toBcId", required = false) String toBcId,
			ModelMap model) throws Exception {
		
		try{
			// 요청경로 세팅
			String path = wbCmSvc.getRequestPath(request, model , null);
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			String schBcRegrUid = "";
			//대리명함일시
			if("setAgntBc".equals(path)){
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
				wbBcBVo.setCompId(userVo.getCompId());
				wbBcBVo.setBcId(toBcId != null && !toBcId.isEmpty() ? toBcId: bcId);
				if("setBc".equals(path) && ( toBcId == null || toBcId.isEmpty() )){//개인명함
					wbBcBVo.setRegrUid(userVo.getUserUid());
					wbBcBVo.setCompId(null);
				}else if("setAgntBc".equals(path)){//대리명함
					// 대리명함 여부를 판단해 등록자UID를 세팅한다.
					wbBcBVo.setRegrUid(schBcRegrUid);
					wbBcBVo.setCompId(null);
				}
				wbBcBVo.setPub(isPub); // 공유명함여부
				wbBcBVo = wbBcSvc.getWbBcInfo(request, wbBcBVo);
				
				if(bcId != null && !"".equals(bcId)){
					wbBcBVo.setBcId(bcId);//수정이면서 동명이인의 명함 정보일 경우 해당 VO로 조회후 bcId를 원래대로 변경
				}
			}else{
				wbBcBVo.setWbBcCntcDVo(wbBcSvc.setCntcList(0 , "CNTC"));// 연락처목록 초기화
				wbBcBVo.setWbBcEmailDVo(wbBcSvc.setCntcList(1 , "EMAIL"));// 이메일목록 초기화
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
					
			/** 친밀도 목록 조회 */
			if(!isPub){
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
			model.put("paramsForList", ParamUtil.getQueryString(request, "bcId","schBcRegrUid"));
			model.put("nonPageParams", ParamUtil.getQueryString(request, "menuId","schBcRegrUid","pageNo"));
		
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
			return MoLayoutUtil.getResultJsp();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
			return MoLayoutUtil.getResultJsp();
		}
			
		return MoLayoutUtil.getJspPath("/wb/setBc");
	}
	
	/** 개인 명함 등록 수정 (저장) */
	@RequestMapping(value = {"/wb/transBcPost","/wb/transAgntBcPost", "/wb/pub/transPubBcPost"})
	public String transBc(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		UploadHandler uploadHandler = null;
		try {
			// Multipart 파일 업로드
			uploadHandler = wbBcFileSvc.upload(request);

			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			// 요청경로 세팅
			String path = wbCmSvc.getRequestPath(request, model , null);
	
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			String viewPage = ParamUtil.getRequestParam(request, "viewPage", true);
			
			String bcId = ParamUtil.getRequestParam(request, "bcId", false);
			String delList = ParamUtil.getRequestParam(request, "delList", false);
			
			// 세션의 언어코드
			//String langTypCd = LoginSession.getLangTypCd(request);
						
			QueryQueue queryQueue = new QueryQueue();
			
			if (bcId == null || bcId.isEmpty()) {// 등록일 경우 목록 화면으로 이동
				model.put("todo", "$m.nav.curr(event, '" + listPage + "');");
			} else {// 수정일 경우 상세보기 화면으로 이동
				model.put("todo", "$m.nav.curr(event, '" + viewPage + "');");
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			WbBcBVo wbBcBVo = new WbBcBVo();
			VoUtil.bind(request, wbBcBVo);
			
			/** 공유명함여부 */
			boolean isPub = request.getRequestURI().startsWith("/wb/pub/");
			
			if(path.startsWith("transBcPost")){
				wbBcBVo.setCompId(null);
			}else if(path.startsWith("transAgntBcPost")){
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
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			model.put("exception", e);
		}finally {
			if (uploadHandler != null) try { uploadHandler.removeTempDir(); } catch (CmException ignored) {}
		}

		return MoLayoutUtil.getResultJsp();
	}
	
	/** [AJAX] 동명이인 중복 체크 */
	@RequestMapping(value = {"/wb/findPwsmCheck", "/wb/pub/findPwsmCheck"})
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
			String result = "";
			if(recodeCount.intValue() == 0 ){
				model.put("message", messageProperties.getMessage("wb.msg.noDupBcNm", request));
			}else{
				result = "popup";
			}
			model.put("result", result);
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			model.put("exception", e);
		}
		
		return JsonUtil.returnJson(model);
	}
	

	/** [FRAME] 개인,공개 명함 목록 조회 */
	@RequestMapping(value = "/wb/findBcSub")
	public String findBcFrm(HttpServletRequest request,
			@Parameter(name="detlViewType", required=false) String detlViewType,
			@Parameter(name="schOpenTypCd", required=false) String schOpenTypCd,			
			@Parameter(name="schBcRegrUid", required=false) String schBcRegrUid,
			@Parameter(name="pagingYn", required=false) String pagingYn,
			Locale locale,
			ModelMap model) throws Exception {
		if(detlViewType==null) detlViewType = "bcList";
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
		}
		if(flag){
			if(pagingYn != null && "N".equals(pagingYn)){//페이징여부
				@SuppressWarnings("unchecked")
				List<WbBcBVo> WbBcBVoList = (List<WbBcBVo>)commonSvc.queryList(wbBcBVo);
				model.put("wbBcBMapList", WbBcBVoList);
			}else{
				Map<String,Object> rsltMap = wbBcSvc.getWbBcMapList(request , wbBcBVo );
				model.put("recodeCount", rsltMap.get("recodeCount"));
				model.put("wbBcBMapList", rsltMap.get("wbBcBMapList"));
			}
		}
		
		model.put("UI_TITLE", messageProperties.getMessage("wb.jsp.findBcPop.title", locale));
		return MoLayoutUtil.getJspPath("/wb/findBcSub");
	}
	
	/** [팝업] 동명이인 조회 */
	@RequestMapping(value = {"/wb/findBcPwsmSub", "/wb/pub/findBcPwsmSub"})
	public String findBcPwsmFrm(HttpServletRequest request,
			@Parameter(name="bcId", required=false) String bcId,
			@Parameter(name="schCat", required=false) String schCat,
			@Parameter(name="schWord", required=false) String schWord,
			@Parameter(name="schBcRegrUid", required=false) String schBcRegrUid,
			Locale locale,
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
		if(!isPub) {
			wbBcBVo.setRegrUid((schBcRegrUid != null && !"".equals(schBcRegrUid)) ? schBcRegrUid : userVo.getUserUid());
			wbBcBVo.setCompId(null);
		}
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
		
		model.put("UI_TITLE", messageProperties.getMessage("wb.jsp.findBcPwsmPop.title", locale));
		return MoLayoutUtil.getJspPath("/wb/findBcPwsmSub");
	}
	
	/** 폴더 목록 조회 팝업 */
	@RequestMapping(value = {"/wb/findBcFldSub", "/wb/pub/findBcFldSub"})
	public String findBcFldPop(HttpServletRequest request,
			@Parameter(name="schBcRegrUid", required=false) String schBcRegrUid,
			Locale locale,
			ModelMap model) throws Exception {
		
		/** 공유명함여부 */
		boolean isPub = request.getRequestURI().startsWith("/wb/pub/");
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		WbBcFldBVo wbBcFldBVo = new WbBcFldBVo();
		if(isPub) wbBcFldBVo.setOrderBy("FLD_ID, SORT_ORDR");
		else wbBcFldBVo.setOrderBy("BC_FLD_ID, SORT_ORDR");
		wbBcFldBVo.setQueryLang(langTypCd);
		
		wbBcFldBVo.setPub(isPub); // 공유명함여부
		
		if(schBcRegrUid != null && !"".equals(schBcRegrUid)){
			// 사용자기본(OR_USER_B) 테이블
			OrUserBVo orUserBVo = new OrUserBVo();
			orUserBVo.setUserUid(schBcRegrUid);
			orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
			//대리관리자일 경우 대리권한 부여자의 회사ID를 조회한다.
			wbBcFldBVo.setCompId(orUserBVo.getCompId());
			wbBcFldBVo.setRegrUid(schBcRegrUid);
			
		}else{
			//wbBcFldBVo.setCompId(userVo.getCompId());
			wbBcFldBVo.setRegrUid(userVo.getUserUid());
		}
		
		List<WbBcFldBVo> wbBcFldBVoList = new ArrayList<WbBcFldBVo>();
		// 미분류 등록
		WbBcFldBVo noneVo = new WbBcFldBVo();
		noneVo.setBcFldId("NONE");
		noneVo.setAbvFldId("ROOT");
		noneVo.setFldNm(messageProperties.getMessage("dm.cols.emptyCls", request)); // 미분류
		noneVo.setSortOrdr("1");
		wbBcFldBVoList.add(noneVo);
		@SuppressWarnings("unchecked")
		List<WbBcFldBVo> tmpList = (List<WbBcFldBVo>)commonSvc.queryList(wbBcFldBVo);
		if(tmpList!=null) wbBcFldBVoList.addAll(tmpList);
		//List<WbBcFldBVo> wbBcFldBVoList = (List<WbBcFldBVo>)commonSvc.queryList(wbBcFldBVo);
		model.put("wbBcFldBVoList", wbBcFldBVoList);
		
		// js - include 옵션
		model.put("JS_OPTS", new String[]{"tree"});
				
		model.put("UI_TITLE", messageProperties.getMessage("wb.btn.fldChoi", locale));
		return MoLayoutUtil.getJspPath("/wb/findBcFldSub");
	}
	
	/** [AJAX] 개인,공개 명함 목록 조회 */
	@RequestMapping(value = "/wb/findBc")
	public String findBc(HttpServletRequest request,
			ModelMap model) throws Exception {
		return MoLayoutUtil.getJspPath("/wb/findBc", "empty");
	}
	
    /** [AJAX] 개인,공개 명함 목록 조회 */
	@RequestMapping(value = "/wb/findBcAjx")
	public String findBcAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		// set, list 로 시작하는 경우 처리
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		try {
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String detlViewType = (String)jsonObject.get("detlViewType");
			//String schOpenTypCd = (String)jsonObject.get("schOpenTypCd");
			String schBcRegrUid = (String)jsonObject.get("schBcRegrUid");
			String pagingYn = (String)jsonObject.get("pagingYn");
			String schCat = (String)jsonObject.get("schCat");
			String schWord = (String)jsonObject.get("schWord");
			
			String selection = (String)jsonObject.get("selection");
			if(selection!=null && !selection.isEmpty()){
				model.put("selection", selection);//for UI - radio/checkbox
			}
			// 조회조건 매핑
			WbBcBVo wbBcBVo = new WbBcBVo();
			if(schCat != null && !schCat.isEmpty())wbBcBVo.setSchCat(schCat);
			if(schWord != null && !schWord.isEmpty())wbBcBVo.setSchWord(schWord);
			//VoUtil.bind(request, wbBcBVo);
			wbBcBVo.setQueryLang(langTypCd);
			wbBcBVo.setCompId(userVo.getCompId());
			
			boolean flag = false;
			if("bcList".equals(detlViewType)){
				String listPage = ParamUtil.getRequestParam(request, "listPage", false);
				if(listPage == null || !"listAllMetng".equals(listPage)){
					wbBcBVo.setRegrUid((schBcRegrUid != null && !"".equals(schBcRegrUid)) ? schBcRegrUid : userVo.getUserUid());
					wbBcBVo.setCompId(null);
				}
				flag = true;
			}else if("bcOpenList".equals(detlViewType)){
				wbBcBVo.setSchUserUid(userVo.getUserUid());//사용자UID
				wbBcBVo.setSchCompId(userVo.getCompId());//사용자 회사코드
				wbBcBVo.setSchDeptId(userVo.getDeptId());//사용자 부서코드
				String[] schOpenTypCds =new String[]{"allPubl","deptPubl","apntrPubl"};
				wbBcBVo.setSchOpenTypCds(schOpenTypCds);
				model.put("schOpenTypCds", schOpenTypCds);
				
				// 원본 , 복사원본 , 메인설정 상태 조건절 추가
				wbBcBVo.setWhereSqllet("AND MAIN_SETUP_YN IN ('O', 'C','Y')");
				flag = true;
			}
			if(flag){
				if(pagingYn != null && "N".equals(pagingYn)){
					@SuppressWarnings("unchecked")
					List<WbBcBVo> WbBcBVoList = (List<WbBcBVo>)commonSvc.queryList(wbBcBVo);
					model.put("wbBcBMapList", WbBcBVoList);
				}else{
					Map<String,Object> rsltMap = wbBcSvc.getWbBcMapList(request , wbBcBVo );
					model.put("recodeCount", rsltMap.get("recodeCount"));
					model.put("wbBcBMapList", rsltMap.get("wbBcBMapList"));
				}
				return MoLayoutUtil.getJspPath("/wb/findBcAjx");
			}else{
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String message = messageProperties.getMessage("cm.msg.notValidCall", request);
				LOGGER.error("Search user by [no orgId] and [no userNm]"+" - "+message);
				return JsonUtil.returnJson(model, message);
			}
		}catch (Exception e) {
			return JsonUtil.returnJson(model, e.getMessage());
		}
	}
	
	/** [팝업] 이미지 변경 */
	@RequestMapping(value = {"/wb/setImagePop"})
	public String setImagePop(HttpServletRequest request,
			ModelMap model) throws Exception {
		model.put("params", ParamUtil.getQueryString(request));
		return MoLayoutUtil.getJspPath("/wb/setImagePop");
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


	/** [히든프레임] 사진 업로드 */
	@RequestMapping(value = {"/wb/transImagePost", "/wb/adm/transImagePost", "/wb/pub/transImagePost"})
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
				model.put("todo", "$m.nav.getWin().setImageReview('"+tempPath+"');");
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
				model.put("todo", "$m.nav.getWin().setImage('"+wbBcImgDVo.getImgPath()+"', "+wbBcImgDVo.getImgWdth()+", "+wbBcImgDVo.getImgHght()+");");
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
	
		return MoLayoutUtil.getResultJsp();
	}
	
	/** [AJAX] 사진 삭제 */
	@RequestMapping(value = {"/wb/transImageDelAjx", "/wb/pub/transImageDelAjx"})
	public String transImageDelAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		// set, list 로 시작하는 경우 처리
		
		try {
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String bcId = (String)jsonObject.get("bcId");
			if ( bcId == null || bcId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			
			// 명함 이미지 상세
			WbBcImgDVo wbBcImgDVo = new WbBcImgDVo();
			wbBcImgDVo.setBcId(bcId);
			queryQueue.delete(wbBcImgDVo);
			commonSvc.execute(queryQueue);
			
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
}
