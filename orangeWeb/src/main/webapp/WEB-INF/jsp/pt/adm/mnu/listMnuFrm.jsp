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
	$("#mnuListArea tbody:first input[type='checkbox']:checked").each(function(){
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
var gMaxRow = parseInt('${fn:length(ptMnuDVoList)}');
<%// 행추가%>
function addRow(){
	var $tr = $("#mnuListArea tbody:first #hiddenTr");
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
	var $tr = $("#mnuListArea tbody:first #hiddenTr").prev();
	delRowInArr([$tr[0]]);
}
<%// 선택삭제%>
function delSelRow(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) delRowInArr(arr);
}
<%// 삭제 - 배열에 담긴 목록%>
function delRowInArr(rowArr){
	var delVa = $("#delList").val(), delArr = [], $mnuId;
	if(delVa!='') delArr.push(delVa);
	for(var i=0;i<rowArr.length;i++){
		$mnuId = $(rowArr[i]).find("input[name='mnuId']");
		if($mnuId.val()!=''){
			delArr.push($mnuId.val());
		}
		$(rowArr[i]).remove();
	}
	$("#delList").val(delArr.join(','));
}
<%
//- 해당 어권의 명이 입력되지 않았 을 경우 해당 어권을 보이게 함 %>
function changeLangSelectorMnu(areaId, id, va){
	if(va==''){
		var langSelector = $('#'+areaId+' #langSelector'+id.substring(id.lastIndexOf('_')));
		var nm = $("#mnuListArea #"+id).attr("name");
		langSelector.val(nm.substring(nm.length-2));
		langSelector.trigger('click');
	}
}
<%// 저장%>
function saveMnu(){

	var arr=[], count = 0, map, valueCnt;
	$("#mnuListArea tbody:first").children().each(function(){
		arr.push(this);
	});
	
	<%// 어권별 리소스가 있는지 체크함%>
	for(var i=1;i<arr.length-1;i++){
		map = new ParamMap().getData(arr[i]);
		if(map.get('fldYn')=='N'){
			if(map.get('mnuUrl')!=''){
				valueCnt = checkRescVa(arr[i]);
			} else {
				valueCnt = checkRescVa(arr[i], true);
				if(valueCnt>0){
					var id = 'mnuUrl_'+i, va='';
					var handler = validator.getHandler(id);
					if(handler!=null && handler(id, va)==false){
						$('#'+id).focus();
						return;
					}
				}
			}
		} else {
			valueCnt = checkRescVa(arr[i], true);
		}
		if(valueCnt<0) return;
		$(arr[i]).find("[name='valid']").val( (valueCnt>0) ? 'Y' : '');
		count += valueCnt;
	}
	
	if(count==0 && $('#delList').val()==''){
		alertMsg("cm.msg.nodata.toSave");<%//cm.msg.nodata.toSave=저장할 데이터가 없습니다.%>
		return;
	}
	
	<%// 정렬순서 세팅%>
	var ordr = 1;
	$("#mnuListArea input[name='sortOrdr']").each(function(){
		if($(this).attr('id')!='sortOrdr_NO'){
			$(this).val(ordr++);
		}
	});
	<%// disable 된 select disable 해제 %>
	var arr = releaseDisable($('#mnuListForm select'));
	
	<%// 서버 전송%>
	var $form = $("#mnuListForm");
	$form.attr('method','post');
	$form.attr('action','./transMnuList.do');
	$form.attr('target','dataframeForFrame');
	$form.submit();
	
	<%// select disable 다시 적용 %>
	unreleaseDisable(arr);
}

<%//어권별 리소스 input에 값이 있는지 체크%>
function checkRescVa(trObj, ignoreAllEmpty){
	var i, id, va, handler, arr=[];
	$(trObj).find("td[id^='langTyp'] input").each(function(){ arr.push(this); });
	
	var hasVa = null;
	for(i=0;i<arr.length;i++){
		id = $(arr[i]).attr('id');
		va = $(arr[i]).val();
		if(ignoreAllEmpty!=true){
			handler = validator.getHandler(id);
			if(handler!=null && handler(id, va)==false){
				$(arr[i]).focus();
				return -1;
			}
		} else {
			if(va!='') hasVa = true;
			else if(hasVa==true) {
				handler = validator.getHandler(id);
				if(handler!=null && handler(id, va)==false){
					$(arr[i]).focus();
					return -1;
				}
			} else {
				hasVa = false;
			}
		}
	}
	if(ignoreAllEmpty!=true){
		return 1;
	} else {
		return hasVa==true ? 1 : 0;
	}
}
<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
}
<%//폴더여부 변경%>
function onChangeFldYn(object, va){
	var tr = getParentTag(object, 'tr');
	var disable = 'Y'==va;
	setReadonly($(tr).find("[name='mnuUrl']"), disable);
	setDisabled($(tr).find("[name='mnuTypCd']"), disable);
	if(disable || $(tr).find("[name='mnuTypCd'] option:selected").val() != 'OUT_POP'){
		disable = true;
	}
	setReadonly($(tr).find("[name='popSetupCont']"), disable);
}
<%//팝업여부 변경%>
function onChangeMnuTyp(object, va){
	var tr = getParentTag(object, 'tr');
	var disable = 'N'==va;
	if(disable || $(tr).find("[name='mnuTypCd'] option:selected").val() != 'OUT_POP'){
		disable = true;
	}
	setReadonly($(tr).find("[name='popSetupCont']"), disable);
}
<%// 저장 후 리로드%>
function afterTrans(mnuPid){
	parent.getIframeContent('mnuTree').reoladFrame(mnuPid);
	parent.openMnu(mnuPid,'Y');
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
	$("#mnuListArea tbody:first").children().each(function(){
		<%// 폴더인 경우 - URL, 팝업여부, 팝업설정 Disable %>
		if($(this).find("[name='fldYn']").val()=='Y'){
			setReadonly($(this).find("[name='mnuUrl']"), true);
			setDisabled($(this).find("[name='mnuTypCd']"), true);
			setReadonly($(this).find("[name='popSetupCont']"), true);
		<%// 팝업이 아닌 경우 - 팝업설정 Disable %>
		} else if($(this).find("[name='mnuTypCd'] option:selected").val() != 'OUT_POP') {
			setReadonly($(this).find("[name='popSetupCont']"), true);
		}
		<%// 행추가 영역 제외하고 uniform 적용%>
		if($(this).attr('id')!='hiddenTr'){
			setJsUniform(this);
		}
	});
});
//-->
</script>

