package com.innobiz.orange.web.ap.ctrl;

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

import com.innobiz.orange.web.ap.svc.ApCmSvc;
import com.innobiz.orange.web.ap.svc.ApRescSvc;
import com.innobiz.orange.web.ap.utils.ApCmUtil;
import com.innobiz.orange.web.ap.utils.ApConstant;
import com.innobiz.orange.web.ap.vo.ApErpFormBVo;
import com.innobiz.orange.web.ap.vo.ApFormApvLnDVo;
import com.innobiz.orange.web.ap.vo.ApFormBVo;
import com.innobiz.orange.web.ap.vo.ApFormBxDVo;
import com.innobiz.orange.web.ap.vo.ApFormBxDeptRVo;
import com.innobiz.orange.web.ap.vo.ApFormCombDVo;
import com.innobiz.orange.web.ap.vo.ApFormImgDVo;
import com.innobiz.orange.web.ap.vo.ApFormItemDVo;
import com.innobiz.orange.web.ap.vo.ApFormItemLVo;
import com.innobiz.orange.web.ap.vo.ApFormTxtDVo;
import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.ap.vo.ApRescDVo;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrOrgTreeVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtxSortOrdrChnVo;
import com.innobiz.orange.web.wf.vo.WfRescBVo;

/** 결재 약식 관리 Ctrl (관리자용) */
@Controller
public class ApAdmFormCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(ApAdmFormCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 결재 공통 서비스 */
	@Autowired
	private ApCmSvc apCmSvc;

	/** 결재 리소스 처리 서비스 */
	@Autowired
	private ApRescSvc apRescSvc;
	
	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
//
//	/** 캐쉬 만료 처리용 서비스 */
//	@Autowired
//	private PtCacheExpireSvc ptCacheExpireSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;

	/** 양식관리 */
	@RequestMapping(value = {"/ap/adm/form/setApvForm", "/ap/env/setApvForm"})
	public String setApvForm(HttpServletRequest request,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/ap/adm/form/setApvForm");
	}
	
	/** [왼쪽 프레임] 양식함 트리 */
	@RequestMapping(value = {"/ap/adm/form/treeFormBxFrm", "/ap/env/treeFormBxFrm"})
	public String treeFormBxFrm(HttpServletRequest request,
			@Parameter(name="formBxId", required=false) String formBxId,
			@Parameter(name="noInit", required=false) String noInit,
			String userPage,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		ApFormBxDVo apFormBxDVo = new ApFormBxDVo();
		apFormBxDVo.setCompId(userVo.getCompId());
		apFormBxDVo.setQueryLang(langTypCd);
		apFormBxDVo.setOrderBy("FORM_BX_PID, SORT_ORDR");
		if("Y".equals(userPage)){
			apFormBxDVo.setUseYn("Y");
			model.put("userPage", userPage);
			
			// 결재 옵션 조회
			Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(model, userVo.getCompId());

			// [옵션] 부서별 양식함
			if("Y".equals(optConfigMap.get("formBxByDept"))){
				apFormBxDVo.setFormBxDeptIdList(ApCmUtil.toList(userVo.getOrgPids()));
			}
		}
		
		String firstFormBxId = null;
		
		@SuppressWarnings("unchecked")
		List<ApFormBxDVo> apFormBxDVoList = (List<ApFormBxDVo>)commonSvc.queryList(apFormBxDVo);
		if(apFormBxDVoList != null && !apFormBxDVoList.isEmpty()){
			model.put("apFormBxDVoList", apFormBxDVoList);
			firstFormBxId = apFormBxDVoList.get(0).getFormBxId();
		}
		if(!"Y".equals(noInit)){
			if(formBxId != null && !formBxId.isEmpty()){
				model.put("selectedNode", formBxId);
			} else if(firstFormBxId != null && !firstFormBxId.isEmpty()){
				model.put("selectedNode", firstFormBxId);
			} else {
				model.put("selectedNode", "ROOT");
			}
		}
		
		return LayoutUtil.getJspPath("/ap/adm/form/treeFormBxFrm");
	}
	
	/** [부모프레임:팝업] 양식이동 */
	@RequestMapping(value = {"/ap/adm/form/setMoveToFormBxPop", "/ap/env/setMoveToFormBxPop"})
	public String setMoveToFormBxPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/ap/adm/form/setMoveToFormBxPop");
	}
	/** [부모프레임:팝업] 양식이동 - 저장 */
	@RequestMapping(value = {"/ap/adm/form/transMoveToFormBxAjx", "/ap/env/transMoveToFormBxAjx"})
	public String transMoveToFormBxAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		
		QueryQueue queryQueue = new QueryQueue();
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		
		String formBxId = (String)jsonObject.get("formBxId");
		String formIds = (String)jsonObject.get("formIds");
		
		// 순서조절용 - 옮겨 갈곳 최대 정렬순서
		PtxSortOrdrChnVo ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
		ptxSortOrdrChnVo.setTabId("AP_FORM_B");
		ptxSortOrdrChnVo.setPkCol("FORM_BX_ID");
		ptxSortOrdrChnVo.setPk(formBxId);
		ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.selectMaxSortOrdr");
		Integer maxSortOrdr = commonSvc.queryInt(ptxSortOrdrChnVo);
		
		ApFormBVo apFormBVo, storedApFormBVo = null;
		
		if(formIds!=null && !formIds.isEmpty()){
			for(String formId : formIds.split(",")){
				
				// 저장된 양식 조회 - 첫째것만
				if(storedApFormBVo==null){
					storedApFormBVo = new ApFormBVo();
					storedApFormBVo.setFormId(formId);
					storedApFormBVo = (ApFormBVo)commonSvc.queryVo(storedApFormBVo);
					
					if(storedApFormBVo == null) continue;
				}
				
				// 정렬순서 더하기
				maxSortOrdr++;
				
				// 저장된 양식 조회
				apFormBVo = new ApFormBVo();
				apFormBVo.setFormId(formId);
				apFormBVo.setFormBxId(formBxId);
				apFormBVo.setSortOrdr(maxSortOrdr.toString());
				queryQueue.update(apFormBVo);
			}
		}
		
		try {
			commonSvc.execute(queryQueue);
		} catch(Exception e){
			model.put("message", e.getMessage());
			return LayoutUtil.returnJson(model);
		}
		
		queryQueue.removeAll();
		
		// 옮겨간곳 정렬 순서 맞추기
		if(storedApFormBVo != null){
			
			apFormBVo = new ApFormBVo();
			apFormBVo.setFormBxId(storedApFormBVo.getFormBxId());
			apFormBVo.setOrderBy("SORT_ORDR");
			@SuppressWarnings("unchecked")
			List<ApFormBVo> apFormBVoList = (List<ApFormBVo>)commonSvc.queryList(apFormBVo);
			
			if(apFormBVoList != null){
				maxSortOrdr = 1;
				for(ApFormBVo sortApFormBVo : apFormBVoList){
					
					if(Integer.parseInt(sortApFormBVo.getSortOrdr()) != maxSortOrdr.intValue()){
						apFormBVo = new ApFormBVo();
						apFormBVo.setFormId(sortApFormBVo.getFormId());
						apFormBVo.setSortOrdr(maxSortOrdr.toString());
						queryQueue.update(apFormBVo);
					}
					maxSortOrdr++;
				}
			}
		}
		
		if(!queryQueue.isEmpty()){
			try {
				commonSvc.execute(queryQueue);
			} catch(Exception e){
				model.put("message", e.getMessage());
				return LayoutUtil.returnJson(model);
			}
		}
		
		//cm.msg.save.success=저장 되었습니다.
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		model.put("result", "ok");
		return LayoutUtil.returnJson(model);
	}
	/** [왼쪽 프레임:팝업] 양식함 등록/수정 */
	@RequestMapping(value = {"/ap/adm/form/setFormBxPop", "/ap/env/setFormBxPop"})
	public String setFormBxPop(HttpServletRequest request,
			@Parameter(name="formBxId", required=false) String formBxId,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		// 모델에 옵션 설정
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(model, userVo.getCompId());
		
		if(formBxId!=null && !formBxId.isEmpty()){
			
			String langTypCd = LoginSession.getLangTypCd(request);
			
			ApFormBxDVo apFormBxDVo = new ApFormBxDVo();
			apFormBxDVo.setCompId(userVo.getCompId());
			apFormBxDVo.setFormBxId(formBxId);
			apFormBxDVo.setQueryLang(langTypCd);
			apFormBxDVo = (ApFormBxDVo)commonSvc.queryVo(apFormBxDVo);
			if(apFormBxDVo!=null){
				model.put("apFormBxDVo", apFormBxDVo);
				
				ApRescDVo apRescDVo = new ApRescDVo();
				apRescDVo.setCompId(userVo.getCompId());
				apRescDVo.setRescId(apFormBxDVo.getRescId());
				@SuppressWarnings("unchecked")
				List<ApRescDVo> apRescDVoList = (List<ApRescDVo>)commonSvc.queryList(apRescDVo);
				if(apRescDVoList != null){
					for(ApRescDVo storedApRescDVo : apRescDVoList){
						model.put(storedApRescDVo.getRescId()+"_"+storedApRescDVo.getLangTypCd(), storedApRescDVo.getRescVa());
					}
				}
			}
			
			// [옵션] 부서별 양식함
			if("Y".equals(optConfigMap.get("formBxByDept"))){
				String orgTypCd = "P";
				
				ApFormBxDeptRVo apFormBxDeptRVo = new ApFormBxDeptRVo();
				apFormBxDeptRVo.setFormBxId(formBxId);
				apFormBxDeptRVo.setOrderBy("SORT_ORDR");
				@SuppressWarnings("unchecked")
				List<ApFormBxDeptRVo> apFormBxDeptRVoList = (List<ApFormBxDeptRVo>)commonSvc.queryList(apFormBxDeptRVo);
				if(apFormBxDeptRVoList != null){
					OrOrgTreeVo orOrgTreeVo;
					List<OrOrgTreeVo> orOrgTreeVoList = new ArrayList<OrOrgTreeVo>();
					
					boolean first = true;
					StringBuilder builder = new StringBuilder();
					for(ApFormBxDeptRVo storedApFormBxDeptRVo : apFormBxDeptRVoList){
						orOrgTreeVo = orCmSvc.getOrgByOrgTypCd(storedApFormBxDeptRVo.getFormBxDeptId(), orgTypCd, langTypCd);
						if(orOrgTreeVo != null){
							orOrgTreeVoList.add(orOrgTreeVo);
							
							if(first) first = false;
							else builder.append(',');
							builder.append(orOrgTreeVo.getOrgId());
						}
					}
					if(!orOrgTreeVoList.isEmpty()){
						model.put("orOrgTreeVoList", orOrgTreeVoList);
						model.put("formBxDeptIds", builder.toString());
					}
				}
			}
		}
		
		return LayoutUtil.getJspPath("/ap/adm/form/setFormBxPop");
	}
	
	/** [왼쪽 프레임:히든프레임] 양식함 저장 */
	@RequestMapping(value = {"/ap/adm/form/transFormBx", "/ap/env/transFormBx"})
	public String transFormBx(HttpServletRequest request,
			@Parameter(name="formBxId", required=false) String formBxId,
			@Parameter(name="formBxDeptIds", required=false) String formBxDeptIds,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		QueryQueue queryQueue = new QueryQueue();
		
		// 옵션관리 설정사항 조회(캐쉬)  - 이중결재 사용안할 경우 제거용
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
		
		// 양식함상세(AP_FORM_BX_D) 테이블
		ApFormBxDVo apFormBxDVo = new ApFormBxDVo();
		VoUtil.bind(request, apFormBxDVo);
		apFormBxDVo.setCompId(userVo.getCompId());
		
		// 리소스기본(AP_RESC_D) 테이블에 저장할 단건의 리소스 데이터를 QueryQueue 에 저장
		ApRescDVo apRescDVo = apRescSvc.collectApRescDVo(request, null, queryQueue);
		
		apFormBxDVo.setRescId(apRescDVo.getRescId());
		apFormBxDVo.setFormBxNm(apRescDVo.getRescVa());
		
		apFormBxDVo.setModrUid(userVo.getUserUid());
		apFormBxDVo.setModDt("sysdate");
		
		boolean isNew = false;
		
		// 양식함ID - KEY 가 없으면
		if(apFormBxDVo.getFormBxId()==null || apFormBxDVo.getFormBxId().isEmpty()){
			formBxId = apCmSvc.createId("AP_FORM_BX_D");
			apFormBxDVo.setFormBxId(formBxId);
			apFormBxDVo.setRegrUid(userVo.getUserUid());
			apFormBxDVo.setRegDt("sysdate");
			queryQueue.insert(apFormBxDVo);
			isNew = true;
		} else {
			queryQueue.store(apFormBxDVo);
		}
		
		// [옵션] 부서별 양식함
		if("Y".equals(optConfigMap.get("formBxByDept"))){
			// 이전 부서함별 양식 삭제
			ApFormBxDeptRVo apFormBxDeptRVo = new ApFormBxDeptRVo();
			apFormBxDeptRVo.setFormBxId(formBxId);
			queryQueue.delete(apFormBxDeptRVo);
			
			// 부서함별 양식 입력
			Integer sortOrdr = 1;
			if(formBxDeptIds!=null && !formBxDeptIds.isEmpty()){
				for(String formBxDeptId : formBxDeptIds.split(",")){
					apFormBxDeptRVo = new ApFormBxDeptRVo();
					apFormBxDeptRVo.setFormBxId(formBxId);
					apFormBxDeptRVo.setFormBxDeptId(formBxDeptId);
					apFormBxDeptRVo.setSortOrdr(sortOrdr.toString());
					queryQueue.insert(apFormBxDeptRVo);
					sortOrdr++;
				}
			}
		}

		try {
			commonSvc.execute(queryQueue);
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			if(isNew){
				model.put("todo", "parent.getIframeContent('formBxFrm').reloadTree('"+apFormBxDVo.getFormBxPid()+"');");
			} else {
				model.put("todo", "parent.getIframeContent('formBxFrm').reloadTree('"+apFormBxDVo.getFormBxId()+"');");
			}
		} catch(Exception e){
			model.put("message", e.getMessage());
		}
		return LayoutUtil.getResultJsp();
	}
	
	/** [왼쪽 프레임:AJAX] 양식함 삭제 */
	@RequestMapping(value = {"/ap/adm/form/transFormBxDelAjx", "/ap/env/transFormBxDelAjx"})
	public String transFormBxDelAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		QueryQueue queryQueue = new QueryQueue();
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		
		String formBxId = (String)jsonObject.get("formBxId");
