<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
 import="java.util.regex.Matcher,java.util.regex.Pattern"%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<% // 파일 업로드 팝업 닫기 %>
function setInOut( str ){
	if(str=='in') loading('uploadForm', false);
	dialog.close('setBcInPop');
};

<% // 내보내기 %>
function setBcOutPop(){
	dialog.open('setBcOutPop', '<u:msg titleId="wb.jsp.setBcOutPop.title" alt="명함 내보내기" />', './setBcOutPop.do?menuId=${menuId}');	
};

<% // 파일 다운로드 %>
function downFile() {
	if (confirmMsg("wb.cfrm.setBcOut")) {
		var $form = $('<form>', {
			'method':'post',
			'action':'/wb/transBcOut.do',
			'target':'dataframe'
			}).append($('<input>', {
				'name':'menuId',
				'value':'${menuId}',
				'type':'hiden'
			}));
		$form.append($('<input>', {
			'name':'csvTypCd',
			'value':$("input[type='radio']:checked").val(),
			'type':'hiden'
		}));
		return;
		$(document.body).append($form);
		$form.submit();
	}
};

<% // 파일 업로드 %>
function fileUploadPop(){
	dialog.open('setBcInPop', '<u:msg titleId="wb.jsp.setBcInPop.title" alt="주소록 가져오기" />', './setBcInPop.do?menuId=${menuId}');	
};

$(document).ready(function() {
setUniformCSS();
});
-->
</script>

<u:msg titleId="cm.msg.preparing" var="msg" alt="준비중.." />

<u:title titleId="wb.jsp.setBcInOut.title" alt="명함IN/OUT" menuNameFirst="true"/>

<u:title titleId="wb.jsp.setBcInOut.subTitle01" type="small" alt="내보내기">
<u:titleButton titleId="cm.btn.fileOut" onclick="setBcOutPop();" auth="R"/>
</u:title>

<div class="headbox">
	<dl>
	<dd class="headbox_body"><u:msg titleId="wb.jsp.setBcInOut.tx01" alt="* 본인의 작업 리스트가 CSV 파일 형태로 만들어집니다." /></dd>
	</dl>
</div>

<u:blank />

<u:title titleId="wb.jsp.setBcInOut.subTitle02" type="small" alt="가져오기">
<u:titleButton titleId="cm.btn.fileIn" onclick="fileUploadPop();" auth="R"/>
</u:title>
<div class="headbox">
	<dl>
	<dd class="headbox_body"><u:msg titleId="wb.jsp.setBcInOut.tx02" alt="* 파일을 이용하여 작업을 생성합니다." /></dd>
	<dd class="headbox_body"><u:msg titleId="wb.jsp.setBcInOut.tx03" alt="* 가져올 파일을 먼저 선택하십시오. (CSV 파일 형태만 가능)" /></dd>
	</dl>
</div>

<u:blank />
<%-- 
<u:title titleId="wb.jsp.setBcInOut.subTitle03" type="small" alt="내보내기">
<u:titleButton titleId="cm.btn.fileSch" onclick="alert('${msg}');" />
</u:title> --%>
<!-- 
<output id=list>
</output>
<hr />
<table id=contents style="width:100%; height:400px;" border>
</table> -->
