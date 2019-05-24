<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<% // 다음 Row 번호 %>
var gMaxRow = parseInt('${fn:length(dmCdGrpBVo.dmCdDVoList)}');
<% // [목록버튼:추가] TR 추가 %>
function addRow() {
	restoreUniform('cdList');
	var $tr = $('#cdList #hiddenTr');
	var html = $tr[0].outerHTML;
	html = html.replace(/_NO/g,'_'+gMaxRow);
	gMaxRow++;
	$tr.before(html);
	$tr = $tr.prev();
	$tr.attr('id','');
	$tr.attr('style','');
	$tr.find('input[name="valid"]').val('Y');
	dialog.resize('setCdPop');
	applyUniform('cdList');
}
<% // [목록버튼:삭제] TR 삭제 %>
function delRow(btn) {
	$tr = $(btn).parents('tr');
	var $cdId = $tr.find('input[name="cdId"]');
	if($cdId.val() != '' && $('#setCdForm').find('input[id="cdGrpId"]').val() != ''){
		callAjax('./chkUseCdAjx.do?menuId=${menuId}', {cdGrpId:'${dmCdGrpBVo.cdGrpId}',cdId:$cdId.val()}, function(data) {
			if (data.message != null) {
				alert(data.message);
				return;
			}
		});
	}
	if(confirmMsg("cm.cfrm.del")) {<% // cm.cfrm.del=삭제하시겠습니까 ? %>		
		var $valid = $tr.find('input[name="valid"]');
		var $deleted = $tr.find('input[name="deleted"]');
		// removeHandler
		var id = $cdId.attr('id');
		var idx = id.substring(id.lastIndexOf('_'));
		validator.removeHandler('colmNm' + idx);
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
		validator.removeHandler('rescVa_${langTypCdVo.cd}' + idx);
		</c:forEach>
		// valid
		if ($cdId.val() == '') $valid.val('');
		// deleted
		$deleted.val('Y');
		$tr.hide();
		dialog.resize('setCdPop');
	}
}
<% // 위로, 아래로 이동 %>
function moveCd(direction){
	var arr = getCheckedValue("cdList", "cm.msg.noSelect"); <% // cm.msg.noSelect=선택한 항목이 없습니다. %>
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
<% // [팝업:저장] 코드그룹 저장 %>
function saveCd() {
	<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
	validator.removeHandler('rescVa_${langTypCdVo.cd}_NO');
	</c:forEach>

	$('input[name="sortOrdr"]').each(function(index) {
		$(this).val(index + 1);
	});

	if (validator.validate('setCdForm')) {
		var $form = $('#setCdForm');
		$form.attr('method','post');
		$form.attr('action','./transCd.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
}
<% // [팝업:저장] 코드그룹 삭제 %>
function delCd() {
	if(confirmMsg("cm.cfrm.del")) {	<% // cm.cfrm.del=삭제하시겠습니까? %>
		callAjax('./transCdDelAjx.do?menuId=${menuId}', {cdGrpId:'${dmCdGrpBVo.cdGrpId}'}, function(data) {
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
function changeLangSelectorCd(areaId, id, va){
	if(va==''){
		var langSelector = $('#'+areaId+' #langSelector'+id.substring(id.lastIndexOf('_')));
		var nm = $("#cdList #"+id).attr("name");
		langSelector.val(nm.substring(nm.length-2));
		langSelector.trigger('click');
	}
}

//- 사용안함 처리시 사용중인 코드인지 확인
function useSelect(obj,cdId){
	if($('#setCdForm').find('input[id="cdGrpId"]').val() != '' && $(obj).val() =='N'){
		// bb.msg.not.use.cd=사용중인 코드는 사용안함 처리 할 수 없습니다.
		callAjax('./chkUseCdAjx.do?menuId=${menuId}', {cdGrpId:'${dmCdGrpBVo.cdGrpId}',cdId:cdId,msgCode:'bb.msg.not.use.cd'}, function(data) {
			if (data.message != null) {
				alert(data.message);
				$(obj).val('Y');
			}
		});
	}
};

$(document).ready(function() {
	<c:if test="${empty dmCdGrpBVo.cdGrpId}">
	addRow();
	</c:if>
});
//-->
</script>

<div style="width:550px">
<form id="setCdForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="cdGrpId" value="${dmCdGrpBVo.cdGrpId}" />

<% // 폼 필드 %>
<u:listArea colgroup="27%,73%">
	<%-- <u:secu auth="SYS">
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.comp" alt="회사" /></td>
	<td>
		<select id="compId" name="compId" <u:elemTitle titleId="cols.comp" /> <c:if test="${!empty dmCdGrpBVo.cdGrpId }">disabled="disabled"</c:if>>
			<c:forEach items="${ptCompBVoList}" var="ptCompBVo" varStatus="status">
				<u:option value="${ptCompBVo.compId}" title="${ptCompBVo.rescNm}" checkValue="${baTblBVo.compId}"/>
			</c:forEach>
		</select>
	</td>
	</tr>
	</u:secu> --%>
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="bb.cols.codeGrp" alt="코드그룹" /></td>
	<td><table cellspacing="0" cellpadding="0" border="0">
		<tr>
		<td id="langTypArea">
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:convert srcId="${dmCdGrpBVo.rescId}_${langTypCdVo.cd}" var="grpRescVa" />
			<u:set test="${status.first}" var="style" value="width:150px;" elseValue="width:150px; display:none" />
			<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
			<u:input id="grpRescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="bb.cols.codeGrp" value="${grpRescVa}" style="${style}"
				maxByte="120" validator="changeLangSelector('setCdForm', id, va)" mandatory="Y" />
		</c:forEach>
		</td>
		<td>
		<c:if test="${fn:length(_langTypCdListByCompId)>1}">
			<select id="langSelector" onchange="changeLangTypCd('setCdForm','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
			</c:forEach>
			</select>
		</c:if>
		<u:input type="hidden" id="grpRescId" value="${dmCdGrpBVo.rescId}" />
		</td>
		</tr>
		</table></td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
		<td>
			<u:checkArea>
				<u:radio name="grpUseYn" value="Y" titleId="cm.option.use" checked="${empty dmCdGrpBVo.grpUseYn || dmCdGrpBVo.grpUseYn eq 'Y' ? true : false}" />
				<u:radio name="grpUseYn" value="N" titleId="cm.option.notUse" checked="${dmCdGrpBVo.grpUseYn eq 'N' ? true : false }" />
			</u:checkArea>
		</td>
	</tr>
</u:listArea>

<% // 코드 %>
<u:title type="small" titleId="bb.cols.code" alt="코드" />

<u:listArea colgroup="33,,100,60" noBottomBlank="true">
	<tr>
	<th class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('cdList', this.checked);" value=""/></th>
	<c:if test="${fn:length(_langTypCdListByCompId)>1}">
	<u:msg titleId="pt.jsp.terms.chgLangAll" alt="일괄 언어 변경" var="title" />
	<th class="head_ct"><u:mandatory /><a href="javascript:changeLangTypCds('setCdForm');" title="${title}"><u:msg titleId="bb.cols.code" alt="코드" /></a></th>
	</c:if>
	<c:if test="${fn:length(_langTypCdListByCompId)<=1}">
	<th class="head_ct"><u:mandatory /><u:msg titleId="bb.cols.code" alt="코드" /></th>
	</c:if>
	<th class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></th>
	<th class="head_ct"><u:msg titleId="cols.fnc" alt="기능" /></th>
	</tr>
</u:listArea>

<u:listArea id="cdList" colgroup="33,,100,60">
	<c:forEach items="${dmCdGrpBVo.dmCdDVoList}" var="cdVo" varStatus="status">
		<u:set test="${status.last}" var="trDisp" value="display: none;" elseValue="" />
		<u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${cdVo.cdId}" />
		<u:set test="${status.last}" var="colmIndex" value="_NO" elseValue="_${status.index+1}" />
		<u:set test="${status.last}" var="valid" value="" elseValue="Y" />
	<tr id="${trId}" style="${trDisp}" onmouseover="this.className='trover'" onmouseout="this.className='trout'">
	<td class="bodybg_ct"><input type="checkbox" value="${cdVo.cdId}"/></td>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			<td id="langTyp${colmIndex}">
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
				<u:convert srcId="${cdVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
				<u:set test="${status.first}" var="style" value="width:200px;" elseValue="width:200px; display:none" />
				<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
				<u:input id="rescVa_${langTypCdVo.cd}${colmIndex}" name="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="bb.cols.code" value="${rescVa}" style="${style}"
					maxByte="200" validator="changeLangSelectorCd('cdList', id, va)" mandatory="Y" />
			</c:forEach>
			</td>
			<td>
			<c:if test="${fn:length(_langTypCdListByCompId)>1}">
				<select id="langSelector${colmIndex}" onchange="changeLangTypCd('cdList','langTyp${colmIndex}',this.value)" <u:elemTitle titleId="cols.langTyp" />>
				<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
				<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
				</c:forEach>
				</select>
			</c:if>
			<u:input type="hidden" id="rescId${colmIndex}" name="rescId" value="${cdVo.rescId}" />
			</td>
			</tr>
			</tbody></table></td>
			<td class="body_ct">
				<select id="useYn" name="useYn" <u:elemTitle titleId="cols.useYn" /> onchange="useSelect(this,'${cdVo.cdId}');">
				<u:option value="Y" titleId="cm.option.use" alt="사용" checkValue="${cdVo.useYn}" />
				<u:option value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${cdVo.useYn}" />
				</select>
			</td>
	<td><table align="center" border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td id="delTd"><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="delRow(this);" auth="A" /></td>
		</tr></tbody></table>
		<u:input type="hidden" id="cdId${colmIndex}" name="cdId" value="${cdVo.cdId}" />
		<u:input type="hidden" id="sortOrdr${colmIndex}" name="sortOrdr" value="${sortOrdr}" />
		<u:input type="hidden" id="deleted${colmIndex}" name="deleted" value="" />
		<u:input type="hidden" id="valid${colmIndex}" name="valid" value="${valid}" />
		</td>
	</tr>
	</c:forEach>
</u:listArea>

<u:buttonArea>
	<u:button id="btnPlus" titleId="cm.btn.plus" alt="행추가" onclick="addRow();" auth="A" />
	<u:button titleId="cm.btn.up" alt="위로이동" onclick="moveCd('up');" auth="A" />
	<u:button titleId="cm.btn.down" alt="아래로이동" onclick="moveCd('down');" auth="A" />
	<u:button titleId="cm.btn.save" alt="저장" onclick="saveCd();" auth="A" />
<c:if test="${!empty dmCdGrpBVo.cdGrpId}">
	<u:button titleId="cm.btn.del" alt="삭제" onclick="delCd();" auth="A" />
</c:if>
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>

</form>
</div>