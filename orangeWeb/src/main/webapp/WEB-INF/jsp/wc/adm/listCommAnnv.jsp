<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<% // [하단버튼:설정] %>
function setEnvPop(){
	dialog.open('setEnvDialog','<u:msg titleId="cm.option.config" alt="설정" />','./setEnvPop.do?menuId=${menuId}');
}
<% // [하단버튼:국가목록설정] %>
function setNatListPop() {
	dialog.open('setNatListDialog','<u:msg titleId="wc.btn.set.nat.list" alt="국가목록설정" />','./setNatListPop.do?menuId=${menuId}');
}
<% // [하단버튼:기본설정(달력)] %>
function setNatPop() {
	dialog.open('setNatDialog','<u:msg titleId="wc.btn.set.nat.dft" alt="기본설정(달력)" />','./setNatPop.do?menuId=${menuId}');
}
function viewCommAnnv(schdlId) {
	
	//유일값 hidden으로 숨기기	
	$("#commAnnV_schdlId").val(schdlId);
	dialog.open('viewCommAnnvPop','<u:msg titleId="wc.jsp.viewAnnv.title" alt="기념일 조회" />','./viewCommAnnvPop.do?menuId=${menuId}&scds_schdlId='+schdlId+'');
}

//viewProm값 전달
function viewSearchMyScd(){
	var $selectRow=$("#"+$("#commAnnV_schdlId").val());
	//수정,삭제버튼 none flag;
	var searchMylist = "";
	var $commAnnv_schdlId = $("#commAnnV_schdlId").val();
	
	var $commAnnv_subj = $selectRow.find("#commAnnV_subj").val();
	var $commAnnv_locNm = $selectRow.find("#commAnnV_locNm").val();
	var $commAnnv_openGradCd = $selectRow.find("#commAnnV_openGradCd").val();
	var $commAnnv_cont = $selectRow.find("#commAnnV_cont").val();
	var $commAnnV_schdlStartDt = $selectRow.find("#commAnnV_schdlStartDt").val();
	var $commAnnV_schdlEndDt = $selectRow.find("#commAnnV_schdlEndDt").val();
	var $commAnnV_repetStartDt = $selectRow.find("#commAnnV_repetStartDt").val();
	var $commAnnV_repetEndDt = $selectRow.find("#commAnnV_repetEndDt").val();
	var $commAnnV_holiYn = $selectRow.find("#commAnnV_holiYn").val();
	var $commAnnV_solaLunaYn = $selectRow.find("#commAnnV_solaLunaYn").val();
	
	return {subj:$commAnnv_subj, loc:$commAnnv_locNm, openGradCd:$commAnnv_openGradCd, cont:$commAnnv_cont, solaLunaYn:$commAnnV_solaLunaYn,
		startYmd:$commAnnV_schdlStartDt, endYmd:$commAnnV_schdlEndDt, repetStDt:$commAnnV_repetStartDt, repetEnDt:$commAnnV_repetEndDt,holiYn:$commAnnV_holiYn,
		searchMylist:searchMylist, schdlId:$commAnnv_schdlId};
}

function betweenCalDay (startYmd, endYmd){

	var startDateArray = startYmd.split("-");
	var endDateArray = endYmd.split("-");  
	
	var startDateObj = new Date(startDateArray[0], Number(startDateArray[1])-1, startDateArray[2]);
	var endDateObj = new Date(endDateArray[0], Number(endDateArray[1])-1, endDateArray[2]);
	
	var betweenDay = (endDateObj.getTime() - startDateObj.getTime())/1000/60/60/24; 
	//ex) (2014-05-20) - (2014-05-15) = 5일이지만 당일 포함 6일간 이니 +1 해줌
	return betweenDay + 1;
}


$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="wc.jsp.listCommAnnv.title" alt="공통기념일목록" menuNameFirst="true"/>
<input id="commAnnV_schdlId" name="commAnnV_schdlId" type="hidden" />

