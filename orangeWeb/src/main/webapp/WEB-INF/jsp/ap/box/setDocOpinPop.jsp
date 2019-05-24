<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--<%
// 문서에 의견 정보 리턴 %>
function getOpinHiddenData(){
	var data = {};
	$("#docDataArea #docOpinArea input").each(function(){
		data[$(this).attr('name')] = this.value;
	});
	return data;
}<%
// 확인 버튼 클릭 %>
function setOpinInfo(){
	var oldApvOpinCont = $("#docDataArea #docOpinArea input[name=apvOpinCont]").val();
	if(!validator.validate('setOpinForm')){
		$("#docDataArea #docOpinArea input[name=apvOpinCont]").val(oldApvOpinCont==null ? '' : oldApvOpinCont);
		return;
	}
	var censr = "${param.reqCensr}";
	var param = new ParamMap().getData($("#setOpinForm")[0]);
	if(censr != 'Y'){<%// 결재의견 - 심사요청 의견 아님 %>
		var $area = $("#docDataArea #docOpinArea");
		$area.html('');
		param.each(function(key, va){
			$area.append("<input type='hidden' name='"+key+"' value='"+escapeValue(va)+"' />");
		});<%
		// items 영역 의견 표시칸에 %>
		if(param.get("apvOpinDispYn")!='Y' || param.get("apvOpinCont")==''){
			$("#itemsArea td#opinView tr[data-myOpion='Y']").remove();
		} else {
			var $opin = $("#docArea div[data-name='itemsArea'] td#opinView");
			if($opin.length>0){
				var $tr = $opin.find("tr[data-myOpion='Y']");
				if($tr.length>0){
					$tr.remove();
				}
				$opin.find("tbody").append('<tr data-myOpion="Y"><td style="min-width:70px;">${sessionScope.userVo.userNm}</td><td>'+escapeHtml(param.get("apvOpinCont"))+'</td></tr>');
			}
		}
	} else {<%// 심사요청 의견의 경우 %>
		var $area = $("#docDataArea #docOpinArea");
		$area.html('');
		$area.append("<input type='hidden' name='pichOpinCont' value='"+escapeValue(param.get('apvOpinCont'))+"' />");
	}
	dialog.close("setDocOpinDialog");
}<%
// 의견 저장 버튼%>
function saveOpinInfo(){
	var $form = $("#setOpinForm");
	var apvOpinCont = $form.find("#apvOpinCont").val();
	var apvOpinDispYn = $form.find("#apvOpinDispYnForPop").val();
	var data = {process:"processSaveOpin", apvNo:"${param.apvNo}", apvLnPno:"${param.apvLnPno}", apvLnNo:"${param.apvLnNo}", apvOpinCont:apvOpinCont, apvOpinDispYn:apvOpinDispYn==null ? '' : apvOpinDispYn};
	
	callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", data, function(data){
		if(data.message != null) alert(data.message);
		if(data.result == 'ok'){
			location.replace(location.href);
		}
	});
	
}<%
// onload %>
$(document).ready(function() {
	var data = getOpinHiddenData();
	var $form = $("#setOpinForm");
	if(data["${param.reqCensr=='Y' ? 'pichOpinCont' : 'apvOpinCont'}"]!=null) $form.find("#apvOpinCont").val(data["${param.reqCensr=='Y' ? 'pichOpinCont' : 'apvOpinCont'}"]);
	if('${param.reqCensr}'!='Y'){
		if(data['apvOpinDispYn']=='Y' || (data['apvOpinDispYn']=='' && '${optConfigMap.opinDftDisp}'=='Y')){
			$form.find("[name='apvOpinDispYn']").checkInput(true);
		}
	}
});
//-->
</script>

<div style="width:600px">
<form id="setOpinForm">

<u:listArea>
	<tr>
	<td width="17%" class="head_ct"><c:if
		test="${param.reqCensr!='Y'}"><u:msg titleId="ap.jsp.doc.apvOpin" alt="결재의견" /></c:if><c:if
		test="${param.reqCensr=='Y'}"><u:msg titleId="ap.btn.reqCensrOpin" alt="심사요청의견" /></c:if></td>
	<td width="83%"><u:textarea id="apvOpinCont" value="" titleId="ap.jsp.doc.apvOpin" maxByte="800" rows="5" style="width:97%" /></td>
	</tr>
	
	<tr<c:if test="${param.opinInDoc != 'Y'}" > style="display:none"</c:if>>
	<td class="head_ct"><u:msg titleId="ap.jsp.doc.opinDisp" alt="의견표시" /></td>
	<td style="padding-left:1px;"><table border="0" cellpadding="0" cellspacing="0"><tbody><tr>
		<u:checkbox id="apvOpinDispYnForPop" name="apvOpinDispYn" value="Y" titleId="ap.jsp.doc.opinDispDetl" />
		</tr></tbody></table></td>
	</tr>
</u:listArea>

<u:buttonArea><c:if
		test="${param.bxId eq 'waitBx' and optConfigMap.saveOpin eq 'Y' and not empty param.apvNo}">
	<u:button titleId="ap.cfg.saveOpin" onclick="saveOpinInfo();" alt="의견 저장" auth="W" />
	<li style="width:10px; float:left" >&nbsp;</li></c:if>
	<u:button titleId="cm.btn.confirm" onclick="setOpinInfo();" alt="확인" auth="W" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>
