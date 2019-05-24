package com.innobiz.orange.web.wl.ctrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
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
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.svc.EmCmSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.wl.svc.WlCmSvc;
import com.innobiz.orange.web.wl.svc.WlLogSvc;
import com.innobiz.orange.web.wl.svc.WlRescSvc;
import com.innobiz.orange.web.wl.utils.WlConstant;
import com.innobiz.orange.web.wl.vo.WlReprtGrpBVo;
import com.innobiz.orange.web.wl.vo.WlReprtGrpLVo;
import com.innobiz.orange.web.wl.vo.WlRescBVo;

@Controller
public class WlUserEnvCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WlUserEnvCtrl.class);
    
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Autowired
	private WlCmSvc wlCmSvc;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "emCmSvc")
	private EmCmSvc emCmSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 일지 서비스 */
	@Resource(name = "wlLogSvc")
	private WlLogSvc wlLogSvc;
	
//	/** 포탈 공통 서비스 */
//	@Autowired
//	private PtCmSvc ptCmSvc;
	
	/** 리소스 서비스 */
	@Autowired
	private WlRescSvc wlRescSvc;
	
	/** 환경설정 */
	@RequestMapping(value = "/wl/env/setEnv")
	public String setEnv(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 보고대상 그룹 조회
		WlReprtGrpBVo wlReprtGrpBVo = new WlReprtGrpBVo();
		wlReprtGrpBVo.setCompId(userVo.getCompId());
		wlReprtGrpBVo.setRegrUid(userVo.getUserUid());
		wlReprtGrpBVo.setQueryLang(langTypCd);
		@SuppressWarnings("unchecked")
		List<WlReprtGrpBVo> wlReprtGrpBVoList = (List<WlReprtGrpBVo>)commonSvc.queryList(wlReprtGrpBVo);
		
		// 보고대상 그룹 목록
		model.put("wlReprtGrpBVoList", wlReprtGrpBVoList);
		
		// 탭 목록
		wlLogSvc.setTabList(model, userVo.getCompId(), true);
				
		// 탭 목록맵
		model.put("tabListMap", wlLogSvc.getTabListMap(model, userVo.getCompId()));
		
		// 환경설정 - 첨부파일 사용여부
		Map<String, String> configMap=wlLogSvc.getEnvConfigAttr(model, userVo.getCompId(), null);
				
		// 컬럼 목록
		wlLogSvc.getColumnList(model, configMap, userVo.getCompId(), langTypCd);
				
		// 환경설정 세팅
		wlCmSvc.getUserConfigMap(model, userVo.getUserUid());
				
		return LayoutUtil.getJspPath("/wl/env/setEnv");
	}
	
	/** [히든프레임] 옵션관리 - 저장 */
	@RequestMapping(value = "/wl/env/transEnv")
	public String transEnv(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		try{
			UserVo userVo = LoginSession.getUser(request);
			QueryQueue queryQueue = new QueryQueue();
			
			// 맵으로 설정 정보 저장
			emCmSvc.setUserSetup(userVo.getUserUid(), WlConstant.USER_CONFIG, queryQueue, true, request);

			if(!queryQueue.isEmpty()){
				commonSvc.execute(queryQueue);
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			} else {
				// cm.msg.nodata.toSave=저장할 데이터가 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.nodata.toSave", request));
			}
			model.put("todo", "parent.location.replace(parent.location.href);");
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
			model.put("todo", "parent.location.replace(parent.location.href);");
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	
	/** [팝업] - 보고자 그룹 목록*/
	@RequestMapping(value = {"/wl/env/setReprtGrpPop", "/wl/adm/env/setReprtGrpPop"})
	public String setReprtGrpPop(HttpServletRequest request,
			@RequestParam(value = "grpNo", required = false) String grpNo,
			ModelMap model) throws Exception {
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		List<WlReprtGrpLVo> recvUserList=null;
		List<WlReprtGrpLVo> reqUserList=null;
		
		if(grpNo!=null && !grpNo.isEmpty()){
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 보고대상자 그룹 목록 조회
			WlReprtGrpBVo wlReprtGrpBVo = new WlReprtGrpBVo();
			wlReprtGrpBVo.setQueryLang(langTypCd);
			wlReprtGrpBVo.setCompId(userVo.getCompId());
			wlReprtGrpBVo.setRegrUid(userVo.getUserUid());
			wlReprtGrpBVo.setGrpNo(grpNo);
			
			wlReprtGrpBVo=(WlReprtGrpBVo)commonSvc.queryVo(wlReprtGrpBVo);
			
			if(wlReprtGrpBVo!=null){
				if (wlReprtGrpBVo.getRescId() != null) {
					// 리소스기본(WL_RESC_B) 테이블 - 조회, 모델에 추가
					wlRescSvc.queryRescBVo(wlReprtGrpBVo.getRescId(), model);
				}
				
				// 보고대상자 상세 목록 조회
				WlReprtGrpLVo wlReprtGrpLVo = new WlReprtGrpLVo();
				wlReprtGrpLVo.setGrpNo(wlReprtGrpBVo.getGrpNo());
				wlReprtGrpLVo.setOrderBy(" TGT_TYP_CD ASC");
				
				@SuppressWarnings("unchecked")
				List<WlReprtGrpLVo> wlReprtGrpLVoList = (List<WlReprtGrpLVo>)commonSvc.queryList(wlReprtGrpLVo);
				if(wlReprtGrpLVoList!=null && wlReprtGrpLVoList.size()>0){
					for(WlReprtGrpLVo vo : wlReprtGrpLVoList){
						if("R".equals(vo.getTgtTypCd())){
							if(recvUserList==null)
								recvUserList=new ArrayList<WlReprtGrpLVo>();
							recvUserList.add(vo);
						}
						if("U".equals(vo.getTgtTypCd())){
							if(reqUserList==null)
								reqUserList=new ArrayList<WlReprtGrpLVo>();
							reqUserList.add(vo);
						}
					}
				}
				model.put("wlReprtGrpBVo", wlReprtGrpBVo);
			}
		}
		
		if(recvUserList==null || recvUserList.size()==0){
			recvUserList=new ArrayList<WlReprtGrpLVo>();
		}
		// 화면 구성용 1개의 빈 vo 넣음
		recvUserList.add(new WlReprtGrpLVo());
		model.put("recvUserList", recvUserList);
		
		if(reqUserList==null || reqUserList.size()==0){
			reqUserList=new ArrayList<WlReprtGrpLVo>();
		}
		// 화면 구성용 1개의 빈 vo 넣음
		reqUserList.add(new WlReprtGrpLVo());
		model.put("reqUserList", reqUserList);
		
		// 탭 목록
		wlLogSvc.setTabList(model, userVo.getCompId(), false);
		
		return LayoutUtil.getJspPath("/wl/env/setReprtGrpPop");
	}
	
	/** 보고대상 그룹 저장 */
	@RequestMapping(value = {"/wl/env/transReprtGrp", "/wl/adm/env/transReprtGrp"})
	public String transReprtGrp(HttpServletRequest request,
			@RequestParam(value = "dialog", required = false) String dialog,
			ModelMap model) {

		try {
			QueryQueue queryQueue = new QueryQueue();
			
			WlRescBVo wlRescBVo = wlRescSvc.collectWlRescBVo(request, "", queryQueue);
			
			if (wlRescBVo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 그룹 번호
			String grpNo = ParamUtil.getRequestParam(request, "grpNo", false);
			
			// 사용여부
			String useYn = ParamUtil.getRequestParam(request, "useYn", false);
			
			// 기본여부
			String dftYn = ParamUtil.getRequestParam(request, "dftYn", false);
			
			// 보고그룹 기본(WL_REPRT_GRP_B) 테이블 - BIND
			WlReprtGrpBVo wlReprtGrpBVo = new WlReprtGrpBVo();
						
			if(useYn!=null && "Y".equals(useYn) && dftYn!=null && "Y".equals(dftYn)){
				wlReprtGrpBVo.setCompId(userVo.getCompId());
				wlReprtGrpBVo.setUseYn("Y");
				if(commonSvc.count(wlReprtGrpBVo)>0){
					wlReprtGrpBVo.setDftYn("N");
					queryQueue.update(wlReprtGrpBVo);
				}
			}
			
			wlReprtGrpBVo = new WlReprtGrpBVo();
			VoUtil.bind(request, wlReprtGrpBVo);
			wlReprtGrpBVo.setQueryLang(langTypCd);
			wlReprtGrpBVo.setCompId(userVo.getCompId());
			wlReprtGrpBVo.setRegrUid(userVo.getUserUid());			
			wlReprtGrpBVo.setGrpNm(wlRescBVo.getRescVa());
			
			
			if(grpNo!=null && !grpNo.isEmpty()){
				wlReprtGrpBVo.setModrUid(userVo.getUserUid());
				wlReprtGrpBVo.setModDt("sysdate");
				queryQueue.update(wlReprtGrpBVo);
			}else{
				wlReprtGrpBVo.setRegDt("sysdate");
				wlReprtGrpBVo.setRescId(wlRescBVo.getRescId());
				grpNo=wlCmSvc.createNo("WL_REPRT_GRP_B").toString();
				wlReprtGrpBVo.setGrpNo(grpNo);
				queryQueue.insert(wlReprtGrpBVo);
			}
			
			// 사용자, 구분코드 목록 - BIND 
			@SuppressWarnings("unchecked")
			List<WlReprtGrpLVo> boundVoList = (List<WlReprtGrpLVo>) VoUtil.bindList(request, WlReprtGrpLVo.class, new String[]{"userUid", "reprtTypCd"});
			
			WlReprtGrpLVo wlReprtGrpLVo = new WlReprtGrpLVo();
			wlReprtGrpLVo.setGrpNo(grpNo);
			if(commonSvc.count(wlReprtGrpLVo)>0)
				queryQueue.delete(wlReprtGrpLVo);
				//commonSvc.delete(wlReprtGrpLVo);				
			
			int sortOrdr=1;
			for(WlReprtGrpLVo storedVo : boundVoList){
				storedVo.setGrpNo(grpNo);
				storedVo.setSortOrdr(sortOrdr);
				queryQueue.insert(storedVo);
				sortOrdr++;
			}
			if(!queryQueue.isEmpty())
				commonSvc.execute(queryQueue);

			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			if(dialog!=null && !dialog.isEmpty())
				model.put("todo", "parent.pageReload('" + dialog + "');");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] - 보고그룹 삭제 */
	@RequestMapping(value = {"/wl/env/transReprtGrpDelAjx", "/wl/adm/env/transReprtGrpDelAjx"})
	public String transReprtGrpDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			JSONArray grpNoArray = (JSONArray) object.get("grpNos");
			if (grpNoArray == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 게시물 복사
			QueryQueue queryQueue = new QueryQueue();
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			String grpNo;
			if(grpNoArray!=null && !grpNoArray.isEmpty()){
				WlReprtGrpBVo wlReprtGrpBVo = null;
				WlReprtGrpLVo wlReprtGrpLVo = null;
				for(int i=0;i<grpNoArray.size();i++){
					grpNo = (String)grpNoArray.get(i);
					
					// 보고그룹 목록 - 삭제
					wlReprtGrpLVo = new WlReprtGrpLVo();
					wlReprtGrpLVo.setGrpNo(grpNo);
					queryQueue.delete(wlReprtGrpLVo);
					
					// 보고그룹 기본 - 삭제
					wlReprtGrpBVo = new WlReprtGrpBVo();
					wlReprtGrpBVo.setCompId(userVo.getCompId());
					wlReprtGrpBVo.setRegrUid(userVo.getUserUid());
					wlReprtGrpBVo.setGrpNo(grpNo);
					queryQueue.delete(wlReprtGrpBVo);
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
	
}
