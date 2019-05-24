<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
import="org.springframework.web.context.WebApplicationContext"
import="org.springframework.web.context.support.WebApplicationContextUtils"
import="org.springframework.web.servlet.FrameworkServlet"
import="com.innobiz.orange.web.or.svc.OrCmSvc"
import="com.innobiz.orange.web.pt.secu.LoginSession"
import="com.innobiz.orange.web.pt.secu.UserVo"
import="java.util.Map"
%><%@ page import="java.util.List,java.util.ArrayList,java.util.Map,java.util.HashMap"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext(), 
		FrameworkServlet.SERVLET_CONTEXT_PREFIX+"appServlet"); // "appServlet"는 web.xml의 org.springframework.web.servlet.DispatcherServlet의 servlet-name을 지정.
UserVo userVo = LoginSession.getUser(request);
OrCmSvc orCmSvc = (OrCmSvc)wac.getBean("orCmSvc");
Map<String, Object> map = orCmSvc.getUserMap(userVo.getUserUid(), "ko");
request.setAttribute("userMap", map);
%><%
/*
	출장 신청서 : bizTrip
*/
// 구분 목록
String[][] erpGubuns = {
		{"10", "한화제약(그룹)"},
		{"40", "(주) 네츄럴 라이프"},
		{"50", "(주) 네츄럴라이프아시아"},
		{"60", "양지화학(주)"},
		{"80", "웨이더아시아(주)"}
	};
request.setAttribute("erpGubuns", erpGubuns);

//교통편
String[][] erpTransfers = {
		{"01", "승용차"},
		{"02", "버스"},		
		{"03", "기차"},
		{"04", "항공기"},
		{"05", "기타"}
	};
request.setAttribute("erpTransfers", erpTransfers);

// 출장일정 초기 row수
request.setAttribute("initStrtCnt", "3");

// 날짜 패턴
request.setAttribute("datePattern", "yyyy년MM월dd일");
%>
<style type="text/css">
.titlearea {
    width: 100%;
    height: 16px;
    margin: 0 0 9px 0;
}
.titlearea .tit_left .title_s {
    float: left;
    height: 13px;
    font-weight: bold;
    color: #454545;
}
.listarea {
    float: left;
    width: 100%;
    padding: 0;
    margin: 0;
    color: #454545;
}
.listtable {
    width: 100%;
    background: #bfc8d2;
    color: #454545;
}
.listtable tr {
    background: #ffffff;
}
.listtable .head_ct {
	height: 22px;
    text-align: center;
    background: #ebf1f6;
    line-height: 17px;
    padding: 2px 2px 0 2px;
}
.listtable .head_lt {
    height: 22px;
    background: #ebf1f6;
    line-height: 17px;
    padding: 2px 0 0 4px;
}
.body_ct {
    height: 22px;
    color: #454545;
    text-align: center;
    line-height: 17px;
    padding: 2px 3px 0 3px;
}
.body_lt {
    height: 22px;
    color: #454545;
    line-height: 17px;
    padding: 2px 3px 0 4px;
}
.blank {
    clear: both;
    height: 10px;
}
</style>
<c:if
test="${formBodyMode ne 'view'}">
<script type="text/javascript">
<!--
var erpGubuns=null;
<%//현재 등록된 id 목록 리턴 %>
function getErpGubuns(refVa){
	if(erpGubuns==null) {
		erpGubuns=new ParamMap();
		<c:forEach items="${erpGubuns}" var="erpGubun">
		erpGubuns.put('${erpGubun[0]}', '${erpGubun[1]}');
		</c:forEach>
	}
	return erpGubuns.get(refVa);
}

<%//현재 등록된 id 목록 리턴 %>
function getChkIds(){
	var arr=[];
	$('#xml-users\\/user input[type="checkbox"]').each(function(){
		if($(this).attr("data-userUid")!=undefined && $(this).attr("data-userUid")!=''){
			arr.push($(this).attr("data-userUid"));
		}
	});
	if(arr.length==0){
		return null;
	}
	return arr;
};

