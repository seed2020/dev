<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
	import="java.net.URLEncoder"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
%>
<!-- 링크 css 개별 추가 -->
<style type="text/css">
span.viewSkinImg{cursor:pointer;}
span.viewSkinImg:link    { color:#454545; text-decoration:none; }
span.viewSkinImg:visited { color:#454545; text-decoration:none; }
span.viewSkinImg:hover   { color:#454545; text-decoration:underline; }
span.viewSkinImg:active  { color:#454545; text-decoration:underline; }
</style>
<script type="text/javascript">
<!--
function doSubmit(){
	if(validator.validate('setMobileLoginForm')){
		var $form = $("#setMobileLoginForm");
		$form.attr('method','post');
		$form.attr('action','./transLoginPage.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form.submit();
	}
}
function setUseMsgLgin(){
	dialog.open('setUseMsgLginDialog','<u:msg titleId="or.jsp.setUserPop.setUseMsgLgin" alt="메세지 자동 로그인 설정" />','./setMsgLginPop.do?menuId=${menuId}');
}
<%// 이미지 보기%>
function viewSkinImgPop(setupId){
	dialog.open("viewSkinImgDialog", '<u:msg titleId="cols.cmImg" alt="이미지" />', "./viewSkinImgPop.do?menuId=${menuId}&width=280&height=153&setupId="+setupId);
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
	// 클릭이벤트 bind
	$('#setMobileLoginForm').find('span.viewSkinImg').on('click', function(){
		viewSkinImgPop('logoImgPath');
	});
});
-->
</script>

<u:title menuNameFirst="true" />

<form id="setMobileLoginForm" method="post" enctype="multipart/form-data">
<input type="hidden" name="menuId" value="${menuId}" />
<u:listArea>

	<tr>
	<td width="18%" class="head_lt"><span class="viewSkinImg"><u:msg titleId="cols.logoImgFile" alt="로고 이미지" /></span></td>
	<td><u:file id="logoImgFile" exts="jpg,gif,png" recomend="280 X 153" /></td>
	</tr>
	
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="or.txt.siteName" alt="사이트명" /></td>
		<td><u:term termId="or.term.siteName" alt="사이트명" var="siteName" /><u:input id="lginTitle" titleId="or.txt.siteName"
			value="${empty mobLginMap.lginTitle ? siteName : mobLginMap.lginTitle
			}" mandatory="Y" maxLength="20" style="width:50%" /></td>
	</tr>
	
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="or.txt.siteNameColor" alt="사이트명 색상" /></td>
		<td><u:color id="lginTitleColor" value="${mobLginMap.lginTitleColor}" titleId="or.txt.siteNameColor"  /></td>
	</tr>
	
</u:listArea>
</form>

<u:buttonArea><c:if
		test="${sessionScope.userVo.userUid eq 'U0000001'}"><u:msg
			titleId="pt.jsp.blockForeignPloc" alt="해외 IP 차단 정책" var="blockForeignPloc" />
	<u:button title="${blockForeignPloc}" alt="해외 IP 차단 정책" onclick="dialog.open('setForeignIpBlockingPlocDialog','${blockForeignPloc}','./setForeignIpBlockingPlocPop.do?menuId=${menuId}');" /></c:if><c:if
		test="${false}">
	<u:button titleId="or.jsp.setUserPop.setUseMsgLgin" alt="메세지 자동 로그인 설정" onclick="setUseMsgLgin()" auth="SYS" /></c:if>
	<u:button titleId="cm.btn.save" alt="저장" onclick="doSubmit()" auth="SYS" />
</u:buttonArea>