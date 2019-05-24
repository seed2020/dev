<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%// 체크된 목록 리턴 - uncheck : true 면 체크를 해제함 %>
function getChecked(uncheck){
	var arr = [];
	$("#listArea tbody:first input[type='checkbox']:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') {
			arr.push({cd:$(this).val(),rescNm:$(this).attr("data-rescNm"),rescId:$(this).attr("data-rescId")});
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
};
<%// Text 조회 %>
function findText(schWord){
	if(schWord==null) {
		$('#listArea .natyNmCls').show();
	}else{
		$('#listArea .natyNmCls').hide();
		$('#listArea .natyNmCls:contains("'+schWord.toUpperCase()+'")').each(function(){
			$(this).show();
		});
	}
};

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<div style="padding:10px;">
<u:listArea id="listArea" tableStyle="table-layout:fixed;">
	<tr id="headerTr">
		<th width="25px" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></th>
		<th width="*" class="head_ct"><u:msg titleId="wc.cols.nat.nm" alt="국가명" /></th>
	</tr>
	<c:forEach var="natyCdVo" items="${natyCdList}" varStatus="status"
	><tr class="natyNmCls">
		<td class="bodybg_ct"><input id="${natyCdVo.cd }Chk" type="checkbox" value="${natyCdVo.cd }" data-rescNm="${natyCdVo.rescNm }" data-rescId="${natyCdVo.rescId }"/></td>
		<td class="body_lt" ><div class="ellipsis"><label for="${natyCdVo.cd }Chk">${natyCdVo.rescNm }</label></div></td>
	</tr>
	</c:forEach>
	
</u:listArea>
</div>