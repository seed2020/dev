<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
import="
		com.innobiz.orange.web.pt.secu.UserVo,
		com.innobiz.orange.web.pt.utils.LoutUtil"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
//<![CDATA[
<%
// Select Option 클릭 %>
function setSelOptions(codeNm, code, value){
	var $form = $("#searchForm");
	$form.find("input[name='"+codeNm+"']").val(code);
	$form.find("#"+codeNm+"Container #selectView span").text(value);
	$form.find("#"+codeNm+"Container #"+codeNm+"Open").hide();
}<%// 검색 클릭 %>
function searchList(event){
	$m.nav.curr(event, '/st/site/listSite.do?'+$('#searchForm').serialize());
}
$(document).ready(function() {
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('list');
});
//]]>
</script>
<form id="searchForm" name="searchForm" action="${_uri}" onsubmit="searchList(event);" >
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="schCat" value="siteNm" />

<!--listsearch S-->
<div class="listsearch">
    <div class="listselect" id="catIdContainer">
    	<c:set var="catNm" value=""/>
	    <c:set var="catId" value=""/>
        <div class="open1" id="catIdOpen" style="display:none">
            <div class="open_in1">
                <div class="open_div">
                <dl>
                 <dd class="txt" onclick="setSelOptions('catId',$(this).attr('data-code'),$(this).text());" data-code=""><u:msg var="noSelect" titleId="cm.option.all" alt="전체선택"/>${noSelect }</dd>
                    <c:if test="${empty param.catId}"><c:set var="catNm" value="${noSelect }"/></c:if>
                    <c:forEach items="${stCatBVoList}" var="stCatBVo" varStatus="status">
                    <c:if test="${stCatBVo.catId == param.catId}"><c:set var="catNm" value="${stCatBVo.catNm }"/><c:set var="catId" value="${stCatBVo.catId }"/></c:if>
					<dd class="line"></dd>
                    <dd class="txt" onclick="setSelOptions('catId',$(this).attr('data-code'),$(this).text());" data-code="${stCatBVo.catId}">${stCatBVo.catNm }</dd>
                    </c:forEach>
             	</dl>
                </div>
            </div>
        </div>
    	 <input type="hidden" name="catId" value="${param.catId }" class="notChkCls"/>
        <div class="select1">
            <div class="select_in1" onclick="$('#catIdContainer #catIdOpen').toggle();">
            <dl>
            <dd class="select_txt1" id="selectView"><span>${catNm }</span></dd>
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
<!--//listsearch E-->
        
</form>
<section>
<div class="listarea">
<article>
<%
UserVo userVo = (UserVo)session.getAttribute("userVo");
%>
	<c:choose>
		<c:when test="${!empty stSiteBVoList}">
			<c:forEach items="${stSiteBVoList}" var="stSiteBVo" varStatus="status">
				 <c:set var="stSiteImgDVo" value="${stSiteBVo.stSiteImgDVo}" />
				 <u:set test="${stSiteImgDVo != null}" var="imgPath" value="${_cxPth}${stSiteImgDVo.imgPath}" elseValue="/images/blue/noimg2.png" />
				 <u:set var="siteUrl" test="${!empty stSiteBVo.siteUrl }" value="${stSiteBVo.siteUrl }" elseValue=""/>
				 <%
				 	String siteUrl=(String)request.getAttribute("siteUrl");				
				 	siteUrl=LoutUtil.converTypedParam(siteUrl!=null && siteUrl.isEmpty() ? null : siteUrl, userVo);				 	
				 	request.setAttribute("siteUrl", siteUrl);
				 %>
				 <u:set var="onclick" test="${!empty stSiteBVo.siteUrl }" value="onclick=\"window.open('${siteUrl }','_blank')\"" elseValue=""/>
                 <div class="listdiv" ${onclick }>
                     <div class="list_photoarea">
                     <div class="list_photoareain">
                         <div class="list_photo">
                         <dl>
                         <dd class="tit">
	                    	<div class="ellipsis" title="${stSiteBVo.siteNm}" >${stSiteBVo.siteNm}</div>
                         </dd>                         
                         <dd class="name">
                         	<u:out value="${stSiteBVo.cont}" maxLength="120"/>
                         </dd>
                          <dd class="name">&nbsp;</dd>
                      </dl>
                         </div>
                     </div>
                     </div>

                     <div class="list_photort">
                     <dl>
                     <c:if test="${stSiteImgDVo != null}">
                    	 <dd class="photo"><img src="${imgPath}" width="73" height="73" onerror='this.src="/images/blue/noimg2.png"' /></dd>
                     </c:if>
                     <c:if test="${stSiteImgDVo == null}">
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

