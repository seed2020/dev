<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

request.setAttribute("freqCds", new String[]{ "monthly", "quaterly", "half", "yearly", "byCase" });

request.setAttribute("basicW", "20%,80%");
request.setAttribute("basicW5", "20%,20%,20%,20%,20%");
request.setAttribute("basicW4", "20%,30%,30%,20%");
request.setAttribute("basicW3", "20%,40%,40%");

%><div>
<u:listArea style="width:25%" colgroup="39.5%,60.5%">
<tr>
	<th class="head_ct">GRADE</th>
	<td class="body_ct">CONFIDENTIAL</td>
</tr>
<tr>
	<th class="head_ct"><strong style="${not empty prevVo and prevVo.freqCd ne woOnecBVo.freqCd ? 'color:blue' : ''
		}">Frequency</strong></th>
	<td class="body_ct"><c:if
	
		test="${empty viewMode}"
		><c:if
			test="${(woOnecBVo.ver > 1 or woOnecBVo.statCd eq 'apvd')}"
			><c:if test="${not empty woOnecBVo.freqCd}"><u:msg titleId="wo.freqCd.${woOnecBVo.freqCd}" /></c:if>
			<input type="hidden" name="freqCd" value="${woOnecBVo.freqCd}" /></c:if><c:if
			test="${not (woOnecBVo.ver > 1 or woOnecBVo.statCd eq 'apvd')}"><select name="freqCd"><c:forEach
				items="${freqCds}" var="freqCd">
		<u:option titleId="wo.freqCd.${freqCd}" value="${freqCd}" checkValue="${woOnecBVo.freqCd}" /></c:forEach></select></c:if></c:if><c:if
		test="${not empty viewMode}"
		><c:if test="${not empty woOnecBVo.freqCd}"><u:msg titleId="wo.freqCd.${woOnecBVo.freqCd}" /></c:if></c:if></td>
</tr>
</u:listArea>

<div style="background-color:black; color:white; font-weight:bold; font-size:16px; padding:5px 0px 5px 3px; font-family: Arial">
One Card
</div>
<div style="margin-bottom:14px; color:red; font-size:12px; padding:6px 0px 0px 0px; font-family: 돋움"><u:msg titleId="wo.msg.thisSecu" alt="이 자료는 ㈜다인소재의 관리등급상 비밀자료에 해당합니다." /></div>

<div>
<%-- 왼편 --%>
<div style="float:left; width:49%; padding-right:2%;">
<u:listArea colgroup="${basicW}">
<tr>
	<th class="head_ct"><strong style="${not empty prevVo and prevVo.itemNm ne woOnecBVo.itemNm ? 'color:blue' : ''
		}">Item</strong></th>
	<td class="body_lt"><u:input title="Item" id="itemNm" value="${woOnecBVo.itemNm}" maxLength="70" style="width:96.2%"
		type="${not empty viewMode ? 'view' : ''}" /></td>
</tr>
</u:listArea>

<u:listArea colgroup="${basicW5}">
<tr>
	<th class="head_ct" rowspan="5">1. Description</th>
	<td class="body_ct"><strong style="${not empty prevVo and prevVo.issDt ne woOnecBVo.issDt ? 'color:blue' : ''
		}">Issued date</strong></td>
	<td class="body_lt"><u:calendar id="issDt" title="Issued date" value="${woOnecBVo.issDt}"
		type="${not empty viewMode ? 'view' : ''}" /></td>
	<td class="body_ct"><strong style="${not empty prevVo and prevVo.revsDt ne woOnecBVo.revsDt ? 'color:blue' : ''
		}">Revised date</strong></td>
	<td class="body_lt"><u:calendar id="revsDt" title="Revised date" value="${woOnecBVo.revsDt}" mandatory="Y"
		type="${not empty viewMode ? 'view' : ''}" /></td>
</tr>
<tr>
	<td class="body_ct"><strong>Holder</strong></td>
	<td class="body_lt">${empty woOnecBVo.holdrNm ? sessionScope.userVo.userNm : woOnecBVo.holdrNm}</td>
	<td class="body_ct"><strong style="${not empty prevVo and prevVo.dueDt ne woOnecBVo.dueDt ? 'color:blue' : ''
		}">Due date</strong></td>
	<td class="body_lt"><u:calendar id="dueDt" title="Due date" value="${woOnecBVo.dueDt}"
		type="${not empty viewMode ? 'view' : ''}" /></td>
</tr>
<tr>
	<td class="body_ct"><strong style="${not empty prevVo and prevVo.brndNm ne woOnecBVo.brndNm ? 'color:blue' : ''
		}">Brand name</strong></td>
	<td class="body_ct" colspan="3"><u:input title="Brand name" id="brndNm" value="${woOnecBVo.brndNm}" maxLength="70" style="width:96.2%"
		type="${not empty viewMode ? 'view' : ''}" /></td>
