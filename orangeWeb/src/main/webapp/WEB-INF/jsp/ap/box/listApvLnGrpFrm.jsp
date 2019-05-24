<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.net.URLEncoder"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
/* 
ap.jsp.prvApvLnGrp=개인경로그룹
ap.jsp.pubApvLnGrp=공용경로그룹
ap.jsp.prvRefVwGrp=개인참조그룹
ap.jsp.pubRefVwGrp=공용참조그룹
 */
String apvLnGrpTypCd = request.getParameter("apvLnGrpTypCd");
String popTitleId = "prv".equals(apvLnGrpTypCd) ? "ap.jsp.prvApvLnGrp"
		: "pub".equals(apvLnGrpTypCd) ? "ap.jsp.pubApvLnGrp"
				: "prvRef".equals(apvLnGrpTypCd) ? "ap.jsp.prvRefVwGrp"
						: "pubRef".equals(apvLnGrpTypCd) ? "ap.jsp.pubRefVwGrp"
								: "ap.jsp.prvApvLnGrp";
request.setAttribute("popTitleId", popTitleId);

%>
<script type="text/javascript">
<!--<%
// 경로그룹 클릭 - 하일라이트 처리, 왼쪽 프레임 리로드 함수 호출 %>
function clickApvLnGrp(apvLnGrpId){
	if(apvLnGrpId!=''){
		var $area = $("#apvLnGrpListArea");
		$area.find(".groupsmu_text_on").removeClass("groupsmu_text_on");
		$area.find("#"+apvLnGrpId).addClass("groupsmu_text_on");
	}
	parent.${param.apvLnGrpTypCd != 'prvRef' and param.apvLnGrpTypCd != 'pubRef' ? 'clickApvLnGrp' : 'clickRefVwGrp'}(apvLnGrpId, '${param.apvLnGrpTypCd}');
}<%
// 경로그룹 추가/수정/삭제 %>
function mngApvLnGrp(act){
	var apvLnGrpTypCd = "${empty param.apvLnGrpTypCd ? 'prv' : param.apvLnGrpTypCd}";
	var popTitle = '<u:msg titleId="${popTitleId}" alt="개인경로그룹/공용경로그룹/개인참조그룹/공용참조그룹" />';
	if(act=='reg'){
		parent.dialog.open('setApvLnGrpDialog', popTitle+' - <u:msg titleId="cm.btn.add" alt="추가" />', "./setApvLnGrpPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvLnGrpTypCd="+apvLnGrpTypCd);
	} else {
		var apvLnGrpId = $("#apvLnGrpListArea .groupsmu_text_on").attr("id");
		if(apvLnGrpId==null){
			alertMsg('cm.msg.noSelect');<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
			return;
		}
		if(act=='mod'){
			parent.dialog.open('setApvLnGrpDialog', popTitle+' - <u:msg titleId="cm.btn.chg" alt="변경" />', "./setApvLnGrpPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvLnGrpTypCd="+apvLnGrpTypCd+"&apvLnGrpId="+apvLnGrpId);
		} else if(act=='del'){
			if(!confirmMsg('cm.cfrm.del')) return;<% // cm.cfrm.del=삭제하시겠습니까 ? %>
			callAjax("./transApvLnGrpDelAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {apvLnGrpTypCd:apvLnGrpTypCd, apvLnGrpId:apvLnGrpId}, function(data){
				if(data.message != null) alert(data.message);
				if(data.result == 'ok'){
					var url = location.href;
					var p = url.indexOf('&apvLnGrpId');
					if(p>0) url = url.substring(0,p);
					location.replace(url);
				}
			});
		}
	}
}<%
// 순서 조정 %>
function movePosition(apvLnGrpId, direction){
	var apvLnGrpTypCd = "${empty param.apvLnGrpTypCd ? 'prv' : param.apvLnGrpTypCd}";
	callAjax("./transApvLnGrpMoveAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {apvLnGrpTypCd:apvLnGrpTypCd, apvLnGrpId:apvLnGrpId, direction:direction}, function(data){
		if(data.message != null) alert(data.message);
		if(data.result == 'ok'){
			location.replace('./listApvLnGrpFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvLnGrpTypCd='+apvLnGrpTypCd+'&apvLnGrpId='+apvLnGrpId);
		}
	});
}<%
// onload 시  %>
$(document).ready(function() {
	var noInit = '${param.noInit}';
	if(noInit!='Y'){
		clickApvLnGrp('${empty param.apvLnGrpId ? apApvLnGrpBVoList[0].apvLnGrpId : param.apvLnGrpId}');
	}
});
-->
</script>

<div style="padding:8px 9px 0px 9px;">
<div><c:if
	test="${param.apvLnGrpTypCd eq 'pub' }">
<u:title titleId="ap.jsp.pub" alt="공용" type="small" >
	<u:titleButton titleId="cm.btn.add" alt="추가" href="javascript:mngApvLnGrp('reg');" auth="S"
	/><u:titleButton titleId="cm.btn.chg" alt="변경" href="javascript:mngApvLnGrp('mod');" auth="S"
	/><u:titleButton titleId="cm.btn.del" alt="삭제" href="javascript:mngApvLnGrp('del');" auth="S"
	/><u:titleButton titleId="ap.jsp.prv" alt="개인" href="./listApvLnGrpFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvLnGrpTypCd=prv"
	/></u:title>
