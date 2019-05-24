<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><link rel="stylesheet" href="${_cxPth}/css/formbuilder.css" type="text/css" /><%
// jstl 에서 스페이스 + 엔터 치환
pageContext.setAttribute("enter","\r\n"); %>
<script type="text/javascript">
<!--
var searchArea2=null;
<%// 사용자,부서 선택 - 복합식 %>
function openUserPop(id){
	var data = [];<%// data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	var val=$('#searchArea2 input[id="'+id+'"]').val();
	if(val!='') data.push({userUid:val});
	<%// option : data, multi, withSub, titleId %>
	parent.searchUserPop({data:data}, function(userVo){
		if(userVo!=null){
			$('#searchArea2 input[id="'+id+'"]').val(userVo.userUid);
			$('#searchArea2 input[id="'+id+'Nm"]').val(userVo.rescNm);
		}
	});
}<%// 부서 선택 %>
function openOrgPop(id){
	var data = [];
	var val=$('#searchArea2 input[id="'+id+'"]').val();
	if(val!='') data.push({orgId:val});
	parent.searchOrgPop({data:data}, function(orgVo){
		if(orgVo!=null){
			$('#searchArea2 input[id="'+id+'"]').val(orgVo.orgId);
			$('#searchArea2 input[id="'+id+'Nm"]').val(orgVo.rescNm);
		}
	});
}
<% // 체크박스 목록 레이어 닫기 %>
function closeSelectList(obj){
	var parent=$(obj).closest('#check_select_list');
	var target=parent.find('#select_list_area');
	target.hide();
}
<% // 체크박스 목록 레이어 노출 %>
function chnSelectList(obj){
	var parent=$(obj).closest('#check_select_list');
	var target=parent.find('#select_list_area');
	target.toggle();
}
<% // 코드 검색조건 세팅 %>
function setSchCodeParams(){
	var vaArrs;
	<c:forEach var="codeParam" items="${codeParamMap }" varStatus="status">
	vaArrs=[];
	<c:forEach var="code" items="${codeParam.value }" varStatus="codeStatus">vaArrs.push('${code}');</c:forEach>
	$('#searchArea2').find(':checkbox[name="${codeParam.key}"]').val(vaArrs);
	</c:forEach>
	$('#searchArea2').find("input, select").uniform();
}

