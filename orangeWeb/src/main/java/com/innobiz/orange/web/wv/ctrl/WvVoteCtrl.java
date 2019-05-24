package com.innobiz.orange.web.wv.ctrl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.web.servlet.ModelAndView;

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
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.wv.svc.WvCmSvc;
import com.innobiz.orange.web.wv.svc.WvFileSvc;
import com.innobiz.orange.web.wv.svc.WvSurvSvc;
import com.innobiz.orange.web.wv.vo.WvQuesExamDVo;
import com.innobiz.orange.web.wv.vo.WvSurvBVo;
import com.innobiz.orange.web.wv.vo.WvSurvFileDVo;
import com.innobiz.orange.web.wv.vo.WvSurvPolcDVo;
import com.innobiz.orange.web.wv.vo.WvSurvPopupDVo;
import com.innobiz.orange.web.wv.vo.WvSurvQuesDVo;
import com.innobiz.orange.web.wv.vo.WvSurvReplyDVo;
import com.innobiz.orange.web.wv.vo.WvSurvUseAuthDVo;

/** 설문조사 */
@Controller
public class WvVoteCtrl {
	
	/** 공통 서비스 */
	@Autowired
	private WvSurvSvc wvSurvSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
//	/** 조직 공통 서비스 */
//	@Autowired
//	private OrCmSvc orCmSvc;
	
	/** 관리자 커뮤니티 공지사항 서비스 */
	@Autowired
	private CtAdmNotcSvc ctAdmNotcSvc;
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 첨부파일 서비스 */
	@Autowired
	private WvFileSvc wvFileSvc;
	