<%//사용자 추가 %>
function setUserRows(arr, vas, totalCnt){
	if(arr==null) return;
	if(totalCnt==null) totalCnt=0;
	var $tr, $hiddenTr = $("#xml-users\\/user tbody:first #hiddenTr");
	var html = $hiddenTr[0].outerHTML, erpGubuns;
	arr.each(function(index, vo){
		if(vo.refVa1==null || vo.ein==null) return true; // 구분(회사) or 사번 없으면 추가하지 않는다.
		if(vas==null || !vas.contains(vo.userUid)){
			totalCnt++;
			if(totalCnt>50){
				alert('최대 50명까지 신청 가능합니다.');
				return false;
			}
			erpGubuns=getErpGubuns(vo.refVa1);
			if(erpGubuns==null) return true; // 구분정보가 없으면 continue
			$hiddenTr.before(html);
			$tr = $hiddenTr.prev();
			$tr.attr('id','user');
			$tr.find("input[type='checkbox']").attr("data-userUid", vo.userUid);				
			$tr.find("input[name='erpGubun']").val(vo.refVa1);				
			//$tr.find("input[name='erpGubunNm']").val(erpGubuns);
			//$tr.find("td#erpGubunNm").text(erpGubuns);
			$tr.find("input[name='erpOrgNm']").val(vo.deptRescNm);
			$tr.find("td#erpOrgNm").text(vo.deptRescNm);
			$tr.find("input[name='erpOrgId']").val(vo.orgId);
			$tr.find("input[name='erpUserUid']").val(vo.userUid);
			$tr.find("input[name='erpEin']").val(vo.ein);
			$tr.find("td#erpEin").text(vo.ein);
			$tr.find("input[name='erpPosit']").val(vo.positNm);
			$tr.find("td#erpPosit").text(vo.positNm);
			$tr.find("input[name='erpName']").val(vo.rescNm);
			$tr.find("td#erpName").text(vo.rescNm);
			//$tr.find("input[name='erpDuty']").val(vo.dutyNm);
			//$tr.find("td#erpDuty").text(vo.dutyNm);
			$tr.show();
			setJsUniform($tr[0]);
		}
	});
}

<%// 다음 Row 번호 %>
var gMaxRow = parseInt("${fn:length(formBodyXML.getChildList('body/schedules'))}");

<%//사용자 추가 %>
function addUsers(){
	var data = [];
	var vas = getChkIds();
	if(vas!=null){
		$.each(vas, function(index, userUid){
			data.push({userUid:userUid});
		});	
	}	
	var totalCnt = data.length;
	searchUserPop({data:data, multi:true, mode:'search'}, function(arr){		
		if(arr!=null){
			setUserRows(arr, vas, totalCnt);
			<c:if test="${formBodyMode eq 'pop'}">dialog.resize("setDocBodyHtmlDialog");</c:if>
		}
		
	});
};
<%// 선택삭제%>
function delSelRow(conId){
	var arr = getCheckedTrs(conId, "cm.msg.noSelect");
	if(arr!=null) {
		delRowInArr(arr);
		<c:if test="${formBodyMode eq 'pop'}">dialog.resize("setDocBodyHtmlDialog");</c:if>
	}
}
<%// 삭제 - 배열에 담긴 목록%>
function delRowInArr(rowArr){
	for(var i=0;i<rowArr.length;i++){
		$(rowArr[i]).remove();
	}
}
<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(conId, noSelectMsg){
	var arr=[], id, obj;
	conId=conId.replace('/','\\/');
	$("#"+conId+" tbody:first input[type='checkbox']:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
}
<%// 행추가%>
function addRow(conId, trId, strtCnt){
	if(strtCnt==undefined) strtCnt = 0;
	strtCnt++;
	conId=conId.replace('/','\\/');
	var $tr = $("#"+conId+" tbody:first #hiddenTr");
	var div=$tr.closest('div');
	$.uniform.restore(div.find('input, textarea, select, button'));
	var html = $tr[0].outerHTML;
	html = html.replace(/_NO/g,'_'+(gMaxRow+parseInt(strtCnt)));
	gMaxRow++;
	$tr.before(html);
	$tr = $tr.prev();
	$tr.attr('id',trId);
	$tr.attr('style','');
	//setJsUniform($tr[0]);
	div.find("input, textarea, select, button").uniform();
	<c:if test="${formBodyMode eq 'pop'}">dialog.resize("setDocBodyHtmlDialog");</c:if>
}
<%//xml 수집 전에 호출함 %>
function checkFormBodyXML(){
	var validId='xml-users\\/user';
	
	// 사용자 추가여부
	var validCnt=$("#"+validId+" tbody:first tr").not('#headerTr, #hiddenTr').length;
	if(validCnt==0){
		alert("출장자를 추가해 주세요.");
		return false;
	}
	
	// 출장일정 체크
	var schedules = $("#xml-schedules\\/schedule tbody:first > tr").not('#headerTr, #hiddenTr');
	if(schedules.length==0){
		alert("출장일정을 추가해 주세요.");
		return false;
	}
	validCnt = 0;
	// 출장일정 유효성 검증여부
	$.each(schedules, function(){
		if($(this).find('input[name="erpDate"]').val()==''){
			$(this).find('input[name="erpDate"]').attr('data-validation', 'Y');
			return true; // 첫번째 날짜를 기준으로 유효성 검증
		}
		$(this).find('input[type="text"]').each(function(){
			if($(this).val()==''){
				alert($(this).attr('title')+'을(를) 입력해주세요.');
				validCnt++;
				return false;
			}
		});
	});
	if(validCnt>0){
		return false;
	}
	return validator.validate('xml\\-body');
}

<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
};

