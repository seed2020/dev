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
	제증명 발급 신청서 : issueReq
*/

//구분
String[][] erpGubuns = {
		{"10", "재직증명서"},
		{"20", "경력증명서"},
		{"30", "기타"}
	};
request.setAttribute("erpGubuns", erpGubuns);

//종류
String[][] erpKnds = {
		{"10", "국문"},
		{"20", "영문"}
	};
request.setAttribute("erpKnds", erpKnds);

//증명서 수력방법
String[][] erpMethods = {
		{"10", "직접수령"},
		{"20", "E-Mail"}
	};
request.setAttribute("erpMethods", erpMethods);

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
<%//사용자 정보 변경 %>
function setUserPop(){
	var data = [];
	searchUserPop({data:data, multi:false, mode:'search'}, function(vo){
		if(vo!=null){
			var $table=$('#xml-body');
			$table.find('input[name="erpUserNm"]').val(vo.rescNm);
			$table.find('input[name="erpUserUid"]').val(vo.userUid);
			$table.find('input[name="erpEntraYmd"]').val(vo.entraYmd);
		}
	});
};<%//xml 수집 전에 호출함 %>
function checkFormBodyXML(){
	<c:if
	test="${formBodyMode ne 'edit'}">
	
	</c:if>
	
	var isGubunChk=false;
	$('#gubunArea input[type="checkbox"]').each(function(){
		if($(this).is(":checked")){
			isGubunChk=true;
			return false;
		}
	});
	if(!isGubunChk){
		alert("구분은 1개이상 필수 선택 사항입니다.");
		return false;
	}
	
	return validator.validate('xml\\-body');
}
//-->
</script></c:if>
<c:if
	test="${formBodyMode ne 'view'}">
<div id="xmlArea" style="${formBodyMode eq 'pop' ? 'width:900px;' : ''}">
<div id="xml-head">
	<input type="hidden" name="typId" value="issueReq"/>
	<input type="hidden" name="ver" value="1"/>
</div>

<div id="xml-body">

