<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">

var tableId;

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
	}
	
	$("#setOendQuesModPop").css("height", "auto");
	dialog.close('setImageDialog');
}

//수정된 주관식 질문 저장
function saveOendQues(){
	//var arr = getIframeContent("searchUserCollectedFrm").getAllUsers();
	var arr = [];
	var oendQuesImgInfo = [];
	
	oendQuesImgInfo.push($("#oendQuesArea").find("input[id='ques_img_file_id']").val());
	oendQuesImgInfo.push($("#oendQuesArea").find("td[id='ques_img_row']").attr("id"));
	oendQuesImgInfo.push($("#oendQuesArea").find("img[id='ques_img']").attr("id"));
	if($("#oendQuesArea").find("img[id='ques_img']").attr("src") == ""){
		oendQuesImgInfo.push("-1");
	}else{
		oendQuesImgInfo.push($("#oendQuesArea").find("img[id='ques_img']").attr("src"));
	}
	oendQuesImgInfo.push($("#oendQuesArea").find("input[id='ques_img_file_id']").attr("id"));
	
	arr.push({tblId:tableId,
			  subj:$("#subj").val(),
			  oendQuesImgInfo:oendQuesImgInfo
			 });
	setOendQuesMod(arr);
	dialog.close('setOendQuesModPop');

}

$(function() {
	var quesInfo = oendQuesInfo('${tblId}');
	tableId = "${tblId}";
	$("#subj").val(quesInfo.quesSubj);
	$("input[id='ques_img_file_id']").attr("id", quesInfo.quesImgFileId);
	$("input[id='ques_img_file_id']").val(quesInfo.quesImgFileIdVal);
	$("td[id='ques_img_row']").attr("id", quesInfo.quesImgRowId);
	$("img[id='ques_img']").attr("id", quesInfo.quesImgNo);
	$("img[id='ques_img']").attr("src", quesInfo.quesImgFilePath);
	if($("img[id='ques_img']").attr("src")!="-1"){
		$("td[id='ques_img_row']").attr("style", "display:block;");
	}else{
		$("td[id='ques_img_row']").attr("style", "display: none;");
	}

 });

function formSubmit(){
	var $form = $("#survOendQues");
	$form.attr('method', 'post');
	$form.attr('action', './setSurvQues.do?menuId=${menuId}&fnc=${fnc}');
	$form.submit();
}

</script>

<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />
<u:set test="${fnc == 'mod'}" var="subj" value="점심 메뉴에 대한 설문" elseValue="" />
<u:set test="${fnc == 'mod'}" var="selected" value=" selected" elseValue="" />


<form id = survOendQues>
<div style="width:800px">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="compId" value="${wvSurvBVo.compId}"/>
<u:input type="hidden" id="survId" value="${wvSurvBVo.survId}"/>
<% // 표 %>
<u:listArea id="oendQuesArea">
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.quesSubj" alt="질문제목" /></td>
	<td colspan="3"><u:input id="subj" value="${subj}" titleId="cols.subj" style="width: 98%;" /></td>
	</tr>

	<tr>
	<td width="18%" class="head_lt">
		<u:msg titleId="cols.quesImg" alt="질문이미지" />
	</td>
	<td colspan="3">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<u:msg titleId="cm.msg.preparing" var="msg" alt="준비중.." />
				<td>				
					<input type="hidden" id="ques_img_file_id" value="">
					<u:buttonS id="quesImgAdd" href="javascript:setImagePop(-1,-1);" titleId="wv.btn.imgAdd" alt="이미지추가" />
				</td>
			</tr>
			<tr>
				<td class="td_imglt" id="ques_img_row" style="display: none;"><img id="ques_img" src="" width="58" height="70"/></td>
			</tr>
	
			<c:if test="${fnc == 'mod'}">
				<tr>
					<td class="td_imglt" style="display: none;"><img src="${_ctx}/images/etc/photo2.png" width="58" height="70"/></td>
				</tr>
			</c:if>
		</table>
	</td>
	</tr>
</u:listArea>

<u:blank />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button href="javascript:saveOendQues();" titleId="cm.btn.save" alt="저장" auth="W" />
	<u:button onclick="dialog.close(this);" titleId="cm.btn.close" alt="닫기" auth="R" />
</u:buttonArea>

<u:blank />

</div>
</form>