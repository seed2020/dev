package com.innobiz.orange.web.ct.ctrl;

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

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.ct.svc.CtCmntSvc;
import com.innobiz.orange.web.ct.svc.CtSiteSvc;
import com.innobiz.orange.web.ct.vo.CtEstbBVo;
import com.innobiz.orange.web.ct.vo.CtFncDVo;
import com.innobiz.orange.web.ct.vo.CtRescBVo;
import com.innobiz.orange.web.ct.vo.CtSiteBVo;
import com.innobiz.orange.web.ct.vo.CtSiteCatDVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.vo.PtCdBVo;

/** 커뮤니티 쿨사이트 */
@Controller
public class CtSiteCtrl {
	

	/** 공통 서비스 */
	@Autowired
	private CtCmntSvc ctCmntSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
//	/** 공통 서비스 */
//	@Autowired
//	private CtCmSvc ctCmSvc;
	
	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
//	/** 조직 공통 서비스 */
//	@Autowired
//	private OrCmSvc orCmSvc;
	
//	/** 첨부파일 서비스 */
//	@Autowired
//	private CtFileSvc ctFileSvc;
	
//	/** 리소스 조회 저장용 서비스 */
//	@Autowired
//	private CtRescSvc ctRescSvc;
	
	/** 커뮤니티 사이트 서비스 */
	@Autowired
	private CtSiteSvc ctSiteSvc;
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(CtSiteCtrl.class);
	
//	/** 카테고리 수정 */
//	@RequestMapping(value = "/ct/site/transCatMod")
//	public String transCatMod(HttpServletRequest request, ModelMap model)throws Exception{
//		
//		return LayoutUtil.getResultJsp();
//	}
	
	/**  쿨사이트 포틀릿 조회 */
	@RequestMapping(value = "/ct/site/listSitePtFrm")
	public String listSitePtFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
		String menuId = request.getParameter("menuId");
		String ctId = request.getParameter("ctId");
		
		CtSiteBVo ctSiteBVo = new CtSiteBVo();
		ctSiteBVo.setQueryLang(langTypCd);
		ctSiteBVo.setCtFncUid(menuId);
		ctSiteBVo.setCtId(ctId);
		ctSiteBVo.setOrderBy("REG_DT DESC");
		request.setAttribute("pageRowCnt", 4);//RowCnt 삽입
		
		Integer recodeCount = commonDao.count(ctSiteBVo);
		PersonalUtil.setPaging(request, ctSiteBVo, recodeCount);
		
		@SuppressWarnings("unchecked")
		List<CtSiteBVo> ctSiteList = (List<CtSiteBVo>) commonDao.queryList(ctSiteBVo);
		model.put("ctSiteList", ctSiteList);
		model.put("recodeCount", recodeCount);
		
