<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set test="${param.rescKndId != null}" var="rescKndId" value="${param.rescKndId}" elseValue="" />
<script type="text/javascript">
//<![CDATA[
<%
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
function viewRescMng(rescMngId) {
	$m.nav.next(event,'/wr/viewResc.do?${params}&rescMngId='+rescMngId);
}

function searchList(event){
	$m.nav.curr(event, '/wr/listResc.do?'+$('#searchForm').serialize());
}

function fnSetSchCd(cd)
{
	$('#rescKndId').val(cd);
	$('.schTxt1 span').text($(".schOpnLayer1 dd[data-schCd='"+cd+"']").text());
	$('.schOpnLayer1').hide();
}

<% // [상단버튼:등록] 등록 %>
function setRezv(rezvId , obj){
	var url = '/wr/setRezv.do?${params}&listPage=listResc';
	
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

var holdHide = false;
$(document).ready(function() {
fnSetSchCd('${rescKndId}');
$('body:first').on('click', function(){
if(holdHide) holdHide = false;
else $(".schOpnLayer1").hide();
});

<%// 목록의 footer 위치를 일정하게 %>
$space.placeFooter('list');
});

//]]>
</script>
<u:secu auth="W">
<div class="btnarea">
    <div class="size">
        <dl>
           	 <dd class="btn" onclick="setRezv();"><u:msg titleId="wr.btn.rescRezv" alt="자원예약" /></dd>           	 
     </dl>
    </div>
</div>
</u:secu>
<form name="searchForm" id="searchForm" action="./listResc.do" >
<input type="hidden" name="menuId" value="${param.menuId}" />
<input type="hidden" name="rescKndId" id="rescKndId" value="" />
<div class="listsearch">
	<div class="listselect">
		<div class="open1 schOpnLayer1" style="display:none;">
			<div class="open_in1">
				<div class="open_div">
					<dl>
					<dd class="txt" onclick="javascript:fnSetSchCd('');" data-schCd=""><u:msg titleId="cm.option.all" alt="전체선택"/></dd>
					<dd class="line"></dd>
					<c:forEach items="${wrRescKndBVoList}" var="list" varStatus="status">
					<dd class="txt" onclick="javascript:fnSetSchCd('${list.rescKndId}');" data-schCd="${list.rescKndId}">${list.kndNm}</dd>
					<dd class="line"></dd>
					</c:forEach>
					</dl>
				</div>
			</div>
		</div>
		<div class="select1">
			<div class="select_in1" onclick="holdHide = true;$('.schOpnLayer1').toggle();">
				<dl>
				<dd class="select_txt1 schTxt1"><span></span></dd>
				<dd class="select_btn"  ></dd>
				</dl>
			</div>
		</div>
		<c:if test="${!empty ptCompBVoList }">
		<c:set var="compNm" value=""/>
	    <c:set var="paramCompId" value=""/>
		<div class="open2" id="paramCompIdOpen" style="display:none;">
			<div class="open_in2">
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
		<div class="select2" id="paramCompIdContainer">
				<div class="select_in2" onclick="$('#paramCompIdOpen').toggle();">
					<dl>
					<dd class="select_txt2" id="selectView"><span>${compNm }</span></dd>
					<dd class="select_btn"></dd>
					</dl>
				</div>
			</div>
		</c:if>
	</div>
	<div class="searchbtn${!empty ptCompBVoList ? 2 : 1}" onclick="searchList(event);"><div class="search"></div></div>
</div>
</form>

<section>
<div class="listarea">
	<article>

	<c:choose>
	<c:when test="${!empty wrRescMngBVoList}">
	<c:forEach var="list" items="${wrRescMngBVoList}" varStatus="status">
	<c:set var="wrRescImgDVo" value="${list.wrRescImgDVo}" />
	<div class="listdiv" onclick="viewRescMng('${list.rescMngId}');">
		<div class="list_photoarea">
			<div class="list_photoareain">
				<div class="list_photo">
					<dl>
					<dd class="tit">${list.rescNm }</dd>
					<dd class="name">${list.kndNm } / ${list.rescLoc }</dd>
					<dd class="name"><u:msg titleId="wr.option.disc${list.discYn }" alt="사용여부"/> / ${list.rescAdmNm }</dd>
					</dl>
				</div>
			</div>
		</div>

		<div class="list_photort">
			<dl>
			<c:if test="${wrRescImgDVo != null}">
			<dd class="photo"><img src="${_cxPth}${list.wrRescImgDVo.imgPath}" width="73" height="73" onerror='this.src="/images/blue/noimg2.png"'/></dd>
			</c:if>
			<c:if test="${wrRescImgDVo == null}">
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
