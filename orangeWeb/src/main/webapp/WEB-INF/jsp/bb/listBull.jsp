<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="faqYn" test="${baBrdBVo.brdTypCd == 'F' }" value="Y" elseValue="N"/>
<u:set var="compIdParam" test="${!empty param.compId }" value="&compId=${param.compId }" elseValue=""/>
<u:set var="listPage" test="${!empty ptCompBVoList }" value="listCoprBull" elseValue="listBull"/>
<u:params var="params" />
<script type="text/javascript">
<!--<c:if test="${faqYn eq 'Y'}">
<% // [하단버튼:수정] 수정 %>
function modBull(id) {
	location.href = './setBullFaq.do?${params}&bullId='+id;
}
<% // [하단버튼:삭제] 삭제 %>
function delBull(id) {
	callAjax('./transBullDelAjxChk.do?menuId=${menuId}&bullId='+id+'&brdId=${baBrdBVo.brdId}', {brdId:'${baBrdBVo.brdId}', bullId:id}, function(data) {
		if (data.message != null) {
			alert(data.message);
			return;
		}
		if (data.result == 'ok') {
			if (confirmMsg("cm.cfrm.del")) {	<% // 삭제하시겠습니까? %>
				callAjax('./transBullDelAjx.do?menuId=${menuId}&bullId='+id+'&brdId=${baBrdBVo.brdId}', {brdId:'${baBrdBVo.brdId}', bullId:id}, function(data) {
					if (data.message != null) {
						alert(data.message);
					}
					if (data.result == 'ok') {
						location.href = './${listPage}.do?${params}';
					}
				});
			}
		}
	});
}
</c:if>


<% // 엑셀 파일 다운로드 %>
function excelDownFile() {
	var $form = $('#excelForm');
	$form.attr('method','post');
	$form.attr('action','./excelDownLoad.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form[0].submit();
};

<% // [목록:제목] 게시물 조회 %>
function viewBull(id) {
	location.href = './${viewPage}.do?${params}&bullId=' + id;
}
<% // [목록:제목] 보안글 조회를 위한 로그인폼 화면 %>
function openLogin(id, callback) {
	var url='./setLoginPop.do?${params}&viewPage=${viewPage}&bullId=' + id;
	if(callback!=undefined) url+='&callback='+callback;
	dialog.open('setLoginPop','<u:msg titleId="bb.jsp.setLoginPop.title" alt="보안글 인증" />', url);
}
<% // [목록:조회수] 조회이력 %>
function readHst(id) {
	dialog.open('listReadHstPop','<u:msg titleId="bb.jsp.listReadHstPop.title" alt="조회자 정보" />','./listReadHstPop.do?menuId=${menuId}&brdId=${param.brdId}&bullId=' + id);
}
<% // [팝업] 파일목록 조회 %>
function viewFileListPop(id) {
	dialog.open('viewFileListDialog','<u:msg titleId="cols.att" alt="첨부" />','./viewFileListPop.do?menuId=${menuId}&brdId=${param.brdId}&bullId='+id);
}
<% // [AJAX] 보안글 인증 %>
function chkValidBull(id, secu){
	callAjax('./chkValidBullAjx.do?menuId=${menuId}', {secu:secu}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			dialog.close('setLoginPop');
			viewFileListPop(id);
		}
	});
}<% // [AJAX] FAQ 상세보기 %>
function openFaqView(id){
	$('#listArea tr[id^="faq_"]').not('#faq_'+id).hide();
	var target=$('#faq_'+id);
	var isEmpty=target.attr('data-load')=='N';
	if(isEmpty){
		//target.find('td:first #dataFrm').attr('src', './${viewPage}Frm.do?${params}&bullId=' + id);
		callAjax('./getBullHtmlAjx.do?menuId=${menuId}&brdId=${baBrdBVo.brdId}', {brdId:'${baBrdBVo.brdId}', bullId:id}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				if(data.bodyHtml!=null)
					target.find('td:first div#cont').html(data.bodyHtml);
				else
					target.find('td:first div#cont').html('<div class="nodata" style="width:100%;text-align:center;line-height:70px;"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></div>');
				
				target.attr('data-load', 'Y');
				target.find('td:first #fileFrm_'+id).attr('src', './viewFaq.do?menuId=${menuId}&brdId=${param.brdId}&bullId='+id);
				//resizeIframe('fileFrm_'+id);
				$('#fileFrm_'+id).load(function() {
			        $(this).css("height", ($(this).contents().find("body").height()+5) + "px");
			    });
			}
			
		});
		target.show();
	}else	
		target.toggle();
}
$(document).ready(function() {
	setUniformCSS();
	$('#listArea tbody:first tr a').click(function(event){
		if(event.stopPropagation) event.stopPropagation(); //MOZILLA
		else event.cancelBubble = true; //IE
	});
});
//-->
</script>
<!-- 타이틀 -->
<jsp:include page="/WEB-INF/jsp/bb/listTopTitle.jsp" />

