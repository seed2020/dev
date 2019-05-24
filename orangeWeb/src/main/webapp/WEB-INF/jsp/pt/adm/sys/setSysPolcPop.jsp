<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.Enumeration, java.util.ArrayList" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function saveSysSetup(){
	var $form = $("#sysSetupForm");
	$form.attr('action','./transSysPolc.do');
	$form.attr('target','dataframe');
	$form.submit();
}
function setFuncUsers(funcId){
	var compId = $("#compList").val();
	var userUidArr = [];
	callAjax("./getFuncUserAjx.do?menuId=${menuId}", {compId:compId, funcId:funcId}, function(data){
		if(data.userUids!=null && data.userUids.length>0){
			data.userUids.split(',').each(function(index, va){
				userUidArr.push({userUid:va});
			});
		}
	});
	
	searchUserPop({data:userUidArr, multi:true, mode:'search', compId:compId, titleId:'pt.sysopt.'+funcId}, function(arr){
		var userUids = "";
		if(arr!=null){
			var users = [];
			arr.each(function(index, userVo){
				if(funcId == 'ptPwExceptionUsers'){
					if(!users.contains(userVo.odurUid)){
						users.push(userVo.odurUid);
					}
				} else {
					users.push(userVo.userUid);
				}
			});
			userUids = users.join(',');
		}
		
		callAjax("./transFuncUserAjx.do?menuId=${menuId}", {compId:compId, funcId:funcId, userUids:userUids}, function(data){
			if(data.message!=null) alert(data.message);
		});
	});
}
//-->
</script>
<div style="width:600px; ">

<form id="sysSetupForm">
<input type="hidden" name="menuId" value="${menuId}" />

<u:tabGroup id="sysSetupTab" noBottomBlank="true">
	<u:tab id="sysSetupTab" alt="모듈" titleId="pt.txt.module" areaId="sysSetupModuleArea" on="${true}" />
	<u:tab id="sysSetupTab" alt="조직" titleId="pt.txt.org" areaId="sysSetupOrgArea" />
	<u:tab id="sysSetupTab" alt="회사" titleId="cols.comp" areaId="sysCompArea" />
	<u:tab id="sysSetupTab" alt="포털" titleId="pt.sysopt.PT" areaId="sysSetupPtArea" />
	<u:tab id="sysSetupTab" alt="결재" titleId="ap.term.apv" areaId="sysSetupApArea" />
	<u:tab id="sysSetupTab" alt="기타" titleId="pt.txt.etc" areaId="sysSetupEtcArea" />
	<u:tab id="sysSetupTab" alt="로그인" titleId="pt.login.title" areaId="sysLognArea" />
	<u:tab id="sysSetupTab" alt="사용자" titleId="cols.user" areaId="sysSetupUserArea" />
</u:tabGroup>
<u:tabArea
	outerStyle="height:420px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="height:400px; margin:10px;">

<div id="sysSetupModuleArea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<colgroup>
		<col width="27px">
		<col width="*">
	</colgroup>
	<tbody style="border:0">
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.mailEnable" titleId="pt.mail.enable"
				alt="메일 사용여부" checkValue="${sysPlocMap.mailEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.apvMailEnable" titleId="pt.apvMail.enable"
				alt="승인메일 사용여부" checkValue="${sysPlocMap.apvMailEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.messengerEnable" titleId="pt.messenger.enable"
				alt="메신저 사용여부" checkValue="${sysPlocMap.messengerEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.messengerUcEnable" titleId="pt.messenger.uc.enable"
				alt="UC 엔터프라이즈 메신저 사용 여부 (조직 동기화 다름)" checkValue="${sysPlocMap.messengerUcEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
	</tbody>
	</table>
	<div class="height5"></div>
	
	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<colgroup>
		<col width="27px">
		<col width="*">
	</colgroup>
	<tbody style="border:0">
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.mobileEnable" titleId="pt.mobile.enable"
				alt="모바일 사용여부" checkValue="${sysPlocMap.mobileEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.mobilePushEnable" titleId="pt.mobile.push.enable"
				alt="모바일 메세지 사용여부" checkValue="${sysPlocMap.mobilePushEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.mobileMsnLginEnable" titleId="pt.mobile.msnLgin.enable"
				alt="모바일 메신저 토큰 확인 없이 자동로그인" checkValue="${sysPlocMap.mobileMsnLginEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
	</tbody>
	</table>
	<div class="height5"></div>
	
	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<colgroup>
		<col width="27px">
		<col width="*">
	</colgroup>
	<tbody style="border:0">
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.namoEditorEnable" titleId="pt.namo.enable"
				alt="나모에디터 사용 여부" checkValue="${sysPlocMap.namoEditorEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.docViewerWebEnable" titleId="pt.docViewerWeb.enable"
				alt="문서뷰어 사용 여부(웹)" checkValue="${sysPlocMap.docViewerWebEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.docViewerMobEnable" titleId="pt.docViewerMob.enable"
				alt="문서뷰어 사용 여부(모바일)" checkValue="${sysPlocMap.docViewerMobEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
	</tbody>
	</table>
	<div class="height5"></div>
