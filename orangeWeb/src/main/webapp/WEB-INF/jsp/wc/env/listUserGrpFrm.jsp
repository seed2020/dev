<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	String callback = (String)request.getAttribute("callback");
	if(callback==null || callback.isEmpty()) callback = "openListFrm";
%>
<script type="text/javascript">
<!--<%// [클릭] 사용자그룹 %>
function userGrpClick(obj, id){
	if(obj != null){
		$('#userGrpListBoxArea a.on').attr('class','');
		$(obj).attr('class','on');	
	}
	parent.<%= callback%>(id);
}
$(document).ready(function() {
	<c:if test="${fn:length(wcUserGrpBVoList)>0}">
		<u:set var="paramUserGrpId" test="${!empty param.userGrpId }" value="${param.userGrpId }" elseValue="${wcUserGrpBVoList[0].userGrpId}"/>
		userGrpClick(null,'${paramUserGrpId}');
	</c:if>
	setUniformCSS();
});

//-->
</script>

<div style="padding:10px;">
<u:listArea id="userGrpListBoxArea" tbodyClass="textBox">
	<tr id="headerTr">
		<th class="head_ct"><u:msg titleId="cols.grpNm" alt="그룹명" /></th>
	</tr>
	<c:forEach items="${wcUserGrpBVoList}" var="wcUserGrpBVo" varStatus="status" >
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" >
		<td class="body_lt"><a href="javascript:;" onclick="userGrpClick(this, '${wcUserGrpBVo.userGrpId }');" class="${(empty param.userGrpId && status.first) || (!empty param.userGrpId && param.userGrpId eq wcUserGrpBVo.userGrpId) ? 'on' : '' }">${wcUserGrpBVo.rescNm }</a></td>
	</tr>
	</c:forEach>
	<c:if test="${empty wcUserGrpBVoList }">
		<tr>
		<td class="nodata" ><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
</u:listArea>
</div>