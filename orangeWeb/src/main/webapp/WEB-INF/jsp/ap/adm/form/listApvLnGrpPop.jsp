<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// 확인 클릭 - 양식 결재선 %>
function setFormApvLnGrp(){
	var $form = $("#setFormApvLnGrpForm");
	var apvLnGrpId = $form.find("select[name='apvLnGrpId']").val();
	var fixdApvrYn = $form.find("select[name='fixdApvrYn']").val();
	var autoApvLnCd = $form.find("select[name='autoApvLnCd']").val();
	setFromApvLnData({apvLnGrpId:apvLnGrpId, fixdApvrYn:fixdApvrYn, autoApvLnCd:autoApvLnCd});<%// - setFormEdit.jsp %>
	dialog.close("setFromApvLnDialog");
}<%
// 확인 클릭 - 양식 참조열람 %>
function setFormRefVwGrp(){
	var $form = $("#setFormApvLnGrpForm");
	var refVwGrpId = $form.find("select[name='refVwGrpId']").val();
	var refVwFixdApvrYn = $form.find("select[name='refVwFixdApvrYn']").val();
	setFromApvLnData({refVwGrpId:refVwGrpId, refVwFixdApvrYn:refVwFixdApvrYn});<%// - setFormEdit.jsp %>
	dialog.close("setFromRefVwDialog");
}
$(document).ready(function() {
	var apvLnGrpData = getFromApvLnData();<%// - setFormEdit.jsp %>
	var apvLnFormEdit = "${not empty apvLnFormEdit ? 'true' : 'false'}" == "true";
	var refVwFormEdit = "${not empty refVwFormEdit ? 'true' : 'false'}" == "true";
	
	var $form = $("#setFormApvLnGrpForm");
	if(apvLnFormEdit){
		if(apvLnGrpData.apvLnGrpId!=null && apvLnGrpData.apvLnGrpId!=''){
			$form.find('#apvLnGrpId').val(apvLnGrpData.apvLnGrpId);
			$form.find('#fixdApvrYn').val(apvLnGrpData.fixdApvrYn);
		}
		if(apvLnGrpData.autoApvLnCd!=null && apvLnGrpData.autoApvLnCd!=''){
			$form.find('#autoApvLnCd').val(apvLnGrpData.autoApvLnCd);
		}
	} else if(refVwFormEdit){
		if(apvLnGrpData.refVwGrpId!=null && apvLnGrpData.refVwGrpId!=''){
			$form.find('#refVwGrpId').val(apvLnGrpData.refVwGrpId);
			$form.find('#refVwFixdApvrYn').val(apvLnGrpData.refVwFixdApvrYn);
		}
	}
});
//-->
</script>
<div style="width:350px">
<form id="setFormApvLnGrpForm"><c:if
	test="${not empty apvLnFormEdit}">
<u:listArea colgroup="40%,60%">
<tr>
	<td class="head_lt"><u:msg titleId="cols.autoApvLnCd" alt="자동결재선코드"/></td>
	<td><select id="autoApvLnCd" name="autoApvLnCd" <u:elemTitle titleId="cols.autoApvLnCd" alt="자동결재선코드" />>
		<u:option value="" titleId="cm.option.notUse" /><c:forEach
		items="${autoApvLnCdList}" var="autoApvLnCdVo" varStatus="seculStatus">
		<u:option value="${autoApvLnCdVo.cd}" title="${autoApvLnCdVo.rescNm}" checkValue="${orUserBVo.autoApvLnCd}"
		/></c:forEach>
	</select></td>
</tr>
</u:listArea>
<u:listArea colgroup="40%,60%">
<tr>
	<td class="head_lt"><u:msg titleId="ap.jsp.pubApvLnGrp" alt="공용경로그룹" /></td>
	<td><select id="apvLnGrpId" name="apvLnGrpId" <u:elemTitle titleId="ap.jsp.pubApvLnGrp" />>
		<option value=""><u:msg titleId="cm.option.noSelect" alt="선택안함" /></option><c:forEach
			items="${apApvLnGrpBVoList}" var="apApvLnGrpBVo" varStatus="status">
		<u:option value="${apApvLnGrpBVo.apvLnGrpId}" title="${apApvLnGrpBVo.rescNm}"
		/></c:forEach>
		</select></td>
</tr>
<tr>
	<td class="head_lt"><u:msg titleId="ap.jsp.fixdApvr" alt="결재라인 고정" /></td>
	<td><select id="fixdApvrYn" name="fixdApvrYn" <u:elemTitle titleId="ap.jsp.fixdApvr" />>
		<u:option value="Y" titleId="cm.option.use" />
		<u:option value="N" titleId="cm.option.notUse" />
		</select></td>
</tr>
</u:listArea>
</c:if><c:if
	test="${not empty refVwFormEdit}">
<u:listArea colgroup="40%,60%">
<tr>
	<td class="head_lt"><u:msg titleId="ap.jsp.pubApvLnGrp" alt="공용경로그룹" /></td>
	<td><select id="refVwGrpId" name="refVwGrpId" <u:elemTitle titleId="ap.jsp.pubApvLnGrp" />>
		<option value=""><u:msg titleId="cm.option.noSelect" alt="선택안함" /></option><c:forEach
			items="${apApvLnGrpBVoList}" var="apApvLnGrpBVo" varStatus="status">
		<u:option value="${apApvLnGrpBVo.apvLnGrpId}" title="${apApvLnGrpBVo.rescNm}"
		/></c:forEach>
		</select></td>
</tr>
<tr>
	<td class="head_lt"><u:msg titleId="ap.jsp.fixdApvr" alt="결재라인 고정" /></td>
	<td><select id="refVwFixdApvrYn" name="refVwFixdApvrYn" <u:elemTitle titleId="ap.jsp.fixdApvr" />>
		<u:option value="Y" titleId="cm.option.use" />
		<u:option value="N" titleId="cm.option.notUse" />
		</select></td>
</tr>
</u:listArea>
</c:if>

</form>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" href="javascript:${not empty apvLnFormEdit ? 'setFormApvLnGrp()' : 'setFormRefVwGrp()'};" alt="확인" auth="A" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>
</div>