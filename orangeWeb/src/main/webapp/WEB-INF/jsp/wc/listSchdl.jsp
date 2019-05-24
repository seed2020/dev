<%@ page  language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"%>

<u:set test="${!empty param.fncCal}" var="fncCal" value="${param.fncCal}" />

<script type="text/javascript">
<!--

//나의일정검색 viewProm값 전달
function viewSearchMyScd(){//return : {subj, loc, dt, openGradCd, cont, scds_promGstUids, scds_promGstNms, scds_promGstDptNms}
	var scds_promGstNms = [];
	var scds_promGstDptNms = [];
	var scds_promGstUids = [];
	var $selectRow=$("#"+$("#scds_schdlId").val());
	//수정,삭제버튼 none flag;
	var fnc = "${param.fncCal}";
	if(fnc=='mng'){
		var searchMylist = "";	
	}else{
		var searchMylist = "searchMylist";	
	}
	
	var $scds_schdlId = $("#scds_schdlId").val();
	var $scds_subj = $selectRow.find("#scds_subj").val();
	var $scds_locNm = $selectRow.find("#scds_locNm").val();
	var $scds_Dt = $selectRow.find("#scds_scds_Dt").val();
	var $scds_openGradCd = $selectRow.find("#scds_openGradCd").val();
	var $scds_cont = $selectRow.find("#scds_cont").val();
	var $scds_startYmd = $selectRow.find("#scds_startYmd").val();
	var $scds_endYmd = $selectRow.find("#scds_endYmd").val();
	var $scds_startHHmm = $selectRow.find("#scds_startHHmm").val();
	var $scds_endHHmm = $selectRow.find("#scds_endHHmm").val();
	var $scds_holiYn = $selectRow.find("#scds_holiYn").val();
	var $scds_solaLunaYn = $selectRow.find("#scds_solaLunaYn").val();
	//할일우선순위
	var $scds_workPrioOrdr =  $selectRow.find("#scds_workPrioOrdr").val();
	var $scds_schdlStatCd =  $selectRow.find("#scds_schdlStatCd").val();
	
	//할일 완료날짜
	var $workCmltYmd = $selectRow.find("#scds_workCmltYmd").val();
	
	var $selctCol = $selectRow.find("#promGstLst");
	
	 $selctCol.find("#scds_promGstUid").each(function(){
		 scds_promGstUids.push($(this).val());
	 });
	 
	 $selctCol.find("#scds_promGstNm").each(function(){
		 scds_promGstNms.push($(this).val());
	 });

	 $selctCol.find("#scds_promGstDptNm").each(function(){
		 scds_promGstDptNms.push($(this).val()); 
	 });
	
	return {subj:$scds_subj, loc:$scds_locNm, dt:$scds_Dt,  openGradCd:$scds_openGradCd, cont:$scds_cont, workPrioOrdr:$scds_workPrioOrdr,solaLunaYn:$scds_solaLunaYn,
			startYmd:$scds_startYmd, endYmd:$scds_endYmd,schdlStatCd:$scds_schdlStatCd,startHHmm:$scds_startHHmm, endHHmm:$scds_endHHmm,holiYn:$scds_holiYn,workCmltYmd:$workCmltYmd,
			scds_promGstUids:scds_promGstUids, scds_promGstNms:scds_promGstNms, scds_promGstDptNms:scds_promGstDptNms, searchMylist:searchMylist, schdlId:$scds_schdlId};
}

function gubun1Chk(){

	$("#chkProm").val(($("input:checkbox[id='PROM']").is(":checked") == true ? "true" : "false" ));
	$("#chkWork").val(($("input:checkbox[id='WORK']").is(":checked") == true ? "true" : "false" ));
	$("#chkEvnt").val(($("input:checkbox[id='EVNT']").is(":checked") == true ? "true" : "false" ));	
	$("#chkAnnv").val(($("input:checkbox[id='ANNV']").is(":checked") == true ? "true" : "false" ));
}

function gubun2Chk(){

	$("#chkPsn").val(($("input:checkbox[id='PSN']").is(":checked") == true ? "true" : "false" ));
	$("#chkGrp").val(($("input:checkbox[id='GRP']").is(":checked") == true ? "true" : "false" ));
	$("#chkComp").val(($("input:checkbox[id='COMP']").is(":checked") == true ? "true" : "false" ));	
	$("#chkDept").val(($("input:checkbox[id='DEPT']").is(":checked") == true ? "true" : "false" ));
}



