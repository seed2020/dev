<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="paramsForList" excludes="meetNo" />
<script type="text/javascript">
<!--
<%// [우하단 버튼] 등록 %>
function setMeet(seq){
	var url = './setMeet.do?${paramsForList }';
	if(seq != undefined) url+= '&meetNo='+seq;
	location.href = url;
}
<% // [하단버튼:배열] %>
function getRightBtnList(){
	var $area = $("#rightBtnArea");
	return $area.find('ul')[0].outerHTML;
}
$(document).ready(function() {
	setUniformCSS();
	//parent.applyDocBtn();
});
//-->
</script>
<div style="padding:10px;">

<u:listArea id="listArea" colgroup="3%,15%,,13%,13%" >	
	<tr id="headerTr">
		<td class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td>
		<th class="head_ct">회사명</th>
		<th class="head_ct">주소</th>
		<th class="head_ct">등록자</th>
		<th class="head_ct">등록일시</th>
	</tr>
	<c:if test="${fn:length(cuTaskStatBVoList) == 0}">
		<tr>
		<td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
	<c:if test="${fn:length(cuTaskStatBVoList)>0}">
		<c:forEach items="${cuTaskStatBVoList}" var="cuTaskStatBVo" varStatus="status">
			<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"' onclick="viewTaskStat('${cuTaskStatBVo.statNo}')">
				<td class="bodybg_ct"><input type="checkbox" value="${cuTaskStatBVo.statNo }" onclick="notEvtBubble(event);"/></td>
				<td class="body_ct"></td>
				<td class="body_lt"><u:out value="${cuTaskStatBVo.subj }"/></td>
				<td class="body_ct"><a href="javascript:viewUserPop('${cuTaskStatBVo.regrUid }');">${cuTaskStatBVo.regrNm }</a></td>
				<td class="body_ct"><a href="javascript:viewUserPop('${cuTaskStatBVo.modrUid }');">${cuTaskStatBVo.modrNm }</a></td>
			</tr>
		</c:forEach>
	</c:if>
	
</u:listArea>
<u:pagination />

</div>
<% // 하단 버튼 %>
<u:buttonArea id="rightBtnArea" style="display:none;">
<u:button titleId="cm.btn.write" alt="등록" href="./setMeet.do?menuId=${menuId}" auth="W" />
</u:buttonArea>
