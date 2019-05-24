<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

String bxId = request.getParameter("bxId");
// 대기함
if("waitBx".equals(bxId) || "deptBx".equals(bxId)){
	// 제목, 도착일, 기안자, 문서상태
	request.setAttribute("attrNms", new String[]{"ap.doc.docSubj", "ap.list.prevDoneDt", "ap.list.docStatNm", "ap.doc.makrNm"});
	request.setAttribute("attrIds", new String[]{"docSubj", "prevApvrApvDt", "docStatNm", "makrNm"});
// 기안함
} else if("myBx".equals(bxId)){
	// 제목, 기안일, 처리자, 문서상태
	request.setAttribute("attrNms", new String[]{"ap.doc.docSubj", "ap.doc.makDd", "ap.list.docStatNm", "ap.list.curApvrNm"});
	request.setAttribute("attrIds", new String[]{"docSubj", "makDt", "docStatNm", "curApvrNm"});
// 통보함
} else if("postApvdBx".equals(bxId)){
	// 제목, 완결일, 기안자, 기안부서
	request.setAttribute("attrNms", new String[]{"ap.doc.docSubj", "ap.list.cmplDd", "ap.doc.makDeptNm", "ap.doc.makrNm"});
	request.setAttribute("attrIds", new String[]{"docSubj", "cmplDt", "makDeptNm", "makrNm"});
// 발송함
} else if("toSendBx".equals(bxId)){
	// 제목, 완결일, 기안자, 시행범위
	request.setAttribute("attrNms", new String[]{"ap.doc.docSubj", "ap.list.cmplDd", "cols.enfcScop", "ap.doc.makrNm"});
	request.setAttribute("attrIds", new String[]{"docSubj", "cmplDt", "enfcScopNm", "makrNm"});
// 접수함
} else if("recvBx".equals(bxId)){
	// 제목, 시행일, 기안자, 발송기관명
	request.setAttribute("attrNms", new String[]{"ap.doc.docSubj", "ap.doc.enfcDd", "cols.sendInstNm", "ap.doc.makrNm"});
	request.setAttribute("attrIds", new String[]{"docSubj", "enfcDt", "sendInstNm", "makrNm"});
// 참조열람
} else if("refVwBx".equals(bxId)){
	// 제목, 시행일, 기안자, 발송기관명
	request.setAttribute("attrNms", new String[]{"ap.doc.docSubj", "ap.doc.makDd", "ap.list.docStatNm", "ap.doc.makrNm"});
	request.setAttribute("attrIds", new String[]{"docSubj", "makDt", "docStatNm", "makrNm"});
}

// 자동숨김 - 1
String tdWidths = "45,20,20,15";
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
<table class="ptltable" id="ptltable" border="0" cellpadding="0" cellspacing="0" style="width:100%; table-layout:fixed;"><%
	// 자동숨김 - 5 - colgroup %>
	<colgroup><c:forEach items="${tdWidthArr}" var="tdWidth">
		<col width="${tdWidth}%" /></c:forEach>
	</colgroup>
	
	<tbody><c:if
		test="${param.colYn != 'N'}">
	<tr id="lineTr"><td colspan="4" class="line"></td></tr>
	<tr id="headerTr"><c:forEach items="${attrNms}" var="attrNm" >
		<td class="head_ct"><div class="ellipsis" title="<u:msg titleId="${attrNm}" />"><u:msg titleId="${attrNm}"
			/></div></td></c:forEach>
	</tr></c:if>
	<tr id="lineTr"><td colspan="4" class="line"></td></tr>