$(document).ready(function() {
	<c:if test="${!empty param.srchDetl && !empty codeParamMap }">setSchCodeParams();</c:if>
});
//-->
</script><c:set var="paramMap" value="${param }" scope="request"
/><u:convertJson var="jsonVa" value="${jsonAttrVa }" 
/>
<% // 검색영역 %>
<c:if test="${!empty schCatList || !empty durCatList}">
<u:searchArea style="position:relative; z-index:2;">
	<div id="searchArea1" style="<c:if test="${not empty param.srchDetl}">display:none;</c:if>">
	<form id="searchForm1" name="searchForm1" action="${_uri}" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="formNo" value="${param.formNo}" /><c:if
		test="${not empty param.pageRowCnt}">
	<u:input type="hidden" id="pageRowCnt" value="${param.pageRowCnt}" /></c:if>
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><u:buttonIcon alt="검색 조건 펼치기" titleId="cm.ico.showCondi" image="ico_wdown.png" onclick="$('#searchArea1').toggle(); $('#searchArea2').toggle();" /></td>
		<c:if test="${!empty schCatList }">
		<td><select name="schCat"><c:forEach var="schCat" items="${schCatList }" varStatus="status"
		><u:option value="${schCat[0] }" title="${schCat[1] }" alt="${schCat[1] }" checkValue="${param.schCat}" selected="${empty param.schCat && status.first}"
		/></c:forEach></select></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 200px;" onkeydown="if (event.keyCode == 13) searchForm1.submit();" 
		/></td><td class="width20"></td></c:if><c:if test="${!empty durCatList }">
		<td><select name="durCat"><c:forEach var="durCat" items="${durCatList }" varStatus="status"
		><u:option value="${durCat[0] }" title="${durCat[1] }" alt="${durCat[1] }" checkValue="${param.durCat}" selected="${empty param.durCat && status.first}"
		/></c:forEach></select></td>
		<td><table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td><u:calendar id="durStrtDt1" name="durStrtDt" option="{end:'durEndDt1'}" titleId="cols.strtYmd" value="${param.durStrtDt}" /></td>
			<td class="search_body_ct"> ~ </td>
			<td><u:calendar id="durEndDt1" name="durEndDt" option="{start:'durStrtDt1'}" titleId="cols.endYmd" value="${param.durEndDt}" /></td>
			</tr>
			</table></td><td class="width20"></td></c:if>
		</tr>
		</table>
	</td>
	<td><div class="button_search"><ul><li class="search"><a href="javascript:document.searchForm1.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form></div>
	<div id="searchArea2" style="<c:if test="${empty param.srchDetl}">display:none;</c:if>">
	<c:set var="srchYmdMap" value="${srchYmdMap }" scope="request"/>
	<form id="searchForm2" name="searchForm2" action="${_uri}" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="formNo" value="${param.formNo}" /><c:if
		test="${not empty param.pageRowCnt}">
	<u:input type="hidden" id="pageRowCnt" value="${param.pageRowCnt}" /></c:if>
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
		<c:set var="dispCnt" value="1"/>
		<c:forEach var="dtlVo" items="${dtlSrchList }" varStatus="status">
			<c:set var="colmTyp" value="${dtlVo[2] }"/> <!-- 컬럼 구분 -->
			<c:set var="atrbId" value="${dtlVo[0]}" 
			/><c:set var="jsonMap" value="${jsonVa[atrbId] }" scope="request"
			/><u:convertMap var="paramVal" srcId="paramMap" attId="${atrbId }" />
			<c:if test="${dispCnt > 1 && dispCnt%maxDispCnt == 1}"></tr><tr></c:if>
			<td class="search_tit">${dtlVo[1] }</td>
			<td class="body_lt"><c:if test="${colmTyp eq 'text' || colmTyp eq 'textarea' || colmTyp eq 'editor' || colmTyp eq 'number' || colmTyp eq 'tel' || colmTyp eq 'addr'}"
			><u:set var="paramVal" test="${param.schCat eq atrbId && !empty param.schWord }" value="${param.schWord}" elseValue="${paramVal }"
			/><u:input id="${atrbId}" value="${paramVal}" title="${dtlVo[1] }" style="width: 98%;"
			/></c:if><c:if test="${fn:startsWith(colmTyp,'date') || colmTyp eq 'period' || colmTyp eq 'regDt' || colmTyp eq 'modDt'}"
			><u:convertMap var="strtDtValue" srcId="paramMap" attId="${atrbId }StrtDt" type="html" 
			/><u:convertMap var="endDtValue" srcId="paramMap" attId="${atrbId }EndDt" type="html" 
			/><table border="0" cellpadding="0" cellspacing="0"
			><tr><td><u:calendartime id="${atrbId }StrtDt" name="${atrbId }StrtDt" option="{end:'${atrbId }EndDt'}" title="${dtlVo[1] }" value="${strtDtValue}" type="${colmTyp eq 'time' ? 'time' : 'calendar'  }"
			/></td><td class="search_body_ct"> ~ </td
			><td><u:calendartime id="${atrbId }EndDt" name="${atrbId }EndDt" option="{start:'${atrbId }StrtDt'}" title="${dtlVo[1] }" value="${endDtValue}" type="${colmTyp eq 'time' ? 'time' : 'calendar'  }"
			/></td></tr></table></c:if><c:if test="${colmTyp eq 'time'}"
			><u:calendartime id="${atrbId }" name="${atrbId }" title="${dtlVo[1] }" value="${paramVal}" type="time"
			/></c:if><c:if test="${colmTyp eq 'radio' || colmTyp eq 'checkbox' || colmTyp eq 'select'}"
			><c:if test="${!empty jsonMap}"
			><c:if test="${colmTyp eq 'checkbox'}"><div id="check_select_list"><div style="min-width:100px;"
			><u:buttonS href="javascript:;" titleId="cm.btn.choice" alt="선택" onclick="chnSelectList(this);" /></div
			><div id="select_list_area" class="check_select_area" style="display:none;"><div class="check_select"><div><span><a class="close-thik" onclick="closeSelectList(this);" href="#"></a></span></div>
			<ul class="check_list"><c:if test="${empty jsonMap['chkTypCd'] }"><c:forTokens var="chkOpt" items="${jsonMap['chkList'] }" delims="${enter }" varStatus="chkStatus"
			><li><div style="float:left;"><span><input name="${atrbId }" id="${atrbId }_${chkStatus.index}" value="${chkOpt }" type="checkbox" <c:if test="${chkOpt eq paramVal}">checked="checked"</c:if>></span></div
			><span style="line-height:20px;"><label for="${atrbId }_${chkStatus.index}">${chkOpt }</label></span></li></c:forTokens></c:if
			><c:if test="${!empty jsonMap['chkTypCd'] && !empty formCdListMap}"
			><u:convertMap srcId="formCdListMap" attId="CD_LIST_${jsonMap['chkTypCd']}" var="wfCdGrpBVoList" 
			/><c:forEach var="wfCdGrpBVo" items="${wfCdGrpBVoList }" varStatus="chkStatus"
			><li style="width:98%;"><div style="float:left;"><span><input name="${atrbId }" id="${atrbId }_${chkStatus.index}" value="${wfCdGrpBVo.cdId }" type="checkbox" <c:if test="${wfCdGrpBVo.cdId eq paramVal}">checked="checked"</c:if>></span></div
			><span style="line-height:20px;"><label for="${atrbId }_${chkStatus.index}">${wfCdGrpBVo.cdRescNm }</label></span></li>		
			</c:forEach></c:if></ul></div></div></div></c:if><c:if test="${colmTyp ne 'checkbox'}"
			><select id="${atrbId }" name="${atrbId }" title="${dtlVo[1] }"
			><u:option value="" titleId="cm.option.all" alt="전체" selected="${empty paramVal }"
			/><c:if test="${empty jsonMap['chkTypCd'] }"><c:forTokens var="chkOpt" items="${jsonMap['chkList'] }" delims="${enter }" varStatus="chkStatus"
			><u:option value="${chkOpt}" title="${chkOpt }" checkValue="${paramVal }"
			/></c:forTokens></c:if><c:if test="${!empty jsonMap['chkTypCd'] && !empty formCdListMap}"
			><u:convertMap srcId="formCdListMap" attId="CD_LIST_${jsonMap['chkTypCd']}" var="wfCdGrpBVoList" 
			/><c:forEach var="wfCdGrpBVo" items="${wfCdGrpBVoList }" varStatus="chkStatus"
			><u:option value="${wfCdGrpBVo.cdId}" title="${wfCdGrpBVo.cdRescNm }" checkValue="${paramVal }"
			/></c:forEach></c:if></select></c:if>
			</c:if></c:if><c:if test="${colmTyp eq 'user' || colmTyp eq 'dept' || colmTyp eq 'regrNm' || colmTyp eq 'modrNm'}"
			><u:convertMap var="valueId" srcId="paramMap" attId="${atrbId}" type="html" />
			<u:convertMap var="valueNm" srcId="paramMap" attId="${atrbId}Nm" type="html" />
			<table border="0" cellpadding="0" cellspacing="0">
				<tbody>
				<tr>
					<td>
						<u:input type="hidden" id="${atrbId}" value="${valueId}"/>
						<u:input id="${atrbId}Nm" title="${dtlVo[1] }" readonly="Y" mandatory="Y" value="${valueNm }"/>
					</td>
					<u:set var="openIdPop" test="${colmTyp eq 'dept'}" value="openOrgPop" elseValue="openUserPop"/>
					<td><u:buttonS href="javascript:;" titleId="cm.btn.choice" alt="선택" onclick="${openIdPop }('${atrbId}');" /></td>
				</tr>
				</tbody>
			</table></c:if></td>
			<td class="width20"></td>
			<c:if test="${(dispCnt == 1 || dispCnt%maxDispCnt == 1) && fn:length(dtlSrchList) == status.count }"></tr><tr><c:set var="dispCnt" value="0"/></c:if>
			<c:set var="dispCnt" value="${dispCnt+1 }"/>
		</c:forEach>
		</tr>
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
</c:if>