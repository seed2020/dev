<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />
<script type="text/javascript">


function selectRepet(){
	
	if($(':radio[name="repetSetup"]:checked').val() == 'Y' &&$(':radio[name="repetKnd"]:checked').val()==null){
		alert("반복종류를 선택하여 주세요.");
		return;
	
	}else if($(':radio[name="repetSetup"]:checked').val() == 'Y'){
		if($(':radio[name="repetKnd"]:checked').val() == 'WELY'){
			var dowChk='';
			$('input[name="dow"]:checkbox:checked').each(function(index){
				dowChk += $(this).val() + "/";});
			if(dowChk==''){
				alert("주간에 요일을 선택하여 주세요.");
				return;
			}
		}
		
		if($(':radio[name="repetKnd"]:checked').val() == 'MOLY'){
			if( $(':radio[name="molyKnd"]:checked').val()==null){
				alert("월간에 반복 방법을 선택하여 주세요.");
				return;
			}
		}
		
		if($(':radio[name="repetKnd"]:checked').val() == 'YELY'){
			if($(':radio[name="yelyKnd"]:checked').val()==null){
				alert("연간에 반복 방법을 선택하여 주세요.");
				return;
			}
		}
		
		if( $("#repetchoiDisDt").val() == ''||$("#repetcmltDisDt").val()==''){
			alert("반복기간에 시작일과 종료일을 입력 하여 주세요.");
			return;
		}
	
	}
		
	var arr = [];
	var result = null;
	

	obj={};
	$this = $(this);
	if($(':radio[name="repetSetup"]:checked').val() == 'Y'){
		
		if($(':radio[name="repetKnd"]:checked').val() == 'DALY'){
			//param : (repetSetup , repetKnd , dalySelect , rechoiDt , recmltDt )	
			arr.push({repetSetup:$(':radio[name="repetSetup"]:checked').val(), 
					  repetKnd:$(':radio[name="repetKnd"]:checked').val(), 
					  dalySelect: $('select[name="dalySelect"]').val(),
					  repetchoiDt: $("#repetchoiDisDt").val(),
					  repetcmltDt: $("#repetcmltDisDt").val()});
		}else if($(':radio[name="repetKnd"]:checked').val() == 'WELY'){
			//체크박스 
			var dowChk='';
			$('#setRepetPopDiv input[name="dow"]:checkbox:checked').each(function(index){
				dowChk += $(this).val() + "/";});
			//param : (repetSetup , repetKnd , welySelect, dow , rechoiDt , recmltDt )
			arr.push({repetSetup:$(':radio[name="repetSetup"]:checked').val(),
					  repetKnd:$(':radio[name="repetKnd"]:checked').val(),
					  welySelect: $('select[name="welySelect"]').val(),
					  dow:dowChk,
					  repetchoiDt: $("#repetchoiDisDt").val(),
					  repetcmltDt: $("#repetcmltDisDt").val()});
			
		}else if($(':radio[name="repetKnd"]:checked').val() == 'MOLY'){
			if($(':radio[name="molyKnd"]:checked').val() == '1'){
				//param : (repetSetup , repetKnd ,molyKnd, firMolySelect , firMolyDaySelect , rechoiDt , recmltDt )
				arr.push({repetSetup:$(':radio[name="repetSetup"]:checked').val(),
						  repetKnd:$(':radio[name="repetKnd"]:checked').val(),
						  molyKnd: $(':radio[name="molyKnd"]:checked').val(),
						  firMolySelect: $('select[name="firMolySelect"]').val(),
						  firMolyDaySelect: $('select[name="firMolyDaySelect"]').val(),
						  repetchoiDt: $("#repetchoiDisDt").val(),
						  repetcmltDt: $("#repetcmltDisDt").val()});
			}else{
				//param : (repetSetup , repetKnd ,molyKnd, secMolySelect , secMolyWeekSelect, secMolyWeekOfDaySelect , rechoiDt , recmltDt)
				arr.push({repetSetup:$(':radio[name="repetSetup"]:checked').val(),
						  repetKnd:$(':radio[name="repetKnd"]:checked').val(),
						  molyKnd: $(':radio[name="molyKnd"]:checked').val(),
						  secMolySelect: $('select[name="secMolySelect"]').val(),
						  secMolyWeekSelect: $('select[name="secMolyWeekSelect"]').val(),
						  secMolyWeekOfDaySelect: $('select[name="secMolyWeekOfDaySelect"]').val(),
						  repetchoiDt: $("#repetchoiDisDt").val(),
						  repetcmltDt: $("#repetcmltDisDt").val()});
			}

		}else if($(':radio[name="repetKnd"]:checked').val() == 'YELY'){
			if($(':radio[name="yelyKnd"]:checked').val() == '1'){
				//param : (repetSetup , repetKnd ,yelyKnd, firYelySelect , firYelyDaySelect , rechoiDt , recmltDt)
				arr.push({repetSetup:$(':radio[name="repetSetup"]:checked').val(),
					  repetKnd:$(':radio[name="repetKnd"]:checked').val(),
					  yelyKnd: $(':radio[name="yelyKnd"]:checked').val(),
					  firYelySelect: $('select[name="firYelySelect"]').val(),
					  firYelyDaySelect: $('select[name="firYelyDaySelect"]').val(),
					  repetchoiDt: $("#repetchoiDisDt").val(),
					  repetcmltDt: $("#repetcmltDisDt").val()});
			
			}else{
				//param : (repetSetup , repetKnd ,yelyKnd, secYelySelect , secYelyWeekSelect, secYelyWeekOfDaySelect , rechoiDt , recmltDt)
				arr.push({repetSetup:$(':radio[name="repetSetup"]:checked').val(),
					  repetKnd:$(':radio[name="repetKnd"]:checked').val(),
					  yelyKnd: $(':radio[name="yelyKnd"]:checked').val(),
					  secYelySelect: $('select[name="secYelySelect"]').val(),
					  secYelyWeekSelect: $('select[name="secYelyWeekSelect"]').val(),
					  secYelyWeekOfDaySelect: $('select[name="secYelyWeekOfDaySelect"]').val(),
					  repetchoiDt: $("#repetchoiDisDt").val(),
					  repetcmltDt: $("#repetcmltDisDt").val()});
			}
		}
		
		result = gRepetHandler(arr);
		//dialog.close('setRepetPop');
		setRepetPop.hide();
	}else{
		
		//param : (repetSetup)
		arr.push({repetSetup:'N'});
		result = gRepetHandler(arr);		
		setRepetPop.hide();
	}
}

