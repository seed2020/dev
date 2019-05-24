<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
	import="com.innobiz.orange.web.cm.utils.StringUtil"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	String schdlParam = "&schdlStartDt="+StringUtil.getCurrYmd()+"&schdlEndDt="+StringUtil.getCurrYmd();
	request.setAttribute("schdlParam",schdlParam);
	
	// 건수 새로고침 : 자동
	String autoRefresh = com.innobiz.orange.web.pt.utils.SysSetupUtil.getSysPloc("autoCntRefreshEnable");
	if("Y".equals(autoRefresh)){
		if(com.innobiz.orange.web.pt.utils.SysSetupUtil.isFrequentRefreshUser((com.innobiz.orange.web.pt.secu.UserVo)session.getAttribute("userVo"))){
			request.setAttribute("autoRefreshSec", Integer.valueOf(1  * 60));
		} else {
			String min = com.innobiz.orange.web.pt.utils.SysSetupUtil.getSysPloc("refreshMin");
			Integer autoRefreshSec = min==null ? Integer.valueOf(20 * 60) : Integer.valueOf(Integer.parseInt(min) * 60);
			request.setAttribute("autoRefreshSec", autoRefreshSec);
		}
		String extMailSsn = com.innobiz.orange.web.pt.utils.SysSetupUtil.getSysPloc("extMailSsn");
		if("Y".equals(extMailSsn)){
			String protocal = "Y".equals(com.innobiz.orange.web.pt.utils.SysSetupUtil.getSysPloc("useSSL")) ? "https://" : "http://";
			String mailDomain = com.innobiz.orange.web.pt.utils.SysSetupUtil.getSvrEnv("mailCall");
			if(mailDomain!=null && !mailDomain.isEmpty()){
				request.setAttribute("extMailSsnDomain", protocal+mailDomain);
			}
		}
	}
	
	// PC 알림
	String pcNoti = com.innobiz.orange.web.pt.utils.SysSetupUtil.getSysPloc("pcNotiEnable");
	if("Y".equals(pcNoti)){
		/*
		if(com.innobiz.orange.web.pt.utils.SysSetupUtil.isFrequentRefreshUser((com.innobiz.orange.web.pt.secu.UserVo)session.getAttribute("userVo"))){
			request.setAttribute("pcNotiSec", Integer.valueOf(1  * 60));
		} else {
			String min = com.innobiz.orange.web.pt.utils.SysSetupUtil.getSysPloc("pcNotiMin");
			Integer pcNotiSec = min==null ? Integer.valueOf(20 * 60) : min.endsWith("s") ? Integer.valueOf(min.substring(0, min.length()-1)) : Integer.valueOf(Integer.parseInt(min) * 60);
			request.setAttribute("pcNotiSec", pcNotiSec);
		}
		*/
		com.innobiz.orange.web.pt.secu.UserVo userVo = (com.innobiz.orange.web.pt.secu.UserVo)session.getAttribute("userVo");
		if(userVo!=null && !com.innobiz.orange.web.cm.config.ServerConfig.IS_LOC){
			request.setAttribute("hashUid", com.innobiz.orange.web.pt.secu.CRC32.hash(userVo.getUserUid().getBytes()));
			request.setAttribute("hashOid", com.innobiz.orange.web.pt.secu.CRC32.hash(userVo.getOdurUid().getBytes()));
			String pushPort = com.innobiz.orange.web.pt.utils.SysSetupUtil.getSvrEnv("pushPort");
			if(pushPort == null || pushPort.isEmpty()) pushPort = "7080";
			if(request.isSecure()){
				pushPort = pushPort.substring(0, 1) + "443";
			}
			request.setAttribute("pushPort", pushPort);
		}
	}
