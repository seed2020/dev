<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="paramsForList" excludes="rezvId"/>
<style>
.bodytime_green { color:#454545; background:#ccffcc; padding:0; }
.bgcoltxt { float:left; font-size:12px; color:#454545; margin:1px 3px 0 0; display:block;width:80px;text-indent:5px; }
.bgcoltxt div{background:#ffffff;}
</style>
<script type="text/javascript">
<!--
<% // 회사변경 %>
function chnComp(obj){
	var $form = $('#searchForm');	
	$form.find("input[name='paramCompId']").remove();
	if(obj.value!='')
		$form.appendHidden({name:'paramCompId',value:obj.value});
	$form.find("#rescKndId, #rescMngId").remove();
	$form[0].submit();
}
<% // 엑셀 파일 다운로드 %>
function excelDownFile() {
	var $form = $('#excelForm');
	$form.attr('method','post');
	$form.attr('action','./excelDownLoad.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form[0].submit();
};

//색상보기
function fnBgcolToggle(){
	$('#bgcolYn').val($('#bgcolYn').val() == 'Y' ? 'N' : 'Y');
	$('#bgcolArea').toggle();
};

//더보기 팝업
function listRezvMorePop(rezvYmd) {
	var regExp = /([0-9]{4})([0-9]{2})([0-9]{2})$/;
	if(regExp.test(rezvYmd)){
		var format = '-';
		rezvYmd = RegExp.$1 +format+RegExp.$2+format+RegExp.$3;
	}
	var url = './listRezvStatPop.do?menuId=${menuId}';
	url+="&durCat=fromYmd";
	url+="&durStrtDt="+rezvYmd;
	url+="&durEndDt="+rezvYmd;
	dialog.open('listRezvStatPop','<u:msg titleId="wr.btn.rezvStat" alt="예약현황"/>',url);
};

//조회조건 초기화
function fnSchValInit(id){
	$('#'+(id == undefined ? 'search_area_all' : id )+' input').val('');
};

//마우스 오버시 css 변경
function fnRezvMouseOver(areaId , type , obj){
	$('.timeArea').removeClass('trover');
	
	if(areaId == '' && type == 'trover'){
		$(obj).addClass('trover');	
		return;
	}
	if(type == 'trover'){
		$('.'+areaId).addClass('trover');	
	}
};

//이전 다음 버튼
function fnPageMove(value){
	$('#schedulePmValue').val(value);
	$('#schDay').val('');
	searchForm.submit();
};

//자원상세보기 팝업
function viewRescPop(rescMngId){
	dialog.open('setRescPop', '<u:msg titleId="wb.jsp.viewRescMngPop.title" alt="자원상세보기" />', './viewRescPop.do?menuId=${menuId}&rescMngId='+rescMngId);
};

//등록 수정 팝업
function setRezvPop(rezvId , obj) {
	var url = './setRezvPop.do?${paramsForList}&listPage=${listPage}';
	
	var title = '<u:msg titleId="wr.jsp.setRezv.title" alt="자원예약"/>';
	if(rezvId != null){
		url+= '&rezvId='+rezvId;
		title+='-<u:msg titleId="cols.mod" alt="수정"/>';
	}
	
	if(obj != null){
		var strtYmd = $(obj).attr('data-day');
		var strtTime = $(obj).attr('data-time');
		url+= "&rezvStrtDt="+strtYmd+" "+strtTime;
	}
	
	dialog.open('setRezvDialog', title, url);
	dialog.onClose("setRezvDialog", function(){ editor('cont').clean(); unloadEvent.removeEditor('cont'); });
};

//상세보기
function viewRezvPop(rezvId,e) {
/* 	 e = e ? e : window.event;
     if(e.stopPropagation){ e.stopPropagation(); } 
     else{ e.cancelBubble = true; } */
     
	var url = './viewRezvPop.do?${paramsForList}&listPage=${listPage}';
	if(rezvId != null){
		url+= '&rezvId='+rezvId;
	}
	dialog.open('setRezvPop','<u:msg titleId="wr.jsp.setRezv.title" alt="자원예약"/>',url);
	//location.href = url;
};

//전체 및 본인 예약 현황 조회
function fnMySearch(fncMy){
	$('#fncMy').val(fncMy);
	searchForm.submit();
};

//주간 월간 보기
function fnListSearch(listType){
	$('#listType').val(listType);
	searchForm.submit();
};

//1명의 사용자 선택
function schUserPop(){
	var data = {};<%// 팝업 열때 선택될 데이타 %>
	<%// option : data, multi, withSub, titleId %>
	searchUserPop({data:data}, function(userVo){
		$('#schRegrUid').val(userVo.userUid);
		$('#schRegrNm').val(userVo.rescNm);
	});
};

<%// 선택삭제%>
function fnDeleteList(){
	var arr = [];
	$("#listArea tbody:first input[name='idCheck']:checked").each(function(){
		arr.push($(this).val());
	});
	if(arr.length==0){
		alertMsg('cm.msg.noSelect');
		return;
	}
	$("#delList").val(arr.join(','));
	if(confirmMsg("cm.cfrm.del")) {
		var $form = $('#deleteListForm');
		$form.attr('method','post');
		$form.attr('action','./transRezvDel.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
	
};

$(document).ready(function() {
	setUniformCSS();
	$('#rescKndId').change(function(){
 		$('#rescMngId').find('option').each(function(){
 			$(this).remove();
 		});
 		<c:if test="${listPage ne 'listRezvStat' || ( listPage eq 'listRezvStat' && !empty param.listType && param.listType ne 'week') }">
 			$('#rescMngId').append('<u:option value="" titleId="cm.option.all" alt="전체선택"/>');
 		</c:if>
 		$("#rescMngId option:eq(0)").attr("selected", "selected"); 
 		$('#rescMngId').uniform();
 		if($(this).val() != ''){
 			$.ajax({
 		        url: './selectRescAjx.do?menuId=${menuId}',
 		        type: 'POST',
 		        data:{
 		        	rescKndId : $(this).val()
 		             },
 		        dataType : "json",
 		        success: function(data){
 		        	$.each(data.model.list , function(index, rescMngVo) {
 		        		$('#rescMngId').append('<u:option value="'+rescMngVo.rescMngId+'" title="'+rescMngVo.rescNm+'"/>');
 		        	});
 		        	$('#rescMngId').uniform();
 		        }
 			});
 		}
 	});
	<c:if test="${autoPopup==true && !empty param.rezvId}">viewRezvPop('${param.rezvId}');</c:if>
	<c:if test="${!empty autoMsg}">alert('${autoMsg}');</c:if>
});
//-->
</script>
<u:set test="${param.fncMy == 'Y'}" var="fnc" value="my" elseValue="all" />


<div class="front notPrint">
	<div class="front_left">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td style="padding:3px 4px 0 0px"><c:choose>
	<c:when test="${listPage eq 'listRezvStat' }"><u:title titleId="wr.jsp.listRezvStat.${fnc}.${empty param.listType ? 'week' : param.listType}.title" alt="전체예약목록" menuNameFirst="True" /></c:when>
	<c:when test="${listPage eq 'listRezvSrch' || listPage eq 'listRezvSrchMng'}"><u:title titleId="wr.jsp.listRezvSrch.title" alt="예약자원검색" menuNameFirst="True" /></c:when>
	<c:when test="${listPage eq 'listRezvDisc' }"><u:title titleId="wr.jsp.listRezvDisc.title" alt="예약심의목록" menuNameFirst="True" /></c:when>
	<c:otherwise><u:title titleId="wr.jsp.listRezv.${fnc}.title" alt="전체예약목록" menuNameFirst="True" /></c:otherwise>
</c:choose></td>
			<c:if test="${listPage ne 'listRezvDisc' && !empty ptCompBVoList }">
				<td class="width5"></td>
				<td class="frontinput">
					<select name="paramCompId" <u:elemTitle titleId="cols.comp" /> style="min-width:100px;" onchange="chnComp(this);">
					<c:forEach items="${ptCompBVoList}" var="ptCompBVo" varStatus="status">
						<u:option value="${ptCompBVo.compId}" title="${ptCompBVo.rescNm}" checkValue="${param.paramCompId }"/>
					</c:forEach>
				</select>
				</td>
			</c:if>
	 		</tr>
		</table>
	</div>
</div>

<% // 상단 주,월 이동 %>
<c:if test="${listPage eq 'listRezvStat' || listPage eq 'listRezv' }">
<div class="front">
	<c:if test="${listPage eq 'listRezvStat' }">
		<div class="front_left">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<c:choose>
						<c:when test="${param.listType eq 'month' }">
							<td class="frontico"><a href="javascript:" onclick="fnPageMove('m');"><img src="${_ctx}/images/${_skin}/ico_left.png" width="20" height="20" /></a></td>
							<td class="scd_head">${fn:substring(wrMonthVo.strtDt,0,7) }</td>
							<td class="frontico"><a href="javascript:" onclick="fnPageMove('p');"><img src="${_ctx}/images/${_skin}/ico_right.png" width="20" height="20" /></a></td>
						</c:when>
						<c:when test="${param.listType eq 'day' }">
							<td class="frontico"><a href="javascript:" onclick="fnPageMove('m');"><img src="${_ctx}/images/${_skin}/ico_left.png" width="20" height="20" /></a></td>
							<td class="scd_head">${wrDayVo.days }</td>
							<td class="frontico"><a href="javascript:" onclick="fnPageMove('p');"><img src="${_ctx}/images/${_skin}/ico_right.png" width="20" height="20" /></a></td>
						</c:when>
						<c:otherwise>
							<td class="frontico"><a href="javascript:" onclick="fnPageMove('m');"><img src="${_ctx}/images/${_skin}/ico_left.png" width="20" height="20" /></a></td>
							<td class="scd_head">${wrWeekVo.strtDt} ~ ${wrWeekVo.endDt}</td>
							<td class="frontico"><a href="javascript:" onclick="fnPageMove('p');"><img src="${_ctx}/images/${_skin}/ico_right.png" width="20" height="20" /></a></td>
						</c:otherwise>
					</c:choose>
					<td><u:buttonS href="javascript:;" titleId="ct.cols.today" alt="오늘" onclick="fnPageMove('t');"/></td>
				</tr>
			</table>
		</div>
	</c:if>
	<div class="front_right">
		<table border="0" cellpadding="0" cellspacing="0">
			<tbody>
			<tr>
				<c:if test="${listPage eq 'listRezvStat' }">
					<td class="frontbtn">
					<c:choose>
						<c:when test="${param.listType == 'month'}"><u:buttonS href="javascript:;" titleId="wr.btn.listRezvWeek" alt="주간보기" onclick="fnListSearch('week');"/></c:when>
						<c:otherwise><u:buttonS href="javascript:;" titleId="wr.btn.listRezvMonth" alt="월간보기" onclick="fnListSearch('month');"/></c:otherwise>
					</c:choose>
					</td>
				</c:if>
				<td class="frontbtn">
				<c:choose>
					<c:when test="${param.fncMy == 'Y'}"><u:buttonS href="javascript:;" titleId="wr.btn.allRezvStat" alt="전체예약현황" onclick="fnMySearch('N');"/></c:when>
					<c:otherwise><u:buttonS href="javascript:;" titleId="wr.btn.myRezvStat" alt="본인예약현황" onclick="fnMySearch('Y');"/></c:otherwise>
				</c:choose>
				</td>
			</tr>
			</tbody>
		</table>
	</div>
</div>

<u:blank />
</c:if>
<% // 검색 영역 %>
<u:searchArea>
<form name="searchForm" id="searchForm" action="./${listPage }.do" >
<u:input type="hidden" id="menuId" value="${menuId}" />
<c:if test="${listPage eq 'listRezvStat' }">
	<u:input type="hidden" id="schedulePmValue" />
	<c:choose>
		<c:when test="${param.listType eq 'month' }"><u:input type="hidden" id="startDay" value="${wrMonthVo.strtDt}"/></c:when>
		<c:when test="${param.listType eq 'day' }"><u:input type="hidden" id="startDay" value="${wrDayVo.days}"/></c:when>
		<c:otherwise><u:input type="hidden" id="startDay" value="${empty paramDay ? wrWeekVo.strtDt : paramDay}"/></c:otherwise>
	</c:choose>
</c:if>
<c:if test="${listPage eq 'listRezvStat' || listPage eq 'listRezv'}"><u:input type="hidden" id="fncMy" value="${param.fncMy }"/></c:if>
<c:if test="${listPage eq 'listRezvStat' }">
	<u:input type="hidden" id="listType" value="${param.listType }"/>
	<u:input type="hidden" id="bgcolYn" value="${param.bgcolYn }"/>
</c:if>

<c:if test="${!empty param.paramCompId }"><u:input type="hidden" id="paramCompId" value="${param.paramCompId}" /></c:if>

<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="search_tit"><u:msg titleId="cols.rescKnd" alt="자원종류" /></td>
					<td>
						<select id="rescKndId" name="rescKndId"  style="width:150px;">
							<c:if test="${listPage ne 'listRezvStat' || ( listPage eq 'listRezvStat' && !empty param.listType && param.listType ne 'week') }"><u:option value="" titleId="cm.option.all" alt="전체선택"/></c:if>
							<c:forEach items="${wrRescKndBVoList}" var="list" varStatus="status">
								<u:option value="${list.rescKndId}" title="${list.kndNm}" selected="${param.rescKndId == list.rescKndId}"/>
							</c:forEach>
						</select>
					</td>
					<c:if test="${listPage eq 'listRezvStat' && param.listType eq 'month' }">
						<td>
							<u:buttonS href="javascript:;" titleId="wr.btn.bgcol.show" alt="색상보기" onclick="fnBgcolToggle();"/>							
						</td>
					</c:if>
					<td class="width20"></td>
					<td class="search_tit"><u:msg titleId="cols.rescNm" alt="자원명" /></td>
					<td>
						<select id="rescMngId" name="rescMngId" style="width:220px;">
							<c:if test="${listPage ne 'listRezvStat' || ( listPage eq 'listRezvStat' && !empty param.listType && param.listType ne 'week') }"><u:option value="" titleId="cm.option.all" alt="전체선택"/></c:if>
							<c:forEach items="${wrRescMngBVoList}" var="list" varStatus="status">
								<u:option value="${list.rescMngId}" title="${list.rescNm}" selected="${param.rescMngId == list.rescMngId}"/>
							</c:forEach>
						</select>
					</td>
					<td class="width20"></td>
					<c:choose>
						<c:when test="${listPage eq 'listRezvStat' }">
							<td class="search_tit"><u:msg titleId="wr.cols.schSpecialYmd.title" alt="특정일자조회" /></td>
							<td><u:calendar id="schDay" value="${param.schDay}" titleId="wr.cols.rezvEndYmd" /></td>
						</c:when>
						<c:when test="${listPage eq 'listRezv' }">
							<td>
								<table id="search_area_all" border="0" cellpadding="0" cellspacing="0">
									<tr>
										<td class="search_tit" ><u:msg titleId="cols.rezvPrd" alt="예약기간" /></td>
										<td>
											<input type="hidden" name="durCat" value="fromYmd"/>
											<u:calendar id="durStrtDt" value="${param.durStrtDt}" />
										</td>
										<td class="search_titx">&nbsp;&nbsp;~</td>
										<td><u:calendar id="durEndDt" value="${param.durEndDt}" /></td>
									</tr>
								</table>
							</td>
						</c:when>
						<c:when test="${listPage eq 'listRezvDisc' }">
							<td class="width20"></td>
								<td class="search_tit"><u:msg titleId="wr.cols.rezvStat" alt="예약상태" /></td>
								<td>
									<select id="discStatCd" name="discStatCd"  style="width:80px;">
										<u:option value="" titleId="cm.option.all" alt="전체선택" selected="${empty param.discStatCd }"/>
										<u:option value="R" titleId="wr.jsp.discStatCdR.title" alt="진행중" checkValue="${param.discStatCd }"/>
										<u:option value="A" titleId="wr.jsp.discStatCdA.title" alt="승인" checkValue="${param.discStatCd }"/>
										<u:option value="J" titleId="wr.jsp.discStatCdJ.title" alt="반려" checkValue="${param.discStatCd }"/>
									</select>
								</td>
						</c:when>
						<c:otherwise>
						</c:otherwise>
					</c:choose>
				</tr>
				<c:choose>
					<c:when test="${listPage eq 'listRezvSrch' || listPage eq 'listRezvSrchMng' }">
						<tr>
							<td class="search_tit"><u:msg titleId="wr.cols.rezvNm" alt="예약자"/></td>
							<td>
								<u:input type="hidden" id="schRegrUid" value="${param.schRegrUid}" />
								<u:input id="schRegrNm" name="schRegrNm" value="${param.schRegrNm}" titleId="cols.regr" style="width:70px;" readonly="Y"/>
								<u:buttonS titleId="cm.btn.choice" alt="선택" href="javascript:;" onclick="schUserPop();"/>
								<u:buttonS titleId="cm.btn.del" alt="삭제" href="javascript:;" onclick="$('#schRegrUid').val('');$('#schRegrNm').val('');"/>
								<%-- <u:input id="regrNm" name="regrNm" value="${param.regrNm}" titleId="wr.cols.rezvNm" style="width:120px;" onkeydown="if (event.keyCode == 13) searchForm.submit();"/> --%>
							</td>
							<td class="width20"></td>
							<td class="search_tit"><u:msg titleId="cols.rezvPrd" alt="예약기간" /></td>
							<td>
								<table id="search_area_all" border="0" cellpadding="0" cellspacing="0">
									<tr>
										<td>
											<input type="hidden" name="durCat" value="fromYmd"/>
											<u:calendar id="durStrtDt" value="${param.durStrtDt}" />
										</td>
										<td  class="search_titx">&nbsp;&nbsp;~</td>
										<td><u:calendar id="durEndDt" value="${param.durEndDt}" /></td>
									</tr>
								</table>
							</td>
							<td class="width20"></td>
							<td class="search_tit"><u:msg titleId="wr.cols.rezvStat" alt="예약상태" /></td>
							<td>
								<select id="discStatCd" name="discStatCd"  style="width:80px;">
									<u:option value="" titleId="cm.option.all" alt="전체선택" selected="${empty param.discStatCd }"/>
									<u:option value="B" titleId="wr.jsp.discStatCdB.title" alt="심의없음" checkValue="${param.discStatCd }"/>
									<u:option value="R" titleId="wr.jsp.discStatCdR.title" alt="진행중" checkValue="${param.discStatCd }"/>
									<u:option value="A" titleId="wr.jsp.discStatCdA.title" alt="승인" checkValue="${param.discStatCd }"/>
									<u:option value="J" titleId="wr.jsp.discStatCdJ.title" alt="반려" checkValue="${param.discStatCd }"/>
								</select>
							</td>
						</tr>
					</c:when>
				</c:choose>
			</table>
		</td>
		<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
</table>
</form>
</u:searchArea>
<c:set var="admAuth" value="N"/>
<u:secu auth="SYS" ><c:set var="admAuth" value="Y"/></u:secu>
<u:secu auth="A" ><c:set var="admAuth" value="Y"/></u:secu>
<c:if test="${listPage ne 'listRezvSrchMng' }"><c:set var="admAuth" value="N"/></c:if>

<c:set var="strtTime" value="07:00" scope="page"/>
<c:set var="endTime" value="24:00" scope="page"/>

<c:set var="strtSi" value="7" scope="page"/>
<c:set var="endSi" value="23" scope="page"/>
<c:choose>
	<c:when test="${listPage eq 'listRezvStat' && param.listType eq 'month' }">
		<!-- 월간 -->
		<div class="listarea" id="molyList">
			<div class="cardarea" id="bgcolArea" style="padding:4px 0 0 3px; margin:0 0 2px -3px;${empty param.bgcolYn || param.bgcolYn eq 'Y' ? '' : 'display:none;'}">
				<dl style="padding-top:1px;">
					<dd class="bgcoltxt" style="background:#ccffcc;"><div class="ellipsis" style="float:right;width:67px;"><u:msg titleId="wr.jsp.discStatCdR.title" alt="진행중"/></div></dd>
					<c:forEach var="list" items="${wrRescKndBVoList }" varStatus="status">
						<dd class="bgcoltxt" style="background:${list.bgcolCd};"><div class="ellipsis" style="float:right;width:67px;" title="${list.kndNm }">${list.kndNm }</div></dd>
					</c:forEach>
				</dl>
			</div>
			<u:blank />
			<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
				<tr>
					<td width="15%" class="scdhead"><strong><u:msg titleId="wc.cols.sun" alt="일" /></strong></td>
					<td width="14%" class="head_ct"><strong><u:msg titleId="wc.cols.mon" alt="월" /></strong></td>
					<td width="14%" class="head_ct"><strong><u:msg titleId="wc.cols.tue" alt="화" /></strong></td>
					<td width="14%" class="head_ct"><strong><u:msg titleId="wc.cols.wed" alt="수" /></strong></td>
					<td width="14%" class="head_ct"><strong><u:msg titleId="wc.cols.thu" alt="목" /></strong></td>
					<td width="14%" class="head_ct"><strong><u:msg titleId="wc.cols.fri" alt="금" /></strong></td>
					<td width="15%" class="head_ct"><strong><u:msg titleId="wc.cols.sat" alt="토" /></strong></td>
				</tr>
				<c:forEach var="list" items="${wrMonthVo.wrWeekVo}" varStatus="status">
				<tr>
					<c:forEach var="subList" items="${list.wrDayVo}" varStatus="subStatus">
						<td class="${subList.todayYn eq 'Y' ? 'scd_today' : 'scdtd'}" >
						<div style="float:left;width:20px;" class="${subList.isHoliDay}">${subList.day}</div>
						<div style="float:right;overflow:hidden;width:100px;white-Space:nowrap;text-align:right;" class="${subList.isHoliDay }">
						<c:set var="spclNms" value=""/>
						<c:forEach var="spclDtList" items="${subList.spclDtList }" varStatus="spclStatus">
							<c:set var="spclNms" value="${spclNms}${spclStatus.index > 0 ? ',' : ''}${spclDtList[1] }" />
						</c:forEach>
						<u:out value="${spclNms }" />
						</div>
						<c:set var="maxCnt" value="4"/>
						<c:set var="totCnt" value="0"/>
						<c:forEach var="rowCnt" begin="1"  end="${maxCnt }" varStatus="rowStatus">
							<c:set var="empCnt" value="0"/>
							<c:forEach var="list" items="${wrRezvBVoList }" varStatus="listStatus">
								<c:set var="rezvStrtYmd" value="${fn:replace(list.rezvStrtYmd,'-','') }"/>
								<c:set var="rezvEndYmd" value="${fn:replace(list.rezvEndYmd,'-','') }"/>
								<c:if test="${(rezvStrtYmd == rezvEndYmd && subList.days >= rezvStrtYmd && subList.days <= rezvEndYmd) || (rezvStrtYmd < rezvEndYmd && ( subList.days == rezvStrtYmd || subList.days  == rezvEndYmd )) || (rezvStrtYmd < rezvEndYmd && subList.days > rezvStrtYmd && subList.days  < rezvEndYmd ) }">
									<c:choose>
										<c:when test="${list.rowIndex == rowCnt }">
											<c:set var="empCnt" value="${list.rowIndex }"/>
											<c:set var="rezvNms" value="" />
											<c:set var="totCnt" value="${totCnt+1 }"/>
											<c:set var="maxCnt" value="${maxCnt-1 }"/>
											<u:set var="rezvBgCss" test="${list.discStatCd ne 'R' && !empty list.bgcolCd}" value="background:${list.bgcolCd};" elseValue=""/>
											<u:set var="rezvDisplay" test="${totCnt > 3}" value="display:none;" elseValue=""/>
											
											<u:set var="rezvBgCss" test="${list.discStatCd ne 'R' && !empty list.bgcolCd}" value="width:100%; font-size:11px; color:#454545; background:${list.bgcolCd }; border-top:1px solid ${list.bgcolCd }; border-bottom:1px solid ${list.bgcolCd };border-right:1px solid ${list.bgcolCd };line-height:16px; padding:2px 3px 0 0px;margin-top:1px;" elseValue="padding: 2px 3px 0px 0px;height:18px;margin-top:1px;"/>
											
											<div class="timeArea ${list.rezvId } ${list.discStatCd eq 'R' ? 'bodytime_green' : '' }" style="${rezvDisplay }clear:both;cursor:pointer;${rezvBgCss}" onclick="viewRezvPop('${list.rezvId }',event);" onmouseover="fnRezvMouseOver('${list.rezvId}','trover',this);" onmouseout="fnRezvMouseOver('${list.rezvId}','trout',this);">
											<c:choose>
												<c:when test="${rezvStrtYmd == rezvEndYmd && subList.days >= rezvStrtYmd && subList.days <= rezvEndYmd }"><c:set var="rezvNms" value="${list.rezvStrtTime}~${list.rezvEndTime}" /></c:when>
												<c:when test="${rezvStrtYmd < rezvEndYmd && ( subList.days == rezvStrtYmd || subList.days  == rezvEndYmd )  }">
													<c:if test="${subList.days == rezvStrtYmd && list.prevRezvYn eq 'N'}"><c:set var="rezvNms" value="${list.rezvStrtTime}~${endTime }" /></c:if>													
													<c:if test="${subList.days == rezvEndYmd && list.nextRezvYn eq 'N'}"><c:set var="rezvNms" value="${strtTime}~${list.rezvEndTime }" /></c:if>
													<c:if test="${(subList.days == rezvStrtYmd && list.prevRezvYn eq 'Y') || (subList.days == rezvEndYmd && list.nextRezvYn eq 'Y')}"><c:set var="rezvNms" value="${strtTime}~${endTime }" /></c:if>
												</c:when>
												<c:when test="${rezvStrtYmd < rezvEndYmd && subList.days > rezvStrtYmd && subList.days  < rezvEndYmd }">
													<c:set var="rezvNms" value="${strtTime}~${endTime}" />
												</c:when>
												<c:otherwise></c:otherwise>
											</c:choose>
											<div class="ellipsis scd_promise" title="${rezvNms } ${list.subj }">${rezvNms } ${list.subj }</div></div>
										</c:when>
										<c:otherwise>
										</c:otherwise>
									</c:choose>
								</c:if>
							</c:forEach>
							<c:if test="${empCnt == 0 || empCnt != rowCnt}">
								<div style="clear:both;width:100%;padding: 2px 3px 0px 0px;height:18px;margin-top:1px;" onclick="setRezvPop(null , this);" data-day="${fn:substring(subList.days,0,4) }-${fn:substring(subList.days,4,6) }-${fn:substring(subList.days,6,8) }" data-time="09:00" onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
									<span class="scd_promise" >&nbsp;</span>
								</div>
							</c:if>
						</c:forEach>
						<c:if test="${totCnt > 3 }">
							<div class="bodytime" style="float:right;"><u:buttonS href="javascript:;" onclick="listRezvMorePop('${subList.days }');" titleId="cm.btn.more" alt="더보기" /></div>
						</c:if>
						<%-- <div class="bodytime" style="float:right;margin:1px 1px 1px 1px;width:99%;"><c:if test="${totCnt > 3 }"><a href="javascript:;">[더보기]</a></c:if>&nbsp;</div> --%>
						</td>
					</c:forEach>
				</tr>
				</c:forEach>	
			</table>
		</div>
	</c:when>
	<c:when test="${listPage eq 'listRezvStat' && ( empty param.listType || param.listType eq 'week' )}">
		<c:forEach var="week" items="${wrWeekVo.wrDayVo }" varStatus="status">
			<c:set var="weekYmd" value="${fn:replace(week.days,'-','') }"/>
			<% // 목록 %>
			<u:msg var="dayOfWeek" titleId="wr.jsp.dayOfWeek${status.index }.title" alt="요일"/>
			<c:set var="weekDayTitle" value="${week.days }&nbsp;${dayOfWeek }"/>
			<u:title title="${weekDayTitle }" alt="요일" type="small" />
			<c:set var="strtHH" value="7"/>
			<c:set var="endHH" value="23"/>
			<u:listArea id="listArea">
			<tr>
				<c:forEach var="si" begin="${strtHH }" end="${endHH }" varStatus="siStatus">
					<u:set var="siValue" test="${si < 10}" value="0${si }" elseValue="${si }"/>
					<td colspan="2" width="5.88%" class="head_ct">${siValue}:00</td>
				</c:forEach>
			</tr>
			<tr>
				<c:forEach var="si" begin="${strtHH }" end="${endHH }" varStatus="siStatus">
					<u:set var="siValue" test="${si < 10}" value="0${si }" elseValue="${si }"/>
					<c:set var="fixedValue" value="${siValue }00"/>
					<c:set var="halfValue" value="${siValue }30"/>
					<c:set var="isHour" value="N" />
					<c:set var="isHalfHour" value="N" />
					<c:set var="actionHourRezv" value="setRezvPop(null,this);" />
					<c:set var="actionHalfRezv" value="setRezvPop(null,this);" />
					<c:set var="areaHourRezvId" value="" />
					<c:set var="areaHalfRezvId" value="" />
					
					<c:set var="areaHourRezvCss" value="" />
					<c:set var="areaHalfRezvCss" value="" />
					
					<c:set var="areaHourRezvTitle" value="" />
					<c:set var="areaHalfRezvTitle" value="" />
					
					<c:forEach var="list" items="${wrRezvBVoList }" varStatus="listStatus">
						<c:set var="rezvStrtYmd" value="${fn:replace(list.rezvStrtYmd,'-','') }"/>
						<c:set var="rezvEndYmd" value="${fn:replace(list.rezvEndYmd,'-','') }"/>
						<c:set var="rezvStrtTime" value="${fn:replace(list.rezvStrtTime,':','') }"/>
						<c:set var="rezvEndTime" value="${fn:replace(list.rezvEndTime,':','') }"/>
						<c:choose>
							<c:when test="${rezvStrtYmd == rezvEndYmd && weekYmd >= rezvStrtYmd && weekYmd  <= rezvEndYmd }">
								<c:if test="${fixedValue >= rezvStrtTime && fixedValue < rezvEndTime}"><c:set var="isHour" value="Y" /><c:set var="areaHourRezvId" value="${list.rezvId }" /><c:set var="areaHourRezvTitle" value="${list.subj }" /><c:set var="areaHourRezvCss" value="${list.discStatCd eq 'R' ? '_green' : '_red' }" /></c:if>
								<c:if test="${halfValue >= rezvStrtTime && halfValue < rezvEndTime}"><c:set var="isHalfHour" value="Y" /><c:set var="areaHalfRezvId" value="${list.rezvId }" /><c:set var="areaHalfRezvTitle" value="${list.subj }" /><c:set var="areaHalfRezvCss" value="${list.discStatCd eq 'R' ? '_green' : '_red' }" /></c:if>
							</c:when>
							<c:when test="${rezvStrtYmd < rezvEndYmd && ( weekYmd == rezvStrtYmd || weekYmd  == rezvEndYmd )  }">
								<c:if test="${(weekYmd == rezvStrtYmd && fixedValue >= rezvStrtTime ) || ( weekYmd == rezvEndYmd && fixedValue < rezvEndTime ) }"><c:set var="isHour" value="Y" /><c:set var="areaHourRezvId" value="${list.rezvId }" /><c:set var="areaHourRezvTitle" value="${list.subj }" /><c:set var="areaHourRezvCss" value="${list.discStatCd eq 'R' ? '_green' : '_red' }" /></c:if>
								<c:if test="${(weekYmd == rezvStrtYmd && halfValue >= rezvStrtTime ) || ( weekYmd == rezvEndYmd && halfValue < rezvEndTime ) }"><c:set var="isHalfHour" value="Y" /><c:set var="areaHalfRezvId" value="${list.rezvId }" /><c:set var="areaHalfRezvTitle" value="${list.subj }" /><c:set var="areaHalfRezvCss" value="${list.discStatCd eq 'R' ? '_green' : '_red' }" /></c:if>
							</c:when>
							<c:when test="${rezvStrtYmd < rezvEndYmd && weekYmd > rezvStrtYmd && weekYmd  < rezvEndYmd }">
								<c:set var="isHour" value="Y" />
								<c:set var="isHalfHour" value="Y" />
								<c:set var="areaHourRezvId" value="${list.rezvId }" />
								<c:set var="areaHalfRezvId" value="${list.rezvId }" />
								<c:set var="areaHourRezvCss" value="${list.discStatCd eq 'R' ? '_green' : '_red' }" />
								<c:set var="areaHalfRezvCss" value="${list.discStatCd eq 'R' ? '_green' : '_red' }" />
								<c:set var="areaHourRezvTitle" value="${list.subj }" />
								<c:set var="areaHalfRezvTitle" value="${list.subj }" />
							</c:when>
							<c:otherwise>
							</c:otherwise>
						</c:choose>
					</c:forEach>
					<c:if test="${isHour eq 'Y' }"><c:set var="actionHourRezv" value="viewRezvPop('${areaHourRezvId }');"/></c:if>
					<c:if test="${isHalfHour eq 'Y' }"><c:set var="actionHalfRezv" value="viewRezvPop('${areaHalfRezvId }');"/></c:if>
					<td width="2.94%" class="timeArea ${areaHourRezvId} bodytime${areaHourRezvCss}" style="cursor:pointer;" onclick="${actionHourRezv}" data-day="${week.days }" data-time="${siValue }:00" onmouseover="fnRezvMouseOver('${areaHourRezvId}','trover',this);" onmouseout="fnRezvMouseOver('${areaHourRezvId}','trout',this);" title="${areaHourRezvTitle }">&nbsp;</td>
					<td width="2.94%" class="timeArea ${areaHalfRezvId} bodytime${areaHalfRezvCss}" style="cursor:pointer;" onclick="${actionHalfRezv}" data-day="${week.days }" data-time="${siValue }:30" onmouseover="fnRezvMouseOver('${areaHalfRezvId}','trover',this);" onmouseout="fnRezvMouseOver('${areaHalfRezvId}','trout',this);" title="${areaHalfRezvTitle }">&nbsp;</td>
				</c:forEach>
			</tr>
			</u:listArea>
		</c:forEach>
	</c:when>
	<c:when test="${listPage eq 'listRezvStat' && param.listType eq 'weeks' }">
		<!-- 주간 -->
		<div class="listarea" id="weeksList">
			<div class="cardarea" id="bgcolArea" style="padding:4px 0 0 3px; margin:0 0 2px -3px;${empty param.bgcolYn || param.bgcolYn eq 'Y' ? '' : 'display:none;'}">
				<dl style="padding-top:1px;">
					<dd class="bgcoltxt" style="background:#ccffcc;"><div class="ellipsis" style="float:right;width:67px;"><u:msg titleId="wr.jsp.discStatCdR.title" alt="진행중"/></div></dd>
					<c:forEach var="list" items="${wrRescKndBVoList }" varStatus="status">
						<dd class="bgcoltxt" style="background:${list.bgcolCd};"><div class="ellipsis" style="float:right;width:67px;" title="${list.kndNm }">${list.kndNm }</div></dd>
					</c:forEach>
				</dl>
			</div>
			<u:blank />
			<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
				<tr>
					<c:forEach var="subList" items="${wrWeekVo.wrDayVo}" varStatus="subStatus">
						<td width="${subStatus.index == 0 ? '15' : '14'}%" class="${subStatus.index == 0 ? 'scdhead' : 'head_lt'}" style="text-align:left;">
							<strong class="${subList.isHoliDay }">${subList.day }
							<c:forTokens var="weekOfDay" items="sun,mon,tue,wed,thu,fri,sat" delims="," varStatus="weekOfDayStatus">
								<c:if test="${subStatus.index == weekOfDayStatus.index }"><u:msg titleId="wc.cols.${weekOfDay }" alt="일" /></c:if>
							</c:forTokens>
							</strong>
							<div style="float:right;" class="${subList.isHoliDay }">
							<c:set var="spclNms" value=""/>
							<c:forEach var="spclDtList" items="${subList.spclDtList }" varStatus="spclStatus">
								<c:set var="spclNms" value="${spclNms}${spclStatus.index > 0 ? ',' : ''}${spclDtList[1] }" />
							</c:forEach>
							<u:out value="${spclNms }" maxLength="10"/>
							</div>
						</td>
					</c:forEach>
				</tr>
				<tr>
					<c:forEach var="subList" items="${wrWeekVo.wrDayVo}" varStatus="subStatus">
						<td class="${subList.todayYn eq 'Y' ? 'scd_today' : 'scdtd'}" style="height:450px;">
						<c:set var="totCnt" value="0"/>
						<c:set var="maxCnt" value="20"/>
						<c:forEach var="rowCnt" begin="1"  end="${maxCnt }" varStatus="rowStatus">
							<c:set var="empCnt" value="0"/>
							<c:forEach var="list" items="${wrRezvBVoList }" varStatus="listStatus">
								<c:set var="rezvStrtYmd" value="${fn:replace(list.rezvStrtYmd,'-','') }"/>
								<c:set var="rezvEndYmd" value="${fn:replace(list.rezvEndYmd,'-','') }"/>
								<c:set var="weekDays" value="${fn:replace(subList.days,'-','') }"/>
								<c:if test="${(rezvStrtYmd == rezvEndYmd && weekDays >= rezvStrtYmd && weekDays <= rezvEndYmd) || (rezvStrtYmd < rezvEndYmd && ( weekDays == rezvStrtYmd || weekDays  == rezvEndYmd )) || (rezvStrtYmd < rezvEndYmd && weekDays > rezvStrtYmd && weekDays  < rezvEndYmd ) }">
									<c:choose>
										<c:when test="${list.rowIndex == rowCnt }">
											<c:set var="empCnt" value="${list.rowIndex }"/>
											<c:set var="rezvNms" value="" />
											<c:set var="totCnt" value="${totCnt+1 }"/>
											<c:set var="maxCnt" value="${maxCnt-1 }"/>
											<u:set var="rezvBgCss" test="${list.discStatCd ne 'R' && !empty list.bgcolCd}" value="background:${list.bgcolCd};" elseValue=""/>
											<u:set var="rezvDisplay" test="${list.rowIndex > rowCnt}" value="display:none;" elseValue=""/>
											
											<u:set var="rezvBgCss" test="${list.discStatCd ne 'R' && !empty list.bgcolCd}" value="width:100%; font-size:11px; color:#454545; background:${list.bgcolCd }; border-top:1px solid ${list.bgcolCd }; border-bottom:1px solid ${list.bgcolCd };border-right:1px solid ${list.bgcolCd };line-height:16px; padding:2px 3px 0 0px;margin-top:1px;" elseValue="padding: 2px 3px 0px 0px;height:18px;margin-top:1px;"/>
											
											<div class="timeArea ${list.rezvId } ${list.discStatCd eq 'R' ? 'bodytime_green' : '' }" style="${rezvDisplay }clear:both;cursor:pointer;${rezvBgCss}" onclick="viewRezvPop('${list.rezvId }',event);" onmouseover="fnRezvMouseOver('${list.rezvId}','trover',this);" onmouseout="fnRezvMouseOver('${list.rezvId}','trout',this);">
											<c:choose>
												<c:when test="${rezvStrtYmd == rezvEndYmd && weekDays >= rezvStrtYmd && weekDays <= rezvEndYmd }"><c:set var="rezvNms" value="${list.rezvStrtTime}~${list.rezvEndTime}" /></c:when>
												<c:when test="${rezvStrtYmd < rezvEndYmd && ( weekDays == rezvStrtYmd || weekDays  == rezvEndYmd )  }">
													<c:if test="${weekDays == rezvStrtYmd && list.prevRezvYn eq 'N'}"><c:set var="rezvNms" value="${list.rezvStrtTime}~${endTime }" /></c:if>
													<c:if test="${weekDays == rezvEndYmd && list.nextRezvYn eq 'N'}"><c:set var="rezvNms" value="${strtTime}~${list.rezvEndTime }" /></c:if>
													<c:if test="${(weekDays == rezvStrtYmd && list.prevRezvYn eq 'Y') || (weekDays == rezvEndYmd && list.nextRezvYn eq 'Y')}"><c:set var="rezvNms" value="${strtTime}~${endTime }" /></c:if>
												</c:when>
												<c:when test="${rezvStrtYmd < rezvEndYmd && weekDays > rezvStrtYmd && weekDays  < rezvEndYmd }">
													<c:set var="rezvNms" value="${strtTime}~${endTime}" />
												</c:when>
												<c:otherwise></c:otherwise>
											</c:choose>
											<div class="ellipsis scd_promise" title="${rezvNms } ${list.subj }">${rezvNms } ${list.subj }</div></div>
										</c:when>
										<c:otherwise>
										</c:otherwise>
									</c:choose>
								</c:if>
							</c:forEach>
							<c:if test="${empCnt == 0 || empCnt != rowCnt}">
								<div style="clear:both;width:100%;padding: 2px 3px 0px 0px;height:18px;margin-top:1px;" onclick="setRezvPop(null , this);"data-day="${fn:substring(subList.days,0,4) }-${fn:substring(subList.days,4,6) }-${fn:substring(subList.days,6,8) }" data-time="09:00" onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
									<span class="scd_promise" >&nbsp;</span>
								</div>
							</c:if>
						</c:forEach>
						<c:if test="${totCnt > (maxCnt-1) }">
							<div class="bodytime" style="float:right;"><u:buttonS href="javascript:;" onclick="listRezvMorePop('${subList.days }');" titleId="cm.btn.more" alt="더보기" /></div>
						</c:if>
						<%-- <div class="bodytime" style="float:right;margin:1px 1px 1px 1px;width:99%;"><c:if test="${totCnt > 3 }"><a href="javascript:;">[더보기]</a></c:if>&nbsp;</div> --%>
						</td>
					</c:forEach>
				</tr>
			</table>
		</div>
	</c:when>
	<c:when test="${listPage eq 'listRezvStat' && param.listType eq 'day' }">
		<!-- 일간 -->
		<div class="listarea" id="dayList">
			<div class="cardarea" id="bgcolArea" style="padding:4px 0 0 3px; margin:0 0 2px -3px;${empty param.bgcolYn || param.bgcolYn eq 'Y' ? '' : 'display:none;'}">
				<dl style="padding-top:1px;">
					<dd class="bgcoltxt" style="background:#ccffcc;"><div class="ellipsis" style="float:right;width:67px;"><u:msg titleId="wr.jsp.discStatCdR.title" alt="진행중"/></div></dd>
					<c:forEach var="list" items="${wrRescKndBVoList }" varStatus="status">
						<dd class="bgcoltxt" style="background:${list.bgcolCd};"><div class="ellipsis" style="float:right;width:67px;" title="${list.kndNm }">${list.kndNm }</div></dd>
					</c:forEach>
				</dl>
			</div>
			<u:blank />
			<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
				<tr>
					<td width="5%" class="head_lt" style="text-align:left;">&nbsp;</td>
					<td width="95%" class="${wrDayVo.dayOfWeek == 1 ? 'scdhead' : 'head_lt'}" style="text-align:left;">
						<strong class="${wrDayVo.isHoliDay }">${wrDayVo.day }
						<c:forTokens var="weekOfDay" items="sun,mon,tue,wed,thu,fri,sat" delims="," varStatus="weekOfDayStatus">
							<c:if test="${wrDayVo.dayOfWeek == weekOfDayStatus.count }"><u:msg titleId="wc.cols.${weekOfDay }" alt="일" /></c:if>
						</c:forTokens>
						</strong>
						<div style="float:right;" class="${wrDayVo.isHoliDay }">
							<c:set var="spclNms" value=""/>
							<c:forEach var="spclDtList" items="${wrDayVo.spclDtList }" varStatus="spclStatus">
								<c:set var="spclNms" value="${spclNms}${spclStatus.index > 0 ? ',' : ''}${spclDtList[1] }" />
							</c:forEach>
							<u:out value="${spclNms }" maxLength="10"/>
						</div>
					</td>
				</tr>
				<c:set var="maxViewCnt" value="10"/>
				<c:forEach var="hour" begin="${strtSi }" end="${endSi }" step="1" >
					<c:forEach var="minute" begin="1" end="2" step="1">
						<u:set var="hours" test="${hour < 10 }" value="0${hour }" elseValue="${hour }"/>
						<c:set var="weekDays" value="${fn:replace(wrDayVo.days,'-','') }"/>
						<c:set var="times" value="${hours }:${minute == 1 ? '00' : '30' }"/>
						<c:set var="fullTimes" value="${weekDays }${hours }${minute == 1 ? '00' : '30' }"/>
						<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
							<c:if test="${minute == 1 }">
								<td class="scdtd_day" rowspan="2">
									<c:if test="${hour == strtSi || hour == 12 }"><span><u:msg titleId="wr.jsp.hour.${hour == strtSi ? 'am' : 'pm'}.title" alt="오전" /></span></c:if>
									<span class="scd_gray">${hour}<u:msg titleId="wr.jsp.hour.title" alt="시" /></span>
								</td>
							</c:if>
							<td class="scdtd_day" data-day="${fn:substring(weekDays,0,4) }-${fn:substring(weekDays,4,6) }-${fn:substring(weekDays,6,8) }" data-time="${hours }:${minute == 1 ? '00' : '30' }" >
								<c:forEach var="list" items="${wrRezvBVoList }" varStatus="listStatus">
									<c:if test="${list.rowIndex <= maxViewCnt  }">
										<c:set var="rezvStrtYmd" value="${fn:replace(list.rezvStrtYmd,'-','') }"/>
										<c:set var="rezvEndYmd" value="${fn:replace(list.rezvEndYmd,'-','') }"/>
										<c:set var="rezvFullStrtTime" value="${fn:replace(list.rezvStrtYmd,'-','') }${fn:replace(list.rezvStrtTime,':','') }"/>
										<c:set var="rezvFullEndTime" value="${fn:replace(list.rezvEndYmd,'-','') }${fn:replace(list.rezvEndTime,':','') }"/>
										<c:if test="${(rezvStrtYmd == rezvEndYmd && fullTimes == rezvFullStrtTime) || (rezvStrtYmd < rezvEndYmd && ( weekDays == rezvStrtYmd || weekDays  == rezvEndYmd )) || (rezvStrtYmd < rezvEndYmd && weekDays > rezvStrtYmd && weekDays  < rezvEndYmd ) }">
											<c:set var="rezvTime" value=""/>
											<c:choose>
												<c:when test="${rezvStrtYmd == rezvEndYmd && fullTimes == rezvFullStrtTime }">
													<c:set var="rezvNms" value="${fn:substring(list.rezvStrtTime,0,2)}:${fn:substring(list.rezvStrtTime,2,4)}~${fn:substring(list.rezvEndTime,0,2)}:${fn:substring(list.rezvEndTime,2,4)}" />
													<c:set var="rezvTime" value="${times }"/>
												</c:when>
												<c:when test="${rezvStrtYmd < rezvEndYmd && ( weekDays == rezvStrtYmd || weekDays  == rezvEndYmd )  }">
													<c:if test="${weekDays == rezvStrtYmd }"><c:set var="rezvNms" value="${list.rezvStrtTime}~${endTime }" /><c:set var="rezvTime" value="${list.rezvStrtTime }"/></c:if>
													<c:if test="${weekDays == rezvEndYmd }"><c:set var="rezvNms" value="${strtTime}~${list.rezvEndTime }" /><c:set var="rezvTime" value="${strtTime }"/></c:if>
												</c:when>
												<c:when test="${rezvStrtYmd < rezvEndYmd && weekDays > rezvStrtYmd && weekDays  < rezvEndYmd }">
													<c:set var="rezvNms" value="${strtTime}~${endTime}" />
													<c:set var="rezvTime" value="${strtTime }"/>
												</c:when>
												<c:otherwise></c:otherwise>
											</c:choose>
											<c:if test="${!empty rezvTime && rezvTime == times}">
												<u:set var="rezvBgCss" test="${list.discStatCd ne 'R' && !empty list.bgcolCd}" value="background:${list.bgcolCd }; border-top:1px solid ${list.bgcolCd }; border-right:1px solid ${list.bgcolCd };border-bottom:1px solid ${list.bgcolCd };line-height:18px;" elseValue=""/>
												<div class="${list.discStatCd eq 'R' ? 'bodytime_green' : '' }" style="position:absolute;height:${(list.timeCnt *18)+(list.timeCnt-2) }px;margin-left:${ ((92/maxViewCnt)+1) * (list.rowIndex-1)}%;width:${92/maxViewCnt}%; font-size:12px;cursor:pointer;"  onclick="viewRezvPop('${list.rezvId }',event);">
													<div class="ellipsis" title="${rezvNms } ${list.subj }" style="${rezvBgCss}height:19px;"><strong class="scd_promise">${rezvNms }</strong><c:if test="${list.timeCnt == 1 }"><span class="scd_promise">${list.subj }</span></c:if></div>
													<c:if test="${list.timeCnt > 1 }"><div class="ellipsis" style="${rezvBgCss}height:${((list.timeCnt-1) *18)+(list.timeCnt-8) }px;${list.discStatCd eq 'R' ? '' : 'color:#ffffff;' }opacity:0.95;filter:alpha(opacity=95);">${list.subj }</div></c:if>
												</div>
											</c:if>	
										</c:if>
									</c:if>
								</c:forEach>
							</td>
						</tr>
					</c:forEach>
				</c:forEach>
			</table>
		</div>
	</c:when>
	<c:otherwise>
		<% // 목록 %>
		<div id="listArea" class="listarea">
			<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
			<colgroup><c:if test="${admAuth eq 'Y' }" ><col width="25px"/></c:if><col width="10%"/><col width="15%"/><col width="24%"/><col width="*"/><col width="10%"/><col width="10%"/></colgroup>
			<tr>
				<c:if test="${admAuth eq 'Y' }" ><td class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td></c:if>
				<td class="head_ct"><u:msg titleId="cols.rescKnd" alt="자원종류" /></td>
				<td class="head_ct"><u:msg titleId="cols.rescNm" alt="자원명" /></td>
				<td class="head_ct"><u:msg titleId="cols.rezvPrd" alt="예약기간" /></td>
				<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
				<td class="head_ct"><u:msg titleId="wr.cols.rezvNm" alt="예약자" /></td>
				<td class="head_ct"><u:msg titleId="wr.cols.rezvStat" alt="예약상태" /></td>
			</tr>
			<c:choose>
				<c:when test="${!empty wrRezvBVoList}">
					<c:forEach var="list" items="${wrRezvBVoList }" varStatus="status">
						<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
							<c:if test="${admAuth eq 'Y' }" ><td class="body_ct"><c:if test="${list.discStatCd ne 'R'}"><u:checkbox name="idCheck" value="${list.rezvId }" checked="false" /></c:if></td></c:if>
							<td class="body_ct">${list.kndNm }</td>
							<td class="body_ct"><a href="javascript:viewRescPop('${list.rescMngId }');">${list.rescNm }</a></td>
							<td class="body_ct"><u:out value="${list.rezvStrtDt }" type="longdate" /> ~ <u:out value="${list.rezvEndDt }" type="longdate" /></td>
							<td class="body_lt"><div class="ellipsis" style="width:98%;" title="${list.subj }"><a href="javascript:;" onclick="viewRezvPop('${list.rezvId}');">${list.subj }</a></div></td>
							<td class="body_ct"><a href="javascript:viewUserPop('${list.regrUid }');">${list.regrNm }</a></td>
							<td class="body_ct">
								<u:msg titleId="wr.jsp.discStatCd${list.discStatCd }.title" alt=""/>
							</td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr>
						<td class="nodata" colspan="${admAuth eq 'Y' ? '7' : '6'}"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
					</tr>
				</c:otherwise>
			</c:choose>
			</table>
		</div>
		<u:blank />
		<u:pagination />
	</c:otherwise>
</c:choose>
<u:blank />
<% // 하단 버튼 %>
<u:buttonArea>
	<c:if test="${listPage ne 'listRezvStat' }">
		<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" auth="R" />
		<u:button titleId="cm.btn.excelDown" alt="엑셀다운" onclick="excelDownFile();" auth="R" />
	</c:if>	
	<c:if test="${admAuth eq 'Y' }" >
		<u:button titleId="cm.btn.del" alt="삭제" href="javascript:fnDeleteList();" auth="W" />
	</c:if>
	<c:if test="${listPage ne 'listRezvDisc' && admAuth eq 'N'}">
		<u:button titleId="wr.btn.rescRezv" alt="자원예약" href="javascript:;" onclick="setRezvPop();" auth="W" />
	</c:if>
</u:buttonArea>
<form id="deleteListForm" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="listPage" value="./${listPage }.do?menuId=${menuId }"/>
	<u:input type="hidden" name="rezvId"  id="delList"/>
</form>
<form id="excelForm">
	<c:forEach items="${paramEntryList}" var="entry" varStatus="status">
		<u:input type="hidden" id="${entry.key}" value="${entry.value}" />
	</c:forEach>
	<u:input type="hidden" id="listPage" value="${listPage}" />
</form>
