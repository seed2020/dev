<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--
function saveSeculCd(){
	var seculCd = $('#setSeculCdForm #seculCd').val(), result = false;
	callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {process:"changeSeculCdByAdm", apvNos:"${param.apvNo}", seculCd:seculCd}, function(data){
		if(data.message != null) alert(data.message);
		result = data.result == 'ok';
	});
	if(result){
		openDetl();
		dialog.close('setSeculCdDialog');
	}
}
// -->
</script>
<div style="width:400px">
<form id="setSeculCdForm">

<u:listArea colgroup="40%,60%">
	<tr>
	<td class="head_ct"><u:msg alt="보안등급 변경" titleId="ap.btn.setSeculCd" /></td>
	<td class="body_lt"><select id="seculCd" name="seculCd"<u:elemTitle titleId="cols.secul" alt="문서보안" />>
		<option value="none"><u:msg titleId="cm.option.noSelect" alt="선택안함" /></option><c:forEach
		items="${seculCdList}" var="seculCd">
		<u:option value="${seculCd.cd}" title="${seculCd.rescNm}" checkValue="${param.seculCd}" /></c:forEach>
		</select></td>
	</tr>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="saveSeculCd();" alt="저장" auth="A" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>