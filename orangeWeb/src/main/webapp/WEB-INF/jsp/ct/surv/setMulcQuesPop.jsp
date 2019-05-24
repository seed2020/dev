<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<u:set test="${!empty param.fnc}" var="fnc" value="${param.fnc}" elseValue="reg" />

<script type="text/javascript">
<!--

var tableNoCount = 0;

function mulcSubmitForm(){
	var emptyChk =0;
	var arrExamIndex = [];
	var $examTableExam=$("#examArea").find(".item");
	if(validator.validate('setMulcQuesPopForm')){
		var $form = $('#setMulcQuesPopForm');
		   $form.attr('method','post');
		   
		   $examTableExam.find("#exam").each(function(index){
				if($(this).val() == ''){
					emptyChk = 1;
					arrExamIndex.push(index+1 + ",");
				}
			});
		   
		if(emptyChk != 1){   
			$form.attr('action','./setMulcQuesPopSave.do?menuId=${menuId}&fnc=${fnc}&survId=${survId}&quesId=${quesId}&ctId=${ctId}');
		   	$form.submit();
		  	dialog.close(this);
		}else{
			alert("[" + arrExamIndex.join('').slice(0,-1) + "]"  + '<u:msg titleId="wv.msg.set.inputOpt" alt="보기(을)를 입력해 주십시오."/>');
		}
	}
	
}


function toggleImgAddTd() {
	if ($('input:checkbox[name="imgAdd"]').is(":checked")) {
		$('#examArea th:nth-child(4)').show();
		$('#examArea td:nth-child(4)').show();
	} else {
		$('#examArea th:nth-child(4)').hide();
		$('#examArea td:nth-child(4)').hide();
	}
}

function checkAll(){
	if($("#checkFlagAll").is(":checked")){

		$("input[name='checkFlag']:checkbox").each(function(){
			$(this).parent().attr("class", "checked");
			//$(this).removeAttr("checked");
			$(this).attr("checked","checked");
			//$(this).trigger("checked");
			
		});
		
	}else{
		$("input[name='checkFlag']:checkbox").each(function(){
			$(this).parent().attr("class", "");
			//$(this).trigger("click");
			$(this).removeAttr("checked");
			
		});
	}
}

//보기 추가
function examAdd(){
	var $examTableObj=$("#examArea").find(".listtable");
	var $examTablelength=$("#examArea").find(".item").length+1;
	//index로 들어갈 값
	var $examTableleng=$("#examArea").find(".item").length;
	var $data = "<tr class='item'><td class='bodybg_ct'><div class='checker' id='uniform-checkFlag'>"
				+"<span><input id='checkFlag' name='checkFlag' type='checkbox'></span></div></td><td class='body_ct' id='itemNo'>"+$examTablelength+"</td>"
				+"<td><input id='exam' name='exam' title='<u:msg titleId="wv.cols.set.optInput" alt="보기 입력"/>' class='uniform-input text' style='width: 97%;' type='text' value='' maxlength='256'><script type='text/javascript'>"
				+"$(document).ready(function() {var inputTitle = '<u:msg titleId="cols.exam" alt="보기" />';validator.addTitle('exam', inputTitle);validator.addHandler('exam', function(id, va, eventNm){});$('#exam').bind('keyup',null);});</script></td>"
				+"<td style='display: table-cell;'><table border='0' cellspacing='0' cellpadding='0'><tbody><tr class='item2'><td class='td_btn'><input type='hidden' name='exam_img_file_id' id='exam_img_file_id'>"
				+"<a title='<u:msg titleId="wv.cols.set.addImg" alt="이미지추가"/>' class='sbutton button_small' href='javascript:setImagePop( -1 , "+$examTablelength+");'><span><u:msg titleId="wv.cols.set.addImg" alt="이미지추가"/></span></a></td></tr>"
				+"<tr><td class='td_imglt' id='exam_img_row' style='display: none;'><img id='exam_img' src='' width='58' height='70'/></td>"
				+"</tr></tbody></table></td></tr>";
	
	$examTableObj.append($data);
	$('#examArea td:nth-child(4)').hide();
	setUniformCSS();
	$("#setMulcQuesPop").css("height", "auto");

	toggleImgAddTd();
}