<form id="searchForm" name="searchForm" action="./${listPage }.do" >
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="compId" value="${param.compId}" />
<u:input type="hidden" id="brdId" value="${param.brdId}" />

<c:if test="${empty listCondApplyYn || listCondApplyYn == false }">
<% // 검색영역 %>
<u:searchArea>
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">
		<u:option value="SUBJ" titleId="cols.subj" alt="제목" checkValue="${param.schCat}" />
		<u:option value="CONT" titleId="cols.cont" alt="내용" checkValue="${param.schCat}" />
		<c:if test="${baBrdBVo.brdTypCd == 'N'}">
		<u:option value="REGR_NM" titleId="cols.regr" alt="등록자" checkValue="${param.schCat}" />
		</c:if>
		<c:if test="${baBrdBVo.brdTypCd == 'A'}">
		<u:option value="ANON_REGR_NM" titleId="cols.regr" alt="등록자" checkValue="${param.schCat}" />
		</c:if>
		</select></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 200px;" onkeydown="if (event.keyCode == 13) searchForm.submit();" /></td>
		<td class="width20"></td>
		<!-- 등록일시 -->
		<td class="search_tit"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
		<td><table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td><u:calendar id="strtYmd" option="{end:'endYmd'}" value="${param.strtYmd}" /></td>
			<td class="search_body_ct"> ~ </td>
			<td><u:calendar id="endYmd" option="{start:'strtYmd'}" value="${param.endYmd}" /></td>
			</tr>
			</table></td>
		<td class="width20"></td>
		<!-- 카테고리 -->
		<c:if test="${baBrdBVo.catYn == 'Y'}">
		<td class="width20"></td>
		<td class="search_tit"><u:msg titleId="cols.cat" alt="카테고리" /></td>
		<td><select name="catId" onchange="searchForm.submit();">
			<u:option value="" titleId="cm.option.all" alt="전체" checkValue="${param.catId}" />
			<c:forEach items="${baCatDVoList}" var="catVo" varStatus="status">
			<u:option value="${catVo.catId}" title="${catVo.rescNm}" checkValue="${param.catId}" />
			</c:forEach>
			</select></td>
		</c:if>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
</u:searchArea>

</c:if>

<% // 목록타입 %>
<c:if test="${baBrdBVo.photoYn == 'Y'}">
<div class="titlearea">
	<div class="tit_right">
	<ul>
	<li><select name="listTyp" onchange="searchForm.submit();">
		<u:option value="I" titleId="bb.cols.listTyp.Image" alt="앨범형" checkValue="${param.listTyp}" />
		<u:option value="B" titleId="bb.cols.listTyp.Board" alt="게시판형" checkValue="${param.listTyp}" />
		</select></li>
	</ul>
	</div>
</div>
</c:if>

</form>
<c:if test="${!empty listCondApplyYn && listCondApplyYn == true }">
<% // 검색영역 %>
<jsp:include page="/WEB-INF/jsp/bb/listBbSrch.jsp" />
</c:if>
<% // 목록 %>
<c:set var="maxColCnt" value="1"/>
<c:if test="${baBrdBVo.optMap.fileUploadYn eq 'Y'}"><c:set var="maxColCnt" value="2"/></c:if>
<div class="listarea" id="listArea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="width:100%;table-layout:fixed;">
	<tr>
	<td width="60" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
