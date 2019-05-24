<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1" />
<style type="text/css">
body, html { font:10pt "dotum","gulim","arial"; color:#454545; margin:0; padding:0; border:0; }
div, span, p, dl, dt, dd, ol, ul, li, form, label { margin:0; padding:0; border:0; outline:0; vertical-align:baseline; background:transparent; }
#jellyOuter { position:absolute; top:0px; left:0px; right:0px; bottom:0px; margin:3px; overflow:auto }
#jellyDoc { font:10pt "dotum","gulim","arial""; color:#454545; margin:0; padding:0; border:0; position:absolute; top:0px; left:0px; right:0px; bottom:0px; zoom:120%; }
#jellyDoc td, #jellyDoc th { vertical-align:middle; margin: 1px 2px 1px 2px; word-break:break-all; }
#jellyDoc P, #jellyDoc li { margin-top:2px; margin-bottom:2px; word-break:break-all; }
#jellyDoc ul, #jellyDoc ol { margin-top:5px; margin-bottom:5px; padding-top:0px; padding-bottom:0px; }
</style>
<script type="text/javascript" src="/js/jquery-2.1.3.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="/js/jellyMobile.1.0.1.min.js" charset="UTF-8"></script>
<script type="text/javascript">
$(document).ready(function() {
	var pw = parent;
	var tw = pw.opener!=null && pw.opener.$m!=null ? pw.opener.$m.nav.getWin(0) : null;
	if(pw!=pw.opener && tw!=null && tw.getEditHtml){
		var editHtml = tw.getEditHtml();
		if(editHtml!=null && editHtml!='') setHtml(editHtml);
	}
});
</script>
</head>
<body class="noInit"><script type="text/javascript">draw();</script></body>
</html>