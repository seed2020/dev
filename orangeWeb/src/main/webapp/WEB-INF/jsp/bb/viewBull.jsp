<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set var="listPage" test="${!empty param.compId }" value="listCoprBull" elseValue="${listPage }"/>
<script type="text/javascript">
<!--
//게시판 ID를 param에 세팅
function setBrdId(brdId){
	var queryParams = '${params}'.split('&');
	var params = "",tmpParams;
	for(var i=0;i<queryParams.length;i++){
		tmpParams = queryParams[i].split('=');
		if(tmpParams[0] == 'brdId') queryParams[i] = tmpParams[0] + "=" + brdId;
		params += params == '' ? queryParams[i] : '&' + queryParams[i]; 
	}
	
	return params;
};

<% // [목록:제목] 게시물 조회 %>
function viewBull(id , brdId) {
	if(brdId == undefined ) brdId = '${baBrdBVo.brdId}';
	//location.href = './${viewPage}.do?${params}&bullId=' + id;
	params = setBrdId(brdId);
	location.href = './${viewPage}.do?'+params+'&bullId=' + id;
}
<% // [목록:제목] 보안글 조회를 위한 로그인폼 화면 %>
function openLogin(id , brdId) {
	if(brdId == undefined ) brdId = '${baBrdBVo.brdId}';
	params = setBrdId(brdId);
	dialog.open('setLoginPop','<u:msg titleId="bb.jsp.setLoginPop.title" alt="보안글 인증" />','./setLoginPop.do?'+params+'&viewPage=${viewPage}&bullId=' + id);
}
<% // [목록:조회수] 조회이력 %>
function readHst(id) {
	dialog.open('listReadHstPop','<u:msg titleId="bb.jsp.listReadHstPop.title" alt="조회자 정보" />','./listReadHstPop.do?menuId=${menuId}&brdId=${baBrdBVo.brdId}&bullId=' + id);
}
<% // [하단버튼:게시물이동|게시물복사] %>
function selectBb() {
	var callback = 'copyBull';
	if (parseInt('${bbBullLVo.replyDpth}') > 0) {
		if (callback == 'copyBull') alertMsg("bb.msg.copyBull.hasPid");<% // bb.msg.copyBull.hasPid=답글은 복사할 수 없습니다. %>
		return;
	}

	var arr = [${bbBullLVo.bullId}];
	if (arr != null) {
		var mul = (callback == 'moveBull') ? '&mul=N' : '&mul=Y';
		var params = '&brdId=${baBrdBVo.brdId}' + mul + '&callback=' + callback + '&callbackArgs=' + arr;
		dialog.open('selectBbPop','<u:msg titleId="bb.jsp.selectBb.title" alt="게시판 선택" />','./selectBbPop.do?menuId=${menuId}' + params);
	}
}
<% // 게시물 복사 %>
function copyBull(brdIds, brdNms, bullIds) {
	var bullIds = bullIds.split(',');
	callAjax('./transBullCopyAjx.do?menuId=${menuId}&bullId=${param.bullId}&brdId=${baBrdBVo.brdId}', {brdId:'${baBrdBVo.brdId}', brdIds:brdIds, bullIds:bullIds}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			if('${fn:length(paramEntryList)}' > 0){
				$form = $('#viewForm'); 
				$form[0].submit();	
			}else{
				location.replace(location.href);
			}
			
		}
	});
}
<% // [하단버튼:답변] 답변 %>
function setReply() {
	location.href = './setReply.do?${params}&bullPid=${param.bullId}';
}
<% // [하단버튼:수정] 수정 %>
function modBull() {
	location.href = './${setPage}.do?${params}&bullId=${param.bullId}';
}
<% // [하단버튼:삭제] 삭제 %>
<c:if test="${delAction != null}">
function delBull() {
	<c:if test="${isSysAdmin == true}">
	if (confirmMsg("bb.msg.del.all")) {	<% // 답변이 있을경우 답변글도 같이 삭제됩니다.\n삭제하시겠습니까? %>
		callAjax('./${delAction}.do?menuId=${menuId}&bullId=${param.bullId}&brdId=${baBrdBVo.brdId}', {brdId:'${baBrdBVo.brdId}', bullId:'${param.bullId}'}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.href = './${listPage}.do?${paramsForList}';
			}
		});
	}
	</c:if>
	<c:if test="${isSysAdmin == false}">
		callAjax('./${delAction}Chk.do?menuId=${menuId}&bullId=${param.bullId}&brdId=${baBrdBVo.brdId}', {brdId:'${baBrdBVo.brdId}', bullId:'${param.bullId}'}, function(data) {
			if (data.message != null) {
				alert(data.message);
				return;
			}
			if (data.result == 'ok') {
				if (confirmMsg("cm.cfrm.del")) {	<% // 삭제하시겠습니까? %>
					callAjax('./${delAction}.do?menuId=${menuId}&bullId=${param.bullId}&brdId=${baBrdBVo.brdId}', {brdId:'${baBrdBVo.brdId}', bullId:'${param.bullId}'}, function(data) {
						if (data.message != null) {
							alert(data.message);
						}
						if (data.result == 'ok') {
							location.href = './${listPage}.do?${paramsForList}';
						}
					});
				}
			}
		});
	</c:if>
}
</c:if>
<% // [하단버튼:목록] 목록으로 이동 %>
function goList() {
	location.href = './${listPage}.do?${paramsForList}';
}
<% // 게시대상 부서 표시 %>
function setBbTgtDept(len) {
	var $nameTd = $("#bbTgtDept");
	if (len > 0) {
		$nameTd.html('<u:msg titleId="cols.dept" alt="부서" /> ' + len);
		$nameTd.show();
	} else {
		$nameTd.html('');
		$nameTd.hide();
	}
}
<% // 게시대상 사용자 표시 %>
function setBbTgtUser(len) {
	var $nameTd = $("#bbTgtUser");
	if (len > 0) {
		$nameTd.html('<u:msg titleId="cols.user" alt="사용자" /> ' + len);
		$nameTd.show();
	} else {
		$nameTd.html('');
		$nameTd.hide();
	}
}
<% // 다중 부서 선택 - 하위부서 여부 포함 %>
function openMuiltiOrgWithSub(mode){
	var $inputTd = $("#bbTgtDeptHidden"), data = [];<% // data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	var $subs = $inputTd.find("input[name='withSubYn']");
	$inputTd.find("input[name='orgId']").each(function(index){
		data.push({orgId:$(this).val(), withSub:$($subs[index]).val()});
	});
	<% // option : data, multi, withSub, titleId %>
	searchOrgPop({data:data, multi:true, withSub:true, mode:mode==null ?'search':'view'}, function(arr){
		if(arr!=null){
			var buffer = [];
			arr.each(function(index, orgVo){
				buffer.push("<input name='orgId' type='hidden' value='"+orgVo.orgId+"' />\n");
				buffer.push("<input name='withSubYn' type='hidden' value='"+orgVo.withSub+"' />\n");
				buffer.push("\n");
			});
			$inputTd.html(buffer.join(''));
			setBbTgtDept(arr.length);
		} else {
			$inputTd.html('');
			setBbTgtDept(0);
		}
		//return false;// 창이 안닫힘
	});
}
<% // 여러명의 사용자 선택 %>
function openMuiltiUser(mode){
	var $inputTd = $("#bbTgtUserHidden"), data = [];<% // data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	$inputTd.find("input[name='userUid']").each(function(){
		data.push({userUid:$(this).val()});
	});
	<% // option : data, multi, titleId %>
	searchUserPop({data:data, multi:true, mode:mode==null ?'search':'view'}, function(arr){
		if(arr!=null){
			var buffer = [];
			arr.each(function(index, userVo){
				buffer.push("<input name='userUid' type='hidden' value='"+userVo.userUid+"' />\n");
				buffer.push("\n");
			});
			$inputTd.html(buffer.join(''));
			setBbTgtUser(arr.length);
		} else {
			$inputTd.html('');
			setBbTgtUser(0);
		}
	});
}
<% // 추천 %>
<c:if test="${etcDIspYn && baBrdBVo.recmdUseYn == 'Y'}">
function recmdBull() {
	if (confirmMsg("bb.cfrm.recmd")) {	<% // bb.cfrm.recmd=추천하시겠습니까? %>
		callAjax('./transBullRecmdAjx.do?menuId=${menuId}&brdId=${param.brdId}&bullId=${param.bullId}', {brdId:'${baBrdBVo.brdId}', bullId:'${param.bullId}'}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				$('#recmdCntTd').html(data.recmdCnt);
			}
		});
	}
}
</c:if>
<% // 찬반투표 %>
<c:if test="${etcDIspYn && baBrdBVo.favotYn == 'Y'}">
function favotBull(favotVa) {
	if (confirmMsg("bb.cfrm.favot")) {	<% // bb.cfrm.favot=투표하시겠습니까? %>
		callAjax('./transBullFavotAjx.do?menuId=${menuId}&brdId=${param.brdId}&bullId=${param.bullId}', {brdId:'${baBrdBVo.brdId}', bullId:'${param.bullId}', favotVa:favotVa}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				if (favotVa == 'F') $('#prosCntSpan').html(data.prosCnt);
				if (favotVa == 'A') $('#consCntSpan').html(data.consCnt);
			}
		});
	}
}
</c:if>
<% // 점수주기 %>
<c:if test="${etcDIspYn && baBrdBVo.screUseYn == 'Y'}">
function saveScre() {
	if (${screHstExist}) {
		alertMsg("bb.msg.scre.already");<% // bb.msg.scre.already=이미 점수를 준 게시물입니다. %>
		return;
	}
	var $checked = $('input[type=radio][name=scre]:checked');
	if ($checked.length == 0) {
		alertMsg("bb.msg.scre.notChecked");<% // bb.msg.scre.notChecked=점수를 선택하세요. %>
		return;
	}
	callAjax('./transBullScreAjx.do?menuId=${menuId}&bullId=${param.bullId}&brdId=${baBrdBVo.brdId}', {brdId:'${baBrdBVo.brdId}', bullId:'${param.bullId}', scre:$checked.val()}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
	});
}
</c:if>
<% // 점수내역 %>
<c:if test="${etcDIspYn && baBrdBVo.screUseYn == 'Y'}">
function viewScre() {
	dialog.open('viewScrePop','<u:msg titleId="bb.jsp.viewScrePop.title" alt="점수내역" />','./viewScrePop.do?menuId=${menuId}&bullId=${param.bullId}&brdId=${param.brdId}');
}
</c:if>
<% // [하단버튼:본문저장] 본문을 html로 저장 %>
function saveBody() {
	var $form = $('#saveBodyForm');
	$form.attr('method','post');
	$form.attr('action','./saveBody.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form[0].submit();	
	
	return;
	/**
		var $form = $('<form>', {
			'method':'post',
			'action':'./saveBody.do',
			'target':'dataframe'
		}).append($('<input>', {
			'name':'brdId',
			'value':'${baBrdBVo.brdId}',
			'type':'hiden'
		})).append($('<input>', {
			'name':'bullId',
			'value':'${bbBullLVo.bullId}',
			'type':'hiden'
		}));
		$form.submit();
	*/ 
	
}

function setPopupPop() {
	dialog.open('setPopupPop','<u:msg titleId="bb.btn.setPopup" alt="로그인 팝업설정" />','./setPopupPop.do?menuId=${menuId}&bullId=${param.bullId}&brdId=${baBrdBVo.brdId}');
}<%// 문서보내기 - 옵션 %>
function saveSendOpt(arrs){
	var $form = $('#saveSendDocForm');
	$.each(arrs,function(index,vo){
		$form.find("[name='"+vo.name+"']").remove();
		$form.appendHidden({name:vo.name,value:vo.value});
	});
	$form.attr('method','post');
	$form.attr('action','./transSendDoc.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form[0].submit();	
}<%// 문서 보내기 - 등록 팝업%>
function setSendWritePop(){
	var param = new ParamMap().getData('saveSendDocForm');
	param.put('tabId','doc');
	param.put('docTyp','brd');
	param.put('dialog','setSendWritePop');
	param.put('menuId','${menuId}');
	sendDocWritePop(param);
}<%// [버튼] - 등록자변경 %>
function setRegChn(){
	<% // option : data, multi, titleId %>
	searchUserPop({data:null, multi:false, mode:'search'}, function(vo){
		if(vo!=null){
			var bullIds=[];
			bullIds.push('${param.bullId}');
			callAjax('./transBullChnAjx.do?menuId=${menuId}&bullId=${param.bullId}&brdId=${baBrdBVo.brdId}', {brdId:'${baBrdBVo.brdId}', bullIds:bullIds, regrUid:vo.userUid}, function(data) {
				if (data.message != null) {
					alert(data.message);
				}
				if (data.result == 'ok') {
					location.replace(location.href);
				}
			});
		}
	});
}
$(document).ready(function() {
	setBbTgtDept(${bullTgtDeptVoList != null ? bullTgtDeptVoList.size() : 0});
	setBbTgtUser(${bullTgtUserVoList != null ? bullTgtUserVoList.size() : 0});

	setUniformCSS();
	<c:if test="${!empty isSns && isSns == true }">
	//setMetaSns('${bbBullLVo.subj}', location.href, '<u:clob lobHandler="${lobHandler }"/>', '${_logoImg}');
	//setFbInit('${_lang}');
	</c:if>
});

//-->
</script>
<c:if test="${isOpen==true }"><c:set var="style" value=" style=\"min-width:1080px;padding:10px;\""/></c:if>
<div${style }>
<c:if test="${fn:length(paramEntryList) > 0}">
	<form id="viewForm" method="post" action="./${viewPage}.do?menuId=${menuId }" >
		<c:forEach items="${paramEntryList}" var="entry" varStatus="status">
			<u:input type="hidden" id="${entry.key}" value="${entry.value}" />
		</c:forEach>
	</form>
</c:if>	


<u:msg titleId="bb.jsp.viewBull.title" var="title" alt="게시물 조회" />

<c:if test="${viewPage == 'viewRezvBull'}"><u:msg titleId="bb.jsp.listRezvBull.title" alt="예약저장함" var="bbNm" /></c:if>
<c:if test="${viewPage == 'viewBullMng'}"><u:msg titleId="bb.jsp.listBullMng.title" alt="게시물관리" var="bbNm" /></c:if>
<c:if test="${bbNm == null}"><c:set var="bbNm" value="${baBrdBVo.rescNm}" /></c:if>

<u:title title="${title} - ${bbNm}" menuNameFirst="true"/>

<c:set var="sendDocYn" value="${dmEnable }"/><!-- 시스템설정 -->

<!-- SNS 사용 -->
<c:if test="${!empty param.brdId && !empty baBrdBVo && !empty param.bullId}">
<u:out var="snsText" value="${bbBullLVo.subj}" type="value"/>
<u:sns mode="view" text="${snsText }" snsParams="&brdId=${param.brdId }&bullId=${param.bullId }"/>
</c:if>
<% // 상단 버튼 %>
<c:if test="${empty isOpen}">
<u:buttonArea>
	<c:if test="${empty param.compId }">
		<c:if test="${isSysAdmin == true}"><u:button titleId="bb.btn.regChn" alt="등록자변경" onclick="setRegChn();" /></c:if>
		<c:if test="${colmMap.cont.readDispYn eq 'Y' }">
		<%-- <c:if test="${!empty bbBullLVo.bullId && !empty sendDocYn && sendDocYn eq 'Y' }"><u:button titleId="dm.btn.sendDoc" alt="문서보내기" onclick="sendDocOptPop('applyJsonCfrm');" auth="W" ownerUid="${bbBullLVo.regrUid}"/></c:if> --%>
		<u:internalIp><c:if test="${!empty bbBullLVo.bullId && !empty sendDocYn && sendDocYn eq 'Y' && sessionScope.userVo.hasMnuGrpMdRidOf('DM')}"><u:button titleId="dm.btn.sendDoc" alt="문서보내기" onclick="setSendWritePop();" auth="W" ownerUid="${bbBullLVo.regrUid}"/></c:if></u:internalIp>
		<c:if test="${baBrdBVo.photoYn != 'Y' and bbBullLVo.bullPid == null}">
			<u:button titleId="bb.btn.setPopup" alt="팝업설정" onclick="setPopupPop();" auth="${!empty isLginPop && isLginPop==true ? 'W' : 'S' }"/>
		</c:if>	
		<u:button titleId="bb.btn.bodySave" alt="본문저장" onclick="saveBody();" />
	</c:if>
	<c:if test="${sessionScope.userVo.hasMnuGrpMdRidOf('MAIL') && mailEnable == 'Y' }">
		<u:internalIp><u:button titleId="cm.btn.emailSend" alt="이메일발송" href="javascript:;" onclick="emailSendPop({bullId:'${bbBullLVo.bullId}', brdId:'${baBrdBVo.brdId}'},'${menuId }');"/></u:internalIp>
	</c:if>
	</c:if>
	<c:if test="${empty param.compId }">
		<u:button titleId="bb.btn.bullCopy" alt="게시물복사" href="javascript:selectBb('copyBull');" auth="W" />
		<c:if test="${listPage == 'listBull'}">
			<c:if test="${baBrdBVo.replyYn == 'Y'}">
			<u:button titleId="bb.btn.reply" alt="답변" onclick="setReply();" auth="W" />
			</c:if>
		</c:if>	
		<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" auth="R" />
	</c:if>
	<u:set var="auth" test="${!empty eqCompYn && eqCompYn eq 'N'}" value="SYS" elseValue="M"/>
	<u:button titleId="cm.btn.mod" alt="수정" onclick="modBull();" auth="${auth }" ownerUid="${bbBullLVo.regrUid}" />
	<u:set var="auth" test="${!empty eqCompYn && eqCompYn eq 'N'}" value="SYS" elseValue="A"/>
	<u:button titleId="cm.btn.del" alt="삭제" onclick="delBull();" auth="${auth }" ownerUid="${bbBullLVo.regrUid}" />
	<u:button titleId="cm.btn.list" alt="목록" onclick="goList();" />
</u:buttonArea>
</c:if>
<c:if test="${baBrdBVo.optMap.privUseYn eq 'Y' && !empty privYn && privYn eq 'Y'}">
<div class="front">
	<div class="front_left">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr><td class="color_stxt">* <u:msg titleId="bb.msg.view.priv" alt="비공개로 등록된 문서입니다." /></td></tr>
		</table>
	</div>
</div>
</c:if>	
<% // 폼 필드 %>
<div class="">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
		<colgroup>
			<col width="18%"/>
			<col width="32%"/>
			<col width="18%"/>
			<col width="32%"/>
		</colgroup>
	<tbody>
	<c:if test="${baColmDispDVoMap['SUBJ'].readDispYn == 'Y'}">
	<tr>
	<td class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td colspan="3" class="body_lt"><div id="snsSubj" class="ellipsis" title="${bbBullLVo.subj}" >${bbBullLVo.subj}</div></td>
	</tr>
	</c:if>

	<c:if test="${viewPage != 'viewBull'}">
		<u:set test="${brdNms != null}" var="brdNms" value="${brdNms}" elseValue="${baBrdBVo.rescNm}" />
	<tr>
	<td class="head_lt"><u:msg titleId="cols.bb" alt="게시판" /></td>
	<td colspan="3"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="body_lt"><span id="brdNms">${brdNms}</span></td>
		</tr>
		</table></td>
	</tr>
	</c:if>

	<c:if test="${baBrdBVo.catYn == 'Y' && baColmDispDVoMap['CAT_ID'].readDispYn == 'Y'}">
	<tr>
	<td class="head_lt"><u:msg titleId="cols.cat" alt="카테고리" /></td>
	<td colspan="3" class="body_lt">${bbBullLVo.catNm}</td>
	</tr>
	</c:if>

	<c:set var="trDispYn" value="false" />
	<c:set var="tdDispYn1" value="false" />
	<c:set var="tdDispYn2" value="false" />
	<c:if test="${baColmDispDVoMap['REGR_UID'].readDispYn == 'Y'}">
		<c:set var="trDispYn" value="true" />
		<c:set var="tdDispYn1" value="true" />
	</c:if>
	<c:if test="${baColmDispDVoMap['READ_CNT'].readDispYn == 'Y'}">
		<c:set var="trDispYn" value="true" />
		<c:set var="tdDispYn2" value="true" />
	</c:if>
	<u:set test="${tdDispYn1 && !tdDispYn2}" var="colspan1" value="3" elseValue="1" />
	<u:set test="${!tdDispYn1 && tdDispYn2}" var="colspan2" value="3" elseValue="1" />
	
	<c:if test="${trDispYn}">
	<tr>
	<c:if test="${tdDispYn1}">
	<td class="head_lt"><u:msg titleId="cols.regr" alt="등록자" /></td>
	<td colspan="${colspan1}" class="body_lt">
		<c:if test="${baBrdBVo.brdTypCd == 'N'}">
		<a href="javascript:viewUserPop('${bbBullLVo.regrUid}');"><u:out value="${bbBullLVo.regrNm}" /></a>
		</c:if>
		<c:if test="${baBrdBVo.brdTypCd == 'A'}">
		<u:out value="${bbBullLVo.anonRegrNm}" />
		</c:if>
		</td>
	</c:if>
	<c:if test="${tdDispYn2}">
	<td class="head_lt"><u:msg titleId="cols.readCnt" alt="조회수" /></td>
	<td colspan="${colspan2}" class="body_lt">
		<c:if test="${baBrdBVo.readHstUseYn == 'Y'}">
		<a href="javascript:readHst('${bbBullLVo.bullId}');"><u:out value="${bbBullLVo.readCnt}" type="number" /></a>
		</c:if>
		<c:if test="${baBrdBVo.readHstUseYn != 'Y'}">
		<u:out value="${bbBullLVo.readCnt}" type="number" />
		</c:if>
		</td>
	</c:if>
	</tr>
	</c:if>

	<c:if test="${baBrdBVo.optMap.bbTgtDispYn eq 'Y' || baBrdBVo.optMap.bbOptYn eq 'Y' }">
	<u:set test="${baBrdBVo.optMap.bbOptYn eq 'Y'}" var="colspan" value="1" elseValue="3" />
	<tr>
	<c:if test="${bbTgtDispYn && baBrdBVo.optMap.bbTgtDispYn eq 'Y'}">
	<td class="head_lt"><u:msg titleId="bb.cols.bbTgt" alt="게시대상" /></td>
	<td colspan="${colspan}"><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td id="bbTgtDeptHidden">	
			<c:if test="${bullTgtDeptVoList != null}">
				<c:forEach items="${bullTgtDeptVoList}" var="baBullTgtDVo" varStatus="status">
				<input name="orgId" type="hidden" value="${baBullTgtDVo.tgtId}" />
				<input name="withSubYn" type="hidden" value="${baBullTgtDVo.withSubYn}" />
				</c:forEach>
			</c:if>
			</td>
		<td id="bbTgtDept" style="display: none;" class="body_lt"></td>
		<td>
			<c:if test="${bullTgtDeptVoList != null}">
			<u:buttonS titleId="bb.btn.dept" alt="부서" onclick="openMuiltiOrgWithSub('view')" />
			</c:if>
		</td>
		<td id="bbTgtUserHidden">
			<c:if test="${bullTgtUserVoList != null}">
				<c:forEach items="${bullTgtUserVoList}" var="baBullTgtDVo" varStatus="status">
				<input name="userUid" type="hidden" value="${baBullTgtDVo.tgtId}" />
				</c:forEach>
			</c:if>
			</td>
		<td id="bbTgtUser" style="display: none;" class="body_lt"></td>
		<td>
			<c:if test="${bullTgtUserVoList != null}">
			<u:buttonS titleId="bb.btn.user" alt="사용자" onclick="openMuiltiUser('view')" />
			</c:if>
		</td>
		<td>
			<c:if test="${bullTgtDeptVoList == null && bullTgtUserVoList == null}">
				&nbsp;<u:msg titleId="cm.option.publ" alt="공개"/>
			</c:if>
		</td>
		</tr>
		</tbody></table></td>
	</c:if>
	<u:set test="${bbTgtDispYn && baBrdBVo.optMap.bbTgtDispYn eq 'Y'}" var="colspan" value="1" elseValue="3" />
	<c:if test="${baBrdBVo.optMap.bbOptYn eq 'Y' }">
	<td class="head_lt"><u:msg titleId="cols.bullRezvDt" alt="게시예약일" /></td>
	<td colspan="${colspan}" class="body_lt"><u:out value="${bbBullLVo.bullRezvDt}" type="longdate" /></td>
	</c:if>
	</tr>
	</c:if>


	<c:if test="${baColmDispDVoMap['REG_DT'].readDispYn == 'Y' || baColmDispDVoMap['MOD_DT'].readDispYn == 'Y'}">
	<u:set test="${baColmDispDVoMap['REG_DT'].readDispYn == 'Y' && baColmDispDVoMap['MOD_DT'].readDispYn == 'Y'}" var="colspan" value="1" elseValue="3" />
	<tr>
	<c:if test="${baColmDispDVoMap['REG_DT'].readDispYn == 'Y'}">
	<td class="head_lt"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	<td colspan="${colspan}" class="body_lt"><u:out value="${bbBullLVo.regDt}" type="longdate" /></td>
	</c:if>
	<c:if test="${baColmDispDVoMap['MOD_DT'].readDispYn == 'Y'}">
	<td class="head_lt"><u:msg titleId="cols.modDt" alt="수정일시" /></td>
	<td colspan="${colspan}" class="body_lt"><u:out value="${bbBullLVo.modDt}" type="longdate" /></td>
	</c:if>
	</tr>
	</c:if>

	<!-- 게시물사진 -->
	<c:if test="${baBrdBVo.photoYn == 'Y'}">
	<tr>
	<td class="head_lt"><u:msg alt="사진 선택" titleId="bb.cols.photo" /></td>
	<td colspan="3">
		<c:if test="${bbBullLVo.photoVo != null}">
		<c:set var="maxWdth" value="800" />
		<u:set test="${bbBullLVo.photoVo.imgWdth <= maxWdth}" var="imgWdth" value="${bbBullLVo.photoVo.imgWdth}" elseValue="${maxWdth}" />
		<div style="padding: 4px;"><img src="${_cxPth}${bbBullLVo.photoVo.savePath}" width="${imgWdth}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"'></div>
		</c:if>
		</td>
	</tr>
	</c:if>
	<c:if test="${bbBullLVo.replyOrdr == 0  }"><c:set var="map" value="${bbBullLVo.exColMap}" scope="request" />
		<c:set var="maxCnt" value="2"/>
		<c:set var="dispCnt" value="1"/>
		<c:set var="maxTrCnt" value="0"/>
		<c:set var="maxLen" value="${fn:length(baColmDispDVoList) }"/>
		<!-- 확장컬럼 -->
		<c:forEach items="${baColmDispDVoList}" var="baColmDispDVo" varStatus="colStatus">
			<c:set var="colmVo" value="${baColmDispDVo.colmVo}" />
			<c:set var="colmNm" value="${colmVo.colmNm.toLowerCase()}" />
			<c:set var="colmTyp" value="${colmVo.colmTyp}" />
			<c:set var="colmTypVal" value="${colmVo.colmTypVal}" />
			<u:set var="colsWdthVa" test="${!empty baColmDispDVo.colsWdthVa }" value="${baColmDispDVo.colsWdthVa }" elseValue="0.5"/>
			<u:set var="nextColsWdthVa" test="${colStatus.count<maxLen && !empty baColmDispDVoList[colStatus.index+1].colsWdthVa }" value="${baColmDispDVoList[colStatus.index+1].colsWdthVa }" elseValue="0.5"/>
		<c:if test="${baColmDispDVo.readDispYn == 'Y' && colmVo.exColmYn == 'Y'}">
		<c:if test="${dispCnt==1 || dispCnt%maxCnt == 1 }"><tr><c:set var="maxTrCnt" value="${maxTrCnt+1 }"/></c:if>
		<u:set var="colspan" test="${(dispCnt == 1 || dispCnt<maxCnt || dispCnt%maxCnt > 0) 
		&& ( colmTyp eq 'TEXTAREA' || fn:length(baColmDispDVoList) == colStatus.count || colsWdthVa=='1' || nextColsWdthVa=='1')}" value="colspan='${(((maxCnt*maxTrCnt)-dispCnt)*2)+1 }'" elseValue=""/>
		<td class="head_lt">${colmVo.rescNm}</td>
		<td class="body_lt wordbreak" ${colspan }>
			<c:if test="${colmTyp == 'TEXT' || colmTyp == 'TEXTAREA' || colmTyp == 'PHONE' || colmTyp == 'CALENDAR'}">
				<u:out value="${bbBullLVo.getExColm(colmVo.colmNm)}" />
			</c:if>
			<c:if test="${colmTyp == 'CALENDARTIME'}">
			<u:out value="${bbBullLVo.getExColm(colmVo.colmNm)}" type="longdate"/>
			</c:if>
			<c:if test="${fn:startsWith(colmTyp,'CODE')}">
				<u:set test="${cdListIndex == null}" var="cdListIndex" value="0" elseValue="${cdListIndex + 1}" />
				<c:if test="${colmTyp == 'CODE' || colmTyp == 'CODERADIO'}">					
					<c:forEach items="${cdList[cdListIndex]}" var="cd" varStatus="status">
					<c:if test="${cd.cdId == bbBullLVo.getExColm(colmVo.colmNm)}">${cd.rescNm}</c:if>
					</c:forEach>
				</c:if>
				<c:if test="${colmTyp == 'CODECHK'}"><c:set var="chkIndex"/>
					<c:forEach items="${cdList[cdListIndex]}" var="cd" varStatus="status"><c:set var="checked" value="N" 
				/><c:forTokens var="chkId" items="${bbBullLVo.getExColm(colmVo.colmNm)}" delims=","><c:if test="${chkId==cd.cdId }"><c:set var="checked" value="Y" 
				/></c:if></c:forTokens><c:if test="${checked eq 'Y'}"><c:set var="chkIndex" value="${empty chkIndex ? 0 : chkIndex+1 }"/><c:if test="${chkIndex>0 }">,</c:if>${cd.rescNm}</c:if></c:forEach></c:if>
			</c:if>
			<c:if test="${colmTyp == 'USER' || colmTyp == 'DEPT'}"><div id="selectListArea_${colmNm }" style="min-height:40px;"><u:convertMap srcId="map" attId="${colmNm }MapList" var="mapList" /><c:if test="${!empty mapList }"><c:forEach 
		var="mapVo" items="${mapList }" varStatus="status">
		<div class="ubox"><dl><dd 
		class="title_view"><c:if test="${colmTyp == 'USER' }"><a href="javascript:viewUserPop('${mapVo.id }');">${mapVo.rescNm }</a></c:if
		><c:if test="${colmTyp == 'DEPT' }">${mapVo.rescNm }</c:if></dd></dl></div>
		</c:forEach></c:if></div></c:if>
			</td>
		<c:if test="${colStatus.count == fn:length(baColmDispDVoList) }"></tr></c:if>
		<c:if test="${!empty colspan && colStatus.count < fn:length(baColmDispDVoList) }"><c:set var="dispCnt" value="${dispCnt+(maxCnt-1) }"/></c:if>
		<c:set var="dispCnt" value="${dispCnt+1 }"/>
		</c:if>
		</c:forEach>
	</c:if>
	<c:if test="${empty isOnlyMd || isOnlyMd==false}">
	<c:if test="${baColmDispDVoMap['CONT'].readDispYn == 'Y'}">
	<tr>
	<td colspan="4" ><div style="overflow:auto;" class="editor printNoScroll" id="snsCont"><u:clob lobHandler="${lobHandler }"/></div></td>
	</tr>
	</c:if></c:if>
	<c:if test="${baBrdBVo.optMap.fileUploadYn eq 'Y'}">
	<tr>
	<td class="head_lt"><u:msg titleId="cols.attFile" alt="첨부파일" /></td>
	<td colspan="3">
		<c:if test="${!empty fileVoList }"><u:files id="${filesId}" fileVoList="${fileVoList}" module="bb" mode="view" urlParam="brdId=${brdId}" /></c:if>
	</td>
	</tr>
	</c:if>
	<c:set var="trDispYn" value="false" />
	<c:set var="tdDispYn1" value="false" />
	<c:set var="tdDispYn2" value="false" />
	<c:if test="${etcDIspYn && baBrdBVo.recmdUseYn == 'Y' && colmMap.recmdCnt.readDispYn=='Y'}">
		<c:set var="trDispYn" value="true" />
		<c:set var="tdDispYn1" value="true" />
	</c:if>
	<c:if test="${etcDIspYn && baBrdBVo.favotYn == 'Y' && colmMap.prosCnt.readDispYn=='Y' && colmMap.consCnt.readDispYn=='Y'}">
		<c:set var="trDispYn" value="true" />
		<c:set var="tdDispYn2" value="true" />
	</c:if>
	<u:set test="${tdDispYn1 && !tdDispYn2}" var="colspan1" value="3" elseValue="1" />
	<u:set test="${!tdDispYn1 && tdDispYn2}" var="colspan2" value="3" elseValue="1" />

	<c:if test="${trDispYn}">
	<tr>
		<c:if test="${tdDispYn1}">
		<td class="head_lt"><u:msg titleId="cols.recmdCnt" alt="추천수" /></td>
		<td colspan="${colspan1}"><table border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			<td id="recmdCntTd" class="body_lt">${bbBullLVo.recmdCnt}</td>
			<c:if test="${recmdHstExist == false}">
			<td><u:buttonS titleId="bb.btn.recmd" alt="추천" onclick="recmdBull();" auth="R" /></td>
			</c:if>
			</tr>
			</tbody></table></td>
		</c:if>
		<c:if test="${tdDispYn2}">
		<td class="head_lt"><u:msg titleId="cols.favot" alt="찬반투표" /></td>
		<td colspan="${colspan2}"><table border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			<td class="body_lt"><u:msg titleId="cols.prosCnt" alt="찬성수" />: <span id="prosCntSpan">${bbBullLVo.prosCnt}</span></td>
			<c:if test="${favotHstExist == false}">
			<td><u:buttonS titleId="bb.btn.pros" alt="찬성" onclick="favotBull('F');" auth="R" /></td>
			</c:if>
			<td class="body_lt"><u:msg titleId="cols.consCnt" alt="반대수" />: <span id="consCntSpan">${bbBullLVo.consCnt}</span></td>
			<c:if test="${favotHstExist == false}">
			<td><u:buttonS titleId="bb.btn.cons" alt="반대" onclick="favotBull('A');" auth="R" /></td>
			</c:if>
			</tr>
			</tbody></table></td>
		</c:if>
	</tr>
	</c:if>
	</tbody>
</table>
</div>
<c:if test="${!empty isOnlyMd && isOnlyMd==true}">
<u:blank />
<jsp:include page="/WEB-INF/jsp/wf/works/viewWorks.jsp" flush="false" />
</c:if>
<% // 점수주기 %>
<c:if test="${etcDIspYn && baBrdBVo.screUseYn == 'Y' && colmMap.scre.readDispYn=='Y'}">
<u:boxArea className="gbox" outerStyle="height:47px;overflow:hidden;" innerStyle="NO_INNER_IDV" noBottomBlank="true">
<table border="0" cellpadding="0" cellspacing="0"><tbody>
<tr>
<td colspan="2" class="body_lt">※ <u:msg titleId="bb.cols.saveScre" alt="점수주기" /> - <u:msg titleId="bb.jsp.viewBull.saveScre" alt="이 글에 대한 점수를 남겨 주세요." /></td>
</tr>

<tr>
<td style="padding: 0 0 0 15px;"><u:checkArea>
	<u:radio name="scre" value="1" title="★☆☆☆☆" inputClass="bodybg_lt" textStyle="color: #777;" />
	<u:radio name="scre" value="2" title="★★☆☆☆" inputClass="bodybg_lt" textStyle="color: #777;" />
	<u:radio name="scre" value="3" title="★★★☆☆" inputClass="bodybg_lt" textStyle="color: #777;" />
	<u:radio name="scre" value="4" title="★★★★☆" inputClass="bodybg_lt" textStyle="color: #777;" />
	<u:radio name="scre" value="5" title="★★★★★" inputClass="bodybg_lt" textStyle="color: #777;" />
	</u:checkArea></td>
<td>
	<c:if test="${screHstExist == false}">
	<u:buttonS titleId="bb.btn.saveScre" alt="점수주기" onclick="saveScre();" auth="R" />
	</c:if>
</td>
<td><u:buttonS titleId="bb.btn.viewScre" alt="점수내역" onclick="viewScre();" /></td>
</tr>
</tbody></table>
</u:boxArea>
</c:if>

<% // 한줄답변 %>
<c:if test="${baBrdBVo.cmtYn == 'Y'}">
	<div class="blank"></div>
	<div style="height: 25px;"><a href="javascript:" onclick="$('#listCmtFrm').toggle();"><u:msg titleId="cols.cmt" alt="한줄답변" /> (${bbBullLVo.cmtCnt})</a></div>
	<iframe id="listCmtFrm" name="listCmtFrm" src="./listCmtFrm.do?menuId=${menuId}&brdId=${brdId}&bullId=${param.bullId}" style="width:100%;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</c:if>

<u:blank />

<c:if test="${baBrdBVo.replyYn == 'Y' && fn:length(replyBullList) > 1}">
<% // 관련글 %>
<div class="titlearea">
	<div class="tit_left">
	<dl>
	<dd class="txt">※ <span class="red_txt">${fn:length(replyBullList)}</span> <u:msg titleId="bb.jsp.viewBull.tx01" alt="개의 관련글이 있습니다" />
		<u:icoCurr /> <u:msg titleId="bb.jsp.viewBull.tx02" alt="표시의 글은 현재글입니다." /></dd>
	</dl>
	</div>
</div>

<% // 관련글 목록 %>
<u:listArea id="replyList" colgroup="6%,,10%,15%">
	<tr>
	<td class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
	<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
	<td class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	</tr>

<c:if test="${fn:length(replyBullList) == 0}">
	<tr>
	<td class="nodata" colspan="4"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(replyBullList) > 0}">
	<c:forEach items="${replyBullList}" var="bullVo" varStatus="status">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="listicon_ct"><u:icoCurr display="${bbBullLVo.bullId == bullVo.bullId}" /></td>
	<td class="body_lt">
		<u:icon type="indent" display="${bullVo.replyDpth > 0}" repeat="${bullVo.replyDpth - 1}" />
		<u:icon type="reply" display="${bullVo.replyDpth > 0}" />
		<u:icon type="new" display="${bullVo.newYn == 'Y'}" />
		<u:icon type="notc" display="${bullVo.notcYn == 'Y'}" />
		<u:set test="${bullVo.secuYn == 'Y'}" var="viewBull" value="openLogin" elseValue="viewBull" />
		<a href="javascript:${viewBull}('${bullVo.bullId}');" title="${bullVo.subj}"><u:out value="${bullVo.subj}" maxLength="80" /></a>
		<c:if test="${baBrdBVo.cmtYn == 'Y'}"><span style="font-size: 10px;">(<u:out value="${bullVo.cmtCnt}" type="number" />)</span></c:if>
		</td>
	<td class="body_ct"><a href="javascript:viewUserPop('${bullVo.regrUid}');">${bullVo.regrNm}</a></td>
	<td class="body_ct"><u:out value="${bullVo.regDt}" type="longdate" /></td>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>
