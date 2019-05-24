<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	String callback = (String)request.getAttribute("callback");
	if(callback==null || callback.isEmpty()) callback = "openSiteListFrm";
%>
<script type="text/javascript">
<!--<%// [클릭] 즐겨찾기 %>
function catClick(obj, id){
	if(obj != null){
		$('#siteListBoxArea a.on').attr('class','');
		$(obj).attr('class','on');	
	}
	parent.<%= callback%>(id);
}
$(document).ready(function() {
	<c:if test="${fn:length(stCatBVoList)>0}">
		<u:set var="paramCatId" test="${!empty param.catId }" value="${param.catId }" elseValue="${stCatBVoList[0].catId}"/>
		catClick(null,'${paramCatId}');
	</c:if>
	setUniformCSS();
});
//-->
</script>
<div style="padding:10px;">
<u:listArea id="siteListBoxArea" tbodyClass="textBox">
	<tr id="headerTr">
		<th class="head_ct"><u:msg titleId="st.jsp.cat.title" alt="카테고리" /></th>
	</tr>
	<c:forEach items="${stCatBVoList}" var="stCatBVo" varStatus="status" >
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" >
		<td class="body_lt"><a href="javascript:;" onclick="catClick(this, '${stCatBVo.catId }');" class="${(empty param.catId && status.first) || (!empty param.catId && param.catId eq stCatBVo.catId) ? 'on' : '' }">${stCatBVo.catNm }</a></td>
	</tr>
	</c:forEach>
	<c:if test="${empty stCatBVoList }">
		<tr>
		<td class="nodata" ><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
</u:listArea>
</div>