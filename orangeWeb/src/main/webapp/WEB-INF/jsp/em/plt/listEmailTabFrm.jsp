<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<!-- 화면에 보여질 목록정보 -->
<c:set var="headList" value="email.subj,email.fromEmail,email.recvDt"/><!-- 컬럼명(컬럼명을 보여줄경우) -->
<c:set var="colSpan" value="3"/><!-- line css 를 주기 위한 colspan -->

<script type="text/javascript">
<!--
//상세보기
function viewEmail(mailSn,mailboxCode) {
	var authUrl = '/cm/zmailPop.do';
	var prefix = authUrl.indexOf('?') > -1 ? "&" : "?";
	authUrl+=prefix+"mailSn="+mailSn+"&mailboxCode="+mailboxCode;
	authUrl+="&cmd=mail_detail";
	//authUrl+="&cmd=mail_detail_portlet";
	top.location.href=authUrl;	
	//openWindow(authUrl,'sendEmailPop',1100,700,'resizable=yes,status=yes,toolbar=no,menubar=no,scrollbars=yes');
};

<%
//자동숨김 - 1
String tdWidths = "60,20,20";
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
				<td class="head_ct" id="head_${fn:split(colId,'.')[1] }" style="display:none;"><div class="ellipsis" title="<u:msg titleId="em.cols.${colId}" alt="이름" />"><u:msg titleId="em.cols.${colId}" alt="이름" /></div></td>
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
						<div class="ellipsis" title="<u:convertMap srcId="mapList" attId="subject" />"><a href="javascript:viewEmail('${mapList.mailSn }','${mapList.mailboxCode }');"><u:convertMap srcId="mapList" attId="subject" type="script"/></a></div>
					</td>
					<td class="body_ct">
						<div class="ellipsis" title="<u:convertMap srcId="mapList" attId="fromName" type="html" />"><u:convertMap srcId="mapList" attId="fromName" type="html" /></div>
					</td>
					<td class="body_ct">
						<fmt:parseDate var="recvDt" value="${mapList.receiveTime }" pattern="yyyyMMddHHmm"/>
						<fmt:formatDate var="recvDt" value="${recvDt}" pattern="yyyy-MM-dd HH:mm"/>
						<div class="ellipsis" title="${recvDt }"><u:out value="${recvDt}" /></div>
					</td>		
				</tr>
				<tr id="headerTr">
				  <td colspan="${colSpan}" class="line"></td>
				</tr>
			</c:forEach>
			</c:when>
		<c:otherwise>
			<tr>
				<td class="nodata" colspan="${colSpan}"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
			<tr id="lineTr"><td colspan="${colSpan}" class="line"></td></tr>
		</c:otherwise>
	</c:choose>
	</tbody>
</table>
<u:blank />
<%-- <u:pagination noTotalCount="true" noBottomBlank="true" pltBlock="true"/> --%>