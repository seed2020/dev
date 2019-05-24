<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%

System.out.println(session.getAttribute("userVo"));

%><!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="/js/jquery-1.10.2.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="/js/common.js" charset="UTF-8"></script>
<script type="text/javascript" src="/js/commonEx.js" charset="UTF-8"></script>
<script>

var pcNoti_attempts = 0;
function pcNoti_waitForInit() {
	if (window['Notification'] === undefined) {
		if (pcNoti_attempts < 12) {
			pcNoti_attempts++;
			console.log('pcNoti_waitForInit - count : '+pcNoti_attempts);
			setTimeout(pcNoti_waitForInit, 333);
		} else {
			// 알림 설치 안됨 - for ie
		}
	} else {
		Notification.requestPermission(function(permission){});
	}
}
function isPcNotiGranted(){
	return window['Notification'] !=null && window['Notification'].permission == 'granted';
}

function alertPcNoti(title, body, url){
	if(browser.ie){
		if(!isPcNotiGranted()) return;
		var notification = new Notification(title, {
			body: body,
			icon: location.protocol+"//"+location.hostname+(location.port=="" ? "" : ":"+location.port)+"/images/cm/fcmBell.png",
			data: url
		});
		notification.onclick = function(event) {
			window.focus();
			window.open(url, '_blank');
		};
	} else {
		var notification = new Notification(title, {
			body: body,
			icon: "/images/cm/fcmBell.png",
			data: url
		});
		notification.onclick = function(event) {
			event.preventDefault();
			notification.close();
			window.open(url, '_blank');
		};
	}
}

function sendNoti(){
	alertPcNoti($("#title").val(), $("#body").val(), $("#url").val());
}

$(document).ready(function() {
	pcNoti_waitForInit();
});

</script>
</head>
<body>

<table>
<tr><td>title : </td><td><input id="title" value="알림" /></td></tr>
<tr><td>body : </td><td><input id="body" value="결재문서 도착" /></td></tr>
<tr><td>url : </td><td><input id="url" value="http://www.naver.com" /></td></tr>
</table>
<br/><br/>

<a href='javascript:sendNoti()'>보내기</a>
<br/><br/>


</body>

</html>