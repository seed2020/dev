<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	request.setAttribute("mdRids", new String[]{ "mail","ap","bb","ct","dm","wb","wc","wr","wv","n01","n02","n03","n04","n05","n06","n07","n08","n09","n10","n11" });
%><script type="text/javascript">
function callbackMdRid(mdRid){
	$("#setMnuGrpPop #mdRid").val(mdRid);
	dialog.close("setMdRidDialog");
}
</script>
<div style="width:462px; padding-right:-4px;">

<div style="height:214px;">
<c:forEach items="${mdRids}" var="mdRid" >
<div style="background:url('/images/mobile/ico${mdRid}.png') no-repeat center; background-size:contain; float:left; width:60px; height:60px; border:2px solid ${param.mdRid eq mdRid ? '#8bd1f9' : 'white'}; margin-right:2px; margin-bottom:4px; background-color:#3d5768; cursor:pointer;"
	title="${mdRid.toUpperCase()}" onclick="callbackMdRid('${mdRid.toUpperCase()}');"
></div>
</c:forEach>
</div>

<u:buttonArea>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>