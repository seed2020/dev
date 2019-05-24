<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set test="${param.fncMul == 'Y'}" var="fncMul" value="Y" elseValue="N" />
<script type="text/javascript">
function fnCntcTypCd( obj ){
	$('li.cntcCls').each(function(){$(this).hide()});
	$('li.'+obj.value+'Cls').each(function(){$(this).show()});
};

<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(noSelectMsg){
	var arr=[], id, obj;
	$("#bcListArea tbody:first input[type=${fncMul == 'Y' ? 'checkbox' : 'radio' }]:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
};

<%// 선택목록ID 리턴 %>
function fnSelBc(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) return selRowInArr(arr);
	else return null;
};
<%// 배열에 담긴 목록%>
function selRowInArr(rowArr){
	var objArr = [], $bcId;
	//if(delVa!='') objArr.push(delVa);
	for(var i=0;i<rowArr.length;i++){
		$bcId = $(rowArr[i]).find("input[name='bcId']");
		if($bcId.val()!=''){
			objArr.push($bcId.val());
		}
		//$(rowArr[i]).remove();
	}
	return objArr;
	//$("#delList").val(objArr.join(','));
};

<%// 선택목록 리턴 %>
function fnSelArrs(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) return selRowInArrs(arr);
	else return null;
};
<%// 배열에 담긴 목록%>
function selRowInArrs(rowArr){
	var objArr = [], $bcId;
	//if(delVa!='') objArr.push(delVa);
	for(var i=0;i<rowArr.length;i++){
		$bcId = $(rowArr[i]).find("input[name='bcId']");
		if($bcId.val()!=''){
			objArr.push({bcId:$bcId.val(),bcNm:$bcId.attr('data-bcNm'),compNm:$bcId.attr('data-compNm'),compPhon:$bcId.attr('data-compPhon'),mbno:$bcId.attr('data-mbno'),email:$bcId.attr('data-email')});
		}
		
		//$(rowArr[i]).remove();
	}
	return objArr;
	//$("#delList").val(objArr.join(','));
};

$(document).ready(function() {
	setUniformCSS();
});
</script>

<c:if test="${param.detlViewType eq 'bcOpenList' }">
<!--
	<div class="front">
		<div class="front_left">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="red_stxt"><u:msg titleId="wb.jsp.findBcPop.tx01" alt="* 공개명함만 목록에 나타납니다.." /></td>
				</tr>
			</table>
		</div>
	
		<div class="front_right"></div>
	</div>
-->
</c:if>

<% // 검색영역 %>
<u:searchArea>
<form name="searchForm" action="./findBcFrm.do" >
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="fncCal" value="${param.fncCal}" />
<u:input type="hidden" id="fncMul" value="${fncMul}" />
<u:input type="hidden" id="detlViewType" value="${param.detlViewType}" />
<c:if test="${!empty param.schBcRegrUid }">
	<u:input type="hidden" id="schBcRegrUid" value="${param.schBcRegrUid }"/>
</c:if>
<c:if test="${!empty param.pagingYn }">
	<u:input type="hidden" id="pagingYn" value="${param.pagingYn }"/>
</c:if>

