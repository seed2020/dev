<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:secu auth="W"><u:set test="${true}" var="writeAuth" value="Y"/></u:secu>
<u:secu auth="M" ownerUid="${wrRezvBVo.regrUid}"><u:set test="${true}" var="modAuth" value="Y"/></u:secu>
<u:secu auth="A" ownerUid="${wrRezvBVo.regrUid}"><u:set test="${true}" var="delAuth" value="Y"/></u:secu>
<script type="text/javascript">
//<![CDATA[
<% // [하단버튼:승인,반려] %>
function saveDisc(discStatCd) {
	if(discStatCd == null) return;

	$m.dialog.open({
	id:'setDiscPop',
	title:'<u:msg titleId="wr.jsp.viewRezvDisc.title"/>',
	url:'/wr/setDiscPop.do?menuId=${menuId}&listPage=${listPage}&rezvId=${wrRezvBVo.rezvId}&discStatCd='+discStatCd,
	});
};

function fnDelete(){
	$m.msg.confirmMsg("cm.cfrm.del", null, function(result){
	if(result){
	$m.ajax('/wr/transRezvDel.do?menuId=${menuId}', {rezvId:'${wrRezvBVo.rezvId}'}, function(data) {
	if (data.message != null) {
	$m.dialog.alert(data.message);
	}
	if (data.result == 'ok') {
	$m.nav.next(event, '/wr/${listPage}.do?menuId=${menuId}');
	}
	});
	}
	});
};

function goList(){
	$m.nav.prev(event, '/wr/${listPage}.do?menuId=${menuId}&paramCompId=${param.paramCompId}');
};

function setRezv(){
	$m.nav.next(event, '/wr/setRezv.do?menuId=${menuId}&listPage=${listPage}&rezvId=${wrRezvBVo.rezvId }&paramCompId=${param.paramCompId}');
};

function fnCheckbox(obj){
	if($(obj).attr("class") == "check")
		$(obj).attr("class", "check_on");
	else
		$(obj).attr("class", "check");
};

$(document).ready(function() {
	$layout.adjustBodyHtml('bodyHtmlArea');
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('tabview');
});

//]]>
</script>

<section>

<!--btnarea S-->
<div class="btnarea" id="btnArea">
	<div class="size">
		<dl>
		<c:if test="${discYn eq 'Y' && listPage eq 'listRezvDisc' && writeAuth == 'Y'}">
		<dd class="btn" onclick="saveDisc('A');"><u:msg titleId="cm.btn.apvd" alt="승인" /></dd>
		<dd class="btn" onclick="saveDisc('J');"><u:msg titleId="cm.btn.rjt" alt="반려" /></dd>
		</c:if>
		<c:if test="${empty wrRezvBVo.rezvId || (!empty wrRezvBVo.rezvId && (wrRezvBVo.discStatCd eq 'B' || wrRezvBVo.discStatCd eq 'R')) }">
		<c:if test="${modAuth == 'Y'}">
		<dd class="btn" onclick="setRezv();"><u:msg titleId="cm.btn.mod" alt="수정" /></dd>
		</c:if>
		<c:if test="${delAuth == 'Y'}">
		<dd class="btn" onclick="fnDelete();"><u:msg titleId="cm.btn.del" alt="삭제" /></dd>
		</c:if>
		</c:if>
		<dd class="btn" onclick="goList();"><u:msg titleId="cm.btn.list" alt="목록"  /></dd>
		<dd class="open" id="moreBtn" style="display:none" onclick="$layout.toggleBtns()"></dd>
		</dl>
	</div>
</div>
<!--//btnarea E-->

<!--titlezone S-->
<div class="titlezone">
    <div class="titarea">
    <dl>
    <dd class="tit">${wrRezvBVo.subj}</dd>
    <dd class="body">${wrRezvBVo.rescNm}${!empty wrRezvBVo.regrNm ? 'ㅣ' : ''}${wrRezvBVo.regrNm}</dd>
    <dd class="name"><u:out value="${wrRezvBVo.rezvStrtDt }" type="longdate" /> ~ <u:out value="${wrRezvBVo.rezvEndDt }" type="longdate" /></dd>
 	</dl>
    </div>
</div>
<!--//titlezone E-->

<div class="tabarea" id="tabBtnArea">
<div class="tabsize">
<dl>
<dd class="tab_on" onclick="$layout.tab.on($(this).attr('id'));" id="cont"><u:msg titleId="cols.body" alt="본문" /></dd>
<dd class="tab" onclick="$layout.tab.on($(this).attr('id'));" id="detail"><u:msg titleId="cm.btn.detl" alt="상세" /></dd>
<c:if test="${envConfigMap.guestUseYn eq 'Y' && !empty wcPromGuestDVoList}">
<dd class="tab" onclick="$layout.tab.on($(this).attr('id'));" id="guest"><u:msg titleId="cols.guest" alt="참석자" /></dd>
</c:if>
</dl>
</div>
<div class="tab_icol" style="display:none" id="toLeft"></div>
<div class="tab_icor" style="display:none" id="toRight"></div>
</div>

