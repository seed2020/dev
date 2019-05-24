<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<% // [팝업:저장] 테이블 저장 %>
function save() {
	$("#itemArea select[name='useYn'] > option:selected").each(function(index){
		$tr = $(this).parents('tr');
		if($(this).val() == '' || $(this).val() == 'N') {
			var $rescId = $tr.find('input[name="rescId"]');
			var id = $rescId.attr('id');
			var idx = id.substring(id.lastIndexOf('_'));
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			validator.removeHandler('rescVa_${langTypCdVo.cd}' + idx);
			</c:forEach>
			$tr.find('input[name="valid"]').val('');
		}
		
		if($(this).val() == 'Y') {
			$tr.find('input[name="valid"]').val('Y');
		}
	});
	
	if (validator.validate('setForm')) {
		var $form = $('#setForm');
		$form.attr('method','post');
		$form.attr('action','./transItem.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
}
<% // 일괄 언어 선택 변경 %>
function changeLangTypCds(areaId){
	var $area = $("#"+areaId);
	<% // 변경할 언어 구하기 - 첫번째 언어 선택 select 의 다음 값 %>
	var langSel = $area.find("select[id^='langSelector']:first");
	if(langSel.length==0) return;
	var index = langSel[0].selectedIndex + 1;
	if(index>=langSel[0].options.length) index = 0;
	var langCd = langSel[0].options[index].value;
	var rescNm;
	<% // input - 어권에 맞게 show/hide %>
	$area.find("td[id^='langTyp'] input").each(function(){
		rescNm = $(this).attr('name');
		if(rescNm!=null && rescNm.endsWith(langCd)){
			$(this).show();
		} else { $(this).hide(); }
	});
	<% // select 변경 %>
	var selectors = $area.find("select[id^='langSelector']");
	selectors.val(langCd);
	selectors.uniform.update();
}
//- 해당 어권의 명이 입력되지 않았 을 경우 해당 어권을 보이게 함 %>
function changeLangSelectorColm(areaId, id, va){
	if(va==''){
		var langSelector = $('#'+areaId+' #langSelector'+id.substring(id.lastIndexOf('_')));
		var nm = $("#itemArea #"+id).attr("name");
		langSelector.val(nm.substring(nm.length-2));
		langSelector.trigger('click');
	}
}
$(document).ready(function() {
	//setUniformCSS();
});
//-->
</script>

<div style="width:700px">
<form id="setForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<c:set var="colgroup" value=",45%,20%"/>
<u:listArea colgroup="${colgroup }" id="itemArea">
	<tr>
		<td class="head_ct"><u:mandatory /><u:msg titleId="cols.itemNm" alt="항목명" /></td>
		<td class="head_ct"><u:msg titleId="dm.cols.itemTyp" alt="항목구분" /></td>
		<td class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
	</tr>
	<c:forEach var="list" items="${dmItemBVoList }" varStatus="status">
		<c:set var="colmIndex" value="_${status.index+1}" />
		<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
			<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			<td id="langTyp${colmIndex}">
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
				<u:convert srcId="${list.rescId}_${langTypCdVo.cd}" var="rescVa" />
				<u:set test="${status.first}" var="style" value="width:100px;" elseValue="width:100px; display:none" />
				<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
				<u:input id="rescVa_${langTypCdVo.cd}${colmIndex}" name="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.colmDispNm" value="${rescVa}" style="${style}"
					maxByte="120" validator="changeLangSelectorColm('itemArea', id, va)" mandatory="Y" />
			</c:forEach>
			</td>
			<td>
			<c:if test="${fn:length(_langTypCdListByCompId)>1}">
				<select id="langSelector${colmIndex}" onchange="changeLangTypCd('itemArea','langTyp${colmIndex}',this.value)" <u:elemTitle titleId="cols.langTyp" />>
				<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
				<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
				</c:forEach>
				</select>
			</c:if>
			<u:input type="hidden" id="rescId${colmIndex}" name="rescId" value="${list.rescId}" />
			</td>
			</tr>
			</tbody></table></td>
			<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0"><tbody>
				<tr>
				<td>
					<u:input type="hidden" id="itemId" value="${list.itemId}" />
					<u:input type="hidden" id="itemNm" value="${list.itemNm}" />
					<u:input type="hidden" id="itemTyp" value="${list.itemTyp}" />
					<u:input type="hidden" id="colmLen" value="${list.colmLen }" />
					<u:input type="hidden" id="dataTyp" name="dataTyp" value="VARCHAR" />
					<u:msg var="itemTypNm" titleId="bb.option.${fn:toLowerCase(list.itemTyp) }" />
					<u:input id="itemTypNm" value="${itemTypNm}" titleId="dm.cols.itemTyp" disabled="Y" style="width:100px;"/>
					<u:set var="valid" test="${list.useYn eq 'Y'}" value="Y" elseValue=""/>
					<u:input type="hidden" id="valid${colmIndex}" name="valid" value="${valid}" />
					</td>
				<c:choose>
					<c:when test="${list.itemTyp eq 'TEXTAREA' }">
						<td><select id="itemTypVa" name="itemTypVa">
						<u:msg titleId="bb.cols.line" alt="라인" var="line" />
						<u:option value="2" title="2 ${line}" checkValue="${list.itemTypVa}" />
						<u:option value="3" title="3 ${line}" checkValue="${list.itemTypVa}" />
						<u:option value="4" title="4 ${line}" checkValue="${list.itemTypVa}" />
						<u:option value="5" title="5 ${line}" checkValue="${list.itemTypVa}" />
						</select></td>
					</c:when>
					<c:when test="${list.itemTyp eq 'CODE' }">
						<td><c:if test="${fn:length(cdList) > 0}"><select id="itemTypVa" name="itemTypVa">
							<c:forEach items="${cdList}" var="cd" varStatus="status">
								<u:option value="${cd.cdGrpId}" title="${cd.rescNm}" checkValue="${list.itemTypVa }"/>
							</c:forEach>
						</select></c:if></td>
					</c:when>
					<c:otherwise>
						<u:input type="hidden" id="itemTypVa"/>
					</c:otherwise>
				</c:choose>
				</tr>
				</tbody></table>
			</td>
			<td class="body_ct">
				<u:set var="useYnStyle" test="${list.itemTyp eq 'CODE' && fn:length(cdList) == 0}" value="style='display:none;'" elseValue=""/>
				<select id="useYn" name="useYn" ${useYnStyle } <u:elemTitle titleId="cols.useYn" />>
				<u:option value="Y" titleId="cm.option.use" checkValue="${list.useYn}" />
				<u:option value="N" titleId="cm.option.notUse" checkValue="${list.useYn}" selected="${!empty useYnStyle || empty list.useYn }"/>
				</select>
			</td>
		</tr>
	</c:forEach>
</u:listArea>
<u:blank />
<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:save();" auth="A" />
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</form>
</div>