<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--

//$(document).ready(function() {
//	dialog.open("setDocPwDialog", '<u:msg titleId="ap.titl.docPwCfrm" alt="문서비밀번호 확인" />', "./setDocPwPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${param.apvNo}");
//	alert("${message}");
//});
//-->
</script>
<div style="width:500px">

<u:listArea>
	<tr>
	<td width="32%" class="head_ct"><u:msg titleId="cols.dept" alt="부서" /></td>
	<td width="20%" class="head_ct"><u:term termId="or.term.posit" alt="직위" /></td>
	<td width="20%" class="head_ct"><u:msg titleId="ap.cfg.apvr" alt="결재자" /></td>
	<td width="28%" class="head_ct"><u:msg titleId="ap.list.apvRole" alt="결재자역할" /></td>
	</tr>
	<c:forEach
		items="${apOngdApvLnDVoList}" var="apOngdApvLnDVo" varStatus="status">
	<tr>
	<td class="body_ct"><u:out value="${apOngdApvLnDVo.apvDeptNm}" /></td>
	<td class="body_ct"><u:out value="${apOngdApvLnDVo.apvrPositNm}" /></td>
	<td class="body_ct"><c:if test="${not empty apOngdApvLnDVo.apvrNm}"
		><c:if
			test="${not empty apOngdApvLnDVo.apvrUid}"
			><a href="javascript:viewUserPop('${apOngdApvLnDVo.apvrUid}');"><u:out value="${apOngdApvLnDVo.apvrNm}" /></a></c:if><c:if
			test="${empty apOngdApvLnDVo.apvrUid}"
			><u:out value="${apOngdApvLnDVo.apvrNm}" /></c:if></c:if></td>
	<td class="body_ct"><u:term termId="ap.term.${apOngdApvLnDVo.apvrRoleCd=='byOneAgr' ? 'byOne' : apOngdApvLnDVo.apvrRoleCd}" /></td>
	</c:forEach>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>