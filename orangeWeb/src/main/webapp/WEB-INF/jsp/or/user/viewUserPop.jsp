<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
$(document).ready(function() {
	dialog.onClose("viewUserDialog", function(){
		if(dialog.isOpen('searchUserDialog')){
			var $search = $("#searchUserDialog");
			var $area = $search.find("#searchUserSchArea");
			if($area.length>0 && $area.css("display")!="none"){
				getIframeContent("searchUserSearchFrm").focusName();
			} else {
				$search.find("#cloasBtn:first").focus();
			}
		}
	});
});
//-->
</script>

<div style="width:700px">

<form id="viewUserInfoForm">
<u:input type="hidden" id="menuId" value="${menuId}" />

<div class="profile_left">
<dl>
	<dd class="photo" id="${orUserBVo.userUid}"><c:if
		test="${empty orUserImgDVo03.imgPath}"
		><a><img id="orImage03" src="${_ctx}/images/${_skin}/photo_noimg.png" data-maxWidth="88" width="88px"/></a></c:if><c:if
		test="${not empty orUserImgDVo03.imgPath}"><c:if
		
			test="${not empty orUserImgDVo03.imgWdth}"><fmt:parseNumber
			var="imgWdth" type="number" value="${orUserImgDVo03.imgWdth}" /><c:if
			test="${imgWdth > 800}"
			><a href="${_ctx}${orUserImgDVo03.imgPath}" target="viewPhotoWin"><img id="orImage03" src="${_ctx}${orUserImgDVo03.imgPath}" width="88px" data-maxWidth="88"/></a></c:if><c:if
			test="${imgWdth <= 800}"
			><a href="javascript:viewImageDetl('${orUserBVo.userUid}','${orUserImgDVo03.userImgTypCd}');"><img id="orImage03" src="${_ctx}${orUserImgDVo03.imgPath}" width="88px" data-maxWidth="88"/></a></c:if></c:if><c:if
			
			test="${empty orUserImgDVo03.imgWdth}"
			><a href="javascript:viewImageDetl('${orUserBVo.userUid}','${orUserImgDVo03.userImgTypCd}');"><img id="orImage03" src="${_ctx}${orUserImgDVo03.imgPath}" width="88px" data-maxWidth="88"/></a></c:if></c:if>
	</dd>
	<c:if test="${sessionScope.userVo.userUid eq orUserBVo.userUid and empty blockPnsPhotoEnable}"
	><dd style="float:right"><u:buttonS alt="사진변경" titleId="or.btn.changePhoto" onclick="setMyImagePop('03');" popYn="Y" /></dd></c:if>
</dl>
</div>

