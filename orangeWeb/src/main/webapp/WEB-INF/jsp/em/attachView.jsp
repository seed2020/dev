<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="module" test="${empty module }" value="${param.module }" elseValue="${module }"/>
<c:set var="moveExts" value="ppt,pptx,pdf"/>
<u:set var="previewUrl" test="${!empty fn && !empty rs }" value="/viewer/skin/doc.html?fn=${fn }&rs=/${rs }" elseValue="/cm/util/reloadable.do"/>
<!-- 확장자 이미지 -->
<c:set var="extSet" value="zip,egg,pptx,ppt,doc,xls,xlsx,pdf,psd,bmp,png,jpg,gif,html,txt,file"/>
<u:set test="${!empty ext}" var="ext" value="${ext}" elseValue="file" />
<u:set test="${extSet.contains(ext)}" var="ext" value="${ext}" elseValue="file" />
<u:set test="${moveExts.contains(ext)}" var="pageMoveYn" value="Y" elseValue="N" />
<script type="text/javascript">
<!--
<% // 파일 다운로드 %>
function downFile() {
	var $form = $('#previewDownForm');
	$form.attr('method','post');
	$form.attr('action','/${module}/preview/downFile.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form[0].submit();
};
<% // 페이지 이동 %>
function pageMove(direction){
	if(getIframeContent("viewerWrap").location.href.indexOf("reloadable.do")<0 && validator.validate('bodyWrap')){
		if(direction=='move'){
			getIframeContent('viewerWrap').localSynap.movePage($('#pageNo').val());
		}else if(direction=='next'){
			getIframeContent('viewerWrap').localSynap.moveNext();
		}else if(direction=='prev'){
			getIframeContent('viewerWrap').localSynap.movePrev();
		}
	}
};

<% // 컨테이너 초기화 %>
function initContainer(id){
	if(id==null || id==undefined) return;
	var $container = $('#'+id+'Container');
	$container.show();
};
<% // 전체 페이지 조회 %>
function initPageTotalCnt(){
	<c:if test="${pageMoveYn ne 'Y' }">return;</c:if>
	if(getIframeContent("viewerWrap").location.href.indexOf("reloadable.do")<0){
		var pageSize = getIframeContent('viewerWrap').localSynap.getPageSize();
		$('#bodyWrap #pageTotalCnt').text(pageSize);
	}
};
<% // 복사방지 %>
function noCopy(){
	// 복사방지
	if(getIframeContent("viewerWrap").location.href.indexOf("reloadable.do")<0)	
    	getIframeContent('viewerWrap').localSynap.properties.allowCopy = false;
}
<% // 프레임 리사이즈 %>
var resize = function(e){
    var e = e || window.event;
    var bodyHeight = (window.innerHeight ? window.innerHeight : document.documentElement.clientHeight ? document.documentElement.clientHeight : document.body.clientHeight);
    document.getElementById('bodyWrap').style.height = bodyHeight + 'px';
    //document.getElementById('viewerWrap').style.height = (bodyHeight - 37 - 36 - 12) + 'px';
    document.getElementById('viewerWrap').style.height = (bodyHeight - 30) + 'px';
};
window.onresize = resize;

$(document).ready(function() {
	/* <c:if test="${fn:contains(moveExts, ext) }">initContainer('page');</c:if>
	<c:if test="${viewDownYn eq 'Y' }">initContainer('btn');</c:if> */
	setUniformCSS();	
});
//-->
</script>
<div id="bodyWrap" style="margin:0px;padding:0px;overflow:hidden;">
<%-- <div class="layerpoparea" style="width: 100%;">
	<div class="lypoptit">
	<dl>
	<dd class="title"><u:msg titleId="pt.docViewer.title" alt="문서뷰어"/></dd>
	</dl>
	</div>
</div> --%>
<ul style="list-style:none;padding:2px; margin:0;background-color:#134372;;height:27px;color:#ffffff;">
<li style="float:left;padding:8px 0 0 0;background:url('${_cxPth}/images/cm/ico_${ext}.png') no-repeat left 6px;text-indent:20px;"><strong>${dispNm }</strong></li>
<li style="float:right;"><c:if test="${pageMoveYn eq 'Y' }"><span id="pageContainer"
><u:input type="text" id="pageNo" minLength="1" titleId="cm.page.moveTo" value="1" maxByte="5" style="width:50px;text-align:right;" valueOption="number" mandatory="Y"
onkeydown="if (event.keyCode == 13) pageMove('move');" />/<strong id="pageTotalCnt" style="width:30px;">00</strong><u:buttonS href="javascript:pageMove('move');" titleId="cm.btn.move" alt="이동" auth="W"
/></span></c:if><c:if test="${viewDownYn eq 'Y' }"><u:buttonS id="btnContainer" titleId="cm.btn.download" href="javascript:downFile();" auth="W" style="margin:4px 10px 0 0;" 
/></c:if></li>
</ul>
<c:if test="${!empty previewUrl }">
<!-- 미리보기 프레임 -->
<iframe src="${previewUrl }" id="viewerWrap" width="100%" height="100%" marginheight="0" marginwidth="0" frameborder="0" onload="resize(event);<c:if test="${!empty noCopy && noCopy==true}">noCopy();</c:if>">
    <p>Your browser does not support iframes.</p>
</iframe>
</c:if>
</div>
<c:if test="${viewDownYn eq 'Y' }">
<form id="previewDownForm">
<c:if test="${!empty paramEntryList}">
<c:forEach items="${paramEntryList}" var="entry" varStatus="status">
	<u:input type="hidden" id="${entry.key}" value="${entry.value}" />
</c:forEach>
</c:if>
</form>
</c:if>