<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<% // [하단버튼:저장] - 컬럼 순서 저장 %>
function saveMobReg(){
	var data=getIframeContent('mobRegFrm').getMobData();
	
	var mobRegTypCd=data.mobRegTypCd;
	var tabVa=data.tabVa;
	var loutVa=data.loutVa;
	
	callAjax('./transMobRegListAjx.do?menuId=${menuId}', {formNo:'${param.formNo}', mobRegTypCd:mobRegTypCd, tabVa:tabVa, loutVa:loutVa, saveOptCd:data.saveOptCd!=undefined ? data.saveOptCd : null}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			dialog.close('setMobRegDialog');
		}
	});
}
$(document).ready(function() {
});
//-->
</script>
<div style="width:560px;">

<iframe id="mobRegFrm" name="mobRegFrm" src="./setMobRegFrm.do?menuId=${menuId}&formNo=${param.formNo }" style="width:100%;min-height:610px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>

<u:buttonArea topBlank="true">
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:;" onclick="saveMobReg();" auth="A" />
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>

</div>