function viewProm(scds_schdlId) {
	//유일값 hidden으로 숨기기	
	$("#scds_schdlId").val(scds_schdlId);
	dialog.open('viewPromPop','<u:msg titleId="wc.jsp.viewProm.title" alt="약속 조회" />','./viewPromPop.do?menuId=${menuId}&fncCal=${fncCal}&scds_schdlId='+scds_schdlId+'');
}

function viewWork(scds_schdlId) {
	//유일값 hidden으로 숨기기	
	$("#scds_schdlId").val(scds_schdlId);
	dialog.open('viewWorkPop','<u:msg titleId="wc.jsp.viewWork.title" alt="할일 조회" />','./viewWorkPop.do?menuId=${menuId}&fncCal=${param.fncCal}&scds_schdlId='+scds_schdlId+'');
}

function viewEvnt(scds_schdlId) {
	//유일값 hidden으로 숨기기	
	$("#scds_schdlId").val(scds_schdlId);
	dialog.open('viewEvntPop','<u:msg titleId="wc.jsp.viewEvnt.title" alt="행사 조회" />','./viewEvntPop.do?menuId=${menuId}&fncCal=${fncCal}&scds_schdlId='+scds_schdlId+'');
}

function viewAnnv(scds_schdlId) {
	//유일값 hidden으로 숨기기	
	$("#scds_schdlId").val(scds_schdlId);
	dialog.open('viewAnnvPop','<u:msg titleId="wc.jsp.viewAnnv.title" alt="기념일 조회" />','./viewAnnvPop.do?menuId=${menuId}&fncCal=${fncCal}&scds_schdlId='+scds_schdlId+'');
}

$(document).ready(function() {
	
	
	//달력셋팅
	$("#choiDt").val($("#startDt").val());
	$("#cmltDt").val($("#endDt").val());
	
	setUniformCSS();
	
});

function betweenCalDay (startYmd, endYmd){

	var startDateArray = startYmd.split("-");
	var endDateArray = endYmd.split("-");  
	
	var startDateObj = new Date(startDateArray[0], Number(startDateArray[1])-1, startDateArray[2]);
	var endDateObj = new Date(endDateArray[0], Number(endDateArray[1])-1, endDateArray[2]);
	
	var betweenDay = (endDateObj.getTime() - startDateObj.getTime())/1000/60/60/24; 
	//ex) (2014-05-20) - (2014-05-15) = 5일이지만 당일 포함 6일간 이니 +1 해줌
	return betweenDay + 1;
}
//-->
</script>

<u:title titleId="wc.jsp.listSchedl.title" alt="일정검색" menuNameFirst="true"/>

<input id="scds_subj" type="hidden"/> 
<input id="subj_id" type="hidden"/>

<input id="schdlGroupId" type="hidden" value=""/>
<input id="scds_promGstUid" type="hidden" value=""/>
<input id="scds_promGstNm" type="hidden" value=""/>
<input id="scds_promGstDptNm" type="hidden" value=""/>


