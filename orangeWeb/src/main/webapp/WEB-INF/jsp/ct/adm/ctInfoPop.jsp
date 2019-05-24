<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
var i=0;
var pasInt =0;

$(function(){
	var $reqsCtId = $("#reqsCtId").val();
	$("#rjtCtId").val($reqsCtId);
	
});

<% // 폼 전송 %>
function formSubmit(){
	var $selectCtClose = $("#selectCtCloseList");
	var selectCloseCtIds = [];
	var $closeOp = $("#rjtOpCont").val();
	var $checkedLength = $('input:checkbox:checked').length;
	var $ctCloseListTbl = $("#listArea");
	var $apprYnVal = '${apprYn}';
	if($checkedLength == '0'){
		alert("<u:msg titleId="ct.msg.noSelectCt" alt="커뮤니티를 선택해주시기 바랍니다." />");
	}else{
		
		$ctCloseListTbl.find("tr[name='ctCloseVo']").each(function(){
			var $selectedCloseCtId = $(this).find("#closeCtId").val();
			$(this).find("#checkFlag").each(function(){
				if($(this).is(":checked")||$(this).attr("checked") == "checked"){
					if($(this).attr("tr")!='headerTr'){
						selectCloseCtIds.push($selectedCloseCtId);
					}
				}
			});

		});
		if($apprYnVal == 'Y'){
			if(confirmMsg("ct.cfrm.close")){
				callAjax('./setCloseAppr.do?menuId=${menuId}', {selectCloseCtIds:selectCloseCtIds, closeOp:$closeOp, signal:$apprYnVal}, function(data){
					if (data.message != null) {
						alert(data.message);
					}
					if (data.result == 'ok') {
						location.href = './listCloseReqs.do?menuId=${menuId}';
					}
				});
			}
		}else{
			if(confirmMsg("ct.cfrm.closeRjt")){
				callAjax('./setCloseAppr.do?menuId=${menuId}', {selectCloseCtIds:selectCloseCtIds, closeOp:$closeOp, signal:$apprYnVal}, function(data){
					if (data.message != null) {
						alert(data.message);
					}
					if (data.result == 'ok') {
						location.href = './listCloseReqs.do?menuId=${menuId}';
					}
				});
			}
		}
	}
}

$(document).ready(function() {
	setUniformCSS();
});

//-->
</script>

<div style="width:500px">
<form id="setRjtOpForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<input id="setSchdlGrp" name="setSchdlGrp" value="setSchdlGrp" type="hidden"/>

<% // 폼 필드 %>

<div class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="width:100%;table-layout:fixed;">
		<tr>
			<td class="head_ct" width="30%">
				<u:msg titleId="cols.cat" alt="카테고리" />
			</td>
			<td class="body_lt">
				<u:out value="${ctEstbBVo.catNm}"/>
			</td>
		</tr>
		<tr>
			<td class="head_ct" >
				<u:msg titleId="cols.cmNm" alt="커뮤니티명" />
			</td>
			<td class="body_lt">
				<u:out value="${ctEstbBVo.ctNm}"/>
			</td>
		</tr>
		<tr>
			<td class="head_ct" style=height:50px;">
				<u:msg titleId="cols.cmItro" alt="커뮤니티 소개글" />
			</td>
			<td class="body_lt" >
				<div style="word-break:break-all; word-wrap:break-word; overflow:scroll; height:150px;">
					${ctEstbBVo.ctItro}
				</div>
			</td>
		</tr>
		<tr>
			<td class="head_ct">
				<u:msg titleId="cols.kwd" alt="키워드" />
			</td>
			<td class="body_lt">
				<u:out value="${ctEstbBVo.ctKwd}"/>
			</td>
		</tr>
		<tr>
			<td class="head_ct">
				<u:msg titleId="cols.mngTgtYn" alt="관리대상 여부" />
			</td>
			<td class="body_lt">
				<c:if test="${ctEstbBVo.mngTgtYn =='Y'}">YES</c:if>
				<c:if test="${ctEstbBVo.mngTgtYn =='N'}">NO</c:if>
			</td>
		</tr>
		<tr>
			<td class="head_ct">
				<u:msg titleId="cols.joinMet" alt="가입방법" />
			</td>
			<td class="body_lt">
				<c:if test="${ctEstbBVo.joinMet == '1'}"> <u:msg titleId="ct.option.joinMet.join01" alt="즉시가입" /></c:if>
				<c:if test="${ctEstbBVo.joinMet == '2'}"> <u:msg titleId="ct.option.joinMet.join02" alt="마스터 승인 후 가입" /></c:if>
			</td>
		</tr>
		<tr>
			<td class="head_ct">
				<u:msg titleId="cols.dftAuth" alt="기본권한" />
			</td>
			<td class="body_lt">
				<c:if test="${ctEstbBVo.dftAuth == 'A'}" > <u:msg titleId="ct.cols.mbshLev3" alt="준회원" /></c:if>
				<c:if test="${ctEstbBVo.dftAuth == 'R'}" > <u:msg titleId="ct.cols.mbshLev2" alt="정회원" /></c:if>
				<c:if test="${ctEstbBVo.dftAuth == 'S'}" > <u:msg titleId="ct.cols.mbshLev1" alt="스텝" /></c:if>
			</td>
		</tr>
		<%-- <tr>
			<td class="head_ct">
				<u:msg titleId="cols.uathdPublYn" alt="비인증 공개여부" />
			</td>
			<td class="body_ct">
				<c:if test="${ctEstbBVo.extnOpenYn =='Y'}">YES</c:if>
				<c:if test="${ctEstbBVo.extnOpenYn =='N'}">NO</c:if>
			</td>
		</tr> --%>
		<tr>
			<td class="head_ct">
				<u:msg titleId="cols.attSizeLim" alt="첨부용량 제한" />(MB)
			</td>
			<td class="body_lt">
				<c:choose>
					<c:when test="${ctEstbBVo.attLimSize == '-1'}">
						<u:msg titleId="cm.btn.unlim" alt="무제한" />
					</c:when>
					<c:otherwise>
						<u:out value="${ctEstbBVo.attLimSize}(MB)"/>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<c:if test="${fileVoList != null && fn:length(fileVoList) > 0}">
		<tr>
			<td class="head_ct">
				<u:msg titleId="cols.attFile" alt="첨부파일" />
			</td>
			<td class="body_lt">
				<u:files id="ctfiles" fileVoList="${fileVoList}" module="ct" mode="view" />
			</td>
		</tr>
		</c:if>
		
		<tr>
			<td class="head_ct">
				<u:msg titleId="ct.cols.fncList" alt="기능 목록" />
			</td>
			<td class="body_lt">
				<c:forEach var="ctFncVo"  items="${ctFncList}" varStatus="status">
					<u:out value="${ctFncVo.ctFncNm}"/><br>
				</c:forEach>
			</td>
		</tr>
	</table>
</div>
<u:blank/>

<u:buttonArea>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>