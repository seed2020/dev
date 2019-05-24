<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// 저장 버튼 %>
function saveUpload(){
	var $form = $("#uploadForm");
	if($form.find("#jsonFile").val()==''){<%
		// cm.msg.noFileSelected=선택한 파일이 없습니다. %>
		alertMsg('cm.msg.noFileSelected');
	} else {
		loading($('#setFormUploadDialog'),true);
		$form.attr("target", "dataframe").submit();
	}
}
function setFormDisabled(){
	setDisabled($('#writtenReqNo'), obj.value=='N');
}<%
// 에러시 인디케이터 제거 %>
function errPopClose(){
	loading($('#setFormUploadDialog'),false);
}
//-->
</script>
<div style="width:400px">
<form id="uploadForm" method="post" enctype="multipart/form-data" action="./transFormJson.do?menuId=${menuId}">
<input type="hidden" name="menuId" value="${menuId }"/>
<input type="hidden" name="dialogId" value="setFormUploadDialog"/>
<c:if test="${!empty param.formNo }"
><input type="hidden" name="formNo" value="${param.formNo }"
/><input type="hidden" name="regDataYn" value="Y"
/></c:if
><c:if test="${!empty param.grpId }"><input type="hidden" name="grpId" value="${param.grpId }"/></c:if>
<c:if test="${empty param.formNo }"
><input type="hidden" name="formDataYn" value="Y"
/><input type="hidden" name="regDataYn" value="Y"
/></c:if>
<u:listArea>
<tr><th class="head_lt"><u:msg alt="파일첨부" titleId="cm.btn.fileAtt" /></th>
<td><u:file id="json" titleId="cm.btn.fileAtt" alt="파일첨부"
mandatory="Y" exts="json" /></td>
</tr>
</u:listArea>

</form>
<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="saveUpload();" alt="저장" auth="A"/>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</div>