<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
var gHeader = null;
var gHeaderHieght = null;
var gOuter = null;
function resizeOuter(){
	var h = $(window).height() - gHeaderHieght;
	var w = gHeader.width(), w2;
	gOuter.height(h+"px");
	gOuter.width(w+"px");
	if(w!=(w2 = gHeader.width())) gOuter.width(w2+"px");
}
$(document).ready(function() {
	gHeader = $("#header_${_skin}");
	gHeaderHieght = gHeader.height();
	gOuter = $("#outerFrm");
	$("body").css("overflow-y", "hidden");
	resizeOuter();
	$(window).resize(resizeOuter);
});
//-->
</script>
<iframe id="outerFrm" src="${frameUrl}"
	style="overflow-x:hidden; overflow-y:hidden; width:0px; height:0px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>