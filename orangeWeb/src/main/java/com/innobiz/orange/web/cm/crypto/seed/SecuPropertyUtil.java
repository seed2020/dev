package com.innobiz.orange.web.cm.crypto.seed;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import com.innobiz.orange.web.cm.crypto.License;
import com.innobiz.orange.web.cm.exception.CmException;

/** SEED 암호화 복호화 유틸 */
public class SecuPropertyUtil {

	/** 핵사 변환용 배열 */
	private static char[] hexArray = "0123456789abcdef".toCharArray();
	/** Seed Cipher */
	private static final SeedCipher SEED = new SeedCipher();
	
	/** 암호화 */
	public static String encode(String txt, byte[] key) throws IOException {
		//FOR JDK-1.5
		if(txt==null || txt.length()==0) return txt;
		
		//FOR JDK-1.6 or Higher
		//if(txt==null || txt.isEmpty()) return txt;
		return byteToHexa(SEED.encrypt(txt, key, "UTF-8"));
	}
	
	/** 복호화 */
	public static String decode(String txt, byte[] key) throws IOException {
		//FOR JDK-1.5
		if(txt==null || txt.length()==0) return txt;
		
		//FOR JDK-1.6 or Higher
		//if(txt==null || txt.isEmpty()) return txt;
		return SEED.decryptAsString(hexaToByte(txt), key, "UTF-8");
	}
	
	/** 암호화된 파일 읽기 */
	public static String read(String path) throws IOException, CmException {
		return License.ins.decryptProperty(readFile(path));
	}
	
	/** byte > hexa 변환 */
	public static String byteToHexa(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    int v;
	    for ( int j = 0; j < bytes.length; j++ ) {
	        v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	/** hexa > byte 변환 */
	public static byte[] hexaToByte(String hex) {
		if (hex == null || hex.length() == 0) {
			return null;
		}
		byte[] ba = new byte[hex.length() / 2];
		for (int i = 0; i < ba.length; i++) {
			ba[i] = (byte) Integer.parseInt(hex.substring(2*i, 2*(i+1)), 16);
		}
		return ba;
	}
	
	/** 파일 읽기 */
	protected static String readFile(String path) throws IOException {
		if(path==null) return null;
		FileInputStream in = new FileInputStream(path);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len;
		byte[] bytes = new byte[2048];
		try{
			while((len = in.read(bytes, 0, 2048))>0) out.write(bytes, 0, len);
		} finally {
			try{ in.close(); } catch(Exception ignore){}
		}
		return out.toString("UTF-8");
	}
}
