<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
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
function modAgnApnt(agntUid, seq){
	parent.setAgnApnt('&agntUid='+agntUid+'&seq='+seq);
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>


	<u:listArea id="apAgnApntDVoListArea" tbodyClass="group_smu" >
	
		<tr>
		<c:if test="${false}">
		<th width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('apAgnApntDVoListArea', this.checked);" value=""/></th>
		</c:if>
		<th width="15%" class="head_ct"><u:msg titleId="ap.cols.agnt" alt="대결자"/></th>
		<th width="45%" class="head_ct"><u:msg titleId="ap.cols.prd" alt="기간"/></th>
		<th class="head_ct"><u:msg titleId="cols.absRson" alt="부재사유"/></th>
		<th width="15%" class="head_ct"><u:msg titleId="cm.btn.del" alt="삭제"/></th>
		</tr>
		<c:if test="${fn:length(apAgnApntDVoList)==0}" >
		<tr>
			<td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
		</c:if>
		<c:forEach items="${apAgnApntDVoList}" var="apAgnApntDVo" varStatus="status">
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
		<c:if test="${false}">
		<td class="bodybg_ct"><input type="checkbox" value="${apAgnApntDVo.agntUid}" data-seq="${apAgnApntDVo.seq}"/></td>
		</c:if>
		<td class="body_ct" id="${apAgnApntDVo.agntUid}_${apAgnApntDVo.seq}" style="padding-left:0px"><a href="javascript:modAgnApnt('${apAgnApntDVo.agntUid}','${apAgnApntDVo.seq}');">${apAgnApntDVo.agntNm}</a></td>
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

