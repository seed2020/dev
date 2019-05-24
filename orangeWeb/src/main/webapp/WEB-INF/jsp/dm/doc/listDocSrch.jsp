<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="paramStorIdQueryString" test="${!empty paramStorId }" value="&paramStorId=${paramStorId }" elseValue=""/><!-- 저장소ID -->
<script type="text/javascript">
<!--<%// 복합식 value 삭제%>
function delCidVa(id){
	$('#'+id+'Id').val('');
	$('#'+id+'Nm').val('');
}<%// 인계자 %>
function takOrgSet(arrs){
	$('#takOrgId').val(arrs[0].compId);
	$('#takOrgNm').val(arrs[0].compNm);
	dialog.close('findCompDialog');
}<%// 인수자 %>
function tgtOrgSet(arrs){
	$('#tgtOrgId').val(arrs[0].compId);
	$('#tgtOrgNm').val(arrs[0].compNm);
	dialog.close('findCompDialog');
}<%// 조직,회사 선택 %>
function openCompPop(atrb){
	<c:if test="${empty isAdmin || isAdmin == false }">openSingOrg(atrb);</c:if>
	<c:if test="${!empty isAdmin && isAdmin == true }">dialog.open('findCompDialog', '<u:msg titleId="dm.cols.doc.target.select" alt="대상선택" />', './findCompPop.do?menuId=${menuId}&callback='+atrb+'Set');</c:if>
}<%// 사용자,부서 선택 - 복합식 %>
function openCidPop(atrb){
	var catVa = $("select[name='"+atrb+"Cat'] > option:selected").eq(0).val();
	if(catVa == 'user') openSingUser(atrb,"Id");
	else openSingOrg(atrb);
}<%// 부서 선택 %>
function openSingOrg(id){
	var data = [];
	searchOrgPop({data:data}, function(orgVo){
		if(orgVo!=null){
			$('#'+id+'Id').val(orgVo.orgId);
			$('#'+id+'Nm').val(orgVo.rescNm);
		}
	});
}<%// 분류,폴더 Prefix %>
function getTabPrefix(lstTyp){
	var prefix = "fld";
	if(lstTyp == 'C') prefix = "cls";
	return prefix;
}<%// [버튼] 분류,폴더 %>
function findSelPop(lstTyp){
	var prefix = getTabPrefix(lstTyp);
	var $area = $("#"+prefix+"InfoArea"), data = [];
	$area.find("input[id='"+prefix+"Id']").each(function(){
		data.push($(this).val());
	});
	var url = './findSelPop.do?menuId=${menuId}${paramStorIdQueryString}&lstTyp='+lstTyp+"&selIds="+data+"&fncMul="+(lstTyp == 'C' ? 'Y':'N');
	var msgTitle = lstTyp == 'C' ? '<u:msg titleId="dm.cols.listTyp.cls" alt="분류보기" />' : '<u:msg titleId="dm.cols.listTyp.fld" alt="폴더보기" />';
	dialog.open('findSelPop', msgTitle, url);
};<%// 분류,폴더 적용%>
function setSelInfos(arr, lstTyp){
	var prefix = getTabPrefix(lstTyp);
	$area = $('#'+prefix+'InfoArea');
	
	var buffer = [];
	var nms = '';
	arr.each(function(index, obj){
		buffer.push("<input type='hidden' id='"+prefix+"Id' name='"+prefix+"Id' value='"+obj.id+"'/>\n");
		nms+= nms == '' ? obj.nm : ','+obj.nm;
	});
	$area.find('#idArea').html('');
	$area.find('#idArea').html(buffer.join(''));
	$area.find('#nmArea input[id="'+prefix+'Nm"]').val(nms);
	dialog.close('findSelPop');
	//setPageChk(prefix);
}<%// 1명의 사용자 선택 %>
function openSingUser(id, suffix){
	if(suffix == undefined) suffix = "Uid";
	var data = [];<%// data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	if($('#'+id+suffix).val() != '') data.push({userUid:$('#'+id+suffix).val()});
	<%// option : data, multi, withSub, titleId %>
	searchUserPop({data:data}, function(userVo){
		if(userVo!=null){
			$('#'+id+suffix).val(userVo.userUid);
			$('#'+id+'Nm').val(userVo.rescNm);
		}
	});
};<%// 저장소 선택 %>
function srchStor(obj){
	var $form;
	<c:if test="${empty param.srchDetl}">$form = $('#searchForm1');</c:if>
	<c:if test="${!empty param.srchDetl}">$form = $('#searchForm2');</c:if>
	$form.find("input[name='paramStorId']").remove();
	$form.appendHidden({name:'paramStorId',value:$(obj).val()});
	$form.submit();
};<%// 회사 선택 %>
function srchComp(obj){
	var $form;
	<c:if test="${empty param.srchDetl}">$form = $('#searchForm1');</c:if>
	<c:if test="${!empty param.srchDetl}">$form = $('#searchForm2');</c:if>
	$form.find("input[name='paramCompId']").remove();
	$form.appendHidden({name:'paramCompId',value:$(obj).val()});
	$form.submit();
};
$(document).ready(function() {
	$('#searchArea2').find('input[type="text"]').keyup(function(event){
		if(event.keyCode == 13) document.searchForm2.submit();
	});
});
//-->
</script>
<c:if test="${!empty storList }">
<u:set var="paramStorId" test="${!empty paramStorId }" value="${paramStorId }" elseValue="${param.paramStorId }"/>
<div class="front notPrint">
	<div class="front_left">		
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td style="padding:3px 4px 0 0px"><u:title titleId="dm.jsp.search.doc.title" alt="문서조회" menuNameFirst="true" /></td>
			<td class="width5"></td>
				<td class="frontinput">
					<u:msg var="storDftTitle" titleId="cm.option.dft" alt="기본" />
					<select id="paramStorId" name="paramStorId" <u:elemTitle titleId="dm.jsp.stor.title" alt="저장소" /> onchange="srchStor(this);">
						<c:forEach items="${storList}" var="storVo" varStatus="status">
							<u:set var="storSubTitle" test="${storVo.dftYn eq 'Y' }" value="(${storDftTitle })" elseValue=""/>
							<u:option value="${storVo.storId}" title="${storVo.storNm}${storSubTitle }" checkValue="${paramStorId }"/>
						</c:forEach>
					</select>
				</td>
	 		</tr>
		</table>
	</div>
