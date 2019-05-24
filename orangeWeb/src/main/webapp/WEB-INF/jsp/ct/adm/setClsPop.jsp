<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<u:set test="${fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />
<c:set var="abvFldNm" value="ROOT" />

<div style="width:400px">
<form id="setClsForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="catPid" value="${ctCtFldBVo.catPid}" />
<u:input type="hidden" id="catId" value="${ctCtFldBVo.catId}" />
<u:input type="hidden" id="mode" value="${param.mode}" />
<% // 폼 필드 %>
<u:listArea>
	<tr>
		<td width="18%" class="head_lt">
			<u:msg titleId="wb.cols.abvFldNm" alt="상위폴더명" />
		</td>
		<td width="82%" class="body_lt">${ctCtFldBVo.catPidNm}</td>
	</tr>
	
	<tr>
		<td class="head_lt">
			<u:mandatory />
				<u:msg titleId="cols.clsNm" alt="분류명" />
		</td>
		<td id="catSubjRescArea">
			<table cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td id="langTypArea">
						<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:convert srcId="${ctCtFldBVo.catSubjRescId}_${langTypCdVo.cd}" var="rescVa" />
							<u:set test="${status.first}" var="style" value="" elseValue="display:none" />
							<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
							<u:set test="${fnc == 'mod'}" var="rescId" value="rescVa_" elseValue="catSubjRescNm_" />
							<u:set test="${fnc == 'mod'}" var="rescNm" value="rescVa_" elseValue="catSubjRescVa_" />
							<u:input id="${rescId}${langTypCdVo.cd}" name="${rescNm}${langTypCdVo.cd}"  titlePrefix="${titlePrefix}" titleId="cols.mnuGrpNm" value="${rescVa}" style="${style}"
								maxByte="200" validator="changeLangSelector('setFldPop', id, va)"
								 mandatory="Y" mandatorySkipper="skipMandatory('catSubjRescArea', 'catSubjRescNm_')" />
					</c:forEach>
					</td>
					<td>
						<c:if test="${fn:length(_langTypCdListByCompId)>1}">
							<select id="langSelector" onchange="changeLangTypCd('catSubjRescArea','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
							<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
							</c:forEach>
							</select>
						</c:if>
						<u:input type="hidden" id="rescId" value="${ctCtFldBVo.catSubjRescId}" />
					</td>
				</tr>
			</table>
		</td>
		
	</tr>
	
	<tr>
		<td class="head_lt"><u:msg titleId="cols.publYn" alt="공개여부" /></td>
		<td>
			<select id="extnOpenYn" name="extnOpenYn" >
				<option  value="Y" <c:if test="${ctCtFldBVo.extnOpenYn =='Y'}" > selected="selected" </c:if>> <u:msg titleId="ct.cols.open" alt="공개" /></option>
				<option  value="N" <c:if test="${ctCtFldBVo.extnOpenYn =='N'}" > selected="selected" </c:if>> <u:msg titleId="ct.cols.notOpen" alt="비공개" /></option>
			</select>
		</td>
	</tr>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save"  href="javascript:saveCls();" alt="저장" auth="W" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</form>
</div>
