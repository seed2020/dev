<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:secu auth="W" ><u:set test="${true}" var="writeAuth" value="Y"/></u:secu>
<u:set test="${param.schCat != null}" var="schCat" value="${param.schCat}" elseValue="bcNm" />
<u:set var="agntParam" test="${!empty schBcRegrUid }" value="&schBcRegrUid=${schBcRegrUid }" elseValue=""/>
<c:if test="${listPage eq 'listBc'}">
<u:set test="${!empty param.schFldId || (!empty param.schFldTypYn && param.schFldTypYn == 'A') }" var="unfoldareaUsed" value="Y" elseValue="N" />
</c:if>
<c:if test="${listPage eq 'listAgntBc'  }">
<u:set test="${!empty param.schBcRegrUid || !empty param.schFldId || (!empty param.schFldTypYn && param.schFldTypYn == 'A')}" var="unfoldareaUsed" value="Y" elseValue="N" />
</c:if>
<c:if test="${listPage eq 'listOpenBc'  }">
<u:set test="${!empty param.schOpenTypCd }" var="unfoldareaUsed" value="Y" elseValue="N" />
</c:if>
<u:set var="urlPrefix" test="${listPage eq 'listPubBc'}" value="/wb/pub" elseValue="/wb"/>
<script type="text/javascript">
//<![CDATA[
function searchList(event){
	$m.nav.curr(event, '${urlPrefix}/${listPage }.do?'+$('#searchForm').serialize());
}

// 상세보기
function viewBc(bcId) {
	$m.nav.next(event, "${urlPrefix}/${viewPage}.do?${paramsForList }&typ=${param.typ}${agntParam}&bcId="+bcId);
}

function fnSetSchCd(cd)
{
	$('#schCat').val(cd);
	$('.listselect span').text($(".open1 dd[data-schCd="+cd+"]").text());
	$('.open1').hide();
}

