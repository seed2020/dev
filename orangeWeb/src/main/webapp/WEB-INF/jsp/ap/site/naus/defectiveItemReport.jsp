<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
//양식명 : 부적합품 보고서

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
	<input type="hidden" name="typId" value="defectiveItemReport"/>
	<input type="hidden" name="ver" value="1"/>
</div>

<div id="xml-body">
<u:listArea id="xml-daily" colgroup="13%,20%,13%,21%,13%,20%" >
	<tr>
		<td class="head_ct">제품명</td>
		<td class="body_lt"><u:input id="erpValue1" value="${formBodyXML.getAttr('body/daily.erpValue1')}" title="제품명" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct">규격</td>
		<td class="body_lt"><u:input id="erpValue2" value="${formBodyXML.getAttr('body/daily.erpValue2')}" title="규격" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct">원지</td>
		<td class="body_lt"><u:input id="erpValue3" value="${formBodyXML.getAttr('body/daily.erpValue3')}" title="원지" style="width:90%;" maxByte="100" /></td>
	</tr>
	<tr>
		<td class="head_ct">작업일</td>
		<td class="body_lt"><u:calendar id="erpValue4" title="작업일" value="${formBodyXML.getAttr('body/daily.erpValue4')}" /></td>
		<td class="head_ct">작업수량</td>
		<td class="body_lt"><u:input id="erpValue5" value="${formBodyXML.getAttr('body/daily.erpValue5')}" title="작업수량" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct">불량수량</td>
		<td class="body_lt"><u:input id="erpValue6" value="${formBodyXML.getAttr('body/daily.erpValue6')}" title="불량수량" style="width:90%;" maxByte="100" /></td>
	</tr>
	<tr>
		<td class="head_ct">불량코드</td>
		<td class="body_lt"><u:input id="erpValue7" value="${formBodyXML.getAttr('body/daily.erpValue7')}" title="불량코드" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct">현장작성자</td>
		<td class="body_lt"><u:input id="erpValue8" value="${formBodyXML.getAttr('body/daily.erpValue8')}" title="현장작성자" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct">누적순번</td>
		<td class="body_lt"><u:input id="erpValue9" value="${formBodyXML.getAttr('body/daily.erpValue9')}" title="누적순번" style="width:90%;" maxByte="100" /></td>
	</tr>
	<tr>
		<td class="head_ct">불량내역</td>
		<td class="body_lt" colspan="3"><u:input id="erpValue10" value="${formBodyXML.getAttr('body/daily.erpValue10')}" title="불량내역" style="width:96%;" maxByte="100" /></td>
		<td class="head_ct">제품코드</td>
		<td class="body_lt"><u:input id="erpValue11" value="${formBodyXML.getAttr('body/daily.erpValue11')}" title="제품코드" style="width:90%;" maxByte="100" /></td>
	</tr>
</u:listArea>
<u:set var="rowStrtCnt" test="${empty formBodyXML.getChildList('body/rows') }" value="${initStrtCnt }" elseValue="0"/><!-- 추가 Row수 -->
<div style="display:block;float:right;height:25px;"><u:buttonS title="행추가" onclick="addRow('xml-rows\\/row', 'row', '${rowStrtCnt }');" alt="행추가"/><u:buttonS title="행삭제" onclick="delRow('xml-rows/row');" alt="행삭제"/></div>
<u:listArea id="xml-rows/row" colgroup="12%,12%,12%,12%,12%,12%,12%,*" >
	<tr id="headerTr">
		<td class="head_ct" colspan="6">손실비용</td>
	</tr>
	<tr id="headerTr">
		<td class="head_ct">원지손실비용</td>
		<td class="head_ct">인쇄손실비용</td>
		<td class="head_ct">제대비</td>
		<td class="head_ct">부재료비</td>
		<td class="head_ct">합계</td>
		<td class="head_ct">비고</td>
	</tr>
	<c:forEach
			items="${formBodyXML.getChildList('body/rows', 1, 1)}" var="row" varStatus="status" >
	<tr id="${status.last ? 'hiddenTr' : 'row'}" style="${status.last ? 'display:none' : ''}">
		<td class="body_ct"><u:input id="erpValue21_${status.index }" name="erpValue21" value="${row.getAttr('erpValue21')}" title="원지손실비용" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
		<td class="body_ct"><u:input id="erpValue22_${status.index }" name="erpValue22" value="${row.getAttr('erpValue22')}" title="인쇄손실비용" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
		<td class="body_ct"><u:input id="erpValue23_${status.index }" name="erpValue23" value="${row.getAttr('erpValue23')}" title="제대비" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
		<td class="body_ct"><u:input id="erpValue24_${status.index }" name="erpValue24" value="${row.getAttr('erpValue24')}" title="부재료비" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
		<td class="body_ct"><u:input id="erpValue25_${status.index }" name="erpValue25" value="${row.getAttr('erpValue25')}" title="합계" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
		<td class="body_ct"><u:input id="erpValue26_${status.index }" name="erpValue26" value="${row.getAttr('erpValue26')}" title="비고" style="width:90%;" maxByte="100" /></td>
	</tr></c:forEach>
