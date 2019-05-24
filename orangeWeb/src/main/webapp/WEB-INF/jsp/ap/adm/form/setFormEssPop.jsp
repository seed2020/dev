<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// 저장 - 이미 저장된 폼의 정보만 수정 %>
function saveForm(){
	if(validator.validate("setFormEssPop")){
		var $form = $("#setFormEssPop");
		$form.attr("action", "./transFormEss.do");
		$form.attr('target','dataframe');
		$form.submit();
	}
}<%
// 확인(신규) or 양식 편집 - 양식 편집 화면으로 이동 %>
function editForm(){
	if(validator.validate("setFormEssPop")){
		var $form = $("#setFormEssPop");
		$form.attr("action", "./setFormEdit.do");
		$form.attr('target','');
		$form.submit();
	}
}<%
// [업무관리] 양식 열기 %>
function openWfFormList(){
	var url='./findFormPop.do?menuId=${menuId}&mdTypCd=AP&callback=setWfForm';
	parent.dialog.open('findFormDialog','<u:msg titleId="ap.cmpt.wfFormBody" alt="본문 (업무양식)" />', url);
}<%
// [업무관리] 양식 열기 - callback %>
function setWfForm(data){
	$("#wfFormNm").val(data.formNm);
	$("#wfFormNo").val(data.formNo);
	$("#wfGenId").val(data.genId);
	$("#wfRescId").val(data.rescId);
	
	var tplFormId = $("select[name='tplFormId']");
	tplFormId[0].selectedIndex = 0;
	tplFormId.uniform();
}
function checkWfForm(idx){
	if(idx!=0){
		$("#wfFormNm").val('');
		$("#wfFormNo").val('');
		$("#wfGenId").val('');
		$("#wfRescId").val('');
	}
}
//-->
</script>
<div style="width:550px">
<form id="setFormEssPop">
<input type="hidden" name="menuId" value="${menuId}" /><c:if test="${not empty param.formBxId}">
<u:input id="formBxId" type="hidden" value="${param.formBxId}" /></c:if><c:if test="${not empty param.formId}">
<u:input id="formId" type="hidden" value="${param.formId}" /></c:if>
<u:listArea>

	<tr>
	<td width="24%" class="head_lt"><u:msg titleId="cols.formBxNm" alt="양식함명" /></td>
	<td class="body_lt"><u:out value="${apFormBxDVo.rescNm}" /></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.formNm" alt="양식명" /></td>
	<td>
		<table cellspacing="0" cellpadding="0" border="0">
		<tr>
		<td id="langTypArea">
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
		<u:convert srcId="${apFormBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
		<u:set test="${status.first}" var="style" value="" elseValue="display:none" />
		<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
		<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.formBxNm" value="${rescVa}" style="${style}"
			maxByte="200" validator="changeLangSelector('setFormEssPop', id, va)" mandatory="Y" />
		</c:forEach>
		</td>
		<td>
		<c:if test="${fn:length(_langTypCdListByCompId)>1}">
			<select id="langSelector" onchange="changeLangTypCd('setFormEssPop','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
			</c:forEach>
			</select>
		</c:if>
		<u:input type="hidden" id="rescId" value="${apFormBVo.rescId}" />
		</td>
		</tr>
		</table>
	</td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="ap.jsp.setSubjCd" alt="문서 제목 설정" /></td>
	<td class="bodybg_lt"><select name="docSubjCd"><u:option
		value="empty" titleId="ap.subjCd.empty" alt="공백" checkValue="${apFormBVo.docSubjCd}" /><u:option
		value="formNm" titleId="ap.subjCd.formNm" alt="양식명" checkValue="${apFormBVo.docSubjCd}" /><u:option
		value="formNmDt" titleId="ap.subjCd.formNmDt" alt="양식명 + 년월일" checkValue="${apFormBVo.docSubjCd}" />
		</select></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.formTyp" alt="양식구분" /></td>
	<td class="bodybg_lt"><table cellspacing="0" cellpadding="0" border="0">
		<tr><c:forEach items="${formTypCdList}" var="formTypCd" varStatus="status">
		<u:radio name="formTypCd" value="${formTypCd.cd}" title="${formTypCd.rescNm}" checked="${formTypCd.cd == apFormBVo.formTypCd or (empty apFormBVo.formTypCd and status.first)}"
			/></c:forEach>
		</tr>
		</table></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="ap.doc.docKeepPrdNm" alt="보존기간" /></td>
	<td><select id="docKeepPrdCd" name="docKeepPrdCd"<u:elemTitle titleId="cols.makDoc" alt="기안문" />><c:forEach
			items="${docKeepPrdCdList}" var="docKeepPrdCd">
			<u:option value="${docKeepPrdCd.cd}" title="${docKeepPrdCd.rescNm}"
				selected="${docKeepPrdCd.cd == apFormBVo.docKeepPrdCd or (empty apFormBVo.docKeepPrdCd and status.first)}"
			/></c:forEach>
			</select></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.bodyOlineYn" alt="본문 외곽선 여부" /></td>
	<td><select name="bodyOlineYn" <u:elemTitle titleId="cols.bodyOlineYn" />>
		<u:option value="Y" title="Y" selected="${empty apFormBVo.bodyOlineYn or apFormBVo.bodyOlineYn=='Y'}" />
		<u:option value="N" title="N" selected="${apFormBVo.bodyOlineYn=='N'}" />
		</select></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.tplYn" alt="템플릿여부" /></td>
	<td><select name="tplYn" <u:elemTitle titleId="cols.tplYn" />>
		<u:option value="Y" title="Y" selected="${apFormBVo.tplYn=='Y'}" />
		<u:option value="N" title="N" selected="${empty apFormBVo.tplYn or apFormBVo.tplYn=='N'}" />
		</select></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
	<td><select name="useYn" <u:elemTitle titleId="cols.useYn" />>
		<u:option value="Y" titleId="cm.option.use" checkValue="${apFormBVo.useYn}" />
		<u:option value="N" titleId="cm.option.notUse" checkValue="${apFormBVo.useYn}" />
		</select></td>
	</tr>
	<c:if test="${empty param.formId}">
	<tr>
	<td class="head_lt"><u:msg titleId="ap.jsp.setApvForm.tplFormId" alt="템플릿 양식" /></td>
	<td><select name="tplFormId" <u:elemTitle titleId="ap.jsp.setApvForm.tplFormId" /> onchange="checkWfForm(this.selectedIndex)">
		<option value="">- <u:msg titleId="cm.btn.sel" alt="선택" /> -</option><c:forEach
			items="${tplApFormBVoList}" var="tplApFormBVo">
		<u:option value="${tplApFormBVo.formId}" title="${tplApFormBVo.rescNm}" /></c:forEach>
		</select></td>
	</tr>
	<c:if test="${sysPloc.wfEnable eq 'Y'}">
	<tr>
	<td class="head_lt"><u:msg titleId="ap.cmpt.wfFormBody" alt="본문 (업무양식)" /></td>
	<td><table cellspacing="0" cellpadding="0" border="0"><tr>
			<td><input id="wfFormNm" name="wfFormNm" value="" style="width:250px" /></td><td>
			<input type="hidden" id="wfFormNo" name="wfFormNo" value="" />
			<input type="hidden" id="wfGenId" name="wfGenId" value="" />
			<input type="hidden" id="wfRescId" name="wfRescId" value="" />
			<u:buttonS titleId="cm.btn.sel"  href="javascript:openWfFormList();" alt="선택" auth="A" /></td></tr></table></td>
	</tr>
	</c:if>
	</c:if>
	
</u:listArea>

<u:buttonArea>
	<c:if test="${empty apFormBVo}">
	<u:button titleId="cm.btn.save" href="javascript:saveForm();" alt="저장" auth="A" />
	</c:if>
	<c:if test="${not empty apFormBVo}">
	<u:button titleId="cm.btn.save" href="javascript:saveForm();" alt="저장" auth="A" />
	<u:button titleId="ap.jsp.setApvForm.editForm" href="./setFormEdit.do?menuId=${menuId}&formId=${param.formId}&formBxId=${param.formBxId}" alt="양식 편집" auth="A" />
	</c:if>
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</form>
</div>