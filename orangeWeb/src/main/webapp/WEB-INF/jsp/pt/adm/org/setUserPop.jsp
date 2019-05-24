<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--
<%//사진 변경 - 팝업 오픈 %>
function setImagePop(userImgTypCd){<%// userImgTypCd : 사용자이미지구분코드 - KEY - 01:도장, 02:싸인, 03:사진%>
	var popTitle = userImgTypCd=='01' ? '<u:msg alt="도장 선택" titleId="or.jsp.setOrg.stampTitle" />' : userImgTypCd=='02' ? '<u:msg alt="서명 선택" titleId="or.jsp.setOrg.signTitle" />' : '<u:msg alt="사진 선택" titleId="or.jsp.setOrg.photoTitle" />';
	dialog.open('setUserImageDialog',popTitle,'./setImagePop.do?menuId=${menuId}&userUid=${param.userUid}&userImgTypCd='+userImgTypCd);
}
<%//저장 버튼%>
function saveUser(){
	if(validator.validate('userPopForm')){
		<%// 서버 전송%>
		var $form = $("#userPopForm");
		$form.attr('method','post');
		$form.attr('action','./transUser.do');
		$form.attr('target','dataframe');
		$form.submit();
	}
}
<%// 역할 - 설정 %>
function setRolesPop(){
	dialog.open('setRoleDialog','<u:term termId="or.term.role" alt="역할"/>','./setRoleCdPop.do?menuId=${menuId}&userUid=${param.userUid}');
}
<%// 역할 - 설정 팝업 %>
function setRoles(){
	var ids=[], nms=[];
	$("#setRoleCdPop input[type='checkbox']:checked").each(function(){
		if($(this).val()!=''){
			ids.push($(this).val());
			nms.push($(this).attr('data-extra'));
		}
	});
	var $form = $("#userPopForm");
	$form.find("#roleNms").html(nms.join(', '));
	$form.find("#roleCds").val(ids.join(','));
	dialog.close('setRoleDialog');
}
<%// 탭 클릭%>
function setUserPopTab(tabNm){
	var $area = $("#setUserPopBtnArea");
	if(tabNm=='agnApnt'){<%// 부재설정 프레임을 호출%>
		var url = "./listApvAgnApntAdmFrm.do?menuId=${menuId}&userUid=${param.userUid}";
		$("#agnApntFrm").attr('src', url);
		displayBtn($area, false, ["setUserPopPwBtn","setUserPopSaveBtn"]);
		displayBtn($area, true, ["setUserPopAddApntBtn"]);
	} else {
		displayBtn($area, true, ["setUserPopPwBtn","setUserPopSaveBtn"]);
		displayBtn($area, false, ["setUserPopAddApntBtn"]);
	}
}
<%// 버튼 보이기 조절 %>
function displayBtn($area, showFlag, arr){
	arr.each(function(index, obj){
		if(showFlag){
			$area.find("#"+obj).show();
		} else {
			$area.find("#"+obj).hide();
		}
	});
}
<%// 부재설정 %>
function setAgnApnt(urlParam){
	dialog.open('setAgnApntDialog','<u:msg alt="대결 지정" titleId="ap.jsp.setApvEnv.agnApntSetting"/>','./setApvAgnApntAdmPop.do?menuId=${menuId}&userUid=${param.userUid}'+(urlParam==null ? '' : urlParam));
}
//-->
</script>

<div style="width:730px;">
<div class="front">
	<form id="changeUserForm" >
	<input type="hidden" name="menuId" value="${menuId}" />
	<div class="front_left">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td class="fronttit"><u:msg alt="같은 부서 사용자" titleId="or.jsp.setUserPop.sameOrgUsers" /></td>
			<td class="width5"></td>
			<td class="frontinput"><select id="userUid" name="userUid" onchange="openUserDetl(this.value)"<u:elemTitle titleId="pt.jsp.setAuthGrp.user" alt="사용자룹" />>
			<c:forEach items="${orUserBVoList}" var="orUserBVo" varStatus="status">
			<u:option value="${orUserBVo.userUid}" title="${orUserBVo.rescNm}" checkValue="${param.userUid}"
			/></c:forEach></select></td>
  		</tr>
		</table>
	</div>
	</form>
