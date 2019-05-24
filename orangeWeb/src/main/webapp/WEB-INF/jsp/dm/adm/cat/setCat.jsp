<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="nonPageParams" excludes="catId,pageNo"/>
<script type="text/javascript">
<!--<% // [하단버튼:항목관리] 항목 관리 %>
function setItemMngPop() {
	dialog.open('setItemMngPop','<u:msg titleId="dm.jsp.setItemMgm" alt="항목관리" />','./setItemMngPop.do?menuId=${menuId}&catId=${dmCatBVo.catId}');
}
<% // [하단버튼:목록순서] 목록 순서 %>
function setListOrdrPop() {
	dialog.open('setListOrdrPop','<u:msg titleId="bb.jsp.setListOrdrPop.title" alt="목록순서" />','./setListOrdrPop.do?menuId=${menuId}&catId=${dmCatBVo.catId}');
}<% // [하단버튼:삭제] 삭제 %>
function delCat() {
	if (confirmMsg("cm.cfrm.del")) {	<% // cm.cfrm.del=삭제하시겠습니까? %>
		callAjax('./${transDelPage}Ajx.do?menuId=${menuId}', {catId:'${dmCatBVo.catId}'}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.href = './${listPage}.do?${nonPageParams}';
			}
		});
	}
}<% // 심의자 선택 %>
function searchDiscr(schWord) {
	var $view = $("#setForm");
	var data = {userUid:$view.find("#discrUid").val()};<% // 팝업 열때 선택될 데이타 %>
	<% // option : data, multi, withSub, titleId %>
	searchUserPop({data:data}, function(userVo){
		if(userVo!=null){
			$view.find("#discrUid").val(userVo.userUid);
			$view.find("#discrNm").val(userVo.rescNm);
			// alert(new ParamMap(userVo));
		}else{
			return false;
		}
	});
}<% // 심의여부 변경 이벤트 %>
function changeDiscYn(va) {
	if (va == 'Y') {
		$('#discrBtn').on('click', function() { searchDiscr(''); });
		$('#discrNm').show();
		$('#discrBtn').show();
	} else {
		$('#discrUid').val('');
		$('#discrNm').val('');
		$('#discrBtn').off();
		$('#discrNm').hide();
		$('#discrBtn').hide();
	}
}<%
// 저장 - 버튼 클릭 %>
function save(){
	if (validator.validate('setForm')) {
		var $form = $('#setForm');
		$form.attr('method','post');
		$form.attr('action','./${transPage}.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
};
$(document).ready(function() {
	setUniformCSS();
	changeDiscYn("${empty dmCatBVo.discYn ? 'N' : dmCatBVo.discYn}");
});
//-->
</script>

<u:title titleId="dm.jsp.catMgm.title" alt="유형관리" menuNameFirst="true"/>
<form id="setForm" method="post">
<input type="hidden" name="menuId" value="${menuId}" />
<u:input type="hidden" id="catId" value="${param.catId}" />
<u:input type="hidden" id="listPage" value="./${listPage}.do?${nonPageParams}" />
<u:input type="hidden" id="viewPage" value="./${setPage}.do?${params}" />

<u:title titleId="dm.jsp.dftInfo.title" alt="기본정보" type="small" />
<c:set var="colgroup" value="15%,35%,15%,35%"/>
<u:listArea colgroup="${colgroup }" noBottomBlank="true">
<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="dm.cols.catNm" alt="유형명" /></td>
	<td class="bodybg_lt" colspan="3"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td id="langTypArea">
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:convert srcId="${dmCatBVo.rescId}_${langTypCdVo.cd}" var="catRescVa" />
			<u:set test="${status.first}" var="style" value="width:300px;" elseValue="width:300px; display:none" />
			<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
			<u:input id="catRescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="dm.cols.catNm" value="${catRescVa}" style="${style}"
				maxByte="30" validator="changeLangSelector('setForm', id, va)" mandatory="Y" />
		</c:forEach>
		</td>
		<td>
		<c:if test="${fn:length(_langTypCdListByCompId)>1}">
			<select id="langSelector" onchange="changeLangTypCd('setForm','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
			</c:forEach>
			</select>
		</c:if>
		<u:input type="hidden" id="catRescId" value="${dmCatBVo.rescId}" />
		</td>
		</tr></table></td>
</tr>
<tr>
	<td class="head_lt"><u:msg titleId="cols.desc" alt="설명" /></td>
	<td class="bodybg_lt" colspan="3"><u:textarea id="catDesc" value="${dmCatBVo.catDesc}" titleId="cols.desc" maxByte="400" style="width:95%" rows="3" /></td>
</tr>
<tr>
	<td class="head_lt"><u:msg titleId="cols.discYn" alt="심의여부" /></td>
	<td class="bodybg_lt" ><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><select id="discYn" name="discYn" onchange="changeDiscYn(this.value);">
			<u:option value="N" titleId="bb.option.discN" alt="미심의" checkValue="${dmCatBVo.discYn}" />
			<u:option value="Y" titleId="bb.option.discY" alt="심의" checkValue="${dmCatBVo.discYn}" />
			</select></td>
		<td id="discYnMsg" class="body_lt" style="display: none;">(<u:msg titleId="bb.jsp.setBb.tx04" alt="기본테이블, 비답변형일 때 선택 가능" />)</td>
		</tr>
		</tbody></table></td>
	<td class="head_lt"><u:msg titleId="cols.discr" alt="심의자" /></td>
	<td class="bodybg_lt" ><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><u:input id="discrNm" titleId="cols.discr" value="${dmCatBVo.discrNm}" readonly="Y" />
			<u:input type="hidden" id="discrUid" value="${dmCatBVo.discrUid}" /></td>
		<td><u:buttonS id="discrBtn" titleId="cm.btn.search" alt="검색" /></td>
		</tr>
		</tbody></table></td>
</tr>
</u:listArea>

<u:blank />

<u:title titleId="dm.jsp.addItem.title" alt="추가항목" type="small" />
<c:set var="colgroup" value=",45%,20%"/>
<u:listArea colgroup="${colgroup }" noBottomBlank="true">
<tr>
	<td class="head_ct"><u:msg titleId="cols.itemNm" alt="항목명" /></td>
	<td class="head_ct"><u:msg titleId="dm.cols.itemTyp" alt="항목구분" /></td>
	<td class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
</tr>
<c:forEach var="list" items="${dmItemBVoList }" varStatus="status">
<tr>
	<td class="bodybg_lt">${list.itemDispNm }</td>
	<td class="bodybg_lt" >
		<u:input type="hidden" id="itemId" value="${list.itemId}" />
		<u:input type="hidden" id="itemNm" value="${list.itemNm}" />
		<u:input type="hidden" id="addItemYn" name="addItemYn" value="Y" />
		<u:msg var="itemTypNm" titleId="bb.option.${fn:toLowerCase(list.itemTyp) }" />${itemTypNm}
	</td>
	<td class="bodybg_ct" >
		<select id="useYn" name="useYn" <u:elemTitle titleId="cols.useYn" />>
			<u:option value="Y" titleId="cm.option.use" checkValue="${list.useYn}" />
			<u:option value="N" titleId="cm.option.notUse" checkValue="${list.useYn}" selected="${empty list.useYn }"/>
		</select>
	</td>
</tr>
</c:forEach>
</u:listArea>

<u:blank />

<% // 하단 버튼 %>
<u:buttonArea>
	<%-- <c:if test="${!empty dmCatBVo.catId }">
		<u:button titleId="dm.jsp.setItemMgm" alt="항목관리" onclick="setItemMngPop();" auth="A" />
		<u:button titleId="bb.btn.listOrdr" alt="목록순서" onclick="setListOrdrPop();" auth="A" />
		<u:button titleId="cm.btn.del" alt="삭제" onclick="delCat();" auth="A" />
	</c:if> --%>
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:save();" auth="A" />
	<u:button titleId="cm.btn.list" href="./${listPage }.do?${paramsForList }" alt="목록" />
	
</u:buttonArea>

</form>

