<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

java.util.Map sysPlocMap = (java.util.Map)request.getAttribute("sysPlocMap");
// 입사일 기준 - 정산 포함
if("Y".equals(sysPlocMap.get("enterBaseAnbMak"))){
	request.setAttribute("modTypCds", new String[]{"forw","cre","use","ongo"});
} else {
	request.setAttribute("modTypCds", new String[]{"forw","cre","use","ongo"});
}

%><script type="text/javascript">
<!--<%--
[버튼] 차감(modTypCd:null), 추가(modTypCd:'use')--%>
function modifyData(modTypCd){
	dialog.close("setModifyDialog");
	var popTitle = '';
	if(modTypCd == 'use'){
		popTitle = '<u:msg titleId="wd.jsp.useList" alt="사용 내역" /> (<u:msg titleId="cm.btn.add" alt="추가" />)';
	} else {
		popTitle = '<u:msg titleId="wd.jsp.detlList" alt="상세 내역" /> (<u:msg titleId="wd.cmd.modify" alt="차감" />)';
	}
	var url = "./setModifyPop.do?menuId=${menuId}&year=${param.year}&anbTypCd=${param.anbTypCd}&odurUid=${param.odurUid}" + (modTypCd==null ? '' : '&modTypCd='+modTypCd);
	dialog.open("setModifyDialog", popTitle, url);
}<%--
[버튼] 삭제 --%>
function deleteData(){
	dialog.close("setModifyDelDialog");
	var useYmds = [];
	$("#wdUseListArea input[name='useYmd']:checked").each(function(){
		useYmds.push($(this).val());
	});
	if(useYmds.length==0){
		alertMsg('cm.msg.noSelect');<%-- cm.msg.noSelect=선택한 항목이 없습니다. --%>
		return;
	}
	var popTitle = '<u:msg titleId="wd.jsp.useList" alt="사용 내역" /> (<u:msg titleId="cm.btn.del" alt="삭제" />)';
	var url = "./setModifyDelPop.do?menuId=${menuId}&year=${param.year}&anbTypCd=${param.anbTypCd}&odurUid=${param.odurUid}&useYmds=" + useYmds.join(',');
	dialog.open("setModifyDelDialog", popTitle, url);
}<%--
[탭클릭] --%>
function viewDetlTabClick(id){
	var $btnArea = $("#wdViewDetlCmdArea");
	if(id=='detl'){
		$btnArea.find("#cmdModify").show();
		$btnArea.find("#cmdAddUse").hide();
		$btnArea.find("#cmdDelUse").hide();
	} else if(id=='use'){
		$btnArea.find("#cmdModify").hide();
		$btnArea.find("#cmdAddUse").show();
		$btnArea.find("#cmdDelUse").show();
	}
}<%--
[라이오] 상세내역 종류 클릭 --%>
function filterModTypCd(modTypCd){
	var dataModTypCd;
	$("#wdCreList tbody:first tr").each(function(){
		dataModTypCd = $(this).attr("data-modTypCd");
		if(dataModTypCd != null){
			if(modTypCd=='') $(this).show();
			else {
				if(dataModTypCd.startsWith(modTypCd)) $(this).show();
				else $(this).hide();
			}
		}
	});
}
// -->
</script>
<div style="width:900px">
<u:listArea id="detailHeaderArea">

	<tr><u:set test="${sysPlocMap.enterBaseAnbMak eq 'Y'}" var="tdWidth" value="11%" elseValue="13%" />
	<td class="head_ct"><u:msg titleId="cols.userNm" alt="사용자명" /></td>
	<td width="13%" class="head_ct"><u:msg titleId="cols.entraYmd" alt="입사일" /></td>
	
	<td width="${tdWidth}" class="head_ct"><u:msg titleId="wd.cnt.forw" alt="이월수" /></td>
	<td width="${tdWidth}" class="head_ct"><u:msg titleId="wd.cnt.cre" alt="발생수" /></td>
	<td width="${tdWidth}" class="head_ct"><u:msg titleId="wd.cnt.use" alt="사용수" /></td>
	<td width="${tdWidth}" class="head_ct"><u:msg titleId="wd.cnt.left" alt="잔여" /></td>
	<td width="${tdWidth}" class="head_ct"><u:msg titleId="wd.cnt.ongo" alt="결재중" /></td>
	</tr>

	<tr>
	<td class="body_ct"><a href="javascript:viewUserPop('${orOdurBVo.odurUid}');"><u:out value="${orOdurBVo.rescNm}" /></a></td>
	<td class="body_ct"><u:out value="${orOdurBVo.entraYmd}" type="shortdate" /></td>
	
	<td class="body_ct" style="${not empty wdAnbBVo.forwModCnt and wdAnbBVo.forwModCnt ne 0.0
		? 'color:#0F6F8E; font-weight:bold;' : ''}">${not empty wdAnbBVo ? (wdAnbBVo.forwCnt + wdAnbBVo.forwModCnt) : ''}</td><%-- 이월수 --%>
	<td class="body_ct" style="${not empty wdAnbBVo.creModCnt and wdAnbBVo.creModCnt ne 0.0
		? 'color:#0F6F8E; font-weight:bold;' : ''}">${not empty wdAnbBVo ? (wdAnbBVo.creCnt + wdAnbBVo.creModCnt) : ''}</td><%-- 발생수 --%>
	<td class="body_ct" style="${not empty wdAnbBVo.useModCnt and wdAnbBVo.useModCnt ne 0.0
		? 'color:#0F6F8E; font-weight:bold;' : ''}">${not empty wdAnbBVo ? (wdAnbBVo.useCnt + wdAnbBVo.useModCnt) : ''}</td><%-- 사용수 --%>
	<td class="body_ct">${not empty wdAnbBVo ? 
			(wdAnbBVo.forwCnt + wdAnbBVo.forwModCnt)
			+ (wdAnbBVo.creCnt + wdAnbBVo.creModCnt)
			- (wdAnbBVo.useCnt + wdAnbBVo.useModCnt) : ''}</td><%-- 잔여 --%>
	<td class="body_ct" style="${not empty wdAnbBVo.ongoModCnt and wdAnbBVo.ongoModCnt ne 0.0
		? 'color:#0F6F8E; font-weight:bold;' : ''}">${not empty wdAnbBVo ? (wdAnbBVo.ongoCnt + wdAnbBVo.ongoModCnt) : ''}</td><%-- 결재중 --%>
	</tr>

