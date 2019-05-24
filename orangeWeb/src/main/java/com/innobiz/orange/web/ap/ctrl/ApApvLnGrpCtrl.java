package com.innobiz.orange.web.ap.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.ap.svc.ApApvLnSvc;
import com.innobiz.orange.web.ap.svc.ApCmSvc;
import com.innobiz.orange.web.ap.svc.ApRescSvc;
import com.innobiz.orange.web.ap.vo.ApApvLnGrpBVo;
import com.innobiz.orange.web.ap.vo.ApApvLnGrpDVo;
import com.innobiz.orange.web.ap.vo.ApOngdApvLnDVo;
import com.innobiz.orange.web.ap.vo.ApRescBVo;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtxSortOrdrChnVo;

/** 결재경로 */
@Controller
public class ApApvLnGrpCtrl {
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 결재선 서비스 */
	@Autowired
	private ApApvLnSvc apApvLnSvc;

	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 결재 공통 서비스 */
	@Autowired
	private ApCmSvc apCmSvc;

//	/** 조직 공통 서비스 */
//	@Autowired
//	private OrCmSvc orCmSvc;
	
	/** 결재 리소스 처리 서비스 */
	@Autowired
	private ApRescSvc apRescSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	
	/** 경로그룹 목록 조회 - 경로지정 > [탭3]경로그룹 > 좌측 프레임 */
	@RequestMapping(value = {"/ap/box/listApvLnGrpFrm", "/ap/adm/box/listApvLnGrpFrm",
			"/ap/env/listApvLnGrpPop","/ap/adm/form/listApvLnGrpPop",
			"/ap/env/listRefVwGrpPop","/ap/adm/form/listRefVwGrpPop",
			"/ap/box/listRefVwGrpFrm"})
	public String listApvLnGrpFrm(HttpServletRequest request,
			@Parameter(name="apvLnGrpTypCd", required=false) String apvLnGrpTypCd,
			@Parameter(name="fixdApvrYn", required=false) String fixdApvrYn,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		
		String uri = request.getRequestURI();
		boolean isApvLnFormEdit = uri.indexOf("listApvLnGrpPop")>0;
		boolean isRefVwFormEdit = uri.indexOf("listRefVwGrpPop")>0;
		if(apvLnGrpTypCd==null){
			if(isApvLnFormEdit) apvLnGrpTypCd = "pub";
			else if(isRefVwFormEdit) apvLnGrpTypCd = "pubRef";
		}
		
		boolean isPub = "pub".equals(apvLnGrpTypCd) || "pubRef".equals(apvLnGrpTypCd);
		
		ApApvLnGrpBVo apApvLnGrpBVo = new ApApvLnGrpBVo();
		apApvLnGrpBVo.setUserUid(!isPub ? userVo.getUserUid() : userVo.getCompId());
		apApvLnGrpBVo.setApvLnGrpTypCd(apvLnGrpTypCd);
		apApvLnGrpBVo.setQueryLang(langTypCd);
		apApvLnGrpBVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<ApApvLnGrpBVo> apApvLnGrpBVoList = (List<ApApvLnGrpBVo>)commonSvc.queryList(apApvLnGrpBVo);
		if(apApvLnGrpBVoList!=null){
			model.put("apApvLnGrpBVoList", apApvLnGrpBVoList);
		}
		
		if(isApvLnFormEdit || isRefVwFormEdit){
			if(isApvLnFormEdit){
				// 자동결재선코드
				List<PtCdBVo> autoApvLnCdList = ptCmSvc.getCdList("AUTO_APV_LN_CD", langTypCd, "Y");
				model.put("autoApvLnCdList", autoApvLnCdList);
				
				model.put("apvLnFormEdit", Boolean.TRUE);
			} else {
				model.put("refVwFormEdit", Boolean.TRUE);
			}
			return LayoutUtil.getJspPath("/ap/adm/form/listApvLnGrpPop");
		}
		
		return LayoutUtil.getJspPath("/ap/box/listApvLnGrpFrm");
	}
	
