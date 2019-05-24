<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.Date"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
// jstl 에서 스페이스 + 엔터 치환
pageContext.setAttribute("enter","\r\n"); %><!-- 권한 -->
<u:secu auth="A" ><c:set var="writeAuth" value="Y"/></u:secu>
<script type="text/javascript">
<!--
<% // 등록 팝업 %>
function setSchdlPop(schdlId){
	//쓰기 권한 체크
	if('${writeAuth}' != 'Y') return;
	var url = "./setSchdlPop.do?menuId=${menuId}";
	var popTitle = '<u:msg titleId="wc.btn.schdlReg" alt="일정등록" />';
	if(schdlId != null) {
		url+= "&schdlId="+schdlId;
		popTitle = '<u:msg titleId="wc.btn.schdlMod" alt="일정수정" />';
	}
	if(arguments.length > 1){
		url+= "&strtDt="+arguments[1];
	}else if(arguments.length==0){
		url+= "&strtDt="+moment().format('YYYYMMDD');
	}
	url+="&callback=reloadWorkSchdule";
	dialog.open('setSchdlPop',popTitle,url);	
};
<% // 페이지 리로드 %>
function reloadWorkSchdule(){
	reload();
}
<% // 검색조건 등록자 선택 %>
function findUserPop() {
	var $view = $("#searchFormTbl1");
	var data = {userUid:$view.find("#userUid").val()};<% // 팝업 열때 선택될 데이타 %>
	<% // option : data, multi, withSub, titleId %>
	searchUserPop({data:data}, function(userVo){
		if(userVo!=null){
			$view.find("#userUid").val(userVo.userUid);
			$view.find("#userNm").val(userVo.rescNm);
		}else{
			return false;
		}
	});
}
$(document).ready(function() {
	$('#listArea tbody:first tr, #listArea #tr').find('a, input[type="checkbox"]').click(function(event){
		if(event.stopPropagation) event.stopPropagation(); //MOZILLA
		else event.cancelBubble = true; //IE
	});
	setUniformCSS();
});
//-->
</script>
<u:title titleId="wa.title.car.mng" menuNameFirst="true" alt="업무용승용차관리" />

<% // 검색영역 %>
<u:searchArea id="searchFormTbl1">
	<form name="searchForm" action="${_uri }" >
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="cols.schdlKnd" alt="일정종류"/></td>
		<td><select name="schdlTypCd">
			<u:option value="" titleId="cm.option.all" alt="전체" selected="${empty param.schdlTypCd }"/>
			<c:forEach var="list" items="${wcCatClsBVoList }" varStatus="status">
			<u:option value="${list.cd }" title="${list.rescNm }" alt="${list.rescNm }" checkValue="${param.schdlTypCd}"/>
			</c:forEach>
			</select></td>
		<td class="width20"></td>
		<td class="search_tit"><u:msg titleId="wc.cols.schdlKndCd" alt="일정대상" /></td>
		<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			<td><u:input id="userNm" value="${param.userNm}" titleId="wc.cols.schdlKndCd" readonly="Y" />
				<u:input type="hidden" id="userUid" value="${param.userUid}" /></td>
			<td><u:buttonS titleId="cm.btn.search" alt="검색" onclick="findUserPop();" /></td>
			</tr>
			</tbody></table></td>
		<td class="width20"></td>
		<!-- 등록일시 -->
		<td class="search_tit"><u:msg titleId="cols.prd" alt="기간" /></td>
		<td><u:input type="hidden" id="durCat" value="useYmd" />
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td><u:calendar id="strtDt" option="{end:'endDt'}" value="${param.strtDt}" /></td>
			<td class="search_body_ct"> ~ </td>
			<td><u:calendar id="endDt" option="{start:'strtDt'}" value="${param.endDt}" /></td>
			</tr>
			</table></td>
		</tr><tr>
			<td colspan="2"><u:buttonS href="javascript:;" onclick="valueReset('searchFormTbl1',['menuId', 'durCat']);" titleId="cm.btn.srch.reset" alt="검색조건 초기화" /></td>
			<td class="width20"></td>			
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<c:set var="colspan" value="5"/>

<u:listArea id="listArea" colgroup="15%,14%,15%,,15%" >	
	<tr id="headerTr">
		<th class="head_ct"><u:msg titleId="cols.schdlKnd" alt="일정종류"/></th>
		<th class="head_ct"><u:msg titleId="wc.cols.schdlKndCd" alt="일정대상" /></th>
		<th class="head_ct"><u:msg titleId="wc.cols.date" alt="일자"/></th>
		<th class="head_ct"><u:msg titleId="cols.cont" alt="내용"/></th>
		<th class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시"/></th>
		
	</tr>
	<c:if test="${fn:length(wcWorkSchdlBVoList) == 0}">
		<tr>
		<td class="nodata" colspan="${colspan}"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
	<c:if test="${fn:length(wcWorkSchdlBVoList)>0}">
		<c:forEach items="${wcWorkSchdlBVoList}" var="wcWorkSchdlBVo" varStatus="status">
			<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"' onclick="setSchdlPop('${wcWorkSchdlBVo.schdlId }');">
				<td class="body_ct"><u:out value="${wcWorkSchdlBVo.schdlTypNm }"/></td>
				<td class="body_ct"><a href="javascript:;" onclick="viewUserPop('${wcWorkSchdlBVo.userUid}');"><u:out value="${wcWorkSchdlBVo.userNm }"/></a></td>
				<td class="body_ct"><fmt:parseDate var="dateStartDt" value="${wcWorkSchdlBVo.strtDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
					<fmt:formatDate var="convStartDt" value="${dateStartDt}" pattern="yyyy-MM-dd"/><u:out value="${convStartDt }"/></td>
				<td class="body_lt">${fn:replace(wcWorkSchdlBVo.cont, enter, '') }</td>
				<td class="body_ct"><u:out value="${wcWorkSchdlBVo.regDt }" type="longdate"/></td>
			</tr>
		</c:forEach>	
	</c:if>
	
</u:listArea>
<u:pagination />
<% // 하단 버튼 %>
<u:buttonArea>
<u:button titleId="cm.btn.write" alt="등록" href="javascript:;" onclick="setSchdlPop(null);" auth="A" />
</u:buttonArea>