		return LayoutUtil.getJspPath("/ct/site/listSitePtFrm");
	}
	
	/** 선택된 카테고리ID 전송 */
	@RequestMapping(value ="/ct/site/sendSiteCatId")
	public String sendSiteCatId(HttpServletRequest request, ModelMap model) throws Exception{
		try{
			String ctId = request.getParameter("ctId");
			String catId = request.getParameter("catSlt");
			model.put("todo", "parent.siteCatList('"+ catId +"');");
			model.put("ctId", ctId);
			
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** 카테고리 삭제*/
	@RequestMapping(value = "/ct/site/transSiteCatDel")
	public String transSiteCatDel(HttpServletRequest request, ModelMap model) throws Exception{
		
		try{
			//쿨사이트 카테고리 테이블
			CtSiteCatDVo ctSiteCatDVo = new CtSiteCatDVo();
			//String ctFncUid = request.getParameter("menuId");
			String ctId = request.getParameter("ctId");
			
			String catId = request.getParameter("catSlt");
			if (catId == null || catId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			QueryQueue queryQueue = new QueryQueue();
			
			ctSiteCatDVo.setCatId(catId);
			ctSiteCatDVo = (CtSiteCatDVo) commonDao.queryVo(ctSiteCatDVo);
			if (ctSiteCatDVo.getCatNmRescId() == null || ctSiteCatDVo.getCatNmRescId().isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			
			CtRescBVo ctRescBVo = new CtRescBVo();
			ctRescBVo.setRescId(ctSiteCatDVo.getCatNmRescId());
			@SuppressWarnings("unchecked")
			List<CtRescBVo> rescList = (List<CtRescBVo>) commonDao.queryList(ctRescBVo);
			
			for(CtRescBVo rescVo : rescList){
				queryQueue.delete(rescVo);
			}
			
			queryQueue.delete(ctSiteCatDVo);
			
			commonDao.execute(queryQueue);
			
			model.put("ctId", ctId);
			//cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("todo", "parent.siteCatList();");
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
			model.put("todo", "parent.siteCatList();");
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
		
	}
	
	/** 카테고리 저장 */
	@RequestMapping(value = "/ct/site/transCatSave")
	public String transCatSave(HttpServletRequest request,
			@Parameter(name="catId", required=false) String catId,
			ModelMap model) throws Exception {
		
		try{
			// 세션 사용 자 정보
			UserVo userVo = LoginSession.getUser(request);
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			//쿨사이트 카테고리 테이블
			//String ctFncUid = request.getParameter("menuId");
			String ctId = request.getParameter("ctId");
			
			QueryQueue queryQueue = new QueryQueue();
			
			//======================================================================================================
			//다국어 처리
			//======================================================================================================
			// 회사별 설정된 리소스의 어권 정보
			List<PtCdBVo> langTypCdList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
			
			//카테고리 관련 CT_RESC_B & CT_SITE_CAT_D 저장 및 삭제
			ctSiteSvc.collectCtRescBVo(request, queryQueue, langTypCdList);
			//======================================================================================================
			
			commonSvc.execute(queryQueue);
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "location.replace('/ct/site/listSite.do?menuId="+request.getParameter("menuId")+"&ctId="+ctId+"');");
			
		}catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
		
	}
	
	/**  카테고리 관리 페이지 내 카테고리 리스트*/
	@RequestMapping(value = "/ct/site/listSiteCatFrm")
	public String listSiteCatFrm(HttpServletRequest request, ModelMap model)throws Exception{
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		String ctFncUid = request.getParameter("menuId");
		
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		ctEstbBVo.setLangTyp(langTypCd);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		CtSiteCatDVo ctSiteCatDVo = new CtSiteCatDVo();
		ctSiteCatDVo.setCompId(ctEstbBVo.getCompId());
		ctSiteCatDVo.setLangTyp(langTypCd);
		ctSiteCatDVo.setCtId(ctId);
		@SuppressWarnings("unchecked")
		List<CtSiteCatDVo> siteCatList = (List<CtSiteCatDVo>) commonDao.queryList(ctSiteCatDVo);
		
		model.put("siteCatList", siteCatList);
		model.put("ctFncUid", ctFncUid);
		model.put("ctId", ctId);
				
		return LayoutUtil.getJspPath("/ct/site/listSiteCatFrm");
	}
	
	/** 카테고리 관리 페이지 */
	@RequestMapping(value = "/ct/site/setCatPop")
	public String setCatPop(HttpServletRequest request,
			@Parameter(name="catId", required=false) String catId,
			@Parameter(name="catNmRescId", required=false) String catNmRescId,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		String ctFncUid = request.getParameter("menuId");
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		ctEstbBVo.setLangTyp(langTypCd);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		CtSiteCatDVo ctSiteCatDVo = new CtSiteCatDVo();
		ctSiteCatDVo.setCompId(ctEstbBVo.getCompId());
		ctSiteCatDVo.setLangTyp(langTypCd);
		ctSiteCatDVo.setCtId(ctId);
		@SuppressWarnings("unchecked")
		List<CtSiteCatDVo> siteCatList = (List<CtSiteCatDVo>) commonDao.queryList(ctSiteCatDVo);
		
		List<CtRescBVo> ctSiteRescList = new ArrayList<CtRescBVo>();
		if(siteCatList.size() != 0){
			for(CtSiteCatDVo siteCatRescId:siteCatList){
				CtRescBVo ctRescBVo = new CtRescBVo();
				ctRescBVo.setRescId(siteCatRescId.getCatNmRescId());
				@SuppressWarnings("unchecked")
				List<CtRescBVo> rescList = (List<CtRescBVo>) commonDao.queryList(ctRescBVo);
				for(CtRescBVo storeRescVo : rescList){
					ctSiteRescList.add(storeRescVo);
				}
			}
		}
		
		
		model.put("siteCatList", siteCatList);
		model.put("ctSiteRescList", ctSiteRescList);
		model.put("ctSiteCatDVo", ctSiteCatDVo);
		model.put("ctFncUid", ctFncUid);
		model.put("langTypCd", langTypCd);
		model.put("ctId", ctId);
		
		return LayoutUtil.getJspPath("/ct/site/setCatPop");
		
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
		
		return LayoutUtil.getJspPath("/ct/site/viewSite");
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
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
			
			return JsonUtil.returnJson(model);
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
		//String siteId = request.getParameter("siteId");
		//String catId = request.getParameter("catId");
		
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
		model.put("todo", "parent.location.replace('/ct/site/listSite.do?menuId="+ctFncUid+"&ctId="+ctId+"');");
		
		//공통 처리 페이지
		return LayoutUtil.getResultJsp();
	}
	
	
	/** 쿨사이트 등록&수정 페이지 */
	@RequestMapping(value = "/ct/site/setSite")
	public String setSite(HttpServletRequest request,
			@RequestParam(value = "siteId", required = false) String siteId,			
			ModelMap model) throws Exception {
		
		CtSiteBVo ctSiteBVo = new CtSiteBVo();
		String ctId = request.getParameter("ctId");
		ctCmntSvc.putLeftMenu(request, ctId, model);
		
		//String catId = request.getParameter("catId");
		
		
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
		
		
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});
		model.put("ctId", ctId);
		model.put("siteId", siteId);
		
		model.put("ctSiteBVo", ctSiteBVo);
		model.put("siteCatList", siteCatList);
		
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		String fncUid = request.getParameter("menuId");
		
		/**
		 * 커뮤니티 기능정보에서 메뉴명 가져오기
		 */
		CtFncDVo ctFncDVo =  new CtFncDVo();
		//ctFncDVo.setCompId(userVo.getCompId());
		ctFncDVo.setCtFncUid(fncUid);
		ctFncDVo.setLangTyp(langTypCd);
		CtFncDVo CtFncDVo =  (CtFncDVo) commonDao.queryVo(ctFncDVo);
		model.put("menuTitle", CtFncDVo.getCtFncNm());
		
		return LayoutUtil.getJspPath("/ct/site/setSite");
	}
	
	/** 쿨사이트 리스트 페이지 */
	@RequestMapping(value = "/ct/site/listSite")
	public String listSite(HttpServletRequest request, ModelMap model) throws Exception{
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String ctId = request.getParameter("ctId");
		ctCmntSvc.putLeftMenu(request, ctId, model);
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
		
		
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});
		model.put("ctId", ctId);
		model.put("recodeCount", recodeCount);
		model.put("siteList", siteList);
		model.put("siteCatList", siteCatList);
		model.put("catId", catId);
		
		/**
		 * 커뮤니티 기능정보에서 메뉴명 가져오기
		 */
		CtFncDVo ctFncDVo =  new CtFncDVo();
		//ctFncDVo.setCompId(userVo.getCompId());
		ctFncDVo.setCtFncUid(fncUid);
		ctFncDVo.setLangTyp(langTypCd);
		CtFncDVo CtFncDVo =  (CtFncDVo) commonDao.queryVo(ctFncDVo);
		model.put("menuTitle", CtFncDVo.getCtFncNm());
		
		return LayoutUtil.getJspPath("/ct/site/listSite");
	}

}
