<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<u:set test="${!empty param.fnc}" var="fnc" value="${param.fnc}" elseValue="reg" />

<script type="text/javascript">

function setImagePop(quesNum, examNum){
	var url='./setImagePop.do?menuId=${menuId}&quesNum='+quesNum+'&examNum='+examNum;
	dialog.open('setImageDialog','<u:msg titleId="or.jsp.setOrg.photoTitle" alt="사진 선택" />',url);
	
}

<%//사진 변경 - 후처리 %>
function setImage( quesNum, examNum, fileId, fileNm, filePath){
	//alert(quesNum+"  "+examNum+"  "+fileId+"  "+fileNm);
	if(quesNum==-1&&examNum==-1){
		$("#oendQuesArea").find("#ques_img_nm").html(fileNm);
		$("#oendQuesArea").find("#ques_img_file_id").val(fileId);
		$("#oendQuesArea").find("#ques_img").attr("src",filePath);
		$("#oendQuesArea").find("#ques_img_row").show();
		$("#oendQuesArea").find("#ques_img_mod_row").hide();
		
	}
	
	$("#setOendQuesPop").css("height", "auto");
	dialog.close('setImageDialog');
}


function saveOendQues(){
	//var arr = getIframeContent("searchUserCollectedFrm").getAllUsers();
	var arr = [];
	arr.push({subj:$("#subj").val()});
	oendQuesHandler(arr);
	dialog.close('setOendQuesPop');

}


function formSubmit(){
	
	if(validator.validate('survOendQuesForm')){
		var $form = $("#survOendQuesForm");
		$form.attr('method', 'post');
		
		$form.attr('action', './transSetSurvOendQuesSave.do?menuId=${menuId}&fnc=${fnc}&survId=${survId}&quesId=${quesId}&ctId=${ctId}');
		$form.submit();
	}
}

</script>

<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />
<u:set test="${fnc == 'mod'}" var="subj" value="${ctSurvBVo.quesCont}" elseValue="" />
<u:set test="${fnc == 'mod'}" var="selected" value=" selected" elseValue="" />
<u:set test="${fnc == 'mod'}" var="mandaReplyYn" value="${ctSurvBVo.mandaReplyYn}" elseValue="" />

<form id = "survOendQuesForm">
<div style="width:800px">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="compId" value="${ctSurvBVo.compId}"/>

<% // 표 %>
<u:listArea id="oendQuesArea">
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="cols.quesSubj" alt="질문제목" /></td>
		<td colspan="3">
			<u:input id="subj" value="${subj}" titleId="cols.subj" style="width: 98%;" mandatory="Y" onkeydown="if (event.keyCode == 13) return false;" maxLength="240"/>
		</td>
	</tr>

	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="cols.quesImg" alt="질문이미지" /></td>
		<td colspan="3">
		
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<u:msg titleId="cm.msg.preparing" var="msg" alt="준비중.." />
				<td>				
					<input type="hidden" id="ques_img_file_id" name="ques_img_file_id" value="${ctSurvBVo.imgSurvFileId}" />
					<u:buttonS id="quesImgAdd" href="javascript:setImagePop(-1,-1);" titleId="wv.btn.imgAdd" alt="이미지추가" />
				</td>
			</tr>
			<tr>
				<td class="td_imglt" id="ques_img_row" style="display: none;"><img id="ques_img" src="" width="58" height="70"/></td>
			</tr>
				<c:if test="${fnc == 'mod'}">
					<tr>
					<u:set test="${empty ctSurvBVo.imgSurvFileId}" var="subjDisp" value="none" elseValue="block" />
						<td class="td_imglt" style="display: ${subjDisp};" id="ques_img_mod_row"><img src="${_ctx}${ctSurvBVo.imgSavePath}" width="58" height="70"/></td>
					</tr>
				</c:if>
		</table>
		</td>
	</tr>
	<tr>
		<c:if test="${fnc == 'mod'}">
		<u:set test="${mandaReplyYn == 'Y'}" var="mandaReplyY" value="true" elseValue="false" />
		<u:set test="${mandaReplyYn == 'N'}" var="mandaReplyN" value="true" elseValue="false" />
		</c:if>
		<c:if test="${fnc != 'mod'}">
			<c:set  var="mandaReplyN" value="true" />
		</c:if>
		<td width="18%" class="head_lt"><u:msg titleId="wv.cols.mandatory.replyYn" alt="필수응답여부" /></td>
			<td><u:checkArea>
				<u:radio name="mandaReplyYn" value="Y" title="Y" checked="${mandaReplyY}"/>
				<u:radio name="mandaReplyYn" value="N" title="N" checked="${mandaReplyN}" />
				</u:checkArea>
		</td>
	</tr>
</u:listArea>

<u:blank />

<% // 하단 버튼 %>
<u:buttonArea>
	<c:if test="${!empty authChkW && authChkW == 'W' }">
		<u:button onclick="formSubmit();" titleId="cm.btn.save" alt="저장" />
	</c:if>
	<c:if test="${!empty authChkR && authChkR == 'R' }">
		<u:button onclick="dialog.close(this);" titleId="cm.btn.close" alt="닫기" />
	</c:if>
</u:buttonArea>

<u:blank />

</div>
</form>