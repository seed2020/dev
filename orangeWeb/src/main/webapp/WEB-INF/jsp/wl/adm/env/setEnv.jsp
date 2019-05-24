<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<% // 종류선택 %>
function checkTypCd(obj, isInit){
	$('#'+obj.value+'ConsolArea').find('input, select').each(function(){
		if(isInit!=undefined && isInit && obj.checked) return true;
		setDisabled($(this), !obj.checked);	
	});
	
}<%//어권별 리소스 input에 값이 있는지 체크%>
function checkRescVa(trObj, ignoreAllEmpty){
	var i, id, va, handler, arr=[];
	$(trObj).find("td[id^='langTyp'] input").each(function(){ arr.push(this); });
	
	if(ignoreAllEmpty){
		var hasNoVa=true;
		arr.each(function(index, obj){
			if($(obj).val()!=''){
				hasNoVa = false;
				return false;
			}
		});
		if(hasNoVa) return 0;
	}
	
	for(i=0;i<arr.length;i++){
		id = $(arr[i]).attr('id');
		va = $(arr[i]).val();
		handler = validator.getHandler(id);
		if(handler!=null && handler(id, va)==false){
			$(arr[i]).focus();
			return -1;
		}
	}
	return 1;
}<% // 저장 - 버튼 클릭 %>
function save(){
	var typCdList=$("#typCdsArea input[type='checkbox']:checked");
	
	if(typCdList.length==0){		
		alertMsg('wl.msg.typCd.empty'); // wl.msg.typCd.empty=업무일지는 최소 1개 이상이어야 합니다.
		return;
	}
	
	var itemList=$("#setEnvForm [id^='langTypArea']").closest('tr').find('input[type="checkbox"]:checked');
	if(itemList.length==0){
		alertMsg('wl.msg.item.empty'); // wl.msg.item.empty=업무일지 입력 항목은 최소 1개 이상이어야 합니다.
		return;
	}
	
	<%// 어권별 리소스가 있는지 체크함%>
	/* for(var i=1;i<trArr.length-1;i++){
		result = checkRescVa(trArr[i], true);
		if(result<0) return;
		$(trArr[i]).find("[name='valid']").val( (result>0) ? 'Y' : '');
		count += result;
	} */
	
	var typCds='';
	var isYear=false;
	if (validator.validate('setEnvForm')) {
		$.each(typCdList, function(index, obj){
			if(index>0) typCds+='/';
			typCds+=$(obj).val();
			if($(obj).val()=='year') isYear=true;
			setDisabled($(obj), true);
		});
		$("#typCdsArea input[type='checkbox']").attr('disabled',true);
	
		var $form = $("#setEnvForm");
		$form.find("input[name='typCds']").remove();
		$form.appendHidden({name:'typCds',value:typCds});
		
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
		//$("#setEnvForm [id^='langTypArea']").find('input, select').each(function(){
		//	setDisabled($(this), true);	
		//});
	
		$form.attr('method','post');
		$form.attr('action','./transEnv.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form.submit();	
	}
	
};
$(document).ready(function() {
	var typCdList=$("#typCdsArea input[type='checkbox']");
	$.each(typCdList, function(){
		checkTypCd(this, true);
	});
	setUniformCSS();
});
//-->
</script>

<u:title titleId="dm.jsp.setEnv" alt="환경설정" />
<form id="setEnvForm" method="post">
<input type="hidden" name="menuId" value="${menuId}" />

<u:title titleId="wl.jsp.logItem.sub.title" alt="업무일지 항목관리" type="small" />
<c:set var="map" value="${envConfigMap }" scope="request"/>
<u:listArea colgroup="15%," noBottomBlank="true">
<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="wl.cols.typCd.select" alt="종류선택" /></td>
	<td class="bodybg_lt">
		<u:checkArea2 id="typCdsArea">
			<c:forTokens var="typCd" items="${typCdList }" delims="/" varStatus="status">
				<c:set var="checked" value="N"/>
				<c:forTokens var="typCds" items="${envConfigMap.typCds }" delims="/"><c:if test="${typCd eq typCds}"><c:set var="checked" value="Y"/></c:if></c:forTokens>
				<u:checkbox2 id="typCds${status.index }" name="typCd" value="${typCd}" titleId="wl.cols.typCd.${typCd }" checked="${checked eq 'Y' }" onclick="checkTypCd(this);"/>
			</c:forTokens>
		</u:checkArea2>
	</td>
</tr>
<c:forEach items="${colList}" var="colNm" varStatus="status">
<tr>
	<td class="head_lt"><u:msg titleId="wl.cols.${colNm}" alt="실적" /></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td id="langTypArea${colNm}">
		<u:convertMap var="rescId" srcId="map" attId="${colNm}RescId" type="html" />
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:convertMap var="rescVa" srcId="map" attId="${rescId}_${langTypCdVo.cd}" type="html" />
			<u:set test="${status.first}" var="style" value="width:100px;" elseValue="width:100px; display:none" />
			<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
			<u:input id="${colNm}RescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="wl.cols.${colNm}" value="${rescVa}" style="${style}"
				maxByte="30" validator="changeLangSelector('setEnvForm', id, va)" />
		</c:forEach>
		</td>
		<td>
		<c:if test="${fn:length(_langTypCdListByCompId)>1}">
			<select id="langSelector" onchange="changeLangTypCd('setEnvForm','langTypArea${colNm}',this.value)" <u:elemTitle titleId="cols.langTyp" />>
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
			</c:forEach>
			</select>
		</c:if>
		
		<u:input type="hidden" id="${colNm}RescId" value="${rescId }" />
		</td><td class="width10"></td>
		<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><u:convertMap var="heightVa" srcId="map" attId="${colNm}Height" type="html" 
		/><select name="${colNm}Height"><u:option value="" titleId="cols.hghtPx"  selected="${empty heightVa }"
		/><c:forEach var="heightIdx" begin="6" end="16" step="1"
		><u:option value="${heightIdx*50 }" title="${heightIdx*50 }px" 
				checkValue="${heightVa }"/></c:forEach></select></td><td class="width10"></td>
		<u:convertMap var="useYn" srcId="map" attId="${colNm}UseYn" type="html" />
		<u:checkbox value="Y" name="${colNm}UseYn" titleId="cm.option.use" alt="사용" checkValue="${useYn }" />				
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
<tr id="dayConsolArea">
	<td class="head_lt"><u:msg titleId="wl.cols.typCd.day" alt="일일" /></td>
	<td class="bodybg_lt"><u:checkArea><u:radio value="today" name="consolDay" titleId="wl.cols.today" alt="오늘" checked="true" /></u:checkArea></td>
</tr>
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
</tr>
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
</tr>
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
</tr>
<tr>
	<td class="head_lt"><u:msg titleId="wl.cols.isReprtDisp" alt="보고여부 표시" /></td>
	<td class="bodybg_lt"><u:checkArea><u:checkbox value="Y" name="isReprtDisp" titleId="cm.option.use" alt="사용" checked="${envConfigMap.isReprtDisp eq 'Y'}" /></u:checkArea></td>	
</tr>
</u:listArea>

<u:blank />

<u:title titleId="wl.cols.etc" alt="기타" type="small" />
<u:listArea colgroup="15%," noBottomBlank="true">
<tr>
	<td class="head_lt"><u:msg titleId="wl.cols.fileUseYn" alt="첨부파일 사용" /></td>
	<td class="bodybg_lt"><u:checkArea><u:checkbox value="Y" name="fileYn" titleId="cm.option.use" alt="사용" checked="${envConfigMap.fileYn eq 'Y'}" /></u:checkArea></td>	
</tr>
<%-- <tr>
	<td class="head_lt"><u:msg titleId="wl.cols.consolOpenYn" alt="취합일지 공개" /></td>
	<td class="bodybg_lt"><u:checkArea><u:checkbox value="Y" name="consolOpenYn" titleId="cm.option.use" alt="사용" checked="${envConfigMap.consolOpenYn eq 'Y'}" /></u:checkArea></td>
</tr> --%>
<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="wl.cols.deptScope" alt="부서일지 범위" /></td>
	<td class="bodybg_lt"><u:checkArea>
			<c:forTokens var="deptScope" items="log/consol/all" delims="/" varStatus="status">
				<u:radio name="deptSrchOpt" value="${deptScope}" titleId="wl.cols.deptScope.${deptScope }" checked="${envConfigMap.deptSrchOpt eq deptScope }"/>
			</c:forTokens>
		</u:checkArea></td>
</tr>

</u:listArea>

<u:blank />
<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:save();" auth="A" />
</u:buttonArea>

</form>

