<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%// 확인 버튼 클릭 %>
function setApvLn(){
	var param = new ParamMap().getData("setApvLnForm");
	setApvLnData(param, "${param.formApvLnTypCd}");<%// - setFormEdit.jsp %>
	dialog.close("setDocApvLnDialog");
}
<%// 결재방 모양별 최대 갯수 설정 - 3줄 8개, 2줄 5개, 1줄 3개 %>
function checkMaxCell(){
	var $form = $("#setApvLnForm");
	var apvLnDispTypCd = $form.find("[name='${apvLnTitlTypCd}-apvLnDispTypCd']:checked").val();
	if(apvLnDispTypCd=='2row' || apvLnDispTypCd=='1row'){
		var maxCnt = parseInt($form.find("#${apvLnTitlTypCd}-maxCnt option:selected").val());
		if(apvLnDispTypCd=='2row' && maxCnt > 5){
			alertMsg("ap.cmpt.maxCnt.limit", ["5"]);<%//ap.cmpt.maxCnt.limit=최대개수는 {0}개까지 설정 가능 합니다.%>
			$form.find("#${apvLnTitlTypCd}-maxCnt").val("5").trigger("click");
		} else if(apvLnDispTypCd=='1row' && maxCnt > 3){
			alertMsg("ap.cmpt.maxCnt.limit", ["3"]);<%//ap.cmpt.maxCnt.limit=최대개수는 {0}개까지 설정 가능 합니다.%>
			$form.find("#${apvLnTitlTypCd}-maxCnt").val("3").trigger("click");
		}
	}
}
$(document).ready(function() {
	var param = new ParamMap();
	$("#docArea").children("#apvLnArea").each(function(){
		param.getData(this);
	});
	param.removeEmpty().setData("setApvLnForm");
});
//-->
</script>

<div style="width:650px">
<form id="setApvLnForm">

<div style="float:left; width: 4%; text-align: center;"><u:radio name="${apvLnTitlTypCd}-apvLnDispTypCd" value="3row" onclick="checkMaxCell();" checked="true" /></div>
<div style="float:right; width:95%;">
	<label for="${apvLnTitlTypCd}-apvLnDispTypCd3row">
	<table class="approvaltable" border="0" cellpadding="0" cellspacing="1"><tbody>
	<tr>
		<td rowspan="3" class="approval_head"><u:msg titleId="ap.signArea.${apvLnTitlTypCd}" charSeperator="<br/>" alt="결<br/>재" /></td>
		<td class="approval_body"><u:msg titleId="ap.cmpt.signArea.sampleTitle" alt="과장" /></td>
	</tr>
	<tr>
		<td class="approval_img"><img src="${_ctx}/images/etc/etc_s.png"></td>
	</tr>
	<tr>
		<td class="approval_body"><u:msg titleId="ap.cmpt.signArea.sampleDate" alt="12/25" /></td>
	</tr>
	</tbody></table>
	</label>
</div>
<u:blank />

<c:if test="${param.formApvLnTypCd != 'apvLnMultiTopList'}">
<div style="float:left; width: 4%; text-align: center;"><u:radio name="${apvLnTitlTypCd}-apvLnDispTypCd" value="2row" onclick="checkMaxCell();" /></div>
<div style="float:right; width:95%;">
	<label for="${apvLnTitlTypCd}-apvLnDispTypCd2row">
	<table class="approvaltable" border="0" cellpadding="0" cellspacing="1"><tbody>
	<tr>
		<td rowspan="2" class="approval_head"><u:msg titleId="ap.signArea.${apvLnTitlTypCd}" charSeperator="<br/>" alt="결<br/>재" /></td>
		<td rowspan="2" class="approval_body"><u:msg titleId="ap.cmpt.signArea.sampleTitle" alt="과장" /></td>
		<td class="approval_body"><u:msg titleId="ap.cmpt.signArea.sampleDate" alt="12/25" /></td>
	</tr>
	<tr>
		<td class="approval_body"><img src="${_ctx}/images/etc/etc_s.png" height="20"></td>
	</tr>
	</tbody></table>
	</label>
</div>
<u:blank />

