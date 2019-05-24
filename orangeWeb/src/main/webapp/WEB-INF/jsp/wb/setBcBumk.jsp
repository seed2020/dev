<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<% // 엑셀 파일 다운로드 %>
function excelDownFile() {
	var $form = $('#excelForm');
	$form.attr('method','post');
	$form.attr('action','./excelDownLoad.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form[0].submit();
};

//상세보기
function viewBc(bcId) {
	//location.href="./viewBcBumk.do?menuId=${menuId}&bcId="+bcId;
	dialog.open('viewBcPop', '<u:msg titleId="wb.jsp.viewBcPop.title" alt="명함상세보기" />', './viewBcPop.do?menuId=${menuId}&bcId='+bcId);
};

<%// 체크된 목록 리턴 - uncheck : true 면 체크를 해제함 %>
function getChecked(uncheck){
	var va, arr = [];
	$("#listAreaFrom input:checked").each(function(){
		va = $(this).val();
		if(va!=null && va!=''){
			arr.push({bcId:va,bcNm:$(this).attr("data-bcNm"),compNm:$(this).attr("data-compNm"),dftCntcVal:$(this).attr("data-dftCntcVal"),email:$(this).attr("data-email")});
		}
		if(uncheck){
			$(this).trigger('click');
		}
	});
	if(arr.length==0){
		alertMsg("cm.msg.noSelect");<%//cm.msg.noSelect=선택한 항목이 없습니다.%>
		return null;
	}
	return arr;
};

<%// [아이콘] 선택추가 %>
function addCols(){	
	var arr = getIframeContent('openListFrm').getChecked(true);
	if(arr==null) return;
	var maxCnt=50; // 최대 즐겨찾기 건수
	var $tr, $hiddenTr = $("#listArea tbody:first #hiddenTr");
	var html = $hiddenTr[0].outerHTML;
	var vas = getAllCheckVas();
	var cnt=$("#listArea tbody:first tr").not('#headerTr, #hiddenTr').length; // 현재 건수
	arr.each(function(index, obj){
		if(vas==null || !vas.contains(obj.bcId)){
			cnt++;
			if(cnt>maxCnt){				
				alertMsg('wb.msg.maxBumk'); // wb.msg.maxBumk=즐겨찾기는 최대 50개까지 추가 가능합니다.
				return false;
			}			
			$hiddenTr.before(html);
			$tr = $hiddenTr.prev();
			$tr.attr('id','tr'+obj.bcId);
			$tr.find("input[type='checkbox']").val(obj.bcId);
			$tr.find("input[name='bcId']").val(obj.bcId);
			$tr.find("td#bcNm div.ellipsis").text(obj.bcNm);
			$tr.find("td#compNm div.ellipsis").text(obj.compNm);
			$tr.find("td#dftCntcVal").text(obj.dftCntcVal);
			$tr.find("td#email div").text(obj.email);
			$tr.show();
			setJsUniform($tr[0]);
		}
	});
};
<%// [아이콘] 선택제거 %>
function removeCols(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr==null) return;
	arr.each(function(index, tr){
		$(tr).remove();
	}, true);
};
<%// [아이콘] 상하 이동 %>
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
<%//checkbox 가 선택된 id 목록 리턴 %>
function getAllCheckVas(){
	var arr=[], va;
	$("#listArea tbody:first input[type='checkbox']").each(function(){
		va = $(this).val();
		if(va!='' && va!=null) arr.push(va);
	});
	if(arr.length==0){
		return null;
	}
	return arr;
};

<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
};

