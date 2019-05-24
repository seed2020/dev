<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set test="${param.schCat != null}" var="schCat" value="${param.schCat}" elseValue="SUBJ" />
<script type="text/javascript">
<!--
<% // [목록:제목] 게시물 조회 %>
function viewNotc(id) {
	$m.nav.next(event, '/ct/notc/viewNotc.do?menuId=${menuId}&bullId=' + id);
}

function fnSetSchCd(cd)
{
	$('#schCat').val(cd);
	$('.listselect span').text($(".open1 dd[data-schCd="+cd+"]").text());
	$('.open1').hide();
}

function searchList(event){
	$m.nav.curr(event, '/ct/notc/listNotc.do?'+$('#searchForm').serialize());
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
<form id="searchForm" name="searchForm" action="./listNotc.do" onsubmit="searchList(event);">
<input type="hidden" name="menuId" value="${param.menuId}" />
<input type="hidden" name="schCat" id="schCat" value="" />
<div class="listsearch">
		<div class="listselect">
		    <div class="open1" style="display:none;">
		        <div class="open_in1">
		        	<div class="open_div">
				        <dl>
				        <dd class="txt" onclick="javascript:fnSetSchCd('SUBJ');" data-schCd="SUBJ"><u:msg titleId="cols.subj" alt="제목" /></dd>
				        <dd class="line"></dd>
				        <dd class="txt" onclick="javascript:fnSetSchCd('CONT');" data-schCd="CONT"><u:msg titleId="cols.cont" alt="내용" /></dd>
				        <dd class="line"></dd>
						<dd class="txt" onclick="javascript:fnSetSchCd('REGR_NM');" data-schCd="REGR_NM"><u:msg titleId="cols.regr" alt="등록자" /></dd>
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
<!--//listsearch E-->


<!--section S-->
<section>
<div class="listarea">
	<article>
	
	<c:forEach var="ctAdmNotcVo" items="${ctAdmNotcMapList}" varStatus="status">
	    <div class="listdiv" onclick="javascript:viewNotc('${ctAdmNotcVo.bullId }');">
	        <div class="list"> 
	        <dl>
	        <dd class="tit" >
				<c:if test="${ctAdmNotcVo.newYn == 'Y'}"><div class="new"></div></c:if>${ctAdmNotcVo.subj}
	        </dd>
	        <dd class="name">${ctAdmNotcVo.regrNm} ㅣ 
	        ${ctAdmNotcVo.regDt} | 
	        <u:out value="${ctAdmNotcVo.readCnt}" type="number"/></dd>
	     </dl>
	        </div>
	    </div>
	</c:forEach>
	<c:if test="${fn:length(ctAdmNotcMapList) == 0}">
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
