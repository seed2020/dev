<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
function mulChoichk(serv){
	//수정일때만 서비스 /기능 일때 이벤트 발생시켜야할지? 전부다 해야할지?
	//var fnc = "${fnc}";
	//if(fnc == 'mod'){
		if(serv == 'Y'){
			$("#mulChoiArea").hide();
			$(':radio[name="mulChoiYn"]').val('N');
		}else{
			$("#mulChoiArea").show();
			//$(':radio[name="mulChoiYn"]:input[value="N"]').parent().attr("class", "");
			//$(':radio[name="mulChoiYn"]:input[value="Y"]').parent().attr("class", "checked");
			//$(this).removeAttr("checked");
		}
	//}
}

function mulchk(serv){
	$(':radio[name="mulChoiYn"]').val(serv);
}

$(document).ready(function() {
	setUniformCSS();
	mulChoichk("${ctFncMngBVo.ctFncTyp}");
	
});
-->

</script>

<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />
<u:set test="${fnc == 'mod'}" var="fncNm" value="게시판" elseValue="" />
<u:set test="${fnc == 'mod'}" var="url" value="${ctFncMngBVo.url}" elseValue="" />
<u:set test="${fnc == 'mod'}" var="ptUrl" value="${ctFncMngBVo.ptUrl}" elseValue="" />
<u:set test="${fnc == 'mod'}" var="relTblSubj" value="${ctFncMngBVo.relTblSubj}" elseValue="" />


<div style="width:700px">
<form id="setFncForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="ctFncId" value="${ctFncId}" />
<u:input type="hidden" id="fnc" value="${fnc}" />

