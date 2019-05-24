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
$(document).ready(function() {
	$gLeftMnuArea = $("#leftMenuArea");
	initLeftMenu('Left${param.menuId}');
	
	//$('a.topmu_10').attr('class', 'topmu_10on');
});
//-->
</script>

	<div class="titlebg">
		<div class="title ellipsis" title="${ctEstbBVo.ctNm}">${ctEstbBVo.ctNm}</div>
		
		<div class="s_community">
		<ul>
		<li><span class="commutit"><u:msg titleId="ct.cols.mastNm" alt="마스터" /></span> : <span class="commutxt">${ctEstbBVo.mastNm}</span></li>
		<li><span class="commutit"><u:msg titleId="ct.cols.statusOfMembers" alt="회원현황" /></span> : <span class="commutxt"><u:msg titleId="ct.cols.all" alt="전체" /> <a href="javascript:"><span>${allPeople}</span></a>, <u:msg titleId="ct.cols.today" alt="오늘" /> <a href="javascript:"><span>${todayPeople}</span></a></span></li>
		<fmt:parseDate var="dateTempParse" value="${ctEstbBVo.ctApvdDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
		<li><span class="commutit"><u:msg titleId="ct.cols.setup.day" alt="설립일" /></span> : <span class="commutxt"><fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/></span></li>
		<li><span class="commutit"><u:msg titleId="ct.cols.attFileOver" alt="첨부용량(MB)" /></span> : <span class="commutxt">${fileSum} /<c:choose>
				<c:when test="${ctEstbBVo.attLimSize == '-1'}"><u:msg titleId="ct.cols.attFileUnlim" alt="무제한" /></c:when>
				<c:otherwise>${ctEstbBVo.attLimSize}MB</c:otherwise>
				</c:choose></span>
		</li>
		</ul>
		</div>
	</div>
	
	<div>
	<dl id="leftMenuArea">
		<c:forEach items="${ctFncList}" var="ctFncList" varStatus="status">
			<c:if test="${ctFncList.ctFncLocStep == '1' && ctFncList.ctFncTyp == '2'}">
				<dd id="Left${ctFncList.ctFncUid}" class="sidemenuno"><a href="${ctFncList.ctFncUrl}${ctFncList.ctFncUid}&ctId=${ctFncList.ctId}" class="sidetxt">${ctFncList.ctFncNm}</a></dd>
			</c:if>
			<c:if test="${ctFncList.ctFncTyp == '3'}">
				<dd id="Left${ctFncList.ctFncUid}" class="sidemenu"><a href="javascript:toggleLeftMenu('Left${ctFncList.ctFncUid}');" class="sidetxt">${ctFncList.ctFncNm}</a></dd>
			</c:if>
			<dd id="subLeft${ctFncList.ctFncUid}" class="sidetree" style="overflow:auto; display:none;">
			
				<c:forEach items="${ctFncChild}" var="ctFncChild" varStatus="stat">
					<c:if test="${ctFncChild.ctFncLocStep=='2' && ctFncList.ctFncUid == ctFncChild.ctFncPid}">
						<ul class="sidetree_menu">
						<li id="Left${ctFncChild.ctFncUid}">
							<a href="javascript:toggleLeftMenu('Left${ctFncChild.ctFncUid}');" class="side_control"><img src="/images/blue/sidetree_open.gif"></a>
							<a href="${ctFncChild.ctFncUrl}${ctFncChild.ctFncUid}&ctId=${ctFncChild.ctId}" title="게시판" class="menu">${ctFncChild.ctFncNm}</a>
						</li>
						</ul>
					</c:if>
				</c:forEach>
			</dd>
			
		</c:forEach>

	</dl>
	</div>