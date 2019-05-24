<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<% //  ct.jsp.listMngMain.title %>
<u:title title="${menuTitle }" alt="커뮤니티관리" menuNameFirst="true"/>

<div style="float:left;width:49.5%;">
	<div class="titlearea">
		<div class="tit_left">
		<dl>
		<dd class="title_s"><u:msg titleId="ct.jsp.listMngMain.subtitle01" alt="일반사항관리" /></dd>
		</dl>
		</div>
	</div>

	<div class="headbox" style="height:150px;">
	<dl>
	<dd class="headbox_tits"><a href="../setCm.do?menuId=${menuId}&fnc=mod&ctId=${ctId}"><u:msg titleId="ct.jsp.mngMain.basic" alt="기본사항" /></a></dd>
	<dd class="headbox_tits"><a href="../setCmFnc.do?menuId=${menuId}&ctId=${ctId}&fnc=mod&mng=mng"><u:msg titleId="ct.jsp.mngMain.fnc" alt="기능선택" /></a></dd>
	<dd class="headbox_tits"><a href="./setMngAuth.do?menuId=${menuId}&ctId=${ctId}"><u:msg titleId="ct.jsp.mngMain.fncRole" alt="기능별권한관리" /></a></dd>
	<dd class="headbox_tits"><a href="./setMngPage.do?menuId=${menuId}&ctId=${ctId}"><u:msg titleId="ct.jsp.mngMain.initPage" alt="초기페이지구성" /></a></dd>
	<dd class="headbox_tits"><a href="./listMngData.do?menuId=${menuId}&ctId=${ctId}"><u:msg titleId="ct.jsp.mngMain.delData" alt="자료삭제" /></a></dd>
	<dd class="headbox_tits"><a href="./setMngClose.do?menuId=${menuId}&ctId=${ctId}"><u:msg titleId="ct.jsp.mngMain.close" alt="커뮤니티폐쇄" /></a></dd>
	</dl>
	</div>
</div>
<div style="float:right;width:49.5%;">
	<div class="titlearea">
		<div class="tit_left">
		<dl>
		<dd class="title_s"><u:msg titleId="ct.jsp.listMngMain.subtitle02" alt="회원관리" /></dd>
		</dl>
		</div>
	</div>

	<div class="headbox" style="height:150px;">
	<dl>
	<dd class="headbox_tits"><a href="./listMngJoinReqs.do?menuId=${menuId}&ctId=${ctId}"><u:msg titleId="ct.jsp.mngMain.joinMng" alt="가입신청관리" /></a></dd>
	<dd class="headbox_tits"><a href="./listMngMbsh.do?menuId=${menuId}&ctId=${ctId}"><u:msg titleId="ct.jsp.mngMain.joinChg" alt="회원정보변경" /></a></dd>
	<dd class="headbox_tits"><a href="./setMngMast.do?menuId=${menuId}&ctId=${ctId}"><u:msg titleId="ct.jsp.mngMain.mastChg" alt="마스터변경" /></a></dd>
	<dd class="headbox_tits"><a href="./listMngVistrStat.do?menuId=${menuId}&ctId=${ctId}"><u:msg titleId="ct.jsp.mngMain.guestStat" alt="방문자현황" /></a></dd>
	</dl>
	</div>
</div>

<u:blank />

