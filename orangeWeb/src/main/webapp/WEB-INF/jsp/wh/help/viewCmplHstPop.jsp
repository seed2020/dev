<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="params"/>
<script type="text/javascript">
<!--
<% // [팝업] 파일목록 조회 %>
function viewHstFileListPop(statCd) {
	var url = './viewFileListPop.do?menuId=${menuId}&reqNo=${param.hstNo}&statCd=H';
	dialog.open('viewHstFileListDialog','<u:msg titleId="cols.att" alt="첨부" />', url);
	dialog.onClose("listCmplHstDialog", function(){ dialog.close('viewHstFileListDialog'); });
}
//-->
</script>
<div style="width:680px;">
<u:title titleId="wh.jsp.cmpl.small.title" alt="완료사항" type="small" notPrint="true">
<c:if test="${envConfigMap.fileYn eq 'Y' && !empty whReqCmplDVo.fileCnt && whReqCmplDVo.fileCnt>0 }"><u:titleButton titleId="wh.btn.fileList" onclick="viewHstFileListPop();" alt="파일목록"/></c:if>
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
<u:blank />
<u:buttonArea>
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</div>