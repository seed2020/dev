<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set test="${param.rescKndId != null}" var="rescKndId" value="${param.rescKndId}" elseValue="" />
<u:set test="${param.rescMngId != null}" var="rescMngId" value="${param.rescMngId}" elseValue="" />
<u:set test="${!empty param.durStrtDt || !empty param.durEndDt || !empty param.discStatCd }" var="unfoldareaUsed" value="Y" elseValue="N" />
<script type="text/javascript">
//<![CDATA[
function searchList(event){
	$m.nav.curr(event, './listRezvStatSub.do?'+$('#searchForm').serialize());
}

function fnSetRescKndId(cd)
{
	$('#rescKndId').val(cd);
	$('#rescMngId').val("");
	$('.listselect .select3 span').text($(".open3 dd[data-rescKndId='"+cd+"']").text());
	$('.open3').hide();

	$(".open4 dl dd").each(function(){
		$(this).remove();
	});

	$(".open4 dl").append("<dd class='txt' onclick='fnSetRescMngId(\"\");' data-rescMngId=''><u:msg titleId="cm.option.all" alt="전체선택"/></dd><dd class='line'></dd>");
	$('.listselect .select4 span').text($(".open4 dd:first").text());
	if(cd != ''){
		$m.ajax('/wr/selectRescAjx.do?menuId=${menuId}', {rescKndId:$('#rescKndId').val()}, function(data){
	        	$.each(data.list , function(index, vo) {
	        		var obj = JSON.parse(JSON.stringify(vo));
		        	$(".open4 dl").append("<dd class='txt' onclick='fnSetRescMngId(\""+obj.rescMngId+"\");' data-rescMngId='"+obj.rescMngId+"'>"+obj.rescNm+"</dd><dd class='line'></dd>");
		     	});
		});
	} 
}

function fnSetRescMngId(cd)
{
	$('#rescMngId').val(cd);
	$('.listselect .select4 span').text($(".open4 dd[data-rescMngId='"+cd+"']").text());
	$('.open4').hide();
}

function fnSetDiscStatCd(cd)
{
	$('#discStatCd').val(cd);
	$('.schTxt2 span').text($(".schOpnLayer2 dd[data-schCd='"+cd+"']").text());
	$('.schOpnLayer2').hide();
}

function fnUnfold(){
	if($('#unfold').attr("class") == "unfoldbtn")
		$('#unfold').attr("class", "unfoldbtn_on");
	else
		$('#unfold').attr("class", "unfoldbtn");
	$('.unfoldArea').toggle(); 
}

var holdHide = false, holdHide2 = false, holdHide3 = false;
$(document).ready(function() {
	fnSetRescKndId('${rescKndId}');
	fnSetRescMngId('${rescMngId}');
	fnSetDiscStatCd('${discStatCd}');
	$('body:first').on('click', function(){
		if(holdHide) holdHide = false;
		else $(".open3").hide();
		if(holdHide2) holdHide2 = false;
		else $(".open4").hide();
		if(holdHide3) holdHide3 = false;
		else $(".schOpnLayer2").hide();
	});
	
	if('${unfoldareaUsed}' == 'Y')
		fnUnfold();
	
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('list');
});

function fnCalendar(id,opt){
	$m.dialog.open({
		id:id,
		noPopbody:true,
		cld:true,
		url:'/cm/util/getCalendarPop.do?id='+id+'&opt='+opt+'&val='+$('#'+id).val()+'&calStyle=m',
	});
}


//]]>
</script>
<form id="searchForm" name="searchForm" action="./listRezvStatSub.do" onsubmit="searchList(event);">
<input type="hidden" name="menuId" value="${param.menuId}" />
<input type="hidden" name="rescKndId" id="rescKndId" value="" />
<input type="hidden" name="rescMngId" id="rescMngId" value="" />
<input type="hidden" name="durCat" value="fromYmd"/>

<c:if test="${!empty param.paramCompId }"><m:input type="hidden" id="paramCompId" value="${param.paramCompId}" /></c:if>

