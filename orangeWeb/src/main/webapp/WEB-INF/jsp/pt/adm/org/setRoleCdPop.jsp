<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
$(document).ready(function() {
	var $area = $("#setRoleCdPop");
	var roles = $("#userPopForm #roleCds").val();
	if(roles!=''){
		roles.split(',').each(function(index, va){
			$area.find("input[value='"+va+"']").trigger('click');
		});
	}
});
//-->
</script>
<div style="width:400px">
<form id="setRoleCdPop">
<u:listArea>

<tr id="headerTr">
	<th width="2%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('setRoleCdPop', this.checked);" value=""/></th>
	<th class="head_ct"><u:term termId="or.term.role" alt="역할" /></th>
</tr>
<c:if test="${fn:length(roleCdList)==0}" >
<tr>
	<td class="nodata" colspan="2"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
</tr>
</c:if>
<c:forEach items="${roleCdList}" var="roleCd" varStatus="status"><u:convert srcId="${roleCd.cd}" var="checked" />
<tr>
	<u:checkbox name="grpId" value="${roleCd.cd}" title="${roleCd.rescNm}"
		inputClass="bodybg_ct" textClass="body_lt" noSpaceTd="true" extraData="${roleCd.rescNm}" />
</tr>
</c:forEach>

</u:listArea>
</form>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" href="javascript:setRoles();" alt="확인" auth="A" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</div>