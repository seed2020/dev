<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%// 메뉴 사용 - 설정 %>
function setMenu(){
	dialog.open('seMyMnuPopDialog','<u:msg titleId="pt.jsp.myMnu.title" alt="나의 메뉴 설정" />','./setMyMnuPop.do?menuId=${menuId}');
}
<%// 포틀릿 사용 - 설정 %>
function setPlt(){
	var $form = $("#myMnuForm");
	$form.attr("target", "").attr("action", "./setPltStep1.do").submit();
}
<%// 저장 버튼 클릭 %>
function saveMyMnuSetup(){
	var $form = $("#myMnuForm");
	$form.attr("target", "dataframe").attr("action", "./transMyMnu.do").submit();
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>

<u:title alt="개인화" menuNameFirst="true" />

<form id="myMnuForm">
<input type="hidden" name="menuId" value="${menuId}" />
<u:listArea>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.jsp.myMnu.useMnu" alt="메뉴 사용" /></td>
		<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0"><tr>
			<td><u:checkbox name="useMnu" value="Y" checked="${myMnuMap.useMnu == 'Y'}" /></td>
			<td><u:buttonS id="btnUseMnu" onclick="setMenu()" titleId="cm.btn.setup" alt="설정" /></td>
			</tr></table>
		</td>
	</tr>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.jsp.myMnu.usePlt" alt="포틀릿 사용" /></td>
		<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0"><tr>
			<td><u:checkbox name="usePlt" value="Y" checked="${myMnuMap.usePlt == 'Y'}" /></td>
			<td><u:buttonS id="btnUsePlt" onclick="setPlt()" titleId="cm.btn.setup" alt="설정" /></td>
			</tr></table>
		</td>
	</tr>
</u:listArea>

</form>

<u:buttonArea>
	<u:button href="javascript:saveMyMnuSetup();" titleId="cm.btn.save" alt="저장" auth="W" />
</u:buttonArea>