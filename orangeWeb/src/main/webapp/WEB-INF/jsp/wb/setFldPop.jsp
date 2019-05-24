<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%// [팝업:폴더등록, 폴더수정] - 저장 버튼 %>
function saveFld(){
	if(validator.validate('setFldForm')){
		var $frm = $('#setFldForm');
		$frm.attr('action','./transFld.do');
		$frm.attr('target','dataframe');
		$frm.submit();
	}
};
//-->
</script>

<div style="width:400px">
<form id="setFldForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="abvFldId" value="${wbBcFldBVo.abvFldId}" />
<u:input type="hidden" id="bcFldId" value="${wbBcFldBVo.bcFldId}" />
<u:input type="hidden" id="mode" value="${param.mode}" />
<c:if test="${!empty param.schBcRegrUid }">
<u:input type="hidden" id="schBcRegrUid" value="${param.schBcRegrUid}" />
</c:if>
<% // 폼 필드 %>
<u:listArea>
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="wb.cols.abvFldNm" alt="상위폴더명" /></td>
	<td width="82%" class="body_lt"><c:if test="${empty wbBcFldBVo.abvFldId || wbBcFldBVo.abvFldId eq 'ROOT' || wbBcFldBVo.abvFldId eq 'NONE' }"><u:msg titleId="cm.option.all" alt="전체" /></c:if>
	<c:if test="${!empty wbBcFldBVo.abvFldId && wbBcFldBVo.abvFldId ne 'ROOT' && wbBcFldBVo.abvFldId ne 'NONE' }">${wbBcFldBVo.abvFldNm }</c:if></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.fldNm" alt="폴더명" /></td>
	
	<c:if test="${empty isPub || isPub == false }">
		<td><u:input id="fldNm" name="fldNm" value="${wbBcFldBVo.fldNm }" titleId="cols.fldNm" style="width: 310px;" maxByte="120"/></td>
	</c:if>
	<c:if test="${!empty isPub && isPub == true }">
	<td>
		<table border="0" cellpadding="0" cellspacing="0">
			<tbody>
			<tr>
				<td id="langTypArea">
					<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
						<u:convert srcId="${wbBcFldBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
						<u:set test="${status.first}" var="style" value="width:200px;" elseValue="width:200px; display:none" />
						<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
						<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.fldNm" value="${rescVa}" style="${style}"
							maxByte="120" validator="changeLangSelector('setFldForm', id, va)" mandatory="Y" />
					</c:forEach>
					<u:input type="hidden" id="rescId" value="${wbBcFldBVo.rescId}" />
				</td>
				<td id="langTypOptions">
					<c:if test="${fn:length(_langTypCdListByCompId)>1}">
						<select id="langSelector" onchange="changeLangTypCd('setFldForm','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
						<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
						<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
						</c:forEach>
						</select>
					</c:if>
				</td>
			</tr>
			</tbody>
		</table>
	</td>
		</c:if>
	</tr>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" href="javascript:saveFld();" alt="저장" auth="W" />
	<u:button href="javascript:void(0)" onclick="dialog.close(this);" alt="닫기" titleId="cm.btn.close" />
</u:buttonArea>

</form>
</div>
