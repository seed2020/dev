<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<%
	if(request.getAttribute("detlViewType")==null){
		request.setAttribute("detlViewType","bcList");// bcList, bcOpenList
	}
%>
<script type="text/javascript">
// 개인 , 공개 명함 프레임 리로드
function openListFrm(detlViewType){
	if(detlViewType !=null){
		var url = './findBcFrm.do?menuId=${menuId }&fncMul=${param.fncMul}&detlViewType='+detlViewType;
		reloadFrame('bcListFrm', url);
	}
};

$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
	$("#${detlViewType}Frm").show();
});
</script>
<div style="width:600px">
	<% // TAB %>
	<u:tabGroup id="findBcTab">
		<u:tab id="findBcTab"  onclick="openListFrm('bcList');" titleId="wb.jsp.findBcPop.tab.psnBc" alt="개인명함" on="true"/>
		<c:if test="${empty param.schBcRegrUid && empty param.isBcListType}"><u:tab id="findBcTab"  onclick="openListFrm('bcOpenList');" titleId="wb.jsp.findBcPop.tab.publBc" alt="공개명함" /></c:if>
	</u:tabGroup>

	<iframe id="bcListFrm" name="bcListFrm" src="./findBcFrm.do?menuId=${menuId }&fncMul=${param.fncMul}&detlViewType=bcList&schBcRegrUid=${param.schBcRegrUid}&pagingYn=${param.pagingYn}&schCat=${param.schCat }&schWord=${param.schWord}" style="width:100%; height:400px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
	<% // 하단 버튼 %>
	<u:buttonArea>
	<u:button titleId="cm.btn.choice" alt="선택" onclick="fnBcSelect('${detlViewType }','${param.callBack }');"  />
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" auth="R" />
	</u:buttonArea>
	
</div>
