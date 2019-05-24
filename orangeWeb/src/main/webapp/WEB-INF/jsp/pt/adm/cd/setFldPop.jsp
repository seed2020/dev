<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<div style="width:450px">
<form id="setFldPop">
<input type="hidden" name="menuId" value="${menuId}" />
<u:input id="mode" type="hidden" value="${param.mode}" />
<u:input id="clsCd" type="hidden" value="${ptCdBVo.clsCd}" />
<u:listArea>

	<tr>
	<td width="35%" class="head_lt"><u:mandatory /><u:msg titleId="pt.cols.fldCd" alt="폴더코드" /></td>
	<td><u:set
		test="${param.mode=='mod'}" var="readonly" value="Y" elseValue="N"
		/><u:input id="cd" titleId="pt.cols.fldCd" value="${ptCdBVo.cd}" maxByte="30" mandatory="Y"
			valueOption="alpha,number" valueAllowed="_" readonly="${readonly}" /></td>
	</tr>
	<tr>
	<td width="35%" class="head_lt"><u:mandatory /><u:msg titleId="pt.cols.fldNm" alt="폴더명" /></td>
	<td>
		<table cellspacing="0" cellpadding="0" border="0">
		<tr>
		<td id="langTypArea">
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
		<u:convert srcId="${ptCdBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
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
		<u:input type="hidden" id="rescId" value="${ptCdBVo.rescId}" />
		</td>
		</tr>
		</table>
	</td>
	</tr>
	<tr>
	<td width="35%" class="head_lt"><u:msg titleId="cols.refVa1" alt="참조값1" /></td>
	<td><u:input id="refVa1" value="${ptCdBVo.refVa1}" titleId="cols.refVa1" maxByte="400" /></td>
	</tr>
	<tr>
	<td width="35%" class="head_lt"><u:msg titleId="pt.jsp.setCd.usageChkTab" alt="사용여부 체크 테이블" /></td>
	<td><u:input id="refVa2" value="${ptCdBVo.refVa2}" titleId="pt.jsp.setCd.usageChkTab" maxByte="400" style="width:94%" /></td>
	</tr>
	<tr>
	<td width="35%" class="head_lt"><u:msg titleId="pt.cols.Desc" alt="설명" /></td>
	<td><u:textarea id="cdDesc" titleId="pt.cols.Desc" value="${ptCdBVo.cdDesc}" maxByte="400" rows="3" style="width:94%" /></td>
	</tr>
	
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" href="javascript:saveFld();" alt="저장" auth="A" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</form>
</div>