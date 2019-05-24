<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.net.URLEncoder"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
var gApvLnGrpTypCd="${param.apvLnGrpTypCd}";<%
// 사용자 정보 관리 항목 %>
var gAttrs =	["apvrUid",  "apvrNm",  "apvrPositNm",  "apvrTitleNm",  "apvDeptId",   "apvDeptNm",     "apvrRoleCd", "dblApvTypCd","apvrDeptYn","apvDeptAbbrNm","absRsonCd","absRsonNm","apvStatCd", "${param.fixdApvrYn eq 'Y' ? 'fixdApvrYn' : ''}"];
var gRefAttrs =	["refVwrUid","refVwrNm","refVwrPositNm","refVwrTitleNm","refVwrDeptId","refVwrDeptNm"]<%
// 선택된 사용자 목록 리턴 - uncheck:리턴할 때 선택을 해제함 %>
function getSelectedUsers(uncheck){
	var arr = [], $me, obj;
	$("#listApvLnGrpForm input[name='apvLnGrp']:checked").each(function(){
		obj = {};
		$me = $(this);
		if($me.is(":disabled")) return;
		if(uncheck){
			$(this).checkInput(false);
		}
		if(gApvLnGrpTypCd=='prvRef' || gApvLnGrpTypCd=='pubRef'){
			gRefAttrs.each(function(index, attr){
				obj[attr] = $me.attr("data-"+attr);
			});
		} else {
			gAttrs.each(function(index, attr){
				if(attr!=''){
					obj[attr] = $me.attr("data-"+attr);
				}
			});
		}
		arr.push(obj);
	});
	return arr.length==0 ? null : arr;
}<%
// 체크 해제%>
function deselectUsers(){
	$("#listApvLnGrpForm input:visible:checked").each(function(){
		$(this).checkInput(false);
	});
}<%
// 체크박스 클릭 %>
function clickUserCheck(va){
	if(parent.clickUserCheck) parent.clickUserCheck(va);
}<%
// 경로 저장 버튼 클릭 - parent 에서 호출 %>
function saveApvLnGrpDetl(arr){
	if(arr==null || arr.length==0) return;
	var apvLnGrpId = '${param.apvLnGrpId}';
	if(apvLnGrpId==''){
		alertMsg('ap.msg.saveAfterCreateApvLnGrp');<%// ap.msg.saveAfterCreateApvLnGrp=경로그룹 추가 후 저장하셔야 합니다.%>
		return;
	}
	var $form = $("#apvLnGrpDetlForm");
	$form.html("");
	$form.append("<input name='menuId' type='hidden' value='${param.menuId}' />");
	$form.append("<input name='bxId' type='hidden' value='${param.bxId}' />");<c:forEach
	items="${arrMnuParam}" var="mnuParam">
	$form.append("<input name='${mnuParam[0]}' type='hidden' value='${mnuParam[1]}' />");</c:forEach>
	$form.append("<input name='apvLnGrpId' type='hidden' value='${param.apvLnGrpId}' />");
	$form.append("<input name='apvLnGrpTypCd' type='hidden' value='${empty param.apvLnGrpTypCd ? 'prv' : param.apvLnGrpTypCd}' />");
	arr.each(function(index, data){
		if(gApvLnGrpTypCd=='prvRef' || gApvLnGrpTypCd=='pubRef') data = toApvLnFormat(data);
		$form.append("<input name='apvLn' type='hidden' value='"+escapeValue(JSON.stringify(data))+"' />");
	});
	$form.attr('action', './transApvLnGrpDetl.do');
	$form.attr('target', 'dataframeForFrame');
	$form.attr('method','post');
	$form.submit();
}<%
// 참조열람 형태에서 - 결재선 형태로 - 전환 %>
function toApvLnFormat(data){
	var obj = {};
	for(var i=0; i<gRefAttrs.length; i++) obj[gAttrs[i]] = data[gRefAttrs[i]]==null ? "" : data[gRefAttrs[i]];
	return obj;
}<%
//onload 시  %>
$(document).ready(function() {
	setUniformCSS();
	var initAct = '${param.initAct}';
	if(initAct=='auto'){
		parent.setAutoApvLnGrp();
	}
});
-->
</script>

<div style="padding:9px 9px 2px 9px;">
<form id="listApvLnGrpForm">
<u:listArea noBottomBlank="true" colgroup="${not(param.apvLnGrpTypCd eq 'prvRef' or param.apvLnGrpTypCd eq 'pubRef') ? '4%,28%,16%,16%,16%,20%' : '4%,30%,22%,22%,22%'}">
	<tr id="titleTr"><td width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all'
		/>" onclick="checkAllCheckbox('listApvLnGrpForm', this.checked);" value=""<c:if test="${fn:length(apOngdApvLnDVoList)>0}"> checked="checked"</c:if> /></td>
		<td class="head_ct"><u:msg titleId="cols.dept" alt="부서" /></td>
		<td class="head_ct"><u:msg titleId="cols.user" alt="사용자" /></td>
		<td class="head_ct"><u:term termId="or.term.posit" alt="직위" /></td>
		<td class="head_ct"><u:term termId="or.term.title" alt="직책" /></td><c:if
			test="${not(param.apvLnGrpTypCd eq 'prvRef' or param.apvLnGrpTypCd eq 'pubRef')}">
		<td class="head_ct"><u:msg titleId="ap.jsp.apvTyp" alt="결재구분" /></td></c:if>
	</tr>
