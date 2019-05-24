package com.innobiz.orange.web.cm.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.pt.secu.LoginSession;

/** VO 관련 자동화 지원 UTIL */
public class VoUtil {
	
	/** 파라미터 제외 대상 */
	private final static String[] EXCLUDINGS = {"whereSqllet"};
	
	/** IE8 이하 에서 unescape 제외할 필드 목록 */
	//public final static String[] NONCHK_ATTRS = {"ctItro", "survFtr", "bodyHtml", "formBodyXML", "opin", "cont", "survItnt"};
	public final static String[] NONCHK_ATTRS = {"ctItro", "survFtr", "bodyHtml", "opin", "cont", "survItnt"};
	
	/** request 에서 데이터를 추출해서 commonVo에 세팅 */
	public static void bind(HttpServletRequest request, CommonVo commonVo){
		
		HashMap<String, Method> setterMap = new HashMap<String, Method>();
		List<String> attributeList = setters(commonVo, setterMap, EXCLUDINGS);
		int i, size = attributeList.size();
		String attribute, value;
		Method method;
		Class<?> setterParamClass;

		// barConnectedTypes : 서버 사이드에서 데이터 조합시 "-" 를 붙여서 조합함 
		// connectedTypes : 서버 사이드에서 데이터 조합시를 그냥 연결함 
		String[] barConnectedTypes = request.getParameterValues("barConnectedTypes");
		String[] connectedTypes = request.getParameterValues("connectedTypes");
		boolean paramCombin = (barConnectedTypes!=null || connectedTypes!=null);
		
		// unescape 할 필드 체크 여부
		boolean isFieldChk = isIe8(request);
		
		for(i=0;i<size;i++){
			attribute = attributeList.get(i);
			if(paramCombin){
				if(isInArray(barConnectedTypes, attribute)){
					value = connectParam(request, attribute, "-");
				} else if(isInArray(connectedTypes, attribute)){
					value = connectParam(request, attribute, null);
				} else {
					value = request.getParameter(attribute);
				}
			} else {
				value = request.getParameter(attribute);
			}
			
			if(value!=null){
				try {
					method = setterMap.get(attribute);
					if(method!=null){
						setterParamClass = method.getParameterTypes()[0];
						if(setterParamClass.equals(String.class)){
							if("formBodyXML".equals(attribute) || "bodyXml".equals(attribute)){
								value = value.replaceAll("\r\n", "&#10;").replaceAll("&amp;quot;", "&quot;");
							} else if(isFieldChk && !isInArray(NONCHK_ATTRS, attribute)){
								value = EscapeUtil.unescapeValue(value);
							}
							//if(isFieldChk && !isInArray(NONCHK_ATTRS, attribute)) value = EscapeUtil.unescapeValue(value);
							method.invoke(commonVo, new Object[]{value});
						} else if(!value.isEmpty()){
							if(setterParamClass.equals(Integer.class) || setterParamClass.equals(int.class)){
								method.invoke(commonVo, new Object[]{Integer.parseInt(value)});
							} else if(setterParamClass.equals(Long.class) || setterParamClass.equals(long.class)){
								method.invoke(commonVo, new Object[]{Long.parseLong(value)});
							} else if(setterParamClass.equals(Double.class) || setterParamClass.equals(double.class)){
								method.invoke(commonVo, new Object[]{Double.parseDouble(value)});
							} else if(setterParamClass.equals(Float.class) || setterParamClass.equals(float.class)){
								method.invoke(commonVo, new Object[]{Float.parseFloat(value)});
							}
						}
					}
				} catch (Exception ignore) {}
			}
		}
		commonVo.setQueryLang(LoginSession.getLangTypCd(request));
	}

