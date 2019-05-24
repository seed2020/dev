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

	request.setAttribute("currYmd", com.innobiz.orange.web.cm.utils.StringUtil.getCurrYmd());


%><style type="text/css">
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
<script type="text/javascript">
<!--
$(document).ready(function(){
	$("#formBodyArea").find("input:visible").uniform();
});
//-->
</script>
<div id="xmlArea" style="${formBodyMode eq 'pop' ? 'width:900px;' : ''}">
<div id="xml-head">
	<input type="hidden" name="typId" value="linkedIssDJ1"/>
	<input type="hidden" name="ver" value="1"/>
</div>
<c:if


	test="${formBodyMode ne 'view'}">
<div id="xml-body">
<u:title type="small" title="사고자 인적 사항 (해당 팀장 작성)" />
<u:listArea id="xml-persion" colgroup="13%,13%,11%,13%,13%,37%">
<tr>
	<td class="head_ct">이 름</td>
	<td class="body_lt"><u:input id="erpName" title="이름" value="${formBodyXML.getAttr('body/persion.erpName')}"
		style="width:89%" /></td>
	<td class="head_ct">성 별</td>
	<td class="body_lt"><select name="erpGender" >
		<u:option value="M" title="남성" selected="${formBodyXML.getAttr('body/persion.erpGender') eq 'M'}"/>
		<u:option value="F" title="여성" selected="${formBodyXML.getAttr('body/persion.erpGender') eq 'F'}"/></select></td>
	<td class="head_ct">직 급</td>
	<td class="body_lt"><u:input id="erpTitle" title="이름" value="${formBodyXML.getAttr('body/persion.erpTitle')}"
		style="width:96%" /></td>
</tr>
<tr>
	<td class="head_ct">고 용 상 태</td>
	<td class="body_lt" colspan="3"><u:input id="erpEmpStatus" title="고용상태" value="${formBodyXML.getAttr('body/persion.erpEmpStatus')}"
		style="width:96%" /></td>
	<td class="head_ct">해당업무 경력</td>
	<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0"><tr>
		<td><select name="erpCareerYear"><c:forEach begin="0" end="20" step="1" var="careerYear">
			<u:option value="${careerYear}" title="${careerYear.toString().concat(careerYear<20 ? '' : ' 이상')}"
				checkValue="${formBodyXML.getAttr('body/persion.erpCareerYear')}" /></c:forEach></select></td>
		<td style="padding-left:6px; padding-right:15px">년</td>
		<td><select name="erpCareerMonth"><c:forEach begin="0" end="11" step="1" var="careerMonth">
			<u:option value="${careerMonth}" title="${careerMonth}"
				checkValue="${formBodyXML.getAttr('body/persion.erpCareerMonth')}" /></c:forEach></select></td>
		<td style="padding-left:6px; padding-right:15px">개월</td>
		</tr></table>
		</td>
</tr>
<tr>
	<td class="head_ct">관리 감독자</td>
	<td class="body_lt" colspan="3"><u:input id="erpSupervisor" title="관리 감독자" value="${formBodyXML.getAttr('body/persion.erpSupervisor')}"
		style="width:96%" /></td>
	<td class="head_ct">목 격 자</td>
	<td class="body_lt"><u:input id="erpWitness" title="관리 감독자" value="${formBodyXML.getAttr('body/persion.erpWitness')}"
		style="width:96%" /></td>
</tr>
</u:listArea>

