<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%// [순서조절:위로,아래로] 서버에 저장하지 않고 화면상에서만 순서 조정함 %>
function moveCd(direction){
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
var gMaxRow = parseInt('${fn:length(wfCdDVoList)}');
<%// 행추가%>
function addCdRow(){
	restoreUniform('cdListArea');
	var $tr = $("#cdListArea tbody:first #hiddenTr");
	var html = $tr[0].outerHTML;
	html = html.replace(/_NO/g,'_'+gMaxRow);
	gMaxRow++;
	$tr.before(html);
	$tr = $tr.prev();
	$tr.attr('id','');
	$tr.attr('style','');
	applyUniform('cdListArea');
	//setJsUniform($tr[0]);
}
<%// 행삭제%>
function delCdRow(){
	var $tr = $("#cdListArea tbody:first #hiddenTr").prev();
	if($tr && $tr.attr('id')!='headerTr')
		delCdRowInArr([$tr[0]]);
}
<%// 선택삭제%>
function delCdSelRow(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) delCdRowInArr(arr);
}
<%// 삭제 - 배열에 담긴 목록%>
function delCdRowInArr(rowArr){
	var delVa = $("#delList").val(), delArr = [], $mdId;
	if(delVa!='') delArr.push(delVa);
	for(var i=0;i<rowArr.length;i++){
		$mdId = $(rowArr[i]).find("input[name='cdId']");
		if($mdId.val()!=''){
			delArr.push($mdId.val());
		}
		$(rowArr[i]).remove();
	}
	$("#delList").val(delArr.join(','));
}<% //- 해당 어권의 명이 입력되지 않았 을 경우 해당 어권을 보이게 함 %>
function changeLangSelectorCd(areaId, id, va){
	if(va==''){
		var langSelector = $('#'+areaId+' #langSelector'+id.substring(id.lastIndexOf('_')));
		var nm = $("#cdListArea #"+id).attr("name");
		langSelector.val(nm.substring(nm.length-2));
		langSelector.trigger('click');
	}
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
}<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
}<% // [팝업:저장] 테이블 저장 %>
function saveCdGrp() {
	if (validator.validate('cdGrpArea')) {
		<%// trArr : 저장할 곳의 tr 테그 배열 %>
		var trArr=[], count=0, result;
		$("#cdListArea tbody:first").children().each(function(){
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
		$("#cdListArea input[name='sortOrdr']").each(function(){
			if($(this).attr('id')!='sortOrdr_NO'){
				$(this).val(ordr++);
			}
		});
		<%// disable 된 select disable 해제 %>
		var arr = releaseDisable($('#cdListArea select'));
		
		var $form = $('#setForm');
		$form.attr('method','post');
		$form.attr('action','./transCdGrpList.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form[0].submit();
		
		<%// select disable 다시 적용 %>
		unreleaseDisable(arr);
		
		dialog.close(this);
	}
};<%//어권별 리소스 input에 값이 있는지 체크%>
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
function pageReload(dialog){
	if(dialog!=undefined)
		parent.dialog.close(dialog);
	location.replace(location.href);
}
$(document).ready(function() {
});
//-->
</script>
<div style="width:400px;">
<form id="setForm">
<u:input type="hidden" id="menuId" value="${menuId }"/>
<u:input type="hidden" id="dialog" value="setCdGrpDialog"/>
<c:if test="${!empty wfCdGrpBVo }">
<u:input type="hidden" id="cdGrpId" value="${wfCdGrpBVo.cdGrpId }"/>
</c:if>
<u:title titleId="wf.jsp.codeGrp.title" type="small" alt="코드그룹" />
<% // 폼 필드 %>
	<u:listArea id="cdGrpArea" colgroup="20%,">
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg titleId="cols.grpNm" alt="그룹명" /></td>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tbody>
				<tr>
					<td id="langTypArea">
						<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:convert srcId="${wfCdGrpBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
							<u:set test="${status.first}" var="style" value="width:200px;" elseValue="width:200px; display:none" />
							<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
							<u:input id="grpRescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.grpNm" value="${rescVa}" style="${style}"
								maxByte="120" validator="changeLangSelector('cdGrpArea', id, va)" mandatory="Y" />
						</c:forEach>
						<u:input type="hidden" id="grpRescId" value="${wfCdGrpBVo.rescId}" />
					</td>
					<td id="langTypOptions">
						<c:if test="${fn:length(_langTypCdListByCompId)>1}">
							<select id="langSelector" onchange="changeLangTypCd('cdGrpArea','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
							<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
							</c:forEach>
							</select>
						</c:if>
					</td>
				</tr>
				</tbody>
			</table>
		</td>
	</tr>	<tr><td class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
		<td><u:checkArea><u:radio name="cdUseYn" value="Y" titleId="cm.option.use" alt="사용" checkValue="${wfCdGrpBVo.cdUseYn }"  inputClass="bodybg_lt" checked="${empty wfCdGrpBVo.cdUseYn }"
		/><u:radio name="cdUseYn" value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${wfCdGrpBVo.cdUseYn }" inputClass="bodybg_lt" /></u:checkArea></td></tr>
</u:listArea>
<u:blank />
<u:title titleId="wf.jsp.codeList.title" type="small" alt="코드목록" >
<u:titleButton href="javascript:addCdRow();" id="btnAddRow" titleId="cm.btn.plus" alt="행추가" auth="A" />
<u:titleButton href="javascript:delCdRow();" id="btnDelRow" titleId="cm.btn.minus" alt="행삭제" auth="A" />
<u:titleButton href="javascript:delCdSelRow();" id="btnDelSel" titleId="cm.btn.selDel" alt="선택삭제" auth="A" />
		
<u:titleIcon type="up" onclick="moveCd('up');" auth="W" />
<u:titleIcon type="down" onclick="moveCd('down');" auth="W" />
</u:title>
<u:listArea id="cdListArea" style="height:220px;overflow-y:auto;" colgroup="15%,,25%">	
	<tr id="headerTr">
		<th class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('cdListArea', this.checked);" value=""/></th>
		<th class="head_ct"><u:mandatory /><a href="javascript:changeLangTypCds('cdListArea');" title="<u:msg titleId="pt.jsp.terms.chgLangAll" alt="일괄 언어 변경" />"><u:msg titleId="wf.cols.code" alt="코드"/></a></th>
		<th class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></th>
	</tr>
	<u:set var="nodataStyle" test="${fn:length(wfCdDVoList)>1}" value="style=\"display:none;\"" elseValue=""/>
	<tr id="nodata" ${nodataStyle }>
	<td class="nodata" colspan="3"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
	<c:if test="${fn:length(wfCdDVoList)>0}">
		<c:forEach items="${wfCdDVoList}" var="vo" varStatus="status"
			 ><u:set test="${status.last}" var="trDisp" value="display:none"
			/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${vo.cdId}"
			/><u:set test="${status.last}" var="lstIndex" value="_NO" elseValue="_${status.index+1}"
			/>
			<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="${trId}" style="${trDisp}" >
				<td class="bodybg_ct"><input type="checkbox" id="listChk" value="${vo.cdId }" /><u:input type="hidden" id="cdId" value="${vo.cdId }"/></td>
				<td class="body_ct" ><table cellspacing="0" cellpadding="0" border="0">
		<tr>
		<td id="langTyp${lstIndex}">
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="langStatus">
		<u:convert srcId="${vo.rescId}_${langTypCdVo.cd}" var="rescVa" />
		<u:set test="${langStatus.first}" var="style" value="" elseValue="display:none;" />
		<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
		<u:input id="rescVa_${langTypCdVo.cd}${lstIndex}" name="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="wf.cols.codeNm" value="${rescVa}" style="${style}"
			maxByte="200" validator="changeLangSelectorCd('cdListArea', id, va)" mandatory="Y" />
		</c:forEach>
		</td>
		<td>
		<c:if test="${fn:length(_langTypCdListByCompId)>1}">
			<select id="langSelector${lstIndex}" onchange="changeLangTypCd('cdListArea','langTyp${lstIndex}', this.value)" <u:elemTitle titleId="cols.langTyp" />>
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="langStatus">
			<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
			</c:forEach>
			</select>
		</c:if>
		<u:input type="hidden" id="rescId${lstIndex}" name="rescId" value="${vo.rescId}" />
		<u:input type="hidden" id="sortOrdr${lstIndex}" name="sortOrdr" />
		<u:input type="hidden" id="valid${lstIndex}" name="valid" />
		</td>
		</tr>
		</table></td><td class="body_ct">
				<select id="useYn${lstIndex}" name="useYn" <u:elemTitle titleId="cols.useYn" />>
				<u:option value="Y" titleId="cm.option.use" alt="사용" checkValue="${vo.useYn}" />
				<u:option value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${vo.useYn}" />
				</select>
			</td>
			</tr>				
		</c:forEach>	
	</c:if>
	
</u:listArea>
<input type="hidden" id="delList" name="delList" />
	</form>
	<u:blank />
	<% // 하단 버튼 %>
<u:buttonArea style="clear:both;">
	<u:button titleId="cm.btn.save" onclick="saveCdGrp();" alt="저장" auth="W" />
	<u:button href="javascript:void(0)" onclick="dialog.close(this);" alt="닫기" titleId="cm.btn.close" />
</u:buttonArea>
</div>
