package com.innobiz.orange.web.cm.crypto;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.crypto.rsa.RsaKey;
import com.innobiz.orange.web.cm.crypto.rsa.RsaKeySet;
import com.innobiz.orange.web.cm.crypto.rsa.RsaUtil;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.exception.LicenseException;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

/**
 * 암호화 서비스
 * <pre>
 * 1. RSA
 *  - RSA 알고리즘(Public key / Private key)를 이용하여 암복호화 함
 *  - 공개키를 Javascript 단으로 전달 후 공개키로 Javascript 이용 암호화 하며 이 클래스는 복호화를 담당함
 *  - 키는 각 바이트별로 265개의 파일에 128개의 키가 저장되어 있음(바이트별 32768 개)
 *  - RsaKeySet 는 한개의 파일에 해당하는 키(128개를) 가지고 있으며 랜덤하게 절반 정도 사용되면 다른 파일을 로드함
 * 
 * 2. SEED
 *  - context.properties 에 있는 SEED 키를 가지고 DB의 개인정보를 암복호화 함
 * 
 * 3. SHA
 *  - USER_UID 를 salt로 패스워드를 암호화함(복호화 안되는 알고리즘)
 * </pre>
 * */
@Service
public class CryptoSvc {

	/** context.properties */
	@Resource(name = "contextProperties")
	private Properties contextProperties;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 로그인용 키셋 : Javascript 암호화 >> 전송 >> java 복호화 */
	private RsaKeySet loginRsaKeySet = null;
	
	/** 쿠키용 키셋 : Javascript 암호화 >> 전송 >> java 복호화 */
	private RsaKeySet cookieRsaKeySet = null;
	
