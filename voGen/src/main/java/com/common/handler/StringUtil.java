package com.common.handler;

import java.sql.Date;
import java.text.SimpleDateFormat;

/** String 관련 처리 유틸 */
public class StringUtil {
	
	/** 대소문자 차이 */
	private final static char DIFF = 'a' - 'A';
	
	/** yyyy-MM-dd HH:mm:ss 형 */
	private final static SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/** 현재 년월일시분초(yyyy-MM-dd HH:mm:ss) 리턴 */
	public static String getCurrDateTime(){
		return DATE_TIME_FORMAT.format(new Date(System.currentTimeMillis()));
	}
	
	/** CamelNotation 으로 변환 - 언더바(_)뒤의 문자는 대문자로 나머지는 소문자로, 언더바는 제거 */
	public static String toCamelNotation(String txt, boolean isInitCap){
		boolean wasSpace = isInitCap;
		StringBuffer buffer = new StringBuffer(txt.length());
		for(char c: txt.toCharArray()){
			if(c==' ') continue;
			else if(c=='_') wasSpace = true;
			else {
				if(wasSpace){
					if(c>='a' && c<='z') c -= DIFF;
					wasSpace = false;
				} else {
					if(c>='A' && c<='Z') c += DIFF;
				}
				buffer.append(c);
			}
		}
		return buffer.toString();
	}
	
	/** 앞에 한글자만 대문자로 치환 */
	public static String changeInitCap(String str){
		
		char[] arrChar = str.toCharArray();
		arrChar[0] = Character.toUpperCase(arrChar[0]);
		
		return String.valueOf(arrChar);
	}
	
	
}


