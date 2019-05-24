package com.innobiz.orange.web.ap.utils;

import java.util.ArrayList;
import java.util.List;

/** 결재 공통 유틸 */
public class ApCmUtil {

	/** 동일한 값인지 체크함 */
	public static boolean isIdentical(String cmpr1, String cmpr2){
		if(cmpr1==null || cmpr1.isEmpty()){
			if(cmpr2==null || cmpr2.isEmpty()) return true;
			else return false;
		} else {
			return cmpr1.equals(cmpr2);
		}
	}
	
	/** 배열을 List 로 전환 */
	public static List<String> toList(String ... array){
		if(array==null || array.length==0) return null;
		List<String> list = new ArrayList<String>();
		for(String str : array){
			if(str!=null) list.add(str);
		}
		return list.isEmpty() ? null : list;
	}
	
	/** 검색용 양식명으로 전환 */
	public static String toSrchFormNm(String formNm){
		if(formNm==null || formNm.isEmpty()) return formNm;
		
		formNm = formNm.replaceAll(" ", "");
		formNm = formNm.replaceAll("%", "");
		
		if(!formNm.isEmpty()){
			StringBuilder builder = new StringBuilder(64);
			boolean first = true;
			for(char c : formNm.toCharArray()){
				if(first) first = false;
				else builder.append('%');
				builder.append(c);
			}
			return builder.toString();
		} else {
			return formNm;
		}
	}
}
