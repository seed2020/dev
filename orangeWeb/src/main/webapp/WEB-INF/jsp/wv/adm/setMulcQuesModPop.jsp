<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--

var tableNoCount = 0;
var tableId;
	
$(function() {
	var $examTableObj=$("#examArea").find(".listtable");
	var quesInfo = mulcQuesInfo('${tblId}');
	var quesExam = quesInfo.quesExam;
	tableId = "${tblId}";
	
	$("input[id='ques_img_file_id']").val(quesInfo.quesImgFileIdVal);
	$("input[id='ques_img_file_id']").attr("id", quesInfo.quesImgFileId);
	$("td[id='ques_img_row']").attr("id", quesInfo.quesImgRowId);
	$("img[id='ques_img']").attr("id", quesInfo.quesImgNo);
	$("img[id='ques_img']").attr("src", quesInfo.quesImgFilePath);
	if($("img[id='ques_img']").attr("src")!="-1"){
		$("tr[id='mulcQuesImg']").attr("style", "display:block;");
	}else{
		$("tr[id='mulcQuesImg']").attr("style", "display: none;");
	}

	quesExam.each(function(index){
		examAdd(index);
		
		$("#exam" + index).val(quesExam[index]);
		$("input[id='exam_img_file_id"+index+"']").attr("id", quesInfo.examImgFileIds[index]);
		$("input[id='exam_img_file_id"+index+"']").val(quesInfo.examImgFileIdVals[index]);
		$("td[id='exam_img_row"+index+"']").attr("id", quesInfo.examImgRowIds[index]);
		$("img[id='exam_img"+index+"']").attr("id", quesInfo.examImgNos[index]);
		$("img[id='exam_img"+index+"']").attr("src", quesInfo.examImgFilePaths[index]);
		if($("img[id='exam_img"+index+"']").attr("src") == "-1"){
			$("#examImgInfo"+index).attr("style", "display: none;");
		}
			

	});
	$("#subj").val(quesInfo.quesSubj);
	
	if($("img[id='ques_img']").attr("src")!=""){
		$("td[id='ques_img_row']").attr("style", "display:block;");
	}
	
	$("#examArea").find("img[id^='exam_img']").each(function(){
		if($(this).attr("src")!=""){
			$(this).attr("style", "display:block;");
			$("#examArea").find("td[id^='exam_img_row']").each(function(){
				$(this).attr("style", "display:block;");
			});
			
		}
	});
	
	var ansAddVal = new String(quesInfo.quesAnsAdd);
	
	if(ansAddVal == 'Y'){
		$("input[name='ansAdd']").trigger('click');
	}else{
		$("input[name='ansAdd']").parent().attr("class", "");
	}
	
	if(quesInfo.quesImgAdd == '1'){
		$("input[name='imgAdd']").trigger('click');
		$("td[id^='exam_img_row']").attr("style", "display:block;");
	}else{
		$("input[name='imgAdd']").parent().attr("class", "");
	}
	if(quesInfo.quesMulChoiYn == 'Y'){
		$("input[id='mulChoiYnY']").trigger('click');
		
	}else{
		$("input[id='mulChoiYnN']").trigger('click');
	}
	
	
	
 });
 
<%//사진 변경 - 팝업 오픈 %>
function setImagePop(quesNum, examNum){
	var url='./setImagePop.do?menuId=${menuId}&quesNum='+quesNum+'&examNum='+examNum;
	dialog.open('setImageDialog','<u:msg titleId="or.jsp.setOrg.photoTitle" alt="사진 선택" />',url);
	
}
<%//사진 변경 - 후처리 %>
function setImage( quesNum, examNum, fileId, fileNm, filePath){
	//alert(quesNum+"  "+examNum+"  "+fileId+"  "+fileNm);
	var $examTableObj=$("#examArea").find(".listtable");
	if(quesNum==-1&&examNum==-1){
		$("#mulcQuesArea").find("#ques_img_nm").html(fileNm);
		$("#mulcQuesArea").find("#ques_img_file_id").val(fileId);
		$("#mulcQuesArea").find("#ques_img").attr("src",filePath);
		$("#mulcQuesArea").find("#ques_img_row").show();
		$("#mulcQuesArea").find("#mulcQuesImg").attr("style", "display: block;");
		
	}else{
		$examTableObj.find("#exam_img_nm"+examNum).html(fileNm);
		$examTableObj.find("#exam_img_file_id"+examNum).val(fileId);
		$examTableObj.find("#exam_img"+examNum).attr("src",filePath);
		$examTableObj.find("#exam_img_row"+examNum).show();
		$examTableObj.find("#examImgInfo"+examNum).attr("style", "display: block;");

	}
	
	$("#setMulcQuesModPop").css("height", "auto");
	dialog.close('setImageDialog');
}
	
