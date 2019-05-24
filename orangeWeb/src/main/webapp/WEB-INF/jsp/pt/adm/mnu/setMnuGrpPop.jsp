<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><script type="text/javascript">
$(document).ready(function() {
	onMnuGrpTypCdChange($("#setMnuGrpPop #mnuGrpTypCd").val());
});
function setMdRid(){
	var mdRid = $("#setMnuGrpPop #mdRid").val();
	if(mdRid=='') mdRid = null;
	dialog.open('setMdRidDialog','<u:msg titleId="cols.mdRid" alt="모듈참조ID" />','./setMdRidPop.do?menuId=${menuId}&mnuGrpMdCd=M'+(mdRid!=null ? '&mdRid='+mdRid.toLowerCase(): ''));
}
</script>

<div style="width:550px">
<form id="setMnuGrpPop">
<input type="hidden" name="menuId" value="${menuId}" />
<u:input type="hidden" id="mnuGrpId" value="${ptMnuGrpBVo.mnuGrpId}" />
<c:if test="${not empty param.mnuGrpMdCd}"><u:input type="hidden" id="mnuGrpMdCd" value="${param.mnuGrpMdCd}" /></c:if>

<u:listArea>

	<tr>
	<td width="25%" class="head_lt"><u:mandatory /><u:msg titleId="cols.mnuGrpNm" alt="메뉴그룹명" /></td>
	<td>
		<table cellspacing="0" cellpadding="0" border="0">
		<tr>
		<td id="langTypArea">
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
		<u:convert srcId="${ptMnuGrpBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
		<u:set test="${status.first}" var="style" value="" elseValue="display:none" />
		<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
		<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.mnuGrpNm" value="${rescVa}" style="${style}"
			maxByte="200" validator="changeLangSelector('setMnuGrpPop', id, va)" mandatory="Y" />
		</c:forEach>
		</td>
		<td>
		<c:if test="${fn:length(_langTypCdListByCompId)>1}">
			<select id="langSelector" onchange="changeLangTypCd('setMnuGrpPop','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.cdVa}" />
			</c:forEach>
			</select>
		</c:if>
		<u:input type="hidden" id="rescId" value="${ptMnuGrpBVo.rescId}" />
		</td>
		</tr>
		</table>
	</td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.mnuGrpTyp" alt="메뉴그룹구분" /></td>
	<td><select id="mnuGrpTypCd" name="mnuGrpTypCd" onchange="onMnuGrpTypCdChange(this.value)" <u:elemTitle titleId="cols.mnuGrpTyp" />>
		<c:forEach items="${mnuGrpTypCdList}" var="mnuGrpTypCdVo" varStatus="status"><c:if
			test="${ empty param.mnuGrpMdCd
				or (param.mnuGrpMdCd == 'A' and mnuGrpTypCdVo.refVa1 != 'Portlet')
				or (param.mnuGrpMdCd == 'M' and mnuGrpTypCdVo.refVa1 == 'Mobile')}">
			<u:option value="${mnuGrpTypCdVo.cd}" title="${mnuGrpTypCdVo.rescNm}" checkValue="${ptMnuGrpBVo.mnuGrpTypCd}" /></c:if>
		</c:forEach>
		</select></td>
	</tr>
	<u:secu auth="SYS">
	<tr>
	<td class="head_lt"><u:msg titleId="pt.cols.openScop" alt="공개범위" /></td>
	<td><select id="openCompId" name="openCompId" <u:elemTitle titleId="pt.cols.openScop" />>
		<u:option value="${sysCompId}" titleId="cm.option.allComp" alt="전체회사" checkValue="${ptMnuGrpBVo.openCompId}" /><c:forEach
			items="${ptCompBVoList}" var="ptCompBVo" varStatus="status">
		<u:option value="${ptCompBVo.compId}" title="${ptCompBVo.rescNm}" checkValue="${ptMnuGrpBVo.openCompId}"
			/></c:forEach>
		</select></td>
	</tr>
	</u:secu>
	<tr>
	<td class="head_lt"><u:msg titleId="cols.mnuUrl" alt="메뉴URL" /></td>
	<td>
	<u:input id="mnuUrl" value="${ptMnuGrpBVo.mnuUrl}" titleId="cols.mnuUrl" maxByte="1000" style="width:95%" /></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.popSetupCont" alt="팝업설정내용" /></td>
	<td>
	<u:input id="popSetupCont" value="${ptMnuGrpBVo.popSetupCont}" titleId="cols.popSetupCont" maxByte="400" style="width:95%" /></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.mdRid" alt="모듈참조ID" /></td>
	<td><table cellspacing="0" cellpadding="0" border="0">
		<tr>
			<td><u:input id="mdRid" value="${ptMnuGrpBVo.mdRid}" titleId="cols.mdRid" maxByte="20" valueOption="alpha,number" valueAllowed="._" /></td><c:if
				test="${not empty mobileEnable and sessionScope.userVo.userUid eq 'U0000001'}">
			<td><u:buttonS titleId="cols.mdRid" href="javascript:setMdRid();" /></td></c:if>
		</tr>
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
	<td><select id="useYn" name="useYn" <u:elemTitle titleId="cols.useYn" />>
		<u:option value="Y" titleId="cm.option.use" checkValue="${ptMnuGrpBVo.useYn}" />
		<u:option value="N" titleId="cm.option.notUse" checkValue="${ptMnuGrpBVo.useYn}" />
		</select></td>
	</tr>

</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" href="javascript:saveMnuGrp();" alt="저장" auth="A" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</form>
</div>