$(document).ready(function() {
	$("#xml-body tbody:first").children().each(function(){
		<%// 행추가 영역 제외하고 uniform 적용%>
		if($(this).attr('id')!='hiddenTr'){
			setJsUniform(this);
		}
	});
	
	// 신청자 정보 등록
	<c:if test="${empty formBodyXML.getChildList('body/users') }">
		var arr=[];
		var data = {userUid:'${userMap.userUid}', refVa1:'${userMap.refVa1}', deptRescNm:'${userMap.deptRescNm}', orgId:'${userMap.orgId}', ein:'${userMap.ein}', positNm:'${userMap.positNm}', rescNm:'${userMap.rescNm}'};
		arr.push(data);
		setUserRows(arr, null, null);
	</c:if>
});
//-->
</script></c:if>
<c:if
	test="${formBodyMode ne 'view'}">
<div id="xmlArea" style="${formBodyMode eq 'pop' ? 'width:900px;' : ''}">
<u:title title="출장자" type="small" alt="출장자">
<u:titleButton title="출장자 추가" onclick="addUsers();" alt="출장자 추가"/><u:titleButton title="선택삭제" onclick="delSelRow('xml-users/user');" alt="선택삭제"/>
</u:title>
<div id="xml-head">
	<input type="hidden" name="typId" value="bizTrip"/>
	<input type="hidden" name="ver" value="1"/>
</div>

<div id="xml-body">
<u:listArea id="xml-users/user" colgroup="3%,36%,10%,27%,24%">
	<tr id="headerTr">
		<th class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('xml-users\\/user', this.checked);" value=""/></th>
		<!-- <th class="head_ct">구분</th> -->
		<th class="head_ct">소속</th>
		<th class="head_ct">사번</th>
		<th class="head_ct">직위</th>
		<th class="head_ct">성명</th>
		<!-- <th class="head_ct">직무</th> -->
	</tr>
	<!-- 등록된 목록 -->
	<c:forEach
		begin="0" end="${fn:length(formBodyXML.getChildList('body/users'))}" var="index" varStatus="status"
		><c:set var="user" value="${status.last ? '' : formBodyXML.getChildList('body/users')[index]}"
		/><u:set test="${status.last}" var="trDisp" value="display:none"
		/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="user"
		/>
	<tr id="${trId}" style="${trDisp}">
		<td class="bodybg_ct">	<input type="checkbox" value="" data-userUid="${empty user ? '' :  user.getAttr('erpUserUid')}"
			/><%-- <input type="hidden" name="erpGubunNm" value="${empty user ? '' :  user.getAttr('erpGubunNm') }"
			/> --%><input type="hidden" name="erpGubun" value="${empty user ? '' :  user.getAttr('erpGubun') }"
			/><input type="hidden" name="erpOrgNm" value="${empty user ? '' :  user.getAttr('erpOrgNm')}"
			/><input type="hidden" name="erpOrgId" value="${empty user ? '' :  user.getAttr('erpOrgId')}" data-validation="Y" 
			/><input type="hidden" name="erpUserUid" value="${empty user ? '' :  user.getAttr('erpUserUid')}" 
			/><input type="hidden" name="erpEin" value="${empty user ? '' :  user.getAttr('erpEin')}" 
			/><input type="hidden" name="erpPosit" value="${empty user ? '' :  user.getAttr('erpPosit')}" 
			/><input type="hidden" name="erpName" value="${empty user ? '' :  user.getAttr('erpName')}"
			/><%-- <input type="hidden" name="erpDuty" value="${empty user ? '' :  user.getAttr('erpDuty')}" /> --%>
		</td>
		<%-- <td class="bodybg_ct" id="erpGubunNm">${empty user ? '' :  user.getAttr('erpGubunNm') }</td> --%>
		<td class="bodybg_ct" id="erpOrgNm">${empty user ? '' :  user.getAttr('erpOrgNm')}
		</td>
		<td class="bodybg_ct" id="erpEin">${empty user ? '' :  user.getAttr('erpEin')}</td>
		<td class="bodybg_ct" id="erpPosit">	${empty user ? '' :  user.getAttr('erpPosit')}</td>
		<td class="bodybg_ct" id="erpName">	${empty user ? '' :  user.getAttr('erpName')}</td>
		<%-- <td class="bodybg_ct" id="erpDuty">	${empty user ? '' :  user.getAttr('erpDuty')}</td> --%>
	</tr></c:forEach>
