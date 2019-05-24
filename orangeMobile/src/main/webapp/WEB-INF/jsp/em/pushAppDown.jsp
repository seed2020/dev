<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta name="Content-Type" content="text/html; charset=utf-8" />
<meta name="format-detection" content="telephone=no" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1" />
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
<title>푸쉬앱 다운로드</title>
<link rel="stylesheet" href="${_cxPth}/css/m.gworange.blue.css" type="text/css" />
<style type="text/css">
#pushInfoList dd.etr_tit{margin-top:0px;}
#pushInfoList dd.etr_bodytit{font-size:inherit;margin-top:14px;}
#pushInfoList dd.name{margin-top:14px;}
.btnarea { margin:45px 0 0 0; padding:0 10px 0 10px;width:inherit;float:none; }
.btnarea .btn { height:35px; font-size:1.6em; color:#fff; text-align:center; background:#109aad; border:1px solid #109aad; border-radius:3px; padding:14px 0 0 0; cursor:pointer; }
.wrapper{background:#f7f7fb;}
/* .listdiv{background:#f7f7fb;} */
</style>
<script type="text/javascript" src="${_cxPth}/js/jquery-2.1.3.min.js" charset="UTF-8"></script>
<script type="text/javascript">
//<![CDATA[
function pushDown(){
	//location.href="/push/PlakorNoti.apk";
	var uagnt = navigator.userAgent;
	var isMobile=/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(uagnt);
	var isSafari=uagnt.indexOf('Safari') > 0 && !(uagnt.indexOf('Chrome') > 0);
	var $form = $('<form>', {
		'method':'get',
		'action':'/push/PlakorNoti.apk',
		'target':isMobile && isSafari ? '' : 'dataframe'
	});
	$('form[action="/push/PlakorNoti.apk"]').remove();
	$(top.document.body).append($form);
	$form.submit();
}
//]]>
</script>
</head>
<body>
<div class="wrapper">
<!--section S-->
    <div class="blankzone"><div class="blank30"></div></div>
	<div style="margin:7px 0 0 10px;text-align:center;">
		<img src="/push/logo.png" width="150px"/>
	</div>
    <div class="blankzone"><div class="blank30"></div></div>
	<div class="btnarea">
        <div class="btn" onclick="pushDown();">푸쉬앱 다운로드</div>
    </div>
    <!-- <div class="attachzone">
    <div class="attacharea">
        <div class="attachin">
            <div class="attach" onclick="javascript:pushDown();">
                <div class="btn"></div>
                <div class="txt">푸쉬앱 다운로드</div>
            </div>
            <div class="down" onclick="javascript:pushDown();"></div>
        </div>
    </div>
    </div> -->
    <div class="blankzone"><div class="blank30"></div></div>
    <div class="entryzone" style="display:none;">
        <div class="entryarea">
        <dl>
        <dd class="etr_tit">설치 방법</dd>
        <dd class="etr_bodytit">위 '푸쉬앱 다운로드'를 통해 설치파일을 다운로드 하세요.</dd>
        <dd class="etr_bodytit">알림목록에서 다운로드 받은 파일을 click 하세요</dd>
        <dd class="etr_bodytit">'이 애플리케이션을 설치하시겠습니까?' 라고 나오며 '설치'를 선택하세요</dd>
        <dd class="etr_bodytit">애플리케이션 설치가 완료되면 해당 앱을 실행 후 이용하시면 됩니다.</dd>
        <dd class="etr_bodytit_asterisk">안드로이드 단말기만 이용가능.</dd>
     	</dl>
    </div>
    </div>
    <div class="listarea">
        <article>
            <div class="listdiv" onclick="javascript:;">
                <div class="list" id="pushInfoList">
                <dl>
                <dd class="etr_tit">푸쉬앱 설치 방법</dd>
                <dd class="etr_bodytit">위 '다운로드'를 통해 설치파일을 다운로드 하세요.</dd>
		        <dd class="etr_bodytit">알림목록에서 다운로드 받은 파일을 click 하세요</dd>
		        <dd class="etr_bodytit">'이 애플리케이션을 설치하시겠습니까?' 라고 나오며 '설치'를 선택하세요</dd>
		        <dd class="etr_bodytit">애플리케이션 설치가 완료되면 해당 앱을 실행 후 이용하시면 됩니다.</dd>
                <dd class="name etr_bodytit_asterisk">안드로이드 단말기만 이용가능.</dd>
             </dl>
                </div>
            </div>
        </article>
    </div>
            
</div>
<iframe id="dataframe" name="dataframe" src="about:blank" style="border:0px; width:0px; height:0px; position:absolute;"></iframe>
</body>
</html>

