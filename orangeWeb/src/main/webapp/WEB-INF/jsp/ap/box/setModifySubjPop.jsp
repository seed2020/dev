<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.net.URLEncoder"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--
function setNewDocSubj(){
	var $newArea = $("#setNewDocSubjArea");
	var newSubj = $newArea.find("#newDocSubj").val();
	
	var $area = $("#docDataArea #docInfoArea");
	var obj = $area.find("input[name='docSubj']");
	if(obj.length==1){
		obj.val(newSubj);
	} else {
		$area.append($('<input>', {'name':'docSubj','value':newSubj,'type':'hidden'}));
	}
	
	var $form = $("#docForm");
	$form.children("#docArea").children().each(function(){
		if($(this).attr('id')=='itemsArea'){
			$(this).find("td[id='docSubjView']").text(newSubj);
		}
	});
	
	dialog.close("setModifySubjDialog");
}
$(document).ready(function() {
	var $area = $("#docDataArea #docInfoArea");
	var docSubj = $area.find("input[name='docSubj']").val();
	if(docSubj!=null && docSubj!=''){
		var $newArea = $("#setNewDocSubjArea");
		$newArea.find("#oldDocSubj").text(docSubj);
		$newArea.find("#newDocSubj").val(docSubj);
	}
});
//-->
</script>
<div id="setNewDocSubjArea" style="width:550px">

<u:listArea colgroup="20%,*">
	<tr>
	<td class="head_ct"><u:msg titleId="ap.jsp.oldTitle" alt="이전 제목" /></td>
	<td class="body_lt" id="oldDocSubj"><u:out value="${docSubj}" /></td>
	</tr>
	
	<tr>
	<td class="head_ct"><u:msg titleId="ap.jsp.newTitle" alt="신규 제목" /></td>
	<td><u:input id="newDocSubj" value="${docSubj}" titleId="ap.jsp.oldTitle" alt="이전 제목" maxByte="800" style="width:97%" /></td>
	</tr>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" onclick="setNewDocSubj();" alt="확인" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>