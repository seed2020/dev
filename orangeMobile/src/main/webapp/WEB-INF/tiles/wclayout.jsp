<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="com.innobiz.orange.web.pt.secu.UserVo,com.innobiz.orange.web.cm.utils.FinderUtil"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"
%><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" 
%><%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%><%
	UserVo uvo = (UserVo)session.getAttribute("userVo");
	if(uvo!=null && FinderUtil.find(uvo.getOdurUid(), false)<0){
		response.sendRedirect("/c"+"m/er"+"ror/li"+"ce"+"nse.do");
		return;
	}
%><!DOCTYPE html>
<html lang="${_lang}">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="format-detection" content="telephone=no" />
<c:if test="${not empty META_TITLE}"><title>${META_TITLE}</title></c:if>
<link rel="stylesheet" href="${_cxPth}/css/animation.css" type="text/css" />
<tiles:insertAttribute name="headinc" />
<script type="text/javascript" src="${_cxPth}/js/orange-layout.js" charset="UTF-8"></script>
<script type="text/javascript">
//<![CDATA[
var $m = parent.$m;
$(document).ready(function() {
	if(window==parent) location.replace('/');
	else {
		$layout.adjust();
		$m.nav.ready(window);
	}
});

function goCtHome(menuId,ctId){
	$m.nav.next(null, "/ct/viewCm.do?menuId="+ menuId +"&ctId=" + ctId );
};
//]]>
</script>
</head>
<body>
<div class="wrapper">
	<!--header S-->
    <header>
        <div class="menu" onclick="$m.menu.open('${_ptMnuLoutDVo.mnuLoutId}', '${menuId}');"></div><c:if
			test="${false}"><!--<div class="logo"></div>--></c:if>
		<u:set var="isPub" test="${fn:startsWith(requestScope['javax.servlet.forward.request_uri'], '/wc/pub')}" value="Y" elseValue="N"/>
        <div class="subtit3" id="pageTitle">
        	<c:if test="${isPub eq 'N'}">
        	<c:choose>
        		<c:when test="${param.tabNo == 1 }"><u:msg titleId="wc.jsp.listPsnSchdl.tab.welySchdl" alt="주간일정"/></c:when>
        		<c:when test="${param.tabNo == 2 }"><u:msg titleId="wc.jsp.listPsnSchdl.tab.dalySchdl" alt="일간일정"/></c:when>
        		<c:otherwise><u:msg titleId="wc.jsp.listPsnSchdl.tab.molySchdl" alt="월간일정"/></c:otherwise>
        	</c:choose>
        	</c:if>
        	<c:if test="${isPub eq 'Y'}">
        		<c:if test="${param.viewTyp eq 'week'}"><u:msg titleId="wc.jsp.listPsnSchdl.tab.welySchdl" alt="주간일정"/></c:if>
        		<c:if test="${param.viewTyp ne 'week'}"><u:msg titleId="wc.jsp.listPsnSchdl.tab.molySchdl" alt="월간일정"/></c:if>
        	</c:if>
        </div>
        <div class="hd_btnarea2">
            <dl>
            	<!-- 권한 -->
				<u:secu auth="W" ><c:set var="writeAuth" value="Y"/></u:secu>
            	<c:if test="${(empty param.ctId && writeAuth == 'Y') || (!empty param.ctId && !empty authChkW && authChkW == 'W')}"><dd class="save" onclick="setSchdl();"></dd></c:if>
                <dd class="view" onclick="$('#tabArea').toggle();" style="float:right;"></dd>
            </dl>
        </div>
        
        <div class="openarea" id="tabArea" style="display:none;">
            <div class="open">
            <dl>
            <dd class="txt" onclick="selectTab(0);"><u:msg titleId="wc.jsp.listPsnSchdl.tab.molySchdl" alt="월간일정"/></dd>
            <dd class="line"></dd>
            <dd class="txt" onclick="selectTab(1);"><u:msg titleId="wc.jsp.listPsnSchdl.tab.welySchdl" alt="주간일정"/></dd>
            <c:if test="${isPub eq 'N'}">
	            <dd class="line"></dd>
	            <dd class="txt" onclick="selectTab(2);"><u:msg titleId="wc.jsp.listPsnSchdl.tab.dalySchdl" alt="일간일정"/></dd>
	            <c:if test="${param.fncCal == 'open' && empty param.ctId}">
	            	<dd class="line"></dd>
	            	<dd class="txt" onclick="schSelectUser();"><u:msg titleId="wc.option.otherPsnSchdl" alt="타인일정"/></dd>
	            	<dd class="line"></dd>
	            	<dd class="txt" onclick="schOneOrg();"><u:msg titleId="wc.option.otherDeptSchdl" alt="타부서일정"/></dd>
	            </c:if>            
	            <c:if test="${param.fncCal == 'my' && empty param.ctId}">
	            	<dd class="line"></dd>
	            	<dd class="txt" onclick="schProxySchdlPop();"><u:msg titleId="wc.jsp.agntSchdl" alt="대리일정"/></dd>
		            <dd class="line"></dd>
		            <dd class="txt" onclick="schSchdlKndCdPop();"><u:msg titleId="wc.cols.schdlKndCd" alt="일정대상"/></dd>
		            <dd class="line"></dd>
		            <dd class="txt" onclick="setUserSetupPop();"><u:msg titleId="cols.tgt" /><u:msg titleId="cm.btn.setup" /></dd>
	            </c:if>
            </c:if>
            <dd class="line"></dd>
            <dd class="txt" onclick="fnCalendar('selectCalYmd');"><input id="selectCalYmd" name="selectCalYmd" type="hidden" /><u:msg titleId="wc.btn.dtMove" alt="날짜이동"/></dd>
            <c:if test="${isPub eq 'N'}">
             <dd class="line"></dd>
            <dd class="txt" onclick="setNatPop(false);"><u:msg titleId="wc.btn.set.nat" alt="국가설정"/></dd>
             <dd class="line"></dd>
            <dd class="txt" onclick="setNatPop(true);"><u:msg titleId="wc.btn.chn.nat" alt="국가변경"/></dd>
            <c:if test="${!empty param.ctId && !empty ctFncDVo}">
            	<dd class="line"></dd>
            	<dd class="txt" onclick="goCtHome('${ctFncDVo.ctFncUid}','${ctFncDVo.ctId}');"><u:msg titleId="ct.cols.cm" alt="커뮤니티" /></dd>
            </c:if>
            </c:if>
         </dl>
            </div> 
        </div>
    </header>
    <!--//header E-->
    <tiles:insertAttribute name="body" />
    <div id="inputSpace" style="height:280px; display:none;"></div>
</div>
</body>
</html>