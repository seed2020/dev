<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<div>
<u:title titleId="wb.jsp.listBc.quicAdd" alt="빠른추가" notPrint="true" menuNameFirst="false"/>
<form id="setRegForm" name="setRegForm" style="padding:10px;">
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="typ" value="${param.typ}" />
	<u:input type="hidden" id="schInitial" value="${param.schInitial}" />
	<u:input type="hidden" id="dftCntcTypCd" name="dftCntcTypCd" value="compPhon"/>
	<u:input type="hidden" id="listPage" value="./${listPage}.do?${nonPageParams}${agntParam}" />
	<u:input type="hidden" id="viewPage" value="./${viewPage}.do?${params}" />
	<c:if test="${!empty schBcRegrUid}"><u:input type="hidden" id="schBcRegrUid" name="schBcRegrUid" value="${schBcRegrUid }"/></c:if>
	
	<div class="listarea notPrint">
		<table class="listtable" border="0" cellpadding="0" cellspacing="1">
			<tr>
				<td width="25%" class="head_ct"><u:mandatory /><u:msg titleId="cols.nm" alt="이름" /></td>
				<td width="25%" class="head_ct"><u:msg titleId="cols.email" alt="E-mail" /></td>
				<td width="20%" class="head_ct"><u:msg titleId="wb.cols.phonTyp" alt="전화구분" /></td>
				<td class="head_ct"><u:msg titleId="cols.phon" alt="전화번호" /></td>
			</tr>
	
			<tr>
				<td class="input_ct"><u:input id="nm" name="bcNm" value="" titleId="cols.nm" maxByte="30" mandatory="Y"/></td>
				<td class="input_ct">
					<u:input type="hidden" id="cntcTypCd" name="cntcTypCd" value="email" />
					<u:input type="hidden" id="cntcClsCd" name="cntcClsCd" value="EMAIL" />
					<u:input id="email" name="cntcCont" value="" titleId="cols.email" valueOption="email" maxByte="200" validator="checkMail(inputTitle, va)" style="width:200px;"/>
				</td>
				<td class="input_ct">
					<select name="cntcTypCd" onchange="fnCntcTypCd(this);">
						<u:option value="compPhon" titleId="cols.comp" alt="회사" />
						<u:option value="homePhon" titleId="cols.home" alt="자택" />
						<u:option value="mbno" titleId="cols.mob" alt="휴대" />
					</select>
				</td>
				<td>
					<table class="input_ct" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td>
								<u:input type="hidden" id="cntcClsCd" name="cntcClsCd" value="CNTC" />
								<u:input id="cntcCont" name="cntcCont" value="" titleId="cols.phon"     /><%-- maxLength="12" minLength="10" mandatory="Y" valueOption="number" validator="checkPhone(inputTitle, va)" onblur="fnPhoneInput(this);" onfocus="fnPhoneUnInput(this);" --%>
							</td>
							<td><u:buttonS href="javascript:saveQuic();" titleId="wb.btn.quicAdd" alt="빠른추가" auth="W"/></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
</form>
</div>
<u:blank />