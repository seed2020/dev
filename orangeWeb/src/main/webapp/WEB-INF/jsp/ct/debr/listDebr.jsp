<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<u:msg titleId="cm.msg.preparing" var="msg" alt="준비중.." />
<script type="text/javascript">
<!--

function modDebr(debrId) {
	dialog.open('setDebrPop','<u:msg titleId="ct.btn.debrMod" alt="토론실 수정"/>','./setDebrPop.do?menuId=${menuId}&ctId=${ctId}&debrId='+debrId);
}

function delDebr(debrId) {
	if(confirmMsg("cm.cfrm.del")){
		callAjax('./transDebrDel.do?menuId=${menuId}&ctId=${ctId}', {debrId:debrId}, function(data){
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.href = './listDebr.do?menuId=${menuId}&ctId=${ctId}';
			}
		});
	}
}

function finSitnDebr(debrId, signal) {
	if(signal == 'fin'){
		if(confirmMsg("ct.cfrm.fin")){
			callAjax('./setDebrFinSitn.do?menuId=${menuId}&ctId=${ctId}', {debrId:debrId, signal:signal}, function(data){
				if (data.message != null) {
					alert(data.message);
				}
				if (data.result == 'ok') {
					location.href = './listDebr.do?menuId=${menuId}&ctId=${ctId}';
				}
			});
		}
	}else{
		if(confirmMsg("ct.cfrm.sitn")){
			callAjax('./setDebrFinSitn.do?menuId=${menuId}&ctId=${ctId}', {debrId:debrId, signal:signal}, function(data){
				if (data.message != null) {
					alert(data.message);
				}
				if (data.result == 'ok') {
					location.href = './listDebr.do?menuId=${menuId}&ctId=${ctId}';
				}
			});
		}
	}
	
}

function listOpin(debrId) {
	location.href = "./listOpin.do?menuId=${menuId}&ctId=${ctId}&debrId="+debrId;
}

function setOpin(debrId, finYn) {
	if(finYn == 'Y'){
		alertMsg("ct.msg.debr.closed"); <% //ct.msg.debr.closed = 이미 마감된 토론입니다. %>
	}else{
		dialog.open('setOpinPop','<u:msg titleId="ct.btn.opinReg" alt="의견 등록"/>','./setOpinPop.do?menuId=${menuId}&ctId=${ctId}&debrId='+debrId);
	}
	
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title title="${menuTitle }"  alt="토론실" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listDebr.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="ctId" name="ctId" value="${ctId}" />
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">
		<u:option value="TOPC" titleId="cols.topc" alt="주제" checkValue="${param.schCat}" />
		<u:option value="ITNT" titleId="cols.itnt" alt="취지" checkValue="${param.schCat}" />
		<u:option value="REGR_NM" titleId="cols.regr" alt="등록자" checkValue="${param.schCat}" />
		</select></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 400px;" /></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<% // 목록 %>
<div class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="width:100%;table-layout:fixed;">
		<tr>
			<td width="6%" class="head_ct"><u:msg titleId="ct.cols.roomNo" alt="방번호" /></td>
			<td class="head_ct"><u:msg titleId="cols.topc" alt="주제" /></td>
			<td width="10%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
			<td width="10%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
			<td width="10%" class="head_ct"><u:msg titleId="cols.opin" alt="의견" /></td>
			<td width="10%" class="head_ct"><u:msg titleId="cols.sitn" alt="회기" /></td>
		</tr>
		<c:forEach var="ctDebrVo" items="${ctDebrList}" varStatus="status">
			<tr>
				<td rowspan="3" class="body_ct">${recodeCount - ctDebrVo.rnum + 1}</td>
				<td class="body_lt">
					<div class="ellipsis" title="<u:out value="${ctDebrVo.subj}" />">
						<u:out value="${ctDebrVo.subj}" />
					</div>
				</td>
				<td class="body_ct"><a href="javascript:viewUserPop('${ctDebrVo.regrUid}');">${ctDebrVo.regrNm}</a></td>
				<td class="body_ct">
					<fmt:parseDate var="dateTempParse" value="${ctDebrVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
					<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
				</td>
				<td class="body_ct">${ctDebrVo.opinCnt}</td>
				<td class="body_ct">
					<c:if test="${ctDebrVo.finYn == 'Y'}">
						<u:msg titleId="ct.cols.finish" alt="마감"/>
					</c:if>
					<c:if test="${ctDebrVo.finYn == 'N'}">
						${ctDebrVo.sitn}<u:msg titleId="ct.cols.week" alt="주"/>
					</c:if>
					
				</td>
			</tr>
			<tr>
				<td colspan="5" class="body_lt">
					<div class="ellipsis" title="<u:out value="${ctDebrVo.estbItnt}" />">
						<u:out value="${ctDebrVo.estbItnt}" />
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="5" style="padding: 2px 0 2px 0;">
					<div style="float: left">
						<table border="0" cellpadding="0" cellspacing="0">
							<tbody>
								<tr>
									<c:if test="${!empty authChkR && authChkR == 'R' }">
										<td><u:buttonS titleId="ct.btn.opinList" alt="의견목록" onclick="listOpin('${ctDebrVo.debrId}');" /></td>
									</c:if>
									<c:if test="${!empty authChkW && authChkW == 'W' }">
										<td><u:buttonS titleId="ct.btn.opinReg" alt="의견등록" onclick="setOpin('${ctDebrVo.debrId}', '${ctDebrVo.finYn}');" /></td>
									</c:if>
								</tr>
							</tbody>
						</table>
					</div>
					<div style="float: right; padding-right: 2px;">
						<table border="0" cellpadding="0" cellspacing="0">
							<tbody>
								<tr>
									<c:if test="${!empty myAuth && myAuth == 'M' || ctDebrVo.regrUid == logUserUid}">
										<td><u:buttonS titleId="ct.btn.sitnExtd" alt="회기연장" onclick="finSitnDebr('${ctDebrVo.debrId}', 'sitn');" /></td>
										<td><u:buttonS titleId="ct.btn.fin" alt="마감" onclick="finSitnDebr('${ctDebrVo.debrId}', 'fin');" /></td>
										<td><u:buttonS titleId="cm.btn.mod" alt="수정" onclick="modDebr('${ctDebrVo.debrId}');" /></td>
										<td><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="delDebr('${ctDebrVo.debrId}');" /></td>
									</c:if>
								</tr>
							</tbody>
						</table>
					</div>
				</td>
			</tr>
		</c:forEach>
		<c:if test="${fn:length(ctDebrList) == 0 }">
			<tr>
				<td class="nodata" colspan="6"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>	
		</c:if>
	</table>
</div>
<u:blank />

<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
	<c:if test="${!empty authChkW && authChkW == 'W' }">
		<u:msg titleId="ct.btn.debrReg" alt="토론방등록" var="debrReg"/>
		<u:button titleId="ct.btn.debrReg" alt="토론방등록" href="javascript:dialog.open('setDebrPop','${debrReg}','./setDebrPop.do?menuId=${menuId}&ctId=${ctId}');"/>
	</c:if>
</u:buttonArea>
