<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

	request.setAttribute("onecTypCds", new String[]{"Item", "Project"});
	//request.setAttribute("clsCds", new String[]{"CAT_CD", "ORGN_CD", "PACK_CD", "STORG_CD"});

	// 기타 설정
	request.setAttribute("sysCds", new String[]{ "formId" });

	// 권한(역할코드) - ONEC_ADM:원카드 관리자, ONEC_VWR:원카드 전체 조회자
	
%><script type="text/javascript">
<!--<%--
[위로(△) 아래로(▽) 아이콘 클릭] 순서 조절 --%>
function moveSort(clsCd, direction){
	var i, arr = getCheckedTrs(clsCd);
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
<%--checkbox 가 선택된 tr 테그 목록 리턴 --%>
function getCheckedTrs(clsCd){
	var arr=[], id, obj;
	$("#"+clsCd+"Area tbody:first input[type='checkbox']:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		return null;
	}
	return arr;
}<%--
[추가(+) 삭제(-) 아이콘 클릭] 추가 삭제 --%>
function addItemCd(clsCd, act){
	if(act=='remove'){
		var i, arr = getCheckedTrs(clsCd);
		if(arr==null) return;
		for(i=0;i<arr.length;i++){
			$(arr[i]).remove();
		}
	} else if(act=='add'){
		var hiddenTr = $("#"+clsCd+"Area tbody:first tr[id='hiddenTr']");
		hiddenTr.before(hiddenTr[0].outerHTML);
		setJsUniform(hiddenTr.prev().attr('id', '').show()[0]);
	}
}<%--
[저장 클릭]--%>
function saveCd(clsCd, to){
	if(!confirmMsg('cm.cfrm.save')) return;<%// cm.cfrm.save=저장하시겠습니까 ?%>
	var $form = $("#frm"+clsCd);
	$form.attr('method','post');
	if(to==null){
		$form.attr('action','/wo/transSettings.do');
	} else {
		$form.attr('action','/wo/'+to+'.do');
	}
	$form.attr('target','dataframe');
	$form.submit();
}<%--
[원카드 관리자, 원카드 전체 조회자] 변경 클릭 --%>
function setAuthPop(which){
	var $area = $("#authSettingArea");
	var uids = $area.find("input[name='"+which+"Uids']").val(), data = [];;
	if(uids!=''){
		uids.split(',').each(function(index, va){
			data.push({userUid:va});
		});
	}
	searchUserPop({data:data, multi:true, mode:'search'}, function(arr){
		if(arr==null){
			$area.find('#'+which+'AuthTd').html('');
			$area.find("input[name='"+which+"Uids']").val('');
		} else {
			var htmls=[], uids=[];
			arr.each(function(index, userVo){
				htmls.push("<a href=\"javascript:viewUserPop('"+userVo.userUid+"')\">"+userVo.rescNm+"</a>");
				uids.push(userVo.userUid);
			});
			$area.find('#'+which+'AuthTd').html(htmls.join(", "));
			$area.find("input[name='"+which+"Uids']").val(uids.join(','));
		}
	});
}
$(document).ready(function() {
	$("#settingsArea tbody").children("[id!='hiddenTr']").each(function(){
		setJsUniform(this);
	});
});<%--
유니폼 적용(스타일)--%>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='input:radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
}
//-->
</script>
<u:title alt="환경 설정" menuNameFirst="true" />

<u:tabGroup id="settingsTab">
<u:tab id="settingsTab" areaId="codeArea" titleId="pt.jsp.setCd.title" alt="코드 관리" on="true" />
<u:tab id="settingsTab" areaId="etcArea" titleId="wo.msg.etcSettings" alt="기타 설정" on="false" />
</u:tabGroup>

<div id="settingsArea">
<div id="codeArea">
<c:forEach items="${clsCds}" var="clsCd">
<u:title titleId="wo.clsCd.${clsCd}" type="small">
	<u:titleIcon type="up" href="javascript:moveSort('${clsCd}','up')" />
	<u:titleIcon type="down" href="javascript:moveSort('${clsCd}','down')" />
	<u:titleIcon type="plus" href="javascript:addItemCd('${clsCd}', 'add')" />
	<u:titleIcon type="minus" href="javascript:addItemCd('${clsCd}', 'remove')" />
</u:title>
<form id="frm${clsCd}">
<input type="hidden" name="menuId" value="${menuId}">
<input type="hidden" name="clsCd" value="${clsCd}">
<u:listArea id="${clsCd}Area" style="" colgroup="2.6%,15%,25%,10%,57.4%">
<tr id="headerTr">
	<th class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('${clsCd}Area', this.checked);" value=""/></th>
	<th class="head_ct"><u:mandatory /><u:msg titleId="cols.cd" alt="코드" /></th>
	<th class="head_ct"><u:mandatory /><u:msg titleId="cols.cdVa" alt="코드값" /></th>
	<th class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></th>
	<th class="head_ct"><u:msg titleId="cols.note" alt="비고" /></th>
</tr><u:convert
	srcId="${clsCd}List" var="cdList" /><c:forEach items="${cdList}" var="woOnecCdDVo" varStatus="status">
