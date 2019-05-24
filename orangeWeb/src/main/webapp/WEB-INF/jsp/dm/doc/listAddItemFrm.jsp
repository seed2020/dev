<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set test="${param.lstTyp eq 'C'}" var="atrbIdPrefix" value="cls" elseValue="fld"	/>
<script type="text/javascript">
<!--<% //값이 등록된 항목을 조회 %>
function getChkVal(){
	var arr=[];
	$('#listArea').find("input[type='text'],input[type='hidden'],textarea,select").each(function(){
		arr.push({name:$(this).attr('name'),value:$(this).val()});
	});
	if(arr.length==0){
		return null;
	}
	return arr;
}<% //심의여부 리턴 %>
function getDiscYn(){
	return $('#listAddItemArea #discYn').val();
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<div id="listAddItemArea">
<u:input type="hidden" id="discYn" value="${discYn }"/>
<c:if test="${!empty itemDispList }">
<c:if test="${!empty dmDocLVoMap }"><c:set var="voMap" value="${dmDocLVoMap }" scope="request"/></c:if>
<c:set var="maxCnt" value="2"/>
<c:set var="dispCnt" value="1"/>
<div class="listarea" id="listArea">
<table class="listtable" border="0" cellpadding="0" cellspacing="1">
<tr>
<c:forEach var="dispVo" items="${itemDispList }" varStatus="status">
<c:set var="colmVo" value="${dispVo.colmVo}" />
<c:set var="itemTyp" value="${colmVo.itemTyp}" />
<c:set var="itemNm" value="${dispVo.atrbId}" />
<u:convertMap var="docVal" srcId="voMap" attId="${itemNm }" />
	<u:set var="colspan" test="${(dispCnt == 1 || dispCnt%maxCnt == 1) && ( itemTyp eq 'TEXTAREA' || fn:length(itemDispList) == status.count )}" value="colspan='${(maxCnt*2)-1 }'" elseValue=""/>
	<c:if test="${dispCnt > 1 && dispCnt%maxCnt == 1}"></tr><tr></c:if>
	<td class="head_lt" width="15%">${colmVo.itemDispNm }</td>
	<td class="bodybg_lt" ${colspan } width="${!empty colspan ? '85' : '35' }%">
		<!-- 확장컬럼의 데이터 최대길이(byte) [input,textarea만 해당] -->
		<u:set var="dataMaxByte" test="${(itemTyp == 'TEXT' || itemTyp == 'TEXTAREA') && colmVo.dataTyp eq 'VARCHAR' && !empty colmVo.colmLen }" value="${colmVo.colmLen }" elseValue="100"/>
		<c:if test="${itemTyp == 'TEXT'}"><u:input id="${itemNm}" value="${docVal}" title="${colmVo.itemDispNm }" maxByte="${dataMaxByte }" style="width: 96%;" /></c:if>
		<c:if test="${itemTyp == 'TEXTAREA'}"><u:textarea id="${itemNm}" value="${docVal}" title="${colmVo.itemDispNm }" maxByte="${dataMaxByte }" style="width:98%;" rows="${colmVo.itemTypVa}" /></c:if>
		<c:if test="${itemTyp == 'PHONE'}"><u:phone id="${itemNm}" value="${docVal}" title="${colmVo.itemDispNm }" /></c:if>
		<c:if test="${itemTyp == 'CALENDAR'}"><u:calendar id="${itemNm}" value="${docVal}" title="${colmVo.itemDispNm }" option="{iframeId:'listAddItemFrm'}"/></c:if>
		<c:if test="${itemTyp == 'CODE'}">
			<select name="${itemNm}">
				<c:forEach items="${colmVo.cdList}" var="cd" varStatus="status">
					<option value="${cd.cdId}" <c:if test="${cd.cdId == docVal}">selected="selected"</c:if>>${cd.rescNm}</option>
				</c:forEach>
			</select>
		</c:if>
	</td>
	<c:if test="${!empty colspan && status.count < fn:length(itemDispList) }"></tr><tr><c:set var="dispCnt" value="0"/></c:if>
	<c:set var="dispCnt" value="${dispCnt+1 }"/>
</c:forEach>
</tr>
</table>
</div>
<u:blank />
</c:if>
</div>