</tr>
<tr>
	<td class="body_ct"><strong style="${not empty prevVo and prevVo.catCd ne woOnecBVo.catCd ? 'color:blue' : ''
		}">Category</strong></td>
	<td class="body_lt" colspan="3"><c:if
	
		test="${empty viewMode}"
		><select name="catCd"><c:forEach items="${CAT_CDList}" var="cdVo"><c:if test="${cdVo.useYn ne 'N'}">
		<u:option value="${cdVo.cd}" title="${cdVo.cdVa}" checkValue="${woOnecBVo.catCd}" /></c:if></c:forEach></select></c:if><c:if
		test="${not empty viewMode}"
		><u:convertMap srcId="CAT_CDMap" attId="${woOnecBVo.catCd}" /></c:if></td>
</tr>
<tr>
	<td class="body_ct"><strong style="${not empty prevVo and prevVo.orgnCd ne woOnecBVo.orgnCd ? 'color:blue' : ''
		}">Origin</strong></td>
	<td class="body_lt" colspan="3"><c:if
	
		test="${empty viewMode}"
		><select name="orgnCd"><c:forEach items="${ORGN_CDList}" var="cdVo"><c:if test="${cdVo.useYn ne 'N'}">
		<u:option value="${cdVo.cd}" title="${cdVo.cdVa}" checkValue="${woOnecBVo.orgnCd}" /></c:if></c:forEach></select></c:if><c:if
		test="${not empty viewMode}"
		><u:convertMap srcId="ORGN_CDMap" attId="${woOnecBVo.orgnCd}" /></c:if></td>
</tr>
</u:listArea>

<u:listArea colgroup="${basicW}">
<tr>
	<th class="head_ct" rowspan="3">2. Environmental Analysis</th>
	<td class="body_lt"><strong style="${not empty prevVo and prevVo.mketSitu ne woOnecBVo.mketSitu ? 'color:blue' : ''
		}">1. Market Situation</strong>
		<div>
		<u:textarea id="mketSitu" title="Market Situation" rows="2" style="width:96.2%" value="${woOnecBVo.mketSitu}" maxLength="200"
		type="${not empty viewMode ? 'view' : ''}" /></div></td>
</tr>
<tr>
	<td class="body_lt"><strong style="${not empty prevVo and prevVo.custAnal ne woOnecBVo.custAnal ? 'color:blue' : ''
		}">2. Customer Analysis</strong>
		<div>
		<u:textarea id="custAnal" title="Customer Analysis" rows="2" style="width:96.2%" value="${woOnecBVo.custAnal}" maxLength="200"
		type="${not empty viewMode ? 'view' : ''}" /></div></td>
</tr>
<tr>
	<td class="body_lt"><strong style="${not empty prevVo and prevVo.cmptrAnal ne woOnecBVo.cmptrAnal ? 'color:blue' : ''
		}">3. Competitors Analysis</strong>
		<div>
		<u:textarea id="cmptrAnal" title="Competitors Analysis" rows="2" style="width:96.2%" value="${woOnecBVo.cmptrAnal}" maxLength="200"
		type="${not empty viewMode ? 'view' : ''}" /></div></td>
</tr>
</u:listArea>

<u:listArea colgroup="${basicW4}">
<tr>
	<th class="head_ct" rowspan="3">3. Benchmark</th>
	<td class="body_lt" colspan="3"><strong style="${not empty prevVo and prevVo.bmItem ne woOnecBVo.bmItem ? 'color:blue' : ''
		}">1. Item</strong>
		<div>
		<u:textarea id="bmItem" title="Benchmark - Item" rows="2" style="width:96.2%" value="${woOnecBVo.bmItem}" maxLength="70"
		type="${not empty viewMode ? 'view' : ''}" /></div></td>
</tr>
<tr>
	<td class="body_lt"><strong style="${not empty prevVo and prevVo.bmRetPrc ne woOnecBVo.bmRetPrc ? 'color:blue' : ''
		}">2. Retail Price(￦)</strong>
		<div>
		<u:input id="bmRetPrc" title="Benchmark - Retail Price" value="${woOnecBVo.bmRetPrc}" style="width:89%" maxLength="12" valueOption="number" valueAllowed=","
		type="${not empty viewMode ? 'view' : ''}" /></div></td>
	<td class="body_lt"><strong style="${not empty prevVo and prevVo.bmSalsPrc ne woOnecBVo.bmSalsPrc ? 'color:blue' : ''
		}">3. Sales Price(￦)</strong>
		<div>
		<u:input id="bmSalsPrc" title="Benchmark - Sales Price" value="${woOnecBVo.bmSalsPrc}" style="width:89%" maxLength="12" valueOption="number" valueAllowed=","
		type="${not empty viewMode ? 'view' : ''}" /></div></td>
	<td class="body_lt"><strong style="${not empty prevVo and prevVo.bmSp ne woOnecBVo.bmSp ? 'color:blue' : ''
		}">4. SP(%)</strong>
		<div>
		<u:input id="bmSp" title="Benchmark - SP" value="${woOnecBVo.bmSp}" style="width:84%" maxLength="5" valueOption="number" valueAllowed=",."
		type="${not empty viewMode ? 'view' : ''}" /></div></td>
