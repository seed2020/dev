<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<% // 순서 저장 %>
function moveSave() {
	var arr=[], id, obj;
	$("#listContentsArea tbody:first input[type='checkbox']").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push($(this).val());
	});
	if(arr.length==0){
		alertMsg('cm.msg.nodata.toSave'); // cm.msg.nodata.toSave=저장할 데이터가 없습니다.
		return;
	}else{
		callAjax('./transRescKndMoveSaveAjx.do?menuId=${menuId}', {rescList: arr}, function (data) {
			if (data.message != null) {
				alert(data.message);
			}
		});
	}
};
<%// [순서조절:위로,아래로] 서버에 저장하지 않고 화면상에서만 순서 조정함 %>
function move(direction){
	var i, arr = getCheckedTrs("cm.msg.noSelect");
	if(arr==null) return;
	
	var $node, $prev, $next, $std;
	if(direction=='up'){
		$std = $('#headerTr');
		for(i=0;i<arr.length;i++){
			$node = $(arr[i]);
			$prev = $node.prev();
			if($prev[0]!=$std[0]){
				$prev.before($node);
			}
			$std = $node;
		}
	} else if(direction=='down'){
		$std = $('#hiddenTr');
		for(i=arr.length-1;i>=0;i--){
			$node = $(arr[i]);
			$next = $node.next();
			if($next[0]!=$std[0]){
				$next.after($node);
			}
			$std = $node;
		}
	}
}
<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(noSelectMsg){
	var arr=[], id, obj;
	$("#listContentsArea tbody:first input[type='checkbox']:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
}
$(document).ready(function() {
setUniformCSS();
});
//-->
</script>

<% // 검색영역 %>
<div id="searchArea" style="padding:10px;">
<u:searchArea>
<form name="searchForm" action="./listRescKndFrm.do" >
<u:input type="hidden" id="menuId" value="${menuId}" />

<table class="search_table" cellspacing="0" cellpadding="0" border="0" >
	<tr>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="search_tit"><u:msg titleId="wr.cols.rescKndNm" alt="자원종류명" /></td>
					<td><u:input id="schWord" maxByte="50" value="${param.schWord }" titleId="cols.schWord" style="width: 80px;" /></td>
				</tr>
				<tr>
					<td class="search_tit"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
					<td>
						<select id="useYn" name="useYn" style="width:80px;">
							<u:option value="" titleId="cm.option.all" checkValue="${param.useYn }" selected="${empty param.useYn }" alt="전체"/>
							<u:option value="Y" titleId="cm.option.use" checkValue="${param.useYn }" alt="사용"/>
							<u:option value="N" titleId="cm.option.notUse" checkValue="${param.useYn }" alt="사용안함"/>
						</select>
					</td>
				</tr>
			</table>
		</td>
		<td>
			<div class="button_search">
				<ul>
					<li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li>
				</ul>
			</div>
		</td>
	</tr>
</table>
</form>
</u:searchArea>
</div>

<% // 목록 %>
<div id="listContentsArea" style="padding:10px;">
	<div class="listarea">
		<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
		<colgroup><col width="10%"/><col width="25px"/><col width="*"/><col width="20%"/></colgroup>
		<tr id="headerTr">
			<td class="head_bg">&nbsp;</td>
			<td class="head_bg">&nbsp;</td>
			<td class="head_ct"><u:msg titleId="cols.rescKnd" alt="자원종류" /></td>
			<td class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
			<%-- <td class="head_ct"><u:msg titleId="cols.regDt" alt="등록일" /></td> --%>
		</tr>
		<c:choose>
			<c:when test="${!empty wrRescKndBVoList}">
				<c:forEach var="list" items="${wrRescKndBVoList }" varStatus="status">
					<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
						<td class="bodybg_ct"><input type="checkbox" value="${list.rescKndId }"/></td>
						<td class="body_ct"><div style="width:15px;height:15px;background:${list.bgcolCd};padding:1px;cursor:pointer;" onclick="parent.fnRescMngSearch('${list.rescKndId}');"></div></td>
						<td class="body_ct"><div class="ellipsis" title="${list.kndNm }"><a href="javascript:;" onclick="parent.setRescKndPop('${list.rescKndId}','${params }');">${list.kndNm }</a></div></td>
						<td class="body_ct"><u:msg titleId="cm.option.${list.useYn eq 'Y' ? 'use' : 'notUse' }" alt="사용여부"/></td>
						<%-- <td class="body_ct"><u:out type="date" value="${list.regDt }"/></td> --%>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<td class="nodata" colspan="4"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
				</tr>
			</c:otherwise>
		</c:choose>
		</table>
	</div>
<u:blank />	

<%-- <u:blank />
<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.reg" alt="등록" onclick="parent.setRescKndPop(null,'${params }');" auth="R" />
</u:buttonArea> --%>
</div>