// 수정된 객관식 질문 저장
function saveMulcQues(){
	var arr = [];
	//질문정보
	var mulcQuesImgInfo = [];
	
	//보기 정보
	var examArr = [];
	var examImgfileIdArr = [];
	var examImgfileIdValArr = [];
	var examImgRowArr = [];
	var examImgNoArr = [];
	var examImgFilePathArr = [];
	
	var $examTableObj=$("#examArea").find(".listtable");
	var $examTablelength=$("#examArea").find(".item");
	var $cheeckFlag = $examTableObj.find("#checkFlag");
	var $ansAdd = 'N';
	var $imgAdd = '0';
	
	mulcQuesImgInfo.push($("#mulcQuesArea").find("input[id='ques_img_file_id']").val());
	mulcQuesImgInfo.push($("#mulcQuesArea").find("td[id='ques_img_row']").attr("id"));
	mulcQuesImgInfo.push($("#mulcQuesArea").find("img[id='ques_img']").attr("id"));
	if($("#mulcQuesArea").find("img[id^='ques_img']").attr("src") == ""){
		mulcQuesImgInfo.push("-1");
	}else{
		mulcQuesImgInfo.push($("#mulcQuesArea").find("img[id^='ques_img']").attr("src"));
	}
	mulcQuesImgInfo.push($("#mulcQuesArea").find("input[id='ques_img_file_id']").attr("id"));
	
	if($(':checkbox[name="ansAdd"]').is(":checked")){
		$ansAdd = 'Y';
	}
	
	if($(':checkbox[name="imgAdd"]').is(":checked")){
		$imgAdd = '1';
	}
	
	//var $selectRow=$cheeckFlag.parent().parent().parent().parent().parent().parent();
	
	$examTablelength.each(function(index){
		examArr.push($("#exam"+index).val());
	});
	
	$examTablelength.each(function(index){
		examImgfileIdArr.push($("#examArea").find("input[id='exam_img_file_id"+index+"']").attr("id"));
		if($("#examArea").find("input[id='exam_img_file_id"+index+"']").val() == ""){
			examImgfileIdValArr.push("-1");
		}else{
			examImgfileIdValArr.push($("#examArea").find("input[id='exam_img_file_id"+index+"']").val());
		}
	});
	
	$examTablelength.each(function(index){
		examImgRowArr.push($("#examArea").find("td[id='exam_img_row"+index+"']").attr("id"));
	});
	
	$examTablelength.each(function(index){
		examImgNoArr.push($("#examArea").find("img[id='exam_img"+index+"']").attr("id"));
		if($("#examArea").find("img[id='exam_img"+index+"']").attr("src") == ""){
			examImgFilePathArr.push("-1");
		}else{
			examImgFilePathArr.push($("#examArea").find("img[id='exam_img"+index+"']").attr("src"));

		}
	});	
	alert(examImgfileIdValArr);
	alert(examImgFilePathArr);
	arr.push({tblId:tableId,
				subj:$("#subj").val(),
				quesImgAdd:$(':radio[name="repetKnd"]:checked').val(), 
				examTypeSelect: $("#examTypeSelect").val(),
				ansAdd:$ansAdd,
				imgAdd:$imgAdd,
				mulcQuesImgInfo:mulcQuesImgInfo,
				mulChoiYn: $(":radio[name='mulChoiYn']:checked").val(),
				examArea:$("#examArea").val(),
				examCount:$("#examArea").find(".item").length,
				examArray:examArr,
				examImgfileIdArr:examImgfileIdArr,
				examImgfileIdValArr:examImgfileIdValArr,
				examImgRowArr:examImgRowArr,
				examImgNoArr:examImgNoArr,
				examImgFilePathArr:examImgFilePathArr
			});
	setMulcQuesMod(arr);
	dialog.close('setMulcQuesModPop');

}

