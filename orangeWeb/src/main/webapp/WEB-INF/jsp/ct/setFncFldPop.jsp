<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />
<c:set var="abvFldNm" value="ROOT" />
<u:set test="${fnc == 'mod'}" var="fldNm" value="운영자게시판" elseValue="" />

<div style="width:400px">
<form id="setFldForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="ctId" value="${ctCtFldBVo.ctId}" />
<u:input type="hidden" id="ctFncId" value="${ctCtFldBVo.ctFncId}" />
<u:input type="hidden" id="ctFncUid" value="${ctCtFldBVo.ctFncUid}" />
<u:input type="hidden" id="mode" value="${param.mode}" />
<u:input type="hidden" id="fnc" value="${param.fnc}" />
<u:input type="hidden" id="catId" value="${param.catId}" />

<% // 폼 필드 %>
<u:listArea>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="wb.cols.abvFldNm" alt="상위폴더명" /></td>
		<td width="82%" class="body_lt">${abvFldNm}</td>
	</tr>
	
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg titleId="cols.fldNm" alt="폴더명" /></td>
		<td id="ctFncSubjRescArea">
			<table cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td id="langTypArea">
						<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:convert srcId="${ctFncDVo.ctFncSubjRescId}_${langTypCdVo.cd}" var="rescVa" />
							<u:set test="${status.first}" var="style" value="" elseValue="display:none" />
							<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
							<u:set test="${fnc == 'mod'}" var="rescId" value="rescVa_" elseValue="ctFncSubjRescNm_" />
							<u:set test="${fnc == 'mod'}" var="rescNm" value="rescVa_" elseValue="ctFncSubjRescVa_" />
							<u:input id="${rescId}${langTypCdVo.cd}" name="${rescNm}${langTypCdVo.cd}"  titlePrefix="${titlePrefix}" titleId="cols.mnuGrpNm" value="${rescVa}" style="${style}"
								maxByte="200" validator="changeLangSelector('setFldPop', id, va)"
								 mandatory="Y" />
					</c:forEach>
					</td>
					<td>
						<c:if test="${fn:length(_langTypCdListByCompId)>1}">
							<select id="langSelector" onchange="changeLangTypCd('ctFncSubjRescArea','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
							<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
							</c:forEach>
							</select>
						</c:if>
						<u:input type="hidden" id="rescId" value="${ctFncDVo.ctFncSubjRescId}" />
					</td>
				</tr>
			</table>
		</td>
	</tr>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="javascript:saveFld();" alt="저장" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</form>
</div>