<div style="float:left; width: 4%; text-align: center;"><u:radio name="${apvLnTitlTypCd}-apvLnDispTypCd" value="1row" onclick="checkMaxCell();" /></div>
<div style="float:right; width:95%;">
	<label for="${apvLnTitlTypCd}-apvLnDispTypCd1row">
	<table class="approvaltable" border="0" cellpadding="0" cellspacing="1"><tbody>
	<tr>
		<td class="approval_headw"><u:term termId="ap.term.${apvLnTitlTypCd}" alt="결재" /></td>
		<td class="approval_body"><u:msg titleId="ap.cmpt.signArea.sampleTitle" alt="과장" /></td>
		<td class="approval_body"><u:msg titleId="ap.cmpt.signArea.sampleDate" alt="12/25" /></td>
		<td class="approval_body"><img src="${_ctx}/images/etc/etc_s.png" height="20"></td>
	</tr>
	</tbody></table>
	</label>
</div>
<u:blank />
</c:if>

<u:listArea>
	<tr>
	<td width="100" class="head_ct"><u:msg titleId="cm.option.config" alt="설정" /></td>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td class="width12"></td>
		<td class="bodyip_lt"><u:msg titleId="ap.cmpt.maxCnt" alt="최대갯수" /></td>
		<td class="width10"></td>
		<td><select id="${apvLnTitlTypCd}-maxCnt" name="${apvLnTitlTypCd}-maxCnt" onchange="checkMaxCell();"<u:elemTitle titleId="ap.cmpt.maxCnt" alt="최대갯수" />>
			<c:forEach begin="1" end="${signAreaMaxCnt}" var="no"><option value="${no}">${no}</option></c:forEach>
			</select></td>
		<td class="width12"></td>
		<td class="bodyip_lt"><u:msg titleId="ap.cmpt.align" alt="기준위치" /></td>
		<td class="width10"></td>
		<td><select id="${apvLnTitlTypCd}-alnVa" name="${apvLnTitlTypCd}-alnVa"<u:elemTitle titleId="ap.cmpt.align" alt="기준위치" />>
			<option value="right"><u:msg titleId="cm.aln.right" alt="우측 정렬" /></option>
			<option value="left"><u:msg titleId="cm.aln.left" alt="좌측 정렬" /></option>
			</select></td>
		<td class="width13"></td>
		</tr>
		<tr>
		<td class="width12"></td>
		<td class="bodyip_lt"><u:msg titleId="ap.cmpt.border" alt="테두리선" /></td>
		<td class="width10"></td>
		<td><select id="${apvLnTitlTypCd}-bordUseYn" name="${apvLnTitlTypCd}-bordUseYn"<u:elemTitle titleId="ap.cmpt.border" alt="테두리선" />>
			<option value="Y"><u:msg titleId="cm.option.use" alt="사용" /></option>
			<option value="N"><u:msg titleId="cm.option.notUse" alt="사용안함" /></option>
			</select></td>
		<td class="width12"></td>
		<td class="bodyip_lt"><u:msg titleId="ap.cmpt.title" alt="타이틀" /></td>
		<td class="width10"></td>
		<td><select id="${apvLnTitlTypCd}-titlUseYn" name="${apvLnTitlTypCd}-titlUseYn"<u:elemTitle titleId="ap.cmpt.title" alt="타이틀" />>
			<option value="Y"><u:msg titleId="cm.option.use" alt="사용" /></option>
			<option value="N"><u:msg titleId="cm.option.notUse" alt="사용안함" /></option>
			</select></td>
		<td class="width13"></td>
		</tr>
		</tbody></table>
		
		<c:if test="${param.formApvLnTypCd == 'apvLnMultiTopList'}">
		<table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td class="width10"></td>
		<u:checkbox value="Y" name="${apvLnTitlTypCd}-lstDupDispYn" titleId="ap.jsp.setFormEdit.showApvrInListYn" alt="서명란 결재자 목록에 표시 여부" />
		</tr>
		</tbody></table>
		</c:if></td>
	</tr>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" href="javascript:setApvLn();" alt="확인" auth="W" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>