%>
<script type="text/javascript">
<!--
var gLoutType = "I";
var gSkin = "${_skin}";
$(document).ready(function() {
	$gMenuTop.$topArea = $("#header_${_skin}");
	$gMenuTop.$topArea.find("#topIconMnuArea dd.topmu").mouseenter(enterMnuMouse).mouseleave(leaveMnuMouse);
	$gMenuTop.$topArea.find("#topRightMnuArea, #topLeftMnuArea").find("dd.rt_department_arrow, dd.rt_admin").mouseenter(enterMnuMouse).mouseleave(leaveMnuMouse);
	var w, max=0, $items, $topPops = $gMenuTop.$topArea.find("#topPopMnuArea").children();
	$topPops.mouseenter(enterSubPopMouse).mouseleave(leaveSubPopMouse).each(function(){
		$items = $(this).find("div ul li a").each(function(){
			$(this).css("padding-left","0px").css("padding-right","0px").css("margin-left","0px").css("margin-right","0px");
		});
		max = 0;
		$items.each(function(){
			w = $(this).width();
			if(w>max) max = w;
		});
		max = max+5;
		max = (max<80) ? "80px" : max+"px";
		$items.css("width",max);
		$(this).css("width",max);
		$(this).hide();
		if(browser.ie && browser.ver<9) $(this).css("filter", "alpha(opacity=100)");
		else $(this).css("opacity", "1");
	});<%
	// 새메일, 결재건수, 일정건수 - AJAX 조회 %>
	mainRefreshCnt({needApBxCnt:${optConfigMap.docCntInBx=='Y'}});<%
if(request.getAttribute("autoRefreshSec")!=null){
	if(request.getAttribute("extMailSsnDomain")!=null){%>
	countRefreshUtil.mailDomain = "${extMailSsnDomain}";<%
	}%>
	countRefreshUtil.setRefresh(${optConfigMap.docCntInBx=='Y'}, ${autoRefreshSec});<%
}
if(request.getAttribute("pcNotiSec")!=null){%>
	pcNotiUtil.setRefresh(${pcNotiSec});<%
}
if(request.getAttribute("hashUid")!=null){%>
	webPushUtil.startWebPush('${hashUid}','${hashOid}','${pushPort}');<%
}
	%>
});
//-->
</script>
<!--left S-->
<div class="left" style="z-index:5;">
<dl id="topLeftMnuArea"><%// 새메일-건수,결재-건수,일정-건수,[추가메뉴] %>
	<dd class="logo"><a href="javascript:goHome();"><img src="${_logoImg}" width="193" height="63" alt="home" /></a></dd><u:set
			test="${sessionScope.userVo.hasMnuGrpMdRidOf('MAIL') and not empty MAIL_ON}"
			var="SHOW_MAIL" value="Y" />
	<dd class="logotxt" style="padding:2px 0 1px 0;${
		SHOW_MAIL eq 'Y'
		or sessionScope.userVo.hasMnuGrpMdRidOf('AP')
		or sessionScope.userVo.hasMnuGrpMdRidOf('WC') ? '' : 'display:none;'}"><a href="javascript:;" onclick="mainRefreshCnt({needApBxCnt:false})"><img src="/images/${_skin}/icoptl_reload_top.png" width="19" height="21" title="<u:msg titleId="em.btn.refresh" alt="새로고침"/>"/></a></dd>
	<c:if
		test="${SHOW_MAIL eq 'Y'}">
	<dd class="logoblank"></dd>
	<dd class="logotxt"><a href="/cm/zmailPop.do?cmd=mail_link_2"><u:msg titleId="pt.top.newMail" alt="메일"
		/> <strong id="topMailCnt">&nbsp;</strong> <u:msg titleId="cm.count" alt="건"
		/></a></dd>
	<dd class="logoblank topApvMailCntCls" style="display:none;"></dd>
	<dd class="logotxt topApvMailCntCls" style="display:none;"><a href="/cm/zmailPop.do?cmd=approve_receive_mailbox"><u:msg titleId="pt.top.apvMail" alt="승인메일"
		/> <strong id="topApvMailCnt">&nbsp;</strong> <u:msg titleId="cm.count" alt="건"
		/></a></dd></c:if>
	<c:if test="${sessionScope.userVo.hasMnuGrpMdRidOf('AP')}">
	<dd class="logoblank"></dd>
	<dd class="logotxt"><a href="<u:authUrl url="/ap/box/listApvBx.do?bxId=waitBx" />"><u:msg titleId="pt.top.newApp" alt="결재"
		/> <strong id="topAppCnt">&nbsp;</strong> <u:msg titleId="cm.count" alt="건"
		/></a></dd></c:if>
	<c:if test="${sessionScope.userVo.hasMnuGrpMdRidOf('WC')}">
	<dd class="logoblank"></dd>
	<dd class="logotxt"><a href="<u:authUrl  url="/wc/listNewSchdl.do?fncCal=my" />&tabNo=2${schdlParam}"><u:msg titleId="pt.top.newSchd" alt="일정"
		/> <strong id="topSchdlCnt">&nbsp;</strong> <u:msg titleId="cm.count" alt="건"
		/></a></dd></c:if><c:if
		
			test="${sessionScope.userVo.hasMnuGrpMdRidOf('AP') and 
				not empty sessionScope.userVo.adurs and sessionScope.userVo.userUid!='U0000001' and optConfigMap.adurMergLst != 'Y'}">
	<dd class="logoblank topAdditionalCntCls"></dd><u:cmt
			
		cmt="commonEx.js 에서  'topAdditionalCnt'를 참조해서 '/changeWhenOnload.do'를 겸직 대기함 링크로 변경함" />
	<dd class="logotxt topAdditionalCntCls"><a href="/changeWhenOnload.do"><u:msg titleId="pt.top.newAdditional" alt="겸직"
		/> <strong id="topAdditionalCnt">&nbsp;</strong> <u:msg titleId="cm.count" alt="건"
		/></a></dd></c:if>
	<c:forEach
		items="${_leftList}" var="ptMnuLoutDVo" varStatus="leftStatus">
	<dd class="logoblank"></dd>
	<dd class="logotxt"><a href="<u:menuUrl url="${ptMnuLoutDVo.mnuUrl}" />"<c:if
			test="${not empty ptMnuLoutDVo.mnuFnc}"> onclick="<u:menuUrl url="${ptMnuLoutDVo.mnuFnc}" /> return false;"</c:if
			><c:if
			test="${empty ptMnuLoutDVo.mnuFnc and not _byAdmin}"> onclick="goLout(event, '${ptMnuLoutDVo.mnuLoutId}'); return false;"</c:if
			> title="${ptMnuLoutDVo.rescNm}">${ptMnuLoutDVo.rescNm}</a></dd></c:forEach
		>
