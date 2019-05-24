<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--
<%// [순서조절:위로,아래로] 서버에 저장하지 않고 화면상에서만 순서 조정함 %>
function move(direction){
	var i, arr = getCheckedTrs("cm.msg.noSelect");
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
function getCheckedTrs(noSelectMsg){
	var arr=[], id, obj;
	$("#cdListArea tbody:first input[type='checkbox']:checked").each(function(){
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
var gMaxRow = parseInt('${fn:length(ptCdBVoList)}');
<%// 행추가%>
function addRow(){
	var $tr = $("#cdListArea tbody:first #hiddenTr");
	var html = $tr[0].outerHTML;
	html = html.replace(/_NO/g,'_'+gMaxRow);
	gMaxRow++;
	$tr.before(html);
	$tr = $tr.prev();
	$tr.attr('id','');
	$tr.attr('style','');
	setJsUniform($tr[0]);
}
<%// 행삭제%>
function delRow(){
	var $tr = $("#cdListArea tbody:first #hiddenTr").prev();
	delRowInArr([$tr[0]]);
}
<%// 선택삭제%>
function delSelRow(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) delRowInArr(arr);
}
<%// 삭제 - 배열에 담긴 목록%>
function delRowInArr(rowArr){
	var delVa = $("#delList").val(), delArr = [], $cd;
	if(delVa!='') delArr.push(delVa);
	for(var i=0;i<rowArr.length;i++){
		$cd = $(rowArr[i]).find("input[name='cd']");
		if($cd.attr("readonly")=="readonly"){
			delArr.push($cd.val());
		}
		$(rowArr[i]).remove();
	}
	$("#delList").val(delArr.join(','));
}
<%// 해당 어권의 명이 입력되지 않았 을 경우 해당 어권을 보이게 함 %>
function changeLangSelectorCd(areaId, id, va){
	if(va==''){
		var langSelector = $('#'+areaId+' #langSelector'+id.substring(id.lastIndexOf('_')));
		var nm = $("#cdListArea #"+id).attr("name");
		langSelector.val(nm.substring(nm.length-2));
		langSelector.trigger('click');
	}
}
<%// 저장%>
function saveCd(){
	<%// cd값이 있는 것중 어권별 리소스가 없는것이 있는지 체크함%>
	var i, $cds = $("#cdListArea input[name='cd']"), size = $cds.length-1, obj, id, no, count=0;
	for(i=0;i<size;i++){
		obj = $cds[i];
		$(obj).val($(obj).val().trim());
		if($(obj).val()!=''){
			id = $(obj).attr('id');
			no = id.substring(id.lastIndexOf('_')+1);
			if(checkRescVa(no)!=true) return;
			count++;
		}
	}
	if(count==0){
		if($('#delList').val()==''){
			alertMsg("cm.msg.nodata.toSave");<%//cm.msg.nodata.toSave=저장할 데이터가 없습니다.%>
			return;
		}
	}
	
	<%// 정렬순서 세팅%>
	var ordr = 1;
	$("#cdListArea input[name='sortOrdr']").each(function(){
		if($(this).attr('id')!='sortOrdr_NO'){
			$(this).val(ordr++);
		}
	});
	<%// 서버 전송%>
	var $form = $("#cdListForm");
	$form.attr('method','post');
	$form.attr('action','/pt/adm/cd/transCd.do');
	$form.attr('target','<u:set test="${noFrameYn == 'Y'}" value="dataframe" elseValue="dataframeForFrame" />');
	$form.submit();
	
}
<%//어권별 리소스 input에 값이 있는지 체크%>
function checkRescVa(no){
	var result = true;
	$("#cdListArea #langTyp_"+no+" input").each(function(){
		var id = $(this).attr('id'), va = $(this).val();
		var handler = validator.getHandler(id);
		if(result && handler!=null && handler(id, va)==false){
			result = false;
			$(this).focus();
		}
	});
	return result;
}
<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='input:radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
}
<%// 일괄 언어 선택 변경%>
function changeLangTypCds(areaId){
	var $area = $("#"+areaId);
	<%// 변경할 언어 구하기 - 첫번째 언어 선택 select 의 다음 값 %>
	var langSel = $area.find("select[id^='langSelector']:first");
	if(langSel.length==0) return;
	var index = langSel[0].selectedIndex + 1;
	if(index>=langSel[0].options.length) index = 0;
	var langCd = langSel[0].options[index].value;
	var rescNm;
	<%// input - 어권에 맞게 show/hide %>
	$area.find("td[id^='langTyp'] input").each(function(){
		rescNm = $(this).attr('name');
		if(rescNm!=null && rescNm.endsWith(langCd)){
			$(this).show();
		} else { $(this).hide(); }
	});
	<%// select 변경%>
	var selectors = $area.find("select[id^='langSelector']");
	selectors.val(langCd);
	selectors.uniform.update();
}
$(document).ready(function() {
	<%// 행추가 영역 제외하고 uniform 적용%>
	$("#cdListArea tbody:first").children("[id!='hiddenTr']").each(function(){
		setJsUniform(this);
	});
});
//-->
</script>
<u:set test="${paddingYn=='N'}" var="formPadding" value="" elseValue="padding:10px;" />
<form id="cdListForm" style="${formPadding}">
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="noFrameYn" value="${noFrameYn}" />
<u:listArea id="cdListArea" >

	<tr id="headerTr">
		<th width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('cdListArea', this.checked);" value=""/></th>
		<th width="11%" class="head_ct"><u:mandatory /><u:msg titleId="cols.cd" alt="코드" /></th>
		<c:if test="${fn:length(_langTypCdListByCompId)>1}">
		<th class="head_ct"><u:mandatory /><a href="javascript:changeLangTypCds('cdListArea');" title="<u:msg titleId="pt.jsp.terms.chgLangAll" alt="일괄 언어 변경" />"><u:msg titleId="cols.cdVa" alt="코드값" /></a></th>
		</c:if>
		<c:if test="${fn:length(_langTypCdListByCompId)<=1}">
		<th class="head_ct"><u:mandatory /><u:msg titleId="cols.cdVa" alt="코드값" /></th>
		</c:if>
		<th width="11%" class="head_ct"><u:msg titleId="cols.refVa1" alt="참조값1" /></th>
		<th width="11%" class="head_ct"><u:msg titleId="cols.refVa2" alt="참조값2" /></th>
		<th class="head_ct"><u:msg titleId="cols.cdDesc" alt="코드설명" /></th>
		<th width="12%" class="head_ct"<c:if test="${not empty forOneComp}"> style="display:none;"</c:if>><u:msg titleId="cols.compIds" alt="회사ID목록" /></th>
		<th width="8%" class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></th>
	</tr>
	
	<c:forEach items="${ptCdBVoList}" var="ptCdBVo" varStatus="status"
			 ><u:set test="${status.last}" var="trDisp" value="display:none"
			/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${ptCdBVo.cd}"
			/><u:set test="${status.last}" var="cdIndex" value="_NO" elseValue="_${status.index+1}"
			/>
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="${trId}" style="${trDisp}" >
		<td class="bodybg_ct"><input type="checkbox" /></td>
		<td><u:set
			test="${not empty ptCdBVo.cd}" var="readonly" value="Y" elseValue="N"
			/><u:input id="cd${cdIndex}" name="cd" value="${ptCdBVo.cd}" titleId="cols.cd" maxByte="30"
				style="width:82%" readonly="${readonly}" valueOption="alpha,number" valueAllowed="_" /></td>
		<td>
			<table cellspacing="0" cellpadding="0" border="0">
			<tr>
			<td id="langTyp${cdIndex}">
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="langStatus">
			<u:convert srcId="${ptCdBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
			<u:set test="${langStatus.first}" var="style" value="" elseValue="display:none;" />
			<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
			<u:input id="rescVa_${langTypCdVo.cd}${cdIndex}" name="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.cdVa" value="${rescVa}" style="${style}"
				maxByte="200" validator="changeLangSelectorCd('cdListArea', id, va)" mandatory="Y" />
			</c:forEach>
			</td>
			<td>
			<c:if test="${fn:length(_langTypCdListByCompId)>1}">
				<select id="langSelector${cdIndex}" onchange="changeLangTypCd('cdListArea','langTyp${cdIndex}', this.value)" <u:elemTitle titleId="cols.langTyp" />>
				<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="langStatus">
				<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
				</c:forEach>
				</select>
			</c:if>
			<u:input type="hidden" id="rescId${cdIndex}" name="rescId" value="${ptCdBVo.rescId}" />
			</td>
			</tr>
			</table>
		</td>
		<td><u:input id="refVa1${cdIndex}" name="refVa1" value="${ptCdBVo.refVa1}" titleId="cols.refVa1" maxByte="400" style="width:82%" /></td>
		<td><u:input id="refVa2${cdIndex}" name="refVa2" value="${ptCdBVo.refVa2}" titleId="cols.refVa2" maxByte="400" style="width:82%" /></td>
		<td><u:input id="cdDesc${cdIndex}" name="cdDesc" value="${ptCdBVo.cdDesc}" titleId="cols.cdDesc" maxByte="400" style="width:88%" /></td><c:if test="${empty forOneComp}"></c:if>
		<td <c:if test="${not empty forOneComp}"> style="display:none;"</c:if>><u:input id="compIds${cdIndex}" name="compIds" value="${ptCdBVo.compIds}" titleId="cols.compIds" maxByte="120" style="width:84%" /></td>
		<td class="listicon_ct"><select id="useYn${cdIndex}" name="useYn" <u:elemTitle titleId="cols.useYn" />>
			<u:option value="Y" title="Y" checkValue="${ptCdBVo.useYn}" />
			<u:option value="N" title="N" checkValue="${ptCdBVo.useYn}" />
			</select>
			<u:input type="hidden" id="sortOrdr${cdIndex}" name="sortOrdr" />
			<u:input type="hidden" id="valid${cdIndex}" name="valid" />
		</td>
	</tr>
	</c:forEach>
	
</u:listArea>
<input type="hidden" id="clsCd" name="clsCd" value="${param.clsCd}" />
<input type="hidden" id="delList" name="delList" />
</form>