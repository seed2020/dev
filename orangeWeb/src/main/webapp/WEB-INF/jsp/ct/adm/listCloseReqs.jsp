<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

//폐쇄승인
function closeAppr(apprYn){
	var $selectCtClose = $("#selectCtCloseList");
	var selectCloseCtIds = [];
	var $checkedLength = $('input:checkbox:checked').length;
	var $ctCloseListTbl = $("#listArea");
	if($checkedLength == '0'){
		alert("<u:msg titleId="ct.msg.noSelectCt" alt="커뮤니티를 선택해주시기 바랍니다." />");
	}else{
		
		$ctCloseListTbl.find("tr[name='ctCloseVo']").each(function(){
			var $selectedCloseCtId = $(this).find("#closeCtId").val();
			$(this).find("#checkFlag").each(function(){
				if($(this).is(":checked")||$(this).attr("checked") == "checked"){
					if($(this).attr("tr")!='headerTr'){
						selectCloseCtIds.push($selectedCloseCtId);
					}
				}
			});

		});
		
		if(apprYn == 'Y'){
			dialog.open('setCloseApprOpPop', '<u:msg titleId="ct.cols.closeOp" alt="폐쇄사유" />','./setCloseApprOpPop.do?menuId=${menuId}&apprYn='+apprYn);
		}else{
			dialog.open('setCloseApprOpPop', '<u:msg titleId="ct.btn.closeRjt" alt="폐쇄거부" />','./setCloseApprOpPop.do?menuId=${menuId}&apprYn='+apprYn);
		}
	}
	
}

//체크박스 전체 선택
function checkAll(){
	if($("#checkFlagAll").is(":checked")){
		$("input[name='checkFlag']:checkbox").each(function(){
			$(this).parent().attr("class", "checked");
			$(this).attr("checked","checked");
		});
		
	}else{
		$("input[name='checkFlag']:checkbox").each(function(){
			$(this).parent().attr("class", "");
			$(this).removeAttr("checked");
		});
	}
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="ct.jsp.listCloseReqs.title" alt="커뮤니티 폐쇄신청" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listCloseReqs.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">
			<option value = "communityOpt" checkValue="${param.schCat}"><u:msg titleId="ct.cols.ctNm" alt="커뮤니티" /></option>
			</select></td>
		<td><u:input id="schWord" maxByte="50" value="" titleId="cols.schWord" style="width: 400px;"/></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<form id = "selectCtCloseList">
</form>

<% // 목록 %>
<u:listArea id="listArea">
	<tr id="headerTr">
	<td width="3%" class="head_bg"><u:checkbox id="checkFlagAll" name="checkFlagAll" value="all" checked="false" onclick="javascript:checkAll();" /></td>
	<td class="head_ct"><u:msg titleId="cols.cmNm" alt="커뮤니티명" /></td>
	<td width="20%" class="head_ct"><u:msg titleId="cols.cls" alt="분류" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.mast" alt="마스터" /></td>
	<%-- <td width="6%" class="head_ct"><u:msg titleId="cols.secul" alt="보안등급" /></td> --%>
	</tr>
	
	<c:forEach var="closeCtVo" items="${closeCtList}" varStatus="status">
		<tr id="ctCloseVo" name="ctCloseVo" onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
		<td class="bodybg_ct"><u:checkbox id="checkFlag" name="checkFlag" value="${status.count}" checked="false" /></td>
		<td class="body_lt">
			<u:out value="${closeCtVo.ctNm}"/>
			<input id="closeCtId" name="closeCtId" type="hidden" value="${closeCtVo.ctId}"/> 
		</td>
		<td class="body_ct">${closeCtVo.catNm}</td>
		<td class="body_ct"><a href="javascript:viewUserPop('${closeCtVo.mastUid}');">${closeCtVo.mastNm}</a></td>
		<%-- <c:if test="${closeCtVo.extnOpenYn == 'Y'}">
			<td class="body_ct"><u:msg titleId="ct.cols.open" alt="공개" /></td>
		</c:if>
		<c:if test="${closeCtVo.extnOpenYn == 'N'}">
			<td class="body_ct"><u:msg titleId="ct.cols.notOpen" alt="비공개" /></td>
		</c:if> --%>
	</c:forEach>
	<c:if test="${fn:length(closeCtList) == 0 }">
		<tr>
			<td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
</u:listArea>

<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:msg titleId="cm.msg.preparing" var="msg" alt="준비중.." />
	<u:button titleId="ct.btn.closeApvd" alt="폐쇄승인" auth="W" href="javascript:closeAppr('Y');" />
	<u:button titleId="ct.btn.closeRjt" alt="폐쇄거부" auth="W" href="javascript:closeAppr('N');" />
</u:buttonArea>
