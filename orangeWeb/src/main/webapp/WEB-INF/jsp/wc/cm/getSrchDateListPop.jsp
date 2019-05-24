<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="callback" test="${!empty param.callback }" value="${param.callback }" elseValue="setDateHidden"/>

<script type="text/javascript">
<!--
<%// [확인] 저장 %>
function applyCfrm(){
	var chkDates = getChkDates();
	if(chkDates == null) return;
	${callback}(chkDates);
	dialog.close('fromToDateDialog');
}

<%//현재 등록된 id 목록 리턴 %>
function getChkDates(){
	var arr=[];
	$('#fromToDateList input[type="checkbox"]:checked').each(function(){
		if($(this).val()!=''){
			arr.push($(this).val());
		}
	});
	if(arr.length==0){
		return null;
	}
	return arr;
};

$(document).ready(function() {
	//setFromToDate();
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>
<u:set var="viewMode" test="${empty param.mode || (!empty param.mode && param.mode ne 'view') }" value="N" elseValue="Y"/>
<div style="width:300px">
	<div style="overflow-y:auto;height:300px;">
		<u:set var="colgroup" test="${viewMode eq 'N' }" value="3%,97%" elseValue="100%"/>
		<u:listArea id="fromToDateList" colgroup="${colgroup }">
		<tr id="headerTr">
			<c:if test="${viewMode eq 'N' }"><th class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('fromToDateList', this.checked);" value=""/></th></c:if>
			<th class="head_ct">날짜</th>
		</tr><u:set var="format" test="${!empty param.format }" value="${param.format }" elseValue="yyyyMMdd"/>
		<c:forEach var="date" items="${fromToList }" varStatus="status"
			><c:set var="checked" value="N"/><c:set var="dateStyle" value=""/>
				<c:if test="${empty param.chkDates || (!empty param.chkDates && fn:contains(param.chkDates, date[0]))}"><c:set var="checked" value="Y" 
				/></c:if>
				<c:if test="${empty param.chkDates && (!empty param.holiYn && param.holiYn eq 'N' && 
					( ( empty param.onlyHoli && ((!empty excludeList && fn:contains(excludeList, date[0])) || (date[1] eq '1' || date[1] eq '7'))) || 
					(!empty param.onlyHoli && param.onlyHoli eq 'Y' && !empty excludeList && !fn:contains(excludeList, date[0]) && (date[1] ne '1' && date[1] ne '7'))) ) }"
				><c:set var="checked" value="N" /></c:if
				><c:if test="${(!empty excludeList && fn:contains(excludeList, date[0])) || date[1] eq '1'}"><c:set var="dateStyle" value="class=\"red_txt\""/></c:if
				><c:if test="${date[1] eq '7' }"><c:set var="dateStyle" value="class=\"color_txt\""/></c:if
				>
			<c:if test="${empty param.mode || (!empty param.mode && param.mode eq 'view' && checked eq 'Y')}">			
			<tr>
				<c:if test="${viewMode eq 'N' }"><td class="bodybg_ct"><input type="checkbox" value="${date[0] }" <c:if test="${checked eq 'Y' }">checked="checked"</c:if>
				/></td></c:if><td class="bodybg_ct"><fmt:parseDate var="date" value="${date[0] }" pattern="${format }"
				/><strong ${dateStyle }><fmt:formatDate value="${date }" type="date" dateStyle="long" /></strong></td>
			</tr></c:if>
			</c:forEach>
		</u:listArea>		
	</div>
	<% // 하단 버튼 %>
	<u:buttonArea>
	<c:if test="${viewMode eq 'N' }"><u:button titleId="cm.btn.confirm" alt="확인" onclick="applyCfrm();"  /></c:if>
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
	</u:buttonArea>
	
</div>