<u:listArea id="xml-detail" colgroup="12%,11%,33%,11%,33%"
	><c:if
	test="${formBodyMode ne 'edit'}"><u:set var="erpUserNm" test="${!empty formBodyXML.getAttr('body/leave.erpUserNm')}" value="${formBodyXML.getAttr('body/leave.erpUserNm') }" elseValue="${sessionScope.userVo.userNm }"
	/><u:set var="erpEntraYmd" test="${!empty formBodyXML.getAttr('body/leave.erpEntraYmd')}" value="${formBodyXML.getAttr('body/leave.erpEntraYmd') }" elseValue="${userMap.entraYmd }"
	/><u:set var="erpUserUid" test="${!empty formBodyXML.getAttr('body/leave.erpUserUid')}" value="${formBodyXML.getAttr('body/leave.erpUserUid') }" elseValue="${sessionScope.userVo.userUid }"
	/><u:input type="hidden" id="erpUserUid" value="${erpUserUid }"/></c:if>
	<tr>
		<td class="head_ct"><u:msg title="신청직원" alt="신청직원" /></td>
		<td class="head_lt"><u:mandatory /><u:msg title="신청자" alt="신청자" /></td>
		<td><u:input id="erpUserNm" value="${erpUserNm}" title="신청자" style="width:60%;" maxByte="100" mandatory="Y" readonly="Y" 
		/><u:buttonS alt="찾기" title="찾기" href="javascript:setUserPop();"/></td>
		<td class="head_lt"><u:msg title="입사일" alt="입사일" /></td>
		<td><u:calendar id="erpEntraYmd" name="erpEntraYmd" title="입사일" value="${erpEntraYmd}" mandatory="Y" readonly="Y"/></td>		
	</tr><tr>
		<td class="head_ct" rowspan="6">발급내용</td>
		<td class="head_lt"><u:mandatory /><u:msg title="구분" alt="구분" /></td>
		<td class="body_lt" colspan="3" id="gubunArea"><table border="0" cellpadding="0" cellspacing="0">
	<tr><u:checkbox title="재직증명서" value="Y" name="erpGubun01" checkValue="${formBodyXML.getAttr('body/detail.erpGubun01')}" />
		<u:checkbox title="경력증명서" value="Y" name="erpGubun02" checkValue="${formBodyXML.getAttr('body/detail.erpGubun02')}" />
		<u:checkbox title="기타" value="Y" name="erpGubun03" checkValue="${formBodyXML.getAttr('body/detail.erpGubun03')}" /></tr></table></td>
	</tr><tr>
		<td class="head_lt"><u:msg title="내용" alt="내용" /></td>
		<td colspan="3"><u:input id="erpNote" value="${formBodyXML.getAttr('body/detail.erpNote')}" title="내용" style="width:95%;" maxByte="200" /></td>
	</tr>
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg title="종류" alt="종류" /></td>
		<td class="body_lt"><u:checkArea><c:forEach var="erpKnd" items="${erpKnds }" varStatus="status">
				<u:radio name="erpKnd" value="${erpKnd[0] }" title="${erpKnd[1] }" checkValue="${formBodyXML.getAttr('body/detail.erpKnd') }" checked="${empty formBodyXML.getAttr('body/detail.erpKnd') && status.first ? true : false}" 
				/></c:forEach>
			</u:checkArea></td>
		<td class="head_lt"><u:mandatory /><u:msg title="발급수량" alt="발급수량" /></td>
		<td><select name="erpIssueCnt"><c:forEach var="num" begin="1" end="10" step="1"><u:option value="${num }" checkValue="${formBodyXML.getAttr('body/detail.erpIssueCnt') }" title="${num }"/></c:forEach></select>부</td>		
	</tr><tr>
		<td class="head_lt"><u:msg title="사용처" alt="사용처" /></td>
		<td colspan="3"><u:input id="erpUsePlace" value="${formBodyXML.getAttr('body/detail.erpUsePlace')}" title="사용처" style="width:95%;" maxByte="100" /></td>
	</tr><tr>
		<td class="head_lt"><u:msg title="용도" alt="용도" /></td>
		<td colspan="3"><u:input id="erpUseTxt" value="${formBodyXML.getAttr('body/detail.erpUseTxt')}" title="용도" style="width:95%;" maxByte="100" /></td>
	</tr><tr>
		<td class="head_lt"><u:mandatory /><u:msg title="증명서 수령방법" alt="증명서 수령방법" /></td>
		<td class="body_lt" colspan="3"><u:checkArea><c:forEach var="erpMethod" items="${erpMethods }" varStatus="status">
				<u:radio name="erpMethod" value="${erpMethod[0] }" title="${erpMethod[1] }" checkValue="${formBodyXML.getAttr('body/detail.erpMethod') }" checked="${empty formBodyXML.getAttr('body/detail.erpMethod') && status.first ? true : false}" 
				/></c:forEach>
			</u:checkArea></td>
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
<u:listArea colgroup="12%,11%,33%,11%,33%">
	<tr>
		<td class="head_ct"><u:msg title="신청직원" alt="신청직원" /></td>
		<td class="head_lt"><u:msg title="신청자" alt="신청자" /></td>
		<td class="body_lt"><a href="javascript:;" onclick="viewUserPop('${formBodyXML.getAttr('body/detail.erpUserUid')}');">${formBodyXML.getAttr('body/detail.erpUserNm')}</a></td>
		<td class="head_lt"><u:msg title="입사일" alt="입사일" /></td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/detail.erpEntraYmd')}" type="date"/></td>		
	</tr><tr>
		<td class="head_ct" rowspan="6">발급내용</td>
		<td class="head_lt"><u:msg title="구분" alt="구분" /></td>
		<td class="body_lt" colspan="3" ><table border="0" cellpadding="0" cellspacing="0">
	<tr><u:checkbox title="재직증명서" value="Y" name="erpGubun01" checkValue="${formBodyXML.getAttr('body/detail.erpGubun01')}" disabled="Y"/>
		<u:checkbox title="경력증명서" value="Y" name="erpGubun02" checkValue="${formBodyXML.getAttr('body/detail.erpGubun02')}" disabled="Y"/>
		<u:checkbox title="기타" value="Y" name="erpGubun03" checkValue="${formBodyXML.getAttr('body/detail.erpGubun03')}" disabled="Y"/></tr></table></td>
	</tr><tr>
		<td class="head_lt"><u:msg title="내용" alt="내용" /></td>
		<td class="body_lt" colspan="3"><u:out value="${formBodyXML.getAttr('body/detail.erpNote')}" type="value"/></td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg title="종류" alt="종류" /></td>
		<td class="body_lt"><c:forEach var="erpKnd" items="${erpKnds }" varStatus="status">
		<c:if test="${erpKnd[0] eq formBodyXML.getAttr('body/detail.erpKnd') }">${erpKnd[1] }</c:if></c:forEach>
		</td>
		<td class="head_lt"><u:msg title="발급수량" alt="발급수량" /></td>
		<td class="body_lt">${formBodyXML.getAttr('body/detail.erpIssueCnt') } 부</td>		
	</tr><tr>
		<td class="head_lt"><u:msg title="사용처" alt="사용처" /></td>
		<td class="body_lt" colspan="3"><u:out value="${formBodyXML.getAttr('body/detail.erpUsePlace') }" type="value"/></td>
	</tr><tr>
		<td class="head_lt"><u:msg title="용도" alt="용도" /></td>
		<td class="body_lt" colspan="3"><u:out value="${formBodyXML.getAttr('body/detail.erpUseTxt') }" type="value"/></td>
	</tr><tr>
		<td class="head_lt"><u:msg title="증명서 수령방법" alt="증명서 수령방법" /></td>
		<td class="body_lt" colspan="3"><c:forEach var="erpMethod" items="${erpMethods }" varStatus="status">
		<c:if test="${erpMethod[0] eq formBodyXML.getAttr('body/detail.erpMethod') }">${erpMethod[1] }</c:if></c:forEach>
		</td>
	</tr>
</u:listArea>

</c:if>
