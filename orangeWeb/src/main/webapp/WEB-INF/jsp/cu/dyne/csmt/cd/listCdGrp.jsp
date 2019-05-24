<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<% // [하단버튼:등록] - 팝업 %>
function setCdGrpPop(id) {
	var url='./setCdGrpPop.do?menuId=${menuId}';
	if(id!=undefined)
		url+='&cdGrpId='+id;
	dialog.open('setCdGrpDialog', '<u:msg titleId="wf.cols.codeGrp" alt="코드그룹" />', url);
}<% // [하단버튼:삭제] - 코드그룹삭제  %>
function delCdGrps(containerId, obj){
	var arr = getCheckedValue("listArea", "cm.msg.noSelect");<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
	if (arr != null && confirmMsg("cm.cfrm.del")) {<% // 삭제하시겠습니까? %>
		callAjax('./transCdGrpDelAjx.do?menuId=${menuId}', {cdGrpIds:arr}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				reload();
			}
		});
	}
};

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="pt.jsp.setCd.title" alt="코드관리" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="${_uri }" >
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="wf.cols.codeGrp" alt="코드그룹" /></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 300px;" />
			<u:input type="hidden" id="schCat" value="cdGrpNm" /></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>

	</form>
</u:searchArea>

<% // 목록 %>
<u:listArea id="listArea" colgroup="3%,15%,,10%,14%">
	<tr>
	<th class="head_ct"></th>
	<td class="head_ct"><u:msg titleId="wf.cols.codeGrp" alt="코드그룹" /></td>
	<td class="head_ct"><u:msg titleId="wf.cols.code" alt="코드" /></td>
	<td class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
	<td class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	</tr>

<c:if test="${fn:length(weCdGrpBVoList) == 0}">
	<tr>
	<td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(weCdGrpBVoList) > 0}">
	<c:forEach items="${weCdGrpBVoList}" var="weCdGrpBVo" varStatus="status">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="bodybg_ct"><input type="checkbox" value="${weCdGrpBVo.cdGrpId}"/></td>
	<td class="body_lt"><a href="javascript:setCdGrpPop('${weCdGrpBVo.cdGrpId}');">${weCdGrpBVo.cdGrpRescNm}</a></td>
	<td class="body_lt">
	<c:if test="${fn:length(weCdGrpBVo.weCdDVoList) > 0}">
		<c:forEach items="${weCdGrpBVo.weCdDVoList}" var="weCdDVo" varStatus="status">
		<c:if test="${status.count > 1 }">,</c:if>
		${weCdDVo.cdRescNm}</c:forEach>
	</c:if></td>
	<td class="body_ct"><u:yn value="${weCdGrpBVo.cdUseYn }" yesId="cm.option.use" noId="cm.option.notUse" alt="사용여부"/></td>
	<td class="body_ct"><u:out value="${weCdGrpBVo.regDt}" type="date" /></td>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>

<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.reg" alt="등록" href="javascript:;" onclick="setCdGrpPop();" auth="A" 
	/><u:button id="btnDel" href="javascript:delCdGrps();" titleId="cm.btn.del" alt="삭제" auth="A" />
</u:buttonArea>
