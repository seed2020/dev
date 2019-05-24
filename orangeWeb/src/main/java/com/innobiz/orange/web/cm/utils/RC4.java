package com.innobiz.orange.web.cm.utils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class RC4 {
	private static String key = "GPROONE";
	// 리눅스 버전용 암호화 조합키
	private static String combKey = "|";
	
	public RC4(){
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}
	
	/** 암호화 */
	public static String getEncrypt(String txt) throws IOException, GeneralSecurityException {
		 // 암호화
	    SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "RC4");
	    Cipher cipher = Cipher.getInstance("RC4");
	    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
	    byte[] encrypted_bin = cipher.doFinal(txt.getBytes("UTF-8"));    
	    
	    String encrypted_str = byteArrayTohexString(encrypted_bin);
		//System.out.println("encrypted : " + encrypted_str);
	    //System.out.println("txt : "+txt + "||||  encrypted_str : "+encrypted_str); 
		return encrypted_str;
	}
	
	public static String getDecrypt(String txt) throws IOException, GeneralSecurityException{
		// 복호화
		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "RC4");
		Cipher cipher = Cipher.getInstance("RC4");
		byte[] decrypted_bin = hexStringToByteArray(txt);
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
	    byte[] original = cipher.doFinal(decrypted_bin);
	    String decrypted = new String(original,"UTF-8");
	    //System.out.println("decrypted : " + decrypted);
	    return decrypted;
	}
	
	/** 암호화 키 조합*/
	public static String getEncryptComb(String txt) throws IOException, GeneralSecurityException {
		if(txt.length()==1) txt+=combKey;
		else{
			int p = txt.length()/2;
			txt=txt.substring(0,p)+combKey+txt.substring(p);
		}
		return getEncrypt(txt);
	}
	
	/** 복호화 키 조합*/
	public static String getDecryptComb(String txt) throws IOException, GeneralSecurityException {
		return getDecrypt(txt).replace(combKey, "").trim();
	}
	
	private static String byteArrayTohexString (byte buf[]) {
	    StringBuffer strbuf = new StringBuffer(buf.length * 2);
	    int i;
	
	    for (i = 0; i < buf.length; i++) {
	      if (((int) buf[i] & 0xff) < 0x10)
	  	    strbuf.append("0");
	
	      strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
	    }
	
	    return strbuf.toString();
	  }
    
	private static byte[] hexStringToByteArray(String s) {
      int len = s.length();
      byte[] data = new byte[len/2];

      for(int i = 0; i < len; i+=2){
          data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
      }

      return data;
  }
}


