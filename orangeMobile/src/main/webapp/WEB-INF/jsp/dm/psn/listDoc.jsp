<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="paramStorIdQueryString" test="${!empty paramStorId }" value="&paramStorId=${paramStorId }" elseValue=""/><!-- 저장소ID -->
<script type="text/javascript">
//<![CDATA[
<%// [우하단 버튼] 등록 %>
function setDoc(seq){
	var url = '/dm/doc/${setPage}.do?${paramsForList }';
	if(seq != undefined) url+= '&docId='+seq;
	$m.nav.next(null, url);
}
<% // [목록:제목] 상세 조회 %>
function viewDoc(id) {
	var url = '/dm/doc/${viewPage}.do?${paramsForList }&docId=' + id;
	$m.nav.next(null, url);
}
var holdHide = false;
$(document).ready(function() {
	$('body:first').on('click', function(){
		if(holdHide) holdHide = false;
		else $("#listsearch #searchCat").hide();
	});
	
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('list');
});

//]]>
</script>
<c:if test="${isAdmin == false }">
<u:secu auth="W">
<div class="btnarea">
    <div class="size">
        <dl>
           	 <dd class="btn" onclick="setDoc();"><u:msg titleId="cm.btn.write" alt="등록" /></dd>
     </dl>
    </div>
</div>
</u:secu>
</c:if>

<!-- 검색조건 페이지 -->
<jsp:include page="/WEB-INF/jsp/dm/psn/listDocSrch.jsp" />

<section>
<div class="listarea">
<article><c:forEach
	items="${mapList}" var="map" varStatus="status">
	<u:set var="docLink" test="${map.statCd eq 'T' || map.statCd eq 'M' }" value="setDoc('${map.docId}');" elseValue="viewDoc('${map.docId}');"/>
	<div class="listdiv" onclick="${docLink}">
		<div class="list">
		<dl>
			<dd class="tit">
			${map.subj }<c:if test="${map.statCd eq 'C' && !empty map.subDocCnt}"
			>(<u:out value="${map.subDocCnt}" type="number" />)</c:if>
			</dd><dd class="name"><u:out value="${map.fldNm }"/>ㅣ <u:out value="${map.regDt}" type="date" 
			/></dd>
		</dl>
		</div>
	</div></c:forEach><c:if
	
		test="${recodeCount == 0}">
	<div class="listdiv_nodata" >
		<dl>
		<dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd>
		</dl>
	</div></c:if>
</article>
</div>

<m:pagination />

<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
</section>
