<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
//양식명 : 지출 결의서

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
<!--
function setCurrency(obj, str){
	if($(obj).val()!='' && !$(obj).val().startsWith(str)){
		$(obj).val(str+$(obj).val());
	}
}<%//xml 수집 전에 호출함 %>
function checkFormBodyXML(){
	editor('erpCont').prepare();
	return true;
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
	<input type="hidden" name="typId" value="dailyProductionStatus"/>
	<input type="hidden" name="ver" value="1"/>
</div>

<div id="xml-body">
<u:listArea id="xml-daily" colgroup="12%,13%,12%,13%,12%,13%,12%,13%" >
	<tr>
		<td class="head_ct">지급총액</td>
		<td class="body_lt"><u:input id="erpValue1" value="${formBodyXML.getAttr('body/daily.erpValue1')}" title="지급총액" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" valueAllowed="."/></td>
		<td class="head_ct" colspan="6">외화지급액</td>		
	</tr>
	<tr>
		<td class="head_ct">예금 지급액</td>
		<td class="body_lt"><u:input id="erpValue2" value="${formBodyXML.getAttr('body/daily.erpValue2')}" title="예금 지급액" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" valueAllowed="."/></td>
		<td class="head_ct">US $</td>
		<td class="body_lt"><u:input id="erpValue3" value="${formBodyXML.getAttr('body/daily.erpValue3')}" title="US $" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" valueAllowed=".$" onchange="setCurrency(this, '$');" /></td>
		<td class="head_ct">EUR</td>
		<td class="body_lt"><u:input id="erpValue4" value="${formBodyXML.getAttr('body/daily.erpValue4')}" title="EUR" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" valueAllowed=".€" onchange="setCurrency(this, '€');" /></td>
		<td class="head_ct">YEN</td>
		<td class="body_lt"><u:input id="erpValue5" value="${formBodyXML.getAttr('body/daily.erpValue5')}" title="YEN" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" valueAllowed=".￥" onchange="setCurrency(this, '￥');" /></td>		
	</tr>
	<tr>
		<td class="head_ct">자동이체<br />예정액</td>
		<td class="body_lt"><u:input id="erpValue6" value="${formBodyXML.getAttr('body/daily.erpValue6')}" title="자동이체 예정액" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" valueAllowed="." /></td>
		<td class="head_ct"><u:input id="erpColm7" value="${formBodyXML.getAttr('body/daily.erpColm7')}" title="${formBodyXML.getAttr('body/daily.erpColm7')}" style="width:85%;" maxByte="100" /></td>
		<td class="body_lt"><u:input id="erpValue7" value="${formBodyXML.getAttr('body/daily.erpValue7')}" title="${formBodyXML.getAttr('body/daily.erpColm7')}" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" valueAllowed=".￦" onchange="setCurrency(this, '￦');" /></td>
		<td class="head_ct"><u:input id="erpColm8" value="${formBodyXML.getAttr('body/daily.erpColm8')}" title="${formBodyXML.getAttr('body/daily.erpColm8')}" style="width:85%;" maxByte="100" /></td>
		<td class="body_lt"><u:input id="erpValue8" value="${formBodyXML.getAttr('body/daily.erpValue8')}" title="${formBodyXML.getAttr('body/daily.erpColm8')}" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" valueAllowed=".￦" onchange="setCurrency(this, '￦');" /></td>
		<td class="head_ct"><u:input id="erpColm9" value="${formBodyXML.getAttr('body/daily.erpColm9')}" title="${formBodyXML.getAttr('body/daily.erpColm9')}" style="width:85%;" maxByte="100" /></td>
		<td class="body_lt"><u:input id="erpValue9" value="${formBodyXML.getAttr('body/daily.erpValue9')}" title="${formBodyXML.getAttr('body/daily.erpColm9')}" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" valueAllowed=".￦" onchange="setCurrency(this, '￦');" /></td>
	</tr>
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
<u:listArea id="xml-daily" colgroup="12%,13%,12%,13%,12%,13%,12%,13%" >
	<tr>
		<td class="head_ct">지급총액</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue1')}" /></td>
		<td class="head_ct" colspan="6">외화지급액</td>		
	</tr>
	<tr>
		<td class="head_ct">예금 지급액</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue2')}" /></td>
		<td class="head_ct">US $</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue3')}" /></td>
		<td class="head_ct">EUR</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue4')}" /></td>
		<td class="head_ct">YEN</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue5')}" /></td>		
	</tr>
	<tr>
		<td class="head_ct">자동이체<br />예정액</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue6')}" /></td>
		<td class="head_ct"><u:out value="${formBodyXML.getAttr('body/daily.erpColm7')}" /></td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue7')}" /></td>
		<td class="head_ct"><u:out value="${formBodyXML.getAttr('body/daily.erpColm8')}" /></td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue8')}" /></td>
		<td class="head_ct"><u:out value="${formBodyXML.getAttr('body/daily.erpColm9')}" /></td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue9')}" /></td>
	</tr>
</u:listArea>

<u:listArea>
<tr><td class="head_ct">내용</td></tr>
<tr><td class="body_lt editor">${formBodyXML.getAttr('body/cont.erpCont')}</td></tr>
</u:listArea>

</div>

<div class="blank"></div>

</div>
</c:if>