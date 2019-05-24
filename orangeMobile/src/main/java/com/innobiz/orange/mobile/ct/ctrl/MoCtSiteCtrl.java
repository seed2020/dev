package com.innobiz.orange.mobile.ct.ctrl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.ct.svc.CtCmntSvc;
import com.innobiz.orange.web.ct.svc.CtSiteSvc;
import com.innobiz.orange.web.ct.vo.CtEstbBVo;
import com.innobiz.orange.web.ct.vo.CtSiteBVo;
import com.innobiz.orange.web.ct.vo.CtSiteCatDVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;

/** 커뮤니티 쿨사이트 */
@Controller
public class MoCtSiteCtrl {

	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 공통 서비스 */
	@Autowired
	private CtCmntSvc ctCmntSvc;
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 커뮤니티 사이트 서비스 */
	@Autowired
	private CtSiteSvc ctSiteSvc;
	
	/** 쿨사이트 리스트 페이지 */
	@RequestMapping(value = "/ct/site/listSite")
	public String listSite(HttpServletRequest request, ModelMap model) throws Exception{

		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String ctId = request.getParameter("ctId");

		String catId = request.getParameter("catId");
		String fncUid = request.getParameter("menuId");

		CtSiteBVo ctSiteBVo = new CtSiteBVo();
		VoUtil.bind(request, ctSiteBVo);
		ctSiteBVo.setCtId(ctId);
		if(catId != null && catId!= ""){
			ctSiteBVo.setCatId(catId);
		}
		
		Integer recodeCount = commonDao.count(ctSiteBVo);
		PersonalUtil.setPaging(request, ctSiteBVo, recodeCount);
		@SuppressWarnings("unchecked")
		List<CtSiteBVo> siteList = (List<CtSiteBVo>) commonDao.queryList(ctSiteBVo);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
		CtSiteCatDVo ctSiteCatDVo = new CtSiteCatDVo();
		ctSiteCatDVo.setCtId(ctId);
		ctSiteCatDVo.setQueryLang(langTypCd);
		@SuppressWarnings("unchecked")
		List<CtSiteCatDVo> siteCatList = (List<CtSiteCatDVo>) commonDao.queryList(ctSiteCatDVo);
		
		ctCmntSvc.putAuthChk(request, model, "R", ctId, fncUid);
		ctCmntSvc.putAuthChk(request, model, "W", ctId, fncUid);
		ctCmntSvc.putAuthChk(request, model, "D", ctId, fncUid);
		model.put("myAuth", ctCmntSvc.getUserSeculCd(userVo.getCompId(), userVo.getUserUid(), ctId));
		
		model.put("ctId", ctId);
		model.put("recodeCount", recodeCount);
		model.put("siteList", siteList);
		model.put("siteCatList", siteCatList);
		model.put("catId", catId);

		return MoLayoutUtil.getJspPath("/ct/site/listSite", "ct");
	}
	
	/** 쿨사이트 내용 */
	@RequestMapping(value = "/ct/site/viewSite")
	public String viewSite(HttpServletRequest request, ModelMap model) throws Exception{
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		model.put("logUserUid", userVo.getUserUid());
		
		String ctId = request.getParameter("ctId");
		ctCmntSvc.putLeftMenu(request, ctId, model);
		String siteId = request.getParameter("siteId");
		String catId = request.getParameter("catId");
		String fncUid = request.getParameter("menuId");
		
		CtSiteBVo ctSiteBVo = new CtSiteBVo();
		ctSiteBVo.setQueryLang(langTypCd);
		ctSiteBVo.setCtId(ctId);
		ctSiteBVo.setSiteId(siteId);
		
		ctSiteBVo = (CtSiteBVo) commonDao.queryVo(ctSiteBVo);
		
		ctCmntSvc.putAuthChk(request, model, "R", ctId, fncUid);
		ctCmntSvc.putAuthChk(request, model, "W", ctId, fncUid);
		ctCmntSvc.putAuthChk(request, model, "D", ctId, fncUid);
		model.put("myAuth", ctCmntSvc.getUserSeculCd(userVo.getCompId(), userVo.getUserUid(), ctId));
		
		model.put("ctId", ctId);
		model.put("siteId", siteId);
		model.put("catId", catId);
		model.put("ctSiteBVo", ctSiteBVo);
		
		return MoLayoutUtil.getJspPath("/ct/site/viewSite", "ct");
	}
	
