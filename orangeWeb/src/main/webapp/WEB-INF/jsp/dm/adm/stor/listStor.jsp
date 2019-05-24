<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:secu auth="SYS"><u:set test="${true}" var="hasAuthSys" value="Y" /></u:secu><u:params var="nonPageParams" excludes="storId,pageNo"/>
<u:set var="paramCompIdQueryString" test="${!empty paramCompId }" value="&paramCompId=${paramCompId }" elseValue=""/><!--회사ID -->
<script type="text/javascript">
<!--<%// [버튼] 저장소 등록 수정 %>
function setStorPop(storId){
	var url = './setStorPop.do?menuId=${menuId}${paramCompIdQueryString}';
	if(storId != null) url+='&storId='+storId;
	dialog.open('setStorDialog', '<u:msg titleId="dm.jsp.stor.title" alt="저장소" />', url);
}<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(noSelectMsg){
	var arr=[], id, obj;
	$("#listArea tbody:first input[type=${multi == true ? 'checkbox' : 'radio' }]:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
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
			objArr.push($storId.val());
		}
		
		//$(rowArr[i]).remove();
	}
	return objArr;
	//$("#delList").val(objArr.join(','));
};<% // [하단버튼:삭제] 삭제 %>
function delStor() {
	var storIds = fnSelArrs();
	if(storIds == null ) return;
	if (confirmMsg("dm.cfrm.del.stor")) {	<% // dm.cfrm.del.stor=저장소에 등록된 모든 문서 정보가 삭제됩니다.\n계속하시겠습니까? %>
		callAjax('./transStorDelAjx.do?menuId=${menuId}', {storIds:storIds,paramCompId:'${paramCompId}'}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.replace(location.href);
			}
		});
	}
}<% //저장소 초기화 %>
function storReset(){
	var $form = $("#setEnvForm");
	$form.attr('action','./transStorReset.do');
	$form.attr('target','dataframe');
	$form.submit();
};<%// 회사 선택 %>
function srchComp(obj){
	var $form = $('#searchForm');
	$form.find("input[name='paramCompId']").remove();
	$form.appendHidden({name:'paramCompId',value:$(obj).val()});
	$form.submit();
};
$(document).ready(function() {
setUniformCSS();
});
//-->
</script>
<c:if test="${multi == true && !empty ptCompBVoList }">
<u:set var="paramCompId" test="${!empty paramCompId }" value="${paramCompId }" elseValue="${param.paramCompId }"/>
<div class="front notPrint">
	<div class="front_left">		
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td style="padding:3px 4px 0 0px"><u:title titleId="dm.jsp.stor.title" alt="저장소" menuNameFirst="true" /></td>
			<td class="width5"></td>
				<td class="frontinput">
					<select id="compId" name="compId" <u:elemTitle titleId="cols.comp" /> onchange="srchComp(this);">
						<c:forEach items="${ptCompBVoList}" var="ptCompBVo" varStatus="status">
							<u:option value="${ptCompBVo.compId}" title="${ptCompBVo.rescNm}" checkValue="${paramCompId }"/>
						</c:forEach>
					</select>
				</td>
	 		</tr>
		</table>
	</div>
</div>
</c:if>
<c:if test="${multi == false}"><u:title titleId="dm.jsp.stor.title" alt="저장소" menuNameFirst="true" /></c:if>
<% // 검색영역 %>
<u:searchArea>
<form id="searchForm" name="searchForm" action="./${listPage }.do" >
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="schCat" value="storNm" />
<c:if test="${!empty paramCompId }"><u:input type="hidden" id="paramCompId" value="${paramCompId }" /></c:if>
<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="search_tit"><u:msg titleId="dm.cols.storNm" alt="저장소명" /></td>
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
<u:set var="colgroup" test="${multi == true }" value="3%,,15%,15%,25%" elseValue=",15%,15%,25%"/>
<% // 목록 %>
<u:listArea colgroup="${colgroup }" id="listArea">
	<tr id="headerTr">
		<c:if test="${multi == true}"><td class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td></c:if>
		<td class="head_ct"><u:msg titleId="dm.cols.storNm" alt="저장소명" /></td>
		<td class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
		<td class="head_ct"><u:msg titleId="dm.cols.dftYn" alt="기본여부" /></td>
		<td class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	</tr>
	<c:choose>
		<c:when test="${!empty dmStorBVoList}">
			<c:forEach var="list" items="${dmStorBVoList}" varStatus="status">
				<tr><c:if test="${multi == true}">
					<td class="bodybg_ct">	<u:checkbox id="check_${list.storId }" name="itemChk" value="${list.storId }" /><input type="hidden" name="storId" value="${list.storId }" /></td>
					</c:if>
					<td class="body_ct"><a href="javascript:setStorPop('${list.storId }');">${list.storNm }</a></td>
					<td class="body_ct">${list.useYn }</td>
					<td class="body_ct">${list.dftYn }</td>
					<td class="body_ct"><u:out value="${list.regDt }" type="longdate" /></td>
				</tr>
			</c:forEach>
			</c:when>
		<c:otherwise>
			<tr>
				<td class="nodata" colspan="${multi == true ? 5 : 4 }"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
		</c:otherwise>
	</c:choose>
</u:listArea>
<u:pagination  />
<% // 하단 버튼 %>

<u:buttonArea>
	<c:if test="${resetYn eq 'Y' }"><u:button titleId="cm.btn.reset" alt="초기화" href="javascript:storReset();" auth="A" /></c:if>
	<c:if test="${(empty resetYn || resetYn ne 'Y') && ((not empty dmTranEnable and not empty hasAuthSys) or sessionScope.userVo.userUid eq 'U0000001')}">
		<u:button titleId="dm.cols.doc.transfer" alt="문서이관" href="./listTran.do?menuId=${menuId }${paramCompIdQueryString }" auth="SYS" />
	</c:if>
	<u:button titleId="cm.btn.del" alt="삭제" onclick="delStor();" auth="A" />
	<%-- <u:button titleId="cm.btn.reg" alt="등록" href="javascript:setStorPop(null);" auth="A" /> --%>
</u:buttonArea>

<form id="setEnvForm" method="post">
<input type="hidden" name="menuId" value="${menuId}" />
<c:if test="${!empty paramCompId }"><u:input type="hidden" id="paramCompId" value="${paramCompId }" /></c:if>
</form>