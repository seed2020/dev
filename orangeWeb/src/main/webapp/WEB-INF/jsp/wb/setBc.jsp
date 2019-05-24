<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set var="frmYn" test="${!empty pageSuffix && pageSuffix == 'Frm' }" value="Y" elseValue="N"/>
<u:set var="agntParam" test="${!empty schBcRegrUid }" value="&schBcRegrUid=${schBcRegrUid }" elseValue=""/>
<u:params var="nonPageParams" excludes="schBcRegrUid,pageNo"/>
<u:params var="addUserParams" excludes="addUserUid"/>
<u:set var="historyPage" test="${!empty param.bcId}" value="./${viewPage }${pageSuffix }.do?${params }" elseValue="./${listPage }${pageSuffix }.do?${paramsForList }${agntParam }"/>
<script type="text/javascript">
<!--
function popOnClose(id){
	dialog.onClose(id, function(){
		$("#bcNm").focus();
	});	
};

//동명이인 > 원본 수정
function fnPwsmUpdate(bcId){
	location.replace('./${setPage}${pageSuffix }.do?menuId=${menuId}&typ=${param.typ}${agntParam}&bcId='+bcId);
};

//동명이인 선택
function fnPwsmSelect(toBcId){
	location.replace('./${setPage}${pageSuffix }.do?menuId=${menuId}&typ=${param.typ}${agntParam}&toBcId='+toBcId);
};

//동명이인 조회 팝업
function fnPwsmSearchPop(bcNm){
	dialog.open('findBcPwsmPop', '<u:msg titleId="wb.jsp.findBcPwsmPop.title" alt="동명이인 조회" />', './findBcPwsmPop.do?menuId=${menuId}&typ=${param.typ}&bcId=${param.bcId}&schCat=pwsmName${agntParam}&schWord='+ encodeURIComponent(bcNm));
	popOnClose('findBcPwsmPop');
};

//동명이인 중복체크
function fnPwsmCheck(){
	if($('#bcNm').val() == ''){
		alertMsg("wb.input.check.require.bcNm",'<u:msg titleId="cols.nm" alt="이름" />');
		return;
	}
	callAjax('./findPwsmCheck.do?menuId=${menuId}', {bcNm:$('#bcNm').val(),schBcRegrUid:'${schBcRegrUid}'}, function(data){
		if(data.message !=null){
			alert(data.message);
		}
		if(data.result == 'popup'){
			fnPwsmSearchPop($("input[name='bcNm']").val());
		}
	});
};
<% //이미지 삭제 %>
function delImg(){
	callAjax('./transImgDelAjx.do?menuId=${menuId}', {bcId:'${param.bcId}'}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if(data.result == 'ok'){
			$('#bcImage').attr("src","${_cxPth}/images/${_skin}/photo_noimg.png");
		}
	});
}

//사진 삭제후 페이지 리로드
function pageReload(){
	$('#bcImage').attr("src","${_cxPth}/images/${_skin}/photo_noimg.png");
	dialog.close('viewImageDialog');
};

//사진보기
function viewBcImageDetl(bcId){
	dialog.open('viewImageDialog', '<u:msg titleId="or.jsp.setOrg.viewImageTitle" alt="이미지 보기" />', './viewImagePop.do?menuId=${menuId}&bcId='+bcId);
}

// 사진 미리보기
function setImageReview( filePath ){
	$('#bcImage').attr("src","./viewImage.do?menuId=${menuId}&fileDir="+filePath);
	$('#photoTempPath').val(filePath);
	dialog.close('setImageDialog');
}

<%//사진 변경 - 팝업 오픈 %>
function setImagePop(){
	parent.dialog.open('setImageDialog','<u:msg titleId="or.jsp.setOrg.photoTitle" alt="사진 선택" />','./setImagePop.do?menuId=${menuId}&bcId=${param.bcId}');
}
<%//사진 변경 - 후처리 %>
function setImage( filePath, width, height){
	var $img = $("#bcImage");
	$img.attr("src", filePath);
	if($img.parent().tagName()=='a'){
		$img.parent().attr("href","javascript:viewBcImageDetl('${param.bcId}');");
	}
	dialog.close('setImageDialog');
}

//지정인 공개시 해당 버튼 컨트롤
function fnApntrAction(flag){
	if(!flag){
		$('.apntUserContainer').each(function(){$(this).hide();});
		$('#apntrUserMsg').hide();
	}else{
		$('.apntUserContainer').each(function(){$(this).show();});
		if($("#apntrList input[id='userUid']").length > 0 ) $('#apntrUserMsg').show();
	}
};

//수정시 지정인목록을 비교하여 수정여부 체크
function fnUserCheck( userVo ){
	<c:forEach var="list" items="${wbBcBVo.wbBcApntrRVoList }" varStatus="status">
		if("${list.bcApntrUid }" == userVo.userUid){
			return 'Y';
		}
	</c:forEach>
	return 'N';
};

//공개여부 체크
function setPublicCdChecked(){
	$('input:radio[name=publTypCd]:input[value="apntPubl"]').attr("checked", true);
	setUniformCSS($('#publTypCdContainer'));
};

