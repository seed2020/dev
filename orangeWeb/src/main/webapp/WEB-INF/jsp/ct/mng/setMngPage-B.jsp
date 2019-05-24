<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

$(function() {
	
	var $ctTplFileSubj = "${tpl}";
	if($ctTplFileSubj == ""){
		$('input:radio[name=tpl]:input[value=A]').attr("checked", true);
		$("#fnc").val('reg');
	}else{
		$('input:radio[name=tpl]:input[value=${tpl}]').attr("checked", true);
		$("#fnc").val('mod');
	}
	

});

function nextSubmit(){
	var tpl =  $(':radio[name=tpl]:checked').val();
	$("#template").val(tpl);
	
	if(validator.validate('setMngPageForm')){
		var $form = $('#setMngPageForm');
	    $form.attr('method','post');
	    $form.attr('action','./setMngPageSetup.do?menuId=${menuId}&ctId=${ctId}');
	    $form.submit();
	}
	
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="ct.jsp.setMngPage.title" alt="초기 페이지 구성" menuNameFirst="true"/>

<div class="front">
	<div class="front_left">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="red_stxt">* <u:msg titleId="ct.jsp.setMngPage.tx01" alt="선택된 초기 페이지 템플릿이 없습니다." /></td>
		</tr>
		</table>
	</div>
</div>
<form id="setMngPageForm">
<input type="hidden" id="template" name="template" />
<input type="hidden" id="fnc" name="fnc" />

<% // 표 %>
<div class="listarea">

	<div style="float:left; width:24.4%; padding:0 8px 8px 0;">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<tr>
	<td class="head_rd"><u:checkArea>
		<u:radio name="tpl" value="A" title="template_A.html
" inputClass="head_bg" textClass="head_lt" noSpaceTd="true" />
		</u:checkArea></td>
	</tr>

	<tr>
	<td style="padding:7px;height:178px;vertical-align:top;"><table class="listtable" border="0" cellpadding="7" cellspacing="3">
		<tr>
		<td colspan="2" class="body_ct" style="height:50px;"><u:msg titleId="ct.jsp.setMngPage.itroMsg" alt="소개글" /></td>
		</tr>

		<tr>
		<td class="body_ct" width="50%"><u:msg titleId="ct.cols.field" alt="영역" /> 1</td>
		<td class="body_ct" width="50%"><u:msg titleId="ct.cols.field" alt="영역" /> 2</td>
		</tr>

		<tr>
		<td class="body_ct"><u:msg titleId="ct.cols.field" alt="영역" /> 3</td>
		<td class="body_ct"><u:msg titleId="ct.cols.field" alt="영역" /> 4</td>
		</tr>
		</table></td>
	</tr>
	</table>
	</div>

	<div style="float:left; width:24.4%; padding:0 8px 8px 0;">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<tr>
	<td class="head_rd"><u:checkArea>
		<u:radio name="tpl" value="B" title="template_B.html
" inputClass="head_bg" textClass="head_lt" noSpaceTd="true" />
		</u:checkArea></td>
	</tr>

	<tr>
	<td style="padding:7px;height:178px;vertical-align:top;"><table class="listtable" border="0" cellpadding="7" cellspacing="3">
		<tr>
		<td class="body_ct" style="height:50px;"><u:msg titleId="ct.jsp.setMngPage.itroMsg" alt="소개글" /></td>
		</tr>

		<tr>
		<td class="body_ct" width="50%"><u:msg titleId="ct.cols.field" alt="영역" /> 1</td>
		</tr>

		<tr>
		<td class="body_ct" width="50%"><u:msg titleId="ct.cols.field" alt="영역" /> 2</td>
		</tr>

		<tr>
		<td class="body_ct" width="50%"><u:msg titleId="ct.cols.field" alt="영역" /> 3</td>
		</tr>

		<tr>
		<td class="body_ct" width="50%"><u:msg titleId="ct.cols.field" alt="영역" /> 4</td>
		</tr>
		</table></td>
	</tr>
	</table>
	</div>

	<div style="float:left; width:24.4%; padding:0 8px 8px 0;">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<tr>
	<td class="head_rd"><u:checkArea>
		<u:radio name="tpl" value="C" title="template_C.html
" inputClass="head_bg" textClass="head_lt" noSpaceTd="true" />
		</u:checkArea></td>
	</tr>

	<tr>
	<td style="padding:7px;height:178px;vertical-align:top;"><table class="listtable" border="0" cellpadding="7" cellspacing="3">
		<tr>
		<td colspan="2" class="body_ct" style="height:50px;"><u:msg titleId="ct.jsp.setMngPage.itroMsg" alt="소개글" /></td>
		</tr>

		<tr>
		<td class="body_ct" width="50%"><u:msg titleId="ct.cols.field" alt="영역" /> 1</td>
		<td class="body_ct" width="50%"><u:msg titleId="ct.cols.field" alt="영역" /> 2</td>
		</tr>
		</table></td>
	</tr>
	</table>
	</div>

	<div style="float:left; width:24.4%; padding:0 0 8px 0;">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<tr>
	<td class="head_rd"><u:checkArea>
		<u:radio name="tpl" value="D" title="template_D.html
" inputClass="head_bg" textClass="head_lt" noSpaceTd="true" />
		</u:checkArea></td>
	</tr>

	<tr>
	<td style="padding:7px;height:178px;vertical-align:top;"><table class="listtable" border="0" cellpadding="7" cellspacing="3">
		<tr>
		<td width="50%" rowspan="2" class="body_ct"><u:msg titleId="ct.jsp.setMngPage.itroMsg" alt="소개글" /></td>
		<td class="body_ct" width="50%"><u:msg titleId="ct.cols.field" alt="영역" /> 1</td>
		</tr>

		<tr>
		<td class="body_ct"><u:msg titleId="ct.cols.field" alt="영역" /> 2</td>
		</tr>

		<tr>
		<td class="body_ct"><u:msg titleId="ct.cols.field" alt="영역" /> 3</td>
		<td class="body_ct"><u:msg titleId="ct.cols.field" alt="영역" /> 4</td>
		</tr>
		</table></td>
	</tr>
	</table>
	</div>

	<div style="float:left; width:24.4%; padding:0 8px 8px 0;">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<tr>
	<td class="head_rd"><u:checkArea>
		<u:radio name="tpl" value="E" title="template_E.html
" inputClass="head_bg" textClass="head_lt" noSpaceTd="true" />
		</u:checkArea></td>
	</tr>

	<tr>
	<td style="padding:7px;height:178px;vertical-align:top;"><table class="listtable" border="0" cellpadding="7" cellspacing="3">
		<tr>
		<td colspan="2" class="body_ct" style="height:50px;"><u:msg titleId="ct.jsp.setMngPage.itroMsg" alt="소개글" /></td>
		</tr>

		<tr>
		<td colspan="2" class="body_ct"><u:msg titleId="ct.cols.field" alt="영역" /> 1</td>
		</tr>

		<tr>
		<td colspan="2" class="body_ct"><u:msg titleId="ct.cols.field" alt="영역" /> 2</td>
		</tr>

		<tr>
		<td class="body_ct" width="50%"><u:msg titleId="ct.cols.field" alt="영역" /> 3</td>
		<td class="body_ct" width="50%"><u:msg titleId="ct.cols.field" alt="영역" /> 4</td>
		</tr>
		</table></td>
	</tr>
	</table>
	</div>

	<div style="float:left; width:24.4%; padding:0 8px 8px 0;">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<tr>
	<td class="head_rd"><u:checkArea>
		<u:radio name="tpl" value="F" title="template_F.html
" inputClass="head_bg" textClass="head_lt" noSpaceTd="true" />
		</u:checkArea></td>
	</tr>

	<tr>
	<td style="padding:7px;height:178px;vertical-align:top;"><table class="listtable" border="0" cellpadding="7" cellspacing="3">
		<tr>
		<td colspan="2" class="body_ct" style="height:50px;"><u:msg titleId="ct.jsp.setMngPage.itroMsg" alt="소개글" /></td>
		</tr>
		</table></td>
	</tr>
	</table>
	</div>

	<div style="float:left; width:24.4%; padding:0 8px 8px 0;">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<tr>
	<td class="head_rd"><u:checkArea>
		<u:radio name="tpl" value="G" title="template_G.html
" inputClass="head_bg" textClass="head_lt" noSpaceTd="true" />
		</u:checkArea></td>
	</tr>

	<tr>
	<td style="padding:7px;height:178px;vertical-align:top;"><table class="listtable" border="0" cellpadding="7" cellspacing="3">
		<tr>
		<td colspan="2" class="body_ct" style="height:50px;"><u:msg titleId="ct.jsp.setMngPage.itroMsg" alt="소개글" /></td>
		</tr>
		<tr>
		<td class="body_ct" width="50%"><u:msg titleId="ct.cols.field" alt="영역" /> 1</td>
		<td class="body_ct" width="50%"><u:msg titleId="ct.cols.field" alt="영역" /> 2</td>
		</tr>

		<tr>
		<td colspan="2" class="body_ct"><u:msg titleId="ct.cols.field" alt="영역" /> 3</td>
		</tr>
		</table></td>
	</tr>
	</table>
	</div>

	<div style="float:left; width:24.4%; padding:0 0 8px 0;">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<tr>
	<td class="head_rd"><u:checkArea>
		<u:radio name="tpl" value="H" title="template_H.html
" inputClass="head_bg" textClass="head_lt" noSpaceTd="true" />
		</u:checkArea></td>
	</tr>

	<tr>
	<td style="padding:7px;height:178px;vertical-align:top;"><table class="listtable" border="0" cellpadding="7" cellspacing="3">
		<tr>
		<td class="body_ct" style="height:50px;"><u:msg titleId="ct.jsp.setMngPage.itroMsg" alt="소개글" /></td>
		</tr>

		<tr>
		<td class="body_ct"><u:msg titleId="ct.cols.field" alt="영역" /> 1</td>
		</tr>

		<tr>
		<td class="body_ct"><u:msg titleId="ct.cols.field" alt="영역" /> 2</td>
		</tr>
		</table></td>
	</tr>
	</table>
	</div>

</div>

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.next" alt="다음" href="javascript:nextSubmit();" auth="W" />
	<u:button titleId="cm.btn.cancel" alt="취소" href="javascript:history.go(-1);" auth="W" />
</u:buttonArea>
</form>
<u:blank />