</dl>
</div>
<!--//left E-->

<!--center S-->
<div class="center" style="z-index:5;">
	<div class="topmuarea"><%// 아이콘 영역 %>
	<dl id="topIconMnuArea"><c:forEach
		items="${_iconList}" var="ptMnuLoutDVo" varStatus="outerStatus"
		><c:if test="${not outerStatus.first}">
		<dd class="topmu_blank"></dd></c:if
		><u:set test="${ptMnuLoutDVo.mnuLoutId == _ptMnuLoutDVo.mnuLoutId or ptMnuLoutDVo.mnuLoutId == _ptMnuLoutDVo.topMnuLoutId}"
			var="on" value="on" elseValue="" />
		<dd id="Icon${ptMnuLoutDVo.mnuLoutId}" class="topmu" title="${ptMnuLoutDVo.rescNm}"><a href="<u:menuUrl url="${ptMnuLoutDVo.mnuUrl}" />"<c:if
			test="${not empty ptMnuLoutDVo.mnuFnc}"> onclick="<u:menuUrl url="${ptMnuLoutDVo.mnuFnc}" /> return false;"</c:if
			><c:if
			test="${empty ptMnuLoutDVo.mnuFnc and not _byAdmin}"> onclick="goLout(event, '${ptMnuLoutDVo.mnuLoutId}'); return false;"</c:if
			> class="${ptMnuLoutDVo.imgKndVa}${on}"><span>${ptMnuLoutDVo.rescNm}</span></a></dd></c:forEach
		>
	</dl>
	</div>
</div>
<!--//center E-->