	/**  쿨사이트 삭제*/
	@RequestMapping(value = "/ct/site/transSiteListDel")
	public String transSiteListDel(HttpServletRequest request,
			@RequestParam(value="data", required = true) String data,
			ModelMap model){
		
		try{
			//파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			JSONArray siteIds = (JSONArray) object.get("selectCtSiteIds");
			QueryQueue queryQueue = new QueryQueue();
			
			String ctId = request.getParameter("ctId");
			
			for(Object storeMbshId : siteIds){
				CtSiteBVo ctSiteBVo = new CtSiteBVo();
				ctSiteBVo.setCtId(ctId);
				ctSiteBVo.setSiteId(storeMbshId.toString());
				
				queryQueue.delete(ctSiteBVo);
			}
			
			commonDao.execute(queryQueue);
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
		} catch (Exception e) {
			model.put("message", e.getMessage());
		}
			
		return JsonUtil.returnJson(model);
	}
	
	/** 쿨사이트 등록&수정 페이지 */
	@RequestMapping(value = "/ct/site/setSite")
	public String setSite(HttpServletRequest request,
			@RequestParam(value = "siteId", required = false) String siteId,			
			ModelMap model) throws Exception {
		
		CtSiteBVo ctSiteBVo = new CtSiteBVo();
		String ctId = request.getParameter("ctId");
		ctCmntSvc.putLeftMenu(request, ctId, model);

		//수정인 경우
		if(siteId != null && !siteId.isEmpty()){
			ctSiteBVo.setSiteId(siteId);
			ctSiteBVo = (CtSiteBVo) commonDao.queryVo(ctSiteBVo);
			String catId = ctSiteBVo.getCatId();
			model.put("catId", catId);
		}
		
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		CtSiteCatDVo ctSiteCatDVo = new CtSiteCatDVo();
		ctSiteCatDVo.setQueryLang(langTypCd);
		ctSiteCatDVo.setCtId(ctId);
		@SuppressWarnings("unchecked")
		List<CtSiteCatDVo> siteCatList = (List<CtSiteCatDVo>) commonDao.queryList(ctSiteCatDVo);

		model.put("ctId", ctId);
		model.put("siteId", siteId);
		
		model.put("ctSiteBVo", ctSiteBVo);
		model.put("siteCatList", siteCatList);
		
		return MoLayoutUtil.getJspPath("/ct/site/setSite", "ct");
	}
	
	/** 쿨사이트 저장 */
	@RequestMapping(value = "/ct/site/transSaveSite")
	public String transSaveSite(HttpServletRequest request, ModelMap model) throws Exception{
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		ctEstbBVo.setLangTyp(langTypCd);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		String ctFncUid = request.getParameter("menuId");
		
		CtSiteBVo ctSiteBVo = new CtSiteBVo();
		VoUtil.bind(request, ctSiteBVo);
		ctSiteBVo.setCompId(ctEstbBVo.getCompId());
		ctSiteBVo.setCtId(ctId);
		ctSiteBVo.setCtFncUid(ctFncUid);
		ctSiteBVo.setQueryLang(langTypCd);
		
		QueryQueue queryQueue = new QueryQueue();
		ctSiteSvc.setCtSite(request, ctSiteBVo, queryQueue);
		
		commonDao.execute(queryQueue);
		
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		model.put("todo", "$m.nav.prev(event, '/ct/site/listSite.do?menuId="+ctFncUid+"&ctId="+ctId
				+"');");
		
		//공통 처리 페이지
		return MoLayoutUtil.getResultJsp();
	}
}
