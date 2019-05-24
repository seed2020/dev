<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

/*
	- 사용처 : 양식편집, 결재본문의 결재라인 리스트, 결재선 팝업 (유통문서의 경우 본문의 결재라인(유통전 결재라인)과 유통 결재라인 이 다름)

	apvLnListInDoc : 양식편집, 결재본문 의 경우 Y
	apvLnListForEdit : 양식편집의 경우 Y
	
	not empty apvLnListInDoc and empty param.apvNo : 결재 본문의 기안시
	
*/

%><c:if test="${empty dispApOngdApvLnDVoList}"><u:convert
	var="dispApOngdApvLnDVoList"
	srcId="${not empty apvLnListInDoc and empty apvLnListForEdit
		and not (empty param.apvNo and empty param.orgnApvNo) ? 'stampApOngdApvLnDVoList' : 'currApOngdApvLnDVoList'}"
	cmt="양식편집, 결재라인 팝업 : currApOngdApvLnDVoList,   본문삽입:stampApOngdApvLnDVoList"

/></c:if><c:if
test="${not empty apvLnListInDoc}"><%/*

[[ 결재 본분에 삽입되는 결재라인 제어 ]]

1. [통보 제외한 결재자 수] 구하기
  - 통보 : psnInfm:개인통보, deptInfm:부서통보, entu:결재안함(위임), postApvd:사후보고(후열)

2.1 [결재자 리스트]의 경우
  - [통보 제외한 결재자 수]

2.2 [최종결재 리스트]의 경우
  - 서명란 목록에 표시 - 면 : [통보 제외한 결재자 수]
  - 서명란 목록에 표시 - 아니면 : [통보 제외한 결재자 수] -1

2.3 [도장방 리스트]의 경우
  - 서명란 목록에 표시 - 면 : [통보 제외한 결재자 수]
  - 서명란 목록에 표시 - 아니면 : [통보 제외한 결재자 수] - [도장방 수]

3 양식편집, 기안할때 
  - 1줄 표시함
*/
%><c:forEach items="${dispApOngdApvLnDVoList}"
	var="apOngdApvLnDVo" varStatus="status"
		><u:set
			cmt="1. [통보 제외한 결재자 수] 구하기"
			
			test="${
			apOngdApvLnDVo.apvrRoleCd=='psnInfm' or apOngdApvLnDVo.apvrRoleCd=='deptInfm'
			or apOngdApvLnDVo.apvrRoleCd=='entu' or apOngdApvLnDVo.apvrRoleCd=='postApvd'
			}"
			var="exceptInfmCnt"
			value="${empty exceptInfmCnt ? 0 : exceptInfmCnt}"
			elseValue="${empty exceptInfmCnt ? 1 : exceptInfmCnt + 1}"
	/></c:forEach
	
	><u:set
		cmt="
			2.1 [결재자 리스트]의 경우
			2.2 [최종결재 리스트]의 경우
			2.3 [도장방 리스트]의 경우"
		
		test="${apFormBVo.formApvLnTypCd != 'apvLnList' and apv.lstDupDispYn != 'Y'}"
		var="validApvLnListSize"
		value="${apFormBVo.formApvLnTypCd=='apvLnOneTopList' 
			? exceptInfmCnt - 1
			: exceptInfmCnt - apvLnMaxCnt_apv}"
		elseValue="${exceptInfmCnt}"
		
/></c:if
><u:set
	cmt="
		3 양식편집, 기안할때
			apvLnListForEdit 있음 - 양식편집 할때 임
			param.apvNo 있음 - 기안할때 아님
			param.orgnApvNo 있음 - 시행변환할때  == 기안할때 아님
			
			- 1줄 표시함"
	
	test="${not empty apvLnListForEdit or (not empty apvLnListInDoc and empty param.apvNo and empty param.orgnApvNo)}"
	var="validApvLnListSize"
	value="1"
	elseValue="${validApvLnListSize}" 

