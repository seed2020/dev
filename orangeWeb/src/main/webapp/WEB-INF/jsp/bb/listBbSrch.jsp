<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%// 사용자,부서 선택 - 복합식 %>
function openCidPop(typ, id){
	if(typ == 'USER') openSingUser(id);
	else openSingOrg(id);
}<%// 1명의 사용자 선택 %>
function openSingUser(id){
	var data = [];<%// data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	if($('#'+id).val() != '') data.push({userUid:$('#'+id).val()});
	<%// option : data, multi, withSub, titleId %>
	searchUserPop({data:data}, function(userVo){
		if(userVo!=null){
			$('#'+id).val(userVo.userUid);
			$('#'+id+'Nm').val(userVo.rescNm);
		}
	});
};<%// 부서 선택 %>
function openSingOrg(id){
	var data = [];
	if($('#'+id).val() != '') data.push({orgId:$('#'+id).val()});
	searchOrgPop({data:data}, function(orgVo){
		if(orgVo!=null){
			$('#'+id).val(orgVo.orgId);
			$('#'+id+'Nm').val(orgVo.rescNm);
		}
	});
}
<% // 날짜 및 시간 변경시 히든값 변경 %>
function chnDateTime(obj){
	if(obj===undefined) return;
	var id=obj.myId===undefined ? $(obj).attr('id') : obj.myId;
	if(id===undefined) return;
	id=id.replace(/Dt|Tm/g, '');
	if($('#'+id+'Dt').val()!='')
		$('#'+id).val($('#'+id+'Dt').val() + ' ' + $('#'+id+'Tm').val() + ':00');
	else
		$('#'+id).val('');
}
//-->
</script>
<u:searchArea>
	<div id="searchArea1" style="<c:if test="${not empty param.srchDetl}">display:none;</c:if>">
	<form id="searchForm1" name="searchForm1" action="${_uri}" >
	<u:input type="hidden" id="menuId" value="${menuId}" /><c:if
	test="${not empty param.pageRowCnt}">
	<u:input type="hidden" id="pageRowCnt" value="${param.pageRowCnt}" /></c:if>
	<u:input type="hidden" id="compId" value="${param.compId}" />
	<u:input type="hidden" id="brdId" value="${param.brdId}" />
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
		<tr>
			<td>
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td><u:buttonIcon alt="검색 조건 펼치기" titleId="cm.ico.showCondi" image="ico_wdown.png" onclick="$('#searchArea1').toggle(); $('#searchArea2').toggle();" /></td>
						<c:if test="${!empty baseList.textOptList }">
						<td><select name="schCat" style="min-width:60px;">
							<c:forEach var="col" items="${baseList.textOptList }" varStatus="status">
							<u:option value="${col[0] }" title="${col[1] }" alt="${col[1] }" checkValue="${param.schCat}"/></c:forEach>
							</select>
						</td>
						<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 200px;" onkeydown="if (event.keyCode == 13) searchForm1.submit();" /></td>
						<td class="width20"></td>
						</c:if>
						<c:if test="${!empty baseList.dateOptList }">
						<td class="search_tit"><u:msg titleId="cols.prd" alt="기간" /></td>
						<td>
							<table border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td>
										<select name="durCat" style="min-width:70px;">
										<c:forEach var="col" items="${baseList.dateOptList }" varStatus="status">
										<u:option value="${col[0] }" title="${col[1] }" alt="${col[1] }" checkValue="${param.durCat}"/></c:forEach>
										</select>
									</td>
									<td><u:calendar id="durStrtDt" option="{end:'durEndDt'}" value="${param.durStrtDt}" /></td>
									<td class="search_body_ct"> ~ </td>
									<td><u:calendar id="durEndDt" option="{start:'durStrtDt'}" value="${param.durEndDt}" /></td>
								</tr>
							</table>
						</td>
						<td class="width20"></td>
						</c:if>
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
				</table>
			</td>
			<td><div class="button_search"><ul><li class="search"><a href="javascript:document.searchForm1.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
		</tr>
	</table>
	</form>
	</div>
	<div id="searchArea2" style="<c:if test="${empty param.srchDetl}">display:none;</c:if>">
	<form id="searchForm2" name="searchForm2" action="${_uri}" >
	<u:input type="hidden" id="menuId" value="${menuId}" /><c:if
	test="${not empty param.pageRowCnt}">
	<u:input type="hidden" id="pageRowCnt" value="${param.pageRowCnt}" /></c:if>
	<u:input type="hidden" id="srchDetl" value="Y" />
	<u:input type="hidden" id="compId" value="${param.compId}" />
	<u:input type="hidden" id="brdId" value="${param.brdId}" />
	<table id="searchFormTbl2" class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td>
		<div style="float:left; padding: 2px 10px 0px 0px;">
			<u:buttonIcon alt="검색 조건 숨기기" titleId="cm.ico.hideCondi" image="ico_wup.png" onclick="$('#searchArea1').toggle(); $('#searchArea2').toggle();" />
		</div>
		<table border="0" cellpadding="0" cellspacing="0">
		<c:set var="maxCnt" value="3"/>
		<c:set var="dispCnt" value="1"/>
		<c:set var="maxTrCnt" value="0"/>
		<c:set var="maxLen" value="${fn:length(dtlList) }"/>
		<c:forEach items="${dtlList}" var="dispVo" varStatus="colStatus">
			<c:set var="title" value="${dispVo[1]}" />
			<c:set var="colmNm" value="${dispVo[0]}" />
			<c:set var="colmTyp" value="${dispVo[2]}" />
			<c:if test="${dispCnt==1 || dispCnt%maxCnt == 1 }"><tr><c:set var="maxTrCnt" value="${maxTrCnt+1 }"/></c:if>
			<u:set var="colspan" test="${(dispCnt == 1 || dispCnt<maxCnt || dispCnt%maxCnt > 0) 
	&& ( fn:length(dtlList) == colStatus.count)}" value="colspan='${(((maxCnt*maxTrCnt)-dispCnt)*3)+1 }'" elseValue=""/>
			<td class="search_tit">${title}</td>
			<td ${colspan }>
				<u:convertMap var="paramValue" srcId="paramMap" attId="${colmNm}" type="html" />
				<c:if test="${colmTyp eq 'TEXT' }"><u:input id="${colmNm }" maxByte="50" value="${paramValue}" title="${title }" style="width: 150px;" onkeydown="if (event.keyCode == 13) searchForm2.submit();" /></c:if>
				<c:if test="${colmTyp == 'PHONE'}"><u:phone id="${colmNm}" title="${title }" value="${paramValue }"/></c:if>
				<c:if test="${colmTyp == 'CALENDAR'}"><u:calendar id="${colmNm}" title="${title }" value="${paramValue }"/></c:if>
				<c:if test="${colmTyp == 'CALENDARTIME'}"><table border="0" cellpadding="0" cellspacing="0"><tr>
				<td><u:out value="${paramValue}" type="date" var="paramDate" />
					<u:out value="${paramValue}" type="hm" var="paramHm" /><u:calendar id="${colmNm}Dt" title="${title }" option="{onchange:chnDateTime}" value="${paramDate }"/></td><td>
						<select id="${colmNm}Tm" onchange="chnDateTime({myId:'${colmNm}Tm'});">
						<c:forEach begin="0" end="23" step="1" var="hour" varStatus="status">
							<u:set test="${hour < 10}" var="hh" value="0${hour}" elseValue="${hour}" />
							<u:option value="${hh}:00" title="${hh}:00" checkValue="${paramHm}" />
							<u:option value="${hh}:30" title="${hh}:30" checkValue="${paramHm}" />
						</c:forEach>
						</select>
					</td></tr></table>
					<u:input type="hidden" id="${colmNm }" value="${paramValue }"/>
				</c:if>
				<c:if test="${fn:startsWith(colmTyp,'CODE')}">
					<u:set test="${cdListIndex == null}" var="cdListIndex" value="0" elseValue="${cdListIndex + 1}" />
					<select name="${colmNm}">
						<u:option value="" titleId="cm.btn.choice"/>
						<c:forEach items="${cdList[cdListIndex]}" var="cd" varStatus="status">
						<option value="${cd.cdId}" <c:if test="${cd.cdId == paramValue}">selected="selected"</c:if>>${cd.rescNm}</option>
						</c:forEach>
						</select>
				</c:if>
				<c:if test="${colmTyp == 'USER' || colmTyp == 'DEPT'}"><c:set var="atrbIdPrefix" value="${colmNm}"/>
						<u:convertMap var="valueUid" srcId="paramMap" attId="${atrbIdPrefix}" type="html" />
						<u:convertMap var="valueNm" srcId="paramMap" attId="${atrbIdPrefix}Nm" type="html" />
						<table border="0" cellpadding="0" cellspacing="0">
							<tbody>
							<tr>
								<td>
									<u:input type="hidden" id="${colmNm }" value="${valueUid}"/>
									<u:input id="${atrbIdPrefix }Nm" title="${title }" readonly="Y" value="${valueNm }"/>
								</td>
								<td><u:buttonS href="javascript:;" titleId="cm.btn.choice" alt="선택" onclick="openCidPop('${colmTyp }', '${colmNm }');" /></td>
							</tr>
							</tbody>
						</table></c:if>
			</td>
			<td class="width20"></td>
			<c:if test="${colStatus.count == fn:length(dtlList) }"></tr></c:if>
			<c:if test="${!empty colspan && colStatus.count < fn:length(dtlList) }"><c:set var="dispCnt" value="${dispCnt+(maxCnt-1) }"/></c:if>
			<c:set var="dispCnt" value="${dispCnt+1 }"/>
		</c:forEach>
		<tr>
			<td colspan="2"><u:buttonS href="javascript:;" onclick="valueReset('searchFormTbl2',['durCat']);;" titleId="cm.btn.srch.reset" alt="검색조건 초기화" /></td>
			<td class="width20"></td>			
		</tr>
	</table>
	</td>
	<td><div class="button_search"><ul><li class="search"><a href="javascript:document.searchForm2.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
	</div>
</u:searchArea>