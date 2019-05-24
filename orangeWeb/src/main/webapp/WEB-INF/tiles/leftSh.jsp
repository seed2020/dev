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
	var $obj = $gLeftMnuArea.find("#"+id), $p = $obj, $a = $obj.children("a:last");
	if($a.attr("class")=="menu"){
		$a.attr("class", "menu_open");
		while(true){<%// 상위 폴더 열기 %>
			$p = $p.parent().parent();
			$p.show();
			if($p.tagName()=='ul'){
				$p.parent().find("a:first").attr("src", "${_cxPth}/images/${_skin}/sidetree_open.gif");
			} else {
				$p.prev().attr("class", "sidemenuopen").children("a:first").attr("class", "sidetxtopen");
				break;
			}
		}
	} else {
		<%// 1레벨 메뉴일 경우 - 바뀐 스타일 적용 %>
		$obj.attr("class","sidemenunoopen").children("a:first").attr("class","sidetxtopen");
	}
}
function goAllSearch(mdRid, cnt){
	if(cnt=='0'){
		alertMsg('cm.msg.noData');<%// cm.msg.noData=해당하는 데이터가 없습니다.%>
	} else {
		top.location.href = '/sh/index.do?mdRid='+mdRid+'&kwd='+encodeURIComponent("<u:out value='${param.kwd}' type='script'/>");
	}
}
function goBxSearch(mdRid, mdBxId){
	top.location.href = '/sh/index.do?mdRid='+mdRid+'&mdBxId='+mdBxId+'&kwd='+encodeURIComponent("<u:out value='${param.kwd}' type='script'/>");
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
	    <div class="title ellipsis" title="<u:msg titleId="pt.title.integratedSearch" />"><u:msg titleId="pt.title.integratedSearch" /></div>
	</div>

	<div>
	<dl id="leftMenuArea"><c:forEach items="${mdRids}" var="currMdRid" ><u:convert
		srcId="${currMdRid}CountSum" var="resultSum" /><c:if
		test="${empty resultSum or resultSum==0}">
	<dd id="LeftM000004J" class="sidemenuno${currMdRid==mdRid ? 'open' : ''}"><a href="javascript:goAllSearch('${currMdRid}', '0')" class="sidetxt${currMdRid==mdRid ? 'open' : ''}"><u:msg titleId="pt.sh.menu.${currMdRid}" /> (0)</a></dd>
		</c:if><c:if
		test="${not(empty resultSum or resultSum==0)}">
	<dd id="Left${currMdRid}" class="sidemenu"><a href="javascript:toggleLeftMenu('Left${currMdRid}');" class="sidetxt"><u:msg titleId="pt.sh.menu.${currMdRid}" /> (${resultSum})</a></dd>
	<dd id="subLeft${currMdRid}" class="sidetree" style="overflow:auto;">
		<ul class="sidetree_menu">
			<li id="Left${currMdRid}_ALL">
			<a href="javascript:toggleLeftMenu('Left${currMdRid}_ALL');" class="side_control"><img src="/images/blue/sidetree_open.gif"></a>
			<a href="javascript:goAllSearch('${currMdRid}', '${resultSum}')" title="<u:msg titleId="cm.option.all" alt="전체"/>" class="menu${currMdRid==mdRid and empty mdBxId ? '_open' : ''}"><u:msg titleId="cm.option.all" alt="전체"/></a>
			</li><u:convert srcId="${currMdRid}CountList" var="menuCountList" />
		<c:forEach
			items="${menuCountList}" var="shSrchVo" varStatus="status2">
		<li id="Left${currMdRid}_${shSrchVo.mdBxId}">
			<a href="javascript:toggleLeftMenu('Left${currMdRid}_${shSrchVo.mdBxId}');" class="side_control"><img src="/images/blue/sidetree_open.gif"></a>
			<a href="javascript:goBxSearch('${currMdRid}', '${shSrchVo.mdBxId}')" title="${shSrchVo.mdBxNm}" class="menu${currMdRid==mdRid and mdBxId==shSrchVo.mdBxId ? '_open' : ''}">${shSrchVo.mdBxNm} (${shSrchVo.cnt})</a>
		</li></c:forEach>
		</ul>
	</dd></c:if>
	</c:forEach>
	</dl>
	</div>