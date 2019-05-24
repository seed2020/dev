<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="wt.jsp.setTaskInOut.title" alt="작업IN/OUT" menuNameFirst="true"/>

<u:title titleId="wt.jsp.setTaskInOut.subTitle01" type="small" alt="내보내기">
	<u:titleButton titleId="cm.btn.fileOut" onclick="" />
</u:title>

<div class="headbox">
<dl>
<dd class="headbox_body"><u:msg titleId="wt.jsp.setTaskInOut.tx01" alt="* 본인의 작업 리스트가 CSV 파일 형태로 만들어집니다." /></dd>
</dl>
</div>

<u:blank />

<u:title titleId="wt.jsp.setTaskInOut.subTitle02" type="small" alt="가져오기">
	<u:titleButton titleId="cm.btn.fileIn" onclick="" />
</u:title>
<div class="headbox">
<dl>
<dd class="headbox_body"><u:msg titleId="wt.jsp.setTaskInOut.tx02" alt="* 파일을 이용하여 작업을 생성합니다." /></dd>
<dd class="headbox_body"><u:msg titleId="wt.jsp.setTaskInOut.tx03" alt="* 가져올 파일을 먼저 선택하십시오. (CSV 파일 형태만 가능)" /></dd>
</dl>
</div>

<u:blank />

<u:title titleId="wt.jsp.setTaskInOut.subTitle03" type="small" alt="내보내기">
	<u:titleButton titleId="cm.btn.fileSch" onclick="" />
</u:title>
