<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<c:if test="${!empty authChkW && authChkW == 'W' }"><u:set test="${true}" var="writeAuth" value="Y"/></c:if>
<u:set test="${param.schCat != null}" var="schCat" value="${param.schCat}" elseValue="SUBJ" />
<u:set test="${(param.strtYmd != null && param.strtYmd != '') || (param.endYmd != null && param.endYmd != '')}" var="unfoldareaUsed" value="Y" elseValue="N" />
<script type="text/javascript">
//<![CDATA[
function searchList(event){
	$m.nav.curr(event, '/ct/board/listBoard.do?'+$('#searchForm').serialize());
}

<% // [목록:제목] 게시물 조회 %>
function viewBull(id) {
	$m.nav.next(event, '/ct/board/viewBoard.do?menuId=${menuId}&ctId=${param.ctId}&bullId=' + id);
}

function fnSetSchCd(cd)
{
	$('#schCat').val(cd);
	$('.schTxt1 span').text($(".schOpnLayer1 dd[data-schCd="+cd+"]").text());
	$('.schOpnLayer1').hide();
}


var holdHide = false, holdHide2 = false;
$(document).ready(function() {
	fnSetSchCd('${schCat}');
	$('body:first').on('click', function(){
		if(holdHide) holdHide = false;
		else $(".schOpnLayer1").hide();
		if(holdHide2) holdHide2 = false;
		else $(".schOpnLayer2").hide();
	});
	
	if('${unfoldareaUsed}' == 'Y')
		fnUnfold();
	
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('list');
});

function fnUnfold(){
	if($('#unfold').attr("class") == "unfoldbtn")
		$('#unfold').attr("class", "unfoldbtn_on");
	else
		$('#unfold').attr("class", "unfoldbtn");
	$('.unfoldArea').toggle(); 
}

function fnCalendar(id,opt){
	$m.dialog.open({
		id:id,
		noPopbody:true,
		cld:true,
		url:'/cm/util/getCalendarPop.do?id='+id+'&opt='+opt+'&val='+$('#'+id).val()+'&calStyle=m',
	});
}

function readHst(id) {
	$m.dialog.open({
		id:'listReadHstPop',
		title:'<u:msg titleId="bb.jsp.listReadHstPop.title" alt="조회자 정보" />',
		url:'/ct/board/listReadHstPop.do?menuId=${menuId}&brdId=${param.brdId}&bullId=' + id,
	});
};

<% // [상단버튼:등록] 등록 %>
function regBull() {
	$m.nav.next(event, "./setBoard.do?menuId=${menuId}&ctId=${ctFncDVo.ctId}");
};

//]]>
</script>

<c:if test="${writeAuth == 'Y'}">
<div class="btnarea">
    <div class="size">
        <dl>
           	 <dd class="btn" onclick="regBull();"><u:msg titleId="cm.btn.write" alt="등록" /></dd>
     </dl>
    </div>
</div>
</c:if>

<!--listsearch S-->
<form id="searchForm" name="searchForm" action="./listBull.do" onsubmit="searchList(event);">
<input type="hidden" name="menuId" value="${param.menuId}" />
<input type="hidden" name="brdId" value="${param.brdId}" />
<input type="hidden" name="schCat" id="schCat" value="" />
<input type="hidden" name="ctId" id="ctId" value="${param.ctId}" />
<div class="listsearch">
		<div class="listselect schTxt1">
		    <div class="open1 schOpnLayer1" style="display:none;">
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
			<div class="select_in1" onclick="holdHide = true;$('.schOpnLayer1').toggle();">
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
	
	<c:if test="${fn:length(ctBullList) == 0}">
         <div class="listdiv_nodata" ><dl><dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd></dl></div>
	</c:if>
	<c:if test="${fn:length(ctBullList) > 0}">
	<c:forEach items="${ctBullList}" var="ctBullVo" varStatus="status">
          <div class="listdiv" onclick="javascript:viewBull('${ctBullVo.bullId}');">
          	  <u:set var="replyYn" test="${ctBullVo.replyDpth > 0 }" value="Y" elseValue="N"/>
          	  <c:if test="${replyYn eq 'Y' }"><div class="listcheck_comment"><dl><dd class="comment"></dd></dl></div></c:if>
	          <div class="list${replyYn eq 'Y' ? '_comment' : '' }">
              <dl>
              <dd class="tit" ><c:if test="${ctBullVo.newYn == 'Y'}"><div class="new"></div></c:if>${ctBullVo.subj}<c:if test="${baBrdBVo.cmtYn == 'Y'}">(<u:out value="${ctBullVo.cmtCnt}" type="number" />)</c:if></dd>
              <dd class="name">
				  	<u:out value="${ctBullVo.regrNm}" />ㅣ<u:out value="${ctBullVo.regDt}" type="longdate" /> ㅣ 
					<u:out value="${ctBullVo.readCnt}" type="number" />
              </dd>
           </dl>
              </div>
          </div>
	</c:forEach>
	</c:if>
	
	 </article>
    
</div>
	<m:pagination />
    
<jsp:include page="/WEB-INF/jsp/ct/inc/footerInc.jsp" flush="false" />
</section>
<!--//section E-->
