<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// 저장 - 버튼 클릭 %>
function saveOpt(){
	var param = new ParamMap().getData("setOptForm");
	if(param.get("catEnab")==null && param.get("docNoCat")!='notUse'){
		alertMsg("ap.jsp.setOpt.msg1");<%//ap.jsp.setOpt.msg1=분류 정보를 사용하지 않아서 문서 분류번호를 체번에 사용 할 수 없습니다.%>
		return;
	}
	var $form = $("#setOptForm");
	$form.attr('action','./transOpt.do');
	$form.attr('target','dataframe');
	$form.submit();
}<%
// 심사 - 체크박스 클릭 %>
function setCensrEnab(checked){
	var $censrLoc = $("#censrLoc");
	var $censrBx = $("#sendFromcensrBx");
	if(!checked){
		$censrLoc.attr("disabled",true);
		if($censrBx.is(":checked")){
			$("#sendFromtoSendBx").checkInput(true);
		}
		$censrBx.attr("disabled",true);
	} else {
		$censrLoc.attr("disabled",false);
		$censrBx.attr("disabled",false);
	}
	$censrLoc.uniform.update();
	$censrBx.uniform.update();
}<%
// 분류 정보 사용 - 체크박스 클릭 %>
function enableCatEnab(checked){
	$("#docNoCat").val("notUse").attr("disabled", !checked).uniform.update();
}<%
// 반려된 문서 등록대장에 저장 - 클릭 %>
function clickKeepRejtDoc(checked){
	$("input[type='checkbox'][name='delRejtBox']").attr('disabled', checked).uniform.update();
}<%
// 자동발송 %>
function setAutoSend(chkd){
	changeCheckEnable('autoOfcSealY', !chkd);<%// 관인 자동 날인 - 관인/서명인 %>
	changeCheckEnable('autoSendWithRefDocY', chkd);<%// 참조문서 포함  - 자동발송 %>
	changeCheckEnable('autoSendSignY', chkd);<%// 발신명의 날인 포함 - 자동발송 %>
}<%
// 의견 저장 %>
function setSaveOpin(chkd){
	changeCheckEnable('saveOpinMailY', chkd);<%// 의견 저장 메일 알림 %>
}<%
// 결재 비밀번호 사용 안함 %>
function setNotAlwApvPw(chkd){
	changeCheckEnable('noMakPwY', !chkd);<%// 기안 비밀번호 사용 안함 %>
}<%
// 체크박스 enable 변경 %>
function changeCheckEnable(id, enable){
	$('#'+id).attr('disabled', !enable).uniform.update();
}<%--
// 대외공문 --%>
function openExtDocPop(){
	dialog.open("setExtDocDialog", '<u:msg titleId="ap.cfg.extDoc" alt="대외공문" />', "./setExtDocPop.do?menuId=${menuId}");
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="ap.jsp.setOpt.title" alt="옵션관리" />

<form id="setOptForm" method="post">
<input type="hidden" name="menuId" value="${menuId}" />

<u:title titleId="ap.jsp.setOpt.dispOpt" alt="표시 정보 옵션" type="small" />
<u:listArea>

	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="ap.cfg.signAreaTitl" alt="서명란 직위 표시 방법"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="bodyip_lt"><strong><u:msg titleId="cols.user" alt="사용자"/></strong></td>
		<td class="width5"></td>
		<td><select id="signAreaUserTitl" name="signAreaUserTitl">
			<u:option value="posit" termId="or.term.posit" alt="직위" selected="${optConfigMap.signAreaUserTitl eq 'posit'}"/>
			<u:option value="title" termId="or.term.title" alt="직책" selected="${optConfigMap.signAreaUserTitl eq 'title'}"/>
			<u:option value="name" titleId="or.cols.name" alt="성명" selected="${optConfigMap.signAreaUserTitl eq 'name'}"/>
			<u:option value="deptNm" titleId="or.cols.deptNm" alt="부서명" selected="${optConfigMap.signAreaUserTitl eq 'deptNm'}"/>
			<u:option value="deptAbs" titleId="or.cols.deptAbs" alt="부서약어" selected="${optConfigMap.signAreaUserTitl eq 'deptAbs'}"/>
			<option value="namePosit"<c:if test="${optConfigMap.signAreaUserTitl eq 'namePosit'}"> selected="selected"</c:if>><u:msg titleId="or.cols.name" alt="성명"/>(<u:term termId="or.term.posit" alt="직위"/>)</option>
			<option value="nameTitle"<c:if test="${optConfigMap.signAreaUserTitl eq 'nameTitle'}"> selected="selected"</c:if>><u:msg titleId="or.cols.name" alt="성명"/>(<u:term termId="or.term.title" alt="직책"/>)</option>
			<option value="nameDeptNm"<c:if test="${optConfigMap.signAreaUserTitl eq 'nameDeptNm'}"> selected="selected"</c:if>><u:msg titleId="or.cols.name" alt="성명"/>(<u:msg titleId="or.cols.deptNm" alt="부서명"/>)</option>
			<option value="nameDeptAbs"<c:if test="${optConfigMap.signAreaUserTitl eq 'nameDeptAbs'}"> selected="selected"</c:if>><u:msg titleId="or.cols.name" alt="성명"/>(<u:msg titleId="or.cols.deptAbs" alt="부서약어"/>)</option>
			<u:option value="apvTyp" titleId="ap.jsp.apvTyp" alt="결재구분" selected="${optConfigMap.signAreaUserTitl eq 'apvTyp'}"/>
			</select></td>
		<td class="width20"></td>
		<td class="bodyip_lt"><strong><u:msg titleId="cols.dept" alt="부서"/></strong></td>
		<td class="width5"></td>
		<td><select id="signAreaDeptTitl" name="signAreaDeptTitl">
			<u:option value="posit" termId="or.term.posit" alt="직위" selected="${optConfigMap.signAreaDeptTitl eq 'posit'}"/>
			<u:option value="title" termId="or.term.title" alt="직책" selected="${optConfigMap.signAreaDeptTitl eq 'title'}"/>
			<u:option value="name" titleId="or.cols.name" alt="성명" selected="${optConfigMap.signAreaDeptTitl eq 'name'}"/>
			<u:option value="deptNm" titleId="or.cols.deptNm" alt="부서명" selected="${optConfigMap.signAreaDeptTitl eq 'deptNm'}"/>
			<u:option value="deptAbs" titleId="or.cols.deptAbs" alt="부서약어" selected="${optConfigMap.signAreaDeptTitl eq 'deptAbs'}"/>
			<option value="deptNmPosit"<c:if test="${optConfigMap.signAreaDeptTitl eq 'deptNmPosit'}"> selected="selected"</c:if>><u:msg titleId="or.cols.deptNm" alt="부서명"/>(<u:term termId="or.term.posit" alt="직위"/>)</option>
			<option value="deptNmTitle"<c:if test="${optConfigMap.signAreaDeptTitl eq 'deptNmTitle'}"> selected="selected"</c:if>><u:msg titleId="or.cols.deptNm" alt="부서명"/>(<u:term termId="or.term.title" alt="직책"/>)</option>
			<option value="deptNmName"<c:if test="${optConfigMap.signAreaDeptTitl eq 'deptNmTitle'}"> selected="selected"</c:if>><u:msg titleId="or.cols.deptNm" alt="부서명"/>(<u:msg titleId="or.cols.name" alt="성명"/>)</option>
			<option value="deptAbsPosit"<c:if test="${optConfigMap.signAreaDeptTitl eq 'deptAbsPosit'}"> selected="selected"</c:if>><u:msg titleId="or.cols.deptAbs" alt="부서약어"/>(<u:term termId="or.term.posit" alt="직위"/>)</option>
			<option value="deptAbsTitle"<c:if test="${optConfigMap.signAreaDeptTitl eq 'deptAbsTitle'}"> selected="selected"</c:if>><u:msg titleId="or.cols.deptAbs" alt="부서약어"/>(<u:term termId="or.term.title" alt="직책"/>)</option>
			<option value="deptAbsName"<c:if test="${optConfigMap.signAreaDeptTitl eq 'deptAbsTitle'}"> selected="selected"</c:if>><u:msg titleId="or.cols.deptAbs" alt="부서약어"/>(<u:msg titleId="or.cols.name" alt="성명"/>)</option>
			<u:option value="apvTyp" titleId="ap.jsp.apvTyp" alt="결재구분" selected="${optConfigMap.signAreaDeptTitl eq 'apvTyp'}"/>
			</select></td>
		<td class="width5"></td>
		</tr>
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.signAreaSign" alt="서명란 날인 방법"/></td>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select id="signAreaSign" name="signAreaSign">
			<u:option value="psn" titleId="ap.cfg.psn" alt="부서약어" selected="${optConfigMap.signAreaSign eq 'psn'}"/>
			<c:forEach items="${signMthdCdList}" var="signMthdCd" varStatus="status">
			<u:option value="${signMthdCd.cd}" title="${signMthdCd.rescNm}" selected="${optConfigMap.signAreaSign eq signMthdCd.cd}"
			/></c:forEach>
			</select></td>
		<td class="width5"></td>
		</tr>
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.signAreaDt" alt="서명란 날짜 표시 방법"/></td>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select id="signAreaDt" name="signAreaDt">
			<option value="yyyy-MM-dd HH:mm:ss"<c:if test="${optConfigMap.signAreaDt eq 'yyyy-MM-dd HH:mm:ss'}"> selected="selected"</c:if>>YYYY-MM-DD HH:MM:SS</option>
			<option value="yyyy-MM-dd"<c:if test="${optConfigMap.signAreaDt eq 'yyyy-MM-dd'}"> selected="selected"</c:if>>YYYY-MM-DD</option>
			<option value="yy-MM-dd"<c:if test="${optConfigMap.signAreaDt eq 'yy-MM-dd'}"> selected="selected"</c:if>>YY-MM-DD</option>
			<option value="MM-dd"<c:if test="${optConfigMap.signAreaDt eq 'MM-dd'}"> selected="selected"</c:if>>MM-DD</option>
			<option value="yyyy/MM/dd"<c:if test="${optConfigMap.signAreaDt eq 'yyyy/MM/dd'}"> selected="selected"</c:if>>YYYY/MM/DD</option>
			<option value="yy/MM/dd"<c:if test="${optConfigMap.signAreaDt eq 'yy/MM/dd'}"> selected="selected"</c:if>>YY/MM/DD</option>
			<option value="MM/dd"<c:if test="${optConfigMap.signAreaDt eq 'MM/dd'}"> selected="selected"</c:if>>MM/DD</option>
			<option value="yyyy.MM.dd"<c:if test="${optConfigMap.signAreaDt eq 'yyyy.MM.dd'}"> selected="selected"</c:if>>YYYY.MM.DD</option>
			<option value="yy.MM.dd"<c:if test="${optConfigMap.signAreaDt eq 'yy.MM.dd'}"> selected="selected"</c:if>>YY.MM.DD</option>
			<option value="MM.dd"<c:if test="${optConfigMap.signAreaDt eq 'MM.dd'}"> selected="selected"</c:if>>MM.DD</option>
			</select></td>
		<td class="width5"></td>
		<td><select id="signAreaDt2" name="signAreaDt2">
			<u:option value="" titleId="cm.option.notUse" alt="사용안함" selected="${empty optConfigMap.signAreaDt2}"/>
			<u:option value="posit" termId="or.term.posit" alt="직위" selected="${optConfigMap.signAreaDt2 eq 'posit'}"/>
			<u:option value="title" termId="or.term.title" alt="직책" selected="${optConfigMap.signAreaDt2 eq 'title'}"/>
			<u:option value="name" titleId="or.cols.name" alt="성명" selected="${optConfigMap.signAreaDt2 eq 'name'}"/>
			<u:option value="deptNm" titleId="or.cols.deptNm" alt="부서명" selected="${optConfigMap.signAreaDt2 eq 'deptNm'}"/>
			<u:option value="deptAbs" titleId="or.cols.deptAbs" alt="부서약어" selected="${optConfigMap.signAreaDt2 eq 'deptAbs'}"/>
			<option value="namePosit"<c:if test="${optConfigMap.signAreaDt2 eq 'namePosit'}"> selected="selected"</c:if>><u:msg titleId="or.cols.name" alt="성명"/>(<u:term termId="or.term.posit" alt="직위"/>)</option>
			<option value="nameTitle"<c:if test="${optConfigMap.signAreaDt2 eq 'nameTitle'}"> selected="selected"</c:if>><u:msg titleId="or.cols.name" alt="성명"/>(<u:term termId="or.term.title" alt="직책"/>)</option>
			<option value="nameDeptNm"<c:if test="${optConfigMap.signAreaDt2 eq 'nameDeptNm'}"> selected="selected"</c:if>><u:msg titleId="or.cols.name" alt="성명"/>(<u:msg titleId="or.cols.deptNm" alt="부서명"/>)</option>
			<option value="nameDeptAbs"<c:if test="${optConfigMap.signAreaDt2 eq 'nameDeptAbs'}"> selected="selected"</c:if>><u:msg titleId="or.cols.name" alt="성명"/>(<u:msg titleId="or.cols.deptAbs" alt="부서약어"/>)</option>
			<u:option value="apvTyp" titleId="ap.jsp.apvTyp" alt="결재구분" selected="${optConfigMap.signAreaDt2 eq 'apvTyp'}"/>
			</select></td>
		<td class="width5"></td>
		</tr>
		</table></td>
	</tr>
<c:if test="${false}"><!-- 옵션 삭제 -->
	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.absMark" alt="공석 표시 방법"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><input type="radio" id="absMarkabsRson" name="absMark" value="absRson"<c:if test="${empty optConfigMap.absMark or optConfigMap.absMark eq 'absRson'}"> checked="checked"</c:if> /></td>
		<td class="bodyip_lt"><label for="absMarkabsRson"><u:msg titleId="ap.cfg.absRson" alt="공석사유"/></label></td>
		<td class="width10"></td>
		<td><input type="radio" id="absMarkabsEmpt" name="absMark" value="absEmpt"<c:if test="${optConfigMap.absMark eq 'absEmpt'}"> checked="checked"</c:if> /></td>
		<td class="listicon_lt"><label for="absMarkabsEmpt"><img src="${_ctx}/images/${_skin}/ico_vacant.png" alt=""/></label></td>
		<td class="width10"></td>
		<td><input type="radio" id="absMarkabsDiag" name="absMark" value="absDiag"<c:if test="${optConfigMap.absMark eq 'absDiag'}"> checked="checked"</c:if> /></td>
		<td class="listicon_lt"><label for="absMarkabsDiag"><img src="${_ctx}/images/${_skin}/ico_vacant_b.png" alt=""/></label></td>
		</tr>
		</table></td>
	</tr>
</c:if>
</u:listArea>

<u:title titleId="ap.cfg.docNoCat" alt="문서번호 및 분류" type="small" />
<u:listArea>
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="ap.cfg.docCatInfo" alt="분류정보"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="catEnab" titleId="ap.cfg.catEnab" alt="분류 정보 사용" checked="${optConfigMap.catEnab eq 'Y'}" onclick="enableCatEnab(this.checked);" />
		<u:checkbox value="Y" name="psnCatEnab" titleId="ap.cfg.psnCatEnab" alt="개인 분류 사용(기안함)" checked="${optConfigMap.psnCatEnab eq 'Y'}" noSpaceTd="${true}" />
		</tr>
		</table></td>
	</tr>

	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="ap.cfg.docNo" alt="문서번호"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="bodyip_lt"><strong><u:msg titleId="ap.cfg.org" alt="기관"/></strong></td>
		<td class="width5"></td>
		<td><select id="docNoInst" name="docNoInst">
		<u:option value="notUse" titleId="cm.option.notUse" alt="사용안함" selected="${empty optConfigMap.docNoInst or optConfigMap.docNoInst eq 'notUse'}"/>
		<u:option value="instAbs" titleId="ap.cfg.instAbs" alt="기관약어" selected="${optConfigMap.docNoInst eq 'instAbs'}"/>
		<u:option value="instNm" titleId="ap.cfg.instNm" alt="기관명" selected="${optConfigMap.docNoInst eq 'instNm'}"/>
		</select></td>
		<td class="width20"></td>
		<td class="bodyip_lt"><strong><u:msg titleId="ap.cfg.docNoDept" alt="부서"/></strong></td>
		<td class="width5"></td>
		<td><select id="docNoDept" name="docNoDept">
		<u:option value="notUse" titleId="cm.option.notUse" alt="사용안함" selected="${optConfigMap.docNoDept eq 'notUse'}"/>
		<u:option value="deptAbs" titleId="or.cols.deptAbs" alt="부서약어" selected="${empty optConfigMap.docNoDept or optConfigMap.docNoDept eq 'deptAbs'}"/>
		<u:option value="deptNm" titleId="or.cols.deptNm" alt="부서명" selected="${optConfigMap.docNoDept eq 'deptNm'}"/>
		</select></td>
		<td class="width20"></td>
		<td class="bodyip_lt"><strong><u:msg titleId="ap.cfg.docCatInfo" alt="분류정보"/></strong></td>
		<td class="width5"></td>
		<td><select id="docNoCat" name="docNoCat"<c:if test="${optConfigMap.catEnab ne 'Y'}"> disabled="disabled"</c:if> >
		<u:option value="notUse" titleId="cm.option.notUse" alt="사용안함" selected="${optConfigMap.docNoCat eq 'notUse'}"/>
		<u:option value="use" titleId="cols.use" alt="사용" selected="${optConfigMap.docNoCat eq 'use'}"/>
		</select></td>
		<td class="width20"></td>
		<td class="bodyip_lt"><strong><u:msg titleId="ap.cfg.docNoYr" alt="년도"/></strong></td>
		<td class="width5"></td>
		<td><select id="docNoYr" name="docNoYr">
		<u:option value="notUse" titleId="cm.option.notUse" alt="사용안함" selected="${optConfigMap.docNoYr eq 'notUse'}"/>
		<u:option value="YYYY" title="YYYY" selected="${empty optConfigMap.docNoYr or optConfigMap.docNoYr eq 'YYYY'}"/>
		<u:option value="YY" title="YY" selected="${optConfigMap.docNoYr eq 'YY'}"/>
		</select></td>
		</tr>
		</table>
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="bodyip_lt"><strong><u:msg titleId="ap.cfg.docNoSeqLen" alt="일련번호"/></strong></td>
		<td class="width5"></td>
		<td><select id="docNoSeqLen" name="docNoSeqLen">
		<u:option value="3" titleId="ap.cfg.len3" alt="3자리" selected="${optConfigMap.docNoSeqLen eq '3'}"/>
		<u:option value="4" titleId="ap.cfg.len4" alt="4자리" selected="${empty optConfigMap.docNoSeqLen or optConfigMap.docNoSeqLen eq '4'}"/>
		<u:option value="5" titleId="ap.cfg.len5" alt="5자리" selected="${optConfigMap.docNoSeqLen eq '5'}"/>
		<u:option value="6" titleId="ap.cfg.len6" alt="6자리" selected="${optConfigMap.docNoSeqLen eq '6'}"/>
		<u:option value="7" titleId="ap.cfg.len7" alt="7자리" selected="${optConfigMap.docNoSeqLen eq '7'}"/>
		<u:option value="8" titleId="ap.cfg.len8" alt="8자리" selected="${optConfigMap.docNoSeqLen eq '8'}"/>
		</select></td>
		<td class="width10"></td>
		<u:checkbox value="Y" name="docNoFxLen" titleId="ap.cfg.docNoFxLen" alt="고정 길이 일련번호" checked="${optConfigMap.docNoFxLen eq 'Y'}" />
		<td class="width20"></td>
		<u:checkbox value="Y" name="useDash" titleId="ap.cfg.useDash" alt="대시(-) 사용" checked="${optConfigMap.useDash eq 'Y'}" />
		</tr>
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.docNoBaseDt" alt="채번 기준일"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="bodyip_lt"><strong><u:msg titleId="ap.cfg.regRecLst" alt="등록 대장"/></strong></td>
		<td class="width5"></td>
		<u:radio value="stDt" name="regRecLstBaseDt" titleId="ap.cfg.stDt" alt="기안일" checked="${optConfigMap.regRecLstBaseDt eq 'stDt'}" />
		<u:radio value="enDt" name="regRecLstBaseDt" titleId="ap.cfg.enDt" alt="완료일" checked="${empty optConfigMap.regRecLstBaseDt or optConfigMap.regRecLstBaseDt eq 'enDt'}" />
		<td class="width20"></td>
		</tr>
		<tr>
		<td class="bodyip_lt"><strong><u:msg titleId="ap.cfg.recvDistRecLst" alt="접수(배부) 대장"/></strong></td>
		<td class="width5"></td>
		<u:radio value="enDt" name="recvRecLstBaseDt" titleId="ap.cfg.enDt" alt="완료일" checked="${optConfigMap.recvRecLstBaseDt eq 'enDt'}" />
		<u:radio value="recvDt" name="recvRecLstBaseDt" titleId="ap.cfg.recvDt" alt="접수(배부)일" checked="${empty optConfigMap.recvRecLstBaseDt or optConfigMap.recvRecLstBaseDt eq 'recvDt'}" />
		<td class="width20"></td>
		</tr>
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.recoDt" alt="년도 기준일"/></td>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select id="recoMt" name="recoMt">
			<c:forEach items="${months}" var="month" varStatus="status">
			<u:option value="${month}" termId="cm.m.${month}" selected="${optConfigMap.recoMt eq month}"
			/></c:forEach>
			</select></td>
		<td class="width5"></td>
		<td><select id="recoDt" name="recoDt">
			<c:forEach items="${days}" var="day" varStatus="status">
			<u:option value="${day}" termId="cm.d.${day}" selected="${optConfigMap.recoMt eq day}"
			/></c:forEach>
			</select></td>
		</tr>
		</table></td>
	</tr>
</u:listArea>

<u:title titleId="ap.jsp.setOpt.funcOpt1" alt="결재 기능 옵션" type="small" />
<u:listArea>
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="ap.cfg.apvr" alt="결재자"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="needLastApvr" titleId="ap.cfg.needLastApvr" alt="최종결재자 필수 지정" checked="${optConfigMap.needLastApvr eq 'Y'}" />
		<u:checkbox value="Y" name="apvrFromOtherComp" titleId="ap.cfg.apvrFromOtherComp" alt="대외 조직도 사용(타 회사 결재선 지정)" checked="${optConfigMap.apvrFromOtherComp eq 'Y'}" />
		<u:checkbox value="Y" name="cnclAftRead" titleId="ap.cfg.cnclAftRead" alt="승인자 열람 후 취소 가능" checked="${optConfigMap.cnclAftRead eq 'Y'}" />
		</tr>
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.uiFeatures" alt="UI 기능"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="showOpinAtBtn" titleId="ap.cfg.showOpinAtBtn" alt="의견있음 표시" checked="${optConfigMap.showOpinAtBtn eq 'Y'}" />
		<u:checkbox value="Y" name="opinDftDisp" titleId="ap.cfg.opinDftDisp" alt="본문에 의견 표시(디폴트)" checked="${optConfigMap.opinDftDisp eq 'Y'}" />
		<u:checkbox value="Y" name="dispOpinAtSignArea" titleId="ap.cfg.dispOpinAtSignArea" alt="의견 도장방에 표시" checked="${optConfigMap.dispOpinAtSignArea eq 'Y'}" />
		<u:checkbox value="Y" name="dispConsAtAgr" titleId="ap.cfg.dispConsAtAgr" alt="반대 도장방에 표시" checked="${optConfigMap.dispConsAtAgr eq 'Y'}" />
		</tr>
		</table><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="empsApvBtn" titleId="ap.cfg.empsApvBtn" alt="승인 버튼 강조" checked="${optConfigMap.empsApvBtn eq 'Y'}" />
		<u:checkbox value="Y" name="openPop" titleId="ap.cfg.openPop" alt="팝업 열기" checked="${optConfigMap.openPop eq 'Y'}" />
		<u:checkbox value="Y" name="newLnEach" titleId="ap.cfg.newLnEach" alt="첨부파일 한줄 개행" checked="${optConfigMap.newLnEach eq 'Y'}" />
		<u:checkbox value="Y" name="upwardApvPop" titleId="ap.cfg.upwardApvPop" alt="승인 팝업 위로" checked="${optConfigMap.upwardApvPop eq 'Y'}" />
		<u:checkbox value="Y" name="modTitle" titleId="ap.jsp.modTitle" alt="제목 수정" checked="${optConfigMap.modTitle eq 'Y'}" />
		</tr>
		</table><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="mobBulkApv" titleId="ap.cfg.mobBulkApv" alt="모바일 일괄결재" checked="${optConfigMap.mobBulkApv eq 'Y'}" />
		<u:checkbox value="Y" name="viewMobileBody" titleId="ap.cfg.viewMobileBody" alt="모바일 본문보기" checked="${optConfigMap.viewMobileBody eq 'Y'}" />
		</tr>
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.deptFnc" alt="부서 기능"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="deptAgrEnab" titleId="ap.cfg.deptAgrEnab" alt="부서합의" checked="${optConfigMap.deptAgrEnab eq 'Y'}" />
		<u:checkbox value="Y" name="deptInfmEnab" titleId="ap.cfg.deptInfmEnab" alt="부서통보" checked="${optConfigMap.deptInfmEnab eq 'Y'}" />
		<u:checkbox value="Y" name="formBxByDept" titleId="ap.cfg.formBxByDept" alt="부서별 양식함" checked="${optConfigMap.formBxByDept eq 'Y'}" />
		</tr>
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.deptEx" alt="부서 확장"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="exRecvRange" titleId="ap.cfg.exRecvRange" alt="접수 범위 확장" checked="${optConfigMap.exRecvRange eq 'Y'}" />
		<u:checkbox value="Y" name="exDeptAgrRange" titleId="ap.cfg.exDeptAgrRange" alt="부서합의 범위 확장" checked="${optConfigMap.exDeptAgrRange eq 'Y'}" />
		<u:checkbox value="Y" name="exDeptAgrMakerRange" titleId="ap.cfg.exDeptAgrMakerRange" alt="부서합의 범위 확장(기안자)" checked="${optConfigMap.exDeptAgrMakerRange eq 'Y'}" />
		<u:checkbox value="Y" name="exPubVwRange" titleId="ap.cfg.exPubVwRange" alt="공람 범위 확장" checked="${optConfigMap.exPubVwRange eq 'Y'}" />
		<u:checkbox value="Y" name="exPubVwMakerRange" titleId="ap.cfg.exPubVwMakerRange" alt="공람 범위 확장(담당자)" checked="${optConfigMap.exPubVwMakerRange eq 'Y'}" />
		</tr>
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.term.dblApv" alt="이중결재"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="dblApvEnab"  titleId="cm.option.use" alt="사용" checked="${optConfigMap.dblApvEnab eq 'Y'}" />
		<u:checkbox value="Y" name="prcDeptEnab" titleId="ap.cfg.prcDeptEnab" alt="처리부서 지정" checked="${optConfigMap.prcDeptEnab eq 'Y'}" />
		<u:checkbox value="Y" name="pichInApvLnEnab" titleId="ap.cfg.pichInApvLnEnab" alt="결재선 내의 담당자 허용" checked="${optConfigMap.pichInApvLnEnab eq 'Y'}" />
		</tr>
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.etcFnc" alt="기타 기능"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="parlEnab" titleId="ap.cfg.parlEnab" alt="병렬사용" checked="${optConfigMap.parlEnab eq 'Y'}" />
		<u:checkbox value="Y" name="bothSendEnab" titleId="ap.cfg.bothSendEnab" alt="대 내외 동시 시행" checked="${optConfigMap.bothSendEnab eq 'Y'}" />
		<u:checkbox value="Y" name="fixdApvLn" titleId="ap.cfg.fixdApvLn" alt="양식 결재선" checked="${optConfigMap.fixdApvLn eq 'Y'}" />
		<u:checkbox value="Y" name="revw2Enable" titleId="ap.cfg.revw2Enable" alt="검토2 사용" checked="${optConfigMap.revw2Enable eq 'Y'}" />
		<u:checkbox value="Y" name="refVwEnable" titleId="ap.cfg.refVwEnable" alt="참조열람 사용" checked="${optConfigMap.refVwEnable eq 'Y'}" />
		<u:checkbox value="Y" name="fixdRefVw" titleId="ap.cfg.fixdRefVw" alt="양식 참조열람" checked="${optConfigMap.fixdRefVw eq 'Y'}" />
		<u:checkbox value="Y" name="adiRevw" titleId="ap.cfg.adiRevw" alt="추가검토" checked="${optConfigMap.adiRevw eq 'Y'}" />
		</tr>
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.msgUsg" alt="메시지 사용"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="msgSign" titleId="ap.cfg.msgSign" alt="서명 시 확인 메시지 사용" checked="${optConfigMap.msgSign eq 'Y'}" />
		<u:checkbox value="Y" name="msgOnePsn" titleId="ap.cfg.msgOnePsn" alt="1인 결재 시 확인 메시지 사용" checked="${optConfigMap.msgOnePsn eq 'Y'}" />
		</tr>
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.doc.opin" alt="의견"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="mailOpin" titleId="ap.cfg.mailOpin" alt="의견 메일 보내기" checked="${optConfigMap.mailOpin eq 'Y'}" />
		<u:checkbox value="Y" name="saveOpin" titleId="ap.cfg.saveOpin" alt="의견 저장" checked="${optConfigMap.saveOpin eq 'Y'}" onclick="setSaveOpin(this.checked)" />
		<u:checkbox value="Y" name="saveOpinMail" titleId="ap.cfg.saveOpinMail" alt="의견 저장 메일 알림" checked="${optConfigMap.saveOpinMail eq 'Y'}" />
		</tr>
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.notiEnable" alt="알림 사용"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="sendMailNoti" titleId="ap.cfg.sendMailNoti" alt="알림 메일로 보내기" checked="${optConfigMap.sendMailNoti eq 'Y'}" />
		<u:checkbox value="Y" name="msgPsnInfm" titleId="ap.cfg.msgPsnInfm" alt="개인 통보 알림 사용" checked="${optConfigMap.msgPsnInfm eq 'Y'}" />
		<u:checkbox value="Y" name="msgDeptBx" titleId="ap.cfg.msgDeptBx" alt="부서 대기함 알림 사용" checked="${optConfigMap.msgDeptBx eq 'Y'}" />
		<u:checkbox value="Y" name="msgRecvBx" titleId="ap.cfg.msgRecvBx" alt="접수함 알림 사용" checked="${optConfigMap.msgRecvBx eq 'Y'}" />
		<u:checkbox value="Y" name="msgRefVwBx" titleId="ap.cfg.msgRefVwBx" alt="참조열람 알림 사용" checked="${optConfigMap.msgRefVwBx eq 'Y'}" />
		</tr>
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.signPw" alt="도장/서명/비밀번호"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="notAlwChgStmp" titleId="ap.cfg.notAlwChgStmp" alt="도장 수정 허용 안함" checked="${optConfigMap.notAlwChgStmp eq 'Y'}" />
		<u:checkbox value="Y" name="notAlwChgSign" titleId="ap.cfg.notAlwChgSign" alt="서명 수정 허용 안함" checked="${optConfigMap.notAlwChgSign eq 'Y'}" />
		<u:checkbox value="Y" name="notAlwApvPw" titleId="ap.cfg.notAlwApvPw" alt="결재 비밀번호 사용 안함" checked="${optConfigMap.notAlwApvPw eq 'Y'}" onclick="setNotAlwApvPw(this.checked)" />
		<u:checkbox value="Y" name="noMakPw" titleId="ap.cfg.noMakPw" alt="기안 비밀번호 사용 안함" checked="${optConfigMap.noMakPw eq 'Y'}" disabled="${optConfigMap.notAlwApvPw eq 'Y' ? 'Y' : ''}" />
		</tr>
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.term.reRevw" alt="재검토"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="rejtPrevEnab" titleId="ap.cfg.rejtPrevEnab" alt="재검토 사용(이전 결재자로)" checked="${optConfigMap.rejtPrevEnab eq 'Y'}" onclick="changeCheckEnable('reRevwAtMyAgrY', this.checked); changeCheckEnable('reRevwToPrevAgrY', this.checked);" />
		<u:checkbox value="Y" name="reRevwAtMyAgr" titleId="ap.cfg.reRevwAtMyAgr" alt="현재 합의자 재검토 사용" checked="${optConfigMap.reRevwAtMyAgr eq 'Y'}" disabled="${optConfigMap.rejtPrevEnab ne 'Y' ? 'Y' : ''}" />
		<u:checkbox value="Y" name="reRevwToPrevAgr" titleId="ap.cfg.reRevwToPrevAgr" alt="합의 시 재검토 사용" checked="${optConfigMap.reRevwToPrevAgr eq 'Y'}" disabled="${optConfigMap.rejtPrevEnab ne 'Y' ? 'Y' : ''}" />
		</tr>
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.term.agr" alt="합의"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="chgApvrAtPsnOrdrdAgr" titleId="ap.cfg.chgApvrAtPsnOrdrdAgr" alt="개인순차합의 결재선 변경" checked="${optConfigMap.chgApvrAtPsnOrdrdAgr eq 'Y'}" />
		<u:checkbox value="Y" name="chgBodyAtPsnOrdrdAgr" titleId="ap.cfg.chgBodyAtPsnOrdrdAgr" alt="개인순차합의 본문 수정" checked="${optConfigMap.chgBodyAtPsnOrdrdAgr eq 'Y'}" />
		<u:checkbox value="Y" name="chgAttchAtPsnOrdrdAgr" titleId="ap.cfg.chgAttchAtPsnOrdrdAgr" alt="개인순차합의 첨부 수정" checked="${optConfigMap.chgAttchAtPsnOrdrdAgr eq 'Y'}" />
		<u:checkbox value="Y" name="rejtWhenPsnOrdrdAgr" titleId="ap.cfg.rejtWhenPsnOrdrdAgr" alt="개인순차합의 반려 사용" checked="${optConfigMap.rejtWhenPsnOrdrdAgr eq 'Y'}" />
		</tr>
		<tr>
		<u:checkbox value="Y" name="noConsAtAgr" titleId="ap.cfg.noConsAtAgr" alt="합의반대 사용안함" checked="${optConfigMap.noConsAtAgr eq 'Y'}" />
		<u:checkbox value="Y" name="dispLastAgrDept" titleId="ap.cfg.dispLastAgrDept" alt="부서합의 최종부서 표기" checked="${optConfigMap.dispLastAgrDept eq 'Y'}" />
		</tr>
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.bx.regRecLst" alt="등록대장"/>/<u:msg titleId="cm.btn.del" alt="삭제"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="keepRejtDoc" titleId="ap.cfg.keepRejtDoc" alt="반려된 문서 등록대장에 저장" checked="${optConfigMap.keepRejtDoc eq 'Y'}"
			onclick="clickKeepRejtDoc(this.checked);" />
		<u:checkbox value="Y" name="delRejtBox" titleId="ap.cfg.delRejtBox" alt="반려함에서 재기안 후 삭제" checked="${optConfigMap.delRejtBox eq 'Y'}"
			disabled="${optConfigMap.keepRejtDoc}" />
		<u:checkbox value="Y" name="unregDft" titleId="ap.cfg.unregDft" alt="미등록 기본값으로" checked="${optConfigMap.unregDft eq 'Y'}" />
		</tr>
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.chosRecv" alt="수신처 지정"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="regOuter" titleId="ap.cfg.regOuter" alt="외부기관 입력 기능 사용" checked="${optConfigMap.regOuter eq 'Y'}" />
		<td class="width20"></td>
		<td class="bodyip_lt"><strong><u:msg titleId="ap.cfg.introScope" alt="대내 조직도 범위"/></strong></td>
		<td class="width8"></td>
		<u:radio value="org" name="introScope" titleId="ap.cfg.org" alt="기관" checked="${optConfigMap.introScope eq 'org' or empty optConfigMap.introScope}" />
		<u:radio value="comp" name="introScope" titleId="ap.cfg.comp" alt="회사" checked="${optConfigMap.introScope eq 'comp'}" />
		</tr>
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.ofcSeal" alt="관인/서명인"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0"><tr>
		<u:checkbox value="Y" name="alwChgOfcSeal" titleId="ap.cfg.alwChgOfcSeal" alt="관인 수정 허용" checked="${optConfigMap.alwChgOfcSeal eq 'Y'}" />
		<u:checkbox value="Y" name="autoOfcSeal" titleId="ap.cfg.autoOfcSeal" alt="관인 자동 날인" checked="${optConfigMap.autoOfcSeal eq 'Y'}" disabled="${optConfigMap.autoSend eq 'Y' ? 'Y' : ''}" />
		<u:checkbox value="Y" name="alwIntroNoSign" titleId="ap.cfg.alwIntroNoSign" alt="대내 시행 시 서명인 생략 가능" checked="${optConfigMap.alwIntroNoSign eq 'Y'}" />
		</tr></table>
		<table border="0" cellpadding="0" cellspacing="0"><tr>
		<u:checkbox value="Y" name="alwIntroOfcSeal" titleId="ap.cfg.alwIntroOfcSeal" alt="대내 시행 시 관인 허용" checked="${optConfigMap.alwIntroOfcSeal eq 'Y'}" />
		<u:checkbox value="Y" name="alwExtroSign" titleId="ap.cfg.alwExtroSign" alt="대외 발송 시 서명인 허용" checked="${optConfigMap.alwExtroSign eq 'Y'}" />
		</tr></table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.jsp.form" alt="양식"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0"><tr>
		<u:checkbox value="Y" name="notMandFormNm" titleId="ap.cfg.notMandFormNm" alt="양식명 선택 입력(양식편집)" checked="${optConfigMap.notMandFormNm eq 'Y'}" />
		<u:checkbox value="Y" name="notEditFormNm" titleId="ap.cfg.notEditFormNm" alt="양식명 수정 불가" checked="${optConfigMap.notEditFormNm eq 'Y'}" />
		<u:checkbox value="Y" name="formNmToSubj" titleId="ap.cfg.formNmToSubj" alt="양식명을 제목으로" checked="${optConfigMap.formNmToSubj eq 'Y'}" />
		<u:checkbox value="Y" name="refMakWithApvLn" titleId="ap.cfg.refMakWithApvLn" alt="참조기안 결재선 유지" checked="${optConfigMap.refMakWithApvLn eq 'Y'}" />
		</tr></table></td>
	</tr>
</u:listArea>

<u:title titleId="ap.cfg.listOpt" alt="목록 옵션" type="small" />
<u:listArea>
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="ap.cfg.listadt" alt="목록 추가 조건"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="bodyip_lt"><strong><u:msg titleId="ap.cfg.apvdBx" alt="완료함"/></strong></td>
		<td class="width8"></td>
		<u:checkbox value="Y" name="apvdBxEntu" titleId="ap.term.entu" alt="결재안함(위임)" checked="${optConfigMap.apvdBxEntu eq 'Y'}" />
		<u:checkbox value="Y" name="apvdBxParalPubVw" titleId="ap.term.paralPubVw" alt="동시공람" checked="${optConfigMap.apvdBxParalPubVw eq 'Y'}" />
		<u:checkbox value="Y" name="apvdBxFstVw" titleId="ap.term.fstVw" alt="선람" checked="${optConfigMap.apvdBxFstVw eq 'Y'}" />
		<u:checkbox value="Y" name="apvdBxPubVw" titleId="ap.term.pubVw" alt="공람" checked="${optConfigMap.apvdBxPubVw eq 'Y'}" />
		<u:checkbox value="Y" name="apvdBxMak" titleId="ap.term.mak" alt="기안" checked="${optConfigMap.apvdBxMak eq 'Y'}" />
		</tr>
		</table><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="bodyip_lt"><strong><u:msg titleId="ap.cfg.ongoBx" alt="진행함"/></strong></td>
		<td class="width8"></td>
		<u:checkbox value="Y" name="ongoBxBefoMyTurn" titleId="ap.cfg.befoMyTurn" alt="결재 이전" checked="${optConfigMap.ongoBxBefoMyTurn eq 'Y'}" />
		<u:checkbox value="Y" name="ongoBxMak" titleId="ap.term.mak" alt="기안" checked="${optConfigMap.ongoBxMak eq 'Y'}" />
		</tr>
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.openWhenAbs" alt="부재 설정 시 문서 열람 범위"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="waitBxAbs" titleId="ap.cfg.waitBx" alt="대기함" checked="${optConfigMap.waitBxAbs eq 'Y'}" />
		<u:checkbox value="Y" name="ongoBxAbs" titleId="ap.cfg.ongoBx" alt="진행함" checked="${optConfigMap.ongoBxAbs eq 'Y'}" />
		<u:checkbox value="Y" name="apvdBxAbs" titleId="ap.cfg.apvdBx" alt="완료함" checked="${optConfigMap.apvdBxAbs eq 'Y'}" />
		<u:checkbox value="Y" name="myBxAbs" titleId="ap.cfg.myBx" alt="기안함" checked="${optConfigMap.myBxAbs eq 'Y'}" />
		<u:checkbox value="Y" name="rejtBxAbs" titleId="ap.cfg.rejtBx" alt="반려함" checked="${optConfigMap.rejtBxAbs eq 'Y'}" />
		<u:checkbox value="Y" name="postApvdBxAbs" titleId="ap.cfg.postApvdBx" alt="후열함" checked="${optConfigMap.postApvdBxAbs eq 'Y'}" />
		<u:checkbox value="Y" name="censrBxAbs" titleId="ap.cfg.censrBx" alt="심사함" checked="${optConfigMap.censrBxAbs eq 'Y'}" />
		</tr><c:if
		
		 test="${false}">
		<!-- 사용안함 -->
		<tr>
		<u:checkbox value="Y" name="drftBxAbs" titleId="ap.cfg.drftBx" alt="개인함" checked="${optConfigMap.drftBxAbs eq 'Y'}" />
		<u:checkbox value="Y" name="disudBxAbs" titleId="ap.cfg.disudBx" alt="폐기함" checked="${optConfigMap.disudBxAbs eq 'Y'}" />
		<u:checkbox value="Y" name="deptBxAbs" titleId="ap.cfg.deptBx" alt="부서 대기함" checked="${optConfigMap.deptBxAbs eq 'Y'}" />
		<u:checkbox value="Y" name="toSendBxAbs" titleId="ap.cfg.toSendBx" alt="발송함" checked="${optConfigMap.toSendBxAbs eq 'Y'}" />
		<u:checkbox value="Y" name="recvBxAbs" titleId="ap.cfg.recvBx" alt="접수함" checked="${optConfigMap.recvBxAbs eq 'Y'}" />
		<u:checkbox value="Y" name="distBxAbs" titleId="ap.cfg.distBx" alt="배부함" checked="${optConfigMap.distBxAbs eq 'Y'}" />
		<u:checkbox value="Y" name="regRecLstAbs" titleId="ap.cfg.regRecLst" alt="등록 대장" checked="${optConfigMap.regRecLstAbs eq 'Y'}" />
		<u:checkbox value="Y" name="recvRecLstAbs" titleId="ap.cfg.recvRecLst" alt="접수 대장" checked="${optConfigMap.recvRecLstAbs eq 'Y'}" />
		<u:checkbox value="Y" name="distRecLstAbs" titleId="ap.cfg.distRecLst" alt="배부 대장" checked="${optConfigMap.distRecLstAbs eq 'Y'}" />
		</tr></c:if>
		
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.unreadMark" alt="읽지않음 표시"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="waitBxUnread" titleId="ap.cfg.waitBx" alt="대기함" checked="${optConfigMap.waitBxUnread eq 'Y'}" />
		<u:checkbox value="Y" name="postApvdBxUnread" titleId="ap.cfg.postApvdBx" alt="통보함" checked="${optConfigMap.postApvdBxUnread eq 'Y'}" />
		</tr>
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.docCntInBx" alt="함별 문서 수 표시"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="docCntInBx" titleId="cm.option.use" alt="사용" checked="${optConfigMap.docCntInBx eq 'Y'}" />
		<u:checkbox value="Y" name="docCntInRecvBx" titleId="ap.cfg.recvBx" alt="접수함" checked="${optConfigMap.docCntInRecvBx eq 'Y'}" />
		</tr>
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.adurMergLst" alt="겸직 통합 표시"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="adurMergLst" titleId="cm.option.use" alt="사용" checked="${optConfigMap.adurMergLst eq 'Y'}" />
		</tr>
		</table></td>
	</tr>
	
</u:listArea>

<u:title titleId="ap.jsp.setOpt.funcOpt2" alt="유통 기능 옵션" type="small" />
<u:listArea>

	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="ap.cfg.censr" alt="심사 "/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="enabCensr" onclick="setCensrEnab(this.checked);" titleId="ap.cfg.enabCensr" alt="심사 기능 사용" checked="${optConfigMap.enabCensr eq 'Y'}" />
		<td class="width20"></td>
		<td class="bodyip_lt"><strong><u:msg titleId="ap.cfg.censrLoc" alt="심사자 선택 범위"/></strong></td>
		<td class="width5"></td>
		<td><select id="censrLoc" name="censrLoc" <c:if test="${optConfigMap.enabCensr ne 'Y'}">disabled="disabled"</c:if>>
		<u:option value="all" titleId="ap.cfg.allDept" alt="전체 부서" selected="${optConfigMap.censrLoc eq 'all'}"/>
		<u:option value="inDept" titleId="ap.cfg.inDept" alt="소속 부서" selected="${optConfigMap.censrLoc eq 'inDept'}"/>
		</select></td>
		</tr>
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.send" alt="발송"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:radio value="myBx" name="sendFrom" titleId="ap.cfg.sendFrmMyBx" alt="기안함에서 발송" checked="${optConfigMap.sendFrom eq 'myBx'}" />
		<u:radio value="toSendBx" name="sendFrom" titleId="ap.cfg.sendFrmToSendBx" alt="발송함에서 발송" checked="${optConfigMap.sendFrom eq 'toSendBx'}" />
		<u:radio value="censrBx" name="sendFrom" titleId="ap.cfg.sendFrmCensrBx" alt="심사함에서 발송" checked="${optConfigMap.sendFrom eq 'censrBx'}"
			disabled="${optConfigMap.enabCensr ne 'Y' ? 'Y' : '' }"/>
		<u:checkbox value="Y" name="sendWithRefDoc" titleId="ap.cfg.sendWithRefDoc" alt="참조문서 유지" checked="${optConfigMap.sendWithRefDoc eq 'Y'}" />
		<u:checkbox value="Y" name="mandRecvr" titleId="ap.cfg.mandRecvr" alt="시행문 수신처 지정 필수" checked="${optConfigMap.mandRecvr eq 'Y'}" />
		</tr>
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.autoSend" alt="자동발송"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="autoSend" titleId="cm.option.use" alt="사용" checked="${optConfigMap.autoSend eq 'Y'}" onclick="setAutoSend(this.checked)" />
		<u:checkbox value="Y" name="autoSendWithRefDoc" titleId="ap.cfg.autoSendWithRefDoc" alt="참조문서 포함" checked="${optConfigMap.autoSendWithRefDoc eq 'Y'}" disabled="${optConfigMap.autoSend ne 'Y' ? 'Y' : ''}" />
		<u:checkbox value="Y" name="autoSendSign" titleId="ap.cfg.autoSendSign" alt="발신명의 날인 포함" checked="${optConfigMap.autoSendSign eq 'Y'}" disabled="${optConfigMap.autoSend ne 'Y' ? 'Y' : ''}" />
		</tr>
		</table></td>
	</tr>
<c:if test="${false}"><!-- 옵션 삭제 -->
	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.pichBx" alt="담당자 표시위치(접수, 부서합의)"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:radio value="myBx" name="pichBx" titleId="ap.cfg.myBx" alt="기안함" checked="${optConfigMap.pichBx eq 'myBx'}" />
		<u:radio value="waitBx" name="pichBx" titleId="ap.cfg.waitBx" alt="대기함" checked="${optConfigMap.pichBx eq 'waitBx'}" />
		</tr>
		</table></td>
	</tr>
</c:if>
	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.sendBck" alt="반송"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="regWhenSendBck" titleId="ap.cfg.regWhenSendBck" alt="반송 시 접수 대장 등록" checked="${optConfigMap.regWhenSendBck eq 'Y'}" />
		</tr>
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.listOfPubRead" alt="공람 게시 사용 가능한 대장"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="regRecLstPubBx" titleId="ap.cfg.regRecLst" alt="등록 대장" checked="${optConfigMap.regRecLstPubBx eq 'Y'}" />
		<u:checkbox value="Y" name="recvRecLstPubBx" titleId="ap.cfg.recvRecLst" alt="접수 대장" checked="${optConfigMap.recvRecLstPubBx eq 'Y'}" />
		</tr>
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.listOfSecuLvl" alt="보안등급 적용 대상"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="regRecLstSecuLvl" titleId="ap.cfg.regRecLst" alt="등록 대장" checked="${optConfigMap.regRecLstSecuLvl eq 'Y'}" />
		<u:checkbox value="Y" name="recvRecLstSecuLvl" titleId="ap.cfg.recvRecLst" alt="접수 대장" checked="${optConfigMap.recvRecLstSecuLvl eq 'Y'}" />
		<u:checkbox value="Y" name="pubBxSecuLvl" titleId="ap.cfg.pubBx" alt="공람 게시" checked="${optConfigMap.pubBxSecuLvl eq 'Y'}" />
		<u:input type="hidden" name="distRecLstSecuLvl" titleId="ap.cfg.distRecLst" alt="배부 대장" value="${optConfigMap.distRecLstSecuLvl}" />
		</tr>
		</table></td>
	</tr>
	
</u:listArea>

<u:title titleId="ap.cfg.intgOpt" alt="연계 옵션" type="small" />
<u:listArea>

	<tr style="${empty dmEnable ? 'display:none' : ''}">
	<td width="18%" class="head_lt"><u:msg titleId="ap.cfg.dmAtMaking" alt="기안시 문서관리 지정 "/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:radio value="noDm" name="dmAtMaking" titleId="ap.cfg.noDmAtMaking" alt="사용 안함" checked="${optConfigMap.dmAtMaking ne 'opt' && optConfigMap.dmAtMaking ne 'man'}" />
		<u:radio value="opt" name="dmAtMaking" titleId="ap.cfg.optDmAtMaking" alt="선택 사용" checked="${optConfigMap.dmAtMaking eq 'opt'}" />
		<u:radio value="man" name="dmAtMaking" titleId="ap.cfg.manDmAtMaking" alt="필수 사용" checked="${optConfigMap.dmAtMaking eq 'man'}" />
		</tr>
		</table></td>
	</tr>

	<tr style="${empty dmEnable ? 'display:none' : ''}">
	<td width="18%" class="head_lt"><u:msg titleId="ap.cfg.sendToDm" alt="문서관리로 보내기 사용"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="dmFromRegRecLst" titleId="ap.cfg.regRecLst" alt="등록 대장" checked="${optConfigMap.dmFromRegRecLst eq 'Y'}" />
		<u:checkbox value="Y" name="dmFromRecvRecLst" titleId="ap.cfg.recvRecLst" alt="접수 대장" checked="${optConfigMap.dmFromRecvRecLst eq 'Y'}" />
		</tr>
		</table></td>
	</tr>
	
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="ap.cfg.sendToBb" alt="게시 보내기 사용"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="sendToBb" titleId="cols.use" alt="사용" checked="${optConfigMap.sendToBb eq 'Y'}" />
		</tr>
		</table></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.sendMail" alt="메일 보내기"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="sendMailAtMyBx" titleId="ap.cfg.myBx" alt="기안함" checked="${optConfigMap.sendMailAtMyBx eq 'Y'}" />
		<u:checkbox value="Y" name="sendMailAtOngoBx" titleId="ap.cfg.ongoBx" alt="진행함" checked="${optConfigMap.sendMailAtOngoBx eq 'Y'}" />
		<u:checkbox value="Y" name="sendMailAtWaitBx" titleId="ap.cfg.waitBx" alt="대기함" checked="${optConfigMap.sendMailAtWaitBx eq 'Y'}" />
		<u:checkbox value="Y" name="sendMailAtApvdBx" titleId="ap.cfg.apvdBx" alt="완료함" checked="${optConfigMap.sendMailAtApvdBx eq 'Y'}" />
		<u:checkbox value="Y" name="sendMailAtRejtBx" titleId="ap.cfg.rejtBx" alt="반려함" checked="${optConfigMap.sendMailAtRejtBx eq 'Y'}" />
		<u:checkbox value="Y" name="sendMailAtPostApvdBx" titleId="ap.bx.postApvdBx" alt="통보함" checked="${optConfigMap.sendMailAtPostApvdBx eq 'Y'}" />
		<u:checkbox value="Y" name="sendMailRegRecLst" titleId="ap.cfg.regRecLst" alt="등록 대장" checked="${optConfigMap.sendMailRegRecLst eq 'Y'}" />
		<u:checkbox value="Y" name="sendMailRecvRecLst" titleId="ap.cfg.recvRecLst" alt="접수 대장" checked="${optConfigMap.sendMailRecvRecLst eq 'Y'}" />
		</table></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.erpDoc" alt="ERP 문서"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="erpBodyLock" titleId="ap.cfg.erpBodyLock" alt="ERP 문서 본문 수정 안함" checked="${optConfigMap.erpBodyLock eq 'Y'}" />
		<u:checkbox value="Y" name="erpBodyPreview" titleId="ap.cfg.erpBodyPreview" alt="ERP 문서 본문 미리보기(대기함)" checked="${optConfigMap.erpBodyPreview eq 'Y'}" />
		</tr>
		</table></td>
	</tr>
	
</u:listArea>

<c:if
	test="${sessionScope.userVo.userUid eq 'U0000001'}">
<u:title titleId="ap.cfg.sysOpt" alt="시스템 옵션" type="small" />
<u:listArea>
	
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="ap.cfg.trx" alt="시행변환"/></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:checkbox value="Y" name="formToEditEnabled" titleId="ap.cfg.formToEditEnabled" alt="폼양식 에디트 양식으로 전환" checked="${optConfigMap.formToEditEnabled eq 'Y'}" />
		</tr>
		</table></td>
	</tr>
	<tr>
	<td width="18%" class="head_lt"><a href="javascript:openExtDocPop()"><u:msg titleId="ap.cfg.extDoc" alt="대외공문"/></a></td>
	<td class="bodybg_lt"><c:forEach
		items="${apFormJspDVoList}" var="apFormJspDVo" varStatus="status"><c:if
			test="${not status.first}">, </c:if>${apFormJspDVo.formId}</c:forEach></td>
	</tr>
	
</u:listArea>
</c:if><c:if
	test="${sessionScope.userVo.userUid ne 'U0000001'}">
<input type="hidden" name="formToEditEnabled" value="${optConfigMap.formToEditEnabled}" />
</c:if><%--
/*
noReRevwBy2nd=둘째 검토자 재검토 사용 안함
	N			둘째 검토자 재검토 항상 사용(디폴트)
	Y			둘째 검토자 합의 사용 안함
	keepRejtDoc	[옵션]반려된 문서 등록대장에 저장 - 옵션이 있으면 둘째 검토자 재검토 사용함
*/
--%><input type="hidden" name="noReRevwBy2nd" value="N" /><%--
/*
lastAbsEnab=마지막 결재자 공석 허용
	Y			허용
	N			허용안함(디폴트)
*/
--%><input type="hidden" name="lastAbsEnab" value="N" /><%--
/*
distDocLangTypCd=유통문서 어권 기준
	orgnDoc			원본문서의 어권
	session			세션
*/
--%><input type="hidden" name="distDocLangTypCd" value="orgnDoc" />

<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:saveOpt();" auth="A" />
</u:buttonArea>

</form>

