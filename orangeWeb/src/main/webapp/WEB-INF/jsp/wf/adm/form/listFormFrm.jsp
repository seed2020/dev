<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="paramsForList" excludes="formNo" />
<script type="text/javascript">
<!--
<% // [하단버튼:메뉴등록] - 팝업 %>
function setMnuPop(val){
	var popTitle=val=='M' ? '<u:msg titleId="wf.btn.mob.mnuReg" alt="모바일 메뉴등록" />' : '<u:msg titleId="wf.btn.web.mnuReg" alt="메뉴등록" />';
	parent.dialog.open('setMnuDialog', popTitle,'./setMnuPop.do?menuId=${menuId}&formNo=${param.formNo}&valUM='+val);
	parent.dialog.onClose("setMnuDialog", function(){parent.dialog.close('setFldDialog');});
}
<% // [하단버튼:삭제] 삭제 %>
function delFormList() {
	var arr = getCheckedValue("listArea", "cm.msg.noSelect");<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
	if (arr != null && confirmMsg("cm.cfrm.del")) {<% // 삭제하시겠습니까? %>
	
		callAjax('./getUseFormAjx.do?menuId=${menuId}', {formNos:arr}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				delFormAjx(arr);
			}
			if(data.result=='use' && data.formJsonString!=null){
				var jsonString=data.formJsonString;
				var jsonData=JSON.parse(jsonString);
				var buffer=[];
				$.each(jsonData, function(idx, json){
					buffer.push(json['formNm']);					
				});
				if(buffer.length>0 && confirmMsg('wf.msg.del.use.forms', [buffer.join(',')])){ // wf.msg.del.use.forms="{0}"이 사용중입니다. 그래도 삭제하시겠습니까?
					delFormAjx(arr);
				}
				
			}
		});
		
	}
}
<% // [AJAX] 양식 - 삭제 %>
function delFormAjx(arr){
	callAjax('./transFormDelAjx.do?menuId=${menuId}', {formNos:arr}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			location.href = './listFormFrm.do?${paramsForList}';
		}
	});
}
<% // [등록화면버튼] - 기본정보 입력화면으로 이동 %>
function setForm(formNo){
	var url='./setFormFrm.do?${paramsForList}';
	if(formNo!=undefined) url+='&formNo='+formNo;
	location.href=url;
}
<% // [하단버튼:배열] %>
function getRightBtnList(){
	var $area = $("#rightBtnArea");
	return $area.find('ul')[0].outerHTML;
}<% // [하단버튼:이동] - 양식 이동 %>
function setMoveToFormGrpPop(){
	var arr = getCheckedValue("listArea", "cm.msg.noSelect");<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
	if(arr!=null) parent.dialog.open('setMoveToFormGrpDialog','<u:msg titleId="cm.btn.move" alt="이동" />','./setMoveToFormGrpPop.do?menuId=${menuId}&formNos='+arr.join(','));
}<% // [하단버튼:등록화면] - 등록화면 구성으로 이동 %>
function setRegForm(formNo){
	parent.location.href="./setRegForm.do?${paramsForList}&formNo="+formNo;
}<% // [하단버튼:목록화면] - 목록화면 구성으로 이동 %>
function setListForm(formNo){
	parent.location.href="./setListForm.do?${paramsForList}&formNo="+formNo;
}<% // [하단버튼:배포] - 선택된 양식을 배포(실제 테이블 구성) %>
function deployForm(){
	var arr = getCheckedValue("listArea", "cm.msg.noSelect");<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
	if (arr != null && confirmMsg("wf.cfrm.deploy.update")) {<% // 변경사항이 있으면 해당 내용이 반영됩니다.\n배포 하시겠습니까? %>
		callAjax('./transDeployAjx.do?menuId=${menuId}', {formNos:arr}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.href = './listFormFrm.do?${paramsForList}';
			}
		});
	}
}<% // [하단버튼:배포이력] - 배포이력 팝업 %>
function listDeployPop(){
	parent.dialog.open('listDeployDialog','<u:msg titleId="wf.btn.deploy.hst" alt="배포이력" />','./listDeployPop.do?menuId=${menuId}');
}
<%
//[팝업] - 양식 업로드 %>
function setFormUploadPop(){
	var arr = getCheckedValue("listArea", null);
	var url='./setFormUploadPop.do?menuId=${menuId}';
	if(arr!=null){
		if(arr.length>1){
			alertMsg('wf.msg.select.one'); // wf.msg.select.one=1개만 선택 가능합니다.
			return;
		}
		url+='&formNo='+arr.join('');
	}
	url+='&grpId=${param.grpId}';
	parent.dialog.open('setFormUploadDialog','<u:msg titleId="wf.btn.form.upload" alt="양식 업로드" />', url);
}
<% // 양식 다운로드 %>
function formDownload() {
	var arr = getCheckedValue("listArea", "cm.msg.noSelect");<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
	if(arr==null) return; 
	var $form = $('<form>', {
			'method':'post',
			'action':'./formDownload.do?menuId=${menuId}',
			'target':'dataframeForFrame'
		}).append($('<input>', {
			'name':'menuId',
			'value':'${empty menuId ? param.menuId : menuId}',
			'type':'hidden'
		})).append($('<input>', {
			'name':'formNos',
			'value':arr.join(','),
			'type':'hidden'
		}));
	$('form[action="./formDownload.do"]').remove();
	$(document.body).append($form);
	$form.submit();
}<% // 페이지 리로드 %>
function reloadForm(){
	reload();
}
$(document).ready(function() {
	setUniformCSS();
	parent.applyFormBtn();
});
//-->
</script>

