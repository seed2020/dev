<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function formSubit(){
	 var ranking = $('#setExntCmTabPlt input:radio[name="ranking"]:checked').val();
	callAjax('/ct/plt/transRankSave.do?menuId=${menuId}', {ranking:ranking}, function(data){
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			dialog.close(this);
		}
	});
	
}

//-->
</script>


<div style="width:300px">

<form id="setExntCmTabPlt"  method="post" >



<% // 폼 필드 %>
<u:listArea>

	<u:set test="${ranking == '5'}" var="fRank" value="true" elseValue="false"></u:set>
	<u:set test="${ranking == '10'}" var="tRank" value="true" elseValue="false"></u:set>
	<u:set test="${ranking == '20'}" var="sRank" value="true" elseValue="false"></u:set>
	<u:set test="${ranking == '30'}" var="thRank" value="true" elseValue="false"></u:set>
	<u:set test="${ranking == '50'}" var="fiRank" value="true" elseValue="false"></u:set>
	<u:set test="${ranking == '100'}" var="hRank" value="true" elseValue="false"></u:set>
	
	<div class="titlearea">
		<div class="tit_left">
			<dl>
				<dd class="title_s"><u:msg titleId="ct.msg.plt.ranking" alt="지정 순위 내의 LIST를 보여줍니다."/></dd>
			</dl>
		</div>
	</div>
	<tr>
		<td class="head_lt">
			<table border="0" cellpadding="0" cellspacing="0">
					<tr style="height:40px" class="head_lt">
						<td style="width:13%;" class="head_lt"><u:radio name="ranking" value="5" titleId="ct.cols.5.rank" alt="5위" checked="${fRank}" /></td>
						<td style="width:13%;" class="head_lt"><u:radio name="ranking" value="10" titleId="ct.cols.10.rank" alt="10위" checked="${tRank}" /></td>
						<td style="width:13%;" class="head_lt"><u:radio name="ranking" value="20" titleId="ct.cols.20.rank" alt="20위" checked="${sRank}" /></td>
					</tr>
					<tr  style="height:40px"  class="head_lt">
						<td style="width:13%;" class="head_lt"><u:radio name="ranking" value="30" titleId="ct.cols.30.rank" alt="30위" checked="${thRank}" /></td>
						<td style="width:13%;" class="head_lt"><u:radio name="ranking" value="50" titleId="ct.cols.50.rank" alt="50위" checked="${fiRank}" /></td>
						<td style="width:13%;" class="head_lt"><u:radio name="ranking" value="100" titleId="ct.cols.100.rank" alt="100위" checked="${hRank}" /></td>
					</tr>
					</tbody>
				
			</table>
			
		</td>
	</tr>
	

</u:listArea>
<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="javascript:formSubit();dialog.close(this);" alt="저장"/>
</u:buttonArea>

</form>
</div>