<% // 팝업 닫기 %>
function setRepetPopClose(){
	<c:if test="${!empty param.schdlId }">chkFileSetting();</c:if>
	setRepetPop.hide();
}

$(document).ready(function() {
});
	
	
</script>


<div id="setRepetPopDiv" style="width:700px">
<form id="setRepetForm">
<u:input type="hidden" id="menuId" value="${menuId}" />

<div class="front">
<div class="front_left">
	<table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
			<td>※ <u:msg titleId="wc.jsp.setRepetPop.tx01" alt="반복종류 설정시 반복기간을 고려해야 하며, 반복기간을 벗어난 반복종류는 미적용 됩니다." /></td>
		</tr>
	</tbody></table>
</div>
</div>

<% // 폼 필드 %>
<u:listArea>
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg titleId="cols.repetSetup" alt="반복설정" /></td>
		<td colspan="2"><u:checkArea>
			<u:radio name="repetSetup" value="Y" titleId="cm.option.setup" alt="설정"  checked = "true" inputClass="bodybg_lt" />
			<u:radio name="repetSetup" value="N" titleId="cm.option.clear" alt="해제" inputClass="bodybg_lt" />
			</u:checkArea>
		</td>
	</tr>
	
	<tr>
		<td width="18%" rowspan="4" class="head_lt"><u:msg titleId="cols.repetKnd" alt="반복종류" /></td>
		<td width="18%"><u:checkArea>
			<u:radio name="repetKnd" value="DALY" titleId="wc.option.daly" inputClass="bodybg_lt" />
			</u:checkArea>
		</td>
	<td width="64%"><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td class="body_lt"><u:msg titleId="wc.jsp.setRepetPop.tx02" alt="매" />
		</td>
			<td><select id="dalySelect" name="dalySelect">
				<c:forEach begin="1" end="60" step="1" var="no">
					<option value="${no}">${no}</option>
				</c:forEach>
				</select>
			</td>
			<td class="body_lt"><u:msg titleId="wc.jsp.setRepetPop.tx03" alt="일마다" />
			</td>
		</tr>
		</tbody></table></td>
	</tr>
	
	<tr>
	<td>
		<u:checkArea>
		<u:radio name="repetKnd" value="WELY" titleId="wc.option.wely" inputClass="bodybg_lt" />
		</u:checkArea></td>
	<td>
		<table border="0" cellpadding="0" cellspacing="0">
			<tbody>
				<tr>
					<td class="body_lt"><u:msg titleId="wc.jsp.setRepetPop.tx02" alt="매" /></td>
					<td><select name="welySelect">
						<c:forEach begin="1" end="8" step="1" var="no">
						<option>${no}</option>
						</c:forEach>
						</select></td>
					<td class="body_lt"><u:msg titleId="wc.jsp.setRepetPop.tx04" alt="주마다" /></td>
				</tr>
			</tbody>
		</table>
		
		<u:checkArea>
		<u:checkbox name="dow" value="SUN" titleId="wc.option.sun" checked="false" inputClass="bodybg_lt" />
		<u:checkbox name="dow" value="MON" titleId="wc.option.mon" checked="false" inputClass="bodybg_lt" />
		<u:checkbox name="dow" value="TUE" titleId="wc.option.tue" checked="false" inputClass="bodybg_lt" />
		<u:checkbox name="dow" value="WED" titleId="wc.option.wed" checked="false" inputClass="bodybg_lt" />
		<u:checkbox name="dow" value="THU" titleId="wc.option.thu" checked="false" inputClass="bodybg_lt" />
		<u:checkbox name="dow" value="FRI" titleId="wc.option.fri" checked="false" inputClass="bodybg_lt" />
		<u:checkbox name="dow" value="SAT" titleId="wc.option.sat" checked="false" inputClass="bodybg_lt" />
		</u:checkArea>
		</td>
	</tr>
	
	<tr>
	<td>
		<u:checkArea>
			<u:radio name="repetKnd" value="MOLY" titleId="wc.option.moly"  inputClass="bodybg_lt" />
		</u:checkArea>
	</td>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
			<td><u:radio name="molyKnd" value="1" /></td>
			<td class="body_lt"><u:msg titleId="wc.jsp.setRepetPop.tx02" alt="매" /></td>
			<td><select id="firMolySelect" name="firMolySelect">
				<c:forEach begin="1" end="60" step="1" var="no">
				<option value="${no}">${no}</option>
				</c:forEach>
				</select></td>
			<td class="body_lt"><u:msg titleId="wc.jsp.setRepetPop.tx05" alt="개월마다" /></td>
			<td><select id="firMolyDaySelect" name="firMolyDaySelect">
				<c:forEach begin="1" end="31" step="1" var="no">
				<option value="${no}">${no}</option>
				</c:forEach>
				</select></td>
			<td class="body_lt"><u:msg titleId="wc.jsp.setRepetPop.tx07" alt="일" /></td>
		</tr>
		</tbody></table>
		
		<table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><u:radio name="molyKnd" value="2" /></td>
		<td class="body_lt"><u:msg titleId="wc.jsp.setRepetPop.tx02" alt="매" /></td>
		<td><select id="secMolySelect" name="secMolySelect">
			<c:forEach begin="1" end="60" step="1" var="no">
			<option value="${no}">${no}</option>
			</c:forEach>
			</select></td>
		<td class="body_lt"><u:msg titleId="wc.jsp.setRepetPop.tx05" alt="개월마다" /></td>
		<td><select id="secMolyWeekSelect"  name="secMolyWeekSelect">
			<option value="1"><u:msg titleId="wc.cols.week1" alt="1주차" /></option>
			<option value="2"><u:msg titleId="wc.cols.week2" alt="2주차" /></option>
			<option value="3"><u:msg titleId="wc.cols.week3" alt="3주차" /></option>
			<option value="4"><u:msg titleId="wc.cols.week4" alt="4주차" /></option>
			<option value="5"><u:msg titleId="wc.cols.week5" alt="5주차" /></option>
			</select></td>
		<td><select id="secMolyWeekOfDaySelect"  name="secMolyWeekOfDaySelect">
			<option value="1"><u:msg titleId="wc.option.sun" alt="일요일" /></option>
			<option value="2"><u:msg titleId="wc.option.mon" alt="월요일" /></option>
			<option value="3"><u:msg titleId="wc.option.tue" alt="화요일" /></option>
			<option value="4"><u:msg titleId="wc.option.wed" alt="수요일" /></option>
			<option value="5"><u:msg titleId="wc.option.thu" alt="목요일" /></option>
			<option value="6"><u:msg titleId="wc.option.fri" alt="금요일" /></option>
			<option value="7"><u:msg titleId="wc.option.sat" alt="토요일" /></option>
			</select></td>
		</tr>
		</tbody></table>
		</td>
	</tr>
	
	<tr>
	<td><u:checkArea>
		<u:radio name="repetKnd" value="YELY" titleId="wc.option.yely" inputClass="bodybg_lt" />
		</u:checkArea></td>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><u:radio name="yelyKnd" value="1" /></td>
		<td class="body_lt"><u:msg titleId="wc.jsp.setRepetPop.tx08" alt="매년" /></td>
		<td><select id="firYelySelect"  name="firYelySelect">
			<c:forEach begin="1" end="12" step="1" var="no">
			<option>${no}</option>
			</c:forEach>
			</select></td>
		<td class="body_lt"><u:msg titleId="wc.jsp.setRepetPop.tx06" alt="월" /></td>
		<td><select id="firYelyDaySelect" name="firYelyDaySelect">
			<c:forEach begin="1" end="31" step="1" var="no">
			<option>${no}</option>
			</c:forEach>
			</select></td>
		<td class="body_lt"><u:msg titleId="wc.jsp.setRepetPop.tx07" alt="일" /></td>
		</tr>
		</tbody></table>
		
		<table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><u:radio name="yelyKnd" value="2" /></td>
		<td class="body_lt"><u:msg titleId="wc.jsp.setRepetPop.tx08" alt="매년" /></td>
		<td><select id="secYelySelect" name="secYelySelect">
			<c:forEach begin="1" end="12" step="1" var="no">
			<option>${no}</option>
			</c:forEach>
			</select></td>
		<td class="body_lt"><u:msg titleId="wc.jsp.setRepetPop.tx06" alt="월" /></td>
		<td><select id="secYelyWeekSelect" name="secYelyWeekSelect">
			<option value="1"><u:msg titleId="wc.cols.week1" alt="1주차" /></option>
			<option value="2"><u:msg titleId="wc.cols.week2" alt="2주차" /></option>
			<option value="3"><u:msg titleId="wc.cols.week3" alt="3주차" /></option>
			<option value="4"><u:msg titleId="wc.cols.week4" alt="4주차" /></option>
			<option value="5"><u:msg titleId="wc.cols.week5" alt="5주차" /></option>
			</select></td>
		<td><select id="secYelyWeekOfDaySelect" name="secYelyWeekOfDaySelect">
			<option value="1"><u:msg titleId="wc.option.sun" alt="일요일" /></option>
			<option value="2"><u:msg titleId="wc.option.mon" alt="월요일" /></option>
			<option value="3"><u:msg titleId="wc.option.tue" alt="화요일" /></option>
			<option value="4"><u:msg titleId="wc.option.wed" alt="수요일" /></option>
			<option value="5"><u:msg titleId="wc.option.thu" alt="목요일" /></option>
			<option value="6"><u:msg titleId="wc.option.fri" alt="금요일" /></option>
			<option value="7"><u:msg titleId="wc.option.sat" alt="토요일" /></option>
			</select></td>
		</tr>
		</tbody></table>
		</td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.repetPrd" alt="반복기간" /></td>
	<td colspan="2"><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td width="80px">
			<u:calendar id="repetchoiDisDt" option="{end:'repetcmltDisDt'}" titleId="cols.choiDt" alt="선택일시" value="${param.repetchoiDisDt }"/>
		</td>
		<td class="search_body_ct"> ~ </td>
		<td width="80px">
			<u:calendar id="repetcmltDisDt" option="{start:'repetchoiDisDt'}" titleId="cols.cmltDt" alt="완료일시" />
		</td>
		</tr>
		</tbody></table></td>
	</tr>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="selectRepet();" alt="저장" />
	<u:button titleId="cm.btn.cancel" onclick="setRepetPopClose();" alt="취소" />
</u:buttonArea>

</form>
</div>

