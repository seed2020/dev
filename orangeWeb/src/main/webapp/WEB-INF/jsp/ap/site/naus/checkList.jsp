<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
/* 양식명 : 
	부대시설위생점검표
	온,습도/조도 점검표
	입고검사
	작업장위생관리점검표
	개인위생관리점검표
	엄체평가보고서
	배송차량위생점검표 
*/

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
<%//xml 수집 전에 호출함 %>
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
	<input type="hidden" name="typId" value="checkList"/>
	<input type="hidden" name="ver" value="1"/>
</div>

<div id="xml-body">
<u:listArea id="xml-daily" colgroup="13%,37%,13%,37%" >
	<tr>
		<td class="head_ct">제정일자</td>
		<td class="body_lt" colspan="3"><u:calendar id="erpValue7" title="제정일자" value="${formBodyXML.getAttr('body/daily.erpValue7')}"/></td>
	</tr>
	<tr>
		<td class="head_ct">개정일자</td>
		<td class="body_lt"><u:calendar id="erpValue1" title="개정일자" value="${formBodyXML.getAttr('body/daily.erpValue1')}" /></td>
		<td class="head_ct">개정번호</td>
		<td class="body_lt"><u:input id="erpValue2" value="${formBodyXML.getAttr('body/daily.erpValue2')}" title="개정번호" style="width:95%;" maxByte="100" /></td>
	</tr>
	<tr>
		<td class="head_ct">점검일자</td>
		<td class="body_lt"><u:calendar id="erpValue3" title="점검일자" value="${formBodyXML.getAttr('body/daily.erpValue3')}" /></td>
		<td class="head_ct">점검자</td>
		<td class="body_lt"><u:input id="erpValue4" value="${formBodyXML.getAttr('body/daily.erpValue4')}" title="점검자" style="width:95%;" maxByte="100" /></td>
	</tr>
	<tr>
		<td class="head_ct">점검주기</td>
		<td class="body_lt"><c:if test="${formBodyMode ne 'edit' && !empty formBodyXML.getAttr('body/daily.erpValue5')}"><u:out value="${formBodyXML.getAttr('body/daily.erpValue5')}" /><u:input type="hidden" id="erpValue5" value="${formBodyXML.getAttr('body/daily.erpValue5')}" /></c:if
		><c:if test="${formBodyMode eq 'edit' || (formBodyMode ne 'view' && empty formBodyXML.getAttr('body/daily.erpValue5'))}"><u:input id="erpValue5" value="${formBodyXML.getAttr('body/daily.erpValue5')}" title="점검주기" style="width:90%;" maxByte="100" /></c:if></td>
		<td class="head_ct">범례</td>
		<td class="body_lt"><c:if test="${formBodyMode ne 'edit' && !empty formBodyXML.getAttr('body/daily.erpValue6')}"><u:out value="${formBodyXML.getAttr('body/daily.erpValue6')}" /><u:input type="hidden" id="erpValue6" value="${formBodyXML.getAttr('body/daily.erpValue6')}" /></c:if
		><c:if test="${formBodyMode eq 'edit' || (formBodyMode ne 'view' && empty formBodyXML.getAttr('body/daily.erpValue6'))}"><u:input id="erpValue6" value="${formBodyXML.getAttr('body/daily.erpValue6')}" title="범례" style="width:90%;" maxByte="100" /></c:if></td>
	</tr>
</u:listArea>
<c:if
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
<u:listArea id="xml-daily" colgroup="13%,37%,13%,37%" noBottomBlank="true">
	<tr>
		<td class="head_ct">제정일자</td>
		<td class="body_lt" colspan="3"><u:out value="${formBodyXML.getAttr('body/daily.erpValue7')}" /></td>
	</tr>
	<tr>
		<td class="head_ct">개정일자</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue1')}" /></td>
		<td class="head_ct">개정번호</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue2')}" /></td>
	</tr>
	<tr>
		<td class="head_ct">점검일자</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue3')}" /></td>
		<td class="head_ct">점검자</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue4')}" /></td>
	</tr>
	<tr>
		<td class="head_ct">점검주기</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue5')}" /></td>
		<td class="head_ct">범례</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue6')}" /></td>
	</tr>
	<tr>
		<td class="body_lt editor" colspan="4">${formBodyXML.getAttr('body/cont.erpCont')}</td>
	</tr>
</u:listArea>
	
</div>

<div class="blank"></div>

</div>
</c:if>