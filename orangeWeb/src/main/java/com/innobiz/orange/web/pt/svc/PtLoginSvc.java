package com.innobiz.orange.web.pt.svc;

import java.io.IOException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserRoleRVo;
import com.innobiz.orange.web.pt.cust.PtCustLoginSvc;
import com.innobiz.orange.web.pt.ip.ForeignIpBlocker;
import com.innobiz.orange.web.pt.secu.IpChecker;
import com.innobiz.orange.web.pt.secu.LastSessionChecker;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserAuthGrp;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtMnuLoutDVo;
import com.innobiz.orange.web.pt.vo.PtPushMsgDVo;
import com.innobiz.orange.web.pt.vo.PtUserSetupDVo;

/** 로그인 관련 서비스 */
@Service
public class PtLoginSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PtLoginSvc.class);
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 포털 보안 서비스 */
	@Resource(name = "ptSecuSvc")
	private PtSecuSvc ptSecuSvc;

	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 사용자 설정 관련 서비스 */
	@Autowired
	private PtPsnSvc ptPsnSvc;
	
	/** 시스템설정 서비스 */
	@Resource(name = "ptSysSvc")
	private PtSysSvc ptSysSvc;
	
//	/** 메뉴 레이아웃 서비스 */
//	@Autowired
//	private PtLoutSvc ptLoutSvc;
	
	/** 사용자 개인 설정 서비스 */
	@Autowired
	private PtMyMnuSvc ptMyMnuSvc;
	
	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** 로그인 커스트마이즈 서비스 */
	@Autowired
	private PtCustLoginSvc ptCustLoginSvc;
	
	/** 관리자 로그인 관련 서비스 */
	@Autowired
	private PtLoginSecuSvc ptLoginSecuSvc;
	
	/** IP 체크용 객체 - IP 조회 및 정책 적용 */
	@Autowired
	private IpChecker ipChecker;

	/** 중국, 해외 IP 차단 정책에 따른 요청에 차단 및 딜레이 적용 */
	@Autowired
	private ForeignIpBlocker foreignIpBlocker;
	
	/** 사용자UID(겸직자UID)로 세션 생성 */
	public void createUserSessionByUserUid(HttpServletRequest request, HttpServletResponse response, String userUid) throws SQLException, CmException, IOException {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 원직자기본(OR_ODUR_B) 테이블 - 조회
		// KEY인 userUid(사용자UID) 가 아닌 odurUid(원직자UID)로 원직자 기본 조회 
		OrOdurBVo orOdurBVo = new OrOdurBVo();
		orOdurBVo.setUserUid(userUid);
		orOdurBVo.setQueryLang(langTypCd);
		orOdurBVo = (OrOdurBVo)commonDao.queryVo(orOdurBVo);
		
		if(orOdurBVo==null){
			// pt.login.noUser=사용자 정보를 확인 할 수 없습니다.
			String msg = messageProperties.getMessage("pt.login.noUser", request);
			LOGGER.error("NO userUid : "+ userUid+ " - "+msg);
			throw new CmException(msg);
		}
		
		createUserSessionByOdurUid(orOdurBVo.getOdurUid(), userUid, request, response, orOdurBVo);
	}
	/** 원직자UID로 세션 생성
	 * 
	 * @param odurUid 원직자UID
	 * @param userUid 사용자UID(겸직자) - null 이면 개인설정에서 디폴트 설정 한것, 디폴트 설정이 없으면 원직 데이터 조회함
	 * @return 로그인 후 이동할 페이지
	 * */
	public String createUserSessionByOdurUid(String odurUid, String userUid, 
			HttpServletRequest request, HttpServletResponse response, OrOdurBVo orOdurBVo) throws SQLException, CmException, IOException {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		HttpSession session = request.getSession();
		
		// 원직자기본(OR_ODUR_B) 테이블 - 조회 : 원직자 정보가 없으면
		if(orOdurBVo==null){
			orOdurBVo = new OrOdurBVo();
			orOdurBVo.setOdurUid(odurUid);
			orOdurBVo.setQueryLang(langTypCd);
			orOdurBVo = (OrOdurBVo)commonDao.queryVo(orOdurBVo);
			if(orOdurBVo==null){
				// pt.login.noUser=사용자 정보를 확인 할 수 없습니다.
				String msg = messageProperties.getMessage("pt.login.noUser", request);
				LOGGER.error("NO odurUid : "+ odurUid+ " - "+msg);
				throw new CmException(msg);
			}
		}
		
		// 원직자의 개인설정 조회 - 디폴트 로그인 계정, 비밀번호변경일
		Map<String, String> odurLoginMap = ptPsnSvc.getUserSetupMap(request, odurUid, PtConstant.PT_LOGIN, true);
		// 겸직자중 로그인 대상자(파라미터:userUid)가 정해지지 않으면 - 원직자의 디폴트 로그인 계정을 조회함 
		if(userUid==null || userUid.isEmpty()){
			// 초기 설정을 조회해 디폴트 로그인할 userUid 를 세팅함
			userUid = odurLoginMap.get("defUserUid");
		}
		
		// 원직자UID로 사용자기본(OR_USER_B) 테이블 - 조회
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setOdurUid(odurUid);
		orUserBVo.setQueryLang(langTypCd);
		@SuppressWarnings("unchecked")
		List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonDao.queryList(orUserBVo);
		
		// [부서명, 사용자UID] 배열의 겸직 목록 
		ArrayList<String[]> adurList = new ArrayList<String[]>();
		
		if(userUid!=null && userUid.isEmpty()) userUid = null;
		
		// 로그인 할 userUid
		String userUidToLogin = (userUid==null) ? null : userUid;
		
		// 사용자중 원직자
		OrUserBVo odurOrUserBVo = null;
		// 사용자(겸직자)
		orUserBVo = null;
		if(orUserBVoList!=null){
			
			for(OrUserBVo storedOrUserBVo : orUserBVoList){
				
				// 원직자 - 원직자VO에 세팅함
				if("01".equals(storedOrUserBVo.getAduTypCd())){//겸직구분코드 - 01:원직, 02:겸직, 03:파견직
					odurOrUserBVo = storedOrUserBVo;
				}
				
				// 로그인 할 userUid 가 정해져 있고 - 해당 사용자면
				if(userUidToLogin != null && userUidToLogin.equals(storedOrUserBVo.getUserUid())){
					orUserBVo = storedOrUserBVo;//로그인할 사용자 정보
				}
				
				// 사용자상태코드 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 98:숨김, 99:삭제
				// 사용자상태코드 - 확장 - 95~97:로그인 가능 사용자 상태 
				if("02".equals(storedOrUserBVo.getUserStatCd())
						|| ArrayUtil.isInArray(PtConstant.USER_STAT_ADU_LGIN, storedOrUserBVo.getUserStatCd())){
					// 계정별 [부서명, 사용자UID] 배열을 adurList에 담음
					adurList.add(new String[]{ storedOrUserBVo.getOrgRescNm(), storedOrUserBVo.getUserUid() });
				}
			}
		}
		
		if(orUserBVo==null){
			if(odurOrUserBVo==null){
				// pt.login.noOdur=원직자 정보를 확인 할 수 없습니다.
				String msg = messageProperties.getMessage("pt.login.noOdur", request);
				LOGGER.error("NO data(OR_USER_B.ADU_TYP_CD:01) - odurUid : "+ odurUid+ " - "+msg);
				throw new CmException(msg);
			}
			
			// 사용자가 겸직하지 않거나, 겸직중 로그인자를 세팅하지 않은 상태
			orUserBVo = odurOrUserBVo;
			userUid = odurOrUserBVo.getUserUid();
		}
		
		boolean byAdminLogin = request.getAttribute("BY_ADMIN") != null;
		boolean byMsg = request.getAttribute("BY_MSG") != null;
		boolean bySso = request.getAttribute("BY_SSO") != null;
		boolean prevExcliLginYn = request.getAttribute("EXCLI_LGIN") != null;
		
		// 최종 로그인 언어 저장
		String lastLangTypCd = !odurUid.equals(userUid) ? null : odurLoginMap.get("lastLangTypCd");
		if(!byAdminLogin && !byMsg && !bySso && (lastLangTypCd==null || !lastLangTypCd.equals(langTypCd))){
			PtUserSetupDVo ptUserSetupDVo = new PtUserSetupDVo();
			ptUserSetupDVo.setUserUid(userUid);
			ptUserSetupDVo.setSetupClsId(PtConstant.PT_LOGIN);
			ptUserSetupDVo.setSetupId("lastLangTypCd");
			ptUserSetupDVo.setSetupVa(langTypCd);
			ptUserSetupDVo.setCacheYn("Y");
			if(commonDao.update(ptUserSetupDVo)==0){
				commonDao.insert(ptUserSetupDVo);
			}
		}
		
		// 사용자역할관계(OR_USER_ROLE_R) 테이블 - 조회
		OrUserRoleRVo orUserRoleRVo = new OrUserRoleRVo();
		orUserRoleRVo.setUserUid(userUid);
		@SuppressWarnings("unchecked")
		List<OrUserRoleRVo> orUserRoleRVoList = (List<OrUserRoleRVo>)commonDao.queryList(orUserRoleRVo);
		
		// 사용자 Vo - 생성
		UserVo userVo = new UserVo();
		userVo.setLginId	(orOdurBVo.getLginId());	//로그인ID
		userVo.setUserNm	(orOdurBVo.getRescNm());	//사용자명
		
		userVo.setOdurUid	(orOdurBVo.getOdurUid());	//원직자UID
		userVo.setUserUid	(orUserBVo.getUserUid());	//사용자UID
		
		userVo.setCompId	(orUserBVo.getCompId());	//회사ID
		userVo.setOrgId		(orUserBVo.getOrgId());		//조직ID
		userVo.setDeptId	(orUserBVo.getDeptId());	//부서ID
		userVo.setDeptNm	(orUserBVo.getDeptRescNm());//부서명
		
		userVo.setSeculCd   (orUserBVo.getSeculCd());	//보안등급코드
		userVo.setAutoApvLnCd(orUserBVo.getAutoApvLnCd());//자동결재선코드
		userVo.setUserStatCd(orUserBVo.getUserStatCd());//사용자상태코드
		
		// SSO 권한 설정일 경우
		String ssoUserStatCd = (String)request.getAttribute("SSO_USER_STAT_CD");
		if(ssoUserStatCd != null){
			userVo.setUserStatCd(ssoUserStatCd);
			orUserBVo.setUserStatCd(ssoUserStatCd);
		}
		
		userVo.setEmail(orCmSvc.getEmail(orOdurBVo.getOdurUid()));
		
		// 원직자 보안 정보
		Map<String, String> odurSecuMap = orCmSvc.getOdurSecuMap(odurUid);
		userVo.setLginIpExcliYn	("Y".equals(odurSecuMap.get("lginIpExYn")));	//lginIpExYn:로그인IP제외대상여부
		userVo.setSesnIpExcliYn	("Y".equals(odurSecuMap.get("sesnIpExYn")));	//sesnIpExYn:세션IP제외대상여부
		
		// SSO 권한 설정일 경우 - 제외
		if(adurList.size()>1 && ssoUserStatCd == null){
			userVo.setAdurs(ArrayUtil.to2Array(adurList));//겸직목록[부서명, 사용자UID]
		}
		String[] roleCds = toRoleList(orUserRoleRVoList);//롤코드 배열
		userVo.setRoleCds(roleCds);
		
		// 사용자가 속한 권한그룹,사용자그룹,관리자권한그룹 맵을 가져옴
		UserAuthGrp userAuthGrp = ptSecuSvc.getUserAuthGrp(userUid, odurUid, orUserBVo.getCompId(), langTypCd);
		
		// 파트가 아닐 경우
		if(userVo.getOrgId() != null && userVo.getOrgId().equals(userVo.getDeptId())){
			List<String> orgIdList = orCmSvc.getOrgUpIdLine(userVo.getDeptId(), langTypCd);
			String[] arr = ArrayUtil.toReversedArray(orgIdList);
			userVo.setDeptPids(arr);
			userVo.setOrgPids(arr);
		} else {
			List<String> orgIdList = orCmSvc.getOrgUpIdLine(userVo.getDeptId(), langTypCd);
			String[] arr = ArrayUtil.toReversedArray(orgIdList);
			userVo.setDeptPids(arr);
			
			orgIdList = orCmSvc.getOrgUpIdLine(userVo.getOrgId(), langTypCd);
			arr = ArrayUtil.toReversedArray(orgIdList);
			userVo.setOrgPids(arr);
		}
		
		// UserVo에 사용자 권한그룹ID, 관리자 권한그룹ID 세팅 - 권한조합 이용하여 속한 권한그룹을 세팅함
		ptSecuSvc.setAuthGrpIds(userAuthGrp, userVo, orUserBVo, roleCds);
		// SSO 권한 설정일 경우 - 관리자 권한그룹 제거
		if(ssoUserStatCd != null){
			userVo.setAdminAuthGrpIds(new String[]{});
		}
		// 사용자 설정 불러오기 - 개인화
		List<String> delList = new ArrayList<String>();// - map에서 삭제할 목록 - 세션에 올릴 맵 : userSetupMap
		Map<String, Map<String, ?>> userSetupMap = ptPsnSvc.getUserSetup(userUid, userVo.getOdurUid(), delList, request);
		session.setAttribute("userSetupMap", userSetupMap);
		
		if(ServerConfig.IS_MOBILE){
			userVo.setLoutCatId("B");
			userVo.setSkin("blue");
		} else {
			
			// 레이아웃, 스킨 설정
			@SuppressWarnings("unchecked")
			Map<String, String> skinMap = (Map<String, String>)userSetupMap.get(ptPsnSvc.toAttrId(PtConstant.PT_LOUT_USER));
			if(skinMap!=null){
				// 레이아웃유형ID, 스킨경로
				userVo.setLoutCatId(skinMap.get("loutCatId"));
				userVo.setSkin(skinMap.get("skin"));
			} else {
				skinMap = ptSysSvc.getDefaultLayout();
				userVo.setLoutCatId(skinMap.get("loutCatId"));
				userVo.setSkin(skinMap.get("skin"));
			}
		}
		
		userVo.setLangTypCd(langTypCd);
		
		// 겸직이 설정되어 있으면 - SSO 권한 설정일 경우 - 제외
		if(adurList.size()>1 && ssoUserStatCd == null){
			// 겸직자의 userVo를 additionalDutyVoList 에 담음
			List<UserVo> additionalDutyVoList = new ArrayList<UserVo>();
			for(OrUserBVo storedOrUserBVo : orUserBVoList){
				if(userUid.equals(storedOrUserBVo.getUserUid())){
					additionalDutyVoList.add(userVo);
				} else {
					addToAdditionalDutyVoList(additionalDutyVoList, orOdurBVo, storedOrUserBVo, odurSecuMap, adurList, langTypCd);
				}
			}
			userVo.setAdditionalDutyVoList(additionalDutyVoList);
		}
		
		// 사용자 정보 세션에 저장
		session.setAttribute("userVo", userVo);
		
		Map<String, String> pwPolc = ptSysSvc.getPwPolc(userVo.getCompId());
		
		// 비밀번호 변경일 체크
		if(!byAdminLogin && !byMsg && !bySso && !ServerConfig.IS_MOBILE && "Y".equals(pwPolc.get("polcUseYn"))){
			// 비밀번호 정책 예외자가 아니면
			List<String> pwException = ptSysSvc.getPtPwExceptionUser();
			if(pwException==null || pwException.isEmpty() || !pwException.contains(userVo.getOdurUid())){
				if(odurLoginMap==null ||  ptSysSvc.isPwPlocViolated(odurLoginMap.get("sysPwChgDt"), userVo.getCompId())){
					String url = ptSecuSvc.toAuthMenuUrl(userVo, PtConstant.URL_SET_PW, null);
					session.setAttribute("FORCE_MOVE", url);
				}
			}
		}
		
		String initPage = null;
		if(!ServerConfig.IS_MOBILE){
			// 레이아웃 별 초기 페이지 조회
			String mnuLoutId = null;
			Map<String, ?> loginMap = userSetupMap.get("loginMap");
			if(loginMap!=null){
				if("B".equals(userVo.getLoutCatId())) mnuLoutId = (String)loginMap.get("bascInitPage");
				else if("I".equals(userVo.getLoutCatId())) mnuLoutId = (String)loginMap.get("iconInitPage");
			}
			
			// 초기 페이지 설정
			boolean defaultHome = true;
			boolean whenLogin = true;
			
			String ip = ipChecker.getIp(request);
			userVo.setLginIp(ip);
			boolean internalIp = byAdminLogin ? true : ipChecker.isInternalIpRange(ip);// 내부망 IP 여부
			userVo.setInternalIp(internalIp);
			
			PtMnuLoutDVo ptMnuLoutDVo = ptSecuSvc.getAuthedPtMnuLoutDVo(userVo, mnuLoutId, defaultHome);
			if(ptMnuLoutDVo == null){
				initPage = ptSecuSvc.toAuthMenuUrl(userVo, PtConstant.URL_SET_INIT, null);
			} else if(ptMnuLoutDVo.isMyMnu()){
				initPage = ptMyMnuSvc.getFirstMyPage(request, userVo);
			} else {
				initPage = ptSecuSvc.getUrlByPtMnuLoutDVo(userVo, ptMnuLoutDVo, whenLogin);
			}
			if(initPage==null){
				initPage = ptSecuSvc.toAuthMenuUrl(userVo, PtConstant.URL_SET_INIT, null);
			}
		} else {
			String ip = ipChecker.getIp(request);
			userVo.setLginIp(ip);
			userVo.setInternalIp(true);// 내부망 IP 여부
		}
		
		// 권한 있는 모듈 참조 ID
		userVo.setMnuGrpMdRids(ptSecuSvc.getAuthdMnuGrpByMdRids(userVo, PtConstant.AUTH_CHK_MNU_GRP_MD_RIDS));
		
		if(byAdminLogin) userVo.setAdminSesn(Boolean.TRUE);
		if(prevExcliLginYn) userVo.setExcliLginYn(true);
		
		// 캐쉬 하지 않을 사용자 설정 제거
		for(String delAttrId : delList){
			userSetupMap.remove(delAttrId);
		}
		
		// 해당 세션ID 저장 - 중복 로그인 방지 처리
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		boolean checkLastSession = "Y".equals(sysPlocMap.get("blockDupLogin"));
		LastSessionChecker.setLastSession(checkLastSession, odurUid, request);
		
		// 고객사별 커스트마이즈 로그인 진행
		ptCustLoginSvc.processCustLogin(request, response, userVo, odurOrUserBVo, orOdurBVo);
		
		return initPage;
	}
	
	/** 겸직자 VO 목록에 추가하기 */
	private void addToAdditionalDutyVoList(List<UserVo> additionalDutyVoList,
			OrOdurBVo orOdurBVo, OrUserBVo orUserBVo, 
			Map<String, String> odurSecuMap, ArrayList<String[]> adurList, String langTypCd) throws SQLException{
		
		String userUid = orUserBVo.getUserUid();
		
		// 사용자역할관계(OR_USER_ROLE_R) 테이블 - 조회
		OrUserRoleRVo orUserRoleRVo = new OrUserRoleRVo();
		orUserRoleRVo.setUserUid(userUid);
		@SuppressWarnings("unchecked")
		List<OrUserRoleRVo> orUserRoleRVoList = (List<OrUserRoleRVo>)commonDao.queryList(orUserRoleRVo);
		
		// 사용자 Vo - 생성
		UserVo userVo = new UserVo();
		userVo.setLginId	(orOdurBVo.getLginId());	//로그인ID
		userVo.setUserNm	(orOdurBVo.getRescNm());	//사용자명
		
		userVo.setOdurUid	(orOdurBVo.getOdurUid());	//원직자UID
		userVo.setUserUid	(orUserBVo.getUserUid());	//사용자UID
		
		userVo.setCompId	(orUserBVo.getCompId());	//회사ID
		userVo.setOrgId		(orUserBVo.getOrgId());		//조직ID
		userVo.setDeptId	(orUserBVo.getDeptId());	//부서ID
		userVo.setDeptNm	(orUserBVo.getDeptRescNm());//부서명
		userVo.setSeculCd   (orUserBVo.getSeculCd());	//보안등급코드
		
		userVo.setLginIpExcliYn	("Y".equals(odurSecuMap.get("lginIpExYn")));	//lginIpExYn:로그인IP제외대상여부
		userVo.setSesnIpExcliYn	("Y".equals(odurSecuMap.get("sesnIpExYn")));	//sesnIpExYn:세션IP제외대상여부
		
		if(adurList.size()>1){
			userVo.setAdurs(ArrayUtil.to2Array(adurList));//겸직목록[부서명, 사용자UID]
		}
		String[] roleCds = toRoleList(orUserRoleRVoList);//롤코드 배열
		
		// 사용자가 속한 권한그룹,사용자그룹,관리자권한그룹 맵을 가져옴
		UserAuthGrp userAuthGrp = ptSecuSvc.getUserAuthGrp(userUid, orOdurBVo.getOdurUid(), orUserBVo.getCompId(), langTypCd);
		
		// 파트가 아닐 경우
		if(userVo.getOrgId() != null && userVo.getOrgId().equals(userVo.getDeptId())){
			List<String> orgIdList = orCmSvc.getOrgUpIdLine(userVo.getDeptId(), langTypCd);
			String[] arr = ArrayUtil.toReversedArray(orgIdList);
			userVo.setDeptPids(arr);
			userVo.setOrgPids(arr);
		} else {
			List<String> orgIdList = orCmSvc.getOrgUpIdLine(userVo.getDeptId(), langTypCd);
			String[] arr = ArrayUtil.toReversedArray(orgIdList);
			userVo.setDeptPids(arr);
			
			orgIdList = orCmSvc.getOrgUpIdLine(userVo.getOrgId(), langTypCd);
			arr = ArrayUtil.toReversedArray(orgIdList);
			userVo.setOrgPids(arr);
		}
		
		// UserVo에 사용자 권한그룹ID, 관리자 권한그룹ID 세팅 - 권한조합 이용하여 속한 권한그룹을 세팅함
		ptSecuSvc.setAuthGrpIds(userAuthGrp, userVo, orUserBVo, roleCds);
		
		additionalDutyVoList.add(userVo);
	}
	
	/** 롤CD를 배열로 전환 */
	private String[] toRoleList(List<OrUserRoleRVo> orUserRoleRVoList){
		if(orUserRoleRVoList!=null){
			ArrayList<String> roleList = new ArrayList<String>();
			for(OrUserRoleRVo orUserRoleRVo : orUserRoleRVoList){
				roleList.add(orUserRoleRVo.getRoleCd());
			}
			if(!roleList.isEmpty()) return ArrayUtil.toArray(roleList);
			return null;
		} else {
			return null;
		}
	}
	
	/** 메세지ID로 메세지 상세내역 조회 */
	public PtPushMsgDVo getPushMsgDetl(String msgId) throws SQLException{
		
		// 메세지 유효시간 체크
		// -- userAgent-Token 방식으로 변경 후 유효시간 체크 안함
		/*
		Map<String, String> mobMsgLginMap = ptSysSvc.getSysSetupMap(PtConstant.MB_MOB_MSG_LGIN, true);
		String autoTime = mobMsgLginMap.get("autoTime");
		int validHour = -1;
		if(autoTime==null){
			validHour = 12;
		} else if(!autoTime.equals("none")){
			try{
				validHour = Integer.parseInt(autoTime);
			} catch(Exception ignore){}
		}
		*/
		PtPushMsgDVo ptPushMsgDVo = new PtPushMsgDVo();
		ptPushMsgDVo.setPushMsgId(msgId);
		ptPushMsgDVo = (PtPushMsgDVo)commonDao.queryVo(ptPushMsgDVo);
		if(ptPushMsgDVo==null) return null;
		/*
		// 유효 시간이 지나면 vaild 하지 않음
		long timeDiff = System.currentTimeMillis() - Timestamp.valueOf(ptPushMsgDVo.getIsuDt()).getTime();
		if(validHour<0 || timeDiff > (validHour * 60 * 60 * 1000)) ptPushMsgDVo.setValid(false);
		else ptPushMsgDVo.setValid(true);
		*/
		// -- userAgent-Token 방식으로 변경 후 유효시간 체크 안함
		ptPushMsgDVo.setValid(true);
		
		return ptPushMsgDVo;
	}
	
	/** 푸쉬메시지상세 VO 로 로그인 시키기 */
	public UserVo processLoginByPtPushMsgDVo(HttpServletRequest request, HttpServletResponse response, 
			PtPushMsgDVo ptPushMsgDVo, ModelMap model) throws IOException, CmException, SQLException{
		
		HttpSession session = request.getSession(true);
		String langTypCd = ptPushMsgDVo.getLangTypCd();
		Locale locale = SessionUtil.toLocale(langTypCd);
		session.setAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE", locale);
		
		// admin 계정 로그인
		if("U0000001".equals(ptPushMsgDVo.getUserUid())){
			session.setAttribute("SKIP_ADMIN_PW", Boolean.TRUE);
			ptLoginSecuSvc.processAdminLogin(request, session, null, locale);
			return LoginSession.getUser(request);
			
		// 일반계정 로그인
		} else {
			
			// 원직자기본(OR_ODUR_B) 테이블 - 로그인 아이디로 사용자 정보 조회
			OrOdurBVo orOdurBVo = new OrOdurBVo();
			orOdurBVo.setUserUid(ptPushMsgDVo.getUserUid());
			orOdurBVo.setQueryLang(langTypCd);
			orOdurBVo = (OrOdurBVo)commonDao.queryVo(orOdurBVo);
			
			if(orOdurBVo==null){
				LOGGER.error("Login-Fail by msgId : no user(OR_ODUR_B) : pushMsgId: "+ptPushMsgDVo.getPushMsgId()+"  userUid:"+ptPushMsgDVo.getUserUid());
				// pt.login.noUser=사용자 정보를 확인 할 수 없습니다.
				model.addAttribute("errorByMsgId", messageProperties.getMessage("pt.login.noUser", locale));
				return null;
			}
			String odurUid = orOdurBVo.getOdurUid();
			
			// 사용자상태코드 체크 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 99:삭제
			if(!"02".equals(orOdurBVo.getUserStatCd())){
				//pt.login.notAllowedStat=로그인 가능 사용자가 아닙니다.
				LOGGER.error("Login-Fail by msgId : user stat(not 02) - pushMsgId:"+ptPushMsgDVo.getPushMsgId()+"  odurUid:"+odurUid+"  userStatCd:"+orOdurBVo.getUserStatCd());
				model.addAttribute("errorByMsgId", messageProperties.getMessage("pt.login.notAllowedStat", locale));
				return null;
			}
			
			// 모바일의 경우
			if(ServerConfig.IS_MOBILE){
				// 원직자 보안 정보
				//   lginIpExYn:로그인IP제외대상여부, sesnIpExYn:세션IP제외대상여부, useMobYn:모바일사용여부, 
				//   useMsgLginYn:메세지로그인여부, useMsgrYn:메신저사용여부, useMailYn:메일사용여부
				Map<String, String> odurSecuMap = orCmSvc.getOdurSecuMap(odurUid);
				
				// useMobYn:모바일사용여부
				if("N".equals(odurSecuMap.get("useMobYn"))){
					//pt.login.notAllowedStat=로그인 가능 사용자가 아닙니다.
					LOGGER.error("Login-Fail by msgId - mobUseYn:N  odurUid:"+orOdurBVo.getOdurUid()+"  pushMsgId:"+ptPushMsgDVo.getPushMsgId());
					model.addAttribute("errorByMsgId", messageProperties.getMessage("pt.login.notAllowedStat", locale));
					return null;
				}
				
				// useMsgLginYn:메세지로그인여부
				if("N".equals(odurSecuMap.get("useMsgLginYn"))){
					//pt.login.notAllowLoginByMsg=사용자 인증에 실패하였습니다.
					LOGGER.error("Login-Fail by msgId - msgLginYn:N  odurUid:"+orOdurBVo.getOdurUid()+"  pushMsgId:"+ptPushMsgDVo.getPushMsgId());
					model.addAttribute("errorByMsgId", messageProperties.getMessage("pt.login.notAllowLoginByMsg", locale));
					return null;
				}
			}
			
			request.setAttribute("BY_MSG", Boolean.TRUE);
			// 사용자 세션 정보 생성
			createUserSessionByOdurUid(odurUid, ptPushMsgDVo.getUserUid(), request, response, orOdurBVo);
			
			// 사용자 세션정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 내부망 - 외부망 : 정책 체크
			if(ipChecker.isInternalIpOnlyPloc() && !userVo.isInternalIp()){// 내부망만 사용 & !내부망 IP
				if(!userVo.getLginIpExcliYn()){// 로그인IP제외대상여부
					request.getSession().invalidate();
					LOGGER.error("MSG Login-Fail : IP Policy - pushMsgId:"+ptPushMsgDVo.getPushMsgId()+"  odurUid:"+odurUid+"  ip:"+userVo.getLginIp());
					return null;
				} else {
					userVo.setExcliLginYn(true);//제외대상로그인여부
				}
			}
			
			// 국내 - 해외 - 중국 : 정책 체크
			if(!userVo.getLginIpExcliYn()){// 로그인IP제외대상여부
				boolean requestOk = foreignIpBlocker.isLoginNoBlockByMsgId(request, ptPushMsgDVo.getPushMsgId());
				if(!requestOk){
					//해외 IP 로그인 Fail 응답 딜레이
					try{ Thread.sleep(1000); }catch(Exception ignore){}
					request.getSession().invalidate();
					return null;
				}
			}
			
//			// IP 정책 체크 - 외부 로그인 가능한 사용자 체크
//			String ip = ipChecker.getIp(request);
//			boolean internalIp = ipChecker.isInternalIpRange(ip);
//			
//			userVo.setLginIp(ip);// 로그인IP
//			userVo.setInternalIp(internalIp);// 내부망 IP 여부
//			
//			boolean excliLginYn        = userVo.getLginIpExcliYn();// 제외대상로그인여부
//			boolean internalIpOnlyPloc = ipChecker.isInternalIpOnlyPloc();// 내부망만 사용 정책 여부
//			
//			// 제외대상자-가 아니고, 내부망 정책에 내부망IP 가 아닌 경우
//			if(!excliLginYn && !(internalIpOnlyPloc && internalIp) ){
//				boolean requestOk = foreignIpBlocker.isLoginNoBlockByMsgId(request, ptPushMsgDVo.getPushMsgId());
//				if(!requestOk){
//					LOGGER.error("Login-Fail by msgId : Foreign IP Policy - pushMsgId:"+ptPushMsgDVo.getPushMsgId()+"  odurUid:"+odurUid+"  ip:"+ip);
//					//해외 IP 로그인 Fail 응답 딜레이
//					try{ Thread.sleep(1000); }catch(Exception ignore){}
//					request.getSession().invalidate();
//					// pt.login.notAllowedIp=보안 정책에 의해 로그인이 차단 되었습니다.(로그인 불가능 IP)
//					model.addAttribute("errorByMsgId", messageProperties.getMessage("pt.login.notAllowedIp", locale));
//					return null;
//				}
//			}
			return userVo;
		}
	}
	
	/** 동일한 사용자 인지 체크 */
	public boolean isSameUser(UserVo userVo, String userUid){
		String[][] adurs = userVo.getAdurs();
		if(adurs != null && userUid != null){
			for(String[] adur : adurs){
				if(userUid.equals(adur[1])) return true;
			}
		}
		return false;
	}
}
