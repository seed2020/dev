<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.Date"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><fmt:formatDate var="today" value="<%=new Date() %>" type="date" pattern="yyyy-MM-dd"/>
<u:params var="paramsForList" excludes="reqNo"/>
<script type="text/javascript">
<!--<%// [왼쪽버튼] - 엑셀업로드 %>
function setExcelUploadPop(){
	dialog.open('setExcelUploadDialog', '<u:msg titleId="wh.jsp.listMd.excel" alt="엑셀업로드" />', './setExcelUploadPop.do?menuId=${menuId}');
}<% // 엑셀 파일 다운로드 %>
function excelDownFile() {
	var $form = $('#excelForm');
	$form.attr('method','post');
	$form.attr('action','./excelDownLoad.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form[0].submit();
};<% // 리로드 %>
function reloadHp(isList) {
	reload();
}<% // [하단버튼:접수|반려|진행처리] %>
function setReqHdlPop(ongoCd){
	var arr = getCheckedValue("listArea", "cm.msg.noSelect");<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
	if(arr==null) return;
	if(ongoCd===undefined)
		ongoCd=null;
	if(ongoCd=='A')
		dialog.open('setReqHdlDialog', '<u:msg titleId="wh.jsp.recv.title" alt="접수처리" />', './listReqHdlPop.do?menuId=${menuId}&reqNos='+arr.join(',')+'&ongoCd='+ongoCd+'&mdId=${param.mdId}');
	else
		dialog.open('setReqHdlDialog', '<u:msg titleId="wh.jsp.reject.title" alt="반려처리" />', './listReqHdlPop.do?menuId=${menuId}&reqNos='+arr.join(',')+'&ongoCd='+ongoCd+'&mdId=${param.mdId}');
	
}<% // [하단버튼:삭제] 삭제 %>
function delReq() {
	var arr = getCheckedValue("listArea", "cm.msg.noSelect");<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
	if (arr != null && confirmMsg("cm.cfrm.del")) {<% // 삭제하시겠습니까? %>
		callAjax('./${transDelPage}Ajx.do?menuId=${menuId}', {reqNos:arr}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.href = './${listPage}.do?${paramsForList}';
			}
		});
	}
}<%// [모듈선택버튼] - 시스템 모듈 선택 팝업 %>
function findSysMdPop(){
	var mdId=$('#paramMdArea').find('input[name="mdId"]').val();
	dialog.open('findSysMdDialog', '<u:msg titleId="wh.jsp.sysMd.title" alt="시스템모듈" />', './findSysMdPop.do?menuId=${menuId}&mdId='+mdId);	
}<%// 시스템 모듈 세팅 %>
function setSysMd(arr){
	if(arr!=null){
		$('#paramMdArea').find('input[name="mdId"]').val(arr.id);
		$('#paramMdArea').find('input[name="mdNm"]').val(arr.nm);
	}else{
		$('#paramMdArea').find('input').val('');
	}
}<% // [팝업] 파일목록 조회 %>
function viewFileListPop(reqNo,statCd) {
	var url = './viewFileListPop.do?${paramsForList }&reqNo='+reqNo+'&statCd='+statCd;
	parent.dialog.open('viewFileListDialog','<u:msg titleId="cols.att" alt="첨부" />', url);
}
<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
};
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<c:if test="${empty pageSuffix}"><u:msg var="menuTitle" titleId="wh.jsp.${path }.title" />
<u:title title="${menuTitle }" menuNameFirst="true" alt="${menuTitle }" /></c:if>
	
<% // 검색영역 %>
<jsp:include page="/WEB-INF/jsp/wh/help/listHelpSrch.jsp" />

