<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set test="${param.schCat != null}" var="schCat" value="${param.schCat}" elseValue="SUBJ" />
<u:set test="${(param.strtYmd != null && param.strtYmd != '') || (param.endYmd != null && param.endYmd != '')}" var="unfoldareaUsed" value="Y" elseValue="N" />
<script type="text/javascript">
//<![CDATA[
function searchList(event){
	$m.nav.curr(event, './${listPage }.do?'+$('#searchForm').serialize());
}

<% // [목록:제목] 게시물 조회 %>
function setBull(bullId) {
	$m.nav.next(null, '/bb/setDiscBull.do?${params}&bullId=' + bullId);
}

function fnSetSchCd(cd)
{
	$('#schCat').val(cd);
	$('.listselect span').text($(".open1 dd[data-schCd="+cd+"]").text());
	$('.open1').hide();
}

function fnUnfold(){
	if($('#unfold').attr("class") == "unfoldbtn")
		$('#unfold').attr("class", "unfoldbtn_on");
	else
		$('#unfold').attr("class", "unfoldbtn");
	$('.unfoldArea').toggle(); 
}

var holdHide = false;
$(document).ready(function() {
	fnSetSchCd('${schCat}');
	$('body:first').on('click', function(){
		if(holdHide) holdHide = false;
		else $(".open1").hide();
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

<!--listsearch S-->
<form id="searchForm" name="searchForm" action="./${listPage }.do" onsubmit="searchList(event);">
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
						<dd class="txt" onclick="javascript:fnSetSchCd('BRD_NM');"><u:msg titleId="cols.bbNm" alt="게시판명" /></dd>
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
    	<div class="listinput2">
			<div class="input1">
			<dl>
				<dd class="input_left"></dd>
				<dd class="input_input"><input type="text" class="input_ip" name="schWord" maxlength="30" value="<u:out value="${param.schWord}" type="value" />" /></dd>
				<dd class="input_btn" onclick="searchList(event);"><div class="search"></div></dd>
			</dl>
			</div>
		</div>
		<div class="unfoldbtn" onclick="fnUnfold();" id="unfold"></div>
</div>

<div class="entryzone unfoldArea" style="display:none;">
      <div class="entryarea">
      <dl>
		<dd class="etr_blank"></dd>
        <dd class="etr_select">
	  	  <div class="etr_calendar_lt">
		  <div class="etr_calendar">
			   <input id="strtYmd" name="strtYmd" value="${param.strtYmd}" type="hidden" />
			   <div class="etr_calendarin">
				   <dl>
				   <dd class="ctxt" onclick="fnCalendar('strtYmd','{end:\'endYmd\'}');"><span id="strtYmd">${param.strtYmd}</span></dd>
				   <dd class="cdelete" onclick="fnTxtDelete(this,'strtYmd');"></dd>
				   <dd class="cbtn" onclick="fnCalendar('strtYmd','{end:\'endYmd\'}');"></dd>
				   </dl>
			   </div>
			</div>
		</div>
	
        <div class="etr_calendar_rt">
            <div class="etr_calendar">
            	<input id="endYmd" name="endYmd" value="${param.endYmd}" type="hidden" />
            	<div class="etr_calendarin">
                    <dl>
                    <dd class="ctxt" onclick="fnCalendar('endYmd','{start:\'strtYmd\'}');"><span id="endYmd">${param.endYmd}</span></dd>
                    <dd class="cdelete" onclick="fnTxtDelete(this,'endYmd');"></dd>
                    <dd class="cbtn" onclick="fnCalendar('endYmd','{start:\'strtYmd\'}');"></dd>
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
<!--//listsearch E-->

<!--section S-->
<section>
<div class="listarea">
	<article>
	
	<!-- 게시물 목록 -->
	<c:if test="${fn:length(baBullSubmLVoList) == 0}">
		<div class="listdiv_nodata" ><dl><dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd></dl></div>
	</c:if>
	<c:if test="${fn:length(baBullSubmLVoList) > 0}">
	<c:forEach items="${baBullSubmLVoList}" var="baBullSubmLVo" varStatus="status">
		<div class="listdiv" onclick="javascript:setBull('${baBullSubmLVo.bullId}');">
	        <div class="list">
	        	<dl>
              	   <dd class="tit">
						<c:if test="${baBullSubmLVo.ugntYn == 'Y'}"><span class="ctxt1">[<u:msg titleId="bb.option.ugnt" alt="긴급" />]</span></c:if>
						<c:if test="${baBullSubmLVo.secuYn == 'Y'}"><span class="ctxt2">[<u:msg titleId="bb.option.secu" alt="보안" />]</span></c:if>
						${baBullSubmLVo.subj}
                    </dd>
                    <dd class="name"><u:out value="${baBullSubmLVo.deptNm}" />ㅣ<u:out value="${baBullSubmLVo.regrNm}" /> ㅣ<u:out value="${baBullSubmLVo.regDt}" type="longdate" /></dd>
           	  </dl>
	        </div>
	   </div>
	</c:forEach>
	</c:if>
	
	</article>
    
</div>
<m:pagination />
    
<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
</section>
<!--//section E-->
