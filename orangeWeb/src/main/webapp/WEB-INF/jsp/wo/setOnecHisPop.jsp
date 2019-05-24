<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%><script type="text/javascript">
<!--
$(document).ready(function() {
	if('${param.mode}'=='mod'){
		var chks = $("#historyArea input[type='checkbox']:visible:checked");
		var frm = $("#historyForm");
		var $tr = $(getParentTag(chks[0],'tr'));
		frm.find("#hisRegDt").val($tr.find("input[name='hisRegDt']").val());
		frm.find("#hisCont").val($tr.find("input[name='hisCont']").val());
	}
});
function setHistoryPop(){
	var frm = $("#historyForm");
	var hisRegDt = frm.find("#hisRegDt").val();
	var hisCont = frm.find("#hisCont").val();
	if(hisRegDt!='' && hisCont!=''){
		var $tr = null;
		if('${param.mode}'=='mod'){
			var chks = $("#historyArea input[type='checkbox']:visible:checked");
			if(chks.length==1){
				$tr = $(getParentTag(chks[0],'tr'));
			}
		} else if('${param.mode}'=='add'){
			var $historyArea = $("#historyArea");
			<%-- html 만듬 --%>
			var $hiddenTr = $historyArea.find("#hiddenTr");
			var newTr = $($hiddenTr[0].outerHTML);
			newTr.attr('id','');
			newTr.find('input[type=checkbox]').attr('class','');
			newTr.show();
			
			newTr.insertBefore($historyArea.find('tr:first'));
			$tr = newTr;
			setUniformCSS($tr[0]);
		}
		if($tr != null){
			$tr.find("input[name='hisRegDt']").val(hisRegDt);
			$tr.find("input[name='hisCont']").val(hisCont);
			$tr.find('td').eq(1).text(hisRegDt);
			$tr.find('td').eq(2).text(hisCont);
		}
	}
	dialog.close("setOnecHisDialog");
}
//-->
</script>
<div style="width:900px;">

<form id="historyForm">
<u:listArea colgroup="12%,88%">
<tr>
	<td class="head_ct"><u:msg titleId="wo.regDt" alt="등록일" /></td>
	<td><u:calendar id="hisRegDt" titleId="wo.regDt" alt="등록일" value="today" /></td>
</tr>
<tr>
	<td class="head_ct"><u:msg titleId="cols.cont" alt="내용" /></td>
	<td><input id="hisCont" name="hisCont" style="width:98%" maxlength="80" /></td>
</tr>
</u:listArea>
</form>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" href="javascript:setHistoryPop();" alt="확인" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</div>