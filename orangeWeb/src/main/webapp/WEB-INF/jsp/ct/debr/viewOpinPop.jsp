<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%// 의견삭제 %>
function delOpin(debrId, opinOrdr) {
	if(confirmMsg("cm.cfrm.del")){
		callAjax('./transOpinDel.do?menuId=${menuId}&ctId=${ctId}', {debrId:debrId, opinOrdr:opinOrdr}, function(data){
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.href = './listOpin.do?menuId=${menuId}&ctId=${ctId}&debrId='+debrId;
			}
		});
	}
}

<%// 의견수정 %>
function modOpin(debrId, opinOrdr) {
	dialog.open('setOpinPop','<u:msg titleId="ct.cols.opinMod" alt="의견 수정"/>','./setOpinPop.do?menuId=${menuId}&ctId=${ctId}&debrId='+debrId+'&opinOrdr='+opinOrdr);
}

//-->
</script>

<c:set var="topc" value="화면 개발시 JSTL을 사용하십니까?" />
<c:set var="subj" value="VELOCITY가 편하던데요." />
<c:set var="cont" value="VELOCITY가 편하던데요. 다른건 안써봐서 모르겠습니다. 화아팅!" />

<div style="width:700px">
<form id="viewOpinForm">
<u:input type="hidden" id="menuId" value="${menuId}" />

<% // 폼 필드 %>
<div class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
		<colgroup><col width="27%"/><col width="73%"/></colgroup>
		<tr>
		<td class="head_lt"><u:msg titleId="cols.topc" alt="주제" /></td>
		<td class="body_lt">
			<div class="ellipsis" title="<u:out value="${ctDebrBVo.subj}"/>">
				<u:out value="${ctDebrBVo.subj}"/>
			</div>
		</td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:mandatory /><u:msg titleId="cols.subj" alt="의견" /></td>
		<td class="body_lt">
			<div class="ellipsis" title="<u:out value="${ctDebrBVo.subj}"/>">
				<u:out value="${ctDebrOpinDVo.subj}"/>
			</div>
		</td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:mandatory /><u:msg titleId="cols.fna" alt="찬반" /></td>
		<td class="body_lt">
			<c:choose>
				<c:when test="${ctDebrOpinDVo.prosConsCd == 'A'}">
					<u:msg titleId="ct.option.for" alt="친상" />
				</c:when>
				<c:when test="${ctDebrOpinDVo.prosConsCd == 'O'}">
					<u:msg titleId="ct.option.against" alt="반대" />
				</c:when>
				<c:when test="${ctDebrOpinDVo.prosConsCd == 'E'}">
					<u:msg titleId="ct.option.etc" alt="기타" />
				</c:when>
			</c:choose>
		</td>
		</tr>
		
		<tr>
		<td colspan="2" class="body_lt" >
			<div style="overflow:auto;height:302px;" class="editor">${ctDebrOpinDVo.opin}</div>
		</td>
		</tr>
	</table>
</div>

<u:blank />

<u:buttonArea>
	<c:choose>
		<c:when test="${!empty myAuth && myAuth == 'M' }">
			<u:button titleId="cm.btn.mod" alt="수정" onclick="modOpin('${ctDebrOpinDVo.debrId}','${ctDebrOpinDVo.opinOrdr}'); dialog.close(this);" />
			<u:button titleId="cm.btn.del" alt="삭제" onclick="delOpin('${ctDebrOpinDVo.debrId}','${ctDebrOpinDVo.opinOrdr}'); dialog.close(this);" />
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${ctDebrOpinDVo.regrUid == logUserUid}">
					<u:button titleId="cm.btn.mod" alt="수정" onclick="modOpin('${ctDebrOpinDVo.debrId}','${ctDebrOpinDVo.opinOrdr}'); dialog.close(this);" />
					<u:button titleId="cm.btn.del" alt="삭제" onclick="delOpin('${ctDebrOpinDVo.debrId}','${ctDebrOpinDVo.opinOrdr}'); dialog.close(this);" />
				</c:when>
				<c:otherwise>
					<c:if test="${!empty authChkD && authChkD == 'D' }">
						<u:button titleId="cm.btn.del" alt="삭제" onclick="delOpin('${ctDebrOpinDVo.debrId}','${ctDebrOpinDVo.opinOrdr}'); dialog.close(this);" />
					</c:if>
				</c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose>
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>

</form>
</div>
