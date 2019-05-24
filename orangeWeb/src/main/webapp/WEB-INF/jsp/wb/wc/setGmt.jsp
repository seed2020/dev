<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
//저장
function save(){
	var arrs = getIframeContent('cdList').getChecked();
	if(arrs == null ) return;
	$('#clsCd').val(arrs.clsCd);
	$('#cd').val(arrs.cd);
	if(confirmMsg("cm.cfrm.save")){
		var $form = $('#setRegForm');
		$form.attr('method','post');
		$form.attr('action','./transGmt.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form[0].submit();	
	}
};

<%// [트리: 트리클릭] - 오른쪽 리스트 열기 %>
function openCdList(clsCd){
	$("#cdList").attr('src', './listGmtFrm.do?menuId=${menuId}&clsCd='+clsCd);
};

//-->
</script>

<u:title titleId="wb.jsp.listBcFld.title" alt="명함폴더" menuNameFirst="true" />

<!-- LEFT -->
<div style="float:left; width:27.8%;">

<u:title type="small" alt="목록" title="국가"/>
<u:titleArea frameId="cdTree" frameSrc="./listGmtTreeFrm.do?menuId=${menuId}&clsCd=${param.clsCd }"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:590px;" />
</div>

	
<!-- RIGHT -->
<div style="float:right; width:71%;">

<u:title type="small" alt="목록" title="GMT목록"/>
<u:titleArea frameId="cdList" frameSrc="./listGmtFrm.do?menuId=${menuId}&clsCd=${param.clsCd }&cd=${param.cd }"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:590px;" />
	
</div>

<u:blank />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="save();" alt="저장" auth="W" />
</u:buttonArea>
<form id="setRegForm" name="setRegForm" >
	<u:input type="hidden" name="menuId" value="${menuId}" />
	<u:input type="hidden" id="clsCd" name="clsCd" />
	<u:input type="hidden" id="cd" name="cd" />
</form>
