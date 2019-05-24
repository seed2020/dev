<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%// 현재 선택된 그룹ID %>
var gSelected = {typCd:'${authGrpTypCd}', catCd:null, grpId:null};
<%// 선택된 그룹 리턴 - parent에서 호출 (수정등의 경우) %>
function getSeletedItem(){
	return {typCd:gSelected.typCd, catCd:gSelected.catCd, grpId:gSelected.grpId};
}
<%// [아이콘] 위로이동, 아래로이동 %>
function move(direction){
	if(gSelected.grpId==null){
		alertMsg('cm.msg.noSelect');<%//선택한 항목이 없습니다.%>
	} else if(direction=='up' && $('#grp'+gSelected.grpId).parent().prev().length==0){
		alertMsg('cm.msg.move.first.up');<%//맨 위의 항목 입니다.%>
	} else if(direction=='down' && $('#grp'+gSelected.grpId).parent().next().length==0){
		alertMsg('cm.msg.move.last.down');<%//맨 아래의 항목 입니다.%>
	} else {
		callAjax('./transAuthGrpMoveAjx.do?menuId=${menuId}', {compId:'${compId}', authGrpTypCd:gSelected.typCd, authGrpCatCd:gSelected.catCd, authGrpId:gSelected.grpId, direction:direction}, function(data){
			if(data.message!=null){
				alert(data.message);
			} else {
				reloadSelf(gSelected.typCd, gSelected.catCd, gSelected.grpId);
			}
		});
	}
}
<%// 카테고리 열고 닫음 - 카테고리명, 아이콘 클릭 %>
function toggleCat(catCd){
	var $img = getForm().find("#catImg"+catCd);
	if($img.length>0 && $img.attr("src").indexOf("group_close")>0){
		$img.attr("src", "${_cxPth}/images/${_skin}/ico_group_open.png");
		getForm().find("#catTab"+catCd).show();
	} else {
		$img.attr("src", "${_cxPth}/images/${_skin}/ico_group_close.png");
		getForm().find("#catTab"+catCd).hide();
	}
	gSelectedCatCd = catCd;
}
<%// 선택된 카테고리 리턴 - parent에서 호출 (등록의 경우)%>
var gSelectedCatCd = null;
function getSelectedCatCd(){
	return gSelectedCatCd;
}
<%// 권한그룹선택 - 권한그룹 클릭 %>
function selectItem(catCd, grpId){
	if(gSelected.grpId!=grpId){
		if(parent.saveChangedDetl()){<%// 상세설정의 변경사항이 있으면 저장할것인지 체크 %>
			return;
		}
		toggleItem(gSelected.grpId, false);
		toggleItem(grpId, true);
		gSelected.catCd = catCd;
		gSelected.grpId = grpId;
		gSelectedCatCd = catCd;
	}
	// parent 함수 호출
	parent.onAuthGrpClick(getSeletedItem());
}
<%// 선택됨 UI상 표시 - 파란색으로 %>
function toggleItem(grpId, on){
	if(grpId==null) return;
	var $td = getForm().find("#grp"+grpId);
	if($td.attr("data-useYn")=="Y"){<%//사용유무 - 사용%>
		$td.attr("class", on ? "text_on" : "text");
	} else {<%//사용유무 - 사용안함%>
		$td.attr("class", on ? "text_onbg" : "textbg");
	}
}
<%// 리로드 - 그룹 등록,수정 후 parent 에서 호출 %>
function reloadSelf(typCd, catCd, grpId){
	var param = (catCd!=null && grpId!=null) ? '&authGrpCatCd='+catCd+'&authGrpId='+grpId : "";
	location.replace('./listAuthGrpFrm.do?menuId=${menuId}&compId=${compId}&authGrpTypCd='+typCd+param);
}
<%// 권한그룹(사용자그룹) 삭제 - parent 에서 호출 %>
function delAuthGrp(){
	if(gSelected.grpId==null){
		alert('<u:msg titleId="cm.msg.noSelect" alt="선택한 항목이 없습니다."/>');
	} else if(confirmMsg("cm.cfrm.del")) {<%//cm.cfrm.del=삭제하시겠습니까 ?%>
		callAjax('./transAuthGrpDelAjx.do?menuId=${menuId}', {compId:'${compId}', authGrpTypCd:gSelected.typCd, authGrpId:gSelected.grpId}, function(data){
			if(data.message!=null){
				alert(data.message);
			}
			if(data.result=='ok'){
				var $tr = $('#grp'+gSelected.grpId).parent();
				var $next = $tr.prev().prev();
				if($next.length>0){
					var id = $next.find("td:first").attr("id");
					if(id!=null && id.length>3) reloadSelf(gSelected.typCd, gSelected.catCd, id.substring(3));
					else reloadSelf(gSelected.typCd);
				} else if(($next = $tr.next().next()).length>0){
					var id = $next.find("td:first").attr("id");
					if(id!=null && id.length>3) reloadSelf(gSelected.typCd, gSelected.catCd, id.substring(3));
					else reloadSelf(gSelected.typCd);
				} else {
					parent.openDetlFrm(null);<%// 부모창 오른쪽 프레임 지움 %>
					reloadSelf(gSelected.typCd);
				}
			}
		});
	}
}
<%// form 리턴 %>
var gForm = null;
function getForm(){
	if(gForm==null) gForm = $("#authGrpForm");
	return gForm;
}
<c:if test="${not empty param.authGrpId}">
$(document).ready(function() {
	selectItem('${param.authGrpCatCd}','${param.authGrpId}');
});
</c:if>
//-->
</script>

<form id="authGrpForm" style="padding:10px;">

<input type="hidden" name="menuId" value="${menuId}" />
<c:forEach items="${authGrpCatCdList}" var="authGrpCatCd" varStatus="outerStatus">
	<table class="group_mu" border="0" cellpadding="0" cellspacing="0">
	<tbody>
	<tr>
		<td class="group_ico"><a href="javascript:toggleCat('${authGrpCatCd.cd}');"><img id="catImg${authGrpCatCd.cd}" src="${_cxPth}/images/${_skin}/ico_group_open.png" width="24" height="22" /></a></td>
		<td class="text"><a href="javascript:toggleCat('${authGrpCatCd.cd}');"><u:out value="${authGrpCatCd.rescNm}" /></a></td>
	</tr>
	</tbody>
	</table>
	<u:convert srcId="cat_${authGrpCatCd.cd}" var="ptAuthGrpBVoList" />
	<c:if test="${not empty ptAuthGrpBVoList}">
	<table id="catTab${authGrpCatCd.cd}" class="group_smu" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<c:forEach items="${ptAuthGrpBVoList}" var="ptAuthGrpBVo" varStatus="status">
		<tr><td id="grp${ptAuthGrpBVo.authGrpId}" data-useYn="${ptAuthGrpBVo.useYn}" class="text<c:if test="'N' == ptAuthGrpBVo.useYn">bg</c:if>">
			<a href="javascript:selectItem('${ptAuthGrpBVo.authGrpCatCd}','${ptAuthGrpBVo.authGrpId}');"><u:out value="${ptAuthGrpBVo.rescNm}" /></a></td></tr>
		<c:if test="${not status.last}"><tr><td class="line"></td></tr></c:if>
		</c:forEach>
	</tbody>
	</table>
	</c:if>
	<div class="blank_s3"></div>
</c:forEach>

</form>