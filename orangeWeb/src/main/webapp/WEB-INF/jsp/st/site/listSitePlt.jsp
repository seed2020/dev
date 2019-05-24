<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<div class="ptlbody_ct">
<table class="ptltable" id="ptltable" border="0" cellpadding="0" cellspacing="0" style="width:100%; table-layout:fixed;">
	<tbody>
	<tr id="lineTr"><td class="line"></td></tr>
	<tr id="headerTr">
		<td class="head_ct"><div class="ellipsis" title="<u:msg titleId="st.cols.siteNm" />"><u:msg titleId="st.cols.siteNm"
			/></div></td>
	</tr>
	<tr id="lineTr"><td colspan="${colspan }" class="line"></td></tr>
<c:forEach
	items="${stSiteBVoList}" var="stSiteBVo">
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
		<td class="body_lt"><div class="ellipsis" title="${stSiteBVo.siteNm }"
		><c:if test="${!empty stSiteBVo.siteUrl }"><a href="${stSiteBVo.siteUrl }" target="_blank">${stSiteBVo.siteNm }</a></c:if><c:if test="${empty stSiteBVo.siteUrl }">${stSiteBVo.siteNm }</c:if
		></div></td></tr><tr id="lineTr"><td class="line"></td></tr></c:forEach
		>
<c:if test="${fn:length(stSiteBVoList)==0}" >
	<tr><td class="nodata" ><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td></tr>
	<tr id="lineTr"><td class="line"></td></tr>
</c:if>
</tbody>
</table>
<u:blank />
<u:pagination noTotalCount="true" noBottomBlank="true" pltBlock="true"/>
</div>