<c:if test="${fn:length(baColmDispDVoList) > 0}">
	<c:forEach items="${baColmDispDVoList}" var="baColmDispDVo" varStatus="status">
	<c:if test="${baColmDispDVo.listDispYn == 'Y'}">
		<c:set var="maxColCnt" value="${maxColCnt+1 }"/>
		<c:set var="colmNm" value="${baColmDispDVo.colmVo.colmNm }"/>
		<u:set var="wdth" test="${!empty baColmDispDVo.wdthPerc }" value="${baColmDispDVo.wdthPerc }" elseValue=""/>
		<c:if test="${colmNm == 'SUBJ'}"><u:set var="wdth" test="${!empty wdth }" value="${wdth }" elseValue="*"/></c:if>
		<c:if test="${colmNm == 'CAT_ID'}"><u:set var="wdth" test="${!empty wdth }" value="${wdth }" elseValue="100" /></c:if>
		<c:if test="${colmNm == 'REZV_DT'}"><u:set var="wdth" test="${!empty wdth }" value="${wdth }" elseValue="130" /></c:if>
		<c:if test="${colmNm == 'BULL_REZV_DT'}"><u:set var="wdth" test="${!empty wdth }" value="${wdth }" elseValue="130" /></c:if>
		<c:if test="${colmNm == 'EXPR_DT'}"><u:set var="wdth" test="${!empty wdth }" value="${wdth }" elseValue="130" /></c:if>
		<c:if test="${colmNm == 'BULL_EXPR_DT'}"><u:set var="wdth" test="${!empty wdth }" value="${wdth }" elseValue="130" /></c:if>
		<c:if test="${colmNm == 'READ_CNT'}"><u:set var="wdth" test="${!empty wdth }" value="${wdth }" elseValue="50" /></c:if>
		<c:if test="${colmNm == 'PROS_CNT'}"><u:set var="wdth" test="${!empty wdth }" value="${wdth }" elseValue="50" /></c:if>
		<c:if test="${colmNm == 'CONS_CNT'}"><u:set var="wdth" test="${!empty wdth }" value="${wdth }" elseValue="50" /></c:if>
		<c:if test="${colmNm == 'RECMD_CNT'}"><u:set var="wdth" test="${!empty wdth }" value="${wdth }" elseValue="50" /></c:if>
		<c:if test="${colmNm == 'REGR_UID'}"><u:set var="wdth" test="${!empty wdth }" value="${wdth }" elseValue="100" /></c:if>
		<c:if test="${colmNm == 'REG_DT'}"><u:set var="wdth" test="${!empty wdth }" value="${wdth }" elseValue="130" /></c:if>
		<c:if test="${colmNm == 'MODR_UID'}"><u:set var="wdth" test="${!empty wdth }" value="${wdth }" elseValue="100" /></c:if>
		<c:if test="${colmNm == 'MOD_DT'}"><u:set var="wdth" test="${!empty wdth }" value="${wdth }" elseValue="130" /></c:if>
	<td width="${empty wdth ? '120' : wdth}" class="head_ct">${baColmDispDVo.colmVo.rescNm}</td>
	</c:if>
	</c:forEach>
</c:if>
	<c:if test="${baBrdBVo.optMap.fileUploadYn eq 'Y'}"><td width="40" class="head_ct"><u:msg titleId="cols.att" alt="첨부" /></td></c:if>
	</tr>

