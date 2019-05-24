<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--var $area;<%// 유형 Prefix %>
function getTabPrefix(lstTyp){
	var prefix = "cat";
	if(lstTyp == 'C') prefix = "cls";
	return prefix;
}<%// [버튼] 유형 %>
function findCatPop(obj){
	var prefix = getTabPrefix(null);
	var td = getParentTag(obj,'td'); 
	$area = $(td).find("#"+prefix+"InfoArea"), data = [];
	$area.find("input[id='"+prefix+"Id']").each(function(){
		data.push($(this).val());
	});
	var url = './findCatPop.do?menuId=${menuId}&selId='+data+'&discYn=N';
	dialog.open('findCatPop', '<u:msg titleId="dm.cols.cat.select" alt="유형선택" />', url);
};<%// 유형 적용%>
function setCatInfo(arr){
	var prefix = getTabPrefix(null);
	var buffer = [];
	var nms = '';
	arr.each(function(index, obj){
		buffer.push("<input type='hidden' id='"+prefix+"Id' name='"+prefix+"Id' value='"+obj.id+"'/>\n");
		buffer.push("<input type='hidden' id='storId' name='storId' value='"+obj.storId+"'/>\n");
		nms+= nms == '' ? obj.nm : ','+obj.nm;
	});
	$area.find('#idArea').html('');
	$area.find('#idArea').html(buffer.join(''));
	$area.find('#nmArea input[id="'+prefix+'Nm"]').val(nms);
	dialog.close('findCatPop');
}
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
	$("#fldListArea tbody:first input[type='checkbox']:checked").each(function(){
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
var gMaxRow = parseInt('${fn:length(dmFldBVoList)}');
<%// 행추가%>
function addRow(){
	var $tr = $("#fldListArea tbody:first #hiddenTr");
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
	var $tr = $("#fldListArea tbody:first #hiddenTr").prev();
	delRowInArr([$tr[0]]);
}
<%// 선택삭제%>
function delSelRow(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) delRowInArr(arr);
}
<%// 삭제 - 배열에 담긴 목록%>
function delRowInArr(rowArr){
	var delVa = $("#delList").val(), delArr = [], $fldId;
	if(delVa!='') delArr.push(delVa);
	for(var i=0;i<rowArr.length;i++){
		$fldId = $(rowArr[i]).find("input[name='fldId']");
		if($fldId.val()!=''){
			delArr.push($fldId.val());
		}
		$(rowArr[i]).remove();
	}
	$("#delList").val(delArr.join(','));
}
<%
//- 해당 어권의 명이 입력되지 않았 을 경우 해당 어권을 보이게 함 %>
function changeLangSelectorFld(areaId, id, va){
	if(va==''){
		var langSelector = $('#'+areaId+' #langSelector'+id.substring(id.lastIndexOf('_')));
		var nm = $("#fldListArea #"+id).attr("name");
		langSelector.val(nm.substring(nm.length-2));
		langSelector.trigger('click');
	}
}
<%// 저장%>
function saveFld(){
	<%// trArr : 저장할 곳의 tr 테그 배열 %>
	var trArr=[], count=0, result;
	$("#fldListArea tbody:first").children().each(function(){
		trArr.push(this);
	});
	
	<%// 어권별 리소스가 있는지 체크함%>
	for(var i=1;i<trArr.length-1;i++){
		result = checkRescVa(trArr[i], true);
		if(result<0) return;	
		$(trArr[i]).find("[name='valid']").val( (result>0) ? 'Y' : '');
		if(result>0){
			if(isEmptyVa($(trArr[i]).find('input[name="catId"]').val())){
				alertMsg('cm.input.check.mandatory',['<u:msg titleId="dm.cols.cat" alt="문서유형" />']);
				return;
			}
		}
		count += result;
	}
	if(count==0 && $('#delList').val()==''){
		alertMsg("cm.msg.nodata.toSave");<%//cm.msg.nodata.toSave=저장할 데이터가 없습니다.%>
		return;
	}
	
	<%// 정렬순서 세팅%>
	var ordr = 1;
	$("#fldListArea input[name='sortOrdr']").each(function(){
		if($(this).attr('id')!='sortOrdr_NO'){
			$(this).val(ordr++);
		}
	});
	<%// disable 된 select disable 해제 %>
	var arr = releaseDisable($('#fldListForm select'));
	
	<%// 서버 전송%>
	var $form = $("#fldListForm");
	$form.attr('method','post');
	$form.attr('action','./transFldList.do');
	$form.attr('target','dataframeForFrame');
	$form.submit();
	
	<%// select disable 다시 적용 %>
	unreleaseDisable(arr);
}

<%//어권별 리소스 input에 값이 있는지 체크%>
function checkRescVa(trObj, ignoreAllEmpty){
	var i, id, va, handler, arr=[];
	$(trObj).find("td[id^='langTyp'] input").each(function(){ arr.push(this); });
	
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
}
<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
}
<%// 저장 후 리로드%>
function afterTrans(fldPid){
	parent.getIframeContent('fldTree').reoladFrame(fldPid);
	parent.openListFrm(fldPid);
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
}<%// 체크된 목록 리턴 - 잘라내기용 %>
function getCheckedArray(){
	var arr = [], va;
	$("#fldListArea input[type=checkbox]:checked").each(function(){
		va = $(this).val();
		if(va!=null && va!='') { arr.push(va); }
	});
	return arr.length==0 ? null : arr;
}
<%// 붙여 넣기 %>
function pasteArray(cutInfo){
	callAjax('./transFldPasteAjx.do?menuId=${menuId}', cutInfo, function(data){
		if(data.message!=null){
			alert(data.message);
		}
		if(data.fldId != null){
			parent.getIframeContent('fldTree').reoladFrame(data.fldId);
			reload();
		}
	});
}
$(document).ready(function() {
	$("#fldListArea tbody:first").children().each(function(){
		<%// 행추가 영역 제외하고 uniform 적용%>
		if($(this).attr('id')!='hiddenTr'){
			setJsUniform(this);
		}
	});
});
//-->
</script>

