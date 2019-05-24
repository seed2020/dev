<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:secu auth="W" ><u:set test="${true}" var="writeAuth" value="Y"/></u:secu>
<u:set test="${param.schCat != null}" var="schCat" value="${param.schCat}" elseValue="metngSubj" />
<u:set test="${param.schMetngClsCd != null}" var="schMetngClsCd" value="${param.schMetngClsCd}" elseValue="" />
<u:set var="agntParam" test="${!empty schBcRegrUid }" value="&schBcRegrUid=${schBcRegrUid }" elseValue=""/>

<u:set test="${!empty schMetngClsCd || ( listPage eq 'listAgntMetng' && !empty param.schBcRegrUid)}" var="unfoldareaUsed" value="Y" elseValue="N" />
<script type="text/javascript">
//<![CDATA[
function searchList(event){
	$m.nav.curr(event, '/wb/${listPage }.do?'+$('#searchForm').serialize());
}

//상세보기
function viewMetng(bcMetngDetlId) {
	$m.nav.next(event, "/wb/${viewPage}.do?${paramsForList }&schOpenYn=${param.schOpenYn}${agntParam}&bcMetngDetlId="+bcMetngDetlId);
};

function fnSetSchCd(cd)
{
	$('#schCat').val(cd);
	$('.listselect span').text($(".open1 dd[data-schCd="+cd+"]").text());
	$('.open1').hide();
}

function fnSetCatId(cd)
{
	$('#schBcRegrUid').val(cd);
	$('.schTxt3 span').text($(".schOpnLayer3 dd[data-schCd='"+cd+"']").text());
	$('.schOpnLayer3').hide();
}

function fnSetMetngClsCd(cd)
{
	$('#schMetngClsCd').val(cd);
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

var holdHide = false,holdHide2 = false,holdHide3 = false;
$(document).ready(function() {
	fnSetSchCd('${schCat}');
	fnSetMetngClsCd('${schMetngClsCd}');
	fnSetCatId('${schBcRegrUid}');
	$('body:first').on('click', function(){
		if(holdHide) holdHide = false;
		else $(".open1").hide();
		if(holdHide2) holdHide2 = false;
		else $(".schOpnLayer2").hide();
		if(holdHide3) holdHide3 = false;
		else $(".schOpnLayer3").hide();
	});
	
	if('${unfoldareaUsed}' == 'Y')
		fnUnfold();
	
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('list');
});

<% // [상단버튼:등록] 등록 %>
function fnReg(){
	$m.nav.next(event, '/wb/${setPage }.do?${paramsForList }&schOpenYn=${param.schOpenYn}${agntParam}');
};

//]]>
</script>
<c:if test="${writeAuth == 'Y' && listPage ne 'listAllMetng' && ( ( listPage eq 'listMetng' && ( empty param.schOpenYn || param.schOpenYn eq 'N' ) ) || ( listPage eq 'listAgntMetng' && wbBcAgntAdmBVo.authCd eq 'RW' ) )}">
<div class="btnarea">
    <div class="size">
        <dl>
           	 <dd class="btn" onclick="fnReg();"><u:msg titleId="cm.btn.write" alt="등록" /></dd>
     </dl>
    </div>
</div>
</c:if>

<!--listsearch S-->
<form id="searchForm" name="searchForm" action="/wb/${listPage }.do" onsubmit="searchList(event);">
<input type="hidden" name="menuId" value="${param.menuId}" />
<input type="hidden" name="schCat" id="schCat" value="" />
<input type="hidden" name="schMetngClsCd" id="schMetngClsCd" value="" />
<input type="hidden" name="schBcRegrUid" id="schBcRegrUid" value="" />
<div class="listsearch">
		<div class="listselect">
		    <div class="open1" style="display:none;">
		        <div class="open_in1">
		        	<div class="open_div">
				        <dl>       
					        <dd class="txt" onclick="javascript:fnSetSchCd('metngSubj');" data-schCd="metngSubj"><u:msg titleId="cols.subj" alt="제목" /></dd>
					        <dd class="line"></dd>
					        <dd class="txt" onclick="javascript:fnSetSchCd('metngCont');" data-schCd="metngCont"><u:msg titleId="cols.cont" alt="내용" /></dd>
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
		<dd class="etr_input">
              <div class="etr_ipmany">
              <dl>
              <dd class="etr_se_lt">
                  <div class="etr_open2 schOpnLayer2" style="display:none;">
                    <div class="open_in1">
                        <div class="open_div">
					        <dl>
						        <dd class="txt" onclick="javascript:fnSetMetngClsCd('');" data-schCd=""><u:msg titleId="cm.option.all" alt="전체선택" /></dd>
						        <dd class="line"></dd>
								<c:forEach items="${wbMetngClsCdBVoList}" var="list" varStatus="status">
							        <dd class="txt" onclick="javascript:fnSetMetngClsCd('${list.rescId}');" data-schCd="${list.rescId}">${list.rescNm}</dd>
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
            
			<c:if test="${listPage eq 'listAgntMetng' }">
	              <dd class="etr_se_rt">
	                  <div class="etr_open3 schOpnLayer3" style="display:none;">
	                    <div class="open_in2">
	                        <div class="open_div">
						        <dl>
						        <dd class="txt" onclick="javascript:fnSetCatId('');" data-schCd=""><u:msg  titleId="wb.jsp.setAgntAdm.select.agntBc"/></dd>
						        <dd class="line"></dd>
						        <c:forEach items="${agntSetupList}" var="agntVo" varStatus="status">
							        <dd class="txt" onclick="javascript:fnSetCatId('${agntVo.regrUid}');" data-schCd="${agntVo.regrUid}">${agntVo.userNm}</dd>
							        <dd class="line"></dd>
								</c:forEach>
						    	</dl>
	                        </div>
	                    </div>
	                </div>   
	                <div class="select_in2 schTxt3" onclick="holdHide3 = true;$('.schOpnLayer3').toggle();">
	                <dl>
	                <dd class="select_txt"><span></span></dd>
	                <dd class="select_btn"></dd>
	                </dl>
	                </div>
	            </dd>
		    </c:if> 
		    
            </dl>
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
	<c:choose>
		<c:when test="${!empty wbBcMetngDVoList}">
			<c:forEach var="list" items="${wbBcMetngDVoList}" varStatus="status">
			    <div class="listdiv" onclick="javascript:viewMetng('${list.bcMetngDetlId }');" >
			        <div class="list">
			        <ul>
			        <li class="tit">
						${list.metngSubj }
			        </li>
			        <li class="name">
				        <c:if test="${!empty list.bcNm }">${list.bcNm } ㅣ </c:if><c:if test="${!empty list.metngClsNm }">${list.metngClsNm } ㅣ </c:if>
				        ${list.metngYmd }
			        </li>
			     </ul>
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