<!--  검색영역  -->
<u:searchArea>
	<form name="searchForm" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>

		<td class="search_tit"><u:msg titleId="cols.subj" alt="제목" /></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 400px;" onkeydown="if (event.keyCode == 13) searchForm.submit();"/></td>
		<td class="width20"></td>
		<td class="search_tit"><u:msg titleId="cols.prd" alt="기간" /></td>
		<td colspan="3"><table border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			
			<!-- <td><u:input id="strtYmd" value="${strtYmd}" titleId="cols.ymd" style="width:80px;" className="input_center" /></td> -->
			<td><u:calendar id="choiDt" option="{end:'cmltDt'}" titleId="cols.choiDt" alt="선택일시" />
				<input name ="startDT" id="startDt" value="${startDt}" type="hidden"/></td>
			<td class="body_lt">~</td>
			<!-- <td><u:input id="endYmd" value="${endYmd}" titleId="cols.ymd" style="width:80px;" className="input_center" /></td> -->
			<td><u:calendar id="cmltDt" option="{start:'choiDt'}" titleId="cols.cmltDt" alt="완료일시" />
				<input name ="endDT" id="endDt" value="${endDt}" type="hidden"/></td>
			</tr>
			</tbody></table></td>
		</tr>
		<u:input type="hidden" id="fncCal" value="${fncCal}"/>
		<tr>
		<td class="search_tit"><u:msg titleId="cols.typ" alt="구분" />1</td>
		<td><u:checkArea>
			<u:checkbox name="gubun1" id ="PROM" value="PROM" titleId="cols.prom" checked="${chkProm}" inputClass="bodybg_lt" onclick="gubun1Chk()"/>
				<input name ="chkProm" id="chkProm" value="${chkProm}" type="hidden"/>
			<u:checkbox name="gubun1" id="WORK" value="WORK" titleId="cols.work" checked="${chkWork}" inputClass="bodybg_lt" onclick="gubun1Chk()"/>
				<input name ="chkWork" id="chkWork" value="${chkWork}" type="hidden"/>
			<u:checkbox name="gubun1" id="EVNT" value="EVNT" titleId="cols.evnt" checked="${chkEvnt}" inputClass="bodybg_lt" onclick="gubun1Chk()"/>
				<input name ="chkEvnt" id="chkEvnt" value="${chkEvnt}" type="hidden"/>
			<u:checkbox name="gubun1" id="ANNV" value="ANNV" titleId="cols.annv" checked="${chkAnnv}" inputClass="bodybg_lt" onclick="gubun1Chk()"/>
				<input name ="chkAnnv" id="chkAnnv" value="${chkAnnv}" type="hidden"/>
			</u:checkArea></td>
		<td class="width20"></td>
		<td class="search_tit"><u:msg titleId="cols.typ" alt="구분" />2</td>
		<td><u:checkArea>
			<u:checkbox name="gubun2" id ="PSN" value="PSN" titleId="wc.option.psnSchdl" checked="${chkPsn}" inputClass="bodybg_lt" onclick="gubun2Chk()"/>
				<input name ="chkPsn" id="chkPsn" value="${chkPsn}" type="hidden"/>
			<u:checkbox name="gubun2" id ="DEPT" value="DEPT" titleId="wc.option.deptSchdl" checked="${chkDept}" inputClass="bodybg_lt" onclick="gubun2Chk()"/>
				<input name ="chkDept" id="chkDept" value="${chkDept}" type="hidden"/>
			<u:checkbox name="gubun2" id ="COMP" value="COMP" titleId="wc.option.compSchdl" checked="${chkComp}" inputClass="bodybg_lt" onclick="gubun2Chk()"/>
				<input name ="chkComp" id="chkComp" value="${chkComp}" type="hidden"/>
			<u:checkbox name="gubun2" id ="GRP" value="GRP" titleId="wc.option.grpSchdl" checked="${chkGrp}" inputClass="bodybg_lt" onclick="gubun2Chk()"/>
				<input name ="chkGrp" id="chkGrp" value="${chkGrp}" type="hidden"/>
			</u:checkArea></td>
		</tr>
		</table></td>
		<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
		</tr>
	</table>
	</form>
</u:searchArea>