	/** request 에서 데이터를 추출해서 commonVo */
	public static List<? extends CommonVo> bindList(HttpServletRequest request, Class<? extends CommonVo> clazz, String[] validCols){
		
		if(validCols==null || validCols.length==0) return null;
		List<CommonVo> voList = new ArrayList<CommonVo>();
		
		HashMap<String, Method> setterMap = new HashMap<String, Method>();
		List<String> attributeList = collect(clazz, "set", setterMap, EXCLUDINGS, null);
		String[] params = request.getParameterValues(validCols[0]);
		CommonVo[] objs = new CommonVo[params==null ? 0 : params.length];
		int i, size, j, jsize = objs.length;
		
		// 유효체크 컬럼이 Vo 속성에 없으면 속성목록에 더함 - 유효 체크를 위한것
		for(i=0;i<validCols.length;i++){
			if(!attributeList.contains(validCols[i])) attributeList.add(validCols[i]);
		}
		
		// 배열에 해당 클래스의 인스탄스를 담음
		for(i=0;i<jsize;i++){ objs[i] = createObject(clazz); }
		
		Method method;
		Class<?> setterParamClass;
		
		// unescape 할 필드 체크 여부
		boolean isFieldChk = isIe8(request);
		
		size = attributeList.size();
		String attribute, value;
		boolean mandatory;
		for(i=0;i<size;i++){
			attribute = attributeList.get(i);
			params = request.getParameterValues(attribute);
			mandatory = ArrayUtil.isInArray(validCols, attribute);
			for(j=0;j<jsize;j++){
				value = params==null ? null : params.length==1 ? params[0] : j >= params.length ? null : params[j];
				if(objs[j]!=null){
					if(mandatory && (value==null || value.isEmpty())){
						objs[j]=null;
					} else if(value!=null) {
						try {
							method = setterMap.get(attribute);
							if(method!=null){
								setterParamClass = method.getParameterTypes()[0];
								if(setterParamClass.equals(String.class)){
									if("formBodyXML".equals(attribute) || "bodyXml".equals(attribute)){
										value = value.replaceAll("\r\n", "&#10;").replaceAll("&amp;quot;", "&quot;");
									} else if(isFieldChk && !isInArray(NONCHK_ATTRS, attribute)){
										value = EscapeUtil.unescapeValue(value);
									}
									//if(isFieldChk && !isInArray(NONCHK_ATTRS, attribute)) value = EscapeUtil.unescapeValue(value);
									method.invoke(objs[j], new Object[]{value});
								} else if(!value.isEmpty()){
									if(setterParamClass.equals(Integer.class) || setterParamClass.equals(int.class)){
										method.invoke(objs[j], new Object[]{Integer.parseInt(value)});
									} else if(setterParamClass.equals(Long.class) || setterParamClass.equals(long.class)){
										method.invoke(objs[j], new Object[]{Long.parseLong(value)});
									} else if(setterParamClass.equals(Double.class) || setterParamClass.equals(double.class)){
										method.invoke(objs[j], new Object[]{Double.parseDouble(value)});
									} else if(setterParamClass.equals(Float.class) || setterParamClass.equals(float.class)){
										method.invoke(objs[j], new Object[]{Float.parseFloat(value)});
									}
								}
							}
						} catch (Exception ignore) {}
					}
				}
			}
		}
		
		for(i=0;i<jsize;i++){
			if(objs[i]!=null) voList.add(objs[i]);
		}
		return voList;
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
	
	/** VO의 객체를 생성함 */
	private static CommonVo createObject(Class<? extends CommonVo> clazz){
		try {
			return clazz.newInstance();
		} catch (Exception e) {}
		return null;
	}
	
	/** getter 를 수집함 */
	private static List<String> getters(CommonVo commonVo, HashMap<String, Method> getterMap, String[] excludings){
		if(commonVo==null) return null;
		return collect(commonVo.getClass(), "get", getterMap, excludings, null);
	}
	
	/** setter 를 수집함 */
	private static List<String> setters(CommonVo commonVo, HashMap<String, Method> setterMap, String[] excludings){
		if(commonVo==null) return null;
		return collect(commonVo.getClass(), "set", setterMap, excludings, null);
	}
	
	/** getter 또는 setter 를 수집함 */
	public static List<String> collect(Class<?> clazz, String methodType, HashMap<String, Method> map, String[] excludings, String[] includingPieces){
		ArrayList<String> attributeList = new ArrayList<String>();
		Method[] methods = clazz.getMethods();
		Method method;
		int parameterCount = methodType.equals("set") ? 1 : 0;
		String methodName;
		String attribute;
		for(int i=0;i<methods.length;i++){
			method = methods[i];
			if(Modifier.isStatic(method.getModifiers())) continue;
			methodName = method.getName();
			if(methodName.length()>4 && methodName.startsWith(methodType) && method.getParameterTypes().length==parameterCount){
				attribute = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
				if(excludings!=null && isInArray(excludings, attribute)){
					continue;
				}
				if(includingPieces!=null && !isPieceInArray(includingPieces, attribute)){
					continue;
				}
				attributeList.add(attribute);
				if(map!=null) map.put(attribute, method);
			}
		}
		return attributeList;
	}
	
	/** commonVo[parameter]를 읽어서  map[parameter]에 세팅함 */
	public static Map<String, Object> toMap(CommonVo commonVo, Map<String, Object> map){
		if(commonVo==null) return null;
		if(map==null) map = new HashMap<String, Object>();
		HashMap<String, Method> getterMap = new HashMap<String, Method>();
		List<String> attributeList = getters(commonVo, getterMap, null);
		int i, size = attributeList.size();
		String attribute;
		Method method;
		Object obj;
		
		for(i=0;i<size;i++){
			attribute = attributeList.get(i);
			try {
				method = getterMap.get(attribute);
				if(method!=null){
					obj = method.invoke(commonVo, (Object[])null);
					if(obj!=null){
						map.put(attribute, obj);
					}
				}
			} catch (Exception ignore) {}
		}
		map.remove("class");
		return map;
	}
	
	/** map[parameter]을 읽어서  commonVo[parameter]에 세팅함 */
	public static void fromMap(CommonVo commonVo, Map<String, ? extends Object> map){
		if(commonVo==null || map==null) return;
		HashMap<String, Method> setterMap = new HashMap<String, Method>();
		List<String> attributeList = setters(commonVo, setterMap, null);
		int i, size = attributeList.size();
		String attribute, value;
		Object obj;
		Method method;
		Class<?> setterParamClass;
		
		for(i=0;i<size;i++){
			attribute = attributeList.get(i);
			obj = map.get(attribute);
			try {
				if(obj!=null){
					method = setterMap.get(attribute);
					if(method!=null){
						setterParamClass = method.getParameterTypes()[0];
						if(setterParamClass.equals(obj.getClass())){
							method.invoke(commonVo, new Object[]{obj});
						} else {
							value = obj.toString();
							if(setterParamClass.equals(String.class)){
								method.invoke(commonVo, new Object[]{Integer.parseInt(value)});
							} else if(setterParamClass.equals(Integer.class) || setterParamClass.equals(int.class)){
								method.invoke(commonVo, new Object[]{Integer.parseInt(value)});
							} else if(setterParamClass.equals(Long.class) || setterParamClass.equals(long.class)){
								method.invoke(commonVo, new Object[]{Long.parseLong(value)});
							} else if(setterParamClass.equals(Double.class) || setterParamClass.equals(double.class)){
								method.invoke(commonVo, new Object[]{Double.parseDouble(value)});
							} else if(setterParamClass.equals(Float.class) || setterParamClass.equals(float.class)){
								method.invoke(commonVo, new Object[]{Float.parseFloat(value)});
							}
						}
					}
				}
			} catch (Exception ignore) {}
		}
	}
	
	/** CommonVo의 attribute를 세팅함 */
	public static boolean setValue(CommonVo commonVo, String attribute, Object object){
		Method method = findMethod(commonVo.getClass(), "set"+attribute.substring(0, 1).toUpperCase()+attribute.substring(1), 1);
		if(method!=null){
			try {
				method.invoke(commonVo, new Object[]{object});
				return true;
			} catch (Exception ignore) {}
		}
		return false;
	}
	
	/** CommonVo의 attribute를 리턴함 */
	public static Object getValue(CommonVo commonVo, String attribute){
		Method method = findMethod(commonVo.getClass(), "get"+attribute.substring(0, 1).toUpperCase()+attribute.substring(1), 0);
		if(method!=null){
			try {
				return method.invoke(commonVo, (Object[])null);
			} catch (Exception ignore) {}
		}
		return null;
	}
	
	/** CommonVo의 함수 찾기 */
	private static Method findMethod(Class<?> clazz, String methodName, int paramCount){
		Method[] methods = clazz.getMethods();
		for(int i=0;i<methods.length;i++){
			if(methodName.equals(methods[i].getName())
					&& methods[i].getParameterTypes().length == paramCount){
				return methods[i];
			}
		}
		return null;
	}
	
	/** CommonVo 를 String 으로 변환 */
	public static String toString(CommonVo commonVo){
		StringBuffer buffer = new StringBuffer(128);
		HashMap<String, Method> getterMap = new HashMap<String, Method>();
		List<String> attributeList = getters(commonVo, getterMap, null);
		int i, size = attributeList.size();
		String attribute, value;
		Object obj;
		
		buffer.append('[').append(commonVo.getClass().getCanonicalName()).append("]\n");
		for(i=0;i<size;i++){
			attribute = attributeList.get(i);
			if("class".equals(attribute)) continue;
			
			try {
				obj = getterMap.get(attribute).invoke(commonVo, (Object[])null);
				if(obj!=null){
					if(obj instanceof Object[]) value = "has-value";
					else value = obj.toString();
					buffer.append(attribute).append(" : ").append(value).append('\n');
				}
			} catch (Exception ignore) {}
		}
		
		return buffer.toString();
	}
	
	/** attribute가 배열의 값중 일치하는 것이 있는지 여부 리턴(equals 비교) */
	private static boolean isInArray(String[] arr, String attribute){
		if(attribute==null || arr==null) return false;
		for(int i=0;i<arr.length;i++){
			if(attribute.equals(arr[i])) return true;
		}
		return false;
	}
	
	/** attribute가 배열의 값을 포함하는지 여부 리턴(indexOf 비교) */
	private static boolean isPieceInArray(String[] arr, String attribute){
		if(attribute==null || arr==null) return false;
		for(int i=0;i<arr.length;i++){
			if(attribute.indexOf(arr[i])>-1) return true;
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