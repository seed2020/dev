<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="paramsForList" excludes="rescMngId"/>
<script type="text/javascript">
<!--//사진보기
function viewWrImageDetl(rescMngId){
	dialog.open('viewImageDialog1', '<u:msg titleId="or.jsp.setOrg.viewImageTitle" alt="이미지 보기" />', './viewImagePop.do?menuId=${menuId}&rescMngId='+rescMngId);
};
//-->
</script>
<div style="width:650px">
	<% // 표 %>
<div class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
	<colgroup><col width="22%"/><col width="*"/></colgroup>
	<tr>
		<td class="head_lt"><u:msg titleId="cols.rescKnd" alt="자원종류" /></td>
		<td class="body_lt">${wrRescMngBVo.kndNm }</td>
	</tr>

	<tr>
		<td class="head_lt"><u:msg titleId="cols.rescNm" alt="자원명" /></td>
		<td class="body_lt"><div class="ellipsis" title="${wrRescMngBVo.rescNm }" >${wrRescMngBVo.rescNm }</div></td>
	</tr>

	<tr>
		<td class="head_lt"><u:msg titleId="cols.rescLoc" alt="자원위치" /></td>
		<td class="body_lt"><div class="ellipsis" title="${wrRescMngBVo.rescLoc }" >${wrRescMngBVo.rescLoc }</div></td>
	</tr>

	<tr>
		<td class="head_lt"><u:msg titleId="cols.suplStat" alt="비품현황" /></td>
		<td class="body_lt"><div style="overflow:auto;height:50px;">${wrRescMngBVo.suplStat }</div></td>
	</tr>

	<tr>
		<td class="head_lt"><u:msg titleId="cols.rescAdm" alt="자원관리자" /></td>
		<td class="body_lt"><a href="javascript:viewUserPop('${wrRescMngBVo.rescAdmUid }');">${wrRescMngBVo.rescAdmNm }</a></td>
	</tr>

	<tr>
		<td class="head_lt"><u:msg titleId="wr.cols.discYn" alt="심의여부" /></td>
		<td class="body_lt"><u:msg titleId="wr.option.disc${wrRescMngBVo.discYn }" alt="사용여부"/></td>
	</tr>

	
	<tr>
		<td class="head_lt"><u:msg titleId="cols.imgFile" alt="이미지파일" /></td>
		<td class="body_lt">
			<c:set var="wrRescImgDVo" value="${wrRescMngBVo.wrRescImgDVo}" />
			<c:set var="maxHght" value="125" />

			<u:set test="${wrRescImgDVo == null || wrRescImgDVo.imgHght > maxHght}" var="imgHght" value="${maxHght}" elseValue="${wrRescImgDVo.imgHght}" />
			<c:if test="${wrRescImgDVo.imgWdth > 800}"	>
				<a href="${_cxPth}${wrRescImgDVo.imgPath}" target="viewPhotoWin"><img src="${_cxPth}${wrRescImgDVo.imgPath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' height="${imgHght }px"></a>
			</c:if>
			<c:if test="${wrRescImgDVo.imgWdth <= 800}">
				<a href="javascript:viewWrImageDetl('${wrRescImgDVo.rescMngId}');"><img src="${_cxPth}${wrRescImgDVo.imgPath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' height="${imgHght }px"></a>
			</c:if>
		</td>
	</tr>
	
	<tr>
		<td class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
		<td class="body_lt"><u:msg titleId="cm.option.${wrRescMngBVo.useYn eq 'Y' ? 'use' : 'notUse' }" alt="사용여부"/></td>
	</tr>
	</table>
</div>
<u:blank />
	<% // 하단 버튼 %>
	<u:buttonArea>
		<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" auth="R" />
		<u:button titleId="cm.btn.mod" alt="수정" onclick="setRescPop('${wrRescMngBVo.rescMngId }','${paramsForList }');" auth="A" />
		<u:button titleId="cm.btn.del" alt="삭제" href="javascript:;" onclick="fnRescDelete('${wrRescMngBVo.rescMngId }');" auth="A" />
		<u:button onclick="dialog.close(this);" titleId="cm.btn.close" alt="닫기" auth="R" />
	</u:buttonArea>
<u:blank />	
	<form id="deleteForm" name="deleteForm" action="./transRescMngDel.do">
		<u:input type="hidden" name="menuId" value="${menuId}" />
		<u:input type="hidden" name="typ" value="${param.typ}" />
		<u:input type="hidden" name="rescMngId"  id="rescMngId"/>
		<u:input type="hidden" id="listPage" value="./listRescMngFrm.do?${paramsForList}" />
	</form>
</div>
