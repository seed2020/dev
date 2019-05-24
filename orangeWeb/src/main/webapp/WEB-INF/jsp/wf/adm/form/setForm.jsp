<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.Date"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="paramsForList" excludes="formNo"
/><u:params var="params" />
<script type="text/javascript">
<!--
<% // [저장] 테이블 저장 %>
function formSave() {
	if (validator.validate('setForm')) {
		var $form = $("#setForm");
		$form.attr('method','post');
		$form.attr('action','./transFormFrm.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form.submit();	
	}
};<%// 저장, 삭제시 리로드 %>
function reloadForm(url){
	if(url != undefined && url != null) location.replace(url);
	else location.replace(location.href);
};<% // [하단버튼:배열] %>
function getRightBtnList(){
	var $area = $("#rightBtnArea");
	return $area.find('ul')[0].outerHTML;
}<% // [하단버튼:목록] %>
function listForm(){
	location.replace($('#listPage').val());
}<% // [양식 사용여부 - 데이터가 1건 이상일 경우 true 리턴, formTyp 이 'R'(본문삽입) 일 경우 첨부파일 사용여부도 리턴] %>
function chkFormTyp(formTyp){
	<c:if test="${empty param.formNo }">return;</c:if>
	callAjax('./chkUseFormAjx.do?menuId=${menuId}', {formNo:'${param.formNo}', formTyp:formTyp}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			// 복원여부[코드]
			var isRestore=data.formUseYn=='Y' || (formTyp=='R' && data.fileUseYn=='Y');
			if(isRestore){
				var msgCd=data.formUseYn=='Y' ? 'wf.msg.form.fnc.insertBody' : 'wf.msg.form.fnc.insertBody';
				alertMsg(msgCd); // wf.msg.form.fnc.insertBody=본문삽입은 해당 양식에 첨부파일을 사용할 수 없습니다.
				$.uniform.restore($('#formFncArea').find("input, textarea, select, button"));
				var chkFormTyp=formTyp=='R' ? 'A' : 'R'; 
				$('#formFncArea').find(':radio[name="formTyp"][value="'+chkFormTyp+'"]').prop('checked', true);
				$('#formFncArea').find("input, textarea, select, button").uniform();
			}
		}
	});
}
$(document).ready(function() {
	setUniformCSS();
	parent.applyFormBtn();
});
//-->
</script>

<form id="setForm" style="padding:10px;">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="listPage" value="./${listPage}.do?${paramsForList}" />
<u:input type="hidden" id="viewPage" value="./setFormFrm.do?${params}" />
<u:input type="hidden" id="grpId" value="${param.grpId }" />

<c:if test="${!empty param.formNo }"><u:input type="hidden" id="formNo" value="${param.formNo}" /></c:if>