<div class="blank"></div>
<u:title type="small" title="사고 발생 내역 (해당 팀장 작성)" />
<u:listArea id="xml-accident" colgroup="13%,37%,13%,37%">
<tr>
	<td class="head_ct">사 고 일 시</td>
	<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0"><tr>
		<td><u:calendar id="erpAccidentDay" title="사고일시" value="${empty formBodyXML.getAttr('body/accident.erpAccidentDay') ? 'today' : formBodyXML.getAttr('body/accident.erpAccidentDay')}" /></td>
		<td style="padding-left:6px; padding-right:15px">일</td>
		<td><select name="erpAccidentHour"><c:forEach begin="0" end="23" step="1" var="hour">
			<u:option value="${hour<10 ? '0'.concat(hour) : hour}" title="${hour<10 ? '0'.concat(hour) : hour}"
				checkValue="${formBodyXML.getAttr('body/accident.erpAccidentHour')}" /></c:forEach></select></td>
		<td style="padding-left:6px; padding-right:15px">시</td>
		<td><select name="erpAccidentMinute"><c:forEach begin="0" end="45" step="15" var="minute">
			<u:option value="${minute<10 ? '0'.concat(minute) : minute}" title="${minute<10 ? '0'.concat(minute) : minute}"
				checkValue="${formBodyXML.getAttr('body/accident.erpAccidentMinute')}" /></c:forEach></select></td>
		<td style="padding-left:6px; padding-right:15px">분</td></tr></table></td>
	<td class="head_ct">사 고 장 소</td>
	<td class="body_lt"><u:input id="erpPlace" title="사 고 장 소" value="${formBodyXML.getAttr('body/accident.erpPlace')}"
		style="width:96%" /></td>
</tr>
<tr>
	<td class="head_ct">사 고 종 류</td>
	<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0">
	<tr><u:checkbox title="베임" value="Y" name="erpAc01" checkValue="${formBodyXML.getAttr('body/accident.erpAc01')}" />
		<u:checkbox title="끼임" value="Y" name="erpAc02" checkValue="${formBodyXML.getAttr('body/accident.erpAc02')}" />
		<u:checkbox title="충돌" value="Y" name="erpAc03" checkValue="${formBodyXML.getAttr('body/accident.erpAc03')}" /></tr>
	<tr><u:checkbox title="차량, 지게차등 운송수단 관련 사고" value="Y" name="erpAc04" checkValue="${formBodyXML.getAttr('body/accident.erpAc04')}"	
		textColspan="7" /></tr>
	<tr><u:checkbox title="삐임/근골격계" value="Y" name="erpAc05" checkValue="${formBodyXML.getAttr('body/accident.erpAc05')}"
		textColspan="4" />
		<u:checkbox title="말림" value="Y" name="erpAc06" checkValue="${formBodyXML.getAttr('body/accident.erpAc06')}" /></tr>
	<tr><u:checkbox title="미끄러짐/추락" value="Y" name="erpAc07" checkValue="${formBodyXML.getAttr('body/accident.erpAc07')}"
		textColspan="4" />
		<u:checkbox title="화상" value="Y" name="erpAc08" checkValue="${formBodyXML.getAttr('body/accident.erpAc08')}" /></tr>
	<tr><u:checkbox title="화학물질 접촉" value="Y" name="erpAc09" checkValue="${formBodyXML.getAttr('body/accident.erpAc09')}"
		textColspan="4" />
		<u:checkbox title="전기사고" value="Y" name="erpAc10" checkValue="${formBodyXML.getAttr('body/accident.erpAc10')}" /></tr>
	</table></td>
	<td class="head_ct">부 상 부 위</td>
	<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0">
	<tr><u:checkbox title="얼굴" value="Y" name="erpInj01" checkValue="${formBodyXML.getAttr('body/accident.erpInj01')}" />
		<u:checkbox title="이마" value="Y" name="erpInj02" checkValue="${formBodyXML.getAttr('body/accident.erpInj02')}" />
		<u:checkbox title="복부" value="Y" name="erpInj03" checkValue="${formBodyXML.getAttr('body/accident.erpInj03')}" /></tr>
	<tr><u:checkbox title="눈" value="Y" name="erpInj04" checkValue="${formBodyXML.getAttr('body/accident.erpInj04')}" />
		<u:checkbox title="턱" value="Y" name="erpInj05" checkValue="${formBodyXML.getAttr('body/accident.erpInj05')}" />
		<u:checkbox title="흉부" value="Y" name="erpInj06" checkValue="${formBodyXML.getAttr('body/accident.erpInj06')}" /></tr>
	<tr><u:checkbox title="코" value="Y" name="erpInj07" checkValue="${formBodyXML.getAttr('body/accident.erpInj07')}" />
		<u:checkbox title="머리" value="Y" name="erpInj08" checkValue="${formBodyXML.getAttr('body/accident.erpInj08')}" />
		<u:checkbox title="등/허리" value="Y" name="erpInj09" checkValue="${formBodyXML.getAttr('body/accident.erpInj09')}" /></tr>
	<tr><u:checkbox title="입" value="Y" name="erpInj10" checkValue="${formBodyXML.getAttr('body/accident.erpInj10')}" />
		<u:checkbox title="손/발" value="Y" name="erpInj11" checkValue="${formBodyXML.getAttr('body/accident.erpInj11')}" />
		<u:checkbox title="손등/손가락" value="Y" name="erpInj12" checkValue="${formBodyXML.getAttr('body/accident.erpInj12')}" /></tr>
	<tr><u:checkbox title="귀" value="Y" name="erpInj13" checkValue="${formBodyXML.getAttr('body/accident.erpInj13')}" />
		<u:checkbox title="발/다리" value="Y" name="erpInj14" checkValue="${formBodyXML.getAttr('body/accident.erpInj14')}" />
		<u:checkbox title="발등/발가락" value="Y" name="erpInj15" checkValue="${formBodyXML.getAttr('body/accident.erpInj15')}" /></tr>
	</table></td>
