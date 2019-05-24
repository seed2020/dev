<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[
function fnConfirm(){
	$m.ajax('/ct/transMbshDropOut.do?menuId=${menuId}&ctId=${param.ctId}', {ctId:'${param.ctId}'}, function(data){
		if (data.message != null) {
			$m.dialog.alert(data.message);
		}
		if (data.result == 'ok') {
			var win = $m.nav.getWin();
			if(win==null) return;
			win.goMyCmList(data.menuUrl);
			$m.dialog.close('dropOutPop');
		}
	}); 
}

//]]>
</script>

<div class="s_tablearea">
	<div class="blank15"></div>


	<table class="s_table">
	<%-- <caption><u:msg titleId="or.jsp.setUserPop.ref" alt="참조정보"/></caption> --%>
	<colgroup>
		<col width=""/>
	</colgroup>
	<tbody>
		<tr>
			<th class="shead_lt"><u:msg titleId="ct.cfrm.dropOut"  /></th>
		</tr>
		
	</tbody>
	</table>
	
</div>


		<div class="blank20"></div>
        <div class="btnarea">
            <div class="size">
            <dl>
            <dd class="btn" onclick="fnConfirm();"><u:msg titleId="cm.btn.confirm" alt="확인" /></dd>
            <dd class="btn" onclick="$m.dialog.close('dropOutPop')"><u:msg titleId="cm.btn.close" alt="닫기" /></dd>
         </dl>
            </div>
        </div>