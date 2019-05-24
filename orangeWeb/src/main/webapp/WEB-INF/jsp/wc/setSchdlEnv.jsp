<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
function saveSubmit(){
	//if(validator.validate('setSchdlEnv')){
		var $form = $('#setSchdlEnv');
		$form.attr('method','post');
		$form.attr('action','/wc/setSchdlEnv.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form.submit();
	//}
}

//지정인 선택삭제
function dellApnt(){
var $view = $("#apntUid");
	
	var index = $("#apntSelect option").index($("#apntSelect option:selected"));
	
	$("#apntSelect option:selected").remove();
	
	var userUidIndex=0;
	
	$("#apntUid").find("#apntUserUid").each(function(){
		if(userUidIndex==index){
			$(this).remove();
			return false;
		}
	userUidIndex++;
	});
}
//대리인 선택삭제
function dellAgnt(){

	var index = $("#agntSelect option").index($("#agntSelect option:selected"));
	$("#agntSelect option:selected").remove();
	var userUidIndex=0;

	$("#agntUid").find("#agntUserUid").each(function(){
		if(userUidIndex==index){
			$(this).remove();
			return false;
		}
	userUidIndex++;
	});
}

//지정인 여러명의 지정인  선택
function apntOpenMuiltiUser(mode){
	var $view = $("#apntUid"), data = [];<%// data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	$view.find("#apntUserUid").each(function(){
		data.push({userUid:$(this).val()});
	});		
	//var gAttrs = ["userUid", "orgId", "deptId", "rescId", "rescNm", "deptRescNm", "gradeNm", "titleNm", "positNm", "dutyNm", "mbno", "email", "compPhon", "compFno", "homePhon", "homeFno", "extnEmail"];
	// option : data, multi, titleId
	searchUserPop({data:data, multi:true, mode:mode==null ?'search':'view' , userNm:encodeURIComponent($('#setSchdlEnv input[name="aptr"]').val())}, function(arr){	
		if(arr!=null){
			var buffer = [];
			var userUids=[];
			var apntRescIds=[];
			var apntRescNms=[];
			
			arr.each(function(index, userVo){	
				buffer.push("<option>"+userVo.rescNm+","+userVo.deptRescNm+","+userVo.positNm+","+userVo.dutyNm+","+userVo.titleNm+"</option>");
				userUids.push("<input id='apntUserUid' name='apntUserUid' type='hidden' value='"+userVo.userUid+"' />\n");
				apntRescIds.push("<input id='apntRescId' name='apntRescId' type='hidden' value='"+userVo.rescId+"' />\n");
				apntRescNms.push("<input id='apntRrescNm' name='apntRescNm' type='hidden' value='"+userVo.rescNm+"' />\n");
			});
			
			$("#apntDivRescNm").html(apntRescNms.join(''));
			$("#apntDivRescId").html(apntRescIds.join(''));
			$view.html(userUids.join(''));
			$("#apntSelect").html(buffer.join(''));
			setUniformCSS($view[0]);
		}
	});
}


