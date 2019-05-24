package com.innobiz.orange.web.cm.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.vo.CommonVo;

/** JSON 타입으로 통신할 경우 해당 데이터 처리용 */
public class JsonUtil {
	
	/** JSON 으로 리턴하는 Ajax 호출에 대한 변환 함수 */
	public static String returnJson(ModelMap model){
		return returnJson(model, null);
	}
	
	/** JSON 으로 리턴하는 Ajax 호출에 대한 변환 함수 */
	public static String returnJson(ModelMap model, String failMessage){
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("model", model);
		map.put("message", failMessage==null ? "" : failMessage);
		String jsonString = toJson(map);
		model.put("jsonString", jsonString);
		return "/cm/result/toJsonObject";
	}
	
	/** JSON 스트링으로 변환 */
	public static String toJson(Object obj){
		StringBuffer buffer = new StringBuffer(128);
		toJsonFromObject(obj, buffer);
		return buffer.toString();
	}
	
	/** Object 타입 변환 */
	@SuppressWarnings("rawtypes")
	private static void toJsonFromObject(Object obj, StringBuffer buffer){
		if(obj instanceof List){
			toJsonFromList((List)obj, buffer);
		} else if(obj instanceof Map){
			toJsonFromMap((Map)obj, buffer);
		} else if(obj instanceof CommonVo){
			toJsonFromMap(((CommonVo)obj).toMap(), buffer);
		} else {
			buffer
				.append('"')
				.append(JSONObject.escape(obj==null ? "" : obj.toString()))
				.append('"');
		}
	}
	
	/** List 타입 변환 */
	@SuppressWarnings("rawtypes")
	private static void toJsonFromList(List list, StringBuffer buffer){
		buffer.append('[');
		int i, size = list.size();
		for(i=0;i<size;i++){
			if(i>0) buffer.append(',');
			toJsonFromObject(list.get(i), buffer);
		}
		buffer.append(']');
	}
	
	/** Map 타입 변환 */
	@SuppressWarnings("rawtypes")
	private static void toJsonFromMap(Map map, StringBuffer buffer){
		buffer.append('{');
		String key;
		Object object;
		boolean isFirst = true;
		Iterator it =  map.keySet().iterator();
		while(it.hasNext()){
			key = it.next().toString();
			object = map.get(key);
			
			if(shouldSkipFromMap(key)) continue;
			
			if(isFirst) isFirst = false;
			else buffer.append(',');
			
			buffer
				.append('"')
				.append(JSONObject.escape(key))
				.append('"').append(':');
			
			toJsonFromObject(object, buffer);
		}
		buffer.append('}');
	}
	
	/** Map 을 변환할때 생략할 인자인지 체크*/
	private static boolean shouldSkipFromMap(String attributeName){
		return attributeName.equals("class") || attributeName.equals("jsonString");
	}
	
	/** Json > Map 타입 변환 */
	public static Map<String, Object> jsonToMap(JSONObject json) {
	    Map<String, Object> retMap = new HashMap<String, Object>();

	    if(json != null) {
	        retMap = toMap(json);
	    }
	    return retMap;
	}
	
	/** Json > Map 타입 변환 */
	public static Object jsonToObj(String value) {
		if(value==null || value.isEmpty()) return null;

	    if(value != null) {
	    	try{
		    	JSONParser parser = new JSONParser();
		    	Object obj = parser.parse(value);
				
		    	if(obj instanceof JSONArray) {
		        	obj = toList((JSONArray) obj);
		        }else if(obj instanceof JSONObject) {
		        	obj = toMap((JSONObject) obj);
		        }
		    	return obj;
	    	}catch(ParseException pe){
				pe.printStackTrace();
			}
	    }
	    return null;
	}
	
	/** Map 타입 변환 */
	private static Map<String, Object> toMap(JSONObject jsonObject) {
	    Map<String, Object> map = new HashMap<String, Object>();
	    
	    Object obj;
		String key;
	    @SuppressWarnings("unchecked")
	    Iterator<String> iterator = jsonObject.keySet().iterator();
	    while(iterator.hasNext()){
	    	obj = iterator.next();
			if(obj==null) continue;
			key = obj.toString();
			obj = jsonObject.get(key);
			if(obj==null) continue;

	        if(obj instanceof JSONArray) {
	        	obj = toList((JSONArray) obj);
	        }else if(obj instanceof JSONObject) {
	        	obj = toMap((JSONObject) obj);
	        }
	        map.put(key, obj);
	    }
	    return map;
	}
	
	/** List 타입 변환 */
	private static List<Object> toList(JSONArray array) {
	    List<Object> list = new ArrayList<Object>();
	    for(int i = 0; i < array.size(); i++) {
	        Object value = array.get(i);
	        if(value instanceof JSONArray) {
	            value = toList((JSONArray) value);
	        }

	        else if(value instanceof JSONObject) {
	            value = toMap((JSONObject) value);
	        }
	        list.add(value);
	    }
	    return list;
	}
	
}
