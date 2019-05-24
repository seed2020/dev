<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
//-->
</script>
<div style="width:550px">

<u:listArea style="max-height:300px; overflow-y:auto;">
	<tr>
	<td width="30%" class="head_ct"><u:msg titleId="cols.dept" alt="부서" /></td>
	<td width="20%" class="head_ct"><u:term termId="or.term.posit" alt="직위" /></td>
	<td width="20%" class="head_ct"><u:msg titleId="or.cols.name" alt="성명" /></td>
	<td width="30%" class="head_ct"><u:msg titleId="ap.jsp.pubBxVwDt" alt="공람일시" /></td>
	</tr>

<c:if test="${fn:length(apOngdPubBxCnfmLVoList)==0}" >
	<tr>
	<td class="nodata" colspan="4"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:forEach items="${apOngdPubBxCnfmLVoList}" var="apOngdPubBxCnfmLVo" varStatus="status">
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
	<td class="body_ct"><u:out value="${apOngdPubBxCnfmLVo.deptNm}" /></td>
	<td class="body_ct"><u:out value="${apOngdPubBxCnfmLVo.positNm}" /></td>
	<td class="body_ct"><c:if
			test="${not empty apOngdPubBxCnfmLVo.userUid}"
			><a href="javascript:viewUserPop('${apOngdPubBxCnfmLVo.userUid}');"><u:out value="${apOngdPubBxCnfmLVo.userNm}" /></a></c:if><c:if
			test="${empty apOngdPubBxCnfmLVo.userUid}"
			><u:out value="${apOngdPubBxCnfmLVo.userNm}" /></c:if></td>
	<td class="body_ct"><u:out value="${apOngdPubBxCnfmLVo.vwDt}" /></td>
	</tr>
</c:forEach>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</div>