package com.innobiz.orange.web.pt.utils;

import java.util.regex.Pattern;

/** 패스워드 정규식 체크용 유틸 */
public class PwUtil {

	/** 패스워드 정규식 체크 */
	public static boolean valid(String pw, int min,  int max, boolean hasNumber, boolean hasSpecialLetter){
		StringBuilder builder = new StringBuilder(64);
		builder.append("^.*(?=^.{").append(min).append(',').append(max).append("}$)");
		builder.append("(?=.*[a-zA-Z])");
		if(hasNumber) builder.append("(?=.*[0-9])");
		if(hasSpecialLetter) builder.append("(?=.*[^a-zA-Z0-9])");
		builder.append(".*$");
		return Pattern.matches(builder.toString(), pw);
	}

	/** 길이 체크 */
	public static boolean checkLength(String pw, int min,  int max){
		int len = pw.length();
		return len>=min && len<=max;
	}
	
	/** 알파벳포함 체크 */
	private static final String REGULA_ALPHABET = "^.*(?=.*[a-zA-Z]).*$";
	public static boolean hasAlphabet(String pw){
		return Pattern.matches(REGULA_ALPHABET, pw);
	}
	
	/** 숫자포함 체크 */
	private static final String REGULA_NUMBER = "^.*(?=.*[0-9]).*$";
	public static boolean hasNumber(String pw){
		return Pattern.matches(REGULA_NUMBER, pw);
	}
	
	/** 특수문자포함 체크 */
	private static final String REGULA_SPC_CHAR = "^.*(?=.*[^a-zA-Z0-9]).*$";
	public static boolean hasSpcChar(String pw){
		return Pattern.matches(REGULA_SPC_CHAR, pw);
	}
}
