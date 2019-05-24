<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
	import="com.innobiz.orange.web.cm.utils.StringUtil"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

request.setAttribute("currTime", StringUtil.getCurrDateTime());

%><script type="text/javascript">
<!--
function saveOnec(statCd){
	
	var $form = $("#onceForm");
	if(!validator.validate('onceForm')){
		return;
	} else if(statCd=='temp'){
		if(!confirmMsg('wo.cfrm.temp')){
			return;
		}
	} else if(statCd=='askApv'){
		if($form.find('#formId').val() == ''){
			alertMsg('wo.msg.noApvFormId');<%--wo.msg.noApvFormId=결재 양식 ID가 설정되지 않았습니다.--%>
			return;
		}
		if(!confirmMsg('wo.cfrm.askApv')){
			return;
		}
	}
	
	setPosLoc();<%--이미지 위치--%>
	
	
	$form.find("#statCd").val(statCd);
	$form.attr('action', './transOnec.do?menuId=${menuId}&cat=${param.cat}');
	$form.attr('target', 'dataframe');
	$form.attr('enctype','multipart/form-data');
	saveFileToForm('wofiles', $form[0], null);<%-- submit 포함 --%>
}<%--
[기능] Positioning - 이미지 위치 hidden에 세팅 --%>
function setPosLoc(){
	var $area = $("#posImgArea");
	
	var myImg = $area.find('#dragStar');
	$area.find('input[name=posMyX]').val(parseInt(myImg.css('left')));
	$area.find('input[name=posMyY]').val(parseInt(myImg.css('top')));

	var othrImg = $area.find('#dragCircle');
	$area.find('input[name=posOthrX]').val(parseInt(othrImg.css('left')));
	$area.find('input[name=posOthrY]').val(parseInt(othrImg.css('top')));
}<%--
[히스토리 변경]--%>
function mngHistory(mode){
	var chks = $("#historyArea input[type='checkbox']:visible:checked");
	if(mode=='del' || mode=='mod'){
		if(chks.length==0) return;
	}
	if(mode=='mod'){
		if(chks.length>1){
			alertMsg('cm.msg.selectOneItem', 'History');<%--//cm.msg.selectOneItem=하나의 "{0}"(을)를 선택해 주십시요.--%>
			return;
		}
	}
	if(mode=='add' || mode=='mod'){
		dialog.open2("setOnecHisDialog", 'History', "./setOnecHisPop.do?menuId=${menuId}&cat=${param.cat}&mode="+mode, {focusId:'hisCont'});
	} else if(mode=='del'){
		var arr = [];
		chks.each(function(){
			arr.push($(getParentTag(this,'tr')));
		});
		arr.each(function(index, obj){
			$(obj).remove();
		});
	}
}<%--
[담당자(R,I,C,A,S,QA) 변경]--%>
function setOnecPich(typCd){
	var $area = $("#pichTypCd"+typCd+"Area");
	var uids = $area.find("input[name='pichTypCd"+typCd+"']").val(), data = [];;
	if(uids!=''){
		uids.split(',').each(function(index, va){
			data.push({userUid:va});
		});
	}
	searchUserPop({data:data, multi:true, mode:'search'}, function(arr){
		if(arr==null){
			$area.find('td:first').html('');
			$area.find("input[name='pichTypCd"+typCd+"']").val('');
		} else {
			var htmls=[], uids=[];
			arr.each(function(index, userVo){
				htmls.push("<a href=\"javascript:viewUserPop('"+userVo.userUid+"')\">"+userVo.rescNm+"</a>");
				uids.push(userVo.userUid);
			});
			$area.find('td:first').html(htmls.join(", "));
			$area.find("input[name='pichTypCd"+typCd+"']").val(uids.join(','));
		}
	});
}<%--
[부서 선택]--%>
function setOnecDept(){
	var $area = $("#onecDeptArea");
	var data = {orgId:$area.find("input[name=deptId]").val()};
	searchOrgPop({data:data, withSub:false}, function(orgVo){
		if(orgVo!=null){
			$area.find('td:first').html(orgVo.rescNm);
			$area.find("input[name=deptId]").val(orgVo.orgId);
		}
	});
}
$(document).ready(function() {
	setUniformCSS();
	
	dragElement(document.getElementById("dragStar"));
	dragElement(document.getElementById("dragCircle"));
});

