<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

<%// 선택삭제 [직접 삭제 submit]%>
function fnDelete(){
	var delArr =[];
	$("#listArea tbody:first input[type='checkbox']:checked").each(function(){
		va = $(this).val();
		if(va!=null && va!=''){
			delArr.push(va);
		}
	});
	
	if(delArr.length > 0){
		$("#delList").val(delArr.join(','));
		
		if(confirmMsg("cm.cfrm.del")) {
			deleteForm.submit();
		}
	}
};

//여러명의 사용자 선택
function openMuiltiUser(mode){
	var $view = $("#listArea"), data = [];<%// data: 팝업 열때 오른쪽에 뿌릴 데이타 %>	
	
	$view.find("input[id='agntAdmUid']").each(function(){
		if($(this).val() != null && $(this).val() != '') data.push({userUid:$(this).val()});
	});
	<%// option : data, multi, titleId %>
	searchUserPop({data:data, multi:true, mode:mode==null ?'search':'view'}, function(arr){
		if(arr!=null){
			var $tr, $hiddenTr = $("#listArea tbody:first #hiddenTr");
			var html = $hiddenTr[0].outerHTML;
			//removeAllCols();
			var vas = getAllVas();
			arr.each(function(index, userVo){
				if(vas==null || !vas.contains(userVo.userUid)){
					$hiddenTr.before(html);
					$tr = $hiddenTr.prev();
					$tr.attr('id','tr'+userVo.userUid);
					$tr.find("input[type='checkbox']").val(userVo.userUid);
					$tr.find("input[name='agntAdmUid']").val(userVo.userUid);
					$tr.find("td a#userNm").text(userVo.rescNm);
					$tr.find("td#deptRescNm").text(userVo.deptRescNm);
					$tr.find("input[type='radio']").each(function(index){
						if(index == 0 ) $(this).attr('checked',true);
						$(this).attr("name","authCd_"+userVo.userUid);
						if(index == 0 ) $(this).attr('id',"authCd_"+userVo.userUid+"R");
						else $(this).attr('id',"authCd_"+userVo.userUid+"RW");
					});
					$tr.find("label").each(function(index){
						if(index == 0 ) $(this).attr('for',"authCd_"+userVo.userUid+"R");
						else $(this).attr('for',"authCd_"+userVo.userUid+"RW");
					});
					$tr.show();
					setJsUniform($tr[0]);
					//alert($tr[0].outerHTML);
				}
			});
			
			//$view.html(buffer.join(''));
			//setUniformCSS($view[0]);
			//var userNms = $view.find("input[id='rescNm']");
			
			//var msg = '<u:msg titleId="wb.jsp.viewBc.apntr" arguments="'+userNms.eq(0).val()+','+(userNms.length-1)+'"/>'
			//$('#apntrUserMsg').html(msg);
			//$('#apntrUserMsg').show();
		}
	});
};

<%// tr 전체제거 %>
function removeAllCols(){
	var arr = getCheckedAllTrs("cm.msg.noSelect");
	if(arr==null) return;
	arr.each(function(index, tr){
		$(tr).remove();
	}, true);
};

<%// [아이콘] 선택제거 %>
function removeCols(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr==null) return;
	
	if(confirmMsg("cm.cfrm.del")) {
		arr.each(function(index, tr){
			$(tr).remove();
		}, true);
		var $form = $('#setAgntAdmForm');
		$form.attr('method','post');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
};

<%//checkbox 가 있는 tr 테그 목록 리턴 %>
function getCheckedAllTrs(noSelectMsg){
	var arr=[], id, obj;
	$("#listArea tbody:first input[type='checkbox']").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		return null;
	}
	return arr;
};

<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(noSelectMsg){
	var arr=[], id, obj;
	$("#listArea tbody:first input[type='checkbox']:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
};

<%//현재 등록된 id 목록 리턴 %>
function getAllVas(){
	var arr=[], va;
	$("#listArea tbody:first input[id='agntAdmUid']").each(function(){
		va = $(this).val();
		if(va!='' && va!=null) arr.push(va);		
	});
	arr.push('${sessionScope.userVo.userUid}');
	if(arr.length==0){
		return null;
	}
	return arr;
}

<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
};

