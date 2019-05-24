<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%// 리로드 - 그룹 등록,수정 후 parent 에서 호출 %>
function reloadSelf(clsCd){
	location.replace('./setAuth.do?menuId=${menuId}&clsCd='+clsCd);
}<%// 테이블헤더의 제목 클릭 - 전체선택%>
function checkAll(cd,obj){
	var checked = $(obj).attr('data-checked') == 'N' ? true : false;
	$(obj).attr('data-checked',checked ? 'Y' : 'N');
	$chks = $("#setForm td."+cd+"Chk input[type='checkbox']");
	$chks.each(function(){
		$(this).checkInput(checked);
		useChk(this, null);
		//this.checked = checked;
	});
	$chks.uniform.update();
}
function useChk(obj, chkId){
	var td = getParentTag(obj,'td');
	$(td).find('input[id="useYn"]').val(obj.checked ? 'Y' : 'N');
	
	if(chkId != null && '${clsCd}' == 'SECUL_CD' && obj.checked){
		$chkCd = $(td).find('input[id="cd"]').val();
		$chks = $("#setForm td."+chkId+"Chk input[type='checkbox']");
		$chks.each(function(){
			tgtTd = getParentTag(this,'td');
			if($chkCd == $(tgtTd).find('input[id="cd"]').val()) return false;
			$(this).checkInput(obj.checked);
		});
	}
}
<%
// 저장 - 버튼 클릭 %>
function save(){
	if (validator.validate('setForm')) {
		var $form = $('#setForm');
		$form.attr('method','post');
		$form.attr('action','./transAuth.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
};
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="dm.jsp.catMgm.title" alt="유형관리" menuNameFirst="true"/>
<div class="front">
	<form id="setAuthForm" >
	<input type="hidden" name="menuId" value="${menuId}" />
	<div class="front_left">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td class="fronttit"><u:msg titleId="pt.jsp.setAuthGrp.authGrp" alt="권한그룹" /></td>
			<td class="width5"></td>
			<td class="frontinput"><select id="authGrpId" name="authGrpId" onchange="reloadSelf(this.value);" <u:elemTitle titleId="pt.jsp.setAuthGrp.authGrp" alt="권한그룹" />>
			<c:forEach items="${clsCdList}" var="cdVo" varStatus="status">
				<u:option value="${cdVo.cd}" title="${cdVo.rescNm}" checkValue="${clsCd}" />
			</c:forEach></select></td>
  		</tr>
		</table>
		
	</div>
	</form>
</div>
<form id="setForm" method="post">
<input type="hidden" name="menuId" value="${menuId}" />
<u:input type="hidden" id="clsCd" value="${clsCd}" />

<u:listArea noBottomBlank="true" style="">
<tr id="R">
	<td class="head_ct" width="10%"><u:msg titleId="cols.itemNm" alt="항목명" /></td>
	<c:forEach var="colmVo" items="${authList }" varStatus="status">
		<td class="head_ct head_${colmVo.va }_Chk" ><a href="javascript:;" onclick="checkAll('${colmVo.va }',this);" title="<u:msg titleId='cm.check.all' />" data-checked="N">${colmVo.msg }</a></td>
	</c:forEach>
</tr>
<c:forEach items="${cdList}" var="cdVo" varStatus="status">
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
		<td class="body_lt" ><a href="javascript:;" onclick="checkAll('${cdVo.cd }',this);" title="<u:msg titleId='cm.check.all' />" data-checked="N">${cdVo.rescNm }</a></td>
		<c:forEach var="colmVo" items="${authList }" varStatus="status">
			<u:convertMap srcId="listMap" attId="${clsCd}_${cdVo.cd }_${colmVo.va }" var="useYn" />
			<td class="body_ct ${colmVo.va }Chk ${cdVo.cd }Chk">
				<u:checkbox name="useChkYn" value="Y" checkValue="${useYn }" title="${colmVo.msg }" alt="${colmVo.msg }" noLabel="true" onclick="useChk(this, '${colmVo.va }');"/>
				<u:input type="hidden" id="cd" value="${cdVo.cd}" />
				<u:input type="hidden" id="useYn" value="${empty useYn ? 'N' : useYn}" />
				<u:input type="hidden" id="authVa" value="${colmVo.va}" />
			</td>
		</c:forEach>
	</tr>
	</c:forEach>
</u:listArea>

<u:blank />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:save();" auth="A" />
</u:buttonArea>

</form>

