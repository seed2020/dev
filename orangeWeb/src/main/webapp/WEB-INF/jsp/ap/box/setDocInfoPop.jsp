<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--<%
// 분류정보 - 버튼클릭 - 팝업 호출 %>
function openClsInfo() {
	var clsInfoId = $("#setDocInfoForm").find("#clsInfoId").val();
	dialog.open('setClsInfoDialog','<u:msg titleId="cols.clsInfo" alt="분류정보" />','./treeOrgClsPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}'+(clsInfoId!='' && clsInfoId!=null ? '&clsInfoId='+clsInfoId : ''));
	dialog.onClose('setDocInfoDialog', function(){dialog.close("setClsInfoDialog");});
}<%
// 문서관리 - 버튼클릭 - 팝업 %>
function openSendToDM(){
	var param = new ParamMap();
	var sendToDm = $('#setDocInfoForm #sendToDm').val();
	if(sendToDm!=null && sendToDm!=''){
		var obj = JSON.parse(sendToDm);
		for(var k in obj){
			param.put(k, obj[k]);
		}
	} else {
		param.put('ownrUid', '${sessionScope.userVo.userUid}');
	}
	sendDocOptPop('callbackDm', param);
}<%
// 문서관리 콜백 %>
function callbackDm(data){
	var $form = $('#setDocInfoForm');
	var jsonObject = JSON.parse(data);
	$form.find('#sendToDmNm').val(jsonObject.fldNm);
	$form.find('#sendToDm').val(data);
}<%
// 시행범위(radio) 클릭 %>
function toggleEnfc(value){
	$("#setDocInfoForm").find("#enfcScopTr, #sendrNmTr, #enfcDocKeepPrdCdTd1, #enfcDocKeepPrdCdTd2").css('display', value=='intro' ? 'none' : '');
	dialog.resize("setDocInfoDialog");
}<%
// 문서번호 세팅 %>
function setDocNo(){
	var $form = $("#setDocInfoForm");
	var useCls = "${optConfigMap.docNoCat=='use' ? 'Y' : ''}";
	var clsInfoId = $form.find("#clsInfoId").val();
	var docLangTypCd = $form.find("#docLangTypCd").val();
	if(useCls=='Y' && (clsInfoId=='' || clsInfoId==null)) return;<%// 문서번호에 분류를 사용하는데 세팅이 안되어 있을 때 %>
	callAjax("./getDocNoAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {clsInfoId:clsInfoId, docLangTypCd:docLangTypCd}, function(data){
		$form.find("#docNo").val(data.docNo);
	});
}
<%
// 확인 버튼 클릭 %>
function setApDocInfo(noMsg){<%
	// 시행범위 변경에 따른 수신처 확인 %>
	if(!confirmEnfcScopCd(noMsg)){
		return;
	}
	var $form = $("#setDocInfoForm");
	
	if(noMsg){
		noAlertMsg = true;
		if(!validator.validate($form[0])){
			noAlertMsg = false;
			return;
		}
		noAlertMsg = false;
	} else {
		if(!validator.validate($form[0])) return;
	}
	
	
	var param = new ParamMap().getData($form[0]);<c:if test="${optConfigMap.catEnab == 'Y'}">
	if(param.get('clsInfoId')==''){
		if(noMsg!=true) alertMsg("cm.select.check.mandatory",["#ap.jsp.setOrgEnv.tab.deptCls"]);<%//cm.select.check.mandatory="{0}"(을)를 선택해 주십시요.%>
		return;
	}</c:if><c:if test="${not empty dmEnable and optConfigMap.dmAtMaking == 'man'}">
	if($form.find('#sendToDm').val()==''){
		if(noMsg!=true) alertMsg("cm.select.check.mandatory",["#ap.jsp.dmInfo"]);<%//cm.select.check.mandatory="{0}"(을)를 선택해 주십시요.%>
		return;
	}
	</c:if>
	if(param.get("secuDocYn")==null) param.remove("docPw");<%// 비밀번호 사용안함 - 비밀번호 지움 %>
	if(param.get("docTypCd")=='intro'){<%// 문서구분 이 내부문서 면 - 시행범위, 발신명의-리소스ID 지움 %>
		param.remove("enfcScopCd");
		param.remove("sendrNmRescId");
		param.remove("sendrNmOrgId");
	} else {
		var sendrNmRescNm = $form.find("#sendrNmRescId option:selected").text();
		if(sendrNmRescNm==null || sendrNmRescNm==''){<%
			// ap.msg.notWithoutSendrNm=발신명의가 지정되지 않아서 진행 할 수 없습니다.%>
			if(noMsg!=true) alertMsg('ap.msg.notWithoutSendrNm');
			return;
		}
		param.put("sendrNmRescNm", sendrNmRescNm);<%// 발신명의-리소스명 세팅 %>
	}
	param.put("docKeepPrdNm", $form.find("#docKeepPrdCd option:selected").text());
	param.put("enfcDocKeepPrdNm", $form.find("#enfcDocKeepPrdCd option:selected").text());
	var va = param.get("docTypCd");
	if(va!=null) param.put("docTypNm", $form.find("#docTypCd"+va+"Label").text());
	va = param.get("enfcScopCd");
	if(va!=null) param.put("enfcScopNm", $form.find("#enfcScopCd"+va+"Label").text());
	va = param.get("seculCd");
	var seculNm = (va!=null && va!='none') ? $form.find("#seculCd option:selected").text() : null;
	var pwDisp = (param.get("docPw") != null && param.get("docPw")) != '' ? "[<u:msg titleId="cols.pw" alt="비밀번호" />]" : null;
	if(seculNm != null || pwDisp != null){
		if(seculNm==null) param.put("seculNm", pwDisp);
		else if(pwDisp==null) param.put("seculNm", seculNm);
		else param.put("seculNm", seculNm + " " + pwDisp);
	} else {
		param.put("seculNm", "");
	}
	setDocInfoData(param);
	dialog.close("setDocInfoDialog");
}<%
// 문서정보 - 문서 원본에 세팅 %>
function setDocInfoData(param){
	var $docDiv = $("#docDiv");
	if(param.get("enfcScopCd")==null){<%// 시행범위코드가 없으면 %>
		param.put("enfcScopNm", "");<%// 시행범위명 지움 - html의 text 제거용 %>
		$docDiv.find("#senderArea").hide();<%// 발신명의 영역 숨기기 %>
	} else {
		$docDiv.find("#senderArea").show();<%// 발신명의 영역 보이기 %>
	}
	setDocDataArea('docInfo', param);<%// 히든테그 생성, 항목지정 해당영역의 html에 표시 %>
	$docDiv.find("#headerArea #docNameViewArea").text(param.get("formNm"));<%// 양식명 세팅 %>
	if(param.get("sendrNmRescNm")!=null) $docDiv.find("#senderArea #docSenderViewArea").text(param.get("sendrNmRescNm"));<%// 발신명의 세팅 %>
	$(".homewrapper:first").attr("class", "homewrapper "+param.get("formWdthTypCd"));<%//브라우져의 인쇄넓이 세팅함 %>
	var bodyHghtPx = param.get("bodyHghtPx");
	$("#bodyHtmlViewArea .editor").css("min-height", bodyHghtPx+(bodyHghtPx=="" ? "" : "px"));<%//본문높이 세팅함 %>
}<%
// 데이터 영역에 데이터 세팅 %>
function setDocDataArea(areaId, param){
	var $dataArea = $gDocDataArea.find("#"+areaId+"Area");
	if($dataArea.length==0){
		$gDocDataArea.append("<div id='"+areaId+"Area'></div>");
		$dataArea = $gDocDataArea.find("#"+areaId+"Area");
	} else {
		$dataArea.html("");
	}
	var $itemsArea = $("#docArea div[data-name='itemsArea']");
	param.each(function(nm, va){
		$dataArea.append("<input type='hidden' name='"+nm+"' value='"+escapeValue(va)+"' />");
		$itemsArea.find("td#"+nm+"View").text(va);
	});
}<%
// 시행범위 변경 체크 - enfcScopCd:시행범위 - dom:대내, for:대외, both:대내외 %>
function confirmEnfcScopCd(noMsg){
	var enfcScopCd = $("#setDocInfoForm input[name='enfcScopCd']:checked:visible").val();
	var ckCd = enfcScopCd=='dom' ? 'for' : enfcScopCd=='for' ? 'dom' : null;
	if(ckCd != null){
		var arr=[], changeArr=[], hasCk=false;<%
		// 수신처 데이터 JSON 파싱해서 모으기 %>
		$("#docDataArea #recvDeptArea input").each(function(){
			if(this.name=='recvDept'){
				arr.push(JSON.parse(this.value));
			}
		});<%
		// 체크할(ckCd) 대내 또는 대외 가 있는지 체크 %>
		arr.each(function(index, va){
			if(ckCd==va['recvDeptTypCd']){
				hasCk = true;
			} else {
				changeArr.push(va);
			}
		});<%
		// 사용자에게 해당 대내, 대외 수신처 삭제할 것인지 묻기
		// ap.jsp.recvDept.delCfrm=시행범위를 "{0}"로 변경하시면 "{1}" 수신처가 삭제 됩니다.\n시행범위를 변경 하시겠습니까 ?
		//    ap.jsp.recvDept.dom=대내
		//    ap.jsp.recvDept.for=대외 %>
		if(hasCk){
			if(noMsg){
				setRecvDeptData(changeArr);<%// >> setDoc.jsp %>
			} else {
				if(confirmMsg("ap.jsp.recvDept.delCfrm", ["#ap.jsp.recvDept."+enfcScopCd,"#ap.jsp.recvDept."+ckCd])){
					setRecvDeptData(changeArr);<%// >> setDoc.jsp %>
				} else {
					return false;
				}
			}
		}
	}
	return true;
}<%
// 시행범위 - 변경전 정보 %>
var gAldEnfcScopCd = null;
var readyTimeoutId = null;
$(document).ready(function() {
	if(readyTimeoutId==null){
		var initAuto = "${param.initAuto}";
		if(browser.ieCompatibility || (browser.ie && browser.ver <= 9) || initAuto=='Y'){
			readyTimeoutId = window.setTimeout('timeoutReady()', 100);
		} else {
			timeoutReady();
		}
	}
});
function timeoutReady(){
	var param = getDocDataArea("docInfo");
	var $form = $("#setDocInfoForm");
	if(param != null) param.setData($form[0], null, true);
	$form.find("#docTypArea #"+param.get("docTypCd")).show();
	gAldEnfcScopCd = param.get("enfcScopCd");
	${optConfigMap.docNoCat != 'use' ? 'setDocNo();' : ''}
	var sendToDm = $form.find('#sendToDm').val();
	if(sendToDm!=null && sendToDm!=''){
		callbackDm(sendToDm);
	}
	changeSendrNmRescId();
	readyTimeoutId = null;
	
	var initAuto = "${param.initAuto}";
	if(initAuto=='Y'){
		setApDocInfo(true);
		dialog.close("setDocInfoDialog");
	}
}<%
// 대장등록 옵션 (등록/미등록) 클릭 %>
function showSkedYmd(flag){
	var area = $("#regRecLstRegYnArea");
	if(flag){
		area.find("#skedYmd1, #skedYmd2").show();
	} else {
		area.find("#skedYmd1, #skedYmd2").hide();
	}
}<%
// 발신명의 변경 %>
function changeSendrNmRescId(obj){
	var $form = $("#setDocInfoForm");
	if(obj==null){
		var rescIdObj = $form .find("#sendrNmRescId");
		if(rescIdObj.length>0) obj = rescIdObj[0];
		else return;
	}
	if(obj!=null && obj.selectedIndex >= 0){
		$form .find("#sendrNmOrgId").val($(obj.options[obj.selectedIndex]).attr('data-orgId'));
	}
}
//-->
</script>

