<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%// [아이콘 선택] - 아이콘 클릭 - 아이콘 활성화 함 %>
function activeIcon(obj){
	$gIconArea = $("#iconArea");
	$gIconArea.find("dd#iconArea_sub").each(function(){
	$(this).attr("class", "cardtxt");
	});
	$(obj).parent().attr("class", "cardtxton");
};

<%// 목록 클릭 %>
function onBcClick(id , originalBcId){
	parent.openBcList(id , originalBcId);
};

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<div style="padding:5px">
<div class="front">
<c:if test="${_lang eq 'ko'}">
	<form name="searchForm" action="./listBcMainFrm.do" >
		<u:input type="hidden" id="menuId" value="${menuId}" />
		<u:input type="hidden" id="schCat" value="${param.schCat}" />
		<u:input type="hidden" id="schWord" value="${param.schWord}" />
		<u:input type="hidden" id="schInitial" value="${param.schInitial}" />
		<u:input type="hidden" id="mainListType" value="${param.mainListType}" />
		
		<div class="front_left">
			<table border="0" cellpadding="0" cellspacing="0">
				<tbody>
					<tr>
						<td style="float:left; padding:2px 0 0 3px; margin:0 0 0 -3px;">
							<c:set var="initialList" value="전체,ㄱ,ㄴ,ㄷ,ㄹ,ㅁ,ㅂ,ㅅ,ㅇ,ㅈ,ㅊ,ㅋ,ㅌ,ㅍ,ㅎ,A~Z,123"/>
							<div class="cardarea" id="iconArea" style="float:left; padding:4px 0 0 3px; margin:0 0 0 -3px;">
								<dl>
								<c:forTokens var="initialList" items="${initialList }" delims="," varStatus="status">
								<dd id="iconArea_sub" class="${(status.count == 1 && empty param.schInitial) || initialList == param.schInitial  ? 'cardtxton' : 'cardtxt'}"><a href="javascript:;" onclick="activeIcon(this);searchForm.schInitial.value='${initialList == '전체' ? '' : initialList}';document.searchForm.submit();">${initialList }</a></dd>
								</c:forTokens>
								</dl>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</form>
</c:if>
</div>

<u:listArea id="bcList">
	<tr>
		<td width="5%" class="head_ct">&nbsp;</td>
		<td width="15%" class="head_ct"><u:msg titleId="cols.nm" alt="이름" /></td>
		<td class="head_ct"><u:msg titleId="wb.cols.compDept" alt="회사/부서" /></td>
		<td width="15%" class="head_ct"><u:msg titleId="wb.cols.main" alt="Main" /></td>
	</tr>
	<c:choose>
		<c:when test="${!empty wbBcBMapList}">
			<c:forEach var="list" items="${wbBcBMapList}" varStatus="status">
				<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
					<td class="body_ct"><u:radio name="bcId" value="1" onclick="onBcClick('${list.bcId }','${list.originalBcId }');"/></td>
					<td class="body_ct">${list.bcNm }</td>
					<td class="body_ct">${list.compNm }/${list.deptNm }</td>
					<td class="body_ct">${empty list.mainSetupYn ? 'N' : list.mainSetupYn }</td>
				</tr>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<tr>
				<td class="nodata" colspan="4"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
		</c:otherwise>
	</c:choose>
	</u:listArea>
	<u:pagination />
</div>