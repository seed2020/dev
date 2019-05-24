<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<c:if test="${!empty myAuth && myAuth == 'M' }"><u:set test="${true}" var="writeAuth" value="Y"/></c:if>
<u:set test="${param.schCat != null}" var="schCat" value="${param.schCat}" elseValue="SUBJ" />
<u:set test="${param.catChoi != null}" var="catChoi" value="${param.catChoi}" elseValue="" />
<u:set test="${param.catChoi != null && param.catChoi != ''}" var="unfoldareaUsed" value="Y" elseValue="N" />
<script type="text/javascript">
//<![CDATA[
function searchList(event){
	$m.nav.curr(event, '/ct/site/listSite.do?'+$('#searchForm').serialize());
}

function fnSetSchCd(cd)
{
	$('#schCat').val(cd);
	$('.schTxt1 span').text($(".schOpnLayer1 dd[data-schCd="+cd+"]").text());
	$('.schOpnLayer1').hide();
}

function fnSetCatId(cd)
{
	$('#catChoi').val(cd);
	$('.schTxt2 span').text($(".schOpnLayer2 dd[data-schCd='"+cd+"']").text());
	$('.schOpnLayer2').hide();
}

var holdHide = false, holdHide2 = false;
$(document).ready(function() {
	fnSetSchCd('${schCat}');
	fnSetCatId('${catChoi}');
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

function openSite(url) {
	window.open(url);
}

function viewSite(id) {
	$m.nav.next(event, '/ct/site/viewSite.do?menuId=${menuId}&ctId=${ctId}&catId=${siteVo.catId}&siteId=' + id);
}

<% // [상단버튼:등록] 등록 %>
function regBtn() {
	$m.nav.next(event, "./setSite.do?menuId=${menuId}&ctId=${ctId}");
};

//]]>
</script>
<c:if test="${writeAuth == 'Y' || ( !empty authChkW && authChkW == 'W' )}">
<div class="btnarea">
    <div class="size">
        <dl>
           	 <dd class="btn" onclick="regBtn();"><u:msg titleId="cm.btn.write" alt="등록" /></dd>
     </dl>
    </div>
</div>
</c:if>
<!--listsearch S-->
<form id="searchForm" name="searchForm" action="./listBull.do" onsubmit="searchList(event);">
<input type="hidden" name="menuId" value="${param.menuId}" />
<input type="hidden" name="schCat" id="schCat" value="" />
<input type="hidden" name="catChoi" id="catChoi" value="" />
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
								<c:forEach var="siteCatVo" items="${siteCatList}" varStatus="status">
							        <dd class="txt" onclick="javascript:fnSetCatId('${siteCatVo.catId}');" data-schCd="${siteCatVo.catId}">${siteCatVo.catNm}</dd>
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
	
	<c:if test="${fn:length(siteList) == 0}">
             <div class="listdiv_nodata" >
             <dl>
             <dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd>
             </dl>
             </div>
	</c:if>
	<c:if test="${fn:length(siteList) > 0}">
	<c:forEach var="siteVo" items="${siteList}" varStatus="status">
          <div class="listdiv">
              <div class="list">
              <dl>
              <dd class="tit" onclick="viewSite('${siteVo.siteId}');">
				<u:out value="${siteVo.subj}"/>
              </dd>
              <dd class="name">
				  	<u:out value="${siteVo.catNm}"/> / ${siteVo.regrNm} /
					<fmt:parseDate var="dateTempParse" value="${siteVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
					<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
              </dd>
			  <dd><div class="list_btnarea">
                      <div class="size">
                      <dl>
                      <dd class="btn" onclick="openSite('${siteVo.url}');"><u:msg titleId="ct.btn.shcut" alt="바로가기" /></dd>
                 	  </dl>
                      </div>
                      </div>
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
