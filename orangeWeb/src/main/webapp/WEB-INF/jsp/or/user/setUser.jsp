<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--
function saveUser(){
	if(validator.validate('setPersonalInfoForm')){
		<%// 서버 전송%>
		var $form = $("#setPersonalInfoForm");
		$form.attr('method','post');
		$form.attr('action','./transUser.do');
		$form.attr('target','dataframe');
		$form.submit();
	}
}

function openUserListFrm(){ reload(); }
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>
<u:title titleId="or.jsp.setOrg.title" alt="조직도 사용자 관리" menuNameFirst="true" />

<form id="setPersonalInfoForm">
<u:input type="hidden" id="menuId" value="${menuId}" />

<div class="profile_left">
<dl>
	<dd class="photo" id="${orUserBVo.userUid}"><c:if
		test="${empty orUserImgDVo03.imgPath}"
		><a><img id="orImage03" src="${_ctx}/images/${_skin}/photo_noimg.png" width="88px"/></a></c:if><c:if
		test="${not empty orUserImgDVo03.imgPath}"><fmt:parseNumber
			var="imgWdth" type="number" value="${orUserImgDVo03.imgWdth}" /><c:if
			test="${imgWdth > 800}"
			><a href="${_ctx}${orUserImgDVo03.imgPath}" target="viewPhotoWin"><img id="orImage03" src="${_ctx}${orUserImgDVo03.imgPath}" width="88px"/></a></c:if><c:if
			test="${imgWdth <= 800}"
			><a href="javascript:viewImageDetl('${orUserBVo.userUid}','${orUserImgDVo03.userImgTypCd}');"><img id="orImage03" src="${_ctx}${orUserImgDVo03.imgPath}" width="88px"/></a></c:if></c:if>
	</dd>
	<c:if test="${sessionScope.userVo.userUid eq orUserBVo.userUid and empty blockPnsPhotoEnable}"
	><dd style="float:right"><u:buttonS alt="사진변경" titleId="or.btn.changePhoto" onclick="setMyImagePop('03');" popYn="Y" /></dd></c:if>
</dl>
</div>

<div class="profile_right">
<div class="inner">

