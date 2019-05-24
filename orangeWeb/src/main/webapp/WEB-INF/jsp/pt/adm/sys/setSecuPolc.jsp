<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	// 디폴트 IP 체크 Http Header - IpChecker.java
%>
<script type="text/javascript">
<!--
function doSubmit(){
	if(validator.validate('secuPolcArea')){
		var $form = $('#secuPolcArea');
		var i, j, $input, ipIds = ["lginIpRange","sessionIpExcp"];
		
		for(i=0;i<ipIds.length;i++){
			for(j=1;;j++){
				$input = $form.find("#"+ipIds[i]+j);
				if($input.length==0) break;
				if(!checkIp($input.val())){
					$input.focus();
					return;
				}
			}
		}
		
		$form.attr("action","./transSecuPolc.do");
		$form.attr("target","dataframe");
		$form.submit();
	}
}<%
// 입력한 IP 검증 %>
function checkIp(va){
	if(va.trim()=='') return true;
	var pattern = /[^(0-9)]/gi;
	var i, j, k, no, nos, ips, ranges = va.split(',');
	for(i=0;i<ranges.length;i++){
		ips = ranges[i].trim().split('-');
		if(ips.length>2){
			alert('<u:msg titleId="pt.jsp.setIpPloc.msg.range" alt="범위 설정이 잘못 되었습니다." />');
			return false;
		} else if(ips.length==2){
			var ip1 = ips[0].trim();
			var ip2 = ips[1].trim();
			if(ip1=='' || ip2==''){
				alert('<u:msg titleId="pt.jsp.setIpPloc.msg.range" alt="범위 설정이 잘못 되었습니다." />');
				return false;
			}
			var p = ip1.lastIndexOf('.');
			if(p>0 && !ip2.startsWith(ip1.substring(0,p+1))){
				alert('<u:msg titleId="pt.jsp.setIpPloc.msg.subnet" alt="범위는 마지막 IP 숫자만 지정 가능 합니다." />');
				return false;
			}
		}
		for(j=0;j<ips.length;j++){
			nos = ips[j].trim().split('.');
			if(nos.length != 4){
				alert('<u:msg titleId="pt.jsp.setIpPloc.msg.4number" alt="4개의 숫자로 지정해야 합니다." />');
				return false;
			}
			for(k=0;k<nos.length;k++){
				no = nos[k].trim();
				if(no=='*'){
					if(k!=3){
						alert('<u:msg titleId="pt.jsp.setIpPloc.msg.ast" alt="* 는 마지막 자리에만 사용 가능 합니다." />');
						return false;
					}
					continue;
				}
				if(pattern.test(no)){
					alert('<u:msg titleId="pt.jsp.setIpPloc.msg.numberType" alt="숫자 형태가 잘못 되었습니다." />');
					return false;
				}
				if(parseInt(no,10)>255){
					alert('<u:msg titleId="pt.jsp.setIpPloc.msg.255" alt="255보다 큰 수를 지정 할 수 없습니다." />');
					return false;
				}
			}
		}
	}
	return true;
}<%
// 해더 조회%>
function viewHeader(){
	dialog.open('viewHeaderDialog','<u:msg titleId="pt.jsp.setIpPloc.btnHeader" alt="HTTP 해더 조회" />','./viewHeaderPop.do?menuId=${menuId}');
}<%
// 해더명 조회%>
function viewHeaderName(){
	dialog.open('viewHeaderNameDialog','<u:msg titleId="pt.jsp.setIpPloc.btnHeaderName" alt="HTTP 해더명 조회" />','./viewProxyPop.do?menuId=${menuId}');
}<%
// 복사 방지%>
function openAntiCopy(){
	dialog.open('setAntiCopyDialog','<u:msg titleId="pt.btn.antiCopy" alt="복사 방지" />','./setAntiCopyPop.do?menuId=${menuId}');
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>

<u:title alt="외부 로그인 설정" menuNameFirst="true" />

<form id="secuPolcArea">
<input type="hidden" name="menuId" value="${menuId}" />
<u:listArea>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.jsp.setIpPloc.lginIpPolc" alt="IP 로그인 정책" /></td>
		<td class="bodybg_lt">
			<u:checkArea>
			<u:radio name="pt.secuPolc.lginIpPolc" value="N" titleId="cm.option.notUse" checked="${secuPolc.lginIpPolc != 'internalIpOnlyPloc' and secuPolc.lginIpPolc != 'authByIpRangePloc'}" />
			<u:radio name="pt.secuPolc.lginIpPolc" value="internalIpOnlyPloc" titleId="pt.jsp.setIpPloc.inOnlyIpRange" alt="내부망만 허용" checked="${secuPolc.lginIpPolc == 'internalIpOnlyPloc'}" /><c:if
				test="${sysPlocMap.authByIpRange eq 'Y'}">
			<u:radio name="pt.secuPolc.lginIpPolc" value="authByIpRangePloc" titleId="pt.jsp.setIpPloc.authByIpRange" alt="외부망 별도 권한" checked="${secuPolc.lginIpPolc == 'authByIpRangePloc'}" /></c:if>
			</u:checkArea></td>
	</tr>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.jsp.setIpPloc.lginIpRange" alt="로그인 IP 범위" /></td>
		<td class="bodybg_lt"><u:checkArea>
			<u:radio name="pt.secuPolc.ipRange" value="IN"  alt="내부망" titleId="pt.jsp.setIpPloc.ipRangeIn" checked="${secuPolc.ipRange != 'OUT'}" />
			<u:radio name="pt.secuPolc.ipRange" value="OUT" alt="외부망" titleId="pt.jsp.setIpPloc.ipRangeEx" checked="${secuPolc.ipRange == 'OUT'}" />
			</u:checkArea><c:forEach
				var="lginIpRange" items="${lginIpRangeList}" varStatus="status">
			<u:input id="lginIpRange${status.index+1}" name="pt.secuPolc.lginIpRange${status.index+1}" titleId="pt.jsp.setIpPloc.lginIpRange" style="width:98%"
			value="${lginIpRange}" maxByte="800" valueOption="number" valueAllowed=",-*. " /><br/></c:forEach><c:forEach
				var="i" begin="1" end="3" step="1">
			<u:input id="lginIpRange${i+lginIpRangeSize}" name="pt.secuPolc.lginIpRange${i+lginIpRangeSize}" titleId="pt.jsp.setIpPloc.lginIpRange" style="width:98%"
			value="" maxByte="800" valueOption="number" valueAllowed=",-*. " /><br/></c:forEach>
			<div style="padding:3px 3px 2px 4px;"><u:msg titleId="pt.jsp.setIpPloc.addComma" alt="콤마(,)로 구별하여 여러 IP를 한칸에 입력 가능 합니다." /> ( 10.10.10.1, 10.10.10.2-10.10.10.9, 10.10.11.* )</div></td>
	</tr>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.jsp.setIpPloc.sessionPolc" alt="세션 IP 정책" /></td>
		<td class="bodybg_lt">
			<u:checkArea>
			<u:radio name="pt.secuPolc.chkSesnIp" value="Y" titleId="pt.jsp.setIpPloc.chkSesnIpY" alt="로그인 후 IP 변경 허용 안함" checked="${secuPolc.chkSesnIp != 'N'}" />
			<u:radio name="pt.secuPolc.chkSesnIp" value="N" titleId="pt.jsp.setIpPloc.chkSesnIpN" alt="로그인 후 IP 변경 허용" checked="${secuPolc.chkSesnIp == 'N'}" />
			</u:checkArea></td>
	</tr>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.jsp.setIpPloc.sessionIpExcp" alt="세션 IP 예외" /></td>
		<td class="bodybg_lt"><c:forEach
				var="sessionIpExcp" items="${sessionIpExcpList}" varStatus="status">
			<u:input id="sessionIpExcp${status.index+1}" name="pt.secuPolc.sessionIpExcp${status.index+1}" titleId="pt.jsp.setIpPloc.sessionIpExcp" style="width:98%"
			value="${sessionIpExcp}" maxByte="800" valueOption="number" valueAllowed=",-*. " /><br/></c:forEach><c:forEach
				var="i" begin="1" end="3" step="1">
			<u:input id="sessionIpExcp${i+sessionIpExcpSize}" name="pt.secuPolc.sessionIpExcp${i+sessionIpExcpSize}" titleId="pt.jsp.setIpPloc.sessionIpExcp" style="width:98%"
			value="" maxByte="800" valueOption="number" valueAllowed=",-*. " /></c:forEach>
			<div style="padding:3px 3px 2px 4px;"><u:msg titleId="pt.jsp.setIpPloc.changeable" alt="서로 변경되는 IP를 한칸에 입력 하십시요." /> ( 10.10.10.1, 10.10.10.2-10.10.10.9, 10.10.11.* )</div></td>
	</tr>
	<tr style="${sessionScope.userVo.userUid eq 'U0000001' ? '' : 'display:none;'}">
		<td width="18%" class="head_lt"><u:msg titleId="pt.jsp.setIpPloc.httpHeader" alt="HTTP 해더" /></td>
		<td><u:input id="headers" name="pt.secuPolc.headers" titleId="pt.jsp.setIpPloc.httpHeader" style="width:98%"
			value="${secuPolc.headers}" maxByte="800" valueOption="alpha" valueAllowed=",- " /></td>
	</tr>
</u:listArea>
</form>

<u:buttonArea>
	<u:button titleId="pt.btn.antiCopy" alt="복사 방지" onclick="openAntiCopy()" auth="A" /><c:if
		test="${sessionScope.userVo.userUid eq 'U0000001'}">
	<u:button titleId="pt.jsp.setIpPloc.btnHeaderName" alt="HTTP 해더명 조회" onclick="viewHeaderName()" auth="SYS" />
	<u:button titleId="pt.jsp.setIpPloc.btnHeader" alt="HTTP 해더 조회" onclick="viewHeader()" auth="SYS" /></c:if>
	<u:button titleId="cm.btn.save" alt="저장" onclick="doSubmit()" auth="SYS" />
</u:buttonArea>