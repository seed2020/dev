package com.innobiz.orange.mobile.dm.ctrl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.dm.svc.DmCmSvc;
import com.innobiz.orange.web.dm.svc.DmDocSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;

@Controller
public class MoDmFldCtrl {
	/** Logger */
	//private static final Logger LOGGER = Logger.getLogger(MoDmFldCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Autowired
	private DmCmSvc dmCmSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 문서 서비스 */
	@Autowired
	private DmDocSvc dmDocSvc;
	
	/** [팝업] 폴더 목록 조회 */
	@RequestMapping(value = {"/dm/doc/findFldSub","/dm/fld/findFldSub"})
	public String listClsPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// js - include 옵션
		model.put("JS_OPTS", new String[]{"tree"});
		
		model.put("UI_TITLE", messageProperties.getMessage("dm.btn.fldSel", request));
		
		return MoLayoutUtil.getJspPath("/dm/fld/findFldSub");
		
	}
	
	/** 폴더 트리 조회 */
	@RequestMapping(value = {"/dm/doc/treeFldFrm","/dm/fld/treeFldFrm","/dm/adm/doc/treeFldFrm"})
	public String treeFldFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 관리자여부
		boolean isAdmin = request.getRequestURI().startsWith("/dm/adm/");
				
		// 요청경로 세팅
		dmCmSvc.getRequestPath(request, model , "Fld");
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		boolean isSrchChk = true;
		// 관리자일 경우에는 조회조건에 등록자UID가 있어야만 조회 가능함.
		if(isAdmin){
			String regrUid = ParamUtil.getRequestParam(request, "regrUid", false);
			if(regrUid != null && !regrUid.isEmpty()) {
				// 폴더조회
				if(isSrchChk) model.put("dmFldBVoList", dmDocSvc.getDmFldBVoList(langTypCd, null, null, regrUid, null));
			}
		}else{
			// 폴더조회
			model.put("dmFldBVoList", dmDocSvc.getDmFldBVoList(langTypCd, null, null, userVo.getUserUid(), null));
		}
		
		return LayoutUtil.getJspPath("/dm/fld/treeFldFrm");
	}
	
	/** [Ajax] 폴더 - 트리 */
	@RequestMapping(value = {"/dm/doc/treeFldAjx","/dm/fld/treeFldAjx","/dm/adm/doc/treeFldAjx"})
	public String treeFldAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		
		try {
			// 관리자여부
			boolean isAdmin = request.getRequestURI().startsWith("/dm/adm/");
					
			// 요청경로 세팅
			dmCmSvc.getRequestPath(request, model , "Fld");
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			boolean isSrchChk = true;
			// 관리자일 경우에는 조회조건에 등록자UID가 있어야만 조회 가능함.
			if(isAdmin){
				String regrUid = ParamUtil.getRequestParam(request, "regrUid", false);
				if(regrUid != null && !regrUid.isEmpty()) {
					// 폴더조회
					if(isSrchChk) model.put("dmFldBVoList", dmDocSvc.getDmFldBVoList(langTypCd, null, null, regrUid, null));
				}
			}else{
				// 폴더조회
				model.put("dmFldBVoList", dmDocSvc.getDmFldBVoList(langTypCd, null, null, userVo.getUserUid(), null));
			}
			
			return JsonUtil.returnJson(model);
			
		} catch(Exception e) {
			e.printStackTrace();
			return JsonUtil.returnJson(model, e.getMessage());
		}
	}
	
}
