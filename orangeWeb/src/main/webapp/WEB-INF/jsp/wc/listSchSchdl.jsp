<%@ page  language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"%>
<u:set test="${!empty param.fncCal}" var="fncCal" value="${param.fncCal}" />

<script type="text/javascript">
<!--
//메일발송 팝업
function sendEmail() {
	//일정대상 조회
	var schSchdlKndCd = "";
	var checkKndCds = [];
	$('input:checkbox[name="schSchdlKndCd"]:checked').each(function(){
		checkKndCds.push($(this).val());
	});
	if(checkKndCds.length > 0 ){
		schSchdlKndCd = checkKndCds.join(','); 
	}
	
	//일정종류 조회
	var schSchdlTypCd = "";
	checkKndCds = [];
	$('input:checkbox[name="schSchdlTypCd"]:checked').each(function(){
		checkKndCds.push($(this).val());
	});
	if(checkKndCds.length > 0 ){
		schSchdlTypCd = checkKndCds.join(','); 
	}
	emailSendPop({schdlStartDt:$("#schdlStartDt").val(),schdlEndDt:$("#schdlEndDt").val(),schWord:$("#schWord").val(),schSchdlKndCd:schSchdlKndCd,schSchdlTypCd:schSchdlTypCd}, '${menuId }');
};

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
	dialog.open('viewSchdlPop','<u:msg titleId="wc.btn.schdlDetail" alt="상세보기" />','./${viewPage}.do?${params}&schdlId='+schdlId+'');
};
<% // 리로드 %>
function reloadCalendar(listPage){
	location.replace(listPage);
}
$(document).ready(function() {
setUniformCSS();
});
//-->
</script>

<u:title titleId="wc.jsp.listSchedl.title" alt="일정검색" menuNameFirst="true"/>

<!--  검색영역  -->
<u:searchArea id="schListArea">
<form name="searchForm" >
<u:input type="hidden" id="menuId" value="${menuId}" />

<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<colgroup><col width="*"/><col width="100px"/></colgroup>
	<tr>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<colgroup><col width="8%"/><col width="45%"/><col width="2%"/><col width="5%"/><col width="40%"/></colgroup>
				<tr>
					<td class="search_tit"><u:msg titleId="wc.cols.schdlKndCd" alt="일정대상"/></td>
					<td>
						<u:checkArea id="schdlKndCdArea" >
							<u:checkbox id="checkHeader1" name="checkHeader1" value="" titleId="cm.check.all" alt="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('schdlKndCdArea', this.checked);"/>
							<c:forEach  var="list" items="${schdlKndCdList}" varStatus="status">
								<c:set var="checked" value="N"/>
								<c:forEach var="schSchdlKnd" items="${schSchdlKndCds }" varStatus="subStatus">
									<c:if test="${schSchdlKnd eq list[0]}"><c:set var="checked" value="Y"/></c:if>
								</c:forEach>
								<u:checkbox id="schSchdlKndCd${status.index }" name="schSchdlKndCd" value="${list[0]}" title="${list[1] }" alt="${list[1] }" checked="${checked eq 'Y' }"/>
							</c:forEach>
						</u:checkArea>
					</td>
					<td class="width20"></td>
					<td class="search_tit"><u:msg titleId="cols.prd" alt="기간" /></td>
					<td>
						<table border="0" cellpadding="0" cellspacing="0">
							<tbody>
							<tr>
								<td>
									<u:calendar id="schdlStartDt" option="{end:'schdlEndDt'}" titleId="cols.choiDt" alt="선택일시" value="${param.schdlStartDt }"/>
								</td>
								<td class="body_lt">~</td>
								<td>
									<u:calendar id="schdlEndDt" option="{start:'schdlStartDt'}" titleId="cols.cmltDt" alt="완료일시" value="${param.schdlEndDt }"/>
								</td>
							</tr>
							</tbody>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="5">
						<table border="0" cellpadding="0" cellspacing="0" >
							<tr>
								<td class="search_tit"><u:msg titleId="cols.subj" alt="제목" /></td>
								<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.subj" style="width: 110px;" onkeydown="if (event.keyCode == 13) searchForm.submit();"/></td>
								<td class="width20"></td>
								<td class="search_tit"><u:msg titleId="cols.schdlKnd" alt="일정종류"/></td>
								<td>
									<u:checkArea2 id="schdlTypCdArea" >
										<u:checkbox2 id="checkHeader2" name="checkHeader2" value="" titleId="cm.check.all" alt="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('schdlTypCdArea', this.checked);"/>
										<c:forEach  var="list" items="${wcCatClsBVoList}" varStatus="status">
											<c:set var="checked" value="N"/>
											<c:forEach var="schSchdlTyp" items="${schSchdlTypCds }" varStatus="subStatus">
												<c:if test="${schSchdlTyp eq list.catId}"><c:set var="checked" value="Y"/></c:if>
											</c:forEach>
											<u:checkbox2 id="schSchdlTypCd${status.index }" name="schSchdlTypCd" value="${list.catId}" title="${list.catNm }" alt="${list.catNm }" checked="${checked eq 'Y' }"/>
										</c:forEach>
									</u:checkArea2>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
		<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
