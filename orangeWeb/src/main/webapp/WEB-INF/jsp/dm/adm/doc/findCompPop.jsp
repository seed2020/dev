<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="params" excludes="data"/>
<u:set var="callback" test="${!empty param.callback }" value="${param.callback }" elseValue="saveComp"/>
<script type="text/javascript">
<!--<%// [확인] 저장 %>
function applyCfrm(){
	var selId = getIframeContent('findCompFrm').selChkIds('compId');
	if(selId == null) return;
	${callback}(selId);
}<%// [확인] 저장 %>
function applyCfrms(){
	var selInfos = getIframeContent('findCompFrm').selChkInfos('compId');
	if(selInfos == null) return;
	${callback}(selInfos);
}<%// 조회 %>
function findCompSearch(){
	var url = './findCompFrm.do?${params }';
	url+= '&'+$('#findCompForm').serialize();
	reloadFrame('findCompFrm', url);
}
$(document).ready(function() {
	
});
//-->
</script>
<div style="width:350px;height:370px;">
<u:searchArea>
	<form id="findCompForm">
	<c:if test="${not empty param.pageRowCnt}">
	<u:input type="hidden" id="pageRowCnt" value="${param.pageRowCnt}" /></c:if>
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
		<tr>
			<td>
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td><u:input id="compNm" maxByte="50" value="${param.compNm}" titleId="cols.compNm" style="width:190px;" onkeydown="if (event.keyCode == 13) findCompSearch();return false;" /></td>
						<td><a href="javascript:findCompSearch();" class="ico_search"><span><u:msg alt="검색" titleId="cm.btn.search" /></span></a></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</form>
</u:searchArea>
<iframe id="findCompFrm" name="findCompFrm" src="./findCompFrm.do?${params }" style="width:100%; height:260px;" frameborder="0" marginheight="0" marginwidth="0" ></iframe>
<u:blank />
<u:buttonArea>
	<c:if test="${empty param.multi }"><u:button titleId="cm.btn.confirm" alt="확인" href="javascript:applyCfrm();" /></c:if>
	<c:if test="${!empty param.multi && param.multi eq 'Y' }"><u:button titleId="cm.btn.confirm" alt="확인" href="javascript:applyCfrms();" /></c:if>
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</div>