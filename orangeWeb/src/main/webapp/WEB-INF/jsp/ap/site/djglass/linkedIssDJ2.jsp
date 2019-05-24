<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
import="org.springframework.web.context.WebApplicationContext"
import="org.springframework.web.context.support.WebApplicationContextUtils"
import="org.springframework.web.servlet.FrameworkServlet"
import="com.innobiz.orange.web.or.svc.OrCmSvc"
import="com.innobiz.orange.web.pt.secu.LoginSession"
import="com.innobiz.orange.web.pt.secu.UserVo"
import="java.util.Map"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

	request.setAttribute("currYmd", com.innobiz.orange.web.cm.utils.StringUtil.getCurrYmd());


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
</style>
<script type="text/javascript">
<!--
$(document).ready(function(){
	$("#formBodyArea").find("input:visible").uniform();
});
//-->
</script>
<div id="xmlArea" style="${formBodyMode eq 'pop' ? 'width:900px;' : ''}">
<div id="xml-head">
	<input type="hidden" name="typId" value="linkedIssDJ2"/>
	<input type="hidden" name="ver" value="1"/>
</div>
<c:if


	test="${formBodyMode ne 'view'}">
<div id="xml-body">
<u:title type="small" title="사고자 인적 사항 (해당 팀장 작성)" />
<u:listArea id="xml-accident" colgroup="13%,87%">
<tr>
	<td class="head_ct">사 고 일 시</td>
	<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0"><tr>
		<td><u:calendar id="erpAccidentDay" title="사고일시" value="${empty formBodyXML.getAttr('body/accident.erpAccidentDay') ? 'today' : formBodyXML.getAttr('body/accident.erpAccidentDay')}" /></td>
		<td style="padding-left:6px; padding-right:15px">일</td>
		<td><select name="erpAccidentHour"><c:forEach begin="0" end="23" step="1" var="hour">
			<u:option value="${hour<10 ? '0'.concat(hour) : hour}" title="${hour<10 ? '0'.concat(hour) : hour}"
				checkValue="${formBodyXML.getAttr('body/accident.erpAccidentHour')}" /></c:forEach></select></td>
		<td style="padding-left:6px; padding-right:15px">시</td>
		<td><select name="erpAccidentMinute"><c:forEach begin="0" end="45" step="15" var="minute">
			<u:option value="${minute<10 ? '0'.concat(minute) : minute}" title="${minute<10 ? '0'.concat(minute) : minute}"
				checkValue="${formBodyXML.getAttr('body/accident.erpAccidentMinute')}" /></c:forEach></select></td>
		<td style="padding-left:6px; padding-right:15px">분</td></tr></table></td>
</tr>
<tr>
	<td class="head_ct">사 고 장 소</td>
	<td class="body_lt" colspan="3"><textarea name="erpVa1" rows="2" style="width:98.4%"><u:out
		type="textarea" value="${formBodyXML.getAttr('body/accident.erpVa1')}" /></textarea></td>
</tr>
<tr>
	<td class="head_ct">사 고 자</td>
	<td class="body_lt" colspan="3"><textarea name="erpVa2" rows="2" style="width:98.4%"><u:out
		type="textarea" value="${formBodyXML.getAttr('body/accident.erpVa2')}" /></textarea></td>
</tr>
<tr>
	<td class="head_ct">사 고 내 용</td>
	<td class="body_lt" colspan="3"><textarea name="erpVa3" rows="5" style="width:98.4%"><u:out
		type="textarea" value="${formBodyXML.getAttr('body/accident.erpVa3')}" /></textarea></td>
</tr>
<tr>
	<td class="head_ct">발 생 원 인</td>
	<td class="body_lt" colspan="3"><textarea name="erpVa4" rows="5" style="width:98.4%"><u:out
		type="textarea" value="${formBodyXML.getAttr('body/accident.erpVa4')}" /></textarea></td>
</tr>
<tr>
	<td class="head_ct">피해 추정 금액</td>
	<td class="body_lt" colspan="3"><textarea name="erpVa5" rows="3" style="width:98.4%"><u:out
		type="textarea" value="${formBodyXML.getAttr('body/accident.erpVa5')}" /></textarea></td>
</tr>
</u:listArea>

<div class="blank"></div>
<div class="blank"></div>
<div style="text-align:center; font-size:18px; font-weight:bold; ">위와 같이 사고 발생 내용을 보고 합니다.</div>
</div>
</c:if><c:if



	test="${formBodyMode eq 'view'}">
<div>
<u:title type="small" title="사고자 인적 사항 (해당 팀장 작성)" />
<u:listArea colgroup="13%,87%">
<tr>
	<td class="head_ct">사 고 일 시</td>
	<td class="body_lt"><c:if
		test="${not empty formBodyXML.getAttr('body/accident.erpAccidentDay')}"
			><span style="padding-right:10px">${formBodyXML.getAttr('body/accident.erpAccidentDay')} 년</span></c:if><c:if
		test="${not empty formBodyXML.getAttr('body/accident.erpAccidentHour')}"
			><span style="padding-right:10px">${formBodyXML.getAttr('body/accident.erpAccidentHour')} 시</span></c:if><c:if
		test="${not empty formBodyXML.getAttr('body/accident.erpAccidentMinute')}"
			><span style="padding-right:10px">${formBodyXML.getAttr('body/accident.erpAccidentMinute')} 분</span></c:if></td>
</tr>
<tr>
	<td class="head_ct">사 고 장 소</td>
	<td class="body_lt" colspan="3"><u:out value="${formBodyXML.getAttr('body/accident.erpVa1')}" /></td>
</tr>
<tr>
	<td class="head_ct">사 고 자</td>
	<td class="body_lt" colspan="3"><u:out value="${formBodyXML.getAttr('body/accident.erpVa2')}" /></td>
</tr>
<tr>
	<td class="head_ct">사 고 내 용</td>
	<td class="body_lt" colspan="3"><u:out value="${formBodyXML.getAttr('body/accident.erpVa3')}" /></td>
</tr>
<tr>
	<td class="head_ct">발 생 원 인</td>
	<td class="body_lt" colspan="3"><u:out value="${formBodyXML.getAttr('body/accident.erpVa4')}" /></td>
</tr>
<tr>
	<td class="head_ct">피해 추정 금액</td>
	<td class="body_lt" colspan="3"><u:out value="${formBodyXML.getAttr('body/accident.erpVa5')}" /></td>
</tr>
</u:listArea>

<div class="blank"></div>
<div class="blank"></div>
<div style="text-align:center; font-size:18px; font-weight:bold; ">위와 같이 사고 발생 내용을 보고 합니다.</div>
</div>
</c:if><c:if
	
	
	test="${formBodyMode eq 'pop'}">
<u:buttonArea>
	<u:button titleId="cm.btn.confirm" onclick="setErpXMLPop();" alt="확인" auth="W" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</c:if>
</div>