<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--
<% // 파일 다운로드 %>
function downApHisFile(id) {
	var ids = [];
	if (id == null) {
		$("#attHisPop input[type='checkbox'][id!='checkHeader']:visible").each(function() {
			if($(this).val()!=null && $(this).val()!=''){
				ids.push($(this).val());
			}
		});
		if (ids.length == 0) {<%
			// cm.msg.noSelect=선택한 항목이 없습니다. %>
			alertMsg("cm.msg.noSelect");
			return;
		}
	} else {
		ids.push(id);
	}
	var $form = $('<form>', {
			'method':'post','action':'${_cxPth}/${apFileModule}/down${apFileTarget}File.do','target':'dataframe'
		}).append($('<input>', {
			'name':'bxId','value':'${param.bxId}','type':'hidden'
		})).append($('<input>', {
			'name':'menuId','value':'${menuId}','type':'hidden'
		})).append($('<input>', {
			'name':'apvNo','value':'${param.apvNo}','type':'hidden'
		})).append($('<input>', {
			'name':'attHstNo','value':'${param.attHstNo}','type':'hidden'
		})).append($('<input>', {
			'name':'attSeq','value':ids.join(','),'type':'hidden'
		}));
	
	$(document.body).append($form);
	$form.submit();
	$form.remove();
}
//-->
</script>
<div style="width:400px">

<u:listArea id="attHisPop">
	<tr id="titleTr">
		<td width="4%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all'
		/>" onclick="checkAllCheckbox('attHisPop', this.checked);" value=""/></td>
		<td class="head_ct" width="65%"><u:msg titleId="ap.jsp.fileNm" alt="파일명" /></td>
		<td class="head_ct" width="31%"><u:msg titleId="ap.jsp.fileSize" alt="파일크기(KB)" /></td>
	</tr>
<c:forEach items="${apOngdAttFileLVoList}" var="apOngdAttFileLVo" varStatus="status">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
		<td class="bodybg_ct"><input type="checkbox" name="apvLnCheck" value="${apOngdAttFileLVo.attSeq}" /></td>
		<td class="body_ct"><a href="javascript:downApHisFile('${apOngdAttFileLVo.attSeq}')"><u:out value="${apOngdAttFileLVo.attDispNm}" /></a></td>
		<td class="body_ct"><u:out value="${apOngdAttFileLVo.fileKb}" type="number" /></td>
	</tr>
</c:forEach>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.download" alt="다운로드" onclick="downApHisFile();" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>