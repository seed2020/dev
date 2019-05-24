<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<u:set test="${!empty param.fncCal}" var="fncCal" value="${param.fncCal}" elseValue="my" />

<script type="text/javascript">
<!--
//viewSchdlPop
function promScdDel(flag,schdlId){

	if(confirmMsg("cm.cfrm.del")) {
		<% // cm.cfrm.del=삭제하시겠습니까? %>
		if(flag == 'del'){
			callAjax('./transSetPromModPopDel.do?menuId=${menuId}', {schdlId:schdlId}, function(data) {
			if (data.message != null) {
			alert(data.message);
			}
			if (data.result == 'ok') {
				if('${listPage}' == 'listNewSchdl') reloadCalendar(); // 리로드
				else location.href = './${listPage}.do?${paramsForList}';
			}
			});
		}
		else if(flag == 'allDel'){
			callAjax('./transSetAllModPopAllDel.do?menuId=${menuId}', {schdlId:schdlId}, function(data) {
			if (data.message != null) {
			alert(data.message);
			}
			if (data.result == 'ok') {
				if('${listPage}' == 'listNewSchdl') reloadCalendar(); // 리로드
				else location.href = './${listPage}.do?${paramsForList}';
			}
			});
		}
	}
}
//-->

</script>
<div style="width:700px">
	<form id="viewSchdl">
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<u:input type="hidden" id="schdl_responseDptNm" name="schdl_responseDptNm" />
	<u:input type="hidden" id="schdl_responseNm" name="schdl_responseNm" />

	<% // 폼 필드 %>
	<u:listArea noBottomBlank="true" colgroup="18%,32%,18%,32%">
	<tr>
		<td class="head_lt"><u:msg titleId="cols.schdlKnd" alt="일정종류"/></td>
		<td class="body_lt">${CtSchdlBVo.schdlTypNm }</td>
		<td class="head_lt"><u:msg titleId="cols.prio" alt="우선순위" /></td>
		<td class="body_lt" colspan="3">${CtSchdlBVo.workPrioOrdr }</td>
	</tr>
	
	</u:listArea>
	<u:blank />
	
	<% // 반복정보 %>
	<c:if test="${CtSchdlBVo.repetYn eq 'Y' && !empty wcRepetSetupDVo  }">
		<u:listArea noBottomBlank="true" colgroup="18%,*">
		<tr>
			<td class="head_lt"><u:msg titleId="cols.repetKnd" alt="반복종류" /></td>
			<td class="body_lt">
				<c:choose>
					<c:when test="${wcRepetSetupDVo.repetPerdCd eq 'EV_DY'}">
						<u:msg titleId="wc.option.daly" alt="일일" />
						<u:msg titleId="wc.jsp.setRepetPop.tx02" alt="매" />${wcRepetSetupDVo.repetDd }<u:msg titleId="wc.jsp.setRepetPop.tx03" alt="일마다" />
					</c:when>
					<c:when test="${wcRepetSetupDVo.repetPerdCd eq 'EV_WK'}">
						<u:msg titleId="wc.option.wely" alt="주간" />
						<u:msg titleId="wc.jsp.setRepetPop.tx02" alt="매" />${wcRepetSetupDVo.repetWk }<u:msg titleId="wc.jsp.setRepetPop.tx04" alt="주마다" />
						<c:if test="${!empty wcRepetSetupDVo.apntDy }">
							<u:checkArea>
								<u:checkbox name="dow" value="SUN" titleId="wc.option.sun" checked="${fn:contains(wcRepetSetupDVo.apntDy,'SUN') }" inputClass="bodybg_lt" disabled="Y"/>
								<u:checkbox name="dow" value="MON" titleId="wc.option.mon" checked="${fn:contains(wcRepetSetupDVo.apntDy,'MON') }" inputClass="bodybg_lt" disabled="Y"/>
								<u:checkbox name="dow" value="TUE" titleId="wc.option.tue" checked="${fn:contains(wcRepetSetupDVo.apntDy,'TUE') }" inputClass="bodybg_lt" disabled="Y"/>
								<u:checkbox name="dow" value="WED" titleId="wc.option.wed" checked="${fn:contains(wcRepetSetupDVo.apntDy,'WEB') }" inputClass="bodybg_lt" disabled="Y"/>
								<u:checkbox name="dow" value="THU" titleId="wc.option.thu" checked="${fn:contains(wcRepetSetupDVo.apntDy,'THU') }" inputClass="bodybg_lt" disabled="Y"/>
								<u:checkbox name="dow" value="FRI" titleId="wc.option.fri" checked="${fn:contains(wcRepetSetupDVo.apntDy,'FRI') }" inputClass="bodybg_lt" disabled="Y"/>
								<u:checkbox name="dow" value="SAT" titleId="wc.option.sat" checked="${fn:contains(wcRepetSetupDVo.apntDy,'SAT') }" inputClass="bodybg_lt" disabled="Y"/>
							</u:checkArea>
						</c:if>
					</c:when>
					<c:when test="${wcRepetSetupDVo.repetPerdCd eq 'EV_DY_MY'}">
						<u:msg titleId="wc.option.moly" alt="월간" />
						<u:msg titleId="wc.jsp.setRepetPop.tx02" alt="매" />${wcRepetSetupDVo.repetMm }<u:msg titleId="wc.jsp.setRepetPop.tx05" alt="개월마다" />
						${wcRepetSetupDVo.apntDd }<u:msg titleId="wc.jsp.setRepetPop.tx07" alt="일" />
					</c:when>
					<c:when test="${wcRepetSetupDVo.repetPerdCd eq 'EV_WK_MT'}">
						<u:msg titleId="wc.option.moly" alt="월간" />
						<u:msg titleId="wc.jsp.setRepetPop.tx02" alt="매" />${wcRepetSetupDVo.repetMm }<u:msg titleId="wc.jsp.setRepetPop.tx05" alt="개월마다" />
						<c:choose>
							<c:when test="${wcRepetSetupDVo.apntWk eq '1'}"><u:msg titleId="wc.cols.firstWeek" alt="첫째주" /></c:when>
							<c:when test="${wcRepetSetupDVo.apntWk eq '2'}"><u:msg titleId="wc.cols.secondWeek" alt="둘째주" /></c:when>
							<c:when test="${wcRepetSetupDVo.apntWk eq '3'}"><u:msg titleId="wc.cols.thirdWeek" alt="셋째주" /></c:when>
							<c:when test="${wcRepetSetupDVo.apntWk eq '4'}"><u:msg titleId="wc.cols.fourWeek" alt="넷째주" /></c:when>
							<c:when test="${wcRepetSetupDVo.apntWk eq '5'}"><u:msg titleId="wc.cols.fiveWeek" alt="다섯째주" /></c:when>
						</c:choose>
						<c:choose>
							<c:when test="${wcRepetSetupDVo.apntDy eq '1'}"><u:msg titleId="wc.cols.sun" alt="일" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
							<c:when test="${wcRepetSetupDVo.apntDy eq '2'}"><u:msg titleId="wc.cols.mon" alt="월" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
							<c:when test="${wcRepetSetupDVo.apntDy eq '3'}"><u:msg titleId="wc.cols.tue" alt="화" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
							<c:when test="${wcRepetSetupDVo.apntDy eq '4'}"><u:msg titleId="wc.cols.wed" alt="수" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
							<c:when test="${wcRepetSetupDVo.apntDy eq '5'}"><u:msg titleId="wc.cols.thu" alt="목" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
							<c:when test="${wcRepetSetupDVo.apntDy eq '6'}"><u:msg titleId="wc.cols.fri" alt="금" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
							<c:when test="${wcRepetSetupDVo.apntDy eq '7'}"><u:msg titleId="wc.cols.sat" alt="토" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
						</c:choose>
					</c:when>
					<c:when test="${wcRepetSetupDVo.repetPerdCd eq 'EV_DY_YR'}">
						<u:msg titleId="wc.option.yely" alt="연간" />
						<u:msg titleId="wc.jsp.setRepetPop.tx08" alt="매년" />${wcRepetSetupDVo.repetMm }<u:msg titleId="wc.jsp.setRepetPop.tx06" alt="월" />
						${wcRepetSetupDVo.apntDd }<u:msg titleId="wc.jsp.setRepetPop.tx07" alt="일" />
					</c:when>
					<c:when test="${wcRepetSetupDVo.repetPerdCd eq 'EV_WK_YR'}">
						<u:msg titleId="wc.option.yely" alt="연간" />
						<u:msg titleId="wc.jsp.setRepetPop.tx08" alt="매년" />${wcRepetSetupDVo.repetMm }<u:msg titleId="wc.jsp.setRepetPop.tx06" alt="월" />
						<c:choose>
							<c:when test="${wcRepetSetupDVo.apntWk eq '1'}"><u:msg titleId="wc.cols.firstWeek" alt="첫째주" /></c:when>
							<c:when test="${wcRepetSetupDVo.apntWk eq '2'}"><u:msg titleId="wc.cols.secondWeek" alt="둘째주" /></c:when>
							<c:when test="${wcRepetSetupDVo.apntWk eq '3'}"><u:msg titleId="wc.cols.thirdWeek" alt="셋째주" /></c:when>
							<c:when test="${wcRepetSetupDVo.apntWk eq '4'}"><u:msg titleId="wc.cols.fourWeek" alt="넷째주" /></c:when>
							<c:when test="${wcRepetSetupDVo.apntWk eq '5'}"><u:msg titleId="wc.cols.fiveWeek" alt="다섯째주" /></c:when>
						</c:choose>
						<c:choose>
							<c:when test="${wcRepetSetupDVo.apntDy eq '1'}"><u:msg titleId="wc.cols.sun" alt="일" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
							<c:when test="${wcRepetSetupDVo.apntDy eq '2'}"><u:msg titleId="wc.cols.mon" alt="월" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
							<c:when test="${wcRepetSetupDVo.apntDy eq '3'}"><u:msg titleId="wc.cols.tue" alt="화" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
							<c:when test="${wcRepetSetupDVo.apntDy eq '4'}"><u:msg titleId="wc.cols.wed" alt="수" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
							<c:when test="${wcRepetSetupDVo.apntDy eq '5'}"><u:msg titleId="wc.cols.thu" alt="목" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
							<c:when test="${wcRepetSetupDVo.apntDy eq '6'}"><u:msg titleId="wc.cols.fri" alt="금" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
							<c:when test="${wcRepetSetupDVo.apntDy eq '7'}"><u:msg titleId="wc.cols.sat" alt="토" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
						</c:choose>
					</c:when>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td class="head_lt"><u:msg titleId="cols.repetPrd" alt="반복기간" /></td>
			<td class="body_lt" ><u:out value="${wcRepetSetupDVo.repetStartDt }" type="date"/>~<u:out value="${wcRepetSetupDVo.repetEndDt }" type="date"/></td>
		</tr>
		</u:listArea>
		<u:blank />
	</c:if>
	
	<div class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
	<colgroup>
		<col width="18%"/>
		<col width="82%"/>
	</colgroup>
	<tr>
		<td class="head_lt"><u:msg titleId="cols.subj" alt="제목"/></td>
		<td class="body_lt">${CtSchdlBVo.subj }</td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="cols.loc" alt="장소" /></td>
		<td class="body_lt">${CtSchdlBVo.locNm }</td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="wc.cols.schdlPriod" alt="기간"/></td>
		<td class="body_lt">
			<table>
				<tr>
					<td>
						<c:choose>
							<c:when test="${CtSchdlBVo.alldayYn eq 'Y' }">
								<u:out value="${CtSchdlBVo.schdlStartDt }" type="date"/>~
								<u:out value="${CtSchdlBVo.schdlEndDt }" type="date"/>
							</c:when>
							<c:otherwise>
								<u:out value="${CtSchdlBVo.schdlStartDt }" type="longdate"/>~
								<u:out value="${CtSchdlBVo.schdlEndDt }" type="longdate"/>
							</c:otherwise>
						</c:choose>
					</td>
					<td>
						<u:checkbox name="" value="Y" titleId="wc.cols.alldayYn" alt="종일여부" checkValue="${CtSchdlBVo.alldayYn}" disabled="Y"/>
					</td>
					<td>
						<u:checkbox name="solaLunaYn" value="N" titleId="wc.option.luna" alt="음력" checkValue="${CtSchdlBVo.solaLunaYn }" disabled="Y"/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="cols.guest" alt="참석자" /></td>
		<td>
			<div style="width:100%;height:160px;overflow-y:auto;">
				<div id="listArea" class="listarea" style="width:95%; padding:5px;">
					<table class="listtable" border="0" cellpadding="0" cellspacing="1" >
						<tr id="headerTr">
							<th width="17%"  class="head_ct"><u:msg titleId="cols.nm" alt="이름" /></th>
							<th width="18%" class="head_ct"><u:msg titleId="wb.cols.emplTyp" alt="임직원구분" /></th>
							<th class="head_ct"><u:msg titleId="cols.comp" alt="회사" />/<u:msg titleId="cols.dept" alt="부서" /></th>
							<th width="17%" class="head_ct"><u:msg titleId="cols.email" alt="이메일" /></th>
						</tr>
						<c:if test="${!empty CtSchdlPromGuestDVoList}">
							<c:forEach var="list" items="${CtSchdlPromGuestDVoList}" varStatus="status">
								<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
									<td class="body_ct" id="emplNm">
										<c:choose>
											<c:when test="${list.guestEmplYn eq 'Y' }"><a href="javascript:viewUserPop('${list.guestUid}');">${list.guestNm}</a></c:when>
											<c:when test="${list.guestEmplYn eq 'N' }"><a href="javascript:viewBc('${list.guestUid }');">${list.guestNm}</a></c:when>
											<c:otherwise>${list.emplNm}</c:otherwise>
										</c:choose>
									</td>
									<td class="body_ct" id="emplTypNm"><u:msg titleId="${list.guestEmplYn eq 'Y' ? 'cm.option.empl' : 'wc.option.frnd'}" alt="지인"/></td>
									<td class="body_ct" id="emplCompNm"><div class="ellipsis" title="${list.guestCompNm }">${list.guestCompNm}</div></td>
									<td class="body_ct" id="emplEmail"><a href="javascript:parent.mailToPop('${list.email }');" title="<u:msg titleId='or.jsp.viewUserPop.mailToPop' />">${list.email}</a></td>
								</tr>
							</c:forEach>
						</c:if>
					</table>
				</div>
			</div>
		</td>
	</tr>

	<tr>
		<td colspan="2" class="body_lt" style="vertical-align:top;">
			<div style="overflow:auto;height:196px;" class="editor">${CtSchdlBVo.cont}</div>
		</td>
	</tr>

	<tr>
		<td class="head_lt"><u:msg titleId="cols.attFile" alt="첨부파일" /></td>
		<td>
			<c:if test="${!empty fileVoList }"><u:files id="${filesId}_view" fileVoList="${fileVoList}" module="ct" mode="view" /></c:if>
		</td>

	</tr>

	</table>
	</div>
<u:blank />
	<u:buttonArea>
	<c:choose>
	<c:when test="${userAuth == 'fail' }">
	<c:set var="promMod"	value= "U" />
	<c:set var="promDel"	value= "A" />
	</c:when>
	<c:when test="${userAuth == 'pass' }">
	<c:set var="promMod"	value= "" />
	<c:set var="promDel"	value= "" />
	</c:when>
	</c:choose>
	
	<c:if test="${plt eq null && !empty authChkD && authChkD == 'D' }">
		<u:button id="promModify" titleId="cm.btn.mod" onclick="setSchdlPop('${scds_schdlId }');" alt="수정" auth="${promMod}" />
		<u:button id="promDelete" titleId="cm.btn.del" href="javascript:promScdDel('del','${scds_schdlId}');" alt="삭제" auth="${promDel}" />
		<c:if test="${scdPidCount > 1 && CtSchdlBVo.repetYn eq 'Y'}">
		<u:button id="promAllDelete" titleId="cm.btn.allDel" href="javascript:promScdDel('allDel','${scds_schdlId}');" alt="전체삭제" auth="${promDel}" />
		</c:if>
	</c:if>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
	</u:buttonArea>

	</form>
</div>
