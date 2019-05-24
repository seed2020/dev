package com.innobiz.orange.web.ct.ctrl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.ct.svc.CtAdmNotcSvc;
import com.innobiz.orange.web.ct.svc.CtCmSvc;
import com.innobiz.orange.web.ct.svc.CtCmntSvc;
import com.innobiz.orange.web.ct.svc.CtFileSvc;
import com.innobiz.orange.web.ct.svc.CtSurvSvc;
import com.innobiz.orange.web.ct.vo.CtEstbBVo;
import com.innobiz.orange.web.ct.vo.CtFileDVo;
import com.innobiz.orange.web.ct.vo.CtFncDVo;
import com.innobiz.orange.web.ct.vo.CtQuesExamDVo;
import com.innobiz.orange.web.ct.vo.CtSurvBVo;
import com.innobiz.orange.web.ct.vo.CtSurvQuesDVo;
import com.innobiz.orange.web.ct.vo.CtSurvReplyDVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
//import com.innobiz.orange.web.ct.vo.CtSurvPolcDVo;

/** 설문조사 */
@Controller
public class CtVoteCtrl {
	
//	private static final Logger LOGGER = Logger.getLogger(CtVoteCtrl.class);
//	
//	/** 공통 DB 처리용 서비스 */
//	@Resource(name = "commonSvc")
//	private CommonSvc commonSvc;
//	
	/** 공통 서비스 */
	@Autowired
	private CtSurvSvc ctSurvSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
//	/** 조직 공통 서비스 */
//	@Autowired
//	private OrCmSvc orCmSvc;
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 첨부파일 서비스 */
	@Autowired
	private CtFileSvc ctFileSvc;
	
	/** 설문 공통 서비스 */
	@Autowired
	private CtCmntSvc ctCmntSvc;
	
	/** 설문 공통 서비스 */
	@Autowired
	private CtCmSvc ctCmSvc;
	
	/** 관리자 커뮤니티 공지사항 서비스 */
	@Autowired
	private CtAdmNotcSvc ctAdmNotcSvc;
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(CtVoteCtrl.class);
	
//	
//	/** 업로드 메니저 */
//	@Resource(name = "uploadManager")
//	private UploadManager uploadManager;
//	
//	/** 배포 매니저 */
//	@Resource(name = "distManager")
//	private DistManager distManager;
//	
//	/** 메세지 */
//	@Autowired
//    private MessageProperties messageProperties;
//	
//	public String listSurv(HttpServletRequest request, ModelMap model){
//	
//	CtSurvBVo ctSurvBVo = new CtSurvBVo();
//	VoUtil.bind(request, ctSurvBVo);
//	return null;
//	}
	
	/** 나의 신청중인 설문 view */
	@RequestMapping(value = "/ct/viewSurvApvd")
	public String viewMySurvApvd(HttpServletRequest request, ModelMap model)throws Exception{
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
		
		String survId = request.getParameter("survId");
		
		CtSurvBVo ctsVo =  new CtSurvBVo();
		ctsVo.setSurvId(survId);
		VoUtil.bind(request, ctsVo);
		//설문기본
		ctsVo = (CtSurvBVo) commonDao.queryVo(ctsVo);
		if(ctsVo != null){
			
			//설문질문상세
			CtSurvQuesDVo ctsQueVo = new CtSurvQuesDVo();
			ctsQueVo.setSurvId(ctsVo.getSurvId());
			ctsQueVo.setCompId(ctEstbBVo.getCompId());
			ctsQueVo.setOrderBy("QUES_SORT_ORDR");

			@SuppressWarnings("unchecked")
			List<CtSurvQuesDVo> ctsQueList = (List<CtSurvQuesDVo>) commonDao.queryList(ctsQueVo);
			
			List<CtQuesExamDVo> returnWcQueExamList = new ArrayList<CtQuesExamDVo>();
			
			for(CtSurvQuesDVo ctsQVo : ctsQueList){
				//질문보기상세
				CtQuesExamDVo ctQueExamVo =  new CtQuesExamDVo();
				ctQueExamVo.setSurvId(ctsQVo.getSurvId());
				ctQueExamVo.setCompId(ctEstbBVo.getCompId());
				ctQueExamVo.setQuesId(ctsQVo.getQuesId());
				@SuppressWarnings("unchecked")
				List<CtQuesExamDVo> ctQueExamList = (List<CtQuesExamDVo>) commonDao.queryList(ctQueExamVo);
				for(CtQuesExamDVo ctQVo : ctQueExamList){
					returnWcQueExamList.add(ctQVo);
				}
			}
			model.put("ctQueExamList", returnWcQueExamList);
			model.put("ctsQueList", ctsQueList);
			model.put("ctsVo", ctsVo);	
		}
		return LayoutUtil.getJspPath("/ct/viewSurvApvd");
	}
	
	/** 나의 신청중인 설문 페이지 */
	@RequestMapping(value = "/ct/listMySurvApvd")
	public String listMySurvApvd(HttpServletRequest request, ModelMap model)throws Exception{
		// set, list 로 시작하는 경우 처리
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		// 조회조건 매핑
		CtSurvBVo ctSurvBVo = new CtSurvBVo();
		VoUtil.bind(request, ctSurvBVo);
		ctSurvBVo.setLogUserUid(userVo.getUserUid());
		ctSurvBVo.setLogUserDeptId(userVo.getDeptId());
		ctSurvBVo.setRegrUid(userVo.getUserUid());
		ctSurvBVo.setLangTyp(langTypCd);
		/*===============================================================
		 * 설문진행코드 : 1.준비중, 2.승인중, 3.진행중, 4.마감, 5.미저장, 6.임시저장, 9.반려
		 ================================================================*/
		List<String> survList=new ArrayList<String>();
		survList.add("1");
		survList.add("2");
		survList.add("6");
		survList.add("9");
		ctSurvBVo.setSurvSearchList(survList);
		ctSurvBVo.setQueryLang(langTypCd);
		
		Integer recodeCount = commonDao.count(ctSurvBVo);
		PersonalUtil.setPaging(request, ctSurvBVo, recodeCount);
		@SuppressWarnings("unchecked")
		List<CtSurvBVo> ctSurvList = (List<CtSurvBVo>) commonDao.queryList(ctSurvBVo);
		
		model.put("recodeCount", recodeCount);
		model.put("ctSurvList", ctSurvList);
		
//		Map<String,Object> rsltMap = ctSurvSvc.getCtSurvMapList(request, ctSurvBVo);
//		
//		model.put("recodeCount", rsltMap.get("recodeCount"));
//		model.put("ctSurvBMapList", rsltMap.get("ctSurvBMapList"));
		
		return LayoutUtil.getJspPath("/ct/listMySurvApvd");
	}
	
	/** 마감변경 저장 */
	@RequestMapping(value = "/ct/surv/transEndModSave")
	public String transEndModSave(HttpServletRequest request, ModelMap model)throws Exception{
		String survId =  request.getParameter("survId");
		String endYn = request.getParameter("endYn");
		String ctId = request.getParameter("ctId");
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy-MM-dd 00:00:00", Locale.KOREA );
		Date currentTime = new Date ( );
		String mTime = mSimpleDateFormat.format ( currentTime );
		
		CtSurvBVo ctSurvBVo = new CtSurvBVo();
		ctSurvBVo.setCtId(ctId);
		ctSurvBVo.setSurvId(survId);
		ctSurvBVo = (CtSurvBVo) commonDao.queryVo(ctSurvBVo);
		
		if(endYn.equals("Y")){
			ctSurvBVo.setSurvEndDt(mTime);
		}else{
			ctSurvBVo.setSurvEndDt(request.getParameter("finDt") + " 23:59:59");
		}
		
		commonDao.update(ctSurvBVo);
		
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		model.put("todo", "location.replace('/ct/surv/listSurv.do?menuId="+request.getParameter("menuId")
				+ "&ctId=" + ctId
				+"');");
		
		//공통 처리 페이지
		return LayoutUtil.getResultJsp();
	}
	
	/** 마감 변경 POP */
	@RequestMapping(value= "/ct/surv/setEndModPop")
	public String setEndModPop(HttpServletRequest request, ModelMap model)throws Exception{
		String survId = request.getParameter("survId");
		
		CtSurvBVo ctsVo = new CtSurvBVo();
		ctsVo.setSurvId(survId);
		ctsVo = (CtSurvBVo) commonDao.queryVo(ctsVo);
		
		model.put("ctsVo", ctsVo);
		
		return LayoutUtil.getJspPath("/ct/surv/setEndModPop");
	}
	
	/** 설문 목록 */
//	@RequestMapping(value = "/ct/listSurv")
//	public String listSurv(HttpServletRequest request, ModelMap model) throws Exception {
//		
//		// set, list 로 시작하는 경우 처리
//		// 세션의 언어코드
//		String langTypCd = LoginSession.getLangTypCd(request);
//		// 세션의 사용자 정보
//		UserVo userVo = LoginSession.getUser(request);
//		// 조회조건 매핑
//		CtSurvBVo ctSurvBVo = new CtSurvBVo();
//		VoUtil.bind(request, ctSurvBVo);
//		ctSurvBVo.setLogUserUid(userVo.getUserUid());
//		ctSurvBVo.setLogUserDeptId(userVo.getDeptId());
//		ctSurvBVo.setRegrUid(userVo.getUserUid());
//		ctSurvBVo.setLangTyp(langTypCd);
//		List<String> survList=new ArrayList<String>();
//		survList.add("3");
//		survList.add("4");
//		ctSurvBVo.setSurvSearchList(survList);
//		ctSurvBVo.setQueryLang(langTypCd);
//		Map<String,Object> rsltMap = ctSurvSvc.getCtSurvMapList(request, ctSurvBVo);
//		model.put("recodeCount", rsltMap.get("recodeCount"));
//		model.put("ctSurvBMapList", rsltMap.get("ctSurvBMapList"));
//		model.put("logUserUid", ctSurvBVo.getLogUserUid());
//		model.put("logUserDeptId", ctSurvBVo.getLogUserDeptId());
//			
//		return LayoutUtil.getJspPath("/ct/listSurv");
//	}
	
