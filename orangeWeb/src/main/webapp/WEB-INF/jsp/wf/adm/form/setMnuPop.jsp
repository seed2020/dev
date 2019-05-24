<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<c:if test="${!empty ptCompBVoList }">
<% // [메뉴] - 회사 변경 %>
function chnComp(compId){
	reloadFrame('setMnuFrm', './setMnuFrm.do?menuId=${menuId }&valUM=${param.valUM }&paramCompId='+compId);
}<% // [양식] - 회사 변경 %>
function chnFormComp(compId){
	if(compId===undefined) compId='';
	reloadFrame('formListAreaFrm', './mnuGrpFrm.do?menuId=${menuId}&callback=cancelHandler&treeSelectOption=2&paramCompId='+compId);
}
</c:if>
<% // 메뉴 추가 %>
function addMnu() {
	var selectList=getIframeContent('formListAreaFrm').getSelectedForm(true);
	if(selectList.length==0){
		alertMsg('cm.msg.noSelect');
		<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
		return;
	}
	var formNos=[];
	$.each(selectList, function(idx, form){
		formNos.push(form['grpId']);
	});
	var mnuArrs = getIframeContent('setMnuFrm').getSelectedMnu();
	if(mnuArrs==null){
		alertMsg('wf.msg.fld.noSelect'); <% // 선택한 폴더가 없습니다. %>
		return;
	}
	callAjax('./transMnuAjx.do?menuId=${menuId}', {formNos:formNos, valUM:'${param.valUM}', mnuId: mnuArrs['mnuId'], mnuGrpId: mnuArrs['mnuGrpId']}, function (data) {
		if (data.message != null) {
			alert(data.message);
		}
		if(data.result=='ok'){
			getIframeContent('setMnuFrm').reloadMnuFrm(null, mnuArrs['mnuId']);
		}
	});
}
<% // 메뉴 삭제 %>
function delMnu() {
	var mnuArrs = getIframeContent('setMnuFrm').getSelectedMnu();
	if(mnuArrs==null){
		alertMsg('wf.msg.fld.noSelect'); <% // 선택한 폴더가 없습니다. %>
		return;
	}

	if (confirmMsg("bb.msg.cfrm.mnu.del")) {<% // bb.msg.cfrm.mnu.del=선택한 메뉴를 삭제하시겠습니까? %>
		callAjax('./transMnuDelAjx.do?menuId=${menuId}', {valUM:'${param.valUM}', mnuId: mnuArrs['mnuId']}, function (data) {
			if (data.message != null) {
				alert(data.message);
			} else {
				getIframeContent('setMnuFrm').reloadMnuFrm();
			}
		});
	}
}<% // 저장 후 트리 리로드 %>
function reloadMnuFrm(mnuPid, mnuId){
	getIframeContent('setMnuFrm').reloadMnuFrm(mnuPid, mnuId);
}
<% // [팝업:폴더등록, 폴더수정] - 저장 버튼 %>
function saveFld(){
	if(validator.validate('setFldPop')){
		var $form = $('#setFldPop');
		$form.find("input[name='callback']").remove();
		$form.appendHidden({name:'callback',value:'reloadMnuFrm'});
		$form.attr('action','/pt/adm/mnu/transFld.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form.submit();
		dialog.close('setFldDialog');
	}
}
function cancelHandler(){}
$(document).ready(function() {
});
//-->
</script>
<div style="width:800px;">

<c:if test="${!empty ptCompBVoList }">
<div class="front notPrint">
	<div class="front_left">		
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td style="padding:3px 4px 0 0px"><u:title titleId="pt.cols.openScop" alt="공개범위" menuNameFirst="true" /></td>
			<td class="width5"></td>
				<td class="frontinput">
					<select id="compId" name="compId" <u:elemTitle titleId="cols.comp" /> onchange="chnComp(this.value);">
						<u:option value="all" titleId="cm.option.allComp" selected="${empty paramCompId && status.first }"/>
						<c:forEach items="${ptCompBVoList}" var="ptCompBVo" varStatus="status">
							<u:option value="${ptCompBVo.compId}" title="${ptCompBVo.rescNm}" checkValue="${paramCompId }" />
						</c:forEach>
					</select>
				</td>
	 		</tr>
		</table>
	</div>
	<div class="front_right">		
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td style="padding:3px 4px 0 0px"><u:title titleId="wf.cols.select.comp" alt="회사선택" menuNameFirst="true" /></td>
			<td class="width5"></td>
				<td class="frontinput">
					<select id="formCompId" name="formCompId" <u:elemTitle titleId="cols.comp" /> onchange="chnFormComp(this.value);">
						<c:forEach items="${ptCompBVoList}" var="ptCompBVo" varStatus="status">
							<u:option value="${ptCompBVo.compId}" title="${ptCompBVo.rescNm}" checkValue="${paramCompId }" selected="${empty paramCompId && status.first }" />
						</c:forEach>
					</select>
				</td>
	 		</tr>
		</table>
	</div>
</div>
</c:if>

<u:boxArea className="wbox" noBottomBlank="true"
	style="float:left; width:50%;padding:2px;"
	outerStyle="height:400px;overflow:hidden;"
	innerStyle="width:96%; margin:0 auto 0 auto; padding:10px 0 0 0;">
	
	<u:set var="setMnuUrl" test="${!empty paramCompId }" value="./setMnuFrm.do?menuId=${menuId }&valUM=${param.valUM }&paramCompId=${paramCompId }" elseValue="./setMnuFrm.do?menuId=${menuId }&valUM=${param.valUM }"/>
	<iframe id="setMnuFrm" name="setMnuFrm" src="${setMnuUrl }" style="width:100%;min-height:410px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
	
</u:boxArea>

<div style="float:left; width:6%; text-align:center; margin:190px 0 0 0;">
	<table style="margin:0 auto 0 auto;" border="0" cellpadding="0" cellspacing="0">
		<tr><td><a href="javascript:addMnu();"<u:elemTitle titleId="cm.btn.selAdd" alt="선택추가" type="image" />><img src="${_cxPth}/images/${_skin}/ico_left.png" width="20" height="20" /></a></td></tr>
		<%-- <tr><td class="height5"></td></tr>
		<tr><td><a href="javascript:delMnu();"<u:elemTitle titleId="cm.btn.selDel" alt="선택삭제" type="image" />><img src="${_cxPth}/images/${_skin}/ico_right.png" width="20" height="20" /></a></td></tr> --%>
	</table>
</div>

<u:boxArea className="wbox" noBottomBlank="true"
	style="float:right; width:42%;padding:2px;"
	outerStyle="height:400px;overflow:hidden;"
	innerStyle="width:96%; margin:0 auto 0 auto; padding:10px 0 0 0;">
	<u:set var="setFormUrl" test="${!empty paramCompId }" value="./mnuGrpFrm.do?menuId=${menuId}&paramCompId=${paramCompId }&treeSelectOption=2&callback=cancelHandler" elseValue="./mnuGrpFrm.do?menuId=${menuId}&callback=cancelHandler"/>
	<iframe id="formListAreaFrm" name="formListAreaFrm" src="${setFormUrl }" style="width:100%;min-height:350px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>

</u:boxArea>

<u:buttonArea topBlank="true">
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>

</div>
