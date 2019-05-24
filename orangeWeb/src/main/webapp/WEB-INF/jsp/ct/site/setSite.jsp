<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:msg titleId="ct.msg.noSelectCat" alt="카테고리를 선택해주시기 바랍니다." var="catSelect" />
<script type="text/javascript">
<!--
function saveSite(){
	var $sltCatId= $("#catChoi").val();
	
	if (validator.validate('setSiteForm')) {
		if($sltCatId == null || $sltCatId == ""){
			alert('${catSelect}'); //카테고리를 선택해주시기 바랍니다.
		}else{
			
			var $form = $("#setSiteForm");
			$form.attr("method", "POST");
			$form.attr("action", "./transSaveSite.do?menuId=${menuId}&ctId=${ctId}&siteId=${siteId}&catId="+$sltCatId);
			editor('cont').prepare();
			$form.submit();
		}
		
	}
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />
<u:set test="${fnc == 'mod'}" var="siteNm" value="OKJSP" elseValue="" />
<u:set test="${fnc == 'mod'}" var="siteUrl" value="http://www.okjsp.net" elseValue="" />
<u:set test="${fnc == 'mod'}" var="recmdRson" value="JSP 커뮤니티 사이트, 강좌, Q&A, ..." elseValue="" />

<u:title title="${menuTitle }" alt="Cool Site 등록/Cool Site 수정" menuNameFirst="true"/>

<form id="setSiteForm">
<u:input type="hidden" id="menuId" value="${menuId}" />

<% // 폼 필드 %>
<u:listArea  noBottomBlank="true">
	<tr>
	<td width="27%" class="head_lt"><u:mandatory /><u:msg titleId="cols.siteNm" alt="사이트명" /></td>
	<td width="73%"><u:input id="siteNm" name="siteNm" titleId="cols.siteNm" style="width:98%;" value="${ctSiteBVo.subj}" mandatory="Y" maxByte="240"/></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.cat" alt="카테고리" /></td>
	<td><select id="catChoi" name="catChoi">
			<option value=""><u:msg titleId="cm.option.all" alt="전체"/></option>
			<c:forEach var="siteCatVo" items="${siteCatList}" varStatus="status">
				<option value="${siteCatVo.catId}" <c:if test="${siteCatVo.catId == catId}"> selected="selected"</c:if>>${siteCatVo.catNm}</option>
			</c:forEach>
		</select></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.siteUrl" alt="Site URL" /></td>
	<td><u:input id="siteUrl" name="siteUrl" titleId="cols.siteUrl" style="width:98%;" value="${ctSiteBVo.url}" mandatory="Y" maxByte="120"/></td>
	</tr>
	
	<tr>
	<td colspan="2" class="head_ct"><u:msg titleId="cols.recmdRson" alt="추천사유" /></td>
	</tr>
</u:listArea>

<div id="editor1Area" class="listarea" style="width:100%; height:302px; padding-top:2px"></div>
<u:editor id="cont" width="100%" height="300" module="ct" areaId="editor1Area" value="${ctSiteBVo.cont}" />
<u:blank />
<% // 하단 버튼 %>
<u:buttonArea>
	<u:msg titleId="cm.msg.save.success" var="msg" alt="저장 되었습니다." />
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:saveSite();"/>
	<u:button titleId="cm.btn.cancel" alt="취소" href="javascript:history.go(-1);" />
</u:buttonArea>

</form>

