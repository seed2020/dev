<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%// 테이블헤더의 제목 클릭 - 전체선택%>
function checkAll(cd,obj){
	var checked = $(obj).attr('data-checked') == 'N' ? true : false;
	$(obj).attr('data-checked',checked ? 'Y' : 'N');
	$chks = $("#setForm td."+cd+"Chk input[type='checkbox']");
	$chks.each(function(){
		$(this).checkInput(checked);
		//useChk(this, null);
		//this.checked = checked;
	});
	$chks.uniform.update();
}<%// 전체삭제%>
function delAllRow(){
	$('#reprtUserArea tbody:first tr').not('#headerTr, #hiddenTr, #nodata').each(function(){
		$(this).remove();
	});
	$('#reprtUserArea tr#nodata').show();
	
}
<%// 선택삭제%>
function delSelRow(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null){
		delRowInArr(arr);
		if($('#reprtUserArea tbody:first tr').not('#headerTr, #hiddenTr, #nodata').length==0) $('tr#nodata').show();
	}
}
<%// 삭제 - 배열에 담긴 목록%>
function delRowInArr(rowArr){
	var delArr = [], $userUid;
	for(var i=0;i<rowArr.length;i++){
		$userUid = $(rowArr[i]).find("input[name='userUid']");
		if($userUid.val()!=''){
			delArr.push($userUid.val());
		}
		$(rowArr[i]).remove();
	}
}
<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(noSelectMsg){
	var arr=[], id, obj;
	$("#reprtUserArea tbody:first input[id='userCheck']:checked").each(function(){
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
<%//현재 등록된 id 목록 리턴 %>
function getAllVas(){
	var arr=[];
	$('#reprtUserArea tbody:first').not('#headerTr, #hiddenTr, #nodata').find('input[id="userUid"]').each(function(){
		if($(this).val()!=''){
			arr.push($(this).val());
		}
	});
	if(arr.length==0){
		return null;
	}
	return arr;
};<% // [팝업] - 보고자선택 %>
function reprtUserSelect(){
	var vas = getAllVas();
	
	var data = [];<% // data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	if(vas!=null){
		$.each(vas, function(){
			data.push({userUid:this});
		});	
	}
	
	var $tr, $hiddenTr = $("#reprtUserArea tbody:first #hiddenTr");
	var html = $hiddenTr[0].outerHTML;
	var addCnt=0;
	<% // option : data, multi, titleId %>
	searchUserPop({data:data, multi:true, mode:'search'}, function(arr){
		if(arr!=null){
			arr.each(function(index, userVo){
				if(vas==null || !vas.contains(userVo.userUid)){
					$hiddenTr.before(html);
					$tr = $hiddenTr.prev();
					$tr.attr('id','tr'+userVo.userUid);
					$tr.find("#userChk input[type='checkbox']").val(userVo.userUid);
					$tr.find("#userChk input[name='userUid']").val(userVo.userUid);
					$tr.find("td#userNm").text(userVo.rescNm);
					$tr.find("td").attr('data-userUid', userVo.userUid);
					$tr.find("input[name^='typCd']").attr('checked', true);
					
					$tr.show();
					setEvt($tr);
					setJsUniform($tr[0]);
					addCnt++;
				}
			});
			if(addCnt>0) $('#reprtUserArea tr#nodata').hide();
		}
	});
}<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
}<% // [팝업:저장] 테이블 저장 %>
function save() {
	if (validator.validate('setForm')) {
		var typCd;
		// 일지종류 합치기
		$('#reprtUserArea tbody:first tr').not('#headerTr, #hiddenTr, #nodata').each(function(){
			typCd='';
			$(this).find('input[name^="typCd"]:checked').each(function(index){
				if(index>0) typCd+='/';
				typCd+=$(this).val();
			});
			$(this).find("input[name='typCd']").remove();
			$(this).appendHidden({name:'typCd',value:typCd});
		});
		var $form = $('#setForm');
		$form.attr('method','post');
		$form.attr('action','./transReprtUser.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
};<% // [클릭] 사용자 정보 팝업 %>
function setEvt(tgt){
	if(tgt===undefined) tgt=$('#reprtUserArea tbody:first tr').not('#headerTr, #hiddenTr, #nodata');
	tgt.on('click', 'td#userNm', function(){
		parent.viewUserPop($(this).attr('data-userUid'));
	});
}
$(document).ready(function() {
	setEvt();
});
//-->
</script>
<div style="width:400px">
<form id="setForm">
<u:input type="hidden" id="menuId" value="${menuId }"/>
<u:input type="hidden" id="dialog" value="setReprtUserDialog"/>

<u:listArea id="reprtUserArea" style="height:300px;overflow-y:auto;">	
	<tr id="headerTr">
		<th class="head_ct"><u:msg titleId="cm.select.actname" alt="선택"/></th>
		<th class="head_ct"><u:msg titleId="cols.user" alt="사용자"/></th>
		<c:forEach var="tab" items="${tabList }" varStatus="status">
			<u:msg var="tabTitle" titleId="wl.cols.typCd.${tab }" />
			<th class="head_ct"><a href="javascript:;" onclick="checkAll('${tab }',this);" title="<u:msg titleId='cm.check.all' />" data-checked="Y"><u:msg title="${tabTitle }" alt="${tabTitle }"/></a></th>
		</c:forEach>
	</tr>
	<u:set var="nodataStyle" test="${fn:length(wlReprtUserLVoList)>1}" value="style=\"display:none;\"" elseValue=""/>
	<tr id="nodata" ${nodataStyle }>
	<td class="nodata" colspan="${fn:length(tabList)+2 }"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
	<c:if test="${fn:length(wlReprtUserLVoList)>0}">
		<c:forEach items="${wlReprtUserLVoList}" var="wlReprtUserLVo" varStatus="status"
			 ><u:set test="${status.last}" var="trDisp" value="display:none"
			/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${wlReprtUserLVo.userUid}"
			/><u:set test="${status.last}" var="clsIndex" value="_NO" elseValue="_${status.index+1}"
			/>
			<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="${trId}" style="${trDisp}" >
				<td class="bodybg_ct" id="userChk"><input type="checkbox" id="userCheck" value="${wlReprtUserLVo.regrUid }" /><u:input type="hidden" id="userUid" value="${wlReprtUserLVo.userUid }"/></td>
				<td class="body_ct" id="userNm" data-userUid="${wlReprtUserLVo.userUid }" style="cursor:pointer;"><u:out value="${wlReprtUserLVo.userNm }" /></td>
				<c:forEach var="tab" items="${tabList }" varStatus="tabStatus">
					<c:set var="checked" value=""/>
					<c:forTokens var="typCd" items="${wlReprtUserLVo.typCd }" delims="/" varStatus="status">
						<c:if test="${typCd eq tab }"><c:set var="checked" value="checked=\"checked\""/></c:if>
					</c:forTokens>
					<td class="body_ct ${tab }Chk" ><input type="checkbox" name="typCd${tab }" value="${tab }" ${checked }/></td>
				</c:forEach>
			</tr>
		</c:forEach>	
	</c:if>
	
</u:listArea>

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button href="javascript:void(0)" onclick="reprtUserSelect();" alt="보고자선택" titleId="wl.btn.user.select" auth="W"/>
	<u:button href="javascript:delAllRow();" titleId="cm.btn.allDel" alt="전체삭제" auth="W" />
	<u:button href="javascript:delSelRow();" titleId="cm.btn.selDel" alt="선택삭제" auth="W" />
	<li style="float:left;width:30px;">&nbsp;</li>
	<u:button titleId="cm.btn.save" onclick="save();" alt="저장" auth="W" />
	<u:button href="javascript:void(0)" onclick="dialog.close(this);" alt="닫기" titleId="cm.btn.close" />
</u:buttonArea>

	</form>
</div>