</c:if>

<% // 이전글 다음글 %>
<c:if test="${prevNextYn == true}">
<div class="listarea notPrint">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
	<colgroup><col width="6%"/><col width="*"/><col width="10%"/><col width="15%"/></colgroup>
<c:if test="${prevBullVo == null}">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="listicon_ct"><u:icon type="prev" /></td>
	<td class="body_lt"><u:msg titleId="bb.jsp.viewBull.prevNotExists" alt="이전글이 존재하지 않습니다." /></td>
	<td class="body_ct">&nbsp;</td>
	<td class="body_ct">&nbsp;</td>
	</tr>
</c:if>
<c:if test="${prevBullVo != null}">
	<u:set test="${prevBullVo.secuYn == 'Y'}" var="viewBull" value="openLogin" elseValue="viewBull" />
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="listicon_ct"><a href="javascript:${viewBull}('${prevBullVo.bullId}','${prevBullVo.brdId }');"><u:icon type="prev" /></a></td>
	<td class="body_lt"><a href="javascript:${viewBull}('${prevBullVo.bullId}','${prevBullVo.brdId }');" title="${prevBullVo.subj}"><u:out value="${prevBullVo.subj}" maxLength="80" /></a>
		<c:if test="${baBrdBVo.cmtYn == 'Y'}"><span style="font-size: 10px;">(<u:out value="${prevBullVo.cmtCnt}" type="number" />)</span></c:if>
		</td>
	<td class="body_ct"><a href="javascript:viewUserPop('${prevBullVo.regrUid}');">${prevBullVo.regrNm}</a></td>
	<td class="body_ct"><u:out value="${prevBullVo.regDt}" type="longdate" /></td>
	</tr>