</tr>
<tr>
	<td class="body_lt" colspan="3"><strong style="${not empty prevVo and prevVo.bmSpecSize ne woOnecBVo.bmSpecSize ? 'color:blue' : ''
		}">5. Spec/Size</strong>
		<div>
		<u:input id="bmSpecSize" title="Benchmark - Spec/Size" value="${woOnecBVo.bmSpecSize}" style="width:96.2%" maxLength="200"
		type="${not empty viewMode ? 'view' : ''}" /></div></td>
</tr>
</u:listArea>

<u:listArea colgroup="${basicW3}">
<tr>
	<th class="head_ct" rowspan="4">4. Development</th>
	<td class="body_lt" colspan="2"><strong style="${not empty prevVo and prevVo.diff ne woOnecBVo.diff ? 'color:blue' : ''
		}">1. Differentiation</strong>
		<div>
		<u:textarea id="diff" title="Development - Differentiation" value="${woOnecBVo.diff}" rows="2" style="width:96.2%" maxLength="200"
		type="${not empty viewMode ? 'view' : ''}" /></div></td>
</tr>
<tr>
	<td class="body_lt" colspan="2"><strong style="${not empty prevVo and prevVo.spec ne woOnecBVo.spec ? 'color:blue' : ''
		}">2. Specifications</strong>
		<div>
		<u:textarea id="spec" title="Development - Specifications" value="${woOnecBVo.spec}" rows="2" style="width:96.2%" maxLength="200"
		type="${not empty viewMode ? 'view' : ''}" /></div></td>
</tr>
<tr>
	<td class="body_lt" colspan="2"><strong style="${not empty prevVo and prevVo.tgtCust ne woOnecBVo.tgtCust ? 'color:blue' : ''
		}">3. Target Customer</strong>
		<div>
		<u:textarea id="tgtCust" title="Development - Target Customer" value="${woOnecBVo.tgtCust}" rows="2" style="width:96.2%" maxLength="200"
		type="${not empty viewMode ? 'view' : ''}" /></div></td>
</tr>
<tr>
	<td class="body_lt"><strong style="${not empty prevVo and prevVo.pack ne woOnecBVo.pack ? 'color:blue' : ''
		}">4. Packaging</strong>
		<div>
		<u:input title="Development - Packaging" name="pack" value="${woOnecBVo.pack}" style="width:94%" maxLength="70"
		type="${not empty viewMode ? 'view' : ''}" /></div></td>
	<td class="body_lt"><strong style="${not empty prevVo and prevVo.storgCd ne woOnecBVo.storgCd ? 'color:blue' : ''
		}">5. Storage<br/></strong>
		<div><c:if
	
		test="${empty viewMode}"
		><select name="storgCd"><c:forEach items="${STORG_CDList}" var="cdVo"><c:if test="${cdVo.useYn ne 'N'}">
		<u:option value="${cdVo.cd}" title="${cdVo.cdVa}" checkValue="${woOnecBVo.storgCd}" /></c:if></c:forEach></select></c:if><c:if
		test="${not empty viewMode}"
		><u:convertMap srcId="STORG_CDMap" attId="${woOnecBVo.storgCd}" /></c:if></div></td>
</tr>
</u:listArea>

</div>


