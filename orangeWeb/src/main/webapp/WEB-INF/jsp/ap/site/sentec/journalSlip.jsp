<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<div style="width:872px;">
<table border="0" cellpadding="0" cellspacing="0" style="width:100%;border-collapse:collapse; border: 1px solid #bfc8d2;">
	<colgroup><col width="12%"/><col width="38%"/><col width="12%"/><col width="38%"/></colgroup>
	<tr>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">전표단위</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;">${formBodyXML.getValue('row[0]/SlipUnitName')}</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">전표유형</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;">${formBodyXML.getValue('row[0]/SlipKindName')}</td>		
	</tr>
	<tr>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">기표번호</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;">${formBodyXML.getValue('row[0]/SlipMstID')}</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">전표번호</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;">${formBodyXML.getValue('row[0]/SlipAppNo')}</td>
	</tr>
	<tr>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">등록부서</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;">${formBodyXML.getValue('row[0]/RegDeptName')}</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">등록자</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;">${formBodyXML.getValue('row[0]/RegEmpName')}</td>
	</tr>
	<tr>
	<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">기표일자</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;">${formBodyXML.getValue('row[0]/RegDate', 'date')}</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">회계일자</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;">${formBodyXML.getValue('row[0]/OrgAccDate', 'date')}</td>
	</tr>
</table>
<u:blank />
<table border="0" cellpadding="0" cellspacing="0" style="width:100%;border-collapse:collapse; border: 1px solid #bfc8d2;">
	<colgroup><col width="3%"/><col width="*"/><col width="20%"/><col width="20%"/><col width="15%"/><col width="10%"/><col width="10%"/></colgroup>
	<tr>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;" rowspan="2">행번</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;" rowspan="2">계정과목</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;" rowspan="2" colspan="2">적요</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;" rowspan="2">귀속부서</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;" colspan="2">금액</td>		
	</tr>
	<tr>		
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">차변</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">대변</td>		
	</tr>	
	<c:set var="SumDrAmt" value="0"/>
	<c:set var="SumCrAmt" value="0"/>
	<!-- loop start -->
	<c:forEach items="${formBodyXML.getChildList('.')}" var="item" varStatus="status"
	><c:set var="SumDrAmt" value="${SumDrAmt+item.getValue('DrAmt') }"
	/><c:set var="SumCrAmt" value="${SumCrAmt+item.getValue('CrAmt') }"/>	
	<tr>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;" rowspan="4">${item.getValue('RowNum') }</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;line-height: 17px;padding: 2px 3px 0 4px;border-bottom:none;" rowspan="3">${item.getValue('AccName') }</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;line-height: 17px;padding: 2px 3px 0 4px;">${item.getValue('RemValue1') }</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;line-height: 17px;padding: 2px 3px 0 4px;">${item.getValue('RemValue2') }</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;" rowspan="4">${item.getValue('CostDeptName') }</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: right;line-height: 17px;padding: 2px 4px 0 3px;" rowspan="4">${item.getValue('DrAmt', 'number') }</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: right;line-height: 17px;padding: 2px 4px 0 3px;" rowspan="4">${item.getValue('CrAmt', 'number') }</td>
	</tr>
	<tr>		
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;line-height: 17px;padding: 2px 3px 0 4px;">${item.getValue('RemValue3') }</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;line-height: 17px;padding: 2px 3px 0 4px;">${item.getValue('RemValue4') }</td>
	</tr>
	<tr>		
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;line-height: 17px;padding: 2px 3px 0 4px;">${item.getValue('RemValue5') }</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;line-height: 17px;padding: 2px 3px 0 4px;"><c:if test="${!empty item.getValue('ForAmt') }">${item.getValue('ForAmt') }/${item.getValue('CurrName') }/${item.getDecimalValue('ExRate', 2) }</c:if></td>
	</tr>
	<tr>		
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;line-height: 17px;padding: 2px 3px 0 4px;border-top:none;">${item.getValue('EvidName') }</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;line-height: 17px;padding: 2px 3px 0 4px;" colspan="2">${item.getValue('Summary') }</td>		
	</tr>
	</c:forEach>
	<!-- loop end -->
	<tr>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;" colspan="4"></td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;">합계</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;"><u:out value="${SumDrAmt }" type="number"/></td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;"><u:out value="${SumCrAmt }" type="number"/></td>
	</tr>
</table>
</div>