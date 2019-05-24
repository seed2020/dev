package com.innobiz.orange.web.pt.ctrl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.crypto.CryptoSvc;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.AuthCdDecider;
import com.innobiz.orange.web.pt.secu.IpChecker;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtLoutSvc;
import com.innobiz.orange.web.pt.svc.PtPsnSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtMnuLoutDVo;
import com.innobiz.orange.web.pt.vo.PtUserSetupDVo;

/** 개인정보, 개인화 */
@Controller
public class PtPsnCtrl {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PtPsnCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 메세지 프로퍼티 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** context.properties */
	@Resource(name = "contextProperties")
	private Properties contextProperties;
	
	/** 암호화 서비스 */
	@Autowired
	private CryptoSvc cryptoSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 사용자 개인 설정 서비스 */
	@Autowired
	private PtPsnSvc ptPsnSvc;

	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 메뉴 레이아웃 서비스 */
	@Autowired
	private PtLoutSvc ptLoutSvc;
	
	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;
	
//	/** 조직도 사용자 Push 방식 동기화 서비스 */
//	@Autowired
//	private PushSyncSvc pushSyncSvc;
	
	/** IP 체크용 객체 - IP 조회 및 정책 적용 */
	@Autowired
	private IpChecker ipChecker;
	
	/** 스킨 설정 */
	@RequestMapping(value = "/pt/psn/skin/setSkin")
	public String setSkin(HttpServletRequest request,
			ModelMap model) throws Exception {

		// 시스템설정 - 레이아웃
		Map<String, String> layout = ptSysSvc.getLayoutSetup();
		model.put("layout", layout);
		
		UserVo userVo = LoginSession.getUser(request);
		model.put("skinSetup", userVo.getLoutCatId()+"_"+userVo.getSkin());
		return LayoutUtil.getJspPath("/pt/psn/skin/setSkin");
	}