function fnSetCatId(cd)
{
	$('#schBcRegrUid').val(cd);
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

var holdHide = false, holdHide2 = false;
$(document).ready(function() {
	fnSetSchCd('${schCat}');
	fnSetCatId('${schBcRegrUid}');
	$('body:first').on('click', function(){
		if(holdHide) holdHide = false;
		else $(".open1").hide();
		if(holdHide2) holdHide2 = false;
		else $(".schOpnLayer2").hide();
	});
	
	if('${unfoldareaUsed}' == 'Y')
		fnUnfold();
	
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('list');
});

function fnSchFld(){
	if($('#schBcRegrUid').val() != ''){
		$m.nav.next(event, "${urlPrefix}/findBcFldSub.do?menuId=${menuId}&schBcRegrUid="+$('#schBcRegrUid').val());
	}else{
		$m.nav.next(event, "${urlPrefix}/findBcFldSub.do?menuId=${menuId}${agntParam }");
	}
	
}
<% // [상단버튼:등록] 등록 %>
function fnReg() {
	$m.nav.next(event, '${urlPrefix}/${setPage }.do?${paramsForList}${agntParam }');
}
//]]>
</script>
<c:if test="${writeAuth == 'Y' && listPage ne 'listAllBc' && ( listPage eq 'listBc' || ( listPage eq 'listAgntBc' && wbBcAgntAdmBVo.authCd eq 'RW' ) )}">
<div class="btnarea">
    <div class="size">
        <dl>
           	 <dd class="btn" onclick="fnReg();"><u:msg titleId="cm.btn.write" alt="등록" /></dd>
     </dl>
    </div>
</div>
</c:if>
<!--listsearch S-->
<form id="searchForm" name="searchForm" action="./${listPage }.do" onsubmit="searchList(event);">
<input type="hidden" name="menuId" value="${param.menuId}" />
<input type="hidden" name="schCat" id="schCat" value="" />
<input type="hidden" name="schBcRegrUid" id="schBcRegrUid" value="" />
<div class="listsearch">
	<div class="listselect">
	    <div class="open1" style="display:none;">
	        <div class="open_in1">
	        	<div class="open_div">
			        <dl>       
				        <c:forEach	items="${wbBcUserLstSetupRVoList}" var="wbBcUserLstSetupRVo" varStatus="status">
					        <dd class="txt" onclick="javascript:fnSetSchCd('${wbBcUserLstSetupRVo.atrbId }');" data-schCd="${wbBcUserLstSetupRVo.atrbId }"><u:msg titleId="${wbBcUserLstSetupRVo.msgId }" alt="검색항목" /></dd>
					        <dd class="line"></dd>
						</c:forEach>
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
		<c:choose>
			<c:when test="${listPage eq 'listBc' || listPage eq 'listPubBc'}">
                <dd class="etr_input">
                	<div class="etr_ipmany">
	                    <dl>
		                    <dd class="etr_ip_lt">
		                    	<div class="ip_txt">
		                    		<input type="hidden" id="schFldId" name="schFldId" value="${param.schFldId}" />
		                    		<input type="text" name="schFldNm" id="schFldNm" class="etr_iplt" value="${param.schFldNm }" readonly="readonly"/>
		                    	</div>
		                    	<div class="ip_delete" onclick="$('#schFldNm').val('');$('#schFldId').val('');"></div>
		                    </dd>
		                    <dd class="etr_se_rt" >
		                    	<div class="etr_btn" onclick="fnSchFld();"><u:msg titleId="wb.cols.fldNm" alt="폴더"/></div>
		                        <div class="etr_ipmany">
		                        <dl>
		                        <dd class="wblank5"></dd>
		                        <u:set var="checked" test="${param.schFldTypYn == 'A'}" value="true" elseValue="false"/>
								<m:check type="checkbox" id="schFldTypYn" name="schFldTypYn" inputId="schFldTypYn" value="A" titleId="wb.check.subIncl" checked="${checked }"  />
		                        </dl>
		                        </div>
		                    </dd>
	                    </dl>
                    </div>
                </dd>
			</c:when>
			<c:when test="${listPage eq 'listAgntBc' }">
				<dd class="etr_input">
                	<div class="etr_ipmany">
	                    <dl>
		                    <dd class="etr_ip_lt">
		                    	<div class="ip_txt">
		                    		<input type="hidden" id="schFldId" name="schFldId" value="${param.schFldId}" />
		                    		<input type="text" name="schFldNm" id="schFldNm" class="etr_iplt" value="${param.schFldNm }" readonly="readonly"/>
		                    	</div>
		                    	<div class="ip_delete" onclick="$('#schFldNm').val('');$('#schFldId').val('');"></div>
		                    </dd>
		                    <dd class="etr_se_rt" >
		                    	<div class="etr_btn" onclick="fnSchFld();"><u:msg titleId="wb.cols.fldNm" alt="폴더"/></div>
		                        <div class="etr_ipmany">
		                        <dl>
		                        <dd class="wblank5"></dd>
		                        <u:set var="checked" test="${param.schFldTypYn == 'A'}" value="true" elseValue="false"/>
								<m:check type="checkbox" id="schFldTypYn" name="schFldTypYn" inputId="schFldTypYn" value="A" titleId="wb.check.subIncl" checked="${checked }"  />
		                        </dl>
		                        </div>
		                    </dd>
	                    </dl>
                    </div>
                </dd>
                <dd class="etr_blank"></dd>
				<dd class="etr_input">
		              <div class="etr_ipmany">
		              <dl>
		              <dd class="etr_se_lt">
		                  <div class="etr_open2 schOpnLayer2" style="display:none;">
		                    <div class="open_in1">
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
			</c:when>
			<c:when test="${listPage eq 'listOpenBc' }">
                <dd class="etr_input">
                    <div class="etr_ipmany">
                    <dl>
					<c:forEach var="list" items="${schOpenTypCds }" varStatus="status">
						<c:if test="${list eq 'allPubl' }"><c:set var="allPublCheck" value="allPubl"/></c:if>
						<c:if test="${list eq 'deptPubl' }"><c:set var="deptPublCheck" value="deptPubl"/></c:if>
						<c:if test="${list eq 'apntrPubl' }"><c:set var="apntrPublCheck" value="apntrPubl"/></c:if>
					</c:forEach>
					
					<u:set var="checked" test="${allPublCheck == 'allPubl'}" value="true" elseValue="false"/>
					<m:check type="checkbox" id="schOpenTypCd" name="schOpenTypCd" inputId="schOpenTypCd" value="allPubl" checked="${checked }" onclick="" />
					<dd class="etr_body"><u:msg titleId="cm.option.allPubl" alt="전체공개" /></dd>
					                    
					<u:set var="checked" test="${deptPublCheck == 'deptPubl'}" value="true" elseValue="false"/>
					<m:check type="checkbox" id="schOpenTypCd" name="schOpenTypCd" inputId="schOpenTypCd" value="deptPubl" checked="${checked }" onclick="" />
					<dd class="etr_body"><u:msg titleId="cm.option.deptPubl" alt="부서공개" /></dd>           
					                    
					<u:set var="checked" test="${apntrPublCheck == 'apntrPubl'}" value="true" elseValue="false"/>
					<m:check type="checkbox" id="schOpenTypCd" name="schOpenTypCd" inputId="schOpenTypCd" value="apntrPubl" checked="${checked }" onclick="" />
					<dd class="etr_body"><u:msg titleId="cm.option.apntPubl" alt="지정인공개" /></dd>   
                    
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
<!--//listsearch E-->

<!--section S-->
<section>
<div class="listarea">
	<article>
	
	<c:choose>
		<c:when test="${!empty wbBcBMapList}">
			<c:forEach var="wbBcBMap" items="${wbBcBMapList}" varStatus="status">
                   <div class="listdiv2d">
                       <div class="list_lt">
                       <dl onclick="javascript:viewBc('${wbBcBMap.bcId }');" style="cursor:pointer;">
                       <dd class="tit">${wbBcBMap.bcNm}<c:if test="${!empty wbBcBMap.dftCntc}">(${wbBcBMap.dftCntc})</c:if></dd>
                       <dd class="body">${wbBcBMap.compNm} 
                       <c:if test="${!empty wbBcBMap.deptNm}">/ ${wbBcBMap.deptNm}</c:if>
                       <c:if test="${!empty wbBcBMap.gradeNm}">/ ${wbBcBMap.gradeNm}</c:if> 
                       </dd>
                    </dl>
                       </div>
                       <div class="list_rt">
                       <dl>
						<m:tel type="image" value="${wbBcBMap.dftCntc}"/>
						<c:if test="${wbBcBMap.dftCntcTypCd == 'mbno'}">
							<m:sms type="image" value="${wbBcBMap.dftCntc}"/>
						</c:if>
						<c:if test="${wbBcBMap.dftCntcTypCd != 'mbno' && !empty wbBcBMap.mbno}">
							<m:sms type="image" value="${wbBcBMap.mbno}"/>
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
<!--//section E-->

