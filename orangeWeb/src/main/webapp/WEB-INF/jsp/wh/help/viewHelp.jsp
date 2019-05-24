<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.Date"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><fmt:formatDate var="today" value="<%=new Date() %>" type="date" pattern="yyyy-MM-dd"/>
<u:params var="params"/>
<u:params var="paramsForList" excludes="reqNo"/><style type="text/css">
ul.selectList{list-style:none;float:left;margin:0px;padding:0px;}
ul.selectList li{float:left;}
ul.selectList li.optionList{padding-left:5px;}
</style>
<script type="text/javascript">
<!--<% // [팝업] - 관련요청 %>
function viewRelReqPop(reqNo){
	var url='./viewRelReqPop.do?menuId=${param.menuId}';
	if(reqNo!=undefined)
		url+='&reqNo='+reqNo;
	dialog.open('viewRelReqDialog','<u:msg titleId="wh.cols.req.relReqNm" alt="관련요청" />', url);
}<% // [하단버튼:결과평가] %>
function setReqEvalPop(){
	dialog.open('setReqEvalDialog', '<u:msg titleId="wh.jsp.resEval.title" alt="결과평가" />', './setReqEvalPop.do?menuId=${menuId}&reqNo=${param.reqNo}');
}<% // [하단버튼:접수|반려|진행처리] %>
function setReqHdlPop(ongoCd){
	if(ongoCd===undefined)
		ongoCd=null;
	if(ongoCd=='A')
		dialog.open('setReqHdlDialog', '<u:msg titleId="wh.jsp.recv.title" alt="접수처리" />', './setReqHdlPop.do?menuId=${menuId}&reqNo=${param.reqNo}&ongoCd='+ongoCd);
	else if(ongoCd=='P')
		dialog.open('setReqHdlDialog', '<u:msg titleId="wh.jsp.progress.title" alt="진행처리" />', './setReqHdlPop.do?menuId=${menuId}&reqNo=${param.reqNo}&ongoCd='+ongoCd);
	else if(ongoCd=='C'){
		<c:if test="${!empty envConfigMap.cmplHdlDisp && envConfigMap.cmplHdlDisp eq 'popup' }">
		dialog.open('setReqHdlDialog', '<u:msg titleId="wh.jsp.complete.title" alt="완료처리" />', './setReqHdlPop.do?menuId=${menuId}&reqNo=${param.reqNo}&ongoCd='+ongoCd);
		</c:if>
		<c:if test="${!empty envConfigMap.cmplHdlDisp && envConfigMap.cmplHdlDisp eq 'page' }">
		location.href = './setReqHdl.do?menuId=${menuId}&reqNo=${param.reqNo}&ongoCd='+ongoCd;
		</c:if>
	}		 
	else
		dialog.open('setReqHdlDialog', '<u:msg titleId="wh.jsp.reject.title" alt="반려처리" />', './setReqHdlPop.do?menuId=${menuId}&reqNo=${param.reqNo}&ongoCd='+ongoCd);
	
}<% // [하단버튼:완료처리 수정] %>
function modReqHdlPop(ongoCd){
	<c:if test="${!empty envConfigMap.cmplHdlDisp && envConfigMap.cmplHdlDisp eq 'popup' }">
	dialog.open('setReqHdlDialog', '<u:msg titleId="wh.jsp.complete.title" alt="완료처리" />', './setCmplHdlPop.do?menuId=${menuId}&reqNo=${param.reqNo}&ongoCd='+ongoCd);
	</c:if>
	<c:if test="${!empty envConfigMap.cmplHdlDisp && envConfigMap.cmplHdlDisp eq 'page' }">
	location.href = './setCmplHdl.do?menuId=${menuId}&reqNo=${param.reqNo}&ongoCd='+ongoCd;
	</c:if>	
}<% // [하단버튼:수정] 수정 %>
function setHelp() {
	location.href = './${setPage}.do?${paramsForList}&reqNo=${param.reqNo}';
}<% // [하단버튼:삭제] 삭제 %>
function delHelp() {
	callAjax('./${transDelPage}Ajx.do?menuId=${menuId}', {reqNo:'${param.reqNo}'}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			location.href = './${listPage}.do?${paramsForList}';
		}
	});
}<% // [하단버튼:평가삭제] 삭제 %>
function delReqEval(){
	callAjax('./transEvalDelAjx.do?menuId=${menuId}', {reqNo:'${param.reqNo}'}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			location.replace(location.href); 
		}
	});
}<% // 리로드 %>
function reloadHp(isList) {
	if(isList) goList();
	else reload();
}<% // [하단버튼:목록] 목록으로 이동 %>
function goList() {
	location.href = './${listPage}.do?${paramsForList}';
}<% // [팝업] 파일목록 조회 %>
function viewFileListPop(statCd) {
	var url = './viewFileListPop.do?menuId=${menuId}&reqNo=${param.reqNo}&statCd='+statCd;
	parent.dialog.open('viewFileListDialog','<u:msg titleId="cols.att" alt="첨부" />', url);
}<% // [팝업] 완료처리 이력 조회 %>
function listCmplHstPop(){
	var url = './listCmplHstPop.do?menuId=${menuId}&reqNo=${param.reqNo}';
	parent.dialog.open('listCmplHstDialog','<u:msg titleId="wh.btn.cmpl.hstList" alt="이력목록" />', url);
}<% // [팝업] 진행처리 이력 조회 %>
function listOngdHstPop(){
	var url = './listOngdHstPop.do?menuId=${menuId}&reqNo=${param.reqNo}';
	parent.dialog.open('listOngdHstDialog','<u:msg titleId="wh.btn.cmpl.hstList" alt="이력목록" />', url);
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<c:if test="${empty pageSuffix}"><u:msg var="menuTitle" titleId="wh.jsp.${path }.title" />
<u:title title="${menuTitle }" menuNameFirst="true" alt="${menuTitle }" /></c:if>

<% // 폼 필드 %>
<u:title titleId="wh.jsp.req.small.title" alt="요청사항" type="small" notPrint="true">
<c:if test="${empty pageSuffix && isReqRel==true}"><u:titleButton titleId="wh.cols.req.relReqNm" onclick="viewRelReqPop('${whReqBVo.reqNo }');" alt="관련요청"/></c:if>
<c:if test="${envConfigMap.fileYn eq 'Y' && !empty whReqBVo.fileCnt && whReqBVo.fileCnt>0 }">
<u:titleButton titleId="wh.btn.fileList" onclick="viewFileListPop('R');" alt="파일목록"/>
</c:if>
</u:title>
<u:listArea id="listArea" colgroup="12%,38%,12%,38%">
	<tr>
	<td class="head_lt"><u:msg titleId="cols.prgStat" alt="진행상태"/></td>
	<td class="body_lt" colspan="3"><div style="font-weight:bold;"><u:msg titleId="wh.option.statCd${whReqOngdDVo.statCd }"/></div></td>
	</tr>	<tr>
	<td class="head_lt"><u:msg titleId="wh.cols.req.deptNm" alt="요청부서" /></td>
	<td class="body_lt"><u:out value="${whReqBVo.regrDeptNm }"/></td>
	<td class="head_lt"><u:msg titleId="wh.cols.req.reqr" alt="요청자" /></td>
	<td class="body_lt"><c:if test="${path eq 'req' && isAdmin==false}"><u:out value="${whReqBVo.regrNm }"/></c:if><c:if test="${path ne 'req' || isAdmin==true}"><a href="javascript:viewUserPop('${whReqBVo.regrUid }');"><u:out value="${whReqBVo.regrNm }"/></a></c:if></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="wh.jsp.sysMd.title" alt="시스템 모듈" /></td>
	<td class="body_lt"><ul id="sysMdArea" class="selectList"><c:forEach items="${paramMdList}" var="whMdBVoList" varStatus="paramStatus"
		><li><c:forEach items="${whMdBVoList}" var="whMdBVoVo" varStatus="status"><c:if test="${whMdBVoVo.mdId eq (empty paramMdIds ? whReqOngdDVo.mdId : paramMdIds[paramStatus.index])}"><c:if test="${paramStatus.index>0 }">&nbsp;&gt;&nbsp;</c:if>${whMdBVoVo.mdNm }</c:if></c:forEach></li></c:forEach></ul></td>
	<td class="head_lt"><u:msg titleId="wh.cols.hdl.pich" alt="처리담당자" /></td>
	<td class="body_lt"><c:if test="${!empty whReqOngdDVo.pichUid}"
	><c:if test="${(isAdmin==true || path ne 'req' || (path eq 'req' && empty envConfigMap.reqPichDtlNotUseYn)) }"
	><a href="javascript:viewUserPop('${whReqOngdDVo.pichUid }');">${whReqOngdDVo.pichNm }</a></c:if
	><c:if test="${!(isAdmin==true || path ne 'req' || (path eq 'req' && empty envConfigMap.reqPichDtlNotUseYn)) }"
	>${whReqOngdDVo.pichNm }</c:if></c:if></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="wh.cols.req.progrmId" alt="프로그램ID" /></td>
	<td class="body_lt"><u:out value="${whReqBVo.progrmId }" type="value"/></td>
	<td class="head_lt"><u:msg titleId="wh.cols.req.progrmNm" alt="프로그램명" /></td>
	<td class="body_lt"><u:out value="${whReqBVo.progrmNm }" type="value"/></td>
	</tr>
	<tr>
	<u:set var="writtenReqYn" test="${!empty whReqBVo.writtenReqYn && whReqBVo.writtenReqYn eq 'Y'}" value="Y" elseValue="N"/>
	<td class="head_lt"><u:msg titleId="wh.cols.req.writtenYn" alt="의뢰서여부" /></td>
	<td class="body_lt"><u:msg titleId="cm.option.${writtenReqYn eq 'Y' ? 'use' : 'notUse'}"/></td>
	<td class="head_lt"><u:msg titleId="wh.cols.req.writtenNo" alt="의뢰서번호" /></td>
	<td class="body_lt"><u:out value="${whReqBVo.writtenReqNo }" type="value"/></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td class="body_lt" colspan="3"><u:out value="${whReqBVo.subj }" type="value"/></td>
	</tr><c:if test="${reqEditorYn == 'Y'}"><tr>
	<td colspan="4" ><div style="overflow:auto;" class="editor printNoScroll"><u:clob lobHandler="${reqLobHandler }"/></div></td>
	</tr>
	</c:if><c:if test="${reqEditorYn == 'N'}"><tr>
	<td class="head_lt"><u:msg titleId="cols.cont" alt="내용" /></td>
	<td class="body_lt" colspan="3"><u:out value="${whReqBVo.cont }" type="html"/></td></tr></c:if>	
	<tr>
	<td class="head_lt"><u:msg titleId="wh.cols.req.reqYmd" alt="요청일" /></td>
	<td class="body_lt"><u:out value="${whReqBVo.reqDt }" type="date"/></td>
	<td class="head_lt"><u:msg titleId="wh.cols.req.cmplYmd" alt="완료희망일" /></td>
	<td class="body_lt"><u:out value="${whReqBVo.cmplPdt }" type="date"/></td>
	</tr>	
</u:listArea>
<u:blank />
<c:if test="${whReqOngdDVo.statCd ne 'R' && whReqOngdDVo.statCd ne 'T'}">
<c:if test="${(empty whReqOngdDVo.prevStatCd || whReqOngdDVo.prevStatCd eq 'A' || whReqOngdDVo.prevStatCd eq 'P') && ((path ne 'req' || (path eq 'req' && isAdmin==true)) || ( path eq 'req' && (whReqOngdDVo.statCd eq 'G' || envConfigMap.dtlRecvYn eq 'Y'))) }">
<u:title titleId="wh.jsp.recv.small.title" alt="접수사항" type="small" />
<u:listArea id="listArea" colgroup="12%,38%,12%,38%">
	<tr>
	<td class="head_lt"><u:msg titleId="wh.cols.recv.recvr" alt="접수자" /></td>
	<td class="body_lt"><a href="javascript:viewUserPop('${whReqOngdDVo.recvUid }');"><u:out value="${whReqOngdDVo.recvNm }"/></a></td>
	<td class="head_lt"><u:msg titleId="wh.cols.recv.recvYmd" alt="접수일" /></td>
	<td class="body_lt"><u:out value="${whReqOngdDVo.recvDt }" type="date"/></td>
	</tr>
	<c:if test="${whReqOngdDVo.statCd ne 'G' }">	<tr><td class="head_lt"><u:msg titleId="wh.cols.recv.devPich" alt="개발담당자" /></td>
	<td class="body_lt"><a href="javascript:viewUserPop('${whReqOngdDVo.pichUid }');"><u:out value="${whReqOngdDVo.pichNm }"/></a></td>
	<td class="head_lt"><u:msg titleId="wh.cols.hdl.typ" alt="처리유형" /></td>
	<td class="body_lt"><u:out value="${whReqOngdDVo.catNm }" /></td></tr></c:if>
	<td class="head_lt"><u:msg titleId="${whReqOngdDVo.statCd eq 'G' ? 'wh.cols.recv.rejectCont' : 'wh.cols.recv.recvCont' }" /></td>
	<td class="body_lt" colspan="3"><u:out value="${whReqOngdDVo.recvCont }"/></td>
</u:listArea>
</c:if>
<c:if test="${((empty whReqOngdDVo.prevStatCd || whReqOngdDVo.prevStatCd eq 'P') && (whReqOngdDVo.statCd eq 'P' || whReqOngdDVo.statCd eq 'C')) && ( (path ne 'req' || (path eq 'req' && isAdmin==true)) || ( path eq 'req' && envConfigMap.dtlHdlYn eq 'Y'))}">
<u:title titleId="wh.jsp.hdl.small.title" alt="처리사항" type="small" >
<c:if test="${isOngdHst eq 'Y' }"><u:titleButton titleId="wh.btn.cmpl.hstList" onclick="listOngdHstPop();" alt="이력목록"/></c:if>
</u:title>
<u:listArea id="listArea" colgroup="12%,38%,12%,38%">
	<tr>
	<td class="head_lt"><u:msg titleId="wh.cols.recv.devPich" alt="개발담당자" /></td>
	<td class="body_lt"><a href="javascript:viewUserPop('${whReqOngdDVo.pichUid }');"><u:out value="${whReqOngdDVo.pichNm }"/></a></td>
	<td class="head_lt"><u:msg titleId="wh.cols.hdl.dueDt" alt="처리예정일" /></td>
	<td class="body_lt"><u:out value="${whReqOngdDVo.cmplDueDt }" type="date"/></td>
	</tr><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.hdl.ongoCont" alt="진행사항"/></td>
	<td class="body_lt" colspan="3"><u:out value="${whReqOngdDVo.ongoCont }" type="html"/></td></tr>
</u:listArea>
</c:if>

<c:if test="${whReqOngdDVo.statCd eq 'C'  }">
<u:title titleId="wh.jsp.cmpl.small.title" alt="완료사항" type="small" notPrint="true">
<c:if test="${hstList eq 'Y' }"><u:titleButton titleId="wh.btn.cmpl.hstList" onclick="listCmplHstPop();" alt="이력목록"/></c:if>
<c:if test="${envConfigMap.fileYn eq 'Y' && !empty whReqCmplDVo.fileCnt && whReqCmplDVo.fileCnt>0 }"><u:titleButton titleId="wh.btn.fileList" onclick="viewFileListPop('C');" alt="파일목록"/></c:if>
</u:title>
<u:listArea id="listArea" colgroup="12%,38%,12%,38%"><tr><td class="head_lt"><u:msg titleId="wh.cols.hdl.typ" alt="처리유형" /></td>
	<td class="body_lt" colspan="3"><u:out value="${whReqOngdDVo.catNm }" /></td></tr>
<tr>
	<td class="head_lt"><u:msg titleId="wh.cols.hdl.pich" alt="처리담당자" /></td>
	<td class="body_lt"><a href="javascript:viewUserPop('${whReqOngdDVo.hdlrUid }');"><u:out value="${whReqOngdDVo.hdlrNm }"/></a></td>
	<td class="head_lt"><u:msg titleId="wh.cols.req.cmplDt" alt="완료일" /></td>
	<td class="body_lt"><u:out value="${whReqOngdDVo.cmplDt }" type="date"/></td>
	</tr><c:if test="${envConfigMap.devHourYn eq 'Y' }"><tr><td class="head_lt"><u:msg titleId="wh.cfg.devHour" alt="공수"/></td>
	<td class="body_lt" colspan="3"><u:out value="${whReqCmplDVo.devHourVa }" /></td></tr>
	</c:if><c:if test="${envConfigMap.hdlCompYn eq 'Y' }"><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.hdl.compNm" alt="처리업체명" /></td>
	<td class="body_lt"><u:out value="${whReqCmplDVo.hdlCompNm }" type="value"/></td>
	<td class="head_lt"><u:msg titleId="wh.cols.hdl.hdlCost" alt="처리비용" /></td>
	<td class="body_lt"><u:out value="${whReqCmplDVo.hdlCost }" type="number"/></td>
	</tr></c:if><c:if test="${envConfigMap.testInfoYn eq 'Y' }"><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.hdl.test" alt="테스트" /></td>
	<td colspan="3" style="padding:2px;"><u:listArea id="listArea" colgroup="15%,35%,15%,35%"><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.hdl.testPich" alt="테스트 담당자" /></td>
	<td class="body_lt"><a href="javascript:viewUserPop('${whReqCmplDVo.testPichUid }');"><u:out value="${whReqCmplDVo.testPichNm }" /></a></td>
	<td class="head_lt"><u:msg titleId="wh.cols.hdl.testDueDt" alt="테스트 기간" /></td>
	<td class="body_lt" ><u:out value="${whReqCmplDVo.strtDt }" type="date"/>~<u:out value="${whReqCmplDVo.endDt }" type="date"/></td>
	</tr><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.hdl.testList" alt="테스트 진행내역" /></td>
	<td class="body_lt" colspan="3"><u:textarea id="testOngoCont" titleId="wh.cols.hdl.testList" maxByte="400" style="width:95%" rows="5" value="${whReqCmplDVo.testOngoCont }" readonly="readonly"/></td>
	</tr><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.hdl.testResult" alt="테스트 결과" /></td>
	<td class="body_lt" colspan="3"><u:textarea id="testResCont" titleId="wh.cols.hdl.testResult" maxByte="400" style="width:95%" rows="5" value="${whReqCmplDVo.testResCont }" readonly="readonly"/></td>
	</tr>		
	</u:listArea></td></tr></c:if><c:if test="${cmplEditorYn == 'Y'}"><tr>
	<td colspan="4" ><div style="overflow:auto;" class="editor printNoScroll"><u:clob lobHandler="${cmplLobHandler }"/></div></td>
	</tr>
	</c:if><c:if test="${cmplEditorYn == 'N'}"><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.hdl.hdlCont" alt="완료처리 사항" /></td>
	<td class="body_lt" colspan="3"><u:out value="${whReqCmplDVo.hdlCont }" type="html"/></td></tr></c:if>
	</u:listArea>
	
	<c:if test="${envConfigMap.resEvalUseYn eq 'Y' && whReqOngdDVo.evalYn eq 'Y'  }">
	<u:title titleId="wh.jsp.eval.small.title" alt="결과평가" type="small" />
	<u:listArea id="listArea" colgroup="12%,38%,12%,38%"><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.eval.regr" alt="평가자" /></td>
	<td class="body_lt" colspan="3"><a href="javascript:viewUserPop('${whReqEvalDVo.regrUid }');"><u:out value="${whReqEvalDVo.regrNm }"/></a></td>
	</tr>	<tr>
	<td class="head_lt"><u:msg titleId="wh.cols.eval.score" alt="평가점수" /></td>
	<td class="body_lt"><u:out value="${whReqEvalDVo.evalNm }"/></td>
	<td class="head_lt"><u:msg titleId="wh.cols.eval.regDt" alt="평가일" /></td>
	<td class="body_lt"><u:out value="${whReqEvalDVo.regDt }" type="date"/></td>
	</tr><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.eval.rson" alt="평가사유"/></td>
	<td class="body_lt" colspan="3"><u:textarea id="evalRson" titleId="wh.cols.eval.rson" maxByte="400" style="width:95%" rows="5" value="${whReqEvalDVo.evalRson }" readonly="readonly" /></td>
	</tr><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.eval.addReqr" alt="추가요청사항"/></td>
	<td class="body_lt" colspan="3"><u:textarea id="addReqr" titleId="wh.cols.eval.addReqr" maxByte="400" style="width:95%" rows="5" value="${whReqEvalDVo.addReqr }" readonly="readonly" /></td>
</tr></u:listArea></c:if>

</c:if>
</c:if>
<c:if test="${empty pageSuffix }">
<% // 하단 버튼 %>
<u:buttonArea topBlank="true">
	<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" auth="R" />
	<c:if test="${path eq 'req' || isAdmin==true}">
		<c:if test="${whReqOngdDVo.statCd eq 'R' || whReqOngdDVo.statCd eq 'T' || whReqOngdDVo.statCd eq 'G'}">
		<u:button titleId="cm.btn.mod" alt="수정" onclick="setHelp();" auth="W" ownerUid="${whReqBVo.regrUid}" />	
		<u:button titleId="cm.btn.del" alt="삭제" onclick="delHelp();" auth="W" ownerUid="${whReqBVo.regrUid}" />
		</c:if><c:if test="${envConfigMap.resEvalUseYn eq 'Y' && whReqOngdDVo.statCd eq 'C'}"><c:if test="${whReqOngdDVo.evalYn eq 'N' && isAdmin==false }">
		<u:button titleId="wh.btn.resEval" alt="평가하기" onclick="setReqEvalPop();" auth="W" ownerUid="${whReqBVo.regrUid}" /></c:if
		><c:if test="${whReqOngdDVo.evalYn eq 'Y' }"><u:button titleId="wh.btn.del.resEval" alt="평가삭제" onclick="delReqEval();" auth="W" ownerUid="${whReqBVo.regrUid}" /></c:if>
		</c:if>		
	</c:if>
	<c:if test="${path eq 'recv' || isAdmin==true}">
	<c:if test="${whReqOngdDVo.statCd eq 'R' }"><u:button titleId="wh.btn.recv" alt="접수" onclick="setReqHdlPop('A');" auth="A"/>
	<u:button titleId="wh.btn.reject" alt="반려" onclick="setReqHdlPop('G');" auth="A"/>
	<c:if test="${envConfigMap.cmplRecvYn eq 'Y'}"><u:button titleId="wh.btn.complete" alt="완료처리" onclick="setReqHdlPop('C');" auth="A"/></c:if></c:if>
	<c:if test="${whReqOngdDVo.statCd eq 'A' && envConfigMap.recvModYn eq 'Y'}"><u:button titleId="wh.btn.recvMod" alt="접수변경" onclick="setReqHdlPop('A');" auth="A"/></c:if>	
	</c:if>
	<c:if test="${(path eq 'hdl' || isAdmin==true) && whReqOngdDVo.statCd eq 'A'}">
	<u:button titleId="wh.btn.progress" alt="진행처리" onclick="setReqHdlPop('P');" auth="A"/>
	<c:if test="${envConfigMap.cmplHdlYn eq 'Y'}"><u:button titleId="wh.btn.complete" alt="완료처리" onclick="setReqHdlPop('C');" auth="A"/></c:if>
	</c:if>
	<c:if test="${(path eq 'hdl' || isAdmin==true) && whReqOngdDVo.statCd eq 'P'}">
	<u:button titleId="wh.btn.complete" alt="완료처리" onclick="setReqHdlPop('C');" auth="A"/>
	<c:if test="${whReqOngdDVo.statCd eq 'P' && envConfigMap.hdlModYn eq 'Y'}"><u:button titleId="wh.btn.hdlMod" alt="처리변경" onclick="setReqHdlPop('P');" auth="A"/></c:if>	
	</c:if>
	<c:if test="${(path eq 'hdl' || isAdmin==true) && whReqOngdDVo.statCd eq 'C' && envConfigMap.cmplModYn eq 'Y'}">
	<u:button titleId="wh.btn.cmplMod" alt="완료처리 수정" onclick="modReqHdlPop('C');" auth="A"/>
	</c:if>
	<u:button titleId="cm.btn.list" alt="목록" onclick="goList();" />
</u:buttonArea>
</c:if>