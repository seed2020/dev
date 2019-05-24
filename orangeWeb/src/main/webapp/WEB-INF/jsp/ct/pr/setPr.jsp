<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
function saveCtPr(){
	//홍보할 커뮤니티 ID
	var $selectCtId = $("#selectCtPr").val();
	
	if (isInUtf8Length($('#cont').val(), 0, '${bodySize}') > 0) {
		alertMsg('cm.input.check.maxbyte', ['<u:msg titleId="cols.cont" />','${bodySize}']);<% // cm.input.check.maxbyte="{0}"의 최대 바이트수 ({1} bytes)를 초과 하였습니다. %>
		return;
	}
	
	if (validator.validate('viewPrForm')) {
		if($selectCtId == null || $selectCtId == ""){
			//ct.msg.noSelectCt = 커뮤니티를 선택해주시기 바랍니다.
			alertMsg("ct.msg.noSelectCt"); 
		}else{
			var $form = $('#viewPrForm');
			$form.attr('method','post');
			$form.attr('action','./transSetCtPrSave.do?menuId=${menuId}');
			$form.attr('enctype','multipart/form-data');
			$form.attr('target','dataframe');
			editor('cont').prepare();
			saveFileToForm('${filesId}', $form[0], null);
			//$form[0].submit();
		}
		
	}
	
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />
<u:set test="${fnc == 'mod'}" var="subj" value="놀러 오세요~" elseValue="" />
<u:set test="${fnc == 'mod'}" var="cont" value="부담없이 놀러 오세요~ \n많이 많이 놀러 오세요~ \n감사합니다." elseValue="" />

<u:title titleId="ct.jsp.setPr.${fnc}.title" alt="홍보마당 등록/홍보마당 수정" menuNameFirst="true"/>

<form id="viewPrForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="bullId" value="${param.bullId}" />
<u:input type="hidden" id="bullPid" value="${param.bullPid}" />
<u:input type="hidden" id="bullStatCd" value="B" />
<u:input type="hidden" id="bullRezvDt" value="${ctPrBVo.bullRezvDt}" />
<u:input type="hidden" id="tgtDeptYn" value="${ctPrBVo.tgtDeptYn}" />
<u:input type="hidden" id="tgtUserYn" value="${ctPrBVo.tgtUserYn}" />

<% // 폼 필드 %>
<u:listArea colgroup="18%,82%" noBottomBlank="true">
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.subj" alt="제목" /></td>
	<td class="body_lt"><u:input id="subj" titleId="cols.subj" style="width:98%;" value="${ctPrBVo.subj}" mandatory="Y" maxByte="240"/></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="ct.cols.cm" alt="커뮤니티" /></td>
	<td class="body_lt">
		<select id="selectCtPr" name="selectCtPr">
			<option value=""><u:msg titleId="ct.jsp.setPr.tx01" alt="해당 커뮤니티를 선택하십시오." /></option>
			<c:forEach var="ctUserMastVo" items="${ctUserMastList}" varStatus="status">
				<option value="${ctUserMastVo.ctId}" <c:if test="${ctUserMastVo.ctId == ctPrBVo.ctId}">selected = "selected"</c:if>><u:out value="${ctUserMastVo.ctNm}"/></option>
			</c:forEach>
		</select></td>
	</tr>
</u:listArea>

<div id="editor1Area" class="listarea" style="width:100%; height:302px; padding-top:2px"></div>
<u:editor id="cont" width="100%" height="300" module="ct" areaId="editor1Area" value="${ctPrBVo.cont}" />

<% // 첨부파일 %>
<u:listArea>
	<tr>
	<td><u:files id="${filesId}" fileVoList="${fileVoList}" module="ct" mode="set" exts="${exts }" extsTyp="${extsTyp }"/></td>
	</tr>
</u:listArea>



<% // 하단 버튼 %>
<u:buttonArea>
	<u:msg titleId="cm.msg.save.success" var="msg" alt="저장 되었습니다." />
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:saveCtPr();"/>
<%-- 	<u:button titleId="cm.btn.save" alt="저장" href="javascript:void(alert('${msg}'));" auth="W" /> --%>
	<u:button titleId="cm.btn.cancel" alt="취소" href="javascript:history.go(-1);" />
</u:buttonArea>

</form>

