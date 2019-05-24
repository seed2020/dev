<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--
<%// [아이콘 >] 추가 %>
function addOrgs(arr){
	var $hiddenTr = $("#listArea #hiddenTr"), $tr;
	var html = $hiddenTr[0].outerHTML;
	arr.each(function(index, obj){
		if(obj.orgId!=null && obj.orgId!='' && $("#listArea #tr"+obj.orgId).length==0){
			$hiddenTr.before(html);
			$tr = $hiddenTr.prev();
			$tr.attr('id','tr'+obj.orgId);
			$tr.find("[name='seltd']").val(obj.orgId).attr("id", obj.orgId).attr("data-rescId", obj.rescId);
			$tr.find("#rescNm").html("<label for='"+obj.orgId+"'>"+obj.rescNm+"</label>");
			if(obj.withSub=='Y'){
				$tr.find("#withSub")[0].checked = true;
			}
			$tr.show();
			setJsUniform($tr[0]);
		}
	});
}
<%// [아이콘 <] 제거 %>
function removeOrgs(){
	var tr;
	$("#listArea input[name='seltd']:checked").each(function(){
		tr = getParentTag(this, 'tr');
		if($(tr).attr('id') != 'hiddenTr'){
			$(tr).remove();
		}
	});
}
<%// 저장 버튼 - parent.gOrgSelectedObj 에 부서 정보 세팅함 %>
function getOrgSelectedArr(){
	var arr = [], $ck;
	$("#listArea tr").not("#headerTr, #hiddenTr").each(function(){
		$ck = $(this).find("[name='seltd']");
		if($ck.val()!=''){
			arr.push({orgId:$ck.val(), rescId:$ck.attr('data-rescId'), rescNm:$(this).find("#rescNm").text(), withSub:($(this).find("#withSub:checked").length > 0 ? "Y" : "N")});
		}
	});
	return arr;
}
<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
}
$(document).ready(function() {
	<%// 행추가 영역 제외하고 uniform 적용%>
	$("#listArea tbody:first").children("[id!='hiddenTr']").each(function(){
		setJsUniform(this);
	});
	var arr = parent.gOrgSelectedObj;
	if(arr!=null && arr.length>0){
		addOrgs(arr);
	}
});
//-->
</script>
<form id="listSeltdOrgForm" style="padding:10px;">

<u:listArea id="listArea">
	
	<tr id="headerTr">
		<th width="8%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></th>
		<th class="head_ct"><u:msg titleId="or.jsp.searchOrgPop.seltdOrg" alt="선택된 부서" /></th><c:if
			test="${param.withSub=='Y'}">
		<th class="head_ct" width="35%"><u:msg titleId="or.check.withSub" alt="하위부서 포함" /></th></c:if>
	</tr>

	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="hiddenTr" style="display:none" >
		<td class="bodybg_ct"><input type="checkbox" name="seltd" value="" data-rescId=""  /></td>
		<td class="body_lt" id="rescNm"></td><c:if
			test="${param.withSub=='Y'}">
		<td class="body_ct"><input type="checkbox" id="withSub" value="Y"<u:elemTitle titleId="or.check.withSub" alt="하위부서 포함"/> /></td></c:if>
	</tr>
</u:listArea>

</form>