</u:listArea>

<u:title title="출장기간" type="small" alt="출장기간"/>
<u:listArea id="xml-period" colgroup="13%,87%">
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg title="출장기간" alt="출장기간" /></td>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td><u:calendar id="erpStart" name="erpStart" option="{end:'erpEnd'}" title="출장기간 시작일" value="${formBodyXML.getAttr('body/period.erpStart')}" mandatory="Y"/></td>
			<td class="search_body_ct">일 부터</td>
			<td><u:calendar id="erpEnd" name="erpEnd" option="{start:'erpStart'}" title="출장기간 종료일" value="${formBodyXML.getAttr('body/period.erpEnd')}" mandatory="Y"/></td>
			<td class="search_body_ct">일 까지</td>
			</tr>
			</table>
		 </td>
	</tr>
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg title="출장사유" alt="출장사유" /></td>
		<td><u:textarea id="erpReason" value="${formBodyXML.getAttr('body/period.erpReason')}" title="출장사유" maxByte="750" style="width:98%;" rows="10" mandatory="Y"/></td>
	</tr>
</u:listArea>
<u:set var="scheduleStrtCnt" test="${empty formBodyXML.getChildList('body/schedules') }" value="${initStrtCnt }" elseValue="0"/><!-- 출장일정 추가 Row수 -->
<u:title title="출장일정" type="small" alt="출장일정">
<u:titleButton title="일정추가" onclick="addRow('xml-schedules\\/schedule', 'schedule', '${scheduleStrtCnt }');" alt="일정추가"/><u:titleButton title="선택삭제" onclick="delSelRow('xml-schedules/schedule');" alt="선택삭제"/>
</u:title>
<u:listArea id="xml-schedules/schedule" colgroup="3%,17%,15%,15%,30%,20%">
	<tr id="headerTr">
		<th class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('xml-schedules\\/schedule', this.checked);" value=""/></th>
		<th class="head_ct"><u:mandatory />날짜</th>
		<th class="head_ct">출발지</th>
		<th class="head_ct">도착지</th>
		<th class="head_ct">업무내용</th>
		<th class="head_ct">교통편</th>
	</tr>
	<!-- 등록된 목록 -->
	<c:forEach
		begin="0" end="${fn:length(formBodyXML.getChildList('body/schedules'))+scheduleStrtCnt}" var="index" varStatus="status"
		><c:set var="schedule" value="${status.last ? '' : formBodyXML.getChildList('body/schedules')[index]}"
		/><u:set test="${status.last}" var="trDisp" value="display:none"
		/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="schedule"
		/><u:set test="${status.last}" var="scheduleIndex" value="_NO" elseValue="_${status.index+1}"
		/>
	<tr id="${trId}" style="${trDisp}">
		<td class="bodybg_ct">	<input type="checkbox" value=""/></td>
		<td class="bodybg_ct">	<u:calendar id="erpDate${scheduleIndex }" name="erpDate" title="날짜" value="${empty schedule ? '' :  schedule.getAttr('erpDate')}" option="{end:'erpEnd'}"/>
		</td>
		<td class="bodybg_ct">	<u:input id="erpFromPlace${scheduleIndex }" name="erpFromPlace" value="${empty schedule ? '' :  schedule.getAttr('erpFromPlace')}" title="출발지" maxByte="150" /></td>
		<td class="bodybg_ct">	<u:input id="erpToPlace${scheduleIndex }" name="erpToPlace" value="${empty schedule ? '' :  schedule.getAttr('erpToPlace')}" title="도착지" maxByte="150" /></td>
		<td class="bodybg_ct">	<u:input id="erpBizCnt${scheduleIndex }" name="erpBizCnt" value="${empty schedule ? '' :  schedule.getAttr('erpBizCnt')}" title="업무내용" maxByte="150" style="width: 95%;" /></td>
		<td class="bodybg_ct">	<select id="erpTransfer${scheduleIndex }" name="erpTransfer" >
			<c:forEach items="${erpTransfers}" var="erpTransfer" varStatus="erpTransferStatus">
				<u:option value="${erpTransfer[0]}" title="${erpTransfer[1]}" selected="${empty schedule && erpTransferStatus.index==0}" checkValue="${empty schedule ? '' : schedule.getAttr('erpTransfer')}" />
			</c:forEach>
		</select></td>
	</tr>
	</c:forEach>
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
<u:title title="출장자" type="small" alt="출장자" />	
<u:listArea colgroup="39%,10%,27%,24%">
<tr id="headerTr">
	<!-- <th class="head_ct">구분</th> -->
	<th class="head_ct">소속</th>
	<th class="head_ct">사번</th>
	<th class="head_ct">직위</th>
	<th class="head_ct">성명</th>
	<!-- <th class="head_ct">직무</th> -->
