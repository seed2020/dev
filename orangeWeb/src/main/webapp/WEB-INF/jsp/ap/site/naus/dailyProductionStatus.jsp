<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
// 양식명 : 일일생산현황

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
	<input type="hidden" name="typId" value="dailyProductionStatus"/>
	<input type="hidden" name="ver" value="1"/>
</div>

<div id="xml-body">
<u:listArea id="xml-daily" colgroup="13%,20%,13%,21%,13%,20%" >
	<tr>
		<td class="head_ct" colspan="2">근무인원</td>
		<td class="head_ct" colspan="2">생산실적</td>
		<td class="head_ct" colspan="2">팀별</td>
	</tr>
	<tr>
		<td class="head_ct">기장</td>
		<td class="body_lt"><u:input id="erpValue1" value="${formBodyXML.getAttr('body/daily.erpValue1')}" title="기장" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y"/></td>
		<td class="head_ct">총생산량/ea</td>
		<td class="body_lt"><u:input id="erpValue2" value="${formBodyXML.getAttr('body/daily.erpValue2')}" title="총생산량/ea" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
		<td class="head_ct">1팀</td>
		<td class="body_lt"><u:input id="erpValue3" value="${formBodyXML.getAttr('body/daily.erpValue3')}" title="1팀" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
	</tr>
	<tr>
		<td class="head_ct">카운터</td>
		<td class="body_lt"><u:input id="erpValue4" value="${formBodyXML.getAttr('body/daily.erpValue4')}" title="카운터" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
		<td class="head_ct">3팀 인쇄/m</td>
		<td class="body_lt"><u:input id="erpValue5" value="${formBodyXML.getAttr('body/daily.erpValue5')}" title="3팀 인쇄/m" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
		<td class="head_ct">시터 1호</td>
		<td class="body_lt"><u:input id="erpValue6" value="${formBodyXML.getAttr('body/daily.erpValue6')}" title="시터 1호" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
	</tr>
	<tr>
		<td class="head_ct">포장</td>
		<td class="body_lt"><u:input id="erpValue7" value="${formBodyXML.getAttr('body/daily.erpValue7')}" title="포장" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
		<td class="head_ct">3팀 티다이/m</td>
		<td class="body_lt"><u:input id="erpValue8" value="${formBodyXML.getAttr('body/daily.erpValue8')}" title="3팀 티다이/m" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
		<td class="head_ct">시터 2호</td>
		<td class="body_lt"><u:input id="erpValue9" value="${formBodyXML.getAttr('body/daily.erpValue9')}" title="시터 2호" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
	</tr>
	<tr>
		<td class="head_ct">보조</td>
		<td class="body_lt"><u:input id="erpValue10" value="${formBodyXML.getAttr('body/daily.erpValue10')}" title="보조" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
		<td class="head_ct">3팀 스릿타/m</td>
		<td class="body_lt"><u:input id="erpValue11" value="${formBodyXML.getAttr('body/daily.erpValue11')}" title="3팀 스릿타/m" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
		<td class="head_ct">시터 3호</td>
		<td class="body_lt"><u:input id="erpValue39" value="${formBodyXML.getAttr('body/daily.erpValue39')}" title="시터 3호" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
	</tr>
	<tr>
		<td class="head_ct">재단</td>
		<td class="body_lt"><u:input id="erpValue13" value="${formBodyXML.getAttr('body/daily.erpValue13')}" title="재단" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
		<td class="head_ct">6팀 재단/연</td>
		<td class="body_lt"><u:input id="erpValue14" value="${formBodyXML.getAttr('body/daily.erpValue14')}" title="6팀 재단/연" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
		<td class="head_ct">2팀</td>
		<td class="body_lt"><u:input id="erpValue12" value="${formBodyXML.getAttr('body/daily.erpValue12')}" title="2팀" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
	</tr>
	<tr>
		<td class="head_ct">기타</td>
		<td class="body_lt"><u:input id="erpValue16" value="${formBodyXML.getAttr('body/daily.erpValue16')}" title="기타" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
		<td class="head_ct">6팀 보강지/ea</td>
		<td class="body_lt"><u:input id="erpValue17" value="${formBodyXML.getAttr('body/daily.erpValue17')}" title="6팀 보강지/ea" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
		<td class="head_ct">새철</td>
		<td class="body_lt"><u:input id="erpValue15" value="${formBodyXML.getAttr('body/daily.erpValue15')}" title="새철" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
	</tr>
	<!-- dsdasdsa -->
	<tr>
		<td class="head_ct"><u:input id="erpColm30" value="${formBodyXML.getAttr('body/daily.erpColm30')}" title="${formBodyXML.getAttr('body/daily.erpColm30')}" style="width:85%;" maxByte="100" /></td>
		<td class="body_lt"><u:input id="erpValue30" value="${formBodyXML.getAttr('body/daily.erpValue30')}" title="합계" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
		<td class="head_ct">7팀 인쇄/ea</td>
		<td class="body_lt"><u:input id="erpValue31" value="${formBodyXML.getAttr('body/daily.erpValue31')}" title="7팀 인쇄/ea" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
		<td class="head_ct">4팀 TB</td>
		<td class="body_lt"><u:input id="erpValue18" value="${formBodyXML.getAttr('body/daily.erpValue18')}" title="4팀 TB" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
	</tr>
	<tr>
		<td class="head_ct"><u:input id="erpColm33" value="${formBodyXML.getAttr('body/daily.erpColm33')}" title="${formBodyXML.getAttr('body/daily.erpColm33')}" style="width:85%;" maxByte="100" /></td>
		<td class="body_lt"><u:input id="erpValue33" value="${formBodyXML.getAttr('body/daily.erpValue33')}" title="${formBodyXML.getAttr('body/daily.erpColm33')}" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
		<td class="head_ct">7팀 코팅/ea</td>
		<td class="body_lt"><u:input id="erpValue34" value="${formBodyXML.getAttr('body/daily.erpValue34')}" title="7팀 코팅/ea" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
		<td class="head_ct">4팀 FB</td>
		<td class="body_lt"><u:input id="erpValue32" value="${formBodyXML.getAttr('body/daily.erpValue32')}" title="4팀 FB" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
	</tr>
	<tr>
		<td class="head_ct"><u:input id="erpColm36" value="${formBodyXML.getAttr('body/daily.erpColm36')}" title="${formBodyXML.getAttr('body/daily.erpColm36')}" style="width:85%;" maxByte="100" /></td>
		<td class="body_lt"><u:input id="erpValue36" value="${formBodyXML.getAttr('body/daily.erpValue36')}" title="${formBodyXML.getAttr('body/daily.erpColm36')}" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
		<td class="head_ct">7팀 다이컷/ea</td>
		<td class="body_lt"><u:input id="erpValue37" value="${formBodyXML.getAttr('body/daily.erpValue37')}" title="7팀 다이컷/ea" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
		<td class="head_ct">6팀 OTB</td>
		<td class="body_lt"><u:input id="erpValue35" value="${formBodyXML.getAttr('body/daily.erpValue35')}" title="6팀 OTB" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>		
	</tr>
	<tr>
		<td class="head_ct"><u:input id="erpColm40" value="${formBodyXML.getAttr('body/daily.erpColm40')}" title="${formBodyXML.getAttr('body/daily.erpColm40')}" style="width:85%;" maxByte="100" /></td>
		<td class="body_lt"><u:input id="erpValue40" value="${formBodyXML.getAttr('body/daily.erpValue40')}" title="${formBodyXML.getAttr('body/daily.erpColm40')}" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
		<td class="head_ct">7팀 탈지/ea</td>
		<td class="body_lt"><u:input id="erpValue20" value="${formBodyXML.getAttr('body/daily.erpValue20')}" title="7팀 탈지/ea" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
		<td class="head_ct">7팀 편면</td>
		<td class="body_lt"><u:input id="erpValue38" value="${formBodyXML.getAttr('body/daily.erpValue38')}" title="7팀 편면" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
	</tr>
	<tr>
		<td class="head_ct">합계</td>
		<td class="body_lt"><u:input id="erpValue19" value="${formBodyXML.getAttr('body/daily.erpValue19')}" title="합계" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
		<td class="head_ct"><u:input id="erpColm41" value="${formBodyXML.getAttr('body/daily.erpColm41')}" title="${formBodyXML.getAttr('body/daily.erpColm41')}" style="width:85%;" maxByte="100" /></td>
		<td class="body_lt"><u:input id="erpValue41" value="${formBodyXML.getAttr('body/daily.erpValue41')}" title="${formBodyXML.getAttr('body/daily.erpColm41')}" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
		<td class="head_ct">7팀 트레이</td>
		<td class="body_lt"><u:input id="erpValue21" value="${formBodyXML.getAttr('body/daily.erpValue21')}" title="7팀 트레이" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y" /></td>
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
		<td class="head_ct" colspan="2">근무인원</td>
		<td class="head_ct" colspan="2">생산실적</td>
		<td class="head_ct" colspan="2">팀별</td>
	</tr>
	<tr>
		<td class="head_ct">기장</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue1')}"/></td>
		<td class="head_ct">총생산량/ea</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue2')}"/></td>
		<td class="head_ct">1팀</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue3')}"/></td>
	</tr>
	<tr>
		<td class="head_ct">카운터</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue4')}"/></td>
		<td class="head_ct">3팀 인쇄/m</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue5')}"/></td>
		<td class="head_ct">시터 1호</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue6')}"/></td>
	</tr>
	<tr>
		<td class="head_ct">포장</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue7')}"/></td>
		<td class="head_ct">3팀 티다이/m</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue8')}"/></td>
		<td class="head_ct">시터 2호</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue9')}"/></td>
	</tr>
	<tr>
		<td class="head_ct">보조</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue10')}" /></td>
		<td class="head_ct">3팀 스릿타/m</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue11')}" /></td>
		<td class="head_ct">시터 3호</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue39')}" /></td>
	</tr>
	<tr>
		<td class="head_ct">재단</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue13')}" /></td>
		<td class="head_ct">6팀 재단/연</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue14')}" /></td>
		<td class="head_ct">2팀</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue12')}" /></td>
	</tr>
	<tr>
		<td class="head_ct">기타</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue16')}" /></td>
		<td class="head_ct">6팀 보강지/ea</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue17')}" /></td>
		<td class="head_ct">새철</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue15')}" /></td>
	</tr>
	<!-- dsdasdsa -->
	<tr>
		<td class="head_ct"><u:out value="${formBodyXML.getAttr('body/daily.erpColm30')}" /></td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue30')}" /></td>
		<td class="head_ct">7팀 인쇄/ea</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue31')}" /></td>
		<td class="head_ct">4팀 TB</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue18')}" /></td>
	</tr>
	<tr>
		<td class="head_ct"><u:out value="${formBodyXML.getAttr('body/daily.erpColm33')}" /></td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue33')}" /></td>
		<td class="head_ct">7팀 코팅/ea</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue34')}" /></td>
		<td class="head_ct">4팀 FB</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue32')}" /></td>
	</tr>
	<tr>
		<td class="head_ct"><u:out value="${formBodyXML.getAttr('body/daily.erpColm36')}" /></td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue36')}" /></td>
		<td class="head_ct">7팀 다이컷/ea</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue37')}" /></td>
		<td class="head_ct">6팀 OTB</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue35')}" /></td>		
	</tr>
	<tr>
		<td class="head_ct"><u:out value="${formBodyXML.getAttr('body/daily.erpColm40')}" /></td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue40')}" /></td>
		<td class="head_ct">7팀 탈지/ea</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue20')}" /></td>
		<td class="head_ct">7팀 편면</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue38')}" /></td>
	</tr>
	<tr>
		<td class="head_ct">합계</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue19')}" /></td>
		<td class="head_ct"><u:out value="${formBodyXML.getAttr('body/daily.erpColm41')}" /></td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue41')}" /></td>
		<td class="head_ct">7팀 트레이</td>
		<td class="body_rt"><u:out value="${formBodyXML.getAttr('body/daily.erpValue21')}" /></td>
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