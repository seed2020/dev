<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="params"/>
<script type="text/javascript">
<!--
function colorPreview(id){
	if(id.indexOf("font") > -1 ) $("#"+id+"Preview").css('color',$('#'+id).val()); 
	else $("#"+id+"Preview").css('background',$('#'+id).val());
};
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
};
<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
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
};
<%// 다음 Row 번호 %>
var gMaxRow = parseInt('${fn:length(CtSchdlCatClsBVoList)}');
<%// 행추가%>
function addRow(){
	var $tr = $("#listArea tbody:first #hiddenTr");
	var html = $tr[0].outerHTML;
	html = html.replace(/_NO/g,'_'+gMaxRow);
	gMaxRow++;
	$tr.before(html);
	$tr = $tr.prev();
	$tr.attr('id','');
	$tr.attr('style','');
	setJsUniform($tr[0]);
};
<%// 행삭제%>
function delRow(){
	var $tr = $("#listArea tbody:first #hiddenTr").prev();
	delRowInArr([$tr[0]]);
};
<%// 선택삭제%>
function delSelRow(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) delRowInArr(arr);
};
<%// 삭제 - 배열에 담긴 목록%>
function delRowInArr(rowArr){
	var delVa = $("#delList").val(), delArr = [], $userUid;
	if(delVa!='') delArr.push(delVa);
	for(var i=0;i<rowArr.length;i++){
		$userUid = $(rowArr[i]).find("input[name='catId']");
		if($userUid.val()!=''){
			delArr.push($userUid.val());
		}
		$(rowArr[i]).remove();
	}
	$("#delList").val(delArr.join(','));
};
<%
//- 해당 어권의 명이 입력되지 않았 을 경우 해당 어권을 보이게 함 %>
function changeLangSelectorUser(areaId, id, va){
	if(va==''){
		var langSelector = $('#'+areaId+' #langSelector'+id.substring(id.lastIndexOf('_')));
		var nm = $("#listArea #"+id).attr("name");
		langSelector.val(nm.substring(nm.length-2));
		langSelector.trigger('click');
	}
};
<%// 저장%>
function save(){
	<%	// trArr : 저장할 곳의 tr 테그 배열
	%>
	var trArr=[], count=0, result;
	$("#listArea tbody:first").children().each(function(){
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
	$("#listArea input[name='sortOrdr']").each(function(){
		if($(this).attr('id')!='sortOrdr_NO'){
			$(this).val(ordr++);
		}
	});
	if (true/*confirmMsg("cm.cfrm.save")*/ ) {
		<%// 서버 전송%>
		var $form = $("#setRegForm");
		$form.attr('method','post');
		$form.attr('action','./transCatCls.do');
		$form.attr('target','dataframe');
		$form.submit();
	}
};

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
};
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
};
<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
};

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
<u:title titleId="wc.jsp.listCatClsMng.title" alt="카테고리관리" menuNameFirst="true">
	<u:titleIcon type="up" onclick="move('up')" id="rightUp" />
	<u:titleIcon type="down" onclick="move('down')" id="rightDown" />
</u:title>
<u:titleArea
	outerStyle="height:514px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV">
