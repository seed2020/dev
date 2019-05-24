<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<div style="width:350px">
<form id="setFldPop">
<input type="hidden" name="menuId" value="${menuId}" />
<u:input id="mode" type="hidden" value="${param.mode}" />
<u:listArea>

	<tr>
	<td width="35%" class="head_lt"><u:mandatory /><u:msg titleId="pt.cols.fldNm" alt="폴더명" /></td>
	<td>
		<table cellspacing="0" cellpadding="0" border="0">
		<tr>
		<td id="langTypArea">
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
		<u:convert srcId="${param.rescId}_${langTypCdVo.cd}" var="rescVa" />
		<u:set test="${status.first}" var="style" value="" elseValue="display:none" />
		<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
		<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="pt.cols.fldNm" value="${rescVa}" style="${style}"
			maxByte="200" validator="changeLangSelector('setFldPop', id, va)" mandatory="Y" />
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
		<u:input type="hidden" id="rescId" value="${param.rescId}" />
		</td>
		</tr>
		</table>
	</td>
	</tr>
	
</u:listArea>

<u:buttonArea>
	<u:button alt="확인" href="javascript:${confirmBtnScript}('${param.mode}');" titleId="cm.btn.confirm" auth="W" />
	<u:button alt="취소" onclick="dialog.close(this);" titleId="cm.btn.cancel" />
</u:buttonArea>

</form>
</div>