package com.innobiz.orange.web.wb.admCtrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONArray;
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
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.wb.svc.WbBcRescSvc;
import com.innobiz.orange.web.wb.svc.WbBcSvc;
import com.innobiz.orange.web.wb.vo.WbBcBVo;
import com.innobiz.orange.web.wb.vo.WbBcLstSetupBVo;
import com.innobiz.orange.web.wb.vo.WbMetngClsCdBVo;
import com.innobiz.orange.web.wb.vo.WmRescBVo;

/** 관련미팅 */
@Controller
public class WbBcAdmCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WbBcAdmCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
//	/** 포탈 공통 서비스 */
//	@Autowired
//	private PtCmSvc ptCmSvc;
	
	/** 공통 서비스 */
	@Autowired
	private WbBcRescSvc wbBcRescSvc;
	
//	/** 관련미팅 관리 */
//	@Autowired
//	private WbMetngSvc wbMetngSvc;
	
	/** 공통 서비스 */
	@Autowired
	private WbBcSvc wbBcSvc;
	
//	/** 공통 서비스 */
//	@Autowired
//	private WbCmSvc wbCmSvc;
	
//	/** [명함]첨부파일 서비스 */
//	@Autowired
//	private WbBcFileSvc wbBcFileSvc;
	
//	/** [미팅]첨부파일 서비스 */
//	@Autowired
//	private WbBcMetngFileSvc wbBcMetngFileSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 메인 명함관리 목록 조회 */
	@RequestMapping(value = "/wb/adm/listBcMainFrm")
	public String listBcMainFrm(HttpServletRequest request,
			@RequestParam(value = "mainListType", required = true) String mainListType,
			ModelMap model) throws Exception {
		// set, list 로 시작하는 경우 처리
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 조회조건 매핑
		WbBcBVo wbBcBVo = new WbBcBVo();
		VoUtil.bind(request, wbBcBVo);
		wbBcBVo.setQueryLang(langTypCd);
		// 시스템 관리자 여부
		//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		// 시스템 관리자가 아닌 경우에는 - 자기 회사만 검색
		//if(!isSysAdmin){
			wbBcBVo.setCompId(userVo.getCompId());
		//}
				
		wbBcBVo.setPublTypCd("allPubl");
		if("result".equals(mainListType)){
			wbBcBVo.setMainSetupYn("Y");
		}else if("all".equals(mainListType)){
			wbBcBVo.setWhereSqllet("AND MAIN_SETUP_YN IN ('O', 'C','Y')");
		}else{
			wbBcBVo.setWhereSqllet("AND MAIN_SETUP_YN IN ('O', 'C')");
		}
		
		//wbBcBVo.setOrderBy("BC_ID");
		Map<String,Object> rsltMap = wbBcSvc.getWbBcMapList(request , wbBcBVo );
		model.put("recodeCount", rsltMap.get("recodeCount"));
		//model.put("wbBcBMapList", rsltMap.get("wbBcBMapList"));
		model.put("wbBcBMapList", rsltMap.get("wbBcBMapList"));
		
		
		return LayoutUtil.getJspPath("/wb/adm/listBcMainFrm");		
	}
	
	
	/** 메인 명함관리 목록 조회 상세 */
	@RequestMapping(value = "/wb/adm/listBcSubFrm")
	public String listBcSubFrm(HttpServletRequest request,
			@RequestParam(value = "bcId", required = true) String bcId,
			@RequestParam(value="originalBcId", required=false) String originalBcId,
			ModelMap model) throws Exception {
		
		try{
			
			// set, list 로 시작하는 경우 처리
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 조회조건 매핑
			WbBcBVo searchVO = new WbBcBVo();
			VoUtil.bind(request, searchVO);
			searchVO.setQueryLang(langTypCd);
			
			// 시스템 관리자 여부
			//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
			// 시스템 관리자가 아닌 경우에는 - 자기 회사만 검색
			//if(!isSysAdmin){
				searchVO.setCompId(userVo.getCompId());
			//}
			
			List<WbBcBVo> wbBcBVoList = new ArrayList<WbBcBVo>();
			
			//상세정보
			WbBcBVo wbBcBVo = wbBcSvc.getWbBcInfo(request, searchVO);
			model.put("wbBcBVo", wbBcBVo);
			
			if(originalBcId != null && !originalBcId.isEmpty()){
				searchVO = new WbBcBVo();
				searchVO.setOriginalBcId(null);
				searchVO.setBcId(originalBcId);
				wbBcBVoList.add(wbBcSvc.getWbBcInfo(request, searchVO));
			}else{
				wbBcBVoList.add(wbBcBVo);
			}
			
			searchVO = new WbBcBVo();
			searchVO.setQueryLang(langTypCd);
			searchVO.setCompId(userVo.getCompId());
			if(originalBcId != null && !originalBcId.isEmpty()){
				searchVO.setOriginalBcId(originalBcId);
			}else{
				searchVO.setOriginalBcId(bcId);
			}
			
			//searchVO.setWhereSqllet("AND (BC_ID = '"+bcId+"' OR ORIGINAL_BC_ID = '"+bcId+"') ");
			wbBcBVo.setOrderBy("BC_ID");
			
			@SuppressWarnings("unchecked")
			List<WbBcBVo> historyList = (List<WbBcBVo>)commonSvc.queryList(searchVO);
			
			wbBcBVoList.addAll(historyList);
			model.put("wbBcBVoList", wbBcBVoList);
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
			return LayoutUtil.getResultJsp();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
			return LayoutUtil.getResultJsp();
		}
		
		return LayoutUtil.getJspPath("/wb/adm/listBcSubFrm");		
	}
	
	/** [AJAX] 메인 명함관리 설정 */
	@RequestMapping(value = "/wb/adm/transBcMainAjx")
	public String transBcMainAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		
		try {
			// 리소스기본(WB_BC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			// 세션의 사용자 정보
			//UserVo userVo = LoginSession.getUser(request);
						
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String originalBcId = (String)jsonObject.get("originalBcId");
			String bcId = (String)jsonObject.get("bcId");
			
			if(bcId == null || bcId.isEmpty() ){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				LOGGER.error("originalBcId or bcId is null  msg:"+msg);
				model.put("message", msg);
				return LayoutUtil.getResultJsp();
			}
			WbBcBVo wbBcBVo = new WbBcBVo();
			
			if(originalBcId == null || "".equals(originalBcId)){
				wbBcBVo.setSchOriginalBcId(bcId);
			}else{
				wbBcBVo.setSchOriginalBcId(originalBcId);
			}
			
			wbBcBVo.setMainSetupYn("N");
			queryQueue.update(wbBcBVo);
			
			if(originalBcId != null || !"".equals(originalBcId)){
				wbBcBVo = new WbBcBVo();
				wbBcBVo.setBcId(originalBcId);
				wbBcBVo.setMainSetupYn("N");
				queryQueue.update(wbBcBVo);
			}
			
			wbBcBVo = new WbBcBVo();
			//wbBcBVo.setSchOriginalBcId(null);
			wbBcBVo.setBcId(bcId);
			wbBcBVo.setMainSetupYn("Y");
			queryQueue.update(wbBcBVo);
			
			commonSvc.execute(queryQueue);
			
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
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
	
	/** 중복 지인관리 */
	@RequestMapping(value = "/wb/adm/setDupFrnd")
	public String setDupFrnd(HttpServletRequest request,
			ModelMap model) throws Exception {
				
		return LayoutUtil.getJspPath("/wb/adm/setDupFrnd");
	}
	
	/** 중복 지인관리 목록 조회 */
	@RequestMapping(value = "/wb/adm/listDuplMainFrm")
	public String listDuplMainFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		// set, list 로 시작하는 경우 처리
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 조회조건 매핑
		WbBcBVo wbBcBVo = new WbBcBVo();
		VoUtil.bind(request, wbBcBVo);
		wbBcBVo.setQueryLang(langTypCd);
		
		// 시스템 관리자 여부
		//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		// 시스템 관리자가 아닌 경우에는 - 자기 회사만 검색
		//if(!isSysAdmin){
			wbBcBVo.setCompId(userVo.getCompId());
		//}
				
		wbBcBVo.setPublTypCd("allPubl");
		wbBcBVo.setOrderBy("BC_NM");
		
		@SuppressWarnings("unchecked")
		List<WbBcBVo> wbBcBVoList = (List<WbBcBVo>)commonSvc.queryList(wbBcBVo);
		Integer recodeCount = commonSvc.count(wbBcBVo);
		PersonalUtil.setPaging(request, wbBcBVo, recodeCount);
		
		//wbBcBVo.setOrderBy("BC_ID");
		//Map<String,Object> rsltMap = wbBcSvc.getWbBcMapList(request , wbBcBVo );
		model.put("recodeCount", recodeCount);
		//model.put("wbBcBMapList", rsltMap.get("wbBcBMapList"));
		
		// 사본 갯수 세팅
		if(wbBcBVoList.size() > 0 ){
			WbBcBVo tempWbBcBVo = null;
			for(WbBcBVo storedWbBcBVo : wbBcBVoList ){
				tempWbBcBVo = new WbBcBVo();
				tempWbBcBVo.setCompId(userVo.getCompId());
				tempWbBcBVo.setDuplicateBcId(storedWbBcBVo.getBcId());
				storedWbBcBVo.setDuplicateBcCnt(commonSvc.count(tempWbBcBVo));
			}
		}
		model.put("wbBcBMapList", wbBcBVoList);
		
		return LayoutUtil.getJspPath("/wb/adm/listDuplMainFrm");		
	}
	
	
	/** 중복 지인관리 목록 조회 상세 */
	@RequestMapping(value = "/wb/adm/listDuplSubFrm")
	public String listDuplSubFrm(HttpServletRequest request,
			@RequestParam(value = "bcId", required = true) String bcId,
			@RequestParam(value="originalBcId", required=false) String originalBcId,
			ModelMap model) throws Exception {
		try{
			// set, list 로 시작하는 경우 처리
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 조회조건 매핑
			WbBcBVo searchVO = new WbBcBVo();
			VoUtil.bind(request, searchVO);
			searchVO.setQueryLang(langTypCd);
			
			// 시스템 관리자 여부
			//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
			// 시스템 관리자가 아닌 경우에는 - 자기 회사만 검색
			//if(!isSysAdmin){
				searchVO.setCompId(userVo.getCompId());
			//}
			
			List<WbBcBVo> wbBcBVoList = new ArrayList<WbBcBVo>();
			
			//상세정보
			WbBcBVo wbBcBVo = wbBcSvc.getWbBcInfo(request, searchVO);
			model.put("wbBcBVo", wbBcBVo);
			
			searchVO = new WbBcBVo();
			searchVO.setQueryLang(langTypCd);
			searchVO.setCompId(userVo.getCompId());
			searchVO = new WbBcBVo();
			searchVO.setSchCat("bcNm");
			searchVO.setSchWord(wbBcBVo.getBcNm());
			
			wbBcBVo.setOrderBy("BC_NM");
			
			@SuppressWarnings("unchecked")
			List<WbBcBVo> historyList = (List<WbBcBVo>)commonSvc.queryList(searchVO);
			
			wbBcBVoList.addAll(historyList);
			model.put("wbBcBVoList", wbBcBVoList);
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
			return LayoutUtil.getResultJsp();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
			return LayoutUtil.getResultJsp();
		}
		
		return LayoutUtil.getJspPath("/wb/adm/listDuplSubFrm");		
	}
	
	/** [AJAX] 중복 지인관리 설정 */
	@RequestMapping(value = "/wb/adm/transDuplMainAjx")
	public String transDuplMainAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		
		try {
			// 리소스기본(WB_BC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String originalBcId = (String)jsonObject.get("originalBcId");
			JSONArray bcIds = (JSONArray)jsonObject.get("bcIds");//사본 대상 명함
			
			if(originalBcId == null || originalBcId.isEmpty() ){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				LOGGER.error("originalBcId or bcId is null  msg:"+msg);
				model.put("message", msg);
				return LayoutUtil.getResultJsp();
			}
			
			String compId = null;
			// 시스템 관리자 여부
			//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
			// 시스템 관리자가 아닌 경우에는 - 자기 회사만 검색
			//if(!isSysAdmin){
				compId = userVo.getCompId();
			//}
			
			if(bcIds.size() > 0 && bcIds.size() > 0){
				// 명함 기본(WB_BC_B) 테이블
				WbBcBVo wbBcBVo = null;
				for(int i=0;i<bcIds.size();i++){
					if(bcIds.get(i) == null ) continue;
					wbBcBVo = new WbBcBVo();
					if(compId != null){
						wbBcBVo.setCompId(compId);
					}
					
					wbBcBVo.setBcId((String)bcIds.get(i));
					wbBcBVo.setDuplicateBcId(originalBcId);
					wbBcBVo.setMainSetupYn("N");
					queryQueue.update(wbBcBVo);
				}
				
				wbBcBVo = new WbBcBVo();
				if(compId != null){
					wbBcBVo.setCompId(compId);
				}
				wbBcBVo.setBcId(originalBcId);
				wbBcBVo.setDuplicateBcId("");
				wbBcBVo.setMainSetupYn("Y");
				queryQueue.update(wbBcBVo);
			}
			
			commonSvc.execute(queryQueue);
			
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
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
	
	/** [AJAX] 중복 지인관리 설정 초기화 */
	@RequestMapping(value = "/wb/adm/transDuplMainResetAjx")
	public String transDuplMainResetAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		
		try {
			// 리소스기본(WB_BC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String originalBcId = (String)jsonObject.get("originalBcId");
			JSONArray bcIds = (JSONArray)jsonObject.get("bcIds");//사본 대상 명함
			
			if(originalBcId == null || originalBcId.isEmpty() ){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				LOGGER.error("originalBcId or bcId is null  msg:"+msg);
				model.put("message", msg);
				return LayoutUtil.getResultJsp();
			}
			
			String compId = null;
			// 시스템 관리자 여부
			//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
			// 시스템 관리자가 아닌 경우에는 - 자기 회사만 검색
			//if(!isSysAdmin){
				compId = userVo.getCompId();
			//}
			
			if(bcIds.size() > 0 && bcIds.size() > 0){
				// 명함 기본(WB_BC_B) 테이블
				WbBcBVo wbBcBVo = null;
				for(int i=0;i<bcIds.size();i++){
					if(bcIds.get(i) == null ) continue;
					wbBcBVo = new WbBcBVo();
					if(compId != null){
						wbBcBVo.setCompId(compId);
					}
					
					wbBcBVo.setBcId((String)bcIds.get(i));
					wbBcBVo.setDuplicateBcId("");
					wbBcBVo.setResetYn("Y");//초기화여부
					queryQueue.update(wbBcBVo);
				}
			}
			commonSvc.execute(queryQueue);
			
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
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
	
	/** 분류관리 목록 조회 */
	@RequestMapping(value = "/wb/adm/listMetngCls")
	public String listMetngCls(HttpServletRequest request,
			ModelMap model) throws Exception {
			
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String compId = null;
		// 시스템 관리자 여부
		//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		// 시스템 관리자가 아닌 경우에는 - 자기 회사만 검색
		//if(!isSysAdmin){
			compId = userVo.getCompId();
		//}
		
		//분류 조회조건
		WbMetngClsCdBVo wbMetngClsCdBVo = new WbMetngClsCdBVo();
		VoUtil.bind(request, wbMetngClsCdBVo);
		wbMetngClsCdBVo.setQueryLang(langTypCd);
		if(compId != null){ wbMetngClsCdBVo.setCompId(compId); }//회사ID
		// 코드 목록 조회
		Integer recodeCount = commonSvc.count(wbMetngClsCdBVo);
		PersonalUtil.setPaging(request, wbMetngClsCdBVo, recodeCount);
		
		@SuppressWarnings("unchecked")
		//목록 조회
		List<WbMetngClsCdBVo> wbMetngClsCdBVoList = (List<WbMetngClsCdBVo>)commonSvc.queryList(wbMetngClsCdBVo);
		model.put("wbMetngClsCdBVoList", wbMetngClsCdBVoList);
		model.put("recodeCount", recodeCount);
			
		return LayoutUtil.getJspPath("/wb/adm/listMetngCls");
	}
	
	/** [POPUP] 분류관리  등록 수정 화면 출력 */
	@RequestMapping(value = "/wb/adm/setMetngClsPop")
	public String setMetngClsPop(HttpServletRequest request,
			@RequestParam(value = "rescId", required = false) String rescId,
			ModelMap model) throws Exception {
			
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String compId = null;
		// 시스템 관리자 여부
		//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		// 시스템 관리자가 아닌 경우에는 - 자기 회사만 검색
		//if(!isSysAdmin){
			compId = userVo.getCompId();
		//}
		
		WbMetngClsCdBVo wbMetngClsCdBVo = new WbMetngClsCdBVo();
		if(compId != null){ wbMetngClsCdBVo.setCompId(compId); }//회사ID
		wbMetngClsCdBVo.setRescId(rescId);
		wbMetngClsCdBVo.setQueryLang(langTypCd);
		// 코드 목록 조회
		
		// 수정인 경우
		if (rescId != null && !rescId.isEmpty()) {
			wbMetngClsCdBVo = (WbMetngClsCdBVo) commonSvc.queryVo(wbMetngClsCdBVo);
			if (wbMetngClsCdBVo.getRescId() != null) {
				// 리소스기본(BA_RESC_B) 테이블 - 조회, 모델에 추가
				wbBcRescSvc.queryRescBVo(wbMetngClsCdBVo.getRescId(), model);
			}
		}
		model.put("wbMetngClsCdBVo", wbMetngClsCdBVo);	
		return LayoutUtil.getJspPath("/wb/adm/setMetngClsPop");
	}
	
	/** 분류관리  저장 (관리자) */
	@RequestMapping(value = "/wb/adm/transMetngCls")
	public String transMetngCls(HttpServletRequest request,
			@RequestParam(value = "menuId", required = false) String menuId,
			@RequestParam(value = "rescId", required = false) String rescId,
			ModelMap model) {

		try {
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);

			// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			WmRescBVo wmRescBVo = wbBcRescSvc.collectBaRescBVo(request, "", queryQueue);
			if (wmRescBVo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			// 테이블관리(BA_TBL_B) 테이블 - BIND
			WbMetngClsCdBVo wbMetngClsCdBVo = new WbMetngClsCdBVo();
			VoUtil.bind(request, wbMetngClsCdBVo);
			wbMetngClsCdBVo.setCompId(userVo.getCompId());//회사ID
			
			// 리소스 조회 후 리소스ID와 리소스명 세팅
			wbMetngClsCdBVo.setRescId(wmRescBVo.getRescId());
			wbMetngClsCdBVo.setCdVa(wmRescBVo.getRescVa());
			
			if (rescId == null || rescId.isEmpty()) {
				// 테이블 등록
				// 수정자, 수정일시
				wbMetngClsCdBVo.setRegrUid(userVo.getUserUid());
				wbMetngClsCdBVo.setRegDt("sysdate");
				queryQueue.insert(wbMetngClsCdBVo);
			} else {
				// 수정자, 수정일시
				wbMetngClsCdBVo.setModrUid(userVo.getUserUid());
				wbMetngClsCdBVo.setModDt("sysdate");
				// 테이블 수정
				queryQueue.update(wbMetngClsCdBVo);
			}
			commonSvc.execute(queryQueue);
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.location.replace('./listMetngCls.do?menuId=" + menuId+"');");

		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** 분류관리  삭제 (관리자) */
	@RequestMapping(value = "/wb/adm/transMetngClsDel")
	public String transMetngClsDel(HttpServletRequest request,
			@RequestParam(value = "menuId", required = false) String menuId,
			@RequestParam(value = "rescId", required = true) String rescId,
			ModelMap model) {

		try {
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);

			// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			
			// 리소스 삭제
			WmRescBVo wmRescBVo = new WmRescBVo();
			wmRescBVo.setCompId(userVo.getCompId());//회사ID
			wmRescBVo.setRescId(rescId);
			queryQueue.delete(wmRescBVo);
			
			// 분류코드 삭제
			WbMetngClsCdBVo wbMetngClsCdBVo = new WbMetngClsCdBVo();
			wbMetngClsCdBVo.setCompId(userVo.getCompId());//회사ID
			wbMetngClsCdBVo.setRescId(rescId);
			queryQueue.delete(wbMetngClsCdBVo);
			//회사ID 삽입 필요
			
			commonSvc.execute(queryQueue);
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.location.replace('./listMetngCls.do?menuId=" + menuId+"');");

//		} catch (CmException e) {
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	
	/** 검색조건 등록 수정 화면 출력 */
	@RequestMapping(value = "/wb/adm/setSrchCond")
	public String setSrchCond(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		List<WbBcLstSetupBVo> wbBcLstSetupBVoList = new ArrayList<WbBcLstSetupBVo>();
		//기본 목록설정항목 조회[선택할 항목]
		model.put("wbBcLstSetupBVoList", wbBcSvc.setWbBcLstSetupInit(wbBcLstSetupBVoList , null));
		
		//사용자[관리자] 목록설정항목 조회[선택된 항목]
		WbBcLstSetupBVo wbBcLstSetupBVo = new WbBcLstSetupBVo();	
		wbBcLstSetupBVo.setCompId(userVo.getCompId());//회사ID
		@SuppressWarnings("unchecked")
		List<WbBcLstSetupBVo> wbBcUserLstSetupRVoList = (List<WbBcLstSetupBVo>)commonSvc.queryList(wbBcLstSetupBVo);
		// UI 구성용 - 빈 VO 하나 더함
		if(wbBcUserLstSetupRVoList == null) wbBcUserLstSetupRVoList = new ArrayList<WbBcLstSetupBVo>();
		wbBcUserLstSetupRVoList.add(new WbBcLstSetupBVo());
		model.put("wbBcUserLstSetupRVoList", wbBcUserLstSetupRVoList);
				
		return LayoutUtil.getJspPath("/wb/adm/setSrchCond");
	}
	
	/** 검색조건 등록 수정 (저장) */
	@RequestMapping(value = "/wb/adm/transSrchCond")
	public String transSrchCond(HttpServletRequest request,
			@Parameter(name="menuId", required=false) String menuId,
			Locale locale,
			ModelMap model) throws Exception {
		try {
			// 리소스기본(WB_BC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			//관리자 목록설정항목 삭제
			WbBcLstSetupBVo wbBcLstSetupBVo = new WbBcLstSetupBVo();	
			wbBcLstSetupBVo.setCompId(userVo.getCompId());//회사ID
			
			// 목록설정 초기화
			queryQueue.delete(wbBcLstSetupBVo);
			
			Integer sortOrdr = 1;
			// atrbId를 기준으로 배열 정보 VO에 세팅
			@SuppressWarnings("unchecked")
			List<WbBcLstSetupBVo> list = (List<WbBcLstSetupBVo>)VoUtil.bindList(request, WbBcLstSetupBVo.class, new String[]{"atrbId"});
			if(list != null){
				for(WbBcLstSetupBVo storedWbBcLstSetupBVo : list){
					storedWbBcLstSetupBVo.setCompId(userVo.getCompId());//회사ID
					storedWbBcLstSetupBVo.setSortOrdr(sortOrdr.toString());
					queryQueue.insert(storedWbBcLstSetupBVo);// 사용자 목록설정 저장
					sortOrdr++;
				}
			}
			
			commonSvc.execute(queryQueue);

			// cm.msg.save.success=저장 되었습니다.
			model.put("todo", "parent.location.replace('./setSrchCond.do?menuId=" + menuId+"');");
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			
//		} catch (CmException e) {
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	
	
}
