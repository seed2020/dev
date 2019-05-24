<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set test="${param.schCat != null}" var="schCat" value="${param.schCat}" elseValue="TOPC" />
<script type="text/javascript">
//<![CDATA[
function searchList(event){
	$m.nav.curr(event, '/ct/debr/listDebr.do?'+$('#searchForm').serialize());
}

function fnSetSchCd(cd)
{
	$('#schCat').val(cd);
	$('.schTxt1 span').text($(".schOpnLayer1 dd[data-schCd="+cd+"]").text());
	$('.schOpnLayer1').hide();
}

var holdHide = false;
$(document).ready(function() {
	fnSetSchCd('${schCat}');
	$('body:first').on('click', function(){
		if(holdHide) holdHide = false;
		else $(".schOpnLayer1").hide();
	});
	
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('list');
});

function setDebr(debrId) {
	$m.nav.next(event,"/ct/debr/setDebr.do?menuId=${menuId}&ctId=${ctId}");
}

function listOpin(debrId) {
	$m.nav.next(event,"/ct/debr/listOpin.do?menuId=${menuId}&ctId=${ctId}&debrId="+debrId);
}
//-->
</script>
<c:if test="${!empty authChkW && authChkW == 'W' }">
<div class="btnarea">
    <div class="size">
        <dl>
           	 <dd class="btn" onclick="setDebr();"><u:msg titleId="cm.btn.write" alt="등록" /></dd>
     </dl>
    </div>
</div>
</c:if>
<form id="searchForm" name="searchForm" action="./listDebr.do" onsubmit="searchList(event);">
<input type="hidden" name="menuId" value="${param.menuId}" />
<input type="hidden" name="schCat" id="schCat" value="" />
<input type="hidden" name="ctId" id="ctId" value="${param.ctId}" />
<div class="listsearch">
		<div class="listselect schTxt1">
		    <div class="open1 schOpnLayer1" style="display:none;">
		        <div class="open_in1">
		        	<div class="open_div">
				        <dl>
				        <dd class="txt" onclick="javascript:fnSetSchCd('TOPC');" data-schCd="TOPC"><u:msg titleId="cols.topc" alt="주제" /></dd>
				        <dd class="line"></dd>
				        <dd class="txt" onclick="javascript:fnSetSchCd('ITNT');" data-schCd="ITNT"><u:msg titleId="cols.itnt" alt="취지" /></dd>
				        <dd class="line"></dd>
						<dd class="txt" onclick="javascript:fnSetSchCd('REGR_NM');" data-schCd="REGR_NM"><u:msg titleId="cols.regr" alt="등록자" /></dd>
				    	</dl>
				    </div>	
		        </div>
		    </div>
			<div class="select1">
			<div class="select_in1" onclick="holdHide = true;$('.schOpnLayer1').toggle();">
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

		<c:forEach var="ctDebrVo" items="${ctDebrList}" varStatus="status">
          <div class="listdiv">
              <div class="list" <c:if test="${!empty authChkR && authChkR == 'R' }">onclick="listOpin('${ctDebrVo.debrId}');"</c:if>>
              <dl>
              <dd class="tit" >
				${ctDebrVo.subj}
              </dd>
              <dd class="name">
				  	${ctDebrVo.regrNm} / 
					<fmt:parseDate var="dateTempParse" value="${ctDebrVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
					<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/> / 
					${ctDebrVo.opinCnt} / 
					<c:if test="${ctDebrVo.finYn == 'Y'}">
						<u:msg titleId="ct.cols.finish" alt="마감"/>
					</c:if>
					<c:if test="${ctDebrVo.finYn == 'N'}">
						${ctDebrVo.sitn}<u:msg titleId="ct.cols.week" alt="주"/>
					</c:if>
              
           	   </dl>
              </div>
          </div>
		</c:forEach>
		<c:if test="${fn:length(ctDebrList) == 0 }">
             <div class="listdiv_nodata" >
             <dl>
             <dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd>
             </dl>
             </div>
		</c:if>

	 </article>
	   
</div>
	<m:pagination />
    
<jsp:include page="/WEB-INF/jsp/ct/inc/footerInc.jsp" flush="false" />
</section>


