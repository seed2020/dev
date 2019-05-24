<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// 검색 조건 변경 여부 %>
var gSearchCondChanged = false;
function onSearchCondChange(changed){
	if(changed == false){
		gSearchCondChanged = false;
		var $area = $("#transferPop");
		$area.find("#resultSubAreaArea").show();
		$area.find("#transferBtn").show();
		$area.find("#defragArea").show();
		//defragArea
	} else {
		if(!gSearchCondChanged){
			gSearchCondChanged = true;
			var $area = $("#transferPop");
			//$area.find("#resultSubAreaArea").hide();
			$area.find("#transferBtn").hide();
		}
	}
	return true;
}<%
// 전체 클릭 - 양식 %>
function disableFormIdArea(flag){
	var $chks = $("#transferPop #formIdArea input[type=checkbox]");
	if(flag){
		$chks.prop('disabled', true).uniform.update();
	} else {
		$chks.removeAttr("disabled").uniform.update();
	}
	onSearchCondChange();
}<%
// 검색 - 이관 대상 수 조회함 %>
function getTransferCnt(){
	var param = getSearchParam();
	if(param==null) return;
	
	callAjax("./getTranCntAjx.do?menuId=${menuId}", param, function(data){
		if(data.message != null) alert(data.message);
		var $countArea = $("#transferPop #countArea");
		onSearchCondChange(false);
		var sum = 0, cnt;
		["regRecLst","recvRecLst","distRecLst","paperDoc"].each(function(index, va){
			cnt = data[va];
			if(cnt==null){
				cnt = "0";
			} else {
				sum += parseInt(cnt);
			}
			$countArea.find('#'+va).text(addComma(cnt));
		});
		$countArea.find('#sum').text(addComma(sum+""));
		onSearchCondChange(sum==0);
	});
}<%
// 검색조건 Object 반환 %>
function getSearchParam(){
	var param = {compId:'${param.compId}'};
	var $area = $("#transferPop");
	
	var sdt = $area.find('#durStrtDt').val();
	if(sdt!='') param['durStrtDt'] = sdt;
	
	var $eobj = $area.find('#durEndDt');
	var edt = $eobj.val();
	if(edt!='') param['durEndDt'] = edt;
	else{
		param['durEndDt'] = '${today}';
		$eobj.val('${today}');
	}
	if($area.find('#papDocInclYn').prop('checked') == true){<%// 종이문서 이관여부 체크%>
		param['papDocInclYn'] = 'Y';
	}
	if($area.find('#allForm').prop('checked') == true){<%// 양식 - 전체 체크%>
		param['docInclYn'] = 'Y';
	} else {
		var arr = [];
		$area.find("#formIdArea input[type=checkbox]:checked").each(function(){
			arr.push($(this).val());
		});
		if(arr.length==0){
			if(param['papDocInclYn'] != 'Y'){<%// 종이문서가 선택 안된 경우 - 전체 문서도 아니고, 양식도 선택 안되어 있으면 %>
				alertMsg('ap.msg.selectForm');<%//ap.msg.selectForm=양식을 선택해 주십시요.%>
				return null;
			}
		} else {
			param['formIds'] = arr.join(',');
			param['docInclYn'] = 'Y';
		}
	}
	
	return param;
}<%
// 저장소 변경%>
function onChangeStor(obj){
	$nmArea = $("#transferPop #storNmArea");
	if(obj.selectedIndex==0){
		$nmArea.find('input[name^=rescVa]').removeAttr("disabled").removeClass('input_disabled').uniform.update();
		$nmArea.find("select").removeAttr("disabled").uniform.update();
	} else {
		$nmArea.find('input[name^=rescVa]').prop('disabled', true).addClass('input_disabled').uniform.update();
		$nmArea.find("select").prop('disabled', true).uniform.update();
	}
}<%
// 문서이관 클릭 %>
function processTransfer(){
	var param = getSearchParam();
	if(param==null) return;
	if(!addStorageParam(param)) return;<%
	// cm.msg.tran.cfrm=이관은 데이터 백업 후 진행해야 합니다.\\n이관을 진행 하시겠습니까 ?%>
	if(confirmMsg('cm.msg.tran.cfrm') != true) return;
	if($("#transferPop #defrag").prop("checked")==true) param['defrag'] = 'Y';<%
	// 이관 진행 %>
	callAjax("./transTranAjx.do?menuId=${menuId}", param, function(data){
		if(data.message != null) alert(data.message);
		if(data.result=='ok') reload();
	});
}<%
// 저장소ID 또는 저장소 리소스명을 파라미터에 더함 %>
function addStorageParam(param){
	var $area = $("#transferPop #resultArea");
	var storId = $area.find("#storId").val();
	if(storId!=''){<%// 저장소ID 전달 - 기존 저장소 %>
		param['storId'] = storId;
	} else {<%// 어권별 리소스명 전달 - 신규 저장소 %>
		var $nmArea = $area.find('#storNmArea');
		if(!validator.validate($nmArea[0])){
			return false;
		}
		var arr = [];
		$nmArea.find('input[name^=rescVa]').each(function(){
			arr.push({langTypCd:$(this).attr('name').substring(7), rescVa:$(this).val()})
		});
		param['resces'] = arr;
	}
	return true;
}
//$(document).ready(function() {
//});
//-->
</script>
<div id="transferPop" style="width:650px;">
<% // 검색영역 %>
<u:searchArea>
<form name="searchForm" id="searchTransferForm" >
<u:input type="hidden" id="durCat" value="regDt" />
<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td class="search_tit" style="width:40px;"><u:msg titleId="cols.prd" alt="기간" /></td>
			<td><u:calendar id="durStrtDt" name="durStrtDt" option="{end:'durEndDt'}" titleId="cols.strtYmd" handler="onSearchCondChange" /></td>
			<td class="search_body_ct"> ~ </td>
			<td><u:calendar id="durEndDt" name="durEndDt" option="{start:'durStrtDt'}" titleId="cols.endYmd" handler="onSearchCondChange" value="${today}" mandatory="Y" /></td>
			</tr>
			</table>
		</td>
		<td>
			<div class="button_search">
				<ul>
					<li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:getTransferCnt();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li>
				</ul>
			</div>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<table border="0" cellpadding="0" cellspacing="0" style="width:100%">
			<tr>
			<td class="search_tit" style="width:40px;"><u:msg titleId="cols.form" alt="양식" /></td>
			<td>
				<table border="0" cellpadding="0" cellspacing="0">
				<tr>
				<u:checkbox value="Y" id="allForm" name="allForm" titleId="cm.option.all" alt="전체" checked="${true}"
					onclick="disableFormIdArea(this.checked);" />
				</tr>
				</table>
			</td>
			</tr>
			<tr>
			<td></td>
			<td>
				<div style="min-height:50px; max-height:150px; width:100%; overflow-y:auto">
				<table id="formIdArea" border="0" cellpadding="0" cellspacing="0">
				<tr>
				<c:forEach items="${apFormBVoList}" var="apFormBVo" varStatus="formStatus">
				<u:set test="${not formStatus.first and (formStatus.index % 4 == 0)}"
					value="</tr><tr>" elseValue="" />
				<u:checkbox value="${apFormBVo.formId}" name="formId" title="${apFormBVo.rescNm}" disabled="Y" onclick="onSearchCondChange()" />
				</c:forEach>
				</tr>
				</table>
				</div>
			</td>
			</tr>
			<tr>
			<td class="search_tit" style="width:40px;"><u:msg titleId="cm.etc" alt="기타" /></td>
			<td>
				<table border="0" cellpadding="0" cellspacing="0">
				<tr>
				<u:checkbox value="Y" id="papDocInclYn" name="papDocInclYn" titleId="ap.jsp.tran.tranPaper" alt="종이문서 이관 여부" 
					onclick="onSearchCondChange(true);" />
				</tr>
				</table>
			</td>
			</tr>
			</table>
		</td>
	</tr>