<!-- 공지 목록 -->
<c:if test="${fn:length(notcBullList) > 0}">
	<c:forEach items="${notcBullList}" var="bbBullLVo" varStatus="status">
	<u:set test="${bbBullLVo.secuYn == 'Y'}" var="viewBull" value="openLogin" elseValue="viewBull" />
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"' onclick="${viewBull}('${bbBullLVo.bullId}');" style="cursor:pointer;">
	<td class="body_ct"><u:icon type="notc" /></td>
	<c:if test="${fn:length(baColmDispDVoList) > 0}">
		<c:forEach items="${baColmDispDVoList}" var="baColmDispDVo" varStatus="status">
			<c:set var="colmVo" value="${baColmDispDVo.colmVo}" />
			<c:set var="colmTyp" value="${colmVo.colmTyp}" />
			<c:set var="colmTypVal" value="${colmVo.colmTypVal}" />
			<u:set var="alnVa" test="${empty baColmDispDVo.alnVa && colmVo.colmNm == 'SUBJ' }" value="left" elseValue="${baColmDispDVo.alnVa }"/>
		<c:if test="${baColmDispDVo.listDispYn == 'Y'}"><u:set var="bodyClass" test="${!empty alnVa && alnVa ne 'center'}" value="${alnVa eq 'right' ? 'body_rt' : 'body_lt'}" elseValue="body_ct"/>
			<c:if test="${colmVo.colmNm == 'CAT_ID'}">
			<td class="${bodyClass}">${bbBullLVo.catNm}</td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'SUBJ'}">
			<td class="${bodyClass}">
				<div class="ellipsis" title="${bbBullLVo.subj}" style="${style}">
				<u:icon type="indent" display="${bbBullLVo.replyDpth > 0}" repeat="${bbBullLVo.replyDpth - 1}" />
				<u:icon type="reply" display="${bbBullLVo.replyDpth > 0}" />
				<u:icon type="new" display="${bbBullLVo.newYn == 'Y'}" />
				<u:set test="${bbBullLVo.readYn == 'Y' && faqYn ne 'Y'}" var="style" value="font-weight: normal;" elseValue="font-weight: bold;" />				
				<u:set test="${bbBullLVo.ugntYn == 'Y' && faqYn ne 'Y'}" var="style" value="${style} color:red;" elseValue="${style}" />
				<c:if test="${bbBullLVo.ugntYn == 'Y'}"><span style="${style}">[<u:msg titleId="bb.option.ugnt" alt="긴급" />]</span></c:if>
				<c:if test="${bbBullLVo.secuYn == 'Y'}"><span style="${style}">[<u:msg titleId="bb.option.secu" alt="보안" />]</span></c:if>
				<u:out value="${bbBullLVo.subj}" type="html"/>
				<c:if test="${baBrdBVo.cmtYn == 'Y'}"><span style="font-size: 10px;">(<u:out value="${bbBullLVo.cmtCnt}" type="number" />)</span></c:if>
				</div>
				</td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'REGR_UID'}">
			<td class="${bodyClass}"><a href="javascript:viewUserPop('${bbBullLVo.regrUid}');"><u:out value="${bbBullLVo.regrNm}" /></a></td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'MODR_UID'}">
			<td class="${bodyClass}"><a href="javascript:viewUserPop('${bbBullLVo.modrUid}');"><u:out value="${bbBullLVo.modrNm}" /></a></td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'REG_DT'}">
			<td class="${bodyClass}"><u:out value="${bbBullLVo.regDt}" type="longdate" /></td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'MOD_DT'}">
			<td class="${bodyClass}"><u:out value="${bbBullLVo.modDt}" type="longdate" /></td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'REZV_DT'}">
			<td class="${bodyClass}"><u:out value="${bbBullLVo.bullRezvDt}" type="longdate" /></td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'BULL_REZV_DT'}">
			<td class="${bodyClass}"><u:out value="${bbBullLVo.bullRezvDt}" type="longdate" /></td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'EXPR_DT'}">
			<td class="${bodyClass}"><u:out value="${bbBullLVo.bullExprDt}" type="longdate" /></td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'BULL_EXPR_DT'}">
			<td class="${bodyClass}"><u:out value="${bbBullLVo.bullExprDt}" type="longdate" /></td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'READ_CNT'}">
			<td class="${bodyClass}">
				<c:if test="${baBrdBVo.readHstUseYn == 'Y'}">
				<a href="javascript:readHst('${bbBullLVo.bullId}');"><u:out value="${bbBullLVo.readCnt}" type="number" /></a>
				</c:if>
				<c:if test="${baBrdBVo.readHstUseYn != 'Y'}">
				<u:out value="${bbBullLVo.readCnt}" type="number" />
				</c:if>
				</td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'PROS_CNT'}">
			<td class="${bodyClass}"><u:out value="${bbBullLVo.prosCnt}" type="number" /></td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'CONS_CNT'}">
			<td class="${bodyClass}"><u:out value="${bbBullLVo.consCnt}" type="number" /></td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'RECMD_CNT'}">
			<td class="${bodyClass}"><u:out value="${bbBullLVo.recmdCnt}" type="number" /></td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'SCRE'}">
			<td class="${bodyClass}"><u:out value="${bbBullLVo.scre}" type="number" /></td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'CONT'}">
			<td class="${bodyClass}"><u:out value="${bbBullLVo.cont}" /></td>
			</c:if>
			<c:if test="${baColmDispDVo.useYn == 'Y' && colmVo.exColmYn == 'Y'}">
				<td class="${bodyClass}">
				<c:if test="${colmTyp == 'TEXT' || colmTyp == 'TEXTAREA' || colmTyp == 'PHONE' || colmTyp == 'CALENDAR'}">
					<u:out value="${bbBullLVo.getExColm(colmVo.colmNm)}" />
				</c:if>
				<c:if test="${colmTyp == 'CALENDARTIME'}"><u:out value="${bbBullLVo.getExColm(colmVo.colmNm)}" type="longdate"/></c:if>
				<c:if test="${fn:startsWith(colmTyp,'CODE')}">
					<u:set test="${cdListIndex2 == null}" var="cdListIndex2" value="0" elseValue="${cdListIndex2 + 1}" />
					<c:if test="${colmTyp == 'CODE' || colmTyp == 'CODERADIO'}">					
						<c:forEach items="${cdList[cdListIndex2]}" var="cd" varStatus="status">
						<c:if test="${cd.cdId == bbBullLVo.getExColm(colmVo.colmNm)}">${cd.rescNm}</c:if>
						</c:forEach>
					</c:if><c:if test="${colmTyp == 'CODECHK'}"><c:set var="chkIndex"/>
						<c:forEach items="${cdList[cdListIndex2]}" var="cd" varStatus="status"><c:set var="checked" value="N" 
					/><c:forTokens var="chkId" items="${bbBullLVo.getExColm(colmVo.colmNm)}" delims=","><c:if test="${chkId==cd.cdId }"><c:set var="checked" value="Y" 
					/></c:if></c:forTokens><c:if test="${checked eq 'Y'}"><c:set var="chkIndex" value="${empty chkIndex ? 0 : chkIndex+1 }"/><c:if test="${chkIndex>0 }">,</c:if>${cd.rescNm}</c:if></c:forEach></c:if>
				</c:if><c:if test="${colmTyp == 'USER' || colmTyp == 'DEPT'}"><c:set var="map" value="${bbBullLVo.exColMap}" scope="request" 
				/><u:convertMap srcId="map" attId="${fn:toLowerCase(colmVo.colmNm)}MapList" var="mapList" /><c:if test="${!empty mapList }"><c:forEach 
			var="mapVo" items="${mapList }" varStatus="mapStatus"><c:if test="${mapStatus.index>0 }">,</c:if><c:if test="${colmTyp == 'USER' }"><a href="javascript:viewUserPop('${mapVo.id }');">${mapVo.rescNm }</a></c:if
			><c:if test="${colmTyp == 'DEPT' }">${mapVo.rescNm }</c:if></c:forEach></c:if></c:if>
				</td>
			</c:if>
		</c:if>
		</c:forEach>
	</c:if>
	<c:if test="${baBrdBVo.optMap.fileUploadYn eq 'Y'}"><td class="body_ct"><c:if test="${bbBullLVo.fileCnt > 0}"><u:set test="${bbBullLVo.secuYn == 'Y'}" var="viewFileListPop" value="openLogin" elseValue="viewFileListPop" 
	/><a href="javascript:${viewFileListPop }('${bbBullLVo.bullId }', 'chkValidBull');"><u:icon type="att" /></a></c:if></td></c:if>
	</tr>
	</c:forEach>