</c:if><c:if
	test="${param.apvLnGrpTypCd eq 'prv' }">
<u:title titleId="ap.jsp.prv" alt="개인" type="small" >
	<u:titleButton titleId="cm.btn.add" alt="추가" href="javascript:mngApvLnGrp('reg');"
	/><u:titleButton titleId="cm.btn.chg" alt="변경" href="javascript:mngApvLnGrp('mod');"
	/><u:titleButton titleId="cm.btn.del" alt="삭제" href="javascript:mngApvLnGrp('del');"
	/><u:titleButton titleId="ap.jsp.pub" alt="공용" href="./listApvLnGrpFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvLnGrpTypCd=pub"
	/></u:title>
</c:if><c:if
	test="${param.apvLnGrpTypCd eq 'pubRef' }">
<u:title titleId="ap.jsp.pub" alt="공용" type="small" >
	<u:titleButton titleId="cm.btn.add" alt="추가" href="javascript:mngApvLnGrp('reg');" auth="S"
	/><u:titleButton titleId="cm.btn.chg" alt="변경" href="javascript:mngApvLnGrp('mod');" auth="S"
	/><u:titleButton titleId="cm.btn.del" alt="삭제" href="javascript:mngApvLnGrp('del');" auth="S"
	/><u:titleButton titleId="ap.jsp.prv" alt="개인" href="./listApvLnGrpFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvLnGrpTypCd=prvRef"
	/></u:title>
</c:if><c:if
	test="${param.apvLnGrpTypCd eq 'prvRef' }">
<u:title titleId="ap.jsp.prv" alt="개인" type="small" >
	<u:titleButton titleId="cm.btn.add" alt="추가" href="javascript:mngApvLnGrp('reg');"
	/><u:titleButton titleId="cm.btn.chg" alt="변경" href="javascript:mngApvLnGrp('mod');"
	/><u:titleButton titleId="cm.btn.del" alt="삭제" href="javascript:mngApvLnGrp('del');"
	/><u:titleButton titleId="ap.jsp.pub" alt="공용" href="./listApvLnGrpFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvLnGrpTypCd=pubRef"
	/></u:title>
</c:if>

<u:listArea id="apvLnGrpListArea" noBottomBlank="${true}"
	style="max-height:176px; overflow-x:hidden; overflow-y:${fn:length(apApvLnGrpBVoList)<8 ? 'hidden' : 'auto'};"
	 ><c:forEach
	items="${apApvLnGrpBVoList}" var="apApvLnGrpBVo" varStatus="status">
	<tr>
	<td class="body_lt" style="padding:0px 0px 0px 0px;">
		<table border="0" cellpadding="0" cellspacing="0" style="width:100%;"><tbody><tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
		<td id="${apApvLnGrpBVo.apvLnGrpId}" class="${empty param.apvLnGrpId ? (status.first ? 'groupsmu_text_on' : '') : (param.apvLnGrpId == apApvLnGrpBVo.apvLnGrpId ? 'groupsmu_text_on' : '')}" style="padding:3px 0px 0px 5px;"><div class="ellipsis" title="<c:if
			test="${param.apvLnGrpTypCd eq 'pub' or param.apvLnGrpTypCd eq 'pubRef'}"
				><u:out value="${apApvLnGrpBVo.rescNm}" type="value" /></c:if><c:if
			test="${not(param.apvLnGrpTypCd eq 'pub' or param.apvLnGrpTypCd eq 'pubRef')}"><u:out value="${apApvLnGrpBVo.apvLnGrpNm}" type="value" /></c:if>"><a href="javascript:clickApvLnGrp('${apApvLnGrpBVo.apvLnGrpId}');"><c:if
			test="${param.apvLnGrpTypCd eq 'pub' or param.apvLnGrpTypCd eq 'pubRef'}"
				><u:out value="${apApvLnGrpBVo.rescNm}" /></c:if><c:if
			test="${not(param.apvLnGrpTypCd eq 'pub' or param.apvLnGrpTypCd eq 'pubRef')}"><u:out value="${apApvLnGrpBVo.apvLnGrpNm}" /></c:if></a></div></td>
		<c:if test="${not(param.apvLnGrpTypCd eq 'pub' or param.apvLnGrpTypCd eq 'pubRef') or (_AUTH=='S' or _AUTH=='SYS')}">
		<td align="left" style="width:50px; padding:2px 0px 2px 0px;"
			><a href="javascript:movePosition('${apApvLnGrpBVo.apvLnGrpId}', 'up')" style="margin-right:5px;"><img src="/images/blue/ico_wup.png" width="20" height="20" title="<u:msg alt="위로이동" titleId="cm.btn.up" />" 
			/></a><a href="javascript:movePosition('${apApvLnGrpBVo.apvLnGrpId}', 'down')"><img src="/images/blue/ico_wdown.png" width="20" height="20" title="<u:msg alt="아래로이동" titleId="cm.btn.down" />"
			/></a></td></c:if>
		</tr></tbody></table>
		</td>
	</tr>
	</c:forEach>
</u:listArea>
</div>
</div>