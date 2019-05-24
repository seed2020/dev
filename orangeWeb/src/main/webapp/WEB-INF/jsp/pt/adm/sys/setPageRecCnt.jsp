<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

	//String[] pageRecCnts = { "10","20","30","40","50","60","70","80","90","100" };
	String[] pageRecCnts = { "5","10","15","20","25","30","35","40","45","50" };
	request.setAttribute("pageRecCnts", pageRecCnts);

%>
<script type="text/javascript">
<!--
function doSubmit(){
	var $form = $('#pageRecCntArea');
	$form.attr("action","./transPageRecCnt.do");
	$form.attr("target","dataframe");
	$form.submit();
}
<%// 첫번째 콤보가 변경 될때 %>
function changeVa(va){
	$("#pageRecCntArea select").val(va);
	$.uniform.update("#pageRecCntArea select");
}

$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>

<u:title titleId="pt.jsp.setPageRecCnt.title" alt="페이지별 레코드 수 설정" menuNameFirst="true" />

<form id="pageRecCntArea">
<input type="hidden" name="menuId" value="${menuId}" />
<u:listArea>
	<u:decode srcId="_lang" tgtValue="ko" var="cdLoc" value="공통코드 : 포털 / 페이지별레코드수" elseValue="" byAdmin="true" />
	<c:forEach items="${pageRecSetupCdList}" var="pageRecSetupCd" varStatus="status">
	<tr><u:set test="${pageRecSetupCd.cd == '_ALL'}" var="onchange" value="changeVa(this.value)" elseValue="" />
		<td width="18%" class="head_lt">${pageRecSetupCd.rescNm}</td>
		<td class="body_lt"><select id="period" name="pt.pageRecCnt.${pageRecSetupCd.cd}" onchange="${onchange}" <u:elemTitle title="${pageRecSetupCd.rescNm}" />>
			<c:forEach items="${pageRecCnts}" var="pageRecCnt" varStatus="status2">
			<u:convertMap srcId="pageRecCntMap" attId="${pageRecSetupCd.cd}" var="storedCnt" />
			<u:option value="${pageRecCnt}" title="${pageRecCnt}" selected="${storedCnt == pageRecCnt or (storedCnt == null and pageRecCnt == '10')}"/>
			</c:forEach>
			</select></td>
	</tr>
	</c:forEach>

</u:listArea>
</form>

<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" onclick="doSubmit()" auth="SYS" />
</u:buttonArea>