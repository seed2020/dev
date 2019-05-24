<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set test="${param.schCat != null}" var="schCat" value="${param.schCat}" elseValue="communityOpt" />
<script type="text/javascript">
//<![CDATA[

function viewCm(ctId, fncUid) {
	$m.nav.next(event, "/ct/viewCm.do?menuId="+ fncUid +"&ctId=" + ctId + "&prevMenuId=${menuId}");
}

function join(ctId) {
	$m.dialog.open({
		id:'setCmJoin',
		title:'<u:msg titleId="ct.jsp.setCmJoin.title" alt="커뮤니티" />',
		url:'/ct/setCmJoinPop.do?menuId=${menuId}&ctId='+ctId+'&catId=${catId}&signal=normal',
	});
}

function fnSetSchCd(cd)
{
	$('#schCat').val(cd);
	$('.listselect span').text($(".open1 dd[data-schCd="+cd+"]").text());
	$('.open1').hide();
}

function searchList(event){
	$m.nav.curr(event, '/ct/listAllCm.do?'+$('#searchForm').serialize());
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
//]]>
</script>

<form id="searchForm" name="searchForm" action="./listAllCm.do" onsubmit="searchList(event);">
<input type="hidden" id="menuId" name="menuId" value="${menuId}" />
<input type="hidden" id="ctId" name="ctId" value="${ctId}"/>
<input type="hidden" id="schCat" name="schCat" value="" />
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
</form>

<section>
<div class="listarea">
	<article>

	<c:forEach var="catCmntVo" items="${catCmntList}" varStatus="status">
		<u:set var="viewFunc" test="${catCmntVo.logUserJoinStat == null }" value="join" elseValue="viewCm"/>
		<div class="listdiv" onclick="${viewFunc }('${catCmntVo.ctId}','${catCmntVo.ctFncUid}');">
			<div class="list_photoarea">
				<div class="list_photoareain">
					<div class="list_photo">
						<dl>
						<dd class="tit">
						<c:choose>
							<c:when test="${catCmntVo.ctEvalCd == 'B'}"><div class="best"></div></c:when>
							<c:when test="${catCmntVo.ctEvalCd == 'E'}"><div class="excellent"></div></c:when>
							<c:when test="${catCmntVo.ctEvalCd == 'G'}"><div class="good"></div></c:when>
						</c:choose>
						<c:if test="${catCmntVo.recmdYn == 'Y'}"><div class="recommend"></div></c:if>
							<u:out value="${catCmntVo.ctNm}" />
						</dd>
						<dd class="name">
						${catCmntVo.mastNm} ㅣ <u:out value="${catCmntVo.regDt}" type="longdate" />
						</dd>
						<dd class="name">
							<c:if test="${catCmntVo.logUserJoinStat != null}">
							<c:choose>
								<c:when test="${catCmntVo.logUserJoinStat == '1'}">
								<u:msg titleId="ct.option.logUserJoinStat01" alt="승인대기중"/>
								</c:when>
								<c:when test="${catCmntVo.logUserJoinStat == '2'}">
								<u:msg titleId="ct.option.logUserJoinStat02" alt="가입불가"/>
								</c:when>
								<c:when test="${catCmntVo.logUserJoinStat == '3'}">
								<u:msg titleId="ct.option.logUserJoinStat03" alt="가입완료"/>
								</c:when>
							</c:choose>
							</c:if>
							<c:if test="${catCmntVo.logUserJoinStat == null}"><u:msg titleId="ct.jsp.setCmJoin.title" alt="커뮤니티가입"/></c:if>
							 ㅣ <u:out value="${catCmntVo.mbshCnt}" type="number" />
						</dd>
						</dl>
					</div>
				</div>
			</div>
			<u:convert srcId="imgFilePath_${catCmntVo.ctId }" var="previewImgPath"/>
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
	<c:if test="${fn:length(catCmntList) == 0}"><div class="listdiv_nodata" ><dl><dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd></dl></div></c:if>
	</article>
    
</div>
<m:pagination />
    
<jsp:include page="/WEB-INF/jsp/ct/inc/footerInc.jsp" flush="false" />
</section>