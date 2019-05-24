package com.innobiz.orange.web.wf.ctrl;

import java.util.List;
import java.util.Locale;

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
import com.innobiz.orange.web.wf.svc.WfCmSvc;
import com.innobiz.orange.web.wf.svc.WfGenSvc;
import com.innobiz.orange.web.wf.vo.WfFormBVo;
import com.innobiz.orange.web.wf.vo.WfFormGenBVo;
import com.innobiz.orange.web.wf.vo.WfFormGenLVo;
import com.innobiz.orange.web.wf.vo.WfFormGenProcLVo;

@Controller
public class WfGenCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WfGenCtrl.class);
	
	/** 공통 서비스 */
	@Autowired
	private WfCmSvc wfCmSvc;
	
	/** 관리 테이블 생성 서비스 */
	@Resource(name = "wfGenSvc")
	private WfGenSvc wfGenSvc;

	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** [팝업] - 배포 */
	@RequestMapping(value = "/wf/adm/form/setGenPop")
	public String setGenPop(HttpServletRequest request,
			@Parameter(name="grpId", required=false) String grpId,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/wf/adm/form/setGenPop");
	}
	
	/** [AJAX] - 양식 배포 - 테이블 생성 */
	@RequestMapping(value = "/wf/adm/form/transDeployAjx")
	public String transDeployAjx(HttpServletRequest request,
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
			
			String genId = wfCmSvc.createId("WF_FORM_GEN_B");
			
			// 기본정보 저장
			wfGenSvc.saveWfFormGenBVo(request, queryQueue, userVo, genId, "N");
			
			// 단건
			if(formNo!=null && !formNo.isEmpty())
				wfGenSvc.saveWfFormGenLVo(request, queryQueue, userVo, genId, formNo);
			
			// 복수
			if(formNoArray!=null && !formNoArray.isEmpty()){
				for(int i=0;i<formNoArray.size();i++){
					formNo = (String)formNoArray.get(i);
					wfGenSvc.saveWfFormGenLVo(request, queryQueue, userVo, genId, formNo);
				}
			}
			
			if(!queryQueue.isEmpty())
				commonSvc.execute(queryQueue);
			
			model.put("result", "ok");
			request.getSession().setAttribute("WF_GEN_ID", genId);
			wfGenSvc.addGenId(genId);
			
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
	
	/** [팝업] 배포 이력 조회 */
	@RequestMapping(value = "/wf/adm/form/listDeployPop")
	public String listDeployPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/wf/adm/form/listDeployPop");
	}
	
	/** [프레임] 배포 이력 조회 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/wf/adm/form/listDeployFrm")
	public String listDeployFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
					
		// 양식생성기본(WF_FORM_GEN_B) 테이블 - 조회
		WfFormGenBVo wfFormGenBVo = new WfFormGenBVo();
		VoUtil.bind(request, wfFormGenBVo);
		wfFormGenBVo.setCompId(userVo.getCompId());
		
		// 페이지 수
		Integer pageRowCnt=10;
		Integer recodeCount = commonSvc.count(wfFormGenBVo);
		PersonalUtil.setFixedPaging(request, wfFormGenBVo, pageRowCnt, recodeCount);
		
		List<WfFormGenBVo> wfFormGenBVoList=(List<WfFormGenBVo>)commonSvc.queryList(wfFormGenBVo);
		
		if(wfFormGenBVoList!=null && wfFormGenBVoList.size()>0){
			WfFormBVo wfFormBVo = null;
			
			WfFormGenLVo wfFormGenLVo = null;
			List<WfFormGenLVo> wfFormGenLVoList = null;
			// 양식생성목록(WF_FORM_GEN_L) 테이블 조회
			for(WfFormGenBVo storedWfFormGenBVo : wfFormGenBVoList){
				wfFormBVo = new WfFormBVo();
				wfFormBVo.setGenId(storedWfFormGenBVo.getGenId());
				if(commonSvc.count(wfFormBVo)==0) storedWfFormGenBVo.setDelYn("Y"); // 삭제여부[현재 양식에서 사용하고 있으면 삭제불가]
				
				wfFormGenLVo = new WfFormGenLVo();
				wfFormGenLVo.setGenId(storedWfFormGenBVo.getGenId());
				wfFormGenLVoList = (List<WfFormGenLVo>)commonSvc.queryList(wfFormGenLVo);
				if(wfFormGenLVoList!=null) storedWfFormGenBVo.setWfFormGenLVoList(wfFormGenLVoList);
			}
		}
		model.put("wfFormGenBVoList", wfFormGenBVoList);
		model.put("recodeCount", recodeCount);
		
		return LayoutUtil.getJspPath("/wf/adm/form/listDeployFrm");
	}
	
	/** [AJAX] - 배포 이력 삭제 */
	@RequestMapping(value = "/wf/adm/form/transDeployDelAjx")
	public String transDeployDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String genId = (String) object.get("genId");
			JSONArray genIdArray = (JSONArray) object.get("genIds");
			if (genId == null && genIdArray == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			QueryQueue queryQueue = new QueryQueue();
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 단건 삭제
			if(genId!=null && !genId.isEmpty())
				wfGenSvc.deleteGenList(queryQueue, userVo, genId);
			
			// 복수 삭제
			if(genIdArray!=null && !genIdArray.isEmpty()){
				for(int i=0;i<genIdArray.size();i++){
					genId = (String)genIdArray.get(i);
					wfGenSvc.deleteGenList(queryQueue, userVo, genId);
				}
			}
			
			if(!queryQueue.isEmpty())
				commonSvc.execute(queryQueue);
			
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
	
	/** [팝업] 배포 진행 내역 */
	@RequestMapping(value = "/wf/adm/form/listDeployProcPop")
	public String listDeployProcPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		// 내용은 스크립트로 Ajax 호출 하여 가져옴
		return LayoutUtil.getJspPath("/wf/adm/form/listDeployProcPop");
	}
	/** [AJX] 배포 진행 내역 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/wf/adm/form/getDeployProcListAjx")
	public String getDeployProcListAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception{
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String genId = (String)jsonObject.get("genId");
		String formNo = (String)jsonObject.get("formNo");
		String strtSeq = (String)jsonObject.get("strtSeq");
		
		String completed="Y";
		// 양식생성기본(WF_FORM_GEN_B) 테이블 - 프로세스 완료여부 조회
		WfFormGenLVo wfFormGenLVo = new WfFormGenLVo();
		wfFormGenLVo.setGenId(genId);
		wfFormGenLVo.setFormNo(formNo);
		wfFormGenLVo = (WfFormGenLVo)commonSvc.queryVo(wfFormGenLVo);
		if(wfFormGenLVo != null){
			// 배포진행상태코드 - preparing:준비중, processing:진행중, completed:완료, error:에러
			String procStatCd = wfFormGenLVo.getProcStatCd();
			if("preparing".equals(procStatCd) || "processing".equals(procStatCd)){
				completed="N";
			}
			if("error".equals(procStatCd)){
				model.put("hasError", "Y");
			}
		}
		
		// 양식생성진행내역(WF_FORM_GEN_PROC_L) 테이블 - 개별 내역 조회
		WfFormGenProcLVo wfFormGenProcLVo = new WfFormGenProcLVo();
		wfFormGenProcLVo.setGenId(genId);
		wfFormGenProcLVo.setFormNo(formNo);
		if(strtSeq != null && !strtSeq.isEmpty()) wfFormGenProcLVo.setStrtSeq(strtSeq.toString());
		List<WfFormGenProcLVo> wfFormGenProcLVoList = (List<WfFormGenProcLVo>)commonSvc.queryList(wfFormGenProcLVo);
		model.put("procList", wfFormGenProcLVoList);
		
		if(wfFormGenProcLVoList==null || wfFormGenProcLVoList.size()==0)
			completed="Y";
		
		model.put("completed", completed);
		
		return LayoutUtil.returnJson(model);
	}
	
	
}
