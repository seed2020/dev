<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%
// 이관 진행 내역 팝업 - 진행상태 클릭 하면 %>
function listDeployProcPop(genId, formNo){
	parent.listDeployProcPop(genId, formNo);
}<% // 배포 완료시 목록 리로드 %>
function listDeployReload(){
	reload();
}
<% // [하단버튼:삭제] - 팝업 %>
function delGenList(){
	var arr = getCheckedValue("listArea", "cm.msg.noSelect");<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
	if (arr != null && confirmMsg("cm.cfrm.del")) {<% // 삭제하시겠습니까? %>
		callAjax('./transDeployDelAjx.do?menuId=${menuId}', {genIds:arr}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				reload();
			}
		});
	}
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<u:listArea id="listArea" colgroup="3%,,19%,19%,11%">

	<tr id="headerTr">
		<th class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></th>
		<th class="head_ct"><u:msg titleId="wf.cols.form.nm" alt="양식명" /></th>
		<th class="head_ct"><u:msg titleId="cols.strtDt" alt="시작일시" /></th>
		<th class="head_ct"><u:msg titleId="cols.endDt" alt="종료일시" /></th>
		<th class="head_ct"><u:msg titleId="cols.prgStat" alt="진행상태" /></th>
	</tr>
	<c:if test="${fn:length(wfFormGenBVoList) == 0}">
		<tr>
		<td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
	<c:forEach items="${wfFormGenBVoList}" var="wfFormGenBVo" varStatus="status">
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" >
		<td class="bodybg_ct"><c:if test="${wfFormGenBVo.delYn eq 'Y'}"><input type="checkbox" value="${wfFormGenBVo.genId}"/></c:if></td>
		<td class="body_ct"><div style="min-height:40px;margin-left:10px;"><c:forEach items="${wfFormGenBVo.wfFormGenLVoList}" var="wfFormGenLVo" varStatus="wfFormGenLVoStatus"
		><div class="ubox"><dl><dd 
		class="title"><a href="javascript:listDeployProcPop('${wfFormGenBVo.genId}', '${wfFormGenLVo.formNo }')">${wfFormGenLVo.formRescNm }</a></dd></dl></div></c:forEach></div></td>
		<td class="body_lt"><u:out value="${wfFormGenBVo.strtDt }" type="longdate" /></td>
		<td class="body_ct"><u:out value="${wfFormGenBVo.endDt }" type="longdate" /></td>
		<td class="body_ct"><c:if test="${!empty wfFormGenBVo.procStatCd }"><u:msg titleId="ap.tran.${wfFormGenBVo.procStatCd}" alt="preparing:준비중, processing:진행중, completed:완료, error:에러" /></c:if></td>
	</tr>
	</c:forEach>
	
</u:listArea>
<u:blank />
<u:pagination pageRowCnt="10" noLeftSelect="true"/>