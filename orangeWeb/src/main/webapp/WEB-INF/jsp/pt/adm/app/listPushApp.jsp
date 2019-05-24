<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function mngApp(custCd, seq){
	var popTitle = custCd==null ? '<u:msg titleId="pt.jsp.appleApp.reg" alt="애플앱 등록" type="script" />' : '<u:msg titleId="pt.jsp.appleApp.mod" alt="애플앱 수정" type="script" />';
	dialog.open('setPushAppDialog', popTitle, './setPushAppPop.do?menuId=${menuId}'+(custCd==null ? '' : ("&custCd="+custCd))+(seq==null ? '' : '&seq='+seq));
}
<%// [버튼] 삭제 %>
function delApp(){
	var arr = getCheckedValue("listArea", "cm.msg.noSelect");<%//cm.msg.noSelect=선택한 항목이 없습니다.%>
	if(arr!=null && confirmMsg("cm.cfrm.del")) {<%//cm.cfrm.del=삭제하시겠습니까 ?%>
		callAjax('./transPushAppAjx.do?menuId=${menuId}', {cmd:'delete', custCds:arr.join(',')}, function(data){
			if(data.message!=null){
				alert(data.message);
			}
			if(data.result=='ok'){
				location.replace(location.href);
			}
		});
	}
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>

<u:title alt="애플앱 관리" menuNameFirst="true" />

<%// 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listPushApp.do">
	<input type="hidden" name="menuId" value="${menuId}" />
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="cols.custNm" alt="고객명" /></td>
		<td><u:input id="custNm" titleId="cols.custNm" style="width:200px;" value="${param.custNm}" maxByte="50" /></td>
		<td class="width10"></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<%// 목록 %>
<u:listArea id="listArea">

	<tr>
	<td width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.custCd" alt="고객코드" /></td>
	<td class="head_ct"><u:msg titleId="cols.custNm" alt="고객명" /></td>
	<td width="20%" class="head_ct"><u:msg titleId="cols.file.nm" alt="파일명" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.strtYmd" alt="시작일" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.endYmd" alt="종료일" /></td>
	<td width="15%" class="head_ct"><u:msg titleId="cols.modDt" alt="수정일시" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
	</tr>

<c:if test="${fn:length(ptPushAppDVoList)==0}" >
	<tr>
	<td class="nodata" colspan="8"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(ptPushAppDVoList) >0}" >
	<c:forEach items="${ptPushAppDVoList}" var="ptPushAppDVo" varStatus="status">
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
	<td class="bodybg_ct"><input type="checkbox" id="contactBack" value="${ptPushAppDVo.custCd}"/></td>
	<td class="body_ct">${ptPushAppDVo.custCd}</td>
	<td class="body_lt"><a href="javascript:mngApp('${ptPushAppDVo.custCd}');" title="<u:msg titleId="pt.jsp.appleApp.mod" alt="애플앱 수정"/> - <u:msg titleId="cm.pop"/>">${ptPushAppDVo.custNm}</a></td>
	<td class="body_ct"><u:out value="${ptPushAppDVo.dispNm}" /></td>
	<td class="body_ct"><u:out value="${ptPushAppDVo.strtYmd}" type="date" /></td>
	<td class="body_ct"><u:out value="${ptPushAppDVo.endYmd}" type="date" /></td>
	<td class="body_ct"><u:out value="${ptPushAppDVo.regDt}" type="longdate" /></td>
	<td class="body_ct"><u:out value="${ptPushAppDVo.useYn}" /></td>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>

<u:pagination />

<u:buttonArea>
	<u:button titleId="cm.btn.reg" alt="등록" href="javascript:mngApp();" auth="A" popYn="Y" />
	<u:button titleId="cm.btn.del" alt="삭제" href="javascript:delApp();" auth="A" />
</u:buttonArea>