<!--listsearch S-->
<div class="listsearch">
    <div class="listselect">
	    <div class="open3" style="display:none;">
	        <div class="open_in1">
	            <div class="open_div">
	            <dl>
	            <dd class="txt" onclick="fnSetRescKndId('');" data-rescKndId=""><u:msg titleId="cm.option.all" alt="전체선택"/></dd>
	            <dd class="line"></dd>
					<c:forEach items="${wrRescKndBVoList}" var="list" varStatus="status">
			            <dd class="txt" onclick="fnSetRescKndId('${list.rescKndId}');" data-rescKndId="${list.rescKndId}">${list.kndNm}</dd>
			            <dd class="line"></dd>
					</c:forEach>
	         	</dl>
	            </div>
	        </div>
	    </div>
	    <div class="open4" style="display:none;">
	        <div class="open_in2">
	            <div class="open_div">
	            <dl></dl>
	            </div>
	        </div>
	    </div>
        <div class="select3">
            <div class="select_in1" onclick="holdHide = true;$('.open3').toggle();">
            <dl>
            <dd class="select_txt1"><span></span></dd>
            <dd class="select_btn"></dd>
            </dl>
            </div>
        </div>
        <div class="select4">
            <div class="select_in2" onclick="holdHide2 = true;$('.open4').toggle();">
            <dl>
            <dd class="select_txt2"><span></span></dd>
            <dd class="select_btn"></dd>
            </dl>
            </div>
        </div>
        <div class="searchbtn3" onclick="searchList(event);"><span></span></div>
    </div>
    <div class="unfoldbtn" onclick="fnUnfold();" id="unfold"></div>
</div>
<!--//listsearch E-->

<div class="entryzone unfoldArea" style="display:none;">
      <div class="entryarea">
      <dl>
		<dd class="etr_blank"></dd>
		<dd class="etr_select">
	          <div class="etr_calendar_lt">
	              <div class="etr_calendar">
	                  <input id="durStrtDt" name="durStrtDt" value="${param.durStrtDt}" type="hidden" />
	              	  <div class="etr_calendarin">
                      <dl>
                      <dd class="ctxt" onclick="fnCalendar('durStrtDt','{end:\'durEndDt\'}');"><span id="durStrtDt">${param.durStrtDt}</span></dd>
                      <dd class="cdelete" onclick="fnTxtDelete(this,'durStrtDt');"></dd>
                      <dd class="cbtn" onclick="fnCalendar('durStrtDt','{end:\'durEndDt\'}');"></dd>
                      </dl>
                  </div>
	            </div>
	        </div>
	        <div class="etr_calendar_rt">
	            <div class="etr_calendar">
	            	<input id="durEndDt" name="durEndDt" value="${param.durEndDt}" type="hidden" />
	            	<div class="etr_calendarin">
	                    <dl>
	                    <dd class="ctxt" onclick="fnCalendar('durEndDt','{start:\'durStrtDt\'}');"><span id="durEndDt">${param.durEndDt}</span></dd>
	                    <dd class="cdelete" onclick="fnTxtDelete(this,'durEndDt');"></dd>
	                    <dd class="cbtn" onclick="fnCalendar('durEndDt','{start:\'durStrtDt\'}');"></dd>
	                    </dl>
                    </div>
	            </div>
	        </div>
	    </dd>   
    	<dd class="etr_blank1"></dd>
 		</dl>
    </div> 
</div>
</form>


<!--section S-->
<section>
<div class="listarea">
<article>
	<c:choose>
		<c:when test="${!empty wrRezvBVoList}">
			<c:forEach var="list" items="${wrRezvBVoList}" varStatus="status">
			
          <div class="listdiv" >
              <div class="list">
              <dl>
              <dd class="tit">${list.subj }</dd>
              <dd class="body">${list.rescNm } ㅣ ${list.regrNm }</dd>
              <dd class="name">
              		<u:msg titleId="wr.jsp.discStatCd${list.discStatCd }.title" alt="예약상태"/> ㅣ 
				  	<u:out value="${list.rezvStrtDt }" type="longdate" /> ~ <u:out value="${list.rezvEndDt }" type="longdate" />
              </dd>
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
	
	<m:pagination />
    
</div>
<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
</section>
<!--//section E-->
