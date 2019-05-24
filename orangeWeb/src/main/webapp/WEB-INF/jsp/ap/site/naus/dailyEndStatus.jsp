<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
//양식명 : 온라인 일일 마감 현황

if(!"view".equals(request.getAttribute("formBodyMode"))){
	com.innobiz.orange.web.ap.utils.XMLElement formBodyXML = (com.innobiz.orange.web.ap.utils.XMLElement)request.getAttribute("formBodyXML");
	if(formBodyXML != null){
		if(request.getAttribute("namoEditorEnable")==null){
			String _bodyHtml = com.innobiz.orange.web.cm.utils.EscapeUtil.escapeWriteableHtml(formBodyXML.getAttr("body/cont.erpCont"));
			request.setAttribute("_bodyHtml", _bodyHtml);
		} else {
			request.setAttribute("_bodyHtml", formBodyXML.getAttr("body/cont.erpCont"));
		}
	}
}
// 초기 row수
request.setAttribute("data1StrtCnt", "1");
request.setAttribute("data2StrtCnt", "1");
request.setAttribute("data3StrtCnt", "1");
request.setAttribute("data4StrtCnt", "1");
request.setAttribute("data5StrtCnt", "1");
%><style type="text/css">
.titlearea {
    width: 100%;
    height: 16px;
    margin: 0 0 9px 0;
}
.titlearea .tit_left .title_s {
    float: left;
    height: 13px;
    font-weight: bold;
    color: #454545;
}
.listarea {
    float: left;
    width: 100%;
    padding: 0;
    margin: 0;
    color: #454545;
}
.listtable {
    width: 100%;
    background: #bfc8d2;
    color: #454545;
}
.listtable tr {
    background: #ffffff;
}
.listtable .head_ct {
	height: 22px;
    text-align: center;
    background: #ebf1f6;
    line-height: 17px;
    padding: 2px 2px 0 2px;
}
.listtable .head_lt {
    height: 22px;
    background: #ebf1f6;
    line-height: 17px;
    padding: 2px 0 0 4px;
}
.body_ct {
    height: 22px;
    color: #454545;
    text-align: center;
    line-height: 17px;
    padding: 2px 3px 0 3px;
}
.body_lt {
    height: 22px;
    color: #454545;
    line-height: 17px;
    padding: 2px 3px 0 4px;
}
.blank {
    clear: both;
    height: 10px;
}
</style><script type="text/javascript">
<!--<%//xml 수집 전에 호출함 %>
function checkFormBodyXML(){
	editor('erpCont').prepare();
	return true;
}
<%// 행삭제%>
function delRow(conId){
	conId=conId.replace('/','\\/');
	var len=$("#"+conId+" tbody:first tr").not('#headerTr, #hiddenTr, #xml-data4, #xml-data21, #xml-data22').length;
	if(conId.startsWith('xml-data2')) len-=1;
	if(len<2){
		alert('최소 1줄 이상 입력해야 합니다.');
		return;
	}
	var $tr = $("#"+conId+" tbody:first #hiddenTr").prev();
	delRowInArr([$tr[0]]);
	
	if(conId.startsWith('xml-data4')){
		var lastTd=$("#"+conId+" tbody:first tr:not(#headerTr):first td:last");
		var rowspan = lastTd.attr('rowspan');
		if(rowspan===undefined) rowspan=1;
		lastTd.attr('rowspan', parseInt(rowspan)-1);
	}
	
}
<%// 삭제 - 배열에 담긴 목록%>
function delRowInArr(rowArr){
	for(var i=0;i<rowArr.length;i++){
		$(rowArr[i]).remove();
	}
}
<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(conId, noSelectMsg){
	var arr=[], id, obj;
	conId=conId.replace('/','\\/');
	$("#"+conId+" tbody:first input[type='checkbox']:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
}
<%// 행추가%>
function addRow(conId, trId, strtCnt){
	if(strtCnt==undefined) strtCnt = 0;
	strtCnt++;
	conId=conId.replace('/','\\/');
	var $tr = $("#"+conId+" tbody:first #hiddenTr");
	var div=$tr.closest('div');
	$.uniform.restore(div.find('input, textarea, select, button'));
	var html = $tr[0].outerHTML;
	$tr.before(html);
	$tr = $tr.prev();
	$tr.attr('id',trId);
	$tr.attr('style','');
	var seq=$("#"+conId+" tbody:first tr").not('#hiddenTr').length;
	$tr.find('td#seq').text(seq++);
	div.find("input, textarea, select, button").uniform();
	
	if(conId.startsWith('xml-data4')){
		var lastTd=$("#"+conId+" tbody:first tr:not(#headerTr):first td:last");
		var rowspan = lastTd.attr('rowspan');
		if(rowspan===undefined) rowspan=1;
		lastTd.attr('rowspan', parseInt(rowspan)+1);
	}
	
	<c:if test="${formBodyMode eq 'pop'}">dialog.resize("setDocBodyHtmlDialog");</c:if>
}
<%// 편집양식 인쇄모드 변경 %>
function setXmlEditViewMode(mode){
	$xmlArea = $("#xmlArea");
	var $xmlCont = $xmlArea.find("#xml-cont");
	var $xmlContView = $xmlArea.find("#xml-contView");
	
	if(mode=='print'){
		$xmlContView.html(editor("erpCont").getHtml());
		$xmlCont.hide();
		$xmlContView.show();
	} else {
		$xmlCont.show();
		$xmlContView.hide();
	}
}
function clearFormEditor(){
	editor('erpCont').clean(); unloadEvent.removeEditor('erpCont');
}
$(document).ready(function(){
	<c:if test="${formBodyMode ne 'view'}">
	$('#xml-cont iframe').eq(0).on('load',function () {
		$('#xml-dft #totalCnt').focus();
	});
	</c:if>
});

-->
</script><c:if

	test="${formBodyMode ne 'view'}">
<div id="xmlArea" style="${formBodyMode eq 'pop' ? 'width:900px;' : ''}">

<div id="xml-head">
	<input type="hidden" name="typId" value="dailyEndStatus"/>
	<input type="hidden" name="ver" value="1"/>
</div>

<div id="xml-body">

<u:set var="data1StrtCnt" test="${empty formBodyXML.getChildList('body/data1') }" value="${data1StrtCnt }" elseValue="0"/><!-- 추가 Row수 -->
<u:title title="1.사이트별 매출액" type="small" alt="1.사이트별 매출액" />
<u:listArea id="xml-dft" style="width:200px;" colgroup="40%,">
	<tr>
		<td class="head_ct">전체</td>
		<td class="body_ct"><u:input id="totalCnt" name="totalCnt" value="${formBodyXML.getAttr('body/dft.totalCnt')}" title="전체 매출액" style="width:88%;" maxByte="100" /></td>
	</tr>
</u:listArea>

<c:if test="${formBodyMode eq 'edit' }"><div style="display:block;float:right;height:25px;"><u:buttonS title="행추가" onclick="addRow('xml-data1\\/row', 'row', '${data1StrtCnt }');" alt="행추가"
/><u:buttonS title="행삭제" onclick="delRow('xml-data1/row');" alt="행삭제"/></div></c:if>

<u:listArea id="xml-data1/row" colgroup="12%,11%,11%,11%,11%,11%,11%,11%,11%" >
	<tr id="headerTr">
		<td class="head_ct">사이트</td>
		<td class="head_ct">자동백</td>
		<td class="head_ct">수동백</td>
		<td class="head_ct">부직/비닐백</td>
		<td class="head_ct">봉투</td>
		<td class="head_ct">식품포장지</td>
		<td class="head_ct">종이용품</td>
		<td class="head_ct">컵류</td>
		<td class="head_ct">포장용품</td>
	</tr>
	<c:forEach
			items="${formBodyXML.getChildList('body/data1', 1, 1)}" var="row" varStatus="status" >
	<tr id="${status.last ? 'hiddenTr' : 'row'}" style="${status.last ? 'display:none' : ''}">
		<td class="body_ct"><c:if test="${formBodyMode ne 'edit' && !empty row.getAttr('erpValue1')}"
		><u:out value="${row.getAttr('erpValue1')}" /><u:input type="hidden" id="erpValue1_${status.index }" name="erpValue1" value="${row.getAttr('erpValue1')}" /></c:if
		><c:if test="${formBodyMode eq 'edit' || (formBodyMode ne 'view' && empty row.getAttr('erpValue1'))}"
		><u:input id="erpValue1_${status.index }" name="erpValue1" value="${row.getAttr('erpValue1')}" title="사이트" style="width:88%;" maxByte="100" 
		/></c:if></td>
		<td class="body_ct"><u:input id="erpValue2_${status.index }" name="erpValue2" value="${row.getAttr('erpValue2')}" title="자동백" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue3_${status.index }" name="erpValue3" value="${row.getAttr('erpValue3')}" title="수동백" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue4_${status.index }" name="erpValue4" value="${row.getAttr('erpValue4')}" title="부직/비닐백" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue5_${status.index }" name="erpValue5" value="${row.getAttr('erpValue5')}" title="봉투" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue6_${status.index }" name="erpValue6" value="${row.getAttr('erpValue6')}" title="식품포장지" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue7_${status.index }" name="erpValue7" value="${row.getAttr('erpValue7')}" title="종이용품" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue8_${status.index }" name="erpValue8" value="${row.getAttr('erpValue8')}" title="컵류" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue9_${status.index }" name="erpValue9" value="${row.getAttr('erpValue9')}" title="포장용품" style="width:88%;" maxByte="100" /></td>
	</tr></c:forEach>
</u:listArea>

<u:set var="data2StrtCnt" test="${empty formBodyXML.getChildList('body/data2') }" value="${data2StrtCnt }" elseValue="0"/><!-- 추가 Row수 -->
<c:if test="${formBodyMode eq 'edit' }"><div style="display:block;float:right;height:25px;"><u:buttonS title="행추가" onclick="addRow('xml-data2\\/row', 'row', '${data2StrtCnt }');" alt="행추가"
/><u:buttonS title="행삭제" onclick="delRow('xml-data2/row');" alt="행삭제"/></div></c:if>
<u:listArea id="xml-data2/row" colgroup="12%,12%,12%,12%,12%,12%,12%," >
	<c:forEach
			items="${formBodyXML.getChildList('body/data2', 1, 1)}" var="row" varStatus="status" >
	<c:if test="${status.index==0 }">
	<tr id="row">
		<td class="head_ct">사이트</td>
		<td class="head_ct">플라스틱용기</td>
		<td class="head_ct">박스류</td>
		<td class="head_ct">기타</td>
		<td class="head_ct"><c:if test="${formBodyMode ne 'edit' && !empty row.getAttr('erpColm5')}"
		><u:out value="${row.getAttr('erpColm5')}" /><u:input type="hidden" id="erpColm5" value="${row.getAttr('erpColm5')}" /></c:if
		><c:if test="${formBodyMode eq 'edit' || (formBodyMode ne 'view' && empty row.getAttr('erpColm5'))}"
		><u:input id="erpColm5_${status.index }" name="erpColm5" value="${row.getAttr('erpColm5')}" title="컬럼5" style="width:88%;" maxByte="100" /></c:if></td>
		<td class="head_ct">건수</td>
		<td class="head_ct">금액</td>
		<td class="head_ct">월계</td>
	</tr></c:if><c:if test="${status.index>0 }">
	<tr id="${status.last ? 'hiddenTr' : 'row'}" style="${status.last ? 'display:none' : ''}">
		<td class="body_ct"><c:if test="${formBodyMode ne 'edit' && !empty row.getAttr('erpValue1')}"
		><u:out value="${row.getAttr('erpValue1')}" /><u:input type="hidden" id="erpValue21_${status.index }" name="erpValue1" value="${row.getAttr('erpValue1')}" /></c:if
		><c:if test="${formBodyMode eq 'edit' || (formBodyMode ne 'view' && empty row.getAttr('erpValue1'))}"
		><u:input id="erpValue21_${status.index }" name="erpValue1" value="${row.getAttr('erpValue1')}" title="사이트" style="width:88%;" maxByte="100" 
		/></c:if></td>
		<td class="body_ct"><u:input id="erpValue22_${status.index }" name="erpValue2" value="${row.getAttr('erpValue2')}" title="플라스틱용기" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue23_${status.index }" name="erpValue3" value="${row.getAttr('erpValue3')}" title="박스류" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue24_${status.index }" name="erpValue4" value="${row.getAttr('erpValue4')}" title="기타" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue25_${status.index }" name="erpValue5" value="${row.getAttr('erpValue5')}" title="컬럼5" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue26_${status.index }" name="erpValue6" value="${row.getAttr('erpValue6')}" title="건수" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue27_${status.index }" name="erpValue7" value="${row.getAttr('erpValue7')}" title="금액" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue28_${status.index }" name="erpValue8" value="${row.getAttr('erpValue8')}" title="월계" style="width:88%;" maxByte="100" /></td>
	</tr></c:if></c:forEach>
	
	<tr id="xml-data21">
		<td class="head_ct">합계</td>
		<td class="body_ct"><u:input id="erpValue212" name="erpValue2" value="${formBodyXML.getAttr('body/data21.erpValue2')}" title="플라스틱용기" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue213" name="erpValue3" value="${formBodyXML.getAttr('body/data21.erpValue3')}" title="박스류" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue214" name="erpValue4" value="${formBodyXML.getAttr('body/data21.erpValue4')}" title="기타" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue215" name="erpValue5" value="${formBodyXML.getAttr('body/data21.erpValue5')}" title="컬럼5" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue216" name="erpValue6" value="${formBodyXML.getAttr('body/data21.erpValue6')}" title="건수" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue217" name="erpValue7" value="${formBodyXML.getAttr('body/data21.erpValue7')}" title="금액" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct" rowspan="2"><u:input id="erpValue218" name="erpValue8" value="${formBodyXML.getAttr('body/data21.erpValue8')}" title="월계" style="width:88%;" maxByte="100" /></td>
	</tr>
	<tr id="xml-data22">
		<td class="head_ct">월 합계</td>
		<td class="body_ct"><u:input id="erpValue222" name="erpValue2" value="${formBodyXML.getAttr('body/data22.erpValue2')}" title="플라스틱용기" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue223" name="erpValue3" value="${formBodyXML.getAttr('body/data22.erpValue3')}" title="박스류" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue224" name="erpValue4" value="${formBodyXML.getAttr('body/data22.erpValue4')}" title="기타" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue225" name="erpValue5" value="${formBodyXML.getAttr('body/data22.erpValue5')}" title="컬럼5" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue226" name="erpValue6" value="${formBodyXML.getAttr('body/data22.erpValue6')}" title="건수" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue227" name="erpValue7" value="${formBodyXML.getAttr('body/data22.erpValue7')}" title="금액" style="width:88%;" maxByte="100" /></td>
	</tr>
	
</u:listArea>

<u:blank />
<u:set var="data3StrtCnt" test="${empty formBodyXML.getChildList('body/data3') }" value="${data3StrtCnt }" elseValue="0"/><!-- 추가 Row수 -->
<u:title title="2.맞춤제작" type="small" alt="2.맞춤제작" >
<c:if test="${formBodyMode eq 'edit' }"><u:titleButton title="행추가" onclick="addRow('xml-data3/row', 'row', '${data3StrtCnt }');" alt="행추가"
/><u:buttonS title="행삭제" onclick="delRow('xml-data3/row');" alt="행삭제"/></c:if>
</u:title>
<u:listArea id="xml-data3/row" colgroup="12%,16%,15%,14%,14%,15%,14%" >
	<tr id="headerTr">
		<td class="head_ct">사이트</td>
		<td class="head_ct">요청사항</td>
		<td class="head_ct">품명</td>
		<td class="head_ct">수량</td>
		<td class="head_ct">금액</td>
		<td class="head_ct">업체명</td>
		<td class="head_ct">문의경로</td>
	</tr>
	<c:forEach
			items="${formBodyXML.getChildList('body/data3', 1, 1)}" var="row" varStatus="status" >
	<tr id="${status.last ? 'hiddenTr' : 'row'}" style="${status.last ? 'display:none' : ''}">
		<td class="body_ct"><c:if test="${formBodyMode ne 'edit' && !empty row.getAttr('erpValue1')}"
		><u:out value="${row.getAttr('erpValue1')}" /><u:input type="hidden" id="erpValue31_${status.index }" name="erpValue1" value="${row.getAttr('erpValue1')}" /></c:if
		><c:if test="${formBodyMode eq 'edit' || (formBodyMode ne 'view' && empty row.getAttr('erpValue1'))}"
		><u:input id="erpValue31_${status.index }" name="erpValue1" value="${row.getAttr('erpValue1')}" title="사이트" style="width:88%;" maxByte="100" 
		/></c:if></td>
		<td class="body_ct"><u:input id="erpValue32_${status.index }" name="erpValue2" value="${row.getAttr('erpValue2')}" title="요청사항" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue33_${status.index }" name="erpValue3" value="${row.getAttr('erpValue3')}" title="품명" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue34_${status.index }" name="erpValue4" value="${row.getAttr('erpValue4')}" title="수량" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue35_${status.index }" name="erpValue5" value="${row.getAttr('erpValue5')}" title="금액" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue36_${status.index }" name="erpValue6" value="${row.getAttr('erpValue6')}" title="업체명" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue37_${status.index }" name="erpValue7" value="${row.getAttr('erpValue7')}" title="문의경로" style="width:88%;" maxByte="100" /></td>
	</tr></c:forEach>
</u:listArea>


<u:blank />
<u:set var="data4StrtCnt" test="${empty formBodyXML.getChildList('body/data4') }" value="${data4StrtCnt }" elseValue="0"/><!-- 추가 Row수 -->
<u:title title="3.일일 CS 현황" type="small" alt="3.일일 CS 현황" >
<c:if test="${formBodyMode eq 'edit' }"><u:titleButton title="행추가" onclick="addRow('xml-data4/row', 'row', '${data4StrtCnt }');" alt="행추가"
/><u:buttonS title="행삭제" onclick="delRow('xml-data4/row');" alt="행삭제"/></c:if>
</u:title>
<u:set var="data4TotalCnt" test="${empty formBodyXML.getChildList('body/data4') }" value="1" elseValue="${fn:length(formBodyXML.getChildList('body/data4')) }"/>
<u:listArea id="xml-data4/row" colgroup="12%,16%,15%,14%,14%,15%,14%" >
	<tr id="headerTr">
		<td class="head_ct">사이트</td>
		<td class="head_ct">품명</td>
		<td class="head_ct">수량</td>
		<td class="head_ct">문의사항</td>
		<td class="head_ct">처리현황</td>
		<td class="head_ct">문의경로</td>
		<td class="head_ct">비고</td>
	</tr>
	<c:forEach
			items="${formBodyXML.getChildList('body/data4', 1, 1)}" var="row" varStatus="status" >
	<tr id="${status.last ? 'hiddenTr' : 'row'}" style="${status.last ? 'display:none' : ''}">
		<td class="body_ct"><c:if test="${formBodyMode ne 'edit' && !empty row.getAttr('erpValue1')}"
		><u:out value="${row.getAttr('erpValue1')}" /><u:input type="hidden" id="erpValue441_${status.index }" name="erpValue1" value="${row.getAttr('erpValue1')}" /></c:if
		><c:if test="${formBodyMode eq 'edit' || (formBodyMode ne 'view' && empty row.getAttr('erpValue1'))}"
		><u:input id="erpValue441_${status.index }" name="erpValue1" value="${row.getAttr('erpValue1')}" title="사이트" style="width:88%;" maxByte="100" 
		/></c:if></td>
		<td class="body_ct"><u:input id="erpValue442_${status.index }" name="erpValue2" value="${row.getAttr('erpValue2')}" title="품명" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue443_${status.index }" name="erpValue3" value="${row.getAttr('erpValue3')}" title="수량" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue444_${status.index }" name="erpValue4" value="${row.getAttr('erpValue4')}" title="문의사항" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue445_${status.index }" name="erpValue5" value="${row.getAttr('erpValue5')}" title="처리현황" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue446_${status.index }" name="erpValue6" value="${row.getAttr('erpValue6')}" title="문의경로" style="width:88%;" maxByte="100" /></td>
		<c:if test="${status.index==0 }"><td class="body_ct" rowspan="${data4TotalCnt}"><u:input id="erpValue447_${status.index }" name="erpValue7" value="${row.getAttr('erpValue7')}" title="비고" style="width:88%;" maxByte="100" /></td></c:if>
	</tr></c:forEach>
	<tr id="xml-data41">
		<td class="head_ct">합계</td>
		<td class="body_ct"><u:input id="erpValue42" name="erpValue42" value="${formBodyXML.getAttr('body/data41.erpValue42')}" title="품명" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue43" name="erpValue43" value="${formBodyXML.getAttr('body/data41.erpValue43')}" title="수량" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue44" name="erpValue44" value="${formBodyXML.getAttr('body/data41.erpValue44')}" title="문의사항" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue45" name="erpValue45" value="${formBodyXML.getAttr('body/data41.erpValue45')}" title="처리현황" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue46" name="erpValue46" value="${formBodyXML.getAttr('body/data41.erpValue46')}" title="문의경로" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue47" name="erpValue47" value="${formBodyXML.getAttr('body/data41.erpValue47')}" title="비고" style="width:88%;" maxByte="100" /></td>
	</tr>
</u:listArea>

<u:blank />
<u:set var="data5StrtCnt" test="${empty formBodyXML.getChildList('body/data5') }" value="${data5StrtCnt }" elseValue="0"/><!-- 추가 Row수 -->
<u:title title="4.광고비 지출 현황" type="small" alt="4.맞춤제작" >
<c:if test="${formBodyMode eq 'edit' }"><u:titleButton title="행추가" onclick="addRow('xml-data5/row', 'row', '${data5StrtCnt }');" alt="행추가"
/><u:buttonS title="행삭제" onclick="delRow('xml-data5/row');" alt="행삭제"/></c:if>
</u:title>
<u:listArea id="xml-data5/row" colgroup="12%,16%,15%,14%,14%,15%,14%" >
	<tr id="headerTr">
		<td class="head_ct">사이트</td>
		<td class="head_ct">금액</td>
		<td class="head_ct">클릭수</td>
		<td class="head_ct">노출수</td>
		<td class="head_ct">클릭률<br />(평균)</td>
		<td class="head_ct">평균클릭비용</td>
		<td class="head_ct">비고</td>
	</tr>
	<c:forEach
			items="${formBodyXML.getChildList('body/data5', 1, 1)}" var="row" varStatus="status" >
	<tr id="${status.last ? 'hiddenTr' : 'row'}" style="${status.last ? 'display:none' : ''}">
		<td class="body_ct"><c:if test="${formBodyMode ne 'edit' && !empty row.getAttr('erpValue1')}"
		><u:out value="${row.getAttr('erpValue1')}" /><u:input type="hidden" id="erpValue51_${status.index }" name="erpValue1" value="${row.getAttr('erpValue1')}" /></c:if
		><c:if test="${formBodyMode eq 'edit' || (formBodyMode ne 'view' && empty row.getAttr('erpValue1'))}"
		><u:input id="erpValue51_${status.index }" name="erpValue1" value="${row.getAttr('erpValue1')}" title="사이트" style="width:88%;" maxByte="100" 
		/></c:if></td>
		<td class="body_ct"><u:input id="erpValue52_${status.index }" name="erpValue2" value="${row.getAttr('erpValue2')}" title="금액" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue53_${status.index }" name="erpValue3" value="${row.getAttr('erpValue3')}" title="클릭수" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue54_${status.index }" name="erpValue4" value="${row.getAttr('erpValue4')}" title="노출수" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue55_${status.index }" name="erpValue5" value="${row.getAttr('erpValue5')}" title="클릭률(평균)" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue56_${status.index }" name="erpValue6" value="${row.getAttr('erpValue6')}" title="평균클릭비용" style="width:88%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue57_${status.index }" name="erpValue7" value="${row.getAttr('erpValue7')}" title="비고" style="width:88%;" maxByte="100" /></td>
	</tr></c:forEach>
</u:listArea>

<u:listArea>
<tr><td class="head_ct">내용</td></tr>
<tr><td class="body_lt"><c:if
	test="${not (formBodyMode eq 'pop' or formBodyCall eq 'ajax')}">
	<div id="xml-cont"><u:editor id="erpCont" width="100%" height="300px" module="ap" padding="2"
		value="${_bodyHtml}" noFocus="true"
	/></div><div id="xml-contView" class="editor" style="display:none"></div></c:if><c:if
	test="${formBodyMode eq 'pop' or formBodyCall eq 'ajax'}">
	<div id="xml-cont" class="listarea" style="width:100%; height:${empty namoEditorEnable ? 184 : 306}px; padding-top:2px"><u:editor
		id="erpCont" width="100%" height="${empty namoEditorEnable ? 180 : 300}px" module="ap" areaId="erpEditArea" 
		value="${_bodyHtml}" namoToolbar="${formBodyMode eq 'pop' ? 'wcPop' : ''}"
	/><div id="erpEditArea"></div></div>
	</c:if>
</td></tr>
</u:listArea>

</div>

<div class="blank"></div>

<c:if
	test="${formBodyMode eq 'pop'}">
<u:buttonArea>
	<u:button titleId="cm.btn.confirm" onclick="setErpXMLPop();" alt="확인" auth="W" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</c:if>

</div></c:if><c:if
	test="${formBodyMode eq 'view'}">
<div id="xmlArea">

<div id="xml-body">

<u:title title="1.사이트별 매출액" type="small" alt="1.사이트별 매출액" />

<u:listArea id="xml-dft" style="width:200px;" colgroup="40%,">
	<tr>
		<td class="head_ct">전체</td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/dft.totalCnt')}" /></td>
	</tr>
</u:listArea>

<u:listArea id="xml-data1/row" colgroup="12%,11%,11%,11%,11%,11%,11%,11%,11%" >
	<tr id="headerTr">
		<td class="head_ct">사이트</td>
		<td class="head_ct">자동백</td>
		<td class="head_ct">수동백</td>
		<td class="head_ct">부직/비닐백</td>
		<td class="head_ct">봉투</td>
		<td class="head_ct">식품포장지</td>
		<td class="head_ct">종이용품</td>
		<td class="head_ct">컵류</td>
		<td class="head_ct">포장용품</td>
	</tr>
	<c:forEach
			items="${formBodyXML.getChildList('body/data1', 1)}" var="row" varStatus="status" >
	<tr>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue1')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue2')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue3')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue4')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue5')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue6')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue7')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue8')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue9')}" /></td>
	</tr></c:forEach>