//여러명의 사용자 선택
function openMuiltiUser(mode){
	setPublicCdChecked();
	var $view = $("#apntrList"), data = [];<%// data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	$view.find("input[id='userUid']").each(function(){
		data.push({userUid:$(this).val()});
	});
	<%// option : data, multi, titleId %>
	parent.searchUserPop({data:data, multi:true, mode:mode==null ?'search':'view'}, function(arr){
		if(arr!=null){
			var buffer = [];
			arr.each(function(index, userVo){
				buffer.push("<input type='hidden' name='bcApntrUid' id='userUid' value='"+userVo.userUid+"' />\n");
				buffer.push("<input type='hidden' name='userNm' id='rescNm' value='"+userVo.rescNm+"' />\n");
				//buffer.push("<input type='hidden' name='rescId' id='rescId' value='"+userVo.rescId+"' />\n");
				buffer.push("<input type='hidden' name='updateYn' id='updateYn' value='"+fnUserCheck(userVo)+"' />\n");
			});
			$view.html(buffer.join(''));
			setUniformCSS($view[0]);
			var userNms = $view.find("input[id='rescNm']");
			var msg = '<u:msg titleId="wb.jsp.viewBc.apntr" arguments="'+userNms.eq(0).val()+','+(userNms.length-1)+'"/>'
			$('#apntrUserMsg').html(msg);
			$('#apntrUserMsg').show();
		}else{
			$("#apntrList").empty();
			$('#apntrUserMsg').html('');
		}
	});
};

//폴더 팝업
function openFldChoi() {
	dialog.open('findBcFldPop','<u:msg titleId="wb.jsp.findBcFldPop.title" alt="폴더 선택" />','./findBcFldPop.do?menuId=${menuId}${agntParam}');
	popOnClose('findBcFldPop');
};

//친밀도 추가 팝업
function openClnsAdd() {
	dialog.open('setClnsPop','<u:msg titleId="wb.jsp.setClnsPop.title" alt="친밀도 추가" />','./setClnsPop.do?menuId=${menuId}${agntParam}');
	popOnClose('setClnsPop');
};
<%// 폴더 선택 %>
function fnFldSelect(){
	var tree = getIframeContent('findBcFldFrm').getTreeData();
	if(tree.id == 'ROOT'){
		$('#fldNm').val('');
		$('#fldId').val('');
	}else{
		$('#fldId').val(tree.id);
		$('#fldNm').val(tree.fldNm);
	}
	dialog.close('findBcFldPop');
};

<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
};

<%// tr추가 %>
function moreTr(cls) {
	var td = $('.' + cls).clone();
	td.css('display', '').attr('class', '').find('input[type=text]').attr('value', '');
	$('.' + cls).before(td);
	setUniformCSS();
};

<% // tr삭제%>
function delTr(obj){
	$(obj).parents('tr:first').remove();
	setUniformCSS();
};

<%// 삭제 - 배열에 담긴 목록%>
function fnDelRow(obj , bcCntcSeq){
	$(obj).parents('tr:first').remove();
};

<%// 친밀도 목록 리로드 %>
fnClnsReload = function(dataMap){
	var $clnsId = $('#clnsId');
	$clnsId.find('option').each(function(){
		$(this).remove();
	});
	$clnsId.append('<u:option value="" titleId="cols.clns"/>');
	var data = dataMap.list;
	for(var i=0;i<data.length;i++){
		$clnsId.append('<u:option value="'+data[i].get('clnsId')+'" title="'+data[i].get('clnsNm')+'"/>');
	}
	$clnsId.val(dataMap.clnsId);
	$clnsId.uniform.update();
};

// 전화번호 입력 체크
function fnCntcCheck(){
	var cntcObj = $("#phonListArea input[type='radio']:checked");
	var tObjs = $('#'+$(cntcObj).val() + 'Trs');
	return tObjs.find("input[name='cntcCont']").eq(0).val();
};

<%// 저장 %>
function save(){
	//if(confirmMsg("cm.cfrm.del")) {	}	
	<%// 서버 전송%>
	if (validator.validate('setRegForm')) {
		if($("#setRegForm input[name='publTypCd']:checked").val() != 'apntPubl'){
			$("#apntrList").empty();
			$('#apntrUserMsg').html('');
		}else{
			if($("input[id='userUid']").length == 0 ){
				alert("<u:msg titleId='wb.cfrm.apntr'/>");
				return;
			}
		}
		
		var etcValidator = false;
		if(fnCntcCheck() == '' ){
			etcValidator = true;
		};
		if ( ( etcValidator && confirmMsg("wb.cfrm.setBcCntc") ) || ( !etcValidator && true/*confirmMsg("cm.cfrm.save")*/  ) ) {
			var $form = $('#setRegForm');
			$form.attr('method','post');
			$form.attr('action','./${transPage}${pageSuffix}.do?menuId=${menuId}');
			$form.attr('enctype','multipart/form-data');
			$form.attr('target','dataframe');
			saveFileToForm('${filesId}', $form[0], null);
			//$form[0].submit();	
		}
	}
};
<% // [하단버튼:배열] %>
function getRightBtnList(){
	var $area = $("#rightBtnArea");
	return $area.find('ul')[0].outerHTML;
}

