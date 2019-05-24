<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="paramStorIdQueryString" test="${!empty paramStorId }" value="&paramStorId=${paramStorId }" elseValue=""/><!-- 저장소ID -->
<u:params var="nonPageParams" excludes="schCat,schWord,pageNo,noCache"/>
<u:set var="callback" test="${!empty param.callback }" value="${param.callback }" elseValue="saveSubDoc"/>
<script type="text/javascript">
//<![CDATA[
<%// [확인] 저장 %>
function applyCfrm(){
	var docId=null;
	$('#docListArea .listdivline').find("input[name='docId']").each(function(){
		if($(this).attr('disabled')!='disabled'){
			docId = $(this).val();
			return true;
		}
	});
	if(docId==null) return;
	$m.nav.getWin(-1).${callback}(docId);
}<%// 문서 클릭 %>
function selectDoc(obj){
	$('#docListArea .listdivline').attr('class','listdiv');
	$(obj).attr('class','listdivline');
}<%// 검색 클릭 %>
function searchList(event){
	$m.nav.curr(event, '${_uri}?${nonPageParams}&'+$('#searchForm').serialize());
}<%
// Select Option 클릭 %>
function setSelOptions(codeNm, code, value){
	var $form = $("#searchForm");
	$form.find("input[name='"+codeNm+"']").val(code);
	$form.find("#"+codeNm+"Container #selectView span").text(value);
	$form.find("#"+codeNm+"Container #"+codeNm+"Open").hide();
}
var holdHide = false;
$(document).ready(function() {
	$('body:first').on('click', function(){
		if(holdHide) holdHide = false;
		else $("#listsearch #schCat").hide();
	});
	
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('list');
});

//]]>
</script>
<div class="btnarea">
    <div class="size">
        <dl>
           	 <dd class="btn" onclick="applyCfrm();"><u:msg titleId="cm.btn.confirm" alt="확인" /></dd>
     </dl>
    </div>
</div>
<!-- 검색조건 페이지 -->
<form id="searchForm" name="searchForm" action="${_uri}" onsubmit="searchList(event);" >
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="schCat" value="${empty param.schCat ? 'subj' : param.schCat}" />

<div class="listsearch" id="listsearch">
	<div class="listselect" id="schCatContainer">
		<div class="open1" id="schCatOpen" style="display:none">
			<div class="open_in1">
				<div class="open_div">
				<dl>
					<dd class="txt" onclick="setSelOptions('schCat',$(this).attr('data-code'),$(this).text());" data-code="subj"><u:msg titleId="cols.subj" alt="제목" /></dd>
					<dd class="line"></dd>
					<dd class="txt" onclick="setSelOptions('schCat',$(this).attr('data-code'),$(this).text());" data-code="cont"><u:msg titleId="cols.cont" alt="내용" /></dd>
					<dd class="line"></dd>
					<dd class="txt" onclick="setSelOptions('schCat',$(this).attr('data-code'),$(this).text());" data-code="regrNm"><u:msg titleId="cols.regr" alt="등록자" /></dd>
					<dd class="line"></dd>
					<dd class="txt" onclick="setSelOptions('schCat',$(this).attr('data-code'),$(this).text());" data-code="kwd"><u:msg titleId="dm.cols.kwd" alt="키워드" /></dd>
					<dd class="line"></dd>
					<dd class="txt" onclick="setSelOptions('schCat',$(this).attr('data-code'),$(this).text());" data-code="docNo"><u:msg titleId="dm.cols.docNo" alt="문서번호" /></dd>
					<dd class="line"></dd>
					<dd class="txt" onclick="setSelOptions('schCat',$(this).attr('data-code'),$(this).text());" data-code="ownrNm"><u:msg titleId="dm.cols.ownr" alt="소유자" /></dd>
				</dl>
				</div>
			</div>
		</div>
		
		<div class="select1">
			<div class="select_in1" onclick="holdHide = true; $('#schCatContainer #schCatOpen').toggle();">
			<dl>
				<dd class="select_txt1" id="selectView"><span><u:msg titleId="${empty param.schCat || param.schCat=='subj' ? 'cols.subj' : param.schCat=='cont' ? 'cols.cont' : param.schCat=='kwd' ? 'dm.cols.kwd' : param.schCat=='docNo' ? 'dm.cols.docNo' : param.schCat=='docNo' ? 'dm.cols.ownr' : 'cols.regr'}" alt="제목/내용/등록자/키워드/문서번호/소유자" /></span></dd>
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
</form>

<section>
<div class="listarea">
<article id="docListArea"><c:forEach items="${dmDocLVoList}" var="list" varStatus="status">
	<u:set test="${status.first}" var="listdiv" value="listdivline" elseValue="listdiv"/>
	<div class="${listdiv }" onclick="selectDoc(this);">
		<c:if test="${list.subYn eq 'Y' }">
		<div class="listcheck_comment"><dl><dd class="comment"></dd></dl></div>
        </c:if>
        <input type="hidden" name="docId" value="${list.docGrpId }"/>
		<div class="list${list.subYn eq 'Y' ? '_comment' : '' }">
		<dl>
		<dd class="tit">${list.subj}</dd>
		<dd class="body">${list.docNo}</dd>
		<dd class="name"><u:out value="${list.fldNm }"/>ㅣ <u:out value="${list.regrNm}"/> ㅣ <u:out value="${list.regDt}" type="date" 
			/></dd>
		
		</dl>
		</div>
	</div></c:forEach><c:if
		
		test="${fn:length(dmDocLVoList) == 0}">
		<div class="listdiv_nodata">
		<dl><dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd></dl>
		</div>
	</c:if>
</article>
</div>

<m:pagination />

<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
</section>