<form id="fldListForm" style="padding:10px;">
<input type="hidden" name="menuId" value="${menuId}" /><c:if test="${not empty param.fldPid}">
<u:input type="hidden" id="fldPid" value="${param.fldPid}" /></c:if>
<u:listArea id="fldListArea" >

	<tr id="headerTr">
		<th width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('fldListArea', this.checked);" value=""/></th>
		<th width="35%" class="head_ct"><u:mandatory /><a href="javascript:changeLangTypCds('fldListForm');" title="<u:msg titleId="pt.jsp.terms.chgLangAll" alt="일괄 언어 변경" />"><u:msg titleId="cols.fldNm" alt="폴더명" /></a></th>
		<th class="head_ct"><u:mandatory /><u:msg titleId="dm.cols.cat" alt="문서유형"/></th>
		<th width="15%" class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></th>
	</tr>
	
	<c:forEach items="${dmFldBVoList}" var="dmFldBVo" varStatus="status"
			 ><u:set test="${status.last}" var="trDisp" value="display:none"
			/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${dmFldBVo.fldId}"
			/><u:set test="${status.last}" var="fldIndex" value="_NO" elseValue="_${status.index+1}"
			/>
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="${trId}" style="${trDisp}" >
		<td class="bodybg_ct"><input type="checkbox" value="${dmFldBVo.fldId}"/></td>
		<td>
			<table cellspacing="0" cellpadding="0" border="0">
			<tr>
			<td id="langTyp${fldIndex}">
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="langStatus">
			<u:convert srcId="${dmFldBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
			<u:set test="${langStatus.first}" var="style" value="" elseValue="display:none;" />
			<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
			<u:input id="rescVa_${langTypCdVo.cd}${fldIndex}" name="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.fldNm" value="${rescVa}" style="${style}"
				maxByte="200" validator="changeLangSelectorFld('fldListArea', id, va)" mandatory="Y" />
			</c:forEach>
			</td>
			<td>
			<c:if test="${fn:length(_langTypCdListByCompId)>1}">
				<select id="langSelector${fldIndex}" onchange="changeLangTypCd('fldListArea','langTyp${fldIndex}', this.value)" <u:elemTitle titleId="cols.langTyp" />>
				<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="langStatus">
				<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
				</c:forEach>
				</select>
			</c:if>
			<u:input type="hidden" id="rescId${fldIndex}" name="rescId" value="${dmFldBVo.rescId}" />
			</td>
			</tr>
			</table>
		</td>
		<td><div id="catInfoArea" style="display:inline;">
			<div id="idArea" style="display:none;"><u:input type="hidden" id="catId" value="${dmFldBVo.catId }"/><u:input type="hidden" id="storId" value="${dmFldBVo.storId }"/></div>
			<div id="nmArea" style="display:inline;"><u:input id="catNm" titleId="dm.cols.catNm" value="${dmFldBVo.catNm}" mandatory="Y" style="width:55%;" readonly="Y"/></div>
		</div><u:buttonS titleId="dm.cols.cat.select" alt="유형선택" onclick="findCatPop(this);" /></td>
		<td class="listicon_ct"><select id="useYn${fldIndex}" name="useYn" <u:elemTitle titleId="cols.useYn" />>
			<u:option value="Y" titleId="cm.option.use" checkValue="${dmFldBVo.useYn}" />
			<u:option value="N" titleId="cm.option.notUse" checkValue="${dmFldBVo.useYn}" />
			</select>
			<u:input type="hidden" id="sortOrdr${fldIndex}" name="sortOrdr" />
			<u:input type="hidden" id="valid${fldIndex}" name="valid" />
			<u:input type="hidden" id="fldId${fldIndex}" name="fldId" value="${dmFldBVo.fldId }"/>
			<u:input type="hidden" id="fldTypCd${fldIndex}" name="fldTypCd" value="F"/>
		</td>
	</tr>
	</c:forEach>
	
</u:listArea>
<input type="hidden" id="delList" name="delList" />
</form>