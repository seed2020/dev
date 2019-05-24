package com.innobiz.orange.web.ct.ctrl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.ct.svc.CtBullMastSvc;
import com.innobiz.orange.web.ct.svc.CtCmSvc;
import com.innobiz.orange.web.ct.svc.CtCmntSvc;
import com.innobiz.orange.web.ct.svc.CtDebrSvc;
import com.innobiz.orange.web.ct.svc.CtFileSvc;
import com.innobiz.orange.web.ct.svc.CtRecmdHstSvc;
import com.innobiz.orange.web.ct.svc.CtRescSvc;
import com.innobiz.orange.web.ct.svc.CtScdManagerSvc;
import com.innobiz.orange.web.ct.svc.CtScreHstSvc;
import com.innobiz.orange.web.ct.svc.CtSiteSvc;
import com.innobiz.orange.web.ct.svc.CtSurvSvc;
import com.innobiz.orange.web.ct.vo.CtBlonCatDVo;
import com.innobiz.orange.web.ct.vo.CtBullMastBVo;
import com.innobiz.orange.web.ct.vo.CtCatBVo;
import com.innobiz.orange.web.ct.vo.CtDebrBVo;
import com.innobiz.orange.web.ct.vo.CtEstbBVo;
import com.innobiz.orange.web.ct.vo.CtFileDVo;
import com.innobiz.orange.web.ct.vo.CtFncDVo;
import com.innobiz.orange.web.ct.vo.CtFncMbshAuthDVo;
import com.innobiz.orange.web.ct.vo.CtFncMngBVo;
import com.innobiz.orange.web.ct.vo.CtMbshDVo;
import com.innobiz.orange.web.ct.vo.CtPolcDVo;
import com.innobiz.orange.web.ct.vo.CtRecMastBVo;
import com.innobiz.orange.web.ct.vo.CtRescBVo;
import com.innobiz.orange.web.ct.vo.CtSchdlBVo;
import com.innobiz.orange.web.ct.vo.CtScrnSetupDVo;
import com.innobiz.orange.web.ct.vo.CtSiteBVo;
import com.innobiz.orange.web.ct.vo.CtSurvBVo;
import com.innobiz.orange.web.ct.vo.CtVistrHstDVo;
import com.innobiz.orange.web.em.svc.EmailSvc;
import com.innobiz.orange.web.em.vo.CmEmailBVo;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtCompBVo;
import com.innobiz.orange.web.wv.vo.WvSurvFileDVo;
//import scala.annotation.cloneable;

/** 커뮤니티 */
@Controller
public class CtCmntCtrl {
	
	/** 공통 서비스 */
	@Autowired
	private CtCmntSvc ctCmntSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Autowired
	private CtCmSvc ctCmSvc;
	
	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 커뮤니티 설문 서비스 */
	@Autowired
	private CtSurvSvc ctSurvSvc;
	
	/** 커뮤니티 일정 서비스 */
	@Autowired
	private CtScdManagerSvc ctScdManagerSvc;
	
	/** 포털 보안 서비스(레이아웃 포함) */
	@Autowired
	private PtSecuSvc ptSecuSvc; 
	
	/** 커뮤니티 게시판 서비스 */
	@Autowired
	private CtBullMastSvc ctBullMastSvc;
	
	/** 커뮤니티 토론실 서비스 */
	@Autowired
	private CtDebrSvc ctDebrSvc;
	
	/** 커뮤니티 쿨사이트 서비스 */
	@Autowired
	private CtSiteSvc ctSiteSvc;
	
	/** 커뮤니티  점수이력 서비스 */
	@Autowired
	private CtScreHstSvc ctScreHstSvc;
	
	/** 커뮤니티  추천이력 서비스 */
	@Autowired
	private CtRecmdHstSvc ctRecmdHstSvc;
	
	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** 첨부파일 서비스 */
	@Autowired
	private CtFileSvc ctFileSvc;
	
	/** 리소스 조회 저장용 서비스 */
	@Autowired
	private CtRescSvc ctRescSvc;
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 이메일 서비스 */
	@Resource(name = "emEmailSvc")
	private EmailSvc emailSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(CtCmntCtrl.class);

	/** 임시1 */
	@RequestMapping(value = "/ct/{path1}")
	public String boardLv1(HttpServletRequest request,
					@PathVariable("path1") String path1,
					ModelMap model) throws Exception {
		
		String ctId = request.getParameter("ctId");
		ctCmntSvc.putLeftMenu(request, ctId, model);
		
		// set, list 로 시작하는 경우 처리
		checkPath(request, path1, model);
		
		return LayoutUtil.getJspPath("/ct/" + path1);
	}

