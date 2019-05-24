<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--
<%//폴더여부 변경%>
function onChangeFldYn(object, va){
	var $frm = $("#mnuForm");
	var disable = 'Y'==va;
	setReadonly($frm.find("[name='mnuUrl']"), disable);
	setDisabled($frm.find("[name='popYn']"), disable);
	if(!disable && $frm.find("[name='popYn']").val()=='N'){
		disable = true;
	}
	setReadonly($frm.find("[name='popSetupCont']"), disable);
}
<%//팝업여부 변경%>
function onChangePopYn(object, va){
	var $frm = $("#mnuForm");
	var disable = 'N'==va;
	setReadonly($frm.find("[name='popSetupCont']"), disable);
}
<%//저장 버튼%>
function saveMnu(){
	if(validator.validate('mnuForm')){
		<%// disable 된 select disable 해제 %>
		var arr = releaseDisable($('#mnuForm select'));
		<%// 서버 전송%>
		var $form = $("#mnuForm");
		$form.attr('method','post');
		$form.attr('action','./transMnu.do');
		$form.attr('target','dataframeForFrame');
		$form.submit();
		<%// select disable 다시 적용 %>
		unreleaseDisable(arr);
	}
}
<%// 저장 후 리로드 %>
function afterTrans(mnuPid){
	parent.getIframeContent('mnuTree').reoladFrame(mnuPid);
	parent.openMnu(mnuPid);
}
<%// 폴더면 url 체크 안함 %>
function skipUrlCheck(){
	return $('#fldYn').val() == 'Y';
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
	onChangeFldYn(null, '${ptMnuDVo.fldYn}');
});
//-->
</script>

<form id="mnuForm" style="padding:10px;">
<input type="hidden" name="menuId" value="${menuId}" />
<u:input type="hidden" id="mnuId" value="${ptMnuDVo.mnuId}" /><c:if test="${not empty param.admMnuYn}">
<u:input id="admMnuYn" type="hidden" value="${param.admMnuYn}" /></c:if>

<u:listArea>
	<tr>
	<td width="18%" class="head_lt"><u:mandatory /><u:msg titleId="cols.mnuNm" alt="메뉴명" /></td>
	<td>
		<table cellspacing="0" cellpadding="0" border="0">
		<tr>
		<td id="langTypArea">
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
		<u:convert srcId="${ptMnuDVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
		<u:set test="${status.first}" var="style" value="width:200px;" elseValue="width:200px; display:none" />
		<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
		<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="pt.cols.fldNm" value="${rescVa}" style="${style}"
			maxByte="200" validator="changeLangSelector('mnuForm', id, va)" mandatory="Y" />
		</c:forEach>
		</td>
		<td>
		<c:if test="${fn:length(_langTypCdListByCompId)>1}">
			<select id="langSelector" onchange="changeLangTypCd('mnuForm','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
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
	<td width="18%" class="head_lt"><u:mandatory id="urlMandatory" display="${ptMnuDVo.fldYn == 'N'}" /><u:msg titleId="cols.mnuUrl" alt="메뉴URL" /></td>
	<td><u:input id="mnuUrl" value="${ptMnuDVo.mnuUrl}" titleId="cols.mnuUrl"
		maxByte="1000" style="width:95%" mandatory="Y" skipper="skipUrlCheck()" /></td>
	</tr>
	
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.fldYn" alt="폴더여부" /></td>
	<td><select id="fldYn" name="fldYn" onchange="onChangeFldYn(this, this.value)" <u:elemTitle titleId="cols.fldYn" />>
		<u:option value="Y" title="Y" checkValue="${ptMnuDVo.fldYn}" />
		<u:option value="N" title="N" checkValue="${ptMnuDVo.fldYn}" />
		</select></td>
	</tr>
	
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
	<td><select id="useYn" name="useYn" <u:elemTitle titleId="cols.useYn" />>
		<u:option value="Y" title="Y" checkValue="${ptMnuDVo.useYn}" />
		<u:option value="N" title="N" checkValue="${ptMnuDVo.useYn}" />
		</select></td>
	</tr>
	
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.sysMnuYn" alt="시스템메뉴여부" /></td>
	<td><select id="sysMnuYn" name="sysMnuYn" <u:elemTitle titleId="cols.sysMnuYn" />>
		<u:option value="Y" title="Y" checkValue="${ptMnuDVo.sysMnuYn}" />
		<u:option value="N" title="N" checkValue="${ptMnuDVo.sysMnuYn}" />
		</select></td>
	</tr>
	
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.popYn" alt="팝업여부" /></td>
	<td><select id="popYn" name="popYn" onchange="onChangePopYn(this, this.value)" <u:elemTitle titleId="cols.popYn" />>
		<u:option value="Y" title="Y" checkValue="${ptMnuDVo.popYn}" />
		<u:option value="N" title="N" checkValue="${ptMnuDVo.popYn}" />
		</select></td>
	</tr>
	
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.popSetupCont" alt="팝업설정내용" /></td>
	<td><u:textarea id="popSetupCont" value="${ptMnuDVo.popSetupCont}" titleId="cols.popSetupCont"
	 maxByte="1000" style="width:95%" rows="4" /></td>
	</tr>
	
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.mnuDesc" alt="메뉴설명" /></td>
	<td><u:textarea id="mnuDesc" value="${ptMnuDVo.mnuDesc}" titleId="cols.mnuDesc"
	 maxByte="1000" style="width:95%" rows="4" /></td>
	</tr>

	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.regr" alt="등록자" /></td>
	<td class="body_lt"><u:out value="${ptMnuDVo.regrNm}" /></td>
	</tr>
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	<td class="body_lt"><u:out value="${ptMnuDVo.regDt}" /></td>
	</tr>
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.modr" alt="수정자" /></td>
	<td class="body_lt"><u:out value="${ptMnuDVo.modrNm}" /></td>
	</tr>
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.modDt" alt="수정일시" /></td>
	<td class="body_lt"><u:out value="${ptMnuDVo.modDt}" /></td>
	</tr>

</u:listArea>

</form>