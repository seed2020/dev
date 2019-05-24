<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

/*
관련파일 :
	- message-ap_ko_KR.properties : ap.term.*
	- PtConstant.AP_TERMS
*/
%>
<script type="text/javascript">
<!--
function doSubmit(){
	if(validator.validate('termsForm')){
		var $form = $('#termsForm');
		$form.attr("action","./transOrTerms.do");
		$form.attr("target","dataframe");
		$form.submit();
	}
}
<%// 해당 어권의 명이 입력되지 않았 을 경우 해당 어권을 보이게 함 %>
function changeLangSelectorTerm(id, va){
	if(va==''){
		var langSelector = $('#termsForm #langSelector_'+id.substring(0, id.indexOf('_')));
		var nm = $("#termsForm #"+id).attr("name");
		langSelector.val(nm.substring(nm.length-2));
		langSelector.trigger('click');
	}
}
<%// 일괄언어변경 - select onchange %>
function changeAllLang(va){
	var $selects = $("#termsArea select");
	$selects.val(va);
	$("#termsArea input:text").each(function(){
		if($(this).attr("name").endsWith(va)){
			$(this).show();
		} else {
			$(this).hide();
		}
	});
	$selects.uniform.update();
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>

<u:title titleId="pt.jsp.setOrTerms.title" alt="조직도 용어 설정" menuNameFirst="true" />

<form id="termsForm" method="post">
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="setupClsId" value="${setupClsId}" />

<u:listArea id="termsArea">

	<tr>
	<td width="18%" class="head_ct"><u:msg titleId="pt.jsp.terms.stdTerm" alt="표준 용어" /></td>
	<td class="head_ct"><u:msg titleId="pt.jsp.terms.apyTerm" alt="적용 용어" /></td>
	</tr>
	
	<c:forEach items="${terms}" var="term" varStatus="status">
	<tr>
		<td width="18%" class="head_lt"><c:if test="${term == 'siteName'}"
			><u:msg titleId="or.txt.siteName" alt="사이트명" /></c:if><c:if test="${term != 'siteName'}"
			><u:msg titleId="${setupClsId}.${term}" /></c:if></td>
		<td>
		
		<table cellspacing="0" cellpadding="0" border="0">
		<tr>
		<td id="langTyp${status.index+1}">
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="langStatus">
			<u:convertMsg srcId="${term}_${langTypCdVo.cd}" var="termVa" msgId="${setupClsId}.${term}" langTypCd="${langTypCdVo.cd}" />
			<u:set test="${langStatus.first}" var="style" value="width:200px" elseValue="width:200px; display:none;" />
			<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
			<u:input id="${term}_${langTypCdVo.cd}" name="${setupClsId}.${term}_${langTypCdVo.cd}"
					titlePrefix="${titlePrefix}" titleId="${setupClsId}.${term}"
					value="${termVa}" style="${style}"
					maxByte="200" validator="changeLangSelectorTerm(id, va)" mandatory="Y" />
			</c:forEach>
		</td>
		<td>
			<c:if test="${fn:length(_langTypCdListByCompId)>1}">
				<select id="langSelector_${term}" onchange="changeLangTypCd('termsForm','langTyp${status.index+1}', this.value)" <u:elemTitle titleId="cols.langTyp" />>
				<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="langStatus">
				<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
				</c:forEach>
				</select>
			</c:if>
		</td>
		<td><c:if test="${sysPlocMap.codeByCompEnable == 'Y' and 
				(term == 'grade' or term == 'title' or term == 'posit' or term == 'duty')}">
			<u:buttonS titleId="cm.option.config" href="/pt/adm/org/setCatCd.do?menuId=${menuId}&clsCd=${term.toUpperCase()}_CD"
				/></c:if><c:if test="${sysPlocMap.seculByCompEnable == 'Y' and term == 'secul'}">
			<u:buttonS titleId="cm.option.config" href="/pt/adm/org/setCatCd.do?menuId=${menuId}&clsCd=${term.toUpperCase()}_CD"
				/></c:if>
		</td>
		</tr>
		</table>
		
		</td>
	</tr>
	</c:forEach>

</u:listArea>
<c:if test="${fn:length(_langTypCdListByCompId)>1}">
<u:listArea>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.jsp.terms.chgLangAll" alt="일괄 언어 변경" /></td>
		<td><select onchange="changeAllLang(this.value)" <u:elemTitle titleId="pt.jsp.terms.chgLangAll" />>
				<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="langStatus">
				<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
				</c:forEach>
				</select>
		</td>
	</tr>
</u:listArea>
</c:if>
</form>

<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:doSubmit();" auth="SYS" />
</u:buttonArea>