package com.innobiz.orange.web.pt.utils;

import com.innobiz.orange.web.cm.utils.EscapeUtil;
import com.innobiz.orange.web.pt.secu.UserVo;

/** 포털 구성 URL 유틸 */
public class LoutUtil {

	/** 페이지 타입 : 팝업 */
	public static final int TYPE_POP = 1;
	/** 페이지 타입 : iframe */
	public static final int TYPE_IFRM = 2;
	/** 페이지 타입 : 포틀릿 */
	public static final int TYPE_PLT = 3;
	/** 페이지 타입 : 포틀릿 내의 프레임 */
	public static final int TYPE_PLT_IFRM = 4;
	/** 페이지 타입 : ajax */
	public static final int TYPE_AJAX = 5;
	/** 페이지 타입 : trans */
	public static final int TYPE_TRANS = 6;
	/** 페이지 타입 : 페이지(상단,좌측메뉴가 붙는 형태) */
	public static final int TYPE_PAGE = 9;
	
	/** 메뉴그룹 URL 리턴 */
	public static String getMnuGrpUrl(String mnuGrpId, String mnuLoutId){
		if(mnuLoutId==null || mnuLoutId.isEmpty()){
			return "/pt/lout/index.do?mnuGrpId="+mnuGrpId;
		} else {
			return "/pt/lout/index.do?mnuGrpId="+mnuGrpId+"&menuId="+mnuLoutId;
		}
	}
	
	/** 메뉴 팝업 설정 함수 리턴(Javascript) */
	public static String getMnuPopFnc(String id, String url, String status, String dialogTitle, boolean isInPop){
		if(isInPop){
			return "openMnuDialog('"+ 
					EscapeUtil.escapeValue(dialogTitle) +"', '"+ 
					addMenuId(url, id) +"', event);";
		} else {
			return "openMnuPop('" + id + "', '"
					+ EscapeUtil.escapeValue(url) + "', '"
					+ EscapeUtil.escapeValue(status) + "', event);";
		}
	}
	/** 나의 레이아웃 페이지 함수 리턴(Javascript) */
	public static String getMyLoutFnc(String mnuGrpId, String mnuLoutId){
		return "goMyLout(event);";
	}
	
	/** 외부 프레임 URL - 하단(메뉴영역포함) iframe URL 리턴 */
	public static String getOuterFrameUrl(String mnuLoutId, String mnuGrpId){
		return "/pt/lout/viewFrame.do?menuId="+mnuLoutId+"&mnuGrpId="+mnuGrpId;
	}
	
	/** 외부 프레임 함수 리턴(Javascript) - 하단우측(메뉴영역제외) iframe */
	public static String getOuterMnuFnc(String mnuLoutCombId, String mnuId){
		return "openFrameMenu('"+mnuLoutCombId+"','"+mnuId+"');";
	}
	
	/** 외부 프레임 URL - 하단우측(메뉴영역제외) iframe : 권한의 URL 등록시 사용 */
	public static String getOuterMnuUrl(String mnuLoutCombId, String mnuId){
		return "/pt/lout/viewMenu.do?mnuId="+mnuId+"&menuId="+mnuLoutCombId;
	}
	
	/** 페이지 형태 리턴 */
	public static int getPageType(String uri){
		
		if(uri==null || uri.isEmpty() || uri.equals("/")){
			return TYPE_PAGE;
		}
		
		int p = uri.lastIndexOf('/');
		String lastPath = p > 0 ? uri.substring(p) : uri;
		
		if(lastPath.startsWith("/trans")) {
			return TYPE_TRANS;
		} else if(uri.endsWith("Pop.do")) {
			return TYPE_POP;
		} else if(uri.endsWith("Plt.do")) {
			return TYPE_PLT;
		} else if(uri.endsWith("PltFrm.do")) {
			return TYPE_PLT_IFRM;
		} else if(uri.endsWith("Frm.do")) {
			return TYPE_IFRM;
		} else if(uri.endsWith("Ajx.do")) {
			return TYPE_AJAX;
		} else {
			if(		lastPath.startsWith("/set")		||	lastPath.startsWith("/list")
					||	lastPath.startsWith("/view")	||	lastPath.startsWith("/tree")
					||	lastPath.startsWith("/error")	||	lastPath.startsWith("/index")){
				return TYPE_PAGE;
			}
			return TYPE_IFRM;
		}
	}
	
	/** URL에 메뉴ID를 더함 */
	public static String addMenuId(String url, String menuId){
		if(url==null || url.isEmpty()) return url;
		if(url.startsWith("javascript")) return url;
		if(url.indexOf('?')>=0){
			return url +"&menuId="+menuId;
		} else {
			return url +"?menuId="+menuId;
		}
	}
	
	/** 메뉴에 등록된 치환 변수를 변환함 */
	public static String converTypedParam(String url, UserVo userVo){
		if(url != null && userVo != null){
			if(url.indexOf("<userId>")>0){
				url = url.replace("<userId>", "userId="+userVo.getLginId());
			}
		}
		return url;
	}
}
