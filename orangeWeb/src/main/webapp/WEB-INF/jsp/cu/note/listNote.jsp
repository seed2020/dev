<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="paramsForList" excludes="menuId, sendNo, recvNo, seqNo"/>
<style type="text/css">
.highlight{font-weight:bold;}
</style>
<script type="text/javascript">
<!--<% // [답장버튼] - 등록 팝업 %>
function setReplyPop(recvNo){
	dialog.open('setNoteDialog','<u:msg titleId="cm.cols.send.note" alt="쪽지 보내기" />', './setNotePop.do?menuId=${menuId}&recvNo='+recvNo);
}<% // 검색조건 등록자 선택 %>
function schUserPop(prefix) {
	var $view = $("#searchForm");
	var data = {userUid:$view.find("#sch"+prefix+"Uid").val()};<% // 팝업 열때 선택될 데이타 %>
	var param={data:data};
	<% // option : data, multi, withSub, titleId %>
	parent.searchUserPop(param, function(userVo){
		if(userVo!=null){
			$view.find("#sch"+prefix+"Uid").val(userVo.userUid);
			$view.find("#sch"+prefix+"Nm").val(userVo.rescNm);
		}else{
			return false;
		}
	});
}
<% // [하단버튼:삭제] %>
function delNote() {
	var arr = getCheckedValue("listArea", "cm.msg.noSelect");<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
	if (arr != null && confirmMsg("cm.cfrm.del")) {<% // 삭제하시겠습니까? %>
		callAjax('./transNoteDelAjx.do?menuId=${menuId}', {seqNos:arr}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.replace(location.href);
			}
		});
	}
};
<% // [목록클릭] - 상세보기 화면으로 이동 %>
function viewNotePop(seqNo){
	dialog.open('viewNoteDialog','<u:msg titleId="cm.btn.detl" alt="상세정보" />', './viewNotePop.do?${paramsForList}&seqNo='+seqNo);
	$('#subj_'+seqNo).removeClass('highlight');
}<% // [등록버튼] - 등록 팝업 %>
function setNotePop(){
	dialog.open('setNoteDialog','<u:msg titleId="cm.cols.send.note" alt="쪽지 보내기" />', './setNotePop.do?${paramsForList}');
}<% // [수신확인버튼] - 수신확인 팝업 %>
function listRecvPop(sendNo){
	dialog.open('listRecvDialog','<u:msg titleId="cm.btn.detl" alt="상세정보" />', './listRecvPop.do?menuId=${menuId}&sendNo='+sendNo);
}<% // 검색 조건 초기화 %>
function searchReset(){	
	valueReset('searchTbl',['durCat']);
}
function notEvtBubble(event){
	if(event.stopPropagation) event.stopPropagation(); //MOZILLA
	else event.cancelBubble = true; //IE
}

$(document).ready(function() {
	setUniformCSS();
	$('#listArea tbody:first tr a').click(function(event){
		notEvtBubble(event);
	});
});
//-->
</script>

<u:title menuNameFirst="true"/>

