<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<% // [목록:사용] 사용여부 체크 이벤트 %>
function checkUseYn(idx) {
	var disabled = $('#use_'+idx).is(':checked') == false;
	<c:if test="${empty param.itemTypCd }">
	setDisabled($('#reg_'+idx), disabled);
	setDisabled($('#mod_'+idx), disabled);
	</c:if>
	setDisabled($('#read_'+idx), disabled);
	setDisabled($('#list_'+idx), disabled);
}
<% // [팝업:저장] 테이블 저장 %>
function saveDisp() {
	var i, yn, max = parseInt('${fn:length(itemDispList)}');
	for (i = 1; i < max+1; i++) {
		yn = $('#use_'+i).is(':checked') ? 'Y' : 'N'; $('#useYn_'+i).val(yn);
		<c:if test="${empty param.itemTypCd }">
		yn = $('#reg_'+i).is(':checked') ? 'Y' : 'N'; $('#regDispYn_'+i).val(yn);
		yn = $('#mod_'+i).is(':checked') ? 'Y' : 'N'; $('#modDispYn_'+i).val(yn);
		</c:if>
		yn = $('#read_'+i).is(':checked') ? 'Y' : 'N'; $('#readDispYn_'+i).val(yn);
		yn = $('#list_'+i).is(':checked') ? 'Y' : 'N'; $('#listDispYn_'+i).val(yn);
	}
	var $form = $('#setItemMngForm');
	$form.attr('method','post');
	$form.attr('action','./transDisp.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form[0].submit();
}
//-->
</script>
<div style="width:600px">
<form id="setItemMngForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<c:if test="${!empty param.catId }"><u:input type="hidden" id="catId" value="${param.catId}" /></c:if>
<c:if test="${!empty param.itemTypCd }"><u:input type="hidden" id="itemTypCd" value="${param.itemTypCd}" /></c:if>
<u:input type="hidden" id="dialog" value="setItemMngPop" />

<% // 폼 필드 %>

<u:listArea>
	<tr>
	<td width="*" class="head_ct"><u:msg titleId="cols.itemNm" alt="항목명" /></td>
	<td width="6%" class="head_ct"><u:msg titleId="cols.use" alt="사용" /></td>
	<c:if test="${empty param.itemTypCd }">
	<td width="6%" class="head_ct"><u:msg titleId="cols.reg" alt="등록" /></td>
	<td width="6%" class="head_ct"><u:msg titleId="cols.mod" alt="수정" /></td>
	</c:if>
	<td width="6%" class="head_ct"><u:msg titleId="cols.view" alt="조회" /></td>
	<td width="6%" class="head_ct"><u:msg titleId="cols.list" alt="목록" /></td>
	</tr>

<c:if test="${fn:length(itemDispList) == 0}">
	<tr>
	<td class="nodata" colspan="9"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(itemDispList) > 0}">
	<c:forEach items="${itemDispList}" var="dispVo" varStatus="status">
	<c:set var="colmIndex" value="_${status.index+1}" />
	<c:if test="${dispVo.colmVo.addItemYn != 'Y'}">
		<c:set var="disable1" value="Y" /><c:if test="${empty param.itemTypCd }"><c:set var="disable2" value="Y" /></c:if><c:set var="disable3" value="N" />
		<c:if test="${dispVo.colmVo.itemNm == 'SUBJ' || dispVo.colmVo.itemNm == 'CONT'}"><c:set var="disable3" value="Y" /></c:if>
	</c:if>
	<c:if test="${dispVo.colmVo.addItemYn == 'Y'}">
		<c:set var="disable1" value="N" /><c:set var="disable2" value="N" /><c:set var="disable3" value="N" />
		<c:if test="${dispVo.useYn == 'N'}">
			<c:set var="disable2" value="Y" /><c:set var="disable3" value="Y" />
		</c:if>
	</c:if>
	<tr>
	<td class="body_lt">${dispVo.colmVo.itemDispNm}</td>
	<td class="bodybg_ct"><u:checkbox id="use${colmIndex}" name="use" value="Y" checkValue="${dispVo.useYn}" disabled="${disable1}" onclick="checkUseYn('${status.index+1}');" /></td>
	<c:if test="${empty param.itemTypCd }">
		<td class="bodybg_ct"><u:checkbox id="reg${colmIndex}" name="reg" value="Y" checkValue="${dispVo.regDispYn}" disabled="${disable2}" /></td>
		<td class="bodybg_ct"><u:checkbox id="mod${colmIndex}" name="mod" value="Y" checkValue="${dispVo.modDispYn}" disabled="${disable2}" /></td>
	</c:if>
	<td class="bodybg_ct"><u:checkbox id="read${colmIndex}" name="read" value="Y" checkValue="${dispVo.readDispYn}" disabled="${disable3}" /></td>
	<td class="bodybg_ct"><u:checkbox id="list${colmIndex}" name="list" value="Y" checkValue="${dispVo.listDispYn}" disabled="${disable3}" />
		<u:input type="hidden" id="itemId${colmIndex}" name="itemId" value="${dispVo.itemId}" />
		<u:input type="hidden" id="atrbId${colmIndex}" name="atrbId" value="${dispVo.atrbId}"/>
		<u:input type="hidden" id="useYn${colmIndex}" name="useYn" value="${dispVo.useYn}" />
		<c:if test="${empty param.itemTypCd }">
			<u:input type="hidden" id="regDispYn${colmIndex}" name="regDispYn" value="${dispVo.regDispYn}" />
			<u:input type="hidden" id="modDispYn${colmIndex}" name="modDispYn" value="${dispVo.modDispYn}" />
		</c:if>
		<u:input type="hidden" id="readDispYn${colmIndex}" name="readDispYn" value="${dispVo.readDispYn}" />
		<u:input type="hidden" id="listDispYn${colmIndex}" name="listDispYn" value="${dispVo.listDispYn}" />
		</td>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>

</form>
<div class="color_txt">※ <u:msg titleId="bb.jsp.setColmMng.tx01" alt="항목 갯수를 6개 이상 설정할경우 화면에 보여지는 목록이 부자연스러울수 있습니다." /></div>
<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="saveDisp();" alt="저장" auth="A" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</div>