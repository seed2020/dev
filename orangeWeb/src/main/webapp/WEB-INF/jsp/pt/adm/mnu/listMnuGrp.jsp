<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
	import="java.net.URLEncoder"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	String backTo = request.getQueryString();
	if(backTo==null) { backTo = ""; }
	backTo = URLEncoder.encode("./listMnuGrp.do?"+backTo, "UTF-8");
	request.setAttribute("backTo", backTo);
%>
<script type="text/javascript">
<!--
<%// [팝업:메뉴그룹구분] 변경 - select - 01:포털구성(포틀릿,메뉴), 02:포털구성(포틀릿), 03:포털구성(메뉴), 04:포털구성(URL), 11:외부팝업, 12:외부프레임(URL) %>
function onMnuGrpTypCdChange(va){
	var $pop = $("#setMnuGrpPop");
	if(va=='04' || va=='11' || va=='12'){<%// mnuUrl:메뉴URL %>
		$pop.find('#mnuUrl').removeClass('input_disabled').removeAttr('readonly');
	} else {
		$pop.find('#mnuUrl').addClass('input_disabled').attr('readonly','readonly');
	}
	if(va=='11'){<%// popSetupCont:팝업설정내용 %>
		$pop.find('#popSetupCont').removeClass('input_disabled').removeAttr('readonly');
	} else {
		$pop.find('#popSetupCont').addClass('input_disabled').attr('readonly','readonly');
	}
}
<%// [팝업:저장] 메뉴그룹저장 %>
function saveMnuGrp(){
	if(validator.validate('setMnuGrpPop')){
		var $form = $("#setMnuGrpPop");
		$form.attr('method','post');
		$form.attr('action','./transMnuGrp.do');
		$form.attr('target','dataframe');
		$form.submit();
	}
}
<%// [하단버튼:추가] %>
function addMnuGrp(){
	dialog.open('setMnuGrpDialog','<u:msg titleId="pt.jsp.listMnuGrp.reg.title" alt="메뉴 그룹 추가" />','./setMnuGrpPop.do?menuId=${menuId}${mnuGrpMdCdParam}');
	onMnuGrpTypCdChange('01');
}
<%// [목록클릭] %>
function modMnuGrp(mnuGrpId){
	dialog.open('setMnuGrpDialog','<u:msg titleId="pt.jsp.listMnuGrp.mod.title" alt="메뉴 그룹 변경" />','./setMnuGrpPop.do?menuId=${menuId}${mnuGrpMdCdParam}&mnuGrpId='+mnuGrpId);
	//onMnuGrpTypCdChange('01');
}
<%// [하단버튼:삭제] %>
function delMnuGrp(){
	var arr = getCheckedValue("listArea", "cm.msg.noSelect");<%//cm.msg.noSelect=선택한 항목이 없습니다.%>
	if(arr!=null && confirmMsg("cm.cfrm.del")) {<%//cm.cfrm.del=삭제하시겠습니까 ?%>
		callAjax('./transMnuGrpDelAjx.do?menuId=${menuId}${mnuGrpMdCdParam}', {mnuGrpIds:arr}, function(data){
			if(data.message!=null){
				alert(data.message);
			}
			if(data.result=='ok'){
				var frm = document.getElementById('paginationForm');
				frm.extValues.value = arr.join(',');
				frm.submit();
			}
		});
	}
}
<%// 위로, 아래로 이동%>
function moveMnuGrp(direction){
	var arr = getCheckedValue("listArea", "cm.msg.noSelect");<%//cm.msg.noSelect=선택한 항목이 없습니다.%>
	if(arr!=null){
		callAjax('./transMnuGrpMoveAjx.do?menuId=${menuId}${mnuGrpMdCdParam}', {direction:direction, mnuGrpIds:arr}, function(data){
			if(data.message!=null){
				alert(data.message);
			}
			if(data.result=='ok'){
				var frm = document.getElementById('paginationForm');
				frm.extValues.value = arr.join(',');
				frm.submit();
			}
		});
	}
}
<%// 관리 범위 설정%>
function setMngCompSetting(){
	dialog.open('setMngCompDialog','<u:msg titleId="pt.jsp.listMnuGrp.mngCompSet" alt="관리 범위 설정" />','./setMngCompPop.do?menuId=${menuId}&setupId=${setupId}');
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
	var frm = document.getElementById('paginationForm');
	if(frm.extValues.value!=''){
		var i, arr = frm.extValues.value.split(','), $listarea = $('#listArea');
		frm.extValues.value = '';
		for(i=0;i<arr.length;i++){
			$listarea.find("#tr"+arr[i]+" input[type='checkbox']").trigger('click');
		}
	}
});
//-->
</script>
<u:title alt="사용자 / 관리자 메뉴 그룹 관리" menuNameFirst="true" />