</c:if>
<!-- 게시물 목록 -->
<c:if test="${fn:length(bbBullLVoList) == 0}">
	<tr>
	<td class="nodata" colspan="${maxColCnt }"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(bbBullLVoList) > 0}">
	<c:forEach items="${bbBullLVoList}" var="bbBullLVo" varStatus="status">
	<u:set test="${bbBullLVo.secuYn == 'Y'}" var="viewBull" value="openLogin" elseValue="viewBull" />
	<u:set test="${faqYn eq 'Y'}" var="viewBull" value="openFaqView" elseValue="${viewBull }" />
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"' onclick="${viewBull}('${bbBullLVo.bullId}');" style="cursor:pointer;">
		<c:set var="cdListIndex" value="${null }" />
	<td class="body_ct"><u:out value="${recodeCount - bbBullLVo.rnum + 1}" type="number" /></td>
	<c:if test="${fn:length(baColmDispDVoList) > 0}">
		<c:forEach items="${baColmDispDVoList}" var="baColmDispDVo" varStatus="status">
			<c:set var="colmVo" value="${baColmDispDVo.colmVo}" />
			<c:set var="colmTyp" value="${colmVo.colmTyp}" />
			<c:set var="colmTypVal" value="${colmVo.colmTypVal}" />
			<u:set var="alnVa" test="${empty baColmDispDVo.alnVa && colmVo.colmNm == 'SUBJ' }" value="left" elseValue="${baColmDispDVo.alnVa }"/>
		<c:if test="${baColmDispDVo.listDispYn == 'Y'}"><u:set var="bodyClass" test="${!empty alnVa && alnVa ne 'center'}" value="${alnVa eq 'right' ? 'body_rt' : 'body_lt'}" elseValue="body_ct"/>
			<c:if test="${colmVo.colmNm == 'CAT_ID'}">
			<td class="${bodyClass}"><div class="ellipsis" title="${bbBullLVo.catNm}">${bbBullLVo.catNm}</div></td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'SUBJ'}">
			<td class="${bodyClass}">
				<u:set test="${bbBullLVo.readYn == 'Y' && faqYn ne 'Y'}" var="style" value="font-weight: normal;" elseValue="font-weight: bold;" />
				<u:set test="${bbBullLVo.ugntYn == 'Y' && faqYn ne 'Y'}" var="style" value="${style} color:red;" elseValue="${style}" />
				<div class="ellipsis" title="${bbBullLVo.subj}" style="${style}">
				<u:icon type="indent" display="${bbBullLVo.replyDpth > 0}" repeat="${bbBullLVo.replyDpth - 1}" />
				<u:icon type="reply" display="${bbBullLVo.replyDpth > 0}" />
				<u:icon type="new" display="${bbBullLVo.newYn == 'Y'}" />
				<c:if test="${bbBullLVo.ugntYn == 'Y'}"><span style="${style}">[<u:msg titleId="bb.option.ugnt" alt="긴급" />]</span></c:if>
				<c:if test="${bbBullLVo.secuYn == 'Y'}"><span style="${style}">[<u:msg titleId="bb.option.secu" alt="보안" />]</span></c:if>
				<u:out value="${bbBullLVo.subj}" type="html"/>
				<c:if test="${baBrdBVo.cmtYn == 'Y'}"><span style="font-size: 10px;">(<u:out value="${bbBullLVo.cmtCnt}" type="number" />)</span></c:if>
				</div>				
				</td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'REGR_UID'}">
			<td class="${bodyClass}">
				<c:if test="${baBrdBVo.brdTypCd == 'N'}">
				<a href="javascript:viewUserPop('${bbBullLVo.regrUid}');"><u:out value="${bbBullLVo.regrNm}" /></a>
				</c:if>
				<c:if test="${baBrdBVo.brdTypCd == 'A'}">
				<u:out value="${bbBullLVo.anonRegrNm}" />
				</c:if>
			</td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'MODR_UID'}">
			<td class="${bodyClass}">
				<c:if test="${baBrdBVo.brdTypCd == 'N'}">
				<a href="javascript:viewUserPop('${bbBullLVo.modrUid}');"><u:out value="${bbBullLVo.modrNm}" /></a>
				</c:if>
				<c:if test="${baBrdBVo.brdTypCd == 'A'}">
				<u:out value="${bbBullLVo.anonRegrNm}" />
				</c:if>
			</td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'REG_DT'}">
			<td class="${bodyClass}"><u:out value="${bbBullLVo.regDt}" type="longdate" /></td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'MOD_DT'}">
			<td class="${bodyClass}"><u:out value="${bbBullLVo.modDt}" type="longdate" /></td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'REZV_DT' || colmVo.colmNm == 'BULL_REZV_DT'}">
			<td class="${bodyClass}"><u:out value="${bbBullLVo.bullRezvDt}" type="longdate" /></td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'EXPR_DT' || colmVo.colmNm == 'BULL_EXPR_DT'}">
			<td class="${bodyClass}"><u:out value="${bbBullLVo.bullExprDt}" type="longdate" /></td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'READ_CNT'}">
			<td class="${bodyClass}">
				<c:if test="${baBrdBVo.readHstUseYn == 'Y'}">
				<a href="javascript:readHst('${bbBullLVo.bullId}');"><u:out value="${bbBullLVo.readCnt}" type="number" /></a>
				</c:if>
				<c:if test="${baBrdBVo.readHstUseYn != 'Y'}">
				<u:out value="${bbBullLVo.readCnt}" type="number" />
				</c:if>
			</td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'PROS_CNT'}">
			<td class="${bodyClass}"><u:out value="${bbBullLVo.prosCnt}" type="number" /></td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'CONS_CNT'}">
			<td class="${bodyClass}"><u:out value="${bbBullLVo.consCnt}" type="number" /></td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'RECMD_CNT'}">
			<td class="${bodyClass}"><u:out value="${bbBullLVo.recmdCnt}" type="number" /></td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'SCRE'}">
			<td class="${bodyClass}"><c:forEach begin="1" end="5" step="1" varStatus="status"><u:set test="${status.count <= bbBullLVo.scre}" var="star" value="★" elseValue="☆" />${star}</c:forEach></td>
			</c:if>
			<c:if test="${colmVo.colmNm == 'CONT'}">
			<td class="${bodyClass}"><u:out value="${bbBullLVo.cont}" /></td>
			</c:if>
			<c:if test="${baColmDispDVo.useYn == 'Y' && colmVo.exColmYn == 'Y'}">
			<td class="${bodyClass}">
			<c:if test="${colmTyp == 'TEXT' || colmTyp == 'TEXTAREA' || colmTyp == 'PHONE' || colmTyp == 'CALENDAR'}">
				<u:out value="${bbBullLVo.getExColm(colmVo.colmNm)}" />
			</c:if>
			<c:if test="${colmTyp == 'CALENDARTIME'}"><u:out value="${bbBullLVo.getExColm(colmVo.colmNm)}" type="longdate"/></c:if>
			<c:if test="${fn:startsWith(colmTyp,'CODE')}">
				<u:set test="${cdListIndex == null}" var="cdListIndex" value="0" elseValue="${cdListIndex + 1}" />
				<c:if test="${colmTyp == 'CODE' || colmTyp == 'CODERADIO'}">					
					<c:forEach items="${cdList[cdListIndex]}" var="cd" varStatus="status">
					<c:if test="${cd.cdId == bbBullLVo.getExColm(colmVo.colmNm)}">${cd.rescNm}</c:if>
					</c:forEach>
				</c:if><c:if test="${colmTyp == 'CODECHK'}"><c:set var="chkIndex"/>
					<c:forEach items="${cdList[cdListIndex]}" var="cd" varStatus="status"><c:set var="checked" value="N" 
				/><c:forTokens var="chkId" items="${bbBullLVo.getExColm(colmVo.colmNm)}" delims=","><c:if test="${chkId==cd.cdId }"><c:set var="checked" value="Y" 
				/></c:if></c:forTokens><c:if test="${checked eq 'Y'}"><c:set var="chkIndex" value="${empty chkIndex ? 0 : chkIndex+1 }"/><c:if test="${chkIndex>0 }">,</c:if>${cd.rescNm}</c:if></c:forEach></c:if>
			</c:if><c:if test="${colmTyp == 'USER' || colmTyp == 'DEPT'}"><c:set var="map" value="${bbBullLVo.exColMap}" scope="request" 
			/><u:convertMap srcId="map" attId="${fn:toLowerCase(colmVo.colmNm)}MapList" var="mapList" /><c:if test="${!empty mapList }"><c:forEach 
		var="mapVo" items="${mapList }" varStatus="mapStatus"><c:if test="${mapStatus.index>0 }">,</c:if><c:if test="${colmTyp == 'USER' }"><a href="javascript:viewUserPop('${mapVo.id }');">${mapVo.rescNm }</a></c:if
		><c:if test="${colmTyp == 'DEPT' }">${mapVo.rescNm }</c:if></c:forEach></c:if></c:if>
			</td>
			</c:if>
		</c:if>
		</c:forEach>
	</c:if>
	<c:if test="${baBrdBVo.optMap.fileUploadYn eq 'Y'}"><td class="body_ct"><c:if test="${bbBullLVo.fileCnt > 0}"><u:set test="${bbBullLVo.secuYn == 'Y'}" var="viewFileListPop" value="openLogin" elseValue="viewFileListPop" 
	/><a href="javascript:${viewFileListPop }('${bbBullLVo.bullId }', 'chkValidBull');"><u:icon type="att" /></a></c:if></td></c:if>
	</tr>
	<!-- FAQ 게시판 일 경우 -->
	<c:if test="${faqYn eq 'Y'}">
		<tr id="faq_${bbBullLVo.bullId }" style="display:none;" data-load="N"><td colspan="${maxColCnt }" style="padding:3px;"><u:secu auth="A">