<div class="profile_right">
<div class="inner">
	<u:listArea>
		<tr>
		<td width="21%" class="head_lt"><u:msg titleId="or.cols.name" alt="성명"/></td>
		<td width="29%" class="body_lt"><u:out value="${orUserBVo.rescNm}" /></td><c:if
			test="${not empty showCompArea}">
		<td width="21%" class="head_lt"><u:out value="${compAreaNm}"/></td>
		<td class="body_lt"><u:out value="${compNm}" /></td></c:if><c:if
			test="${empty showCompArea}">
		<td width="21%" class="head_lt"><u:msg titleId="cols.dept" alt="부서"/></td>
		<td class="body_lt"><u:out value="${orUserBVo.deptRescNm}" /></td></c:if>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.ein" alt="사원번호"/></td>
		<td class="body_lt"><c:if
			test="${not empty blockEinEnable}"><c:if
				test="${not empty orOdurBVo.ein}">******</c:if></c:if><c:if
			test="${empty blockEinEnable}"><u:out value="${orOdurBVo.ein}" /></c:if></td><c:if
			test="${not empty showCompArea}">
		<td width="21%" class="head_lt"><u:msg titleId="cols.dept" alt="부서"/></td>
		<td class="body_lt"><u:out value="${orUserBVo.deptRescNm}" /></td></c:if><c:if
			test="${empty showCompArea}">
		<td class="head_lt"><u:msg titleId="or.cols.statCd" alt="상태코드" /></td>
		<td class="body_lt"><u:out value="${orUserBVo.userStatNm}" /></td></c:if>
		</tr>
		
		<tr>
		<td class="head_lt"><u:term termId="or.term.posit" alt="직위" /></td>
		<td class="body_lt"><u:out value="${orUserBVo.positNm}" /></td>
		<td class="head_lt"><u:term termId="or.term.title" alt="직책"/></td>
		<td class="body_lt"><u:out value="${orUserBVo.titleNm}" /></td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.mbno" alt="휴대전화번호"/></td>
		<td class="body_lt"><u:phone id="mbno" value="${orUserPinfoDVo.mbno}" titleId="cols.mbno" type="view" /></td>
		<td class="head_lt"><u:msg titleId="cols.email" alt="이메일"/></td>
		<td class="body_lt"><c:if
				test="${sessionScope.userVo.hasMnuGrpMdRidOf('MAIL')}"><u:input id="email" value="${orUserPinfoDVo.email}" type="view"
			valueOption="email" maxByte="200" style="width:96%"
			validator="checkMail(inputTitle, va)" /></c:if><c:if
				test="${not sessionScope.userVo.hasMnuGrpMdRidOf('MAIL')}"><u:out value="${orUserPinfoDVo.email}" /></c:if></td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.compPhon" alt="회사전화번호"/></td>
		<td class="body_lt"><u:phone id="compPhon" value="${orUserPinfoDVo.compPhon}" titleId="cols.compPhon" type="view" /></td>
		<td class="head_lt"><u:msg titleId="cols.compFno" alt="회사팩스번호"/></td>
		<td class="body_lt"><u:phone id="compFno" value="${orUserPinfoDVo.compFno}" titleId="cols.compFno" type="view" /></td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.compAdr" alt="회사주소"/></td>
		<td colspan="3" class="body_lt"><u:address id="comp" alt="회사주소" adrStyle="width:94%" zipNoValue="${orUserPinfoDVo.compZipNo }" adrValue="${orUserPinfoDVo.compAdr }" type="view"/></td>
		</tr>
		<c:if test="${empty showHomeInfoDisable}">
		<tr>
		<td class="head_lt"><u:msg titleId="cols.homePhon" alt="자택전화번호"/></td>
		<td class="body_lt"><u:phone id="homePhon" value="${orUserPinfoDVo.homePhon}" titleId="cols.homePhon" type="view" /></td>
		<td class="head_lt"><u:msg titleId="cols.homeFno" alt="자택팩스번호"/></td>
		<td class="body_lt"><u:phone id="homeFno" value="${orUserPinfoDVo.homeFno}" titleId="cols.homeFno" type="view" /></td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.homeAdr" alt="자택주소"/></td>
		<td class="body_lt" colspan="3"><u:address id="comp" alt="자택주소" adrStyle="width:94%" zipNoValue="${orUserPinfoDVo.homeZipNo }" adrValue="${orUserPinfoDVo.homeAdr }" type="view"/></td>
		</tr></c:if>
		
		<tr>
		<td class="head_lt"><u:msg titleId="or.cols.tich" alt="담당업무"/></td>
		<td class="body_lt" colspan="3"><u:input id="tichCont" value="${orUserBVo.tichCont}" type="view"
			titleId="or.cols.tich" maxByte="250" style="width:96%" /></td>
		</tr>
		
		<c:if test="${not empty showCompArea}">
		<tr>
		<td class="head_lt"><u:msg titleId="or.cols.statCd" alt="상태코드" /></td>
		<td class="body_lt" colspan="3"><u:out value="${orUserBVo.userStatNm}" /></td>
		</tr></c:if>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.hpageUrl" alt="홈페이지URL"/></td>
		<td class="body_lt" colspan="3"><u:input id="hpageUrl" value="${orUserPinfoDVo.hpageUrl}" type="view"
			titleId="or.cols.tich" maxByte="200" style="width:96%"
			valueOption="alpha,number" valueAllowed=".:/?&-_" /></td>
		</tr>
		
		<c:if test="${empty lostPwEnable}">
		<tr>
		<td class="head_lt"><u:msg titleId="cols.extnEmail" alt="외부이메일"/></td>
		<td class="body_lt" colspan="3"><u:input id="extnEmail" value="${orUserPinfoDVo.extnEmail}" type="view"
			valueOption="email" maxByte="200" style="width:96%"
			validator="checkMail(inputTitle, va)" /></td>
		</tr></c:if>
		
	</u:listArea>
</div>
</div>
</form>

<u:buttonArea>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>

