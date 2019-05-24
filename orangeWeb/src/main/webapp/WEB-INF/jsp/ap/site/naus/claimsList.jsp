<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
// 양식명 : 클레임 발생보고

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
	<input type="hidden" name="typId" value="claimsList"/>
	<input type="hidden" name="ver" value="1"/>
</div>

<div id="xml-body">
<u:listArea id="xml-daily" colgroup="13%,37%,13%,37%">
	<tr>
		<td class="head_ct">발생일</td>
		<td class="body_lt"><u:calendar id="erpValue1" title="발생일" value="${formBodyXML.getAttr('body/daily.erpValue1')}" /></td>
		<td class="head_ct">생산담당</td>
		<td class="body_lt"><u:input id="erpValue2" value="${formBodyXML.getAttr('body/daily.erpValue2')}" title="생산담당" style="width:95%;" maxByte="100" /></td>
	</tr>
	<tr>
		<td class="head_ct">게시일</td>
		<td class="body_lt"><u:calendar id="erpValue3" title="게시일" value="${formBodyXML.getAttr('body/daily.erpValue3')}" /></td>
		<td class="head_ct">영업담당</td>
		<td class="body_lt"><u:input id="erpValue4" value="${formBodyXML.getAttr('body/daily.erpValue4')}" title="영업담당" style="width:95%;" maxByte="100" /></td>
	</tr>
	<tr>
		<td class="head_ct">회수일</td>
		<td class="body_lt"><u:calendar id="erpValue5" title="게시일" value="${formBodyXML.getAttr('body/daily.erpValue5')}" /></td>
		<td class="head_ct">손실금액</td>
		<td class="body_lt"><u:input id="erpValue6" value="${formBodyXML.getAttr('body/daily.erpValue6')}" title="손실금액" style="width:95%;" maxByte="100" /></td>
	</tr>
	<tr>
		<td class="head_ct">제품명</td>
		<td class="body_lt"><u:input id="erpValue7" value="${formBodyXML.getAttr('body/daily.erpValue7')}" title="제품명" style="width:95%;" maxByte="100" /></td>
		<td class="head_ct">제품코드</td>
		<td class="body_lt"><u:input id="erpValue8" value="${formBodyXML.getAttr('body/daily.erpValue8')}" title="제품코드" style="width:95%;" maxByte="100" /></td>
	</tr>
	<tr>
		<td class="head_ct">생산일자</td>
		<td class="body_lt"><u:calendar id="erpValue9" title="게시일" value="${formBodyXML.getAttr('body/daily.erpValue9')}" /></td>
		<td class="head_ct">불량수량</td>
		<td class="body_lt"><u:input id="erpValue10" value="${formBodyXML.getAttr('body/daily.erpValue10')}" title="불량수량" style="width:95%;" maxByte="100" /></td>
	</tr>
	<tr>
		<td class="head_ct">불량내용</td>
		<td class="body_lt"><u:input id="erpValue11" value="${formBodyXML.getAttr('body/daily.erpValue11')}" title="불량내용" style="width:95%;" maxByte="100" /></td>
		<td class="head_ct">불량원인</td>
		<td class="body_lt"><u:input id="erpValue12" value="${formBodyXML.getAttr('body/daily.erpValue12')}" title="불량원인" style="width:95%;" maxByte="100" /></td>
	</tr>
	<tr>
		<td class="head_ct">영업부조치</td>
		<td class="body_lt"><u:input id="erpValue13" value="${formBodyXML.getAttr('body/daily.erpValue13')}" title="영업부조치" style="width:95%;" maxByte="100" /></td>
		<td class="head_ct">기타</td>
		<td class="body_lt"><u:input id="erpValue14" value="${formBodyXML.getAttr('body/daily.erpValue14')}" title="기타" style="width:95%;" maxByte="100" /></td>
	</tr>
</u:listArea>
<u:listArea>
<tr><td class="head_ct">관리부 개선조치</td></tr>
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
<u:listArea id="xml-daily" colgroup="13%,37%,13%,37%" >
	<tr>
		<td class="head_ct">발생일</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue1')}" /></td>
		<td class="head_ct">생산담당</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue2')}" /></td>
	</tr>
	<tr>
		<td class="head_ct">게시일</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue3')}" /></td>
		<td class="head_ct">영업담당</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue4')}" /></td>
	</tr>
	<tr>
		<td class="head_ct">회수일</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue5')}" /></td>
		<td class="head_ct">손실금액</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue6')}" /></td>
	</tr>
	<tr>
		<td class="head_ct">제품명</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue7')}" /></td>
		<td class="head_ct">제품코드</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue8')}" /></td>
	</tr>
	<tr>
		<td class="head_ct">생산일자</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue9')}" /></td>
		<td class="head_ct">불량수량</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue10')}" /></td>
	</tr>
	<tr>
		<td class="head_ct">불량내용</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue11')}" /></td>
		<td class="head_ct">불량원인</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue12')}" /></td>
	</tr>
	<tr>
		<td class="head_ct">영업부조치</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue13')}" /></td>
		<td class="head_ct">기타</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue14')}" /></td>
	</tr>
</u:listArea>

<u:listArea>
<tr><td class="head_ct">관리부 개선조치</td></tr>
<tr><td class="body_lt editor">${formBodyXML.getAttr('body/cont.erpCont')}</td></tr>
</u:listArea>
	
</div>

<div class="blank"></div>

</div>
</c:if>