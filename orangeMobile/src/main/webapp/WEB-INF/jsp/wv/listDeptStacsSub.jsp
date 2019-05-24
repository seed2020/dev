<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ page import="com.innobiz.orange.web.cm.utils.StringUtil"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[

function listDeptStacsPop(survId,quesId,replyrDeptId){
	$m.dialog.open({
		id:'listDeptStacsPop',
		title:'<u:msg titleId="wv.cols.set.deptStat" alt="부서별통계" />',
		url:'/wv/listDeptStacsPop.do?menuId=${menuId}&survId='+survId+'&quesId='+quesId+'&replyrDeptId='+replyrDeptId,
	});
}
           
$(document).ready(function() {
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('tabview');
});  
//]]>
</script>
<div class="blank20"></div>
<div id="tabViewArea">    
<!--s_tablearea S-->
<div class="s_tablearea">
<table class="s_table">
    <caption><u:msg titleId="cols.ques" alt="질문" /><u:out value="${quesCount} ) ${wvsQueVo.quesCont}"  maxLength="65" /></caption>
    <colgroup>
        <col width=""/>
        <col width="20%"/>
    </colgorup>
    <tbody>
        <tr>
            <th class="shead_ct"><u:msg titleId="cols.dept" alt="부서" /></th>
            <th class="shead_ct"><u:msg titleId="wv.cols.sum" alt="합계" /></th>
        </tr>
        <c:forEach  var="wvsDept" items="${wvSurvReplyDeptList}"  varStatus="status">
	        <tr>
	            <td class="sbody_lt"><a href="javascript:listDeptStacsPop('${param.survId}','${param.quesId}','${wvsDept.replyrDeptId}');">${wvsDept.replyrDeptNm}</a></td>
	            <td class="sbody_ct"><a href="javascript:listDeptStacsPop('${param.survId}','${param.quesId}','${wvsDept.replyrDeptId}');">${wvsDept.deptTotalCount}</a></td>
	        </tr>
        </c:forEach>
    </tbody>
</table>
</div>
</div>
<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
    