<%// 저장, 삭제시 리로드 %>
function reloadBcFrm(url, dialogId){
	//팝업 닫기
	if(dialogId != undefined && dialogId != null && dialogId !='') {
		if(dialogId == 'all') parent.dialog.closeAll();
		else parent.dialog.close(dialogId);
	}
	if(url != undefined && url != null) location.replace(url);
	else location.replace(location.href);
};
<%// [버튼:]취소 %>
function historyPage(){
	location.href="${historyPage }";
}
<%// [POPUP] 조직 사용자 추가 - 싱글 %>
function addBcSinglePop(){
	var data = [];<% // 팝업 열때 선택될 데이타 %>
	<% // option : data, multi, withSub, titleId %>
	parent.searchUserPop({data:data}, function(userVo){
		if(userVo!=null){
			location.href='./${setPage}${pageSuffix }.do?${addUserParams}&addUserUid='+userVo.userUid;
		}
	});
}
var isTabUniform=false;
<%// 탭 uniform 적용 %>
function setUniform(){
	if(!isTabUniform){
		setUniformCSS($('#addInfoArea'));
		isTabUniform=true;
	}
}
$(document).ready(function() {
	<c:if test="${pageSuffix eq 'Frm'}">parent.applyDocBtn();</c:if>
	setUniformCSS($('#dftInfoArea'));
});
//-->
</script>

<c:if test="${empty pageSuffix}"><u:title titleId="wb.jsp.setBc.${!empty param.bcId ? 'mod' : 'reg'}.title" alt="명함 등록" menuNameFirst="true"/></c:if>
<u:set var="style" test="${pageSuffix eq 'Frm' }" value="style='padding:10px;'" elseValue="style='padding-top:10px;'"/>
<div ${style }>
<form id="setRegForm">
<u:input type="hidden" id="typ" value="${param.typ}" />
<c:if test="${!empty param.bcId}"><u:input type="hidden" id="bcId" name="bcId" value="${param.bcId}" /></c:if>
<c:if test="${!empty param.toBcId}"><u:input type="hidden" id="toBcId" name="toBcId" value="${param.toBcId}" /></c:if>
<c:if test="${!empty schBcRegrUid}"><u:input type="hidden" id="schBcRegrUid" value="${schBcRegrUid }"/></c:if>
<c:if test="${!empty param.addUserUid}"><u:input type="hidden" id="addUserUid" value="${param.addUserUid }"/></c:if>
<u:input type="hidden" id="listPage" value="./${listPage}${pageSuffix }.do?${nonPageParams}${agntParam}" />
<u:input type="hidden" id="viewPage" value="./${viewPage}${pageSuffix }.do?${params}" />

<u:tabGroup id="bcTab" noBottomBlank="">
<u:tab id="bcTab" areaId="dftInfoArea" titleId="wb.jsp.setBc.tab.dftInfo" alt="기본정보" on="true" />
<u:tab id="bcTab" areaId="addInfoArea" titleId="wb.jsp.setBc.tab.addInfo" alt="추가정보" onclick="setUniform();"/>
</u:tabGroup>

<% // LEFT %>
<div class="profile_left">
	<dl>
	<dd class="photo">
		<c:choose>
			<c:when test="${empty wbBcBVo.wbBcImgDVo.imgPath}"><img id="bcImage" src="${_cxPth}/images/${_skin}/photo_noimg.png" width="88px"/></c:when>
			<c:otherwise>
				<fmt:parseNumber var="imgWdth" type="number" value="${wbBcBVo.wbBcImgDVo.imgWdth}" />
				<c:if test="${imgWdth > 800}"	>
					<a href="${_ctx}${wbBcBVo.wbBcImgDVo.imgPath}" target="viewPhotoWin"><img id="bcImage" src="${_cxPth}${wbBcBVo.wbBcImgDVo.imgPath}" width="88px" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"'/></a>
				</c:if>
				<c:if test="${imgWdth <= 800}">
					<a href="javascript:viewBcImageDetl('${param.bcId}');"><img id="bcImage" src="${_cxPth}${wbBcBVo.wbBcImgDVo.imgPath}" width="88px" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"'/></a>
				</c:if>
			</c:otherwise>
		</c:choose>
	</dd>
	<dd class="photo_btn"><u:set var="photoMsgId" test="${empty wbBcBVo.wbBcImgDVo.imgWdth }" value="cm.btn.add" elseValue="cm.btn.chg"/>
	<u:buttonS alt="사진변경" titleId="${photoMsgId }" onclick="setImagePop();" popYn="Y" 
	/><c:if test="${!empty wbBcBVo.wbBcImgDVo.imgWdth }"><u:buttonS id="delImgBtn" alt="삭제" titleId="cm.btn.del" onclick="delImg();" popYn="Y" /></c:if></dd>
	<dd><u:mandatory />88px 110px</dd>
	</dl>
</div>