<u:listArea id="persionalBasicTab" >
	<tr>
	<td width="22%" class="head_lt"><u:msg titleId="or.cols.name" alt="성명"/></td>
	<td width="28%" class="body_lt"><u:out value="${orUserBVo.rescNm}" /></td>
	<td width="22%" class="head_lt"><u:msg titleId="cols.dept" alt="부서"/></td>
	<td class="body_lt"><u:out value="${orUserBVo.deptRescNm}" /></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.ein" alt="사원번호"/></td>
	<td class="body_lt"><u:out value="${orOdurBVo.ein}" /></td>
	<td class="head_lt"><u:msg titleId="or.cols.statCd" alt="상태코드" /></td>
	<td class="body_lt"><u:out value="${orOdurBVo.userStatNm}" /></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:term termId="or.term.grade" alt="직급"/></td>
	<td class="body_lt"><u:out value="${orUserBVo.gradeNm}" /></td>
	<td class="head_lt"><u:term termId="or.term.title" alt="직책"/></td>
	<td class="body_lt"><u:out value="${orUserBVo.titleNm}" /></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.mbno" alt="휴대전화번호"/></td>
	<td class="${empty blockPnsInfoEnable ? '' : 'body_lt'}"><u:phone id="mbno" value="${orUserPinfoDVo.mbno}" titleId="cols.mbno" type="${empty blockPnsInfoEnable ? 'text' : 'view'}" /></td>
	<td class="head_lt"><u:msg titleId="cols.email" alt="이메일"/></td>
	<td class="body_lt"><u:input id="email" type="view" value="${orUserPinfoDVo.email}" valueOption="email" maxByte="200" validator="checkMail(inputTitle, va)" readonly="Y" /></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.compPhon" alt="회사전화번호"/></td>
	<td class="${empty blockPnsInfoEnable ? '' : 'body_lt'}"><u:phone id="compPhon" value="${orUserPinfoDVo.compPhon}" titleId="cols.compPhon" type="${empty blockPnsInfoEnable ? 'text' : 'view'}" /></td>
	<td class="head_lt"><u:msg titleId="cols.compFno" alt="회사팩스번호"/></td>
	<td class="${empty blockPnsInfoEnable ? '' : 'body_lt'}"><u:phone id="compFno" value="${orUserPinfoDVo.compFno}" titleId="cols.compFno" type="${empty blockPnsInfoEnable ? 'text' : 'view'}" /></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.compAdr" alt="회사주소"/></td>
	<td colspan="3" class="${empty blockPnsInfoEnable ? '' : 'body_lt'}"><u:address id="comp" alt="회사주소" adrStyle="width:94%" zipNoValue="${orUserPinfoDVo.compZipNo }" adrValue="${orUserPinfoDVo.compAdr }" readonly="Y"
		type="${empty blockPnsInfoEnable ? 'text' : 'view'}" /></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.homePhon" alt="자택전화번호"/></td>
	<td class="${empty blockPnsInfoEnable ? '' : 'body_lt'}"><u:phone id="homePhon" value="${orUserPinfoDVo.homePhon}" titleId="cols.homePhon" type="${empty blockPnsInfoEnable ? 'text' : 'view'}" /></td>
	<td class="head_lt"><u:msg titleId="cols.homeFno" alt="자택팩스번호"/></td>
	<td class="${empty blockPnsInfoEnable ? '' : 'body_lt'}"><u:phone id="homeFno" value="${orUserPinfoDVo.homeFno}" titleId="cols.homeFno" type="${empty blockPnsInfoEnable ? 'text' : 'view'}" /></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.homeAdr" alt="자택주소"/></td>
	<td colspan="3" class="${empty blockPnsInfoEnable ? '' : 'body_lt'}"><u:address id="home" alt="자택주소" adrStyle="width:94%" zipNoValue="${orUserPinfoDVo.homeZipNo }" adrValue="${orUserPinfoDVo.homeAdr }" readonly="Y"
		type="${empty blockPnsInfoEnable ? 'text' : 'view'}"/></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="or.cols.tich" alt="담당업무"/></td>
	<td colspan="3" class="${empty blockPnsInfoEnable ? '' : 'body_lt'}"><u:input id="tichCont" value="${orUserBVo.tichCont}" titleId="or.cols.tich" maxByte="250" style="width:96%"
		type="${empty blockPnsInfoEnable ? 'text' : 'view'}" /></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.hpageUrl" alt="홈페이지URL"/></td>
	<td colspan="3" class="${empty blockPnsInfoEnable ? '' : 'body_lt'}"><u:input id="hpageUrl" value="${orUserPinfoDVo.hpageUrl}" titleId="cols.hpageUrl" maxByte="200" style="width:96%"
		valueOption="alpha,number" valueAllowed=".:/?&-_" type="${empty blockPnsInfoEnable ? 'text' : 'view'}" /></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.extnEmail" alt="외부이메일"/></td>
	<td colspan="3" class="${empty blockPnsInfoEnable ? '' : 'body_lt'}"><u:input id="extnEmail" value="${orUserPinfoDVo.extnEmail}" valueOption="email" maxByte="200" style="width:96%"
		validator="checkMail(inputTitle, va)" type="${empty blockPnsInfoEnable ? 'text' : 'view'}" /></td>
	</tr>
	
	<c:if test="${not empty mobileEnabled}">
	<tr>
	<td class="head_lt"><u:msg titleId="or.jsp.setUserPop.useMobMsgLgin" alt="모바일 메세지 자동 로그인"/></td>
	<td colspan="3" class="bodybg_lt"><u:checkArea><u:checkbox value="N" name="useMsgLginYn" checked="${odurSecuMap.useMsgLginYn == 'N'}"
			titleId="cm.option.notUse" alt="사용안함" /></u:checkArea></td>
	</tr>
	</c:if>
	
	<c:if test="${not empty pcNotiEnable}">
	<tr>
	<td class="head_lt"><u:msg titleId="pt.sysopt.pcNoti" alt="PC 알림 사용"/></td>
	<td colspan="3" class="bodybg_lt"><u:checkArea><c:forEach var="pcNotiMd" items="${pcNotiMds}"><u:convertMap
		attId="${pcNotiMd}" srcId="psnPcNotiMap" var="pcNotiVa" />
		<u:checkbox value="Y" name="pcNoti${pcNotiMd}" checked="${pcNotiVa ne 'N'}"
			titleId="pt.sysopt.pcNoti.${pcNotiMd}" alt="결재/게시" /></c:forEach></u:checkArea></td>
	</tr>
	</c:if>
	
	
	
</u:listArea>

</div>

<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="saveUser();" alt="저장" />
</u:buttonArea>

</div>
</form>
