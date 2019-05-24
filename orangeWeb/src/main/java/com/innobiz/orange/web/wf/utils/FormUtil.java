package com.innobiz.orange.web.wf.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;

import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.EscapeUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonVo;

/** 파라미터 관련 자동화 지원 UTIL */
public class FormUtil {
	
	/** request 에서 데이터를 추출해서 Map에 세팅 */
	public static void bind(HttpServletRequest request, Map<String, Object> paramMap, List<String> attributeList, String[] nonchkAttrs){
		
		int i, size = attributeList.size();
		String attribute, value;

		// barConnectedTypes : 서버 사이드에서 데이터 조합시 "-" 를 붙여서 조합함 
		// connectedTypes : 서버 사이드에서 데이터 조합시를 그냥 연결함 
		String[] barConnectedTypes = request.getParameterValues("barConnectedTypes");
		String[] connectedTypes = request.getParameterValues("connectedTypes");
		boolean paramCombin = (barConnectedTypes!=null || connectedTypes!=null);
		
		// unescape 할 필드 체크 여부
		boolean isFieldChk = isIe8(request);
		
		String[] params=null;
		for(i=0;i<size;i++){
			value=null;
			attribute = attributeList.get(i);
			params=request.getParameterValues(attribute);
			if(paramCombin){
				if(isInArray(barConnectedTypes, attribute)){
					value = connectParam(request, attribute, "-");
				} else if(isInArray(connectedTypes, attribute)){
					value = connectParam(request, attribute, null);
				} else {
					if(params!=null && params.length>1){
						for(String param : params){
							if(value==null) value=param;
							else value+=","+param;
						}
					}else{
						value = request.getParameter(attribute);
					}
				}
			} else {
				if(params!=null && params.length>1){
					for(String param : params){
						if(value==null) value=param;
						else value+=","+param;
					}
				}else{
					value = request.getParameter(attribute);
				}
			}
			//System.out.println(attribute+" : "+value);
			if(value!=null){
				try {
					if("formBodyXML".equals(attribute) || "bodyXml".equals(attribute)){
						value = value.replaceAll("\r\n", "&#10;").replaceAll("&amp;quot;", "&quot;");
					} else if(isFieldChk && !isInArray(nonchkAttrs, attribute)){
						value = EscapeUtil.unescapeValue(value);
					}
					//System.out.println(attribute+" : "+value);
					paramMap.put(attribute, value);
				} catch (Exception ignore) {}
			}
		}
	}
	
	/** json 에서 데이터를 추출해서 Map에 세팅 */
	public static void bindJsonMap(JSONObject jsonVa, Map<String, Object> paramMap, List<String> attributeList, String[] nonchkAttrs){
		if(jsonVa==null || jsonVa.isEmpty()) return;
		int i, size = attributeList.size();
		String attribute, value;

		for(i=0;i<size;i++){
			value=null;
			attribute = attributeList.get(i);
			value=(String)jsonVa.get(attribute);
			
			if(value!=null && !isInArray(nonchkAttrs, attribute)){
				paramMap.put(attribute, value);
			}
		}
	}
	
	/** Map to Vo */
	public static void setParamToVo(Map<String, Object> paramMap, CommonVo commonVo, String[] attributes){
		if(paramMap==null || commonVo==null) return;
		// set header
		Entry<String, Object> entry;
		Iterator<Entry<String, Object>> iterator = paramMap.entrySet().iterator();
		String key;
		while(iterator.hasNext()){
			entry = iterator.next();
			key = entry.getKey();
			if(attributes!=null && !ArrayUtil.isInArray(attributes, key)) continue;
			VoUtil.setValue(commonVo, key, entry.getValue());
		}
	}
	
	/** 파라미터 연결해서 리턴함 : 전화번호, 우편번호, 주민번호, 팩스번호... */
	private static String connectParam(HttpServletRequest request, String attribute, String connectString) {
		StringBuilder builder = new StringBuilder();
		String value;
		int i = 0;
		for(i=1;i<10;i++){
			value = request.getParameter(attribute+i);
			if(value==null) break;
			if(value.isEmpty()) return "";
			if(i>1 && connectString!=null) builder.append(connectString);
			builder.append(value);
		}
		return builder.toString();
	}
	
	/** attribute가 배열의 값중 일치하는 것이 있는지 여부 리턴(equals 비교) */
	private static boolean isInArray(String[] arr, String attribute){
		if(attribute==null || arr==null) return false;
		for(int i=0;i<arr.length;i++){
			if(attribute.equals(arr[i])) return true;
		}
		return false;
	}
	
	/** unescape 할 필드 체크 여부 - IE8 이하 */
	public static boolean isIe8(HttpServletRequest request){
		if(ServerConfig.IS_MOBILE) return false;
		String agent = request.getHeader("User-Agent");
		if(agent==null || agent.isEmpty()) return false;
		
		int p;
		if((p=agent.indexOf("MSIE"))>0){
			String ver = agent.substring(p+5, agent.indexOf(";",p+5));
			if((p=ver.indexOf('.'))>0) ver = ver.substring(0, p).trim();
			if(Integer.parseInt(ver)<9) return true;
		} else if((p=agent.indexOf("Trident"))>0){
			p = agent.indexOf("rv:",p+8);
			if(Integer.parseInt(agent.substring(p+3, agent.indexOf('.',p+3)))<9) return true;
		} 
		return false;
	}
}