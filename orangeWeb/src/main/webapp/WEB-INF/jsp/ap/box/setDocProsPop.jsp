<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	// 왼쪽 도장 미리보기 - 높이 계산
	int signAreaHeight = 79;
	java.util.Map optConfigMap = (java.util.Map)request.getAttribute("optConfigMap");
	if(!"Y".equals(optConfigMap.get("notAlwApvPw")) && !("mak".equals(request.getParameter("apvStatCd")) && "Y".equals(optConfigMap.get("noMakPw")))){
		signAreaHeight += 25;
	}
	if("Y".equals(request.getParameter("opinInDoc"))) signAreaHeight += 25;
	request.setAttribute("signAreaHeight", signAreaHeight+"px");
	
	// mak:기안, apvd:승인, pros:찬성, cons:반대 - 도장 찍어야 할 곳
%><c:if
	test="${(param.apvStatCd == 'mak' or param.apvStatCd == 'apvd' or param.apvStatCd == 'pros' or param.apvStatCd == 'cons')
		and signMthdCd != '03' and param.noStamp != 'Y'
		and empty orUserImgDVo.imgPath}"><c:set var="hasNoStamp" value="${true}" /></c:if>
<script type="text/javascript">
<!--
<%// 문서에 의견 정보 리턴 %>
function getOpinHiddenData(){
	var data = {};
	$("#docDataArea #docOpinArea input").each(function(){
		data[$(this).attr('name')] = this.value;
	});
	return data;
}
var gStampMsg = null;
<%// [mak:상신(기안), reRevw:재검토, apvd:승인, rejt:반려, pros:찬성, cons:반대] - 버튼 클릭 %>
function submitPros(apvStatCd){
	if(gStampMsg!=null){<%// [도장/서명] 이미지 설정 검사 %>
		alert(gStampMsg);
		return;
	}
	var oldApvOpinCont = $("#docDataArea #docOpinArea input[name=apvOpinCont]").val();
	if(!validator.validate('setProsForm')){
		$("#docDataArea #docOpinArea input[name=apvOpinCont]").val(oldApvOpinCont==null ? '' : oldApvOpinCont);
		return;
	}
	<%
	// 데이터 모으기 %>
	var param = new ParamMap().getData($("#setProsForm")[0]);
	if(gOptConfig.notAlwApvPw!='Y' && !(apvStatCd=='mak' && gOptConfig.noMakPw=='Y')){
		if(param.get("pw")!=null && param.get("pw").trim()==''){<%
			// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [비밀번호] %>
			alertMsg("cm.input.check.mandatory", ["#ap.jsp.doc.pw"]);
			$("#setProsForm #pw").focus();
			return;
		}
	}
	if(apvStatCd=='reRevw' || apvStatCd=='rejt' || apvStatCd=='cons'){<%// reRevw:재검토, rejt:반려, cons:반대 %>
		if(param.get("apvOpinCont").trim()==''){<%
			// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [결재의견] %>
			alertMsg("cm.input.check.mandatory", ["#ap.jsp.doc.apvOpin"]);
			$("#setProsForm #apvOpinCont").focus();
			return;
		}
	}
	var $area = $("#docDataArea #docOpinArea");
	$area.html('');
	param.each(function(key, va){
		if(key!='pw'){
			$area.append("<input type='hidden' name='"+key+"' value='"+escapeValue(va)+"' />");
		}
	});
	<%
	// [옵션]결재 비밀번호 사용 안함 - N 일 경우
	//     - 암호화 세션 만들고 비빌번호 확인후 확인키(secuId) 받아서 문서에 세팅함
	%>
	if(gOptConfig.notAlwApvPw!='Y' && !(apvStatCd=='mak' && gOptConfig.noMakPw=='Y')){
		var key = null, secuId = null, noPw = null;<%
		// 암호화 세션 생성 - 키 가져옴 %>
		callAjax("${_cxPth}/cm/login/createSecuSessionAjx.do", null, function(data){
			key = new RSAPublicKey(data.e, data.m);
		});
		if(key!=null){
			var data = encrypt(key, JSON.stringify({apvPw:param.get('pw')}));<%
			// 결재 비밀번호 확인하고 보안ID 발급 %>
			callAjax("./getSecuIdAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {secu:data}, function(data){
				secuId = data['secuId'];<%// 보안ID %>
				noPw = data['noPw'];
			});
		}
		if(secuId!=null){<%
			// secuId 를 서밋할 곳에 세팅함 %>
			var $sucuId = $area.find("input[name='secuId']");
			if($sucuId.length==0){
				$area.append("<input type='hidden' name='secuId' value='"+secuId+"' />");
			} else {
				$sucuId.val(secuId);
			}<%
			// 옵션에 따른 상신 메세지 체크 %>
			if(!checkSubmitMsg(apvStatCd)) return;
			submitDoc(apvStatCd);
			dialog.close("setDocOpinDialog");
		} else if(noPw=='Y'){
			alertMsg('ap.trans.noApvPw');
			return;
		}
	} else {
		if(!checkSubmitMsg(apvStatCd)) return;
		submitDoc(apvStatCd);
		dialog.close("setDocOpinDialog");
	}
}<%
// 옵션 체크해서 메세지 컴%>
function checkSubmitMsg(apvStatCd){
	var roleCd = getMakrRoleCd();
	if(roleCd == 'byOne' || roleCd == 'byOneAgr'){
		if(gOptConfig.msgOnePsn=='Y'){<%
			// ap.cfrm.process={0} 하시겠습니까 ? %>
			return confirmMsg("ap.cfrm.process", ["#ap.term.byOne"]);
		}
		return true;
	} else if(apvStatCd == 'reRevw'){
		if(gOptConfig.msgSign=='Y'){<%
			// ap.cfrm.reqProcess={0} 요청 하시겠습니까 ? %>
			return confirmMsg("ap.cfrm.reqProcess", ["#ap.term.reRevw"]);
		}
		return true;
	} else {
		if(gOptConfig.msgSign=='Y'){<%
			// ap.cfrm.process={0} 하시겠습니까 ? %>
			return confirmMsg("ap.cfrm.process", ["#ap.term."+apvStatCd]);
		}
		return true;
	}
}
<%// secuId(비밀번호 확인값) 세팅하고 전송 %>
function processSubmitPros(secuId){
	var $area = $("#docDataArea #docOpinArea");
	$area.append("<input type='hidden' name='secuId' value='"+secuId+"' />");
	submitDoc(apvStatCd);
	dialog.close("setDocOpinDialog");
}<%
// 엔터- 패스워드 %>
function checkSubmit(event){
	doNotSubmit(event);
	submitPros('${param.apvStatCd}');
}
$(document).ready(function() {
	var data = getOpinHiddenData();
	var $form = $("#setProsForm");
	if(data['apvOpinCont']!=null) $form.find("#apvOpinCont").val(data['apvOpinCont']);
	if(data['apvOpinDispYn']=='Y' || (data['apvOpinDispYn']=='' && '${optConfigMap.opinDftDisp}'=='Y')){
		$form.find("[name='apvOpinDispYn']").checkInput(true);
	}
	$form.find("#signArea").height($form.find("#opinArea").height());<%
	
	// 반려가 아니고(승인,찬성,반대), 텍스트서명(결재자명)이 아닐때 - 도장 or 서명이 없으면
	//    - 메세지 출력 및 진행 금지
	// ap.msg.noImgNoApv={0} 이미지를 설정하지 않아서 진행 할 수 없습니다. - [도장 or 서명]
	%>
	<c:if test="${hasNoStamp}"><c:if
		test="${signMthdCd=='01'}">gStampMsg = '<u:msg titleId="ap.msg.noImgNoApv" arguments="#or.txt.stamp"/>';</c:if><c:if
		test="${signMthdCd=='02'}">gStampMsg = '<u:msg titleId="ap.msg.noImgNoApv" arguments="#or.txt.sign"/>';</c:if>
	</c:if>
	if(gStampMsg!=null) alert(gStampMsg);
});
//-->
</script>