<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<select name="schCat">
							<u:option value="bcNm" titleId="cols.nm" alt="이름" checkValue="${param.schCat}" />
							<u:option value="compNm" titleId="cols.compNm" alt="회사" checkValue="${param.schCat}" />
						</select>
					</td>
					<td><u:input id="schWord" maxByte="50" name="schWord" value="${param.schWord}" titleId="cols.schWord" /></td>
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
<div id="bcListArea" class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
	<tr id="headerTr">
		<td width="20px" class="head_bg">
			<c:if test="${fncMul != 'Y'}">&nbsp;</c:if>
			<c:if test="${fncMul == 'Y'}"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('bcListArea', this.checked);" value=""/></c:if>
		</td>
		<td width="15%" class="head_ct"><u:msg titleId="cols.nm" alt="이름" /></td>
		<td class="head_ct"><u:msg titleId="cols.comp" alt="회사" /></td>
		<%-- <td width="25%" class="head_ct"><u:msg titleId="cols.dept" alt="부서" /></td> --%>
		<td width="20%" class="head_ct">
			<select name="cntcTypCd" onchange="fnCntcTypCd(this);">
				<u:option value="compPhon" titleId="cols.comp" alt="회사" />
				<u:option value="homePhon" titleId="cols.home" alt="자택" />
				<u:option value="mbno" titleId="cols.mob" alt="휴대" />
			</select>
		</td>
		<td width="20%" class="head_ct">
		<c:choose>
			<c:when test="${param.detlViewType eq 'bcOpenList' }"><u:msg titleId="cols.publYn" alt="공개여부" /></c:when>
			<c:otherwise><u:msg titleId="wb.cols.fldNm" alt="폴더" /></c:otherwise>
		</c:choose>
		</td>
	</tr>
	<c:choose>
		<c:when test="${!empty wbBcBMapList}">
			<c:forEach var="wbBcBMap" items="${wbBcBMapList}" varStatus="status">
				<tr>
					<td class="bodybg_ct">
						<c:if test="${fncMul != 'Y'}"><u:radio id="check_${wbBcBMap.bcId }" name="bcCheck" value="${wbBcBMap.bcId }" checked="false" /><input type="hidden" name="bcId" value="${wbBcBMap.bcId }" data-bcNm="${wbBcBMap.bcNm }" data-compNm="${wbBcBMap.compNm }" data-compPhon="${wbBcBMap.compPhon }" data-mbno="${wbBcBMap.mbno }" data-email="${wbBcBMap.email }"/></c:if>
						<c:if test="${fncMul == 'Y'}">
							<u:checkbox id="check_${wbBcBMap.bcId }" name="bcCheck" value="${wbBcBMap.bcId }" checked="${fn:contains(param.selBcId , wbBcBMap.bcId)}" /><input type="hidden" name="bcId" value="${wbBcBMap.bcId }" data-bcNm="${wbBcBMap.bcNm }" data-compNm="${wbBcBMap.compNm }" data-compPhon="${wbBcBMap.compPhon }" data-mbno="${wbBcBMap.mbno }" data-email="${wbBcBMap.email }" />
						</c:if>
					</td>
					<td class="body_ct"><div class="ellipsis" title="${wbBcBMap.compNm }"><label for="check_${wbBcBMap.bcId }">${wbBcBMap.bcNm }</label></div></td>
					<td class="body_ct"><div class="ellipsis" title="${wbBcBMap.compNm }">${wbBcBMap.compNm }</div></td>
					<%-- <td class="body_ct"><div class="ellipsis" title="${wbBcBMap.compNm }">${wbBcBMap.deptNm }</div></td> --%>
					<td class="body_ct">
						<ul style="list-style:none;margin:0;padding:0px;">
							<li class="cntcCls compPhonCls" >${wbBcBMap.compPhon }</li>
							<li class="cntcCls homePhonCls" style="display:none;">${wbBcBMap.homePhon }</li>
							<li class="cntcCls mbnoCls" style="display:none;">${wbBcBMap.mbno }</li>
						</ul>
					</td>
					<td class="body_ct">
						<c:choose>
							<c:when test="${param.detlViewType eq 'bcOpenList' && wbBcBMap.publTypCd eq 'allPubl'}"><u:msg titleId="cm.option.allPubl"/></c:when>
							<c:when test="${param.detlViewType eq 'bcOpenList' && wbBcBMap.publTypCd eq 'deptPubl'}"><u:msg titleId="cm.option.deptPubl"/></c:when>
							<c:when test="${param.detlViewType eq 'bcOpenList' && wbBcBMap.publTypCd eq 'apntPubl'}"><u:msg titleId="cm.option.apntPubl"/></c:when>
							<c:otherwise>${wbBcBMap.fldId eq 'ROOT' ? 'Category' : wbBcBMap.fldNm}</c:otherwise>
						</c:choose>						
					</td>
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
