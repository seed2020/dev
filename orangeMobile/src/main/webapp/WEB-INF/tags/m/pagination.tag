<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="
		java.util.ArrayList,
		java.util.List,
		com.innobiz.orange.web.pt.utils.PersonalUtil,
		com.innobiz.orange.web.cm.utils.ArrayUtil,
		com.innobiz.orange.web.cm.utils.EscapeUtil"

%><%@ taglib prefix="m" tagdir="/WEB-INF/tags/m"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"
%><%@ attribute name="formId" required="false"
%><%@ attribute name="javascriptFunction" required="false"
%><%@ attribute name="noJavascript" required="false" type="java.lang.Boolean"
%><%@ attribute name="pageNo" required="false" type="java.lang.Integer"
%><%@ attribute name="pageRowCnt" required="false" type="java.lang.Integer"
%><%@ attribute name="recodeCount" required="false" type="java.lang.Integer"
%><%@ attribute name="excludedParams" required="false"
%><%

	// configuration
	// <<,>> : first-page, last-page
	// <,> : first-page of previous block, first-page of next block

	// javascript function
	if(javascriptFunction==null || javascriptFunction.isEmpty()) javascriptFunction = "goPage";
	
	// form id
	if(formId==null || formId.isEmpty()) formId = "paginationForm";
	
	// parameter init
	String no;
	if(pageNo==null){
		no = request.getParameter("pageNo");
		if(no==null || no.isEmpty()) pageNo = 1;
		else {
			try{ pageNo = Integer.parseInt(no); }
			catch(Exception ignore){}
		}
	}
	if(pageRowCnt==null){
		pageRowCnt = PersonalUtil.getPageRowCnt(request);
	}
	request.setAttribute("pageRowCnt", pageRowCnt);
	
	if(recodeCount==null){
		recodeCount = (Integer)request.getAttribute("recodeCount");
	}
	if(recodeCount==null) recodeCount = 0;
	
	// 페이지 수
	int pageCount = (int)Math.ceil((double)recodeCount / pageRowCnt);
	
	if(pageNo<0) pageNo = 1;
	if(pageNo>pageCount) pageNo = pageCount;
	
	// 블럭 카운트
	Integer nBlock = 5;
	int blockCount = (int)Math.ceil((double)pageCount / nBlock);
	int blockNo = (int)Math.ceil((double)pageNo / nBlock);
	
	String[] naviScript = new String[4];
	/*
	if(pageNo==1){
		naviScript[0] = "void(0);";
	} else {
		naviScript[0] = javascriptFunction+"(1);";
	}*/
	if(blockNo>1){
		naviScript[1] = javascriptFunction+"("+((blockNo-2) * nBlock + 1)+");";
	} else {
		naviScript[1] = "void(0);";
	}
	
	if(blockNo<blockCount){
		naviScript[2] = javascriptFunction+"("+(blockNo * nBlock + 1)+");";
	} else {
		naviScript[2] = "void(0);";
	}/*
	if(pageNo==pageCount){
		naviScript[3] = "void(0);";
	} else {
		naviScript[3] = javascriptFunction+"("+pageCount+");";
	}*/
	
	List<String> excludedParamList = ArrayUtil.toList(excludedParams, ",", false);
	excludedParamList.add("pageNo");
	excludedParamList.add("noCache");
	
	if(recodeCount > 0){
		if(!Boolean.TRUE.equals(noJavascript)){
%>
<script type="text/javascript">
//<![CDATA[
function goPage(pageNo){
	var form = $('#<%=formId%>');
	form.find("[name='pageNo']").val(pageNo);
	$m.nav.curr(null, form.attr('action')+'?'+form.serialize());
}
function adjustPaging(){
	var area = $('#pagingarea');
	if(area.length==0) return;
	var setup = area.find('#setup');
	setup.show();
	if(area.height() > 26){
		area.find('#setup').hide();
	}
}
$(document).ready(function(){
	adjustPaging();
});
//]]>
</script><%
		}//if(!Boolean.TRUE.equals(noJavascript)){
%>
	<form method="get" action="${_uri}" id="<%=formId%>" name="<%=formId%>">
		<input type="hidden" name="pageNo" value="<%=pageNo%>" /><%
		
		java.util.Enumeration reqParams = request.getParameterNames();
		while (reqParams.hasMoreElements()) {
			String key = (String)reqParams.nextElement();
			if(excludedParamList.contains(key)) continue;
			String value = (String)request.getParameter(key);
			
		%>
		<input type="hidden" name="<%= key%>" value="<%= EscapeUtil.escapeValue(value)%>" /><%
		}
		%>
	</form>
	<div class="pagingarea" id="pagingarea"><div class="paging">
		<dl>
			<dd class="setup" id="setup" onclick="$m.nav.next(event, '<u:authUrl url="/pt/psn/setPageRecCnt.do" />');"></dd>
			<dd class="prev" onclick="<%= naviScript[1]%>"></dd><%
				
			Integer currPage, startPage = (blockNo-1) * nBlock + 1;
			for(int i=0;i<nBlock;i++){
				
				currPage = startPage + i;
				if(currPage<=pageCount){
					if(pageNo==currPage){
						%>
			<dd class="number_on"><%= currPage%></dd><%
					} else {
						%>
			<dd class="number" onclick="<%= javascriptFunction%>(<%= currPage%>)"><%= currPage%></dd><%
			
					}
				}
			}
			%>
			<dd class="next" onclick="<%= naviScript[2]%>"></dd>
		</dl>
	</div></div><%
	}
%>