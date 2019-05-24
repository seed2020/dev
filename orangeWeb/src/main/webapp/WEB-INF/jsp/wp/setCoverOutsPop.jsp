<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%><script type="text/javascript">
<!--<%--
[버튼] 저장 --%>
function saveCoverOuts(){
	var arr = [];
	$("#coverOutsArea input[type=checkbox]").each(function(){
		if(this.checked && this.value!=''){
			arr.push(this.value);
		}
	});
	callAjax('./transProcessActAjx.do?menuId=${menuId}&cat=${param.cat}', {prjNo:'${param.prjNo}', actCd:"coverOuts", mpIds:arr.join(',')}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			dialog.close('setCoverOutsDialog');
		}
	});
}
-->
</script>
<div style="width:300px">

<u:listArea id="coverOutsArea">
<tr>
	<td class="head_bg" style="width:27px"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('coverOutsArea', this.checked);" value=""/></td>
	<td class="head_ct" style="width:45%"><u:msg titleId="wp.prjRole1Cd" alt="역할분류" /></td>
	<td class="head_ct" style="width:45%"><u:msg titleId="cols.nm" alt="이름" /></td>
</tr><c:forEach
	items="${wpPrjMpPlanDVoList}" var="wpPrjMpPlanDVo"><c:if
		test="${wpPrjMpPlanDVo.mpTypCd eq 'emp'}">
<tr>
	<td class="bodybg_ct" style="width:27px"><input type="checkbox" value="${wpPrjMpPlanDVo.mpId}" ${coverOutsList.contains(wpPrjMpPlanDVo.mpId) ? 'checked="checked"' : ''} /></td>
	<td class="body_ct"><c:if
		test="${not empty wpPrjMpPlanDVo.prjRole2Cd}"><u:msg
		titleId="wp.prjRole2Cd.${wpPrjMpPlanDVo.prjRole2Cd}" /></c:if></td>
	<td class="body_ct"><u:out value="${wpPrjMpPlanDVo.mpNm}" /></td>
	
</tr></c:if>
</c:forEach>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="saveCoverOuts();" alt="저장" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</div>