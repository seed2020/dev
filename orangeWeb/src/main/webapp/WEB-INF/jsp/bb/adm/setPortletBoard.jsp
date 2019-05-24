<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<% // [아이콘:선택추가] 게시판선택 추가 %>
function addBb() {
	$('#rightTbl input[type=checkbox]:checked').each(function() {
		$tr = $(this).parents('tr:first').detach();
		$tr.find('input[type=checkbox]:checked').prop('checked', false).uniform.update();
		$tr.appendTo($('#leftTbl > tbody'));
	});
}
<% // [아이콘:선택삭제] 게시판선택 제거 %>
function removeBb() {
	$('#leftTbl input[type=checkbox]:checked').each(function() {
		$tr = $(this).parents('tr:first').detach();
		$tr.find('input[type=checkbox]:checked').prop('checked', false).uniform.update();
		$tr.appendTo($('#rightTbl > tbody'));
	});
}
<% // [하단버튼:저장] 저장 %>
function saveBb() {
	var brdIds = [];
	$('#leftTbl input[name=brdId]').each(function() {
		brdIds.push($(this).val());
	});
	callAjax('./transPortletBoardAjx.do?menuId=${menuId}', {brdIds:brdIds}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			location.replace(location.href);
		}
	});
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="bb.jsp.setPortletBoard.title" alt="포틀렛게시판 설정" menuNameFirst="true"/>

<form id="setMyBullPop">

<!-- LEFT -->
<div style="float:left; width:47%;">

<u:titleArea outerStyle="height: 300px" innerStyle="width:94%; margin: 0 auto 0 auto; padding: 10px 0 0 0">
	<u:title titleId="bb.jsp.setMyBullPop.choiBbList" type="small" alt="선택된 게시판 목록" />

	<div class="listarea">
		<table class="listtable" border="0" cellpadding="0" cellspacing="1">
		<colgroup>
			<col width="33">
			<col>
		<tbody>
		<tr>
		<td class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('leftTbl', this.checked);" value=""/></td>
		<td class="head_ct"><u:msg titleId="cols.bbNm" alt="게시판명" /></td>
		</tr>
		</tbody></table>

		<div class="listbody" style="height:225px;">
		<table id="leftTbl" class="listtable" border="0" cellpadding="0" cellspacing="1">
		<colgroup>
			<col width="33">
			<col>
		</colgroup>
		<tbody>
	<c:if test="${fn:length(baBrdBVoList) > 0}">
		<c:forEach items="${baBrdBVoList}" var="baBrdBVo" varStatus="status">
		<c:if test="${baBrdBVo.pltYn == 'Y'}">
		<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
		<td class="bodybg_ct"><u:checkbox name="chk" value="${baBrdBVo.brdId}" checked="false" />
			<u:input type="hidden" id="id_${baBrdBVo.brdId}" name="brdId" value="${baBrdBVo.brdId}" />
			<u:input type="hidden" id="nm_${baBrdBVo.brdId}" name="rescNm" value="${baBrdBVo.rescNm}" />
			</td>
		<td class="body_ct"><label for="chk${baBrdBVo.brdId}">${baBrdBVo.rescNm}</label></td>
		</tr>
		</c:if>
		</c:forEach>
	</c:if>
		</tbody></table>
		</div>
	</div>
</u:titleArea>

</div>

<!-- ICON -->
<div style="float:left; width:6%; height: 270px;">

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0"><tbody>
<tr>
<td style="vertical-alignment: middle;">
	<table align="center" width="20" border="0" cellpadding="0" cellspacing="0"><tbody>
	<tr>
	<td><u:buttonIcon href="javascript:" image="ico_left.png" titleId="cm.btn.selAdd" alt="선택추가" onclick="addBb();" /></td>
	</tr>

	<tr>
	<td class="height5"></td>
	</tr>

	<tr>
	<td><u:buttonIcon href="javascript:" image="ico_right.png" titleId="cm.btn.selDel" alt="선택삭제" onclick="removeBb();" /></td>
	</tr>
	</tbody></table></td>
</tr>
</tbody></table>
</div>

<!-- RIGHT -->
<div style="float:right; width:47%;">

<u:titleArea outerStyle="height: 300px" innerStyle="width:94%; margin: 0 auto 0 auto; padding: 10px 0 0 0">
	<u:title titleId="bb.jsp.setMyBullPop.bbList" type="small" alt="게시판 목록" />

	<div class="listarea">
		<table class="listtable" border="0" cellpadding="0" cellspacing="1">
		<colgroup>
			<col width="33">
			<col>
		<tbody>
		<tr>
		<td class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('rightTbl', this.checked);" value=""/></td>
		<td class="head_ct"><u:msg titleId="cols.bbNm" alt="게시판명" /></td>
		</tr>
		</tbody></table>

		<div class="listbody" style="height:225px;">
		<table id="rightTbl" class="listtable" border="0" cellpadding="0" cellspacing="1">
		<colgroup>
			<col width="33">
			<col>
		</colgroup>
		<tbody>
	<c:if test="${fn:length(baBrdBVoList) > 0}">
		<c:forEach items="${baBrdBVoList}" var="baBrdBVo" varStatus="status">
		<c:if test="${baBrdBVo.pltYn != 'Y'}">
		<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
		<td class="bodybg_ct"><u:checkbox name="chk" value="${baBrdBVo.brdId}" checked="false" />
			<u:input type="hidden" id="id_${baBrdBVo.brdId}" name="brdId" value="${baBrdBVo.brdId}" />
			<u:input type="hidden" id="nm_${baBrdBVo.brdId}" name="rescNm" value="${baBrdBVo.rescNm}" />
			</td>
		<td class="body_ct"><label for="chk${baBrdBVo.brdId}">${baBrdBVo.rescNm}</label></td>
		</tr>
		</c:if>
		</c:forEach>
	</c:if>
		</tbody></table>
		</div>
	</div>
</u:titleArea>

</div>


<div class="color_txt">
※ <u:msg titleId="bb.jsp.setPltBoard.tx01"
	alt="선택된 게시판은 사용자별 포틀릿의 설정 대상이 됩니다." /><br/>
	※ <u:msg titleId="bb.jsp.setPltBoard.tx02"
	alt="선택된 목록에서 제외 시 사용자가 설정한 포틀릿에서 자동 삭제됩니다." />
</div>

<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" onclick="saveBb();" auth="A" />
</u:buttonArea>

</form>
</div>
