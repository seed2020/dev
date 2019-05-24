<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><style>
ul.selectList{list-style:none;float:left;margin:0px;padding:0px;}
ul.selectList li{float:left;}
ul.selectList li.optionList{padding-left:5px;}
</style>
<script type="text/javascript">
<!--
<c:if test="${isHst==true}">
<% // 이력 상세보기 %>
function viewRegForm(genId){
	reloadFrame('regFormFrm', './viewRegFormFrm.do?menuId=${menuId}&genId='+genId+'&formNo=${param.formNo}');
}
<% // 이력을 등록화면으로 변경 %>
function chnRegForm(typ){
	callAjax('./transRegFormAjx.do?menuId=${menuId}', {genId:$('#hstGenId').val(), formNo:'${param.formNo}'}, function(data) {
		if (typ=='save' && data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			if(typ=='deploy'){
				deployForm();
			}else{
				reloadForm();
				dialog.close('viewRegFormDialog');
			}
			
		}
	}, null, null, false);
}
<% // [하단버튼:배포] - 선택된 양식을 배포(실제 테이블 구성) %>
function deployForm(genId, formNo){
	callAjax('./transDeployAjx.do?menuId=${menuId}', {formNos:['${param.formNo}']}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			dialog.close('viewRegFormDialog');
			reloadForm();
		}
	});
}
</c:if>
<c:if test="${isAll==true}">
<% // 그룹 선택 %>
function selectGrpList(obj, formNo){
	var target=$(obj).closest('li');
	$(target).nextAll().remove();
	
	// 양식 select
	var formSelect=$('select#paramFormNo');
	formSelect.find('option').not(':first').remove(); // 선택을 제외하고 삭제
	
	// 이력 select
	var hstSelect=$('select#hstGenId');
	hstSelect.find('option').not(':first').remove(); // 선택을 제외하고 삭제
	setJsUniform($('#formContainer'));
	
	viewRegForm(null);
	
	if($(obj).val()=='' || $(obj).val()===undefined) return;
	
	callAjax('./getGrpListAjx.do?menuId=${menuId}', {grpPid:$(obj).val()}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			addGrpList(target, data.wfFormGrpBVoList, $(obj).val());
			setFormList(formSelect, $(obj).val(), formNo);
		}		
	});
}<% // 그룹 추가 %>
function addGrpList(target, wfFormGrpBVoList, grpId){
	if(wfFormGrpBVoList==null || wfFormGrpBVoList.length==0) return;
	var buffer=[];
	var parent=$('<li></li>');
	buffer.push('<select onchange="selectGrpList(this);" style="min-width:100px;">');
	var wfFormGrpBVo;
	buffer.push('<option value="">'+callMsg('cm.select.actname')+'</option>');
	$.each(wfFormGrpBVoList, function(index, item){
		wfFormGrpBVo=item.map;
		buffer.push('<option value="'+wfFormGrpBVo.grpId+'">'+wfFormGrpBVo.grpNm+'</option>');
	});
	buffer.push('</select>');
	
	parent.append($(buffer.join('')));
	
	if(target!=undefined){
		restoreUniform('grpContainer');
		$(target).after(parent);		
		//setJsUniform(parent);
		var container=$('#grpContainer');
		if(container.scrollTop()>0){
			container.css('height', (container.height()+container.scrollTop()+5)+'px');
		}
		applyUniform('grpContainer');
	}
}<% // 양식 조회 %>
function setFormList(formSelect, grpId, formNo){
	callAjax('./getFormListAjx.do?menuId=${menuId}', {grpId:grpId}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') { // 담당자 추가
			addFormList(formSelect, data.wfFormBVoList, formNo);
		}
	});
}<% // 양식 추가 %>
function addFormList(target, wfFormBVoList, formNo){
	if(wfFormBVoList==null || wfFormBVoList.length==0) return;
	$.each(wfFormBVoList, function(index, item){
		wfFormBVo=item.map;
		target.append('<option value="'+wfFormBVo.formNo+'"'+(formNo!=undefined && wfFormBVo.formNo==formNo ? 'selected="selected"' : '')+'>'+wfFormBVo.formNm+'</option>');
	});
	
}<% // 양식 이력 조회 %>
function selectFormHst(obj){
	// 양식 select
	var hstSelect=$('select#hstGenId');
	hstSelect.find('option').not(':first').remove(); // 선택을 제외하고 삭제
	setJsUniform($('#formContainer'));
	
	viewRegForm(null);
	
	if($(obj).val()=='' || $(obj).val()===undefined) return;
	
	callAjax('./getFormHstAjx.do?menuId=${menuId}', {formNo:$(obj).val()}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') { // 담당자 추가
			addFormHst(hstSelect, data.wfFormRegDVoList);
		}
	});
}<% // 양식 추가 %>
function addFormHst(target, wfFormRegDVoList){
	if(wfFormRegDVoList==null || wfFormRegDVoList.length==0) return;
	var wfFormRegDVo;
	$.each(wfFormRegDVoList, function(index, item){
		wfFormRegDVo=item.map;
		target.append('<option value="'+wfFormRegDVo.genId+'">'+wfFormRegDVo.regDt.substring(0,19)+'</option>');
	});
	
}
<% // 이력 상세보기 %>
function viewRegForm(genId){
	if(genId==null){		
		reloadFrame('regFormFrm', '/cm/util/reloadable.do');
		return;
	}
	var formNo=$('#paramFormNo').val();
	if(formNo=='') return;	
	reloadFrame('regFormFrm', './viewRegFormFrm.do?menuId=${menuId}&genId='+genId+'&formNo='+formNo);
}
function setRegForm(){
	var formNo=$('#paramFormNo').val();
	var genId=$('#hstGenId').val();
	if(formNo=='' || genId=='') return;
	setRegPage(formNo, genId);
}
</c:if>

