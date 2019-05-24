<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(noSelectMsg){
	var arr=[], id, obj;
	$("#listArea tbody:first input[type=${!empty param.multi && param.multi eq 'Y' ? 'checkbox' : 'radio' }]:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
};<%// 선택목록ID 리턴 %>
function fnSelId(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) return selRowInArr(arr);
	else return null;
};<%// 배열에 담긴 목록%>
function selRowInArr(rowArr){
	var objArr = [], $storId;
	//if(delVa!='') objArr.push(delVa);
	for(var i=0;i<rowArr.length;i++){
		$storId = $(rowArr[i]).find("input[name='storId']");
		if($storId.val()!=''){
			objArr.push($storId.val());
		}
	}
	return objArr;
};<%// 선택목록 리턴 %>
function fnSelArrs(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) return selRowInArrs(arr);
	else return null;
};<%// 배열에 담긴 목록%>
function selRowInArrs(rowArr){
	var objArr = [], $storId;
	//if(delVa!='') objArr.push(delVa);
	for(var i=0;i<rowArr.length;i++){
		$storId = $(rowArr[i]).find("input[name='storId']");
		if($storId.val()!=''){
			objArr.push({storId:$storId.val()});
		}
		
		//$(rowArr[i]).remove();
	}
	return objArr;
	//$("#delList").val(objArr.join(','));
};
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<% // 검색영역 %>
<u:searchArea>
<form name="searchForm" action="./listStorFrm.do" >
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="schCat" value="storNm" />
<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td><u:input id="schWord" maxByte="50" name="schWord" value="${param.schWord}" titleId="cols.schWord" style="width:350px;"/></td>
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

<% // 목록 %>
<div id="listArea" class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
	<colgroup><col width="30px"/><col width="*"/><col width="15%"/><col width="15%"/><col width="25%"/></colgroup>
	<tr id="headerTr">
		<td class="head_bg">
			<c:if test="${empty param.multi || param.multi eq 'N'}">&nbsp;</c:if>
			<c:if test="${!empty param.multi && param.multi eq 'Y'}"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></c:if>
		</td>
		<td class="head_ct"><u:msg titleId="dm.cols.storNm" alt="저장소명" /></td>
		<td class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
		<td class="head_ct"><u:msg titleId="dm.cols.dftYn" alt="기본여부" /></td>
		<td class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	</tr>
	<c:choose>
		<c:when test="${!empty dmStorBVoList}">
			<c:forEach var="list" items="${dmStorBVoList}" varStatus="status">
				<tr>
					<td class="bodybg_ct">
						<c:if test="${empty param.multi || param.multi eq 'N'}"><u:radio id="check_${list.storId }" name="itemChk" value="${list.storId }" checked="false" /><input type="hidden" name="storId" value="${list.storId }" /></c:if>
						<c:if test="${!empty param.multi && param.multi eq 'Y'}">
							<u:checkbox id="check_${list.storId }" name="itemChk" value="${list.storId }" checked="${fn:contains(param.selBcId , list.storId)}" /><input type="hidden" name="storId" value="${list.storId }" />
						</c:if>
					</td>
					<td class="body_ct">${list.storNm }</td>
					<td class="body_ct">${list.useYn }</td>
					<td class="body_ct">${list.dftYn }</td>
					<td class="body_ct"><u:out value="${list.regDt }" type="longdate" /></td>
				</tr>
			</c:forEach>
			</c:when>
		<c:otherwise>
			<tr>
				<td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
		</c:otherwise>
	</c:choose>
	</table>
</div>
<c:if test="${empty pagingYn || pagingYn eq 'Y'}">
	<u:blank />
	<u:pagination noTotalCount="true" />
</c:if>