//대리인 여러명의 대리인 선택
function agntOpenMuiltiUser(mode){
	var $view = $("#agntUid"), data = [];<%// data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	
	$view.find("#agntUserUid").each(function(){
		data.push({userUid:$(this).val()});
	});
	//var gAttrs = ["userUid", "orgId", "deptId", "rescId", "rescNm", "deptRescNm", "gradeNm", "titleNm", "positNm", "dutyNm", "mbno", "email", "compPhon", "compFno", "homePhon", "homeFno", "extnEmail"];
	// option : data, multi, titleId
	searchUserPop({data:data, multi:true, mode:mode==null ?'search':'view' , userNm:encodeURIComponent($('#setSchdlEnv input[name="agnt"]').val())}, function(arr){
		
		if(arr!=null){
			var buffer = [];
			var userUids=[];
			var agntRescIds=[];
			var agntRescNms=[];
			
			arr.each(function(index, userVo){	
				buffer.push("<option>"+userVo.rescNm+","+userVo.deptRescNm+","+userVo.positNm+","+userVo.dutyNm+","+userVo.titleNm+"</option>");
				userUids.push("<input id='agntUserUid' name='agntUserUid' type='hidden' value='"+userVo.userUid+"' />\n");
				agntRescIds.push("<input id='agntRescId' name='agntRescId' type='hidden' value='"+userVo.rescId+"' />\n");
				agntRescNms.push("<input id='agntRescNm' name='agntRescNm' type='hidden' value='"+userVo.rescNm+"' />\n");
			});
			
			$("#agntDivRescNm").html(agntRescNms.join(''));
			setUniformCSS($view[0]);
			$("#agntDivRescId").html(agntRescIds.join(''));
			setUniformCSS($view[0]);
			$view.html(userUids.join(''));
			setUniformCSS($view[0]);
			$("#agntSelect").html(buffer.join(''));
			setUniformCSS($view[0]);

		}
	});
}
<%// 선택된 탭 %>
var tId = null;
<% // 탭 클릭 - 목록:L/폴더:F/분류:C %>
function toggleTabBtn(lstTyp, id){
	if(lstTyp=='userGrp'){		
		reloadFrm(null);
	}
}<%// [버튼] 사용자그룹관리 %>
function setUserGrpPop(){
	var url = './setUserGrpPop.do?menuId=${menuId}&multi=Y';
	dialog.open('setUserGrpDialog', '<u:msg titleId="wc.term.userGroup" alt="사용자그룹" />', url);
}<%// [리로드] - 프레임 %>
function reloadFrm(id, popCloseId){
	var url = './listUserGrpFrm.do?menuId=${menuId}';
	if(id!=null) url+='&userGrpId='+id;
	reloadFrame('userGrpFrm', url);
	if(popCloseId != undefined) dialog.close(popCloseId);
}<%// [사용자그룹클릭] - 오른쪽 리스트 열기 %>
function openListFrm(id){
	if(id!=null){
		tId = id;
		reloadFrame('openListFrm', './listUserGrpDtlListFrm.do?menuId=${menuId}&userGrpId='+id);
	}else{
		reloadFrame('openListFrm', '/cm/util/reloadable.do');
	}
};<%// [사용자그룹] - 사용자 팝업 열기 %>
function openSetUserPop(mode){
	if(tId==null){
		alertMsg("dm.jsp.setDoc.not.fld");<%//dm.jsp.setDoc.not.fld='폴더'를 선택 후 사용해 주십시요.%>
		return;
	}
	var data = [];<%// data: 팝업 열때 오른쪽에 뿌릴 데이타 %>	
	
	<%// option : data, multi, titleId %>
	searchUserPop({data:data, multi:true, mode:mode==null ?'search':'view'}, function(arr){
		if(arr!=null){
			getIframeContent('openListFrm').setUserList(arr);
		}
	});
};
<%// 선택삭제%>
function delUserRow(){
	getIframeContent('openListFrm').delUserRow();
}
<%// 사용자추가%>
function saveUserList(){
	getIframeContent('openListFrm').save();
}
<%// 선택된 조직 %>
var gFldId = null;
<%// [아이콘] 위로이동 %>
function moveUp(){
	moveDirection('up');
}
<%// [아이콘] 아래로이동 %>
function moveDown(){
	moveDirection('down');
}
<%// [아이콘] 위/아래로 이동 %>
function moveDirection(direction){
	if(tId==null){
		alertMsg("dm.jsp.setDoc.not.fld");<%//dm.jsp.setDoc.not.fld='폴더'를 선택 후 사용해 주십시요.%>
	} else {
		var contWin = getIframeContent('openListFrm');
		contWin.move(direction);
	}
}
$(document).ready(function() {
	setUniformCSS();

});
//-->
</script>

<u:title titleId="wc.jsp.setSchdlEnv.title" alt="환경설정" menuNameFirst="true"/>
<u:tabGroup id="envTab" noBottomBlank="true">
	<u:tab id="envTab" areaId="envTab1" termId="wc.term.agnt" on="true" onclick="toggleTabBtn('apnt', null);"/>
	<u:tab id="envTab" areaId="envTab2" termId="wc.term.userGroup" on="false" onclick="toggleTabBtn('userGrp', null);"/>
</u:tabGroup>

<u:tabArea id="envTab1" outerStyle="" innerStyle="margin: 0px 10px 0px 10px; padding-top:10px;" noBottomBlank="true">
<input id="agntUserUid" type="hidden" />
<input id="apntUserUid" type="hidden" />
<input id="agntRescId" type="hidden" />
<input id="apntRescNm" type="hidden" />
<input id="apntRescId" type="hidden" />
<input id="apntRescNm" type="hidden" />
	

<input id="returnAgntVo" type="hidden"/>
<form id="setSchdlEnv">
<u:input type="hidden" id="menuId" value="${menuId}" />