<% // 목록 %>
<u:listArea id="listArea" colgroup=",17%,17%,15%,10%,15%">
	<tr>
	<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td class="head_ct"><u:msg titleId="cols.strtDt" alt="시작일시" /></td>
	<td class="head_ct"><u:msg titleId="cols.endDt" alt="종료일시" /></td>
	<td class="head_ct"><u:msg titleId="wc.cols.repet" alt="반복" /></td>
	<td class="head_ct"><u:msg titleId="cols.holiYn" alt="휴일여부" /></td>
	<td class="head_ct"><u:msg titleId="wc.btn.set.nat" alt="국가설정" /></td>
	</tr>
	
	<c:forEach var="commAnnv" items="${commonAnnv}" varStatus="status">
		<c:if test="${commAnnv.schdlId == commAnnv.schdlPid}">
			<u:convert srcId="wcAnnvDVo${commAnnv.schdlId }" var="wcAnnvDVoMap"/>
			<tr onmouseover='this.className="trover"' id="${commAnnv.schdlId}" onmouseout='this.className="trout"'>
			<c:set var="viewFunction"	value= "${commAnnv.schdlId}" />
			<td class="body_lt">
				<u:set var="subj" test="${!empty wcAnnvDVoMap }" value="${wcAnnvDVoMap.rescNm }" elseValue="${commAnnv.subj }"/>
				<a href="javascript:viewCommAnnv('${commAnnv.schdlId}')"><u:out value="${subj}" maxLength="80"/></a>
				<input id="commAnnV_schdlId" type = "hidden" value="${commAnnv.schdlId}"/>
				<input id="commAnnV_subj" name="commAnnV_subj" type = "hidden" value="${commAnnv.subj}" />
	
				<input id="commAnnV_solaLunaYn" type="hidden" value="${commAnnv.solaLunaYn}"/>
				<input id="commAnnV_holiYn" type="hidden" value="${commAnnv.holiYn}"/>
	
				<input id="commAnnV_locNm" type="hidden" value="${commAnnv.locNm}"/>
				<input id="commAnnV_openGradCd" type="hidden" value="${commAnnv.openGradCd}"/>
				<input id="commAnnV_cont" type="hidden" value="${commAnnv.cont}"/>
	
			</td>
			<td class="body_ct">
				<fmt:parseDate var="startDtParse" value="${commAnnv.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:parseDate> 
				<fmt:formatDate var="startDtFmt" value="${startDtParse}" pattern="yyyy-MM-dd"/>
				<fmt:formatDate var="startDt" value="${startDtParse}" pattern="yyyy-MM-dd"/>
				<c:out value="${startDt}"/>
				<c:if test="${commAnnv.solaLunaYn == 'Y'}">	(<u:msg titleId="wc.option.sola" alt="양" />) </c:if>
				<c:if test="${commAnnv.solaLunaYn == 'N'}"> (<u:msg titleId="wc.option.luna" alt="음" />) </c:if>
			</td>
			<td class="body_ct">
				<fmt:parseDate var="endDtParse" value="${commAnnv.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:parseDate>
				<fmt:formatDate var="endDtFmt" value="${endDtParse}" pattern="yyyy-MM-dd"/>
				<fmt:formatDate var="endDt" value="${endDtParse}" pattern="yyyy-MM-dd"/>
				<c:out value="${endDt}"/>
				<c:if test="${commAnnv.solaLunaYn == 'Y'}">	(<u:msg titleId="wc.option.sola" alt="양" />) </c:if>
				<c:if test="${commAnnv.solaLunaYn == 'N'}"> (<u:msg titleId="wc.option.luna" alt="음" />) </c:if>
			</td>
			<td class="body_ct">
				<fmt:parseDate var="repetStartDtParse" value="${commAnnv.repetStartDt}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:parseDate>
				<fmt:parseDate var="repetEndDtParse" value="${commAnnv.repetEndDt}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:parseDate>
					<c:if test="${commAnnv.repetStartDt == null || commAnnv.repetStartDt == ''}">
						<fmt:formatDate value="${startDtParse}" pattern="yyyy"/>
					</c:if>
						<c:if test="${commAnnv.repetStartDt != null}">
							<fmt:formatDate var="repetStartDtFmt" value="${repetStartDtParse}" pattern="yyyy"/>
							<fmt:formatDate var="repetEndDtFmt" value="${repetEndDtParse}" pattern="yyyy"/>
								<c:if test="${repetStartDtFmt == repetEndDtFmt}">
									<c:out value="${repetStartDtFmt}"/>
								</c:if>
								<c:if test="${repetStartDtFmt != repetEndDtFmt}">
									<c:out value="${repetStartDtFmt}"/> ~
									<c:out value="${repetEndDtFmt}"/>
								</c:if>
						</c:if>
			 	<input id="commAnnV_schdlStartDt" type="hidden" value="${startDtFmt}"/>
				<input id="commAnnV_schdlEndDt" type="hidden" value="${endDtFmt}"/>
				<input id="commAnnV_repetStartDt" type="hidden" value="${repetStartDtFmt}"/>
				<input id="commAnnV_repetEndDt" type="hidden" value="${repetEndDtFmt}"/>
			</td>
			<td class="body_ct">
				<c:if test="${commAnnv.holiYn == 'Y'}"> <u:msg titleId="wc.option.commHoli" alt="공휴일" /> </c:if>
				<c:if test="${commAnnv.holiYn == 'N'}"> <u:msg titleId="wc.option.wday" alt="평일" /></c:if>	
			</td>
			<td class="body_ct">
				<u:convert srcId="wcNatBVo${commAnnv.schdlId }" var="wcNatBVoMap"/>
				<u:set var="natNm" test="${!empty wcNatBVoMap }" value="${wcNatBVoMap.rescNm }" elseValue=""/>
				<u:out value="${natNm}" maxLength="80"/>
			</td>
			</tr>
		</c:if>
	</c:forEach>
	<c:if test="${fn:length(commonAnnv) == 0}">
			<tr>
				<td class="nodata" colspan="6"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
	</c:if>
</u:listArea>

<u:pagination />

<% // 하단 버튼 %>
<u:msg titleId="wc.btn.annvReg" alt="기념일등록" var="annvReg"/>
<u:buttonArea>
	<u:button titleId="cm.btn.setup" alt="설정" href="javascript:setEnvPop();" auth="A" />
	<u:button titleId="wc.btn.set.nat.dft" alt="기본설정(달력)" href="javascript:setNatPop();" auth="A" />
	<u:button titleId="wc.btn.set.nat.list" alt="국가목록설정" href="javascript:setNatListPop();" auth="A" />
	<u:button titleId="cm.btn.reg" alt="등록" href="javascript:dialog.open('setCommAnnvPop','${annvReg }','./setCommAnnvPop.do?menuId=${menuId}&fnc=reg&fncAdm=Y');" auth="W" />
</u:buttonArea>
