<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%
// openStylePop() - setFormEdit.jsp
%>
<%// 스타일 소버튼 후 저장 %>
function applyStylePop(formTxtTypCd){
	var param = getStyleData();<%// getStyleData() - setDocStylePop.jsp %>
	param.setData(formTxtTypCd+"PopArea", formTxtTypCd+"-");
}
<%// 텍스트 입력값이 없으면 사용여부 언체크 함 %>
function onTxtContBlur(obj){
	obj.value = obj.value.trim();
	if(obj.value!=''){
		var id = $(obj).attr("id");
		var $useYn = $("#"+(id.substring(0, id.indexOf('-')))+"-useYn");
		if(!$useYn[0].checked) $useYn.trigger('click');
	}
}
<%// 확인 버튼 클릭 %>
function setFooter(){
	var param = new ParamMap().getData("setFooterPopForm");
	setFooterData(param);<%// - setFormEdit.jsp %>
	dialog.close("setDocFooterDialog");
}

$(document).ready(function() {
	var param = new ParamMap().getData("footerDataArea");
	param.setData("setFooterPopForm");
});
//-->
</script>
<div style="width:650px">
<form id="setFooterPopForm">
<u:listArea>

	<tr>
	<td width="20%" class="head_lt"><u:msg titleId="ap.cmpt.footer" alt="바닥글" /></td>
	<td class="body_lt" id="docFooterPopArea"><u:checkArea>
		<u:checkbox value="Y" id="docFooter-useYn" name="docFooter-useYn" titleId="cols.useYn" onclick="if(!this.checked) $('#docFooter-txtCont').val('');" alt="사용여부" checked="${not empty docFooter.txtCont}" />
		<td><u:input id="docFooter-txtCont" titleId="ap.cmpt.footer" value="${docFooter.txtCont}" style="width:350px" maxByte="200" onblur="onTxtContBlur(this);" />
			<u:input type="hidden" id="docFooter-txtFontVa" value="${docFooter.txtFontVa}" />
			<u:input type="hidden" id="docFooter-txtStylVa" value="${docFooter.txtStylVa}" />
			<u:input type="hidden" id="docFooter-txtSize" value="${docFooter.txtSize}" />
			<u:input type="hidden" id="docFooter-txtColrVa" value="${docFooter.txtColrVa}" /></td>
		<td><u:buttonS titleId="ap.cmpt.style" alt="스타일" onclick="openStylePop('docFooter', 'ap.cmpt.footer');" /></td>
	</u:checkArea></td>
	</tr>

</u:listArea>

<div>
	※ <u:msg titleId="ap.dept.replace" alt="부서정보" /> - &nbsp;
	<u:msg titleId="cols.phon" alt="전화번호" /> : #phone,
	<u:msg titleId="cols.fno" alt="팩스번호" /> : #fax,
	<u:msg titleId="cols.email" alt="이메일" /> : #email,
	<u:msg titleId="cols.hpageUrl" alt="홈페이지URL" /> : #homepage,
	<u:msg titleId="cols.adr" alt="주소" /> : #address
</div>
<div class="blank"></div>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" href="javascript:setFooter();" alt="확인" auth="A" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</form>
</div>