	/** 설문 공통 서비스 */
	@Autowired
	private WvCmSvc wvCmSvc;
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WvVoteCtrl.class);
	
	/** 문서뷰어 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** 나의 신청중인 설문 view */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/wv/viewMySurvApvd")
	public String viewMySurvApvd(HttpServletRequest request, ModelMap model)throws Exception{
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		String survId = request.getParameter("survId");
		
		WvSurvBVo wvsVo =  new WvSurvBVo();
		wvsVo.setSurvId(survId);
		VoUtil.bind(request, wvsVo);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		wvFileSvc.putFileListToModel(survId, model, userVo.getCompId());
		
		//설문기본
		wvsVo = (WvSurvBVo) commonDao.queryVo(wvsVo);
		if(wvsVo != null){
			
			//설문질문상세
			WvSurvQuesDVo wvsQueVo = new WvSurvQuesDVo();
			wvsQueVo.setSurvId(wvsVo.getSurvId());
			wvsQueVo.setCompId(wvsVo.getCompId());
			wvsQueVo.setOrderBy("QUES_SORT_ORDR");

			//질문 목록
			List<WvSurvQuesDVo> wvsQueList = (List<WvSurvQuesDVo>) commonDao.queryList(wvsQueVo);
			
			//질문 답변 목록
			List<WvQuesExamDVo> returnWcQueExamList = new ArrayList<WvQuesExamDVo>();
			
			for(WvSurvQuesDVo wvsQVo : wvsQueList){
				//질문보기상세
				WvQuesExamDVo wvQueExamVo =  new WvQuesExamDVo();
				wvQueExamVo.setSurvId(wvsQVo.getSurvId());
				wvQueExamVo.setCompId(wvsQVo.getCompId());
				wvQueExamVo.setQuesId(wvsQVo.getQuesId());
				
				List<WvQuesExamDVo> wcQueExamList = (List<WvQuesExamDVo>) commonDao.queryList(wvQueExamVo);
					for(WvQuesExamDVo wvQVo : wcQueExamList){
						returnWcQueExamList.add(wvQVo);
					}
			}
			
		
			model.put("wcQueExamList", returnWcQueExamList);
			model.put("wvsQueList", wvsQueList);
			model.put("wvsVo", wvsVo);	
		}
		return LayoutUtil.getJspPath("/wv/viewMySurvApvd");
	}
	
	/** [팝업] 사진 보기 */
    @RequestMapping(value = {"/wv/viewImagePop", "/wv/adm/viewImagePop"})
	public String viewImagePop(HttpServletRequest request,
			@RequestParam(value = "survId", required = true) String survId,
			@RequestParam(value = "quesId", required = true) String quesId,
			@RequestParam(value = "examNo", required = false) String examNo,
			ModelMap model) throws Exception {
		
    	if(examNo == null || "".equals(examNo)){
    		// 세션의 사용자 정보
    		//UserVo userVo = LoginSession.getUser(request);
    		WvSurvQuesDVo wvsQueVo = new WvSurvQuesDVo();
    		VoUtil.bind(request, wvsQueVo);
			//wvsQueVo.setCompId(userVo.getCompId());
			WvSurvQuesDVo wvQueExamVo = (WvSurvQuesDVo) commonDao.queryVo(wvsQueVo);
			model.put("wvQueExamVo", wvQueExamVo);
    	}else{
    		// 설문상세(WV_QUES_EXAM_D) 테이블
        	WvQuesExamDVo wvQueExamVo =  new WvQuesExamDVo();
        	VoUtil.bind(request, wvQueExamVo);
        	wvQueExamVo = (WvQuesExamDVo)commonSvc.queryVo(wvQueExamVo);
        	model.put("wvQueExamVo", wvQueExamVo);
    	}
    	
		return LayoutUtil.getJspPath("/wv/viewImagePop");
	}
    
	/** 나의 신청중인 설문 페이지 */
	@RequestMapping(value = "/wv/listMySurvApvd")
	public String listMySurvApvd(HttpServletRequest request, ModelMap model)throws Exception{
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 조회조건 매핑
		WvSurvBVo wvSurvBVo = new WvSurvBVo();
		VoUtil.bind(request, wvSurvBVo);
		wvSurvBVo.setLogUserUid(userVo.getUserUid());
		wvSurvBVo.setLogUserDeptId(userVo.getDeptId());
		wvSurvBVo.setRegrUid(userVo.getUserUid());
		wvSurvBVo.setLangTyp(langTypCd);
		wvSurvBVo.setRegrUid(userVo.getUserUid());
		/*===============================================================
		 * 설문진행코드 : 1.준비중, 2.승인중, 3.진행중, 4.마감, 5.미저장, 6.임시저장, 9.반려
		 ================================================================*/
		List<String> survList=new ArrayList<String>();
		
		//검색조건 [상태]
		String schCtStat = request.getParameter("schCtStat");
		if(schCtStat != null && !schCtStat.isEmpty()){
			survList.add(schCtStat);
		}else{
			survList.add("1");
			survList.add("2");
			survList.add("6");
			survList.add("9");
		}
		
		wvSurvBVo.setSurvSearchList(survList);
		wvSurvBVo.setQueryLang(langTypCd);
		wvSurvBVo.setCompId(userVo.getCompId());
		Integer recodeCount = commonDao.count(wvSurvBVo);
		PersonalUtil.setPaging(request, wvSurvBVo, recodeCount);
		@SuppressWarnings("unchecked")
		List<WvSurvBVo> wvSurvList = (List<WvSurvBVo>) commonDao.queryList(wvSurvBVo);
		
		model.put("recodeCount", recodeCount);
		model.put("wvSurvList", wvSurvList);
		
		return LayoutUtil.getJspPath("/wv/listMySurvApvd");
	}
	
	/** 마감변경 저장 */
	@RequestMapping(value = "/wv/adm/transEndModSave")
	public String transEndModSave(HttpServletRequest request, ModelMap model)throws Exception{
		String survId =  request.getParameter("survId");
		String endYn = request.getParameter("endYn");
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy-MM-dd 00:00:00", Locale.KOREA );
		Date currentTime = new Date ( );
		String mTime = mSimpleDateFormat.format ( currentTime );
		
		WvSurvBVo wvSurvBVo = new WvSurvBVo();
		wvSurvBVo.setSurvId(survId);
		wvSurvBVo = (WvSurvBVo) commonDao.queryVo(wvSurvBVo);
		
		if(endYn.equals("Y")){
			wvSurvBVo.setSurvEndDt(mTime);
		}else{
			wvSurvBVo.setSurvEndDt(request.getParameter("finDt") + " 23:59:59");
		}
		
		commonDao.update(wvSurvBVo);
		
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		model.put("todo", "location.replace('/wv/adm/listSurv.do?menuId="+request.getParameter("menuId")
				+"');");
		
		//공통 처리 페이지
		return LayoutUtil.getResultJsp();
	}
	
	/** 마감 변경 POP */
	@RequestMapping(value= "/wv/adm/setEndModPop")
	public String setEndModPop(HttpServletRequest request, ModelMap model)throws Exception{
		String survId = request.getParameter("survId");
		
		WvSurvBVo wvsVo = new WvSurvBVo();
		wvsVo.setSurvId(survId);
		wvsVo = (WvSurvBVo) commonDao.queryVo(wvsVo);
		
		model.put("wvsVo", wvsVo);
		
		return LayoutUtil.getJspPath("/wv/adm/setEndModPop");
	}
	
	/** 설문 목록 */
	@RequestMapping(value = "/wv/listSurv")
	public String listSurv(HttpServletRequest request, ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		// 조회조건 매핑
		WvSurvBVo wvSurvBVo = new WvSurvBVo();
		VoUtil.bind(request, wvSurvBVo);
		wvSurvBVo.setLogUserUid(userVo.getUserUid());
		wvSurvBVo.setLogUserDeptId(userVo.getDeptId());
		//추가 - 조직ID 목록
		wvSurvBVo.setSchOrgPids(userVo.getOrgPids());
		wvSurvBVo.setAuthTgtUid("authTgtUid");
		wvSurvBVo.setLangTyp(langTypCd);
		List<String> survList=new ArrayList<String>();
		
		//검색조건 [상태]
		String schCtStat = request.getParameter("schCtStat");
		if(schCtStat != null && !schCtStat.isEmpty()){
			if(schCtStat.equals("ALL"))
			{
				survList.add("3");
				survList.add("4");
			}
			else
				survList.add(schCtStat);
		}else{
			/**
			 * 설문목록 검색조건 디폴트는 '진행중'
			 * */
			survList.add("3");
		}
		
		wvSurvBVo.setSurvSearchList(survList);
		wvSurvBVo.setQueryLang(langTypCd);
		wvSurvBVo.setCompId(userVo.getCompId());
		/**
		 * 설문목록에서 임시저장 상태는 뺌.
		 * */
		//wvSurvBVo.setSurvPrgStatCd("6"); 
		Map<String,Object> rsltMap = wvSurvSvc.getWvSurvMapList(request, wvSurvBVo);
		
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("wvSurvBMapList", rsltMap.get("wvSurvBMapList"));
		model.put("logUserUid", wvSurvBVo.getLogUserUid());
		model.put("logUserDeptId", wvSurvBVo.getLogUserDeptId());
		if(userVo.getOrgPids()!=null)
			model.put("orgPidsToString", Arrays.toString(userVo.getOrgPids()));
			
		return LayoutUtil.getJspPath("/wv/listSurv");
	}
	
	/** 설문 등록 페이지 */
	@RequestMapping(value= {"/wv/adm/setSurv", "/wv/setSurv"})
	public String setSurv(HttpServletRequest request, ModelMap model) throws Exception {
		
		String survId = request.getParameter("survId");
		String fnc = request.getParameter("fnc");
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String authType = null;
		String authGrdCode = null;
		
		//설문아이디가 있다면 수정 모드
		if(survId != null && fnc.equalsIgnoreCase("mod")){
			WvSurvBVo wvsVo = new WvSurvBVo();
			wvsVo.setSurvId(survId);
			wvsVo.setCompId(userVo.getCompId());
			
			wvsVo = (WvSurvBVo) commonDao.queryVo(wvsVo);
			//========================================================================			
			//투표권한 : W
			//부서  : D
			//사용자  : U
			//========================================================================
			
			//투표권한 부서 조회
			authGrdCode = "W";
			authType = "D";
			List<WvSurvUseAuthDVo> voteTgtDeptList = wvSurvSvc.getAuthList(userVo, wvsVo, authType, authGrdCode);
			
			model.put("voteTgtDeptList", voteTgtDeptList);
			model.put("voteTgtDeptCnt", voteTgtDeptList.size());
			
			//투표권한 사용자 조회
			authType = "U";
			
			List<WvSurvUseAuthDVo> voteTgtUserList = wvSurvSvc.getAuthList(userVo, wvsVo, authType, authGrdCode);
			
			model.put("voteTgtUserList", voteTgtUserList);
			model.put("voteTgtUserCnt", voteTgtUserList.size());
			
			//========================================================================
			//조회권한  : R
			//부서 : D
			//사용자 : U
			//========================================================================
			
			//조회권한 부서 조회
			authGrdCode = "R";
			authType = "D";
			
			
			List<WvSurvUseAuthDVo> chkTgtDeptList = wvSurvSvc.getAuthList(userVo, wvsVo, authType, authGrdCode);
			
			model.put("chkTgtDeptList", chkTgtDeptList);
			model.put("chkTgtDeptCnt", chkTgtDeptList.size());
			
			//조회권한 사용자 조회			
			authType = "U";
			List<WvSurvUseAuthDVo> chkTgtUserList = wvSurvSvc.getAuthList(userVo, wvsVo, authType, authGrdCode);
			
			model.put("chkTgtUserList", chkTgtUserList);
			model.put("chkTgtUserCnt", chkTgtUserList.size());
			//========================================================================
			
			
			model.put("wvsVo", wvsVo);
			model.put("fnc", fnc);
			wvFileSvc.putFileListToModel(wvsVo.getSurvId(), model, userVo.getCompId());
		}
		
		
		// 최대 본문 사이즈 model에 추가
		ctAdmNotcSvc.putBodySizeToModel(request, model);
		
		checkPath(request, "index", model);
		model.put("survId", survId);
		
		model.put("listpage", request.getRequestURI().indexOf("adm")>0?"listSurv":"listMySurvApvd");
		return LayoutUtil.getJspPath("/wv/adm/setSurv");
		
		
	}
	
	
	/** 설문에 대한 질문 등록 페이지 */
	@RequestMapping(value= {"/wv/adm/transSetSurvQuesNextSave", "/wv/transSetSurvQuesNextSave"})
	public String transSetSurvQuesNextSave(HttpServletRequest request, ModelMap model) throws Exception {
		
		UploadHandler uploadHandler = null;	
		
		try {	// Multipart 파일 업로드

			uploadHandler = wvFileSvc.upload(request);

			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			//String flagFile =null;
			
			WvSurvBVo wvSurvBVo = new WvSurvBVo();
			//VoUtil.bind(request, wvSurvBVo);
			QueryQueue queryQueue = new QueryQueue();
			wvSurvBVo.setQueryLang(langTypCd);
			wvSurvBVo.setCompId(userVo.getCompId());
			wvSurvBVo.setRegrUid(userVo.getUserUid());
			
			
			String fnc = request.getParameter("fnc");
			
			
			wvSurvSvc.setWvSurvList(request, wvSurvBVo, queryQueue, userVo, model, fnc);
			
			if(!fnc.equalsIgnoreCase("view")){
				// 설문에 대한 사용권한설정 DB입력 **************************************************** (시작)
				String voteTgtDeptArr[] = request.getParameter("voteTgtDeptIds").split("\\,");
				
				String voteTgtUserArr[] = request.getParameter("voteTgtUserIds").split("\\,");
				String chkTgtDeptArr[] = request.getParameter("chkTgtDeptIds").split("\\,");
				
				String chkTgtUserArr[] = request.getParameter("chkTgtUserIds").split("\\,");
				
				WvSurvUseAuthDVo useAuthDVo = new WvSurvUseAuthDVo();
				useAuthDVo.setSurvId(wvSurvBVo.getSurvId());
				
				queryQueue.delete(useAuthDVo);
				
				if(!voteTgtDeptArr[0].equalsIgnoreCase("")){
					//투표 권한 부서 - 하위부서포함여부
					String voteWithSubYnArr[] = request.getParameter("voteWithSubYns").split("\\,");
					for(int i=0; i<voteTgtDeptArr.length; i++){
						WvSurvUseAuthDVo wvSurvUseAuthDVo = new WvSurvUseAuthDVo();
						wvSurvUseAuthDVo.setCompId(wvSurvBVo.getCompId());
						wvSurvUseAuthDVo.setSurvId(wvSurvBVo.getSurvId());
						wvSurvUseAuthDVo.setAuthTgtTypCd("D");
						wvSurvUseAuthDVo.setAuthInclYn(voteWithSubYnArr[i]);
						wvSurvUseAuthDVo.setAuthGradCd("W");
						wvSurvUseAuthDVo.setAuthTgtUid(voteTgtDeptArr[i]);
						queryQueue.insert(wvSurvUseAuthDVo);
					}
				}
				if(!voteTgtUserArr[0].equalsIgnoreCase("")){
					for(int i=0; i<voteTgtUserArr.length; i++){
						WvSurvUseAuthDVo wvSurvUseAuthDVo = new WvSurvUseAuthDVo();
						wvSurvUseAuthDVo.setCompId(wvSurvBVo.getCompId());
						wvSurvUseAuthDVo.setSurvId(wvSurvBVo.getSurvId());
						wvSurvUseAuthDVo.setAuthTgtTypCd("U");
						wvSurvUseAuthDVo.setAuthInclYn("N");
						wvSurvUseAuthDVo.setAuthGradCd("W");
						wvSurvUseAuthDVo.setAuthTgtUid(voteTgtUserArr[i]);
						queryQueue.insert(wvSurvUseAuthDVo);
					}
				}
				if(!chkTgtDeptArr[0].equalsIgnoreCase("")){
					//조회 권한 부서 - 하위부서포함여부
					String chkWithSubYnArr[] = request.getParameter("chkWithSubYns").split("\\,");
					for(int i=0; i<chkTgtDeptArr.length; i++){
						WvSurvUseAuthDVo wvSurvUseAuthDVo = new WvSurvUseAuthDVo();
						wvSurvUseAuthDVo.setCompId(wvSurvBVo.getCompId());
						wvSurvUseAuthDVo.setSurvId(wvSurvBVo.getSurvId());
						wvSurvUseAuthDVo.setAuthTgtTypCd("D");
						wvSurvUseAuthDVo.setAuthInclYn(chkWithSubYnArr[i]);
						wvSurvUseAuthDVo.setAuthGradCd("R");
						wvSurvUseAuthDVo.setAuthTgtUid(chkTgtDeptArr[i]);
						queryQueue.insert(wvSurvUseAuthDVo);
					}
				}
			
				if(!chkTgtUserArr[0].equalsIgnoreCase("")){
					for(int i=0; i<chkTgtUserArr.length; i++){
						WvSurvUseAuthDVo wvSurvUseAuthDVo = new WvSurvUseAuthDVo();
						wvSurvUseAuthDVo.setCompId(wvSurvBVo.getCompId());
						wvSurvUseAuthDVo.setSurvId(wvSurvBVo.getSurvId());
						wvSurvUseAuthDVo.setAuthTgtTypCd("U");
						wvSurvUseAuthDVo.setAuthInclYn("N");
						wvSurvUseAuthDVo.setAuthGradCd("R");
						wvSurvUseAuthDVo.setAuthTgtUid(chkTgtUserArr[i]);
						queryQueue.insert(wvSurvUseAuthDVo);
					}
				}
				
				commonDao.execute(queryQueue);
			}
			// 설문에 대한 사용권한설정 DB입력 **************************************************** (끝)
		
		//model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		model.put("todo", "parent.location.replace('./setSurvQues.do?menuId="+request.getParameter("menuId")
				+ "&fnc="+request.getParameter("fnc")
				+ "&survId=" + wvSurvBVo.getSurvId()
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
	@SuppressWarnings("unchecked")
	@RequestMapping(value= {"/wv/adm/setSurvQues","/wv/setSurvQues"})
	public String setSurvQues(HttpServletRequest request, ModelMap model) throws Exception {

		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String survId = request.getParameter("survId");
		
		if(survId != null && !survId.equalsIgnoreCase("")){	
			
			WvSurvBVo wvSurvBVo = new WvSurvBVo();
			wvSurvBVo.setSurvId(survId);
			
			wvSurvBVo = (WvSurvBVo) commonDao.queryVo(wvSurvBVo);
			
			//설문질문상세
			WvSurvQuesDVo wvsQueVo = new WvSurvQuesDVo();
			wvsQueVo.setSurvId(survId);
			wvsQueVo.setCompId(userVo.getCompId());
			wvsQueVo.setOrderBy("QUES_SORT_ORDR");
			
			List<WvSurvQuesDVo> wvsQueList = (List<WvSurvQuesDVo>) commonDao.queryList(wvsQueVo);
			
			List<WvQuesExamDVo> returnWcQueExamList = new ArrayList<WvQuesExamDVo>();
			
			for(WvSurvQuesDVo wvsQVo : wvsQueList){
				//질문보기상세
				WvQuesExamDVo wvQueExamVo =  new WvQuesExamDVo();
				wvQueExamVo.setSurvId(wvsQVo.getSurvId());
				wvQueExamVo.setCompId(wvsQVo.getCompId());
				wvQueExamVo.setQuesId(wvsQVo.getQuesId());
				
				List<WvQuesExamDVo> wcQueExamList = (List<WvQuesExamDVo>) commonDao.queryList(wvQueExamVo);
					for(WvQuesExamDVo wvQVo : wcQueExamList){
						returnWcQueExamList.add(wvQVo);
					}
			}
			
			model.put("wvSurvBVo", wvSurvBVo);
			model.put("returnWcQueExamList", returnWcQueExamList);
			model.put("wvsQueList", wvsQueList);
			model.put("survId", survId);
		}

		// set, list 로 시작하는 경우 처리
		checkPath(request, "set", model);
		return LayoutUtil.getJspPath("/wv/adm/setSurvQues");
		
		
	}
	
	/** 객관식 질문 팝업*/
	@RequestMapping(value= {"/wv/adm/setMulcQuesPop","/wv/setMulcQuesPop"})
	public String setMulcQuesPop(HttpServletRequest request, ModelMap model) throws Exception {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		String quesId = request.getParameter("quesId");
		String fnc = request.getParameter("fnc");
		String survId = request.getParameter("survId");
		
		if(fnc.equalsIgnoreCase("mod")){
			WvSurvQuesDVo wvsQuesVo = new WvSurvQuesDVo();
			wvsQuesVo.setSurvId(survId);
			wvsQuesVo.setQuesId(quesId);
			wvsQuesVo.setCompId(userVo.getCompId());
			
			wvsQuesVo = (WvSurvQuesDVo) commonDao.queryVo(wvsQuesVo);
			
			WvQuesExamDVo wvQExamVo = new WvQuesExamDVo();
			
			wvQExamVo.setSurvId(survId);
			wvQExamVo.setCompId(userVo.getCompId());
			wvQExamVo.setQuesId(quesId);
			@SuppressWarnings("unchecked")
			List<WvQuesExamDVo> wvQExamList = (List<WvQuesExamDVo>) commonDao.queryList(wvQExamVo);
			
			model.put("wvQExamList", wvQExamList);
			model.put("wvsQuesVo", wvsQuesVo);
		}
		model.put("survId", survId);
		model.put("quesId", quesId);

		checkPath(request, "index", model);
		return LayoutUtil.getJspPath("/wv/adm/setMulcQuesPop");
	}
	
	/** 주관식 질문 팝업*/
	@RequestMapping(value= {"/wv/adm/setOendQuesPop","/wv/setOendQuesPop"})
	public String setOendQuesPop(HttpServletRequest request, ModelMap model) throws Exception {
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String survId = request.getParameter("survId");
		String quesId = request.getParameter("quesId");
		//String fnc = request.getParameter("fnc");
		
		WvSurvQuesDVo wvSurvQuesDVo = new WvSurvQuesDVo();
		VoUtil.bind(request, wvSurvQuesDVo);
		
		wvSurvQuesDVo.setSurvId(survId);
		wvSurvQuesDVo.setQuesId(quesId);
		wvSurvQuesDVo.setCompId(userVo.getCompId());
		wvSurvQuesDVo = (WvSurvQuesDVo) commonDao.queryVo(wvSurvQuesDVo);
		
		model.put("wvSurvBVo", wvSurvQuesDVo);
		model.put("survId", survId);
		model.put("quesId", quesId);
		

		return LayoutUtil.getJspPath("/wv/adm/setOendQuesPop");
	}

	private void checkPath(HttpServletRequest request, String path1,
			ModelMap model) throws SQLException {
		// listXXX 이면
		// 페이지 정보 세팅
		if (path1.startsWith("list") || path1.equals("index")) {
//			CommonVo commonVo = new CommonVoImpl();
//			PersonalUtil.setPaging(request, commonVo, wvSurvSvc.getSurvSurvey().size());
//			model.put("recodeCount", wvSurvSvc.getSurvSurvey().size());
		}
		
		// setXXX 이면
		// 에디터 사용
		if (path1.startsWith("set") || path1.equals("index")) {
			model.addAttribute("JS_OPTS", new String[]{"editor"});
			
		}
	}
	
	/** 팝업 보기 */
	@RequestMapping(value = {"/wv/viewSurvPop", "/cm/viewSurvPop", "/wv/adm/viewSurvPop"})
	public String viewPopupPop(HttpServletRequest request,
			@RequestParam(value = "survId", required = false) String survId,
			ModelMap model) throws Exception {

		if (survId==null || survId.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		WvSurvPopupDVo wvSurvPopupDVo = new WvSurvPopupDVo();
		wvSurvPopupDVo.setSurvId(survId);
		wvSurvPopupDVo.setCompId(userVo.getCompId());
		wvSurvPopupDVo =  (WvSurvPopupDVo) commonSvc.queryVo(wvSurvPopupDVo);
		
		model.put("wvSurvPopupDVo", wvSurvPopupDVo);
		
		return LayoutUtil.getJspPath("/wv/viewSurvPop");
	}
	
	/** 설문참여 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value= {"/wv/viewSurv", "/cm/viewSurvFrm"})
	public String viewSurv(HttpServletRequest request, ModelMap model) throws Exception {
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String survId = request.getParameter("survId");
		
		WvSurvBVo wvsVo =  new WvSurvBVo();
		wvsVo.setSurvId(survId);
		VoUtil.bind(request, wvsVo);
		
		wvFileSvc.putFileListToModel(survId, model, userVo.getCompId());
		
		//설문기본
		wvsVo = (WvSurvBVo) commonDao.queryVo(wvsVo);
		if(wvsVo != null){
						
			//설문질문상세
			WvSurvQuesDVo wvsQueVo = new WvSurvQuesDVo();
			wvsQueVo.setSurvId(wvsVo.getSurvId());
			wvsQueVo.setCompId(wvsVo.getCompId());
			wvsQueVo.setOrderBy("QUES_SORT_ORDR");
			
			
			List<WvSurvQuesDVo> wvsQueList = (List<WvSurvQuesDVo>) commonDao.queryList(wvsQueVo);
			
			List<WvQuesExamDVo> returnWcQueExamList = new ArrayList<WvQuesExamDVo>();
			
			for(WvSurvQuesDVo wvsQVo : wvsQueList){
				//질문보기상세
				WvQuesExamDVo wvQueExamVo =  new WvQuesExamDVo();
				wvQueExamVo.setSurvId(wvsQVo.getSurvId());
				wvQueExamVo.setCompId(wvsQVo.getCompId());
				wvQueExamVo.setQuesId(wvsQVo.getQuesId());
				
				List<WvQuesExamDVo> wcQueExamList = (List<WvQuesExamDVo>) commonDao.queryList(wvQueExamVo);
					for(WvQuesExamDVo wvQVo : wcQueExamList){
						returnWcQueExamList.add(wvQVo);
					}
				
					//단 라디오버튼일때는 각각 넣어줘야함 나중에 정리 필요함.
					WvSurvReplyDVo wvSurvReplyDVo = new WvSurvReplyDVo();
					wvSurvReplyDVo.setSurvId(survId);
					wvSurvReplyDVo.setReplyrUid(userVo.getUserUid());
					wvSurvReplyDVo.setQuesId(wvsQVo.getQuesId());
				
					List<WvSurvReplyDVo> survReplyListAdd = (List<WvSurvReplyDVo>) commonDao.queryList(wvSurvReplyDVo);
					
					wvsQVo.setWvSurvReplyDVo(survReplyListAdd);
			}
			
			
			//멀티체크와 주관식일때 문제 없이 이용가능함.
			WvSurvReplyDVo wvSurvReplyDVo = new WvSurvReplyDVo();
			wvSurvReplyDVo.setSurvId(survId);
			wvSurvReplyDVo.setReplyrUid(userVo.getUserUid());
			
			List<WvSurvReplyDVo> survReplyList = (List<WvSurvReplyDVo>) commonDao.queryList(wvSurvReplyDVo);
			
			model.put("survReplyList", survReplyList);
			model.put("wcQueExamList", returnWcQueExamList);
			model.put("wvsQueList", wvsQueList);
			model.put("wvsVo", wvsVo);	
			checkPath(request, "set", model);
		}
		
		// 프레임 여부
		boolean isFrm=request.getRequestURI().indexOf("Frm")>0;
				
		if(isFrm){// 프레임
			model.put("isFrm", Boolean.TRUE);
			return LayoutUtil.getJspPath("/wv/viewSurv","Frm");
		}
		
		return LayoutUtil.getJspPath("/wv/viewSurv");
	}
	/** 통계보기 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value= "/wv/viewSurvRes")
	public String viewSurvRes(HttpServletRequest request, ModelMap model) throws Exception {

		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String survId = request.getParameter("survId");
		String openYn = request.getParameter("openYn");
		String repetYn = request.getParameter("repetYn");
		
		wvFileSvc.putFileListToModel(survId, model, userVo.getCompId());
		
		WvSurvReplyDVo wvsReVo = new WvSurvReplyDVo();
		
		wvsReVo.setSurvId(survId);
		wvsReVo.setReplyrUid(userVo.getUserUid());
		
		int survReply = commonDao.count(wvsReVo)!=null?commonDao.count(wvsReVo):0; 
		
		if(survReply != 0){ 
			if(openYn.equalsIgnoreCase("N")){
				model.put("message", messageProperties.getMessage("wv.msg.survApvd.openFail", request));
				model.put("todo", "parent.location.replace('/wv/listSurv.do?menuId="+request.getParameter("menuId")
						+"');");
				
				//공통 처리 페이지
				return LayoutUtil.getResultJsp();
			}
		}
		
		String langTypCd = LoginSession.getLangTypCd(request);
		
		WvSurvBVo wvsVo =  new WvSurvBVo();
		wvsVo.setSurvId(survId);
		wvsVo.setQueryLang(langTypCd);
		VoUtil.bind(request, wvsVo);
		//설문기본
		wvsVo = (WvSurvBVo) commonDao.queryVo(wvsVo);
		if(wvsVo != null){
			
			//설문질문상세
			WvSurvQuesDVo wvsQueVo = new WvSurvQuesDVo();
			wvsQueVo.setSurvId(wvsVo.getSurvId());
			wvsQueVo.setCompId(wvsVo.getCompId());
			wvsQueVo.setOrderBy("QUES_SORT_ORDR");

			
			List<WvSurvQuesDVo> wvsQueList = (List<WvSurvQuesDVo>) commonDao.queryList(wvsQueVo);
			
			List<WvQuesExamDVo> returnWcQueExamList = new ArrayList<WvQuesExamDVo>();
			
			for(WvSurvQuesDVo wvsQVo : wvsQueList){
				//질문보기상세
				WvQuesExamDVo wvQueExamVo =  new WvQuesExamDVo();
				wvQueExamVo.setSurvId(wvsQVo.getSurvId());
				wvQueExamVo.setCompId(wvsQVo.getCompId());
				wvQueExamVo.setQuesId(wvsQVo.getQuesId());
				
				List<WvQuesExamDVo> wcQueExamList = (List<WvQuesExamDVo>) commonDao.queryList(wvQueExamVo);
					for(WvQuesExamDVo wvQVo : wcQueExamList){
						
						//질문전체 count
						WvSurvReplyDVo wvReAllCount = new WvSurvReplyDVo();
						wvReAllCount.setCompId(wvQVo.getCompId());
						wvReAllCount.setSurvId(wvQVo.getSurvId());
						wvReAllCount.setQuesId(wvQVo.getQuesId());
						float quesAllCount = commonDao.count(wvReAllCount)!=null?commonDao.count(wvReAllCount):0;

						//질문보기 개당 count
						WvSurvReplyDVo wvReOneCount = new WvSurvReplyDVo();
						wvReOneCount.setCompId(wvQVo.getCompId());
						wvReOneCount.setSurvId(wvQVo.getSurvId());
						wvReOneCount.setQuesId(wvQVo.getQuesId());
						wvReOneCount.setReplyNo(wvQVo.getExamNo());
						//질문보기상세 선택된 개수 count 구하기
						float quesOneCount = commonDao.count(wvReOneCount)!=null?commonDao.count(wvReOneCount):0;
						
						//평균 구하기
						float average = (quesOneCount / quesAllCount) * 100;
						
						wvQVo.setSelectCount(String.valueOf((int)quesOneCount));
						wvQVo.setQuesAverage(String.valueOf((int)average));
						returnWcQueExamList.add(wvQVo);
					}
					
					//주관식 질문  count
					WvSurvReplyDVo wvReOendCount = new WvSurvReplyDVo();
					wvReOendCount.setCompId(wvsQVo.getCompId());
					wvReOendCount.setSurvId(wvsQVo.getSurvId());
					wvReOendCount.setQuesId(wvsQVo.getQuesId());
					wvReOendCount.setReplyNo(-1);
					float quesOendCount = commonDao.count(wvReOendCount)!=null?commonDao.count(wvReOendCount):0;
					
					wvsQVo.setOendCount(String.valueOf((int)quesOendCount));
					
					
			}
			
			
			model.put("returnWcQueExamList", returnWcQueExamList);
			model.put("wvsQueList", wvsQueList);
			model.put("wvsVo", wvsVo);
			model.put("repetYn", repetYn);
			checkPath(request, "set", model);
		}
		
		return LayoutUtil.getJspPath("/wv/viewSurvRes");
	}
	
	/** 설문참여 저장*/
	@RequestMapping(value= {"/wv/viewSurvSave", "/cm/viewSurvSave"})
	public String viewSurvSave(HttpServletRequest request, 
			ModelMap model) throws Exception {
		
		QueryQueue queryQueue = new QueryQueue();
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String survId = request.getParameter("survId");
		
		WvSurvBVo wvsVo = new WvSurvBVo(); 
		wvsVo.setSurvId(survId);
		wvsVo.setCompId(userVo.getCompId());
		wvsVo = (WvSurvBVo) commonDao.queryVo(wvsVo);
		
		if(wvsVo != null){
			//재설문일시 사용사 답변상세 삭제 후 재 입력
			if(wvsVo.getRepetSurvYn().equalsIgnoreCase("Y")){
				WvSurvReplyDVo wvsReVo = new  WvSurvReplyDVo();
				wvsReVo.setSurvId(survId);
				wvsReVo.setReplyrUid(userVo.getUserUid());
				wvsReVo.setCompId(userVo.getCompId());
				queryQueue.delete(wvsReVo);
			}
		}
		
		WvSurvQuesDVo wvsQueVo =  new WvSurvQuesDVo();
		wvsQueVo.setSurvId(survId);
		@SuppressWarnings("unchecked")
		List<WvSurvQuesDVo> returnQuesVo =  (List<WvSurvQuesDVo>) commonDao.queryList(wvsQueVo);
		
  		for(WvSurvQuesDVo wcsQVo : returnQuesVo){
			WvSurvReplyDVo wcsRVo = new WvSurvReplyDVo();
			
			
			wcsRVo.setCompId(wcsQVo.getCompId());
			wcsRVo.setQuesId(wcsQVo.getQuesId());
			wcsRVo.setSurvId(wcsQVo.getSurvId());
			wcsRVo.setReplyDt(wvSurvSvc.currentDay());
			wcsRVo.setReplyrUid(userVo.getUserUid());
			wcsRVo.setReplyrDeptId(userVo.getDeptId());
			
			//객관식
			if(wcsQVo.getMulChoiYn() != null){
				if(wcsQVo.getMulChoiYn().equalsIgnoreCase("Y")){
					String[] survQues = request.getParameterValues(wcsQVo.getQuesId());
					String[] survQuesInput = request.getParameterValues("inputCheck"+wcsQVo.getQuesId());
					//String inputYn = request.getParameter("checkInputYn");
					if(survQues != null){
						for(int i=0; i < survQues.length; i++){
							WvSurvReplyDVo cloneWvSurv =(WvSurvReplyDVo) wcsRVo.clone();
							cloneWvSurv.setReplyNo(Integer.parseInt(survQues[i]));
							if(survQuesInput != null){
								//입력여부Y이면 객관식에 text 가능함.
								cloneWvSurv.setMulcInputReplyCont(survQuesInput[Integer.parseInt(survQues[i])-1]);
							}
							queryQueue.insert(cloneWvSurv);
						}
					}
				}else if(wcsQVo.getMulChoiYn().equalsIgnoreCase("N")){
					String survQues = request.getParameter(wcsQVo.getQuesId());
					String survQuesInput = request.getParameter("inputRadio"+ wcsQVo.getQuesId() + survQues);
					//String inputYn = request.getParameter("radioInputYn");
					if(survQues != null){
						wcsRVo.setReplyNo(Integer.parseInt(survQues));
						if(survQuesInput != null){
							//입력여부Y이면 객관식에 text 가능함.
							wcsRVo.setMulcInputReplyCont(survQuesInput);
							
						}
						queryQueue.insert(wcsRVo);
					}
				}
			//주관식
			}else{
				String survQues = request.getParameter(wcsQVo.getQuesId());
				
				if(!survQues.isEmpty()){
					wcsRVo.setReplyNo(-1);
					wcsRVo.setOendReplyCont(survQues);
					queryQueue.insert(wcsRVo);
				}
			}
			
		}
		
		commonDao.execute(queryQueue);		
		
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		
		String returnFunc = ParamUtil.getRequestParam(request, "returnFunc", false);
		
		if(returnFunc!=null && !returnFunc.isEmpty()){
			model.put("todo", "parent."+returnFunc+"();");
		}else{
			model.put("todo", "parent.location.replace('/wv/listSurv.do?menuId="+request.getParameter("menuId")+"');");
		}
		
		//공통 처리 페이지
		return LayoutUtil.getResultJsp();
	}
	
	/** 부서별 통계 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value= "/wv/listDeptStacsPop")
	public String listDeptStacsPop(HttpServletRequest request, ModelMap model) throws Exception {
		
		String survId = request.getParameter("survId");
		String quesId = request.getParameter("quesId");
		String quesCount = request.getParameter("quesCount");
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		//#######################################################################################
		
		//VoUtil.bind(request, wvsVo);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
			//설문질문상세 (질문순서때문에 사용함)
			WvSurvQuesDVo wvsQueVo = new WvSurvQuesDVo();
			wvsQueVo.setQuesId(quesId);
			wvsQueVo.setSurvId(survId);
			wvsQueVo.setCompId(userVo.getCompId());
			
			wvsQueVo = (WvSurvQuesDVo) commonDao.queryVo(wvsQueVo);
			
			List<WvQuesExamDVo> wcQueExamList = new ArrayList<WvQuesExamDVo>();
			
			//질문보기상세
			WvQuesExamDVo wvQueExamVo =  new WvQuesExamDVo();
			wvQueExamVo.setSurvId(survId);
			wvQueExamVo.setCompId(userVo.getCompId());
			wvQueExamVo.setQuesId(quesId);
			
			WvSurvReplyDVo wvSurvReplyDistinct = new WvSurvReplyDVo();
			wvSurvReplyDistinct.setInstanceQueryId("selectWvSurvReplyDssss");
			wvSurvReplyDistinct.setSurvId(survId);
			wvSurvReplyDistinct.setCompId(userVo.getCompId());
			wvSurvReplyDistinct.setQuesId(quesId);
			wvSurvReplyDistinct.setQueryLang(langTypCd);
			
			List<WvSurvReplyDVo> wvSurvReplyDeptList = (List<WvSurvReplyDVo>) commonDao.queryList(wvSurvReplyDistinct);
			
			for(WvSurvReplyDVo wvRe : wvSurvReplyDeptList){
				List<WvQuesExamDVo> returnWcQueExamList = new ArrayList<WvQuesExamDVo>();
				wcQueExamList = (List<WvQuesExamDVo>) commonDao.queryList(wvQueExamVo);
				for(WvQuesExamDVo wvQVo : wcQueExamList){
					
					
						//질문전체 count
						WvSurvReplyDVo wvReAllCount = new WvSurvReplyDVo();
						wvReAllCount.setCompId(wvQVo.getCompId());
						wvReAllCount.setSurvId(wvQVo.getSurvId());
						wvReAllCount.setQuesId(wvQVo.getQuesId());
						wvReAllCount.setReplyrDeptId(wvRe.getReplyrDeptId());
						float quesAllCount = commonDao.count(wvReAllCount)!=null?commonDao.count(wvReAllCount):0;
		
						//질문보기 개당 count
						WvSurvReplyDVo wvReOneCount = new WvSurvReplyDVo();
						wvReOneCount.setCompId(wvQVo.getCompId());
						wvReOneCount.setSurvId(wvQVo.getSurvId());
						wvReOneCount.setQuesId(wvQVo.getQuesId());
						wvReOneCount.setReplyNo(wvQVo.getExamNo());
						wvReOneCount.setReplyrDeptId(wvRe.getReplyrDeptId());
						//질문보기상세 선택된 개수 count 구하기
						float quesOneCount = commonDao.count(wvReOneCount)!=null?commonDao.count(wvReOneCount):0;
						
						//평균 구하기
//						float average = (quesOneCount / quesAllCount) * 100;
						
						wvQVo.setSelectCount(String.valueOf((int)quesOneCount));
						wvRe.setDeptTotalCount(String.valueOf((int)quesAllCount));
						returnWcQueExamList.add(wvQVo);
						
						}
					wvRe.setQuesExam(returnWcQueExamList);
			}
			
			model.put("wvSurvReplyDeptList", wvSurvReplyDeptList);
			model.put("wcQueExamList", wcQueExamList);
			model.put("wvsQueVo", wvsQueVo);
			model.put("quesCount",quesCount);
			checkPath(request, "set", model);
	//##############################################################################################
		
		
		checkPath(request, "index", model);
		return LayoutUtil.getJspPath("/wv/listDeptStacsPop");
	}
	
	
	/** 설문 최종 저장*/
	@RequestMapping(value= {"/wv/adm/transSetSurvQuesSave", "/wv/transSetSurvQuesSave"})
	public String setSurvQuesSave(HttpServletRequest request, ModelMap model) throws Exception {
		QueryQueue queryQueue = new QueryQueue();
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String survId = request.getParameter("survId");
		
		
		WvSurvBVo wvsThisVo = new WvSurvBVo();
		wvsThisVo.setSurvId(survId);
		wvsThisVo.setCompId(userVo.getCompId());
		
		WvSurvPolcDVo wvsPolcVo = new WvSurvPolcDVo();
		wvsPolcVo.setCompId(userVo.getCompId());
		
		wvsPolcVo = (WvSurvPolcDVo) commonDao.queryVo(wvsPolcVo);
		
		if(wvsPolcVo!=null && wvsPolcVo.getApvdYn()!=null && wvsPolcVo.getApvdYn().equalsIgnoreCase("Y")){
			wvsThisVo.setSurvPrgStatCd("2");
		}else{
			wvsThisVo.setSurvPrgStatCd("3");
		}
		
		queryQueue.update(wvsThisVo);
		
		commonDao.execute(queryQueue);
		
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		
		/**
		 * 관리자권한자 상관없이 사용자페이지에서 처리시 사용자페이지로 이동해야함.
		 */
		if(request.getRequestURI().indexOf("/adm/") > 0){   //&& userVo.isAdmin() && userVo.isSysAdmin()
			model.put("todo", "parent.location.replace('/wv/adm/listSurvApvd.do?menuId="+request.getParameter("menuId")
					+"');");
		}else{
			model.put("todo", "parent.location.replace('/wv/listMySurvApvd.do?menuId="+request.getParameter("menuId")
					+"');");
		}
		
		checkPath(request, "index", model);
		return LayoutUtil.getResultJsp();
	}
	
	/** 주관식 답변보기*/
	@RequestMapping(value= "/wv/listOendAnsPop")
	public String listOendAnsPop(HttpServletRequest request, ModelMap model) throws Exception {
		
		String survId = request.getParameter("survId");
		String quesId = request.getParameter("quesId");
		String quesCount = request.getParameter("quesCount");
		
		model.put("survId", survId);
		model.put("quesId", quesId);
		model.put("quesCount", quesCount);
		
		checkPath(request, "index", model);
		return LayoutUtil.getJspPath("/wv/listOendAnsPop");
	}
	
	/** 주관식 답변보기*/
	@RequestMapping(value= "/wv/listOendAnsFrm")
	public String listOendAnsFrm(HttpServletRequest request, ModelMap model) throws Exception {
		
		String survId = request.getParameter("survId");
		String quesId = request.getParameter("quesId");
		String quesCount = request.getParameter("quesCount");
		request.setAttribute("pageRowCnt", 10);//RowCnt 삽입
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		WvSurvQuesDVo wvsQueVo = new WvSurvQuesDVo();
		wvsQueVo.setQueryLang(langTypCd);
		wvsQueVo.setCompId(userVo.getCompId());
		wvsQueVo.setQuesId(quesId);
		wvsQueVo.setSurvId(survId);

		wvsQueVo = (WvSurvQuesDVo) commonDao.queryVo(wvsQueVo);
		
		// 조회조건 매핑
		WvSurvReplyDVo wvsReVo = new WvSurvReplyDVo();
//		VoUtil.bind(request, wvSurvBVo);
		
		wvsReVo.setQueryLang(langTypCd);
		wvsReVo.setCompId(userVo.getCompId());
		wvsReVo.setSurvId(survId);
		wvsReVo.setQuesId(quesId);
		wvsReVo.setReplyNo(-1);
		wvsReVo.setOrderBy("REPLY_DT DESC");
		Map<String,Object> rsltMap = wvSurvSvc.getOendReplyCont(request, wvsReVo);
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("wvSurvBMapList", rsltMap.get("wvSurvBMapList"));
		model.put("wvsQueVo", wvsQueVo);
		model.put("quesCount", quesCount);
		
		checkPath(request, "index", model);
		return LayoutUtil.getJspPath("/wv/listOendAnsFrm");
	}
	
	/** 설문정책 */
	@RequestMapping(value= "/wv/adm/setSurvPolc")
	public String setSurvPolc(HttpServletRequest request, ModelMap model) throws Exception {
		
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		//String survPolc = request.getParameter("survPolc");
		WvSurvPolcDVo wvsPolcVo = new WvSurvPolcDVo();
		wvsPolcVo.setCompId(userVo.getCompId());
		wvsPolcVo.setQueryLang(langTypCd);
		
		wvsPolcVo = (WvSurvPolcDVo) commonDao.queryVo(wvsPolcVo);
		
		String apvdY = null;
		String apvdN = null;
		
		if(wvsPolcVo != null){
			if(wvsPolcVo.getApvdYn().equalsIgnoreCase("Y")){
				apvdY = "true";
				apvdN = "false";
			}else if(wvsPolcVo.getApvdYn().equalsIgnoreCase("N")){
				apvdY = "false";
				apvdN = "true";
			}
		}else{
			apvdN = "true";
		}
		
		model.put("apvdY", apvdY);
		model.put("apvdN", apvdN);
		
		checkPath(request, "index", model);
		return LayoutUtil.getJspPath("/wv/adm/setSurvPolc");
	}
	/** 설문정책 등록 */
	@RequestMapping(value= "/wv/adm/setSurvPolcSave")
	public String setSurvPolcSave(HttpServletRequest request, ModelMap model) throws Exception {
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String survPolc = request.getParameter("survPolc");
		WvSurvPolcDVo wvsPolcVo = new WvSurvPolcDVo();
		wvsPolcVo.setCompId(userVo.getCompId());
		wvsPolcVo.setQueryLang(langTypCd);
		
		wvsPolcVo = (WvSurvPolcDVo) commonDao.queryVo(wvsPolcVo);
		
		QueryQueue  queryQueue = new QueryQueue();
		if(wvsPolcVo == null){
			WvSurvPolcDVo wvsPolcReg = new WvSurvPolcDVo();
			
			//설문정책 Y,N
			wvsPolcReg.setApvdYn(survPolc);
			//등록자
			wvsPolcReg.setRegrUid(userVo.getUserUid());
			//등록시간
			wvsPolcReg.setRegDt(wvSurvSvc.currentDay());
			
			//수정자
			wvsPolcReg.setModrUid(userVo.getUserUid());
			//수정시간
			wvsPolcReg.setModDt(wvSurvSvc.currentDay());
			
			wvsPolcReg.setCompId(userVo.getCompId());
			wvsPolcReg.setQueryLang(langTypCd);
			
			queryQueue.insert(wvsPolcReg);
		}else{
			//설문정책 Y,N
			wvsPolcVo.setApvdYn(survPolc);
			//수정자
			wvsPolcVo.setModrUid(userVo.getUserUid());
			//수정시간
			wvsPolcVo.setModDt(wvSurvSvc.currentDay());
			queryQueue.update(wvsPolcVo);
		}
		checkPath(request, "index", model);
		
		commonDao.execute(queryQueue);
		
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		model.put("todo", "parent.location.replace('/wv/adm/setSurvPolc.do?menuId="+request.getParameter("menuId")
				+"');");
		
		//공통 처리 페이지
		return LayoutUtil.getResultJsp();
	}
	
	/** 승인대기 설문 */
	@RequestMapping(value= {"/wv/listSurvApvd","/wv/adm/listSurvApvd"})
	public String listSurvApvd(HttpServletRequest request, ModelMap model) throws Exception {
		
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 조회조건 매핑
		WvSurvBVo wvSurvBVo = new WvSurvBVo();
		VoUtil.bind(request, wvSurvBVo);
		//SecuUtil.hasAuth 사용자의 관리권한 여부 확인
		
		// 시스템 관리자 여부
		//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		// 시스템 관리자가 아닌 경우에는 - 회사ID 추가
		//if(!isSysAdmin){
			wvSurvBVo.setCompId(userVo.getCompId());
		//}
				
		Map<String,Object> rsltMap = new HashMap<String,Object>();
		if(!userVo.isAdmin() && !userVo.isSysAdmin() && !SecuUtil.hasAuth(request, "A", null)){
			rsltMap.put("wvSurvBMapList", null);
			rsltMap.put("recodeCount", 0);
		}else{
			List<String> survList=new ArrayList<String>();
			survList.add("2");
			wvSurvBVo.setSurvSearchList(survList);

			wvSurvBVo.setQueryLang(langTypCd);
			rsltMap = wvSurvSvc.getSurvApvdMapList(request, wvSurvBVo);
		}

		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("wvSurvBMapList", rsltMap.get("wvSurvBMapList"));

//		checkPath(request, "index", model);
		return LayoutUtil.getJspPath("/wv/adm/listSurvApvd");
	}
	
	/** 설문승인 view */
	@SuppressWarnings("unchecked")
	@RequestMapping(value= {"/wv/adm/viewSurvApvd","/wv/viewSurvApvd"})
	public String viewSurvApvd(HttpServletRequest request, ModelMap model) throws Exception {
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		String survId = request.getParameter("survId");
		
		WvSurvBVo wvsVo =  new WvSurvBVo();
		wvsVo.setSurvId(survId);
		VoUtil.bind(request, wvsVo);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		wvFileSvc.putFileListToModel(survId, model, userVo.getCompId());
		
		//설문기본
		wvsVo = (WvSurvBVo) commonDao.queryVo(wvsVo);
		if(wvsVo != null){
			
			//설문질문상세
			WvSurvQuesDVo wvsQueVo = new WvSurvQuesDVo();
			wvsQueVo.setSurvId(wvsVo.getSurvId());
			wvsQueVo.setCompId(wvsVo.getCompId());
			wvsQueVo.setOrderBy("QUES_SORT_ORDR");
			
			List<WvSurvQuesDVo> wvsQueList = (List<WvSurvQuesDVo>) commonDao.queryList(wvsQueVo);
			
			List<WvQuesExamDVo> returnWcQueExamList = new ArrayList<WvQuesExamDVo>();
			
			for(WvSurvQuesDVo wvsQVo : wvsQueList){
				//질문보기상세
				WvQuesExamDVo wvQueExamVo =  new WvQuesExamDVo();
				wvQueExamVo.setSurvId(wvsQVo.getSurvId());
				wvQueExamVo.setCompId(wvsQVo.getCompId());
				wvQueExamVo.setQuesId(wvsQVo.getQuesId());
				
				List<WvQuesExamDVo> wcQueExamList = (List<WvQuesExamDVo>) commonDao.queryList(wvQueExamVo);
					for(WvQuesExamDVo wvQVo : wcQueExamList){
						returnWcQueExamList.add(wvQVo);
					}
			}
			
		
			model.put("wcQueExamList", returnWcQueExamList);
			model.put("wvsQueList", wvsQueList);
			model.put("wvsVo", wvsVo);	
			checkPath(request, "set", model);
		}
		
		return LayoutUtil.getJspPath("/wv/adm/viewSurvApvd");
	}
	
	/** 설문정책 등록 */
	@RequestMapping(value= {"/wv/adm/setSurvApvdSave","/wv/setSurvApvdSave"})
	public String setSurvApvdPolcSave(HttpServletRequest request, ModelMap model) throws Exception {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String survPolc = request.getParameter("apvdYn");
	
		String survId = request.getParameter("survId");
		String returnSurvCont = request.getParameter("returnSurvCont");
		
		WvSurvBVo wvsVo = new WvSurvBVo();
		wvsVo.setSurvId(survId);
		wvsVo.setCompId(userVo.getCompId());
		
		if(survPolc.equalsIgnoreCase("Y")){
			wvsVo.setSurvPrgStatCd("3");
		}else if(survPolc.equalsIgnoreCase("N")){
			wvsVo.setSurvPrgStatCd("9");
			wvsVo.setAdmRjtOpinCont(returnSurvCont);
		}
		
		QueryQueue  queryQueue = new QueryQueue();
		
		queryQueue.update(wvsVo);
	
		checkPath(request, "index", model);
		
		commonDao.execute(queryQueue);
		
		
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		model.put("todo", "parent.location.replace('/wv/"+(request.getRequestURI().indexOf("adm")>0?"adm":"")+"/listSurvApvd.do?menuId="+request.getParameter("menuId")
				+"');");
		
		//공통 처리 페이지
		return LayoutUtil.getResultJsp();
	}
	
	/** 설문목록 관리자
	 *	/wv/listSurv같은 이름으로 있어서
	 *  /wv/adm/listSurv로 맵핑 후 
	 *  listSurvAdm로 함수 이름.  
	 */
	
	@RequestMapping(value= "/wv/adm/listSurv")
	public String listSurvAdm(HttpServletRequest request, ModelMap model) throws Exception {
		
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 조회조건 매핑
		WvSurvBVo wvSurvBVo = new WvSurvBVo();
		VoUtil.bind(request, wvSurvBVo);
		wvSurvBVo.setLogUserUid(userVo.getUserUid());
		wvSurvBVo.setLangTyp(langTypCd);
		
		List<String> survList=new ArrayList<String>();
		//검색조건 [상태]
		String schCtStat = request.getParameter("schCtStat");
		if(schCtStat != null && schCtStat != ""){
			survList.add(schCtStat);
			wvSurvBVo.setSurvSearchList(survList);
		}else{
			survList.add("1");
			survList.add("3");
			survList.add("4");
			wvSurvBVo.setSurvSearchList(survList);
		}
		
		wvSurvBVo.setQueryLang(langTypCd);
		
		// 시스템 관리자 여부
		//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		// 시스템 관리자가 아닌 경우에는 - 회사ID 추가
		//if(!isSysAdmin){
			wvSurvBVo.setCompId(userVo.getCompId());
		//}
				
		Map<String,Object> rsltMap = wvSurvSvc.getAdmSurvMapList(request, wvSurvBVo);
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("wvSurvBMapList", rsltMap.get("wvSurvBMapList"));
		model.put("logUserUid", wvSurvBVo.getLogUserUid());
		model.put("schCtStat", schCtStat);
		checkPath(request, "index", model);
		return LayoutUtil.getJspPath("/wv/adm/listSurv");
	}
	
	
	/** 통계보기 관리자
	 *	/wv/viewSurvRes 같은 이름으로 있어서
	 *  /wv/adm/viewSurvRes 로 맵핑 후 
	 *  viewSurvResAdm로 지명
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value= "/wv/adm/viewSurvRes")
	public String viewSurvResAdm(HttpServletRequest request, ModelMap model) throws Exception {

		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		String survId = request.getParameter("survId");
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		wvFileSvc.putFileListToModel(survId, model, userVo.getCompId());
		
		WvSurvBVo wvsVo =  new WvSurvBVo();
		wvsVo.setSurvId(survId);
		VoUtil.bind(request, wvsVo);
		//설문기본
		wvsVo = (WvSurvBVo) commonDao.queryVo(wvsVo);
		if(wvsVo != null){
			if(request.getParameter("survStatCd").equals("1") || request.getParameter("survStatCd").equals("2") || request.getParameter("survStatCd").equals("5")){
					model.put("message", messageProperties.getMessage("wv.msg.admSurv.doNot", request));
					model.put("todo", "parent.location.replace('/wv/adm/listSurv.do?menuId="+request.getParameter("menuId")
							+"');");
					//공통 처리 페이지
					return LayoutUtil.getResultJsp();
			}else if(request.getParameter("survStatCd").equals("9")){
				model.put("message", messageProperties.getMessage("wv.msg.rejectCont", request) +"\r\n"+ wvsVo.getAdmRjtOpinCont());
				model.put("todo", "parent.location.replace('/wv/adm/listSurv.do?menuId="+request.getParameter("menuId")
						+"');");
				//공통 처리 페이지
				return LayoutUtil.getResultJsp();
			}
			
			//설문질문상세
			WvSurvQuesDVo wvsQueVo = new WvSurvQuesDVo();
			wvsQueVo.setSurvId(wvsVo.getSurvId());
			wvsQueVo.setCompId(wvsVo.getCompId());
			wvsQueVo.setOrderBy("QUES_SORT_ORDR");
			
			List<WvSurvQuesDVo> wvsQueList = (List<WvSurvQuesDVo>) commonDao.queryList(wvsQueVo);
			
			List<WvQuesExamDVo> returnWcQueExamList = new ArrayList<WvQuesExamDVo>();
			
			
			for(WvSurvQuesDVo wvsQVo : wvsQueList){
				//질문보기상세
				WvQuesExamDVo wvQueExamVo =  new WvQuesExamDVo();
				wvQueExamVo.setSurvId(wvsQVo.getSurvId());
				wvQueExamVo.setCompId(wvsQVo.getCompId());
				wvQueExamVo.setQuesId(wvsQVo.getQuesId());
				
				List<WvQuesExamDVo> wcQueExamList = (List<WvQuesExamDVo>) commonDao.queryList(wvQueExamVo);
					for(WvQuesExamDVo wvQVo : wcQueExamList){
						
						//질문전체 count
						WvSurvReplyDVo wvReAllCount = new WvSurvReplyDVo();
						wvReAllCount.setCompId(wvQVo.getCompId());
						wvReAllCount.setSurvId(wvQVo.getSurvId());
						wvReAllCount.setQuesId(wvQVo.getQuesId());
						float quesAllCount = commonDao.count(wvReAllCount)!=null?commonDao.count(wvReAllCount):0;

						//질문보기 개당 count
						WvSurvReplyDVo wvReOneCount = new WvSurvReplyDVo();
						wvReOneCount.setCompId(wvQVo.getCompId());
						wvReOneCount.setSurvId(wvQVo.getSurvId());
						wvReOneCount.setQuesId(wvQVo.getQuesId());
						wvReOneCount.setReplyNo(wvQVo.getExamNo());
						//질문보기상세 선택된 개수 count 구하기
						float quesOneCount = commonDao.count(wvReOneCount)!=null?commonDao.count(wvReOneCount):0;
						
						//평균 구하기
						float average = (quesOneCount / quesAllCount) * 100;
						
						wvQVo.setSelectCount(String.valueOf((int)quesOneCount));
						wvQVo.setQuesAverage(String.valueOf((int)average));
						returnWcQueExamList.add(wvQVo);
					}
					
					//주관식 질문  count
					WvSurvReplyDVo wvReOendCount = new WvSurvReplyDVo();
					wvReOendCount.setCompId(wvsQVo.getCompId());
					wvReOendCount.setSurvId(wvsQVo.getSurvId());
					wvReOendCount.setQuesId(wvsQVo.getQuesId());
					wvReOendCount.setReplyNo(-1);
					float quesOendCount = commonDao.count(wvReOendCount)!=null?commonDao.count(wvReOendCount):0;
					
					wvsQVo.setOendCount(String.valueOf((int)quesOendCount));
					
					
			}
			
			
			model.put("returnWcQueExamList", returnWcQueExamList);
			model.put("wvsQueList", wvsQueList);
			model.put("wvsVo", wvsVo);	
			checkPath(request, "set", model);
		}
		
		return LayoutUtil.getJspPath("/wv/adm/viewSurvRes");
	}
	
	/** 객관식입력항목 답변보기*/
	@RequestMapping(value= "/wv/listMulcAnsPop")
	public String listMulcAnsPop(HttpServletRequest request, ModelMap model) throws Exception {
		
		String survId = request.getParameter("survId");
		String quesId = request.getParameter("quesId");
		String replyNo = request.getParameter("replyNo");
		
		model.put("survId", survId);
		model.put("quesId", quesId);
		model.put("replyNo", replyNo);
		
		return LayoutUtil.getJspPath("/wv/listMulcAnsPop");
	}
	
	/** 객관식입력항목 답변보기*/
	@RequestMapping(value= "/wv/listMulcAnsFrm")
	public String listMulcAnsFrm(HttpServletRequest request, ModelMap model) throws Exception {
		
		String survId = request.getParameter("survId");
		String quesId = request.getParameter("quesId");
		String replyNo = request.getParameter("replyNo");
		request.setAttribute("pageRowCnt", 10);//RowCnt 삽입
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		WvSurvQuesDVo wvsQueVo = new WvSurvQuesDVo();
		wvsQueVo.setQueryLang(langTypCd);
		wvsQueVo.setCompId(userVo.getCompId());
		wvsQueVo.setQuesId(quesId);
		wvsQueVo.setSurvId(survId);

		wvsQueVo = (WvSurvQuesDVo) commonDao.queryVo(wvsQueVo);
		
		// 조회조건 매핑
		WvSurvReplyDVo wvsReVo = new WvSurvReplyDVo();
//		
		
		wvsReVo.setQueryLang(langTypCd);
		wvsReVo.setCompId(userVo.getCompId());
		wvsReVo.setSurvId(survId);
		wvsReVo.setQuesId(quesId);
		wvsReVo.setReplyNo(Integer.parseInt(replyNo));
		wvsReVo.setOrderBy("REPLY_DT DESC");
		wvsReVo.setMulcInputReplyContYn("Y");
		VoUtil.bind(request, wvsReVo);
		
		Map<String,Object> rsltMap = wvSurvSvc.getOendReplyCont(request, wvsReVo);
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("wvSurvBMapList", rsltMap.get("wvSurvBMapList"));
		model.put("wvsQueVo", wvsQueVo);
		
		checkPath(request, "index", model);
		return LayoutUtil.getJspPath("/wv/listMulcAnsFrm");
	}
	
	/** [팝업] 사진 선택 */
	@RequestMapping(value = {"/wv/setImagePop", "/wv/adm/setImagePop"})
	public String setImagePop(HttpServletRequest request,
			@Parameter(name="bcId", required=false) String bcId,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/wv/adm/setImagePop");
	}
	
	/** [히든프레임] 사진 업로드 */
	@RequestMapping(value = {"/wv/transImage", "/wv/adm/transImage"})
	public String transImage(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		UploadHandler uploadHandler = null;
		boolean tempFileDel = false;
		try{
			uploadHandler = uploadManager.createHandler(request, "temp", "wv");
			//Map<String, File> fileMap = uploadHandler.upload();//업로드 파일 정보
			//Map<String, String> param = uploadHandler.getParamMap();//파라미터 정보
			uploadHandler.upload();
			
			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			QueryQueue queryQueue = new QueryQueue();
			//List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
			WvSurvFileDVo wvSurvfileDvo = new WvSurvFileDVo();
			WvSurvFileDVo newwbSurvFileDVo = (WvSurvFileDVo)wvFileSvc.savePhoto(request,"photo", -1, wvSurvfileDvo , queryQueue);
//				if(newwbSurvFileDVo != null){
//					deletedFileList.add(newwbSurvFileDVo);					
//					// 파일 삭제
//					wvFileSvc.deleteDiskFiles(deletedFileList);
//				}
			
			commonSvc.execute(queryQueue);
			//String fileDir = distManager.getContextProperty("distribute.web.local.root")+distPath;
			model.put("todo", "parent.setImage('"+request.getParameter("quesNum")+"','"+request.getParameter("examNum")+"','"+newwbSurvFileDVo.getFileId()+"', '"+newwbSurvFileDVo.getDispNm()+"', '"+newwbSurvFileDVo.getSavePath().replace('\\', '/')+"');");
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
	@RequestMapping(value = {"/wb/adm/transImageDel"})
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
	
	/** 설문삭제 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/wv/adm/transSetSurvDel", "/wv/transSetSurvDel"})
	public String transSetSurvDel(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		
		try{
			
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String survId  = (String) object.get("survId");
			if (survId == null || survId.equalsIgnoreCase("")) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			UserVo userVo = LoginSession.getUser(request);
			// 설문 그룹 삭제
			QueryQueue queryQueue = new QueryQueue();
			/** 삭제순서 
			 * 1.설문답변상세 (WV_SURV_REPLY_D)삭제
			 * 2.설문파일상세 (WV_SURV_FILD_D)삭제
			 * 3.질문보기상세 (WV_QUES_EXAM_D)삭제
			 * 4.설문질문상세 (WV_SURV_QUES_D)삭제
			 * 5.설문기본  (WV_SURV_B)삭제
			 * 6.설문사용권한상세 (WV_SURV_USE_AUTH_D)삭제
			 * */
			
			//해당 설문 내용을 모두 조회
			WvSurvBVo wvsThisVo =  new WvSurvBVo();
			wvsThisVo.setSurvId(survId);
			wvsThisVo = (WvSurvBVo) commonDao.queryVo(wvsThisVo);
			
			//설문 유효성 검사
			if(wvsThisVo == null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
				
			//설문답변상세 삭제
			WvSurvReplyDVo wvsReVo = new WvSurvReplyDVo();
			wvsReVo.setSurvId(wvsThisVo.getSurvId());
			List<WvSurvReplyDVo> wvsReList = (List<WvSurvReplyDVo>) commonDao.queryList(wvsReVo);
			if(wvsReList.size() != 0){
				for(WvSurvReplyDVo wvsRe :wvsReList){
					WvSurvReplyDVo wvsReDel = new WvSurvReplyDVo();
					wvsReDel.setSurvId(wvsRe.getSurvId());
					wvsReDel.setCompId(wvsRe.getCompId());
					wvsReDel.setQuesId(wvsRe.getQuesId());
					wvsReDel.setReplyNo(wvsRe.getReplyNo());
					//답변자삭제
					queryQueue.delete(wvsReDel);
				}
			}
			
			//질문보기상세 & 이미지 삭제
			WvQuesExamDVo wvQExamVo = new WvQuesExamDVo();
			wvQExamVo.setSurvId(wvsThisVo.getSurvId());
			
			List<WvQuesExamDVo> wvQExamList = (List<WvQuesExamDVo>) commonDao.queryList(wvQExamVo);
			if(wvQExamList.size() != 0){
				for(WvQuesExamDVo wvQE : wvQExamList){
					if(wvQE.getImgSurvFileId() != null && !wvQE.getImgSurvFileId().equalsIgnoreCase("")){
						WvSurvFileDVo wvsFileVo = new WvSurvFileDVo();
						wvsFileVo.setFileId(Integer.parseInt(wvQE.getImgSurvFileId()));
						//질문보기상세관련 설문파일상세 File 삭제
						queryQueue.delete(wvsFileVo);
					}
					WvQuesExamDVo wvQExamDel = new WvQuesExamDVo();
					wvQExamDel.setSurvId(wvQE.getSurvId());
					wvQExamDel.setQuesId(wvQE.getQuesId());
					wvQExamDel.setCompId(wvQE.getCompId());
					wvQExamDel.setExamNo(wvQE.getExamNo());
					//질문보기상세 삭제
					queryQueue.delete(wvQExamDel);
				}
			}
			
			//설문질문상세 삭제
			WvSurvQuesDVo wvsQuesVo = new WvSurvQuesDVo();
			wvsQuesVo.setSurvId(wvsThisVo.getSurvId());
			
			List<WvSurvQuesDVo> wvsQuesList = (List<WvSurvQuesDVo>) commonDao.queryList(wvsQuesVo);
			if(wvsQuesList.size() != 0){
				for(WvSurvQuesDVo wvsQ: wvsQuesList){
					if(wvsQ.getImgSurvFileId() != null){
						if(!wvsQ.getImgSurvFileId().equalsIgnoreCase("")){
							WvSurvFileDVo wvsFileVo = new WvSurvFileDVo();
							wvsFileVo.setFileId(Integer.parseInt(wvsQ.getImgSurvFileId()));
							//설문질문상세관련 설문파일상세 File 삭제
							queryQueue.delete(wvsFileVo);
						}
					}
					WvSurvQuesDVo wvsQDel = new WvSurvQuesDVo();
					wvsQDel.setSurvId(wvsQ.getSurvId());
					wvsQDel.setQuesId(wvsQ.getQuesId());
					wvsQDel.setCompId(wvsQ.getCompId());
					
					//설문질문상세 삭제
					queryQueue.delete(wvsQDel);
				}
			}
			
			//설문사용권한상세 삭제
			WvSurvUseAuthDVo wvsUseAuthVo = new WvSurvUseAuthDVo();
			wvsUseAuthVo.setSurvId(wvsThisVo.getSurvId());
			
			List<WvSurvUseAuthDVo> wvsUseAuthList = (List<WvSurvUseAuthDVo>) commonDao.queryList(wvsUseAuthVo);
			
			for(WvSurvUseAuthDVo wvsUseAuth : wvsUseAuthList){
				WvSurvUseAuthDVo wvsUseAuthDel = new WvSurvUseAuthDVo();
				wvsUseAuthDel.setSurvId(wvsUseAuth.getSurvId());
				wvsUseAuthDel.setAuthTgtTypCd(wvsUseAuth.getAuthTgtTypCd());
				wvsUseAuthDel.setAuthTgtUid(wvsUseAuth.getAuthTgtUid());
				wvsUseAuthDel.setCompId(wvsUseAuth.getCompId());
				
				//설문사용권한상세
				queryQueue.delete(wvsUseAuthDel);
			}
			
			
			
			WvSurvBVo wvsBVo = new WvSurvBVo();
			wvsBVo.setSurvId(wvsThisVo.getSurvId());
			wvsBVo.setCompId(userVo.getCompId());
			//설문기본 삭제
			queryQueue.delete(wvsBVo);
			
			//설문 첨부파일 삭제
			//List<CommonFileVo> deletedFileList = wvFileSvc.deleteWcFile(wvsThisVo.getSurvId(), queryQueue);
			wvFileSvc.deleteWcFile(wvsThisVo.getSurvId(), queryQueue);
				
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
	@RequestMapping(value= {"/wv/adm/transSetTmpSaveSurvQues","/wv/transSetTmpSaveSurvQues"})
	public String transSetTmpSaveSurvQues(HttpServletRequest request, ModelMap model) throws Exception {
		
		QueryQueue queryQueue = new QueryQueue();
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String survId = request.getParameter("survId");
		
		
		WvSurvBVo wvsThisVo = new WvSurvBVo();
		wvsThisVo.setSurvId(survId);
		wvsThisVo.setCompId(userVo.getCompId());
		
		wvsThisVo.setSurvPrgStatCd("6");
		
		queryQueue.update(wvsThisVo);
		
		commonDao.execute(queryQueue);
		model.put("message", messageProperties.getMessage("bb.msg.save.tmp.success", request));

		/**
		 * 관리자권한자 상관없이 사용자페이지에서 처리시 사용자페이지로 이동해야함.
		 */
		if(request.getRequestURI().indexOf("adm") > 0){   //&& userVo.isAdmin() && userVo.isSysAdmin()
			model.put("todo", "parent.location.replace('/wv/adm/listSurvApvd.do?menuId="+request.getParameter("menuId")
					+"');");
			
		}else{
			model.put("todo", "parent.location.replace('/wv/listMySurvApvd.do?menuId="+request.getParameter("menuId")
					+"');");
		}

		
		checkPath(request, "index", model);
		
		//공통 처리 페이지
		return LayoutUtil.getResultJsp();
	}
	
	/** 첨부파일 다운로드 (사용자) */
	@RequestMapping(value = {"/wv/downFile","/wv/preview/downFile"}, method = RequestMethod.POST)
	public ModelAndView downFile(HttpServletRequest request,
			@RequestParam(value = "fileIds", required = true) String fileIds,
			@RequestParam(value = "actionParam", required = false) String actionParam
			) throws Exception {
		
		try {
			if (fileIds.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			// 다운로드 체크
			emAttachViewSvc.chkAttachDown(request, userVo.getCompId());
						
			// 파일 목록조회
			ModelAndView mv = wvCmSvc.getFileList(request , fileIds , actionParam);
			
			return mv;
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
			return mv;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
		}
		return null;
	}
	
	/** 설문질문상세 저장 */
	@RequestMapping(value= {"/wv/adm/setMulcQuesPopSave","/wv/setMulcQuesPopSave"})
	public String setMulcQuesPopSave(HttpServletRequest request, ModelMap model) throws Exception {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		QueryQueue queryQueue = new QueryQueue();
		String fnc = request.getParameter("fnc");
		String survId = request.getParameter("survId");
		int wvsQuesOrdr =0;
		if(fnc.equalsIgnoreCase("mod") && survId != null){
			String quesId = request.getParameter("quesId");
			if(quesId != null){
				WvSurvQuesDVo wvsQues = new WvSurvQuesDVo();
				wvsQues.setSurvId(survId);
				wvsQues.setQuesId(quesId);
				wvsQues.setCompId(userVo.getCompId());
				
				queryQueue.delete(wvsQues);
				
				wvsQues = (WvSurvQuesDVo) commonDao.queryVo(wvsQues);
				wvsQuesOrdr = wvsQues.getQuesSortOrdr();
				//질문이미지삭제
//				WvSurvFileDVo wvsFileVo = new WvSurvFileDVo();
//				if(wvsQues.getImgSurvFileId() != null){
//					if(!wvsQues.getImgSurvFileId().equalsIgnoreCase("")){
//						wvsFileVo.setFileId(Integer.parseInt(wvsQues.getImgSurvFileId()));
//						queryQueue.delete(wvsFileVo);
//					}
//				}
				
				WvQuesExamDVo wvQExam = new WvQuesExamDVo();
				wvQExam.setSurvId(survId);
				wvQExam.setQuesId(quesId);
				wvQExam.setCompId(userVo.getCompId());
				
				queryQueue.delete(wvQExam);
				
//				List<WvQuesExamDVo> wvQueExamList = (List<WvQuesExamDVo>) commonDao.queryList(wvQExam);
				
				//보기이미지삭제
//				for(WvQuesExamDVo wvqExam :wvQueExamList){
//					WvSurvFileDVo wvsFileExam = new WvSurvFileDVo();
//					if(wvqExam.getImgSurvFileId() != null){
//						if(!wvqExam.getImgSurvFileId().equalsIgnoreCase("")){
//							wvsFileExam.setFileId(Integer.parseInt(wvqExam.getImgSurvFileId()));
//							queryQueue.delete(wvsFileExam);
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
		
		WvSurvQuesDVo wvsQues =  new WvSurvQuesDVo();
		wvsQues.setSurvId(survId);
		wvsQues.setQuesId(wvCmSvc.createId("WV_SURV_QUES_D"));
		wvsQues.setCompId(userVo.getCompId());
		//등록일때 순서
		
		//질문정렬순서
		//============================================
		WvSurvQuesDVo wvsCount =  new WvSurvQuesDVo();
		wvsCount.setInstanceQueryId("countMaxWvSurvQuesD");
		wvsCount.setSurvId(survId);
		wvsCount.setCompId(userVo.getCompId());
		int quesOrdrCount = commonDao.queryInt(wvsCount)!=null?commonDao.queryInt(wvsCount):0;
		wvsQues.setCompId(userVo.getCompId());	
		if(fnc.equalsIgnoreCase("reg")){
			wvsQues.setQuesSortOrdr(quesOrdrCount +1);
			
			//등록일때만 설문기본 질문개수가 +1 됨.
			//============================================
			//설문기본 질문 개수
			WvSurvQuesDVo wvsQuesCount = new WvSurvQuesDVo();
			wvsQuesCount.setSurvId(wvsQues.getSurvId());
			wvsQuesCount.setCompId(wvsQues.getCompId());
			int quesCount = commonDao.count(wvsQuesCount)!=null?commonDao.count(wvsQuesCount):0;
			WvSurvBVo wvsVo = new WvSurvBVo();
			wvsVo.setSurvId(wvsQuesCount.getSurvId());
			wvsVo.setQuesCnt(quesCount+1);
			queryQueue.update(wvsVo);
			
		}else if(fnc.equalsIgnoreCase("mod")){
			wvsQues.setQuesSortOrdr(wvsQuesOrdr);
		}
		
		
		
		
		//============================================
		
		wvsQues.setQuesCont(subj);
		wvsQues.setQuesImgAttYn(ques_img_file_id.equals("")&&ques_img_file_id!=null?"Y":"N");
		wvsQues.setImgSurvFileId(ques_img_file_id);
		//보기선택항목코드
		wvsQues.setExamChoiItemCd(examTypeSelect);
		wvsQues.setMulChoiYn(mulChoiYn);
		wvsQues.setMandaReplyYn(mandaReplyYn);
		
		queryQueue.insert(wvsQues);
		
		//질문보기상세 저장
		for(int i=0; i < exam.length; i++){
			WvQuesExamDVo wvQExamVo = new WvQuesExamDVo();
			wvQExamVo.setExamNo(i+1);
			wvQExamVo.setQuesId(wvsQues.getQuesId());
			wvQExamVo.setCompId(userVo.getCompId());
			wvQExamVo.setSurvId(wvsQues.getSurvId());
			wvQExamVo.setExamOrdr(i+1);
			wvQExamVo.setExamDispNm(exam[i]);
			wvQExamVo.setInputYn(ansAdd==null?"N":ansAdd);
			wvQExamVo.setExamImgUseYn(imgAdd);
			if(exam_img_file_id != null){
				wvQExamVo.setImgSurvFileId(exam_img_file_id[i]);
			}
			
			queryQueue.insert(wvQExamVo);
		}
		
		
		commonDao.execute(queryQueue);
		
		
		/**
		 * 관리자권한자 상관없이 사용자페이지에서 처리시 사용자페이지로 이동해야함.
		 */
		if(request.getRequestURI().indexOf("adm") > 0){   //&& userVo.isAdmin() && userVo.isSysAdmin()
			model.put("todo", "parent.location.replace('/wv/adm/setSurvQues.do?menuId="+request.getParameter("menuId")
					+ "&fnc=" + request.getParameter("fnc")
					+ "&survId=" + survId
					+"');");
		}else{
			model.put("todo", "parent.location.replace('/wv/setSurvQues.do?menuId="+request.getParameter("menuId")
					+ "&fnc=" + request.getParameter("fnc")
					+ "&survId=" + survId
					+"');");
		}
		
		
		//공통 처리 페이지
		return LayoutUtil.getResultJsp();
	}
	
	/** 질문삭제 [FORM SUBMIT] 
	 * @throws Exception */
	@RequestMapping(value = {"/wv/adm/setSurvQuesDel","/wv/setSurvQuesDel"} , method = RequestMethod.POST)
	public String setSurvQuesDel(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception{
		
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);

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
			
			WvSurvQuesDVo wvQueVo = new WvSurvQuesDVo();
			wvQueVo.setQuesId(quesId);
			wvQueVo.setSurvId(survId);
			wvQueVo.setCompId(userVo.getCompId());
			queryQueue.delete(wvQueVo);
			
			wvQueVo = (WvSurvQuesDVo) commonDao.queryVo(wvQueVo);
					
			
			WvSurvFileDVo wvsFileVo = new WvSurvFileDVo();
			if(wvQueVo.getImgSurvFileId() != null){
				if(!wvQueVo.getImgSurvFileId().equalsIgnoreCase("")){
					wvsFileVo.setFileId(Integer.parseInt(wvQueVo.getImgSurvFileId()));
					queryQueue.delete(wvsFileVo);
				}
			}
			
			WvQuesExamDVo wvQueExamVo = new WvQuesExamDVo();
			wvQueExamVo.setQuesId(quesId);
			wvQueExamVo.setSurvId(survId);
			wvQueExamVo.setCompId(userVo.getCompId());
			
			queryQueue.delete(wvQueExamVo);
			
			@SuppressWarnings("unchecked")
			List<WvQuesExamDVo> wvQueExamList = (List<WvQuesExamDVo>) commonDao.queryList(wvQueExamVo);
			
			for(WvQuesExamDVo wvqExam :wvQueExamList){
				WvSurvFileDVo wvsFileExam = new WvSurvFileDVo();
				if(wvqExam.getImgSurvFileId() != null){
					if(!wvqExam.getImgSurvFileId().equalsIgnoreCase("")){
						wvsFileExam.setFileId(Integer.parseInt(wvqExam.getImgSurvFileId()));
						queryQueue.delete(wvsFileExam);
					}
				}
			}
			//============================================
			//설문기본 질문 개수
			WvSurvQuesDVo wvsQuesCount = new WvSurvQuesDVo();
			wvsQuesCount.setSurvId(wvQueExamVo.getSurvId());
			wvsQuesCount.setCompId(wvQueExamVo.getCompId());
			int quesCount = commonDao.count(wvsQuesCount)!=null?commonDao.count(wvsQuesCount):0; 
			WvSurvBVo wvsVo = new WvSurvBVo();
			wvsVo.setSurvId(wvsQuesCount.getSurvId());
			if(quesCount > 1){
				wvsVo.setQuesCnt(quesCount-1);
			}else{
				wvsVo.setQuesCnt(quesCount);
			}
			
			
			queryQueue.update(wvsVo);
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
	@RequestMapping(value= {"/wv/adm/transSetSurvOendQuesSave","/wv/transSetSurvOendQuesSave"})
	public String transSetSurvOendQuesSave(HttpServletRequest request, ModelMap model) throws Exception {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		QueryQueue queryQueue = new QueryQueue();
		String fnc = request.getParameter("fnc");
		String survId = request.getParameter("survId");
		String subj = request.getParameter("subj");
		String ques_img_file_id = request.getParameter("ques_img_file_id");
		String mandaReplyYn = request.getParameter("mandaReplyYn");
		if(!fnc.equalsIgnoreCase("mod")){
			
			WvSurvQuesDVo wvsQuesVo = new WvSurvQuesDVo();
			wvsQuesVo.setCompId(userVo.getCompId());
			wvsQuesVo.setQuesImgAttYn(ques_img_file_id.equals("")&&ques_img_file_id!=null?"Y":"N");
			wvsQuesVo.setQuesId(wvCmSvc.createId("WV_SURV_QUES_D"));
			wvsQuesVo.setSurvId(survId);
			wvsQuesVo.setQuesCont(subj);
			wvsQuesVo.setMulChoiYn(null);
			wvsQuesVo.setMandaReplyYn(mandaReplyYn);
			wvsQuesVo.setImgSurvFileId(ques_img_file_id);

			//질문정렬순서
			//============================================
			WvSurvQuesDVo wvsCount =  new WvSurvQuesDVo();
			wvsCount.setInstanceQueryId("countMaxWvSurvQuesD");
			wvsCount.setSurvId(survId);
			wvsCount.setCompId(userVo.getCompId());
			int quesOrdrCount = commonDao.queryInt(wvsCount)!=null?commonDao.queryInt(wvsCount):0;
			wvsQuesVo.setCompId(userVo.getCompId());		
			wvsQuesVo.setQuesSortOrdr(quesOrdrCount +1);
			//============================================
			//설문기본 질문 개수
			WvSurvQuesDVo wvsQuesCount = new WvSurvQuesDVo();
			wvsQuesCount.setSurvId(wvsQuesVo.getSurvId());
			wvsQuesCount.setCompId(wvsQuesVo.getCompId());
			int quesCount = commonDao.count(wvsQuesCount)!=null?commonDao.count(wvsQuesCount):0;
			WvSurvBVo wvsVo = new WvSurvBVo();
			wvsVo.setSurvId(wvsQuesCount.getSurvId());
			wvsVo.setQuesCnt(quesCount+1);
			
			queryQueue.update(wvsVo);
			//============================================
			queryQueue.insert(wvsQuesVo);
		}else{
			String quesId = request.getParameter("quesId");
			WvSurvQuesDVo wvsQuesVo = new WvSurvQuesDVo();
			wvsQuesVo.setSurvId(survId);
			wvsQuesVo.setQuesId(quesId);
			wvsQuesVo.setCompId(userVo.getCompId());
			wvsQuesVo.setQuesCont(subj);
			wvsQuesVo.setImgSurvFileId(ques_img_file_id);
			wvsQuesVo.setQuesImgAttYn(ques_img_file_id.equals("")&&ques_img_file_id!=null?"Y":"N");
			wvsQuesVo.setMulChoiYn(null);
			wvsQuesVo.setMandaReplyYn(mandaReplyYn);
			
			queryQueue.update(wvsQuesVo);
		}
		
		commonDao.execute(queryQueue);
		model.put("survId",survId);
		//model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		
		/**
		 * 관리자권한자 상관없이 사용자페이지에서 처리시 사용자페이지로 이동해야함.
		 */
		if(request.getRequestURI().indexOf("/adm/") > 0){   //&& userVo.isAdmin() && userVo.isSysAdmin()
			model.put("todo", "parent.location.replace('/wv/adm/setSurvQues.do?menuId="+request.getParameter("menuId")
					+ "&fnc=" + request.getParameter("fnc")
					+ "&survId=" + survId
					+"');");
		}else{
			model.put("todo", "parent.location.replace('/wv/setSurvQues.do?menuId="+request.getParameter("menuId")
					+ "&fnc=" + request.getParameter("fnc")
					+ "&survId=" + survId
					+"');");
		}
		
		//공통 처리 페이지
		return LayoutUtil.getResultJsp();
		
	}

}
