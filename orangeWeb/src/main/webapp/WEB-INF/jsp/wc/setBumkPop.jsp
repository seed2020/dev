<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">

function bumkDel(){
	if (confirmMsg("cm.cfrm.del")) {	<% // 삭제하시겠습니까? %>
		var aa = {fncCal:$("#setBumkForm #fncCal").val(), bumkId:$("#setBumkForm #bumkId").val()};	
		var param = $.parseJSON(JSON.stringify(aa));
			
		callAjax('./transSetBumkPopDel.do?menuId=${menuId}&fncCal=open', param, function(data) {
			if (data.result == 'ok') {
				if (data.message != null) {
					alert(data.message);
					location.href="/wc/listNewSchdl.do?fncCal=open&menuId=${menuId}&tabNo="+$('#tabNo').val();
					dialog.close('setBumkPop');
				}
			}
		});
	}
}

function formSubit(){
	
	if(validator.validate('setBumkForm')){
		
		var aa = {bumkNm:$("#setBumkForm #bumkNm").val(),
				fncCal:$("#setBumkForm #fncCal").val(), 
				fncCalSub:$("#setBumkForm #fncCalSub").val(), 
				viewOrgId:$("#setBumkForm #viewOrgId").val(), 
				viewUserUid:$("#setBumkForm #viewUserUid").val(),
				viewUserNm:$("#setBumkForm #viewUserNm").val(),
				viewOrgNm:$("#setBumkForm #viewOrgNm").val(),
				bumkId:$("#setBumkForm #bumkId").val()};	
		var param = $.parseJSON(JSON.stringify(aa));
			
		callAjax('./transSetBumkPopSave.do?menuId=${menuId}&fncCal=${fncCal}', param, function(data) {
			if (data.result == 'ok') {
					//$('#bumk').append("<option value='"+($("#fncCal").val()=="psn"?$("#viewUserUid").val():$("#viewOrgId").val())+"'>"+$("#bumkNm").val()+"</option>");
					if (data.message != null) {
						alert(data.message);
						location.href="/wc/listNewSchdl.do?fncCal=open&menuId=${menuId}&tabNo="+$('#tabNo').val();
						dialog.close('setBumkPop');
					}
				
			}else{
				if (data.message != null) {
					alert(data.message);
					
				}
			}
		});

	}	
}


</script>



<div style="width:380px">
<form id="setBumkForm"  method="post" >
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="fncCalSub" value="${fncCalSub}" />
<u:input type="hidden" id="fncCal" value="${fncCal}" />
<u:input type="hidden" id="viewOrgId" value="${viewOrgId}" />
<u:input type="hidden" id="viewUserUid" value="${viewUserUid}" />
<u:input type="hidden" id="viewUserNm" value="${viewUserNm}" />
<u:input type="hidden" id="viewOrgNm" value="${viewOrgNm}" />
<u:input type="hidden" id="bumkId" value="${bumkId}" />


<% // 폼 필드 %>
<u:listArea >
	<tr>
		<td width="25%" class="head_lt"><u:mandatory /><u:msg titleId="wc.cols.bumkNm" alt="즐겨찾기명" /></td>
		<td width="75%">
			<c:if test="${bumkDvo.bumkDispNm != null}">
				<c:set var="bumkNm" value="${bumkDvo.bumkDispNm}" />
			</c:if>
			<c:if test="${bumkDvo.bumkDispNm == null}">
				<u:set test="${empty viewUserNm}" var="bumkNm" value="${viewOrgNm}" elseValue="${viewUserNm}" />
			</c:if>
			<u:input id="bumkNm" value="${bumkNm }" titleId="wc.cols.bumkNm" style="width: 265px;" mandatory="Y" maxByte="50"/>
		</td>
	</tr>
	
	<tr>
		<td width="25%" class="head_lt"><u:mandatory /><u:msg titleId="wc.cols.bumkTgtNm" alt="즐겨찾기대상" /></td>
		<td width="75%">
		<c:if test="${bumkDvo != null}">
			<c:if test="${bumkDvo.userDeptTypCd == '1'}">
				<c:set var="bumkTgtNm" value="${bumkDvo.bumkTgtNm}" />
			</c:if>
			<c:if test="${bumkDvo.userDeptTypCd == '2'}">
				<c:set var="bumkTgtNm" value="${bumkDvo.bumkTgtDeptNm}" />
			</c:if>
		</c:if>
		<c:if test="${bumkDvo == null}">
			<u:set test="${empty viewUserNm}" var="bumkTgtNm" value="${viewOrgNm}" elseValue="${viewUserNm}" />
		</c:if>
			<u:input id="bumkTgtNm" value="${bumkTgtNm}" titleId="wc.cols.bumkTgtNm" style="width: 265px;"  readonly="Y"/>		
		</td>
	</tr>
</u:listArea>


<u:buttonArea>
	<c:if test="${bumkDvo != null}">
		<u:button titleId="cm.btn.del" onclick="javascript:bumkDel();" alt="삭제" auth="R" />
	</c:if>
	<u:button titleId="cm.btn.save" onclick="javascript:formSubit();" alt="저장" auth="R" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>
