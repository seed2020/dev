<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%// 추가된 조직정보 %>
function getOrgIds(){
	var data = [];
	$("#orgListArea input[name='orgId']").each(function(){
		data.push({orgId:$(this).val()});	
	});
	return data;
};<%// 전체제거 %>
function removeAllOrgRows(){
	var arr = [];
	$("#orgListArea input[name='orgId']").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0) return;
	arr.each(function(index, tr){
		$(tr).remove();
	}, true);
};<%// 선택제거 %>
function removeOrgRows(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr==null) return;
	arr.each(function(index, tr){
		$(tr).remove();
	}, true);
};<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(noSelectMsg){
	var arr=[], id, obj;
	$("#orgListArea tbody:first input[type='checkbox']:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
};<% // [버튼:선택] 조직도 %>
function selectOrgPop(){
	var data = getOrgIds();
	<% // option : data, multi, withSub, titleId %>
	searchOrgPop({data:data, multi:true, withSub:false, mode:'search'}, function(arr){
		if(arr!=null){
			removeAllOrgRows();
			var $tr, $hiddenTr = $("#orgListArea tbody:first #hiddenTr");
			var html = $hiddenTr[0].outerHTML;
			arr.each(function(index, orgVo){
				$hiddenTr.before(html);
				$tr = $hiddenTr.prev();
				$tr.attr('id','tr'+orgVo.orgId);
				$tr.find("input[type='checkbox']").val(orgVo.orgId);
				$tr.find("td#orgNm").text(orgVo.rescNm);
				$tr.find("input[name='orgId']").val(orgVo.orgId);
				$tr.show();
				setJsUniform($tr[0]);
			});
		}
	});
}<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
};<% // 저장 - 버튼 클릭 %>
function resetSave(){
	var arr = [];
	$("#orgListArea input[name='orgId']").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	
	if(arr.length==0){
		validator.removeHandler('orgDocNo');
	}
	if (validator.validate('setDocNoResetForm')) {
		var $form = $("#setDocNoResetForm");
		$form.attr('method','post');
		$form.attr('action','./transDocNoReset.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
};
//-->
</script>
<div style="width:350px;">
<form id="setDocNoResetForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="storId" value="${dmStorBVo.storId}" />
<u:input type="hidden" id="listPage" value="./listStor.do?${nonPageParams}" />
<u:title titleId="dm.option.comp" alt="회사" type="small" />
<% // 폼 필드 %>
<u:listArea colgroup="27%,73%">
	<tr>
		<td class="head_lt"><u:msg titleId="dm.cfg.strtNo" alt="시작번호" /></td>
		<td class="bodybg_lt" colspan="3"><u:input id="compDocNo" titleId="dm.cols.docNo" value="${compDocSeq }" maxByte="8" mandatory="Y" style="width:50px;" valueOption="number" /></td>
	</tr>
</u:listArea>
<u:blank />
<c:if test="${isOrgChk == true }">
<u:title titleId="dm.option.org" alt="조직" type="small" />
<% // 폼 필드 %>
<u:listArea colgroup="27%,73%">
	<tr>
		<td class="head_lt"><u:msg titleId="dm.cfg.strtNo" alt="시작번호" /></td>
		<td class="bodybg_lt" colspan="3"><u:input id="orgDocNo" titleId="dm.cols.docNo" value="" maxByte="8" mandatory="Y" style="width:50px;" valueOption="number" 
		/><u:buttonS href="javascript:selectOrgPop();" titleId="cm.btn.sel" alt="선택" auth="A" 
		/><u:buttonS href="javascript:removeOrgRows();" titleId="cm.btn.del" alt="삭제"  auth="A"
		/></td>
	</tr>
	<tr>
		<td colspan="3">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tbody>
				<tr>
					<td>
						<div style="width:100%;height:160px;overflow-y:auto;">
							<div id="orgListArea" class="listarea" style="width:95%; padding:5px;">
								<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
								<tr id="headerTr">
									<th width="36"  class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('orgListArea', this.checked);" value=""/></th>
									<th class="head_ct"><u:msg titleId="cols.deptNm" alt="부서명" /></th>
								</tr>
								<c:if test="${!empty dmDocNoDVoList}">
									<c:forEach var="list" items="${dmDocNoDVoList}" varStatus="status">
										<u:set test="${status.last}" var="trDisp" value="display:none" />
										<u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${status.count}" />
										<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="${trId}" style="${trDisp}">
											<td width="32" class="bodybg_ct"><input type="checkbox" value="" /><input type="hidden" name="orgId" /></td>
											<td class="body_ct" id="orgNm"></td>
										</tr>
									</c:forEach>
								</c:if>
								</table>
							</div>
						</div>
					</td>
				</tr>
				</tbody>
			</table>
		</td>
	</tr>
</u:listArea>
<u:blank />
</c:if>
<c:if test="${isOrgChk == true }">
<div class="color_txt"><u:msg titleId="dm.msg.docNo.org.reset" alt="* 조직을 선택하지 않으면 일괄변경됩니다."/></div>
</c:if>
<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" onclick="resetSave();" auth="A" />
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>

</form>
</div>