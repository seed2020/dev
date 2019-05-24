<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<% // 열람요청허용여부 %>
function dtlViewOptChk() {
	var checked = $('#dtlViewOptArea input[type="radio"]:checked').eq(0);
	if($(checked).val() == 'Y') $('#dtlViewOptDtlArea').show();
	else $('#dtlViewOptDtlArea').hide();
}<% // [하단버튼:항목관리] 항목 관리 %>
function setDocNoResetPop() {
	dialog.open('setDocNoResetDialog','<u:msg titleId="dm.cols.docNoReset" alt="문서번호 초기화" />','./setDocNoResetPop.do?menuId=${menuId}&docNoDftOrg='+$('#docNoDftOrg').val());
}<% // [하단버튼:항목관리] 항목 관리 %>
function setItemMngPop(id) {
	dialog.open('setItemMngPop','<u:msg titleId="dm.jsp.setItemMgm" alt="항목관리" />','./setItemMngPop.do?menuId=${menuId}&itemTypCd='+id);
}<% // [하단버튼:목록순서] 목록 순서 %>
function setListOrdrPop(id) {
	dialog.open('setListOrdrPop','<u:msg titleId="bb.jsp.setListOrdrPop.title" alt="목록순서" />','./setListOrdrPop.do?menuId=${menuId}&itemTypCd='+id);
}<%
// 회사 - 선택 %>
function selComp(compId){
	location.href="./setEnv.do?menuId=${menuId}&compId="+compId;
};<% // 문서번호채번 - 연도 선택 %>
function recoDtSelect(obj){
	var disabled = $(obj).val() == 'Y' ? false : true;
	setDisabled($('#recoMt'), disabled);
	setDisabled($('#recoDt'), disabled);
	
}<% // 저장 - 버튼 클릭 %>
function save(){
	//var param = new ParamMap().getData("setEnvForm");
	// 작업코드배열 생성
	var taskCds = [];
	$("#taskCdArea input[type='checkbox']:checked").each(function(){
		taskCds.push($(this).val());
	});
	// disabled 처리
	$("#taskCdArea input[type='checkbox']").each(function(){
		$(this).attr('disabled',true);
	});
	
	var $form = $("#setEnvForm");
	$form.find("input[name='taskCds']").remove();
	$form.appendHidden({name:'taskCds',value:taskCds.join(',')});
	$form.attr('method','post');
	$form.attr('action','./transEnv.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form.submit();
};
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="dm.jsp.setEnv" alt="환경설정" />
<%-- <u:secu auth="SYS">
<u:listArea colgroup="15%,*" noBottomBlank="true">
	<tr>
	<td class="head_lt"><u:msg titleId="cols.comp" alt="회사" /></td>
	<td>
		<select id="compId" name="compId" <u:elemTitle titleId="cols.comp" /> onchange="selComp(this.value);">
			<c:forEach items="${ptCompBVoList}" var="ptCompBVo" varStatus="status">
				<u:option value="${ptCompBVo.compId}" title="${ptCompBVo.rescNm}" checkValue="${param.compId}"/>
			</c:forEach>
		</select>
	</td>
	</tr>
</u:listArea>
<u:blank />
</u:secu> --%>
<form id="setEnvForm" method="post">
<input type="hidden" name="menuId" value="${menuId}" />

<u:title titleId="dm.jsp.setEnv.docVer" alt="문서버전" type="small" />
<c:set var="colgroup" value="15%,35%,15%,35%"/>
<u:listArea colgroup="${colgroup }" noBottomBlank="true">
<tr>
	<td class="head_lt"><u:msg titleId="dm.cfg.verDft" alt="버전(시작)" /></td>
	<td class="bodybg_lt">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><u:input id="verDft" titleId="cols.nm" value="${envConfigMap.verDft }" maxByte="30" mandatory="Y" valueOption="number" valueAllowed="."/></td>
		<td class="width10"></td>
		<u:checkbox value="Y" name="verMnalYn" titleId="dm.cfg.verMnalYn" alt="수동입력여부" checked="${envConfigMap.verMnalYn == 'Y'}" />
		<td class="width5"></td>
		</tr>
		</table>
	</td>
	<td class="head_lt"><u:msg titleId="dm.cfg.verNo" alt="버전번호" /></td>
	<td  class="bodybg_lt">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="bodyip_lt"><strong><u:msg titleId="dm.cfg.verFrt" alt="버전앞자리"/></strong></td>
		<td><u:input id="verFrt" titleId="dm.cfg.verFrt" value="${envConfigMap.verFrt }" maxByte="3" mandatory="Y" style="width:50px;" valueOption="number" valueAllowed="."/></td>
		<td class="width10"></td>
		<td class="bodyip_lt"><strong><u:msg titleId="dm.cfg.verRear" alt="버전뒷자리"/></strong></td>
		<td><u:input id="verRear" titleId="dm.cfg.verRear" value="${envConfigMap.verRear }" maxByte="3" mandatory="Y" style="width:50px;" valueOption="number" valueAllowed="."/></td>
		<td class="width5"></td>
		</tr>
		</table>
	</td>
</tr>
</u:listArea>

<u:blank />
<u:title titleId="dm.jsp.setEnv.docNo" alt="문서번호" type="small" />
<u:listArea colgroup="15%,*" noBottomBlank="true">
<tr>
	<td class="head_lt"><u:msg titleId="dm.cols.docNoDisp" alt="문서번호(표시)" /></td>
	<td class="bodybg_lt" ><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select id="docNoOpt1" name="docNoOpt1">
		<u:option value="notUse" titleId="cm.option.notUse" alt="사용안함" selected="${envConfigMap.docNoOpt1 == 'notUse'}"/>
		<u:option value="YYYY" titleId="dm.cfg.docNoYYYY" alt="연도(YYYY)" selected="${envConfigMap.docNoOpt1 == 'YYYY'}"/>
		<u:option value="YY" titleId="dm.cfg.docNoYY" alt="연도(YY)" selected="${envConfigMap.docNoOpt1 == 'YY'}"/>
		<u:option value="fld" titleId="dm.cols.fld" alt="폴더" selected="${envConfigMap.docNoOpt1 == 'fld'}"/>
		<u:option value="orgNm" titleId="dm.cols.orgNm" alt="조직명" selected="${envConfigMap.docNoOpt1 == 'orgNm'}"/>
		<u:option value="orgAbs" titleId="dm.cols.orgAbs" alt="조직약어" selected="${envConfigMap.docNoOpt1 == 'orgAbs'}"/>
		</select></td>
		<td class="width15"></td>
		<td><select id="docNoOpt2" name="docNoOpt2" >
		<u:option value="notUse" titleId="cm.option.notUse" alt="사용안함" selected="${envConfigMap.docNoOpt2 == 'notUse'}"/>
		<u:option value="YYYY" titleId="dm.cfg.docNoYYYY" alt="연도(YYYY)" selected="${envConfigMap.docNoOpt2 == 'YYYY'}"/>
		<u:option value="YY" titleId="dm.cfg.docNoYY" alt="연도(YY)" selected="${envConfigMap.docNoOpt2 == 'YY'}"/>
		<u:option value="fld" titleId="dm.cols.fld" alt="폴더" selected="${envConfigMap.docNoOpt2 == 'fld'}"/>
		<u:option value="orgNm" titleId="dm.cols.orgNm" alt="조직명" selected="${envConfigMap.docNoOpt2 == 'orgNm'}"/>
		<u:option value="orgAbs" titleId="dm.cols.orgAbs" alt="조직약어" selected="${envConfigMap.docNoOpt2 == 'orgAbs'}"/>
		</select></td>
		<td class="width15"></td>
		<td><select id="docNoOpt3" name="docNoOpt3">
		<u:option value="notUse" titleId="cm.option.notUse" alt="사용안함" selected="${envConfigMap.docNoOpt3 == 'notUse'}"/>
		<u:option value="YYYY" titleId="dm.cfg.docNoYYYY" alt="연도(YYYY)" selected="${envConfigMap.docNoOpt3 == 'YYYY'}"/>
		<u:option value="YY" titleId="dm.cfg.docNoYY" alt="연도(YY)" selected="${envConfigMap.docNoOpt3 == 'YY'}"/>
		<u:option value="fld" titleId="dm.cols.fld" alt="폴더" selected="${envConfigMap.docNoOpt3 == 'fld'}"/>
		<u:option value="orgNm" titleId="dm.cols.orgNm" alt="조직명" selected="${envConfigMap.docNoOpt3 == 'orgNm'}"/>
		<u:option value="orgAbs" titleId="dm.cols.orgAbs" alt="조직약어" selected="${envConfigMap.docNoOpt3 == 'orgAbs'}"/>
		</select></td>
		<td class="width15"></td>
		<td class="bodyip_lt"><strong><u:msg titleId="ap.cfg.docNoSeqLen" alt="일련번호"/></strong></td>
		<td><select id="docNoSeqLen" name="docNoSeqLen">
		<u:option value="3" titleId="ap.cfg.len3" alt="3자리" selected="${envConfigMap.docNoSeqLen == '3'}"/>
		<u:option value="4" titleId="ap.cfg.len4" alt="4자리" selected="${empty envConfigMap.docNoSeqLen or envConfigMap.docNoSeqLen == '4'}"/>
		<u:option value="5" titleId="ap.cfg.len5" alt="5자리" selected="${envConfigMap.docNoSeqLen == '5'}"/>
		<u:option value="6" titleId="ap.cfg.len6" alt="6자리" selected="${envConfigMap.docNoSeqLen == '6'}"/>
		<u:option value="7" titleId="ap.cfg.len7" alt="7자리" selected="${envConfigMap.docNoSeqLen == '7'}"/>
		<u:option value="8" titleId="ap.cfg.len8" alt="8자리" selected="${envConfigMap.docNoSeqLen == '8'}"/>
		</select></td>
		<td class="width15"></td>
		<u:checkbox value="Y" name="docNoFxLen" titleId="dm.cfg.docNoFxLen" alt="고정 길이 일련번호" checked="${envConfigMap.docNoFxLen == 'Y'}" />
		<td class="width15"></td>
		<u:checkbox value="Y" name="docNoMnalYn" titleId="dm.cfg.docNoMnalYn" alt="수동입력" checked="${envConfigMap.docNoMnalYn == 'Y'}" />
		</tr>
		</table>
	</td>
</tr>
<tr>
	<td class="head_lt"><u:msg titleId="dm.cols.docNoDft" alt="문서번호(채번기준)" /></td>
	<td class="bodybg_lt" ><u:set var="recoDtDisabled" test="${envConfigMap.docNoDftYear == 'Y'}" value="" elseValue="disabled=\"disabled\""/><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="bodyip_lt"><strong><u:msg titleId="dm.option.year" alt="연도"/></strong></td>
		<td><select id="docNoDftYear" name="docNoDftYear" >
		<u:option value="N" titleId="cm.option.notUse" alt="사용안함" selected="${envConfigMap.docNoDftYear == 'N'}"/>
		<u:option value="Y" titleId="cm.option.use" alt="사용" selected="${envConfigMap.docNoDftYear == 'Y'}"/>		
		</select></td>
		<td class="width15"></td>
		<td class="bodyip_lt"><strong><u:msg titleId="ap.cfg.recoDt" alt="회계 기준일"/></strong></td>
		<td><select id="recoMt" name="recoMt" >
			<c:forEach items="${months}" var="month" varStatus="status">
			<u:option value="${month}" termId="cm.m.${month}" selected="${envConfigMap.recoMt == month}"
			/></c:forEach>
			</select></td>
		<td class="width5"></td>
		<td><select id="recoDt" name="recoDt" >
			<c:forEach items="${days}" var="day" varStatus="status">
			<u:option value="${day}" termId="cm.d.${day}" selected="${envConfigMap.recoMt == day}"
			/></c:forEach>
			</select></td>
		<td class="width15"></td>		
		<td class="bodyip_lt"><strong><u:msg titleId="dm.option.org" alt="조직"/></strong></td>
		<td><select id="docNoDftOrg" name="docNoDftOrg" >
		<u:option value="N" titleId="cm.option.notUse" alt="사용안함" selected="${envConfigMap.docNoDftOrg == 'N'}"/>
		<u:option value="Y" titleId="cm.option.use" alt="사용" selected="${envConfigMap.docNoDftOrg == 'Y'}"/>
		</select></td>
		<td class="width15"></td>
		<td><u:buttonS href="javascript:setDocNoResetPop();" titleId="dm.cols.docNoReset" alt="문서번호 초기화" auth="A" /></td>
		</tr>
		</table>
	</td>
</tr>
<%-- <tr>
	<td class="head_lt"><u:msg titleId="dm.cols.docNoReset" alt="문서번호(초기화)" /></td>
	<td class="bodybg_lt" ><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><u:buttonS href="javascript:docNoReset('all');" titleId="dm.btn.docNo.reset.all" alt="전체" auth="A" /></td>
		<td class="width5"></td>
		<td><u:buttonS href="javascript:docNoReset('org');" titleId="dm.btn.docNo.reset.org" alt="조직별" auth="A" /></td>
		<td class="width5"></td>
		</tr>
		</table>
	</td>
</tr> --%>
</u:listArea>
<u:blank />

<u:title titleId="dm.jsp.setEnv.lstTyp" alt="문서보기" type="small" />
<u:listArea colgroup="15%,,10%" noBottomBlank="true">
<tr>
	<td class="head_lt"><u:msg titleId="dm.cfg.lstTyp" alt="보기형식" /></td>
	<td class="bodybg_lt">
		<u:checkArea>
			<u:radio name="lstTyp" value="F" titleId="dm.cfg.fld" inputClass="bodybg_lt" checkValue="${envConfigMap.lstTyp }" checked="${empty envConfigMap.lstTyp }"/>
			<u:radio name="lstTyp" value="C" titleId="dm.cfg.cls" inputClass="bodybg_lt" checkValue="${envConfigMap.lstTyp }" />
			<u:radio name="lstTyp" value="L" titleId="dm.cfg.list" inputClass="bodybg_lt" checkValue="${envConfigMap.lstTyp }" />
		</u:checkArea>
	</td>
</tr>
<tr>
	<td class="head_lt"><u:msg titleId="dm.cols.cls" alt="분류체계" /></td>
	<td class="bodybg_lt"><u:buttonS href="javascript:setItemMngPop('C');" titleId="dm.jsp.setItemMgm" alt="항목관리" auth="A" />
					<u:buttonS href="javascript:setListOrdrPop('C');" titleId="bb.btn.listOrdr" alt="목록순서" auth="A" /></td>
</tr>
<tr>
	<td class="head_lt"><u:msg titleId="dm.cfg.list" alt="목록" /></td>
	<td class="bodybg_lt"><u:buttonS href="javascript:setItemMngPop('L');" titleId="dm.jsp.setItemMgm" alt="항목관리" auth="A" />
					<u:buttonS href="javascript:setListOrdrPop('L');" titleId="bb.btn.listOrdr" alt="목록순서" auth="A" /></td>
</tr>
</u:listArea>

<u:blank />

<u:title titleId="dm.jsp.setEnv.ddlnSetup" alt="기한설정" type="small" />
<c:set var="days" value="30"/>
<c:set var="months" value="6"/>
<u:listArea colgroup="15%," noBottomBlank="true">
<tr>
	<td class="head_lt"><u:msg titleId="dm.cols.newDoc" alt="최신문서" /></td>
	<td class="bodybg_lt"><select name="newDdln">
		<c:forEach var="val" begin="1" end="${days }" step="1">
			<u:option value="${val }D" titleId="cm.d.${val}" checkValue="${envConfigMap.newDdln}" />
		</c:forEach>
		<%-- <c:forEach var="val" begin="1" end="${months }" step="1">
			<u:option value="${val }M" titleId="cm.m.${val}" checkValue="${envConfigMap.ddlnNew}" />
		</c:forEach> --%>
	</select>
	</td>
</tr>
<tr>
	<td class="head_lt"><u:msg titleId="dm.cols.keepPrd" alt="보존연한" /></td>
	<td class="bodybg_lt"><select name="keepDdln">
		<u:msg titleId="bb.cols.day" alt="일" var="day" />
		<c:forEach var="val" begin="1" end="30" step="1">
			<u:option value="${val }D" titleId="cm.d.${val}" checkValue="${envConfigMap.keepDdln}" />
		</c:forEach>
		<%-- <c:forEach var="val" begin="1" end="${months }" step="1">
			<u:option value="${val }M" titleId="cm.m.${val}" checkValue="${envConfigMap.ddlnNew}" />
		</c:forEach> --%>
	</select>
	</td>
</tr>
</u:listArea>

<u:blank />

<u:title titleId="dm.jsp.setEnv.docHst" alt="문서이력" type="small" />
<u:listArea colgroup="15%," noBottomBlank="true">
<tr>
	<td class="head_lt"><u:msg titleId="dm.cols.taskGubun" alt="작업구분" /></td>
	<td class="bodybg_lt">
		<u:checkArea2 id="taskCdArea">
			<c:forEach var="colmVo" items="${taskList }" varStatus="status">
				<c:if test="${!fn:contains(exKeys,colmVo.va) }">
					<c:set var="checked" value="N"/>
					<c:forTokens var="taskCd" items="${envConfigMap.taskCds }" delims=","><c:if test="${colmVo.va eq taskCd}"><c:set var="checked" value="Y"/></c:if></c:forTokens>
					<u:checkbox2 id="taskCd${status.index }" name="taskCd" value="${colmVo.va}" title="${colmVo.msg }" alt="${colmVo.msg }" checked="${checked eq 'Y' }"/>
				</c:if>
			</c:forEach>
		</u:checkArea2>
	</td>
</tr>
<tr>
	<td class="head_lt"><u:msg titleId="dm.cols.doc.listHstOpt" alt="조회이력옵션" /></td>
	<td class="bodybg_lt">
		<u:checkArea><!-- daily:1일1회, user:한번, unlimited:무제한 -->
			<u:radio name="readCntRule" value="daily" titleId="dm.cfg.readCnt.daily" inputClass="bodybg_lt" checkValue="${envConfigMap.readCntRule }" checked="${empty envConfigMap.readCntRule }"/>
			<u:radio name="readCntRule" value="user" titleId="dm.cfg.readCnt.user" inputClass="bodybg_lt" checkValue="${envConfigMap.readCntRule }" />
			<u:radio name="readCntRule" value="unlimited" titleId="dm.cfg.readCnt.unlimited" inputClass="bodybg_lt" checkValue="${envConfigMap.readCntRule }" />
		</u:checkArea>
	</td>
</tr>

</u:listArea>

<u:blank />

<u:title titleId="dm.cols.doc.list" alt="문서목록조회" type="small" />
<u:listArea colgroup="15%," noBottomBlank="true">
<tr>
	<td class="head_lt"><u:msg titleId="dm.cols.doc.listOpt" alt="조회옵션" /></td>
	<td class="bodybg_lt">
		<u:checkArea><!-- A:전체조회, B:권한적용 -->
			<u:radio name="listOpt" value="A" titleId="dm.cfg.listOpt.all" inputClass="bodybg_lt" checkValue="${envConfigMap.listOpt }" checked="${empty envConfigMap.listOpt }"/>
			<u:radio name="listOpt" value="B" titleId="dm.cfg.listOpt.auth" inputClass="bodybg_lt" checkValue="${envConfigMap.listOpt }" />
		</u:checkArea>
	</td>
</tr>
<tr>
	<td class="head_lt"><u:msg titleId="dm.cols.dtlView" alt="상세보기" /></td>
	<td class="bodybg_lt">
		<u:checkArea id="dtlViewOptArea"><!-- Y:열람요청허용, N:열람요청허용안함 -->
			<u:radio name="dtlViewOpt" value="Y" titleId="dm.cols.dtlViewY" inputClass="bodybg_lt" checkValue="${envConfigMap.dtlViewOpt }" onclick="dtlViewOptChk();"/>
			<u:radio name="dtlViewOpt" value="N" titleId="dm.cols.dtlViewN" inputClass="bodybg_lt" checkValue="${envConfigMap.dtlViewOpt }" checked="${empty envConfigMap.dtlViewOpt }" onclick="dtlViewOptChk();"/>
		</u:checkArea>
		<u:set var="dtlViewOptDtlAreaStyle" test="${empty envConfigMap.dtlViewOpt || envConfigMap.dtlViewOpt == 'N' }" value="style=\"display:none;\""/>
		<div id="dtlViewOptDtlArea" ${dtlViewOptDtlAreaStyle }><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="bodyip_lt"><strong><u:msg titleId="dm.cols.dtlView.prd.isAllow" alt="기간선택여부"/></strong></td>
		<td><select name="dtlViewPrdAllow" >
		<u:option value="Y" titleId="dm.cols.dtlView.prd.allow" alt="기간선택허용" selected="${empty envConfigMap.dtlViewOptPrdAllow || envConfigMap.dtlViewOptPrdAllow == 'Y'}"/>
		<u:option value="N" titleId="dm.cols.dtlView.prd.notAllow" alt="기간선택허용안함" selected="${envConfigMap.dtlViewOptPrdAllow == 'N'}"/>
		</select></td><td class="width15"></td>
		<td class="bodyip_lt"><strong><u:msg titleId="dm.cols.dtlView.prd" alt="열람기간"/></strong></td>
		<td><select name="dtlViewPrdSelect" >
		<c:forEach var="val" begin="1" end="12" step="1" varStatus="status">
			<option value="${val }">${val}</option>
		</c:forEach>
		</select><select name="dtlViewPrdUnit" >
			<u:option value="unlimited" titleId="dm.cols.dtlView.unlimited" alt="제한없음" selected="${envConfigMap.dtlViewPrdUnit == 'unlimited'}"/>
			<u:option value="day" titleId="dm.cols.dtlView.day" alt="일" selected="${envConfigMap.dtlViewPrdUnit == 'day'}"/>
			<u:option value="week" titleId="dm.cols.dtlView.week" alt="주" selected="${empty envConfigMap.dtlViewPrdUnit || envConfigMap.dtlViewPrdUnit == 'week'}"/>
			<u:option value="month" titleId="dm.cols.dtlView.month" alt="개월" selected="${envConfigMap.dtlViewPrdUnit == 'month'}"/>
		</select></td></tr></table></div>
	</td>
</tr>
</u:listArea>

<%-- <u:blank />

<u:title titleId="dm.cols.subDoc" alt="하위문서" type="small" />
<u:listArea colgroup="15%," noBottomBlank="true">
<tr>
	<td class="head_lt"><u:msg titleId="dm.cols.doc.move" alt="문서이동" /></td>
	<td class="bodybg_lt">
		<u:checkArea><!-- Y:포함, N:미포함 -->
			<u:radio name="moveWithSubYn" value="Y" titleId="dm.cols.withSubYn" inputClass="bodybg_lt" checkValue="${envConfigMap.moveWithSubYn }" />
			<u:radio name="moveWithSubYn" value="N" titleId="dm.cols.not.withSubYn" inputClass="bodybg_lt" checkValue="${envConfigMap.moveWithSubYn }" checked="${empty envConfigMap.moveWithSubYn }"/>
		</u:checkArea>
	</td>
</tr>
<tr>
	<td class="head_lt"><u:msg titleId="dm.cols.doc.copy" alt="문서복사" /></td>
	<td class="bodybg_lt">
		<u:checkArea><!-- Y:포함, N:미포함 -->
			<u:radio name="copyWithSubYn" value="Y" titleId="dm.cols.withSubYn" inputClass="bodybg_lt" checkValue="${envConfigMap.copyWithSubYn }" />
			<u:radio name="copyWithSubYn" value="N" titleId="dm.cols.not.withSubYn" inputClass="bodybg_lt" checkValue="${envConfigMap.copyWithSubYn }" checked="${empty envConfigMap.copyWithSubYn }"/>
		</u:checkArea>
	</td>
</tr>
<tr>
	<td class="head_lt"><u:msg titleId="dm.cols.doc.tak" alt="문서인계" /></td>
	<td class="bodybg_lt">
		<u:checkArea><!-- Y:포함, N:미포함 -->
			<u:radio name="takovrWithSubYn" value="Y" titleId="dm.cols.withSubYn" inputClass="bodybg_lt" checkValue="${envConfigMap.takovrWithSubYn }" />
			<u:radio name="takovrWithSubYn" value="N" titleId="dm.cols.not.withSubYn" inputClass="bodybg_lt" checkValue="${envConfigMap.takovrWithSubYn }" checked="${empty envConfigMap.takovrWithSubYn }"/>
		</u:checkArea>
	</td>
</tr>
<tr>
	<td class="head_lt"><u:msg titleId="dm.cols.doc.transfer" alt="문서이관" /></td>
	<td class="bodybg_lt">
		<u:checkArea><!-- Y:포함, N:미포함 -->
			<u:radio name="transferWithSubYn" value="Y" titleId="dm.cols.withSubYn" inputClass="bodybg_lt" checkValue="${envConfigMap.transferWithSubYn }" />
			<u:radio name="transferWithSubYn" value="N" titleId="dm.cols.not.withSubYn" inputClass="bodybg_lt" checkValue="${envConfigMap.transferWithSubYn }" checked="${empty envConfigMap.transferWithSubYn }"/>
		</u:checkArea>
	</td>
</tr>
</u:listArea> --%>

<u:blank />
<% // 하단 버튼 %>
<u:buttonArea>
	<%-- <c:if test="${resetYn eq 'Y' }"><u:button titleId="cm.btn.reset" alt="초기화" href="javascript:storReset();" auth="A" /></c:if>
	<u:button titleId="dm.btn.storMgm" alt="저장소관리" href="javascript:setStorMgmPop();" auth="A" /> --%>
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:save();" auth="A" />
</u:buttonArea>

</form>

