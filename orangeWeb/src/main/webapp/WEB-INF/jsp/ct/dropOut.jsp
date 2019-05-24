<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
$(function() {
	//ct.cfrm.dropOut =  해당 커뮤니티에 탈퇴 하시겠습니까?
	if (confirmMsg("ct.cfrm.dropOut")) {
		callAjax('./transMbshDropOut.do?menuId=${menuId}', {ctId:"${ctId}"}, function(data){
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				//사용자 권한에 따른 url에 대한 menuId를 가져온다.
				location.href = data.menuUrl;
			}else if(data.result == 'fail'){
				history.back();
			}
		});
	}else{
		history.back();
	}
	
});
//-->
</script>


