<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set test="${param.schCat != null}" var="schCat" value="${param.schCat}" elseValue="bcNm" />
<script type="text/javascript">
<!--
function searchList(event){
	$m.nav.curr(event, './setBcBumk.do?'+$('#searchForm').serialize());
}

// 상세보기
function viewBc(bcId) {
	$m.nav.next(event, "/wb/viewBumkBc.do?${paramsForList }&typ=${param.typ}${agntParam}&bcId="+bcId);
};

function fnSetSchCd(cd)
{
	$('#schCat').val(cd);
	$('.listselect span').text($(".open1 dd[data-schCd="+cd+"]").text());
	$('.open1').hide();
}

$(document).ready(function() {
	fnSetSchCd('${schCat}');
	$space.placeFooter('list');
});
//-->
</script>


<!--listsearch S-->
<form id="searchForm" name="searchForm" action="./setBcBumk.do" onsubmit="searchList(event);">
<input type="hidden" name="menuId" value="${param.menuId}" />
<input type="hidden" name="schCat" id="schCat" value="${param.schCat }" />
<!--listsearch S-->
<div class="listsearch">
    <div class="listselect">
    	<c:set var="schCatNm" />
    	<div class="open1" style="display:none;">
	        <div class="open_in1">
	        	<div class="open_div">
			        <dl>       
				        <c:forEach	items="${wbBcUserLstSetupRVoList}" var="wbBcUserLstSetupRVo" varStatus="status">
				        	<c:if test="${status.index > 0 }"><dd class="line"></dd></c:if>
					        <dd class="txt" onclick="javascript:fnSetSchCd('${wbBcUserLstSetupRVo.atrbId }');" data-schCd="${wbBcUserLstSetupRVo.atrbId }"><u:msg titleId="${wbBcUserLstSetupRVo.msgId }" alt="검색항목" /></dd>
						</c:forEach>
			    	</dl>
			    </div>	
	        </div>
	    </div>
        <div class="select1">
            <div class="select_in1" onclick="$('.open1').toggle();">
            <dl>
            <dd class="select_txt1"><span><u:msg titleId="cols.subj" alt="제목" /></span></dd>
            <dd class="select_btn" ></dd>
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
<!--//listsearch E-->
</form>


<!--section S-->
<section>
<div class="listarea">

	<article>
	<c:choose>
		<c:when test="${!empty wbBcBVoBumkList && fn:length(wbBcBVoBumkList) > 1}">
			<c:forEach var="list" items="${wbBcBVoBumkList}" varStatus="status">
				<c:if test="${!status.last}">
			    
					<c:choose>
						<c:when test="${list.dftCntcTypCd eq 'homePhon' }"><c:set var="dftCntc" value="${list.homePhon }"/></c:when>
						<c:when test="${list.dftCntcTypCd eq 'compPhon' }"><c:set var="dftCntc" value="${list.compPhon }"/></c:when>
						<c:otherwise><c:set var="dftCntc" value="${list.mbno }"/></c:otherwise>
					</c:choose>
			    
                   <div class="listdiv2d">
                       <div class="list_lt" onclick="javascript:viewBc('${list.bcId }');" style="cursor:pointer;">
                       <dl>
                       <dd class="tit">${list.bcNm}<c:if test="${!empty dftCntc}">(${dftCntc})</c:if></dd>
                       <dd class="body">${list.compNm} 
                       <c:if test="${!empty list.deptNm}">/ ${list.deptNm}</c:if>
                       <c:if test="${!empty list.gradeNm}">/ ${list.gradeNm}</c:if> 
                       </dd>
                    </dl>
                       </div>
                       <div class="list_rt">
                       <dl>
						<m:tel type="image" value="${dftCntc}"/>
						
						<c:if test="${list.dftCntcTypCd == 'mbno'}">
							<m:sms type="image" value="${list.dftCntc}"/>
						</c:if>
						<c:if test="${list.dftCntcTypCd != 'mbno' && !empty list.mbno}">
							<m:sms type="image" value="${list.mbno}"/>
						</c:if>
                    </dl>
                       </div>
                   </div>
			    </c:if>
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
<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
</section>
<!--//section E-->

