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
			if($prev[0]!=$std[0] && $prev.attr('id') != 'trU0000001'){
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
	$("#userListArea tbody:first input[type='checkbox']:checked").each(function(){
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
	var $tr = $("#userListArea tbody:first #hiddenTr");
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
	var $tr = $("#userListArea tbody:first #hiddenTr").prev();
	delRowInArr([$tr[0]]);
}
<%// 선택삭제%>
function delSelRow(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) delRowInArr(arr);
}
<%// 삭제 - 배열에 담긴 목록%>
function delRowInArr(rowArr){
	var delVa = $("#delList").val(), delArr = [], $userUid;
	if(delVa!='') delArr.push(delVa);
	for(var i=0;i<rowArr.length;i++){
		$userUid = $(rowArr[i]).find("input[name='userUid']");
		if($userUid.val()!=''){
			delArr.push($userUid.val());
		}
		$(rowArr[i]).remove();
	}
	$("#delList").val(delArr.join(','));
}
<%
//- 해당 어권의 명이 입력되지 않았 을 경우 해당 어권을 보이게 함 %>
function changeLangSelectorUser(areaId, id, va){
	if(va==''){
		var langSelector = $('#'+areaId+' #langSelector'+id.substring(id.lastIndexOf('_')));
		var nm = $("#userListArea #"+id).attr("name");
		langSelector.val(nm.substring(nm.length-2));
		langSelector.trigger('click');
	}
}
<%// 저장%>
function saveOrg(){
	<%	// trArr : 저장할 곳의 tr 테그 배열
		// lginIds : 로그인ID 중복 체크용 - DB값과 비교는 submit 후에 하고 입력값 만 체크함
	%>
	var trArr=[], count=0, result, id, lginIdVa, lginIds = [];
	$("#userListArea tbody:first").children().each(function(){
		trArr.push(this);
	});
	var hasNewUser = false;
	<%// 어권별 리소스가 있는지 체크함%>
	for(var i=1;i<trArr.length-1;i++){
		id = 'lginId_'+i;
		lginIdVa = $(trArr[i]).find("input[name='lginId']").val();
		if(lginIdVa!=''){
			if(lginIds.contains(lginIdVa)){<%// 로그인ID 중복 검사 %>
				alertMsg("pt.login.dupId");<%//pt.login.dupId=중복된 로그인ID 입니다.%>
				$(trArr[i]).find("input[name='lginId']").focus();
				return;
			} else {
				lginIds.push(lginIdVa);
			}
			if(!checkLognIdVa(trArr[i], id, lginIdVa)) return;
			result = checkRescVa(trArr[i], false);
			if(result<0) return;
		} else {
			result = checkRescVa(trArr[i], true);
			if(result>0){
				if(!checkLognIdVa(trArr[i], id, lginIdVa)) return;
			} else if(result<0) return;
		}
		$(trArr[i]).find("[name='valid']").val( (result>0) ? 'Y' : '');
		count += result;
		if(!hasNewUser && result>0){
			if($(trArr[i]).find("[name='rescId']").val() == ''){
				hasNewUser = true;
			}
		}
	}
	
	if(count==0 && $('#delList').val()==''){
		alertMsg("cm.msg.nodata.toSave");<%//cm.msg.nodata.toSave=저장할 데이터가 없습니다.%>
		return;
	}
	
	<%// 정렬순서 세팅%>
	var ordr = 1;
	$("#userListArea input[name='sortOrdr']").each(function(){
		if($(this).attr('id')!='sortOrdr_NO'){
			$(this).val(ordr++);
		}
	});
	
	var initPwEnable = '${initPwEnable}';
	if(hasNewUser && initPwEnable=='true'){
		parent.dialog.open('setInitPwDialog','<u:msg titleId="pt.jsp.setPw.typeSys" alt="로그인 비밀번호"/>','./setInitPwPop.do?menuId=${menuId}');
	} else {
		processSaveOrg();
	}
}
function processSaveOrg(){
	<%// disable 된 select disable 해제 %>
	var arr = releaseDisable($('#orgListForm select'));
	
	<%// 서버 전송%>
	var $form = $("#orgListForm");
	$form.attr('method','post');
	$form.attr('action','./transUserList.do');
	$form.attr('target','dataframeForFrame');
	$form.submit();
	
	<%// select disable 다시 적용 %>
	unreleaseDisable(arr);
}
<%// 유효한 ID 값인지 검사 %>
function checkLognIdVa(trObj, id, va){
	var handler = validator.getHandler(id);
	if(handler!=null && handler(id, va)==false){
		$(trObj).find('#'+id).focus();
		return false;
	}
	return true;
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
function afterTrans(orgId){
	parent.getIframeContent('orgTree').reoladFrame(orgId);
	parent.openUserListFrm(orgId);
	parent.dialog.close('setInitPwDialog');
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
	$("#userListArea input[type=checkbox]:checked").each(function(){
		va = $(this).val();
		if(va!=null && va!='') { arr.push(va); }
	});
	return arr.length==0 ? null : arr;
}
<%// 붙여 넣기(이동/겸직,파견) %>
function pasteArray(cutInfo){
	callAjax('./transUserPasteAjx.do?menuId=${menuId}', cutInfo, function(data){
		if(data.message!=null){
			alert(data.message);
		}
		if(data.orgId != null){
			//parent.getIframeContent('orgTree').reoladFrame(data.orgId);
			reload();
		}
	});
}
$(document).ready(function() {
	$("#userListArea tbody:first").children().each(function(){
		<%// 행추가 영역 제외하고 uniform 적용%>
		if($(this).attr('id')!='hiddenTr'){
			setJsUniform(this);
		}
	});
});
//-->
</script>

<form id="orgListForm" style="padding:10px;">
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="orgId" value="${param.orgId}" />
<u:listArea id="userListArea" >

	<tr id="headerTr">
		<th width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('userListArea', this.checked);" value=""/></th>
		<th width="10%" class="head_ct"><u:mandatory /><u:msg titleId="cols.lginId" alt="로그인ID" /></th>
		<c:if
			test="${fn:length(_langTypCdListByCompId)>1}"
		><th width="19%" class="head_ct"><u:mandatory /><a href="javascript:changeLangTypCds('orgListForm');" title="<u:msg titleId="pt.jsp.terms.chgLangAll" alt="일괄 언어 변경" />"><u:msg titleId="cols.userNm" alt="사용자명"
		/></a></th></c:if><c:if
			test="${fn:length(_langTypCdListByCompId)<=1}">
		<th width="10%" class="head_ct"><u:mandatory /><u:msg titleId="cols.userNm" alt="사용자명" /></th></c:if>
		<th width="8%" class="head_ct"><u:msg titleId="cols.aduTyp" alt="겸직구분" /></th>
		<th class="head_ct"><u:msg titleId="cols.userUid" alt="사용자UID" /></th>
		<th width="10%" class="head_ct"><u:msg titleId="cols.gen" alt="성별" /></th>
		<th width="12%" class="head_ct"><u:term termId="or.term.posit" alt="직위" /></th>
		<th width="12%" class="head_ct"><u:term termId="or.term.title" alt="직책" /></th>
		<th width="12%" class="head_ct"><u:msg titleId="or.cols.statCd" alt="상태코드" /></th>
	</tr>
	
	<c:forEach items="${orUserBVoList}" var="orUserBVo" varStatus="status"
			 ><u:set test="${status.last or orUserBVo.userUid eq 'U0000001'}" var="trDisp" value="display:none"
			/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${orUserBVo.userUid}"
			/><u:set test="${status.last}" var="userIndex" value="_NO" elseValue="_${status.index+1}"
			/>
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="${trId}" style="${trDisp}" >
		<td class="bodybg_ct"><input type="checkbox" value="${orUserBVo.userUid}"/></td>
		<td class="listicon_ct"><u:input id="lginId${userIndex}" name="lginId" value="${orUserBVo.orOdurBVo.lginId}" titleId="cols.lginId" maxByte="40"
			style="width:80%" valueOption="alpha,number" valueAllowed="_.-" mandatory="Y" /></td>
		<td>
			<table cellspacing="0" cellpadding="0" border="0">
			<tr>
			<td id="langTyp${userIndex}">
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="langStatus">
			<u:convert srcId="${orUserBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
			<u:set test="${langStatus.first}" var="style" value="" elseValue="display:none;" />
			<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
			<u:input id="rescVa_${langTypCdVo.cd}${userIndex}" name="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.userNm" value="${rescVa}" style="width:70px; ${style}"
				maxByte="200" validator="changeLangSelectorUser('userListArea', id, va)" mandatory="Y" />
			</c:forEach>
			</td>
			<td>
			<c:if test="${fn:length(_langTypCdListByCompId)>1}">
				<select id="langSelector${userIndex}" onchange="changeLangTypCd('userListArea','langTyp${userIndex}', this.value)" <u:elemTitle titleId="cols.langTyp" />>
				<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="langStatus">
				<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
				</c:forEach>
				</select>
			</c:if>
			<u:input type="hidden" id="rescId${userIndex}" name="rescId" value="${orUserBVo.rescId}" />
			</td>
			</tr>
			</table>
		</td>
		<td class="body_ct" style="${not empty orUserBVo.roleNms ? 'font-weight:bold' : ''
			}"><span title="${orUserBVo.roleNms}">${orUserBVo.aduTypNm}</span></td>
		<td align="center"><u:set
			test="${not empty orUserBVo.userUid}" var="readonly" value="Y" elseValue="N"
			/><input type="hidden" name="odurUid" value="${orUserBVo.odurUid}" /><input type="hidden" name="aduTypCd" value="${orUserBVo.aduTypCd}" /><u:input
			id="userUid${userIndex}" name="userUid" value="${orUserBVo.userUid}" titleId="cols.userUid"
			maxByte="30" style="width:82%" readonly="${readonly}" valueOption="upper,number"
			/></td>
		<td class="listicon_ct">
			<select id="genCd${orgIndex}" name="genCd" <u:elemTitle titleId="cols.gen" alt="성별" />><c:forEach
				items="${genCdList}" var="genCdVo" varStatus="genStatus">
				<u:option value="${genCdVo.cd}" title="${genCdVo.rescNm}" checkValue="${orUserBVo.orOdurBVo.genCd}"
				/></c:forEach>
			</select></td>
		<td class="listicon_ct">
			<select id="positCd${orgIndex}" name="positCd" <u:elemTitle termId="or.term.posit" alt="직위" />><c:forEach
				items="${positCdList}" var="positCdVo" varStatus="positStatus">
				<u:option value="${positCdVo.cd}" title="${positCdVo.rescNm}" checkValue="${orUserBVo.positCd}"
				/></c:forEach>
				<u:option value="" title="" selected="${empty orUserBVo.positCd}" />
			</select></td>
		<td class="listicon_ct">
			<select id="titleCd${orgIndex}" name="titleCd" <u:elemTitle termId="or.term.title" alt="직책" />><c:forEach
				items="${titleCdList}" var="titleCdVo" varStatus="titleStatus">
				<u:option value="${titleCdVo.cd}" title="${titleCdVo.rescNm}" checkValue="${orUserBVo.titleCd}"
				/></c:forEach>
				<u:option value="" title="" selected="${empty orUserBVo.titleCd}" />
			</select></td>
		<td class="listicon_ct">
			<select id="userStatCd${orgIndex}" name="userStatCd" <u:elemTitle titleId="or.cols.statCd" alt="상태코드" />><c:if
				test="${empty orUserBVo.aduTypCd or orUserBVo.aduTypCd eq '01'}"><c:forEach
				items="${userStatCdList}" var="userStatCdVo" varStatus="userStatStatus">
				<u:option value="${userStatCdVo.cd}" title="${userStatCdVo.rescNm}" checkValue="${orUserBVo.userStatCd}"
				/></c:forEach></c:if><c:if
				test="${not empty orUserBVo.aduTypCd and orUserBVo.aduTypCd ne '01'}"><c:forEach
				items="${userStatAduCdList}" var="userStatCdVo" varStatus="userStatStatus">
				<u:option value="${userStatCdVo.cd}" title="${userStatCdVo.rescNm}" checkValue="${orUserBVo.userStatCd}"
				/></c:forEach></c:if>
			</select>
			<u:input type="hidden" id="sortOrdr${userIndex}" name="sortOrdr" />
			<u:input type="hidden" id="valid${userIndex}" name="valid" />
		</td>
	</tr>
	</c:forEach>
	
</u:listArea>
<input type="hidden" id="delList" name="delList" />
</form>