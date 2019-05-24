<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ include
	file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"%>

<script type="text/javascript">
<!--
	$(function() {
		var schdlList = viewSearchMyScd();
		//$("#schdl_subj").text(schdlList.subj);
		$("#schdl_statYmd").text(schdlList.startYmd);
		$("#schdl_endYmd").text(schdlList.endYmd);
		$("#schdl_cont").html(schdlList.cont);

		if (schdlList.holiYn == 'Y') {
			$("#schdl_holiYn").text(
					'<u:msg titleId="wc.option.holi" alt="휴일" />');
		} else if (schdlList.holiYn == 'N') {
			$("#schdl_holiYn").text(
					'<u:msg titleId="wc.option.wday" alt="평일" />');
		}

		if (schdlList.solaLunaYn == 'Y') {
			$("#schdl_solaLunaYn").text(
					'<u:msg titleId="wc.option.sola" alt="양령" />');
		} else if (schdlList.solaLunaYn == 'N') {
			$("#schdl_solaLunaYn").text(
					'<u:msg titleId="wc.option.luna" alt="음력" />');
		}

		//나의일정 검색에서는 수정,삭제가 되서는 안된다.
		if (schdlList.searchMylist == "searchMylist"
				&& schdlList.searchMylist != null) {
			$('#annvModBtn').hide();
			$('#annvDelBtn').hide();
		} else {
			$('#annvModBtn').show();
			$('#annvDelBtn').show();
		}

	});

	function annvScdDel(schdlId) {

		if (confirmMsg("cm.cfrm.del")) {
<% // cm.cfrm.del=삭제하시겠습니까? %>
	callAjax('./transSetCommAnnvModPopDel.do?menuId=${menuId}',	{schdlId : schdlId},
					function(data) {
						if (data.message != null) {
							alert(data.message);
						}
						if (data.result == 'ok') {
							//dialog.close(this);
							location.href = './listCommAnnv.do?fncCal=${fncCal}&menuId=${menuId}';
						}
					});
		}
	}

	-->
</script>


<u:set test="${!empty param.fncCal}" var="fncCal"
	value="${param.fncCal}" elseValue="psn" />
<c:set var="annvNm" value="창립기념일" />
<c:set var="cont" value="이노비즈 창립기념일" />
<c:set var="strtYmd" value="2014-01-10" />
<c:set var="endYmd" value="2014-01-10" />
<c:set var="repetStrtDt" value="2013" />
<c:set var="repetEndDt" value="2023" />
<c:set var="holiYn" value="휴일" />
<c:set var="solaYn" value="양력" />

<div style="width: 700px">
	<form id="viewAnnv">
		<u:input type="hidden" id="menuId" value="${menuId}" />

		<%
			// 폼 필드
		%>
		<u:listArea>
			<c:if test="${fncCal == 'grp'}">
				<tr>
					<td class="head_lt"><u:msg titleId="wc.cols.regGrp" alt="등록그룹" /></td>
					<td class="body_lt" colspan="3">부천지역모임</td>
				</tr>
			</c:if>

			<tr>
				<td class="head_lt"><u:msg titleId="cols.annvNm" alt="기념일명" /></td>
				<td class="body_lt" colspan="3"><span id="schdl_subj">${wcAnnvDVo.rescNm }</span></td>
			</tr>

			<tr>
				<td class="head_lt"><u:msg titleId="cols.strtDt" alt="시작일" /></td>
				<td class="body_lt"><span id="schdl_statYmd"></span></td>
				<td class="head_lt"><u:msg titleId="cols.endDt" alt="종료일" /></td>
				<td class="body_lt"><span id="schdl_endYmd"></span></td>
			</tr>

			<tr>
				<td class="head_lt"><u:msg titleId="cols.repetStrtDt" alt="반복시작일시" /></td>
				<td class="body_lt">
					<fmt:parseDate var="repetDateStartDt" value="${returnWcrVo.repetStartDt}" pattern="yyyy-MM-dd HH:mm:ss" />
					<fmt:formatDate var="repetStartY" value="${repetDateStartDt}" pattern="yyyy" /> ${repetStartY}
				</td>
				<td class="head_lt"><u:msg titleId="cols.repetEndDt" alt="반복종료일시" /></td>
				<td class="body_lt">
					<fmt:parseDate var="repetDateEndDt" value="${returnWcrVo.repetEndDt}" pattern="yyyy-MM-dd HH:mm:ss" />
					<fmt:formatDate var="repetEndY" value="${repetDateEndDt}" pattern="yyyy" /> ${repetEndY}
				</td>
			</tr>

			<tr>
				<td class="head_lt"><u:msg titleId="cols.holiYn" alt="휴일여부" /></td>
				<td class="body_lt"><span id="schdl_holiYn"></span></td>
				<td class="head_lt"><u:msg titleId="cols.solaYn" alt="양력여부" /></td>
				<td class="body_lt"><span id="schdl_solaLunaYn"></span></td>
			</tr>
			<tr>
			<td class="head_lt"><u:msg titleId="wc.btn.set.nat" alt="국가설정" /></td>
			<td class="body_lt" colspan="3">${wcNatBVo.rescNm }</td>
			</tr>
	

		</u:listArea>

		<u:buttonArea>
			<u:button id="annvModBtn" titleId="cm.btn.mod" onclick="dialog.close(this); dialog.open('setCommAnnvModPop','기념일수정','./setCommAnnvModPop.do?menuId=${menuId}&fnc=mod&fncCal=${fncCal}&repetStartY=${repetStartY}&repetEndY=${repetEndY}&scds_schdlId=${scds_schdlId}');" alt="수정" auth="W" />
			<u:button id="annvDelBtn" titleId="cm.btn.del" href="javascript:annvScdDel('${scds_schdlId}');" alt="삭제" auth="W" />
			<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
		</u:buttonArea>

	</form>
</div>