<div style="padding:10px;">
<form id="searchForm" name="searchForm" action="${_uri }" >
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="grpId" value="${param.grpId}" />

<% // 검색영역 %>
<u:searchArea>
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td class="search_tit"><u:msg titleId="wf.cols.form.nm" alt="양식명" /></td>
		<td><u:input type="hidden" id="schCat" value="formNm" 
		/><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 200px;" onkeydown="if (event.keyCode == 13) searchForm.submit();" /></td>	
		</tr>
		</table></td>
		<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
</u:searchArea>
</form>

<u:listArea id="listArea" colgroup="3%,,9%,8%,10%,19%,13%">

	<tr id="headerTr">
		<th class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></th>
		<th class="head_ct"><u:msg titleId="wf.cols.form.nm" alt="양식명" /></th>
		<th class="head_ct"><u:msg titleId="wf.cols.md" alt="외부모듈" /></th>
		<th class="head_ct"><u:msg titleId="cols.stat" alt="상태" /></th>
		<th class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></th>
		<%-- <th class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></th> --%>
		<th class="head_ct"><u:msg titleId="wf.cols.date.deploy" alt="배포일시" /></th>
		<th class="head_ct"><u:msg titleId="wf.cols.set.screen" alt="화면구성" /></th>
	</tr>
	<c:if test="${fn:length(wfFormBVoList) == 0}">
		<tr>
		<td class="nodata" colspan="7"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
	<c:forEach items="${wfFormBVoList}" var="wfFormBVo" varStatus="status">
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" >
		<td class="bodybg_ct"><input type="checkbox" value="${wfFormBVo.formNo}"/></td>
		<td class="body_lt"><a href="javascript:;" onclick="setForm('${wfFormBVo.formNo}');"><u:out value="${wfFormBVo.formNm }"/></a></td>
		<td class="body_ct"><u:out value="${pageRecSetupCdMap[wfFormBVo.mdTypCd] }"/></td>
		<td class="body_ct"><u:msg titleId="wf.option.statCd${wfFormBVo.statCd }" alt="상태"/></td>
		<td class="body_ct"><u:yn value="${wfFormBVo.useYn }" yesId="cm.option.use" noId="cm.option.notUse" alt="사용여부"/></td>
		<%-- <td class="body_ct"><u:out value="${wfFormBVo.regrNm }"/></td> --%>
		<td class="body_ct"><u:out value="${wfFormBVo.deployDt }" type="longdate"/></td>		
		<td class="body_ct"><u:buttonS href="javascript:void(0)" onclick="setRegForm('${wfFormBVo.formNo }');" alt="등록" titleId="cols.reg" auth="A"
		/><c:if test="${!empty wfFormBVo.regYn && wfFormBVo.regYn eq 'Y' }"
		><u:buttonS href="javascript:void(0)" onclick="setListForm('${wfFormBVo.formNo }');" alt="목록" titleId="cols.list" auth="A"
		/></c:if></td>
	</tr>
	</c:forEach>
	
</u:listArea>
<u:blank />
<u:pagination />
</div>
<% // 하단 버튼 %>
<u:buttonArea id="rightBtnArea" style="display:none;">
	<u:button titleId="wf.btn.form.download" alt="양식다운로드" onclick="formDownload();" auth="A" />
	<u:button titleId="wf.btn.form.upload" alt="양식업로드" onclick="setFormUploadPop();" auth="A" />
	<u:button id="btnMove" href="javascript:setMnuPop('U');" titleId="wf.btn.web.mnuReg" alt="메뉴등록" auth="A" />
	<c:if test="${mobileEnable == 'Y' }">
		<u:button titleId="wf.btn.mob.mnuReg" alt="모바일 메뉴등록" onclick="setMnuPop('M');" auth="A" />
	</c:if>
	<u:button id="btnMove" href="javascript:deployForm();" titleId="wf.btn.deploy" alt="배포" auth="A" />
	<u:button id="btnMove" href="javascript:listDeployPop();" titleId="wf.btn.deploy.hst" alt="배포이력" auth="A" />
	<u:button id="btnMove" href="javascript:setMoveToFormGrpPop();" titleId="cm.btn.move" alt="이동" auth="A" />
	<u:button id="btnReg" href="javascript:setForm();" titleId="cm.btn.write" alt="등록" auth="A" />
	<u:button id="btnDel" href="javascript:delFormList();" titleId="cm.btn.del" alt="삭제" auth="A" />
</u:buttonArea>