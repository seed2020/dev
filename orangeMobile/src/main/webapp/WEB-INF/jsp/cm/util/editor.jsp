<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1" />
<title><u:msg titleId="mcm.title.editCont" alt="내용편집" /></title>
<style type="text/css">
body { line-height:1; font-family:"gulim","dotum","arial"; font-size:0.8em; color:#222; line-height:17px; }
body, div, span, p, dl, dt, dd, ol, ul, li, form, label { margin:0; padding:0; border:0; outline:0; font-size:100%; vertical-align:baseline; background:transparent; }

body { -webkit-text-size-adjust:none; background:#dce5e9; }
/* nav */
.nav { width:100%; height:51px; font-size:1.2em; color:#fff; text-align:center; background:#dce5e9; }
.nav_lt { position:absolute; top:6px; left:5px; z-index:1; }
.nav_lt dd,.nav_rt dd { float:left; width:40px; cursor:pointer; }
.nav_rt { position:absolute; top:6px; right:5px; z-index:1; }
.nav .size,.font,.font_on,.align,.align_on,.color,.italic,.italic_on,.nav_rt dd,.open_fontin dd,.open_sortingin dd { height:39px; }
.nav .font,.font_on,.align,.align_on,.color,.save { border-top:1px solid #82a6ba; border-right:1px solid #82a6ba; border-bottom:1px solid #82a6ba; }
.nav .italic,.italic_on,.under,.under_on,.center,.center_on,.right,.right_on { border-right:1px solid #658da5; border-bottom:1px solid #658da5; }
.nav .size,.cancel { border:1px solid #82a6ba; border-top-left-radius:7px; }
.nav .bold,.bold_on,.left,.left_on { border-left:1px solid #658da5; border-right:1px solid #658da5; border-bottom:1px solid #658da5; border-bottom-left-radius:7px; }
.nav .color,.save { border-top-right-radius:7px; }
.nav .under,.under_on,.right,.right_on { border-bottom-right-radius:7px; }
.nav .size { background:#a4c1ce url("/images/editor/size.png") no-repeat; background-size:contain; }
.nav .font { background:#a4c1ce url("/images/editor/font.png") no-repeat; background-size:contain; }
.nav .align { background:#a4c1ce url("/images/editor/align.png") no-repeat; background-size:contain; }
.nav .color { background:#a4c1ce url("/images/editor/color.png") no-repeat; background-size:contain; }
.nav .cancel { background:#a4c1ce url("/images/editor/cancel.png") no-repeat; background-size:contain; }
.nav .save { background:#a4c1ce url("/images/editor/save.png") no-repeat; background-size:contain; }
.nav .bold { background:#82a6ba url("/images/editor/bold.png") no-repeat; background-size:contain; }
.nav .italic { background:#82a6ba url("/images/editor/italic.png") no-repeat; background-size:contain; }
.nav .under { background:#82a6ba url("/images/editor/under.png") no-repeat; background-size:contain; }
.nav .left { background:#82a6ba url("/images/editor/left.png") no-repeat; background-size:contain; }
.nav .center { background:#82a6ba url("/images/editor/center.png") no-repeat; background-size:contain; }
.nav .right { background:#82a6ba url("/images/editor/right.png") no-repeat; background-size:contain; }
.nav .font_on { background:#82a6ba url("/images/editor/font.png") no-repeat; background-size:contain; }
.nav .align_on { background:#82a6ba url("/images/editor/align.png") no-repeat; background-size:contain; }
.nav .bold_on { background:#658da5 url("/images/editor/bold.png") no-repeat; background-size:contain; }
.nav .italic_on { background:#658da5 url("/images/editor/italic.png") no-repeat; background-size:contain; }
.nav .under_on { background:#658da5 url("/images/editor/under.png") no-repeat; background-size:contain; }
.nav .left_on { background:#658da5 url("/images/editor/left.png") no-repeat; background-size:contain; }
.nav .center_on { background:#658da5 url("/images/editor/center.png") no-repeat; background-size:contain; }
.nav .right_on { background:#658da5 url("/images/editor/right.png") no-repeat; background-size:contain; }
.nav .open_font,.open_sorting { position:absolute; top:46px; width:126px; }
.nav .open_font dd,.open_sorting dd { float:left; width:40px; cursor:pointer; }
.nav .open_font { left:46px; z-index:1; }
.nav .open_sorting { left:87px; z-index:1; }
/* entry */
.entryarea { position:absolute; top:46px; right:5px; bottom:5px; left:5px; background:#fff; border:1px solid #b6cdd9; border-bottom-right-radius:7px; border-bottom-left-radius:7px; overflow:hidden; }
.entry { float:left; width:100%; height:100%; color:#454545; padding:0; overflow-x:auto; overflow-y:scroll; }

.popbackground { position:absolute; top:0; right:0; bottom:0; left:0; background:#456071; filter:alpha(opacity=70); opacity:0.6; }
.popbackground .btn { float:right; width:37px; height:37px; background:url("/images/blue/aside_close.png") no-repeat; margin:-17px 17px 5px 0; background-size:contain; cursor:pointer; }
.popuparea { position:absolute; top:20px; right:20px; bottom:20px; left:18px; }
.popuparea .popup { position:relative; width:100%; height:100%; background:#fff; border:1px solid #4a6173; border-radius:10px; overflow-x:hidden; overflow-y:auto; }
.popuparea_s2 { position:absolute; top:50%; left:50%; margin:-140px 0 0 -150px; }
.popuparea_s2 .btn { float:right; width:37px; height:37px; background:url("/images/blue/aside_close.png") no-repeat; margin:0 0 1px 0; background-size:contain; cursor:pointer; }
.popuparea_s2 .popup { width:300px; height:223px; margin:38px 0 0 0; }
.popuparea_s2 .inner { width:100%; height:100%; background:#fff; border:1px solid #4a6173; border-radius:10px; overflow-x:hidden; overflow-y:auto; }
.pop_radio { float:left; width:100%; }
.pop_radio .txt { float:left; width:100%; font-size:1.6em; padding:6px 0 0 0; }
.pop_radio .line { float:left; width:100%; border-bottom:1px solid #d4d4d4; }
.pop_radio dl { float:left; width:100%; cursor:pointer; }
.sradio,.sradio_on,.sradio_disabled,.sradio_disabled_on { position:relative; left:0; right:2px; height:26px; margin:8px 0 8px 8px; padding:0 2px 0 33px; }
.sradio { background:url("/images/blue/radio.png") no-repeat; background-size:contain; }
.sradio_on { background:url("/images/blue/radio_on.png") no-repeat; background-size:contain; }
.sradio_disabled { background:url("/images/blue/radiod.png") no-repeat; background-size:contain; }
.sradio_disabled_on { background:url("/images/blue/radiod_on.png") no-repeat; background-size:contain; }
.sradio span,.sradio_on span,.sradio_disabled span,.sradio_disabled_on span { float:left; width:100%; font-size:1.25em; white-space:nowrap; text-overflow:ellipsis; overflow:hidden; padding:5px 0 0 0; }
#colorPicker td { width:40px; height:35px; }
</style>
<script type="text/javascript" src="/js/jquery-2.1.3.min.js" charset="utf-8"></script>
<script type="text/javascript" src="/js/orange-mobile-1.0.1.js" charset="utf-8"></script>
<script type="text/javascript" src="/js/jellyMobile.1.0.1.min.js" charset="utf-8"></script>
</head>
<body class="noInit" style="margin:0; padding:0;">
	<div id="nav" class="nav" style="z-index:2" onclick="toolbarClick(event);">
		<div class="nav_lt">
			<dl>
			<dd class="size" onclick="popStyle(event,'font-size');"></dd>
			<dd class="font" onclick="popStyle(event,'font-pop');"></dd>
			<dd class="align" onclick="popStyle(event,'text-align');"></dd>
			<dd class="color" onclick="popStyle(event,'color');"></dd>
			</dl>
		</div>
		<div class="nav_rt">
			<dl>
			<dd class="cancel" onclick="window.close();"></dd>
			<dd class="save" onclick="setEditHtml(); window.close();"></dd>
			</dl>
		</div>
	</div>
	<div class="entryarea">
	<iframe id="jellyFrame" class="entry" src="editorJelly.do" style="border:0; margin:0"></iframe>
	</div>
</body>
</html>