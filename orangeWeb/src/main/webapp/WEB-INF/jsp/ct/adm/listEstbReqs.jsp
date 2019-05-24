<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

function ctInfoPoP(ctId){
	dialog.open('ctInfoPop', '<u:msg titleId="ct.cols.ctEstbInfo" alt="커뮤니티 개설정보" />','./ctInfoPop.do?menuId=${menuId}&ctId='+ctId);
}

function setRjtOp(ctId){
	dialog.open('setRjtOpPop', '<u:msg titleId="ct.cols.rjtOp" alt="반려사유" />','./setRjtOpPop.do?menuId=${menuId}&ctId='+ctId);
}

function setEstbReqsAppr(ctId, appr){
	var $tabNo = $("#tabNo").val(); 
	if(appr=='A'){
		if (confirmMsg("ct.cfrm.appr")) {
			callAjax('./setEstbReqsAppr.do?menuId=${menuId}', {ctId:ctId, appr:appr, tabNo:$tabNo}, function(data){
				if (data.message != null) {
					alert(data.message);
				}
				if (data.result == 'ok') {
					location.href = './listEstbReqs.do?menuId=${menuId}&tabNo=${showTab}';
				}
			});
		}
	}else{
		if (confirmMsg("ct.cfrm.close")) {
			callAjax('./setEstbReqsAppr.do?menuId=${menuId}', {ctId:ctId, appr:appr, tabNo:$tabNo}, function(data){
				if (data.message != null) {
					alert(data.message);
				}
				if (data.result == 'ok') {
					location.href = './listEstbReqs.do?menuId=${menuId}&tabNo=${showTab}';
				}
			});
		}
		
	}
}

function selectTab(tabNo){
	location.href = "./listEstbReqs.do?menuId=${menuId}&tabNo="+tabNo;
}

$(document).ready(function() {
	setUniformCSS();
	//달력셋팅
	$("#strtDt").val($("#startDt").val());
	$("#finDt").val($("#endDt").val());
	$("#reqsTab").css("z-index", "0");
});
//-->
</script>

<u:title titleId="ct.jsp.listEstbReqs.title" alt="커뮤니티 개설승인" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listEstbReqs.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="tabNo" name="tabNo" value="${showTab}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">
				<u:option value="communityOpt" titleId="ct.cols.ctNm" alt="커뮤니티" checkValue="${param.schCat}" />
				<u:option value="masterOpt" titleId="ct.cols.mastNm" alt="마스터" checkValue="${param.schCat}" />
			</select></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 400px;"/></td>
		<td class="width20"></td>
		
		<!-- 등록일시 -->
		<td class="search_tit"><u:msg titleId="cols.reqsDt" alt="신청일시" /></td>
		<td colspan="2"><table border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			<td><u:calendar id="strtDt" option="{end:'finDt'}" mandatory="Y" />
				<input name ="startDT" id="startDt" value="${startDt}" type="hidden"/></td>
			</td>
			<td class="body_lt">~</td>
			<td ><u:calendar id="finDt" option="{start:'strtDt'}" mandatory="Y"/>
				<input name ="endDT" id="endDt" value="${endDt}" type="hidden"/></td>
			</td>
			</tr>
			</tbody></table>
		</td>
		
		</tr>
		<tr>
<%-- 		<td class="search_tit"><u:msg titleId="cols.secul" alt="보안등급" /></td>
		<td><select id="extnOpenYn" name="extnOpenYn">
			<option value="" <c:if test="${extnOpenYn eq '' || extnOpenYn eq null}">selected = "selected" </c:if>> <u:msg titleId="cm.option.all" alt="전체" /> </option>
			<option value="Y" <c:if test="${extnOpenYn =='Y'}">selected = "selected" </c:if>><u:msg titleId="ct.cols.public" alt="공개" /></option>
			<option value="N" <c:if test="${extnOpenYn =='N'}">selected = "selected" </c:if>><u:msg titleId="ct.cols.nonPublic" alt="비공개" /></option>
			</select>
		</td> 
		<td class="width20"></td>	--%>
		<td class="search_tit"><u:msg titleId="cols.mngTgtYn" alt="관리대상여부" /></td>
		<td><select id="mngTgtYn" name="mngTgtYn">
			<option value="" <c:if test="${mngTgtYn eq '' || mngTgtYn eq null}">selected = "selected" </c:if>><u:msg titleId="cm.option.all" alt="전체" /></option>
			<option value="Y" <c:if test="${mngTgtYn == 'Y'}">selected = "selected" </c:if>>YES</option>
			<option value="N" <c:if test="${mngTgtYn =='N'}">selected = "selected" </c:if>>NO</option>
			</select>
		</td>
		</tr>
		
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<% // TAB %>
<u:tabGroup id="reqsTab">
	<u:set var="showReqsList" value="true" elseValue="false" test="${showTab == '0' || showTab == '' || showTab == null}" />
	<u:set var="showRjtList" value="true" elseValue="false" test="${showTab == '1'}" />
	<u:tab id="reqsTab" areaId="reqsList" titleId="ct.jsp.listEstbReqs.tab.reqsCm" alt="신청중 커뮤니티" on="${showReqsList}" onclick="javascript:selectTab('0');"/>
	<u:tab id="reqsTab" areaId="rjtList" titleId="ct.jsp.listEstbReqs.tab.rjtCm" alt="미승인 커뮤니티" on="${showRjtList}" onclick="javascript:selectTab('1');" />
