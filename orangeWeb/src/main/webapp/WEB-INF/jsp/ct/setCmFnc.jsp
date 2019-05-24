<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />

<script type="text/javascript">
<!--

function ctCrtCancel(){
	callAjax('./ajaxCtCrtCancel.do?menuId=${menuId}&fnc=${fnc}&catId=${catId}', {menuId:"${menuId}", ctId:"${ctId}"} , function(data){
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ct') {
			//strtDt:strtDt, endDt:endDt, dateSelect:dateSelect  fncMng
			location.href = data.menuUrl;
		}else{
			location.href = data.menuUrl;
		}
	});
	
}

function fncUp(){
	getIframeContent('fncTree').move('up',"${ctId}");
}

function fncDown(){
	getIframeContent('fncTree').move('down',"${ctId}");
}

function rightToLeft(id){
	
	if(typeof $("#ctFnc option:selected").val()!="undefined"){
		if($("#ctFnc option:selected").val() == "CTHOME" 
		|| $("#ctFnc option:selected").val() == "CTGUESTSEARCH"
		|| $("#ctFnc option:selected").val() == "CTMANAGEMENT"
		|| $("#ctFnc option:selected").val() == "CTSECESSION"){
			alert('<u:msg titleId="cm.msg.movCd.sys" alt="시스템 코드는 이동 할 수 없습니다." />');
		}else{
		
		}
			var ctId = $("#ctId").val();
			var fncId =  $("#ctFnc option:selected").val();
			var fncRescId = $("#ctFnc option:selected").attr("id");
			var tree = getIframeContent('fncTree').getTreeRightToLeftData('reg');
			var fncPid = tree.id;
			var fncfldType = tree.fldTyp;
			if(fncPid != 'fail'){
				var $frm = $("#fncRightToLeft");
				$frm.attr('method','post');
				$frm.attr('action','./transFncRightToLeft.do?menuId=${menuId}&fnc=${fnc}&catId=${catId}&ctId='+ctId+'&fncId='+fncId+'&fncRescId='+fncRescId+'&fncPid='+fncPid +'&fncfldType='+fncfldType);
				$frm.attr('target','dataframe');
				$frm.submit();
			}
			
			var iValue = $("#ctFnc option:selected").text().indexOf("*");
			if(iValue == -1 && fncPid != 'fail'){
				$("#ctFnc option:selected").remove();	
			}
		
		
	//	$("#choiGrp").append("<option value='"+$("#myGrp option:selected").val()+"'>"+$("#myGrp option:selected").text()+"</option>");
		
	}
}

function leftToRight(){
	getIframeContent('fncTree').delFld("${ctId}");
}



<%// [트리: 트리클릭] - 오른쪽 리스트 열기 %>
function openCdList(id){
	/* var src = './listBcFrm.do?menuId=${menuId}';
	if(id !='ROOT'){
		src+= '&schFldTypYn=F&schFldId='+id;
	}
	$("#cdList").attr('src', src);
	return; */
	$("#cdList").attr('src', './listCtClsFrm.do?menuId=${menuId}&fnc=${fnc}&catId=${catId}&typ=C&catId='+id);
	//gEmptyRight = false;
};

<%// 뒤로 버튼 %>
function formPrev(){
	$form = $('#setCmFncForm');
    $form.attr('method','post');
    $form.attr('action','./setCm.do?menuId=${menuId}&fnc=${fnc}&catId=${catId}&ctId=${ctId}');
    $form.submit();
	
}

<%// 최종 저장 버튼 %>
function formSubmit(){
	$form = $('#setCmFncForm');
    $form.attr('method','post');
    $form.attr('action','./transFinalSave.do?menuId=${menuId}&ctId=${ctId}&fnc=${fnc}&catId=${catId}');
    $form.submit();
	
}

