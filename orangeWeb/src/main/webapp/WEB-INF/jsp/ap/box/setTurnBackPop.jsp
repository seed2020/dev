<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
		import="java.util.ArrayList"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><script type="text/javascript">
<!--<%
// 되돌리기 %>
function processTurnBackDoc(){
	var apvLnNo = $("#turnBackListArea input:checked").val();
	if(apvLnNo!=null){
		var result = false;
		callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {process:"turnBackDoc", apvNo:"${param.apvNo}", apvLnNo:apvLnNo}, function(data){
			if(data.message != null) alert(data.message);
			result = data.result == 'ok';
		});
		if(result) reload();
	}
}
//-->
</script>
<div style="width:600px">

<u:listArea id="turnBackListArea">
	<tr>
	<td width="2%" class="head_ct"></td>
	<td width="20%" class="head_ct"><u:msg titleId="cols.dept" alt="부서" /></td>
	<td width="16%" class="head_ct"><u:term termId="or.term.posit" alt="직위" /></td>
	<td width="16%" class="head_ct"><u:msg titleId="ap.cfg.apvr" alt="결재자" /></td>
	<td width="20%" class="head_ct"><u:msg titleId="ap.list.apvRole" alt="결재자역할" /></td>
	<td width="24%" class="head_ct"><u:msg titleId="ap.list.apvDt" alt="결재일시" /></td>
	</tr>
	
	<c:forEach
		items="${apOngdApvLnDVoList}"
		var="apOngdApvLnDVo" varStatus="status"><c:if
			test="${apOngdApvLnDVo.apvLnPno eq '0' and apOngdApvLnDVo.apvrRoleCd ne 'psnInfm' and apOngdApvLnDVo.apvrRoleCd ne 'deptInfm'}">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="bodybg_ct"><c:if
		test="${ apOngdApvLnDVo.apvrRoleCd ne 'abs' and (
				apOngdApvLnDVo.apvStatCd eq 'apvd' or apOngdApvLnDVo.apvStatCd eq 'rejt'
			 or apOngdApvLnDVo.apvStatCd eq 'pros' or apOngdApvLnDVo.apvStatCd eq 'cons')}"
		 ><input type="radio" name="turnBackApvLnNo" value="${apOngdApvLnDVo.apvLnNo}" /></c:if></td>
	<td class="body_ct"><u:out value="${apOngdApvLnDVo.apvDeptNm}" /></td>
	<td class="body_ct"><u:out value="${apOngdApvLnDVo.apvrPositNm}" /></td>
	<td class="body_ct"><u:out value="${apOngdApvLnDVo.apvrNm}" /></td>
	<td class="body_ct"><c:if
		test="${empty apOngdApvLnDVo.apvrRoleCd or (apOngdApvLnDVo.apvrRoleCd=='mak' and empty apOngdApvLnDVo.apvrNm)}">&nbsp;</c:if><c:if
		test="${not empty apOngdApvLnDVo.apvrRoleCd and not (apOngdApvLnDVo.apvrRoleCd=='mak' and empty apOngdApvLnDVo.apvrNm)}"
			><u:term termId="ap.term.${apOngdApvLnDVo.apvrRoleCd=='byOneAgr' ? 'byOne' : apOngdApvLnDVo.apvrRoleCd}"
		langTypCd="${apvData.docLangTypCd}" /><u:cmt
		
		cmt="rejt:반려, reRevw:재검토, cons:반대" /><c:if
		
		test="${apOngdApvLnDVo.apvStatCd == 'rejt' or apOngdApvLnDVo.apvStatCd == 'reRevw'
			or apOngdApvLnDVo.apvStatCd == 'cons'}"> (<u:term termId="ap.term.${apOngdApvLnDVo.apvStatCd}" /><c:if
		
		test="${not empty apOngdApvLnDVo.agntUid}">, <a href="javascript:viewUserPop('${apOngdApvLnDVo.agntUid}');"><u:term termId="ap.term.agnt" /></a></c:if>)</c:if><c:if
		
		test="${not empty apOngdApvLnDVo.agntUid and not (
			apOngdApvLnDVo.apvStatCd == 'rejt' or apOngdApvLnDVo.apvStatCd == 'reRevw'
			or apOngdApvLnDVo.apvStatCd == 'cons')}"> (<a href="javascript:viewUserPop('${apOngdApvLnDVo.agntUid}');"><u:term termId="ap.term.agnt" /></a>)</c:if></c:if><c:if
		test="${apOngdApvLnDVo.apvrRoleCd=='abs'}"> [${apOngdApvLnDVo.absRsonNm}]</c:if></td>
	<td class="body_ct"><u:out value="${apOngdApvLnDVo.apvDt}" /></td>
	</tr></c:if>
	</c:forEach>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" onclick="processTurnBackDoc();" alt="확인" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>