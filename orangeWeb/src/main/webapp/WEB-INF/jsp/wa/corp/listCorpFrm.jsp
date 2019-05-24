<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	String callback = (String)request.getAttribute("callback");
	if(callback==null || callback.isEmpty()) callback = "openCarListFrm";
%>
<script type="text/javascript">
<!--<%// [클릭] 관리 %>
function corpClick(obj, id){
	if(obj != null){
		$('#carListBoxArea a.on').attr('class','');
		$(obj).attr('class','on');	
	}
	parent.<%= callback%>(id);
}
$(document).ready(function() {
	<c:if test="${fn:length(waCorpBVoList)>0}">
		<u:set var="paramCorpNo" test="${!empty param.corpNo }" value="${param.corpNo }" elseValue="${waCorpBVoList[0].corpNo}"/>
		corpClick(null,'${paramCorpNo}');
	</c:if>
	setUniformCSS();
});
//-->
</script>
<div style="padding:10px;">
<u:listArea id="carListBoxArea" tbodyClass="textBox" colgroup=",45%">
	<tr id="headerTr">
		<th class="head_ct"><u:msg titleId="wa.cols.corp.name" alt="법인명" /></th>
		<th class="head_ct"><u:msg titleId="wa.cols.corp.regNo" alt="사업자등록번호" /></th>
	</tr>
	<c:forEach items="${waCorpBVoList}" var="waCorpBVo" varStatus="status" >
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" >
		<td class="body_lt"><div class="ellipsis" title="${waCorpBVo.corpNm }"><a href="javascript:;" onclick="corpClick(this, '${waCorpBVo.corpNo }');" class="${(empty param.corpNo && status.first) || (!empty param.corpNo && param.corpNo eq waCorpBVo.corpNo) ? 'on' : '' }">${waCorpBVo.corpNm }</a></div></td>
		<td class="body_lt"><div class="ellipsis" title="${waCorpBVo.corpRegNo }">${waCorpBVo.corpRegNo }</div></td>
	</tr>
	</c:forEach>
	<c:if test="${empty waCorpBVoList }">
		<tr>
		<td class="nodata" colspan="2"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
</u:listArea>
</div>