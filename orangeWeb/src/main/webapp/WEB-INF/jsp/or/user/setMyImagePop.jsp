<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function savePhoto(){
	if(validator.validate('changeImageForm')){
		var $form = $("#changeImageForm");
		$form.attr('action','/or/user/transMyImage.do');
		$form.attr('target','dataframe');
		$form.submit();
	}
}
//-->
</script>
<div style="width:400px">
<form id="changeImageForm" method="post" enctype="multipart/form-data">
<input type="hidden" name="userImgTypCd" value="${param.userImgTypCd}" /><%//01:도장, 02:싸인, 03:사진 %>
<u:listArea>
<c:if
	test="${param.userImgTypCd=='01'}">
	<tr>
	<th width="30%" class="head_lt"><u:msg alt="도장 선택" titleId="or.jsp.setOrg.stampTitle" /></th>
	<td width="70%"><u:file id="photo" titleId="or.jsp.setOrg.stampTitle" alt="도장 선택"
		mandatory="Y" exts="gif,jpg,jpeg,png,tif" /></td>
	</tr></c:if><c:if
	test="${param.userImgTypCd=='02'}">
	<tr>
	<th width="30%" class="head_lt"><u:msg alt="서명 선택" titleId="or.jsp.setOrg.signTitle" /></th>
	<td width="70%"><u:file id="photo" titleId="or.jsp.setOrg.signTitle" alt="서명 선택"
		mandatory="Y" exts="gif,jpg,jpeg,png,tif" /></td>
	</tr></c:if><c:if
	test="${param.userImgTypCd=='03'}">
	<tr>
	<th width="30%" class="head_lt"><u:msg alt="사진 선택" titleId="or.jsp.setOrg.photoTitle" /></th>
	<td width="70%"><u:file id="photo" titleId="or.jsp.setOrg.photoTitle" alt="사진 선택"
		mandatory="Y" exts="gif,jpg,jpeg,png,tif" /></td>
	</tr></c:if>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="savePhoto();" alt="저장" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>