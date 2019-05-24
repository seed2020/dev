<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.Enumeration, java.util.ArrayList" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%//사용자 정보 변경 %>
function chkGubuns(obj){
	var val = $(obj).val();
	if(/[\{\}\[\]\/?.:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/g.test(val))
		$(obj).val(val.replace(/[\{\}\[\]\/?.;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"\s]/g, ''));
}
<% // 저장 - 버튼 클릭 %>
function saveEnv(){
	if(validator.validate('setEnvForm')){
		var $form = $("#setEnvForm");
		$form.attr('method','post');
		$form.attr('action','./transEnv.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form.submit();
		location.replace(location.href);
		dialog.close('setEnvDialog');
	}
};
$(document).ready(function() {
	//setUniformCSS();
});
//-->
</script>
<div style="width:400px; ">

<form id="setEnvForm">
<input type="hidden" name="menuId" value="${menuId}" />
<c:set var="colgroup" value="25%,75%"/>
<u:listArea colgroup="${colgroup }" noBottomBlank="true">
<tr>
		<td class="head_ct"><u:mandatory />담당자</td>
		<td class="body_lt"><u:textarea id="chargers" value="${envConfigMap.chargers }" title="담당자" maxByte="300" style="width:95%" rows="4" mandatory="Y" onblur="chkGubuns(this);"/></td></tr>
</u:listArea>
</form>
<u:blank />
<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" onclick="saveEnv()" auth="A" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>