</div>

<div id="sysSetupOrgArea" style="display:none">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<colgroup>
		<col width="27px">
		<col width="*">
	</colgroup>
	<tbody style="border:0">
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.showCompNmEnable" titleId="pt.showCompNm.enable"
				alt="회사명 표시 (사용자 상세 정보)" checkValue="${sysPlocMap.showCompNmEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.showInstNmEnable" titleId="pt.showInstNm.enable"
				alt="기관명 표시 (사용자 상세 정보)" checkValue="${sysPlocMap.showInstNmEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.showParentDeptEnable" titleId="pt.showParentDept.enable"
				alt="상위부서 표시(사용자 상세 정보)" checkValue="${sysPlocMap.showParentDeptEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
	</tbody>
	</table>
	<div class="height5"></div>
	
	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<colgroup>
		<col width="27px">
		<col width="*">
	</colgroup>
	<tbody style="border:0">
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.showHomeInfoDisable" titleId="pt.showHomeInfo.disable"
				alt="자택 정보 표시 안함 (사용자 상세 정보)" checkValue="${sysPlocMap.showHomeInfoDisable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.blockEinEnable" titleId="pt.blockEin.enable"
				alt="사번 표시 안함" checkValue="${sysPlocMap.blockEinEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.blockPnsInfoEnable" titleId="pt.blockPnsInfo.enable"
				alt="개인정보 수정 안함" checkValue="${sysPlocMap.blockPnsInfoEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.blockPnsPhotoEnable" titleId="pt.blockPnsPhoto.enable"
				alt="사진 수정 안함" checkValue="${sysPlocMap.blockPnsPhotoEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.changeEmailEnable" titleId="pt.changeEmail.enable"
				alt="이메일 수정 가능 (사용자 관리)" checkValue="${sysPlocMap.changeEmailEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.exPhoneEnable" titleId="pt.exPhone.enable"
				alt="확장된 전화번호 사용" checkValue="${sysPlocMap.exPhoneEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
	</tbody>
	</table>
	<div class="height5"></div>
	
	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<colgroup>
		<col width="27px">
		<col width="*">
	</colgroup>
	<tbody style="border:0">
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.dispDeptAddrEnable" titleId="pt.dispDeptAddr.enable"
				alt="조직도 부서 주소 표시" checkValue="${sysPlocMap.dispDeptAddrEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.dispAllUsersEnable" titleId="pt.dispAllUsers.enable"
				alt="조직도 전체 사용자 기본 표시" checkValue="${sysPlocMap.dispAllUsersEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.orgDbSyncEnable" titleId="pt.orgDbSync.enable"
				alt="조직도 DB 동기화 사용" checkValue="${sysPlocMap.orgDbSyncEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.orgMailSyncLogEnable" titleId="pt.debug.orgMailSyncLogEnable"
				alt="조직도 동기화 로그(메일)" checkValue="${sysPlocMap.orgMailSyncLogEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
	</tbody>
	</table>
	<div class="height5"></div>
</div>

