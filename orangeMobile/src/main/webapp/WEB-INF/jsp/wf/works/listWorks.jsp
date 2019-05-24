<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="params" 
/><u:params var="paramsForList" excludes="workNo,noCache" />
<script type="text/javascript">
//<![CDATA[
function searchList(event){
	$m.nav.curr(event, '/wf/works/listWorks.do?'+$('#searchForm').serialize());
}
<c:if test="${mobRegTypCd ne 'U' }">
<% // [등록:상단버튼] - 등록화면 이동 %>
function setWorks() {
	$m.nav.next(null, '/wf/works/setWorks.do?${params}');
}
</c:if>
<c:if test="${mobRegTypCd eq 'U' }">
<% // [등록:상단버튼] - 웹버전 등록화면 팝업 출력 %>
function setWorksWeb() {
	var param={};
	param['formNo']='${param.formNo}';
	param['mode']='set';
	$m.ajax('/wf/works/getWorksUrlAjx.do?menuId=${menuId}', param, function(data) {
		if(data.message !=null){
			$m.dialog.alert(data.message);
		}
		if(data.webUrl!=null) {
			var url=data.webUrl;
			url+=url.indexOf('?') > -1 ? "&" : "?";
			url+="isMobile=Y";
			window.open(url, "setWorksWin");
		}
	});
}
<% // [새로고침] - 팝업 등록화면에서 저장후 새로고침 %>
function reloadOpen(){
	$m.nav.reload();
}
</c:if>
<% // [목록:제목] 게시물 조회 %>
function viewWorks(id) {
	$m.nav.next(null, '/wf/works/viewWorks.do?${paramsForList}&workNo=' + id);
}

var holdHide = false, holdHide2 = false;
$(document).ready(function() {
	$('#listArea dd').find('a, input[type="checkbox"]').click(function(event){
		if(event.stopPropagation) event.stopPropagation(); //MOZILLA
		else event.cancelBubble = true; //IE
	});
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('list');
});

//]]>
</script>

<section>
<c:if test="${!empty lstDispList }">
<div class="btnarea">
    <div class="size">
        <dl>
           	 <c:if test="${mobRegTypCd ne 'U' }"><dd class="btn" onclick="setWorks();"><u:msg titleId="cm.btn.write" alt="등록" /></dd></c:if>
           	 <c:if test="${mobRegTypCd eq 'U' }"><dd class="btn" onclick="setWorksWeb();"><u:msg titleId="cm.btn.write" alt="등록" /></dd></c:if>
     </dl>
    </div>
</div>
</c:if>

<% // 검색영역 %>
<jsp:include page="/WEB-INF/jsp/wf/works/listWorksSrch.jsp" />

<!--section S-->

<div class="listarea" id="listArea">
	<article>
	<!-- 목록 -->
	<c:if test="${fn:length(mapList) == 0}">
     <div class="listdiv_nodata" ><dl><dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd></dl></div>
	</c:if>
	<c:if test="${!empty lstDispList }"><u:convertJson var="jsonVa" value="${wfFormRegDVo.attrVa }" 
/><c:forEach var="map" items="${mapList}" varStatus="listStatus">	<c:set var="map" value="${map }" scope="request"/>
		<div class="listdiv" onclick="viewWorks('${map.workNo}');">
	        <div class="list">
	        	<dl><c:forEach var="wfFormLstDVo" items="${lstDispList }" varStatus="status"
		><c:set var="colmId" value="${wfFormLstDVo.colmId }"
		/><c:set var="colmTyp" value="${wfFormLstDVo.colmTyp }"
		/><c:set var="jsonMap" value="${jsonVa[wfFormLstDVo.colmNm] }" scope="request"
		/><u:set var="titStyle" test="${status.first }" value=" style=\"font-weight:bold;\"" elseValue=""
		/><u:set var="skipTitleYn" test="${(status.first && colmTyp ne 'file' ) || colmTyp eq 'image' }" value="Y" elseValue="N"
		/><dd class="${status.first ? 'tit' : 'name'}"${titStyle }><c:if test="${skipTitleYn eq 'N' }"><u:out value="${wfFormLstDVo.itemNm }"/> : </c:if><c:set var="dataVa" value="${map[wfFormLstDVo.colmNm] }"
		/><c:choose><c:when test="${colmTyp eq 'user' || colmTyp eq 'dept'}"
		><c:set var="dataVa" value="${map.cdListMap[wfFormLstDVo.colmNm] }"
		/><c:if test="${!empty dataVa }"><c:forEach var="codeVo" items="${dataVa }" varStatus="codeStatus"
		><c:if test="${!codeStatus.first }">, </c:if><c:if test="${colmTyp eq 'user' }"><a href="javascript:;" onclick="$m.user.viewUserPop('${codeVo.cdVa}')">${codeVo.cdNm }</a></c:if
		><c:if test="${colmTyp ne 'user' }">${codeVo.cdNm }</c:if></c:forEach></c:if></c:when
		><c:when test="${colmTyp eq 'select' || colmTyp eq 'radio' || colmTyp eq 'checkbox'}"
		><c:if test="${empty jsonMap['chkTypCd'] }"><u:out value="${dataVa }"/></c:if
		><c:if test="${!empty jsonMap['chkTypCd'] }"><c:set var="dataVa" value="${map.cdListMap[wfFormLstDVo.colmNm] }"
		/><c:if test="${!empty dataVa }"><c:forEach var="codeVo" items="${dataVa }" varStatus="codeStatus"
		><c:if test="${!codeStatus.first }">, </c:if>${codeVo.cdNm }</c:forEach></c:if></c:if></c:when
		><c:when test="${colmTyp eq 'file'}"
		><c:if test="${map.fileCnt>0}">${map.fileCnt }</c:if></c:when
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
		><a href="javascript:;" onclick="$m.user.viewUserPop('${map[colmId]}')">${map[colmTyp]}</a></c:when
		><c:when test="${colmTyp eq 'regDt' || colmTyp eq 'modDt'}"
		><u:out value="${map[colmTyp] }" type="longdate"/></c:when
		><c:when test="${colmTyp eq 'editor'}"
		><div class="ellipsis" style="max-height:200px;"><u:out value="${dataVa }" type="value"/></div></c:when><c:otherwise>${dataVa }</c:otherwise>
		</c:choose></dd></c:forEach>
               </dl>
	        </div>
	    </div>
	</c:forEach></c:if>
	
	 </article>
    
</div>
<m:pagination />
    
<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
</section>
<!--//section E-->