<div style="text-align:right;"><u:buttonS titleId="cm.btn.mod" alt="수정" onclick="modBull('${bbBullLVo.bullId }');" auth="A" ownerUid="${bbBullLVo.regrUid}" 
/><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="delBull('${bbBullLVo.bullId }');" auth="A" ownerUid="${bbBullLVo.regrUid}" /></div>
</u:secu><div id="cont"></div>
		<c:if test="${baBrdBVo.optMap.fileUploadYn eq 'Y'}">
		<div id="file"><iframe id="fileFrm_${bbBullLVo.bullId }" name="fileFrm" src="/cm/util/reloadable.do" style="width:100%;height:180px;scroll:" frameborder="0" marginheight="0" marginwidth="0" ></iframe></div></c:if></td></tr>
	</c:if>
	</c:forEach>
</c:if>
</table>
</div>
<u:blank />
<u:pagination />


<c:if test="${empty ptCompBVoList || (!empty ptCompBVoList && !empty param.brdId) }">
<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" auth="R" />
	<c:if test="${empty ptCompBVoList }"><u:button titleId="cm.btn.excelDown" alt="엑셀다운" onclick="excelDownFile();" auth="R" /></c:if>
	<u:button titleId="cm.btn.write" alt="등록" href="./setBull.do?menuId=${menuId}&brdId=${param.brdId}${compIdParam }" auth="W" />
