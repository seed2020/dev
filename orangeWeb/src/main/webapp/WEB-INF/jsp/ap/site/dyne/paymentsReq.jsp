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
	경조금 지급 신청서 : paymentsReq
*/

//구분 목록
String[][] erpGubuns = {
		{"10", "결혼"},
		{"20", "출산"},
		{"30", "회갑"},
		{"40", "사망"},
		{"50", "기타"}
	};
request.setAttribute("erpGubuns", erpGubuns);
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
			$table.find('input[name="erpPhone"]').val(vo.mbno);
			$table.find('input[name="erpUserUid"]').val(vo.userUid);
			$table.find('input[name="erpEntraYmd"]').val(vo.entraYmd);
		}
	});
};<%//xml 수집 전에 호출함 %>
function checkFormBodyXML(){
	return validator.validate('xml\\-body');
}
//-->
</script></c:if>
<c:if
	test="${formBodyMode ne 'view'}">
<div id="xmlArea" style="${formBodyMode eq 'pop' ? 'width:900px;' : ''}">
<div id="xml-head">
	<input type="hidden" name="typId" value="paymentsReq"/>
	<input type="hidden" name="ver" value="1"/>
</div>

<div id="xml-body">

<u:listArea id="xml-detail" colgroup="12%,11%,33%,11%,33%"
	><c:if
	test="${formBodyMode ne 'edit'}"><u:set var="erpUserNm" test="${!empty formBodyXML.getAttr('body/leave.erpUserNm')}" value="${formBodyXML.getAttr('body/leave.erpUserNm') }" elseValue="${sessionScope.userVo.userNm }"
	/><u:set var="erpPhone" test="${!empty formBodyXML.getAttr('body/leave.erpPhone')}" value="${formBodyXML.getAttr('body/leave.erpPhone') }" elseValue="${userMap.mbno }"
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
		<td class="head_ct" rowspan="5">경조사항</td>
		<td class="head_lt"><u:mandatory /><u:msg title="구분" alt="구분" /></td>
		<td class="body_lt" colspan="3"><u:checkArea><c:forEach var="erpGubun" items="${erpGubuns }" varStatus="status">
				<u:radio name="erpGubun" value="${erpGubun[0] }" title="${erpGubun[1] }" checkValue="${formBodyXML.getAttr('body/detail.erpGubun') }" checked="${empty formBodyXML.getAttr('body/detail.erpGubun') && status.first ? true : false}" 
				/></c:forEach>
			</u:checkArea></td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg title="관계" alt="관계" /></td>
		<td><u:input id="erpRelation" value="${formBodyXML.getAttr('body/detail.erpRelation')}" title="관계" style="width:95%;" maxByte="100" /></td>
		<td class="head_lt"><u:mandatory /><u:msg title="경조금" alt="경조금" /></td>
		<td><u:input id="erpMoney" value="${formBodyXML.getAttr('body/detail.erpMoney')}" title="경조금" valueOption="number" maxLength="9" mandatory="Y" onfocus="uncommifyInput('erpMoney');" onblur="commifyInput('erpMoney');"/></td>		
	</tr><tr>
		<td class="head_lt"><u:mandatory /><u:msg title="경조일자" alt="경조일자" /></td>
		<td><u:calendar id="erpEventYmd" name="erpEventYmd" title="경조일자" value="${formBodyXML.getAttr('body/detail.erpEventYmd')}" mandatory="Y"/></td>
		<td class="head_lt"><u:mandatory /><u:msg title="자금집행요청일" alt="자금집행요청일" /></td>
		<td><u:calendar id="erpReqYmd" name="erpReqYmd" title="자금집행요청일" value="${formBodyXML.getAttr('body/detail.erpReqYmd')}" mandatory="Y"/></td>		
	</tr><tr>
		<td class="head_lt"><u:msg title="경조장소" alt="경조장소" /></td>
		<td colspan="3"><u:input id="erpPlace" value="${formBodyXML.getAttr('body/detail.erpPlace')}" title="경조장소" style="width:95%;" maxByte="100" /></td>
	</tr><tr>
		<td class="head_lt"><u:msg title="연락처" alt="연락처" /></td>
		<td colspan="3"><u:input id="erpPhone" value="${erpPhone}" title="연락처" style="width:95%;" maxByte="50" /></td>
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
		<td class="head_ct" rowspan="5">경조사항</td>
		<td class="head_lt"><u:msg title="구분" alt="구분" /></td>
		<td class="body_lt" colspan="3"><c:forEach var="erpGubun" items="${erpGubuns }" varStatus="status">
		<c:if test="${erpGubun[0] eq formBodyXML.getAttr('body/detail.erpGubun') }">${erpGubun[1] }</c:if>	</c:forEach></td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg title="관계" alt="관계" /></td>
		<td class="body_lt">${formBodyXML.getAttr('body/detail.erpRelation') }</td>
		<td class="head_lt"><u:msg title="경조금" alt="경조금" /></td>
		<td class="body_lt">${formBodyXML.getAttr('body/detail.erpMoney') }</td>		
	</tr><tr>
		<td class="head_lt"><u:msg title="경조일자" alt="경조일자" /></td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/detail.erpEventYmd')}" type="date"/></td>
		<td class="head_lt"><u:msg title="자금집행요청일" alt="자금집행요청일" /></td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/detail.erpReqYmd')}" type="date"/></td>		
	</tr><tr>
		<td class="head_lt"><u:msg title="경조장소" alt="경조장소" /></td>
		<td class="body_lt" colspan="3"><u:out value="${formBodyXML.getAttr('body/detail.erpPlace')}"/></td>
	</tr><tr>
		<td class="head_lt"><u:msg title="연락처" alt="연락처" /></td>
		<td class="body_lt" colspan="3"><u:out value="${formBodyXML.getAttr('body/detail.erpPhone')}"/></td>
	</tr>
</u:listArea>

</c:if>
