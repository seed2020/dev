<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="nonPageParams" excludes="storId,pageNo,data"/>
<u:set var="callback" test="${!empty param.callback }" value="${param.callback }" elseValue="selectStorIds"/>
<u:set var="selectIds" test="${!empty param.multi && param.multi eq 'Y'}" value="fnSelArrs" elseValue="fnSelId"/>
<script type="text/javascript">
<!--<% // [하단버튼:선택] 확인 %>
function applyCfrm() {
	var arr = getIframeContent('listStorFrm').${selectIds}();
	if(arr == null ) return;
	${callback}(arr);
}
//-->
</script>
<div style="width:600px">
<iframe id="listStorFrm" name="listStorFrm" src="./listStorFrm.do?${nonPageParams }" style="width:100%; height:400px;" frameborder="0" marginheight="0" marginwidth="0" ></iframe>
<u:blank />
<% // 하단 버튼 %>
<u:buttonArea>
<u:button titleId="cm.btn.sel" alt="선택" onclick="applyCfrm();" auth="A" />
<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</div>