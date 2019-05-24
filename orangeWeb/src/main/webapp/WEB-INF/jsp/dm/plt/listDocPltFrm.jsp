<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

String bxId = request.getParameter("bxId");
String tdWidths = "45,20,15,20";
// 문서조회, 최신문서, 소유문서
if("listDoc".equals(bxId) || "listNewDoc".equals(bxId) || "listOwnDoc".equals(bxId)){
	// 폴더, 분류, 제목, 등록일
	request.setAttribute("attrNms", new String[]{"cols.subj", "cols.fld","cols.cls","cols.regDt"});
	request.setAttribute("attrIds", new String[]{"subj", "fldNm", "clsNm", "regDt"});
// 보존연한문서
} else if("listKprdDoc".equals(bxId)){
	// 폴더, 제목, 등록일, 보존기한
	request.setAttribute("attrNms", new String[]{"cols.subj", "cols.fld","cols.regDt","dm.cols.keepPrd"});
	request.setAttribute("attrIds", new String[]{"subj", "fldNm", "regDt", "keepPrdDt"});
// 개인문서
} else if("listPsnDoc".equals(bxId)){
	// 폴더, 제목, 등록일시
	request.setAttribute("attrNms", new String[]{"cols.subj", "cols.fld","cols.regDt"});
	request.setAttribute("attrIds", new String[]{"subj", "fldNm", "regDt"});
	tdWidths = "60,20,20";
// 열람요청문서
} else if("listOpenReqDoc".equals(bxId)){
	// 폴더, 제목, 등록일, 보존기한
	request.setAttribute("attrNms", new String[]{"cols.subj", "cols.cls","cols.regDt","dm.cols.dtlView.prd"});
	request.setAttribute("attrIds", new String[]{"subj", "clsNm", "regDt", "readDt"});
} 

// 자동숨김 - 1
request.setAttribute("tdWidths", tdWidths);
request.setAttribute("tdWidthArr", tdWidths.split(","));

%>
<script type="text/javascript">
<!--<%
// onload %>
$(document).ready(function() {<%
	// 자동숨김 - 2
	// 텝 - 가로폭에 따라서 컬럼을 숨김 %>
	resizeForPltTable('ptltable', 190, '${tdWidths}');
	var qstr = '${queryString}';
	if(qstr.indexOf('pageRowCnt')<0) qstr += '&pageNo=1&pageRowCnt=${pageRowCnt}';
	parent.setQueryString(qstr);
});<%

//자동숨김 - 3
// 포틀릿 테이블 - 테이블ID %>
var gPltTableId = null;<%
// 포틀릿 테이블 - 첫째 컬럼 최소 가로 길이(타입:number, 단위:px) %>
var gPltTableMinFirst = null;<%
// 포틀릿 테이블 - 각 컬럼 비율 (예: "40,20,20,20") %>
var gPltTableRatio = null;<%
// 포틀릿 테이블 - 가로폭에 따른 컬럼 숨기기, 보이기 %>
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
// 포틀릿 테이블 - 가로폭에 따른 컬럼 숨기기, 보이기 - 실제 함수 %>
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
	var totalColCnt=0;
	for(i=0;i<colCnt;i++){
		if(i==showIndex){
			percent = 100 - percentSum;
			$colgroup.find("col:eq("+i+")").attr("width", percent+"%");
			totalColCnt++;
		} else if(i<showIndex){
			percent = parseInt((gPltTableRatio[i]/ratioSum)*100 ,10);
			percentSum += percent;
			$colgroup.find("col:eq("+i+")").attr("width", percent+"%");
			totalColCnt++;
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
	if(totalColCnt>0)
		$table.find("tbody td.line").attr('colspan', totalColCnt);
}
//-->
</script>
<table class="ptltable" id="ptltable" border="0" cellpadding="0" cellspacing="0" style="width:100%; table-layout:fixed;"><%
	// 자동숨김 - 5 - colgroup %>
	<colgroup><c:forEach items="${tdWidthArr}" var="tdWidth">
		<col width="${tdWidth}%" /></c:forEach>
	</colgroup>
	<c:set var="colspan" value="${empty param.bxId ? 4 : fn:length(attrNms) }"/>
	<tbody><c:if
		test="${param.colYn != 'N'}">
	<tr id="lineTr"><td colspan="${colspan }" class="line"></td></tr>
	<tr id="headerTr"><c:forEach items="${attrNms}" var="attrNm" >
		<td class="head_ct"><div class="ellipsis" title="<u:msg titleId="${attrNm}" />"><u:msg titleId="${attrNm}"
			/></div></td></c:forEach>
	</tr></c:if>
	<tr id="lineTr"><td colspan="${colspan }" class="line"></td></tr>
<c:forEach
	items="${mapList}" var="map">
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'"><c:forEach
	items="${attrIds}" var="atrbId"><c:set	var="map" value="${map}" scope="request" />
		<c:choose>
			<c:when test="${atrbId eq 'readDt'}">
				<u:convertMap var="strtValue" srcId="map" attId="${fn:substring(atrbId,0,fn:length(atrbId)-2)}StrtDt" type="html" />
				<u:convertMap var="endValue" srcId="map" attId="${fn:substring(atrbId,0,fn:length(atrbId)-2)}EndDt" type="html" />
				<u:out var="strtDt" value="${strtValue }" type="date"/><u:out var="endDt" value="${endValue }" type="date"/>
				<c:set var="value" value="${strtDt }~${endDt }"/>
			</c:when>
			<c:otherwise><u:convertMap var="mapValue" srcId="map" attId="${atrbId}" type="html" /><c:set var="value" value="${mapValue }"/></c:otherwise>
		</c:choose>
		<td class="body_${atrbId eq 'subj' ? 'lt' : 'ct' }"><div class="ellipsis" title="${value }"
		><c:choose><c:when test="${atrbId eq 'subj'}"><u:set var="viewDoc" test="${param.bxId eq 'listPsnDoc'}" value="viewPsnDoc('${map.docId}');" elseValue="viewDoc('${map.docId}','${map.docGrpId }','${map.tgtId }');"/><a href="javascript:parent.${viewDoc }">${value }</a></c:when
		><c:when test="${fn:endsWith(atrbId,'Dt')}"><u:out value="${value }" type="longdate"/></c:when
		><c:otherwise>${value }</c:otherwise></c:choose></div></c:forEach
		></tr><tr id="lineTr"><td colspan="${colspan }" class="line"></td></tr></c:forEach
		>
<c:if test="${fn:length(mapList)==0}" >
	<tr><td class="nodata" colspan="${colspan }"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td></tr>
	<tr id="lineTr"><td colspan="${colspan }" class="line"></td></tr>
</c:if>
</tbody>
</table>

<u:blank />
<u:pagination noTotalCount="true" noBottomBlank="true" pltBlock="true"/>