</u:buttonArea>
</c:if>

<% // 게시판 담당자 %>
<c:if test="${baBrdBVo.pichDispYn == 'Y'}">
<u:title titleId="bb.jsp.listBull.pich.title" alt="게시판 담당자" type="small" />

<u:listArea id="pichArea">

	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.pichNm" alt="담당자" /></td>
	<td width="32%" class="body_lt"><a href="javascript:viewUserPop('${baBrdBVo.pichUid}');">${baBrdBVo.pichVo.rescNm}</a></td>
	<td width="18%" class="head_lt"><u:msg titleId="cols.dept" alt="부서" /></td>
	<td width="32%" class="body_lt">${baBrdBVo.pichVo.deptRescNm}</td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.phon" alt="전화번호" /></td>
	<td class="body_lt">${baBrdBVo.pichPinfoVo.mbno}</td>
	<td class="head_lt"><u:msg titleId="cols.email" alt="이메일" /></td>
	<td class="body_lt">
		<a href="javascript:parent.mailToPop('${baBrdBVo.pichPinfoVo.email}')" title="<u:msg titleId='or.jsp.viewUserPop.mailToPop' />">${baBrdBVo.pichPinfoVo.email}</a>
	</td>
	</tr>

</u:listArea>
</c:if>
<form id="excelForm">
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="brdId" value="${param.brdId }" />
	<u:input type="hidden" id="strtYmd" value="${param.strtYmd}" />
	<u:input type="hidden" id="endYmd" value="${param.endYmd}" />
	<u:input type="hidden" id="catId" value="${param.catId}" />
	<u:input type="hidden" id="schCat" value="${param.schCat }" />
	<u:input type="hidden" id="schWord" value="${param.schWord}" />
</form>