<!--right S-->
<div class="right" style="z-index:5;">
	<div class="search">
		<div class="inputarea">
		<dl><%//임직원 검색 %>
		<dd><form id="topSearchUserForm" onsubmit="doNotSubmit(event); return false;"><span class="inputbg"><input type="text" id="topUserName" name="userNm" onfocus="searchUserTop(event, 'focus', 'topSearchUserForm')" onblur="searchUserTop(event, 'blur', 'topSearchUserForm')"
			onkeydown="if(event.keyCode==13){searchUserTop(event, 'keydown', 'topSearchUserForm'); return false;}" class="input" style="width:100%;" value="<u:msg
			titleId="pt.top.orgSrch" alt="임직원 검색" />" title="<u:msg
			titleId="pt.top.orgSrch" alt="임직원 검색" />" /></span><span class="inputbtn" title="<u:msg
			titleId="pt.top.orgSrch" alt="임직원 검색" />"><a href="javascript:void(0);" id="topSearchUserBtn" onclick="searchUserTop(event, 'click', 'topSearchUserForm')" class="inputbtn"><span><u:msg
			titleId="pt.top.orgSrch" alt="임직원 검색" /></span></a></span><c:if
					test="${not empty globalOrgChartEnable}"><input type="hidden" name="global" value="Y" /></c:if></form></dd>
		</dl>
		</div><c:if
			
		test="${not empty integratedSearchEnable}">
		<div class="inputarea">
		<dl><%//통합 검색 %>
		<dd><form id="topSearchIntgForm" action="/sh/index.do"><span class="inputbg"><input type="text" id="topIntgName" name="kwd" onfocus="searchIntgTop(event, 'focus', 'topSearchIntgForm')" onblur="searchIntgTop(event, 'blur', 'topSearchIntgForm')"
			onkeydown="searchIntgTop(event, 'keydown', 'topSearchIntgForm')" class="input" style="width:100%;" value="<u:msg
			titleId="pt.top.unifySrch" alt="통합 검색" />" title="<u:msg
			titleId="pt.top.unifySrch" alt="통합 검색" />" /></span><span class="inputbtn" title="<u:msg
			titleId="pt.top.unifySrch" alt="통합 검색" />"><a href="javascript:void(0);" id="topSearchIntgBtn" onclick="$('#topSearchIntgForm').submit();" class="inputbtn"><span><u:msg
			titleId="pt.top.unifySrch" alt="통합 검색" /></span></a></span></form></dd>
		</dl>
		</div>
		</c:if>
	</div>
	<div class="searchtxt">
		<dl id="topRightMnuArea"><%//부서명▽,사용자명,관리자▽,[추가메뉴],로그아웃 %>
			<c:if test="${fn:length(sessionScope.userVo.adurs)<=1}"
				><dd class="rt_department"><a href="<u:authUrl url="/or/user/setUser.do" />">${sessionScope.userVo.deptNm}</a></dd></c:if
			><c:if test="${fn:length(sessionScope.userVo.adurs)>1}"
				><dd id="AdurMnu" class="rt_department_arrow"><a href="<u:authUrl url="/or/user/setUser.do" />">${sessionScope.userVo.deptNm}</a></dd></c:if>
			<dd class="rt_name"><a href="<u:authUrl url="/or/user/setUser.do" />">${sessionScope.userVo.userNm}</a></dd><c:if
				test="${not sessionScope.userVo.excliLginYn and empty isExAuth}"><c:if
						test="${fn:length(admList)==1}" >
			<dd class="rt_blank"></dd>
			<dd class="rt_menu"><a href="<u:menuUrl url="${admList[0].mnuUrl}" />"<c:if
					test="${not empty admList[0].mnuFnc}"> onclick="<u:menuUrl url="${admList[0].mnuFnc}" /> return false;"</c:if
				><c:if
					test="${empty admList[0].mnuFnc and not _byAdmin}"> onclick="goLout(event, '${admList[0].mnuLoutId}'); return false;"</c:if
				>><u:msg titleId="pt.top.adm" alt="관리자" /></a></dd>
			</c:if><c:if
						test="${fn:length(admList)>1}" >
			<dd class="rt_blank"></dd>
			<dd id="AdmMnu" class="rt_admin"><a href="javascript:void(0);"><u:msg titleId="pt.top.adm" alt="관리자" /></a></dd></c:if
			></c:if><c:forEach
			items="${_rightList}" var="ptMnuLoutDVo" varStatus="leftStatus">
			<dd class="rt_blank"></dd>
			<dd class="rt_menu"><a href="<u:menuUrl url="${ptMnuLoutDVo.mnuUrl}" />"<c:if
				test="${not empty ptMnuLoutDVo.mnuFnc}"> onclick="<u:menuUrl url="${ptMnuLoutDVo.mnuFnc}" /> return false;"</c:if
				><c:if
				test="${empty ptMnuLoutDVo.mnuFnc and not _byAdmin}"> onclick="goLout(event, '${ptMnuLoutDVo.mnuLoutId}'); return false;"</c:if
				> title="${ptMnuLoutDVo.rescNm}">${ptMnuLoutDVo.rescNm}</a></dd></c:forEach>
			<dd class="rt_blank"></dd>
			<dd class="rt_menu"><a href="/cm/login/processLogout.do"<c:if test="${not empty hashUid}"> onclick="webPushUtil.doStop();"</c:if>><u:msg titleId="pt.top.logout" alt="로그아웃" /></a></dd>
		</dl>
	</div>
