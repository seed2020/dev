<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="params" excludes="brdId" />
<script type="text/javascript">
<!--
<% // [하단버튼:설정] %>
function setEnvPop(){
	dialog.open('setEnvDialog','<u:msg titleId="cm.option.config" alt="설정" />','./setEnvPop.do?menuId=${menuId}');
}
<% // 엑셀 파일 다운로드 %>
function excelDownFile() {
	var $form = $('#excelForm');
	$form.attr('method','post');
	$form.attr('action','./excelDownLoad.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form[0].submit();
};
<% // 검색조건 등록자 선택 %>
function findRegr() {
	var $view = $("#searchForm");
	var data = {userUid:$view.find("#schRegrUid").val()};<% // 팝업 열때 선택될 데이타 %>
	<% // option : data, multi, withSub, titleId %>
	searchUserPop({data:data}, function(userVo){
		if(userVo!=null){
			$view.find("#schRegrUid").val(userVo.userUid);
			$view.find("#schRegrNm").val(userVo.rescNm);
			// alert(new ParamMap(userVo));
		}else{
			return false;
		}
	});
}<%// 부서 선택 %>
function openSingOrg(id){
	var data = [];
	searchOrgPop({data:data}, function(orgVo){
		if(orgVo!=null){
			$('#schOrgId').val(orgVo.orgId);
			$('#schOrgNm').val(orgVo.rescNm);
		}
	});
}
<% // [목록:제목] 게시물 조회 %>
function viewBull(id) {
	location.href = './viewBullMng.do?${params}&brdId=${baBrdBVo.brdId}&bullId=' + id;
}
<% // [목록:제목] 보안글 조회를 위한 로그인폼 화면 %>
function openLogin(id) {
	dialog.open('setLoginPop','<u:msg titleId="bb.jsp.setLoginPop.title" alt="보안글 인증" />','./setLoginPop.do?${params}&viewPage=viewBullMng&brdId=${baBrdBVo.brdId}&bullId=' + id);
}
<% // [목록:조회수] 조회이력 %>
function readHst(id) {
	dialog.open('listReadHstPop','<u:msg titleId="bb.jsp.listReadHstPop.title" alt="조회자 정보" />','./listReadHstPop.do?menuId=${menuId}&bullId=' + id);
}
<% // [하단버튼:공지지정] 공지 지정 %>
function setNotc() {
	var arr = getCheckedValue("listArea", "cm.msg.noSelect");<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
	if (arr != null) {
		callAjax('./transNotcAjx.do?menuId=${menuId}', {brdId:'${baBrdBVo.brdId}', bullIds:arr}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.replace(location.href);
			}
		});
	}
}
<% // 선택된 게시물 중에 답글이 있는가? %>
function hasPid() {
	var checked = $('#listArea input:checked');
	for (var i = 0; i < checked.length; i++) {
		var replyDpth = $(checked[i]).parents('tr:first').find('#replyDpth').val();
		if (replyDpth > 0) {
			return true;
		}
	}
	return false;
}
<% // [하단버튼:게시물이동|게시물복사] %>
function selectBb(callback) {
	if (hasPid()) {
		if (callback == 'moveBull') alertMsg("bb.msg.moveBull.hasPid");<% // bb.msg.moveBull.hasPid=답글은 원글없이 이동할 수 없습니다. 원글 이동시 답글도 함께 이동합니다. %>
		if (callback == 'copyBull') alertMsg("bb.msg.copyBull.hasPid");<% // bb.msg.copyBull.hasPid=답글은 복사할 수 없습니다. %>
		return;
	}

	var arr = getCheckedValue("listArea", "cm.msg.noSelect");<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
	if (arr != null) {
		var mul = (callback == 'moveBull') ? '&mul=N' : '&mul=Y';
		var params = '&brdId=${baBrdBVo.brdId}' + mul + '&callback=' + callback + '&callbackArgs=' + arr;
		dialog.open('selectBbPop','<u:msg titleId="bb.jsp.selectBb.title" alt="게시판 선택" />','./selectBbPop.do?menuId=${menuId}' + params);
	}
}
<% // 게시물 이동 %>
function moveBull(brdIds, brdNms, bullIds) {
	var bullIds = bullIds.split(',');
	callAjax('./transBullMoveAjx.do?menuId=${menuId}', {brdId:'${baBrdBVo.brdId}', targetBrdId:brdIds[0], bullIds:bullIds}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			location.replace(location.href);
		}
	});
}
<% // 게시물 복사 %>
function copyBull(brdIds, brdNms, bullIds) {
	var bullIds = bullIds.split(',');
	callAjax('./transBullCopyAjx.do?menuId=${menuId}', {brdId:'${baBrdBVo.brdId}', brdIds:brdIds, bullIds:bullIds}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			location.replace(location.href);
		}
	});
}
<% // [하단버튼:파일다운로드] 파일다운로드 %>
function downFile(id) {
	var arr = getCheckedValue("listArea", "cm.msg.noSelect");<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
	if (arr != null && confirmMsg("bb.cfrm.down")) {<% // bb.cfrm.down=다운받으시겠습니까? %>
		var $form = $('#downForm');
		$form.find('input[name="bullIds"]').val(arr);
		$form.attr('method','post');
		$form.attr('action','./downFile.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
}
<% // [하단버튼:삭제] 게시물 삭제 %>
function delBull() {
	var arr = getCheckedValue("listArea", "cm.msg.noSelect");<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
	<c:if test="${isSysAdmin == true}">
	if (arr != null && confirmMsg("bb.msg.del.all")) {<% // 답변이 있을경우 답변글도 같이 삭제됩니다.\n삭제하시겠습니까? %>
		callAjax('./transBullDelAjx.do?menuId=${menuId}', {brdId:'${baBrdBVo.brdId}', bullIds:arr}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.replace(location.href);
			}
		});
	}
	</c:if>
	<c:if test="${isSysAdmin == false}">
		callAjax('./transBullDelAjxChk.do?menuId=${menuId}', {brdId:'${baBrdBVo.brdId}', bullIds:arr}, function(data) {
			if (data.message != null) {
				alert(data.message);
				return;
			}
			if (data.result == 'ok') {
				if (confirmMsg("cm.cfrm.del")) {	<% // 삭제하시겠습니까? %>
					callAjax('./transBullDelAjx.do?menuId=${menuId}', {brdId:'${baBrdBVo.brdId}', bullIds:arr}, function(data) {
						if (data.message != null) {
							alert(data.message);
						}
						if (data.result == 'ok') {
							location.replace(location.href);
						}
					});
				}
			}
		});
	</c:if>
}