</tr>
<tr>
	<td class="head_ct">사 고 내 용</td>
	<td class="body_lt" colspan="3"><textarea name="erpDetail" rows="3" style="width:98.4%"><u:out
		type="textarea" value="${formBodyXML.getAttr('body/accident.erpDetail')}" /></textarea></td>
</tr>
<tr>
	<td class="head_ct">사 고 원 인</td>
	<td class="body_lt" colspan="3"><textarea name="erpReason" rows="3" style="width:98.4%"><u:out
		type="textarea" value="${formBodyXML.getAttr('body/accident.erpReason')}" /></textarea></td>
</tr>
<tr>
	<td class="head_ct">발생일의 경과사항</td>
	<td class="body_lt" colspan="3"><table border="0" cellpadding="0" cellspacing="0"><tr>
		<u:checkbox name="erpRes1" value="Y" title="24시간 이내 미복귀" checkValue="${formBodyXML.getAttr('body/accident.erpRes1')}" />
		<u:checkbox name="erpRes2" value="Y" title="24시간 이내 복귀" checkValue="${formBodyXML.getAttr('body/accident.erpRes2')}" />
		<u:checkbox name="erpRes3" value="Y" title="회사내에서 응급처치" checkValue="${formBodyXML.getAttr('body/accident.erpRes3')}" />
		<u:checkbox name="erpRes4" value="Y" title="아차 사고" checkValue="${formBodyXML.getAttr('body/accident.erpRes4')}" />
		</tr></table></td>
</tr>
</u:listArea>

<div class="blank"></div>
<div class="blank"></div>
<div style="text-align:center; font-size:18px; font-weight:bold; ">위와 같이 사고 발생 내용을 보고 합니다.</div>
</div>
</c:if><c:if



	test="${formBodyMode eq 'view'}">
<div>
<u:title type="small" title="사고자 인적 사항 (해당 팀장 작성)" />
<u:listArea colgroup="13%,13%,11%,13%,13%,37%">
<tr>
	<td class="head_ct">이 름</td>
	<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/persion.erpName')}" /></td>
	<td class="head_ct">성 별</td>
	<td class="body_lt">${formBodyXML.getAttr('body/persion.erpGenderNm')}</td>
	<td class="head_ct">직 급</td>
	<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/persion.erpTitle')}" /></td>
</tr>
<tr>
	<td class="head_ct">고 용 상 태</td>
	<td class="body_lt" colspan="3"><u:out value="${formBodyXML.getAttr('body/persion.erpEmpStatus')}" /></td>
	<td class="head_ct">해당업무 경력</td>
	<td class="body_lt"><c:if
		test="${not empty formBodyXML.getAttr('body/persion.erpCareerYear')}"><span style="padding-right:10px">${
			formBodyXML.getAttr('body/persion.erpCareerYear') eq '20' ? '20 년 이상'
				: formBodyXML.getAttr('body/persion.erpCareerYear').concat(' 년')}</span></c:if><c:if
		test="${not (empty formBodyXML.getAttr('body/persion.erpCareerMonth') or formBodyXML.getAttr('body/persion.erpCareerMonth') eq '0')}"><span style="padding-right:15px">${
			formBodyXML.getAttr('body/persion.erpCareerMonth').concat(' 개월')}</span></c:if></td>
