package com.innobiz.orange.mobile.pt.ctrl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;
import com.innobiz.orange.mobile.or.ctrl.MoOrOrgCtrl;
import com.innobiz.orange.web.ap.svc.ApCmSvc;
import com.innobiz.orange.web.ap.utils.ApConstant;
import com.innobiz.orange.web.cm.crypto.CryptoSvc;
import com.innobiz.orange.web.cm.crypto.License;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserPinfoDVo;
import com.innobiz.orange.web.or.vo.OrUserPwDVo;
import com.innobiz.orange.web.pt.secu.AuthCdDecider;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtLoutSvc;
import com.innobiz.orange.web.pt.svc.PtPsnSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtMnuLoutDVo;

/** 개인 설정 컨트롤러 */
@Controller
public class MoPsnCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(MoOrOrgCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 메세지 처리용 프라퍼티 - 다국어 */
	@Autowired
	private MessageProperties messageProperties;

	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;

//	/** 조직 공통 서비스 */
//	@Autowired
//	private OrCmSvc orCmSvc;
	
	/** 결재 공통 서비스 */
	@Autowired
	private ApCmSvc apCmSvc;
	
	/** 암호화 서비스 */
	@Autowired
	private CryptoSvc cryptoSvc;
	
	/** 메뉴 레이아웃 서비스 */
	@Autowired
	private PtLoutSvc ptLoutSvc;
	
	/** 사용자 개인 설정 서비스 */
	@Autowired
	private PtPsnSvc ptPsnSvc;
	
	/** 포털 보안 서비스(레이아웃 포함) */
	@Autowired
	private PtSecuSvc ptSecuSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
//	/** 조직도 사용자 Push 방식 동기화 서비스 */
//	@Autowired
//	private PushSyncSvc pushSyncSvc;
//	
//	/** 일정 서비스 */
//	@Autowired
//	private WcScdManagerSvc wcScdManagerSvc;
	
	/** 환경 설정 */
	@RequestMapping(value = "/pt/psn/setEnv")
	public String setEnv(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		////////////////////////////////////////////////
		// 개인 정보
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		// 개인정보 수정 안함
		if("Y".equals(sysPlocMap.get("blockPnsInfoEnable"))){
			model.put("blockPnsInfoEnable", Boolean.TRUE);
		}
		
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setUserUid(userVo.getUserUid());
		orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
		
		// 사용자개인정보상세(OR_USER_PINFO_D) 테이블 - 조회
		OrUserPinfoDVo orUserPinfoDVo = new OrUserPinfoDVo();
		orUserPinfoDVo.setOdurUid(userVo.getOdurUid());
		orUserPinfoDVo = (OrUserPinfoDVo)commonSvc.queryVo(orUserPinfoDVo);
		
		if(orUserPinfoDVo != null){
			
			String[] phone;
			String decripted;
			
			// 휴대전화번호
			decripted =  cryptoSvc.decryptPersanal(orUserPinfoDVo.getMbnoEnc());
			phone = StringUtil.splitPhone(decripted);
			model.put("mbno1", phone[0]);
			model.put("mbno2", phone[1]);
			model.put("mbno3", phone[2]);
			
			// 회사전화번호
			decripted =  cryptoSvc.decryptPersanal(orUserPinfoDVo.getCompPhonEnc());
			phone = StringUtil.splitPhone(decripted);
			model.put("compPhon1", phone[0]);
			model.put("compPhon2", phone[1]);
			model.put("compPhon3", phone[2]);
			
			// 외부이메일
			decripted =  cryptoSvc.decryptPersanal(orUserPinfoDVo.getExtnEmailEnc());
			model.put("extnEmail", decripted);
		}
		
		// 담당업무내용
		if(orUserBVo!=null){
			model.put("tichCont", orUserBVo.getTichCont());
		}
		
		
		////////////////////////////////////////////////
		// 결재 서명 방법
		
		// 서명방법코드 - 01:도장 이미지, 02:서명 이미지, 03:사용자명(문자)
		if(orUserBVo!=null){
			model.put("signMthdCd", orUserBVo.getSignMthdCd());
		}
		
		// 디폴트 서명 방법 코드 
		model.put("deftSignMthdCd", ApConstant.DFT_SIGN_MTHD_CD);
		
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//서명방법코드
		List<PtCdBVo> signMthdCdList = ptCmSvc.getCdList("SIGN_MTHD_CD", langTypCd, "Y");
		model.put("signMthdCdList", signMthdCdList);
		
		// 결재 옵션 세팅
		apCmSvc.getOptConfigMap(model, userVo.getCompId());
		
		////////////////////////////////////////////////
		// 초기 페이지
		
		// 레이아웃위치코드 별 레이아웃트리 맵 조회
		Map<String, List<PtMnuLoutDVo>> loutTreeByLoutLocCdMap = ptLoutSvc.getLoutTreeByLoutLocCdMap(userVo.getCompId(), userVo.getLangTypCd());
		// 아이콘 레이아웃
		List<PtMnuLoutDVo> mobileList  = loutTreeByLoutLocCdMap.get("mobile");
		

		Map<String, String> loginMap = ptPsnSvc.getUserSetupMap(request, userVo.getUserUid(), PtConstant.MB_LOGIN, false);
		model.put("loginMap", loginMap);
		
		// 관리자 여부
		boolean isAdmin = ArrayUtil.isInArray(userVo.getAdminAuthGrpIds(), PtConstant.AUTH_ADMIN)
				|| ArrayUtil.isInArray(userVo.getAdminAuthGrpIds(), PtConstant.AUTH_SYS_ADMIN);
		if(isAdmin){
			model.put("mobileList", mobileList);
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
			ptSecuSvc.setAuthedLoutList(mobileList, returnList, userAuthGrpIds, adminAuthGrpIds, authCdDecider);
			model.put("mobileList", returnList);
		}
		
		
		////////////////////////////////////////////////
		// 로그인 사용자 선택
		String defUserUid = null;
		
		// 초기 사용자 선택을  웹과 모바일을 같이 사용하기 위해
		// PtConstant.MB_LOGIN(모바일용) 대신 PtConstant.PT_LOGIN(웹용)을 사용함
		
		// 원직자의 개인설정 조회 - 디폴트 로그인 계정
		Map<String, String> odurLoginMap = ptPsnSvc.getUserSetupMap(request, userVo.getOdurUid(), PtConstant.PT_LOGIN, true);
		if(odurLoginMap!=null) defUserUid = odurLoginMap.get("defUserUid");
		
		// 설정이 없으면 - 원직자로 설정
		if(defUserUid==null || defUserUid.isEmpty()){
			defUserUid = userVo.getOdurUid();
		}
		model.put("defUserUid", defUserUid);
		
		
		// 겸직 결재 건수 표시
		odurLoginMap = ptPsnSvc.getUserSetupMap(request, userVo.getOdurUid(), PtConstant.MB_LOGIN, true);
		String useOdurApvCnt = null;
		if(odurLoginMap!=null) useOdurApvCnt = odurLoginMap.get("useOdurApvCnt");
		if(useOdurApvCnt!=null){
			model.put("useOdurApvCnt", useOdurApvCnt);
		}
		
		// 결재 옵션 - 겸직통합 표시
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
		if(!"Y".equals(optConfigMap.get("adurMergLst"))){//겸직통합 표시
			// 조직도 DB 동기화 할 경우 - 사용자 정보 수정 안함
			if("Y".equals(sysPlocMap.get("orgDbSyncEnable"))){
				model.put("orgSyncEnable", Boolean.TRUE);
			}
		} else {
			model.put("adurMergLst", Boolean.TRUE);
		}
		
		return MoLayoutUtil.getJspPath("/pt/psn/setEnv");
	}
	
	/** 환경 설정 */
	@RequestMapping(value = {"/pt/psn/transEnvAjx", "/pt/psn/transEnvAjxPost"})
	public String transEnvAjx(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="secu", required=false) String secu,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception {
		
		boolean isEncrypted = false;
		JSONObject jsonObject = null;
		if(secu!=null){
			try {
				jsonObject = cryptoSvc.processRsa(request);
				isEncrypted = true;
			} catch(CmException e){
				LOGGER.error("Login-Fail : "+e.getMessage());
				//pt.login.fail.decrypt=복호화에 실패하였습니다.
				return LayoutUtil.returnJson(model, e.getMessage());
			}
		} else {
			jsonObject = (JSONObject)JSONValue.parse(data);
		}
		
		QueryQueue queryQueue = new QueryQueue();
		UserVo userVo = LoginSession.getUser(request);
		
		// cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
		String notEncryptedMsg = messageProperties.getMessage("cm.msg.notValidCall", locale);
		
		String which = (String)jsonObject.get("which"), encrypted;
		
		// 개인정보 변경
		if("psnInfo".equals(which)){
			if(!isEncrypted) return LayoutUtil.returnJson(model, notEncryptedMsg);
			
			try {
				String tichCont = (String)jsonObject.get("tichCont");
				if(tichCont==null){
					return LayoutUtil.returnJson(model, notEncryptedMsg);
				}
				
				OrUserBVo orUserBVo = new OrUserBVo();
				orUserBVo.setUserUid(userVo.getUserUid());
				orUserBVo.setTichCont(tichCont);
				queryQueue.update(orUserBVo);
				
				OrUserPinfoDVo orUserPinfoDVo = new OrUserPinfoDVo();
				orUserPinfoDVo.setOdurUid(userVo.getOdurUid());
				
				encrypted = cryptoSvc.encryptPersanal((String)jsonObject.get("mbno"));
				orUserPinfoDVo.setMbnoEnc(encrypted);
				
				encrypted = cryptoSvc.encryptPersanal((String)jsonObject.get("compPhon"));
				orUserPinfoDVo.setCompPhonEnc(encrypted);
				
				encrypted = cryptoSvc.encryptPersanal((String)jsonObject.get("extnEmail"));
				orUserPinfoDVo.setExtnEmailEnc(encrypted);
				
				queryQueue.store(orUserPinfoDVo);
				
				// 일괄실행
				commonSvc.execute(queryQueue);
				
				// cm.msg.save.success=저장 되었습니다.
				String message = messageProperties.getMessage("cm.msg.save.success", locale);
				model.put("message", message);
				model.put("result", "ok");
				return LayoutUtil.returnJson(model);
				
			} catch(CmException e){
				return LayoutUtil.returnJson(model, e.getMessage());
			}
			
		// 로그인 비밀번호, 결재 비밀번호
		} else if("sysPw".equals(which)){
			if(!isEncrypted) return LayoutUtil.returnJson(model, notEncryptedMsg);
			
			try {
				
				String sysPw = (String)jsonObject.get("sysPw");
				String newPw = (String)jsonObject.get("newPw");
				String odurUid = userVo.getOdurUid();
				
				ptPsnSvc.changePw(odurUid, userVo.getUserUid(), sysPw, newPw, userVo.getCompId(), "PSN PW", locale);
				
//				// 패스워드 정책 조회
//				Map<String, String> pwPolc = ptSysSvc.getPwPolc(userVo.getCompId());
//				ptPsnSvc.checkPwPolc(pwPolc, odurUid, newPw, locale);
//				
//				// 암호화된 비밀번호
//				String encryptedNewPw = ptPsnSvc.getEncryptedSysPw(odurUid, sysPw, newPw, locale);
//				String sysdate = commonSvc.querySysdate(new CommonVoImpl("YYYY-MM-DD HH24:MI:SS"));
//				
//				// 사용자비밀번호상세(OR_USER_PW_D) 테이블 - UPDATE
//				OrUserPwDVo orUserPwDVo = new OrUserPwDVo();
//				orUserPwDVo.setOdurUid(odurUid);
//				orUserPwDVo.setPwTypCd("SYS");//SYS:시스템 비밀번호, APV:결재 비밀번호
//				orUserPwDVo.setModDt(sysdate);
//				orUserPwDVo.setModrUid(userVo.getUserUid());
//				orUserPwDVo.setPwEnc(encryptedNewPw);
//				queryQueue.store(orUserPwDVo);
//				
//				// 사용자비밀번호이력(OR_USER_PW_H) 테이블
//				// - 비밀번호 변경 내역의 저장
//				// - 예전 비밀번호로 비밀번호 찾기 기능을 위한 것 - 구현여부 미정
//				
//				// 사용자비밀번호이력(OR_USER_PW_H) 테이블 - INSERT
//				OrUserPwHVo orUserPwHVo = new OrUserPwHVo();
//				orUserPwHVo.setOdurUid(odurUid);
//				orUserPwHVo.setPwTypCd("SYS");//SYS:시스템 비밀번호, APV:결재 비밀번호
//				orUserPwHVo.setModDt(sysdate);
//				orUserPwHVo.setModrUid(userVo.getUserUid());
//				orUserPwHVo.setPwEnc(encryptedNewPw);
//				queryQueue.store(orUserPwHVo);
//				
//				String currYmd = sysdate.substring(0,10);
//				
//				// 사용자설정상세(PT_USER_SETUP_D) 테이블에 저장
//				ptPsnSvc.addUserSetup(queryQueue, odurUid, PtConstant.PT_LOGIN, "sysPwChgDt", currYmd, currYmd, "N");
//				
//				// 일괄실행
//				commonSvc.execute(queryQueue);
//				
//				// 사용자 조직도 동기화 PUSH - 사용자
//				if(pushSyncSvc.hasSync()){
//					pushSyncSvc.syncUsers(odurUid, null, null, null);
//				}
//				
//				// 일괄실행
//				commonSvc.execute(queryQueue);
				
				// cm.msg.save.success=저장 되었습니다.
				String message = messageProperties.getMessage("cm.msg.save.success", locale);
				model.put("message", message);
				model.put("result", "ok");
				return LayoutUtil.returnJson(model);
				
			} catch(CmException e){
				return LayoutUtil.returnJson(model, e.getMessage());
			}
			
		// 결재 비밀번호
		} else if("apvPw".equals(which)){
			if(!isEncrypted) return LayoutUtil.returnJson(model, notEncryptedMsg);
			try {
				
				String sysPw = (String)jsonObject.get("sysPw");
				String newPw = (String)jsonObject.get("newPw");
				String odurUid = userVo.getOdurUid();
				
				// 기존 로그인 비밀번호가 같은지 확인
				ptPsnSvc.getEncryptedSysPw(odurUid, sysPw, null, "PSN APV PW", locale);
				String encryptedNewPw = License.ins.encryptPw(newPw, odurUid);
				String sysdate = commonSvc.querySysdate(new CommonVoImpl("YYYY-MM-DD HH24:MI:SS"));
				
				// 사용자비밀번호상세(OR_USER_PW_D) 테이블 - UPDATE
				OrUserPwDVo orUserPwDVo = new OrUserPwDVo();
				orUserPwDVo.setOdurUid(odurUid);
				orUserPwDVo.setPwTypCd("APV");//SYS:시스템 비밀번호, APV:결재 비밀번호
				orUserPwDVo.setModDt(sysdate);
				orUserPwDVo.setModrUid(userVo.getUserUid());
				orUserPwDVo.setPwEnc(encryptedNewPw);
				queryQueue.store(orUserPwDVo);

				// 일괄실행
				commonSvc.execute(queryQueue);
				
				// cm.msg.save.success=저장 되었습니다.
				String message = messageProperties.getMessage("cm.msg.save.success", locale);
				model.put("message", message);
				model.put("result", "ok");
				return LayoutUtil.returnJson(model);
				
			} catch(CmException e){
				return LayoutUtil.returnJson(model, e.getMessage());
			}
			
		// 결재 서명 방법
		} else if("signMthd".equals(which)){
			
			try {
				String signMthd = (String)jsonObject.get("signMthd");
				if(signMthd==null||signMthd.isEmpty()){
					return LayoutUtil.returnJson(model, notEncryptedMsg);
				}
				
				OrUserBVo orUserBVo = new OrUserBVo();
				orUserBVo.setUserUid(userVo.getUserUid());
				orUserBVo.setSignMthdCd(signMthd);
				queryQueue.update(orUserBVo);
				
				// 일괄실행
				commonSvc.execute(queryQueue);
				
				// cm.msg.save.success=저장 되었습니다.
				String message = messageProperties.getMessage("cm.msg.save.success", locale);
				model.put("message", message);
				model.put("result", "ok");
				return LayoutUtil.returnJson(model);
				
			} catch(Exception e){
				return LayoutUtil.returnJson(model, e.getMessage());
			}
			
		// 초기 페이지
		} else if("initPage".equals(which)){
			
			try {
				
//				// 현재 시간
//				String currYmd = commonSvc.querySysdate(new CommonVoImpl("YYYY-MM-DD"));
				
				// 초기 페이지
				String initPage = (String)jsonObject.get("initPage");
				if(initPage==null||initPage.isEmpty()){
					return LayoutUtil.returnJson(model, notEncryptedMsg);
				}
				
				// 사용자설정상세(PT_USER_SETUP_D) 테이블에 저장
				ptPsnSvc.addUserSetup(queryQueue, userVo.getUserUid(), PtConstant.MB_LOGIN, "initPage", initPage, "N");
				
				// menuOpenYn:로그인 후 메뉴 열기, fixedMenuIcon:메뉴 아이콘 표시
				String value;
				for(String attrId : new String[]{"menuOpenYn", "fixedMenuIcon"}){
					value = (String)jsonObject.get(attrId);
					if(value==null || value.isEmpty()) value = "N";
					
					// 사용자설정상세(PT_USER_SETUP_D) 테이블에 저장
					ptPsnSvc.addUserSetup(queryQueue, userVo.getUserUid(), PtConstant.MB_LOGIN, attrId, value, "N");
				}
				
				// 일괄실행
				commonSvc.execute(queryQueue);
				
				// cm.msg.save.success=저장 되었습니다.
				String message = messageProperties.getMessage("cm.msg.save.success", locale);
				model.put("message", message);
				model.put("result", "ok");
				return LayoutUtil.returnJson(model);
				
			} catch(Exception e){
				return LayoutUtil.returnJson(model, e.getMessage());
			}
			
		// 로그인 사용자 선택
		} else if("defUserUid".equals(which)){
			
			try {
				
//				// 현재 시간
//				String currYmd = commonSvc.querySysdate(new CommonVoImpl("YYYY-MM-DD"));
				
				// 로그인 사용자 선택
				String defUserUid = (String)jsonObject.get("defUserUid");
				if(defUserUid==null||defUserUid.isEmpty()){
					return LayoutUtil.returnJson(model, notEncryptedMsg);
				}
				
				// 초기 사용자 선택을  웹과 모바일을 같이 사용하기 위해
				// PtConstant.MB_LOGIN(모바일용) 대신 PtConstant.PT_LOGIN(웹용)을 사용함
				
				// 사용자설정상세(PT_USER_SETUP_D) 테이블에 저장
				ptPsnSvc.addUserSetup(queryQueue, userVo.getOdurUid(), PtConstant.PT_LOGIN, "defUserUid", defUserUid, "N");
				
				// 일정 대신 겸직 결재 표시
				String useOdurApvCnt = (String)jsonObject.get("useOdurApvCnt");
				ptPsnSvc.addUserSetup(queryQueue, userVo.getOdurUid(), PtConstant.MB_LOGIN, "useOdurApvCnt", useOdurApvCnt, "N");
				
				// 일괄실행
				commonSvc.execute(queryQueue);
				
				// cm.msg.save.success=저장 되었습니다.
				String message = messageProperties.getMessage("cm.msg.save.success", locale);
				model.put("message", message);
				model.put("result", "ok");
				return LayoutUtil.returnJson(model);
				
			} catch(Exception e){
				return LayoutUtil.returnJson(model, e.getMessage());
			}
		}
		
		return LayoutUtil.returnJson(model, notEncryptedMsg);
	}
	
	/** 페이지 목록 갯수 */
	@RequestMapping(value = "/pt/psn/setPageRecCnt")
	public String setPage(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 페이지별레코드설정코드 목록 조회
		List<PtCdBVo> pageRecSetupCdList = ptCmSvc.getCdList("PAGE_REC_SETUP_CD", langTypCd, "Y");
		if(pageRecSetupCdList!=null){
			for(PtCdBVo ptCdBVo : pageRecSetupCdList){
				if("_ALL".equals(ptCdBVo.getCd())){
					model.put("_all", ptCdBVo);
				}
			}
			model.put("pageRecSetupCdList", pageRecSetupCdList);
		}
		
		// 페이지별 레코드 수 조회
		Map<String, Integer> pageRecCntMap = ptPsnSvc.getPageRecCntMap(request);
		if(pageRecCntMap!=null){
			model.put("pageRecCntMap", pageRecCntMap);
		}
		
		// 디폴트 값 세팅
		model.put("_default", PersonalUtil.getDefaultPageRowCount());
		
		return MoLayoutUtil.getJspPath("/pt/psn/setPageRecCnt");
	}

	/** 개인설정 저장 */
	@RequestMapping(value = "/pt/psn/transPageRecCntAjx")
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
				model.put("result", "ok");
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
		
		return LayoutUtil.returnJson(model);
	}
	
}
