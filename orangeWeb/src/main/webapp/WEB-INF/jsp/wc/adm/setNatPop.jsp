<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%// [아이콘] 선택추가 %>
function addRows(){	
	var arr = getIframeContent('findNatFrm').getChecked(true);
	if(arr==null) return;
	var $tr, $hiddenTr = $("#listNatArea tbody:first #hiddenTr");
	var html = $hiddenTr[0].outerHTML;
	var vas = getAllCheckVas();
	arr.each(function(index, obj){
		if(vas==null || !vas.contains(obj.cd)){
			$hiddenTr.before(html);
			$tr = $hiddenTr.prev();
			$tr.attr('id','tr'+obj.cd);
			$tr.find("input[type='checkbox']").val(obj.cd);
			$tr.find("input[name='cd']").val(obj.cd);
			$tr.find("input[name='rescId']").val(obj.rescId);
			$tr.find("td#msgNm").text(obj.rescNm);
			$tr.show();
			setJsUniform($tr[0]);
		}
	});
};
<%// [아이콘] 선택제거 %>
function delSelRows(){
	var arr = getCheckedTrs("cm.msg.noSelect", true);
	if(arr==null) return;
	arr.each(function(index, tr){
		$(tr).remove();
	}, true);
};
<%// [아이콘] 상하 이동 %>
function move(direction){
	var i, arr = getCheckedTrs("cm.msg.noSelect", true);
	if(arr==null) return;
	
	var $node, $prev, $next, $std;
	if(direction=='up'){
		$std = $('#headerTr');
		for(i=0;i<arr.length;i++){
			$node = $(arr[i]);
			$prev = $node.prev();
			if($prev[0]!=$std[0]){
				$prev.before($node);
			}
			$std = $node;
		}
	} else if(direction=='down'){
		$std = $('#hiddenTr');
		for(i=arr.length-1;i>=0;i--){
			$node = $(arr[i]);
			$next = $node.next();
			if($next[0]!=$std[0]){
				$next.after($node);
			}
			$std = $node;
		}
	}
};
<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(noSelectMsg, checked){
	var arr=[], id, obj;
	$("#listNatArea tbody:first input[type='${multi == 'Y' ? 'checkbox' : 'radio' }']"+(checked!=undefined&&checked ? ":checked" : "")).each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
};
<%//checkbox 가 선택된 id 목록 리턴 %>
function getAllCheckVas(){
	var arr=[];
	$("#listNatArea tbody:first input[type='checkbox']").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push($(this).val());
	});
	if(arr.length==0){
		return null;
	}
	return arr;
};<%// 선택목록 리턴 %>
function fnSelArrs(arr, id){
	if(arr!=null) return selRowInArrs(arr, id);
	else return null;
};<%// 배열에 담긴 목록%>
function selRowInArrs(rowArr, id){
	var objArr = [], $cd;
	for(var i=0;i<rowArr.length;i++){
		$cd = $(rowArr[i]).find("input[name='"+id+"']");
		if($cd.val()!=''){
			objArr.push($cd.val());
		}
	}
	return objArr;
};
<% // [버튼:저장] %>
function saveNat(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	callAjax('./transNatListAjx.do?menuId=${menuId}&fncCal=${param.fncCal}', {cds:fnSelArrs(arr, 'cd'),rescIds:fnSelArrs(arr, 'rescId')}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			dialog.close('setNatListDialog');
		}
	});
}<% // [버튼:저장] %>
function saveOk(){
	var arr = getCheckedTrs("cm.msg.noSelect", true);
	var cds = fnSelArrs(arr, 'cd');
	var rescIds = fnSelArrs(arr, 'rescId');
	<c:if test="${!empty param.callback}">${param.callback}(cds, rescIds);</c:if>
	<c:if test="${empty param.callback}">
	callAjax('./transNatAjx.do?menuId=${menuId}&fncCal=${param.fncCal}', {cds:cds,rescIds:rescIds}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			dialog.close('setNatDialog');
		}
	});
	</c:if>
}
<%// 조회 %>
function findNatSearch(areaId){
	var schWord = $('#'+areaId+' #schWord').val();
	if(schWord=='') schWord = null;
	getIframeContent('findNatFrm').findText(schWord);
}
<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
};
$(document).ready(function() {
	//setUniformCSS();
});
//-->
</script>

