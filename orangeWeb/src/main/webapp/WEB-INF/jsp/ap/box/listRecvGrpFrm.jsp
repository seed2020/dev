<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.net.URLEncoder"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// 경로그룹 클릭 - 하일라이트 처리, 왼쪽 프레임 리로드 함수 호출 %>
function changeRecvGrpId(recvGrpId){
	location.replace('./listRecvGrpFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&mode=${param.mode}&recvGrpId='+recvGrpId);
}<%
// 경로그룹 추가/수정/삭제 %>
function mngRecvGrp(act){
	var popTitle = '<u:msg titleId="${param.mode=='pub' ? 'ap.jsp.pubRecvGrp' : 'ap.jsp.prvRecvGrp'}" alt="개인수신그룹/공용수신그룹" />';
	if(act=='reg'){
		parent.dialog.open('setRecvGrpDialog', popTitle+' - <u:msg titleId="cm.btn.add" alt="추가" />', "./setRecvGrpPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&mode=${param.mode=='pub' ? 'pub' : 'prv'}");
	} else {
		var recvGrpId = $("#recvGrpId").val();
		if(recvGrpId==null){<%
			// cm.msg.noSelect=선택한 항목이 없습니다. %>
			alertMsg('cm.msg.noSelect');
			return;
		}
		if(act=='mod'){
			parent.dialog.open('setRecvGrpDialog', popTitle+' - <u:msg titleId="cm.btn.chg" alt="변경" />', "./setRecvGrpPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&mode=${param.mode=='pub' ? 'pub' : 'prv'}&recvGrpId="+recvGrpId);
		} else if(act=='del'){<%
			// cm.cfrm.del=삭제하시겠습니까 ? %>
			if(!confirmMsg('cm.cfrm.del')) return;
			callAjax("./transRecvGrpDelAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {mode:"${param.mode}", recvGrpId:recvGrpId}, function(data){
				if(data.message != null) alert(data.message);
				if(data.result == 'ok'){
					var url = location.href;
					var p = url.indexOf('&recvGrpId');
					if(p>0) url = url.substring(0,p);
					location.replace(url);
				}
			});
		}
	}
}<%
// 순서 조정 %>
function movePosition(recvGrpId, direction){
	callAjax("./transRecvGrpMoveAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {mode:"${param.mode}", recvGrpId:recvGrpId, direction:direction}, function(data){
		if(data.message != null) alert(data.message);
		if(data.result == 'ok'){
			location.replace('./listRecvGrpFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&mode=${param.mode}&recvGrpId='+recvGrpId);
		}
	});
}<%
// 수신그룹 저장 %>
function saveRecvGrpDetl(arr){
	var recvGrpId = $("#recvGrpId").val();
	if(recvGrpId==null || recvGrpId==''){<%
		// ap.msg.noRecvGrpSelected=선택된 수신그룹이 업습니다. %>
		alertMsg('ap.msg.noRecvGrpSelected');
		return;
	}
	if(arr==null || arr.length==0){
		return;
	}
	var $form = $("#recvGrpDetlForm");
	$form.html("");
	$form.append("<input name='menuId' type='hidden' value='${param.menuId}' />");
	$form.append("<input name='bxId' type='hidden' value='${param.bxId}' />");<c:forEach
		items="${arrMnuParam}" var="mnuParam">
		$form.append("<input name='${mnuParam[0]}' type='hidden' value='${mnuParam[1]}' />");</c:forEach>
	$form.append("<input name='recvGrpId' type='hidden' value='"+recvGrpId+"' />");
	$form.append("<input name='mode' type='hidden' value='${param.mode=='pub' ? 'pub' : 'prv'}' />");
	arr.each(function(index, data){
		$form.append("<input name='recvDept' type='hidden' value='"+escapeValue(JSON.stringify(data))+"' />");
	});
	$form.attr('action', './transRecvGrpDetl.do');
	$form.attr('target', 'dataframeForFrame');
	$form.attr('method','post');
	$form.submit();
}<%
// 수신처 데이터 모으기 %>
function collectRecvDept(recvDeptAttrNms){
	var arr = [], obj, $check, va;
	$("#recvDeptDataPopArea input[id!='checkHeader']:checked").each(function(){
		$check = $(this);
		obj = {};
		recvDeptAttrNms.each(function(idx, name){
			va = $check.attr("data-"+name);
			obj[name] = va==null ? '' : va;
		});
		arr.push(obj);
	});
	return arr;
}<%
// 전체 선택 해제 %>
function deSelectNodes(){
	$("input[type='checkbox']").each(function(){
		$(this).checkInput(false);
	});
}<%
// 맨위로 버튼 - 순서조절 - 처음으로 %>
function setFirstGrp(){
	var recvGrpId = $("#recvGrpId").val();
	if(recvGrpId==null || recvGrpId==''){<%
		// ap.msg.noRecvGrpSelected=선택된 수신그룹이 업습니다. %>
		alertMsg('ap.msg.noRecvGrpSelected');
		return;
	}
	callAjax("./transRecvGrpMoveAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {mode:"${param.mode}", recvGrpId:recvGrpId, direction:'first'}, function(data){
		if(data.message != null) alert(data.message);
		if(data.result == 'ok'){
			location.replace('./listRecvGrpFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&mode=${param.mode}&recvGrpId='+recvGrpId);
		}
	});
}<%
// onload 시  %>
$(document).ready(function() {<%
	// 유니폼 적용 %>
	setUniformCSS();
	parent.toggleSaveRecvGrpBtn("${param.mode != 'pub' or (_AUTH=='S' or _AUTH=='SYS') ? 'prv' : 'pub'}");
});
-->
</script>

