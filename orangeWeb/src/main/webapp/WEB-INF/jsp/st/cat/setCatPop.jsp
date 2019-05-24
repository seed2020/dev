<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--<%// 카테고리 저장 %>
function selectCat(){
	var arr = getCheckedTrsCat("cm.msg.noSelect");
	if(arr==null) return;
	var catId = selRowInArr(arr);
	setCat(catId);
}<%// 선택 - 배열에 담긴 목록%>
function selRowInArr(rowArr){
	var selIds = [];
	for(var i=0;i<rowArr.length;i++){
		$catId = $(rowArr[i]).find("input[name='catId']");
		if($catId.val()!=''){
			selIds.push($catId.val());
		}
	}
	if(selIds.length == 0 ) return null;
	return selIds.join(',');
}<%// [순서조절:위로,아래로] 서버에 저장하지 않고 화면상에서만 순서 조정함 %>
function moveCat(direction){
	var i, arr = getCheckedTrsCat("cm.msg.noSelect");
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
function getCheckedTrsCat(noSelectMsg){
	var arr=[], id, obj;
	$("#catListArea tbody:first input[type=${param.fncMul == 'Y' ? 'checkbox' : 'radio' }]:checked").each(function(){
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
var gMaxRow = parseInt('${fn:length(stCatBVoList)}');
<%// 행추가%>
function addRowCat(){
	restoreUniform('catListArea');
	var $tr = $("#catListArea tbody:first #hiddenTr");
	var html = $tr[0].outerHTML;
	html = html.replace(/_NO/g,'_'+gMaxRow);
	gMaxRow++;
	$tr.before(html);
	$tr = $tr.prev();
	$tr.attr('id','');
	$tr.attr('style','');
	dialog.resize('setCatDialog');
	applyUniform('catListArea');
}
<%// 행삭제%>
function delRowCat(){
	var $tr = $("#catListArea tbody:first #hiddenTr").prev();
	delRowCatInArr([$tr[0]]);
}<%// 선택삭제%>
function delSelRowCat(){
	var arr = getCheckedTrsCat("cm.msg.noSelect");
	if(arr!=null) delRowCatInArr(arr);
}<%// 삭제 - 배열에 담긴 목록%>
function delRowCatInArr(rowArr){
	var delVa = $("#delList").val(), delArr = [], $catId;
	if(delVa!='') delArr.push(delVa);
	for(var i=0;i<rowArr.length;i++){
		$catId = $(rowArr[i]).find("input[name='catId']");
		if($catId.val()!=''){
			delArr.push($catId.val());
		}
		$(rowArr[i]).remove();
	}
	$("#delList").val(delArr.join(','));
	dialog.resize('setCatPop');
}<% //- 해당 어권의 명이 입력되지 않았 을 경우 해당 어권을 보이게 함 %>
function changeLangSelectorCat(areaId, id, va){
	if(va==''){
		var langSelector = $('#'+areaId+' #langSelector'+id.substring(id.lastIndexOf('_')));
		var nm = $("#catListArea #"+id).attr("name");
		langSelector.val(nm.substring(nm.length-2));
		langSelector.trigger('click');
	}
}<%// 저장%>
function saveCat(){
	<%// trArr : 저장할 곳의 tr 테그 배열 %>
	var trArr=[], count=0, result;
	$("#catListArea tbody:first").children().each(function(){
		trArr.push(this);
	});
	
	<%// 어권별 리소스가 있는지 체크함%>
	for(var i=1;i<trArr.length-1;i++){
		result = checkRescVa(trArr[i], true);
		if(result<0) return;
		$(trArr[i]).find("[name='valid']").val( (result>0) ? 'Y' : '');
		count += result;
	}
	if(count==0 && $('#delList').val()==''){
		alertMsg("cm.msg.nodata.toSave");<%//cm.msg.nodata.toSave=저장할 데이터가 없습니다.%>
		return;
	}
	
	<%// 정렬순서 세팅%>
	var ordr = 1;
	$("#catListArea input[name='sortOrdr']").each(function(){
		if($(this).attr('id')!='sortOrdr_NO'){
			$(this).val(ordr++);
		}
	});
	<%// disable 된 select disable 해제 %>
	var arr = releaseDisable($('#catListForm select'));
	
	<%// 서버 전송%>
	var $form = $("#catListForm");
	$form.attr('method','post');
	$form.attr('action','./transCatList.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form.submit();
	
	<%// select disable 다시 적용 %>
	unreleaseDisable(arr);
	
	dialog.close(this);
}<%//어권별 리소스 input에 값이 있는지 체크%>
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
}<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
}<%// 일괄 언어 선택 변경%>
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
	$("#catListArea input[type=checkbox]:checked").each(function(){
		va = $(this).val();
		if(va!=null && va!='') { arr.push(va); }
	});
	return arr.length==0 ? null : arr;
}
$(document).ready(function() {
	$("#catListArea tbody:first").children().each(function(){
		<%// 행추가 영역 제외하고 uniform 적용%>
		if($(this).attr('id')!='hiddenTr'){
			setJsUniform(this);
		}
	});
});
//-->
</script>
<div style="width:300px;">
<c:if test="${isSelect eq true }">
<u:listArea id="catListArea" >

	<tr id="headerTr">
		<td width="20px" class="head_bg">
		<c:if test="${param.fncMul != 'Y'}">&nbsp;</c:if>
		<c:if test="${param.fncMul == 'Y'}"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('catListArea', this.checked);" value=""/></c:if>
		</td>
		<th class="head_ct"><u:msg titleId="dm.cols.cat" alt="카테고리" /></th>
	</tr>
	<c:forEach items="${stCatBVoList}" var="stCatBVo" varStatus="status">
	<tr>
		<td class="bodybg_ct">
			<c:if test="${param.fncMul != 'Y'}"><u:radio id="check_${stCatBVo.catId }" name="catChk" value="${stCatBVo.catId }" checked="${status.first }" /><input type="hidden" name="catId" value="${stCatBVo.catId }" /></c:if>
			<c:if test="${param.fncMul == 'Y'}">
				<u:checkbox id="check_${stCatBVo.catId }" name="catChk" value="${stCatBVo.catId }" checked="${status.first }" /><input type="hidden" name="catId" value="${stCatBVo.catId }" />
			</c:if>
		</td>
		<td class="body_ct"><div class="ellipsis" title="${stCatBVo.catNm }"><label for="check_${stCatBVo.catId }">${stCatBVo.catNm }</label></div></td>
	</tr>
	</c:forEach>
	<c:if test="${empty stCatBVoList }">
		<tr>
		<td class="nodata" colspan="2"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
</u:listArea>
<% // 하단 버튼 %>
	<u:buttonArea>
	<u:button titleId="cm.btn.choice" alt="선택" onclick="selectCat();" auth="W" />
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" auth="R" />
	</u:buttonArea>
</c:if>
<c:if test="${isSelect eq false }">
<u:title titleId="st.jsp.cat.title" type="small" alt="카테고리">
	<u:titleIcon type="up" onclick="moveCat('up');" id="rightUp" auth="W" />
	<u:titleIcon type="down" onclick="moveCat('down');" id="rightDown" auth="W" />
</u:title>
<div style="height:410px;overflow-y:auto;">
<form id="catListForm" >
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="dialog" value="setCatDialog" />
<u:listArea id="catListArea" >

	<tr id="headerTr">
		<th width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('catListArea', this.checked);" value=""/></th>
		<th class="head_ct"><u:mandatory /><a href="javascript:changeLangTypCds('catListForm');" title="<u:msg titleId="pt.jsp.terms.chgLangAll" alt="일괄 언어 변경" />"><u:msg titleId="st.cols.catNm" alt="카테고리명" /></a></th>
	</tr>
	
	<c:forEach items="${stCatBVoList}" var="stCatBVo" varStatus="status"
			 ><u:set test="${status.last}" var="trDisp" value="display:none"
			/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${stCatBVo.catId}"
			/><u:set test="${status.last}" var="catIndex" value="_NO" elseValue="_${status.index+1}"
			/>
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="${trId}" style="${trDisp}" >
		<td class="bodybg_ct"><input type="checkbox" value="${stCatBVo.catId}"/>
			<u:input type="hidden" id="sortOrdr${catIndex}" name="sortOrdr" />
			<u:input type="hidden" id="valid${catIndex}" name="valid" />
			<u:input type="hidden" id="catId${catIndex}" name="catId" value="${stCatBVo.catId }"/></td>
		<td>
			<table cellspacing="0" cellpadding="0" border="0">
			<tr>
			<td id="langTyp${catIndex}">
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="langStatus">
			<u:convert srcId="${stCatBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
			<u:set test="${langStatus.first}" var="style" value="" elseValue="display:none;" />
			<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
			<u:input id="rescVa_${langTypCdVo.cd}${catIndex}" name="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="st.cols.catNm" value="${rescVa}" style="${style}"
				maxByte="200" validator="changeLangSelectorCat('catListArea', id, va)" mandatory="Y" />
			</c:forEach>
			</td>
			<td>
			<c:if test="${fn:length(_langTypCdListByCompId)>1}">
				<select id="langSelector${catIndex}" onchange="changeLangTypCd('catListArea','langTyp${catIndex}', this.value)" <u:elemTitle titleId="cols.langTyp" />>
				<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="langStatus">
				<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
				</c:forEach>
				</select>
			</c:if>
			<u:input type="hidden" id="rescId${catIndex}" name="rescId" value="${stCatBVo.rescId}" />
			</td>
			</tr>
			</table>
		</td>
	</tr>
	</c:forEach>
	
</u:listArea>
<input type="hidden" id="delList" name="delList" />
</form>
</div><% // 하단 버튼 %>
<u:buttonArea>
	<u:button href="javascript:addRowCat();" titleId="cm.btn.plus" alt="행추가" auth="W" />
	<u:button href="javascript:delRowCat();" titleId="cm.btn.minus" alt="행삭제" auth="W" />
	<u:button href="javascript:delSelRowCat();" titleId="cm.btn.selDel" alt="선택삭제" auth="W" />
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:saveCat();" auth="W" />
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</c:if>
</div>