<form id="mnuListForm" style="padding:10px;">
<input type="hidden" name="menuId" value="${menuId}" /><c:if
	test="${not empty param.mnuPid}">
<u:input type="hidden" id="mnuPid" value="${param.mnuPid}" /></c:if><c:if
	test="${not empty param.mnuGrpId}">
<u:input type="hidden" id="mnuGrpId" value="${param.mnuGrpId}" /></c:if><c:if
	test="${not empty param.mnuGrpMdCd}">
<u:input id="mnuGrpMdCd" type="hidden" value="${param.mnuGrpMdCd}" /></c:if>
<u:listArea id="mnuListArea" >

	<tr id="headerTr">
		<th width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('mnuListArea', this.checked);" value=""/></th><c:if
			test="${param.mnuGrpMdCd != 'M'}">
		<th width="7%" class="head_ct"><u:msg titleId="cols.fld" alt="폴더" /></th></c:if><c:if
			test="${fn:length(_langTypCdListByCompId)>1}">
		<th width="20%" class="head_ct"><u:mandatory /><a href="javascript:changeLangTypCds('mnuListForm');" title="<u:msg
				titleId="pt.jsp.terms.chgLangAll" alt="일괄 언어 변경" />"><u:msg titleId="cols.mnuNm" alt="메뉴명" /></a></th></c:if><c:if
			test="${fn:length(_langTypCdListByCompId)<=1}">
		<th width="20%" class="head_ct"><u:mandatory /><u:msg titleId="cols.mnuNm" alt="메뉴명" /></th></c:if>
		<th class="head_ct"><u:msg titleId="cols.mnuUrl" alt="메뉴URL" /></th>
		<th width="11%" class="head_ct"><u:msg titleId="cols.mnuTyp" alt="메뉴구분" /></th>
		<th width="15%" class="head_ct"><u:msg titleId="cols.popSetupCont" alt="팝업설정내용" /></th>
		<th width="7%" class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></th>
	</tr>
	
	<c:forEach items="${ptMnuDVoList}" var="ptMnuDVo" varStatus="status"
			 ><u:set test="${status.last}" var="trDisp" value="display:none"
			/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${ptMnuDVo.mnuId}"
			/><u:set test="${status.last}" var="mnuIndex" value="_NO" elseValue="_${status.index+1}"
			/>
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="${trId}" style="${trDisp}" >
		<td class="bodybg_ct"><input type="checkbox" /></td><c:if
			test="${param.mnuGrpMdCd != 'M'}">
		<td class="listicon_ct"><u:input type="hidden" id="mnuId" value="${ptMnuDVo.mnuId}" />
			<select id="fldYn" name="fldYn" onchange="onChangeFldYn(this, this.value);" <u:elemTitle titleId="cols.fldYn" />><u:decode srcValue="${ptMnuDVo.fldYn}" tgtValue="Y" var="checked" value="Y" elseValue="N" />
			<u:option value="Y" title="Y" checkValue="${checked}" />
			<u:option value="N" title="N" checkValue="${checked}" /></select></td></c:if><c:if
			test="${param.mnuGrpMdCd == 'M'}"><u:input type="hidden" id="mnuId" value="${ptMnuDVo.mnuId}"
				/><input type="hidden" name="fldYn" value="N" /></c:if>
		<td align="center">
			<table cellspacing="0" cellpadding="0" border="0">
			<tr>
			<td id="langTyp${mnuIndex}">
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="langStatus">
			<u:convert srcId="${ptMnuDVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
			<u:set test="${langStatus.first}" var="style" value="" elseValue="display:none;" />
			<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
			<u:input id="rescVa_${langTypCdVo.cd}${mnuIndex}" name="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.mnuNm" value="${rescVa}" style="width:100px; ${style}"
				maxByte="200" validator="changeLangSelectorMnu('mnuListArea', id, va)" mandatory="Y" />
			</c:forEach>
			</td>
			<td>
			<c:if test="${fn:length(_langTypCdListByCompId)>1}">
				<select id="langSelector${mnuIndex}" onchange="changeLangTypCd('mnuListArea','langTyp${mnuIndex}', this.value)" <u:elemTitle titleId="cols.langTyp" />>
				<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="langStatus">
				<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
				</c:forEach>
				</select>
			</c:if>
			<u:input type="hidden" id="rescId${mnuIndex}" name="rescId" value="${ptMnuDVo.rescId}" />
			</td>
			</tr>
			</table>
		</td>
		<td><u:input id="mnuUrl${mnuIndex}" name="mnuUrl" value="${ptMnuDVo.mnuUrl}" titleId="cols.mnuUrl" maxByte="1000" mandatory="Y" style="width:95%" /></td>
		<td class="listicon_ct"><select id="mnuTypCd${mnuIndex}" name="mnuTypCd" onchange="onChangeMnuTyp(this, this.value);" <u:elemTitle titleId="cols.mnuTyp" />>
			<c:forEach items="${mnuTypCdList}" var="mnuTypCd" varStatus="mnuTypStatus"><c:if
					test="${param.mnuGrpMdCd != 'M' or mnuTypCd.refVa1 == 'Mobile'}">
				<u:option value="${mnuTypCd.cd}" title="${mnuTypCd.rescNm}" checkValue="${ptMnuDVo.mnuTypCd}"/></c:if>
			</c:forEach></select>
		</td>
		<td><u:input id="popSetupCont${mnuIndex}" name="popSetupCont" value="${ptMnuDVo.popSetupCont}" titleId="cols.popSetupCont" maxByte="400" style="width:89%" /></td>
		<td class="listicon_ct"><select id="useYn${mnuIndex}" name="useYn" <u:elemTitle titleId="cols.useYn" />>
			<u:option value="Y" title="Y" checkValue="${ptMnuDVo.useYn}" />
			<u:option value="N" title="N" checkValue="${ptMnuDVo.useYn}" />
			</select>
			<u:input type="hidden" id="sortOrdr${mnuIndex}" name="sortOrdr" />
			<u:input type="hidden" id="valid${mnuIndex}" name="valid" />
		</td>
	</tr>
	</c:forEach>
	
</u:listArea>
<input type="hidden" id="clsCd" name="clsCd" value="${param.clsCd}" />
<input type="hidden" id="delList" name="delList" />
</form>