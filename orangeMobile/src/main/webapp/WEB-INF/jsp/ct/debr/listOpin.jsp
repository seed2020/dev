<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[
function goList() {
	$m.nav.prev(event, '/ct/debr/listDebr.do?menuId=${menuId}&ctId=${ctId}');
}

function setOpin(debrId, opinOrdr) {
	if('${ctDebrBVo.finYn}' == 'Y'){
		$m.msg.alertMsg("ct.msg.debr.closed");
		<% //ct.msg.debr.closed = 이미 마감된 토론입니다. %>
	}
	else{
		$m.nav.next(event, '/ct/debr/setOpin.do?menuId=${menuId}&ctId=${ctId}&debrId='+debrId+'&opinOrdr='+opinOrdr);
	}
}

function viewOpin(debrId, opinOrdr) {
	$m.nav.next(event, '/ct/debr/viewOpin.do?menuId=${menuId}&ctId=${ctId}&debrId='+debrId+'&opinOrdr='+opinOrdr);
}

function modDebr(debrId) {
	$m.nav.next(event,"/ct/debr/setDebr.do?menuId=${menuId}&ctId=${ctId}&debrId="+debrId);
}


function delDebr(debrId) {
	$m.dialog.confirm('<u:msg titleId="cm.cfrm.del" alt="삭제하시겠습니까?"  />', function(result){
	if(result){
	$m.ajax('/ct/debr/transDebrDel.do?menuId=${menuId}&ctId=${ctId}', {debrId:debrId}, function(data) {
	if (data.message != null) {
	$m.dialog.alert(data.message);
	}
	if (data.result == 'ok') {
	$m.nav.next(event, '/ct/debr/listDebr.do?menuId=${menuId}&ctId=${ctId}');
	}
	});
	}
	});

}

function finSitnDebr(debrId, signal) {
	if(signal == 'fin'){

		$m.dialog.confirm('<u:msg titleId="ct.cfrm.fin"   />', function(result){
		if(result){
		$m.ajax('/ct/debr/setDebrFinSitn.do?menuId=${menuId}&ctId=${ctId}', {debrId:debrId, signal:signal}, function(data) {
		if (data.message != null) {
		$m.dialog.alert(data.message);
		}
		if (data.result == 'ok') {
		$m.nav.next(event, '/ct/debr/listDebr.do?menuId=${menuId}&ctId=${ctId}');
		}
		});
		}
		});

	}
	else{

		$m.dialog.confirm('<u:msg titleId="ct.cfrm.sitn"   />', function(result){
		if(result){
		$m.ajax('/ct/debr/setDebrFinSitn.do?menuId=${menuId}&ctId=${ctId}', {debrId:debrId, signal:signal}, function(data) {
		if (data.message != null) {
		$m.dialog.alert(data.message);
		}
		if (data.result == 'ok') {
		$m.nav.next(event, '/ct/debr/listDebr.do?menuId=${menuId}&ctId=${ctId}');
		}
		});
		}
		});

	}

}
var contOn = false;
$(document).ready(function() {
//$layout.adjustBodyHtml('bodyHtmlArea');

<%// 목록의 footer 위치를 일정하게 %>
$space.placeFooter('list');
});
//]]>
</script>

<!--section S-->
<section>

<!--btnarea S-->
<div class="btnarea" id="btnArea">
	<div class="size">
		<dl>

		<c:choose>
		<c:when test="${!empty myAuth && myAuth == 'M' }">
		<dd class="btn" onclick="setOpin('${ctDebrBVo.debrId}', '');"><u:msg titleId="ct.btn.opinReg" alt="의견등록"  /></dd>
		</c:when>
		<c:otherwise>
		<c:if test="${!empty authChkW && authChkW == 'W' }">
		<dd class="btn" onclick="setOpin('${ctDebrBVo.debrId}', '');"><u:msg titleId="ct.btn.opinReg" alt="의견등록"  /></dd>
		</c:if>
		</c:otherwise>
		</c:choose>

		<dd class="btn" onclick="goList();"><u:msg titleId="cm.btn.list" alt="목록"  /></dd>

		<c:if test="${!empty myAuth && myAuth == 'M' || ctDebrBVo.regrUid == logUserUid}">
		<dd class="btn"  onclick="finSitnDebr('${ctDebrBVo.debrId}', 'sitn');" ><u:msg titleId="ct.btn.sitnExtd" alt="회기연장"  /></dd>
		<dd class="btn"  onclick="finSitnDebr('${ctDebrBVo.debrId}', 'fin');" ><u:msg titleId="ct.btn.fin" alt="마감"  /></dd>
		<dd class="btn"  onclick="modDebr('${ctDebrBVo.debrId}');" ><u:msg titleId="cm.btn.mod" alt="수정" /></dd>
		<dd class="btn"  onclick="delDebr('${ctDebrBVo.debrId}');" ><u:msg titleId="cm.btn.del" alt="삭제"  /></dd>
		</c:if>

		<dd class="open" id="moreBtn" style="display:none" onclick="$layout.toggleBtns()"></dd>
		</dl>
	</div>
</div>
<!--//btnarea E-->

<!--titarea S-->
<div class="titarea">
	<dl>
	<dd class="tit">${ctDebrBVo.subj}</dd>
	<dd class="name">

	</dd>
	</dl>
</div>
<!--//titarea E-->

<!--tabarea S-->
<div class="tabarea" id="tabBtnArea">
	<div class="tabsize">
		<dl>
		<dd class="tab_on" onclick="$layout.tab.on($(this).attr('id'))" id="detail"><u:msg titleId="ct.btn.opinList" alt="의견목록" /></dd>
		<dd class="tab" onclick="$layout.tab.on($(this).attr('id'));if(!contOn){$layout.adjustBodyHtml('bodyHtmlArea');contOn = true;}" id="cont"><u:msg titleId="ct.cols.itnt" alt="취지" /></dd>
		</dl>
	</div>
	<div class="tab_icol" style="display:none" id="toLeft"></div>
	<div class="tab_icor" style="display:none" id="toRight"></div>
</div>
<!--//tabarea E-->
<div id="tabViewArea">
	<div class="bodyzone_scroll" id="detail">
		<div class="blank30"></div>
	
		<c:if test="${fn:length(opinList) > 0}">
		<div class="listreplyarea">
			<c:forEach var="opinVo" items="${opinList}" varStatus="status">
			<div class="listreply">
				<dl>
				<dd class="replytit" onclick="viewOpin('${ctDebrBVo.debrId}','${opinVo.opinOrdr}');"><u:out value="${opinVo.subj}"/></dd>
				<!-- <dd class="replybody"></dd> -->
				<dd class="replyname">
				<c:choose>
				<c:when test="${opinVo.prosConsCd == 'A'}">
				<u:msg titleId="ct.option.for" alt="친상" />
				</c:when>
				<c:when test="${opinVo.prosConsCd == 'O'}">
				<u:msg titleId="ct.option.against" alt="반대" />
				</c:when>
				<c:when test="${opinVo.prosConsCd == 'E'}">
				<u:msg titleId="ct.option.etc" alt="기타" />
				</c:when>
				</c:choose> /
				<a href="javascript:$m.user.viewUserPop('${opinVo.regrUid}');">${opinVo.regrNm}</a> /
				<fmt:parseDate var="dateTempParse" value="${opinVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
				</dd>
				</dl>
			</div>
			<div class="line"></div>
	
			</c:forEach>
		</div>
		</c:if>
		<c:if test="${fn:length(opinList) == 0 }">
		<div class="entryzone" >
			<div class="entryarea">
				<dl>
				<dd class="etr_input"><div class="etr_body_gray"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></div></dd>
				</dl>
			</div>
		</div>
		</c:if>
	
	</div>
	
	<div class="bodyzone_scroll" id="cont" style="display:none;"><!-- 취지 -->
	<div class="bodyarea">
		<dl>
		<dd class="bodytxt_scroll">
			<div class="scroll editor" id="bodyHtmlArea"><div id="zoom"><u:out value="${ctDebrBVo.estbItnt}" type="noscript" /></div></div>
		</dd>
		</dl>
	</div>
	</div>
</div>

<jsp:include page="/WEB-INF/jsp/ct/inc/footerInc.jsp" flush="false" />

</section>