<div style="width:${isFindNat == true ? 500 : 300}px;">
<c:if test="${isFindNat == true }">
<u:searchArea style="width:100%;" id="findNatForm">
<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td><u:input id="schWord" maxByte="50" value="" titleId="cols.schWord" style="width:300px;" onkeydown="if (event.keyCode == 13){findNatSearch('findNatForm');return false;}" /></td>
					<td><a href="javascript:findNatSearch('findNatForm');" class="ico_search"><span><u:msg alt="검색" titleId="cm.btn.search" /></span></a></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</u:searchArea>
</c:if>
<div <c:if test="${isFindNat == true }">style="height:410px;"</c:if>>
<u:set var="containerStyle" test="${isFindNat == true }" value="float:left;width:46%;" elseValue=""/>
<!-- LEFT -->
<div style="${containerStyle}" id="leftContainer">
<u:set var="outerStyle" test="${isFindNat == true }" value="height:380px; overflow-x:hidden; overflow-y:auto;" elseValue="min-height:170px;overflow-x:hidden; overflow-y:auto;"/>
<u:titleArea outerStyle="${outerStyle }" innerStyle="padding: 10px;">
	<u:listArea id="listNatArea" >
	<tr id="headerTr">
		<th width="30px" class="head_bg">
			<c:if test="${multi != 'Y'}">&nbsp;</c:if>
			<c:if test="${multi == 'Y'}"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listNatArea', this.checked);" value=""/></c:if>
		</th>
		<th width="*" class="head_ct"><u:msg titleId="wc.cols.nat.sel" alt="선택한국가" /></th>
	</tr>
	<c:forEach var="wcNatBVo" items="${wcNatBVoList}" varStatus="status"
	><u:set test="${status.last}" var="trDisp" value="display:none" />
		<u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${list.cd}" />
		<tr id="${trId}" style="${trDisp}" class="natyNmCls">
		<td class="bodybg_ct">
			<c:if test="${multi != 'Y'}"><u:radio id="${wcNatBVo.cd }Chk" name="natChk" value="${wcNatBVo.cd }" checked="${(empty chkNatCd && status.first) || (!empty chkNatCd && chkNatCd == wcNatBVo.cd ) }" /><u:input type="hidden" id="cd" value="${wcNatBVo.cd }"/><u:input type="hidden" id="rescId" value="${wcNatBVo.rescId }"/></c:if>
			<c:if test="${multi == 'Y'}">
				<u:checkbox id="${wcNatBVo.cd }Chk" name="natChk" value="${wcNatBVo.cd }" checked="false" /><u:input type="hidden" id="cd" value="${wcNatBVo.cd }"/><u:input type="hidden" id="rescId" value="${wcNatBVo.rescId }"/>
			</c:if>
		</td>
		<td class="body_lt" id="msgNm"><label for="${wcNatBVo.cd }Chk">${wcNatBVo.rescNm }</label></td>
	</tr>
	</c:forEach>
</u:listArea>
</u:titleArea>
	
</div>
<c:if test="${isFindNat == true }">
<div style="float:left; width:9%; text-align:center; margin:175px 0 0 0;">
	<table style="margin:0 auto 0 auto;" border="0" cellpadding="0" cellspacing="0">
		<tr><td><a href="javascript:addRows();"<u:elemTitle titleId="cm.btn.selAdd" alt="선택추가" type="image" />><img src="${_cxPth}/images/${_skin}/ico_left.png" width="20" height="20" /></a></td></tr>
		<tr><td class="height5"></td></tr>
		<tr><td><a href="javascript:delSelRows();"<u:elemTitle titleId="cm.btn.selDel" alt="선택삭제" type="image" />><img src="${_cxPth}/images/${_skin}/ico_right.png" width="20" height="20" /></a></td></tr>
	</table>
</div>

<!-- RIGHT -->
<div style="float:right; width:45%;" id="rContainer">

<u:titleArea frameId="findNatFrm" frameSrc="./listNatFrm.do?menuId=${menuId }"
	outerStyle="height:380px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:370px;" noBottomBlank="true"  />
</div>
</c:if>
</div>
<u:buttonArea id="rightBtnArea">
	<c:if test="${isFindNat == true }"><u:button titleId="cm.btn.save" alt="저장" href="javascript:saveNat();" auth="A"/></c:if>
	<c:if test="${isFindNat == false }"><u:button titleId="cm.btn.confirm" alt="확인" href="javascript:saveOk();" auth="W"/></c:if>
	<u:button id="btnCancel" titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>

</div>