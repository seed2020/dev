package com.innobiz.orange.web.wc.admCtrl;

import java.util.ArrayList;
import java.util.List;
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

import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.exception.CmException;
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
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.wc.svc.WcAdmSvc;
import com.innobiz.orange.web.wc.svc.WcCmSvc;
import com.innobiz.orange.web.wc.svc.WcRescSvc;
import com.innobiz.orange.web.wc.svc.WcScdManagerSvc;
import com.innobiz.orange.web.wc.utils.WcConstant;
import com.innobiz.orange.web.wc.vo.WcCatClsBVo;
import com.innobiz.orange.web.wc.vo.WcNatBVo;
import com.innobiz.orange.web.wc.vo.WcRescBVo;

/** 자원관리 */
@Controller
public class WcSchdlAdmCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WcSchdlAdmCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 일정관리 공통 서비스 */
	@Autowired
	private WcCmSvc wcCmSvc;
	
	/** 리소스 서비스 */
	@Autowired
	private WcRescSvc wcRescSvc;
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 공통 서비스 */
	@Autowired
	private WcScdManagerSvc wcScdManagerSvc;
	
	/** 일정관리 관리자 서비스 */
	@Autowired
	private WcAdmSvc wcAdmSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;
	
	/** 카테고리 목록 조회 */
	@RequestMapping(value = "/wc/adm/listCatCls")
	public String listCatCls(HttpServletRequest request,
			ModelMap model) throws Exception {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 조회조건 매핑
		WcCatClsBVo searchVO = new WcCatClsBVo();
		VoUtil.bind(request, searchVO);
		searchVO.setQueryLang(langTypCd);
		searchVO.setOrderBy("SORT_ORDR");
		// 시스템 관리자 여부
		//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		// 시스템 관리자가 아닌 경우에는 - 자기 회사만 검색
		//if(!isSysAdmin){
			searchVO.setCompId(userVo.getCompId());
		//}
		
		@SuppressWarnings("unchecked")
		List<WcCatClsBVo> wcCatClsBVoList = (List<WcCatClsBVo>)commonSvc.queryList(searchVO);
		
		// UI 구성용 - 빈 VO 하나 더함
		if(wcCatClsBVoList == null) wcCatClsBVoList = new ArrayList<WcCatClsBVo>();
		wcCatClsBVoList.add(new WcCatClsBVo());
		wcCatClsBVoList.add(new WcCatClsBVo());
		model.put("wcCatClsBVoList", wcCatClsBVoList);
		
		// 리소스 조회
		WcRescBVo wcRescBVo = new WcRescBVo();
		wcRescBVo.setCompId(userVo.getCompId());
		@SuppressWarnings("unchecked")
		List<WcRescBVo> wcRescBVoList = (List<WcRescBVo>)commonSvc.queryList(wcRescBVo);
		
		// 코드 리소스 model에 저장
		if(wcRescBVoList!=null){
			for(WcRescBVo storedWcRescBVo : wcRescBVoList){
				model.put(storedWcRescBVo.getRescId()+"_"+storedWcRescBVo.getLangTypCd(), storedWcRescBVo.getRescVa());
			}
		}
		return LayoutUtil.getJspPath("/wc/adm/listCatCls");
	}
	
	/** 카테고리 등록 수정 (저장) */
	@RequestMapping(value = "/wc/adm/transCatCls")
	public String transCatCls(HttpServletRequest request,
			@Parameter(name="delList", required=false)String delList,
			ModelMap model) throws Exception {
		
		try {
			QueryQueue queryQueue = new QueryQueue();
			
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			//  삭제 목록 처리
			int i, size;
			String[] delIds = delList==null || delList.isEmpty() ? new String[]{} : delList.split(",");
			if(delIds.length > 0 ){
				for(String catId : delIds){
					WcCatClsBVo delWcCatClsBVo = new WcCatClsBVo();
					delWcCatClsBVo.setCompId(userVo.getCompId());
					delWcCatClsBVo.setCatId(catId);
					wcRescSvc.deleteCatCls(queryQueue, delWcCatClsBVo);
				}
			}
			
			// 카테고리관리(WC_CAT_CLS_B) 테이블
			@SuppressWarnings("unchecked")
			List<WcCatClsBVo> wcCatClsBVoList = (List<WcCatClsBVo>)VoUtil.bindList(request, WcCatClsBVo.class, new String[]{"valid"});
			size = wcCatClsBVoList==null ? 0 : wcCatClsBVoList.size();
			WcCatClsBVo wcCatClsBVo = new WcCatClsBVo();
			// 리소스 정보 queryQueue에 담음
			wcRescSvc.collectWcRescBVoList(request, queryQueue, wcCatClsBVoList, "valid", "rescId", "catNm");
			
			for(i=0;i<size;i++){
				wcCatClsBVo = wcCatClsBVoList.get(i);
				if(wcCatClsBVo.getCatId()==null || wcCatClsBVo.getCatId().isEmpty()){
					wcCatClsBVo.setCatId(wcCmSvc.createId("WC_CAT_CLS_B"));
					wcCatClsBVo.setCompId(userVo.getCompId());
					wcCatClsBVo.setRegDt("sysdate");
					wcCatClsBVo.setRegrUid(userVo.getUserUid());
					queryQueue.insert(wcCatClsBVo);
				} else {
					wcCatClsBVo.setModDt("sysdate");
					wcCatClsBVo.setModrUid(userVo.getUserUid());
					queryQueue.update(wcCatClsBVo);
				}
			}
			
			commonSvc.execute(queryQueue);
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
	
	/** [팝업] 국가선택 */
	@RequestMapping(value = {"/wc/setNatPop","/wc/adm/setNatPop","/wc/adm/setNatListPop"})
	public String setNatPop(HttpServletRequest request,
			@RequestParam(value = "compId", required = false) String compId,
			ModelMap model) throws Exception {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
		WcNatBVo wcNatBVo = new WcNatBVo();
		if(compId==null || compId.isEmpty()) compId = userVo.getCompId();
		wcNatBVo.setCompId(compId);
		wcNatBVo.setQueryLang(langTypCd);
		
		@SuppressWarnings("unchecked")
		List<WcNatBVo> wcNatBVoList = (List<WcNatBVo>)commonSvc.queryList(wcNatBVo);
		if(request.getRequestURI().startsWith("/wc/adm/setNatListPop")){
			model.put("multi", "Y");
			model.put("isFindNat", Boolean.TRUE);
		}else{
			model.put("isFindNat", Boolean.FALSE);
			model.put("multi", "N");
		}
		if(wcNatBVoList==null || wcNatBVoList.size()==0) wcNatBVoList = new ArrayList<WcNatBVo>();
		wcNatBVoList.add(new WcNatBVo());
		model.put("wcNatBVoList", wcNatBVoList);
		
		String chkNatCd = ParamUtil.getRequestParam(request, "chkNatCd", false);
		
		// 개인설정 또는 일정
		if((chkNatCd==null || chkNatCd.isEmpty()) && request.getRequestURI().startsWith("/wc/setNatPop")){
			chkNatCd = wcScdManagerSvc.getNatCd(userVo);
		}
		model.put("chkNatCd", chkNatCd);
		
		return LayoutUtil.getJspPath("/wc/adm/setNatPop");
	}
	
	/** [FRAME] 국가 목록 조회(오른쪽 프레임) */
	@RequestMapping(value = "/wc/adm/listNatFrm")
	public String listNatFrm(HttpServletRequest request,
			@RequestParam(value = "compId", required = false) String compId,
			ModelMap model) throws Exception {
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		if(compId==null || compId.isEmpty()) compId = userVo.getCompId(); 
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		/** 국적코드 조회 */
		List<PtCdBVo> natyCdList = ptCmSvc.getCdList("NATY_CD", langTypCd, "Y");
		model.put("natyCdList", natyCdList);
		
		return LayoutUtil.getJspPath("/wc/adm/listNatFrm");
	}
	
	/** [AJAX] 국가설정 저장 */
	@RequestMapping(value = {"/wc/transNatAjx","/wc/adm/transNatAjx","/wc/adm/transNatListAjx"})
	public String transEmailAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			
			String compId = (String)object.get("compId"); // 회사ID
			JSONArray cds = (JSONArray) object.get("cds");
			JSONArray rescIds = (JSONArray) object.get("rescIds");
			if (cds == null || cds.size() == 0 || rescIds == null || rescIds.size() == 0) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				String msg = messageProperties.getMessage("pt.msg.nodata.passed", request);
				LOGGER.error("jsonArray size : 0  msg:"+msg);
				model.put("message", msg);
				return LayoutUtil.getResultJsp();
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			if(compId==null || compId.isEmpty()) compId = userVo.getCompId();
			
			QueryQueue queryQueue = new QueryQueue();
			WcNatBVo wcNatBVo = null;
			String actKey = "psn";
			// 기본설정된 국가코드
			String dftCd = null;
			if(request.getRequestURI().startsWith("/wc/adm/transNatListAjx")){
				actKey = "adm";
				wcNatBVo = new WcNatBVo();
				wcNatBVo.setCompId(compId);
				wcNatBVo.setDftYn("Y");
				wcNatBVo = (WcNatBVo)commonSvc.queryVo(wcNatBVo);
				if(wcNatBVo!=null){
					dftCd = wcNatBVo.getCd();
				}
				
				wcNatBVo = new WcNatBVo();
				wcNatBVo.setCompId(compId);
				// 전체 삭제
				queryQueue.delete(wcNatBVo);
			}else if(request.getRequestURI().startsWith("/wc/adm/transNatAjx")){
				actKey = "dft";
				wcNatBVo = new WcNatBVo();
				wcNatBVo.setCompId(compId);
				wcNatBVo.setDftYn("N");
				queryQueue.update(wcNatBVo);
			}
			
			String cd,rescId;
			// 저장
			for(int i=0;i<cds.size();i++){
				cd = (String)cds.get(i);
				rescId = (String)rescIds.get(i);
				wcNatBVo = new WcNatBVo();
				wcNatBVo.setCd(cd);
				wcNatBVo.setCompId(compId);
				wcNatBVo.setRescId(rescId);
				if("adm".equals(actKey)){
					if(dftCd!=null && dftCd.equals(cd)) wcNatBVo.setDftYn("Y");
					else wcNatBVo.setDftYn("N");
					wcNatBVo.setSortOrdr(i+1);
				}else if("psn".equals(actKey)){
					wcNatBVo.setUserUid(userVo.getUserUid());
				}else if("dft".equals(actKey)){
					wcNatBVo.setDftYn("Y");
				}else{
					break;
				}
				queryQueue.store(wcNatBVo);
			}
			
			commonSvc.execute(queryQueue);
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("result", "ok");
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** 설정 - 조회 */
	@RequestMapping(value = "/wc/adm/setEnvPop")
	public String setEnvPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 환경설정
		Map<String, String> envConfigMap = wcAdmSvc.getEnvConfigMap(null, userVo.getCompId());
		model.put("envConfigMap", envConfigMap);
		
		// 일정대상 조회
		model.put("schdlKndCdList", wcCmSvc.getSchdlKndList(request));
				
		return LayoutUtil.getJspPath("/wc/adm/setEnvPop");
	}
	
	/** [히든프레임] 옵션관리 - 저장 */
	@RequestMapping(value = "/wc/adm/transEnv")
	public String transSchdlSetup(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		try{
			UserVo userVo = LoginSession.getUser(request);
			QueryQueue queryQueue = new QueryQueue();
			ptSysSvc.setSysSetup(WcConstant.SYS_CONFIG+userVo.getCompId(), queryQueue, true, request);

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