<%-- 오른편 --%>
<div style="float:right; width:49%">
<u:listArea colgroup="${basicW}">
<tr>
	<th class="head_ct">5. Positioning</th>
	<td>
	<table border="0" cellpadding="0" cellspacing="0" style="margin-left:auto; margin-right:auto; width:100%">
	<tr>
		<td>
		<div id="posImgArea" style="width:170px; height:140px; position:relative; background: url('/images/etc/DYNE1.gif') no-repeat center center; margin-top:6px;">
		<div style="position:absolute; top:0px; left:52px; z-index:2;">High  price</div>
		<div style="position:absolute; top:79px; left:100px; z-index:2;">High quality</div>
		<img src="/images/etc/DYNE2.gif" style="position:absolute; top:${empty woOnecBVo.posMyY ? 32 : woOnecBVo.posMyY}px; left:${empty woOnecBVo.posMyX ? 95 : woOnecBVo.posMyX}px; z-index:6" id="dragStar" />
		<img src="/images/etc/DYNE3.gif" style="position:absolute; top:${empty woOnecBVo.posOthrY ? 52 : woOnecBVo.posOthrY}px; left:${empty woOnecBVo.posOthrX ? 112 : woOnecBVo.posOthrX}px; z-index:5" id="dragCircle" />
		<input type="hidden" name="posMyX" value="${woOnecBVo.posMyY}" />
		<input type="hidden" name="posMyY" value="${woOnecBVo.posMyY}" />
		<input type="hidden" name="posOthrX" value="${woOnecBVo.posOthrX}" />
		<input type="hidden" name="posOthrY" value="${woOnecBVo.posOthrY}" />
		</div></td>
		<td style="width:60%">
			<table border="0" cellpadding="0" cellspacing="0">
			<tr><td class="body_lt"><strong style="${not empty prevVo and prevVo.posTgtPrc ne woOnecBVo.posTgtPrc ? 'color:blue' : ''
		}">Target price :</strong></td>
				<td class="body_rt"><u:input id="posTgtPrc" title="Positioning - Target price" value="${woOnecBVo.posTgtPrc}" style="width:70px" maxLength="12" valueOption="number" valueAllowed=","
		type="${not empty viewMode ? 'view' : ''}" /></td></tr>
			<tr><td class="body_lt"><strong style="${not empty prevVo and prevVo.posCost ne woOnecBVo.posCost ? 'color:blue' : ''
		}">Cost :</strong></td>
				<td class="body_rt"><u:input id="posCost" title="Positioning - Cost" value="${woOnecBVo.posCost}" style="width:70px" maxLength="12" valueOption="number" valueAllowed=","
		type="${not empty viewMode ? 'view' : ''}" /></td></tr>
			<tr><td class="body_lt"><strong style="${not empty prevVo and prevVo.posSp ne woOnecBVo.posSp ? 'color:blue' : ''
		}">SP(%) :</strong></td>
				<td class="body_rt"><u:input id="posSp" title="Positioning - SP" value="${woOnecBVo.posSp}" style="width:70px" maxLength="5" valueOption="number" valueAllowed=",."
		type="${not empty viewMode ? 'view' : ''}" /></td></tr>
			</table>
			<table border="0" cellpadding="0" cellspacing="0" style="margin-top:20px; width:100%;">
			<tr><td style="float:right; padding-top:4px;"><img src="/images/etc/DYNE2.gif"></td><td style="width:65px;" class="body_lt">: <u:msg titleId="wo.msg.myItem" alt="자사제품" /></td></tr>
			<tr><td style="float:right; padding-right:2px; padding-top:6px;"><img src="/images/etc/DYNE3.gif"></td><td style="width:65px;" class="body_lt">: <u:msg titleId="wo.msg.othrItem" alt="타사제품" /></td></tr>
			</table></td>
	</tr>
	</table>
	<table border="0" cellpadding="0" cellspacing="0" style="width:100%">
	<tr>
		<td colspan="2" class="body_lt"><strong style="${not empty prevVo and prevVo.posRemk ne woOnecBVo.posRemk ? 'color:blue' : ''
		}">Remark</strong>
		<div>
		<u:textarea id="posRemk" title="Positioning - Remark" value="${woOnecBVo.posRemk}" rows="3" style="width:96.2%" maxLength="200"
		type="${not empty viewMode ? 'view' : ''}" /></div></td>
	</tr>
	</table>
	</td>
</tr>
</u:listArea>

<u:listArea colgroup="${basicW}">
<tr>
	<th class="head_ct">6. T/A</th>
	<td class="body_ct"><div style="padding: 6px 0px 6px 0px"><strong style="${not empty prevVo and prevVo.taApvYn ne woOnecBVo.taApvYn ? 'color:blue' : ''
		}">Technical Approval</strong></div>
		<div style="text-align:center; margin-bottom:6px;"><c:if
	
		test="${empty viewMode}"
		><u:checkArea style="margin-left:auto; margin-right:auto;">
		<u:radio value="Y" name="taApvYn" title="Yes" checkValue="${woOnecBVo.taApvYn}" />
		<u:radio value="N" name="taApvYn" title="No" checkValue="${empty woOnecBVo.taApvYn ? 'N' : woOnecBVo.taApvYn}" /></u:checkArea></c:if><c:if
	
		test="${not empty viewMode}"
		><c:if test="${not empty woOnecBVo.taApvYn}"><u:msg titleId="wo.ynCd.${woOnecBVo.taApvYn}" /></c:if></c:if></div></td>
</tr>
</u:listArea>

