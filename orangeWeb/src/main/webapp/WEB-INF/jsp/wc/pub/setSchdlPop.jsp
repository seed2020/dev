<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%// 1명의 사용자 선택 %>
function setUserPop(){
	var data = [];<%// data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	var $form=$('#setRegForm');
	if($form.find('input[name="userUid"]').val() != '') data.push({userUid:$form.find('input[name="userUid"]').val()});
	<%// option : data, multi, withSub, titleId %>
	searchUserPop({data:data}, function(userVo){
		if(userVo!=null){
			$form.find('input[name="userUid"]').val(userVo.userUid);
			$form.find('input[name="userNm"]').val(userVo.rescNm);
			$form.find('input[name="subj"]').val(userVo.rescNm);
		}
	});
};
<%// 삭제 %>
function delSchdl(){
	callAjax('./transSchdlDelAjx.do?menuId=${menuId}', {schdlId:'${param.schdlId}'}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if(data.result == 'ok'){
			reloadCalendar();
		}
	});
}
<%// 저장 %>
function saveSchdl(){
	if(validator.validate('setRegForm')){
		var $form=$('#setRegForm');
		$form.attr('method','post');
		$form.attr('action','./transSchdl.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		//editor("cont").prepare();
		$form[0].submit();

		//dialog.close(this);
	}
}
$(document).ready(function () {
});
//-->
</script>

<div style="width:400px;">
	<form id="setRegForm">
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<c:if test="${!empty wcErpSchdlBVo.schdlId }">
		<input id="schdlId" name="schdlId" type="hidden" value="${wcErpSchdlBVo.schdlId }"/>
	</c:if>
	<% // 폼 필드 %>
	<u:listArea noBottomBlank="true" colgroup="25%,75%">
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg titleId="cols.schdlKnd" alt="일정종류"/></td>
		<td>
			<select name="schdlTypCd">
			<c:forEach var="list" items="${wcCatClsBVoList }" varStatus="status">
			<u:option value="${list.cd }" title="${list.rescNm }" alt="${list.rescNm }" checkValue="${wcErpSchdlBVo.schdlTypCd}"/>
			</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg titleId="wc.cols.schdlKndCd" alt="일정대상" /></td>
		<td><u:input type="hidden" id="userUid" value="${wcErpSchdlBVo.userUid}"
		/><u:input id="userNm" titleId="wc.cols.schdlKndCd" readonly="Y"  value="${wcErpSchdlBVo.userNm }" mandatory="Y"
		/><u:buttonS href="javascript:;" titleId="cm.btn.choice" alt="선택" onclick="setUserPop();" />
		</td>
	</tr>
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg titleId="cols.subj" alt="제목" /></td>
		<td><u:input id="subj" value="${wcErpSchdlBVo.subj }" titleId="cols.subj" style="width: 95%;" mandatory="Y" maxByte="120"/></td>
	</tr>
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg titleId="wc.cols.date" alt="일자"/></td>
		<td><fmt:parseDate var="dateStartDt" value="${wcErpSchdlBVo.strtDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
			<fmt:formatDate var="convStartDt" value="${dateStartDt}" pattern="yyyy-MM-dd"/>
			<u:calendar id="startYmd" titleId="wc.cols.date" alt="일자" mandatory="Y" value="${convStartDt }"/></td>
	</tr>
	<tr>
		<td colspan="2"><u:textarea id="cont" value="${wcErpSchdlBVo.cont}" titleId="cols.cont" maxByte="1000" style="width:95.5%;resize:none;" rows="5" /></td>
	</tr>
	</u:listArea>
	
	</form>
	<u:blank />
	<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="javascript:saveSchdl();" alt="저장" auth="A"/>
	<c:if test="${!empty wcErpSchdlBVo.schdlId }"><u:button titleId="cm.btn.del" onclick="javascript:delSchdl();" alt="삭제" auth="A"/></c:if>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
	</u:buttonArea>
</div>
