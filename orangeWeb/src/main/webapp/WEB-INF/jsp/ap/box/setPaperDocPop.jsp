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
	saveFileToForm('docAttch', $form, null);
	//$form.submit();
}
//$(document).ready(function() {
//});
//-->
</script>

<div style="width:750px">
<form id="setPaperDocForm" method="post" enctype="multipart/form-data"><c:if
	test="${not empty param.apvNo}">
<u:input id="apvNo" type="hidden" value="${param.apvNo}" /></c:if>
<u:input id="bxId" type="hidden" value="${param.bxId}" />
<% // 폼 필드 %>
<u:listArea colgroup="18%,32%,18%,32%">
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.subj" alt="제목" /></td>
	<td colspan="3"><u:input id="docSubj" value="${apOngdBVo.docSubj}" titleId="cols.subj" style="width:97.4%;" maxByte="400" mandatory="Y" /></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="ap.doc.docNo" alt="문서번호" /></td>
	<td colspan="3"><u:input id="docNo" value="${apOngdBVo.docNo}" titleId="ap.doc.docNo" style="width:235px;" maxByte="50" mandatory="Y"
		readonly="${not empty apOngdExtnDocDVo.regSeq ? 'Y' : ''}" /></td>
	</tr>
	
	<c:if test="${optConfigMap.catEnab == 'Y'}">
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.clsInfo" alt="분류정보" /></td>
	<td colspan="3"><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><u:input id="clsInfoNm" value="${apOngdBVo.clsInfoNm}" titleId="cols.taskInfo" style="width:175px;" readonly="readonly" />
		<u:input type="hidden" id="clsInfoId" value="${apOngdBVo.clsInfoId}" /></td>
		<td style="padding-right: 2px;"><u:buttonS titleId="cols.clsInfo" alt="분류정보" href="javascript:openClsInfo();" /></td>
		</tr>
		</tbody></table></td>
	</tr>
	</c:if>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.extnDocTyp" alt="외부문서구분" /></td>
	<td><select id="extnDocTypCd" name="extnDocTypCd"<u:elemTitle titleId="cols.extnDocTyp" alt="외부문서구분" />><c:forEach
		items="${extnDocTypCdList}" var="extnDocTypCd">
		<u:option value="${extnDocTypCd.cd}" title="${extnDocTypCd.rescNm}" selected="${apOngdExtnDocDVo.extnDocTypCd == extnDocTypCd.cd
		or (param.bxId=='regRecLst' and empty apOngdExtnDocDVo.extnDocTypCd and extnDocTypCd.cd == 'paperApv')
		or (param.bxId=='recvRecLst' and empty apOngdExtnDocDVo.extnDocTypCd and extnDocTypCd.cd == 'extnRecv')
		}" /></c:forEach>
		</select></td>
	<td class="head_lt"><u:msg titleId="cols.extnDocContTyp" alt="외부문서컨텐츠구분" /></td>
	<td><select id="extnDocContTypCd" name="extnDocContTypCd"<u:elemTitle titleId="cols.extnDocContTyp" alt="외부문서컨텐츠구분" />><c:forEach
		items="${extnDocContTypCdList}" var="extnDocContTypCd">
		<u:option value="${extnDocContTypCd.cd}" title="${extnDocContTypCd.rescNm}" selected="${apOngdExtnDocDVo.extnDocContTypCd == extnDocContTypCd.cd}" /></c:forEach>
		</select></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.secul" alt="보안등급" /></td>
	<td colspan="3"><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><select id="seculCd" name="seculCd"<u:elemTitle titleId="cols.secul" alt="문서보안" />>
			<option value="none"><u:msg titleId="cm.option.noSelect" alt="선택안함" /></option><c:forEach
			items="${seculCdList}" var="seculCd">
			<u:option value="${seculCd.cd}" title="${seculCd.rescNm}" selected="${apOngdBVo.seculCd == seculCd.cd}" /></c:forEach>
			</select></td>
		<td class="width15"></td>
		<u:checkbox id="secuDocYn" name="secuDocYn" value="Y" titleId="cols.pw" alt="비밀번호" inputClass="bodybg_lt"
			checked="${not empty apOngdBVo.docPwEnc}"
			onclick="$('#setPaperDocForm #docPw').css('display', this.checked ? '' : 'none').focus();" />
		<td><u:input type="password" id="docPw" value="" titleId="cols.pw" style="width:150px;${empty apOngdBVo.docPwEnc ? ' display:none;' : ''}" maxLength="20" /></td>
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
	<td class="head_lt"><u:msg titleId="cols.prsvPrd" alt="보존기간" /></td>
	<td><c:if
	
			test="${param.bxId!='recvRecLst'}">
		<select id="docKeepPrdCd" name="docKeepPrdCd"<u:elemTitle titleId="cols.prsvPrd" alt="보존기간" />><c:forEach
		items="${docKeepPrdCdList}" var="docKeepPrdCd">
		<option value="${docKeepPrdCd.cd}" selected="${docKeepPrdCd.cd == apOngdBVo.docKeepPrdCd ? 'selected' : ''
			}">${docKeepPrdCd.rescNm}</option></c:forEach>
		</select></c:if><c:if
		
			test="${param.bxId=='recvRecLst'}">
		<select id="enfcDocKeepPrdCd" name="enfcDocKeepPrdCd"<u:elemTitle titleId="cols.enfcDoc" alt="기안문" />><c:forEach
		items="${docKeepPrdCdList}" var="docKeepPrdCd">
		<option value="${docKeepPrdCd.cd}" selected="${docKeepPrdCd.cd == apOngdBVo.enfcDocKeepPrdCd ? 'selected' : ''
			}">${docKeepPrdCd.rescNm}</option></c:forEach>
		</select></c:if>
	</td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="ap.cols.pich" alt="업무담당자" /></td>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody><tr>
		<td><u:input id="pichNm" value="${apOngdExtnDocDVo.pichNm}" titleId="ap.cols.pich" readonly="Y"
			/><u:input id="pichUid" value="${apOngdExtnDocDVo.pichUid}" type="hidden"
			/></td>
		<td><u:buttonS alt="선택" titleId="cm.btn.sel" href="javascript:openUserPop('pich')" /></td>
		</tr></tbody></table></td><c:if
	
			test="${param.bxId=='recvRecLst'}">
	<td class="head_lt"><u:msg titleId="ap.list.recvDd" alt="접수일자" /></td>
	<td><u:calendar id="recvDt" titleId="ap.list.recvDd" value="${apOngdBVo.recvDt}" /></td></c:if><c:if
	
			test="${param.bxId!='recvRecLst'}">
	<td class="head_lt"><u:msg titleId="ap.list.cmplDd" alt="완결일자" /></td>
	<td><u:calendar id="cmplDt" titleId="ap.list.cmplDd" value="${apOngdBVo.cmplDt}" /></td></c:if>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="ap.cfg.apvr" alt="결재자" /></td>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody><tr>
		<td><u:input id="cmplrNm" value="${apOngdBVo.cmplrNm}" titleId="ap.cfg.apvr" readonly="Y"
			/><u:input id="cmplrUid" value="${apOngdBVo.cmplrUid}" type="hidden"
			/></td>
		<td><u:buttonS alt="선택" titleId="cm.btn.sel" href="javascript:openUserPop('cmplr')" /></td>
		</tr></tbody></table></td>
	<td class="head_lt"><u:msg titleId="cols.makYy" alt="생산년도" /></td>
	<td><u:input id="makYy" value="${apOngdExtnDocDVo.makYy}" titleId="cols.makYy" valueOption="number" maxLength="4" /></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="ap.cols.sendr" alt="발신자" /></td>
	<td><u:input id="sendrNm" value="${apOngdExtnDocDVo.sendrNm}" titleId="ap.cols.sendr" style="width:93.4%;" maxByte="200" /></td>
	<td class="head_lt"><u:msg titleId="ap.doc.enfcDd" alt="시행일자" /></td>
	<td><u:calendar id="enfcDt" titleId="ap.doc.enfcDd" value="${apOngdBVo.enfcDt}" /></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="ap.cols.makZone" alt="생산지" /></td>
	<td><u:input id="makZoneNm" value="${apOngdExtnDocDVo.makZoneNm}" titleId="ap.cols.makZone" style="width:93.4%;" maxByte="200" /></td>
	<td class="head_lt"><u:msg titleId="ap.cols.pageCnt" alt="쪽수" /></td>
	<td><u:input id="pageCnt" value="${apOngdExtnDocDVo.pageCnt}" titleId="ap.cols.pageCnt" valueOption="number" maxLength="4" /></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cm.ico.att" alt="첨부파일" /></td>
	<td colspan="3"></td>
	</tr>
	
</u:listArea>

<u:listArea><tr><td><u:apFiles mode="set" id="docAttch" apvNo="${param.apvNo}"
		attHstNo="1" module="${apFileModule}" fileTarget="${apFileTarget}"
		onchange="dialog.resize('setPaperDocDialog');" exts="${exts}" extsTyp="${extsTyp}" isHidden="N"/></td></tr>
</u:listArea>
	
<u:buttonArea>
	<u:button titleId="cm.btn.save" href="javascript:savePaperDoc();" alt="저장" auth="A" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>
