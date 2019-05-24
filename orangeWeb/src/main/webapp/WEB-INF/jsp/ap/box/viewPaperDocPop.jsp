<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--<%
// 분류정보 - 버튼클릭 - 팝업 호출 %>
function openClsInfo() {
	var clsInfoId = $("#setPaperDocForm").find("#clsInfoId").val();
	dialog.open('setClsInfoDialog','<u:msg titleId="cols.clsInfo" alt="분류정보" />','./treeOrgClsPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&setArea=setPaperDocForm'+(clsInfoId!='' && clsInfoId!=null ? '&clsInfoId='+clsInfoId : ''));
	dialog.onClose('setPaperDocDialog', function(){dialog.close("setClsInfoDialog");});
}<%
// 결재자, 업무담당자 선택 - which( cmplr:결재자, pich:업무담당자 )%>
function openUserPop(which){
	var $form = $("#setPaperDocForm");
	var data = {userUid:$form.find("#"+which+"Uid").val()};
	searchUserPop({data:data}, function(userVo){
		if(userVo!=null){
			$form.find("#"+which+"Uid").val(userVo.userUid);
			$form.find("#"+which+"Nm").val(userVo.rescNm);
		}
	});
}<%
// 오류방지용 - 문서번호세팅 %>
function setDocNo(){}<%
// 저장 클릭 %>
function savePaperDoc(){
	if(!validator.validate("setPaperDocForm")) return;
	var $form = $("#setPaperDocForm");
	<c:if test="${optConfigMap.catEnab == 'Y'}">
	if($form.find("input[name='clsInfoId']").val()==''){<%
		// cm.select.check.mandatory="{0}"(을)를 선택해 주십시요. - 분류 정보 %>
		alertMsg("cm.select.check.mandatory",["#ap.jsp.setOrgEnv.tab.deptCls"]);
		return;
	}</c:if>
	$form.attr("method", "post");
	$form.attr("action", "./transPaperDoc.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}");
	$form.attr('target','dataframe');
	$form.submit();
}
//$(document).ready(function() {
//});
//-->
</script>

<div style="width:750px">
<form id="setPaperDocForm" method="post" enctype="multipart/form-data"><c:if
	test="${not empty spReg and param.mode == 'spReg'}">
<u:input id="orgnApvNo" type="hidden" value="${orgnApvNo}" /></c:if><c:if
	test="${not (not empty spReg and param.mode == 'spReg') and not empty param.apvNo}">
