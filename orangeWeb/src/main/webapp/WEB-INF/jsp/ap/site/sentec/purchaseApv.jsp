<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><% // 양식 : 구매품의서 %>
<u:set var="totalMaxFractionDigits" test="${fn:length(formBodyXML.getChildList('.'))>0 && formBodyXML.getChildList('.')[0].getValue('CurrName') == 'KRW'}" value="0" elseValue="3"/>
<div>
<table border="0" cellpadding="0" cellspacing="0" style="width:100%;border-collapse:collapse; border: 1px solid #bfc8d2;">
	<colgroup><col width="15%"/><col width="35%"/><col width="15%"/><col width="35%"/></colgroup>
	<tr>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">구매품의번호</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;">${formBodyXML.getValue('row[0]/ApproReqNo')}</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">품의부서</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;">${formBodyXML.getValue('row[0]/DeptName')}</td>
	</tr>
	<tr>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">구매품의일</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;">${formBodyXML.getValue('row[0]/ApproReqDate', 'date')}</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">품의담당자</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;">${formBodyXML.getValue('row[0]/EmpName')}</td>
	</tr>
	<tr>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">총금액</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;line-height: 17px;padding: 2px 3px 0 4px;" colspan="3">${formBodyXML.getDecimalValue('row[0]/totDomAmt', totalMaxFractionDigits)}</td>
	</tr>	
	<tr>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">비고</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;line-height: 17px;padding: 2px 3px 0 4px;" colspan="3">${formBodyXML.getValue('row[0]/Remark')}</td>		
	</tr>	
</table>
<u:blank />
<table border="0" cellpadding="0" cellspacing="0" style="width:100%;border-collapse:collapse; border: 1px solid #bfc8d2;">
	<colgroup><col width="3%"/><col width="7%"/><col width="7%"/><col width="13%"/><col width="5%"/><col width="5%"/><col width="5%"/><col width="6%"/><col width="7%"/><col width="9%"/><col width="9%"/><col width="8%"/><col width="7%"/><col width="*"/></colgroup>	
	<tr>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">No</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">품번</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">품명</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">규격</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">단위</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">통화</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">수량</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">단가</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">금액</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">납기일</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">거래처</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">요구부서</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">요구자</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">비고</td>	
	</tr>
	<!-- loop start -->
	<c:forEach items="${formBodyXML.getChildList('.')}" var="item" varStatus="status">
	<u:set var="maxFractionDigits" test="${!empty item.getValue('CurrName') && item.getValue('CurrName') == 'KRW'}" value="0" elseValue="2"/>
	<tr>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;">${item.getValue('RowNO') }</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;line-height: 17px;padding: 2px 3px 0 4px;">${item.getValue('ItemNo') }</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;line-height: 17px;padding: 2px 3px 0 4px;">${item.getValue('ItemName') }</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;line-height: 17px;padding: 2px 3px 0 4px;">${item.getValue('Spec') }</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;">${item.getValue('UnitName') }</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;">${item.getValue('CurrName') }</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;">${item.getDecimalValue('Qty', 0)}</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: right;line-height: 17px;padding: 2px 4px 0 3px;">${item.getDecimalValue('Price', maxFractionDigits)}</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: right;line-height: 17px;padding: 2px 4px 0 3px;">${item.getDecimalValue('DomAmt', maxFractionDigits)}</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;">${item.getValue('DelvDate', 'date') }</td>		
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;line-height: 17px;padding: 2px 3px 0 4px;">${item.getValue('CustName') }</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;line-height: 17px;padding: 2px 3px 0 4px;">${item.getValue('ReqDeptName') }</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;">${item.getValue('ReqEmpName') }</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;line-height: 17px;padding: 2px 3px 0 4px;">${item.getValue('RemarkItem') }</td>	
	</tr>
	</c:forEach>
	<!-- loop end -->
</table>
</div>