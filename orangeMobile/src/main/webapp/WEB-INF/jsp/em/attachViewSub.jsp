<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="module" test="${empty module }" value="${param.module }" elseValue="${module }"/>
<c:set var="moveExts" value="ppt,pptx,pdf"/>
<u:set var="previewUrl" test="${!empty fn && !empty rs }" value="/viewer/skin/doc.html?fn=${fn }&rs=/${rs }" elseValue="/cm/util/reloadable.do"/>
<!-- 확장자 이미지 -->
<c:set var="extSet" value="zip,egg,pptx,ppt,doc,xls,xlsx,pdf,psd,bmp,png,jpg,gif,html,txt,file"/>
<u:set test="${extSet.contains(ext)}" var="ext" value="${ext}" elseValue="file" />
<u:set test="${moveExts.contains(ext)}" var="pageMoveYn" value="Y" elseValue="N" />
<script type="text/javascript">
//<![CDATA[
<c:if test="${!empty viewDownYn && viewDownYn eq 'Y'}">
<% // 파일 다운로드 %>
function downFile(dispNm) {
	var $form = $('<form>', {
		'method':'get',
		'action':'/${module}/preview/downFile.do',
		'target':$m.browser.mobile && $m.browser.safari ? '' : 'dataframe'
	});
	<c:if test="${!empty paramEntryList}">
	<c:forEach items="${paramEntryList}" var="entry" varStatus="status">
		$form.append($('<input>', {
			'name':'${entry.key}',
			'value':'${entry.value}',
			'type':'hidden'
		}));
	</c:forEach>
	</c:if>

if($m.browser.naver || $m.browser.daum){
	$form.append($('<input>', {'name':'fwd','value':$form.attr('action'),'type':'hidden'}));
	$form.attr('action', '/cm/download/${module}/'+encodeURI(dispNm));
}

$(top.document.body).append($form);
$m.secu.set();
$form.submit();
$form.remove();
};
</c:if>
var pageTotalList=null;
<% // 페이지 이동 %>
function pageMove(direction){
	if(getIframeContent("viewerWrap").location.href.indexOf("reloadable.do")<0){
		if(direction=='move'){
			if(pageTotalList==null){
				return;
			}
			var $container = $("#bodyWrap");
			var selected = $container.find('#pageNo').text();
			$m.dialog.openSelect({id:'selectBumkPop', cdList:pageTotalList, selected:selected}, function(selObj){
				if(selObj!=null){
					$container.find("#pageNo").text(selObj.cd);
					getIframeContent('viewerWrap').localSynap.movePage(selObj.cd);
				}
			});
		}else if(direction=='next'){
			getIframeContent('viewerWrap').localSynap.moveNext();
		}else if(direction=='prev'){
			getIframeContent('viewerWrap').localSynap.movePrev();
		}
	}
};
<% // 전체 페이지 조회 %>
function initPageTotalCnt(){
	<c:if test="${pageMoveYn ne 'Y' }">return;</c:if>
	if(getIframeContent("viewerWrap").location.href.indexOf("reloadable.do")<0){
		var pageSize = getIframeContent('viewerWrap').localSynap.getPageSize();
		$('#bodyWrap #pageTotalCnt').text(pageSize);
		// 전체 페이지 초기화
		if(pageTotalList==null){
			pageTotalList = [];
			for(i=1;i<=pageSize;i++){
				pageTotalList.push({cd:i, nm:i});
			}
		}
	}
};
<% // 복사방지 %>
function noCopy(){
	// 복사방지
	if(getIframeContent("viewerWrap").location.href.indexOf("reloadable.do")<0)	
    	getIframeContent('viewerWrap').localSynap.properties.allowCopy = false;
}
<% // 프레임 리사이즈 %>
function resize(e){
    var e = e || window.event;
    var bodyHeight = (window.innerHeight ? window.innerHeight : document.documentElement.clientHeight ? document.documentElement.clientHeight : document.body.clientHeight);
    var marginTop = 56;
    $("#bodyWrap").css("height", (bodyHeight - marginTop) + 'px');
    //document.getElementById('bodyWrap').style.height = (bodyHeight - marginTop) + 'px';    
    <c:if test="${pageMoveYn eq 'Y' || (!empty viewDownYn && viewDownYn eq 'Y')}">
    marginTop+=36;
    </c:if>
    $("#viewerWrap").css("height", (bodyHeight - marginTop) + 'px');
    //document.getElementById('viewerWrap').style.height = (bodyHeight - marginTop) + 'px';
};
//]]>
</script>           
<section id="bodyWrap">
<c:if test="${pageMoveYn eq 'Y' || (!empty viewDownYn && viewDownYn eq 'Y')}">
<div class="entryzone">
	<div class="blank5"></div>
    <div class="entryarea">
        <dl>
		<dd class="etr_input">
            <div class="etr_ipmany">
            <dl>
            <c:if test="${pageMoveYn eq 'Y' }">
            <dd class="wblank5"></dd>
            <dd class="etr_body"><strong id="pageNo">1</strong> / <span id="pageTotalCnt">0</span></dd>
            <dd class="etr_btn" onclick="pageMove('move')"><u:msg titleId="cm.btn.move" alt="이동"/></dd>
            </c:if>
            <c:if test="${!empty viewDownYn && viewDownYn eq 'Y'}">
            <dd class="wblank10"></dd>
            <dd class="etr_btn" style="float:right;" onclick="downFile('${dispNm}');"><u:msg titleId="cm.btn.download" alt="다운로드"/></dd>
            </c:if>
            </dl>
            </div>
        </dd>
		</dl>
	</div>
</div>
</c:if>
<%-- <div class="attachzone">
<div class="blank5"></div>
<div class="attacharea">
<div class="attachin">
<div class="attach" <c:if test="${!empty viewDownYn && viewDownYn eq 'Y'}">onclick="downFile('${dispNm}');"</c:if>>
	<div class="btn"></div>
	<div class="txt"><u:out value="${dispNm}" /></div>	
</div>
<c:if test="${!empty viewDownYn && viewDownYn eq 'Y'}"><div class="down" onclick="downFile('${dispNm}');"></div></c:if>
</div>
</div>
</div> --%>
                
<div class="blank5"></div>
<c:if test="${!empty previewUrl }">
<!-- 미리보기 프레임 -->
<iframe src="${previewUrl }" id="viewerWrap" width="100%" height="100%" marginheight="0" marginwidth="0" frameborder="0" onload="resize(event);<c:if test="${!empty noCopy && noCopy==true}">noCopy();</c:if>">
    <p>Your browser does not support iframes.</p>
</iframe>
</c:if>
</section>
<c:if test="${!empty viewDownYn && viewDownYn eq 'Y'}">
<form id="previewDownForm">
<c:if test="${!empty paramEntryList}">
<c:forEach items="${paramEntryList}" var="entry" varStatus="status">
	<m:input type="hidden" id="${entry.key}" value="${entry.value}" />
</c:forEach>
</c:if>
</form>
</c:if>


