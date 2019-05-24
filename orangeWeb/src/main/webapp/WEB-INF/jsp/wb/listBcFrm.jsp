<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(noSelectMsg){
	var arr=[], id, obj;
	$("#bcListArea tbody:first input[type='checkbox']:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
};

<%// 선택목록 리턴 %>
function fnSelBc(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) return selRowInArr(arr);
	else return null;
};
<%// 배열에 담긴 목록%>
function selRowInArr(rowArr){
	var objArr = [], $bcId;
	//if(delVa!='') objArr.push(delVa);
	for(var i=0;i<rowArr.length;i++){
		$bcId = $(rowArr[i]).find("input[name='bcId']");
		if($bcId.val()!=''){
			objArr.push($bcId.val());
		}
		//$(rowArr[i]).remove();
	}
	return objArr;
	//$("#delList").val(objArr.join(','));
};

<%// [아이콘 선택] - 아이콘 클릭 - 아이콘 활성화 함 %>
function activeIcon(obj){
	$gIconArea = $("#iconArea");
	$gIconArea.find("dd#iconArea_sub").each(function(){
	$(this).attr("class", "cardtxt");
	});
	$(obj).parent().attr("class", "cardtxton");
};

//상세보기
function viewBc(bcId) {
	parent.viewBc(bcId);
};

$(document).ready(function() {
setUniformCSS();
});
</script>
<form id="searchForm" name="searchForm" style="padding:10px;" action="./listBcFrm.do">
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="typ" value="${param.typ}" />
	<u:input type="hidden" id="schInitial" value="${param.schInitial}" />
	<u:input type="hidden" id="schFldTypYn" value="${param.schFldTypYn}" />
	<u:input type="hidden" id="schFldId" value="${param.schFldId}" />
	<c:if test="${!empty param.listType }">
		<u:input type="hidden" id="listType" value="${param.listType}" />
	</c:if>
	
	<div class="front">
	<c:if test="${_lang eq 'ko'}">
		<div class="front_left">
			<table border="0" cellpadding="0" cellspacing="0">
				<tbody>
					<tr>
						<td style="float:left; padding:2px 0 0 3px; margin:0 0 0 -3px;">
							<c:set var="initialList" value="전체,ㄱ,ㄴ,ㄷ,ㄹ,ㅁ,ㅂ,ㅅ,ㅇ,ㅈ,ㅊ,ㅋ,ㅌ,ㅍ,ㅎ,A~Z,123"/>
							<div class="cardarea" id="iconArea" style="float:left; padding:4px 0 0 3px; margin:0 0 0 -3px;">
								<dl>
								<c:forTokens var="initialList" items="${initialList }" delims="," varStatus="status">
								<dd id="iconArea_sub" class="${(status.count == 1 && empty param.schInitial) || initialList == param.schInitial  ? 'cardtxton' : 'cardtxt'}"><a href="javascript:;" onclick="activeIcon(this);searchForm.schInitial.value='${initialList == '전체' ? '' : initialList}';document.searchForm.submit();">${initialList }</a></dd>
								</c:forTokens>
								</dl>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</c:if>
		<c:if test="${_lang ne 'ko'}">
			<div class="front_left">
				<table border="0" cellpadding="0" cellspacing="0">
					<tbody>
						<tr>
							<td style="float:left; padding:2px 0 0 3px; margin:0 0 0 -3px;">
								<c:set var="initialList" value="ALL,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,123"/>
								<div class="cardarea" id="iconArea" style="float:left; padding:4px 0 0 3px; margin:0 0 0 -3px;">
									<dl>
									<c:forTokens var="initialList" items="${initialList }" delims="," varStatus="status">
									<dd id="iconArea_sub" class="${(status.count == 1 && empty param.schInitial) || initialList == param.schInitial  ? 'cardtxton' : 'cardtxt'}"><a href="javascript:;" onclick="activeIcon(this);searchForm.schInitial.value='${initialList == 'ALL' ? '' : initialList}';document.searchForm.submit();">${initialList }</a></dd>
									</c:forTokens>
									</dl>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</c:if>
	
	
	
	
	<c:if test="${param.listType ne 'list' }">
		<div class="front_right">
			<table border="0" cellpadding="0" cellspacing="0">
				<tbody>
					<tr>
						<td class="frontbtn">
							<c:choose>
								<c:when test="${param.typ eq 'C' || ( empty wbBcUserScrnSetupRVo && empty param.typ )|| ( empty param.typ && wbBcUserScrnSetupRVo.lstTypCd eq 'BC' )}"><u:buttonS href="./list${param.suffixUrl }BcFrm.do?menuId=${menuId}&typ=L&schFldTypYn=A&schFldId=${param.schFldId}" titleId="wb.btn.txtView" alt="리스트로보기" /></c:when>
								<c:otherwise><u:buttonS href="./list${param.suffixUrl }BcFrm.do?menuId=${menuId}&typ=C&schFldTypYn=A&schFldId=${param.schFldId}" titleId="wb.btn.bcView" alt="명함으로보기" /></c:otherwise>
							</c:choose>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</c:if>
	
