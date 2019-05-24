<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="params" excludes="data"/>
<u:set var="callback" test="${!empty param.callback }" value="${param.callback }" elseValue="setPichGrpList"/>
<script type="text/javascript">
<!--<%
// 요청 데이터 모으기 %>
function collectGrpToArrs(){
	var attrs = ["pichGrpId","pichGrpNm"], obj, $check, returnArr=[];
	$("#pichGrpListArea input[id!='checkHeader']").each(function(){
		obj = {};
		$check = $(this);
		attrs.each(function(index, attr){
			obj[attr] = $check.attr("data-"+attr);
		});
		returnArr.push(obj);
	});
	return returnArr;
}<%// [확인] 저장 %>
function applyCfrms(){
	var arr = collectGrpToArrs();
	${callback}(arr);
	dialog.close('findPichGrpListDialog');
}
<% // [팝업] - 그룹 상세 %>
function openGrpView(id){
	dialog.open('openGrpDialog','<u:msg titleId="wh.jsp.pichList.title" alt="담당자목록" />','./viewPichGrpPop.do?menuId=${menuId}&pichGrpId='+id);
	dialog.onClose("findPichGrpListDialog", function(){ dialog.close('openGrpDialog'); });
}
//-->
</script>
<div style="width:350px">
<u:titleArea
	outerStyle="height:350px; overflow-x:hidden; overflow-y:auto;"
	innerStyle = "NO_INNER_IDV">
<div class="resetFont" style="padding:5px 10px 0px 10px;">
<u:listArea id="pichGrpListArea">
	<tr id="titleTr">
		<c:if test="${empty isView || isView==false }"><td width="5%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all'
			/>" onclick="checkAllCheckbox('pichGrpListArea', this.checked);" value=""/></td></c:if>
		<th class="head_ct"><u:msg titleId="wh.cols.pichGrp.nm" alt="담당자그룹명" /></th>
	</tr>
<c:if test="${fn:length(whPichGrpBVoList)==0}">
	<tr id="noDataTr">
		<td class="nodata" colspan="2"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:forEach items="${whPichGrpBVoList}" var="whPichGrpBVo" varStatus="status">
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
		<c:if test="${empty isView || isView==false }"><td class="bodybg_ct"><input type="checkbox" name="pichGrpId" value="${whPichGrpBVo.pichGrpId
			}" data-pichGrpId="<u:out value="${whPichGrpBVo.pichGrpId}" type="value"
			/>" data-pichGrpNm="<u:out value="${whPichGrpBVo.pichGrpNm}" type="value"
			/>" /></td></c:if>
		<td class="body_lt" ><div class="ellipsis" title="${whPichGrpBVo.pichGrpNm }"><a href="javascript:openGrpView('${whPichGrpBVo.pichGrpId }');"><u:out value="${whPichGrpBVo.pichGrpNm }"/></a></div></td>
	</tr>
</c:forEach>
</u:listArea>
</div>
</u:titleArea>
<u:buttonArea>
	<c:if test="${empty isView || isView==false }"><u:button titleId="cm.btn.confirm" alt="확인" href="javascript:applyCfrms();" /></c:if>
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</div>