</table>
</form>
</u:searchArea>

<div id="resultArea" style="height:170px;">
<div id="resultSubAreaArea" style="display:none">
<u:title titleId="dm.jsp.stor.title" alt="저장소" type="small" />
<u:listArea colgroup="25%,75%">
<tr>
	<td class="head_lt"><u:msg titleId="dm.cols.stor.select" alt="저장소선택" /></td>
	<td class="bodybg_lt" ><select id="storId" name="storId" <u:elemTitle titleId="dm.jsp.stor.title" alt="저장소" /> onchange="onChangeStor(this);"
	><u:option value="" titleId="dm.cols.stor.create" selected="true"/><c:forEach 
	items="${apStorBVoList}" var="apStorBVo" varStatus="status"><u:option value="${apStorBVo.storId}" title="${apStorBVo.rescNm}" /></c:forEach></select></td>	
</tr>
<tr>
	<td class="head_lt"><u:mandatory id="storMandatory"/><u:msg titleId="dm.cols.storNm" alt="저장소명" /></td>
	<td id="storNmArea"><table cellspacing="0" cellpadding="0" border="0">
		<tr>
		<td id="langTypArea">
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:set test="${status.first}" var="style" value="width:150px;" elseValue="width:150px; display:none" />
			<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
			<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="dm.cols.storNm" value="" style="${style}"
				maxByte="120" validator="changeLangSelector('storNmArea', id, va)" mandatory="Y" />
		</c:forEach>
		</td>
		<td>
		<c:if test="${fn:length(_langTypCdListByCompId)>1}">
			<select id="langSelector" onchange="changeLangTypCd('storNmArea','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
			</c:forEach>
			</select>
		</c:if>
		<u:input type="hidden" id="rescId" value="" />
		</td>
		</tr>
		</table></td>
	</tr>
