<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ page import="java.util.List,java.util.ArrayList,java.util.Map,java.util.HashMap"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%//현재 등록된 id 목록 리턴 %>
function getChkIds(){
	var arr=[];
	$('#lginPopUserList input[type="checkbox"]').each(function(){
		if($(this).attr("data-userUid")!=undefined && $(this).attr("data-userUid")!=''){
			arr.push($(this).attr("data-userUid"));
		}
	});
	if(arr.length==0){
		return null;
	}
	return arr;
};

<%//사용자 추가 %>
function addUsers(){
	var data = [];
	var vas = getChkIds();
	if(vas!=null){
		$.each(vas, function(index, userUid){
			data.push({userUid:userUid});
		});	
	}	
	searchUserPop({data:data, multi:true, mode:'search'}, function(arr){		
		if(arr!=null){
			var $tr, $hiddenTr = $("#lginPopUserList tbody:first #hiddenTr");
			var html = $hiddenTr[0].outerHTML;
			arr.each(function(index, vo){
				if(vas==null || !vas.contains(vo.userUid)){
					$hiddenTr.before(html);
					$tr = $hiddenTr.prev();
					$tr.attr('id','user');
					$tr.find("input[type='checkbox']").attr("data-userUid", vo.userUid);
					$tr.find("input[name='userUid']").val(vo.userUid);
					$tr.find("td#rescNm").text(vo.rescNm);
					$tr.find("td#deptRescNm").text(vo.deptRescNm);
					$tr.find("td#positNm").text(vo.positNm);
					$tr.find("td#titleNm").text(vo.titleNm);
					$tr.show();
					setJsUniform($tr[0]);
				}
			});
		}
		
	});
};
<%// 선택삭제%>
function delLginUserRow(){
	var arr = getCheckedUserTrs("cm.msg.noSelect");
	if(arr!=null) {
		delLginUserRowInArr(arr);
	}
}
<%// 삭제 - 배열에 담긴 목록%>
function delLginUserRowInArr(rowArr){
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
<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedUserTrs(noSelectMsg){
	var arr=[], id, obj;
	$("#lginPopUserList tbody:first input[type='checkbox']:checked").each(function(){
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

<%// 저장%>
function saveLginPopUser(){
	// 사용자 추가여부
	//var validCnt=$("#lginPopUserList tbody:first tr").not('#headerTr, #hiddenTr').length;
	//if(validCnt==0){
	//	alertMsg("cm.msg.nodata.toSave");<%//cm.msg.nodata.toSave=저장할 데이터가 없습니다.%>
	//	return;
	//}
	
	<%// 서버 전송%>
	var $form = $("#lginPopUserForm");
	$form.attr('method','post');
	$form.attr('action','./transLginPopUser.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form.submit();
}

<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
};
$(document).ready(function() {
	$("#lginPopUserList tbody:first").children().each(function(){
		<%// 행추가 영역 제외하고 uniform 적용%>
		if($(this).attr('id')!='hiddenTr'){
			setJsUniform(this);
		}
	});
});
//-->
</script>
<div style="width:400px;">
<div style="height:310px;overflow-y:auto;">
<div class="front notPrint"><div class="front_right">
<table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr><td class="frontbtn"><u:buttonS alt="추가" titleId="cm.btn.add" href="javascript:addUsers();" 
		/></td><td class="frontbtn"><u:buttonS alt="선택삭제" titleId="cm.btn.selDel" href="javascript:delLginUserRow();" 
		/></td></tr></tbody>
</table>		
</div>
</div>
<form id="lginPopUserForm" >
<input type="hidden" name="menuId" value="${menuId}" />
<u:listArea id="lginPopUserList" colgroup="3%,42%,25%,15%,15%">

	<tr id="headerTr">
		<th class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('lginPopUserList', this.checked);" value=""/></th>
		<td width="25%" class="head_ct"><u:msg titleId="cols.dept" alt="부서" /></td>
		<td width="15%" class="head_ct"><u:msg titleId="cols.user" alt="사용자" /></td>
		<td width="15%" class="head_ct"><u:term termId="or.term.posit" alt="직위" /></td>
		<td width="15%" class="head_ct"><u:term termId="or.term.title" alt="직책" /></td>
	</tr>
	
	<c:forEach items="${orUserBVoList}" var="userVo" varStatus="status"
			 ><u:set test="${status.last}" var="trDisp" value="display:none"
			/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="user"
			/><u:set test="${status.last}" var="userIndex" value="_NO" elseValue="_${status.index+1}"
			/>
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="${trId}" style="${trDisp}" >
		<td class="bodybg_ct"><input type="checkbox" data-userUid="${userVo.userUid}"/>
			<u:input type="hidden" id="userUid${userIndex}" name="userUid"  value="${userVo.userUid}"
			/></td>
		<td class="bodybg_ct" id="deptRescNm">${userVo.deptRescNm }</td>
		<td class="bodybg_ct" id="rescNm">${userVo.rescNm }</td>
		<td class="bodybg_ct" id="positNm">${userVo.positNm }</td>
		<td class="bodybg_ct" id="titleNm">${userVo.titleNm }</td>
	</tr>
	</c:forEach>
	
</u:listArea>
<input type="hidden" id="delList" name="delList" />
</form>
</div><% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:saveLginPopUser();" auth="W" />
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</div>
