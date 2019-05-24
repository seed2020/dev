<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><style type="text/css">
.search_table .search_tit{white-space:nowrap;}
</style>
<script type="text/javascript">
<!--<c:if test="${!empty param.isFind && param.isFind eq 'Y' }"><%
//페이지 변경시 호출 - 페이지 이동간에 선택된 것 유지 %>
function onPageChange(){
	var checks=[], notChecks=[];<%// chk:체크된것, notChk:체크 안된것 %>
	var attrs = ["reqNo","subj","docNo","hdlrUid","hdlrNm","cmplDt"], relReq, $check;
	$("#listReqFrmArea input[type='checkbox'][id!='checkHeader']").each(function(){
		if(this.checked){
			$check = $(this);
			relReq = {};
			attrs.each(function(index, attr){
				relReq[attr] = $check.attr("data-"+attr);
			});
			checks.push(relReq);
		} else {
			notChecks.push($(this).val());
		}
	});
	parent.setRefApvChecks(checks, notChecks);
}</c:if><% // [팝업] - 요청 상세 %>
function openReqView(reqNo){
	parent.dialog.open('openReqDialog','<u:msg titleId="wh.cols.req" alt="요청사항" />','./viewReqPop.do?menuId=${param.menuId}&reqNo='+reqNo);
	parent.dialog.onClose("listReqListDialog", function(){ dialog.close('openReqDialog'); });
}
$(document).ready(function() {
	<c:if test="${!empty param.isFind && param.isFind eq 'Y' }">
	// 페이지 이동간 체크한 목록 다시 체크해 줌 %>
	var $form = $("#listReqFrmArea");
	parent.gRelReqs.each(function(index, relReq){
		$form.find("input[value='"+relReq.reqNo+"']").attr("checked", true);
	});</c:if><%
	// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>
<c:if test="${!empty param.isFind && param.isFind eq 'Y' }">
<u:searchArea>
	<form id="searchForm" name="searchForm" action="${_uri}" >
	<u:input type="hidden" id="menuId" value="${menuId}" /><c:if
	test="${not empty param.pageRowCnt}">
	<u:input type="hidden" id="pageRowCnt" value="${param.pageRowCnt}" /></c:if>
	<u:set var="paramStatCd" test="${!empty paramStatCd }" value="${paramStatCd }" elseValue="${param.statCd }"/>
	<c:if test="${!empty param.isFind}"><u:input type="hidden" id="isFind" value="${param.isFind}" /></c:if>
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
		<tr>
			<td>
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td><select name="schCat" style="min-width:60px;">
							<u:option value="docNo" titleId="wh.cols.docNo" alt="요청번호" checkValue="${param.schCat}" selected="${!empty param.docNo }"/>
							<u:option value="subj" titleId="cols.subj" alt="제목" checkValue="${param.schCat}" selected="${!empty param.subj }"/>
							<u:option value="cont" titleId="cols.cont" alt="내용" checkValue="${param.schCat}" />
							</select>
						</td>
						<td><u:set var="paramSchWord" test="${!empty param.subj }" value="${param.subj }" elseValue="${param.schWord }"/><u:input id="schWord" maxByte="50" value="${paramSchWord}" titleId="cols.schWord" style="width: 100px;" onkeydown="if (event.keyCode == 13) searchForm.submit();" /></td>
						<td class="width20"></td>
						<td class="search_tit"><u:msg titleId="cols.prd" alt="기간" /></td>
						<td>
							<table border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td>
										<select name="durCat" style="min-width:70px;">
										<u:option value="reqDt" titleId="wh.cols.reqYmd" alt="요청일" checkValue="${param.durCat}"  selected="${empty param.durCat }"
										/><c:if test="${path ne 'recv'}"><u:option value="recvDt" titleId="wh.cols.recv.recvYmd" alt="접수일" checkValue="${param.durCat}"
										/></c:if><u:option value="cmplDt" titleId="wh.cols.req.cmplDt" alt="완료일" checkValue="${param.durCat}" />
										</select>
									</td>
									<td><u:calendar id="durStrtDt" option="{end:'durEndDt'}" value="${param.durStrtDt}" /></td>
									<td class="search_body_ct"> ~ </td>
									<td><u:calendar id="durEndDt" option="{start:'durStrtDt'}" value="${param.durEndDt}" /></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
			<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
		</tr>
	</table>
	</form>
</u:searchArea></c:if>
<c:if test="${empty param.isFind || param.isFind eq 'N' }">
<jsp:include page="/WEB-INF/jsp/wh/help/listHelpSrch.jsp" flush="false">
<jsp:param value="dashbrd" name="path"/>
<jsp:param value="popup" name="listTyp"/>
</jsp:include>
</c:if>

<div class="listarea" id="listReqFrmArea">
<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
<colgroup><c:if test="${!empty param.isFind && param.isFind eq 'Y' }"><col width="5%"/></c:if><col width="25%"/><col width="*"/><col width="15%"/><col width="15%"/></colgroup>
	<tr id="headerTr">
		<c:if test="${!empty param.isFind && param.isFind eq 'Y' }">
		<td class="head_bg">
			<input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listReqFrmArea', this.checked);" value=""/>
		</td></c:if>
		<th class="head_ct"><u:msg titleId="wh.cols.docNo" alt="요청번호"/></th>	
		<th class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></th>
		<th class="head_ct"><u:msg titleId="wh.cols.hdl.pich" alt="처리 담당자" /></th>
		<th class="head_ct"><u:msg titleId="wh.cols.req.cmplDt" alt="완료일" /></th>
	</tr>
	<c:forEach var="whReqBVo" items="${whReqBVoList}" varStatus="status">
		<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"' >
			<c:if test="${!empty param.isFind && param.isFind eq 'Y' }"><td class="bodybg_ct"><input type="checkbox" name="reqNo"
			value="${whReqBVo.reqNo
			}" data-reqNo="<u:out value="${whReqBVo.reqNo}" type="value"
			/>" data-docNo="<u:out value="${whReqBVo.docNo}" type="value"
			/>" data-subj="<u:out value="${whReqBVo.subj}" type="value"
			/>" data-hdlrUid="<u:out value="${whReqBVo.hdlrUid}" type="value"
			/>" data-hdlrNm="<u:out value="${whReqBVo.hdlrNm}" type="value"
			/>" data-cmplDt="<u:out value="${whReqBVo.cmplDt}" type="date"
			/>" /></td></c:if>
			<td class="body_lt" ><div class="ellipsis" title="${whReqBVo.docNo }"><a href="javascript:openReqView('${whReqBVo.reqNo }');"><u:out value="${whReqBVo.docNo }"/></a></div></td>
			<td class="body_lt" ><div class="ellipsis" title="${whReqBVo.subj }"><a href="javascript:openReqView('${whReqBVo.reqNo }');"><u:out value="${whReqBVo.subj }"/></a></div></td>
			<td class="body_ct"><a href="javascript:viewUserPop('${whReqBVo.hdlrUid }');"><u:out value="${whReqBVo.hdlrNm }" /></a></td>
			<td class="body_ct" ><u:out value="${whReqBVo.cmplDt }" type="date"/></td>
		</tr>
	</c:forEach>
	<c:if test="${empty whReqBVoList }">
		<tr>
		<td class="nodata" colspan="${!empty param.isFind && param.isFind eq 'Y' ? 5 : 4}"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
	
</table>
</div>
<u:blank />
<c:choose>
<c:when test="${frmYn eq 'Y' }"><div style="padding:10px;"><u:pagination noLeftSelect="true"/></div></c:when>
<c:otherwise><u:pagination /></c:otherwise>
</c:choose>
