<%@ page  language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"%>
<u:set test="${!empty param.fncCal}" var="fncCal" value="${param.fncCal}" />

<script type="text/javascript">
<!--
//등록 팝업
function setSchdlPop(schdlId){
	var url = "./${setPage}.do?${params}";
	var popTitle = '<u:msg titleId="wc.btn.schdlReg" alt="일정등록" />';
	if(arguments.length > 1){
		var schdlStartDt = arguments[0]+''+getDayVal(arguments[1])+''+getDayVal(arguments[2]);
		if(arguments.length == 4) schdlStartDt+= ''+getDayVal(arguments[3].split(':')[0])+''+arguments[3].split(':')[1];
		url+= "&schdlStartDt="+schdlStartDt;
	}else{
		if(schdlId != null) {
			url+= "&schdlId="+schdlId;
			popTitle = '<u:msg titleId="wc.btn.schdlMod" alt="일정수정" />';
		}	
	}
	dialog.open('setSchdlPop',popTitle,url);
};

//상세보기 팝업
function viewSchdlPop(schdlId) {

	parent.dialog.open('viewSchdlPop','<u:msg titleId="wc.btn.schdlDetail" alt="상세보기" />','/ct/schdl/viewSchdlPop.do?${params}&plt=y&schdlId='+schdlId+'');
};

$(document).ready(function() {
setUniformCSS();
});
//-->
</script>


<c:set var="admAuth" value="N"/>
<u:secu auth="SYS" ><c:set var="admAuth" value="${listPage eq 'listAllSchdl' ? 'Y' : 'N'}"/></u:secu>
<u:secu auth="A" ><c:set var="admAuth" value="${listPage eq 'listAllSchdl' ? 'Y' : 'N'}"/></u:secu>
<!-- 목록   -->

<table class="ptltable" id="ptltable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
	<tbody>
		<tr>
			<td width="15%" class="head_ct"><u:msg titleId="cols.schdlKnd" alt="일정종류"/></td>
			<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
			<td width="35%" class="head_ct"><u:msg titleId="cols.prd" alt="기간" /></td>
			
			<c:if test="${admAuth eq 'Y' }"><td width="10%" class="head_ct"><u:msg titleId="cols.regr" alt="작성자" /></td></c:if>
		</tr>
		<c:choose>
			<c:when test="${!empty CtSchdlBVoList}">
				<c:forEach  var="list" items="${CtSchdlBVoList}"  varStatus="status">
					<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
						
						<td class="body_ct">
							<div class="ellipsis" title="<u:out value="${list.schdlTypNm}"/>">
								<u:out value="${list.schdlTypNm}"/>			
							</div>
						</td>
						<td class="body_lt">
							<div class="ellipsis" title="<u:out value="${list.subj}"/>">
								<a href="javascript:viewSchdlPop('${list.schdlId}');"><u:out value="${list.subj}"/></a>
							</div>
						</td>
						<td class="body_ct" >
							<fmt:parseDate var="strtDate" value="${list.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
							<fmt:formatDate var="strtYmd" value="${strtDate}" pattern="yyyy-MM-dd"/> 
							<fmt:formatDate var="strtTime" value="${strtDate}" pattern="HH:mm"/>
							<fmt:parseDate var="endDate" value="${list.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
							<fmt:formatDate var="endYmd" value="${endDate}" pattern="yyyy-MM-dd"/> 
							<fmt:formatDate var="endTime" value="${endDate}" pattern="HH:mm"/>
							<c:choose>
								<c:when test="${list.alldayYn eq 'Y'}">${strtYmd }~${endYmd }</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${strtYmd eq endYmd}">${strtYmd }</c:when>
										<c:otherwise>${strtYmd }~${endYmd }</c:otherwise>
									</c:choose>
								(${strtTime} ~ ${endTime})
								</c:otherwise>
							</c:choose>
						</td>
						
						<c:if test="${admAuth eq 'Y' }"><td class="body_ct" ><a href="javascript:viewUserPop('${list.regrUid }');">${list.regrNm }</a></td></c:if>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
				<td class="nodata" colspan="${admAuth eq 'Y' ? '4' : '3' }"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
				</tr>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>
<u:listArea/>
<u:pagination noTotalCount="true" noBottomBlank="true"/>

