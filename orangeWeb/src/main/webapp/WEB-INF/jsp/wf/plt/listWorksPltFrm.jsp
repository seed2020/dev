<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:authUrl var="viewUrl" url="/wf/works/viewWorks.do?formNo=${param.formNo }" authCheckUrl="/wf/works/listWorks.do?formNo=${param.formNo }"/><!-- view page 호출관련 url 조합(menuId추가) -->
<u:authUrl var="viewFileListUrl" url="/wf/works/viewFileListPop.do?formNo=${param.formNo }" authCheckUrl="/wf/works/listWorks.do?formNo=${param.formNo }"/><!-- 파일목록 팝업 page 호출관련 url 조합(menuId추가) -->
<script type="text/javascript">
<!--
//상세보기
function viewWorks(workNo) {
	var authUrl = '${viewUrl}';
	var prefix = authUrl.indexOf('?') > -1 ? "&" : "?";
	authUrl+=prefix+"workNo="+workNo;
	top.location.href=authUrl;
};
<% // [팝업] 파일목록 조회 %>
function viewFileListPop(workNo) {
	var url = '${viewFileListUrl}&workNo='+workNo;
	top.dialog.open('viewFileListDialog','<u:msg titleId="cols.att" alt="첨부" />', url);
}

<%

//자동숨김 - 3
//포틀릿 테이블 - 테이블ID %>
var gPltTableId = null;<%
//포틀릿 테이블 - 첫째 컬럼 최소 가로 길이(타입:number, 단위:px) %>
var gPltTableMinFirst = null;<%
//포틀릿 테이블 - 각 컬럼 비율 (예: "40,20,20,20") %>
var gPltTableRatio = null;<%
//포틀릿 테이블 - 가로폭에 따른 컬럼 숨기기, 보이기 %>
function resizeForPltTable(tableClass, minFirst, ratio){
	gPltTableId = tableClass;
	gPltTableMinFirst = minFirst;
	gPltTableRatio = [];
	ratio.split(',').each(function(index, va){
		gPltTableRatio.push(parseInt(va, 10));
	});
	onResizeForPltTable();
	$(window).resize(onResizeForPltTable);
}<%
//포틀릿 테이블 - 가로폭에 따른 컬럼 숨기기, 보이기 - 실제 함수 %>
function onResizeForPltTable(){
	if(gPltTableId==null) return;
	var $table = $("#"+gPltTableId), tableWidth = $table.width(), showIndex=0, ratioSum=0;
	while(true){
		ratioSum += gPltTableRatio[showIndex];
		if(parseInt((gPltTableRatio[0]/ratioSum) * tableWidth) < gPltTableMinFirst){
			break;
		}
		if(showIndex==gPltTableRatio.length-1) break;
		showIndex++;
	}
	var $colgroup = $table.find("colgroup");
	var i, colCnt = $colgroup.find("col").length, percent, percentSum=0;
	for(i=0;i<colCnt;i++){
		if(i==showIndex){
			percent = 100 - percentSum;
			$colgroup.find("col:eq("+i+")").attr("width", percent+"%");
		} else if(i<showIndex){
			percent = parseInt((gPltTableRatio[i]/ratioSum)*100 ,10);
			percentSum += percent;
			$colgroup.find("col:eq("+i+")").attr("width", percent+"%");
		} else {
			$colgroup.find("col:eq("+i+")").attr("width", "0%").hide();
		}
	}
	var $trs = $table.find("tbody tr[id!='lineTr']");
	for(i=0;i<colCnt;i++){
		if(i<=showIndex){
			$trs.find("td:eq("+i+")").show();
		} else {
			$trs.find("td:eq("+i+")").hide();
		}
	}
}
<% // 가로길이 배열 리턴 %>
function getTdWidths(){
	var tdWidths="", width;
	$.each($('#colgroup > col'), function(idx){
		width=$(this).attr('width');
		if(width.indexOf('%')>-1) width=width.replace('%', '');
		if(idx>0) tdWidths+=',';
		tdWidths+=width;
	});
	return tdWidths;
}