	/** 개인설정 저장 */
	@RequestMapping(value = "/pt/psn/skin/transSkinSetup")
	public String transSkinSetup(HttpServletRequest request,
			@RequestParam(value = "skinSetup", required = true) String skinSetup,
			HttpSession session, ModelMap model) throws Exception {

		try{
			
			String loutCatId = null; // 레이아웃유형ID
			String skin = null; // 스킨경로
			
			UserVo userVo = LoginSession.getUser(request);
			
			List<String[]> list = new ArrayList<String[]>();
			int p = skinSetup==null ? 0 : skinSetup.indexOf('_');
			if(p>0){
				loutCatId = skinSetup.substring(0, p);
				skin = skinSetup.substring(p+1);
				list.add(new String[]{ "loutCatId", loutCatId });
				list.add(new String[]{ "skin", skin });
			}
			
			if(list.size()>0){
				
				QueryQueue queryQueue = new QueryQueue();
				ptPsnSvc.storeUserSetupToQueue(list, userVo.getUserUid(), PtConstant.PT_LOUT_USER, "N", queryQueue);
				
				// 일괄실행
				commonSvc.execute(queryQueue);
				
				// 세션에 스킨 및 레이아웃 저장
				userVo.setLoutCatId(loutCatId);
				userVo.setSkin(skin);
				
				// menuId 변경된 경로
				String url = ptSecuSvc.toAuthMenuUrl(userVo, "/pt/psn/skin/setSkin.do", null);
				
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
				model.put("todo", "parent.location.replace('"+url+"')");
			} else {
				// cm.msg.nodata.toSave=저장할 데이터가 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.nodata.toSave", request));
			}
			
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** 비밀번호 설정 */
	@RequestMapping(value = "/pt/psn/pw/setPw")
	public String setPw(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// RSA 암호화 스크립트 추가
		model.put("JS_OPTS", new String[]{"pt.rsa"});
		
		UserVo userVo = LoginSession.getUser(request);
		
		// 패스워드 정책 조회
		Map<String, String> pwPolc = ptSysSvc.getPwPolc(userVo.getCompId());
		model.put("pwPolc", pwPolc);
		
		// 원직자의 개인설정 조회
		Map<String, String> odurLoginMap = ptPsnSvc.getUserSetupMap(request, userVo.getOdurUid(), PtConstant.PT_LOGIN, true);
		String lastChgDt = odurLoginMap.get("sysPwChgDt");//비밀번호변경일
		if(lastChgDt != null) model.put("lastChgDt", lastChgDt);
		
		if("Y".equals(pwPolc.get("polcUseYn"))){
			String nextChgDt = ptSysSvc.getNextChgPwDt(lastChgDt, userVo.getCompId(), 1);
			if(nextChgDt!=null) model.put("nextChgDt", nextChgDt);
		}
		
		return LayoutUtil.getJspPath("/pt/psn/pw/setPw");
	}
	
	/** 비밀번호 설정 저장 */
	@RequestMapping(value = "/pt/psn/pw/transPw")
	public String transPw(HttpServletRequest request,
			@RequestParam(value = "secu", required = true) String secu,
			HttpSession session, ModelMap model, Locale locale) throws Exception {
		try {
			
			JSONObject jsonObject = null;
			try {
				jsonObject = cryptoSvc.processRsa(request);
			} catch(CmException e){
				LOGGER.error("Change password fail(by user) : "+e.getMessage());
				//pt.login.fail.decrypt=복호화에 실패하였습니다.
				throw new CmException(messageProperties.getMessage("pt.login.fail.decrypt", locale));
			}
			
			UserVo userVo = LoginSession.getUser(request);
			
			String orgPw = (String)jsonObject.get("orgPw");
			String newPw = (String)jsonObject.get("newPw1");
			String odurUid = userVo.getOdurUid();
			
			// 데모 로그인 여부
			Boolean demoLgin = userVo.isDemoLgin();
			if(demoLgin!=null && Boolean.TRUE.equals(demoLgin)){
				//pt.demo.notChangePw=데모용 계정은 비밀번호 변경 할 수 없습니다.
				model.put("message", messageProperties.getMessage("pt.demo.notChangePw", locale));
				return LayoutUtil.getResultJsp();
			}
			
			ptPsnSvc.changePw(odurUid, userVo.getUserUid(), orgPw, newPw, userVo.getCompId(), "PSN PW", locale);
			
//			// 패스워드 정책 조회
//			Map<String, String> pwPolc = ptSysSvc.getPwPolc(userVo.getCompId());
//			ptPsnSvc.checkPwPolc(pwPolc, odurUid, newPw, locale);
//			
//			// 기존 로그인 비밀번호가 같은지 확인 후 새 비밀번호 암호화하여 리턴
//			String encryptedNewPw = ptPsnSvc.getEncryptedSysPw(odurUid, orgPw, newPw, locale);
//			
//			QueryQueue queryQueue = new QueryQueue();
//			
//			String sysdate = commonSvc.querySysdate(new CommonVoImpl("YYYY-MM-DD HH24:MI:SS"));
//			
//			// 사용자비밀번호상세(OR_USER_PW_D) 테이블 - UPDATE
//			OrUserPwDVo orUserPwDVo = new OrUserPwDVo();
//			orUserPwDVo.setOdurUid(odurUid);
//			orUserPwDVo.setPwTypCd("SYS");//SYS:시스템 비밀번호, APV:결재 비밀번호
//			orUserPwDVo.setModDt(sysdate);
//			orUserPwDVo.setModrUid(userVo.getUserUid());
//			orUserPwDVo.setPwEnc(encryptedNewPw);
//			queryQueue.store(orUserPwDVo);
//			
//			// 사용자비밀번호이력(OR_USER_PW_H) 테이블
//			// - 비밀번호 변경 내역의 저장
//			// - 예전 비밀번호로 비밀번호 찾기 기능을 위한 것 - 구현여부 미정
//			
//			// 사용자비밀번호이력(OR_USER_PW_H) 테이블 - INSERT
//			OrUserPwHVo orUserPwHVo = new OrUserPwHVo();
//			orUserPwHVo.setOdurUid(odurUid);
//			orUserPwHVo.setPwTypCd("SYS");//SYS:시스템 비밀번호, APV:결재 비밀번호
//			orUserPwHVo.setModDt(sysdate);
//			orUserPwHVo.setModrUid(userVo.getUserUid());
//			orUserPwHVo.setPwEnc(encryptedNewPw);
//			queryQueue.store(orUserPwHVo);
//			
//			String currYmd = sysdate.substring(0,10);
//			
//			// 사용자설정상세(PT_USER_SETUP_D) 테이블에 저장
//			ptPsnSvc.addUserSetup(queryQueue, odurUid, PtConstant.PT_LOGIN, "sysPwChgDt", currYmd, currYmd, "N");
//			
//			// 일괄실행
//			commonSvc.execute(queryQueue);
//			
//			// 사용자 조직도 동기화 PUSH - 사용자
//			if(pushSyncSvc.hasSync()){
//				pushSyncSvc.syncUsers(odurUid, null, null, null);
//			}
			
			request.getSession().removeAttribute("FORCE_MOVE");
			
			// 결과 메세지
			// cm.msg.modify.success=변경 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.modify.success", locale));
			
		} catch(CmException e){
			model.put("message", e.getMessage());
		} catch(Exception e){
			model.put("message", e.getMessage());
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** 페이지별 레코드 수 설정 */
	@RequestMapping(value = "/pt/psn/cnt/setPageRecCnt")
	public String setPageRecCnt(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 페이지별레코드설정코드 목록 조회
		List<PtCdBVo> pageRecSetupCdList = ptCmSvc.getCdList("PAGE_REC_SETUP_CD", langTypCd, "Y");
		model.put("pageRecSetupCdList", pageRecSetupCdList);
		
		// 페이지별 레코드 수 조회
		Map<String, Integer> pageRecCntMap = ptPsnSvc.getPageRecCntMap(request);
		if(pageRecCntMap!=null){
			model.put("pageRecCntMap", pageRecCntMap);
		}
		
		// 페이지별레코드수코드 목록 - PersonalUtil 로 이관
		// 개인설정 내용은 세션에 있음 - attribute id : pageRecCntMap
		
		return LayoutUtil.getJspPath("/pt/psn/cnt/setPageRecCnt");
	}

	/** 개인설정 저장 */
	@RequestMapping(value = "/pt/psn/cnt/transPsnSetup")
	public String transPsnSetup(HttpServletRequest request,
			@RequestParam(value = "setupClsId", required = true) String setupClsId,
			@RequestParam(value = "cacheYn", required = true) String cacheYn,
			HttpSession session, ModelMap model) throws Exception {

		try{

			QueryQueue queryQueue = new QueryQueue();
			ptPsnSvc.storeUserSetupToQueue(request, setupClsId, cacheYn, queryQueue);
			
			if(!queryQueue.isEmpty()){
				
				// 일괄실행
				commonSvc.execute(queryQueue);
				
				// 개인화설정 세션에 다시 로듯함
				if("Y".equals(cacheYn)){
					UserVo userVo = LoginSession.getUser(request);
					Map<String, Map<String, ?>> userSetupMap = ptPsnSvc.getUserSetup(userVo.getUserUid(), userVo.getOdurUid(), null, request);
					request.getSession().setAttribute("userSetupMap", userSetupMap);
				}
				
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			} else {
				// cm.msg.nodata.toSave=저장할 데이터가 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.nodata.toSave", request));
			}
			
		} catch(SQLException e){
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** 초기 페이지 설정 */
	@RequestMapping(value = "/pt/psn/init/setInitPage")
	public String setInitPage(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		// 시스템설정 - 레이아웃
		Map<String, String> layout = ptSysSvc.getLayoutSetup();
		model.put("layout", layout);
		
		// 레이아웃위치코드 별 레이아웃트리 맵 조회
		Map<String, List<PtMnuLoutDVo>> loutTreeByLoutLocCdMap = ptLoutSvc.getLoutTreeByLoutLocCdMap(userVo.getCompId(), userVo.getLangTypCd());
		// 아이콘 레이아웃
		List<PtMnuLoutDVo> iconList  = loutTreeByLoutLocCdMap.get("icon");
		// 기본 레이아웃
		List<PtMnuLoutDVo> mainList = loutTreeByLoutLocCdMap.get("main");
		
		Map<String, String> loginMap = ptPsnSvc.getUserSetupMap(request, userVo.getUserUid(), PtConstant.PT_LOGIN, false);
		model.put("loginMap", loginMap);
		
		// 외부망 권한 적용
		boolean isExAuth = ipChecker.isExAuth(userVo);
		
		// 관리자 여부
		boolean isAdmin = ArrayUtil.isInArray(userVo.getAdminAuthGrpIds(), PtConstant.AUTH_ADMIN)
				|| ArrayUtil.isInArray(userVo.getAdminAuthGrpIds(), PtConstant.AUTH_SYS_ADMIN);
		if(!isExAuth && isAdmin){
			model.put("iconList", iconList);
			model.put("mainList", mainList);
		} else {
			// 사용자가 속한 [사용자권한그룹ID] 목록
			String[] userAuthGrpIds = userVo.getUserAuthGrpIds();
			// 사용자가 속한 [관리자권한그룹ID] 목록
			String[] adminAuthGrpIds = userVo.getAdminAuthGrpIds();
			// 메뉴에 어떤 권한이 있는지 결정하는 객체
			AuthCdDecider authCdDecider = ptSecuSvc.getAuthCdDecider(userVo.getCompId(),
					userVo.isInternalIp() ? PtConstant.AUTH_IP_IN : PtConstant.AUTH_IP_EX);
			List<PtMnuLoutDVo> returnList;
			
			returnList = new ArrayList<PtMnuLoutDVo>();
			ptSecuSvc.setAuthedLoutList(iconList, returnList, userAuthGrpIds, adminAuthGrpIds, authCdDecider);
			model.put("iconList", returnList);
			
			returnList = new ArrayList<PtMnuLoutDVo>();
			ptSecuSvc.setAuthedLoutList(mainList, returnList, userAuthGrpIds, adminAuthGrpIds, authCdDecider);
			model.put("mainList", returnList);
		}
		
		return LayoutUtil.getJspPath("/pt/psn/init/setInitPage");
	}
	
	/** 초기 페이지 설정 - 저장 */
	@RequestMapping(value = "/pt/psn/init/transInitPage")
	public String transInitPage(HttpServletRequest request,
			@RequestParam(value = "iconInitPage", required = true) String iconInitPage,
			@RequestParam(value = "bascInitPage", required = true) String bascInitPage,
			HttpSession session, ModelMap model) throws Exception {

		try{

			UserVo userVo = LoginSession.getUser(request);
			QueryQueue queryQueue = new QueryQueue();
			
//			String currYmd = commonSvc.querySysdate(new CommonVoImpl("YYYY-MM-DD"));
			
			// 사용자설정상세(PT_USER_SETUP_D) 테이블에 저장
			ptPsnSvc.addUserSetup(queryQueue, userVo.getUserUid(), PtConstant.PT_LOGIN, "iconInitPage", iconInitPage, "N");
			ptPsnSvc.addUserSetup(queryQueue, userVo.getUserUid(), PtConstant.PT_LOGIN, "bascInitPage", bascInitPage, "N");
			
			// 일괄실행
			commonSvc.execute(queryQueue);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			
//		} catch(CmException e){
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** 초기 사용자 설정 - 로그인 디폴트 사용자 */
	@RequestMapping(value = "/pt/psn/init/setInitUserPop")
	public String setInitUserPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String defUserUid = null;
		
		// 원직자의 개인설정 조회 - 디폴트 로그인 계정
		Map<String, String> odurLoginMap = ptPsnSvc.getUserSetupMap(request, userVo.getOdurUid(), PtConstant.PT_LOGIN, false);
		if(odurLoginMap!=null) defUserUid = odurLoginMap.get("defUserUid");
		
		// 설정이 없으면 - 원직자로 설정
		if(defUserUid==null || defUserUid.isEmpty()){
			defUserUid = userVo.getOdurUid();
		}
		
		model.put("defUserUid", defUserUid);
		
		return LayoutUtil.getJspPath("/pt/psn/init/setInitUserPop");
	}
	
	/** [AJX] 초기 사용자 설정 저장 - 로그인 디폴트 사용자 */
	@RequestMapping(value = "/pt/psn/init/transInitUserAjx")
	public String transInitUserAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception{
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		
		String defUserUid = (String)jsonObject.get("defUserUid");
		
		UserVo userVo = LoginSession.getUser(request);
		QueryQueue queryQueue = new QueryQueue();
		
		if(defUserUid!=null && !defUserUid.isEmpty()){
			
			PtUserSetupDVo ptUserSetupDVo = new PtUserSetupDVo();
			ptUserSetupDVo.setUserUid(userVo.getOdurUid());
			ptUserSetupDVo.setSetupClsId(PtConstant.PT_LOGIN);
			ptUserSetupDVo.setSetupId("defUserUid");
			ptUserSetupDVo.setSetupVa(defUserUid);
			
			queryQueue.store(ptUserSetupDVo);
			
			commonSvc.execute(queryQueue);
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("result", "ok");
			
		}
		try {
			
			if(!queryQueue.isEmpty()){
				
				commonSvc.execute(queryQueue);
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
				model.put("result", "ok");
			}
			
		} catch(Exception e){
			String message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
			model.put("message", message);
		}
		
		return LayoutUtil.returnJson(model);
	}
}