</u:tabGroup>

<% // 신청중 커뮤니티 %>
<div id="reqsList" style="display: block;">

<u:listArea id="listArea">
	<tr>
	<td class="head_ct"><u:msg titleId="cols.cmNm" alt="커뮤니티명" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.mast" alt="마스터" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.reqsDt" alt="신청일시" /></td>
	<%-- <td width="6%" class="head_ct"><u:msg titleId="cols.secul" alt="보안등급" /></td> --%>
	<td width="10%" class="head_ct"><u:msg titleId="cols.mngTgtYn" alt="관리대상여부" /></td>
	<td width="6%" class="head_ct"><u:msg titleId="cols.att" alt="첨부" /></td>
	<td width="15%" class="head_ct"><u:msg titleId="cols.apvdYn" alt="승인여부" /></td>
	</tr>
	
	<c:forEach var="reqsCtVo" items="${reqsCtMapList}" varStatus="status">
		<tr>
			<td style="padding: 2px 5px 2px 5px;">
				<div class="ellipsis" >
				<table border="0" cellpadding="0" cellspacing="0" style="width:100%;table-layout:fixed;">
					<tbody>
						<tr>
						<td class="body_lt"><strong><a href="javascript:ctInfoPoP('${reqsCtVo.ctId}')"><u:out value="${reqsCtVo.ctNm}"/></strong>
							<input id="reqsCtId" name="reqsCtId" type="hidden" value="${reqsCtVo.ctId}"/>
						</td>
						</tr>
						<tr>
							<td>&nbsp; [<u:msg titleId="cols.cat" alt="카테고리" />] ${reqsCtVo.catPnm} &gt; ${reqsCtVo.catNm}</td>
						</tr>
						<tr>
							<td>
								<div class="ellipsis" title="${reqsCtVo.ctItro}">
									&nbsp; [<u:msg titleId="cols.estbItnt" alt="개설취지" />] ${reqsCtVo.ctItro}
								</div>
							</td>
						</tr>
						<c:if test="${showTab == '1'}">
							<tr>
								<td>
									<div class="ellipsis" title="${reqsCtVo.rjtOpinCont}">
									 	&nbsp; [<u:msg titleId="ct.cols.rjtOp" alt="반려사유" />] ${reqsCtVo.rjtOpinCont}
									</div>
								</td>
							</tr>
						</c:if>
					</tbody>
				</table>
				</div>
			</td>
			<td class="body_ct"><a href="javascript:viewUserPop('${reqsCtVo.mastUid}');">${reqsCtVo.mastNm}</a></td>
			<td class="body_ct">
				<fmt:parseDate var="dateTempParse" value="${reqsCtVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
			</td>
			<%-- <td class="body_ct">
				<c:if test="${reqsCtVo.extnOpenYn == 'Y'}">
					<u:msg titleId="ct.cols.public" alt="공개" />
				</c:if>
				<c:if test="${reqsCtVo.extnOpenYn == 'N'}">
					<u:msg titleId="ct.cols.nonPublic" alt="비공개" />
				</c:if>
			</td> --%>
			<td class="body_ct">
				<c:if test="${reqsCtVo.mngTgtYn == 'Y'}">
					<u:msg titleId="ct.cols.yes" alt="Yes" />
				</c:if>
				<c:if test="${reqsCtVo.mngTgtYn == 'N'}">
					<u:msg titleId="ct.cols.no" alt="No" />
				</c:if>
			</td>
			<td class="body_ct">
				<c:if test="${reqsCtVo.attYn == 'Y'}">
					<u:icon type="att" />
				</c:if>
				<c:if test="${reqsCtVo.attYn == 'N'}">
				</c:if>
			</td>
			<c:if test="${showTab == '0' || showTab == null || showTab == ''}">
				<td>
					<table align="center" border="0" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
								<td><u:buttonS href="" titleId="cm.btn.apvd" alt="승인" onclick="javascript:setEstbReqsAppr('${reqsCtVo.ctId}', 'A');"/></td>
								<td><u:buttonS href="" titleId="cm.btn.napvd" alt="미승인" onclick="javascript:setRjtOp('${reqsCtVo.ctId}');"/></td>
							</tr>
						</tbody>
					</table>
				</td>
			</c:if>
			<c:if test="${showTab == '1'}">
				<td>
					<table align="center" border="0" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
								<td><u:buttonS href="" titleId="cm.btn.apvd" alt="승인" onclick="javascript:setEstbReqsAppr('${reqsCtVo.ctId}', 'A')"/></td>
								<td><u:buttonS href="" titleId="ct.btn.cmClose" alt="커뮤니티 폐쇄" onclick="javascript:setEstbReqsAppr('${reqsCtVo.ctId}', 'C')"/></td>
							</tr>
						</tbody>
					</table>
				</td>
			</c:if>
		</tr>
	</c:forEach>
	<c:if test="${fn:length(reqsCtMapList) == 0 }">
		<tr>
			<td class="nodata" colspan="7"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
