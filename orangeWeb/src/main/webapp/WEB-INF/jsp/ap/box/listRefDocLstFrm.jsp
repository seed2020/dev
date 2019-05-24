<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// 페이지 변경시 호출 - 페이지 이동간에 선택된 것 유지 %>
function onPageChange(){
	var checks=[], notChecks=[];<%// chk:체크된것, notChk:체크 안된것 %>
	var attrs = ["apvNo","docSubj","makrUid","makrNm","cmplDt","secuYn"], refApv, $check;
	$("#listApvForm input[type='checkbox'][id!='checkHeader']").each(function(){
		if(this.checked){
			$check = $(this);
			refApv = {};
			attrs.each(function(index, attr){
				refApv[attr] = $check.attr("data-"+attr);
			});
			checks.push(refApv);
		} else {
			notChecks.push($(this).val());
		}
	});
	parent.setRefApvChecks(checks, notChecks);
}<%
// 기안함 - 개인분류 클릭 %>
function clickPsnClsInfo(clsId, clsNm){
	onPageChange();
	var $form = $("#searchForm1");
	$form.find("input[name='psnClsInfoId']").remove();
	if(clsId!=null && clsId!='' && clsId!='ROOT'){
		$form.appendHidden({name:'psnClsInfoId',value:clsId});
	}
	$form.submit();
}<%
// onload %>
$(document).ready(function() {<%
	// 페이지 이동간 체크한 목록 다시 체크해 줌 %>
	var $form = $("#listApvForm");
	parent.gRefApvs.each(function(index, refApv){
		$form.find("input[value='"+refApv.apvNo+"']").attr("checked", true);
	});<%
	// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>
<div style="width:100%">

<% // 검색영역 %>
<u:searchArea>
	<form id="searchForm1" name="searchForm1" action="${_uri}" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="bxId" value="${param.bxId}" /><c:forEach
		items="${arrMnuParam}" var="mnuParam">
	<u:input type="hidden" id="${mnuParam[0]}" value="${mnuParam[1]}" /></c:forEach>
	<u:input type="hidden" id="refDocBxId" value="${param.refDocBxId}" />
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select id="schCat" name="schCat" >
			<u:option value="docSubj" titleId="ap.doc.docSubj" alt="문서제목" selected="${param.schCat == 'docSubj'}" />
			<u:option value="makrNm" titleId="ap.doc.makrNm" alt="기안자" selected="${param.schCat == 'makrNm'}" />
			<u:option value="bodyHtml" titleId="ap.jsp.bodyHtml" alt="결재본문" selected="${param.schCat == 'bodyHtml'}" />
			</select></td>
		<td><u:input id="schWord" value="${param.schWord}" titleId="cols.schWord" style="width: 260px;" /></td>
		</tr>
		</table>
	</td>
	<td><div class="button_search"><ul><li class="search"><a href="javascript:document.searchForm1.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<%// 목록 %>
<form id="listApvForm">

<u:listArea id="listArea" tableStyle="table-layout: fixed;">
	<tr>
		<td width="4%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all'
		/>" onclick="checkAllCheckbox('listApvForm', this.checked);" value=""/></td>
		<td width="46%" class="head_ct"><u:msg titleId="ap.doc.docSubj" alt="제목" /></td><c:if
			test="${param.refDocBxId ne 'myBx'}">
		<td width="17%" class="head_ct"><u:msg titleId="ap.doc.makrNm" alt="기안자" /></td></c:if>
		<td width="17%" class="head_ct"><u:msg titleId="ap.list.cmplDd" alt="완결일자" /></td>
		<td width="16%" class="head_ct"><u:msg titleId="cols.docTyp" alt="문서구분" /></td>
	</tr>
<c:if test="${recodeCount == 0}">
	<tr>
		<td class="nodata" colspan="${param.refDocBxId ne 'myBx' ? '5' : '4'}"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>

<c:forEach items="${apOngdBVoMapList}" var="apOngdBVoMap" varStatus="outStatus"><c:set
	var="apOngdBVoMap" value="${apOngdBVoMap}" scope="request" />
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
		<td class="bodybg_ct"><input type="checkbox" name="apvNo" value="${apOngdBVoMap.apvNo
			}" data-apvNo="<u:out value="${apOngdBVoMap.apvNo}" type="value"
			/>" data-docSubj="<u:out value="${apOngdBVoMap.docSubj}" type="value"
			/>" data-makrUid="<u:out value="${apOngdBVoMap.makrUid}" type="value"
			/>" data-makrNm="<u:out value="${apOngdBVoMap.makrNm}" type="value"
			/>" data-cmplDt="<u:out value="${apOngdBVoMap.cmplDt}" type="value"
			/>" data-secuYn="${
						not empty apOngdBVoMap.docPwEnc
						and apOngdBVoMap.makrUid != sessionScope.userVo.userUid ? 'Y' : ''}" /></td>
		<td class="body_lt"><div class="ellipsis"><a href="javascript:parent.openDocView('${apOngdBVoMap.apvNo}','${
						not empty apOngdBVoMap.docPwEnc
						and apOngdBVoMap.makrUid != sessionScope.userVo.userUid ? 'Y' : ''}')"><c:if
						test="${apOngdBVoMap.ugntDocYn == 'Y'}"
					>[<u:msg titleId="bb.option.ugnt" alt="긴급" />] </c:if><c:if
						test="${not empty apOngdBVoMap.docPwEnc}"
					>[<u:msg titleId="bb.option.secu" alt="보안" />] </c:if><u:out
				value="${apOngdBVoMap.docSubj}" /></a></div></td><c:if
			test="${param.refDocBxId ne 'myBx'}">
		<td class="body_ct"><c:if test="${not empty apOngdBVoMap.makrUid}"
			><a href="javascript:parent.viewUserPop('${apOngdBVoMap.makrUid}')"><u:out
				value="${apOngdBVoMap.makrNm}" type="html" /></a></c:if><c:if
			test="${empty apOngdBVoMap.makrUid}"
			><u:out value="${apOngdBVoMap.makrNm}" type="html" /></c:if></td></c:if>
		<td class="body_ct"><u:out value="${apOngdBVoMap.cmplDt}" type="date" /></td>
		<td class="body_ct"><u:out value="${apOngdBVoMap.docTypNm}" /></td>
	</tr>
</c:forEach>
</u:listArea>
</form>
			
<u:pagination pageRowCnt="10" noBottomBlank="${true}" noTotalCount="true" onPageChange="onPageChange()" />

<form id="toDocForm" method="post">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="bxId" value="${bxId}" />
<u:input type="hidden" id="queryString" value="${queryString}" />
</form>

</div>