</c:if>

<c:if test="${nextBullVo == null}">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="listicon_ct"><u:icon type="next" /></td>
	<td class="body_lt"><u:msg titleId="bb.jsp.viewBull.nextNotExists" alt="다음글이 존재하지 않습니다." /></td>
	<td class="body_ct">&nbsp;</td>
	<td class="body_ct">&nbsp;</td>
	</tr>
</c:if>
<c:if test="${nextBullVo != null}">
	<u:set test="${nextBullVo.secuYn == 'Y'}" var="viewBull" value="openLogin" elseValue="viewBull" />
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="listicon_ct"><a href="javascript:${viewBull}('${nextBullVo.bullId}','${nextBullVo.brdId }');"><u:icon type="next" /></a></td>
	<td class="body_lt"><a href="javascript:${viewBull}('${nextBullVo.bullId}','${nextBullVo.brdId }');" title="${nextBullVo.subj}"><u:out value="${nextBullVo.subj}" maxLength="80" /></a>
		<c:if test="${baBrdBVo.cmtYn == 'Y'}"><span style="font-size: 10px;">(<u:out value="${nextBullVo.cmtCnt}" type="number" />)</span></c:if>
		</td>
	<td class="body_ct"><a href="javascript:viewUserPop('${nextBullVo.regrUid}');">${nextBullVo.regrNm}</a></td>
	<td class="body_ct"><u:out value="${nextBullVo.regDt}" type="longdate" /></td>
	</tr>
