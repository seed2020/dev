<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
//양식명 : 반입 보고서

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
	editor('erpCont').prepare();
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
	var len=$("#"+conId+" tbody:first tr").not('#hiddenTr').length;
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
	var seq=$("#"+conId+" tbody:first tr").not('#hiddenTr').length;
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
function clearFormEditor(){
	editor('erpCont').clean(); unloadEvent.removeEditor('erpCont');
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

<u:listArea id="xml-daily" colgroup="13%,37%,13%,37%">
	<tr>
		<td class="head_ct">반입일자</td>
		<td class="body_lt"><u:calendar id="erpValue11" title="일자" value="${formBodyXML.getAttr('body/daily.erpValue11')}" /></td>
		<td class="head_ct">반입업체</td>
		<td class="body_lt"><u:input id="erpValue12" value="${formBodyXML.getAttr('body/daily.erpValue12')}" title="반입업체" style="width:95%;" maxByte="100" /></td>
	</tr>
</u:listArea>
<u:set var="rowStrtCnt" test="${empty formBodyXML.getChildList('body/rows') }" value="${initStrtCnt }" elseValue="0"/><!-- 추가 Row수 -->
<div style="display:block;float:right;height:25px;"><u:buttonS title="행추가" onclick="addRow('xml-rows\\/row', 'row', '${rowStrtCnt }');" alt="행추가"/><u:buttonS title="행삭제" onclick="delRow('xml-rows/row');" alt="행삭제"/></div>
<u:listArea id="xml-rows/row" colgroup="12%,12%,12%,12%,12%,12%,12%,*">
	<tr id="headerTr">
		<td class="head_ct">품번</td>
		<td class="head_ct">품명</td>
		<td class="head_ct">규격</td>
		<td class="head_ct">반입수량(매)</td>
		<td class="head_ct">박스</td>
		<td class="head_ct">반입내역</td>
		<td class="head_ct">기타</td>
		<td class="head_ct"><div id="xml-colm"><u:input id="erpColm" value="${formBodyXML.getAttr('body/colm.erpColm')}" title="${formBodyXML.getAttr('body/colm.erpColm')}" style="width:85%;" maxByte="100" /></div></td>
	</tr>
	<c:forEach
			items="${formBodyXML.getChildList('body/rows', 1, 1)}" var="row" varStatus="status" >
	<tr id="${status.last ? 'hiddenTr' : 'row'}" style="${status.last ? 'display:none' : ''}">
		<td class="body_ct"><u:input id="erpValue1_${status.index }" name="erpValue1" value="${row.getAttr('erpValue1')}" title="품번" style="width:90%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue2_${status.index }" name="erpValue2" value="${row.getAttr('erpValue2')}" title="품명" style="width:90%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue3_${status.index }" name="erpValue3" value="${row.getAttr('erpValue3')}" title="규격" style="width:90%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue4_${status.index }" name="erpValue4" value="${row.getAttr('erpValue4')}" title="반입수량(매)" style="width:90%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue5_${status.index }" name="erpValue5" value="${row.getAttr('erpValue5')}" title="박스" style="width:90%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue6_${status.index }" name="erpValue6" value="${row.getAttr('erpValue6')}" title="반입내역" style="width:90%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue7_${status.index }" name="erpValue7" value="${row.getAttr('erpValue7')}" title="기타" style="width:90%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue8_${status.index }" name="erpValue8" value="${row.getAttr('erpValue8')}" title="${formBodyXML.getAttr('body/rows.erpColm')}" style="width:85%;" maxByte="100" /></td>
	</tr></c:forEach>
</u:listArea>
<u:listArea>
<tr><td class="head_ct">내용</td></tr>
<tr><td class="body_lt"><c:if
	test="${not (formBodyMode eq 'pop' or formBodyCall eq 'ajax')}">
	<div id="xml-cont"><u:editor id="erpCont" width="100%" height="300px" module="ap" padding="2"
		value="${_bodyHtml}" noFocus="${not empty param.apvLnGrpId}"
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
<u:listArea colgroup="13%,37%,13%,37%">
	<tr>
		<td class="head_ct">반입일자</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue11')}" /></td>
		<td class="head_ct">반입업체</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue12')}" /></td>
	</tr>
</u:listArea>
<u:listArea colgroup="12%,12%,12%,12%,12%,12%,12%,*" >
	<tr id="headerTr">
		<td class="head_ct">품번</td>
		<td class="head_ct">품명</td>
		<td class="head_ct">규격</td>
		<td class="head_ct">반입수량(매)</td>
		<td class="head_ct">박스</td>
		<td class="head_ct">반입내역</td>
		<td class="head_ct">기타</td>
		<td class="head_ct"><u:out value="${formBodyXML.getAttr('body/colm.erpColm')}" /></td>
	</tr>
	<c:forEach
			items="${formBodyXML.getChildList('body/rows', 1)}" var="row" varStatus="status" >
	<tr>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue1')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue2')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue3')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue4')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue5')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue6')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue7')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue8')}" /></td>
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