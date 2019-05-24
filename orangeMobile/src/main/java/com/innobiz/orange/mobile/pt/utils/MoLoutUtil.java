package com.innobiz.orange.mobile.pt.utils;

import com.innobiz.orange.web.cm.utils.EscapeUtil;

public class MoLoutUtil {

	/** 페이지 타입 : 인덱스 */
	public static final int TYPE_IDX = 7;
	/** 페이지 타입 : 매니페스트 */
	public static final int TYPE_MANIFEST = 8;
	
	/** 페이지 타입 : 팝업 */
	public static final int TYPE_POP = 1;
	/** 페이지 타입 : iframe */
	public static final int TYPE_IFRM = 2;
	/** 페이지 타입 : 포틀릿 */
	public static final int TYPE_PLT = 3;
	/** 페이지 타입 : ajax */
	public static final int TYPE_AJAX = 5;
	/** 페이지 타입 : trans */
	public static final int TYPE_TRANS = 6;
	/** 페이지 타입 : 페이지(상단,좌측메뉴가 붙는 형태) */
	public static final int TYPE_PAGE = 9;
	/** 프로세스 */
	public static final int TYPE_PROC = 11;
	/** 윈도우팝업 */
	public static final int TYPE_WIN_POP = 12;
	/** SSO 콜백 */
	public static final int TYPE_CALLBACK = 13;
	
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
		
		if(uri==null || uri.isEmpty() || uri.equals("/") || uri.equals("/index.do")){
			return TYPE_IDX;
		}
		if(uri.startsWith("/manifest")){
			return TYPE_MANIFEST;
		}
		
		int p = uri.lastIndexOf('/');
		String lastPath = p > 0 ? uri.substring(p) : uri;
		
		if(lastPath.startsWith("/trans")) {
			return TYPE_TRANS;
		} else if(lastPath.startsWith("/process")) {
			return TYPE_PROC;
		} else if(uri.endsWith("WinPop.do")) {
			return TYPE_WIN_POP;
		} else if(uri.endsWith("Pop.do")) {
			return TYPE_POP;
		} else if(uri.endsWith("Plt.do")) {
			return TYPE_PLT;
		} else if(uri.endsWith("Frm.do")) {
			return TYPE_IFRM;
		} else if(uri.endsWith("Ajx.do")) {
			return TYPE_AJAX;
		} else if(uri.startsWith("/callback.do")) {
			return TYPE_CALLBACK;
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
		if(url.indexOf('?')>=0){
			return url +"&menuId="+menuId;
		} else {
			return url +"?menuId="+menuId;
		}
	}

}
