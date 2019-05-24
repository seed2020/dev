<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function saveStatCd(){
	var $form = $('#statCdForm');
	$form.attr("action","./transStatCd.do");
	$form.attr("target","dataframe");
	$form.submit();
}
//-->
</script>
<div style="width:400px">
<form id="statCdForm" method="post">
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="userUids" value="${param.userUids}" />
<u:listArea>

	<tr>
		<td width="34%" class="head_lt"><u:msg titleId="or.cols.statCd" alt="상태코드" /></td>
		<td>
			<select id="userStatCd" name="userStatCd" <u:elemTitle termId="or.cols.statCd" alt="상태코드" />><c:forEach
				items="${userStatCdList}" var="userStatCdVo" varStatus="statStatus">
				<u:option value="${userStatCdVo.cd}" title="${userStatCdVo.rescNm}" checkValue="${orUserBVo.userStatCd}"
				/></c:forEach>
			</select>
		</td>
	</tr>

</u:listArea>
</form>

<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="saveStatCd();" auth="A" alt="저장" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</div>