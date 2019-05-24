<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:secu auth="W" ><u:set test="${true}" var="regAuth" value="Y"/></u:secu>
<u:set test="${param.schCat != null}" var="schCat" value="${param.schCat}" elseValue="SUBJ" />
<u:set test="${param.catId != null}" var="catId" value="${param.catId}" elseValue="" />
<u:set test="${(param.catId != null && param.catId != '') || (param.strtYmd != null && param.strtYmd != '') || (param.endYmd != null && param.endYmd != '')}" var="unfoldareaUsed" value="Y" elseValue="N" />
<script type="text/javascript">
//<![CDATA[
function searchList(event){
	$m.nav.curr(event, './listBull.do?'+$('#searchForm').serialize());
}

<% // [목록:제목] 게시물 조회 %>
function viewBull(id) {
	$m.nav.next(null, '/bb/viewBull.do?${params}&bullId=' + id);
}

function fnSetSchCd(cd)
{
	$('#schCat').val(cd);
	$('.schTxt1 span').text($(".schOpnLayer1 dd[data-schCd="+cd+"]").text());
	$('.schOpnLayer1').hide();
}

function fnSetCatId(cd)
{
	$('#catId').val(cd);
	$('.schTxt2 span').text($(".schOpnLayer2 dd[data-schCd='"+cd+"']").text());
	$('.schOpnLayer2').hide();
}

var holdHide = false, holdHide2 = false;
$(document).ready(function() {
	fnSetSchCd('${schCat}');
	fnSetCatId('${catId}');
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

function fnPichPop(){
	$m.dialog.open({
		id:'viewBullPich',
		title:'<u:msg titleId="bb.jsp.listBull.pich.title" alt="게시판 담당자" />',
		url:'/bb/viewBullPichPop.do?${params}',
	});
}

<% // [상단버튼:등록] 등록 %>
function regBull() {
	$m.nav.next(null, '/bb/setBull.do?${params}');
}

//]]>
</script>
<c:if test="${baBrdBVo.pichDispYn == 'Y' || regAuth == 'Y'}">
<div class="btnarea">
    <div class="size">
        <dl>
        	<c:if test="${baBrdBVo.pichDispYn == 'Y'}"><dd class="btn" onclick="javascript:fnPichPop();"><u:msg titleId="cols.pich" alt="담당자"/></dd></c:if>
           	 <c:if test="${regAuth == 'Y'}"><dd class="btn" onclick="regBull();"><u:msg titleId="cm.btn.write" alt="등록" /></dd></c:if>
     </dl>
    </div>
</div>
</c:if>
<form id="searchForm" name="searchForm" action="./listPhotoBull.do" onsubmit="searchList(event);">
<input type="hidden" name="menuId" value="${param.menuId}" />
<input type="hidden" name="brdId" value="${param.brdId}" />
<input type="hidden" name="schCat" id="schCat" value="" />
<input type="hidden" name="catId" id="catId" value="" />
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
						<c:if test="${baBrdBVo.brdTypCd == 'N'}">
							<dd class="txt" onclick="javascript:fnSetSchCd('REGR_NM');" data-schCd="REGR_NM"><u:msg titleId="cols.regr" alt="등록자" /></dd>
						</c:if>
						<c:if test="${baBrdBVo.brdTypCd == 'A'}">
							<dd class="txt" onclick="javascript:fnSetSchCd('ANON_REGR_NM');" data-schCd="ANON_REGR_NM"><u:msg titleId="cols.regr" alt="등록자" /></dd>
						</c:if>
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
		<dd class="etr_blank"></dd>
		
    <c:if test="${baBrdBVo.catYn == 'Y'}">
		<dd class="etr_input">
              <div class="etr_ipmany">
              <dl>
              <dd class="etr_se_lt">
                  <div class="etr_open2 schOpnLayer2" style="display:none;">
                    <div class="open_in1">
                        <div class="open_div">
					        <dl>
					        <dd class="txt" onclick="javascript:fnSetCatId('');" data-schCd=""><u:msg titleId="cm.option.all" alt="전체" /></dd>
					        <dd class="line"></dd>
					        <c:forEach items="${baCatDVoList}" var="catVo" varStatus="status">
						        <dd class="txt" onclick="javascript:fnSetCatId('${catVo.catId}');" data-schCd="${catVo.catId}">${catVo.rescNm}</dd>
						        <dd class="line"></dd>
							</c:forEach>
					    	</dl>
                        </div>
                    </div>
                </div>   
                <div class="select_in1 schTxt2" onclick="holdHide2 = true;$('.schOpnLayer2').toggle();">
                <dl>
                <dd class="select_txt"><span></span></dd>
                <dd class="select_btn"></dd>
                </dl>
                </div>
            </dd>
            </dl>
            </div>
        </dd>
		</c:if>  

    	<dd class="etr_blank1"></dd>
 		</dl>
    </div> 
</div>

</form>

<section>
<div class="listarea">
<article>

	<c:choose>
		<c:when test="${!empty bbBullLVoList}">
			<c:forEach items="${bbBullLVoList}" var="bbBullLVo" varStatus="status">
				 <c:set var="photoVo" value="${bbBullLVo.photoVo}" />
				 <u:set test="${photoVo != null}" var="savePath" value="${_cxPth}${photoVo.savePath}" elseValue="/images/blue/noimg2.png" />
                 <div class="listdiv" onclick="viewBull('${bbBullLVo.bullId}')">
                     <div class="list_photoarea">
                     <div class="list_photoareain">
                         <div class="list_photo">
                         <dl>
                         <dd class="tit">
                         	<c:if test="${bbBullLVo.newYn == 'Y'}"><div class="new"></div></c:if>
	                    	<c:if test="${bbBullLVo.readYn != 'Y'}"><strong></c:if>
	                    	<c:if test="${bbBullLVo.ugntYn == 'Y'}"><span class="ctxt1">[<u:msg titleId="bb.option.ugnt" alt="긴급" />]</span></c:if><c:if test="${bbBullLVo.secuYn == 'Y'}"><span class="ctxt2">[<u:msg titleId="bb.option.secu" alt="보안" />]</span></c:if>
							${bbBullLVo.subj}<c:if test="${baBrdBVo.cmtYn == 'Y'}">(<u:out value="${bbBullLVo.cmtCnt}" type="number" />)</c:if>
							<c:if test="${bbBullLVo.readYn != 'Y'}"></strong></c:if>
                         </dd>
                         <dd class="name">
                         	<u:out value="${bbBullLVo.deptNm}" /> /
                         	${bbBullLVo.regrNm}  
                         </dd>
                         <dd class="name">
                         	<u:out value="${bbBullLVo.regDt}" type="shortdate" /> /
							<u:out value="${bbBullLVo.readCnt}" type="number" />
                         </dd>
                      </dl>
                         </div>
                     </div>
                     </div>

                     <div class="list_photort">
                     <dl>
                     <c:if test="${photoVo != null}">
                    	 <dd class="photo"><img src="${savePath}" width="73" height="73" onerror='this.src="/images/blue/noimg2.png"' /></dd>
                     </c:if>
                     <c:if test="${photoVo == null}">
                     	<dd class="noimg"></dd>
                     </c:if>
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
