<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	String callback = (String)request.getAttribute("callback");
	if(callback==null || callback.isEmpty()) callback = "openListFrm";
%>
<script type="text/javascript">
<!--<%// [클릭] 즐겨찾기 %>
function bumkClick(obj, id){
	if(obj != null){
		$('#bumkListBoxArea a.on').attr('class','');
		$(obj).attr('class','on');	
	}
	parent.<%= callback%>(id);
}
$(document).ready(function() {
	<c:if test="${fn:length(dmBumkBVoList)>0}">
		<u:set var="paramBumkId" test="${!empty param.bumkId }" value="${param.bumkId }" elseValue="${dmBumkBVoList[0].bumkId}"/>
		bumkClick(null,'${paramBumkId}');
	</c:if>
	setUniformCSS();
});
//-->
</script>
<div style="padding:10px;">
<u:listArea id="bumkListBoxArea" tbodyClass="textBox">
	<tr id="headerTr">
		<th class="head_ct"><u:msg titleId="dm.jsp.bumkList.title" alt="즐겨찾기목록" /></th>
	</tr>
	<c:forEach items="${dmBumkBVoList}" var="dmBumkBVo" varStatus="status" >
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" >
		<td class="body_lt"><a href="javascript:;" onclick="bumkClick(this, '${dmBumkBVo.bumkId }');" class="${(empty param.bumkId && status.first) || (!empty param.bumkId && param.bumkId eq dmBumkBVo.bumkId) ? 'on' : '' }">${dmBumkBVo.bumkNm }</a></td>
	</tr>
	</c:forEach>
	<c:if test="${empty dmBumkBVoList }">
		<tr>
		<td class="nodata" ><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
</u:listArea>
</div>