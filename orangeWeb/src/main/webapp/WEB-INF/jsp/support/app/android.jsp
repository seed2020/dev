<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><!DOCTYPE html>
<html lang="ko">
<head>
<meta name="Content-Type" content="text/html; charset=utf-8" />
<meta name="format-detection" content="telephone=no" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1" />
<title>푸쉬앱 다운로드</title>
<link rel="stylesheet" href="/css/m.gworange.blue.css" type="text/css" />
<style type="text/css">
#pushInfoList dd.etr_tit{margin-top:0px;}
#pushInfoList dd.etr_bodytit{font-size:inherit;margin-top:14px;}
#pushInfoList dd.name{margin-top:14px;}
.btnarea { margin:45px 0 0 0; padding:0 10px 0 10px;width:inherit;float:none; }
.btnarea .btn { height:35px; font-size:1.6em; color:#fff; text-align:center; background:#109aad; border:1px solid #109aad; border-radius:3px; padding:14px 0 0 0; cursor:pointer; }
.wrapper{background:#f7f7fb;}
</style>
</head>
<body>
<div class="wrapper">
<!--section S-->
	<div class="blankzone"><div class="blank30"></div></div>
	<div style="background-image: url(/images/etc/app.png);
		background-size: contain; background-repeat: no-repeat; background-position:center center;
		margin:40px 0 0 0; height:110px">
	</div>
	<div class="btnarea">
		<div class="btn" onclick="pushDown();" style="padding-top:24px;"><a href="./OrangeNoti.apk" style="color:white; font-weight:bold;">DOWNLOAD for Android</a></div>
	</div>
	<div class="blankzone"><div class="blank20"></div></div>
	<div class="listarea">
		<article>
			<div class="listdiv">
			<div class="list" id="pushInfoList">
				<dl>
				<dd class="etr_tit">${ptPushAppDVo.custNm} 안드로이드 앱 설치</dd>
				<dd class="etr_bodytit">위 링크를 클릭해서 "${ptPushAppDVo.dispNm}"을 설치 하세요.</dd>
				<dd class="etr_bodytit">안드로이드 단말기만 설치 가능 합니다.</dd>
				</dl>
			</div>
			</div>
		</article>
	</div>
</div>

</body>
</html>