	/** 경로그룹 수정/등록 - 경로지정 > [탭3]경로그룹 > 좌측 프레임 */
	@RequestMapping(value = "/ap/box/setApvLnGrpPop")
	public String setApvLnGrpPop(HttpServletRequest request,
			@Parameter(name="apvLnGrpTypCd", required=false) String apvLnGrpTypCd,
			@Parameter(name="apvLnGrpId", required=false) String apvLnGrpId,
			ModelMap model) throws Exception {
		
		if(apvLnGrpId!=null && !apvLnGrpId.isEmpty()){
			UserVo userVo = LoginSession.getUser(request);
			String langTypCd = LoginSession.getLangTypCd(request);
			boolean isPub = "pub".equals(apvLnGrpTypCd) || "pubRef".equals(apvLnGrpTypCd);
			
			ApApvLnGrpBVo apApvLnGrpBVo = new ApApvLnGrpBVo();
			apApvLnGrpBVo.setApvLnGrpId(apvLnGrpId);
			apApvLnGrpBVo.setUserUid(!isPub ? userVo.getUserUid() : userVo.getCompId());
			apApvLnGrpBVo.setQueryLang(langTypCd);
			
			apApvLnGrpBVo = (ApApvLnGrpBVo)commonSvc.queryVo(apApvLnGrpBVo);
			if(apApvLnGrpBVo!=null){
				model.put("apApvLnGrpBVo", apApvLnGrpBVo);
				
				// model에 리소스 세팅
				if(isPub && apApvLnGrpBVo.getRescId()!=null && !apApvLnGrpBVo.getRescId().isEmpty()){
					ApRescBVo apRescBVo = new ApRescBVo();
					apRescBVo.setRescId(apApvLnGrpBVo.getRescId());
					@SuppressWarnings("unchecked")
					List<ApRescBVo> apRescBVoList = (List<ApRescBVo>)commonSvc.queryList(apRescBVo);
					if(apRescBVoList!=null){
						for(ApRescBVo storedApRescBVo : apRescBVoList){
							model.put(storedApRescBVo.getRescId()+"_"+storedApRescBVo.getLangTypCd(), storedApRescBVo.getRescVa());
						}
					}
				}
			}
		}
		return LayoutUtil.getJspPath("/ap/box/setApvLnGrpPop");
	}
	