<div id="sysLognArea" style="display:none">
	
	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<colgroup>
		<col width="27px">
		<col width="*">
	</colgroup>
	<tbody style="border:0">
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.einLginEnable" titleId="pt.einLgin.enable"
				alt="사번 로그인" checkValue="${sysPlocMap.einLginEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.blockDupLogin" titleId="pt.block.dupLogin"
				alt="동일 계정 로그인 차단" checkValue="${sysPlocMap.blockDupLogin}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.lostPwEnable" titleId="pt.jsp.lostPw"
				alt="비밀번호 찾기" checkValue="${sysPlocMap.lostPwEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.initPwEnable" titleId="pt.sysopt.setInitPw"
				alt="사용자 추가 시 비밀번호 설정" checkValue="${sysPlocMap.initPwEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.chgToOldPwEnable" titleId="pt.chgToOldPw.enable"
				alt="예전 암호 사용 허용" checkValue="${sysPlocMap.chgToOldPwEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
	</tbody>
	</table>
	<div class="height5"></div>
	
	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<colgroup>
		<col width="27px">
		<col width="*">
	</colgroup>
	<tbody style="border:0">
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.integratedSearchEnable" titleId="pt.integratedSearch.enable"
				alt="통합검색 사용 여부" checkValue="${sysPlocMap.integratedSearchEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.integratedSearchUiEnable" titleId="pt.integratedSearchUi.enable"
				alt="통합검색 UI 사용 여부" checkValue="${sysPlocMap.integratedSearchUiEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'"><u:msg
				titleId="pt.integratedSearchUi.enable" var="titleSearchAdmUi" alt="통합검색UI 사용 여부" /><u:msg
				titleId="pt.top.adm" var="titleAdmin" alt="관리자" />
			<u:checkbox value="Y" name="pt.sysPloc.integratedSearchAdmUiEnable" title="${titleSearchAdmUi} - ${titleAdmin}"
				alt="통합검색 UI 사용 여부 - 관리자" checkValue="${sysPlocMap.integratedSearchAdmUiEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.mailSrchEnable" titleId="pt.mailSrch.enable"
				alt="통합검색 메일 포함" checkValue="${sysPlocMap.mailSrchEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
	</tbody>
	</table>
	<div class="height5"></div>
</div>

<div id="sysCompArea" style="display:none">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<colgroup>
		<col width="27px">
		<col width="*">
	</colgroup>
	<tbody style="border:0">
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.licenseByCompEnable" titleId="pt.licenseByComp.enable"
				alt="회사별 라이센스 사용여부" checkValue="${sysPlocMap.licenseByCompEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.codeByCompEnable" titleId="pt.codeByComp.enable"
				alt="회사별 코드(직위, 직급, 직책, 직무) 사용 여부" checkValue="${sysPlocMap.codeByCompEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.seculByCompEnable" titleId="pt.seculByComp.enable"
				alt="회사별 보안등급코드 사용여부" checkValue="${sysPlocMap.seculByCompEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
	</tbody>
	</table>
	<div class="height5"></div>
	
	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<colgroup>
		<col width="27px">
		<col width="*">
	</colgroup>
	<tbody style="border:0">
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.affiliatesEnable" titleId="pt.affiliates.enable"
				alt="계열사 사용" checkValue="${sysPlocMap.affiliatesEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
	</tbody>
	</table>
	<div class="height5"></div>
	
	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<colgroup>
		<col width="27px">
		<col width="*">
	</colgroup>
	<tbody style="border:0">
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.globalOrgChartEnable" titleId="pt.globalOrgChart.enable"
				alt="타회사 조직도 표시" checkValue="${sysPlocMap.globalOrgChartEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.searchUserOnOtherCompDisable" titleId="pt.searchUserOnOtherComp.disable"
				alt="타회사 임직원 검색 안함" checkValue="${sysPlocMap.searchUserOnOtherCompDisable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.helpdeskAllCompEnable" titleId="pt.helpdeskAllComp.enable"
				alt="헬프데스크 전사 사용여부" checkValue="${sysPlocMap.helpdeskAllCompEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.taskLogAllCompEnable" titleId="pt.taskLogAllComp.enable"
				alt="업무일지 전사 사용여부" checkValue="${sysPlocMap.taskLogAllCompEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
	</tbody>
	</table>
	<div class="height5"></div>
</div>