<%// [이름변경 (이름변경)] - 저장 버튼 %>
function saveReNm(){
	var tree = getIframeContent('fncTree').getTreeData('mod');
	if(tree != null){
		if(validator.validate('setNmForm')){
			var $frm = $('#setNmForm');
			$frm.attr('method','post');
			$frm.attr('action','./transReNameSave.do?ctFncUid=' + tree.id + '&fncRescId=' + tree.fncRescId +'&ctId=${ctId}&menuId=${menuId}&fnc=${fnc}&catId=${catId}');
			$frm.attr('target','dataframe');
			$frm.submit();
		}
	}
};


<%// [팝업:폴더등록, 폴더수정] - 저장 버튼 %>
function saveFld(){
	if(validator.validate('setFldForm')){
		var $frm = $('#setFldForm');
		$frm.attr('method','post');
		$frm.attr('action','./transFncFldSave.do');
		$frm.attr('target','dataframe');
		$frm.submit();
	}
};

<%// [버튼] 기능 이름변경 %>
function setNm() {
	var tree = getIframeContent('fncTree').getTreeData('mod');
	if(tree != null){
		dialog.open('setNmDialog','<u:msg titleId="cm.btn.chnNm" alt="이름 변경" />','./setNmPop.do?menuId=${menuId}&fnc=${fnc}&catId=${catId}&fncRescId=' + tree.fncRescId);
	}
}


<%// [버튼] 폴더등록 %>
function regFld() {
	var tree = getIframeContent('fncTree').getTreeData('reg');
	if(tree!=null){<%//pt.jsp.setCd.regFld=폴더등록%>
		dialog.open('setFncFldDialog','<u:msg titleId="pt.jsp.setCd.regFld"/>','./setFncFldPop.do?menuId=${menuId}&fnc=${fnc}&catId=${catId}&mode=reg&ctId=${ctId}&ctFncId='+tree.id );
	}
};

<%// [버튼] 폴더및 기능 전체삭제 %>
function delAllFld() {
	getIframeContent('fncTree').delAllFld("${ctId}","${fnc}","${catId}");
};

<%// [메뉴팝업:저장 후처리] - 트리 리로드 %>
function reloadTree(ctFncUid){
	getIframeContent('fncTree').reload('./listCtFncFrm.do?menuId=${menuId}&fnc=${fnc}&catId=${catId}&ctId=${ctId}&ctFncUid='+ctFncUid);
	dialog.close('setFncFldDialog');
	dialog.close('setNmDialog');
	
};

