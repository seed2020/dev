<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<% // [목록:사용] 사용여부 체크 이벤트 %>
function checkUseYn(idx) {
	var disabled = $('#use_'+idx).is(':checked') == false;
	setDisabled($('#reg_'+idx), disabled);
	setDisabled($('#mod_'+idx), disabled);
	setDisabled($('#read_'+idx), disabled);
	setDisabled($('#list_'+idx), disabled);
}
<% // [팝업:저장] 테이블 저장 %>
function saveDisp() {
	var i, yn, max = parseInt('${fn:length(baColmDispDVoList)}');
	for (i = 1; i < max+1; i++) {
		//yn = $('#use_'+i).is(':checked') ? 'Y' : 'N'; $('#useYn_'+i).val(yn);
		//yn = $('#reg_'+i).is(':checked') ? 'Y' : 'N'; $('#regDispYn_'+i).val(yn);
		//yn = $('#mod_'+i).is(':checked') ? 'Y' : 'N'; $('#modDispYn_'+i).val(yn);
		yn = $('#read_'+i).is(':checked') ? 'Y' : 'N'; $('#readDispYn_'+i).val(yn);
		yn = $('#list_'+i).is(':checked') ? 'Y' : 'N'; $('#listDispYn_'+i).val(yn);
		yn = $('#mandatory_'+i).is(':checked') ? 'Y' : 'N'; $('#mandatoryYn_'+i).val(yn);
	}
	var $form = $('#setColmMngForm');
	$form.attr('method','post');
	$form.attr('action','./transDisp.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form[0].submit();
}
<%// 등록/조회 선택시 필수여부 disabled 변경 %>
function readDispChn(obj, id, atrbId){
	var mandatory=$('#mandatory'+id);
	if(!obj.checked && mandatory.prop('checked')==true)
		mandatory.trigger('click');
	setDisabled(mandatory, !obj.checked);
	if(obj.checked && atrbId!='' && atrbId=='subj' && mandatory.prop('checked')==false){
		mandatory.trigger('click');
	}
}
<%// 테이블헤더의 제목 클릭 - 전체선택%>
function checkAll(cd,obj){
	var checked = $(obj).attr('data-checked') == 'N' ? true : false;
	$(obj).attr('data-checked',checked ? 'Y' : 'N');
	$chks = $("#setColmMngForm td."+cd+"Chk input[type='checkbox']").not(':disabled');
	$chks.each(function(){
		if($(this).prop('checked')!=checked)
			$(this).trigger('click');
	});
}
//-->
</script>
<div style="width:400px">
<form id="setColmMngForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="brdId" value="${param.brdId}" />
<u:input type="hidden" id="dialog" value="setColmMngPop" />

<% // 폼 필드 %>
<c:set var="notColNmList" value="SUBJ"/>
<u:listArea colgroup="20%,,17%,17%">
	<tr>
	<td class="head_ct"><u:msg titleId="cols.tblColmNm" alt="테이블컬럼명" /></td>
	<td class="head_ct"><u:msg titleId="cols.itemNm" alt="항목명" /></td>
	<td class="head_ct"><a href="javascript:;" onclick="checkAll('read',this);" title="<u:msg titleId='cm.check.all' />" data-checked="N"><u:msg titleId="cols.regAndRead" alt="등록/조회" /></a></td>
	<td class="head_ct"><a href="javascript:;" onclick="checkAll('mandatory',this);" title="<u:msg titleId='cm.check.all' />" data-checked="N"><u:msg titleId="cm.ico.mandatory" alt="필수입력" /></a></td>
	<td class="head_ct"><a href="javascript:;" onclick="checkAll('list',this);" title="<u:msg titleId='cm.check.all' />" data-checked="N"><u:msg titleId="cols.list" alt="목록" /></a></td>
	</tr>

<c:if test="${fn:length(baColmDispDVoList) == 0}">
	<tr>
	<td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(baColmDispDVoList) > 0}">
	<c:forEach items="${baColmDispDVoList}" var="dispVo" varStatus="status">
	<c:set var="disable1" value="N" /><c:set var="disable2" value="N" />
	<c:set var="colmIndex" value="_${status.index+1}" />
	<c:if test="${dispVo.colmVo.exColmYn != 'Y'}">
		<c:if test="${fn:contains(notColNmList, dispVo.colmVo.colmNm)}"><c:set var="disable1" value="N" /><c:set var="disable2" value="N" /></c:if>
		<c:if test="${dispVo.colmVo.colmNm == 'CONT'}"><c:set var="disable1" value="N" /><c:set var="disable2" value="Y" /></c:if>
	</c:if>
	<c:if test="${dispVo.colmVo.exColmYn == 'Y'}">
		<c:set var="disable1" value="N" /><c:set var="disable2" value="N" />
	</c:if>
	<tr>
	<td class="body_lt">${dispVo.colmVo.colmNm}</td>
	<td class="body_lt">${dispVo.colmVo.rescNm}</td>
	<td class="bodybg_ct readChk"><u:checkbox id="read${colmIndex}" name="read" value="Y" checkValue="${dispVo.readDispYn}" disabled="${disable1}" onclick="readDispChn(this, '${colmIndex}', '${dispVo.atrbId }')"/></td>
	<td class="bodybg_ct mandatoryChk"><u:checkbox id="mandatory${colmIndex}" name="mandatory" value="Y" checkValue="${dispVo.mandatoryYn}" disabled="${dispVo.readDispYn ne 'Y' ? 'Y' : 'N'}" /></td>
	<td class="bodybg_ct listChk"><u:checkbox id="list${colmIndex}" name="list" value="Y" checkValue="${dispVo.listDispYn}" disabled="${disable2}" />
		<u:input type="hidden" id="colmId${colmIndex}" name="colmId" value="${dispVo.colmId}" />
		<u:input type="hidden" id="readDispYn${colmIndex}" name="readDispYn" value="${dispVo.readDispYn}" />
		<u:input type="hidden" id="listDispYn${colmIndex}" name="listDispYn" value="${dispVo.listDispYn}" />
		<u:input type="hidden" id="mandatoryYn${colmIndex}" name="mandatoryYn" value="${dispVo.mandatoryYn}" />
		</td>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>

</form>
<div class="color_txt">※ <u:msg titleId="bb.jsp.setColmMng.tx01" alt="컬럼 갯수를 6개 이상 설정할경우 화면에 보여지는 목록이 부자연스러울수 있습니다." /></div>
<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="saveDisp();" alt="저장" auth="A" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</div>