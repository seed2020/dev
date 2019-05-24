package com.innobiz.orange.web.wf.ctrl;

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
import com.innobiz.orange.web.wf.svc.WfCdSvc;
import com.innobiz.orange.web.wf.svc.WfCmSvc;
import com.innobiz.orange.web.wf.svc.WfRescSvc;
import com.innobiz.orange.web.wf.vo.WfCdDVo;
import com.innobiz.orange.web.wf.vo.WfCdGrpBVo;
import com.innobiz.orange.web.wf.vo.WfRescBVo;

@Controller
public class WfCdCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WfCdCtrl.class);
	
	/** 공통 서비스 */
	@Autowired
	private WfCmSvc wfCmSvc;
	
	/** 코드 서비스 */
	@Resource(name = "wfCdSvc")
	private WfCdSvc wfCdSvc;
	
	/** 리소스 조회 저장용 서비스 */
	@Resource(name = "wfRescSvc")
	private WfRescSvc wfRescSvc;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 코드관리 목록조회 */
	@RequestMapping(value = "/wf/adm/cd/listCdGrp")
	public String listCdGrp(HttpServletRequest request,
			ModelMap model) throws Exception {

		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		// 코드그룹(WF_CD_GRP_B) 테이블 - BIND
		WfCdGrpBVo wfCdGrpBVo = new WfCdGrpBVo();
		VoUtil.bind(request, wfCdGrpBVo);
		wfCdGrpBVo.setQueryLang(langTypCd);
		wfCdGrpBVo.setCompId(userVo.getCompId());
				
		// 코드그룹(WF_CD_GRP_B) 테이블 - COUNT
		Integer recodeCount = commonSvc.count(wfCdGrpBVo);
		PersonalUtil.setPaging(request, wfCdGrpBVo, recodeCount);

		// 코드그룹(WF_CD_GRP_B) 테이블 - SELECT
		List<WfCdGrpBVo> wfCdGrpBVoList = wfCdSvc.getWfCdGrpBVoList(wfCdGrpBVo, null, null, null, true);
		
		model.put("recodeCount", recodeCount);
		model.put("wfCdGrpBVoList", wfCdGrpBVoList);

		return LayoutUtil.getJspPath("/wf/adm/cd/listCdGrp");
	}
	
	/** [팝업] - 코드그룹 등록 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/wf/adm/cd/setCdGrpPop")
	public String setCdGrpPop(HttpServletRequest request,
			@Parameter(name="cdGrpId", required=false) String cdGrpId,
			ModelMap model) throws Exception {
		
		List<WfCdDVo> wfCdDVoList=null;
		if(cdGrpId!=null && !cdGrpId.isEmpty()){
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);			
			
			WfCdGrpBVo wfCdGrpBVo = new WfCdGrpBVo();
			wfCdGrpBVo.setQueryLang(langTypCd);
			wfCdGrpBVo.setCompId(userVo.getCompId());
			wfCdGrpBVo.setCdGrpId(cdGrpId);
			wfCdGrpBVo=(WfCdGrpBVo)commonSvc.queryVo(wfCdGrpBVo);
			if(wfCdGrpBVo==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			WfCdDVo wfCdDVo = new WfCdDVo();
			wfCdDVo.setCdGrpId(cdGrpId);
			wfCdDVoList = (List<WfCdDVo>)commonSvc.queryList(wfCdDVo);
			if(wfCdDVoList!=null){
				for (WfCdDVo storedWfCdDVo : wfCdDVoList) {
					if (storedWfCdDVo.getRescId() != null) {
						// 리소스기본(WH_RESC_B) 테이블 - 조회
						wfRescSvc.queryRescBVo(storedWfCdDVo.getRescId(), model);
					}
				}
			}
			if (wfCdGrpBVo.getRescId() != null) {
				// 리소스기본(WH_RESC_B) 테이블 - 조회
				wfRescSvc.queryRescBVo(wfCdGrpBVo.getRescId(), model);
			}
			model.put("wfCdGrpBVo", wfCdGrpBVo);
		}
		
		if(wfCdDVoList==null)
			wfCdDVoList=new ArrayList<WfCdDVo>();
		
		// 화면 구성용 2개의 빈 vo 넣음
		wfCdDVoList.add(new WfCdDVo());
		wfCdDVoList.add(new WfCdDVo());
		
		model.put("wfCdDVoList", wfCdDVoList);
		
		return LayoutUtil.getJspPath("/wf/adm/cd/setCdGrpPop");
	}
	
	
	/** [히든프레임] 코드 일괄 저장 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/wf/adm/cd/transCdGrpList")
	public String transCdGrpList(HttpServletRequest request,			
			@Parameter(name="cdGrpId", required=false) String cdGrpId,
			@Parameter(name="delList", required=false) String delList,
			@Parameter(name="dialog", required=false) String dialog,
			ModelMap model) throws Exception {
		
		try{
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 코드기본(ST_CAT_B) 테이블 VO
			WfCdDVo wfCdDVo;
			
			///////////////////////////////////////////////////////////////////
			//
			//  삭제 목록 처리 : Start
			
			int i, size;
			String cdId;
			String[] delCds = delList==null || delList.isEmpty() ? new String[]{} : delList.split(",");
			
			// 코드가 사용중인지 체크
			/*if(delCds.length>0){
				// String[] -> List
		        List<String> cdIdList = new ArrayList<String>(Arrays.asList(delCds));
		        if(wfCdSvc.isCdUseList(null, null, null, cdIdList)){
		        	// wf.msg.cd.not.delete=사용중인 코드가 있어서 삭제할 수 없습니다.  
					throw new CmException("wf.msg.cd.not.delete", request);
		        }
			}*/
			
			// 코드 사용여부
			boolean isUseCd=false;
						
			for(i=0;i<delCds.length;i++){
				cdId = delCds[i];
				
				// 기본 테이블 - 삭제
				wfCdDVo = new WfCdDVo();
				wfCdDVo.setCdId(cdId);
				
				// 사용중인 코드가 있을경우 코드 사용여부를 변경하고 메시지 처리
				if(wfCdSvc.isCdUseList(null, null, cdId, null)){
					if(!isUseCd) isUseCd=true;
					wfCdDVo.setUseYn("N");
					wfCdDVo.setSortOrdr(999);
					queryQueue.update(wfCdDVo);
		        }else{
		        	queryQueue.delete(wfCdDVo);
		        }				
				
			}
			
			//  삭제 목록 처리 : End
			//
			///////////////////////////////////////////////////////////////////
			
			WfRescBVo wfRescBVo = wfRescSvc.collectWfRescBVo(request, "grp", queryQueue);
			
			if (wfRescBVo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			WfCdGrpBVo wfCdGrpBVo = new WfCdGrpBVo();
			VoUtil.bind(request, wfCdGrpBVo);
			wfCdGrpBVo.setCompId(userVo.getCompId());
			wfCdGrpBVo.setRescId(wfRescBVo.getRescId());
			wfCdGrpBVo.setCdGrpNm(wfRescBVo.getRescVa());
			
			if(cdGrpId!=null && !cdGrpId.isEmpty()){
				wfCdGrpBVo.setModrUid(userVo.getUserUid());
				wfCdGrpBVo.setModDt("sysdate");
				queryQueue.update(wfCdGrpBVo);
			}else{
				cdGrpId=wfCmSvc.createId("WF_CD_GRP_B").toString();
				wfCdGrpBVo.setCdGrpId(cdGrpId);
				wfCdGrpBVo.setRegrUid(userVo.getUserUid());
				wfCdGrpBVo.setRegDt("sysdate");
				queryQueue.insert(wfCdGrpBVo);
			}
			// 코드기본(ST_CAT_B) 테이블
			List<WfCdDVo> wfCdDVoList = (List<WfCdDVo>)VoUtil.bindList(request, WfCdDVo.class, new String[]{"valid"});
			size = wfCdDVoList==null ? 0 : wfCdDVoList.size();
			
			if(size>0){
				// 리소스 정보 queryQueue에 담음
				wfRescSvc.collectWfRescBVoList(request, queryQueue, wfCdDVoList, "valid", "rescId", "cdNm");
			}
			for(i=0;i<size;i++){
				wfCdDVo = wfCdDVoList.get(i);
				wfCdDVo.setCdGrpId(cdGrpId);
				if(wfCdDVo.getCdId() == null || wfCdDVo.getCdId().isEmpty())
					wfCdDVo.setCdId(wfCmSvc.createId("WF_CD_D").toString());
				
				queryQueue.store(wfCdDVo);
			}
			
			if(!queryQueue.isEmpty())
				commonSvc.execute(queryQueue);
			if(isUseCd){
				// wf.msg.cd.use.save=사용중인 코드가 있어서 사용여부를 변경 후 저장하였습니다.
				model.put("alertMessage", messageProperties.getMessage("wf.msg.cd.use.update", request));
			}else{
				// cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			}
			
			if(dialog!=null && !dialog.isEmpty())
				model.put("todo", "parent.pageReload('" + dialog + "');");
		}catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] - 코드그룹 삭제 */
	@RequestMapping(value = "/wf/adm/cd/transCdGrpDelAjx")
	public String transCdGrpDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			JSONArray jsonArray = (JSONArray) object.get("cdGrpIds");
			if (jsonArray == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 게시물 복사
			QueryQueue queryQueue = new QueryQueue();
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			String cdGrpId;
			
			// 코드 사용여부
			boolean isUseCd=false;
			if(jsonArray!=null && !jsonArray.isEmpty()){
				/*// 사용중인 코드 체크
				List<String> cdGrpIdList=new ArrayList<String>();
				
				for(int i=0;i<jsonArray.size();i++){
					cdGrpId = (String)jsonArray.get(i);
					cdGrpIdList.add(cdGrpId);
				}
				if(cdGrpIdList.size()>0){
					if(wfCdSvc.isCdUseList(null, cdGrpIdList, null, null)){
			        	// wf.msg.cd.not.delete=사용중인 코드가 있어서 삭제할 수 없습니다.  
						throw new CmException("wf.msg.cd.not.delete", request);
			        }
				}*/
				WfCdGrpBVo wfCdGrpBVo = null;
				WfCdDVo wfCdDVo = null;
				
				for(int i=0;i<jsonArray.size();i++){
					cdGrpId = (String)jsonArray.get(i);
					
					// 코드그룹 - 삭제
					wfCdGrpBVo = new WfCdGrpBVo();
					wfCdGrpBVo.setCompId(userVo.getCompId());
					wfCdGrpBVo.setCdGrpId(cdGrpId);
					
					// 사용중인 코드가 있을경우 코드그룹 사용여부를 변경하고 메시지 처리
					if(wfCdSvc.isCdUseList(cdGrpId, null, null, null)){
						if(!isUseCd) isUseCd=true;
						wfCdGrpBVo.setCdUseYn("N");
						queryQueue.update(wfCdGrpBVo);
			        }else{
			        	// 코드 목록 - 삭제
						wfCdDVo = new WfCdDVo();
						wfCdDVo.setCdGrpId(cdGrpId);
						queryQueue.delete(wfCdDVo);
						
						queryQueue.delete(wfCdGrpBVo);
			        }
				}
			}
			
			if(!queryQueue.isEmpty())
				commonSvc.execute(queryQueue);
			
			if(isUseCd){
				// wf.msg.cd.use.update=사용중인 코드가 있어서 사용여부를 변경하였습니다.
				model.put("alertMessage", messageProperties.getMessage("wf.msg.cd.use.update", request));
			}else{
				// cm.msg.del.success=삭제 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
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
	
	/** [AJX] 코드그룹 목록조회 */
	@RequestMapping(value = "/wf/adm/form/getCdGrpListAjx")
	public String getCdGrpListAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception{
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 코드그룹(WF_CD_GRP_B) 테이블 - SELECT
		/*List<WfCdGrpBVo> wfCdGrpBVoList = wfCdSvc.getWfCdGrpBVoList(null, langTypCd, userVo.getCompId(), true);
		model.put("wfCdGrpBVoList", wfCdGrpBVoList);*/
		
		String cdGrpJsonString = wfCdSvc.getCdGrpListToJson(null, langTypCd, userVo.getCompId(), "Y", true);
		model.put("cdGrpJsonString", cdGrpJsonString);
		
				
		return LayoutUtil.returnJson(model);
	}
	
	
}