<u:listArea colgroup="${basicW}">
<tr>
	<th class="head_ct">7. Sales Forecast Analysis</th>
	<td class="body_lt"><div style="text-align:right; padding: 6px 3px 6px 0px"><u:msg titleId="wo.msg.sfUnit" alt="(Kg/월, 백만원)" /></div>
		<u:listArea style="margin-right:-1px; margin-bottom:8px;"
			colgroup="16.6%,16.6%,16.6%,16.6%,16.6%,16.6%" noBottomBlank="true">
		<tr>
			<td class="body_ct"></td>
			<td class="body_ct">3rd month</td>
			<td class="body_ct">6th month</td>
			<td class="body_ct">1st year</td>
			<td class="body_ct">2nd year</td>
			<td class="body_ct">3rd year</td>
		</tr>
		<tr>
			<td class="body_ct">Volume</td>
			<td class="body_ct" style="padding-right:3px;${not empty prevVo and prevVo.sf3mVo ne woOnecBVo.sf3mVo ? ' color:blue;' : ''
		}"><u:input id="sf3mVo" title="Volume - 3rd month" value="${woOnecBVo.sf3mVo}" style="width:78%" valueOption="number" valueNotAllowed="," maxLength="12"
		type="${not empty viewMode ? 'view' : ''}" /></td>
			<td class="body_ct" style="padding-right:3px;${not empty prevVo and prevVo.sf6mVo ne woOnecBVo.sf6mVo ? ' color:blue;' : ''
		}"><u:input id="sf6mVo" title="Volume - 6th month" value="${woOnecBVo.sf6mVo}" style="width:78%" valueOption="number" valueNotAllowed="," maxLength="12"
		type="${not empty viewMode ? 'view' : ''}" /></td>
			<td class="body_ct" style="padding-right:3px;${not empty prevVo and prevVo.sf1yVo ne woOnecBVo.sf1yVo ? ' color:blue;' : ''
		}"><u:input id="sf1yVo" title="Volume - 1st year" value="${woOnecBVo.sf1yVo}" style="width:78%" valueOption="number" valueNotAllowed="," maxLength="12"
		type="${not empty viewMode ? 'view' : ''}" /></td>
			<td class="body_ct" style="padding-right:3px;${not empty prevVo and prevVo.sf2yVo ne woOnecBVo.sf2yVo ? ' color:blue;' : ''
		}"><u:input id="sf2yVo" title="Volume - 2nd year" value="${woOnecBVo.sf2yVo}" style="width:78%" valueOption="number" valueNotAllowed="," maxLength="12"
		type="${not empty viewMode ? 'view' : ''}" /></td>
			<td class="body_ct" style="padding-right:3px;${not empty prevVo and prevVo.sf3yVo ne woOnecBVo.sf3yVo ? ' color:blue;' : ''
		}"><u:input id="sf3yVo" title="Volume - 3rd year" value="${woOnecBVo.sf3yVo}" style="width:78%" valueOption="number" valueNotAllowed="," maxLength="12"
		type="${not empty viewMode ? 'view' : ''}" /></td>
		</tr>
		<tr>
			<td class="body_ct">Sales</td>
			<td class="body_ct" style="padding-right:3px;${not empty prevVo and prevVo.sf3mSail ne woOnecBVo.sf3mSail ? ' color:blue;' : ''
		}"><u:input id="sf3mSail" title="Sales - 3rd month" style="width:78%" value="${woOnecBVo.sf3mSail}" valueOption="number" valueNotAllowed="," maxLength="12"
		type="${not empty viewMode ? 'view' : ''}" /></td>
			<td class="body_ct" style="padding-right:3px;${not empty prevVo and prevVo.sf6mSail ne woOnecBVo.sf6mSail ? ' color:blue;' : ''
		}"><u:input id="sf6mSail" title="Sales - 6th month" style="width:78%" value="${woOnecBVo.sf6mSail}" valueOption="number" valueNotAllowed="," maxLength="12"
		type="${not empty viewMode ? 'view' : ''}" /></td>
			<td class="body_ct" style="padding-right:3px;${not empty prevVo and prevVo.sf1ySail ne woOnecBVo.sf1ySail ? ' color:blue;' : ''
		}"><u:input id="sf1ySail" title="Sales - 1st year" style="width:78%" value="${woOnecBVo.sf1ySail}" valueOption="number" valueNotAllowed="," maxLength="12"
		type="${not empty viewMode ? 'view' : ''}" /></td>
			<td class="body_ct" style="padding-right:3px;${not empty prevVo and prevVo.sf2ySail ne woOnecBVo.sf2ySail ? ' color:blue;' : ''
		}"><u:input id="sf2ySail" title="Sales - 2nd year" style="width:78%" value="${woOnecBVo.sf2ySail}" valueOption="number" valueNotAllowed="," maxLength="12"
		type="${not empty viewMode ? 'view' : ''}" /></td>
			<td class="body_ct" style="padding-right:3px;${not empty prevVo and prevVo.sf3ySail ne woOnecBVo.sf3ySail ? ' color:blue;' : ''
		}"><u:input id="sf3ySail" title="Sales - 3rd year" style="width:78%" value="${woOnecBVo.sf3ySail}" valueOption="number" valueNotAllowed="," maxLength="12"
		type="${not empty viewMode ? 'view' : ''}" /></td>
		</tr>
		</u:listArea>
		<div style="padding: 6px 0px 0px 0px"><strong style="${not empty prevVo and prevVo.sfRemk ne woOnecBVo.sfRemk ? 'color:blue' : ''
		}">Remark</strong></div>
		<u:textarea id="sfRemk" title="Sales Forecast Analysis - Remark" value="${woOnecBVo.sfRemk}" rows="3" style="width:96.2%" maxLength="200"
		type="${not empty viewMode ? 'view' : ''}" />
		</td>
