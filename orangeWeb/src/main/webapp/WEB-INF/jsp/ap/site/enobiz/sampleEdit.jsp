<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
/*
// -- request.attribute --

	formBodyMode : reg(등록용) / pop(등록용 팝업) / view(조회용) / edit(양식편집)
	formBodyXML
	
// -- javascript --

	checkFormBodyXML
		- xml 수집 전에 호출함, collectFormBodyXML 함수가 있을 경우 호출 안함
		- 사용자 input 유효값 검증
		- return false 면 submit 안함 (메세지 처리 필요함)
	collectFormBodyXML
		- 기본 제공 xml collect 함수 사용하지 않을 경우 사용
		- return : xml
		- return null 이면 submit 안함 (메세지 처리 필요함)

// -- html --
	- 모든 input, select 등 element 의 명은 'erp'로 시작 할 것
	- xml 을 위한 구조
		- 등록용 : xmlArea 로 싸고 그안에 만들 것
		- 구조
			xml-head
			xml-body
				xml-users/user	: '/'가 있으면 - 루프 돌면서 서브 테그 만들고 attr 생성
				xml-period		: '/'가 없으면 - attr 생성
		- 기본 유효값 체크
			루프 항목에서 - data-validation="Y" 된 곳의 값이 없으면 서브 테그 만들지 않음
		
// -- xml(샘플) --
<formBodyXML>
	<head typId="bizTrip" ver="1"/>
	<body>
		<users>
			<user erpGubun="111" erpOrgId="222" erpEin="333" erpPosit="444" erpName="555" erpDuty="666"/>
			<user erpGubun="555" erpOrgId="666" erpEin="77" erpPosit="8" erpName="9" erpDuty="0"/>
		</users>
		<period erpStart="qq" erpEnd="w" erpReason="e"/>
		<schedules>
			<schedule erpDate="aaass" erpFromPlace="ssss" erpToPlace="dddd" erpBizCnt="fff" erpTransfer="gggg"/>
		</schedules>
	</body>
</formBodyXML>

*/
%><script type="text/javascript">
<!--
//<c:if test="${formBodyMode eq 'edit'}">
function editErpUsers(){
	
	<%// html 만들기 %>
	var html = [];
	html.push('<div style="width:510px">');
	html.push('<div style="text-align:center; padding-bottom:6px;">콤마(,) 또는 엔터키로 구별하여 이릅을 입력 하세요.</div>');
	html.push('<textarea rows="12" cols="80" name="erpUsers"></textarea>');
	html.push('<div class="blank"></div>');
	html.push('<div class="button_basic notPrint"><ul>');
	
	html.push('<li class="basic"><a href="javascript:void(0);" onclick="setErpUsers()" title="확인"><span>확인</span></a></li>');
	html.push('<li class="basic"><a href="javascript:void(0);" onclick="dialog.close(this)" title="닫기"><span>닫기</span></a></li>');
	
	html.push('</ul></div>');
	
	html.push('<div class="blank"></div>');
	html.push('</div>');
	
	<%// 팝업창 오픈 %>
	var option = {popHtml:html.join('')};
	dialog.open2('erpUserDialog', '이름 편집', null, option);
	
	<%// 사용자 정보 모으기 %>
	var users = [], va, idx=0;
	$("#xmlArea #xml-body input[name='erpName']").each(function(){
		va = $(this).val().trim();
		if(va != ''){
			if(idx!=0){<%// 5명 단위로 엔터 넣기 %>
				if(idx%5 == 0) { users.push(',\r\n'); }
				else { users.push(', '); }
			}
			users.push(va);
		}
		idx++;
	});
	
	<%// 팝업에 사용자 정보 세팅 %>
	$("#erpUserDialog textarea").val(users.length>0 ? users.join('') : '');
}
function setErpUsers(){
	<%// 팝업의 사용자 정보 가져오기 %>
	var va = $("#erpUserDialog textarea").val();
	va = va.replaceAll('\n', ',');
	va = va.replaceAll('\r', '');
	
	<%// 사용자 정보 배열로 만들기 %>
	var arr = [];
	va.split(',').each(function(index, value){
		value = value.trim();
		if(value!=''){
			arr.push(value);
		}
	});
	
	<%// 삭제할것, 마지막TR 정리 %>
	var $list = $("#xmlArea #xml-body .listarea[id='xml-users/user'] tbody:first");
	var dels = [], lastTr = null;
	$list.children().each(function(){
		if($(this).attr('id') == 'user'){
			dels.push(this);
		}
		if($(this).attr('id') == 'hiddenTr'){
			lastTr = this;
		}
	});
	
	<%// 삭제 %>
	dels.each(function(index, obj){
		$(obj).remove();
	});
	
	var appendHtml = lastTr.outerHTML;
	appendHtml = appendHtml.replace('hiddenTr','user');
	
	var appendObj;
	if(arr.length==0) arr.push('');
	
	<%// 추가 %>
	var seq = 1;
	arr.each(function(idx, va){
		appendObj = $(appendHtml);
		appendObj.find("#seq").text(seq++);
		appendObj.find("#erpNameTxt").text(va);
		appendObj.find("input[name='erpName']").val(va);
		appendObj.insertBefore(lastTr);
		
		appendObj.show();
		appendObj.find("input, textarea, select, button").not(".skipThese").uniform();
	});
	
	dialog.close('erpUserDialog');
}
$(document).ready(function() {
	$("#erpDate").focus().blur();
});
//</c:if>
function checkAllByName(areaId, name){
	var first = true, chked = false;
	$("#"+areaId).find("input[name='"+name+"']").each(function(){
		if(first){
			chked = !this.checked;
			first = false;
		}
		this.checked = chked;
		$(this).uniform.update();
	});
}
function stayOn(obj){
	obj.checked = true;
	$(obj).uniform.update();
}
function stayOff(obj){
	obj.checked = false;
	$(obj).uniform.update();
}
//<c:if test="${formBodyMode eq 'view'}">
$(document).ready(function() {
	$("#xmlArea").find("input:visible").uniform();
});
//</c:if>
-->
</script><c:if

	test="${formBodyMode ne 'view'}">
