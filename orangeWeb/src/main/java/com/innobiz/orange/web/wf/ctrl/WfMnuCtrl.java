package com.innobiz.orange.web.wf.ctrl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
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
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtMnuDVo;
import com.innobiz.orange.web.pt.vo.PtMnuGrpBVo;
import com.innobiz.orange.web.pt.vo.PtRescBVo;
import com.innobiz.orange.web.wf.svc.WfAdmSvc;
import com.innobiz.orange.web.wf.svc.WfRescSvc;
import com.innobiz.orange.web.wf.vo.WfFormBVo;

@Controller
public class WfMnuCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WfMnuCtrl.class);
	
	/** 관리 서비스 */
	@Resource(name = "wfAdmSvc")
	private WfAdmSvc wfAdmSvc;
	
	/** 리소스 조회 저장용 서비스 */
	@Resource(name = "wfRescSvc")
	private WfRescSvc wfRescSvc;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;
	
	/** [팝업] - 메뉴등록 화면 */
	@RequestMapping(value = "/wf/adm/form/setMnuPop")
	public String setMnuPop(HttpServletRequest request,
			@RequestParam(value = "valUM", required = false) String valUM,
			ModelMap model) throws Exception {
	
		// 관리자 여부
		boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
				
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 전체회사 목록 조회[시스템관리자]
		if(isSysAdmin){
			wfAdmSvc.setCompAffiliateVoList(model, userVo.getCompId(), langTypCd, isSysAdmin);
		/*List<PtCompBVo> ptCompBVoList = wfAdmSvc.setCompAffiliateVoList(model, userVo.getCompId(), langTypCd, isSysAdmin);
		if(ptCompBVoList!=null && ptCompBVoList.size()>0){
			model.put("paramCompId", ptCompBVoList.get(0).getCompId());
		}*/
		}
		model.put("paramCompId", userVo.getCompId());
		
		return LayoutUtil.getJspPath("/wf/adm/form/setMnuPop");
	}
	
	/** [프레임] - 메뉴등록 화면 */
	@RequestMapping(value = "/wf/adm/form/setMnuFrm")
	public String setMnuFrm(HttpServletRequest request,
			@RequestParam(value = "paramCompId", required = false) String paramCompId,
			@RequestParam(value = "valUM", required = false) String valUM,
			ModelMap model) throws Exception {
		
		if(valUM==null || valUM.isEmpty()){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 관리자 여부
		boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		
		// 회사ID
		String compId=isSysAdmin && paramCompId!=null && !paramCompId.isEmpty() ? paramCompId : userVo.getCompId();
		
		if(isSysAdmin && "all".equals(paramCompId)){
			compId="0";
		}
		
		// 메뉴그룹기본(PT_MNU_GRP_B) 테이블 - SELECT
		PtMnuGrpBVo ptMnuGrpBVo = new PtMnuGrpBVo();
		ptMnuGrpBVo.setSysgYn("N");
		ptMnuGrpBVo.setMnuGrpMdCd(valUM);
		ptMnuGrpBVo.setUseYn("Y");
		ptMnuGrpBVo.setQueryLang(langTypCd);
		ptMnuGrpBVo.setOpenCompId(compId);
		@SuppressWarnings("unchecked")
		List<PtMnuGrpBVo> ptMnuGrpBVoList = (List<PtMnuGrpBVo>) commonSvc.queryList(ptMnuGrpBVo);
		
		List<PtMnuGrpBVo> mnuGrpList=new ArrayList<PtMnuGrpBVo>();		
		for(PtMnuGrpBVo storedPtMnuGrpBVo : ptMnuGrpBVoList){
			if(!ArrayUtil.isInArray(new String[]{"01", "03"}, storedPtMnuGrpBVo.getMnuGrpTypCd())) continue;
			mnuGrpList.add(storedPtMnuGrpBVo);
		}
		
		String firstMnuGrpId=null;
		if(mnuGrpList != null && !mnuGrpList.isEmpty()){
			model.put("ptMnuGrpBVoList", mnuGrpList);
			firstMnuGrpId = mnuGrpList.get(0).getMnuGrpId();
			model.put("firstMnuGrpId", firstMnuGrpId);
		}
		
		return LayoutUtil.getJspPath("/wf/adm/form/setMnuFrm");
	}
	
	/** [프레임] - 메뉴 트리 */
	@RequestMapping(value = "/wf/adm/form/treeMnuFrm")
	public String treeMnuFrm(HttpServletRequest request,
			@RequestParam(value = "mnuGrpId", required = false) String mnuGrpId,
			@RequestParam(value = "mnuId", required = false) String mnuId,
			@RequestParam(value = "noInit", required = false) String noInit,
			ModelMap model) throws Exception {
		
		if(mnuGrpId==null || mnuGrpId.isEmpty()){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
	
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 메뉴상세(PT_MNU_D) 테이블
		PtMnuDVo ptMnuDVo = new PtMnuDVo();
		ptMnuDVo.setMnuGrpId(mnuGrpId);
		ptMnuDVo.setQueryLang(langTypCd);
		ptMnuDVo.setOrderBy("T.SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<PtMnuDVo> ptMnuDVoList = (List<PtMnuDVo>)commonSvc.queryList(ptMnuDVo);
		
		String firstId=null;
		if(ptMnuDVoList != null && !ptMnuDVoList.isEmpty()){
			model.put("ptMnuDVoList", ptMnuDVoList);
			firstId = ptMnuDVoList.get(0).getMnuId();
		}
		
		if(!"Y".equals(noInit)){
			if(mnuId != null && !mnuId.isEmpty()){
				model.put("selectedNode", mnuId);
			} else if(firstId != null && !firstId.isEmpty()){
				model.put("selectedNode", firstId);
			} else {
				model.put("selectedNode", "ROOT");
			}
		}
		
		return LayoutUtil.getJspPath("/wf/adm/form/treeMnuFrm");
	}
	
	/** [AJAX] - 메뉴순서변경 - 일괄저장 */
	@RequestMapping(value = "/wf/adm/form/transMnuMoveSaveAjx")
	public String transMnuMoveSaveAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		
		// 메뉴 JSON String
		String mnuList = (String)jsonObject.get("mnuList");
		if(mnuList == null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		// JSON String 을 Map 으로 변환
		Map<String, List<String>> mnuMap = new ObjectMapper().readValue(mnuList.toString(), new TypeReference<Map<String, List<String>>>(){}) ;
		
		if(mnuMap==null || mnuMap.size()==0){
			//cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		Entry<String, List<String>> entry;
		Iterator<Entry<String, List<String>>> iterator = mnuMap.entrySet().iterator();
		List<String> mnuIdList=null;
		PtMnuDVo storedPtMnuDVo;
		String mnuId;
		
		QueryQueue queryQueue = new QueryQueue();
		while(iterator.hasNext()){
			entry = iterator.next();
			mnuIdList=entry.getValue();
			for(int i=0;i<mnuIdList.size();i++){
				mnuId = (String)mnuIdList.get(i);
				storedPtMnuDVo = new PtMnuDVo();
				storedPtMnuDVo.setMnuPid(entry.getKey());
				storedPtMnuDVo.setMnuId(mnuId);
				storedPtMnuDVo.setSortOrdr(String.valueOf(i+1));
				queryQueue.update(storedPtMnuDVo);
			}
		}
		
		if(!queryQueue.isEmpty()){
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, 
					CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
						
		} else {
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			model.put("message",  messageProperties.getMessage("cm.msg.notValidCall", request));
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] 메뉴저장 (관리자) */
	@RequestMapping(value = "/wf/adm/form/transMnuAjx")
	public String transMnuAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) {

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String mnuGrpId = (String) object.get("mnuGrpId");
			String mnuId = (String) object.get("mnuId");
			String valUM = (String) object.get("valUM");
			JSONArray jsonArray = (JSONArray) object.get("formNos");
			if (mnuGrpId==null || mnuGrpId.isEmpty() ||mnuId==null || mnuId.isEmpty() || valUM==null || valUM.isEmpty() || jsonArray==null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 메뉴그룹 조회
			PtMnuGrpBVo ptMnuGrpBVo = new PtMnuGrpBVo();
			ptMnuGrpBVo.setSysgYn("N");
			ptMnuGrpBVo.setMnuGrpMdCd(valUM);
			ptMnuGrpBVo.setUseYn("Y");
			ptMnuGrpBVo.setMnuGrpId(mnuGrpId);
			ptMnuGrpBVo=(PtMnuGrpBVo)commonSvc.queryVo(ptMnuGrpBVo);
			
			// 모듈참조ID
			String mdRid = ptMnuGrpBVo!=null && ptMnuGrpBVo.getMdRid()!=null && "WF".equals(ptMnuGrpBVo.getMdRid()) ? "WF" : null;
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);

			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 시스탬 관리자 여부
			boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
			
			PtRescBVo firstPtRescBVo;
			String formNo, rescId, ptRescVa, ptLangTypCd;
			Map<String, String> formRescMap;
			QueryQueue queryQueue = new QueryQueue();
			
			// 회사 ID
			String compId=isSysAdmin ? null : userVo.getCompId();
			for (int i = 0; i < jsonArray.size(); i++) {
				formNo = (String) jsonArray.get(i);
				
				WfFormBVo wfFormBVo = new WfFormBVo();
				wfFormBVo.setQueryLang(langTypCd);
				wfFormBVo.setFormNo(formNo);
				if(compId!=null) wfFormBVo.setCompId(compId); // 시스템관리자가 아닐경우 회사ID 삽입
				wfFormBVo=(WfFormBVo)commonSvc.queryVo(wfFormBVo);
				
				// 양식이 없을 경우 continue
				if(wfFormBVo==null) continue;
				
				// 양식 리소스맵[vo to map]
				formRescMap=wfRescSvc.getWfRescBVoToMap(wfFormBVo.getRescId());
				if(formRescMap==null) continue;
				
				// 회사의 언어 목록 조회
				List<PtCdBVo> ptCdBVoList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
				
				// 리소스ID
				rescId = null;
				
				firstPtRescBVo = null;
				
				for (PtCdBVo ptCdBVo : ptCdBVoList) {
					ptLangTypCd=ptCdBVo.getCd();
					ptRescVa=formRescMap.get(ptLangTypCd);
					if(ptRescVa==null) continue;
					if(rescId==null) rescId = ptCmSvc.createId("PT_RESC_B");
					
					PtRescBVo ptRescBVo = new PtRescBVo();
					ptRescBVo.setRescId(rescId);
					ptRescBVo.setLangTypCd(ptLangTypCd);
					ptRescBVo.setRescVa(ptRescVa);

					if (firstPtRescBVo == null || "ko".equals(ptCdBVo.getCd())) {
						firstPtRescBVo = ptRescBVo;
					}

					// 리소스기본(PT_RESC_B) 테이블 - INSERT
					queryQueue.insert(ptRescBVo);
				}

				if (firstPtRescBVo == null) {
					// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
					throw new CmException("pt.msg.nodata.passed", request);
				}
				
				// 메뉴상세(PT_MNU_D) 테이블 - INSERT
				PtMnuDVo ptMnuDVo = new PtMnuDVo();
				ptMnuDVo.setMnuId(ptCmSvc.createId("PT_MNU_D")); // "PT_MNU_D", 'M', 8
				ptMnuDVo.setMnuGrpId(mnuGrpId);
				ptMnuDVo.setMnuGrpMdCd(valUM);
				ptMnuDVo.setMnuNm(firstPtRescBVo.getRescVa());
				ptMnuDVo.setRescId(firstPtRescBVo.getRescId());
				ptMnuDVo.setMnuPid("ROOT".equals(mnuId) ? mnuGrpId : mnuId);
				ptMnuDVo.setMnuTypCd("IN_URL");
				ptMnuDVo.setPopSetupCont("");
				ptMnuDVo.setMnuDesc("");
				ptMnuDVo.setFldYn("N");
				ptMnuDVo.setSysMnuYn("N");
				ptMnuDVo.setMdRid(mdRid);
				if(mdRid!=null) ptMnuDVo.setMdId(formNo);
				ptMnuDVo.setUseYn("Y");
				ptMnuDVo.setModDt("sysdate");
				ptMnuDVo.setModrUid(userVo.getUserUid());
				ptMnuDVo.setMnuUrl("/wf/works/listWorks.do?formNo="+formNo);
				
				queryQueue.insert(ptMnuDVo);
			}

			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime,
					CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
			
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

	/** [AJAX] 메뉴삭제 (관리자) */
	@RequestMapping(value = "/wf/adm/form/transMnuDelAjx")
	public String transMnuDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) {

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String mnuId = (String) object.get("mnuId");
			if (mnuId == null || mnuId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			// 메뉴상세(PT_MNU_D) 테이블 - SELECT
			PtMnuDVo ptMnuDVo = new PtMnuDVo();
			ptMnuDVo.setMnuId(mnuId);
			ptMnuDVo = (PtMnuDVo) commonSvc.queryVo(ptMnuDVo);

			if (ptMnuDVo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			QueryQueue queryQueue = new QueryQueue();
			// 리소스기본(PT_RESC_B) 테이블 - DELETE
			PtRescBVo ptRescBVo = new PtRescBVo();
			ptRescBVo.setRescId(ptMnuDVo.getRescId());
			queryQueue.delete(ptRescBVo);

			// 메뉴상세(PT_MNU_D) 테이블 - DELETE
			queryQueue.delete(ptMnuDVo);

			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime,
					CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
			
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
	
}