//보기 삭제
function examDel(){
	var checkCount = 0;
	var $examTableObj=$("#examArea");
	var $examTablelength=$("#examArea").find(".item");
	$examTableObj.find("#checkFlag").each(function(){
		if($(this).is(":checked")||$(this).attr("checked") == "checked"){
			var $selectRow=$(this).parent().parent().parent().parent().parent().parent();
			$selectRow.remove();
			$("#setMulcQuesPop").css("height", "auto");
			checkCount++;
		}
		
	});
	
	$examTablelength.find("#itemNo").each(function(){
		$(this).text('');
		tableNoCount++;
		$(this).text(tableNoCount);
		
	});
	
	if(checkCount == 0){
		alert("<u:msg titleId="wv.msg.set.chkDel" alt="체크박스를 선택 후 삭제가 가능합니다."/>");
	}
	tableNoCount =0;
}

// 선택된 보기 위로 이동
function rowUp() {
	var checkCount = 0;
	var $examTableObj=$("#examArea").find(".listtable");
	var $examTablelength=$("#examArea").find(".item");

	$examTableObj.find("#checkFlag").each(function(index){
		if($(this).is(":checked")){
			var $selectRow=$(this).parent().parent().parent().parent().parent().parent();
			if(index != 0){
				var $prevRow = $(this).parent().parent().parent().parent().parent().parent().prev();
				$prevRow.insertAfter($selectRow);
				
			}else{
				checkCount++;
				return false;
			}
			checkCount++;
			setUniformCSS();
			
		}
	});
	
	$examTablelength.find("#itemNo").each(function(){
		$(this).text('');
		tableNoCount++;
		$(this).text(tableNoCount);
		
	});
	
	if(checkCount == 0){
		alert("<u:msg titleId="wv.msg.set.chkMvUp" alt="체크박스를 선택 후 위로이동이 가능합니다."/>");
	}
	tableNoCount =0;

}

//선택된 보기 아래로 이동
function rowDown() {
	var checkCount = 0;
	var $examTableObj=$("#examArea").find(".listtable");
	var $examTablelength=$("#examArea").find(".item");
	jQuery.fn.reverse = [].reverse;
	$examTableObj.find("#checkFlag").reverse().each(function(index){
		if($(this).is(":checked")){
		
			if(index != 0){
				var $selectRow=$(this).parent().parent().parent().parent().parent().parent();
				var $nextRow = $(this).parent().parent().parent().parent().parent().parent().next();
				
				$nextRow.insertBefore($selectRow);
			}else{
				checkCount++;
				return false;
			}
			
			setUniformCSS();
			checkCount++;
		}
	});
	
	$examTablelength.find("#itemNo").each(function(){
		$(this).text('');
		tableNoCount++;
		$(this).text(tableNoCount);
		
	});
	if(checkCount == 0){
		alert("<u:msg titleId="wv.msg.set.chkMvDn" alt="체크박스를 선택 후 아래이동이 가능합니다."/>");
	}
	tableNoCount =0;
}