<div id="xmlArea" style="${formBodyMode eq 'pop' ? 'width:900px;' : ''}">

<div id="xml-head">
	<input type="hidden" name="typId" value="overtime1"/>
	<input type="hidden" name="ver" value="1"/>
</div>

<div style="font-size:12pt; text-align:center; color:#454545; padding:20px;">사규에 의거하여 다음과 같이 특근 신청서를 제출합니다.</div>

<div id="xml-body">
<u:listArea id="xml-date" colgroup="21%,*">
	<tr>
		<td class="head_ct">일자</td>
		<td><u:calendar id="erpDate" title="일자" value="${formBodyXML.getAttr('body/date.erpDate')}" /></td>
	</tr>
</u:listArea>
<u:listArea id="xml-users/user" colgroup="8%,13%,12%,12%,12%,12%,*">
	<tr id="headerTr">
		<th class="head_ct" rowspan="2">순번</th>
		<th class="head_ct" rowspan="2">이름<c:if test="${formBodyMode eq 'edit'}">
			<br/><u:buttonS title="편집" onclick="editErpUsers()" /></c:if></th>
		<th class="head_ct" colspan="2">토요근무</th>
		<th class="head_ct" colspan="2">일요근무</th>
		<th class="head_ct" rowspan="2">비고</th>
	</tr>
	<tr id="headerTr">
		<th class="head_ct"><a href="javascript:checkAllByName('xmlArea', 'erpSatDayWork')">주간</a></th>
		<th class="head_ct"><a href="javascript:checkAllByName('xmlArea', 'erpSatNightWork')">야간</a></th>
		<th class="head_ct"><a href="javascript:checkAllByName('xmlArea', 'erpSunDayWork')">주간</a></th>
		<th class="head_ct"><a href="javascript:checkAllByName('xmlArea', 'erpSunNightWork')">야간</a></th>
	</tr><c:forEach
			items="${formBodyXML.getChildList('body/users', 1, 1)}" var="user" varStatus="status" >
	<tr id="${status.last ? 'hiddenTr' : 'user'}" style="${status.last ? 'display:none' : ''}">
		<td id="seq" class="body_ct">${status.index + 1}</td>
		<td class="body_ct"><span id="erpNameTxt">${user.getAttr('erpName')}</span><input type="hidden" name="erpName" value="${user.getAttr('erpName')}" data-validation="Y" /></td>
		<td class="bodybg_ct"><u:checkbox name="erpSatDayWork" value="Y" checkValue="${user.getAttr('erpSatDayWork')}" /></td>
		<td class="bodybg_ct"><u:checkbox name="erpSatNightWork" value="Y" checkValue="${user.getAttr('erpSatNightWork')}" /></td>
		<td class="bodybg_ct"><u:checkbox name="erpSunDayWork" value="Y" checkValue="${user.getAttr('erpSunDayWork')}" /></td>
		<td class="bodybg_ct"><u:checkbox name="erpSunNightWork" value="Y" checkValue="${user.getAttr('erpSunNightWork')}" /></td>
		<td><input type="text" name="erpRemarks" title="비고" value="${user.getAttr('erpRemarks')}" style="width:94%" /></td>
	</tr></c:forEach>
