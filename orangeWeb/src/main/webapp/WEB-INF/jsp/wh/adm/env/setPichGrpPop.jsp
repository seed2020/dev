<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%// [순서조절:위로,아래로] 서버에 저장하지 않고 화면상에서만 순서 조정함 %>
function movePich(direction){
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
	$("#pichListArea tbody:first input[type='checkbox']:checked").each(function(){
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
var gMaxRow = parseInt('${fn:length(whPichGrpLVoList)}');
<% // [버튼클릭] - 사용자추가  %>
function addRowUser(){
	var data=[];
	$.each($('#pichListArea input[name="userUid"]'), function(){
		if($(this).val()!='')
			data.push({userUid:$(this).val()});
	});
	parent.searchUserPop({data:data, multi:true, mode:'search'}, function(arr){
		if(arr!=null){
			//$('#userSelectArea').html('');
			addPichRow(arr);
			setNodata();
		}
	});
}<%// 행추가%>
function addPichRow(arr){
	var $tr, $hiddenTr = $("#pichListArea tbody:first #hiddenTr");
	var html = $hiddenTr[0].outerHTML;	
	var idVaList=getUserList();
	arr.each(function(index, userVo){
		if($.inArray(userVo.userUid, idVaList)!=-1)
			return true;
		html = html.replace(/_NO/g,'_'+gMaxRow);
		gMaxRow++;
		$hiddenTr.before(html);		
		$tr = $hiddenTr.prev();
		$tr.attr('id','tr'+userVo.userUid);
		$tr.find("input[type='checkbox']").val(userVo.userUid);
		$tr.find("input[name='userUid']").val(userVo.userUid);
		$tr.find("td#userNm").text(userVo.rescNm);
		
		$tr.show();
		setJsUniform($tr[0]);
	});
}<%// 선택삭제%>
function delPichSelRow(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) delPichRowInArr(arr);
	setNodata();
}
<%// 삭제 - 배열에 담긴 목록%>
function delPichRowInArr(rowArr){
	var delVa = $("#delList").val(), delArr = [], $mdId;
	if(delVa!='') delArr.push(delVa);
	for(var i=0;i<rowArr.length;i++){
		$mdId = $(rowArr[i]).find("input[name='userUid']");
		if($mdId.val()!=''){
			delArr.push($mdId.val());
		}
		$(rowArr[i]).remove();
	}
	$("#delList").val(delArr.join(','));
}<% // 보고대상 목록  %>
function getUserList(){
	var idVaList=[];
	$('#pichListArea input[name="userUid"]').each(function(){
		idVaList.push($(this).val());
	});
	return idVaList;
	
}<%//nodata 세팅 %>
function setNodata(){
	if($('#pichListArea tr').not('#hiddenTr').find('input[name="userUid"]').length>0) $('#nodata').hide();
	else $('#nodata').show();
}<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
}<% // [팝업:저장] 테이블 저장 %>
function savePich() {
	if (validator.validate('pichGrpArea')) {
		<%// trArr : 저장할 곳의 tr 테그 배열 %>
		
		var trArr=$("#pichListArea tbody:first tr").not("#headerTr,#nodata,#hiddenTr");
		
		var count=trArr.length;
		
		if(count==0 && $('#delList').val()==''){
			alertMsg("cm.msg.nodata.toSave");<%//cm.msg.nodata.toSave=저장할 데이터가 없습니다.%>
			return;
		}
		
		$.each(trArr, function(){
			$(this).find("[name='valid']").val('Y');
		});
		
		<%// 정렬순서 세팅%>
		var ordr = 1;
		$("#pichListArea input[name='sortOrdr']").each(function(){
			if($(this).attr('id')!='sortOrdr_NO'){
				$(this).val(ordr++);
			}
		});
		<%// disable 된 select disable 해제 %>
		var arr = releaseDisable($('#pichListArea select'));
		
		var $form = $('#setForm');
		$form.attr('method','post');
		$form.attr('action','./transPichGrpList.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form[0].submit();
		
		<%// select disable 다시 적용 %>
		unreleaseDisable(arr);
		
		dialog.close(this);
	}
};
function pageReload(dialog){
	if(dialog!=undefined)
		parent.dialog.close(dialog);
	location.replace(location.href);
}
$(document).ready(function() {
});
//-->
</script>
<div style="width:300px;">
<c:if test="${empty isView || isView==false}">
<form id="setForm">
<u:input type="hidden" id="menuId" value="${menuId }"/>
<u:input type="hidden" id="dialog" value="setGrpDialog"/>
<c:if test="${!empty whPichGrpBVo }">
<u:input type="hidden" id="pichGrpId" value="${whPichGrpBVo.pichGrpId }"/>
</c:if>
<u:title titleId="wh.jsp.pichGrp.title" type="small" alt="담당자그룹" />
<% // 폼 필드 %>
	<u:listArea id="pichGrpArea" colgroup="35%,">
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg titleId="cols.grpNm" alt="그룹명" /></td>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tbody>
				<tr>
					<td id="langTypArea">
						<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:convert srcId="${whPichGrpBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
							<u:set test="${status.first}" var="style" value="width:100px;" elseValue="width:100px; display:none" />
							<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
							<u:input id="grpRescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.grpNm" value="${rescVa}" style="${style}"
								maxByte="120" validator="changeLangSelector('pichGrpArea', id, va)" mandatory="Y" />
						</c:forEach>
						<u:input type="hidden" id="grpRescId" value="${whPichGrpBVo.rescId}" />
					</td>
					<td id="langTypOptions">
						<c:if test="${fn:length(_langTypCdListByCompId)>1}">
							<select id="langSelector" onchange="changeLangTypCd('pichGrpArea','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
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
				<u:radio name="useYn" value="Y" titleId="cm.option.use" alt="사용" checkValue="${whPichGrpBVo.useYn }"  inputClass="bodybg_lt" checked="${empty whPichGrpBVo.useYn }"/>
				<u:radio name="useYn" value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${whPichGrpBVo.useYn }" inputClass="bodybg_lt" />
			</u:checkArea>
		</td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="wl.cols.dftYn" alt="기본여부" /></td>
		<td>
			<u:checkArea>
				<u:radio name="dftYn" value="Y" titleId="cm.option.use" alt="사용" checkValue="${whPichGrpBVo.dftYn }"  inputClass="bodybg_lt" />
				<u:radio name="dftYn" value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${whPichGrpBVo.dftYn }" inputClass="bodybg_lt" checked="${empty whPichGrpBVo.dftYn }"/>
			</u:checkArea>
		</td>
	</tr>
</u:listArea>
<u:blank />
<u:title titleId="wh.jsp.pichList.title" type="small" alt="담당자목록" >
<u:titleButton href="javascript:addRowUser();" id="btnAddRow" titleId="wl.btn.addUser" alt="사용자추가" auth="W" />
<u:titleButton href="javascript:delPichSelRow();" id="btnDelSel" titleId="cm.btn.selDel" alt="선택삭제" auth="W" />
		
<u:titleIcon type="up" onclick="movePich('up');" auth="W" />
<u:titleIcon type="down" onclick="movePich('down');" auth="W" />
</u:title>
<u:listArea id="pichListArea" style="height:220px;overflow-y:auto;" colgroup="15%,">	
	<tr id="headerTr">
		<th class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('pichListArea', this.checked);" value=""/></th>
		<th class="head_ct"><u:msg titleId="wh.cols.hdl.pich" alt="처리담당자"/></th>
	</tr>
	<u:set var="nodataStyle" test="${fn:length(whPichGrpLVoList)>1}" value="style=\"display:none;\"" elseValue=""/>
	<tr id="nodata" ${nodataStyle }>
	<td class="nodata" colspan="2"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
	<c:if test="${fn:length(whPichGrpLVoList)>0}">
		<c:forEach items="${whPichGrpLVoList}" var="vo" varStatus="status"
			 ><u:set test="${status.last}" var="trDisp" value="display:none"
			/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${vo.userUid}"
			/><u:set test="${status.last}" var="lstIndex" value="_NO" elseValue="_${status.index+1}"
			/>
			<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="${trId}" style="${trDisp}" >
				<td class="bodybg_ct"><input type="checkbox" value="${vo.userUid }" /><u:input type="hidden" id="userUid" value="${vo.userUid }"
				/><u:input type="hidden" id="sortOrdr${lstIndex}" name="sortOrdr" 
				/><u:input type="hidden" id="valid${lstIndex}" name="valid" /></td>
				<td class="body_ct" id="userNm"><u:out value="${vo.userNm }"/></td>
			</tr>				
		</c:forEach>	
	</c:if>
	
</u:listArea>
<input type="hidden" id="delList" name="delList" />
	</form>
	<u:blank />
	<% // 하단 버튼 %>
<u:buttonArea style="clear:both;">
	<u:button titleId="cm.btn.save" onclick="savePich();" alt="저장" auth="W" />
	<u:button href="javascript:void(0)" onclick="dialog.close(this);" alt="닫기" titleId="cm.btn.close" />
</u:buttonArea></c:if>

<c:if test="${isView==true }">
<u:listArea id="pichListArea" style="height:220px;overflow-y:auto;" colgroup="15%,">	
	<tr id="headerTr">
		<th class="head_ct"><u:msg titleId="wh.cols.hdl.pich" alt="처리담당자"/></th>
	</tr>
	<u:set var="nodataStyle" test="${fn:length(whPichGrpLVoList)>0}" value="style=\"display:none;\"" elseValue=""/>
	<tr id="nodata" ${nodataStyle }>
	<td class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
	<c:if test="${fn:length(whPichGrpLVoList)>0}">
		<c:forEach items="${whPichGrpLVoList}" var="vo" varStatus="status" >
			<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
				<td class="body_ct" ><a href="javascript:viewUserPop('${vo.userUid }');"><u:out value="${vo.userNm }"/></a></td>
			</tr>				
		</c:forEach>	
	</c:if>
	
</u:listArea>
	<u:blank />
	<% // 하단 버튼 %>
<u:buttonArea style="clear:both;">
	<u:button href="javascript:void(0)" onclick="dialog.close(this);" alt="닫기" titleId="cm.btn.close" />
</u:buttonArea>
</c:if>

</div>
