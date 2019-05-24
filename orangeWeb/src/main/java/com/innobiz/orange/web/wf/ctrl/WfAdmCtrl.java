package com.innobiz.orange.web.wf.ctrl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtxSortOrdrChnVo;
import com.innobiz.orange.web.wf.svc.WfAdmSvc;
import com.innobiz.orange.web.wf.svc.WfCdSvc;
import com.innobiz.orange.web.wf.svc.WfCmSvc;
import com.innobiz.orange.web.wf.svc.WfFormSvc;
import com.innobiz.orange.web.wf.svc.WfGenSvc;
import com.innobiz.orange.web.wf.svc.WfRescSvc;
import com.innobiz.orange.web.wf.utils.FormUtil;
import com.innobiz.orange.web.wf.utils.WfConstant;
import com.innobiz.orange.web.wf.vo.WfFormBVo;
import com.innobiz.orange.web.wf.vo.WfFormColmLVo;
import com.innobiz.orange.web.wf.vo.WfFormGrpBVo;
import com.innobiz.orange.web.wf.vo.WfFormLstDVo;
import com.innobiz.orange.web.wf.vo.WfFormRegDVo;
import com.innobiz.orange.web.wf.vo.WfRescBVo;

@Controller
public class WfAdmCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WfAdmCtrl.class);
	
	/** 공통 서비스 */
	@Autowired
	private WfCmSvc wfCmSvc;
	
	/** 리소스 조회 저장용 서비스 */
	@Resource(name = "wfRescSvc")
	private WfRescSvc wfRescSvc;
	
	/** 관리 서비스 */
	@Resource(name = "wfAdmSvc")
	private WfAdmSvc wfAdmSvc;
	
	/** 양식 서비스 */
	@Resource(name = "wfFormSvc")
	private WfFormSvc wfFormSvc;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 관리 테이블 생성 서비스 */
	@Resource(name = "wfGenSvc")
	private WfGenSvc wfGenSvc;
	
	/** 코드 서비스 */
	@Resource(name = "wfCdSvc")
	private WfCdSvc wfCdSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;
	
	/** 양식 목록 */
	@RequestMapping(value = {"/wf/adm/form/listForm", "/wf/adm/works/listWorks"})
	public String listForm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 양식편집 여부
		model.put("urlOpt", request.getRequestURI().startsWith("/wf/adm/form") ? "form" : "works");
		
		return LayoutUtil.getJspPath("/wf/adm/form/listForm");
	}
	
	/** 그룹 트리 조회(왼쪽 프레임) */
	@RequestMapping(value = {"/wf/adm/form/treeGrpFrm", "/wf/adm/works/treeGrpFrm", "/wf/adm/form/mnuGrpFrm", "/cm/form/findFormFrm", "/bb/adm/findFormFrm", "/ap/env/findFormFrm", "/ap/adm/form/findFormFrm"})
	public String treeMdFrm(HttpServletRequest request,
			@Parameter(name="grpId", required=false) String grpId,
			@Parameter(name="noInit", required=false) String noInit,
			@Parameter(name="formNo", required=false) String formNo,
			@Parameter(name="mdTypCd", required=false) String mdTypCd,
			@RequestParam(value = "paramCompId", required = false) String paramCompId,
			ModelMap model) throws Exception {
				
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 관리자 여부
		boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		
		// 회사ID
		String compId=isSysAdmin && paramCompId!=null && !paramCompId.isEmpty() ? paramCompId : userVo.getCompId();
				
		boolean inWorksList=request.getRequestURI().startsWith("/wf/adm/works/") || request.getRequestURI().startsWith("/wf/adm/form/mnu")
				|| request.getRequestURI().startsWith("/ap/") || request.getRequestURI().startsWith("/bb/") || request.getRequestURI().startsWith("/cm/form/");
		
		// 양식 편집 여부
		String urlOpt = inWorksList ? "works" : "form";
		
		if("works".equals(urlOpt) && formNo!=null && !formNo.isEmpty())
			grpId=formNo;
		
		// 트리 목록 조회
		List<WfFormGrpBVo> wfFormGrpBVoList = wfAdmSvc.getGrpTreeList(compId, null, langTypCd);
		
		String firstGrpId=null;
		if(wfFormGrpBVoList != null && !wfFormGrpBVoList.isEmpty()){
			model.put("wfFormGrpBVoList", wfFormGrpBVoList);
			firstGrpId = wfFormGrpBVoList.get(0).getGrpId();
		}
		if(!"Y".equals(noInit)){
			if(grpId != null && !grpId.isEmpty()){
				model.put("selectedNode", grpId);
			} else if(firstGrpId != null && !firstGrpId.isEmpty()){
				model.put("selectedNode", firstGrpId);
			} else {
				model.put("selectedNode", "ROOT");
			}
		}
		
		if("works".equals(urlOpt)){
			// 메뉴등록 여부
			boolean inMnu=request.getRequestURI().startsWith("/wf/adm/form/mnu") || request.getRequestURI().startsWith("/wf/adm/works");
			
			// 테이블관리 기본(WF_FORM_B) 테이블 - BIND
			WfFormBVo wfFormBVo = new WfFormBVo();
			//VoUtil.bind(request, wfFormBVo);
			wfFormBVo.setQueryLang(langTypCd);
			wfFormBVo.setCompId(compId);
			wfFormBVo.setUseYn("Y"); // 사용여부
			if(inMnu) wfFormBVo.setFormTyp("A"); // 메뉴등록여부
			
			// 모듈 별 목록 조회 여부
			boolean isMdUri=request.getRequestURI().startsWith("/ap/") || request.getRequestURI().startsWith("/bb/") || request.getRequestURI().startsWith("/cm/form/");
			if(isMdUri && mdTypCd!=null){
				wfFormBVo.setMdTypCd(mdTypCd);
			}
			
			// cm.option.allComp=전체회사
			String grpSuffix=inMnu ? messageProperties.getMessage("cm.option.allComp", request) : null;
			
			String grpNm=null;
			@SuppressWarnings("unchecked")
			List<WfFormBVo> wfFormBVoList = (List<WfFormBVo>) commonSvc.queryList(wfFormBVo);
			if(wfFormBVoList!=null){
				if(wfFormGrpBVoList==null) wfFormGrpBVoList=new ArrayList<WfFormGrpBVo>();
				WfFormGrpBVo wfFormGrpBVo;
				for(WfFormBVo storedWfFormBVo : wfFormBVoList){
					if(storedWfFormBVo.getGenId()==null) continue; // 배포되지 않은 양식은 제외
					wfFormGrpBVo = new WfFormGrpBVo();
					wfFormGrpBVo.setGrpId(storedWfFormBVo.getFormNo());
					grpNm=storedWfFormBVo.getFormNm();
					if(grpSuffix!=null && storedWfFormBVo.getAllCompYn()!=null && "Y".equals(storedWfFormBVo.getAllCompYn())){
						grpNm+="("+grpSuffix+")";
					}
					wfFormGrpBVo.setGrpNm(grpNm);
					wfFormGrpBVo.setGrpPid(storedWfFormBVo.getGrpId());
					wfFormGrpBVo.setGrpTyp("A");
					if(storedWfFormBVo.getGenId()!=null) wfFormGrpBVo.setGenId(storedWfFormBVo.getGenId());
					wfFormGrpBVo.setRescId(storedWfFormBVo.getRescId());
					wfFormGrpBVoList.add(wfFormGrpBVo);
				}
			}
		}
		
		
		return LayoutUtil.getJspPath("/wf/adm/form/treeGrpFrm");
	}
	
	/** [팝업] - 그룹 등록 */
	@RequestMapping(value = "/wf/adm/form/setGrpPop")
	public String setGrpPop(HttpServletRequest request,
			@Parameter(name="grpId", required=false) String grpId,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		if(grpId!=null && !grpId.isEmpty()){
			WfFormGrpBVo wfFormGrpBVo = wfAdmSvc.getWfFormGrpBVo(userVo.getCompId(), grpId, langTypCd);
			if(wfFormGrpBVo==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			if (wfFormGrpBVo.getRescId() != null) {
				// 리소스기본(WL_RESC_B) 테이블 - 조회, 모델에 추가
				wfRescSvc.queryRescBVo(wfFormGrpBVo.getRescId(), model);
			}
			model.put("wfFormGrpBVo", wfFormGrpBVo);
			
			if("ROOT".equals(wfFormGrpBVo.getGrpPid())){ // 최상위 그룹 일경우 회사 목록을 추가한다.
				wfAdmSvc.setCompAffiliateVoList(model, userVo.getCompId(), langTypCd, false);
			}
			
		}
		return LayoutUtil.getJspPath("/wf/adm/form/setGrpPop");
	}
	
	/** [AJAX] - 그룹 저장 (단건) */
	@RequestMapping(value = "/wf/adm/form/transGrpAjx")
	public String transGrpAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String grpId = (String) object.get("grpId");
			Map<String, Object> paramMap = JsonUtil.jsonToMap(object);
			
			QueryQueue queryQueue = new QueryQueue();
						
			WfRescBVo whRescBVo = wfRescSvc.collectWfRescBVo(request, "", queryQueue, paramMap);
			
			if (whRescBVo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			String compId = userVo.getCompId(); // 회사ID
			WfFormGrpBVo wfFormGrpBVo = new WfFormGrpBVo();
			
			// jsonMap => Vo
			FormUtil.setParamToVo(paramMap, wfFormGrpBVo, null);
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
						
			// 회사ID 일괄 변경
			if(grpId!=null && !grpId.isEmpty() && paramMap.containsKey("paramCompId") && paramMap.get("paramCompId")!=null){
				wfFormGrpBVo.setCompId(null);
				String paramCompId=(String)paramMap.get("paramCompId");
				
				// 하위 그룹 목록 조회
				List<WfFormGrpBVo> downGrpList=wfAdmSvc.getDownTreeList(paramCompId, grpId, langTypCd, false, "Y");
				WfFormGrpBVo updateWfFormGrpBVo = new WfFormGrpBVo();
				List<String> grpIdList = new ArrayList<String>();
				for(WfFormGrpBVo storedWfFormGrpBVo : downGrpList){
					grpIdList.add(storedWfFormGrpBVo.getGrpId());
				}
				if(grpIdList.size()>0){ // 하위 그룹 회사 일괄변경
					updateWfFormGrpBVo.setParamCompId(paramCompId);
					updateWfFormGrpBVo.setGrpIdList(grpIdList);
					queryQueue.update(updateWfFormGrpBVo);
				}
				
			}
			
			// 회사 ID 조회조건 추가[계열사 설정 확인]
			//wfAdmSvc.setCompAffiliateIdList(compId, langTypCd, wfFormGrpBVo, false);
			wfFormGrpBVo.setGrpNm(whRescBVo.getRescVa());
			
			if(grpId!=null && !grpId.isEmpty()){
				wfFormGrpBVo.setModrUid(userVo.getUserUid());
				wfFormGrpBVo.setModDt("sysdate");
				
				queryQueue.update(wfFormGrpBVo);
				
			}else{
				String grpPid=!paramMap.containsKey("grpPid") || ((String)paramMap.get("grpPid")).isEmpty() ? "ROOT" : (String)paramMap.get("grpPid");
				grpId=wfCmSvc.createId("WF_FORM_GRP_B");
				// 순서 구하기
				WfFormGrpBVo sortVo = new WfFormGrpBVo();
				sortVo.setCompId(compId);
				// 회사 ID 조회조건 추가[계열사 설정 확인]
				//wfAdmSvc.setCompAffiliateIdList(compId, langTypCd, sortVo);
				sortVo.setGrpPid(grpPid);
				sortVo.setInstanceQueryId("com.innobiz.orange.web.wf.dao.WfFormGrpBDao.maxWfFormGrpB");
				
				Integer sortOrdr=commonSvc.queryInt(sortVo);
				if(sortOrdr==null)
					sortOrdr=1;
				
				// 부모 그룹ID 조회
				if(grpPid==null || "ROOT".equals(grpPid)){
					wfFormGrpBVo.setCompId(compId);
				}else{
					WfFormGrpBVo parentVo = wfAdmSvc.getWfFormGrpBVo(grpPid, langTypCd);
					wfFormGrpBVo.setCompId(parentVo.getCompId());
				}
				wfFormGrpBVo.setGrpPid(grpPid);
				wfFormGrpBVo.setRescId(whRescBVo.getRescId());
				wfFormGrpBVo.setRegrUid(userVo.getUserUid());	
				wfFormGrpBVo.setRegDt("sysdate");
				wfFormGrpBVo.setGrpId(grpId);
				wfFormGrpBVo.setSortOrdr(sortOrdr.intValue());
				queryQueue.insert(wfFormGrpBVo);
			}
			
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, WfConstant.GRP);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(WfConstant.GRP);
						
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("grpId", grpId);
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] - 삭제 */
	@RequestMapping(value = "/wf/adm/form/transGrpDelAjx")
	public String transGrpDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			JSONArray jsonArray = (JSONArray) object.get("grpIds");
			if (jsonArray == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			String compId = userVo.getCompId();
			QueryQueue queryQueue = new QueryQueue();
			
			String grpId;
			for(int i=0;i<jsonArray.size();i++){
				grpId = (String)jsonArray.get(i);
				if(grpId==null || grpId.isEmpty()) continue;				
				wfAdmSvc.deleteGrpList(queryQueue, compId, grpId);
			}
			
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, WfConstant.GRP);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(WfConstant.GRP);
			
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
	
	
	/** [AJAX] - 순서변경 */
	@RequestMapping(value = "/wf/adm/form/transGrpMoveAjx")
	public String transGrpMoveAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		//
		// 폴더 및 메뉴의 순서 목적으로 만들었는데
		// 메뉴의 순서는 저장하면서 한번에 바뀌므로 실제로
		// grpIds - 여기에 한개의 폴더 아이디만 들어옴
		//
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		
		JSONArray jsonArray = (JSONArray)jsonObject.get("grpIds");
		String direction = (String)jsonObject.get("direction");
		
		QueryQueue queryQueue = new QueryQueue();
		PtxSortOrdrChnVo ptxSortOrdrChnVo;
		
		if(direction==null){
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			if(direction==null) LOGGER.error("Menu move(up/down) - direction==null : "+msg);
			model.put("message", msg);
			return JsonUtil.returnJson(model);
		}
			
		WfFormGrpBVo wfFormGrpBVo = new WfFormGrpBVo(), storedWfFormGrpBVo;
		// 위로 이동
		if("up".equals(direction) || "tup".equals(direction)){
			
			// curOrdr - 현재순번
			// stdOrdr - 기준순번 - 현재순번과 기준순번이 같으면 옮길수 없는 것으로 패스
			Integer curOrdr, stdOrdr=1, switchOrdr;
			wfFormGrpBVo = new WfFormGrpBVo();
			
			String grpId, mdPid=null;
			for(int i=0;i<jsonArray.size();i++){
				grpId = (String)jsonArray.get(i);
				
				wfFormGrpBVo.setGrpId(grpId);
				storedWfFormGrpBVo = (WfFormGrpBVo)commonSvc.queryVo(wfFormGrpBVo);
				if(storedWfFormGrpBVo==null){
					//cm.msg.noData=해당하는 데이터가 없습니다.
					String msg = messageProperties.getMessage("cm.msg.noData", request);
					model.put("message", msg);
					LOGGER.error(msg+" : CANNOT MOVE-UP - grpId:"+grpId);
					return JsonUtil.returnJson(model);
				}
				curOrdr = Integer.valueOf(storedWfFormGrpBVo.getSortOrdr());
				mdPid = storedWfFormGrpBVo.getGrpPid();
				
				if(stdOrdr==curOrdr){
					stdOrdr++;
					continue;
				}
				switchOrdr = curOrdr-1;
				
				ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
				ptxSortOrdrChnVo.setTabId("WF_FORM_GRP_B");
				ptxSortOrdrChnVo.setPkCol("GRP_PID");
				ptxSortOrdrChnVo.setPk(storedWfFormGrpBVo.getGrpPid());
				ptxSortOrdrChnVo.setChnVa(1);
				ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
				if("tup".equals(direction)){
					switchOrdr = 1;
					ptxSortOrdrChnVo.setLessThen(curOrdr-1);
				}else{
					switchOrdr = curOrdr-1;
					ptxSortOrdrChnVo.setLessThen(switchOrdr);
				}
				ptxSortOrdrChnVo.setMoreThen(switchOrdr);
				queryQueue.update(ptxSortOrdrChnVo);
				storedWfFormGrpBVo = new WfFormGrpBVo();
				storedWfFormGrpBVo.setGrpId(grpId);
				storedWfFormGrpBVo.setSortOrdr(switchOrdr);
				queryQueue.update(storedWfFormGrpBVo);
			}
			
			if(!queryQueue.isEmpty()){

				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, WfConstant.GRP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(WfConstant.GRP);
				
				// 리로드 하지 않고 스크립트로 폴더의 순서만 변경하려고 데이터 조회하여 grpIds 에 세팅함
				wfFormGrpBVo = new WfFormGrpBVo();
				wfFormGrpBVo.setGrpPid(mdPid);
				wfFormGrpBVo.setOrderBy("SORT_ORDR");
				
				@SuppressWarnings("unchecked")
				List<WfFormGrpBVo> wfFormGrpBVoList = (List<WfFormGrpBVo>)commonSvc.queryList(wfFormGrpBVo);
				int i, size = wfFormGrpBVoList==null ? 0 : wfFormGrpBVoList.size();
				List<String> grpIdList = new ArrayList<String>();
				for(i=0;i<size;i++){
					grpIdList.add(wfFormGrpBVoList.get(i).getGrpId());
				}
				model.put("grpIds", grpIdList);
				
			} else {
				//cm.msg.nodata.moveup=위로 이동할 항목이 없습니다.
				model.put("message",  messageProperties.getMessage("cm.msg.nodata.moveup", request));
			}
			return JsonUtil.returnJson(model);
			
			// 아래로 이동
		} else if("down".equals(direction) || "tdown".equals(direction)){
			
			String mdPid = (String)jsonObject.get("mdPid");
			
			ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
			ptxSortOrdrChnVo.setTabId("WF_FORM_GRP_B");
			ptxSortOrdrChnVo.setPkCol("GRP_PID");
			ptxSortOrdrChnVo.setPk(mdPid);
			ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.selectMaxSortOrdr");
			Integer maxSortOrdr = commonSvc.queryInt(ptxSortOrdrChnVo);
			
			// curOrdr - 현재순번
			// stdOrdr - 기준순번 - 현재순번과 기준순번이 같으면 옮길수 없는 것으로 패스
			Integer curOrdr, stdOrdr=maxSortOrdr, switchOrdr;
			wfFormGrpBVo = new WfFormGrpBVo();
			
			String grpId;
			for(int i=jsonArray.size()-1;i>=0;i--){
				grpId = (String)jsonArray.get(i);
				
				wfFormGrpBVo.setGrpId(grpId);
				storedWfFormGrpBVo = (WfFormGrpBVo)commonSvc.queryVo(wfFormGrpBVo);
				if(storedWfFormGrpBVo==null){
					//cm.msg.noData=해당하는 데이터가 없습니다.
					String msg = messageProperties.getMessage("cm.msg.noData", request);
					model.put("message", msg);
					LOGGER.error(msg+" : CANNOT MOVE-DOWN - grpId:"+grpId);
					return JsonUtil.returnJson(model);
				}
				curOrdr = Integer.valueOf(storedWfFormGrpBVo.getSortOrdr());
				
				if(stdOrdr==curOrdr){
					stdOrdr--;
					continue;
				}
				switchOrdr = curOrdr+1;
				
				ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
				ptxSortOrdrChnVo.setTabId("WF_FORM_GRP_B");
				ptxSortOrdrChnVo.setPkCol("GRP_PID");
				ptxSortOrdrChnVo.setPk(storedWfFormGrpBVo.getGrpPid());
				ptxSortOrdrChnVo.setChnVa(-1);
				ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
				
				//맨아래 일경우 기준순번을 최대 정렬 순서로 변경한다.			
				if("tdown".equals(direction)){
					switchOrdr = maxSortOrdr.intValue();
					ptxSortOrdrChnVo.setMoreThen(curOrdr+1);
					ptxSortOrdrChnVo.setLessThen(maxSortOrdr.intValue());
				}else{
					switchOrdr = curOrdr+1;
					ptxSortOrdrChnVo.setMoreThen(switchOrdr);
					ptxSortOrdrChnVo.setLessThen(switchOrdr);
				}
				queryQueue.update(ptxSortOrdrChnVo);
				storedWfFormGrpBVo = new WfFormGrpBVo();
				storedWfFormGrpBVo.setGrpId(grpId);
				storedWfFormGrpBVo.setSortOrdr(switchOrdr);
				queryQueue.update(storedWfFormGrpBVo);
			}
			
			if(!queryQueue.isEmpty()){

				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, WfConstant.GRP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(WfConstant.GRP);
				
				// 리로드 하지 않고 스크립트로 폴더의 순서만 변경하려고 데이터 조회하여 grpIds 에 세팅함
				wfFormGrpBVo = new WfFormGrpBVo();
				wfFormGrpBVo.setGrpPid(mdPid);
				wfFormGrpBVo.setOrderBy("SORT_ORDR");
				
				@SuppressWarnings("unchecked")
				List<WfFormGrpBVo> wfFormGrpBVoList = (List<WfFormGrpBVo>)commonSvc.queryList(wfFormGrpBVo);
				int i, size = wfFormGrpBVoList==null ? 0 : wfFormGrpBVoList.size();
				List<String> grpIdList = new ArrayList<String>();
				for(i=0;i<size;i++){
					grpIdList.add(wfFormGrpBVoList.get(i).getGrpId());
				}
				
				model.put("grpIds", grpIdList);
				
			} else {
				//cm.msg.nodata.movedown=아래로 이동할 항목이 없습니다.
				model.put("message",  messageProperties.getMessage("cm.msg.nodata.movedown", request));
			}
			return JsonUtil.returnJson(model);
			
		} else {
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			LOGGER.error("Code move(up/down) - direction=="+direction+" : "+msg);
			model.put("message", msg);
			return JsonUtil.returnJson(model);
		}

	}
	
	
	
	/** 목록 조회(오른쪽 프레임) */
	@RequestMapping(value = "/wf/adm/form/listFormFrm")
	public String listMdFrm(HttpServletRequest request,
			@Parameter(name="grpId", required=false) String grpId,
			ModelMap model) throws Exception {
		
		// 요청경로 세팅
		wfCmSvc.getRequestPath(request, model , null);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 테이블관리 기본(WL_TASK_LOG_B) 테이블 - BIND
		WfFormBVo wfFormBVo = new WfFormBVo();
		VoUtil.bind(request, wfFormBVo);
		wfFormBVo.setQueryLang(langTypCd);
		wfFormBVo.setCompId(userVo.getCompId());
		
		if(grpId!=null && "ROOT".equals(grpId)) wfFormBVo.setGrpId(null);
		
		Integer recodeCount = commonSvc.count(wfFormBVo);
		PersonalUtil.setPaging(request, wfFormBVo, recodeCount);
		
		@SuppressWarnings("unchecked")
		List<WfFormBVo> wfFormBVoList = (List<WfFormBVo>) commonSvc.queryList(wfFormBVo);
		
		model.put("recodeCount", recodeCount);
		model.put("wfFormBVoList", wfFormBVoList);
		
		// 설정해야할 모듈 목록
		List<PtCdBVo> pageRecSetupCdList = ptCmSvc.getCdList("PAGE_REC_SETUP_CD", langTypCd, "Y");
		Map<String,String> pageRecSetupCdMap = new HashMap<String, String>();
		for(PtCdBVo storedPtCdBVo : pageRecSetupCdList){
			pageRecSetupCdMap.put(storedPtCdBVo.getCd(), storedPtCdBVo.getRescNm());
		}
		
		model.put("pageRecSetupCdMap", pageRecSetupCdMap);
				
				
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if("Y".equals(sysPlocMap.get("mobileEnable"))){
			model.put("mobileEnable", "Y");
		}
		
		return LayoutUtil.getJspPath("/wf/adm/form/listFormFrm");
	}
	
	/** 양식[기본정보] */
	@RequestMapping(value = "/wf/adm/form/setFormFrm")
	public String setForm(HttpServletRequest request,
			@RequestParam(value = "formNo", required = false) String formNo,
			ModelMap model) throws Exception {
		
		// 요청경로 세팅
		wfCmSvc.getRequestPath(request, model , null);
		
		if(formNo!=null && !formNo.isEmpty()){
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			WfFormBVo wfFormBVo = new WfFormBVo();
			wfFormBVo.setCompId(userVo.getCompId());
			wfFormBVo.setQueryLang(langTypCd);
			wfFormBVo.setFormNo(formNo);
			wfFormBVo=(WfFormBVo)commonSvc.queryVo(wfFormBVo);
			
			if(wfFormBVo==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			if (wfFormBVo.getRescId() != null) {
				// 리소스기본(WL_RESC_B) 테이블 - 조회, 모델에 추가
				wfRescSvc.queryRescBVo(wfFormBVo.getRescId(), model);
			}
			
			model.put("wfFormBVo", wfFormBVo);
			
			// 양식 사용여부[데이터 등록]
			model.put("isUseForm", wfFormSvc.isUseForm(langTypCd, formNo));
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
					
		// 설정해야할 모듈 목록
		List<PtCdBVo> pageRecSetupCdList = ptCmSvc.getCdList("PAGE_REC_SETUP_CD", langTypCd, "Y");
		model.put("pageRecSetupCdList", pageRecSetupCdList);
				
		return LayoutUtil.getJspPath("/wf/adm/form/setForm", "Frm");
	}
	
	/** 양식[기본정보] - 저장 */
	@RequestMapping(value = "/wf/adm/form/transFormFrm", method = RequestMethod.POST)
	public String transForm(HttpServletRequest request,
			ModelMap model) {
		
		try {
			// 요청경로 세팅
			String path = wfCmSvc.getRequestPath(request, model , null);
			
			// parameters
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			String viewPage = ParamUtil.getRequestParam(request, "viewPage", true);
			String formNo = ParamUtil.getRequestParam(request, "formNo", false);
			
			if (listPage.isEmpty() || viewPage.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			QueryQueue queryQueue = new QueryQueue();
			
			// 양식 기본 저장
			wfAdmSvc.saveForm(request, queryQueue, null, null, formNo);
			
			commonSvc.execute(queryQueue);

			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
	        
			String page = formNo==null || formNo.isEmpty() ? listPage : viewPage;
			
			if(path.endsWith("Frm"))
				model.put("todo", "parent.reloadForm('"+page+"');");
			else
				model.put("todo", "parent.location.replace('" + page + "');");

		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** [팝업] 양식 이동 */
	@RequestMapping(value = "/wf/adm/form/setMoveToFormGrpPop")
	public String setMoveToFormGrpPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/wf/adm/form/setMoveToFormGrpPop");
	}
	
	/** [AJAX] - 양식 이동 저장 */
	@RequestMapping(value = "/wf/adm/form/transMoveToFormGrpAjx")
	public String transMoveToFormGrpAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String grpId = (String) object.get("grpId");
			String formNos = (String) object.get("formNos");
			if ((grpId==null || grpId.isEmpty() || formNos==null || formNos.isEmpty())) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			QueryQueue queryQueue = new QueryQueue();
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			String[] nos = formNos.split(",");
			for(String no : nos){
				no=no.trim();
				wfFormSvc.moveFormGrp(request, queryQueue, userVo, no, grpId);
			}
			
			if(!queryQueue.isEmpty())
				commonSvc.execute(queryQueue);
			
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
	
	/** [AJAX] - 양식 삭제 */
	@RequestMapping(value = "/wf/adm/form/transFormDelAjx")
	public String transFormDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String formNo = (String) object.get("formNo");
			JSONArray formNoArray = (JSONArray) object.get("formNos");
			if (formNo == null && formNoArray == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			QueryQueue queryQueue = new QueryQueue();
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			List<String> formNoList=new ArrayList<String>();
			
			// 단건 삭제
			if(formNo!=null && !formNo.isEmpty())
				wfFormSvc.deleteForm(request, queryQueue, userVo, formNo, formNoList);
			
			// 복수 삭제
			if(formNoArray!=null && !formNoArray.isEmpty()){
				for(int i=0;i<formNoArray.size();i++){
					formNo = (String)formNoArray.get(i);
					wfFormSvc.deleteForm(request, queryQueue, userVo, formNo, formNoList);
				}
			}
			
			if(!queryQueue.isEmpty())
				commonSvc.execute(queryQueue);
			
			if(formNoList.size()>0){
				try{
					wfGenSvc.dropTables(formNoList);
				}catch(SQLException sqe){
					LOGGER.debug(sqe.getMessage());
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
	
	/** [AJAX] - 양식 사용여부 */
	@RequestMapping(value = "/wf/adm/form/getUseFormAjx")
	public String getUseFormAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String formNo = (String) object.get("formNo");
			JSONArray formNoArray = (JSONArray) object.get("formNos");
			if (formNo == null && formNoArray == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			List<Map<String, String>> useFormList=new ArrayList<Map<String, String>>();
			
			// 단건 체크
			if(formNo!=null && !formNo.isEmpty())
				wfFormSvc.getUseFormList(langTypCd, formNo, useFormList);
			
			// 복수 체크
			if(formNoArray!=null && !formNoArray.isEmpty()){
				for(int i=0;i<formNoArray.size();i++){
					formNo = (String)formNoArray.get(i);
					wfFormSvc.getUseFormList(langTypCd, formNo, useFormList);
				}
			}
			
			if(useFormList.size()>0){
				String jsonString = JsonUtil.toJson(useFormList);
				model.put("formJsonString", jsonString);
				model.put("result", "use");
			}else{
				model.put("result", "ok");
			}
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] - 양식 사용여부 [데이터가 1건 이상일 경우 true 리턴, formTyp 이 'R'(본문삽입) 일 경우 첨부파일 사용여부도 리턴]*/
	@RequestMapping(value = "/wf/adm/form/chkUseFormAjx")
	public String chkUseFileAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String formNo = (String) object.get("formNo");
			if (formNo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 양식 사용여부
			model.put("formUseYn", wfFormSvc.isUseForm(langTypCd, formNo) ? "Y" : "N");
			
			// 양식구분[R:본문삽입, A:메뉴등록]
			String formTyp = (String) object.get("formTyp");
			
			if(formTyp!=null && "R".equals(formTyp)){
				// 배포할 테이블 컬럼목록 조회
				List<WfFormColmLVo> colmVoList=wfAdmSvc.getColmVoList(formNo, "Y");
				
				String fileUseYn="N";
				for(WfFormColmLVo storedWfFormColmLVo : colmVoList){
					if("file".equals(storedWfFormColmLVo.getColmTypCd())){
						fileUseYn="Y";
						break;
					}
				}
				model.put("fileUseYn", fileUseYn);
			}
			
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	
	/** 양식[등록화면] - 구성 */
	@RequestMapping(value = "/wf/adm/form/setRegForm")
	public String setRegForm(HttpServletRequest request,
			@RequestParam(value = "genId", required = false) String genId,
			@RequestParam(value = "formNo", required = false) String formNo,
			@RequestParam(value = "paramGenId", required = false) String paramGenId,
			@RequestParam(value = "paramFormNo", required = false) String paramFormNo,
			ModelMap model) throws Exception {
		
		// 양식번호 체크
		if(formNo==null || formNo.isEmpty()){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		WfFormBVo wfFormBVo = wfFormSvc.getWfFormBVo(null, null, formNo, null);
		
		if(wfFormBVo==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		model.put("wfFormBVo", wfFormBVo);
		
		// 다른 양식 파라미터 여부
		boolean isParam=paramFormNo!=null && !paramFormNo.isEmpty() && paramGenId!=null && !paramGenId.isEmpty();
		
		// 양식등록상세 조회
		WfFormRegDVo wfFormRegDVo = wfAdmSvc.getWfFormRegDVo(paramGenId, isParam ? paramFormNo : formNo, isParam ? true : false, null);
		model.put("wfFormRegDVo", wfFormRegDVo);
		
		if(!isParam){
			// 양식 컬럼 목록
			WfFormColmLVo wfFormColmLVo= new WfFormColmLVo();
			wfFormColmLVo.setFormNo(formNo);
			@SuppressWarnings("unchecked")
			List<WfFormColmLVo> wfFormColmLVoList=(List<WfFormColmLVo>)commonSvc.queryList(wfFormColmLVo);
			if(wfFormColmLVoList!=null){
				Map<String, String> wfFormColmLVoMap = new HashMap<String, String>();
				for(WfFormColmLVo storedWfFormColmLVo : wfFormColmLVoList){
					wfFormColmLVoMap.put(storedWfFormColmLVo.getColmNm().replaceAll("_", ""), storedWfFormColmLVo.getColmId());
				}
				model.put("wfFormColmLVoMap", wfFormColmLVoMap);
			}
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
		// 기본으로 설정된 목록 컬럼 조회
		//List<WfFormColmLVo> wfFormColmLVoList = wfAdmSvc.getCurrColmVoList(genId, formNo, null);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
					
		// 양식 코드 목록 세팅
		wfCdSvc.setAllFormCdListMap(model, langTypCd, userVo.getCompId(), "Y", true);
				
		// 양식 코드 목록 세팅
		//wfCdSvc.setFormCdListMap(model, wfFormColmLVoList, wfFormRegDVo, langTypCd);
				
		// 이력조회 버튼
		wfFormRegDVo = new WfFormRegDVo();
		wfFormRegDVo.setFormNo(formNo);
		wfFormRegDVo.setHst(true);
		if(commonSvc.count(wfFormRegDVo)>1){
			model.put("hstBtn", Boolean.TRUE);
		}
				
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if("Y".equals(sysPlocMap.get("mobileEnable"))){
			model.put("mobileEnable", "Y");
		}
		
		// 어권별 폰트 조회
		String[] fonts = ptSysSvc.getFontFamilyByLang(langTypCd);
		if(fonts!=null){
			model.put("fontFamilies", fonts);
		}
		
		// 폰트 사이즈 조회
		String[] sizes = ptSysSvc.getFontSizeArray();
		model.put("fontSizes", sizes);
		
				
		return LayoutUtil.getJspPath("/wf/adm/form/setRegForm");
	}
	
	/** [팝업] 레이아웃 설정 */
	@RequestMapping(value = "/wf/adm/form/setLoutPop")
	public String setLoutPop(HttpServletRequest request,
			@Parameter(name="formNo", required=false) String formNo,
			@Parameter(name="seq", required=false) String seq,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/wf/adm/form/setLoutPop");
	}
	
	/** [팝업] 셀 설정 */
	@RequestMapping(value = "/wf/adm/form/setCellPop")
	public String setCellPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/wf/adm/form/setCellPop");
	}
	
	/** [AJAX] - 등록 폼 저장 */
	@RequestMapping(value = "/wf/adm/form/transFormAjx")
	public String transFormAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			
			// Json to Map
			Map<String, Object> paramMap = JsonUtil.jsonToMap(object);
			
			// 양식번호 체크
			if(!paramMap.containsKey("formNo") || ((String)paramMap.get("formNo")).isEmpty()){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			String formNo=(String)paramMap.get("formNo");
			WfFormBVo wfFormBVo = new WfFormBVo();
			wfFormBVo.setCompId(userVo.getCompId());
			wfFormBVo.setQueryLang(langTypCd);
			wfFormBVo.setFormNo(formNo);
			wfFormBVo=(WfFormBVo)commonSvc.queryVo(wfFormBVo);
			
			if(wfFormBVo==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			
			WfFormRegDVo wfFormRegDVo = new WfFormRegDVo();
			
			// jsonMap => Vo
			FormUtil.setParamToVo(paramMap, wfFormRegDVo, null);
			
			queryQueue.store(wfFormRegDVo);
						
			// 양식 컬럼 목록
			WfFormColmLVo wfFormColmLVo= null;
			// 저장할 컬럼 목록
			boolean isSaveColmList=paramMap.containsKey("colmList") && paramMap.get("colmList")!=null;
			
			// 적용여부 - 컬럼 갯수가 달라지지 않고 옵션만 변경된 경우에는 배포버전 수정
			boolean isApply=false;
			
			// 컬럼
			if(isSaveColmList){
				String colmList=(String)paramMap.get("colmList");
				colmList=colmList.replaceAll("\\\\","");
				try{
					JSONParser parser = new JSONParser();
					JSONArray jsonArray = (JSONArray)parser.parse(colmList);
					if(jsonArray!=null && !jsonArray.isEmpty()){
						
						JSONObject json;
						
						// 현재 테이블 컬럼목록 조회
						List<WfFormColmLVo> colmVoList=wfAdmSvc.getColmVoList(formNo, null);
						List<String> dbColmList=new ArrayList<String>();
						for(WfFormColmLVo storedWfFormColmLVo : colmVoList){
							dbColmList.add(storedWfFormColmLVo.getColmNm());
						}						
						
						List<String> jsonColmList=new ArrayList<String>();
						
						int i, size = jsonArray==null ? 0 : jsonArray.size();
						
						// 양식 컬럼 목록 저장
						for(i=0;i<size;i++){
							wfFormColmLVo = new WfFormColmLVo();
							json=(JSONObject)jsonArray.get(i);
							//if(ArrayUtil.isInArray(WfConstant.EXCLUDE_COLM_LIST, (String)json.get("colmNm"))) continue;
							jsonColmList.add((String)json.get("colmNm"));
						}
						
						// db에 저장된 컬럼 목록과 저장할 목록이 동일하지 않을 경우에만 저장
						if(!(dbColmList.containsAll(jsonColmList) && jsonColmList.containsAll(dbColmList))){
							// 양식 컬럼 목록 삭제
							wfFormColmLVo= new WfFormColmLVo();
							wfFormColmLVo.setFormNo(formNo);
							queryQueue.delete(wfFormColmLVo);
							
							// Json to Map
							Map<String, Object> colmMap;
							
							// 기존 컬럼ID 목록
							List<String> updateColmIdList=new ArrayList<String>();
							int sortOrdr=0;
							// 양식 컬럼 목록 저장
							for(i=0;i<size;i++){
								wfFormColmLVo = new WfFormColmLVo();
								json=(JSONObject)jsonArray.get(i);
								colmMap = JsonUtil.jsonToMap(json);
								
								// jsonMap => Vo
								FormUtil.setParamToVo(colmMap, wfFormColmLVo, null);
								wfFormColmLVo.setFormNo(formNo);
								if(wfFormColmLVo.getColmId()==null || wfFormColmLVo.getColmId().isEmpty()){
									wfFormColmLVo.setColmId(wfCmSvc.createId("WF_FORM_COLM_L"));
								}else{
									updateColmIdList.add(wfFormColmLVo.getColmId());
								}
								
								wfFormColmLVo.setSortOrdr(++sortOrdr);
								wfFormColmLVo.setUseYn("Y");
								wfFormColmLVo.setLstYn("Y");
								queryQueue.insert(wfFormColmLVo);
							}
							
							// 목록컬럼중에 삭제된 컬럼이 있으면 목록컬럼 삭제
							boolean isDiff=!jsonColmList.containsAll(dbColmList);
							if(isDiff){
								// 데이터 컬럼 조회[배포 버전이 아닌 현재 버전]
								List<WfFormLstDVo> wfFormLstDVoList = wfAdmSvc.getWfFormLstDVoList(null, null, formNo, null, null, false);
								// 목록 컬럼ID
								List<String> lstColmIdList=new ArrayList<String>();
								for(WfFormLstDVo storedWfFormLstDVo : wfFormLstDVoList){
									if(!lstColmIdList.contains(storedWfFormLstDVo.getColmId()))
										lstColmIdList.add(storedWfFormLstDVo.getColmId());
								}
								// HashSet 으로 중복ID 제거
								Set<String> hs = new HashSet<String>(lstColmIdList);
								lstColmIdList = new ArrayList<String>(hs);
								
								if(!updateColmIdList.containsAll(lstColmIdList)){
									// 삭제된 컬럼ID 목록
									List<String> diffColmIdList=new ArrayList<String>();
									boolean isDelColm;
									for(String colmId : lstColmIdList){
										isDelColm=true;
										for(String updateColmId : updateColmIdList){
											if(colmId.equals(updateColmId)){
												isDelColm=false;
												break;
											}
										}
										if(isDelColm) diffColmIdList.add(colmId);
									}
									
									// 양식 목록 데이터 삭제
									if(diffColmIdList.size()>0){
										WfFormLstDVo wfFormLstDVo;
										for(String colmId : diffColmIdList){
											wfFormLstDVo= new WfFormLstDVo();
											wfFormLstDVo.setColmId(colmId);
											wfFormLstDVo.setFormNo(formNo);
											queryQueue.delete(wfFormLstDVo);
										}
									}
								}
							}							
						}else{
							isApply=true; // 컬럼 갯수가 기존과 같으면(옵션만 변경) 배포 버전 수정
						}
					}
				}catch(ParseException pe){
					pe.printStackTrace();
				}
			}else{
				// 양식 컬럼 목록 삭제
				wfFormColmLVo= new WfFormColmLVo();
				wfFormColmLVo.setFormNo(formNo);
				queryQueue.delete(wfFormColmLVo);
				
				// 양식 목록 데이터 삭제
				WfFormLstDVo wfFormLstDVo= new WfFormLstDVo();
				wfFormLstDVo.setFormNo(formNo);
				queryQueue.delete(wfFormLstDVo);
			}
			
			// 생성ID
			String genId = wfFormBVo.getGenId();
			
			// 생성 여부[최초 배포]
			boolean isDeploy = genId!=null && !genId.isEmpty();
			
			// 등록여부 'Y' 로 저장
			wfFormBVo = new WfFormBVo();
			wfFormBVo.setCompId(userVo.getCompId());
			wfFormBVo.setFormNo(formNo);
			wfFormBVo.setRegYn("Y");
			wfFormBVo.setStatCd(isDeploy && isApply ? "D" : "U"); // 상태코드[D:배포, U:수정]
			
			queryQueue.update(wfFormBVo);
			
			
			// 이미 배포되었으면서 옵션만 변경될 경우 배포 버전 수정
			if(isDeploy && isApply){
				WfFormRegDVo newWfFormRegDVo = new WfFormRegDVo();
				// 등록화면 이력 을 현재 데이터로 변환
				BeanUtils.copyProperties(wfFormRegDVo, newWfFormRegDVo, new String[]{"genId"});
				
				newWfFormRegDVo.setGenId(genId);
				newWfFormRegDVo.setHst(true);
				
				queryQueue.update(newWfFormRegDVo);
			}
			
			// 모바일 화면 저장
			if(paramMap.containsKey("loutMobVa") || paramMap.containsKey("tabMobVa")){
				// 모바일등록상세 저장
				wfFormRegDVo = new WfFormRegDVo();
				wfFormRegDVo.setMob(true); // 모바일여부
				wfFormRegDVo.setLoutVa((String)paramMap.get("loutMobVa"));
				wfFormRegDVo.setTabVa((String)paramMap.get("tabMobVa"));
				wfFormRegDVo.setFormNo(formNo);
				queryQueue.store(wfFormRegDVo);
			}
						
						
			commonSvc.execute(queryQueue);
			
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
	
	/** [팝업] - 이력 or 미리보기 */
	@RequestMapping(value = {"/wf/adm/form/previewRegFormPop", "/wf/adm/form/viewRegFormPop", "/wf/adm/form/allRegFormPop"})
	public String viewRegFormPop(HttpServletRequest request,
			@Parameter(name="formNo", required=false) String formNo,
			ModelMap model) throws Exception {
		
		// 전체보기 여부
		boolean isAll=request.getRequestURI().startsWith("/wf/adm/form/all");
		
		// 이력보기 여부
		boolean isHst=request.getRequestURI().startsWith("/wf/adm/form/view");
		
		if(!isAll && (formNo==null || formNo.isEmpty())){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
					
		if(isHst){
			// 등록화면 이력 조회
			List<WfFormRegDVo> wfFormRegDVoList = wfAdmSvc.getWfFormRegDVoList(formNo);
			model.put("wfFormRegDVoList", wfFormRegDVoList);
			
			if(wfFormRegDVoList!=null && wfFormRegDVoList.size()>0){
				model.put("firstGenId", wfFormRegDVoList.get(0).getGenId());
			}
			model.put("isHst", Boolean.TRUE);
		}else if(isAll){
			// parameters
			String grpId = ParamUtil.getRequestParam(request, "grpId", false);
			
			// 그룹 목록 세팅
			wfAdmSvc.setParamGrpList(request, model, userVo.getCompId(), langTypCd, grpId, "Y");
			
			model.put("isAll", Boolean.TRUE);
		}else{
			model.put("isPreview", Boolean.TRUE);
		}
		
		// 양식 코드 목록 세팅
		wfCdSvc.setAllFormCdListMap(model, langTypCd, null, "Y", true);
				
		return LayoutUtil.getJspPath("/wf/adm/form/viewRegFormPop");
	}
	
	/** [AJAX] - 그룹 목록 불러오기 */
	@RequestMapping(value = "/wf/adm/form/getGrpListAjx")
	public String getGrpListAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String grpPid = (String) object.get("grpPid");
			if (grpPid == null && grpPid == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			Map<String, Object> paramMap = JsonUtil.jsonToMap(object);
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			String compId=userVo.getCompId();
			WfFormGrpBVo wfFormGrpBVo=wfAdmSvc.getWfFormGrpBVo(grpPid, langTypCd);
			if(wfFormGrpBVo==null){
				model.put("result", "end");
				return JsonUtil.returnJson(model);
			}
			
			// 하위 모듈 조회
			wfFormGrpBVo = new WfFormGrpBVo();
			// jsonMap => Vo
			FormUtil.setParamToVo(paramMap, wfFormGrpBVo, null);
			wfFormGrpBVo.setUseYn("Y"); // 사용중인 것만 조회
			model.put("wfFormGrpBVoList", wfAdmSvc.getGrpList(wfFormGrpBVo, compId, langTypCd, grpPid, "Y"));
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] - 양식 목록 불러오기 */
	@RequestMapping(value = "/wf/adm/form/getFormListAjx")
	public String getFormListAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String grpId = (String) object.get("grpId");
			if (grpId == null && grpId == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			model.put("wfFormBVoList", wfFormSvc.getWfFormBVoList(userVo.getCompId(), langTypCd, "Y", grpId, true));
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] - 양식 이력 불러오기 [배포기준]*/
	@RequestMapping(value = "/wf/adm/form/getFormHstAjx")
	public String getFormHstAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String formNo = (String) object.get("formNo");
			if (formNo == null && formNo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 등록화면 이력 조회
			List<WfFormRegDVo> wfFormRegDVoList = wfAdmSvc.getWfFormRegDVoList(formNo);
			model.put("wfFormRegDVoList", wfFormRegDVoList);
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	
	/** [프레임] - 등록화면 미리보기 or 이력보기 */
	@RequestMapping(value = "/wf/adm/form/viewRegFormFrm")
	public String viewRegFormFrm(HttpServletRequest request,
			@Parameter(name="genId", required=false) String genId,
			@Parameter(name="formNo", required=false) String formNo,
			ModelMap model) throws Exception {
		
		if(formNo==null || formNo.isEmpty()){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 프레임 여부
		boolean isPreview=genId==null || genId.isEmpty();
		
		WfFormRegDVo wfFormRegDVo=null;
		if(isPreview){
			wfFormRegDVo=WfConstant.getPreviewVo();
			model.put("wfFormRegDVo", wfFormRegDVo);
		}else{
			wfFormRegDVo = wfAdmSvc.getWfFormRegDVo(genId, formNo, true, null);
			if(wfFormRegDVo==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			model.put("wfFormRegDVo", wfFormRegDVo);
			model.put("isHst", Boolean.TRUE);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 기본으로 설정된 목록 컬럼 조회
		List<WfFormColmLVo> wfFormColmLVoList = wfAdmSvc.getCurrColmVoList(genId, formNo, null);
		
		// 양식 코드 목록 세팅
		wfCdSvc.setFormCdListMap(model, wfFormColmLVoList, wfFormRegDVo, langTypCd);
					
		return LayoutUtil.getJspPath("/wf/adm/form/viewRegFormFrm");
	}
	
	/** [AJAX] - 선택한 양식 이력을 현재 버전으로 대체 */
	@RequestMapping(value = "/wf/adm/form/transRegFormAjx")
	public String transRegFormAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String genId = (String) object.get("genId");
			String formNo = (String) object.get("formNo");
			
			if (genId == null || genId.isEmpty() || formNo == null || formNo.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			WfFormRegDVo wfFormRegDVo = wfAdmSvc.getWfFormRegDVo(genId, formNo, true, null);
			if(wfFormRegDVo==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			WfFormRegDVo newWfFormRegDVo = new WfFormRegDVo();
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 등록화면 이력 을 현재 데이터로 변환
			BeanUtils.copyProperties(wfFormRegDVo, newWfFormRegDVo, new String[]{"genId"});
			queryQueue.update(newWfFormRegDVo);
			
			
			// 목록 컬럼 복사
			// 이력 데이터
			List<WfFormColmLVo> wfFormColmLVoList =  wfAdmSvc.getCurrColmVoList(genId, formNo, null);
			// 양식 컬럼 목록 삭제
			WfFormColmLVo wfFormColmLVo= new WfFormColmLVo();
			wfFormColmLVo.setFormNo(wfFormRegDVo.getFormNo());
			queryQueue.delete(wfFormColmLVo);
			
			if(wfFormColmLVoList!=null && wfFormColmLVoList.size()>0){
				for(WfFormColmLVo storedWfFormColmLVo : wfFormColmLVoList){
					queryQueue.insert(storedWfFormColmLVo);
				}
			}
			
			// 양식 목록 데이터 삭제
			WfFormLstDVo wfFormLstDVo= new WfFormLstDVo();
			wfFormLstDVo.setFormNo(wfFormRegDVo.getFormNo());
			queryQueue.delete(wfFormLstDVo);
						
			// 목록 데이터 복사			
			List<WfFormLstDVo> wfFormLstDVoList =  wfAdmSvc.getWfFormLstDVoList(request, genId, formNo, null, null, true);
			if(wfFormLstDVoList!=null && wfFormLstDVoList.size()>0){
				for(WfFormLstDVo storedWfFormLstDVo : wfFormLstDVoList){
					queryQueue.insert(storedWfFormLstDVo);
				}
			}
			
			commonSvc.execute(queryQueue);
			
			// cm.msg.del.success=저장 되었습니다.
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
	
	/** [AJAX] - 미리보기 폼 저장 */
	@RequestMapping(value = "/wf/adm/form/previewFormAjx")
	public String previewFormAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			Map<String, Object> paramMap = JsonUtil.jsonToMap(object);
			
			WfFormRegDVo previewVo =WfConstant.getPreviewVo();
			if(previewVo==null) previewVo=new WfFormRegDVo();
			// jsonMap => Vo
			FormUtil.setParamToVo(paramMap, previewVo, null);
			
			WfConstant.setPreviewVo(previewVo);
			
			model.put("result", "ok");
			
		}/* catch (CmException e) {
			model.put("message", e.getMessage());
		}*/ catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** 양식 - 목록화면 구성 */
	@RequestMapping(value = "/wf/adm/form/setListForm")
	public String setListForm(HttpServletRequest request,
			@RequestParam(value = "formNo", required = false) String formNo,
			@RequestParam(value = "mdCd", required = false) String mdCd,
			ModelMap model) throws Exception {
		
		
		if(formNo==null || formNo.isEmpty()){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		if(mdCd==null || mdCd.isEmpty()) mdCd="U";
		
		// 요청경로 세팅
		wfCmSvc.getRequestPath(request, model , null);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		WfFormBVo wfFormBVo = new WfFormBVo();
		wfFormBVo.setCompId(userVo.getCompId());
		wfFormBVo.setQueryLang(langTypCd);
		wfFormBVo.setFormNo(formNo);
		wfFormBVo=(WfFormBVo)commonSvc.queryVo(wfFormBVo);
		
		if(wfFormBVo==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		
		// 양식등록상세 조회(Current)
		WfFormRegDVo wfFormRegDVo = wfAdmSvc.getWfFormRegDVo(null, formNo, false, null);
		model.put("wfFormRegDVo", wfFormRegDVo);
				
		// 양식 컬럼 목록 조회
		List<WfFormColmLVo> wfFormColmLVoList=wfAdmSvc.getWfFormColmLVoList(request, formNo, mdCd);
		
		model.put("wfFormBVo", wfFormBVo);
		model.put("wfFormColmLVoList", wfFormColmLVoList);
		
		// 선택된 목록 조회
		List<WfFormLstDVo> wfFormLstDVoList=wfAdmSvc.getWfFormLstDVoList(request, null, formNo, null, mdCd, false);
		
		if(wfFormLstDVoList==null) wfFormLstDVoList=new ArrayList<WfFormLstDVo>();
		// UI 구성용 - 빈 VO 하나 더함
		wfFormLstDVoList.add(new WfFormLstDVo());
		model.put("wfFormLstDVoList", wfFormLstDVoList);
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if("Y".equals(sysPlocMap.get("mobileEnable"))){
			model.put("mobileEnable", "Y");
		}
				
		return LayoutUtil.getJspPath("/wf/adm/form/setListForm");
	}
	
	/** 양식 - 항목 일괄 저장 */
	@RequestMapping(value = "/wf/adm/form/transColmList")
	public String transColmList(HttpServletRequest request,			
			@Parameter(name="formNo", required=false) String formNo,
			ModelMap model) throws Exception {
		
		try{
			if(formNo==null || formNo.isEmpty()){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			WfFormBVo wfFormBVo = new WfFormBVo();
			wfFormBVo.setCompId(userVo.getCompId());
			wfFormBVo.setQueryLang(langTypCd);
			wfFormBVo.setFormNo(formNo);
			wfFormBVo=(WfFormBVo)commonSvc.queryVo(wfFormBVo);
			
			if(wfFormBVo==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 양식 컬럼 목록 삭제
			WfFormColmLVo wfFormColmLVo;
						
			int i, size;
			// 양식목록상세(WF_FORM_LST_D) 테이블
			@SuppressWarnings("unchecked")
			List<WfFormColmLVo> wfFormColmLVoList = (List<WfFormColmLVo>)VoUtil.bindList(request, WfFormColmLVo.class, new String[]{"colmId"});
			size = wfFormColmLVoList==null ? 0 : wfFormColmLVoList.size();
			
			if(size>0){
				for(i=0;i<size;i++){
					wfFormColmLVo = wfFormColmLVoList.get(i);					
					if(wfFormColmLVo.getLstYn()!=null && "A".equals(wfFormColmLVo.getLstYn())) continue; // 기본컬럼 변경 제외
					wfFormColmLVo.setFormNo(formNo);
					queryQueue.update(wfFormColmLVo);
				}
			}
			
			if(!queryQueue.isEmpty()) commonSvc.execute(queryQueue);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reload();");
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** 양식 - 목록화면 일괄 저장 */
	@RequestMapping(value = "/wf/adm/form/transListForm")
	public String transListForm(HttpServletRequest request,			
			@Parameter(name="formNo", required=false) String formNo,
			@Parameter(name="mdCd", required=false) String mdCd,
			ModelMap model) throws Exception {
		
		try{
			
			if(formNo==null || formNo.isEmpty()){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			if(mdCd==null || mdCd.isEmpty()) mdCd="U";
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			WfFormBVo wfFormBVo = new WfFormBVo();
			wfFormBVo.setCompId(userVo.getCompId());
			wfFormBVo.setQueryLang(langTypCd);
			wfFormBVo.setFormNo(formNo);
			wfFormBVo=(WfFormBVo)commonSvc.queryVo(wfFormBVo);
			
			if(wfFormBVo==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 목록여부 'Y' 로 저장
			wfFormBVo = new WfFormBVo();
			wfFormBVo.setCompId(userVo.getCompId());
			wfFormBVo.setFormNo(formNo);
						
			// 기존 목록 삭제
			WfFormLstDVo wfFormLstDVo=new WfFormLstDVo();
			wfFormLstDVo.setFormNo(formNo);
			wfFormLstDVo.setMdCd(mdCd);
			queryQueue.delete(wfFormLstDVo);
			
			int i, size;
			// 양식목록상세(WF_FORM_LST_D) 테이블
			@SuppressWarnings("unchecked")
			List<WfFormLstDVo> wfFormLstDVoList = (List<WfFormLstDVo>)VoUtil.bindList(request, WfFormLstDVo.class, new String[]{"colmId"});
			size = wfFormLstDVoList==null ? 0 : wfFormLstDVoList.size();
			
			if(size>0){
				int sortOrdr = 1;
				for(i=0;i<size;i++){
					wfFormLstDVo = wfFormLstDVoList.get(i);
					wfFormLstDVo.setFormNo(formNo);
					wfFormLstDVo.setMdCd(mdCd);
					wfFormLstDVo.setSortOrdr(sortOrdr);
					queryQueue.insert(wfFormLstDVo);
					sortOrdr++;
				}
				wfFormBVo.setLstYn("Y");
			}else{
				wfFormBVo.setLstYn("N");
			}
			queryQueue.update(wfFormBVo);
			
			if(!queryQueue.isEmpty()) commonSvc.execute(queryQueue);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reload();");
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	
	/** [팝업] - 모바일 등록(상세보기) 화면구성 */
	@RequestMapping(value = "/wf/adm/form/setMobRegPop")
	public String setMobRegPop(HttpServletRequest request,
			@RequestParam(value = "formNo", required = true) String formNo,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/wf/adm/form/setMobRegPop");
	}
	
	/** [프레임] - 모바일 등록(상세보기) 화면구성 */
	@RequestMapping(value = "/wf/adm/form/setMobRegFrm")
	public String setMobRegFrm(HttpServletRequest request,
			@RequestParam(value = "formNo", required = true) String formNo,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		WfFormBVo wfFormBVo = new WfFormBVo();
		wfFormBVo.setCompId(userVo.getCompId());
		wfFormBVo.setQueryLang(langTypCd);
		wfFormBVo.setFormNo(formNo);
		wfFormBVo=(WfFormBVo)commonSvc.queryVo(wfFormBVo);
		
		if(wfFormBVo==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		// 생성ID
		//String genId = wfFormBVo.getGenId();
				
		String mdCd = "U";
		
		// 양식 컬럼 목록 조회
		List<WfFormColmLVo> wfFormColmLVoList=wfAdmSvc.getWfFormColmLVoList(null, formNo, mdCd);
		if(wfFormColmLVoList!=null){
			Map<String, String> wfFormColmLVoMap = new HashMap<String, String>();
			for(WfFormColmLVo storedWfFormColmLVo : wfFormColmLVoList){
				wfFormColmLVoMap.put(storedWfFormColmLVo.getColmNm().replaceAll("_", ""), storedWfFormColmLVo.getColmId());
			}
			model.put("wfFormColmLVoMap", wfFormColmLVoMap);
		}
		
		//boolean isHst=false;
		
		// 양식등록상세 조회
		//WfFormRegDVo wfFormRegDVo = wfAdmSvc.getWfFormRegDVo(genId, formNo, true, null);
		//model.put("wfFormRegDVo", wfFormRegDVo);
		
		// 양식등록상세 조회[Current]
		WfFormRegDVo wfFormRegDVo = wfAdmSvc.getWfFormRegDVo(null, formNo, false, null);
		model.put("wfFormRegDVo", wfFormRegDVo);
		
		// 양식모바일상세 조회[Current]
		WfFormRegDVo wfFormMobDVo = wfAdmSvc.getWfFormRegDVo(null, formNo, false, Boolean.TRUE);
		if(wfFormMobDVo!=null){
			//JSONParser parser = new JSONParser();
			//JSONArray jsonArray = (JSONArray)parser.parse(wfFormMobDVo.getLoutVa());
		}
		model.put("wfFormMobDVo", wfFormMobDVo);
		
		// 모바일 화면
		//List<WfFormMobDVo> wfFormMobDVoList = wfAdmSvc.getWfFormMobDVoList(langTypCd, null, formNo, isHst);
		
		//if(wfFormMobDVoList==null) wfFormMobDVoList=new ArrayList<WfFormMobDVo>();
		// UI 구성용 - 빈 VO 하나 더함
		//wfFormMobDVoList.add(new WfFormMobDVo());
		
		//model.put("wfFormMobDVoList", wfFormMobDVoList);
		
		return LayoutUtil.getJspPath("/wf/adm/form/setMobRegFrm");
	}
	
	
	/** [AJAX] - 모바일 양식 데이터 저장 */
	@RequestMapping(value = "/wf/adm/form/transMobRegListAjx")
	public String transMobRegListAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			
			// Json to Map
			Map<String, Object> paramMap = JsonUtil.jsonToMap(object);
						
			// 양식번호 체크
			if(!paramMap.containsKey("formNo") || ((String)paramMap.get("formNo")).isEmpty()){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			String formNo=(String)paramMap.get("formNo");
			WfFormBVo wfFormBVo = new WfFormBVo();
			wfFormBVo.setCompId(userVo.getCompId());
			wfFormBVo.setQueryLang(langTypCd);
			wfFormBVo.setFormNo(formNo);
			wfFormBVo=(WfFormBVo)commonSvc.queryVo(wfFormBVo);
			
			if(wfFormBVo==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			
			// 모바일 등록구분코드
			String mobRegTypCd=(String)paramMap.get("mobRegTypCd");
			QueryQueue queryQueue = new QueryQueue();
			
			// 양식 등록상세 테이블에 모바일등록구분코드 저장
			WfFormRegDVo wfFormRegDVo = new WfFormRegDVo();
			wfFormRegDVo.setFormNo(formNo);
			wfFormRegDVo.setMobRegTypCd(mobRegTypCd);
			queryQueue.update(wfFormRegDVo);
			
			// 모바일등록상세 저장
			wfFormRegDVo = new WfFormRegDVo();
			
			// jsonMap => Vo
			FormUtil.setParamToVo(paramMap, wfFormRegDVo, null);
			wfFormRegDVo.setMob(true); // 모바일여부
			queryQueue.store(wfFormRegDVo);
			
			// 저장옵션코드
			String saveOptCd=(String)paramMap.get("saveOptCd");
			if(saveOptCd==null || saveOptCd.isEmpty()) saveOptCd="S";
			// 현재 버전에 저장
			if("D".equals(saveOptCd) && wfFormBVo.getGenId()!=null && !wfFormBVo.getGenId().isEmpty()){
				String genId=wfFormBVo.getGenId();
				WfFormRegDVo wfFormMobDVo = new WfFormRegDVo();
				BeanUtils.copyProperties(wfFormRegDVo, wfFormMobDVo);
				
				wfFormMobDVo.setGenId(genId);
				wfFormMobDVo.setHst(true);
				wfFormMobDVo.setMob(true); // 모바일
				queryQueue.store(wfFormMobDVo);
			}
			
			
			if(!queryQueue.isEmpty())
				commonSvc.execute(queryQueue);
			
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
	
}