//이미지 추가 버튼 클릭시 이미지 추가 열 show & hide
function toggleImgAddTd() {
	if ($('input:checkbox[name="imgAdd"]').is(":checked")) {
		$('#examArea th:nth-child(4)').show();
		$('#examArea td:nth-child(4)').show();
	} else {
		$('#examArea th:nth-child(4)').hide();
		$('#examArea td:nth-child(4)').hide();
	}
}

//체크박스 전체 선택
function checkAll(){
	if($("#checkFlagAll").is(":checked")){

		$("input[name='checkFlag']:checkbox").each(function(){
			$(this).parent().attr("class", "checked");
		});
		
	}else{
		$("input[name='checkFlag']:checkbox").each(function(){
			$(this).parent().attr("class", "");
		});
	}
}

//보기 추가
function examAdd(index){
	var $examTableObj=$("#examArea").find(".listtable");
	var $examTablelength=$("#examArea").find(".item").length+1;
	var $data = "<tr class='item'><td class='bodybg_ct'><div class='checker' id='uniform-checkFlag'>"
				+"<span><input id='checkFlag' name='checkFlag' type='checkbox'></span></div></td><td class='body_ct' id='itemNo'>"+$examTablelength+"</td>"
				+"<td><input id='exam"+index+"' name='exam"+index+"' title='보기 입력' class='uniform-input text' style='width: 97%;' type='text' ><script type='text/javascript'>"
				+"$(document).ready(function() {var inputTitle = '<u:msg titleId="cols.exam" alt="보기" />';validator.addTitle('exam', inputTitle);validator.addHandler('exam', function(id, va, eventNm){});$('#exam').bind('keyup',null);});</script></td>"
				+"<td style='display: table-cell;'><table border='0' cellspacing='0' cellpadding='0'><tbody><tr class='item2'><td class='td_btn'>"
				+"<a title='이미지추가' class='sbutton button_small' href='javascript:setImagePop( -1 , "+index+");'><span>이미지추가</span></a></td></tr>"
				+"<tr id='examImgInfo"+index+"'><td class='td_imglt' id='exam_img_row"+index+"' style='display: none;'><img id='exam_img"+index+"' src='' width='58' height='70'/><input type='hidden' id='exam_img_file_id"+index+"'></td>"
				+"</tr></tbody></table></td></tr>";
	
	$examTableObj.append($data);
	$('#examArea td:nth-child(4)').hide();
	setUniformCSS();
	$("#setMulcQuesModPop").css("height", "auto");

}

//보기 삭제
function examDel(){
	var $examTableObj=$("#examArea");
	var $examTablelength=$("#examArea").find(".item");
	$examTableObj.find("#checkFlag").each(function(){
		if($(this).is(":checked")){
			var $selectRow=$(this).parent().parent().parent().parent().parent().parent();
			$selectRow.remove();
			$("#setMulcQuesModPop").css("height", "auto");
		}
		
	});
	
	$examTablelength.find("#itemNo").each(function(){
		$(this).text('');
		tableNoCount++;
		$(this).text(tableNoCount);
		
	});
	tableNoCount =0;
}

// 선택된 보기 위로 이동
function rowUp() {
	var $examTableObj=$("#examArea").find(".listtable");
	var $examTablelength=$("#examArea").find(".item");

	$examTableObj.find("#checkFlag").each(function(index){
		if($(this).is(":checked")){
			var $selectRow=$(this).parent().parent().parent().parent().parent().parent();
			if(index != 0){
				var $prevRow = $(this).parent().parent().parent().parent().parent().parent().prev();
				$prevRow.insertAfter($selectRow);
			}
			setUniformCSS();
		}
	});
	
	$examTablelength.find("#itemNo").each(function(){
		$(this).text('');
		tableNoCount++;
		$(this).text(tableNoCount);
		
	});
	tableNoCount =0;

}