<u:input id="apvNo" type="hidden" value="${param.apvNo}" /></c:if>
<u:input id="bxId" type="hidden" value="${param.bxId}" />
<% // 폼 필드 %>
<u:listArea colgroup="18%,32%,18%,32%">
	<tr>
	<td class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td class="body_lt" colspan="3"><u:out value="${apOngdBVo.docSubj}" /></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="ap.doc.docNo" alt="문서번호" /></td>
	<td class="body_lt" colspan="3"><u:out value="${apOngdBVo.docNo}" /></td>
	</tr>
	
	<c:if test="${optConfigMap.catEnab == 'Y'}">
	<tr>
	<td class="head_lt"><u:msg titleId="cols.clsInfo" alt="분류정보" /></td>
	<td class="body_lt" colspan="3"><u:out value="${apOngdBVo.clsInfoNm}" /></td>
	</tr>
	</c:if>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.extnDocTyp" alt="외부문서구분" /></td>
	<td class="body_lt"><u:out value="${apOngdExtnDocDVo.extnDocTypNm}" /></td><c:if
	
		test="${not empty spReg}">
	<td class="head_lt"><u:msg titleId="cols.extnDocContTyp" alt="외부문서컨텐츠구분" /></td>
	
	<td><select id="extnDocContTypCd" name="extnDocContTypCd"<u:elemTitle titleId="cols.extnDocContTyp" alt="외부문서컨텐츠구분" />><c:forEach
		items="${extnDocContTypCdList}" var="extnDocContTypCd">
		<u:option value="${extnDocContTypCd.cd}" title="${extnDocContTypCd.rescNm}" selected="${apOngdExtnDocDVo.extnDocContTypCd == extnDocContTypCd.cd}" /></c:forEach>
		</select></td></c:if><c:if
	
		test="${empty spReg}">
	<td class="head_lt"><u:msg titleId="cols.extnDocContTyp" alt="외부문서컨텐츠구분" /></td>
	<td class="body_lt"><u:out value="${apOngdExtnDocDVo.extnDocContTypNm}" /></td></c:if>
	
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.secul" alt="보안등급" /></td>
	<td class="body_lt" colspan="3"><c:if
		test="${not empty apOngdBVo.seculNm}" ><u:out value="${apOngdBVo.seculNm}" nullValue="" /> </c:if><c:if
		test="${not empty apOngdBVo.docPwEnc}" >[<u:msg titleId="cols.pw" alt="비밀번호" />]</c:if></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.readScop" alt="열람범위" /></td>
	<td class="body_lt"><c:if
		test="${apOngdBVo.allReadYn == 'Y'}" ><u:msg titleId="ap.option.all" alt="전체" /></c:if><c:if
		test="${apOngdBVo.allReadYn != 'Y'}" ><u:msg titleId="ap.option.dept" alt="부서" /></c:if></td>
	<td class="head_lt"><u:msg titleId="cols.prsvPrd" alt="보존기간" /></td>
	<td class="body_lt"><c:if
			test="${param.bxId!='recvRecLst'}"><u:out value="${apOngdBVo.docKeepPrdNm}" /></c:if><c:if
			test="${param.bxId=='recvRecLst'}"><u:out value="${apOngdBVo.enfcDocKeepPrdNm}" /></c:if>
	</td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="ap.cols.pich" alt="업무담당자" /></td>
	<td class="body_lt"><c:if
			test="${not empty apOngdExtnDocDVo.pichUid}"
			><a href="javascript:viewUserPop('${apOngdExtnDocDVo.pichUid}');"><u:out value="${apOngdExtnDocDVo.pichNm}" /></a></c:if><c:if
			test="${empty apOngdExtnDocDVo.pichUid}"
			><u:out value="${apOngdExtnDocDVo.pichNm}" /></c:if></td>
	<c:if
	
			test="${param.bxId=='recvRecLst'}">
	<td class="head_lt"><u:msg titleId="ap.list.recvDd" alt="접수일자" /></td>
	<td class="body_lt"><u:out value="${apOngdBVo.recvDt}" type="date" /></td></c:if><c:if
	
			test="${param.bxId!='recvRecLst'}">
	<td class="head_lt"><u:msg titleId="ap.list.cmplDd" alt="완결일자" /></td>
	<td class="body_lt"><u:out value="${apOngdBVo.cmplDt}" type="date" /></td></c:if>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.apvr" alt="결재자" /></td>
	<td class="body_lt"><c:if
			test="${not empty apOngdBVo.cmplrUid}"
			><a href="javascript:viewUserPop('${apOngdBVo.cmplrUid}');"><u:out value="${apOngdBVo.cmplrNm}" /></a></c:if><c:if
			test="${empty apOngdBVo.cmplrUid}"
			><u:out value="${apOngdBVo.cmplrNm}" /></c:if></td>
	<td class="head_lt"><u:msg titleId="cols.makYy" alt="생산년도" /></td>
	<td class="body_lt"><u:input id="makYy" value="${apOngdExtnDocDVo.makYy}" titleId="cols.makYy" valueOption="number" maxLength="4" type="view" /></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="ap.cols.sendr" alt="발신자" /></td>
	<td class="body_lt"><u:out value="${apOngdExtnDocDVo.sendrNm}" /></td>
	<td class="head_lt"><u:msg titleId="ap.doc.enfcDd" alt="시행일자" /></td>
	<td class="body_lt"><u:out value="${apOngdBVo.enfcDt}" type="date" /></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="ap.cols.makZone" alt="생산지" /></td>
	<td class="body_lt"><u:out value="${apOngdExtnDocDVo.makZoneNm}" /></td><c:if
	
		test="${not empty spReg}">
	<td class="head_lt"><u:msg titleId="ap.cols.pageCnt" alt="쪽수" /></td>
	<td><u:input id="pageCnt" value="${apOngdExtnDocDVo.pageCnt}" titleId="ap.cols.pageCnt" valueOption="number" maxLength="4" /></td></c:if><c:if
	
		test="${empty spReg}">
	<td class="head_lt"><u:msg titleId="ap.cols.pageCnt" alt="쪽수" /></td>
	<td class="body_lt"><u:out value="${apOngdExtnDocDVo.pageCnt}" /></td></c:if>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cm.ico.att" alt="첨부파일" /></td>
	<td colspan="3"><u:apFiles mode="${not empty spReg ? 'set' : 'view'}" id="docAttch" apvNo="${param.apvNo}"
		attHstNo="1" module="${apFileModule}" fileTarget="${apFileTarget}"
		onchange="dialog.resize('setPaperDocDialog');" /></td>
	</tr>
	
</u:listArea>

<u:buttonArea><c:if
		test="${not empty spReg}">
	<u:button titleId="cm.btn.save" href="javascript:savePaperDoc();" alt="저장" auth="A" /></c:if>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>