</tr>
</u:listArea>

<u:listArea colgroup="${basicW}">
<tr>
	<th class="head_ct" rowspan="2">8. Sales Status</th>
	<td class="body_lt" colspan="2"><strong style="${not empty prevVo and prevVo.promPlan ne woOnecBVo.promPlan ? 'color:blue' : ''
		}">1. Promotion plan</strong>
		<div style="min-height:51px">
		<u:textarea id="promPlan" title="Sales Status - Promotion plan" value="${woOnecBVo.promPlan}" rows="6" style="width:96.2%" maxLength="200"
		type="${not empty viewMode ? 'view' : ''}" /></div></td>
</tr>
<tr>
	<td class="body_lt" colspan="2"><strong style="${not empty prevVo and prevVo.sailPerfm ne woOnecBVo.sailPerfm ? 'color:blue' : ''
		}">2. Sale performance</strong>
		<div style="min-height:51px">
		<u:textarea id="sailPerfm" title="Sales Status - Sale performance" value="${woOnecBVo.sailPerfm}" rows="6" style="width:96.2%" maxLength="200"
		type="${not empty viewMode ? 'view' : ''}" /></div></td>
</tr>
</u:listArea>

</div>
</div>

<div style="float:left; width:100%;">
<div style="color:red; padding-bottom:2px;">
	<table border="0" cellpadding="0" cellspacing="0" style="width:100%;"><tr>
	<td style="vertical-align: bottom;"><u:msg titleId="wo.msg.apvOrdr" alt="※ 문서 검토, 승인자는 Grade / Frequency 정확히 확인 할 것." /></td><c:if
		test="${empty viewMode}">
	<td><table border="0" cellpadding="0" cellspacing="0" style="float:right"><tr>
	<td><u:buttonS titleId="cm.btn.add" alt="추가" onclick="mngHistory('add')" /></td>
	<td><u:buttonS titleId="cm.btn.mod" alt="변경" onclick="mngHistory('mod')" /></td>
	<td><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="mngHistory('del')" /></td></tr></table></td></c:if></tr></table></div>
<u:listArea>
<tr><th class="head_lt" style="text-align:left;">9. History : <u:msg titleId="wo.msg.hisOrdr" alt="Item 별 세부 진행상황, 기술적인 내용/문제/요청사항 등을 기록관리 유지 함." /></th></tr>
<tr><td>
<div style="min-height:149px; max-height:249px; overflow-x:hidden; overflow-y:auto;">
<u:listArea id="historyArea"
	style="margin:-1px -1px -1px -1px; width:${browser.ie ? '100.15%' : '100.1%'}"
	tableStyle="${browser.ie ? '' : 'width:100.1%'}"
	colgroup="${not empty viewMode ? '10%,90%' : '2.5%,10%,87.5%'}"
	 noBottomBlank="true"><c:forEach
	items="${woOnecHisLVoList}" var="woOnecHisLVo"><c:if test="${
		empty viewMode or woOnecBVo.ver eq lastVer
		or (woOnecBVo.apvdHisNo >= woOnecHisLVo.hisNo)}"><%-- 수정모드거나, 조회의 최종본(홀더의경우)이거나, 최종 승인된 것 --%>
