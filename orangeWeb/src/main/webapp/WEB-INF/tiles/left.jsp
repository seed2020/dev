<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%// 왼쪽 메뉴 영역 %>
var $gLeftMnuArea = null;
<%// 왼쪽 메뉴 열고 닫기 %>
function toggleLeftMenu(id){
	var $mnu = $gLeftMnuArea.find("#"+id);
	var $sub = $gLeftMnuArea.find("#sub"+id);
	var on = null;
	if($sub.length>0){
		if($sub.css("display")=="none"){
			on = true;
			$sub.show();
		} else {
			on = false;
			$sub.hide();
		}
	}
	if(on != null){
		var tagNm = $mnu.tagName();
		if(tagNm=='dd'){<%// 1 level left menu%>
			if(on==true){
				<%// 현재 이전 메뉴 스타일 제거 - 바뀐 스타일 적용 %>
				$gLeftMnuArea.children("dd.sidemenuopen").each(function(){
					$(this).attr("class", "sidemenu");
					$(this).children("a:first").attr("class", "sidetxt");
				});
				$mnu.attr("class", "sidemenuopen").children("a:first").attr("class", "sidetxtopen");
			} else if(on==false){
				$mnu.attr("class", "sidemenu").children("a:first").attr("class", "sidetxt");
			}
		} else if(tagNm=='li'){
			var $icon = $mnu.children("a:first").find("img");
			if(on==true){
				$icon.attr("src", "${_cxPth}/images/${_skin}/sidetree_open.gif");
			} else if(on==false){
				$icon.attr("src", "${_cxPth}/images/${_skin}/sidetree_close.gif");
			}
		}
	}
}
<%// 초기 메뉴 설정 %>
function initLeftMenu(id){
	if(id=='Left') return;
	var $obj = $gLeftMnuArea.find("#"+id), $p, $a = $obj.children("a:last");
	<%// 2레벨 또는 그 이상 %>
	if($a.attr("class")=="menu"){
		$a.attr("class", "menu_open");
		
		if($obj.parent().tagName()=='ul'){
			$p = $obj.parent();
			$p.show();
			<%// 2레벨 이상의 경우 [-] 표시하고 보이게 함 %>
			while($p.parent().parent().tagName()=='ul'){
				$p.parent().find("img:first").attr("src", "${_cxPth}/images/${_skin}/sidetree_open.gif");
				$p = $p.parent().parent();
				$p.show();
			}
			<%// 1레벨 %>
			if($p.parent().tagName()=='dd'){
				$p = $p.parent();
				$p.prev().attr("class", "sidemenuopen").children("a:first").attr("class", "sidetxtopen");
				$p.show();
			}
		<%// 2레벨 메뉴일 경우 - 바뀐 스타일 적용 %>
		} else if($obj.parent().parent().tagName()=='dd'){
			$p = $obj.parent().parent();
			$p.show();
			<%// 1레벨%>
			$p.prev().attr("class", "sidemenuopen").children("a:first").attr("class", "sidetxtopen");
		}
	} else {
		<%// 1레벨 메뉴일 경우 - 바뀐 스타일 적용 %>
		$obj.attr("class","sidemenunoopen").children("a:first").attr("class","sidetxtopen");
	}
}
$(document).ready(function() {
	$gLeftMnuArea = $("#leftMenuArea");
	initLeftMenu('Left${_ptMnuLoutCombDVo.mnuLoutCombId}');
});
//-->
</script>
	<!--
	<div class="bar">
	<ul>
	<li><a href="javascript:" class="sidebtn"><span>left-resize-bar</span></a></li>
	</ul>
	</div>
	-->

	<div class="titlebg">
	    <div class="title ellipsis" title="<u:out value="${_ptMnuLoutDVo.rescNm}" type="value" /><c:if
	    	test="${_ptMnuLoutDVo.loutCatId=='A'}"> <u:msg titleId="pt.top.adm" alt="관리자" /></c:if>">${_ptMnuLoutDVo.rescNm}<c:if
	    	test="${_ptMnuLoutDVo.loutCatId=='A'}"> <u:msg titleId="pt.top.adm" alt="관리자" /></c:if></div>
	</div>

	<div>
	<dl id="leftMenuArea"><c:forEach
		items="${_sideList}" var="ptMnuLoutCombDVo1" varStatus="status1"
		><c:if test="${ptMnuLoutCombDVo1.fldYn!='Y'}">
		<dd id="Left${ptMnuLoutCombDVo1.mnuLoutCombId}" class="sidemenuno"><a href="<u:menuUrl url="${ptMnuLoutCombDVo1.mnuUrl}" />"<c:if
			test="${not empty ptMnuLoutCombDVo1.mnuFnc}"> onclick="<u:menuUrl url="${ptMnuLoutCombDVo1.mnuFnc}" /> return false;"</c:if
			> class="sidetxt">${ptMnuLoutCombDVo1.rescNm}</a></dd></c:if
		><c:if test="${ptMnuLoutCombDVo1.fldYn=='Y'}">
		<dd id="Left${ptMnuLoutCombDVo1.mnuLoutCombId}" class="sidemenu"><a href="javascript:toggleLeftMenu('Left${ptMnuLoutCombDVo1.mnuLoutCombId}');" class="sidetxt">${ptMnuLoutCombDVo1.rescNm}</a></dd>
		<dd id="subLeft${ptMnuLoutCombDVo1.mnuLoutCombId}" class="sidetree" style="overflow:auto; display:none;">
		<ul class="sidetree_menu"><c:forEach
			items="${ptMnuLoutCombDVo1.childList}" var="ptMnuLoutCombDVo2" varStatus="status2">
<u:leftTree vo="${ptMnuLoutCombDVo2}" tabCount="0" /></c:forEach
>
		</ul>
		</dd></c:if
	></c:forEach>
	</dl>
	</div>