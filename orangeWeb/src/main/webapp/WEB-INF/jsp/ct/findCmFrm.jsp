<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

//커뮤니티 선택
function selectCt(){
	var $findCmFrmFrom = $("#findCmForm");
	var $selectCtId = $findCmFrmFrom.find('input:radio:checked').val();
	var $selectRow = $findCmFrmFrom.find('input:radio:checked').parent().parent().parent().parent();
	var $selectCtNm = $selectRow.find("#cmntNm").val();
	parent.setSearchCt($selectCtId, $selectCtNm);
	
}

// 커뮤니티 검색
function search(){
	var $findCt = $("#schWordPop").find("input[name='schWord']").val();
	var $schCatVal = $("#searchCtPop").val();
	var $form = $("#findCmForm");
	$form.attr("method", "POST");
	$form.attr("action", "./findCmFrm.do?menuId=${menuId}&parentSchCat="+$schCatVal+"&parentSchWord="+$findCt);
	$form.submit();
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>



<div style="width:100%;">
<form id="findCmForm">

<% // 검색영역 %>
<u:searchArea>
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<c:if test="${!empty param.ctActStat }"><u:input type="hidden" id="ctActStat" value="${param.ctActStat}" /></c:if>
	
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select id="searchCtPop" name="schCat">
				<u:option value="communityOpt" titleId="ct.cols.ctNm" alt="커뮤니티" checkValue="${parentSchCat}" />
				<u:option value="masterOpt" titleId="ct.cols.mastNm" alt="마스터" checkValue="${parentSchCat}" />
			</select></td>
		<td id="schWordPop"><u:input id="schWord" maxByte="50" value="${parentSchWord}" titleId="cols.schWord" style="width: 300px;" onkeydown="if (event.keyCode == 13) javascript:search();"/></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:search();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
</u:searchArea>


<% // 목록 %>
<div class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="width:100%;table-layout:fixed;">
		<tr>
		<td width="25" class="head_bg">&nbsp;</td>
		<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
		<td width="80" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
		<td width="80" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
		</tr>
		
		<c:forEach var="myCtVo" items="${myCtMapList}" varStatus="status">
			<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
				<td class="bodybg_ct"><u:radio name="chk" value="${myCtVo.ctId}" checked="false" /></td>
				<td class="body_lt">
					<div class="ellipsis" title="<u:out value="${myCtVo.ctNm}"/>">
						<label for="chk${myCtVo.ctId}"><u:out value="${myCtVo.ctNm}"/></label><input id="cmntNm" name="cmntNm" type="hidden" value="${myCtVo.ctNm}">
					</div>	
				</td>
				<td class="body_ct"><a href="javascript:viewUserPop('${myCtVo.mastUid}');">${myCtVo.mastNm}</a></td>
				<td class="body_ct">
					<fmt:parseDate var="dateTempParse" value="${myCtVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
					<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
				</td>
			</tr>
		
		</c:forEach>
		<c:if test="${fn:length(myCtMapList) == 0}">
			<tr>
				<td class="nodata" colspan="4"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
		</c:if>
	</table>
</div>
</form>
<u:blank/>
<u:pagination noTotalCount="true" />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.choice" alt="선택" onclick="javascript:selectCt();" />
	<u:button titleId="cm.btn.close" alt="닫기" onclick="parent.dialog.close('findCmPop');" />
</u:buttonArea>

</div>
