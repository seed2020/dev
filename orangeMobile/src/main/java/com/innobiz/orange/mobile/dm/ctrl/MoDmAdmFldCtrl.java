package com.innobiz.orange.mobile.dm.ctrl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.dm.svc.DmAdmSvc;
import com.innobiz.orange.web.dm.svc.DmDocSvc;
import com.innobiz.orange.web.dm.svc.DmRescSvc;
import com.innobiz.orange.web.dm.svc.DmStorSvc;
import com.innobiz.orange.web.dm.utils.DmConstant;
import com.innobiz.orange.web.dm.vo.DmFldBVo;
import com.innobiz.orange.web.dm.vo.DmStorBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.vo.PtCompBVo;

@Controller
public class MoDmAdmFldCtrl {
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 리소스 조회 저장용 서비스 */
	@Resource(name = "dmRescSvc")
	private DmRescSvc dmRescSvc;
	
	/** 저장소 서비스 */
	@Resource(name = "dmStorSvc")
	private DmStorSvc dmStorSvc;
	
	/** 관리 서비스 */
	@Resource(name = "dmAdmSvc")
	private DmAdmSvc dmAdmSvc;
	
	/** 문서 서비스 */
	@Autowired
	private DmDocSvc dmDocSvc;
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 폴더 트리 조회 */
	@RequestMapping(value = {"/dm/doc/treeDocFldSub","/dm/adm/doc/treeDocFldSub","/dm/fld/treeDocFldSub","/dm/adm/fld/treeDocFldSub","/dm/adm/fld/treeFldSub","/cm/doc/treeDocFldSub"})
	public String treeFldSub(HttpServletRequest request,
			@Parameter(name="psnYn", required=false) String psnYn,
			@Parameter(name="popYn", required=false) String popYn,
			@Parameter(name="fldGrpId", required=false) String fldGrpId,
			@Parameter(name="compId", required=false) String compId,
			ModelMap model) throws Exception {
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		String storId = dmStorBVo.getStorId();
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 팝업에서 회사ID가 넘어올경우 해당회사의 폴더 정보를 로드함
		if(popYn == null && "N".equals(popYn) && request.getRequestURI().startsWith("/dm/adm/") && compId != null && !compId.isEmpty()){
			List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(null, "Y", langTypCd);
			model.put("ptCompBVoList", ptCompBVoList);
		}

		if(compId == null || compId.isEmpty()) compId = userVo.getCompId();
		String orgId = userVo.getOrgId();
		// 전체 조회 여부
		boolean allChk = request.getRequestURI().startsWith("/dm/adm/") && (fldGrpId == null || fldGrpId.isEmpty());
		if(!allChk && fldGrpId != null && !fldGrpId.isEmpty() ){
			if(DmConstant.FLD_DEPT.equals(fldGrpId)) compId = null;
			if(DmConstant.FLD_COMP.equals(fldGrpId)) orgId = null;
		}
		// 미분류 조회 여부
		boolean noneChk = (request.getRequestURI().startsWith("/dm/adm/doc/") || request.getRequestURI().startsWith("/dm/doc/")) 
				&& (popYn == null || "N".equals(popYn) && (fldGrpId == null || fldGrpId.isEmpty()));
		List<DmFldBVo> dmFldBVoList = dmAdmSvc.getDmFldBVoList(storId, langTypCd, compId, orgId, allChk, noneChk);
		if(psnYn != null && !psnYn.isEmpty() && "Y".equals(psnYn) && !allChk){
			dmDocSvc.setPsnFldList(dmFldBVoList, storId, langTypCd, userVo.getUserUid());
		}
		model.put("dmFldBVoList", dmFldBVoList);
		
		// js - include 옵션
		model.put("JS_OPTS", new String[]{"tree"});
				
		model.put("UI_TITLE", messageProperties.getMessage("dm.btn.fldSel", request));
				
		return MoLayoutUtil.getJspPath("/dm/adm/fld/treeFldSub");
	}
	
	/** [Ajax] 폴더 - 트리 */
	@RequestMapping(value = {"/dm/doc/treeDocFldAjx","/dm/adm/doc/treeDocFldAjx","/dm/fld/treeDocFldAjx","/dm/adm/fld/treeDocFldAjx","/dm/adm/fld/treeFldAjx","/cm/doc/treeDocFldAjx"})
	public String treeDocFldAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		
		try {
			JSONObject jsonObject = (data==null || data.isEmpty()) 
					? new JSONObject(): (JSONObject)JSONValue.parse(data);
			
			String psnYn = (String)jsonObject.get("psnYn");
			String popYn = (String)jsonObject.get("popYn");
			String fldGrpId = (String)jsonObject.get("fldGrpId");
			
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			String storId = dmStorBVo.getStorId();
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			String compId = userVo.getCompId();
			String orgId = userVo.getOrgId();
			// 전체 조회 여부
			boolean allChk = request.getRequestURI().startsWith("/dm/adm/") && (fldGrpId == null || fldGrpId.isEmpty());
			if(!allChk && fldGrpId != null && !fldGrpId.isEmpty() ){
				if(DmConstant.FLD_DEPT.equals(fldGrpId)) compId = null;
				if(DmConstant.FLD_COMP.equals(fldGrpId)) orgId = null;
			}
			// 미분류 조회 여부
			boolean noneChk = (request.getRequestURI().startsWith("/dm/adm/doc/") || request.getRequestURI().startsWith("/dm/doc/")) && (popYn == null || "N".equals(popYn) && (fldGrpId == null || fldGrpId.isEmpty()));
			List<DmFldBVo> dmFldBVoList = dmAdmSvc.getDmFldBVoList(storId, langTypCd, compId, orgId, allChk, noneChk);
			if(psnYn != null && !psnYn.isEmpty() && "Y".equals(psnYn) && !allChk){
				dmDocSvc.setPsnFldList(dmFldBVoList, storId, langTypCd, userVo.getUserUid());
			}
			model.put("dmFldBVoList", dmFldBVoList);
			
			return JsonUtil.returnJson(model);
			
		} catch(Exception e) {
			e.printStackTrace();
			return JsonUtil.returnJson(model, e.getMessage());
		}
	}
	
}
