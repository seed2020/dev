<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
	import="java.net.URLEncoder"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
%>
<script type="text/javascript">
<!--<%
// [하단버튼:등록] %>
function addLginPage(){
	dialog.open('setMnuGrpDialog','<u:msg titleId="pt.jsp.setLoginPage.reg" alt="로그인 페이지 추가" />','./setLoginPagePop.do?menuId=${menuId}');
}<%
// [목록클릭] %>
function modLginPage(lginImgId){
	dialog.open('setMnuGrpDialog','<u:msg titleId="pt.jsp.setLoginPage.mod" alt="로그인 페이지 변경" />','./setLoginPagePop.do?menuId=${menuId}&lginImgId='+lginImgId);
}<%
// [하단버튼:삭제] %>
function delLginPage(){
	var arr = getCheckedValue("listArea", "cm.msg.noSelect");<%//cm.msg.noSelect=선택한 항목이 없습니다.%>
	if(arr!=null && confirmMsg("cm.cfrm.del")) {<%//cm.cfrm.del=삭제하시겠습니까 ?%>
		callAjax('./transLoginPageDelAjx.do?menuId=${menuId}', {lginImgIds:arr}, function(data){
			if(data.message!=null){
				alert(data.message);
			}
			if(data.result=='ok'){
				reload();
			}
		});
	}
}<%
// [하단버튼:최상위로] %>
function toTheTop(){
	var arr = getCheckedValue("listArea", "cm.msg.noSelect");<%//cm.msg.noSelect=선택한 항목이 없습니다.%>
	if(arr == null) return;
	if(arr.length > 1){
		<%// cm.msg.selectOneItem=하나의 "{0}"(을)를 선택해 주십시요. / pt.login.title=로그인%>
		alertMsg("cm.msg.selectOneItem", ["#pt.login.title"]);
		return;
	}
	callAjax('./transLoginPageToTopAjx.do?menuId=${menuId}', {lginImgId:arr[0]}, function(data){
		if(data.message!=null){
			alert(data.message);
		}
		if(data.result=='ok'){
			reload();
		}
	});
}<%
// [리스트버튼:미리보기] %>
function previewLogin(lginImgId){
	window.open("/cm/login/viewLogin.do?lginImgId="+lginImgId, "_preview");
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>
<u:title title="로그인 페이지 설정" menuNameFirst="true" />

<%// 목록 %>
<u:listArea id="listArea" >

	<tr>
	<td width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td>
	<td class="head_ct"><u:msg titleId="cols.lginPageNm" alt="로그인 페이지명" /></td>
	<td width="22%" class="head_ct"><u:msg titleId="ap.cols.prd" alt="기간" /></td>
	<td width="12%" class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
	<td width="12%" class="head_ct"><u:msg titleId="cols.dftSetupYn" alt="기본설정여부" /></td>
	<td width="12%" class="head_ct"><u:msg titleId="cm.btn.preview" alt="미리보기" /></td>
	</tr>

<c:if test="${fn:length(ptLginImgDVoList)==0}" >
	<tr>
	<td class="nodata" colspan="6"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(ptLginImgDVoList)!=0}" >
	<c:forEach items="${ptLginImgDVoList}" var="ptLginImgDVo" varStatus="status">
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" >
		<td class="bodybg_ct"><input type="checkbox" value="${ptLginImgDVo.lginImgId}" /></td>
		<td class="body_lt"><a href="javascript:modLginPage('${ptLginImgDVo.lginImgId}');" title="<u:out value="${ptLginImgDVo.rescNm}" type="value" /> - <u:msg titleId="cm.pop"/>"><u:out value="${ptLginImgDVo.rescNm}" /></a></td>
		<td class="body_ct">
			<u:out value="${ptLginImgDVo.strtYmd}" type="shortdate" />
			~
			<u:out value="${ptLginImgDVo.endYmd}" type="shortdate" /></td>
		<td class="body_ct"><u:decode srcValue="${ptLginImgDVo.useYn}" tgtValue="Y" valueRescId="cm.option.use" elseValueRescId="cm.option.notUse" /></td>
		<td class="body_ct"><c:if
			test="${ptLginImgDVo.dftImgYn=='Y'}"><u:msg titleId="cm.option.default" alt="기본값" /></c:if></td>
		<td class="body_ct"><u:buttonS titleId="cm.btn.preview" alt="미리보기" href="javascript:previewLogin('${ptLginImgDVo.lginImgId}');" /></td>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>

<u:pagination />

<u:buttonArea>
	<c:if test="${sessionScope.userVo.userUid eq 'U0000001'}"><u:msg
		titleId="pt.jsp.blockForeignPloc" alt="해외 IP 차단 정책" var="blockForeignPloc" />
	<u:button title="${blockForeignPloc}" alt="해외 IP 차단 정책" onclick="dialog.open('setForeignIpBlockingPlocDialog','${blockForeignPloc}','./setForeignIpBlockingPlocPop.do?menuId=${menuId}');" /></c:if>
	<u:button titleId="cm.btn.toTop" alt="최상위로" href="javascript:toTheTop();" auth="SYS" />
	<u:button titleId="cm.btn.reg" alt="등록" href="javascript:addLginPage();" auth="SYS" popYn="Y" />
	<u:button titleId="cm.btn.del" alt="삭제" href="javascript:delLginPage();" auth="SYS" />
</u:buttonArea>