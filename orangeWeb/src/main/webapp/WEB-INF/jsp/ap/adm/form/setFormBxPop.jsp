<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// 저장 - 버튼 클릭 %>
function saveFormBx(){
	if(validator.validate("setFormBxForm")){
		var $form = $("#setFormBxForm");
		$form.attr("action", "./transFormBx.do");
		$form.attr('target','dataframe');
		$form.submit();
	}
}<%
// 부서선택 - 버튼 클릭 %>
function setFormBxDept(){
	var $form = $("#setFormBxForm"), data = [];
	var $formBxDeptIds = $form.find("#formBxDeptIds");
	var $deptNmDispArea = $form.find("#deptNmDispArea");
	
	var formBxDeptIds = $formBxDeptIds.val();
	if(formBxDeptIds!=null && formBxDeptIds!=''){
		formBxDeptIds.split(',').each(function(index, orgId){
			data.push({orgId:orgId});
		});
	}
	
	searchOrgPop({data:data, multi:true, mode:"search"}, function(arr){
		var ids=[], html=[];
		if(arr!=null){
			arr.each(function(index, orgVo){
				if(index!=0) html.push(", ");
				html.push("<nobr>");
				html.push(orgVo.rescNm);
				html.push("</nobr>");
				
				ids.push(orgVo.orgId);
			});
			
			$formBxDeptIds.val(ids.join(","));
			$deptNmDispArea.html(html.join(""));
		} else {
			$formBxDeptIds.val("");
			$deptNmDispArea.html("");
		}
		dialog.resize("setFormBxDialog");
	});
}
//-->
</script>
<div style="width:450px">
<form id="setFormBxForm">
<input type="hidden" name="menuId" value="${menuId}" /><c:if
	test="${not empty param.formBxId}">
<u:input id="formBxId" type="hidden" value="${param.formBxId}" /></c:if><c:if
	test="${not empty param.formBxPid}">
<u:input id="formBxPid" type="hidden" value="${param.formBxPid}" /></c:if><c:if
	test="${optConfigMap.formBxByDept eq 'Y' }">
<u:input id="formBxDeptIds" type="hidden" value="${formBxDeptIds}" /></c:if>
<u:listArea>

	<tr>
	<td width="30%" class="head_lt"><u:mandatory /><u:msg titleId="cols.formBxNm" alt="양식함명" /></td>
	<td>
		<table cellspacing="0" cellpadding="0" border="0">
		<tr>
		<td id="langTypArea">
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
		<u:convert srcId="${apFormBxDVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
		<u:set test="${status.first}" var="style" value="" elseValue="display:none" />
		<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
		<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.formBxNm" value="${rescVa}" style="${style}"
			maxByte="200" validator="changeLangSelector('setFormBxForm', id, va)" mandatory="Y" />
		</c:forEach>
		</td>
		<td>
		<c:if test="${fn:length(_langTypCdListByCompId)>1}">
			<select id="langSelector" onchange="changeLangTypCd('setFormBxForm','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
			</c:forEach>
			</select>
		</c:if>
		<u:input type="hidden" id="rescId" value="${apFormBxDVo.rescId}" />
		</td>
		</tr>
		</table>
	</td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
	<td><select id="useYn${cdIndex}" name="useYn" <u:elemTitle titleId="cols.useYn" />>
		<u:option value="Y" titleId="cm.option.use" checkValue="${apFormBxDVo.useYn}" />
		<u:option value="N" titleId="cm.option.notUse" checkValue="${apFormBxDVo.useYn}" />
		</select></td>
	</tr><c:if
		test="${optConfigMap.formBxByDept eq 'Y' }">
	<tr>
	<td class="head_lt"><u:msg titleId="pt.btn.dept" alt="부서" /></td>
	<td class="body_lt" id="deptNmDispArea" data-orgIds="${orgIds}"><c:forEach
		items="${orOrgTreeVoList}" var="orOrgTreeVo" varStatus="status"><c:if
			test="${not status.first}">, </c:if>
		<nobr>${orOrgTreeVo.rescNm}</nobr></c:forEach></td>
	</tr></c:if>
	
</u:listArea>

<u:buttonArea><c:if
		test="${optConfigMap.formBxByDept eq 'Y' }">
	<u:button titleId="or.jsp.searchOrgPop.title" href="javascript:setFormBxDept();" alt="부서 선택" auth="A" /></c:if>
	<u:button titleId="cm.btn.save" href="javascript:saveFormBx();" alt="저장" auth="A" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</form>
</div>