<% // [팝업] - 로그인 팝업 사용자%>
function setLginPopUser(){
	dialog.open('setLginPopUserDialog','<u:msg titleId="bb.btn.lginPopUser" alt="로그인팝업 사용자" />','./setLginPopUserPop.do?${params}');
}
<% // [팝업] 파일목록 조회 %>
function viewFileListPop(id) {
	dialog.open('viewFileListDialog','<u:msg titleId="cols.att" alt="첨부" />','./viewFileListPop.do?menuId=${menuId}&brdId=${param.brdId}&bullId='+id);
}
<%// [버튼] - 등록자변경 %>
function setRegChn(){
	var bullIds = getCheckedValue("listArea", "cm.msg.noSelect");<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
	if(bullIds == null) return;
	<% // option : data, multi, titleId %>
	searchUserPop({data:null, multi:false, mode:'search'}, function(vo){
		if(vo!=null){
			callAjax('./transBullChnAjx.do?menuId=${menuId}&brdId=${baBrdBVo.brdId}', {brdId:'${baBrdBVo.brdId}', bullIds:bullIds, regrUid:vo.userUid}, function(data) {
				if (data.message != null) {
					alert(data.message);
				}
				if (data.result == 'ok') {
					location.replace(location.href);
				}
			});
		}
	});
}
<%
//이벤트 버블 방지 %>
function notEvtBubble(event){
	if(event.stopPropagation) event.stopPropagation(); //MOZILLA
	else event.cancelBubble = true; //IE
}
$(document).ready(function() {
	setUniformCSS();
	$('#listArea tbody:first tr').find('input[type="checkbox"], a').on('click', function(event){
		notEvtBubble(event);
	});
	
});
//-->
</script>

