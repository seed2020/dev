<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
	import="com.innobiz.orange.web.cm.utils.StringUtil"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

request.setAttribute("currTime", StringUtil.getCurrDateTime());

%><script type="text/javascript">
<!--<%--
[버전 탭 클릭]--%>
function changeVer(ver, cat){
	location.href = './viewOnec.do?cat=${param.cat}&menuId=${menuId}&onecNo=${woOnecBVo.onecNo}&ver='+ver;
}<%--
[삭제 버튼 클릭]--%>
function delVer(){
	callAjax('./transProcessActAjx.do?menuId=${menuId}&cat=${param.cat}', {onecNo:'${woOnecBVo.onecNo}', ver:'${woOnecBVo.ver}', actCd:'del'}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			location.replace('./listOnec.do?menuId=${menuId}&cat=${param.cat}');
		}
	});
}<%--
[홀더 변경(일괄 포함) 클릭]--%>
function setHoldr(onecNo){
	var titleId = onecNo=='' ? "wo.btn.modHoldrAll" : "wo.btn.modHoldr";
	var data = {userUid:'${woOnecBVo.holdrUid}'};
	searchUserPop({data:data, titleId:titleId}, function(userVo){
		if(userVo!=null){
			
			if(!confirmMsg('wo.cfrm.modHoldr')){<%--wo.cfrm.modHoldr=홀더를 변경 하시겠습니까 ?--%>
				return;
			}
			
			callAjax('./transProcessActAjx.do?menuId=${menuId}&cat=${param.cat}', {actCd:'changeHoldr', onecNo:onecNo, oldHoldrUid:'${woOnecBVo.holdrUid}', holdrUid:userVo.userUid}, function(data) {
				if (data.message != null) {
					alert(data.message);
				}
				if (data.result == 'ok') {
					location.replace('./listOnec.do?menuId=${menuId}&cat=${param.cat}');
				}
			});
		}
	});
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<u:title alt="원카드 목록 / 원카드 관리" menuNameFirst="true" notPrint="true" />
<c:if test="${fn:length(verVoList) > 0}">
<div class="notPrint">
<u:tabGroup id="onecTab"><c:forEach
	items="${verVoList}" var="verVo" varStatus="status"><c:if
		test="${status.index < 10}">
<u:tab id="onecTab"
	title="${verVo.statCd eq 'apvd' ? verVo.revsDt.substring(0,10) : ''}"
	titleId="${verVo.statCd eq 'apvd' ? null : 'wo.statCd.'.concat(verVo.statCd)}"
	onclick="changeVer(${verVo.ver});" on="${verVo.ver eq woOnecBVo.ver}" /></c:if></c:forEach>
</u:tabGroup>
</div>
</c:if>
<c:if

	test="${not empty woOnecApvLnDVoList}">
<div id="apvLnArea">
<div><img style="width: 216px; height: 72px; float: left;" src="/images/etc/DYNE.png"></div>
<div id="3row">
	<div style="float:right; width:60%; text-align:right;">
	<span style="font-size:16pt; color:red; font-weight: bold;">기 밀</span><br/>
	<span style="font-size:10pt;">${currTime}</span><span style="font-size:10pt; padding-left:8px">${sessionScope.userVo.userNm}</span><br/>
	</div>
	<div style="float:right; width:60%; text-align:right;">
	<table class="approvaltable" style="float: right;" border="0" cellspacing="1" cellpadding="0"><tbody>
	<tr><td class="approval_head" rowspan="3">결<br>재</td><c:forEach
			items="${woOnecApvLnDVoList}" var="woOnecApvLnDVo" varStatus="tdStatus">
		<td class="approval_body" style="width: 77px;"><div class="approval_bodyin">${woOnecApvLnDVo.positDispVa}</div></td></c:forEach>
	</tr>
	<tr><c:forEach
			items="${woOnecApvLnDVoList}" var="woOnecApvLnDVo" varStatus="tdStatus">
		<td class="approval_img" style="width: 77px; position:relative; font-weight:bold;"><c:if
			test="${woOnecApvLnDVo.apvrRoleCd=='abs'}"><u:out value="${woOnecApvLnDVo.absRsonNm}" /></c:if><c:if
			test="${woOnecApvLnDVo.apvrRoleCd!='abs' and (
					woOnecApvLnDVo.apvStatCd=='apvd' or woOnecApvLnDVo.apvStatCd=='rejt'
					or woOnecApvLnDVo.apvStatCd=='pros' or woOnecApvLnDVo.apvStatCd=='cons')}"><u:set
				
				test="${not empty woOnecApvLnDVo.signImgPath}" var="signHtml"
				value='<img alt="sign" src="${_cxPth}${woOnecApvLnDVo.signImgPath}" height="40px" />'
				elseValue="${woOnecApvLnDVo.signDispVa}"
				cmt="서명용 사진 또는 텍스트를 'signHtml'에 저장 후 링크별(사용자 팝업, 결재선팝업)로 사용"
				
				/><c:if
			test="${not empty woOnecApvLnDVo.apvrUid}"
			><a href="javascript:viewUserPop('${woOnecApvLnDVo.apvrUid}');">${signHtml}</a></c:if><c:if
			test="${empty woOnecApvLnDVo.apvrUid}"
			>${signHtml}</c:if></c:if></td></c:forEach>
	</tr>
	<tr><c:forEach
			items="${woOnecApvLnDVoList}" var="woOnecApvLnDVo" varStatus="tdStatus">
		<td class="approval_body" style="width: 77px;">${woOnecApvLnDVo.dtDispVa}</td></c:forEach>
	</tr>
	</tbody></table>
	</div>
