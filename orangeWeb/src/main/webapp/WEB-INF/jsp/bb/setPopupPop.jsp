<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:out value="${baBullPopupDVo.strtDt}" type="date" var="strtYmd" />
<u:out value="${baBullPopupDVo.strtDt}" type="hm" var="strtHm" />
<u:out value="${baBullPopupDVo.endDt}" type="date" var="endYmd" />
<u:out value="${baBullPopupDVo.endDt}" type="hm" var="endHm" />
<script type="text/javascript">
<!--
<% // 예약일시 세팅 %>
function setDt() {
	if ($('#strtYmd').val() != '') {
		$('#strtDt').val($('#strtYmd').val() + ' ' + $('#strtHm').val() + ':00');
	} else {
		$('#strtDt').val('');
	}
	if ($('#endYmd').val() != '') {
		$('#endDt').val($('#endYmd').val() + ' ' + $('#endHm').val() + ':00');
	} else {
		$('#endDt').val('');
	}
}

<% // submit form %>
function submitForm() {
	if (validator.validate('setPopupPopForm')) {
		setDt();
		var $form = $('#setPopupPopForm');
		$form.attr('method','post');
		$form.attr('action','./transSetPopup.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
}

function preview(){
	dialog.open('viewPopupPop','${baBrdBVo.rescNm}','/bb/viewPopupPop.do?menuId=${menuId}&bullId=${param.bullId}&brdId=${param.brdId}');
}
//-->
</script>
<div style="width:550px">
<form id="setPopupPopForm">
<u:input type="hidden" id="brdId" value="${param.brdId}" />
<u:input type="hidden" id="bullId" value="${param.bullId}" />
<u:input type="hidden" id="strtDt"  />
<u:input type="hidden" id="endDt"  />
<u:listArea style="width:550px">
	<tr>
		<td width="25%" class="head_lt"><u:mandatory /><u:msg titleId="bb.jsp.popup.period" alt="팝업기간" /></td>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td>
				<u:calendar id="strtYmd" option="{end:'endYmd'}" value="${strtYmd}" mandatory="Y"/>
			</td>
			<td>
				<select id="strtHm" name="strtHm">
				<c:forEach begin="0" end="23" step="1" var="hour" varStatus="status">
					<u:set test="${hour < 10}" var="hh" value="0${hour}" elseValue="${hour}" />
					<u:option value="${hh}:00" title="${hh}:00"  checkValue="${strtHm}"/>
					<u:option value="${hh}:30" title="${hh}:30"  checkValue="${strtHm}"/>
				</c:forEach>
				</select>
			</td>
			<td class="search_body_ct"> ~ </td>
			<td>
				<u:calendar id="endYmd" option="{start:'strtYmd'}" value="${endYmd}" mandatory="Y"/>
			</td>
			<td>
				<select id="endHm" name="endHm">
				<c:forEach begin="0" end="23" step="1" var="hour" varStatus="status">
					<u:set test="${hour < 10}" var="hh" value="0${hour}" elseValue="${hour}" />
					<u:option value="${hh}:00" title="${hh}:00"  checkValue="${endHm}"/>
					<u:option value="${hh}:30" title="${hh}:30"  checkValue="${endHm}"/>
				</c:forEach>
				</select>
			</td>
			</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td width="25%" class="head_lt"><u:mandatory /><u:msg titleId="cols.wdthPx" alt="넓이픽셀" /></td>
		<td>
			<u:input id="width" value="${baBullPopupDVo.width}" titleId="cols.wdthPx"
			mandatory="Y" minInt="200" maxInt="800" valueOption="number" style="width:80px" />
		</td>
	</tr>
	<tr>
		<td width="25%" class="head_lt"><u:msg titleId="bb.jsp.popup.notDisp" alt="그만보기" /></td>
		<td>
			<select id="dispYn" name="dispYn" <u:elemTitle titleId="bb.jsp.popup.notToday" />>
			<u:option value="Y" titleId="cm.option.use"  checkValue="${baBullPopupDVo.dispYn}"/>
			<u:option value="N" titleId="cm.option.notUse"  checkValue="${baBullPopupDVo.dispYn}"/>
			</select>
			<u:msg titleId="bb.jsp.popup.notToday" alt="앞으로 열지 않습니다." />
		</td>
	</tr>
	<tr>
		<td width="25%" class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
		<td>
			<select id="useYn" name="useYn" <u:elemTitle titleId="cols.useYn" />>
			<u:option value="Y" titleId="cm.option.use"  checkValue="${baBullPopupDVo.useYn}"/>
			<u:option value="N" titleId="cm.option.notUse"  checkValue="${baBullPopupDVo.useYn}"/>
			</select>
		</td>
	</tr>
</u:listArea>

<% // 하단 버튼 %>
<u:buttonArea>
		<u:button titleId="bb.jsp.popup.preview" href="javascript:preview();" alt="미리보기"  />
		<u:button titleId="cm.btn.save" href="javascript:submitForm();" alt="저장" auth="${!empty isLginPop && isLginPop==true ? 'W' : 'S' }" />
		<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>