/>
<c:if test="${not empty apvLnListInDoc and apFormBVo.formApvLnTypCd != 'apvLnList' and apFormBVo.formApvLnTypCd != 'apvLnDblList'}"><div class="blank" style="${validApvLnListSize < 1 ? 'display:none' : ''}"></div></c:if>
<u:listArea id="apvLnListArea" noBottomBlank="${not empty apvLnListInDoc}" style="${not empty apvLnListInDoc and validApvLnListSize < 1 ? 'display:none' : ''}">
	<tr>
	<td width="20%" class="head_ct"><u:msg titleId="cols.dept" alt="부서" /></td>
	<td width="11%" class="head_ct"><u:term termId="or.term.posit" alt="직위" /></td>
	<td width="11%" class="head_ct"><u:msg titleId="ap.cfg.apvr" alt="결재자" /></td>
	<td width="20%" class="head_ct"><u:msg titleId="ap.list.apvRole" alt="결재자역할" /></td>
	<td width="20%" class="head_ct"><u:msg titleId="ap.list.apvDt" alt="결재일시" /></td><c:if
		test="${empty apvLnListInDoc}">
	<td width="18%" class="head_ct"><u:msg titleId="ap.list.modHis" alt="변경사항" /></td></c:if><c:if
		test="${not empty apvLnListInDoc}">
	<td width="18%" class="head_ct"><u:msg titleId="ap.doc.opin" alt="의견" /></td></c:if>
	</tr>
	<c:forEach
		items="${dispApOngdApvLnDVoList}"
		var="apOngdApvLnDVo" varStatus="status">
	<tr data-apvStatCd="${apOngdApvLnDVo.apvStatCd}" style="${
		empty apvLnListInDoc or validApvLnListSize > status.index ? '' : 'display:none'}" <c:if test="${empty apvLnListInDoc}">onmouseover='this.className="trover"' onmouseout='this.className="trout"'</c:if>>
	<td class="body_ct"><c:if
		test="${
					(apvData.recLstTypCd!='recvRecLst' and apvData.recLstTypCd!='distRecLst')
				and (param.bxId !='recvBx' and param.bxId !='distBx')
				and apOngdApvLnDVo.pichApntYn == 'Y'}"><u:term
			termId="ap.term.${apOngdApvLnDVo.apvrRoleCd=='byOneAgr' ? 'byOne' : apOngdApvLnDVo.apvrRoleCd}"
			langTypCd="${apvData.docLangTypCd}" var="apvrRoleNm" /><c:if
				test="${empty apvLnListInDoc and apOngdApvLnDVo.subOpinYn eq 'Y'}"
				><a href="javascript:viewApvLnPop('${apOngdApvLnDVo.apvNo}','${apOngdApvLnDVo.apvLnNo}','<u:out
					value="${apOngdApvLnDVo.apvDeptNm} (${apvrRoleNm})" type="value" />')" style="font-weight:bold" title="<u:msg titleId="ap.jsp.doc.apvOpin" alt="결재의견" />"><u:out
						value="${apOngdApvLnDVo.apvDeptNm}" /></a></c:if><c:if
				test="${not(empty apvLnListInDoc and apOngdApvLnDVo.subOpinYn eq 'Y')}"
				><a href="javascript:viewApvLnPop('${apOngdApvLnDVo.apvNo}','${apOngdApvLnDVo.apvLnNo}','<u:out
					value="${apOngdApvLnDVo.apvDeptNm} (${apvrRoleNm})" type="value" />')"><u:out
						value="${apOngdApvLnDVo.apvDeptNm}" /></a></c:if></c:if><c:if
		test="${not (
					(apvData.recLstTypCd!='recvRecLst' and apvData.recLstTypCd!='distRecLst')
				and (param.bxId !='recvBx' and param.bxId !='distBx')
				and apOngdApvLnDVo.pichApntYn == 'Y')}"><u:out value="${apOngdApvLnDVo.apvDeptNm}" /></c:if></td>
	<td class="body_ct"><u:out value="${apOngdApvLnDVo.apvrPositNm}" /></td>
	<td class="body_ct"><c:if
		test="${empty apOngdApvLnDVo.apvrNm}">&nbsp;</c:if><c:if
		test="${not empty apOngdApvLnDVo.apvrNm}"
		><c:if
			test="${not empty apOngdApvLnDVo.apvrUid}"
			><a href="javascript:${frmYn=='Y'? 'parent.' : ''}viewUserPop('${apOngdApvLnDVo.apvrUid}');"><u:out value="${apOngdApvLnDVo.apvrNm}" /></a></c:if><c:if
			test="${empty apOngdApvLnDVo.apvrUid}"
			><u:out value="${apOngdApvLnDVo.apvrNm}" /></c:if></c:if></td>
	<td class="body_ct"><c:if
		test="${empty apOngdApvLnDVo.apvrRoleCd or (apOngdApvLnDVo.apvrRoleCd=='mak' and empty apOngdApvLnDVo.apvrNm)}">&nbsp;</c:if><c:if
		test="${not empty apOngdApvLnDVo.apvrRoleCd and not (apOngdApvLnDVo.apvrRoleCd=='mak' and empty apOngdApvLnDVo.apvrNm)}"
			><u:term termId="ap.term.${apOngdApvLnDVo.apvrRoleCd=='byOneAgr' ? 'byOne' : apOngdApvLnDVo.apvrRoleCd}"
		langTypCd="${apvData.docLangTypCd}" /><u:cmt
		
		cmt="rejt:반려, reRevw:재검토, cons:반대" /><c:if
		
		test="${apOngdApvLnDVo.apvStatCd == 'rejt' or apOngdApvLnDVo.apvStatCd == 'reRevw'
			or apOngdApvLnDVo.apvStatCd == 'cons'}"> (<u:term termId="ap.term.${apOngdApvLnDVo.apvStatCd}" /><c:if
		
		test="${not empty apOngdApvLnDVo.agntUid}">, <a href="javascript:viewUserPop('${apOngdApvLnDVo.agntUid}');"><u:term termId="ap.term.agnt" /></a></c:if>)</c:if><c:if
		
		test="${not empty apOngdApvLnDVo.agntUid and not (
			apOngdApvLnDVo.apvStatCd == 'rejt' or apOngdApvLnDVo.apvStatCd == 'reRevw'
			or apOngdApvLnDVo.apvStatCd == 'cons')}"> (<a href="javascript:viewUserPop('${apOngdApvLnDVo.agntUid}');"><u:term termId="ap.term.agnt" /></a>)</c:if></c:if><c:if
		test="${apOngdApvLnDVo.apvrRoleCd=='abs'}"> [${apOngdApvLnDVo.absRsonNm}]</c:if></td>
	<td class="body_ct"><u:out value="${apOngdApvLnDVo.apvDt}" /></td>
	<td style="text-align:center;"><c:if
		test="${empty apvLnListInDoc}"><c:if
			test="${not empty apOngdApvLnDVo.prevBodyHstNo}"
		><u:buttonS titleId="cols.body" alt="본문" href="javascript:viewBodyHis('${apOngdApvLnDVo.apvNo}','${apOngdApvLnDVo.prevBodyHstNo}');" popYn="Y" /></c:if><c:if
			test="${not empty apOngdApvLnDVo.prevApvLnHstNo}"
		><u:buttonS titleId="ap.btn.ln" alt="경로" href="javascript:viewApvLnHis('${apOngdApvLnDVo.apvNo}','${apOngdApvLnDVo.apvLnPno}','${apOngdApvLnDVo.prevApvLnHstNo}');" popYn="Y" /></c:if><c:if
			test="${not empty apOngdApvLnDVo.prevAttHstNo}"
		><u:buttonS titleId="ap.btn.attShort" alt="첨부" href="javascript:viewAttchHis('${apOngdApvLnDVo.apvNo}','${apOngdApvLnDVo.prevAttHstNo}');" popYn="Y" /></c:if></c:if><c:if
		test="${not empty apvLnListInDoc}"><c:if
			test="${not empty apOngdApvLnDVo.apvOpinCont or apOngdApvLnDVo.subOpinYn eq 'Y'}"><c:if
			
				test="${(param.bxId eq 'waitBx' or param.bxId eq 'ongoBx' or param.bxId eq 'apvdBx'
					or param.bxId eq 'myBx' or param.bxId eq 'rejtBx' or param.bxId eq 'postApvdBx'
					or param.bxId eq 'drftBx' or param.bxId eq 'deptBx' or param.bxId eq 'regRecLst'
					or param.bxId eq 'admOngoBx' or param.bxId eq 'admRegRecLst' or param.bxId eq 'admApvdBx' or param.bxId eq 'admRejtBx')
					and (apvData.recLstTypCd!='recvRecLst' and apvData.recLstTypCd!='distRecLst') }"
				><c:if
					test="${apOngdApvLnDVo.subOpinYn eq 'Y'}"
					><a href="javascript:viewApvLnPop('${apOngdApvLnDVo.apvNo}','${apOngdApvLnDVo.apvLnNo}','<u:out value="${apOngdApvLnDVo.apvDeptNm} (${apvrRoleNm})" type="value" />')"><img alt="ballen" src="/images/cm/ballon.gif" /></a></c:if><c:if
					test="${apOngdApvLnDVo.subOpinYn ne 'Y'}"
					><a href="javascript:openDetl('apvInof')"><img alt="ballen" src="/images/cm/ballon.gif" /></a></c:if></c:if></c:if></c:if>
	</td>
	</tr>
	</c:forEach>
