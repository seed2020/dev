<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="
		java.util.Locale,
		java.util.Map,
		javax.servlet.jsp.tagext.JspFragment,
		com.innobiz.orange.web.pt.secu.LoginSession,
		com.innobiz.orange.web.pt.utils.SysSetupUtil,
		com.innobiz.orange.web.cm.utils.MessageProperties,
		com.innobiz.orange.web.cm.utils.SessionUtil"

%><%@ attribute name="id"    required="false"
%><%@ attribute name="title"    required="false"
%><%@ attribute name="titleId"  required="false"
%><%@ attribute name="titleSuffix"  required="false"
%><%@ attribute name="alt"  required="false"
%><%@ attribute name="type" required="false" rtexprvalue="false"
%><%@ attribute name="menuNameFirst" required="false" type="java.lang.Boolean"
%><%@ attribute name="bottomBlank" required="false" type="java.lang.Boolean"
%><%@ attribute name="notPrint" required="false" type="java.lang.Boolean"
%><%@ attribute name="hideButtons" required="false" type="java.lang.Boolean"
%><%
/*
titleId : message 파일에 등록된 메세지 아이디
type : big/small - 큰타이틀 작은 타이틀
//icons : 위로,아래로  - script - 삭제
buttons : 버튼의 - titileId, script

menuNameFirst : 메뉴에 등록된 메뉴명 우선 적용

alt		: 사용되지 않음 - 참조용으로 한글명 넣어 주세요
*/

String className = "title";
if("small".equals(type)) className = "title_s";

String dispTitle = null;
if(Boolean.TRUE.equals(menuNameFirst)){
	dispTitle = (String)request.getAttribute("UI_TITLE");
}
if(dispTitle==null){
	if(titleId!=null){
		
		int p = titleId.lastIndexOf('.');
		String termVa = null;
		if(p>0){
			if(titleId.indexOf(".term")>0){
				String setupClsId = titleId.substring(0, p);
				String setupId = titleId.substring(p+1);
				String langTypCd = SessionUtil.getLangTypCd(request);
				Map<String, String> termMap = SysSetupUtil.getTermMap(setupClsId, langTypCd);
				termVa = termMap==null ? null : termMap.get(setupId);
			}
			
			if(termVa==null){
				dispTitle = MessageProperties.getInstance().getMessage(titleId, request);
			} else {
				dispTitle = termVa;
			}
		}
		
	} else {
		dispTitle = title;
	}
}

if(titleSuffix!=null && !titleSuffix.isEmpty()){
	dispTitle = dispTitle+" ("+titleSuffix+")";
}

String idAttr = (id==null) ? "" : " id=\""+id+"\"";
String idBtnAttr = (id==null) ? "" : " id=\""+id+"BtnArea\"";
String notPrintClass = Boolean.TRUE.equals(notPrint) ? " notPrint" : "";

%>	<div class="titlearea<%= notPrintClass%>">
	<% if(dispTitle!=null && !dispTitle.isEmpty()){ %><div class="tit_left">
	<dl>
		<dd class="<%= className%>"<%= idAttr%>><%= dispTitle%></dd>
	</dl>
	</div><%}
	
	
	JspFragment jspFragment = getJspBody();
	if(jspFragment!=null){
		
		%>
	<div class="tit_right"<%= idBtnAttr%> style="<%= (Boolean.TRUE.equals(hideButtons) ? "display:none;" : "")%>">
	<ul><%
		
		jspFragment.invoke(out);
		
		String titleButtonType = (String)request.getAttribute("titleSubType");
		if(titleButtonType!=null){
			request.removeAttribute("titleSubType");
			// sub-tag : titleButton, titleIcon, titleText 에서 열어 놓은 li 테그를 닫음
			out.println("\r\n			</li>");
		}
		%>
	</ul>
	</div><%
	}
	
	
	%>
	</div><%
	
	
	if(Boolean.TRUE.equals(bottomBlank)){
		%>
	<div class="blank<%= notPrintClass%>"></div><%
	}
	%>