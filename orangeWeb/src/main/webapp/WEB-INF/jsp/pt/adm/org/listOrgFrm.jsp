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
	$("#orgListArea tbody:first input[type='checkbox']:checked").each(function(){
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
var gMaxRow = parseInt('${fn:length(orOrgBVoList)}');
<%// 행추가%>
function addRow(){
	var $tr = $("#orgListArea tbody:first #hiddenTr");
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
	var $tr = $("#orgListArea tbody:first #hiddenTr").prev();
	delRowInArr([$tr[0]]);
}
<%// 선택삭제%>
function delSelRow(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) delRowInArr(arr);
}
<%// 삭제 - 배열에 담긴 목록%>
function delRowInArr(rowArr){
	var delVa = $("#delList").val(), delArr = [], $orgId;
	if(delVa!='') delArr.push(delVa);
	for(var i=0;i<rowArr.length;i++){
		$orgId = $(rowArr[i]).find("input[name='orgId']");
		if($orgId.val()!=''){
			delArr.push($orgId.val());
		}
		$(rowArr[i]).remove();
	}
	$("#delList").val(delArr.join(','));
}
<%
//- 해당 어권의 명이 입력되지 않았 을 경우 해당 어권을 보이게 함 %>
function changeLangSelectorOrg(areaId, id, va){
	if(va==''){
		var langSelector = $('#'+areaId+' #langSelector'+id.substring(id.lastIndexOf('_')));
		var nm = $("#orgListArea #"+id).attr("name");
		langSelector.val(nm.substring(nm.length-2));
		langSelector.trigger('click');
	}
}
<%// 저장%>
function saveOrg(){
	<%// trArr : 저장할 곳의 tr 테그 배열 %>
	var trArr=[], count=0, result;
	$("#orgListArea tbody:first").children().each(function(){
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
	$("#orgListArea input[name='sortOrdr']").each(function(){
		if($(this).attr('id')!='sortOrdr_NO'){
			$(this).val(ordr++);
		}
	});
	<%// disable 된 select disable 해제 %>
	var arr = releaseDisable($('#orgListForm select'));
	
	<%// 서버 전송%>
	var $form = $("#orgListForm");
	$form.attr('method','post');
	$form.attr('action','./transOrgList.do');
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
function afterTrans(orgPid){
	parent.getIframeContent('orgTree').reoladFrame(orgPid);
	parent.openUserListFrm(orgPid);
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
<%// 조직구분 일괄 변경 %>
function changeOrgTyp(areaId){
	var $typs = $("#"+areaId+" select[name='orgTypCd']");
	var $first = $typs.first();
	if($first.length>0){
		var maxIndex = $first.children().length;
		var selIndex = $first[0].selectedIndex;
		if(++selIndex == maxIndex) selIndex = 0;
		$typs.each(function(){
			this.selectedIndex = selIndex;
			$(this).trigger('click');
		});
	}
}
<%// 체크된 목록 리턴 - 잘라내기용 %>
function getCheckedArray(){
	var arr = [], va;
	$("#orgListArea input[type=checkbox]:checked").each(function(){
		va = $(this).val();
		if(va!=null && va!='') { arr.push(va); }
	});
	return arr.length==0 ? null : arr;
}
<%// 붙여 넣기 %>
function pasteArray(cutInfo){
	callAjax('./transOrgPasteAjx.do?menuId=${menuId}', cutInfo, function(data){
		if(data.message!=null){
			alert(data.message);
		}
		if(data.orgId != null){
			parent.getIframeContent('orgTree').reoladFrame(data.orgId);
			reload();
		}
	});
}
$(document).ready(function() {
	$("#orgListArea tbody:first").children().each(function(){
		<%// 행추가 영역 제외하고 uniform 적용%>
		if($(this).attr('id')!='hiddenTr'){
			setJsUniform(this);
		}
	});
});
//-->
</script>

<form id="orgListForm" style="padding:10px;">
<input type="hidden" name="menuId" value="${menuId}" /><c:if test="${not empty param.orgPid}">
<u:input type="hidden" id="orgPid" value="${param.orgPid}" /></c:if>
<u:listArea id="orgListArea" >

	<tr id="headerTr">
		<th width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('orgListArea', this.checked);" value=""/></th>
		<th width="10%" class="head_ct"><c:if
			test="${fn:length(orgTypCdList)<=1}"><u:msg titleId="cols.orgTyp" alt="조직구분" /></c:if><c:if
			test="${fn:length(orgTypCdList)>1}"><a href="javascript:changeOrgTyp('orgListForm');" title="<u:msg titleId="cm.changAll" alt="일괄변경"/>"><u:msg titleId="cols.orgTyp" alt="조직구분" /></a></c:if></th>
		<c:if test="${fn:length(_langTypCdListByCompId)>1}">
		<th width="25%" class="head_ct"><u:mandatory /><a href="javascript:changeLangTypCds('orgListForm');" title="<u:msg titleId="pt.jsp.terms.chgLangAll" alt="일괄 언어 변경" />"><u:msg titleId="cols.orgNm" alt="조직명" /></a></th>
		</c:if>
		<c:if test="${fn:length(_langTypCdListByCompId)<=1}">
		<th width="25%" class="head_ct"><u:mandatory /><u:msg titleId="cols.orgNm" alt="조직명" /></th>
		</c:if>
		<th class="head_ct"><u:msg titleId="cols.orgId" alt="조직ID" /></th>
		<th width="12%" class="head_ct"><c:if
			test="${'ROOT' eq param.orgPid}"><u:msg titleId="or.sel.comp" alt="회사 선택" /></c:if><c:if
			test="${'ROOT' ne param.orgPid}"><u:msg titleId="cols.rid" alt="참조ID" /></c:if></th>
		<th width="12%" class="head_ct"><u:msg titleId="cols.refVa1" alt="참조값1" /></th>
		<th width="12%" class="head_ct"><u:msg titleId="cols.refVa2" alt="참조값2" /></th>
		<th width="8%" class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></th>
	</tr>
	
	<c:forEach items="${orOrgBVoList}" var="orOrgBVo" varStatus="status"
			 ><u:set test="${status.last}" var="trDisp" value="display:none"
			/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${orOrgBVo.orgId}"
			/><u:set test="${status.last}" var="orgIndex" value="_NO" elseValue="_${status.index+1}"
			/>
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="${trId}" style="${trDisp}" >
		<td class="bodybg_ct"><input type="checkbox" value="${orOrgBVo.orgId}"/></td>
		<td class="listicon_ct">
			<select id="orgTypCd" name="orgTypCd" <u:elemTitle titleId="cols.orgTyp" />><c:forEach
				items="${orgTypCdList}" var="orgTypCdVo" varStatus="cdStatus">
				<u:option value="${orgTypCdVo.cd}" title="${orgTypCdVo.rescNm}" checkValue="${orOrgBVo.orgTypCd}"
			 	/></c:forEach>
			 </select></td>
		<td>
			<table cellspacing="0" cellpadding="0" border="0">
			<tr>
			<td id="langTyp${orgIndex}">
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="langStatus">
			<u:convert srcId="${orOrgBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
			<u:set test="${langStatus.first}" var="style" value="" elseValue="display:none;" />
			<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
			<u:input id="rescVa_${langTypCdVo.cd}${orgIndex}" name="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.orgNm" value="${rescVa}" style="${style}"
				maxByte="200" validator="changeLangSelectorOrg('orgListArea', id, va)" mandatory="Y" />
			</c:forEach>
			</td>
			<td>
			<c:if test="${fn:length(_langTypCdListByCompId)>1}">
				<select id="langSelector${orgIndex}" onchange="changeLangTypCd('orgListArea','langTyp${orgIndex}', this.value)" <u:elemTitle titleId="cols.langTyp" />>
				<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="langStatus">
				<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
				</c:forEach>
				</select>
			</c:if>
			<u:input type="hidden" id="rescId${orgIndex}" name="rescId" value="${orOrgBVo.rescId}" />
			</td>
			</tr>
			</table>
		</td>
		<td><u:set
			test="${not empty orOrgBVo.orgId}" var="readonly" value="Y" elseValue="N"
			/><u:input id="orgId${orgIndex}" name="orgId" value="${orOrgBVo.orgId}" titleId="cols.orgId" maxByte="30"
			style="width:87%" readonly="${readonly}" valueOption="alpha,number" valueAllowed="_" /></td>
		<td><c:if
			test="${'ROOT' eq param.orgPid}"><select id="compId${orgIndex}" name="compId" <u:elemTitle titleId="cols.compId" />>
			<c:forEach items="${ptCompBVoList}" var="ptCompBVo" varStatus="compStatus">
			<u:option value="${ptCompBVo.compId}" title="${ptCompBVo.rescNm}" checkValue="${orOrgBVo.compId}"
				/></c:forEach></select></c:if><c:if
			test="${'ROOT' ne param.orgPid}"><u:input
			id="rid${orgIndex}" name="rid" value="${orOrgBVo.rid}" titleId="cols.rid" maxByte="30"
			style="width:85%" valueOption="alpha,number" valueAllowed="_" /></c:if></td>
		<td><u:input id="refVa1${orgIndex}" name="refVa1" value="${orOrgBVo.refVa1}" titleId="cols.refVa1" maxByte="30" style="width:85%" /></td>
		<td><u:input id="refVa2${orgIndex}" name="refVa2" value="${orOrgBVo.refVa2}" titleId="cols.refVa2" maxByte="30" style="width:85%" /></td>
		<td class="listicon_ct"><select id="useYn${orgIndex}" name="useYn" <u:elemTitle titleId="cols.useYn" />>
			<u:option value="Y" title="Y" checkValue="${orOrgBVo.useYn}" />
			<u:option value="N" title="N" checkValue="${orOrgBVo.useYn}" />
			</select>
			<u:input type="hidden" id="sortOrdr${orgIndex}" name="sortOrdr" />
			<u:input type="hidden" id="valid${orgIndex}" name="valid" />
		</td>
	</tr>
	</c:forEach>
	
</u:listArea>
<input type="hidden" id="delList" name="delList" />
</form>