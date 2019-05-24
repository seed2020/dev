package com.innobiz.orange.mobile.cm.utils;

import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.utils.JsonUtil;

/** 타일즈 Layout 적용 유틸 */
public class MoLayoutUtil {
	
	/** tiles layout을 적용한 jsp path 리턴 */
	public static String getJspPath(String path){
		if(path.endsWith("WinPop")) return "winpop"+path;
		else if(path.endsWith("Pop") || path.endsWith("Ajx") || path.endsWith("AjxPost")) return path;
		else if(path.endsWith("Sub")) return "sublayout"+path;
		else if(path.endsWith("Frm")||path.endsWith("Plt")) return "nolayout"+path;
		else if(path.indexOf("/process")>=0 || path.indexOf("/trans")>=0) return "nolayout"+path;
		return "layout"+path;
	}
	
	/** tiles layout을 적용한 jsp path 리턴 */
	public static String getJspPath(String path, String jspType){
		if(jspType==null || jspType.isEmpty()) return getJspPath(path);
		if(path.endsWith("WinPop")) return "winpop"+path;
		else if(jspType.equals("Pop") || jspType.equals("Ajx")) return path;
		else if(jspType.equals("Sub")) return "sublayout"+path;
		else if(jspType.equals("Frm")||jspType.equals("Plt")) return "nolayout"+path;
		else if(jspType.equals("process")||jspType.equals("trans")) return "nolayout"+path;
		else if(jspType.equals("empty")) return "empty"+path;
		else if(jspType.equals("ct")) return "ctlayout"+path;
		else if(jspType.equals("wc")) return "wclayout"+path;
		else if(jspType.equals("preview")) return "previewlayout"+path;
		return "layout"+path;
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