<div style="padding:8px 9px 0px 9px;">
<div>
<c:if test="${param.mode == 'pub' }">
<u:title titleId="ap.jsp.pub" alt="공용" type="small" >
	<u:titleButton titleId="cm.btn.add" alt="추가" href="javascript:mngRecvGrp('reg');" auth="S"
	/><u:titleButton titleId="cm.btn.chg" alt="변경" href="javascript:mngRecvGrp('mod');" auth="S"
	/><u:titleButton titleId="cm.btn.del" alt="삭제" href="javascript:mngRecvGrp('del');" auth="S"
	/><u:titleButton titleId="ap.jsp.prv" alt="개인" href="./listRecvGrpFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&mode=prv"
	/></u:title>
</c:if>
<c:if test="${param.mode != 'pub' }">
<u:title titleId="ap.jsp.prv" alt="개인" type="small" >
	<u:titleButton titleId="cm.btn.add" alt="추가" href="javascript:mngRecvGrp('reg');"
	/><u:titleButton titleId="cm.btn.chg" alt="변경" href="javascript:mngRecvGrp('mod');"
	/><u:titleButton titleId="cm.btn.del" alt="삭제" href="javascript:mngRecvGrp('del');"
	/><u:titleButton titleId="ap.jsp.pub" alt="공용" href="./listRecvGrpFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&mode=pub"
	/></u:title>
</c:if>

<u:listArea colgroup="25%,75%">
	<tr>
		<td class="head_ct"><u:msg alt="수신그룹" titleId="ap.jsp.recvGrp" /></td>
		<td>
			<table border="0" cellpadding="0" cellspacing="0" style="width:100%;"><tbody><tr>
			<td><select id="recvGrpId" name="recvGrpId" onchange="changeRecvGrpId(this.value);" style="max-width:${browser.ie ? '170' : '180'}px"><c:forEach
				items="${apRecvGrpBVoList}" var="apRecvGrpBVo" varStatus="status">
				<option value="${apRecvGrpBVo.recvGrpId}" ${apRecvGrpBVo.recvGrpId == param.recvGrpId ? 'selected="selected"' : ''
					}><u:out value="${param.mode == 'pub' ? apRecvGrpBVo.rescNm : apRecvGrpBVo.recvGrpNm}" /></option></c:forEach>
			</select></td>
			<td style="padding:0px 3px 0px 5px; text-align:right"><u:buttonS alt="맨위로" titleId="ap.msg.toTop" href="javascript:setFirstGrp();" /></td>
			</tr></tbody></table>
		</td>
	</tr>
</u:listArea>

<u:listArea id="recvDeptDataPopArea" colgroup="'4%,37%,37%,22%'"
	style="max-height:270px; overflow-x:hidden; overflow-y:auto;"
	noBottomBlank="${true}">
	<tr id="titleTr"><td class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all'
		/>" onclick="checkAllCheckbox('recvDeptDataPopArea', this.checked);" value=""/></td>
		<td class="head_ct"><u:msg titleId="cols.recvDept" alt="수신처" /></td>
		<td class="head_ct"><u:msg titleId="cols.refDept" alt="참조처" /></td>
		<td class="head_ct"><u:msg titleId="ap.jsp.recvDept.recvTyp" alt="수신구분" /></td>
	</tr>
<c:forEach items="${apOngdRecvDeptLVoList}" var="apOngdRecvDeptLVo" varStatus="status">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
		<td class="bodybg_ct"><input type="checkbox" name="apvLnCheck"<c:if
				test="${apOngdRecvDeptLVo.sendYn=='Y'}"> disabled="disabled"</c:if>
			data-recvDeptSeq="${apOngdRecvDeptLVo.recvDeptSeq}"
			data-recvDeptTypCd="${apOngdRecvDeptLVo.recvDeptTypCd}"
			data-recvDeptTypNm="${apOngdRecvDeptLVo.recvDeptTypNm}"
			data-recvDeptId="${apOngdRecvDeptLVo.recvDeptId}"
			data-recvDeptNm="<u:out value="${apOngdRecvDeptLVo.recvDeptNm}" type="value" />"
			data-refDeptId="${apOngdRecvDeptLVo.refDeptId}"
			data-refDeptNm="<u:out value="${apOngdRecvDeptLVo.refDeptNm}" type="value" />"
			data-sendYn=""
			/></td>
		<td class="body_ct"><div class="ellipsis" title="<u:out value="${apOngdRecvDeptLVo.recvDeptNm}" type="value"
			/>"><u:out value="${apOngdRecvDeptLVo.recvDeptNm}" /></div></td>
		<td class="body_ct"><div class="ellipsis" title="<u:out value="${apOngdRecvDeptLVo.refDeptNm}" type="value"
			/>"><u:out value="${apOngdRecvDeptLVo.refDeptNm}" /></div></td>
		<td class="body_ct"><div class="ellipsis" title="<u:out value="${apOngdRecvDeptLVo.recvDeptTypNm}" type="value"
			/>"><u:out value="${apOngdRecvDeptLVo.recvDeptTypNm}" /></div></td>
	</tr>
</c:forEach>
</u:listArea>

</div>
<form id="recvGrpDetlForm" style="display:none;"></form>
</div>