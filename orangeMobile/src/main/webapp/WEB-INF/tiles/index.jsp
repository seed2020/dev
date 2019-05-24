<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="com.innobiz.orange.web.pt.secu.UserVo,com.innobiz.orange.web.cm.utils.FinderUtil"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"
%><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"
%><%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"
%><%
	request.setAttribute("footerClass", "mu");
%><!DOCTYPE html>
<html lang="${_lang}">
<head>
<meta name="Content-Type" content="text/html; charset=utf-8" />
<meta name="format-detection" content="telephone=no" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=3" />
<link rel="apple-touch-icon" sizes="57x57" href="/images/icon/apple-icon-57x57.png">
<link rel="apple-touch-icon" sizes="60x60" href="/images/icon/apple-icon-60x60.png">
<link rel="apple-touch-icon" sizes="72x72" href="/images/icon/apple-icon-72x72.png">
<link rel="apple-touch-icon" sizes="76x76" href="/images/icon/apple-icon-76x76.png">
<link rel="apple-touch-icon" sizes="114x114" href="/images/icon/apple-icon-114x114.png">
<link rel="apple-touch-icon" sizes="120x120" href="/images/icon/apple-icon-120x120.png">
<link rel="apple-touch-icon" sizes="144x144" href="/images/icon/apple-icon-144x144.png">
<link rel="apple-touch-icon" sizes="152x152" href="/images/icon/apple-icon-152x152.png">
<link rel="apple-touch-icon" sizes="180x180" href="/images/icon/apple-icon-180x180.png">
<link rel="icon" type="image/png" sizes="192x192"  href="/images/icon/android-icon-192x192.png">
<link rel="icon" type="image/png" sizes="32x32" href="/images/icon/favicon-32x32.png">
<link rel="icon" type="image/png" sizes="96x96" href="/images/icon/favicon-96x96.png">
<link rel="icon" type="image/png" sizes="16x16" href="/images/icon/favicon-16x16.png">
<link rel="manifest" href="/images/icon/manifest.json">
<link id="favicon" rel="shortcut icon" type="image/png" href="/images/icon/favicon-96x96.png" />
<meta name="msapplication-TileColor" content="#ffffff">
<meta name="msapplication-TileImage" content="/images/icon/ms-icon-144x144.png">
<meta name="theme-color" content="#ffffff">
<title><u:term termId="or.term.siteName" /></title>
<link rel="stylesheet" href="${_cxPth}/css/animation.css" type="text/css" />
<tiles:insertAttribute name="headinc" />
<script type="text/javascript" src="${_cxPth}/js/rsa.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/orange-mobile-1.0.1.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/calendar.js" charset="UTF-8"></script>
<script type="text/javascript">
//<![CDATA[
$(document).ready(function() {
	$m.custCode = "${custCode}";
	if(window!=parent && parent.$m!=null){
		parent.location.replace('/');
		return;
	}
	var msg = '${errorByMsgId}';
	if(msg!=''){
		alert(msg);
		top.location.replace('/');
	}
	<%
	// 푸쉬 메세지에 의해 로그인 될 경우 - 기존 사용자와 푸쉬 메세지의 사용자가 다를 경우
	// - 다시 로그인 안 한다고 하면 설정된 초기화면 출력함 %>
	var reloginMsg = '<u:out value="${reloginMsg}" type="script" />';
	if(reloginMsg!=''){
		if(confirm(reloginMsg)){
			$m.nav.init(null, '${loginPage}${tilesMsgIdParam}');
		} else {
			top.location.replace('/');
		}
	} else {
		if('${needUagntToken}'=='true'){
			$m.msgmap['btn-close'] = '<u:msg titleId="mcm.btn.close" />';
			var param = {msgId:"${msgId}","time":""+new Date().getTime()};
			if(window.localStorage) param['token'] = window.localStorage.getItem("uagntToken");
			$m.ajax("/cm/login/createSecuSessionAjx.do?secuSessionCode=${secuSessionCode}", null, 
				function(data){
					var key = $m.rsa.getKey(data.e, data.m);
					var enc = $m.rsa.encrypt(key, JSON.stringify(param));<%
					// 푸쉬 메세지 ID - 로그인 후 이동 페이지 찾기 위해 필요 %>
					$m.nav.init(event, '/cm/login/processMsg.do?secu='+enc);
				}, {secuSession:true} );
			return;
		}
		var msgUrl = '${msgUrl}';<%// msgId 확인 후 로그인 되고, 유효할 경우  %>
		if(msgUrl!=''){
			$m.nav.init(null, msgUrl);
		} else if(!$m.secu.hasKey()){<%// 로그인 페이지로 이동 - msgId 파라미터 유지 %>
			$m.nav.init(null, '${loginPage}${tilesMsgIdParam}');
		} else {
			var pg = '${initPage}';
			$m.nav.init(null, pg=='' ? $m.menu.getFirstUrl() : pg);<%
			// 로그인 후 기본 메뉴 열기 %>
			var afterLoginMnuLoutId = '${afterLoginMnuLoutId}';
			if(afterLoginMnuLoutId!=''){
				window.setTimeout("$m.menu.open('"+afterLoginMnuLoutId+"', null)", 300);
			}
		}
		if(!$m.nav.isMobile){
			$('body').css('background-color','#fbfbfb');
		}
	}
	
	$m.msgmap['msg-timeout'] = '<u:msg titleId="mcm.conn.timeout" />';
	$m.msgmap['btn-close'] = '<u:msg titleId="mcm.btn.close" />';
	$m.msgmap['btn-ok'] = '<u:msg titleId="mcm.btn.ok" />';
	$m.msgmap['btn-cancel'] = '<u:msg titleId="mcm.btn.cancel" />';
	
});<%
// 겸직 결재 건수 클릭 - 원직 + 겸직 이 3 이상일 경우 현재 사용자를 제외한 나머지 사용자 중 선택 %>
function openAdditionalApWaitBx(){
	$m.ajax("/cm/ap/setApAddiUserAjx.do", null, function(result){
		var resultList = result.apvMapList, obj;
		var cdList = [];
		if(resultList!=null && resultList.length>0){
			for(var i=0;i<resultList.length;i++){
				obj = resultList[i];
				cdList.push({cd:obj['userUid'], nm:obj['deptNm']+" ("+obj['count']+")"});
			}
		}
		if(cdList.length>0){
			$m.dialog.openSelect({id:'apAddiUserUid', cdList:cdList}, function(obj){
				$m.menu.switchUser(event, obj.cd, 'waitBx');
			});
		}
	});
}
//]]>
</script>
</head>
<body>
<div class="wrapper">
<aside id="mainMenu" style="z-index:10; left:-200%; width:100%;"><c:if
	test="${not empty sessionScope.userVo}">
	<div class="size" style="z-index:2">

		<div class="aside_header"><c:if
				
				test="${not(fn:length(sessionScope.userVo.adurs)>1)}"
			>
			<div class="header1">
				<dl>
					<dd class="ico"></dd>
					<dd><div class="namearea"><span>${sessionScope.userVo.userNm}</span>${sessionScope.userVo.deptNm}</div></dd>
					<dd class="close" onclick="$m.menu.close();"></dd>
					<dd class="logout" onclick="$m.menu.logout(event);"></dd>
				</dl>
			</div></c:if><c:if
				
				test="${fn:length(sessionScope.userVo.adurs)>1}"
			>
			<div class="header1">
				<dl>
					<dd class="ico"></dd>
					<dd class="subteam" onclick="$(adursSelector).toggle();">
						<div class="subteam_txt">${sessionScope.userVo.deptNm}</div>
						<div class="subteam_btn"></div>
					</dd>
					<dd class="subname" onclick="javascript:;">${sessionScope.userVo.userNm}</dd>
					<dd class="close" onclick="$m.menu.close();"></dd>
					<dd class="logout" onclick="$m.menu.logout(event);"></dd>
				</dl>
			</div></c:if>
			<div class="header2" id="countArea">
				<dl><u:set test="${sessionScope.userVo.hasMnuGrpMdRidOf('MAIL') and not empty MAIL_ON}"
					var="SHOW_MAIL" value="Y" /><c:if
						test="${SHOW_MAIL eq 'Y'}">
					<dd class="body" onclick="$m.menu.openSso(event, {act:'notReadMail'});"><u:msg titleId="pt.top.newMail" alt="새메일"
						/> <span id="topMailCnt">0</span> <u:msg titleId="cm.count" alt="건"
						/></dd>
					<dd class="blank topApvMailCntCls" style="display:none;">l</dd>
					<dd class="body topApvMailCntCls" onclick="$m.menu.openSso(event, {act:'notApvReadMail'});" style="display:none;"><u:msg titleId="pt.top.apvMail" alt="승인메일"
						/> <span id="topApvMailCnt">0</span> <u:msg titleId="cm.count" alt="건"
						/></dd></c:if><c:if
						test="${sessionScope.userVo.hasMnuGrpMdRidOf('AP')}"><c:if
							test="${SHOW_MAIL eq 'Y'}">
					<dd class="blank">l</dd></c:if>
					<dd class="body" onclick="$m.menu.menuClick(event, 'IN_URL', '<u:authUrl
						url="/ap/box/listApvBx.do?bxId=waitBx" />');"><u:msg titleId="pt.top.newApp" alt="결재"
						/> <span id="topAppCnt">0</span> <u:msg titleId="cm.count" alt="건"
						/></dd></c:if><c:if
						test="${sessionScope.userVo.hasMnuGrpMdRidOf('WC')}"><c:if
							test="${SHOW_MAIL eq 'Y' or sessionScope.userVo.hasMnuGrpMdRidOf('AP')}">
					<dd class="blank topSchdlCntCls">l</dd></c:if>
					<dd class="body topSchdlCntCls" onclick="$m.menu.menuClick(event, 'IN_URL', '<u:authUrl
						url="/wc/listNewSchdl.do?fncCal=my" />');" style="${not empty useOdurApvCnt ? 'display:none' : ''}"><u:msg titleId="pt.top.newSchd" alt="일정"
						/> <span id="topSchdlCnt">0</span> <u:msg titleId="cm.count" alt="건"
						/></dd></c:if><c:if
						test="${sessionScope.userVo.hasMnuGrpMdRidOf('AP') and not empty sessionScope.userVo.adurs}">
					<dd class="blank topAdditionalCntCls" style="${empty useOdurApvCnt ? 'display:none' : ''}">l</dd>
					<dd class="body topAdditionalCntCls" onclick="$m.menu.menuClick(event, 'IN_URL', '/changeWhenOnload.do');" style="${empty useOdurApvCnt ? 'display:none' : ''}"><u:msg titleId="pt.top.newAdditional" alt="겸직"
						/> <span id="topAdditionalCnt">0</span> <u:msg titleId="cm.count" alt="건"
						/></dd></c:if>
				</dl>
			</div>
		</div><c:if
				
				test="${fn:length(sessionScope.userVo.adurs)>1}">
		<div class="team_open" style="display:none;" id="adursSelector">
			<div class="team_open2">
				<ul><c:forEach
					items="${sessionScope.userVo.adurs}" var="adurs" varStatus="status"><c:if
						test="${sessionScope.userVo.userUid != adurs[1]}">
					<li onclick="$m.menu.switchUser(event, '${adurs[1]}');"><u:out value="${adurs[0]}" /></li></c:if></c:forEach>
				</ul>
			</div>
		</div></c:if>
		
		
		<div class="aside_nav" id="iconArea"><c:forEach
    	
    	items="${mobileList}" var="ptMnuLoutDVo">
			<div class="ico" onclick="$m.menu.iconClick(event, '${ptMnuLoutDVo.mnuLoutId}', '${ptMnuLoutDVo.mnuGrpTypCd == '11' ? ptMnuLoutDVo.mnuGrpId : ''}')" id="${ptMnuLoutDVo.mnuLoutId}">
				<dl>
					<dd class="${ptMnuLoutDVo.imgKndVa}"></dd>
					<dd class="aside_mu"><u:out value="${ptMnuLoutDVo.rescNm}" /></dd>
				</dl>
			</div></c:forEach><c:if test="${not empty emptySize}"><c:forEach begin="1" end="${emptySize}" step="1">
			<div class="ico_default" onclick="javascript:;">
				<dl>
					<dd class="default"></dd>
				</dl>
			</div></c:forEach></c:if>
		</div>
    
    
    <!--aside-nav-sub S-->
		<div class="navsub" id="subArea">
			<div id="subAniArea"><c:forEach
    	
	    	items="${mobileList}" var="ptMnuLoutDVo" varStatus="subStatus"><u:convert
	    	srcId="${ptMnuLoutDVo.mnuLoutId}" var="asideList" />
	    	<div id="${ptMnuLoutDVo.mnuLoutId}" style="position:absolute; z-index:2; width:100%; left:-200%">
		    	<div class="sub">
		    		<ul><c:forEach
		    				items="${asideList}" var="ptMnuLoutCombDVo">
		    			<li onclick="$m.menu.menuClick(event, '${ptMnuLoutCombDVo.ptMnuDVo.mnuTypCd
		    				}', $(this).attr('data-url'));" data-url="${ptMnuLoutCombDVo.mnuUrl
		    				}" id="${ptMnuLoutCombDVo.mnuLoutCombId}"><u:out value="${ptMnuLoutCombDVo.rescNm}" /></li></c:forEach>
		    		</ul>
		    	</div>
		    	<div class="dno" >&nbsp;</div>
		    </div>
		    </c:forEach><c:forEach
    	
	    	items="${footerList}" var="ptMnuLoutDVo" varStatus="subStatus"><u:convert
	    	srcId="${ptMnuLoutDVo.mnuLoutId}" var="asideList" />
	    	<div id="${ptMnuLoutDVo.mnuLoutId}" style="position:absolute; z-index:2; width:100%; left:-200%">
		    	<div class="sub">
		    		<ul><c:forEach
		    				items="${asideList}" var="ptMnuLoutCombDVo">
		    			<li onclick="$m.menu.menuClick(event, '${ptMnuLoutCombDVo.ptMnuDVo.mnuTypCd
		    				}', $(this).attr('data-url'));" data-url="${ptMnuLoutCombDVo.mnuUrl
		    				}" id="${ptMnuLoutCombDVo.mnuLoutCombId}"><u:out value="${ptMnuLoutCombDVo.rescNm}" /></li></c:forEach>
		    		</ul>
		    	</div>
		    	<div class="dno" >&nbsp;</div>
		    </div>
		    </c:forEach>
		    </div>
		    <jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
		</div>
    </div>
    
    <div class="background" style="z-index:1; left:-400%; display:none;" onclick="$m.menu.close();"></div></c:if>
</aside>
</div>

<div id="fixedMenuIcon" class="fixed_bl" style="${empty fixedMenuIcon ? 'display:none' : ''}">
<div><a href="javascript:$m.menu.fixedMenuClick();" class="menu"></a></div>
</div>

<iframe id="dataframe" name="dataframe" src="about:blank" style="border:0px; width:0px; height:0px; position:absolute;"></iframe>
</body>
</html>