	/** 경로그룹 저장 - 경로지정 > [탭3]경로그룹 > 좌측 프레임 */
	@RequestMapping(value = "/ap/box/transApvLnGrp")
	public String transApvLnGrp(HttpServletRequest request,
			@Parameter(name="menuId", required=false) String menuId,
			@Parameter(name="bxId", required=false) String bxId,
			@Parameter(name="formId", required=false) String formId,
			@Parameter(name="apvLnGrpTypCd", required=false) String apvLnGrpTypCd,
			@Parameter(name="apvLnGrpId", required=false) String apvLnGrpId,
			@Parameter(name="apvLnGrpNm", required=false) String apvLnGrpNm,
			ModelMap model, Locale locale) throws Exception {
		
		try{
			
			QueryQueue queryQueue = new QueryQueue();
			
			UserVo userVo = LoginSession.getUser(request);
			boolean isPub =    "pub".equals(apvLnGrpTypCd) || "pubRef".equals(apvLnGrpTypCd);
			boolean isRef = "prvRef".equals(apvLnGrpTypCd) || "pubRef".equals(apvLnGrpTypCd);
			
			boolean isNewGrp = false;
			if(apvLnGrpId==null || apvLnGrpId.isEmpty()){
				isNewGrp = true;
				apvLnGrpId = apCmSvc.createId("AP_APV_LN_GRP_B");
			}
			
			ApApvLnGrpBVo apApvLnGrpBVo = new ApApvLnGrpBVo();
			apApvLnGrpBVo.setApvLnGrpId(apvLnGrpId);
			apApvLnGrpBVo.setApvLnGrpTypCd(apvLnGrpTypCd);
			if(!isPub){
				apApvLnGrpBVo.setUserUid(userVo.getUserUid());
				apApvLnGrpBVo.setApvLnGrpNm(apvLnGrpNm);
			} else {
				apApvLnGrpBVo.setUserUid(userVo.getCompId());
				ApRescBVo apRescBVo = apRescSvc.collectApRescBVo(request, null, queryQueue);
				if(apRescBVo!=null){
					apApvLnGrpBVo.setRescId(apRescBVo.getRescId());
					apApvLnGrpBVo.setApvLnGrpNm(apRescBVo.getRescNm());
				}
			}
			
			apApvLnGrpBVo.setModrUid(userVo.getUserUid());
			apApvLnGrpBVo.setModDt("sysdate");
			
			if(isNewGrp){
				queryQueue.insert(apApvLnGrpBVo);
			} else {
				queryQueue.store(apApvLnGrpBVo);
			}
			
			commonSvc.execute(queryQueue);
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", locale));
			if(isRef){
				model.put("todo", "parent.dialog.close('setApvLnGrpDialog');"
						+" parent.getIframeContent('refVwGrpFrm').location.href = "
						+"'./listRefVwGrpFrm.do?menuId="+menuId+"&bxId="+bxId
						+(formId==null||formId.isEmpty() ? "" : "&formId="+formId)
						+(isPub?"&apvLnGrpTypCd=pubRef":"&apvLnGrpTypCd=prvRef")+"&apvLnGrpId="+apvLnGrpId+"';");
			} else {
				model.put("todo", "parent.dialog.close('setApvLnGrpDialog');"
						+" parent.getIframeContent('apvLnGrpFrm').location.href = "
						+"'./listApvLnGrpFrm.do?menuId="+menuId+"&bxId="+bxId
						+(formId==null||formId.isEmpty() ? "" : "&formId="+formId)
						+(isPub?"&apvLnGrpTypCd=pub":"&apvLnGrpTypCd=prv")+"&apvLnGrpId="+apvLnGrpId+"';");
			}
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		return LayoutUtil.getResultJsp();
	}
	
	/** [AJX] 경로그룹 삭제 - 경로지정 > [탭3]경로그룹 > 좌측 프레임 */
	@RequestMapping(value = "/ap/box/transApvLnGrpDelAjx")
	public String transApvLnGrpDelAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception{
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		
		String apvLnGrpTypCd = (String)jsonObject.get("apvLnGrpTypCd");
		String apvLnGrpId = (String)jsonObject.get("apvLnGrpId");
		
		UserVo userVo = LoginSession.getUser(request);
		boolean isPub = "pub".equals(apvLnGrpTypCd) || "pubRef".equals(apvLnGrpTypCd);
		
		QueryQueue queryQueue = new QueryQueue();
		
		try {

			// 결재라인그룹기본(AP_APV_LN_GRP_B) 테이블 - 삭제
			ApApvLnGrpBVo apApvLnGrpBVo = new ApApvLnGrpBVo();
			apApvLnGrpBVo.setApvLnGrpId(apvLnGrpId);
			apApvLnGrpBVo.setUserUid(!isPub ? userVo.getUserUid() : userVo.getCompId());
			queryQueue.delete(apApvLnGrpBVo);
			
			// 결재라인그룹상세(AP_APV_LN_GRP_D) 테이블 - 삭제
			ApApvLnGrpDVo apApvLnGrpDVo = new ApApvLnGrpDVo();
			apApvLnGrpDVo.setApvLnGrpId(apvLnGrpId);
			apApvLnGrpDVo.setUserUid(!isPub ? userVo.getUserUid() : userVo.getCompId());
			queryQueue.delete(apApvLnGrpDVo);
			
			// 리소스 지우기 위한 테이블 조회
			apApvLnGrpBVo = new ApApvLnGrpBVo();
			apApvLnGrpBVo.setApvLnGrpId(apvLnGrpId);
			if(!isPub){
				apApvLnGrpBVo.setUserUid(userVo.getUserUid());
			} else {
				apApvLnGrpBVo.setUserUid(userVo.getCompId());
			}
			apApvLnGrpBVo = (ApApvLnGrpBVo)commonSvc.queryVo(apApvLnGrpBVo);
			
			if(apApvLnGrpBVo != null && apApvLnGrpBVo.getRescId() != null && !apApvLnGrpBVo.getRescId().isEmpty()){
				// 리소스기본(AP_RESC_B) 테이블 - 삭제
				ApRescBVo apRescBVo = new ApRescBVo();
				apRescBVo.setRescId(apApvLnGrpBVo.getRescId());
				queryQueue.delete(apRescBVo);
			}
			
			commonSvc.execute(queryQueue);
			model.put("result", "ok");
			// cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", locale));
			
		} catch(Exception e){
			String message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
			model.put("message", message);
		}
		
		return LayoutUtil.returnJson(model);
	}
	
	/** [프레임] 경로그룹 상세 저장 */
	@RequestMapping(value = "/ap/box/transApvLnGrpDetl")
	public String transApvLnGrpDetl(HttpServletRequest request,
			@Parameter(name="menuId", required=false) String menuId,
			@Parameter(name="bxId", required=false) String bxId,
			@Parameter(name="formId", required=false) String formId,
			@Parameter(name="apvLnGrpId", required=false) String apvLnGrpId,
			@Parameter(name="apvLnGrpTypCd", required=false) String apvLnGrpTypCd,
			ModelMap model, Locale locale) throws Exception{
		
		String[] apvLns = request.getParameterValues("apvLn");
		
		UserVo userVo = LoginSession.getUser(request);
		boolean isPub =    "pub".equals(apvLnGrpTypCd) || "pubRef".equals(apvLnGrpTypCd);
		boolean isRef = "prvRef".equals(apvLnGrpTypCd) || "pubRef".equals(apvLnGrpTypCd);
		
		QueryQueue queryQueue = new QueryQueue();
		
		// 결재라인그룹상세(AP_APV_LN_GRP_D) 테이블 - 삭제
		ApApvLnGrpDVo apApvLnGrpDVo = new ApApvLnGrpDVo();
		apApvLnGrpDVo.setApvLnGrpId(apvLnGrpId);
		apApvLnGrpDVo.setUserUid(!isPub ? userVo.getUserUid() : userVo.getCompId());
		queryQueue.delete(apApvLnGrpDVo);
		
		boolean first = true;
		Integer seq = 1;
		JSONObject jsonObject;
		if(apvLns!=null){
			for(String apvLn : apvLns){
				if(!isRef && first) {
					first = false;
					continue;
				}
				jsonObject = (JSONObject)JSONValue.parse(apvLn);
				
				apApvLnGrpDVo = new ApApvLnGrpDVo();
				apApvLnGrpDVo.setUserUid(!isPub ? userVo.getUserUid() : userVo.getCompId());
				apApvLnGrpDVo.setApvLnGrpId(apvLnGrpId);
				apApvLnGrpDVo.setApvLnGrpSeq(seq.toString());
				seq++;
				apApvLnGrpDVo.setApvrUid((String)jsonObject.get("apvrUid"));
				apApvLnGrpDVo.setApvDeptId((String)jsonObject.get("apvDeptId"));
				if(!isRef){
					apApvLnGrpDVo.setApvrDeptYn((String)jsonObject.get("apvrDeptYn"));
					apApvLnGrpDVo.setApvrRoleCd((String)jsonObject.get("apvrRoleCd"));
					apApvLnGrpDVo.setDblApvTypCd((String)jsonObject.get("dblApvTypCd"));
					apApvLnGrpDVo.setAbsRsonCd((String)jsonObject.get("absRsonCd"));
				}
				
				queryQueue.insert(apApvLnGrpDVo);
			}
		}
		
		try{
			
			commonSvc.execute(queryQueue);
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", locale));
			if(isRef){
				model.put("todo", "parent.location.replace('./listApvLnGrpDetlFrm.do?menuId="+menuId
						+"&bxId="+bxId
						+(formId==null||formId.isEmpty() ? "" : "&formId="+formId)
						+"&apvLnGrpId="+apvLnGrpId
						+(isPub ? "&apvLnGrpTypCd=pubRef" : "&apvLnGrpTypCd=prvRef")
						+"');");
			} else {
				model.put("todo", "parent.location.replace('./listApvLnGrpDetlFrm.do?menuId="+menuId
						+"&bxId="+bxId
						+(formId==null||formId.isEmpty() ? "" : "&formId="+formId)
						+"&apvLnGrpId="+apvLnGrpId
						+(isPub ? "&apvLnGrpTypCd=pub" : "&apvLnGrpTypCd=prv")
						+"');");
			}
			
		} catch(Exception e){
			String message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
			model.put("message", message);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** 경로그룹 별 결재선(사용자 or 부서)목록 조회 - 경로지정 > [탭3]경로그룹 > 우측 프레임 */
	@RequestMapping(value = {"/ap/box/listApvLnGrpDetlFrm", "/ap/adm/box/listApvLnGrpDetlFrm"})
	public String listApvLnGrpDetlFrm(HttpServletRequest request,
			@Parameter(name="apvLnGrpTypCd", required=false) String apvLnGrpTypCd,
			@Parameter(name="apvLnGrpId", required=false) String apvLnGrpId,
			@Parameter(name="formApvLnTypCd", required=false) String formApvLnTypCd,
			@Parameter(name="autoApvLnCd", required=false) String autoApvLnCd,
			@Parameter(name="fixdApvrYn", required=false) String fixdApvrYn,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		List<ApApvLnGrpDVo> apApvLnGrpDVoList = null;
		
		boolean isPub =    "pub".equals(apvLnGrpTypCd) || "pubRef".equals(apvLnGrpTypCd);
		boolean isRef = "prvRef".equals(apvLnGrpTypCd) || "pubRef".equals(apvLnGrpTypCd);
		boolean isDblLn = ("apvLnDbl".equals(formApvLnTypCd) || "apvLnDblList".equals(formApvLnTypCd));
		boolean isFixd = "Y".equals(fixdApvrYn);
		
		String rescVa;
		OrUserBVo orUserBVo;
		OrOrgBVo orOrgBVo;
		ApOngdApvLnDVo apOngdApvLnDVo;
		
		Map<Integer, OrUserBVo> userCacheMap = new HashMap<Integer, OrUserBVo>();
		Map<Integer, OrOrgBVo> orgCacheMap = new HashMap<Integer, OrOrgBVo>();
		
		// 경로그룹의 결재자 목록 조회
		if(apvLnGrpId!=null && !apvLnGrpId.isEmpty()){
			ApApvLnGrpDVo apApvLnGrpDVo = new ApApvLnGrpDVo();
			apApvLnGrpDVo.setUserUid(!isPub ? userVo.getUserUid() : userVo.getCompId());
			apApvLnGrpDVo.setApvLnGrpId(apvLnGrpId);
			apApvLnGrpDVo.setQueryLang(langTypCd);
			@SuppressWarnings("unchecked")
			List<ApApvLnGrpDVo> apApvLnGrpDVoList2 = (List<ApApvLnGrpDVo>)commonSvc.queryList(apApvLnGrpDVo);
			
			// 상신자 & 상신부서 제거
			if(apApvLnGrpDVoList2 != null && !isRef){
				
				String userUid = userVo.getUserUid();
				String deptId = userVo.getDeptId();
				
				int i, size = apApvLnGrpDVoList2.size();
				for(i=0;i<size;i++){
					apApvLnGrpDVo = apApvLnGrpDVoList2.get(i);
					// 결재선에 동일 사용자(상신자)가 있고, 개인통보가 아니면
					if(userUid.equals(apApvLnGrpDVo.getApvrUid())
							&& !"psnInfm".equals(apApvLnGrpDVo.getApvrRoleCd())){
						apApvLnGrpDVoList2.remove(i);
						i--;
						size--;
					// 결재선에 동일 부서(상신부서)가 있고, 부서통보가 아니면
					} else if("Y".equals(apApvLnGrpDVo.getApvrDeptYn())
							&& deptId.equals(apApvLnGrpDVo.getApvDeptId())
							&& !"deptInfm".equals(apApvLnGrpDVo.getApvrRoleCd())){
						apApvLnGrpDVoList2.remove(i);
						i--;
						size--;
					}
				}
			}
			
			if(apApvLnGrpDVoList2!=null && !apApvLnGrpDVoList2.isEmpty()){
				apApvLnGrpDVoList = apApvLnGrpDVoList2;
			}
		}
		
		// 자동결재선코드 에 해당하는 결재자 목록
		List<ApOngdApvLnDVo> autoApOngdApvLnDVoList = null;
		if(autoApvLnCd!=null && !autoApvLnCd.isEmpty()){
			List<OrUserBVo> autoUserList = apApvLnSvc.getAutoApvLnUserList(userVo, autoApvLnCd, apApvLnGrpDVoList);
			if(autoUserList!=null){
				
				autoApOngdApvLnDVoList = new ArrayList<ApOngdApvLnDVo>();
				
				for(OrUserBVo autoOrUserBVo : autoUserList){
					
					apOngdApvLnDVo = new ApOngdApvLnDVo();
					
					apOngdApvLnDVo.setApvrUid(autoOrUserBVo.getUserUid());
					apOngdApvLnDVo.setApvrRoleCd("revw");
					apOngdApvLnDVo.setDblApvTypCd(isDblLn ? "reqDept" : null);
					apOngdApvLnDVo.setApvrDeptYn("N");
					
					apOngdApvLnDVo.setApvrNm(autoOrUserBVo.getRescNm());
					apOngdApvLnDVo.setApvrPositCd(autoOrUserBVo.getPositCd());
					apOngdApvLnDVo.setApvrPositNm(autoOrUserBVo.getPositNm());
					apOngdApvLnDVo.setApvrTitleCd(autoOrUserBVo.getTitleCd());
					apOngdApvLnDVo.setApvrTitleNm(autoOrUserBVo.getTitleNm());
					
					apOngdApvLnDVo.setApvDeptId(autoOrUserBVo.getDeptId());
					orOrgBVo = apRescSvc.getOrOrgBVo(autoOrUserBVo.getDeptId(), langTypCd, orgCacheMap);
					
					if(orOrgBVo != null){
						apOngdApvLnDVo.setApvDeptNm(orOrgBVo.getRescNm());
						rescVa = orOrgBVo.getOrgAbbrRescNm();
						if(rescVa==null || rescVa.isEmpty()){
							apOngdApvLnDVo.setApvDeptAbbrNm(orOrgBVo.getRescNm());
						} else {
							apOngdApvLnDVo.setApvDeptAbbrNm(rescVa);
						}
						
						autoApOngdApvLnDVoList.add(apOngdApvLnDVo);
					}
				}
			}
		}
		
		if(apApvLnGrpDVoList!=null){
			
			List<ApOngdApvLnDVo> apOngdApvLnDVoList = new ArrayList<ApOngdApvLnDVo>();
			String dblApvTypCd;
			
			// 이중결재용 결재라인을 가지고 있는지 여부
			boolean hasDblApvLn = false;
			for(ApApvLnGrpDVo storedApApvLnGrpDVo : apApvLnGrpDVoList){
				dblApvTypCd = storedApApvLnGrpDVo.getDblApvTypCd();
				if("reqDept".equals(dblApvTypCd) || "prcDept".equals(dblApvTypCd)){
					hasDblApvLn = true;
					break;
				}
			}
			
			boolean onlyInfm = true;
			String apvrRoleCd;
			for(ApApvLnGrpDVo storedApApvLnGrpDVo : apApvLnGrpDVoList){
				apvrRoleCd = storedApApvLnGrpDVo.getApvrRoleCd();//psnInfm:개인통보, deptInfm:부서통보
				if(!"psnInfm".equals(apvrRoleCd) && !"deptInfm".equals(apvrRoleCd)){
					onlyInfm = false;
					break;
				}
			}
			
			// 이중결재 일때 이중결재 아닌 결재라인이거나 / 이중결재가 아닐때 이중결재 결재라인 인 경우 / 통보만 있는 경우 제외
			boolean disableAll = isRef || onlyInfm ? false : hasDblApvLn != isDblLn;
			
			String apvDeptId;
			for(ApApvLnGrpDVo storedApApvLnGrpDVo : apApvLnGrpDVoList){
				
				apOngdApvLnDVo = new ApOngdApvLnDVo();
				apOngdApvLnDVo.fromMap(storedApApvLnGrpDVo.toMap());
				if(isFixd) apOngdApvLnDVo.setFixdApvrYn("Y");
				// 사용자면
				if(!"Y".equals(storedApApvLnGrpDVo.getApvrDeptYn())){
					
					if(storedApApvLnGrpDVo.getApvrUid()==null) continue;
					
					orUserBVo = apRescSvc.getOrUserBVo(storedApApvLnGrpDVo.getApvrUid(), langTypCd, userCacheMap);
					if(orUserBVo != null){
						apOngdApvLnDVo.setApvrNm(orUserBVo.getRescNm());
						apOngdApvLnDVo.setApvrPositCd(orUserBVo.getPositCd());
						apOngdApvLnDVo.setApvrPositNm(orUserBVo.getPositNm());
						apOngdApvLnDVo.setApvrTitleCd(orUserBVo.getTitleCd());
						apOngdApvLnDVo.setApvrTitleNm(orUserBVo.getTitleNm());
						
						if(disableAll){
							apOngdApvLnDVo.setApvStatCd("apvd");
						// 사용중 상태가 아닌 사용자 - 선택 안되도록 처리하기 위함 - 결재했음으로 코드 변경
						} else if(!"02".equals(orUserBVo.getUserStatCd())){
							apOngdApvLnDVo.setApvStatCd("apvd");
						}
						
						apvDeptId = orUserBVo.getDeptId();
						
					} else {
						continue;
					}
				} else {
					apvDeptId = storedApApvLnGrpDVo.getApvDeptId();
				}
				
				if(apvDeptId==null) continue;
				
				orOrgBVo = apRescSvc.getOrOrgBVo(apvDeptId, langTypCd, orgCacheMap);
				if(orOrgBVo != null){
					
					apOngdApvLnDVo.setApvDeptId(apvDeptId);
					apOngdApvLnDVo.setApvDeptNm(orOrgBVo.getRescNm());
					rescVa = orOrgBVo.getOrgAbbrRescNm();
					if(rescVa==null || rescVa.isEmpty()){
						apOngdApvLnDVo.setApvDeptAbbrNm(orOrgBVo.getRescNm());
					} else {
						apOngdApvLnDVo.setApvDeptAbbrNm(rescVa);
					}
					
					if(disableAll){
						apOngdApvLnDVo.setApvStatCd("apvd");
					} else if(!"Y".equals(orOrgBVo.getUseYn())){
						apOngdApvLnDVo.setApvStatCd("apvd");
					}
				} else {
					continue;
				}
				
				apOngdApvLnDVoList.add(apOngdApvLnDVo);
			}
			
			// 자동결재선코드 에 해당하는 결재자 목록
			if(autoApOngdApvLnDVoList!=null && !autoApOngdApvLnDVoList.isEmpty()){
				
				if(!isDblLn && (onlyInfm || disableAll)){
					// 이중결재가 아니면 - 마지막 것 결재로 바꿈
					autoApOngdApvLnDVoList.get(autoApOngdApvLnDVoList.size()-1).setApvrRoleCd("apv");
				}
				// 자동결재선코드 의 결재자와 - 결재경로의 결재자를 합하여 세팅
				autoApOngdApvLnDVoList.addAll(apOngdApvLnDVoList);
				model.put("apOngdApvLnDVoList", autoApOngdApvLnDVoList);
			} else {
				model.put("apOngdApvLnDVoList", apOngdApvLnDVoList);
			}
			
		} else {
			
			if(autoApOngdApvLnDVoList!=null && !autoApOngdApvLnDVoList.isEmpty()){
				if(!isDblLn){
					autoApOngdApvLnDVoList.get(autoApOngdApvLnDVoList.size()-1).setApvrRoleCd("apv");
				}
				model.put("apOngdApvLnDVoList", autoApOngdApvLnDVoList);
			}
		}
		
		return LayoutUtil.getJspPath("/ap/box/listApvLnGrpDetlFrm");
	}
	
	/** [AJAX] 경로그룹 순서변경 */
	@RequestMapping(value = "/ap/box/transApvLnGrpMoveAjx")
	public String transApvLnGrpMove(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String apvLnGrpTypCd = (String)jsonObject.get("apvLnGrpTypCd");
		String apvLnGrpId = (String)jsonObject.get("apvLnGrpId");
		String direction = (String)jsonObject.get("direction");
		boolean isPub = "pub".equals(apvLnGrpTypCd) || "pubRef".equals(apvLnGrpTypCd);
		
		UserVo userVo = LoginSession.getUser(request);
		
		QueryQueue queryQueue = new QueryQueue();
		PtxSortOrdrChnVo ptxSortOrdrChnVo;
		
		if(apvLnGrpId==null || direction==null){
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			model.put("message", msg);
			return JsonUtil.returnJson(model);
		} else {
			
			ApApvLnGrpBVo apApvLnGrpBVo, storedApApvLnGrpBVo;
			
			// 위로 이동
			if("up".equals(direction)){
				
				// curOrdr - 현재순번
				// stdOrdr - 기준순번 - 현재순번과 기준순번이 같으면 옮길수 없는 것으로 패스
				Integer curOrdr, switchOrdr;
				
				storedApApvLnGrpBVo = new ApApvLnGrpBVo();
				storedApApvLnGrpBVo.setUserUid(!isPub ? userVo.getUserUid() : userVo.getCompId());
				storedApApvLnGrpBVo.setApvLnGrpId(apvLnGrpId);
				storedApApvLnGrpBVo = (ApApvLnGrpBVo)commonSvc.queryVo(storedApApvLnGrpBVo);
				curOrdr = Integer.valueOf(storedApApvLnGrpBVo.getSortOrdr());
				switchOrdr = curOrdr-1;
				
				if(curOrdr.intValue() > 1){
					
					ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
					ptxSortOrdrChnVo.setTabId("AP_APV_LN_GRP_B");
					ptxSortOrdrChnVo.setPkCol("USER_UID");
					ptxSortOrdrChnVo.setPk(!isPub ? userVo.getUserUid() : userVo.getCompId());
					if(!isPub){
						ptxSortOrdrChnVo.setPkCol2("APV_LN_GRP_TYP_CD");
						ptxSortOrdrChnVo.setPk2(apvLnGrpTypCd);
					}
					ptxSortOrdrChnVo.setMoreThen(switchOrdr);
					ptxSortOrdrChnVo.setLessThen(switchOrdr);
					ptxSortOrdrChnVo.setChnVa(1);
					ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
					queryQueue.update(ptxSortOrdrChnVo);
					
					apApvLnGrpBVo = new ApApvLnGrpBVo();
					apApvLnGrpBVo.setUserUid(!isPub ? userVo.getUserUid() : userVo.getCompId());
					if(!isPub){
						ptxSortOrdrChnVo.setPkCol2("APV_LN_GRP_TYP_CD");
						ptxSortOrdrChnVo.setPk2(apvLnGrpTypCd);
					}
					apApvLnGrpBVo.setApvLnGrpId(apvLnGrpId);
					apApvLnGrpBVo.setSortOrdr(switchOrdr.toString());
					queryQueue.update(apApvLnGrpBVo);
					
					commonSvc.execute(queryQueue);
					model.put("result", "ok");
					
				} else {
					//cm.msg.nodata.moveup=위로 이동할 항목이 없습니다.
					model.put("message",  messageProperties.getMessage("cm.msg.nodata.moveup", request));
				}
				
			// 아래로 이동
			} else if("down".equals(direction)){
				
				// curOrdr - 현재순번
				// stdOrdr - 기준순번 - 현재순번과 기준순번이 같으면 옮길수 없는 것으로 패스
				Integer curOrdr, maxSortOrdr, switchOrdr;
				
				ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
				ptxSortOrdrChnVo.setTabId("AP_APV_LN_GRP_B");
				ptxSortOrdrChnVo.setPkCol("USER_UID");
				ptxSortOrdrChnVo.setPk(!isPub ? userVo.getUserUid() : userVo.getCompId());
				if(!isPub){
					ptxSortOrdrChnVo.setPkCol2("APV_LN_GRP_TYP_CD");
					ptxSortOrdrChnVo.setPk2(apvLnGrpTypCd);
				}
				ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.selectMaxSortOrdr");
				maxSortOrdr = commonSvc.queryInt(ptxSortOrdrChnVo);
				
				storedApApvLnGrpBVo = new ApApvLnGrpBVo();
				storedApApvLnGrpBVo.setUserUid(!isPub ? userVo.getUserUid() : userVo.getCompId());
				storedApApvLnGrpBVo.setApvLnGrpId(apvLnGrpId);
				storedApApvLnGrpBVo = (ApApvLnGrpBVo)commonSvc.queryVo(storedApApvLnGrpBVo);
				curOrdr = Integer.valueOf(storedApApvLnGrpBVo.getSortOrdr());
				
				if(maxSortOrdr.intValue() > curOrdr.intValue()){
					
					switchOrdr = curOrdr+1;
					
					ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
					ptxSortOrdrChnVo.setTabId("AP_APV_LN_GRP_B");
					ptxSortOrdrChnVo.setPkCol("USER_UID");
					ptxSortOrdrChnVo.setPk(!isPub ? userVo.getUserUid() : userVo.getCompId());
					if(!isPub){
						ptxSortOrdrChnVo.setPkCol2("APV_LN_GRP_TYP_CD");
						ptxSortOrdrChnVo.setPk2(apvLnGrpTypCd);
					}
					ptxSortOrdrChnVo.setMoreThen(switchOrdr);
					ptxSortOrdrChnVo.setLessThen(switchOrdr);
					ptxSortOrdrChnVo.setChnVa(-1);
					ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
					queryQueue.update(ptxSortOrdrChnVo);
					
					apApvLnGrpBVo = new ApApvLnGrpBVo();
					apApvLnGrpBVo.setUserUid(!isPub ? userVo.getUserUid() : userVo.getCompId());
					apApvLnGrpBVo.setApvLnGrpId(apvLnGrpId);
					apApvLnGrpBVo.setSortOrdr(switchOrdr.toString());
					queryQueue.update(apApvLnGrpBVo);
					
					commonSvc.execute(queryQueue);
					model.put("result", "ok");
					
				} else {
					//cm.msg.nodata.movedown=아래로 이동할 항목이 없습니다.
					model.put("message",  messageProperties.getMessage("cm.msg.nodata.movedown", request));
				}
				
			} else {
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				model.put("message", msg);
			}
			return JsonUtil.returnJson(model);
		}
	}
}