//선택된 보기 아래로 이동
function rowDown() {
	var $examTableObj=$("#examArea").find(".listtable");
	var $examTablelength=$("#examArea").find(".item");
	
	$examTableObj.find("#checkFlag").each(function(){
		if($(this).is(":checked")){
			var $selectRow=$(this).parent().parent().parent().parent().parent().parent();
			var $nextRow = $(this).parent().parent().parent().parent().parent().parent().next();
			$nextRow.insertBefore($selectRow);
			setUniformCSS();
		}
	});
	
	$examTablelength.find("#itemNo").each(function(){
		$(this).text('');
		tableNoCount++;
		$(this).text(tableNoCount);
		
	});
	tableNoCount =0;
}

// "보기유형" 선택에 따른 테이블 변경
//$exam = "1": 직접입력, "2": 5지선다, "3": 7지선다, "4": Yes/No, "5": 찬성/반대
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
		$("#setMulcQuesModPop").css("height", "auto");
	}else if(examType=="2"){
			$("#examArea").find(".item").remove();
		for(var i=0; i< 5 ;i++){
			examAdd();
		}
		$("#setMulcQuesModPop").css("height", "auto");


	}else if(examType=="3"){
			$("#examArea").find(".item").remove();
		for(var i=0; i< 7 ;i++){
			examAdd();
		}
		$("#setMulcQuesModPop").css("height", "auto");
		
	}else if(examType=="4"){
		$("#examArea").find(".item").remove();
		$examTablelength=$("#examArea").find(".item").length+1;
		var $ansYes = "<tr class='item'><td class='bodybg_ct'><div class='checker' id='uniform-checkFlag'><span><input id='checkFlag' type='checkbox'></span></div></td>"
					  +"<td class='body_ct' id='itemNo'>1</td><td class='body_lt'>Yes<script type='text/javascript'>"
					  +"$(document).ready(function() {var inputTitle = '<u:msg titleId="cols.exam" alt="보기" />';validator.addTitle('exam', inputTitle);validator.addHandler('exam', function(id, va, eventNm){});$('#exam').bind('keyup',null);});</script></td>"
					  +"<td style='display: table-cell;'><table border='0' cellspacing='0' cellpadding='0'><tbody><tr class='item2'><td class='td_btn'><a title='이미지추가' class='sbutton button_small' href='javascript:void(0);'><span>이미지추가</span></a></td></tr></tbody></table></td></tr>";
		var $ansNo = "<tr class='item'><td class='bodybg_ct'><div class='checker' id='uniform-checkFlag'><span><input id='checkFlag' type='checkbox'></span></div></td>"
					  +"<td class='body_ct' id='itemNo'>2</td><td class='body_lt'>No<script type='text/javascript'>"
					  +"$(document).ready(function() {var inputTitle = '<u:msg titleId="cols.exam" alt="보기" />';validator.addTitle('exam', inputTitle);validator.addHandler('exam', function(id, va, eventNm){});$('#exam').bind('keyup',null);});</script></td>"
					  +"<td style='display: table-cell;'><table border='0' cellspacing='0' cellpadding='0'><tbody><tr class='item2'><td class='td_btn'><a title='이미지추가' class='sbutton button_small' href='javascript:void(0);'><span>이미지추가</span></a></td></tr></tbody></table></td></tr>";
		$examTableObj.append($ansYes);
		$examTableObj.append($ansNo);
		$('#examArea td:nth-child(4)').hide();
		$("#setMulcQuesModPop").css("height", "auto");
		setUniformCSS();
		
	}else if(examType=="5"){
		$("#examArea").find(".item").remove();
		$examTablelength=$("#examArea").find(".item").length+1;
		var $ansAgr = "<tr class='item'><td class='bodybg_ct'><div class='checker' id='uniform-checkFlag'><span><input id='checkFlag' type='checkbox'></span></div></td>"
					  +"<td class='body_ct' id='itemNo'>1</td><td class='body_lt'>찬성<script type='text/javascript'>"
					  +"$(document).ready(function() {var inputTitle = '<u:msg titleId="cols.exam" alt="보기" />';validator.addTitle('exam', inputTitle);validator.addHandler('exam', function(id, va, eventNm){});$('#exam').bind('keyup',null);});</script></td>"
					  +"<td style='display: table-cell;'><table border='0' cellspacing='0' cellpadding='0'><tbody><tr class='item2'><td class='td_btn'><a title='이미지추가' class='sbutton button_small' href='javascript:void(0);'>"
					  +"<span>이미지추가</span></a></td></tr></tbody></table></td></tr>";
		var $ansDisarg = "<tr class='item'><td class='bodybg_ct'><div class='checker' id='uniform-checkFlag'><span><input id='checkFlag' type='checkbox'></span></div></td>"
					  +"<td class='body_ct' id='itemNo'>2</td><td class='body_lt'>반대<script type='text/javascript'>"
					  +"$(document).ready(function() {var inputTitle = '<u:msg titleId="cols.exam" alt="보기" />';validator.addTitle('exam', inputTitle);validator.addHandler('exam', function(id, va, eventNm){});$('#exam').bind('keyup',null);});</script></td>"
					  +"<td style='display: table-cell;'><table border='0' cellspacing='0' cellpadding='0'><tbody><tr class='item2'><td class='td_btn'><a title='이미지추가' class='sbutton button_small' href='javascript:void(0);'>"
					  +"<span>이미지추가</span></a></td></tr></tbody></table></td></tr>";
		$examTableObj.append($ansAgr);
		$examTableObj.append($ansDisarg);
		$('#examArea td:nth-child(4)').hide();
		$("#setMulcQuesModPop").css("height", "auto");
		setUniformCSS();
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

$(document).ready(function() {
	$('#examArea th:nth-child(4)').hide();
	$('#examArea td:nth-child(4)').hide();
	
	if($("img[id^='exam_img']").attr("src")!=""){
		$('#examArea th:nth-child(4)').show();
		$('#examArea td:nth-child(4)').show();
		$("#setMulcQuesPop").css("height", "auto");
	}

});
//-->
</script>

<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />
<u:set test="${fnc == 'mod'}" var="subj" value="점심 메뉴에 대한 설문" elseValue="" />
<u:set test="${fnc == 'mod'}" var="selected" value=" selected" elseValue="" />

<div style="width:800px;">

<% // 표 %>
<u:listArea id="mulcQuesArea">
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.quesSubj" alt="질문제목" /></td>
	<td colspan="3"><u:input id="subj" value="${subj}" titleId="cols.subj" style="width: 98%;" /></td>
	</tr>

	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.quesImg" alt="질문이미지" /></td>
	<td colspan="3">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<u:msg titleId="cm.msg.preparing" var="msg" alt="준비중.." />
				<td>				
					<input type="hidden" id="ques_img_file_id">
					<u:buttonS id="quesImgAdd" href="javascript:setImagePop(-1,-1);" titleId="wv.btn.imgAdd" alt="이미지추가" />
				</td>
			</tr>
			<tr id="mulcQuesImg">
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

	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="wv.cols.examTyp" alt="보기유형" /></td>
	<td colspan="3"><select id="examTypeSelect" onchange = "javascript:setTable(this.value);">
		<option value="1">직접입력</option>
		<option value="2">5지선</option>
		<option value="3">7지선</option>
		<option value="4">Yes/No</option>
		<option value="5">찬성/반대</option>
		</select></td>
	</tr>

	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="wv.cols.examOpt" alt="보기옵션" /></td>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<u:checkbox name="ansAdd" value="1" titleId="wv.cols.ansAdd" alt="답변추가" inputClass="bodybg_lt" />
		<td class="width10"></td>
		<u:checkbox name="imgAdd" value="1" titleId="wv.cols.imgAdd" alt="이미지추가" onclick="toggleImgAddTd()" inputClass="bodybg_lt" />
		</tr>
		</tbody></table></td>
	<td width="18%" class="head_lt"><u:msg titleId="cols.mulChoiYn" alt="복수선택가능여부" /></td>
	<td><u:checkArea>
		<u:radio name="mulChoiYn" value="Y" titleId="cm.option.posb" checked=""/>
		<u:radio name="mulChoiYn" value="N" titleId="cm.option.imps" checked="true" />
		</u:checkArea></td>
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
</u:listArea>

<!-- 질문 E -->

<u:blank />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:msg titleId="cm.msg.save.success" alt="저장 되었습니다." var="msg" />
	<u:button onclick="saveMulcQues();" titleId="cm.btn.save" alt="저장" auth="W" />
	<u:button onclick="dialog.close(this);" titleId="cm.btn.close" alt="닫기" auth="R" />
</u:buttonArea>

<u:blank />

</div>
