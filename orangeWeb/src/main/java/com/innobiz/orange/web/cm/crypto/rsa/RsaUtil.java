package com.innobiz.orange.web.cm.crypto.rsa;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

public class RsaUtil {

	public static String encrypt(String text, RsaKey rsaKey) throws IOException{
		BigInteger pubKey = new BigInteger(rsaKey.getPublicKey(), 16);
		BigInteger modKey = new BigInteger(rsaKey.getModulus(), 16);
		int blockSize = rsaKey.getBlockSize();
		return encrypt(text, pubKey, modKey, blockSize);
	}
	
	public static String encrypt(String text, BigInteger pubKey, BigInteger modKey, int blockSize) throws IOException{
		
		//FOR JDK-1.5
		if(text==null || text.length()==0) return null;
		
		//FOR JDK-1.6 or Higher
		//if(text==null || text.isEmpty()) return null;
		
		int encBlockSize = blockSize*2, blockDiff;
		byte[] src = toUnicode(text).getBytes("UTF-8");
		BigInteger message, encrypted;
		if(blockSize > src.length){
			message = new BigInteger(src);
			encrypted = encrypt(message, pubKey, modKey);
			String encText = encrypted.toString(16);
			blockDiff = encBlockSize - encText.length();
			if(blockDiff>0){
				// 복호화 블럭 사이즈 보다 작게 암호화 된경우 앞에 0을 체워서 맞춤
				StringBuilder builder = new StringBuilder(encBlockSize+2);
				for(; blockDiff>0; blockDiff--){
					builder.append('0');
				}
				builder.append(encText);
				return builder.toString();
			}
			return encText;
		} else {
			int i=0;
			byte[] bytes = new byte[blockSize];
			StringBuilder builder = new StringBuilder();
			String encText;
			
			while(true){
				if((i+1) * blockSize <= src.length){
					System.arraycopy(src, i * blockSize, bytes, 0, bytes.length);
				} else if( i * blockSize < src.length ){
					int size = src.length - (i * blockSize);
					bytes = new byte[size];
					System.arraycopy(src, i * blockSize, bytes, 0, bytes.length);
				} else {
					break;
				}
				i++;
				
				encrypted = encrypt(new BigInteger(bytes), pubKey, modKey);
				encText = encrypted.toString(16);
				
				// 복호화 블럭 사이즈 보다 작게 암호화 된경우 앞에 0을 체워서 맞춤
				blockDiff = encBlockSize - encText.length();
				for(; blockDiff>0; blockDiff--){
					builder.append('0');
				}
				
				builder.append(encText);
			}
			
			return builder.toString();
		}
	}
	
	public static String decrypt(String text, RsaKey rsaKey) throws IOException{
		BigInteger priKey = new BigInteger(rsaKey.getPrivateKey(), 16);
		BigInteger modKey = new BigInteger(rsaKey.getModulus(), 16);
		int blockSize = rsaKey.getBlockSize();
		return decrypt(text, priKey, modKey, blockSize);
	}
	
	public static String decrypt(String encHaxa, BigInteger priKey, BigInteger modKey, int blockSize) throws IOException{
		//FOR JDK-1.5
		if(encHaxa==null || encHaxa.length()==0) return null;
		
		//FOR JDK-1.6 or Higher
		//if(encHaxa==null || encHaxa.isEmpty()) return null;
		
		int decByteSize = blockSize*2, len = encHaxa.length();
		BigInteger encrypted, decrypted;
		if(len==decByteSize){
			encrypted = new BigInteger(encHaxa, 16);
			decrypted = decrypt(encrypted, priKey, modKey);
			return new String(decrypted.toByteArray(), "UTF-8");
		} else {
			
			int i=0;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			while(true){
				if((i+1) * decByteSize <= len){
					encrypted = new BigInteger(encHaxa.substring(i * decByteSize, (i+1) * decByteSize), 16);
				} else {
					break;
				}
				i++;
				
				decrypted = decrypt(encrypted, priKey, modKey);
				out.write(decrypted.toByteArray());
			}
			
			return fromUnicode(out.toString("UTF-8"));
		}
	}
	
	public static String toUnicode(String text){
		StringBuilder builder = new StringBuilder(text.length()*3);
		for(char ch : text.toCharArray()){
			if (ch < 128) {
				builder.append(ch);
			} else if (ch < 0x100) {
				builder.append("\\u00").append(Integer.toHexString(ch));
			} else if (ch < 0x1000) {
				builder.append("\\u0").append(Integer.toHexString(ch));
			} else {
				builder.append("\\u").append(Integer.toHexString(ch));
			}
		}
		return builder.toString();
	}
	
	public static String fromUnicode(String unicode){
		int p = 0, last = 0, len = unicode.length();
		StringBuilder builder = new StringBuilder(len+1);
		while((p = unicode.indexOf("\\u", last))>0){
			if(p>last) builder.append(unicode, last, p);
			if(p+4<=len){
				try{
					builder.append((char)Integer.valueOf(unicode.substring(p+2, p+6), 16).intValue());
					last = 6+p;
				} catch(Exception e){
					builder.append("\\u");
					last += 2;
				}
			} else {
				builder.append("\\u");
				last += 2;
			}
		}
		if(last<len){
			builder.append(unicode, last, len);
		}
		return builder.toString();
	}
	
	private static BigInteger encrypt(BigInteger message, BigInteger encryptKey, BigInteger modulus) {
		return message.modPow(encryptKey, modulus);
	}

	private static BigInteger decrypt(BigInteger encrypted, BigInteger decryptKey, BigInteger modulus) {
		return encrypted.modPow(decryptKey, modulus);
	}
}
