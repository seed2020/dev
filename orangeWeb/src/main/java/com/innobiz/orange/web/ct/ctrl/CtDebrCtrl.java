package com.innobiz.orange.web.ct.ctrl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
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
import com.innobiz.orange.web.ct.svc.CtDebrSvc;
import com.innobiz.orange.web.ct.vo.CtDebrBVo;
import com.innobiz.orange.web.ct.vo.CtDebrOpinDVo;
import com.innobiz.orange.web.ct.vo.CtEstbBVo;
import com.innobiz.orange.web.ct.vo.CtFncDVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.wc.svc.WcScdManagerSvc;

/** 토론실 */
@Controller
public class CtDebrCtrl {

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
	
//	/** 포털 공통 서비스 */
//	@Autowired
//	private PtCmSvc ptCmSvc;
	
//	/** 조직 공통 서비스 */
//	@Autowired
//	private OrCmSvc orCmSvc;
	
//	/** 첨부파일 서비스 */
//	@Autowired
//	private CtFileSvc ctFileSvc;
	
//	/** 리소스 조회 저장용 서비스 */
//	@Autowired
//	private CtRescSvc ctRescSvc;
	
	/** 커뮤니티 토론실 서비스 */
	@Autowired
	private CtDebrSvc ctDebrSvc;
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(CtDebrCtrl.class);
	
	/** 의견삭제 */
	@RequestMapping(value = "/ct/debr/transOpinDel")
	public String transOpinDel(HttpServletRequest request, 
			@RequestParam(value="data", required = true) String data,
			ModelMap model){
		
		try{
			
			//파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String debrId = (String) object.get("debrId");
			String opinOrdr = (String) object.get("opinOrdr");
			
			if(debrId == null || opinOrdr == null){
				//pt.msg.nodata.passed = 데이터가 잘못 전달되었습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			QueryQueue queryQueue = new QueryQueue();
			
			CtDebrOpinDVo ctDebrOpinDVo = new CtDebrOpinDVo();
			ctDebrOpinDVo.setDebrId(debrId);
			ctDebrOpinDVo.setOpinOrdr(Integer.parseInt(opinOrdr));
			ctDebrOpinDVo = (CtDebrOpinDVo) commonDao.queryVo(ctDebrOpinDVo);
			
			if(ctDebrOpinDVo == null){
				// ct.msg.opinNotExists = 의견이 존재하지 않습니다.
				throw new CmException("ct.msg.opinNotExists", request);
			}
			
			queryQueue.delete(ctDebrOpinDVo);
			
			commonDao.execute(queryQueue);
			
			// cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
			
		}catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		
		return JsonUtil.returnJson(model);
	}
	
	/** 토론실 포틀릿 조회 */
	@RequestMapping(value = "/ct/debr/listDebrPtFrm")
	public String listDebrPtFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		String menuId = request.getParameter("menuId");
		String ctId = request.getParameter("ctId");
		
		CtDebrBVo ctDebrBVo = new CtDebrBVo();
		ctDebrBVo.setCtFncUid(menuId);
		ctDebrBVo.setCtId(ctId);
		ctDebrBVo.setOrderBy("REG_DT DESC");
		request.setAttribute("pageRowCnt", 4);//RowCnt 삽입
		
		Integer recodeCount = commonDao.count(ctDebrBVo);
		PersonalUtil.setPaging(request, ctDebrBVo, recodeCount);
		@SuppressWarnings("unchecked")
		List<CtDebrBVo> ctDebrList = (List<CtDebrBVo>) commonDao.queryList(ctDebrBVo);
		model.put("ctDebrList", ctDebrList);
		model.put("recodeCount", recodeCount);
		
		return LayoutUtil.getJspPath("/ct/debr/listDebrPtFrm");
	}
	