</u:listArea>
<u:listArea>
<tr><td class="head_ct">손실경위 세부사항</td></tr>
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

<u:listArea id="xml-etc" colgroup="13%,67%,20%" noBottomBlank="true">
	<tr>
		<td class="head_ct" colspan="2">사후처리내역</td>
		<td class="head_ct">결재</td>
	</tr>
	<tr>
		<td class="head_ct">담당</td>
		<td class="body_lt"><u:input id="erpValue41" value="${formBodyXML.getAttr('body/etc.erpValue41')}" title="담당" style="width:97%;" maxByte="100" /></td>
		<td class="body_lt"><u:input id="erpValue42" value="${formBodyXML.getAttr('body/etc.erpValue42')}" title="결재" style="width:90%;" maxByte="100" /></td>
	</tr>
	<tr>
		<td class="head_ct">범칙금해당자</td>
		<td class="body_lt"><u:input id="erpValue43" value="${formBodyXML.getAttr('body/etc.erpValue43')}" title="범칙금해당자" style="width:97%;" maxByte="200" /></td>
		<td class="body_lt"><u:input id="erpValue44" value="${formBodyXML.getAttr('body/etc.erpValue44')}" title="결재" style="width:90%;" maxByte="100" /></td>
	</tr>
	<tr>
		<td class="head_ct">범칙금액</td>
		<td class="body_lt"><u:input id="erpValue45" value="${formBodyXML.getAttr('body/etc.erpValue45')}" title="범칙금액" style="width:97%;" maxByte="100" /></td>
		<td class="body_lt"><u:input id="erpValue46" value="${formBodyXML.getAttr('body/etc.erpValue46')}" title="결재" style="width:90%;" maxByte="100" /></td>
	</tr>
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
<u:listArea colgroup="13%,20%,13%,21%,13%,20%" >
	<tr>
		<td class="head_ct">제품명</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue1')}" /></td>
		<td class="head_ct">규격</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue2')}" /></td>
		<td class="head_ct">원지</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue3')}" /></td>
	</tr>
	<tr>
		<td class="head_ct">작업일</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue4')}" /></td>
		<td class="head_ct">작업수량</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue5')}" /></td>
		<td class="head_ct">불량수량</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue6')}" /></td>
	</tr>
	<tr>
		<td class="head_ct">불량코드</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue7')}" /></td>
		<td class="head_ct">현장작성자</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue8')}" /></td>
		<td class="head_ct">누적순번</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue9')}" /></td>
	</tr>
	<tr>
		<td class="head_ct">불량내역</td>
		<td class="body_lt" colspan="3"><u:out value="${formBodyXML.getAttr('body/daily.erpValue10')}" /></td>
		<td class="head_ct">제품코드</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue11')}" /></td>
	</tr>
</u:listArea>
<u:listArea colgroup="12%,12%,12%,12%,12%,12%,12%,*" >
	<tr id="headerTr">
		<td class="head_ct" colspan="6">손실비용</td>
	</tr>
	<tr id="headerTr">
		<td class="head_ct">원지손실비용</td>
		<td class="head_ct">인쇄손실비용</td>
		<td class="head_ct">제대비</td>
		<td class="head_ct">부재료비</td>
		<td class="head_ct">합계</td>
		<td class="head_ct">비고</td>
	</tr>
	<c:forEach
			items="${formBodyXML.getChildList('body/rows', 1)}" var="row" varStatus="status" >
	<tr>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue21')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue22')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue23')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue24')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue25')}" /></td>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue26')}" /></td>
	</tr></c:forEach>
</u:listArea>

<u:listArea>
<tr><td class="head_ct">손실경위 세부사항</td></tr>
<tr><td class="body_lt editor">${formBodyXML.getAttr('body/cont.erpCont')}</td></tr>
</u:listArea>

<u:listArea id="xml-etc" colgroup="13%,67%,20%" >
	<tr>
		<td class="head_ct" colspan="2">사후처리내역</td>
		<td class="head_ct">결재</td>
	</tr>
	<tr>
		<td class="head_ct">담당</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/etc.erpValue41')}" /></td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/etc.erpValue42')}" /></td>
	</tr>
	<tr>
		<td class="head_ct">범칙금해당자</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/etc.erpValue43')}" /></td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/etc.erpValue44')}" /></td>
	</tr>
	<tr>
		<td class="head_ct">범칙금액</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/etc.erpValue45')}" /></td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/etc.erpValue46')}" /></td>
	</tr>
</u:listArea>	
	
</div>

<div class="blank"></div>

</div>
</c:if>