<div id="sysSetupPtArea" style="display:none">

	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<colgroup>
		<col width="27px">
		<col width="*">
	</colgroup>
	<tbody style="border:0">
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.previewEnable" titleId="pt.print.previewEnable"
				alt="인쇄 미리보기 허용(IE)" checkValue="${sysPlocMap.previewEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.sendRowAddEnable" titleId="pt.sendRowAddEnable.enable"
				alt="에디터 첫줄 삽입 여부(보내기)" checkValue="${sysPlocMap.sendRowAddEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.singleDomainEnable" titleId="pt.singleDomain.enable"
				alt="단일 도메인 사용" checkValue="${sysPlocMap.singleDomainEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.authByIpRange" titleId="pt.jsp.setIpPloc.authByIpRange"
				alt="외부망 별도 권한" checkValue="${sysPlocMap.authByIpRange}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.portForwarding" titleId="pt.sysopt.portForwarding"
				alt="포트 포워딩(리눅스)" checkValue="${sysPlocMap.portForwarding}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.pushMsgLog" titleId="pt.sysopt.pushMsgLog"
				alt="푸쉬 메세지 로그" checkValue="${sysPlocMap.pushMsgLog}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px; position:relative; padding-right:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.mailSendLog" titleId="pt.sysopt.mailSendLog"
				alt="메일 전송 로그" checkValue="${sysPlocMap.mailSendLog}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px; position:relative; padding-right:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.useSSL" titleId="pt.sysopt.useSSL"
				alt="SSL 사용" checkValue="${sysPlocMap.useSSL}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px; position:relative; padding-right:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.useAduStatAuth" titleId="pt.sysopt.useAduStatAuth"
				alt="겸직 상태 권한 설정" checkValue="${sysPlocMap.useAduStatAuth}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px; position:relative; padding-right:4px;" />
		</tr>
	</tbody>
	</table>
	<div class="height5"></div>

	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<colgroup>
		<col width="27px">
		<col width="*">
	</colgroup>
	<tbody style="border:0">
		<tr>
			<u:checkbox value="Y" name="pt.sysPloc.autoCntRefreshEnable" titleId="pt.sysopt.autoCntRefreshEnable"
				alt="자동 건수 Refresh 사용" checkValue="${sysPlocMap.autoCntRefreshEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px; position:relative; padding-right:4px;" >
			<table><tr>
			<u:radio name="pt.sysPloc.refreshMin" value="1"  titleId="pt.sysopt.1m"  alt="1분" checkValue="${sysPlocMap.refreshMin}" textStyle="background-color:transparent" />
			<u:radio name="pt.sysPloc.refreshMin" value="2"  titleId="pt.sysopt.2m"  alt="2분" checkValue="${sysPlocMap.refreshMin}" textStyle="background-color:transparent" />
			<u:radio name="pt.sysPloc.refreshMin" value="5"  titleId="pt.sysopt.5m"  alt="5분" checkValue="${sysPlocMap.refreshMin}" textStyle="background-color:transparent" />
			<u:radio name="pt.sysPloc.refreshMin" value="10" titleId="pt.sysopt.10m" alt="10분" checkValue="${sysPlocMap.refreshMin}" textStyle="background-color:transparent" />
			<u:radio name="pt.sysPloc.refreshMin" value="15" titleId="pt.sysopt.15m" alt="15분" checkValue="${sysPlocMap.refreshMin}" textStyle="background-color:transparent" />
			<u:radio name="pt.sysPloc.refreshMin" value="20" titleId="pt.sysopt.20m" alt="20분" checkValue="${sysPlocMap.refreshMin}" textStyle="background-color:transparent" />
			<u:radio name="pt.sysPloc.refreshMin" value="30" titleId="pt.sysopt.30m" alt="30분" checkValue="${sysPlocMap.refreshMin}" textStyle="background-color:transparent" />
			</tr></table>
			</u:checkbox>
		</tr>
		<tr>
			<u:checkbox value="Y" name="pt.sysPloc.pcNotiEnable" titleId="pt.sysopt.pcNoti"
				alt="PC 알림 사용" checkValue="${sysPlocMap.pcNotiEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px; position:relative; padding-right:4px;" >
			<!--
			<table><tr>
			<u:radio name="pt.sysPloc.pcNotiMin" value="20s"  titleId="pt.sysopt.20s"  alt="20초" checkValue="${sysPlocMap.pcNotiMin}" textStyle="background-color:transparent" />
			<u:radio name="pt.sysPloc.pcNotiMin" value="1"  titleId="pt.sysopt.1m"  alt="1분" checkValue="${sysPlocMap.pcNotiMin}" textStyle="background-color:transparent" />
			<u:radio name="pt.sysPloc.pcNotiMin" value="2"  titleId="pt.sysopt.2m"  alt="2분" checkValue="${sysPlocMap.pcNotiMin}" textStyle="background-color:transparent" />
			<u:radio name="pt.sysPloc.pcNotiMin" value="5"  titleId="pt.sysopt.5m"  alt="5분" checkValue="${sysPlocMap.pcNotiMin}" textStyle="background-color:transparent" />
			<u:radio name="pt.sysPloc.pcNotiMin" value="10" titleId="pt.sysopt.10m" alt="10분" checkValue="${sysPlocMap.pcNotiMin}" textStyle="background-color:transparent" />
			<u:radio name="pt.sysPloc.pcNotiMin" value="15" titleId="pt.sysopt.15m" alt="15분" checkValue="${sysPlocMap.pcNotiMin}" textStyle="background-color:transparent" />
			<u:radio name="pt.sysPloc.pcNotiMin" value="20" titleId="pt.sysopt.20m" alt="20분" checkValue="${sysPlocMap.pcNotiMin}" textStyle="background-color:transparent" />
			<u:radio name="pt.sysPloc.pcNotiMin" value="30" titleId="pt.sysopt.30m" alt="30분" checkValue="${sysPlocMap.pcNotiMin}" textStyle="background-color:transparent" />
			</tr></table>
			-->
			<table><tr><c:forEach var="pcNotiMd" items="${pcNotiMds}"><u:convertMap
				attId="pcNoti${pcNotiMd}" srcId="sysPlocMap" var="pcNotiMdVa" />
			<u:checkbox value="Y" name="pt.sysPloc.pcNoti${pcNotiMd}" titleId="pt.sysopt.pcNoti.${pcNotiMd}"
				alt="결재 / 메일 / 게시" checkValue="${pcNotiMdVa}"
				labelStyle="padding-left:2px; position:relative; padding-right:2px;" />
			</c:forEach>
			</tr></table>
			</u:checkbox>
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.extMailSsn" titleId="pt.sysopt.extMailSsn"
				alt="메일 세션 연장" checkValue="${sysPlocMap.extMailSsn}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px; position:relative; padding-right:4px;" />
		</tr>
	</tbody>
	</table>
	<div class="height5"></div>
	