$(document).ready(function() {
	// 자동숨김 - 2
	// 텝 - 가로폭에 따라서 컬럼을 숨김 %>
	resizeForPltTable('ptltable', 100, getTdWidths());
	var qstr = '${queryString}';
	if(qstr.indexOf('pageRowCnt')<0) qstr += '&pageNo=1&pageRowCnt=${pageRowCnt}';
	parent.setQueryString(qstr);
	
	$('#ptltable tbody:first tr').find('a, input[type="checkbox"]').click(function(event){
		if(event.stopPropagation) event.stopPropagation(); //MOZILLA
		else event.cancelBubble = true; //IE
	});
});
//-->
</script>
<% // 목록 %><c:if test="${!empty lstDispList }"><u:convertJson var="jsonVa" value="${wfFormRegDVo.attrVa }" />
<table class="ptltable" id="ptltable" border="0" cellpadding="0" cellspacing="0" style="width:100%; table-layout:fixed;"><%
	// 자동숨김 - 5 - colgroup %>
	<colgroup id="colgroup"><c:forEach items="${lstDispList}" var="wfFormLstDVo" varStatus="status"><u:set var="wdthPerc" test="${empty wfFormLstDVo.wdthPerc }" value="5%" elseValue="${wfFormLstDVo.wdthPerc }"/><col width="${wdthPerc }"></c:forEach></colgroup>
	<c:set var="colspan" value="${fn:length(lstDispList)}"/>
	<tbody><c:if
		test="${param.colYn != 'N'}">
	<tr id="lineTr"><td colspan="${colspan }" class="line"></td></tr>
	<tr id="headerTr"><c:forEach var="wfFormLstDVo" items="${lstDispList }" varStatus="status"
		><td class="head_ct"><u:out var="title" value="${wfFormLstDVo.itemNm }"/><div class="ellipsis" title="${title }">${title }</div></td></c:forEach>
	</tr></c:if>
	<tr id="lineTr"><td colspan="${colspan }" class="line"></td></tr>
	<c:if test="${fn:length(mapList) == 0}">
		<tr>
		<td class="nodata" colspan="${colspan}"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
	<c:forEach items="${mapList}" var="map" varStatus="status"><c:set var="map" value="${map }" scope="request"/>
		<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"' onclick="viewWorks('${map.workNo}');">
			<c:forEach var="wfFormLstDVo" items="${lstDispList }" varStatus="status"
		><c:set var="colmId" value="${wfFormLstDVo.colmId }"
		/><c:set var="colmTyp" value="${wfFormLstDVo.colmTyp }"
		/><c:set var="jsonMap" value="${jsonVa[wfFormLstDVo.colmNm] }" scope="request"
		/><td class="body_${wfFormLstDVo.alnVa eq 'center' ? 'ct' : wfFormLstDVo.alnVa eq 'right' ? 'rt' : 'lt'}"
		><c:set var="dataVa" value="${map[wfFormLstDVo.colmNm] }"
		/><c:choose><c:when test="${colmTyp eq 'user' || colmTyp eq 'dept'}"
		><c:set var="dataVa" value="${map.cdListMap[wfFormLstDVo.colmNm] }"
		/><c:if test="${!empty dataVa }"><c:forEach var="codeVo" items="${dataVa }" varStatus="codeStatus"
		><c:if test="${!codeStatus.first }">, </c:if><c:if test="${colmTyp eq 'user' }"><a href="javascript:;" onclick="viewUserPop('${codeVo.cdVa}')">${codeVo.cdNm }</a></c:if
		><c:if test="${colmTyp ne 'user' }">${codeVo.cdNm }</c:if></c:forEach></c:if></c:when
		><c:when test="${colmTyp eq 'select' || colmTyp eq 'radio' || colmTyp eq 'checkbox'}"
		><c:if test="${empty jsonMap['chkTypCd'] }"><u:out value="${dataVa }"/></c:if
		><c:if test="${!empty jsonMap['chkTypCd'] }"><c:set var="dataVa" value="${map.cdListMap[wfFormLstDVo.colmNm] }"
		/><c:if test="${!empty dataVa }"><c:forEach var="codeVo" items="${dataVa }" varStatus="codeStatus"
		><c:if test="${!codeStatus.first }">, </c:if><c:if test="${colmTyp eq 'user' }"><a href="javascript:;" onclick="viewUserPop('${codeVo.cdVa}')">${codeVo.cdNm }</a></c:if
		><c:if test="${colmTyp ne 'user' }">${codeVo.cdNm }</c:if></c:forEach></c:if></c:if></c:when
		><c:when test="${colmTyp eq 'file'}"
		><c:if test="${map.fileCnt>0}"><a href="javascript:viewFileListPop('${map.workNo}');"><u:icon type="att" /></a></c:if></c:when
		><c:when test="${colmTyp eq 'image'}"
		><u:set var="viewSizeTyp" test="${!empty jsonMap['viewSizeTyp'] && jsonMap['viewSizeTyp'] eq '%' }" value="per" elseValue="px" 
		/><u:set var="maxWdth" test="${!empty jsonMap['viewWdth'] }" value="${jsonMap['viewWdth'] }" elseValue="88" 
		/><u:set var="maxHght" test="${!empty jsonMap['viewHeight'] }" value="${jsonMap['viewHeight'] }" elseValue="${viewSizeTyp eq 'px' ? '110' : '100' }" 
		/><c:set var="imgVo" value="${!empty map ? map.imgListMap[wfFormLstDVo.colmNm] : ''}"
		/><c:if test="${!empty imgVo}"><u:set test="${!empty imgVo.imgPath}" var="imgPath" value="${_cxPth}${imgVo.imgPath}" elseValue="${_cxPth}/images/${_skin}/photo_noimg.png" 
		/><c:if test="${viewSizeTyp eq 'px'}"><u:set test="${imgVo.imgWdth <= maxWdth}" var="imgWdth" value="${imgVo.imgWdth}" elseValue="${maxWdth}" 
		/><u:set test="${imgVo.imgHght <= maxHght}" var="imgHght" value="${imgVo.imgHght}" elseValue="${maxHght}" 
		/><u:set test="${imgVo.imgWdth < imgVo.imgHght}" var="imgWdthHgth" value="height='${imgHght}'" elseValue="width='${imgWdth}'" 
		/></c:if><c:if test="${viewSizeTyp eq 'per'}"
		><u:set test="${maxWdth < maxHght}" var="imgWdthHgth" value="height='${maxHght}%'" elseValue="width='${maxWdth}%'" 
		/></c:if></c:if><div class="image_profile"><dl><dd class="photo"><c:if test="${!empty imgVo}"><a href="javascript:viewImagePop('${componentId }');" 
		><img src="${imgPath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' ${imgWdthHgth}
		></a></c:if><c:if test="${empty imgVo }"><img src="${_cxPth}/images/${_skin}/photo_noimg.png" width="88px"></c:if></dd></dl></div
		></c:when><c:when test="${colmTyp eq 'period'}"
		><u:convertMap var="periodVa1" srcId="map" attId="${wfFormLstDVo.colmNm }_1" 
		/><c:if test="${!empty periodVa1 }">${periodVa1 }</c:if
		> ~ <u:convertMap var="periodVa2" srcId="map" attId="${wfFormLstDVo.colmNm }_2" 
		/><c:if test="${!empty periodVa2 }">${periodVa2 }</c:if></c:when><c:when test="${colmTyp eq 'regrNm' || colmTyp eq 'modrNm'}"
		><a href="javascript:;" onclick="viewUserPop('${map[colmId]}')">${map[colmTyp]}</a></c:when
		><c:when test="${colmTyp eq 'regDt' || colmTyp eq 'modDt'}"
		><u:out value="${map[colmTyp] }" type="longdate"/></c:when
		><c:when test="${colmTyp eq 'editor'}"
		><u:out value="${dataVa }" type="value"/></c:when><c:otherwise><div class="ellipsis" title="${dataVa }">${dataVa }</div></c:otherwise>
		</c:choose></td></c:forEach>
		</tr>
		<tr id="headerTr">
		  <td colspan="${colspan}" class="line"></td>
		</tr>
	</c:forEach>
	
	</tbody>
</table>
<u:blank />
<u:pagination noTotalCount="true" noBottomBlank="true"  pltBlock="true"/></c:if>