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
// 검색 - 이관 대상 수 조회함 %>
function getTransferCnt(){
	var param = getSearchParam();
	if(param==null) return;
	var $area = $("#transferPop");
	$area.find("#transferApvdBtn").hide();
	$area.find("#transferRjtBtn").hide();
	$area.find("#transferTakovrBtn").hide();
	
	callAjax("./getTranCntAjx.do?menuId=${menuId}", param, function(data){
		if(data.message != null) alert(data.message);
		var $countArea = $("#transferPop #countArea");
		onSearchCondChange(false);
		var sum = 0, cnt;
		["totalCnt","discCnt","takovrCnt"].each(function(index, va){
			cnt = data[va];
			if(cnt==null){
				cnt = "0";
			}
			if(va=='totalCnt') sum += parseInt(cnt);
			$countArea.find('#'+va).text(addComma(cnt));
			if(va=='discCnt' && cnt!="0"){
				$area.find("#transferApvdBtn").show();
				$area.find("#transferRjtBtn").show();
			}
			if(va=='takovrCnt' && cnt!="0"){
				$area.find("#transferTakovrBtn").show();
			}
		});
		$countArea.find('#sum').text(addComma(sum+""));
		onSearchCondChange(sum==0);
	});
}<%
// 검색조건 Object 반환 %>
function getSearchParam(){
	var param = {paramCompId:'${param.paramCompId}'};
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
	var $area = $("#transferPop");
	if($area.find('#discCnt').html()!='0'){
		alertMsg('dm.msg.transfer.not.disc');<% // dm.msg.transfer.not.disc=심의중인 문서가 있어서 이관할수 없습니다.%>
		return;	
	}
	var param = getSearchParam();
	if(param==null) return;
	if(!addStorageParam(param)) return;
	if(confirmMsg('cm.msg.tran.cfrm') != true) return;<% // cm.msg.tran.cfrm=이관은 데이터 백업 후 진행해야 합니다.\\n이관을 진행 하시겠습니까 ?%>
	if($area.find("#defrag").prop("checked")==true) param['defrag'] = 'Y';<%
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
			arr.push({langTypCd:$(this).attr('name').substring(7), rescVa:$(this).val()});
		});
		param['resces'] = arr;
	}
	return true;
}<% // [심의문서일괄처리] %>
function saveTranProcDoc(statCd) {
	var param = getSearchParam();
	if(param==null) return;
	var url = 'transTakovrDocAllAjx';
	if(statCd != null){
		param['statCd'] = statCd;
		url = 'transDiscDocAllAjx';
	}
	callAjax('./'+url+'.do?menuId=${menuId}', param, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			getTransferCnt();
		}
	});
};
//$(document).ready(function() {
//});
//-->
</script>
<div id="transferPop" style="width:450px;">
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
	items="${dmStorBVoList}" var="dmStorBVo" varStatus="status"><u:option value="${dmStorBVo.storId}" title="${dmStorBVo.storNm}" /></c:forEach></select></td>	
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
<u:listArea colgroup=",20%,20%" noBottomBlank="true">
<tr>
	<th class="head_ct"><u:msg titleId="dm.cols.doc.transfer.target" alt="대상" /></th>
	<th class="head_ct"><u:msg titleId="dm.cols.discDoc" alt="심의문서" /></th>
	<th class="head_ct"><u:msg titleId="dm.cols.docStatO" alt="인수대기" /></th>
</tr>
<tr id="countArea">
	<td class="body_ct" id="totalCnt">&nbsp;</td>
	<td class="body_ct" id="discCnt">&nbsp;</td>
	<td class="body_ct" id="takovrCnt">&nbsp;</td>
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
<u:button id="transferTakovrBtn" titleId="dm.btn.trans.tgtCancel" alt="인계취소" onclick="saveTranProcDoc(null);" auth="A" style="display:none;"/>
<u:button id="transferApvdBtn" titleId="dm.cols.discDoc.apvd" alt="심의문서(승인)" onclick="saveTranProcDoc('A');" auth="A" style="display:none;"/>
<u:button id="transferRjtBtn" titleId="dm.cols.discDoc.rjt" alt="심의문서(반려)" onclick="saveTranProcDoc('R');" auth="A" style="display:none;"/>
<u:button id="transferBtn" titleId="dm.cols.doc.transfer" alt="문서이관" onclick="processTransfer();" auth="A" style="display:none;"/>
<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</div>