//"보기유형" 선택에 따른 테이블 변경
//$exam = "1": <u:msg titleId="wv.cols.set.examTypInp" alt="직접입력"/>, "2": 5지선다, "3": 7지선다, "4": Yes/No, "5": 찬성/반대
function setTable(examType){

	var $examTableObj=$("#examArea").find(".listtable");
	var $exam = $("#examTypeSelect").val();
	var before = $("#examArea").find(".item");
	//alert(before);
	
	if(examType=="1"){
		$("#examArea").find(".item").remove();
		for(var i=0; i< 2 ;i++){
			examAdd();
		}
		$("#setMulcQuesPop").css("height", "auto");
	}else if(examType=="2"){
			$("#examArea").find(".item").remove();
		for(var i=0; i< 5 ;i++){
			examAdd();
		}
		$("#setMulcQuesPop").css("height", "auto");


	}else if(examType=="3"){
			$("#examArea").find(".item").remove();
		for(var i=0; i< 7 ;i++){
			examAdd();
		}
		$("#setMulcQuesPop").css("height", "auto");
		
	}else if(examType=="4"){
		$("#examArea").find(".item").remove();
		$examTablelength=$("#examArea").find(".item").length+1;
		var $ansYes = "<tr class='item'><td class='bodybg_ct'><div class='checker' id='uniform-checkFlag'><span><input id='checkFlag' type='checkbox'></span></div></td>"
					  +"<td class='body_ct' id='itemNo'>1</td><td><input id='exam' name='exam' title='<u:msg titleId="wv.cols.set.optInput" alt="보기 입력"/>' class='uniform-input text' style='width: 97%;' type='text' value='Yes' readonly='readonly'/><script type='text/javascript'>"
					  +"$(document).ready(function() {var inputTitle = '<u:msg titleId="cols.exam" alt="보기" />';validator.addTitle('exam', inputTitle);validator.addHandler('exam', function(id, va, eventNm){});$('#exam').bind('keyup',null);});</script></td>"
					  +"<td style='display: table-cell;'><table border='0' cellspacing='0' cellpadding='0'><tbody><tr class='item2'><td class='td_btn'><input type='hidden' name='exam_img_file_id' id='exam_img_file_id'>"
					  +"<a title='<u:msg titleId="wv.cols.set.addImg" alt="이미지추가"/>' class='sbutton button_small' href='javascript:setImagePop( -1 , "+$examTablelength+");'><span><u:msg titleId="wv.cols.set.addImg" alt="이미지추가"/></span></a></td></tr>"
					  +"<tr><td class='td_imglt' id='exam_img_row' style='display: none;'><img id='exam_img' src='' width='58' height='70'/></td>"
					  +"</tr></tbody></table></td></tr>";
		var $ansNo = "<tr class='item'><td class='bodybg_ct'><div class='checker' id='uniform-checkFlag'><span><input id='checkFlag' type='checkbox'></span></div></td>"
					  +"<td class='body_ct' id='itemNo'>2</td><td><input id='exam' name='exam' title='<u:msg titleId="wv.cols.set.optInput" alt="보기 입력"/>' class='uniform-input text' style='width: 97%;' type='text' value='No' readonly='readonly'/><script type='text/javascript'>"
					  +"$(document).ready(function() {var inputTitle = '<u:msg titleId="cols.exam" alt="보기" />';validator.addTitle('exam', inputTitle);validator.addHandler('exam', function(id, va, eventNm){});$('#exam').bind('keyup',null);});</script></td>"
					  +"<td style='display: table-cell;'><table border='0' cellspacing='0' cellpadding='0'><tbody><tr class='item2'><td class='td_btn'><input type='hidden' name='exam_img_file_id' id='exam_img_file_id'>"
					  +"<a title='<u:msg titleId="wv.cols.set.addImg" alt="이미지추가"/>' class='sbutton button_small' href='javascript:setImagePop( -1 , "+$examTablelength+");'><span><u:msg titleId="wv.cols.set.addImg" alt="이미지추가"/></span></a></td></tr>"
					  +"<tr><td class='td_imglt' id='exam_img_row' style='display: none;'><img id='exam_img' src='' width='58' height='70'/></td>"
					  +"</tr></tbody></table></td></tr>";
		$examTableObj.append($ansYes);
		$examTableObj.append($ansNo);
		$('#examArea td:nth-child(4)').hide();
		$("#setMulcQuesPop").css("height", "auto");
		setUniformCSS();
		toggleImgAddTd();
		
	}else if(examType=="5"){
		$("#examArea").find(".item").remove();
		$examTablelength=$("#examArea").find(".item").length+1;
		var $ansAgr = "<tr class='item'><td class='bodybg_ct'><div class='checker' id='uniform-checkFlag'><span><input id='checkFlag' type='checkbox'></span></div></td>"
					  +"<td class='body_ct' id='itemNo'>1</td><td><input id='exam' name='exam' title='<u:msg titleId="wv.cols.set.optInput" alt="보기 입력"/>' class='uniform-input text' style='width: 97%;' type='text' value='찬성' readonly='readonly'/><script type='text/javascript'>"
					  +"$(document).ready(function() {var inputTitle = '<u:msg titleId="cols.exam" alt="보기" />';validator.addTitle('exam', inputTitle);validator.addHandler('exam', function(id, va, eventNm){});$('#exam').bind('keyup',null);});</script></td>"
					  +"<td style='display: table-cell;'><table border='0' cellspacing='0' cellpadding='0'><tbody><tr class='item2'><td class='td_btn'><input type='hidden' name='exam_img_file_id' id='exam_img_file_id'>"
					  +"<a title='<u:msg titleId="wv.cols.set.addImg" alt="이미지추가"/>' class='sbutton button_small' href='javascript:setImagePop( -1 , "+$examTablelength+");'><span><u:msg titleId="wv.cols.set.addImg" alt="이미지추가"/></span></a></td></tr>"
					  +"<tr><td class='td_imglt' id='exam_img_row' style='display: none;'><img id='exam_img' src='' width='58' height='70'/></td>"
					  +"</tr></tbody></table></td></tr>";
					  
		var $ansDisarg = "<tr class='item'><td class='bodybg_ct'><div class='checker' id='uniform-checkFlag'><span><input id='checkFlag' type='checkbox'></span></div></td>"
					  +"<td class='body_ct' id='itemNo'>2</td><td><input id='exam' name='exam' title='<u:msg titleId="wv.cols.set.optInput" alt="보기 입력"/>' class='uniform-input text' style='width: 97%;' type='text' value='반대' readonly='readonly'/><script type='text/javascript'>"
					  +"$(document).ready(function() {var inputTitle = '<u:msg titleId="cols.exam" alt="보기" />';validator.addTitle('exam', inputTitle);validator.addHandler('exam', function(id, va, eventNm){});$('#exam').bind('keyup',null);});</script></td>"
					  +"<td style='display: table-cell;'><table border='0' cellspacing='0' cellpadding='0'><tbody><tr class='item2'><td class='td_btn'><input type='hidden' name='exam_img_file_id' id='exam_img_file_id'>"
					  +"<a title='<u:msg titleId="wv.cols.set.addImg" alt="이미지추가"/>' class='sbutton button_small' href='javascript:setImagePop( -1 , "+$examTablelength+");'><span><u:msg titleId="wv.cols.set.addImg" alt="이미지추가"/></span></a></td></tr>"
					  +"<tr><td class='td_imglt' id='exam_img_row' style='display: none;'><img id='exam_img' src='' width='58' height='70'/></td>"
					  +"</tr></tbody></table></td></tr>";
					  
					  
		$examTableObj.append($ansAgr);
		$examTableObj.append($ansDisarg);
		$('#examArea td:nth-child(4)').hide();
		$("#setMulcQuesPop").css("height", "auto");
		setUniformCSS();
		toggleImgAddTd();
	}else{
	}
	
	//보기 유형에 따른 보기 컨트롤 버튼 Display 조정
	if(examType == "1"){
		$('#etcAddBtn').css("display", "inline"); 
		$('#examAddBtn').css("display", "inline"); 
		$('#examDelBtn').css("display", "inline");
		$('#upBtn').css("display", "inline");
		$('#downBtn').css("display", "inline");
	}else if(examType == "2" || examType == "3"){
		$('#etcAddBtn').css("display", "none"); 
		$('#examAddBtn').css("display", "none"); 
		$('#examDelBtn').css("display", "none");
		$('#upBtn').css("display", "inline");
		$('#downBtn').css("display", "inline");
	}else{
		$('#etcAddBtn').css("display", "none"); 
		$('#examAddBtn').css("display", "none"); 
		$('#examDelBtn').css("display", "none");
		$('#upBtn').css("display", "none");
		$('#downBtn').css("display", "none");
	}
	
}