<tr>
	<c:if
		test="${empty viewMode}"><td class="bodybg_ct"><c:if
			test="${(not empty woOnecBVo.apvdHisNo and woOnecBVo.apvdHisNo >= woOnecHisLVo.hisNo)}"
			><input type="checkbox" disabled="disabled"></c:if><c:if
			test="${not(not empty woOnecBVo.apvdHisNo and woOnecBVo.apvdHisNo >= woOnecHisLVo.hisNo) }"
			><input type="checkbox">
	<input type="hidden" name="hisRegDt" value="<u:out value="${woOnecHisLVo.hisRegDt}" type="date" />"/>
	<input type="hidden" name="hisCont" value="<u:out value="${woOnecHisLVo.hisCont}" type="value" />"/></c:if></td></c:if>
	<td class="body_ct" style="${not empty prevVo and (empty prevVo.apvdHisNo or prevVo.apvdHisNo < woOnecHisLVo.hisNo) ? 'color:blue;' : ''}"><u:out value="${woOnecHisLVo.hisRegDt}" type="date" /></td>
	<td class="body_lt"><u:out value="${woOnecHisLVo.hisCont}" /></td>
</tr></c:if></c:forEach><c:if
		test="${empty viewMode}">
<tr id="hiddenTr" style="display:none;">
	<td class="bodybg_ct"><input type="checkbox" class="skipThese">
	<input type="hidden" name="hisRegDt" value=""/>
	<input type="hidden" name="hisCont" value=""/></td>
	<td class="body_ct"></td>
	<td class="body_lt"></td></tr></c:if>
</u:listArea></div></td>
</tr>
</u:listArea>

<u:listArea id="attachArea">
<tr><th class="head_lt" style="text-align:left;">10. Attachment</th></tr>
<tr>
<td>
	<u:files id="wofiles" fileVoList="${fileVoList}" module="wo" mode="${empty viewMode ? 'set' : 'view'}"
		exts="${exts}" extsTyp="${extsTyp}" actionParam="${woOnecBVo.onecNo}" height="55" urlParam="cat=${param.cat}"/>
</td>
</tr>
</u:listArea>

<c:if test="${empty htmlMode}">
<div class="blank"></div>
<u:title titleId="wo.etc" alt="기타 항목" type="small" />

<u:listArea colgroup="9.6%,39.4%,9.6%,39.4%">
<tr>
	<td class="head_ct"><u:msg titleId="wo.clsCd.ONEC_TYP_CD" alt="분류" /></td>
	<td class="body_lt"><c:if
	
		test="${empty viewMode}"
		><select name="onecTypCd"><c:forEach items="${ONEC_TYP_CDList}" var="cdVo"><c:if test="${cdVo.useYn ne 'N'}">
		<u:option value="${cdVo.cd}" title="${cdVo.cdVa}" checkValue="${woOnecBVo.onecTypCd}" /></c:if></c:forEach></select></c:if><c:if
		test="${not empty viewMode}"
		><u:convertMap srcId="ONEC_TYP_CDMap" attId="${woOnecBVo.onecTypCd}" /></c:if></td>
	
	<td class="head_ct"><u:msg titleId="cols.dept" alt="부서" /></td>
	<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0" style="width:100%" id="onecDeptArea" ><tr>
		<td><u:out value="${empty woOnecBVo ? sessionScope.userVo.deptNm : deptVo.rescNm}" /></td><c:if
			test="${empty viewMode}">
		<td style="float:right"><u:buttonS titleId="cm.btn.sel" alt="선택" onclick="setOnecDept()" />
			<input type="hidden" name="deptId" value="${empty woOnecBVo ? sessionScope.userVo.deptId : deptVo.orgId}" /></td></c:if>
		</tr></table></td>
</tr>
<tr>
	<td class="head_ct"><u:msg titleId="wo.pichTypCd.R" alt="R" /></td>
	<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0" style="width:100%" id="pichTypCdRArea" ><tr>
		<td><c:forEach
			items="${pichTypCdRList}" var="woOnecPichDVo" varStatus="status">
		<c:if test="${not status.first}">, </c:if><a href="javascript:viewUserPop('${woOnecPichDVo.userUid}');">${woOnecPichDVo.userNm}</a></c:forEach></td><c:if
			test="${empty viewMode}">
		<td style="float:right"><u:buttonS titleId="cm.btn.sel" alt="선택" onclick="setOnecPich('R')" />
			<input type="hidden" name="pichTypCdR" value="<c:forEach
			items="${pichTypCdRList}" var="woOnecPichDVo" varStatus="status"><c:if test="${not status.first}">,</c:if>${woOnecPichDVo.userUid}</c:forEach>" ></td></c:if>
		</tr></table></td>
	
	<td class="head_ct"><u:msg titleId="wo.pichTypCd.A" alt="A" /></td>
	<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0" style="width:100%" id="pichTypCdAArea"><tr>
		<td><c:forEach
			items="${pichTypCdAList}" var="woOnecPichDVo" varStatus="status">
		<c:if test="${not status.first}">, </c:if><a href="javascript:viewUserPop('${woOnecPichDVo.userUid}');">${woOnecPichDVo.userNm}</a></c:forEach></td><c:if
			test="${empty viewMode}">
		<td style="float:right"><u:buttonS titleId="cm.btn.sel" alt="선택" onclick="setOnecPich('A')" />
			<input type="hidden" name="pichTypCdA" value="<c:forEach
			items="${pichTypCdAList}" var="woOnecPichDVo" varStatus="status"><c:if test="${not status.first}">,</c:if>${woOnecPichDVo.userUid}</c:forEach>" ></td></c:if>
		</tr></table></td>
