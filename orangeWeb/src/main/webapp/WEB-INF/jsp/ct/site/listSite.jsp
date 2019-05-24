<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

//선택된 카테고리에 해당하는 커뮤니티 검색
function schSiteCat(catId){
	$("#catId").val(catId);
	location.href = "./listSite.do?menuId=${menuId}&ctId=${ctId}&catId="+catId;
	
}

//쿨 사이트 삭제
function delSite(){
	var $selectCtSiteList = $("#selectCtSiteList");
	var selectCtSiteIds = [];
	var $checkedLength = $('input:checkbox:checked').length;
	var $ctSiteListTbl = $("#listArea");
	
	if($checkedLength == '0'){
		alert("<u:msg titleId="ct.msg.site.sel" alt="사이트를 선택해주시기 바랍니다."/>");
	}else{
		$ctSiteListTbl.find("tr[name='cmntSiteVo']").each(function(){
			var $selectedCtSiteId = $(this).find("#siteId").val();
			$(this).find("#checkFlag").each(function(){
				if($(this).is(":checked")||$(this).attr("checked") == "checked"){
					if($(this).attr("tr")!='headerTr'){
						selectCtSiteIds.push($selectedCtSiteId);
					}
				}
			});
		});
		
		if (confirmMsg("cm.cfrm.del")) {
			callAjax('./transSiteListDel.do?menuId=${menuId}&ctId=${ctId}', {selectCtSiteIds:selectCtSiteIds}, function(data){
				if (data.message != null) {
					alert(data.message);
				}
				if (data.result == 'ok') {
					location.href = './listSite.do?menuId=${menuId}&ctId=${ctId}';
				}
			});
		}else{
			return;
		}
	}
}


//체크박스 전체 선택
function checkAll(){
	if($("#checkFlagAll").is(":checked")){
		$("input[name='checkFlag']:checkbox").each(function(){
			if($(this).attr("disabled") != 'Y'){
				$(this).parent().attr("class", "checked");
				$(this).attr("checked","checked");
			}
		});
		
	}else{
		$("input[name='checkFlag']:checkbox").each(function(){
			if(!$(this).attr("disabled") != 'Y'){
				$(this).parent().attr("class", "");
				$(this).removeAttr("checked");
			}
		});
	}
}

function openSite(url) {
	window.open(url);
}

function setCat() {
	dialog.open('setCatPop','<u:msg titleId="ct.btn.catMng" alt="카테고리 관리"/>','./setCatPop.do?menuId=${menuId}&ctId=${ctId}');
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title title="${menuTitle }" alt="Cool Site" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listSite.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="ctId" name="ctId" value="${ctId}" />
	<u:input type="hidden" id="catId" name="catId" value="${catId}" />
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">
			<u:option value="SUBJ" titleId="cols.subj" alt="제목" checkValue="${param.schCat}" />
			<u:option value="CONT" titleId="cols.cont" alt="내용" checkValue="${param.schCat}" />
			<u:option value="REGR_NM" titleId="cols.regr" alt="등록자" checkValue="${param.schCat}" />
			</select></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 400px;" /></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<% // 카테고리 %>
<div class="titlearea">
	<div class="tit_right">
	<ul>
	<li class="txt"><u:msg titleId="ct.msg.reqs.cat" alt="카테고리"/></li>
	<li><select id="catChoi" name="catChoi" onchange="javascript:schSiteCat(this.value)">
			<option value=""><u:msg titleId="cm.option.all" alt="전체" /></option>
			<c:forEach var="siteCatVo" items="${siteCatList}" varStatus="status">
				<option value="${siteCatVo.catId}" <c:if test="${siteCatVo.catId == catId}"> selected="selected"</c:if>>${siteCatVo.catNm}</option>
			</c:forEach>
		</select></li>
	</ul>
	</div>
</div>

<form id = "selectCtSiteList">
</form>

<% // 목록 %>
<div id="listArea" class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="width:100%;table-layout:fixed;">
		<tr id="headerTr">
		<td width="3%" class="head_bg"><u:checkbox id="checkFlagAll" name="checkFlagAll" value="all" checked="false" onclick="javascript:checkAll();"/></td>
		<td width="10%" class="head_ct"><u:msg titleId="cols.cat" alt="카테고리" /></td>
		<td class="head_ct"><u:msg titleId="cols.siteNm" alt="사이트명" /></td>
		<td width="10%" class="head_ct"><u:msg titleId="cols.shcut" alt="바로가기" /></td>
		<td width="10%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
		<td width="10%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
		</tr>
		
		<c:forEach var="siteVo" items="${siteList}" varStatus="status">
			<tr id="cmntSiteVo" name="cmntSiteVo" onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
				<td class="bodybg_ct"><u:checkbox id="checkFlag" name="checkFlag" value="${status.count}" checked="false" /></td>
				<td class="body_ct"><u:out value="${siteVo.catNm}"/></td>
				<td class="body_lt">
					<div class="ellipsis" title="<u:out value="${siteVo.subj}"/>">
						<a href="./viewSite.do?menuId=${menuId}&ctId=${ctId}&siteId=${siteVo.siteId}&catId=${siteVo.catId}"><u:out value="${siteVo.subj}"/></a></td>
					</div>
				<td class="listicon_ct"><u:buttonS titleId="ct.btn.shcut" alt="바로가기" onclick="openSite('${siteVo.url}');" /></td>
				<td class="body_ct"><a href="javascript:viewUserPop('${siteVo.regrUid}');">${siteVo.regrNm}</a>
					<input id="siteId" name="siteId" type="hidden" value="${siteVo.siteId}">
				</td>
				<td class="body_ct">
					<fmt:parseDate var="dateTempParse" value="${siteVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
					<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
				</td>
			</tr>
		</c:forEach>
		<c:if test="${fn:length(siteList) == 0 }">
			<tr>
				<td class="nodata" colspan="6"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
		</c:if>
	</table>
</div>
<u:blank/>

<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
	<c:choose>
		<c:when test="${!empty myAuth && myAuth == 'M' }">
			<u:button titleId="cm.btn.reg" alt="등록" href="./setSite.do?menuId=${menuId}&ctId=${ctId}"/>
			<u:button titleId="cm.btn.del" alt="삭제" href="javascript:delSite();"/>
			<u:button titleId="ct.btn.catMng" alt="카테고리관리" onclick="javascript:setCat();"/>
		</c:when>
		<c:otherwise>
			<c:if test="${!empty authChkW && authChkW == 'W' }">
					<u:button titleId="cm.btn.reg" alt="등록" href="./setSite.do?menuId=${menuId}&ctId=${ctId}"/>
			</c:if>
			<c:if  test="${!empty authChkD && authChkD == 'D' }">
					<u:button titleId="cm.btn.del" alt="삭제" href="javascript:delSite();"/>
			</c:if>
		</c:otherwise>
	</c:choose>
</u:buttonArea>