</u:listArea>

<div class="blank"></div>

<u:tabGroup id="wdListTab" noBottomBlank="${true}">
	<u:tab id="wdListTab" areaId="wdUseListArea" titleId="wd.jsp.useList" alt="사용 내역"
		on="${param.modTypCd eq 'use'}" onclick="viewDetlTabClick('use');" />
	<u:tab id="wdListTab" areaId="wdCreListArea" titleId="wd.jsp.detlLog" alt="상세 로그"
		on="${param.modTypCd ne 'use'}" onclick="viewDetlTabClick('detl');" />
</u:tabGroup>
<u:tabArea
	outerStyle="height:380px; overflow-x:hidden; overflow-y:auto; padding:10px 10px 0px 10px;"
	innerStyle = "NO_INNER_IDV">

<div id="wdUseListArea" style="${param.modTypCd ne 'use' ? 'display:none;' : ''}">
<u:listArea id="wdUseList" colgroup="3%,11%,8%,17%,8%,*">
<tr>
	<td class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all'
		/>" onclick="checkAllCheckbox('wdUseListArea', this.checked);" value=""/></td>
	<td class="head_ct"><u:msg titleId="wd.jsp.useDate" alt="사용일" /></td>
	<td class="head_ct"><u:msg titleId="wd.jsp.cnt" alt="수량" /></td>
	<td class="head_ct"><u:msg titleId="wd.jsp.regDate" alt="등록일" /></td>
	<td class="head_ct"><u:msg titleId="wd.cnt.ongo" alt="결재중" /></td>
	<td class="head_ct"><u:msg titleId="wd.jsp.remark" alt="비고" /></td>
</tr>
<c:if test="${fn:length(wdAnbUseLVoList)==0}" >
<tr>
	<td class="nodata" colspan="6"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
