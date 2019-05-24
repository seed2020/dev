<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="pageSuffix" test="${lstTyp eq 'L' }" value="" elseValue="Frm"/>
<u:set var="frmYn" test="${!empty pageSuffix && pageSuffix == 'Frm' }" value="Y" elseValue="N"/>
<script type="text/javascript">
<!--<%// [팝업] 보내기 %>
function send(){
	var arrs = selDocIds('docId');
	if(arrs == null) return;
	var url = './sendPsnPop.do?${params}&docTyp=psn&dialog=sendPop&multi=${multi}&docId='+arrs.join(',');
	parent.dialog.open('sendPop', '<u:msg titleId="cm.btn.send" alt="보내기" />', url);
};<%// [팝업] 이동 %>
function move(){
	var arrs = selDocIds('docId');
	if(arrs == null) return;
	var url = './sendPsnPop.do?${params}&docTyp=psn&dialog=sendPop&mode=move&multi=${multi}&docId='+arrs.join(',');
	parent.dialog.open('sendPop', '<u:msg titleId="dm.jsp.send.move.title" alt="문서이동" />', url);
};<% // [하단버튼:배열] %>
function getRightBtnList(){
	var $area = $("#rightBtnArea");
	return $area.find('ul')[0].outerHTML;
}<%// [목록 URL] 조회 %>
function getPageUrl(page){
	return $('#'+page).val();
}<%// [우하단 버튼] 등록 %>
function setDoc(seq){
	var url = './${setPage}.do?${paramsForList }';
	if(seq != undefined) url+= '&docId='+seq;
	location.href = url;
}<% // [목록:제목] 상세 조회 %>
function viewDoc(id) {
	location.href = './${viewPage}.do?${paramsForList }&docId=' + id;
}<%
// 탭 클릭 - 목록:L/폴더:F/분류:C %>
function toggleTabBtn(tabCd){
	var $form = $("#searchForm");
	$form.find("input[name='lstTyp']").remove();
	$form.appendHidden({name:'lstTyp',value:tabCd});
	$form.submit();
}<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(noSelectMsg){
	var arr=[], id, obj;
	$("#listArea tbody:first input[type='checkbox']:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
};<%// 선택목록 리턴 %>
function selDocIds(idNm){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) return selRowInArr(arr, idNm);
	else return null;
};
<%// 배열에 담긴 목록%>
function selRowInArr(rowArr, idNm){
	var objArr = [], $docId;
	for(var i=0;i<rowArr.length;i++){
		$docId = $(rowArr[i]).find("input[name='"+idNm+"']");
		if($docId.val()!=''){
			objArr.push($docId.val());
		}
	}
	return objArr;
};<% // [하단버튼:삭제] %>
function delDocList(statCd) {
	var arrs = selDocIds('docId');
	if(arrs == null) return;
	if (confirmMsg("cm.cfrm.del")) {	<% // cm.cfrm.del=삭제하시겠습니까? %>
		callAjax('./transPsnDocDelAjx.do?menuId=${menuId}', {docId:arrs.join(',')}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.replace(location.href);
			}
		});
	}
}<%// 저장, 삭제시 리로드 %>
function reloadDocFrm(url, dialogId){
	//팝업 닫기
	if(dialogId != undefined && dialogId != null && dialogId !='') {
		if(dialogId == 'all') parent.dialog.closeAll();
		else parent.dialog.close(dialogId);
	}
	
	if(url != undefined && url != null) location.replace(url);
	else location.replace(location.href);
};
$(document).ready(function() {
	/* <c:if test="${pageSuffix eq 'Frm'}">parent.applyDocBtn('list','${authJson }');</c:if> */
	setUniformCSS();
	<c:if test="${pageSuffix eq 'Frm'}">parent.applyDocBtn();</c:if>
	<c:if test="${frmYn eq 'Y' && (param.lstTyp eq 'C' || param.lstTyp eq 'F') && (!empty param.fldId || !empty param.clsId)}">parent.setParamNmId("${param.lstTyp eq 'C' ? param.clsId : param.fldId}");</c:if>
	});