<%// 저장 %>
function save(){
	<%// 서버 전송%>
	if (true/*confirmMsg("cm.cfrm.save")*/ ) {
		var $form = $('#setBcBumk');
		$form.attr('method','post');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
};

$(document).ready(function() {
	setUniformCSS();
	$("#openListFrm").attr('src', './listBcBumkFrm.do?menuId=${menuId}');
});
//-->
</script>

<u:title titleId="wb.jsp.setBcBumk.title" alt="자주찾는 명함" menuNameFirst="true"/>

<div style="float:left; width:48%;">
	<u:title titleId="wb.jsp.setBcBumk.subtitle02" alt="추가된 명함" type="small" >
		<u:titleIcon type="up" href="javascript:move('up')" auth="A" />
		<u:titleIcon type="down" href="javascript:move('down')" auth="A" />
	</u:title>
	<u:titleArea
		outerStyle="height:600px; overflow-x:hidden; overflow-y:auto;"
		innerStyle="NO_INNER_IDV">
		<form id="setBcBumk" style="padding:10px;" action="./transBcBumk.do">
			<u:input type="hidden" id="menuId" value="${menuId }"/>
			<div id="listArea" class="listarea">
				<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
					<tr id="headerTr">
						<th width="20px" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></th>
						<th width="20%" class="head_ct"><u:msg titleId="cols.nm" alt="이름" /></th>
						<th class="head_ct"><u:msg titleId="cols.comp" alt="회사" /></th>
						<th width="20%" class="head_ct"><u:msg titleId="cols.reprCntc" alt="우선연락처" /></th>
						<th width="20%" class="head_ct"><u:msg titleId="cols.email" alt="이메일" /></th>
					</tr>
					<c:if test="${!empty wbBcBVoBumkList}">
						<c:forEach var="list" items="${wbBcBVoBumkList}" varStatus="status">
							<u:set test="${status.last}" var="trDisp" value="display:none" />
							<u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${list.bcId}" />
							<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="${trId}" style="${trDisp}">
								<td class="bodybg_ct"><input type="checkbox" value="${list.bcId}" /><input type="hidden" name="bcId" value="${list.bcId}" /></td>
								<td class="body_ct" id="bcNm"><div class="ellipsis" title="${list.bcNm }"><a href="javascript:viewBc('${list.bcId }');">${list.bcNm }</a></div></td>
								<td class="body_ct" id="compNm"><div class="ellipsis" title="${list.compNm }">${list.compNm }</div></td>
								<td class="body_ct" id="dftCntcVal">
									<c:choose>
										<c:when test="${list.dftCntcTypCd eq 'homePhon' }">${list.homePhon }</c:when>
										<c:when test="${list.dftCntcTypCd eq 'compPhon' }">${list.compPhon }</c:when>
										<c:otherwise>${list.mbno }</c:otherwise>
									</c:choose>
								</td>
								<td class="body_ct" id="email"><div class="ellipsis" title="${list.email }"><a href="javascript:parent.mailToPop('${list.email }');" title="<u:msg titleId='or.jsp.viewUserPop.mailToPop' />">${list.email }</a></div></td>
							</tr>
						</c:forEach>
					</c:if>
				</table>
			</div>
		</form>
	</u:titleArea>
</div>

<div style="float:left; width:4%; text-align:center; margin:250px 0 0 0;">
	<table style="margin:0 auto 0 auto;" border="0" cellpadding="0" cellspacing="0">
		<tr><td><a href="javascript:removeCols();"<u:elemTitle titleId="cm.btn.selDel" alt="선택삭제" type="image" />><img src="${_cxPth}/images/${_skin}/ico_right.png" width="20" height="20" /></a></td></tr>
		<tr><td class="height5"></td></tr>
		<tr><td><a href="javascript:addCols();"<u:elemTitle titleId="cm.btn.selAdd" alt="선택추가" type="image" />><img src="${_cxPth}/images/${_skin}/ico_left.png" width="20" height="20" /></a></td></tr>
	</table>
</div>

<div style="float:right; width:48%;">
<u:title titleId="wb.jsp.setBcBumk.subtitle01" alt="추가 가능한 명함" type="small"  />
<u:titleArea  frameId="openListFrm" frameSrc="/cm/util/reloadable.do"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:600px;">
	
</u:titleArea>

</div>
<u:blank />
<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.excelDown" alt="엑셀다운" onclick="excelDownFile();" auth="R" />
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:;" onclick="save();" auth="W" />
</u:buttonArea>
<form id="excelForm">
	<c:forEach items="${paramEntryList}" var="entry" varStatus="status">
		<u:input type="hidden" id="${entry.key}" value="${entry.value}" />
	</c:forEach>
	<u:input type="hidden" id="listPage" value="setBcBumk" />
</form>

