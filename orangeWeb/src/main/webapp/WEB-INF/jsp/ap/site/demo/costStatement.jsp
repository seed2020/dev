<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
/* 양식명 : 
	원가 계산서
*/

if(!"view".equals(request.getAttribute("formBodyMode"))){
	com.innobiz.orange.web.ap.utils.XMLElement formBodyXML = (com.innobiz.orange.web.ap.utils.XMLElement)request.getAttribute("formBodyXML");
	if(formBodyXML != null){
		if(request.getAttribute("namoEditorEnable")==null){
			String _bodyHtml = com.innobiz.orange.web.cm.utils.EscapeUtil.escapeWriteableHtml(formBodyXML.getAttr("body/cont.erpCont"));
			request.setAttribute("_bodyHtml", _bodyHtml);
		} else {
			request.setAttribute("_bodyHtml", formBodyXML.getAttr("body/cont.erpCont"));
		}
	}
}
%><script src="/js/numeral.min.js" type="text/javascript"></script>
<script src="/js/jquery-calx-2.2.7.min.js" type="text/javascript"></script>
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
</style><script type="text/javascript">
<!--
$(document).ready(function(){
	<c:if test="${formBodyMode ne 'view'}">
	$('#xml-body').calx();
	</c:if>
});
-->
</script><c:if

	test="${formBodyMode ne 'view'}">
<div id="xmlArea" style="${formBodyMode eq 'pop' ? 'width:900px;' : ''}">

<div id="xml-head">
	<input type="hidden" name="typId" value="checkList"/>
	<input type="hidden" name="ver" value="1"/>
</div>

