<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="paramForList" excludes="menuId" />

<%

String bxId = request.getParameter("bxId");
String tdWidths = "55,20,25";
// 보낸쪽지
if("send".equals(bxId)){
	// 제목, 보낸시간, 비고[수신확인]
	request.setAttribute("attrNms", new String[]{"cols.subj", "cm.cols.send.user","cols.note"});
	request.setAttribute("attrIds", new String[]{"subj", "regDt", "note"});
	tdWidths = "55,25,20";
// 읽지않은쪽지, 최근쪽지, 받은쪽지
} else{
	// 제목, 보낸사람, 받은시간
	request.setAttribute("attrNms", new String[]{"cols.subj", "cm.cols.send.user","cm.cols.recv.time"});
	request.setAttribute("attrIds", new String[]{"subj", "regrNm", "regDt"});
} 

// 자동숨김 - 1
request.setAttribute("tdWidths", tdWidths);
request.setAttribute("tdWidthArr", tdWidths.split(","));

%>
<u:authUrl var="viewUrl" url="/cu/${param.bxId eq 'send' ? 'send' : 'recv'}/viewNotePltPop.do" authCheckUrl="/cu/${param.bxId eq 'send' ? 'send' : 'recv'}/listNote.do"/><!-- view page 호출관련 url 조합(menuId추가) -->

<script type="text/javascript">
<!--
//상세보기
function viewNote(seqNo) {
	var authUrl = '${viewUrl}';
	var prefix = authUrl.indexOf('?') > -1 ? "&" : "?";
	authUrl+=prefix+"seqNo="+seqNo;
	top.dialog.open('viewNoteDialog','<u:msg titleId="cm.btn.detl" alt="상세정보" />', authUrl);
	<c:if test="${param.bxId=='notRead'}">
	var dataTr=$('#data_'+seqNo);
	dataTr.next().remove();
	dataTr.remove();
	if($('tr[id^="data_"]').length==0){
		var noData = $('#noData');
		noData.next().show();
		noData.show();
	}
	</c:if>	
};

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

$(document).ready(function() {
	// 자동숨김 - 2
	// 텝 - 가로폭에 따라서 컬럼을 숨김 %>
	resizeForPltTable('ptltable', 100, '${tdWidths}');
	var qstr = '${queryString}';
	if(qstr.indexOf('pageRowCnt')<0) qstr += '&pageNo=1&pageRowCnt=${pageRowCnt}';
	parent.setQueryString(qstr);
});
//-->
</script>
<% // 목록 %>
<table class="ptltable" id="ptltable" border="0" cellpadding="0" cellspacing="0" style="width:100%; table-layout:fixed;"><%
	// 자동숨김 - 5 - colgroup %>
	<colgroup><c:forEach items="${tdWidthArr}" var="tdWidth">
		<col width="${tdWidth}%" /></c:forEach>
	</colgroup>
	<c:set var="colspan" value="${fn:length(attrNms) }"/>
	<tbody><c:if
		test="${param.colYn != 'N'}">
	<tr id="lineTr"><td colspan="${colspan }" class="line"></td></tr>
	<tr id="headerTr"><c:forEach items="${attrNms}" var="attrNm" >
		<td class="head_ct"><div class="ellipsis" title="<u:msg titleId="${attrNm}" />"><u:msg titleId="${attrNm}"
			/></div></td></c:forEach>
	</tr></c:if>
	<tr id="lineTr"><td colspan="${colspan }" class="line"></td></tr>
	<c:if test="${!empty rsltMapList}">
		<c:forEach var="map" items="${rsltMapList}" varStatus="status">
			<u:set var="seqNo" test="${param.bxId eq 'send' }" value="${map.sendNo }" elseValue="${map.recvNo }"/>
			<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"' id="data_${seqNo }">
				<c:forEach items="${attrIds}" var="atrbId"><c:set	var="map" value="${map}" scope="request" />
					<u:convertMap var="value" srcId="map" attId="${atrbId}" type="html" />						
					<td class="body_${atrbId eq 'subj' || atrbId eq 'rescNm' ? 'lt' : 'ct' }" >
						<c:choose>
							<c:when test="${fn:indexOf(atrbId, 'Dt')>0}"><div class="ellipsis" title="${value}"><u:out value="${value}" type="longdate"/></div></c:when>
							<c:when test="${atrbId eq 'subj' }"><div class="ellipsis" title="${value}"><a href="javascript:viewNote('${seqNo }');">${value}</a></div></c:when>
							<c:otherwise><div class="ellipsis" title="${value}">${value}</div></c:otherwise>
						</c:choose>
					</td>
				</c:forEach>
			</tr>
			<tr id="headerTr">
			  <td colspan="${colspan}" class="line"></td>
			</tr>
		</c:forEach>
	</c:if>
	
	<tr id="noData" <c:if test="${!empty rsltMapList}">style="display:none;"</c:if>>
		<td class="nodata" colspan="${colspan}"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
	<tr id="lineTr" <c:if test="${!empty rsltMapList}">style="display:none;"</c:if>><td colspan="${colspan}" class="line"></td></tr>
	</tbody>
</table>
<u:blank />
<u:pagination noTotalCount="true" noBottomBlank="true"  pltBlock="true"/>