</div>

<form id="userPopForm" method="post">
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="userUid" value="${param.userUid}" />

<div class="profile_left">
<dl>
	<dd class="photo" id="${param.userUid}"><c:if
		test="${empty orUserImgDVo03.imgPath}"
		><a><img id="orImage03" src="${_ctx}/images/${_skin}/photo_noimg.png" width="88px" data-maxWidth="88"/></a></c:if><c:if
		test="${not empty orUserImgDVo03.imgPath}"><fmt:parseNumber
			var="imgWdth" type="number" value="${orUserImgDVo03.imgWdth}" /><c:if
			test="${imgWdth > 800}"
			><a href="${_ctx}${orUserImgDVo03.imgPath}" target="viewPhotoWin"><img id="orImage03" src="${_ctx}${orUserImgDVo03.imgPath}" width="88px" data-maxWidth="88"/></a></c:if><c:if
			test="${imgWdth <= 800}"
			><a href="javascript:viewImageDetl('${param.userUid}','${orUserImgDVo03.userImgTypCd}');"><img id="orImage03" src="${_ctx}${orUserImgDVo03.imgPath}" width="88px" data-maxWidth="88"/></a></c:if></c:if>
	</dd>
	<dd style="float:right"><u:buttonS alt="사진변경" titleId="or.btn.changePhoto" onclick="setImagePop('03');" popYn="Y" /></dd>
</dl>
</div>

<div class="profile_right">
<div class="inner">

<u:tabGroup id="userInfoTab" noBottomBlank="true">
	<u:tab id="userInfoTab" alt="기본정보" areaId="userBascTab" titleId="or.jsp.setUserPop.basic" onclick="setUserPopTab('userBasc');" on="true" />
	<u:tab id="userInfoTab" alt="참조정보" areaId="userRefTab" titleId="or.jsp.setUserPop.ref" onclick="setUserPopTab('userRef');" />
	<u:tab id="userInfoTab" alt="대결 정보" areaId="agnApntTab" titleId="ap.jsp.setApvEnv.agnApntInfo" onclick="setUserPopTab('agnApnt');" />
</u:tabGroup>