</div>
<!--//right E-->

<div id="topPopMnuArea" style="position:absolute; z-index:100;">
<!--center toppop S-->
<c:forEach items="${_iconList}" var="ptMnuLoutDVo" varStatus="outerStatus"
><c:if test="${fn:length(ptMnuLoutDVo.childList)>1}"><%// [팝업]아이콘 서브메뉴 - 한건일 경우 아이콘의 경로를 바꾸므로 필요 없음 %>
<div id="topSubIcon${ptMnuLoutDVo.mnuLoutId}" style="position:absolute; width:auto; top:83px; left:0px; z-index:100; opacity:0; filter:alpha(opacity=0);">
	<div class="toppop">
		<div class="toppop_arrow" style="position:relative"><img src="${_cxPth}/images/${_skin}/header_icon/arrow_ct.png"/></div>
		<div class="toppop_body" style="min-width:80px;">
		<ul><c:forEach
			items="${ptMnuLoutDVo.childList}" var="childPtMnuLoutDVo" varStatus="status"
			><c:if test="${not status.first}">
			<li class="toppop_line"></li></c:if>
			<li><a href="<u:menuUrl url="${childPtMnuLoutDVo.mnuUrl}" />"<c:if
			test="${not empty childPtMnuLoutDVo.mnuFnc}"> onclick="<u:menuUrl url="${childPtMnuLoutDVo.mnuFnc}" /> return false;"</c:if
			><c:if
			test="${empty childPtMnuLoutDVo.mnuFnc and not _byAdmin}"> onclick="goLout(event, '${childPtMnuLoutDVo.mnuLoutId}'); return false;"</c:if
			> class="toppop_menu"><nobr>${childPtMnuLoutDVo.rescNm}</nobr></a></li></c:forEach
			>
		</ul>
		</div>
	</div>
</div>
</c:if
></c:forEach>
<!--//center toppop E-->

<!--right toppop S-->
<c:if test="${fn:length(sessionScope.userVo.adurs)>1}" ><%// [팝업]겸직 서브메뉴 - 한건 일 경우 표시 안함 %>
<div id="topSubAdurMnu" style="position:absolute; width:auto; top:90px; left:0px; z-index:100; visibility:visible; opacity:0; filter:alpha(opacity=0);">
	<div class="toppop">
		<div class="toppop_body" style="min-width:80px;">
		<ul><c:forEach
			items="${sessionScope.userVo.adurs}" var="adurs" varStatus="status"
			><c:if test="${not status.first}">
			<li class="toppop_line"></li></c:if>
			<li><a href="/cm/login/processAdurSwitch.do?${sessionScope.userVo.userUid=='U0000001' ? 'compId' : 'userUid'}=${adurs[1]}" class="toppop_menu"><nobr>${adurs[0]}</nobr></a></li></c:forEach
			>
		</ul>
		</div>
	</div>
</div>
</c:if>
<c:if test="${fn:length(admList)>1}" ><%// [팝업]관리자 서브메뉴 - 한건일 경우 "관리자" 클릭하면 바로 해당 메뉴 이동 %>
<div id="topSubAdmMnu" style="position:absolute; width:auto; top:90px; left:0px; z-index:100; visibility:visible; opacity:0; filter:alpha(opacity=0);">
	<div class="toppop">
		<div class="toppop_body" style="min-width:80px;">
		<ul><c:forEach
			items="${admList}" var="ptMnuLoutDVo" varStatus="status"
			><c:if test="${not status.first}">
			<li class="toppop_line"></li></c:if>
			<li><a href="<u:menuUrl url="${ptMnuLoutDVo.mnuUrl}" />"<c:if
				test="${not empty ptMnuLoutDVo.mnuFnc}"> onclick="<u:menuUrl url="${ptMnuLoutDVo.mnuFnc}" /> return false;"</c:if
			><c:if
				test="${empty ptMnuLoutDVo.mnuFnc and not _byAdmin}"> onclick="goLout(event, '${ptMnuLoutDVo.mnuLoutId}'); return false;"</c:if
				> class="toppop_menu"><nobr>${ptMnuLoutDVo.rescNm}</nobr></a></li></c:forEach
			>
		</ul>
		</div>
	</div>
</div>
</c:if>
<!--//right toppop E-->
</div>