</u:listArea>

<u:listArea id="xml-data2/row" colgroup="12%,12%,12%,12%,12%,12%,12%," >
	<c:forEach
			items="${formBodyXML.getChildList('body/data2', 1)}" var="row" varStatus="status" >
	<c:if test="${status.index==0 }">
	<tr id="row">
		<td class="head_ct">사이트</td>
		<td class="head_ct">플라스틱용기</td>
		<td class="head_ct">박스류</td>
		<td class="head_ct">기타</td>
		<td class="head_ct"><u:out value="${row.getAttr('erpColm5')}" /></td>
		<td class="head_ct">건수</td>
		<td class="head_ct">금액</td>
		<td class="head_ct">월계</td>
	</tr></c:if><c:if test="${status.index>0 }">
	<tr>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue1')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue2')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue3')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue4')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue5')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue6')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue7')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue8')}" /></td>
	</tr></c:if></c:forEach>
	<tr id="xml-data21">
		<td class="head_ct">합계</td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/data21.erpValue2')}" /></td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/data21.erpValue3')}" /></td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/data21.erpValue4')}" /></td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/data21.erpValue5')}" /></td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/data21.erpValue6')}" /></td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/data21.erpValue7')}" /></td>
		<td class="body_ct" rowspan="2"><u:out value="${formBodyXML.getAttr('body/data21.erpValue8')}" /></td>
	</tr>
	<tr id="xml-data22">
		<td class="head_ct">월 합계</td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/data22.erpValue2')}" /></td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/data22.erpValue3')}" /></td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/data22.erpValue4')}" /></td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/data22.erpValue5')}" /></td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/data22.erpValue6')}" /></td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/data22.erpValue7')}" /></td>
	</tr>
