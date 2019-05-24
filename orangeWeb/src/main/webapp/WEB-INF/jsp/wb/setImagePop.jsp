<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function savePhoto(){
	if(validator.validate('photoImageForm')){
		var $form = $("#photoImageForm");
		$form.attr('target','dataframe');
		$form[0].submit();
	}
}
//-->
</script>
<div style="width:400px">
<form id="photoImageForm" method="post" enctype="multipart/form-data" action="./transImage.do?menuId=${menuId }">
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="bcId" value="${param.bcId}" />
<u:listArea>
	<tr>
	<th width="30%" class="head_lt"><u:msg alt="사진 선택" titleId="or.jsp.setOrg.photoTitle" /></th>
	<td width="70%"><u:file id="photo" titleId="or.jsp.setOrg.photoTitle" alt="사진 선택"
		mandatory="Y" exts="gif,jpg,jpeg,png,tif" /></td>
	</tr>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="savePhoto();" alt="저장" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>