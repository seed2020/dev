<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.Date"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="params"/>
<u:params var="paramsForList" excludes="reqNo"/>
<script type="text/javascript">
<!--
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<div style="padding:10px;">
<form id="setForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="listPage" value="./${listPage}.do?${paramsForList}" />
<u:input type="hidden" id="viewPage" value="./${viewPage}.do?${params}" />
<u:input type="hidden" id="compId" value="${whReqOngdDVo.mdId }" />

<u:title titleId="wh.jsp.req.small.title" alt="요청사항" type="small" notPrint="true">
<u:titleButton titleId="wh.cols.req.relReqNm" onclick="setRelReqPop();" alt="관련요청"/>
</u:title>
<% // 폼 필드 %>
<div class="listarea" id="listArea">
<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
<colgroup><col width="12%"/><col width="38%"/><col width="12%"/><col width="38%"/></colgroup>
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="wh.cols.req.deptNm" alt="요청부서" /></td>
	<td class="body_lt"><u:out value="${sessionScope.userVo.deptNm }"/></td>
	<td class="head_lt"><u:mandatory /><u:msg titleId="wh.cols.req.reqr" alt="요청자" /></td>
	<td class="body_lt"><u:out value="${sessionScope.userVo.userNm }"/></td>
	</tr>
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="wh.jsp.sysMd.title" alt="시스템 모듈" /></td>
	<td class="body_lt"><u:set var="paramMdId" test="${!empty whReqOngdDVo && !empty whReqOngdDVo.mdId}" value="${whReqOngdDVo.mdId }" elseValue="${param.mdId }"/><div id="sysMdContainer" style="width:100%;overflow-y:auto;"><ul id="sysMdArea" class="selectList"><c:forEach items="${paramMdList}" var="whMdBVoList" varStatus="paramStatus"
		><li><select onchange="selectSysMdList(this);" style="min-width:100px;"><u:option value="" titleId="cm.select.actname" alt="선택"
		/><c:forEach items="${whMdBVoList}" var="whMdBVoVo" varStatus="status"><u:option value="${whMdBVoVo.mdId }" title="${whMdBVoVo.mdNm }" checkValue="${empty paramMdIds ? paramMdId : paramMdIds[paramStatus.index]}" /></c:forEach></select></li></c:forEach></ul></div></td>
	<td class="head_lt"><u:msg titleId="wh.cols.hdl.pich" alt="처리담당자" /></td>
	<td class="body_lt"><div id="mdPichContainer"><select name="pichUid" style="min-width:100px;"><u:option value="" titleId="cm.select.actname" alt="선택"
		/><c:if test="${!empty whMdPichLVoList }"><c:forEach var="whMdPichLVo" items="${whMdPichLVoList }" varStatus="status"><u:option value="${whMdPichLVo.idVa }" title="${whMdPichLVo.pichNm }" checkValue="${whReqOngdDVo.pichUid }"/></c:forEach></c:if></select></div></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="wh.cols.req.progrmId" alt="프로그램ID" /></td>
	<td class="body_lt"><u:input id="progrmId" titleId="wh.cols.req.progrmId" style="width:98%;" value="${whReqBVo.progrmId }" maxByte="120"/></td>
	<td class="head_lt"><u:msg titleId="wh.cols.req.progrmNm" alt="프로그램명" /></td>
	<td class="body_lt"><u:input id="progrmNm" titleId="wh.cols.req.progrmNm" style="width:98%;" value="${whReqBVo.progrmNm }" maxByte="240"/></td>
	</tr>
	<tr>
	<u:set var="writtenReqYn" test="${!empty whReqBVo.writtenReqYn && whReqBVo.writtenReqYn eq 'Y'}" value="Y" elseValue="N"/>
	<td class="head_lt"><u:msg titleId="wh.cols.req.writtenYn" alt="의뢰서여부" /></td>
	<td class="body_lt"><u:checkArea>
				<u:radio name="writtenReqYn" value="Y" titleId="cm.option.use" alt="사용" checkValue="${writtenReqYn}"  inputClass="bodybg_lt" onclick="resEvalUseChange(this);"/>
				<u:radio name="writtenReqYn" value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${writtenReqYn }" inputClass="bodybg_lt" onclick="resEvalUseChange(this);"/>
			</u:checkArea></td>
	<td class="head_lt"><u:msg titleId="wh.cols.req.writtenNo" alt="의뢰서번호" /></td>
	<td class="body_lt"><u:input id="writtenReqNo" titleId="wh.cols.req.writtenNo" style="width:98%;" value="${whReqBVo.writtenReqNo }" maxByte="120" disabled="${writtenReqYn eq 'N' ? 'Y' : 'N'}"/></td>
	</tr>
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.subj" alt="제목" /></td>
	<td class="body_lt" colspan="3"><u:input id="subj" titleId="cols.subj" style="width:98%;" value="${whReqBVo.subj }" mandatory="Y" maxByte="240"/></td>
	</tr>
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="wh.cols.req.reqYmd" alt="요청일" /></td>
	<td class="body_lt"><u:calendar id="reqDt" titleId="wh.cols.req.reqYmd" option="{checkHandler:onChgTodayChk}" value="${!empty whReqBVo.reqDt ? whReqBVo.reqDt : today}" /></td>
	<td class="head_lt"><u:mandatory /><u:msg titleId="wh.cols.req.cmplYmd" alt="완료희망일" /></td>
	<td class="body_lt"><u:calendar id="cmplPdt" titleId="wh.cols.req.cmplYmd" option="{checkHandler:onChgTodayChk}" value="${whReqBVo.cmplPdt}" mandatory="Y" /></td>
	</tr>
	<c:if test="${isEditor == false}">
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.cont" alt="내용" /></td>
	<td class="body_lt" colspan="3"><u:textarea id="cont" titleId="cols.cont" maxByte="8000" style="width:95%" rows="15" value="${whReqBVo.cont }"/></td>
	</tr>
	</c:if>
</table>
</div>
<c:if test="${envConfigMap.fileYn eq 'Y' }">
<u:blank />
<u:listArea>
	<tr>
	<td><u:files id="${filesId}" fileVoList="${fileVoList}" module="wh" mode="set" exts="${exts }" extsTyp="${extsTyp }"/></td>
	</tr>
</u:listArea>
</c:if>
</form>
<u:blank />
<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" onclick="saveReq();" auth="W" />
	<u:button titleId="cm.btn.cancel" alt="취소" href="javascript:history.go(-1);" />
</u:buttonArea>
</div>