function modFld() {
	dialog.open('setFldPop','<u:msg titleId="cm.btn.modFld" alt="폴더 수정" />','./setFldPop.do?menuId=${menuId}&fnc=mod&catId=${catId}');
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title title="${menuTitle }" alt="기능선택" menuNameFirst="true"/>

<form id ="fncRightToLeft" >
</form> 

<form id="setCmFncForm" >
	<u:input type="hidden" id="catId" value="${catId}"/>
	<u:input type="hidden" id="ctId" value="${ctId}"/>
	<u:input type="hidden" id="fncCatId" />
	

</form>

<% // 상단 도움말 %>
<div class="headbox">
<dl>
<dd class="headbox_tit"><u:msg titleId="ct.msg.create.guideTit" alt="커뮤니티 운영에 필요한 기능을 선택, 변경하고 이름 및 순서를 변경할 수 있습니다." /></dd>
<dd class="headbox_body"><u:msg titleId="ct.msg.create.guideInfo1" alt="- 왼쪽에서 필요한 기능을 클릭하여 선택한 다음 '추가' 버튼을 누르십시오." /></dd>
<dd class="headbox_body"><u:msg titleId="ct.msg.create.guideInfo2" alt="- 서비스메뉴는 이름변경이 불가능하며 기능메뉴만 이름변경이 가능합니다." /></dd>
</dl>
</div>

<u:blank />

<u:boxArea className="gbox" outerStyle="height:310px;padding:9px 12px 0 10px;" innerStyle="NO_INNER_IDV">

<!-- LEFT -->
<div class="left" style="width:48%; height: 310px;">
	<div class="titlearea">
		<div class="tit_left">
		<dl>
		<dd class="title_s"><u:msg titleId="ct.jsp.setCmFnc.subtitle02" alt="사용할 이름 변경" /></dd>
		</dl>
		</div>
		<div class="tit_right">
		<ul>
		<li class="ico">
			<u:titleIcon type="up" onclick="javascript:fncUp();" />
			<u:titleIcon type="down" onclick="javascript:fncDown();" />
		</ul>
		</div>
	</div>
	

	 <u:titleArea frameId="fncTree" frameSrc="./listCtFncFrm.do?menuId=${menuId}&ctId=${ctId}&fnc=${fnc}&catId=${catId}"
		outerStyle="height: 250px; overflow:hidden;" 
		innerStyle="NO_INNER_IDV;"
		noBottomBlank="true"
		frameStyle="width:100%; height:250px; overflow:auto;" />
		

	<table width="100%" border="0" cellpadding="0" cellspacing="0"><tbody>
	<tr>
	<td class="listicon_rt">
		<u:buttonS titleId="cm.btn.chnNm" alt="이름변경" onclick="setNm();" />
		<u:buttonS titleId="cm.btn.createFld" alt="폴더생성" onclick="regFld();" />
		<u:buttonS titleId="pt.jsp.setCd.delFld" alt="폴더삭제" onclick="delAllFld();" />
		</td>
	</tr>
	</tbody></table>
</div>

<!-- CENTER -->
<div class="left" style="float:left; width:4%; height: 310px;">
	<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0"><tbody>
	<tr>
	<td style="vertical-align: middle">
		<table align="center" border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><u:buttonIcon href="javascript:rightToLeft();" titleId="cm.btn.left" alt="왼쪽으로이동" /></td>
		</tr>

		<tr>
		<td class="height5"></td>
		</tr>

		<tr>
		<td><u:buttonIcon href="javascript:leftToRight();" titleId="cm.btn.right" alt="오른쪽으로이동" /></td>
		</tr>
		</tbody></table></td>
	</tr>
	</tbody></table>
</div>

<!-- RIGHT -->
<div class="right" style="width:48%; height: 310px;">
	<u:title titleId="ct.jsp.setCmFnc.subtitle01" type="small" alt="커뮤니티에서 사용할 기능 선택" />
	<select id="ctFnc" size="13" style="width:100%; height: 250px;">
		<c:forEach  var="ctFncMngList" items="${ctFncMngList}" varStatus="status">
			<c:if test="${ctFncMngList.useYn == 'Y' && ctFncMngList.ctMngYn == 'Y'}">
				<option id="${ctFncMngList.ctFncSubjRescId}" value="${ctFncMngList.ctFncId}">
					
					<c:if test="${ctFncMngList.mulChoiYn == 'Y'}" >*</c:if>${ctFncMngList.ctFncNm}
					
				</option>
				
			</c:if>
		</c:forEach>
	</select>

	<table width="100%" border="0" cellpadding="0" cellspacing="0"><tbody>
	<tr>
	<td class="body_lt"><u:msg titleId="ct.msg.create.spCharFnc" alt="* 표시된 기능은 여러번 선택 가능합니다." /></td>
	</tr>
	</tbody></table>
</div>

</u:boxArea>

<% // 하단 버튼 %>
<u:buttonArea>
	<c:if test="${empty mng}">
		<u:button titleId="cm.btn.back" alt="뒤로" href="javascript:formPrev();" />
	</c:if>
	<u:msg titleId="cm.msg.save.success" var="msg" alt="저장 되었습니다." />
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:formSubmit();" />
	<c:if test="${!empty mng}">
		<u:button titleId="cm.btn.cancel" alt="취소" href="javascript:history.go(-1);" />
	</c:if>
</u:buttonArea>

