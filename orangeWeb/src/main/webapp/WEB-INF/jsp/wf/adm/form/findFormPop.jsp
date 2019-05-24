<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="callback" test="${!empty param.callback }" value="${param.callback }" elseValue="setSelectForm"/>

<script type="text/javascript">
<!--
<c:if test="${!empty ptCompBVoList }">
<% // [메뉴] - 회사 변경 %>
function chnComp(compId){
	reloadFrame('setMnuFrm', './setMnuFrm.do?menuId=${menuId }&valUM=${param.valUM }&paramCompId='+compId);
}<% // [양식] - 회사 변경 %>
function chnFormComp(compId){
	if(compId===undefined) compId='';
	reloadFrame('formListAreaFrm', './mnuGrpFrm.do?menuId=${menuId}&callback=cancelHandler&treeSelectOption=2&paramCompId='+compId);
}
</c:if>
<% // [하단버튼:선택] - 양식선택 %>
function selectForm() {
	var arr=getIframeContent('formListAreaFrm').getSelectedFormData();
	if(arr==null){
		alertMsg('cm.msg.noSelect');
		<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
		return;
	}
	${callback}(arr);
	dialog.close('findFormDialog');
}
<% // 저장 후 트리 리로드 %>
function reloadMnuFrm(mnuPid, mnuId){
	getIframeContent('setMnuFrm').reloadMnuFrm(mnuPid, mnuId);
}
function openListFrm(){}
$(document).ready(function() {
});
//-->
</script>
<div style="width:400px;">

<u:set var="setFormUrl" test="${!empty paramCompId }" value="./findFormFrm.do?menuId=${menuId}&formNo=${param.formNo }&paramCompId=${paramCompId }&treeSelectOption=2&mdTypCd=${param.mdTypCd }&noInitClick=Y&callback=${param.callback }" elseValue="./findFormFrm.do?menuId=${menuId}&formNo=${param.formNo }&mdTypCd=${param.mdTypCd }&noInitClick=Y&callback=${param.callback }"/>
<iframe id="formListAreaFrm" name="formListAreaFrm" src="${setFormUrl }" style="width:100%;min-height:350px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>

<u:buttonArea topBlank="true">
	<u:button titleId="cm.btn.sel" alt="선택" onclick="selectForm();" />
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>

</div>