	private void checkPath(HttpServletRequest request, String path1,
			ModelMap model) throws SQLException {
		// listXXX 이면  
		// 페이지 정보 세팅
		CommonVo commonVo = new CommonVoImpl();
		PersonalUtil.setPaging(request, commonVo, 25);
		model.put("recodeCount", 25);
		
		// setXXX 이면
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});
	}
	
	
	
	/** 임시2 */
	@RequestMapping(value = "/ct/{path1}/{path2}")
	public String boardLv2(HttpServletRequest request,
					@PathVariable("path1") String path1,
					@PathVariable("path2") String path2,
					ModelMap model) throws Exception {
		
		String ctId = request.getParameter("ctId");
		ctCmntSvc.putLeftMenu(request, ctId, model);
	
		
		return LayoutUtil.getJspPath("/ct/" + path1 + "/" + path2);
	}
	
	/** 커뮤니티 개설정보 Pop */
	@RequestMapping(value = "/ct/adm/ctInfoPop")
	public String ctInfoPop(HttpServletRequest request, ModelMap model)throws Exception{
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setLangTyp(langTypCd);
		VoUtil.bind(request, ctEstbBVo);
		ctEstbBVo = (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		
		model.put("ctEstbBVo", ctEstbBVo);
		
		CtFncDVo ctFncDVo = new CtFncDVo();
		ctFncDVo.setCtId(ctEstbBVo.getCtId());
		ctFncDVo.setLangTyp(langTypCd);
		ctFncDVo.setOrderBy("CT_FNC_ORDR ASC");
		@SuppressWarnings("unchecked")
		List<CtFncDVo> ctFncList = (List<CtFncDVo>) commonDao.queryList(ctFncDVo);
		
		model.put("ctFncList", ctFncList);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		ctFileSvc.putFileListToModel(ctEstbBVo.getCtId(), model, userVo.getCompId());
		
		return LayoutUtil.getJspPath("/ct/adm/ctInfoPop");
	}
	
	/** 폐쇄 승인/거부 사유 Pop */
	@RequestMapping(value = "/ct/adm/setCloseApprOpPop")
	public String setCloseApprOpPop(HttpServletRequest request, ModelMap model)throws Exception{
		
		String apprYn = request.getParameter("apprYn");
		if(apprYn != null && apprYn != ""){
			model.put("apprYn", apprYn);
		}
		
		return LayoutUtil.getJspPath("/ct/adm/setCloseApprOpPop");
	}
	
	/** 폐쇄 사유 Pop */
	@RequestMapping(value = "/ct/adm/setCloseOpPop")
	public String setCloseOpPop(HttpServletRequest request, ModelMap model)throws Exception{
		return LayoutUtil.getJspPath("/ct/adm/setCloseOpPop");
	}
	/** 마스터 메일보내기 페이지 */
	@RequestMapping(value = "/ct/adm/listMastEmail")
	public String listMastEmail(HttpServletRequest request, ModelMap model)throws Exception{
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		CtMbshDVo ctMbshDVo = new CtMbshDVo();
		VoUtil.bind(request, ctMbshDVo);
		ctMbshDVo.setCompId(userVo.getCompId());
		ctMbshDVo.setQueryLang(langTypCd);
		ctMbshDVo.setLangTyp(langTypCd);
		
		List<String> userSeculCd = new ArrayList<String>();
		userSeculCd.add("M");
		userSeculCd.add("S");
		ctMbshDVo.setUserSeculCdList(userSeculCd);
		
//		CtEstbBVo ctEstbBVo = new CtEstbBVo();
//		VoUtil.bind(request, ctEstbBVo);
//		ctEstbBVo.setCompId(userVo.getCompId());
//		ctEstbBVo.setQueryLang(langTypCd);
//		ctEstbBVo.setLangTyp(langTypCd);
//		ctEstbBVo.setOrderBy("MAST_UID ASC");
		
		Integer recodeCount = commonDao.count(ctMbshDVo);
		PersonalUtil.setPaging(request, ctMbshDVo, recodeCount);
		@SuppressWarnings("unchecked")
		List<CtMbshDVo> ctMbshList = (List<CtMbshDVo>) commonDao.queryList(ctMbshDVo);
		
		model.put("recodeCount", recodeCount);
		model.put("ctMbshList", ctMbshList);
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if("Y".equals(sysPlocMap.get("mailEnable"))){
			model.put("mailEnable", "Y");
		}
		
		return LayoutUtil.getJspPath("/ct/adm/listMastEmail");
	}
	
	/** 이메일 전송 */
	@RequestMapping(value = {"/ct/transEmailAjx" , "/ct/adm/transEmailAjx"})
	public String sendEmailToCtMbsh(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception{
		
		try {
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			JSONArray userUids=(JSONArray) object.get("selectMbshList");
			
			if (userUids == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
//			String[][] userUidList = new String[userUids.size()][2];
//			
//			// jsonArray를 String[]에 담는다.
//			for(int i=0;i<userUids.size();i++){
//				
//				//Email 정보조회
//				OrUserPinfoDVo orUserPinfoDVo = new OrUserPinfoDVo();
//				orUserPinfoDVo.setOdurUid((String) userUids.get(i));
//				orUserPinfoDVo = commonEmailSvc.getOrUserPinfoDVo(orUserPinfoDVo);
//				
//				// 사용자 정보 조회
//				OrUserBVo orUserBVo = new OrUserBVo();
//				orUserBVo.setUserUid((String) userUids.get(i));
//				orUserBVo.setQueryLang(langTypCd);
//				orUserBVo = (OrUserBVo)commonDao.queryVo(orUserBVo);
//				
//				userUidList[i][0] = orUserBVo.getUserNm();
//				userUidList[i][1] = orUserPinfoDVo.getEmail();
//			}
			
			// 이메일 초기 정보 저장
			QueryQueue queryQueue = new QueryQueue();
			
			//이메일 Vo[업무별 정보 세팅-제목,내용]
			CmEmailBVo cmEmailBVo = new CmEmailBVo();
			 
			List<CommonFileVo> allFileList = new ArrayList<CommonFileVo>();
			
			String Context = "";
//			cmEmailBVo.setSubj(userVo.getUserNm() + "[" + wcsList  +"]");
			cmEmailBVo.setCont(Context);
			
			//이메일 정보 저장
			Integer emailId = emailSvc.saveEmailInfo(cmEmailBVo , userUids , allFileList , queryQueue , userVo);
			commonSvc.execute(queryQueue);
			//메세지 처리
			emailSvc.setEmailMessage(model, request, emailId);

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
		
	
	/** 엑셀파일 다운로드 (회원정보 검색 페이지) */
	@RequestMapping(value = "/ct/mbshListExcelDownLoad")
	public ModelAndView mbshListExcelDownLoad(HttpServletRequest request,
			@RequestParam(value = "ext", required = false) String ext,
			ModelMap model) throws Exception {
		
		try{
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			//UserVo userVo = LoginSession.getUser(request);
			String ctId = request.getParameter("ctId");
			
			// 커뮤니티 회원 상세(CT_MBSH_D) 테이블 - SELECT
			CtMbshDVo ctMbshDVo = new CtMbshDVo();
			VoUtil.bind(request, ctMbshDVo);
			ctMbshDVo.setQueryLang(langTypCd);
			ctMbshDVo.setLangTyp(langTypCd);
			//ctMbshDVo.setCompId(userVo.getCompId());
			ctMbshDVo.setCtId(ctId);
			
			//목록 조회
			@SuppressWarnings("unchecked")
			List<CtMbshDVo> ctMbshList = (List<CtMbshDVo>) commonDao.queryList(ctMbshDVo);
			
			if(ctMbshList.size() == 0 ){
				ModelAndView mv = new ModelAndView("cm/result/commonResult");
				mv.addObject("message", messageProperties.getMessage("em.msg.noExcelData", request));
				return mv;
			}
			
			// 파일 목록조회
			ModelAndView mv = new ModelAndView("excelDownloadView");
			
			//보여줄 목록 정의
			String[][] columns = new String [][]{{"userNm","ct.cols.userNm"},{"deptNm","ct.cols.deptNm"},{"joinDt","ct.cols.joinDt"},{"userSeculCd","ct.cols.userSeculCd"}};
			
			//컬럼명
			List<String> colNames = new ArrayList<String>();
			//데이터
			List<String> colValue = null;
			Map<String,Object> colValues = new HashMap<String,Object>();
			Map<String,Object> tempMap = null;
			
			for(int i=0;i<ctMbshList.size();i++){
				tempMap = VoUtil.toMap(ctMbshList.get(i), null);
				colValue = new ArrayList<String>();
				for(String[] column : columns){
					if(column[0] == "userSeculCd"){
						if(tempMap.get(column[0]).toString().equals("M")){
							colValue.add(messageProperties.getMessage("ct.cols.mastNm", request));
						}else if(tempMap.get(column[0]).toString().equals("S")){
							colValue.add(messageProperties.getMessage("ct.cols.mbshLev1", request));
						}else if(tempMap.get(column[0]).toString().equals("A")){
							colValue.add(messageProperties.getMessage("ct.cols.mbshLev3", request));
						}else if(tempMap.get(column[0]).toString().equals("R")){
							colValue.add(messageProperties.getMessage("ct.cols.mbshLev2", request));
						}
					}else{
						colValue.add(tempMap.get(column[0]).toString());
					}
					
					if( i == 0 )	colNames.add(messageProperties.getMessage(column[1], request)); //컬럼명 세팅
				}
				colValues.put("col"+i,colValue);//데이터 세팅
			}
			
			//시트명 세팅
			String sheetNm = "ct.jsp.listMbsh.title";
			
			mv.addObject("sheetName", messageProperties.getMessage(sheetNm, request));//시트명
			mv.addObject("colNames", colNames);//컬럼명
			mv.addObject("colValues", colValues);//데이터
			mv.addObject("fileName", messageProperties.getMessage(sheetNm, request));//파일명
			mv.addObject("ext", ext == null ? "xlsx" : ext);//파일 확장자(없으면 xls)
			
			return mv;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
		}
		return null;
	}
	
	/** 회원 정보 검색 페이지 */
	@RequestMapping(value = "/ct/listMbsh")
	public String listMbsh(HttpServletRequest request, ModelMap model)throws Exception{
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		String ctId = request.getParameter("ctId");
		ctCmntSvc.putLeftMenu(request, ctId, model);
		String fncUid = request.getParameter("menuId");
		
		CtMbshDVo ctMbshDVo = new CtMbshDVo();
		VoUtil.bind(request, ctMbshDVo);
		ctMbshDVo.setQueryLang(langTypCd);
		ctMbshDVo.setLangTyp(langTypCd);
		ctMbshDVo.setJoinStat("3");
		ctMbshDVo.setCtId(ctId);
		
		//================================================================================================
		//커뮤니티 가입 현황 전체/오늘 (model에 allPeople, todayPeople를 put한다.)
		//================================================================================================
		ctCmntSvc.putJoinPeopleStat(request, model, ctId);
		//================================================================================================
		
		Integer recodeCount = commonDao.count(ctMbshDVo);
		PersonalUtil.setPaging(request, ctMbshDVo, recodeCount);
		@SuppressWarnings("unchecked")
		List<CtMbshDVo> ctMbshList = (List<CtMbshDVo>) commonDao.queryList(ctMbshDVo);
		
		model.put("recodeCount", recodeCount);
		model.put("ctMbshList", ctMbshList);
		model.put("ctId", ctId);
		
		ctCmntSvc.putAuthChk(request, model, "R", ctId, fncUid);
		ctCmntSvc.putAuthChk(request, model, "W", ctId, fncUid);
		ctCmntSvc.putAuthChk(request, model, "D", ctId, fncUid);
		model.put("myAuth", ctCmntSvc.getUserSeculCd(userVo.getCompId(), userVo.getUserUid(), ctId));
		
		/**
		 * 커뮤니티 기능정보에서 메뉴명 가져오기
		 */
		CtFncDVo ctFncDVo =  new CtFncDVo();
		//ctFncDVo.setCompId(userVo.getCompId());
		ctFncDVo.setCtFncUid(fncUid);
		ctFncDVo.setLangTyp(langTypCd);
		CtFncDVo CtFncDVo =  (CtFncDVo) commonDao.queryVo(ctFncDVo);
		model.put("menuTitle", CtFncDVo.getCtFncNm());
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if("Y".equals(sysPlocMap.get("mailEnable"))){
			model.put("mailEnable", "Y");
		}
		
		return LayoutUtil.getJspPath("/ct/listMbsh");
	}
	
	
	/** 마스터 변경 저장 */
	@RequestMapping(value = "/ct/mng/transMngMastModSave")
	public String transMngMastModSave(HttpServletRequest request, ModelMap model) throws Exception{
		String listPage     = ParamUtil.getRequestParam(request, "listPage", true);
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		String ctId = request.getParameter("ctId");
		QueryQueue queryQueue = new QueryQueue();
		
		CtMbshDVo ctMbshDVo = new CtMbshDVo();
		//ctMbshDVo.setCompId(userVo.getCompId());
		ctMbshDVo.setCtId(ctId);
		
		ctCmntSvc.updateCtMast(request, ctMbshDVo, queryQueue);
		
		commonDao.execute(queryQueue);
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		//model.put("todo", "location.replace('/ct/listMyCm.do?')");
		model.put("todo", "parent.location.replace('" + listPage + "');");
		return LayoutUtil.getResultJsp();
	}
	
	/** 마스터변경 페이지 */
	@RequestMapping(value = "/ct/mng/setMngMast")
	public String setMngMast(HttpServletRequest request, ModelMap model) throws Exception{
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		String ctId = request.getParameter("ctId");
		ctCmntSvc.putLeftMenu(request, ctId, model);
		
		
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		//ctEstbBVo.setCompId(userVo.getCompId());
		ctEstbBVo.setCtId(ctId);
		ctEstbBVo.setQueryLang(langTypCd);
		ctEstbBVo.setLangTyp(langTypCd);
		ctEstbBVo = (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		
		model.put("ctId", ctId);
		model.put("mastNm", ctEstbBVo.getMastNm());
		model.put("prevMastUid", ctEstbBVo.getMastUid());
		
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
		
		// 타사 조직도 조회
		if(ctEstbBVo.getAllCompYn()!=null && "Y".equals(ctEstbBVo.getAllCompYn())){
			request.setAttribute("globalOrgChartEnable", Boolean.TRUE);
		}
				
		return LayoutUtil.getJspPath("/ct/mng/setMngMast");
	
	}
	
	
	/** 정보조회 페이지 엑셀파일 다운로드 (관리자) */
	@RequestMapping(value = "/ct/adm/excelDownLoad")
	public ModelAndView excelDownLoad(HttpServletRequest request,
			@RequestParam(value = "ext", required = false) String ext,
			ModelMap model) throws Exception {
		
		
		try{
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			//UserVo userVo = LoginSession.getUser(request);
			
			// 기간 시작일 & 마감일
			String startDt = request.getParameter("strtDt");
			String endDt = request.getParameter("finDt");
			
			// 커뮤니티 개설 기본(CT_ESTB_B) 테이블 - SELECT
			CtEstbBVo ctEstbBVo = new CtEstbBVo();
			VoUtil.bind(request, ctEstbBVo);
			ctEstbBVo.setQueryLang(langTypCd);
			//ctEstbBVo.setCompId(userVo.getCompId());
			ctEstbBVo.setLangTyp(langTypCd);
			ctEstbBVo.setCtActStat("A");
			if(startDt != null && !startDt.isEmpty()){
				ctEstbBVo.setTermStartDt(startDt + " 00:00:00");
				model.put("startDt", startDt);
			}
			
			if(endDt != null && !endDt.isEmpty()){
				ctEstbBVo.setTermEndDt(endDt + " 23:59:59");
				model.put("endDt", endDt);
			}
			//목록 조회
			@SuppressWarnings("unchecked")
			List<CtEstbBVo> ctEstbList = (List<CtEstbBVo>) commonDao.queryList(ctEstbBVo);
			
			if(ctEstbList.size() == 0 ){
				ModelAndView mv = new ModelAndView("cm/result/commonResult");
				mv.addObject("message", messageProperties.getMessage("em.msg.noExcelData", request));
				return mv;
			}
			
			// 파일 목록조회
			ModelAndView mv = new ModelAndView("excelDownloadView");
			
			//보여줄 목록 정의
			String[][] columns = new String [][]{{"ctNm","ct.cols.ctNm"},{"catNm","ct.cols.catNm"},{"mastNm","ct.cols.mastNm"},{"regDt","ct.cols.regDt"},{"ctActStat","ct.cols.ctActStat"},{"mbshCnt","ct.cols.mbshCnt"},{"visitCnt","ct.cols.visitCnt"},{"bullCnt","ct.cols.bullCnt"}};
			
			//컬럼명
			List<String> colNames = new ArrayList<String>();
			//데이터
			List<String> colValue = null;
			Map<String,Object> colValues = new HashMap<String,Object>();
			Map<String,Object> tempMap = null;
			
			for(int i=0;i<ctEstbList.size();i++){
				tempMap = VoUtil.toMap(ctEstbList.get(i), null);
				colValue = new ArrayList<String>();
				for(String[] column : columns){
					if(column[0] == "mbshCnt"){
						if(tempMap.get("ctTermMbshCnt") == null){
							colValue.add("0/"+tempMap.get("mbshCnt").toString());
						}else{
							colValue.add(tempMap.get("ctTermMbshCnt").toString()+"/"+tempMap.get("mbshCnt").toString());
						}
					}else if(column[0] == "visitCnt"){
						if(tempMap.get("ctTermVisitCnt") == null){
							colValue.add("0/"+tempMap.get("ctTotalVisitCnt").toString());
						}else{
							colValue.add(tempMap.get("ctTermVisitCnt").toString()+"/"+tempMap.get("ctTotalVisitCnt").toString());
						}
					}else if(column[0] == "bullCnt"){
						if(tempMap.get("ctTermBullCnt") == null){
							colValue.add("0/"+tempMap.get("ctTotalBullCnt").toString());
						}else{
							colValue.add(tempMap.get("ctTermBullCnt").toString()+"/"+tempMap.get("ctTotalBullCnt").toString());
						}
					}else if(column[0] == "ctActStat"){
						if(tempMap.get(column[0]).toString().equals("S")){
							colValue.add(messageProperties.getMessage("ct.cols.ready", request));
						}else if(tempMap.get(column[0]).toString().equals("A")){
							colValue.add(messageProperties.getMessage("ct.cols.act", request));
						}else{
							colValue.add(messageProperties.getMessage("ct.cols.close", request));
						}
					}else{
						colValue.add(tempMap.get(column[0]).toString());
					}
					if( i == 0 )	colNames.add(messageProperties.getMessage(column[1], request)); //컬럼명 세팅
				}
				colValues.put("col"+i,colValue);//데이터 세팅
			}
			
			//시트명 세팅
			String sheetNm = "ct.jsp.listUseStat.title";
			
			mv.addObject("sheetName", messageProperties.getMessage(sheetNm, request));//시트명
			mv.addObject("colNames", colNames);//컬럼명
			mv.addObject("colValues", colValues);//데이터
			mv.addObject("fileName", messageProperties.getMessage(sheetNm, request));//파일명
			mv.addObject("ext", ext == null ? "xlsx" : ext);//파일 확장자(없으면 xls)
			
			return mv;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
		}
		return null;
	}
	
	/** 정보조회 페이지(관리자) */
	@RequestMapping(value = "/ct/adm/listUseStat")
	public String listUseStat(HttpServletRequest request, ModelMap model) throws Exception{
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 기간 시작일 & 마감일
		String startDt = request.getParameter("strtDt");
		String endDt = request.getParameter("finDt");
		
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		VoUtil.bind(request, ctEstbBVo);
		//ctEstbBVo.setCompId(userVo.getCompId());
		ctEstbBVo.setQueryLang(langTypCd);
		ctEstbBVo.setLangTyp(langTypCd);
		ctEstbBVo.setCtActStat("A");
		
		if(startDt != null && !startDt.isEmpty()){
			ctEstbBVo.setTermStartDt(startDt + " 00:00:00");
			model.put("startDt", startDt);
		}
		
		if(endDt != null && !endDt.isEmpty()){
			ctEstbBVo.setTermEndDt(endDt + " 23:59:59");
			model.put("endDt", endDt);
		}
		
		Integer recodeCount = commonDao.count(ctEstbBVo);
		PersonalUtil.setPaging(request, ctEstbBVo, recodeCount);
		@SuppressWarnings("unchecked")
		List<CtEstbBVo> ctEstbList = (List<CtEstbBVo>) commonDao.queryList(ctEstbBVo);
		
		model.put("recodeCount", recodeCount);
		model.put("ctEstbList", ctEstbList);
		
		
		return LayoutUtil.getJspPath("/ct/adm/listUseStat");
	}
	
	/** [버튼] 일반, 장려, 우수, 최우수 */
	@RequestMapping(value = "/ct/adm/ajaxCtEvalCdCng")
	public String ajaxCtEvalCdCng(HttpServletRequest request,
			@RequestParam(value="data", required = true) String data,
			ModelMap model){
		
		try{
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			//파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			JSONArray ctIds = (JSONArray) object.get("selectCtIds");
			String evalCd = (String) object.get("evalCd");
			
			if (ctIds.size() == 0) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			QueryQueue queryQueue = new QueryQueue();
			
			for(Object storeCtId : ctIds){
				CtEstbBVo ctEstbBVo = new CtEstbBVo();
				ctEstbBVo.setCompId(userVo.getCompId());
				ctEstbBVo.setCtId(storeCtId.toString());
				ctEstbBVo = (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
				ctEstbBVo.setCtEvalCd(evalCd);
				queryQueue.update(ctEstbBVo);
			}
			
			commonDao.execute(queryQueue);
			// ct.msg.change.success = 변경되었습니다.
			model.put("message", messageProperties.getMessage("ct.msg.change.success", request));
			model.put("result", "ok");
			
		}catch (CmException e) {
			model.put("message", e.getMessage());
		}catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** 우수 커뮤니티 페이지(관리자) */
	@RequestMapping(value = "/ct/adm/listExntCm")
	public String listExntCm(HttpServletRequest request, ModelMap model) throws Exception{
		
		
		//File file = new File(request.getSession().getServletContext().getRealPath("WEB-INF") +"/classes/com/innobiz/innogw/ct/CT_ScoreInfo.xml");
		DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuild = docBuildFact.newDocumentBuilder();
		Document doc = docBuild.parse(CtCmntCtrl.class.getResourceAsStream("/com/innobiz/orange/web/ct/CT_ScoreInfo.xml"));
		doc.getDocumentElement().normalize();
		
		// 회원수
		NodeList UserNumList = doc.getElementsByTagName("UserNum");
		Node userNumNode = UserNumList.item(0);
		
		// 게시물 수
		NodeList nBoardCountList = doc.getElementsByTagName("nBoardCount");
		Node nBoardCountNode = nBoardCountList.item(0);
		
		// 게시판 활동 인원수
		NodeList nBoardUserCountList = doc.getElementsByTagName("nBoardUserCount");
		Node nBoardUserCountNode = nBoardUserCountList.item(0);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 검색 시 SELECT 조건 [일반 // 장려 // 우수 // 최우수]
		String schCondition = request.getParameter("schCondition");
		
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		VoUtil.bind(request, ctEstbBVo);
		ctEstbBVo.setCompId(userVo.getCompId());
		ctEstbBVo.setQueryLang(langTypCd);
		ctEstbBVo.setLangTyp(langTypCd);
		
		if(schCondition != null){
			ctEstbBVo.setCtEvalCd(schCondition);
		}
		
		ctEstbBVo.setCtActStat("A");
		
		Integer recodeCount = commonDao.count(ctEstbBVo);
		PersonalUtil.setPaging(request, ctEstbBVo, recodeCount);
		@SuppressWarnings("unchecked")
		List<CtEstbBVo> ctEstbVoList = (List<CtEstbBVo>) commonDao.queryList(ctEstbBVo);
		
		List<CtEstbBVo> ctEstbList = new ArrayList<CtEstbBVo>();
		
		//점수 셋팅
		for(CtEstbBVo estbBVo : ctEstbVoList){
			
			//회원수에 따른 점수
			if(userNumNode.getNodeType() == Node.ELEMENT_NODE){
				Element userNum = (Element) userNumNode;
				
				//userNum 에 uPoint_0~4 태그 / count = userNum 노드 안의 element 수
				for(int i=0; i < Integer.parseInt(userNum.getAttribute("count")); i++ ){
					NodeList uPointList = userNum.getElementsByTagName("uPoint_"+i);
					
					Element uPoint = (Element) uPointList.item(0);
					Node score = uPoint.getFirstChild();
					
					int minVal = Integer.parseInt(uPoint.getAttribute("min"));
					
					if(minVal <= estbBVo.getMbshCnt()){
						if(i != (Integer.parseInt(userNum.getAttribute("count")) - 1) ){
							int maxVal = Integer.parseInt(uPoint.getAttribute("max"));
							if(estbBVo.getMbshCnt() < maxVal){
								estbBVo.setMbshCntScore(Integer.parseInt(score.getNodeValue()));
							}
						}
					
					}
					
				}
			}
			//회원수에 따른 점수
			Integer mbshCntScore = estbBVo.getMbshCntScore();
			if(mbshCntScore==null) mbshCntScore = 0;
			
			//게시물 수에 따른 점수
			if(nBoardCountNode.getNodeType() == Node.ELEMENT_NODE){
				Element nBoardCount = (Element) nBoardCountNode;
				
				//nBoardCount 에 uPoint_0~9 태그 / count = nBoardCount 노드 안의 element 수
				for(int i=0; i < Integer.parseInt(nBoardCount.getAttribute("count")); i++ ){
					NodeList nBPointList = nBoardCount.getElementsByTagName("nBPoint_"+i);
					
					Element nBPoint = (Element) nBPointList.item(0);
					Node score = nBPoint.getFirstChild();
					int minVal = Integer.parseInt(nBPoint.getAttribute("min"));
					
					if(minVal <= estbBVo.getCtTotalBullCnt()){
						if(i != (Integer.parseInt(nBoardCount.getAttribute("count")) - 1) ){
							int maxVal = Integer.parseInt(nBPoint.getAttribute("max"));
							if(estbBVo.getCtTotalBullCnt() < maxVal){
								estbBVo.setBullCntScore(Integer.parseInt(score.getNodeValue()));
							}
						}
					
					}
				
				}
				
			}
			
			//게시물 수에 따른 점수
			Integer bullCntScore = estbBVo.getBullCntScore();
			if(bullCntScore==null) bullCntScore = 0;
			// 커뮤니티 게시판 활동 인원수에 따른 점수
			if(nBoardUserCountNode.getNodeType() == Node.ELEMENT_NODE){
				Element nBoardUserCount = (Element) nBoardUserCountNode;
				
				//nBoardUserCount 에 uPoint_0~3 태그 / count = nBoardUserCount 노드 안의 element 수
				for(int i=0; i < Integer.parseInt(nBoardUserCount.getAttribute("count")); i++ ){
					NodeList nBUPointList = nBoardUserCount.getElementsByTagName("nBUPoint_"+i);
					
					Element nBUPoint = (Element) nBUPointList.item(0);
					Node score = nBUPoint.getFirstChild();
					int minVal = Integer.parseInt(nBUPoint.getAttribute("min"));
					
					if(minVal <= estbBVo.getBullRealActUserCnt()){
						if(i != (Integer.parseInt(nBoardUserCount.getAttribute("count")) - 1) ){
							int maxVal = Integer.parseInt(nBUPoint.getAttribute("max"));
							if(estbBVo.getBullRealActUserCnt() < maxVal){
								estbBVo.setBullActUserCntScore(Integer.parseInt(score.getNodeValue()));
							}
						}
					
					}
				
				}
				
			}
			
			// 커뮤니티 게시판 활동 인원수에 따른 점수
			Integer bullActUserCntScore = estbBVo.getBullActUserCntScore();
			if(bullActUserCntScore==null) bullActUserCntScore = 0;
			//평가 점수
			estbBVo.setEvalScore(mbshCntScore + bullCntScore + bullActUserCntScore);
			
			ctEstbList.add(estbBVo);
			
		}
		
		model.put("recodeCount", recodeCount);
		model.put("ctEstbList", ctEstbList);
		model.put("schCondition", schCondition);
		
		
		return LayoutUtil.getJspPath("/ct/adm/listExntCm");
	}
	/** [버튼] 폐쇄*/
	@RequestMapping(value= "/ct/adm/ajaxCtClose")
	public String ajaxCtClose(HttpServletRequest request,
			@RequestParam(value="data", required = true) String data,
			ModelMap model){
		
		try{
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			//파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			JSONArray ctIds = (JSONArray) object.get("selectCtIds");
			String closeOp = (String) object.get("closeOp");
			if (ctIds.size() == 0) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			QueryQueue queryQueue = new QueryQueue();
			
			for(Object storeCtId : ctIds){
				CtEstbBVo ctEstbBVo = new CtEstbBVo();
				ctEstbBVo.setCompId(userVo.getCompId());
				ctEstbBVo.setLangTyp(langTypCd);
				ctEstbBVo.setQueryLang(langTypCd);
				ctEstbBVo.setCtId(storeCtId.toString());
				ctEstbBVo = (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
				if(ctEstbBVo.getCtActStat().equals("C")){
					// ct.msg.close.already = 이미 폐쇄된 커뮤니티 입니다.
					throw new CmException("ct.msg.close.already", request);
				}
				ctEstbBVo.setRjtOpinCont(closeOp);
				ctEstbBVo.setCtActStat("C");
				ctEstbBVo.setCtStat("C");
				//폐쇄 및 사유 업데이트
				queryQueue.update(ctEstbBVo);
				
				//폐쇄 커뮤니티의 회원정보 삭제
				CtMbshDVo ctMbshDVo = new CtMbshDVo();
				ctMbshDVo.setCtId(ctEstbBVo.getCtId());
				@SuppressWarnings("unchecked")
				List<CtMbshDVo> ctMbshList = (List<CtMbshDVo>) commonDao.queryList(ctMbshDVo);
				for(CtMbshDVo delMbshVo : ctMbshList){
					if(!delMbshVo.getUserSeculCd().equals("M")){
						queryQueue.delete(delMbshVo);
					}
				}
				
				// 시스템 정책 조회
				Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
				
				//이메일 발송
				if(sysPlocMap!=null && "Y".equals(sysPlocMap.get("mailEnable"))){
					//커뮤니티 마스터 이메일
					String[] toUids = new String[1]; toUids[0] = ctEstbBVo.getMastUid();
					String subject = "["+messageProperties.getMessage("ct.cols.ctNm", request)+"] '"+ctEstbBVo.getCtNm()+"' "+messageProperties.getMessage("ct.msg.ctIsClosed", request);

					String p1 = "<p>";
					String span1 = "<span style='font-weight: bold;'>";
					String span2 = "</span>";
					String p2 = "</p>";
					// 사유
					// 이메일 내용 생성
					StringBuilder sb = new StringBuilder();
					sb.append(p1).append(span1).append(messageProperties.getMessage("ct.cols.ctNm", request)).append(span2).append(" : ").append(ctEstbBVo.getCtNm()).append(p2); // 커뮤니티 명
					sb.append(p1).append(span1).append(messageProperties.getMessage("ct.cols.mastNm", request)).append(span2).append(" : ").append(ctEstbBVo.getMastNm()).append(p2); // 마스터 명
					sb.append(p1).append(span1).append(messageProperties.getMessage("ct.cols.opin", request)).append(span2).append(" : ").append(closeOp).append(p2); // 사유
					
					//메일 발송
					emailSvc.sendMailSvc(userVo.getUserUid(), toUids, subject, sb.toString(), null, true,true, langTypCd);
				}
			}
			
			commonDao.execute(queryQueue);
			// ct.msg.close.success=폐쇄되었습니다.
			model.put("message", messageProperties.getMessage("ct.msg.close.success", request));
			model.put("result", "ok");
			
						
		}catch (CmException e) {
			model.put("message", e.getMessage());
		}catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
		
	}
	
	/** [버튼] 추천 */
	@RequestMapping(value = "/ct/adm/ajaxCtRecomende")
	public String ajaxCtRecomende(HttpServletRequest request,
			@RequestParam(value="data", required = true) String data,
			ModelMap model){
		
		try{
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			//파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			JSONArray ctIds = (JSONArray) object.get("selectCtIds");
			
			if (ctIds.size() == 0) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			QueryQueue queryQueue = new QueryQueue();
			
			for(Object storeCtId : ctIds){
				CtEstbBVo ctEstbBVo = new CtEstbBVo();
				ctEstbBVo.setCompId(userVo.getCompId());
				ctEstbBVo.setCtId(storeCtId.toString());
				ctEstbBVo = (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
				if(ctEstbBVo.getCtActStat().equals("C")){
					// ct.msg.close.passed = 폐쇄된 커뮤니티는 추천할 수 없습니다.
					throw new CmException("ct.msg.close.passed", request);
				}
				
				ctEstbBVo.setRecmdYn("Y");
				ctEstbBVo.setRecmdDt(ctCmntSvc.currentDay());
				
				queryQueue.update(ctEstbBVo);
			}
			
			commonDao.execute(queryQueue);
			
			model.put("message", messageProperties.getMessage("ct.msg.recomende.success", request));
			model.put("result", "ok");
			
		}catch (CmException e) {
			model.put("message", e.getMessage());
		}catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
			
	
	/** 마스터 변경 저장 */
	@RequestMapping(value = "/ct/adm/transMastModSave")
	public String transMastModSave(HttpServletRequest request, ModelMap model) throws Exception{
		String listPage = ParamUtil.getRequestParam(request, "listPage", true);
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		String ctId = request.getParameter("ctId");
		QueryQueue queryQueue = new QueryQueue();
		
		CtMbshDVo ctMbshDVo = new CtMbshDVo();
		//ctMbshDVo.setCompId(userVo.getCompId());
		ctMbshDVo.setCtId(ctId);
		
		ctCmntSvc.updateCtMast(request, ctMbshDVo, queryQueue);
		
		commonDao.execute(queryQueue);
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		//model.put("todo", "location.replace('/ct/adm/listCmInfo.do?')");
		model.put("todo", "parent.location.replace('" + listPage + "');");
		return LayoutUtil.getResultJsp();
	}
	
	/** 마스터변경 [POP] */
	@RequestMapping(value = "/ct/adm/setMastPop")
	public String setMastPop(HttpServletRequest request, ModelMap model) throws Exception{
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		String ctId = request.getParameter("ctId");
		
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		//ctEstbBVo.setCompId(userVo.getCompId());
		ctEstbBVo.setCtId(ctId);
		ctEstbBVo.setQueryLang(langTypCd);
		ctEstbBVo.setLangTyp(langTypCd);
		ctEstbBVo = (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		
		model.put("ctId", ctId);
		model.put("mastNm", ctEstbBVo.getMastNm());
		model.put("prevMastUid", ctEstbBVo.getMastUid());
		return LayoutUtil.getJspPath("/ct/adm/setMastPop");
	
	}
	/** 정보변경 페이지(관리자) */
	@RequestMapping(value = "/ct/adm/listCmInfo")
	public String listCmInfo(HttpServletRequest request, ModelMap model)throws Exception{
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 검색 시 SELECT 조건 [추천 // 일반 // 폐쇄]
		String schCondition = request.getParameter("schCondition");
		
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		VoUtil.bind(request, ctEstbBVo);
		ctEstbBVo.setCompId(userVo.getCompId());
		ctEstbBVo.setQueryLang(langTypCd);
		ctEstbBVo.setLangTyp(langTypCd);
		
		if(schCondition != null){
			if(schCondition.equals("C")){
				ctEstbBVo.setCtActStat("C");
			}else{
				ctEstbBVo.setCtActStat("A");
				ctEstbBVo.setRecmdYn(schCondition);
			}
		}else{
			ctEstbBVo.setCtActStat("A");
		}
		
		Integer recodeCount = commonDao.count(ctEstbBVo);
		PersonalUtil.setPaging(request, ctEstbBVo, recodeCount);
		@SuppressWarnings("unchecked")
		List<CtEstbBVo> ctEstbList = (List<CtEstbBVo>) commonDao.queryList(ctEstbBVo);
		
		model.put("recodeCount", recodeCount);
		model.put("ctEstbList", ctEstbList);
		model.put("schCondition", schCondition);
		
		return LayoutUtil.getJspPath("/ct/adm/listCmInfo");
	}
	
	/** 커뮤니티 회원정보변경 내 이메일 발송 */
	@RequestMapping(value = "/ct/mng/transEmailAjx")
	public String sendMailCtMbsh(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception{
		
		try {
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);

			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			JSONArray mbshIds=(JSONArray) object.get("selectCtMbshIds");
			
			if (mbshIds == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 이메일 초기 정보 저장
			QueryQueue queryQueue = new QueryQueue();
			
			//이메일 Vo[업무별 정보 세팅-제목,내용]
			CmEmailBVo cmEmailBVo = new CmEmailBVo();
			
			List<CommonFileVo> allFileList = new ArrayList<CommonFileVo>();
			String Context = "";
//			cmEmailBVo.setSubj(userVo.getUserNm() + "[" + wcsList  +"]");
			cmEmailBVo.setCont(Context);
			
			//이메일 정보 저장
			Integer emailId = emailSvc.saveEmailInfo(cmEmailBVo , mbshIds , allFileList , queryQueue , userVo);
			
			commonSvc.execute(queryQueue);
			//메세지 처리
			emailSvc.setEmailMessage(model, request, emailId);
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	/** 커뮤니티 회원 권한 수정 List */
	@RequestMapping(value = "/ct/mng/transMbshListAuthMod")
	public String transMbshListAuthMod(HttpServletRequest request,
			@RequestParam(value="data", required = true) String data,
			ModelMap model){
		
		try{
			//파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			JSONArray mbshIds = (JSONArray) object.get("selectCtMbshIds");
			String mbshCtId = (String) object.get("mbshCtId");
			String mbshDefAuth = (String) object.get("mbshDefAuth");
			
			QueryQueue queryQueue = new QueryQueue();
			
			for(Object storeMbshId : mbshIds){
				CtMbshDVo ctMbshDVo = new CtMbshDVo();
				ctMbshDVo.setCtId(mbshCtId);
				ctMbshDVo.setUserUid(storeMbshId.toString());
				ctMbshDVo.setUserSeculCd(mbshDefAuth);
				
				queryQueue.update(ctMbshDVo);
			}
			
			commonDao.execute(queryQueue);
			
			model.put("message", messageProperties.getMessage("ct.msg.mbshAuthMod.success", request));
			model.put("result", "ok");
		}
		//catch (CmException e) {
				//	model.put("message", e.getMessage());
				//}
				
		catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
		
	}
	
	
	/** 커뮤니티 회원 임의 탈퇴 List */
	@RequestMapping(value = "/ct/mng/transMbshListDel")
	public String transMbshListDel(HttpServletRequest request, 
			@RequestParam(value="data", required = true) String data,
			ModelMap model){
		
		try{
			
			//세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			//세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			//파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			JSONArray mbshIds = (JSONArray) object.get("selectCtMbshIds");
			String mbshCtId = (String) object.get("mbshCtId");
			QueryQueue queryQueue = new QueryQueue();
			
			CtEstbBVo ctEstbBVo = new CtEstbBVo();
			ctEstbBVo.setCtId(mbshCtId);
			ctEstbBVo.setLangTyp(langTypCd);
			ctEstbBVo = (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
			
			List<String> ctWidrMbshEmailList = new ArrayList<String>();
			
			for(Object storeMbshId : mbshIds){
				CtMbshDVo ctMbshDVo = new CtMbshDVo();
				ctMbshDVo.setCtId(mbshCtId);
				ctMbshDVo.setUserUid(storeMbshId.toString());
				
				queryQueue.delete(ctMbshDVo);
				
				//강제 탈퇴 되어지는 회원 Email
				/*OrUserPinfoDVo orUserPinfoDVo = new OrUserPinfoDVo();
				orUserPinfoDVo.setOdurUid(storeMbshId.toString());
				orUserPinfoDVo = commonEmailSvc.getOrUserPinfoDVo(orUserPinfoDVo);
				String from_email = orUserPinfoDVo.getEmail();*/
				ctWidrMbshEmailList.add(storeMbshId.toString());
			}
			
			commonDao.execute(queryQueue);
			
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			if(sysPlocMap!=null && "Y".equals(sysPlocMap.get("mailEnable"))){
				String[] toUids = new String[ctWidrMbshEmailList.size()];
				for(int i=0; i < ctWidrMbshEmailList.size(); i++){
					toUids[i] = ctWidrMbshEmailList.get(i);
				}
				
				String subject = "["+messageProperties.getMessage("ct.cols.ctNm", request)+"] '"+ctEstbBVo.getCtNm()+"' "+messageProperties.getMessage("ct.msg.ctWidr", request);
				
				String p1 = "<p>";
				String span1 = "<span style='font-weight: bold;'>";
				String span2 = "</span>";
				String p2 = "</p>";
				
				// 이메일 내용 생성
				StringBuilder sb = new StringBuilder();
				sb.append(p1).append(span1).append(messageProperties.getMessage("ct.msg.widrAlert", request));
				sb.append(p1).append(span1).append(span2).append("&nbsp;").append(p2);
				sb.append(p1).append(span1).append(messageProperties.getMessage("ct.cols.ctNm", request)).append(span2).append(" : ").append(ctEstbBVo.getCtNm()).append(p2); // 커뮤니티 명
				sb.append(p1).append(span1).append(messageProperties.getMessage("ct.cols.mastNm", request)).append(span2).append(" : ").append(ctEstbBVo.getMastNm()).append(p2); // 마스터 명
				sb.append(p1).append(span1).append(messageProperties.getMessage("ct.cols.widrDt", request)).append(span2).append(" : ").append(ctCmntSvc.currentYmdDay()).append(p2); // 탈퇴 일시
				sb.append(p1).append(span1).append(span2).append("&nbsp;").append(p2);
				sb.append(p1).append(span1).append(messageProperties.getMessage("ct.msg.ask", request));
			
					
				//메일 발송
				emailSvc.sendMailSvc(userVo.getUserUid(), toUids, subject, sb.toString(), null, true,true, langTypCd);
			}
			
			model.put("message", messageProperties.getMessage("ct.msg.mbshWidr.success", request));
			model.put("result", "ok");
		
		}
		
		catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** 커뮤니티 회원 임의 탈퇴 Vo, POP */
	@RequestMapping(value = "/ct/mng/transMbshDel")
	public String transMbshDel(HttpServletRequest request, 
			@RequestParam(value="data", required = true) String data,
			ModelMap model){
		
		try{
			//파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String mbshCtId = (String) object.get("mbshCtId");
			String mbshId = (String) object.get("mbshId");
			
			CtMbshDVo ctMbshDVo = new CtMbshDVo();
			ctMbshDVo.setCtId(mbshCtId);
			ctMbshDVo.setUserUid(mbshId);
			
			commonDao.delete(ctMbshDVo);
			
			model.put("message", messageProperties.getMessage("ct.msg.mbshWidr.success", request));
			model.put("result", "ok");
		
		}
		//catch (CmException e) {
		//	model.put("message", e.getMessage());
		//}
		
		catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** 커뮤니티 회원 권한 수정 POP */
	@RequestMapping(value= "/ct/mng/transMbshAuthMod")
	public String transMbshAuthMod(HttpServletRequest request, 
			@RequestParam(value="data", required = true) String data,
			ModelMap model){
		
		try{
			//파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String mbshCtId = (String) object.get("mbshCtId");
			String mbshId = (String) object.get("mbshId");
			String mbshAuth = (String) object.get("mbshAuth");
			
			CtMbshDVo ctMbshDVo = new CtMbshDVo();
			ctMbshDVo.setCtId(mbshCtId);
			ctMbshDVo.setUserUid(mbshId);
			ctMbshDVo.setUserSeculCd(mbshAuth);
			
			commonDao.update(ctMbshDVo);
			
			model.put("message", messageProperties.getMessage("ct.msg.mbshAuthMod.success", request));
			model.put("result", "ok");
		
		}
		//catch (CmException e) {
		//	model.put("message", e.getMessage());
		//}
		
		catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	
	/** 커뮤니티 회원 정보 POP */
	@RequestMapping(value = "/ct/mng/setMngMbshPop")
	public String setMngMbshPop(HttpServletRequest request, ModelMap model)throws Exception{
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		//회원ID(사용자UID, get방식)
		String mbshId = request.getParameter("mbshId");
		String mbshCtId = request.getParameter("ctId");
		
		CtMbshDVo ctMbshDVo = new CtMbshDVo();
		//ctMbshDVo.setCompId(userVo.getCompId());
		ctMbshDVo.setUserUid(mbshId);
		ctMbshDVo.setCtId(mbshCtId);
		ctMbshDVo.setLangTyp(langTypCd);
		
		ctMbshDVo = (CtMbshDVo) commonDao.queryVo(ctMbshDVo);
		
		String mbshCompNm = ctMbshDVo.getCompNm();
		String mbshUserSeculCd = ctMbshDVo.getUserSeculCd();
		
		Map<String, Object> mbshInfoMap = orCmSvc.getUserMap(mbshId, langTypCd);
		model.put("mbshInfoMap", mbshInfoMap);
		model.put("mbshCompNm", mbshCompNm);
		model.put("mbshCtId", mbshCtId);
		model.put("mbshUserSeculCd", mbshUserSeculCd);
		return LayoutUtil.getJspPath("/ct/mng/setMngMbshPop");
	}
	
	/** 커뮤니티 회원정보 변경페이지 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/ct/mng/listMngMbsh")
	public String listMngMbsh(HttpServletRequest request, ModelMap model)throws Exception{
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		
		//커뮤니티 ID(get방식)
		String ctId = request.getParameter("ctId");
		ctCmntSvc.putLeftMenu(request, ctId, model);
		
		CtMbshDVo ctMbshDVo = new CtMbshDVo();
		//ctMbshDVo.setCompId(userVo.getCompId());
		ctMbshDVo.setCtId(ctId);
		ctMbshDVo.setLangTyp(langTypCd);
		/*=============================================
		 joinStat(가입상태)
		 1.가입대기중    2.가입불가    3.가입확정
		//=============================================*/
		ctMbshDVo.setJoinStat("3");
		
		//현재 가입되어 있는 회원 목록
		List<CtMbshDVo> ctMbshList = (List<CtMbshDVo>) commonDao.queryList(ctMbshDVo);
		//강제 가입시킬 회원 목록
		List<String> ctJoinList = new ArrayList<String>();
		QueryQueue queryQueue = new QueryQueue();
		
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		ctEstbBVo.setLangTyp(langTypCd);
		ctEstbBVo = (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		
		if(request.getParameter("fnc") != null && request.getParameter("fnc") != ""){
			
			String[] userUidArr = request.getParameterValues("userUid");
			if(request.getParameter("fnc").equals("join")){
				for(int i=0; i < userUidArr.length; i++){
					CtMbshDVo ctMbshJoinDVo = new CtMbshDVo();
					ctMbshJoinDVo.setCompId(ctEstbBVo.getCompId());
					ctMbshJoinDVo.setCtId(ctEstbBVo.getCtId());
					ctMbshJoinDVo.setUserUid(userUidArr[i]);
					ctMbshJoinDVo = (CtMbshDVo) commonDao.queryVo(ctMbshJoinDVo);
					if(ctMbshJoinDVo == null){
						//강제 회원가입
						CtMbshDVo ctMbshJoinVo = new CtMbshDVo();
						ctMbshJoinVo.setCompId(ctEstbBVo.getCompId());
						ctMbshJoinVo.setCtId(ctEstbBVo.getCtId());
						ctMbshJoinVo.setUserUid(userUidArr[i]);
						ctMbshJoinVo.setUserSeculCd(ctEstbBVo.getDftAuth());
						ctMbshJoinVo.setJoinDt(ctCmntSvc.currentDay());
						ctMbshJoinVo.setJoinStat("3");
						queryQueue.insert(ctMbshJoinVo);
						
						//커뮤니티 마스터 이메일
						ctJoinList.add(userUidArr[i]);
					}
				}
			}
			if(queryQueue.size() != 0){
				commonDao.execute(queryQueue);
			}
			
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			
			if(sysPlocMap!=null && "Y".equals(sysPlocMap.get("mailEnable"))){
				String[] toUids = new String[ctJoinList.size()];
				for(int i=0; i < ctJoinList.size(); i++){
					toUids[i] = ctJoinList.get(i);
				}
				
				String subject =  "["+messageProperties.getMessage("ct.cols.ctNm", request)+"] '"+ctEstbBVo.getCtNm()+"' "+messageProperties.getMessage("ct.msg.ctJoin", request);
				
				String p1 = "<p>";
				String span1 = "<span style='font-weight: bold;'>";
				String span2 = "</span>";
				String p2 = "</p>";
				
				// 이메일 내용 생성
				StringBuilder sb = new StringBuilder();
				sb.append(p1).append(span1).append(messageProperties.getMessage("ct.msg.joinAlert", request));
				sb.append(p1).append(span1).append(span2).append("&nbsp;").append(p2);
				sb.append(p1).append(span1).append(messageProperties.getMessage("ct.cols.ctNm", request)).append(span2).append(" : ").append(ctEstbBVo.getCtNm()).append(p2); // 커뮤니티 명
				sb.append(p1).append(span1).append(messageProperties.getMessage("ct.cols.mastNm", request)).append(span2).append(" : ").append(ctEstbBVo.getMastNm()).append(p2); // 마스터 명
				sb.append(p1).append(span1).append(messageProperties.getMessage("ct.cols.joinDt", request)).append(span2).append(" : ").append(ctCmntSvc.currentYmdDay()).append(p2); // 가입 일시
				sb.append(p1).append(span1).append(span2).append("&nbsp;").append(p2);
				sb.append(p1).append(span1).append(messageProperties.getMessage("ct.msg.ask", request));
				
				//메일 발송
				emailSvc.sendMailSvc(userVo.getUserUid(), toUids, subject, sb.toString(), null, true,true, langTypCd);
				//commonEmailSvc.sendMail(from_name, from_email, to_list, subject, sb.toString(), tar, file_list, save_send_mail, save_attach_file);
			}
			
			//강제 회원 가입 후 목록 reload.
			Integer recodeCount = commonDao.count(ctMbshDVo);
			PersonalUtil.setPaging(request, ctMbshDVo, recodeCount);
			List<CtMbshDVo> ctMbshJoinList = (List<CtMbshDVo>) commonDao.queryList(ctMbshDVo);
			model.put("recodeCount", recodeCount);
			model.put("ctMbshList", ctMbshJoinList);
		}else{
			VoUtil.bind(request, ctMbshDVo);
			Integer recodeCount = commonDao.count(ctMbshDVo);
			PersonalUtil.setPaging(request, ctMbshDVo, recodeCount);
			ctMbshList = (List<CtMbshDVo>) commonDao.queryList(ctMbshDVo);
			model.put("recodeCount", recodeCount);
			model.put("ctMbshList", ctMbshList);
		}
		
		model.put("ctId", ctId);
		
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
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if("Y".equals(sysPlocMap.get("mailEnable"))){
			model.put("mailEnable", "Y");
		}
		
		// 타사 조직도 조회
		if(ctEstbBVo.getAllCompYn()!=null && "Y".equals(ctEstbBVo.getAllCompYn())){
			request.setAttribute("globalOrgChartEnable", Boolean.TRUE);
		}
				
		return LayoutUtil.getJspPath("/ct/mng/listMngMbsh");
	}
	
	/** 커뮤니티 정보(임시) */
	@RequestMapping(value = "/ct/viewCm")
	public String viewCm(HttpServletRequest request, ModelMap model) throws Exception{
		
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String ctId = request.getParameter("ctId");
		//String tpl = request.getParameter("tpl");
		String preView = request.getParameter("preView");
		
		
		//================================================================================================
		//커뮤니티 기본 정보
		//================================================================================================
		
		ctCmntSvc.putCtEstbInfo(request, model, ctId);
		
		//================================================================================================
		//커뮤니티 가입 현황 전체/오늘
		//================================================================================================
		
		ctCmntSvc.putJoinPeopleStat(request, model, ctId);
		
		//================================================================================================
		//템플릿에 따른 커뮤니티 메인 HOME 선택
		//================================================================================================
		
		String ctMainView = null;
		
		CtScrnSetupDVo ctScrnSetupDVo = new CtScrnSetupDVo();
		//ctScrnSetupDVo.setCompId(userVo.getCompId());
		ctScrnSetupDVo.setCtId(ctId);
		ctScrnSetupDVo.setLangTyp(langTypCd);
		@SuppressWarnings("unchecked")
		List<CtScrnSetupDVo> ctScrnSetupList = (List<CtScrnSetupDVo>) commonDao.queryList(ctScrnSetupDVo);
		
		//미리보기가 아니라면
		if(preView == null){
			if(ctScrnSetupList.size() != 0){
				ctMainView = ctScrnSetupList.get(0).getTplFileSubj();
				model.put("ctFncMngPtList", ctScrnSetupList);
				String tplTemp= ctScrnSetupList.get(0).getTplFileSubj();
				Map<String,String> tplMap=new HashMap<String, String>();
				if(tplTemp!=null){
					for(String tplStrTemp:tplTemp.split("\\|")){
						tplMap.put(tplStrTemp, tplStrTemp);
					}
				}
				model.put("tpl", tplMap);
				if(ctScrnSetupList.get(0).getImgFileId()!=null&&!ctScrnSetupList.get(0).getImgFileId().equals("")){
					CtFileDVo conditionFileDvo=new CtFileDVo();
					conditionFileDvo.setFileId(Integer.parseInt(ctScrnSetupList.get(0).getImgFileId()));
					conditionFileDvo=(CtFileDVo) commonDao.queryVo(conditionFileDvo);
					model.put("imgFilePath", conditionFileDvo.getSavePath());					
				}
			}else{
				
				
				
				CtFncDVo ctFncDVo =  new CtFncDVo();
				//ctFncDVo.setCompId(userVo.getCompId());
				ctFncDVo.setCtId(ctId);
				ctFncDVo.setCtMngYn("Y");
				ctFncDVo.setLangTyp(langTypCd);
				@SuppressWarnings("unchecked")
				List<CtFncDVo> ctFncMngPtList =  (List<CtFncDVo>) commonDao.queryList(ctFncDVo);
				for(CtFncDVo ctfncDvo:ctFncMngPtList){
					CtScrnSetupDVo setupDvo=new CtScrnSetupDVo();
					setupDvo.setPtUrl(ctfncDvo.getPtUrl());
					setupDvo.setCtFncNm(ctFncDVo.getCtFncNm());
					ctScrnSetupList.add(setupDvo);
				}
				Map<String,String> tplMap=new HashMap<String, String>();
				tplMap.put("1", "1");
//				tplMap.put("34", "34");
//				tplMap.put("67", "67");
				model.put("tpl", tplMap);
				model.put("ctFncMngPtList", ctScrnSetupList);
				ctMainView = "viewCm";
			}
			ctMainView="viewCm";
		}else{//미리보기 라면, 파라메터로 들어온
			ctScrnSetupList.clear();
			CtScrnSetupDVo ctScrnSetup = new CtScrnSetupDVo();	
			VoUtil.bind(request, ctScrnSetup);	
			
			String[] tplArr = request.getParameterValues("tpl");
			Map<String,String> tplMap=new HashMap<String, String>();
			for(String tplTemp:tplArr){
				tplMap.put(tplTemp, tplTemp);
			}
			
			if(ctScrnSetup.getImgFileId()!=null&&!ctScrnSetup.getImgFileId().equals("")){
				CtFileDVo conditionFileDvo=new CtFileDVo();
				conditionFileDvo.setFileId(Integer.parseInt(ctScrnSetup.getImgFileId()));
				conditionFileDvo=(CtFileDVo) commonDao.queryVo(conditionFileDvo);
				model.put("imgFilePath", conditionFileDvo.getSavePath());					
			}
			
			model.put("tpl", tplMap);
			CtFncDVo ctFncDVo =  new CtFncDVo();
			//ctFncDVo.setCompId(userVo.getCompId());
			ctFncDVo.setCtId(ctId);
			ctFncDVo.setCtMngYn("Y");
			ctFncDVo.setLangTyp(langTypCd);
			@SuppressWarnings("unchecked")
			List<CtFncDVo> ctFncMngPtList =  (List<CtFncDVo>) commonDao.queryList(ctFncDVo);
			Map<String, CtFncDVo> tempCtFncMngPtMap=new HashMap<String, CtFncDVo>();
			for(CtFncDVo ctfncDVo:ctFncMngPtList){
				tempCtFncMngPtMap.put(ctfncDVo.getCtFncUid(), ctfncDVo);
			}
					
			
			if(ctScrnSetup.getLoc3CtFncId()!=null){
				CtScrnSetupDVo tempCtScrnSetupDvo=(CtScrnSetupDVo) ctScrnSetup.clone();				
				tempCtScrnSetupDvo.setCtFncNm(tempCtFncMngPtMap.get(ctScrnSetup.getLoc3CtFncId()).getCtFncNm());
				tempCtScrnSetupDvo.setPtUrl(tempCtFncMngPtMap.get(ctScrnSetup.getLoc3CtFncId()).getPtUrl());
				ctScrnSetupList.add(tempCtScrnSetupDvo);
			}
			if(ctScrnSetup.getLoc4CtFncId()!=null){
				CtScrnSetupDVo tempCtScrnSetupDvo=(CtScrnSetupDVo) ctScrnSetup.clone();				
				tempCtScrnSetupDvo.setCtFncNm(tempCtFncMngPtMap.get(ctScrnSetup.getLoc4CtFncId()).getCtFncNm());
				tempCtScrnSetupDvo.setPtUrl(tempCtFncMngPtMap.get(ctScrnSetup.getLoc4CtFncId()).getPtUrl());
				ctScrnSetupList.add(tempCtScrnSetupDvo);
			}
			if(ctScrnSetup.getLoc5CtFncId()!=null){
				CtScrnSetupDVo tempCtScrnSetupDvo=(CtScrnSetupDVo) ctScrnSetup.clone();				
				tempCtScrnSetupDvo.setCtFncNm(tempCtFncMngPtMap.get(ctScrnSetup.getLoc5CtFncId()).getCtFncNm());
				tempCtScrnSetupDvo.setPtUrl(tempCtFncMngPtMap.get(ctScrnSetup.getLoc5CtFncId()).getPtUrl());
				ctScrnSetupList.add(tempCtScrnSetupDvo);
			}
			if(ctScrnSetup.getLoc6CtFncId()!=null){
				CtScrnSetupDVo tempCtScrnSetupDvo=(CtScrnSetupDVo) ctScrnSetup.clone();				
				tempCtScrnSetupDvo.setCtFncNm(tempCtFncMngPtMap.get(ctScrnSetup.getLoc6CtFncId()).getCtFncNm());
				tempCtScrnSetupDvo.setPtUrl(tempCtFncMngPtMap.get(ctScrnSetup.getLoc6CtFncId()).getPtUrl());
				ctScrnSetupList.add(tempCtScrnSetupDvo);
			}
			if(ctScrnSetup.getLoc7CtFncId()!=null){
				CtScrnSetupDVo tempCtScrnSetupDvo=(CtScrnSetupDVo) ctScrnSetup.clone();				
				tempCtScrnSetupDvo.setCtFncNm(tempCtFncMngPtMap.get(ctScrnSetup.getLoc7CtFncId()).getCtFncNm());
				tempCtScrnSetupDvo.setPtUrl(tempCtFncMngPtMap.get(ctScrnSetup.getLoc7CtFncId()).getPtUrl());
				ctScrnSetupList.add(tempCtScrnSetupDvo);
			}
			if(ctScrnSetup.getLoc8CtFncId()!=null){
				CtScrnSetupDVo tempCtScrnSetupDvo=(CtScrnSetupDVo) ctScrnSetup.clone();				
				tempCtScrnSetupDvo.setCtFncNm(tempCtFncMngPtMap.get(ctScrnSetup.getLoc8CtFncId()).getCtFncNm());
				tempCtScrnSetupDvo.setPtUrl(tempCtFncMngPtMap.get(ctScrnSetup.getLoc8CtFncId()).getPtUrl());
				ctScrnSetupList.add(tempCtScrnSetupDvo);
			}
			if(ctScrnSetup.getLoc9CtFncId()!=null){
				CtScrnSetupDVo tempCtScrnSetupDvo=(CtScrnSetupDVo) ctScrnSetup.clone();				
				tempCtScrnSetupDvo.setCtFncNm(tempCtFncMngPtMap.get(ctScrnSetup.getLoc9CtFncId()).getCtFncNm());
				tempCtScrnSetupDvo.setPtUrl(tempCtFncMngPtMap.get(ctScrnSetup.getLoc9CtFncId()).getPtUrl());
				ctScrnSetupList.add(tempCtScrnSetupDvo);
			}
			if(ctScrnSetup.getLoc10CtFncId()!=null){
				CtScrnSetupDVo tempCtScrnSetupDvo=(CtScrnSetupDVo) ctScrnSetup.clone();				
				tempCtScrnSetupDvo.setCtFncNm(tempCtFncMngPtMap.get(ctScrnSetup.getLoc10CtFncId()).getCtFncNm());
				tempCtScrnSetupDvo.setPtUrl(tempCtFncMngPtMap.get(ctScrnSetup.getLoc10CtFncId()).getPtUrl());
				ctScrnSetupList.add(tempCtScrnSetupDvo);
			}
			if(ctScrnSetup.getLoc11CtFncId()!=null){
				CtScrnSetupDVo tempCtScrnSetupDvo=(CtScrnSetupDVo) ctScrnSetup.clone();				
				tempCtScrnSetupDvo.setCtFncNm(tempCtFncMngPtMap.get(ctScrnSetup.getLoc11CtFncId()).getCtFncNm());
				tempCtScrnSetupDvo.setPtUrl(tempCtFncMngPtMap.get(ctScrnSetup.getLoc11CtFncId()).getPtUrl());
				ctScrnSetupList.add(tempCtScrnSetupDvo);
			}
			if(ctScrnSetup.getLoc12CtFncId()!=null){
				CtScrnSetupDVo tempCtScrnSetupDvo=(CtScrnSetupDVo) ctScrnSetup.clone();				
				tempCtScrnSetupDvo.setCtFncNm(tempCtFncMngPtMap.get(ctScrnSetup.getLoc12CtFncId()).getCtFncNm());
				tempCtScrnSetupDvo.setPtUrl(tempCtFncMngPtMap.get(ctScrnSetup.getLoc12CtFncId()).getPtUrl());
				ctScrnSetupList.add(tempCtScrnSetupDvo);
			}
			
			model.put("ctFncMngPtList", ctScrnSetupList);
			ctMainView="viewCmFrm";
		}
		
		
		
		//================================================================================================
		//권한 체크 (강제로 주소 입력시 접근 권한)
		//================================================================================================
		
		if(!ctCmntSvc.chkJoinCt2(userVo, model, request, ctId)){
			return LayoutUtil.getResultJsp();
		}
		
		//왼쪽 메뉴 
		ctCmntSvc.putLeftMenu(request, ctId, model);
		
		//방문기록
		ctCmntSvc.setVistHst(userVo, ctId, "H" , null);
		
		//미리보기
		model.put("preView", preView);
		
		return LayoutUtil.getJspPath("/ct/"+ctMainView);
	}
	
	
	/** 폐쇄 승인/거부 */
	@RequestMapping(value = "/ct/adm/setCloseAppr")
	public String setCloseAppr(HttpServletRequest request, 
			@RequestParam(value="data", required = true) String data,
			ModelMap model){
		try{
			
			//세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			//세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			
			//파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			JSONArray ctIds = (JSONArray) object.get("selectCloseCtIds");
			String signalVal = (String) object.get("signal");
			String closeOp = (String) object.get("closeOp");
			QueryQueue queryQueue = new QueryQueue();
			
			for(Object selectCloseCtId: ctIds){
				CtEstbBVo ctCloseVo = new CtEstbBVo();
				ctCloseVo.setCtId(selectCloseCtId.toString());
				ctCloseVo.setLangTyp(langTypCd);
				ctCloseVo.setQueryLang(langTypCd);
				ctCloseVo = (CtEstbBVo) commonDao.queryVo(ctCloseVo);
				ctCloseVo.setRjtOpinCont(closeOp);
				
				if(signalVal.equals("Y")){
					ctCloseVo.setCtActStat("C");
					
//					//폐쇄 커뮤니티의 회원정보 삭제 
//					(폐쇄 신청 시 본인을 제외한 모든 회원은 탈퇴시켜야 하므로 나의 커뮤니티 목록에 보여주기 위해 마스터 본인은 둔다.)
//					CtMbshDVo ctMbshDVo = new CtMbshDVo();
//					ctMbshDVo.setCtId(ctCloseVo.getCtId());
//					queryQueue.delete(ctMbshDVo);
				}else{
					ctCloseVo.setClsReqsYn("Y");
					ctCloseVo.setCtStat("A");
				}
				//폐쇄 및 사유 업데이트
				queryQueue.update(ctCloseVo);
				
				if(sysPlocMap!=null && "Y".equals(sysPlocMap.get("mailEnable"))){
					//커뮤니티 마스터 이메일
					String[] toUids = new String[1]; toUids[0] = ctCloseVo.getMastUid();
					String subject = "";
					
					if(signalVal.equals("Y")){
						subject =  "["+messageProperties.getMessage("ct.cols.ctNm", request)+"] '"+ctCloseVo.getCtNm()+"' "+messageProperties.getMessage("ct.msg.ctCloseAppr", request);
					}else{
						subject =  "["+messageProperties.getMessage("ct.cols.ctNm", request)+"] '"+ctCloseVo.getCtNm()+"' "+messageProperties.getMessage("ct.msg.ctCloseRjt", request);
					}
					String p1 = "<p>";
					String span1 = "<span style='font-weight: bold;'>";
					String span2 = "</span>";
					String p2 = "</p>";
					
					// 이메일 내용 생성
					StringBuilder sb = new StringBuilder();
					sb.append(p1).append(span1).append(messageProperties.getMessage("ct.cols.ctNm", request)).append(span2).append(" : ").append(ctCloseVo.getCtNm()).append(p2); // 커뮤니티 명
					sb.append(p1).append(span1).append(messageProperties.getMessage("ct.cols.mastNm", request)).append(span2).append(" : ").append(ctCloseVo.getMastNm()).append(p2); // 마스터 명
					sb.append(p1).append(span1).append(messageProperties.getMessage("ct.cols.opin", request)).append(span2).append(" : ").append(closeOp).append(p2); // 사유
					
					//메일 발송
					emailSvc.sendMailSvc(userVo.getUserUid(), toUids, subject, sb.toString(), null, true,true, langTypCd);
					//commonEmailSvc.sendMail(from_name, from_email, to_list, subject, sb.toString(), tar, file_list, save_send_mail, save_attach_file); 
				}
			}
			
			commonDao.execute(queryQueue);
			
			if(signalVal.equals("Y")){
				model.put("message", messageProperties.getMessage("ct.msg.close.success", request));
			}else{
				model.put("message", messageProperties.getMessage("ct.msg.closeRjt.success", request));
			}
			
			model.put("result", "ok");
			
		}
		catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		return JsonUtil.returnJson(model);
		
	}
	
	/** 폐쇄관리 */
	@RequestMapping(value = "/ct/adm/listCloseReqs")
	public String listCloseReqs(HttpServletRequest request, ModelMap model)throws Exception{
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		VoUtil.bind(request, ctEstbBVo);
		ctEstbBVo.setCompId(userVo.getCompId());
		ctEstbBVo.setQueryLang(langTypCd);
		ctEstbBVo.setLangTyp(langTypCd);
		ctEstbBVo.setCtStat("C");
		ctEstbBVo.setCtActStat("A");
		Integer recodeCount = commonDao.count(ctEstbBVo);
		PersonalUtil.setPaging(request, ctEstbBVo, recodeCount);
		@SuppressWarnings("unchecked")
		List<CtEstbBVo> closeCtList = (List<CtEstbBVo>) commonDao.queryList(ctEstbBVo);
		
		model.put("recodeCount", recodeCount);
		model.put("closeCtList", closeCtList);
		
		return LayoutUtil.getJspPath("/ct/adm/listCloseReqs");
		
	}
	
	/** 반려사유 저장 */
	@RequestMapping(value= "/ct/adm/transSetRjtOpContSave")
	public String transSetRjtOpContSave(HttpServletRequest request, ModelMap model)throws Exception{
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		//반려 사유
		String rjtOpCont = request.getParameter("rjtOpCont");
		
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		VoUtil.bind(request, ctEstbBVo);
		ctEstbBVo.setCtId(request.getParameter("rjtCtId"));
		ctEstbBVo.setLangTyp(langTypCd);
		ctEstbBVo.setQueryLang(langTypCd);
		ctEstbBVo = (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		ctEstbBVo.setRjtOpinCont(rjtOpCont);
		ctEstbBVo.setCtStat("R");
		commonDao.update(ctEstbBVo);
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		
		if(sysPlocMap!=null && "Y".equals(sysPlocMap.get("mailEnable"))){
			//받는사람
			String[] toUids = new String[1]; toUids[0] = ctEstbBVo.getMastUid();
			String subject = "["+messageProperties.getMessage("ct.cols.ctNm", request)+"] '"+ctEstbBVo.getCtNm()+"' "+messageProperties.getMessage("ct.msg.ctEstbRjt", request);
			
			// 이메일 내용 생성
			StringBuilder sb = new StringBuilder();
			
			String p1 = "<p>";
			String span1 = "<span style='font-weight: bold;'>";
			String span2 = "</span>";
			String p2 = "</p>";
			
			sb.append(p1).append(span1).append(messageProperties.getMessage("ct.msg.ctEstbRjtAlert", request));
			sb.append(p1).append(span1).append(span2).append("&nbsp;").append(p2);
			sb.append(p1).append(span1).append(messageProperties.getMessage("ct.cols.ctNm", request)).append(span2).append(" : ").append(ctEstbBVo.getCtNm()).append(p2); // 커뮤니티 명
			sb.append(p1).append(span1).append(messageProperties.getMessage("ct.cols.mastNm", request)).append(span2).append(" : ").append(ctEstbBVo.getMastNm()).append(p2); // 마스터 명
			sb.append(p1).append(span1).append(messageProperties.getMessage("ct.cols.rjtDt", request)).append(span2).append(" : ").append(ctCmntSvc.currentYmdDay()).append(p2); // 반려 일시
			sb.append(p1).append(span1).append(messageProperties.getMessage("ct.cols.opin", request)).append(span2).append(" : ").append(rjtOpCont).append(p2); // 반려 사유
			
			//메일 발송
			emailSvc.sendMailSvc(userVo.getUserUid(), toUids, subject, sb.toString(), null, true,true, langTypCd);
			//commonEmailSvc.sendMail(from_name, from_email, to_list, subject, sb.toString(), tar, file_list, save_send_mail, save_attach_file); 
		}
		
		model.put("message", messageProperties.getMessage("ct.msg.rjt.success", request));
		model.put("todo", "parent.location.replace('/ct/adm/listEstbReqs.do?menuId="+request.getParameter("menuId")+"')");
		
		//공통 처리 페이지
		return LayoutUtil.getResultJsp();
		//return LayoutUtil.getJspPath("/ct/adm/setEstbReqsAppr");
	}
	
	/** 커뮤니티 개설 미승인에 따른 반려사유POP  */
	@RequestMapping(value= "/ct/adm/setRjtOpPop")
	public String setRjtOpPop(HttpServletRequest request, ModelMap model)throws Exception{
		//미승인 처리 된 커뮤니티 ID
		String reqsCtId = request.getParameter("ctId");
		model.put("reqsCtId", reqsCtId);
		
		return LayoutUtil.getJspPath("/ct/adm/setRjtOpPop");
	}
	
	/** 개설 승인 */
	@RequestMapping(value = "/ct/adm/setEstbReqsAppr")
	public String setEstbReqsAppr(HttpServletRequest request, 
			@RequestParam(value="data", required = true) String data,
			ModelMap model){
		
		try{
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			//파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String ctId = (String) object.get("ctId");
			String appr = (String) object.get("appr");
			String tabNo = (String) object.get("tabNo");
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			CtEstbBVo ctEstbBVo = new CtEstbBVo();
			ctEstbBVo.setCtId(ctId);
			ctEstbBVo.setLangTyp(langTypCd);
			ctEstbBVo.setQueryLang(langTypCd);
			ctEstbBVo = (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
			if(appr.equals("A")){
				ctEstbBVo.setCtStat(appr);
				ctEstbBVo.setCtActStat(appr);
				ctEstbBVo.setCtApvdDt(ctCmntSvc.currentDay());
				model.put("message", messageProperties.getMessage("ct.msg.appr.success", request));
			}else{
				ctEstbBVo.setCtApvdDt(null);
				ctEstbBVo.setCtActStat(appr);
				model.put("message", messageProperties.getMessage("ct.msg.close.success", request));
			}
			commonDao.update(ctEstbBVo);
			
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			
			if(sysPlocMap!=null && "Y".equals(sysPlocMap.get("mailEnable"))){
				//받는 사람
				String[] toUids = new String[1]; toUids[0] = ctEstbBVo.getMastUid();
				
				String subject =  "["+messageProperties.getMessage("ct.cols.ctNm", request)+"] '"+ctEstbBVo.getCtNm()+"' "+messageProperties.getMessage("ct.msg.ctEstbAppr", request);

				// 이메일 내용 생성
				StringBuilder sb = new StringBuilder();
				
				String p1 = "<p>";
				String span1 = "<span style='font-weight: bold;'>";
				String span2 = "</span>";
				String p2 = "</p>";
				
				sb.append(p1).append(span1).append(messageProperties.getMessage("ct.msg.ctEstbApprAlert", request));
				sb.append(p1).append(span1).append(span2).append("&nbsp;").append(p2);
				sb.append(p1).append(span1).append(messageProperties.getMessage("ct.cols.ctNm", request)).append(span2).append(" : ").append(ctEstbBVo.getCtNm()).append(p2); // 커뮤니티 명
				sb.append(p1).append(span1).append(messageProperties.getMessage("ct.cols.mastNm", request)).append(span2).append(" : ").append(ctEstbBVo.getMastNm()).append(p2); // 마스터 명
				sb.append(p1).append(span1).append(messageProperties.getMessage("ct.cols.apprDt", request)).append(span2).append(" : ").append(ctCmntSvc.currentYmdDay()).append(p2); // 승인 일시
				
				//메일 발송
				emailSvc.sendMailSvc(userVo.getUserUid(), toUids, subject, sb.toString(), null, true,true, langTypCd);
				//commonEmailSvc.sendMail(from_name, from_email, to_list, subject, sb.toString(), tar, file_list, save_send_mail, save_attach_file); 
			}
			
			model.put("result", "ok");
			model.put("showTab", tabNo);
		}
		//catch (CmException e) {
		//	model.put("message", e.getMessage());
		//}
		catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		return JsonUtil.returnJson(model);
	}
			
	
	/** 개설 승인 페이지(관리자) */
	@RequestMapping(value = "/ct/adm/listEstbReqs")
	public String listEstbReqs(HttpServletRequest request, ModelMap model)throws Exception{
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		VoUtil.bind(request, ctEstbBVo);
		ctEstbBVo.setCompId(userVo.getCompId());
		ctEstbBVo.setQueryLang(langTypCd);
		ctEstbBVo.setLangTyp(langTypCd);
		ctEstbBVo.setCtStat("S");
		ctEstbBVo.setCtActStat("S");
		
		// 기간 시작일 & 마감일
		String startDt = request.getParameter("strtDt");
		String endDt = request.getParameter("finDt");
		if(startDt != null && endDt != null){
			ctEstbBVo.setStrtDt(startDt);
			ctEstbBVo.setEndDt(endDt);
			model.put("startDt", startDt);
			model.put("endDt", endDt);
		}
		
		//보안등급 
		String extnOpenYn = request.getParameter("extnOpenYn");
		if(extnOpenYn != "" && extnOpenYn != null){
			ctEstbBVo.setExtnOpenYn(extnOpenYn);
			model.put("extnOpenYn", extnOpenYn);
		}
		
		//관리대상여부
		String mngTgtYn = request.getParameter("mngTgtYn");
		if(mngTgtYn != "" && mngTgtYn != null){
			ctEstbBVo.setMngTgtYn(mngTgtYn);
			model.put("mngTgtYn", mngTgtYn);
		}
		
		String tabNo = request.getParameter("tabNo");
		if(tabNo != null){
			if(tabNo.equals("1")){
				ctEstbBVo.setCtStat("R");
			}
		}
		Map<String, Object> rsltMap = ctCmntSvc.getEstbResqList(request, ctEstbBVo);
		
		model.put("reqsCtMapList", rsltMap.get("reqsCtMapList"));
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("showTab", tabNo);
		return LayoutUtil.getJspPath("/ct/adm/listEstbReqs");
		
	}
	
	/** 관리대상 여부 승인(관리자) */
	@RequestMapping(value = "/ct/adm/setMngTgtAppr")
	public String setMngTgtAppr(HttpServletRequest request, 
			@RequestParam(value="data", required = true) String data,
			ModelMap model){
		
		try{
			//파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			JSONArray ctIds = (JSONArray) object.get("selectCtMngTgtCtIds");
			String signalVal = (String) object.get("signal");
			
			//CtEstbBVo ctEstbBVo = new CtEstbBVo();
			QueryQueue queryQueue = new QueryQueue();
			
			for(Object selectMngTgtCtId: ctIds){
				CtEstbBVo ctMngTgtVo = new CtEstbBVo();
				ctMngTgtVo.setCtId(selectMngTgtCtId.toString());
				ctMngTgtVo = (CtEstbBVo) commonDao.queryVo(ctMngTgtVo);
				if(signalVal.equals("N")){
					if(ctMngTgtVo.getMngTgtYn().equals("Y")){
						ctMngTgtVo.setMngTgtYn("N");
					}else{
						ctMngTgtVo.setMngTgtYn("Y");
					}
				}
				
				ctMngTgtVo.setModifyYn("N");
				queryQueue.update(ctMngTgtVo);
			}
			commonDao.execute(queryQueue);

			if(signalVal.equals("Y")){
				model.put("message", messageProperties.getMessage("ct.msg.appr.success", request));
			}else{
				model.put("message", messageProperties.getMessage("ct.msg.rjt.success", request));
			}
			
			model.put("result", "ok");
			
		}
		//catch (CmException e) {
		//	model.put("message", e.getMessage());
		//}
		
		catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** 관리대상 커뮤니티 승인페이지(관리자) */
	@RequestMapping(value = "/ct/adm/listMngTgtApvd")
	public String listMngTgtApvd(HttpServletRequest request, ModelMap model) throws Exception{
		
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		VoUtil.bind(request, ctEstbBVo);
		ctEstbBVo.setCompId(userVo.getCompId());
		ctEstbBVo.setQueryLang(langTypCd);
		ctEstbBVo.setLangTyp(langTypCd);
		ctEstbBVo.setModifyYn("Y");
		
		Map<String, Object> rsltMap = ctCmntSvc.getMngTgtApvdList(request, ctEstbBVo);
		
		model.put("ctMngTgtMapList", rsltMap.get("ctMngTgtMapList"));
		model.put("recodeCount", rsltMap.get("recodeCount"));
		
		return LayoutUtil.getJspPath("/ct/adm/listMngTgtApvd");
	}
		
		
			
	
	/** 커뮤니티 가입 */
	@RequestMapping(value = "/ct/setCmntJoin")
	public String setCmntJoin(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		
		try{
			
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String ctId  = (String) object.get("ctId");
			if (ctId == null || ctId.equalsIgnoreCase("")) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			UserVo userVo = LoginSession.getUser(request);
			//QueryQueue queryQueue = new QueryQueue();
			
			CtMbshDVo ctMbshDVo = new CtMbshDVo();
			ctMbshDVo.setCompId(userVo.getCompId());
			ctMbshDVo.setCtId(ctId);
			ctMbshDVo.setUserUid(userVo.getUserUid());
			ctCmntSvc.setCtMbshJoin(request, ctMbshDVo);
			
			// "ct.msg.join.success" 가입 신청이 완료되었습니다.
			model.put("message", messageProperties.getMessage("ct.msg.join.success", request));
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		
		return JsonUtil.returnJson(model);
		
		
	}
	
	/** 커뮤니티 가입 페이지*/
	@RequestMapping(value = "/ct/setCmJoin")
	public String setCmJoin(HttpServletRequest request, ModelMap model) throws Exception{
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		CtEstbBVo joinCtEstbBVo = new CtEstbBVo();
		joinCtEstbBVo.setCompId(userVo.getCompId());
		joinCtEstbBVo.setCtId(request.getParameter("ctId"));
		joinCtEstbBVo.setLangTyp(langTypCd);
		joinCtEstbBVo = (CtEstbBVo) commonDao.queryVo(joinCtEstbBVo);
		
		model.put("joinCtEstbBVo", joinCtEstbBVo);
		model.put("signal", request.getParameter("signal"));
		model.put("catId", request.getParameter("catId"));
		return LayoutUtil.getJspPath("/ct/setCmJoin");
		
		
	}
	
	
	/** 관리대상 목록 */
	@RequestMapping(value = "/ct/listCmMngTgt")
	public String listCmMngTgt(HttpServletRequest request, ModelMap model) throws Exception{
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		//조회조건 매핑
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		VoUtil.bind(request, ctEstbBVo);
		ctEstbBVo.setCompId(userVo.getCompId());
		ctEstbBVo.setQueryLang(langTypCd);
		ctEstbBVo.setLangTyp(langTypCd);
		//ctEstbBVo.setMngTgtYn("Y");
		ctEstbBVo.setWhereSqllet("AND ( (CEB_T.MNG_TGT_YN = 'Y' AND CEB_T.MODIFY_YN = 'N') OR (CEB_T.MNG_TGT_YN = 'N' AND CEB_T.MODIFY_YN = 'Y'))");
		ctEstbBVo.setCtActStat("A");
		Map<String, Object>	 rsltMap = ctCmntSvc.getCmMngTgtList(request, ctEstbBVo);
		
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("ctMngTgtMapList", rsltMap.get("ctMngTgtMapList"));
		
		return LayoutUtil.getJspPath("/ct/listCmMngTgt");
		
	}
	
	/** 해당 카테고리에 포함된 커뮤니티 */
	@RequestMapping(value = "/ct/listCm")
	public String listCm(HttpServletRequest request, ModelMap model) throws Exception{
		//조회조건 매핑
		CtCatBVo ctCatBVo = new CtCatBVo();
		ctCatBVo.setExtnOpenYn("Y");
		VoUtil.bind(request, ctCatBVo);
		Map<String, Object> rsltMap = ctCmntSvc.getCatCmntList(request, ctCatBVo);
		ctCatBVo.setCatId(request.getParameter("catId"));
		
		model.put("catCmntList", rsltMap.get("catCmntList"));
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("catId", request.getParameter("catId"));
		
		return LayoutUtil.getJspPath("/ct/listCm");
	}
	
	/** 전체 커뮤니티 */
	@RequestMapping(value = "/ct/listAllCm")
	public String listAllCm(HttpServletRequest request, ModelMap model) throws Exception{
		//조회조건 매핑
		CtCatBVo ctCatBVo = new CtCatBVo();
		ctCatBVo.setExtnOpenYn("Y");
		VoUtil.bind(request, ctCatBVo);
		Map<String, Object> rsltMap = ctCmntSvc.getAllCtCatList(request, ctCatBVo);
	
		model.put("catLength", rsltMap.get("catLength"));
		model.put("allCtCatFldList", rsltMap.get("ctCatFldList"));
		model.put("allCtCatClsList", rsltMap.get("catClsList"));
		
		return LayoutUtil.getJspPath("/ct/listAllCm");
		
		
	}
	
	/** 나의 커뮤니티 */
	@RequestMapping(value= "/ct/listMyCm")
	public String listMyCm(HttpServletRequest request, ModelMap model) throws Exception{
		
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		//조회조건 매핑
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		VoUtil.bind(request, ctEstbBVo);
		//ctEstbBVo.setCompId(userVo.getCompId());
		ctEstbBVo.setLogUserUid(userVo.getUserUid());
		ctEstbBVo.setQueryLang(langTypCd);
		ctEstbBVo.setLangTyp(langTypCd);
		ctEstbBVo.setSignal("my");
		
		/* ======================================
		 * ctActStat 커뮤니티 활동 상태
		 * S : 신청중 / A : 활동중 / C : 폐쇄
		   ======================================*/
		List<String> ctActList = new ArrayList<String>();
		ctActList.add("A");
		if(ctEstbBVo.getSchClose() != null && ctEstbBVo.getSchClose().equals("Y")){
			ctActList.add("C");
		}
		ctEstbBVo.setCtActStatList(ctActList);
		List<String> joinStateList = new ArrayList<String>();
		joinStateList.add("1");
		joinStateList.add("3");		
		ctEstbBVo.setJoinStatList(joinStateList);
		Map<String, Object> rsltMap = ctCmntSvc.getMyCtMapList(request, ctEstbBVo);
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("myCtMapList", rsltMap.get("myCtMapList"));
		model.put("logUserUid", ctEstbBVo.getLogUserUid());
		
		return LayoutUtil.getJspPath("/ct/listMyCm");
		
	}
	
	/** 신청중인 커뮤니티 */
	@RequestMapping(value= "/ct/listCmReqs")
	public String listCmReqs(HttpServletRequest request, ModelMap model) throws Exception{
		
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		//조회조건 매핑
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		QueryQueue queryQueue = new QueryQueue();
		
		//신청중인 커뮤니티 삭제
		String signalVal = request.getParameter("signal");
		if(signalVal != null){
			if(signalVal.equals("del")){
				ctCmntSvc.transReqsCtDel(request, ctEstbBVo, queryQueue);
			}
		}
		
		VoUtil.bind(request, ctEstbBVo);
		
		ctEstbBVo.setCompId(userVo.getCompId());
		ctEstbBVo.setRegrUid(userVo.getUserUid());
		/*========================================
		 * ctSearchList = 커뮤니티 상태 리스트
		 * 승인요청(S), 승인(A), 반려(R)
		 ========================================*/
		List<String> ctSearchList = new ArrayList<String>();
		ctSearchList.add("S");
		ctSearchList.add("R");
		ctEstbBVo.setCtSearchList(ctSearchList);
		ctEstbBVo.setQueryLang(langTypCd);
		ctEstbBVo.setLangTyp(langTypCd);
		Map<String, Object> rsltMap = ctCmntSvc.getReqsCtMapList(request, ctEstbBVo);
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("reqsCtMapList", rsltMap.get("reqsCtMapList"));
		model.put("logUserUid", ctEstbBVo.getLogUserUid());

		return LayoutUtil.getJspPath("/ct/listCmReqs");
		
	}
	
	/** 커뮤니티 개설 정책 페이지 */
	@RequestMapping(value= "/ct/adm/setEstbPolc")
	public String setEstbPolc(HttpServletRequest request, ModelMap model) throws Exception {
		
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		CtPolcDVo ctPolcDVo = new CtPolcDVo();
		ctPolcDVo.setCompId(userVo.getCompId());
		ctPolcDVo.setQueryLang(langTypCd);
		
		ctPolcDVo = (CtPolcDVo) commonDao.queryVo(ctPolcDVo); 
		
		String apvdY = null;
		String apvdN = null;
		
		if(ctPolcDVo != null){
			if(ctPolcDVo.getApvdYn().equals("Y")){
				apvdY = "true";
				apvdN = "false";
			}else if(ctPolcDVo.getApvdYn().equals("N")){
				apvdY = "false";
				apvdN = "true";
			}
			
		}else{
			apvdN = "true";
		}
		model.put("apvdY", apvdY);
		model.put("apvdN", apvdN);
		
		return LayoutUtil.getJspPath("/ct/adm/setEstbPolc");
	}
	
	/** 커뮤니티 개설 정책 등록 */
	@RequestMapping(value= "/ct/adm/transSetEstbPolcSave")
	public String transSetEstbPolcSave(HttpServletRequest request, ModelMap model) throws Exception {
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String estbPolc = request.getParameter("estbPolc");
		
		CtPolcDVo ctPolcDVo = new CtPolcDVo();
		ctPolcDVo.setCompId(userVo.getCompId());
		ctPolcDVo.setQueryLang(langTypCd);
		
		// 시스템 관리자 여부
		boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		// 시스템 관리자가 아닌 경우에는 - 회사ID 추가
		if(!isSysAdmin){
			ctPolcDVo.setCompId(userVo.getCompId());
		}
				
		ctPolcDVo = (CtPolcDVo) commonDao.queryVo(ctPolcDVo); 
				
		QueryQueue queryQueue = new QueryQueue();
		if(ctPolcDVo == null){
			CtPolcDVo newCtPolcDVo = new CtPolcDVo();
			
			newCtPolcDVo.setCompId(userVo.getCompId());
			newCtPolcDVo.setQueryLang(langTypCd);
			
			newCtPolcDVo.setRegrUid(userVo.getUserUid());
			newCtPolcDVo.setRegDt(ctCmntSvc.currentDay());
			
			newCtPolcDVo.setModrUid(userVo.getUserUid());
			newCtPolcDVo.setModDt(ctCmntSvc.currentDay());
			
			newCtPolcDVo.setApvdYn(estbPolc);
			
			queryQueue.insert(newCtPolcDVo);
			
		}else{
			ctPolcDVo.setApvdYn(estbPolc);
			ctPolcDVo.setModrUid(userVo.getUserUid());
			ctPolcDVo.setModDt(ctCmntSvc.currentDay());
			
			queryQueue.update(ctPolcDVo);
		}
		
		
		commonDao.execute(queryQueue);
		
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		model.put("todo", "parent.location.replace('/ct/adm/setEstbPolc.do?menuId="+request.getParameter("menuId")
				+"');");
		
		//공통 처리 페이지
		return LayoutUtil.getResultJsp();
	}
	

	/** 커뮤니티 기능관리 */
	@RequestMapping(value = "/ct/adm/listFnc")
	public String listFnc(HttpServletRequest request, ModelMap model) throws Exception{
		
		//세션언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		CtFncMngBVo ctFncMngVo = new CtFncMngBVo();
		ctFncMngVo.setLangTyp(langTypCd);
		
		// 기본기능
		ctFncMngVo.setCtMngYn("N");
		@SuppressWarnings("unchecked")
		List<CtFncMngBVo> ctFncMngList1 = (List<CtFncMngBVo>) commonDao.queryList(ctFncMngVo);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 시스템 관리자 여부
		//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		// 시스템 관리자가 아닌 경우에는 - 회사ID 추가
		//if(!isSysAdmin){
			ctFncMngVo.setCompId(userVo.getCompId());
		//}
				
		// 추가기능
		ctFncMngVo.setCtMngYn("Y");
		Integer recodeCount = commonDao.count(ctFncMngVo);
		PersonalUtil.setPaging(request, ctFncMngVo, recodeCount);
		@SuppressWarnings("unchecked")
		List<CtFncMngBVo> ctFncMngList2 = (List<CtFncMngBVo>) commonDao.queryList(ctFncMngVo);
		
		model.put("ctFncMngList1", ctFncMngList1);
		model.put("recodeCount", recodeCount);
		model.put("ctFncMngList2", ctFncMngList2);
		//페이징 기능 구현 해야함
	
		return LayoutUtil.getJspPath("/ct/adm/listFnc");
	}
	
	/** 커뮤니티 기능(adm) 수정용 조회 및 등록 화면 */
	@RequestMapping(value = "/ct/adm/setFncPop")
	public String setFncPop(HttpServletRequest request, ModelMap model) throws Exception{
		
		String ctFncId =null; 
		
		String fnc = request.getParameter("fnc");
		if("mod".equals(fnc)){
			ctFncId = request.getParameter("ctFncId");
			
			CtFncMngBVo tempCtFncMngBVo = new CtFncMngBVo();
			tempCtFncMngBVo.setCtFncId(ctFncId);
			tempCtFncMngBVo = (CtFncMngBVo) commonSvc.queryVo(tempCtFncMngBVo);
			
			if(tempCtFncMngBVo != null){
				ctRescSvc.queryRescBVo(tempCtFncMngBVo.getCtFncSubjRescId(), model);
				model.put("ctFncMngBVo", tempCtFncMngBVo);
			}
		}
		
		model.put("fnc", fnc);
		model.put("ctFncId", ctFncId);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
					
		// 회사목록
		List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(null, "Y", langTypCd);
		model.put("ptCompBVoList", ptCompBVoList);
				
		return LayoutUtil.getJspPath("/ct/adm/setFncPop");
	}
	
	/** 커뮤니티 기능관리 저장 */
	@RequestMapping(value = "/ct/adm/transFncSave")
	public String transFncSave(HttpServletRequest request,
			@Parameter(name="ctFncId", required=false) String ctFncId,
//			@Parameter(name="svcMnuYn", required=false) String svcMnuYn,
			@Parameter(name="url", required=false) String url,
			@Parameter(name="ptUrl", required=false) String ptUrl,
			@Parameter(name="dftYn", required=false) String dftYn,
			@Parameter(name="mulChoiYn", required=false) String mulChoiYn,
			@Parameter(name="useYn", required=false) String useYn,
			@Parameter(name="relTblSubj", required=false) String relTblSubj,
			@Parameter(name="fnc", required=false) String fnc,
			@Parameter(name="compId", required=false) String compId,
			ModelMap model) throws Exception {
		
		if(fnc==null || fnc.isEmpty() ){
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			LOGGER.error("fnc reg,mod flag Fail  msg:"+msg);
			model.put("message", msg);
			return LayoutUtil.getResultJsp();
		}
		
		try{
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 기능관리 기본 테이블
			CtFncMngBVo ctFncMngBVo = new CtFncMngBVo();
//			VoUtil.bind(request, ctcatFldBVo);
			
			String fncSubj = "fncSubj";
			
			if("mod".equals(fnc)) fncSubj ="";
			
			
			QueryQueue queryQueue = new QueryQueue();
			//======================================================================================================
			//다국어 처리
			//======================================================================================================
			// 회사별 설정된 리소스의 어권 정보
			List<PtCdBVo> langTypCdList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
			CtRescBVo ctRescBVo;
			
			// 부서장 직위 - 리소스 테이블용 리소스 데이터 모으고, 리소스ID 세팅
			ctRescBVo = ctRescSvc.collectCtRescBVo(request, queryQueue, fncSubj, langTypCdList);
			if(ctRescBVo != null){
				ctFncMngBVo.setCtFncSubjRescId(ctRescBVo.getRescId());
			}
			//======================================================================================================			
			
			//기능관리 구분 무조건 "기능" "서비스"에 구분에 대해서 상관 없음ㅁ
			ctFncMngBVo.setCtFncTyp("N");
			//기능관리 URL
			if(url != null) ctFncMngBVo.setUrl(url);
			//기능관리 PTURL
			if(ptUrl != null) ctFncMngBVo.setPtUrl(ptUrl);
			//기능관리 기본여부
			if(dftYn != null) ctFncMngBVo.setDftYn(dftYn);
			//기능관리 멀티선택여부
			if(mulChoiYn != null) ctFncMngBVo.setMulChoiYn(mulChoiYn);
			//기능관리 멀티선택여부
			if(useYn != null) ctFncMngBVo.setUseYn(useYn);
			//관계테이블
			if(relTblSubj != null) ctFncMngBVo.setRelTblSubj(relTblSubj);
			
			//SYSTEM 기능은 N으로 기본설정 되어있어서 
			//생성되는 기능들은 모두 Y
			if(ctFncId.startsWith(PtConstant.MNU_GRP_REF_CT))
				ctFncMngBVo.setCtMngYn("N");
			else
				ctFncMngBVo.setCtMngYn("Y");
			
			// 시스템 관리자 여부
			//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
			ctFncMngBVo.setCompId(userVo.getCompId());
			// 시스템 관리자가 아닌 경우에는 - 회사ID 추가
			/*if(isSysAdmin){
				if(compId != null && !compId.isEmpty()){
					ctFncMngBVo.setCompId(compId);
				}
			}else{
				ctFncMngBVo.setCompId(userVo.getCompId());
			}*/
			
			if("reg".equals(fnc)){
				// ID 생성
				ctFncMngBVo.setCtFncId(ctCmSvc.createId("CT_FNC_MNG_B"));
				//테이블 - INSERT
				queryQueue.insert(ctFncMngBVo);
			} else {
				if(ctFncId != null && !ctFncId.isEmpty()){
					ctFncMngBVo.setCtFncId(ctFncId);
				}
				//테이블 - UPDATE
				queryQueue.update(ctFncMngBVo);
			}
			commonSvc.execute(queryQueue);
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.location.replace('/ct/adm/listFnc.do?menuId="+request.getParameter("menuId")
					+"');");
			
			
//		} catch(CmException e){
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] 커뮤니티명 중복 확인 */
	@RequestMapping(value = "/ct/transCtSubjRescNmChk")
	public String transCtSubjRescNmChk(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		try {
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			
			JSONArray subjArr = (JSONArray) jsonObject.get("subjArr");
			JSONArray subjLangCd = (JSONArray) jsonObject.get("subjLangCd");
			String rescId  = (String) jsonObject.get("rescId");
			
			if(subjArr==null || subjArr.isEmpty() ){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				LOGGER.error("save cd[folder] - subjArr:"+subjArr+"  msg:"+msg);
				model.put("message", msg);
				return LayoutUtil.getResultJsp();
			}
			int subjNmCount=0;
			int returnCount=0;
			String subjCd = "";
			
			for(int i=0; i < subjArr.size(); i++){
				CtRescBVo ctRescBVo = new CtRescBVo();
				if(rescId != null) ctRescBVo.setRescId(rescId);
				ctRescBVo.setRescVa((String)subjArr.get(i));
				ctRescBVo.setCtSubjNmChkYn("Y");
				returnCount = commonDao.count(ctRescBVo);
				if(returnCount > 0){
					subjCd += subjLangCd.get(i) + ",";
					subjNmCount += 1;
				}
				
			}
				
			if(subjNmCount == 0){
				//ct.msg.ctSubjNm.chk.success = 커뮤니티명이 중복되지 않습니다.
				model.put("message", messageProperties.getMessage("ct.msg.ctSubjNm.chk.success", request));
				model.put("result", "ok");
			}else{
				//ct.msg.ctSubjNm.chk.fail = 커뮤니티명이 중복 되었습니다.\n 다시확인 하십시오.
				model.put("message","[ " + subjCd.substring(0, subjCd.length()-1 ) + " ] " +  messageProperties.getMessage("ct.msg.ctSubjNm.chk.fail", request));
				model.put("result", "");
			}
			
			
		
			
			
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return JsonUtil.returnJson(model);
	}
	
	/** 커뮤니티 개설 저장 */
	@RequestMapping(value= "/ct/transSetCmFncNextSave")
	public String transSetCmFncNextSave(HttpServletRequest request, ModelMap model) throws Exception {
		
		UploadHandler uploadHandler = null;	
		
		try {	// Multipart 파일 업로드

			uploadHandler = ctFileSvc.upload(request);

			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			QueryQueue queryQueue = new QueryQueue();
			
			//String fnc = request.getParameter("fnc");
			String catId = request.getParameter("catId");
			String cmItro = request.getParameter("cmItro");
			String kwd = request.getParameter("kwd");
			String mngTgtYn = request.getParameter("mngTgtYn");
			String joinMet = request.getParameter("joinMet");
			String dftAuth = request.getParameter("dftAuth");
			String extnOpenYn = request.getParameter("extnOpenYn");
			String attSizeLim = request.getParameter("attSizeLim");
			String ctFncUid = request.getParameter("menuId");
			String ctId = request.getParameter("ctId");
			String allCompYn = (String)request.getParameter("allCompYn");
			
			
			int chkId = 0;
			
			if(ctId!=null && !ctId.isEmpty()){
				CtEstbBVo chkCtId = new CtEstbBVo();
				chkCtId.setCtId(ctId);
				
				//chkId  신규인지 수정인지 판단
				chkId = commonDao.count(chkCtId);
			}
			
			//커뮤니티 소속 카테고리 상세(CT_BLON_CAT_D) 테이블 에 해당 카테고리와 커뮤니티를 저장한다.
			CtBlonCatDVo ctBlonCatDVo = new CtBlonCatDVo();
			ctBlonCatDVo.setCatId(catId);
			//ctBlonCatDVo.setCompId(userVo.getCompId());
			ctBlonCatDVo.setCtId(ctId);
			
			//커뮤니티 개설 기본 (CT_ESTB_B)테이블에 저장한다.
			CtEstbBVo ctEstbBVo = new CtEstbBVo();
			//ctEstbBVo.setCompId(userVo.getCompId());
			ctEstbBVo.setCtId(ctId);
			
			ctEstbBVo.setCtItro(cmItro);
			ctEstbBVo.setCtKwd(kwd);
			ctEstbBVo.setMngTgtYn(mngTgtYn);
			ctEstbBVo.setJoinMet(joinMet);
			ctEstbBVo.setDftAuth(dftAuth);
			ctEstbBVo.setExtnOpenYn(extnOpenYn);
			if(allCompYn==null || allCompYn.isEmpty()) allCompYn="N";
			ctEstbBVo.setAllCompYn(allCompYn);
			
			float sizeLim;
			if(attSizeLim.equalsIgnoreCase("INFINITY")){
				sizeLim = -1;
			}else{
				sizeLim = Integer.parseInt(attSizeLim);
			}
			ctEstbBVo.setAttLimSize((int)(sizeLim));
			
			//파일 저장
			ctFileSvc.saveSurvFile(request, ctEstbBVo.getCtId(), queryQueue, ctFncUid, ctId);
			int fileSaveSize=queryQueue.size();
			ctEstbBVo.setAttYn(fileSaveSize>0?"Y":"N");
			
			if(0 != chkId ){
				

				ctEstbBVo.setModrUid(userVo.getUserUid());
				ctEstbBVo.setModDt(ctCmntSvc.currentDay());
				//======================================================================================================
				//다국어 처리 수정
				//======================================================================================================
				// 회사별 설정된 리소스의 어권 정보
				List<PtCdBVo> langTypCdList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
				CtRescBVo ctRescBVo;
				
				// 부서장 직위 - 리소스 테이블용 리소스 데이터 모으고, 리소스ID 세팅
				ctRescBVo = ctRescSvc.collectCtRescBVo(request, queryQueue, "", langTypCdList);
				if(ctRescBVo != null){
					ctEstbBVo.setCtSubjResc(ctRescBVo.getRescId());
				}
				//======================================================================================================
				//수정시 활동생태가(활동중)이면 관리대상 여부를 수정 하면 setModifyYn 이 'Y' 수정을 안했으면 'N' 으로 변경
				//======================================================================================================
				
				
				CtEstbBVo ctEstbMngTgt = new CtEstbBVo();
				//ctEstbMngTgt.setCompId(userVo.getCompId());
				ctEstbMngTgt.setCtId(ctId);
				
				ctEstbMngTgt = (CtEstbBVo) commonDao.queryVo(ctEstbMngTgt);
				
				if(ctEstbMngTgt != null){
					if(ctEstbMngTgt.getCtActStat().equalsIgnoreCase("A")){
						if(!ctEstbMngTgt.getMngTgtYn().equalsIgnoreCase(mngTgtYn)){
							ctEstbBVo.setModifyYn("Y");
						}else{
							ctEstbBVo.setModifyYn("N");
						}
					}else{
						ctEstbBVo.setModifyYn("N");
					}
				}
				//======================================================================================================
				queryQueue.update(ctBlonCatDVo);
 				queryQueue.update(ctEstbBVo);
 				
 				// 기능관리 기본 테이블
 				CtScrnSetupDVo ctScrnSetup = new CtScrnSetupDVo();
 				
 				//ctScrnSetup.setCompId(userVo.getCompId());
 				ctScrnSetup.setCtId(ctId);
 				int ctScrnChk = commonDao.count(ctScrnSetup);
 				
 				if(ctScrnChk == 0){
 					ctScrnSetup.setRegDt(ctCmntSvc.currentDay());
 					ctScrnSetup.setRegrUid(userVo.getUserUid());
 					ctScrnSetup.setModDt(ctCmntSvc.currentDay());
 					ctScrnSetup.setModrUid(userVo.getUserUid());
 					ctScrnSetup.setCtItro(ctEstbBVo.getCtItro());
 					ctScrnSetup.setTplFileSubj("1");
 					queryQueue.insert(ctScrnSetup);
 				} else {
 					ctScrnSetup.setModDt(ctCmntSvc.currentDay());
 					ctScrnSetup.setModrUid(userVo.getUserUid());
 					ctScrnSetup.setCtItro(ctEstbBVo.getCtItro());
 					
 					//테이블 - UPDATE
 					ctScrnSetup.setInstanceQueryId("updateCtScrnCtInto");
 					queryQueue.update(ctScrnSetup);
 				}
				
			}else{
				
				ctEstbBVo.setMbshCnt(1);
				//평가코드
				ctEstbBVo.setCtEvalCd("C");
				//상태코드
				ctEstbBVo.setCtStat("S");
				//미저장
				ctEstbBVo.setCtActStat("N");
				
				ctEstbBVo.setMastUid(userVo.getUserUid());
				ctEstbBVo.setRegrUid(userVo.getUserUid());
				ctEstbBVo.setRegDt(ctCmntSvc.currentDay());
				//======================================================================================================
				//다국어 처리 저장
				//======================================================================================================
				// 회사별 설정된 리소스의 어권 정보
				List<PtCdBVo> langTypCdList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
				CtRescBVo ctRescBVo;
				
				// 부서장 직위 - 리소스 테이블용 리소스 데이터 모으고, 리소스ID 세팅
				ctRescBVo = ctRescSvc.collectCtRescBVo(request, queryQueue, "ctSubj", langTypCdList);
				if(ctRescBVo != null){
					ctEstbBVo.setCtSubjResc(ctRescBVo.getRescId());
				}
				//======================================================================================================
				
				ctBlonCatDVo.setCompId(userVo.getCompId());
				ctEstbBVo.setCompId(userVo.getCompId());
				queryQueue.insert(ctBlonCatDVo);
				queryQueue.insert(ctEstbBVo);
				
				// 기능관리 기본 테이블
 				CtScrnSetupDVo ctScrnSetup = new CtScrnSetupDVo();
 				
 				//ctScrnSetup.setCompId(userVo.getCompId());
 				ctScrnSetup.setCtId(ctId);
 				int ctScrnChk = commonDao.count(ctScrnSetup);
 				
 				if(ctScrnChk == 0){
 					ctScrnSetup.setCompId(userVo.getCompId());
 					ctScrnSetup.setRegDt(ctCmntSvc.currentDay());
 					ctScrnSetup.setRegrUid(userVo.getUserUid());
 					ctScrnSetup.setModDt(ctCmntSvc.currentDay());
 					ctScrnSetup.setModrUid(userVo.getUserUid());
 					ctScrnSetup.setCtItro(ctEstbBVo.getCtItro());
 					ctScrnSetup.setTplFileSubj("1");
 					queryQueue.insert(ctScrnSetup);
 				} else {
 					ctScrnSetup.setModDt(ctCmntSvc.currentDay());
 					ctScrnSetup.setModrUid(userVo.getUserUid());
 					ctScrnSetup.setCtItro(ctEstbBVo.getCtItro());
 					//테이블 - UPDATE
 					ctScrnSetup.setInstanceQueryId("updateCtScrnCtInto");
 					queryQueue.update(ctScrnSetup);
 				}
				
			}
			
			commonDao.execute(queryQueue);
	
//			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.location.replace('/ct/setCmFnc.do?menuId="+request.getParameter("menuId")
					+ "&fnc="+request.getParameter("fnc")
					+ "&ctId=" + ctId
					+ "&catId=" + catId
					+"');");
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		} finally {
			if (uploadHandler != null) try { uploadHandler.removeTempDir(); } catch (CmException ignored) {}
		}
		
		//공통 처리 페이지
		return LayoutUtil.getResultJsp();
		
		
	}
	
	/** 커뮤니티 등록 마지막 저장*/
	@RequestMapping(value= "/ct/transFinalSave")
	public String transFinalSave(HttpServletRequest request, ModelMap model) throws Exception {
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		QueryQueue queryQueue = new QueryQueue();
		
		String  ctId = request.getParameter("ctId");
		String menuId = request.getParameter("menuId");
		String fnc = request.getParameter("fnc");
		
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		
		ctEstbBVo.setCtId(ctId);
		//ctEstbBVo.setCompId(userVo.getCompId());
		
		ctEstbBVo = (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		
		//최종 저장할때 커뮤니티 상태 와 커뮤니티 활동 상태를 저장한다.
		CtPolcDVo ctPolcDVo = new CtPolcDVo();
		//ctPolcDVo.setCompId(userVo.getCompId());
		ctPolcDVo.setCompId(ctEstbBVo.getCompId());
		ctPolcDVo = (CtPolcDVo) commonDao.queryVo(ctPolcDVo);
		
		if(!ctEstbBVo.getCtStat().equalsIgnoreCase("A") || !ctEstbBVo.getCtActStat().equalsIgnoreCase("A")){
			if(ctPolcDVo != null && ctPolcDVo.getApvdYn().equalsIgnoreCase("Y")){
				ctEstbBVo.setCtStat("S");
				ctEstbBVo.setCtActStat("S");
			}else{
				ctEstbBVo.setCtStat("A");
				ctEstbBVo.setCtActStat("A");
				ctEstbBVo.setCtApvdDt(ctCmntSvc.currentDay());
			}
			queryQueue.update(ctEstbBVo);
		}
		
		
		CtMbshDVo ctMbshChk = new CtMbshDVo();
		ctMbshChk.setCtId(ctId);
		//ctMbshChk.setCompId(userVo.getCompId());
		ctMbshChk.setCompId(ctEstbBVo.getCompId());
		ctMbshChk.setUserUid(userVo.getUserUid());
		ctMbshChk = (CtMbshDVo) commonDao.queryVo(ctMbshChk);
		
		if(ctMbshChk == null){
			//커뮤니티 회원 상세 
			//커뮤니티를 만든 사람은 MASTER이자 가입확정으로 저장 된다.
			CtMbshDVo ctMbshDVo = new CtMbshDVo();
			ctMbshDVo.setCtId(ctId);
			//ctMbshDVo.setCompId(userVo.getCompId());
			ctMbshDVo.setCompId(ctEstbBVo.getCompId());
			ctMbshDVo.setUserUid(userVo.getUserUid());
			ctMbshDVo.setUserSeculCd("M");
			ctMbshDVo.setJoinDt(ctCmntSvc.currentDay());
			ctMbshDVo.setJoinStat("3");
			
			queryQueue.insert(ctMbshDVo);
		}
			
		commonDao.execute(queryQueue);

		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		//model.addAttribute("todo", "history.go(-3);");  
		String menuUrl = ptSecuSvc.toAuthMenuUrl(userVo, "/ct/listMyCm.do");
		
		menuUrl = ptSecuSvc.toAuthMenuUrl(userVo, "/ct/listMyCm.do");
		
		
		String ctHomeMenuId =  ctCmntSvc.getCtHomeMenuId(ctId);
		
		if(menuId.substring(0,2).equalsIgnoreCase(PtConstant.MNU_GRP_REF_CT)){
			menuUrl = "/ct/viewCm.do?menuId="+ (ctHomeMenuId!=""?ctHomeMenuId:menuId) + "&ctId=" + ctId;
		}else{
			String retUrl = "";
			
			// 신규개설시 승인정책에 따라 리턴URL 처리
			if(fnc.equals("reg"))
			{
				if(ctPolcDVo != null && ctPolcDVo.getApvdYn().equalsIgnoreCase("Y"))
					retUrl = "/ct/listCmReqs.do";
				else
					retUrl = "/ct/listMyCm.do";
			}
			else
				retUrl = "/ct/listCmReqs.do";
		
			menuUrl = ptSecuSvc.toAuthMenuUrl(userVo, retUrl);
		}
		
		model.put("todo", "parent.location.replace('" + menuUrl +"');");

		//공통 처리 페이지
		return LayoutUtil.getResultJsp();
	}
	
	/** 커뮤니티 가입*/
	@RequestMapping(value = "/ct/mng/listMngMain")
	public String listMngMain(HttpServletRequest request, ModelMap model) throws Exception{
		String ctId = request.getParameter("ctId");
		ctCmntSvc.putLeftMenu(request, ctId, model);
		
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
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
		
		return LayoutUtil.getJspPath("/ct/mng/listMngMain");
	}
	
	
	/** 커뮤니티 관리*/
	@RequestMapping(value = "/ct/mng/setMngAuth")
	public String setMngAuth(HttpServletRequest request, ModelMap model) throws Exception{
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
		String fncUid = request.getParameter("menuId");
		
		CtFncDVo ctFncDVo = new CtFncDVo();
		//ctFncDVo.setCompId(userVo.getCompId());
		ctFncDVo.setLangTyp(langTypCd);
		ctFncDVo.setOrderBy("CT_FNC_LOC_STEP, CT_FNC_PID , CT_FNC_ORDR");
		ctFncDVo.setCtId(ctId);
		ctFncDVo.setCtFncTyp("2");
		@SuppressWarnings("unchecked")
		List<CtFncDVo> ctFncSelectList = (List<CtFncDVo>) commonDao.queryList(ctFncDVo);
		CtFncDVo delVo=null;
		for(CtFncDVo dvo:ctFncSelectList){
			if(dvo.getCtFncId().equals("CTMANAGEMENT")){
				delVo=dvo;
			}
		}
		if(delVo!=null)
			ctFncSelectList.remove(delVo);
		model.put("ctFncSelectList", ctFncSelectList);
		
		ctCmntSvc.putLeftMenu(request, ctId, model);
		//저장 "W"/ 삭제"D"
		ctCmntSvc.putAuthChk(request, model, "W", ctId, fncUid);
		
		/**
		 * 커뮤니티 기능정보에서 메뉴명 가져오기
		 */
		CtFncDVo ctFncDMenu =  new CtFncDVo();
		ctFncDMenu.setCompId(ctEstbBVo.getCompId());
		ctFncDMenu.setCtFncUid(fncUid);
		ctFncDMenu.setLangTyp(langTypCd);
		CtFncDVo CtFncDVo =  (CtFncDVo) commonDao.queryVo(ctFncDMenu);
		model.put("menuTitle", CtFncDVo.getCtFncNm());
		
		
		return LayoutUtil.getJspPath("/ct/mng/setMngAuth");
	}
	
	
	
	/** 커뮤니티 마스터 권한 조회 */
	@RequestMapping(value = "/ct/mng/listMngAuthFrm")
	public String listCtFldFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		String fncUid = request.getParameter("fncUid");
		
		ctCmntSvc.putCtAuth(request, model, fncUid);
		
		return LayoutUtil.getJspPath("/ct/mng/listMngAuthFrm");
	}
	
	/** 기능권한 등록  */
	@RequestMapping(value = "/ct/mng/transCtAuthSaveAjx")
	public String transCtAuthSaveAjx(HttpServletRequest request,
			@Parameter(name="fncUid", required=false) String fncUid,
			@Parameter(name="mode", required=false) String mode,
			ModelMap model) throws Exception {
		
		if(fncUid==null || fncUid.isEmpty() ){
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			LOGGER.error("save cd[folder] - fncUid:"+fncUid+"  msg:"+msg);
			model.put("message", msg);
			return LayoutUtil.getResultJsp();
		}
		
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		try{
			// 세션 사용자 정보
			//UserVo userVo = LoginSession.getUser(request);

			QueryQueue queryQueue = new QueryQueue();
			
			String authR = null;
			String authW = null;
			String authD = null;
			String auth = null;
			//마스터(M) R,W,D (읽기, 쓰기, 삭제) 권한
//			ctFncMbshAuthMVo.setCompId(userVo.getCompId());
//			ctFncMbshAuthMVo.setSeculCd("M");
//			ctFncMbshAuthMVo.setAuthCd("R/W/D");
			
			//마스터 권한이 제대로 안들어 갔을 경우를 대비하여
			//기능별권한관리에서 기능 저장 할때 다시 마스터 권한 확인 후 없으면 insert
			//////////////////////////////////////
			CtFncMbshAuthDVo ctFncMbshAuthMVo = new CtFncMbshAuthDVo();
			ctFncMbshAuthMVo.setCompId(ctEstbBVo.getCompId());
			ctFncMbshAuthMVo.setCtFncUid(fncUid);
			ctFncMbshAuthMVo.setSeculCd("M");
			ctFncMbshAuthMVo.setAuthCd("R/W/D");
			int ctFncMchk = commonDao.count(ctFncMbshAuthMVo);
			if(ctFncMchk == 0){
				queryQueue.insert(ctFncMbshAuthMVo);
			//////////////////////////////////////
			}
			
			//================================================================
			//스탭(S) R,W,D (읽기, 쓰기, 삭제) 권한
			//================================================================			
			String authSR = request.getParameter("authSR");
			String authSW = request.getParameter("authSW");
			String authSD = request.getParameter("authSD");
			
			CtFncMbshAuthDVo ctFncMbshAuthSVo = new CtFncMbshAuthDVo();
			ctFncMbshAuthSVo.setCompId(ctEstbBVo.getCompId());
			ctFncMbshAuthSVo.setCtFncUid(fncUid);
			ctFncMbshAuthSVo.setSeculCd("S");
			
			int ctFncSchk = commonDao.count(ctFncMbshAuthSVo);
			
			authR = authSR!=null?authSR+"/":"/";
			authW = authSW!=null?authSW+"/":"/";
			authD = authSD!=null?authSD+"/":"/";
			auth = authR+authW+authD;
			ctFncMbshAuthSVo.setAuthCd(auth.substring(0, auth.length()-1 ));
			
			if(ctFncSchk != 0){
				queryQueue.update(ctFncMbshAuthSVo);
			}else{
				queryQueue.insert(ctFncMbshAuthSVo);
			}
			
			//================================================================			
			//정회원(R) R,W,D (읽기, 쓰기, 삭제) 권한
			//================================================================			
			String authRR = request.getParameter("authRR");
			String authRW = request.getParameter("authRW");
			String authRD = request.getParameter("authRD");
			
			CtFncMbshAuthDVo ctFncMbshAuthRVo = new CtFncMbshAuthDVo();
			ctFncMbshAuthRVo.setCompId(ctEstbBVo.getCompId());
			ctFncMbshAuthRVo.setCtFncUid(fncUid);
			ctFncMbshAuthRVo.setSeculCd("R");
			
			int ctFncRchk = commonDao.count(ctFncMbshAuthRVo);
			
			authR = authRR!=null?authRR+"/":"/";
			authW = authRW!=null?authRW+"/":"/";
			authD = authRD!=null?authRD+"/":"/";
			auth = authR+authW+authD;
			ctFncMbshAuthRVo.setAuthCd(auth.substring(0, auth.length()-1 ));
			
			if(ctFncRchk != 0){
				queryQueue.update(ctFncMbshAuthRVo);
			}else{
				queryQueue.insert(ctFncMbshAuthRVo);
			}
			
			//================================================================
			//준회원(A) R,W,D (읽기, 쓰기, 삭제) 권한
			//================================================================
			String authAR = request.getParameter("authAR");
			String authAW = request.getParameter("authAW");
			String authAD = request.getParameter("authAD");
			
			CtFncMbshAuthDVo ctFncMbshAuthAVo = new CtFncMbshAuthDVo();
			ctFncMbshAuthAVo.setCompId(ctEstbBVo.getCompId());
			ctFncMbshAuthAVo.setCtFncUid(fncUid);
			ctFncMbshAuthAVo.setSeculCd("A");
			
			int ctFncAchk = commonDao.count(ctFncMbshAuthAVo);
			
			authR = authAR!=null?authAR+"/":"/";
			authW = authAW!=null?authAW+"/":"/";
			authD = authAD!=null?authAD+"/":"/";
			auth = authR+authW+authD;
			ctFncMbshAuthAVo.setAuthCd(auth.substring(0, auth.length()-1 ));
			
			if(ctFncAchk != 0){
				queryQueue.update(ctFncMbshAuthAVo);
			}else{
				queryQueue.insert(ctFncMbshAuthAVo);
			}
			

			commonSvc.execute(queryQueue);
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.fncSelected('"+ fncUid +"','');");
//			model.put("todo", "parent.location.href('/ct/mng/listMngAuthFrm.do?menuId="+request.getParameter("menuId")
//					+"&fncUid=" + fncUid 
//					+"');");
//			model.put("todo", "parent.getIframeContent('mngAuth').reload();");
//			model.put("todo", "parent.reload();");  
			
//		} catch(CmException e){
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	
	
	
	/** [팝업] 사진 선택 */
	@RequestMapping(value = {"/ct/mng/setImagePop"})
	public String setImagePop(HttpServletRequest request,
			@Parameter(name="bcId", required=false) String bcId,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/ct/mng/setImagePop");
	}
	
	/** [히든프레임] 사진 업로드 */
	@RequestMapping(value = {"/ct/mng/transImage"})
	public String transImage(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		UploadHandler uploadHandler = null;
		boolean tempFileDel = false;
		try{
			uploadHandler = uploadManager.createHandler(request, "temp", "wc");
			//Map<String, File> fileMap = uploadHandler.upload();//업로드 파일 정보
			//Map<String, String> param = uploadHandler.getParamMap();//파라미터 정보
			uploadHandler.upload();
			
			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
					
			QueryQueue queryQueue = new QueryQueue();
			//List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
			CtFileDVo ctFileDvo = new CtFileDVo();
			CtFileDVo newCtFileDvo = (CtFileDVo)ctFileSvc.savePhoto(request,"photo", -1, ctFileDvo , queryQueue);
//				if(newwbSurvFileDVo != null){
//					deletedFileList.add(newwbSurvFileDVo);					
//					// 파일 삭제
//					wvFileSvc.deleteDiskFiles(deletedFileList);
//				}
			
			commonSvc.execute(queryQueue);
			//String fileDir = distManager.getContextProperty("distribute.web.local.root")+distPath;
			model.put("todo", "parent.setImage('"+request.getParameter("quesNum")+"','"+request.getParameter("examNum")+"','"+newCtFileDvo.getFileId()+"', '"+newCtFileDvo.getDispNm()+"', '"+newCtFileDvo.getSavePath().replace('\\', '/')+"');");
			tempFileDel = true;
			
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			
		} catch(CmException e){
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		} finally {
			if(uploadHandler!=null && tempFileDel) uploadHandler.removeTempDir();
		}
	
		return LayoutUtil.getResultJsp();
	}
	
	/** [히든프레임] 사진 삭제 */
	@RequestMapping(value = {"/ct/mng/transImageDel"})
	public String transImageDel(HttpServletRequest request,
			@Parameter(name="fileId", required=true) int fileId,
			ModelMap model) throws Exception {
		
		try{
			// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			
			// 명함 이미지 상세
			WvSurvFileDVo wvSurvFileDvo = new WvSurvFileDVo();
			wvSurvFileDvo.setFileId(fileId);
			queryQueue.delete(wvSurvFileDvo);
			commonSvc.execute(queryQueue);
			
			//cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("todo", "parent.pageReload();");
//		} catch (CmException e) {			
//			model.put("message", e.getMessage());
		} catch (Exception e) {			
			model.put("exception", e);
		}
	
		return LayoutUtil.getResultJsp();
	}
	
	
	
	/** 커뮤니티 초기페이지 구성 첫번째 조회 */
	@RequestMapping(value = "/ct/mng/setMngPage")
	public String setMngPage(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		CtScrnSetupDVo ctScrnSetupDVo = new CtScrnSetupDVo();
		ctScrnSetupDVo.setCompId(ctEstbBVo.getCompId());
		ctScrnSetupDVo.setCtId(ctId);
		@SuppressWarnings("unchecked")
		List<CtScrnSetupDVo> ctScrnSetupList = (List<CtScrnSetupDVo>) commonDao.queryList(ctScrnSetupDVo);
		
		if(ctScrnSetupList.size() != 0){
			String tplTemp= ctScrnSetupList.get(0).getTplFileSubj();
			Map<String,String> tplMap=new HashMap<String, String>();
			if(tplTemp!=null){
				for(String tplStrTemp:tplTemp.split("\\|")){
					tplMap.put(tplStrTemp, tplStrTemp);
				}
			}
			model.put("tpl", tplMap);			
		
		}
		ctCmntSvc.putLeftMenu(request, ctId, model);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
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
		
		return LayoutUtil.getJspPath("/ct/mng/setMngPage");
	}
	
	/** 커뮤니티 초기페이지 구성 두번째 조회 */
	@RequestMapping(value = "/ct/mng/setMngPageSetup")
	public String setMngPageSetup(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		
		String fnc = request.getParameter("fnc");
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		ctEstbBVo.setLangTyp(langTypCd);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		String[] tplArr = request.getParameterValues("tpl");
		Map<String,String> tpl=new HashMap<String, String>();
		for(String tplTemp:tplArr){
			tpl.put(tplTemp, tplTemp);
		}
		CtScrnSetupDVo ctScrnSetupDVo = new CtScrnSetupDVo();
		ctScrnSetupDVo.setCompId(ctEstbBVo.getCompId());
		ctScrnSetupDVo.setCtId(ctId);
		@SuppressWarnings("unchecked")
		List<CtScrnSetupDVo> ctScrnSetupList = (List<CtScrnSetupDVo>) commonDao.queryList(ctScrnSetupDVo);
		
		CtFncDVo ctFncDVo = new CtFncDVo();
		//ctFncDVo.setCompId(userVo.getCompId());
		ctFncDVo.setCtId(ctId);
		ctFncDVo.setCtMngYn("Y");
		ctFncDVo.setLangTyp(langTypCd);
		ctFncDVo.setOrderBy("CT_FNC_PID");
		@SuppressWarnings("unchecked")
		List<CtFncDVo> ctFncFieldList = (List<CtFncDVo>) commonDao.queryList(ctFncDVo);
		
		model.put("ctFncFieldList", ctFncFieldList);
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});
		
		model.put("ctScrnSetupList", ctScrnSetupList);
		if(ctScrnSetupList.size()>0){
			//model.put("ctItro", ctScrnSetupList.get(0).getCtItro());
			if(ctScrnSetupList.get(0).getImgFileId()!=null&&!ctScrnSetupList.get(0).getImgFileId().equals("")){
				CtFileDVo conditionFileDvo=new CtFileDVo();
				conditionFileDvo.setFileId(Integer.parseInt(ctScrnSetupList.get(0).getImgFileId()));
				conditionFileDvo=(CtFileDVo) commonDao.queryVo(conditionFileDvo);
				model.put("imgFilePath", conditionFileDvo.getSavePath());
				model.put("imgFileId", conditionFileDvo.getFileId());
			}			
		}
		model.put("tpl", tpl);
		model.put("fnc", fnc);
		ctCmntSvc.putLeftMenu(request, ctId, model);
		
		return LayoutUtil.getJspPath("/ct/mng/setMngPageSetup");
	}
	
	
	/** 커뮤니티 초기페이지 구성 저장 */
	@RequestMapping(value = "/ct/mng/transMngPageSetupSave")
	public String transMngPageSetupSave(HttpServletRequest request,
			@Parameter(name="ctId", required=false) String ctId,
//			@Parameter(name="svcMnuYn", required=false) String svcMnuYn,		
			ModelMap model) throws Exception {
		
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		String compId=ctEstbBVo.getCompId();
		
		String[] tplArr = request.getParameterValues("tpl");
		String tpl="";
		for(int i=0; i<tplArr.length; i++){
			tpl=tpl+tplArr[i]+(i<(tplArr.length-1)?"|":"");
		}
		
		if(ctId==null || ctId.isEmpty() && tpl ==null || tpl.isEmpty() ){
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			LOGGER.error("ctId flag Fail  msg:"+msg);
			model.put("message", msg);
			return LayoutUtil.getResultJsp();
		}
		
		try{
			
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			CtScrnSetupDVo ctScrnSetupFnc = new CtScrnSetupDVo();			
			ctScrnSetupFnc.setCompId(compId);
			ctScrnSetupFnc.setCtId(ctId);
			
			
			int ctScrnChk = commonDao.count(ctScrnSetupFnc);
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 기능관리 기본 테이블
			CtScrnSetupDVo ctScrnSetup = new CtScrnSetupDVo();
			VoUtil.bind(request, ctScrnSetup);
			
			//소개글 저장
			if(ctScrnSetup.getCtItro() != null && !ctScrnSetup.getCtItro().isEmpty()){
				//커뮤니티 개설 기본 (CT_ESTB_B)테이블에 저장한다.
				ctEstbBVo = new CtEstbBVo();
				ctEstbBVo.setCompId(compId);
				ctEstbBVo.setCtId(ctId);
				
				ctEstbBVo.setCtItro(ctScrnSetup.getCtItro());

				queryQueue.update(ctEstbBVo);
			}
			
			ctScrnSetup.setCompId(compId);
			ctScrnSetup.setTplFileSubj(tpl);
			
			
			if(ctScrnChk == 0){
				ctScrnSetup.setRegDt(ctCmntSvc.currentDay());
				ctScrnSetup.setRegrUid(userVo.getUserUid());
				ctScrnSetup.setModDt(ctCmntSvc.currentDay());
				ctScrnSetup.setModrUid(userVo.getUserUid());
				queryQueue.insert(ctScrnSetup);
			} else {
				ctScrnSetup.setModDt(ctCmntSvc.currentDay());
				ctScrnSetup.setModrUid(userVo.getUserUid());
				//테이블 - UPDATE
				queryQueue.update(ctScrnSetup);
			}
			commonSvc.execute(queryQueue);
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.location.replace('/ct/viewCm.do?menuId="+request.getParameter("menuId")
					+"&ctId=" + ctId
					+"');");
			
			
//		} catch(CmException e){
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** 커뮤니티 홈(HOME) 게시판 포틀릿 조회 */
	@RequestMapping(value = "/ct/board/listBoardPtFrm")
	public String listBoardPtFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
		String menuId = request.getParameter("menuId");
		String ctId = request.getParameter("ctId");
		
		CtBullMastBVo ctBullMastBVo = new CtBullMastBVo();
		ctBullMastBVo.setCtFncUid(menuId);
		ctBullMastBVo.setCtId(ctId);
		ctBullMastBVo.setBullExprDt("");
		ctBullMastBVo.setQueryLang(langTypCd);
		//TOP5만 보여주기 위한 flag 값

//		ctBullMastBVo.setCtHomePtYn("Y");
//		ctBullMastBVo.setReplyOrdr(0);
//		ctBullMastBVo.setReplyDpth(0);
//		ctBullMastBVo.setOrderBy("REG_DT DESC");
		request.setAttribute("pageRowCnt", 4);//RowCnt 삽입
		
		Integer recodeCount = commonDao.count(ctBullMastBVo);
		PersonalUtil.setPaging(request, ctBullMastBVo, recodeCount);
		@SuppressWarnings("unchecked")
		List<CtBullMastBVo> ctBullMastList = (List<CtBullMastBVo>) commonDao.queryList(ctBullMastBVo);
		
		model.put("ctBullMastList", ctBullMastList);
		model.put("recodeCount", recodeCount);
		return LayoutUtil.getJspPath("/ct/board/listBoardPtFrm");
	}
	
	/** 커뮤니티 홈(HOME) 자료실 포틀릿 조회 */
	@RequestMapping(value = "/ct/pds/listPdsPtFrm")
	public String listPdsPtFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		String menuId = request.getParameter("menuId");
		String ctId = request.getParameter("ctId");
		
		CtRecMastBVo ctRecMastBVo = new CtRecMastBVo();
		ctRecMastBVo.setCtFncUid(menuId);
		ctRecMastBVo.setCtId(ctId);
		//TOP5만 보여주기 위한 flag 값

		ctRecMastBVo.setOrderBy("REG_DT DESC");
		request.setAttribute("pageRowCnt", 4);//RowCnt 삽입
		@SuppressWarnings("unchecked")
		List<CtBullMastBVo> ctRecMastList = (List<CtBullMastBVo>) commonDao.queryList(ctRecMastBVo);
				
		
		
		model.put("ctRecMastList", ctRecMastList);
		
		return LayoutUtil.getJspPath("/ct/pds/listPdsPtFrm");
	}
	
	/** 커뮤니티 [관리] 커뮤니티폐쇄 */
	@RequestMapping(value = "/ct/mng/setMngClose")
	public String setMngClose(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		String fncUid = request.getParameter("menuId");
		String ctId = request.getParameter("ctId");
		
		//커뮤니티 기본 정보
		ctCmntSvc.putCtEstbInfo(request, model, ctId);
		
		//커뮤니티 전체 현황
		ctCmntSvc.putJoinPeopleStat(request, model, ctId);
		
		//커뮤니티 왼쪽 메뉴
		ctCmntSvc.putLeftMenu(request, ctId, model);
		
		//저장 "W"/ 삭제"D"
		ctCmntSvc.putAuthChk(request, model, "W", ctId, fncUid);
		
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
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
		
		return LayoutUtil.getJspPath("/ct/mng/setMngClose");
	}
	
	/** 커뮤니티 폐쇄 */
	@RequestMapping(value = "/ct/mng/setCmntClose")
	public String setCmntClose(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		
		try{
			
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String ctId  = (String) object.get("ctId");
			if (ctId == null || ctId.equalsIgnoreCase("")) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			//UserVo userVo = LoginSession.getUser(request);
			QueryQueue queryQueue = new QueryQueue();
			
			if(1 < ctCmntSvc.getMbshCount(request, ctId)){
				//ct.msg.close.mbsh.fail = 가입 회원이 존재 합니다.
				model.put("message", messageProperties.getMessage("ct.msg.close.guest.fail", request));
				model.put("result", "guest");
			}else{
				
				CtMbshDVo ctMbsh = ctCmntSvc.getMbshVo(request, ctId);
				if(!ctMbsh.getUserSeculCd().equalsIgnoreCase("M")){
					//ct.msg.close.mbsh.fail = 가입 회원이 존재 합니다.
					model.put("message", messageProperties.getMessage("ct.msg.close.guest.fail", request));
					model.put("result", "guest");
				}else{
					CtEstbBVo ctEstB = ctCmntSvc.getCtEstbInfo(request, ctId);
					if(ctEstB.getCtStat().equalsIgnoreCase("C")){
						//ct.msg.close.ing = 이미 폐쇄 신청중인 커뮤니티 입니다.
						model.put("message", messageProperties.getMessage("ct.msg.close.ing", request));
						model.put("result", "close");
					}else{
						CtEstbBVo ctEstbBVo = new CtEstbBVo();
						ctEstbBVo.setCtId(ctId);
						
						ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
						if(ctEstbBVo==null){
							// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
							throw new CmException("pt.msg.nodata.passed", request);
						}
						
						CtEstbBVo ctEstbStat = new CtEstbBVo();
						ctEstbStat.setCtId(ctId);
						ctEstbStat.setCompId(ctEstbBVo.getCompId());
						ctEstbStat.setCtStat("C");
						
						queryQueue.update(ctEstbStat);
						
						//ct.msg.close = 커뮤니티 폐쇄 신청이 완료 되었습니다.
						model.put("message", messageProperties.getMessage("ct.msg.close", request));
						model.put("result", "ok");
					}
				}
				
				commonDao.execute(queryQueue);
				
			}
			
		model.put("ctId", ctId);	
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		return JsonUtil.returnJson(model);
	}
	
	/** 커뮤니티 [관리] 커뮤니티 회원관리 */
	@RequestMapping(value = "/ct/mng/listMngJoinReqs")
	public String listMngJoinReqs(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		
		//String menuId = request.getParameter("menuId");
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		ctEstbBVo.setLangTyp(langTypCd);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		CtMbshDVo ctMbshDVo = new CtMbshDVo();
		ctMbshDVo.setCtId(ctId);
		ctMbshDVo.setCompId(ctEstbBVo.getCompId());
		ctMbshDVo.setJoinStat("1");
		ctMbshDVo.setLangTyp(langTypCd);
		
		VoUtil.bind(request, ctMbshDVo);
		
		Map<String, Object> rsltMap = ctCmntSvc.getMngJoinReqsList(request, ctMbshDVo);
		
		model.put("ctMngJoinReqsList", rsltMap.get("ctMngJoinReqsList"));
		model.put("recodeCount", rsltMap.get("recodeCount"));
		
		
		
		//커뮤니티 전체 현황
		ctCmntSvc.putJoinPeopleStat(request, model, ctId);
		
		//커뮤니티 왼쪽 메뉴
		ctCmntSvc.putLeftMenu(request, ctId, model);
		
		model.put("ctId", ctId);
		
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
		
		
		return LayoutUtil.getJspPath("/ct/mng/listMngJoinReqs");
	}
	
	/** 커뮤니티 회원 정보 POP */
	@RequestMapping(value = "/ct/mng/viewMngJoinReqsPop")
	public String viewMngJoinReqsPop(HttpServletRequest request, ModelMap model)throws Exception{
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		//회원ID(사용자UID, get방식)
		String mbshId = request.getParameter("mbshId");
		
		String mbshCtId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(mbshCtId);
		ctEstbBVo.setLangTyp(langTypCd);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		CtMbshDVo ctMbshDVo = new CtMbshDVo();
		ctMbshDVo.setCompId(ctEstbBVo.getCompId());
		ctMbshDVo.setUserUid(mbshId);
		ctMbshDVo.setCtId(mbshCtId);
		ctMbshDVo.setLangTyp(langTypCd);
		
		ctMbshDVo = (CtMbshDVo) commonDao.queryVo(ctMbshDVo);
		
		String mbshCompNm = ctMbshDVo.getCompNm();
		String mbshUserSeculCd = ctMbshDVo.getUserSeculCd();
		
		Map<String, Object> mbshInfoMap = orCmSvc.getUserMap(mbshId, langTypCd);
		model.put("mbshInfoMap", mbshInfoMap);
		model.put("mbshCompNm", mbshCompNm);
		model.put("mbshCtId", mbshCtId);
		model.put("mbshUserSeculCd", mbshUserSeculCd);
		return LayoutUtil.getJspPath("/ct/mng/viewMngJoinReqsPop");
	}
	
	
	/** 커뮤니티 가입승인 */
	@RequestMapping(value = "/ct/mng/transMbshApvd")
	public String transMbshApvd(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		
		try{
			
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String mbshCtId  = (String) object.get("mbshCtId");
			String mbshId  = (String) object.get("mbshId");
			if (mbshCtId == null || mbshCtId.isEmpty() && mbshId == null || mbshId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			CtEstbBVo ctEstbBVo = new CtEstbBVo();
			ctEstbBVo.setCtId(mbshCtId);
			
			ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
			if(ctEstbBVo==null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			//UserVo userVo = LoginSession.getUser(request);
			QueryQueue queryQueue = new QueryQueue();
			
			CtMbshDVo ctMbshDVo = new CtMbshDVo();
			ctMbshDVo.setCtId(mbshCtId);
			ctMbshDVo.setCompId(ctEstbBVo.getCompId());
			ctMbshDVo.setUserUid(mbshId);
			ctMbshDVo.setJoinStat("3");
			
			queryQueue.update(ctMbshDVo);
						
			commonDao.execute(queryQueue);
					
			//ct.msg.mbshApvd = 커뮤니티 가입이 승인 처리 되었습니다.
			model.put("message", messageProperties.getMessage("ct.msg.mbshApvd", request));
			model.put("result", "ok");
				
			model.put("mbshCtId", mbshCtId);	
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		return JsonUtil.returnJson(model);
	}
	
	
	/** 커뮤니티 회원 정보 POP */
	@RequestMapping(value = "/ct/dropOut")
	public String dropOut(HttpServletRequest request, ModelMap model)throws Exception{
		//회원ID(사용자UID, get방식)
		String menuId = request.getParameter("menuId");
		String ctId = request.getParameter("ctId");
	
		ctCmntSvc.putLeftMenu(request, ctId, model);
		model.put("menuId", menuId);
		return LayoutUtil.getJspPath("/ct/dropOut");
	}
	
	/** 커뮤니티 가입미승인 */
	@RequestMapping(value = "/ct/mng/transMbshNapvd")
	public String transMbshNapvd(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		
		try{
			
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String mbshCtId  = (String) object.get("mbshCtId");
			String mbshId  = (String) object.get("mbshId");
			if (mbshCtId == null || mbshCtId.isEmpty() && mbshId == null || mbshId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			CtEstbBVo ctEstbBVo = new CtEstbBVo();
			ctEstbBVo.setCtId(mbshCtId);
			
			ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
			if(ctEstbBVo==null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			//UserVo userVo = LoginSession.getUser(request);
			QueryQueue queryQueue = new QueryQueue();
			
			CtMbshDVo ctMbshDVo = new CtMbshDVo();
			ctMbshDVo.setCtId(mbshCtId);
			ctMbshDVo.setCompId(ctEstbBVo.getCompId());
			ctMbshDVo.setUserUid(mbshId);
			ctMbshDVo.setJoinStat("2");
			
			queryQueue.update(ctMbshDVo);
						
			commonDao.execute(queryQueue);
					
			//ct.msg.mbshNapvd = 커뮤니티 가입이 미승인 처리 되었습니다.
			model.put("message", messageProperties.getMessage("ct.msg.mbshNapvd", request));
			model.put("result", "ok");
				
			model.put("mbshCtId", mbshCtId);	
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		return JsonUtil.returnJson(model);
	}
	
	
	
	/** 커뮤니티 탈퇴 */
	@RequestMapping(value = "/ct/transMbshDropOut")
	public String transMbshDropOut(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		
		try{
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			QueryQueue queryQueue = new QueryQueue();
			
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String ctId  = (String) object.get("ctId");
			
			if (ctId == null || ctId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			CtEstbBVo ctEstbBVo = new CtEstbBVo();
			ctEstbBVo.setCtId(ctId);
			
			ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
			if(ctEstbBVo==null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			String seculCd = ctCmntSvc.getUserSeculCd(userVo.getCompId(), userVo.getUserUid(), ctId);
			
			if(seculCd.equalsIgnoreCase("M")){
				//ct.msg.mast.dropOut.fail = 마스터는 해당 커뮤니티에 탈퇴 할 수 없습니다. 
				model.put("message", messageProperties.getMessage("ct.msg.mast.dropOut.fail", request));
				model.put("result", "fail");
			}else{
				//사용자 권한에 따른 url에 대한 menuId를 가져온다.
				String menuUrl = ptSecuSvc.toAuthMenuUrl(userVo, "/ct/listMyCm.do");
				
				CtMbshDVo ctMbshDVo = new CtMbshDVo();
				ctMbshDVo.setCtId(ctId);
				ctMbshDVo.setCompId(ctEstbBVo.getCompId());
				ctMbshDVo.setUserUid(userVo.getUserUid());
				
				queryQueue.delete(ctMbshDVo);
							
				commonDao.execute(queryQueue);
				
				//ct.msg.dropOut.sucess = 커뮤니티 탈퇴 되었습니다.
				model.put("message", messageProperties.getMessage("ct.msg.dropOut.sucess", request));
				model.put("result", "ok");
				model.put("menuUrl", menuUrl);
				
			}
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		return JsonUtil.returnJson(model);
	}
	
	/** 커뮤니티 방문자현황 */
	@RequestMapping(value = "/ct/mng/listMngVistrStat")
	public String listMngVistrStat(HttpServletRequest request, ModelMap model)throws Exception{
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		//회원ID(사용자UID, get방식)
		String menuId = request.getParameter("menuId");
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		ctEstbBVo.setLangTyp(langTypCd);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		String strtDt = request.getParameter("strtDT");
		String endDt = request.getParameter("endDT");
		//왼쪽 메뉴 리스트
		ctCmntSvc.putLeftMenu(request, ctId, model);
		
		CtVistrHstDVo ctVistrHstDVo = new CtVistrHstDVo();
		if(strtDt != null && endDt != null){
			ctVistrHstDVo.setStrtDt(strtDt);
			ctVistrHstDVo.setEndDt(endDt);
		}else{
			ctVistrHstDVo.setStrtDt(ctCmntSvc.currentYmdDay());
			ctVistrHstDVo.setEndDt(ctCmntSvc.currentYmdDay());
		}
		
		ctVistrHstDVo.setCtId(ctId);
		ctVistrHstDVo.setCompId(ctEstbBVo.getCompId());
		ctVistrHstDVo.setInstanceQueryId("selectCtVistrHstCountSumD");
		ctVistrHstDVo.setLangTyp(langTypCd);
		ctVistrHstDVo.setJoinStat("3");
		Map<String, Object> rsltMap = ctCmntSvc.getMngVistrStatList(request, ctVistrHstDVo, ctId);
		
		model.put("ctMngVistHstList", rsltMap.get("ctMngVistHstList"));
		model.put("recodeCount", rsltMap.get("recodeCount"));
		
		model.put("menuId", menuId);
		model.put("strtDt", ctVistrHstDVo.getStrtDt());
		model.put("endDt", ctVistrHstDVo.getEndDt());
		
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
		
		return LayoutUtil.getJspPath("/ct/mng/listMngVistrStat");
	}
	
	/** 커뮤니티 자료관리 조회 */
	@RequestMapping(value = "/ct/mng/listMngData")
	public String listMngData(HttpServletRequest request, ModelMap model)throws Exception{
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//회원ID(사용자UID, get방식)
		//String menuId = request.getParameter("menuId");
		String ctId = request.getParameter("ctId");
		String fncMng = request.getParameter("fncMng");
		String dateSelect = request.getParameter("dateSelect");
		String strtDt = request.getParameter("strtDt");
		String endDt = request.getParameter("endDt");
		
		
		//왼쪽 메뉴
		ctCmntSvc.putLeftMenu(request, ctId, model);
		
		//==================================================================================================
		//FncMngList
		//==================================================================================================
		CtFncMngBVo ctFncMngBVo = new CtFncMngBVo();
		ctFncMngBVo.setCtMngYn("Y");
		ctFncMngBVo.setUseYn("Y");
		ctFncMngBVo.setCtId(ctId);
		ctFncMngBVo.setLangTyp(langTypCd);
		@SuppressWarnings("unchecked")
		List<CtFncMngBVo> ctFncMngList =  (List<CtFncMngBVo>) commonDao.queryList(ctFncMngBVo);
		
		model.put("ctFncMngList", ctFncMngList);
		
		String fncMngDef = null;
		if(ctFncMngList.size() != 0) {
			fncMngDef = ctFncMngList.get(0).getRelTblSubj();
		}else{
			fncMngDef ="";
		}
		//==================================================================================================
		//TABLE 별 자료 검색결과 나타내기
		//커뮤니티 기능관리에서 관계테이블제목을 지정 후 코딩 해야합니다.
		//==================================================================================================
		Integer recodeCount = 0;
		request.setCharacterEncoding("utf-8");
		String fncMngNm = fncMng!=null?fncMng:fncMngDef;
		//게시판
		if("BULL".equalsIgnoreCase(fncMngNm)){
			CtBullMastBVo ctBullMastBVo = new CtBullMastBVo();
			ctBullMastBVo.setStrtDt(strtDt);
			ctBullMastBVo.setEndDt(endDt);
			ctBullMastBVo.setCtId(ctId);
			ctBullMastBVo.setLangTyp(langTypCd);
			VoUtil.bind(request, ctBullMastBVo);
					
			
			recodeCount = commonDao.count(ctBullMastBVo);
			PersonalUtil.setPaging(request, ctBullMastBVo, recodeCount);
			@SuppressWarnings("unchecked")
			List<CtBullMastBVo> ctBullMastList = (List<CtBullMastBVo>) commonDao.queryList(ctBullMastBVo);
			
			//기능별로 ID명이 틀려서 가공한다 가공하는 ID명은 ctComFncUid
			for(CtBullMastBVo ctBullMst: ctBullMastList){
				ctBullMst.setCtComFncUid(String.valueOf(ctBullMst.getBullId()));
			}
			model.put("recodeCount", recodeCount);
			model.put("ctFncMngFileList", ctBullMastList);
			
		//토론실
		}else if("DEBR".equalsIgnoreCase(fncMngNm)){
			CtDebrBVo ctDebrBVo = new CtDebrBVo();
			ctDebrBVo.setStrtDt(strtDt);
			ctDebrBVo.setEndDt(endDt);
			ctDebrBVo.setCtId(ctId);
			ctDebrBVo.setLangTyp(langTypCd);
			VoUtil.bind(request, ctDebrBVo);
			
			
			recodeCount = commonDao.count(ctDebrBVo);
			PersonalUtil.setPaging(request, ctDebrBVo, recodeCount);
			@SuppressWarnings("unchecked")
			List<CtDebrBVo> ctDebrList = (List<CtDebrBVo>) commonDao.queryList(ctDebrBVo);
			//기능별로 ID명이 틀려서 가공한다 가공하는 ID명은 ctComFncUid
			for(CtDebrBVo ctDebr: ctDebrList){
				ctDebr.setCtComFncUid(ctDebr.getDebrId());
			}
			
			model.put("recodeCount", recodeCount);
			model.put("ctFncMngFileList", ctDebrList);
			
		//CoolSite
		}else if("SITE".equalsIgnoreCase(fncMngNm)){
			
			CtSiteBVo ctSiteBVo = new CtSiteBVo();
			ctSiteBVo.setStrtDt(strtDt);
			ctSiteBVo.setEndDt(endDt);
			ctSiteBVo.setCtId(ctId);
			ctSiteBVo.setLangTyp(langTypCd);
			VoUtil.bind(request, ctSiteBVo);
			
			recodeCount = commonDao.count(ctSiteBVo);
			PersonalUtil.setPaging(request, ctSiteBVo, recodeCount);
			@SuppressWarnings("unchecked")
			List<CtSiteBVo> ctSiteList = (List<CtSiteBVo>) commonDao.queryList(ctSiteBVo);
			//기능별로 ID명이 틀려서 가공한다 가공하는 ID명은 ctComFncUid
			for(CtSiteBVo ctSite: ctSiteList){
				ctSite.setCtComFncUid(ctSite.getSiteId());
			}
			
			model.put("recodeCount", recodeCount);
			model.put("ctFncMngFileList", ctSiteList);
		//투표	
		}else if("SURV".equalsIgnoreCase(fncMngNm)){
			// 조회조건 매핑
			CtSurvBVo ctSurvBVo = new CtSurvBVo();
			ctSurvBVo.setLangTyp(langTypCd);
			ctSurvBVo.setCtId(ctId);
			VoUtil.bind(request, ctSurvBVo);
			//시작을과 종료일 없다면 mapper-에 등록
			ctSurvBVo.setStrtDt(strtDt);
			ctSurvBVo.setEndDt(endDt);
			
			recodeCount = commonDao.count(ctSurvBVo);
			PersonalUtil.setPaging(request, ctSurvBVo, recodeCount);
			@SuppressWarnings("unchecked")
			List<CtSurvBVo> ctSurvList = (List<CtSurvBVo>) commonDao.queryList(ctSurvBVo);
			//커뮤니티 통합기능Uid ctComFncUid
			//기능별로 ID명이 틀려서 가공한다 가공하는 ID명은 ctComFncUid
			for(CtSurvBVo ctSurv: ctSurvList){
				//ctComFncUid Vo에 등록
				ctSurv.setCtComFncUid(ctSurv.getSurvId());
			}
			
			model.put("recodeCount", recodeCount);
			model.put("ctFncMngFileList", ctSurvList);
			
		}else if("SCHDL".equalsIgnoreCase(fncMngNm)){
			CtSchdlBVo ctSchdlBVo = new CtSchdlBVo();
			ctSchdlBVo.setLangTyp(langTypCd);
			ctSchdlBVo.setCtId(ctId);
			VoUtil.bind(request, ctSchdlBVo);
			ctSchdlBVo.setStrtDt(strtDt);
			ctSchdlBVo.setEndDt(endDt);
			
			recodeCount = commonDao.count(ctSchdlBVo);
			PersonalUtil.setPaging(request, ctSchdlBVo, recodeCount);
			@SuppressWarnings("unchecked")
			List<CtSchdlBVo> ctSchdlList = (List<CtSchdlBVo>) commonDao.queryList(ctSchdlBVo);
			//커뮤니티 통합기능Uid ctComFncUid
			//기능별로 ID명이 틀려서 가공한다 가공하는 ID명은 ctComFncUid
			for(CtSchdlBVo ctSchdl: ctSchdlList){
				//ctComFncUid Vo에 등록
				ctSchdl.setCtComFncUid(ctSchdl.getSchdlId());
			}
			
			model.put("recodeCount", recodeCount);
			model.put("ctFncMngFileList", ctSchdlList);
			
		}
		
		
		model.put("fncMng", fncMng);
		model.put("dateSelect", dateSelect);
		model.put("strtDt", strtDt);
		model.put("endDt", endDt);
		
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
		
		return LayoutUtil.getJspPath("/ct/mng/listMngData");
	}
	
	/** 커뮤니티 마스터 기능 자료삭제 */
	@RequestMapping(value = "/ct/mng/ajaxSelectDataDel")
	public String ajaxSelectDataDel(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		
		try{
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			JSONArray selectFncIds = (JSONArray) object.get("selectFncIds");
			String ctId  = (String) object.get("ctId");
			String fncMng  = (String) object.get("fncMng");

			
			if (ctId == null || ctId.isEmpty() && selectFncIds == null || selectFncIds.isEmpty() && fncMng == null || fncMng.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			
			//String seculCd = ctCmntSvc.getUserSeculCd(userVo.getCompId(), userVo.getUserUid(), ctId);
			List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
			List<List<CommonFileVo>> deleteFileListInList = new ArrayList<List<CommonFileVo>>();
				
			String fncMngNm = fncMng!=null?fncMng:"";
			//게시판
			if("BULL".equalsIgnoreCase(fncMngNm)){
				
				for(int i=0; i < selectFncIds.size(); i++){
					//한줄답변 삭제
					ctBullMastSvc.deleteCmdBoard(Integer.parseInt((String) selectFncIds.get(i)), queryQueue);
					
					//게시물 삭제 
					ctBullMastSvc.deleteBoard(Integer.parseInt((String) selectFncIds.get(i)), queryQueue);
					
					//점수전체 삭제
					ctScreHstSvc.deleteScreHst(Integer.parseInt((String) selectFncIds.get(i)), queryQueue);
					
					//추천전체 삭제
					ctRecmdHstSvc.deleteRecmdHst(Integer.parseInt((String) selectFncIds.get(i)), queryQueue);
					
					//방문이력 삭제
					ctCmntSvc.deleteVistrHst((String) selectFncIds.get(i), queryQueue);
					
					//파일이력 DB 삭제
					deletedFileList = ctFileSvc.deleteCtFile((String) selectFncIds.get(i), queryQueue);
					deleteFileListInList.add(deletedFileList);
				}
									
			//토론실
			}else if("DEBR".equalsIgnoreCase(fncMngNm)){
				for(int i=0; i < selectFncIds.size(); i++){
					
					//토론 게시물 내용 삭제
					ctDebrSvc.deleteDebrOpin((String) selectFncIds.get(i), queryQueue);
					//토론 게시물 삭제
					ctDebrSvc.deleteDebr((String) selectFncIds.get(i), queryQueue);
					
					//자료(파일) DB 삭제
//						deletedFileList = ctFileSvc.deleteCtFile((String) selectFncIds.get(i), queryQueue);
//						deleteFileListInList.add(deletedFileList);
				
				}
			//Cool Site
			}else if("SITE".equalsIgnoreCase(fncMngNm)){
				for(int i=0; i < selectFncIds.size(); i++){
					ctSiteSvc.deleteSite((String) selectFncIds.get(i), ctId, queryQueue);
				}
			//설문
			}else if("SURV".equalsIgnoreCase(fncMngNm)){
				for(int i=0; i < selectFncIds.size(); i++){
					ctSurvSvc.deleteSurv((String) selectFncIds.get(i), ctId, queryQueue ,request);
				}
			}else if("SCHDL".equalsIgnoreCase(fncMngNm)){
				for(int i=0; i < selectFncIds.size(); i++){
					
					//약속 참석자 포함 같이 삭제
					ctScdManagerSvc.deleteSchdl((String) selectFncIds.get(i), ctId, queryQueue, request);
					
					//파일이력 DB 삭제
					deletedFileList = ctFileSvc.deleteCtFile((String) selectFncIds.get(i), queryQueue);
					deleteFileListInList.add(deletedFileList);
				}
			}
			
						
			commonDao.execute(queryQueue);
			for(List<CommonFileVo> FileList : deleteFileListInList){
				//게시물 첨부파일 삭제
				ctFileSvc.deleteDiskFiles(FileList);
			}
			
			//	cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
			
			model.put("fncMng", fncMng);
//			model.put("dateSelect", dateSelect);
//			model.put("strtDt", strtDt);
//			model.put("endDt", endDt);
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		return JsonUtil.returnJson(model);
	}
	
	
	/** 커뮤니티 자료 전체삭제 */
	@RequestMapping(value = "/ct/mng/ajaxSelectAllDel")
	public String ajaxSelectAllDel(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		
		try{
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String ctId  = (String) object.get("ctId");
			String fncMng  = (String) object.get("fncMng");
			String strtDt  = (String) object.get("strtDt");
			String endDt  = (String) object.get("endDt");

			
			if (ctId == null || ctId.isEmpty() && fncMng == null || fncMng.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			
			//String seculCd = ctCmntSvc.getUserSeculCd(userVo.getCompId(), userVo.getUserUid(), ctId);
			List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
			List<List<CommonFileVo>> deleteFileListInList = new ArrayList<List<CommonFileVo>>();
				
			String fncMngNm = fncMng!=null?fncMng:"";
			//게시판
			if("BULL".equalsIgnoreCase(fncMngNm)){
				
				CtBullMastBVo ctBullMastVo = new CtBullMastBVo();
				ctBullMastVo.setCtId(ctId);
				ctBullMastVo.setStrtDt(strtDt);
				ctBullMastVo.setEndDt(endDt);
				@SuppressWarnings("unchecked")
				List<CtBullMastBVo> ctBullMastList = (List<CtBullMastBVo>) commonDao.queryList(ctBullMastVo);
				
				
				for(CtBullMastBVo ctBullMast: ctBullMastList){
				
					//한줄답변 삭제
					ctBullMastSvc.deleteCmdBoard(ctBullMast.getBullId(), queryQueue);
					
					//게시물 삭제 
					ctBullMastSvc.deleteBoard(ctBullMast.getBullId(), queryQueue);
					
					//점수전체 삭제
					ctScreHstSvc.deleteScreHst(ctBullMast.getBullId(), queryQueue);
					
					//추천전체 삭제
					ctRecmdHstSvc.deleteRecmdHst(ctBullMast.getBullId(), queryQueue);
					
					//방문이력 삭제
					ctCmntSvc.deleteVistrHst(String.valueOf(ctBullMast.getBullId()), queryQueue);
					
					//자료(파일) DB 삭제
					deletedFileList = ctFileSvc.deleteCtFile(String.valueOf(ctBullMast.getBullId()), queryQueue);
					deleteFileListInList.add(deletedFileList);
				}
									
			//토론실
			}else if("DEBR".equalsIgnoreCase(fncMngNm)){
					
				CtDebrBVo ctDebrBVo = new CtDebrBVo();
				ctDebrBVo.setCtId(ctId);
				ctDebrBVo.setStrtDt(strtDt);
				ctDebrBVo.setEndDt(endDt);
				@SuppressWarnings("unchecked")
				List<CtDebrBVo> ctDebrList = (List<CtDebrBVo>) commonDao.queryList(ctDebrBVo);
					
				for(CtDebrBVo ctDebr: ctDebrList){
					
					//토론 게시물 내용 삭제
					ctDebrSvc.deleteDebrOpin(ctDebr.getDebrId(), queryQueue);
					
					//토론 게시물 삭제
					ctDebrSvc.deleteDebr(ctDebr.getDebrId(), queryQueue);
				
				
				
				//자료(파일) DB 삭제
//						deletedFileList = ctFileSvc.deleteCtFile(String.valueOf(ctDebr.getDebrId()), queryQueue);
//						deleteFileListInList.add(deletedFileList);
				}
					
			//Cool Site
			}else if("SITE".equalsIgnoreCase(fncMngNm)){
				
				CtSiteBVo ctSiteBVo = new CtSiteBVo();
				ctSiteBVo.setCtId(ctId);
				ctSiteBVo.setStrtDt(strtDt);
				ctSiteBVo.setEndDt(endDt);
				@SuppressWarnings("unchecked")
				List<CtSiteBVo> ctSiteList = (List<CtSiteBVo>) commonDao.queryList(ctSiteBVo);
				
					
				for(CtSiteBVo ctSite: ctSiteList){
					
				//CoolSite 게시물 내용 삭제
					ctSiteSvc.deleteSite(ctSite.getSiteId(), ctId ,queryQueue);
				
				//자료(파일) DB 삭제
//					deletedFileList = ctFileSvc.deleteCtFile(String.valueOf(ctDebr.getDebrId()), queryQueue);
//					deleteFileListInList.add(deletedFileList);
				}
				
			}else if("SURV".equalsIgnoreCase(fncMngNm)){
				CtSurvBVo ctSurvBVo = new CtSurvBVo();
				ctSurvBVo.setCtId(ctId);
				ctSurvBVo.setStrtDt(strtDt);
				ctSurvBVo.setEndDt(endDt);
				@SuppressWarnings("unchecked")
				List<CtSurvBVo> ctSiteList = (List<CtSurvBVo>) commonDao.queryList(ctSurvBVo);
				
				for(CtSurvBVo ctSurv: ctSiteList){
					
					//CoolSite 게시물 내용 삭제
					ctSurvSvc.deleteSurv(ctSurv.getSurvId(), ctId ,queryQueue, request);
					
					//자료(파일) DB 삭제
//						deletedFileList = ctFileSvc.deleteCtFile(String.valueOf(ctDebr.getDebrId()), queryQueue);
//						deleteFileListInList.add(deletedFileList);
				}
				
			}else if("SCHDL".equalsIgnoreCase(fncMngNm)){
				CtSchdlBVo ctSchdlBVo = new CtSchdlBVo();
				ctSchdlBVo.setCtId(ctId);
				ctSchdlBVo.setStrtDt(strtDt);
				ctSchdlBVo.setEndDt(endDt);
				@SuppressWarnings("unchecked")
				List<CtSchdlBVo> ctSchdlList = (List<CtSchdlBVo>) commonDao.queryList(ctSchdlBVo);

				for(CtSchdlBVo ctSchdl: ctSchdlList){
					
					//일정 참석자 포함 모두 삭제
					ctScdManagerSvc.deleteSchdl(ctSchdl.getSchdlId(), ctId, queryQueue, request);
					
					//자료(파일) DB 삭제
					deletedFileList = ctFileSvc.deleteCtFile(String.valueOf(ctSchdl.getSchdlId()), queryQueue);
					deleteFileListInList.add(deletedFileList);
				}
				
				
			}
			
						
			commonDao.execute(queryQueue);
			for(List<CommonFileVo> FileList : deleteFileListInList){
				//게시물 첨부파일 삭제
				ctFileSvc.deleteDiskFiles(FileList);
			}
			
			//cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
			
			model.put("fncMng", fncMng);
//			model.put("dateSelect", dateSelect);
			model.put("strtDt", strtDt);
			model.put("endDt", endDt);
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		return JsonUtil.returnJson(model);
	}
	
	
	/** 커뮤니티 관리자 자료관리 조회 */
	@RequestMapping(value = "/ct/adm/listData")
	public String listData(HttpServletRequest request, ModelMap model)throws Exception{
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		//회원ID(사용자UID, get방식)
		//String menuId = request.getParameter("menuId");
		//String ctId = request.getParameter("ctId");
		String fncMng = request.getParameter("fncMng");
		String dateSelect = request.getParameter("dateSelect");
		String strtDt = request.getParameter("strtDt");
		String endDt = request.getParameter("endDt");
		
		
		//왼쪽 메뉴
//		ctCmntSvc.putLeftMenu(request, ctId, model);
		
		//==================================================================================================
		//FncMngList
		//==================================================================================================
		CtFncMngBVo ctFncMngBVo = new CtFncMngBVo();
		ctFncMngBVo.setCtMngYn("Y");
		ctFncMngBVo.setUseYn("Y");
		ctFncMngBVo.setLangTyp(langTypCd);
		
		String compId = "";
		// 시스템 관리자 여부
		boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		// 시스템 관리자가 아닌 경우에는 - 회사ID 추가
		if(isSysAdmin){
			String schCompId = request.getParameter("compId");
			if(schCompId !=null && !schCompId.isEmpty()){
				compId = schCompId;
				ctFncMngBVo.setCompId(schCompId);
			}
		}else{
			compId = userVo.getCompId();
			ctFncMngBVo.setCompId(compId);
		}
				
		@SuppressWarnings("unchecked")
		List<CtFncMngBVo> ctFncMngList =  (List<CtFncMngBVo>) commonDao.queryList(ctFncMngBVo);
		
		model.put("ctFncMngList", ctFncMngList);
		
		String fncMngDef = null;
		if(ctFncMngList.size() != 0) {
			fncMngDef = ctFncMngList.get(0).getRelTblSubj();
		}else{
			fncMngDef ="";
		}
		//==================================================================================================
		//TABLE 별 자료 검색결과 나타내기
		//==================================================================================================
		Integer recodeCount = 0;
		request.setCharacterEncoding("utf-8");
		String fncMngNm = fncMng!=null?fncMng:fncMngDef;
		//게시판
		if("BULL".equalsIgnoreCase(fncMngNm)){
			CtBullMastBVo ctBullMastBVo = new CtBullMastBVo();
			ctBullMastBVo.setStrtDt(strtDt);
			ctBullMastBVo.setEndDt(endDt);
//			ctBullMastBVo.setCtId(ctId);
			ctBullMastBVo.setLangTyp(langTypCd);			
			VoUtil.bind(request, ctBullMastBVo);
			if(!"".equals(compId)) ctBullMastBVo.setCompId(compId);//회사코드 추가		
			
			recodeCount = commonDao.count(ctBullMastBVo);
			PersonalUtil.setPaging(request, ctBullMastBVo, recodeCount);
			@SuppressWarnings("unchecked")
			List<CtBullMastBVo> ctBullMastList = (List<CtBullMastBVo>) commonDao.queryList(ctBullMastBVo);
			
			//기능별로 ID명이 틀려서 가공한다 가공하는 ID명은 ctComFncUid
			for(CtBullMastBVo ctBullMst: ctBullMastList){
				ctBullMst.setCtComFncUid(String.valueOf(ctBullMst.getBullId()));
			}
			model.put("recodeCount", recodeCount);
			model.put("ctFncMngFileList", ctBullMastList);
			
		//토론실
		}else if("DEBR".equalsIgnoreCase(fncMngNm)){
			CtDebrBVo ctDebrBVo = new CtDebrBVo();
			ctDebrBVo.setStrtDt(strtDt);
			ctDebrBVo.setEndDt(endDt);
//			ctDebrBVo.setCtId(ctId);
			ctDebrBVo.setLangTyp(langTypCd);
			VoUtil.bind(request, ctDebrBVo);
			
			if(!"".equals(compId)) ctDebrBVo.setCompId(compId);//회사코드 추가
			
			recodeCount = commonDao.count(ctDebrBVo);
			PersonalUtil.setPaging(request, ctDebrBVo, recodeCount);
			@SuppressWarnings("unchecked")
			List<CtDebrBVo> ctDebrList = (List<CtDebrBVo>) commonDao.queryList(ctDebrBVo);
			//기능별로 ID명이 틀려서 가공한다 가공하는 ID명은 ctComFncUid
			for(CtDebrBVo ctDebr: ctDebrList){
				ctDebr.setCtComFncUid(ctDebr.getDebrId());
			}
			
			model.put("recodeCount", recodeCount);
			model.put("ctFncMngFileList", ctDebrList);
		//CoolSite
		}else if("SITE".equalsIgnoreCase(fncMngNm)){
			
			CtSiteBVo ctSiteBVo = new CtSiteBVo();
			ctSiteBVo.setStrtDt(strtDt);
			ctSiteBVo.setEndDt(endDt);
			ctSiteBVo.setLangTyp(langTypCd);
			VoUtil.bind(request, ctSiteBVo);
			
			if(!"".equals(compId)) ctSiteBVo.setCompId(compId);//회사코드 추가
			
			recodeCount = commonDao.count(ctSiteBVo);
			PersonalUtil.setPaging(request, ctSiteBVo, recodeCount);
			@SuppressWarnings("unchecked")
			List<CtSiteBVo> ctSiteList = (List<CtSiteBVo>) commonDao.queryList(ctSiteBVo);
			//기능별로 ID명이 틀려서 가공한다 가공하는 ID명은 ctComFncUid
			for(CtSiteBVo ctSite: ctSiteList){
				ctSite.setCtComFncUid(ctSite.getSiteId());
			}
			
			model.put("recodeCount", recodeCount);
			model.put("ctFncMngFileList", ctSiteList);
		//설문
		}else if("SURV".equalsIgnoreCase(fncMngNm)){
			
			// 조회조건 매핑
			CtSurvBVo ctSurvBVo = new CtSurvBVo();
			ctSurvBVo.setLogUserUid(userVo.getUserUid());
			ctSurvBVo.setLangTyp(langTypCd);
			VoUtil.bind(request, ctSurvBVo);
			//시작을과 종료일 없다면 mapper-에 등록
			ctSurvBVo.setStrtDt(strtDt);
			ctSurvBVo.setEndDt(endDt);
			
			if(!"".equals(compId)) ctSurvBVo.setCompId(compId);//회사코드 추가
			
			recodeCount = commonDao.count(ctSurvBVo);
			PersonalUtil.setPaging(request, ctSurvBVo, recodeCount);
			@SuppressWarnings("unchecked")
			List<CtSurvBVo> ctSurvList = (List<CtSurvBVo>) commonDao.queryList(ctSurvBVo);
			//커뮤니티 통합기능Uid ctComFncUid
			//기능별로 ID명이 틀려서 가공한다 가공하는 ID명은 ctComFncUid
			for(CtSurvBVo ctSurv: ctSurvList){
				//ctComFncUid Vo에 등록
				ctSurv.setCtComFncUid(ctSurv.getSurvId());
			}
			
			model.put("recodeCount", recodeCount);
			model.put("ctFncMngFileList", ctSurvList);
			
			
		}else if("SCHDL".equalsIgnoreCase(fncMngNm)){
			CtSchdlBVo ctSchdlBVo = new CtSchdlBVo();
			ctSchdlBVo.setLangTyp(langTypCd);
			VoUtil.bind(request, ctSchdlBVo);
			ctSchdlBVo.setStrtDt(strtDt);
			ctSchdlBVo.setEndDt(endDt);
			
			if(!"".equals(compId)) ctSchdlBVo.setCompId(compId);//회사코드 추가
			
			recodeCount = commonDao.count(ctSchdlBVo);
			PersonalUtil.setPaging(request, ctSchdlBVo, recodeCount);
			@SuppressWarnings("unchecked")
			List<CtSchdlBVo> ctSchdlList = (List<CtSchdlBVo>) commonDao.queryList(ctSchdlBVo);
			//커뮤니티 통합기능Uid ctComFncUid
			//기능별로 ID명이 틀려서 가공한다 가공하는 ID명은 ctComFncUid
			for(CtSchdlBVo ctSchdl: ctSchdlList){
				//ctComFncUid Vo에 등록
				ctSchdl.setCtComFncUid(ctSchdl.getSchdlId());
			}
			
			model.put("recodeCount", recodeCount);
			model.put("ctFncMngFileList", ctSchdlList);
		}
		
		
		model.put("fncMng", fncMng);
		model.put("dateSelect", dateSelect);
		model.put("strtDt", strtDt);
		model.put("endDt", endDt);
		
		return LayoutUtil.getJspPath("/ct/adm/listData");
	}
	
	/** 커뮤니티 관리자 자료삭제 */
	@RequestMapping(value = "/ct/adm/ajaxAdmSelectDataDel")
	public String ajaxAdmSelectDataDel(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		
		try{
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			QueryQueue queryQueue = new QueryQueue();
			
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			JSONArray selectFncIds = (JSONArray) object.get("selectFncIds");
			String fncMng  = (String) object.get("fncMng");
			String dateSelect  = (String) object.get("dateSelect");
			String strtDt  = (String) object.get("strtDt");
			String endDt  = (String) object.get("endDt");
			
			if (selectFncIds == null || selectFncIds.isEmpty() && fncMng == null || fncMng.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
			List<List<CommonFileVo>> deleteFileListInList = new ArrayList<List<CommonFileVo>>();
			if(userVo.isAdmin() || userVo.isSysAdmin()){
				
				String fncMngNm = fncMng!=null?fncMng:"";
				//게시판
				if("BULL".equalsIgnoreCase(fncMngNm)){
					
					for(int i=0; i < selectFncIds.size(); i++){
						//한줄답변 삭제
						ctBullMastSvc.deleteCmdBoard(Integer.parseInt((String) selectFncIds.get(i)), queryQueue);
						
						//게시물 삭제 
						ctBullMastSvc.deleteBoard(Integer.parseInt((String) selectFncIds.get(i)), queryQueue);
						
						//점수전체 삭제
						ctScreHstSvc.deleteScreHst(Integer.parseInt((String) selectFncIds.get(i)), queryQueue);
						
						//추천전체 삭제
						ctRecmdHstSvc.deleteRecmdHst(Integer.parseInt((String) selectFncIds.get(i)), queryQueue);
						
						//방문이력 삭제
						ctCmntSvc.deleteVistrHst((String) selectFncIds.get(i), queryQueue);
						
						//자료(파일) DB 삭제
						deletedFileList = ctFileSvc.deleteCtFile((String) selectFncIds.get(i), queryQueue);
						deleteFileListInList.add(deletedFileList);
					}
									
				//토론실
				}else if("DEBR".equalsIgnoreCase(fncMngNm)){	
					for(int i=0; i < selectFncIds.size(); i++){
						
						//토론 게시물 내용 삭제
						ctDebrSvc.deleteDebrOpin((String) selectFncIds.get(i), queryQueue);
						//토론 게시물 삭제
						ctDebrSvc.deleteDebr((String) selectFncIds.get(i), queryQueue);
						
						//자료(파일) DB 삭제
//						deletedFileList = ctFileSvc.deleteCtFile((String) selectFncIds.get(i), queryQueue);
//						deleteFileListInList.add(deletedFileList);
					
					}
				//Cool Site
				}else if("SITE".equalsIgnoreCase(fncMngNm)){	
					for(int i=0; i < selectFncIds.size(); i++){
						ctSiteSvc.deleteAdmSite((String) selectFncIds.get(i), queryQueue);
					}
				//설문 답변및 문제까지 모두삭제
				}else if("SURV".equalsIgnoreCase(fncMngNm)){
					for(int i=0; i < selectFncIds.size(); i++){
						ctSurvSvc.deleteSurv((String) selectFncIds.get(i), queryQueue, request);
					}
				}else if("SCHDL".equalsIgnoreCase(fncMngNm)){
					for(int i=0; i < selectFncIds.size(); i++){
						
						//약속 참석자 포함 같이 삭제
						ctScdManagerSvc.deleteSchdl((String) selectFncIds.get(i), queryQueue, request);
						
						//파일이력 DB 삭제
						deletedFileList = ctFileSvc.deleteCtFile((String) selectFncIds.get(i), queryQueue);
						deleteFileListInList.add(deletedFileList);
					}
				}
				
				
				commonDao.execute(queryQueue);
				for(List<CommonFileVo> FileList : deleteFileListInList){
					//게시물 첨부파일 삭제
					ctFileSvc.deleteDiskFiles(FileList);
				}
				
				
				
				//	cm.msg.del.success=삭제 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
				model.put("result", "ok");
				
				model.put("fncMng", fncMng);
				model.put("dateSelect", dateSelect);
				model.put("strtDt", strtDt);
				model.put("endDt", endDt);

			}
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		return JsonUtil.returnJson(model);
	}
	
	
	/** 커뮤니티 관리자 자료 전체삭제 */
	@RequestMapping(value = "/ct/adm/ajaxAdmSelectAllDel")
	public String ajaxAdmSelectAllDel(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		
		try{
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			QueryQueue queryQueue = new QueryQueue();
			
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String fncMng  = (String) object.get("fncMng");
			String strtDt  = (String) object.get("strtDt");
			String endDt  = (String) object.get("endDt");
			String schCat  = (String) object.get("schCat");
			String schWord  = (String) object.get("schWord");

			
			if (fncMng == null || fncMng.isEmpty() &&schCat ==null || schCat.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			String compId = "";
			// 시스템 관리자 여부
			boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
			// 시스템 관리자가 아닌 경우에는 - 회사ID 추가
			if(!isSysAdmin){
				compId = userVo.getCompId();
			}
			
			List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
			List<List<CommonFileVo>> deleteFileListInList = new ArrayList<List<CommonFileVo>>();
			//관리자 만 삭제 할 수 있다. 삭제권한도 있는사람은 할 수 있따.
			if(userVo.isAdmin() || userVo.isSysAdmin()){
				
				String fncMngNm = fncMng!=null?fncMng:"";
				//게시판
				if("BULL".equalsIgnoreCase(fncMngNm)){
					
					CtBullMastBVo ctBullMastVo = new CtBullMastBVo();
					ctBullMastVo.setStrtDt(strtDt);
					ctBullMastVo.setEndDt(endDt);
					ctBullMastVo.setLangTyp(langTypCd);
					ctBullMastVo.setSchCat(schCat);
					ctBullMastVo.setSchWord(schWord);
					VoUtil.bind(request, ctBullMastVo);
					
					if(!"".equals(compId)) ctBullMastVo.setCompId(compId);//회사코드 추가
					@SuppressWarnings("unchecked")
					List<CtBullMastBVo> ctBullMastList = (List<CtBullMastBVo>) commonDao.queryList(ctBullMastVo);
					
					
					for(CtBullMastBVo ctBullMast: ctBullMastList){
					
						//한줄답변 삭제
						ctBullMastSvc.deleteCmdBoard(ctBullMast.getBullId(), queryQueue);
						
						//게시물 삭제 
						ctBullMastSvc.deleteBoard(ctBullMast.getBullId(), queryQueue);
						
						//점수전체 삭제
						ctScreHstSvc.deleteScreHst(ctBullMast.getBullId(), queryQueue);
						
						//추천전체 삭제
						ctRecmdHstSvc.deleteRecmdHst(ctBullMast.getBullId(), queryQueue);
						
						//방문이력 삭제
						ctCmntSvc.deleteVistrHst(String.valueOf(ctBullMast.getBullId()), queryQueue);
						
						//자료(파일) DB 삭제
						deletedFileList = ctFileSvc.deleteCtFile(String.valueOf(ctBullMast.getBullId()), queryQueue);
						deleteFileListInList.add(deletedFileList);
					}
										
				//토론실
				}else if("DEBR".equalsIgnoreCase(fncMngNm)){	
						
					CtDebrBVo ctDebrBVo = new CtDebrBVo();
					ctDebrBVo.setStrtDt(strtDt);
					ctDebrBVo.setEndDt(endDt);
					ctDebrBVo.setSchCat(schCat);
					ctDebrBVo.setSchWord(schWord);
					ctDebrBVo.setLangTyp(langTypCd);
					VoUtil.bind(request, ctDebrBVo);
					
					if(!"".equals(compId)) ctDebrBVo.setCompId(compId);//회사코드 추가
					@SuppressWarnings("unchecked")
					List<CtDebrBVo> ctDebrList = (List<CtDebrBVo>) commonDao.queryList(ctDebrBVo);
					
						
					for(CtDebrBVo ctDebr: ctDebrList){
						
					//토론 게시물 내용 삭제
					ctDebrSvc.deleteDebrOpin(ctDebr.getDebrId(), queryQueue);
					
					//토론 게시물 삭제
					ctDebrSvc.deleteDebr(ctDebr.getDebrId(), queryQueue);
					
					//자료(파일) DB 삭제
//						deletedFileList = ctFileSvc.deleteCtFile(String.valueOf(ctDebr.getDebrId()), queryQueue);
//						deleteFileListInList.add(deletedFileList);
					}
						
				//Cool Site
				}else if("SITE".equalsIgnoreCase(fncMngNm)){		
					CtSiteBVo ctSiteBVo = new CtSiteBVo();
					ctSiteBVo.setStrtDt(strtDt);
					ctSiteBVo.setEndDt(endDt);
					ctSiteBVo.setSchCat(schCat);
					ctSiteBVo.setSchWord(schWord);
					ctSiteBVo.setLangTyp(langTypCd);
					VoUtil.bind(request, ctSiteBVo);
					if(!"".equals(compId)) ctSiteBVo.setCompId(compId);//회사코드 추가
					@SuppressWarnings("unchecked")
					List<CtSiteBVo> ctSiteList = (List<CtSiteBVo>) commonDao.queryList(ctSiteBVo);
					
						
					for(CtSiteBVo ctSite: ctSiteList){
						
					//CoolSite 게시물 내용 삭제
						ctSiteSvc.deleteAdmSite(ctSite.getSiteId(), queryQueue);
					
					//자료(파일) DB 삭제
//					deletedFileList = ctFileSvc.deleteCtFile(String.valueOf(ctDebr.getDebrId()), queryQueue);
//					deleteFileListInList.add(deletedFileList);
					}
				}else if("SURV".equalsIgnoreCase(fncMngNm)){
					CtSurvBVo ctSurvBVo = new CtSurvBVo();
					ctSurvBVo.setStrtDt(strtDt);
					ctSurvBVo.setEndDt(endDt);
					if(!"".equals(compId)) ctSurvBVo.setCompId(compId);//회사코드 추가
					@SuppressWarnings("unchecked")
					List<CtSurvBVo> ctSiteList = (List<CtSurvBVo>) commonDao.queryList(ctSurvBVo);
					
					for(CtSurvBVo ctSurv: ctSiteList){
						
						//CoolSite 게시물 내용 삭제
						ctSurvSvc.deleteSurv(ctSurv.getSurvId(), queryQueue, request);
						
						//자료(파일) DB 삭제
//							deletedFileList = ctFileSvc.deleteCtFile(String.valueOf(ctDebr.getDebrId()), queryQueue);
//							deleteFileListInList.add(deletedFileList);
						}
					
				}else if("SCHDL".equalsIgnoreCase(fncMngNm)){
					CtSchdlBVo ctSchdlBVo = new CtSchdlBVo();
					ctSchdlBVo.setStrtDt(strtDt);
					ctSchdlBVo.setEndDt(endDt);
					if(!"".equals(compId)) ctSchdlBVo.setCompId(compId);//회사코드 추가
					@SuppressWarnings("unchecked")
					List<CtSchdlBVo> ctSchdlList = (List<CtSchdlBVo>) commonDao.queryList(ctSchdlBVo);

					for(CtSchdlBVo ctSchdl: ctSchdlList){
						
						//일정 참석자 포함 모두 삭제
						ctScdManagerSvc.deleteSchdl(ctSchdl.getSchdlId(), queryQueue, request);
						
						//자료(파일) DB 삭제
						deletedFileList = ctFileSvc.deleteCtFile(String.valueOf(ctSchdl.getSchdlId()), queryQueue);
						deleteFileListInList.add(deletedFileList);
					}
				
				}
				
							
				commonDao.execute(queryQueue);
				for(List<CommonFileVo> FileList : deleteFileListInList){
					//게시물 첨부파일 삭제
					ctFileSvc.deleteDiskFiles(FileList);
				}
				
				//cm.msg.del.success=삭제 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
				model.put("result", "ok");
				model.put("fncMng", fncMng);
//				model.put("dateSelect", dateSelect);
				model.put("strtDt", strtDt);
				model.put("endDt", endDt);

			}
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		return JsonUtil.returnJson(model);
	}
	
	
	/** 정보변경 > 커뮤니티 완전삭제(DB) */
	@RequestMapping(value = "/ct/adm/transDelCt")
	public String transDelCt(HttpServletRequest request,
			@RequestParam(value = "delCtId", required = false) String delCtId,
			ModelMap model
		) throws Exception{
		
		try {
			if (delCtId == null || "".equals(delCtId)) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			
			// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			//삭제할 커뮤니티 ID
			CtEstbBVo ctEstbBVo = null;
			String msgCode = "cm.msg.del.success";
			if(delCtId != null && !delCtId.isEmpty() ){
				String[] delCtIds = delCtId.split(",");
				boolean flag = true;
				List<List<CommonFileVo>> deleteFileListInList = new ArrayList<List<CommonFileVo>>();
				for(String id : delCtIds){
					ctEstbBVo = new CtEstbBVo();
					ctEstbBVo.setCtId(id);
					ctEstbBVo.setCompId(userVo.getCompId());
					flag = ctCmntSvc.deleteAllCt(request, ctEstbBVo, queryQueue,deleteFileListInList);
					if(!flag) break;
				}
				if(!flag) msgCode = "cm.msg.del.fail";
				else {
					commonSvc.execute(queryQueue);
					for(List<CommonFileVo> FileList : deleteFileListInList){
						//게시물 첨부파일 삭제
						ctFileSvc.deleteDiskFiles(FileList);
					}
				}
			}
			
			model.put("message", messageProperties.getMessage(msgCode, request));
			model.put("todo", "parent.location.replace('" + listPage + "');");
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	
	/** 정보변경 > 커뮤니티 추천취소(DB) */
	@RequestMapping(value = "/ct/adm/transRecmdCancelCt")
	public String transRecmdCancelCt(HttpServletRequest request,
			@RequestParam(value = "selCtId", required = false) String selCtId,
			ModelMap model
		) throws Exception{
		
		try {
			if (selCtId == null || "".equals(selCtId)) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			
			// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			//삭제할 커뮤니티 ID
			CtEstbBVo ctEstbBVo = null;
			if(selCtId != null && !selCtId.isEmpty() ){
				String[] selCtIds = selCtId.split(",");
				for(String id : selCtIds){
					ctEstbBVo = new CtEstbBVo();
					ctEstbBVo.setCtId(id);
					ctEstbBVo.setCompId(userVo.getCompId());
					ctEstbBVo.setRecmdYn("N");//추천취소
					queryQueue.update(ctEstbBVo);
				}
			}
			commonSvc.execute(queryQueue);
			//추천이 취소되었습니다.
			model.put("message", messageProperties.getMessage("ct.msg.recmdCancel.success", request));
			model.put("todo", "parent.location.replace('" + listPage + "');");
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	
}
