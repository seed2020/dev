<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
%><script type="text/javascript">
<!--<%--
[소버튼] 임직원 / 외주직원 클릭 --%>
function setManPower(which){
	if(which=='emp'){
		var data = {};
		searchUserPop({data:data}, function(userVo){
			if(userVo!=null){
				var $form = $("#searchForm1");
				$form.find("input[name=mpId]").val(userVo.userUid);
				$form.find("input[name=mpTypCd]").val('emp');
				$form.submit();
			}
		});
	} else {
		dialog.open("listOutsDialog", '<u:msg titleId="wp.outsourcing" alt="외주 직원" />', "./listOutsPop.do?menuId=${menuId}&cat=${param.cat}&mode=single");
	}
}<%--
[기능] 외주직원 팝업 콜백 --%>
function setOutsourcingData(arr){
	if(arr!=null && arr.length>0){
		var $form = $("#searchForm1");
		$form.find("input[name=mpId]").val(arr[0].id);
		$form.find("input[name=mpTypCd]").val('out');
		$form.submit();
	}
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title alt="개인별 집계" menuNameFirst="true" />

<%-- // 검색영역 --%>
<u:searchArea style="position:relative; z-index:2;">
	<form id="searchForm1" name="searchForm1" action="${_uri}" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="cat" value="${param.cat}" />
	<u:input type="hidden" id="mpId" value="${param.mpId}" />
	<u:input type="hidden" id="mpTypCd" value="${param.mpTypCd}" />
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td>
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="pt.jsp.setAuthGrp.user" alt="사용자" /></td>
		<td><table border="0" cellpadding="0" cellspacing="0"><tr>
		<td><u:input id="mpNm" titleId="pt.jsp.setAuthGrp.user"
			style="width:80px" readonly="Y" value="${not empty orUserBVo ? orUserBVo.rescNm : wpMpBVo.mpNm}" /></td>
		<td><u:buttonS titleId="cm.option.empl" alt="임직원" onclick="setManPower('emp');"/></td>
		<td><u:buttonS titleId="wp.outsourcing" alt="외주 직원" onclick="setManPower('out');"/></td>
		</tr></table></td>
		<td style="width:25px;"></td>
		
		<td class="search_tit"><u:msg titleId="wp.pred" alt="기간" /></td>
		<td>
		<u:calendar
				titleId="cm.cal.startDd" alt="시작일자"
				id="strtYmd" value="${strtYmd}" option="{end:'endYmd'}"
				readonly="${not empty noChange ? 'Y' : ''}" /></td>
		<td style="padding-left:8px; padding-right:5px;">~</td>
		<td><u:calendar
				titleId="cm.cal.endDd" alt="종료일자"
				id="endYmd" value="${param.endYmd}" option="{start:'strtYmd'}"
				readonly="${not empty noChange ? 'Y' : ''}" /></td>
		<td style="width:25px;"></td>
		
		</tr>
		</table>
	</td>
	<td><div class="button_search"><ul><li class="search"><a href="javascript:document.searchForm1.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<c:if test="${not empty orUserBVo or not empty wpMpBVo}">
<u:listArea colgroup="13%,37%,13%,37%">
<tr>
	<td class="head_ct"><u:msg titleId="pt.jsp.setAuthGrp.user" alt="사용자" /></td>
	<td class="body_lt"><c:if
			test="${not empty orUserBVo}"><u:out value="${orUserBVo.rescNm}" /></c:if><c:if
			test="${not empty wpMpBVo}"><u:out value="${wpMpBVo.mpNm}" /></c:if></td>
	<td class="head_ct"><u:msg titleId="cm.gubun" alt="구분" /></td>
	<td class="body_lt"><c:if test="${not empty orUserBVo or not empty wpMpBVo}"><u:msg titleId="wp.mpTypCd.${param.mpTypCd}" /></c:if></td>
</tr><c:if
	test="${not empty orUserBVo}">
<tr>
	<td class="head_ct"><u:term termId="or.term.grade" alt="직급"/></td>
	<td class="body_lt"><u:out value="${orUserBVo.gradeNm}" /></td>
	<td class="head_ct"><u:msg titleId="cols.dept" alt="부서"/></td>
	<td class="body_lt"><u:out value="${orUserBVo.deptRescNm}" /></td>
</tr>
</c:if><c:if
	test="${not empty wpMpBVo}">
<tr>
	<td class="head_ct"><u:term termId="or.term.grade" alt="직급"/></td>
	<td class="body_lt"><u:out value="${wpMpBVo.mpGrade}" /></td>
	<td class="head_ct"><u:msg titleId="cols.phon" alt="전화번호" /></td>
	<td class="body_lt"><u:out value="${wpMpBVo.mpPhone}" /></td>
</tr>
</c:if>
</u:listArea></c:if>

<c:if test="${not empty wpPrjBVoList}">
<u:title titleId="wp.manPowerPrj" alt="투입 내역" type="small" menuNameFirst="false" />
<div style="overflow-y:show; overflow-x:auto"><u:convert
		value="${usrMpDetails.getMonthList()}" var="yearMonthList" />
<u:listArea tableStyle="width:${140 + 190 + 180 + 60 + (fn:length(yearMonthList) * 50)}px">
<tr>
	<th class="head_ct" style="width:140px;"><u:msg titleId="wp.prjCd" alt="프로잭트 코드" /></th>
	<th class="head_ct" style="width:190px;"><u:msg titleId="wp.prjNm" alt="프로잭트 명" /></th>
	<th class="head_ct" style="width:180px;"><u:msg titleId="wp.pred" alt="기간" /></th>
	<th class="head_ct" style="width:60px;"></th><c:forEach
		items="${yearMonthList}" var="yearMonth">
	<th class="head_ct" style="width:50px;">${yearMonth}</th>
	</c:forEach>
	
</tr><c:forEach items="${wpPrjBVoList}" var="wpPrjBVo" varStatus="trStatus">
<tr>
	<td class="body_ct" rowspan="2" style="border-bottom:1px solid #CECECE;"><u:out value="${wpPrjBVo.prjCd}" /></td>
	<td class="body_ct" rowspan="2" style="border-bottom:1px solid #CECECE;"><u:out value="${wpPrjBVo.prjNm}" /></td>
	<td class="body_ct" rowspan="2" style="border-bottom:1px solid #CECECE;"><c:if test="${not empty wpPrjBVo.strtYmd or not empty wpPrjBVo.endYmd}"
		><u:out value="${wpPrjBVo.strtYmd}" type="shortdate" /> ~ <u:out value="${wpPrjBVo.endYmd}" type="shortdate"
		/></c:if></td>
	
	<td class="body_ct" style=""><u:msg titleId="wp.plan" alt="계획" /></td><u:convert
			value="${usrMpDetails.getPlanMap(wpPrjBVo.prjNo)}" var="planMap" /><c:forEach
		items="${yearMonthList}" var="yearMonth">
	<td class="body_ct"><u:convert value="${planMap.get(yearMonth)}" /></td>
	</c:forEach>
<tr>
	<td class="body_ct" style="border-bottom:1px solid #CECECE;"><u:msg titleId="wp.rslt" alt="결과" /></td><u:convert
			value="${usrMpDetails.getRsltMap(wpPrjBVo.prjNo)}" var="rsltMap" /><c:forEach
		items="${yearMonthList}" var="yearMonth">
	<td class="body_ct" style="border-bottom:1px solid #CECECE;"><u:convert value="${rsltMap.get(yearMonth)}" /></td>
	</c:forEach>
</tr></c:forEach>

</u:listArea>
</div></c:if>