	/** 의견 내용 */
	@RequestMapping(value = "/ct/debr/viewOpinPop")
	public String viewOpinPop(HttpServletRequest request, ModelMap model) throws Exception{
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		model.put("logUserUid", userVo.getUserUid());
		
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		String debrId = request.getParameter("debrId");
		String opinOrdr = request.getParameter("opinOrdr");
		String fncUid = request.getParameter("menuId");
		
		model.put("ctId", ctId);
		
		CtDebrBVo ctDebrBVo = new CtDebrBVo();
		ctDebrBVo.setDebrId(debrId);
		ctDebrBVo = (CtDebrBVo) commonDao.queryVo(ctDebrBVo);
		
		model.put("ctDebrBVo", ctDebrBVo);
		
		CtDebrOpinDVo ctDebrOpinDVo = new CtDebrOpinDVo();
		ctDebrOpinDVo.setOpinOrdr(Integer.parseInt(opinOrdr));
		ctDebrOpinDVo = (CtDebrOpinDVo) commonDao.queryVo(ctDebrOpinDVo);
		
		model.put("ctDebrOpinDVo", ctDebrOpinDVo);
		
		//조회이력 저장
		if(ctDebrSvc.saveReadHst(opinOrdr, userVo.getUserUid(), ctEstbBVo.getCompId())){
			//조회수 증가
			ctDebrSvc.addReadCnt(Integer.parseInt(opinOrdr));
		}
		
		ctCmntSvc.putAuthChk(request, model, "R", ctId, fncUid);
		ctCmntSvc.putAuthChk(request, model, "W", ctId, fncUid);
		ctCmntSvc.putAuthChk(request, model, "D", ctId, fncUid);
		model.put("myAuth", ctCmntSvc.getUserSeculCd(ctEstbBVo.getCompId(), userVo.getUserUid(), ctId));
		
		return LayoutUtil.getJspPath("/ct/debr/viewOpinPop");
	}
	