</tr>
<tr>
	<td class="head_ct"><u:msg titleId="wo.pichTypCd.C" alt="C" /></td>
	<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0" style="width:100%" id="pichTypCdCArea"><tr>
		<td><c:forEach
			items="${pichTypCdCList}" var="woOnecPichDVo" varStatus="status">
		<c:if test="${not status.first}">, </c:if><a href="javascript:viewUserPop('${woOnecPichDVo.userUid}');">${woOnecPichDVo.userNm}</a></c:forEach></td><c:if
			test="${empty viewMode}">
		<td style="float:right"><u:buttonS titleId="cm.btn.sel" alt="선택" onclick="setOnecPich('C')" />
			<input type="hidden" name="pichTypCdC" value="<c:forEach
			items="${pichTypCdCList}" var="woOnecPichDVo" varStatus="status"><c:if test="${not status.first}">,</c:if>${woOnecPichDVo.userUid}</c:forEach>" ></td></c:if>
		</tr></table></td>
	
	<td class="head_ct"><u:msg titleId="wo.pichTypCd.I" alt="I" /></td>
	<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0" style="width:100%" id="pichTypCdIArea"><tr>
		<td><c:forEach
			items="${pichTypCdIList}" var="woOnecPichDVo" varStatus="status">
		<c:if test="${not status.first}">, </c:if><a href="javascript:viewUserPop('${woOnecPichDVo.userUid}');">${woOnecPichDVo.userNm}</a></c:forEach></td><c:if
			test="${empty viewMode}">
		<td style="float:right"><u:buttonS titleId="cm.btn.sel" alt="선택" onclick="setOnecPich('I')" />
			<input type="hidden" name="pichTypCdI" value="<c:forEach
			items="${pichTypCdIList}" var="woOnecPichDVo" varStatus="status"><c:if test="${not status.first}">,</c:if>${woOnecPichDVo.userUid}</c:forEach>" ></td></c:if>
		</tr></table></td>
</tr>
<tr>
	<td class="head_ct"><u:msg titleId="wo.pichTypCd.S" alt="S" /></td>
	<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0" style="width:100%" id="pichTypCdSArea"><tr>
		<td><c:forEach
			items="${pichTypCdSList}" var="woOnecPichDVo" varStatus="status">
		<c:if test="${not status.first}">, </c:if><a href="javascript:viewUserPop('${woOnecPichDVo.userUid}');">${woOnecPichDVo.userNm}</a></c:forEach></td><c:if
			test="${empty viewMode}">
		<td style="float:right"><u:buttonS titleId="cm.btn.sel" alt="선택" onclick="setOnecPich('S')" />
			<input type="hidden" name="pichTypCdS" value="<c:forEach
			items="${pichTypCdSList}" var="woOnecPichDVo" varStatus="status"><c:if test="${not status.first}">,</c:if>${woOnecPichDVo.userUid}</c:forEach>" ></td></c:if>
		</tr></table></td>
	
	<td class="head_ct"><u:msg titleId="wo.pichTypCd.QC" alt="QC" /></td>
	<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0" style="width:100%" id="pichTypCdQCArea"><tr>
		<td><c:forEach
			items="${pichTypCdQCList}" var="woOnecPichDVo" varStatus="status">
		<c:if test="${not status.first}">, </c:if><a href="javascript:viewUserPop('${woOnecPichDVo.userUid}');">${woOnecPichDVo.userNm}</a></c:forEach></td><c:if
			test="${empty viewMode}">
		<td style="float:right"><u:buttonS titleId="cm.btn.sel" alt="선택" onclick="setOnecPich('QC')" />
			<input type="hidden" name="pichTypCdQC" value="<c:forEach
			items="${pichTypCdQCList}" var="woOnecPichDVo" varStatus="status"><c:if test="${not status.first}">,</c:if>${woOnecPichDVo.userUid}</c:forEach>" ></td></c:if>
		</tr></table></td>
</tr>
<tr>
	<td class="head_ct"><u:msg titleId="cols.note" alt="비고" /></td>
	<td class="body_lt" colspan="3"><u:textarea rows="5" maxLength="200" id="note" titleId="cols.note" alt="비고"
		style="width:98.5%" value="${woOnecBVo.note}"
		type="${not empty viewMode ? 'view' : ''}" /></td>
</tr>
</u:listArea>

</c:if>

</div>
</div>