<!-- 목록   -->
<u:listArea id="listArea">
	<tr>
	<td width="6%" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
	<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td class="head_ct"><u:msg titleId="cols.typ" alt="구분" />1</td>
	<td class="head_ct"><u:msg titleId="cols.typ" alt="구분" />2</td>
	<td class="head_ct"><u:msg titleId="cols.prd" alt="기간" /></td>
	<td class="head_ct"><u:msg titleId="cols.note" alt="비고" /></td>
	</tr>
	
	
	<c:forEach  var="scds" items="${schdlGroupBMapList}"  varStatus="status">
		
	<!--  Ymd Start/End Dt -->
	<fmt:parseDate var="dateStartTempParse" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
		<fmt:formatDate var="startYmd" value="${dateStartTempParse}" pattern="yyyy-MM-dd"/> 
	<fmt:parseDate var="dateEndTempParse" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
		<fmt:formatDate var="endYmd" value="${dateEndTempParse}" pattern="yyyy-MM-dd"/>
	
	<!--  HH:mm Start/End Dt -->
	<fmt:parseDate var="dateStartTempParse" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
		<fmt:formatDate var="startHHmm" value="${dateStartTempParse}" pattern="HH:mm"/> 
	<fmt:parseDate var="dateEndTempParse" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
		<fmt:formatDate var="endHHmm" value="${dateEndTempParse}" pattern="HH:mm"/>
		 
		 
	<c:choose>
			<c:when test="${scds.schdlTypCd == '1'}"> 
				<c:set var="scds_typeCd"	value= "약속" />
				<c:set var="viewFunction"	value= "viewProm('${scds.schdlId}')" />
				<c:set var="scds_Dt" value="${startYmd} ~ ${endYmd} (${startHHmm} ~ ${endHHmm})" />
			</c:when>
			<c:when test="${scds.schdlTypCd == '2'}"> 
				<c:set var="scds_typeCd"	value= "할일" />
				<c:set var="viewFunction"	value= "viewWork('${scds.schdlId}')" />
				<c:set var="scds_Dt" value="${startYmd}" />
			</c:when>
			<c:when test="${scds.schdlTypCd == '3'}"> 
				<c:set var="scds_typeCd"	value= "행사" />
				<c:set var="viewFunction"	value= "viewEvnt('${scds.schdlId}')" />
				<c:set var="scds_Dt" value="${startYmd}" />
			</c:when>
			<c:when test="${scds.schdlTypCd == '4'}"> 
				<c:set var="scds_typeCd"	value= "기념일" />
				<c:set var="viewFunction"	value= "viewAnnv('${scds.schdlId}')" />
				<c:set var="scds_Dt" value="${startYmd}" />
			</c:when>
	</c:choose>
	<c:choose>
			<c:when test="${scds.schdlKndCd == 1}"> 
				<c:set var="scds_kndCd"	value= "개인일정" />
			</c:when>
			<c:when test="${scds.schdlKndCd == 2}"> 
				<c:set var="scds_kndCd"	value= "그룹일정" />
			</c:when>
			<c:when test="${scds.schdlKndCd == 3}"> 
				<c:set var="scds_kndCd"	value= "회사일정" />
			</c:when>
			<c:when test="${scds.schdlKndCd == 4}"> 
				<c:set var="scds_kndCd"	value= "부서일정" />
			</c:when>
	</c:choose>
	
	<tr onmouseover='this.className="trover"' id="${scds.schdlId}" onmouseout='this.className="trout"'>
		
		<td class="body_ct" style="width:2%">${scds.rnum}</td>	
		<td class="body_lt" style="width:35%" id="promGstLst">
			<!--  약속 포함 기본값 -->
			 <a href="javascript:${viewFunction}"> <u:out value="${scds.subj}" maxLength="65" /></a>
			 <input id="scds_subj" type = "hidden" value="${scds.subj}" />
			 <input id="scds_schdlId" type = "hidden" />
			 <input id="scds_locNm" type="hidden" value="${scds.locNm}"/>
			 <input id="scds_openGradCd" type="hidden" value="${scds.openGradCd}"/>
			 <input id="scds_scds_Dt" type="hidden" value="${scds_Dt}"/>
			 <input id="scds_cont" type="hidden" value="${scds.cont}"/>
			 <!-- 참석자 -->
			 <c:forEach  var="promGst" items="${scds.promGuestLst}"  varStatus="status">
			 	<input id="scds_promGstUid" type="hidden" value="${promGst.guestUid}" />
			 	<input id="scds_promGstNm" type="hidden" value="${promGst.guestNm}" />
			 	<input id="scds_promGstDptNm" type="hidden" value="${promGst.guestDeptNm}" />
			 </c:forEach>
			 <!-- 기본값 제외한 할일  -->
			 <input id="scds_workPrioOrdr" type="hidden" value="${scds.workPrioOrdr}"/>
			 <input id="scds_startYmd" type="hidden" value="${startYmd}" />
			 <input id="scds_endYmd" type="hidden" value="${endYmd}"/>
			 <input id="scds_startHHmm" type="hidden" value="${startHHmm}" />
			 <input id="scds_endHHmm" type="hidden" value="${endHHmm}"/>
			 <input id="scds_schdlStatCd" type="hidden" value="${scds.schdlStatCd}"/>
			 <input id="scds_holiYn" type="hidden" value="${scds.holiYn}"/>
			 <input id="scds_solaLunaYn" type="hidden" value="${scds.solaLunaYn}"/>
			 
		 	<fmt:parseDate var="workDateEndDt" value="${scds.workCmltYmd}" pattern="yyyy-MM-dd HH:mm:ss"/>
		    <fmt:formatDate var="workCmltYmd" value="${workDateEndDt}" pattern="yyyy-MM-dd"/>
			<input type="hidden" id="scds_workCmltYmd"  value="${workCmltYmd}"/>

		</td>
		<td class="body_ct" style="width:8%">${scds_typeCd}</td>
		<td class="body_ct" style="width:8%">${scds_kndCd}</td>	
		<td class="body_ct" style="width:35%">${scds_Dt}</td>
		<td class="body_ct" style="width:8%">${scds.afterDay} <u:msg titleId="wc.jsp.viewWorkPop.tx02" alt="일간" /></td>
	</tr>
		
	</c:forEach>
	<c:if test="${fn:length(schdlGroupBMapList) == 0}">
		<tr>
		<td class="nodata" colspan="6"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
	
</u:listArea>

<u:pagination />

