<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set test="${param.schCat != null}" var="schCat" value="${param.schCat}" elseValue="communityOpt" />
<script type="text/javascript">
<!--
<%// 폐쇄된 커뮤니티 Alert %>
function closedAlert(closeOpin , ctStat){
	if(ctStat == 'ctStat') $m.msg.alertMsg("ct.close.request",closeOpin);
	else $m.msg.alertMsg("ct.close.complete",closeOpin);
};

<%// 커뮤니티 홈 화면 %>
function viewCm(ctId, fncUid) {
	$m.nav.next(event, "/ct/viewCm.do?menuId="+ fncUid +"&ctId=" + ctId + "&prevMenuId=${menuId}");
};

function searchList(event){
	$m.nav.curr(event, '/ct/listMyCm.do?'+$('#searchForm').serialize());
}

function fnSetSchCd(cd)
{
	$('#schCat').val(cd);
	$('.listselect span').text($(".open1 dd[data-schCd="+cd+"]").text());
	$('.open1').hide();
}

var holdHide = false;
$(document).ready(function() {
fnSetSchCd('${schCat}');
$('body:first').on('click', function(){
if(holdHide) holdHide = false;
else $(".open1").hide();
});
<%// 목록의 footer 위치를 일정하게 %>
$space.placeFooter('list');
});
//-->
</script>


<!--listsearch S-->
<form id="searchForm" name="searchForm" action="./listMyCm.do" onsubmit="searchList(event);">
<input type="hidden" name="menuId" value="${param.menuId}" />
<input type="hidden" name="schCat" id="schCat" value="" />
<div class="listsearch">
	<div class="listselect">
		<div class="open1" style="display:none;">
			<div class="open_in1">
				<div class="open_div">
					<dl>
					<dd class="txt" onclick="javascript:fnSetSchCd('communityOpt');" data-schCd="communityOpt"><u:msg titleId="ct.cols.ctNm" alt="커뮤니티" /></dd>
					<dd class="line"></dd>
					<dd class="txt" onclick="javascript:fnSetSchCd('masterOpt');" data-schCd="masterOpt"><u:msg titleId="ct.cols.mastNm" alt="마스터" /></dd>
					<dd class="line"></dd>
					</dl>
				</div>
			</div>
		</div>
		<div class="select1">
			<div class="select_in1" onclick="holdHide = true;$('.open1').toggle();">
				<dl>
				<dd class="select_txt1"><span></span></dd>
				<dd class="select_btn"></dd>
				</dl>
			</div>
		</div>
	</div>
	<div class="listinput">
		<div class="input1">
			<dl>
			<dd class="input_left"></dd>
			<dd class="input_input"><input type="text" class="input_ip" name="schWord" maxlength="30" value="<u:out value="${param.schWord}" type="value" />" /></dd>
			<dd class="input_btn" onclick="searchList(event);"><div class="search"></div></dd>
			</dl>
		</div>
	</div>
</div>
<%-- <checkbox name="schClose" value="Y" titleId="ct.cols.close" checkValue="${param.schClose}" textClass="search_body" /> --%>
</form>
<!--//listsearch E-->



<!--section S-->
<section>
<div class="listarea">
	<article>


	<c:forEach var="myCtVo" items="${myCtMapList}" varStatus="status">

	<c:set var="function" value="" />
	<c:if test="${myCtVo.ctStat eq 'A'}" ><c:set var="function" value="viewCm('${myCtVo.ctId}','${myCtVo.ctFncUid}')" /></c:if>
	<c:if test="${myCtVo.ctStat eq 'C' || myCtVo.ctActStat eq 'C' }" ><c:set var="function" value="closedAlert('${myCtVo.rjtOpinCont}','${myCtVo.ctActStat eq 'C' ? 'ctActStat' : 'ctStat' }')" /></c:if>

	<div class="listdiv" onclick="javascript:${function};">
		<div class="list_photoarea">
			<div class="list_photoareain">
				<div class="list_photo">
					<dl>
					<dd class="tit">
						<c:choose>
							<c:when test="${myCtVo.ctEvalCd == 'B'}"><div class="best"></div></c:when>
							<c:when test="${myCtVo.ctEvalCd == 'E'}"><div class="excellent"></div></c:when>
							<c:when test="${myCtVo.ctEvalCd == 'G'}"><div class="good"></div>	</c:when>
						</c:choose>
						<c:if test="${myCtVo.recmdYn == 'Y'}"><div class="recommend"></div></c:if>
						${myCtVo.ctNm}
					</dd>
					<dd class="name">
					${myCtVo.mastNm} ㅣ <u:out value="${myCtVo.regDt}" type="longdate" />
					</dd>
					<dd class="name">
						<c:if test="${myCtVo.logUserJoinStat == '3'}">
							<c:choose>
							<c:when test="${myCtVo.logUserSeculCd == 'M'}">
							<u:msg titleId="ct.option.mbshLev0" alt="마스터"/>
							</c:when>
							<c:when test="${myCtVo.logUserSeculCd == 'G'}">
							<u:msg titleId="ct.option.mbshLev4" alt="게스트"/>
							</c:when>
							<c:when test="${myCtVo.logUserSeculCd == 'S'}">
							<u:msg titleId="ct.option.mbshLev1" alt="스텝"/>
							</c:when>
							<c:when test="${myCtVo.logUserSeculCd == 'A'}">
							<u:msg titleId="ct.option.mbshLev3" alt="준회원"/>
							</c:when>
							<c:when test="${myCtVo.logUserSeculCd == 'R'}">
							<u:msg titleId="ct.option.mbshLev2" alt="정회원"/>
							</c:when>
							</c:choose>
							 ㅣ 
							<c:choose>
							<c:when test="${myCtVo.ctActStat eq 'S'}">
							<u:msg titleId="ct.option.joinStat01" alt="승인대기중"/>
							</c:when>
							<c:when test="${myCtVo.ctStat ne 'C' && myCtVo.ctActStat eq 'A'}">
							<u:msg titleId="ct.cols.act" alt="활동중"/>
							</c:when>
							<c:when test="${myCtVo.ctStat eq 'C' && myCtVo.ctActStat eq 'A'}">
							<u:msg titleId="ct.cols.closeWait" alt="폐쇄신청중" />
							</c:when>
							<c:when test="${myCtVo.ctActStat eq 'C'}">
							<u:msg titleId="ct.cols.close" alt="폐쇄"/>
							</c:when>
							</c:choose>
							 ㅣ ${myCtVo.mbshCnt} 
						</c:if>
					</dd>
					</dl>
				</div>
			</div>
		</div>
		<u:convert srcId="imgFilePath_${myCtVo.ctId }" var="previewImgPath"/>
		<div class="list_photort">
			<dl>
			<c:if test="${!empty previewImgPath}">
			<dd class="photo"><img src="${_cxPth}${previewImgPath}" width="73" height="73" onerror='this.src="/images/blue/noimg2.png"'/></dd>
			</c:if>
			<c:if test="${empty previewImgPath}">
			<dd class="noimg"></dd>
			</c:if>
			</dl>
		</div>
	</div>
	</c:forEach>
	<c:if test="${fn:length(myCtMapList) == 0}">
	<div class="listdiv_nodata" >
		<dl>
		<dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd>
		</dl>
	</div>
	</c:if>

	</article>

</div>
<m:pagination />

	<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
</section>
<!--//section E-->

