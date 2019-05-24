<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
function saveOpin(){
	if (validator.validate('setOpinForm')) {
		
		var $form = $("#setOpinForm");
		$form.attr("method", "POST");
		$form.attr("action", "./transSaveOpin.do?menuId=${menuId}&ctId=${ctId}&debrId=${debrId}&opinOrdr=${opinOrdr}");
		editor('opin').prepare();
		$form.submit();
	}
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />
<c:set var="topc" value="화면 개발시 JSTL을 사용하십니까?" />
<u:set test="${fnc == 'mod'}" var="subj" value="VELOCITY가 편하던데요." elseValue="" />
<u:set test="${fnc == 'mod'}" var="cont" value="VELOCITY가 편하던데요. 다른건 안써봐서 모르겠습니다. 화아팅!" elseValue="" />

<div style="width:750px">
<form id="setOpinForm">
<u:input type="hidden" id="menuId" value="${menuId}" />

<% // 폼 필드 %>
<div class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="width:100%;table-layout:fixed;">
		<tr>
			<td width="27%" class="head_lt"><u:msg titleId="cols.topc" alt="주제" /></td>
			<td width="73%" class="body_lt">
				<div class="ellipsis" title="<u:out value="${ctDebrBVo.subj}"/>">
					<u:out value="${ctDebrBVo.subj}"/>
				</div>
			</td>
		</tr>
		
		<tr>
			<td class="head_lt"><u:mandatory /><u:msg titleId="cols.subj" alt="의견" /></td>
			<td><u:input id="opinSubj" name="opinSubj" titleId="cols.subj" style="width:97%;" value="${ctDebrOpinDVo.subj}" mandatory="Y" maxByte="240"/></td>
		</tr>
		
		<tr>
			<td class="head_lt"><u:mandatory /><u:msg titleId="cols.fna" alt="찬반" /></td>
			<td>
				<c:if test="${ctDebrOpinDVo.prosConsCd == null || ctDebrOpinDVo.prosConsCd == 'A'}">
					<c:set var="prosCdA" value="true"/>
				</c:if>
				<c:if test="${ctDebrOpinDVo.prosConsCd == 'O'}">
					<c:set var="prosCdO" value="true"/>
				</c:if>
				<c:if test="${ctDebrOpinDVo.prosConsCd == 'E'}">
					<c:set var="prosCdE" value="true"/>
				</c:if>
				<u:checkArea>
					<u:radio name="fna" value="A" titleId="ct.option.for" alt="찬성" inputClass="bodybg_lt" checked="${prosCdA}"/>
					<u:radio name="fna" value="O" titleId="ct.option.against" alt="반대" inputClass="bodybg_lt" checked="${prosCdO}"/>
					<u:radio name="fna" value="E" titleId="ct.option.etc" alt="기타" inputClass="bodybg_lt" checked="${prosCdE}"/>
				</u:checkArea>
			</td>
		</tr>
	</table>
</div>

<div id="editor1Area" class="listarea" style="width:100%; height:302px; padding-top:2px"></div>
<u:editor id="opin" width="100%" height="300" module="ct" areaId="editor1Area" value="${ctDebrOpinDVo.opin}" />

<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="javascript:saveOpin();" alt="저장" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>
