<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

<%// [선택] 기능권한조회 %>
function fncSelected(fncUid){
	$("#fncUid").val(fncUid);
	$("#mngAuth").attr('src', './listMngAuthFrm.do?menuId=${menuId}&ctId=${ctId}&fncUid=' + fncUid);
	//getIframeContent('mngAuth').reload('./listMngAuthFrm.do?menuId=${menuId}&ctId=${ctId}&fncUid=' + fncUid);
}

<%// [버튼] 기능권한저장 %>
function fncAuthSave(){
	getIframeContent('mngAuth').fncAuthSubmit();
}

$(document).ready(function() {
	setUniformCSS();
	
	$(".wbox").attr("class", "");
});
//-->
</script>

<u:title title="${menuTitle }" alt="기능별권한관리" menuNameFirst="true"/>
<input type="hidden" id="fncUid" name="fncUid" />
<div style="float:left;width:49.5%;">
	<div class="titlearea">
		<div class="tit_left">
		<dl>
		<dd class="title_s"><u:msg titleId="ct.jsp.setMngAuth.subtitle01" alt="기능" /></dd>
		</dl>
		</div>
	</div>

	<select id="ctFnc" size="13" style="width:100%; height:187px;" onchange="fncSelected(this.value)">
		<c:forEach  var="ctFncList" items="${ctFncSelectList}" varStatus="status">
			<option value="${ctFncList.ctFncUid}">
				<c:if test="${!empty ctFncList.ctFncPnm}">[ ${ctFncList.ctFncPnm} ]</c:if> ${ctFncList.ctFncNm}</option>
		</c:forEach>
	</select>
</div>
<div style="float:right;width:49.5%;">
	<div class="titlearea">
		<div class="tit_left">
		<dl>
		<dd class="title_s"><u:msg titleId="ct.jsp.setMngAuth.subtitle02" alt="권한" /></dd>
		</dl>
		</div>
	</div>

	 <u:titleArea frameId="mngAuth" frameSrc="./listMngAuthFrm.do?menuId=${menuId}&ctId=${ctId}"
	 	outerStyle="height: 170px; overflow:hidden;" 
	 	innerStyle="padding:0 0px 0 0px;"
		frameStyle="width:100%; height:200px; overflow:hidden;" />
	
</div>

<u:blank />

<div class="headbox">
<dl>
<dd class="headbox_body"><u:msg titleId="ct.jsp.setMngAuth.tx01" alt="기능명을 선택하면 해당 기능명의 색이 반전되고 우측에 기능별 권한이 나타납니다." /></dd>
<dd class="headbox_red">※ <u:msg titleId="ct.jsp.setMngAuth.tx02" alt="권한 설정은 기능별로 각각 설정됩니다." /></dd>
</dl>
</div>

<u:blank />

<% // 하단 버튼 %>
<u:buttonArea>
	<c:if test="${!empty authChkW && authChkW == 'W' }">
		<u:button titleId="cm.btn.save" alt="저장" onclick="javascript:fncAuthSave();"/>
	</c:if>
	<u:button titleId="cm.btn.cancel" alt="취소" href="javascript:history.go(-1);" />
</u:buttonArea>

<u:blank />