</u:listArea>

<u:pagination />

</div>

<% // 미승인 커뮤니티 %>
<div id="rjtList" style="display: none;">

<u:listArea id="listArea">
	<tr>
	<td class="head_ct"><u:msg titleId="cols.cmNm" alt="커뮤니티명" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.mast" alt="마스터" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.reqsDt" alt="신청일시" /></td>
	<%-- <td width="6%" class="head_ct"><u:msg titleId="cols.secul" alt="보안등급" /></td> --%>
	<td width="10%" class="head_ct"><u:msg titleId="cols.mngTgtYn" alt="관리대상여부" /></td>
	<td width="6%" class="head_ct"><u:msg titleId="cols.att" alt="첨부" /></td>
	<td width="15%" class="head_ct"><u:msg titleId="cols.apvdYn" alt="승인여부" /></td>
	</tr>
	<c:forEach var="reqsCtVo" items="${reqsCtMapList}" varStatus="status">
		<tr>
			<td style="padding: 2px 5px 2px 5px;">
				<div class="ellipsis" >
				<table border="0" cellpadding="0" cellspacing="0" style="width:100%;table-layout:fixed;">
					<tbody>
						<tr>
						<td class="body_lt"><strong><u:out value="${reqsCtVo.ctNm}"/></strong></td>
						</tr>
						<tr>
							<td>&nbsp; [<u:msg titleId="cols.cat" alt="카테고리" />] ${reqsCtVo.catPnm} &gt; ${reqsCtVo.catNm}</td>
						</tr>
						<tr>
							<td>
								<div class="ellipsis" title="${reqsCtVo.ctItro}">
									&nbsp; [<u:msg titleId="cols.estbItnt" alt="개설취지" />] ${reqsCtVo.ctItro}
								</div>
							</td>
						</tr>
						<c:if test="${showTab == '1'}">
							<tr>
								<td>
									<div class="ellipsis" title="${reqsCtVo.rjtOpinCont}">
									 	&nbsp; [<u:msg titleId="ct.cols.rjtOp" alt="반려사유" />] ${reqsCtVo.rjtOpinCont}
									</div>
								</td>
							</tr>
						
						</c:if>
						
					</tbody>
				</table>
				</div>
			</td>
			<td class="body_ct"><a href="javascript:viewUserPop('${reqsCtVo.mastUid}');">${reqsCtVo.mastNm}</a></td>
			<td class="body_ct">
				<fmt:parseDate var="dateTempParse" value="${reqsCtVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
			</td>
			<%-- <td class="body_ct">
				<c:if test="${reqsCtVo.extnOpenYn == 'Y'}">
					<u:msg titleId="ct.cols.open" alt="공개" />
				</c:if>
				<c:if test="${reqsCtVo.extnOpenYn == 'N'}">
					<u:msg titleId="ct.cols.notOpen" alt="비공개" />
				</c:if>
			</td> --%>
			<td class="body_ct">
				<c:if test="${reqsCtVo.mngTgtYn == 'Y'}">
					<u:msg titleId="ct.cols.yes" alt="Yes" />
				</c:if>
				<c:if test="${reqsCtVo.mngTgtYn == 'N'}">
					<u:msg titleId="ct.cols.no" alt="No" />
				</c:if>
			</td>
			<td class="body_ct">
				<c:if test="${reqsCtVo.attYn == 'Y'}">
					<u:icon type="att" />
				</c:if>
				<c:if test="${reqsCtVo.attYn == 'N'}">
				</c:if>
			</td>
			<c:if test="${showTab == '0' || showTab == null || showTab == ''}">
				<td>
					<table align="center" border="0" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
								<td><u:buttonS href="" titleId="cm.btn.apvd" alt="승인" onclick="javascript:setEstbReqsAppr('${reqsCtVo.ctId}', 'A');"/></td>
								<td><u:buttonS href="" titleId="cm.btn.napvd" alt="미승인" onclick="javascript:setRjtOp('${reqsCtVo.ctId}');"/></td>
							</tr>
						</tbody>
					</table>
				</td>
			</c:if>
			<c:if test="${showTab == '1'}">
				<td>
					<table align="center" border="0" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
								<td><u:buttonS href="" titleId="cm.btn.apvd" alt="승인" onclick="javascript:setEstbReqsAppr('${reqsCtVo.ctId}', 'A');"/></td>
								<td><u:buttonS href="" titleId="ct.btn.cmClose" alt="커뮤니티 폐쇄" onclick="javascript:setEstbReqsAppr('${reqsCtVo.ctId}', 'C');"/></td>
							</tr>
						</tbody>
					</table>
				</td>
			</c:if>
		</tr>
	</c:forEach>
	<c:if test="${fn:length(reqsCtMapList) == 0 }">
		<tr>
			<td class="nodata" colspan="7"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
	
</u:listArea>

<u:pagination />

</div>

