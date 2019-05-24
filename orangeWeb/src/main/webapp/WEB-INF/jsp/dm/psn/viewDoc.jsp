<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="frmYn" test="${!empty pageSuffix && pageSuffix == 'Frm' }" value="Y" elseValue="N"/>
<u:set var="saveDisc" test="${!empty param.pltYn && param.pltYn eq 'Y' }" value="saveDiscAjx" elseValue="saveDisc"/>
<u:params var="nonPageParams" excludes="docId,pageNo"/>
<u:params var="viewPageParams" excludes="docId,docPid"/>
<u:set var="includeParams" test="${!empty dmDocLVoMap.docPid}" value="&docId=${dmDocLVoMap.docPid }" elseValue="&docId=${dmDocLVoMap.docId }"/>
<c:set var="exKeys" value="view,update,owner,version,disuse,saveDisc,keepDdln,seculCd,recycle,fld,cls,docNoMod,setSubDoc,bumk"/><!-- 하단 버튼 제외 key -->
<script type="text/javascript">
<!--<%// [팝업] 보내기 %>
function send(){
	var url = './sendPsnPop.do?${params}&docTyp=psn';
	parent.dialog.open('sendPop', '<u:msg titleId="cm.btn.send" alt="보내기" />', url);
};<% // [하단버튼:배열] %>
function getRightBtnList(){
	var $area = $("#rightBtnArea");
	return $area.find('ul')[0].outerHTML;
}<% // [하단버튼:뒤로|취소] %>
function cancelDoc(){
	history.go(-1);
}<% // [하단버튼:목록] %>
function listDoc(){
	location.replace($('#listPage').val());
}<% // [목록:제목] 상세 조회 %>
function viewDoc(id) {
	location.href = './${viewPage}.do?${paramsForList }&docId=' + id;
}<% // [하단버튼:수정] %>
function setDoc(){
	location.href = './${setPage}.do?${paramsForList }&docId=${param.docId }';
}<% // [하단버튼:삭제] %>
function delDocTrans(param, valid) {
	if (!valid || confirmMsg("cm.cfrm.del")) {	<% // cm.cfrm.del=삭제하시겠습니까? %>
		callAjax('./transPsnDocDelAjx.do?menuId=${menuId}', param, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.href = $('#listPage').val();
			}
		});
	}
}<% // [하단버튼:삭제] 문서%>
function delDoc(statCd) {
	delDocTrans({docId:$('#docId').val(),statCd:statCd},true);
}<% // [하단버튼:삭제] 문서그룹전체%>
function delDocGrp(statCd) {
	if (confirmMsg("dm.cfrm.del.with")) <% // dm.cfrm.del.with=하위 버전이 있을 경우 함께 삭제됩니다\n그래도 하시겠습니까? %>
		delDocTrans({docGrpId:$('#docGrpId').val(),statCd:statCd},false);	
}
$(document).ready(function() {
	setUniformCSS();
	<c:if test="${pageSuffix eq 'Frm'}">parent.applyDocBtn();</c:if>
});
//-->
</script>
<c:if test="${frmYn == 'Y' }">
<c:set var="frmStyle" value="style='padding:10px;'"/>
</c:if>
<c:if test="${frmYn == 'N' }">
<u:title titleId="dm.jsp.search.doc.title" alt="문서조회" menuNameFirst="true" />
</c:if>
<div ${frmStyle }>
<c:set var="voMap" value="${dmDocLVoMap }" scope="request"/>

<input type="hidden" name="menuId" value="${menuId}" />
<u:input type="hidden" id="listPage" value="./${listPage}.do?${nonPageParams}" />
<u:input type="hidden" id="viewPage" value="./${viewPage}.do?${viewPageParams}${includeParams }" />
<u:input type="hidden" id="viewRefPage" value="./${viewPage}.do?${paramsForList}&docId=${refDocId }" />
<u:input type="hidden" id="docId" value="${dmDocLVoMap.docId }" />
<u:input type="hidden" id="docGrpId" value="${dmDocLVoMap.docGrpId }" />

<c:set var="colgroup" value="15%,"/>
<u:listArea colgroup="${colgroup }" noBottomBlank="true">
<tr>
	<td class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td class="body_lt" >${dmDocLVoMap.subj}</td>
</tr>
<tr>
	<td class="head_lt"><u:msg titleId="cols.fld" alt="폴더" /></td>
	<td class="body_lt">${dmDocLVoMap.fldNm}</td>
</tr>

<c:if test="${!empty lobHandler }">
<tr>
	<td colspan="4" ><div style="overflow:auto;min-height:100px;" class="editor"><u:clob lobHandler="${lobHandler }"/></div></td>
</tr>
</c:if>
<tr>
	<td class="head_lt"><u:msg titleId="cols.attFile" alt="첨부파일" /></td>
	<td colspan="3">
		<c:if test="${!empty fileVoList }"><u:files id="${filesId}" fileVoList="${fileVoList}" module="dm" mode="view" urlParam="docId=${dmDocLVoMap.docId}" actionParam="psn"/></c:if>
	</td>
