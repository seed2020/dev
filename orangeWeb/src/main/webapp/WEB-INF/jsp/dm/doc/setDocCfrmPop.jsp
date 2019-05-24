<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%// [선택] 수동입력 %>
function docNoMnalYnChk(obj){
	$docNo = $('#setCfrmForm').find("[name='docNo']");
	//$docNo.val('');
	setDisabled($docNo, !obj.checked);
	//var docTxt = $('#setCfrmForm #docNoTxt');
	//if(obj.checked) docTxt.hide();
	//else docTxt.show();
}<%// [선택] 버전 %>
function verSelect(obj, verVa){
	if($(obj).val() == 'mnal' ){
		$('#cfrmListArea #verVa').val('${dmDocLVo.verVa}');
		$('#cfrmListArea #verVaTr').show();
	}else{
		$('#cfrmListArea #verVa').val(verVa);
		$('#cfrmListArea #verVaTr').hide();
	}
}<%// [확인] 저장 %>
function applyCfrm(){
	if(validator.validate('setCfrmForm')){
		var arr=[];
		$('#cfrmListArea').find("input[type='text'],input[type='radio']:checked").each(function(){
			if($(this).attr('disabled')!='disabled'){
				arr.push({name:$(this).attr('name'),value:$(this).val()});
			}
		});
		if(arr.length==0){
			return null;
		}
		saveCfrmOk(arr);
	}
}
$(document).ready(function() {
});
//-->
</script>
<div style="width:500px;">
<form id="setCfrmForm">

<u:listArea id="cfrmListArea" colgroup="15%," style="height:70px;">
<c:choose>
	<c:when test="${!empty param.docId && (dmDocLVo.statCd eq 'C' || dmDocLVo.statCd eq 'M')}">
		<c:if test="${!empty param.docNoMod }">
			<tr>
				<td class="head_lt"><u:mandatory /><u:msg titleId="dm.cols.docNo" alt="문서번호" /></td>
				<td class="bodybg_lt" ><u:input type="text" id="docNo" titleId="dm.cols.docNo" value="${dmDocLVo.docNo }" mandatory="Y" maxByte="55" style="width:200px;" /></td>
			</tr>
		</c:if>
		<tr>
			<td class="head_lt" rowspan="${envConfigMap.verMnalYn eq 'Y' ? 2 : 1 }"><u:mandatory /><u:msg titleId="dm.cols.verNo" alt="버전번호" /></td>
			<td class="bodybg_lt" >
			<u:set var="verFrtTmp" test="${dmDocLVo.verVa%1 == 0}" value="${dmDocLVo.verVa+0.1 }" elseValue="${dmDocLVo.verVa }"/>
			<fmt:formatNumber var="verDft" value="${dmDocLVo.verVa}" type="pattern" pattern="0.0" />
			<fmt:formatNumber var="verFrt" value="${verFrtTmp+(1-(verFrtTmp%1))%1}" type="pattern" pattern="0.0" />
			<fmt:formatNumber var="verRear" value="${dmDocLVo.verVa+envConfigMap.verRear}" type="pattern" pattern="0.0" />
			<u:msg var="verDftTitle" titleId="dm.msg.serDoc.verDft" arguments="${verDft}"/>
			<u:msg var="verFrtTitle" titleId="dm.msg.serDoc.verFrt" arguments="${verFrt }"/>
			<u:msg var="verRearTitle" titleId="dm.msg.serDoc.verRear" arguments="${verRear }"/>
			<u:checkArea>
				<c:if test="${envConfigMap.verMnalYn eq 'Y' }">
					<u:radio name="verVaChk" value="mnal" titleId="dm.cfg.verMnalYn" onclick="verSelect(this);"/>
				</c:if>
				<u:radio name="verVaChk" value="curr" title="${verDftTitle }" onclick="verSelect(this,'${verDft }');" checked="true"/>
				<u:radio name="verVaChk" value="frt" title="${verFrtTitle }" onclick="verSelect(this,'${verFrt }');"/>
				<u:radio name="verVaChk" value="rear" title="${verRearTitle }" onclick="verSelect(this,'${verRear }');" />
			</u:checkArea>
			</td>
		</tr>
		<tr id="verVaTr" style="display:none;">
			<td>
				<u:input type="text" id="verVa" titleId="dm.cfg.verNo" value="${dmDocLVo.verVa }" mandatory="Y" valueOption="number" valueAllowed="." readonly="${envConfigMap.verMnalYn eq 'Y' ? 'N' : 'Y'}" onblur="isChkFloat(this);" />
			</td>
		</tr>
	</c:when>
	<c:otherwise>
		<tr>
			<td class="head_lt"><u:mandatory /><u:msg titleId="dm.cols.docNo" alt="문서번호" /></td>
			<td class="bodybg_lt" ><u:checkArea>
				<td><u:input type="text" id="docNo" titleId="dm.cols.docNo" value="${dmDocLVo.docNo }" mandatory="Y" disabled="Y" style="width:200px;"/><!-- <span id="docNoTxt" class="color_txt"> + 일련번호</span> --></td>
				<c:if test="${envConfigMap.docNoMnalYn eq 'Y' }"><u:checkbox name="docNoMnalYn" value="Y" titleId="dm.cfg.docNoMnalYn" alt="수동입력여부" onclick="docNoMnalYnChk(this);"/></c:if>
				</u:checkArea></td>
		</tr>
		<tr>
			<td class="head_lt"><u:mandatory /><u:msg titleId="dm.cols.verNo" alt="버전번호" /></td>
			<td class="bodybg_lt" ><u:input type="text" id="verVa" titleId="dm.cfg.verNo" value="${dmDocLVo.verVa }" valueOption="number" valueAllowed="." readonly="${envConfigMap.verMnalYn eq 'Y' ? 'N' : 'Y'}" /></td>
		</tr>
	</c:otherwise>
</c:choose>
</u:listArea>
<div class="color_txt">
	<c:if test="${empty param.docId && envConfigMap.docNoMnalYn eq 'Y' }"><u:msg titleId="dm.msg.setDocCfrm.tx01" alt="* 수동입력여부를 체크해제하면 문서번호를 직접 입력할 수 있습니다."/></c:if>
	<c:if test="${empty envConfigMap.docNoMnalYn || envConfigMap.docNoMnalYn eq 'N' }"><u:msg titleId="dm.msg.setDocCfrm.tx02" alt="* 문서번호는 자동채번 됩니다."/></c:if>
	<%-- <c:if test="${!empty param.docPid }"><u:msg titleId="dm.msg.setDocCfrm.tx03" alt="* 하위문서는 상위문서번호를 기준으로 직접 입력할 수 있습니다."/></c:if> --%>
</div>
</form>	
<u:buttonArea>
	<u:button titleId="cm.btn.confirm" alt="확인" href="javascript:applyCfrm();" auth="W" />
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>

</div>