</tr>
<tr>
	<td class="head_ct">관리 감독자</td>
	<td class="body_lt" colspan="3"><u:out value="${formBodyXML.getAttr('body/persion.erpSupervisor')}" /></td>
	<td class="head_ct">목 격 자</td>
	<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/persion.erpWitness')}" /></td>
</tr>
</u:listArea>

<div class="blank"></div>
<u:title type="small" title="사고 발생 내역 (해당 팀장 작성)" />
<u:listArea id="xml-accident" colgroup="13%,37%,13%,37%">
<tr>
	<td class="head_ct">사 고 일 시</td>
	<td class="body_lt"><c:if
		test="${not empty formBodyXML.getAttr('body/accident.erpAccidentDay')}"
			><span style="padding-right:10px">${formBodyXML.getAttr('body/accident.erpAccidentDay')} 년</span></c:if><c:if
		test="${not empty formBodyXML.getAttr('body/accident.erpAccidentHour')}"
			><span style="padding-right:10px">${formBodyXML.getAttr('body/accident.erpAccidentHour')} 시</span></c:if><c:if
		test="${not empty formBodyXML.getAttr('body/accident.erpAccidentMinute')}"
			><span style="padding-right:10px">${formBodyXML.getAttr('body/accident.erpAccidentMinute')} 분</span></c:if></td>
	<td class="head_ct">사 고 장 소</td>
	<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/accident.erpPlace')}" /></td>
</tr>
<tr>
	<td class="head_ct">사 고 종 류</td>
	<td class="body_lt">
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpAc01')}"><nobr>베임,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpAc02')}"><nobr>끼임,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpAc03')}"><nobr>충돌,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpAc04')}"><nobr>차량, 지게차등 운송수단 관련 사고,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpAc05')}"><nobr>삐임/근골격계,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpAc06')}"><nobr>말림,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpAc07')}"><nobr>미끄러짐/추락,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpAc08')}"><nobr>화상,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpAc09')}"><nobr>화학물질 접촉,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpAc10')}"><nobr>전기사고</nobr></c:if></td>
	<td class="head_ct">부 상 부 위</td>
	<td class="body_lt">
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpInj01')}"><nobr>얼굴,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpInj02')}"><nobr>이마,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpInj03')}"><nobr>복부,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpInj04')}"><nobr>눈,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpInj05')}"><nobr>턱,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpInj06')}"><nobr>흉부,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpInj07')}"><nobr>코,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpInj08')}"><nobr>머리,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpInj09')}"><nobr>등/허리,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpInj10')}"><nobr>입,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpInj11')}"><nobr>손/발,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpInj12')}"><nobr>손등/손가락,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpInj13')}"><nobr>귀,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpInj14')}"><nobr>발/다리,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpInj15')}"><nobr>발등/발가락</nobr></c:if></td>
</tr>
<tr>
	<td class="head_ct">사 고 내 용</td>
	<td class="body_lt" colspan="3"><u:out
		value="${formBodyXML.getAttr('body/accident.erpDetail')}" /></td>
</tr>
<tr>
	<td class="head_ct">사 고 원 인</td>
	<td class="body_lt" colspan="3"><u:out
		value="${formBodyXML.getAttr('body/accident.erpReason')}" /></td>
</tr>
<tr>
	<td class="head_ct">발생일의 경과사항</td>
	<td class="body_lt" colspan="3">
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpRes1')}"><nobr>24시간 이내 미복귀,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpRes2')}"><nobr>24시간 이내 복귀,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpRes3')}"><nobr>회사내에서 응급처치,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpRes4')}"><nobr>아차 사고</nobr></c:if></td>
</tr>
</u:listArea>

<div class="blank"></div>
<div class="blank"></div>
<div style="text-align:center; font-size:18px; font-weight:bold; ">위와 같이 사고 발생 내용을 보고 합니다.</div>
</div>
</c:if><c:if
	
	
	test="${formBodyMode eq 'pop'}">
<u:buttonArea>
	<u:button titleId="cm.btn.confirm" onclick="setErpXMLPop();" alt="확인" auth="W" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</c:if>
</div>