	/** 설문 등록 페이지 */
	@RequestMapping(value= "/ct/surv/setSurv")
	public String setSurv(HttpServletRequest request, ModelMap model) throws Exception {
		
		String survId = request.getParameter("survId");
		String fnc = request.getParameter("fnc");
		String fncUid = request.getParameter("menuId");
		
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		//String authType = null;
		//String authGrdCode = null;
		
		//설문아이디가 있다면 수정 모드
		if(survId != null && fnc.equalsIgnoreCase("mod")){
			CtSurvBVo ctsVo = new CtSurvBVo();
			ctsVo.setSurvId(survId);
			ctsVo.setCompId(ctEstbBVo.getCompId());
			
			ctsVo = (CtSurvBVo) commonDao.queryVo(ctsVo);
			
			if(ctsVo.getSurvTgtCd() != null){
			
				String[] tgtCd = ctsVo.getSurvTgtCd().split("\\/");
				
				for(int i=0; i < tgtCd.length; i++){
					
					if(tgtCd[i].equalsIgnoreCase("M")) ctsVo.setSurvTgtM(tgtCd[i]);
					if(tgtCd[i].equalsIgnoreCase("S")) ctsVo.setSurvTgtS(tgtCd[i]);
					if(tgtCd[i].equalsIgnoreCase("R")) ctsVo.setSurvTgtR(tgtCd[i]);
					if(tgtCd[i].equalsIgnoreCase("A")) ctsVo.setSurvTgtA(tgtCd[i]);
					
				}
			}
			//========================================================================			
			//투표권한 : W
			//부서  : D
			//사용자  : U
			//========================================================================
			
			//투표권한 부서 조회
//			authGrdCode = "W";
//			authType = "D";
//			List<CtSurvUseAuthDVo> voteTgtDeptList = ctSurvSvc.getAuthList(userVo, ctsVo, authType, authGrdCode);
			
//			model.put("voteTgtDeptList", voteTgtDeptList);
//			model.put("voteTgtDeptCnt", voteTgtDeptList.size());
			
			//투표권한 사용자 조회
//			authType = "U";
			
//			List<CtSurvUseAuthDVo> voteTgtUserList = ctSurvSvc.getAuthList(userVo, ctsVo, authType, authGrdCode);
			
//			model.put("voteTgtUserList", voteTgtUserList);
//			model.put("voteTgtUserCnt", voteTgtUserList.size());
			//========================================================================
			//조회권한  : R
			//부서 : D
			//사용자 : U
			//========================================================================
			//조회권한 부서 조회
//			authGrdCode = "R";
//			authType = "D";
			
			
//			List<CtSurvUseAuthDVo> chkTgtDeptList = ctSurvSvc.getAuthList(userVo, ctsVo, authType, authGrdCode);
//			
//			model.put("chkTgtDeptList", chkTgtDeptList);
//			model.put("chkTgtDeptCnt", chkTgtDeptList.size());
			
			//조회권한 사용자 조회			
//			authType = "U";
//			List<CtSurvUseAuthDVo> chkTgtUserList = ctSurvSvc.getAuthList(userVo, ctsVo, authType, authGrdCode);
			
//			model.put("chkTgtUserList", chkTgtUserList);
//			model.put("chkTgtUserCnt", chkTgtUserList.size());
			//========================================================================
			
			
			model.put("ctsVo", ctsVo);
			
			
			
			ctFileSvc.putFileListToModel(ctsVo.getSurvId(), model, userVo.getCompId());
		}
		
		
		
//		String chkVoteDpt = request.getParameter("voteTgtDeptIdList");
//		String chkVoteUser = request.getParameter("voteTgtUserIdList");
//		String chkChekDpt = request.getParameter("chkTgtDeptList");
//		String chkChekUser = request.getParameter("chkTgtUserList");
		
//		if(chkVoteDpt!=null){
//			String voteTgtDeptArr[] = request.getParameter("voteTgtDeptIdList").split("\\,");
//			List<CtSurvUseAuthDVo> voteTgtDeptList = new ArrayList<CtSurvUseAuthDVo>();
//			for(String voteTgtDeptVo : voteTgtDeptArr){
//				CtSurvUseAuthDVo ctSurvUseAuthDVo = new CtSurvUseAuthDVo();
//				ctSurvUseAuthDVo.setAuthTgtUid(voteTgtDeptVo);
//				if(!voteTgtDeptVo.equalsIgnoreCase("") && voteTgtDeptVo != null){
//					voteTgtDeptList.add(ctSurvUseAuthDVo);
//				}
//			}
//			model.put("voteTgtDeptList", voteTgtDeptList);
//			model.put("voteTgtDeptCnt", voteTgtDeptList.size());
//		}
//		
//		if(chkVoteUser!=null){
//			String voteTgtUserArr[] = request.getParameter("voteTgtUserIdList").split("\\,");
//			List<CtSurvUseAuthDVo> voteTgtUserList = new ArrayList<CtSurvUseAuthDVo>();
//			for(String voteTgtUserVo : voteTgtUserArr){
//				CtSurvUseAuthDVo ctSurvUseAuthDVo = new CtSurvUseAuthDVo();
//				ctSurvUseAuthDVo.setAuthTgtUid(voteTgtUserVo);
//				if(!voteTgtUserVo.equalsIgnoreCase("") && voteTgtUserVo != null){
//						voteTgtUserList.add(ctSurvUseAuthDVo);
//				}
//			}
//			model.put("voteTgtUserList", voteTgtUserList);
//			model.put("voteTgtUserCnt", voteTgtUserList.size());
//		}
//		
		
//		if(chkChekDpt!=null){
//			String chkTgtDeptArr[] = request.getParameter("chkTgtDeptList").split("\\,");
//			List<CtSurvUseAuthDVo> chkTgtDeptList = new ArrayList<CtSurvUseAuthDVo>();
//			for(String chkTgtDeptVo : chkTgtDeptArr){
//				CtSurvUseAuthDVo ctSurvUseAuthDVo = new CtSurvUseAuthDVo();
//				ctSurvUseAuthDVo.setAuthTgtUid(chkTgtDeptVo);
//				if(!chkTgtDeptVo.equalsIgnoreCase("") && chkTgtDeptVo != null){
//					chkTgtDeptList.add(ctSurvUseAuthDVo);
//				}
//			}
//			model.put("chkTgtDeptList", chkTgtDeptList);
//			model.put("chkTgtDeptCnt", chkTgtDeptList.size());
//		}
//		
		
//		if(chkChekUser!=null){
//			String chkTgtUserArr[] = request.getParameter("chkTgtUserList").split("\\,");
//			List<CtSurvUseAuthDVo> chkTgtUserList = new ArrayList<CtSurvUseAuthDVo>();
//			for(String chkTgtUserVo : chkTgtUserArr){
//				CtSurvUseAuthDVo ctSurvUseAuthDVo = new CtSurvUseAuthDVo();
//				ctSurvUseAuthDVo.setAuthTgtUid(chkTgtUserVo);
//				if(!chkTgtUserVo.equalsIgnoreCase("") && chkTgtUserVo != null){
//					chkTgtUserList.add(ctSurvUseAuthDVo);
//				}
//			}
//			model.put("chkTgtUserList", chkTgtUserList);
//			model.put("chkTgtUserCnt", chkTgtUserList.size());
//		}
//		
//		CtSurvBVo ctSurvBVo = new CtSurvBVo();
//		String setSurvStartDt = null;
//		String setSurvEndDt = null;
//		if(request.getParameter("survId") != null){
//			ctSurvBVo.setSurvId(request.getParameter("survId"));
//			ctSurvBVo = (CtSurvBVo) commonDao.queryVo(ctSurvBVo);
//			setSurvStartDt = ctSurvBVo.getSurvStartDt().substring(0, 10);
//			setSurvEndDt = ctSurvBVo.getSurvEndDt().substring(0, 10);
//		}
//		
//		if(flagFile != null){
//			flagFile ="mod";
//		}else{
//			flagFile ="set";
//		}
//		model.put("flagFile", flagFile);
//		model.put("ctSurvBVo", ctSurvBVo);
//		model.put("rsltPubYn", ctSurvBVo.getOpenYn());
//		model.put("resurvYn", ctSurvBVo.getRepetSurvYn());
//		model.put("survStartDt", setSurvStartDt);
//		model.put("survEndDt", setSurvEndDt);
//		ctFileSvc.putFileListToModel(ctSurvBVo.getSurvId(), model);
		checkPath(request, "index", model);
		model.put("survId", survId);
		model.put("fnc", fnc);
		
		//왼쪽 메뉴 
		ctCmntSvc.putLeftMenu(request, ctId, model);
		 
		//권한 W=등록
		ctCmntSvc.putAuthChk(request, model, "W", ctId, fncUid);
		
		// 최대 본문 사이즈 model에 추가
		ctAdmNotcSvc.putBodySizeToModel(request, model);
		
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

		
		return LayoutUtil.getJspPath("/ct/surv/setSurv");
		
		
	}
	
	
	/** 설문에 대한 질문 등록 페이지 */
	@RequestMapping(value= "/ct/surv/transSetSurvQuesNextSave")
	public String transSetSurvQuesNextSave(HttpServletRequest request, ModelMap model) throws Exception {
		
		UploadHandler uploadHandler = null;	
		
		try {	// Multipart 파일 업로드

			uploadHandler = ctFileSvc.upload(request);

			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			//String flagFile =null;
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
			
			CtSurvBVo ctSurvBVo = new CtSurvBVo();
			//VoUtil.bind(request, ctSurvBVo);
			QueryQueue queryQueue = new QueryQueue();
			ctSurvBVo.setQueryLang(langTypCd);
			ctSurvBVo.setCompId(ctEstbBVo.getCompId());
			ctSurvBVo.setRegrUid(userVo.getUserUid());
			
			
			String fnc = request.getParameter("fnc");
			
			
			ctSurvSvc.setCtSurvList(request, ctSurvBVo, queryQueue, userVo, model, fnc, ctId, fncUid);
			
//			if(!fnc.equalsIgnoreCase("view")){
//				// 설문에 대한 사용권한설정 DB입력 **************************************************** (시작)
//				String voteTgtDeptArr[] = request.getParameter("voteTgtDeptIds").split("\\,");
//				String voteTgtUserArr[] = request.getParameter("voteTgtUserIds").split("\\,");
//				String chkTgtDeptArr[] = request.getParameter("chkTgtDeptIds").split("\\,");
//				String chkTgtUserArr[] = request.getParameter("chkTgtUserIds").split("\\,");
				
//				CtSurvUseAuthDVo useAuthDVo = new CtSurvUseAuthDVo();
//				useAuthDVo.setSurvId(ctSurvBVo.getSurvId());
				
//				queryQueue.delete(useAuthDVo);
				
//				if(!voteTgtDeptArr[0].equalsIgnoreCase("")){
//					for(int i=0; i<voteTgtDeptArr.length; i++){
//						CtSurvUseAuthDVo ctSurvUseAuthDVo = new CtSurvUseAuthDVo();
//						ctSurvUseAuthDVo.setCompId(ctSurvBVo.getCompId());
//						ctSurvUseAuthDVo.setSurvId(ctSurvBVo.getSurvId());
//						ctSurvUseAuthDVo.setAuthTgtTypCd("D");
//						ctSurvUseAuthDVo.setAuthInclYn("N");
//						ctSurvUseAuthDVo.setAuthGradCd("W");
//						ctSurvUseAuthDVo.setAuthTgtUid(voteTgtDeptArr[i]);
//						queryQueue.insert(ctSurvUseAuthDVo);
//					}
//				}
//				if(!voteTgtUserArr[0].equalsIgnoreCase("")){
//					for(int i=0; i<voteTgtUserArr.length; i++){
//						CtSurvUseAuthDVo ctSurvUseAuthDVo = new CtSurvUseAuthDVo();
//						ctSurvUseAuthDVo.setCompId(ctSurvBVo.getCompId());
//						ctSurvUseAuthDVo.setSurvId(ctSurvBVo.getSurvId());
//						ctSurvUseAuthDVo.setAuthTgtTypCd("U");
//						ctSurvUseAuthDVo.setAuthInclYn("N");
//						ctSurvUseAuthDVo.setAuthGradCd("W");
//						ctSurvUseAuthDVo.setAuthTgtUid(voteTgtUserArr[i]);
//						queryQueue.insert(ctSurvUseAuthDVo);
//					}
//				}
//				if(!chkTgtDeptArr[0].equalsIgnoreCase("")){
//					for(int i=0; i<chkTgtDeptArr.length; i++){
//						CtSurvUseAuthDVo ctSurvUseAuthDVo = new CtSurvUseAuthDVo();
//						ctSurvUseAuthDVo.setCompId(ctSurvBVo.getCompId());
//						ctSurvUseAuthDVo.setSurvId(ctSurvBVo.getSurvId());
//						ctSurvUseAuthDVo.setAuthTgtTypCd("D");
//						ctSurvUseAuthDVo.setAuthInclYn("N");
//						ctSurvUseAuthDVo.setAuthGradCd("R");
//						ctSurvUseAuthDVo.setAuthTgtUid(chkTgtDeptArr[i]);
//						queryQueue.insert(ctSurvUseAuthDVo);
//					}
//				}
//			
//				if(!chkTgtUserArr[0].equalsIgnoreCase("")){
//					for(int i=0; i<chkTgtUserArr.length; i++){
//						CtSurvUseAuthDVo ctSurvUseAuthDVo = new CtSurvUseAuthDVo();
//						ctSurvUseAuthDVo.setCompId(ctSurvBVo.getCompId());
//						ctSurvUseAuthDVo.setSurvId(ctSurvBVo.getSurvId());
//						ctSurvUseAuthDVo.setAuthTgtTypCd("U");
//						ctSurvUseAuthDVo.setAuthInclYn("N");
//						ctSurvUseAuthDVo.setAuthGradCd("R");
//						ctSurvUseAuthDVo.setAuthTgtUid(chkTgtUserArr[i]);
//						queryQueue.insert(ctSurvUseAuthDVo);
//					}
//				}
				
				
				
				
//				
//			}
			// 설문에 대한 사용권한설정 DB입력 **************************************************** (끝)
		
		commonDao.execute(queryQueue);
		//왼쪽 메뉴 
		ctCmntSvc.putLeftMenu(request, ctId, model);
		
		
		//model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		model.put("todo", "parent.location.replace('/ct/surv/setSurvQues.do?menuId="+request.getParameter("menuId")
				+ "&fnc="+request.getParameter("fnc")
				+ "&survId=" + ctSurvBVo.getSurvId()
				+ "&ctId=" + ctId
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
	
	
	/** 설문에 대한 질문 등록 페이지 */
	@RequestMapping(value= "/ct/surv/setSurvQues")
	public String setSurvQues(HttpServletRequest request, ModelMap model) throws Exception {

		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		String survId = request.getParameter("survId");
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		String fncUid = request.getParameter("menuId");
		
		
		if(survId != null && !survId.equalsIgnoreCase("")){	
			
			CtSurvBVo ctSurvBVo = new CtSurvBVo();
			ctSurvBVo.setSurvId(survId);
			
			ctSurvBVo = (CtSurvBVo) commonDao.queryVo(ctSurvBVo);
			
			//설문질문상세
			CtSurvQuesDVo ctsQueVo = new CtSurvQuesDVo();
			ctsQueVo.setSurvId(survId);
			ctsQueVo.setCompId(ctEstbBVo.getCompId());
			ctsQueVo.setOrderBy("QUES_SORT_ORDR");
			@SuppressWarnings("unchecked")
			List<CtSurvQuesDVo> ctsQueList = (List<CtSurvQuesDVo>) commonDao.queryList(ctsQueVo);
			
			List<CtQuesExamDVo> returnWcQueExamList = new ArrayList<CtQuesExamDVo>();
			
			for(CtSurvQuesDVo ctsQVo : ctsQueList){
				//질문보기상세
				CtQuesExamDVo ctQueExamVo =  new CtQuesExamDVo();
				ctQueExamVo.setSurvId(ctsQVo.getSurvId());
				ctQueExamVo.setCompId(ctEstbBVo.getCompId());
				ctQueExamVo.setQuesId(ctsQVo.getQuesId());
				@SuppressWarnings("unchecked")
				List<CtQuesExamDVo> ctQueExamList = (List<CtQuesExamDVo>) commonDao.queryList(ctQueExamVo);
				for(CtQuesExamDVo ctQVo : ctQueExamList){
					returnWcQueExamList.add(ctQVo);
				}
			}
			
			//커뮤니티 왼쪽 메뉴
			ctCmntSvc.putLeftMenu(request, ctId, model);
			
			//권한 W=등록 
			ctCmntSvc.putAuthChk(request, model, "W", ctId, fncUid);
			
			model.put("ctSurvBVo", ctSurvBVo);
			model.put("returnWcQueExamList", returnWcQueExamList);
			model.put("ctsQueList", ctsQueList);
			model.put("survId", survId);
		}

		// set, list 로 시작하는 경우 처리
		checkPath(request, "set", model);
		
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
		
		return LayoutUtil.getJspPath("/ct/surv/setSurvQues");
		
		
	}
	
	/** 객관식 질문 팝업*/
	@RequestMapping(value= "/ct/surv/setMulcQuesPop")
	public String setMulcQuesPop(HttpServletRequest request, ModelMap model) throws Exception {
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
				
		String quesId = request.getParameter("quesId");
		String fnc = request.getParameter("fnc");
		String survId = request.getParameter("survId");
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		String fncUid = request.getParameter("menuId");
		
		if(fnc.equalsIgnoreCase("mod")){
			CtSurvQuesDVo ctsQuesVo = new CtSurvQuesDVo();
			ctsQuesVo.setSurvId(survId);
			ctsQuesVo.setQuesId(quesId);
			ctsQuesVo.setCompId(ctEstbBVo.getCompId());
			
			ctsQuesVo = (CtSurvQuesDVo) commonDao.queryVo(ctsQuesVo);
			
			CtQuesExamDVo ctQExamVo = new CtQuesExamDVo();
			
			ctQExamVo.setSurvId(survId);
			ctQExamVo.setCompId(ctEstbBVo.getCompId());
			ctQExamVo.setQuesId(quesId);
			@SuppressWarnings("unchecked")
			List<CtQuesExamDVo> ctQExamList = (List<CtQuesExamDVo>) commonDao.queryList(ctQExamVo);
			
			
			model.put("ctQExamList", ctQExamList);
			model.put("ctsQuesVo", ctsQuesVo);
		}
		model.put("survId", survId);
		model.put("quesId", quesId);
		model.put("ctId", ctId);
		checkPath(request, "index", model);
		//권한 W=등록
		ctCmntSvc.putAuthChk(request, model, "W", ctId, fncUid);
		//권한 R=읽기
		ctCmntSvc.putAuthChk(request, model, "R", ctId, fncUid);
		return LayoutUtil.getJspPath("/ct/surv/setMulcQuesPop");
	}
	
	/** 주관식 질문 팝업*/
	@RequestMapping(value= "/ct/surv/setOendQuesPop")
	public String setOendQuesPop(HttpServletRequest request, ModelMap model) throws Exception {
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		
		String survId = request.getParameter("survId");
		String quesId = request.getParameter("quesId");
		//String fnc = request.getParameter("fnc");
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		String fncUid = request.getParameter("menuId");
		
		
		CtSurvQuesDVo ctSurvQuesDVo = new CtSurvQuesDVo();
		VoUtil.bind(request, ctSurvQuesDVo);
		
		ctSurvQuesDVo.setSurvId(survId);
		ctSurvQuesDVo.setQuesId(quesId);
		ctSurvQuesDVo.setCompId(ctEstbBVo.getCompId());
		ctSurvQuesDVo = (CtSurvQuesDVo) commonDao.queryVo(ctSurvQuesDVo);
		
		model.put("ctSurvBVo", ctSurvQuesDVo);
		model.put("survId", survId);
		model.put("quesId", quesId);
		model.put("ctId", ctId);
		
		//권한 W=등록
		ctCmntSvc.putAuthChk(request, model, "W", ctId, fncUid);
		//권한 R=읽기
		ctCmntSvc.putAuthChk(request, model, "R", ctId, fncUid);

		return LayoutUtil.getJspPath("/ct/surv/setOendQuesPop");
	}

	private void checkPath(HttpServletRequest request, String path1,
			ModelMap model) throws SQLException {
		// listXXX 이면
		// 페이지 정보 세팅
		if (path1.startsWith("list") || path1.equals("index")) {
//			CommonVo commonVo = new CommonVoImpl();
//			PersonalUtil.setPaging(request, commonVo, ctSurvSvc.getSurvSurvey().size());
//			model.put("recodeCount", ctSurvSvc.getSurvSurvey().size());
		}
		
		// setXXX 이면
		// 에디터 사용
		if (path1.startsWith("set") || path1.equals("index")) {
			model.addAttribute("JS_OPTS", new String[]{"editor"});
			
		}
	}
	
	/** 설문참여 */
	@RequestMapping(value= "/ct/surv/viewSurv")
	public String viewSurv(HttpServletRequest request, ModelMap model) throws Exception {
		
//		CtSurvBVo ctSurvBVo = new CtSurvBVo();
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String survId = request.getParameter("survId");
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		CtSurvBVo ctsVo =  new CtSurvBVo();
		ctsVo.setSurvId(survId);
		VoUtil.bind(request, ctsVo);
		
		//파일 추가
//		ctFileSvc.putFileListToModel(survId, model);
		
		//설문기본
		ctsVo = (CtSurvBVo) commonDao.queryVo(ctsVo);
		if(ctsVo != null){
						
			//설문질문상세
			CtSurvQuesDVo ctsQueVo = new CtSurvQuesDVo();
			ctsQueVo.setSurvId(ctsVo.getSurvId());
			ctsQueVo.setCompId(ctEstbBVo.getCompId());
			ctsQueVo.setOrderBy("QUES_SORT_ORDR");
			
			@SuppressWarnings("unchecked")
			List<CtSurvQuesDVo> ctsQueList = (List<CtSurvQuesDVo>) commonDao.queryList(ctsQueVo);
			
			List<CtQuesExamDVo> returnWcQueExamList = new ArrayList<CtQuesExamDVo>();
			
			for(CtSurvQuesDVo ctsQVo : ctsQueList){
				//질문보기상세
				CtQuesExamDVo ctQueExamVo =  new CtQuesExamDVo();
				ctQueExamVo.setSurvId(ctsQVo.getSurvId());
				ctQueExamVo.setCompId(ctEstbBVo.getCompId());
				ctQueExamVo.setQuesId(ctsQVo.getQuesId());
				@SuppressWarnings("unchecked")
				List<CtQuesExamDVo> ctQueExamList = (List<CtQuesExamDVo>) commonDao.queryList(ctQueExamVo);
				for(CtQuesExamDVo ctQVo : ctQueExamList){
					returnWcQueExamList.add(ctQVo);
				}
				
				//단 라디오버튼일때는 각각 넣어줘야함 나중에 정리 필요함.
				CtSurvReplyDVo ctSurvReplyDVo = new CtSurvReplyDVo();
				ctSurvReplyDVo.setSurvId(survId);
				ctSurvReplyDVo.setReplyrUid(userVo.getUserUid());
				ctSurvReplyDVo.setQuesId(ctsQVo.getQuesId());
				@SuppressWarnings("unchecked")
				List<CtSurvReplyDVo> survReplyListAdd = (List<CtSurvReplyDVo>) commonDao.queryList(ctSurvReplyDVo);
				ctsQVo.setCtSurvReplyDVo(survReplyListAdd);
			}
			
			CtSurvReplyDVo ctSurvReplyDVo = new CtSurvReplyDVo();
			ctSurvReplyDVo.setSurvId(survId);
			ctSurvReplyDVo.setReplyrUid(userVo.getUserUid());
			@SuppressWarnings("unchecked")
			List<CtSurvReplyDVo> survReplyList = (List<CtSurvReplyDVo>) commonDao.queryList(ctSurvReplyDVo);
			
			//커뮤니티 왼쪽 메뉴
			ctCmntSvc.putLeftMenu(request, ctId, model);
			
			model.put("survReplyList", survReplyList);
			model.put("ctQueExamList", returnWcQueExamList);
			model.put("ctsQueList", ctsQueList);
			model.put("ctsVo", ctsVo);	
			checkPath(request, "set", model);
		}

		return LayoutUtil.getJspPath("/ct/surv/viewSurv");
	}
	/** 통계보기 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value= "/ct/surv/viewSurvRes")
	public String viewSurvRes(HttpServletRequest request, ModelMap model) throws Exception {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String survId = request.getParameter("survId");
//		String openYn = request.getParameter("openYn");
		String repetYn = request.getParameter("repetYn");
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		ctFileSvc.putFileListToModel(survId, model, userVo.getCompId());
		
		CtSurvReplyDVo ctsReVo = new CtSurvReplyDVo();
		
		ctsReVo.setSurvId(survId);
		ctsReVo.setReplyrUid(userVo.getUserUid());
		
//		int survReply = commonDao.count(ctsReVo)!=null?commonDao.count(ctsReVo):0; 
		
//		if(survReply != 0){
//			if(openYn.equalsIgnoreCase("N")){
//				model.put("message", messageProperties.getMessage("ct.msg.survApvd.openFail", request));
//				model.put("todo", "parent.location.replace('/ct/listSurv.do?menuId="+request.getParameter("menuId")
//						+"');");
//				
//				//공통 처리 페이지
//				return LayoutUtil.getResultJsp();
//			}
//		}
		String langTypCd = LoginSession.getLangTypCd(request);
		
		CtSurvBVo ctsVo =  new CtSurvBVo();
		ctsVo.setSurvId(survId);
		ctsVo.setQueryLang(langTypCd);
		VoUtil.bind(request, ctsVo);
		//설문기본
		ctsVo = (CtSurvBVo) commonDao.queryVo(ctsVo);
		if(ctsVo != null){
			
			//설문질문상세
			CtSurvQuesDVo ctsQueVo = new CtSurvQuesDVo();
			ctsQueVo.setSurvId(ctsVo.getSurvId());
			ctsQueVo.setCompId(ctEstbBVo.getCompId());
			ctsQueVo.setOrderBy("QUES_SORT_ORDR");
			
			List<CtSurvQuesDVo> ctsQueList = (List<CtSurvQuesDVo>) commonDao.queryList(ctsQueVo);
			
			List<CtQuesExamDVo> returnWcQueExamList = new ArrayList<CtQuesExamDVo>();
			
			for(CtSurvQuesDVo ctsQVo : ctsQueList){
				//질문보기상세
				CtQuesExamDVo ctQueExamVo =  new CtQuesExamDVo();
				ctQueExamVo.setSurvId(ctsQVo.getSurvId());
				ctQueExamVo.setCompId(ctEstbBVo.getCompId());
				ctQueExamVo.setQuesId(ctsQVo.getQuesId());
				
				List<CtQuesExamDVo> ctQueExamList = (List<CtQuesExamDVo>) commonDao.queryList(ctQueExamVo);
					for(CtQuesExamDVo ctQVo : ctQueExamList){
						
						//질문전체 count
						CtSurvReplyDVo ctReAllCount = new CtSurvReplyDVo();
						ctReAllCount.setCompId(ctEstbBVo.getCompId());
						ctReAllCount.setSurvId(ctQVo.getSurvId());
						ctReAllCount.setQuesId(ctQVo.getQuesId());
						float quesAllCount = commonDao.count(ctReAllCount)!=null?commonDao.count(ctReAllCount):0;

						//질문보기 개당 count
						CtSurvReplyDVo ctReOneCount = new CtSurvReplyDVo();
						ctReOneCount.setCompId(ctEstbBVo.getCompId());
						ctReOneCount.setSurvId(ctQVo.getSurvId());
						ctReOneCount.setQuesId(ctQVo.getQuesId());
						ctReOneCount.setReplyNo(ctQVo.getExamNo());
						//질문보기상세 선택된 개수 count 구하기
						float quesOneCount = commonDao.count(ctReOneCount)!=null?commonDao.count(ctReOneCount):0;
						
						//평균 구하기
						float average = (quesOneCount / quesAllCount) * 100;
						
						ctQVo.setSelectCount(String.valueOf((int)quesOneCount));
						ctQVo.setQuesAverage(String.valueOf((int)average));
						returnWcQueExamList.add(ctQVo);
					}
					
					//주관식 질문  count
					CtSurvReplyDVo ctReOendCount = new CtSurvReplyDVo();
					ctReOendCount.setCompId(ctEstbBVo.getCompId());
					ctReOendCount.setSurvId(ctsQVo.getSurvId());
					ctReOendCount.setQuesId(ctsQVo.getQuesId());
					ctReOendCount.setReplyNo(-1);
					float quesOendCount = commonDao.count(ctReOendCount)!=null?commonDao.count(ctReOendCount):0;
					
					ctsQVo.setOendCount(String.valueOf((int)quesOendCount));
					
					
			}
			
			//커뮤니티 왼쪽 메뉴
			ctCmntSvc.putLeftMenu(request, ctId, model);
			
			model.put("returnWcQueExamList", returnWcQueExamList);
			model.put("ctsQueList", ctsQueList);
			model.put("ctsVo", ctsVo);	
			model.put("repetYn", repetYn);
			model.put("logUserUid", userVo.getUserUid());
			checkPath(request, "set", model);
		}
		
		return LayoutUtil.getJspPath("/ct/surv/viewSurvRes");
	}
	
	/** 설문참여 저장*/
	@RequestMapping(value= "/ct/surv/viewSurvSave")
	public String viewSurvSave(HttpServletRequest request, ModelMap model) throws Exception {
		
//		CtSurvBVo ctSurvBVo = new CtSurvBVo(); 
		QueryQueue queryQueue = new QueryQueue();
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request); 
		String survId = request.getParameter("survId");
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		CtSurvBVo ctsVo = new CtSurvBVo(); 
		ctsVo.setSurvId(survId);
		ctsVo.setCtId(ctId);
		ctsVo.setCompId(ctEstbBVo.getCompId());
		ctsVo = (CtSurvBVo) commonDao.queryVo(ctsVo);
		
		if(ctsVo != null){
			//재설문일시 사용사 답변상세 삭제 후 재 입력
			if(ctsVo.getRepetSurvYn().equalsIgnoreCase("Y")){
				CtSurvReplyDVo ctsReVo = new  CtSurvReplyDVo();
				ctsReVo.setSurvId(survId);
				ctsReVo.setReplyrUid(userVo.getUserUid());
				ctsReVo.setCompId(ctEstbBVo.getCompId());
				queryQueue.delete(ctsReVo);
			}
		}
		
		CtSurvQuesDVo ctsQueVo =  new CtSurvQuesDVo();
		ctsQueVo.setSurvId(survId);
		@SuppressWarnings("unchecked")
		List<CtSurvQuesDVo> returnQuesVo =  (List<CtSurvQuesDVo>) commonDao.queryList(ctsQueVo);
		
  		for(CtSurvQuesDVo ctsQVo : returnQuesVo){
			CtSurvReplyDVo ctsRVo = new CtSurvReplyDVo();
			
			
			ctsRVo.setCompId(ctEstbBVo.getCompId());
			ctsRVo.setQuesId(ctsQVo.getQuesId());
			ctsRVo.setSurvId(ctsQVo.getSurvId());
			ctsRVo.setReplyDt(ctSurvSvc.currentDay());
			ctsRVo.setReplyrUid(userVo.getUserUid());
			ctsRVo.setReplyrDeptId(userVo.getDeptId());
			
			//객관식
			if(ctsQVo.getMulChoiYn() != null){
				if(ctsQVo.getMulChoiYn().equalsIgnoreCase("Y")){
					String[] survQues = request.getParameterValues(ctsQVo.getQuesId());
					String[] survQuesInput = request.getParameterValues("inputCheck"+ctsQVo.getQuesId());
					if(survQues != null){
						for(int i=0; i < survQues.length; i++){
							CtSurvReplyDVo cloneCtSurv =(CtSurvReplyDVo) ctsRVo.clone();
							cloneCtSurv.setReplyNo(Integer.parseInt(survQues[i]));
							if(survQuesInput != null){
									//입력여부Y이면 객관식에 text 가능함.
									cloneCtSurv.setMulcInputReplyCont(survQuesInput[Integer.parseInt(survQues[i])-1]);
							}
							queryQueue.insert(cloneCtSurv);
						}
					}
				}else if(ctsQVo.getMulChoiYn().equalsIgnoreCase("N")){
					String survQues = request.getParameter(ctsQVo.getQuesId());
					String survQuesInput = request.getParameter("inputRadio"+ ctsQVo.getQuesId() + survQues);
					
					if(survQues != null){
						ctsRVo.setReplyNo(Integer.parseInt(survQues));
						if(survQuesInput != null){
							//입력여부Y이면 객관식에 text 가능함.
							ctsRVo.setMulcInputReplyCont(survQuesInput);
						}
						queryQueue.insert(ctsRVo);
					}
				}
			//주관식
			}else{
				String survQues = request.getParameter(ctsQVo.getQuesId());
				
				if(!survQues.isEmpty()){
					ctsRVo.setReplyNo(-1);
					ctsRVo.setOendReplyCont(survQues);
					queryQueue.insert(ctsRVo);
				}
			}
			
		}
		
		commonDao.execute(queryQueue);		
		
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		model.put("todo", "parent.location.replace('/ct/surv/listSurv.do?menuId="+request.getParameter("menuId")
				+ "&ctId=" + ctId
				+"');");
		
		//공통 처리 페이지
		return LayoutUtil.getResultJsp();
	}
	
	/** 부서별 통계 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value= "/ct/listDeptStacsPop")
	public String listDeptStacsPop(HttpServletRequest request, ModelMap model) throws Exception {
		
		String survId = request.getParameter("survId");
		String quesId = request.getParameter("quesId");
		String quesCount = request.getParameter("quesCount");
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		//#######################################################################################
		
		//VoUtil.bind(request, ctsVo);
	
			//설문질문상세 (질문순서때문에 사용함)
			CtSurvQuesDVo ctsQueVo = new CtSurvQuesDVo();
			ctsQueVo.setQuesId(quesId);
			ctsQueVo.setSurvId(survId);
			//ctsQueVo.setCompId(ctEstbBVo.getCompId());
			
			ctsQueVo = (CtSurvQuesDVo) commonDao.queryVo(ctsQueVo);
			
			
			List<CtQuesExamDVo> ctQueExamList = new ArrayList<CtQuesExamDVo>();

			
			//질문보기상세
			CtQuesExamDVo ctQueExamVo =  new CtQuesExamDVo();
			ctQueExamVo.setSurvId(survId);
			//ctQueExamVo.setCompId(ctEstbBVo.getCompId());
			ctQueExamVo.setQuesId(quesId);
			
			CtSurvReplyDVo ctSurvReplyDistinct = new CtSurvReplyDVo();
			ctSurvReplyDistinct.setInstanceQueryId("selectCtSurvReplyDssss");
			ctSurvReplyDistinct.setSurvId(survId);
			//ctSurvReplyDistinct.setCompId(ctEstbBVo.getCompId());
			ctSurvReplyDistinct.setQuesId(quesId);
			
			List<CtSurvReplyDVo> ctSurvReplyDeptList = (List<CtSurvReplyDVo>) commonDao.queryList(ctSurvReplyDistinct);
			
			for(CtSurvReplyDVo ctRe : ctSurvReplyDeptList){
				List<CtQuesExamDVo> returnWcQueExamList = new ArrayList<CtQuesExamDVo>();
				ctQueExamList = (List<CtQuesExamDVo>) commonDao.queryList(ctQueExamVo);
				for(CtQuesExamDVo ctQVo : ctQueExamList){
					
					
						//질문전체 count
						CtSurvReplyDVo ctReAllCount = new CtSurvReplyDVo();
						//ctReAllCount.setCompId(ctEstbBVo.getCompId());
						ctReAllCount.setSurvId(ctQVo.getSurvId());
						ctReAllCount.setQuesId(ctQVo.getQuesId());
						ctReAllCount.setReplyrDeptId(ctRe.getReplyrDeptId());
						float quesAllCount = commonDao.count(ctReAllCount)!=null?commonDao.count(ctReAllCount):0;
		
						//질문보기 개당 count
						CtSurvReplyDVo ctReOneCount = new CtSurvReplyDVo();
						//ctReOneCount.setCompId(ctEstbBVo.getCompId());
						ctReOneCount.setSurvId(ctQVo.getSurvId());
						ctReOneCount.setQuesId(ctQVo.getQuesId());
						ctReOneCount.setReplyNo(ctQVo.getExamNo());
						ctReOneCount.setReplyrDeptId(ctRe.getReplyrDeptId());
						//질문보기상세 선택된 개수 count 구하기
						float quesOneCount = commonDao.count(ctReOneCount)!=null?commonDao.count(ctReOneCount):0;
						
						//평균 구하기
//						float average = (quesOneCount / quesAllCount) * 100;
						
						ctQVo.setSelectCount(String.valueOf((int)quesOneCount));
						ctRe.setDeptTotalCount(String.valueOf((int)quesAllCount));
						returnWcQueExamList.add(ctQVo);
						
						}
					ctRe.setQuesExam(returnWcQueExamList);
			}
			
			model.put("ctSurvReplyDeptList", ctSurvReplyDeptList);
			model.put("ctQueExamList", ctQueExamList);
			model.put("ctsQueVo", ctsQueVo);
			model.put("quesCount",quesCount);
			checkPath(request, "set", model);
	//##############################################################################################
		
		
		checkPath(request, "index", model);
		return LayoutUtil.getJspPath("/ct/listDeptStacsPop");
	}
	
	
	/** 설문 최종 저장*/
	@RequestMapping(value= "/ct/surv/transSetSurvQuesSave")
	public String setSurvQuesSave(HttpServletRequest request, ModelMap model) throws Exception {
		QueryQueue queryQueue = new QueryQueue();
		
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		String survId = request.getParameter("survId");
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		
		CtSurvBVo ctsThisVo = new CtSurvBVo();
		ctsThisVo.setCtId(ctId);
		ctsThisVo.setSurvId(survId);
		ctsThisVo.setCompId(ctEstbBVo.getCompId());
		
//		CtSurvPolcDVo ctsPolcVo = new CtSurvPolcDVo();
//		ctsPolcVo.setCompId(userVo.getCompId());
//		
//		ctsPolcVo = (CtSurvPolcDVo) commonDao.queryVo(ctsPolcVo);
		
//		if(ctsPolcVo.getApvdYn().equalsIgnoreCase("Y")){
//			ctsThisVo.setSurvPrgStatCd("2");
//		}else{
//			ctsThisVo.setSurvPrgStatCd("3");
//		}
		ctsThisVo.setSurvPrgStatCd("3");
		queryQueue.update(ctsThisVo);
		
		
		commonDao.execute(queryQueue);
		
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.location.replace('/ct/surv/listSurv.do?menuId="+request.getParameter("menuId")
					+"&ctId="+ctId
					+"');");
			
		checkPath(request, "index", model);
		return LayoutUtil.getResultJsp();
	}
	
	/** 주관식 답변보기*/
	@RequestMapping(value= "/ct/surv/listOendAnsPop")
	public String listOendAnsPop(HttpServletRequest request, ModelMap model) throws Exception {
		
		String survId = request.getParameter("survId");
		String quesId = request.getParameter("quesId");
		String quesCount = request.getParameter("quesCount");
		
		model.put("survId", survId);
		model.put("quesId", quesId);
		model.put("quesCount", quesCount);
		
		checkPath(request, "index", model);
		return LayoutUtil.getJspPath("/ct/surv/listOendAnsPop");
	}
	
	/** 주관식 답변보기*/
	@RequestMapping(value= "/ct/surv/listOendAnsFrm")
	public String listOendAnsFrm(HttpServletRequest request, ModelMap model) throws Exception {
		
		String survId = request.getParameter("survId");
		String quesId = request.getParameter("quesId");
		String quesCount = request.getParameter("quesCount");
		request.setAttribute("pageRowCnt", 10);//RowCnt 삽입
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		
		CtSurvQuesDVo ctsQueVo = new CtSurvQuesDVo();
		ctsQueVo.setQueryLang(langTypCd);
		//ctsQueVo.setCompId(ctEstbBVo.getCompId());
		ctsQueVo.setQuesId(quesId);
		ctsQueVo.setSurvId(survId);

		ctsQueVo = (CtSurvQuesDVo) commonDao.queryVo(ctsQueVo);
		
		// 조회조건 매핑
		CtSurvReplyDVo ctsReVo = new CtSurvReplyDVo();
		
		
		ctsReVo.setQueryLang(langTypCd);
		//ctsReVo.setCompId(ctEstbBVo.getCompId());
		ctsReVo.setSurvId(survId);
		ctsReVo.setQuesId(quesId);
		ctsReVo.setReplyNo(-1);
		ctsReVo.setOrderBy("REPLY_DT DESC");
		VoUtil.bind(request, ctsReVo);
		
		Map<String,Object> rsltMap = ctSurvSvc.getOendReplyCont(request, ctsReVo);
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("ctSurvBMapList", rsltMap.get("ctSurvBMapList"));
		model.put("ctsQueVo", ctsQueVo);
		model.put("quesCount", quesCount);
		
		checkPath(request, "index", model);
		return LayoutUtil.getJspPath("/ct/surv/listOendAnsFrm");
	}
	
	/** 설문정책 */
	@RequestMapping(value= "/ct/adm/setSurvPolc")
	public String setSurvPolc(HttpServletRequest request, ModelMap model) throws Exception {
		
		//String survPolc = request.getParameter("survPolc");
//		CtSurvPolcDVo ctsPolcVo = new CtSurvPolcDVo();
//		ctsPolcVo.setCompId(userVo.getCompId());
//		ctsPolcVo.setQueryLang(langTypCd);
//		
//		ctsPolcVo = (CtSurvPolcDVo) commonDao.queryVo(ctsPolcVo);
		
		String apvdY = null;
		String apvdN = null;
		
//		if(ctsPolcVo != null){
//			if(ctsPolcVo.getApvdYn().equalsIgnoreCase("Y")){
//				apvdY = "true";
//				apvdN = "false";
//			}else if(ctsPolcVo.getApvdYn().equalsIgnoreCase("N")){
//				apvdY = "false";
//				apvdN = "true";
//			}
//		}else{
//			apvdN = "true";
//		}
		
		model.put("apvdY", apvdY);
		model.put("apvdN", apvdN);
		
		checkPath(request, "index", model);
		return LayoutUtil.getJspPath("/ct/adm/setSurvPolc");
	}
	/** 설문정책 등록 */
	@RequestMapping(value= "/ct/adm/setSurvPolcSave")
	public String setSurvPolcSave(HttpServletRequest request, ModelMap model) throws Exception {
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		
		//String survPolc = request.getParameter("survPolc");
//		CtSurvPolcDVo ctsPolcVo = new CtSurvPolcDVo();
//		ctsPolcVo.setCompId(userVo.getCompId());
//		ctsPolcVo.setQueryLang(langTypCd);
//		
//		ctsPolcVo = (CtSurvPolcDVo) commonDao.queryVo(ctsPolcVo);
//		
//		QueryQueue  queryQueue = new QueryQueue();
//		if(ctsPolcVo == null){
//			CtSurvPolcDVo ctsPolcReg = new CtSurvPolcDVo();
			
//			//설문정책 Y,N
//			ctsPolcReg.setApvdYn(survPolc);
//			//등록자
//			ctsPolcReg.setRegrUid(userVo.getUserUid());
//			//등록시간
//			ctsPolcReg.setRegDt(ctSurvSvc.currentDay());
//			
//			//수정자
//			ctsPolcReg.setModrUid(userVo.getUserUid());
//			//수정시간
//			ctsPolcReg.setModDt(ctSurvSvc.currentDay());
//			
//			ctsPolcReg.setCompId(userVo.getCompId());
//			ctsPolcReg.setQueryLang(langTypCd);
//			
//			queryQueue.insert(ctsPolcReg);
//		}else{
//			//설문정책 Y,N
//			ctsPolcVo.setApvdYn(survPolc);
//			//수정자
//			ctsPolcVo.setModrUid(userVo.getUserUid());
//			//수정시간
//			ctsPolcVo.setModDt(ctSurvSvc.currentDay());
//			queryQueue.update(ctsPolcVo);
//		}
//		checkPath(request, "index", model);
		
//		commonDao.execute(queryQueue);
		
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		model.put("todo", "parent.location.replace('/ct/adm/setSurvPolc.do?menuId="+request.getParameter("menuId")
				+"');");
		
		//공통 처리 페이지
		return LayoutUtil.getResultJsp();
	}
	
	/** 신청중인 설문 */
	@RequestMapping(value= "/ct/adm/listSurvApvd")
	public String listSurvApvd(HttpServletRequest request, ModelMap model) throws Exception {
		
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 조회조건 매핑
		CtSurvBVo ctSurvBVo = new CtSurvBVo();
		VoUtil.bind(request, ctSurvBVo);
		
		ctSurvBVo.setRegrUid(userVo.getUserUid());
		List<String> survList=new ArrayList<String>();
		survList.add("2");
		ctSurvBVo.setSurvSearchList(survList);
		
		
		ctSurvBVo.setQueryLang(langTypCd);
		Map<String,Object> rsltMap = ctSurvSvc.getSurvApvdMapList(request, ctSurvBVo);
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("ctSurvBMapList", rsltMap.get("ctSurvBMapList"));

	
//		checkPath(request, "index", model);
		return LayoutUtil.getJspPath("/ct/adm/listSurvApvd");
	}
	
	/** 설문승인 view */
	@SuppressWarnings("unchecked")
	@RequestMapping(value= "/ct/adm/viewSurvApvd")
	public String viewSurvApvd(HttpServletRequest request, ModelMap model) throws Exception {
		
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		String survId = request.getParameter("survId");
		
		CtSurvBVo ctsVo =  new CtSurvBVo();
		ctsVo.setSurvId(survId);
		VoUtil.bind(request, ctsVo);
		//설문기본
		ctsVo = (CtSurvBVo) commonDao.queryVo(ctsVo);
		if(ctsVo != null){
			
			//설문질문상세
			CtSurvQuesDVo ctsQueVo = new CtSurvQuesDVo();
			ctsQueVo.setSurvId(ctsVo.getSurvId());
			//ctsQueVo.setCompId(ctEstbBVo.getCompId());
			ctsQueVo.setOrderBy("QUES_SORT_ORDR");

			
			List<CtSurvQuesDVo> ctsQueList = (List<CtSurvQuesDVo>) commonDao.queryList(ctsQueVo);
			
			List<CtQuesExamDVo> returnWcQueExamList = new ArrayList<CtQuesExamDVo>();
			
			for(CtSurvQuesDVo ctsQVo : ctsQueList){
				//질문보기상세
				CtQuesExamDVo ctQueExamVo =  new CtQuesExamDVo();
				ctQueExamVo.setSurvId(ctsQVo.getSurvId());
				//ctQueExamVo.setCompId(ctEstbBVo.getCompId());
				ctQueExamVo.setQuesId(ctsQVo.getQuesId());
				
				List<CtQuesExamDVo> ctQueExamList = (List<CtQuesExamDVo>) commonDao.queryList(ctQueExamVo);
					for(CtQuesExamDVo ctQVo : ctQueExamList){
						returnWcQueExamList.add(ctQVo);
					}
			}
			
		
			model.put("ctQueExamList", returnWcQueExamList);
			model.put("ctsQueList", ctsQueList);
			model.put("ctsVo", ctsVo);	
			checkPath(request, "set", model);
		}
		
		return LayoutUtil.getJspPath("/ct/adm/viewSurvApvd");
	}
	
	/** 설문정책 등록 */
	@RequestMapping(value= "/ct/adm/setSurvApvdSave")
	public String setSurvApvdPolcSave(HttpServletRequest request, ModelMap model) throws Exception {
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		
		String survPolc = request.getParameter("apvdYn");
	
		String survId = request.getParameter("survId");
		String returnSurvCont = request.getParameter("returnSurvCont");
		
		CtSurvBVo ctsVo = new CtSurvBVo();
		ctsVo.setSurvId(survId);
		//ctsVo.setCompId(ctEstbBVo.getCompId());
		
		if(survPolc.equalsIgnoreCase("Y")){
			ctsVo.setSurvPrgStatCd("3");
		}else if(survPolc.equalsIgnoreCase("N")){
			ctsVo.setSurvPrgStatCd("9");
			ctsVo.setAdmRjtOpinCont(returnSurvCont);
		}
		
		QueryQueue  queryQueue = new QueryQueue();
		
		queryQueue.update(ctsVo);
	
		checkPath(request, "index", model);
		
		commonDao.execute(queryQueue);
		
		
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		model.put("todo", "parent.location.replace('/ct/adm/listSurvApvd.do?menuId="+request.getParameter("menuId")
				+"');");
		
		//공통 처리 페이지
		return LayoutUtil.getResultJsp();
	}
	
	/** 설문목록 
	 */
	
	@RequestMapping(value= "/ct/surv/listSurv")
	public String listSurv(HttpServletRequest request, ModelMap model) throws Exception {
		
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String ctId = request.getParameter("ctId");
		String fncUid = request.getParameter("menuId");
		
		
		// 조회조건 매핑
		CtSurvBVo ctSurvBVo = new CtSurvBVo();
		VoUtil.bind(request, ctSurvBVo);
		ctSurvBVo.setLogUserUid(userVo.getUserUid());
		ctSurvBVo.setLangTyp(langTypCd);
		ctSurvBVo.setCtId(ctId);
		ctSurvBVo.setCtFncUid(fncUid);
		
		
		List<String> survList=new ArrayList<String>();
		//검색조건 [상태]
		String schCtStat = request.getParameter("schCtStat");
		if(schCtStat != null && schCtStat != ""){
			
			if(schCtStat.equalsIgnoreCase("6")){
				//union을 하기때문에 조회가 되지 않는 flag를 입력합니다.
				survList.add("F");
				ctSurvBVo.setSurvPrgStatCd("6");
			}else{
				survList.add(schCtStat);
				ctSurvBVo.setSurvPrgStatCd(survList.get(0));
			}
			ctSurvBVo.setSurvSearchList(survList);
			
		}else{
			ctSurvBVo.setSurvPrgStatCd("6");
			//준비중, 진행중, 마감, 임시저장
			survList.add("1");
			survList.add("3");
			survList.add("4");
			ctSurvBVo.setSurvSearchList(survList);
		}
		
		ctSurvBVo.setQueryLang(langTypCd);
		Map<String,Object> rsltMap = ctSurvSvc.getAdmSurvMapList(request, ctSurvBVo);
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("ctSurvBMapList", rsltMap.get("ctSurvBMapList"));
		model.put("logUserUid", ctSurvBVo.getLogUserUid());
		model.put("schCtStat", schCtStat);
		checkPath(request, "index", model);
		
	
		
//		if(userVo.isAdmin() || userVo.isSysAdmin()){
//			model.put("admin", "admin");
//		}
		
		//왼쪽 메뉴 
		ctCmntSvc.putLeftMenu(request, ctId, model);
		
		//권한 W=등록
		ctCmntSvc.putAuthChk(request, model, "W", ctId, fncUid);
		

		/**
		 * 커뮤니티 기능정보에서 메뉴명 가져오기
		 */
		CtFncDVo ctFncDVo =  new CtFncDVo();
		//ctFncDVo.setCompId(userVo.getCompId());
		ctFncDVo.setCtFncUid(fncUid);
		ctFncDVo.setLangTyp(langTypCd);
		CtFncDVo CtFncDVo =  (CtFncDVo) commonDao.queryVo(ctFncDVo);
		model.put("menuTitle", CtFncDVo.getCtFncNm());
		
		
		return LayoutUtil.getJspPath("/ct/surv/listSurv");
	}
	
	/** 설문 포틀릿 목록 
	 */
	
	@RequestMapping(value= "/ct/surv/listSurvPtFrm")
	public String listSurvPtFrm(HttpServletRequest request, ModelMap model) throws Exception {
		request.setAttribute("pageRowCnt", 4);//RowCnt 삽입
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String ctId = request.getParameter("ctId");
		
		// 조회조건 매핑
		CtSurvBVo ctSurvBVo = new CtSurvBVo();
		VoUtil.bind(request, ctSurvBVo);
		ctSurvBVo.setLogUserUid(userVo.getUserUid());
		ctSurvBVo.setLangTyp(langTypCd);
		ctSurvBVo.setCtId(ctId);
		ctSurvBVo.setSurvPrgStatCd("6");
		List<String> survList=new ArrayList<String>();
		//검색조건 [상태]
		String schCtStat = request.getParameter("schCtStat");
		if(schCtStat != null && schCtStat != ""){
			survList.add(schCtStat);
			ctSurvBVo.setSurvSearchList(survList);
		}else{
			//준비중, 진행중, 마감, 임시저장
			survList.add("1");
			survList.add("3");
			survList.add("4");
			ctSurvBVo.setSurvSearchList(survList);
		}
		
		ctSurvBVo.setQueryLang(langTypCd);
		Map<String,Object> rsltMap = ctSurvSvc.getAdmSurvMapList(request, ctSurvBVo);
		model.put("ctId", ctId);
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("ctSurvBMapList", rsltMap.get("ctSurvBMapList"));
		model.put("logUserUid", ctSurvBVo.getLogUserUid());
		model.put("schCtStat", schCtStat);
		checkPath(request, "index", model);
		model.put("myAuth", ctCmntSvc.getUserSeculCd(userVo.getCompId(), userVo.getUserUid(), ctId));
		
		//왼쪽 메뉴 
//		ctCmntSvc.putLeftMenu(request, ctId, model);
		
		return LayoutUtil.getJspPath("/ct/surv/listSurvPtFrm");
	}
	
	/** 통계보기 관리자
	 *	/ct/viewSurvRes 같은 이름으로 있어서
	 *  /ct/adm/viewSurvRes 로 맵핑 후 
	 *  viewSurvResAdm로 지명
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value= "/ct/adm/viewSurvRes")
	public String viewSurvResAdm(HttpServletRequest request, ModelMap model) throws Exception {

		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		String survId = request.getParameter("survId");
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		ctFileSvc.putFileListToModel(survId, model, userVo.getCompId());
		
		CtSurvBVo ctsVo =  new CtSurvBVo();
		ctsVo.setSurvId(survId);
		VoUtil.bind(request, ctsVo);
		//설문기본
		ctsVo = (CtSurvBVo) commonDao.queryVo(ctsVo);
		if(ctsVo != null){
			if(request.getParameter("survStatCd").equals("1") || request.getParameter("survStatCd").equals("2") || request.getParameter("survStatCd").equals("5")){
					model.put("message", messageProperties.getMessage("ct.msg.admSurv.doNot", request));
					model.put("todo", "parent.location.replace('/ct/adm/listSurv.do?menuId="+request.getParameter("menuId")
							+"');");
					//공통 처리 페이지
					return LayoutUtil.getResultJsp();
			}else if(request.getParameter("survStatCd").equals("9")){
				model.put("message", messageProperties.getMessage("ct.msg.rejectCont", request) +"\r\n"+ ctsVo.getAdmRjtOpinCont());
				model.put("todo", "parent.location.replace('/ct/adm/listSurv.do?menuId="+request.getParameter("menuId")
						+"');");
				//공통 처리 페이지
				return LayoutUtil.getResultJsp();
			}
			
			//설문질문상세
			CtSurvQuesDVo ctsQueVo = new CtSurvQuesDVo();
			ctsQueVo.setSurvId(ctsVo.getSurvId());
			//ctsQueVo.setCompId(ctEstbBVo.getCompId());
			ctsQueVo.setOrderBy("QUES_SORT_ORDR");
			
			List<CtSurvQuesDVo> ctsQueList = (List<CtSurvQuesDVo>) commonDao.queryList(ctsQueVo);
			
			List<CtQuesExamDVo> returnWcQueExamList = new ArrayList<CtQuesExamDVo>();
//			if(ctsQueList.size() == 0 ){
//				model.put("message", messageProperties.getMessage("ct.msg.admSurvRes.notSave", request));
//				model.put("todo", "parent.location.replace('/ct/adm/listSurv.do?menuId="+request.getParameter("menuId")
//						+"');");
//				
//				//공통 처리 페이지
//				return LayoutUtil.getResultJsp();
//			}
			
			for(CtSurvQuesDVo ctsQVo : ctsQueList){
				//질문보기상세
				CtQuesExamDVo ctQueExamVo =  new CtQuesExamDVo();
				ctQueExamVo.setSurvId(ctsQVo.getSurvId());
				//ctQueExamVo.setCompId(ctEstbBVo.getCompId());
				ctQueExamVo.setQuesId(ctsQVo.getQuesId());
				
				List<CtQuesExamDVo> ctQueExamList = (List<CtQuesExamDVo>) commonDao.queryList(ctQueExamVo);
					for(CtQuesExamDVo ctQVo : ctQueExamList){
						
						//질문전체 count
						CtSurvReplyDVo ctReAllCount = new CtSurvReplyDVo();
						//ctReAllCount.setCompId(ctEstbBVo.getCompId());
						ctReAllCount.setSurvId(ctQVo.getSurvId());
						ctReAllCount.setQuesId(ctQVo.getQuesId());
						float quesAllCount = commonDao.count(ctReAllCount)!=null?commonDao.count(ctReAllCount):0;

						//질문보기 개당 count
						CtSurvReplyDVo ctReOneCount = new CtSurvReplyDVo();
						//ctReOneCount.setCompId(ctEstbBVo.getCompId());
						ctReOneCount.setSurvId(ctQVo.getSurvId());
						ctReOneCount.setQuesId(ctQVo.getQuesId());
						ctReOneCount.setReplyNo(ctQVo.getExamNo());
						//질문보기상세 선택된 개수 count 구하기
						float quesOneCount = commonDao.count(ctReOneCount)!=null?commonDao.count(ctReOneCount):0;
						
						//평균 구하기
						float average = (quesOneCount / quesAllCount) * 100;
						
						ctQVo.setSelectCount(String.valueOf((int)quesOneCount));
						ctQVo.setQuesAverage(String.valueOf((int)average));
						returnWcQueExamList.add(ctQVo);
					}
					
					//주관식 질문  count
					CtSurvReplyDVo ctReOendCount = new CtSurvReplyDVo();
					//ctReOendCount.setCompId(ctEstbBVo.getCompId());
					ctReOendCount.setSurvId(ctsQVo.getSurvId());
					ctReOendCount.setQuesId(ctsQVo.getQuesId());
					ctReOendCount.setReplyNo(-1);
					float quesOendCount = commonDao.count(ctReOendCount)!=null?commonDao.count(ctReOendCount):0;
					
					ctsQVo.setOendCount(String.valueOf((int)quesOendCount));
					
					
			}
			
			
			model.put("returnWcQueExamList", returnWcQueExamList);
			model.put("ctsQueList", ctsQueList);
			model.put("ctsVo", ctsVo);	
			checkPath(request, "set", model);
		}
		
		return LayoutUtil.getJspPath("/ct/adm/viewSurvRes");
	}
	
	/** 객관식입력항목 답변보기*/
	@RequestMapping(value= "/ct/surv/listMulcAnsPop")
	public String listMulcAnsPop(HttpServletRequest request, ModelMap model) throws Exception {
		
		String survId = request.getParameter("survId");
		String quesId = request.getParameter("quesId");
		String replyNo = request.getParameter("replyNo");
		
		model.put("survId", survId);
		model.put("quesId", quesId);
		model.put("replyNo", replyNo);
		
		return LayoutUtil.getJspPath("/ct/surv/listMulcAnsPop");
	}
	
	/** 객관식입력항목 답변보기*/
	@RequestMapping(value= "/ct/surv/listMulcAnsFrm")
	public String listMulcAnsFrm(HttpServletRequest request, ModelMap model) throws Exception {
		
		String survId = request.getParameter("survId");
		String quesId = request.getParameter("quesId");
		String replyNo = request.getParameter("replyNo");
		request.setAttribute("pageRowCnt", 10);//RowCnt 삽입
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		
		CtSurvQuesDVo ctsQueVo = new CtSurvQuesDVo();
		ctsQueVo.setQueryLang(langTypCd);
		//ctsQueVo.setCompId(ctEstbBVo.getCompId());
		ctsQueVo.setQuesId(quesId);
		ctsQueVo.setSurvId(survId);

		ctsQueVo = (CtSurvQuesDVo) commonDao.queryVo(ctsQueVo);
		
		// 조회조건 매핑
		CtSurvReplyDVo ctsReVo = new CtSurvReplyDVo();
//		VoUtil.bind(request, ctSurvBVo);
		
		ctsReVo.setQueryLang(langTypCd);
		//ctsReVo.setCompId(ctEstbBVo.getCompId());
		ctsReVo.setSurvId(survId);
		ctsReVo.setQuesId(quesId);
		ctsReVo.setReplyNo(Integer.parseInt(replyNo));
		ctsReVo.setMulcInputReplyContYn("Y");
		ctsReVo.setOrderBy("REPLY_DT DESC");
		Map<String,Object> rsltMap = ctSurvSvc.getOendReplyCont(request, ctsReVo);
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("ctSurvBMapList", rsltMap.get("ctSurvBMapList"));
		model.put("ctsQueVo", ctsQueVo);
		
		checkPath(request, "index", model);
		return LayoutUtil.getJspPath("/ct/surv/listMulcAnsFrm");
	}
	
	/** [팝업] 사진 선택 */
	@RequestMapping(value = {"/ct/surv/setImagePop", "/ct/surv/setImagePop"})
	public String setImagePop(HttpServletRequest request,
			@Parameter(name="bcId", required=false) String bcId,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/ct/surv/setImagePop");
	}
	
	/** [히든프레임] 사진 업로드 */
	@RequestMapping(value = {"/ct/surv/transImage"})
	public String transImage(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		UploadHandler uploadHandler = null;
		boolean tempFileDel = false;
		try{
			uploadHandler = uploadManager.createHandler(request, "temp", "ct");
			//Map<String, File> fileMap = uploadHandler.upload();//업로드 파일 정보
			//Map<String, String> param = uploadHandler.getParamMap();//파라미터 정보
			uploadHandler.upload();
			
			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
					
			QueryQueue queryQueue = new QueryQueue();
			//List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
			CtFileDVo ctSurvfileDvo = new CtFileDVo();
			CtFileDVo newctSurvFileDVo = (CtFileDVo)ctFileSvc.savePhoto(request,"photo", -1, ctSurvfileDvo , queryQueue);
//				if(newctSurvFileDVo != null){
//					deletedFileList.add(newctSurvFileDVo);					
//					// 파일 삭제
//					ctFileSvc.deleteDiskFiles(deletedFileList);
//				}
			
			commonSvc.execute(queryQueue);
			//String fileDir = distManager.getContextProperty("distribute.web.local.root")+distPath;
			model.put("todo", "parent.setImage('"+request.getParameter("quesNum")+"','"+request.getParameter("examNum")+"','"+newctSurvFileDVo.getFileId()+"', '"+newctSurvFileDVo.getDispNm()+"', '"+newctSurvFileDVo.getSavePath().replace('\\', '/')+"');");
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
	@RequestMapping(value = {"/ct/adm/transImageDel"})
	public String transImageDel(HttpServletRequest request,
			@Parameter(name="fileId", required=true) int fileId,
			ModelMap model) throws Exception {
		
		try{
			// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			
			// 명함 이미지 상세
			CtFileDVo ctSurvFileDvo = new CtFileDVo();
			ctSurvFileDvo.setFileId(fileId);
			queryQueue.delete(ctSurvFileDvo);
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
	
	/** 관리자설문삭제 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/ct/surv/transSetSurvDel"})
	public String transSetSurvDel(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		
		try{
			
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String survId  = (String) object.get("survId");
			String ctId  = (String) object.get("ctId");
			if (survId == null || survId.isEmpty() || ctId == null || ctId.isEmpty()) {
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
			
			//UserVo userVo = LoginSession.getUser(request);
			// 설문 그룹 삭제
			QueryQueue queryQueue = new QueryQueue();
			/** 삭제순서 
			 * 1.설문답변상세 (WV_SURV_REPLY_D)삭제
			 * 2.설문파일상세 (WV_SURV_FILD_D)삭제
			 * 3.질문보기상세 (WV_QUES_EXAM_D)삭제
			 * 4.설문질문상세 (CT_SURV_QUES_D)삭제
			 * 5.설문기본  (WV_SURV_B)삭제
			 * 6.설문사용권한상세 (WV_SURV_USE_AUTH_D)삭제
			 * */
			
			//해당 설문 내용을 모두 조회
			CtSurvBVo ctsThisVo =  new CtSurvBVo();
			ctsThisVo.setSurvId(survId);
			ctsThisVo.setCtId(ctId);
			ctsThisVo = (CtSurvBVo) commonDao.queryVo(ctsThisVo);
			
			//설문 유효성 검사
			if(ctsThisVo == null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
				
			//설문답변상세 삭제
			CtSurvReplyDVo ctsReVo = new CtSurvReplyDVo();
			ctsReVo.setSurvId(ctsThisVo.getSurvId());
			List<CtSurvReplyDVo> ctsReList = (List<CtSurvReplyDVo>) commonDao.queryList(ctsReVo);
			if(ctsReList.size() != 0){
				for(CtSurvReplyDVo ctsRe :ctsReList){
					CtSurvReplyDVo ctsReDel = new CtSurvReplyDVo();
					ctsReDel.setSurvId(ctsRe.getSurvId());
					ctsReDel.setCompId(ctEstbBVo.getCompId());
					ctsReDel.setQuesId(ctsRe.getQuesId());
					ctsReDel.setReplyNo(ctsRe.getReplyNo());
					//답변자삭제
					queryQueue.delete(ctsReDel);
				}
			}
			
			//질문보기상세 & 이미지 삭제
			CtQuesExamDVo ctQExamVo = new CtQuesExamDVo();
			ctQExamVo.setSurvId(ctsThisVo.getSurvId());
			
			List<CtQuesExamDVo> ctQExamList = (List<CtQuesExamDVo>) commonDao.queryList(ctQExamVo);
			if(ctQExamList.size() != 0){
				for(CtQuesExamDVo ctQE : ctQExamList){
					if(!ctQE.getImgSurvFileId().equalsIgnoreCase("") && ctQE.getImgSurvFileId() != null){
						CtFileDVo ctsFileVo = new CtFileDVo();
						if(ctQE.getImgSurvFileId() != null){
							if(!ctQE.getImgSurvFileId().equalsIgnoreCase("")){
								ctsFileVo.setFileId(Integer.parseInt(ctQE.getImgSurvFileId()));
								//질문보기상세관련 설문파일상세 File 삭제
								queryQueue.delete(ctsFileVo);
							}
						}
					}
					CtQuesExamDVo ctQExamDel = new CtQuesExamDVo();
					ctQExamDel.setSurvId(ctQE.getSurvId());
					ctQExamDel.setQuesId(ctQE.getQuesId());
					ctQExamDel.setCompId(ctEstbBVo.getCompId());
					ctQExamDel.setExamNo(ctQE.getExamNo());
					//질문보기상세 삭제
					queryQueue.delete(ctQExamDel);
				}
			}
			
			//설문질문상세 삭제
			CtSurvQuesDVo ctsQuesVo = new CtSurvQuesDVo();
			ctsQuesVo.setSurvId(ctsThisVo.getSurvId());
			
			List<CtSurvQuesDVo> ctsQuesList = (List<CtSurvQuesDVo>) commonDao.queryList(ctsQuesVo);
			if(ctsQuesList.size() != 0){
				for(CtSurvQuesDVo ctsQ: ctsQuesList){
					if(ctsQ.getImgSurvFileId() != null){
						if(!ctsQ.getImgSurvFileId().equalsIgnoreCase("")){
							CtFileDVo ctsFileVo = new CtFileDVo();
							ctsFileVo.setFileId(Integer.parseInt(ctsQ.getImgSurvFileId()));
							//설문질문상세관련 설문파일상세 File 삭제
							queryQueue.delete(ctsFileVo);
						}
					}
					CtSurvQuesDVo ctsQDel = new CtSurvQuesDVo();
					ctsQDel.setSurvId(ctsQ.getSurvId());
					ctsQDel.setQuesId(ctsQ.getQuesId());
					ctsQDel.setCompId(ctEstbBVo.getCompId());
					
					//설문질문상세 삭제
					queryQueue.delete(ctsQDel);
				}
			}
			
			//설문사용권한상세 삭제
//			CtSurvUseAuthDVo ctsUseAuthVo = new CtSurvUseAuthDVo();
//			ctsUseAuthVo.setSurvId(ctsThisVo.getSurvId());
//			
//			List<CtSurvUseAuthDVo> ctsUseAuthList = (List<CtSurvUseAuthDVo>) commonDao.queryList(ctsUseAuthVo);
//			
//			for(CtSurvUseAuthDVo ctsUseAuth : ctsUseAuthList){
//				CtSurvUseAuthDVo ctsUseAuthDel = new CtSurvUseAuthDVo();
//				ctsUseAuthDel.setSurvId(ctsUseAuth.getSurvId());
//				ctsUseAuthDel.setAuthTgtTypCd(ctsUseAuth.getAuthTgtTypCd());
//				ctsUseAuthDel.setAuthTgtUid(ctsUseAuth.getAuthTgtUid());
//				ctsUseAuthDel.setCompId(ctsUseAuth.getCompId());
				
				//설문사용권한상세
//				queryQueue.delete(ctsUseAuthDel);
//			}
			
			
			
			CtSurvBVo ctsBVo = new CtSurvBVo();
			ctsBVo.setSurvId(ctsThisVo.getSurvId());
			ctsBVo.setCtId(ctsThisVo.getCtId());
			ctsBVo.setCompId(ctEstbBVo.getCompId());
			//설문기본 삭제
			queryQueue.delete(ctsBVo);
				
			commonSvc.execute(queryQueue);
			
			//cm.msg.del.success=삭제 되었습니다.
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
	
	/** 임시 저장*/
	@RequestMapping(value= "/ct/surv/transSetTmpSaveSurvQues")
	public String transSetTmpSaveSurvQues(HttpServletRequest request, ModelMap model) throws Exception {
		
		QueryQueue queryQueue = new QueryQueue();
		
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		
		String survId = request.getParameter("survId");
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		
		CtSurvBVo ctsThisVo = new CtSurvBVo();
		ctsThisVo.setSurvId(survId);
		ctsThisVo.setCompId(ctEstbBVo.getCompId());
		
		ctsThisVo.setSurvPrgStatCd("6");
		
		queryQueue.update(ctsThisVo);
		
		commonDao.execute(queryQueue);
		model.put("message", messageProperties.getMessage("bb.msg.save.tmp.success", request));
		model.put("todo", "parent.location.replace('/ct/surv/listSurv.do?menuId="+request.getParameter("menuId")
			+"&ctId=" +	ctId
		+"');");
		
		checkPath(request, "index", model);
		
		//공통 처리 페이지
		return LayoutUtil.getResultJsp();
	}
	
//	/** 첨부파일 다운로드 (사용자) */
//	@RequestMapping(value = "/ct/downFile", method = RequestMethod.POST)
//	public ModelAndView downFile(HttpServletRequest request,
//			@RequestParam(value = "fileIds", required = true) String fileIds,
//			@RequestParam(value = "actionParam", required = false) String actionParam
//			) throws Exception {
//		
//		try {
//			if (fileIds.isEmpty()) {
//				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
//				throw new CmException("pt.msg.nodata.passed", request);
//			}
//			
//			// 파일 목록조회
//			ModelAndView mv = ctCmSvc.getFileList(request , fileIds , actionParam);
//			
//			return mv;
//			
//		} catch (CmException e) {
//			LOGGER.error(e.getMessage());
//			ModelAndView mv = new ModelAndView("cm/result/commonResult");
//			mv.addObject("message", e.getMessage());
//			return mv;
//		} catch (Exception e) {
//			LOGGER.error(e.getMessage());
//			ModelAndView mv = new ModelAndView("cm/result/commonResult");
//			mv.addObject("message", e.getMessage());
//		}
//		return null;
//	}
	
	/** 설문질문상세 저장 */
	@RequestMapping(value= "/ct/surv/setMulcQuesPopSave")
	public String setMulcQuesPopSave(HttpServletRequest request, ModelMap model) throws Exception {
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		QueryQueue queryQueue = new QueryQueue();
		String fnc = request.getParameter("fnc");
		String survId = request.getParameter("survId");
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		int ctsQuesOrdr =0;
		if(fnc.equalsIgnoreCase("mod") && survId != null){
			String quesId = request.getParameter("quesId");
			if(quesId != null){
				CtSurvQuesDVo ctsQues = new CtSurvQuesDVo();
				ctsQues.setSurvId(survId);
				ctsQues.setQuesId(quesId);
				ctsQues.setCompId(ctEstbBVo.getCompId());
				
				queryQueue.delete(ctsQues);
				
				ctsQues = (CtSurvQuesDVo) commonDao.queryVo(ctsQues);
				ctsQuesOrdr = ctsQues.getQuesSortOrdr();
				//질문이미지삭제
//				CtFileDVo ctsFileVo = new CtFileDVo();
//				if(ctsQues.getImgSurvFileId() != null){
//					if(!ctsQues.getImgSurvFileId().equalsIgnoreCase("")){
//						ctsFileVo.setFileId(Integer.parseInt(ctsQues.getImgSurvFileId()));
//						queryQueue.delete(ctsFileVo);
//					}
//				}
				
				CtQuesExamDVo ctQExam = new CtQuesExamDVo();
				ctQExam.setSurvId(survId);
				ctQExam.setQuesId(quesId);
				ctQExam.setCompId(ctEstbBVo.getCompId());
				
				queryQueue.delete(ctQExam);
				
//				List<CtQuesExamDVo> ctQueExamList = (List<CtQuesExamDVo>) commonDao.queryList(ctQExam);
				
				//보기이미지삭제
//				for(CtQuesExamDVo ctqExam :ctQueExamList){
//					CtFileDVo ctsFileExam = new CtFileDVo();
//					if(ctqExam.getImgSurvFileId() != null){
//						if(!ctqExam.getImgSurvFileId().equalsIgnoreCase("")){
//							ctsFileExam.setFileId(Integer.parseInt(ctqExam.getImgSurvFileId()));
//							queryQueue.delete(ctsFileExam);
//						}
//					}
//					
//				}
				
			}
		}
		
		
		
		String subj = request.getParameter("subj");
		String ques_img_file_id = request.getParameter("ques_img_file_id");
		String[] exam_img_file_id = request.getParameterValues("exam_img_file_id");
		String examTypeSelect =  request.getParameter("examTypeSelect");
		String ansAdd = request.getParameter("ansAdd");
		String imgAdd = request.getParameter("imgAdd");
		String[] exam = request.getParameterValues("exam");
		String mulChoiYn = request.getParameter("mulChoiYn");
		String mandaReplyYn = request.getParameter("mandaReplyYn");
		
		CtSurvQuesDVo ctsQues =  new CtSurvQuesDVo();
		ctsQues.setSurvId(survId);
		ctsQues.setQuesId(ctCmSvc.createId("CT_SURV_QUES_D"));
		ctsQues.setCompId(ctEstbBVo.getCompId());
		//등록일때 순서
		
		//질문정렬순서
		//============================================
		CtSurvQuesDVo ctsCount =  new CtSurvQuesDVo();
		ctsCount.setInstanceQueryId("countMaxCtSurvQuesD");
		ctsCount.setSurvId(survId);
		ctsCount.setCompId(ctEstbBVo.getCompId());
		int quesOrdrCount = commonDao.queryInt(ctsCount)!=null?commonDao.queryInt(ctsCount):0;
		ctsQues.setCompId(ctEstbBVo.getCompId());	
		if(fnc.equalsIgnoreCase("reg")){
			ctsQues.setQuesSortOrdr(quesOrdrCount +1);
			
			//등록일때만 설문기본 질문개수가 +1 됨.
			//============================================
			//설문기본 질문 개수
			CtSurvQuesDVo ctsQuesCount = new CtSurvQuesDVo();
			ctsQuesCount.setSurvId(ctsQues.getSurvId());
			ctsQuesCount.setCompId(ctEstbBVo.getCompId());
			int quesCount = commonDao.count(ctsQuesCount)!=null?commonDao.count(ctsQuesCount):0;
			CtSurvBVo ctsVo = new CtSurvBVo();
			ctsVo.setSurvId(ctsQuesCount.getSurvId());
			ctsVo.setQuesCnt(quesCount+1);
			queryQueue.update(ctsVo);
			
		}else if(fnc.equalsIgnoreCase("mod")){
			ctsQues.setQuesSortOrdr(ctsQuesOrdr);
		}
		
		
		
		
		//============================================
		
		ctsQues.setQuesCont(subj);
		ctsQues.setQuesImgAttYn(ques_img_file_id.equals("")&&ques_img_file_id!=null?"Y":"N");
		ctsQues.setImgSurvFileId(ques_img_file_id);
		//보기선택항목코드
		ctsQues.setExamChoiItemCd(examTypeSelect);
		ctsQues.setMulChoiYn(mulChoiYn);
		ctsQues.setMandaReplyYn(mandaReplyYn);
		
		queryQueue.insert(ctsQues);
		
		//질문보기상세 저장
		for(int i=0; i < exam.length; i++){
			CtQuesExamDVo ctQExamVo = new CtQuesExamDVo();
			ctQExamVo.setExamNo(i+1);
			ctQExamVo.setQuesId(ctsQues.getQuesId());
			ctQExamVo.setCompId(ctEstbBVo.getCompId());
			ctQExamVo.setSurvId(ctsQues.getSurvId());
			ctQExamVo.setExamOrdr(i+1);
			ctQExamVo.setExamDispNm(exam[i]);
			ctQExamVo.setInputYn(ansAdd==null?"N":ansAdd);
			ctQExamVo.setExamImgUseYn(imgAdd);
			if(exam_img_file_id != null){
				ctQExamVo.setImgSurvFileId(exam_img_file_id[i]);
			}
			
			queryQueue.insert(ctQExamVo);
		}
		
		
		commonDao.execute(queryQueue);
		
		//model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		model.put("todo", "parent.location.replace('/ct/surv/setSurvQues.do?menuId="+request.getParameter("menuId")
				+ "&fnc=" + request.getParameter("fnc")
				+ "&survId=" + survId
				+ "&ctId=" + ctId
				+"');");
		
		//공통 처리 페이지
		return LayoutUtil.getResultJsp();
	}
	
	/** 질문삭제 [FORM SUBMIT] 
	 * @throws Exception */
	@RequestMapping(value = {"/ct/adm/setSurvQuesDel","/ct/surv/setSurvQuesDel" }, method = RequestMethod.POST)
	public String setSurvQuesDel(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception{
		
			// 세션의 사용자 정보
			//UserVo userVo = LoginSession.getUser(request);

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String survId  = (String) object.get("survId");
			String quesId  = (String) object.get("quesId");
			//			String bullId = (String) object.get("bullId");
			if (survId == null || survId.equalsIgnoreCase("")
					|| quesId == null || quesId.equalsIgnoreCase("")) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			// 설문 그룹 삭제
			QueryQueue queryQueue = new QueryQueue();
			
			CtSurvQuesDVo ctQueVo = new CtSurvQuesDVo();
			ctQueVo.setQuesId(quesId);
			ctQueVo.setSurvId(survId);
			//ctQueVo.setCompId(ctEstbBVo.getCompId());
			queryQueue.delete(ctQueVo);
			
			ctQueVo = (CtSurvQuesDVo) commonDao.queryVo(ctQueVo);
					
			
			CtFileDVo ctsFileVo = new CtFileDVo();
			if(ctQueVo.getImgSurvFileId() != null){
				if(!ctQueVo.getImgSurvFileId().equalsIgnoreCase("")){
					ctsFileVo.setFileId(Integer.parseInt(ctQueVo.getImgSurvFileId()));
					queryQueue.delete(ctsFileVo);
				}
			}
			
			CtQuesExamDVo ctQueExamVo = new CtQuesExamDVo();
			ctQueExamVo.setQuesId(quesId);
			ctQueExamVo.setSurvId(survId);
			//ctQueExamVo.setCompId(ctEstbBVo.getCompId());
			
			queryQueue.delete(ctQueExamVo);
			@SuppressWarnings("unchecked")
			List<CtQuesExamDVo> ctQueExamList = (List<CtQuesExamDVo>) commonDao.queryList(ctQueExamVo);
			
			for(CtQuesExamDVo ctqExam :ctQueExamList){
				CtFileDVo ctsFileExam = new CtFileDVo();
				if(ctqExam.getImgSurvFileId() != null){
					if(!ctqExam.getImgSurvFileId().equalsIgnoreCase("")){
						ctsFileExam.setFileId(Integer.parseInt(ctqExam.getImgSurvFileId()));
						queryQueue.delete(ctsFileExam);
					}
				}
				
			}
		

			//============================================
			//설문기본 질문 개수
			CtSurvQuesDVo ctsQuesCount = new CtSurvQuesDVo();
			ctsQuesCount.setSurvId(ctQueExamVo.getSurvId());
			//ctsQuesCount.setCompId(ctEstbBVo.getCompId());
			int quesCount = commonDao.count(ctsQuesCount)!=null?commonDao.count(ctsQuesCount):0; 
			CtSurvBVo ctsVo = new CtSurvBVo();
			ctsVo.setSurvId(ctsQuesCount.getSurvId());
			if(quesCount > 1){
				ctsVo.setQuesCnt(quesCount-1);
			}else{
				ctsVo.setQuesCnt(quesCount);
			}
			
			
			queryQueue.update(ctsVo);
			//============================================
			
			commonDao.execute(queryQueue);
			
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
	
	/** 주관식 저장*/
	@RequestMapping(value= "/ct/surv/transSetSurvOendQuesSave")
	public String transSetSurvOendQuesSave(HttpServletRequest request, ModelMap model) throws Exception {
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		QueryQueue queryQueue = new QueryQueue();
		String fnc = request.getParameter("fnc");
		String survId = request.getParameter("survId");
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		String subj = request.getParameter("subj");
		String ques_img_file_id = request.getParameter("ques_img_file_id");
		String mandaReplyYn = request.getParameter("mandaReplyYn");
		if(!fnc.equalsIgnoreCase("mod")){
			
			CtSurvQuesDVo ctsQuesVo = new CtSurvQuesDVo();
			ctsQuesVo.setCompId(ctEstbBVo.getCompId());
			ctsQuesVo.setQuesImgAttYn(ques_img_file_id.equals("")&&ques_img_file_id!=null?"Y":"N");
			ctsQuesVo.setQuesId(ctCmSvc.createId("CT_SURV_QUES_D"));
			ctsQuesVo.setSurvId(survId);
			ctsQuesVo.setQuesCont(subj);
			ctsQuesVo.setMulChoiYn(null);
			ctsQuesVo.setMandaReplyYn(mandaReplyYn);
			ctsQuesVo.setImgSurvFileId(ques_img_file_id);

			//질문정렬순서
			//============================================
			CtSurvQuesDVo ctsCount =  new CtSurvQuesDVo();
			ctsCount.setInstanceQueryId("countMaxCtSurvQuesD");
			ctsCount.setSurvId(survId);
			ctsCount.setCompId(ctEstbBVo.getCompId());
			int quesOrdrCount = commonDao.queryInt(ctsCount)!=null?commonDao.queryInt(ctsCount):0;
			ctsQuesVo.setCompId(ctEstbBVo.getCompId());		
			ctsQuesVo.setQuesSortOrdr(quesOrdrCount +1);
			//============================================
			//설문기본 질문 개수
			CtSurvQuesDVo ctsQuesCount = new CtSurvQuesDVo();
			ctsQuesCount.setSurvId(ctsQuesVo.getSurvId());
			ctsQuesCount.setCompId(ctEstbBVo.getCompId());
			int quesCount = commonDao.count(ctsQuesCount)!=null?commonDao.count(ctsQuesCount):0;
			CtSurvBVo ctsVo = new CtSurvBVo();
			ctsVo.setSurvId(ctsQuesCount.getSurvId());
			ctsVo.setQuesCnt(quesCount+1);
			
			queryQueue.update(ctsVo);
			//============================================
			queryQueue.insert(ctsQuesVo);
		}else{
			String quesId = request.getParameter("quesId");
			CtSurvQuesDVo ctsQuesVo = new CtSurvQuesDVo();
			ctsQuesVo.setSurvId(survId);
			ctsQuesVo.setQuesId(quesId);
			ctsQuesVo.setCompId(ctEstbBVo.getCompId());
			ctsQuesVo.setQuesCont(subj);
			ctsQuesVo.setImgSurvFileId(ques_img_file_id);
			ctsQuesVo.setQuesImgAttYn(ques_img_file_id.equals("")&&ques_img_file_id!=null?"Y":"N");
			ctsQuesVo.setMulChoiYn(null);
			ctsQuesVo.setMandaReplyYn(mandaReplyYn);
			
			queryQueue.update(ctsQuesVo);
		}
		
		commonDao.execute(queryQueue);
		model.put("survId",survId);
		model.put("ctId", ctId);
		//model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		model.put("todo", "parent.location.replace('/ct/surv/setSurvQues.do?menuId="+request.getParameter("menuId")
				+ "&fnc=" + request.getParameter("fnc")
				+ "&survId=" + survId
				+ "&ctId=" + ctId
				+"');");
		
		//공통 처리 페이지
		return LayoutUtil.getResultJsp();
		
	}
	
	
	/** [팝업] 사진 보기 */
    @RequestMapping(value = {"/ct/surv/viewImagePop", "/ct/surv/adm/viewImagePop"})
	public String viewImagePop(HttpServletRequest request,
			@RequestParam(value = "survId", required = true) String survId,
			@RequestParam(value = "quesId", required = true) String quesId,
			@RequestParam(value = "examNo", required = false) String examNo,
			ModelMap model) throws Exception {
		
    	if(examNo == null || "".equals(examNo)){
    		// 세션의 사용자 정보
    		//UserVo userVo = LoginSession.getUser(request);
    		CtSurvQuesDVo wvsQueVo = new CtSurvQuesDVo();
    		VoUtil.bind(request, wvsQueVo);
			//wvsQueVo.setCompId(userVo.getCompId());
			CtSurvQuesDVo wvQueExamVo = (CtSurvQuesDVo) commonDao.queryVo(wvsQueVo);
			model.put("wvQueExamVo", wvQueExamVo);
    	}else{
    		// 설문상세(WV_QUES_EXAM_D) 테이블
        	CtQuesExamDVo wvQueExamVo =  new CtQuesExamDVo();
        	VoUtil.bind(request, wvQueExamVo);
        	wvQueExamVo = (CtQuesExamDVo)commonSvc.queryVo(wvQueExamVo);
        	model.put("wvQueExamVo", wvQueExamVo);
    	}
    	return LayoutUtil.getJspPath("/ct/surv/viewImagePop");
	}

}