<u:tabArea outerStyle="height:390px; overflow-x:hidden; overflow-y:auto;" innerStyle="margin:10px;" >
	<u:listArea id="userBascTab" noBottomBlank="true" style="padding-bottom:8px;">
		<tr>
		<td width="21%" class="head_lt"><u:msg titleId="or.cols.name" alt="성명"/></td>
		<td width="29%">
		
		<table cellspacing="0" cellpadding="0" border="0">
		<tr>
		<td id="langTypArea">
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
		<u:convert srcId="${orOdurBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
		<u:set test="${status.first}" var="style" value="" elseValue="display:none;" />
		<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
		<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="or.cols.name" value="${rescVa}" style="width:90px; ${style}"
			maxByte="200" validator="changeLangSelector('userPopForm', id, va)" mandatory="Y" />
		</c:forEach>
		</td>
		<td>
		<c:if test="${fn:length(_langTypCdListByCompId)>1}">
			<select id="langSelector" onchange="changeLangTypCd('userPopForm','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
			</c:forEach>
			</select>
		</c:if>
		<u:input type="hidden" id="rescId" value="${orOdurBVo.rescId}" />
		</td>
		</tr>
		</table>
		
		</td>
		<td width="21%" class="head_lt"><u:msg titleId="cols.dept" alt="부서"/></td>
		<td class="body_lt"><u:out value="${orUserBVo.orgRescNm}" /></td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.ein" alt="사원번호"/></td>
		<td><u:input id="ein" value="${orOdurBVo.ein}" maxByte="20" /></td>
		<td class="head_lt"><u:msg titleId="or.cols.statCd" alt="상태코드" /></td>
		<td>
			<select id="userStatCd" name="userStatCd" <u:elemTitle termId="or.cols.statCd" alt="상태코드" />><c:forEach
				items="${userStatCdList}" var="userStatCdVo" varStatus="statStatus">
				<u:option value="${userStatCdVo.cd}" title="${userStatCdVo.rescNm}" checkValue="${orUserBVo.userStatCd}"
				/></c:forEach>
			</select></td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:term termId="or.term.posit" alt="직위"/></td>
		<td>
			<select id="positCd" name="positCd" <u:elemTitle termId="or.term.posit" alt="직위" />><c:forEach
				items="${positCdList}" var="positCdVo" varStatus="positStatus">
				<u:option value="${positCdVo.cd}" title="${positCdVo.rescNm}" checkValue="${orUserBVo.positCd}"
				/></c:forEach>
				<u:option value="" title="" selected="${empty orUserBVo.positCd}" />
			</select></td>
		<td class="head_lt"><u:term termId="or.term.title" alt="직책"/></td>
		<td>
			<select id="titleCd" name="titleCd" <u:elemTitle termId="or.term.title" alt="직책" />><c:forEach
				items="${titleCdList}" var="titleCdVo" varStatus="titleStatus">
				<u:option value="${titleCdVo.cd}" title="${titleCdVo.rescNm}" checkValue="${orUserBVo.titleCd}"
				/></c:forEach>
				<u:option value="" title="" selected="${empty orUserBVo.titleCd}" />
			</select></td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.mbno" alt="휴대전화번호"/></td>
		<td><u:phone id="mbno" value="${orUserPinfoDVo.mbno}" titleId="cols.mbno" /></td>
		<td class="head_lt"><u:msg titleId="cols.email" alt="이메일"/></td>
		<td><u:input id="email" value="${orUserPinfoDVo.email}" valueOption="email" maxByte="200" validator="checkMail(inputTitle, va)" readonly="${changeEmailEnable ? 'N' : 'Y'}" /></td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.compPhon" alt="회사전화번호"/></td>
		<td><u:phone id="compPhon" value="${orUserPinfoDVo.compPhon}" titleId="cols.compPhon" /></td>
		<td class="head_lt"><u:msg titleId="cols.compFno" alt="회사팩스번호"/></td>
		<td><u:phone id="compFno" value="${orUserPinfoDVo.compFno}" titleId="cols.compFno" /></td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.compAdr" alt="회사주소"/></td>
		<td colspan="3"><u:address id="comp" alt="회사주소" adrStyle="width:94%" zipNoValue="${orUserPinfoDVo.compZipNo }" adrValue="${orUserPinfoDVo.compAdr }" readonly="Y"/>
			</td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.homePhon" alt="자택전화번호"/></td>
		<td><u:phone id="homePhon" value="${orUserPinfoDVo.homePhon}" titleId="cols.homePhon" /></td>
		<td class="head_lt"><u:msg titleId="cols.homeFno" alt="자택팩스번호"/></td>
		<td><u:phone id="homeFno" value="${orUserPinfoDVo.homeFno}" titleId="cols.homeFno" /></td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.homeAdr" alt="자택주소"/></td>
		<td colspan="3"><u:address id="home" alt="자택주소" adrStyle="width:94%" zipNoValue="${orUserPinfoDVo.homeZipNo }" adrValue="${orUserPinfoDVo.homeAdr }" readonly="Y"/></td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="or.cols.tich" alt="담당업무"/></td>
		<td colspan="3"><u:input id="tichCont" value="${orUserBVo.tichCont}" titleId="or.cols.tich" maxByte="250" style="width:96%" /></td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.hpageUrl" alt="홈페이지URL"/></td>
		<td colspan="3"><u:input id="hpageUrl" value="${orUserPinfoDVo.hpageUrl}" titleId="or.cols.tich" maxByte="200" style="width:96%"
			valueOption="alpha,number" valueAllowed=".:/?&-_" /></td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.extnEmail" alt="외부이메일"/></td>
		<td colspan="3"><u:input id="extnEmail" value="${orUserPinfoDVo.extnEmail}" valueOption="email" maxByte="200" style="width:96%"
			validator="checkMail(inputTitle, va)" /></td>
		</tr>
		
	</u:listArea>

	<u:listArea id="userRefTab" noBottomBlank="true" style="display:none; padding-bottom:8px;">
		
		<tr>
		<td width="21%" class="head_lt"><u:term termId="or.term.grade" alt="직급"/></td>
		<td width="29%">
			<select id="gradeCd" name="gradeCd" <u:elemTitle termId="or.term.grade" alt="직급" />><c:forEach
				items="${gradeCdList}" var="gradeCdVo" varStatus="gradeStatus">
				<u:option value="${gradeCdVo.cd}" title="${gradeCdVo.rescNm}" checkValue="${orUserBVo.gradeCd}"
				/></c:forEach>
				<u:option value="" title="" selected="${empty orUserBVo.gradeCd}" />
			</select></td>
		<td width="21%" class="head_lt"><u:term termId="or.term.duty" alt="직무"/></td>
		<td width="29%">
			<select id="dutyCd" name="dutyCd" <u:elemTitle termId="or.term.duty" alt="직무" />><c:forEach
				items="${dutyCdList}" var="dutyCdVo" varStatus="dutyStatus">
				<u:option value="${dutyCdVo.cd}" title="${dutyCdVo.rescNm}" checkValue="${orUserBVo.dutyCd}"
				/></c:forEach>
				<u:option value="" title="" selected="${empty orUserBVo.dutyCd}" />
			</select></td>
		</tr>
		
		<tr>
		<td width="21%" class="head_lt"><u:term termId="or.term.secul" alt="보안등급"/></td>
		<td width="29%">
			<select id="seculCd" name="seculCd" <u:elemTitle termId="or.term.secul" alt="보안등급" />><c:forEach
				items="${seculCdList}" var="seculCdVo" varStatus="seculStatus">
				<u:option value="${seculCdVo.cd}" title="${seculCdVo.rescNm}" checkValue="${orUserBVo.seculCd}"
				/></c:forEach>
				<u:option value="" title="" selected="${empty orUserBVo.seculCd}" />
			</select></td>
		<td class="head_lt"><u:msg titleId="cols.aduTyp" alt="겸직구분"/></td>
		<td class="body_lt"><u:out value="${orUserBVo.aduTypNm}" /></td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:term termId="or.term.role" alt="역할"/></td>
		<td class="body_lt" colspan="3">
			<table cellspacing="0" cellpadding="0" border="0" style="width:100%"><tbody><tr>
			<td id="roleNms"><u:out value="${roleNms}"/></td>
			<td align="right"><input type="hidden" id="roleCds" name="roleCds" value="${roleCds}" /><u:buttonS
				termId="or.term.role" href="javascript:setRolesPop();" alt="역할" popYn="Y" /></td>
			</tr></tbody></table>
		</td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.id" alt="ID"/></td>
		<td><u:input id="lginId" value="${orOdurBVo.lginId}" maxByte="20" /></td>
		<td class="head_lt"><u:msg titleId="cols.rid" alt="참조ID"/></td>
		<td><u:input id="rid" value="${orOdurBVo.rid}" maxByte="30" /></td>
		</tr>
		<c:if
			test="${orUserBVo.aduTypCd=='01'}">
		<tr>
		<td class="head_lt"><u:msg titleId="cols.entraYmd" alt="입사일"/></td>
		<td><u:calendar id="entraYmd" option="{end:'resigYmd'}" value="${orOdurBVo.entraYmd}" /></td>
		<td class="head_lt"><u:msg titleId="cols.resigYmd" alt="퇴사일"/></td>
		<td><u:calendar id="resigYmd" option="{start:'entraYmd'}" value="${orOdurBVo.resigYmd}" /></td>
		</tr></c:if><c:if
			test="${orUserBVo.aduTypCd!='01'}">
		<tr>
		<td class="head_lt"><u:msg titleId="cols.strtYmd" alt="시작일"/></td>
		<td><u:calendar id="strtYmd" option="{end:'endYmd'}" value="${orUserBVo.strtYmd}" /></td>
		<td class="head_lt"><u:msg titleId="cols.endYmd" alt="종료일"/></td>
		<td><u:calendar id="endYmd" option="{start:'strtYmd'}" value="${orUserBVo.endYmd}" /></td>
		</tr></c:if>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.refVa1" alt="참조값1"/></td>
		<td><u:input id="refVa1" value="${orUserBVo.refVa1}" maxByte="400" style="width:91%" /></td><c:if
			
			test="${empty orgSyncEnable}">
		<td class="head_lt"><u:msg titleId="cols.refVa2" alt="참조값2"/></td>
		<td><u:input id="refVa2" value="${orUserBVo.refVa2}" maxByte="400" style="width:91%" /></td></c:if><c:if
			
			test="${not empty orgSyncEnable}">
		<td class="head_lt"><u:msg titleId="or.txt.noSync" alt="동기화 제외 대상"/></td>
		<td class="bodybg_lt"><u:checkArea><u:checkbox value="Y" name="sysUserYn" checked="${orUserBVo.sysUserYn == 'Y'}"
			titleId="pt.jsp.setAuthGrp.excTgt" alt="제외대상" /></u:checkArea></td></c:if>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="or.jsp.setUserPop.lginIp" alt="로그인 IP"/></td>
		<td class="bodybg_lt"><u:checkArea><u:checkbox value="Y" name="lginIpExYn" checked="${odurSecuMap.lginIpExYn == 'Y'}"
			titleId="or.jsp.setUserPop.polcExp" alt="정책 예외" /></u:checkArea></td>
		<td class="head_lt"><u:msg titleId="or.jsp.setUserPop.sesnIp" alt="세션 IP"/></td>
		<td class="bodybg_lt"><u:checkArea><u:checkbox value="Y" name="sesnIpExYn" checked="${odurSecuMap.sesnIpExYn == 'Y'}"
			titleId="or.jsp.setUserPop.polcExp" alt="정책 예외" /></u:checkArea></td>
		</tr>
		
		<c:if test="${not empty mobileEnabled}">
		<tr>
		<td class="head_lt"><u:msg titleId="or.jsp.setUserPop.useMobile" alt="모바일 사용"/></td>
		<td class="bodybg_lt"><u:checkArea><u:checkbox value="N" name="useMobYn" checked="${odurSecuMap.useMobYn == 'N'}"
			titleId="cm.option.notUse" alt="사용안함" /></u:checkArea></td>
		<td class="head_lt"><u:msg titleId="or.jsp.setUserPop.useMsgLgin" alt="메세지 자동 로그인"/></td>
		<td class="bodybg_lt"><u:checkArea><u:checkbox value="N" name="useMsgLginYn" checked="${odurSecuMap.useMsgLginYn == 'N'}"
			titleId="cm.option.notUse" alt="사용안함" /></u:checkArea></td>
		</tr>
		</c:if>
		
		<tr>
		<td class="head_lt"><u:msg titleId="or.txt.notUseMail" alt="메일 사용 안함"/></td>
		<td class="bodybg_lt"><u:checkArea><u:checkbox value="N" name="useMailYn" checked="${odurSecuMap.useMailYn == 'N'}"
			titleId="cm.option.notUse" alt="사용안함" /></u:checkArea></td>
		<td class="head_lt"><u:msg titleId="or.txt.notUseMsgr" alt="메신저 사용 안함"/></td>
		<td class="bodybg_lt"><u:checkArea><u:checkbox value="N" name="useMsgrYn" checked="${odurSecuMap.useMsgrYn == 'N'}"
			titleId="cm.option.notUse" alt="사용안함" /></u:checkArea></td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.signMthd" alt="서명방법"/></td>
		<td>
			<select id="signMthdCd" name="signMthdCd" <u:elemTitle termId="cols.signMthd" alt="보안등급" />><c:forEach
				items="${signMthdCdList}" var="signMthdCd" varStatus="signMthdStatus">
				<u:option value="${signMthdCd.cd}" title="${signMthdCd.rescNm}" checkValue="${not empty orUserBVo.signMthdCd ? orUserBVo.signMthdCd : dftSignMthdCd}"
				/></c:forEach></select></td>
		<td class="head_lt"><u:msg titleId="cols.autoApvLnCd" alt="자동결재선코드"/></td>
		<td class="bodybg_lt"><select id="autoApvLnCd" name="autoApvLnCd" <u:elemTitle titleId="cols.autoApvLnCd" alt="자동결재선코드" />>
				<u:option value="" titleId="cm.option.notUse" /><c:forEach
				items="${autoApvLnCdList}" var="autoApvLnCdVo" varStatus="seculStatus">
				<u:option value="${autoApvLnCdVo.cd}" title="${autoApvLnCdVo.rescNm}" checkValue="${orUserBVo.autoApvLnCd}"
				/></c:forEach>
			</select></td>
		</tr>
		
		<c:if test="${false}">
		<tr>
		<td class="head_lt"><u:msg titleId="cols.ssn" alt="주민등록번호"/></td>
		<td colspan="3"><u:ssn id="ssn" value="${orUserPinfoDVo.ssn}" /></td>
		</tr>
		</c:if>
		
		<tr>
		<td class="head_lt"><u:msg titleId="or.cols.stamp" alt="도장 - 01:도장, 02:싸인, 03:사진"/></td>
		<td class="body_lt" style="padding-bottom:2px;">
			<table cellspacing="0" cellpadding="0" border="0" style="width:100%; height:110px"><tbody><tr>
			<td align="right" id="${param.userUid}"><c:if
		test="${empty orUserImgDVo01.imgPath}"
			><a><img id="orImage01" src="${_ctx}/images/${_skin}/photo_noimg.png" height="110px" data-maxHeight="110"/></a></c:if><c:if
		test="${not empty orUserImgDVo01.imgPath}"
			><a href="javascript:viewImageDetl('${param.userUid}','${orUserImgDVo01.userImgTypCd}');"><img id="orImage01" src="${_ctx}${orUserImgDVo01.imgPath}"
				height="40px" data-maxHeight="110"/></a></c:if></td>
			<td align="right" valign="bottom"><u:buttonS alt="도장변경" titleId="or.btn.changeStamp" onclick="setImagePop('01');" popYn="Y" /></td>
			</tr></tbody></table>
		</td>
		
		<td class="head_lt"><u:msg titleId="or.cols.sign" alt="서명 - 01:도장, 02:싸인, 03:사진"/></td>
		<td class="body_lt" style="padding-bottom:2px;">
			<table cellspacing="0" cellpadding="0" border="0" style="width:100%; height:110px"><tbody><tr>
			<td align="right" id="${param.userUid}"><c:if
		test="${empty orUserImgDVo02.imgPath}"
			><a><img id="orImage02" src="${_ctx}/images/${_skin}/photo_noimg.png" height="110px" data-maxHeight="110"/></a></c:if><c:if
		test="${not empty orUserImgDVo02.imgPath}"
			><a href="javascript:viewImageDetl('${param.userUid}','${orUserImgDVo02.userImgTypCd}');"><img id="orImage02" src="${_ctx}${orUserImgDVo02.imgPath}"
				height="40px" data-maxHeight="110"/></a></c:if></td>
			<td align="right" valign="bottom"><u:buttonS alt="서명변경" titleId="or.btn.changeSign" onclick="setImagePop('02');" popYn="Y" /></td>
			</tr></tbody></table>
		</td>
		</tr>
		
	</u:listArea>
	
	<div id="agnApntTab" style="display:none">
	<iframe id="agnApntFrm" name="agnApntFrm" src="/cm/util/reloadable.do" style="width:100%; height:370px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
	</div>

</u:tabArea>

</div>
</div>
</form>

<u:buttonArea id="setUserPopBtnArea">
	<u:button href="javascript:popPw('${param.userUid}');" id="setUserPopPwBtn" alt="비밀번호 변경" titleId="pt.jsp.setPw.title" auth="A" />
	<u:button href="javascript:saveUser();" id="setUserPopSaveBtn" alt="저장" titleId="cm.btn.save" auth="A" />
	<u:button titleId="cm.btn.add" id="setUserPopAddApntBtn" alt="추가" href="javascript:setAgnApnt();" style="display:none" auth="A" />
	<u:button onclick="dialog.close(this);" alt="닫기" titleId="cm.btn.close" />
</u:buttonArea>
</div>