<% // 폼 필드 %>
<u:listArea>
<!-- 
	<tr>
	<td width="27%" class="head_lt"><u:mandatory /><u:msg titleId="cols.svcMnuYn" alt="서비스메뉴여부" /></td>
	<td width="73%"><u:checkArea>
		<c:if test="${fnc == 'mod'}">
			<u:set test="${ctFncMngBVo.ctFncTyp == 'Y'}" var="svcMnuY" value="true" elseValue="false" />
			<u:set test="${ctFncMngBVo.ctFncTyp == 'N'}" var="svcMnuN" value="true" elseValue="false" />
		</c:if>
		<c:if test="${fnc == 'reg'}">
			<c:set var="svcMnuN" value="true"></c:set>
		</c:if>
		<u:radio name="svcMnuYn" value="Y" titleId="ct.option.svc" alt="서비스" checked="${svcMnuY}" onclick="mulChoichk('Y')" inputClass="bodybg_lt" />
		<u:radio name="svcMnuYn" value="N" titleId="ct.option.fnc" alt="기능" checked="${svcMnuN}" onclick="mulChoichk('N')" inputClass="bodybg_lt" />
		</u:checkArea></td>
	</tr>
 -->
 <%-- 	<u:secu auth="SYS">
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.comp" alt="회사" /></td>
	<td>
		<select id="compId" name="compId" <u:elemTitle titleId="cols.comp" /> <c:if test="${!empty ctFncMngBVo.ctFncId }">disabled="disabled"</c:if>>
			<c:forEach items="${ptCompBVoList}" var="ptCompBVo" varStatus="status">
				<u:option value="${ptCompBVo.compId}" title="${ptCompBVo.rescNm}" checkValue="${ctFncMngBVo.compId}"/>
			</c:forEach>
		</select>
	</td>
	</tr>
	</u:secu>	 --%>
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.fncNm" alt="기능명" /></td>
	<td id="fncSubjRescArea">
	<table border="0" cellpadding="0" cellspacing="0">
		<tbody>
			<tr>
				<td id="langTypArea">
					<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
						<u:convert srcId="${ctFncMngBVo.ctFncSubjRescId}_${langTypCdVo.cd}" var="rescVa" />
						<u:set test="${status.first}" var="style" value="" elseValue="display:none" />
						<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
						<u:set test="${fnc == 'mod'}" var="rescId" value="rescVa_" elseValue="fncSubjRescNm_" />
						<u:set test="${fnc == 'mod'}" var="rescNm" value="rescVa_" elseValue="fncSubjRescVa_" />
						<u:input id="${rescId}${langTypCdVo.cd}" name="${rescNm}${langTypCdVo.cd}"  titlePrefix="${titlePrefix}" titleId="cols.mnuGrpNm" value="${rescVa}" style="${style}"
							maxByte="200" validator="changeLangSelector('setFncPop', id, va)"
							 mandatory="Y" />
					</c:forEach>
				</td>
				<td>
					<c:if test="${fn:length(_langTypCdListByCompId)>1}">
						<select id="langSelector" onchange="changeLangTypCd('fncSubjRescArea','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
						<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
						<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
						</c:forEach>
						</select>
					</c:if>
					<u:input type="hidden" id="rescId" value="${ctFncMngBVo.ctFncSubjRescId}" />
				</td>
			</tr>
		</tbody>
	</table>
	</td>
	
	<c:if test="${param.typ == '2' || fnc == 'reg'}">
		<tr>
		<td class="head_lt"><u:mandatory /><u:msg titleId="cols.url" alt="URL" /></td>
		<td><u:input id="url" name="url" value="${url}" titleId="cols.url" style="width: 494px;" mandatory="Y" maxByte="256"/></td>
		</tr>
		
		<tr> 
		<td class="head_lt"><u:msg titleId="ct.cols.ptUrl" alt="포틀릿 URL" /></td>
		<td><u:input id="ptUrl" name="ptUrl" value="${ptUrl}" titleId="ct.cols.ptUrl" style="width: 494px;" maxByte="256"/></td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:mandatory /><u:msg titleId="ct.cols.relTblSubj" alt="관계테이블제목" /></td>
	
		<td><u:input id="relTblSubj" name="relTblSubj" value="${relTblSubj}" titleId="ct.cols.relTblSubj" style="width: 200px;" mandatory="Y" maxByte="200"/></td>
		</tr>
	
		<tr>
		<td class="head_lt"><u:msg titleId="cols.dftYn" alt="디폴트여부" /></td>
			<td>
				<u:checkArea>
					<c:if test="${fnc == 'mod'}">
						<u:set test="${ctFncMngBVo.dftYn == 'Y'}" var="dftY" value="true" elseValue="false" />
						<u:set test="${ctFncMngBVo.dftYn == 'N'}" var="dftN" value="true" elseValue="false" />
					</c:if>
					<c:if test="${fnc == 'reg'}">
						<c:set var="dftN" value="true"></c:set>
					</c:if>
					<u:radio name="dftYn" value="Y" titleId="cm.option.yes" alt="예" checked="${dftY}" inputClass="bodybg_lt" />
					<u:radio name="dftYn" value="N" titleId="cm.option.no" alt="아니오" checked="${dftN}" inputClass="bodybg_lt" />
				</u:checkArea>
			</td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.mulChoiYn" alt="다중선택여부" /></td>
		<td><u:checkArea id="mulChoiArea">
			<c:if test="${fnc == 'mod'}">
				<u:set test="${ctFncMngBVo.mulChoiYn == 'Y'}" var="mulChoiY" value="true" elseValue="false" />
				<u:set test="${ctFncMngBVo.mulChoiYn == 'N'}" var="mulChoiN" value="true"  elseValue="false" />
			</c:if>
			<c:if test="${fnc == 'reg'}">
				<c:set var="mulChoiN" value="true"></c:set>
			</c:if>
			<u:radio name="mulChoiYn" value="Y" titleId="cm.option.yes" alt="예" checked="${mulChoiY}"  onclick="mulchk('Y')"  inputClass="bodybg_lt" />
			<u:radio name="mulChoiYn" value="N" titleId="cm.option.no" alt="아니오" checked="${mulChoiN}" onclick="mulchk('N')" inputClass="bodybg_lt" />
			</u:checkArea></td>
		</tr>
	</c:if>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
	<td><u:checkArea>
		<c:if test="${fnc == 'mod'}">
			<u:set test="${ctFncMngBVo.useYn == 'Y'}" var="useY" value="true" elseValue="false" />
			<u:set test="${ctFncMngBVo.useYn == 'N'}" var="useN" value="true" elseValue="false" />
		</c:if>
		<c:if test="${fnc == 'reg'}">
			<c:set var="useY" value="true"></c:set>
		</c:if>
		<u:radio name="useYn" value="Y" titleId="cm.option.yes" alt="예" checked="${useY}" inputClass="bodybg_lt" />
		<u:radio name="useYn" value="N" titleId="cm.option.no" alt="아니오" checked="${useN}" inputClass="bodybg_lt" />
		</u:checkArea></td>
	</tr>
</u:listArea>
<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="javascript:saveFnc();" alt="저장" auth="${param.typ == 1 ? 'SYS' : 'A'}" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>