</u:listArea>

<u:title titleId="ap.cols.tgtCnt" alt="대상 건수" type="small" />
<u:listArea colgroup="20%,20%,20%,20%,20%" noBottomBlank="true">
<tr>
	<th class="head_ct"><u:msg titleId="ap.bx.regRecLst" alt="등록대장" /></th>
	<th class="head_ct"><u:msg titleId="ap.bx.recvRecLst" alt="접수대장" /></th>
	<th class="head_ct"><u:msg titleId="ap.bx.distRecLst" alt="배부대장" /></th>
	<th class="head_ct"><u:msg titleId="ap.jsp.paperDoc" alt="종이문서" /></th>
	<th class="head_ct"><u:msg titleId="cm.option.all" alt="전체" /></th>
</tr>
<tr id="countArea">
	<td class="body_ct" id="regRecLst">&nbsp;</td>
	<td class="body_ct" id="recvRecLst">&nbsp;</td>
	<td class="body_ct" id="distRecLst">&nbsp;</td>
	<td class="body_ct" id="paperDoc">&nbsp;</td>
	<td class="body_ct" id="sum">&nbsp;</td>
</tr>
</u:listArea>
</div>
</div>

<u:blank /><c:if
	test="${sessionScope.userVo.userUid eq 'U0000001'}">
<div style="height:20px">
<u:checkArea id="defragArea" style="display:none"><u:checkbox
	value="Y" titleId="ap.msg.defragAftTran" alt="이관 후 테이블 조각모음을 수행 합니다." id="defrag" name="defrag" /></u:checkArea>
</div></c:if>

<% // 하단 버튼 %>
<u:buttonArea>
<u:button id="transferBtn" titleId="dm.cols.doc.transfer" alt="문서이관" onclick="processTransfer();" auth="SYS" style="display:none;"/>
<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</div>