<div id="xml-body">
<div id="xml-default"><table border="0" cellpadding="0" cellspacing="0" style="width:400px;">
<colgroup><col width="*"/><col width="35%"/><col width="15%"/><col width="30%"/></colgroup>
<tr>
<td class="body_lt" style="border-bottom:1px solid black;">생산의뢰번호</td>
<td class="body_ct" style="border-bottom:1px solid black;"><u:input id="erpValue1000" title="생산의뢰번호" style="width:92%;" value="${formBodyXML.getAttr('body/default.erpValue1000')}" maxByte="120"/></td>
<td class="body_lt" style="border-bottom:1px solid black;">영업자</td>
<td class="body_ct" style="border-bottom:1px solid black;"><u:input id="erpValue1001" title="영업자" style="width:92%;" value="${formBodyXML.getAttr('body/default.erpValue1001')}" maxByte="120"/></td>
</tr><tr>
<td class="body_lt" style="border-bottom:1px solid black;">거래선</td>
<td class="body_ct" style="border-bottom:1px solid black;" colspan="3" ><u:input id="erpValue1002" title="거래선" style="width:97%;" value="${formBodyXML.getAttr('body/default.erpValue1002')}" maxByte="120"/></td>
</tr><tr>
<td class="body_lt" style="border-bottom:1px solid black;">품명</td>
<td class="body_ct" style="border-bottom:1px solid black;"><u:input id="erpValue1003" title="품명" style="width:92%;" value="${formBodyXML.getAttr('body/default.erpValue1003')}" maxByte="120"/></td>
<td class="body_lt" style="border-bottom:1px solid black;">규격</td>
<td class="body_ct" style="border-bottom:1px solid black;"><u:input id="erpValue1004" title="규격" style="width:92%;" value="${formBodyXML.getAttr('body/default.erpValue1004')}" maxByte="120"/></td>
</tr><tr>
<td class="body_lt" style="border-bottom:1px solid black;">수량</td>
<td class="body_ct" style="border-bottom:1px solid black;"><u:input id="erpValue1005" title="수량" style="width:92%;text-align:right;" value="${formBodyXML.getAttr('body/default.erpValue1005')}" maxByte="120" dataList="data-cell=\"B6\" data-format=\"0,0[.]00\""/></td>
<td class="body_lt" style="border-bottom:1px solid black;">매수</td>
<td class="body_ct" style="border-bottom:1px solid black;"><u:input id="erpValue1006" title="매수" style="width:92%;" value="${formBodyXML.getAttr('body/default.erpValue1006')}" maxByte="120"/></td>
</tr><tr>
<td class="body_lt" style="border-bottom:1px solid black;">작성일</td>
<td class="body_ct" style="border-bottom:1px solid black;" colspan="3"><u:calendar id="erpValue1007" title="작성일" value="${empty formBodyXML.getAttr('body/default.erpValue1007') and formBodyMode ne 'edit' ? 'today' : formBodyXML.getAttr('body/default.erpValue1007')}" /></td>
</tr>
</table></div><u:blank />
<u:listArea id="xml-detail" colgroup="7%,5%,,5%,6%,6%,6%,6%,6%,6%,6%,8%,7%,8%,6%,7%" >
	<tr>
			<td class="head_ct" colspan="2">제작공정</td>
			<td class="head_ct" colspan="5">자재내역</td>
			<td class="head_ct">매수</td>
			<td class="head_ct">절수</td>
			<td class="head_ct">정미</td>
			<td class="head_ct">여분 3%</td>
			<td class="head_ct">수량</td>
			<td class="head_ct">단 가</td>
			<td class="head_ct">금액</td>
			<td class="head_ct">D/C</td>
			<td class="head_ct">부당</td>
		</tr>
		<tr><c:set var="rowNo" value="10"/>
			<td class="head_ct" rowspan="6">용지</td>
			<td class="body_ct"><u:input id="erpValue13" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue13')}" maxByte="120"/></td>
			<td class="body_ct" colspan="3"><u:input id="erpValue12" title="입력" style="width:80%;" value="${formBodyXML.getAttr('body/detail.erpValue12')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue1" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue1')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue2" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue2')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue3" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue3')}" maxByte="120" dataList="data-cell=\"H${rowNo}\""/></td>
			<td class="body_ct"><u:input id="erpValue4" title="절수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue4')}" maxByte="120" dataList="data-cell=\"I${rowNo}\"" /></td>
			<td class="body_ct"><u:input id="erpValue5" title="정미" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue5')}" maxByte="120" dataList="data-cell=\"J${rowNo}\" data-formula=\"(B6*H${rowNo})/I${rowNo}/500\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue6" title="여분 3%" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue6')}" maxByte="120" dataList="data-cell=\"K${rowNo}\" data-formula=\"J${rowNo}*0.03\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue7" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue7')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"ROUNDUP(K${rowNo}+J${rowNo},2)\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue8" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue8')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue9" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue9')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue10" title="D/C" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue10')}" maxByte="120" dataList="data-cell=\"O${rowNo}\" data-format=\"0%\""/></td>
			<td class="body_ct"><u:input id="erpValue11" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue11')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUNDUP(N${rowNo}/B6,2)\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:input id="erpValue32" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue32')}" maxByte="120"/></td>
			<td class="body_ct" colspan="3"><u:input id="erpValue33" title="입력" style="width:80%;" value="${formBodyXML.getAttr('body/detail.erpValue33')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue21" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue21')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue22" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue22')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue23" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue23')}" maxByte="120" dataList="data-cell=\"H${rowNo}\""/></td>
			<td class="body_ct"><u:input id="erpValue24" title="절수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue24')}" maxByte="120" dataList="data-cell=\"I${rowNo}\"" /></td>
			<td class="body_ct"><u:input id="erpValue25" title="정미" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue25')}" maxByte="120" dataList="data-cell=\"J${rowNo}\" data-formula=\"(B6*H${rowNo})/I${rowNo}/500\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue26" title="여분 3%" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue26')}" maxByte="120" dataList="data-cell=\"K${rowNo}\" data-formula=\"J${rowNo}*0.03\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue27" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue27')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"ROUNDUP(K${rowNo}+J${rowNo},2)\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue28" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue28')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue29" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue29')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue30" title="D/C" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue30')}" maxByte="120" dataList="data-cell=\"O${rowNo}\" data-format=\"0%\""/></td>
			<td class="body_ct"><u:input id="erpValue31" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue31')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:input id="erpValue52" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue52')}" maxByte="120"/></td>
			<td class="body_ct" colspan="3"><u:input id="erpValue53" title="입력" style="width:80%;" value="${formBodyXML.getAttr('body/detail.erpValue53')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue41" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue41')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue42" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue42')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue43" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue43')}" maxByte="120" dataList="data-cell=\"H${rowNo}\""/></td>
			<td class="body_ct"><u:input id="erpValue44" title="절수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue44')}" maxByte="120" dataList="data-cell=\"I${rowNo}\"" /></td>
			<td class="body_ct"><u:input id="erpValue45" title="정미" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue45')}" maxByte="120" dataList="data-cell=\"J${rowNo}\" data-formula=\"(B6*H${rowNo})/I${rowNo}/500\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue46" title="여분 3%" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue46')}" maxByte="120" dataList="data-cell=\"K${rowNo}\" data-formula=\"J${rowNo}*0.03\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue47" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue47')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"ROUNDUP(K${rowNo}+J${rowNo},2)\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue48" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue48')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue49" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue49')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue50" title="D/C" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue50')}" maxByte="120" dataList="data-cell=\"O${rowNo}\" data-format=\"0%\""/></td>
			<td class="body_ct"><u:input id="erpValue51" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue51')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:input id="erpValue72" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue72')}" maxByte="120"/></td>
			<td class="body_ct" colspan="3"><u:input id="erpValue73" title="입력" style="width:80%;" value="${formBodyXML.getAttr('body/detail.erpValue73')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue61" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue61')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue62" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue62')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue63" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue63')}" maxByte="120" dataList="data-cell=\"H${rowNo}\""/></td>
			<td class="body_ct"><u:input id="erpValue64" title="절수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue64')}" maxByte="120" dataList="data-cell=\"I${rowNo}\"" /></td>
			<td class="body_ct"><u:input id="erpValue65" title="정미" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue65')}" maxByte="120" dataList="data-cell=\"J${rowNo}\" data-formula=\"(B6*H${rowNo})/I${rowNo}/500\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue66" title="여분 3%" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue66')}" maxByte="120" dataList="data-cell=\"K${rowNo}\" data-formula=\"J${rowNo}*0.03\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue67" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue67')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"ROUNDUP(K${rowNo}+J${rowNo},2)\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue68" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue68')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue69" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue69')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue70" title="D/C" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue70')}" maxByte="120" dataList="data-cell=\"O${rowNo}\" data-format=\"0%\""/></td>
			<td class="body_ct"><u:input id="erpValue71" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue71')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:input id="erpValue112" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue112')}" maxByte="120"/></td>
			<td class="body_ct" colspan="3"><u:input id="erpValue113" title="입력" style="width:80%;" value="${formBodyXML.getAttr('body/detail.erpValue113')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue101" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue101')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue102" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue102')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue103" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue103')}" maxByte="120" dataList="data-cell=\"H${rowNo}\""/></td>
			<td class="body_ct"><u:input id="erpValue104" title="절수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue104')}" maxByte="120" dataList="data-cell=\"I${rowNo}\"" /></td>
			<td class="body_ct"><u:input id="erpValue105" title="정미" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue105')}" maxByte="120" dataList="data-cell=\"J${rowNo}\" data-formula=\"(B6*H${rowNo})/I${rowNo}/500\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue106" title="여분 3%" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue106')}" maxByte="120" dataList="data-cell=\"K${rowNo}\" data-formula=\"J${rowNo}*0.03\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue107" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue107')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"ROUNDUP(K${rowNo}+J${rowNo},2)\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue108" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue108')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue109" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue109')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue110" title="D/C" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue110')}" maxByte="120" dataList="data-cell=\"O${rowNo}\" data-format=\"0%\""/></td>
			<td class="body_ct"><u:input id="erpValue111" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue111')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:input id="erpValue132" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue132')}" maxByte="120"/></td>
			<td class="body_ct" colspan="3"><u:input id="erpValue133" title="입력" style="width:80%;" value="${formBodyXML.getAttr('body/detail.erpValue133')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue121" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue121')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue122" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue122')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue123" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue123')}" maxByte="120" dataList="data-cell=\"H${rowNo}\""/></td>
			<td class="body_ct"><u:input id="erpValue124" title="절수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue124')}" maxByte="120" dataList="data-cell=\"I${rowNo}\"" /></td>
			<td class="body_ct"><u:input id="erpValue125" title="정미" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue125')}" maxByte="120" dataList="data-cell=\"J${rowNo}\" data-formula=\"(B6*H${rowNo})/I${rowNo}/500\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue126" title="여분 3%" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue126')}" maxByte="120" dataList="data-cell=\"K${rowNo}\" data-formula=\"J${rowNo}*0.03\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue127" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue127')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"ROUNDUP(K${rowNo}+J${rowNo},2)\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue128" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue128')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue129" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue129')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue130" title="D/C" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue130')}" maxByte="120" dataList="data-cell=\"O${rowNo}\" data-format=\"0%\""/></td>
			<td class="body_ct"><u:input id="erpValue131" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue131')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" colspan="7">소계</td>
			<td class="body_ct"><u:input id="erpValue134" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue134')}" maxByte="120" dataList="data-cell=\"H${rowNo}\" data-formula=\"SUM(H10:H15)\" data-format=\"0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue135" title="정미" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue135')}" maxByte="120" dataList="data-cell=\"J${rowNo}\" data-formula=\"SUM(J10:J15)\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue136" title="여분 3%" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue136')}" maxByte="120" dataList="data-cell=\"K${rowNo}\" data-formula=\"SUM(K10:K15)\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue137" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue137')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"SUM(L10:L15)\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue138" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue138')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-formula=\"SUM(M10:M15)\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue139" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue139')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"SUM(N10:N15)\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue140" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue140')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"SUM(P10:P15)\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" rowspan="6">편집/출력</td>
			<td class="body_ct"><u:input id="erpValue141" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue141')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue142" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue142')}" maxByte="120" dataList="data-cell=\"C${rowNo}\""/></td>
			<td class="body_ct"><u:input id="erpValue143" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue143')}" maxByte="120"/></td>
			<td class="body_ct" colspan="2"><u:input id="erpValue144" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue144')}" maxByte="120" dataList="data-cell=\"E${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"><u:input id="erpValue145" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue145')}" maxByte="120" dataList="data-cell=\"G${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue146" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue146')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"(E${rowNo}+G${rowNo})*C${rowNo}\" data-format=\"0\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue147" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue147')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\"" /></td>
			<td class="body_ct"><u:input id="erpValue148" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue148')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue149" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue149')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:input id="erpValue151" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue151')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue152" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue152')}" maxByte="120" dataList="data-cell=\"C${rowNo}\""/></td>
			<td class="body_ct"><u:input id="erpValue153" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue153')}" maxByte="120"/></td>
			<td class="body_ct" colspan="2"><u:input id="erpValue154" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue154')}" maxByte="120" dataList="data-cell=\"E${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"><u:input id="erpValue155" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue155')}" maxByte="120" dataList="data-cell=\"G${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue156" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue156')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"(E${rowNo}+G${rowNo})*C${rowNo}\" data-format=\"0\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue157" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue157')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\"" /></td>
			<td class="body_ct"><u:input id="erpValue158" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue158')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue159" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue159')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:input id="erpValue161" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue161')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue162" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue162')}" maxByte="120" dataList="data-cell=\"C${rowNo}\""/></td>
			<td class="body_ct"><u:input id="erpValue163" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue163')}" maxByte="120"/></td>
			<td class="body_ct" colspan="2"><u:input id="erpValue164" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue164')}" maxByte="120" dataList="data-cell=\"E${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"><u:input id="erpValue165" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue165')}" maxByte="120" dataList="data-cell=\"G${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue166" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue166')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"(E${rowNo}+G${rowNo})*C${rowNo}\" data-format=\"0\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue167" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue167')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\"" /></td>
			<td class="body_ct"><u:input id="erpValue168" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue168')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue169" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue169')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:input id="erpValue171" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue171')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue172" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue172')}" maxByte="120" dataList="data-cell=\"C${rowNo}\""/></td>
			<td class="body_ct"><u:input id="erpValue173" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue173')}" maxByte="120"/></td>
			<td class="body_ct" colspan="2"><u:input id="erpValue174" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue174')}" maxByte="120" dataList="data-cell=\"E${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"><u:input id="erpValue175" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue175')}" maxByte="120" dataList="data-cell=\"G${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue176" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue176')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"(E${rowNo}+G${rowNo})*C${rowNo}\" data-format=\"0\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue177" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue177')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\"" /></td>
			<td class="body_ct"><u:input id="erpValue178" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue178')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue179" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue179')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:input id="erpValue181" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue181')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue182" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue182')}" maxByte="120" dataList="data-cell=\"C${rowNo}\""/></td>
			<td class="body_ct"><u:input id="erpValue183" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue183')}" maxByte="120"/></td>
			<td class="body_ct" colspan="2"><u:input id="erpValue184" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue184')}" maxByte="120" dataList="data-cell=\"E${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"><u:input id="erpValue185" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue185')}" maxByte="120" dataList="data-cell=\"G${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue186" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue186')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"(E${rowNo}+G${rowNo})*C${rowNo}\" data-format=\"0\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue187" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue187')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\"" /></td>
			<td class="body_ct"><u:input id="erpValue188" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue188')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue189" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue189')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="6">PS판</td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue191" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue191')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"SUM(L17:L21)\" data-format=\"0\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue192" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue192')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\"" /></td>
			<td class="body_ct"><u:input id="erpValue193" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue193')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M22*L22\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue194" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue194')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" colspan="7">소계</td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue195" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue195')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"SUM(L17:L21)\" data-format=\"0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue196" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue196')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"SUM(N17:N22)\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue197" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue197')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"SUM(P17:P22)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" rowspan="6">인쇄</td>
			<td class="body_ct"><u:input id="erpValue201" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue201')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue202" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue202')}" maxByte="120" /></td>
			<td class="body_ct"><u:input id="erpValue203" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue203')}" maxByte="120"/></td>
			<td class="body_ct" colspan="2"><u:input id="erpValue204" title="입력" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue204')}" maxByte="120" dataList="data-cell=\"E${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"><u:input id="erpValue205" title="입력" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue205')}" maxByte="120" dataList="data-cell=\"G${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"><u:input id="erpValue206" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue206')}" maxByte="120" dataList="data-cell=\"H${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"><u:input id="erpValue207" title="절수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue207')}" maxByte="120" dataList="data-cell=\"I${rowNo}\" data-format=\"0\"" /></td>
			<td class="body_ct"><u:input id="erpValue208" title="정미" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue208')}" maxByte="120" dataList="data-cell=\"J${rowNo}\" data-formula=\"(B6*H${rowNo})/I${rowNo}/500\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue209" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue209')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"J${rowNo}\" data-format=\"0,0[.]00\"" readonly="Y" /></td>
			<td class="body_ct"><u:input id="erpValue210" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue210')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue211" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue211')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"(E${rowNo}+G${rowNo})*L${rowNo}*M${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue212" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue212')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:input id="erpValue221" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue221')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue222" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue222')}" maxByte="120" /></td>
			<td class="body_ct"><u:input id="erpValue223" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue223')}" maxByte="120"/></td>
			<td class="body_ct" colspan="2"><u:input id="erpValue224" title="입력" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue224')}" maxByte="120" dataList="data-cell=\"E${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"><u:input id="erpValue225" title="입력" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue225')}" maxByte="120" dataList="data-cell=\"G${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"><u:input id="erpValue226" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue226')}" maxByte="120" dataList="data-cell=\"H${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"><u:input id="erpValue227" title="절수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue227')}" maxByte="120" dataList="data-cell=\"I${rowNo}\" data-format=\"0\"" /></td>
			<td class="body_ct"><u:input id="erpValue228" title="정미" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue228')}" maxByte="120" dataList="data-cell=\"J${rowNo}\" data-formula=\"(B6*H${rowNo})/I${rowNo}/500\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue229" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue229')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"J${rowNo}\" data-format=\"0,0[.]00\"" readonly="Y" /></td>
			<td class="body_ct"><u:input id="erpValue230" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue230')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue231" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue231')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"(E${rowNo}+G${rowNo})*L${rowNo}*M${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue232" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue232')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:input id="erpValue241" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue241')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue242" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue242')}" maxByte="120" /></td>
			<td class="body_ct"><u:input id="erpValue243" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue243')}" maxByte="120"/></td>
			<td class="body_ct" colspan="2"><u:input id="erpValue244" title="입력" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue244')}" maxByte="120" dataList="data-cell=\"E${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"><u:input id="erpValue245" title="입력" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue245')}" maxByte="120" dataList="data-cell=\"G${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"><u:input id="erpValue246" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue246')}" maxByte="120" dataList="data-cell=\"H${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"><u:input id="erpValue247" title="절수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue247')}" maxByte="120" dataList="data-cell=\"I${rowNo}\" data-format=\"0\"" /></td>
			<td class="body_ct"><u:input id="erpValue248" title="정미" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue248')}" maxByte="120" dataList="data-cell=\"J${rowNo}\" data-formula=\"(B6*H${rowNo})/I${rowNo}/500\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue249" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue249')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"J${rowNo}\" data-format=\"0,0[.]00\"" readonly="Y" /></td>
			<td class="body_ct"><u:input id="erpValue250" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue250')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue251" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue251')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"(E${rowNo}+G${rowNo})*L${rowNo}*M${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue252" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue252')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:input id="erpValue261" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue261')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue262" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue262')}" maxByte="120" /></td>
			<td class="body_ct"><u:input id="erpValue263" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue263')}" maxByte="120"/></td>
			<td class="body_ct" colspan="2"><u:input id="erpValue264" title="입력" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue264')}" maxByte="120" dataList="data-cell=\"E${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"><u:input id="erpValue265" title="입력" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue265')}" maxByte="120" dataList="data-cell=\"G${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"><u:input id="erpValue266" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue266')}" maxByte="120" dataList="data-cell=\"H${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"><u:input id="erpValue267" title="절수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue267')}" maxByte="120" dataList="data-cell=\"I${rowNo}\" data-format=\"0\"" /></td>
			<td class="body_ct"><u:input id="erpValue268" title="정미" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue268')}" maxByte="120" dataList="data-cell=\"J${rowNo}\" data-formula=\"(B6*H${rowNo})/I${rowNo}/500\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue269" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue269')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"J${rowNo}\" data-format=\"0,0[.]00\"" readonly="Y" /></td>
			<td class="body_ct"><u:input id="erpValue270" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue270')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue271" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue271')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"(E${rowNo}+G${rowNo})*L${rowNo}*M${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue272" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue272')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:input id="erpValue281" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue281')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue282" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue282')}" maxByte="120" /></td>
			<td class="body_ct"><u:input id="erpValue283" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue283')}" maxByte="120"/></td>
			<td class="body_ct" colspan="2"><u:input id="erpValue284" title="입력" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue284')}" maxByte="120" dataList="data-cell=\"E${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"><u:input id="erpValue285" title="입력" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue285')}" maxByte="120" dataList="data-cell=\"G${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"><u:input id="erpValue286" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue286')}" maxByte="120" dataList="data-cell=\"H${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"><u:input id="erpValue287" title="절수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue287')}" maxByte="120" dataList="data-cell=\"I${rowNo}\" data-format=\"0\"" /></td>
			<td class="body_ct"><u:input id="erpValue288" title="정미" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue288')}" maxByte="120" dataList="data-cell=\"J${rowNo}\" data-formula=\"(B6*H${rowNo})/I${rowNo}/500\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue289" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue289')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"J${rowNo}\" data-format=\"0,0[.]00\"" readonly="Y" /></td>
			<td class="body_ct"><u:input id="erpValue290" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue290')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue291" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue291')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"(E${rowNo}+G${rowNo})*L${rowNo}*M${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue292" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue292')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:input id="erpValue301" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue301')}" maxByte="120"/></td>
			<td class="body_ct"><u:input id="erpValue302" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue302')}" maxByte="120" /></td>
			<td class="body_ct"><u:input id="erpValue303" title="입력" style="width:60%;" value="${formBodyXML.getAttr('body/detail.erpValue303')}" maxByte="120"/></td>
			<td class="body_ct" colspan="2"><u:input id="erpValue304" title="입력" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue304')}" maxByte="120" dataList="data-cell=\"E${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"><u:input id="erpValue305" title="입력" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue305')}" maxByte="120" dataList="data-cell=\"G${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"><u:input id="erpValue306" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue306')}" maxByte="120" dataList="data-cell=\"H${rowNo}\" data-format=\"0\""/></td>
			<td class="body_ct"><u:input id="erpValue307" title="절수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue307')}" maxByte="120" dataList="data-cell=\"I${rowNo}\" data-format=\"0\"" /></td>
			<td class="body_ct"><u:input id="erpValue308" title="정미" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue308')}" maxByte="120" dataList="data-cell=\"J${rowNo}\" data-formula=\"(B6*H${rowNo})/I${rowNo}/500\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue309" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue309')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"J${rowNo}\" data-format=\"0,0[.]00\"" readonly="Y" /></td>
			<td class="body_ct"><u:input id="erpValue310" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue310')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue311" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue311')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"(E${rowNo}+G${rowNo})*L${rowNo}*M${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue312" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue312')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" colspan="7">소계</td>
			<td class="body_ct"><u:input id="erpValue314" title="매수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue314')}" maxByte="120" dataList="data-cell=\"H${rowNo}\" data-formula=\"SUM(H24:H29)\" data-format=\"0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue315" title="정미" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue315')}" maxByte="120" dataList="data-cell=\"J${rowNo}\" data-formula=\"SUM(J24:J29)\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue316" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue316')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"SUM(L24:L29)\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue317" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue317')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"SUM(N24:N29)\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue318" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue318')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"SUM(P24:P29)\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" rowspan="9">표지</td>
			<td class="body_ct" colspan="2">가공방법</td>
			<td class="body_ct" colspan="3"><u:input id="erpValue320" title="가공방법" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue320')}" maxByte="120" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct">원단</td>
			<td class="body_ct"><u:input id="erpValue321" title="원단" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue321')}" maxByte="120" /></td>
			<td class="body_ct" colspan="4"><u:input id="erpValue322" title="원단" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue322')}" maxByte="120" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue323" title="절수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue323')}" maxByte="120" dataList="data-cell=\"I${rowNo}\"" /></td>
			<td class="body_ct"><u:input id="erpValue324" title="정미" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue324')}" maxByte="120" dataList="data-cell=\"J${rowNo}\" data-formula=\"B6/I${rowNo}\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue325" title="여분 3%" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue325')}" maxByte="120" dataList="data-cell=\"K${rowNo}\" data-formula=\"J${rowNo}*0.03\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue326" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue326')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"K${rowNo}+J${rowNo}\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue327" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue327')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue328" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue328')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue329" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue329')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct">원단</td>
			<td class="body_ct"><u:input id="erpValue331" title="원단" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue331')}" maxByte="120" /></td>
			<td class="body_ct" colspan="4"><u:input id="erpValue332" title="원단" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue332')}" maxByte="120" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue333" title="절수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue333')}" maxByte="120" dataList="data-cell=\"I${rowNo}\"" /></td>
			<td class="body_ct"><u:input id="erpValue334" title="정미" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue334')}" maxByte="120" dataList="data-cell=\"J${rowNo}\" data-formula=\"B6/I${rowNo}\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue335" title="여분 3%" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue335')}" maxByte="120" dataList="data-cell=\"K${rowNo}\" data-formula=\"J${rowNo}*0.03\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue336" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue336')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"K${rowNo}+J${rowNo}\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue337" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue337')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue338" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue338')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue339" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue339')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct">원단</td>
			<td class="body_ct"><u:input id="erpValue341" title="원단" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue341')}" maxByte="120" /></td>
			<td class="body_ct" colspan="4"><u:input id="erpValue342" title="원단" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue342')}" maxByte="120" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue343" title="절수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue343')}" maxByte="120" dataList="data-cell=\"I${rowNo}\"" /></td>
			<td class="body_ct"><u:input id="erpValue344" title="정미" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue344')}" maxByte="120" dataList="data-cell=\"J${rowNo}\" data-formula=\"B6/I${rowNo}\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue345" title="여분 3%" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue345')}" maxByte="120" dataList="data-cell=\"K${rowNo}\" data-formula=\"J${rowNo}*0.03\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue346" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue346')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"K${rowNo}+J${rowNo}\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue347" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue347')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue348" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue348')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue349" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue349')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:input id="erpValue351" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue351')}" maxByte="120" /></td>
			<td class="body_ct" colspan="4"><u:input id="erpValue352" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue352')}" maxByte="120" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue353" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue353')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-format=\"0,0.00\""/></td>
			<td class="body_ct"><u:input id="erpValue354" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue354')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue355" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue355')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue356" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue356')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:input id="erpValue361" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue361')}" maxByte="120" dataList="data-cell=\"B${rowNo}\""/></td>
			<td class="body_ct" colspan="4"><u:input id="erpValue362" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue362')}" maxByte="120" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue363" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue363')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"IF(B${rowNo}='','',B6)\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue364" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue364')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue365" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue365')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue366" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue366')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:input id="erpValue371" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue371')}" maxByte="120" dataList="data-cell=\"B${rowNo}\"" /></td>
			<td class="body_ct" colspan="4"><u:input id="erpValue372" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue372')}" maxByte="120" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue373" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue373')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"IF(B${rowNo}='','',B6)\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue374" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue374')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue375" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue375')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue376" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue376')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:input id="erpValue381" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue381')}" maxByte="120" dataList="data-cell=\"B${rowNo}\"" /></td>
			<td class="body_ct" colspan="4"><u:input id="erpValue382" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue382')}" maxByte="120" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue383" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue383')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"IF(B${rowNo}='','',B6)\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue384" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue384')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue385" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue385')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue386" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue386')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:input id="erpValue391" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue391')}" maxByte="120" dataList="data-cell=\"B${rowNo}\"" /></td>
			<td class="body_ct" colspan="4"><u:input id="erpValue392" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue392')}" maxByte="120" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue393" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue393')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"IF(B${rowNo}='','',B6)\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue394" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue394')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue395" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue395')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue396" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue396')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" colspan="7">소계</td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue397" title="정미" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue397')}" maxByte="120" dataList="data-cell=\"J${rowNo}\" data-formula=\"SUM(J32:J39)\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue398" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue398')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"SUM(L32:L39)\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue399" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue399')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"SUM(N32:N39)\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue400" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue400')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"SUM(P32:P39)\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" rowspan="5">기타 외주가공</td>
			<td class="body_ct" colspan="2"><u:input id="erpValue401" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue401')}" maxByte="120" /></td>
			<td class="body_ct" colspan="4"><u:input id="erpValue402" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue402')}" maxByte="120" dataList="data-cell=\"D${rowNo}\"" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:input id="erpValue421" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue421')}" maxByte="120" /></td>
			<td class="body_ct" colspan="4"><u:input id="erpValue422" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue422')}" maxByte="120" dataList="data-cell=\"D${rowNo}\"" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue423" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue423')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"IF(D${rowNo}='','',J13)\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue424" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue424')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue425" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue425')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue426" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue426')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:input id="erpValue441" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue441')}" maxByte="120" /></td>
			<td class="body_ct" colspan="4"><u:input id="erpValue442" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue442')}" maxByte="120" dataList="data-cell=\"D${rowNo}\"" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue443" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue443')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"IF(D${rowNo}='','',J13)\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue444" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue444')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue445" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue445')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue446" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue446')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:input id="erpValue461" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue461')}" maxByte="120" /></td>
			<td class="body_ct" colspan="4"><u:input id="erpValue462" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue462')}" maxByte="120" dataList="data-cell=\"D${rowNo}\"" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue463" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue463')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"IF(D${rowNo}='','',J13)\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue464" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue464')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue465" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue465')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue466" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue466')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:input id="erpValue481" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue481')}" maxByte="120" /></td>
			<td class="body_ct" colspan="4"><u:input id="erpValue482" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue482')}" maxByte="120" dataList="data-cell=\"D${rowNo}\"" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue483" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue483')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"IF(D${rowNo}='','',J13)\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue484" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue484')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue485" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue485')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue486" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue486')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" colspan="7">소계</td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue496" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue496')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"SUM(L41:L45)\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue497" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue497')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"SUM(N41:N45)\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue498" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue498')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"SUM(P41:P45)\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" rowspan="5">제본부자재</td>
			<td class="body_ct" colspan="2"><u:input id="erpValue501" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue501')}" maxByte="120" /></td>
			<td class="body_ct" colspan="4"><u:input id="erpValue502" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue502')}" maxByte="120" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue503" title="절수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue503')}" maxByte="120" dataList="data-cell=\"I${rowNo}\" data-format=\"0,0[.]00\"" /></td>
			<td class="body_ct"><u:input id="erpValue504" title="정미" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue504')}" maxByte="120" dataList="data-cell=\"J${rowNo}\" data-formula=\"B6*I${rowNo}\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue505" title="여분 3%" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue505')}" maxByte="120" dataList="data-cell=\"K${rowNo}\" data-formula=\"J${rowNo}*0.03\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue506" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue506')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"K${rowNo}+J${rowNo}\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue507" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue507')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue508" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue508')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue509" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue509')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:input id="erpValue521" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue521')}" maxByte="120" /></td>
			<td class="body_ct" colspan="4"><u:input id="erpValue522" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue522')}" maxByte="120" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue523" title="절수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue523')}" maxByte="120" dataList="data-cell=\"I${rowNo}\" data-format=\"0,0[.]00\"" /></td>
			<td class="body_ct"><u:input id="erpValue524" title="정미" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue524')}" maxByte="120" dataList="data-cell=\"J${rowNo}\" data-formula=\"B6*I${rowNo}\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue525" title="여분 3%" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue525')}" maxByte="120" dataList="data-cell=\"K${rowNo}\" data-formula=\"J${rowNo}*0.03\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue526" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue526')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"K${rowNo}+J${rowNo}\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue527" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue527')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue528" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue528')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue529" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue529')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:input id="erpValue541" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue541')}" maxByte="120" /></td>
			<td class="body_ct" colspan="4"><u:input id="erpValue542" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue542')}" maxByte="120" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue543" title="절수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue543')}" maxByte="120" dataList="data-cell=\"I${rowNo}\" data-format=\"0,0[.]00\"" /></td>
			<td class="body_ct"><u:input id="erpValue544" title="정미" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue544')}" maxByte="120" dataList="data-cell=\"J${rowNo}\" data-formula=\"B6*I${rowNo}\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue545" title="여분 3%" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue545')}" maxByte="120" dataList="data-cell=\"K${rowNo}\" data-formula=\"J${rowNo}*0.03\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue546" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue546')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"K${rowNo}+J${rowNo}\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue547" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue547')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue548" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue548')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue549" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue549')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:input id="erpValue561" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue561')}" maxByte="120" /></td>
			<td class="body_ct" colspan="4"><u:input id="erpValue562" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue562')}" maxByte="120" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue563" title="절수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue563')}" maxByte="120" dataList="data-cell=\"I${rowNo}\" data-format=\"0,0[.]00\"" /></td>
			<td class="body_ct"><u:input id="erpValue564" title="정미" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue564')}" maxByte="120" dataList="data-cell=\"J${rowNo}\" data-formula=\"B6*I${rowNo}\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue565" title="여분 3%" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue565')}" maxByte="120" dataList="data-cell=\"K${rowNo}\" data-formula=\"J${rowNo}*0.03\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue566" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue566')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"K${rowNo}+J${rowNo}\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue567" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue567')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue568" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue568')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue569" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue569')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:input id="erpValue581" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue581')}" maxByte="120" /></td>
			<td class="body_ct" colspan="4"><u:input id="erpValue582" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue582')}" maxByte="120" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue583" title="절수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue583')}" maxByte="120" dataList="data-cell=\"I${rowNo}\" data-format=\"0,0[.]00\"" /></td>
			<td class="body_ct"><u:input id="erpValue584" title="정미" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue584')}" maxByte="120" dataList="data-cell=\"J${rowNo}\" data-formula=\"B6*I${rowNo}\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue585" title="여분 3%" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue585')}" maxByte="120" dataList="data-cell=\"K${rowNo}\" data-formula=\"J${rowNo}*0.03\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue586" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue586')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"K${rowNo}+J${rowNo}\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue587" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue587')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue588" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue588')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue589" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue589')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" colspan="7">소계</td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue601" title="정미" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue601')}" maxByte="120" dataList="data-cell=\"J${rowNo}\" data-formula=\"SUM(J47:J51)\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue602" title="여분 3%" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue602')}" maxByte="120" dataList="data-cell=\"K${rowNo}\" data-formula=\"SUM(K47:K51)\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue603" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue603')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"SUM(L47:L51)\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue604" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue604')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"SUM(N47:N51)\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue605" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue605')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"SUM(P47:P51)\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" rowspan="5">제본</td>
			<td class="body_ct" colspan="2"><u:input id="erpValue621" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue621')}" maxByte="120" dataList="data-cell=\"B${rowNo}\"" /></td>
			<td class="body_ct" colspan="4"><u:input id="erpValue622" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue622')}" maxByte="120" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue623" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue623')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"IF(B${rowNo}='','',B6)\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue624" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue624')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue625" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue625')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue626" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue626')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:input id="erpValue641" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue641')}" maxByte="120" dataList="data-cell=\"B${rowNo}\"" /></td>
			<td class="body_ct" colspan="4"><u:input id="erpValue642" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue642')}" maxByte="120" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue643" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue643')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"IF(B${rowNo}='','',B6)\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue644" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue644')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue645" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue645')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue646" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue646')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:input id="erpValue661" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue661')}" maxByte="120" dataList="data-cell=\"B${rowNo}\"" /></td>
			<td class="body_ct" colspan="4"><u:input id="erpValue662" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue662')}" maxByte="120" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue663" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue663')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"IF(B${rowNo}='','',B6)\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue664" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue664')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue665" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue665')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue666" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue666')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:input id="erpValue681" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue681')}" maxByte="120" dataList="data-cell=\"B${rowNo}\"" /></td>
			<td class="body_ct" colspan="4"><u:input id="erpValue682" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue682')}" maxByte="120" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue683" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue683')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"IF(B${rowNo}='','',B6)\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue684" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue684')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue685" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue685')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue686" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue686')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:input id="erpValue701" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue701')}" maxByte="120" dataList="data-cell=\"B${rowNo}\"" /></td>
			<td class="body_ct" colspan="4"><u:input id="erpValue702" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue702')}" maxByte="120" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue703" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue703')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"IF(B${rowNo}='','',B6)\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue704" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue704')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue705" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue705')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue706" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue706')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" colspan="7">소계</td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue721" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue721')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"SUM(L53:L57)\" data-format=\"0,0.00\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue722" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue722')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"SUM(N53:N57)\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue723" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue723')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"SUM(P53:P57)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" rowspan="4">포장 배송</td>
			<td class="body_ct" rowspan="2"><u:input id="erpValue741" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue741')}" maxByte="120" /></td>
			<td class="body_ct"><u:input id="erpValue742" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue742')}" maxByte="120" /></td>
			<td class="body_ct" colspan="4"><u:input id="erpValue743" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue743')}" maxByte="120" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue743" title="절수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue743')}" maxByte="120" dataList="data-cell=\"I${rowNo}\"" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue744" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue744')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"B6/I${rowNo}\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue745" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue745')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue746" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue746')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue747" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue747')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:input id="erpValue761" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue741')}" maxByte="120" /></td>
			<td class="body_ct" colspan="4"><u:input id="erpValue762" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue762')}" maxByte="120" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue763" title="절수" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue763')}" maxByte="120" dataList="data-cell=\"I${rowNo}\"" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue764" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue764')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"B6/I${rowNo}\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue765" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue765')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue766" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue766')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue767" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue767')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:input id="erpValue781" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue781')}" maxByte="120" /></td>
			<td class="body_ct" colspan="4"><u:input id="erpValue782" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue782')}" maxByte="120" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue783" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue783')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"B6\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue784" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue784')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue785" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue785')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue786" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue786')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:input id="erpValue801" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue801')}" maxByte="120" /></td>
			<td class="body_ct" colspan="4"><u:input id="erpValue802" title="입력" style="width:70%;" value="${formBodyXML.getAttr('body/detail.erpValue802')}" maxByte="120" /></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue803" title="수량" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue803')}" maxByte="120" dataList="data-cell=\"L${rowNo}\" data-formula=\"L60\" data-format=\"0,0[.]00\"" readonly="Y"/></td>
			<td class="body_ct"><u:input id="erpValue804" title="단가" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue804')}" maxByte="120" dataList="data-cell=\"M${rowNo}\" data-format=\"0,0\""/></td>
			<td class="body_ct"><u:input id="erpValue805" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue805')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"M${rowNo}*L${rowNo}\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue806" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue806')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"ROUND(N${rowNo}/B6,2)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" colspan="7">소계</td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue821" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue821')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"SUM(N59:N62)\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue822" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue822')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"SUM(P59:P62)\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" rowspan="4">분석</td>
			<td class="body_lt" colspan="2">1.납품예정일 :</td>
			<td class="body_ct" colspan="4"><u:calendar id="erpValue841" title="납품예정일" value="${formBodyXML.getAttr('body/detail.erpValue841')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct">원가총액</td>
			<td class="body_ct"><u:input id="erpValue842" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue842')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"SUM(N63,N58,N52,N46,N40,N30,N23,N16)\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue843" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue843')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"N64/B6\" data-format=\"0,0.00\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_lt" colspan="2">2.원고승인예정일 :</td>
			<td class="body_ct" colspan="4"><u:calendar id="erpValue861" title="납품예정일" value="${formBodyXML.getAttr('body/detail.erpValue861')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct">수주총액</td>
			<td class="body_ct"><u:input id="erpValue862" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue862')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"P65*B6\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue863" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue863')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-format=\"0,0.00\""/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2">　</td>
			<td class="body_ct" colspan="4"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct">차액</td>
			<td class="body_ct"><u:input id="erpValue881" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue881')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"N65-N64\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue882" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue882')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"(N65-N64)/N65\" data-format=\"0%\"" readonly="Y"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2">　</td>
			<td class="body_ct" colspan="4">　</td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct">가득액</td>
			<td class="body_ct"><u:input id="erpValue901" title="금액" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue901')}" maxByte="120" dataList="data-cell=\"N${rowNo}\" data-formula=\"SUM(N17:N20)+SUM(N24:N27)+SUM(N53:N56)+N61+N66\" data-format=\"0,0\"" readonly="Y"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:input id="erpValue902" title="부당" style="width:70%;text-align:right;" value="${formBodyXML.getAttr('body/detail.erpValue902')}" maxByte="120" dataList="data-cell=\"P${rowNo}\" data-formula=\"N67/N65\" data-format=\"0%\"" readonly="Y"/></td>
		</tr>
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

