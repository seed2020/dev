package com.innobiz.orange.mobile.cm.ctrl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.innobiz.orange.web.cm.utils.StringUtil;

/** Manifest 컨트롤러 - 클라이언트 캐쉬를 위한 정보 리턴  - 사용안함 */
//@Controller
public class ManifestCtrl {
	
	private String versionDate = StringUtil.getCurrYmd();
	
	private String versionNo = "1";
	
	private Map<String, String> resourceMap = new HashMap<String, String>();
	
//	@RequestMapping(value = "/cm/manifest/viewLogin.do", method = RequestMethod.GET)
//	public String viewLogin(HttpServletRequest request, HttpServletResponse response,
//			ModelMap model) throws ServletException, IOException{
//		
//		StringBuilder builder = getDefaultBuilder();
//		builder.append("/images/blue/login_check.png\r\n");
//		builder.append("/images/blue/login_check_on.png\r\n");
//		builder.append("/images/blue/search_btn1.png\r\n");
//		String logoPath = resourceMap.get("logoPath");
//		if(logoPath!=null){
//			builder.append(logoPath).append("\r\n");
//		}
//		sendResponse(response, builder);
//		return null;
//	}

	@RequestMapping(value = "/cm/manifest/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws ServletException, IOException{
		
		StringBuilder builder = getDefaultBuilder();
		builder.append("/images/blue/aside_close.png\r\n");
		builder.append("/images/blue/aside_go.png\r\n");
		builder.append("/images/blue/aside_go_on.png\r\n");
		builder.append("/images/blue/aside_name.png\r\n");
		builder.append("/images/blue/aside_team.png\r\n");
		builder.append("/images/blue/icoap.png\r\n");
		builder.append("/images/blue/icobb.png\r\n");
		builder.append("/images/blue/icoct.png\r\n");
		builder.append("/images/blue/icomail.png\r\n");
		builder.append("/images/blue/icowb.png\r\n");
		builder.append("/images/blue/icowc.png\r\n");
		builder.append("/images/blue/icowr.png\r\n");
		builder.append("/images/blue/icowv.png\r\n");
		
		// /css/m.gworange.blue.calendar.css
		builder.append("/images/blue/btn_down.png\r\n");
		builder.append("/images/blue/btn_first_w.png\r\n");
		builder.append("/images/blue/btn_last_w.png\r\n");
		builder.append("/images/blue/btn_next_w.png\r\n");
		builder.append("/images/blue/btn_prev_w.png\r\n");
		builder.append("/images/blue/btn_up.png\r\n");
		
		// /css/m.gworange.blue.popup.css
		builder.append("/images/blue/unified_bg.png\r\n");
		builder.append("/images/blue/unified_delete.png\r\n");
		builder.append("/images/blue/unified_search.png\r\n");
		
		builder.append("/images/blue/noimg.png\r\n");
		sendResponse(response, builder);
		return null;
	}
	
