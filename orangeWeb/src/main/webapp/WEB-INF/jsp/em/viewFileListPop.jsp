<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--<c:if test="${empty isFrm}"><% 
// 파일 다운로드 %>
function downFile(id) {
	var ids = [];
	if (id == undefined) {
		var $checked = $('.fileItem input:checked');
		if ($checked.length == 0) {
			<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
			alertMsg("cm.msg.noSelect");
			return;
		}
		$checked.each(function() {
			ids.push($(this).val());
		});
	} else {
		ids.push(id);
	}
	var $form = $('<form>', {
			'method':'post',
			'action':'/${module}${urlSuffix}/downFile.do?${urlParam}',
			'target':'dataframe'
		}).append($('<input>', {
			'name':'menuId',
			'value':'${empty menuId ? param.menuId : menuId}',
			'type':'hidden'
		})).append($('<input>', {
			'name':'fileIds',
			'value':ids,
			'type':'hidden'
		}));
	if('${actionParam}' != null) $form.append($('<input>',{'name':'actionParam','value':'${actionParam}','type':'hidden'}));
	$('form[action="/${module}${urlSuffix}/downFile.do"]').remove();
	$(document.body).append($form);
	$form.submit();
}

<c:if test="${viewYn eq 'Y'}">
<% // 미리보기 %>
function attachViewPop(id, fileExt) {
	if(fileExt==undefined || fileExt=='') return;
	var url = './attachViewAjx.do?menuId=${empty menuId ? param.menuId : menuId}'
	url+='&fileIds='+id;
	if('${urlParam}' != '') url+= '&${urlParam}';
	callAjax(url, null, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.fn != null && data.rs != null) {
			var url = '/viewer/skin/doc.html?fn='+data.fn+'&rs=/'+data.rs;
			window.open(url);
		}
	});
}
</c:if>
</c:if>
<c:if test="${!empty isFrm&& isFrm == true}">
$(document).ready(function() {
	setUniformCSS();
});
</c:if>
//-->
</script>
<c:if test="${empty isFrm }">
<div style="width:500px">

<u:titleArea outerStyle="height:300px;overflow-y:auto;" innerStyle="padding: 5px 10px 0px;" >
	<div id="viewAttchFileArea">
		<input name="delSeqs" type="hidden">
		<div class="attachbtn">
			<dl>
			<dd class="attachbtn_check"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('viewAttchFileArea', this.checked);" value=""/></dd>
			<dd><u:buttonS titleId="cm.btn.download" alt="다운로드" onclick="downFile();" /></dd>
			</dl>
		</div>
		<c:if test="${fn:length(fileVoList) > 0}">
			<c:forEach items="${fileVoList}" var="fileVo" varStatus="status">
			<u:set test="${extSet.contains(fileVo.fileExt)}" var="ext" value="${fileVo.fileExt}" elseValue="file" />
			<div class="attacharea fileItem" style="width:90%;">
			<dl>
			<c:if test="${mode ne 'view' || (mode eq 'view' && (empty downYn || downYn eq 'Y'))}"><dd class="attach_check"><input type="checkbox" name="fileChk" value="${fileVo.fileId}" /></dd></c:if>
			<dd class="attach_img"><c:if test="${viewYn eq 'Y' }"><a href="javascript:attachViewPop('${fileVo.fileId}', '${fileVo.fileExt }');"><img src="${_cxPth}/images/cm/ico_${ext}.png"/></a></c:if><c:if test="${viewYn ne 'Y' }"><img src="${_cxPth}/images/cm/ico_${ext}.png"/></c:if></dd>
			<c:set var="fileSizeKB" value="${fileVo.fileSize/1024 }"/>
			<c:set var="fileSizeMB" value="${fileVo.fileSize/(1024*1024) }"/>
			<c:set var="fileSizeTemp" value="${fileSizeKB+(1-(fileSizeKB%1))%1}"/>
			<u:set var="fileSize" test="${fileSizeTemp > 999 }" value="${fileSizeMB+(1-(fileSizeMB%1))%1 }" elseValue="${fileSizeTemp }"/>
			<u:set var="fileSizeUnit" test="${fileSizeTemp > 999 }" value="MB" elseValue="KB"/>
			<u:set var="ctSendYn" test="${ctSendYn == 'Y' }" value="Y" elseValue="N"/>
			<dd class="attach"><c:if test="${empty downYn || downYn eq 'Y'}"
			><a href="javascript:downFile('${fileVo.fileId}');">${fileVo.dispNm}(<fmt:formatNumber value="${fileSize}" type="number"/>${fileSizeUnit })</a></c:if
			><c:if test="${!empty downYn && downYn ne 'Y' }">${fileVo.dispNm}(<fmt:formatNumber value="${fileSize}" type="number"/>${fileSizeUnit })</c:if
			></dd>
			</dl>
			</div>
			</c:forEach>
		</c:if>
	</div>
</u:titleArea>

<u:buttonArea>
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>

</div>
</c:if>
<c:if test="${!empty isFrm&& isFrm == true}">
<c:if test="${!empty fileVoList }"><u:files id="${module }files" fileVoList="${fileVoList}" module="${module }" mode="view" urlParam="${urlParam }" /></c:if>
</c:if>
<div id="fileDownArea"></div>