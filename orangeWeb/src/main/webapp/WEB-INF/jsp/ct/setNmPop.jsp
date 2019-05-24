<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>


<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />
<c:set var="curNm" value="게시판" />
<u:set test="${fnc == 'mod'}" var="chnNm" value="자유게시판" elseValue="" />

<div style="width:400px">
<form id="setNmForm">
<u:input type="hidden" id="menuId" value="${menuId}" />

<% // 폼 필드 %>
<u:listArea>
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="ct.cols.curNm" alt="현재이름" /></td>
	<td id="ctFncPrevNmArea">
		<table cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td id="langTypArea">
						<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:convert srcId="${fncRescId}_${langTypCdVo.cd}" var="rescVa" />
							<u:set test="${status.first}" var="style" value="" elseValue="display:none" />
							<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
							
							<u:input id="${langTypCdVo.cd}" name="${langTypCdVo.cd}"  titlePrefix="${titlePrefix}" titleId="cols.mnuGrpNm" value="${rescVa}" style="${style}"
								maxByte="200" validator="changeLangSelector('setNmPop', id, va)" readonly="readonly" disabled="disabled"
								 />
					</c:forEach>
					</td>
					<td>
						<c:if test="${fn:length(_langTypCdListByCompId)>1}">
							<select id="langSelector" onchange="changeLangTypCd('ctFncPrevNmArea','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
							<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
							</c:forEach>
							</select>
						</c:if>
					</td>
				</tr>
			</table>
	</td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="ct.cols.chnNm" alt="변경이름" /></td>
	<td id="ctFncSubjRescArea">
			<table cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td id="langTypArea">
						<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:convert srcId="${fncRescId}_${langTypCdVo.cd}" var="rescVa" />
							<u:set test="${status.first}" var="style" value="" elseValue="display:none" />
							<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
							
							<u:input id="rescVa_${langTypCdVo.cd}" name="rescVa_${langTypCdVo.cd}"  titlePrefix="${titlePrefix}" titleId="cols.mnuGrpNm" value="${rescVa}" style="${style}"
								maxByte="200" validator="changeLangSelector('setNmPop', id, va)"
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
						<u:input type="hidden" id="rescId" value="${fncRescId}" />
					</td>
				</tr>
			</table>
		</td>
	</tr>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="javascript:saveReNm();" alt="저장" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</form>
</div>