<c:forEach

	items="${apOngdBVoMapList}" var="apOngdBVoMap">
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'"><c:forEach
	
	items="${attrIds}" var="attrId"><c:set
	var="apOngdBVoMap" value="${apOngdBVoMap}" scope="request" /><c:if
	
	
		test="${attrId == 'docSubj'}"
		><u:set test="${apOngdBVoMap.docTypCd=='paper'}" var="docOpenFunc" value="parent.openPaperDoc('${apOngdBVoMap.apvNo}','${
						not empty apOngdBVoMap.docPwEnc
						and apOngdBVoMap.makrUid != sessionScope.userVo.userUid ? 'Y' : ''}')"
		/><c:if test="${apOngdBVoMap.docTypCd!='paper'}"><u:set test="${param.bxId=='drftBx' or (param.bxId=='rejtBx' and apOngdBVoMap.makrUid == sessionScope.userVo.userUid)}"
				var="docOpenFunc"
				value="parent.remakeDoc('${apOngdBVoMap.apvNo}','${apOngdBVoMap.apvLnPno}','${apOngdBVoMap.apvLnNo}')"
				elseValue="parent.openDoc('${apOngdBVoMap.apvNo}','${apOngdBVoMap.apvLnPno}','${apOngdBVoMap.apvLnNo}','${apOngdBVoMap.sendSeq}','${
						not empty apOngdBVoMap.docPwEnc
						and apOngdBVoMap.makrUid != sessionScope.userVo.userUid ? 'Y' : ''}')"
		/></c:if>
		<td class="body_lt"><div class="ellipsis" title="<u:convertMap
				srcId="apOngdBVoMap" attId="${attrId}" type="value"
			/>"><a href="javascript:${docOpenFunc}" style="${apOngdBVoMap.fontStyle}"><c:if
				test="${apOngdBVoMap.ugntDocYn == 'Y'}"
			>[<u:msg titleId="bb.option.ugnt" alt="긴급" />] </c:if><c:if
				test="${not empty apOngdBVoMap.docPwEnc}"
			>[<u:msg titleId="bb.option.secu" alt="보안" />] </c:if><u:convertMap
				srcId="apOngdBVoMap" attId="${attrId}" type="html" /></a></div></td></c:if><c:if
	
	
		test="${attrId == 'prevApvrApvDt' or attrId == 'makDt' or attrId == 'cmplDt' or attrId == 'enfcDt'}"
		>
		<td class="body_ct"><div class="ellipsis" title="<u:convertMap
				srcId="apOngdBVoMap" attId="${attrId}" type="date" />"><u:convertMap
				srcId="apOngdBVoMap" attId="${attrId}" type="date" /></div></td></c:if><c:if
	
	
		test="${attrId == 'makrNm'}"
		>
		<td class="body_ct"><div class="ellipsis" title="<u:convertMap srcId="apOngdBVoMap" attId="${attrId}" type="value"
			/>"><c:if test="${empty apOngdBVoMap.makrUid}"
				><u:out value="${apOngdBVoMap.makrNm}" type="html" /></c:if><c:if
					test="${not empty apOngdBVoMap.makrUid}"
				><c:if
			test="${not empty apOngdBVoMap.makrUid}"
			><a href="javascript:top.viewUserPop('${apOngdBVoMap.makrUid}')"><u:out
					value="${apOngdBVoMap.makrNm}" type="html" /></a></c:if><c:if
			test="${empty apOngdBVoMap.makrUid}"
			><u:out
					value="${apOngdBVoMap.makrNm}" type="html" /></c:if></c:if></div></td></c:if><c:if
	
	
		test="${attrId == 'curApvrNm'}"
		>
		<td class="body_ct"><div class="ellipsis" title="<u:convertMap srcId="apOngdBVoMap" attId="${attrId}" type="value"
			/>"><c:if test="${empty apOngdBVoMap.curApvrId or apOngdBVoMap.curApvrDeptYn=='Y'}"
				><u:out value="${apOngdBVoMap.curApvrNm}" type="html" /></c:if><c:if
					test="${not (empty apOngdBVoMap.curApvrId or apOngdBVoMap.curApvrDeptYn=='Y')}"
				><c:if
			test="${not empty apOngdBVoMap.curApvrId}"
			><a href="javascript:top.viewUserPop('${apOngdBVoMap.curApvrId}')"><u:out
					value="${apOngdBVoMap.curApvrNm}" type="html" /></a></c:if><c:if
			test="${empty apOngdBVoMap.curApvrId}"
			><u:out
					value="${apOngdBVoMap.curApvrNm}" type="html" /></c:if></c:if></div></td></c:if><c:if
			
		test="${attrId == 'docStatNm'}"
		><c:if
			test="${param.bxId == 'waitBx' and apOngdBVoMap.apvStatCd == 'hold'}"><u:msg
				titleId="ap.btn.hold" alt="보류" var="docStatNm" /></c:if><c:if
			test="${param.bxId == 'waitBx' and apOngdBVoMap.apvStatCd == 'cncl'}"><c:if
				test="${apOngdBVoMap.apvrRoleCd == 'psnOrdrdAgr'
					or apOngdBVoMap.apvrRoleCd == 'psnParalAgr'
					or apOngdBVoMap.apvrRoleCd == 'deptOrdrdAgr'
					or apOngdBVoMap.apvrRoleCd == 'deptParalAgr'
					or apOngdBVoMap.apvLnPno != '0'}"><u:msg
						titleId="ap.btn.cancelAgr" alt="합의취소" var="docStatNm" /></c:if><c:if
				test="${not (apOngdBVoMap.apvrRoleCd == 'psnOrdrdAgr'
					or apOngdBVoMap.apvrRoleCd == 'psnParalAgr'
					or apOngdBVoMap.apvrRoleCd == 'deptOrdrdAgr'
					or apOngdBVoMap.apvrRoleCd == 'deptParalAgr'
					or apOngdBVoMap.apvLnPno != '0')}"><u:msg
						titleId="ap.btn.cancelApv" alt="승인취소" var="docStatNm" /></c:if></c:if><c:if
			test="${param.bxId != 'waitBx' or not (apOngdBVoMap.apvStatCd == 'hold' or apOngdBVoMap.apvStatCd == 'cncl')}"><u:term
				termId="ap.term.${apOngdBVoMap.docStatCd}" var="docStatNm" /></c:if
		>
		<td class="body_ct"><div class="ellipsis" title="${docStatNm}">${docStatNm}</div></td></c:if><c:if
	
	
		test="${
			not (attrId == 'docSubj' or attrId == 'makrNm' or attrId == 'curApvrNm' or attrId == 'docStatNm'
				or attrId == 'prevApvrApvDt' or attrId == 'makDt' or attrId == 'cmplDt' or attrId == 'enfcDt')}"
		><td class="body_ct"><div class="ellipsis" title="<u:convertMap
				srcId="apOngdBVoMap" attId="${attrId}" type="value" />"><u:convertMap
				srcId="apOngdBVoMap" attId="${attrId}" /></div></td></c:if
></c:forEach>
	</tr>
	<tr id="lineTr"><td colspan="4" class="line"></td></tr>
</c:forEach>
<c:if test="${fn:length(apOngdBVoMapList)==0}" >
	<tr><td class="nodata" colspan="4"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td></tr>
	<tr id="lineTr"><td colspan="4" class="line"></td></tr>
</c:if>
</tbody>
</table>

<u:blank />
<u:pagination noTotalCount="true" noBottomBlank="true" pltBlock="true"/>
