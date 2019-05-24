<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%// [순서조절:위로,아래로] 서버에 저장하지 않고 화면상에서만 순서 조정함 %>
function move(obj, direction){
	var parent=$(obj).closest('div.topContainer');
	var i, arr = getCheckedTrs(parent, "cm.msg.noSelect");
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
}<%// 테이블헤더의 제목 클릭 - 전체선택%>
function checkAll(cd,obj){
	var parent=$(obj).closest('div.topContainer');
	var checked = $(obj).attr('data-checked') == 'N' ? true : false;
	$(obj).attr('data-checked',checked ? 'Y' : 'N');
	$chks = $(parent).find("td."+cd+"Chk input[type='checkbox']");
	$chks.each(function(){
		$(this).checkInput(checked);
		//useChk(this, null);
		//this.checked = checked;
	});
	$chks.uniform.update();
}<%// 전체삭제%>
function delAllRow(obj){
	var parent=$(obj).closest('div.topContainer');
	$(parent).find('tbody:first tr').not('#headerTr, #hiddenTr, #nodata').each(function(){
		$(this).remove();
	});
	$(parent).find('tr#nodata').show();
	
}
<%// 선택삭제%>
function delSelRow(obj){
	var parent=$(obj).closest('div.topContainer');
	var arr = getCheckedTrs(parent, "cm.msg.noSelect");
	if(arr!=null){
		delRowInArr(arr);
		if($(parent).find('tbody:first tr').not('#headerTr, #hiddenTr, #nodata').length==0) $('tr#nodata').show();
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
function getCheckedTrs(parent, noSelectMsg){
	var arr=[], id, obj;
	$(parent).find("tbody:first input[id='userCheck']:checked").each(function(){
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
function getAllVas(parent){
	var arr=[];
	$(parent).find('tbody:first').not('#headerTr, #hiddenTr, #nodata').find('input[id="userUid"]').each(function(){
		if($(this).val()!=''){
			arr.push($(this).val());
		}
	});
	if(arr.length==0){
		return null;
	}
	return arr;
};<% // [팝업] - 보고자선택 %>
function reprtUserSelect(obj){
	var parent=$(obj).closest('div.topContainer');
	var vas = getAllVas(parent);
	
	var data = [];<% // data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	if(vas!=null){
		$.each(vas, function(){
			data.push({userUid:this});
		});	
	}
	
	var $tr, $hiddenTr = $(parent).find("tbody:first #hiddenTr");
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
					$tr.find("input[id^='reprtTypCdChk']").attr('checked', true);
					
					$tr.show();
					setEvt($tr);
					setJsUniform($tr[0]);
					addCnt++;
				}
			});
			if(addCnt>0) $(parent).find('tr#nodata').hide();
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
		$('#setForm div.topContainer').find('tbody:first tr').not('#headerTr, #hiddenTr, #nodata').each(function(){
			typCd='';
			$(this).find('input[id^="reprtTypCdChk"]:checked').each(function(index){
				if(index>0) typCd+='/';
				typCd+=$(this).val();
			});
			$(this).find("input[name='reprtTypCd']").val(typCd);
			//$(this).find("input[name='reprtTypCd']").remove();
			//$(this).appendHidden({name:'reprtTypCd',value:typCd});
		});
		var $form = $('#setForm');
		$form.attr('method','post');
		$form.attr('action','./transReprtGrp.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
};<% // [클릭] 사용자 정보 팝업 %>
function setEvt(tgt){
	if(tgt===undefined) tgt=$('#setForm div.topContainer').find('tbody:first tr').not('#headerTr, #hiddenTr, #nodata');
	tgt.on('click', 'td#userNm', function(){
		parent.viewUserPop($(this).attr('data-userUid'));
	});
}
function pageReload(dialog){
	if(dialog!=undefined)
		parent.dialog.close(dialog);
	location.replace(location.href);
}
$(document).ready(function() {
	setEvt();
//	pageReload('setReprtGrpDialog');
});
//-->
</script>
<div style="width:700px;">
<form id="setForm">
<u:input type="hidden" id="menuId" value="${menuId }"/>
<u:input type="hidden" id="dialog" value="setReprtGrpDialog"/>
<c:if test="${!empty wlReprtGrpBVo }">
<u:input type="hidden" id="grpNo" value="${wlReprtGrpBVo.grpNo }"/>
</c:if>

<% // 폼 필드 %>
	<u:listArea id="grpArea" colgroup="15%,">
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg titleId="cols.grpNm" alt="그룹명" /></td>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tbody>
				<tr>
					<td id="langTypArea">
						<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:convert srcId="${wlReprtGrpBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
							<u:set test="${status.first}" var="style" value="width:200px;" elseValue="width:200px; display:none" />
							<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
							<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.grpNm" value="${rescVa}" style="${style}"
								maxByte="120" validator="changeLangSelector('grpArea', id, va)" mandatory="Y" />
						</c:forEach>
						<u:input type="hidden" id="rescId" value="${wlReprtGrpBVo.rescId}" />
					</td>
					<td id="langTypOptions">
						<c:if test="${fn:length(_langTypCdListByCompId)>1}">
							<select id="langSelector" onchange="changeLangTypCd('grpArea','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
							<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
							</c:forEach>
							</select>
						</c:if>
					</td>
				</tr>
				</tbody>
			</table>
		</td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
		<td>
			<u:checkArea>
				<u:radio name="useYn" value="Y" titleId="cm.option.use" alt="사용" checkValue="${wlReprtGrpBVo.useYn }"  inputClass="bodybg_lt" checked="${empty wlReprtGrpBVo.useYn }"/>
				<u:radio name="useYn" value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${wlReprtGrpBVo.useYn }" inputClass="bodybg_lt" />
			</u:checkArea>
		</td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="wl.cols.dftYn" alt="기본여부" /></td>
		<td>
			<u:checkArea>
				<u:radio name="dftYn" value="Y" titleId="cm.option.use" alt="사용" checkValue="${wlReprtGrpBVo.dftYn }"  inputClass="bodybg_lt" />
				<u:radio name="dftYn" value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${wlReprtGrpBVo.dftYn }" inputClass="bodybg_lt" checked="${empty wlReprtGrpBVo.dftYn }"/>
			</u:checkArea>
		</td>
	</tr>
</u:listArea>
<div style="width:100%;height:450px;">
<div class="topContainer" style="float:left; width:48%; height:400px;">
	<u:titleArea outerStyle="height:100%" innerStyle="width:94%; margin: 0 auto 0 auto; padding: 10px 0 0 0">
	<u:title titleId="wl.jsp.group.user.title" type="small" alt="내가 보고할 대상" >
	<u:titleIcon type="up" onclick="move(this, 'up');" auth="W" />
	<u:titleIcon type="down" onclick="move(this, 'down');" auth="W" />
	</u:title>
	<u:listArea style="height:350px;overflow-y:auto;">	
		<tr id="headerTr">
			<th class="head_ct"><u:msg titleId="cm.select.actname" alt="선택"/></th>
			<th class="head_ct"><u:msg titleId="cols.user" alt="사용자"/></th>
			<c:forEach var="tab" items="${tabList }" varStatus="status">
				<u:msg var="tabTitle" titleId="wl.cols.typCd.${tab }" />
				<th class="head_ct"><a href="javascript:;" onclick="checkAll('${tab }',this);" title="<u:msg titleId='cm.check.all' />" data-checked="Y"><u:msg title="${tabTitle }" alt="${tabTitle }"/></a></th>
			</c:forEach>
		</tr>
		<u:set var="nodataStyle" test="${fn:length(reqUserList)>1}" value="style=\"display:none;\"" elseValue=""/>
		<tr id="nodata" ${nodataStyle }>
		<td class="nodata" colspan="${fn:length(tabList)+2 }"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
		<c:if test="${fn:length(reqUserList)>0}">
			<c:forEach items="${reqUserList}" var="vo" varStatus="status"
				 ><u:set test="${status.last}" var="trDisp" value="display:none"
				/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${vo.userUid}"
				/><u:set test="${status.last}" var="clsIndex" value="_NO" elseValue="_${status.index+1}"
				/>
				<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="${trId}" style="${trDisp}" >
					<td class="bodybg_ct" id="userChk"><input type="checkbox" id="userCheck" value="${vo.userUid }" /><u:input type="hidden" id="userUid" value="${vo.userUid }"/><u:input type="hidden" id="tgtTypCd" value="U" /></td>
					<td class="body_ct" id="userNm" data-userUid="${vo.userUid }" style="cursor:pointer;"><u:out value="${vo.userNm }" /></td>
					<c:forEach var="tab" items="${tabList }" varStatus="tabStatus">
						<c:set var="checked" value=""/>
						<c:forTokens var="typCd" items="${vo.reprtTypCd }" delims="/" varStatus="status">
							<c:if test="${typCd eq tab }"><c:set var="checked" value="checked=\"checked\""/></c:if>
						</c:forTokens>
						<td class="body_ct ${tab }Chk" ><input type="checkbox" id="reprtTypCdChk${tab }" value="${tab }" ${checked }/></td>
					</c:forEach>
					<u:input type="hidden" id="reprtTypCd" value="" />
				</tr>				
			</c:forEach>	
		</c:if>
		
	</u:listArea>
	</u:titleArea>
	<u:buttonArea>
		<u:buttonS href="javascript:void(0)" onclick="reprtUserSelect(this);" alt="추가등록" titleId="cm.btn.addReg" auth="W"/>
		<u:buttonS onclick="delAllRow(this);" titleId="cm.btn.allDel" alt="전체삭제" auth="W" />
		<u:buttonS onclick="delSelRow(this);" titleId="cm.btn.selDel" alt="선택삭제" auth="W" />
	</u:buttonArea>
</div>

<div class="topContainer" style="float:right; width:48%; height:400px;">
	<u:titleArea outerStyle="height:100%" innerStyle="width:94%; margin: 0 auto 0 auto; padding: 10px 0 0 0">
	<u:title titleId="wl.jsp.group.reprt.title" type="small" alt="나에게 보고할 대상" >
	<u:titleIcon type="up" onclick="move(this, 'up');" auth="W" />
	<u:titleIcon type="down" onclick="move(this, 'down');" auth="W" />
	</u:title>
	<u:listArea style="height:350px;overflow-y:auto;">	
		<tr id="headerTr">
			<th class="head_ct"><u:msg titleId="cm.select.actname" alt="선택"/></th>
			<th class="head_ct"><u:msg titleId="cols.user" alt="사용자"/></th>
			<c:forEach var="tab" items="${tabList }" varStatus="status">
				<u:msg var="tabTitle" titleId="wl.cols.typCd.${tab }" />
				<th class="head_ct"><a href="javascript:;" onclick="checkAll('${tab }',this);" title="<u:msg titleId='cm.check.all' />" data-checked="Y"><u:msg title="${tabTitle }" alt="${tabTitle }"/></a></th>
			</c:forEach>
		</tr>
		<u:set var="nodataStyle" test="${fn:length(recvUserList)>1}" value="style=\"display:none;\"" elseValue=""/>
		<tr id="nodata" ${nodataStyle }>
		<td class="nodata" colspan="${fn:length(tabList)+2 }"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
		<c:if test="${fn:length(recvUserList)>0}">
			<c:forEach items="${recvUserList}" var="vo" varStatus="status"
				 ><u:set test="${status.last}" var="trDisp" value="display:none"
				/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${vo.userUid}"
				/><u:set test="${status.last}" var="clsIndex" value="_NO" elseValue="_${status.index+1}"
				/>
				<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="${trId}" style="${trDisp}" >
					<td class="bodybg_ct" id="userChk"><input type="checkbox" id="userCheck" value="${vo.userUid }" /><u:input type="hidden" id="userUid" value="${vo.userUid }"/><u:input type="hidden" id="tgtTypCd" value="R" /></td>
					<td class="body_ct" id="userNm" data-userUid="${vo.userUid }" style="cursor:pointer;"><u:out value="${vo.userNm }" /></td>
					<c:forEach var="tab" items="${tabList }" varStatus="tabStatus">
						<c:set var="checked" value=""/>
						<c:forTokens var="typCd" items="${vo.reprtTypCd }" delims="/" varStatus="status">
							<c:if test="${typCd eq tab }"><c:set var="checked" value="checked=\"checked\""/></c:if>
						</c:forTokens>
						<td class="body_ct ${tab }Chk" ><input type="checkbox" id="reprtTypCdChk${tab }" value="${tab }" ${checked }/></td>
					</c:forEach>
					<u:input type="hidden" id="reprtTypCd" value="" />
				</tr>				
			</c:forEach>	
		</c:if>
		
	</u:listArea>
	</u:titleArea>
	<u:buttonArea>
		<u:buttonS href="javascript:void(0)" onclick="reprtUserSelect(this);" alt="추가등록" titleId="cm.btn.addReg" auth="W"/>
		<u:buttonS onclick="delAllRow(this);" titleId="cm.btn.allDel" alt="전체삭제" auth="W" />
		<u:buttonS onclick="delSelRow(this);" titleId="cm.btn.selDel" alt="선택삭제" auth="W" />
	</u:buttonArea>
</div>
</div>
	</form>
	<u:blank />
	<% // 하단 버튼 %>
<u:buttonArea style="clear:both;">
	<u:button titleId="cm.btn.save" onclick="save();" alt="저장" auth="W" />
	<u:button href="javascript:void(0)" onclick="dialog.close(this);" alt="닫기" titleId="cm.btn.close" />
</u:buttonArea>
</div>