<tr id="${status.last ? 'hiddenTr' : ''}" style="${status.last ? 'display:none;' : ''}">
	<td class="bodybg_ct"><input type="checkbox" /></td>
	<td><u:input id="cd" name="cd" titleId="cols.cd" alt="코드" value="${woOnecCdDVo.cd}" style="width:90%" /></td>
	<td><u:input id="cdVa" name="cdVa" titleId="cols.cdVa" alt="코드값" value="${woOnecCdDVo.cdVa}" style="width:94%" /></td>
	<td style="text-align:center"><select id="useYn" name="useYn" <u:elemTitle titleId="cols.useYn" />>
			<u:option value="Y" title="Y" checkValue="${woOnecCdDVo.useYn}" />
			<u:option value="N" title="N" checkValue="${woOnecCdDVo.useYn}" />
			</select></td>
	<td><u:input id="note" name="note" titleId="cols.note" alt="비고" value="${woOnecCdDVo.note}" style="width:96.7%" /></td>
</tr></c:forEach>
</u:listArea>
</form>
<u:buttonArea noBottomBlank="true">
	<u:button href="javascript:saveCd('${clsCd}')" titleId="cm.btn.save" alt="저장" auth="A" />
</u:buttonArea>
<div class="blank"></div>
<div class="blank"></div>
</c:forEach>
</div>

<div id="etcArea" style="display:none;">

<u:title titleId="pt.btn.auth" alt="권한설정" type="small" />
<form id="frmAUTH">
<input type="hidden" name="menuId" value="${menuId}">
<input type="hidden" name="clsCd" value="AUTH">
<u:listArea id="authSettingArea" style="" colgroup="17.6%,82.4%">
<tr>
	<td class="head_ct"><u:msg titleId="wo.admAuth" alt="원카드 관리자" /><input type="hidden" name="admUids" value='<c:forEach items="${onecAdmList}" var="orUserBVo" varStatus="status"><c:if test="${not status.first}">,</c:if>${orUserBVo.userUid}</c:forEach>' /></td>
	<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0" style="width:100%"><tr>
		<td id="admAuthTd"><c:forEach
		items="${onecAdmList}" var="orUserBVo" varStatus="status"><c:if
			test="${not status.first}">, </c:if><a href="javascript:viewUserPop('${orUserBVo.userUid}');">${orUserBVo.rescNm}</a></c:forEach></td>
		<td style="text-align:right;"><u:buttonS titleId="cm.btn.mod" title="변경" onclick="setAuthPop('adm')" /></td></tr></table></td>
</tr>
<tr>
	<td class="head_ct"><u:msg titleId="wo.vwrAuth" alt="원카드 전체 조회자" /><input type="hidden" name="vwrUids" value='<c:forEach items="${onecVwrList}" var="orUserBVo" varStatus="status"><c:if test="${not status.first}">,</c:if>${orUserBVo.userUid}</c:forEach>' /></td>
	<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0" style="width:100%"><tr>
		<td id="vwrAuthTd"><c:forEach
		items="${onecVwrList}" var="orUserBVo" varStatus="status"><c:if
			test="${not status.first}">, </c:if><a href="javascript:viewUserPop('${orUserBVo.userUid}');">${orUserBVo.rescNm}</a></c:forEach></td>
		<td style="text-align:right;"><u:buttonS titleId="cm.btn.mod" title="변경" onclick="setAuthPop('vwr')" /></td></tr></table></td>
</tr>
</u:listArea>
</form>
<u:buttonArea noBottomBlank="true">
	<u:button href="javascript:saveCd('AUTH', 'transAuths')" titleId="cm.btn.save" alt="저장" auth="A" />
</u:buttonArea>
<div class="blank"></div>
<div class="blank"></div>


<u:title titleId="wo.etc" alt="기타 항목" type="small" />
<form id="frmSYS">
<input type="hidden" name="menuId" value="${menuId}">
<input type="hidden" name="clsCd" value="SYS">
<u:listArea id="etcSettingArea" style="" colgroup="17.6%,82.4%"><c:forEach
		items="${sysCds}" var="sysCd"><u:convertMap
		srcId="sysCdMap" attId="${sysCd}" var="woOnecCdDVo" />
<tr>
	<td class="head_ct"><u:msg titleId="wo.sysCd.${sysCd}" alt="결재 양식 ID .. 등" /></td>
	<td><input type="hidden" name="cd" value="${sysCd}" /><input type="hidden" name="useYn" value="Y" /><input type="hidden" name="note" value="" />
	<u:input id="cdVa" name="cdVa" titleId="wo.sysCd.${sysCd}" alt="결재 양식 ID .. 등" value="${woOnecCdDVo.cdVa}" style="width:98%" /></td>
</tr></c:forEach>
</u:listArea>
</form>
<u:buttonArea noBottomBlank="true">
	<u:button href="javascript:saveCd('SYS')" titleId="cm.btn.save" alt="저장" auth="A" />
</u:buttonArea>
<div class="blank"></div>
<div class="blank"></div>
</div>
</div>