<%// 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listMnuGrp.do">
	<input type="hidden" name="menuId" value="${menuId}" /><c:if
		test="${not empty mnuGrpMdCd}">
	<input type="hidden" name="mnuGrpMdCd" value="${mnuGrpMdCd}" /></c:if>
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="cols.mnuGrpNm" alt="메뉴그룹명" /></td>
		<td><u:input id="schWord" titleId="cm.schWord" style="width:300px;" value="${param.schWord}"  maxByte="50"/></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>


<%// 목록 %>
<u:listArea id="listArea" >

	<tr>
	<td width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td>
	<td class="head_ct"><u:msg titleId="cols.mnuGrpNm" alt="메뉴그룹명" /></td>
	<td width="13%" class="head_ct"><u:msg titleId="cols.mnuGrpTyp" alt="메뉴그룹구분" /></td>
	<td width="13%" class="head_ct"><u:msg titleId="pt.cols.openScop" alt="공개범위" /></td>
	<td width="9%" class="head_ct"><u:msg titleId="pt.jsp.listMnuGrp.design.menu" alt="메뉴 구성" /></td>
	<c:if test="${mnuGrpMdCd == 'U'}"><td width="9%" class="head_ct"><u:msg titleId="pt.jsp.listMnuGrp.design.portlet" alt="포틀릿 구성" /></td></c:if>
	<td width="8%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
	<td width="12%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
	</tr>

<c:if test="${fn:length(ptMnuGrpBVoList)==0}" >
	<tr>
	<c:if test="${mnuGrpMdCd != 'U'}"><td class="nodata" colspan="8"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td></c:if>
	<c:if test="${mnuGrpMdCd == 'U'}"><td class="nodata" colspan="9"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td></c:if>
	</tr>
</c:if>
<c:if test="${fn:length(ptMnuGrpBVoList)!=0}" >
	<c:forEach items="${ptMnuGrpBVoList}" var="ptMnuGrpBVo" varStatus="status">
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="tr${ptMnuGrpBVo.mnuGrpId}" >
		<td class="bodybg_ct"><input type="checkbox" value="${ptMnuGrpBVo.mnuGrpId}" /></td>
		<td class="body_lt"><a href="javascript:modMnuGrp('${ptMnuGrpBVo.mnuGrpId}');" title="<u:msg titleId="pt.jsp.listMnuGrp.mod.title"/> - <u:msg titleId="cm.pop"/>"><u:out value="${ptMnuGrpBVo.rescNm}" /></a></td>
		<td class="body_ct"><u:out value="${ptMnuGrpBVo.mnuGrpTypNm}" /></td>
		<td class="body_ct"><u:out value="${ptMnuGrpBVo.openCompNm}" /></td>
		<td class="listicon_ct"><c:if test="${ptMnuGrpBVo.mnuGrpTypCd == '01' or ptMnuGrpBVo.mnuGrpTypCd == '03'}"><u:buttonS href="./set${mnuGrpMd}Mnu.do?menuId=${menuId}&mnuGrpId=${ptMnuGrpBVo.mnuGrpId}" titleId="pt.jsp.listMnuGrp.design.menu" alt="메뉴 구성" /></c:if></td>
		<c:if test="${mnuGrpMdCd == 'U'}"><td class="listicon_ct"><c:if test="${ptMnuGrpBVo.mnuGrpTypCd == '01' or ptMnuGrpBVo.mnuGrpTypCd == '02'}"><u:buttonS href="./setPltStep1.do?menuId=${menuId}&mnuGrpId=${ptMnuGrpBVo.mnuGrpId}&backTo=${backTo}" titleId="pt.jsp.listMnuGrp.design.portlet" alt="포틀릿 구성" /></c:if></td></c:if>
		<td class="body_ct"><u:out value="${ptMnuGrpBVo.regrNm}" /></td>
		<td class="body_ct"><u:out value="${ptMnuGrpBVo.regDt}" type="date" /></td>
		<td class="body_ct"><u:decode srcValue="${ptMnuGrpBVo.useYn}" tgtValue="Y" valueRescId="cm.option.use" elseValueRescId="cm.option.notUse" /></td>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>

<u:pagination />

<u:buttonArea>
	<u:button titleId="pt.jsp.listMnuGrp.mngCompSet" alt="관리 범위 설정" href="javascript:setMngCompSetting();" auth="SYS" popYn="Y" />
	<u:button titleId="cm.btn.reg" alt="등록" href="javascript:addMnuGrp();" auth="A" popYn="Y" />
	<u:button titleId="cm.btn.del" alt="삭제" href="javascript:delMnuGrp();" auth="A" />
	<u:button titleId="cm.btn.up" alt="위로이동" href="javascript:moveMnuGrp('up');" auth="A" />
	<u:button titleId="cm.btn.down" alt="아래로이동" href="javascript:moveMnuGrp('down');" auth="A" />
</u:buttonArea>