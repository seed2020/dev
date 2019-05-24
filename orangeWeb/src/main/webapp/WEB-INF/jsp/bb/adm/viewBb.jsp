<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="params" excludes="brdId" />
<script type="text/javascript">
<!--
<% // [하단버튼:수정] 수정 %>
function modBb() {
	location.href = './setBb.do?${params}&brdId=${baBrdBVo.brdId}'
}
<% // [하단버튼:삭제] 삭제 %>
function delBb() {
	if (confirmMsg("cm.cfrm.del")) {	<% // cm.cfrm.del=삭제하시겠습니까? %>
		callAjax('./transBbDelAjx.do?menuId=${menuId}', {brdId:'${baBrdBVo.brdId}'}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.href = './listBb.do?${params}';
			}
		});
	}
}
<% // [하단버튼:컬럼관리] 컬럼 관리 %>
function setColmMngPop() {
	dialog.open('setColmMngPop','<u:msg titleId="bb.jsp.setColmMngPop.title" alt="컬럼관리" />','./setColmMngPop.do?menuId=${menuId}&brdId=${baBrdBVo.brdId}');
}
<% // [하단버튼:목록순서] 목록 순서 %>
function setListOrdrPop() {
	dialog.open('setListOrdrPop','<u:msg titleId="bb.jsp.setListOrdrPop.title" alt="목록순서" />','./setListOrdrPop.do?menuId=${menuId}&brdId=${baBrdBVo.brdId}');
}<% // [하단버튼:항목구성] - 팝업 %>
function setDtlOrdrPop() {
	dialog.open('setDtlOrdrDialog','<u:msg titleId="bb.btn.itemForm" alt="항목구서ㅇ" />','./setDtlOrdrPop.do?menuId=${menuId}&brdId=${baBrdBVo.brdId}');
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="bb.jsp.viewBb.title" alt="게시판 조회" menuNameFirst="true"/>

<form id="viewBbForm">
<u:input type="hidden" id="menuId" value="${menuId}" />

<% // 폼 필드 %>
<u:listArea>

	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.bbNm" alt="게시판명" /></td>
	<td class="body_lt" colspan="3">
		${baBrdBVo.rescNm}
		<%-- <c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
		<u:convert srcId="${baBrdBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
		<div id="lang_${langTypCdVo.cd}">(<u:out value="${langTypCdVo.rescNm}" />) ${rescVa}</div>
		</c:forEach> --%>
	</td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.desc" alt="설명" /></td>
	<td class="body_lt" colspan="3">${baBrdBVo.brdDesc}</td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.url" alt="URL" /></td>
	<td class="body_lt" colspan="3">/bb/listBull.do?brdId=${baBrdBVo.brdId}</td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.deptBbYn" alt="부서게시판여부" /></td>
	<td class="body_lt"><u:yn value="${baBrdBVo.deptBrdYn}" yesId="cm.option.use" noId="cm.option.notUse" alt="사용/사용안함" /></td>
	<td class="head_lt"><u:msg titleId="cols.allCompBbYn" alt="전사게시판여부" /></td>
	<td class="body_lt"><u:yn value="${baBrdBVo.allCompYn}" yesId="cm.option.use" noId="cm.option.notUse" alt="사용/사용안함" /></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.tblChoi" alt="테이블선택" /></td>
	<td class="body_lt">${baBrdBVo.tblDispNm}
		[<u:yn value="${baBrdBVo.exYn}" yesId="bb.cols.ex" noId="bb.cols.def" alt="확장/기본" />]</td>
	<td class="head_lt"><u:msg titleId="cols.bullCat" alt="게시물카테고리" /></td>
	<td class="body_lt"><u:yn value="${baBrdBVo.catYn}" yesId="cm.option.use" noId="cm.option.notUse" alt="사용/사용안함" />
		<c:if test="${baBrdBVo.catYn == 'Y'}">
		(${baBrdBVo.catGrpNm})
		</c:if></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.bbTyp" alt="게시판타입" /></td>
	<td width="32%" class="body_lt">${baBrdBVo.brdTypNm}</td>
	<td width="18%" class="head_lt"><u:msg titleId="cols.replyYn" alt="답변형여부" /></td>
	<td width="32%" class="body_lt"><u:yn value="${baBrdBVo.replyYn}" yesId="bb.option.replyY" noId="bb.option.replyN" alt="답변형/비답변형" /></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.discYn" alt="심의여부" /></td>
	<td class="body_lt"><u:yn value="${baBrdBVo.discYn}" yesId="bb.option.discY" noId="bb.option.discN" alt="심의/미심의" /></td>
	<td width="18%" class="head_lt"><u:msg titleId="cols.discr" alt="심의자" /></td>
	<td class="body_lt">${baBrdBVo.discrNm}</td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.knd" alt="종류" /></td>
	<td class="body_lt">${baBrdBVo.kndNm}</td>
	<td class="head_lt"><u:msg titleId="cols.bullRezvPrd" alt="게시예약기간" /></td>
	<td class="body_lt">${baBrdBVo.rezvPrd} <u:msg titleId="cols.mth" alt="개월" /></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.notcDispPrd" alt="공지표시기간" /></td>
	<td class="body_lt">${baBrdBVo.notcDispPrd}<u:msg titleId="bb.cols.day" alt="일" /></td>
	<td class="head_lt"><u:msg titleId="cols.readHst" alt="조회이력" /></td>
	<td class="body_lt"><c:if test="${empty baBrdBVo.readHstUseYn || baBrdBVo.readHstUseYn ne 'R'}"><u:yn value="${baBrdBVo.readHstUseYn}" yesId="cm.option.use" noId="cm.option.notUse" alt="사용/사용안함" /></c:if
	><c:if test="${!empty baBrdBVo.readHstUseYn && baBrdBVo.readHstUseYn eq 'R'}"><u:msg titleId="bb.option.use.readCnt" alt="사용(조회수만)"/></c:if>
		(※ <u:msg titleId="bb.jsp.setBb.tx03" alt="사용자 조회 이력을 기록합니다." />)</td>
	</tr>

	<tr>
		<td class="head_lt"><u:msg titleId="cols.newBullDisp" alt="신규게시물표시" /></td>
		<td class="body_lt"><u:yn value="${baBrdBVo.newDispYn}" yesId="cm.option.use" noId="cm.option.notUse" alt="사용/사용안함" />
		<c:if test="${baBrdBVo.newDispYn == 'Y'}">
		(${baBrdBVo.newDispPrd} <u:msg titleId="bb.jsp.setBb.tx01" alt="일 이내 표시" />)
		</c:if>
		</td>
		<td class="head_lt"><u:msg titleId="cols.prevNext" alt="이전/다음" /></td>
		<td class="body_lt"><u:yn value="${baBrdBVo.prevNextYn}" yesId="cm.option.use" noId="cm.option.notUse" alt="사용/사용안함" /></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.cmt" alt="한줄답변" /></td>
	<td class="body_lt"><u:yn value="${baBrdBVo.cmtYn}" yesId="cm.option.use" noId="cm.option.notUse" alt="사용/사용안함" /></td>
	<td class="head_lt"><u:msg titleId="cols.favot" alt="찬반투표" /></td>
	<td class="body_lt"><u:yn value="${baBrdBVo.favotYn}" yesId="cm.option.use" noId="cm.option.notUse" alt="사용/사용안함" /></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.recmd" alt="추천" /></td>
	<td class="body_lt"><u:yn value="${baBrdBVo.recmdUseYn}" yesId="cm.option.use" noId="cm.option.notUse" alt="사용/사용안함" /></td>
	<td class="head_lt"><u:msg titleId="cols.scre" alt="점수" /></td>
	<td class="body_lt"><u:yn value="${baBrdBVo.screUseYn}" yesId="cm.option.use" noId="cm.option.notUse" alt="사용/사용안함" /></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.pichDisp" alt="담당자표시" /></td>
	<td class="body_lt"><u:yn value="${baBrdBVo.pichDispYn}" yesId="cm.option.use" noId="cm.option.notUse" alt="사용/사용안함" /></td>
	<td class="head_lt"><u:msg titleId="cols.photoBbYn" alt="포토게시판여부" /></td>
	<td class="body_lt"><u:yn value="${baBrdBVo.photoYn}" yesId="cm.option.use" noId="cm.option.notUse" alt="사용/사용안함" /></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.pich" alt="담당자" /></td>
	<td class="body_lt">${baBrdBVo.pichVo.rescNm}</td>
	<td class="head_lt"><u:msg titleId="cols.pichDept" alt="담당자 부서" /></td>
	<td class="body_lt">${baBrdBVo.pichVo.deptRescNm}</td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.pichPhon" alt="담당자 전화번호" /></td>
	<td class="body_lt">${baBrdBVo.pichPinfoVo.mbno}</td>
	<td class="head_lt"><u:msg titleId="cols.pichEmail" alt="담당자 이메일" /></td>
	<td class="body_lt">
		<a href="javascript:parent.mailToPop('${baBrdBVo.pichPinfoVo.email}')" title="<u:msg titleId='or.jsp.viewUserPop.mailToPop' />">${baBrdBVo.pichPinfoVo.email}</a>
	</td>
	</tr><tr>
	<td class="head_lt"><u:msg titleId="bb.cols.addOpt" alt="추가옵션" /></td>
	<td colspan="3"><u:checkArea id="optVaArea"><u:checkbox value="Y" name="brdCopyYn" titleId="bb.cols.brdCopy" alt="게시물 등록시 게시판 선택" checkValue="${baBrdBVo.optMap.brdCopyYn }" disabled="Y"
	/><u:checkbox value="Y" name="bbTgtDispYn" titleId="bb.cols.bbTgt" alt="게시대상" checkValue="${baBrdBVo.optMap.bbTgtDispYn }" disabled="Y"  
	/><u:checkbox value="Y" name="bbOptYn" titleId="bb.cols.bbOpt" alt="게시옵션" checkValue="${baBrdBVo.optMap.bbOptYn }" disabled="Y" 
	/><u:checkbox value="Y" name="snsUploadYn" titleId="bb.sns.upload" alt="SNS 올리기" checkValue="${baBrdBVo.optMap.snsUploadYn }" disabled="Y"
	/><u:checkbox value="Y" name="tmpSaveYn" titleId="cm.btn.tmpSave" alt="임시저장" checkValue="${baBrdBVo.optMap.tmpSaveYn }" disabled="Y"
	/><u:checkbox value="Y" name="fileUploadYn" titleId="cols.attFile" alt="첨부파일" checkValue="${baBrdBVo.optMap.fileUploadYn }" disabled="Y"
	/><u:checkbox value="Y" name="listCondApplyYn" titleId="bb.cols.listCondApply" alt="목록 조회조건 적용" checkValue="${baBrdBVo.optMap.listCondApplyYn }" disabled="Y"
	/><u:checkbox value="Y" name="privUseYn" titleId="bb.cols.priv.use" alt="비공개 사용" checkValue="${baBrdBVo.optMap.privUseYn }" disabled="Y"
	/></u:checkArea></td>	
	</tr><c:if test="${sysPloc.wfEnable eq 'Y'}">
	<tr>
	<td class="head_lt"><u:msg titleId="ap.cmpt.wfFormBody" alt="본문 (업무양식)" /></td>
	<td class="body_lt" colspan="3">${wfFormBVo.formNm }</td>
	</tr>
	</c:if>

</u:listArea>

<u:set var="auth" test="${baBrdBVo.allCompYn eq 'Y' }" value="SYS" elseValue="A"/>
<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="bb.btn.colmMng" alt="컬럼관리" onclick="setColmMngPop();" auth="${auth}" />
	<u:button titleId="bb.btn.listOrdr" alt="목록순서" onclick="setListOrdrPop();" auth="${auth}" />
	<u:button titleId="bb.btn.itemForm" alt="항목구성" onclick="setDtlOrdrPop();" auth="${auth}" />
	<u:button titleId="cm.btn.mod" alt="수정" onclick="modBb();" auth="${auth}" />
	<u:button titleId="cm.btn.del" alt="삭제" onclick="delBb();" auth="${auth}" />
	<u:button titleId="cm.btn.list" alt="목록" href="./listBb.do?${params}" />
</u:buttonArea>

</form>

