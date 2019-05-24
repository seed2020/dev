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
	<input type="hidden" name="typId" value="linkedIssDJ4"/>
	<input type="hidden" name="ver" value="1"/>
</div>
<c:if


	test="${formBodyMode ne 'view'}">
<div id="xml-body">
<u:title type="small" title="품질관리부서 작성 (발생후 24시간 내에 작성)" />
<u:listArea id="xml-accident" colgroup="13%,37%,13%,37%">
<tr>
	<td class="head_ct">품 종</td>
	<td class="body_lt" colspan="3"><table border="0" cellpadding="0" cellspacing="0">
	<tr><u:checkbox title="배강도유리"  value="Y" name="erpCK101" checkValue="${formBodyXML.getAttr('body/accident.erpCK101')}" />
		<u:checkbox title="강화유리" value="Y" name="erpCK102" checkValue="${formBodyXML.getAttr('body/accident.erpCK102')}" />
		<u:checkbox title="접합유리" value="Y" name="erpCK103" checkValue="${formBodyXML.getAttr('body/accident.erpCK103')}" />
		<u:checkbox title="강화접합유리" value="Y" name="erpCK104" checkValue="${formBodyXML.getAttr('body/accident.erpCK104')}" />
		<u:checkbox title="복층유리" value="Y" name="erpCK105" checkValue="${formBodyXML.getAttr('body/accident.erpCK105')}" />
		<u:checkbox title="SPG 복층유리" value="Y" name="erpCK106" checkValue="${formBodyXML.getAttr('body/accident.erpCK106')}" /></tr>
		</table></td>
</tr>
<tr>
	<td class="head_ct">사 양</td>
	<td class="body_lt"><u:input id="erpVa1" title="사양" value="${formBodyXML.getAttr('body/accident.erpVa1')}"
		style="width:96%" /></td>
	<td class="head_ct">거 래 처</td>
	<td class="body_lt"><u:input id="erpVa2" title="거래처" value="${formBodyXML.getAttr('body/accident.erpVa2')}"
		style="width:96%" /></td>
</tr>
<tr>
	<td class="head_ct">규 격</td>
	<td class="body_lt"><u:input id="erpVa3" title="규격" value="${formBodyXML.getAttr('body/accident.erpVa3')}"
		style="width:96%" /></td>
	<td class="head_ct">현 장 명</td>
	<td class="body_lt"><u:input id="erpVa4" title="현장명" value="${formBodyXML.getAttr('body/accident.erpVa4')}"
		style="width:96%" /></td>
</tr>
<tr>
	<td class="head_ct">총 수 량</td>
	<td class="body_lt"><u:input id="erpVa5" title="총수량" value="${formBodyXML.getAttr('body/accident.erpVa5')}"
		style="width:96%" /></td>
	<td class="head_ct">수 주 번 호</td>
	<td class="body_lt"><u:input id="erpVa6" title="수주번호" value="${formBodyXML.getAttr('body/accident.erpVa6')}"
		style="width:96%" /></td>
</tr>
<tr>
	<td class="head_ct">부 적 합 수 량</td>
	<td class="body_lt"><u:input id="erpVa7" title="부적합 수량" value="${formBodyXML.getAttr('body/accident.erpVa7')}"
		style="width:96%" /></td>
	<td class="head_ct">계 획 납 기</td>
	<td class="body_lt"><u:calendar id="erpVa8" title="계획납기" value="${formBodyXML.getAttr('body/accident.erpVa8')}" /></td>
</tr>
</u:listArea>

<u:listArea id="xml-accident2" colgroup="13%,37%,13%,37%">
<tr>
	<td class="head_ct">부 적 합 내 용<br/>발 생 구 분</td>
	<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0">
	<tr><u:checkbox title="인수검사"  value="Y" name="erpCK201" checkValue="${formBodyXML.getAttr('body/accident2.erpCK201')}" />
		<u:checkbox title="공정/중간검사" value="Y" name="erpCK202" checkValue="${formBodyXML.getAttr('body/accident2.erpCK202')}" />
		<u:checkbox title="최종/제품검사 " value="Y" name="erpCK203" checkValue="${formBodyXML.getAttr('body/accident2.erpCK203')}" /></tr>
		</table></td>
	<td class="head_ct">발 생 일 자</td>
	<td class="body_lt"><u:calendar id="erpVa21" title="발생 일자" value="${formBodyXML.getAttr('body/accident2.erpVa21')}" /></td>