</c:if>
</table>
</div>
</c:if>

<% // 하단 버튼 %>
<c:if test="${empty isOpen}">
<u:buttonArea topBlank="true">
	<c:if test="${empty param.compId }">
		<c:if test="${isSysAdmin == true}"><u:button titleId="bb.btn.regChn" alt="등록자변경" onclick="setRegChn();" /></c:if>
		<c:if test="${colmMap.cont.readDispYn eq 'Y' }">
		<u:internalIp><c:if test="${!empty bbBullLVo.bullId && !empty sendDocYn && sendDocYn eq 'Y' && sessionScope.userVo.hasMnuGrpMdRidOf('DM')}"><u:button titleId="dm.btn.sendDoc" alt="문서보내기" onclick="setSendWritePop();" auth="W" ownerUid="${bbBullLVo.regrUid}"/></c:if></u:internalIp>
		<%-- <u:admin><c:if test="${baBrdBVo.photoYn != 'Y' and bbBullLVo.bullPid == null}">
			<u:button titleId="bb.btn.setPopup" alt="팝업설정" onclick="setPopupPop();" />
		</c:if></u:admin> --%>
		<c:if test="${baBrdBVo.photoYn != 'Y' and bbBullLVo.bullPid == null}">
		<u:button titleId="bb.btn.setPopup" alt="팝업설정" onclick="setPopupPop();" auth="${!empty isLginPop && isLginPop==true ? 'W' : 'A' }"/>
		</c:if>
		<u:button titleId="bb.btn.bodySave" alt="본문저장" onclick="saveBody();" />
		<%-- <u:email titleId="cm.btn.emailSend" alt="이메일발송" param="{bullId:'${bbBullLVo.bullId}', brdId:'${baBrdBVo.brdId}'}" /> --%>
	</c:if>
	<c:if test="${sessionScope.userVo.hasMnuGrpMdRidOf('MAIL') && mailEnable == 'Y'}">
		<u:internalIp><u:button titleId="cm.btn.emailSend" alt="이메일발송" href="javascript:;" onclick="emailSendPop({bullId:'${bbBullLVo.bullId}', brdId:'${baBrdBVo.brdId}'},'${menuId }');"/></u:internalIp>
	</c:if>
	</c:if>
	<c:if test="${empty param.compId }">
		<u:button titleId="bb.btn.bullCopy" alt="게시물복사" href="javascript:selectBb('copyBull');" auth="W" />
		<c:if test="${listPage == 'listBull'}">
			<c:if test="${baBrdBVo.replyYn == 'Y'}">
			<u:button titleId="bb.btn.reply" alt="답변" onclick="setReply();" auth="W" />
			</c:if>
		</c:if>
		
		<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" auth="R" />
	</c:if>
	<u:set var="auth" test="${!empty eqCompYn && eqCompYn eq 'N'}" value="SYS" elseValue="M"/>
	<u:button titleId="cm.btn.mod" alt="수정" onclick="modBull();" auth="${auth }" ownerUid="${bbBullLVo.regrUid}" />
	<u:set var="auth" test="${!empty eqCompYn && eqCompYn eq 'N'}" value="SYS" elseValue="A"/>
	<u:button titleId="cm.btn.del" alt="삭제" onclick="delBull();" auth="${auth }" ownerUid="${bbBullLVo.regrUid}" />
	<u:button titleId="cm.btn.list" alt="목록" onclick="goList();" />
</u:buttonArea></c:if>
<c:if test="${!empty isOpen && isOpen==true }">
<u:buttonArea>
	<u:button titleId="cm.btn.close" alt="닫기" href="javascript:;" onclick="window.open('about:blank','_self').close();"/>		
</u:buttonArea>
</c:if>
<form id="saveBodyForm" >
	<input type="hidden" name="brdId" value="${baBrdBVo.brdId}"/>
	<input type="hidden" name="bullId" value="${bbBullLVo.bullId}"/>
</form>
<form id="saveSendDocForm" >
	<u:input type="hidden" id="brdId" value="${baBrdBVo.brdId}"/>
	<u:input type="hidden" id="bullId" value="${bbBullLVo.bullId}"/>
</form>
</div>