//사진 변경 - 팝업 오픈 
function setImagePop(quesNum, examNum){
	var url='./setImagePop.do?menuId=${menuId}&quesNum='+quesNum+'&examNum='+examNum;
	dialog.open('setImageDialog','<u:msg titleId="or.jsp.setOrg.photoTitle" alt="사진 선택" />',url);
	
}
//사진 변경 - 후처리 
function setImage( quesNum, examNum, fileId, fileNm, filePath){
	//alert(quesNum+"  "+examNum+"  "+fileId+"  "+fileNm);
	var $examTableObj=$("#examArea").find(".listtable");
	if(quesNum==-1&&examNum==-1){
		$("#mulcQuesArea").find("#ques_img_nm").html(fileNm);
		$("#mulcQuesArea").find("#ques_img_file_id").val(fileId);
		$("#mulcQuesArea").find("#ques_img").attr("src",filePath);
		$("#mulcQuesArea").find("#ques_img_row").show();
		$("#mulcQuesArea").find("#ques_img_mod_row").hide();
		 
		
	}else{
	 	$examTableObj.find(".item").each(function(row){
	 		if((row+1) == examNum){
		 		$(this).find("#exam_img_file_id").val(fileId);
				$(this).find("#exam_img").attr("src",filePath);
				$(this).find("#exam_img_row").show();
	 		}
	 		
	 	});

		//var $examTr = examTableObj.(tr:eq("+ examNum +"));
		//alert($examTr + "examNum = " + examNum);
		//$examTr.remove();
		//$examTr.find("#exam_img_file_id").val(fileId);
		//alert($examTr.find("#exam_img_file_id").val());
		//$examTr.find("#exam_img").attr("src",filePath);
		//$examTr.find("#exam_img_row").show();
	}
	
	$("#setMulcQuesPop").css("height", "auto");
	dialog.close('setImageDialog');
}

