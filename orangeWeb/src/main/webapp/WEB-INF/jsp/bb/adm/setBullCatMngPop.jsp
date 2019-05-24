<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<% // 다음 Row 번호 %>
var gMaxRow = parseInt('${fn:length(baCatGrpBVo.catVoList)}');
<% // [목록버튼:추가] TR 추가 %>
function addRow() {
	restoreUniform('catList');
	var $tr = $('#catList #hiddenTr');
	var html = $tr[0].outerHTML;
	html = html.replace(/_NO/g,'_'+gMaxRow);
	gMaxRow++;
	$tr.before(html);
	$tr = $tr.prev();
	$tr.attr('id','');
	$tr.attr('style','');
	$tr.find('input[name="valid"]').val('Y');
	dialog.resize('setBullCatMngPop');
	applyUniform('catList');
}
<% // [목록버튼:삭제] TR 삭제 %>
function delRow(btn) {
	if(confirmMsg("cm.cfrm.del")) {<% // cm.cfrm.del=삭제하시겠습니까 ? %>
		$tr = $(btn).parents('tr');
		var $catId = $tr.find('input[name="catId"]');
		var $valid = $tr.find('input[name="valid"]');
		var $deleted = $tr.find('input[name="deleted"]');
		// removeHandler
		var id = $catId.attr('id');
		var idx = id.substring(id.lastIndexOf('_'));
		validator.removeHandler('colmNm' + idx);
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
		validator.removeHandler('rescVa_${langTypCdVo.cd}' + idx);
		</c:forEach>
		// valid
		if ($catId.val() == '') $valid.val('');
		// deleted
		$deleted.val('Y')
		$tr.hide();
		dialog.resize('setBullCatMngPop');
	}
}
<% // 위로, 아래로 이동 %>
function moveCat(direction){
	var arr = getCheckedValue("catList", "cm.msg.noSelect"); <% // cm.msg.noSelect=선택한 항목이 없습니다. %>
	if (direction == 'up') {
		for (var i = 0; i < arr.length; i++) {
			$tr = $('#tr'+arr[i]);
			$tr.insertBefore($tr.prev());
		}
	}
	if (direction == 'down') {
		for (var i = arr.length-1; i >= 0; i--) {
			$tr = $('#tr'+arr[i]);
			$tr.insertAfter($tr.next());
		}
	}
}
<% // [팝업:저장] 카테고리그룹 저장 %>
function saveCat() {
	<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
	validator.removeHandler('rescVa_${langTypCdVo.cd}_NO');
	</c:forEach>

	$('input[name="dispOrdr"]').each(function(index) {
		$(this).val(index + 1);
	});

	if (validator.validate('setBullCatMngForm')) {
		var $form = $('#setBullCatMngForm');
		$form.attr('method','post');
		$form.attr('action','./transCat.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
}
<% // [팝업:저장] 카테고리그룹 삭제 %>
function delCat() {
	if(confirmMsg("cm.cfrm.del")) {	<% // cm.cfrm.del=삭제하시겠습니까? %>
		callAjax('./transCatDelAjx.do?menuId=${menuId}', {catGrpId:'${baCatGrpBVo.catGrpId}'}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				dialog.close(this);
				parent.location.replace(location.href);
			}
		});
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
function changeLangSelectorCat(areaId, id, va){
	if(va==''){
		var langSelector = $('#'+areaId+' #langSelector'+id.substring(id.lastIndexOf('_')));
		var nm = $("#catList #"+id).attr("name");
		langSelector.val(nm.substring(nm.length-2));
		langSelector.trigger('click');
	}
}

$(document).ready(function() {
	<c:if test="${empty baCatGrpBVo.catGrpId}">
	addRow();
	</c:if>
});
//-->
</script>

<div style="width:450px">
<form id="setBullCatMngForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="catGrpId" value="${baCatGrpBVo.catGrpId}" />

<% // 폼 필드 %>
<u:listArea colgroup="27%,73%">
	<%-- <u:secu auth="SYS">
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.comp" alt="회사" /></td>
	<td>
		<select id="compId" name="compId" <u:elemTitle titleId="cols.comp" /> <c:if test="${!empty baCatGrpBVo.catGrpId }">disabled="disabled"</c:if>>
			<c:forEach items="${ptCompBVoList}" var="ptCompBVo" varStatus="status">
				<u:option value="${ptCompBVo.compId}" title="${ptCompBVo.rescNm}" checkValue="${baTblBVo.compId}"/>
			</c:forEach>
		</select>
	</td>
	</tr>
	</u:secu> --%>
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.catGrp" alt="카테고리그룹" /></td>
	<td><table cellspacing="0" cellpadding="0" border="0">
		<tr>
		<td id="langTypArea">
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:convert srcId="${baCatGrpBVo.rescId}_${langTypCdVo.cd}" var="grpRescVa" />
			<u:set test="${status.first}" var="style" value="width:150px;" elseValue="width:150px; display:none" />
			<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
			<u:input id="grpRescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.catGrp" value="${grpRescVa}" style="${style}"
				maxByte="120" validator="changeLangSelector('setBullCatMngForm', id, va)" mandatory="Y" />
		</c:forEach>
		</td>
		<td>
		<c:if test="${fn:length(_langTypCdListByCompId)>1}">
			<select id="langSelector" onchange="changeLangTypCd('setBullCatMngForm','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
			</c:forEach>
			</select>
		</c:if>
		<u:input type="hidden" id="grpRescId" value="${baCatGrpBVo.rescId}" />
		</td>
		</tr>
		</table></td>
	</tr>
</u:listArea>

<% // 카테고리 %>
<u:title type="small" titleId="cols.cat" alt="카테고리" />

<u:listArea colgroup="33,,60" noBottomBlank="true">
	<tr>
	<th class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('catList', this.checked);" value=""/></th>
	<c:if test="${fn:length(_langTypCdListByCompId)>1}">
	<u:msg titleId="pt.jsp.terms.chgLangAll" alt="일괄 언어 변경" var="title" />
	<th class="head_ct"><u:mandatory /><a href="javascript:changeLangTypCds('setBullCatMngForm');" title="${title}"><u:msg titleId="cols.cat" alt="카테고리" /></a></th>
	</c:if>
	<c:if test="${fn:length(_langTypCdListByCompId)<=1}">
	<th class="head_ct"><u:mandatory /><u:msg titleId="cols.cat" alt="카테고리" /></th>
	</c:if>
	<th class="head_ct"><u:msg titleId="cols.fnc" alt="기능" /></th>
	</tr>
</u:listArea>

<u:listArea id="catList" colgroup="33,,60">
	<c:forEach items="${baCatGrpBVo.catVoList}" var="catVo" varStatus="status">
		<u:set test="${status.last}" var="trDisp" value="display: none;" elseValue="" />
		<u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${catVo.catId}" />
		<u:set test="${status.last}" var="colmIndex" value="_NO" elseValue="_${status.index+1}" />
		<u:set test="${status.last}" var="valid" value="" elseValue="Y" />
	<tr id="${trId}" style="${trDisp}" onmouseover="this.className='trover'" onmouseout="this.className='trout'">
	<td class="bodybg_ct"><input type="checkbox" value="${catVo.catId}"/></td>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			<td id="langTyp${colmIndex}">
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
				<u:convert srcId="${catVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
				<u:set test="${status.first}" var="style" value="width:200px;" elseValue="width:200px; display:none" />
				<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
				<u:input id="rescVa_${langTypCdVo.cd}${colmIndex}" name="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.colmDispNm" value="${rescVa}" style="${style}"
					maxByte="30" validator="changeLangSelectorCat('catList', id, va)" mandatory="Y" />
			</c:forEach>
			</td>
			<td>
			<c:if test="${fn:length(_langTypCdListByCompId)>1}">
				<select id="langSelector${colmIndex}" onchange="changeLangTypCd('catList','langTyp${colmIndex}',this.value)" <u:elemTitle titleId="cols.langTyp" />>
				<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
				<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
				</c:forEach>
				</select>
			</c:if>
			<u:input type="hidden" id="rescId${colmIndex}" name="rescId" value="${catVo.rescId}" />
			</td>
			</tr>
			</tbody></table></td>
	<td><table align="center" border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td id="delTd"><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="delRow(this);" auth="A" /></td>
		</tr></tbody></table>
		<u:input type="hidden" id="catId${colmIndex}" name="catId" value="${catVo.catId}" />
		<u:input type="hidden" id="dispOrdr${colmIndex}" name="dispOrdr" value="${dispOrdr}" />
		<u:input type="hidden" id="deleted${colmIndex}" name="deleted" value="" />
		<u:input type="hidden" id="valid${colmIndex}" name="valid" value="${valid}" />
		</td>
	</tr>
	</c:forEach>
</u:listArea>

<u:buttonArea>
	<u:button id="btnPlus" titleId="cm.btn.plus" alt="행추가" onclick="addRow();" auth="A" />
	<u:button titleId="cm.btn.up" alt="위로이동" onclick="moveCat('up');" auth="A" />
	<u:button titleId="cm.btn.down" alt="아래로이동" onclick="moveCat('down');" auth="A" />
	<u:button titleId="cm.btn.save" alt="저장" onclick="saveCat();" auth="A" />
<c:if test="${!empty baCatGrpBVo.catGrpId}">
	<u:button titleId="cm.btn.del" alt="삭제" onclick="delCat();" auth="A" />
</c:if>
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>

</form>
</div>