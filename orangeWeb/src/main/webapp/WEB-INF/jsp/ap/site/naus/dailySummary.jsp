<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
// 양식명 : 일계표

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
<%// xml 수집 전에 호출함 %>
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
	<input type="hidden" name="typId" value="dailySummary"/>
	<input type="hidden" name="ver" value="1"/>
</div>

<div id="xml-body">
<u:listArea id="xml-daily" colgroup="13%,20%,13%,21%,13%,20%" >
	<tr>
		<td class="head_ct">입금총액</td>
		<td class="body_lt"><u:input id="erpValue1" value="${formBodyXML.getAttr('body/daily.erpValue1')}" title="입금총액" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct">지급총액</td>
		<td class="body_lt"><u:input id="erpValue2" value="${formBodyXML.getAttr('body/daily.erpValue2')}" title="지급총액" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct">총잔액</td>
		<td class="body_lt"><u:input id="erpValue3" value="${formBodyXML.getAttr('body/daily.erpValue3')}" title="총잔액" style="width:90%;" maxByte="100" /></td>
	</tr>
	<tr>
		<td class="head_ct">예금수금</td>
		<td class="body_lt"><u:input id="erpValue4" value="${formBodyXML.getAttr('body/daily.erpValue4')}" title="예금수금" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct">물품대금지급</td>
		<td class="body_lt"><u:input id="erpValue5" value="${formBodyXML.getAttr('body/daily.erpValue5')}" title="물품대금지급" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct">예금잔액</td>
		<td class="body_lt"><u:input id="erpValue6" value="${formBodyXML.getAttr('body/daily.erpValue6')}" title="예금잔액" style="width:90%;" maxByte="100" /></td>
	</tr>
	<tr>
		<td class="head_ct">전자어음수금</td>
		<td class="body_lt"><u:input id="erpValue7" value="${formBodyXML.getAttr('body/daily.erpValue7')}" title="전자어음수금" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct">일반관리비지급</td>
		<td class="body_lt"><u:input id="erpValue8" value="${formBodyXML.getAttr('body/daily.erpValue8')}" title="일반관리비지급" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct">전자어음잔액</td>
		<td class="body_lt"><u:input id="erpValue9" value="${formBodyXML.getAttr('body/daily.erpValue9')}" title="전자어음잔액" style="width:90%;" maxByte="100" /></td>
	</tr>
	<tr>
		<td class="head_ct">종이어음수금</td>
		<td class="body_lt"><u:input id="erpValue10" value="${formBodyXML.getAttr('body/daily.erpValue10')}" title="종이어음수금" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct">종이어음지급</td>
		<td class="body_lt"><u:input id="erpValue11" value="${formBodyXML.getAttr('body/daily.erpValue11')}" title="종이어음지급" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct">종이어음잔액</td>
		<td class="body_lt"><u:input id="erpValue12" value="${formBodyXML.getAttr('body/daily.erpValue12')}" title="종이어음잔액" style="width:90%;" maxByte="100" /></td>
	</tr>
	<tr>
		<td class="head_ct">어음만기입금</td>
		<td class="body_lt"><u:input id="erpValue13" value="${formBodyXML.getAttr('body/daily.erpValue13')}" title="어음만기입금" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct">전자어음지급</td>
		<td class="body_lt"><u:input id="erpValue14" value="${formBodyXML.getAttr('body/daily.erpValue14')}" title="전자어음지급" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct">현금잔액</td>
		<td class="body_lt"><u:input id="erpValue15" value="${formBodyXML.getAttr('body/daily.erpValue15')}" title="현금잔액" style="width:90%;" maxByte="100" /></td>
	</tr>
	<tr>
		<td class="head_ct">어음할인입금</td>
		<td class="body_lt"><u:input id="erpValue16" value="${formBodyXML.getAttr('body/daily.erpValue16')}" title="어음할인입금" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct">지급어음만기</td>
		<td class="body_lt"><u:input id="erpValue17" value="${formBodyXML.getAttr('body/daily.erpValue17')}" title="지급어음만기" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct"><u:input id="erpColm18" value="${formBodyXML.getAttr('body/daily.erpColm18')}" title="${formBodyXML.getAttr('body/daily.erpColm18')}" style="width:85%;" maxByte="100" /></td>
		<td class="body_lt"><u:input id="erpValue18" value="${formBodyXML.getAttr('body/daily.erpValue18')}" title="${formBodyXML.getAttr('body/daily.erpColm18')}" style="width:90%;" maxByte="100" /></td>
	</tr>
	<tr>
		<td class="head_ct">현금입금</td>
		<td class="body_lt"><u:input id="erpValue19" value="${formBodyXML.getAttr('body/daily.erpValue19')}" title="어음할인입금" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct">현금지급</td>
		<td class="body_lt"><u:input id="erpValue20" value="${formBodyXML.getAttr('body/daily.erpValue20')}" title="지급어음만기" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct"><u:input id="erpColm21" value="${formBodyXML.getAttr('body/daily.erpColm21')}" title="${formBodyXML.getAttr('body/daily.erpColm21')}" style="width:85%;" maxByte="100" /></td>
		<td class="body_lt"><u:input id="erpValue21" value="${formBodyXML.getAttr('body/daily.erpValue21')}" title="${formBodyXML.getAttr('body/daily.erpColm21')}" style="width:90%;" maxByte="100" /></td>
	</tr>
	<tr>
		<td class="head_ct"><u:input id="erpColm22" value="${formBodyXML.getAttr('body/daily.erpColm22')}" title="${formBodyXML.getAttr('body/daily.erpColm22')}" style="width:85%;" maxByte="100" /></td>
		<td class="body_lt"><u:input id="erpValue22" value="${formBodyXML.getAttr('body/daily.erpValue22')}" title="${formBodyXML.getAttr('body/daily.erpColm22')}" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct"><u:input id="erpColm23" value="${formBodyXML.getAttr('body/daily.erpColm23')}" title="${formBodyXML.getAttr('body/daily.erpColm23')}" style="width:85%;" maxByte="100" /></td>
		<td class="body_lt"><u:input id="erpValue23" value="${formBodyXML.getAttr('body/daily.erpValue23')}" title="${formBodyXML.getAttr('body/daily.erpColm23')}" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct"><u:input id="erpColm24" value="${formBodyXML.getAttr('body/daily.erpColm24')}" title="${formBodyXML.getAttr('body/daily.erpColm24')}" style="width:85%;" maxByte="100" /></td>
		<td class="body_lt"><u:input id="erpValue24" value="${formBodyXML.getAttr('body/daily.erpValue24')}" title="${formBodyXML.getAttr('body/daily.erpColm24')}" style="width:90%;" maxByte="100" /></td>
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
<u:listArea id="xml-daily" colgroup="13%,20%,13%,21%,13%,20%" >
	<tr>
		<td class="head_ct">입금총액</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue1')}"/></td>
		<td class="head_ct">지급총액</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue2')}"/></td>
		<td class="head_ct">총잔액</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue3')}"/></td>
	</tr>
	<tr>
		<td class="head_ct">예금수금</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue4')}"/></td>
		<td class="head_ct">물품대금지급</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue5')}"/></td>
		<td class="head_ct">예금잔액</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue6')}"/></td>
	</tr>
	<tr>
		<td class="head_ct">전자어음수금</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue7')}" /></td>
		<td class="head_ct">일반관리비지급</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue8')}"/></td>
		<td class="head_ct">전자어음잔액</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue9')}"/></td>
	</tr>
	<tr>
		<td class="head_ct">종이어음수금</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue10')}"/></td>
		<td class="head_ct">종이어음지급</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue11')}"/></td>
		<td class="head_ct">종이어음잔액</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue12')}"/></td>
	</tr>
	<tr>
		<td class="head_ct">어음만기입금</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue13')}"/></td>
		<td class="head_ct">전자어음지급</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue14')}"/></td>
		<td class="head_ct">현금잔액</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue15')}"/></td>
	</tr>
	<tr>
		<td class="head_ct">어음할인입금</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue16')}"/></td>
		<td class="head_ct">지급어음만기</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue17')}"/></td>
		<td class="head_ct"><u:out value="${formBodyXML.getAttr('body/daily.erpColm18')}"/></td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue18')}"/></td>
	</tr>
	<tr>
		<td class="head_ct">현금입금</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue19')}"/></td>
		<td class="head_ct">현금지급</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue20')}"/></td>
		<td class="head_ct"><u:out value="${formBodyXML.getAttr('body/daily.erpColm21')}"/></td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue21')}"/></td>
	</tr>
	<tr>
		<td class="head_ct"><u:out value="${formBodyXML.getAttr('body/daily.erpColm22')}"/></td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue22')}"/></td>
		<td class="head_ct"><u:out value="${formBodyXML.getAttr('body/daily.erpColm23')}"/></td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue23')}"/></td>
		<td class="head_ct"><u:out value="${formBodyXML.getAttr('body/daily.erpColm24')}"/></td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue24')}"/></td>
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