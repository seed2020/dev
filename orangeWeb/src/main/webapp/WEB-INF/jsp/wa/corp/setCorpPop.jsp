<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--<%// 카테고리 저장 %>
function selectCorp(){
	var arr = getCheckedTrsCorp("cm.msg.noSelect");
	if(arr==null) return;
	var corpNo = selRowInArr(arr);
	setCorp(corpNo);
}<%// 선택 - 배열에 담긴 목록%>
function selRowInArr(rowArr){
	var selIds = [];
	for(var i=0;i<rowArr.length;i++){
		$corpNo = $(rowArr[i]).find("input[name='corpNo']");
		if($corpNo.val()!=''){
			selIds.push($corpNo.val());
		}
	}
	if(selIds.length == 0 ) return null;
	return selIds.join(',');
}<%// [순서조절:위로,아래로] 서버에 저장하지 않고 화면상에서만 순서 조정함 %>
function moveCorp(direction){
	var i, arr = getCheckedTrsCorp("cm.msg.noSelect");
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
}
<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrsCorp(noSelectMsg){
	var arr=[], id, obj;
	$("#corpListArea tbody:first input[type=${param.fncMul == 'Y' ? 'checkbox' : 'radio' }]:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
}
<%// 다음 Row 번호 %>
var gMaxRow = parseInt('${fn:length(waCorpBVoList)}');
<%// 행추가%>
function addRowCorp(){
	restoreUniform('corpListArea');
	var $tr = $("#corpListArea tbody:first #hiddenTr");
	var html = $tr[0].outerHTML;
	html = html.replace(/_NO/g,'_'+gMaxRow);
	gMaxRow++;
	$tr.before(html);
	$tr = $tr.prev();
	$tr.attr('id','');
	$tr.attr('style','');
	dialog.resize('setCorpDialog');
	applyUniform('corpListArea');
}
<%// 행삭제%>
function delRowCorp(){
	var $tr = $("#corpListArea tbody:first #hiddenTr").prev();
	delRowCorpInArr([$tr[0]]);
}<%// 선택삭제%>
function delSelRowCorp(){
	var arr = getCheckedTrsCorp("cm.msg.noSelect");
	if(arr!=null) delRowCorpInArr(arr);
}<%// 삭제 - 배열에 담긴 목록%>
function delRowCorpInArr(rowArr){
	var delVa = $("#delList").val(), delArr = [], $corpNo;
	if(delVa!='') delArr.push(delVa);
	for(var i=0;i<rowArr.length;i++){
		$corpNo = $(rowArr[i]).find("input[name='corpNo']");
		if($corpNo.val()!=''){
			delArr.push($corpNo.val());
		}
		$(rowArr[i]).remove();
	}
	$("#delList").val(delArr.join(','));
	dialog.resize('setCorpPop');
}<%// 저장%>
function saveCorp(){	
	<%// trArr : 저장할 곳의 tr 테그 배열 %>
	var trArr=[], count=0, result;
	$("#corpListArea tbody:first").children().each(function(){
		trArr.push(this);
	});
	
	<%// 값이 있는지 체크함%>
	for(var i=1;i<trArr.length-1;i++){
		result = checkVa(trArr[i], true);
		if(result<0) return;
		$(trArr[i]).find("[name='valid']").val( (result>0) ? 'Y' : '');
		count += result;
	}
	
	if(count==0 && $('#delList').val()==''){
		alertMsg("cm.msg.nodata.toSave");<%//cm.msg.nodata.toSave=저장할 데이터가 없습니다.%>
		return;
	}
	
	<%// 서버 전송%>
	var $form = $("#corpListForm");
	$form.attr('method','post');
	$form.attr('action','./transCorpList.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form.submit();
	
	dialog.close('setCatDialog');
}<%// input에 값이 있는지 체크%>
function checkVa(trObj, ignoreAllEmpty){
	var i, id, va, handler, arr=[];
	$(trObj).find("td[id='required'] input").each(function(){ arr.push(this); });
	
	if(ignoreAllEmpty){
		var hasNoVa=true;
		arr.each(function(index, obj){
			if($(obj).val()!=''){
				hasNoVa = false;
				return false;
			}
		});
		if(hasNoVa) return 0;
	}
	
	for(i=0;i<arr.length;i++){
		id = $(arr[i]).attr('id');
		va = $(arr[i]).val();
		handler = validator.getHandler(id);
		if(handler!=null && handler(id, va)==false){
			$(arr[i]).focus();
			return -1;
		}
	}
	return 1;
}<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
}<%// 체크된 목록 리턴 - 잘라내기용 %>
function getCheckedArray(){
	var arr = [], va;
	$("#corpListArea input[type=checkbox]:checked").each(function(){
		va = $(this).val();
		if(va!=null && va!='') { arr.push(va); }
	});
	return arr.length==0 ? null : arr;
}
$(document).ready(function() {
	$("#corpListArea tbody:first").children().each(function(){
		<%// 행추가 영역 제외하고 uniform 적용%>
		if($(this).attr('id')!='hiddenTr'){
			setJsUniform(this);
		}
	});
});
//-->
</script>
<div style="width:450px;">
<c:if test="${isSelect eq true }">
<u:listArea id="corpListArea" colgroup=",45%">

	<tr id="headerTr">
		<td width="20px" class="head_bg">
		<c:if test="${param.fncMul != 'Y'}">&nbsp;</c:if>
		<c:if test="${param.fncMul == 'Y'}"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('corpListArea', this.checked);" value=""/></c:if>
		</td>
		<th class="head_ct"><u:msg titleId="wa.cols.corp.name" alt="법인명" /></th>
		<th class="head_ct"><u:msg titleId="wa.cols.corp.regNo" alt="사업자등록번호" /></th>
	</tr>
	<c:forEach items="${waCorpBVoList}" var="waCorpBVo" varStatus="status">
	<tr>
		<td class="bodybg_ct">
			<c:if test="${param.fncMul != 'Y'}"><u:radio id="check_${waCorpBVo.corpNo }" name="catChk" value="${waCorpBVo.corpNo }" checked="${status.first }" /><input type="hidden" name="corpNo" value="${waCorpBVo.corpNo }" /></c:if>
			<c:if test="${param.fncMul == 'Y'}">
				<u:checkbox id="check_${waCorpBVo.corpNo }" name="catChk" value="${waCorpBVo.corpNo }" checked="${status.first }" /><input type="hidden" name="corpNo" value="${waCorpBVo.corpNo }" />
			</c:if>
		</td>
		<td class="body_ct"><div class="ellipsis" title="${waCorpBVo.corpNm }"><label for="check_${waCorpBVo.corpNo }">${waCorpBVo.corpNm }</label></div></td>
		<td class="body_ct"><div class="ellipsis" title="${waCorpBVo.corpRegNo }">${waCorpBVo.corpRegNo }</div></td>
	</tr>
	</c:forEach>
	<c:if test="${empty waCorpBVoList }">
		<tr>
		<td class="nodata" colspan="3"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
</u:listArea>
<% // 하단 버튼 %>
	<u:buttonArea>
	<u:button titleId="cm.btn.choice" alt="선택" onclick="selectCorp();" auth="W" />
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" auth="R" />
	</u:buttonArea>
</c:if>
<c:if test="${isSelect eq false }">
<u:title titleId="wa.jsp.corp.title" type="small" alt="법인" />	
<div style="height:410px;overflow-y:auto;">
<form id="corpListForm" >
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="dialog" value="setCorpDialog" />
<u:listArea id="corpListArea" >

	<tr id="headerTr">
		<th width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('corpListArea', this.checked);" value=""/></th>
		<th class="head_ct"><u:mandatory /><u:msg titleId="wa.cols.corp.name" alt="법인명" /></th>
		<th class="head_ct"><u:mandatory /><u:msg titleId="wa.cols.corp.regNo" alt="사업자등록번호" /></th>
	</tr>
	
	<c:forEach items="${waCorpBVoList}" var="waCorpBVo" varStatus="status"
			 ><u:set test="${status.last}" var="trDisp" value="display:none"
			/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${waCorpBVo.corpNo}"
			/><u:set test="${status.last}" var="corpIndex" value="_NO" elseValue="_${status.index+1}"
			/>
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="${trId}" style="${trDisp}" >
		<td class="bodybg_ct"><input type="checkbox" value="${waCorpBVo.corpNo}"/>
			<u:input type="hidden" id="valid${carIndex}" name="valid" />
			<u:input type="hidden" id="sortOrdr${corpIndex}" name="sortOrdr" />
			<u:input type="hidden" id="corpNo${corpIndex}" name="corpNo" value="${waCorpBVo.corpNo }"/></td>
		<td id="required"><u:input id="corpNm" value="${waCorpBVo.corpNm}" titleId="wa.cols.corp.name" maxByte="120" style="width:92%;" mandatory="Y"/></td>
		<td id="required"><u:input id="corpRegNo" value="${waCorpBVo.corpRegNo}" titleId="wa.cols.corp.regNo" maxByte="60" style="width:92%;" mandatory="Y"/></td>
	</tr>
	</c:forEach>
	
</u:listArea>
<input type="hidden" id="delList" name="delList" />
</form>
</div><% // 하단 버튼 %>
<u:buttonArea>
	<u:button href="javascript:addRowCorp();" titleId="cm.btn.plus" alt="행추가" auth="W" />
	<u:button href="javascript:delRowCorp();" titleId="cm.btn.minus" alt="행삭제" auth="W" />
	<u:button href="javascript:delSelRowCorp();" titleId="cm.btn.selDel" alt="선택삭제" auth="W" />
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:saveCorp();" auth="W" />
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</c:if>
</div>