<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="callback" test="${!empty param.callback }" value="${param.callback }" elseValue="fnFldSelect" />
<script type="text/javascript">
<!--
function selectFld(){
	${callback}();
}
//-->

<%// [저장버튼] - parent 호출 %>
function getCombData(){
	var pObj = {};
	getCombDataUL($("#combTree #ROOTUL"), pObj);
	return pObj.children == null ? [] : pObj.children;
}
<%// UL 테그 하위 LI 테그로 부터 저장 할 데이터 추출 %>
function getCombDataUL($ul, pObj){
	var sortOrdr = 0, exts, rescs;
	$ul.children().each(function(){
		exts = $.parseJSON($(this).attr("data-exts"));
		exts.sortOrdr = (++sortOrdr)+"";
		rescs = $(this).attr("data-rescs");
		if(rescs!=null && rescs!=''){
			exts.rescs = $.parseJSON(rescs);
		}
		if(pObj.children==null){
			pObj.children=[];
		}
		if(pObj.title!=null) delete pObj.title;
		pObj.children.push(exts);
		getCombDataUL($(this).children('ul'), exts);
	});
};
</script>

<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />

<div style="width:400px">
	<iframe id="findBcFldFrm" name="findBcFldFrm" src="./findBcFldFrm.do?menuId=${menuId }&schBcRegrUid=${param.schBcRegrUid}" style="width:100%; height:400px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>

	<u:buttonArea>
		<u:button titleId="cm.btn.choice" onclick="selectFld();" alt="선택" auth="W" />
		<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
	</u:buttonArea>

</div>
