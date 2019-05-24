<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<u:authUrl var="viewUrl" url="/ct/listAllCm.do"  authCheckUrl="/ct/listAllCm.do"/><!-- view page 호출관련 url 조합(menuId추가) -->

<script type="text/javascript">
<!--
function goCm(catId) {
	var authUrl = '${viewUrl}';
	var menuId = authUrl.substring(authUrl.length - 8, authUrl.length);
	
	top.location.href= "/ct/listCm.do?menuId=" + menuId +"&catId=" + catId;
	
}
function getHeadList(){
	var headList = [];
	<c:forTokens	items="${headList}" var="colId" varStatus="colStatus" delims=",">
	headList.push('${colId}');
	</c:forTokens>
	return headList;
};

function getBodyList(){
	var bodyList = [];
	<c:forTokens	items="${bodyList}" var="colId" varStatus="colStatus" delims=",">
	bodyList.push('${colId}');
	</c:forTokens>
	return bodyList;
};

function getVaAlignList(){
	var bodyList = [];
	<c:forTokens	items="${vaAlign}" var="colId" varStatus="colStatus" delims=",">
	bodyList.push('${colId}');
	</c:forTokens>
	return bodyList;
};

function getColList(){
	var bodyList = [];
	<c:forTokens	items="${colList}" var="colId" varStatus="colStatus" delims=",">
	bodyList.push('${colId}');
	</c:forTokens>
	return bodyList;
};

$(document).ready(function() {
	<%// 유니폼 적용 %>
	//setUniformCSS();
});

</script>

	
	<c:forEach var="allCtCatFldVo" items="${allCtCatFldList}" varStatus="status">
	<div>
		<dl>
		<dd class="ptltxt_head">
			<img src="/images/${_skin}/tree_folder_close.gif"><strong>${allCtCatFldVo.catNm}</strong></dd>
		<dd class="ptltxt_body">
			<c:forEach var="allCtCatClsVo" items="${allCtCatClsList}" varStatus="stat">
			<c:if test="${allCtCatClsVo.catPid == allCtCatFldVo.catId}">
				<a href="javascript:goCm('${allCtCatClsVo.catId}');">
					<c:choose>
						<c:when test="${allCtCatClsVo.catCmntCnt == '' || allCtCatClsVo.catCmntCnt == null}">
							${allCtCatClsVo.catNm}(0)<%-- <c:if test="${stat.last eq false}">,</c:if> --%>
						</c:when>
						<c:otherwise>
							${allCtCatClsVo.catNm}(${allCtCatClsVo.catCmntCnt})<%-- <c:if test="${stat.last eq false}">,</c:if> --%>
						</c:otherwise>
					</c:choose>
				</a>
			</c:if>
			</c:forEach>
		</dd>
		</dl>

		<dl>
		<dd style="width:95%;border-bottom:#ced5dd 1px solid;margin:0px 5px;line-height:5px;height:1px;"></dd>
		</dl>

	</div>
	
	</c:forEach>
