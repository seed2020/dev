<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="sendFunc" test="${not empty param.sendFunc }" value="${param.sendFunc }" elseValue="setSelect"/>
<script type="text/javascript">
<!--
var params=null;
function getParameters(){
	if(params==null){
		params=new ParamMap();
		<c:forEach items="${paramEntryList}" var="entry" varStatus="status">
		params.put('${entry.key}', '${entry.value}');
		</c:forEach>
	}
	return param;
}
<% // 선택 %>
function selectList(){
	var selId = getIframeContent('sendFrm').getSelectId();
	if(selId == null){
		alertMsg("cm.msg.noSelect");<%//cm.msg.noSelect=선택한 항목이 없습니다.%>
		return;
	}
	${sendFunc }(selId);
} 
<% // [샘플] 게시판 선택시 - 각 페이지에서 처리%>
function setSelect(selId){
	var param=getParameters();
	callAjax('./saveSendAjx.do?menuId=${menuId}&${paramsForList}', param, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.sendNo != null) {
			dialog.open('setSendWritePop', '<u:msg titleId="dm.jsp.sendWrite.title" alt="보내기작성" />', '/bb/setSendPop.do?${paramsForList}&brdId='+selId+'&sendNo='+data.sendNo+'&returnFunc=closePop');
		}
	});
}
$(document).ready(function() {
});
//-->
</script>
<div style="width:500px;">
<u:set var="frmSrc" test="${!empty frmSrc }" value="./${frmSrc }.do?menuId=${menuId}&${paramsForList }" elseValue="/cm/util/reloadable.do"/>
<iframe id="sendFrm" name="sendFrm" src="${frmSrc }" style="width:100%;height:300px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
<u:blank />
<u:buttonArea>
	<u:button titleId="dm.btn.send" alt="보내기" onclick="selectList();" />
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</div>