	/** 의견 목록 페이지 */
	@RequestMapping(value = "/ct/debr/listOpin")
	public String listOpin(HttpServletRequest request, ModelMap model)throws Exception{
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		model.put("logUserUid", userVo.getUserUid());
		
		String ctId = request.getParameter("ctId");
		String debrId = request.getParameter("debrId");
		String fncUid = request.getParameter("menuId");
		ctCmntSvc.putLeftMenu(request, ctId, model);
		model.put("ctId", ctId);
		model.put("debrId", debrId);
		
		CtDebrBVo ctDebrBVo = new CtDebrBVo();
		ctDebrBVo.setDebrId(debrId);
		ctDebrBVo = (CtDebrBVo) commonDao.queryVo(ctDebrBVo);
		model.put("ctDebrBVo", ctDebrBVo);
		
		CtDebrOpinDVo ctDebrOpinDVo = new CtDebrOpinDVo();
		VoUtil.bind(request, ctDebrOpinDVo);
		ctDebrOpinDVo.setDebrId(debrId);
		
		Integer recodeCount = commonDao.count(ctDebrOpinDVo);
		PersonalUtil.setPaging(request, ctDebrOpinDVo, recodeCount);
		@SuppressWarnings("unchecked")
		List<CtDebrOpinDVo> opinList = (List<CtDebrOpinDVo>) commonDao.queryList(ctDebrOpinDVo);
		
		ctCmntSvc.putAuthChk(request, model, "R", ctId, fncUid);
		ctCmntSvc.putAuthChk(request, model, "W", ctId, fncUid);
		ctCmntSvc.putAuthChk(request, model, "D", ctId, fncUid);
		model.put("myAuth", ctCmntSvc.getUserSeculCd(userVo.getCompId(), userVo.getUserUid(), ctId));
		
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});
		model.put("recodeCount", recodeCount);
		model.put("opinList", opinList);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		/**
		 * 커뮤니티 기능정보에서 메뉴명 가져오기
		 */
		CtFncDVo ctFncDVo =  new CtFncDVo();
		//ctFncDVo.setCompId(userVo.getCompId());
		ctFncDVo.setCtFncUid(fncUid);
		ctFncDVo.setLangTyp(langTypCd);
		CtFncDVo CtFncDVo =  (CtFncDVo) commonDao.queryVo(ctFncDVo);
		model.put("menuTitle", CtFncDVo.getCtFncNm());
		
		return LayoutUtil.getJspPath("/ct/debr/listOpin");
	}
	
	/** 의견 저장 */
	@RequestMapping(value = "/ct/debr/transSaveOpin")
	public String transSaveOpin(HttpServletRequest request, ModelMap model) throws Exception{
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		String ctFncUid = request.getParameter("menuId");
		String debrId = request.getParameter("debrId");
		
		
		CtDebrOpinDVo ctDebrOpinDVo = new CtDebrOpinDVo();
		VoUtil.bind(request, ctDebrOpinDVo);
		ctDebrOpinDVo.setCompId(ctEstbBVo.getCompId());
		ctDebrOpinDVo.setCtId(ctId);
		ctDebrOpinDVo.setDebrId(debrId);
		ctDebrOpinDVo.setQueryLang(langTypCd);
		
		QueryQueue queryQueue = new QueryQueue();
		ctDebrSvc.setCtOpin(request, ctDebrOpinDVo, queryQueue);
		
		commonDao.execute(queryQueue);
		
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		model.put("todo", "parent.location.replace('/ct/debr/listOpin.do?menuId="+ctFncUid+"&ctId="+ctId+"&debrId="+debrId+"');");
		
		//공통 처리 페이지
		return LayoutUtil.getResultJsp();
	}
	
	/** 의견 등록&수정 POP */
	@RequestMapping(value = "/ct/debr/setOpinPop")
	public String setOpinPop(HttpServletRequest request, ModelMap model) throws Exception{
		
		String ctId = request.getParameter("ctId");
		String debrId = request.getParameter("debrId");
		String opinOrdr = request.getParameter("opinOrdr");
		model.put("ctId", ctId);
		model.put("debrId", debrId);
		model.put("opinOrdr", opinOrdr);
		
		CtDebrBVo ctDebrBVo = new CtDebrBVo();
		ctDebrBVo.setDebrId(debrId);
		ctDebrBVo = (CtDebrBVo) commonDao.queryVo(ctDebrBVo);
		model.put("ctDebrBVo", ctDebrBVo);
		
		if(opinOrdr != null && opinOrdr != ""){
			CtDebrOpinDVo ctDebrOpinDVo = new CtDebrOpinDVo();
			ctDebrOpinDVo.setOpinOrdr(Integer.parseInt(opinOrdr));
			ctDebrOpinDVo = (CtDebrOpinDVo) commonDao.queryVo(ctDebrOpinDVo);
			model.put("ctDebrOpinDVo", ctDebrOpinDVo);
		}
		
		return LayoutUtil.getJspPath("/ct/debr/setOpinPop");
	}
	
	
	/** 토론실 마감 & 회기연장 */
	@RequestMapping(value = "/ct/debr/setDebrFinSitn")
	public String setDebrFinSitn(HttpServletRequest request,
			@RequestParam(value="data", required = true) String data,
			ModelMap model){
		
		try{
			
			//파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String debrId = (String) object.get("debrId");
			String signal = (String) object.get("signal");
			if(debrId == null){
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			CtDebrBVo ctDebrBVo = new CtDebrBVo();
			ctDebrBVo.setDebrId(debrId);
			ctDebrBVo = (CtDebrBVo) commonDao.queryVo(ctDebrBVo);
			if (ctDebrBVo == null) {
				// ct.msg.debrNotExists = 토론방이 존재하지 않습니다.
				throw new CmException("ct.msg.debrNotExists", request);
			}
			
			if(ctDebrBVo.getFinYn().equals("Y")){
				// ct.msg.finAlready = 이미 마감된 토론방입니다.
				throw new CmException("ct.msg.finAlready", request);
			}else{
				if(signal.equals("fin")){
					ctDebrBVo.setFinYn("Y");
				}else{
					ctDebrBVo.setSitn(ctDebrBVo.getSitn() + 1);
				}
			}
			
			commonDao.update(ctDebrBVo);
			
			if(signal.equals("fin")){
				// ct.msg.fin.success = 마감 처리되었습니다.
				model.put("message", messageProperties.getMessage("ct.msg.fin.success", request));
			}else{
				// ct.msg.sitn.success = 회기가 연장되었습니다.
				model.put("message", messageProperties.getMessage("ct.msg.sitn.success", request));
			}
			
			model.put("result", "ok");
			
		}catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** 토론실 삭제 */
	@RequestMapping(value = "/ct/debr/transDebrDel")
	public String transDebrDel(HttpServletRequest request,
			@RequestParam(value="data", required = true) String data,
			ModelMap model){
		
		try{
			
			//파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String debrId = (String) object.get("debrId");
			if(debrId == null){
				throw new CmException("pt.msg.nodata.passed", request);
			}
			QueryQueue queryQueue = new QueryQueue();
			
			CtDebrBVo ctDebrBVo = new CtDebrBVo();
			ctDebrBVo.setDebrId(debrId);
			ctDebrBVo = (CtDebrBVo) commonDao.queryVo(ctDebrBVo);
			
			if (ctDebrBVo == null) {
				// ct.msg.debrNotExists = 토론방이 존재하지 않습니다.
				throw new CmException("ct.msg.debrNotExists", request);
			}
			//해당 토론 삭제
			queryQueue.delete(ctDebrBVo);
			
			//해당 토론에 대한 의견 삭제
			CtDebrOpinDVo ctDebrOpinDVo = new CtDebrOpinDVo();
			ctDebrOpinDVo.setDebrId(debrId);
			queryQueue.delete(ctDebrOpinDVo);
			
			commonDao.execute(queryQueue);
			
			// cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
			
		}catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		
		return JsonUtil.returnJson(model);
	}
	
	
	/** 토론실 등록 */
	@RequestMapping(value = "/ct/debr/transSaveDebr")
	public String transSaveDebr(HttpServletRequest request, ModelMap model)throws Exception{
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
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
		
		CtDebrBVo ctDebrBVo = new CtDebrBVo();
		VoUtil.bind(request, ctDebrBVo);
		ctDebrBVo.setCompId(ctEstbBVo.getCompId());
		ctDebrBVo.setQueryLang(langTypCd);
		ctDebrBVo.setLangTyp(langTypCd);
		ctDebrBVo.setCtId(ctId);
		ctDebrBVo.setCtFncUid(ctFncUid);
		ctDebrBVo.setRegrUid(userVo.getUserUid());
		
		QueryQueue queryQueue = new QueryQueue();
		ctDebrSvc.setCtDebr(request, ctDebrBVo, queryQueue);
		
		commonDao.execute(queryQueue);
		
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		model.put("todo", "parent.location.replace('/ct/debr/listDebr.do?menuId="+ctFncUid+"&ctId="+ctId+"');");
		
		//공통 처리 페이지
		return LayoutUtil.getResultJsp();
	}
	
	
	/** 토론실 등록 페이지 */
	@RequestMapping(value = "/ct/debr/setDebrPop")
	public String setDebrPop(HttpServletRequest request, 
			@RequestParam(value = "debrId", required = false) String debrId,			
			ModelMap model) throws Exception {
		
		CtDebrBVo ctDebrBVo = new CtDebrBVo();
		//수정인 경우
		if(debrId != null && !debrId.isEmpty()){
			ctDebrBVo.setDebrId(debrId);
			ctDebrBVo = (CtDebrBVo) commonDao.queryVo(ctDebrBVo);
		}
		
		String ctId = request.getParameter("ctId");
		model.put("ctId", ctId);
		model.put("ctDebrBVo", ctDebrBVo);
		return LayoutUtil.getJspPath("/ct/debr/setDebrPop");
	}
	
	/** 토론실 목록*/
	@RequestMapping(value = "/ct/debr/listDebr")
	public String listDebr(HttpServletRequest request, ModelMap model) throws Exception{
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		model.put("logUserUid", userVo.getUserUid());
		
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});
		
		//커뮤니티 ID(get방식)
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		ctCmntSvc.putLeftMenu(request, ctId, model);
		
		String fncUid = request.getParameter("menuId");
		
		CtDebrBVo ctDebrBVo = new CtDebrBVo();
		VoUtil.bind(request, ctDebrBVo);
		ctDebrBVo.setCompId(ctEstbBVo.getCompId());
		ctDebrBVo.setCtFncUid(fncUid);
		ctDebrBVo.setCtId(ctId);
		
		Integer recodeCount = commonDao.count(ctDebrBVo);
		PersonalUtil.setPaging(request, ctDebrBVo, recodeCount);
		//토론실 목록
		@SuppressWarnings("unchecked")
		List<CtDebrBVo> ctDebrList = (List<CtDebrBVo>) commonDao.queryList(ctDebrBVo);
		
		QueryQueue queryQueue = new QueryQueue();
		
		//오늘 날짜에 따른 토론실 회기상태 업데이트
		for(CtDebrBVo storedCtDebrVo : ctDebrList){
			if(storedCtDebrVo.getFinYn().equals("N")){
				Calendar endDt = WcScdManagerSvc.stringConvertCal(storedCtDebrVo.getRegDt(),true);
				endDt.add(Calendar.WEEK_OF_YEAR, storedCtDebrVo.getSitn());
				Date today = new Date();
				if(today.after(endDt.getTime())){
					storedCtDebrVo.setFinYn("Y");
					queryQueue.update(storedCtDebrVo);
				}
			}			
		}
		
		if(queryQueue.size() != 0){
			commonDao.execute(queryQueue);
		}
		
		ctCmntSvc.putAuthChk(request, model, "R", ctId, fncUid);
		ctCmntSvc.putAuthChk(request, model, "W", ctId, fncUid);
		ctCmntSvc.putAuthChk(request, model, "D", ctId, fncUid);
		model.put("myAuth", ctCmntSvc.getUserSeculCd(userVo.getCompId(), userVo.getUserUid(), ctId));
		
		model.put("recodeCount", recodeCount);
		model.put("ctDebrList", ctDebrList);
		model.put("ctId", ctId);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);

		/**
		 * 커뮤니티 기능정보에서 메뉴명 가져오기
		 */
		CtFncDVo ctFncDVo =  new CtFncDVo();
		//ctFncDVo.setCompId(ctEstbBVo.getCompId());
		ctFncDVo.setCtFncUid(fncUid);
		ctFncDVo.setLangTyp(langTypCd);
		CtFncDVo CtFncDVo =  (CtFncDVo) commonDao.queryVo(ctFncDVo);
		model.put("menuTitle", CtFncDVo.getCtFncNm());
		
		return LayoutUtil.getJspPath("/ct/debr/listDebr");
	}
	
}
