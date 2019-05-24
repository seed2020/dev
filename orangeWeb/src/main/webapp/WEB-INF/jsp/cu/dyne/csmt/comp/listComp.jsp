<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="paramsForList" excludes="workNo" />
<script type="text/javascript">
<!--
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title menuNameFirst="true"/>

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="${_uri}" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="schInitial" value="${param.schInitial}" />
	
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">
			<u:option value="subj" titleId="cols.subj" alt="제목" checkValue="${param.schCat}" />
			<u:option value="cont" titleId="cols.cont" alt="내용" checkValue="${param.schCat}" />
			</select></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 400px;" /></td>
		<td class="width20"></td>
		<td class="search_tit">날짜<u:input type="hidden" id="durCat" value="reprtDt"/></td>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td><u:calendar id="durStrtDt" option="{end:'durEndDt'}" value="${param.durStrtDt}" /></td>
					<td class="search_body_ct"> ~ </td>
					<td><u:calendar id="durEndDt" option="{start:'durStrtDt'}" value="${param.durEndDt}" /></td>
				</tr>
			</table>
		</td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<div class="front">
	<c:if test="${_lang eq 'ko'}">
		<div class="front_left">
			<table border="0" cellpadding="0" cellspacing="0">
				<tbody>
					<tr>
						<td style="float:left; padding:2px 0 0 3px; margin:0 0 0 -3px;">
							<c:set var="initialList" value="전체,ㄱ,ㄴ,ㄷ,ㄹ,ㅁ,ㅂ,ㅅ,ㅇ,ㅈ,ㅊ,ㅋ,ㅌ,ㅍ,ㅎ,A~Z,123"/>
							<div class="cardarea" id="iconArea" style="float:left; padding:4px 0 0 3px; margin:0 0 0 -3px;">
								<dl>
								<c:forTokens var="initial" items="${initialList }" delims="," varStatus="status">
								<dd id="iconArea_sub" class="${(status.count == 1 && empty param.schInitial) || initial == param.schInitial  ? 'cardtxton' : 'cardtxt'}"><a href="javascript:;" onclick="searchForm.schInitial.value='${initial == '전체' ? '' : initial}';document.searchForm.submit();">${initial }</a></dd>
								</c:forTokens>
								</dl>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</c:if>
		<c:if test="${_lang ne 'ko'}">
			<div class="front_left">
				<table border="0" cellpadding="0" cellspacing="0">
					<tbody>
						<tr>
							<td style="float:left; padding:2px 0 0 3px; margin:0 0 0 -3px;">
								<c:set var="initialList" value="ALL,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,123"/>
								<div class="cardarea" id="iconArea" style="float:left; padding:4px 0 0 3px; margin:0 0 0 -3px;">
									<dl>
									<c:forTokens var="initial" items="${initialList }" delims="," varStatus="status">
									<dd id="iconArea_sub" class="${(status.count == 1 && empty param.schInitial) || initial == param.schInitial  ? 'cardtxton' : 'cardtxt'}"><a href="javascript:;" onclick="searchForm.schInitial.value='${initial == 'ALL' ? '' : initial}';document.searchForm.submit();">${initial }</a></dd>
									</c:forTokens>
									</dl>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</c:if>
	</div>
<u:listArea id="listArea" colgroup="3%,15%,,13%,13%">	
	<tr id="headerTr">
		<td class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td>
		<th class="head_ct">회사명</th>
		<th class="head_ct">주소</th>
		<th class="head_ct">등록자</th>
		<th class="head_ct">등록일시</th>
	</tr>
	<c:if test="${fn:length(cuTaskStatBVoList) == 0}">
		<tr>
		<td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
	<c:if test="${fn:length(cuTaskStatBVoList)>0}">
		<c:forEach items="${cuTaskStatBVoList}" var="cuTaskStatBVo" varStatus="status">
			<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"' onclick="viewTaskStat('${cuTaskStatBVo.statNo}')">
				<td class="bodybg_ct"><input type="checkbox" value="${cuTaskStatBVo.statNo }" onclick="notEvtBubble(event);"/></td>
				<td class="body_ct"></td>
				<td class="body_lt"><u:out value="${cuTaskStatBVo.subj }"/></td>
				<td class="body_ct"><a href="javascript:viewUserPop('${cuTaskStatBVo.regrUid }');">${cuTaskStatBVo.regrNm }</a></td>
				<td class="body_ct"><a href="javascript:viewUserPop('${cuTaskStatBVo.modrUid }');">${cuTaskStatBVo.modrNm }</a></td>
			</tr>
		</c:forEach>
	</c:if>
	
</u:listArea>
<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
<u:button titleId="cm.btn.write" alt="등록" href="./setComp.do?menuId=${menuId}" auth="W" />
<c:if test="${!empty auth && auth eq 'A' }"><u:button titleId="cm.btn.del" alt="삭제" href="javascript:delTaskStat();" /></c:if>
</u:buttonArea>
