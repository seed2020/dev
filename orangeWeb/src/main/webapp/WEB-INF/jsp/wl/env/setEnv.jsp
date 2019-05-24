<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%//checkbox 가 선택된 div ubox 테그 목록 리턴 %>
function getCheckedUboxs(noSelectMsg, areaId){
	var arr=[], obj;
	$("#"+areaId+" div.ubox input[type='checkbox']:checked").each(function(){
		obj = $(this).closest('div.ubox');
		if(obj!=undefined) arr.push(obj);
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
}<%// 선택삭제%>
function delSelUbox(areaId){
	var arr = getCheckedUboxs("cm.msg.noSelect", areaId);
	if(arr!=null) delUboxInArr(arr);
}<%// 삭제 - 배열에 담긴 목록%>
function delUboxInArr(arr){
	var delArr = [], $chkId;
	for(var i=0;i<arr.length;i++){
		$chkId = $(arr[i]).find("input[id='grpNo']").val();
		if($chkId=='') continue;
		delArr.push($chkId);
		$(arr[i]).remove();
	}
	if(delArr.length>0){
		callAjax('./transReprtGrpDelAjx.do?menuId=${menuId}', {grpNos:delArr}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
		});
	}
}
<% // [하단버튼:보고자그룹] - 팝업 %>
function setReprtGrpPop(id) {
	var url='./setReprtGrpPop.do?menuId=${menuId}';
	if(id!=undefined)
		url+='&grpNo='+id;
	dialog.open('setReprtGrpDialog','<u:msg titleId="wl.btn.user.select" alt="보고자선택" />', url);
}<% // [버튼클릭] - 보고그룹삭제  %>
function grpSelectDel(obj){
	var isAll=obj===undefined;
	
	var arr=isAll ? getDelList($('#groupList')) : getDelList($(obj).closest('div.ubox'));
	if(arr==null) return;
	
	callAjax('./transReprtGrpDelAjx.do?menuId=${menuId}', {grpNos:arr}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			if(isAll) $('#groupList').html('');	
			else $(obj).closest('div.ubox').remove();
		}
	});
};<% // 그룹번호 조회  %>
function getDelList(parent){
	var arr=[];
	$(parent).find('input[name="grpNo"]').each(function(){
		arr.push($(this).val());
	});
	if(arr.length==0)
		return null;
	return arr;
}<% // 저장 - 버튼 클릭 %>
function save(){
	if (validator.validate('setEnvForm')) {
		var typCdList=$("#typCdsArea input[type='radio']");
		
		var isYear=false;
		$.each(typCdList, function(index, obj){
			if($(obj).val()=='year') isYear=true;
		});
		
		var $form = $("#setEnvForm");
		
		if(isYear){
			var consolYear='';
			$('#yearConsolArea select').each(function(){
				consolYear+=$(this).val();
				setDisabled($(this), true);
			});
			if(consolYear!=''){
				$form.find("input[name='consolYear']").remove();
				$form.appendHidden({name:'consolYear',value:consolYear});	
			}
		}
		
		$form.attr('method','post');
		$form.attr('action','./transEnv.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form.submit();	
	}
};
$(document).ready(function() {	
	setUniformCSS();
});
//-->
</script>

<u:title titleId="dm.jsp.setEnv" alt="환경설정" />
<c:set var="map" value="${envConfigMap }" scope="request"/>
<c:if test="${!empty tabListMap }">
<form id="setEnvForm" method="post">
<input type="hidden" name="menuId" value="${menuId}" />
<u:title titleId="wl.jsp.logItem.sub.title" alt="업무일지 항목관리" type="small" />
<u:listArea colgroup="15%," noBottomBlank="true">
<tr>
	<td class="head_lt"><u:msg titleId="wl.cols.typCd.select" alt="종류선택" /></td>
	<td class="bodybg_lt">
		<u:checkArea id="typCdsArea">
			<c:forEach var="tab" items="${tabList }" varStatus="status">
				<u:radio id="typCds${status.index }" name="typCd" value="${tab}" titleId="wl.cols.typCd.${tab }" checked="${(empty envConfigMap.typCd && status.index==0)}" checkValue="${envConfigMap.typCd }"/>
			</c:forEach>
		</u:checkArea>
	</td>
</tr>
<c:forEach items="${columnList}" var="colVo" varStatus="status">
<c:set var="colNm" value="${colVo[0] }"/>
<tr>
	<td class="head_lt"><u:msg titleId="wl.cols.${colNm}" alt="실적" /></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><u:convertMap var="heightVa" srcId="map" attId="${colNm}Height" type="html" 
		/><select name="${colNm}Height"><u:option value="" titleId="cols.hghtPx"  selected="${empty heightVa }"
		/><c:forEach var="heightIdx" begin="6" end="16" step="1"
		><u:option value="${heightIdx*50 }" title="${heightIdx*50 }px" 
				checkValue="${heightVa }"/></c:forEach></select></td>
		</tr>
		</table></td>
		</tr></table>		
	</td>
</tr>
</c:forEach>
</u:listArea>

<u:blank />

<u:title titleId="wl.jsp.consolDt.sub.title" alt="취합기준일" type="small" />
<u:listArea colgroup="15%," noBottomBlank="true">
<c:if test="${!empty tabListMap.day }">
<tr id="dayConsolArea">
	<td class="head_lt"><u:msg titleId="wl.cols.typCd.day" alt="일일" /></td>
	<td class="bodybg_lt"><u:checkArea><u:radio value="today" name="consolDay" titleId="wl.cols.today" alt="오늘" checked="true" /></u:checkArea></td>
</tr></c:if><c:if test="${!empty tabListMap.week }">
<tr id="weekConsolArea">
	<td class="head_lt"><u:msg titleId="wl.cols.typCd.week" alt="주간" /></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="bodyip_lt"><u:msg titleId="wl.cols.weekly" alt="매주"/></td>
		<td class="width10"></td>
		<td><u:checkArea>
			<c:forTokens var="dayOfWeek" items="sun/mon/tue/wed/thu/fri/sat" delims="/" varStatus="status">
				<u:radio name="consolWeek" value="${status.count}" titleId="wl.cols.${dayOfWeek }" checked="${(empty envConfigMap.consolWeek && status.count == 4) || envConfigMap.consolWeek == status.count }" />
			</c:forTokens>
		</u:checkArea></td>
		</tr>
		</table></td>
</tr></c:if><c:if test="${!empty tabListMap.month }">
<tr id="monthConsolArea">
	<td class="head_lt"><u:msg titleId="wl.cols.typCd.month" alt="월간" /></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="bodyip_lt"><u:msg titleId="wl.cols.monthly" alt="매월"/></td>
		<td class="width10"></td>
		<td><select name="consolMonth"><c:forEach var="day" begin="1" end="31" step="1"
		><u:set var="dayVa" test="${day<10 }" value="0${day }" elseValue="${day }"/><u:option value="${dayVa }" title="${day }" 
				checkValue="${envConfigMap.consolMonth }"/></c:forEach></select></td>
		<td class="width10"></td>
		<td class="bodyip_lt"><u:msg titleId="wl.cols.day" alt="일"/></td>
		</tr>
		</table></td>
</tr></c:if><c:if test="${!empty tabListMap.year }">
<tr id="yearConsolArea">
	<td class="head_lt"><u:msg titleId="wl.cols.typCd.year" alt="연간" /></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="bodyip_lt"><u:msg titleId="wl.cols.annually" alt="매년"/></td>
		<td class="width10"></td>
		<td><select name="consolYearMonth"><c:forEach var="month" begin="1" end="12" step="1"
		><u:set var="monthVa" test="${month<10 }" value="0${month }" elseValue="${month }"/><u:option value="${monthVa }" title="${month }" 
				selected="${!empty envConfigMap.consolYear && fn:substring(envConfigMap.consolYear,0,2) eq monthVa }"/></c:forEach></select></td>
		<td class="width10"></td>
		<td class="bodyip_lt"><u:msg titleId="wl.cols.month" alt="월"/></td>
		<td class="width10"></td>
		<td><select name="consolYearDay"><c:forEach var="day" begin="1" end="31" step="1"
		><u:set var="dayVa" test="${day<10 }" value="0${day }" elseValue="${day }"/><u:option value="${dayVa }" title="${day }" 
				selected="${!empty envConfigMap.consolYear && fn:substring(envConfigMap.consolYear,2,4) eq dayVa }"/></c:forEach></select></td>
		<td class="width10"></td>
		<td class="bodyip_lt"><u:msg titleId="wl.cols.day" alt="일"/></td>
		</tr>
		</table></td>
</tr></c:if>
<tr>
	<td class="head_lt"><u:msg titleId="wl.cols.isReprtDisp" alt="보고여부 표시" /></td>
	<td class="bodybg_lt"><u:checkArea><u:radio value="Y" name="isReprtDisp" titleId="cm.option.use" alt="사용" checked="${empty envConfigMap.isReprtDisp}" checkValue="${envConfigMap.isReprtDisp }" 
	/><u:radio value="N" name="isReprtDisp" titleId="cm.option.notUse" alt="사용안함" checkValue="${envConfigMap.isReprtDisp}" /></u:checkArea></td>	
</tr>
</u:listArea>
<u:blank />
</form>
</c:if>
<u:title titleId="wl.jsp.group.reprtGrp.title" alt="보고대상 그룹" type="small" >
<u:titleButton titleId="cm.btn.add" onclick="setReprtGrpPop();" alt="추가"/><u:titleButton titleId="cm.btn.selDel" onclick="delSelUbox('groupList');" alt="선택삭제"/>
</u:title>
<div id="groupList" class="groupdiv" style="height:50px;"><c:forEach 
		var="wlReprtGrpBVo" items="${wlReprtGrpBVoList }" varStatus="status">
		<div class="ubox"><dl><dd 
		class="title"><input type="checkbox" /><a href="javascript:setReprtGrpPop('${wlReprtGrpBVo.grpNo }');">${wlReprtGrpBVo.grpNm }</a><u:input type="hidden" id="grpNo" value="${wlReprtGrpBVo.grpNo }"/></dd>
		<dd class="btn"><a class="delete" onclick="grpSelectDel(this);" href="javascript:void(0);"><span>delete</span></a></dd></dl></div>
		</c:forEach>
</div>
<u:blank />
<c:if test="${!empty tabListMap }">
<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:save();" auth="W" />
</u:buttonArea>
</c:if>
