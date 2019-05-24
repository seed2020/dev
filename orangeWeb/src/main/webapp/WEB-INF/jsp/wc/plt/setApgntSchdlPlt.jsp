<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">

	function leftToRight(){
		
		if(typeof $("#myApnt option:selected").val()!="undefined"){
			$("#choiApnt").append("<option value='"+$("#myApnt option:selected").val()+"'>"+$("#myApnt option:selected").text()+"</option>");
			$("#myApnt option:selected").remove();
		}
	}

	function rightToLeft(){
		
		if((typeof $("#choiApnt option:selected").val())!="undefined"){
			$("#myApnt").append("<option value='"+$("#choiApnt option:selected").val()+"'>"+$("#choiApnt option:selected").text()+"</option>");
			$("#choiApnt option:selected").remove();			
		}
		
	}
	
	function submitForm(){
		var arrApnt=[];
		
		jQuery('#choiApnt option').each(function(i){
			var valu=$(this).val();
			var textVal=$(this).text();		
			
			arrApnt.push("<input id='choiApntIds' name='choiApntIds' type='hidden' value='"+valu+"'>");
			arrApnt.push("<input id='choiApntNms' name='choiApntNms' type='hidden' value='"+textVal+"'>");
		});            
		arrApnt.push("<input id='apntResetFlag' name='apntResetFlag' type='hidden' value='true'>");
		
		$("#choiApntDiv").html(arrApnt.join(''));
		var $form = $('#setMyBullPop');	
		$form.attr('method','post');
		$form.attr('action','/wc/plt/transApgntSchdlPlt.do?menuId=${menuId}');		
		$form.attr('target','dataframe');
		$form[0].submit();		
	}
	
	$(function() {
		$("input[id='choiApntIds']").each(function(i){
			$("#choiApnt").append("<option value='"+$(this).val()+"'>"+$("input[id='choiApntNms']:eq("+i+")").val()+"</option>");			
			$("#myApnt").val($(this).val());
			$("#myApnt option:selected").remove();
		});
		
	});
	
	

</script>


<div style="width:560px">
<form id="setMyBullPop">
<div id="choiApntDiv">

</div>
<!-- LEFT -->
<div style="float:left; width:47%;">

<u:titleArea outerStyle="height: 270px" innerStyle="width:94%; margin: 0 auto 0 auto; padding: 10px 0 0 0">
	<u:title titleId="wc.jsp.setApntChoiPop.tx01" type="small" alt="선택 가능한 사용자 리스트" />

		<select id="myApnt" size="10" style="width:100%; height:225px; margin: 0; padding: 0;">
		<!-- 선택 가능한 사용자 리스트  반복 S-->
		<c:forEach  var="apntItem" items="${wcApntRvoLst}" varStatus="status">	
			<option value="${apntItem.userUid }">${apntItem.rescNm }</option>
		</c:forEach>
		<!-- 선택 가능한 사용자 리스트  반복 E-->
		</select>
	</u:titleArea>

</div>

<!-- ICON -->
<div style="float:left; width:6%; height: 270px;">

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0"><tbody>
<tr>
<td style="vertical-alignment: middle;">
	<table align="center" width="20" border="0" cellpadding="0" cellspacing="0"><tbody>
	<tr>
	<td><u:buttonIcon href="javascript:rightToLeft();" titleId="cm.btn.left" alt="왼쪽으로이동" /></td>
	</tr>

	<tr>
	<td class="height5"></td>
	</tr>

	<tr>
	<td><u:buttonIcon href="javascript:leftToRight();" titleId="cm.btn.right" alt="오른쪽으로이동" /></td>
	</tr>
	</tbody></table></td>
</tr>
</tbody></table>
</div>

<!-- RIGHT -->
<div style="float:right; width:47%;">

<u:titleArea outerStyle="height: 270px" innerStyle="width:94%; margin: 0 auto 0 auto; padding: 10px 0 0 0">
	
	<u:title titleId="wc.jsp.setApntChoiPop.tx02" type="small" alt="선택된 사용자" />

	<select id="choiApnt" size="13" style="width:100%; height:225px; margin: 0; padding: 0;">
		<!-- 선택된 사용자 리스트  반복 S-->
		<c:forEach  var="apntItem" items="${wcApntSchdlDvoLst}" varStatus="status">	
			<option value="${apntItem.apntrUid }">${apntItem.rescNm }</option>
		</c:forEach>
		<!-- 선택된 사용자 리스트  반복 E-->
	</select>
</u:titleArea>

</div>

<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="submitForm();" alt="저장" auth="R" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</form>
</div>
