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
<!--<%--
[팝업] 안전 사고 보고서 선택 - callback --%>
var xmlAreaId = "${formBodyMode eq 'pop' ? 'setDocBodyHtmlDialog' : 'formBodyArea'}";
function setLinkedDoc(apvNo, docSubj, secuYn){
	var $area = $("#"+xmlAreaId);
	var $dataArea = $area.find("#linkedDocDataArea");
	$dataArea.html('');
	$dataArea.appendHidden
	$dataArea.appendHidden({'name':'erpLinkedApvNo','value':apvNo});
	
	var $subjArea = $area.find("#linkedDocSubjArea");
	$subjArea.html("<a href=\"javascript:openDocView('"+apvNo+"','"+secuYn+"')\">"+docSubj+"</a>");
}<%--
[팝업] 안전 사고 보고서 선택 --%>
function openLinkedPop(){
	var apvNo = '${param.apvNo}';
	var erpLinkedApvNo = $("#"+xmlAreaId+" #linkedDocDataArea input[name='erpLinkedApvNo']").val();
	var param = "&xmlTypId=linkedIssDJ1"+(apvNo!='' ? '&apvNo='+apvNo : '')+(erpLinkedApvNo!=null && erpLinkedApvNo!='' ? '&erpLinkedApvNo='+erpLinkedApvNo : '');
	dialog.open('openLinkedDocDialog', '안전 사고 보고서 선택', './listLinkedDocPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}'+param);
}<%--
XML 작업 validation --%>
function checkFormBodyXML(){
	var linkedApvNo = $("#"+xmlAreaId+" #linkedDocDataArea input[name='erpLinkedApvNo']").val();
	if(linkedApvNo!=null && linkedApvNo!='') return true;
	alert('관련 문서를 선택해 주십시요');
	return false;
}<c:if test="${formBodyMode eq 'pop'}">
function collectFormBodyXML(){
	if(checkFormBodyXML()==false) return null;
	
	var formBodyXML = getFormBodyXML();
	setErpXMLByValue(formBodyXML, true);
	
	var alink = $("#setDocBodyHtmlDialog #xmlArea #linkedDocSubjArea").html();
	$("#docArea").children("#linkedArea").find("#linkedSubjArea").html(alink);

	dialog.close("setDocBodyHtmlDialog");
}
</c:if>
$(document).ready(function(){
	$("#"+xmlAreaId).find("input:visible").uniform();
});
//-->
</script>
<div id="xmlArea" style="${formBodyMode eq 'pop' ? 'width:900px;' : ''}">
<div id="xml-head">
	<input type="hidden" name="typId" value="linkedSolDJ1"/>
	<input type="hidden" name="ver" value="1"/>
</div>
<c:if


	test="${formBodyMode ne 'view'}">
<div id="xml-body">
<u:title type="small" title="사고원인 분석 및 재발 방지 대책  (안전관리부서 작성)" />
<u:listArea id="xml-result" colgroup="13%,87%">
<tr>
	<td class="head_ct">사 고 원 인<br/>분 석</td>
	<td class="body_lt"><textarea name="erpVa1" rows="4" style="width:98.4%"><u:out
		type="textarea" value="${formBodyXML.getAttr('body/result.erpVa1')}" /></textarea></td>
</tr>
<tr>
	<td class="head_ct">재 발 방 지<br/>대 책</td>
	<td class="body_lt"><textarea name="erpVa2" rows="4" style="width:98.4%"><u:out
		type="textarea" value="${formBodyXML.getAttr('body/result.erpVa2')}" /></textarea></td>
</tr>
<tr>
	<td class="head_ct">세 부 실 행<br/>계 획</td>
	<td class="body_lt"><textarea name="erpVa3" rows="4" style="width:98.4%"><u:out
		type="textarea" value="${formBodyXML.getAttr('body/result.erpVa3')}" /></textarea></td>
</tr>
</u:listArea>

<div class="blank"></div>
<u:title type="small" title="기타 사항" />
<u:listArea id="xml-etc" colgroup="13%,87%">
<tr>
	<td class="head_ct">소 속 부 서<br/>임 원 의 견</td>
	<td class="body_lt"><textarea name="erpVa4" rows="4" style="width:98.4%"><u:out
		type="textarea" value="${formBodyXML.getAttr('body/etc.erpVa4')}" /></textarea></td>
</tr>
<tr>
	<td class="head_ct">관 리 부 서<br/>임 원 의 견</td>
	<td class="body_lt"><textarea name="erpVa5" rows="4" style="width:98.4%"><u:out
		type="textarea" value="${formBodyXML.getAttr('body/etc.erpVa5')}" /></textarea></td>
</tr>
<tr>
	<td class="head_ct">총 소요 경비 등<br/>기 타 사 항</td>
	<td class="body_lt"><textarea name="erpVa6" rows="4" style="width:98.4%"><u:out
		type="textarea" value="${formBodyXML.getAttr('body/etc.erpVa6')}" /></textarea>
	</td>
</tr>
</u:listArea>

<div class="blank"></div>
<u:title title="관련문서" type="small" >
	<u:titleButton href="javascript:openLinkedPop();" title="선택" />
</u:title>
<u:listArea id="xml-linked" colgroup="13%,87%">
<tr>
	<td class="head_ct">제 목</td>
	<td class="body_lt">
		<div id="linkedDocSubjArea"><c:if test="${not empty linkedApOngdBVo}"><a href="javascript:openDocView('${linkedApOngdBVo.apvNo}','${
						not empty linkedApOngdBVo.docPwEnc
						and linkedApOngdBVo.makrUid != sessionScope.userVo.userUid ? 'Y' : ''}');">${linkedApOngdBVo.docSubj}</a></c:if></div>
		<div id="linkedDocDataArea"><c:if test="${not empty linkedApOngdBVo}"><input type="hidden" name="erpLinkedApvNo" value="${linkedApOngdBVo.apvNo}" /></c:if></div></td>
</tr>
</u:listArea>
</div>
</c:if><c:if


	test="${formBodyMode eq 'view'}">
<div>
<u:title type="small" title="사고원인 분석 및 재발 방지 대책  (안전관리부서 작성)" />
<u:listArea colgroup="13%,87%">
<tr>
	<td class="head_ct">사 고 원 인<br/>분 석</td>
	<td class="body_lt"><u:out
		type="textarea" value="${formBodyXML.getAttr('body/result.erpVa1')}" /></td>
</tr>
<tr>
	<td class="head_ct">재 발 방 지<br/>대 책</td>
	<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/result.erpVa2')}" /></td>
</tr>
<tr>
	<td class="head_ct">세 부 실 행<br/>계 획</td>
	<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/result.erpVa3')}" /></td>
</tr>
</u:listArea>

<div class="blank"></div>
<u:title type="small" title="기타 사항" />
<u:listArea id="xml-etc" colgroup="13%,87%">
<tr>
	<td class="head_ct">소 속 부 서<br/>임 원 의 견</td>
	<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/etc.erpVa4')}" /></td>
</tr>
<tr>
	<td class="head_ct">관 리 부 서<br/>임 원 의 견</td>
	<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/etc.erpVa5')}" /></td>
</tr>
<tr>
	<td class="head_ct">총 소요 경비 등<br/>기 타 사 항</td>
	<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/etc.erpVa6')}" />
	</td>
</tr>
</u:listArea>
</div>
</c:if><c:if
	
	
	test="${formBodyMode eq 'pop'}">
<u:buttonArea>
	<u:button titleId="cm.btn.confirm" onclick="setErpXMLPop();" alt="확인" auth="W" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</c:if>
</div>

