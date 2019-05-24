<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
//우선연락처 세팅
function fnCntcTypCd( obj ){
	$("#setRegForm input[id='dftCntcTypCd']").val(obj.value);
};

<%// 빠른 추가%>
function saveQuic(){
	if (validator.validate('setRegForm') && true/*confirmMsg("cm.cfrm.save")*/ ) {
		var $form = $('#setRegForm');
		$form.attr('method','post');
		$form.attr('action','./transBc.do?menuId=${menuId}');
		//$form.attr('target','dataframe');
		$form[0].submit();
	}
};

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<form id="setRegForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="dftCntcTypCd" name="dftCntcTypCd" value="compPhon"/>
<div class="ptlbody_ct">
	<table class="ptltable" border="0" cellpadding="0" cellspacing="0" >
		<colgroup>
			<col width="30%"/>
			<col width="70%"/>
		</colgroup>
		<tr>
			<td colspan="2" class="line"></td>
		</tr>
		<tr>
			<td class="head_lt"><u:mandatory /><u:msg titleId="cols.nm" alt="이름" /></td>
			<td style="text-align:left;"><u:input id="nm" name="bcNm" value="" titleId="cols.nm" maxByte="30" mandatory="Y" style="width:90px;"/></td>
		</tr>
		<tr>
			<td colspan="2" class="line"></td>
		</tr>
		<tr>
			<td class="head_lt"><u:msg titleId="cols.email" alt="E-mail" /></td>
			<td style="text-align:left;">
				<u:input type="hidden" id="cntcTypCd" name="cntcTypCd" value="email" />
				<u:input type="hidden" id="cntcClsCd" name="cntcClsCd" value="EMAIL" />
				<u:input id="email" name="cntcCont" value="" titleId="cols.email" valueOption="email" maxByte="200" validator="checkMail(inputTitle, va)" style="width:110px;"/>
			</td>
		</tr>
		<tr>
			<td colspan="2" class="line"></td>
		</tr>
		<tr>
			<td class="head_lt"><u:msg titleId="cols.phon" alt="전화번호" /></td>
			<td style="text-align:left;">
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>
							<u:input type="hidden" id="cntcClsCd" name="cntcClsCd" value="CNTC" />
							<u:input id="cntcCont" name="cntcCont" value="" titleId="cols.phon"    style="width:90px;"/><!-- maxLength="12" minLength="10" mandatory="Y" valueOption="number" validator="checkPhone(inputTitle, va)" onblur="fnPhoneInput(this);" onfocus="fnPhoneUnInput(this);" -->
						</td>
						<td>
							<select name="cntcTypCd" onchange="fnCntcTypCd(this);">
								<u:option value="compPhon" titleId="cols.comp" alt="회사" />
								<u:option value="homePhon" titleId="cols.home" alt="자택" />
								<u:option value="mbno" titleId="cols.mob" alt="휴대" />
							</select>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan="2" class="line"></td>
		</tr>
	</table>

	<div class="blank_s"> </div>

	<div class="front">
		<div class="front_right">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="frontbtn"><u:buttonS href="javascript:saveQuic();" titleId="wb.btn.quicAdd" alt="빠른추가" auth="W"/></td>
				</tr>
			</table>
		</div>
	</div>
</div>

</form>