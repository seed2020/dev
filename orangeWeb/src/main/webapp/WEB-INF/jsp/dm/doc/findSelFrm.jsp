<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set test="${param.lstTyp eq 'C'}" var="atrbIdPrefix" value="cls" elseValue="fld"	/>
<script type="text/javascript">
<!--<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(noSelectMsg){
	var arr=[], id, obj;
	$("#listArea tbody:first input[type='checkbox']:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
}<%// 선택추가 %>
function addRows(selObj){
	if(selObj==null) return;
	var $tr, $hiddenTr = $("#listArea tbody:first #hiddenTr");
	var html = $hiddenTr[0].outerHTML;
	var vas = getAllVas();
	if(vas==null || !vas.contains(selObj.id)){
		$hiddenTr.before(html);
		$tr = $hiddenTr.prev();
		$tr.attr('id','tr'+selObj.id);
		$tr.find("input[type='checkbox']").val(selObj.id);
		$tr.find("td#selNm").text(selObj.nm);
		$tr.find("input[name='${atrbIdPrefix}Id']").val(selObj.id);
		$tr.find("input[name='${atrbIdPrefix}Nm']").val(selObj.nm);
		$tr.show();
		setJsUniform($tr[0]);
	}
};<%// 행삭제%>
function delRow(obj){
	var $tr = $(obj).parents('tr');
	delRowInArr([$tr[0]]);
}<%// 선택삭제%>
function delSelRows(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) delRowInArr(arr);
}
<%// 삭제 - 배열에 담긴 목록%>
function delRowInArr(rowArr){
	for(var i=0;i<rowArr.length;i++){
		$(rowArr[i]).remove();
	}
}<%//현재 등록된 id 목록 리턴 %>
function getAllVas(){
	var arr=[];
	$('#listArea input[name="${atrbIdPrefix}Id"]').each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push($(this).val());
	});
	if(arr.length==0){
		return null;
	}
	return arr;
};<%//현재 등록된 id 목록 리턴 %>
function getSelInfos(){
	var arr=[];
	$('#listArea input[name="${atrbIdPrefix}Id"]').each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push({id:$(this).val(),nm:$(obj).find("input[name='${atrbIdPrefix}Nm']").val()});
	});
	if(arr.length==0){
		return null;
	}
	return arr;
}
<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
}

$(document).ready(function() {
	$("#listArea tbody:first").children().each(function(){
		<%// 행추가 영역 제외하고 uniform 적용%>
		if($(this).attr('id')!='hiddenTr'){
			setJsUniform(this);
		}
	});
});
//-->
</script>

<div style="padding:10px;">
<u:listArea id="listArea" >

	<tr id="headerTr">
		<th width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></th>
		<th width="35%" class="head_ct"><u:msg titleId="cols.${param.lstTyp eq 'C' ? 'cls' : 'fld'}" alt="항목명" /></th>
		<th width="15%" class="head_ct"><u:msg titleId="cols.fnc" alt="기능" /></th>
	</tr>
	<c:forEach var="map" items="${selList}" varStatus="status"
			 ><c:set	var="map" value="${map}" scope="request" 
			/><u:convertMap var="idVal" srcId="map" attId="${atrbIdPrefix}Id" type="value"
			/><u:convertMap var="idNm" srcId="map" attId="${atrbIdPrefix}Nm" type="value"
			/><u:set test="${status.last}" var="trDisp" value="display:none"
			/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${idVal}"
			/>
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="${trId}" style="${trDisp}" >
		<td class="bodybg_ct"><input type="checkbox" value="${idVal }"/></td>
		<td class="bodybg_lt" id="selNm">${idNm }</td>
		<td class="bodybg_ct">
			<u:buttonS titleId="cm.btn.del" alt="삭제" onclick="delRow(this);" />
			<input type="hidden" name="${atrbIdPrefix}Id" value="${idVal }" />
			<input type="hidden" name="${atrbIdPrefix}Nm" value="${idNm }" />
		</td>
	</tr>
	</c:forEach>
	
</u:listArea>
</div>