<u:title titleId="bb.jsp.listBullMng.title" alt="게시물 관리" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form id="searchForm" name="searchForm" action="./listBullMng.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="cols.bb" alt="게시판" /></td>
		<td><select name="brdId" onchange="searchForm.submit();">
			<c:forEach items="${baBrdBVoList}" var="brdVo" varStatus="status">
			<u:option value="${brdVo.brdId}" title="${brdVo.rescNm}" checkValue="${baBrdBVo.brdId}" />
			</c:forEach>
			</select></td>
		<td class="width20"></td>
		<td class="search_tit"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
		<td><table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td><u:calendar id="strtYmd" option="{end:'endYmd'}" value="${param.strtYmd}" /></td>
			<td class="search_body_ct"> ~ </td>
			<td><u:calendar id="endYmd" option="{start:'strtYmd'}" value="${param.endYmd}" /></td>
			</tr>
			</table></td>
		<td class="width20"></td>
		<td class="search_tit"><u:msg titleId="cols.valdPrd" alt="유효기간" /></td>
		<td><table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<u:checkbox name="schExpr" value="Y" titleId="cols.expr" checkValue="${param.schExpr}" textClass="search_body" />
			</tr>
			</table></td>
		</tr>

		<tr>
		<td class="search_tit"><u:msg titleId="cols.regr" alt="등록자" /></td>
		<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			<td><u:input id="schRegrNm" value="${param.schRegrNm}" titleId="cols.pich" readonly="Y" />
				<u:input type="hidden" id="schRegrUid" value="${param.schRegrUid}" /></td>
			<td><u:buttonS titleId="cm.btn.search" alt="검색" onclick="findRegr();" /></td>
			</tr>
			</tbody></table></td>
		<td></td>
		<td class="search_tit"><u:msg titleId="cols.subj" alt="제목" /></td>
		<td colspan="2"><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 234px;" onkeydown="if (event.keyCode == 13) searchForm.submit();" />
			<u:input type="hidden" id="schCat" value="SUBJ" />
			</td>
			<td class="search_tit"><u:msg titleId="cols.dept" alt="부서" /></td>
			<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
				<tr>
				<td><u:input id="schOrgNm" value="${param.schOrgNm}" titleId="cols.pich" readonly="Y" />
					<u:input type="hidden" id="schOrgId" value="${param.schOrgId}" /></td>
				<td><u:buttonS titleId="cm.btn.search" alt="검색" onclick="openSingOrg();" /></td>
				</tr>
				</tbody></table></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>

	</form>
</u:searchArea>

<u:blank />

<% // 목록 %>
<u:listArea id="listArea">

	<tr>
	<td width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td>
	<td width="6%" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
	<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
	<td width="14%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	<td width="6%" class="head_ct"><u:msg titleId="cols.readCnt" alt="조회수" /></td>
	<td width="6%" class="head_ct"><u:msg titleId="cols.att" alt="첨부" /></td>
	</tr>

