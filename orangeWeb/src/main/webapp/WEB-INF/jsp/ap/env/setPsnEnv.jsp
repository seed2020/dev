<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%// 이미지 - 삭제 %>
function delMyImage(userImgTypCd){
	if(confirmMsg('cm.cfrm.delImg')){<%//cm.cfrm.delImg=이미지를 삭제하시겠습니까 ? %>
		callAjax('/or/user/transMyImageDelAjx.do?menuId=${menuId}', {'userImgTypCd':userImgTypCd}, function(data){
			if(data.message!=null){
				alert(data.message);
			}
			if(data.result == "ok"){
				reload();
			}
		});
	}
}
<%// 서명방법 - 저장 %>
function saveSignMthdCd(){
	var $form = $("#signMthdCdForm");
	$form.attr('action','./transPsnSignMthd.do');
	$form.attr('target','dataframe');
	$form.submit();
}
<%// 결재 비밀번호 - 저장 %>
function savApvPw(){
	if(validator.validate('pwForm')){
		var param = new ParamMap().getData('pwForm');
		if(param.get('lginPw') == param.get('apvPw1')){
			alert("<u:msg titleId='pt.jsp.setPsnEnv.pw.sameWithLgin' alt='결재 비밀번호는 로그인 비밀번호와 같게 설정 할 수 없습니다.' javaScriptEscape='true' />");
			$('#apvPw1').focus();
		} else if(param.get('apvPw1') != param.get('apvPw2')){
			alert("<u:msg titleId='pt.jsp.setPsnEnv.pw.notSameTwo' alt='결재 비밀번호와 결재 비밀번호확인이 일치하지 않습니다. ' javaScriptEscape='true' />");
			$('#apvPw1').focus();
		} else {
			callAjax("${_cxPth}/cm/login/createSecuSessionAjx.do", null, function(data){
				var key = new RSAPublicKey(data.e, data.m);
				var data = encrypt(key, JSON.stringify(param.toJSON()));
				var $form = $('#secuForm');
				$form.find('#secu').val(data);
				$form.attr('action','./transPsnApvPw.do');
				$form.attr("target","dataframe");
				$form.submit();
			});
		}
	}
}<%
// 탭 클릭 %>
function clickPsnTab(tabCd){
	if(tabCd=='agnApntTab'){
		if(getIframeContent("agnApntFrm").location.href.indexOf("reloadable.do")>0){
			reloadFrame("agnApntFrm","./setApvAgnApntFrm.do?menuId=${menuId}");
		}
	}
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="ap.jsp.setPsnEnv.title" alt="개인정보" menuNameFirst="true" />

<u:tabGroup id="normEnvTab" noBottomBlank="true">
	<u:tab id="normEnvTab" areaId="psnTabArea" alt="개인 설정" titleId="ap.jsp.setPsnEnv.tab1" onclick="clickPsnTab('psnTab');" on="${empty param.tabCd or param.tabCd == 'psnTab'}" />
	<u:tab id="normEnvTab" areaId="agnApntTabArea" alt="대결 정보" titleId="ap.jsp.setApvEnv.agnApntInfo" onclick="clickPsnTab('agnApntTab');" on="${param.tabCd == 'agnApntTab'}" />
</u:tabGroup>

<u:titleArea outerStyle="overflow-x:hidden; padding-bottom:10px;" innerStyle="padding:0 0 0 10px;" blueTop="true">

<div id="psnTabArea" style="min-height: 350px; ${not (empty param.tabCd or param.tabCd == 'psnTab') ? 'display:none;' : ''}">
	
	<u:listArea style="float:left; width:32.5%; padding:8px 8px 0 0;" noBottomBlank="true">
		<tr>
		<td class="head_ct"><u:msg titleId="or.txt.photo" alt="사진"/></td>
		</tr>

		<tr>
		<td><table class="listtable" border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td class="photo_ct" style="height:150px" id="${orUserBVo.userUid}"><c:if
				test="${empty orUserImgDVo03.imgPath}"
				><a><img id="orImage03" src="${_ctx}/images/${_skin}/photo_noimg.png" height="110px" alt="<u:msg titleId='or.txt.photo' alt='사진'/>" data-maxHeight="150"/></a></c:if><c:if
				test="${not empty orUserImgDVo03.imgPath}"><fmt:parseNumber
					var="imgWdth" type="number" value="${orUserImgDVo03.imgWdth}" /><c:if
					test="${imgWdth > 800}"
					><a href="${_ctx}${orUserImgDVo03.imgPath}" target="viewPhotoWin"><img id="orImage03" src="${_ctx}${orUserImgDVo03.imgPath}" height="${orUserImgDVo03.imgHght}px" alt="<u:msg titleId='or.txt.photo' alt='사진'/>" data-maxHeight="150"/></a></c:if><c:if
					test="${imgWdth <= 800}"
					><a href="javascript:viewImageDetl('${orUserBVo.userUid}','${orUserImgDVo03.userImgTypCd}');"><img id="orImage03" src="${_ctx}${orUserImgDVo03.imgPath}" height="${orUserImgDVo03.imgHght}px" alt="<u:msg titleId='or.txt.photo' alt='사진'/>" data-maxHeight="150"/></a></c:if></c:if>
			</td>
			</tr>

			<tr>
			<td class="photo_btn" style="height:20px;"><c:if
					test="${empty blockPnsPhotoEnable}">
				<u:buttonS titleId="cm.btn.mod" alt="수정" href="javascript:setMyImagePop('03');" auth="W"
				/><u:buttonS titleId="cm.btn.del" alt="삭제" href="javascript:delMyImage('03');" auth="W" /></c:if></td>
			</tr>
			</table></td>
		</tr>
	</u:listArea>

	<u:listArea style="float:left; width:32.5%; padding:8px 8px 0 0;" noBottomBlank="true">
		<tr>
		<td class="head_ct"><u:msg titleId="or.txt.stamp" alt="도장"/></td>
		</tr>

		<tr>
		<td><table class="listtable" border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td class="photo_ct" style="height:150px" id="${orUserBVo.userUid}"><c:if
				test="${empty orUserImgDVo01.imgPath}"
				><a><img id="orImage01" src="${_ctx}/images/${_skin}/photo_noimg.png" height="110px" alt="<u:msg titleId='or.txt.stamp' alt='도장'/>" data-maxHeight="150"/></a></c:if><c:if
				test="${not empty orUserImgDVo01.imgPath}"
				><a href="javascript:viewImageDetl('${orUserBVo.userUid}','${orUserImgDVo01.userImgTypCd}');"><img id="orImage01" src="${_ctx}${orUserImgDVo01.imgPath}" height="${orUserImgDVo01.imgHght}px" alt="<u:msg titleId='or.txt.stamp' alt='도장'/>" data-maxHeight="150"/></a></c:if>
			</td>
			</tr>
			<tr>
			<td class="photo_btn" style="height:20px;"><c:if
					test="${optConfigMap.notAlwChgStmp != 'Y'}">
				<u:buttonS titleId="cm.btn.mod" alt="수정" href="javascript:setMyImagePop('01');" auth="W"
				/><u:buttonS titleId="cm.btn.del" alt="삭제" href="javascript:delMyImage('01');" auth="W" /></c:if></td>
			</tr>
			</table></td>
		</tr>
	</u:listArea>

	<u:listArea style="float:left; width:32.5%; padding:8px 8px 0 0;" noBottomBlank="true">
		<tr>
		<td class="head_ct"><u:msg titleId="or.txt.sign" alt="서명"/></td>
		</tr>

		<tr>
		<td><table class="listtable" border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td class="photo_ct" style="height:150px" id="${orUserBVo.userUid}"><c:if
				test="${empty orUserImgDVo02.imgPath}"
				><a><img id="orImage02" src="${_ctx}/images/${_skin}/photo_noimg.png" height="110px" alt="<u:msg titleId='or.txt.sign' alt='서명'/>" data-maxHeight="150"/></a></c:if><c:if
				test="${not empty orUserImgDVo02.imgPath}"
				><a href="javascript:viewImageDetl('${orUserBVo.userUid}','${orUserImgDVo02.userImgTypCd}');"><img id="orImage02" src="${_ctx}${orUserImgDVo02.imgPath}" height="${orUserImgDVo02.imgHght}px" data-maxHeight="150"/></a></c:if>
			</td>
			</tr>
			<tr>
			<td class="photo_btn" style="height:20px;"><c:if
					test="${optConfigMap.notAlwChgSign != 'Y'}">
				<u:buttonS titleId="cm.btn.mod" alt="수정" href="javascript:setMyImagePop('02');" auth="W"
				/><u:buttonS titleId="cm.btn.del" alt="삭제" href="javascript:delMyImage('02');" auth="W" /></c:if></td>
			</tr>
			</table></td>
		</tr>
	</u:listArea>

	<u:listArea style="float:left; width:32.5%; padding:8px 8px 0 0;" noBottomBlank="true">
		<tr>
		<td class="head_ct"><u:msg titleId="cols.signMthd" alt="서명방법"/></td>
		</tr>

		<tr>
		<td style="padding:5px 0 0 5px;height:95px;"><c:if
				test="${optConfigMap.signAreaSign == 'psn'}"
				><u:set test="${empty orUserBVo.signMthdCd}" var="signAreaSign" value="${deftSignMthdCd}" elseValue="${orUserBVo.signMthdCd}" /></c:if><c:if
				test="${optConfigMap.signAreaSign != 'psn'}"
				><u:set test="${empty optConfigMap.signAreaSign}" var="signAreaSign" value="${deftSignMthdCd}" elseValue="${optConfigMap.signAreaSign}" /></c:if>
			<form id="signMthdCdForm">
			<input type="hidden" name="menuId" value="${menuId}" />
			<table class="listtable" border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td style="height:72px;"><u:checkArea>
				<c:forEach items="${signMthdCdList}" var="signMthdCd" varStatus="status">
				<tr><u:radio name="signMthdCd" value="${signMthdCd.cd}" title="${signMthdCd.rescNm}" inputClass="bodybg_lt" checked="${signAreaSign == signMthdCd.cd}" /></tr>
				</c:forEach>
				</u:checkArea></td>
			</tr>

			<tr>
			<td class="photo_btn" style="height:20px;"><c:if
					test="${optConfigMap.signAreaSign == 'psn'}">
				<u:buttonS titleId="cm.btn.mod" alt="수정" href="javascript:saveSignMthdCd();" auth="W" /></c:if></td>
			</tr>
			</table></form></td>
		</tr>
	</u:listArea>

	<u:listArea style="float:left; width:65.8%; padding:8px 8px 0 0;" noBottomBlank="true">
	<tr>
	<td class="head_ct"><u:msg titleId="pt.jsp.setPw.typeApp" alt="결재 비밀번호"/></td>
	</tr>

	<tr>
	<td style="padding:5px 0 0 5px;height:95px;">
		<table class="listtable" border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td>
			<form id="pwForm">
			<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td class="body_lt"><u:msg titleId="pt.jsp.setPw.typeSys" alt="로그인 비밀번호"/></td>
				<td><u:input id="lginPw" type="password" mandatory="Y" titleId="pt.jsp.setPw.typeSys" /></td>
				<td class="width5"></td>
				<td class="red_stxt"></td>
				</tr>
				<tr>
				<td class="body_lt"><u:msg titleId="pt.jsp.setPw.typeApp" alt="결재 비밀번호"/></td>
				<td><u:input id="apvPw1" type="password" mandatory="Y" minLength="6" titleId="pt.jsp.setPw.typeApp" /></td>
				<td class="width5"></td>
				<td class="red_stxt">(<u:msg titleId="ap.jsp.setPsnEnv.pwGuide" alt="영문 소문자와 숫자를 혼용하세요"/>)</td>
				</tr>
				<tr>
				<td class="body_lt"><u:msg titleId="pt.jsp.setPw.typeApp2" alt="결재 비밀번호 확인"/></td>
				<td><u:input id="apvPw2" type="password" mandatory="Y" minLength="6" titleId="pt.jsp.setPw.typeApp2" /></td>
				<td class="width5"></td>
				<td class="red_stxt"></td>
				</tr>
				</table></form>
				<form id="secuForm"><input type="hidden" name="menuId" value="${menuId}" /><input type="hidden" name="secu" id="secu" value="" /></form>
				</td>
			</tr>
			<tr>
			<td class="photo_btn" style="height:20px;"><c:if
					test="${optConfigMap.notAlwApvPw != 'Y'}">
				<u:buttonS titleId="cm.btn.mod" alt="수정" href="javascript:savApvPw();" auth="W" /></c:if></td>
			</tr>
			</table></td>
		</tr>
	</u:listArea>

</div>

<div id="agnApntTabArea" style="${not(param.tabCd == 'agnApntTab') ? 'display:none;' : ''}">
<iframe id="agnApntFrm" name="agnApntFrm" src="${_cxPth}/cm/util/reloadable.do" style="width:100%; height:402px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</div>

</u:titleArea>