<% // 검색영역 %>
<u:searchArea>
	<form id="searchForm" name="searchForm" action="./listNote.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<table id="searchTbl" class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">
			<u:option value="subj" titleId="cols.subj" alt="제목" checkValue="${param.schCat}" />
			<u:option value="cont" titleId="cols.cont" alt="내용" checkValue="${param.schCat}" />
			</select></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 200px;" /></td>
		<td class="width20"></td>
		<td class="search_tit"><u:msg titleId="cm.cols.send.time" alt="보낸시간"/><u:input type="hidden" id="durCat" value="regDt"/></td>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td><u:calendar id="durStrtDt" option="{end:'durEndDt'}" value="${param.durStrtDt}" /></td>
					<td class="search_body_ct"> ~ </td>
					<td><u:calendar id="durEndDt" option="{start:'durStrtDt'}" value="${param.durEndDt}" /></td>
				</tr>
			</table>
		</td>
		<c:if test="${isRecvNote==true }">
		<td class="width20"></td>
		<td class="search_tit"><u:msg titleId="cm.cols.send.user" alt="보낸사람"/></td>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<u:input id="schRegrNm" value="${param.schRegrNm}" titleId="cols.regr" readonly="Y" />
						<u:input type="hidden" id="schRegrUid" value="${param.schRegrUid}" />
					</td>
					<td><u:buttonS titleId="cm.btn.read" alt="조회" onclick="schUserPop('Regr');" /></td>
				</tr>
			</table>
		</td>
		</c:if><c:if test="${isRecvNote==false }">
		<td class="width20"></td>
		<td class="search_tit"><u:msg titleId="cm.cols.recv.user" alt="받는사람"/></td>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<u:input id="schRecvNm" value="${param.schRecvNm}" titleId="cols.regr" readonly="Y" />
						<u:input type="hidden" id="schRecvUid" value="${param.schRecvUid}" />
					</td>
					<td><u:buttonS titleId="cm.btn.read" alt="조회" onclick="schUserPop('Recv');" /></td>
				</tr>
			</table>
		</td>
		</c:if>
		</tr>
		<tr>
			<td colspan="2"><u:buttonS href="javascript:;" onclick="searchReset();" titleId="cm.btn.srch.reset" alt="검색조건 초기화" /></td>
			<td class="width20"></td>			
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>
<u:set var="colgroup" test="${isRecvNote == true }" value="3%,*,10%,15%,10%,10%" elseValue="3%,*,15%,10%,15%"/>
<u:listArea id="listArea" colgroup="${colgroup }">	
	<tr id="headerTr">
		<td class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td>
		<th class="head_ct"><u:msg titleId="cols.subj" alt="제목"/></th>
		<c:if test="${isRecvNote == true }"><th class="head_ct"><u:msg titleId="cm.cols.send.user" alt="보낸사람"/></th></c:if>
		<th class="head_ct"><c:if test="${isRecvNote == true }"><u:msg titleId="cm.cols.recv.time" alt="받은시간"/></c:if><c:if test="${isRecvNote == false }"><u:msg titleId="cm.cols.send.time" alt="보낸시간"/></c:if></th>
		<th class="head_ct"><c:if test="${isRecvNote == true }"><u:msg titleId="cols.note" alt="비고"/></c:if><c:if test="${isRecvNote == false }"><u:msg titleId="cm.cols.recv.user" alt="받는사람"/></c:if></th>
		<c:if test="${isRecvNote == false }"><th class="head_ct"><u:msg titleId="cm.cols.confirm.time" alt="확인시간"/></th></c:if>
		<c:if test="${isRecvNote == true }"><th class="head_ct"><u:msg titleId="cols.att" alt="첨부"/></th></c:if>
	</tr>
	<c:if test="${fn:length(cuNoteList) == 0}">
		<tr>
		<td class="nodata" colspan="${isRecvNote == true ? 6 : 5}"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
	<c:if test="${fn:length(cuNoteList)>0}">
		<c:forEach items="${cuNoteList}" var="cuNoteRecvBVo" varStatus="status">
			<u:set var="seqNo" test="${isRecvNote == true }" value="${cuNoteRecvBVo.recvNo }" elseValue="${cuNoteRecvBVo.sendNo }"/>
			<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"' onclick="viewNotePop('${seqNo}')">
				<td class="bodybg_ct"><input type="checkbox" value="${seqNo }" onclick="notEvtBubble(event);"/></td>
				<td class="body_lt${isRecvNote==true && cuNoteRecvBVo.readYn=='N' ? ' highlight' : '' }" id="subj_${seqNo }"><u:out value="${cuNoteRecvBVo.subj }"/></td>
				<c:if test="${isRecvNote == true }">
				<td class="body_ct"><a href="javascript:viewUserPop('${cuNoteRecvBVo.regrUid }');">${cuNoteRecvBVo.regrNm }</a></td>
				</c:if>
				<td class="body_ct"><u:out value="${cuNoteRecvBVo.regDt }" type="longdate"/></td>
				<td class="body_ct"><c:if test="${isRecvNote == true && isAdmin ne true}"><u:buttonS titleId="cm.btn.recv.reply" alt="답장" onclick="setReplyPop('${cuNoteRecvBVo.recvNo }');" 
				/></c:if><c:if test="${isRecvNote == false && !empty cuNoteRecvBVo.cuNoteRecvLVoList && fn:length(cuNoteRecvBVo.cuNoteRecvLVoList)>0}"
				><c:if test="${fn:length(cuNoteRecvBVo.cuNoteRecvLVoList)>1 }"><a href="javascript:listRecvPop('${cuNoteRecvBVo.sendNo }');" ><img src="${_cxPth}/images/${_skin}/ico_bottom.png" width="12" height="9"></a></c:if><a href="javascript:viewUserPop('${cuNoteRecvBVo.cuNoteRecvLVoList[0].recvrUid }');">${cuNoteRecvBVo.cuNoteRecvLVoList[0].recvrRescNm }</a></c:if></td>
				<c:if test="${isRecvNote == false }"><td class="body_ct"><c:if test="${!empty cuNoteRecvBVo.cuNoteRecvLVoList && !empty cuNoteRecvBVo.cuNoteRecvLVoList[0].recvDt}"><u:out value="${cuNoteRecvBVo.cuNoteRecvLVoList[0].recvDt }" type="longdate"/></c:if></td></c:if>
				<c:if test="${isRecvNote == true }"><td class="body_ct"><c:if test="${cuNoteRecvBVo.fileCnt > 0}"><u:icon type="att" /></c:if></td></c:if>
			</tr>
		</c:forEach>
	</c:if>
</u:listArea>
<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
<c:if test="${isAdmin ne true }"><u:button titleId="cm.cols.send.note" alt="쪽지 보내기" href="javascript:setNotePop();" auth="W" /></c:if>
<u:button titleId="cm.btn.del" alt="삭제" href="javascript:delNote();" />
</u:buttonArea>
