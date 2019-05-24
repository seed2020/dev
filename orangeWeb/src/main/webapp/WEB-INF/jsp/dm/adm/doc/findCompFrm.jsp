<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(noSelectMsg){
	var arr=[], id, obj;
	$("#listArea tbody:first input[type=${multi == 'Y' ? 'checkbox' : 'radio' }]:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
};<%// 선택목록 리턴[docPid] %>
function selChkIds(idNm){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) return selRowInArr(arr, idNm);
	else return null;
};<%// 선택목록 리턴 %>
function selChkInfos(idNm){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) return selRowInArrs(arr, idNm);
	else return null;
};
<%// 배열에 담긴 목록 ID%>
function selRowInArr(rowArr, idNm){
	var objArr = [], $compId;
	for(var i=0;i<rowArr.length;i++){
		$compId = $(rowArr[i]).find("input[name='"+idNm+"']");
		if($compId.val()!=''){
			objArr.push({compId:$compId.val(),compNm:$compId.attr('data-compNm')});
		}
	}
	return objArr;
};<%// 배열에 담긴 목록 배열%>
function selRowInArrs(rowArr, idNm){
	var atrbIds = ["compNm"];
	var objArr = [], $compId;
	var param = null;
	for(var i=0;i<rowArr.length;i++){
		$compId = $(rowArr[i]).find("input[name='"+idNm+"']");
		param = new ParamMap();
		for(var j=0;j<atrbIds.length;j++){
			param.put(atrbIds[j],$compId.attr('data-'+atrbIds[j]));	
		}
		objArr.push(param);
	}
	return objArr;
};
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<div class="listarea" id="listArea">
<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
<colgroup><col width="10%"/><col width="*"/></colgroup>
	<tr id="headerTr">
		<td width="20px" class="head_bg">
			<c:if test="${multi != 'Y'}">&nbsp;</c:if>
			<c:if test="${multi == 'Y'}"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></c:if>
		</td>
		<td class="head_ct"><u:msg titleId="cols.compNm" alt="회사명"/></td>
	</tr>
	<c:forEach var="list" items="${ptCompBVoList}" varStatus="status">
		<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"' >
			<td class="bodybg_ct">
				<c:if test="${multi != 'Y'}"><u:radio id="check_${list.compId }" name="docChk" value="${list.compId }" checked="${status.first }" /><input type="hidden" name="compId" value="${list.compId }" data-compNm="${list.rescNm }" /></c:if>
				<c:if test="${multi == 'Y'}">
					<u:checkbox name="docChk" value="${list.compId }" checked="false" /><u:input type="hidden" name="compId" value="${list.compId }"/><u:input type="hidden" name="compId" value="${list.compId }"/>
				</c:if>
			</td>
			<td class="body_lt" ><div class="ellipsis" title="${list.rescNm }">${list.rescNm }</div></td>
		</tr>
	</c:forEach>
	<c:if test="${empty ptCompBVoList }">
		<tr>
		<td class="nodata" colspan="2"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
	
</table>
</div>