</div>
<% // 목록1 %>
<c:choose>
<c:when test="${param.typ eq 'L' || ( empty param.typ && wbBcUserScrnSetupRVo.lstTypCd eq 'LIST' )}">
	<c:set var="dftWdth" value="${97 / fn:length(wbBcUserLstSetupRVoList) }%"/>
	<div id="bcListArea" class="listarea">
		<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
		<tr id="headerTr">
			<td width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('bcListArea', this.checked);" value=""/></td>
			<c:forEach	items="${wbBcUserLstSetupRVoList}" var="wbBcUserLstSetupRVo" varStatus="status">
				<td width="${empty wbBcUserLstSetupRVo.wdthPerc ? dftWdth : wbBcUserLstSetupRVo.wdthPerc}" class="head_ct"><u:msg titleId="${wbBcUserLstSetupRVo.msgId }" alt="이름" /></td>
			</c:forEach>
		</tr>
		<c:choose>
			<c:when test="${!empty wbBcBMapList}">
				<c:forEach var="wbBcBMap" items="${wbBcBMapList}" varStatus="status">
					<c:set var="wbBcBMap" value="${wbBcBMap}" scope="request" />
					<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
						<td class="bodybg_ct"><u:checkbox name="bcCheck" value="${wbBcBMap.bcId }" checked="false" /><u:input type="hidden" name="bcId" value="${wbBcBMap.bcId }"/></td>
						<c:forEach	items="${wbBcUserLstSetupRVoList}" var="wbBcUserLstSetupRVo" varStatus="status">
							<td class="body_lt" align="${wbBcUserLstSetupRVo.alnVa}">
								<c:choose>
									<c:when test="${wbBcUserLstSetupRVo.atrbId eq 'bcNm' }"><div class="ellipsis" title="<u:convertMap srcId="wbBcBMap" attId="${wbBcUserLstSetupRVo.atrbId}" type="html" />"><a href="javascript:viewBc('${wbBcBMap.bcId }');"><u:convertMap srcId="wbBcBMap" attId="${wbBcUserLstSetupRVo.atrbId}" type="html" /></a></div></c:when>
									<c:when test="${wbBcUserLstSetupRVo.atrbId eq 'email' }">
										<a href="javascript:parent.mailToPop('<u:convertMap srcId="wbBcBMap" attId="${wbBcUserLstSetupRVo.atrbId}" type="script" />')" title="<u:msg titleId='or.jsp.viewUserPop.mailToPop' />"><div class="ellipsis" title="<u:convertMap srcId="wbBcBMap" attId="${wbBcUserLstSetupRVo.atrbId}" type="html" />"><u:convertMap srcId="wbBcBMap" attId="${wbBcUserLstSetupRVo.atrbId}" type="html" /></div></a>
									</c:when>
									<c:otherwise><div class="ellipsis" title="<u:convertMap srcId="wbBcBMap" attId="${wbBcUserLstSetupRVo.atrbId}" type="html" />"><u:convertMap srcId="wbBcBMap" attId="${wbBcUserLstSetupRVo.atrbId}" type="html" /></div></c:otherwise>
								</c:choose>
							</td>
						</c:forEach>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<td class="nodata" colspan="${fn:length(wbBcUserLstSetupRVoList) +1}"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
				</tr>
			</c:otherwise>
		</c:choose>
		</table>
	</div>
</c:when>
<c:otherwise>
<% // 목록2 %>
<div style="position:relative; width:100%; height:313px; background:#ffffff; border:1px solid #bfc8d2; overflow-y:auto; overflow-x:auto;">
	<c:choose>
		<c:when test="${!empty wbBcBMapList}">
			<c:forEach var="wbBcBMap" items="${wbBcBMapList}" varStatus="status">
				<c:set var="wbBcBMap" value="${wbBcBMap}" scope="request" />
				<div class="cardbox" style="width:23.8%;">
					<div class="ptltit">
						<dl>
						<dd class="title"><a href="javascript:viewBc('${wbBcBMap.bcId }');">${wbBcBMap.bcNm }</a>
						<c:if test="${wbBcBMap.publTypCd eq 'priv' }"><u:icon type="lock" /></c:if>
						</dd>
						</dl>
					</div>
					<div class="ptlbody">
						<table class="ptltable" border="0" cellpadding="0" cellspacing="0">														
							<c:forEach	items="${wbBcUserLstSetupRVoList}" var="wbBcUserLstSetupRVo" varStatus="status">
								<c:if test="${wbBcUserLstSetupRVo.dispYn eq 'Y'}">
									<c:if test="${status.count > 1 }"><tr><td colspan="2" class="line"></td></tr></c:if>
									<tr>
										<td width="30%" class="head_lt" ><c:if test="${( wbBcUserLstSetupRVo.atrbId eq 'compPhon' || wbBcUserLstSetupRVo.atrbId eq 'homePhon' || wbBcUserLstSetupRVo.atrbId eq 'mbno') && wbBcBMap.dftCntcTypCd eq wbBcUserLstSetupRVo.atrbId }"><u:mandatory /></c:if><u:msg titleId="${wbBcUserLstSetupRVo.msgId }"/></td>
										<td class="body_lt">
											<c:choose>
												<c:when test="${wbBcUserLstSetupRVo.atrbId eq 'email' }">
													<a href="javascript:parent.mailToPop('<u:convertMap srcId="wbBcBMap" attId="${wbBcUserLstSetupRVo.atrbId}" type="script" />')" title="<u:msg titleId='or.jsp.viewUserPop.mailToPop' />"><u:convertMap srcId="wbBcBMap" attId="${wbBcUserLstSetupRVo.atrbId}" type="html" /></a>
												</c:when>
												<c:otherwise><u:convertMap srcId="wbBcBMap" attId="${wbBcUserLstSetupRVo.atrbId}" type="html" /></c:otherwise>
											</c:choose>
										</td>
									</tr>
								</c:if>
							</c:forEach>
						</table>
					</div>
				</div>
			</c:forEach>
		</c:when>
	</c:choose>
</div>
</c:otherwise>
</c:choose>
</form>
<u:blank />
<div style="padding:10px;"><u:pagination /></div>