$(document).ready(function() {
	//$('#examArea th:nth-child(4)').hide();
	//$('#examArea th:nth-child(4)').hide();
	if("${fnc}" != 'mod'){
		examAdd();
	}
	toggleImgAddTd();
	
	//객관식 보기 추가 시 글자수 입력 제한 Alert
	$("#setMulcQuesPopForm").find("input[name^='exam']").keyup(function(){
        if ($(this).val().length >= $(this).attr('maxlength')) {
            alertMsg('wv.jsp.viewSurv.maxLengthAlert'); <%//wv.jsp.viewSurv.maxLengthAlert= 제한 글자 수를 초과하였습니다. %>
            $(this).val($(this).val().substr(0, $(this).attr('maxlength')));
        }
    });
	
	//examAdd();
});
//-->
</script>

<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />
<u:set test="${fnc == 'mod'}" var="subj" value="${ctsQuesVo.quesCont}" elseValue="" />
<u:set test="${fnc == 'mod'}" var="imagePath" value="${ctsQuesVo.imgSavePath}" elseValue="" />
<u:set test="${fnc == 'mod'}" var="examChoiItemCd" value="${ctsQuesVo.examChoiItemCd}" elseValue="" />
<u:set test="${fnc == 'mod'}" var="inputYn" value="${ctQExamList[0].inputYn}" elseValue="" />
<u:set test="${fnc == 'mod'}" var="mulChoiYn" value="${ctsQuesVo.mulChoiYn}" elseValue="" />
<u:set test="${fnc == 'mod'}" var="examImgUseYn" value="${ctQExamList[0].examImgUseYn}" elseValue="" />
<u:set test="${fnc == 'mod'}" var="mandaReplyYn" value="${ctsQuesVo.mandaReplyYn}" elseValue="" />


<div style="width:800px;">
<form id="setMulcQuesPopForm">
<input id="exam" type="hidden"/>
<input id="survId"  name="survId" type="hidden" value="${survId}"/>