<c:if test="${isPreview==true}">
<% // 미리보기 %>
function previewRegForm(){
	reloadFrame('regFormFrm', './viewRegFormFrm.do?menuId=${menuId}&formNo=${param.formNo}&previewYn=Y');
} 
</c:if>
$(document).ready(function(){
	<c:if test="${isHst==true && !empty firstGenId }">viewRegForm('${firstGenId}');</c:if>
	<c:if test="${isPreview==true && !empty param.formNo}">previewRegForm();</c:if>
	<c:if test="${isAll==true}">
		var grpSelect = $('#grpContainer select:last');
		var grpId=grpSelect.val();
		if(grpId==''){
			var obj=$(grpSelect).closest('li');
			var val=null;
			$.each($(obj).prevAll(), function(){
				val=$(this).find('select > option:selected').val();
				if(val!=''){
					grpSelect=$(this).find('select').eq(0);
					return false;
				}
			});
		} 
		selectGrpList(grpSelect, '${param.formNo}');
		<c:if test="${!empty param.formNo}">selectFormHst($('#formContainer select#paramFormNo'));</c:if>
	</c:if>		
});
//-->
</script>
<div style="width:910px;">
<c:if test="${isAll==true }"><div id="grpContainer" style="float:left;width:50%;"
><ul id="grpArea" class="selectList"><c:forEach items="${paramGrpList}" var="wfFormGrpBVoList" varStatus="paramStatus"
><li>	<select onchange="selectGrpList(this);" style="min-width:60px;">
<u:option value="" titleId="wf.option.grp" alt="그룹"
/><c:forEach items="${wfFormGrpBVoList}" var="wfFormGrpBVo" varStatus="status"><u:option value="${wfFormGrpBVo.grpId }" title="${wfFormGrpBVo.grpNm }" checkValue="${empty paramGrpIds ? param.grpId : paramGrpIds[paramStatus.index]}"/></c:forEach>
</select>
</li>
</c:forEach>
</ul></div><div id="formContainer" style="float:right;width:50%;text-align:right;"
><select id="paramFormNo" name="paramFormNo" style="min-width:160px;" <u:elemTitle titleId="cols.regDt" /> onchange="selectFormHst(this);"><u:option value="" titleId="wf.option.form" alt="양식"
/></select><select id="hstGenId" name="hstGenId" style="min-width:160px;" <u:elemTitle titleId="cols.regDt" /> onchange="viewRegForm(this.value);"><u:option value="" titleId="wf.option.hst" alt="이력"
/></select></div></c:if>

<c:if test="${isHst==true && !empty wfFormRegDVoList }"><div style="text-align:right;"><select id="hstGenId" name="hstGenId" style="min-width:160px;" <u:elemTitle titleId="cols.regDt" /> onchange="viewRegForm(this.value);">
<c:forEach var="wfFormRegDVo" items="${wfFormRegDVoList }" varStatus="status"
><u:out var="regDt" value="${wfFormRegDVo.regDt }" type="longdate"
/><u:option value="${wfFormRegDVo.genId }" title="${regDt }" selected="${status.first }" 
/></c:forEach></select></div></c:if>

<iframe id="regFormFrm" name="regFormFrm" src="/cm/util/reloadable.do" style="width:100%;min-height:510px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>

<u:buttonArea topBlank="true">
	<c:if test="${isAll==true}"
	><u:button titleId="cm.btn.confirm" onclick="setRegForm();" alt="확인" 
	/><u:button titleId="cm.btn.turnBack" onclick="reloadForm();" alt="되돌리기" 
	/></c:if><c:if test="${isHst==true && !empty wfFormRegDVoList }"
	><u:button titleId="cm.btn.save" onclick="chnRegForm('save');" alt="저장" 
	/><u:button titleId="wf.btn.hst.saveAndeploy" onclick="chnRegForm('deploy');" alt="저장 및 배포" /></c:if>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</div>