<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<!-- 화면에 보여질 목록정보 -->
<c:set var="headList" value="ct.cols.cm,ct.cols.join"/><!-- 컬럼명(컬럼명을 보여줄경우) -->
<c:set var="bodyList" value="ctNm,logUserJoinStat"/><!-- 데이터 목록 -->
<c:set var="colSpan" value="2"/><!-- line css 를 주기 위한 colspan -->
<c:set var="vaAlign" value="body_lt,body_ct"/><!-- 데이터 정렬 조건 lt:left , ct:center , rt:right -->
<c:set var="colList" value="*,20%"/><!-- 데이터목록의 가로길이 -->
<u:authUrl var="joinUrl" url="/ct/setCmJoin.do" authCheckUrl="/ct/listAllCm.do"/>

<script type="text/javascript">
<!--

function join(ctId,catId) {
	var authUrl = '${joinUrl}';
	var prefix = authUrl.indexOf('?') > -1 ? "&" : "?";
	authUrl+=prefix+"ctId="+ctId+"&catId="+catId+"&signal=normal";
	top.location.href=authUrl;
}

function viewCm(ctId, fncUid) {
	//location.href = "./leftCt.do?menuId="+ fncUid +"&ctId=" + ctId + "&prevMenuId=${menuId}";
	top.location.href = "/ct/viewCm.do?menuId="+ fncUid +"&ctId=" + ctId + "&prevMenuId=${menuId}";
}

//상세보기
function viewMetng(bcMetngDetlId) {
	var authUrl = '${viewUrl}';
	var prefix = authUrl.indexOf('?') > -1 ? "&" : "?";
	authUrl+=prefix+"bcMetngDetlId="+bcMetngDetlId+"${agntParam}";
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
	if( wdthPx <= 400 ) selList = [0,1];
	
	if(selList.length == 0 ){//항목을 모두 보여줄때
		for(var i=0;i<headList.length;i++){
			$('#head_'+headList[i].split('.')[2]).show();
			$('.body_'+bodyList[i]).addClass(vaAlign[i]);
			$('.body_'+bodyList[i]).show();
			$('.body_'+bodyList[i]).css("width",colList[i]);
		}
	}else{
		for(var i=0;i<selList.length;i++){
			$('#head_'+headList[selList[i]].split('.')[2]).show();
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
	//setUniformCSS();
});
//-->
</script>
<% // 목록 %>
<table class="ptltable" id="ptltable" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed;">
	<tbody>
		<tr>
		  <td colspan="${colSpan}" class="line"></td>
		</tr>
	<c:if test="${param.colYn eq 'Y'}">
		<tr id="headerTr">
			<c:forTokens	items="${headList}" var="colId" varStatus="colStatus" delims=",">
				<td class="head_ct" id="head_${fn:split(colId,'.')[2] }" style="display:none;"><div class="ellipsis" title="<u:msg titleId="${colId}" alt="이름" />"><u:msg titleId="${colId}" alt="이름" /></div></td>
			</c:forTokens>
		</tr>
		<tr>
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
								<c:when test="${colId eq 'ctNm' }"><div class="ellipsis" title="<u:convertMap srcId="mapList" attId="${colId}" type="html" />"><a href="javascript:viewCm('${mapList.ctId}','${mapList.ctFncUid}');"><u:convertMap srcId="mapList" attId="${colId}" type="html" /></a></div></c:when>
								<c:when test="${colId eq 'logUserJoinStat' }">
									<c:choose>
										<c:when test="${mapList.logUserJoinStat == null}">
											<u:buttonS titleId="ct.btn.join" alt="가입" onclick="join('${mapList.ctId}','${mapList.catId}');" />
										</c:when>
 										<c:when test="${mapList.logUserJoinStat == '1'}">
											<u:msg titleId="ct.option.logUserJoinStat01" alt="승인대기중"/>
										</c:when>
										<c:when test="${mapList.logUserJoinStat == '2'}">
											<u:msg titleId="ct.option.logUserJoinStat02" alt="가입불가"/>
										</c:when>
										<%--<c:when test="${mapList.logUserJoinStat == '3'}">
											<u:msg titleId="ct.option.logUserJoinStat03" alt="가입완료"/>
										</c:when>	 --%>
									</c:choose>
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
			<tr id="lineTr"><td colspan="${colSpan}" class="line"></td></tr>
		</c:otherwise>
	</c:choose>
	
	</tbody>
</table>
<u:blank />
<u:pagination noTotalCount="true" noBottomBlank="true" pltBlock="true"/>
