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
					//$("#agntUid").append("<option value=\""+userVo.userUid+"\">"+userVo.rescNm+"</option>");
					reload();
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
			//$("#agntUid").append("<option value=\""+userVo.userUid+"\">"+userVo.rescNm+"</option>");
			reload();
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
		$form.attr("target", "dataframeForFrame");
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
<%// 대결 지정 정보 상세 조회 - 대결 정보(대결자명) 클릭-왼쪽 > 대결 지정 정보 세팅 - 오른쪽 %>
function setAgnApnt(agntUid, seq){
	if($("#agntUid option[value='"+agntUid+"']").length<1){
		alertMsg("ap.jsp.setApvEnv.msg.noUser",["#ap.cols.agnt"]);<%//ap.jsp.setApvEnv.msg.noUser=해당하는 {0}이(가) 없습니다.%>
		return;
	}
	
	callAjax('./getAgnApntAjx.do?menuId=${menuId}${uidParam}', {agntUid:agntUid, seq:seq}, function(data){
		if(data.message!=null){
			alert(data.message);
		}
		if(data.apAgnApntDVo!=null){
			setAgnApntData(data.apAgnApntDVo.map);
			<%// 선택 하일라이트 %>
			$("#apAgnApntDVoListArea td").each(function(){
				if($(this).attr('id')==(agntUid+"_"+seq)){
					$(this).addClass("text_on");
				} else if($(this).hasClass("text_on")) {
					$(this).removeClass("text_on");
				}
			});
		}
	});
}
<%// 선택한 데이터를 오른쪽 뷰(수정용)에 세팅함 %>
function setAgnApntData(vo){
	var $form = $("#agnApntForm");
	if(vo!=null){
		$form.find("#seq").val(vo.seq);
		$form.find("#agntUid").val(vo.agntUid).trigger('click');
		$form.find("#absRsonCd").val(vo.absRsonCd).trigger('click');
		if(vo.strtDt.length>13){
			$form.find("#strtDt").val(vo.strtDt.substring(0,10));
			$form.find("#strtDtAmPm").val(vo.strtDt.substring(11,13)=='12' ? 'PM' : 'AM').trigger('click');
		}
		if(vo.endDt.length>13){
			$form.find("#endDt").val(vo.endDt.substring(0,10));
			$form.find("#endDtAmPm").val(vo.endDt.substring(11,13)=='12' ? 'AM' : 'PM').trigger('click');
		}
	} else {
		$form.find("#seq").val("");
		$form.find("#agntUid option:eq(0)").attr("selected", "selected").trigger('click');
		$form.find("#absRsonCd option:eq(0)").attr("selected", "selected").trigger('click');
		$form.find("#strtDt").val("");
		$form.find("#strtDtAmPm").val("AM").trigger('click');
		$form.find("#endDt").val("");
		$form.find("#endDtAmPm").val("PM").trigger('click');
	}
}
<%// 신규 - 소버튼 %>
function setNewAgnApnt(){
	$("#apAgnApntDVoListArea td").each(function(){
		if($(this).hasClass("text_on")) {
			$(this).removeClass("text_on");
		}
	});
	setAgnApntData();
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>


<div style="padding:10px;">

	<div style="float:left; width:49.5%; overflow-y:auto;">
	<u:title alt="대결 정보" titleId="ap.jsp.setApvEnv.agnApntInfo" type="small" />
	
	<u:listArea id="apAgnApntDVoListArea" tbodyClass="group_smu" >
		<tr>
		<td width="15%" class="head_ct"><u:msg titleId="ap.cols.agnt" alt="대결자"/></td>
		<td width="50%" class="head_ct"><u:msg titleId="ap.cols.prd" alt="기간"/></td>
		<td class="head_ct"><u:msg titleId="cols.absRson" alt="부재사유"/></td>
		<td width="15%" class="head_ct"><u:msg titleId="cm.btn.del" alt="삭제"/></td>
		</tr>
		<c:if test="${fn:length(apAgnApntDVoList)==0}" >
		<tr>
			<td class="nodata" colspan="4"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
		</c:if>
		<c:forEach items="${apAgnApntDVoList}" var="apAgnApntDVo" varStatus="status">
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
		<td class="body_ct" id="${apAgnApntDVo.agntUid}_${apAgnApntDVo.seq}" style="padding-left:0px"><a href="javascript:setAgnApnt('${apAgnApntDVo.agntUid}','${apAgnApntDVo.seq}')">${apAgnApntDVo.agntNm}</a></td>
		<td class="body_ct"><u:out
			value="${apAgnApntDVo.strtDt}" type="date" /> <c:if
			test="${fn:substring(apAgnApntDVo.strtDt, 11, 13) == '12'}"><u:msg titleId="cm.option.pm" alt="오후" /></c:if><c:if
			test="${fn:substring(apAgnApntDVo.strtDt, 11, 13) != '12'}"><u:msg titleId="cm.option.am" alt="오전" /></c:if>
			 ~ 
			<u:out
			value="${apAgnApntDVo.endDt}" type="date" /> <c:if
			test="${fn:substring(apAgnApntDVo.endDt, 11, 13) == '12'}"><u:msg titleId="cm.option.am" alt="오전" /></c:if><c:if
			test="${fn:substring(apAgnApntDVo.endDt, 11, 13) != '12'}"><u:msg titleId="cm.option.pm" alt="오후" /></c:if></td>
		<td class="body_ct">${apAgnApntDVo.absRsonNm}</td>
		<td class="body_ct"><u:buttonS titleId="cm.btn.del" alt="삭제" href="javascript:delAgnApnt('${apAgnApntDVo.agntUid}','${apAgnApntDVo.seq}');"
			/></td>
		</tr>
		</c:forEach>
	</u:listArea>

	</div>
	
	<div style="float:right; width:49.5%;">
		<u:title alt="대결 지정" titleId="ap.jsp.setApvEnv.agnApntSetting" type="small" />

		<form id="agnApntForm" method="get">
		<input type="hidden" name="menuId" value="${menuId}" />
		<input type="hidden" id="seq" name="seq" value="" />
		
		<u:listArea colgroup="25%,*">
			<tr>
			<td class="head_lt"><u:msg titleId="ap.cols.agnt" alt="대결자"/></td>
			<td><select id="agntUid" name="agntUid"<u:elemTitle titleId="ap.cols.agnt" alt="대결자" />><c:forEach
				items="${apAgnApntRVoList}" var="apAgnApntRVo" varStatus="status">
				<u:option value="${apAgnApntRVo.agntUid}" title="${apAgnApntRVo.agntRescNm}" />
				</c:forEach>
				</select> <u:buttonS titleId="cm.btn.add" alt="추가" onclick="addAgnt();" 
				/><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="delAgnt();" /></td>
			</tr>

			<tr>
			<td class="head_lt"><u:msg titleId="cols.absRson" alt="부재사유"/></td>
			<td><select id="absRsonCd" name="absRsonCd"<u:elemTitle titleId="cols.absRson" alt="부재사유" />><c:forEach items="${absRsonCdList}" var="absRsonCd" varStatus="status">
				<u:option value="${absRsonCd.cd}" title="${absRsonCd.rescNm}" />
				</c:forEach>
				</select></td>
			</tr>

			<tr>
			<td class="head_lt"><u:msg titleId="cols.strtDt" alt="시작일시" /></td>
			<td><u:calendar id="strtDt" titleId="cols.strtDt" option="{end:'endDt'}" mandatory="Y"><td><select id="strtDtAmPm" name="strtDtAmPm"<u:elemTitle titleId="cm.option.amPm" alt="오전 오후" />>
				<option value="AM"><u:msg titleId="cm.option.am" alt="오전" /></option>
				<option value="PM"><u:msg titleId="cm.option.pm" alt="오후" /></option>
				</select></td></u:calendar></td>
			</tr>

			<tr>
			<td class="head_lt"><u:msg titleId="cols.endDt" alt="종료일시" /></td>
			<td><u:calendar id="endDt" titleId="cols.endDt" option="{start:'strtDt'}" mandatory="Y"><td><select id="endDtAmPm" name="endDtAmPm"<u:elemTitle titleId="cm.option.amPm" alt="오전 오후" />>
				<option value="AM"><u:msg titleId="cm.option.am" alt="오전" /></option>
				<option value="PM" selected="selected"><u:msg titleId="cm.option.pm" alt="오후" /></option>
				</select></td></u:calendar></td>
			</tr>
		</u:listArea>
		</form>

		<u:buttonArea>
			<u:button titleId="cm.btn.new" alt="신규" href="javascript:setNewAgnApnt()" auth="W"
			/><u:button titleId="cm.btn.save" alt="저장" href="javascript:saveAgnApnt();" auth="W"
			/><u:button titleId="cm.btn.del" alt="삭제" href="javascript:delAgnApnt();" auth="W"
			/>
		</u:buttonArea>
	</div>
</div>
