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

import com.innobiz.orange.web.ap.svc.ApCmSvc;
import com.innobiz.orange.web.ap.svc.ApRescSvc;
import com.innobiz.orange.web.ap.vo.ApOngdRecvDeptLVo;
import com.innobiz.orange.web.ap.vo.ApRecvGrpBVo;
import com.innobiz.orange.web.ap.vo.ApRecvGrpDVo;
import com.innobiz.orange.web.ap.vo.ApRescBVo;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.vo.PtxSortOrdrChnVo;

/** 수신경로 */
@Controller
public class ApRecvGrpCtrl {
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 결재 공통 서비스 */
	@Autowired
	private ApCmSvc apCmSvc;

	/** 결재 리소스 처리 서비스 */
	@Autowired
	private ApRescSvc apRescSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	
	/** 수신그룹 목록 조회 - 수신처지정 > [탭4]수신그룹 > 좌측 프레임 */
	@RequestMapping(value = "/ap/box/listRecvGrpFrm")
	public String listRecvGrpFrm(HttpServletRequest request,
			@Parameter(name="mode", required=false) String mode,
			@Parameter(name="recvGrpId", required=false) String recvGrpId,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		boolean isPub = "pub".equals(mode);
		
		// 상단 - 수신그룹 선택용
		// 수신그룹기본(AP_RECV_GRP_B) 테이블
		ApRecvGrpBVo apRecvGrpBVo = new ApRecvGrpBVo();
		apRecvGrpBVo.setUserUid(!isPub ? userVo.getUserUid() : userVo.getCompId());
		apRecvGrpBVo.setQueryLang(langTypCd);
		apRecvGrpBVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<ApRecvGrpBVo> apRecvGrpBVoList = (List<ApRecvGrpBVo>)commonSvc.queryList(apRecvGrpBVo);
		if(apRecvGrpBVoList!=null && !apRecvGrpBVoList.isEmpty()){
			model.put("apRecvGrpBVoList", apRecvGrpBVoList);
			if(recvGrpId == null || recvGrpId.isEmpty()){
				recvGrpId = apRecvGrpBVoList.get(0).getRecvGrpId();
			}
		}
		
		// 하단 - 수신그룹 별 수신처 목록
		if(recvGrpId != null && !recvGrpId.isEmpty()){
			
			ApRecvGrpDVo apRecvGrpDVo = new ApRecvGrpDVo();
			apRecvGrpDVo.setUserUid(!isPub ? userVo.getUserUid() : userVo.getCompId());
			apRecvGrpDVo.setRecvGrpId(recvGrpId);
			apRecvGrpDVo.setQueryLang(langTypCd);
			@SuppressWarnings("unchecked")
			List<ApRecvGrpDVo> apRecvGrpDVoList = (List<ApRecvGrpDVo>)commonSvc.queryList(apRecvGrpDVo);
			
			if(apRecvGrpDVoList!=null){
				
				List<ApOngdRecvDeptLVo> apOngdRecvDeptLVoList = new ArrayList<ApOngdRecvDeptLVo>();
				OrOrgBVo orOrgBVo;
				ApOngdRecvDeptLVo apOngdRecvDeptLVo;
				String deptId;
				
				Map<Integer, OrOrgBVo> orgCacheMap = new HashMap<Integer, OrOrgBVo>();
				
				for(ApRecvGrpDVo storedApRecvGrpDVo : apRecvGrpDVoList){
					
					apOngdRecvDeptLVo = new ApOngdRecvDeptLVo();
					apOngdRecvDeptLVo.fromMap(storedApRecvGrpDVo.toMap());
					
					// 수신처구분코드 - dom:대내, for:대외, outOrg:외부기관
					//recvDeptTypCd = storedApRecvGrpDVo.getRecvDeptTypCd();
					
					deptId = apOngdRecvDeptLVo.getRecvDeptId();
					if(deptId != null && !deptId.isEmpty()){
						orOrgBVo = apRescSvc.getOrOrgBVo(deptId, langTypCd, orgCacheMap);
						if(orOrgBVo==null) continue;
						apOngdRecvDeptLVo.setRecvDeptNm(orOrgBVo.getRescNm());
					}
					deptId = apOngdRecvDeptLVo.getRefDeptId();
					if(deptId != null && !deptId.isEmpty()){
						orOrgBVo = apRescSvc.getOrOrgBVo(deptId, langTypCd, orgCacheMap);
						if(orOrgBVo==null) continue;
						apOngdRecvDeptLVo.setRefDeptNm(orOrgBVo.getRescNm());
					}
					
					apOngdRecvDeptLVoList.add(apOngdRecvDeptLVo);
					
				}
				model.put("apOngdRecvDeptLVoList", apOngdRecvDeptLVoList);
			}
			
		}
		return LayoutUtil.getJspPath("/ap/box/listRecvGrpFrm");
	}
	