<% // RIGHT %>
<div class="profile_right">

	<% // 기본정보 %>
	<div id="dftInfoArea" class="inner">
		<div class="listarea">
			<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<c:if test="${listPage ne 'listPubBc' }">
				<tr id="publTypCdContainer">
					<td width="18%" class="head_lt"><u:msg titleId="cols.publYn" alt="공개여부" /></td>
					<td class="bodybg_lt" colspan="3">
						<input type="radio" id="schFldTypYnH" name="publTypCd" value="priv" onclick="fnApntrAction(false);" <c:if test="${empty wbBcBVo.publTypCd || wbBcBVo.publTypCd eq 'priv'}">checked="checked"</c:if> /><label for="schFldTypYnH"><u:msg titleId="cm.option.priv"/></label>
						<input type="radio" id="schFldTypYnA" name="publTypCd" value="allPubl" onclick="fnApntrAction(false);" <c:if test="${wbBcBVo.publTypCd eq 'allPubl'}">checked="checked"</c:if> /><label for="schFldTypYnA"><u:msg titleId="cm.option.allPubl"/></label>
						<input type="radio" id="schFldTypYnO" name="publTypCd" value="deptPubl" onclick="fnApntrAction(false);" <c:if test="${wbBcBVo.publTypCd eq 'deptPubl'}">checked="checked"</c:if> /><label for="schFldTypYnO"><u:msg titleId="cm.option.deptPubl"/></label>
						<input type="radio" id="schFldTypYnP" name="publTypCd" value="apntPubl" onclick="fnApntrAction(true);" <c:if test="${wbBcBVo.publTypCd eq 'apntPubl'}">checked="checked"</c:if> /><label for="schFldTypYnP"><u:buttonS href="javascript:;"  titleId="cm.option.apntPubl" alt="지정인공개" onclick="openMuiltiUser();" /></label>
						<span id="apntrUserMsg" class="color_stxt" style="margin-left:5px;font-weight:bold;display:${!empty wbBcBVo.wbBcApntrRVoList ? '' : 'none' }"><u:msg titleId="wb.jsp.viewBc.apntr" arguments="${wbBcBVo.wbBcApntrRVoList[0].userNm },${fn:length(wbBcBVo.wbBcApntrRVoList) -1}"/></span>
						<span id="apntrList" class="apntUserContainer" style="display:inline;">
							<c:if test="${wbBcBVo.publTypCd eq 'apntPubl' }">
								<c:forEach var="list" items="${wbBcBVo.wbBcApntrRVoList }" varStatus="status">
									<u:input type="hidden" id="userUid" name="bcApntrUid" value="${list.bcApntrUid }"/>
									<u:input type="hidden" id="updateYn" name="updateYn" value="Y"/>
								</c:forEach>
							</c:if>
						</span>
					</td>
				</tr>
				</c:if>
				<c:if test="${listPage eq 'listPubBc' }"><input type="hidden" name="publTypCd" value="allPubl"/></c:if>
				<tr>
					<td width="18%" class="head_lt"><u:msg titleId="wb.cols.fldNm" alt="폴더" /></td>
					<td><u:input type="hidden" id="fldId" value="${wbBcBVo.fldId }"/>
					<u:input id="fldNm" titleId="cols.fld" value="${wbBcBVo.fldId eq 'ROOT' ? '' : wbBcBVo.fldNm}" style="width:55%;" readonly="Y" disabled="Y"/>
					<u:buttonS titleId="wb.btn.fldChoi" alt="폴더 선택" onclick="openFldChoi();" /><span id="fldNm" class="color_stxt" style="margin-left:5px;font-weight:bold;"></span>
					</td>
				</tr>
			</table>
			<u:blank />
			<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<tr>
					<td width="18%" class="head_lt"><u:mandatory /><u:msg titleId="cols.nm" alt="이름" /></td>
					<td width="32%">
						<u:input id="bcNm" titleId="cols.nm" value="${wbBcBVo.bcNm}" style="${style}" maxByte="30" mandatory="Y" />
						<u:buttonS href="javascript:;" titleId="cm.btn.pwsmRead" alt="동명이인조회" onclick="fnPwsmCheck();"/>
					</td>
					<td width="18%" class="head_lt"><u:term termId="wb.cols.enNm" alt="영문이름" /></td>
					<td width="32%"><u:input id="bcEnNm" titleId="wb.cols.enNm" value="${wbBcBVo.bcEnNm}" style="${style}" maxByte="30" /></td>
				</tr>
			</table>
			<u:blank />
			<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<tbody id="phonListArea">
					<tr id="compPhonTrs">
						<td width="9%" rowspan="3" class="head_lt"><u:msg titleId="cols.phon" alt="전화번호" /></td>
						<td width="9%" class="head_rd">
							<table border="0" cellpadding="0" cellspacing="0">
								<tr>
									<u:radio name="dftCntcTypCd" value="compPhon" titleId="cols.comp" alt="회사" checked="${empty wbBcBVo.dftCntcTypCd}" checkValue="${wbBcBVo.dftCntcTypCd }"
									inputClass="head_rd" textClass="head_lt" noSpaceTd="true" />
								</tr>
							</table>
						</td>
						<td>
							<table border="0" cellpadding="0" cellspacing="0">
								<c:forEach var="list" items="${wbBcBVo.wbBcCntcDVo }" varStatus="status">
									<c:if test="${list.cntcClsCd eq 'CNTC' && list.cntcTypCd eq 'compPhon' }">
										<tr>
											<td>
												<u:input type="hidden" id="cntcTypCd" name="cntcTypCd" value="${empty list.cntcTypCd ? 'compPhon' : list.cntcTypCd}" />
												<u:input type="hidden" id="bcCntcSeq" name="bcCntcSeq" value="${list.bcCntcSeq }" />
												<u:input type="hidden" id="cntcClsCd" name="cntcClsCd" value="${empty list.cntcClsCd ? 'CNTC' : list.cntcClsCd}" />
												<u:input id="compPhon" name="cntcCont" value="${list.cntcCont}" titleId="cols.compPhon" maxByte="120" /><!-- maxLength="12" minLength="10" valueOption="number" validator="checkPhone(inputTitle, va)" onblur="fnPhoneInput(this);" onfocus="fnPhoneUnInput(this);" -->
											</td>
											<td>
												<c:choose>
													<c:when test="${list.bcCntcSeq > 1 }"><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="fnDelRow(this,'${list.bcCntcSeq }');" /></c:when>
													<c:otherwise><u:buttonS titleId="wb.btn.more" alt="more" onclick="moreTr('compPhonTr');" /></c:otherwise>
												</c:choose>
											</td>
										</tr>
									</c:if>
								</c:forEach>
								<tr class="compPhonTr" style="display: none;">
									<td>
										<u:input type="hidden" id="cntcTypCd" name="cntcTypCd" value="compPhon" />
										<u:input type="hidden" id="bcCntcSeq" name="bcCntcSeq" value="" />
										<u:input type="hidden" id="cntcClsCd" name="cntcClsCd" value="CNTC" />
										<u:input id="compPhon" name="cntcCont" value="" titleId="cols.compPhon" maxByte="120" /><!-- valueOption="number" validator="checkPhone(inputTitle, va)" onblur="fnPhoneInput(this);" onfocus="fnPhoneUnInput(this);" -->
									</td>
									<td><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="delTr(this);" /></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr id="homePhonTrs">
						<td class="head_rd">
							<table border="0" cellpadding="0" cellspacing="0">
								<tr>
									<u:radio name="dftCntcTypCd" value="homePhon" titleId="cols.home" alt="자택" checkValue="${wbBcBVo.dftCntcTypCd }"
									inputClass="head_rd" textClass="head_lt" noSpaceTd="true" />
								</tr>
							</table>
						</td>
						<td>
							<table border="0" cellpadding="0" cellspacing="0">
								<c:forEach var="list" items="${wbBcBVo.wbBcCntcDVo }" varStatus="status">
									<c:if test="${list.cntcClsCd eq 'CNTC' && list.cntcTypCd eq 'homePhon' }">
										<tr>
											<td>
												<u:input type="hidden" id="cntcTypCd" name="cntcTypCd" value="${empty list.cntcTypCd ? 'homePhon' : list.cntcTypCd}" />
												<u:input type="hidden" id="bcCntcSeq" name="bcCntcSeq" value="${list.bcCntcSeq }" />
												<u:input type="hidden" id="cntcClsCd" name="cntcClsCd" value="${empty list.cntcClsCd ? 'CNTC' : list.cntcClsCd}" />
												<u:input id="homePhon" name="cntcCont" value="${list.cntcCont}" titleId="cols.homePhon" maxByte="120" /><!-- valueOption="number" validator="checkPhone(inputTitle, va)" onblur="fnPhoneInput(this);" onfocus="fnPhoneUnInput(this);" -->
											</td>
											<td>
												<c:choose>
													<c:when test="${list.bcCntcSeq > 1 }"><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="fnDelRow(this,'${list.bcCntcSeq }');" /></c:when>
													<c:otherwise><u:buttonS titleId="wb.btn.more" alt="more" onclick="moreTr('homePhonTr');" /></c:otherwise>
												</c:choose>
											</td>
										</tr>
									</c:if>
								</c:forEach>
								<tr class="homePhonTr" style="display: none;">
									<td>
										<u:input type="hidden" id="cntcTypCd" name="cntcTypCd" value="homePhon" />
										<u:input type="hidden" id="bcCntcSeq" name="bcCntcSeq" value="" />
										<u:input type="hidden" id="cntcClsCd" name="cntcClsCd" value="CNTC" />
										<u:input id="homePhon" name="cntcCont" value="" titleId="cols.homePhon" maxByte="120"  /><!-- valueOption="number" validator="checkPhone(inputTitle, va)" onblur="fnPhoneInput(this);" onfocus="fnPhoneUnInput(this);" -->
									</td>
									<td><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="delTr(this);" /></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr id="mbnoTrs">
						<td class="head_rd">
							<table border="0" cellpadding="0" cellspacing="0">
								<tr>
									<u:radio name="dftCntcTypCd" value="mbno" titleId="cols.mob" alt="휴대전화" checkValue="${wbBcBVo.dftCntcTypCd }"
									inputClass="head_rd" textClass="head_lt" noSpaceTd="true" />
								</tr>
							</table>
						</td>
						<td>
							<table border="0" cellpadding="0" cellspacing="0">
								<c:forEach var="list" items="${wbBcBVo.wbBcCntcDVo }" varStatus="status">
									<c:if test="${list.cntcClsCd eq 'CNTC' && list.cntcTypCd eq 'mbno' }">
										<tr>
											<td>
												<u:input type="hidden" id="cntcTypCd" name="cntcTypCd" value="${empty list.cntcTypCd ? 'mbno' : list.cntcTypCd}" />
												<u:input type="hidden" id="bcCntcSeq" name="bcCntcSeq" value="${list.bcCntcSeq }" />
												<u:input type="hidden" id="cntcClsCd" name="cntcClsCd" value="${empty list.cntcClsCd ? 'CNTC' : list.cntcClsCd}" />
												<u:input id="mobPhon" name="cntcCont" value="${list.cntcCont}" titleId="cols.mobPhon" maxByte="120"  /> <!-- valueOption="number" validator="checkPhone(inputTitle, va)" onblur="fnPhoneInput(this);" onfocus="fnPhoneUnInput(this);" -->
											</td>
											<td>
												<c:choose>
													<c:when test="${list.bcCntcSeq > 1 }"><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="fnDelRow(this,'${list.bcCntcSeq }');" /></c:when>
													<c:otherwise><u:buttonS titleId="wb.btn.more" alt="more" onclick="moreTr('mobPhonTr');" /></c:otherwise>
												</c:choose>
											</td>
										</tr>
									</c:if>
								</c:forEach>
								<tr class="mobPhonTr" style="display: none;">
									<td>
										<u:input type="hidden" id="cntcTypCd" name="cntcTypCd" value="mbno" />
										<u:input type="hidden" id="bcCntcSeq" name="bcCntcSeq" value="" />
										<u:input type="hidden" id="cntcClsCd" name="cntcClsCd" value="CNTC" />
										<u:input id="mobPhon" name="cntcCont" value="" titleId="cols.mobPhon" maxByte="120" /><!-- valueOption="number" validator="checkPhone(inputTitle, va)" onblur="fnPhoneInput(this);" onfocus="fnPhoneUnInput(this);" -->
									</td>
									<td><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="delTr(this);" /></td>
								</tr>
							</table>
						</td>
					</tr>
				</tbody>				
			</table>
		</div>
		<div class="front">
			<div class="front_left">
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="red_stxt"><u:msg titleId="wb.jsp.setBc.tx01" alt="* 우선 연락처로 사용할 전화번호를 선택하십시오." /></td>
					</tr>
				</table>
			</div>
			<div class="front_right">
			</div>
		</div>
		<div class="listarea">
			<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<tr>
					<td width="18%" class="head_lt"><u:msg titleId="cols.comp" alt="회사" /></td>
					<td colspan="3"><u:input id="compNm" name="compNm" value="${wbBcBVo.compNm}" titleId="cols.comp" style="width:98%;" maxByte="120"/></td>
				</tr>
				<tr>
					<td width="18%" class="head_lt"><u:msg titleId="cols.dept" alt="부서" /></td>
					<td width="32%"><u:input id="deptNm" name="deptNm" value="${wbBcBVo.deptNm}" titleId="cols.dept" maxByte="120"/></td>
					<td width="18%" class="head_lt"><u:term termId="or.term.grade" alt="직급" /></td>
					<td><u:input id="gradeNm" name="gradeNm" value="${wbBcBVo.gradeNm}" titleId="cols.grade" maxByte="30" /></td>
				</tr>
				<tr>
					<td class="head_lt"><u:msg titleId="cols.tich" alt="담당업무" /></td>
					<td ><u:input id="tichCont" name="tichCont" value="${wbBcBVo.tichCont}" titleId="cols.tich" maxByte="120"/></td>
					<td class="head_lt"><u:msg titleId="wb.cols.vip" alt="주요인사" /></td>
					<td class="bodybg_lt">
						<table border="0" cellpadding="0" cellspacing="0">
							<tr>
								<u:checkbox name="iptfgYn" value="Y" titleId="wb.cols.vipSetup" checkValue="${wbBcBVo.iptfgYn}" inputClass="bodybg_lt" alt="주요인사로 설정"/>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td width="18%" class="head_lt"><u:msg titleId="cols.fno" alt="팩스번호" /></td>
					<td colspan="3"><u:input id="fno" name="fno" value="${wbBcBVo.fno}" titleId="cols.fno" maxLength="12" minLength="10" valueOption="number" validator="checkPhone(inputTitle, va)" onblur="fnPhoneInput(this);" onfocus="fnPhoneUnInput(this);"/></td>
				</tr>
				<tr>
					<td width="18%" class="head_lt"><u:msg titleId="cols.email" alt="이메일" /></td>
					<td colspan="3">
						<table border="0" cellpadding="0" cellspacing="0">
							<c:forEach var="list" items="${wbBcBVo.wbBcEmailDVo }" varStatus="status">
								<c:if test="${list.cntcClsCd eq 'EMAIL' && list.cntcTypCd eq 'email' }">
									<tr>
										<td>
											<u:input type="hidden" id="cntcTypCd" name="cntcTypCd" value="${empty list.cntcTypCd ? 'email' : list.cntcTypCd}" />
											<u:input type="hidden" id="bcCntcSeq" name="bcCntcSeq" value="${list.bcCntcSeq }" />
											<u:input type="hidden" id="cntcClsCd" name="cntcClsCd" value="${empty list.cntcClsCd ? 'EMAIL' : list.cntcClsCd}" />
											<u:input id="email${status.index }" name="cntcCont" value="${list.cntcCont}" titleId="cols.email" valueOption="email" maxByte="200" validator="checkMail(inputTitle, va)" />
										</td>
										<td>
											<c:choose>
												<c:when test="${list.bcCntcSeq > 1 }"><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="fnDelRow(this,'${list.bcCntcSeq }');" /></c:when>
												<c:otherwise><u:buttonS titleId="wb.btn.more" alt="more" onclick="moreTr('emailTr');" /></c:otherwise>
											</c:choose>
										</td>
									</tr>
								</c:if>
							</c:forEach>
							<tr class="emailTr" style="display: none;">
								<td>
									<u:input type="hidden" id="cntcTypCd" name="cntcTypCd" value="email" />
									<u:input type="hidden" id="bcCntcSeq" name="bcCntcSeq" value="" />
									<u:input type="hidden" id="cntcClsCd" name="cntcClsCd" value="EMAIL" />
									<u:input id="email" name="cntcCont" value="" titleId="cols.email" valueOption="email" maxByte="200" validator="checkMail(inputTitle, va)"/>
								</td>
								<td><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="delTr(this);" /></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<u:blank />
			<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<tr>
					<td width="9%"  class="head_lt"><u:msg titleId="cols.compAdr" alt="회사주소"/></td>
					<td class="head_lt" colspan="2"><u:address id="comp" alt="회사주소" adrStyle="width:94%" zipNoValue="${wbBcBVo.compZipNo }" adrValue="${wbBcBVo.compAdr }" readonly="Y" frameId="openListFrm"/></td>
				</tr>
				<tr>
					<td width="9%"  class="head_lt"><u:msg titleId="cols.homeAdr" alt="자택주소"/></td>
					<td class="head_lt" colspan="2"><u:address id="home" alt="자택주소" adrStyle="width:94%" zipNoValue="${wbBcBVo.homeZipNo }" adrValue="${wbBcBVo.homeAdr }" readonly="Y" frameId="openListFrm"/></td>
				</tr>
			</table>
		</div>
	</div>

	<% // 추가정보 %>
	<div id="addInfoArea" class="inner" style="display:none;">
		<div class="listarea">
			<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<tr>
					<td width="18%" class="head_lt"><u:msg titleId="cols.gen" alt="성별" /></td>
					<td width="32%">
						<select id="genCd" name="genCd" <u:elemTitle titleId="cols.gen" alt="성별" />>
							<c:forEach items="${genCdList}" var="genCdVo" varStatus="genStatus">
								<u:option value="${genCdVo.cd}" title="${genCdVo.rescNm}" checkValue="${wbBcBVo.genCd}"	/>
							</c:forEach>
						</select>
					</td>
					<td width="18%" class="head_lt"><u:msg titleId="cols.naty" alt="국적" /></td>
					<td width="32%">
						<select id="natyCd" name="natyCd" <u:elemTitle titleId="cols.naty" alt="국적" />>
							<option value=""><u:msg titleId="cm.option.noSelect" alt="선택안함" /></option>
							<c:forEach items="${natyCdList}" var="natyCdVo" varStatus="natyStatus">
								<u:option value="${natyCdVo.cd}" title="${natyCdVo.rescNm}" checkValue="${wbBcBVo.natyCd}"	/>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td class="head_lt"><u:msg titleId="cols.birth" alt="생년월일" /></td>
					<td>
						<table border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td><u:calendar id="birth" value="${wbBcBVo.birth}" option="{dtCheck:'A'}"/></td>
								<td class="width10"></td>
								<u:radio name="birthSclcCd" value="SOLA" titleId="cols.sola" alt="양력" checkValue="${wbBcBVo.birthSclcCd }" checked="${empty wbBcBVo.birthSclcCd }"/>
								<u:radio name="birthSclcCd" value="LUNA" titleId="cols.luna" alt="음력" checkValue="${wbBcBVo.birthSclcCd }" />
							</tr>
						</table>
					</td>
					<td class="head_lt"><u:msg titleId="cols.weddAnnv" alt="결혼기념일" /></td>
					<td>
						<table border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td><u:calendar id="weddAnnv" value="${wbBcBVo.weddAnnv}" /></td>
								<td class="width10"></td>
								<u:radio name="weddAnnvSclcCd" value="SOLA" titleId="cols.sola" alt="양력" checkValue="${wbBcBVo.weddAnnvSclcCd }" checked="${empty wbBcBVo.weddAnnvSclcCd }"/>
								<u:radio name="weddAnnvSclcCd" value="LUNA" titleId="cols.luna" alt="음력" checkValue="${wbBcBVo.weddAnnvSclcCd }" />
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td class="head_lt"><u:msg titleId="cols.psnHpage" alt="개인홈페이지" /></td>
					<td colspan="3"><u:input id="psnHpageUrl" name="psnHpageUrl" value="${wbBcBVo.psnHpageUrl}" titleId="cols.psnHpage" style="width:98%;" maxByte="240"/></td>
				</tr>
				<tr>
					<td class="head_lt"><u:msg titleId="cols.compHpage" alt="회사홈페이지" /></td>
					<td colspan="3"><u:input id="compHpageUrl" name="compHpageUrl" value="${wbBcBVo.compHpageUrl}" titleId="cols.compHpage" style="width:98%;" maxByte="240"/></td>
				</tr>
			</table>
			<u:blank />
			<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<tr>
					<td width="18%" class="head_lt"><u:msg titleId="cols.hoby" alt="취미" /></td>
					<td width="32%">
						<u:textarea id="hobyCont" name="hobyCont" value="${wbBcBVo.hobyCont}" titleId="cols.hoby" maxByte="400" style="width:95.5%;" rows="3" /></td>
					<td width="18%" class="head_lt"><u:msg titleId="cols.spect" alt="특기사항" /></td>
					<td>
						<u:textarea id="spectCont" name="spectCont" value="${wbBcBVo.spectCont}" titleId="cols.spect" maxByte="400" style="width:95.5%;" rows="3" /></td>
				</tr>
				<tr>
					<td class="head_lt"><u:msg titleId="cols.eschl" alt="초등학교" /></td>
					<td><u:input id="eschlNm" name="eschlNm" value="${wbBcBVo.eschlNm}" titleId="cols.eschl" style="width:95.5%;" maxByte="120"/></td>
					<td rowspan="3" class="head_lt"><u:msg titleId="cols.univGschl" alt="대학교/대학원" /></td>
					<td rowspan="3">
						<u:textarea id="univCont" name="univCont" value="${wbBcBVo.univCont}" titleId="cols.univGschl"	maxByte="240" style="width:95.5%; height: 64px;" rows="4" />
					</td>
				</tr>
				<tr>
					<td class="head_lt"><u:msg titleId="cols.mschl" alt="중학교" /></td>
					<td><u:input id="mschlNm" name="mschlNm" value="${wbBcBVo.mschlNm}" titleId="cols.mschl" style="width:95.5%;" maxByte="120"/></td>
				</tr>
				<tr>
					<td class="head_lt"><u:msg titleId="cols.hschl" alt="고등학교" /></td>
					<td><u:input id="hschlNm" name="hschlNm" value="${wbBcBVo.hschlNm}" titleId="cols.hschl" style="width:95.5%;" maxByte="120"/></td>
				</tr>
			</table>
			<u:blank />
			<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<tr>
					<td width="18%" class="head_lt"><u:msg titleId="cols.bhist" alt="약력" /></td>
					<td>
						<u:textarea id="bhistCont" name="bhistCont" value="${wbBcBVo.bhistCont}" titleId="cols.bhist" maxByte="400" style="width:98%;" rows="5" />
					</td>
				</tr>
			</table>
			<u:blank />
			<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<colgroup><col width="18%"/><col width="*"/></colgroup>
				<c:if test="${listPage ne 'listPubBc' }">
				<tr>
					<td class="head_lt"><u:msg titleId="cols.clns" alt="친밀도" /></td>
					<td>
						<table border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td>
									<select id="clnsId" name="clnsId">
										<u:option value="" titleId="cols.clns" selected="${empty wbBcBVo.clnsId }"/>
										<c:forEach var="list" items="${wbBcClnsCVoList }" varStatus="status">
											<u:option value="${list.clnsId }" title="${list.clnsNm }" checkValue="${wbBcBVo.clnsId }"/>
										</c:forEach>
									</select>
								</td>
								<td><u:buttonS titleId="wb.btn.clnsAdd" alt="친밀도추가" onclick="openClnsAdd();" /></td>
							</tr>
						</table>
					</td>
				</tr>
				</c:if>
				<tr>
					<td class="head_lt"><u:msg titleId="cols.note" alt="비고" /></td>
					<td>
						<u:textarea id="noteCont" name="noteCont" value="${wbBcBVo.noteCont}" titleId="cols.note" maxByte="400" style="width:98%;" rows="3" />
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<u:files id="${filesId}" fileVoList="${fileVoList}" module="wb" mode="set" exts="${exts }" extsTyp="${extsTyp }"/>
					</td>
				</tr>
			</table>
		</div>
	</div>
</div>
<u:input type="hidden" id="delList" name="delList" />
<input type="hidden" id="photoTempPath" name="tempDir" value="${!empty param.toBcId ? wbBcBVo.wbBcImgDVo.imgPath : ''}"/>
</form>

<u:blank />

<u:set var="rightBtnDisplay" test="${frmYn eq 'Y' }" value="display:none;" elseValue=""/>
<% // 하단 버튼 %>
<u:buttonArea id="rightBtnArea" style="${rightBtnDisplay }">
<c:if test="${empty param.bcId }"><u:button titleId="cm.btn.add" alt="추가" href="javascript:addBcSinglePop();" auth="W" /></c:if>
<u:button titleId="cm.btn.save" onclick="save();" alt="저장" auth="${listPage eq 'listAllBc' ? 'A' : 'W' }" ownerUid="${listPage eq 'listBc' ? wbBcBVo.regrUid : ''}"/>
<u:button titleId="cm.btn.cancel" href="javascript:historyPage();" alt="취소" />
</u:buttonArea>
</div>