</div>


<div id="sysSetupApArea" style="display:none">

	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<colgroup>
		<col width="27px">
		<col width="*">
	</colgroup>
	<tbody style="border:0">
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.apTranEnable" titleId="pt.tran.apTranEnable"
				alt="결재 이관 허용(시스템관리자)" checkValue="${sysPlocMap.apTranEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.dmTranEnable" titleId="pt.tran.dmTranEnable"
				alt="문서관리 이관 허용(시스템관리자)" checkValue="${sysPlocMap.dmTranEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
	</tbody>
	</table>
	<div class="height5"></div>

	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<colgroup>
		<col width="27px">
		<col width="*">
	</colgroup>
	<tbody style="border:0">
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.apSysRejtEnable" titleId="pt.apSysRejt.enable"
				alt="결재 시스템 반려" checkValue="${sysPlocMap.apSysRejtEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.apCnclApvEnable" titleId="ap.btn.cancelCmpl"
				alt="완결 취소" checkValue="${sysPlocMap.apCnclApvEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.apSysAdmViewEnable" titleId="pt.apSysAdmView.enable"
				alt="결재 전사 문서 보기(시스템 관리자)" checkValue="${sysPlocMap.apSysAdmViewEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<%-- 기능삭제
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.apEditorAndForm" titleId="pt.sysopt.apEditorAndForm"
				alt="결재 에티터 폼 동시 사용(양식)" checkValue="${sysPlocMap.apEditorAndForm}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		--%>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.apUnRegAdmView" titleId="pt.sysopt.apUnRegAdmView"
				alt="결재 미등록 문서 보기(관리자 완료문서)" checkValue="${sysPlocMap.apUnRegAdmView}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.apAdmAddInfm" titleId="pt.sysopt.apAdmAddInfm"
				alt="결재 관리자 통보추가" checkValue="${sysPlocMap.apAdmAddInfm}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
	</tbody>
	</table>
	<div class="height5"></div>
	
	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<colgroup>
		<col width="27px">
		<col width="*">
	</colgroup>
	<tbody style="border:0">
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.erpEnable" titleId="pt.erp.enable"
				alt="ERP 결재 연계" checkValue="${sysPlocMap.erpEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.erpPwEnable" titleId="pt.erpPw.enable"
				alt="ERP 결재 연계 비밀번호 확인" checkValue="${sysPlocMap.erpPwEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.erpAuthEnable" titleId="pt.erpAuth.enable"
				alt="ERP 결재 연계 권한 제어" checkValue="${sysPlocMap.erpAuthEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
	</tbody>
	</table>
	<div class="height5"></div>
	
	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<colgroup>
		<col width="27px">
		<col width="*">
	</colgroup>
	<tbody style="border:0">
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.apInterface1" titleId="pt.sso.apInterface1"
				alt="결재 추가 연계1 : /ap/interface1.do?erpOnetime=xxx" checkValue="${sysPlocMap.apInterface1}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.apInterface2" titleId="pt.sso.apInterface2"
				alt="결재 추가 연계2 : /ap/interface2.do?erpOnetime=xxx" checkValue="${sysPlocMap.apInterface2}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.apInterface3" titleId="pt.sso.apInterface3"
				alt="결재 추가 연계3 : /ap/interface3.do?erpOnetime=xxx" checkValue="${sysPlocMap.apInterface3}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
	</tbody>
	</table>
	<div class="height5"></div>
