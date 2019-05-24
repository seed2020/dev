<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--
<%// 이미지클릭 - 라디오체크 되도록%>
function checkRadio(id){
	$('#'+id).trigger('click');
	$('#'+id).uniform.update();
}
<%// 이미지클릭 - 라디오체크 되도록%>
function goNext(){
	if($("#step1 input[name='pltLoutCd']:checked").length==0){
		alertMsg('pt.jsp.setPltStep1.choose.layout')<%//pt.jsp.setPltStep1.choose.layout=레이아웃을 선택해 주십시요.%>
	} else {
		var $form = $("#step1");
		$form.attr('action','./setPltStep2.do');
		$form.submit();
	}
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>

<u:title titleId="pt.jsp.setPltStep1.title" alt="포틀릿 설정 1단계" />

<u:boxArea className="gbox" style="min-width:1005px;" outerStyle="height:457px;padding:5px 0 0 10px;" innerStyle="NO_INNER_IDV">
<form id="step1">
<input type="hidden" name="menuId" value="${menuId}" /><c:if test="${not empty param.mnuGrpId}">
<input type="hidden" name="mnuGrpId" value="${param.mnuGrpId}" /></c:if><c:if test="${not empty param.backTo}">
<input type="hidden" name="backTo" value="${param.backTo}" /></c:if><c:if test="${not empty param.useMnu}">
<input type="hidden" name="useMnu" value="${param.useMnu}" /></c:if><c:if test="${empty param.backTo}">
<input type="hidden" name="usePlt" value="Y" /></c:if>
		<!--1단 S-->
		<div class="titlearea">
			<div class="tit_left">
			<dl>
			<dd class="title_s"><u:msg titleId="pt.jsp.setPltStep1.1s" alt="1단" /></dd>
			</dl>
			</div>
		</div>
		<div class="pagecolumnarea">
			<div class="pagecolumn">
			<dl>
			<dd class="radio"><u:radio name="pltLoutCd" value="FREE" checkValue="${pltLoutCd}"/></dd>
			<dd class="img"><a href="javascript:;" onclick="checkRadio('pltLoutCdFREE')"><img src="${_cxPth}/images/${_skin}/column.png" width="138" height="78" /></a></dd>
			</dl>
			</div>
		</div>
		
		<u:blank />

		<!--2단 S-->
		<div class="titlearea">
			<div class="tit_left">
			<dl>
			<dd class="title_s"><u:msg titleId="pt.jsp.setPltStep1.2s" alt="2단" /></dd>
			</dl>
			</div>
		</div>
		<div class="pagecolumnarea">
			<div class="pagecolumn">
			<dl>
			<dd class="radio"><u:radio name="pltLoutCd" value="D2R37" checkValue="${pltLoutCd}"/></dd>
			<dd class="img"><a href="javascript:;" onclick="checkRadio('pltLoutCdD2R37')"><img src="${_cxPth}/images/${_skin}/column2a.png" width="138" height="78" /></a></dd>
			</dl>
			</div>
			
			<div class="pagecolumn">
			<dl>
			<dd class="radio"><u:radio name="pltLoutCd" value="D2R46" checkValue="${pltLoutCd}"/></dd>
			<dd class="img"><a href="javascript:;" onclick="checkRadio('pltLoutCdD2R46')"><img src="${_cxPth}/images/${_skin}/column2b.png" width="138" height="78" /></a></dd>
			</dl>
			</div>
			
			<div class="pagecolumn">
			<dl>
			<dd class="radio"><u:radio name="pltLoutCd" value="D2R55" checkValue="${pltLoutCd}"/></dd>
			<dd class="img"><a href="javascript:;" onclick="checkRadio('pltLoutCdD2R55')"><img src="${_cxPth}/images/${_skin}/column2c.png" width="138" height="78" /></a></dd>
			</dl>
			</div>
			
			<div class="pagecolumn">
			<dl>
			<dd class="radio"><u:radio name="pltLoutCd" value="D2R64" checkValue="${pltLoutCd}"/></dd>
			<dd class="img"><a href="javascript:;" onclick="checkRadio('pltLoutCdD2R64')"><img src="${_cxPth}/images/${_skin}/column2e.png" width="138" height="78" /></a></dd>
			</dl>
			</div>
			
			<div class="pagecolumn">
			<dl>
			<dd class="radio"><u:radio name="pltLoutCd" value="D2R73" checkValue="${pltLoutCd}"/></dd>
			<dd class="img"><a href="javascript:;" onclick="checkRadio('pltLoutCdD2R73')"><img src="${_cxPth}/images/${_skin}/column2d.png" width="138" height="78" /></a></dd>
			</dl>
			</div>
		</div>

		<u:blank />

		<!--3단 S-->
		<div class="titlearea">
			<div class="tit_left">
			<dl>
			<dd class="title_s"><u:msg titleId="pt.jsp.setPltStep1.3s" alt="3단" /></dd>
			</dl>
			</div>
		</div>
		<div class="pagecolumnarea">
			<div class="pagecolumn">
			<dl>
			<dd class="radio"><u:radio name="pltLoutCd" value="D3R111" checkValue="${pltLoutCd}"/></dd>
			<dd class="img"><a href="javascript:;" onclick="checkRadio('pltLoutCdD3R111')"><img src="${_cxPth}/images/${_skin}/column3a.png" width="138" height="78" /></a></dd>
			</dl>
			</div>
			
			<div class="pagecolumn">
			<dl>
			<dd class="radio"><u:radio name="pltLoutCd" value="D3R112" checkValue="${pltLoutCd}"/></dd>
			<dd class="img"><a href="javascript:;" onclick="checkRadio('pltLoutCdD3R112')"><img src="${_cxPth}/images/${_skin}/column3b.png" width="138" height="78" /></a></dd>
			</dl>
			</div>
			
			<div class="pagecolumn">
			<dl>
			<dd class="radio"><u:radio name="pltLoutCd" value="D3R121" checkValue="${pltLoutCd}"/></dd>
			<dd class="img"><a href="javascript:;" onclick="checkRadio('pltLoutCdD3R121')"><img src="${_cxPth}/images/${_skin}/column3c.png" width="138" height="78" /></a></dd>
			</dl>
			</div>
			
			<div class="pagecolumn">
			<dl>
			<dd class="radio"><u:radio name="pltLoutCd" value="D3R211" checkValue="${pltLoutCd}"/></dd>
			<dd class="img"><a href="javascript:;" onclick="checkRadio('pltLoutCdD3R211')"><img src="${_cxPth}/images/${_skin}/column3d.png" width="138" height="78" /></a></dd>
			</dl>
			</div>
			
			<div class="pagecolumn">
			<dl>
			<dd class="radio"><u:radio name="pltLoutCd" value="D3R221" checkValue="${pltLoutCd}"/></dd>
			<dd class="img"><a href="javascript:;" onclick="checkRadio('pltLoutCdD3R221')"><img src="${_cxPth}/images/${_skin}/column3e.png" width="138" height="78" /></a></dd>
			</dl>
			</div>
			
			<div class="pagecolumn">
			<dl>
			<dd class="radio"><u:radio name="pltLoutCd" value="D3R212" checkValue="${pltLoutCd}"/></dd>
			<dd class="img"><a href="javascript:;" onclick="checkRadio('pltLoutCdD3R212')"><img src="${_cxPth}/images/${_skin}/column3f.png" width="138" height="78" /></a></dd>
			</dl>
			</div>
		</div>
</form>
</u:boxArea>

<u:buttonArea><c:if
		test="${not empty param.mnuGrpId}">
	<u:button href="javascript:goNext();" titleId="cm.btn.next" alt="다음" auth="A" />
	<u:button href="javascript:history.go(-1);" titleId="cm.btn.cancel" alt="취소" />	</c:if><c:if
		test="${empty param.mnuGrpId}">
	<u:button href="javascript:goNext()" titleId="cm.btn.next" alt="다음" auth="W" />
	<u:button href="/pt/psn/my/setMyMnu.do?menuId=${menuId}" titleId="cm.btn.cancel" alt="취소" /></c:if>
</u:buttonArea>