<div id="xml-body">
<div id="xml-default"><table border="0" cellpadding="0" cellspacing="0" style="width:400px;">
<colgroup><col width="*"/><col width="35%"/><col width="15%"/><col width="30%"/></colgroup>
<tr>
<td class="body_lt" style="border-bottom:1px solid black;">생산의뢰번호</td>
<td class="body_ct" style="border-bottom:1px solid black;"><u:out value="${formBodyXML.getAttr('body/default.erpValue1000')}"/></td>
<td class="body_lt" style="border-bottom:1px solid black;">영업자</td>
<td class="body_ct" style="border-bottom:1px solid black;"><u:out value="${formBodyXML.getAttr('body/default.erpValue1001')}"/></td>
</tr><tr>
<td class="body_lt" style="border-bottom:1px solid black;">거래선</td>
<td class="body_ct" style="border-bottom:1px solid black;" colspan="3" ><u:out value="${formBodyXML.getAttr('body/default.erpValue1002')}"/></td>
</tr><tr>
<td class="body_lt" style="border-bottom:1px solid black;">품명</td>
<td class="body_ct" style="border-bottom:1px solid black;"><u:out value="${formBodyXML.getAttr('body/default.erpValue1003')}"/></td>
<td class="body_lt" style="border-bottom:1px solid black;">규격</td>
<td class="body_ct" style="border-bottom:1px solid black;"><u:out value="${formBodyXML.getAttr('body/default.erpValue1004')}"/></td>
</tr><tr>
<td class="body_lt" style="border-bottom:1px solid black;">수량</td>
<td class="body_ct" style="border-bottom:1px solid black;"><u:out value="${formBodyXML.getAttr('body/default.erpValue1005')}"/></td>
<td class="body_lt" style="border-bottom:1px solid black;">매수</td>
<td class="body_ct" style="border-bottom:1px solid black;"><u:out value="${formBodyXML.getAttr('body/default.erpValue1006')}"/></td>
</tr><tr>
<td class="body_lt" style="border-bottom:1px solid black;">작성일</td>
<td class="body_ct" style="border-bottom:1px solid black;" colspan="3"><u:out value="${formBodyXML.getAttr('body/default.erpValue1007')}"/></td>
</tr>
</table></div><u:blank />
<u:listArea id="xml-detail" colgroup="7%,5%,,5%,6%,6%,6%,6%,6%,6%,6%,8%,7%,8%,6%,7%" >
	<tr>
			<td class="head_ct" colspan="2">제작공정</td>
			<td class="head_ct" colspan="5">자재내역</td>
			<td class="head_ct">매수</td>
			<td class="head_ct">절수</td>
			<td class="head_ct">정미</td>
			<td class="head_ct">여분 3%</td>
			<td class="head_ct">수량</td>
			<td class="head_ct">단 가</td>
			<td class="head_ct">금액</td>
			<td class="head_ct">D/C</td>
			<td class="head_ct">부당</td>
		</tr>
		<tr><c:set var="rowNo" value="10"/>
			<td class="head_ct" rowspan="6">용지</td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue13')}"/></td>
			<td class="body_ct" colspan="3"><u:out value="${formBodyXML.getAttr('body/detail.erpValue12')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue1')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue2')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue3')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue4')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue5')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue6')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue7')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue8')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue9')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue10')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue11')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue32')}"/></td>
			<td class="body_ct" colspan="3"><u:out value="${formBodyXML.getAttr('body/detail.erpValue33')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue21')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue22')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue23')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue24')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue25')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue26')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue27')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue28')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue29')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue30')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue31')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue52')}"/></td>
			<td class="body_ct" colspan="3"><u:out value="${formBodyXML.getAttr('body/detail.erpValue53')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue41')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue42')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue43')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue44')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue45')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue46')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue47')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue48')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue49')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue50')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue51')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue72')}"/></td>
			<td class="body_ct" colspan="3"><u:out value="${formBodyXML.getAttr('body/detail.erpValue73')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue61')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue62')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue63')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue64')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue65')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue66')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue67')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue68')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue69')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue70')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue71')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue112')}"/></td>
			<td class="body_ct" colspan="3"><u:out value="${formBodyXML.getAttr('body/detail.erpValue113')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue101')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue102')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue103')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue104')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue105')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue106')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue107')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue108')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue109')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue110')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue111')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue132')}"/></td>
			<td class="body_ct" colspan="3"><u:out value="${formBodyXML.getAttr('body/detail.erpValue133')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue121')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue122')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue123')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue124')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue125')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue126')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue127')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue128')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue129')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue130')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue131')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" colspan="7">소계</td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue134')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue135')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue136')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue137')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue138')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue139')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue140')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" rowspan="6">편집/출력</td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue141')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue142')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue143')}"/></td>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue144')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue145')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue146')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue147')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue148')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue149')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue151')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue152')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue153')}"/></td>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue154')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue155')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue156')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue157')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue158')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue159')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue161')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue162')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue163')}"/></td>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue164')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue165')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue166')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue167')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue168')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue169')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue171')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue172')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue173')}"/></td>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue174')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue175')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue176')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue177')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue178')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue179')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue181')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue182')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue183')}"/></td>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue184')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue185')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue186')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue187')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue188')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue189')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="6">PS판</td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue191')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue192')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue193')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue194')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" colspan="7">소계</td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue195')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue196')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue197')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" rowspan="6">인쇄</td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue201')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue202')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue203')}"/></td>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue204')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue205')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue206')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue207')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue208')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue209')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue210')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue211')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue212')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue221')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue222')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue223')}"/></td>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue224')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue225')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue226')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue227')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue228')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue229')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue230')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue231')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue232')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue241')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue242')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue243')}"/></td>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue244')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue245')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue246')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue247')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue248')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue249')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue250')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue251')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue252')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue261')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue262')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue263')}"/></td>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue264')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue265')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue266')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue267')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue268')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue269')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue270')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue271')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue272')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue281')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue282')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue283')}"/></td>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue284')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue285')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue286')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue287')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue288')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue289')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue290')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue291')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue292')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue301')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue302')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue303')}"/></td>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue304')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue305')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue306')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue307')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue308')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue309')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue310')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue311')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue312')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" colspan="7">소계</td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue314')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue315')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue316')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue317')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue318')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" rowspan="9">표지</td>
			<td class="body_ct" colspan="2">가공방법</td>
			<td class="body_ct" colspan="3"><u:out value="${formBodyXML.getAttr('body/detail.erpValue320')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct">원단</td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue321')}"/></td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue322')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue323')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue324')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue325')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue326')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue327')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue328')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue329')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct">원단</td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue331')}"/></td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue332')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue333')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue334')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue335')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue336')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue337')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue338')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue339')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct">원단</td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue341')}"/></td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue342')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue343')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue344')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue345')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue346')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue347')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue348')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue349')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue351')}"/></td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue352')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue353')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue354')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue355')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue356')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue361')}"/></td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue362')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue363')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue364')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue365')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue366')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue371')}"/></td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue372')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue373')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue374')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue375')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue376')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue381')}"/></td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue382')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue383')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue384')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue385')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue386')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue391')}"/></td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue392')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue393')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue394')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue395')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue396')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" colspan="7">소계</td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue397')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue398')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue399')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue400')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" rowspan="5">기타 외주가공</td>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue401')}"/></td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue402')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue421')}"/></td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue422')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue423')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue424')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue425')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue426')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue441')}"/></td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue442')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue443')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue444')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue445')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue446')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue461')}"/></td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue462')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue463')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue464')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue465')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue466')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue481')}"/></td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue482')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue483')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue484')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue485')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue486')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" colspan="7">소계</td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue496')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue497')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue498')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" rowspan="5">제본부자재</td>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue501')}"/></td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue502')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue503')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue504')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue505')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue506')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue507')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue508')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue509')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue521')}"/></td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue522')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue523')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue524')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue525')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue526')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue527')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue528')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue529')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue541')}"/></td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue542')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue543')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue544')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue545')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue546')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue547')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue548')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue549')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue561')}"/></td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue562')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue563')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue564')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue565')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue566')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue567')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue568')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue569')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue581')}"/></td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue582')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue583')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue584')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue585')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue586')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue587')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue588')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue589')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" colspan="7">소계</td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue601')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue602')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue603')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue604')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue605')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" rowspan="5">제본</td>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue621')}"/></td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue622')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue623')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue624')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue625')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue626')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue641')}"/></td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue642')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue643')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue644')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue645')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue646')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue661')}"/></td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue662')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue663')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue664')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue665')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue666')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue681')}"/></td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue682')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue683')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue684')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue685')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue686')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue701')}"/></td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue702')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue703')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue704')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue705')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue706')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" colspan="7">소계</td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue721')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue722')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue723')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" rowspan="4">포장 배송</td>
			<td class="body_ct" rowspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue741')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue742')}"/></td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue743')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue743')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue744')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue745')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue746')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue747')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue741')}"/></td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue762')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue763')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue764')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue765')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue766')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue767')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue781')}"/></td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue782')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue783')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue784')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue785')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue786')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/detail.erpValue801')}"/></td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue802')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue803')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue804')}"/></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue805')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue806')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" colspan="7">소계</td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue821')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue822')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="head_ct" rowspan="4">분석</td>
			<td class="body_lt" colspan="2">1.납품예정일 :</td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue841')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct">원가총액</td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue842')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue843')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_lt" colspan="2">2.원고승인예정일 :</td>
			<td class="body_ct" colspan="4"><u:out value="${formBodyXML.getAttr('body/detail.erpValue861')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct">수주총액</td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue862')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue863')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2">　</td>
			<td class="body_ct" colspan="4"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct">차액</td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue881')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue882')}"/></td>
		</tr><tr><c:set var="rowNo" value="${rowNo+1 }"/>
			<td class="body_ct" colspan="2">　</td>
			<td class="body_ct" colspan="4">　</td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct"></td>
			<td class="body_ct">가득액</td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue901')}"/></td>
			<td class="body_ct"></td>
			<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/detail.erpValue902')}"/></td>
		</tr>
</u:listArea>
	
</div>

<div class="blank"></div>

</div>
</c:if>