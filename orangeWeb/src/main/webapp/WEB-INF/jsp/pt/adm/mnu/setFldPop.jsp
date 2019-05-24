<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<div style="width:400px">
<form id="setFldPop">
<input type="hidden" name="menuId" value="${menuId}" />
<u:input id="mode" type="hidden" value="${param.mode}" />
<u:input id="mnuId" type="hidden" value="${param.mnuId}" /><c:if
	test="${empty param.mnuId}">
<u:input id="mnuPid" type="hidden" value="${param.mnuPid}" />
<u:input id="mnuGrpId" type="hidden" value="${param.mnuGrpId}" /></c:if><c:if
	test="${not empty param.mnuGrpMdCd}">
<u:input id="mnuGrpMdCd" type="hidden" value="${param.mnuGrpMdCd}" /></c:if>

<u:listArea>

	<tr>
	<td width="30%" class="head_lt"><u:mandatory /><u:msg titleId="pt.cols.fldNm" alt="폴더명" /></td>
	<td>
		<table cellspacing="0" cellpadding="0" border="0">
		<tr>
		<td id="langTypArea">
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
		<u:convert srcId="${ptMnuDVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
		<u:set test="${status.first}" var="style" value="" elseValue="display:none" />
		<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
		<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="pt.cols.fldNm" value="${rescVa}" style="${style}"
			maxByte="200" validator="changeLangSelector('setFldPop', id, va)" mandatory="Y" onkeydown="if (event.keyCode == 13) return false;"/>
		</c:forEach>
		</td>
		<td>
		<c:if test="${fn:length(_langTypCdListByCompId)>1}">
			<select id="langSelector" onchange="changeLangTypCd('setFldPop','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
			</c:forEach>
			</select>
		</c:if>
		<u:input type="hidden" id="rescId" value="${ptMnuDVo.rescId}" />
		</td>
		</tr>
		</table>
	</td>
	</tr>
	<tr>
	<td width="30%" class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
	<td><select id="useYn${cdIndex}" name="useYn" <u:elemTitle titleId="cols.useYn" />>
		<u:option value="Y" titleId="cm.option.use" checkValue="${ptMnuDVo.useYn}" />
		<u:option value="N" titleId="cm.option.notUse" checkValue="${ptMnuDVo.useYn}" />
		</select></td>
	</tr>
	
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" href="javascript:saveFld();" alt="저장" auth="A" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</form>
</div>