<div style="width:${param.noStamp == 'Y' ? '600px' : '720px'};">
<form id="setProsForm" onsubmit="checkSubmit(event)"><c:if
	test="${param.atMakAgr == 'Y'}">
<u:input id="atMakAgr" value="Y" type="hidden" /></c:if>

<table border="0" cellpadding="0" cellspacing="0"><tbody><tr><c:if
	test="${param.noStamp != 'Y'}">
<td width="116px">
<u:listArea><u:cmt
		cmt="apvStatCd - mak:기안, apvd:승인, rejt:반려, pros:찬성, cons:반대" />
<tr><td style="height:${signAreaHeight}; text-align:center"><c:if
	test="${param.apvStatCd == 'rejt' || param.apvStatCd == 'reRevw'}"
	><u:term termId="ap.term.${param.apvStatCd}" alt="반려 or 재검토" /></c:if><c:if
	test="${param.apvStatCd != 'rejt' and param.apvStatCd != 'reRevw'}"
	><c:if
		test="${signMthdCd == '03'}"
		>${sessionScope.userVo.userNm}</c:if><c:if
		test="${signMthdCd != '03' and not empty orUserImgDVo.imgPath}"
		><img height="40px" src="${orUserImgDVo.imgPath}" /></c:if><c:if
		test="${signMthdCd != '03' and empty orUserImgDVo.imgPath}"
		><u:msg titleId="ap.msg.noImg" alt="이미지 없음" /></c:if></c:if>
