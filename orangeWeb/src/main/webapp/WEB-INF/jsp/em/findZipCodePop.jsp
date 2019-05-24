<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<style type="text/css">
	#schResultContainer ul {list-style-type: none;margin: 3px;padding: 3px;}
	#schResultContainer li {	padding: 5px;cursor: pointer;border-bottom:1px solid #e5e5e5;}
	#schResultContainer span.ad1 {margin-left:5px;}
</style>
<script type="text/javascript">
<% // 탭 클릭 %>
function fnTabClick(schCat){
	$('#zipCodeContainer #schCat').val(schCat);
	$('#zipCodeContainer #schWord').val("");
	fnSearchInit();
	$('#zipCodeContainer #schNotice > div').hide();
	$('#zipCodeContainer #'+schCat+'Notice').show();
	
	$("#zipCodeContainer #sidoSelect option:eq(0)").attr("selected", "selected");
	$('#zipCodeContainer #sidoSelect').uniform();
	$("#zipCodeContainer #gugunSelect option:eq(0)").attr("selected", "selected");
	$('#zipCodeContainer #gugunSelect').find('option').each(function(){
		if($(this).val() != '') $(this).remove();
	});
	$('#zipCodeContainer #gugunSelect').uniform();
	
};<% // 검색시에 모두 초기화 %>
function fnSearchInit(){
	$('#schResultContainer li').off('click');
	$('#schResultContainer > ul').empty();		// 검색 결과 초기화
	$('#setZipReviewTbl').hide();
	$("#schResultBar").hide();
	$('#schResultError').hide();
	$('#setZipCodeBtn').hide();	
};<% // 검색어 체크(한글,영문,숫자) %>
function chkParams(schCat){
	var pattern=/[^\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi;
	
	var schCat=$('#zipCodeContainer #schCat').val();
	var schWord = $("#zipCodeContainer #schWord").val();
	if(/\s{2,}/.test(schWord)) return false; // 연속 공백 체크
	if(schCat!='bldNm' && schWord.indexOf(' ')>-1){
		pattern=/[a-zA-Z0-9가-힣]\s\d{1,6}$|[a-zA-Z0-9가-힣]\s\d{1,6}-\d{1,6}$|[a-zA-Z0-9가-힣]\s\d{1,6} \d{1,6}$/;
	}
	return pattern.test(schWord);
};
<% // 주소세팅
//1.‘법정리명’이 NULL이 아닌경우, ‘법정 읍면동명’ 컬럼 내용이 도로명주소에 포함
//2.‘법정리명’이 NULL인 경우, ‘법정읍면동명’은 참고항목에 포함
//3.건물부번이 0인 경우, 표기 안함
//4.공동주택여부가 1인 경우, 참고항목에 ‘시군구건물명’ 포함
//5.지하여부가 1인 경우, 건물본번 앞에 ‘지하’ 문구 포함
%>
function setZipData(data){
	var addr_view1, addr_view2, addr_view3, addr_view4;
	$.each(data , function(index, zipVo) {
		addr_view1 = zipVo.sido + " " + (zipVo.gugun===undefined ? '' : zipVo.gugun) + (zipVo.ri != "" && zipVo.ri != undefined ? " " + zipVo.dong : "" );		// 시도 + 시구군 + 읍면
		addr_view2 = zipVo.roadNm + " " + (zipVo.basementYn == '1' ? '<u:msg titleId="em.cols.findZipCode.basement" alt="지하" /> ' : '')+ zipVo.mainBldNo + (zipVo.subBldNo == "0" ? "" : "-" + zipVo.subBldNo);		// 도로명 주소
		addr_view3 = "";		// 참고 항목
		if(zipVo.ri == "" || zipVo.ri == undefined)
			addr_view3 = "(" + zipVo.dong + (zipVo.aptHourseYn == 1 ? ","+zipVo.cityPublicBldNm : "" );
		if(zipVo.ri != "" && zipVo.ri != undefined && zipVo.aptHourseYn == 1)
			addr_view3 = (zipVo.cityPublicBldNm != "" ? "(" + zipVo.cityPublicBldNm : "");
		if(addr_view3 != "")
			addr_view3 += ")";
		addr_view4 = "[" + ((zipVo.ri != "" && zipVo.ri != undefined && zipVo.ri != null) ? zipVo.ri : zipVo.dong) + " " + (zipVo.mtYn == '1' ? '<u:msg titleId="em.cols.findZipCode.mt" alt="산" /> ' : '')+zipVo.mainLotNo + (zipVo.subLotNo == "0" ? "" : "-" + zipVo.subLotNo)+(zipVo.cityPublicBldNm == null ? "" : " " +zipVo.cityPublicBldNm);		// 지번 주소
		//if (zipVo.cityPublicBldNm != '')
		//	addr_view4 = addr_view4 + " " + zipVo.cityPublicBldNm + (zipVo.bldDetlNm != "" ? "("+zipVo.bldDetlNm+")" : "");
		addr_view4 += "]";
		addr_view5 = addr_view1;
		addr_view5 += "|"+ addr_view2;
		addr_view5 += "|"+ addr_view3;
		addr_view5 += "|"+ ( zipVo.ri == "" || zipVo.ri == undefined ? zipVo.dong : "");
		addr_view5 += "|"+ ( zipVo.aptHourseYn == 1 ? zipVo.cityPublicBldNm : "");
		addr_view5 += "|"+ zipVo.aptHourseYn;
			
		$('#schResultContainer ul').append(
			'<li><a href="#"><span class="zip">'+ zipVo.zipNo + '</span> '
			+ ' <span class="ad1">' + addr_view1 + '</span>'
			+ ' <span class="ad2">' + addr_view2 + '</span>'
			+ ' <span class="ad3">' + addr_view3 + '</span>'
			+ ' <span class="ad4">' + addr_view4 + '</span>'
			+ ' <span class="ad5" style="display:none;">' + addr_view5 + '</span>'
			+ '</a></li>');
	});
}
<% // 검색 결과에 클릭 이벤트 추가 %>
function initClickEvt(data){
	$('#schResultContainer li').click(function(e){
		$('#setZipReviewTbl').show();
		$('#addrZipNo').val($(this).find("span.zip").text());
		$('#addrInfo').val($(this).find("span.ad1").text() + " " + $(this).find("span.ad2").text());
		$('#detlAdr > input').val('');		// 상세정보 초기화
		var adrDetl = $(this).find("span.ad5").text().split('|');
		$('#detlAdr').find('#referNm').val('');
		if(adrDetl[2] != "" ){
			$('#detlAdr').find('#referNm').val(adrDetl[2]);
		}
		$('#addrBody').animate({ scrollTop: 0 }, 'fast');
		$('#setZipCodeBtn').show();
	});
	$("#schResultCnt").text(data.model.recodeCount);
}<% // 오류메세지 처리 %>
function addErrMsg(errorCode){
	switch (errorCode){
		case '1':
			$("#schResultContainer").hide();
			$('#schResultError').show();
			$("#schResultErrorMsg").text('<u:msg titleId="em.msg.noZipCodeData" alt="※ 검색 결과가 없습니다." />');
			$("#schWord").focus();
			break;
	
		case '2':
			$("#schResultContainer").hide();
			$('#schResultError').show();
			$("#schResultErrorMsg").text('<u:msg titleId="em.msg.notValidSchNm" alt="※ 검색 단어가 너무 짧습니다. 한글은 2자 이상 입력해야 합니다." />');
			$("#schWord").focus();
			break;
	
		case '3':
			$("#schResultErrorMsg").text('<u:msg titleId="em.msg.errorSchResult" alt="※ 검색 결과가 너무 많습니다. 건물번호나 번지를 추가해서 검색해주세요." />');
			$('#schResultError').show();
			$("#schWord").focus();
			break;
	
		default:
			$("#schResultErrorMsg").text("");
			$('#schResultError').hide();
			break;
	}
	$("#schResultCnt").text('');
}
<% // 시도 클릭시 시구군 조회 %>
function initSidoSelectChange(){
	$('#sidoSelect').change(function(){
 		$('#gugunSelect').find('option').each(function(){
 			$(this).remove();
 		});
 		$('#gugunSelect').append('<u:option value="" titleId="em.jsp.findZipCode.gugunSelect" alt="--시군구선택--"/>');
 		$("#gugunSelect option:eq(0)").attr("selected", "selected"); 
 		$('#gugunSelect').uniform();
 		if($(this).val() != ''){
 			$.ajax({
 		        url: '/cm/selectCommonSidoAjx.do?menuId=${menuId}',
 		        type: 'POST',
 		        data:{
 		        	schCat : 'gugun',
 		        	schWord : $(this).val()
 		             },
 		        dataType : "json",
 		        success: function(data){
 		        	$.each(data.model.list , function(index, zipVo) {
 		        		$('#gugunSelect').append('<u:option value="'+zipVo.value+'" title="'+zipVo.label+'"/>');
 		        	});
 		        }
 			});
 		}
 	});
}
<% // 주소찾기 초기화 %>
function initAdrSrch(){
	$('#button_search').click(function(e){
		if($("#zipCodeContainer #schWord").val() == "" || $("#zipCodeContainer #schWord").val().length < 2){
			alertMsg('em.msg.noSchNm');
			return;
		}
		var schCat = $("#zipCodeContainer #schCat").val();
		if(!chkParams(schCat)){
			alertMsg('em.msg.errorValidSchNm');//검색어를 올바르게 입력해 주세요.
			return;
		}
		fnSearchInit();
		$.ajax({
	        url: '/cm/getZipCode.do?menuId=${menuId}',
	        type: 'POST',
	        data:{
	        	schCat : schCat,
	        	schWord : $("#zipCodeContainer #schWord").val(),
	        	schSido : $("#zipCodeContainer #sidoSelect").val(),
	        	schGugun : $("#zipCodeContainer #gugunSelect").val(),
	        	pageRowCnt : 1000,
	        	pageType : '${param.pageType}'
	             },
	        dataType : "json",
	        timeout: 5000,
	        success: function(data){
				$('#schResultContainer').show();
				if(data.model.ptAdrBldInfoBVoList) setZipData(data.model.ptAdrBldInfoBVoList); // 주소결과 세팅
				initClickEvt(data); // 주소클릭 이벤트
				//$("#schResultTime").text(data.model.time);
				if(data.model.errorCode) addErrMsg(data.model.errorCode);
	        },
	        error: function(request, status, error){
	         $("#schResultContainer").hide();
	             if(error=='timeout') alertMsg('em.msg.adrSch.timeout');// em.msg.adrSch.timeout=검색 시간이 많이 소요됩니다. 검색어를 추가로 입력해주세요.
	             else alertMsg('em.msg.adrSchError'); // em.msg.adrSchError=검색 중 오류가 발생하였습니다.
	            loading('zipCodeContainer', false);
	        },
	        beforeSend: function() {
				$('#schResultBar').hide();
				$('#schResultLoading').show().fadeIn('fast');
				loading('zipCodeContainer', true, false);
			},
			complete: function() {
				$('#schResultLoading').hide();
				$('#schResultBar').show();
				loading('zipCodeContainer', false);
			}
	    });
		// 요청 취소
		//xhr.abort();

	 	e.preventDefault();
	});
	
	initSidoSelectChange(); // 시도 선택시 시군구 조회
 	
 	$("#zipCodeContainer #schWord").keydown(function(evt){ // 검색어 엔터
        if( (evt.keyCode) && (evt.keyCode==13) ) {
        	$('#button_search').trigger("click");
        }
    });
}<% // 입력된 주소를 세팅한다. %>
function setZipCode(){
	if (!validator.validate('setZipCodeForm')) {
		return;
	}
	var adrId = '${param.adrId}';
	var zipNo = adrId == ''  ? "zipNo" : (adrId+"ZipNo");
	//var zipNo2 = adrId == ''  ? "zipNo2" : (adrId+"ZipNo2");
	
	var adr = adrId == ''  ? "adr" : (adrId+"Adr");
	<c:if test="${empty param.frameId }">
	$('#'+zipNo).val($('#addrZipNo').val());
	$('#'+adr).val($('#addrInfo').val() + "" + ($('#bldDong').val() == '' ? '' : " "+$('#bldDong').val() + '<u:msg titleId="em.cols.setZipCode.dong" alt="동" />')+"" + ($('#bldHo').val() == '' ? '' : " " +$('#bldHo').val() + '<u:msg titleId="em.cols.setZipCode.ho" alt="호" />')+ ($('#referNm').val() == '' ? '' : " " +$('#referNm').val()));
	</c:if>
	<c:if test="${!empty param.frameId }">
	var frame = getIframeContent('${param.frameId}');
	frame.$('#'+zipNo).val($('#addrZipNo').val());
	frame.$('#'+adr).val($('#addrInfo').val() + "" + ($('#bldDong').val() == '' ? '' : " "+$('#bldDong').val() + '<u:msg titleId="em.cols.setZipCode.dong" alt="동" />')+"" + ($('#bldHo').val() == '' ? '' : " " +$('#bldHo').val() + '<u:msg titleId="em.cols.setZipCode.ho" alt="호" />')+ ($('#referNm').val() == '' ? '' : " " +$('#referNm').val()));
	</c:if>
	
	dialog.close('zipCodePopup');
};

$(document).ready(function(){
	initAdrSrch();
});

</script>

<div id="zipCodeContainer" style="width:650px">
	<% // TAB %>
	<u:tabGroup id="findZipCodeTab">
		<u:tab id="findZipCodeTab"  onclick="fnTabClick('road');" titleId="em.jsp.findZipCode.tab.road" alt="도로명주소" on="true"/>
		<u:tab id="findZipCodeTab"  onclick="fnTabClick('oldpost');" titleId="em.jsp.findZipCode.tab.oldpost" alt="지번" />
		<u:tab id="findZipCodeTab"  onclick="fnTabClick('bldNm');" titleId="em.jsp.findZipCode.tab.bldNm" alt="건물명" />
	</u:tabGroup>
	<div id="schNotice" >
		<div class="color_txt" id="roadNotice"><u:msg titleId="em.jsp.findZipCode.tx01" alt="※ 도로명주소 : 도로명 + 본번 + 부번  예) 통일로 170(170-1 or 170 1)"/></div>
		<div class="color_txt" id="oldpostNotice" style="display:none;"><u:msg titleId="em.jsp.findZipCode.tx02" alt="※ 지번 : 동(읍/면/동)명 + 본번 + 부번 예) 잠실동 330(330-6 or 330 6)"/></div>
		<div class="color_txt" id="bldNmNotice" style="display:none;"><u:msg titleId="em.jsp.findZipCode.tx03" alt="※ 건물명 : 동명 + 건물명(아파트명, 빌라명 등) 예) 양재동 삼성팰리스"/></div>
	</div>
	
	<u:searchArea>
		<u:input type="hidden" id="menuId" value="${menuId}" />
		<u:input type="hidden" id="schCat" value="road"/>
		<table class="search_table" cellspacing="0" cellpadding="0" border="0">
			<tr>
				<td>
					<table border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td>
								<select id="sidoSelect" name="sidoSelect" style="width:110px;">
									<u:option value="" titleId="em.jsp.findZipCode.sidoSelect" alt="--시도선택--"/>
									<c:forEach var="list" items="${sidoList }" varStatus="status">
										<u:option value="${list.value }" title="${list.label }" />
									</c:forEach>												
								</select>
								<select id="gugunSelect" name="sidoSelect" style="width:130px;">
									<u:option value="" titleId="em.jsp.findZipCode.gugunSelect" alt="--시군구선택--"/>
								</select>
							</td>
						</tr>
						<tr>
							<td><u:input id="schWord" maxByte="50" name="schWord" value="" titleId="cols.schWord" style="width: 200px;" mandatory="Y" /></td>
						</tr>
					</table>
				</td>
				<td>
					<div id="button_search" class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:;"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div>
				</td>
			</tr>
		</table>
	</u:searchArea>
	
	<div id="addrBody" style="width:100%;height:370px; overflow:auto;">
		<h4 id="schResultBar" style="display: none"><u:msg titleId="em.jsp.findZipCode.schResult" alt="검색 결과"/> : <span id="schResultCnt"></span><u:msg titleId="em.jsp.findZipCode.schCnt" alt="건"/> <%-- (<span id="schResultTime"></span><u:msg titleId="em.jsp.findZipCode.schSecondTime" alt="초"/>) --%></h4>
		<h4 id="schResultError" style="display: none"><span id="schResultErrorMsg"></span></h4>
		<h4 id="schResultLoading" style="display: none"><u:msg titleId="em.jsp.findZipCode.schStatus" alt="검색중입니다....."/></h4>
		<form id="setZipCodeForm">
			<u:listArea id="setZipReviewTbl" colgroup="25%,*" style="width:96%;display:none;">
				<tr>
					<td>
						<table border="0" cellpadding="0" cellspacing="0" style="width:100%">
							<tbody>
							<tr>
								<td>
									<table border="0" cellpadding="0" cellspacing="0">
										<tbody>
										<tr>
											<td>
												<u:input id="addrZipNo" titleId="cols.addrZipNo" readonly="Y" className="input_center" style="ime-mode:disabled; width:80px" mandatory="Y"/>
											</td>
										</tr>
										</tbody>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<u:input id="addrInfo"  titleId="cols.addrAdr" readonly="Y" style="width:96%" mandatory="Y"/>
								</td>
							</tr>
							</tbody>
						</table>
						<ul id="detlAdr" style="list-style:none;">
							<li id="aptYn1" ><u:msg titleId="em.cols.setZipCode.detlDongHoNm" alt="상세주소(동·호)"/>
								<%-- <u:input id="bldNm" titleId="em.cols.setZipCode.bldNm" style="width:80px;" maxByte="30" /> --%>
								<u:input id="bldDong" titleId="em.cols.setZipCode.dong" style="width:25px;" maxByte="4" valueOption="upper,number"/><u:msg titleId="em.cols.setZipCode.dong" alt="동" />
								<u:input id="bldHo" titleId="em.cols.setZipCode.ho" style="width:25px;" maxByte="4" valueOption="number"/><u:msg titleId="em.cols.setZipCode.ho" alt="호" />
							</li>
							<li id="aptYn0" ><u:msg titleId="em.cols.setZipCode.detlEtcCont" alt="상세주소(참고항목)"/>
								<u:input id="referNm" titleId="em.cols.setZipCode.referNm" style="width:150px;" maxByte="100" readonly="Y"/>
							</li>
						</ul>
					</td>
				</tr>
			</u:listArea>
		</form>
		
		<div id="schResultContainer" style="display:none;">
			<ul></ul>
		</div>
	</div>

	<% // 하단 버튼 %>
	<u:buttonArea>
		<u:button id="setZipCodeBtn" title="입력완료" alt="입력완료" onclick="setZipCode();" style="display:none;"/>
		<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
	</u:buttonArea>
	
</div>