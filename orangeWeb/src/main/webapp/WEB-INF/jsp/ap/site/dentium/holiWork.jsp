<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
//양식명 : 휴일근무 신청서

// 초기 row수
request.setAttribute("initStrtCnt", "1");
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
	return true;
}<%// 선택삭제%>
function delSelRow(conId){
	var arr = getCheckedTrs(conId, "cm.msg.noSelect");
	if(arr!=null) {
		delRowInArr(arr);
		<c:if test="${formBodyMode eq 'pop'}">dialog.resize("setDocBodyHtmlDialog");</c:if>
	}
}
<%// 행삭제%>
function delRow(conId){
	conId=conId.replace('/','\\/');
	var len=$("#"+conId+" tbody:first tr").not('#headerTr, #hiddenTr').length;
	if(len<2){
		alert('최소 1줄 이상 입력해야 합니다.');
		return;
	}
	var $tr = $("#"+conId+" tbody:first #hiddenTr").prev();
	delRowInArr([$tr[0]]);
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
<%// 다음 Row 번호 %>
var gMaxRow = parseInt("${fn:length(formBodyXML.getChildList('body/rows'))}");
<%// 행추가%>
function addRow(conId, trId, strtCnt){
	if(strtCnt==undefined) strtCnt = 0;
	strtCnt++;
	conId=conId.replace('/','\\/');
	var $tr = $("#"+conId+" tbody:first #hiddenTr");
	var div=$tr.closest('div');
	$.uniform.restore(div.find('input, textarea, select, button'));
	var html = $tr[0].outerHTML;
	html = html.replace(/_NO/g,'_'+(gMaxRow+parseInt(strtCnt)));
	gMaxRow++;
	$tr.before(html);
	$tr = $tr.prev();
	$tr.attr('id',trId);
	$tr.attr('style','');
	var seq=$("#"+conId+" tbody:first tr").not('#headerTr, #hiddenTr').length;
	$tr.find('td#seq').text(seq++);
	div.find("input, textarea, select, button").uniform();
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
<% // 시간입력 시 근무시간 변경%>
function chnTime(id){	
	var val=$('#'+id).val();
	var td=$('#'+id).closest('td');
	if(val==''){
		td.next().find('input[name="erpValue8"]').val('');
	}else{
		var times=val.split(':');
		var workTime=parseInt(times[0])+4;
		if(workTime==24)
			workTime=0;
		td.next().find('input[name="erpValue8"]').val((workTime>24 ? '0'+(workTime-24) : workTime<10 ? '0'+workTime : workTime)+':'+times[1]);
	}
}
<% // 요일 변경%>
function chnSelectDay(obj){
	var val=$(obj).val();
	var tr=$(obj).closest('tr');
	tr.find('div#timeSelectArea, div#timeInputArea').hide();
	tr.find('div#timeSelectArea, div#timeInputArea').find(':text').val('');
	if(val=='sat'){
		tr.find('div#timeInputArea').show();
		var id=tr.find('td#chnTimeSelector input[name="erpValue6"]').attr('id');
		chnTime(id);
	}else if(val=='sun'){
		tr.find('div#timeSelectArea').show();
	}
	
}

$(document).ready(function(){
});

-->
</script><c:if

	test="${formBodyMode ne 'view'}">
<div id="xmlArea" style="${formBodyMode eq 'pop' ? 'width:900px;' : ''}">

<div id="xml-head">
	<input type="hidden" name="typId" value="carryInReport"/>
	<input type="hidden" name="ver" value="1"/>
</div>

<div id="xml-body">

<u:listArea id="xml-dft" colgroup="13%,">
	<tr>
		<td class="head_ct">부서</td>
		<td class="body_lt"><u:input id="erpValue11" value="${formBodyMode ne 'edit' && empty formBodyXML.getAttr('body/dft.erpValue11') ? sessionScope.userVo.deptNm : formBodyXML.getAttr('body/dft.erpValue11')}" title="부서" style="width:90%;" maxByte="200" /></td>
	</tr>
</u:listArea>

<u:set var="rowStrtCnt" test="${empty formBodyXML.getChildList('body/rows') }" value="${initStrtCnt }" elseValue="0"/><!-- 추가 Row수 -->
<div style="display:block;float:left;height:25px;"class="red_txt">※ 토요일 인정 근무시간 : 4시간</div>
<div style="display:block;float:right;height:25px;"><u:buttonS title="행추가" onclick="addRow('xml-rows\\/row', 'row', '${rowStrtCnt }');" alt="행추가"/><u:buttonS title="행삭제" onclick="delRow('xml-rows/row');" alt="행삭제"/></div>
<u:listArea id="xml-rows/row" colgroup="12%,12%,,12%,12%,12%,12%">
	<tr id="headerTr">
		<td class="head_ct">성명</td>
		<td class="head_ct">직급</td>
		<td class="head_ct">사유(구체적으로 기술)</td>
		<td class="head_ct">예정일</td>
		<td class="head_ct">요일</td>
		<td class="head_ct">출근시간</td>
		<td class="head_ct">근무시간</td>
	</tr>
	<c:forEach
			items="${formBodyXML.getChildList('body/rows', 1, 1)}" var="row" varStatus="status" >
	<tr id="${status.last ? 'hiddenTr' : 'row'}" style="${status.last ? 'display:none' : ''}">
		<td class="body_ct"><u:input id="erpValue1_${status.index }" name="erpValue1" value="${row.getAttr('erpValue1')}" title="성명" style="width:90%;" maxByte="100" dataList="${formBodyMode ne 'edit' ? 'data-validation=\"Y\"' : ''}"/></td>
		<td class="body_ct"><u:input id="erpValue2_${status.index }" name="erpValue2" value="${row.getAttr('erpValue2')}" title="직급" style="width:90%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue3_${status.index }" name="erpValue3" value="${row.getAttr('erpValue3')}" title="사유" style="width:90%;" maxByte="300" /></td>
		<td class="body_ct"><u:input id="erpValue4_${status.index }" name="erpValue4" value="${row.getAttr('erpValue4')}" title="예정일" style="width:90%;" maxByte="20" /></td>
		<td class="body_ct"><select id="erpValue5_${status.index }" name="erpValue5" onchange="chnSelectDay(this)"
		><u:option value="sat" title="토요일" selected="${empty row.getAttr('erpValue5') && status.index==0}" checkValue="${row.getAttr('erpValue5')}"
		/><u:option value="sun" title="일요일" checkValue="${row.getAttr('erpValue5')}"/>
		</select></td>
		<td class="body_ct" id="chnTimeSelector"><u:calendartime id="erpValue6_${status.index }" name="erpValue6" value="${row.getAttr('erpValue6')}" title="출근시간" type="time" timeUnit="10" timeStrt="8" timeHandler="chnTime"/></td>
		<td class="body_ct"><div id="timeSelectArea"<c:if test="${empty row.getAttr('erpValue5') || row.getAttr('erpValue5') eq 'sat'}"> style="display:none;"</c:if>><u:calendartime id="erpValue7_${status.index }" name="erpValue7" value="${row.getAttr('erpValue7')}" title="근무시간" type="time" timeUnit="10" timeStrt="8"
		/></div><div id="timeInputArea"<c:if test="${!empty row.getAttr('erpValue5') && row.getAttr('erpValue5') eq 'sun'}"> style="display:none;"</c:if>><u:input id="erpValue8_${status.index }" name="erpValue8" value="${row.getAttr('erpValue8')}" title="근무시간" style="width:90%;text-align:center;" maxByte="20" readonly="Y"/></div></td>
	</tr></c:forEach>
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
<div>

<div>

<u:listArea colgroup="13%,">
	<tr>
		<td class="head_ct">부서</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/dft.erpValue11')}" /></td>
	</tr>
</u:listArea>
<div style="display:block;float:left;height:25px;"class="red_txt">※ 토요일 인정 근무시간 : 4시간</div>
<u:listArea colgroup="12%,12%,,12%,12%,12%">
	<tr id="headerTr">
		<td class="head_ct">성명</td>
		<td class="head_ct">직급</td>
		<td class="head_ct">사유(구체적으로 기술)</td>
		<td class="head_ct">예정일</td>
		<td class="head_ct">요일</td>
		<td class="head_ct">출근시간</td>
		<td class="head_ct">근무시간</td>
	</tr>
	<c:forEach
			items="${formBodyXML.getChildList('body/rows')}" var="row" varStatus="status" >
	<tr>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue1')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue2')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue3')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue4')}" /></td>
		<td class="body_ct"><c:if test="${row.getAttr('erpValue5') eq 'sat'}">토요일</c:if><c:if test="${row.getAttr('erpValue5') eq 'sun'}">일요일</c:if></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue6')}" /></td>
		<td class="body_ct"><c:if test="${row.getAttr('erpValue5') eq 'sat'}"><u:out value="${row.getAttr('erpValue8')}" /></c:if><c:if test="${row.getAttr('erpValue5') eq 'sun'}"><u:out value="${row.getAttr('erpValue7')}" /></c:if></td>
	</tr></c:forEach>
</u:listArea>

</div>

<div class="blank"></div>

</div>
</c:if>