	/** RSA 키셋을 리로드 함
	 * 10 초마다 스프링 스케쥴러에 의해 호출되며
	 * RsaKeySet 이 초기화 되지 않았거나, 자신이 가진 키 목록의 반 정도가 사용되면 리로드 함
	 * */
	@Scheduled(fixedRate = 5 * 1000)
	public synchronized void reloadRsaKey() {
		
		// 로그인 키셋 리로드
		if(loginRsaKeySet==null || loginRsaKeySet.needReload()){
			int bits = 1024;
			try{ bits = Integer.parseInt(contextProperties.getProperty("cm.login.rsa.bit")); }
			catch(Exception ignore){}
			
			try {
				loginRsaKeySet = RsaKeySet.load(bits);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// 보안쿠키 사용여부
		boolean secuCookie = false;
		try {
			Map<String, String> secuPolc = ptSysSvc.getSecuPolc();
			secuCookie = ServerConfig.IS_MOBILE || "Y".equals(secuPolc.get("secuCookie"));
		} catch (SQLException ignore) {}
		
		// 보안쿠키 키셋 리로드
		if(secuCookie){
			if(cookieRsaKeySet==null || cookieRsaKeySet.needReload()){
				int bits = 512;
				try{ bits = Integer.parseInt(contextProperties.getProperty("cm.cookie.rsa.bit")); }
				catch(Exception ignore){}
				
				try {
					cookieRsaKeySet = RsaKeySet.load(bits);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			cookieRsaKeySet = null;
		}
	}
	
	/** 로그인용 RSA 키셋을 리턴 */
	public RsaKey getNextLoginKey(){
		if(loginRsaKeySet!=null) return loginRsaKeySet.next();
		return null;
	}
	
	/** 보안쿠키용 RSA 키셋을 리턴 */
	public RsaKey getNextCookieKey(){
		if(cookieRsaKeySet!=null) return cookieRsaKeySet.next();
		return null;
	}
	
	/** 로그인 암호화(구간 암호화) - 알고리즘:RSA */
	public String encryptLogin(String text, RsaKey rsaKey) throws IOException {
		if(text==null) return null;
		return RsaUtil.encrypt(text, rsaKey);
	}
	
	/** 로그인 복호화(구간 암호화) - 알고리즘:RSA */
	public String decryptLogin(String encrypted, RsaKey rsaKey) throws IOException {
		if(encrypted==null) return null;
		return RsaUtil.decrypt(encrypted, rsaKey);
	}
	
	/** 패스워드 암호화 - 알고리즘:SHA */
	public String encryptPw(String pw, String userUid) throws IOException, CmException {
		if(License.ins==null) throw new LicenseException("Invalid license !");
		if(pw==null) return null;
		return License.ins.encryptPw(pw, userUid);
	}
	
	/** 개인정보 암호화 - 알고리즘:SEED */
	public String encryptPersanal(String text) throws IOException, CmException {
		if(License.ins==null) throw new LicenseException("Invalid license !");
		if(text==null) return null;
		return License.ins.encryptPersanal(text);
	}
	
	/** 개인정보 복호화 - 알고리즘:SEED */
	public String decryptPersanal(String encrypted) throws IOException, CmException {
		if(License.ins==null) throw new CmException("Invalid license !");
		if(encrypted==null) return null;
		return License.ins.decryptPersanal(encrypted);
	}
	
	/** 쿠키 암호화 - 알고리즘:SEED */
	public String encryptCookie(String text) throws IOException, CmException {
		if(License.ins==null) throw new LicenseException("Invalid license !");
		if(text==null) return null;
		return License.ins.encryptCookie(text);
	}
	
	/** 쿠키 복호화 - 알고리즘:SEED */
	public String decryptCookie(String encrypted) throws IOException, CmException {
		if(License.ins==null) throw new LicenseException("Invalid license !");
		if(encrypted==null) return null;
		return License.ins.decryptCookie(encrypted);
	}
	
	/** RSA 준비 - RSA키를 세션에 저장하고, 공개키를 설정한 모델을 리턴한다. */
	public void prepareRsa(HttpSession session, ModelMap model){
		// RSA Key 생성
		RsaKey rsaKey = getNextLoginKey();
		// 세션에 KEY 및 생성시간 저장
		session.setAttribute("LOGIN_KEY", rsaKey);
		session.setAttribute("LOGIN_KEY_TIME", Long.valueOf(System.currentTimeMillis()));
		// 공개키 설정
		model.addAttribute("e", rsaKey.getPublicKey());
		model.addAttribute("m", rsaKey.getModulus());
	}
	
	/** RSA 복호화 - secu 파라미터와 세션에 있는 키를 이용 복호화하여 JSONObject 리턴함 */
	public JSONObject processRsa(HttpServletRequest request) throws CmException, IOException {
		return processRsa(request, request.getParameter("secu"));
	}
	
	/** RSA 복호화 - secu 파라미터와 세션에 있는 키를 이용 복호화하여 JSONObject 리턴함 */
	public JSONObject processRsa(HttpServletRequest request, String secu) throws CmException, IOException {
		// secu 파라미터 확인
		if(secu==null || secu.isEmpty()) throw new CmException("no secu param !");
		
		// RSA Key - 세션에서 가져오기
		HttpSession session = request.getSession(false);
		if(session==null){
			throw new CmException("no rsa key in session !");
		}
		RsaKey rsaKey = (RsaKey)session.getAttribute("LOGIN_KEY");
		Long createTime = (Long)session.getAttribute("LOGIN_KEY_TIME");
		
		// 세션에서 RSA Key 제거
		session.removeAttribute("LOGIN_KEY");
		session.removeAttribute("LOGIN_KEY_TIME");
		
		// 세션에 RSA 키와 Timeout 시간 있는지 확인
		if(rsaKey==null || createTime==null){
			throw new CmException("no rsa key in session !");
		}
		
		JSONObject jsonObject = null;
		String decrypted = decryptLogin(secu, rsaKey);
		if(decrypted.startsWith("{") && decrypted.endsWith("}")){
			jsonObject = (JSONObject)JSONValue.parse(decrypted);
		} else {
			throw new CmException("Fail to decode data !");
		}
		
		// RSA Key - timeout 체크
		if(jsonObject !=null && !"Y".equals(jsonObject.get("demoLogin"))){
			Long duration = System.currentTimeMillis() - createTime;
			int timeout = Integer.parseInt(contextProperties.getProperty("cm.login.timeout.sec"));
			if(duration > timeout*1000){
				throw new CmException("rsa key timeout !");
			}
		}
		
		return jsonObject;
	}
}