</div>

<div id="sysSetupEtcArea" style="display:none">
	
	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<colgroup>
		<col width="27px">
		<col width="*">
	</colgroup>
	<tbody style="border:0">
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.brdSnsEnable" titleId="pt.brdSns.enable"
				alt="게시판 SNS 사용여부" checkValue="${sysPlocMap.brdSnsEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.deptBrdHstYn" titleId="pt.sysopt.deptBrdHstYn"
				alt="부서게시판 이력 포함 조회" checkValue="${sysPlocMap.deptBrdHstYn}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px; position:relative; padding-right:4px;" />
		</tr>
	</tbody>
	</table>
	<div class="height5"></div>
	
	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<colgroup>
		<col width="27px">
		<col width="*">
	</colgroup>
	<tbody style="border:0">
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.dmEnable" titleId="pt.dm.enable"
				alt="문서관리 사용 여부" checkValue="${sysPlocMap.dmEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
	</tbody>
	</table>
	<div class="height5"></div>
	
	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<colgroup>
		<col width="27px">
		<col width="*">
	</colgroup>
	<tbody style="border:0">
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
			<u:checkbox value="Y" name="pt.sysPloc.wfEnable" titleId="pt.sysopt.wfEnable"
				alt="업무관리 사용" checkValue="${sysPlocMap.wfEnable}"
				noSpaceTd="true" inputStyle="padding-left:4px;" labelStyle="padding-left:4px;" />
		</tr>
	</tbody>
	</table>
	<div class="height5"></div>

</div>

<div id="sysSetupUserArea" style="display:none">

	<table class="listtable" border="0" cellpadding="0" cellspacing="1">
	<colgroup>
		<col width="27px">
		<col width="*">
	</colgroup>
	<tbody style="border:0">
		<tr>
			<td colspan="2" align="left" style="padding:2px 2px 2px 2px;">
				<select id="compList" name="_compId"><c:forEach items="${ptCompBVoList}" var="ptCompBVo">
					<option value="${ptCompBVo.compId}">${ptCompBVo.rescNm}</option></c:forEach></select>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="left" style="padding:4px 2px 4px 2px;">
				<u:buttonS titleId="pt.sysopt.frequentRefreshUsers" alt="빈번한 Refresh 사용자" onclick="setFuncUsers('frequentRefreshUsers')" />
				<u:buttonS titleId="pt.sysopt.apBodyViewUsers" alt="결재 본문 보기 사용자" onclick="setFuncUsers('apBodyViewUsers')" />
				<u:buttonS titleId="pt.sysopt.ptPwExceptionUsers" alt="비밀번호 변경 예외" onclick="setFuncUsers('ptPwExceptionUsers')" />
			</td>
		</tr>
	</tbody>
	</table>
	<div class="height5"></div>
</div>
</u:tabArea>
</form>

<u:buttonArea><c:if test="${sessionScope.userVo.userUid == 'U0000001'}">
	<u:button titleId="cm.btn.save" alt="저장" onclick="saveSysSetup()" /></c:if>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>