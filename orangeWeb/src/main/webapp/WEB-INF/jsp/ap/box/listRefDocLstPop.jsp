<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// 체크된 목록 %>
var gRefApvs = [];<%
// 페이지 이동할때 체크된 목록 추가/제거 %>
function setRefApvChecks(checks, notChecks){
	var curRefApvs=[], apvNos=[];<%
	// 체크 안된 목록 제거%>
	gRefApvs.each(function(index, refApv){
		if(!notChecks.contains(refApv.apvNo)){
			curRefApvs.push(refApv);
			apvNos.push(refApv.apvNo);
		}
	});<%
	// 체크된 목록 추가 %>
	checks.each(function(index, newRefApv){
		if(!apvNos.contains(newRefApv.apvNo)){
			curRefApvs.push(newRefApv);
			apvNos.push(newRefApv.apvNo);
		}
	});
	gRefApvs = curRefApvs;
}<%
// 확인 버튼 클릭 - 첨부 팝업의 참조문서 영역에 해당 데이터를 넣음 %>
function applyRefDoc(){
	getIframeContent('listRefDocLstFrm').onPageChange();
	setRefApvsToAttchPop(gRefApvs);
}<%
// 탭 클릭 - refDocBxId - 등록대장:regRecLst 접수대장:recvRecLst 통보함:postApvdBx %>
function onRefDocTabClick(refDocBxId){
	var psnCatEnab = '${optConfigMap.psnCatEnab}';
	getIframeContent('listRefDocLstFrm').onPageChange();
	if(psnCatEnab=='Y'){
		if(refDocBxId=='myBx'){
			$("#psnClsFrmArea").show();
			$("#listRefDocLstFrm").width('73.6%');
		} else {
			$("#psnClsFrmArea").hide();
			$("#listRefDocLstFrm").width('100%');
		}
	}
	getIframeContent('listRefDocLstFrm').location.href= './listRefDocLstFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&refDocBxId='+refDocBxId + (gPsnClsInfoId==null ? '' : '&psnClsInfoId='+gPsnClsInfoId);
}
var gPsnClsInfoId = null;<%
// 기안함 - 개인분류 클릭 %>
function clickPsnClsInfo(clsId, clsNm){
	gPsnClsInfoId = (clsId=='ROOT' || clsId=='') ? null : clsId;
	getIframeContent('listRefDocLstFrm').clickPsnClsInfo(clsId, clsNm);
}<%
// onload %>
$(document).ready(function() {<%
	// 첨부에 있는 참조 문서를 전역변수 gRefApvs 에 세팅함 %>
	gRefApvs = collectRefApvToDoc();
	getIframeContent('listRefDocLstFrm').location.href= './listRefDocLstFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}';
});
//-->
</script>
<div style="width:700px">

<u:tabGroup id="refDocTab" noBottomBlank="true">
	<u:tab id="refDocTab" areaId="NO_AREA" titleId="ap.bx.regRecLst" alt="등록대장" onclick="onRefDocTabClick('regRecLst');" on="true" />
	<u:tab id="refDocTab" areaId="NO_AREA" titleId="ap.bx.recvRecLst" alt="접수대장" onclick="onRefDocTabClick('recvRecLst');" />
	<u:tab id="refDocTab" areaId="NO_AREA" titleId="ap.bx.postApvdBx" alt="통보함" onclick="onRefDocTabClick('postApvdBx');" />
	<u:tab id="refDocTab" areaId="NO_AREA" titleId="ap.bx.apvdBx" alt="완료함" onclick="onRefDocTabClick('apvdBx');" />
	<u:tab id="refDocTab" areaId="NO_AREA" titleId="ap.cfg.myBx" alt="기안함" onclick="onRefDocTabClick('myBx');" />
</u:tabGroup>

<u:tabArea
	outerStyle="height:380px; overflow-x:hidden; overflow-y:auto; padding:10px 10px 0px 10px;"
	innerStyle = "NO_INNER_IDV"><c:if test="${optConfigMap.psnCatEnab eq 'Y'}">

<u:boxArea
	id = "psnClsFrmArea"
	className="wbox" noBottomBlank="${true}"
	style="float:left; width:25%; display:none;"
	outerStyle="min-height:367px; overflow:hidden;"
	innerStyle="NO_INNER_IDV">
<iframe id="treePsnClsFrm" name="treePsnClsFrm" src="./treePsnClsFrm.do?menuId=${menuId}&bxId=${param.bxId
	}${strMnuParam}${not empty param.psnClsInfoId ? '&psnClsInfoId='.concat(param.psnClsInfoId) : ''}" style="width:100%; height:367px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:boxArea>

</c:if>
<iframe id="listRefDocLstFrm" name="listRefDocLstFrm" src="${_cxPth}/cm/util/reloadable.do" style="float:right; width:100%; height:370px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:tabArea>



<u:buttonArea>
	<u:button titleId="cm.btn.confirm" alt="확인" href="javascript:applyRefDoc();" />
	<u:button titleId="cm.btn.cancel" alt="취소" onclick="dialog.close(this);" />
</u:buttonArea>
</div>
