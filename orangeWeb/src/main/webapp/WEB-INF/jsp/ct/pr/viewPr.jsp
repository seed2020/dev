<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
function delPr(){
	if (confirmMsg("cm.cfrm.del")) {
		callAjax('./transPrDel.do?menuId=${menuId}', {bullId : '${ctPrBVo.bullId}'}, function(data){
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.href = './listPr.do?menuId=${menuId}';
			}
		});
	}
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<c:set var="subj" value="놀러 오세요~" />
<c:set var="cmNm" value="JQuery 연구 모임" />
<c:set var="cont" value="부담없이 놀러 오세요~ 많이 많이 놀러 오세요~ 감사합니다." />

<u:title titleId="ct.jsp.viewPr.title" alt="홍보마당 조회" menuNameFirst="true"/>

<form id="viewPrForm">
<u:input type="hidden" id="menuId" value="${menuId}" />

<% // 폼 필드 %>
<div class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
	<colgroup><col width="18%"/><col width="82%"/></colgroup>
		<tbody>	
			<tr>
			<td class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
			<td class="body_lt">${ctPrBVo.subj}</td>
			</tr>
			
			<tr>
			<td class="head_lt"><u:msg titleId="ct.cols.cm" alt="커뮤니티" /></td>
			<td class="body_lt">${ctEstbBVo.ctNm}</td>
			</tr>
			
			<tr>
			<td colspan="2" ><div style="overflow:auto;" class="editor">${ctPrBVo.cont}</div></td>
			</tr>
			<tr>
			<td class="head_lt"><u:msg titleId="cols.attFile" alt="첨부파일" /></td>
			<td colspan="2">
				<c:if test="${!empty fileVoList }"><u:files id="${filesId}" fileVoList="${fileVoList}" module="ct" mode="view" /></c:if>
			</td>
			</tr>
		</tbody>
	</table>
</div>
<u:blank />
<% // 하단 버튼 %>
<u:buttonArea>
	
<%-- 	<u:button titleId="cm.btn.mod" alt="수정" auth="W" href="./setPr.do?menuId=${menuId}&fnc=mod" /> --%>
	<u:msg titleId="cm.cfrm.del" alt="삭제하시겠습니까?" var="msg" />
	
	<c:if test="${prRegr == 'prRegr' || admin == 'admin'}">
		<u:button titleId="cm.btn.mod" alt="수정" href="./setPr.do?menuId=${menuId}&bullId=${ctPrBVo.bullId}" />
		<u:button titleId="cm.btn.del" alt="삭제"  href="javascript:delPr();" />
	</c:if>
	<u:button titleId="cm.btn.list" alt="목록" href="./listPr.do?menuId=${menuId};" />
</u:buttonArea>

</form>

