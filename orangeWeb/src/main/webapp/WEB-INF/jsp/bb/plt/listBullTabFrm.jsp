<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<!-- 화면에 보여질 목록정보 -->
<c:set var="colSpan" value="3"/><!-- line css 를 주기 위한 colspan -->
<c:set var="headList" value="cols.subj,cols.regDt,cols.readCnt"/><!-- 컬럼명(컬럼명을 보여줄경우) -->
<u:authUrl var="viewUrl" url="/bb/${viewPage}.do" authCheckUrl="/bb/${listPage }.do?brdId=${param.brdId }"/><!-- view page 호출관련 url 조합(menuId추가) -->
<u:authUrl var="loginPopUrl" url="/bb/setLoginPop.do" authCheckUrl="/bb/${listPage }.do?brdId=${param.brdId }"/><!-- view page 호출관련 url 조합(menuId추가) -->

<script type="text/javascript">
<!--
<% // [목록:제목] 게시물 조회 %>
function viewBull(bullId,brdId) {
	var authUrl = '${viewUrl}';
	var prefix = authUrl.indexOf('?') > -1 ? "&" : "?";
	authUrl+=prefix+"bullId="+bullId;
	if(brdId != null) authUrl+="&brdId="+brdId;
	top.location.href=authUrl;
};

<% // [목록:제목] 보안글 조회를 위한 로그인폼 화면 %>
function openLogin(bullId,brdId) {
	//dialog.open('setLoginPop','<u:msg titleId="bb.jsp.setLoginPop.title" alt="보안글 인증" />','./setLoginPop.do?${params}&viewPage=${viewPage}&bullId=' + id);
	var authUrl = '${loginPopUrl}';
	var prefix = authUrl.indexOf('?') > -1 ? "&" : "?";
	authUrl+=prefix+"bullId="+bullId;
	if(brdId != null) authUrl+="&brdId="+brdId;
	authUrl+='&viewPage=${viewPage}';
	authUrl+='&pltYn=Y';
	top.dialog.open('setLoginPop','<u:msg titleId="bb.jsp.setLoginPop.title" alt="보안글 인증" />',authUrl);
};

$(document).ready(function() {
	top.dialog.close('setLoginPop');
	<%// 유니폼 적용 %>
	//setUniformCSS();
});


<%
//자동숨김 - 1
String tdWidths = "65,25,10";
request.setAttribute("tdWidths", tdWidths);
request.setAttribute("tdWidthArr", tdWidths.split(","));
%>
$(document).ready(function() {
	resizeForPltTable('ptltable', 190, '${tdWidths}');
	<%// 유니폼 적용 %>
	setUniformCSS();
});