</tr>
<tr>
	<td class="head_ct">부 적 합<br/>발 생 공 정</td>
	<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0">
	<tr><u:checkbox title="영업(수주)" value="Y" textColspan="4"
			name="erpCK301" checkValue="${formBodyXML.getAttr('body/accident2.erpCK301')}" />
		<u:checkbox title="SCM(입력)" value="Y" textColspan="4"
			name="erpCK302" checkValue="${formBodyXML.getAttr('body/accident2.erpCK302')}" /></tr>
	<tr><u:checkbox title="재단" value="Y"
			name="erpCK303" checkValue="${formBodyXML.getAttr('body/accident2.erpCK303')}" />
		<u:checkbox title="전처리" value="Y"
			name="erpCK304" checkValue="${formBodyXML.getAttr('body/accident2.erpCK304')}" />
		<u:checkbox title="강화" value="Y"
			name="erpCK305" checkValue="${formBodyXML.getAttr('body/accident2.erpCK305')}" />
		<u:checkbox title="접합" value="Y"
			name="erpCK306" checkValue="${formBodyXML.getAttr('body/accident2.erpCK306')}" />
		<u:checkbox title="복층 " value="Y"
			name="erpCK307" checkValue="${formBodyXML.getAttr('body/accident2.erpCK307')}" />
		<u:checkbox title="포장" value="Y"
			name="erpCK308" checkValue="${formBodyXML.getAttr('body/accident2.erpCK308')}" /></tr>
		</table></td>
	<td class="head_ct">발 견 자</td>
	<td class="body_lt"><u:input id="erpVa22" title="발견자" value="${formBodyXML.getAttr('body/accident2.erpVa22')}"
		style="width:96%" /></td>
</tr>
<tr>
	<td class="head_ct">부 적 합 내 용<br/>(사진 첨부)</td>
	<td class="body_lt" colspan="3"><textarea name="erpVa23" rows="3" style="width:98.4%"><u:out
		type="textarea" value="${formBodyXML.getAttr('body/accident2.erpVa23')}" /></textarea></td>
</tr>
<tr>
	<td class="head_ct">원 인 분 석</td>
	<td class="body_lt" colspan="3"><textarea name="erpVa24" rows="3" style="width:98.4%"><u:out
		type="textarea" value="${formBodyXML.getAttr('body/accident2.erpVa24')}" /></textarea></td>
</tr>
<tr>
	<td class="head_ct">처 리 내 용</td>
	<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0">
	<tr><u:checkbox title="출고정지 (고객과 협의후 처리 방향 결정)" value="Y"
			name="erpCK401" checkValue="${formBodyXML.getAttr('body/accident2.erpCK401')}" /></tr>
	<tr><u:checkbox title="폐기" value="Y"
			name="erpCK402" checkValue="${formBodyXML.getAttr('body/accident2.erpCK402')}" /></tr>
	<tr><u:checkbox title="부분 활용 (불량발생 유리 폐기 / 정상 짝유리 재사용)" value="Y"
			name="erpCK403" checkValue="${formBodyXML.getAttr('body/accident2.erpCK403')}" /></tr>
		</table></td>
	<td class="head_ct">처 리 후<br/>후 속 초 치</td>
	<td class="body_lt"><textarea name="erpVa25" rows="4" style="width:96%"><u:out
		type="textarea" value="${formBodyXML.getAttr('body/accident2.erpVa25')}" /></textarea></td>
</tr>
<tr>
	<td class="head_ct">시 정 및<br/>예 방 조 치</td>
	<td class="body_lt" colspan="3"><table border="0" cellpadding="0" cellspacing="0">
	<tr><u:checkbox title="필요"  value="Y" name="erpCK501" checkValue="${formBodyXML.getAttr('body/accident2.erpCK501')}" />
		<u:checkbox title="불필요" value="Y" name="erpCK502" checkValue="${formBodyXML.getAttr('body/accident2.erpCK502')}" /></tr>
		</table></td>
</tr>
</u:listArea>

<div class="blank"></div>
<div class="blank"></div>
<div style="text-align:center; font-size:18px; font-weight:bold; ">위와 같이 사고 발생 내용을 보고 합니다.</div>
</div>
</c:if><c:if



	test="${formBodyMode eq 'view'}">
