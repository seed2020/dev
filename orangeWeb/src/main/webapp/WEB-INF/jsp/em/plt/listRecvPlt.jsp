<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set var="agntParam" test="${!empty schBcRegrUid }" value="&schBcRegrUid=${schBcRegrUid }" elseValue=""/>
<!-- 화면에 보여질 목록정보 -->
<c:set var="headList" value="cols.nm,cols.compNm,cols.reprCntc,cols.email"/><!-- 컬럼명(컬럼명을 보여줄경우) -->
<c:set var="bodyList" value="bcNm,compNm,dftCntc,email"/><!-- 데이터 목록 -->
<c:set var="colSpan" value="4"/><!-- line css 를 주기 위한 colspan -->
<c:set var="vaAlign" value="body_lt,body_lt,body_ct,body_ct"/><!-- 데이터 정렬 조건 lt:left , ct:center , rt:right -->
<c:set var="colList" value="*,25%,25%,28%"/><!-- 데이터목록의 가로길이 -->
<u:authUrl var="viewUrl" url="/wb/${viewPage}.do" authCheckUrl="/wb/${listPage }.do"/><!-- view page 호출관련 url 조합(menuId추가) -->
<script type="text/javascript">
<!--
//상세보기
function viewBc(bcId) {
	var authUrl = '${viewUrl}';
	var prefix = authUrl.indexOf('?') > -1 ? "&" : "?";
	authUrl+=prefix+"bcId="+bcId+"${agntParam}";
	top.location.href=authUrl;
};

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

//포틀릿의 가로 세로 길이 재정렬
function fnPltSet(){
	var wdthPx = '${param.wdthPx}' == '' ? $('#ptltable').width() : '${param.wdthPx -18}';
	var selList = [];
	var headList = getHeadList();//목록컬럼
	var vaAlign = getVaAlignList();//정렬
	var bodyList = getBodyList();//데이터목록
	var colList = getColList();//가로 사이즈
	
	//가로사이즈에 맞게 보여줄 컬럼 목록(배열index)
	if( wdthPx <= 200 ) selList = [0,2];
	else if( wdthPx <= 600 && wdthPx > 200 ) selList = [0,1,2]; 
	
	if(selList.length == 0 ){//항목을 모두 보여줄때
		for(var i=0;i<headList.length;i++){
			$('#head_'+headList[i]).show();
			$('.body_'+bodyList[i]).addClass(vaAlign[i]);
			$('.body_'+bodyList[i]).show();
			$('.body_'+bodyList[i]).css("width",colList[i]);
		}
	}else{
		for(var i=0;i<selList.length;i++){
			$('#head_'+headList[selList[i]]).show();
			$('.body_'+bodyList[selList[i]]).addClass(vaAlign[selList[i]]);
			$('.body_'+bodyList[selList[i]]).show();
			$('.body_'+bodyList[selList[i]]).css("width",colList[selList[i]]);
		}
	}
	var colspan = selList.length == 0 ? headList.length : selList.length;
	$('.line').attr('colspan',colspan);
	$('.nodata').attr('colspan',colspan);
};

$(document).ready(function() {
	fnPltSet();
	<%// 유니폼 적용 %>
	setUniformCSS();
	
});
//-->
</script>
<% // 목록 %>
<div class="ptlbody_ct" >
<table class="ptltable" id="ptltable" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed;">
	<tbody>
	<c:if test="${param.colYn eq 'Y'}">
		<tr id="headerTr">
		  <td colspan="${colSpan}" class="line"></td>
		</tr>
		<tr id="headerTr" class="aaa">
			<c:forTokens	items="${headList}" var="colId" varStatus="colStatus" delims=",">
				<td class="head_ct" id="head_${fn:split(colId,'.')[1] }" style="display:none;"><div class="ellipsis" title="<u:msg titleId="${colId}" alt="이름" />"><u:msg titleId="${colId}" alt="이름" /></div></td>
			</c:forTokens>
		</tr>
		<tr id="headerTr">
		  <td colspan="${colSpan}" class="line"></td>
		</tr>
	</c:if>
	<c:choose>
		<c:when test="${!empty rsltMapList}">
			<c:forEach var="mapList" items="${rsltMapList}" varStatus="status">
				<c:set var="mapList" value="${mapList}" scope="request" />
				<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
					<c:forTokens	items="${bodyList}" var="colId" varStatus="colStatus" delims=",">
						<td class="body_${colId }" id="list_${colStatus.count == 1 ? colId : '' }" style="display:none;">
							<c:choose>
								<c:when test="${colId eq 'bcNm' }"><div class="ellipsis" title="<u:convertMap srcId="mapList" attId="${colId}" type="html" />"><a href="javascript:viewBc('${mapList.bcId }');"><u:convertMap srcId="mapList" attId="${colId}" type="html" /></a></div></c:when>
								<c:when test="${colId eq 'email' }">
									<a href="javascript:parent.mailToPop('<u:convertMap srcId="mapList" attId="${colId}" type="script" />')" title="<u:msg titleId='or.jsp.viewUserPop.mailToPop' />"><u:convertMap srcId="mapList" attId="${colId}" type="html" /></a>
								</c:when>
								<c:otherwise><div class="ellipsis" title="<u:convertMap srcId="mapList" attId="${colId}" type="html" />"><u:convertMap srcId="mapList" attId="${colId}" type="html" /></div></c:otherwise>
							</c:choose>
						</td>
					</c:forTokens>
				</tr>
				<tr id="headerTr">
				  <td colspan="${colSpan}" class="line"></td>
				</tr>
			</c:forEach>
			</c:when>
		<c:otherwise>
			<tr>
				<td class="nodata" colspan="${colSpan}"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
		</c:otherwise>
	</c:choose>
	</tbody>
</table>
</div>