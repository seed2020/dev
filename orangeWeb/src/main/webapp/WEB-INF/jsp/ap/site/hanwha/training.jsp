<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
import="org.springframework.web.context.WebApplicationContext"
import="org.springframework.web.context.support.WebApplicationContextUtils"
import="org.springframework.web.servlet.FrameworkServlet"
import="com.innobiz.orange.web.or.svc.OrCmSvc"
import="com.innobiz.orange.web.pt.secu.LoginSession"
import="com.innobiz.orange.web.pt.secu.UserVo"
import="java.util.Map"
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
	교육수강 신청서 : training
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

//교통보험 환급금
String[][] erpRefundsYn = {
		{"Y", "대상"},
		{"N", "비 대상"}
	};
request.setAttribute("erpRefundsYn", erpRefundsYn);

//형태
String[][] erpShapeYn = {
		{"Y", "합숙"},
		{"N", "비 합숙"}
	};
request.setAttribute("erpShapeYn", erpShapeYn);

// 교육시간
request.setAttribute("strtSi", "5");
request.setAttribute("endSi", "23");
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
	var returnGubun = erpGubuns.get(refVa);
	return returnGubun;
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
			$tr.show();
			setJsUniform($tr[0]);
		}
	});
}

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

<% // 기간 날짜 조회 %>
function setFromToDate(){
	var erpStart=$('#xml-body').find('input[name="erpStart"]').val();
	var erpEnd=$('#xml-body').find('input[name="erpEnd"]').val();
	if(erpStart=='' || erpEnd=='') return;
	callAjax('/cm/date/getSrchDateListAjx.do?menuId=${menuId}', {start:erpStart, end:erpEnd}, function(data) {
		if (data.message != null){
			alert(data.message);
		}
		if (data.result == 'ok' && data.fromToList!=null) {
			$('#xml-body #erpTotalDay').val(data.fromToList.length);
		}
	});
}

