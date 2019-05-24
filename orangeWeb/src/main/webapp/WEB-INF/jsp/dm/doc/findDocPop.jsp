<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="params" excludes="data"/>
<u:set var="callback" test="${!empty param.callback }" value="${param.callback }" elseValue="saveSubDoc"/>
<script type="text/javascript">
<!--<%// [확인] 저장 %>
function applyCfrm(){
	var selId = getIframeContent('findDocFrm').selDocIds('docId');
	if(selId == null) return;
	${callback}(selId);
}<%// [확인] 저장 %>
function applyCfrms(){
	var selInfos = getIframeContent('findDocFrm').selDocInfos('docId');
	if(selInfos == null) return;
	${callback}(selInfos);
}<%// 조회 %>
function findDocSearch(){
	var url = './findDocFrm.do?${params }';
	url+= '&'+$('#findDocForm').serialize();
	reloadFrame('findDocFrm', url);
}
$(document).ready(function() {
});
//-->
</script>
<div style="width:750px;height:470px;">
<u:searchArea>
	<form id="findDocForm">
	<c:if test="${not empty param.pageRowCnt}">
	<u:input type="hidden" id="pageRowCnt" value="${param.pageRowCnt}" /></c:if>
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
		<tr>
			<td>
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td><select name="schCat">
						<u:option value="subj" titleId="cols.subj" alt="제목" checkValue="${param.schCat}" />
						<u:option value="cont" titleId="cols.cont" alt="내용" checkValue="${param.schCat}" />
						<u:option value="regrNm" titleId="cols.regr" alt="등록자" checkValue="${param.schCat}" />
						<u:option value="kwdNm" titleId="dm.cols.kwd" alt="키워드" checkValue="${param.schCat}" />
						</select>
						</td>
						<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 200px;" onkeydown="if (event.keyCode == 13) findDocSearch();return false;" /></td>
						<td><a href="javascript:findDocSearch();" class="ico_search"><span><u:msg alt="검색" titleId="cm.btn.search" /></span></a></td>
						<c:if test="${empty param.fldId }">
						</c:if>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</form>
</u:searchArea>
<iframe id="findDocFrm" name="findDocFrm" src="/cm/doc/findDocFrm.do?${params }" style="width:100%; height:350px;" frameborder="0" marginheight="0" marginwidth="0" ></iframe>
<u:blank />
<u:buttonArea>
	<c:if test="${empty param.callback }"><u:button titleId="cm.btn.confirm" alt="확인" href="javascript:applyCfrm();" /></c:if>
	<c:if test="${!empty param.callback }"><u:button titleId="cm.btn.confirm" alt="확인" href="javascript:applyCfrms();" /></c:if>
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</div>