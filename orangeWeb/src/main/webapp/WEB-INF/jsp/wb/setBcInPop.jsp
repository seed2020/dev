<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function saveUpload(){
	if(validator.validate('uploadForm') && confirmMsg("wb.cfrm.setBcIn")){
		var $form = $("#uploadForm");
		$form.attr('target','dataframe');
		//var obj=$('#uploadForm').closest('div.layerpoparea');
		//loading(obj, true);
		loading('uploadForm', true);
		$form[0].submit();
	}
}
//-->
</script>
<div style="width:400px">
<form id="uploadForm" method="post" enctype="multipart/form-data" action="./transBcIn.do?menuId=${menuId}">
	<c:if test="${!empty isSelect && isSelect == true}">
		<u:input type="hidden" id="pubBcYn" value="Y"/>
		<%-- <u:title titleId="wb.jsp.findBcPop.title" alt="명함선택" type="small"  />
		<u:checkArea>
			<u:radio name="pubBcYn" value="N" titleId="wb.jsp.listBc.title" checked="true" />
			<u:radio name="pubBcYn" value="Y" titleId="wb.jsp.listPublBc.title" />
		</u:checkArea>
		<u:blank /><u:blank /> --%>
	</c:if>
	<u:title titleId="wb.jsp.setBcInOut.tx06" alt="가져올 형식을 선택하세요." type="small"  />
	<u:radio name="csvTypCd" value="google" titleId="wb.cols.bcOut.google" checked="true"/><br />
	<u:radio name="csvTypCd" value="outlook" titleId="wb.cols.bcOut.outlook"/><br />
	<u:radio name="csvTypCd" value="outlook2003" titleId="wb.cols.bcOut.outlook2003"/><br />
	<u:radio name="csvTypCd" value="outlook2007" titleId="wb.cols.bcOut.outlook2007"/><br />
	<u:radio name="csvTypCd" value="outlook2010" titleId="wb.cols.bcOut.outlook2010"/><br />
	<u:radio name="csvTypCd" value="express" titleId="wb.cols.bcOut.express"/><br />
	<u:radio name="csvTypCd" value="" titleId="wb.cols.bcOut.out"/><br />
	<u:blank /><u:blank />
	<u:listArea colgroup="30%,70%">
		<tr>
		<th class="head_lt"><u:msg alt="폴더" titleId="wb.cols.fldNm" /></th>
		<td><select name="fldId">
			<u:option value="" titleId="cm.msg.uncategorized" alt="미분류" selected="true"/>
			<c:forEach items="${wbBcFldBVoList}" var="list" varStatus="status" >
			<u:option value="${list.bcFldId}" title="${list.fldNm}" />
			</c:forEach>
			</select></td>
		</tr>
		<tr>
		<th class="head_lt"><u:msg alt="파일첨부" titleId="cm.btn.fileAtt" /></th>
		<td><u:file id="file" titleId="cm.btn.fileAtt" alt="파일첨부"
			mandatory="Y" exts="csv" /></td>
		</tr>
	</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.fileIn" onclick="saveUpload();" alt="가져오기" auth="R"/>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>