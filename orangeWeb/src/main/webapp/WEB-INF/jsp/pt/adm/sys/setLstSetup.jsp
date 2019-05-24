<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
	import="com.innobiz.orange.web.cm.config.ServerConfig"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	if(request.getAttribute("detlViewType")==null){
		request.setAttribute("detlViewType","userList");// orgList, orgDetl, userList
	}

	request.setAttribute("run", ServerConfig.IS_RUN);// 실제 운영서버 여부

%>
<script type="text/javascript">
<!--
<%// [상단 SELECT] 변경 %>
function changeTarget(cd){
	reloadFrame('lstSetupMetaFrm','./listLstSetupMetaFrm.do?menuId=${menuId}&lstSetupMetaId='+cd);
	reloadFrame('lstSetupFrm','./listLstSetupFrm.do?menuId=${menuId}&lstSetupMetaId='+cd);
}
<%// [아이콘 >] 선택추가 %>
function addCols(){
	var arr = getIframeContent('lstSetupMetaFrm').getChecked(true);
	if(arr!=null){
		getIframeContent('lstSetupFrm').addCols(arr);
	}
}
<%// [아이콘 <] 선택삭제 %>
function removeCols(){
	getIframeContent('lstSetupFrm').removeCols();
}
<%// [아이콘 위/아래 이동]%>
function move(direction){
	getIframeContent('lstSetupFrm').move(direction);
}<c:if test="${not run}">
function saveAsDefault(){<%// 기본값으로 저장하기 %>
	getIframeContent('lstSetupFrm').saveAsDefault();
}
</c:if>
function setDefault(){<%// 기본값으로 되돌리기 %>
	getIframeContent('lstSetupFrm').setDefault();
}
function saveLstEnv(){<%// 저장 %>
	getIframeContent('lstSetupFrm').saveLstEnv();
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
	changeTarget($("#lstSetupMetaId").val());
});
//-->
</script>

<u:title title="리스트 환경 설정" menuNameFirst="true" />

<u:listArea>
	<tr><td width="11%" class="head_lt"><u:msg alt="대상 리스트" titleId="pt.jsp.setLstSetup.tgtLst" /></td>
		<td id="targetListArea">
			<select id="lstSetupMetaId" onchange="changeTarget(this.value);"<u:elemTitle titleId="pt.jsp.setLstSetup.tgtLst" />><c:forEach
			items="${lstSetupCdList}" var="lstSetupCd" varStatus="status">
			<u:option value="${lstSetupCd.cd}" title="${lstSetupCd.rescNm}" /></c:forEach></select>
		</td>
	</tr>
</u:listArea>

<div style="float:left; width:27%;">

<u:title titleId="pt.jsp.setLstSetup.lstColNm" alt="리스트 컬럼 명" type="small" />

<u:titleArea
	outerStyle="height:494px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV">
<iframe id="lstSetupMetaFrm" name="lstSetupMetaFrm" src="/cm/util/reloadable.do" style="width:100%; height:488px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:titleArea>

</div>

<div style="float:left; width:4%; text-align:center; margin:250px 0 0 0;">
	<table style="margin:0 auto 0 auto;" border="0" cellpadding="0" cellspacing="0">
		<tr><td><a href="javascript:addCols();"<u:elemTitle titleId="cm.btn.selAdd" alt="선택추가" type="image" />><img src="${_cxPth}/images/${_skin}/ico_right.png" width="20" height="20" /></a></td></tr>
		<tr><td class="height5"></td></tr>
		<tr><td><a href="javascript:removeCols();"<u:elemTitle titleId="cm.btn.selDel" alt="선택삭제" type="image" />><img src="${_cxPth}/images/${_skin}/ico_left.png" width="20" height="20" /></a></td></tr>
	</table>
</div>

<div style="float:right; width:69%;">

<u:title titleId="pt.jsp.setLstSetup.seldLstColNm" alt="선택된 리스트 컬럼 명" type="small" >
	<u:titleIcon type="up" href="javascript:move('up')" auth="A" />
	<u:titleIcon type="down" href="javascript:move('down')" auth="A" />
</u:title>

<u:titleArea
	outerStyle="height:494px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV">
<iframe id="lstSetupFrm" name="lstSetupFrm" src="/cm/util/reloadable.do" style="width:99.8%; height:488px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:titleArea>

<u:buttonArea id="rightBtnArea" noBottomBlank="true">
	<c:if test="${false}">
	<u:button href="javascript:setDefault();" titleId="pt.jsp.setLstSetup.setDefault" alt="기본값 설정" auth="SYS" />
	<u:button href="javascript:saveAsDefault();" titleId="pt.jsp.setLstSetup.saveAsDefault" alt="기본값으로 저장" auth="SYS" /></c:if>
	<u:button href="javascript:saveLstEnv();" titleId="cm.btn.save" alt="저장" auth="SYS" />
</u:buttonArea>

</div>

