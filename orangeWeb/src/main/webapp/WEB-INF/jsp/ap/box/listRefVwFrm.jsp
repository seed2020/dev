<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
		import="java.util.ArrayList"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	
	// 담당 - 접수문서 담당의 경우
	if("makVw".equals(request.getParameter("makrRoleCd"))){
		request.setAttribute("makRole", "makVw");
		request.setAttribute("byOneRole", "byOne");
		
	// 합의부서 기안의 경우
	} else if(request.getParameter("apvLnPno") != null && !"0".equals(request.getParameter("apvLnPno"))){
		request.setAttribute("makRole", "makAgr");
		request.setAttribute("byOneRole", "byOneAgr");
		
	// 일반 기안의 경우
	} else {
		request.setAttribute("makRole", "mak");
		request.setAttribute("byOneRole", "byOne");
	}

%>
<script type="text/javascript">
<!--<%
// 사용자 정보 관리 항목 %>
var gAttrs = ["refVwrUid","refVwrNm","refVwStatCd","refVwStatNm","refVwrPositNm","refVwrTitleNm","refVwrDeptId","refVwrDeptNm","refVwDt","refVwFixdApvrYn","hasOpin"];<%
// 사용자 추가 %>
function addSelected(arr, opt){
	var addCnt=0, msgs=[], msg;
	arr.each(function(index, obj){
		data = {};
		data["refVwrUid"] = obj.userUid;<%// 사용자UID%>
		data["refVwrNm"] = obj.rescNm;<%// 사용자명%>
		data["refVwrPositNm"] = obj.positNm==null ? "" : obj.positNm;<%// 직위%>
		data["refVwrTitleNm"] = obj.titleNm==null ? "" : obj.titleNm;<%// 직책%>
		data["refVwrDeptId"] = obj.deptId;<%//부서ID%>
		data["refVwrDeptNm"] = obj.deptRescNm==null ? "" : obj.deptRescNm;<%//부서명%>
		data["refVwStatCd"] = "";
		data["refVwStatNm"] = "";
		data["refVwDt"] = "";
		data["hasOpin"] = "";
		data["refVwFixdApvrYn"] = "";
		if((msg = checkAddValidation(data)) == null){
			addNewTr(data, true);
			addCnt++;
		} else {
			if(!msgs.contains(msg)) msgs.push(msg);
		}
	});
	if(msgs.length>0){
		alert(msgs.join('\n'));
	}
	return addCnt>0;
}<%
// 경로그룹 - 선택한 것 추가 %>
function addRefVwGrp(arr){
	if(arr==null) return;
	
	var addCnt=0, msgs=[], msg;
	arr.each(function(index, obj){
		data = obj;
		if((msg = checkAddValidation(data)) == null){
			addNewTr(data, true);
			addCnt++;
		} else {
			if(!msgs.contains(msg)) msgs.push(msg);
		}
	});
	if(msgs.length>0){
		alert(msgs.join('\n'));
	}
	return addCnt>0;
}<%
// 추가용 - 신규 TR 생성 %>
function addNewTr(data, applyUniform){
	var param = new ParamMap(data);
	var $tr = addEmptyTr();
	$check = $tr.find("input[type='checkbox']");
	$check.val(data["apvrUid"]);
	param.each(function(key, va){<%// checkbox 의 추가 attribute 에 데이터 저장 %>
		$check.attr("data-"+key, va);
	});
	if(param.get("refVwStatCd")=='cmplRefVw' || param.get("refVwFixdApvrYn")=='Y'){
		$check[0].disabled = true;
	} else {
		$check.bind("keydown", function(event){
			if(event.which==13) $(this).trigger("click");
		});
	}
	if(applyUniform!=false) $check.uniform();<%
	
	// 화면 각 TD에 표시%>
	$tds = $tr.children();
	$($tds[1]).text(data.refVwrDeptNm);<%// 부서명%>
	if(data.refVwrUid!=""){
		$($tds[2]).find("a").attr("href", "javascript:parent.viewUserPop('"+data.refVwrUid+"');").text(data.refVwrNm);<%// 사용자명%>
		$($tds[3]).text(data.refVwrPositNm);<%// 직위명%>
		$($tds[4]).text(data.refVwrTitleNm);<%// 직책명%>
	} else {
		$($tds[2]).find("a").text("");<%// 사용자명%>
		$($tds[3]).text("");<%// 직위명%>
		$($tds[4]).text("");<%// 직책명%>
	}
	$tr.show();
	return true;
}<%
// 결재자/결재부서를 추가할 때 추가 할 수 있는지 체크 %>
function checkAddValidation(data, opt){
	var $inputs = $("input[type='checkbox'][id!='checkHeader']:visible");
	var $parent = $inputs.parent();
	var $user = $parent.find("input[data-refVwrUid='"+data.refVwrUid+"']");
	if($user.length>0){<%
		//ap.apvLn.alrdyUser=이미 사용자가 결재라인에 지정되어 있습니다.%>
		return callMsg('ap.apvLn.alrdyUser')+" : "+data.refVwrNm;
	}
	return null;
}<%
// 추가용 - 신규 빈 TR HTML 생성 %>
function addEmptyTr(){
	var $tr = $("#hiddenTr:last");
	var html = $tr[0].outerHTML;
	$(html).insertBefore($tr);
	$tr = $tr.prev();
	$tr.attr("id", "");
	return $tr;
}<%
// 삭제 %>
function delSelected(){
	var arr = [], tr;
	$("input:checked").each(function(){
		if($(this).is("[data-refVwFixdApvrYn!='Y']:visible") == true){
			tr = getParentTag(this, 'tr');
			if($(tr).attr('id')!='hiddenTr' && $(tr).attr('id')!='titleTr'){
				arr.push(tr);
			}
		}
	});
	if(arr.length==0){
		//alertMsg("cm.msg.noSelectedItem",["#ap.cols.item"]);<%//선택된 항목이 없습니다.%>
	} else {
		arr.each(function(index, obj){
			$(obj).remove();
		});
	}
}<%
// 상하이동 %>
function moveLine(direction){
	var i, arr = [];<%
	// 체크된 목록 모으기 %>
	$("input[type='checkbox'][id!='checkHeader'][data-fixed!='Y']:checked:visible").each(function(){
		arr.push(getParentTag(this,'tr'));
	});
	if(arr.length==0) return;
	
	var $node, $prev, $next, $std;
	if(direction=='up'){
		$std = $('#titleTr');
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
}<%
// 선택 해제 - 조직도클릭 or 사용자 클릭하면 하단의 사용자/부서 선택된것 선택 해제함 - parent에서 호출 %>
function deselectAll(){
	$("input:checked").each(function(){
		$(this).checkInput(false);
	});
}<%
// 선택된 사용자 목록 리턴 - uncheck:리턴할 때 선택을 해제함 %>
function getSelected(uncheck){
	var arr = [], $me, obj;
	$("#listApvLnForm input[name='apvLnCheck']:checked").each(function(){
		obj = {};
		$me = $(this);
		if(uncheck){ $me.checkInput(false); }
		gAttrs.each(function(index, attr){
			obj[attr] = $me.attr("data-"+attr);
		});
		arr.push(obj);
	});
	return arr.length==0 ? null : arr;
}<%
// [확인 - 버튼] - 결재라인 체크 %>
function validateConfirm($inputs){
	var uids=[], uid, msgs=[], msg;
	$inputs.each(function(){
		uid = $(this).attr('data-refVwrUid');
		if(!uids.contains(uid)){
			uids.push(uid);
		} else {<%
			//ap.apvLn.alrdyAsind=이미 사용자가 지정되어 있습니다.%>
			msg = callMsg('ap.apvLn.alrdyAsind') + " : " + $(input).attr('data-refVwrNm');
			if(!msgs.contains(msg)) msgs.push(msg);
		}
	});
	if(msgs.length>0){
		alert(msgs.join('\n\n'));
		return false;
	}
	return true;
}<%
// [확인 - 버튼] - parent 에서 호출 %>
function getConfirmList(noValidate){
	var $inputs = $("input[type='checkbox'][id!='checkHeader']:visible");
	if(noValidate!=true && !validateConfirm($inputs)) return null;
	var apvLns = [], data, $me, va;
	$inputs.each(function(){
		data = {};
		$me = $(this);<%
		// input의 데이터를 data 에 담음 %>
		gAttrs.each(function(index, key){
			va = $me.attr("data-"+key);
			if(va != null) data[key] = va;
		});
		apvLns.push(data);
	});
	return apvLns;
}<%

// onload %>
$(document).ready(function() {
	var modified = '${param.modified}';
	if(modified == 'Y'){
		var arr = parent.getRefVwList();
		if(arr!=null){
			for(var i=0;i<arr.length;i++){
				addNewTr(arr[i], false);
			}
		}
	}
	$("input[type='checkbox']:visible").uniform();
});
//-->
</script>
<form id="listApvLnForm" style="padding:8px 10px 10px 10px;">

<u:listArea noBottomBlank="true">
	<tr id="titleTr"><td width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all'
		/>" onclick="checkAllCheckbox('listApvLnForm', this.checked);" value=""/></td>
		<td width="25%" class="head_ct"><u:msg titleId="cols.dept" alt="부서" /></td>
		<td width="22%" class="head_ct"><u:msg titleId="cols.user" alt="사용자" /></td>
		<td width="25%" class="head_ct"><u:term termId="or.term.posit" alt="직위" /></td>
		<td width="25%" class="head_ct"><u:term termId="or.term.title" alt="직책" /></td>
	</tr>
<c:forEach items="${apOngdRefVwDVoList}" var="apOngdRefVwDVo" varStatus="status">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'<c:if
		test="${status.last}"> style="display:none;" id="hiddenTr"</c:if>>
		<td class="bodybg_ct"><input type="checkbox" name="apvLnCheck"<c:if
				test="${status.last}"> class="skipThese"</c:if><c:if
				test="${apOngdRefVwDVo.refVwStatCd == 'cmplRefVw' or apOngdRefVwDVo.refVwFixdApvrYn eq 'Y'}"> disabled="disabled"</c:if>
			value="${apOngdRefVwDVo.refVwrUid}"
			data-refVwrUid="${apOngdRefVwDVo.refVwrUid}"
			data-refVwrNm="<u:out value="${apOngdRefVwDVo.refVwrNm}" type="value" />"
			data-refVwStatCd="${apOngdRefVwDVo.refVwStatCd}"
			data-refVwStatNm="<u:out value="${apOngdRefVwDVo.refVwStatNm}" type="value" />"
			data-refVwrPositNm="<u:out value="${apOngdRefVwDVo.refVwrPositNm}" type="value" />"
			data-refVwrTitleNm="<u:out value="${apOngdRefVwDVo.refVwrTitleNm}" type="value" />"
			data-refVwrDeptId="${apOngdRefVwDVo.refVwrDeptId}"
			data-refVwrDeptNm="<u:out value="${apOngdRefVwDVo.refVwrDeptNm}" type="value" />"
			data-refVwDt="${apOngdRefVwDVo.refVwDt}"
			data-refVwFixdApvrYn="${apOngdRefVwDVo.refVwFixdApvrYn}"
			/></td>
		<td class="body_ct"><u:out value="${apOngdRefVwDVo.refVwrDeptNm}" /></td>
		<td class="body_ct"><c:if
			test="${not empty apOngdRefVwDVo.refVwrUid}"
				><a href="javascript:parent.viewUserPop('${apOngdRefVwDVo.refVwrUid}');"><u:out value="${apOngdRefVwDVo.refVwrNm}" /></a></c:if><c:if
			test="${empty apOngdRefVwDVo.refVwrUid}" ><c:if
				test="${not empty apOngdRefVwDVo.refVwrNm}"
					><u:out value="${apOngdRefVwDVo.refVwrNm}" /></c:if><c:if
				test="${empty apOngdRefVwDVo.refVwrNm}"
					><a></a></c:if></c:if></td>
		<td class="body_ct"><u:out value="${apOngdRefVwDVo.refVwrPositNm}" /></td>
		<td class="body_ct"><u:out value="${apOngdRefVwDVo.refVwrTitleNm}" /></td>
	</tr>
</c:forEach>
</u:listArea>
</form>