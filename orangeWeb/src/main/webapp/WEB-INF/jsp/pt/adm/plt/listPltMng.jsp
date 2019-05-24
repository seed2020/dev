<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<%// [팝업] 포틀릿 저장 %>
function savePlt(){
	if(validator.validate('setPltPop')){
		var $form = $("#setPltPop");
		$form.attr("action","./transPltMng.do");
		$form.attr("target","dataframe");
		$form.submit();
	}
}
<%// [버튼] 삭제 %>
function delPlt(){
	var arr = getCheckedValue("listArea", "cm.msg.noSelect");<%//cm.msg.noSelect=선택한 항목이 없습니다.%>
	if(arr!=null && confirmMsg("cm.cfrm.del")) {<%//cm.cfrm.del=삭제하시겠습니까 ?%>
		callAjax('./transPltDelAjx.do?menuId=${menuId}', {pltIds:arr}, function(data){
			if(data.message!=null) { alert(data.message); }
			if(data.result=='ok') { location.replace(location.href); }
		});
	}
}
<%// [버튼] 카테고리 %>
function goCatCd(){
	location.href="./setCatCd.do?menuId=${menuId}&clsCd=PLT_CAT_CD&backTo="+ encodeURIComponent("/pt/adm/plt/listPltMng.do?<%= request.getQueryString()%>");
}
<%// 관리 범위 설정%>
function setMngCompSetting(){
	dialog.open('setMngCompDialog','<u:msg titleId="pt.jsp.listMnuGrp.mngCompSet" alt="관리 범위 설정" />','./setMngCompPop.do?menuId=${menuId}&setupId=${setupId}');
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>

<u:title titleId="pt.jsp.listPlt.title" alt="포틀릿 관리" menuNameFirst="true" />

<%// 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listPltMng.do">
	<input type="hidden" name="menuId" value="${menuId}" />
	<input type="hidden" name="schCat" value="pltNm" />
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr><u:decode srcId="_lang" tgtValue="ko" var="cdLoc" value="공통코드 : 포털 / 포털카테고리 / 포틀릿카테고리" elseValue="" />
		<td class="search_tit" ><u:msg titleId="cols.pltCat" alt="포틀릿카테고리" /></td>
		<td><select name="pltCatCd" <u:elemTitle titleId="cols.pltCat" />>
			<u:option value="" titleId="cm.option.all" alt="전체" checkValue="${param.pltCatCd}" />
			<c:forEach items="${pltCatCdList}" var="pltCatCd" varStatus="status">
			<u:option value="${pltCatCd.cd}" title="${pltCatCd.rescNm}" checkValue="${param.pltCatCd}" />
			</c:forEach></select></td>
		<td class="width10"></td>
		<td class="search_tit"><u:msg titleId="cols.pltNm" alt="포틀릿명" /></td>
		<td><u:input id="schWord" titleId="cm.schWord" style="width:200px;" value="${param.schWord}" maxByte="50" /></td>
		<td class="width10"></td>
		<td class="search_tit"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
		<td><select name="useYn"<u:elemTitle titleId="cols.useYn" />>
			<u:option value="" titleId="cm.option.all" alt="전체" checkValue="${param.useYn}" />
			<u:option value="Y" titleId="cm.option.use" alt="사용" checkValue="${param.useYn}" />
			<u:option value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${param.useYn}" />
			</select></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<%// 목록 %>
<u:listArea id="listArea">

	<tr>
	<td width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td>
	<td width="15%" class="head_ct"><u:msg titleId="cols.pltCat" alt="포틀릿카테고리" /></td>
	<td class="head_ct"><u:msg titleId="cols.pltNm" alt="포틀릿명" /></td>
	<td width="12%" class="head_ct"><u:msg titleId="pt.cols.openScop" alt="공개범위" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
	<td width="15%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
	</tr>

<c:if test="${fn:length(ptPltDVoList)==0}" >
	<tr>
	<td class="nodata" colspan="7"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(ptPltDVoList) >0}" >
	<c:forEach items="${ptPltDVoList}" var="ptPltDVo" varStatus="status">
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
	<td class="bodybg_ct"><input type="checkbox" id="contactBack" value="${ptPltDVo.pltId}"/></td>
	<td class="body_ct">${ptPltDVo.pltCatNm}</td>
	<td class="body_lt"><a href="javascript:dialog.open('setsetPltDialog','<u:msg titleId="pt.jsp.listPlt.title" alt="포틀릿 설정"/>','./setPltPop.do?menuId=${menuId}&pltId=${ptPltDVo.pltId}');" title="<u:msg titleId="pt.jsp.listPlt.title"/> - <u:msg titleId="cm.pop"/>">${ptPltDVo.rescNm}</a></td>
	<td class="body_ct"><u:out value="${ptPltDVo.openCompNm}" /></td>
	<td class="body_ct"><u:out value="${ptPltDVo.regrNm}" /></td>
	<td class="body_ct"><u:out value="${ptPltDVo.modDt}" type="date" /></td>
	<td class="body_ct"><u:yn value="${ptPltDVo.useYn}" yesId="cm.option.use" noId="cm.option.notUse" alt="사용/사용안함" /></td>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>

<u:pagination />

<u:msg titleId="pt.jsp.listPlt.reg" var="addTitle" alt="포틀릿 등록" />
<u:buttonArea>
	<u:button titleId="pt.jsp.listMnuGrp.mngCompSet" alt="관리 범위 설정" href="javascript:setMngCompSetting();" auth="SYS" popYn="Y" />
	<u:button href="javascript:goCatCd()" titleId="pt.btn.cat" alt="카테고리" auth="SYS" />
	<u:button titleId="cm.btn.reg" alt="등록" href="javascript:dialog.open('setPltDialog','${addTitle}','./setPltPop.do?menuId=${menuId}');" auth="A" popYn="Y" />
	<u:button titleId="cm.btn.del" alt="삭제" href="javascript:delPlt();" auth="A" />
</u:buttonArea>
