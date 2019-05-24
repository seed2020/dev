<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	String callback = (String)request.getAttribute("callback");
	if(callback==null || callback.isEmpty()) callback = "setSelInfos";
%><u:set var="treePage" test="${param.lstTyp eq 'C'}" value="treeDocClsFrm" elseValue="treeDocFldFrm"/>
<u:set var="treePageParam" test="${param.lstTyp eq 'C'}" value="&initSelect=Y" elseValue="&fldId=${param.selIds }"/>
<script type="text/javascript">
<!--<%// [트리: 트리클릭] - 오른쪽 리스트 열기 %>
function openListFrm(){};
<%// [아이콘 > ] 추가%>
function addRows(){
	var tree = getIframeContent('docTree').getTreeData();
	if(tree.id != 'ROOT')	{
		var selObj = {id:tree.clsId,nm:tree.clsNm};
		getIframeContent('openListFrm').addRows(selObj);
	}
};<%// [아이콘 < ] 삭제 %>
function delSelRows(){
	getIframeContent('openListFrm').delSelRows();
}<%// 선택된 유형 %>
function getCatSelect(){
	var arr = [];
	$('#catListArea a.on').each(function(){
		arr.push({id:$(this).attr('data-catId'),nm:$(this).attr('data-catNm'),storId:$(this).attr('data-storId')});
	});
	if(arr.length == 0) return null;
	return arr;
}<%// 확인 버튼 클릭[한개] %>
function applySelect(){
	var arr = getCatSelect();
	if(arr != null) setCatInfo(arr);
	
}<%// [트리: 트리클릭] - 오른쪽 리스트 열기 %>
function catClick(obj, id){
	if(obj!=null){
		$('#catListArea a.on').attr('class','');
		$(obj).attr('class','on');
	}
	reloadFrame('catListFrm', './findCatFrm.do?menuId=${menuId}&catId='+id);
};
$(document).ready(function() {
	<c:if test="${fn:length(dmCatBVoList)>0}">
		var selObj = $('#catListArea a.on').eq(0);
		catClick(null,$(selObj).attr('data-catId'));
	</c:if>
	setUniformCSS();
});
//-->
</script>
<div style="width:500px;">
<div style="height:420px;">

<!-- LEFT -->
<div style="float:left;width:63%;" id="leftContainer">
<u:titleArea outerStyle="height:380px; overflow-x:hidden; overflow-y:auto;padding:10px;" noBottomBlank="true" innerStyle="NO_INNER_IDV">
<u:listArea colgroup=",35%" id="catListArea" tbodyClass="textBox">
	<tr>
		<td class="head_ct"><u:msg titleId="dm.cols.catNm" alt="유형명" /></td>
		<td class="head_ct"><u:msg titleId="dm.cols.dftYn" alt="기본여부" /></td>
	</tr>
	<c:choose>
		<c:when test="${!empty dmCatBVoList}">
			<c:forEach var="list" items="${dmCatBVoList }" varStatus="status">
				<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
					<td class="body_lt" ><a href="javascript:;" onclick="catClick(this, '${list.catId }');" class="${(empty param.selId && status.first) || !empty param.selId && param.selId == list.catId ? 'on' : '' }" data-catId="${list.catId }" data-catNm="${list.catNm }" data-storId="${list.storId }">${list.catNm }</a></td>
					<td class="body_ct">${list.dftYn }</td>
				</tr>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<tr>
				<td class="nodata" colspan="2"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
		</c:otherwise>
	</c:choose>
</u:listArea>
</u:titleArea>	
</div>

<!-- RIGHT -->
<div style="float:right; width:35%;" id="rContainer">
<u:titleArea frameId="catListFrm" frameSrc="/cm/util/reloadable.do"
	outerStyle="height:400px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:370px;" noBottomBlank="true"  />
</div>

</div>
	
<u:buttonArea id="rightBtnArea">
	<c:if test="${param.fncMul eq 'Y'}"><u:button titleId="cm.btn.confirm" alt="확인" href="javascript:applySelects();" auth="W" /></c:if>
	<c:if test="${empty param.fncMul || param.fncMul eq 'N'}"><u:button titleId="cm.btn.confirm" alt="확인" href="javascript:applySelect();" auth="W" /></c:if>
	<u:button id="btnCancel" titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>

</div>