</tr>
</u:listArea>

<u:blank />
<c:if test="${!empty itemDispList }">
<c:set var="maxCnt" value="2"/>
<c:set var="dispCnt" value="1"/>
<div class="listarea" id="listArea">
<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
<colgroup><col width="15%"/><col width="35%"/><col width="15%"/><col width="35%"/></colgroup>
<tr>
<c:forEach var="dispVo" items="${itemDispList }" varStatus="status">
<c:set var="colmVo" value="${dispVo.colmVo}" />
<c:set var="itemTyp" value="${colmVo.itemTyp}" />
<c:set var="itemNm" value="${dispVo.atrbId}" />
<u:convertMap var="docVal" srcId="voMap" attId="${itemNm }" />
	<u:set var="colspan" test="${((dispCnt == 1 || dispCnt%maxCnt == 1) && ( itemTyp eq 'TEXTAREA') || fn:length(itemDispList) == status.count )}" value="colspan='${(maxCnt*2)-1 }'" elseValue=""/>
	<c:if test="${dispCnt > 1 && dispCnt%maxCnt == 1}"></tr><tr></c:if>
	<td class="head_lt">${colmVo.itemDispNm }</td>
	<td class="body_lt" ${colspan }>
		<c:if test="${itemTyp != 'CODE'}">${docVal }</c:if>
		<c:if test="${itemTyp == 'CODE'}">
			<c:forEach items="${colmVo.cdList}" var="cd" varStatus="status">
				<c:if test="${cd.cdId == docVal}">${cd.rescNm}</c:if>
			</c:forEach>
		</c:if>
	</td>
	<c:if test="${!empty colspan && status.count < fn:length(itemDispList) }"></tr><tr><c:set var="dispCnt" value="0"/></c:if>
	<c:set var="dispCnt" value="${dispCnt+1 }"/>
</c:forEach>
</tr>
</table>
</div>
<u:blank />
</c:if>

<% // 이전글 다음글 %>
<u:listArea colgroup="6%,,10%,15%">
<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
<td class="listicon_ct"><u:icon type="prev" /></td>
<c:choose>
	<c:when test="${!empty prevVo }"><td class="body_lt"><a href="javascript:viewDoc('${prevVo.docId}');" title="${prevVo.subj}"><u:out value="${prevVo.subj}" maxLength="80" /></a></td>
<td class="body_ct"><a href="javascript:viewUserPop('${prevVo.regrUid}');"><u:out value="${prevVo.regrNm}" /></a></td>
<td class="body_ct"><u:out value="${prevVo.regDt }" type="longdate" /></td></c:when>
	<c:otherwise><td class="body_lt"><u:msg titleId="dm.jsp.viewDoc.prevNotExists" alt="이전 문서가 없습니다." /></td>
<td class="body_ct">&nbsp;</td>
<td class="body_ct">&nbsp;</td></c:otherwise>
</c:choose>
</tr>

<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
<td class="listicon_ct"><u:icon type="next" /></td>
<c:choose>
	<c:when test="${!empty nextVo }"><td class="body_lt"><a href="javascript:viewDoc('${nextVo.docId}');" title="${nextVo.subj}"><u:out value="${nextVo.subj}" maxLength="80" /></a></td>
<td class="body_ct"><a href="javascript:viewUserPop('${nextVo.regrUid}');"><u:out value="${nextVo.regrNm}" /></a></td>
<td class="body_ct"><u:out value="${nextVo.regDt }" type="longdate" /></td></c:when>
	<c:otherwise><td class="body_lt"><u:msg titleId="dm.jsp.viewDoc.nextNotExists" alt="다음 문서가 없습니다." /></td>
<td class="body_ct">&nbsp;</td>
<td class="body_ct">&nbsp;</td></c:otherwise>
</c:choose>
</tr>
</u:listArea>

<u:set var="rightBtnDisplay" test="${frmYn eq 'Y' }" value="display:none;" elseValue=""/>
<% // 하단 버튼 %>
<u:buttonArea id="rightBtnArea" style="${rightBtnDisplay }">
	<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" auth="R" />
	<u:button titleId="dm.cols.auth.send" href="javascript:send();" alt="보내기" />
	<c:if test="${isAdmin == false }"><u:button titleId="cm.btn.mod" href="javascript:setDoc();" alt="수정" auth="W"/></c:if>
	<u:button titleId="cm.btn.del" href="javascript:delDoc('F');" alt="삭제" auth="W"/>
	<u:button titleId="cm.btn.list" href="javascript:listDoc();" alt="목록" />
</u:buttonArea>
</div>
<form id="setForm" name="setForm">
<u:input type="hidden" id="listPage" value="./${listPage}.do?${nonPageParams}" />
<u:input type="hidden" id="viewPage" value="./${viewPage}.do?${params}" />
<u:input type="hidden" id="viewRefPage" value="./${viewPage}.do?${paramsForList}&docId=${refDocId }" />
<u:input type="hidden" id="docId" value="${dmDocLVoMap.docId }" />
</form>