</tr>
<c:forEach items="${formBodyXML.getChildList('body/users')}" var="user" ><c:if
		test="${not empty user.getAttr('erpOrgId') and not empty user.getAttr('erpName')}">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
		<%-- <td class="body_ct">${user.getAttr('erpGubunNm')}</td> --%>
		<td class="body_ct">${user.getAttr('erpOrgNm')}</td>
		<td class="body_ct">${user.getAttr('erpEin')}</td>
		<td class="body_ct">${user.getAttr('erpPosit')}</td>
		<td class="body_ct"><a href="javascript:;" onclick="viewUserPop('${user.getAttr('erpUserUid')}');">${user.getAttr('erpName')}</a></td>
		<%-- <td class="body_ct">${user.getAttr('erpDuty')}</td> --%>
	</tr>	</c:if>
</c:forEach>
</u:listArea>
<br/>
<u:title title="출장기간" type="small" alt="출장기간"/>
<u:listArea colgroup="13%,87%">
	<tr>
		<td class="head_lt"><u:msg title="출장기간" alt="출장기간" /></td>
		<td class="body_lt"><fmt:parseDate var="erpStart" value="${formBodyXML.getAttr('body/period.erpStart')}" pattern="yyyy-MM-dd"
		/><fmt:formatDate value="${erpStart}" type="date" dateStyle="long" 
		/> ~ <fmt:parseDate var="erpEnd" value="${formBodyXML.getAttr('body/period.erpEnd')}" pattern="yyyy-MM-dd"
		/><fmt:formatDate value="${erpEnd}" type="date" dateStyle="long" 
		/></td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg title="출장사유" alt="출장사유" /></td>
		<td class="body_lt"><textarea id="erpReason" style="width:98%;overflow:visible;min-height:70px;" readonly="readonly" title="출장사유" rows="10">${formBodyXML.getAttr('body/period.erpReason')}</textarea></td>
	</tr>
</u:listArea>
<br/>

<u:title title="출장일정" type="small" alt="출장일정" />
<u:listArea colgroup="17%,15%,15%,33%,20%">
	<tr id="headerTr">
		<th class="head_ct">날짜</th>
		<th class="head_ct">출발지</th>
		<th class="head_ct">도착지</th>
		<th class="head_ct">업무내용</th>
		<th class="head_ct">교통편</th>
	</tr>
	<c:forEach items="${formBodyXML.getChildList('body/schedules')}" var="schedule" ><c:if
		test="${not empty schedule.getAttr('erpDate') and not empty schedule.getAttr('erpFromPlace')}">
		<tr>
			<td class="body_ct">${schedule.getAttr('erpDate')}</td>
			<td class="body_ct">${schedule.getAttr('erpFromPlace')}</td>
			<td class="body_ct">${schedule.getAttr('erpToPlace')}</td>
			<td class="body_ct">${schedule.getAttr('erpBizCnt')}</td>
			<td class="body_ct"><c:forEach items="${erpTransfers}" var="erpTransfer" varStatus="erpTransferStatus">
				<c:if test="${schedule.getAttr('erpTransfer') eq erpTransfer[0]}">${erpTransfer[1]}</c:if>
			</c:forEach></td>
		</tr>
		</c:if>
	</c:forEach>
</u:listArea>
</c:if>