<c:set var="chkYn" value="N"/>
<c:set var="colspan" value="8"/>
<c:set var="colgroup" value="12%,*,7%,8%,8%,8%,8%,8%"/>
<%-- <c:if test="${!empty envConfigMap.resEvalUseYn && envConfigMap.resEvalUseYn eq 'Y'}">
<c:set var="colspan" value="8"/>
<c:set var="colgroup" value=",9%,9%,9%,9%,9%,8%,8%"/>
</c:if> --%>
<c:if test="${path eq 'recv' || path eq 'hdl' || isAdmin==true}">
<c:set var="colspan" value="${colspan+2 }"/>
<c:set var="colgroup" value="12%,7%,*,7%,7%,7%,8%,8%,8%,8%"/>
<%-- <c:if test="${path eq 'hdl' || isAdmin==true}">
<c:set var="colspan" value="${colspan+1 }"/>
<c:set var="colgroup" value="9%,${colgroup }"/>
</c:if> --%>
</c:if>
<c:if test="${path ne 'req' || isAdmin==true}">
<c:set var="colspan" value="${colspan+1 }"/>
<c:set var="colgroup" value="${colgroup },7%"/>
</c:if>
<c:if test="${isAdmin==true || path eq 'req' || (!empty param.mdId && path eq 'recv')}">
<c:set var="chkYn" value="Y"/>
<c:set var="colspan" value="${colspan+1 }"/>
<c:set var="colgroup" value="3%,${colgroup }"/>
</c:if>
<c:if test="${envConfigMap.fileYn eq 'Y' }">
<c:set var="colspan" value="${colspan+1 }"/>
<c:set var="colgroup" value="${colgroup },7%"/>
</c:if>
<div class="listarea" id="listArea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
	<c:if test="${!empty colgroup }">
	<colgroup>
		<c:forTokens var="colWdth" items="${colgroup }" delims=","><col width="${colWdth }"></c:forTokens>
	</colgroup>
	</c:if>
	<tbody style="border:0">
	<tr id="headerTr">
		<c:if test="${chkYn eq 'Y'}"><td class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td></c:if>
		<th class="head_ct"><u:msg titleId="wh.cols.docNo" alt="요청번호"/></th>
		<c:if test="${path eq 'recv' || path eq 'hdl' || isAdmin==true}">
		<th class="head_ct"><u:msg titleId="wh.cols.md.nm" alt="모듈명"/></th>		
		<%-- <c:if test="${path eq 'hdl' || isAdmin==true}"><th class="head_ct"><u:msg titleId="wh.cols.hdl.typ" alt="처리유형"/></th></c:if> --%>
		</c:if>
		<th class="head_ct"><u:msg titleId="cols.subj" alt="제목"/></th>
		<c:if test="${path eq 'recv' || path eq 'hdl' || isAdmin==true}">
		<th class="head_ct"><u:msg titleId="wh.cols.req.deptNm" alt="요청부서"/></th>
		</c:if>
		<c:if test="${path ne 'req' || isAdmin==true}">
		<th class="head_ct"><u:msg titleId="wh.cols.req.reqr" alt="요청자"/></th>
		</c:if>
		<th class="head_ct"><u:msg titleId="cols.pich" alt="담당자"/></th>
		<th class="head_ct"><u:msg titleId="wh.cols.reqYmd" alt="요청일"/></th>
		<th class="head_ct"><u:msg titleId="wh.cols.recv.recvYmd" alt="접수일"/></th>
		<th class="head_ct"><u:msg titleId="wh.cols.hdl.dueDt" alt="처리예정일"/></th>
		<th class="head_ct"><u:msg titleId="wh.cols.req.cmplDt" alt="완료일시"/></th>
		<th class="head_ct"><u:msg titleId="cols.prgStat" alt="진행상태"/></th>
		<%-- <c:if test="${!empty envConfigMap.resEvalUseYn && envConfigMap.resEvalUseYn eq 'Y'}"><th class="head_ct"><u:msg titleId="wh.cols.req.eval" alt="평가"/></th></c:if> --%>
		<c:if test="${envConfigMap.fileYn eq 'Y' }"><th class="head_ct"><u:msg titleId="cols.att" alt="첨부"/></th></c:if>
	</tr>
	<c:if test="${fn:length(whReqBVoList) == 0}">
		<tr>
		<td class="nodata" colspan="${colspan }"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
	<c:if test="${fn:length(whReqBVoList)>0}">
		<c:forEach items="${whReqBVoList}" var="whReqBVo" varStatus="status">
			<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
				<c:if test="${chkYn eq 'Y'}"><td class="bodybg_ct"><c:if test="${isAdmin==true || (path eq 'req' && whReqBVo.statCd eq 'R') || (path eq 'recv' && whReqBVo.statCd eq 'R' && !empty whReqBVo.mdId) }"><input type="checkbox" value="${whReqBVo.reqNo }" data-reqNo="${whReqBVo.reqNo }" data-subj="${whReqBVo.subj }" data-regrNm="${whReqBVo.regrNm }" data-regDt="<u:out value="${whReqBVo.regDt }" type="date"/>" /></c:if></td></c:if>
				<td class="body_lt" ><div class="ellipsis" title="${whReqBVo.docNo }"><a href="./${viewPage }.do?${paramsForList}&reqNo=${whReqBVo.reqNo }"><u:out value="${whReqBVo.docNo }"/></a></div></td>
				<c:if test="${path eq 'recv' || path eq 'hdl' || isAdmin==true}">				
				<td class="body_ct"><div class="ellipsis" title="${whReqBVo.mdNm }">${whReqBVo.mdNm }</div></td>		
				<%-- <c:if test="${path eq 'hdl' || isAdmin==true}"><td class="body_ct"><div class="ellipsis" title="${whReqBVo.catNm }">${whReqBVo.catNm }</div></td></c:if> --%>
				</c:if>
				<td class="body_lt" id="subj"><div class="ellipsis" title="${whReqBVo.subj }"><a href="./${viewPage }.do?${paramsForList}&reqNo=${whReqBVo.reqNo }"><u:out value="${whReqBVo.subj }"/></a></div></td>
				<c:if test="${path eq 'recv' || path eq 'hdl' || isAdmin==true}"><td class="body_ct" ><u:out value="${whReqBVo.deptNm }"/></td></c:if>
				<c:if test="${path ne 'req' || isAdmin==true}"><td class="body_ct" ><a href="javascript:viewUserPop('${whReqBVo.regrUid }');"><u:out value="${whReqBVo.regrNm }"/></a></td></c:if>
				<c:if test="${path eq 'req' || path eq 'recv' || path eq 'hdl' || isAdmin == true}"
				><td class="body_ct" id="regrNm"><c:if test="${whReqBVo.statCd ne 'C' && !empty whReqBVo.pichUid}"
				><c:if test="${(isAdmin==true || path ne 'req' || (path eq 'req' && empty envConfigMap.reqPichDtlNotUseYn)) }"
				><a href="javascript:viewUserPop('${whReqBVo.pichUid }');"><u:out value="${whReqBVo.pichNm }" /></a></c:if
				><c:if test="${!(isAdmin==true || path ne 'req' || (path eq 'req' && empty envConfigMap.reqPichDtlNotUseYn)) }"
				><u:out value="${whReqBVo.pichNm }" /></c:if></c:if
				><c:if test="${whReqBVo.statCd eq 'C' && !empty whReqBVo.hdlrUid}"
				><c:if test="${(isAdmin==true || path ne 'req' || (path eq 'req' && empty envConfigMap.reqPichDtlNotUseYn)) }"
				><a href="javascript:viewUserPop('${whReqBVo.hdlrUid }');"><u:out value="${whReqBVo.hdlrNm }" /></a></c:if
				><c:if test="${!(isAdmin==true || path ne 'req' || (path eq 'req' && empty envConfigMap.reqPichDtlNotUseYn)) }"
				><u:out value="${whReqBVo.hdlrNm }" /></c:if></c:if></td></c:if>
				<td class="body_ct"><u:out value="${whReqBVo.reqDt }" type="date"/></td>
				<td class="body_ct"><u:out value="${whReqBVo.recvDt }" type="date"/></td>
				<td class="body_ct"><u:out value="${whReqBVo.cmplDueDt }" type="date"/><c:if test="${whReqBVo.statCd eq 'P' && !empty whReqBVo.cmplDueDt}">
					<fmt:parseDate value="${today }" var="todayValue" pattern="yyyy-MM-dd"/>					
					<fmt:parseDate value="${whReqBVo.cmplDueDt }" var="cmplDueDt" pattern="yyyy-MM-dd"/>
					<c:if test="${todayValue>cmplDueDt }">
						<fmt:parseNumber value="${cmplDueDt.time / (1000*60*60*24)}" integerOnly="true" var="strDate" />
						<fmt:parseNumber value="${todayValue.time / (1000*60*60*24)}" integerOnly="true" var="endDate" />
						<u:set var="delayDate" test="${endDate-strDate >0}" value="${endDate-strDate }일" elseValue=""/>
						<strong style="color:#d68277;">(+${endDate-strDate })</strong>
					</c:if>
				</c:if></td>
				<td class="body_ct"><u:out value="${whReqBVo.cmplDt }" type="date"/></td>
				<td class="body_ct"><u:msg titleId="wh.option.statCd${whReqBVo.statCd }"/></td>
				<%-- <c:if test="${!empty envConfigMap.resEvalUseYn && envConfigMap.resEvalUseYn eq 'Y'}"><td class="body_ct"><u:msg titleId="wh.option.eval${whReqBVo.evalYn }"/></td></c:if> --%>
				<c:if test="${envConfigMap.fileYn eq 'Y' }"><td class="body_ct"><c:if test="${whReqBVo.fileCnt>0}"><a href="javascript:viewFileListPop('${whReqBVo.reqNo}','R');"><u:icon type="att" /></a></c:if></td></c:if>
			</tr>
		</c:forEach>	
	</c:if>
	</tbody>
	</table>
