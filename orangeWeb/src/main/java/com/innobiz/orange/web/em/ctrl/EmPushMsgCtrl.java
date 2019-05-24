package com.innobiz.orange.web.em.ctrl;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.cm.crypto.CryptoSvc;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserPinfoDVo;
import com.innobiz.orange.web.pt.svc.PtMobilePushMsgSvc;
import com.innobiz.orange.web.pt.svc.PtPsnSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.svc.PtWebPushMsgSvc;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtPushMsgDVo;

/** 모바일 푸쉬 메세지 발송용 - 메일 시스템에서 호출할 인터 페이스 URL */
@Controller
public class EmPushMsgCtrl {
	
//	/** Logger */
//	private static final Logger LOGGER = Logger.getLogger(EmPushMsgCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 암호화 서비스 */
	@Autowired
	private CryptoSvc cryptoSvc;
	
	/** 모바일 푸쉬 메세지 발송용 */
	@Autowired
	private PtMobilePushMsgSvc ptMobilePushMsgSvc;
	
	/** 사용자 개인 설정 서비스 */
	@Autowired
	private PtPsnSvc ptPsnSvc;
	
//	/** 포털 공통 서비스 */
//	@Autowired
//	private PtCmSvc ptCmSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** PC 알림 발송 서비스 */
	@Autowired
	private PtWebPushMsgSvc ptWebPushMsgSvc;
	
	/** 메세지 처리용 프라퍼티 - 다국어 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** context.properties */
	@Resource(name = "contextProperties")
	private Properties contextProperties;
	