</tr>
</c:if>
<c:if test="${fn:length(wdAnbUseLVoList)>0}" >
<c:forEach items="${wdAnbUseLVoList}" var="wdAnbUseLVo" varStatus="status">
<tr>
	<td class="bodybg_ct"><input type="checkbox" name="useYmd" value="<u:out value="${wdAnbUseLVo.useYmd}" type="shortdate" />" /></td>
	<td class="body_ct"><u:out value="${wdAnbUseLVo.useYmd}" type="shortdate" /></td>
	<td class="body_ct" style="${empty wdAnbUseLVo.apvNo ? 
		'color:#0F6F8E; font-weight:bold;' : ''}"><u:out value="${wdAnbUseLVo.useCnt}" /></td>
	<td class="body_ct"><u:out value="${wdAnbUseLVo.regDt}" type="longdate" /></td>
	<td class="body_ct">${wdAnbUseLVo.cmplYn eq 'Y' ? '' : 'Y'}</td>
	<td class="body_lt"><c:if
		test="${empty wdAnbUseLVo.apvNo}"><u:out value="${wdAnbUseLVo.rson}" /></c:if><c:if
		test="${not empty wdAnbUseLVo.apvNo}">[${wdAnbUseLVo.apvNo}] <a href="./viewDocWinPop.do?menuId=${menuId}&apvNo=${wdAnbUseLVo.apvNo}" target="_blank"><u:out value="${wdAnbUseLVo.rson}" /></a></c:if></td>
</tr>
</c:forEach>
</c:if>

</u:listArea>
</div>

<div id="wdCreListArea" style="${param.modTypCd eq 'use' ? 'display:none;' : ''}">

<table cellspacing="0" cellpadding="0" border="0" style="margin-top:-2px;">
<tr><u:radio name="rdoModTypCd" value="" titleId="cm.option.all" alt="전체" onclick="filterModTypCd(this.value)" checked="true"
	/><c:forEach items="${modTypCds}" var="modTypCd"><u:radio
		name="rdoModTypCd" value="${modTypCd}" titleId="wd.modTypCd.${modTypCd}" onclick="filterModTypCd(this.value)"
	/></c:forEach></tr>
</table>
<div class="blank"></div>

<u:listArea id="wdCreList" colgroup="10%,8%,10%,17%,*">
<tr>
	<td class="head_ct"><u:msg titleId="wd.jsp.gubun" alt="구분" /></td>
	<td class="head_ct"><u:msg titleId="wd.jsp.cnt" alt="수량" /></td>
	<td class="head_ct"><c:if
		test="${param.anbTypCd eq 'repb'}"><u:msg titleId="wd.jsp.useCreDate" alt="사용(발생)일" /></c:if><c:if
		test="${param.anbTypCd ne 'repb'}"><u:msg titleId="wd.jsp.useDate" alt="사용일" /></c:if></td>
	<td class="head_ct"><u:msg titleId="wd.jsp.regDate" alt="등록일" /></td>
	<td class="head_ct"><u:msg titleId="wd.jsp.remark" alt="비고" /></td>
</tr>
<c:if test="${fn:length(wdAnbModLVoList)==0}" >
<tr>
	<td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
</tr>
</c:if>
<c:if test="${fn:length(wdAnbModLVoList)>0}" >
<c:forEach items="${wdAnbModLVoList}" var="wdAnbModLVo" varStatus="status">
<tr data-modTypCd="${wdAnbModLVo.modTypCd}">
	<td class="body_ct"><u:msg titleId="wd.modTypCd.${wdAnbModLVo.modTypCd}" alt="구분" /></td>
	<td class="body_ct" style="${wdAnbModLVo.modified ? 
		'color:#0F6F8E; font-weight:bold;' : ''}">${wdAnbModLVo.modCnt}</td>
	<td class="body_ct"><u:out value="${wdAnbModLVo.useYmd}" type="shortdate" /></td>
	<td class="body_ct"><u:out value="${wdAnbModLVo.modDt}" type="longdate" /></td>
	<td class="body_lt"><c:if
		test="${not empty wdAnbModLVo.apvNo}">[${wdAnbModLVo.apvNo}] </c:if><u:out value="${wdAnbModLVo.note}" /><c:if
		test="${not empty wdAnbModLVo.rson}"><u:out value=" << " /><u:out value="${wdAnbModLVo.rson}" /></c:if></td>
</tr>
</c:forEach>
</c:if>
</u:listArea>

</div>

</u:tabArea>


<u:buttonArea id="wdViewDetlCmdArea">
	<u:button id="cmdModify" titleId="wd.cmd.modify" alt="차감" href="javascript:modifyData();" style="${param.modTypCd eq 'use' ? 'display:none;' : ''}" />
	<u:button id="cmdAddUse" titleId="cm.btn.add" alt="추가" href="javascript:modifyData('use');" style="${param.modTypCd ne 'use' ? 'display:none;' : ''}" />
	<u:button id="cmdDelUse" titleId="cm.btn.del" alt="삭제" href="javascript:deleteData();" style="${param.modTypCd ne 'use' ? 'display:none;' : ''}" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>


</div>