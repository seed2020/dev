<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

request.setAttribute("roundNos", new String[]{ "0", "0.5", "1" });
request.setAttribute("oneToFive", new String[]{ "1", "2", "3", "4", "5" });
request.setAttribute("zeroToFive", new String[]{ "0","1","2","3","4","5" });
request.setAttribute("zeroToTwenty", new String[]{ "0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20" });
%>
<script type="text/javascript">
<!--<%
// 저장 - 버튼 클릭 %>
function saveConfig(){
	var $form = $("#setConfigForm");
	$form.attr('action','./transAnbPolc.do');
	$form.attr('target','dataframe');
	$form.submit();
}
function checkClick(id, chked){
	if(id=='manlAnbMak'){
		$("#enterBaseAnbMakY").checkInput(!chked);
		$("#yearBaseAnbMakY").checkInput(!chked);
	} else if(id=='enterBaseAnbMak'){
		$("#yearBaseAnbMakY").checkInput(!chked);
		$("#manlAnbMakY").checkInput(!chked);
	} else if(id=='yearBaseAnbMak'){
		$("#enterBaseAnbMakY").checkInput(!chked);
		$("#manlAnbMakY").checkInput(!chked);
	}
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<u:title alt="환경설정" menuNameFirst="true" />

<form id="setConfigForm" method="post">
<input type="hidden" name="menuId" value="${menuId}" />

<u:title titleId="wd.cfg.anbMake" alt="연차 생성" type="small" />
<u:listArea>
	
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="wd.cfg.manlAnbMak" alt="연차 수동 생성"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="manlAnbMak" titleId="cm.option.use" alt="사용"
		 checked="${sysPlocMap.manlAnbMak eq 'Y'}" onclick="checkClick(this.name, this.checked)" />
		</tr>
		</table>
	</td>
	</tr>
	<%--
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="wd.cfg.enterBaseAnbMak" alt="연차 자동 생성 (입사일 기준)"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="enterBaseAnbMak" titleId="cm.option.use" alt="사용"
		 checked="${sysPlocMap.enterBaseAnbMak eq 'Y'}" onclick="checkClick(this.name, this.checked)" />
		</tr>
		</table>
	</td>
	</tr>
	--%>
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="wd.cfg.yearBaseAnbMak" alt="연차 자동 생성 (회계일 기준)"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="yearBaseAnbMak" titleId="cm.option.use" alt="사용"
		 checked="${sysPlocMap.yearBaseAnbMak eq 'Y'}" onclick="checkClick(this.name, this.checked)" />
		</tr>
		</table><%-- <table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="width2"></td>
		<td><u:msg titleId="ap.cfg.recoDt" alt="년도 기준일"/></td>
		<td class="width5"></td>
		<td><select id="newYearMonth" name="newYearMonth">
			<c:forEach items="${months}" var="month" varStatus="status">
			<u:option value="${month}" titleId="cm.m.${month}" selected="${sysPlocMap.newYearMonth eq month}"
			/></c:forEach>
			</select></td>
		<td class="width5"></td>
		<td><select id="newYearDay" name="newYearDay">
			<c:forEach items="${days}" var="day" varStatus="status">
			<u:option value="${day}" titleId="cm.d.${day}" selected="${sysPlocMap.newYearDay eq day}"
			/></c:forEach>
			</select></td>
		</tr>
		</table> --%><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="notMakeLessThen1Y" titleId="wd.cfg.notMakeLessThen1Y" alt="1년 미만 잔여 연차 생성 안함" checked="${sysPlocMap.notMakeLessThen1Y eq 'Y'}" />
		</tr>
		</table><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="width2"></td>
		<td><u:msg titleId="wd.cfg.lessThen49" alt="잔여 연차 계산 (0.01 ~ 0.49 일)"/></td>
		<td class="width5"></td>
		<td><select id="lessThen49" name="lessThen49">
			<c:forEach items="${roundNos}" var="roundNo" varStatus="status">
			<u:option value="${roundNo}" title="${roundNo}" selected="${sysPlocMap.lessThen49 eq roundNo}"
			/></c:forEach>
			</select></td>
		<td class="width20"></td>
		<td><u:msg titleId="wd.cfg.lessThen99" alt="잔여 연차 계산 (0.50 ~ 0.99 일)"/></td>
		<td class="width5"></td>
		<td><select id="lessThen99" name="lessThen99">
			<c:forEach items="${roundNos}" var="roundNo" varStatus="status">
			<u:option value="${roundNo}" title="${roundNo}" selected="${sysPlocMap.lessThen99 eq roundNo}"
			/></c:forEach>
			</select></td>
		</tr>
		</table></td>
	</tr>
	
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="ap.cfg.recoDt" alt="년도 기준일"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0"><tr>
		<td><select id="newYearMonth" name="newYearMonth">
			<c:forEach items="${months}" var="month" varStatus="status">
			<u:option value="${month}" titleId="cm.m.${month}" selected="${sysPlocMap.newYearMonth eq month}"
			/></c:forEach>
			</select></td>
		<td class="width5"></td>
		<td><select id="newYearDay" name="newYearDay">
			<c:forEach items="${days}" var="day" varStatus="status">
			<u:option value="${day}" titleId="cm.d.${day}" selected="${sysPlocMap.newYearDay eq day}"
			/></c:forEach>
			</select></td>
		</tr></table></td>
	</tr>
	
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="wd.cfg.nanbMake" alt="개정년차 생성"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="enterNextMonth" titleId="wd.cfg.enterNextMonth" alt="입사 익월 생성" checked="${sysPlocMap.enterNextMonth eq 'Y'}" />
		</table></td>
	</tr>
	
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="wd.cfg.repbNextYear" alt="대체 근무 이월 "/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="repbNextYear" titleId="cm.option.use" alt="사용" checked="${sysPlocMap.repbNextYear eq 'Y'}" />
		</table></td>
	</tr>
	
</u:listArea>

<u:title titleId="wd.cfg.use" alt="연차 사용" type="small" />
<u:listArea>
	
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="wd.cfg.nanbUseYears" alt="개정 연차 유지 년 수"/></td>
	<td class="bodybg_lt"><select id="nanbUseYears" name="nanbUseYears">
		<c:forEach items="${zeroToFive}" var="no" varStatus="status">
		<u:option value="${no}" title="${no}" selected="${sysPlocMap.nanbUseYears eq no}"
		/></c:forEach>
		</select></td>
	</tr>
	
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="wd.cfg.anbMinusAllow" alt="연차 당겨쓰기 일 수"/></td>
	<td class="bodybg_lt"><select id="anbMinusAllow" name="anbMinusAllow">
		<c:forEach items="${zeroToTwenty}" var="no" varStatus="status">
		<u:option value="${no}" title="${no}" selected="${sysPlocMap.anbMinusAllow eq no}"
		/></c:forEach>
		</select></td>
	</tr>
	
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="wd.cfg.nanbMinusAllow" alt="개정 연차 당겨쓰기 일 수"/></td>
	<td class="bodybg_lt"><select id="nanbMinusAllow" name="nanbMinusAllow">
		<c:forEach items="${zeroToFive}" var="no" varStatus="status">
		<u:option value="${no}" title="${no}" selected="${sysPlocMap.nanbMinusAllow eq no}"
		/></c:forEach>
		</select></td>
	</tr>
	<!--
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="wd.cfg.mixedUseAllow" alt="연차 혼합 사용"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="mixedUseAllow" titleId="cm.option.use" alt="사용" checked="${sysPlocMap.mixedUseAllow eq 'Y'}" />
		</table></td>
	</tr>
	-->
</u:listArea>

<u:title titleId="wd.cfg.listLog" alt="내역 로그" type="small" />
<u:listArea>
	
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="wd.cfg.logLang" alt="로그 언어"/></td>
	<td class="bodybg_lt"><select id="logLangTypCd" name="logLangTypCd">
		<c:forEach items="${langTypCdList}" var="langTypCd" varStatus="status">
		<u:option value="${langTypCd.cd}" title="${langTypCd.rescNm}" selected="${sysPlocMap.logLangTypCd eq langTypCd}"
		/></c:forEach>
		</select></td>
	</tr>
	
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:saveConfig();" auth="A" />
</u:buttonArea>

</form>