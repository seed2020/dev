<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<div>
<table border="0" cellpadding="0" cellspacing="0" style="width:100%;border-collapse:collapse; border: 1px solid #bfc8d2;">
	<colgroup><col width="15%"/><col width="35%"/><col width="15%"/><col width="35%"/></colgroup>
	<tr>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">구매요청번호</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;">${formBodyXML.getValue('row[0]/POReqNo')}</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">요청부서</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;">${formBodyXML.getValue('row[0]/DeptName')}</td>
	</tr>
	<tr>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">구매요청일</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;">${formBodyXML.getValue('row[0]/ReqDate', 'date')}</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">요청담당자</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;">${formBodyXML.getValue('row[0]/EmpName')}</td>
	</tr>
	<tr>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">비고</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;line-height: 17px;padding: 2px 3px 0 4px;" colspan="3">${formBodyXML.getValue('row[0]/Remark')}</td>		
	</tr>	
</table>
<u:blank />
<table border="0" cellpadding="0" cellspacing="0" style="width:100%;border-collapse:collapse; border: 1px solid #bfc8d2;">
	<colgroup><col width="3%"/><col width="12%"/><col width="*"/><col width="17%"/><col width="7%"/><col width="7%"/><col width="13%"/><col width="15%"/></colgroup>
	<tr>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">No</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">품번</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">품명</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">규격</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">단위</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">수량</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">납기요청일</td>
		<td style="border:1px solid #bfc8d2;height: 22px;text-align: center;background: #E5E4E2;font-weight:bold;font-size:14px;line-height: 17px;padding: 2px 2px 0 2px;">비고</td>	
	</tr>
	<!-- loop start -->
	<c:forEach items="${formBodyXML.getChildList('.')}" var="item" varStatus="status">
	<tr>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;">${status.count }</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;line-height: 17px;padding: 2px 3px 0 4px;">${item.getValue('ItemNo') }</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;line-height: 17px;padding: 2px 3px 0 4px;">${item.getValue('ItemName') }</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;line-height: 17px;padding: 2px 3px 0 4px;">${item.getValue('Spec') }</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;">${item.getValue('UnitName') }</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;">${item.getValue('Qty', 'number') }</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;text-align: center;line-height: 17px;padding: 2px 3px 0 3px;">${item.getValue('DelvDate', 'date') }</td>
		<td style="border:1px solid #bfc8d2;height: 22px;color: #454545;line-height: 17px;padding: 2px 3px 0 4px;">${item.getValue('RemarkItem') }</td>	
	</tr>
	</c:forEach>
	<!-- loop end -->
</table>
</div>