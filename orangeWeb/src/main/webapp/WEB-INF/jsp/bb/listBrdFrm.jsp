<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--<%// [클릭] 게시판 %>
function brdClick(obj, id){
	if(obj != null){
		$('#brdAreaList a.on').attr('class','');
		$(obj).attr('class','on');	
	}
}<%// 선택 ID 리턴 %>
function getSelectId(){
	return $('#brdAreaList a.on').attr('data-brdId');
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<div style="padding:10px;">

<% // 목록 %>
<u:listArea id="brdAreaList" tbodyClass="textBox">
	<tr>
		<td class="head_ct"><u:msg titleId="cols.bbNm" alt="게시판명" /></td>
	</tr>
	<c:choose>
		<c:when test="${!empty baBrdBVoList}">
			<c:forEach items="${baBrdBVoList}" var="baBrdBVo" varStatus="status">
				<tr>
					<td class="body_lt"><a href="javascript:;" onclick="brdClick(this, '${baBrdBVo.brdId }');" class="${status.first ? 'on' : '' }" data-brdId="${baBrdBVo.brdId }">${baBrdBVo.rescNm}</a></td>
				</tr>
			</c:forEach>
			</c:when>
		<c:otherwise>
			<tr>
				<td class="nodata" ><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
		</c:otherwise>
	</c:choose>
</u:listArea>
</div>