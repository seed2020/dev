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
import com.innobiz.orange.web.dm.svc.DmStorSvc;
import com.innobiz.orange.web.dm.vo.DmClsBVo;
import com.innobiz.orange.web.dm.vo.DmStorBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;

@Controller
public class MoDmAdmClsCtrl {
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 저장소 서비스 */
	@Resource(name = "dmStorSvc")
	private DmStorSvc dmStorSvc;
	
	/** 관리 서비스 */
	@Resource(name = "dmAdmSvc")
	private DmAdmSvc dmAdmSvc;
	
	/** 분류체계관리 */
	@RequestMapping(value = "/dm/adm/cls/setCls")
	public String setCls(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/dm/adm/cls/setCls");
	}
	
	/** [FRAME] 분류체계 트리 조회 */
	@RequestMapping(value = {"/dm/adm/cls/treeClsSub","/dm/doc/treeDocClsSub","/dm/adm/doc/treeDocClsSub","/cm/doc/treeDocClsSub"})
	public String treeClsSub(HttpServletRequest request,
			@Parameter(name="clsId", required=false) String clsId,
			@Parameter(name="initSelect", required=false) String initSelect,
			ModelMap model) throws Exception {
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		String storId = dmStorBVo.getStorId();
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		List<DmClsBVo> dmClsBVoList = dmAdmSvc.getDmClsBVoList(storId, langTypCd);
		model.put("dmClsBVoList", dmClsBVoList);
		
		if((clsId == null || clsId.isEmpty()) && (dmClsBVoList.size() > 0 && initSelect != null && "Y".equals(initSelect))){
			model.put("clsId", dmClsBVoList.get(0).getClsId());
		}
		
		// js - include 옵션
		model.put("JS_OPTS", new String[]{"tree"});
				
		model.put("UI_TITLE", messageProperties.getMessage("dm.btn.clsSel", request));
				
		return MoLayoutUtil.getJspPath("/dm/adm/cls/treeClsSub");
	}	
	
	/** [Ajax] 폴더 - 트리 */
	@RequestMapping(value = {"/dm/adm/cls/treeClsAjx","/dm/doc/treeDocClsAjx","/dm/adm/doc/treeDocClsAjx","/cm/doc/treeDocClsAjx"})
	public String treeDocClsAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		
		try {
			JSONObject jsonObject = (data==null || data.isEmpty()) 
					? new JSONObject(): (JSONObject)JSONValue.parse(data);
			
			String clsId = (String)jsonObject.get("clsId");
			String initSelect = (String)jsonObject.get("initSelect");
			
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			String storId = dmStorBVo.getStorId();
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			List<DmClsBVo> dmClsBVoList = dmAdmSvc.getDmClsBVoList(storId, langTypCd);
			model.put("dmClsBVoList", dmClsBVoList);
			// 분류체계ID 가 없을경우 초기 선택값을 세팅해준다.
			if((clsId == null || clsId.isEmpty()) && (dmClsBVoList.size() > 0 && initSelect != null && "Y".equals(initSelect))){
				model.put("clsId", dmClsBVoList.get(0).getClsId());
			}
			
			return JsonUtil.returnJson(model);
			
		} catch(Exception e) {
			e.printStackTrace();
			return JsonUtil.returnJson(model, e.getMessage());
		}
	}
	
}