</div>
</c:if>
<c:if test="${!empty ptCompBVoList }">
<u:set var="paramCompId" test="${!empty paramCompId }" value="${paramCompId }" elseValue="${param.paramCompId }"/>
<div class="front notPrint">
	<div class="front_left">		
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td style="padding:3px 4px 0 0px"><u:title titleId="cols.comp" alt="회사" menuNameFirst="true" /></td>
			<td class="width5"></td>
				<td class="frontinput">
					<select id="compId" name="compId" <u:elemTitle titleId="cols.comp" /> onchange="srchComp(this);">
						<c:forEach items="${ptCompBVoList}" var="ptCompBVo" varStatus="status">
							<u:option value="${ptCompBVo.compId}" title="${ptCompBVo.rescNm}" checkValue="${paramCompId }"/>
						</c:forEach>
					</select>
				</td>
	 		</tr>
		</table>
	</div>
</div>
</c:if>
<% // 검색영역 %>
<u:searchArea style="position:relative; z-index:2;">
	<div id="searchArea1" style="<c:if test="${not empty param.srchDetl}">display:none;</c:if>">
	<form id="searchForm1" name="searchForm1" action="${_uri}" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="lstTyp" value="${lstTyp}" /><c:if
		test="${not empty param.pageRowCnt}">
	<u:input type="hidden" id="pageRowCnt" value="${param.pageRowCnt}" /></c:if><c:if test="${!empty paramStorId }"
	><u:input type="hidden" id="paramStorId" value="${paramStorId}" /></c:if>
	<c:if test="${!empty param.fromStorId }"><u:input type="hidden" id="fromStorId" value="${param.fromStorId}" /></c:if>
	<c:if test="${!empty param.tgtStorId }"><u:input type="hidden" id="tgtStorId" value="${param.tgtStorId}" /></c:if>
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><u:buttonIcon alt="검색 조건 펼치기" titleId="cm.ico.showCondi" image="ico_wdown.png" onclick="$('#searchArea1').toggle(); $('#searchArea2').toggle();" /></td>
		<td><c:if test="${!empty param.subj }"><c:set var="schCat" value="subj"/></c:if
		><c:if test="${empty schCat && !empty param.regrNm }"><c:set var="schCat" value="regrNm"/></c:if
		><c:if test="${empty schCat && !empty param.kwdNm }"><c:set var="schCat" value="kwdNm"/></c:if
		><c:if test="${empty schCat && !empty param.ownrNm }"><c:set var="schCat" value="ownrNm"/></c:if
		><select name="schCat">
		<u:option value="subj" titleId="cols.subj" alt="제목" checkValue="${!empty schCat ? schCat : param.schCat}" selected="${empty schCat }"/>
		<u:option value="cont" titleId="cols.cont" alt="내용" checkValue="${!empty schCat ? schCat : param.schCat}" />
		<u:option value="regrNm" titleId="cols.regr" alt="등록자" checkValue="${!empty schCat ? schCat : param.schCat}" />
		<u:option value="kwdNm" titleId="dm.cols.kwd" alt="키워드" checkValue="${!empty schCat ? schCat : param.schCat}" />
		<u:option value="ownrNm" titleId="dm.cols.ownr" alt="소유자" checkValue="${!empty schCat ? schCat : param.schCat}" />
		</select></td>
		<td><c:if test="${!empty param.subj }"><c:set var="schWord" value="${param.subj }"/></c:if
		><c:if test="${empty schWord && !empty param.regrNm }"><c:set var="schWord" value="${param.regrNm }"/></c:if
		><c:if test="${empty schWord && !empty param.kwdNm }"><c:set var="schWord" value="${param.kwdNm }"/></c:if
		><c:if test="${empty schWord && !empty param.ownrNm }"><c:set var="schWord" value="${param.ownrNm }"/></c:if
		><c:if test="${empty schWord}"><c:set var="schWord" value="${param.schWord }"/></c:if
		><u:input id="schWord" maxByte="50" value="${schWord}" titleId="cols.schWord" style="width: 200px;" onkeydown="if (event.keyCode == 13) searchForm1.submit();" /></td>
		<td class="width20"></td>
		<!-- 등록일시 -->
		<td class="search_tit"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
		<td><u:input type="hidden" id="durCat" name="durCat" value="regDt"/>
			<c:set var="srchYmdMap" value="${srchYmdMap }" scope="request"/>
			<u:convertMap var="srchYmdMap1" srcId="srchYmdMap" attId="regDt" />
			<u:convertMap var="strtValue1" srcId="srchYmdMap1" attId="durStrtDt" />
			<u:convertMap var="endValue1" srcId="srchYmdMap1" attId="durEndDt" />
			<table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td><u:calendar id="durStrtDt1" name="durStrtDt" option="{end:'durEndDt1'}" titleId="cols.strtYmd" value="${strtValue1}" /></td>
			<td class="search_body_ct"> ~ </td>
			<td><u:calendar id="durEndDt1" name="durEndDt" option="{start:'durStrtDt1'}" titleId="cols.endYmd" value="${endValue1}" /></td>
			</tr>
			</table></td>
		<td class="width20"><u:input type="hidden" id="durCat" name="durCat" value="regDt"/></td>
		</tr>
		</table>
	</td>
	<td><div class="button_search"><ul><li class="search"><a href="javascript:document.searchForm1.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form></div>
	<c:set var="paramMap" value="${param }" scope="request"/>
	<c:set var="initSrchMap" value="${initSrchMap }" scope="request"/>
	<div id="searchArea2" style="<c:if test="${empty param.srchDetl}">display:none;</c:if>">
	<c:set var="srchYmdMap" value="${srchYmdMap }" scope="request"/>
	<form id="searchForm2" name="searchForm2" action="${_uri}" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="lstTyp" value="${lstTyp}" /><c:if
		test="${not empty param.pageRowCnt}">
	<u:input type="hidden" id="pageRowCnt" value="${param.pageRowCnt}" /></c:if><c:if test="${!empty paramStorId }"
	><u:input type="hidden" id="paramStorId" value="${paramStorId}" /></c:if>
	<c:if test="${!empty param.fromStorId }"><u:input type="hidden" id="fromStorId" value="${param.fromStorId}" /></c:if>
	<c:if test="${!empty param.tgtStorId }"><u:input type="hidden" id="tgtStorId" value="${param.tgtStorId}" /></c:if>
	<u:input type="hidden" id="srchDetl" value="Y" />
	<table id="searchFormTbl2" class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td>
		<div style="float:left; padding: 2px 10px 0px 0px;">
			<u:buttonIcon alt="검색 조건 숨기기" titleId="cm.ico.hideCondi" image="ico_wup.png" onclick="$('#searchArea1').toggle(); $('#searchArea2').toggle();" />
		</div>
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<c:set var="maxDispCnt" value="2"/><!-- 1줄에 표시할 컬럼 갯수 -->
		<c:set var="uidAtrbs" value="regr,modr,ownr,reqUser"/><!-- 사용자 비교 컬럼명 -->
		<c:set var="cdAtrbs" value="viewReqStatCd"/><!-- 코드에서 '전체'가 없어야 되는 컬럼명 -->
		<c:set var="dispCnt" value="1"/>
		<c:forEach var="dispVo" items="${srchDispList }" varStatus="status">
			<c:set var="colmVo" value="${dispVo.colmVo}" />
			<c:set var="itemTyp" value="${colmVo.itemTyp}" />
			<c:set var="atrbId" value="${dispVo.atrbId}" />
			<u:convertMap var="docVal" srcId="paramMap" attId="${atrbId }" />
			<c:if test="${dispCnt > 1 && dispCnt%maxDispCnt == 1}"></tr><tr></c:if>
			<td class="search_tit">${colmVo.itemDispNm }</td>
			<td>
				<c:choose>
					<c:when test="${itemTyp eq 'FLD' }">
						<div id="fldInfoArea" style="display:inline;">
							<div id="idArea" style="display:none;"><u:input type="hidden" id="fldId" value="${param.fldId }"/></div>
							<div id="nmArea" style="display:inline;"><u:input id="fldNm" titleId="cols.fld" value="${param.fldNm}" mandatory="Y" style="width:55%;" readonly="Y"/></div>
						</div>
						<u:buttonS titleId="dm.btn.fldSel" alt="폴더 선택" onclick="findSelPop('F');" />
					</c:when>
					<c:when test="${itemTyp eq 'CLS' }">
						<c:set var="clsNmTmp" />
						<div id="clsInfoArea" style="display:inline;">
							<div id="idArea" style="display:none;">
								<c:forEach var="clsVo" items="${dmClsBVoList }" varStatus="status">
									<u:input type="hidden" id="clsId" value="${clsVo.clsId }"/>
									<c:set var="clsNmTmp" value="${clsNmTmp}${status.count > 1 ? ',' : '' }${clsVo.clsNm }"/>
								</c:forEach>
							</div>
							<div id="nmArea" style="display:inline;"><u:input id="clsNm" titleId="cols.cls" value="${clsNmTmp}" mandatory="Y" style="width:55%;" readonly="Y"/></div>
						</div>
						<u:buttonS titleId="dm.btn.clsSel" alt="분류 선택" onclick="findSelPop('C');" />
					</c:when>
					<c:when test="${itemTyp eq 'TEXT' || itemTyp eq 'TEXTAREA' }">
						<u:set var="docVal" test="${param.schCat eq atrbId && !empty param.schWord }" value="${param.schWord}" elseValue="${docVal }"/>
						<u:input id="${atrbId}" value="${docVal}" title="${colmVo.itemDispNm }" style="width: 98%;" />
					</c:when>
					<c:when test="${itemTyp eq 'PHONE' }"><u:phone id="${atrbId}" value="${docVal}" title="${colmVo.itemDispNm }" /></c:when>
					<c:when test="${itemTyp eq 'CODE' || itemTyp eq 'CD'}"><u:convertMap var="value" srcId="paramMap" attId="${fn:replace(atrbId,'Nm','Cd')}" type="html" />
					<c:if test="${empty value }"><u:convertMap var="value" srcId="initSrchMap" attId="${fn:replace(atrbId,'Nm','Cd')}" type="html" /></c:if>
					<select name="${fn:replace(atrbId,'Nm','Cd')}">
						<c:if test="${(isAdmin == false && fn:contains(cdAtrbs,fn:replace(atrbId,'Nm','Cd'))) || !fn:contains(cdAtrbs,fn:replace(atrbId,'Nm','Cd')) }"><option value="" <c:if test="${empty value}">selected="selected"</c:if>/><u:msg titleId="cm.option.all" alt="전체선택"/></c:if>
						<c:forEach items="${colmVo.cdList}" var="cd" varStatus="status">
							<option value="${cd.cdId}" <c:if test="${cd.cdId == value}">selected="selected"</c:if>>${cd.rescNm}</option>
						</c:forEach>
					</select></c:when>
					<c:when test="${itemTyp eq 'CID' && !empty colmVo.itemTypVa}">
						<c:set var="atrbIdPrefix" value="${fn:substring(dispVo.atrbId,0,fn:length(dispVo.atrbId)-2)}"/>
						<u:convertMap var="valueId" srcId="paramMap" attId="${atrbIdPrefix}Id" type="html" />
						<u:convertMap var="valueNm" srcId="paramMap" attId="${atrbIdPrefix}Nm" type="html" />
						<u:convertMap var="cidCat" srcId="paramMap" attId="${atrbIdPrefix}Cat" type="html" />
						<table border="0" cellpadding="0" cellspacing="0">
							<tbody>
							<tr>
								<td><select name="${atrbIdPrefix }Cat" onchange="delCidVa('${atrbIdPrefix }');">
									<c:forTokens var="cidVa" items="${colmVo.itemTypVa }" delims=",">
										<u:option value="${cidVa }" titleId="cols.${cidVa eq 'org' ? 'dept' : cidVa}" alt="사용자,부서" selected="${cidVa eq cidCat }"/>
									</c:forTokens>
								</select>
								</td>
								<td>
									<u:input type="hidden" id="${atrbIdPrefix }Id" value="${valueId}"/>
									<u:input id="${atrbIdPrefix}Nm" titleId="cols.user" readonly="Y" mandatory="Y" value="${valueNm }"/>							
								</td>
								<td><u:buttonS href="javascript:;" titleId="cm.btn.choice" alt="선택" onclick="openCidPop('${atrbIdPrefix}');" /></td>
							</tr>
							</tbody>
						</table>
					</c:when>
					<c:when test="${itemTyp eq 'ID' && colmVo.itemTypVa eq 'ORG_ID'}">
						<c:set var="atrbIdPrefix" value="${fn:substring(dispVo.atrbId,0,fn:length(dispVo.atrbId)-2)}"/>
						<u:convertMap var="valueId" srcId="paramMap" attId="${atrbIdPrefix}Id" type="html" />
						<u:convertMap var="valueNm" srcId="paramMap" attId="${atrbIdPrefix}Nm" type="html" />
						<table border="0" cellpadding="0" cellspacing="0">
							<tbody>
							<tr>
								<td>
									<u:input type="hidden" id="${atrbIdPrefix}Id" value="${valueId}"/>
									<u:input id="${atrbIdPrefix}Nm" title="${colmVo.itemDispNm }" readonly="Y" mandatory="Y" value="${valueNm }"/>							
								</td>
								<u:set var="openIdPop" test="${dispVo.atrbId eq 'tgtOrgNm' || dispVo.atrbId eq 'takOrgNm' }" value="openCompPop" elseValue="openSingOrg"/>
								<td><u:buttonS href="javascript:;" titleId="cm.btn.choice" alt="선택" onclick="${openIdPop }('${atrbIdPrefix}');" /></td>
							</tr>
							</tbody>
						</table>
					</c:when>
					<c:when test="${itemTyp eq 'UID' && fn:contains(uidAtrbs,fn:substring(atrbId,0,fn:length(dispVo.atrbId)-2)) }">
						<c:set var="atrbIdPrefix" value="${fn:substring(atrbId,0,fn:length(dispVo.atrbId)-2)}"/>
						<u:convertMap var="valueUid" srcId="paramMap" attId="${atrbIdPrefix}Uid" type="html" />
						<u:convertMap var="valueNm" srcId="paramMap" attId="${atrbIdPrefix}Nm" type="html" />
						<table border="0" cellpadding="0" cellspacing="0">
							<tbody>
							<tr>
								<td>
									<u:input type="hidden" id="${atrbIdPrefix }Uid" value="${valueUid}"/>
									<u:input id="${atrbIdPrefix }Nm" title="${colmVo.itemDispNm }" readonly="Y" mandatory="Y" value="${valueNm }"/>							
								</td>
								<td><u:buttonS href="javascript:;" titleId="cm.btn.choice" alt="선택" onclick="openSingUser('${atrbIdPrefix }');" /></td>
							</tr>
							</tbody>
						</table>
					</c:when>
					<c:when test="${fn:endsWith(dispVo.atrbId,'Dt') || dispVo.colmVo.dataTyp eq 'DATETIME' || dispVo.colmVo.itemTyp eq 'CALENDAR' }">
						<c:choose>
							<c:when test="${!empty colmVo.itemTypVa && colmVo.itemTypVa eq 'MERGE' }">
								<c:set var="atrbIdPrefix" value="${fn:substring(atrbId,0,fn:length(dispVo.atrbId)-2)}"/>
								<u:convertMap var="strtDtValue" srcId="paramMap" attId="${atrbIdPrefix }StrtDt" type="html" />
								<u:convertMap var="endDtValue" srcId="paramMap" attId="${atrbIdPrefix }EndDt" type="html" />
								<table border="0" cellpadding="0" cellspacing="0">
								<tr>
								<td><u:calendar id="${atrbIdPrefix }StrtDt" name="${atrbIdPrefix }StrtDt" option="{end:'${atrbIdPrefix }EndDt'}" title="${colmVo.itemDispNm }" value="${strtDtValue}" /></td>
								<td class="search_body_ct"> ~ </td>
								<td><u:calendar id="${atrbIdPrefix }EndDt" name="${atrbIdPrefix }EndDt" option="{start:'${atrbIdPrefix }StrtDt'}" title="${colmVo.itemDispNm }" value="${endDtValue}" /></td>
								</tr>
								</table>
							</c:when>
							<c:otherwise>
								<u:input type="hidden" id="durCat" name="durCat" value="${dispVo.atrbId }"/>	
								<u:convertMap var="srchYmdMap${status.count }" srcId="srchYmdMap" attId="${dispVo.atrbId }" />
								<u:convertMap var="strtValue" srcId="srchYmdMap${status.count }" attId="durStrtDt" />
								<u:convertMap var="endValue" srcId="srchYmdMap${status.count }" attId="durEndDt" />
								<table border="0" cellpadding="0" cellspacing="0">
								<tr>
								<td><u:calendar id="durStrtDt${dispVo.atrbId }" name="durStrtDt" option="{end:'durEndDt${dispVo.atrbId }'}" title="${colmVo.itemDispNm }" value="${strtValue}" /></td>
								<td class="search_body_ct"> ~ </td>
								<td><u:calendar id="durEndDt${dispVo.atrbId }" name="durEndDt" option="{start:'durStrtDt${dispVo.atrbId }'}" title="${colmVo.itemDispNm }" value="${endValue}" /></td>
								</tr>
								</table>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise><u:input id="${atrbId}" value="${docVal}" title="${colmVo.itemDispNm }" style="width: 98%;" /></c:otherwise>
				</c:choose>
			</td>
			<td class="width20"></td>
			<c:if test="${(dispCnt == 1 || dispCnt%maxDispCnt == 1) && fn:length(srchDispList) == status.count }"></tr><tr><c:set var="dispCnt" value="0"/></c:if>
			<c:set var="dispCnt" value="${dispCnt+1 }"/>
		</c:forEach>
		</tr>
		<!-- 임시저장함,휴지통은 추가항목 제외 -->
		<c:if test="${!fn:endsWith(path,'TmpDoc') && !fn:endsWith(path,'RecycleDoc') }">
		<!-- 기본 항목 이외 검색 추가 항목 -->
		<tr>
			<td class="search_tit"><u:msg titleId="dm.cols.withSubYn" alt="하위문서포함" /></td>
			<td><u:checkArea>
				<u:radio name="withSubYn" value="Y" titleId="cm.option.yes" checkValue="${param.withSubYn }" checked="${empty param.withSubYn }"/>
				<u:radio name="withSubYn" value="N" titleId="cm.option.no" checkValue="${param.withSubYn }"/>
			</u:checkArea></td>
			<td class="width20"></td>
			<c:if test="${empty orderSubYn }">
			<td class="search_tit"><u:msg titleId="dm.cols.orderSubYn" alt="하위문서정렬여부" /></td>
			<td><u:checkArea>
				<u:radio name="orderSubYn" value="Y" titleId="cm.option.yes" checkValue="${param.orderSubYn }" checked="${empty param.orderSubYn }"/>
				<u:radio name="orderSubYn" value="N" titleId="cm.option.no" checkValue="${param.orderSubYn }" />
			</u:checkArea></td>
			<td class="width20"></td>
			</c:if>
		</tr>
		<u:set var="kwdNm" test="${!empty param.schCat && param.schCat eq 'kwdNm' && !empty param.schWord}" value="${param.schWord }" elseValue="${param.kwdNm }"/>
		<tr>
			<td class="search_tit"><u:msg titleId="dm.cols.kwd" alt="키워드" /></td>
			<td><u:input id="kwdNm" value="${kwdNm}" titleId="dm.cols.kwd" style="width: 98%;" /></td>
			<td class="width20"></td>
		</tr>
		</c:if>
		<%-- <c:if test="${fn:startsWith(path,'listSubm') || fn:startsWith(path,'listDisc')}">
		<tr>
			<td class="search_tit"><u:msg titleId="cols.docStat" alt="문서상태" /></td>
			<td><select id="statCd" name="statCd">
		<u:option value="" titleId="cm.option.all" alt="전체선택" checkValue="${param.statCd}" />
		<u:option value="S" titleId="dm.cols.docStatS" alt="대기" checkValue="${param.statCd}" selected="${param.statCd == null }"/>
		<u:option value="C" titleId="dm.cols.docStatA" alt="승인" checkValue="${param.statCd}" />
		<u:option value="R" titleId="dm.cols.docStatR" alt="반려" checkValue="${param.statCd}" />
		</select></td>
		</tr>
		</c:if> --%>
		<tr>
			<td colspan="2"><u:buttonS href="javascript:;" onclick="valueReset('searchFormTbl2',['durCat']);" titleId="cm.btn.srch.reset" alt="검색조건 초기화" /></td>
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