</u:listArea>

<c:if test="${empty apvLnListInDoc}">
<u:listArea noBottomBlank="true">
	<tr>
	<td width="20%" class="head_ct"><u:msg titleId="ap.cfg.apvr" alt="결재자" /></td>
	<td width="80%" class="head_ct"><u:msg titleId="ap.jsp.doc.apvOpin" alt="결재의견" /></td>
	</tr>
	<c:forEach
		items="${dispApOngdApvLnDVoList}"
			var="apOngdApvLnDVo" varStatus="status"><c:if
			test="${not empty apOngdApvLnDVo.apvOpinCont and apOngdApvLnDVo.apvStatCd != 'cncl'}"
			><u:set test="${true}" var="hasOpin" value="Y" /><c:if
			
			test="${apOngdApvLnDVo.apvrDeptYn == 'Y'}">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_ct"><u:out value="${apOngdApvLnDVo.apvDeptNm}" /></td>
	<td class="body_lt"><u:out value="${apOngdApvLnDVo.apvOpinCont}" /></td>
	</tr></c:if><c:if
			
			test="${apOngdApvLnDVo.apvrDeptYn != 'Y'}">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_ct"><c:if
			test="${not empty apOngdApvLnDVo.apvrUid}"
			><a href="javascript:viewUserPop('${apOngdApvLnDVo.apvrUid}');"><u:out value="${apOngdApvLnDVo.apvrNm}" /></a></c:if><c:if
			test="${empty apOngdApvLnDVo.apvrUid}"
			><u:out value="${apOngdApvLnDVo.apvrNm}" /></c:if></td>
	<td class="body_lt"><u:out value="${apOngdApvLnDVo.apvOpinCont}" /></td>
	</tr></c:if></c:if>
	</c:forEach>
	<c:if test="${empty hasOpin}">
	<tr>
	<td class="nodata" colspan="2"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
	</c:if>
	
</u:listArea>
</c:if>