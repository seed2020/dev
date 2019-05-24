<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function popOnClose(){
	dialog.close(this);
};

//저장
function saveCd(){
	getIframeContent('frameList').saveCd();
};

//저장 버튼 활성화
function btnDipYn( dispVal ){
	if(dispVal == 'hide'){
		$('#saveBtn').hide();	
	}else{
		$('#saveBtn').show();
	}
};

function saveUpload(){
	if(validator.validate('uploadForm') ){
		btnDipYn('hide');
		var $form = $("#uploadForm");
		$form.attr('target','frameList');
		$form.attr('action','/cm/cdExcelUploadFrm.do?menuId=${menuId}');
		$form[0].submit();
	}
};
//-->
</script>
<div style="width:700px;height:750px;">
<form id="uploadForm" method="post" enctype="multipart/form-data" >
	<input type="hidden" name="pageRowCnt" value="20"/>
	<u:listArea>
		<tr>
		<th width="30%" class="head_lt"><u:mandatory /><u:msg alt="파일첨부" titleId="cm.btn.fileAtt" /></th>
		<td width="70%"><u:file id="excelFile" titleId="cm.btn.fileAtt" alt="파일첨부" mandatory="Y" exts="xls,xlsx"/></td>
		</tr>
		<tr>
		<th width="30%" class="head_lt"><u:mandatory /><u:msg titleId="cols.clsCd" alt="분류코드"/></th>
		<td width="70%"><u:input id="clsCd" name="clsCd" titleId="cols.clsCd" maxByte="30" mandatory="Y" /><u:buttonS titleId="cm.btn.fileIn" onclick="saveUpload();" alt="가져오기" /></td>
		</tr>
	</u:listArea>

</form>

<u:titleArea frameId="frameList" frameSrc="/cm/util/reloadable.do"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:700px; height:500px;" />
	
<u:buttonArea>
	<u:button titleId="cm.btn.save" id="saveBtn" onclick="saveCd();" alt="저장" style="display:none;"/>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>