//		String formBxPid = (String)jsonObject.get("formBxPid");
		
		// 하위 양식함이 있는지 검사
		ApFormBxDVo apFormBxDVo = new ApFormBxDVo();
		apFormBxDVo.setCompId(userVo.getCompId());
		apFormBxDVo.setFormBxPid(formBxId);
		if(commonSvc.count(apFormBxDVo) > 0){
			//ap.jsp.setApvForm.notDelBxWithChild=하위 양식함이 있는 양식함은 삭제 할 수 없습니다.
			model.put("message", messageProperties.getMessage("ap.jsp.setApvForm.notDelBxWithChild", request));
			return LayoutUtil.returnJson(model);
		}
		
		// 양식이 등록된 함인지 검사
		ApFormBVo apFormBVo = new ApFormBVo();
		apFormBVo.setCompId(userVo.getCompId());
		apFormBVo.setFormBxId(formBxId);
		if(commonSvc.count(apFormBVo) > 0){
			//ap.jsp.setApvForm.notDelBxWithForm=양식이 등록되어 있는 양식함은 삭제 할 수 없습니다.
			model.put("message", messageProperties.getMessage("ap.jsp.setApvForm.notDelBxWithForm", request));
			return LayoutUtil.returnJson(model);
		}
		
		// 해당 양식함 데이터 조회
		ApFormBxDVo storedApFormBxDVo = new ApFormBxDVo();
		storedApFormBxDVo.setCompId(userVo.getCompId());
		storedApFormBxDVo.setFormBxId(formBxId);
		storedApFormBxDVo = (ApFormBxDVo)commonSvc.queryVo(storedApFormBxDVo);
		if(storedApFormBxDVo==null){
			//cm.msg.noData=해당하는 데이터가 없습니다.
			model.put("message", messageProperties.getMessage("cm.msg.noData", request));
			return LayoutUtil.returnJson(model);
		}
		
		// 리소스 삭제
		ApRescDVo apRescDVo = new ApRescDVo();
		apRescDVo.setCompId(userVo.getCompId());
		apRescDVo.setRescId(storedApFormBxDVo.getRescId());
		queryQueue.delete(apRescDVo);
		
		// 해당 양식함 데이터 삭제
		apFormBxDVo = new ApFormBxDVo();
		apFormBxDVo.setCompId(userVo.getCompId());
		apFormBxDVo.setFormBxId(formBxId);
		queryQueue.delete(apFormBxDVo);
		
		// 양식함부서관계 - 삭제
		ApFormBxDeptRVo apFormBxDeptRVo = new ApFormBxDeptRVo();
		apFormBxDeptRVo.setFormBxId(formBxId);
		queryQueue.delete(apFormBxDeptRVo);
		
		try {
			
			commonSvc.execute(queryQueue);
			
			//cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
			
		} catch(Exception e){
			model.put("message", e.getMessage());
		}
		
		return LayoutUtil.returnJson(model);
	}
	
	/** [왼쪽 프레임:AJAX] 양식함 순서조절 */
	@RequestMapping(value = {"/ap/adm/form/transFormBxMoveAjx", "/ap/env/transFormBxMoveAjx"})
	public String transFormBxMoveAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		
		try {
			
			UserVo userVo = LoginSession.getUser(request);
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			
			String direction = (String)jsonObject.get("direction");
			String formBxId = (String)jsonObject.get("formBxId");
//			String formBxPid = (String)jsonObject.get("formBxPid");
			
			QueryQueue queryQueue = new QueryQueue();
			PtxSortOrdrChnVo ptxSortOrdrChnVo;
			
			if(	direction==null || (!"up".equals(direction) && !"down".equals(direction))
				|| formBxId==null || formBxId.isEmpty()){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String message = messageProperties.getMessage("cm.msg.notValidCall", request);
				if(direction==null || (!"up".equals(direction) && !"down".equals(direction))){
					LOGGER.error("Org move(up/down) - direction==null  : "+message);
				} else if(formBxId==null || formBxId.isEmpty()){
					LOGGER.error("Org move(up/down) - direction:"+direction+" formBxId:"+formBxId+"  : "+message);
				}
				throw new CmException(message);
			}
			
			// curOrdr - 현재순번
			// switchOrdr - 바꿀 순번
			Integer curOrdr, switchOrdr;
			String formBxPid=null;
			
			ApFormBxDVo apFormBxDVo, storedApFormBxDVo;
			
			storedApFormBxDVo = new ApFormBxDVo();
			storedApFormBxDVo.setCompId(userVo.getCompId());
			storedApFormBxDVo.setFormBxId(formBxId);
			storedApFormBxDVo = (ApFormBxDVo)commonSvc.queryVo(storedApFormBxDVo);
			
			if(storedApFormBxDVo==null){
				//cm.msg.noData=해당하는 데이터가 없습니다.
				String msg = messageProperties.getMessage("cm.msg.noData", request);
				model.put("message", msg);
				LOGGER.error("no data(OR_ORG_B) - formBxId:"+formBxId+"  : "+msg);
				return JsonUtil.returnJson(model);
			}
			
			curOrdr = Integer.valueOf(storedApFormBxDVo.getSortOrdr());
			formBxPid = storedApFormBxDVo.getFormBxPid();
			
			// 위로 이동
			if("up".equals(direction)){
				
				switchOrdr = curOrdr-1;
				
				if(switchOrdr>0){
					ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
					ptxSortOrdrChnVo.setTabId("AP_FORM_BX_D");
					ptxSortOrdrChnVo.setCompId(userVo.getCompId());
					ptxSortOrdrChnVo.setPkCol("FORM_BX_PID");
					ptxSortOrdrChnVo.setPk(formBxPid);
					ptxSortOrdrChnVo.setMoreThen(switchOrdr);
					ptxSortOrdrChnVo.setLessThen(switchOrdr);
					ptxSortOrdrChnVo.setChnVa(1);
					ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
					queryQueue.update(ptxSortOrdrChnVo);
					
					apFormBxDVo = new ApFormBxDVo();
					apFormBxDVo.setCompId(userVo.getCompId());
					apFormBxDVo.setFormBxId(formBxId);
					apFormBxDVo.setSortOrdr(switchOrdr.toString());
					queryQueue.update(apFormBxDVo);
				}
				
				if(!queryQueue.isEmpty()){
					commonSvc.execute(queryQueue);
					model.put("result", "ok");
				} else {
					//cm.msg.nodata.moveup=위로 이동할 항목이 없습니다.
					model.put("message",  messageProperties.getMessage("cm.msg.nodata.moveup", request));
				}
				
			// 아래로 이동
			} else if("down".equals(direction)){
				
				ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
				ptxSortOrdrChnVo.setTabId("AP_FORM_BX_D");
				ptxSortOrdrChnVo.setCompId(userVo.getCompId());
				ptxSortOrdrChnVo.setPkCol("FORM_BX_PID");
				ptxSortOrdrChnVo.setPk(formBxPid);
				ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.selectMaxSortOrdr");
				Integer maxSortOrdr = commonSvc.queryInt(ptxSortOrdrChnVo);
				
				if(maxSortOrdr>curOrdr){
					
					switchOrdr = curOrdr+1;
					
					ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
					ptxSortOrdrChnVo.setTabId("AP_FORM_BX_D");
					ptxSortOrdrChnVo.setCompId(userVo.getCompId());
					ptxSortOrdrChnVo.setPkCol("FORM_BX_PID");
					ptxSortOrdrChnVo.setPk(formBxPid);
					ptxSortOrdrChnVo.setMoreThen(switchOrdr);
					ptxSortOrdrChnVo.setLessThen(switchOrdr);
					ptxSortOrdrChnVo.setChnVa(-1);
					ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
					queryQueue.update(ptxSortOrdrChnVo);
					

					apFormBxDVo = new ApFormBxDVo();
					apFormBxDVo.setCompId(userVo.getCompId());
					apFormBxDVo.setFormBxId(formBxId);
					apFormBxDVo.setSortOrdr(switchOrdr.toString());
					queryQueue.update(apFormBxDVo);
				}
				
				if(!queryQueue.isEmpty()){
					commonSvc.execute(queryQueue);
					model.put("result", "ok");
				} else {
					//cm.msg.nodata.movedown=아래로 이동할 항목이 없습니다.
					model.put("message",  messageProperties.getMessage("cm.msg.nodata.movedown", request));
				}
			}
			
		} catch(CmException e){
			//LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
		}
		return LayoutUtil.returnJson(model);
	}
	
	/** [오른쪽 프레임] 양식함 별 양식 목록 */
	@RequestMapping(value = {"/ap/adm/form/listFormFrm", "/ap/env/listFormFrm"})
	public String listFormFrm(HttpServletRequest request,
			@Parameter(name="formBxId", required=false) String formBxId,
			@Parameter(name="schWord", required=false) String schWord,
			@Parameter(name="formTypCd", required=false) String formTypCd,
			@Parameter(name="forTrans", required=false) String forTrans,// 변환용 - 변환용일 경우 변환용도 보이도록 함(일반 양식은 다 보이게 함)
			String userPage,//사용자용 페이지 - Y: 사용안함, 테플릿용 은 보이지 않음
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		ApFormBVo apFormBVo = new ApFormBVo();
		apFormBVo.setCompId(userVo.getCompId());
		apFormBVo.setFormBxId(formBxId);
		if(schWord!=null && !schWord.isEmpty()){
			apFormBVo.setSchCat("formNm");
			apFormBVo.setSchWord(schWord);
		}
		if(formTypCd!=null && !formTypCd.isEmpty()){
			apFormBVo.setFormTypCd(formTypCd);
		}
		apFormBVo.setQueryLang(langTypCd);

		if("Y".equals(userPage)){
			apFormBVo.setUseYn("Y");
			apFormBVo.setTplYn("N");
			model.put("userPage", userPage);
			
			// 옵션관리 설정사항 조회(캐쉬)  - 이중결재 사용안할 경우 제거용
			Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
			
			// 이중 결재 사용 안함
			if(!"Y".equals(optConfigMap.get("dblApvEnab"))){
				
				// apvLn:결재(합의표시안함), apvLnMixd:결재(결재합의혼합), apvLn1LnAgr:결재+합의(1줄), 
				// apvLn2LnAgr:결재+합의(2줄), apvLnDbl:이중결재
				
				if((formTypCd==null || formTypCd.isEmpty()) && !"Y".equals(forTrans)){
					// 03:시행변환용 - 제거, 이중결재 - 제거
					// 양식구분코드 - intro:기안(내부문서), extro:기안(시행겸용), trans:시행변환용
					// apvLnDbl:이중결재, apvLnDblList:리스트(이중결재)
					apFormBVo.setWhereSqllet("AND FORM_TYP_CD IN ('intro', 'extro') AND FORM_APV_LN_TYP_CD NOT IN ('apvLnDbl','apvLnDblList')");
				} else {
					// apvLnDbl:이중결재, apvLnDblList:리스트(이중결재) - 제거
					apFormBVo.setWhereSqllet("AND FORM_APV_LN_TYP_CD NOT IN ('apvLnDbl','apvLnDblList')");
				}
			} else {
				if((formTypCd==null || formTypCd.isEmpty()) && !"Y".equals(forTrans)){
					// 03:시행변환용 - 제거
					// 양식구분코드 - intro:기안(내부문서), extro:기안(시행겸용), trans:시행변환용
					apFormBVo.setWhereSqllet("AND FORM_TYP_CD IN ('intro', 'extro')");
				}
			}
		}
		apFormBVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<ApFormBVo> apFormBVoList = (List<ApFormBVo>)commonSvc.queryList(apFormBVo);
		model.put("apFormBVoList", apFormBVoList);
		
		//양식구분코드
		//List<PtCdBVo> formTypCdList = ptCmSvc.getCdList("FORM_TYP_CD", langTypCd, "Y".equals(forTrans) ? null : "Y");
		List<PtCdBVo> formTypCdList = ptCmSvc.getAllCdList("FORM_TYP_CD", langTypCd);
		model.put("formTypCdList", formTypCdList);
		
		return LayoutUtil.getJspPath("/ap/adm/form/listFormFrm");
	}
	
	/** [오른쪽 프레임:AJAX] 양식 순서조절 */
	@RequestMapping(value = {"/ap/adm/form/transFormMoveAjx", "/ap/env/transFormMoveAjx"})
	public String transFormMoveAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {

		try {
			
			//UserVo userVo = LoginSession.getUser(request);
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			
			String direction = (String)jsonObject.get("direction");
			String formBxId = (String)jsonObject.get("formBxId");
			JSONArray jsonArray = (JSONArray)jsonObject.get("formIds");
			
			QueryQueue queryQueue = new QueryQueue();
			PtxSortOrdrChnVo ptxSortOrdrChnVo;
			
			if(	direction==null || (!"up".equals(direction) && !"down".equals(direction))
				|| formBxId==null || formBxId.isEmpty()){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String message = messageProperties.getMessage("cm.msg.notValidCall", request);
				if(direction==null || (!"up".equals(direction) && !"down".equals(direction))){
					LOGGER.error("Org move(up/down) - direction==null  : "+message);
				} else if(formBxId==null || formBxId.isEmpty()){
					LOGGER.error("Org move(up/down) - direction:"+direction+" formBxId:"+formBxId+"  : "+message);
				}
				throw new CmException(message);
			}
			
			String formId;
			ApFormBVo apFormBVo, storedApFormBVo;
			
			boolean isFirst = true;
			StringBuilder builder = new StringBuilder();
			
			// 위로 이동
			if("up".equals(direction)){

				// curOrdr - 현재순번
				// stdOrdr - 기준순번 - 현재순번과 기준순번이 같으면 옮길수 없는 것으로 패스
				Integer curOrdr, stdOrdr=1, switchOrdr;
				for(int i=0;i<jsonArray.size();i++){
					formId = (String)jsonArray.get(i);
					
					apFormBVo = new ApFormBVo();
					apFormBVo.setFormId(formId);
					storedApFormBVo = (ApFormBVo)commonSvc.queryVo(apFormBVo);
					curOrdr = Integer.valueOf(storedApFormBVo.getSortOrdr());
					
					if(stdOrdr==curOrdr){
						stdOrdr++;
						continue;
					}
					switchOrdr = curOrdr-1;
					
					if(isFirst) isFirst = false;
					else builder.append(',');
					builder.append(formId);
					
					// 옮겨 갈곳의 순서를 내림
					ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
					ptxSortOrdrChnVo.setTabId("AP_FORM_B");
					ptxSortOrdrChnVo.setPkCol("FORM_BX_ID");
					ptxSortOrdrChnVo.setPk(formBxId);
					ptxSortOrdrChnVo.setMoreThen(switchOrdr);
					ptxSortOrdrChnVo.setLessThen(switchOrdr);
					ptxSortOrdrChnVo.setChnVa(1);
					ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
					queryQueue.update(ptxSortOrdrChnVo);
					
					// 선택된 것의 순서를 올림
					apFormBVo = new ApFormBVo();
					apFormBVo.setFormId(formId);
					apFormBVo.setSortOrdr(switchOrdr.toString());
					queryQueue.update(apFormBVo);
				}
				
				if(!queryQueue.isEmpty()){
					commonSvc.execute(queryQueue);
					model.put("formIds", builder.toString());
				} else {
					//cm.msg.nodata.moveup=위로 이동할 항목이 없습니다.
					model.put("message",  messageProperties.getMessage("cm.msg.nodata.moveup", request));
				}
				return JsonUtil.returnJson(model);
				
			} else if("down".equals(direction)){
				
				ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
				ptxSortOrdrChnVo.setTabId("AP_FORM_B");
				ptxSortOrdrChnVo.setPkCol("FORM_BX_ID");
				ptxSortOrdrChnVo.setPk(formBxId);
				ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.selectMaxSortOrdr");
				Integer maxSortOrdr = commonSvc.queryInt(ptxSortOrdrChnVo);
				
				// curOrdr - 현재순번
				// stdOrdr - 기준순번 - 현재순번과 기준순번이 같으면 옮길수 없는 것으로 패스
				Integer curOrdr, stdOrdr=maxSortOrdr, switchOrdr;
				//ApFormBVo apFormBVo, storedApFormBVo;
				
				for(int i=jsonArray.size()-1;i>=0;i--){
					formId = (String)jsonArray.get(i);
					
					apFormBVo = new ApFormBVo();
					apFormBVo.setFormId(formId);
					storedApFormBVo = (ApFormBVo)commonSvc.queryVo(apFormBVo);
					curOrdr = Integer.valueOf(storedApFormBVo.getSortOrdr());
					
					if(stdOrdr==curOrdr){
						stdOrdr--;
						continue;
					}
					switchOrdr = curOrdr+1;
					
					if(isFirst) isFirst = false;
					else builder.append(',');
					builder.append(formId);
					
					// 옮겨갈 곳의 순서를 올림
					ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
					ptxSortOrdrChnVo.setTabId("AP_FORM_B");
					ptxSortOrdrChnVo.setPkCol("FORM_BX_ID");
					ptxSortOrdrChnVo.setPk(formBxId);
					ptxSortOrdrChnVo.setMoreThen(switchOrdr);
					ptxSortOrdrChnVo.setLessThen(switchOrdr);
					ptxSortOrdrChnVo.setChnVa(-1);
					ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
					queryQueue.update(ptxSortOrdrChnVo);
					
					// 선택된 것의 순서를 내림
					apFormBVo = new ApFormBVo();
					apFormBVo.setFormId(formId);
					apFormBVo.setSortOrdr(switchOrdr.toString());
					queryQueue.update(apFormBVo);
				}
				
				if(!queryQueue.isEmpty()){
					commonSvc.execute(queryQueue);
					model.put("formIds", builder.toString());
				} else {
					//cm.msg.nodata.movedown=아래로 이동할 항목이 없습니다.
					model.put("message",  messageProperties.getMessage("cm.msg.nodata.movedown", request));
				}
				return JsonUtil.returnJson(model);
			}
			
		} catch(CmException e){
			//LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
		}
		
		return LayoutUtil.returnJson(model);
	}
	
	/** [오른쪽 프레임:팝업] 양식 기본 설정 */
	@RequestMapping(value = {"/ap/adm/form/setFormEssPop", "/ap/env/setFormEssPop"})
	public String setFormEssPop(HttpServletRequest request,
			@Parameter(name="formBxId", required=false) String formBxId,
			@Parameter(name="formId", required=false) String formId,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		model.put("sysPloc", sysPlocMap);
		
		// 양식함 정보 조회
		if(formBxId!=null && !formBxId.isEmpty()){
			ApFormBxDVo apFormBxDVo = new ApFormBxDVo();
			apFormBxDVo.setCompId(userVo.getCompId());
			apFormBxDVo.setFormBxId(formBxId);
			apFormBxDVo.setQueryLang(langTypCd);
			apFormBxDVo = (ApFormBxDVo)commonSvc.queryVo(apFormBxDVo);
			if(apFormBxDVo!=null){
				model.put("apFormBxDVo", apFormBxDVo);
			}
		}
		
		// 양식정보 조회
		if(formId!=null && !formId.isEmpty()){
			ApFormBVo apFormBVo = new ApFormBVo();
			apFormBVo.setCompId(userVo.getCompId());
			apFormBVo.setFormId(formId);
			
			apFormBVo = (ApFormBVo)commonSvc.queryVo(apFormBVo);
			if(apFormBVo!=null){
				model.put("apFormBVo", apFormBVo);
				
				ApRescDVo apRescDVo = new ApRescDVo();
				apRescDVo.setCompId(userVo.getCompId());
				apRescDVo.setRescId(apFormBVo.getRescId());
				@SuppressWarnings("unchecked")
				List<ApRescDVo> apRescDVoList = (List<ApRescDVo>)commonSvc.queryList(apRescDVo);
				if(apRescDVoList != null){
					for(ApRescDVo storedApRescDVo : apRescDVoList){
						model.put(storedApRescDVo.getRescId()+"_"+storedApRescDVo.getLangTypCd(), storedApRescDVo.getRescVa());
					}
				}
			}
		} else {
			// 신규 생성의 경우 템플릿 조회
			ApFormBVo apFormBVo = new ApFormBVo();
			apFormBVo.setCompId(userVo.getCompId());
			apFormBVo.setUseYn("Y");
			apFormBVo.setTplYn("Y");
			apFormBVo.setQueryLang(langTypCd);
			@SuppressWarnings("unchecked")
			List<ApFormBVo> tplApFormBVoList = (List<ApFormBVo>)commonSvc.queryList(apFormBVo);
			if(tplApFormBVoList!=null && !tplApFormBVoList.isEmpty()){
				model.put("tplApFormBVoList", tplApFormBVoList);
			}
		}

		//양식구분코드
		List<PtCdBVo> formTypCdList = ptCmSvc.getCdList("FORM_TYP_CD", langTypCd, "Y");
		model.put("formTypCdList", formTypCdList);
		// 문서보존기간코드
		List<PtCdBVo> docKeepPrdCdList = ptCmSvc.getCdList("DOC_KEEP_PRD_CD", langTypCd, "Y");
		model.put("docKeepPrdCdList", docKeepPrdCdList);
		
		return LayoutUtil.getJspPath("/ap/adm/form/setFormEssPop");
	}
	
	/** [오른쪽 프레임:팝업-저장] 양식설정 저장 */
	@RequestMapping(value = {"/ap/adm/form/transFormEss", "/ap/env/transFormEss"})
	public String transFormEss(HttpServletRequest request,
			@Parameter(name="formId", required=false) String formId,
			@Parameter(name="formBxId", required=false) String formBxId,
			@Parameter(name="tplFormId", required=false) String tplFormId,
			@Parameter(name="wfFormNo", required=false) String wfFormNo,
			@Parameter(name="wfGenId", required=false) String wfGenId,
			@Parameter(name="wfRescId", required=false) String wfRescId,
			@Parameter(name="menuId", required=false) String menuId,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		QueryQueue queryQueue = new QueryQueue();
		
		ApFormBVo apFormBVo = new ApFormBVo();
		VoUtil.bind(request, apFormBVo);
		apFormBVo.setCompId(userVo.getCompId());
		
		// 리소스기본(AP_RESC_D) 테이블에 저장할 단건의 리소스 데이터를 QueryQueue 에 저장
		ApRescDVo apRescDVo = apRescSvc.collectApRescDVo(request, null, queryQueue);
		apFormBVo.setRescId(apRescDVo.getRescId());
		apFormBVo.setFormNm(apRescDVo.getRescVa());
		
		apFormBVo.setModrUid(userVo.getUserUid());
		apFormBVo.setModDt("sysdate");
		
		boolean isNew = false;
		if(formId==null || formId.isEmpty()){
			formId = apCmSvc.createId("AP_FORM_B");
			apFormBVo.setFormId(formId);
			apFormBVo.setFormSeq("1");
			if(apFormBVo.getUseYn()==null) apFormBVo.setUseYn("Y");
			if(apFormBVo.getTplYn()==null) apFormBVo.setTplYn("N");
			if(apFormBVo.getBodyOlineYn()==null) apFormBVo.setBodyOlineYn("Y");
			apFormBVo.setRegrUid(userVo.getUserUid());
			apFormBVo.setRegDt("sysdate");
			
			// 업무관리 폼 번호
			if(wfFormNo != null && !wfFormNo.isEmpty()){
				String erpFormId = apCmSvc.createId("AP_ERP_FORM_B");
				String erpFormTypCd = "wfForm";
				apFormBVo.setErpFormId(erpFormId);
				apFormBVo.setErpFormTypCd(erpFormTypCd);
				
				ApErpFormBVo apErpFormBVo = new ApErpFormBVo();
				apErpFormBVo.setErpFormId(erpFormId);
				apErpFormBVo.setCompId(userVo.getCompId());
				apErpFormBVo.setErpFormTypCd(erpFormTypCd);
				
				String rescId = null;
				if(wfRescId != null && !wfRescId.isEmpty()){
					rescId = apCmSvc.createId("AP_RESC_D");
					
					WfRescBVo wfRescBVo = new WfRescBVo();
					wfRescBVo.setRescId(wfRescId);
					@SuppressWarnings("unchecked")
					List<WfRescBVo> rescList = (List<WfRescBVo>)commonSvc.queryList(wfRescBVo);
					if(rescList != null){
						for(WfRescBVo rescVo : rescList){
							apRescDVo = new ApRescDVo();
							apRescDVo.setCompId(userVo.getCompId());
							apRescDVo.setRescId(rescId);
							apRescDVo.setLangTypCd(rescVo.getLangTypCd());
							apRescDVo.setRescVa(rescVo.getRescVa());
							queryQueue.insert(apRescDVo);
						}
					}
				}
				apErpFormBVo.setRescId(rescId);
				apErpFormBVo.setRegUrl(ApConstant.WF_REG_PATH+".jsp?formNo="+wfFormNo+"&genId="+wfGenId);
				queryQueue.insert(apErpFormBVo);
				
				ApFormCombDVo apFormCombDVo = new ApFormCombDVo();
				apFormCombDVo.setFormId(formId);
				apFormCombDVo.setFormCombId("wfFormBody");
				apFormCombDVo.setFormCombSeq("1");
				apFormCombDVo.setSortOrdr("1");
				queryQueue.insert(apFormCombDVo);
			}
			
			queryQueue.insert(apFormBVo);
			
			if(tplFormId!=null && !tplFormId.isEmpty()){
				applyFormTemplate(queryQueue, apFormBVo, tplFormId, formId);
			}
			
			isNew = true;
		} else {
			
			ApFormBVo storedApFormBVo = new ApFormBVo();
			storedApFormBVo.setFormId(formId);
			storedApFormBVo = (ApFormBVo)commonSvc.queryVo(storedApFormBVo);
			
			if(storedApFormBVo==null){
				apFormBVo.setFormSeq("1");
				queryQueue.insert(apFormBVo);
			} else {
				// 본문 외곽선 사용 여부가 - 전과 다르면 : 버전(폼시퀀스)을 올림
				if(apFormBVo.getBodyOlineYn()!=null && !apFormBVo.getBodyOlineYn().equals(storedApFormBVo.getBodyOlineYn())){
					Integer formSeq = Integer.parseInt(storedApFormBVo.getFormSeq()) + 1;
					apFormBVo.setFormSeq(formSeq.toString());
				}
				queryQueue.update(apFormBVo);
			}
		}
		
		try {
			commonSvc.execute(queryQueue);
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			if(isNew){
				model.put("todo", "parent.location.replace('./setFormEdit.do?menuId="+menuId+"&formId="+formId+"&formBxId="+formBxId+"');");
			} else {
				model.put("todo", "parent.getIframeContent('formListFrm').reload(); parent.dialog.close('setFormEssDialog');");
			}
		} catch(Exception e){
			model.put("message", e.getMessage());
		}
		return LayoutUtil.getResultJsp();
	}

	/** 템플릿 적용 */
	private void applyFormTemplate(QueryQueue queryQueue, ApFormBVo apFormBVo, String tplFormId, String newFormId) throws Exception {
		
		ApFormBVo storedApFormBVo = new ApFormBVo();
		storedApFormBVo.setFormId(tplFormId);
		storedApFormBVo = (ApFormBVo)commonSvc.queryVo(storedApFormBVo);
		if(storedApFormBVo==null) return;
		
		// 본문HTML, 결재라인구분코드 - 복사
		apFormBVo.setBodyHtml(storedApFormBVo.getBodyHtml());
		apFormBVo.setFormApvLnTypCd(storedApFormBVo.getFormApvLnTypCd());
		
		// 양식구성상세(AP_FORM_COMB_D) 테이블 - 복사
		ApFormCombDVo apFormCombDVo = new ApFormCombDVo();
		apFormCombDVo.setFormId(tplFormId);
		apFormCombDVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<ApFormCombDVo> apFormCombDVoList = (List<ApFormCombDVo>)commonSvc.queryList(apFormCombDVo);
		if(apFormCombDVoList!=null){
			for(ApFormCombDVo storedApFormCombDVo : apFormCombDVoList){
				storedApFormCombDVo.setFormId(newFormId);
				queryQueue.insert(storedApFormCombDVo);
			}
		}

		// 양식텍스트상세(AP_FORM_TXT_D) 테이블 - 복사
		ApFormTxtDVo apFormTxtDVo = new ApFormTxtDVo();
		apFormTxtDVo.setFormId(tplFormId);
		@SuppressWarnings("unchecked")
		List<ApFormTxtDVo> apFormTxtDVoList = (List<ApFormTxtDVo>)commonSvc.queryList(apFormTxtDVo);
		if(apFormTxtDVoList!=null){
			for(ApFormTxtDVo storedApFormTxtDVo : apFormTxtDVoList){
				storedApFormTxtDVo.setFormId(newFormId);
				queryQueue.insert(storedApFormTxtDVo);
			}
		}
		
		// 양식이미지상세(AP_FORM_IMG_D) 테이블 - 복사
		ApFormImgDVo apFormImgDVo = new ApFormImgDVo();
		apFormImgDVo.setFormId(tplFormId);
		@SuppressWarnings("unchecked")
		List<ApFormImgDVo> apFormImgDVoList = (List<ApFormImgDVo>)commonSvc.queryList(apFormImgDVo);
		if(apFormTxtDVoList!=null){
			for(ApFormImgDVo storedApFormImgDVo : apFormImgDVoList){
				storedApFormImgDVo.setFormId(newFormId);
				queryQueue.insert(storedApFormImgDVo);
			}
		}
		
		// 양식결재라인상세(AP_FORM_APV_LN_D) - 복사
		ApFormApvLnDVo apFormApvLnDVo = new ApFormApvLnDVo();
		apFormApvLnDVo.setFormId(tplFormId);
		@SuppressWarnings("unchecked")
		List<ApFormApvLnDVo> apFormApvLnDVoList = (List<ApFormApvLnDVo>)commonSvc.queryList(apFormApvLnDVo);
		if(apFormApvLnDVoList!=null){
			for(ApFormApvLnDVo storedApFormApvLnDVo : apFormApvLnDVoList){
				storedApFormApvLnDVo.setFormId(newFormId);
				queryQueue.insert(storedApFormApvLnDVo);
			}
		}
		
		// 항목지정
		
		// 양식항목상세(AP_FORM_ITEM_D) 테이블 - 복사
		ApFormItemDVo apFormItemDVo = new ApFormItemDVo();
		apFormItemDVo.setFormId(tplFormId);
		apFormItemDVo.setOrderBy("FORM_COMB_SEQ");
		@SuppressWarnings("unchecked")
		List<ApFormItemDVo> apFormItemDVoList = (List<ApFormItemDVo>) commonSvc.queryList(apFormItemDVo);
		if(apFormItemDVoList!=null){
			for(ApFormItemDVo storedApFormItemDVo : apFormItemDVoList){
				storedApFormItemDVo.setFormId(newFormId);
				queryQueue.insert(storedApFormItemDVo);
			}
		}
		
		// 양식항목내역(AP_FORM_ITEM_L) 테이블 - 복사
		ApFormItemLVo apFormItemLVo = new ApFormItemLVo();
		apFormItemLVo.setFormId(tplFormId);
		apFormItemLVo.setOrderBy("FORM_COMB_SEQ, ROW_NO, COL_NO");
		@SuppressWarnings("unchecked")
		List<ApFormItemLVo> apFormItemLVoList = (List<ApFormItemLVo>) commonSvc.queryList(apFormItemLVo);
		if(apFormItemLVoList!=null){
			for(ApFormItemLVo storedApFormItemLVo : apFormItemLVoList){
				storedApFormItemLVo.setFormId(newFormId);
				queryQueue.insert(storedApFormItemLVo);
			}
		}
	}
	
	/** [AJAX] 양식 삭제 */
	@RequestMapping(value = {"/ap/adm/form/transFormDelAjx", "/ap/env/transFormDelAjx"})
	public String transFormDelAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		QueryQueue queryQueue = new QueryQueue();
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String formIds = (String)jsonObject.get("formIds");
		
		int delCnt = 0, inUseCnt=0;
		
		
		if(formIds!=null && !formIds.isEmpty()){
			
			ApFormBVo apFormBVo, storedApFormBVo;
			ApFormCombDVo apFormCombDVo;
			ApFormApvLnDVo apFormApvLnDVo;
			ApFormTxtDVo apFormTxtDVo;
			ApFormImgDVo apFormImgDVo;
			ApFormItemDVo apFormItemDVo;
			ApFormItemLVo apFormItemLVo;
			
			ApOngdBVo apOngdBVo = new ApOngdBVo();
			
			for(String formId : formIds.split(",")){
				
				// 양식기본(AP_FORM_B) 테이블 - 조회
				storedApFormBVo = new ApFormBVo();
				storedApFormBVo.setFormId(formId);
				storedApFormBVo = (ApFormBVo)commonSvc.queryVo(storedApFormBVo);
				if(storedApFormBVo==null) continue;
				
				// 사용중인 양식인지 확인함
				apOngdBVo.setFormId(formId);
				if(commonSvc.count(apOngdBVo) > 0){
					inUseCnt++;
					continue;
				}
				
				// 양식기본(AP_FORM_B) 테이블 - 삭제
				apFormBVo = new ApFormBVo();
				apFormBVo.setFormId(formId);
				queryQueue.delete(apFormBVo);
				// 양식구성상세(AP_FORM_COMB_D) 테이블 - 삭제
				apFormCombDVo = new ApFormCombDVo();
				apFormCombDVo.setFormId(formId);
				queryQueue.delete(apFormCombDVo);
				// 양식결재라인상세(AP_FORM_APV_LN_D) 테이블 - 삭제
				apFormApvLnDVo = new ApFormApvLnDVo();
				apFormApvLnDVo.setFormId(formId);
				queryQueue.delete(apFormApvLnDVo);
				// 양식텍스트상세(AP_FORM_TXT_D) 테이블 - 삭제
				apFormTxtDVo = new ApFormTxtDVo();
				apFormTxtDVo.setFormId(formId);
				queryQueue.delete(apFormTxtDVo);
				// 양식이미지상세(AP_FORM_IMG_D) 테이블 - 삭제
				apFormImgDVo = new ApFormImgDVo();
				apFormImgDVo.setFormId(formId);
				queryQueue.delete(apFormImgDVo);
				// 양식항목상세(AP_FORM_ITEM_D) 테이블 - 삭제
				apFormItemDVo = new ApFormItemDVo();
				apFormItemDVo.setFormId(formId);
				queryQueue.delete(apFormItemDVo);
				// 양식항목내역(AP_FORM_ITEM_L) 테이블 - 삭제
				apFormItemLVo = new ApFormItemLVo();
				apFormItemLVo.setFormId(formId);
				queryQueue.delete(apFormItemLVo);
				
				// 리소스 삭제
				ApRescDVo apRescDVo = new ApRescDVo();
				apRescDVo.setCompId(userVo.getCompId());
				apRescDVo.setRescId(storedApFormBVo.getRescId());
				queryQueue.delete(apRescDVo);
				
				delCnt++;
			}
		}
		
		try {
			
			commonSvc.execute(queryQueue);
			
			if(delCnt == 0 && inUseCnt > 0){
				// ap.msg.form.notDel=사용중인 양식은 삭제 할 수 없습니다.
				model.put("message", messageProperties.getMessage("ap.msg.form.notDel", request));
			} else {
				// ap.msg.form.delSucc={0}건의 양식이 삭제 되었습니다.
				model.put("message", messageProperties.getMessage("ap.msg.form.delSucc", new String[]{""+delCnt}, request));
				model.put("result", "ok");
			}
			
		} catch(Exception e){
			model.put("message", e.getMessage());
		}
		
		return LayoutUtil.returnJson(model);
	}
}
