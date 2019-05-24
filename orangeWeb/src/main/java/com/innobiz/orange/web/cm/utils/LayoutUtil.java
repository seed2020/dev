package com.innobiz.orange.web.cm.utils;

import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.config.ServerConfig;

/** 타일즈 Layout 적용 유틸 */
public class LayoutUtil {
	
	/** tiles layout을 적용한 jsp path 리턴 */
	public static String getJspPath(String path){
		if(ServerConfig.IS_MOBILE) throw new RuntimeException("use : com.innobiz.orange.mobile.cm.utils.MoLayoutUtil");
		if(path.endsWith("Frm")||path.endsWith("Plt")||path.endsWith("WinPop")) return "nolayout"+path;
		else if(path.endsWith("Pop")) return path;
		return "layout"+path;
	}
	
	/** tiles layout을 적용한 jsp path 리턴 */
	public static String getJspPath(String path, String jspType){
		if(ServerConfig.IS_MOBILE) throw new RuntimeException("use : com.innobiz.orange.mobile.cm.utils.MoLayoutUtil");
		if(jspType==null || jspType.isEmpty()) return getJspPath(path);
		if(jspType.equals("Frm")||jspType.equals("Plt")||path.endsWith("WinPop")) return "nolayout"+path;
		else if(jspType.equals("Pop")) return path;
		return "layout"+path;
	}
	
	/** tiles layout을 적용한 download path 리턴 */
	public static String getDownloadPath(String path){
		return "download"+path;
	}
	
	/** 결과처리 JSP 리턴 */
	public static String getResultJsp(){
		return "/cm/result/commonResult";
	}
	
	/** 오류페이지 리턴 - errorNo : 404:페이지없음, 403:권한, 900:라이센스 초과, 500:기타 모든 오류 */
	public static String getErrorJsp(int errorNo){
		if(errorNo==403){
			return "/WEB-INF/jsp/cm/error/error403.jsp";
		} else if(errorNo==404){
			return "/WEB-INF/jsp/cm/error/error404.jsp";
		} else if(errorNo==900){
			return "/WEB-INF/jsp/cm/error/error900.jsp";
		} else if(errorNo==0){
			return "/WEB-INF/jsp/cm/result/commonResult.jsp";
		} else {
			return "/WEB-INF/jsp/cm/error/error500.jsp";
		}
	}
	
	/** json으로 결과변환 - JsonUtil 의 returnJson 호출 */
	public static String returnJson(ModelMap model){
		return JsonUtil.returnJson(model, null);
	}
	
	/** json으로 결과변환 - JsonUtil 의 returnJson 호출 */
	public static String returnJson(ModelMap model, String failMessage){
		return JsonUtil.returnJson(model, failMessage);
	}
}