//-->
</script>
<c:if test="${empty pageSuffix}">
<u:title titleId="dm.jsp.search.doc.title" alt="문서조회" menuNameFirst="true" />

<form id="searchForm" name="searchForm" action="./${listPage }.do" style="padding:10px;">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="lstTyp" value="${lstTyp}" />
<c:if test="${!empty paramEntryList}">
<c:forEach items="${paramEntryList}" var="entry" varStatus="status">
	<u:input type="hidden" id="${entry.key}" value="${entry.value}" />
</c:forEach>
</c:if>
</form>
<jsp:include page="/WEB-INF/jsp/dm/psn/listDocSrch.jsp" />

<c:if test="${fn:length(tabList) > 1}">
<u:tabGroup noBottomBlank="${true}">
	<c:forEach var="tab" items="${tabList }" varStatus="status">
		<u:tab alt="목록전체" titleId="dm.cols.listTyp.${tab[1] }"
		on="${lstTyp == tab[0]}"
		onclick="toggleTabBtn('${tab[0] }');" />
	</c:forEach>
</u:tabGroup>
<u:blank />
</c:if>
</c:if>

<u:set var="style" test="${pageSuffix eq 'Frm' }" value="style='padding:10px;'" elseValue="style='padding-top:10px;'"/>
<form id="listForm" action="./listDoc.do" ${style }>
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="lstTyp" value="${lstTyp}" />
<c:set var="colSpan" value="${fn:length(itemDispList) }"/>
<u:listArea id="listArea" >
	<tr id="headerTr">
		<td width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td>
		<c:forEach var="dispVo" items="${itemDispList }" varStatus="status">
			<u:set var="wdthPerc" test="${empty dispVo.wdthPerc }" value="17%" elseValue="${dispVo.wdthPerc}%"/>
			<td width="${dispVo.atrbId eq 'subj' ? '' : wdthPerc}" class="head_ct">${dispVo.colmVo.itemDispNm }</td>
		</c:forEach>
		<c:if test="${fn:startsWith(path,'listTmp')}"><c:set var="colSpan" value="${colSpan +1}"/><td class="head_ct"><u:msg titleId="cols.note" alt="비고" /></td></c:if>
		<c:if test="${fn:startsWith(path,'listSubm') || fn:startsWith(path,'listDisc')}"><c:set var="colSpan" value="${colSpan +1}"/><td width="6%" class="head_ct"><u:msg titleId="cols.stat" alt="상태" /></td></c:if>
	</tr>
	<c:set var="viewAtrbs" value="subj,fldNm,clsNm,docNo,verVar"/><!-- 상세화면 비교 컬럼명-->
	<c:set var="uidAtrbs" value="regr,modr,ownr"/><!-- 사용자 비교 컬럼명 -->
	<c:forEach var="map" items="${mapList}" varStatus="status">
		<c:set var="map" value="${map }" scope="request"/>
		<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"' >
			<td class="bodybg_ct"><u:checkbox name="docChk" value="${map.docId }" checked="false" /><u:input type="hidden" name="docId" value="${map.docId }"/></td>
			<c:forEach var="dispVo" items="${itemDispList }" varStatus="status">				
				<u:convertMap var="value" srcId="map" attId="${dispVo.atrbId}" type="html" />
				<td class="body_lt" align="${dispVo.alnVa}" ${viewFunc }>
					<c:if test="${dispVo.atrbId eq 'subj' }">
						<c:if test="${map.statCd == 'W' }"><u:icon type="lock" /></c:if>
						<c:if test="${map.newYn == 'Y' }"><u:icon type="new" /></c:if>
						<c:if test="${map.keepDdlnYn == 'Y' }"><u:icon type="notc" /></c:if>
					</c:if>
					<%-- <c:if test="${dispVo.atrbId eq 'subj' && map.keepDdlnYn == 'Y'}"><span>[<u:msg titleId="dm.option.keepDdln" alt="보존연한도래" />]</span></c:if> --%>
					<c:choose>
						<c:when test="${dispVo.colmVo.itemTyp eq 'TEXTAREA' }"><div class="ellipsis" title="${value }">${value }</div></c:when>
						<c:when test="${fn:contains(uidAtrbs,fn:substring(dispVo.atrbId,0,4)) }">
							<u:convertMap var="valueUid" srcId="map" attId="${fn:substring(dispVo.atrbId,0,4)}Uid" type="html" />
							<a href="javascript:viewUserPop('${valueUid}');">${value }</a>
						</c:when>
						<c:when test="${fn:endsWith(dispVo.atrbId,'Dt') || dispVo.colmVo.dataTyp eq 'DATETIME' || dispVo.colmVo.itemTyp eq 'CALENDAR' }"><u:out value="${value }" type="longdate"/></c:when>
						<c:when test="${dispVo.colmVo.itemTyp eq 'FILE' }"><c:if test="${value > 0}"><u:icon type="att" /></c:if></c:when>
						<c:otherwise><c:if test="${dispVo.colmVo.itemTyp == 'CODE'}">
							<c:forEach items="${dispVo.colmVo.cdList}" var="cd" varStatus="status">
								<c:if test="${cd.cdId == value}">${cd.rescNm }</c:if>
							</c:forEach>
						</c:if><u:set var="setFunc" test="${map.statCd eq 'T' }" value="setDoc('${map.docId}');" elseValue="viewDoc('${map.docId}');"/>
						<c:if test="${dispVo.colmVo.itemTyp != 'CODE'}"><u:set var="linkStyle" test="${fn:contains(viewAtrbs,dispVo.atrbId) }" value="onclick=\"${setFunc }\" style='cursor:pointer;'" elseValue=""/>
						<c:if test="${dispVo.atrbId eq 'subj' && map.subYn eq 'Y'}"><u:icoCurr display="true" /></c:if>
						<c:if test="${!empty linkStyle }">
						<a href="javascript:;" title="${value}" ${linkStyle }>${value }</a></c:if><c:if test="${empty linkStyle }">
						${value }</c:if></c:if></c:otherwise>
					</c:choose>
				</td>
			</c:forEach>
			<c:if test="${fn:startsWith(path,'listTmp')}"><td class="body_ct" ><u:buttonS titleId="cm.btn.mod" href="javascript:setDoc('${map.docId}');" alt="수정" auth="W" /></td></c:if>
			<c:if test="${fn:startsWith(path,'listSubm') || fn:startsWith(path,'listDisc')}"><td class="body_ct" ><u:msg titleId="dm.cols.docStat${map.statCd }" alt="대기/반려/승인" /></td></c:if>
		</tr>
	</c:forEach>
	<c:if test="${empty mapList }">
		<tr>
		<td class="nodata" colspan="${colSpan+1 }"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
	
</u:listArea>
<u:blank />
<u:pagination />
</form>

<u:blank />
<u:set var="rightBtnDisplay" test="${frmYn eq 'Y' }" value="display:none;" elseValue=""/>
<% // 하단 버튼 %>
<u:buttonArea id="rightBtnArea" style="${rightBtnDisplay }">
	<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" auth="R" />
	<u:button titleId="dm.cols.auth.send" href="javascript:send();" alt="보내기" auth="W"/>
	<c:if test="${isAdmin == false }"><u:button titleId="dm.cols.auth.move" href="javascript:move();" alt="이동" auth="W"/></c:if>
	<u:button titleId="dm.cols.auth.disuse" href="javascript:delDocList('F');" alt="완전삭제" auth="A"/>
	<c:if test="${isAdmin == false }"><u:button id="setDocBtn" titleId="cm.btn.reg" alt="등록" href="javascript:setDoc();" auth="W" /></c:if>
</u:buttonArea>
