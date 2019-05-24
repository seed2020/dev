<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

function join(ctId){
	 if(confirmMsg("ct.cfrm.join")) { <% // ct.cfrm.join=가입 신청하시겠습니까? %>
			callAjax('./setCmntJoin.do?menuId=${menuId}', {ctId:ctId}, function(data) {
				if (data.message != null) {
					alert(data.message);
				}
				if (data.result == 'ok') {
					if('${signal}'== 'normal'){
						location.href = './listCm.do?menuId=${menuId}&catId=${catId}';
					}else{
						location.href = './listCmMngTgt.do?menuId=${menuId}';
					}
					
				}
			});
	 }
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:msg titleId="ct.jsp.setCmJoin.title" var="title" alt="커뮤니티 가입" />
<c:set var="cmNm" value="JQuery 연구 모임" />
<c:set var="mast" value="김윤아" />

<u:title title="${title} (${joinCtEstbBVo.ctNm})" alt="커뮤니티 가입 (커뮤니티명)" menuNameFirst="true"/>

<form id="setCmJoinForm">
<u:input type="hidden" id="menuId" value="${menuId}" />

<% // 폼 필드 %>
<u:listArea>
	<tr>
	<td width="27%" class="head_lt"><u:msg titleId="cols.cmNm" alt="커큐니티명" /></td>
	<td width="73%" class="body_lt">${joinCtEstbBVo.ctNm}</td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.mast" alt="마스터" /></td>
	<td class="body_lt">${joinCtEstbBVo.mastNm}</td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.joinMet" alt="가입방법" /></td>
	<c:choose>
		<c:when test="${joinCtEstbBVo.joinMet == '1'}">
			<td class="body_lt"><u:msg titleId="ct.jsp.setCmJoin.joinMet02" alt="즉시 가입됩니다." /></td>
		</c:when>
		<c:when test="${joinCtEstbBVo.joinMet == '2'}">
			<td class="body_lt"><u:msg titleId="ct.jsp.setCmJoin.joinMet01" alt="커뮤니티 마스터의 승인 후 가입됩니다."/></td>
		</c:when>
		<c:otherwise>
			<td class="body_lt"></td>
		</c:otherwise>
	</c:choose>
	</tr>
</u:listArea>

<% // 하단 버튼 %>
<u:buttonArea>
	<u:msg titleId="cm.msg.preparing" var="msg" alt="준비중.." />
	<u:button titleId="cm.btn.confirm" alt="확인" href="javascript:join('${joinCtEstbBVo.ctId}');"/>
	<u:button titleId="cm.btn.cancel" alt="취소" href="javascript:history.go(-1);" />
</u:buttonArea>

</form>

