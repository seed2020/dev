<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function doSubmit(){
	var $form = $('#pageRecExtArea');
	$form.attr("action","./transAttachExt.do");
	$form.attr("target","dataframe");
	$form.submit();
}
<%// 첫번째 콤보가 변경 될때 %>
function changeVa(va, prefix){
	var disabled = va=='Y' ? false:true;
	$("#pageRecExtArea select[name^='"+prefix+"'],textarea[name^='"+prefix+"']").each(function(index, obj){
		if($(this).prop("tagName") == 'SELECT'){
			if(index == 0) return true;
			if(disabled) $(this).val('N');
			else $(this).val('Y');
		}else{
			if(disabled)	$(this).val('');
		}
		//setDisabled($(this), disabled);
	});
	$.uniform.update("#pageRecExtArea select, input");
}<%// 전체 확장자 모듈에 복사 %>
function extCopy(id, prefix){
	var va = $('#'+id).val();
	if(va=='') return;
	$("#pageRecExtArea textarea[name^='"+prefix+"']").each(function(index, obj){
		if(index == 0) return true;
		$(this).val(va);
	});
}<%// 전체 확장자 확인 %>
function viewExtsPop(){
	dialog.open('viewExtsDialog','<u:msg titleId="cols.ext" alt="확장자" />','./viewExtsPop.do?menuId=${menuId}');
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>
<%
//기본 확장자
String[] attachExts = {"doc","docx","xls","xlsx","ppt","pptx","jpg","jpeg"};
request.setAttribute("attachExts", attachExts);
%>
<u:title title="첨부 확장자 허용 설정" menuNameFirst="true" />

<form id="pageRecExtArea">
<input type="hidden" name="menuId" value="${menuId}" />
<c:set var="disabled" value="N"/>
<u:listArea>
	<tr><th width="18%" class="head_lt"></th>
		<th width="8%" class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></th>
		<th class="head_ct"><u:convertMap srcId="attachExtMap" attId="extTyp" var="extTyp" 
		/><select name="${attachPrefix}.extTyp" <u:elemTitle titleId="cols.useYn" /> <c:if test="${disabled eq 'Y' }">disabled="disabled"</c:if>>
			<u:option value="A" titleId="cols.allow" checkValue="${extTyp}" selected="${empty extTyp }"/>
			<u:option value="L" titleId="cols.limit" checkValue="${extTyp}" />
			</select> <u:msg alt="확장자" titleId="cols.ext" /></th>
		<th width="8%" class="head_ct"><u:msg alt="비고" titleId="cols.note" /></th>
	</tr>
	<c:forEach items="${pageRecSetupCdList}" var="pageRecSetupCd" varStatus="status">
	<tr>
		<td width="18%" class="head_lt">${pageRecSetupCd.rescNm}</td>
		<td class="body_ct"><u:convertMap srcId="attachExtMap" attId="${pageRecSetupCd.cd}.useYn" var="useYn" 
		/><u:set
		test="${pageRecSetupCd.cd == '_ALL'}" var="onchange" value="changeVa(this.value,'${attachPrefix}')" elseValue="" />
		<select id="useYn${status.count }" name="${attachPrefix}.${pageRecSetupCd.cd}.useYn" onchange="${onchange}" <u:elemTitle titleId="cols.useYn" /> <c:if test="${disabled eq 'Y' }">disabled="disabled"</c:if>>
			<u:option value="Y" title="Y" checkValue="${useYn}" selected="${empty useYn }"/>
			<u:option value="N" title="N" checkValue="${useYn}" />
			</select></td>
		<td><u:convertMap srcId="attachExtMap" attId="${pageRecSetupCd.cd}.ext" var="exts" 
		/><u:textarea id="ext${status.count }" name="${attachPrefix}.${pageRecSetupCd.cd}.ext" value="${exts}" titleId="cols.ext" valueOption="lower" valueAllowed="," maxByte="750" style="width:97%" rows="2" />	
		</td>
		<td class="body_ct"><c:if test="${status.first }"><u:buttonS titleId="cm.btn.copy" alt="복사" onclick="extCopy('ext${status.count }','${attachPrefix}');" /></c:if>
		<c:if test="${!status.first }"><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="$('#ext${status.count }').val('');" /></c:if></td>
		<%-- <c:if test="${pageRecSetupCd.cd == '_ALL' && (empty useYn || useYn eq 'N')}"><c:set var="disabled" value="Y"/></c:if> --%>
	</tr>
	</c:forEach>

</u:listArea>
</form>

<u:buttonArea>
	<c:if test="${_lang == 'ko'}"><u:button titleId="cols.ext" alt="확장자" onclick="viewExtsPop()" auth="A" /></c:if>
	<u:button titleId="cm.btn.save" alt="저장" onclick="doSubmit()" auth="A" />
</u:buttonArea>