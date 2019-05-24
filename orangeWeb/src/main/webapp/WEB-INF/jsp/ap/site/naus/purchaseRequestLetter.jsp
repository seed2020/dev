<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
// 양식명 : 구매품의서

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
	<input type="hidden" name="typId" value="purchaseRequestLetter"/>
	<input type="hidden" name="ver" value="1"/>
</div>

<div id="xml-body">
<u:listArea id="xml-daily" colgroup="15%,">
	<tr>
		<td class="head_ct">품의내역</td>
		<td class="body_lt"><u:input id="erpValue1" value="${formBodyXML.getAttr('body/daily.erpValue1')}" title="품의내역" style="width:98%;" maxByte="400" /></td>		
	</tr>
	<tr>
		<td class="head_ct">구매비용</td>
		<td class="body_lt"><u:input id="erpValue2" value="${formBodyXML.getAttr('body/daily.erpValue2')}" title="구매비용" style="width:98%;" maxByte="100" /></td>
	</tr>
	<tr>
		<td class="head_ct">구매대행업체</td>
		<td class="body_lt"><u:input id="erpValue3" value="${formBodyXML.getAttr('body/daily.erpValue3')}" title="구매대행업체" style="width:98%;" maxByte="200" /></td>		
	</tr>
	<tr>
		<td class="head_ct">결제방법</td>
		<td class="body_lt"><u:input id="erpValue4" value="${formBodyXML.getAttr('body/daily.erpValue4')}" title="결제방법" style="width:98%;" maxByte="200" /></td>
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
<u:listArea id="xml-daily" colgroup="15%," >
	<tr>
		<td class="head_ct">품의내역</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue1')}" /></td>		
	</tr>
	<tr>
		<td class="head_ct">구매비용</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue2')}" /></td>
	</tr>
	<tr>
		<td class="head_ct">구매대행업체</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue3')}" /></td>		
	</tr>
	<tr>
		<td class="head_ct">결제방법</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue4')}" /></td>
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