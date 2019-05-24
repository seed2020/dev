<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
function saveBull(){
	
	var $bullPid = $("#bullPid").val();
	if (isInUtf8Length($('#cont').val(), 0, '${bodySize}') > 0) {
		alertMsg('cm.input.check.maxbyte', ['<u:msg titleId="cols.cont" />','${bodySize}']);<% // cm.input.check.maxbyte="{0}"의 최대 바이트수 ({1} bytes)를 초과 하였습니다. %>
		return;
	}
	
	if (validator.validate('setPdsForm')) {
		var $form = $('#setPdsForm');
		$form.attr('method','post');
		$form.attr('action','./transSetBullSave.do?menuId=${menuId}&ctId=${ctId}');
		$form.attr('enctype','multipart/form-data');
		$form.attr('target','dataframe');
		editor('cont').prepare();
		saveFileToForm('${filesId}', $form[0], null);
		//$form[0].submit();
	}
	
}
	
	
function sendTo() {
	dialog.open('sendToPop','보내기','../sendToPop.do?menuId=${menuId}');
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />
<u:set test="${fnc == 'mod'}" var="subj" value="Java Virtual Machine Specification" elseValue="" />
<u:set test="${fnc == 'mod'}" var="cont" value="JVM 규격 문서입니다. 참고하시기 바랍니다. 감사합니다." elseValue="" />

<u:title titleId="ct.jsp.setPds.${fnc}.title" alt="자료실 조회" menuNameFirst="true"/>

<form id="setPdsForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="bullId" value="${param.bullId}" />
<u:input type="hidden" id="bullPid" value="${param.bullPid}" />
<u:input type="hidden" id="bullStatCd" value="B" />
<u:input type="hidden" id="bullRezvDt" value="${ctRecMastBVo.bullRezvDt}" />
<u:input type="hidden" id="tgtDeptYn" value="${ctRecMastBVo.tgtDeptYn}" />
<u:input type="hidden" id="tgtUserYn" value="${ctRecMastBVo.tgtUserYn}" />
<% // 폼 필드 %>
<u:listArea>
	<tr>
	<td class="head_lt"><u:msg titleId="cols.subj" alt="제목" />
		<input id="bullCtFncId" name="bullCtFncId" type="hidden" value="${bullCtFncId}"/>
		<input id="bullCtFncUid" name="bullCtFncUid" type="hidden" value="${bullCtFncUid}"/>
		<input id="bullCtFncPid" name="bullCtFncPid" type="hidden" value="${bullCtFncPid}"/>
		<input id="bullCtFncLocStep" name="bullCtFncLocStep" type="hidden" value="${bullCtFncLocStep}"/>
		<input id="bullCtFncOrdr" name="bullCtFncOrdr" type="hidden" value="${bullCtFncOrdr}"/>
		
		<input id="bullPid" name="bullPid" type="hidden" value="${bullPid}"/>
	</td>
	<td class="body_lt"><u:input id="subj" name="subj" titleId="cols.subj" style="width:98%;" value="${ctRecMastBVo.subj}" mandatory="Y" maxByte="240"/></td>
	</tr>
	
	<tr>
	<td id="editor1Area" colspan="2"><u:editor id="cont" width="1022" height="300" module="ct" areaId="editor1Area" value="${ctRecMastBVo.cont}" /></td>
	</tr>
</u:listArea>

<u:listArea>	
	<tr>
	<td><u:files id="${filesId}" fileVoList="${fileVoList}" module="ct" mode="set" exts="${exts }" extsTyp="${extsTyp }"/></td>
	</tr>
</u:listArea>
<!-- 
	<tr>	
		<dl>
		<dd class="attachbtn_check"><input type="checkbox" id="contactBack"/></dd>
		<dd><a href="javascript:" class="sbutton button_small"><span>파일첨부</span></a><a href="javascript:" class="sbutton button_small"><span><img src="${_cxPth}/images/${_skin}/ico_delete.png"/>삭제</span></a></dd>
		</dl>
		</div>

		<c:if test="${fnc == 'mod'}">
		<div class="attacharea">
		<dl>
		<dd class="attach_check"><input type="checkbox" id="contactBack"/></dd>
		<dd class="attach_img"><img src="${_cxPth}/images/cm/ico_zip.png"/></dd>
		<dd class="attach"><a href="javascript:">JVMSpec1.zip</a></dd>
		</dl>
		</div>
		
		<div class="attacharea">
		<dl>
		<dd class="attach_check"><input type="checkbox" id="contactBack"/></dd>
		<dd class="attach_img"><img src="${_cxPth}/images/cm/ico_zip.png"/></dd>
		<dd class="attach"><a href="javascript:">JVMSpec2.zip</a></dd>
		</dl>
		</div>
		</c:if>
		</td>
	</tr>
-->
<% // 하단 버튼 %>
<u:buttonArea>
	<u:msg titleId="cm.msg.save.success" var="msg" alt="저장 되었습니다." />
<%-- 	<u:button titleId="cm.btn.save" alt="저장" href="javascript:void(alert('${msg}'));" auth="W" /> --%>
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:saveBull();"/>
	<u:button titleId="cm.btn.cancel" alt="취소" href="./listPds.do?menuId=${menuId}&ctId=${ctId}" />
<%-- 	<u:button titleId="cm.btn.cancel" alt="취소" href="javascript:history.go(-1);" /> --%>
</u:buttonArea>

</form>

