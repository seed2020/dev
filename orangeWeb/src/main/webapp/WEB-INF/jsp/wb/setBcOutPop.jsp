<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%// 명함 조회 팝업 %>
function fnCsvOutType(obj){
	if($(obj).val() == 'sel'){
		var $form = $('#outForm');
		var pubBcYn = $form.find('input[name="pubBcYn"]').val(); 
		var url = './findBcPop.do?menuId=${menuId}&fncMul=Y&pagingYn=N';
		if(pubBcYn != undefined && pubBcYn=='Y') url+='&isBcListType=pub';
		else url+='&isBcListType=psn';
		url+='&selBcId='+$('#bcId').val();
		dialog.open('findBcPop','<u:msg titleId="wb.jsp.findBcPop.title" alt="명함선택" />', url);	
	}else{
		$("#bcId").val('');
		$('#selCntMsg').html('');
	}
};
<%// 명함 내보내기 구분 선택 %>
function fnCsvOutTypeSelect(obj){
	if(obj.value == ''){
		$('#fldCntMsg').hide();
		$('#allCntMsg').show();
	}else{
		var param={pubBcYn:'N', fldId:obj.value};
		<c:if test="${!empty isSelect && isSelect == true}">param.pubBcYn='Y';</c:if>	
		callAjax('./getTotalCntAjx.do?menuId=${menuId}', param, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				$('#outForm').find('input[name="bcId"]').val('');
				if(data.totalCnt != null){
					$('#fldCntMsg').text('('+data.totalCnt+')');					
				}
			}
		});
		$('#fldCntMsg').show();
		$('#allCntMsg').hide();
	}
}

//명함 선택
function fnBcSelect(detlViewType, callBack ){
	var objArr = getIframeContent(detlViewType+'Frm').fnSelArrs();
	if(objArr == null ) return;
	var bcIds = [];
	if(callBack != "" ){
		eval(callBack)(objArr);
	}else{
		objArr.each(function(index, obj){
			bcIds.push(obj.bcId);
		});		
	}
	if(bcIds.length > 0 ){
		$("#bcId").val(bcIds.join(','));
		var msg = "("+bcIds.length+")";
		$('#selCntMsg').html(msg);
		
		dialog.close('findBcPop');
	}
};
<% // 명함선택 %>
function getTotalCntAjx(fldId){
	var $form = $('#outForm');
	var param={pubBcYn:'N'};
	<c:if test="${!empty isSelect && isSelect == true}">param.pubBcYn='Y';</c:if>	
	if(fldId!=undefined) param.fldId=fldId;
	callAjax('./getTotalCntAjx.do?menuId=${menuId}', param, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			$form.find('input[name="bcId"]').val('');
			if(data.totalCnt != null){
				console.log('data.totalCnt : '+data.totalCnt);
				return data.totalCnt;
				//$form.find('span#allCntMsg').text('('+data.totalCnt+')');	
			}
			//$form.find('span#selCntMsg').text('');
		}
	});
	
}
<% // 파일 다운로드 %>
function downFile() {
	if (confirmMsg("wb.cfrm.setBcOut")) {
		var $form = $('#outForm');
		$form.attr('method','post');
		$form.attr('target','dataframe');
		$form[0].submit();	
		dialog.close('setBcOutPop');
	}
};
$(document).ready(function() {
	setUniformCSS();
}); 
//-->
</script>
<div style="width:400px">
<form id="outForm" method="post" action="./transBcOut.do?menuId=${menuId}">
	<u:input type="hidden" id="bcId" />
	<c:if test="${!empty isSelect && isSelect == true}">
		<u:input type="hidden" id="pubBcYn" value="Y"/>		
	</c:if>
	
	<u:title titleId="wb.jsp.setBcInOut.tx04" alt="내보낼 명함을 선택하세요." type="small"  />
	<select name="fldId" onchange="fnCsvOutTypeSelect(this);">
			<u:option value="" titleId="wb.cols.bcOut.allBc" alt="전체명함" selected="true"/>
			<u:option value="NONE" titleId="cm.msg.uncategorized" alt="미분류" />
			<c:forEach items="${wbBcFldBVoList}" var="list" varStatus="status" >
			<u:option value="${list.bcFldId}" title="${list.fldNm}" />
			</c:forEach>
			</select>
	<span id="allCntMsg" class="color_stxt" style="margin-left:5px;font-weight:bold;">(${recodeCount })</span>
	<span id="fldCntMsg" class="color_stxt" style="margin-left:5px;font-weight:bold;display:none;"></span>
	
	<u:blank /><u:blank />
	<u:title titleId="wb.jsp.setBcInOut.tx05" alt="내보낼 형식을 선택하세요." type="small"  />
	<u:radio name="csvTypCd" value="google" titleId="wb.cols.bcOut.google" checked="true"/><br />
		<u:radio name="csvTypCd" value="outlook" titleId="wb.cols.bcOut.outlook"/><br />
		<u:radio name="csvTypCd" value="outlook2003" titleId="wb.cols.bcOut.outlook2003"/><br />
		<u:radio name="csvTypCd" value="outlook2007" titleId="wb.cols.bcOut.outlook2007"/><br />
		<u:radio name="csvTypCd" value="outlook2010" titleId="wb.cols.bcOut.outlook2010"/><br />
		<u:radio name="csvTypCd" value="express" titleId="wb.cols.bcOut.express"/><br />
		<%-- <u:radio name="csvTypCd" value="contact" titleId="wb.cols.bcOut.contact"/><br /> --%>
		<u:radio name="csvTypCd" value="" titleId="wb.cols.bcOut.out"/><br />
	
<u:blank />
<u:buttonArea>
	<u:button titleId="cm.btn.fileOut" onclick="downFile();" alt="내보내기" auth="R"/>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>