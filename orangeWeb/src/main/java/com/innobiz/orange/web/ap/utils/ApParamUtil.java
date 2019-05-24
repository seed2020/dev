package com.innobiz.orange.web.ap.utils;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

public class ApParamUtil {

	/** 파라미터명 배열 스트링 리턴 */
	public static String[] getParameterNames(HttpServletRequest request, String prefix, String suffix){
		ArrayList<String> list = new ArrayList<String>(128);
		String name;
		
//		@SuppressWarnings("unchecked")
//		Enumeration<String> paraNames = (Enumeration<String>)request.getParameterNames();
		Enumeration<String> paraNames = request.getParameterNames();
		while(paraNames.hasMoreElements()){
			name = paraNames.nextElement();
			if(name==null || name.isEmpty()) continue;
			if(prefix!=null && !name.startsWith(prefix)) continue;
			if(suffix!=null && !name.endsWith(suffix)) continue;
			list.add(name);
		}
		String[] names = new String[list.size()];
		int i = 0;
		for(String va : list){
			names[i++] = va;
		}
		return names;
	}
	
	/** 메뉴ID 제거한 queryString 세팅 */
	public static void setQueryString(HttpServletRequest request){
		String queryString = request.getQueryString();
		queryString = removeParam(queryString, "menuId");
		queryString = removeParam(queryString, "bxId");
		queryString = removeParam(queryString, "secuId");
		request.setAttribute("queryString", queryString);
	}

	/** queryString 에서 해당하는 파라미터 제거 */
	public static String removeParam(String queryString, String paramName){
		if(queryString==null) return queryString;
		
		String finding = paramName+"=";
		int p = queryString.indexOf(finding);
		if(p>=0){
			int q = queryString.indexOf('&', p+finding.length());
			if(p>0){
				if(q>0){
					queryString = queryString.substring(0,p-1)+queryString.substring(q);
				} else {
					queryString = queryString.substring(0,p-1);
				}
			} else {
				if(q>0){
					queryString = queryString.substring(q+1);
				} else {
					queryString = "";
				}
			}
		}
		return queryString;
	}
}
