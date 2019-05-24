<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

// 테스트용
//request.setAttribute("pects", new String[]{ "76.2", "120.0", "180.0", "260", "320" });

%><script type="text/javascript">
<!--
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<u:title alt="프로잭트 관리 / 공수 관리 / 프로잭트별 집계" menuNameFirst="true" />


<%-- // 검색영역 --%>
<u:searchArea style="position:relative; z-index:2;">
	<form id="searchForm1" name="searchForm1" action="${_uri}" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="cat" value="${param.cat}" /><c:if
		test="${not empty param.pageRowCnt}">
	<u:input type="hidden" id="pageRowCnt" value="${param.pageRowCnt}" /></c:if>
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td>
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="wp.prjNm" alt="프로잭트 명" /></td>
		<td><u:input id="prjNm" titleId="wp.prjNm" alt="프로잭트 명" value="${param.prjNm}" /></td>
		<td style="width:25px;"></td>
		
		<td class="search_tit"><u:msg titleId="wp.prjGrp" alt="프로잭트 그룹"  /></td>
		<td><select name="grpId" id="grpId">
			<u:option value="" titleId="cm.option.noSelect" alt="선택안함" /><c:forEach
				items="${wpPrjGrpBVoList}" var="wpPrjGrpBVo">
			<u:option value="${wpPrjGrpBVo.grpId}" title="${wpPrjGrpBVo.grpNm}" checkValue="${param.grpId}" /></c:forEach></select></td>
		<td style="width:25px;"></td>
		
		<td class="search_tit"><u:msg titleId="wp.pred" alt="기간" /></td>
		<td>
		<u:calendar
				titleId="cm.cal.startDd" alt="시작일자"
				id="strtYmd" value="${param.strtYmd}" option="{end:'endYmd'}"
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

<%--// 목록 --%>
<u:listArea id="listArea" colgroup="${
		param.cat eq 'prjMd'  ? '10%,18%,18%,16%,8%,8%,10%,12%' :
		param.cat eq 'apv'    ? '10%,20%,18%,16%,10%,10%,16%' : 
		param.cat eq 'askApv' ? '12%,20%,18%,16%,12%,12%,10%' : 
								'12%,22%,20%,20%,13%,13%'}" >
<tr>
	<th class="head_ct"><u:msg titleId="wp.prjCd" alt="프로잭트 코드" /></th>
	<th class="head_ct"><u:msg titleId="wp.prjNm" alt="프로잭트 명" /></th>
	<th class="head_ct"><u:msg titleId="wp.prjGrp" alt="프로잭트 그룹"  /></th>
	<th class="head_ct"><u:msg titleId="wp.pred" alt="기간" /></th>
	<th class="head_ct"><u:msg titleId="wp.endDt" alt="종료일" /></th>
	<th class="head_ct"><u:msg titleId="wp.pm" alt="PM" /></th><c:if
	
		test="${param.cat eq 'prjMd'}">
	<th class="head_ct"><u:msg titleId="wp.plan" alt="계획" /> / <u:msg titleId="wp.rslt" alt="결과" /></th>
	<th class="head_ct"><u:msg titleId="wp.mdRslt" alt="투입결과" /></th></c:if><c:if
	
		test="${param.cat eq 'askApv'}">
	<th class="head_ct"><u:msg titleId="wp.verStat" alt="버전 상태" /></c:if><c:if
	
		test="${param.cat eq 'apv'}">
	<th class="head_ct"><u:msg titleId="wp.reqDt" alt="요청일" /></c:if>
</tr>
<c:if test="${fn:length(wpPrjBVoList)==0}" >
	<tr>
	<td class="nodata" colspan="${param.cat eq 'prjMd' ? '9' : '7'}"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:forEach items="${wpPrjBVoList}" var="wpPrjBVo" varStatus="status">
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
	<td class="body_ct"><a href="./${param.cat eq 'regMd' ? 'setPrjMp' : 
		param.cat eq 'temp' ? 'setPrj' : 
		param.cat eq 'prjMd' ? 'listPrjMp' : 'viewPrj'}.do?menuId=${menuId}&cat=${param.cat}&prjNo=${wpPrjBVo.prjNo}"><u:out value="${wpPrjBVo.prjCd}" /></a></td>
	<td class="body_ct"><a href="./${param.cat eq 'regMd' ? 'setPrjMp' : 
		param.cat eq 'temp' ? 'setPrj' : 
		param.cat eq 'prjMd' ? 'listPrjMp' : 'viewPrj'}.do?menuId=${menuId}&cat=${param.cat}&prjNo=${wpPrjBVo.prjNo}"><u:out value="${wpPrjBVo.prjNm}" /></a></td>
	<td class="body_ct"><u:convertMap srcId="prjGrpMap" attId="${wpPrjBVo.grpId}" /></td>
	<td class="body_ct"><c:if test="${not empty wpPrjBVo.strtYmd or not empty wpPrjBVo.endYmd}"
		><u:out value="${wpPrjBVo.strtYmd}" type="shortdate" /> ~ <u:out value="${wpPrjBVo.endYmd}" type="shortdate" /></c:if></td>
	<td class="body_ct"><u:out value="${wpPrjBVo.cmplYmd}" /></td>
	<td class="body_ct"><u:out value="${wpPrjBVo.pmNm}" /></td><c:if
	
	
		test="${param.cat eq 'prjMd'}"><u:convertMap
			srcId="planMmMap" attId="${wpPrjBVo.prjNo}" var="planMm"/><u:convertMap
			srcId="rsltMmMap" attId="${wpPrjBVo.prjNo}" var="rsltMm"/><u:set
			test="${not empty planMm and not empty rsltMm}"
				var="percent" value="${ (rsltMm / planMm) * 100.0 }" func="round.1" />
	<td class="body_ct">${planMm} / ${rsltMm}</td>
	<td style="padding:2px;"><c:if
		test="${not empty percent}"><c:if
			
		test="${percent <= 100.0}">
		<div style="width:${percent}%; height:6px; background-color: #71A3F4" title="${percent} %"></div></c:if><c:if
			
		test="${percent <= 200.0 and percent > 100.0}">
		<div style="width:100%; height:6px; background-color: #71A3F4" title="${percent} %"></div>
		<div style="width:${percent-100.0}%; height:6px; margin-top:2px; background-color: #E382AB" title="${percent} %"></div></c:if><c:if
			
		test="${percent > 200.0}">
		<div style="width:100%; height:10px; background-color: #FD5551" title="${percent} %"></div></c:if>
	</c:if></td></c:if><c:if
	
	
		test="${param.cat eq 'askApv'}">
	<td class="body_ct"><c:if test="${not empty wpPrjBVo.modStatCd}"><u:msg titleId="wp.modStatCd.${wpPrjBVo.modStatCd}" /></c:if></td></c:if><c:if
	
	
		test="${param.cat eq 'apv'}">
	<td class="body_ct"><u:out value="${wpPrjBVo.regDt}" type="longdate" /></c:if>
	</tr>
</c:forEach>
</u:listArea>

<u:pagination />

<u:buttonArea><c:if
		test="${param.cat eq 'prj' or param.cat eq 'temp' or param.cat eq 'askApv'}" >
	<u:button titleId="cm.btn.write" alt="등록" href="./setPrj.do?menuId=${menuId}&cat=${param.cat}" auth="W" /></c:if>
</u:buttonArea>