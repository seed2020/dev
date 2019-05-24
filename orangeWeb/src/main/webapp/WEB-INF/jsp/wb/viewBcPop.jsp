<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
// 미팅 상세보기
function viewMetngPop(bcMetngDetlId) {
	//location.href="./viewMetng.do?menuId=${menuId}&bcMetngDetlId="+bcMetngDetlId;
	dialog.open('viewMetngPop', '<u:msg titleId="wb.jsp.viewMetngPop.title" alt="미팅상세보기" />', './viewMetngPop.do?menuId=${menuId}&schBcRegrUid=${param.schBcRegrUid}&bcMetngDetlId='+bcMetngDetlId);
};
<%// 버튼 보이기 조절 - displayBtn 함수 이용  id배열 넘겨줌 %>
function applyDocBtn(action, param){
}
</script>
<div style="width:800px">
	<iframe id="viewBcFrm" name="viewBcFrm" src="${!empty param.pltYn && param.pltYn eq 'Y' ? '/wb' : '.' }/viewBcFrm.do?menuId=${menuId }&bcId=${param.bcId}&schBcRegrUid=${param.schBcRegrUid}&popYn=Y" style="width:100%; height:400px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
	
	<u:blank />
	
	<u:buttonArea>
		<u:button href="javascript:void(0)" onclick="dialog.close(this);" alt="닫기" titleId="cm.btn.close" />
	</u:buttonArea>

</div>
