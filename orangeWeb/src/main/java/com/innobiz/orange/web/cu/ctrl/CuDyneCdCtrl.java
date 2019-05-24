package com.innobiz.orange.web.cu.ctrl;

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
import com.innobiz.orange.web.cu.svc.CuDyneCdSvc;
import com.innobiz.orange.web.cu.vo.WeCdDVo;
import com.innobiz.orange.web.cu.vo.WeCdGrpBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.wf.svc.WfCmSvc;
import com.innobiz.orange.web.wf.svc.WfRescSvc;
import com.innobiz.orange.web.wf.vo.WfRescBVo;

/** 코드관리 */
@Controller
public class CuDyneCdCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(CuDyneCdCtrl.class);

	/** 공통 서비스 */
	@Autowired
	private WfCmSvc wfCmSvc;
	
	/** 코드 서비스 */
	@Resource(name = "cuDyneCdSvc")
	private CuDyneCdSvc cuDyneCdSvc;
	
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
	@RequestMapping(value = "/cu/dyne/csmt/cd/listCdGrp")
	public String listCdGrp(HttpServletRequest request,
			ModelMap model) throws Exception {

		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		// 코드그룹(WF_CD_GRP_B) 테이블 - BIND
		WeCdGrpBVo weCdGrpBVo = new WeCdGrpBVo();
		VoUtil.bind(request, weCdGrpBVo);
		weCdGrpBVo.setQueryLang(langTypCd);
		weCdGrpBVo.setCompId(userVo.getCompId());
				
		// 코드그룹(WF_CD_GRP_B) 테이블 - COUNT
		Integer recodeCount = commonSvc.count(weCdGrpBVo);
		PersonalUtil.setPaging(request, weCdGrpBVo, recodeCount);

		// 코드그룹(WF_CD_GRP_B) 테이블 - SELECT
		List<WeCdGrpBVo> weCdGrpBVoList = cuDyneCdSvc.getWeCdGrpBVoList(weCdGrpBVo, null, null, null, true);
		
		model.put("recodeCount", recodeCount);
		model.put("weCdGrpBVoList", weCdGrpBVoList);

		return LayoutUtil.getJspPath("/cu/dyne/csmt/cd/listCdGrp");
	}
	
	/** [팝업] - 코드그룹 등록 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cu/dyne/csmt/cd/setCdGrpPop")
	public String setCdGrpPop(HttpServletRequest request,
			@Parameter(name="cdGrpId", required=false) String cdGrpId,
			ModelMap model) throws Exception {
		
		List<WeCdDVo> weCdDVoList=null;
		if(cdGrpId!=null && !cdGrpId.isEmpty()){
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);			
			
			WeCdGrpBVo weCdGrpBVo = new WeCdGrpBVo();
			weCdGrpBVo.setQueryLang(langTypCd);
			weCdGrpBVo.setCompId(userVo.getCompId());
			weCdGrpBVo.setCdGrpId(cdGrpId);
			weCdGrpBVo=(WeCdGrpBVo)commonSvc.queryVo(weCdGrpBVo);
			if(weCdGrpBVo==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			WeCdDVo weCdDVo = new WeCdDVo();
			weCdDVo.setCdGrpId(cdGrpId);
			weCdDVoList = (List<WeCdDVo>)commonSvc.queryList(weCdDVo);
			if(weCdDVoList!=null){
				for (WeCdDVo storedWeCdDVo : weCdDVoList) {
					if (storedWeCdDVo.getRescId() != null) {
						// 리소스기본(WH_RESC_B) 테이블 - 조회
						wfRescSvc.queryRescBVo(storedWeCdDVo.getRescId(), model);
					}
				}
			}
			if (weCdGrpBVo.getRescId() != null) {
				// 리소스기본(WH_RESC_B) 테이블 - 조회
				wfRescSvc.queryRescBVo(weCdGrpBVo.getRescId(), model);
			}
			model.put("weCdGrpBVo", weCdGrpBVo);
		}
		
		if(weCdDVoList==null)
			weCdDVoList=new ArrayList<WeCdDVo>();
		
		// 화면 구성용 2개의 빈 vo 넣음
		weCdDVoList.add(new WeCdDVo());
		weCdDVoList.add(new WeCdDVo());
		
		model.put("weCdDVoList", weCdDVoList);
		
		return LayoutUtil.getJspPath("/cu/dyne/csmt/cd/setCdGrpPop");
	}
	
	
	/** [히든프레임] 코드 일괄 저장 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cu/dyne/csmt/cd/transCdGrpList")
	public String transCdGrpList(HttpServletRequest request,			
			@Parameter(name="cdGrpId", required=false) String cdGrpId,
			@Parameter(name="delList", required=false) String delList,
			@Parameter(name="dialog", required=false) String dialog,
			ModelMap model) throws Exception {
		
		try{
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 코드기본(ST_CAT_B) 테이블 Vo
			WeCdDVo weCdDVo;
			
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
		        if(cuDyneCdSvc.isCdUseList(null, null, null, cdIdList)){
		        	// wf.msg.cd.not.delete=사용중인 코드가 있어서 삭제할 수 없습니다.  
					throw new CmException("wf.msg.cd.not.delete", request);
		        }
			}*/
			
			// 코드 사용여부
			boolean isUseCd=false;
						
			for(i=0;i<delCds.length;i++){
				cdId = delCds[i];
				
				// 기본 테이블 - 삭제
				weCdDVo = new WeCdDVo();
				weCdDVo.setCdId(cdId);
				
				// 사용중인 코드가 있을경우 코드 사용여부를 변경하고 메시지 처리
				if(cuDyneCdSvc.isCdUseList(null, null, cdId, null)){
					if(!isUseCd) isUseCd=true;
					weCdDVo.setUseYn("N");
					weCdDVo.setSortOrdr(999);
					queryQueue.update(weCdDVo);
		        }else{
		        	queryQueue.delete(weCdDVo);
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
			
			WeCdGrpBVo weCdGrpBVo = new WeCdGrpBVo();
			VoUtil.bind(request, weCdGrpBVo);
			weCdGrpBVo.setCompId(userVo.getCompId());
			weCdGrpBVo.setRescId(wfRescBVo.getRescId());
			weCdGrpBVo.setCdGrpNm(wfRescBVo.getRescVa());
			
			if(cdGrpId!=null && !cdGrpId.isEmpty()){
				weCdGrpBVo.setModrUid(userVo.getUserUid());
				weCdGrpBVo.setModDt("sysdate");
				queryQueue.update(weCdGrpBVo);
			}else{
				cdGrpId=wfCmSvc.createId("WF_CD_GRP_B").toString();
				weCdGrpBVo.setCdGrpId(cdGrpId);
				weCdGrpBVo.setRegrUid(userVo.getUserUid());
				weCdGrpBVo.setRegDt("sysdate");
				queryQueue.insert(weCdGrpBVo);
			}
			// 코드기본(ST_CAT_B) 테이블
			List<WeCdDVo> weCdDVoList = (List<WeCdDVo>)VoUtil.bindList(request, WeCdDVo.class, new String[]{"valid"});
			size = weCdDVoList==null ? 0 : weCdDVoList.size();
			
			if(size>0){
				// 리소스 정보 queryQueue에 담음
				wfRescSvc.collectWfRescBVoList(request, queryQueue, weCdDVoList, "valid", "rescId", "cdNm");
			}
			for(i=0;i<size;i++){
				weCdDVo = weCdDVoList.get(i);
				weCdDVo.setCdGrpId(cdGrpId);
				if(weCdDVo.getCdId() == null || weCdDVo.getCdId().isEmpty())
					weCdDVo.setCdId(wfCmSvc.createId("WF_CD_D").toString());
				
				queryQueue.store(weCdDVo);
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
	@RequestMapping(value = "/cu/dyne/csmt/cd/transCdGrpDelAjx")
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
					if(cuDyneCdSvc.isCdUseList(null, cdGrpIdList, null, null)){
			        	// wf.msg.cd.not.delete=사용중인 코드가 있어서 삭제할 수 없습니다.  
						throw new CmException("wf.msg.cd.not.delete", request);
			        }
				}*/
				WeCdGrpBVo weCdGrpBVo = null;
				WeCdDVo weCdDVo = null;
				
				for(int i=0;i<jsonArray.size();i++){
					cdGrpId = (String)jsonArray.get(i);
					
					// 코드그룹 - 삭제
					weCdGrpBVo = new WeCdGrpBVo();
					weCdGrpBVo.setCompId(userVo.getCompId());
					weCdGrpBVo.setCdGrpId(cdGrpId);
					
					// 사용중인 코드가 있을경우 코드그룹 사용여부를 변경하고 메시지 처리
					if(cuDyneCdSvc.isCdUseList(cdGrpId, null, null, null)){
						if(!isUseCd) isUseCd=true;
						weCdGrpBVo.setCdUseYn("N");
						queryQueue.update(weCdGrpBVo);
			        }else{
			        	// 코드 목록 - 삭제
						weCdDVo = new WeCdDVo();
						weCdDVo.setCdGrpId(cdGrpId);
						queryQueue.delete(weCdDVo);
						
						queryQueue.delete(weCdGrpBVo);
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
	@RequestMapping(value = "/cu/dyne/csmt/form/getCdGrpListAjx")
	public String getCdGrpListAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception{
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 코드그룹(WF_CD_GRP_B) 테이블 - SELECT
		/*List<WeCdGrpBVo> weCdGrpBVoList = cuDyneCdSvc.getWeCdGrpBVoList(null, langTypCd, userVo.getCompId(), true);
		model.put("weCdGrpBVoList", weCdGrpBVoList);*/
		
		String cdGrpJsonString = cuDyneCdSvc.getCdGrpListToJson(null, langTypCd, userVo.getCompId(), "Y", true);
		model.put("cdGrpJsonString", cdGrpJsonString);
		
				
		return LayoutUtil.returnJson(model);
	}
	
	
}