<u:title titleId="wf.jsp.form.base.title" alt="기본정보" type="small" notPrint="true" />
<% // 폼 필드 %>
<u:listArea id="listArea" colgroup="15%,">
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg titleId="wf.cols.form.nm" alt="양식명" /></td>
		<td class="body_lt">
			<table border="0" cellpadding="0" cellspacing="0">
				<tbody>
				<tr>
					<td id="langTypArea">
						<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:convert srcId="${wfFormBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
							<u:set test="${status.first}" var="style" value="width:180px;" elseValue="width:180px; display:none" />
							<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
							<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="wf.cols.form.nm" value="${rescVa}" style="${style}"
								maxByte="120" validator="changeLangSelector('listArea', id, va)" mandatory="Y" />
						</c:forEach>
						<u:input type="hidden" id="rescId" value="${wfFormBVo.rescId}" />
					</td>
					<td id="langTypOptions">
						<c:if test="${fn:length(_langTypCdListByCompId)>1}">
							<select id="langSelector" onchange="changeLangTypCd('listArea','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
							<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
							</c:forEach>
							</select>
						</c:if>
					</td>
				</tr>
				</tbody>
			</table>
		</td>
	</tr><tr>
		<td class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
		<td class="body_lt">
			<u:checkArea>
				<u:radio name="useYn" value="Y" titleId="cm.option.use" alt="사용" checkValue="${wfFormBVo.useYn }"  inputClass="bodybg_lt" checked="${empty wfFormBVo.useYn }"/>
				<u:radio name="useYn" value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${wfFormBVo.useYn }" inputClass="bodybg_lt" />
			</u:checkArea>
		</td>
	</tr><tr>
		<td class="head_lt"><u:msg titleId="wf.cols.allComp.yn" alt="전사여부(공개범위)" /></td>
		<td class="body_lt"><u:checkArea>
				<u:radio name="allCompYn" value="Y" titleId="cm.option.use" alt="사용" checkValue="${wfFormBVo.allCompYn }"  inputClass="bodybg_lt" />
				<u:radio name="allCompYn" value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${wfFormBVo.allCompYn }" inputClass="bodybg_lt" checked="${empty wfFormBVo.allCompYn }" />
			</u:checkArea>
			<div class="color_txt">※ <u:msg titleId="wf.msg.form.allComp" alt="컨텐츠를 전체회사에서 공유합니다." /></div>
		</td>
	</tr><tr>
		<td class="head_lt"><u:msg titleId="wf.btn.screen.list" alt="목록화면" /></td>
		<td class="body_lt"><u:checkArea>
				<u:radio name="lstTypCd" value="D" titleId="wf.option.list.dft" alt="기본형" checkValue="${wfFormBVo.lstTypCd }"  inputClass="bodybg_lt" checked="${empty wfFormBVo.lstTypCd }"/>
				<u:radio name="lstTypCd" value="I" titleId="wf.option.list.img" alt="앨범형" checkValue="${wfFormBVo.lstTypCd }" inputClass="bodybg_lt" />
			</u:checkArea>
			<div class="color_txt">※ <u:msg titleId="wf.msg.form.list.screen" alt="목록화면을 선택합니다." /></div>
		</td>
	</tr><tr>
		<td class="head_lt"><u:msg titleId="wf.cols.form.fnc" alt="기능" /></td>
		<td class="body_lt"><u:set var="fncDisabled" test="${!empty isUseForm && isUseForm==true}" value="Y" elseValue="N"/><u:checkArea id="formFncArea">
			<u:radio name="formTyp" value="R" titleId="wf.cols.form.fnc.insertBody" alt="본문삽입" checkValue="${wfFormBVo.formTyp }" inputClass="bodybg_lt" onclick="chkFormTyp('R');" disabled="${fncDisabled }"/>
			<u:radio name="formTyp" value="A" titleId="wf.cols.form.fnc.regMnu" alt="메뉴등록" checkValue="${wfFormBVo.formTyp }"  inputClass="bodybg_lt" onclick="chkFormTyp('A');" checked="${empty wfFormBVo.formTyp }" disabled="${fncDisabled }"/>				
			</u:checkArea>
			<div class="color_txt">※ <u:msg titleId="wf.msg.form.fnc.insertBody" alt="본문삽입은 해당 양식에 첨부파일을 사용할 수 없습니다. " /></div>
		</td>
	</tr><tr>
		<td class="head_lt"><u:msg titleId="wf.cols.md" alt="외부모듈" /></td>
		<td class="body_lt"><select name="mdTypCd" style="min-width:100px;" <u:elemTitle titleId="wf.cols.md" />>
		<u:option value="no" titleId="cm.option.noSelect" alt="선택안함" selected="${empty wfFormBVo.mdTypCd }" 
		/><c:forEach items="${pageRecSetupCdList}" var="pageRecSetupCd" varStatus="status"
		><c:if test="${pageRecSetupCd.cd ne '_ALL' }"><u:option value="${pageRecSetupCd.cd }" title="${pageRecSetupCd.rescNm}" checkValue="${wfFormBVo.mdTypCd }" 
		/></c:if></c:forEach></select>
		<div class="color_txt">※ <u:msg titleId="wf.msg.form.mdTypCd" alt="컨텐츠의 크기 제한, 파일 크기제한, 목록갯수 등 해당 모듈의 설정을 사용합니다." /></div>
		</td>
	</tr><tr>
	<td class="head_lt"><u:msg titleId="wf.cols.form.desc" alt="양식 설명" /></td>
	<td class="body_lt"><u:textarea id="formDesc" titleId="wf.cols.form.desc" maxByte="200" style="width:95%" rows="5" value="${wfFormBVo.formDesc }"/></td>
	</tr>
	
</u:listArea>
</form>
<% // 하단 버튼 %>
<u:buttonArea id="rightBtnArea" style="display:none;">
	<u:button id="btnSave" href="javascript:formSave();" titleId="cm.btn.save" alt="저장" auth="A" />
	<u:button id="btnCancel" href="javascript:listForm();" titleId="cm.btn.cancel" alt="취소" auth="A" />
</u:buttonArea>