</div>
<u:blank />
<u:pagination />
<% // 하단 버튼 %>
<u:buttonArea>
<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" auth="R" />
<c:if test="${path eq 'req'}">
<c:if test="${isAdmin == false}"><u:button titleId="cm.btn.write" alt="등록" href="./${setPage }.do?${paramsForList }" auth="W" /></c:if>
<c:if test="${isAdmin == true}"><u:button titleId="wh.jsp.listMd.excel" alt="엑셀업로드" href="javascript:setExcelUploadPop();" auth="A" /></c:if>
<u:button titleId="cm.btn.del" alt="삭제" href="javascript:delReq();" />
</c:if>
<c:if test="${chkYn eq 'Y'}"><c:if test="${path eq 'recv' }">
<u:button titleId="wh.btn.recv" alt="접수" onclick="setReqHdlPop('A');" auth="W"/>
<u:button titleId="wh.btn.reject" alt="반려" onclick="setReqHdlPop('G');" auth="W"/>
</c:if>
<%-- <u:button titleId="cm.btn.del" alt="삭제" href="javascript:delLog();" /> --%>
</c:if>
<u:button titleId="cm.btn.excelDown" alt="엑셀다운" onclick="excelDownFile();" auth="R" />
</u:buttonArea>
<form id="excelForm">
	<c:forEach items="${paramEntryList}" var="entry" varStatus="status">
		<u:input type="hidden" id="${entry.key}" value="${entry.value}" />
	</c:forEach>
	<u:input type="hidden" id="listPage" value="${listPage}" />
</form>