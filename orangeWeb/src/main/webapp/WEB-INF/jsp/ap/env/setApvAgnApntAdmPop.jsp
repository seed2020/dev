<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%// 대결자 추가 - 사용자 선택 %>
function addAgnt(){
	parent.searchUserPop({mode:'search'}, function(userVo){
		if(userVo!=null){
			callAjax('./transAgntAjx.do?menuId=${menuId}${uidParam}', {agntUid:userVo.userUid, agntRescId:userVo.rescId, mode:'reg'}, function(data){
				if(data.message!=null){
					alert(data.message);
				}
				if(data.result=='ok'){
					dialog.open('setAgnApntDialog','<u:msg alt="대결 지정" titleId="ap.jsp.setApvEnv.agnApntSetting"/>','./setApvAgnApntAdmPop.do?menuId=${menuId}&userUid=${param.userUid}');
				}
			});
		}
	});
}
<%// 대결자 삭제 %>
function delAgnt(){
	var agntUid = $("#agntUid option:selected").val();
	callAjax('./transAgntAjx.do?menuId=${menuId}${uidParam}', {agntUid:agntUid, mode:'del'}, function(data){
		if(data.message!=null){
			alert(data.message);
		}
		if(data.result=='ok'){
			dialog.open('setAgnApntDialog','<u:msg alt="대결 지정" titleId="ap.jsp.setApvEnv.agnApntSetting"/>','./setApvAgnApntAdmPop.do?menuId=${menuId}&userUid=${param.userUid}');
		}
	});
}
<%// 저장 %>
function saveAgnApnt(){
	
	var agntUid = $("#agntUid option:selected").val();
	if(agntUid==null || agntUid==''){
		alertMsg("cm.select.check.mandatory",["#ap.cols.agnt"]);<%//cm.select.check.mandatory="{0}"(을)를 선택해 주십시요.%>
		$("#strtDtAmPm").focus();
		return;
	}
	
	if(validator.validate("agnApntForm")){
		
		var today = getToday();
		if($("#endDt").val()<today){
			alertMsg("ap.msg.afterToday",["#cols.endDt"]);<%//ap.msg.afterToday={0}(을)를 오늘날짜 또는 이후로 설정해 주십시요.%>
			return;
		}
		
		if($("#strtDt").val()==$("#endDt").val()){
			if($("#strtDtAmPm option:selected").val()=="PM" && $("#endDtAmPm option:selected").val()=="AM"){
				alertMsg("cm.msg.errors.strtEndDt");<%//cm.msg.errors.strtEndDt=종료일은 시작일보다 빠를 수 없습니다.%>
				$("#strtDtAmPm").focus();
				return;
			}
		}
		
		var $form = $("#agnApntForm");
		$form.attr("action", "./transAgnApnt.do");
		$form.attr("target", "dataframe");
		$form.submit();
	}
}
<%// 삭제 %>
function delAgnApnt(agntUid, seq){
	if(agntUid==null){
		var $form = $("#agnApntForm");
		agntUid = $form.find("#agntUid").val();
		seq = $form.find("#seq").val();
	}
	if(seq!=null && seq!=""){
		callAjax('./transAgnApntDelAjx.do?menuId=${menuId}${uidParam}', {agntUid:agntUid, seq:seq}, function(data){
			if(data.message!=null){
				alert(data.message);
			}
			if(data.result=='ok'){
				reload();
			}
		});
	} else {
		alertMsg("cm.msg.noSelectedItem",["#ap.jsp.setApvEnv.agnApntInfo"]);<%//cm.msg.noSelectedItem=선택된 "{0}"(이)가 없습니다.%>
	}
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<div style="width:400px">

		<form id="agnApntForm" method="get">
		<input type="hidden" name="menuId" value="${menuId}" />
		<input type="hidden" id="userUid" name="userUid" value="${param.userUid}" />
		<input type="hidden" id="seq" name="seq" value="${apAgnApntDVo.seq}" />
		
		<u:listArea colgroup="25%,*">
			<tr>
			<td class="head_lt"><u:msg titleId="ap.cols.agnt" alt="대결자"/></td>
			<td><select id="agntUid" name="agntUid"<u:elemTitle titleId="ap.cols.agnt" alt="대결자" />><c:forEach
				items="${apAgnApntRVoList}" var="apAgnApntRVo" varStatus="status">
				<u:option value="${apAgnApntRVo.agntUid}" title="${apAgnApntRVo.agntRescNm}" checkValue="${apAgnApntDVo.agntUid}" />
				</c:forEach>
				</select> <u:buttonS titleId="cm.btn.add" alt="추가" onclick="addAgnt();" 
				/><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="delAgnt();" /></td>
			</tr>

			<tr>
			<td class="head_lt"><u:msg titleId="cols.absRson" alt="부재사유"/></td>
			<td><select id="absRsonCd" name="absRsonCd"<u:elemTitle titleId="cols.absRson" alt="부재사유" />><c:forEach items="${absRsonCdList}" var="absRsonCd" varStatus="status">
				<u:option value="${absRsonCd.cd}" title="${absRsonCd.rescNm}" checkValue="${apAgnApntDVo.absRsonCd}" />
				</c:forEach>
				</select></td>
			</tr>

			<tr>
			<td class="head_lt"><u:msg titleId="cols.strtDt" alt="시작일시" /></td>
			<td><u:calendar id="strtDt" titleId="cols.strtDt" option="{end:'endDt'}"
				value="${fn:substring(apAgnApntDVo.strtDt, 0, 10)}" mandatory="Y"><td><select id="strtDtAmPm" name="strtDtAmPm"<u:elemTitle
					titleId="cm.option.amPm" alt="오전 오후" />>
				<u:option value="AM" titleId="cm.option.am" alt="오전" selected="${fn:substring(apAgnApntDVo.strtDt, 11, 13) != '12'}" />
				<u:option value="PM" titleId="cm.option.pm" alt="오후" selected="${fn:substring(apAgnApntDVo.strtDt, 11, 13) == '12'}" />
				</select></td></u:calendar></td>
			</tr>

			<tr>
			<td class="head_lt"><u:msg titleId="cols.endDt" alt="종료일시" /></td>
			<td><u:calendar id="endDt" titleId="cols.endDt" option="{start:'strtDt'}"
				value="${fn:substring(apAgnApntDVo.endDt, 0, 10)}" mandatory="Y"><td><select id="endDtAmPm" name="endDtAmPm"<u:elemTitle
					titleId="cm.option.amPm" alt="오전 오후" />>
				<u:option value="AM" titleId="cm.option.am" alt="오전" selected="${fn:substring(apAgnApntDVo.endDt, 11, 13) == '12'}" />
				<u:option value="PM" titleId="cm.option.pm" alt="오후" selected="${fn:substring(apAgnApntDVo.endDt, 11, 13) != '12'}" />
				</select></td></u:calendar></td>
			</tr>
		</u:listArea>
		</form>

		<u:buttonArea>
			<u:button titleId="cm.btn.save" alt="저장" href="javascript:saveAgnApnt();" auth="W"
			/><u:button titleId="cm.btn.close" alt="닫기" href="javascript:dialog.close('setAgnApntDialog');"
			/>
		</u:buttonArea>

</div>
