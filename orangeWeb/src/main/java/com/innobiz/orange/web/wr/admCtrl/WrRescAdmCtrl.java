package com.innobiz.orange.web.wr.admCtrl;

import java.util.ArrayList;
import java.util.List;

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

import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.wr.svc.WrAdmSvc;
import com.innobiz.orange.web.wr.svc.WrCmSvc;
import com.innobiz.orange.web.wr.svc.WrRescFileSvc;
import com.innobiz.orange.web.wr.svc.WrRescMngSvc;
import com.innobiz.orange.web.wr.svc.WrRescSvc;
import com.innobiz.orange.web.wr.utils.WrConstant;
import com.innobiz.orange.web.wr.vo.WrRescBVo;
import com.innobiz.orange.web.wr.vo.WrRescKndBVo;
import com.innobiz.orange.web.wr.vo.WrRescMngBVo;

/** 자원관리 */
@Controller
public class WrRescAdmCtrl {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WrRescAdmCtrl.class);
	
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
	
	/** 파일 서비스 */
	@Autowired
	private WrRescFileSvc wrRescFileSvc;
	
	/** 관리자 서비스 */
	@Autowired
	private WrAdmSvc wrAdmSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;
	
	/** [FRAME] 자원종류 목록 조회 */
	@RequestMapping(value = "/wr/adm/listRescKndFrm")
	public String listRescKndFrm(HttpServletRequest request,
			@RequestParam(value = "typ", required = false) String typ,
			ModelMap model) throws Exception {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 조회조건 매핑
		WrRescKndBVo searchVO = new WrRescKndBVo();
		VoUtil.bind(request, searchVO);
		searchVO.setQueryLang(langTypCd);
		searchVO.setCompId(userVo.getCompId());
		
		//Integer recodeCount = commonSvc.count(searchVO);
		//PersonalUtil.setPaging(request, searchVO, recodeCount);
		
		//목록조회
		@SuppressWarnings("unchecked")
		List<WrRescKndBVo> wrRescKndBVoList = (List<WrRescKndBVo>)commonSvc.queryList(searchVO);
		model.put("wrRescKndBVoList", wrRescKndBVoList);
		//model.put("recodeCount", recodeCount);
		
		model.put("params", ParamUtil.getQueryString(request));
		
		return LayoutUtil.getJspPath("/wr/adm/listRescKndFrm");	
	}
	
	/** [POPUP] 자원종류 상세보기 */
	@RequestMapping(value = "/wr/adm/viewRescKndPop")
	public String viewRescKndPop(HttpServletRequest request,
			@RequestParam(value = "rescKndId", required = true) String rescKndId,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		WrRescKndBVo searchVO = new WrRescKndBVo();
		searchVO.setQueryLang(langTypCd);
		searchVO.setRescKndId(rescKndId);
		
		// 시스템 관리자 여부
		//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		// 시스템 관리자가 아닌 경우에는 - 자기 회사만 검색
		//if(!isSysAdmin){
			searchVO.setCompId(userVo.getCompId());
		//}
		
		WrRescKndBVo wrRescKndBVo = (WrRescKndBVo)commonSvc.queryVo(searchVO);
		
		if (wrRescKndBVo.getRescId() != null) {
			// 리소스기본(WR_RESC_B) 테이블 - 조회, 모델에 추가
			wrRescSvc.queryRescBVo(wrRescKndBVo.getRescId(), model);
		}
		
		model.put("wrRescKndBVo", wrRescKndBVo);		
		return LayoutUtil.getJspPath("/wr/adm/viewRescKndPop");
	}
	
	
	/** [POPUP] 자원종류 등록 수정 화면 출력 */
	@RequestMapping(value = "/wr/adm/setRescKndPop")
	public String setRescKndPop(HttpServletRequest request,
			@RequestParam(value = "rescKndId", required = false) String rescKndId,
			ModelMap model) throws Exception {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
					
		WrRescKndBVo wrRescKndBVo = new WrRescKndBVo();
		if(rescKndId != null && !rescKndId.isEmpty()){
			wrRescKndBVo.setQueryLang(langTypCd);
			wrRescKndBVo.setRescKndId(rescKndId);
			
			// 시스템 관리자 여부
			//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
			// 시스템 관리자가 아닌 경우에는 - 자기 회사만 검색
			//if(!isSysAdmin){
				wrRescKndBVo.setCompId(userVo.getCompId());
			//}
			
			wrRescKndBVo = (WrRescKndBVo)commonSvc.queryVo(wrRescKndBVo);
			
			if (wrRescKndBVo.getRescId() != null) {
				// 리소스기본(WR_RESC_B) 테이블 - 조회, 모델에 추가
				wrRescSvc.queryRescBVo(wrRescKndBVo.getRescId(), model);
			}
		}
		
		model.put("wrRescKndBVo", wrRescKndBVo);
		model.put("paramsForList", ParamUtil.getQueryString(request, "data","rescKndId"));
		
		return LayoutUtil.getJspPath("/wr/adm/setRescKndPop");
	}
	
	/** 자원종류 등록 수정 (저장) */
	@RequestMapping(value = "/wr/adm/transRescKnd")
	public String transRescKnd(HttpServletRequest request,
			@RequestParam(value = "menuId", required = false) String menuId,
			@RequestParam(value = "rescKndId", required = false) String rescKndId,
			ModelMap model) throws Exception {
		
		try {
			QueryQueue queryQueue = new QueryQueue();
			
			WrRescBVo wrRescBVo = wrRescSvc.collectBaRescBVo(request, "", queryQueue);
			
			if (wrRescBVo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			// 조회조건 매핑
			WrRescKndBVo wrRescKndBVo = new WrRescKndBVo();
			VoUtil.bind(request, wrRescKndBVo);
			wrRescKndBVo.setCompId(userVo.getCompId());
			// 리소스 조회 후 리소스ID와 리소스명 세팅
			wrRescKndBVo.setRescId(wrRescBVo.getRescId());
			wrRescKndBVo.setKndNm(wrRescBVo.getRescVa());
			
			if (rescKndId == null || rescKndId.isEmpty()) {
				wrRescKndBVo.setRescKndId(wrCmSvc.createId("WR_RESC_KND_B"));
				wrRescKndBVo.setRegrUid(userVo.getUserUid());
				wrRescKndBVo.setRegDt("sysdate");
				
				// 순서 구하기
				WrRescKndBVo sortVo = new WrRescKndBVo();
				sortVo.setCompId(userVo.getCompId());
				sortVo.setInstanceQueryId("com.innobiz.orange.web.wr.dao.WrRescKndBDao.maxWrRescKndB");
				
				Integer sortOrdr = commonSvc.queryInt(sortVo);
				if(sortOrdr==null) sortOrdr = 1;
				wrRescKndBVo.setSortOrdr(sortOrdr.intValue());
				
				queryQueue.insert(wrRescKndBVo);
			}else{
				//종류에 해당하는 자원목록의 사용여부도 일괄 변경한다.
				String useYn = wrRescKndBVo.getUseYn();
				WrRescMngBVo wrRescMngBVo = new WrRescMngBVo();
				wrRescMngBVo.setRescKndId(rescKndId);
				//wrRescMngBVo.setUseYn(useYn);
				
				//목록 조회
				@SuppressWarnings("unchecked")
				List<WrRescMngBVo> wrRescMngBVoList = (List<WrRescMngBVo>)commonSvc.queryList(wrRescMngBVo);
				if(wrRescMngBVoList.size() > 0 ){
					for(WrRescMngBVo storedWrRescMngBVo : wrRescMngBVoList){
						if(!useYn.equals(storedWrRescMngBVo.getUseYn()) ){
							storedWrRescMngBVo.setUseYn(useYn);
							//자원관리 수정
							queryQueue.update(storedWrRescMngBVo);
						}
					}
				}
				wrRescKndBVo.setRescKndId(rescKndId);
				wrRescKndBVo.setModrUid(userVo.getUserUid());
				wrRescKndBVo.setModDt("sysdate");
				//자원종류 수정
				queryQueue.update(wrRescKndBVo);
			}
			
			// 자원종류기본(WR_RESC_KND_B) 테이블 - UPDATE or INSERT
			//queryQueue.store(wrRescKndBVo);
			
			commonSvc.execute(queryQueue);
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reloadRescKnd('" + listPage + "');");
			//model.put("todo", "parent.location.replace('./listRescMng.do?menuId=" + menuId+"');");
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** 자원종류 삭제 */
	@RequestMapping(value = "/wr/adm/transRescKndDel")
	public String transRescKndDel(HttpServletRequest request,
			@RequestParam(value = "rescKndId", required = true) String rescKndId,
			@Parameter(name="menuId", required=false) String menuId,
			ModelMap model) throws Exception {
		
		try {
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			if(rescKndId != null && !rescKndId.isEmpty() ){
				String[] kndIds = rescKndId.split(",");
				for(String id : kndIds){
					wrRescSvc.deleteRescKnd(queryQueue, id);
				}
				commonSvc.execute(queryQueue);
			}
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("todo", "parent.reloadRescKnd('" + listPage + "');");
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}
		return LayoutUtil.getResultJsp();
	}
	
	
//	/** 임시1 */
//	@RequestMapping(value = "/wr/adm/{path1}")
//	public String boardLv1(HttpServletRequest request,
//					@PathVariable("path1") String path1,
//					ModelMap model) throws Exception {
//		
//		// set, list 로 시작하는 경우 처리
//		checkPath(request, path1, model);
//		
//		return LayoutUtil.getJspPath("/wr/adm/" + path1);
//	}

//	private void checkPath(HttpServletRequest request, String path1,
//			ModelMap model) throws SQLException {
//		// listXXX 이면
//		// 페이지 정보 세팅
//		if (path1.startsWith("list") || path1.equals("index")) {
//			CommonVo commonVo = new CommonVoImpl();
//			PersonalUtil.setPaging(request, commonVo, 25);
//			model.put("recodeCount", 25);
//		}
//		
//		// setXXX 이면
//		// 에디터 사용
//		if (path1.startsWith("set") || path1.equals("index")) {
//			model.addAttribute("JS_OPTS", new String[]{"editor"});
//		}
//	}
	
//	/** 임시2 */
//	@RequestMapping(value = "/wr/adm/{path1}/{path2}")
//	public String boardLv2(HttpServletRequest request,
//					@PathVariable("path1") String path1,
//					@PathVariable("path2") String path2,
//					ModelMap model) throws Exception {
//		
//		// set, list 로 시작하는 경우 처리
//		checkPath(request, path2, model);
//		return LayoutUtil.getJspPath("/wr/adm/" + path1 + "/" + path2);
//	}
	
	/** 자원관리목록 조회 */
	@RequestMapping(value = "/wr/adm/listRescMng")
	public String listRescMng(HttpServletRequest request,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/wr/adm/listRescMng");
	}
	
	/** [FRAME] 자원관리목록 조회 */
	@RequestMapping(value = "/wr/adm/listRescMngFrm")
	public String listRescMngFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 조회조건 매핑
		WrRescMngBVo searchVO = new WrRescMngBVo();
		VoUtil.bind(request, searchVO);
		searchVO.setQueryLang(langTypCd);
		searchVO.setCompId(userVo.getCompId());
		
		//목록 조회 건수
		//Integer recodeCount = commonSvc.count(searchVO);
		//PersonalUtil.setPaging(request, searchVO, recodeCount);
		
		//목록 조회
		@SuppressWarnings("unchecked")
		List<WrRescMngBVo> wrRescMngBVoList = (List<WrRescMngBVo>)commonSvc.queryList(searchVO);
		
		//이미지 정보 세팅
		wrRescMngSvc.setWrRescMngImg(wrRescMngBVoList);
		model.put("wrRescMngBVoList", wrRescMngBVoList);
		//model.put("recodeCount", recodeCount);
		
		/** 종류코드 조회 */
		WrRescKndBVo wrRescKndBVo = new WrRescKndBVo();
		wrRescKndBVo.setCompId(userVo.getCompId());
		wrRescKndBVo.setQueryLang(langTypCd);
		@SuppressWarnings("unchecked")
		List<WrRescKndBVo> wrRescKndBVoList = (List<WrRescKndBVo>)commonSvc.queryList(wrRescKndBVo);
		model.put("wrRescKndBVoList", wrRescKndBVoList);
		
		model.put("paramsForList", ParamUtil.getQueryString(request, "typ"));
		model.put("params", ParamUtil.getQueryString(request));
		
		// get 파라미터를 post 파라미터로 전달하기 위해
		model.put("paramEntryList", ParamUtil.getEntryMapList(request));
		model.put("listPage", "listResc");
		return LayoutUtil.getJspPath("/wr/adm/listRescMngFrm");
	}
	
	/** [POPUP] 자원관리상세보기 */
	@RequestMapping(value = "/wr/adm/viewRescMngPop")
	public String viewRescMngPop(HttpServletRequest request,
			@RequestParam(value = "rescMngId", required = true) String rescMngId,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		WrRescMngBVo searchVO = new WrRescMngBVo();
		searchVO.setQueryLang(langTypCd);
		searchVO.setRescMngId(rescMngId);
		
		// 시스템 관리자 여부
		//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		// 시스템 관리자가 아닌 경우에는 - 자기 회사만 검색
		//if(!isSysAdmin){
			searchVO.setCompId(userVo.getCompId());
		//}
		
		WrRescMngBVo wrRescMngBVo = (WrRescMngBVo)wrRescMngSvc.getWbBcInfo(searchVO);
		
		if (wrRescMngBVo.getRescId() != null) {
			// 리소스기본(WR_RESC_B) 테이블 - 조회, 모델에 추가
			wrRescSvc.queryRescBVo(wrRescMngBVo.getRescId(), model);
		}
		
		model.put("wrRescMngBVo", wrRescMngBVo);
		
		model.put("paramsForList", ParamUtil.getQueryString(request, "data","rescMngId"));
		
		return LayoutUtil.getJspPath("/wr/adm/viewRescMngPop");
	}
	
	
	/** [POPUP] 자원관리등록 수정 화면 출력 */
	@RequestMapping(value = "/wr/adm/setRescMngPop")
	public String setRescMngPop(HttpServletRequest request,
			@RequestParam(value = "rescMngId", required = false) String rescMngId,
			ModelMap model) throws Exception {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
					
		WrRescMngBVo wrRescMngBVo = new WrRescMngBVo();
		if(rescMngId != null && !rescMngId.isEmpty()){
			wrRescMngBVo.setQueryLang(langTypCd);
			wrRescMngBVo.setRescMngId(rescMngId);
			
			// 시스템 관리자 여부
			//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
			// 시스템 관리자가 아닌 경우에는 - 자기 회사만 검색
			//if(!isSysAdmin){
				wrRescMngBVo.setCompId(userVo.getCompId());
			//}
			
			wrRescMngBVo = (WrRescMngBVo)wrRescMngSvc.getWbBcInfo(wrRescMngBVo);
			
			if (wrRescMngBVo.getRescId() != null) {
				// 리소스기본(WR_RESC_B) 테이블 - 조회, 모델에 추가
				wrRescSvc.queryRescBVo(wrRescMngBVo.getRescId(), model);
			}
		}
		model.put("paramsForList", ParamUtil.getQueryString(request, "data","rescMngId"));
		model.put("wrRescMngBVo", wrRescMngBVo);
		
		/** 종류코드 조회 */
		WrRescKndBVo wrRescKndBVo = new WrRescKndBVo();
		wrRescKndBVo.setCompId(userVo.getCompId());
		wrRescKndBVo.setQueryLang(langTypCd);
		@SuppressWarnings("unchecked")
		List<WrRescKndBVo> wrRescKndBVoList = (List<WrRescKndBVo>)commonSvc.queryList(wrRescKndBVo);
		model.put("wrRescKndBVoList", wrRescKndBVoList);
		
		return LayoutUtil.getJspPath("/wr/adm/setRescMngPop");
	}
	
	/** 자원관리등록 수정 (저장) */
	@RequestMapping(value = "/wr/adm/transRescMng")
	public String transRescMng(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		UploadHandler uploadHandler = null;
		try {
			// Multipart 파일 업로드
			uploadHandler = wrRescFileSvc.upload(request);

			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			String rescMngId = ParamUtil.getRequestParam(request, "rescMngId", false);
			String listPage     = ParamUtil.getRequestParam(request, "listPage", true);
			
			QueryQueue queryQueue = new QueryQueue();
			
			WrRescBVo wrRescBVo = wrRescSvc.collectBaRescBVo(request, "", queryQueue);
			
			if (wrRescBVo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			// 조회조건 매핑
			WrRescMngBVo wrRescMngBVo = new WrRescMngBVo();
			VoUtil.bind(request, wrRescMngBVo);
			
			// 시스템 관리자 여부
			//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
			// 시스템 관리자가 아닌 경우에는 - 자기 회사만 검색
			//if(!isSysAdmin){
				wrRescMngBVo.setCompId(userVo.getCompId());
			//}
			
			// 리소스 조회 후 리소스ID와 리소스명 세팅
			wrRescMngBVo.setRescId(wrRescBVo.getRescId());
			wrRescMngBVo.setRescNm(wrRescBVo.getRescVa());
			//wrRescMngBVo.setRegrUid(userVo.getUserUid());
			
			if (rescMngId != null && !rescMngId.isEmpty()) {
				wrRescMngBVo.setRescMngId(rescMngId);
			}
			//저장
			wrRescMngSvc.saveRescMng(request, queryQueue, wrRescMngBVo, userVo);
			
			commonSvc.execute(queryQueue);
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			//model.put("todo", "parent.location.replace('" + listPage + "');");
			model.put("todo", "parent.reloadRescMng('" + listPage + "');");
			
			//model.put("todo", "parent.location.replace('./listRescMng.do?menuId=" + menuId+"&typ="+typ+"');");
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		} finally {
			if(uploadHandler!=null ) uploadHandler.removeTempDir();
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** 자원관리삭제 */
	@RequestMapping(value = "/wr/adm/transRescMngDel")
	public String transRescMngDel(HttpServletRequest request,
			@RequestParam(value = "rescMngId", required = true) String rescMngId,
			@Parameter(name="menuId", required=false) String menuId,
			@RequestParam(value = "typ", required = false) String typ,
			ModelMap model) throws Exception {
		
		try {
			// 세션의 사용자 정보
			//UserVo userVo = LoginSession.getUser(request);
			
			// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			if(rescMngId != null && !rescMngId.isEmpty() ){
				String[] kndIds = rescMngId.split(",");
				for(String id : kndIds){
					wrRescSvc.deleteRescMng(queryQueue, id);
				}
				
				commonSvc.execute(queryQueue);
			}
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("todo", "parent.reloadRescMng('" + listPage + "');");
			//model.put("todo", "parent.location.replace('" + listPage + "');");
			//model.put("todo", "parent.location.replace('./listRescMng.do?menuId=" + menuId+"&typ="+typ+"');");
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] 중복 조회(자원종류,자원) */
	@RequestMapping(value = "/wr/adm/chkDupRescAjx")
	public String chkDupRescAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception{
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			
			// 종류,자원 구분키
			String actKey = (String) object.get("actKey");
			String prefix = (String) object.get("prefix"); // 파라미터 Prefix
			String langs = (String) object.get("langs"); // 언어
			
			if ( actKey == null || actKey.isEmpty() || prefix == null || prefix.isEmpty() || langs == null || langs.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			String seqId = (String) object.get("seqId"); // 시퀀스ID
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			// 언어 배열
			String[] langTypCds = langs.split(",");
			String chkVa  = null;
			List<String> msgList = null;
			if("knd".equals(actKey)){ // 종류
				// 조회조건 매핑
				WrRescKndBVo wrRescKndBVo = null;
				for(String lang : langTypCds){
					chkVa  = (String) object.get(prefix+lang); // 중복기준값
					if ( chkVa == null || chkVa.isEmpty()) {
						// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
						throw new CmException("pt.msg.nodata.passed", request);
					}
					if(seqId!=null && !seqId.isEmpty()){// 수정일 경우 자기자신인지 체크한다.
						wrRescKndBVo = new WrRescKndBVo();
						wrRescKndBVo.setCompId(userVo.getCompId());
						wrRescKndBVo.setQueryLang(lang);
						wrRescKndBVo.setRescKndId(seqId);
						wrRescKndBVo = (WrRescKndBVo)commonSvc.queryVo(wrRescKndBVo);
						if(chkVa.equals(wrRescKndBVo.getKndNm())) continue;
					}

					wrRescKndBVo = new WrRescKndBVo();
					wrRescKndBVo.setCompId(userVo.getCompId());
					wrRescKndBVo.setSchCat("chkNm");
					wrRescKndBVo.setQueryLang(lang);
					wrRescKndBVo.setSchWord(chkVa);
					if(commonSvc.count(wrRescKndBVo)>0){
						if(msgList==null) msgList = new ArrayList<String>();
						msgList.add(messageProperties.getMessage("cm.dup.title", new String[]{chkVa}, request));
					}
				}
			}else if("resc".equals(actKey)){ // 자원
				String rescKndId = (String) object.get("rescKndId"); // 자원종류
				
				if ( rescKndId == null || rescKndId.isEmpty()) {
					// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
					throw new CmException("pt.msg.nodata.passed", request);
				}
				
				// 조회조건 매핑
				WrRescMngBVo wrRescMngBVo = null;
				for(String lang : langTypCds){
					chkVa  = (String) object.get(prefix+lang); // 중복기준값
					if ( chkVa == null || chkVa.isEmpty()) {
						// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
						throw new CmException("pt.msg.nodata.passed", request);
					}
					if(seqId!=null && !seqId.isEmpty()){// 수정일 경우 자기자신인지 체크한다.
						wrRescMngBVo = new WrRescMngBVo();
						wrRescMngBVo.setCompId(userVo.getCompId());
						wrRescMngBVo.setQueryLang(lang);
						wrRescMngBVo.setRescMngId(seqId);
						wrRescMngBVo = (WrRescMngBVo)commonSvc.queryVo(wrRescMngBVo);
						if(chkVa.equals(wrRescMngBVo.getRescNm())) continue;
					}

					wrRescMngBVo = new WrRescMngBVo();
					wrRescMngBVo.setCompId(userVo.getCompId());
					wrRescMngBVo.setRescKndId(rescKndId);
					wrRescMngBVo.setSchCat("chkNm");
					wrRescMngBVo.setQueryLang(lang);
					wrRescMngBVo.setSchWord(chkVa);
					if(commonSvc.count(wrRescMngBVo)>0){
						if(msgList==null) msgList = new ArrayList<String>();
						msgList.add(messageProperties.getMessage("cm.dup.title", new String[]{chkVa}, request));
					}
				}
			}else{
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			if(msgList!=null) model.put("msgList", msgList);
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] 순서변경 - 자원종류*/
	@RequestMapping(value = "/wr/adm/transRescKndMoveSaveAjx")
	public String transRescKndMoveSaveAjx(HttpServletRequest request,			
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {
		
		try {
			// 파라미터 검사
			JSONObject jsonObject = (JSONObject) JSONValue.parse(data);
			
			// 순서 변경할 ID 목록
			JSONArray jsonArray = (JSONArray)jsonObject.get("rescList");
			
			if ( jsonArray == null || jsonArray.isEmpty()) {
				LOGGER.error("[ERROR] jsonArray == null || jsonArray.isEmpty()");
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			WrRescKndBVo wrRescKndBVo=null;
			Integer sortOrdr=1;
			String rescKndId;
			for (int i = 0; i < jsonArray.size(); i++) {
				rescKndId=(String) jsonArray.get(i);
				wrRescKndBVo = new WrRescKndBVo();
				wrRescKndBVo.setCompId(userVo.getCompId());
				wrRescKndBVo.setRescKndId(rescKndId);
				wrRescKndBVo.setSortOrdr(sortOrdr++);
				queryQueue.update(wrRescKndBVo);
			}
			if(!queryQueue.isEmpty()) commonSvc.execute(queryQueue);
			
			//cm.msg.save.success=저장 되었습니다.
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
	
	/** [AJAX] 순서변경 - 자원 */
	@RequestMapping(value = "/wr/adm/transRescMngMoveSaveAjx")
	public String transRescMngMoveSaveAjx(HttpServletRequest request,			
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {
		
		try {
			// 파라미터 검사
			JSONObject jsonObject = (JSONObject) JSONValue.parse(data);
			
			// 순서 변경할 ID 목록
			JSONArray jsonArray = (JSONArray)jsonObject.get("rescList");
			String rescKndId = (String) jsonObject.get("rescKndId");
			
			if ( jsonArray == null || jsonArray.isEmpty() || rescKndId == null || rescKndId.isEmpty()) {
				LOGGER.error("[ERROR] jsonArray == null || jsonArray.isEmpty() || rescKndId == null || rescKndId.isEmpty()");
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			WrRescMngBVo wrRescMngBVo = null;
			String rescMngId;
			Integer sortOrdr=1;
			for (int i = 0; i < jsonArray.size(); i++) {
				rescMngId=(String) jsonArray.get(i);
				wrRescMngBVo = new WrRescMngBVo();
				wrRescMngBVo.setCompId(userVo.getCompId());
				wrRescMngBVo.setRescKndId(rescKndId);
				wrRescMngBVo.setRescMngId(rescMngId);
				wrRescMngBVo.setSortOrdr(sortOrdr++);
				queryQueue.update(wrRescMngBVo);
			}
			if(!queryQueue.isEmpty()) commonSvc.execute(queryQueue);
			
			//cm.msg.save.success=저장 되었습니다.
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
	
	/** 설정 - 조회 */
	@RequestMapping(value = "/wr/adm/setEnvPop")
	public String setEnvPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 환경설정
		wrAdmSvc.getEnvConfigMap(model, userVo.getCompId());
		
		// 일정대상 목록
		model.put("schdlKndCdList", wrAdmSvc.getSchdlTgtList(request));
		return LayoutUtil.getJspPath("/wr/adm/setEnvPop");
	}
	
	/** [히든프레임] 옵션관리 - 저장 */
	@RequestMapping(value = "/wr/adm/transEnv")
	public String transSchdlSetup(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		try{
			UserVo userVo = LoginSession.getUser(request);
			QueryQueue queryQueue = new QueryQueue();
			ptSysSvc.setSysSetup(WrConstant.SYS_CONFIG+userVo.getCompId(), queryQueue, true, request);

			if(!queryQueue.isEmpty()){
				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP);
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			} else {
				// cm.msg.nodata.toSave=저장할 데이터가 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.nodata.toSave", request));
			}
			
//		} catch(CmException e){
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
}
