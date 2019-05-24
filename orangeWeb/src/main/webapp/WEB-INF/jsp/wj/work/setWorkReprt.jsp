<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.Date"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="params"/>
<u:params var="paramsForList" excludes="reprtNo"/>
<script type="text/javascript">
<!--
<% // [버튼클릭] - 저장  %>
function save() {
	if (validator.validate('setForm')){
		if (isInUtf8Length($('#cont').val(), 0, '${bodySize}') > 0) {
			alertMsg('cm.input.check.maxbyte', ['<u:msg titleId="cols.cont" />','${bodySize}']);<% // cm.input.check.maxbyte="{0}"의 최대 바이트수 ({1} bytes)를 초과 하였습니다. %>
			return false;
		}
		
		var $form = $('#setForm');
		$form.attr('method','post');
		$form.attr('action','./${transPage}.do?menuId=${menuId}');
		$form.attr('enctype','multipart/form-data');
		$form.attr('target','${dataframe}');
		editor('cont').prepare();
		saveFileToForm('${filesId}', $form[0], null);
		return true;
		
	}
	
};
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<u:title menuNameFirst="true"/>
<form id="setForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="listPage" value="./${listPage}.do?${paramsForList}" />
<u:input type="hidden" id="viewPage" value="./${viewPage}.do?${params}" />
<c:if test="${!empty param.reprtNo }"><u:input type="hidden" id="reprtNo" value="${param.reprtNo}" /></c:if>
<% // 폼 필드 %>
<div class="listarea" id="listArea">
<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
<colgroup><col width="12%"/><col width="38%"/><col width="12%"/><col width="38%"/></colgroup>
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.subj" alt="제목" /></td>
	<td class="body_lt" colspan="3"><u:input id="subj" titleId="cols.subj" style="width:98%;" value="${wjWorkReprtBVo.subj}" mandatory="Y" maxByte="240"/></td>
	</tr>
</table>
</div>
<%
	com.innobiz.orange.web.wj.vo.WjWorkReprtBVo wjWorkReprtBVo = (com.innobiz.orange.web.wj.vo.WjWorkReprtBVo)request.getAttribute("wjWorkReprtBVo");
	if(wjWorkReprtBVo != null){
		if(request.getAttribute("namoEditorEnable")==null){
			String _bodyHtml = com.innobiz.orange.web.cm.utils.EscapeUtil.escapeWriteableHtml(wjWorkReprtBVo.getCont());
			request.setAttribute("_bodyHtml", _bodyHtml);
		} else {
			request.setAttribute("_bodyHtml", wjWorkReprtBVo.getCont());
		}
	}
%><u:editor id="cont" width="100%" height="400px" module="wj" value="${_bodyHtml}" padding="2" />
	
<u:blank />
<u:listArea>
	<tr>
	<td><u:files id="${filesId}" fileVoList="${fileVoList}" module="wj" mode="set" exts="${exts }" extsTyp="${extsTyp }"/></td>
	</tr>
</u:listArea>

</form>

<% // 하단 버튼 %>
<u:buttonArea topBlank="true">
	<u:button titleId="cm.btn.save" alt="저장" onclick="save();" auth="W" />
	<u:button titleId="cm.btn.cancel" alt="취소" href="javascript:history.go(-1);" />
</u:buttonArea>