<c:if test="${fn:length(bbBullLVoList) == 0}">
	<tr>
	<td class="nodata" colspan="7"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(bbBullLVoList) > 0}">
	<c:forEach items="${bbBullLVoList}" var="bbBullLVo" varStatus="status">
	<u:set test="${bbBullLVo.secuYn == 'Y'}" var="viewBull" value="openLogin" elseValue="viewBull" />
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"' onclick="${viewBull}('${bbBullLVo.bullId}');" style="cursor:pointer;">
	<td class="bodybg_ct"><input type="checkbox" value="${bbBullLVo.bullId}" />
		<u:input type="hidden" id="replyDpth" value="${bbBullLVo.replyDpth}" />
		</td>
	<td class="body_ct">
		<c:if test="${bbBullLVo.notcYn == 'Y'}"><u:icon type="notc" /></c:if>
		<c:if test="${bbBullLVo.notcYn != 'Y'}"><u:out value="${recodeCount - bbBullLVo.rnum + 1}" type="number" /></c:if>
		</td>
	<td class="body_lt">
		<u:icon type="indent" display="${bbBullLVo.replyDpth > 0}" repeat="${bbBullLVo.replyDpth - 1}" />
		<u:icon type="reply" display="${bbBullLVo.replyDpth > 0}" />
		<u:icon type="new" display="${bbBullLVo.newYn == 'Y'}" />
		<u:set test="${bbBullLVo.secuYn == 'Y' || bbBullLVo.ugntYn == 'Y'}" var="style" value="color:red;" elseValue="" />
		<c:if test="${bbBullLVo.secuYn == 'Y'}"><span style="${style}">[<u:msg titleId="bb.option.secu" alt="보안" />]</span></c:if>
		<c:if test="${bbBullLVo.ugntYn == 'Y'}"><span style="${style}">[<u:msg titleId="bb.option.ugnt" alt="긴급" />]</span></c:if>
		<u:out value="${bbBullLVo.subj}" maxLength="80" />
		<c:if test="${baBrdBVo.cmtYn == 'Y'}"><span style="font-size: 10px;">(<u:out value="${bbBullLVo.cmtCnt}" type="number" />)</span></c:if>
		</td>
	<td class="body_ct">
		<c:if test="${baBrdBVo.brdTypCd == 'N'}">
		<a href="javascript:viewUserPop('${bbBullLVo.regrUid}');"><u:out value="${bbBullLVo.regrNm}" /></a>
		</c:if>
		<c:if test="${baBrdBVo.brdTypCd == 'A'}">
		<u:out value="${bbBullLVo.anonRegrNm}" />
		</c:if>
		</td>
	<td class="body_ct"><u:out value="${bbBullLVo.regDt}" type="longdate" /></td>
	<td class="body_ct"><a href="javascript:readHst('${bbBullLVo.bullId}');"><u:out value="${bbBullLVo.readCnt}" type="number" /></a></td>
	<td class="body_ct"><c:if test="${bbBullLVo.fileCnt > 0}"><a href="javascript:viewFileListPop('${bbBullLVo.bullId }');"><u:icon type="att" /></a></c:if></td>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>

<u:pagination />

<u:set var="auth" test="${baBrdBVo.allCompYn eq 'Y'}" value="SYS" elseValue="A"/>

<% // 하단 버튼 %>
<u:buttonArea>
	<%-- <u:button titleId="cm.btn.setup" alt="설정" href="javascript:setEnvPop();" auth="A" /> --%>
	<u:button titleId="bb.btn.regChn" alt="등록자변경" onclick="setRegChn();" />
	<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" auth="R" />
	<u:button titleId="cm.btn.excelDown" alt="엑셀다운" onclick="excelDownFile();" auth="R" />
	<u:button titleId="bb.btn.lginPopUser" alt="로그인팝업사용자" href="javascript:setLginPopUser();" auth="A" />
	<u:button titleId="bb.btn.notcApnt" alt="공지지정" href="javascript:setNotc();" auth="${auth}" />
	<u:button titleId="bb.btn.bullMove" alt="게시물이동" href="javascript:selectBb('moveBull');" auth="${auth}" />
	<u:button titleId="bb.btn.bullCopy" alt="게시물복사" href="javascript:selectBb('copyBull');" auth="${auth}" />
	<%-- <u:button titleId="bb.btn.fileDown" alt="파일다운" href="javascript:downFile();" auth="${auth}" /> --%>
	<u:button titleId="cm.btn.del" alt="삭제" href="javascript:delBull();" auth="${auth}" />
</u:buttonArea>

<form id="downForm" name="downForm"><input type="hidden" name="bullIds"></form>
<form id="excelForm">
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="brdId" value="${baBrdBVo.brdId }" />	
	<u:input type="hidden" id="strtYmd" value="${param.strtYmd}" />
	<u:input type="hidden" id="endYmd" value="${param.endYmd}" />
	<c:if test="${param.schExpr eq 'Y' }"><u:input type="hidden" id="schExpr" value="${param.schExpr}" /></c:if>
	<u:input type="hidden" id="schRegrNm" value="${param.schRegrNm}" />
	<u:input type="hidden" id="schRegrUid" value="${param.schRegrUid}" />
	<u:input type="hidden" id="schCat" value="SUBJ" />
	<u:input type="hidden" id="schWord" value="${param.schWord}" />
</form>