</u:listArea>
</div>

<div class="blank"></div>

<c:if
	test="${formBodyMode eq 'pop'}">
<u:buttonArea>
	<u:button titleId="cm.btn.confirm" onclick="setErpXMLPop();" alt="확인" auth="W" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</c:if>

</div></c:if><c:if
	test="${formBodyMode eq 'view'}">
<div id="xmlArea">

<div style="font-size:12pt; text-align:center; color:#454545; padding:20px;">사규에 의거하여 다음과 같이 특근 신청서를 제출합니다.</div>

<div id="xml-body">
<u:listArea id="xml-date" colgroup="21%,*">
	<tr>
		<td class="head_ct">일자</td>
		<td class="body_lt">${formBodyXML.getAttr('body/date.erpDate')}</td>
	</tr>
</u:listArea>
<u:listArea id="xml-users/user" colgroup="8%,13%,12%,12%,12%,12%,*">
	<tr id="headerTr">
		<th class="head_ct" rowspan="2">순번</th>
		<th class="head_ct" rowspan="2">이름</th>
		<th class="head_ct" colspan="2">토요근무</th>
		<th class="head_ct" colspan="2">일요근무</th>
		<th class="head_ct" rowspan="2">비고</th>
	</tr>
	<tr id="headerTr">
		<th class="head_ct">주간</th>
		<th class="head_ct">야간</th>
		<th class="head_ct">주간</th>
		<th class="head_ct">야간</th>
	</tr><c:forEach
			items="${formBodyXML.getChildList('body/users', 1)}" var="user" varStatus="status" >
	<tr id="${status.last ? 'hiddenTr' : 'user'}" style="${status.last ? 'display:none' : ''}">
		<td id="seq" class="body_ct">${status.index + 1}</td>
		<td class="body_ct"><span id="erpNameTxt">${user.getAttr('erpName')}</span></td>
		<td class="bodybg_ct"><u:checkbox name="erpSatDayWork" value="Y" checkValue="${user.getAttr('erpSatDayWork')}"
			onclick="${user.getAttr('erpSatDayWork') eq 'Y' ? 'stayOn(this);' : 'stayOff(this);'}" /></td>
		<td class="bodybg_ct"><u:checkbox name="erpSatNightWork" value="Y" checkValue="${user.getAttr('erpSatNightWork')}"
			onclick="${user.getAttr('erpSatNightWork') eq 'Y' ? 'stayOn(this);' : 'stayOff(this);'}" /></td>
		<td class="bodybg_ct"><u:checkbox name="erpSunDayWork" value="Y" checkValue="${user.getAttr('erpSunDayWork')}"
			onclick="${user.getAttr('erpSunDayWork') eq 'Y' ? 'stayOn(this);' : 'stayOff(this);'}" /></td>
		<td class="bodybg_ct"><u:checkbox name="erpSunNightWork" value="Y" checkValue="${user.getAttr('erpSunNightWork')}"
			onclick="${user.getAttr('erpSunNightWork') eq 'Y' ? 'stayOn(this);' : 'stayOff(this);'}" /></td>
		<td class="body_lt"><u:out value="${user.getAttr('erpRemarks')}" /></td>
	</tr></c:forEach>
</u:listArea>
</div>

<div class="blank"></div>

</div>
</c:if>