<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
function mbshWidr(mbshId){
	var $mbshCtId = $("#mbshCtId").val();
	if (confirmMsg("ct.cfrm.mbshWidr")) {
		callAjax('./transMbshDel.do?menuId=${menuId}', {mbshCtId:$mbshCtId, mbshId:mbshId}, function(data){
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.href = './listMngMbsh.do?menuId=${menuId}&ctId='+$mbshCtId;
			}
		});
	}else{
		return;
	}
}

function mbshAuthMod(mbshId){
	var $mbshCtId = $("#mbshCtId").val();
	var $mbshAuth = $("#mbshAuth").val();
	if (confirmMsg("ct.cfrm.mbshAuthMod")) {
		callAjax('./transMbshAuthMod.do?menuId=${menuId}', {mbshCtId:$mbshCtId, mbshId:mbshId, mbshAuth:$mbshAuth,}, function(data){
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.href = './listMngMbsh.do?menuId=${menuId}&ctId='+$mbshCtId;
			}
		});
	}else{
		return;
	}
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<div style="width:700px">

<div class="profile_left">
<dl>
	<dd class="photo" id="${mbshInfoMap.userUid}"><c:if
		test="${empty orUserImgDVo03.imgPath}"
		><a><img id="orImage03" src="${_ctx}/images/${_skin}/photo_noimg.png" data-maxWidth="88" width="88px"/></a></c:if><c:if
		test="${not empty orUserImgDVo03.imgPath}"><fmt:parseNumber
			var="imgWdth" type="number" value="${orUserImgDVo03.imgWdth}" /><c:if
			test="${imgWdth > 800}"
			><a href="${_ctx}${orUserImgDVo03.imgPath}" target="viewPhotoWin"><img id="orImage03" src="${_ctx}${orUserImgDVo03.imgPath}" width="88px" data-maxWidth="88"/></a></c:if><c:if
			test="${imgWdth <= 800}"
			><a href="javascript:viewImageDetl('${mbshInfoMap.userUid}','${orUserImgDVo03.userImgTypCd}');"><img id="orImage03" src="${_ctx}${orUserImgDVo03.imgPath}" width="88px" data-maxWidth="88"/></a></c:if></c:if>
	</dd>
	<c:if test="${sessionScope.userVo.userUid == mbshInfoMap.userUid}"
	><dd style="float:right"><u:buttonS alt="사진변경" titleId="or.btn.changePhoto" onclick="setMyImagePop('03');" popYn="Y" /></dd></c:if>
</dl>
</div>

<div class="profile_right">
<div class="inner">
	<% // 표 %>
	<u:listArea id="quesArea">
		<tr>
		<td width="18%" class="head_lt"><u:msg titleId="cols.nm" alt="이름" /></td>
		<td width="32%" class="body_lt">
			${mbshInfoMap.userNm}
			<input id="mbshCtId" name="mbshCtId" type="hidden" value="${mbshCtId}" />
		</td>
		<td width="18%" class="head_lt"><u:msg titleId="cols.userId" alt="사용자아이디" /></td>
		<td width="32%" class="body_lt">${mbshInfoMap.userUid}</td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.comp" alt="회사" /></td>
		<td class="body_lt">${mbshCompNm}</td>
		<td class="head_lt"><u:msg titleId="cols.dept" alt="부서" /></td>
		<td class="body_lt">${mbshInfoMap.deptRescNm}</td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.posit" alt="직위" /></td>
		<td class="body_lt">${mbshInfoMap.positNm}</td>
		<td class="head_lt"><u:msg titleId="cols.ein" alt="사번" /></td>
		<td class="body_lt">${mbshInfoMap.ein}&nbsp;</td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.compPhon" alt="회사전화" /></td>
		<td class="body_lt">${mbshInfoMap.compPhon}</td>
		<td class="head_lt"><u:msg titleId="cols.compFax" alt="회사FAX" /></td>
		<td class="body_lt">${mbshInfoMap.compFno}</td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.homePhon" alt="자택전화" /></td>
		<td class="body_lt">${mbshInfoMap.homePhon}</td>
		<td class="head_lt"><u:msg titleId="cols.homeFax" alt="자택FAX" /></td>
		<td class="body_lt">${mbshInfoMap.homeFno}</td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.mobPhon" alt="휴대전화" /></td>
		<td colspan="3" class="body_lt">${mbshInfoMap.mbno}</td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.compAdr" alt="회사주소" /></td>
		<td colspan="3" class="body_lt">${mbshInfoMap.compAdr}</td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.homeAdr" alt="자택주소" /></td>
		<td colspan="3" class="body_lt">${mbshInfoMap.homeAdr}</td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.tich" alt="담당업무" /></td>
		<td colspan="3" class="body_lt">${mbshInfoMap.dutyNm}</td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.email" alt="이메일" /></td>
		<td colspan="3" class="body_lt">${mbshInfoMap.email}</td>
		</tr>
	</u:listArea>
</div>
</div>
<% // 작은 버튼 영역 %>
<c:if test="${mbshUserSeculCd != 'M' }">
	<u:listArea>
		<tr>
		<td width="18%" class="head_lt"><u:msg titleId="cols.dftAuth" alt="기본권한" /></td>
		<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			<td><select id="mbshAuth" name="mbshAuth"> 
				<option value="S"><u:msg titleId="ct.cols.mbshLev1" alt="스텝" /></option>
				<option value="R"><u:msg titleId="ct.cols.mbshLev2" alt="정회원" /></option>
				<option value="A"><u:msg titleId="ct.cols.mbshLev3" alt="준회원" /></option>
				</select></td>
			<td><u:buttonS href="" titleId="cm.btn.mod" alt="수정" onclick="javascript:mbshAuthMod('${mbshInfoMap.userUid}')"/></td>
			</tr>
			</tbody></table></td>
		</tr>
		
		<tr>
		<td width="18%" class="head_lt"><u:msg titleId="cols.forcedWidr" alt="임의탈퇴" /></td>
		<td><u:buttonS href="" titleId="ct.btn.widr" alt="탈퇴" onclick="javascript:mbshWidr('${mbshInfoMap.userUid}')"/></td>
		</tr>
	</u:listArea>
</c:if>

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button onclick="dialog.close(this);" titleId="cm.btn.close" alt="닫기" auth="R" />
</u:buttonArea>

</div>
