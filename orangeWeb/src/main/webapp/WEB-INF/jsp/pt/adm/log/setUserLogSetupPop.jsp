<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript" src="${_cxPth}/js/calendar/moment.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/calendar/locales.min.js" charset="UTF-8"></script>
<script type="text/javascript">
<% // 일시 체크 %>
function checkYmd(date){
	if(moment(date).isBefore(moment().subtract(1, 'd'))){
		alertMsg('cm.calendar.check.dateAI');
		return true;
	}
	selectNextRepetYmd(date);
	return false;
};

<% // [조회] 삭제일시 %>
function selectNextRepetYmd(date) {
	var useYn = $('input:radio[name="useYn"]').val(); 
	if(useYn=='N') return;	 
	var strtYmd = date != undefined ? date : $('#strtYmd').val();	
	if(strtYmd != '') {
		var momentMap={year:'y', month:'M', week:'w', day:'d'};
		var repetTypCd = $(':radio[name="repetTypCd"]:checked').val();
		var nextDay=moment(strtYmd); // 시작일자 세팅
		var addDay=1;
		if(repetTypCd=='year' && nextDay.isBefore(moment(), 'year'))
			nextDay.year(moment().year());
		else if(repetTypCd=='month' && nextDay.isBefore(moment(), 'month'))
			nextDay.year(moment().year()).month(moment().month());
		else if(repetTypCd=='week' && nextDay.isBefore(moment(), 'day'))
			nextDay=moment().weekday(nextDay.weekday());
		else if(repetTypCd.startsWith('day')){
			addDay=parseInt(repetTypCd.replace(/day/, ''));
			repetTypCd='day';
		}
		if(!moment(strtYmd).isSame(moment(), 'day')){
			nextDay.hour(23).minute(30);
		}
		
		if(nextDay.isBefore(moment())){
			nextDay.add(addDay, momentMap[repetTypCd]);
		}
		nextDay=nextDay.format('YYYY-MM-DD');
		$('#nextRepetYmdArea').show();
		$('#nextRepetYmdTxt').text(nextDay);
		$('#nextRepetYmd').val(nextDay);
	}
};

<% // [팝업:저장] 테이블 저장 %>
function save() {
	if(validator.validate('setRegForm') && confirmMsg("cm.cfrm.save")) {<%//cm.cfrm.save=저장하시겠습니까 ?%>
		var repetTypCd = $(':radio[name="repetTypCd"]:checked').val(); 
		var useYn = $(':radio[name="useYn"]:checked').val(); 
		callAjax('./transUserLogSetupAjx.do?menuId=${menuId}', {repetTypCd:repetTypCd, strtYmd:$('#strtYmd').val(), useYn:useYn, nextRepetYmd:$('#nextRepetYmd').val()}, function(data){
			if(data.message!=null){
				alert(data.message);
			}
			dialog.close('setUserLogSetupPop');
		});
	}
};

</script>
<div style="width:400px">
<form id="setRegForm">
	<u:input type="hidden" name="menuId" value="${menuId}" />
	<% // 폼 필드 %>
	<u:listArea>
	<tr>
		<td width="32%" class="head_lt"><u:mandatory /><u:msg titleId="wc.btn.repetSetup" alt="반복설정"/></td>
		<td>
			<u:checkArea>
				<u:radio name="repetTypCd" value="year" titleId="wc.option.yely" alt="연간" checkValue="${orUserLginSetupBVo.repetTypCd }"  inputClass="bodybg_lt" onclick="selectNextRepetYmd();" checked="${empty orUserLginSetupBVo.repetTypCd }"/>
				<u:radio name="repetTypCd" value="month" titleId="wc.option.moly" alt="월간" checkValue="${orUserLginSetupBVo.repetTypCd }" inputClass="bodybg_lt" onclick="selectNextRepetYmd();"/>
				<u:radio name="repetTypCd" value="week" titleId="wc.option.wely" alt="주간" checkValue="${orUserLginSetupBVo.repetTypCd }" inputClass="bodybg_lt" onclick="selectNextRepetYmd();"/>
			</u:checkArea>
		</td>
	</tr>
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg titleId="cm.cal.startDd" alt="시작일자"/></td>
		<td>
			<u:calendar id="strtYmd" value="${orUserLginSetupBVo.strtYmd}" option="{checkHandler:checkYmd}" mandatory="Y"/>
		</td>
	</tr>
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg titleId="cols.useYn" alt="사용여부"/></td>
		<td>
			<u:checkArea>
				<u:radio name="useYn" value="Y" titleId="cm.option.use" alt="사용" checkValue="${orUserLginSetupBVo.useYn }"  inputClass="bodybg_lt" checked="${empty orUserLginSetupBVo.useYn }"/>
				<u:radio name="useYn" value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${orUserLginSetupBVo.useYn }" inputClass="bodybg_lt" />
			</u:checkArea>
		</td>
	</tr>
</u:listArea>
<div class="color_txt" id="nextRepetYmdArea" style="float:left;display:${!empty orUserLginSetupBVo.nextRepetYmd ? 'inline' : 'none'};">※ <u:msg titleId="wc.cols.del.date" alt="삭제일자"/> : <span id="nextRepetYmdTxt">${orUserLginSetupBVo.nextRepetYmd}<c:if test="${!empty orUserLginSetupBVo.delYn && orUserLginSetupBVo.delYn eq 'Y'}">(<u:msg titleId="cm.msg.complete" alt="완료"/>)</c:if></span><u:input type="hidden" id="nextRepetYmd" titleId="wc.cols.del.date" alt="삭제일자" value="${orUserLginSetupBVo.nextRepetYmd}"/></div>
<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="save();" alt="저장" auth="A" />
	<u:button href="javascript:void(0)" onclick="dialog.close(this);" alt="닫기" titleId="cm.btn.close" />
</u:buttonArea>

</form>
</div>