</table>
</form>
</u:searchArea>

<c:set var="admAuth" value="N"/>
<u:secu auth="SYS" ><c:set var="admAuth" value="${listPage eq 'listAllSchdl' ? 'Y' : 'N'}"/></u:secu>
<u:secu auth="A" ><c:set var="admAuth" value="${listPage eq 'listAllSchdl' ? 'Y' : 'N'}"/></u:secu>
<!-- 목록   -->
<u:listArea id="listArea" colgroup="10%,10%,*,25%,${admAuth eq 'Y' ? '10%,10%' : '10%' }">
	<tr>
		<td class="head_ct"><u:msg titleId="wc.cols.schdlKndCd" alt="일정대상"/></td>
		<td class="head_ct"><u:msg titleId="cols.schdlKnd" alt="일정종류"/></td>
		<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
		<td class="head_ct"><u:msg titleId="cols.prd" alt="기간" /></td>
		<td class="head_ct"><u:msg titleId="cols.note" alt="비고" /></td>
		<c:if test="${admAuth eq 'Y' }"><td class="head_ct"><u:msg titleId="cols.regr" alt="작성자" /></td></c:if>
	</tr>
	<c:choose>
		<c:when test="${!empty wcSchdlBVoList}">
			<c:forEach  var="list" items="${wcSchdlBVoList}"  varStatus="status">
				<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
					<td class="body_ct">
						<c:choose>
							<c:when test="${list.schdlKndCd == 1}"><u:msg titleId="wc.jsp.listPsnSchdl.psn.title" alt="개인일정"/></c:when>
							<c:when test="${list.schdlKndCd == 2}"><u:msg titleId="wc.jsp.listPsnSchdl.grp.title" alt="그룹일정"/></c:when>
							<c:when test="${list.schdlKndCd == 3}"><u:msg titleId="wc.jsp.listPsnSchdl.dept.title" alt="부서일정"/></c:when>
							<c:when test="${list.schdlKndCd == 4}"><u:msg titleId="wc.jsp.listPsnSchdl.comp.title" alt="회사일정"/></c:when>
						</c:choose>
					</td>
					<td class="body_ct">
						<!-- 추후 삭제 -->
						<c:choose>
							<c:when test="${list.schdlTypCd == '1'}"><u:msg titleId="wc.cols.prom" alt="약속"/></c:when>
							<c:when test="${list.schdlTypCd == '2'}"><u:msg titleId="wc.cols.work" alt="할일"/></c:when>
							<c:when test="${list.schdlTypCd == '3'}"><u:msg titleId="wc.cols.evnt" alt="행사"/></c:when>
							<c:when test="${list.schdlTypCd == '4'}"><u:msg titleId="wc.cols.annv" alt="기념일"/></c:when>
							<c:otherwise>${list.schdlTypNm }</c:otherwise>
						</c:choose>
					</td>
					<td class="body_lt"><div class="ellipsis"><a href="javascript:viewSchdlPop('${list.schdlId}');">${list.subj}</a></div></td>
					<td class="body_lt" >
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
					<td class="body_ct" >${list.afterDay+1} <u:msg titleId="wc.jsp.viewWorkPop.tx02" alt="일간" /></td>
					<c:if test="${admAuth eq 'Y' }"><td class="body_ct" ><a href="javascript:viewUserPop('${list.regrUid }');">${list.regrNm }</a></td></c:if>
				</tr>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<tr>
			<td class="nodata" colspan="${admAuth eq 'Y' ? '6' : '5' }"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
		</c:otherwise>
	</c:choose>
</u:listArea>
<u:pagination />
<u:blank />
<u:buttonArea>
	<u:button titleId="cm.btn.sendMail" onclick="sendEmail();" alt="메일 쓰기" auth="R" />
</u:buttonArea>