<u:listArea>
	<tr>
	<td width="15%" rowspan="2" class="head_lt"><u:msg titleId="cols.agnt" alt="대리인" /></td>
	<td width="85%"><u:input name="agnt" value="" titleId="cols.agnt" style="width: 350px;" /><u:buttonS titleId="cm.btn.agntApnt" alt="대리인지정" onclick="agntOpenMuiltiUser();" /><u:buttonS titleId="wc.btn.agntDel" alt="대리인삭제" onclick="dellAgnt();" /></td>
	</tr>
	
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
		
		<tr>
		<td>
			
			<div id="agntUid">
				<c:forEach var="agntResponse" items="${returnAgntLst}" varStatus="status">
					<input id='agntUserUid' type="hidden" name='agntUserUid' value="${agntResponse.agntUid}"/>
					<input id='agntRescId'  type="hidden" name='agntRescId'  value="${agntResponse.userUid}"/>
				</c:forEach>
			</div>
			<div id="apntUid">
				<c:forEach var="apntResponse" items="${returnApntLst}" varStatus="status">
					<input id='apntUserUid' type="hidden" name='apntUserUid' value="${apntResponse.apntrUid}"/>
					<input id='apntRescId' type="hidden" name='apntRescId'  value="${apntResponse.userUid}"/>
				</c:forEach>
			</div>
			<div id="agntDivRescId"></div>
				
			<div id="agntDivRescNm"></div>
			<div id="apntDivRescId"></div>
			<div id="apntDivRescNm"></div>
			<select name="agntSelect" id="agntSelect" size="5" style="width: 541px;">
				<c:forEach var="agntResponse" items="${returnAgntLst}" varStatus="status">
					<option id ="${agntResponse.userUid}" value="${agntResponse.agntUid}">${agntResponse.rescNm},${agntResponse.deptRescNm},${agntResponse.positNm},${agntResponse.dutyNm},${agntResponse.titleNm} </option>
				</c:forEach>				
			
			</select>

			</td>
		</tr>
		</tbody></table></td>
	</tr>
	
	<tr>
	<td rowspan="2" class="head_lt"><u:msg titleId="cols.apntr" alt="지정인" /></td>
	<td><u:input name="aptr" value="" titleId="cols.apntr" style="width: 350px;" /><u:buttonS titleId="wc.btn.apntrApnt" alt="지정인지정" onclick="apntOpenMuiltiUser();" /><u:buttonS titleId="wc.btn.apntrDel" alt="지정인삭제" onclick="dellApnt();" /></td>
	</tr>

	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><select name="apntSelect" id="apntSelect" size="5" style="width: 541px;">
				<c:forEach var="apntResponse" items="${returnApntLst}" varStatus="status">
					<option id ="${apntResponse.userUid}" value="${apntResponse.apntrUid}">${apntResponse.rescNm},${apntResponse.deptRescNm},${apntResponse.positNm},${apntResponse.dutyNm},${apntResponse.titleNm} </option>
				</c:forEach>
			</select></td>
		</tr>
		</tbody></table></td>
	</tr>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" onclick="saveSubmit();" auth="R" />
</u:buttonArea>

</form>

<u:blank />

<div class="headbox">
<dl>
<dd class="headbox_tit"><u:msg titleId="wc.jsp.setSchdlEnv.help1" alt="대리인 설정이란?" /></dd>
<dd class="headbox_body">- <u:msg titleId="wc.jsp.setSchdlEnv.tx11" alt="" /></dd>
<dd class="headbox_body">- <u:msg titleId="wc.jsp.setSchdlEnv.tx12" alt="" /></dd>
<dd class="headbox_body">- <u:msg titleId="wc.jsp.setSchdlEnv.tx13" alt="" /></dd>
</dl>
</div>

<u:blank />

<div class="headbox">
<dl>
<dd class="headbox_tit"><u:msg titleId="wc.jsp.setSchdlEnv.help2" alt="지정인 설정이란?" /></dd>
<dd class="headbox_body">- <u:msg titleId="wc.jsp.setSchdlEnv.tx21" alt="" /></dd>
<dd class="headbox_body">- <u:msg titleId="wc.jsp.setSchdlEnv.tx22" alt="" /></dd>
<dd class="headbox_body">- <u:msg titleId="wc.jsp.setSchdlEnv.tx23" alt="" /></dd>
<dd class="headbox_body">- <u:msg titleId="wc.jsp.setSchdlEnv.tx24" alt="" /></dd>
<dd class="headbox_body">- <u:msg titleId="wc.jsp.setSchdlEnv.tx25" alt="" /></dd>
</dl>
</div>
</u:tabArea>

<u:tabArea id="envTab2" style="display:none" outerStyle="" innerStyle="margin: 0px 10px 0px 10px; padding-top:10px;" noBottomBlank="false">
<!-- LEFT -->
<div style="float:left; width:46.8%;">
<u:title titleId="wc.term.userGroup" type="small" alt="사용자그룹" />
<u:titleArea frameId="userGrpFrm" frameSrc="/cm/util/reloadable.do"
	outerStyle="height:450px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:440px;" />
	
	<u:buttonArea>
		<u:button href="javascript:setUserGrpPop();" id="btnAddRow" titleId="cols.mng" alt="관리" auth="W" />
	</u:buttonArea>

</div>

	
<!-- RIGHT -->
<div style="float:right; width:51%;">
<u:title titleId="cols.user" type="small" alt="사용자" >
	<u:titleIcon type="up" onclick="moveUp()" id="rightUp" auth="W" />
	<u:titleIcon type="down" onclick="moveDown()" id="rightDown" auth="W" />
</u:title>
<u:titleArea frameId="openListFrm" frameSrc="/cm/util/reloadable.do"
	outerStyle="height:450px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:440px;" />
	
	<u:buttonArea>
		<u:button href="javascript:openSetUserPop();" id="btnAddRow" titleId="cm.btn.add" alt="추가" auth="W" />
		<u:button href="javascript:delUserRow();" id="btnDelSel" titleId="cm.btn.selDel" alt="선택삭제" auth="W" />
		<u:button href="javascript:saveUserList();" id="btnSave" titleId="cm.btn.save" alt="저장" auth="W" />
	</u:buttonArea>
	
</div>

<u:blank />
</u:tabArea>