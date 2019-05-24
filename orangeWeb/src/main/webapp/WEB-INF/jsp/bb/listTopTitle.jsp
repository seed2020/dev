<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<% // [게시판목록] 조회 %>
function setBrdList(obj){
	var selectObj=$('#brdId');
	selectObj.find('option').each(function(index){
		if(index==0) return true;
		$(this).remove();
	});
	var compId=$(obj).val();
	if(compId=='') return;
	callAjax('./selectBrdListAjx.do?menuId=${menuId}', {compId:compId}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			$.each(data.baBrdBVoList , function(index, baBrdBVo) {
				$vo=baBrdBVo.map;
        		selectObj.append('<u:option value="'+$vo.brdId+'" title="'+$vo.brdNm+'"/>');
        	});
        	selectObj.uniform();
		}
	});
};
<% // [게시판:법인] 조회 %>
function listCoprBull(){
	var $form = $('#searchForm');
	$form.find("input[name='compId']").remove();
	$form.appendHidden({name:'compId',value:$('#compId').val()});
	$form.find("input[name='brdId']").remove();
	$form.appendHidden({name:'brdId',value:$('#brdId').val()});
	$form.submit();
}
//-->
</script>
<c:set var="bbNm" value="${baBrdBVo.rescNm}" /><c:set var="bbNm" value="${baBrdBVo.rescNm}" />
<c:if test="${!empty ptCompBVoList }">
<u:set var="paramCompId" test="${!empty paramCompId }" value="${paramCompId }" elseValue="${param.compId }"/>
<div class="front notPrint">
	<div class="front_left">		
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td style="padding:3px 4px 0 0px"><u:title title="${bbNm}" menuNameFirst="true"/></td>
			<td class="width5"></td>
			<td class="frontinput">
				<select id="compId" name="compId" <u:elemTitle titleId="cols.comp" /> onchange="setBrdList(this);" style="min-width:100px;">
					<c:forEach items="${ptCompBVoList}" var="ptCompBVo" varStatus="status">
						<u:option value="${ptCompBVo.compId}" title="${ptCompBVo.rescNm}" checkValue="${paramCompId }"/>
					</c:forEach>
				</select>
			</td>
			<c:if test="${!empty baBrdBVoList }">
				<td class="width5"></td>
				<td class="frontinput">
					<select id="brdId" name="brdId" <u:elemTitle titleId="bb.btn.bbChoi" /> onchange="listCoprBull();" style="min-width:120px;">
						<u:option value="" titleId="cm.select.actname" alt="선택"/>
						<c:forEach items="${baBrdBVoList}" var="baBrdBVo" varStatus="status">
							<u:option value="${baBrdBVo.brdId}" title="${baBrdBVo.rescNm}" checkValue="${param.brdId }"/>
						</c:forEach>
					</select>
				</td>
			</c:if>
	 	</tr>
		</table>
	</div>
</div>
</c:if>
<c:if test="${empty ptCompBVoList}"><u:title title="${bbNm}" menuNameFirst="true"/></c:if>