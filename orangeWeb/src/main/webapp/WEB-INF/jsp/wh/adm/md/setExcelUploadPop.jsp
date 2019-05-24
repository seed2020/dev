<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// 저장 버튼 %>
function uploadExcel(){
	var $form = $("#setExcelForm");
	if($form.find("#excelFile").val()==''){<%
		// cm.msg.noFileSelected=선택한 파일이 없습니다. %>
		alertMsg('cm.msg.noFileSelected');
	} else {
		loading($('#setExcelUploadDialog'),true);
		$form.attr("target", "dataframe").submit();
	}
}<%
// 에러시 인디케이터 제거 %>
function errPopClose(){
	loading($('#setExcelUploadDialog'),false);
}
//-->
</script>
<div style="width:450px">
<form id="setExcelForm" action="./transExcelUpload.do?menuId=${menuId}" method="post" enctype="multipart/form-data">
<u:listArea><tr><td width="35%" class="head_lt"><u:msg titleId="cm.excel" alt="엑셀파일" /></td>
		<td><u:file id="excel" titleId="cm.excel" exts="xls,xlsx" /></td></tr></u:listArea>
<u:buttonArea>
	<u:button titleId="cm.btn.save" href="javascript:uploadExcel();" alt="저장" auth="A" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</form>
</div>