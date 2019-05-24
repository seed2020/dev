<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="params"/>
<script type="text/javascript">
<!--<%
// [삭제] 버튼 클릭 - 관련요청 %>
function deleteHstReq(){
	var trArr = [], tr, trId, arrs=[];
	$("#hstReqListArea input:checked").each(function(){
		tr = getParentTag(this, 'tr');
		trId = $(tr).attr('id');
		if(trId!='hiddenTr' && trId!='titleTr'){
			trArr.push(tr);
			arrs.push($(tr).find('input[name="hstNo"]').val());
		}
	});
	if(arrs.length>0){
		callAjax('./transCmplHstDelAjx.do?menuId=${menuId}', {reqNo:'${param.reqNo}', hstNos:arrs}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				trArr.each(function(index, tr){
					$(tr).remove();
				});
				dialog.resize('listCmplHstDialog');
			}
		});
	}
}
<% // [팝업] 완료처리 이력 조회 %>
function viewCmplHstPop(hstNo){
	var url = './viewCmplHstPop.do?menuId=${menuId}&reqNo=${param.reqNo}&hstNo='+hstNo;
	dialog.open('viewCmplHstDialog','<u:msg titleId="wh.jsp.cmpl.small.title" alt="완료사항" />', url);
}
//-->
</script>
<div style="width:280px;">
<div class="front">
<div class="front_right">
	<table border="0" cellpadding="0" cellspacing="0">
	<tr><td class="frontbtn"><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="deleteHstReq();" auth="A"/></td>
	</tr>
	</table>
</div>
</div>
<u:listArea id="hstReqListArea">
	<tr id="titleTr">
		<td width="5%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all'
			/>" onclick="checkAllCheckbox('hstReqListArea', this.checked);" value=""/></td>
		<th width="30%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자"/></th>	
		<th class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></th>
	</tr>
<c:if test="${fn:length(whReqCmplDVoList)==0}">
	<tr id="hstReqNoDataTr">
		<td class="nodata" colspan="3"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:forEach items="${whReqCmplDVoList}" var="whReqCmplDVo" varStatus="status">
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
		<td class="bodybg_ct"><input type="checkbox" name="hstNo" value="${whReqCmplDVo.hstNo}" /></td>
		<td class="body_ct" ><a href="javascript:viewUserPop('${whReqCmplDVo.regrUid }');"><u:out value="${whReqCmplDVo.regrNm }"/></a></td>
		<td class="body_ct"><a href="javascript:viewCmplHstPop('${whReqCmplDVo.hstNo }');"><u:out value="${whReqCmplDVo.regDt }" type="longdate"/></a></td>
	</tr>
</c:forEach>
</u:listArea>
<u:blank />
<u:buttonArea>
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</div>