<div id="xml-body">
<u:title type="small" title="품질관리부서 작성 (발생후 24시간 내에 작성)" />
<u:listArea id="xml-accident" colgroup="13%,37%,13%,37%">
<tr>
	<td class="head_ct">품 종</td>
	<td class="body_lt" colspan="3">
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpCK101')}"><nobr>배강도유리,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpCK102')}"><nobr>강화유리,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpCK103')}"><nobr>접합유리,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpCK104')}"><nobr>강화접합유리,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpCK105')}"><nobr>복층유리,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident.erpCK106')}"><nobr>SPG 복층유리</nobr></c:if></td>
</tr>
<tr>
	<td class="head_ct">사 양</td>
	<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/accident.erpVa1')}" /></td>
	<td class="head_ct">거 래 처</td>
	<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/accident.erpVa2')}" /></td>
</tr>
<tr>
	<td class="head_ct">규 격</td>
	<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/accident.erpVa3')}" /></td>
	<td class="head_ct">현 장 명</td>
	<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/accident.erpVa4')}" /></td>
</tr>
<tr>
	<td class="head_ct">총 수 량</td>
	<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/accident.erpVa5')}" /></td>
	<td class="head_ct">수 주 번 호</td>
	<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/accident.erpVa6')}" /></td>
</tr>
<tr>
	<td class="head_ct">부 적 합 수 량</td>
	<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/accident.erpVa7')}" /></td>
	<td class="head_ct">계 획 납 기</td>
	<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/accident.erpVa8')}" /></td>
</tr>
</u:listArea>

<u:listArea id="xml-accident2" colgroup="13%,37%,13%,37%">
<tr>
	<td class="head_ct">부 적 합 내 용<br/>발 생 구 분</td>
	<td class="body_lt">
		<c:if test="${not empty formBodyXML.getAttr('body/accident2.erpCK201')}"><nobr>인수검사,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident2.erpCK202')}"><nobr>공정/중간검사,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident2.erpCK203')}"><nobr>최종/제품검사</nobr></c:if></td>
	<td class="head_ct">발 생 일 자</td>
	<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/accident2.erpVa21')}" /></td>
</tr>
<tr>
	<td class="head_ct">부 적 합<br/>발 생 공 정</td>
	<td class="body_lt">
		<c:if test="${not empty formBodyXML.getAttr('body/accident2.erpCK301')}"><nobr>영업(수주),</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident2.erpCK302')}"><nobr>SCM(입력),</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident2.erpCK303')}"><nobr>재단,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident2.erpCK304')}"><nobr>전처리,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident2.erpCK305')}"><nobr>강화,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident2.erpCK306')}"><nobr>접합,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident2.erpCK307')}"><nobr>복층,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident2.erpCK308')}"><nobr>포장</nobr></c:if></td>
	<td class="head_ct">발 견 자</td>
	<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/accident2.erpVa22')}" /></td>
</tr>
<tr>
	<td class="head_ct">부 적 합 내 용<br/>(사진 첨부)</td>
	<td class="body_lt" colspan="3"><u:out value="${formBodyXML.getAttr('body/accident2.erpVa23')}" /></td>
</tr>
<tr>
	<td class="head_ct">원 인 분 석</td>
	<td class="body_lt" colspan="3"><u:out value="${formBodyXML.getAttr('body/accident2.erpVa24')}" /></td>
</tr>
<tr>
	<td class="head_ct">처 리 내 용</td>
	<td class="body_lt">
		<c:if test="${not empty formBodyXML.getAttr('body/accident2.erpCK401')}"><nobr>출고정지 (고객과 협의후 처리 방향 결정),</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident2.erpCK402')}"><nobr>폐기,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident2.erpCK403')}"><nobr>부분 활용 (불량발생 유리 폐기 / 정상 짝유리 재사용)</nobr></c:if></td>
	<td class="head_ct">처 리 후<br/>후 속 초 치</td>
	<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/accident2.erpVa25')}" /></td>
</tr>
<tr>
	<td class="head_ct">시 정 및<br/>예 방 조 치</td>
	<td class="body_lt" colspan="3">
		<c:if test="${not empty formBodyXML.getAttr('body/accident2.erpCK501')}"><nobr>필요,</nobr></c:if>
		<c:if test="${not empty formBodyXML.getAttr('body/accident2.erpCK502')}"><nobr>불필요</nobr></c:if></td>
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