<% // 표 %>
<u:listArea id="mulcQuesArea">
	<tr>
	<td width="18%" class="head_lt"><u:mandatory /><u:msg titleId="cols.quesSubj" alt="질문제목" /></td>
	<td colspan="3"><u:input id="subj" value="${subj}" titleId="cols.subj" style="width: 98%;"   mandatory="Y"  maxByte="240"/></td>
	</tr>

	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.quesImg" alt="질문이미지" /></td>
	<td colspan="3">
	
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<u:msg titleId="cm.msg.preparing" var="msg" alt="준비중.." />
			<td>				
				<input type="hidden" id="ques_img_file_id" name="ques_img_file_id" value="${ctsQuesVo.imgSurvFileId}">
				<u:buttonS id="quesImgAdd" href="javascript:setImagePop(-1,-1);" titleId="wv.btn.imgAdd" alt="이미지추가" />
			</td>
		</tr>
		<tr>
			<td class="td_imglt" id="ques_img_row" style="display: none;"><img id="ques_img" src="" width="58" height="70"/></td>
		</tr>
		<c:if test="${fnc == 'mod'}">
			<tr>
				<u:set test="${empty ctsQuesVo.imgSurvFileId}" var="subjDisp" value="none" elseValue="block" />
				<td class="td_imglt" style="display: ${subjDisp};"  id="ques_img_mod_row"><img src="${_ctx}${imagePath}" width="58" height="70"/></td>
			</tr>
		</c:if>
		</table></td>
	</tr>

	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="wv.cols.examTyp" alt="보기유형" /></td>
	<td colspan="1"><select id="examTypeSelect" name="examTypeSelect" onchange = "javascript:setTable(this.value);">
		<option value="1"><u:msg titleId="wv.cols.set.examTypInp" alt="직접입력"/></option>
		<option value="2"><u:msg titleId="wv.cols.set.examTyp5" alt="5지선"/></option>
		<option value="3"><u:msg titleId="wv.cols.set.examTyp7" alt="7지선"/></option>
		<option value="4">Yes/No</option>
		<option value="5"><u:msg titleId="wv.cols.set.examTypAprv" alt="찬성/반대"/></option>
		</select>
	</td>
	
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

	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="wv.cols.examOpt" alt="보기옵션" /></td>
		<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			
			<c:if test="${fnc == 'mod'}">
				<u:set test="${inputYn == 'Y'}" var="ansAdd" value="true" elseValue="false" />
				<u:set test="${mulChoiYn == 'Y'}" var="mulChoiY" value="true" elseValue="false" />
				<u:set test="${mulChoiYn == 'N'}" var="mulChoiN" value="true" elseValue="false" />
				<u:set test="${examImgUseYn == '1'}" var="imgAdd" value="true" elseValue="false" />
				
			</c:if>
			<c:if test="${fnc != 'mod'}">
				<c:set  var="mulChoiN" value="true" />
			</c:if>
			
			<u:checkbox name="ansAdd" value="Y" titleId="wv.cols.ansAdd" alt="답변추가" inputClass="bodybg_lt" checked="${ansAdd}" />
			<td class="width10"></td>
			<u:checkbox name="imgAdd" value="1" titleId="wv.cols.imgAdd" alt="이미지추가" onclick="toggleImgAddTd();" checked="${imgAdd}" inputClass="bodybg_lt"  />
			</tr>
			</tbody></table>
		</td>
		<td width="18%" class="head_lt"><u:msg titleId="cols.mulChoiYn" alt="복수선택가능여부" /></td>
		<td><u:checkArea>
			<u:radio name="mulChoiYn" value="Y" title="Y" checked="${mulChoiY}"/>
			<u:radio name="mulChoiYn" value="N" title="N" checked="${mulChoiN}" />
			</u:checkArea>
		</td>
	</tr>
</u:listArea>