<%// 대리인 저장 %>
function save(){
	<%// 서버 전송%>
	if (true/*confirmMsg("cm.cfrm.save")*/ ) {
		var $form = $('#setAgntAdmForm');
		$form.attr('method','post');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
};

<%// 설정 저장 %>
function saveSetup(){
	if($("#bcRegrUid option:selected").val() == ''){
		alertMsg('cm.msg.noSelect');
		return;
	}
	<%// 서버 전송%>
	if (true/*confirmMsg("cm.cfrm.save")*/ ) {
		var $form = $('#setAgntBcSetupForm');
		$form.attr('method','post');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
};

$(document).ready(function() {
	<%// 행추가 영역 제외하고 uniform 적용%>
	$("#listArea tbody:first").children("[id!='hiddenTr']").each(function(){
		setJsUniform(this);
	});
	setJsUniform($('#listArea2'));
	setJsUniform($('#paginationForm'));
});
//-->
</script>
<u:title titleId="wb.jsp.setAgntAdm.title" alt="대리관리자 설정" menuNameFirst="true" />

<u:tabGroup id="agntTab">
<u:tab id="agntTab" areaId="agntAdmArea" titleId="wb.jsp.setAgntAdm.tab.agntAdm" alt="대리인 지정" on="true" />
<u:tab id="agntTab" areaId="agntBcArea" titleId="wb.jsp.setAgntAdm.tab.agntBc" alt="대리명함 기본값 설정" />
</u:tabGroup>

<% // 대리인 지정 %>
<div id="agntAdmArea">
	<form id="setAgntAdmForm" action="./transAgntAdm.do?menuId=${menuId}">
		<u:listArea id="listArea">
			<tr id="headerTr">
				<td width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td>
				<td class="head_ct"><u:msg titleId="cols.nm" alt="이름" /></td>
				<td width="30%" class="head_ct"><u:msg titleId="cols.dept" alt="부서" /></td>
				<td width="30%" class="head_ct"><u:msg titleId="cols.auth" alt="권한" /></td>
			</tr>
			<c:choose>
				<c:when test="${!empty wbBcAgntAdmBVoList}">
					<c:forEach var="list" items="${wbBcAgntAdmBVoList}" varStatus="status">
						<u:set test="${status.last}" var="trDisp" value="display:none" />
						<u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${list.agntAdmUid}" />
						<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"' id="${trId}" style="${trDisp}">
							<td class="bodybg_ct"><u:checkbox name="listCheck" value="${list.agntAdmUid }" checked="false" /><u:input type="hidden" id="agntAdmUid" name="agntAdmUid" value="${list.agntAdmUid }"/></td>
							<td class="body_ct"><a href="javascript:viewUserPop('${list.agntAdmUid }');" id="userNm">${list.userNm }</a></td>
							<td class="body_ct" id="deptRescNm">${list.deptRescNm }</td>
							<td>
								<u:checkArea style="margin: 0 auto 0 auto;">
								<u:radio id="authCd_${list.agntAdmUid }R" name="authCd_${list.agntAdmUid }" value="R" titleId="wb.option.read" alt="읽기" checkValue="${list.authCd }" checked="${empty list.agntAdmUid || list.authCd eq 'R'}" inputClass="bodybg_lt" />
								<u:radio id="authCd_${list.agntAdmUid }RW" name="authCd_${list.agntAdmUid }" value="RW" titleId="wb.option.readWrite" alt="읽기/쓰기" checkValue="${list.authCd }" inputClass="bodybg_lt" />
								</u:checkArea>
							</td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr>
						<td class="nodata" colspan="4"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
					</tr>
				</c:otherwise>
			</c:choose>
		</u:listArea>
	</form>
	<u:pagination />

	<% // 하단 버튼 %>
	<u:buttonArea>
		<u:button titleId="wb.btn.agntReg" alt="대리인추가" href="javascript:openMuiltiUser();" auth="R" />
		<u:button titleId="wb.btn.agntDel" alt="대리인삭제" href="javascript:removeCols();" auth="R" />
		<u:button titleId="cm.btn.save" alt="저장" href="javascript:;" onclick="save();" auth="R" />
	</u:buttonArea>

</div>

<% // 대리명함 기본값 설정 %>
<div id="agntBcArea" style="display: none;">
	<form id="setAgntBcSetupForm" action="./transAgntSetup.do?menuId=${menuId}">
		<u:listArea id="listArea2">
		<tr>
			<td width="27%" class="head_ct"><u:msg titleId="wb.jsp.setAgntAdm.appointer" alt="기본 지정인" /></td>
			<td width="73%">
				<select id="bcRegrUid" name="bcRegrUid" style="width:150px;" <u:elemTitle titleId="wb.jsp.setAgntAdm.tab.agntBc" alt="대리인기본값설정" /> >
					<u:option value="" titleId="wb.jsp.setAgntAdm.select.agntBc" selected="${empty agntSetupList }"/>
					<c:forEach items="${agntSetupList}" var="agntVo" varStatus="status">
						<u:option value="${agntVo.regrUid}" title="${agntVo.userNm}" checkValue="${wbBcAgntSetupBVo.bcRegrUid }"/>
					</c:forEach>
				</select>
			</td>
		</tr>
		</u:listArea>
	</form>

	<% // 하단 버튼 %>
	<u:buttonArea>
		<u:button titleId="cm.btn.save" alt="저장" href="javascript:;" onclick="saveSetup();" auth="R"/>
	</u:buttonArea>

</div>
<form name="deleteForm" action="./transAgntAdmDel">
	<u:input type="hidden" name="menuId" value="${menuId}" />
	<u:input type="hidden" name="delList"  id="delList"/>
</form>
