<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function savePhoto(){
	if(validator.validate('addImageForm')){
		var $form = $("#addImageForm");
		$form.attr('action','./transOrgImage.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form.submit();
	}
}
$(document).ready(function() {
	if(browser.ie && browser.ver<9){
		$("#dataframe").attr("src","${_cxPth}/cm/util/reloadable.do");
	}
});
//-->
</script>
<div style="width:400px">
<form id="addImageForm" method="post" enctype="multipart/form-data">

<u:listArea>

	<c:if test="${empty param.seq}">
	<tr>
	<th width="30%" class="head_lt" style="font-weight:normal; text-align:left;"><u:msg titleId="cols.dept" alt="부서" /></th>
	<td width="70%"><select id="orgId" name="orgId"<u:elemTitle titleId="cols.dept" alt="부서" />><c:forEach
		items="${orgTreeVoList}" var="orgTreeVo" varStatus="status">
		<u:option title="${orgTreeVo.rescNm}" value="${orgTreeVo.orgId}" selected="${param.orgId == orgTreeVo.orgId or (empty param.orgId and status.last)}" /></c:forEach>
		</select></td>
	</tr>
	
	<tr>
	<th class="head_lt" style="font-weight:normal; text-align:left;"><u:msg titleId="or.cols.imgTyp" alt="이미지 구분" /></th>
	<td ><select id="ofseTypCd" name="ofseTypCd"<u:elemTitle titleId="or.cols.imgTyp" alt="이미지 구분" />><c:forEach
		items="${ofseTypCdList}" var="ofseTypCd" varStatus="status">
		<u:option title="${ofseTypCd.rescNm}" value="${ofseTypCd.cd}" selected="${param.ofseTypCd == ofseTypCd.cd}" /></c:forEach>
		</select></td>
	</tr>
	</c:if><c:if
		test="${not empty param.seq}">
	<tr>
	<th width="30%" class="head_lt" style="font-weight:normal; text-align:left;"><u:msg titleId="cols.dept" alt="부서" /></th>
	<td class="body_lt" width="70%"><c:forEach
		items="${orgTreeVoList}" var="orgTreeVo" varStatus="status"><c:if
		test="${param.orgId == orgTreeVo.orgId}"><u:out value="${orgTreeVo.rescNm}" /></c:if></c:forEach></td>
	</tr>
	
	<tr>
	<th class="head_lt" style="font-weight:normal; text-align:left;"><u:msg titleId="or.cols.imgTyp" alt="이미지 구분" /></th>
	<td class="body_lt"><u:out value="${orOfseDVo.ofseTypNm}" /></td>
	</tr>
	<input type="hidden" name="orgId" value="${param.orgId}" /><input type="hidden" name="seq" value="${param.seq}" />
	</c:if>

	<tr>
	<td width="30%" class="head_lt" style="font-weight:normal; text-align:left;"><u:mandatory /><u:msg titleId="or.cols.imgNm" alt="이미지 명" /></td>
	<td>
		<table cellspacing="0" cellpadding="0" border="0">
		<tr>
		<td id="langTypArea">
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
		<u:convert srcId="${orOfseDVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
		<u:set test="${status.first}" var="style" value="" elseValue="display:none" />
		<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
		<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="or.cols.imgNm" value="${rescVa}" style="${style}"
			maxByte="200" validator="changeLangSelector('addImageForm', id, va)" mandatory="Y" />
		</c:forEach>
		</td>
		<td>
		<c:if test="${fn:length(_langTypCdListByCompId)>1}">
			<select id="langSelector" onchange="changeLangTypCd('addImageForm','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
			</c:forEach>
			</select>
		</c:if>
		<u:input type="hidden" id="rescId" value="${orOfseDVo.rescId}" />
		</td>
		</tr>
		</table>
	</td>
	</tr>

	<tr>
	<th width="30%" class="head_lt" style="font-weight:normal; text-align:left;"><u:msg titleId="or.cols.dftImgYn" alt="기본 이미지 여부" /></th>
	<td width="70%"><u:checkArea>
		<u:radio value="Y" name="dftOfseYn" titleId="cm.btn.setup" alt="설정" checked="${orOfseDVo.dftOfseYn == 'Y'}" />
		<u:radio value="N" name="dftOfseYn" titleId="cm.btn.notSetup" alt="설정안함" checked="${orOfseDVo.dftOfseYn != 'Y'}" />
	</u:checkArea></td>
	</tr>

	<tr>
	<th width="30%" class="head_lt" style="font-weight:normal; text-align:left;"><c:if test="${empty param.seq}"><u:mandatory /></c:if><u:msg alt="이미지 선택" titleId="or.btn.selImg" /></th>
	<td width="70%"><u:file id="image" titleId="or.btn.selImg" alt="이미지 선택"
		mandatory="${empty param.seq ? 'Y' : 'N'}" exts="gif,jpg,jpeg,png,tif" /></td>
	</tr>
	
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" href="javascript:savePhoto();" alt="저장" auth="A" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>