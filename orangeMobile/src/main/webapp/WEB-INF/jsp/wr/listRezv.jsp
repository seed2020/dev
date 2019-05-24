<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:secu auth="W" ><u:set test="${true}" var="writeAuth" value="Y"/></u:secu>
<u:set test="${param.rescKndId != null}" var="rescKndId" value="${param.rescKndId}" elseValue="" />
<u:set test="${param.rescMngId != null}" var="rescMngId" value="${param.rescMngId}" elseValue="" />
<u:set test="${param.discStatCd != null}" var="discStatCd" value="${param.discStatCd}" elseValue="" />
<u:set test="${(listPage eq 'listRezv' && ( !empty param.durStrtDt || !empty param.durEndDt || !empty param.paramCompId)) || ( listPage eq 'listRezvDisc' && !empty param.discStatCd )}" var="unfoldareaUsed" value="Y" elseValue="N" />
<script type="text/javascript">
//<![CDATA[
// Select Option 클릭 %>
function setSelOptions(codeNm, code, value, handler){
	var $form = $("#searchForm");
	$form.find("input[name='"+codeNm+"']").val(code);
	$form.find("#"+codeNm+"Container #selectView span").text(value);
	$form.find("#"+codeNm+"Open").hide();
	if(handler!=undefined){
		handler();
	}
}
function chnComp(){
	var $form = $('#searchForm');	
	$form.find("#rescKndId").remove();
	searchList(event);
}
function searchList(event){
	$m.nav.curr(event, './${listPage }.do?'+$('#searchForm').serialize());
}

//상세보기
function viewRezv(rezvId) {
	$m.nav.next(event, '/wr/viewRezv.do?${params}&listPage=${listPage}&rezvId='+rezvId);
};

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

<% // [상단버튼:등록] 등록 %>
function setRezv(rezvId , obj){
	var url = '/wr/setRezv.do?${params}&listPage=${listPage}';
	
	if(rezvId != null){
		url+= '&rezvId='+rezvId;
	}
	
	if(obj != null){
		var strtYmd = $(obj).attr('data-day');
		var strtTime = $(obj).attr('data-time');
		url+= "&rezvStrtDt="+strtYmd+" "+strtTime;
	}
	
	$m.nav.next(event, url);
};

//전체 및 본인 예약 현황 조회
function fnMySearch(fncMy){
	$('#fncMy').val(fncMy);
	$m.nav.curr(event, './${listPage }.do?'+$('#searchForm').serialize());
};
//]]>
</script>

<div class="btnarea">
    <div class="size">
        <dl>
           	 <c:if test="${listPage ne 'listRezvDisc' && writeAuth == 'Y'}"><dd class="btn" onclick="setRezv();"><u:msg titleId="wr.btn.rescRezv" alt="자원예약" /></dd></c:if>
           	 <c:if test="${listPage eq 'listRezvStat' || listPage eq 'listRezv' }">
	           	 <c:choose>
					 <c:when test="${param.fncMy == 'Y'}"><dd class="btn" onclick="fnMySearch('N');"><u:msg titleId="wr.btn.allRezvStat" alt="전체예약현황" /></dd></c:when>
					 <c:otherwise><dd class="btn" onclick="fnMySearch('Y');"><u:msg titleId="wr.btn.myRezvStat" alt="본인예약현황" /></dd></c:otherwise>
				 </c:choose>
			 </c:if>
     </dl>
    </div>
</div>
<form id="searchForm" name="searchForm" action="./${listPage }.do" onsubmit="searchList(event);">
<input type="hidden" name="menuId" value="${param.menuId}" />
<input type="hidden" name="rescKndId" id="rescKndId" value="" />
<input type="hidden" name="rescMngId" id="rescMngId" value="" />
<input type="hidden" name="discStatCd" id="discStatCd" value="" />
<input type="hidden" name="durCat" value="fromYmd"/>
<c:if test="${listPage eq 'listRezvStat' || listPage eq 'listRezv'}"><input type="hidden" id="fncMy" name="fncMy" value="${param.fncMy }"/></c:if>
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
	<c:choose>
		<c:when test="${listPage eq 'listRezv' }">
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
	    
	    <c:if test="${!empty ptCompBVoList }">
	    <c:set var="compNm" value=""/>
	    <c:set var="paramCompId" value=""/>
	    <dd class="dd_blank5"></dd>
		<dd class="etr_input" id="paramCompIdContainer">
              <div class="etr_ipmany">
              <dl>
              <dd class="etr_se_lt">
                  <div class="etr_open2" id="paramCompIdOpen" style="display:none;">
                    <div class="open_in1">
                        <div class="open_div">
					        <dl>
					        <c:forEach items="${ptCompBVoList}" var="ptCompBVo" varStatus="status">
		                    <c:if test="${(empty param.paramCompId && status.first) || ptCompBVo.compId == param.paramCompId}"><c:set var="compNm" value="${ptCompBVo.rescNm }"/></c:if>
							<c:if test="${!status.first }"><dd class="line"></dd></c:if>
		                    <dd class="txt" onclick="setSelOptions('paramCompId',$(this).attr('data-code'),$(this).text(), chnComp);" data-code="${ptCompBVo.compId}">${ptCompBVo.rescNm }</dd>
		                    </c:forEach>
					    	</dl>
                        </div>
                    </div>
                </div>
                <input type="hidden" name="paramCompId" value="${param.paramCompId }" class="notChkCls"/>
                <div class="select_in1" onclick="$('#paramCompIdOpen').toggle();">
                <dl>
                <dd class="select_txt" id="selectView"><span>${compNm }</span></dd>
                <dd class="select_btn"></dd>
                </dl>
                </div>
            </dd>
            </dl>
            </div>
        </dd><dd class="etr_blank1"></dd>
	    </c:if>
	       
		</c:when>
		<c:when test="${listPage eq 'listRezvDisc' }">
		<dd class="etr_input">
              <div class="etr_ipmany">
              <dl>
              <dd class="etr_se_lt">
                  <div class="etr_open2 schOpnLayer2" style="display:none;">
                    <div class="open_in1">
                        <div class="open_div">
					        <dl>
						        <dd class="txt" onclick="javascript:fnSetDiscStatCd('');" data-schCd=""><u:msg titleId="cm.option.all" alt="전체선택" /></dd>
						        <dd class="line"></dd>
						        <dd class="txt" onclick="javascript:fnSetDiscStatCd('R');" data-schCd="R"><u:msg titleId="wr.jsp.discStatCdR.title" alt="진행중" /></dd>
						        <dd class="line"></dd>
						        <dd class="txt" onclick="javascript:fnSetDiscStatCd('A');" data-schCd="A"><u:msg titleId="wr.jsp.discStatCdA.title" alt="승인" /></dd>
						        <dd class="line"></dd>
						        <dd class="txt" onclick="javascript:fnSetDiscStatCd('J');" data-schCd="J"><u:msg titleId="wr.jsp.discStatCdJ.title" alt="반려" /></dd>
						        <dd class="line"></dd>
					    	</dl>
                        </div>
                    </div>
                </div>   
                <div class="select_in1 schTxt2" onclick="holdHide3 = true;$('.schOpnLayer2').toggle();">
                <dl>
                <dd class="select_txt"><span></span></dd>
                <dd class="select_btn"></dd>
                </dl>
                </div>
            </dd>
            </dl>
            </div>
        </dd>
    	</c:when>
   	</c:choose>
    	
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
			
          <div class="listdiv" onclick="javascript:viewRezv('${list.rezvId }');">
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
    
</div>
<m:pagination />
	
    <jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
</section>
<!--//section E-->
