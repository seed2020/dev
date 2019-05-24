<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<div style="width:400px">
<form id="setAuthGrpPop">
<input type="hidden" name="menuId" value="${menuId}" />
<u:input id="compId" type="hidden" value="${compId}" />
<u:input id="authGrpTypCd" type="hidden" value="${param.authGrpTypCd}" />
<u:input id="authGrpId" type="hidden" value="${ptAuthGrpBVo.authGrpId}" />
<u:listArea>

	<tr>
		<td width="30%" class="head_lt"><u:mandatory /><u:msg titleId="cols.authGrpNm" alt="권한그룹명" /></td>
		<td>
			<table cellspacing="0" cellpadding="0" border="0">
			<tr>
			<td id="langTypArea">
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:convert srcId="${ptAuthGrpBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
			<u:set test="${status.first}" var="style" value="" elseValue="display:none" />
			<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
			<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.authGrpNm" value="${rescVa}" style="${style}"
				maxByte="200" validator="changeLangSelector('setAuthGrpPop', id, va)" mandatory="Y" />
			</c:forEach>
			</td>
			<td>
			<c:if test="${fn:length(_langTypCdListByCompId)>1}">
				<select id="langSelector" onchange="changeLangTypCd('setAuthGrpPop','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
				<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
				<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
				</c:forEach>
				</select>
			</c:if>
			<u:input type="hidden" id="rescId" value="${ptAuthGrpBVo.rescId}" />
			</td>
			</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td width="30%" class="head_lt"><u:msg titleId="cols.authGrpCat" alt="권한그룹카테고리" /></td>
		<td><select id="authGrpCatCd" name="authGrpCatCd"<u:elemTitle titleId="cols.authGrpCat" /> >
			<c:forEach items="${authGrpCatCdList}" var="authGrpCatCd" varStatus="status">
			<u:option value="${authGrpCatCd.cd}" title="${authGrpCatCd.rescNm}" selected="${param.authGrpCatCd == authGrpCatCd.cd || ptAuthGrpBVo.authGrpCatCd == authGrpCatCd.cd}" />
			</c:forEach>
			</select></td>
	</tr>
	<c:if test="${not empty param.authGrpId}">
	<tr>
		<td width="30%" class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
		<td><select id="useYn" name="useYn" <u:elemTitle titleId="cols.useYn" />>
			<u:option value="Y" titleId="cm.option.use" checkValue="${ptAuthGrpBVo.useYn}" />
			<u:option value="N" titleId="cm.option.notUse" checkValue="${ptAuthGrpBVo.useYn}" />
			</select>
		</td>
	</tr>
	</c:if>
	<tr>
		<td width="30%" class="head_lt"><u:msg titleId="cols.authGrpDesc" alt="권한그룹설명" /></td>
		<td><u:textarea id="authGrpDesc" value="${ptAuthGrpBVo.authGrpDesc}" titleId="cols.authGrpDesc" maxByte="400" rows="3" style="width:94%" /></td>
	</tr>

</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" href="javascript:saveAuthGrp();" alt="저장" auth="A" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</form>
</div>