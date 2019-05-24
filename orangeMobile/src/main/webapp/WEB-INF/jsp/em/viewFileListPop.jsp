<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[
<% // 파일 다운로드 %>
function downFile(id) {
	var ids = [];
	ids.push(id);
	var $form = $('<form>', {
			'method':'get',
			'action':'/wf/works/downFile.do',
			'target':$m.browser.mobile && $m.browser.safari ? '' : 'dataframe'
		}).append($('<input>', {
			'name':'menuId',
			'value':'${menuId}',
			'type':'hidden'
		})).append($('<input>', {
			'name':'formNo',
			'value':'${param.formNo}',
			'type':'hidden'
		})).append($('<input>', {
			'name':'workNo',
			'value':'${param.workNo}',
			'type':'hidden'
		})).append($('<input>', {
			'name':'fileIds',
			'value':ids,
			'type':'hidden'
		}));
	
	$(top.document.body).append($form);
	$m.secu.set();
	$form.submit();
	$form.remove();
}           
//]]>
</script>
<!--entryzone S-->
<div class="attachzone view attchCls" >
<div class="blank30"></div>
<div class="attacharea">
<c:if test="${!empty fileVoList }">
<c:forEach items="${fileVoList}" var="fileVo" varStatus="status">
<m:attach fileName="${fileVo.dispNm}" fileKb="${fileVo.fileSize/1024 }" downFnc="downFile('${fileVo.fileId}');" viewFnc="viewAttchFile('${fileVo.fileId}');"/>
</c:forEach>
</c:if>
</div>
</div>

<div class="blank20"></div>
<div class="btnarea">
	<div class="size">
		<dl>
		<dd class="btn" onclick="$m.dialog.close('viewFileListDialog');"><u:msg titleId="cm.btn.close" alt="닫기" /></dd>
		</dl>
	</div>
</div>


          

