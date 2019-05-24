<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<%//checkbox 단일 한개의 catId 리턴  %>
function getCheckedTrs(noSelectMsg){
	var clsId;
	var $chkBox = $("input[name=chk]:checkbox:checked").length;
    if($chkBox == 1){
    	$("#ctListArea").find("input[name='chk']:checked").each(function(){
    		clsId =$(this).val();
    	});
    }else if($chkBox > 1){
         alert('<u:msg titleId="ct.jsp.setCd.modClsOnlyOne" alt="분류 수정은 한개씩 수정이 가능합니다." />');
         return null;
    }else{
    	if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
    }
	return clsId;
};

<%//checkbox 가 arr catId 리턴 %>
function getCheckedTrsDel(noSelectMsg){
	var clsId =[];
	var clsPid =[];
	var $chkBox = $("input[name=chk]:checkbox:checked").length;
    if($chkBox >= 1){
    	$("#ctListArea").find("input[name='chk']:checked").each(function(){
    		clsId.push($(this).val());
    		clsPid.push($("#clsPid").val());
    	});
    }else{
    	if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
    }
	return {clsId:clsId, clsPid:clsPid};
};

<%// 선택분류 리턴 %>
function clsSelId(){
	var clsId = getCheckedTrs("cm.msg.noSelect");
	if(clsId!=null) return clsId;
	else return null;
};

<%// 분류 삭제%>
function delCls(){
	var objCls = getCheckedTrsDel("cm.msg.noSelect");
	
	if(objCls.clsId==null){
		return null;
	} else if(confirmMsg("cm.cfrm.del")) {<%//cm.cfrm.del=삭제하시겠습니까 ?%>
		callAjax('./transCtClsDelAjx.do?menuId=${menuId}', {mode:'delete', catId:objCls.clsId}, function(data){
			if(data.message!=null){
				alert(data.message);
			}
			if(data.result=='ok'){
				reload('./listCtClsFrm.do?menuId=${menuId}&typ=C&catId='+objCls.clsPid[0]);
				
			}else{
//				reload('./listCtClsFrm.do?menuId=${menuId}&catId='+id);
				
				reload('./listCtClsFrm.do?menuId=${menuId}&typ=C&catId='+objCls.clsPid[0]);
			}
			
			
		});
	}
};

$(document).ready(function() {
setUniformCSS();
});
</script>
<form id="searchForm" name="searchForm" style="padding:10px;" action="">
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="typ" value="${param.typ}" />
	<u:input type="hidden" id="schInitial" value="${param.schInitial}" />
	<u:input type="hidden" id="schFldTypYn" value="${param.schFldTypYn}" />
	<u:input type="hidden" id="schFldId" value="${param.schFldId}" />
	<c:if test="${!empty param.listType }">
		<u:input type="hidden" id="listType" value="${param.listType}" />
	</c:if>
	

	
		<div class="front_left" id="ctListArea">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<colgroup>
					<col style="width: 6%;">
					<col style="width: 94%;">
				</colgroup>
				
				<c:forEach	items="${ctCatFldBVoList}" var="ctCatFldBVo" varStatus="status">
					<tbody>
					<tr>
						<td>
							<u:checkbox name="chk" value="${ctCatFldBVo.catId}" checked="false" />
						</td>
						<td class="body_lt">
							<label for="chk${status.count}">${ctCatFldBVo.catNm}</label>
							<input id="catId" value="${ctCatFldBVo.catId}" type="hidden"/>
							<input id="clsPid" value="${ctCatFldBVo.catPid}" type="hidden"/>

						</td>
							
					</tr>
					<tr>
						<td colspan="2" class="dotline">
						</td>
					</tr>
					</tbody>
				</c:forEach>
				
				
				</table>
					</div>

	
	


</form>
<u:blank />



