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
			callAjax('./transSetPromModPopDel.do?menuId=${menuId}&fncCal=${fncCal}', {schdlId:schdlId}, function(data) {
			if (data.message != null) {
			alert(data.message);
			}
			if (data.result == 'ok') {
				if('${listPage}' == 'listNewSchdl') reloadCalendar(); // 리로드
				else location.href = './${listPage}.do?${paramsForList}';
				//reloadCalendar(); // 리로드
				//location.href = './${listPage}.do?${paramsForList}';
			}
			});
		}
		else if(flag == 'allDel'){
			callAjax('./transSetAllModPopAllDel.do?menuId=${menuId}&fncCal=${fncCal}', {schdlId:schdlId}, function(data) {
			if (data.message != null) {
			alert(data.message);
			}
			if (data.result == 'ok') {
				if('${listPage}' == 'listNewSchdl') reloadCalendar(); // 리로드
				else location.href = './${listPage}.do?${paramsForList}';
				//reloadCalendar(); // 리로드
				//location.href = './${listPage}.do?${paramsForList}';
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
		<td class="body_lt">${wcSchdlBVo.schdlTypNm }</td>
		<td class="head_lt"><u:msg titleId="wc.cols.schdlKndCd" alt="일정대상"/></td>
		<td class="body_lt">
			<c:choose>
				<c:when test="${wcSchdlBVo.schdlKndCd eq '1' }"><u:msg titleId="wc.jsp.listPsnSchdl.psn.title" alt="개인일정"/></c:when>
				<c:when test="${wcSchdlBVo.schdlKndCd eq '2' }">${wcSchdlBVo.grpNm }(<u:msg titleId="wc.jsp.listPsnSchdl.grp.title" alt="그룹일정"/>)</c:when>
				<c:when test="${wcSchdlBVo.schdlKndCd eq '3' }"><u:msg titleId="wc.jsp.listPsnSchdl.dept.title" alt="부서일정"/></c:when>
				<c:when test="${wcSchdlBVo.schdlKndCd eq '4' }"><u:msg titleId="wc.jsp.listPsnSchdl.comp.title" alt="회사일정"/></c:when>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="cols.publYn" alt="공개여부" /></td>
		<td class="body_lt">
			<c:choose>
				<c:when test="${wcSchdlBVo.openGradCd eq '2'}"><u:msg titleId="cm.option.apntPubl" alt="지정인공개"/></c:when>
				<c:when test="${wcSchdlBVo.openGradCd eq '3'}"><u:msg titleId="cm.option.priv" alt="비공개"/></c:when>
				<c:when test="${wcSchdlBVo.openGradCd eq '4'}"><u:msg titleId="wc.cols.sel.dept" alt="부서선택"/></c:when>
				<c:otherwise><u:msg titleId="cm.option.publ" alt="공개"/></c:otherwise>
			</c:choose>
		</td>
		<td class="head_lt"><u:msg titleId="cols.prio" alt="우선순위" /></td>
		<td class="body_lt" >${wcSchdlBVo.workPrioOrdr }</td>
	</tr>
	<c:if test="${param.fncCal eq 'dept' && !empty wcSchdlBVo.openGradCd && wcSchdlBVo.openGradCd eq '4'}">
	<tr>
		<td class="head_lt">	<u:msg titleId="wc.cols.sel.dept" alt="부서선택" /></td>
		<td class="body_lt" colspan="3"><div style="min-height:40px;overflow-y:auto;height:40px;"><c:forEach 
		var="wcSchdlDeptRVo" items="${wcSchdlDeptRVoList }" varStatus="status">
		<div class="ubox"><dl><dd 
		class="title">${wcSchdlDeptRVo.orgNm }</dd></dl></div>
		</c:forEach></div></td>
	</tr></c:if>
	</u:listArea>
	<u:blank />
	
	<% // 반복정보 %>
	<c:if test="${wcSchdlBVo.repetYn eq 'Y' && !empty wcRepetSetupDVo  }">
		<u:listArea noBottomBlank="true" colgroup="18%,*">
		<tr>
			<td class="head_lt"><u:msg titleId="cols.repetKnd" alt="반복종류" /></td>
			<td class="body_lt">
				<c:choose>
					<c:when test="${wcRepetSetupDVo.repetPerdCd eq 'EV_DY'}">
						<u:msg titleId="wc.option.daly" alt="일일" /> -
						<u:msg titleId="wc.jsp.setRepetPop.tx02" alt="매" /> ${wcRepetSetupDVo.repetDd} <u:msg titleId="wc.jsp.setRepetPop.tx03" alt="일마다" />
					</c:when>
					<c:when test="${wcRepetSetupDVo.repetPerdCd eq 'EV_WK'}">
						<u:msg titleId="wc.option.wely" alt="주간" /> -
						<u:msg titleId="wc.jsp.setRepetPop.tx02" alt="매" /> ${wcRepetSetupDVo.repetWk} <u:msg titleId="wc.jsp.setRepetPop.tx04" alt="주마다" />
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
	<colgroup><col width="18%"/><col width="*"/></colgroup>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="cols.subj" alt="제목"/></td>
		<td class="body_lt">${wcSchdlBVo.subj }</td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="cols.loc" alt="장소" /></td>
		<td class="body_lt">${wcSchdlBVo.locNm }</td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="wc.cols.schdlPriod" alt="기간"/></td>
		<td class="body_lt">
			<table>
				<tr>
					<td>
						<c:choose>
							<c:when test="${wcSchdlBVo.alldayYn eq 'Y' }">
								<u:out value="${wcSchdlBVo.schdlStartDt }" type="date"/>~
								<u:out value="${wcSchdlBVo.schdlEndDt }" type="date"/>
							</c:when>
							<c:otherwise>
								<u:out value="${wcSchdlBVo.schdlStartDt }" type="longdate"/>~
								<u:out value="${wcSchdlBVo.schdlEndDt }" type="longdate"/>
							</c:otherwise>
						</c:choose>
					</td>
					<td>
						<u:checkbox name="" value="Y" titleId="wc.cols.alldayYn" alt="종일여부" checkValue="${wcSchdlBVo.alldayYn}" disabled="Y"/>
					</td>
					<td>
						<u:checkbox name="solaLunaYn" value="N" titleId="wc.option.luna" alt="음력" checkValue="${wcSchdlBVo.solaLunaYn }" disabled="Y"/>
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
							<c:if test="${!empty mailAcceptMap }"><th width="10%" class="head_ct"><u:msg titleId="wc.cols.guest.accept" alt="수락" /></th></c:if>
						</tr>
						<c:if test="${!empty wcPromGuestDVoList}">
							<c:forEach var="list" items="${wcPromGuestDVoList}" varStatus="status">
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
									<c:if test="${!empty mailAcceptMap }"><td class="body_ct" ><c:if test="${list.guestEmplYn eq 'Y'}"
										><u:convertMap srcId="mailAcceptMap" attId="${list.guestUid}" var="acptMap" 
										/><div class="ellipsis" title="<u:msg titleId="cols.regDt" alt="등록일시"/> : <u:out value="${acptMap.regDt}" type="longdate"/>">
										${acptMap.acptYn }</div></c:if><c:if test="${list.guestEmplYn ne 'Y'}">X</c:if></td>
									</c:if>
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
			<div style="overflow:auto;height:196px;" class="editor">${wcSchdlBVo.cont}</div>
		</td>
	</tr>

	<tr>
		<td class="head_lt"><u:msg titleId="cols.attFile" alt="첨부파일" /></td>
		<td>
			<c:if test="${!empty fileVoList }"><u:files id="${filesId}_view" fileVoList="${fileVoList}" module="wc" mode="view" urlParam="fncCal=${fncCal}"/></c:if>
		</td>

	</tr>

	</table>
	</div>
<u:blank />
	<u:buttonArea>
	<c:choose>
	<c:when test="${userAuth == 'fail' }">
	<c:set var="promMod"	value= "A" />
	<c:set var="promDel"	value= "A" />
	</c:when>
	<c:when test="${userAuth == 'pass' }">
	<c:set var="promMod"	value= "W" />
	<c:set var="promDel"	value= "W" />
	</c:when>
	</c:choose>

	<u:set var="isBtnCheck" test="${listPage eq 'listAllSchdl' || (plt eq null && fncCal eq 'my' && ( wcSchdlBVo.schdlKndCd eq '1' || wcSchdlBVo.schdlKndCd eq '2')) || ( fncCal ne 'my' && fncCal ne 'open' && ( wcSchdlBVo.schdlKndCd eq '3' || wcSchdlBVo.schdlKndCd eq '4') ) }" value="Y" elseValue="N"/>
	<c:if	test="${isBtnCheck eq 'Y'}">
		<u:button id="promModify" titleId="cm.btn.mod" onclick="setSchdlPop('${scds_schdlId }');" alt="수정" auth="${promMod}" />
		<u:button id="promDelete" titleId="cm.btn.del" href="javascript:promScdDel('del','${scds_schdlId}');" alt="삭제" auth="${promDel}" />
		<c:if test="${scdPidCount > 1 && wcSchdlBVo.repetYn eq 'Y'}">
		<u:button id="promAllDelete" titleId="cm.btn.allDel" href="javascript:promScdDel('allDel','${scds_schdlId}');" alt="전체삭제" auth="${promDel}" />
		</c:if>
	</c:if>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
	</u:buttonArea>

	</form>
</div>
