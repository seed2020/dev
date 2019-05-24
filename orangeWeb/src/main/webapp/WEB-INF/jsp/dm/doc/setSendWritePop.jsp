<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="frmUrl" test="${!empty param.docTyp && param.docTyp eq 'brd'}" value="/cm/doc/setSendBrdWriteFrm.do?menuId=${param.menuId }&${paramsForList }" elseValue="./setSendWriteFrm.do?menuId=${param.menuId }&${paramsForList }"/>
<u:set var="saveFunc" test="${!empty param.tabId && param.tabId eq 'brd'}" value="saveBull" elseValue="save"/>
<u:params var="nonPageParams" excludes="docId,pageNo,data"/>
<script type="text/javascript">
<!--<%// [확인] 저장 %>
function saveWrite(){
	if(getIframeContent("writeFrm").${saveFunc}())
		dialog.close('setSendWritePop');
}<%// [팝업] 하위문서 %>
function findWriteDocPop(tabId, callback){
	var url = '/cm/doc/findDocPop.do?${nonPageParams}&fncMul=N&tabId=${param.tabId}';
	if(tabId != undefined && tabId == 'doc') {
		var selId = getIframeContent('writeFrm').getSelectAllId(tabId);
		if(selId == null){
			alertMsg("dm.jsp.setDoc.not.fld");<%//dm.jsp.setDoc.not.fld='폴더'를 선택 후 사용해 주십시요.%>
			return;
		}
		url+= "&fldId="+selId;
	}
	if(callback != null) url+= "&callback="+callback;
	dialog.open('findDocPop', '<u:msg titleId="dm.jsp.search.doc.title" alt="문서조회" />', url);
};<%// [하단버튼:] 보내기옵션 %>
function setSendOptPop(){
	var selId = '';
	if(selId == null){
		alertMsg("dm.jsp.setDoc.not.fld");<%//dm.jsp.setDoc.not.fld=왼쪽 '폴더'를 선택 후 사용해 주십시요.%>
		return;
	}
	dialog.open('setSendOptDialog', '<u:msg titleId="dm.jsp.sendWrite.title" alt="보내기작성" />', '/cm/doc/setSendOptPop.do?${paramsForList}&docId=${param.docId}&selId='+selId+'&tabId='+clkTab);
};<%// [프레임리로드:] 하위문서선택시  %>
function sendSubReload(arr){
	if(arr == null) {
		reloadFrame('writeFrm', '${frmUrl}');
		$('#sendWriteBtnArea #subDocListBtn').show();
	}else {
		if(arr.length == 1){
			reloadFrame('writeFrm', '${frmUrl}&docPid='+arr[0].get('docPid'));
			dialog.close('findDocPop');	
			$('#sendWriteBtnArea #subDocListBtn').hide();
		}
		
	}
	//getIframeContent('writeFrm').setSendWriteFrm();
};
$(document).ready(function() {
});
//-->
</script>
<div style="width:900px;height:700px;">
<iframe id="writeFrm" name="writeFrm" src="${frmUrl }" style="width:100%; height:650px;" frameborder="0" marginheight="0" marginwidth="0" ></iframe>
<u:blank />
<u:buttonArea id="sendWriteBtnArea">
	<c:if test="${!empty param.tabId && param.tabId eq 'doc' }"><u:button id="subDocListBtn" titleId="dm.btn.sendSubDoc" alt="하위문서로 보내기" onclick="findWriteDocPop('${param.tabId}', 'sendSubReload');" /></c:if>
	<u:button titleId="dm.btn.restore" alt="되돌리기" href="javascript:sendSubReload(null);" />
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:saveWrite();" />
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</div>