<div class="titlearea">
	<div class="tit_left">
	<dl>
	<dd class="title_s"><u:msg titleId="wv.cols.examInput" alt="보기입력" /></dd>
	</dl>
	</div>
	<div class="tit_right">
	<ul>
	<li class="btn">
		<u:msg titleId="cm.msg.preparing" var="msg" alt="준비중.." />
		<!-- 
		<u:buttonS id="etcAddBtn" href="javascript:alert('${msg}');" titleId="wv.btn.etcAdd" alt="기타추가"/>
		 -->
		<u:buttonS id="examAddBtn" href="javascript:examAdd();" titleId="wv.btn.examAdd" alt="보기추가"/>
		<u:buttonS id="examDelBtn" href="javascript:examDel();" titleId="wv.btn.examDel" alt="보기삭제"/>
		<u:buttonS id="upBtn" href="javascript:rowUp();" titleId="cm.btn.up" alt="위로이동"/>
		<u:buttonS id="downBtn" href="javascript:rowDown();" titleId="cm.btn.down" alt="아래로이동"/></li>
	</ul>
	</div>
</div>
<input type="hidden" id="checkFlag"/>

<!-- 질문 S -->
<u:listArea id="examArea">
	<tr>
		<th width="40" class="head_ct"><input type="checkbox" id="checkFlagAll" onclick="javascript:checkAll();"/></th>
		<th width="60" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></th>
		<th class="head_ct"><u:msg titleId="cols.exam" alt="보기" /></th>
		<th width="200" class="head_ct"><u:msg titleId="wv.cols.examImg" alt="보기이미지" /></th>
	</tr>
	<c:if test="${fnc == 'mod'}">
	<c:forEach  var="ctsQue" items="${ctQExamList}"  varStatus="status">
	
		<tr class='item'>
			<td class='bodybg_ct'>
				<div class='checker' id='uniform-checkFlag'>
					<span>
						<input id='checkFlag' name='checkFlag' type='checkbox'>
					</span>
				</div>
			</td>
			<td class='body_ct' id='itemNo'>${status.count}</td>
			<td>
				<u:input id='exam' name='exam' titleId='wv.cols.examInput' style='width: 97%;' type='text' value='${ctsQue.examDispNm}' maxLength='256'/>
				<script type='text/javascript'>
					$(document).ready(function() 
							{
								var inputTitle = '<u:msg titleId="cols.exam" alt="보기" />';
									validator.addTitle('exam', inputTitle);
									validator.addHandler('exam', function(id, va, eventNm){
									});
									$('#exam').bind('keyup',null);});
				</script>
			</td>
			<td style='display: table-cell;'>
				<table border='0' cellspacing='0' cellpadding='0'>
				<tbody>
					<tr class='item2'>
						<td class='td_btn'>
							<input type='hidden' name='exam_img_file_id' id='exam_img_file_id' value="${ctsQue.imgSurvFileId}">
								<a title='<u:msg titleId="wv.cols.set.addImg" alt="이미지추가"/>' class='sbutton button_small' href='javascript:setImagePop( -1 , "${status.count}");'><span><u:msg titleId="wv.cols.set.addImg" alt="이미지추가"/></span>
								</a>
						</td>
					</tr>
					<tr>
						<u:set test="${empty ctsQue.imgSurvFileId}" var="disp" value="none" elseValue="block" />
						<td class='td_imglt' id='exam_img_row' style='display: ${disp};'><img id='exam_img' src='${ctsQue.imgSavePath}' width='58' height='70'/>
						
						</td>
					</tr>
				</tbody>
				</table>
			</td>
		</tr>
	</c:forEach>
	</c:if>
	
</u:listArea>
<!-- 질문 E -->

<u:blank />

<% // 하단 버튼 %>
<u:buttonArea>
	<c:if test="${!empty authChkW && authChkW == 'W' }">
		<u:button onclick="mulcSubmitForm();" titleId="cm.btn.save" alt="저장" />
	</c:if>
	<c:if test="${!empty authChkR && authChkR == 'R' }">
		<u:button onclick="dialog.close(this);" titleId="cm.btn.close" alt="닫기" />
	</c:if>
	
</u:buttonArea>

<u:blank />
</form>
</div>