<form id="setRegForm" style="padding:10px;">
	<input type="hidden" name="menuId" value="${menuId}" />
	<input type="hidden" name="listPage" value="./listCatCls.do?${params }"/>
	<div class="listarea" id="listArea" >
		<table class="listtable" border="0" cellpadding="0" cellspacing="1" >
		<colgroup><col width="25px"/><col width="*"/><col width="20%"/><col width="20%"/><col width="15%"/><col width="12%"/></colgroup>
		<tr id="headerTr">
			<th width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></th>
			<td class="head_ct"><u:mandatory /><a href="javascript:changeLangTypCds('listArea');" title="<u:msg titleId="pt.jsp.terms.chgLangAll" alt="일괄 언어 변경" />"><u:msg titleId="wc.cols.catNm" alt="카테고리명" /></a></td>
			<td class="head_ct"><u:msg titleId="wc.cols.bgCol" alt="배경색상" /></td>
			<td class="head_ct"><u:msg titleId="wc.cols.fontColr" alt="글자색상" /></td>
			<td class="head_ct"><u:msg titleId="wc.cols.preview" alt="미리보기" /></td>
			<td class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></td>		
		</tr>
		<c:forEach var="list" items="${CtSchdlCatClsBVoList }" varStatus="status">
			<u:set test="${status.last}" var="trDisp" value="display:none"/>
			<u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${list.catId}"/>
			<u:set test="${status.last}" var="userIndex" value="_NO" elseValue="_${status.index+1}"/>
			<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"' id="${trId}" style="${trDisp}">
				<td class="bodybg_ct"><input type="checkbox" value="${list.catId}"/></td>
				<td class="body_ct">
					<table cellspacing="0" cellpadding="0" border="0">
						<tr>
						<td id="langTyp${userIndex}">
						<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="langStatus">
						<u:convert srcId="${list.rescId}_${langTypCdVo.cd}" var="rescVa" />
						<u:set test="${langStatus.first}" var="style" value="" elseValue="display:none;" />
						<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
						<u:input id="rescVa_${langTypCdVo.cd}${userIndex}" name="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="wc.cols.catNm" value="${rescVa}" style="width:180px; ${style}"
							maxByte="120" validator="changeLangSelectorUser('listArea', id, va)" mandatory="Y" />
						</c:forEach>
						</td>
						<td>
						<c:if test="${fn:length(_langTypCdListByCompId)>1}">
							<select id="langSelector${userIndex}" onchange="changeLangTypCd('listArea','langTyp${userIndex}', this.value)" <u:elemTitle titleId="cols.langTyp" />>
							<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="langStatus">
							<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
							</c:forEach>
							</select>
						</c:if>
						<u:input type="hidden" id="rescId${userIndex}" name="rescId" value="${list.rescId}" />
						</td>
						</tr>
					</table>
				</td>
				<td class="body_ct"><u:color id="bgcolCd${userIndex }" input="Y" name="bgcolCd" value="${empty list.bgcolCd ? '#0080FF' : list.bgcolCd }" handler="colorPreview"/></td>
				<td class="body_ct"><u:color id="fontColrCd${userIndex}" input="Y" name="fontColrCd" value="${empty list.fontColrCd ? '#FFFFFF' : list.fontColrCd }" handler="colorPreview"/></td>
				<td class="body_ct"><div id="bgcolCd${userIndex }Preview" style="width:100px;border-radius:3px;margin:auto;background:${empty list.bgcolCd ? '#0080FF' : list.bgcolCd};"><span id="fontColrCd${userIndex}Preview" style="color:${empty list.fontColrCd ? '#FFFFFF' : list.fontColrCd};">가나다</span></div></td>
				<td class="body_ct">
					<select id="useYn${userIndex}" name="useYn" <u:elemTitle termId="cols.useYn" alt="사용여부" />>
						<u:option value="Y" titleId="cm.option.use" checkValue="${list.useYn}" selected="${empty list.useYn }"/>
						<u:option value="N" titleId="cm.option.notUse" checkValue="${list.useYn}"	/>
					</select>
					<input type="hidden" name="catId" value="${list.catId }"/>
					<u:input type="hidden" id="sortOrdr${userIndex}" name="sortOrdr" />
					<u:input type="hidden" id="valid${userIndex}" name="valid" />
				</td>
			</tr>
		</c:forEach>
		</table>
	</div>
	<input type="hidden" id="delList" name="delList" />
</form>
</u:titleArea>
<u:blank />
<% // 하단 버튼 %>
<u:buttonArea>
	<u:button href="javascript:addRow();" id="btnAddRow" titleId="cm.btn.plus" alt="행추가" auth="A" />
	<u:button href="javascript:delRow();" id="btnDelRow" titleId="cm.btn.minus" alt="행삭제" auth="A" />
	<u:button href="javascript:delSelRow();" id="btnDelSel" titleId="cm.btn.selDel" alt="선택삭제" auth="A" />
	<u:button titleId="cm.btn.save" alt="저장" onclick="save();" auth="A" />
</u:buttonArea>
