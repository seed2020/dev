<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
import="org.springframework.web.context.WebApplicationContext"
import="org.springframework.web.context.support.WebApplicationContextUtils"
import="org.springframework.web.servlet.FrameworkServlet"
import="com.innobiz.orange.web.or.svc.OrCmSvc"
import="com.innobiz.orange.web.pt.secu.LoginSession"
import="com.innobiz.orange.web.pt.secu.UserVo"
import="java.util.Map"
import="java.util.List,java.util.ArrayList"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
//양식명 : 지출 결의서

com.innobiz.orange.web.ap.utils.XMLElement formBodyXML = (com.innobiz.orange.web.ap.utils.XMLElement)request.getAttribute("formBodyXML");
if(!"view".equals(request.getAttribute("formBodyMode"))){
	if(formBodyXML != null){
		if(request.getAttribute("namoEditorEnable")==null){
			String _bodyHtml = com.innobiz.orange.web.cm.utils.EscapeUtil.escapeWriteableHtml(formBodyXML.getAttr("body/cont.erpCont"));
			request.setAttribute("_bodyHtml", _bodyHtml);
		} else {
			request.setAttribute("_bodyHtml", formBodyXML.getAttr("body/cont.erpCont"));
		}
	}
}
if(formBodyXML != null){
	List<String> gubunList=null;
	
	// 구분 목록
	String gubunTxt = formBodyXML.getAttr("body/gubun.erpValue31");
	if(gubunTxt!=null && !gubunTxt.trim().isEmpty()){
		String[] gubuns=gubunTxt.split(",");
		gubunList=new ArrayList<String>();
		if(gubuns.length>0){
			for(int i=0;i<gubuns.length;i++){
				if(gubuns[i].trim().isEmpty()) continue; 
				gubunList.add(gubuns[i]);
			}
			if(gubunList.size()>0)
				request.setAttribute("gubuns", gubunList);
		}
	}
	
	// 구분 목록
	gubunTxt = formBodyXML.getAttr("body/gubun.erpValue32");
	if(gubunTxt!=null && !gubunTxt.trim().isEmpty()){
		String[] gubuns=gubunTxt.split(",");
		gubunList=new ArrayList<String>();
		if(gubuns.length>0){
			for(int i=0;i<gubuns.length;i++){
				if(gubuns[i].trim().isEmpty()) continue; 
				gubunList.add(gubuns[i]);
			}
			if(gubunList.size()>0)
				request.setAttribute("spendingGubuns", gubunList);
		}
		
	}
	
}

