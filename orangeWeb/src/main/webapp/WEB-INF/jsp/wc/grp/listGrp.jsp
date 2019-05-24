<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<u:set test="${param.fncMy == 'N'}" var="fncMy" value="all" elseValue="my" />

<script type="text/javascript">
<!--

function formSend(uid, mastUid, grpNm){
	$("#schdlGrpId").val(uid);
	$("#mastUid").val(mastUid);
	$("#grpNm").val(grpNm);
    var $form = $('#listGrp');
    $form.attr('method','post');
    $form.attr('action','./listGrpMng.do?menuId=${menuId}');
    $form.submit();

}

function widrGrp(uid){
	
	var $selectRow=$("#"+uid).parent().parent();
	var $selectRowId = $selectRow.find("#schdlGroupId").val(uid);
	
	if(confirmMsg("wc.cfrm.widr")) { <% // wc.cfrm.widr=탈퇴 하시겠습니까? %>
		callAjax('./transListGrpWidr.do?menuId=${menuId}', {schdlGrpId:uid}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.href = './listGrp.do?menuId=${menuId}';
			}
		});
	}
}

function delGrp(uid){
	
	
	var $selectRow=$("#"+uid).parent().parent();
	var $selectRowId = $selectRow.find("#schdlGroupId").val();
	
	if(confirmMsg("cm.cfrm.del")) { <% // cm.cfrm.del=삭제하시겠습니까? %>
		callAjax('./transListGrpDel.do?menuId=${menuId}', {schdlGrpId:uid}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.href = './listGrp.do?menuId=${menuId}';
			}
		});
	}
	
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="wc.jsp.listGrp.${fncMy}.title" alt="나의 그룹 목록" menuNameFirst="true" />
<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listGrp.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="fncMy" value="${fncMy}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="cols.grpNm" alt="그룹명" /></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 400px;" onkeydown="if (event.keyCode == 13) searchForm.submit();"/></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<div class="titlearea">
	<div class="tit_right">
	<ul>
	<c:if test="${fncMy == 'my'}">
	<li><u:buttonS titleId="wc.btn.allGrp" alt="전체그룹" href="./listGrp.do?menuId=${menuId}&fncMy=N" /></li>
	</c:if>
	<c:if test="${fncMy == 'all'}">
	<li><u:buttonS titleId="wc.btn.myGrp" alt="나의그룹" href="./listGrp.do?menuId=${menuId}&fncMy=Y" /></li>
	</c:if>
	</ul>
	</div>
</div>

