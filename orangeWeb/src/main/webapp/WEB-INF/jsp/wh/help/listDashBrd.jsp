<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
request.setAttribute("statCdColors", new String[]{"#BCA9F5", "#00BFFF", "#fa8258", "#F78181", "#CC2EFA", "#58FA58", "#F7D358"});
%>
<style>
ul.selectList{list-style:none;float:left;margin:0px;padding:0px;}
ul.selectList li{float:left;}
ul.selectList li.optionList{padding-left:5px;}
#progressbar {
  height:${isPlt==true ? 10 : 20}px;
  border:1px solid #bfc8d2;
  background-color:#ffffff;
  /*border-radius: 8px;*/
  padding: ${isPlt==true ? 2 : 3}px;
  margin:${isPlt==true ? 2 : 3}px 0 ${isPlt==true ? 2 : 3}px 0;
}

#progressbar > div {
   height: ${isPlt==true ? 10 : 20}px;
   /*border-radius: 8px;*/
}
.groupdiv table#groupTbl td{border-bottom:1px solid #bfc8d2;}
</style>
<c:if test="${empty isPlt }">
<script type="text/javascript">
<!--<% // 검색조건 등록자 선택 %>
function schUserPop() {
	var $view = $("#searchForm");
	var data = {userUid:$view.find("#schOptWord").val()};<% // 팝업 열때 선택될 데이타 %>
	<% // option : data, multi, withSub, titleId %>
	searchUserPop({data:data}, function(userVo){
		if(userVo!=null){
			$view.find("#schOptWord").val(userVo.userUid);
			$view.find("#schUserNm").val(userVo.rescNm);
		}else{
			return false;
		}
	});
}<%// 부서 선택 %>
function schOrgPop(){
	var data = [];
	searchOrgPop({data:data}, function(orgVo){
		if(orgVo!=null){
			$('#deptId').val(orgVo.orgId);
			$('#deptNm').val(orgVo.rescNm);
		}
	});
}<% // 시스템 모듈 선택 %>
function selectSysMdList(obj){
	var target=$(obj).closest('li');
	$(target).nextAll().remove();
	// 담당자 select
	var pichSelect=$('#mdPichContainer select').eq(0);
	pichSelect.find('option').not(':first').remove(); // 선택을 제외하고 삭제
	setJsUniform($('#mdPichContainer'));
	
	// 담당자 select
	var catSelect=$('#mdCatContainer select').eq(0);
	catSelect.find('option').not(':first').remove(); // 선택을 제외하고 삭제
	setJsUniform($('#mdCatContainer'));
	
	if(obj.value=='' || obj.value===undefined) return;
	callAjax('./getSysMdListAjx.do?menuId=${menuId}', {mdPid:obj.value}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			addSysMdList(target, data.whMdBVoList, obj.value, pichSelect, catSelect);
		}
		if (data.result == 'end') {
			setMdDtlList(obj.value, pichSelect, catSelect);
		}
	});
}<% // 담당자 | 유형  조회 %>
function setMdDtlList(mdId, pichSelect, catSelect){
	callAjax('./getMdPichListAjx.do?menuId=${menuId}', {mdId:mdId}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') { // 담당자 추가
			addMdPichList(pichSelect, data.whMdPichLVoList);
		}
	});
	callAjax('./getMdCatListAjx.do?menuId=${menuId}', {mdId:mdId}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') { // 담당자 추가
			addMdCatList(catSelect, data.whCatGrpLVoList);
		}
	});
}<% // 시스템 모듈 추가 %>
function addSysMdList(target, whMdBVoList, mdId, pichSelect, catSelect){
	if(whMdBVoList==null || whMdBVoList.length==0) return;
	var buffer=[];
	var parent=$('<li></li>');
	buffer.push('<select onchange="selectSysMdList(this);" style="min-width:100px;">');
	var whMdBVo;
	buffer.push('<option value="">'+callMsg('cm.select.actname')+'</option>');
	$.each(whMdBVoList, function(index, item){
		whMdBVo=item.map;
		buffer.push('<option value="'+whMdBVo.mdId+'">'+whMdBVo.mdNm+'</option>');
	});
	buffer.push('</select>');
	
	parent.append($(buffer.join('')));
	
	if(target!=undefined){
		restoreUniform('sysMdContainer');
		$(target).after(parent);		
		//setJsUniform(parent);
		var container=$('#sysMdContainer');
		if(container.scrollTop()>0){
			container.css('height', (container.height()+container.scrollTop()+5)+'px');
		}
		applyUniform('sysMdContainer');
	}
}<% // 시스템 모듈 담당자 추가 %>
function addMdPichList(target, whMdPichLVoList){
	if(whMdPichLVoList==null || whMdPichLVoList.length==0) return;
	$.each(whMdPichLVoList, function(index, item){
		whMdPichLVo=item.map;
		target.append('<option value="'+whMdPichLVo.idVa+'">'+whMdPichLVo.pichNm+'</option>');
	});
	
}<% // 시스템 모듈 처리유형 추가 %>
function addMdCatList(target, whCatGrpLVoList){
	if(whCatGrpLVoList==null || whCatGrpLVoList.length==0) return;
	$.each(whCatGrpLVoList, function(index, item){
		whCatGrpLVo=item.map;
		target.append('<option value="'+whCatGrpLVo.catNo+'">'+whCatGrpLVo.catNm+'</option>');
	});
}<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
}<% // 조회 %>
function searchForm(){
	var $form = $('#searchForm');	
	var mdSelect = $('#sysMdContainer select:last');
	var mdId=mdSelect.val();
	if(mdId==''){
		var obj=$(mdSelect).closest('li');
		var val=null;
		$.each($(obj).prevAll(), function(){
			val=$(this).find('select > option:selected').val();
			if(val!=''){
				mdId=val;
				return false;
			}
		});
	} 
	$form.find("input[name='mdId']").remove();
	if(mdId!='')
		$form.appendHidden({name:'mdId',value:mdId});
	$form[0].submit();
}<% // 검색 조건 초기화 %>
function searchReset(){	
	valueReset('searchFormTbl',['durCat','schOptWord']);
	selectSysMdList($('#sysMdContainer select:first'));
}<% // 목록 상세정보 %>
function openListDtl(statCd, direction){
	$('#upBtn, #downBtn').hide();
	$('#'+direction+'Btn').show();
	$('#evalArea').toggle();
}<% // [팝업] - 목록 %>
function listReqListPop(statCd){
	var url='./listReqListPop.do';
	var page=null;
	if(statCd=='P' || statCd=='C') page='hdl';
	else if(statCd=='W') page='dashbrd';
	else page='recv';
	if(page==null) return;
	url+='?page='+page;
	if(statCd!=undefined) 
		url+='&statCd='+statCd;
	
	var mdSelect = $('#searchForm2 #sysMdContainer select:last');
	var mdId=mdSelect.val();
	if(mdId==''){
		var obj=$(mdSelect).closest('li');
		var val=null;
		$.each($(obj).prevAll(), function(){
			val=$(this).find('select > option:selected').val();
			if(val!=''){
				mdId=val;
				return false;
			}
		});
	}
	if(mdId!='') url+='&mdId='+mdId;
	url+='&'+$('#searchForm2').serialize();
	
	dialog.open('listReqListDialog','<u:msg titleId="wh.jsp.req.small.title" alt="요청사항" />', url);
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<u:title titleId="wh.jsp.dashBrd.title" menuNameFirst="true" alt="처리현황" />
	
<% // 검색영역 %>
<jsp:include page="/WEB-INF/jsp/wh/help/listHelpSrch.jsp" />
</c:if><c:if test="${!empty isPlt && isPlt==true }">
<script type="text/javascript">
<!--
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<form id="searchForm" name="searchForm" action="${_uri}" >
<u:input type="hidden" id="menuId" value="${menuId}" />
<table class="ptltable" border="0" cellpadding="0" cellspacing="0"><tr><td class="head_ct" style="padding:2px 0 2px 0;"><table class="center" border="0" cellpadding="0" cellspacing="0">
<tr><!-- 날짜 및 요일 표기 S -->
<td><u:input type="hidden" id="durCat" value="reqDt"/><u:calendar id="durStrtDt" option="{end:'durEndDt'}" value="${!empty durStrtDt ? durStrtDt : param.durStrtDt}" /></td>
<td class="search_body_ct"> ~ </td>
<td><u:calendar id="durEndDt" option="{start:'durStrtDt'}" value="${param.durEndDt}" /></td>
<td><u:buttonS titleId="cm.btn.search" alt="검색" href="javascript:document.searchForm.submit();" /></td>
<!-- 날짜 및 요일 표기  E -->
</tr></table></td></tr></table>
<c:if test="${!empty param.colYn }"><u:input type="hidden" id="colYn" value="${param.colYn}" /></c:if>
<c:if test="${!empty param.hghtPx }"><u:input type="hidden" id="hghtPx" value="${param.hghtPx}" /></c:if>
<c:if test="${!empty param.bxId }"><u:input type="hidden" id="bxId" value="${param.bxId}" /></c:if>
</form></c:if>

<div class="groupdiv" >
<table id="groupTbl" border="0" cellpadding="0" cellspacing="0" style="width:100%;font-size:13px;">
<colgroup><col width="25%"/><col width="*"/><col width="10%"/><c:if test="${empty isPlt }"><col width="10%"/></c:if></colgroup>
<c:forEach items="${statCdList }" var="statCd" varStatus="status"><c:set var="map" value="${dashBrdMap}" scope="request" 
/><u:convertMap var="totalCnt" srcId="map" attId="${statCd }" type="value"
/><tr>
<td><strong><u:msg titleId="wh.option.statCd${statCd }" /></strong></td>
<td <c:if test="${!empty totalCnt && totalCnt>0}">onclick="listReqListPop('${statCd }');"</c:if>><u:set var="totalPer" test="${sumCnt>0 && !empty totalCnt }" value="${totalCnt/sumCnt*100 }" elseValue="0"/><div id="progressbar" style="width:99%;"><div style="background-color:${statCdColors[status.index]};width:${totalPer}%;"></div></div></td><td style="text-indent:10px;"><strong>${!empty totalCnt ? totalCnt : 0 }</strong></td>
<c:if test="${empty isPlt }"><td class="body_rt">&nbsp;<c:if test="${statCd eq 'E' && !empty totalCnt && totalCnt>0}"><u:buttonIcon id="downBtn" alt="상세정보" titleId="cm.btn.detl" image="ico_wdown.png" onclick="openListDtl('${statCd }', 'up');" /><u:buttonIcon id="upBtn" alt="상세정보" titleId="cm.btn.detl" image="ico_wup.png" onclick="openListDtl('${statCd }', 'down');" style="display:none;"/></c:if></td></c:if></tr></c:forEach></table></div>
<c:if test="${empty isPlt && !empty evalDtlList}">
<div class="listarea" id="evalArea" style="width:200px;display:none;">
<u:blank />
	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<colgroup><col width="*"/><col width="35%"/></colgroup>
	<tr><td class="head_ct"><u:msg titleId="wh.cols.req.eval" alt="평가"/></td><td class="head_ct"><u:msg titleId="cm.count" alt="건"/></td></tr>
<c:forEach items="${evalDtlList }" var="evalMap" varStatus="status">
<tr><td class="body_ct">${evalMap.evalNm }</td><td class="body_ct">${evalMap.totalCnt }</td></tr>
</c:forEach>
</table></div>
<% // 하단 버튼 %>
<u:buttonArea topBlank="true">
	<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" auth="R" />
</u:buttonArea></c:if>