// 초기 row수
request.setAttribute("initStrtCnt", "1");
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
.body_rt {
    height: 22px;
    color: #454545;
    text-align: right;
    line-height: 17px;
    padding: 2px 3px 0 4px;
}
.blank {
    clear: both;
    height: 10px;
}
.divider {
	width:100%;
	text-align:center;
	margin:10px 0 10px 0;
}
.divider hr {
	display:block;
	margin-left:auto;
	margin-right:auto;
	width:48%;
	border:1px dashed #d22681;
}
@media print {
.head_ct { font-weight:bold; }
.body_ct { font-weight:bold; }
.body_lt { font-weight:bold; }
.body_rt { font-weight:bold; }
}
</style><script type="text/javascript">
<!--
<%// 계좌정보 사용 %>
function onBank(obj){
	var visible = $("#xml-banks\\/bank").is(':visible');
	var arrs = ['xml-banks\\/bank', 'bankAddRowBtn', 'bankDelRowBtn'];
	for(var i=0;i<arrs.length;i++){
		if(visible) $('#'+arrs[i]).hide(); 
		else $('#'+arrs[i]).show();
	}
	$(obj).find('span:eq(0)').text(visible ? '입력 열기' : '입력 닫기');
	if(visible){ // 닫기
		$('#xml-spending input[id="erpBankYn"]').val('N');
		
		var removeTrs = [];
		$("#xml-banks\\/bank tbody:first").children().not('#hiddenTr, #headerTr').each(function(index){
			removeTrs.push($(this));
		});
		if(removeTrs.length>0)
			delRowInArr(removeTrs);
		
		addRow('xml-banks/bank', 'bank', '${bankStrtCnt }');
	}else{
		$('#xml-spending input[id="erpBankYn"]').val('Y');
	}
	<c:if test="${formBodyMode eq 'pop'}">dialog.resize("setDocBodyHtmlDialog");</c:if>
}
<%// 자동계산 %>
function setCalculate(obj){
	var va, total;
	if(obj===undefined) obj = $("#xml-rows\\/row input[id^='erpValue6']");
	obj.change(function(){
		total=0;
		$('#xml-rows\\/row tbody:first').children().not('#hiddenTr, #headerTr').find("input[id^='erpValue6']").each(function(){
			va=removeComma($(this).val());
			if(va=='' || isNaN(va)) return true;
			total+=parseInt(va);
		});
		$('#xml-rows\\/row input[name="erpValue7"]').val(addComma(String(total)));
	});
}<%// [콤보박스] - 업체정보 변경 %>
function chnBanks(id){
	var gAttrs = ["bankNm", "holderNm", "number"];
	var arr = {};
	$opt = $('#'+id+' option:selected');
	$tr=$('#'+id).closest('tr#bank');
	$tr.find('#directContainer').hide();
	$tr.find('input[name="erpValue222"]').val('');
	if($opt.val()=='empty' || $opt.val()=='direct'){		
		$tr.find('input[name="erpValue22"]').val('');
		$tr.find('input[name="erpValue23"]').val('');
		$tr.find('input[name="erpValue24"]').val('');
		if($opt.val()=='direct')
			$tr.find('#directContainer').show();
	}else{
		gAttrs.each(function(index, attr){
			arr[attr] = $opt.attr("data-"+attr);
		});
		$tr.find('input[name="erpValue22"]').val(arr.bankNm);
		$tr.find('input[name="erpValue23"]').val(arr.holderNm);
		$tr.find('input[name="erpValue24"]').val(arr.number);
	}
	
	
}
<%//사용자 정보 변경 %>
function chkGubuns(obj){
	var val = $(obj).val();
	if(/[\{\}\[\]\/?.:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/g.test(val))
		$(obj).val(val.replace(/[\{\}\[\]\/?.;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"\s]/g, ''));
}
<%//사용자 정보 변경 %>
function setUserPop(){
	var data = [];
	searchUserPop({data:data, multi:false, mode:'search'}, function(vo){
		if(vo!=null){
			var $table=$('#xml-spending');
			$table.find('input[name="erpValue12"]').val(vo.deptRescNm);
			$table.find('input[name="erpValue13"]').val(vo.positNm);
			$table.find('input[name="erpValue15"]').val(vo.rescNm);
		}
	});
};<%//xml 수집 전에 호출함 %>
function checkFormBodyXML(){
	<c:if
	test="${formBodyMode eq 'edit'}">
		return validator.validate('xml\\-gubun');
	</c:if>
	return true;
}<%// 선택삭제%>
function delSelRow(conId){
	var arr = getCheckedTrs(conId, "cm.msg.noSelect");
	if(arr!=null) {
		delRowInArr(arr);
		<c:if test="${formBodyMode eq 'pop'}">dialog.resize("setDocBodyHtmlDialog");</c:if>
	}
}
<%// 행삭제%>
function delRow(conId){
	conId=conId.replace('/','\\/');
	var len=$("#"+conId+" tbody:first").children().not('#hiddenTr, #headerTr').length;
	if(!conId.endsWith('account') && len<2){
		alert('최소 1줄 이상 입력해야 합니다.');
		return;
	}
	var $tr = $("#"+conId+" tbody:first #hiddenTr").prev();
	delRowInArr([$tr[0]]);
	<c:if test="${formBodyMode eq 'pop'}">dialog.resize("setDocBodyHtmlDialog");</c:if>
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
<%// 다음 Row 번호 %>
var maxRowMap=null;
var gMaxRow = parseInt("${fn:length(formBodyXML.getChildList('body/rows'))}");
<%// 행추가%>
function addRow(conId, trId, strtCnt){
	if(strtCnt==undefined) strtCnt = 0;
	strtCnt++;
	conId=conId.replace('/','\\/');
	var $tr = $("#"+conId+" tbody:first #hiddenTr");
	var div=$tr.closest('div');
	$.uniform.restore(div.find('input, textarea, select, button'));
	var html = $tr[0].outerHTML;
	$maxRow=maxRowMap.get(trId);
	if($maxRow==null) $maxRow=0;
	html = html.replace(/_NO/g,'_'+(parseInt($maxRow)+parseInt(strtCnt)));
	$maxRow++;
	maxRowMap.put(trId, $maxRow);
	gMaxRow++;
	$tr.before(html);
	$tr = $tr.prev();
	$tr.attr('id',trId);
	$tr.attr('style','');
	//var seq=$("#"+conId+" tbody:first").children().not('#hiddenTr, #headerTr').length;
	//$tr.find('td#seq').text(seq++);
	div.find("input, textarea, select, button").uniform();
	<c:if test="${formBodyMode eq 'pop'}">dialog.resize("setDocBodyHtmlDialog");</c:if>
	
	if(trId=='row')
		setCalculate($tr.find("input[id^='erpValue6']"));
}
<%// 편집양식 인쇄모드 변경 %>
function setXmlEditViewMode(mode){
	$xmlArea = $("#xmlArea");
	var $xmlCont = $xmlArea.find("#xml-cont");
	var $xmlContView = $xmlArea.find("#xml-contView");
	
	if(mode=='print'){
		$xmlContView.html(editor("erpCont").getHtml());
		$xmlCont.hide();
		$xmlContView.show();
	} else {
		$xmlCont.show();
		$xmlContView.hide();
	}
}
$(document).ready(function(){
	<c:if
	test="${formBodyMode ne 'view'}">
	setCalculate();
	
	<%// 다음 Row 번호 %>
	maxRowMap=new ParamMap();
	// 초기 Row 세팅 
	maxRowMap.put('row', "${fn:length(formBodyXML.getChildList('body/rows'))}");
	maxRowMap.put('bank', "${fn:length(formBodyXML.getChildList('body/banks'))}");
	</c:if>
});

-->
</script><c:if
	test="${formBodyMode ne 'view'}">
<%
WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext(), 
		FrameworkServlet.SERVLET_CONTEXT_PREFIX+"appServlet"); // "appServlet"는 web.xml의 org.springframework.web.servlet.DispatcherServlet의 servlet-name을 지정.
//System.out.println("!!" + java.util.Arrays.asList(wac.getBeanDefinitionNames()));
UserVo userVo = LoginSession.getUser(request);
OrCmSvc orCmSvc = (OrCmSvc)wac.getBean("orCmSvc");
Map<String, Object> map = orCmSvc.getUserMap(userVo.getUserUid(), "ko");
request.setAttribute("userMap", map);
%>		
<div id="xmlArea" style="${formBodyMode eq 'pop' ? 'width:900px;' : ''}">

<div id="xml-head">
	<input type="hidden" name="typId" value="spendingResolution"/>
	<input type="hidden" name="ver" value="1"/>
</div>
<u:set var="erpBankYn" test="${empty formBodyXML.getAttr('body/spending.erpBankYn')}" value="N" elseValue="${formBodyXML.getAttr('body/spending.erpBankYn') }"/>
<div id="xml-body">
<div class="front notPrint"><div class="front_right"><u:buttonS alt="신청자 변경" title="신청자 변경" href="javascript:setUserPop();"/></div></div>
<u:listArea id="xml-spending" colgroup="15%,15%,29%,15%,13%,13%">
	<tr>
		<td class="head_ct"><u:mandatory />작성일</td>
		<td class="body_lt"><u:set var="erpValue11" test="${formBodyMode ne 'edit' && empty formBodyXML.getAttr('body/spending.erpValue11')}" value="today" elseValue="${formBodyXML.getAttr('body/spending.erpValue11')}"
		/><u:calendar id="erpValue11" title="작성일" value="${erpValue11}" mandatory="Y"/></td>
		<td class="head_ct">부서</td>
		<td class="body_lt"><u:set var="erpValue12" test="${!empty formBodyXML.getAttr('body/spending.erpValue12')}" value="${formBodyXML.getAttr('body/spending.erpValue12') }" elseValue="${userMap.deptRescNm }"
	/><u:input id="erpValue12" value="${erpValue12 }" title="부서" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct">직급</td>
		<td class="body_lt"><u:set var="erpValue13" test="${!empty formBodyXML.getAttr('body/spending.erpValue13')}" value="${formBodyXML.getAttr('body/spending.erpValue13') }" elseValue="${userMap.positNm }"
	/><u:input id="erpValue13" value="${erpValue13}" title="직급" style="width:90%;" maxByte="100" /></td>
	</tr>
	<tr>
		<td class="head_ct"><u:mandatory />지급일</td>
		<td class="body_lt"><u:calendar id="erpValue14" title="지급일" value="${formBodyXML.getAttr('body/spending.erpValue14')}" /></td>
		<td class="head_ct"><u:mandatory />성명</td>
		<td class="body_lt" colspan="3"><u:set var="erpValue15" test="${!empty formBodyXML.getAttr('body/spending.erpValue15')}" value="${formBodyXML.getAttr('body/spending.erpValue15') }" elseValue="${userMap.userNm }"
	/><u:input id="erpValue15" value="${erpValue15}" title="성명" style="width:95%;" maxByte="100" mandatory="Y"
	/><u:input type="hidden" id="erpBankYn" value="${erpBankYn}"/></td>
	</tr>
</u:listArea>
<u:set var="rowStrtCnt" test="${empty formBodyXML.getChildList('body/rows') }" value="${initStrtCnt }" elseValue="0"/><!-- 추가 Row수 -->
<div style="display:block;float:right;height:25px;"><u:buttonS title="행추가" onclick="addRow('xml-rows\\/row', 'row', '${rowStrtCnt }');" alt="행추가"/><u:buttonS title="행삭제" onclick="delRow('xml-rows/row');" alt="행삭제"/></div>
<u:listArea id="xml-rows/row" colgroup="15%,15%,29%,15%,13%,13%">
	<tr id="headerTr">
		<td class="head_ct">발생일자</td>
		<td class="head_ct">구분</td>
		<td class="head_ct">내역</td>
		<td class="head_ct">업체명</td>
		<td class="head_ct">지출 구분</td>
		<td class="head_ct">금액</td>
	</tr>
	<c:forEach
			items="${formBodyXML.getChildList('body/rows', 1, 1)}" var="row" varStatus="status" 
			><u:set test="${status.last}" var="trDisp" value="display:none"
		/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="row"
		/><u:set test="${status.last}" var="statusIndex" value="_NO" elseValue="_${status.index+1}"
		/>
		<tr id="${trId}" style="${trDisp}">
		<td class="body_ct"><u:calendar id="erpValue1${statusIndex}" name="erpValue1" title="발생일자" value="${row.getAttr('erpValue1')}" align="center"/></td>
		<td class="body_ct"><c:if test="${!empty gubuns }"><select name="erpValue2" id="erpValue2${statusIndex}">
			<c:forEach items="${gubuns}" var="gubun" varStatus="gubunStatus" >
				<u:option value="${gubunStatus.index}" title="${gubun}" selected="${empty row.getAttr('erpValue2') && gubunStatus.index==0}" 
				checkValue="${row.getAttr('erpValue2')}"/>
			</c:forEach>
		</select></c:if></td>
		<td class="body_ct"><u:input id="erpValue3${statusIndex}" name="erpValue3" value="${row.getAttr('erpValue3')}" title="내역" style="width:90%;" maxByte="100" /></td>
		<td class="body_ct"><u:input id="erpValue4${statusIndex}" name="erpValue4" value="${row.getAttr('erpValue4')}" title="업체명" style="width:90%;" maxByte="100" /></td>
		<td class="body_ct"><c:if test="${!empty spendingGubuns }"><select name="erpValue5" id="erpValue5${statusIndex}">
			<c:forEach items="${spendingGubuns}" var="gubun" varStatus="gubunStatus" >
				<u:option value="${gubunStatus.index}" title="${gubun}" selected="${empty row.getAttr('erpValue5') && gubunStatus.index==0}" 
				checkValue="${row.getAttr('erpValue5')}" />
			</c:forEach>
		</select></c:if></td>
		<td class="body_ct"><u:input id="erpValue6${statusIndex}" name="erpValue6" value="${row.getAttr('erpValue6')}" title="금액" style="width:90%;text-align:right;" maxByte="100" valueOption="number" commify="Y"/></td>
	</tr></c:forEach>
	<tr id="headerTr"><td class="head_ct" colspan="5">합계</td><td class="body_ct" id="xml-total"><u:input id="erpValue7" name="erpValue7" value="${formBodyXML.getAttr('body/total.erpValue7')}" title="합계" style="width:90%;text-align:right;" maxByte="100" readonly="Y"/></td></tr>
</u:listArea>

<c:if test="${erpBankYn eq 'N' }"><c:set var="bankStyle" value="display:none;" /></c:if>
<u:set var="bankStrtCnt" test="${empty formBodyXML.getChildList('body/banks') }" value="${initStrtCnt }" elseValue="0"/><!-- 추가 Row수 -->
<u:title title="계좌정보" type="small" alt="계좌정보">
<u:titleButton id="bankAddRowBtn" title="행추가" onclick="addRow('xml-banks\\/bank', 'bank', '${bankStrtCnt }');" alt="행추가" style="${bankStyle }"
/><u:titleButton id="bankDelRowBtn" title="행삭제" onclick="delRow('xml-banks/bank');" alt="행삭제" style="${bankStyle }"
/><c:if test="${erpBankYn eq 'N' }"><u:titleButton title="입력 열기" onclick="onBank(this);" alt="입력열기"/></c:if
><c:if test="${erpBankYn ne 'N' }"><u:titleButton title="입력 닫기" onclick="onBank(this);" alt="입력닫기"/></c:if>
</u:title>
<u:listArea id="xml-banks/bank" colgroup="10%,21%,10%,13%,10%,13%,10%,13%" style="${bankStyle }">
	<c:forEach
			items="${formBodyXML.getChildList('body/banks', 1, 1)}" var="bank" varStatus="status" 
			><u:set test="${status.last}" var="trDisp" value="display:none"
		/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="bank"
		/><u:set test="${status.last}" var="statusIndex" value="_NO" elseValue="_${status.index+1}"
		/><tr id="${trId}" style="${trDisp}">
		<td class="head_ct">업체명</td>
		<td class="body_lt"><u:set var="directYn" test="${empty formBodyXML.getChildList('body/accounts') || (!empty bank.getAttr('erpValue21') && bank.getAttr('erpValue21') eq 'direct')}" value="Y" elseValue="N"
		/><table style="width:100%;"><tr><c:if test="${!empty formBodyXML.getChildList('body/accounts') }"><td><select name="erpValue21" id="erpValue21${statusIndex}" onchange="chnBanks('erpValue21${statusIndex}');">
			<u:option value="empty" titleId="cm.select.actname" alt="선택" selected="${empty bank.getAttr('erpValue21') }"/>
			<u:option value="direct" title="직접입력" alt="직접입력" checkValue="${bank.getAttr('erpValue21') }"/>
			<c:forEach items="${formBodyXML.getChildList('body/accounts')}" var="account" varStatus="bankStatus" >
				<option value="${account.getAttr('erpValue21')}" <c:if test="${!empty bank.getAttr('erpValue21') && bank.getAttr('erpValue21') ne 'direct' && account.getAttr('erpValue21') eq bank.getAttr('erpValue21') }">selected="selected"</c:if> 
				data-bankNm="${account.getAttr('erpValue22')}" data-holderNm="${account.getAttr('erpValue23')}" data-number="${account.getAttr('erpValue24')}">${account.getAttr('erpValue21')}</option>
			</c:forEach>
		</select>
		</td></c:if><td><c:if test="${empty formBodyXML.getChildList('body/accounts') }"><input type="hidden" name="erpValue21" value="direct"/></c:if><div id="directContainer" <c:if test="${directYn eq 'N'}">style="display:none;"</c:if>
		><u:input id="erpValue222${statusIndex}" name="erpValue222" value="${bank.getAttr('erpValue222')}" title="업체명" style="width:90%;" maxByte="100" /></div></td></tr>
		</table></td>
		<td class="head_ct">은행명</td>
		<td class="body_ct"><u:input id="erpValue22${statusIndex}" name="erpValue22" value="${bank.getAttr('erpValue22')}" title="은행명" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct">예금주</td>
		<td class="body_ct"><u:input id="erpValue23${statusIndex}" name="erpValue23" value="${bank.getAttr('erpValue23')}" title="예금주" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct">계좌번호</td>
		<td class="body_ct"><u:input id="erpValue24${statusIndex}" name="erpValue24" value="${bank.getAttr('erpValue24')}" title="계좌번호" style="width:90%;" maxByte="100" /></td>
	</tr></c:forEach>
</u:listArea>

<c:if
	test="${formBodyMode eq 'edit'}">
	<!-- <div class="divider"><hr style="float:left;"/><strong>설 정</strong><hr class="right" style="float:right;"/></div> -->
	<div class="blank" style="border-bottom:2px dashed #d22681;margin-bottom:10px;text-align:center;"></div>
	<u:set var="accountStrtCnt" test="${empty formBodyXML.getChildList('body/accounts') }" value="${initStrtCnt }" elseValue="0"/><!-- 추가 Row수 -->
<u:title title="계좌정보 관리" type="small" alt="계좌정보 관리">
<u:titleButton title="행추가" onclick="addRow('xml-accounts\\/account', 'account', '${accountStrtCnt }');" alt="행추가"/><u:titleButton title="행삭제" onclick="delRow('xml-accounts/account');" alt="행삭제"/>
</u:title>
<u:listArea id="xml-accounts/account" colgroup="12%,13%,12%,13%,12%,13%,12%,13%">
	<c:forEach
			items="${formBodyXML.getChildList('body/accounts', 1, 1)}" var="account" varStatus="status" 
			><u:set test="${status.last}" var="trDisp" value="display:none"
		/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="account"
		/><u:set test="${status.last}" var="statusIndex" value="_NO" elseValue="_${status.index+1}"
		/><tr id="${trId}" style="${trDisp}">
		<td class="head_ct">업체명</td>
		<td class="body_ct"><u:input id="erpValue21${statusIndex}" name="erpValue21" value="${account.getAttr('erpValue21')}" title="업체명" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct">은행명</td>
		<td class="body_ct"><u:input id="erpValue22${statusIndex}" name="erpValue22" value="${account.getAttr('erpValue22')}" title="은행명" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct">예금주</td>
		<td class="body_ct"><u:input id="erpValue23${statusIndex}" name="erpValue23" value="${account.getAttr('erpValue23')}" title="예금주" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct">계좌번호</td>
		<td class="body_ct"><u:input id="erpValue24${statusIndex}" name="erpValue24" value="${account.getAttr('erpValue24')}" title="계좌번호" style="width:90%;" maxByte="100" /></td>
	</tr></c:forEach>
</u:listArea>

<u:title title="구분 관리" type="small" alt="구분 관리" />
<u:listArea id="xml-gubun" colgroup="15%,">
	<tr>
		<td class="head_lt">구분</td>
		<td class="body_lt"><u:input id="erpValue31" value="${formBodyXML.getAttr('body/gubun.erpValue31')}" 
		title="구분" style="width:97%;" maxByte="1000" mandatory="Y" onblur="chkGubuns(this);" /></td></tr>
	<tr>
		<td class="head_lt">지출 구분</td>
		<td class="body_lt"><u:input id="erpValue32" value="${formBodyXML.getAttr('body/gubun.erpValue32')}" 
		title="지출 구분" style="width:97%;" maxByte="300" mandatory="Y" onblur="chkGubuns(this);" /></td></tr><tr>
		<td class="body_lt" colspan="2"><div class="color_txt"><strong>(',') 콤마 구분</strong></div></td>
		</tr>
</u:listArea>
</c:if>
<c:if
	test="${formBodyMode ne 'edit'}">
	<div id="xml-accounts/account">
	<c:forEach
			items="${formBodyXML.getChildList('body/accounts', 1, 1)}" var="account" varStatus="status" 
			><div id="account"><u:input type="hidden" name="erpValue21" value="${account.getAttr('erpValue21')}" 
			/><u:input type="hidden" name="erpValue22" value="${account.getAttr('erpValue22')}" 
			/><u:input type="hidden" name="erpValue23" value="${account.getAttr('erpValue23')}" 
			/><u:input type="hidden" name="erpValue24" value="${account.getAttr('erpValue24')}" 
			/></div></c:forEach>
	</div>
	<div id="xml-gubun"><u:input type="hidden" id="erpValue31" value="${formBodyXML.getAttr('body/gubun.erpValue31')}" 
	/><u:input type="hidden" id="erpValue32" value="${formBodyXML.getAttr('body/gubun.erpValue32')}"  />
	</div>
	</c:if>
</div>

<div class="blank"></div>
<div><strong>※ 위 금액을 청구합니다.</strong></div>

<c:if
	test="${formBodyMode eq 'pop'}">
<u:buttonArea>
	<u:button titleId="cm.btn.confirm" onclick="setErpXMLPop();" alt="확인" auth="W" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</c:if>

</div></c:if><c:if
	test="${formBodyMode eq 'view'}">
<div style="border:2px solid #555555;padding:4px;">

<div>
<u:listArea colgroup="15%,15%,29%,15%,13%,13%">
	<tr>
		<td class="head_ct">작성일</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/spending.erpValue11')}" type="date"/></td>
		<td class="head_ct">부서</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/spending.erpValue12') }" /></td>
		<td class="head_ct">직급</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/spending.erpValue13')}" /></td>
	</tr>
	<tr>
		<td class="head_ct">지급일</td>
		<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/spending.erpValue14') }" type="date"/></td>
		<td class="head_ct">성명</td>
		<td class="body_lt" colspan="3"><u:out value="${formBodyXML.getAttr('body/spending.erpValue15')}" /></td>
	</tr>
</u:listArea>
<u:listArea colgroup="15%,15%,29%,15%,13%,13%">
	<tr>
		<td class="head_ct">발생일자</td>
		<td class="head_ct">구분</td>
		<td class="head_ct">내역</td>
		<td class="head_ct">업체명</td>
		<td class="head_ct">지출 구분</td>
		<td class="head_ct">금액</td>
	</tr>
	<c:forEach
			items="${formBodyXML.getChildList('body/rows')}" var="row" varStatus="status" >
	<tr>
		<td class="body_ct"><u:out value="${row.getAttr('erpValue1')}" type="date"/></td>
		<td class="body_ct"><c:if test="${!empty gubuns }">
			<c:forEach items="${gubuns}" var="gubun" varStatus="gubunStatus" >
				<c:if test="${gubunStatus.index eq row.getAttr('erpValue2') }">${gubun}</c:if>
			</c:forEach>
		</c:if></td>
		<td class="body_lt"><u:out value="${row.getAttr('erpValue3')}" /></td>
		<td class="body_lt"><u:out value="${row.getAttr('erpValue4')}" /></td>
		<td class="body_ct"><c:if test="${!empty spendingGubuns }">
			<c:forEach items="${spendingGubuns}" var="gubun" varStatus="gubunStatus" >
				<c:if test="${gubunStatus.index eq row.getAttr('erpValue5') }">${gubun}</c:if>
			</c:forEach>
		</c:if></td>
		<td class="body_rt"><u:out value="${row.getAttr('erpValue6')}" /></td>
	</tr></c:forEach>
	<tr><td class="head_ct" colspan="5">합계</td><td class="body_rt"><u:out value="${formBodyXML.getAttr('body/total.erpValue7')}" /></td></tr>
</u:listArea>

<c:if test="${!empty formBodyXML.getAttr('body/spending.erpBankYn') && formBodyXML.getAttr('body/spending.erpBankYn') ne 'N' }">
<u:title title="계좌정보" type="small" alt="계좌정보" />
<u:listArea colgroup="10%,21%,10%,13%,10%,13%,10%,13%">
	<c:forEach
			items="${formBodyXML.getChildList('body/banks')}" var="bank" varStatus="status" >
	<tr>
		<td class="head_ct">업체명</td>
		<td class="body_lt"><c:if test="${!empty bank.getAttr('erpValue21') && bank.getAttr('erpValue21') ne 'direct' }">
				<c:forEach items="${formBodyXML.getChildList('body/accounts')}" var="account" varStatus="bankStatus" >
					<c:if test="${account.getAttr('erpValue21') eq bank.getAttr('erpValue21') }">${account.getAttr('erpValue21')}</c:if>
				</c:forEach>
			</c:if><c:if test="${!empty bank.getAttr('erpValue21') && bank.getAttr('erpValue21') eq 'direct' }"><u:out value="${bank.getAttr('erpValue222')}" /></c:if>
		</td>
		<td class="head_ct">은행명</td>
		<td class="body_lt"><u:out value="${bank.getAttr('erpValue22')}" /></td>
		<td class="head_ct">예금주</td>
		<td class="body_lt"><u:out value="${bank.getAttr('erpValue23')}" /></td>
		<td class="head_ct">계좌번호</td>
		<td class="body_lt"><u:out value="${bank.getAttr('erpValue24')}" /></td>
	</tr></c:forEach>
</u:listArea>
</c:if>
</div>

<div class="blank"></div>
<div><strong>※ 위 금액을 청구합니다.</strong></div>
</div>
</c:if>