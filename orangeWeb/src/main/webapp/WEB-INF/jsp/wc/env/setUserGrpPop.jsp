<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	String callback = (String)request.getAttribute("callback");
	if(callback==null || callback.isEmpty()) callback = "setUserGrpList";
%>
<u:set var="queryParamUserUid" test="${!empty param.paramUserUid}" value="&paramUserUid=${param.paramUserUid }" elseValue=""/>
<script type="text/javascript">
<!--<%// 선택 - 배열에 담긴 목록%>
function selRowInArr(rowArr){
	var selIds = [];
	for(var i=0;i<rowArr.length;i++){
		$userGrpId = $(rowArr[i]).find("input[name='userGrpId']");
		if($userGrpId.val()!=''){
			selIds.push($userGrpId.val());
		}
	}
	if(selIds.length == 0 ) return null;
	return selIds.join(',');
}<%// [순서조절:위로,아래로] 서버에 저장하지 않고 화면상에서만 순서 조정함 %>
function move(direction){
	var i, arr = getCheckedGrpTrs("cm.msg.noSelect");
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
function getCheckedGrpTrs(noSelectMsg){
	var arr=[], id, obj;
	$("#userGrpListArea tbody:first input[type=${param.multi == 'Y' ? 'checkbox' : 'radio' }]:checked").each(function(){
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
var gMaxRow = parseInt('${fn:length(wcUserGrpBVoList)}');
<%// 행추가%>
function addRow(){
	restoreUniform('userGrpListArea');
	var $tr = $("#userGrpListArea tbody:first #hiddenTr");
	var html = $tr[0].outerHTML;
	html = html.replace(/_NO/g,'_'+gMaxRow);
	gMaxRow++;
	$tr.before(html);
	$tr = $tr.prev();
	$tr.attr('id','');
	$tr.attr('style','');
	dialog.resize('setUserGrpDialog');
	applyUniform('userGrpListArea');
}
<%// 행삭제%>
function delRow(){
	var $tr = $("#userGrpListArea tbody:first #hiddenTr").prev();
	delRowInArr([$tr[0]]);
}<%// 선택삭제%>
function delSelRow(){
	var arr = getCheckedGrpTrs("cm.msg.noSelect");
	if(arr!=null) delRowInArr(arr);
}<%// 삭제 - 배열에 담긴 목록%>
function delRowInArr(rowArr){
	var delVa = $("#delList").val(), delArr = [], $userGrpId;
	if(delVa!='') delArr.push(delVa);
	for(var i=0;i<rowArr.length;i++){
		$userGrpId = $(rowArr[i]).find("input[name='userGrpId']");
		if($userGrpId.val()!=''){
			delArr.push($userGrpId.val());
		}
		$(rowArr[i]).remove();
	}
	$("#delList").val(delArr.join(','));
	dialog.resize('setUserGrpDialog');
}<% //- 해당 어권의 명이 입력되지 않았 을 경우 해당 어권을 보이게 함 %>
function changeLangSelectorFld(areaId, id, va){
	if(va==''){
		var langSelector = $('#'+areaId+' #langSelector'+id.substring(id.lastIndexOf('_')));
		var nm = $("#userGrpListArea #"+id).attr("name");
		langSelector.val(nm.substring(nm.length-2));
		langSelector.trigger('click');
	}
}<%// 저장%>
function save(){
	<%// trArr : 저장할 곳의 tr 테그 배열 %>
	var trArr=[], count=0, result;
	$("#userGrpListArea tbody:first").children().each(function(){
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
	$("#userGrpListArea input[name='sortOrdr']").each(function(){
		if($(this).attr('id')!='sortOrdr_NO'){
			$(this).val(ordr++);
		}
	});
	<%// disable 된 select disable 해제 %>
	var arr = releaseDisable($('#userGrpListForm select'));
	
	<%// 서버 전송%>
	var $form = $("#userGrpListForm");
	$form.attr('method','post');
	$form.attr('action','./transUserGrpList.do?menuId=${menuId}');
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
	$("#userGrpListArea input[type=checkbox]:checked").each(function(){
		va = $(this).val();
		if(va!=null && va!='') { arr.push(va); }
	});
	return arr.length==0 ? null : arr;
}<%// [클릭] 사용자그룹 %>
function userGrpClick(obj, id){
	if(obj != null){
		$('#userGrpListArea a.on').attr('class','');
		$(obj).attr('class','on');	
	}
	openListFrm(id);
}<%// [사용자그룹클릭] - 오른쪽 리스트 열기 %>
function openListFrm(id){
	if(id!=null){
		tId = id;
		reloadFrame('openListFrm', './listUserGrpDtlListFrm.do?menuId=${menuId}${queryParamUserUid }&userGrpId='+id);
	}else{
		reloadFrame('openListFrm', '/cm/util/reloadable.do');
	}
}
<%// [소버튼] +추가 %>
function addSelectedUser(){
	var frameId = "openListFrm";
	if(getIframeContent(frameId).location.href.indexOf("reloadable.do")<0){
		var arr = getIframeContent(frameId).getSelectedUsers(true);
		if(arr!=null) getIframeContent("searchUserCollectedFrm").addSelectedUser(arr);
	}
}
<%// [소버튼] -삭제 %>
function delSelectedUser(){
	getIframeContent("searchUserCollectedFrm").delSelectedUser();
}
<%// [버튼] -확인 %>
function setCollectedUsers(){
	var arr = getIframeContent("searchUserCollectedFrm").getAllUsers();
	parent.<%= callback%>(arr);
	dialog.close('setUserGrpDialog');
}
$(document).ready(function() {
	reloadFrame("searchUserCollectedFrm", "/or/user/listSeltdUserFrm.do?lstSetupMetaId=OR_SETUP${optParam}");
});
//-->
</script>
<div style="width:${isSelect eq true ? 700 : 300}px;">
<c:if test="${isSelect eq true }">
<%
	String multi = request.getParameter("multi");
	if(multi != null && "Y".equals(multi)){
		request.setAttribute("outHeight", "240px");
		request.setAttribute("inHeight", "230px");
		request.setAttribute("divHeight", "215px");
		request.setAttribute("frmHeight", "210px");
	} else {
		request.setAttribute("outHeight", "370px");
		request.setAttribute("inHeight", "350px");
		request.setAttribute("divHeight", "345px");
		request.setAttribute("frmHeight", "340px");
	}
%>
<u:tabArea
	outerStyle="height:${outHeight}; overflow-x:hidden; overflow-y:hidden;"
	innerStyle="height:${inHeight}; margin: 0px 10px 0px 10px; padding-top:10px;">

<u:cmt cmt="[조직도]탭 영역" />
<div id="searchUserTreeArea" >

<div style="float:left; width:43.8%;">
<u:titleArea
	outerStyle="height:${divHeight}; overflow-x:hidden; overflow-y:auto;"
	innerStyle="padding:10px;">
	<u:listArea id="userGrpListArea" tbodyClass="textBox">

	<tr id="headerTr">
		<th class="head_ct"><u:msg titleId="cols.grpNm" alt="그룹명" /></th>
	</tr>
	
	<c:forEach items="${wcUserGrpBVoList}" var="wcUserGrpBVo" varStatus="status">
	<tr>
		<td class="body_lt"><div class="ellipsis" title="${wcUserGrpBVo.rescNm }"><a href="javascript:;" onclick="userGrpClick(this, '${wcUserGrpBVo.userGrpId }');" class="${status.first ? 'on' : '' }">${wcUserGrpBVo.rescNm }</a></div></td>
	</tr>
	</c:forEach>
	<c:if test="${empty wcUserGrpBVoList }">
		<tr>
		<td class="nodata" ><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if></u:listArea>
</u:titleArea>
</div>

<div style="float:right; width:55%;">
<u:titleArea frameId=""
	outerStyle="height:${divHeight}; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"><u:set
		test="${!empty wcUserGrpBVoList}" var="userGrpUrl" value="./listUserGrpDtlListFrm.do?menuId=${menuId}&userGrpId=${wcUserGrpBVoList[0].userGrpId }&fncCal=${param.fncCal}${queryParamUserUid }" elseValue="/cm/util/reloadable.do" />
<iframe id="openListFrm" name="searchUserListFrm" src="${userGrpUrl }" style="width:100%; height:${frmHeight};" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:titleArea>
</div>

</div>

<u:cmt cmt="[임직원 검색]탭 영역" />
<div id="searchUserSchArea" <c:if test="${empty param.userNm}">style="display:none"</c:if>>

<iframe id="searchUserSearchFrm" name="searchUserSearchFrm" src="/or/user/searchUserFrm.do?userNm=${userNm}${optParam}${uidParam}${schUserParam}${downParam}${userStatParam}" style="width:100%; height:${inHeight};" frameborder="0" marginheight="0" marginwidth="0"></iframe>

</div>

</u:tabArea>

<c:if test="${param.multi == 'Y'}">

<u:title alt="선택된 리스트" titleId="cm.selectedList" >
	<u:titleButton titleId="cm.btn.add" alt="추가" img="ico_add.png" href="javascript:addSelectedUser()" />
	<u:titleButton titleId="cm.btn.del" alt="삭제" img="ico_minus.png" href="javascript:delSelectedUser()" />
</u:title>

<u:titleArea
	outerStyle="height:${divHeight}; overflow-x:hidden; overflow-y:auto; padding:0px"
	innerStyle="NO_INNER_IDV">

<iframe id="searchUserCollectedFrm" name="searchUserCollectedFrm" src="/cm/util/reloadable.do" style="width:100%; height:${frmHeight};" frameborder="0" marginheight="0" marginwidth="0"></iframe>

</u:titleArea>

</c:if>

<u:buttonArea><c:if
		test="${param.multi!='Y'}">
	<u:button titleId="cm.btn.confirm" onclick="setUsers();" alt="확인" /></c:if><c:if
		test="${param.multi=='Y'}">
	<u:button titleId="cm.btn.confirm" onclick="setCollectedUsers();" alt="확인" /></c:if>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</c:if>
<c:if test="${isSelect eq false }">
<u:title titleId="wc.term.userGroup" type="small" alt="사용자그룹">
	<u:titleIcon type="up" onclick="move('up');" id="rightUp" auth="W" />
	<u:titleIcon type="down" onclick="move('down');" id="rightDown" auth="W" />
</u:title>
<div style="height:410px;overflow-y:auto;">
<form id="userGrpListForm" >
<input type="hidden" name="menuId" value="${menuId}" />
<u:listArea id="userGrpListArea" >

	<tr id="headerTr">
		<th width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('userGrpListArea', this.checked);" value=""/></th>
		<th class="head_ct"><u:mandatory /><a href="javascript:changeLangTypCds('userGrpListForm');" title="<u:msg titleId="pt.jsp.terms.chgLangAll" alt="일괄 언어 변경" />"><u:msg titleId="cols.grpNm" alt="그룹명" /></a></th>
	</tr>
	
	<c:forEach items="${wcUserGrpBVoList}" var="wcUserGrpBVo" varStatus="status"
			 ><u:set test="${status.last}" var="trDisp" value="display:none"
			/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${wcUserGrpBVo.userGrpId}"
			/><u:set test="${status.last}" var="index" value="_NO" elseValue="_${status.index+1}"
			/>
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="${trId}" style="${trDisp}" >
		<td class="bodybg_ct"><input type="checkbox" value="${wcUserGrpBVo.userGrpId}"/>
			<u:input type="hidden" id="sortOrdr${index}" name="sortOrdr" />
			<u:input type="hidden" id="valid${index}" name="valid" />
			<u:input type="hidden" id="userGrpId${index}" name="userGrpId" value="${wcUserGrpBVo.userGrpId }"/></td>
		<td>
			<table cellspacing="0" cellpadding="0" border="0">
			<tr>
			<td id="langTyp${index}">
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="langStatus">
			<u:convert srcId="${wcUserGrpBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
			<u:set test="${langStatus.first}" var="style" value="" elseValue="display:none;" />
			<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
			<u:input id="rescVa_${langTypCdVo.cd}${index}" name="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.grpNm" value="${rescVa}" style="${style}"
				maxByte="200" validator="changeLangSelectorFld('userGrpListArea', id, va)" mandatory="Y" />
			</c:forEach>
			</td>
			<td>
			<c:if test="${fn:length(_langTypCdListByCompId)>1}">
				<select id="langSelector${index}" onchange="changeLangTypCd('userGrpListArea','langTyp${index}', this.value)" <u:elemTitle titleId="cols.langTyp" />>
				<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="langStatus">
				<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
				</c:forEach>
				</select>
			</c:if>
			<u:input type="hidden" id="rescId${index}" name="rescId" value="${wcUserGrpBVo.rescId}" />
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
	<u:button href="javascript:addRow();" titleId="cm.btn.plus" alt="행추가" auth="W" />
	<u:button href="javascript:delRow();" titleId="cm.btn.minus" alt="행삭제" auth="W" />
	<u:button href="javascript:delSelRow();" titleId="cm.btn.selDel" alt="선택삭제" auth="W" />
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:save();" auth="W" />
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</c:if>
</div>