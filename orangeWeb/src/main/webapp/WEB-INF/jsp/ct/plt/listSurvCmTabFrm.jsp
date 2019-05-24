<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<!-- 화면에 보여질 목록정보 -->
<c:set var="headList" value="cols.subj"/><!-- 컬럼명(컬럼명을 보여줄경우) -->
<c:set var="bodyList" value="ctNm"/><!-- 데이터 목록 -->
<c:set var="colSpan" value="1"/><!-- line css 를 주기 위한 colspan -->
<c:set var="vaAlign" value="body_lt"/><!-- 데이터 정렬 조건 lt:left , ct:center , rt:right -->
<c:set var="colList" value="*,20%,15%"/><!-- 데이터목록의 가로길이 -->

<script type="text/javascript">
<!--


function authHaveNot(){
	alert("<u:msg titleId="wv.msg.set.noRight" alt="투표 및 조회권한이 없습니다."/>");
	return;
}


function viewSurv(fncUid, survId, ctId){
	top.location.href = '/ct/surv/viewSurv.do?menuId='+ fncUid + '&survId=' + survId + "&ctId=" + ctId;
	
}

function viewSurvRes(fncUid ,survId, survPrgStatCd, ctId){
	
	top.location.href = '/ct/surv/viewSurvRes.do?menuId='+fncUid+'&survId='+survId+'&survStatCd='+survPrgStatCd + "&ctId="+ ctId;
	
}

function imsiViewSurv(fncUid, survId, ctId){
	
	top.location.href = '/ct/surv/setSurvQues.do?menuId='+fncUid+'&fnc=mod&survId='+survId+'&ctId='+ctId;
	
}

function viewCm(ctId, fncUid) {
	//location.href = "./leftCt.do?menuId="+ fncUid +"&ctId=" + ctId + "&prevMenuId=${menuId}";
	top.location.href = "/ct/viewCm.do?menuId="+ fncUid +"&ctId=" + ctId;
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
	if( wdthPx <= 400 ) selList = [];
	
	if(selList.length == 0 ){//항목을 모두 보여줄때
		for(var i=0;i<headList.length;i++){
			$('#head_'+headList[i].split('.')[1]).show();
			$('.body_'+bodyList[i]).addClass(vaAlign[i]);
			$('.body_'+bodyList[i]).show();
			$('.body_'+bodyList[i]).css("width",colList[i]);
		}
	}else{
		for(var i=0;i<selList.length;i++){
			$('#head_'+headList[selList[i]].split('.')[1]).show();
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
				<td class="head_ct" id="head_${fn:split(colId,'.')[1] }" style="display:none;"><div class="ellipsis" title="<u:msg titleId="${colId}" alt="이름" />"><u:msg titleId="${colId}" alt="이름" /></div></td>
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
				
				<u:set test="${!empty mapList.survTgtM}" var="tgtM" value="${mapList.survTgtM}" elseValue="N"></u:set>
				<u:set test="${!empty mapList.survTgtS}" var="tgtS" value="${mapList.survTgtS}" elseValue="N"></u:set>
				<u:set test="${!empty mapList.survTgtR}" var="tgtR" value="${mapList.survTgtR}" elseValue="N"></u:set>
				<u:set test="${!empty mapList.survTgtA}" var="tgtA" value="${mapList.survTgtA}" elseValue="N"></u:set>
				
				<u:set test="${tgtM == mapList.ctMyAuth}" var="myAuthChkM"  value="Y" elseValue="N" />
				<u:set test="${tgtS == mapList.ctMyAuth}" var="myAuthChkS"  value="Y" elseValue="N" />
				<u:set test="${tgtR == mapList.ctMyAuth}" var="myAuthChkR"  value="Y" elseValue="N" />
				<u:set test="${tgtA == mapList.ctMyAuth}" var="myAuthChkA"  value="Y" elseValue="N" />
				
				<u:set test="${mapList.regrUid == logUserUid || mapList.modrUid == logUserUid }" var="regrAuth"  value="Y" elseValue="N" />	
				
				<c:choose>
					<c:when test="${regrAuth == 'Y' || myAuthChkM == 'Y' || myAuthChkS == 'Y' || myAuthChkR == 'Y' || myAuthChkA == 'Y'}">
						<c:choose>
							<c:when test="${mapList.survPrgStatCd == '6'}">
								<c:set var="viewFunction"	value= "imsiViewSurv('${mapList.ctFncUid}','${mapList.survId}','${mapList.ctId}')" />
							</c:when>
							<c:when test="${mapList.replyYn == 'O'}">
								<c:set var="viewFunction"	value= "viewSurvRes('${mapList.ctFncUid}','${mapList.survId}' , '${mapList.survPrgStatCd}','${mapList.ctId}')" />
							</c:when>
							<c:when test="${mapList.replyYn == 'X'}">
								<c:set var="viewFunction"	value= "viewSurv('${mapList.ctFncUid}','${mapList.survId}','${mapList.ctId}')" />
							</c:when>
						</c:choose>
						
					</c:when>
					<c:otherwise>
						<c:set var="viewFunction"	value= "authHaveNot()" />
					</c:otherwise>
				</c:choose>
				
				<input type="hidden" id="openYn" name="openYn" value="${mapList.openYn}"/>
				
				<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
					<c:forTokens	items="${bodyList}" var="colId" varStatus="colStatus" delims=",">
						<td class="body_${colId }" id="list_${colStatus.count == 1 ? colId : '' }" style="display:none;">
							<c:choose>
								<c:when test="${colId eq 'ctNm' }"><div class="ellipsis" title="<u:convertMap srcId="mapList" attId="${colId}" type="html" />${mapList.subj}"><a href="javascript:${viewFunction}">[<u:convertMap srcId="mapList" attId="${colId}" type="html" />]${mapList.subj}</a></div></c:when>
								
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
