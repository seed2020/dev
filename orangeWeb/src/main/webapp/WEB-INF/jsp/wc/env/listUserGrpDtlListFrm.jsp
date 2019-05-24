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
var gMaxRow2 = parseInt('${fn:length(orUserBVoList)}');
<%// 행추가%>
function addRow(){
	var $tr = $("#userListArea tbody:first #hiddenTr");
	var html = $tr[0].outerHTML;
	html = html.replace(/_NO/g,'_'+gMaxRow2);
	gMaxRow2++;
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
function delUserRow(){
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
function changeLangSelectorFld(areaId, id, va){
	if(va==''){
		var langSelector = $('#'+areaId+' #langSelector'+id.substring(id.lastIndexOf('_')));
		var nm = $("#userListArea #"+id).attr("name");
		langSelector.val(nm.substring(nm.length-2));
		langSelector.trigger('click');
	}
}
<%// 저장%>
function save(){
	<%// trArr : 저장할 곳의 tr 테그 배열 %>
	var trArr=[], count=0, result;
	$("#userListArea tbody:first").children().each(function(){
		trArr.push(this);
	});
	
	<%// 어권별 리소스가 있는지 체크함%>
	for(var i=1;i<trArr.length-1;i++){
		result = $(trArr[i]).find("input[name='userUid']").val()!='' ? 1 : 0;
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
	$("#userListArea input[name='sortOrdr']").each(function(){
		if($(this).attr('id')!='sortOrdr_NO'){			
			$(this).val(ordr++);
		}
	});
	<%// disable 된 select disable 해제 %>
	var arr = releaseDisable($('#userGrpDtlListForm select'));
	
	<%// 서버 전송%>
	var $form = $("#userGrpDtlListForm");
	$form.attr('method','post');
	$form.attr('action','./transUserGrpDtlList.do');
	$form.attr('target','dataframeForFrame');
	$form.submit();
	
	<%// select disable 다시 적용 %>
	unreleaseDisable(arr);
}

<%//어권별 리소스 input에 값이 있는지 체크%>
function checkRescVa(trObj, ignoreAllEmpty){
	var i, id, va, handler, arr=[];
	$(trObj).find("input[name^='userUid']").each(function(){ arr.push(this); });
	
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
	$("#userListArea input[type=checkbox]:checked").each(function(){
		va = $(this).val();
		if(va!=null && va!='') { arr.push(va); }
	});
	return arr.length==0 ? null : arr;
}
<%// 저장 후 리로드%>
function afterTrans(userGrpId){
	parent.reloadFrm(userGrpId);
	//parent.openListFrm(userGrpId);
}
<%// 사용자 세팅 %>
function setUserList(arr){
	var $tr, $hiddenTr = $("#userListArea tbody:first #hiddenTr");
	var html = $hiddenTr[0].outerHTML;
	html = html.replace(/_NO/g,'_'+gMaxRow2);
	var vas = getAllVas();
	arr.each(function(index, obj){
		if(vas==null || !vas.contains(obj.userUid)){
			gMaxRow2++;
			$hiddenTr.before(html);
			$tr = $hiddenTr.prev();
			$tr.attr('id','tr'+obj.userUid);
			$tr.find("input[type='checkbox']").val(obj.userUid);
			$tr.find("td#rescNm div.ellipsis").text(obj.rescNm);
			$tr.find("td#deptRescNm div.ellipsis").text(obj.deptRescNm);
			$tr.find("td#positNm div.ellipsis").text(obj.positNm);
			$tr.find("input[name='userUid']").val(obj.userUid);
			$tr.attr('style','');
			//$tr.show();
			setJsUniform($tr[0]);
		}
	});
}
<%//현재 등록된 id 목록 리턴 %>
function getAllVas(){
	var arr=[];
	$('#userListArea input[name="userUid"]').each(function(){
		if($(this).val() != ''){
			arr.push($(this).val());
		}
	});
	if(arr.length==0){
		return null;
	}
	return arr;
};
<%// 사용자 정보 관리 항목 %>
var gAttrs = ["userUid", "rescNm", "deptRescNm", "positNm"];
<%// 선택된 사용자 목록 리턴 - uncheck:리턴할 때 선택을 해제함 %>
function getSelectedUsers(uncheck){
	var arr = [], $me, obj;
	$("#userListArea input[id='grpChk']:checked").each(function(){
		obj = {};
		$me = $(this);
		if(uncheck){
			$(this).checkInput(false);
		}
		gAttrs.each(function(index, attr){
			obj[attr] = $me.attr("data-"+attr);
		});
		arr.push(obj);
	});
	return arr.length==0 ? null : arr;
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

<form id="userGrpDtlListForm" style="padding:10px;">
<input type="hidden" name="menuId" value="${menuId}" /><c:if test="${not empty param.userGrpId}">
<u:input type="hidden" id="userGrpId" value="${param.userGrpId}" /></c:if>
<u:listArea id="userListArea" >

	<tr id="headerTr">
		<th width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('userListArea', this.checked);" value=""/></th>
		<th width="20%" class="head_ct"><u:msg titleId="or.cols.name" alt="성명"/></th>
		<th width="*" class="head_ct"><u:msg titleId="cols.dept" alt="부서"/></th>
		<th width="15%" class="head_ct"><u:term termId="or.term.posit" alt="직위" /></th>
	</tr>
	
	<c:forEach items="${orUserBVoList}" var="orUserBVo" varStatus="status"
			 ><u:set test="${status.last}" var="trDisp" value="display:none"
			/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${orUserBVo.userUid}"
			/><u:set test="${status.last}" var="index" value="_NO" elseValue="_${status.index+1}"
			/>
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="${trId}" style="${trDisp}" >
		<td class="bodybg_ct"><input type="checkbox" id="grpChk" value="${orUserBVo.userUid}" data-userUid="${orUserBVo.userUid }" data-rescNm="${orUserBVo.rescNm }" 
		data-deptRescNm="${orUserBVo.deptRescNm }" data-positNm="${orUserBVo.positNm }"/></td>
		<td class="body_lt" id="rescNm"><div class="ellipsis"><c:if test="${!status.last}"><a href="javascript:parent.viewUserPop('${orUserBVo.userUid}');">${orUserBVo.rescNm }</a></c:if></div>
			<u:input type="hidden" id="sortOrdr${index}" name="sortOrdr" />
			<u:input type="hidden" id="valid${index}" name="valid" />
			<u:input type="hidden" id="userUid${index}" name="userUid" value="${orUserBVo.userUid }"/>
		</td>
		<td class="body_ct" id="deptRescNm"><div class="ellipsis">${orUserBVo.deptRescNm }</div></td>
		<td class="body_ct" id="positNm"><div class="ellipsis">${orUserBVo.positNm }</div></td>
	</tr>
	</c:forEach>
</u:listArea>
<input type="hidden" id="delList" name="delList" />
</form>