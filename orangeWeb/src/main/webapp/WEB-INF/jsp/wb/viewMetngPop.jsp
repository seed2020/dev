<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//상세보기
function viewBc(bcId) {
	//location.href="./viewBc.do?menuId=${menuId}&bcId="+bcId;
	dialog.open('viewBcPop', '<u:msg titleId="wb.jsp.viewBcPop.title" alt="명함상세보기" />', './viewBcPop.do?menuId=${menuId}&schBcRegrUid=${param.schBcRegrUid}&bcId='+bcId);
};
</script>
<div style="width:800px">
	<jsp:include page="/WEB-INF/jsp/wb/viewMetngContents.jsp" flush="false"/>
	
	<u:blank />
	
	<u:buttonArea>
		<u:button href="javascript:void(0)" onclick="dialog.close(this);" alt="닫기" titleId="cm.btn.close" />
	</u:buttonArea>

</div>
