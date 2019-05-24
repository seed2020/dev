<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
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
function setPrinter(){
	var param = new ParamMap();
	$form = $("#setPrinterPopForm");
	var formWdthTypCd = $form.find("[name=formWdthTypCd] option:selected").val();
	param.put("formWdthTypCd", formWdthTypCd);<%//양식넓이%>
	var bodyHghtPx = $form.find("[name='bodyHght-useYn']")[0].checked ? $form.find("[name='bodyHght-bodyHghtPx']").val() : "";
	param.put("bodyHghtPx", bodyHghtPx);<%//본문높이%>
	
	var $formMagnArea = $("#formMagnArea"), arr=[];
	['top','right','bottom','left'].each(function(index, where){
		var va = $formMagnArea.find('#'+where).val();
		if(va==null || va=='') va = '0';
		arr.push(va+'px');
	});
	param.put("formMagnVa", arr.join(' '));
	param.setData("printerDataArea");
	$(".homewrapper:first").attr("class", "homewrapper "+formWdthTypCd);<%//브라우져의 인쇄넓이 세팅함 %>
	$("#bodyHtmlViewArea .editor").css("min-height", bodyHghtPx+(bodyHghtPx=="" ? "" : "px"));<%//본문높이 세팅함 %>
	dialog.close("setDocPrinterDialog");
}

$(document).ready(function() {
	var param = new ParamMap().getData("printerDataArea");
	$form = $("#setPrinterPopForm");
	if(param.get("formWdthTypCd")!=null) $form.find("[name=formWdthTypCd]").val(param.get("formWdthTypCd"));
	if(param.get("bodyHghtPx")!=null && param.get("bodyHghtPx")!=""){
		$form.find("[name='bodyHght-useYn']").attr("checked","checked");
		$form.find("[name='bodyHght-bodyHghtPx']").val(param.get("bodyHghtPx"));
	} else {
		$form.find("[name='bodyHght-bodyHghtPx']").val("<u:msg titleId="ap.cmpt.printer.dftBodyHigt" alt="400" />");
	}
	var formMagn = param.get('formMagnVa');
	if(formMagn!=null && formMagn!=''){
		var magns = formMagn.replace(/px/gi, '').split(' ');
		var $formMagnArea = $("#formMagnArea");
		['top','right','bottom','left'].each(function(index, where){
			if(index<magns.length && magns[index]!='0'){
				$formMagnArea.find('#'+where).val(magns[index]);
			}
		});
	}
	param.setData("setPrinterPopForm");
});
//-->
</script>
<div style="width:650px">
<form id="setPrinterPopForm">
<u:listArea>

	<tr>
	<td width="20%" class="head_lt"><u:msg titleId="cols.formWdth" alt="양식넓이" /></td>
	<td class="body_lt"><select id="formWdth-formWdthTypCd" name="formWdthTypCd"<u:elemTitle titleId="cols.formWdth" alt="양식넓이" />>
		<option value="">- <u:msg titleId="cm.btn.sel" alt="선택" /> -</option><c:forEach
			items="${formWdthTypCdList}" var="formWdthTypCd">
		<option value="${formWdthTypCd.cd}">${formWdthTypCd.rescNm}</option></c:forEach>
		</select></td>
	</tr>
	
	<tr>
	<td width="20%" class="head_lt"><u:msg titleId="cols.bodyHght" alt="본문높이" /></td>
	<td class="body_lt" ><u:checkArea>
		<u:checkbox value="Y" id="bodyHght-useYn" name="bodyHght-useYn" titleId="cols.useYn" onclick="if(!this.checked) $('#setPrinterPopForm #bodyHght-bodyHghtPx').val('');" alt="사용여부" />
		<td><u:input id="bodyHght-bodyHghtPx" titleId="cols.bodyHght" value="" style="width:100px" maxInt="1000" onblur="onTxtContBlur(this);" valueOption="number" /> px</td>
	</u:checkArea></td>
	</tr>
	
	<tr>
	<td width="20%" class="head_lt"><u:msg titleId="cols.formMagn" alt="양식여백" /></td>
	<td class="body_lt" id="formMagnArea" ><u:checkArea>
		
		<td><u:msg titleId="cols.top" alt="상" /> :
			<u:input id="top" style="width:35px" maxLength="3" valueOption="number" /> px</td>
		<td style="width:30px"></td>
		<td><u:msg titleId="cols.bottom" alt="하" /> :
			<u:input id="bottom" style="width:35px" maxLength="3" valueOption="number" /> px</td>
		<td style="width:30px"></td>
		<td><u:msg titleId="cols.left" alt="좌" /> :
			<u:input id="left" style="width:35px" maxLength="3" valueOption="number" /> px</td>
		<td style="width:30px"></td>
		<td><u:msg titleId="cols.right" alt="우" /> :
			<u:input id="right" style="width:35px" maxLength="3" valueOption="number" /> px</td>
	</u:checkArea></td>
	</tr>
	
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" href="javascript:setPrinter();" alt="확인" auth="A" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</form>
</div>