<%//xml 수집 전에 호출함 %>
function checkFormBodyXML(){
	var validId='xml-users\\/user';
	
	// 사용자 추가여부
	var validCnt=$("#"+validId+" tbody:first tr").not('#headerTr, #hiddenTr').length;
	if(validCnt==0){
		alert("참가자를 추가해 주세요.");
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
<u:title title="교육 참가자" type="small" alt="교육 참가자">
<u:titleButton title="참가자 추가" onclick="addUsers();" alt="참가자 추가"/><u:titleButton title="선택삭제" onclick="delSelRow('xml-users/user');" alt="선택삭제"/>
</u:title>
<div id="xml-head">
	<input type="hidden" name="typId" value="training"/>
	<input type="hidden" name="ver" value="1"/>
</div>

<div id="xml-body">
<u:listArea id="xml-users/user" colgroup="3%,36%,10%,27%,24%">
	<tr id="headerTr">
		<th class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('xml-users\\/user', this.checked);" value=""/></th>
		<th class="head_ct">소속</th>
		<th class="head_ct">사번</th>
		<th class="head_ct">직위</th>
		<th class="head_ct">성명</th>
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
			/><input type="hidden" name="erpGubun" value="${empty user ? '' :  user.getAttr('erpGubun') }"
			/><input type="hidden" name="erpOrgNm" value="${empty user ? '' :  user.getAttr('erpOrgNm')}"
			/><input type="hidden" name="erpOrgId" value="${empty user ? '' :  user.getAttr('erpOrgId')}" data-validation="Y" 
			/><input type="hidden" name="erpUserUid" value="${empty user ? '' :  user.getAttr('erpUserUid')}" 
			/><input type="hidden" name="erpEin" value="${empty user ? '' :  user.getAttr('erpEin')}" 
			/><input type="hidden" name="erpPosit" value="${empty user ? '' :  user.getAttr('erpPosit')}" 
			/><input type="hidden" name="erpName" value="${empty user ? '' :  user.getAttr('erpName')}"
			/>
		</td>
		<td class="bodybg_ct" id="erpOrgNm">${empty user ? '' :  user.getAttr('erpOrgNm')}
		</td>
		<td class="bodybg_ct" id="erpEin">${empty user ? '' :  user.getAttr('erpEin')}</td>
		<td class="bodybg_ct" id="erpPosit">	${empty user ? '' :  user.getAttr('erpPosit')}</td>
		<td class="bodybg_ct" id="erpName">	${empty user ? '' :  user.getAttr('erpName')}</td>
	</tr></c:forEach>
</u:listArea>

<u:title title="교육내용" type="small" alt="교육내용"/>
<u:listArea id="xml-detail" colgroup="15%,35%,15%,35%">
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg title="과정명" alt="과정명" /></td>
		<td colspan="3"><u:input id="erpSubj" value="${formBodyXML.getAttr('body/detail.erpSubj')}" title="과정명" style="width:98%;" maxByte="200" mandatory="Y"/></td>
	</tr>
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg title="교육장소" alt="교육장소" /></td>
		<td><u:input id="erpPlace" value="${formBodyXML.getAttr('body/detail.erpPlace')}" title="교육장소" style="width:96%;" maxByte="100" mandatory="Y"/></td>
		<td class="head_lt"><u:mandatory /><u:msg title="교육기관" alt="교육기관" /></td>
		<td><u:input id="erpInstitutions" value="${formBodyXML.getAttr('body/detail.erpInstitutions')}" title="교육기관" style="width:96%;" maxByte="100" mandatory="Y"/></td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg title="수강료" alt="수강료" /></td>
		<td><u:input id="erpTuition" value="${formBodyXML.getAttr('body/detail.erpTuition')}" title="수강료" valueOption="number" maxLength="9" onfocus="uncommifyInput('erpTuition');" onblur="commifyInput('erpTuition');"/></td>
		<td class="head_lt"><u:mandatory /><u:msg title="고용보험 환급금" alt="고용보험 환급금" /></td>
		<td><u:checkArea>
					<u:radio name="erpRefundsYn" value="Y" title="대상" alt="대상" checkValue="${formBodyXML.getAttr('body/detail.erpRefundsYn')}" inputClass="bodybg_lt"/>
					<u:radio name="erpRefundsYn" value="N" title="비 대상" alt="비 대상" checkValue="${formBodyXML.getAttr('body/detail.erpRefundsYn')}" checked="${empty formBodyXML.getAttr('body/detail.erpRefundsYn')}" inputClass="bodybg_lt" />
				</u:checkArea>
		</td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg title="예산" alt="예산" /></td>
		<td><u:input id="erpBudget" value="${formBodyXML.getAttr('body/detail.erpBudget')}" title="예산" valueOption="number" maxLength="9" onfocus="uncommifyInput('erpBudget');" onblur="commifyInput('erpBudget');"/></td>
		<td class="head_lt"><u:mandatory /><u:msg title="형태" alt="형태" /></td>
		<td><u:checkArea>
					<u:radio name="erpShapeYn" value="Y" title="합숙" alt="합숙" checkValue="${formBodyXML.getAttr('body/detail.erpShapeYn')}" inputClass="bodybg_lt"/>
					<u:radio name="erpShapeYn" value="N" title="비 합숙" alt="비 합숙" checkValue="${formBodyXML.getAttr('body/detail.erpShapeYn')}" checked="${empty formBodyXML.getAttr('body/detail.erpShapeYn')}" inputClass="bodybg_lt" />
				</u:checkArea>
		</td>
	</tr>
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg title="교육기간" alt="교육기간" /></td>
		<td colspan="3">
			<table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td><u:calendar id="erpStart" name="erpStart" option="{end:'erpEnd',onchange:setFromToDate}" title="교육기간 시작일" value="${formBodyXML.getAttr('body/detail.erpStart')}" mandatory="Y" /></td>
			<td><u:set var="chkStrtSi" test="${!empty formBodyXML.getAttr('body/detail.erpStartSi')}" value="${formBodyXML.getAttr('body/detail.erpStartSi') }" elseValue="9"/>
			<select name="erpStartSi"><c:forEach var="si" begin="${strtSi }" end="${endSi }" step="1"><u:option value="${si }" title="${si }" checkValue="${chkStrtSi }"/></c:forEach></select></td>
			<td>시 부터</td>
			<td><u:calendar id="erpEnd" name="erpEnd" option="{start:'erpStart',onchange:setFromToDate}" title="교육기간 종료일" value="${formBodyXML.getAttr('body/detail.erpEnd')}" mandatory="Y"/></td>
			<td><u:set var="chkEndSi" test="${!empty formBodyXML.getAttr('body/detail.erpEndSi')}" value="${formBodyXML.getAttr('body/detail.erpEndSi') }" elseValue="18"/>
			<select name="erpEndSi"><c:forEach var="si" begin="${strtSi }" end="${endSi }" step="1"><u:option value="${si }" title="${si }" checkValue="${chkEndSi }"/></c:forEach></select></td>
			<td>시 까지(</td>
			<td><u:input id="erpTotalDay" value="${formBodyXML.getAttr('body/detail.erpTotalDay')}" title="일" valueOption="number" maxLength="3" mandatory="Y" style="width:50px;"/></td>
			<td>일 /</td>
			<td>총 <u:input id="erpTotalTime" value="${formBodyXML.getAttr('body/detail.erpTotalTime')}" title="시간" valueOption="number" maxLength="3" mandatory="Y" style="width:50px;" valueAllowed="."/></td>
			<td>시간</td>
			<td>)</td>
			</tr>
			</table>
		 </td>
	</tr>
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg title="교육 목적" alt="교육 목적" /></td>
		<td colspan="3"><u:textarea id="erpPurpose" value="${formBodyXML.getAttr('body/detail.erpPurpose')}" title="교육목적" maxByte="750" style="width:98%;" rows="10" mandatory="Y"/></td>
	</tr>
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg title="교육 내용" alt="교육 내용" /></td>
		<td colspan="3"><u:textarea id="erpCont" value="${formBodyXML.getAttr('body/detail.erpCont')}" title="교육내용" maxByte="3000" style="width:98%;" rows="10" mandatory="Y"/></td>
	</tr>
</u:listArea>
</div>

<c:if
	test="${formBodyMode eq 'pop'}">
<u:buttonArea>
	<u:button titleId="cm.btn.confirm" onclick="setErpXMLPop();" alt="확인" auth="W" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</c:if>

</div></c:if><c:if
	test="${formBodyMode eq 'view'}">
<u:title title="교육참가자" type="small" alt="교육참가자" />	
<u:listArea colgroup="39%,10%,27%,24%">
<tr id="headerTr">
	<th class="head_ct">소속</th>
	<th class="head_ct">사번</th>
	<th class="head_ct">직위</th>
	<th class="head_ct">성명</th>
</tr>
<c:forEach items="${formBodyXML.getChildList('body/users')}" var="user" ><c:if
		test="${not empty user.getAttr('erpOrgId') and not empty user.getAttr('erpName')}">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
		<td class="body_ct">${user.getAttr('erpOrgNm')}</td>
		<td class="body_ct">${user.getAttr('erpEin')}</td>
		<td class="body_ct">${user.getAttr('erpPosit')}</td>
		<td class="body_ct"><a href="javascript:;" onclick="viewUserPop('${user.getAttr('erpUserUid')}');">${user.getAttr('erpName')}</a></td>
	</tr>	</c:if>
</c:forEach>
</u:listArea>
<br/>

<u:title title="교육내용" type="small" alt="교육내용"/>
<u:listArea colgroup="15%,35%,15%,35%">
	<tr>
		<td class="head_lt"><u:msg title="과정명" alt="과정명" /></td>
		<td class="body_lt" colspan="3">${formBodyXML.getAttr('body/detail.erpSubj')}</td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg title="교육장소" alt="교육장소" /></td>
		<td class="body_lt">${formBodyXML.getAttr('body/detail.erpPlace')}</td>
		<td class="head_lt"><u:msg title="교육기관" alt="교육기관" /></td>
		<td class="body_lt">${formBodyXML.getAttr('body/detail.erpInstitutions')}</td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg title="수강료" alt="수강료" /></td>
		<td class="body_lt">${formBodyXML.getAttr('body/detail.erpTuition')}</td>
		<td class="head_lt"><u:msg title="고용보험 환급금" alt="고용보험 환급금" /></td>
		<td class="body_lt">${formBodyXML.getAttr('body/detail.erpRefundsYn') eq 'Y' ? '대상' : '비 대상'}</td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg title="예산" alt="예산" /></td>
		<td class="body_lt">${formBodyXML.getAttr('body/detail.erpBudget')}</td>
		<td class="head_lt"><u:msg title="형태" alt="형태" /></td>
		<td class="body_lt">${formBodyXML.getAttr('body/detail.erpShapeYn') eq 'Y' ? '합숙' : '비 합숙'}</td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg title="교육기간" alt="교육기간" /></td>
		<td class="body_lt" colspan="3">
			<table border="0" cellpadding="2" cellspacing="0">
			<tr>
			<td><fmt:parseDate var="erpStart" value="${formBodyXML.getAttr('body/detail.erpStart')}" pattern="yyyy-MM-dd"
			/><fmt:formatDate value="${erpStart}" type="date" dateStyle="long" 
			/></td>
			<td>${formBodyXML.getAttr('body/detail.erpStartSi')}</td>
			<td>시 부터</td>
			<td><fmt:parseDate var="erpEnd" value="${formBodyXML.getAttr('body/detail.erpEnd')}" pattern="yyyy-MM-dd"
			/><fmt:formatDate value="${erpEnd}" type="date" dateStyle="long" 
			/></td>
			<td>${formBodyXML.getAttr('body/detail.erpEndSi')}</td>
			<td>시 까지(</td>
			<td>${formBodyXML.getAttr('body/detail.erpTotalDay')}</td>
			<td>일 /</td>
			<td>총 ${formBodyXML.getAttr('body/detail.erpTotalTime')}</td>
			<td>시간</td>
			<td>)</td>
			</tr>
			</table>
		 </td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg title="교육 목적" alt="교육 목적" /></td>
		<td colspan="3"><textarea id="erpPurpose" style="width:98%;" rows="10" readonly="readonly" title="교육목적" >${formBodyXML.getAttr('body/detail.erpPurpose')}</textarea></td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg title="교육 내용" alt="교육 내용" /></td>
		<td colspan="3"><textarea id="erpCont" style="width:98%;" rows="10" readonly="readonly" title="교육목적" >${formBodyXML.getAttr('body/detail.erpCont')}</textarea></td>
	</tr>
</u:listArea>


</c:if>