</td></tr>
</u:listArea>
</td>
<td width="4px"></td>
</c:if>
<td width="600px">
<u:listArea>
	<c:if test="${optConfigMap.notAlwApvPw ne 'Y' and not (param.apvStatCd eq 'mak' and optConfigMap.noMakPw eq 'Y')}">
	<tr>
	<td width="17%" class="head_ct"><u:msg titleId="ap.jsp.doc.pw" alt="비밀번호" /></td>
	<td width="83%"><u:input id="pw" type="password" value="" titleId="ap.jsp.doc.pw" maxLength="30" style="width:32%" /></td>
	</tr>
	</c:if>
	<tr>
	<td class="head_ct"><u:msg titleId="ap.jsp.doc.apvOpin" alt="결재의견" /></td>
	<td><u:textarea id="apvOpinCont" value="" titleId="ap.jsp.doc.apvOpin" maxByte="800" rows="5" style="width:97%" /></td>
	</tr>
	
	<tr<c:if test="${param.opinInDoc != 'Y'}" > style="display:none"</c:if>>
	<td class="head_ct"><u:msg titleId="ap.jsp.doc.opinDisp" alt="의견표시" /></td>
	<td style="padding-left:1px;"><table border="0" cellpadding="0" cellspacing="0"><tbody><tr>
		<u:checkbox id="apvOpinDispYnForPop" name="apvOpinDispYn" value="Y" titleId="ap.jsp.doc.opinDispDetl" />
		</tr></tbody></table></td>
	</tr>
</u:listArea>
</td>
</tr></tbody></table>

<u:buttonArea><u:cmt

		cmt="apvStatCd - mak:기안, apvd:승인, rejt:반려, pros:찬성, cons:반대" /><c:if
		test="${param.apvStatCd == 'mak' and not hasNoStamp}">
	<u:button termId="ap.btn.subm" href="javascript:submitPros('${param.apvStatCd}');" alt="상신" auth="W" /></c:if><c:if
		test="${param.apvStatCd == 'apvd' and not hasNoStamp}">
	<u:button termId="ap.term.apvd" href="javascript:submitPros('${param.apvStatCd}');" alt="승인" auth="W" /></c:if><c:if
		test="${param.apvStatCd == 'rejt'}">
	<u:button termId="ap.term.rejt" href="javascript:submitPros('${param.apvStatCd}');" alt="반려" auth="W" /></c:if><c:if
		test="${param.apvStatCd == 'pros' and not hasNoStamp}">
	<u:button termId="ap.term.pros" href="javascript:submitPros('${param.apvStatCd}');" alt="합의승인" auth="W" /></c:if><c:if
		test="${param.apvStatCd == 'cons' and not hasNoStamp}">
	<u:button termId="ap.term.cons" href="javascript:submitPros('${param.apvStatCd}');" alt="합의반대" auth="W" /></c:if><c:if
		test="${param.apvStatCd == 'reRevw'}">
	<u:button termId="ap.term.reRevw" href="javascript:submitPros('${param.apvStatCd}');" alt="재검토" auth="W" /></c:if><c:if
		test="${param.apvStatCd == 'makVw'}">
	<u:button termId="ap.btn.subm" href="javascript:submitPros('${param.apvStatCd}');" alt="상신" auth="W" /></c:if><c:if
		test="${param.apvStatCd == 'fstVw'}">
	<u:button termId="ap.term.fstVw" href="javascript:submitPros('${param.apvStatCd}');" alt="선람" auth="W" /></c:if><c:if
		test="${param.apvStatCd == 'pubVw'}">
	<u:button termId="ap.term.pubVw" href="javascript:submitPros('${param.apvStatCd}');" alt="공람" auth="W" /></c:if><c:if
		test="${hasNoStamp}"><u:authUrl url="/ap/env/setPsnEnv.do" var="regStampUrl" />
	<u:button titleId="ap.btn.regStampImg" onclick="if(confirmMsg('ap.confirm.moveToRegStamp')) location.href='${regStampUrl}';" alt="도장 / 서명 이미지 등록" />
	</c:if>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>