<!--//tabarea E-->
<div id="tabViewArea">
	<div class="bodyzone_scroll" id="cont" >
		<div class="bodyarea">
			<dl>
				<dd class="bodytxt_scroll"><div class="scroll editor" id="bodyHtmlArea"><div id="zoom"><u:out value="${wrRezvBVo.cont}" type="noscript" /></div></div>
				</dd>
			</dl>
		</div>
	</div>
	<!--listtablearea S-->
	<div class="s_tablearea" id="detail" style="display:none;">
		<div class="blank10"></div>
		<table class="s_table">
			<!-- <caption>타이틀</caption> -->
			<colgroup>
			<col width="33%"/>
			<col width=""/>
			</colgroup>
			<tbody>
			<tr>
				<th class="shead_lt"><u:msg titleId="cols.subj" alt="제목" /></th>
				<td class="shead_lt">
					${wrRezvBVo.subj}
				</td>
			</tr>
			<tr>
				<th class="shead_lt"><u:msg titleId="cols.rescKnd" alt="자원종류" /></th>
				<td class="shead_lt">
					${wrRezvBVo.kndNm }
				</td>
			</tr>
			<tr>
				<th class="shead_lt"><u:msg titleId="cols.rescNm" alt="자원명" /></th>
				<td class="shead_lt">
					${wrRezvBVo.rescNm }
				</td>
			</tr>
			<tr>
				<th class="shead_lt"><u:msg titleId="cols.rezvr" alt="예약자" /></th>
				<td class="sbody_lt">
					<a href="javascript:;" onclick="$m.user.viewUserPop('${wrRezvBVo.regrUid}');">${wrRezvBVo.regrNm }</a>
				</td>
			</tr>
			<tr>
				<th class="shead_lt"><u:msg titleId="cols.rezvDt" alt="예약일시" /></th>
				<td class="shead_lt">
					<u:out value="${wrRezvBVo.rezvStrtDt }" type="longdate" /> ~ <u:out value="${wrRezvBVo.rezvEndDt }" type="longdate" />
				</td>
			</tr>
			<tr>
				<th class="shead_lt"><u:msg titleId="wr.cols.rezvStat" alt="예약상태" /></th>
				<td class="shead_lt">
					<u:msg titleId="wr.jsp.discStatCd${wrRezvBVo.discStatCd }.title" alt="예약상태"/>
				</td>
			</tr>
			<c:if test="${envConfigMap.tgtUseYn eq 'Y' && !empty wrRezvBVo.schdlKndCd}">
			<tr>
				<th class="shead_lt"><u:msg titleId="wc.cols.schdlKndCd" alt="일정대상"/></th>
				<td class="shead_lt"><c:choose><c:when test="${wrRezvBVo.schdlKndCd eq '1' }"><u:msg titleId="wc.jsp.listPsnSchdl.psn.title" alt="개인일정"
			/></c:when><c:when test="${wrRezvBVo.schdlKndCd eq '3' }"><u:msg titleId="wc.jsp.listPsnSchdl.dept.title" alt="부서일정"
			/></c:when><c:when test="${wrRezvBVo.schdlKndCd eq '4' }"><u:msg titleId="wc.jsp.listPsnSchdl.comp.title" alt="회사일정"
			/></c:when></c:choose></td>
			</tr>
			</c:if><c:if test="${wrRezvBVo.discStatCd eq 'J' || wrRezvBVo.discStatCd eq 'A'}">
			<tr>
				<th class="shead_lt"><u:msg titleId="wr.cols.discrNm" alt="심의자" /></th>
				<td class="sbody_lt">
					<a href="javascript:;" onclick="$m.user.viewUserPop('${wrRezvBVo.discrUid}');">${wrRezvBVo.discrNm }</a>
				</td>
			</tr>
			<tr>
				<th class="shead_lt"><u:msg titleId="cols.discOpin" alt="심의의견" /></th>
				<td class="shead_lt">
					${wrRezvBVo.discCont}
				</td>
			</tr>
			</c:if>
			</tbody>
		</table>
	</div>
	<!--//listtablearea E-->
	
	<c:if test="${envConfigMap.guestUseYn eq 'Y' && !empty wcPromGuestDVoList}">
	<div class="listarea" id="guest" style="display:none;"><!-- 참석자 목록 -->
		<div class="blank30"></div>
		<article>
			<c:choose>
				<c:when test="${!empty wcPromGuestDVoList }">
					<c:forEach var="list" items="${wcPromGuestDVoList}" varStatus="status">
						<u:set var="viewFunc" test="${list.guestEmplYn eq 'Y' }" value="$m.user.viewUserPop" elseValue="viewBc"/>
						<div class="listdiv" onclick="${viewFunc }('${list.guestUid }');">
	                        <div class="list">
	                        <dl>
	                        <dd class="tit"><u:out value="${list.guestNm}" /> | <u:msg titleId="${list.guestEmplYn eq 'Y' ? 'cm.option.empl' : 'wc.option.frnd'}" alt="지인"/></dd>
	                        <dd class="body">${list.guestCompNm } ${!empty list.guestCompNm && !empty list.email ? '|' : ''} ${list.email }</dd>
		                    </dl>
	                        </div>
	                    </div>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<div class="listdiv_nodata" >
                    <dl>
                    <dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd>
                    </dl>
                    </div>
				</c:otherwise>
			</c:choose>
		</article>
	</div></c:if>
		
		
</div>

<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />

</section>