<c:forEach items="${apOngdApvLnDVoList}" var="apOngdApvLnDVo" varStatus="status">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
		<td class="bodybg_ct"><input type="checkbox" name="apvLnGrp"<c:if
				test="${apOngdApvLnDVo.apvStatCd ne 'apvd'}"> checked="checked"</c:if><c:if
				test="${apOngdApvLnDVo.apvStatCd eq 'apvd'}"> disabled="disabled"</c:if>
			value="${apOngdApvLnDVo.apvrDeptYn ne 'Y' ? apOngdApvLnDVo.apvrUid : apOngdApvLnDVo.apvDeptId}"<c:if
				test="${not(param.apvLnGrpTypCd eq 'prvRef' or param.apvLnGrpTypCd eq 'pubRef')}">
			data-apvrRoleCd="${apOngdApvLnDVo.apvrRoleCd}"
			data-dblApvTypCd="${apOngdApvLnDVo.dblApvTypCd}"
			data-apvrDeptYn="${apOngdApvLnDVo.apvrDeptYn}"
			data-apvrUid="${apOngdApvLnDVo.apvrUid}"
			data-apvrNm="<u:out value="${apOngdApvLnDVo.apvrNm}" type="value" />"
			data-apvrPositNm="<u:out value="${apOngdApvLnDVo.apvrPositNm}" type="value" />"
			data-apvrTitleNm="<u:out value="${apOngdApvLnDVo.apvrTitleNm}" type="value" />"
			data-apvDeptId="${apOngdApvLnDVo.apvDeptId}"
			data-apvDeptNm="<u:out value="${apOngdApvLnDVo.apvDeptNm}" type="value" />"
			data-apvDeptAbbrNm="<u:out value="${apOngdApvLnDVo.apvDeptAbbrNm}" type="value" />"
			data-absRsonCd="<u:out value="${apOngdApvLnDVo.absRsonCd}" type="value" />"
			data-absRsonNm="<u:out value="${apOngdApvLnDVo.absRsonNm}" type="value" />"
			data-apvStatCd=""</c:if><c:if
				test="${param.fixdApvrYn eq 'Y'}">
			data-fixdApvrYn="<u:out value="${apOngdApvLnDVo.fixdApvrYn}" type="value" />"</c:if><c:if
				test="${param.apvLnGrpTypCd eq 'prvRef' or param.apvLnGrpTypCd eq 'pubRef'}">
			data-refVwrUid="${apOngdApvLnDVo.apvrUid}"
			data-refVwrNm="<u:out value="${apOngdApvLnDVo.apvrNm}" type="value" />"
			data-refVwrPositNm="<u:out value="${apOngdApvLnDVo.apvrPositNm}" type="value" />"
			data-refVwrTitleNm="<u:out value="${apOngdApvLnDVo.apvrTitleNm}" type="value" />"
			data-refVwrDeptId="${apOngdApvLnDVo.apvDeptId}"
			data-refVwrDeptNm="<u:out value="${apOngdApvLnDVo.apvDeptNm}" type="value" />"</c:if>
			/></td>
		<td class="body_ct"><u:out value="${apOngdApvLnDVo.apvDeptNm}" /></td>
		<td class="body_ct"><c:if
			test="${not empty apOngdApvLnDVo.apvrUid}"
			><a href="javascript:parent.viewUserPop('${apOngdApvLnDVo.apvrUid}');"><u:out value="${apOngdApvLnDVo.apvrNm}" /></a></c:if><c:if
			test="${empty apOngdApvLnDVo.apvrUid}"
			><u:out value="${apOngdApvLnDVo.apvrNm}" /></c:if></td>
		<td class="body_ct"><u:out value="${apOngdApvLnDVo.apvrPositNm}" /></td>
		<td class="body_ct"><u:out value="${apOngdApvLnDVo.apvrTitleNm}" /></td><c:if
			test="${not(param.apvLnGrpTypCd eq 'prvRef' or param.apvLnGrpTypCd eq 'pubRef')}">
		<td class="body_ct" id="view"><c:if
			test="${not empty apOngdApvLnDVo.dblApvTypCd and apOngdApvLnDVo.apvrRoleCd != 'prcDept'}"><u:term
			termId="ap.term.${apOngdApvLnDVo.dblApvTypCd}" /> / </c:if><c:if
			test="${not empty apOngdApvLnDVo.apvrRoleCd}"><u:term
			termId="ap.term.${apOngdApvLnDVo.apvrRoleCd=='byOneAgr' ? 'byOne' : apOngdApvLnDVo.apvrRoleCd}"
			/></c:if><c:if
			test="${apOngdApvLnDVo.apvrRoleCd == 'abs' and not empty apOngdApvLnDVo.absRsonNm}"> [${apOngdApvLnDVo.absRsonNm}]</c:if></td></c:if>
	</tr>
</c:forEach>
</u:listArea>
</form>
<form id="apvLnGrpDetlForm"></form>
</div>