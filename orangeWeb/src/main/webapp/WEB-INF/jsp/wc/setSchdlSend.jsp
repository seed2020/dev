<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

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
	emailSendPop({startDay:$("#startDay").val(),listType:$(':radio[name="listType"]:checked').val(),schSchdlKndCd:schSchdlKndCd,schSchdlTypCd:schSchdlTypCd}, '${menuId }');
};

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="wc.jsp.setSchdlSend.title" alt="일정 보내기" menuNameFirst="true"/>

<u:input type="hidden" id="menuId" value="${menuId}" />

<!--  검색영역  -->
<u:searchArea id="schListArea">
<form name="searchForm" >
<u:input type="hidden" id="menuId" value="${menuId}" />

<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<colgroup><col width="8%"/><col width="45%"/><col width="2%"/><col width="8%"/><col width="37%"/></colgroup>
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
					<td class="search_tit"><u:msg titleId="cols.choiDt" alt="선택일시" /></td>
					<td>
						<fmt:parseDate var="strtDate" value="${startDay}" pattern="yyyy-MM-dd HH:mm:ss"/>
						<fmt:formatDate var="strtYmd" value="${strtDate}" pattern="yyyy-MM-dd"/> 
						<u:calendar id="startDay"  titleId="cols.choiDt" alt="선택일시" mandatory="Y" value="${strtYmd }"/>
					</td>
				</tr>
				
				<tr>
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
					<td class="width20"></td>
					<td class="search_tit"><u:msg titleId="cols.opt" alt="옵션" /></td>
					<td>
						<u:checkArea>
							<u:radio name="listType" value="daly" titleId="wc.option.daly" inputClass="bodybg_lt"  checked="true"/>
							<u:radio name="listType" value="wely" titleId="wc.option.wely" inputClass="bodybg_lt" />
							<u:radio name="listType" value="moly" titleId="wc.option.moly" inputClass="bodybg_lt" />
						</u:checkArea>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</form>
</u:searchArea>

<u:buttonArea>
	<u:button titleId="cm.btn.sendMail" onclick="sendEmail();" alt="메일 쓰기" auth="R" />
</u:buttonArea>