<div style="width:600px">
<form id="setDocInfoForm">

<% // 폼 필드 %>
<u:listArea>
	<tr>
	<td width="20%" class="head_lt"><u:mandatory /><u:msg titleId="cols.subj" alt="제목" /></td>
	<td width="80%"><u:input id="docSubj" value="" titleId="cols.subj" style="width: 462px;" maxByte="400" mandatory="Y" /></td>
	</tr><c:if
	
		test="${empty param.noDocNm}">
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.formNm" alt="양식명" /></td>
	<td><u:input id="formNm" value="" titleId="cols.formNm" style="width: 462px;" maxByte="240"
		mandatory="${empty param.noDocNm ? 'Y' : ''}" readonly="${optConfigMap.notEditFormNm}" /></td>
	</tr></c:if><c:if
	
		test="${not empty param.noDocNm}"><input type="hidden" name="formNm" value="" /></c:if>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.docNo" alt="문서번호" /></td>
	<td><u:input id="docNo" value="${tempDocNo}" titleId="cols.docNo" style="width: 462px;" readonly="Y" /></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.doc.docKeepPrdNm" alt="보존연한" /></td>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td class="body_lt"><u:msg titleId="cols.makDoc" alt="기안문" /></td>
		<td><select id="docKeepPrdCd" name="docKeepPrdCd"<u:elemTitle titleId="cols.makDoc" alt="기안문" />><c:forEach
			items="${docKeepPrdCdList}" var="docKeepPrdCd">
			<option value="${docKeepPrdCd.cd}">${docKeepPrdCd.rescNm}</option></c:forEach>
			</select></td>
		<td class="width10"></td>
		<td class="body_lt" id="enfcDocKeepPrdCdTd1" style="display:none;"><u:msg titleId="cols.enfcDoc" alt="시행문" /></td>
		<td id="enfcDocKeepPrdCdTd2" style="display:none;"><select id="enfcDocKeepPrdCd" name="enfcDocKeepPrdCd"<u:elemTitle titleId="cols.enfcDoc" alt="기안문" />><c:forEach
			items="${docKeepPrdCdList}" var="docKeepPrdCd">
			<option value="${docKeepPrdCd.cd}">${docKeepPrdCd.rescNm}</option></c:forEach>
			</select></td>
		</tr>
		</tbody></table></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.ugntDoc" alt="긴급문서" /></td>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<u:radio name="ugntDocYn" value="N" titleId="ap.option.mdrt" alt="보통" inputClass="bodybg_lt" checked="true" />
		<u:radio name="ugntDocYn" value="Y" titleId="ap.option.ugnt" alt="긴급" inputClass="bodybg_lt" />
		</tr>
		</tbody></table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.secu" alt="보안" /></td>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><select id="seculCd" name="seculCd"<u:elemTitle titleId="cols.secul" alt="문서보안" />>
			<option value="none"><u:msg titleId="cm.option.noSelect" alt="선택안함" /></option><c:forEach
			items="${seculCdList}" var="seculCd">
			<option value="${seculCd.cd}">${seculCd.rescNm}</option></c:forEach>
			</select></td>
		<td class="width15"></td>
		<u:checkbox id="secuDocYn" name="secuDocYn" value="Y" titleId="cols.pw" alt="비밀번호" inputClass="bodybg_lt"
			onclick="$('#setDocInfoForm #docPw').css('display', this.checked ? '' : 'none').focus();" />
		<td><u:input type="password" id="docPw" value="" titleId="cols.pw" style="width:150px; display:none;" maxLength="20" /></td>
		</tr>
		</tbody></table>
	</td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.readScop" alt="열람범위" /></td>
	<td><u:checkArea>
		<u:radio name="allReadYn" value="N" titleId="ap.option.dept" alt="부서" inputClass="bodybg_lt" checked="true" />
		<u:radio name="allReadYn" value="Y" titleId="ap.option.all" alt="전체" inputClass="bodybg_lt" />
		</u:checkArea></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.rgstReg" alt="대장등록" /></td>
	<td><u:checkArea id="regRecLstRegYnArea">
		<u:radio name="regRecLstRegYn" value="Y" titleId="ap.option.regY" alt="등록" inputClass="bodybg_lt" checked="true" onclick="showSkedYmd(false)" />
		<u:radio name="regRecLstRegYn" value="N" titleId="ap.option.regN" alt="미등록" inputClass="bodybg_lt" onclick="showSkedYmd(true)" />
		<td width="20px"></td>
		<td class="body_lt" id="skedYmd1" style="padding-top:4px; display:none;"><u:msg titleId="ap.jsp.skedDt" alt="예약 등록일" /> : </td>
		<td id="skedYmd2" style="display:none;"><u:calendar id="regRecLstRegSkedYmd" titleId="ap.jsp.skedDt" /></td>
		</u:checkArea></td>
	</tr>
	
	<tr style="display:none;">
	<td class="head_lt"><u:msg titleId="cols.docTyp" alt="문서구분" /></td>
	<td><u:checkArea><c:forEach
		items="${docTypCdList}" var="docTypCd"><c:if test="${docTypCd.cd != 'paper'}">
		<u:radio name="docTypCd" value="${docTypCd.cd}" title="${docTypCd.rescNm}"
			inputClass="bodybg_lt" checked="${docTypCd.cd == 'intro'}"
			onclick="toggleEnfc(this.value);" /></c:if></c:forEach>
		</u:checkArea></td>
	</tr>
	
	<tr><u:cmt cmt="문서구분 - 양식에 따라 픽스 되는 형태로 변환 되어서 - 라디오는 숨기고 보이는 곳을 다시 만듬" />
	<td class="head_lt"><u:msg titleId="cols.docTyp" alt="문서구분" /></td>
	<td class="body_lt" id="docTypArea"><c:forEach
		items="${docTypCdList}" var="docTypCd"><c:if test="${docTypCd.cd != 'paper'}">
		<div id="${docTypCd.cd}" style="display:none">${docTypCd.rescNm}</div></c:if></c:forEach></td>
	</tr>
	
	<tr id="enfcScopTr" style="display:none;">
	<td class="head_lt"><u:msg titleId="cols.enfcScop" alt="시행범위" /></td>
	<td><u:checkArea><c:forEach
		items="${enfcScopCdList}" var="enfcScopCd"><c:if
			test="${optConfigMap.bothSendEnab=='Y' or enfcScopCd.cd != 'both'}">
		<u:radio name="enfcScopCd" value="${enfcScopCd.cd}" title="${enfcScopCd.rescNm}"
			inputClass="bodybg_lt" checked="${enfcScopCd.cd == 'dom'}" /></c:if></c:forEach>
		</u:checkArea></td>
	</tr>
	
	<tr id="sendrNmTr" style="display:none;">
	<td class="head_lt"><u:msg titleId="cols.sendrNm" alt="발신명의" /></td>
	<td><select id="sendrNmRescId" name="sendrNmRescId"<u:elemTitle titleId="cols.sendrNm" alt="발신명의" /> onchange="changeSendrNmRescId(this);"><c:forEach
		items="${orOrgApvDVoList}" var="orOrgApvDVo" varStatus="sendrNmStatus"><c:if 
			test="${not empty orOrgApvDVo.sendrNmRescNm}">
		<option value="${orOrgApvDVo.sendrNmRescId}" data-orgId="${orOrgApvDVo.orgId}">${orOrgApvDVo.sendrNmRescNm}</option></c:if></c:forEach>
		</select>
		<input type="hidden" id="sendrNmOrgId" name="sendrNmOrgId" value="" /></td>
	</tr>
	
	<c:if test="${optConfigMap.catEnab == 'Y'}">
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.clsInfo" alt="분류정보" /></td>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><u:input id="clsInfoNm" value="" titleId="cols.taskInfo" style="width: 200px;" readonly="readonly" />
		<u:input type="hidden" id="clsInfoId" /></td>
		<td style="padding-right: 2px;"><u:buttonS titleId="cols.clsInfo" alt="분류정보" href="javascript:openClsInfo();" /></td>
		</tr>
		</tbody></table></td>
	</tr>
	</c:if>
	
	<c:if test="${not empty dmEnable and (
		optConfigMap.dmAtMaking == 'opt' || optConfigMap.dmAtMaking == 'man')}">
	<tr>
	<td class="head_lt"><c:if
		test="${optConfigMap.dmAtMaking == 'man'}"><u:mandatory /></c:if><u:msg titleId="ap.btn.sendToDm" alt="문서관리" /></td>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><u:input id="sendToDmNm" value="" titleId="ap.btn.sendToDm" style="width: 200px;" readonly="readonly" />
		<u:input type="hidden" id="sendToDm" /></td>
		<td style="padding-right: 2px;"><u:buttonS titleId="ap.btn.sendToDm" alt="문서관리" href="javascript:openSendToDM();" /></td>
		</tr>
		</tbody></table><c:if test="${empty dmEnable or not (
		optConfigMap.dmAtMaking == 'opt' || optConfigMap.dmAtMaking == 'man')}">
	<u:input type="hidden" id="sendToDm" />
	</c:if></td>
	</tr>
	</c:if>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.docLang" alt="문서언어" /></td>
	<td><select id="docLangTypCd" name="docLangTypCd"<u:elemTitle titleId="cols.docLang" alt="문서언어" /> onchange="setDocNo();">
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
		<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" checkValue="${sessionScope.userVo.langTypCd}" />
		</c:forEach>
		</select></td>
	</tr>
	
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" href="javascript:setApDocInfo();" alt="확인" auth="W" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

<input type="hidden" name="formWdthTypCd" value="">
<input type="hidden" name="bodyHghtPx" value="">
</form>
</div>
