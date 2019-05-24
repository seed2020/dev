<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<% // [하단버튼:항목설정] - 팝업 %>
function setExcelColListPop(url, title) {
	dialog.open('setExcelColListDialog', '<u:msg titleId="wh.btn.set.item" alt="항목설정" />', './setExcelColListPop.do?menuId=${menuId}');
}<%//checkbox 가 선택된 div ubox 테그 목록 리턴 %>
function getCheckedUboxs(noSelectMsg, areaId){
	var arr=[], obj;
	$("#"+areaId+" div.ubox input[type='checkbox']:checked").each(function(){
		obj = $(this).closest('div.ubox');
		if(obj!=undefined) arr.push(obj);
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
}<%// 선택삭제%>
function delSelUbox(areaId){
	var arr = getCheckedUboxs("cm.msg.noSelect", areaId);
	if(arr!=null) delUboxInArr(arr, areaId);
}<%// 삭제 - 배열에 담긴 목록%>
function delUboxInArr(arr, areaId){
	var delArr = [], $chkId;
	for(var i=0;i<arr.length;i++){
		$chkId = $(arr[i]).find("input[id='chkId']").val();
		if($chkId=='') continue;
		delArr.push($chkId);
	}
	if(delArr.length>0){
		var transUrl=areaId=='pichGrpList' ? 'transPichGrpDelAjx' : 'transCatGrpDelAjx';
		callAjax('./'+transUrl+'.do?menuId=${menuId}', {grpIds:delArr}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				$(arr).remove();
			}
		});
	}
}<% // [하단버튼:처리유형그룹] - 팝업 %>
function setCatGrpPop(id) {
	var url='./setCatGrpPop.do?menuId=${menuId}';
	if(id!=undefined)
		url+='&catGrpId='+id;
	setGrpPop(url, '<u:msg titleId="wh.jsp.hdlTyp.title" alt="처리유형" />');
}<% // [하단버튼:담당자그룹] - 팝업 %>
function setPichGrpPop(id) {
	var url='./setPichGrpPop.do?menuId=${menuId}';
	if(id!=undefined)
		url+='&pichGrpId='+id;
	setGrpPop(url, '<u:msg titleId="wh.jsp.pichGrp.title" alt="담당자그룹" />');
}<% // [하단버튼:결과평가] - 팝업 %>
function setResEvalPop(){
	var url='./setResEvalPop.do?menuId=${menuId}';
	setGrpPop(url, '<u:msg titleId="wh.jsp.resEval.title" alt="결과평가" />');
}<% // [하단버튼:처리유형그룹 | 결과평가] - 팝업 %>
function setGrpPop(url, title) {
	dialog.open('setGrpDialog', title, url);
}<% // [버튼클릭] - 처리유형그룹삭제  %>
function grpSelectDel(containerId, obj){
	var isAll=obj===undefined;
	var arr=isAll ? getDelList($('#'+containerId)) : getDelList($(obj).closest('div.ubox'));
	if(arr==null) return;
	var transUrl=containerId=='pichGrpList' ? 'transPichGrpDelAjx' : 'transCatGrpDelAjx';
	callAjax('./'+transUrl+'.do?menuId=${menuId}', {grpIds:arr}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			if(isAll) $('#'+containerId).html('');	
			else $(obj).closest('div.ubox').remove();
		}
	});
};
<% // 그룹번호 조회  %>
function getDelList(parent){
	var arr=[];
	$(parent).find('input[name="chkId"]').each(function(){
		arr.push($(this).val());
	});
	if(arr.length==0)
		return null;
	return arr;
}<% // 저장 - 버튼 클릭 %>
function save(){
	if (validator.validate('setEnvForm')) {
		var $form = $("#setEnvForm");
		$form.attr('method','post');
		$form.attr('action','./transEnv.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form.submit();	
	}
};
$(document).ready(function() {	
	setUniformCSS();
});
//-->
</script>

<u:title titleId="dm.jsp.setEnv" alt="환경설정" />
<c:set var="map" value="${envConfigMap }" scope="request"/>

<u:title titleId="wh.jsp.hdlTyp.title" alt="처리유형" type="small" >
<u:titleButton titleId="cm.btn.add" onclick="setCatGrpPop();" alt="추가"/><u:titleButton titleId="cm.btn.selDel" onclick="delSelUbox('groupList');" alt="선택삭제"/>
</u:title>
<div id="groupList" class="groupdiv" style="height:50px;"><c:forEach 
		var="whCatGrpBVo" items="${whCatGrpBVoList }" varStatus="status">
		<div class="ubox"><dl><dd 
		class="title"><input type="checkbox" /><a href="javascript:setCatGrpPop('${whCatGrpBVo.catGrpId }');"><c:if test="${whCatGrpBVo.dftYn eq 'Y' }"><strong>[<u:msg titleId="cm.option.dft" alt="기본"/>]</strong></c:if>${whCatGrpBVo.catGrpNm }</a><u:input type="hidden" id="chkId" value="${whCatGrpBVo.catGrpId }"/></dd>
		<dd class="btn"><a class="delete" onclick="grpSelectDel('groupList', this);" href="javascript:void(0);"><span>delete</span></a></dd></dl></div>
		</c:forEach>
</div>
<u:blank />
<u:title titleId="wh.jsp.pichGrp.title" alt="담당자그룹" type="small" >
<u:titleButton titleId="cm.btn.add" onclick="setPichGrpPop();" alt="추가"/><u:titleButton titleId="cm.btn.selDel" onclick="delSelUbox('pichGrpList');" alt="선택삭제"/>
</u:title>
<div id="pichGrpList" class="groupdiv" style="height:50px;"><c:forEach 
		var="whPichGrpBVo" items="${whPichGrpBVoList }" varStatus="status">
		<div class="ubox"><dl><dd 
		class="title"><input type="checkbox" /><a href="javascript:setPichGrpPop('${whPichGrpBVo.pichGrpId }');"><c:if test="${whPichGrpBVo.dftYn eq 'Y' }"><strong>[<u:msg titleId="cm.option.dft" alt="기본"/>]</strong></c:if>${whPichGrpBVo.pichGrpNm }</a><u:input type="hidden" id="chkId" value="${whPichGrpBVo.pichGrpId }"/></dd>
		<dd class="btn"><a class="delete" onclick="grpSelectDel('pichGrpList', this);" href="javascript:void(0);"><span>delete</span></a></dd></dl></div>
		</c:forEach>
</div>
<u:blank />
<u:title titleId="wh.jsp.resEval.title" alt="결과평가" type="small" >
<u:titleButton titleId="cols.mng" onclick="setResEvalPop();" alt="관리"/>
</u:title>
<div id="resEvalList" class="groupdiv" style="height:50px;"><c:forEach 
		var="whResEvalBVo" items="${whResEvalBVoList }" varStatus="status">
		<div class="ubox"><dl><dd 
		class="title" style="min-width:60px;text-align:center;"><u:out value="${whResEvalBVo.evalNm }" type="value"/><u:input type="hidden" id="chkId" value="${whResEvalBVo.evalNo }"/></dd>		</dl></div>
		</c:forEach>
</div>
<u:blank />
<form id="setEnvForm" method="post">
<input type="hidden" name="menuId" value="${menuId}" />
<u:title titleId="wh.jsp.req.small.title" alt="요청사항" type="small" />
<u:listArea colgroup="15%,*" noBottomBlank="true">
<tr>
	<td class="head_lt"><u:msg titleId="wh.cols.docNoDisp" alt="문서번호(표시)" /></td>
	<td class="bodybg_lt" ><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select id="docNoOpt1" name="docNoOpt1">
		<u:option value="notUse" titleId="cm.option.notUse" alt="사용안함" selected="${envConfigMap.docNoOpt1 == 'notUse'}"/>
		<u:option value="YYYY" titleId="wh.cfg.docNoYYYY" alt="연도(YYYY)" selected="${envConfigMap.docNoOpt1 == 'YYYY'}"/>
		<u:option value="YY" titleId="wh.cfg.docNoYY" alt="연도(YY)" selected="${envConfigMap.docNoOpt1 == 'YY'}"/>
		<u:option value="YYYYMMDD" titleId="wh.cfg.docNoYYYYMMDD" alt="연월일(YYYYMMDD)" selected="${envConfigMap.docNoOpt1 == 'YYYYMMDD'}"/>
		<u:option value="mdNm" titleId="wh.cols.md.nm" alt="모듈명" selected="${envConfigMap.docNoOpt1 == 'mdNm'}"/>
		<u:option value="orgNm" titleId="wh.cols.orgNm" alt="조직명" selected="${envConfigMap.docNoOpt1 == 'orgNm'}"/>
		<u:option value="orgAbs" titleId="wh.cols.orgAbs" alt="조직약어" selected="${envConfigMap.docNoOpt1 == 'orgAbs'}"/>
		</select></td>
		<td class="width15"></td>
		<td><select id="docNoOpt2" name="docNoOpt2" >
		<u:option value="notUse" titleId="cm.option.notUse" alt="사용안함" selected="${envConfigMap.docNoOpt2 == 'notUse'}"/>
		<u:option value="YYYY" titleId="wh.cfg.docNoYYYY" alt="연도(YYYY)" selected="${envConfigMap.docNoOpt2 == 'YYYY'}"/>
		<u:option value="YY" titleId="wh.cfg.docNoYY" alt="연도(YY)" selected="${envConfigMap.docNoOpt2 == 'YY'}"/>
		<u:option value="YYYYMMDD" titleId="wh.cfg.docNoYYYYMMDD" alt="연월일(YYYYMMDD)" selected="${envConfigMap.docNoOpt2 == 'YYYYMMDD'}"/>
		<u:option value="mdNm" titleId="wh.cols.md.nm" alt="모듈명" selected="${envConfigMap.docNoOpt2 == 'mdNm'}"/>
		<u:option value="orgNm" titleId="wh.cols.orgNm" alt="조직명" selected="${envConfigMap.docNoOpt2 == 'orgNm'}"/>
		<u:option value="orgAbs" titleId="wh.cols.orgAbs" alt="조직약어" selected="${envConfigMap.docNoOpt2 == 'orgAbs'}"/>
		</select></td>
		<td class="width15"></td>
		<td><select id="docNoOpt3" name="docNoOpt3">
		<u:option value="notUse" titleId="cm.option.notUse" alt="사용안함" selected="${envConfigMap.docNoOpt3 == 'notUse'}"/>
		<u:option value="YYYY" titleId="wh.cfg.docNoYYYY" alt="연도(YYYY)" selected="${envConfigMap.docNoOpt3 == 'YYYY'}"/>
		<u:option value="YY" titleId="wh.cfg.docNoYY" alt="연도(YY)" selected="${envConfigMap.docNoOpt3 == 'YY'}"/>
		<u:option value="YYYYMMDD" titleId="wh.cfg.docNoYYYYMMDD" alt="연월일(YYYYMMDD)" selected="${envConfigMap.docNoOpt3 == 'YYYYMMDD'}"/>
		<u:option value="mdNm" titleId="wh.cols.md.nm" alt="모듈명" selected="${envConfigMap.docNoOpt3 == 'mdNm'}"/>
		<u:option value="orgNm" titleId="wh.cols.orgNm" alt="조직명" selected="${envConfigMap.docNoOpt3 == 'orgNm'}"/>
		<u:option value="orgAbs" titleId="wh.cols.orgAbs" alt="조직약어" selected="${envConfigMap.docNoOpt3 == 'orgAbs'}"/>
		</select></td>
		<td class="width15"></td>
		<td class="bodyip_lt"><strong><u:msg titleId="wh.cfg.docNoSeqLen" alt="일련번호"/></strong></td>
		<td><select id="docNoSeqLen" name="docNoSeqLen">
		<u:option value="3" titleId="ap.cfg.len3" alt="3자리" selected="${envConfigMap.docNoSeqLen == '3'}"/>
		<u:option value="4" titleId="ap.cfg.len4" alt="4자리" selected="${empty envConfigMap.docNoSeqLen or envConfigMap.docNoSeqLen == '4'}"/>
		<u:option value="5" titleId="ap.cfg.len5" alt="5자리" selected="${envConfigMap.docNoSeqLen == '5'}"/>
		<u:option value="6" titleId="ap.cfg.len6" alt="6자리" selected="${envConfigMap.docNoSeqLen == '6'}"/>
		<u:option value="7" titleId="ap.cfg.len7" alt="7자리" selected="${envConfigMap.docNoSeqLen == '7'}"/>
		<u:option value="8" titleId="ap.cfg.len8" alt="8자리" selected="${envConfigMap.docNoSeqLen == '8'}"/>
		</select></td>
		<td class="width15"></td>
		<u:checkbox value="Y" name="docNoFxLen" titleId="wh.cfg.docNoFxLen" alt="고정 길이 일련번호" checked="${envConfigMap.docNoFxLen == 'Y'}" />
		<td class="width15"></td>
		<u:checkbox value="Y" name="useDash" titleId="ap.cfg.useDash" alt="대시(-) 사용" checked="${envConfigMap.useDash eq 'Y'}" />
		</tr>
		</table>
	</td>
</tr><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.docNoDft" alt="문서번호(채번기준)" /></td>
	<td class="bodybg_lt" ><u:set var="recoDtDisabled" test="${envConfigMap.docNoDftYear == 'Y'}" value="" elseValue="disabled=\"disabled\""/><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="bodyip_lt"><strong><u:msg titleId="dm.option.year" alt="연도"/></strong></td>
		<td><select id="docNoDftYear" name="docNoDftYear" >
		<u:option value="N" titleId="cm.option.notUse" alt="사용안함" selected="${envConfigMap.docNoDftYear == 'N'}"/>
		<u:option value="Y" titleId="cm.option.use" alt="사용" selected="${envConfigMap.docNoDftYear == 'Y'}"/>		
		</select></td>
		<td class="width15"></td>
		<td class="bodyip_lt"><strong><u:msg titleId="wh.cfg.recoDt" alt="연도 기준일"/></strong></td>
		<td><select id="recoMt" name="recoMt" >
			<c:forEach items="${months}" var="month" varStatus="status">
			<u:option value="${month}" termId="cm.m.${month}" selected="${envConfigMap.recoMt == month}"
			/></c:forEach>
			</select></td>
		<td class="width5"></td>
		<td><select id="recoDt" name="recoDt" >
			<c:forEach items="${days}" var="day" varStatus="status">
			<u:option value="${day}" termId="cm.d.${day}" selected="${envConfigMap.recoMt == day}"
			/></c:forEach>
			</select></td>
		<td class="width15"></td>		
		<td class="bodyip_lt"><strong><u:msg titleId="dm.option.org" alt="조직"/></strong></td>
		<td><select id="docNoDftOrg" name="docNoDftOrg" >
		<u:option value="N" titleId="cm.option.notUse" alt="사용안함" selected="${envConfigMap.docNoDftOrg == 'N'}"/>
		<u:option value="Y" titleId="cm.option.use" alt="사용" selected="${envConfigMap.docNoDftOrg == 'Y'}"/>
		</select></td>
		</tr>
		</table>
	</td>
</tr><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.req.dtl" alt="상세정보(요청자)" /></td>
	<td class="body_lt"><u:checkArea><u:checkbox value="Y" name="dtlRecvYn" titleId="wh.cols.recv" alt="접수사항" checked="${envConfigMap.dtlRecvYn eq 'Y'}" 
	/><u:checkbox value="Y" name="dtlHdlYn" titleId="wh.cols.hdl" alt="처리사항" checked="${envConfigMap.dtlHdlYn eq 'Y'}" /></u:checkArea></td>	
</tr><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.mail.send.pich" alt="담당자에게 메일 발송" /></td>
	<td class="body_lt"><u:checkArea>
				<u:radio name="mailSendPich" value="in" titleId="wh.cols.mail.in" alt="내부메일" checkValue="${envConfigMap.mailSendPich }"  inputClass="bodybg_lt" />
				<u:radio name="mailSendPich" value="out" titleId="wh.cols.mail.out" alt="외부메일" checkValue="${envConfigMap.mailSendPich }" inputClass="bodybg_lt" />
				<u:radio name="mailSendPich" value="none" titleId="wh.cols.mail.notSend" alt="메일발송안함" checkValue="${envConfigMap.mailSendPich }"  inputClass="bodybg_lt" checked="${empty envConfigMap.mailSendPich }"/>
			</u:checkArea></td>	
</tr><tr>
	<td class="head_lt"><u:msg titleId="wh.cfg.etc.opt" alt="기타옵션" /></td>
	<td class="body_lt"><u:checkArea><u:checkbox value="Y" name="reqEditorYn" titleId="wh.cols.editor.use" alt="에디터사용" checked="${envConfigMap.reqEditorYn eq 'Y'}" 
	/><u:checkbox value="Y" name="reqPichDtlNotUseYn" titleId="wh.cols.pichDtl.notUse" alt="처리담당자 상세보기 사용안함" checked="${envConfigMap.reqPichDtlNotUseYn eq 'Y'}" 
	/></u:checkArea></td>	
</tr><%-- <tr>
	<td class="head_lt"><u:msg titleId="cols.sortOrdr" alt="정렬순서" /></td>
	<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="sortOptVa">
		<u:option value="regDt" titleId="cols.regDt" alt="등록일시" selected="${empty envConfigMap.sortOptVa || envConfigMap.sortOptVa == 'regDt'}" />
		<u:option value="reqDt" titleId="wh.cols.reqDt" alt="요청일시" selected="${envConfigMap.sortOptVa == 'reqDt'}"/>
		</select></td>
		<td class="width15"></td><td><select name="dataSortVa">
		<u:option value="desc" titleId="cm.sort.desc" alt="오름차순" selected="${empty envConfigMap.dataSortVa || envConfigMap.dataSortVa == 'desc'}" />
		<u:option value="asc" titleId="cm.sort.asc" alt="내림차순" selected="${envConfigMap.dataSortVa == 'asc'}"/>
		</select></td></tr></table></td>	
</tr> --%></u:listArea>
<u:blank />
<u:title titleId="wh.jsp.recv.small.title" alt="접수사항" type="small" />
<u:listArea colgroup="15%,*" noBottomBlank="true">
<%-- <tr>
	<td class="head_lt"><u:msg titleId="wh.cfg.lst.opt" alt="목록조회 기준" /></td>
	<td class="body_lt"><u:checkArea>
				<u:radio name="recvLstOpt" value="menu" titleId="wh.cfg.lst.opt.menu" alt="메뉴" checkValue="${envConfigMap.recvLstOpt }"  inputClass="bodybg_lt" />
				<u:radio name="recvLstOpt" value="pich" titleId="wh.cfg.lst.opt.pich" alt="담당자" checkValue="${envConfigMap.recvLstOpt }" inputClass="bodybg_lt" checked="${empty envConfigMap.recvLstOpt }" />
			</u:checkArea></td>	
</tr> --%><tr>
	<td class="head_lt"><u:msg titleId="wh.cfg.etc.opt" alt="기타옵션" /></td>
	<td class="body_lt"><u:checkArea><u:checkbox value="Y" name="cmplRecvYn" titleId="wh.cols.cmpl.use" alt="완료처리 가능" checked="${envConfigMap.cmplRecvYn eq 'Y'}" 
	/><u:checkbox value="Y" name="recvModYn" titleId="wh.cols.recvMod" alt="접수변경가능" checked="${envConfigMap.recvModYn eq 'Y'}" 
	/><%-- <u:checkbox value="Y" name="recvNotChkYn" titleId="wh.cols.required.notChk" alt="필수항목 체크안함" checked="${envConfigMap.recvNotChkYn eq 'Y'}" /> --%></u:checkArea></td>	
</tr>
</u:listArea>
<u:blank />
<u:title titleId="wh.jsp.hdl.small.title" alt="처리사항" type="small" />
<u:listArea colgroup="15%,*" noBottomBlank="true">
<tr>
	<td class="head_lt"><u:msg titleId="wh.cfg.lst.opt" alt="목록조회 기준" /></td>
	<td class="body_lt"><u:checkArea>
				<u:radio name="hdlLstOpt" value="menu" titleId="wh.cfg.lst.opt.menu" alt="메뉴" checkValue="${envConfigMap.hdlLstOpt }"  inputClass="bodybg_lt" />
				<u:radio name="hdlLstOpt" value="pich" titleId="wh.cfg.lst.opt.pich" alt="담당자" checkValue="${envConfigMap.hdlLstOpt }" inputClass="bodybg_lt" checked="${empty envConfigMap.hdlLstOpt }" />
			</u:checkArea></td>	
</tr><tr>
	<td class="head_lt"><u:msg titleId="wh.cfg.etc.opt" alt="기타옵션" /></td>
	<td class="body_lt"><u:checkArea><u:checkbox value="Y" name="cmplHdlYn" titleId="wh.cols.cmpl.use" alt="완료처리 가능" checked="${envConfigMap.cmplHdlYn eq 'Y'}" 
	/><u:checkbox value="Y" name="hdlModYn" titleId="wh.cols.hdlMod" alt="처리변경가능" checked="${envConfigMap.hdlModYn eq 'Y'}" /></u:checkArea></td>	
</tr>
</u:listArea>
<u:blank />
<u:title titleId="wh.cols.cmpl" alt="완료사항" type="small" />
<u:listArea colgroup="15%," noBottomBlank="true">
<tr>
	<td class="head_lt"><u:msg titleId="wh.cfg.addItem" alt="추가항목" /></td>
	<td class="body_lt"><u:checkArea><u:checkbox value="Y" name="hdlCompYn" titleId="wh.cfg.hdlComp" alt="처리업체" checked="${envConfigMap.hdlCompYn eq 'Y'}" 
	/><u:checkbox value="Y" name="testInfoYn" titleId="wh.cfg.testInfo" alt="테스트" checked="${envConfigMap.testInfoYn eq 'Y'}" 
	/><u:checkbox value="Y" name="devHourYn" titleId="wh.cfg.devHour" alt="개발공수" checked="${envConfigMap.devHourYn eq 'Y'}" /></u:checkArea></td>
</tr><tr>
	<td class="head_lt"><u:msg titleId="wh.cfg.cmplHdlDisp" alt="완료처리 화면" /></td>
	<td class="body_lt"><u:checkArea>
				<u:radio name="cmplHdlDisp" value="page" titleId="wh.cfg.cmplHdlDisp.page" alt="페이지" checkValue="${envConfigMap.cmplHdlDisp }"  inputClass="bodybg_lt" />
				<u:radio name="cmplHdlDisp" value="popup" titleId="wh.cfg.cmplHdlDisp.popup" alt="팝업" checkValue="${envConfigMap.cmplHdlDisp }" inputClass="bodybg_lt" checked="${empty envConfigMap.cmplHdlDisp }" />
			</u:checkArea></td>
</tr><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.mail.tgt" alt="메일발송대상" /></td>
	<td class="body_lt"><u:checkArea>
				<u:radio name="mailSendTgt" value="in" titleId="wh.cols.mail.in" alt="내부메일" checkValue="${envConfigMap.mailSendTgt }"  inputClass="bodybg_lt" />
				<u:radio name="mailSendTgt" value="out" titleId="wh.cols.mail.out" alt="외부메일" checkValue="${envConfigMap.mailSendTgt }" inputClass="bodybg_lt" />
				<u:radio name="mailSendTgt" value="none" titleId="wh.cols.mail.notSend" alt="메일발송안함" checkValue="${envConfigMap.mailSendTgt }"  inputClass="bodybg_lt" checked="${empty envConfigMap.mailSendTgt }"/>
			</u:checkArea></td>
</tr><tr>
	<td class="head_lt"><u:msg titleId="wh.cfg.etc.opt" alt="기타옵션" /></td>
	<td class="body_lt"><u:checkArea><u:checkbox value="Y" name="cmplEditorYn" titleId="wh.cols.editor.use" alt="에디터사용" checked="${envConfigMap.cmplEditorYn eq 'Y'}" 
	/><u:checkbox value="Y" name="cmplModYn" titleId="wh.cols.cmpl.mod" alt="수정가능" checked="${envConfigMap.cmplModYn eq 'Y'}" 
	/><u:checkbox value="Y" name="cmplHstYn" titleId="wh.cols.cmpl.history" alt="이력생성가능" checked="${envConfigMap.cmplHstYn eq 'Y'}" 
	/></u:checkArea></td>	
</tr>
</u:listArea>
<u:blank />
<u:title titleId="wl.cols.etc" alt="기타" type="small" />
<u:listArea colgroup="15%," noBottomBlank="true">
<tr>
	<td class="head_lt"><u:msg titleId="wh.cols.hdl.dueDt" alt="처리예정일" /></td>
	<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="bodyip_lt"><u:msg titleId="wh.jsp.hdl.dueDt.txt01" alt="진행일로부터" /></td>
		<td class="width10"></td>
		<td><select name="dueDt" <u:elemTitle titleId="wh.cols.hdl.dueDt" />><c:forEach var="date" begin="1" end="100" step="1" varStatus="status">
			<u:option value="${date }" title="${date }" checkValue="${envConfigMap.dueDt }" selected="${empty envConfigMap.dueDt && date==14 }"/></c:forEach></select></td>
		<td class="width10"></td>
		<td class="bodyip_lt"><u:msg titleId="wl.cols.day" alt="일"/></td>
		<%-- <td class="width10"></td>
		<td><u:checkArea><u:checkbox name="holidayYn" value="Y" titleId="wh.option.inclHoliDay" alt="휴일포함" checkValue="${envConfigMap.holidayYn}"/></u:checkArea></td> --%>
		</tr>
		</table></td>	
</tr><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.resEval.useYn" alt="결과평가 사용여부" /></td>
	<td class="body_lt"><u:checkArea>
				<u:radio name="resEvalUseYn" value="Y" titleId="cm.option.use" alt="사용" checkValue="${envConfigMap.resEvalUseYn }"  inputClass="bodybg_lt" checked="${empty envConfigMap.resEvalUseYn }"/>
				<u:radio name="resEvalUseYn" value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${envConfigMap.resEvalUseYn }" inputClass="bodybg_lt" />
			</u:checkArea></td>	
</tr><tr>
	<td class="head_lt"><u:msg titleId="wl.cols.fileUseYn" alt="첨부파일 사용" /></td>
	<td class="bodybg_lt"><u:checkArea><u:checkbox value="Y" name="fileYn" titleId="cm.option.use" alt="사용" checked="${envConfigMap.fileYn eq 'Y'}" /></u:checkArea></td>	
</tr><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.list.mdNmDisp" alt="목록 모듈명(표시)" /></td>
	<td class="bodybg_lt"><u:checkArea>
				<u:radio name="mdNmDisp" value="reg" titleId="wh.cfg.mdNmDisp.reg" alt="등록모듈명" checkValue="${envConfigMap.mdNmDisp }"  inputClass="bodybg_lt" checked="${empty envConfigMap.mdNmDisp }"/>
				<u:radio name="mdNmDisp" value="top" titleId="wh.cfg.mdNmDisp.top" alt="최상위모듈명" checkValue="${envConfigMap.mdNmDisp }" inputClass="bodybg_lt" />
			</u:checkArea></td>	
</tr><%-- <tr>
	<td class="head_lt"><u:msg titleId="cm.btn.excelDown" alt="엑셀다운" /></td>
	<td class="bodybg_lt"><u:buttonS href="javascript:setExcelColListPop();" titleId="wh.btn.set.item" alt="항목설정" auth="A" /></td>	
</tr> --%>
</u:listArea>
</form>
<u:blank />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:save();" auth="A" />
</u:buttonArea>