	@RequestMapping(value = "/cm/manifest/layout", method = RequestMethod.GET)
	public String layout(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws ServletException, IOException{
		
		StringBuilder builder = getDefaultBuilder();
		
		// /css/m.gworange.blue.css
		builder.append("/images/blue/header_back.png\r\n");
		builder.append("/images/blue/header_bar.png\r\n");
		builder.append("/images/blue/header_bg.png\r\n");
		builder.append("/images/blue/header_left.png\r\n");
		builder.append("/images/blue/header_menu.png\r\n");
		builder.append("/images/blue/header_reload.png\r\n");
		builder.append("/images/blue/header_right.png\r\n");
		builder.append("/images/blue/header_save.png\r\n");
		builder.append("/images/blue/header_search.png\r\n");
		builder.append("/images/blue/header_view.png\r\n");
		
		// /css/m.gworange.blue.list.css
		builder.append("/images/blue/btn_open.png\r\n");
		builder.append("/images/blue/comment.png\r\n");
		builder.append("/images/blue/ico_delete.png\r\n");
		builder.append("/images/blue/ico_good.png\r\n");
		builder.append("/images/blue/ico_recommend.png\r\n");
		builder.append("/images/blue/ico_phone.png\r\n");
		builder.append("/images/blue/ico_sms.png\r\n");
		builder.append("/images/blue/ico_attach.png\r\n");
		builder.append("/images/blue/ico_down.png\r\n");
		builder.append("/images/blue/ico_reply.png\r\n");
		builder.append("/images/blue/paging_next.png\r\n");
		builder.append("/images/blue/paging_prev.png\r\n");
		builder.append("/images/blue/paging_setup.png\r\n");
		builder.append("/images/blue/search_bg.png\r\n");
		builder.append("/images/blue/search_btn1.png\r\n");
		builder.append("/images/blue/search_btn3.png\r\n");
		builder.append("/images/blue/search_btn4.png\r\n");
		builder.append("/images/blue/search_left.png\r\n");
		
		// /css/m.gworange.blue.entry.css
		builder.append("/images/blue/btn_down_s.png\r\n");
		builder.append("/images/blue/btn_up_s.png\r\n");
		builder.append("/images/blue/calendar_btn.png\r\n");
		builder.append("/images/blue/cdelete.png\r\n");
		builder.append("/images/blue/check.png\r\n");
		builder.append("/images/blue/check_on.png\r\n");
		builder.append("/images/blue/dot_tit.png\r\n");
		builder.append("/images/blue/dot_tit2.png\r\n");
		builder.append("/images/blue/dot_tit_asterisk.png\r\n");
		builder.append("/images/blue/dot_tits.png\r\n");
		builder.append("/images/blue/ipdelete.png\r\n");
		builder.append("/images/blue/radio.png\r\n");
		builder.append("/images/blue/radio_on.png\r\n");
		
		// /css/m.gworange.blue.schedule.css
		builder.append("/images/blue/btn_prev.png\r\n");
		builder.append("/images/blue/btn_next.png\r\n");
		builder.append("/images/blue/dot_o.png\r\n");
		builder.append("/images/blue/dot_t.png\r\n");
		builder.append("/images/blue/dot_tbg.png\r\n");
		builder.append("/images/blue/dot_tits_c.png\r\n");
		
		// tree
		builder.append("/images/blue/unified_left.png\r\n");
		builder.append("/images/blue/mtree_org.png\r\n");
		builder.append("/images/blue/mtree_open.png\r\n");
		builder.append("/images/blue/mtree_close.png\r\n");
		builder.append("/images/blue/mtree_folder_close.png\r\n");
		builder.append("/images/blue/mtree_company.png\r\n");
		builder.append("/images/blue/mtree_organization.png\r\n");
		builder.append("/images/blue/mtree_department.png\r\n");
		builder.append("/images/blue/mtree_part.png\r\n");
		
		builder.append("/images/blue/noimg.png\r\n");
		builder.append("/images/blue/noimg2.png\r\n");
		builder.append("/images/blue/photo_noimg.png\r\n");
		builder.append("/images/blue/tab_left.png\r\n");
		builder.append("/images/blue/tab_right.png\r\n");
		sendResponse(response, builder);
		return null;
	}
	
	@RequestMapping(value = "/cm/manifest/editor", method = RequestMethod.GET)
	public String viewLogin(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws ServletException, IOException{
		
		StringBuilder builder = getDefaultBuilder();
		builder.append("/images/editor/size.png\r\n");
		builder.append("/images/editor/font.png\r\n");
		builder.append("/images/editor/align.png\r\n");
		builder.append("/images/editor/color.png\r\n");
		
		builder.append("/images/editor/italic.png\r\n");
		builder.append("/images/editor/under.png\r\n");
		builder.append("/images/editor/bold.png\r\n");
		
		builder.append("/images/editor/left.png\r\n");
		builder.append("/images/editor/center.png\r\n");
		builder.append("/images/editor/right.png\r\n");
		
		builder.append("/images/editor/save.png\r\n");
		builder.append("/images/editor/cancel.png\r\n");
		sendResponse(response, builder);
		return null;
	}
	
	public void setResource(String key, String resource){
		resourceMap.put(key, resource);
	}
	
	private StringBuilder getDefaultBuilder(){
		StringBuilder builder = new StringBuilder(1024*4);
		builder.append("CACHE MANIFEST\r\n");
		builder.append("# ").append(versionDate).append(":v").append(versionNo).append("\r\n\r\n");
		builder.append("CACHE:\r\n");
		return builder;
	}
	
	private void sendResponse(HttpServletResponse response, StringBuilder builder) throws IOException{
		response.setContentType("mime-type text/cache-manifest");
		PrintWriter out = response.getWriter();
		builder.append("\r\nNETWORK:\r\n*\r\n");
		//builder.append("FALLBACK:\r\n");
		out.write(builder.toString());
	}
/*

CACHE MANIFEST
# 2010-06-18:v2

# Explicitly cached 'master entries'.
CACHE:
/favicon.ico
index.html
stylesheet.css
images/logo.png
scripts/main.js

# Resources that require the user to be online.
NETWORK:
*

# static.html will be served if main.py is inaccessible
# offline.jpg will be served in place of all images in images/large/
# offline.html will be served in place of all other .html files
FALLBACK:
/main.py /static.html
images/large/ images/offline.jpg

*/
}
