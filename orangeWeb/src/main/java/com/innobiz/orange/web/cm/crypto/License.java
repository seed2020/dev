package com.innobiz.orange.web.cm.crypto;

import java.io.IOException;

import com.innobiz.orange.web.cm.exception.LicenseException;

public interface License {
	
	public static final License ins = LicenseLoader.loadLicense(true);
	
	/** 패스워드 암호화 - 알고리즘:SHA */
	public String encryptPw(String pw, String userUid) throws IOException, LicenseException;
	
	/** 개인정보 암호화 - 알고리즘:SEED */
	public String encryptPersanal(String text) throws IOException, LicenseException;
	
	/** 개인정보 복호화 - 알고리즘:SEED */
	public String decryptPersanal(String encrypted) throws IOException, LicenseException;
	
	/** 쿠키 암호화 - 알고리즘:SEED */
	public String encryptCookie(String text) throws IOException, LicenseException;
	
	/** 쿠키 복호화 - 알고리즘:SEED */
	public String decryptCookie(String encrypted) throws IOException, LicenseException;
	
	/** 프로퍼티 암호화 - 알고리즘:SEED */
	public String encryptProperty(String encrypted) throws IOException, LicenseException;
	
	/** 프로퍼티 복호화 - 알고리즘:SEED */
	public String decryptProperty(String encrypted) throws IOException, LicenseException;
	
	/** SSO, Sync 암호화 - 알고리즘:SEED */
	public String encryptIntegration(String encrypted) throws IOException, LicenseException;
	
	/** SSO, Sync 복호화 - 알고리즘:SEED */
	public String decryptIntegration(String encrypted) throws IOException, LicenseException;

	/** 라이센스 정보 리턴 */
	public String getNotice() throws LicenseException;
	
	/** 라이센스 정보 리턴 */
	public String getCustomerCode();
}
