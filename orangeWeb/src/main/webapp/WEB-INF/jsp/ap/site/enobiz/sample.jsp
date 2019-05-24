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


javascript : clearFormEditor - popup 닫힐때 에디터 초기화
*/
%><c:if
	test="${formBodyMode ne 'view'}">
<div id="xmlArea" style="${formBodyMode eq 'pop' ? 'width:900px;' : ''}">

<div id="xml-head">
	<input type="hidden" name="typId" value="bizTrip"/>
	<input type="hidden" name="ver" value="1"/>
</div>

<div id="xml-body">
<div id="xml-users/user"><c:forEach
		items="${formBodyXML.getChildList('body/users', 3)}" var="user">
	<div id="user">
		<input type="text" name="erpGubun" value="${user.getAttr('erpGubun')}"/>
		<input type="text" name="erpOrgId" value="${user.getAttr('erpOrgId')}" data-validation="Y"/>
		<input type="text" name="erpEin" value="${user.getAttr('erpEin')}"/>
		<input type="text" name="erpPosit" value="${user.getAttr('erpPosit')}"/>
		<input type="text" name="erpName" value="${user.getAttr('erpName')}"/>
		<input type="text" name="erpDuty" value="${user.getAttr('erpDuty')}"/>
	</div></c:forEach>
</div>
<div id="xml-period">
	<input type="text" name="erpStart" value="${formBodyXML.getAttr('body/period.erpStart')}"/>
	<input type="text" name="erpEnd" value="${formBodyXML.getAttr('body/period.erpEnd')}"/>
	<input type="text" name="erpReason" value="${formBodyXML.getAttr('body/period.erpReason')}"/>
</div>
<div id="xml-schedules/schedule"><c:forEach
		items="${formBodyXML.getChildList('body/schedules', 3)}" var="schedule">
	<div id="schedule">
		<input type="text" name="erpDate" value="${schedule.getAttr('erpDate')}" data-validation="Y"/>
		<input type="text" name="erpFromPlace" value="${schedule.getAttr('erpFromPlace')}"/>
		<input type="text" name="erpToPlace" value="${schedule.getAttr('erpToPlace')}" data-validation="Y"/>
		<input type="text" name="erpBizCnt" value="${schedule.getAttr('erpBizCnt')}"/>
		<input type="text" name="erpTransfer" value="${schedule.getAttr('erpTransfer')}"/>
	</div></c:forEach>
</div>
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
<table>
	<tr>
		<td>구분</td>
		<td>부서코드</td>
		<td>사번</td>
		<td>직위</td>
		<td>성명</td>
		<td>직무</td>
	</tr>
	<c:forEach items="${formBodyXML.getChildList('body/users')}" var="user" ><c:if
		test="${not empty user.getAttr('erpOrgId') and not empty user.getAttr('erpName')}">
	<tr>
		<td>${user.getAttr('erpGubun')}</td>
		<td>${user.getAttr('erpOrgId')}</td>
		<td>${user.getAttr('erpEin')}</td>
		<td>${user.getAttr('erpPosit')}</td>
		<td>${user.getAttr('erpName')}</td>
		<td>${user.getAttr('erpDuty')}</td>
	</tr>
	</c:if></c:forEach>
</table>
<br/>

<table>
	<tr>
		<td>시작</td>
		<td>${formBodyXML.getAttr('body/period.erpStart')}</td>
		<td>종료</td>
		<td>${formBodyXML.getAttr('body/period.erpEnd')}</td>
	</tr>
	<tr>
		<td colspan="4">사유</td>
	</tr>
	<tr>
		<td colspan="4">${formBodyXML.getAttr('body/period.erpReason')}</td>
	</tr>
</table>
<br/>

<table>
	<tr>
		<td>일정</td>
		<td>출발지</td>
		<td>도착지</td>
		<td>내용</td>
		<td>교통편</td>
	</tr>
	<c:forEach items="${formBodyXML.getChildList('body/schedules')}" var="schedule" ><c:if
		test="${not empty schedule.getAttr('erpDate') and not empty schedule.getAttr('erpFromPlace')}">
	<tr>
		<td>${schedule.getAttr('erpDate')}</td>
		<td>${schedule.getAttr('erpFromPlace')}</td>
		<td>${schedule.getAttr('erpToPlace')}</td>
		<td>${schedule.getAttr('erpBizCnt')}</td>
		<td>${schedule.getAttr('erpTransfer')}</td>
	</tr>
	</c:if></c:forEach>
</table>
</c:if>