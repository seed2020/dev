<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set
	test="${optConfigMap.newLnEach eq 'Y'}" var="openNobr" value="" elseValue="<nobr>" /><u:set
	test="${optConfigMap.newLnEach eq 'Y'}" var="closeNobr" value="" elseValue="</nobr>" /><u:set
	test="${optConfigMap.newLnEach eq 'Y'}" var="newLnBr" value="<br/>" elseValue=" " /><c:if
			
			test="${apFormItemLVo.itemId eq 'makDd'}"><u:out value="${apvData.makDt}" type="date" /></c:if><c:if
			
			test="${apFormItemLVo.itemId eq 'enfcDd'}"><u:out value="${apvData.enfcDt}" type="date" /></c:if><c:if
			
			test="${apFormItemLVo.itemId eq 'attFile'}"><c:forEach
			items="${apOngdAttFileLVoList}" var="apOngdAttFileLVo" varStatus="attStatus"
			><c:if test="${not attStatus.first}">,${newLnBr}</c:if>${openNobr}<a href="javascript:downAttchFile('${downWithFile eq 'Y' ? apOngdAttFileLVo.filePath : apOngdAttFileLVo.attSeq
			}');">${apOngdAttFileLVo.attDispNm}</a>${closeNobr}</c:forEach><c:if
				test="${not empty param.intgNo and not empty apErpIntgFileDVoList}"><c:forEach
			items="${apErpIntgFileDVoList}" var="apErpIntgFileDVo" varStatus="attStatus"
			><c:if test="${not attStatus.first
			}">, </c:if>${openNobr}<a href="javascript:downErpIntgAttchFile('${param.intgNo}','${apErpIntgFileDVo.seq
			}');">${apErpIntgFileDVo.attDispNm}</a>${closeNobr}</c:forEach></c:if></c:if><c:if
			
			test="${apFormItemLVo.itemId eq 'refDocNm'}"><c:forEach
			items="${refApOngdBVoList}" var="refApOngdBVo" varStatus="refStatus"
			><c:if test="${not refStatus.first}">,${newLnBr}</c:if>${openNobr}<c:if
				test="${ (apvData.sendWithRefDocYn ne 'Y' and (
					apvData.recLstTypCd eq 'recvRecLst' or apvData.recLstTypCd eq 'distRecLst' or param.bxId eq 'recvBx' or param.bxId eq 'distBx'))
					or not empty param.refdBy or not empty dmUriBase or winPop eq 'Y'}"
				><u:out value="${refApOngdBVo.docSubj}" /></c:if><c:if
				test="${not ((apvData.sendWithRefDocYn ne 'Y' and (
					apvData.recLstTypCd eq 'recvRecLst' or apvData.recLstTypCd eq 'distRecLst' or param.bxId eq 'recvBx' or param.bxId eq 'distBx'))
					or not empty param.refdBy or not empty dmUriBase or winPop eq 'Y')}"
				><a href="javascript:openDoc${frmYn eq'Y' ? 'Frm' : 'View'}('${refApOngdBVo.apvNo}','${
						not empty refApOngdBVo.docPwEnc
						and refApOngdBVo.makrUid ne sessionScope.userVo.userUid ? 'Y' : ''}');"><u:out value="${refApOngdBVo.docSubj}" /></a></c:if>${closeNobr}</c:forEach></c:if><c:if
			
			test="${apFormItemLVo.itemId eq 'recvDeptNm'}"><c:forEach
			items="${apOngdRecvDeptLVoList}" var="apOngdRecvDeptLVo" varStatus="recvDeptStatus"><c:if
				test="${apOngdRecvDeptLVo.addSendYn eq 'N'}"
				><c:if
				test="${not recvDeptStatus.first}"
				>, </c:if><nobr><u:out value="${apOngdRecvDeptLVo.recvDeptNm}" /><c:if
				test="${not empty apOngdRecvDeptLVo.refDeptNm}"
				>(<u:out value="${apOngdRecvDeptLVo.refDeptNm}" />)</c:if></nobr></c:if></c:forEach></c:if><c:if
			
			test="${apFormItemLVo.itemId eq 'recvDeptRefNm'}"><c:if
				test="${empty orgnSendCnt or orgnSendCnt eq 0}">&nbsp;</c:if><c:if
				test="${not empty apOngdRecvDeptLVoList}"><c:if
					test="${orgnSendCnt eq 1}"><nobr><u:out value="${apOngdRecvDeptLVoList[0].recvDeptNm}" /><c:if
				test="${not empty apOngdRecvDeptLVoList[0].refDeptNm}"
				>(<u:out value="${apOngdRecvDeptLVoList[0].refDeptNm}" />)</c:if></nobr></c:if><c:if
					test="${orgnSendCnt>1}"><u:msg titleId="ap.jsp.recvDept.recvRefDisp" alt="수신처참조" /></c:if></c:if></c:if><c:if
			
			test="${apFormItemLVo.itemId eq 'opin'}">
		<table border="0" cellpadding="0" cellspacing="0"><tbody><c:forEach
			items="${stampApOngdApvLnDVoList}" var="apOngdApvLnDVo" varStatus="opinStatus"><c:if
			
			
				test="${myTurnApOngdApvLnDVo.apvLnNo eq apOngdApvLnDVo.apvLnNo
					and myTurnApOngdApvLnDVo.apvrUid eq apOngdApvLnDVo.apvrUid }"><c:if
	test="${not empty apOngdHoldOpinDVo and apOngdHoldOpinDVo.apvOpinDispYn eq 'Y' and not empty apOngdHoldOpinDVo.apvOpinCont}">
			<tr data-myOpion="Y">
				<td style="min-width:70px;">${apOngdApvLnDVo.apvrNm}</td>
				<td><u:out value="${apOngdHoldOpinDVo.apvOpinCont}" /></td>
			</tr></c:if><c:if
	test="${empty apOngdHoldOpinDVo and myTurnApOngdApvLnDVo.apvOpinDispYn eq 'Y' and not empty myTurnApOngdApvLnDVo.apvOpinCont}">
			<tr data-myOpion="Y">
				<td style="min-width:70px;">${apOngdApvLnDVo.apvrNm}</td>
				<td><u:out value="${myTurnApOngdApvLnDVo.apvOpinCont}" /></td>
			</tr></c:if></c:if><c:if
			
			
				test="${not (myTurnApOngdApvLnDVo.apvLnNo eq apOngdApvLnDVo.apvLnNo
					and myTurnApOngdApvLnDVo.apvrUid eq apOngdApvLnDVo.apvrUid)}"><c:if
					
				test="${apOngdApvLnDVo.apvOpinDispYn eq 'Y'
					and not empty apOngdApvLnDVo.apvOpinCont}"><c:if
			
			test="${apOngdApvLnDVo.apvrDeptYn eq 'Y' and not empty apOngdApvLnDVo.apvDeptNm}">
			<tr><c:if test="${myTurnApOngdApvLnDVo.apvLnNo eq apOngdApvLnDVo.apvLnNo }"></c:if>
				<td style="min-width:70px;">${apOngdApvLnDVo.apvDeptNm}</td>
				<td><u:out value="${apOngdApvLnDVo.apvOpinCont}" /></td>
			</tr></c:if><c:if
			
			test="${not (apOngdApvLnDVo.apvrDeptYn eq 'Y' and not empty apOngdApvLnDVo.apvDeptNm)}">
			<tr>
				<td style="min-width:70px;">${apOngdApvLnDVo.apvrNm}</td>
				<td><u:out value="${apOngdApvLnDVo.apvOpinCont}" /></td>
			</tr></c:if></c:if></c:if></c:forEach>
		</tbody></table></c:if><c:if
		
			test="${apFormItemLVo.itemId eq 'recvNo' and apvData.recLstTypCd eq 'recvRecLst'}"><u:out value="${apvData.recvDocNo}" /></c:if><c:if
			
			test="${apFormItemLVo.itemId eq 'recvDd' and apvData.recLstTypCd eq 'recvRecLst'}"><u:out value="${apvData.recvDt}" type="date" /></c:if><c:if
			
			test="${apFormItemLVo.itemId eq 'recvDt' and apvData.recLstTypCd eq 'recvRecLst'}"><u:out value="${apvData.recvDt}" /></c:if><c:if
			
			
			test="${apFormItemLVo.itemId ne 'makDd' and apFormItemLVo.itemId ne 'enfcDd'
				and apFormItemLVo.itemId ne 'attFile' and apFormItemLVo.itemId ne 'refDocNm'
				and apFormItemLVo.itemId ne 'recvDeptNm' and apFormItemLVo.itemId ne 'recvDeptRefNm'
				and apFormItemLVo.itemId ne 'opin' and apFormItemLVo.itemId ne 'recvNo'
				and apFormItemLVo.itemId ne 'recvDd' and apFormItemLVo.itemId ne 'recvDt'
				and apFormItemLVo.itemId ne ''}"><u:convertMap
			srcId="apvData" attId="${apFormItemLVo.itemId}" nullValue="&nbsp;" type="html" /></c:if>