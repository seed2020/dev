<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function saveOrgPop(){
	var $radio = $("#aduTypArea input:checked");
	if($radio.length==0){
		alertMsg("cm.select.check.mandatory","#or.jsp.setOrg.aduTyp");<%//cm.select.check.mandatory="{0}"(을)를 선택해 주십시요.%>
	} else {
		gCopyInfo['aduTypCd'] = $radio.val();
		processPaste();
		dialog.close("setAduTypDialog");
	}
}
//-->
</script>
<div style="width:400px">
<form id="aduTypArea">
<u:listArea>

	<tr id="headerTr">
		<th colspan="2" class="head_ct"><u:msg titleId="or.jsp.setOrg.aduTypTitle" alt="겸직 파견직 선택" /></th>
	</tr>
	<c:forEach items="${aduTypCdList}" var="aduTypCd" varStatus="status">
	<c:if test="${aduTypCd.cd != '01'}">
	<tr>
		<u:radio name="aduTypCd" value="${aduTypCd.cd}" title="${aduTypCd.rescNm}"
			inputClass="bodybg_ct" textClass="body_lt" noSpaceTd="true"
			inputStyle="width:5%;"
			extraData="${aduTypCd.rescId},${aduTypCd.rescNm}" />
	</tr>
	</c:if>
	</c:forEach>

</u:listArea>
</form>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" href="javascript:saveOrgPop();" alt="확인" auth="A" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</div>