	/** 모바일 푸쉬 메세지 발송 - 메일용  */
	@RequestMapping(value = "/cm/mail/sendMobilePush")
	public String sendMobilePush(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="mdId", required=false) String mdId,
			@Parameter(name="subj", required=false) String subj,
			@Parameter(name="url", required=false) String url,
			@Parameter(name="userUid", required=false) String userUid,
			ModelMap model) throws Exception {
		
		String mdRid = "MAIL";
		
		if(mdId==null || mdId.isEmpty()
				|| subj==null || subj.isEmpty()
				|| url==null || url.isEmpty()
				|| userUid==null || userUid.isEmpty()){
			writeError(response, "Empty parameter");
			return null;
		}
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		boolean pushEnabled = "Y".equals(sysPlocMap.get("mobilePushEnable"));
		boolean pcNotiEnable = "Y".equals(sysPlocMap.get("pcNotiEnable")) && "Y".equals(sysPlocMap.get("pcNotiMAIL"));
		if(!(pushEnabled || pcNotiEnable)){
			return null;
		}
		
		String mobUrl = url;
		String webUrl = url;
		
		// url : /zmail/m_login_groupware.nvd?cmd=mail_detail_popup&user_account=u000000f@innogw.com&mail_sn=182
		// webUrl >> webUrl = "/cm/zmailPop.do?mailSn="+mailSn+"&mailboxCode="+mailboxCode+"&cmd=mail_detail";
		
		userUid = userUid.toUpperCase();
		
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setUserUid(userUid);
		orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
		if(orUserBVo==null){
			writeError(response, "No user");
			return null;
		}
		
		// 원직자의 개인설정 조회 - 디폴트 로그인 계정, 비밀번호변경일
		Map<String, String> odurLoginMap = ptPsnSvc.getUserSetupMap(request, orUserBVo.getOdurUid(), PtConstant.PT_LOGIN, true);
		// 기본로그인 UID - 겸직자의 경우 / 설정을 했을 경우 있음
		String defaultUserUid = odurLoginMap.get("defUserUid");
		// 기본로그인 설정된게 있으면 해당UID로 없으면 원직으로 로그인 시킴
		if(defaultUserUid != null) userUid = defaultUserUid;
		else userUid = orUserBVo.getOdurUid();
		
		// 최종 로그인 언어 조회
		String langTypCd = ptPsnSvc.getLastLginLangTypCd(userUid, false);
		if(langTypCd==null) langTypCd = contextProperties.getProperty("login.default.lang", "ko");
		
		Locale locale = SessionUtil.toLocale(langTypCd);
		
		// 개인정보 - 휴대폰 조회
		OrUserPinfoDVo orUserPinfoDVo = new OrUserPinfoDVo();
		orUserPinfoDVo.setUserUid(userUid);
		orUserPinfoDVo = (OrUserPinfoDVo)commonSvc.queryVo(orUserPinfoDVo);
		String mbnoEnc = orUserPinfoDVo==null ? null : orUserPinfoDVo.getMbnoEnc();
		boolean noMbno = mbnoEnc==null || mbnoEnc.isEmpty();
		if(noMbno && !pcNotiEnable){
			writeError(response, "No phone number");
			return null;
		}
		String mbno = noMbno ? null : cryptoSvc.decryptPersanal(mbnoEnc);
		
		// 모바일 url 로부터 web 연계 url 추출
		String mailSn=null, mailboxCode=null;
    	int p, q;
    	String finding = "mail_sn=";
		p = url.indexOf(finding);
		if(p>0){
			q = url.indexOf('&', p+1);
			mailSn = q>0 ? url.substring(p+finding.length(), q) : url.substring(p+finding.length());
		}
		finding = "mailbox_code=";
		p = url.indexOf(finding);
		if(p>0){
			q = url.indexOf('&', p+1);
			mailboxCode = q>0 ? url.substring(p+finding.length(), q) : url.substring(p+finding.length());
		}
		
		if(mailSn==null){
			webUrl = url;
		} else if(mailboxCode==null){
			webUrl = "/cm/zmailPop.do?cmd=mail_detail&mailSn="+mailSn;
		} else {
			webUrl = "/cm/zmailPop.do?cmd=mail_detail&mailSn="+mailSn+"&mailbox_code="+mailboxCode;
		}
		
//		if(!url.startsWith("http")){
//			Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
//			String mailDomain = svrEnvMap.get("mailCall");
//			if(mailDomain==null){
//				LOGGER.error("no mail domain for compId:"+orUserBVo.getCompId());
//				//em.msg.noMailDomain=메일도메인이 설정되지 않았습니다.
//				String msg = messageProperties.getMessage("em.msg.noMailDomain", locale);
//				throw new CmException(msg);
//			}
//			url = "http://"+mailDomain+url;
//		}
		
		boolean useSSL = "Y".equals(sysPlocMap.get("useSSL"));
		if(useSSL){
			mobUrl = ptSysSvc.toZMailSslUrl(mobUrl);
		}
		
		String msgId = StringUtil.getNextHexa(24);
		
		PtPushMsgDVo ptPushMsgDVo = new PtPushMsgDVo();
		ptPushMsgDVo.setPushMsgId(msgId);
		ptPushMsgDVo.setLangTypCd(langTypCd);
		
		ptPushMsgDVo.setMdRid(mdRid);
		ptPushMsgDVo.setMdId(mdId);
		ptPushMsgDVo.setPushSubj(subj);
		ptPushMsgDVo.setMbno(mbno);
		ptPushMsgDVo.setWebUrl(webUrl);
		ptPushMsgDVo.setMobUrl(mobUrl);
		ptPushMsgDVo.setUserUid(userUid);
		ptPushMsgDVo.setIsuDt("sysdate");
		ptPushMsgDVo.setValdLginCnt("3");
		
		commonSvc.insert(ptPushMsgDVo);
		
		if(pushEnabled && !noMbno){
			List<PtPushMsgDVo> ptPushMsgDVoList = new ArrayList<PtPushMsgDVo>();
			ptPushMsgDVoList.add(ptPushMsgDVo);
			// 모바일 푸쉬 메세지 - 보내기
			ptMobilePushMsgSvc.sendMobilePush(ptPushMsgDVoList);
		}
		
		if(pcNotiEnable){
			List<Map<String, String>> pcNotiList = new ArrayList<Map<String, String>>();
			Map<String, String> pcNotiData = new HashMap<String, String>();
			
			pcNotiData.put("oid", orUserBVo.getOdurUid());
			pcNotiData.put("title", messageProperties.getMessage("pt.sysopt.pcNoti.MAIL", locale));
			pcNotiData.put("body", subj);
			pcNotiData.put("url", "/index.do?msgId="+msgId);
			
			pcNotiList.add(pcNotiData);
			ptWebPushMsgSvc.sendAsyncWebPush(pcNotiList);
		}
		
		writeError(response, "OK");
		return null;
	}
	
	/** JSP PrintWriter 에 해당 메세지 출력 */
	private void writeError(HttpServletResponse response, String msg) throws IOException{
		Writer out = response.getWriter();
		out.write(msg);
		out.close();
	}
}