</u:listArea>

<u:blank />
<u:title title="2.맞춤제작" type="small" alt="2.맞춤제작" />
<u:listArea id="xml-data3/row" colgroup="12%,16%,15%,14%,14%,15%,14%" >
	<tr id="headerTr">
		<td class="head_ct">사이트</td>
		<td class="head_ct">요청사항</td>
		<td class="head_ct">품명</td>
		<td class="head_ct">수량</td>
		<td class="head_ct">금액</td>
		<td class="head_ct">업체명</td>
		<td class="head_ct">문의경로</td>
	</tr>
	<c:forEach
			items="${formBodyXML.getChildList('body/data3', 1)}" var="row" varStatus="status" >
	<tr>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue1')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue2')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue3')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue4')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue5')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue6')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue7')}" /></td>
	</tr></c:forEach>
</u:listArea>


<u:blank />
<u:title title="3.일일 CS 현황" type="small" alt="3.일일 CS 현황" />
<c:set var="data4TotalCnt" value="${fn:length(formBodyXML.getChildList('body/data4', 1)) }"/>
<u:listArea id="xml-data4/row" colgroup="12%,16%,15%,14%,14%,15%,14%" >
	<tr id="headerTr">
		<td class="head_ct">사이트</td>
		<td class="head_ct">품명</td>
		<td class="head_ct">수량</td>
		<td class="head_ct">문의사항</td>
		<td class="head_ct">처리현황</td>
		<td class="head_ct">문의경로</td>
		<td class="head_ct">비고</td>
	</tr>
	<c:forEach
			items="${formBodyXML.getChildList('body/data4', 1)}" var="row" varStatus="status" >
	<tr>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue1')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue2')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue3')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue4')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue5')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue6')}" /></td>
		<c:if test="${status.index==0 }"><td class="body_ct" rowspan="${data4TotalCnt }"><u:out value="${row.getAttr('erpValue7')}" /></td></c:if>
	</tr></c:forEach>
	<tr id="xml-data41">
		<td class="head_ct">합계</td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/data41.erpValue42')}" /></td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/data41.erpValue43')}" /></td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/data41.erpValue44')}" /></td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/data41.erpValue45')}" /></td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/data41.erpValue46')}" /></td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/data41.erpValue47')}" /></td>
	</tr>
</u:listArea>

<u:blank />
<u:title title="4.광고비 지출 현황" type="small" alt="4.맞춤제작" />
<u:listArea id="xml-data5/row" colgroup="12%,16%,15%,14%,14%,15%,14%" >
	<tr id="headerTr">
		<td class="head_ct">사이트</td>
		<td class="head_ct">금액</td>
		<td class="head_ct">클릭수</td>
		<td class="head_ct">노출수</td>
		<td class="head_ct">클릭률<br />(평균)</td>
		<td class="head_ct">평균클릭비용</td>
		<td class="head_ct">비고</td>
	</tr>
	<c:forEach
			items="${formBodyXML.getChildList('body/data5', 1)}" var="row" varStatus="status" >
	<tr>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue1')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue2')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue3')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue4')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue5')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue6')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue7')}" /></td>
	</tr></c:forEach>
</u:listArea>



<u:listArea>
<tr><td class="head_ct">내용</td></tr>
<tr><td class="body_lt editor">${formBodyXML.getAttr('body/cont.erpCont')}</td></tr>
</u:listArea>

</div>

<div class="blank"></div>

</div>
</c:if>