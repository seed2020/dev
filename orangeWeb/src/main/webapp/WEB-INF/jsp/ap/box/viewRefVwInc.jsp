<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

/*
	
*/

%>
<u:listArea id="refVwListArea" noBottomBlank="${not empty refVwListInDoc}"
	colgroup="${not empty refVwListInDoc ? '20%,20%,20%,20%,20%' : '20%,25%,25%,30%'}" >
	<tr id="refVwListHeader">
	<td class="head_ct"><u:msg titleId="cols.dept" alt="부서" /></td>
	<td class="head_ct"><u:term termId="or.term.posit" alt="직위" /></td>
	<td class="head_ct"><u:term termId="ap.term.refVwr" alt="열람자" /></td>
	<td class="head_ct"><u:term termId="ap.term.refVwDt" alt="열람일시" /></td><c:if
		test="${not empty refVwListInDoc}">
	<td class="head_ct"><u:msg titleId="ap.doc.opin" alt="의견" /></td></c:if>
	</tr>
	<c:forEach
		items="${apOngdRefVwDVoList}"
		var="apOngdRefVwDVo" varStatus="status">
	<tr <c:if test="${empty refVwListInDoc}">onmouseover='this.className="trover"' onmouseout='this.className="trout"'</c:if>>
	<td class="body_ct"><u:out value="${apOngdRefVwDVo.refVwrDeptNm}" /></td>
	<td class="body_ct"><u:out value="${apOngdRefVwDVo.refVwrPositNm}" /></td>
	<td class="body_ct"><c:if
		test="${empty apOngdRefVwDVo.refVwrNm}">&nbsp;</c:if><c:if
		test="${not empty apOngdRefVwDVo.refVwrNm}"
		><c:if
			test="${not empty apOngdRefVwDVo.refVwrUid}"
			><a href="javascript:${frmYn=='Y'? 'parent.' : ''}viewUserPop('${apOngdRefVwDVo.refVwrUid}');"><u:out value="${apOngdRefVwDVo.refVwrNm}" /></a></c:if><c:if
			test="${empty apOngdRefVwDVo.refVwrUid}"
			><u:out value="${apOngdRefVwDVo.refVwrNm}" /></c:if></c:if></td>
	<td class="body_ct"><u:out value="${apOngdRefVwDVo.refVwDt}" /></td><c:if
		test="${not empty refVwListInDoc}">
	<td style="text-align:center;"><c:if
			test="${not empty apOngdRefVwDVo.refVwOpinCont and not (
					apvData.recLstTypCd eq 'recvRecLst' or apvData.recLstTypCd eq 'distRecLst'
					or param.bxId eq 'recvBx' or param.bxId eq 'distBx')
				}"><a href="javascript:openDetl('refVw')"><img alt="ballen" src="/images/cm/ballon.gif" /></a></c:if></td></c:if>
	</tr>
	</c:forEach>
</u:listArea>

<c:if test="${empty refVwListInDoc}">
<u:listArea noBottomBlank="true">
	<tr>
	<td width="20%" class="head_ct"><u:term termId="ap.term.refVwr" alt="열람자" /></td>
	<td width="80%" class="head_ct"><u:msg titleId="ap.doc.opin" alt="의견" /></td>
	</tr>
	<c:forEach
		items="${apOngdRefVwDVoList}"
			var="apOngdRefVwDVo" varStatus="status"><c:if
			test="${not empty apOngdRefVwDVo.refVwOpinCont}" ><u:set test="true" var="hasOpin" value="Y" />
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_ct"><c:if
			test="${not empty apOngdRefVwDVo.refVwrUid}"
			><a href="javascript:viewUserPop('${apOngdRefVwDVo.refVwrUid}');"><u:out value="${apOngdRefVwDVo.refVwrNm}" /></a></c:if><c:if
			test="${empty apOngdRefVwDVo.refVwrUid}"
			><u:out value="${apOngdRefVwDVo.refVwrNm}" /></c:if></td>
	<td class="body_lt"><u:out value="${apOngdRefVwDVo.refVwOpinCont}" /></td>
	</tr></c:if>
	</c:forEach>
	<c:if test="${empty hasOpin}">
	<tr>
	<td class="nodata" colspan="2"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
	</c:if>
	
</u:listArea>
</c:if>