</div>
<div class="blank" id="bottomBlank"></div>
</div></c:if><c:if

	test="${empty woOnecApvLnDVoList}">
<div id="apvLnArea">
<div><img style="width: 216px; height: 72px; float: left;" src="/images/etc/DYNE.png"></div>
<div id="3row">
	<div style="float:right; width:60%; text-align:right;">
	<span style="font-size:16pt; color:red; font-weight: bold;">기 밀</span><br/>
	<span style="font-size:10pt;">${currTime}</span><span style="font-size:10pt; padding-left:8px">${sessionScope.userVo.userNm}</span><br/>
	</div>
	<div style="float:right; width:60%; text-align:right;">
	<table class="approvaltable" style="float: right;" border="0" cellspacing="1" cellpadding="0"><tbody>
	<tr><td class="approval_head" rowspan="3">결<br>재</td>
		<td class="approval_body" style="width: 77px;"></td><td class="approval_body" style="width: 77px;"></td><td class="approval_body" style="width: 77px;"></td></tr>
	<tr><td class="approval_img" style="width: 77px;"></td><td class="approval_img" style="width: 77px;"></td><td class="approval_img" style="width: 77px;"></td></tr>
	<tr><td class="approval_body" style="width: 77px;"></td><td class="approval_body" style="width: 77px;"></td><td class="approval_body" style="width: 77px;"></td></tr>
	</tbody></table>
	</div>
</div>
<div class="blank" id="bottomBlank"></div>
</div></c:if>

<div style="margin-top:-60px;">
<jsp:include page="./viewOnecInc.jsp" />
</div>

<div style="blank"></div>

<%-- lastVer:홀더의 경우 최종 버전에 세팅됨 --%>
<u:buttonArea><c:if
		test="${param.cat eq 'all' and sessionScope.userVo.hasRole('ONEC_ADM')}" >
	<u:button titleId="wo.btn.modHoldr" alt="홀더 변경" onclick="setHoldr('${woOnecBVo.onecNo}');" auth="W" />
	<u:button titleId="wo.btn.modHoldrAll" alt="홀더 일괄 변경" onclick="setHoldr('');" auth="W" /></c:if><c:if
		test="${lastVer eq woOnecBVo.ver}"><c:if
			test="${param.cat ne 'all' and woOnecBVo.statCd ne 'askApv'}">
	<u:button titleId="cm.btn.mod" alt="수정" href="./setOnec.do?menuId=${menuId}&cat=${param.cat}&onecNo=${param.onecNo}" auth="W" /></c:if><c:if
			test="${param.cat ne 'all' and woOnecBVo.statCd eq 'temp' or woOnecBVo.statCd eq 'modify' or woOnecBVo.statCd eq 'cncl' or woOnecBVo.statCd eq 'rejt'}">
	<u:button titleId="cm.btn.del" alt="삭제" onclick="delVer();" auth="W" /></c:if></c:if>
	<u:button titleId="cm.btn.close" alt="닫기" href="./listOnec.do?cat=${param.cat}&menuId=${menuId}" />
</u:buttonArea>
