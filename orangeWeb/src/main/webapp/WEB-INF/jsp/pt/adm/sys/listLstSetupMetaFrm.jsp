<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--
<%// 체크된 목록 리턴 - uncheck : true 면 체크를 해제함 %>
function getChecked(uncheck){
	var va, arr = [];
	$("#listArea input:checked").each(function(){
		va = $(this).val();
		if(va!=null && va!=''){
			arr.push({atrbId:va,rescNm:$(this).attr("data-rescNm"),msgId:$(this).attr("data-msgId"),sortOptVa:$(this).attr("data-sortOptVa")});
		}
		if(uncheck){
			$(this).trigger('click');
		}
	});
	if(arr.length==0){
		alertMsg("cm.msg.noSelect");<%//cm.msg.noSelect=선택한 항목이 없습니다.%>
		return null;
	}
	return arr;
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<form style="padding:10px;">
<%// 목록 %>
<u:listArea id="listArea" >

	<tr>
		<th width="6%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></th>
		<th class="head_ct"><u:msg titleId="pt.jsp.setLstSetup.colNm" alt="컬럼명" /></th>
	</tr>

<c:if test="${fn:length(ptLstSetupMetaDVoList)==0}" >
	<tr>
		<td class="nodata" colspan="2"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(ptLstSetupMetaDVoList)!=0}" >
	<c:forEach items="${ptLstSetupMetaDVoList}" var="ptLstSetupMetaDVo" varStatus="status">
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" >
		<td class="bodybg_ct"><input type="checkbox" id="${ptLstSetupMetaDVo.atrbId}" name="${ptLstSetupMetaDVo.atrbId}" value="${ptLstSetupMetaDVo.atrbId}" data-rescNm="<u:term termId='${ptLstSetupMetaDVo.msgId}' />" data-msgId="${ptLstSetupMetaDVo.msgId}" data-sortOptVa="${ptLstSetupMetaDVo.sortOptVa}" /></td>
		<td class="body_lt"><label for="${ptLstSetupMetaDVo.atrbId}"><u:term termId="${ptLstSetupMetaDVo.msgId}" /></label></td>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>
</form>