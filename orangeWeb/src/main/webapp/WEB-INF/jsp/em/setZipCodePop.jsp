<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
// 입력된 주소를 세팅한다.
function setZipCode(){
	if (!validator.validate('setZipCodeForm')) {
		return;
	}
	var adrId = '${param.adrId}';
	var zipNo = adrId == ''  ? "zipNo" : (adrId+"ZipNo");
	
	var adr = adrId == ''  ? "adr" : (adrId+"Adr");
	
	<c:if test="${empty param.frameId }">
	$('#'+zipNo).val($('#addrZipNo').val());
	$('#'+adr).val($('#addrInfo').val() + "" + ($('#bldDong').val() == '' ? '' : " "+$('#bldDong').val() + '<u:msg titleId="em.cols.setZipCode.dong" alt="동" />')+"" + ($('#bldHo').val() == '' ? '' : " " +$('#bldHo').val() + '<u:msg titleId="em.cols.setZipCode.ho" alt="호" />')+ ($('#referNm').val() == '' ? '' : " (" +$('#referNm').val()+")"));
	</c:if>
	<c:if test="${!empty param.frameId }">
	var frame = getIframeContent('${param.frameId}');
	frame.$('#'+zipNo).val($('#addrZipNo').val());
	frame.$('#'+adr).val($('#addrInfo').val() + "" + ($('#bldDong').val() == '' ? '' : " "+$('#bldDong').val() + '<u:msg titleId="em.cols.setZipCode.dong" alt="동" />')+"" + ($('#bldHo').val() == '' ? '' : " " +$('#bldHo').val() + '<u:msg titleId="em.cols.setZipCode.ho" alt="호" />')+ ($('#referNm').val() == '' ? '' : " (" +$('#referNm').val()+")"));
	</c:if>
	
	
	dialog.close('zipCodePopup');
};
</script>

<div id="zipCodeContainer" style="width:500px">
	<div id="addrBody" style="width:100%;">
		<form id="setZipCodeForm">
			<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<colgroup>
					<col width="25%"/>
					<col width="75%"/>
				</colgroup>
				<tr>
					<td class="head_lt"><u:mandatory /><u:msg titleId="cols.addrZipNo" alt="우편번호"/></td>
					<td>
						<table border="0" cellpadding="0" cellspacing="0">
							<tbody>
							<tr>
								<td>
									<u:input id="addrZipNo" titleId="cols.addrZipNo" className="input_center" style="ime-mode:disabled; width:80px" minByte="5" maxByte="5" valueOption="number" maxLength="5" mandatory="Y"/>
								</td>
							</tr>
							</tbody>
						</table>
					</td>
				</tr>
				<tr>
					<td class="head_lt"><u:mandatory /><u:msg titleId="cols.addrAdr" alt="주소"/></td>
					<td><u:input id="addrInfo"  titleId="cols.addrAdr" style="width:96%" maxByte="100" mandatory="Y"/>
						<div class="color_txt" id="detlAdrMsg"><u:msg titleId="em.jsp.findZipCode.tx04" alt="※ 건물명 및 동·호 를 제외한 항목 입력 예) 서울특별시 서초구 매헌로 54-1"/></div>
					</td>
				</tr>
				<tr>
					<td class="head_lt"><u:msg titleId="em.cols.setZipCode.detlDongHoNm" alt="상세주소(동·호)"/></td>
					<td>
						<u:input id="bldDong" titleId="em.cols.setZipCode.dong" style="width:25px;" maxByte="4" valueOption="upper,number" maxLength="4"/><u:msg titleId="em.cols.setZipCode.dong" alt="동" />
						<u:input id="bldHo" titleId="em.cols.setZipCode.ho" style="width:25px;" maxByte="4" valueOption="number" maxLength="4"/><u:msg titleId="em.cols.setZipCode.ho" alt="호" />
						<div class="color_txt" id="detlAdrMsg"><u:msg titleId="em.jsp.findZipCode.tx05" alt="※ 주거용 주택(아파트,빌라등 공동주택)일 경우만 입력."/></div>
					</td>
				</tr>
				<tr>
					<td class="head_lt"><u:msg titleId="em.cols.setZipCode.detlEtcCont" alt="상세주소(참고항목)"/></td>
					<td><u:input id="referNm" titleId="em.cols.setZipCode.referNm" style="width:150px;" maxByte="100" />
						<div class="color_txt" id="detlAdrMsg"><u:msg titleId="em.jsp.findZipCode.tx06" alt="※ 주소에 '동'이 포함된 경우 , 공동주택 일경우 입력. 예) 양재동,삼성아파트"/></div>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<u:blank />
	<% // 하단 버튼 %>
	<u:buttonArea>
		<u:button titleId="cm.btn.apply" alt="입력완료" onclick="setZipCode();" />
		<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
	</u:buttonArea>
	
</div>