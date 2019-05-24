<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set test="${param.schCat != null}" var="schCat" value="${param.schCat}" elseValue="survSubj" />
<script type="text/javascript">
<!--
function viewSurv(survId){
	$m.nav.next(event, './viewSurvApvd.do?menuId=${menuId}&survId=' + survId);
};

function searchList(event){
	$m.nav.curr(event, './listSurvApvd.do?'+$('#searchForm').serialize());
};

function fnSetSchCd(cd)
{
	$('#schCat').val(cd);
	$('.schTxt1 span').text($(".schOpnLayer1 dd[data-schCd="+cd+"]").text());
	$('.schOpnLayer1').hide();
};

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
//-->
</script>

<!--section S-->
<section>

	<!--listsearch S-->
	<form id="searchForm" name="searchForm" action="./listSurvApvd.do" onsubmit="searchList(event);">
	<input type="hidden" name="menuId" value="${param.menuId}" />
	<input type="hidden" name="schCat" id="schCat" value="" />
	<div class="listsearch">
		<div class="listselect schTxt1">
		    <div class="open1 schOpnLayer1" style="display:none;">
		        <div class="open_in1">
		        	<div class="open_div">
				        <dl>
				        <dd class="txt" onclick="javascript:fnSetSchCd('survSubj');" data-schCd="survSubj"><u:msg titleId="cols.subj" alt="제목" /></dd>
				        <dd class="line"></dd>
				        <dd class="txt" onclick="javascript:fnSetSchCd('survItnt');" data-schCd="survItnt"><u:msg titleId="cols.itnt" alt="취지" /></dd>
				        <dd class="line"></dd>
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
	<!--//listsearch E-->

	<div class="listarea">

		<article>
		<c:forEach var="wvSurvBVo" items ="${wvSurvBMapList}" varStatus="status">
          <div class="listdiv" onclick="javascript:viewSurv('${wvSurvBVo.survId}');">
              <div class="list">
              <dl>
              <dd class="tit"><u:out value="${wvSurvBVo.survSubj}" /></dd>
              <dd class="name">
				  	${wvSurvBVo.regrNm } |
				<fmt:parseDate var="dateTempParse" value="${wvSurvBVo.survStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="survStartDt" value="${dateTempParse}" pattern="yyyy-MM-dd"/>
				<u:out value="${survStartDt}"/> ~
				<fmt:parseDate var="endDtTempParse" value="${wvSurvBVo.survEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="survEndDt" value="${endDtTempParse}" pattern="yyyy-MM-dd"/>
				<u:out value="${survEndDt}"/>
              </dd>
           </dl>
              </div>
          </div>

		</c:forEach>
		<c:if test="${fn:length(wvSurvBMapList) == 0}">
             <div class="listdiv_nodata" >
             <dl>
             <dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd>
             </dl>
             </div>
		</c:if>
	
		</article>
	<m:pagination />
    
	</div>
	<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
</section>
<!--//section E-->