	/** 수신그룹 수정/등록 - 수신처지정 > [탭4]수신그룹 > 좌측 프레임 */
	@RequestMapping(value = "/ap/box/setRecvGrpPop")
	public String setRecvGrpPop(HttpServletRequest request,
			@Parameter(name="mode", required=false) String mode,
			@Parameter(name="recvGrpId", required=false) String recvGrpId,
			ModelMap model) throws Exception {
		
		if(recvGrpId!=null && !recvGrpId.isEmpty()){
			UserVo userVo = LoginSession.getUser(request);
			String langTypCd = LoginSession.getLangTypCd(request);
			boolean isPub = "pub".equals(mode);
			
			ApRecvGrpBVo apRecvGrpBVo = new ApRecvGrpBVo();
			apRecvGrpBVo.setRecvGrpId(recvGrpId);
			apRecvGrpBVo.setUserUid(!isPub ? userVo.getUserUid() : userVo.getCompId());
			apRecvGrpBVo.setQueryLang(langTypCd);
			
			apRecvGrpBVo = (ApRecvGrpBVo)commonSvc.queryVo(apRecvGrpBVo);
			if(apRecvGrpBVo!=null){
				model.put("apRecvGrpBVo", apRecvGrpBVo);
				
				// model에 리소스 세팅
				if(isPub && apRecvGrpBVo.getRescId()!=null && !apRecvGrpBVo.getRescId().isEmpty()){
					ApRescBVo apRescBVo = new ApRescBVo();
					apRescBVo.setRescId(apRecvGrpBVo.getRescId());
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
		return LayoutUtil.getJspPath("/ap/box/setRecvGrpPop");
	}
	
	/** 수신그룹 저장 - 수신처지정 > [탭4]수신그룹 > 좌측 프레임 */
	@RequestMapping(value = "/ap/box/transRecvGrp")
	public String transRecvGrp(HttpServletRequest request,
			@Parameter(name="menuId", required=false) String menuId,
			@Parameter(name="bxId", required=false) String bxId,
			@Parameter(name="formId", required=false) String formId,
			@Parameter(name="mode", required=false) String mode,
			@Parameter(name="recvGrpId", required=false) String recvGrpId,
			@Parameter(name="recvGrpNm", required=false) String recvGrpNm,
			ModelMap model, Locale locale) throws Exception {
		
		try{
			
			QueryQueue queryQueue = new QueryQueue();
			
			UserVo userVo = LoginSession.getUser(request);
			boolean isPub = "pub".equals(mode);
			boolean isNewGrp = false;
			if(recvGrpId==null || recvGrpId.isEmpty()){
				isNewGrp = true;
				recvGrpId = apCmSvc.createId("AP_RECV_GRP_B");
			}
			
			ApRecvGrpBVo apRecvGrpBVo = new ApRecvGrpBVo();
			apRecvGrpBVo.setRecvGrpId(recvGrpId);
			if(!isPub){
				apRecvGrpBVo.setUserUid(userVo.getUserUid());
				apRecvGrpBVo.setRecvGrpNm(recvGrpNm);
			} else {
				apRecvGrpBVo.setUserUid(userVo.getCompId());
				ApRescBVo apRescBVo = apRescSvc.collectApRescBVo(request, null, queryQueue);
				if(apRescBVo!=null){
					apRecvGrpBVo.setRescId(apRescBVo.getRescId());
					apRecvGrpBVo.setRecvGrpNm(apRescBVo.getRescNm());
				}
			}
			
			apRecvGrpBVo.setModrUid(userVo.getUserUid());
			apRecvGrpBVo.setModDt("sysdate");
			
			if(isNewGrp){
				queryQueue.insert(apRecvGrpBVo);
			} else {
				queryQueue.store(apRecvGrpBVo);
			}
			
			commonSvc.execute(queryQueue);
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", locale));
			model.put("todo", "parent.dialog.close('setRecvGrpDialog');"
					+" parent.getIframeContent('docRecvGrpFrm').location.href = "
					+"'./listRecvGrpFrm.do?menuId="+menuId+"&bxId="+bxId
					+(formId==null || formId.isEmpty() ? "" : "&formId="+formId)
					+(isPub ? "&mode=pub" : "&mode=prv")
					+"&recvGrpId="+recvGrpId+"';");
			
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		return LayoutUtil.getResultJsp();
	}
	
	/** [AJX] 수신그룹 삭제 - 수신처지정 > [탭4]수신그룹 > 좌측 프레임 */
	@RequestMapping(value = "/ap/box/transRecvGrpDelAjx")
	public String transRecvGrpDelAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception{
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		
		String mode = (String)jsonObject.get("mode");
		String recvGrpId = (String)jsonObject.get("recvGrpId");
		
		UserVo userVo = LoginSession.getUser(request);
		boolean isPub = "pub".equals(mode);
		
		QueryQueue queryQueue = new QueryQueue();
		
		try {

			// 결재라인그룹기본(AP_APV_LN_GRP_B) 테이블 - 삭제
			ApRecvGrpBVo apRecvGrpBVo = new ApRecvGrpBVo();
			apRecvGrpBVo.setRecvGrpId(recvGrpId);
			apRecvGrpBVo.setUserUid(!isPub ? userVo.getUserUid() : userVo.getCompId());
			queryQueue.delete(apRecvGrpBVo);
			
			// 결재라인그룹상세(AP_APV_LN_GRP_D) 테이블 - 삭제
			ApRecvGrpDVo apRecvGrpDVo = new ApRecvGrpDVo();
			apRecvGrpDVo.setRecvGrpId(recvGrpId);
			apRecvGrpDVo.setUserUid(!isPub ? userVo.getUserUid() : userVo.getCompId());
			queryQueue.delete(apRecvGrpDVo);
			
			// 리소스 지우기 위한 테이블 조회
			apRecvGrpBVo = new ApRecvGrpBVo();
			apRecvGrpBVo.setRecvGrpId(recvGrpId);
			if(!isPub){
				apRecvGrpBVo.setUserUid(userVo.getUserUid());
			} else {
				apRecvGrpBVo.setUserUid(userVo.getCompId());
			}
			apRecvGrpBVo = (ApRecvGrpBVo)commonSvc.queryVo(apRecvGrpBVo);
			
			if(apRecvGrpBVo != null && apRecvGrpBVo.getRescId() != null && !apRecvGrpBVo.getRescId().isEmpty()){
				// 리소스기본(AP_RESC_B) 테이블 - 삭제
				ApRescBVo apRescBVo = new ApRescBVo();
				apRescBVo.setRescId(apRecvGrpBVo.getRescId());
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
	
	/** [프레임] 수신그룹 상세 저장 */
	@RequestMapping(value = "/ap/box/transRecvGrpDetl")
	public String transRecvGrpDetl(HttpServletRequest request,
			@Parameter(name="menuId", required=false) String menuId,
			@Parameter(name="bxId", required=false) String bxId,
			@Parameter(name="formId", required=false) String formId,
			@Parameter(name="recvGrpId", required=false) String recvGrpId,
			@Parameter(name="mode", required=false) String mode,
			ModelMap model, Locale locale) throws Exception{
		
		String[] recvDepts = request.getParameterValues("recvDept");
		
		UserVo userVo = LoginSession.getUser(request);
		boolean isPub = "pub".equals(mode);
		
		QueryQueue queryQueue = new QueryQueue();
		
		// 결재라인그룹상세(AP_APV_LN_GRP_D) 테이블 - 삭제
		ApRecvGrpDVo apRecvGrpDVo = new ApRecvGrpDVo();
		apRecvGrpDVo.setRecvGrpId(recvGrpId);
		apRecvGrpDVo.setUserUid(!isPub ? userVo.getUserUid() : userVo.getCompId());
		queryQueue.delete(apRecvGrpDVo);
		
		Integer seq = 1;
		JSONObject jsonObject;
		if(recvDepts!=null){
			for(String recvDept : recvDepts){
				jsonObject = (JSONObject)JSONValue.parse(recvDept);
				
				apRecvGrpDVo = new ApRecvGrpDVo();
				apRecvGrpDVo.setUserUid(!isPub ? userVo.getUserUid() : userVo.getCompId());
				apRecvGrpDVo.setRecvGrpId(recvGrpId);
				apRecvGrpDVo.setRecvGrpSeq(seq.toString());
				seq++;
				apRecvGrpDVo.setRecvDeptTypCd((String)jsonObject.get("recvDeptTypCd"));
				apRecvGrpDVo.setRecvDeptId((String)jsonObject.get("recvDeptId"));
				apRecvGrpDVo.setRecvDeptNm((String)jsonObject.get("recvDeptNm"));
				apRecvGrpDVo.setRefDeptId((String)jsonObject.get("refDeptId"));
				apRecvGrpDVo.setRefDeptNm((String)jsonObject.get("refDeptNm"));
				
				queryQueue.insert(apRecvGrpDVo);
			}
		}
		
		try{
			
			commonSvc.execute(queryQueue);
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", locale));
			model.put("todo", "parent.location.replace('./listRecvGrpFrm.do?menuId="+menuId
					+"&bxId="+bxId
					+(formId==null || formId.isEmpty() ? "" : "&formId="+formId)
					+"&recvGrpId="+recvGrpId
					+(isPub ? "&mode=pub" : "&mode=prv")
					+"');");
			
		} catch(Exception e){
			String message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
			model.put("message", message);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
//	/** 수신그룹 별 수신처 목록 조회 - 수신처지정 > [탭4]수신그룹 > 우측 프레임 */
//	@RequestMapping(value = "/ap/box/listRecvGrpDetlFrm")
//	public String listRecvGrpDetlFrm(HttpServletRequest request,
//			@Parameter(name="mode", required=false) String mode,
//			@Parameter(name="recvGrpId", required=false) String recvGrpId,
//			ModelMap model) throws Exception {
//		
//		UserVo userVo = LoginSession.getUser(request);
//		String langTypCd = LoginSession.getLangTypCd(request);
//		boolean isPub = "pub".equals(mode);
//		
//		ApRecvGrpDVo apRecvGrpDVo = new ApRecvGrpDVo();
//		apRecvGrpDVo.setUserUid(!isPub ? userVo.getUserUid() : userVo.getCompId());
//		apRecvGrpDVo.setRecvGrpId(recvGrpId);
//		apRecvGrpDVo.setQueryLang(langTypCd);
//		@SuppressWarnings("unchecked")
//		List<ApRecvGrpDVo> apRecvGrpDVoList = (List<ApRecvGrpDVo>)commonSvc.queryList(apRecvGrpDVo);
//		if(apRecvGrpDVoList!=null){
//			
//			List<ApOngdRecvDVo> apOngdRecvDVoList = new ArrayList<ApOngdRecvDVo>();
//			OrUserBVo orUserBVo;
//			OrOrgBVo orOrgBVo;
//			ApOngdRecvDVo apOngdRecvDVo;
//			String rescVa, apvDeptId;
//			
//			Map<Integer, OrUserBVo> userCacheMap = new HashMap<Integer, OrUserBVo>();
//			Map<Integer, OrOrgBVo> orgCacheMap = new HashMap<Integer, OrOrgBVo>();
//			
//			for(ApRecvGrpDVo storedApRecvGrpDVo : apRecvGrpDVoList){
//				
//				apOngdRecvDVo = new ApOngdRecvDVo();
//				apOngdRecvDVo.fromMap(storedApRecvGrpDVo.toMap());
//				
//				// 사용자면
//				if(!"Y".equals(storedApRecvGrpDVo.getApvrDeptYn())){
//					
//					orUserBVo = apRescSvc.getOrUserBVo(storedApRecvGrpDVo.getApvrUid(), langTypCd, userCacheMap);
//					if(orUserBVo != null){
//						apOngdRecvDVo.setApvrNm(orUserBVo.getUserNm());
//						apOngdRecvDVo.setApvrPositCd(orUserBVo.getPositCd());
//						apOngdRecvDVo.setApvrPositNm(orUserBVo.getPositNm());
//						apOngdRecvDVo.setApvrTitleCd(orUserBVo.getTitleCd());
//						apOngdRecvDVo.setApvrTitleNm(orUserBVo.getTitleNm());
//						
//						// 사용중 상태가 아닌 사용자 - 선택 안되도록 처리하기 위함 - 결재했음으로 코드 변경
//						if(!"02".equals(orUserBVo.getUserStatCd())){
//							apOngdRecvDVo.setApvStatCd("apvd");
//						}
//						
//						apvDeptId = orUserBVo.getDeptId();
//					} else {
//						continue;
//					}
//				} else {
//					apvDeptId = storedApRecvGrpDVo.getApvDeptId();
//				}
//				orOrgBVo = apRescSvc.getOrOrgBVo(apvDeptId, langTypCd, orgCacheMap);
//				if(orOrgBVo != null){
//					
//					apOngdRecvDVo.setApvDeptNm(orOrgBVo.getRescNm());
//					rescVa = orOrgBVo.getOrgAbbrRescNm();
//					if(rescVa==null || rescVa.isEmpty()){
//						apOngdRecvDVo.setApvDeptAbbrNm(orOrgBVo.getRescNm());
//					} else {
//						apOngdRecvDVo.setApvDeptAbbrNm(rescVa);
//					}
//					
//					if(!"Y".equals(orOrgBVo.getUseYn())){
//						apOngdRecvDVo.setApvStatCd("apvd");
//					}
//				} else {
//					continue;
//				}
//				
//				apOngdRecvDVoList.add(apOngdRecvDVo);
//			}
//			model.put("apOngdRecvDVoList", apOngdRecvDVoList);
//		}
//		
//		return LayoutUtil.getJspPath("/ap/box/listRecvGrpDetlFrm");
//	}
	
	/** [AJAX] 수신그룹 순서변경 */
	@RequestMapping(value = "/ap/box/transRecvGrpMoveAjx")
	public String transRecvGrpMove(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String mode = (String)jsonObject.get("mode");
		String recvGrpId = (String)jsonObject.get("recvGrpId");
		String direction = (String)jsonObject.get("direction");
		boolean isPub = "pub".equals(mode);
		
		UserVo userVo = LoginSession.getUser(request);
		
		QueryQueue queryQueue = new QueryQueue();
		PtxSortOrdrChnVo ptxSortOrdrChnVo;
		
		if(recvGrpId==null || direction==null){
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			model.put("message", msg);
			return JsonUtil.returnJson(model);
		} else {
			
			ApRecvGrpBVo apRecvGrpBVo, storedApRecvGrpBVo;
			
			// 맨위로
			if("first".equals(direction)){
				
				// curOrdr - 현재순번
				// stdOrdr - 기준순번 - 현재순번과 기준순번이 같으면 옮길수 없는 것으로 패스
				Integer curOrdr;
				
				storedApRecvGrpBVo = new ApRecvGrpBVo();
				storedApRecvGrpBVo.setUserUid(!isPub ? userVo.getUserUid() : userVo.getCompId());
				storedApRecvGrpBVo.setRecvGrpId(recvGrpId);
				storedApRecvGrpBVo = (ApRecvGrpBVo)commonSvc.queryVo(storedApRecvGrpBVo);
				curOrdr = Integer.valueOf(storedApRecvGrpBVo.getSortOrdr()) - 1;
				
				ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
				ptxSortOrdrChnVo.setTabId("AP_RECV_GRP_B");
				ptxSortOrdrChnVo.setPkCol("USER_UID");
				ptxSortOrdrChnVo.setPk(!isPub ? userVo.getUserUid() : userVo.getCompId());
				ptxSortOrdrChnVo.setMoreThen(Integer.valueOf(1));
				ptxSortOrdrChnVo.setLessThen(curOrdr);
				ptxSortOrdrChnVo.setChnVa(1);
				ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
				queryQueue.update(ptxSortOrdrChnVo);
				
				apRecvGrpBVo = new ApRecvGrpBVo();
				apRecvGrpBVo.setUserUid(!isPub ? userVo.getUserUid() : userVo.getCompId());
				apRecvGrpBVo.setRecvGrpId(recvGrpId);
				apRecvGrpBVo.setSortOrdr("1");
				queryQueue.update(apRecvGrpBVo);
				
				commonSvc.execute(queryQueue);
				model.put("result", "ok");
				
			// 위로 이동
			} else if("up".equals(direction)){
				
				// curOrdr - 현재순번
				// stdOrdr - 기준순번 - 현재순번과 기준순번이 같으면 옮길수 없는 것으로 패스
				Integer curOrdr, switchOrdr;
				
				storedApRecvGrpBVo = new ApRecvGrpBVo();
				storedApRecvGrpBVo.setUserUid(!isPub ? userVo.getUserUid() : userVo.getCompId());
				storedApRecvGrpBVo.setRecvGrpId(recvGrpId);
				storedApRecvGrpBVo = (ApRecvGrpBVo)commonSvc.queryVo(storedApRecvGrpBVo);
				curOrdr = Integer.valueOf(storedApRecvGrpBVo.getSortOrdr());
				switchOrdr = curOrdr-1;
				
				if(curOrdr.intValue() > 1){
					
					ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
					ptxSortOrdrChnVo.setTabId("AP_RECV_GRP_B");
					ptxSortOrdrChnVo.setPkCol("USER_UID");
					ptxSortOrdrChnVo.setPk(!isPub ? userVo.getUserUid() : userVo.getCompId());
					ptxSortOrdrChnVo.setMoreThen(switchOrdr);
					ptxSortOrdrChnVo.setLessThen(switchOrdr);
					ptxSortOrdrChnVo.setChnVa(1);
					ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
					queryQueue.update(ptxSortOrdrChnVo);
					
					apRecvGrpBVo = new ApRecvGrpBVo();
					apRecvGrpBVo.setUserUid(!isPub ? userVo.getUserUid() : userVo.getCompId());
					apRecvGrpBVo.setRecvGrpId(recvGrpId);
					apRecvGrpBVo.setSortOrdr(switchOrdr.toString());
					queryQueue.update(apRecvGrpBVo);
					
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
				ptxSortOrdrChnVo.setTabId("AP_RECV_GRP_B");
				ptxSortOrdrChnVo.setPkCol("USER_UID");
				ptxSortOrdrChnVo.setPk(!isPub ? userVo.getUserUid() : userVo.getCompId());
				ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.selectMaxSortOrdr");
				maxSortOrdr = commonSvc.queryInt(ptxSortOrdrChnVo);
				
				storedApRecvGrpBVo = new ApRecvGrpBVo();
				storedApRecvGrpBVo.setUserUid(!isPub ? userVo.getUserUid() : userVo.getCompId());
				storedApRecvGrpBVo.setRecvGrpId(recvGrpId);
				storedApRecvGrpBVo = (ApRecvGrpBVo)commonSvc.queryVo(storedApRecvGrpBVo);
				curOrdr = Integer.valueOf(storedApRecvGrpBVo.getSortOrdr());
				
				if(maxSortOrdr.intValue() > curOrdr.intValue()){
					
					switchOrdr = curOrdr+1;
					
					ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
					ptxSortOrdrChnVo.setTabId("AP_RECV_GRP_B");
					ptxSortOrdrChnVo.setPkCol("USER_UID");
					ptxSortOrdrChnVo.setPk(!isPub ? userVo.getUserUid() : userVo.getCompId());
					ptxSortOrdrChnVo.setMoreThen(switchOrdr);
					ptxSortOrdrChnVo.setLessThen(switchOrdr);
					ptxSortOrdrChnVo.setChnVa(-1);
					ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
					queryQueue.update(ptxSortOrdrChnVo);
					
					apRecvGrpBVo = new ApRecvGrpBVo();
					apRecvGrpBVo.setUserUid(!isPub ? userVo.getUserUid() : userVo.getCompId());
					apRecvGrpBVo.setRecvGrpId(recvGrpId);
					apRecvGrpBVo.setSortOrdr(switchOrdr.toString());
					queryQueue.update(apRecvGrpBVo);
					
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