<%
//자동숨김 - 3
//포틀릿 테이블 - 테이블ID %>
var gPltTableId = null;<%
//포틀릿 테이블 - 첫째 컬럼 최소 가로 길이(타입:number, 단위:px) %>
var gPltTableMinFirst = null;<%
//포틀릿 테이블 - 각 컬럼 비율 (예: "40,20,20,20") %>
var gPltTableRatio = null;<%
//포틀릿 테이블 - 가로폭에 따른 컬럼 숨기기, 보이기 %>
function resizeForPltTable(tableClass, minFirst, ratio){
	gPltTableId = tableClass;
	gPltTableMinFirst = minFirst;
	gPltTableRatio = [];
	ratio.split(',').each(function(index, va){
		gPltTableRatio.push(parseInt(va, 10));
	});
	onResizeForPltTable();
	$(window).resize(onResizeForPltTable);
}<%
//포틀릿 테이블 - 가로폭에 따른 컬럼 숨기기, 보이기 - 실제 함수 %>
function onResizeForPltTable(){
	if(gPltTableId==null) return;
	var $table = $("#"+gPltTableId), tableWidth = $table.width(), showIndex=0, ratioSum=0;
	while(true){
		ratioSum += gPltTableRatio[showIndex];
		if(parseInt((gPltTableRatio[0]/ratioSum) * tableWidth) < gPltTableMinFirst){
			break;
		}
		if(showIndex==gPltTableRatio.length-1) break;
		showIndex++;
	}
	var $colgroup = $table.find("colgroup");
	var i, colCnt = $colgroup.find("col").length, percent, percentSum=0;
	for(i=0;i<colCnt;i++){
		if(i==showIndex){
			percent = 100 - percentSum;
			$colgroup.find("col:eq("+i+")").attr("width", percent+"%");
		} else if(i<showIndex){
			percent = parseInt((gPltTableRatio[i]/ratioSum)*100 ,10);
			percentSum += percent;
			$colgroup.find("col:eq("+i+")").attr("width", percent+"%");
		} else {
			$colgroup.find("col:eq("+i+")").attr("width", "0%").hide();
		}
	}
	var $trs = $table.find("tbody tr[id!='lineTr']");
	for(i=0;i<colCnt;i++){
		if(i<=showIndex){
			$trs.find("td:eq("+i+")").show();
		} else {
			$trs.find("td:eq("+i+")").hide();
		}
	}
}
//-->
</script>
<% // 목록 %>
<table class="ptltable" id="ptltable" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed;"><%
	// 자동숨김 - 5 - colgroup %>
	<colgroup><c:forEach items="${tdWidthArr}" var="tdWidth">
		<col width="${tdWidth}%" /></c:forEach>
	</colgroup>
	
	<tbody>
		<tr>
		  <td colspan="${colSpan }" class="line"></td>
		</tr>
	<c:if test="${param.colYn eq 'Y'}">
		<tr id="headerTr">
			<c:forTokens	items="${headList}" var="colId" varStatus="colStatus" delims=",">
				<td class="head_ct" id="head_${fn:split(colId,'.')[1] }" style="display:none;"><div class="ellipsis" title="<u:msg titleId="${colId}" alt="이름" />"><u:msg titleId="${colId}" alt="이름" /></div></td>
			</c:forTokens>
		</tr>
		<tr>
		  <td colspan="${colSpan }" class="line"></td>
		</tr>
	</c:if>
	<c:choose>
		<c:when test="${!empty rsltMapList}">
			<c:forEach var="mapList" items="${rsltMapList}" varStatus="status">
				<c:set var="mapList" value="${mapList}" scope="request" />
				<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
					<td class="body_lt">
						<u:set test="${mapList.readYn == 'Y'}" var="style" value="font-weight: normal;" elseValue="font-weight: bold;" />
						<u:set test="${mapList.ugntYn == 'Y'}" var="style" value="${style} color:red;" elseValue="${style}" />
						<u:set test="${mapList.secuYn == 'Y'}" var="viewBull" value="openLogin" elseValue="viewBull" />
						<div class="ellipsis" title="<u:convertMap srcId="mapList" attId="subj" type="html" />">
							<u:icon type="indent" display="${mapList.replyDpth > 0}" repeat="${mapList.replyDpth - 1}" />
							<u:icon type="reply" display="${mapList.replyDpth > 0}" />
							<u:icon type="new" display="${mapList.newYn == 'Y'}" />
							<a href="javascript:${viewBull}('${mapList.bullId}','${mapList.brdId }');" title="${mapList.subj}" style="${style}">
							<c:if test="${mapList.ugntYn == 'Y'}"><span style="${style}">[<u:msg titleId="bb.option.ugnt" alt="긴급" />]</span></c:if>
							<c:if test="${mapList.secuYn == 'Y'}"><span style="${style}">[<u:msg titleId="bb.option.secu" alt="보안" />]</span></c:if>
							<u:convertMap srcId="mapList" attId="subj" type="html" />
							<c:if test="${baBrdBVo.cmtYn == 'Y'}"><span style="font-size: 10px;">(<u:out value="${mapList.cmtCnt}" type="number" />)</span></c:if>
							</a>
						</div>
					</td>
					<td class="body_ct">
						<div class="ellipsis" title="<u:out value="${mapList.regDt}" type="longdate" />"><u:out value="${mapList.regDt}" type="longdate" /></div>
					</td>
					<td class="body_ct">
						<div class="ellipsis" title="<u:out value="${mapList.readCnt}" type="number" />"><u:out value="${mapList.readCnt}" type="number" /></div>
					</td>
				</tr>
				<tr id="headerTr">
				  <td colspan="${colSpan }" class="line"></td>
				</tr>
			</c:forEach>
			</c:when>
		<c:otherwise>
			<tr>
				<td class="nodata" colspan="${colSpan }"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
			<tr id="lineTr"><td colspan="${colSpan }" class="line"></td></tr>
		</c:otherwise>
	</c:choose>
	</tbody>
</table>
<u:blank />
<u:pagination noTotalCount="true" noBottomBlank="true" pltBlock="true"/>