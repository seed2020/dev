package com.innobiz.orange.web.pt.svc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.crypto.License;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.FinderUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.pt.secu.IpChecker;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtMnuLoutDVo;

/** 관리자 로그인 관련 서비스 - 관리용 계정 로그인을 처리하는 서비스 */
@Service
public class PtLoginSecuSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PtLoginSecuSvc.class);
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 사용자 설정 관련 서비스 */
	@Autowired
	private PtPsnSvc ptPsnSvc;
	
	/** 사용자 개인 설정 서비스 */
	@Autowired
	private PtMyMnuSvc ptMyMnuSvc;
	
	/** IP 체크용 객체 - IP 조회 및 정책 적용 */
	@Autowired
	private IpChecker ipChecker;
	
	/** 관리자 세션 생성 */
	public String processAdminLogin(HttpServletRequest request,
			HttpSession session, String pw, Locale locale) throws IOException, CmException, SQLException  {
		
		String userUid = null, switchCompId = null;
		String uri = request.getRequestURI();
		
		// admin 겸직 처리가 아닌 경우 - 비밀번호 체크함
		boolean isSwitchUser = uri.indexOf("/processAdurSwitch.do") > 0;
		if(!isSwitchUser){
			
			Map<String, String> adminPwMap = ptSysSvc.getSysSetupMap(PtConstant.PT_ADMIN_PW, false);
			String decTxt, encTxt = adminPwMap.get("value");
			String storedPw = null;
			if(encTxt!=null && !encTxt.isEmpty()){
				decTxt = License.ins.decryptPersanal(encTxt);
				if(decTxt.startsWith("{") && decTxt.endsWith("}")){
					JSONObject jsonObject = (JSONObject)JSONValue.parse(decTxt);
					storedPw = (String)jsonObject.get("pw");
					userUid = (String)jsonObject.get("userUid");
				} else {
					LOGGER.error("Admin Password fail ! - check license file");
				}

				if(!Boolean.TRUE.equals(session.getAttribute("SKIP_ADMIN_PW"))){
					if(pw==null || storedPw==null || !pw.equals(storedPw)){
						// pt.login.noUserNoPw=비밀번호가 다르거나 사용자 정보를 확인 할 수 없습니다.
						String msg = messageProperties.getMessage("pt.login.noUserNoPw", locale);
						LOGGER.error(msg+"  lginId:"+PtConstant.ADMIN_ID);
						throw new CmException(msg);
					}
				} else {
					session.removeAttribute("SKIP_ADMIN_PW");
				}
			}
		} else {
			UserVo storedUserVo = LoginSession.getUser(request);
			userUid = storedUserVo.getUserUid();
			switchCompId = request.getParameter("compId");
		}
		
		
		// 사용자 Vo - 생성
		UserVo userVo = new UserVo();
		userVo.setLginId	(PtConstant.ADMIN_ID);	//로그인ID
		if("ko".equals(locale.getLanguage())){
			userVo.setUserNm	("관리자");	//사용자명
		} else {
			userVo.setUserNm	("Admin");	//사용자명
		}
		userVo.setOdurUid	(userUid);	//원직자UID
		userVo.setUserUid	(userUid);	//사용자UID
		
		// 루트 조직 조회
		OrOrgBVo orOrgBVo = new OrOrgBVo();
		orOrgBVo.setOrgPid("ROOT");
		orOrgBVo.setUseYn("Y");
		orOrgBVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<OrOrgBVo> orgList = (List<OrOrgBVo>)commonSvc.queryList(orOrgBVo);
		if(orgList != null && !orgList.isEmpty()){
			
			int i, size = orgList.size();
			String[][] adurs = size>1 ? new String[size][] : null;
			for(i=0;i<size;i++){
				orOrgBVo = orgList.get(i);
				// switchCompId 가 없을경우(최초 로그인) 를 대비 첫번째 것을 일단 세팅하고
				//  - switchCompId 가 있으면 해당 것으로 다시 세팅함
				if(i==0 || (switchCompId!=null && switchCompId.equals(orOrgBVo.getCompId()))){
					userVo.setCompId(orOrgBVo.getCompId());//회사ID
					userVo.setDeptId(orOrgBVo.getOrgId());//부서ID
					userVo.setOrgId(orOrgBVo.getOrgId());//조직ID
					userVo.setDeptNm(orOrgBVo.getRescNm());//부서명
				}
				
				if(adurs != null && !ServerConfig.IS_MOBILE){
					adurs[i] = new String[]{ orOrgBVo.getRescNm(), orOrgBVo.getCompId() };//겸직목록[부서명, 사용자UID]
				}
			}
			
			if(adurs != null && !ServerConfig.IS_MOBILE){
				userVo.setAdurs(adurs);
			}
			
		} else {
			orOrgBVo = null;
			userVo.setCompId("A01");//회사ID
			userVo.setDeptId("A01");//부서ID
			userVo.setOrgId("A01");//기관ID
		}
		
		String[] orgIds = new String[]{ orOrgBVo==null ? "A01" : orOrgBVo.getOrgId() };
		userVo.setOrgPids(orgIds);
		userVo.setDeptPids(orgIds);
		
		userVo.setAdminAuthGrpIds(new String[]{PtConstant.AUTH_SYS_ADMIN});
		FinderUtil.setMngUid(userUid);// 관리자 - 라이센스 없음 해제
		
		// 사용자 설정 불러오기 - 개인화
		List<String> delList = new ArrayList<String>();// - map에서 삭제할 목록 - 세션에 올릴 맵 : userSetupMap
		Map<String, Map<String, ?>> userSetupMap = ptPsnSvc.getUserSetup(userUid, userVo.getOdurUid(), delList, request);
		request.getSession().setAttribute("userSetupMap", userSetupMap);
		
		// 레이아웃, 스킨 설정
		@SuppressWarnings("unchecked")
		Map<String, String> skinMap = userSetupMap==null ? null :
			(Map<String, String>)userSetupMap.get(ptPsnSvc.toAttrId(PtConstant.PT_LOUT_USER));
		
		if(skinMap!=null){
			// 레이아웃유형ID, 스킨경로
			userVo.setLoutCatId(skinMap.get("loutCatId"));
			userVo.setSkin(skinMap.get("skin"));
		} else {
			skinMap = ptSysSvc.getDefaultLayout();
			userVo.setLoutCatId(skinMap.get("loutCatId"));
			userVo.setSkin(skinMap.get("skin"));
		}
		userVo.setLangTypCd(locale.getLanguage());
		
		// 사용자 정보 세션에 저장
		session.setAttribute("userVo", userVo);

		// 사용자 초기 페이지 설정 - 조회
		String initPage = null, mnuLoutId = null;
		if(!ServerConfig.IS_MOBILE){
			Map<String, ?> loginMap = userSetupMap.get("loginMap");
			if(loginMap!=null){
				if("B".equals(userVo.getLoutCatId())) mnuLoutId = (String)loginMap.get("bascInitPage");
				else if("I".equals(userVo.getLoutCatId())) mnuLoutId = (String)loginMap.get("iconInitPage");
			}
		} else {
			userVo.setLoutCatId("B");
			userVo.setSkin("blue");
		}
		
		// 초기 페이지 설정
		boolean defaultHome = true;
		boolean whenLogin = true;
		
		if(!ServerConfig.IS_MOBILE){
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
		}
		
		// 캐쉬 하지 않을 사용자 설정 제거
		for(String delAttrId : delList){
			userSetupMap.remove(delAttrId);
		}
		
		// 로그인 IP 세팅
		userVo.setLginIp(ipChecker.getIp(request));
		userVo.setInternalIp(true);
		
		// 권한 있는 모듈 참조 ID
		userVo.setMnuGrpMdRids(PtConstant.AUTH_CHK_MNU_GRP_MD_RIDS);
		
		return initPage;
	}
	
}
