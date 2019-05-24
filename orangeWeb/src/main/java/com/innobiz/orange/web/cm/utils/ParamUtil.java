package com.innobiz.orange.web.cm.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;

import com.innobiz.orange.web.cm.exception.CmException;

/** URL Parameter 관련 처리 유틸 */
public class ParamUtil {

	/** QueryString 리턴 */
	public static String getQueryString(HttpServletRequest request) {
		return request.getQueryString();
	}

	/** exclude를 제외한 QueryString 리턴 */
	public static String getQueryString(HttpServletRequest request, String... exclude) {
		StringBuilder sb = new StringBuilder();
		if ("get".equals(request.getMethod().toLowerCase())) {
			String[] params = request.getQueryString().split("&");
			String menuId = (String)request.getAttribute("menuId");
			boolean first = true, hasMenuId = false;;
			for (String param : params) {
				String[] split = param.split("=");
				if (!ArrayUtils.contains(exclude, split[0])) {
					if(first) first = false;
					else sb.append('&');
					if("menuId".equals(split[0])){
						menuId = (String)request.getAttribute("menuId");
						if(menuId==null || menuId.isEmpty()) menuId = split[0];
						sb.append("menuId=").append(menuId);
						hasMenuId = true;
					} else {
						sb.append(param);
					}
				}
			}
			if(!hasMenuId && menuId!=null){
				if(!first) sb.append('&');
				sb.append("menuId=").append(menuId);
			}
		} else {
			@SuppressWarnings("rawtypes")
			Enumeration names = request.getParameterNames();
			String menuId;
			boolean first = true;
			while (names.hasMoreElements()) {
				String key = (String) names.nextElement();
				if (!ArrayUtils.contains(exclude, key)) {
					if(first) first = false;
					else sb.append('&');
					if("menuId".equals(key)){
						menuId = (String)request.getAttribute("menuId");
						if(menuId==null || menuId.isEmpty()) menuId = request.getParameter(key);
						sb.append("menuId=").append(menuId);
					} else {
						sb.append(key).append('=').append(request.getParameter(key));
					}
				}
			}
		}
		return sb.toString();
	}

	/** QueryString의 exclude를 제외한 key 목록 리턴 */
	public static List<String> getKeyList(HttpServletRequest request, String... exclude) {
		String[] params = request.getQueryString().split("&");
		ArrayList<String> keyList = new ArrayList<String>();
		for (String param : params) {
			String[] split = param.split("=");
			if (!ArrayUtils.contains(exclude, split[0]))
				keyList.add(split[0]);
		}
		return keyList;
	}

	/** QueryString의 exclude를 제외한 Entry 목록 리턴 */
	@SuppressWarnings("rawtypes")
	public static List<AbstractMap.SimpleEntry> getEntryList(HttpServletRequest request, String... exclude) throws UnsupportedEncodingException {
		String[] params = request.getQueryString().split("&");
		List<AbstractMap.SimpleEntry> entryList = new ArrayList<AbstractMap.SimpleEntry>();
		for (String param : params) {
			if (param.indexOf('=') < 0) continue;
			String[] split = param.split("=");
			if (ArrayUtils.contains(exclude, split[0])) continue;
			String key = null, value = null;
			if (split.length > 0) key = split[0];
			if (split.length > 1) {
				value = URLDecoder.decode(split[1], "UTF-8");
			}
			entryList.add(new AbstractMap.SimpleEntry<String, String>(key, value));
		}
		return entryList;
	}
	
	/** QueryString의 exclude를 제외한 Entry 목록 리턴 */
	@SuppressWarnings("rawtypes")
	public static List<AbstractMap.SimpleEntry> getEntryMapList(HttpServletRequest request, String... exclude) throws UnsupportedEncodingException {
		List<AbstractMap.SimpleEntry> entryList = new ArrayList<AbstractMap.SimpleEntry>();
		Enumeration names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String key = (String) names.nextElement();
			if (!ArrayUtils.contains(exclude, key)) {
				entryList.add(new AbstractMap.SimpleEntry<String, String>(key, request.getParameter(key)));
			}
		}
		
		return entryList;
	}
	
	/** required 여부 확인하고 request의 parameter를 리턴 */
	public static String getRequestParam(HttpServletRequest request, String key, boolean required) throws CmException {
		String param = request.getParameter(key);
		if (required) {
			if (param == null || param.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
		}
		return param;
	}
	
	/** queryString 으로부터 파라미터 맵 리턴 */
    public static Map<String, String> getMapFromQueryString(String queryString) throws IOException{
    	int p;
    	Map<String, String> map = new HashMap<String, String>();
    	String[] params = queryString.split("&");
    	for (String param : params){
    		p = param.indexOf('=');
    		if(p>0){
    			map.put(param.substring(0, p), URLDecoder.decode(param.substring(p+1),"UTF-8"));
    		}
    	}
    	return map;
    }
}
