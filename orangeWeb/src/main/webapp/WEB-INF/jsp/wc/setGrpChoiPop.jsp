<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">

	function rightToLeft(){
		
		if(typeof $("#myGrp option:selected").val()!="undefined"){
			$("#choiGrp").append("<option value='"+$("#myGrp option:selected").val()+"'>"+$("#myGrp option:selected").text()+"</option>");
			$("#myGrp option:selected").remove();
		}
	}

	function leftToRight(){
		
		if((typeof $("#choiGrp option:selected").val())!="undefined"){
			$("#myGrp").append("<option value='"+$("#choiGrp option:selected").val()+"'>"+$("#choiGrp option:selected").text()+"</option>");
			$("#choiGrp option:selected").remove();			
		}
		
	}
	
	function submitForm(){
		var arrGrp=[];
		
		jQuery('#choiGrp option').each(function(i){
			var valu=$(this).val();
			var textVal=$(this).text();		
			
			arrGrp.push("<input id='choiGrpIds' name='choiGrpIds' type='hidden' value='"+valu+"'>");
			arrGrp.push("<input id='choiGrpNms' name='choiGrpNms' type='hidden' value='"+textVal+"'>");
		});            
		arrGrp.push("<input id='grpResetFlag' name='grpResetFlag' type='hidden' value='true'>");
		$("#choiGrpDiv").html(arrGrp.join(''));
		selectMonth('');
		dialog.close('setGrpChoiPop');
	}
	
	$(function() {
		$("input[id='choiGrpIds']").each(function(i){
			$("#choiGrp").append("<option value='"+$(this).val()+"'>"+$("input[id='choiGrpNms']:eq("+i+")").val()+"</option>");			
			$("#myGrp").val($(this).val());
			$("#myGrp option:selected").remove();
		});
		
	});
	
	

</script>


<div style="width:600px">
<form id="setMyBullPop">

<!-- LEFT -->
<div style="float:left; width:47%;">

<u:titleArea outerStyle="height: 270px" innerStyle="width:94%; margin: 0 auto 0 auto; padding: 10px 0 0 0">
	<u:title titleId="wc.jsp.setGrpChoiPop.tx02" type="small" alt="선택된 그룹 목록" />

		<select id="choiGrp" size="10" style="width:100%; height:225px; margin: 0; padding: 0;">
		<!-- 선택된 그룹  반복 S-->
		<!-- 선택된 그룹  반복 E-->
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
	<u:title titleId="wc.jsp.setGrpChoiPop.tx01" type="small" alt="그룹 목록" />

	<select id="myGrp" size="13" style="width:100%; height:225px; margin: 0; padding: 0;">
		<c:forEach  var="grpItem" items="${wcSchdlGroupBVoList}" varStatus="status">	
			<option value="${grpItem.schdlGrpId }">${grpItem.grpNm }</option>
		</c:forEach>
	</select>
</u:titleArea>

</div>

<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="submitForm();" alt="저장" auth="R" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>
