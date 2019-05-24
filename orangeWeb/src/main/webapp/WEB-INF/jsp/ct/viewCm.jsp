<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
function view(url) {
	location.href = url;
}

$(document).ready(function() {
	
	$(".wbox").attr("class", "");
	setUniformCSS();
});
//-->
</script>

<c:set var="title" value="${ctEstbBVo.ctNm}" />

<u:title title="${title}" alt="커뮤니티명" menuNameFirst="true" />

<style>
.itrobox { float: left; width: 99%; background: #ebf1f6; border: 1px solid #bfc8d2; padding: 5px 7px 3px 10px; }
.itrobox_tit { float: left; width: 48%; background:url("/images/blue/dot_search.png") no-repeat 0 6px; padding:3px 2px 5px 9px; }
.itrobox_body { width: 99%; line-height:18px; padding:5px 0 3px 0; }
</style>

<u:blank />

<div class="titlearea">
	<div class="tit_left">
	<dl>
		<dd class="title_s"><u:msg titleId="ct.jsp.main.title01" alt="커뮤니티 소개" /></dd>
	</dl>
	</div>
</div>

<c:if test="${tpl['1'] != null}">
<div class="itrobox" style="float: left; width: <c:if test="${tpl['2'] != null}">69%</c:if><c:if test="${tpl['2'] == null}">99%</c:if>; height: 198px;">
	<div style="height: 198px;overflow:auto;">
	<dl>
		<dd class="itrobox_body">${ctEstbBVo.ctItro}</dd>
		<%-- <c:if test="${ctFncMngPtList[0].ctItro != null}">
			<dd class="itrobox_body">${ctFncMngPtList[0].ctItro}</dd>
		</c:if>
		<c:if test="${ctFncMngPtList[0].ctItro == null}">
			<dd class="itrobox_body">${ctEstbBVo.ctItro}</dd>
		</c:if> --%>
	</dl>
	</div>
</div>
</c:if>

<c:if test="${tpl['2'] != null}">
<div  style="float: right; width: <c:if test="${tpl['1'] != null}">29%</c:if><c:if test="${tpl['1'] == null}">99%</c:if>; height: 208px;">
	
		<c:if test="${imgFilePath != null}">
			<img src="${imgFilePath}"   style="height: 100%;  width:100%"/>
		</c:if>

</div>
</c:if>
<u:blank />

<c:set var="idx"	value= "0" />
<c:if test="${tpl['34'] != null}">
<% // 영역1 %>
<div style="float: left; width: 49%; height: 198px;">

	<div class="titlearea">
		<div class="tit_left">
		<dl>
		<dd class="title_s">${ctFncMngPtList[idx].ctFncNm}</dd>
		</dl>
		</div>
	</div>
	
	<u:titleArea frameId="baordPt1" frameSrc="${ctFncMngPtList[idx].ptUrl}${ctFncMngPtList[idx].ctFncUid}&ctId=${ctId}"
		 	outerStyle="height: 170px;  overflow:hidden;" 
		 	innerStyle="padding:0 0px 0 0px;"
			frameStyle="width:100%; height:200px; overflow:hidden;" />
		<c:set var="idx"	value="${idx+1}" />
</div>

<% // 영역2 %>
<div style="float: right; width: 49%; height: 198px;">

	<div class="titlearea">
		<div class="tit_left">
		<dl>
		<dd class="title_s">${ctFncMngPtList[idx].ctFncNm}</dd>
		</dl>
		</div>
	</div>

	<u:titleArea frameId="baordPt2" frameSrc="${ctFncMngPtList[idx].ptUrl}${ctFncMngPtList[idx].ctFncUid}&ctId=${ctId}"
		 	outerStyle="height: 170px;  overflow:hidden;" 
		 	innerStyle="padding:0 0px 0 0px;"
			frameStyle="width:100%; height:200px; overflow:hidden;" />
			<c:set var="idx"	value="${idx+1}" />
</div>

</c:if>

<c:if test="${tpl['5'] != null}">
<% // 영역3 %>
<div style="float: left; width: 100%; height: 198px;">
	
	<div class="titlearea">
		<div class="tit_left">
		<dl>
		<dd class="title_s">${ctFncMngPtList[idx].ctFncNm}</dd>
		</dl>
		</div>
	</div>
	
	<u:titleArea frameId="baordPt3" frameSrc="${ctFncMngPtList[idx].ptUrl}${ctFncMngPtList[idx].ctFncUid}&ctId=${ctId}"
		 	outerStyle="height: 170px;  overflow:hidden;" 
		 	innerStyle="padding:0 0px 0 0px;"
			frameStyle="width:100%; height:200px; overflow:hidden;" />
	<c:set var="idx"	value="${idx+1}" />
</div>
</c:if>

<c:if test="${tpl['67'] != null}">
<% // 영역4 %>
<div style="float: left; width: 49%; height: 198px;">

	<div class="titlearea">
		<div class="tit_left">
		<dl>
		<dd class="title_s">${ctFncMngPtList[idx].ctFncNm}</dd>
		</dl>
		</div>
	</div>
	
	<u:titleArea frameId="baordPt4" frameSrc="${ctFncMngPtList[idx].ptUrl}${ctFncMngPtList[idx].ctFncUid}&ctId=${ctId}"
		 	outerStyle="height: 170px;  overflow:hidden;" 
		 	innerStyle="padding:0 0px 0 0px;"
			frameStyle="width:100%; height:200px; overflow:hidden;" />
	<c:set var="idx"	value="${idx+1}" />

</div>

<% // 영역5 %>
<div style="float: right; width: 49%; height: 198px;">

	<div class="titlearea">
		<div class="tit_left">
		<dl>
		<dd class="title_s">${ctFncMngPtList[idx].ctFncNm}</dd>
		</dl>
		</div>
	</div>
	
	<u:titleArea frameId="baordPt4" frameSrc="${ctFncMngPtList[idx].ptUrl}${ctFncMngPtList[idx].ctFncUid}&ctId=${ctId}"
		 	outerStyle="height: 170px;  overflow:hidden;" 
		 	innerStyle="padding:0 0px 0 0px;"
			frameStyle="width:100%; height:200px; overflow:hidden;" />
	<c:set var="idx"	value="${idx+1}" />
</div>

</c:if>

<c:if test="${tpl['8'] != null}">
<% // 영역6 %>
<div style="float: left; width: 100%; height: 198px;">

	<div class="titlearea">
		<div class="tit_left">
		<dl>
		<dd class="title_s">${ctFncMngPtList[idx].ctFncNm}</dd>
		</dl>
		</div>
	</div>
	
	<u:titleArea frameId="baordPt4" frameSrc="${ctFncMngPtList[idx].ptUrl}${ctFncMngPtList[idx].ctFncUid}&ctId=${ctId}"
		 	outerStyle="height: 170px;  overflow:hidden;" 
		 	innerStyle="padding:0 0px 0 0px;"
			frameStyle="width:100%; height:200px; overflow:hidden;" />

	<c:set var="idx"	value="${idx+1}" />
</div>
</c:if>

<c:if test="${tpl['910'] != null}">

<% // 영역7 %>
<div style="float: left; width: 49%; height: 198px;">

	<div class="titlearea">
		<div class="tit_left">
		<dl>
		<dd class="title_s">${ctFncMngPtList[idx].ctFncNm}</dd>
		</dl>
		</div>
	</div>
	
	<u:titleArea frameId="baordPt4" frameSrc="${ctFncMngPtList[idx].ptUrl}${ctFncMngPtList[idx].ctFncUid}&ctId=${ctId}"
		 	outerStyle="height: 170px;  overflow:hidden;" 
		 	innerStyle="padding:0 0px 0 0px;"
			frameStyle="width:100%; height:200px; overflow:hidden;" />

	<c:set var="idx"	value="${idx+1}" />
</div>

<% // 영역8 %>
<div style="float: right; width: 49%; height: 198px;">

	<div class="titlearea">
		<div class="tit_left">
		<dl>
		<dd class="title_s">${ctFncMngPtList[idx].ctFncNm}</dd>
		</dl>
		</div>
	</div>
	
	<u:titleArea frameId="baordPt4" frameSrc="${ctFncMngPtList[idx].ptUrl}${ctFncMngPtList[idx].ctFncUid}&ctId=${ctId}"
		 	outerStyle="height: 170px;  overflow:hidden;" 
		 	innerStyle="padding:0 0px 0 0px;"
			frameStyle="width:100%; height:200px; overflow:hidden;" />

	<c:set var="idx"	value="${idx+1}" />
</div>
</c:if>

<c:if test="${tpl['11'] != null}">

<% // 영역7 %>
<div style="float: left; width: 100%; height: 198px;">

	<div class="titlearea">
		<div class="tit_left">
		<dl>
		<dd class="title_s">${ctFncMngPtList[idx].ctFncNm}</dd>
		</dl>
		</div>
	</div>
	
	<u:titleArea frameId="baordPt4" frameSrc="${ctFncMngPtList[idx].ptUrl}${ctFncMngPtList[idx].ctFncUid}&ctId=${ctId}"
		 	outerStyle="height: 170px;  overflow:hidden;" 
		 	innerStyle="padding:0 0px 0 0px;"
			frameStyle="width:100%; height:200px; overflow:hidden;" />

	<c:set var="idx"	value="${idx+1}" />
</div>
</c:if>