<%--// 이하  - 별, 동그라미  drag & drop --%>
function dragElement(elmnt) {
	var pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
	
	elmnt.onmousedown = dragMouseDown;
	
	function dragMouseDown(e) {
		e = e || window.event;
		e.preventDefault();
		pos3 = e.clientX;
		pos4 = e.clientY;
		document.onmouseup = closeDragElement;
		document.onmousemove = elementDrag;
	}
	function elementDrag(e) {
		e = e || window.event;
		e.preventDefault();
		pos1 = pos3 - e.clientX;
		pos2 = pos4 - e.clientY;
		pos3 = e.clientX;
		pos4 = e.clientY;
		var topV = elmnt.offsetTop - pos2;
		topV = (topV < 2) ? 2 : (topV > 124) ? 124 : topV;
		elmnt.style.top = topV + "px";
		var leftV = elmnt.offsetLeft - pos1;
		leftV = (leftV < 4) ? 4 : (leftV > 155) ? 155 : leftV;
		elmnt.style.left = leftV + "px";
	}
	function closeDragElement() {
		document.onmouseup = null;
		document.onmousemove = null;
	}
}
//-->
</script>
<u:title alt="원카드 목록 / 원카드 관리" menuNameFirst="true" />


<div id="apvLnArea">
<div><img style="width: 216px; height: 72px; float: left;" src="/images/etc/DYNE.png"></div>
<div id="3row">
	<div style="float:right; width:60%; text-align:right;">
	<span style="font-size:16pt; color:red; font-weight: bold;">기 밀</span><br/>
	<span style="font-size:10pt;">${currTime}</span><span style="font-size:10pt; padding-left:8px">${sessionScope.userVo.userNm}</span><br/>
	</div>
	<div style="float:right; width:60%; text-align:right;">
	<table class="approvaltable" style="float: right;" border="0" cellspacing="1" cellpadding="0"><tbody>
	<tr><td class="approval_head" rowspan="3">결<br>재</td>
		<td class="approval_body" style="width: 77px;"></td><td class="approval_body" style="width: 77px;"></td><td class="approval_body" style="width: 77px;"></td></tr>
	<tr><td class="approval_img" style="width: 77px;"></td><td class="approval_img" style="width: 77px;"></td><td class="approval_img" style="width: 77px;"></td></tr>
	<tr><td class="approval_body" style="width: 77px;"></td><td class="approval_body" style="width: 77px;"></td><td class="approval_body" style="width: 77px;"></td></tr>
	</tbody></table>
	</div>
</div>
<div class="blank" id="bottomBlank"></div>
</div>


<form id="onceForm" enctype="multipart/form-data" method="post">
<input type="hidden" id="onecNo" name="onecNo" value="${woOnecBVo.onecNo}" />
<input type="hidden" id="statCd" name="statCd" value="${woOnecBVo.statCd}" />
<input type="hidden" id="formId" name="formId" value="${SYSMap.formId}" />

<div style="margin-top:-60px;">
<jsp:include page="./viewOnecInc.jsp" />
</div>

</form>

<div style="blank"></div>

<u:buttonArea><c:if
			test="${(woOnecBVo.ver > 1 or woOnecBVo.statCd eq 'apvd')}">
	<u:button titleId="cm.btn.save" alt="저장" onclick="saveOnec('modify')" auth="W" /></c:if><c:if
			test="${not (woOnecBVo.ver > 1 or woOnecBVo.statCd eq 'apvd')}">
	<u:button titleId="cm.btn.tmpSave" alt="임시저장" onclick="saveOnec('temp')" auth="W" /></c:if>
	<u:button titleId="wo.btn.askApv" alt="결재요청" onclick="saveOnec('askApv')" auth="W" />
	<u:button titleId="cm.btn.cancel" alt="취소" href="./listOnec.do?cat=${param.cat}&menuId=${menuId}" />
</u:buttonArea>