<% // 목록 %>
<form id = "listGrp">
<u:listArea id="listArea">
	<tr>
		<td width="6%" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
		<td class="head_ct"><u:msg titleId="cols.grpNm" alt="그룹명" /></td>
		<td width="10%" class="head_ct"><u:msg titleId="wc.cols.myStat" alt="나의 상태" /></td>
		<td width="10%" class="head_ct"><u:msg titleId="wc.cols.myAuth" alt="나의 권한" /></td>
		<td width="10%" class="head_ct"><u:msg titleId="cols.mast" alt="마스터" /></td>
		<td width="10%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
		<td width="10%" class="head_ct"><u:msg titleId="wc.cols.delWidr" alt="삭제/탈퇴" /></td>
	</tr>


	<u:msg titleId="cm.msg.preparing" var="msg" alt="준비중.." />
	<c:forEach var="WcSchdlGrpBVo" items ="${wcSchdlGroupBMapList}" varStatus="status">
		<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
		<td class="body_ct" >${WcSchdlGrpBVo.rnum}</td>
		<!-- 여기 수정 -->
		<td class="body_lt">
			<input id="behave" name = "behave" type = "hidden" value ="N"/>
			<input id="schdlGrpId" name="schdlGrpId" type="hidden"/>
			<input id="mastUid" name="mastUid" type="hidden"/>
			<input id="grpNm" name="grpNm" type="hidden" />
			<input id="${WcSchdlGrpBVo.schdlGrpId}" value="${WcSchdlGrpBVo.schdlGrpId}" type="hidden"/>
			<input id="${WcSchdlGrpBVo.mastrUid}" value="${WcSchdlGrpBVo.mastrUid}" type="hidden"/>
			
			<a href="#" onclick="javascript:formSend('${WcSchdlGrpBVo.schdlGrpId}', '${WcSchdlGrpBVo.mastrUid}', '${WcSchdlGrpBVo.grpNm}');"><u:out value="${WcSchdlGrpBVo.grpNm}"/></a>
			
			<%-- <c:if test="${WcSchdlGrpBVo.auth == 'MNG'}">
				<a href="#" onclick="javascript:formSend('${WcSchdlGrpBVo.schdlGrpId}', '${WcSchdlGrpBVo.mastrUid}', '${WcSchdlGrpBVo.grpNm}');"><u:out value="${WcSchdlGrpBVo.grpNm}"/></a>
			</c:if>
				
			<c:if test="${empty WcSchdlGrpBVo.auth}">
				<a href="javascript:alert('<u:msg titleId="wc.msg.not.auth" alt="권한이 존재하지 않습니다." />');"><u:out value="${WcSchdlGrpBVo.grpNm}"/></a>
			</c:if> --%>
			
		
			</td>
		<c:choose>
			<c:when test="${WcSchdlGrpBVo.mbshTypCd == 'O'}">
				<td class="body_ct"><u:msg titleId="wc.cols.grp.opener" alt="개설자" /></td>
			</c:when>
			<c:when test="${WcSchdlGrpBVo.mbshTypCd == 'M'}">
				<td class="body_ct"><u:msg titleId="wc.cols.grp.member" alt="멤버" /></td>
			</c:when>
			<c:otherwise>
				<td class="body_ct"><u:msg titleId="wc.cols.grp.notReg" alt="미가입" /></td>
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${WcSchdlGrpBVo.authGradCd == 'A'}">
				<td class="body_ct"><u:msg titleId="wc.cols.grp.adm" alt="관리" /></td>
			</c:when>
			<c:when test="${WcSchdlGrpBVo.authGradCd == 'W'}">
				<td class="body_ct"><u:msg titleId="wc.cols.grp.wrt" alt="쓰기" /></td>
			</c:when>
			<c:when test="${WcSchdlGrpBVo.authGradCd == 'R'}">
				<td class="body_ct"><u:msg titleId="wc.cols.grp.red" alt="읽기" /></td>
			</c:when>
			<c:otherwise>
				<td class="body_ct"><u:msg titleId="wc.cols.grp.noRight" alt="미권한" /></td>
			</c:otherwise>
		</c:choose>
		<td class="body_ct"><a href="javascript:viewUserPop('${WcSchdlGrpBVo.mastrUid}');"><u:out value="${WcSchdlGrpBVo.regrNm}"/></a></td>
		
		<td class="body_ct">
		<fmt:parseDate var="dateTempParse" value="${WcSchdlGrpBVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
		<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
		</td>
		<c:choose>
			<c:when test="${WcSchdlGrpBVo.mbshTypCd == 'O'}">
				<td class="listicon_ct" ><u:buttonS titleId="cm.btn.del" id="${WcSchdlGrpBVo.schdlGrpId}" alt="삭제" onclick="javascript:delGrp('${WcSchdlGrpBVo.schdlGrpId}');" /></td>
			</c:when>
			<c:when test="${WcSchdlGrpBVo.mbshTypCd == 'M'}">
				<td class="listicon_ct"><u:buttonS titleId="cm.btn.widr" id="${WcSchdlGrpBVo.schdlGrpId}" alt="탈퇴" onclick="javascript:widrGrp('${WcSchdlGrpBVo.schdlGrpId}');" /></td>
			</c:when>
			<c:otherwise>
				<td class="listicon_ct"></td>
				<!-- 
				<td class="listicon_ct"><u:buttonS titleId="cm.btn.del" alt="가입" onclick="alert('${msg}');" /></td>
				 -->
			</c:otherwise>
		</c:choose>
		</tr>
	</c:forEach>
	
	<c:if test="${fn:length(wcSchdlGroupBMapList) == 0}">
			<tr>
				<td class="nodata" colspan="9"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
	</c:if>
	
</u:listArea>
</form>
<u:pagination />

<% // 하단 버튼 %>
<u:msg titleId="wc.cols.grp.Reg" alt="그룹등록" var="grpReg"/>
<u:buttonArea>
	<u:button titleId="cm.btn.reg" alt="등록"  href="javascript:dialog.open('setGrpPop','${grpReg }','./setGrpPop.do?menuId=${menuId}');" />
</u:buttonArea>



