<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.Date,java.util.List,java.util.ArrayList" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
//고객코드
String custCode=(String)request.getAttribute("custCode");
//가족사항 초기 row수
request.setAttribute("fmyInitCnt", "5");

//사내외 교육 초기 row수
request.setAttribute("eduInitCnt", "2");

if("CD4B5E".equals(custCode)) //와이너리 투어 초기 row수
	request.setAttribute("tourInitCnt", "2");

//면허 및 자격 초기 row수
request.setAttribute("licenseInitCnt", "3");

//수상경력 초기 row수
request.setAttribute("prizeInitCnt", "2");

//외국어 초기 row수
request.setAttribute("forinLangInitCnt", "4");

//학력 초기 row수
request.setAttribute("achvmtInitCnt", "5");

//경력 초기 row수
request.setAttribute("careerInitCnt", "14");

//인사발령 초기 row수
request.setAttribute("insaInitCnt", "2");

//상벌 초기 row수
request.setAttribute("rewardInitCnt", "2");

List<String[]> tabList=new ArrayList<String[]>(); 
tabList.add(new String[]{"fmy", "가족사항"});
tabList.add(new String[]{"edu", "사내/외 교육"});
if("CD4B5E".equals(custCode))
	tabList.add(new String[]{"tour", "와이너리 투어"});
tabList.add(new String[]{"license", "면허 및 자격"});
tabList.add(new String[]{"prize", "수상경력"});
tabList.add(new String[]{"insa", "인사발령"});
tabList.add(new String[]{"etc", "기타"});
tabList.add(new String[]{"forinLang", "외국어"});
tabList.add(new String[]{"achvmt", "학력"});
tabList.add(new String[]{"army", "병역"});
tabList.add(new String[]{"career", "경력 및 중요업무 실적"});
tabList.add(new String[]{"reward", "상벌"});

// 탭 목록
request.setAttribute("tabList", tabList);

// 인사발령 목록
String[][] insaKndList = {
		{"승진", "승진"},
		{"인사이동", "인사이동"},
		{"전보발령", "전보발령"}
	};
request.setAttribute("insaKndList", insaKndList);

// 외국어 목록
String[][] forinLangGubunList = {
		{"상", "상"},
		{"중", "중"},
		{"하", "하"}
	};
request.setAttribute("forinLangGubunList", forinLangGubunList);

// 학력 졸업여부
String[][] graduationList = {
		{"졸업", "졸업"},
		{"중퇴", "중퇴"}
	};
request.setAttribute("graduationList", graduationList);

%>
<style type="text/css">
@media print {
#tabArea{display:none;}
#dftArea, #detlArea{display:block !important;}
td{color:#000000 !important;font:11px "dotum", "arial" !important;}
div.radio input{opacity: 1;filter: alpha(opacity=100);-moz-opacity: 1;}
}
</style>
<script type="text/javascript" src="${_cxPth}/js/calendar/moment.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/calendar/locales.min.js" charset="UTF-8"></script>
<u:params var="viewParams" excludes="menuId"/>
<c:set var="date" value="<%=new Date() %>"/>
<fmt:formatDate var="toYear" value="${date}" type="date" pattern="y"/>
<c:set var="strtYear" value="${toYear-70 }"/>
<c:set var="dftSelectYear" value="${toYear-15 }"/>
<u:set var="listUrlPrefix" test="${isMgm==true }" value="list" elseValue="view"/>
<!-- <div id="loadingContainer" style="display:none;"></div> -->
<script type="text/javascript">
<!--
//console.log('moment : '+moment().startOf('month').format('D'));
//console.log('moment : '+moment().endOf('month').format('D'));
<% // [자동계산:줄] %>
function setCalculate(obj){	
	$(obj).find("[id^='careerStrtYear'], [id^='careerStrtMonth'], [id^='careerEndYear'], [id^='careerEndMonth']").change(function(){
		$tr=$(this).closest('tr');
		var valMap={};
		var isValChk=true;
		$tr.find("[id^='careerStrtYear'], [id^='careerStrtMonth'], [id^='careerEndYear'], [id^='careerEndMonth']").each(function(){
			if($(this).val()==''){				
				isValChk=false;
				return false;
			}
			valMap[$(this).attr('name')]=$(this).val();	
		});
		if(isValChk){
			var moment1 = moment([valMap['careerStrtYear'], valMap['careerStrtMonth']-1, 1]);
			var moment2 = moment([valMap['careerEndYear'], valMap['careerEndMonth']-1, 1]).add(1, 'month');
			if(!moment2.isAfter(moment1, 'day')){
				// 값 초기화
				alert('시작일이 종료일보다 클 수 없습니다.');
				$tr.find("[id^='careerTotalYear'], [id^='careerTotalMonth']").val('');				
				return;
			}
			var monthGap=0;
			var diffYear=moment2.diff(moment1, 'year');
			if(parseInt(diffYear)>0){
				$tr.find("[id^='careerTotalYear']").val(diffYear);
				monthGap=parseInt(diffYear)*12;
			}else $tr.find("[id^='careerTotalYear']").val('');
			var diffMonth=moment2.diff(moment1, 'month');
			diffMonth=diffMonth-monthGap;
			if(parseInt(diffMonth)>0) $tr.find("[id^='careerTotalMonth']").val(diffMonth);
			else $tr.find("[id^='careerTotalMonth']").val('');
		}else{
			$tr.find("[id^='careerTotalYear'], [id^='careerTotalMonth']").val('');
		}
	});
}<% // [자동계산:이벤트 정의] %>
function initCalculate(){
	$('#xml-careers\\/career tbody:first tr').not('#headerTr, #hiddenTr').each(function(){
		setCalculate($(this));
	});
}
<% // [날짜선택] %>
function setDaySelect(obj, id, target){	
	var year=$('#'+id+'Year').val();
	//console.log('date : '+moment().format('YYYY-MM-DD'));
	var month=$(obj).val();
	var date=moment().clone();
	date.set('year', year);
	date.set('month', month);	
	//console.log('day : '+date.endOf('month').format('D'));
}
<% // [하단버튼:삭제] %>
function delPsnCard(cardNo) {
	var arr = [];
	arr.push(cardNo);
	if (arr != null && confirmMsg("cm.cfrm.del")) {<% // 삭제하시겠습니까? %>
		callAjax('./transPsnCardDelAjx.do?menuId=${menuId}', {cardNos:arr}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.replace('./${listUrlPrefix}PsnCard.do?${paramsForList }');
			}
		});
	}
};
<% // [하단버튼:수정] 수정화면으로 이동 %>
function setPsnCard(){
	location.href = './setPsnCard.do?${paramsForList}&cardNo=${param.cardNo}';
}<% // [하단버튼:목록] 목록으로 이동 %>
function goList() {
	location.href = './${listUrlPrefix}PsnCard.do?${paramsForList}';
}
var imgObj=null
<% //이미지 변경 - 팝업 오픈 %>
function setImagePop(obj, id){
	var url='./setImagePop.do?menuId=${menuId}';
	if(id!='') url+='&cardNo='+id;
	dialog.open('setImageDialog','<u:msg titleId="st.jsp.select.img" alt="이미지 선택" />', url);
}<% //이미지 상세보기 - 팝업 오픈 %>
function viewImagePop(id){
	var url='./viewImagePop.do?menuId=${menuId}';
	if(id!='') url+='&cardNo='+id;	
	dialog.open('viewImageDialog','<u:msg titleId="st.cols.img" alt="이미지" />', url);
}<% //이미지 ID 세팅 %>
function setTmpImgId(id, attrs){
	var $form=$('#setForm');
	$form.find("input[name='tmpImgId']").remove();
	$form.appendHidden({'name':'tmpImgId','value':id});
	var $img = $("#photo");
	$img.attr("src", '/cm/viewTmpImage.do?tmpFileId='+id);
	$img.removeAttr('width');
	$img.removeAttr('height');
	$img.attr(attrs.name, attrs.value);
}

<%// 선택삭제%>
function delSelRow(conId){
	var arr = getCheckedTrs(conId, "cm.msg.noSelect");
	if(arr!=null) {
		if(conId=='xml-careers/career'){
			$(arr).find("[id^='careerStrtYear'], [id^='careerStrtMonth'], [id^='careerEndYear'], [id^='careerEndMonth']").off('change');
		}			
		delRowInArr(arr);
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
		if(id!='headerTr' && id!='hiddenTr'){
			arr.push(obj);
			$rowspan=$(obj).find('td:first').attr('rowspan');
			if($rowspan!=undefined && $rowspan>1){
				for(var i=0;i<$rowspan-1;i++){
					arr.push($(obj).next());
				}
			}
		}
		
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
}
var maxRowMap=null, clickTabMap=null;

<%// 행추가%>
function addRow(conId, trId, strtCnt){
	if(strtCnt==undefined) strtCnt = 0;
	strtCnt++;
	conId=conId.replace('/','\\/');
	var $tr = $("#"+conId+" tbody:first #hiddenTr");
	$target=$("#"+conId+" tbody:first #hiddenTr:first");
	var html=null;
	$.each($tr, function(index, obj){
		html = obj.outerHTML;
		$maxRow=maxRowMap.get(trId);
		if($maxRow==null) $maxRow=0;
		html = html.replace(/_NO/g,'_'+(parseInt($maxRow)+parseInt(strtCnt)));
		$maxRow++;
		maxRowMap.put(trId, $maxRow);
		$target.before(html);
		$newTr = $target.prev();
		$newTr.attr('id',trId);
		$newTr.attr('style','');
		setJsUniform($newTr[0]);
		if(trId=='career') setCalculate($newTr[0]);
	});
}

<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, button, select").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
};

<c:if test="${formBodyMode ne 'view'}">
<%
//(formBodyXML) 관련 %>
function getFormBodyXML(){
	var xmlDoc = createXMLParser("<formBodyXML></formBodyXML>");
	var root = xmlDoc.getElementsByTagName("formBodyXML")[0];
	
	var head = xmlDoc.createElement("head");
	root.appendChild(head);
	
	var body = xmlDoc.createElement("body");
	root.appendChild(body);
	
	var $area = $("#xmlArea");
	$area.find("[id='xml-head'] :input").each(function(){
		$(head).attr($(this).attr('name'), $(this).val());
	});
	
	var elemId, loopId=null, p;
	$area.find("[id='xml-body'] [id^='xml-']").each(function(){
		elemId = $(this).attr('id').substring(4);
		p = elemId.indexOf('/');
		if(p>0){
			loopId = elemId.substring(p+1);
			elemId = elemId.substring(0,p);
			
			var pElem = xmlDoc.createElement(elemId), elem;
			body.appendChild(pElem);
			
			$(this).find("[id='"+loopId+"']").each(function(){
				//console.log('rowspan : '+$(this).children('td:first').attr('rowspan'));
				var elem = xmlDoc.createElement(loopId), tp, attNm, attVa;
				var valid = true;
				$(this).find(":input").each(function(){
					//if($(this).attr('data-exclude')=='Y') return true;
					if(valid){
						tp = $(this).attr('type');
						tp = tp==null ? '' : tp.toLowerCase();
						if(tp=='radio'||tp=='checkbox'){
							if(!this.checked) return;
						}
						attNm = $(this).attr('name');
						attVa = $(this).val();
						if(attNm!=null && !attNm.startsWith('exc')){
							$(elem).attr($(this).attr('name'), attVa);
							if($(this).attr('data-validation')=='Y' && attVa==''){
								valid = false;
							}
						}
					}
				});
				if(valid){
					pElem.appendChild(elem);
				}
			});
		} else {
			var elem = xmlDoc.createElement(elemId);
			body.appendChild(elem);
			var tp, attNm, attVa;
			$(this).find(":input").each(function(){
				//if($(this).attr('data-exclude')=='Y') return true;
				tp = $(this).attr('type');
				tp = tp==null ? '' : tp.toLowerCase();
				if(tp=='radio'||tp=='checkbox'){
					if(!this.checked) return;
				}
				attNm = $(this).attr('name');
				attVa = $(this).val();
				if(attNm!=null && !attNm.startsWith('exc')){
					$(elem).attr($(this).attr('name'), attVa);
					if($(this).attr('data-validation')=='Y' && attVa==''){
						valid = false;
					}
					if(this.tagName.toLowerCase()=='select'){
						$(elem).attr(attNm+'Nm', $(this).text());
					}
					
					if(attNm == 'barConnectedTypes' && attVa=='tel'){						
						$(this).closest('td').find("[id^='"+attVa+"']").each(function(index){
							if($(this).val()=='') return false;
							if(index==0) attVa='';
							if(index>0) attVa+='-';
							attVa+=$(this).val();
						});
						$(elem).attr('tel', attVa);
					}
				}
			});
		}
	});
	var ieVer8 = (browser.ie==true && browser.ver<9);
	if(ieVer8){
		var xml = serializeXML(xmlDoc).trim();
		xml = xml.replaceAll("\r\n", "&#10;");
		xml = xml.replaceAll("'",    "&apos;");
		return xml;
	} else {
		return serializeXML(xmlDoc);
	}
}
function createXMLParser(xmlString){
	if(window.DOMParser){
		var parser = new DOMParser();
		return parser.parseFromString(xmlString, "text/xml");
	} else {
		var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
		xmlDoc.async = false;
		xmlDoc.loadXML(xmlString);
     return xmlDoc;
	}
}
function serializeXML(xmlDoc){
	if(window.XMLSerializer){
		var serializer = new XMLSerializer();
		return serializer.serializeToString(xmlDoc);
	} else {
		return xmlDoc.xml;
	}
}<%
//사용자 정보 html 초기화 %>
function initOrUser(vo){
	var atts=null;
	atts=["ein", "koNm", "deptNm", "positNm", "mbno", "compNm", "joinDt"];
	$.each(atts, function(index, attNm){
		$('#xml-dft #'+attNm).val(vo==null ? '' : vo[attNm]);
	});
	atts=[["homeZipNo", "excHomeZipNo"], ["homeAdr", "excHomeAdr"], ["mbno", "excMbno"]];
	if(vo==null){
		$.each(['excMbno1', 'excMbno2', 'excMbno3'], function(index, key){
			$('#xml-dft #'+key).val('');
		});
	}
	$.each(atts, function(index, attNm){
		if(vo!=null && attNm[0]=='mbno' && vo['mbnos']!=null){
			var mbnos = JSON.parse(vo['mbnos']);
			$.each(mbnos, function(key, value){
				$('#xml-dft #'+key.replace('mbno', 'excMbno')).val(value);
			});
		}
		$('#xml-dft #'+attNm[1]).val(vo==null ? '' : vo[attNm[0]]);
	});
}<%
//사용자 정보 html 초기화 %>
function initOrUserImg(vo){
	$img=$('#xml-dft #photo');
	var imgPath=vo==null ? '${_cxPth}/images/${_skin}/photo_noimg.png' : vo.imgPath;
	$img.attr('src', imgPath);
	$img.removeAttr('width');
	$img.removeAttr('height');
	if(vo!=null){
		var maxWdth = 88;
		var maxhght = 110;
		var wdth=parseInt(vo.imgWdth);
		var hght=parseInt(vo.imgHght);
		var width = wdth <= maxWdth ? wdth : maxWdth;
		var height = hght <= maxhght ? hght : maxhght;
		if(wdth < hght) $img.attr('height', height);
		else $img.attr('width', width);
	}
}
<%
//사용자 정보 조회, html 세팅 %>
function setOrUser(){
	var ein=$('#xml-dft #ein').val();
	if(ein===undefined || ein=='') return;
	initOrUser(null);
	initOrUserImg(null);
	chkDupUser(ein, function(){
		callAjax('./getUserMapAjx.do?menuId=${menuId}', {ein:ein}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {				
				if(data.cuPsnCardBVo!=null){
					initOrUser(data.cuPsnCardBVo.map);
				}
				if(data.cuPsnImgDVo!=null){
					initOrUserImg(data.cuPsnImgDVo.map);
				}
				
			}
		}, null, null, true);
	});
	//if(!chkDupUser()) return;
	
}<%
//사용자 정보 조회, html 세팅 %>
function chkDupUser(ein, handler){
	callAjax('./isDupUserAjx.do?menuId=${menuId}', {ein:ein}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}else{
			if (data.result == 'ok' && data.dupYn=='N') handler();
			else {//cm.dup.title="{0}"(은)는 이미 등록되어 있습니다.
				alertMsg('cm.dup.title', [ein]);
				//$('#xml-dft #ein').val('');
			}			
		}
				
	}, null, null, true);
}
<%
//xml 히든값 세팅 및, html 변환 세팅 %>
function setXMLByValue(formBodyXML, applyHtmlArea){
	var $xmlDataArea = $("#formBodyXMLDataArea");
	$xmlDataArea.html('');
	
	var ieVer8 = (browser.ie==true && browser.ver<9);
	if(ieVer8){
		var escapeVa = formBodyXML;
		escapeVa = escapeVa.replaceAll("&quot;", "&amp;quot;");
		escapeVa = escapeVa.replaceAll("&#10;",  "\r\n");
		$xmlDataArea.appendHidden({'name':'bodyXml','value':escapeVa});
	} else {
		$xmlDataArea.appendHidden({'name':'bodyXml','value':formBodyXML});
	}
	
	var array = $('#xml-dft :input').serializeArray();
    $.each( array, function(index, field ) {
    	$xmlDataArea.appendHidden({'name':field.name,'value':field.value});
    });

}
<%
//저장 - xml 세팅 %>
function save(){
	var $xmlArea = $("#xmlArea");
	if($xmlArea.length>0){
		var formBodyXML = '';
		if(window.collectFormBodyXML){
			formBodyXML = collectFormBodyXML();
			if(formBodyXML==null) return false;
		} else if(window.checkFormBodyXML && checkFormBodyXML()==false){
			return false;
		} else {
			formBodyXML = getFormBodyXML();
		}
		if(formBodyXML!=''){
			//loading('xml-body', true);
			setXMLByValue(formBodyXML, true);
			
			var $form = $('#setForm');
			$form.attr('method','post');
			$form.attr('action','./transPsnCard.do?menuId=${menuId}');
			$form.attr('target','dataframe');		
			$form[0].submit();
			//loading('xml-body', false);
		}
	}
}

<%//xml 수집 전에 호출함 %>
function checkFormBodyXML(){
	return validator.validate('xml\\-body');
}
<%//초기화 %>
function reset(){
	location.replace(location.href);
}
<%// 탭 uniform 적용 %>
function setUniform(id){
	if(clickTabMap.get(id)!=null && !clickTabMap.get(id)){
		$("#"+id+"Area tbody").children().each(function(){
			<%// 행추가 영역 제외하고 uniform 적용%>
			if($(this).attr('id')!='hiddenTr') setJsUniform(this);
		});
		clickTabMap.put(id, true);
	}
}
</c:if>
$(document).ready(function() {
	<c:if test="${formBodyMode eq 'view'}">setUniformCSS();</c:if>	
	<c:if test="${formBodyMode ne 'view'}">
	setUniformCSS($('#xml-dft'));
	initCalculate();
	//setCalculate();
	//$("#xml-body #dftArea tbody").children().each(function(){
		<%// 행추가 영역 제외하고 uniform 적용%>
	//	if($(this).attr('id')!='hiddenTr'){
	//		setJsUniform(this);
	//	}
	//});
	//loading($('div.content'), false);
	<%// 다음 Row 번호 %>
	maxRowMap=new ParamMap();
	// 초기 Row 세팅 
	maxRowMap.put('fmy', "${fn:length(formBodyXML.getChildList('body/fmys'))}");
	maxRowMap.put('edu', "${fn:length(formBodyXML.getChildList('body/edus'))}");
	<c:if test="${custCode eq 'CD4B5E'}">maxRowMap.put('tour', "${fn:length(formBodyXML.getChildList('body/tours'))}");</c:if>
	maxRowMap.put('license', "${fn:length(formBodyXML.getChildList('body/licenses'))}");
	maxRowMap.put('prize', "${fn:length(formBodyXML.getChildList('body/prizes'))}");
	maxRowMap.put('forinLang', "${fn:length(formBodyXML.getChildList('body/forinLangs'))}");
	maxRowMap.put('achvmt', "${fn:length(formBodyXML.getChildList('body/achvmts'))}");
	maxRowMap.put('career', "${fn:length(formBodyXML.getChildList('body/careers'))}");
	
	<%// tab uniform 적용 여부 초기화 %>
	clickTabMap=new ParamMap();
	<c:forEach var="tab" items="${tabList }" varStatus="status">
	clickTabMap.put('${tab[0]}', false);
	</c:forEach>
	</c:if>
});

//loading($('div.content'), true);
//-->
</script>
<u:title menuNameFirst="true"/>
<% // 탭 %>
<c:if
	test="${formBodyMode ne 'view'}">
<u:tabGroup id="tabArea" noBottomBlank="">
<u:tab id="tabArea" areaId="xml-dft" titleId="wb.jsp.setBc.tab.dftInfo" alt="기본정보" on="true" />
<%-- <u:tab id="tabArea" areaId="detlArea" titleId="wb.jsp.setBc.tab.addInfo" alt="추가정보" /> --%>
<c:forEach var="tab" items="${tabList }" varStatus="status">
<u:tab id="tabArea" areaId="${tab[0] }Area" title="${tab[1] }" alt="${tab[1] }" onclick="setUniform('${tab[0] }');"/>
</c:forEach>
</u:tabGroup>	
<% // 하단 버튼 %>
<u:buttonArea>
<u:button titleId="cm.btn.reset" alt="초기화" href="javascript:reset();" auth="W" />
<u:button titleId="cm.btn.save" onclick="save();" alt="저장" auth="W" />
<c:if test="${isMgm==true || (!empty cuPsnCardBVo && !empty cuPsnCardBVo.cardNo)}"><u:button titleId="cm.btn.cancel" alt="취소" href="javascript:history.go(-1);" /></c:if>
</u:buttonArea>	
<c:set var="dftAreaColspan" value="3%,13%,10%,13%,13%,12%,10%,8%,8%,10%"/>
<div id="xmlArea">
<div id="xml-body">
<div id="dftArea" >

<u:listArea id="xml-dft" colgroup="8%,8%,10%,13%,13%,12%,10%,8%,8%,10%">
	<tr>
		<td colspan="2" class="head_ct"><u:mandatory />사원번호</td>
		<td colspan="5"><u:input id="ein" title="사원번호" style="width:85%;" value="${cuPsnCardBVo.ein }" maxByte="30" mandatory="Y" readonly="${isMgm==true ? 'N' : 'Y'}"/><c:if test="${isMgm==true }"><u:buttonS alt="조회" title="조회" onclick="setOrUser();"/></c:if></td>
		<td colspan="2" class="head_ct"><u:mandatory />소속법인</td>
		<td><u:input id="compNm" title="소속법인" style="width:85%;" value="${cuPsnCardBVo.compNm }" mandatory="Y" maxByte="120"/></td>
	</tr>
	<tr>
		<td colspan="2" rowspan="5">
			<dl>
			<dd class="photo" style="text-align:center;padding:5px;">
				<c:set var="maxWdth" value="88" />
				<c:set var="maxHght" value="110" />
				<u:set test="${cuPsnImgDVo != null}" var="imgPath" value="${_cxPth}${cuPsnImgDVo.imgPath}" elseValue="${_cxPth}/images/${_skin}/photo_noimg.png" />
				<u:set test="${cuPsnImgDVo != null && cuPsnImgDVo.imgWdth <= maxWdth}" var="imgWdth" value="${cuPsnImgDVo.imgWdth}" elseValue="${maxWdth}" />
				<u:set test="${cuPsnImgDVo != null && cuPsnImgDVo.imgHght <= maxHght}" var="imgHght" value="${cuPsnImgDVo.imgHght}" elseValue="${maxHght}" />
				<u:set test="${cuPsnImgDVo.imgWdth < cuPsnImgDVo.imgHght}" var="imgWdthHgth" value="height='${imgHght}'" elseValue="width='${imgWdth}'" />
				<img id="photo" src="${imgPath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' ${imgWdthHgth}>
			</dd>
			<dd class="photo_btn" style="text-align:center;"><u:buttonS alt="사진변경" titleId="cm.btn.chg" onclick="setImagePop(this, '${bbPsnCardBVo.cardNo }');" popYn="Y" /></dd>
			</dl>
		</td>
		<td class="head_ct">부서명</td>
		<td colspan="4"><u:input id="deptNm" title="부서명" style="width:85%;" value="${cuPsnCardBVo.deptNm }" maxByte="120"/></td>
		<td colspan="2" class="head_ct" >입사일자</td>
		<td><u:calendar id="joinDt" title="입사일자" value="${!empty cuPsnCardBVo && !empty cuPsnCardBVo.joinDt ? cuPsnCardBVo.joinDt : formBodyXML.getAttr('body/dft.joinDt')}"/></td>
	</tr>
	<tr>
		<td class="head_ct">팀명</td>
		<td colspan="4"><u:input id="teamNm" title="팀명" style="width:85%;" value="${formBodyXML.getAttr('body/dft.teamNm')}" maxByte="120"/></td>
		<td colspan="2" class="head_ct" >직위/직책</td>
		<td><u:input id="positNm" title="직위/직책" style="width:85%;" value="${cuPsnCardBVo.positNm }" maxByte="60"/></td>
	</tr>
	<tr>
		<td class="head_ct" rowspan="3">성명</td>
		<td class="head_ct"><u:mandatory />한글</td>
		<td colspan="6"><u:input id="koNm" title="성명(한글)" style="width:85%;" value="${cuPsnCardBVo.koNm }" mandatory="Y" maxByte="30"/></td>		
	</tr>
	<tr>
		<td class="head_ct">한문</td>
		<td colspan="6"><u:input id="znNm" title="성명(한문)" style="width:85%;" value="${formBodyXML.getAttr('body/dft.znNm')}" maxByte="30"/></td>		
	</tr>
	<tr>
		<td class="head_ct">영문</td>
		<td colspan="6"><u:input id="enNm" title="성명(영문)" style="width:85%;" value="${formBodyXML.getAttr('body/dft.enNm')}" maxByte="60"/></td>		
	</tr>
	<tr>
		<td colspan="2" class="head_ct">주민등록번호</td>
		<td colspan="4"><u:ssn id="excSsn" title="주민등록번호" value="${cuPsnCardBVo.ssn}" /></td>
		<td class="head_ct">생년월일</td>
		<td colspan="3">
			<table border="0" cellpadding="0" cellspacing="0"><tr><td><u:calendar id="excBirth" title="생년월일" value="${cuPsnCardBVo.birth}"/></td>
				<td class="width10"></td><td><u:checkArea><u:checkbox name="solaLunaYn" value="N" titleId="wc.option.luna" alt="음력" checkValue="${formBodyXML.getAttr('body/dft.solaLunaYn')}"/></u:checkArea></td></tr></table></td>
	</tr>
	<tr>
		<td colspan="2" class="head_ct">현주소</td>
		<td colspan="8"><u:address id="excHome" alt="현주소" adrTitle="현주소" zipNoTitle="우편번호" adrStyle="width:94%" zipNoValue="${cuPsnCardBVo.homeZipNo }" adrValue="${cuPsnCardBVo.homeAdr }" readonly="Y" /></td>
	</tr>
	<tr>
		<td colspan="2" class="head_ct">본인의 휴대폰 번호</td>
		<td colspan="4"><u:phone id="excMbno" title="본인의 휴대폰 번호" value="${cuPsnCardBVo.mbno}" type="text"/></td>
		<td colspan="2" class="head_ct">긴급연락처</td>
		<td colspan="2"><u:phone id="tel" title="긴급연락처" value="${formBodyXML.getAttr('body/dft.tel')}"/></td>
	</tr>
	<tr>
		<td colspan="2" class="head_ct">장애여부</td>
		<td colspan="3">
			<u:checkArea>
				<u:radio name="disabilityYn" value="Y" title="대상" checkValue="${formBodyXML.getAttr('body/dft.disabilityYn')}" alt="대상" />
				<u:radio name="disabilityYn" value="N" title="비대상" checkValue="${formBodyXML.getAttr('body/dft.disabilityYn')}" alt="비대상" checked="${empty formBodyXML.getAttr('body/dft.disabilityYn')}"/>
			</u:checkArea>
		</td>
		<td class="head_ct">장애구분</td>
		<td colspan="2"><u:input id="disabilityTyp" title="장애구분" style="width:85%;" value="${formBodyXML.getAttr('body/dft.disabilityTyp')}" maxByte="30"/></td>
		<td class="head_ct">장애등급</td>
		<td><u:input id="disabilityGrade" title="장애등급" style="width:50px;" value="${formBodyXML.getAttr('body/dft.disabilityGrade')}" maxByte="30"/> 급</td>
	</tr>
	<tr>
		<td colspan="2" class="head_ct">보훈여부</td>
		<td colspan="3">
			<u:checkArea>
				<u:radio name="redmPatrtsmYn" value="Y" title="대상" checkValue="${formBodyXML.getAttr('body/dft.redmPatrtsmYn')}" alt="대상"/>
				<u:radio name="redmPatrtsmYn" value="N" title="비대상" checkValue="${formBodyXML.getAttr('body/dft.redmPatrtsmYn')}" alt="비대상" checked="${empty formBodyXML.getAttr('body/dft.redmPatrtsmYn')}" />
			</u:checkArea>
		</td>
		<td class="head_ct">보훈번호</td>
		<td colspan="2"><u:input id="redmPatrtsmNo" title="보훈번호" style="width:85%;" value="${formBodyXML.getAttr('body/dft.redmPatrtsmNo')}" maxByte="30"/></td>
		<td class="head_ct">관계</td>
		<td><u:input id="relation" title="관계" style="width:85%;" value="${formBodyXML.getAttr('body/dft.relation')}" maxByte="30"/></td>
	</tr>
</u:listArea>
<div id="fmyArea" style="display:none;">
<u:set var="fmyStrtCnt" test="${empty formBodyXML.getChildList('body/fmys') }" value="${fmyInitCnt }" elseValue="0"/><!-- 가족사항 추가 Row수 -->
<u:title title="가족사항" type="small" alt="가족사항">
<u:titleButton title="추가" onclick="addRow('xml-fmys/fmy', 'fmy', '${fmyStrtCnt }');" alt="추가"/><u:titleButton title="선택삭제" onclick="delSelRow('xml-fmys/fmy');" alt="선택삭제"/>
</u:title>
<u:listArea id="xml-fmys/fmy" colgroup="${dftAreaColspan }">
	<tr id="headerTr">
		<td class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('xml-fmys\\/fmy', this.checked);" value=""/></td>
		<td colspan="2" class="head_ct" >성명</td>
		<td class="head_ct" >관계</td>
		<td colspan="2" class="head_ct" >생년월일</td>
		<td colspan="3" class="head_ct" >직업</td>
		<td class="head_ct" >기타</td>		
	</tr>
	<c:forEach begin="0" end="${fn:length(formBodyXML.getChildList('body/fmys'))+fmyStrtCnt}" var="index" varStatus="status"
		><c:set var="xmlMap" value="${status.last ? '' : formBodyXML.getChildList('body/fmys')[index]}"
		/><u:set test="${status.last}" var="trDisp" value="display:none"
		/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="fmy"
		/><u:set test="${status.last}" var="statusIndex" value="_NO" elseValue="_${status.index+1}"
		/>
	<tr id="${trId}" style="${trDisp}">
		<td class="bodybg_ct"><input type="checkbox" value="" /></td>
		<td colspan="2" class="bodybg_ct"><u:input id="fmyNm" title="성명" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('fmyNm') }"/></td>
		<td class="bodybg_ct"><u:input id="fmyRelation" title="관계" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('fmyRelation') }"/></td>
		<td colspan="2" class="bodybg_ct"><table border="0" cellpadding="0" cellspacing="0"><tr><td><u:calendar id="fmyBirth${statusIndex }" name="fmyBirth" title="생년월일" value="${empty xmlMap ? '' :  xmlMap.getAttr('fmyBirth') }"/></td>
				<td class="width10"></td><td><u:checkArea><u:checkbox id="fmySolaLunaYn${statusIndex }" name="fmySolaLunaYn" value="N" titleId="wc.option.luna" alt="음력" checkValue="${empty xmlMap ? '' :  xmlMap.getAttr('fmySolaLunaYn') }"/></u:checkArea></td></tr></table>
		</td>
		<td colspan="3" class="bodybg_ct"><u:input id="fmyJob" title="직업" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('fmyJob') }"/></td>
		<td class="bodybg_ct"><u:input id="fmyEtc" title="기타" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('fmyEtc') }"/></td>
	</tr>
	</c:forEach>
</u:listArea>
</div><div id="eduArea" style="display:none;">
<u:set var="eduStrtCnt" test="${empty formBodyXML.getChildList('body/edus') }" value="${eduInitCnt }" elseValue="0"/><!-- 초기 Row수 -->
<u:title title="사내/외 교육" type="small" alt="사내/외 교육">
<u:titleButton title="추가" onclick="addRow('xml-edus/edu', 'edu', '${eduStrtCnt }');" alt="추가"/><u:titleButton title="선택삭제" onclick="delSelRow('xml-edus/edu');" alt="선택삭제"/>
</u:title>
<u:listArea id="xml-edus/edu" colgroup="${dftAreaColspan }">
	<tr id="headerTr">
		<td class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('xml-edus\\/edu', this.checked);" value=""/></td>
		<td colspan="3" class="head_ct" >교육명</td>
		<td colspan="2" class="head_ct" >교육기간</td>
		<td colspan="3" class="head_ct" >교육기관</td>
		<td class="head_ct" >비고</td>
	</tr>
	<c:forEach begin="0" end="${fn:length(formBodyXML.getChildList('body/edus'))+eduStrtCnt}" var="index" varStatus="status"
		><c:set var="xmlMap" value="${status.last ? '' : formBodyXML.getChildList('body/edus')[index]}"
		/><u:set test="${status.last}" var="trDisp" value="display:none"
		/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="edu"
		/><u:set test="${status.last}" var="statusIndex" value="_NO" elseValue="_${status.index+1}"
		/>
	<tr id="${trId}" style="${trDisp}">
		<td class="bodybg_ct"><input type="checkbox" value="" /></td>
		<td colspan="3" class="bodybg_ct"><u:input id="eduNm" title="교육명" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('eduNm') }"/></td>
		<td colspan="2" class="bodybg_ct">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
				<td><u:calendar id="eduStrt${statusIndex }" name="eduStrt" title="교육기간" option="{end:'eduEnd'}" value="${empty xmlMap ? '' :  xmlMap.getAttr('eduStrt') }"/></td>
				<td class="search_body_ct">~</td>
				<td><u:calendar id="eduEnd${statusIndex }" name="eduEnd" title="교육기간" option="{start:'eduStrt'}" value="${empty xmlMap ? '' :  xmlMap.getAttr('eduEnd') }"/></td>
				</tr>
			</table>
		</td>
		<td colspan="3" class="bodybg_ct"><u:input id="eduOrgan" title="교육기관" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('eduOrgan') }"/></td>
		<td class="bodybg_ct"><u:input id="eduEtc" title="비고" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('eduEtc') }"/></td>
	</tr>
	</c:forEach>
</u:listArea>
</div><c:if test="${custCode eq 'CD4B5E'}"><div id="tourArea" style="display:none;">
<u:set var="tourStrtCnt" test="${empty formBodyXML.getChildList('body/tours') }" value="${tourInitCnt }" elseValue="0"/><!-- 초기 Row수 -->
<u:title title="와이너리 투어" type="small" alt="와이너리 투어">
<u:titleButton title="추가" onclick="addRow('xml-tours/tour', 'tour', '${tourStrtCnt }');" alt="추가"/><u:titleButton title="선택삭제" onclick="delSelRow('xml-tours/tour');" alt="선택삭제"/>
</u:title>
<u:listArea id="xml-tours/tour" colgroup="${dftAreaColspan }">
	<tr id="headerTr">
		<td class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('xml-tours\\/tour', this.checked);" value=""/></td>
		<td colspan="3" class="head_ct" >와이너리명(국가, 지역)</td>
		<td colspan="2" class="head_ct" >투어기간</td>
		<td colspan="4" class="head_ct" >사유</td>
	</tr>
	<c:forEach begin="0" end="${fn:length(formBodyXML.getChildList('body/tours'))+tourStrtCnt}" var="index" varStatus="status"
		><c:set var="xmlMap" value="${status.last ? '' : formBodyXML.getChildList('body/tours')[index]}"
		/><u:set test="${status.last}" var="trDisp" value="display:none"
		/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tour"
		/><u:set test="${status.last}" var="statusIndex" value="_NO" elseValue="_${status.index+1}"
		/>
	<tr id="${trId}" style="${trDisp}">
		<td class="bodybg_ct"><input type="checkbox" value="" /></td>
		<td colspan="3" class="bodybg_ct"><u:input id="tourAreaNm" title="와이너리명" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('tourAreaNm') }"/></td>
		<td colspan="2" class="bodybg_ct">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
				<td><u:calendar id="tourStrt${statusIndex }" name="tourStrt" title="투어기간 시작일" option="{end:'tourEnd'}" value="${empty xmlMap ? '' :  xmlMap.getAttr('tourStrt') }"/></td>
				<td class="search_body_ct">~</td>
				<td><u:calendar id="tourEnd${statusIndex }" name="tourEnd" title="투어기간 종료일" option="{start:'tourStrt'}" value="${empty xmlMap ? '' :  xmlMap.getAttr('tourEnd') }"/></td>
				</tr>
			</table>
		</td>
		<td colspan="4" class="bodybg_ct"><u:input id="tourReason" title="사유" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('tourReason') }"/></td>
	</tr>	
	</c:forEach>
</u:listArea>
</div></c:if><div id="licenseArea" style="display:none;">
<u:set var="licenseStrtCnt" test="${empty formBodyXML.getChildList('body/licenses') }" value="${licenseInitCnt }" elseValue="0"/><!-- 초기 Row수 -->
<u:title title="면허 및 자격" type="small" alt="면허 및 자격">
<u:titleButton title="추가" onclick="addRow('xml-licenses/license', 'license', '${licenseStrtCnt }');" alt="추가"/><u:titleButton title="선택삭제" onclick="delSelRow('xml-licenses/license');" alt="선택삭제"/>
</u:title>
<u:listArea id="xml-licenses/license" colgroup="${dftAreaColspan }">
	<tr id="headerTr">
		<td class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('xml-licenses\\/license', this.checked);" value=""/></td>
		<td colspan="2" class="head_ct" >면허 및 자격내용</td>
		<td colspan="2" class="head_ct" >취득년월일</td>
		<td colspan="2" class="head_ct" >등록번호</td>
		<td colspan="2" class="head_ct" >등록연월일</td>
		<td class="head_ct" >발급처</td>
	</tr>
	<c:forEach begin="0" end="${fn:length(formBodyXML.getChildList('body/licenses'))+licenseStrtCnt}" var="index" varStatus="status"
		><c:set var="xmlMap" value="${status.last ? '' : formBodyXML.getChildList('body/licenses')[index]}"
		/><u:set test="${status.last}" var="trDisp" value="display:none"
		/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="license"
		/><u:set test="${status.last}" var="statusIndex" value="_NO" elseValue="_${status.index+1}"
		/>
	<tr id="${trId}" style="${trDisp}">
		<td class="bodybg_ct"><input type="checkbox" value="" /></td>
		<td colspan="2" class="bodybg_ct"><u:input id="licenseCont" title="면허 및 자격내용" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('licenseCont') }"/></td>
		<td colspan="2" class="bodybg_ct"><u:calendar id="licenseGetDt${statusIndex }" name="licenseGetDt" title="취득년월일" value="${empty xmlMap ? '' :  xmlMap.getAttr('licenseGetDt') }"/></td>
		<td colspan="2" class="bodybg_ct"><u:input id="licenseRegNo" title="등록번호" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('licenseRegNo') }"/></td>
		<td colspan="2" class="bodybg_ct"><u:calendar id="licenseRegDt${statusIndex }" name="licenseRegDt" title="등록연월일" value="${empty xmlMap ? '' :  xmlMap.getAttr('licenseRegDt') }"/></td>
		<td class="bodybg_ct"><u:input id="licenseIsuOrg" title="발급처" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('licenseIsuOrg') }"/></td>
	</tr>
	</c:forEach>
</u:listArea>	
</div><div id="prizeArea" style="display:none;">
<u:set var="prizeStrtCnt" test="${empty formBodyXML.getChildList('body/prizes') }" value="${prizeInitCnt }" elseValue="0"/><!-- 초기 Row수 -->
<u:title title="수상경력(외부기관에서 포상받은 내용)" type="small" alt="수상경력(외부기관에서 포상받은 내용)">
<u:titleButton title="추가" onclick="addRow('xml-prizes/prize', 'prize', '${prizeStrtCnt }');" alt="추가"/><u:titleButton title="선택삭제" onclick="delSelRow('xml-prizes/prize');" alt="선택삭제"/>
</u:title>
<u:listArea id="xml-prizes/prize" colgroup="${dftAreaColspan }">
	<tr id="headerTr">
		<td class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('xml-prizes\\/prize', this.checked);" value=""/></td>
		<td colspan="2" class="head_ct" >수상 구분</td>
		<td colspan="2" class="head_ct" >연월일</td>
		<td colspan="2" class="head_ct" >내용</td>
		<td colspan="2" class="head_ct" >수상수여 기관/자</td>
		<td class="head_ct" >비고</td>
	</tr>
	<c:forEach begin="0" end="${fn:length(formBodyXML.getChildList('body/prizes'))+prizeStrtCnt}" var="index" varStatus="status"
		><c:set var="xmlMap" value="${status.last ? '' : formBodyXML.getChildList('body/prizes')[index]}"
		/><u:set test="${status.last}" var="trDisp" value="display:none"
		/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="prize"
		/><u:set test="${status.last}" var="statusIndex" value="_NO" elseValue="_${status.index+1}"
		/>
	<tr id="${trId}" style="${trDisp}">
		<td class="bodybg_ct"><input type="checkbox" value="" /></td>
		<td colspan="2" class="bodybg_ct"><u:input id="prizeTyp" title="수상 구분" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('prizeTyp') }"/></td>
		<td colspan="2" class="bodybg_ct"><u:calendar id="prizeDt${statusIndex }" name="prizeDt" title="연월일" value="${empty xmlMap ? '' :  xmlMap.getAttr('prizeDt') }"/></td>
		<td colspan="2" class="bodybg_ct"><u:input id="prizeCont" title="내용" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('prizeCont') }"/></td>
		<td colspan="2" class="bodybg_ct"><u:input id="prizeOrgNm" title="수상수여 기관/자" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('prizeOrgNm') }"/></td>
		<td class="bodybg_ct"><u:input id="prizeEtc" title="비고" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('prizeEtc') }"/></td>
	</tr>
	</c:forEach>
</u:listArea>	
</div><div id="insaArea" style="display:none;">
<u:set var="insaStrtCnt" test="${empty formBodyXML.getChildList('body/insas') }" value="${insaInitCnt }" elseValue="0"/><!-- 초기 Row수 -->
<u:title title="인사발령" type="small" alt="인사발령">
<u:titleButton title="추가" onclick="addRow('xml-insas/insa', 'insa', '${insaStrtCnt }');" alt="추가"/><u:titleButton title="선택삭제" onclick="delSelRow('xml-insas/insa');" alt="선택삭제"/>
</u:title>
<u:listArea id="xml-insas/insa" colgroup="${dftAreaColspan }">
	<tr id="headerTr">
		<td class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('xml-insas\\/insa', this.checked);" value=""/></td>
		<td class="head_ct" >종류</td>
		<td class="head_ct" >직위</td>
		<td class="head_ct" >직책</td>
		<td colspan="2" class="head_ct" >법인명/부서명/팀명</td>
		<td class="head_ct" >인사발령일</td>
		<td colspan="3" class="head_ct" >비고</td>
	</tr>
	<c:forEach begin="0" end="${fn:length(formBodyXML.getChildList('body/insas'))+insaStrtCnt}" var="index" varStatus="status"
		><c:set var="xmlMap" value="${status.last ? '' : formBodyXML.getChildList('body/insas')[index]}"
		/><u:set test="${status.last}" var="trDisp" value="display:none"
		/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="insa"
		/><u:set test="${status.last}" var="statusIndex" value="_NO" elseValue="_${status.index+1}"
		/>
	<tr id="${trId}" style="${trDisp}">
		<td class="bodybg_ct"><input type="checkbox" value="" /></td>
		<td class="bodybg_ct"><select id="insaKnd${statusIndex }" name="insaKnd" ><u:option value="" title="선택" selected="${empty xmlMap}"/>
			<c:forEach items="${insaKndList}" var="list" varStatus="listStatus">
				<u:option value="${list[0]}" title="${list[1]}" checkValue="${empty xmlMap ? '' : xmlMap.getAttr('insaKnd')}" />
			</c:forEach>
		</select>
		</td>
		<td class="bodybg_ct"><u:input id="insaPositNm" title="직위" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('insaPositNm') }"/></td>
		<td class="bodybg_ct"><u:input id="insaTitleNm" title="직책" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('insaTitleNm') }"/></td>
		<td colspan="2" class="bodybg_ct"><u:input id="insaCorpNm" title="법인명/부서명/팀명" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('insaCorpNm') }"/></td>
		<td class="bodybg_ct"><u:calendar id="insaIssueDt${statusIndex }" name="insaIssueDt" title="인사발령일" value="${empty xmlMap ? '' :  xmlMap.getAttr('insaIssueDt') }"/></td>
		<td colspan="3" class="bodybg_ct"><u:input id="insaEtc" title="비고" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('insaEtc') }"/></td>
	</tr>
	</c:forEach>
</u:listArea>
</div><div id="rewardArea" style="display:none;">
<u:set var="rewardStrtCnt" test="${empty formBodyXML.getChildList('body/rewards') }" value="${rewardInitCnt }" elseValue="0"/><!-- 초기 Row수 -->
<u:title title="상벌" type="small" alt="상벌">
<u:titleButton title="추가" onclick="addRow('xml-rewards/reward', 'reward', '${rewardStrtCnt }');" alt="추가"/><u:titleButton title="선택삭제" onclick="delSelRow('xml-rewards/reward');" alt="선택삭제"/>
</u:title>
<u:listArea id="xml-rewards/reward" colgroup="${dftAreaColspan }">
	<tr id="headerTr">
		<td class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('xml-rewards\\/reward', this.checked);" value=""/></td>
		<td colspan="2" class="head_ct" >표창/징계 구분</td>
		<td colspan="2" class="head_ct" >날짜</td>
		<td colspan="3" class="head_ct" >내용</td>
		<td colspan="2" class="head_ct" >상벌권자</td>
	</tr>
	<c:forEach begin="0" end="${fn:length(formBodyXML.getChildList('body/rewards'))+rewardStrtCnt}" var="index" varStatus="status"
		><c:set var="xmlMap" value="${status.last ? '' : formBodyXML.getChildList('body/rewards')[index]}"
		/><u:set test="${status.last}" var="trDisp" value="display:none"
		/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="reward"
		/><u:set test="${status.last}" var="statusIndex" value="_NO" elseValue="_${status.index+1}"
		/>
	<tr id="${trId}" style="${trDisp}">
		<td class="bodybg_ct"><input type="checkbox" value="" /></td>
		<td colspan="2" class="bodybg_ct"><u:input id="rewardGubun" title="표창/징계 구분" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('rewardGubun') }"/></td>
		<td colspan="2" class="bodybg_ct"><u:calendar id="rewardDt${statusIndex }" name="rewardDt" title="날짜" value="${empty xmlMap ? '' :  xmlMap.getAttr('rewardDt') }"/></td>
		<td colspan="3" class="bodybg_ct"><u:input id="rewardCont" title="내용" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('rewardCont') }"/></td>
		<td colspan="2" class="bodybg_ct"><u:input id="rewardUser" title="상벌권자" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('rewardUser') }"/></td>
	</tr>
	</c:forEach>
</u:listArea>
</div>
</div><!-- dftArea End -->
<!-- <hr style="border: none;border-top: 2px dotted blue;color: #fff;background-color: #fff;height: 1px;width: 100%;display:block;"/> -->


<c:set var="detlAreaColspan" value="3%,15%,15%,10%,13%,9%,9%,8%,8%,10%"/>

<div id="detlArea">
<div id="etcArea" style="display:none;">
<u:title title="기타" type="small" alt="기타" />
<u:listArea id="xml-unusual" colgroup="8%,13%,13%,9%,13%,9%,9%,8%,8%,10%">
	<tr>
		<td class="head_ct">성별</td>
		<td colspan="2">
			<u:checkArea>
				<u:radio name="gendar" value="M" title="남" checkValue="${formBodyXML.getAttr('body/unusual.gendar')}" alt="남" checked="${empty formBodyXML.getAttr('body/unusual.gendar') }"/>
				<u:radio name="gendar" value="F" title="여" checkValue="${formBodyXML.getAttr('body/unusual.gendar')}" alt="여" />
			</u:checkArea>
		</td>
		<td class="head_ct">결혼기념일</td>
		<td><u:calendar id="weddAnnvDt" title="결혼기념일" value="${formBodyXML.getAttr('body/unusual.weddAnnvDt')}"/></td>
		<td class="head_ct">구분</td>
		<td colspan="4">
			<u:checkArea>
				<u:radio name="positTypCd" value="R" title="정규직" checkValue="${formBodyXML.getAttr('body/unusual.positTypCd')}" alt="정규직" checked="${empty formBodyXML.getAttr('body/unusual.positTypCd') }"/>
				<u:radio name="positTypCd" value="C" title="계약직" checkValue="${formBodyXML.getAttr('body/unusual.positTypCd')}" alt="계약직" />
				<u:radio name="positTypCd" value="T" title="임시직" checkValue="${formBodyXML.getAttr('body/unusual.positTypCd')}" alt="임시직" />
			</u:checkArea>
		</td>
	</tr>
	<tr>
		<td class="head_ct">결혼유무</td>
		<td colspan="2">
			<u:checkArea>
				<u:radio name="weddYn" value="Y" title="기혼" checkValue="${formBodyXML.getAttr('body/unusual.weddYn')}" alt="기혼" />
				<u:radio name="weddYn" value="N" title="미혼" checkValue="${formBodyXML.getAttr('body/unusual.weddYn')}" alt="미혼" checked="${empty formBodyXML.getAttr('body/unusual.weddYn') }"/>
			</u:checkArea>
		</td>
		<td class="head_ct">종교</td>
		<td><u:input id="religion" title="종교" style="width:85%;" maxByte="30" value="${formBodyXML.getAttr('body/unusual.religion')}"/></td>
		<td class="head_ct">취미</td>
		<td colspan="2"><u:input id="hobby" title="취미" style="width:85%;" maxByte="30" value="${formBodyXML.getAttr('body/unusual.hobby')}"/></td>
		<td class="head_ct">특기</td>
		<td><u:input id="specialty" title="특기" style="width:85%;" maxByte="30" value="${formBodyXML.getAttr('body/unusual.specialty')}"/></td>
	</tr>
</u:listArea>
</div>
<div id="forinLangArea" style="display:none;">
<u:set var="forinLangStrtCnt" test="${empty formBodyXML.getChildList('body/forinLangs') }" value="${forinLangInitCnt }" elseValue="0"/><!-- 초기 Row수 -->
<u:title title="외국어" type="small" alt="외국어">
<u:titleButton title="추가" onclick="addRow('xml-forinLangs/forinLang', 'forinLang', '${forinLangStrtCnt }');" alt="추가"/><u:titleButton title="선택삭제" onclick="delSelRow('xml-forinLangs/forinLang');" alt="선택삭제"/>
</u:title>
<u:listArea id="xml-forinLangs/forinLang" colgroup="${detlAreaColspan }">
	<tr id="headerTr">
		<td class="head_ct" ><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('xml-forinLangs\\/forinLang', this.checked);" value=""/></td>
		<td colspan="2" class="head_ct">외국어명</td>
		<td class="head_ct">Speaking</td>
		<td class="head_ct">Writing</td>
		<td class="head_ct">Reading</td>
		<td colspan="2" class="head_ct">공인점수</td>
		<td colspan="2" class="head_ct">취득연월일</td>
	</tr>	
	<c:forEach begin="0" end="${fn:length(formBodyXML.getChildList('body/forinLangs'))+forinLangStrtCnt}" var="index" varStatus="status"
		><c:set var="xmlMap" value="${status.last ? '' : formBodyXML.getChildList('body/forinLangs')[index]}"
		/><u:set test="${status.last}" var="trDisp" value="display:none"
		/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="forinLang"
		/><u:set test="${status.last}" var="statusIndex" value="_NO" elseValue="_${status.index+1}"
		/>
		<tr id="${trId}" style="${trDisp}">
			<td class="bodybg_ct"><input type="checkbox" value="" /></td>
			<td colspan="2" class="bodybg_ct"><u:input id="foreignLangNm" title="외국어명" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('foreignLangNm') }"/></td>
			<td class="bodybg_ct"><select id="speaking${statusIndex }" name="speaking" ><u:option value="" title="선택" selected="${empty xmlMap}"/>
				<c:forEach items="${forinLangGubunList}" var="list" varStatus="listStatus">
					<u:option value="${list[0]}" title="${list[1]}" checkValue="${empty xmlMap ? '' : xmlMap.getAttr('speaking')}" />
				</c:forEach>
			</select></td>
			<td class="bodybg_ct"><select id="writing${statusIndex }" name="writing" ><u:option value="" title="선택" selected="${empty xmlMap}"/>
				<c:forEach items="${forinLangGubunList}" var="list" varStatus="listStatus">
					<u:option value="${list[0]}" title="${list[1]}" checkValue="${empty xmlMap ? '' : xmlMap.getAttr('writing')}" />
				</c:forEach>
			</select></td>
			<td class="bodybg_ct"><select id="reading${statusIndex }" name="reading" ><u:option value="" title="선택" selected="${empty xmlMap}"/>
				<c:forEach items="${forinLangGubunList}" var="list" varStatus="listStatus">
					<u:option value="${list[0]}" title="${list[1]}" checkValue="${empty xmlMap ? '' : xmlMap.getAttr('reading')}" />
				</c:forEach>
			</select></td>
			<td colspan="2" class="bodybg_ct"><u:input id="officialScore" title="공인점수" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('officialScore') }"/></td>
			<td colspan="2" class="bodybg_ct"><u:calendar id="foreignLangDt${statusIndex }" name="foreignLangDt" title="취득연월일" value="${empty xmlMap ? '' :  xmlMap.getAttr('foreignLangDt') }"/></td>
		</tr>
	</c:forEach>
</u:listArea>
</div><div id="achvmtArea" style="display:none;">
<u:set var="achvmtStrtCnt" test="${empty formBodyXML.getChildList('body/achvmts') }" value="${achvmtInitCnt }" elseValue="0"/><!-- 초기 Row수 -->
<u:title title="학력(해외연수 포함)" type="small" alt="학력(해외연수 포함)">
<u:titleButton title="추가" onclick="addRow('xml-achvmts/achvmt', 'achvmt', '${achvmtStrtCnt }');" alt="추가"/><u:titleButton title="선택삭제" onclick="delSelRow('xml-achvmts/achvmt');" alt="선택삭제"/>
</u:title>
<u:listArea id="xml-achvmts/achvmt" colgroup="${detlAreaColspan }">
	<tr id="headerTr">
		<td class="head_ct" ><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('xml-achvmts\\/achvmt', this.checked);" value=""/></td>
		<td class="head_ct">연 / 월</td>
		<td class="head_ct">연 / 월</td>
		<td colspan="3" class="head_ct">학교명</td>
		<td colspan="2" class="head_ct">전공</td>
		<td class="head_ct">졸업여부</td>
		<td class="head_ct">비고</td>
	</tr>
	<c:forEach begin="0" end="${fn:length(formBodyXML.getChildList('body/achvmts'))+achvmtStrtCnt}" var="index" varStatus="status"
		><c:set var="xmlMap" value="${status.last ? '' : formBodyXML.getChildList('body/achvmts')[index]}"
		/><u:set test="${status.last}" var="trDisp" value="display:none"
		/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="achvmt"
		/><u:set test="${status.last}" var="statusIndex" value="_NO" elseValue="_${status.index+1}"
		/>
		<tr id="${trId}" style="${trDisp}">
			<td class="bodybg_ct"><input type="checkbox" value="" /></td>
			<td>
				<select name="achvmtStrtYear"><u:option value="" title="연"/><c:forEach var="year" begin="${strtYear }" end="${toYear }" step="1"><u:option value="${year }" title="${year }" 
				checkValue="${empty xmlMap ? '' :  xmlMap.getAttr('achvmtStrtYear') }"/></c:forEach></select>
				<select name="achvmtStrtMonth"><u:option value="" title="월"/><c:forEach var="month" begin="1" end="12" step="1"><u:option value="${month }" title="${month }" 
				checkValue="${empty xmlMap ? '' :  xmlMap.getAttr('achvmtStrtMonth') }"/></c:forEach></select>
			</td>
			<td>
				<select name="achvmtEndYear"><u:option value="" title="연"/><c:forEach var="year" begin="${strtYear }" end="${toYear }" step="1"><u:option value="${year }" title="${year }" 
				checkValue="${empty xmlMap ? '' :  xmlMap.getAttr('achvmtEndYear') }"/></c:forEach></select>
				<select name="achvmtEndMonth"><u:option value="" title="월"/><c:forEach var="month" begin="1" end="12" step="1"><u:option value="${month }" title="${month }" 
				checkValue="${empty xmlMap ? '' :  xmlMap.getAttr('achvmtEndMonth') }"/></c:forEach></select>
			</td>
			<td colspan="3"><u:input id="schoolNm" title="학교명" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('schoolNm') }"/></td>
			<td colspan="2"><u:input id="majorNm" title="전공" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('majorNm') }"/></td>
			<td class="body_ct"><select id="graduation${statusIndex }" name="graduation" ><u:option value="" title="선택" selected="${empty xmlMap}"/>
				<c:forEach items="${graduationList}" var="list" varStatus="listStatus">
					<u:option value="${list[0]}" title="${list[1]}" checkValue="${empty xmlMap ? '' : xmlMap.getAttr('graduation')}" />
				</c:forEach>
			</select></td>
			<td><u:input id="achvmtEtc" title="비고" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('achvmtEtc') }"/></td>
		</tr>
	</c:forEach>
</u:listArea>	
</div><div id="armyArea" style="display:none;">
<u:title title="병역" type="small" alt="병역" />
<u:listArea id="xml-armyServe" colgroup="${dftAreaColspan }">
	<tr>
		<td class="head_ct" rowspan="2">병역</td>
		<td class="head_ct">군필여부</td>
		<td class="body_ct"><input type="checkbox" name="joinArmyYn" value="Y" title="군필여부" class="bodybg_lt" <c:if test="${formBodyXML.getAttr('body/armyServe.joinArmyYn') eq 'Y' }">checked="checked"</c:if>/></td>		
		<td class="head_ct">군별</td>
		<td><u:input id="joinArmyTyp" title="군별" style="width:85%;" maxByte="30" value="${formBodyXML.getAttr('body/armyServe.joinArmyTyp')}"/></td>
		<td class="head_ct">계급</td>
		<td><u:input id="joinArmyClass" title="계급" style="width:85%;" maxByte="30" value="${formBodyXML.getAttr('body/armyServe.joinArmyClass')}"/></td>
		<td class="head_ct">복무기간</td>
		<td colspan="2">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
				<td><u:calendar id="strtDt" title="복무기간 시작일" option="{end:'armyServeEndDt'}" value="${formBodyXML.getAttr('body/armyServe.strtDt')}"/></td>
				<td class="search_body_ct">~</td>
				<td><u:calendar id="endDt" title="복무기간  종료일" option="{start:'armyServeStrtDt'}" value="${formBodyXML.getAttr('body/armyServe.endDt')}"/></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="head_ct">병과</td>
		<td><u:input id="armOfService" title="병과" style="width:85%;" maxByte="60" value="${formBodyXML.getAttr('body/armyServe.armOfService')}"/></td>
		<td class="head_ct">미필사유</td>
		<td colspan="6"><u:input id="notArmCont" title="미필사유" style="width:85%;" maxByte="240" value="${formBodyXML.getAttr('body/armyServe.notArmCont')}"/></td>
	</tr>
</u:listArea>	
</div><div id="careerArea" style="display:none;">
<u:set var="careerStrtCnt" test="${empty formBodyXML.getChildList('body/careers') }" value="${careerInitCnt }" elseValue="0"/><!-- 초기 Row수 -->
<u:title title="경력 및 중요업무 실적" type="small" alt="경력 및 중요업무 실적">
<u:titleButton title="추가" onclick="addRow('xml-careers/career', 'career', '${careerStrtCnt }');" alt="추가"/><u:titleButton title="선택삭제" onclick="delSelRow('xml-careers/career');" alt="선택삭제"/>
</u:title>
<u:listArea id="xml-careers/career" colgroup="${detlAreaColspan }">
	<tr id="headerTr">
		<td class="head_ct" ><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('xml-careers\\/career', this.checked);" value=""/></td>
		<td class="head_ct">연 / 월</td>
		<td class="head_ct">연 / 월</td>
		<td class="head_ct">근무년 / 개월수</td>
		<td colspan="2" class="head_ct">회사명</td>
		<td class="head_ct">부서</td>
		<td class="head_ct">직위</td>
		<td colspan="2" class="head_ct">담당업무</td>
	</tr>
	<c:forEach begin="0" end="${fn:length(formBodyXML.getChildList('body/careers'))+careerStrtCnt}" var="index" varStatus="status"
		><c:set var="xmlMap" value="${status.last ? '' : formBodyXML.getChildList('body/careers')[index]}"
		/><u:set test="${status.last || status.index-1 == index}" var="isLast" value="true" elseValue="false"
		/><u:set test="${isLast}" var="trDisp" value="display:none"
		/><u:set test="${isLast}" var="trId" value="hiddenTr" elseValue="career"
		/><u:set test="${isLast}" var="statusIndex" value="_NO" elseValue="_${status.index+1}"
		/>
		<c:if test="${status.index==0 || status.index%2==0 || isLast}">
		<tr id="${trId}" style="${trDisp}">
		<td class="bodybg_ct" rowspan="2"><input type="checkbox" value="" /></td>
		<td>
			<select id="careerStrtYear" name="careerStrtYear"><u:option value="" title="연"/><c:forEach var="year" begin="${strtYear }" end="${toYear }" step="1"><u:option value="${year }" title="${year }" 
			checkValue="${empty xmlMap ? '' :  xmlMap.getAttr('careerStrtYear') }"/></c:forEach></select>
			<select id="careerStrtMonth" name="careerStrtMonth"><u:option value="" title="월"/><c:forEach var="month" begin="1" end="12" step="1"><u:option value="${month }" title="${month }" 
			checkValue="${empty xmlMap ? '' :  xmlMap.getAttr('careerStrtMonth') }"/></c:forEach></select>
		</td>
		<td>
			<select id="careerEndYear" name="careerEndYear"><u:option value="" title="연"/><c:forEach var="year" begin="${strtYear }" end="${toYear }" step="1"><u:option value="${year }" title="${year }" 
			checkValue="${empty xmlMap ? '' :  xmlMap.getAttr('careerEndYear') }"/></c:forEach></select>
			<select id="careerEndMonth" name="careerEndMonth"><u:option value="" title="월"/><c:forEach var="month" begin="1" end="12" step="1"><u:option value="${month }" title="${month }" 
			checkValue="${empty xmlMap ? '' :  xmlMap.getAttr('careerEndMonth') }"/></c:forEach></select>
		</td>
		<td>
			<u:input id="careerTotalYear" title="근무년" style="width:20px;" valueOption="number" value="${empty xmlMap ? '' :  xmlMap.getAttr('careerTotalYear') }" />/
			<u:input id="careerTotalMonth" title="개월수" style="width:20px;" valueOption="number" value="${empty xmlMap ? '' :  xmlMap.getAttr('careerTotalMonth') }" />
		</td>
		<td colspan="2"><u:input id="careerCompNm" title="회사명" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('careerCompNm') }"/></td>
		<td><u:input id="careerDeptNm" title="부서명" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('careerDeptNm') }"/></td>
		<td><u:input id="careerPositNm" title="직위명" style="width:82%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('careerPositNm') }"/></td>
		<td colspan="2"><u:input id="careerPichNm" title="담당업무" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('careerPichNm') }"/></td>
	</tr>
	</c:if>
	<c:if test="${status.index==1 || status.index%2>0 || isLast}">
	<tr id="${trId}" style="${trDisp}">
		<td colspan="2" class="head_ct">업무</td>
		<td colspan="7"><u:input id="careerDutyNm${statusIndex }" name="careerDutyNm" title="업무" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('careerDutyNm') }"/></td>
	</tr>
	</c:if>
	</c:forEach>
</u:listArea>	
</div>
</div><!-- detlArea End -->
</div>
</div>
<u:blank />
<form id="setForm">
<u:input type="hidden" id="menuId" value="${menuId }"/>
<u:input type="hidden" id="listPage" value="./${listUrlPrefix}PsnCard.do?${paramsForList }"/>
<u:input type="hidden" id="viewPage" value="./viewPsnCard.do?${viewParams }"/>
<c:if test="${!empty cuPsnCardBVo && !empty cuPsnCardBVo.cardNo}"><u:input type="hidden" id="cardNo" value="${cuPsnCardBVo.cardNo }"/></c:if>
<div id="formBodyXMLDataArea"></div>
</form>
<% // 하단 버튼 %>
<u:buttonArea topBlank="true">
<u:button titleId="cm.btn.reset" alt="초기화" href="javascript:reset();" auth="W" />
<u:button titleId="cm.btn.save" onclick="save();" alt="저장" auth="W" />
<c:if test="${isMgm==true || (!empty cuPsnCardBVo && !empty cuPsnCardBVo.cardNo)}"><u:button titleId="cm.btn.cancel" alt="취소" href="javascript:history.go(-1);" /></c:if>
</u:buttonArea>
</c:if><c:if
	test="${formBodyMode eq 'view'}">
<% // 하단 버튼 %>
<u:buttonArea>
<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" auth="R" />
<c:if test="${isAdmin==true || (!empty isModify && isModify == true)}">
<u:button titleId="cm.btn.mod" onclick="setPsnCard();" alt="수정" auth="W" />
<u:button titleId="cm.btn.del" alt="삭제" onclick="delPsnCard('${cuPsnCardBVo.cardNo }');" auth="W"/>
</c:if>
<c:if test="${isMgm==true }"><u:button titleId="cm.btn.list" alt="목록" onclick="goList();" /></c:if>
</u:buttonArea>
	
<div id="xml-body">
<u:listArea id="dftArea" colgroup="8%,8%,10%,13%,13%,12%,10%,8%,8%,10%">
	<tr>
		<td colspan="2" class="head_ct">사원번호</td>
		<td colspan="5" class="body_lt"><u:out value="${formBodyXML.getAttr('body/dft.ein')}"/></td>
		<td colspan="2" class="head_ct">소속법인</td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/dft.compNm')}"/></td>
	</tr>
	<tr>
		<td colspan="2" rowspan="5">
			<dl>
			<dd class="photo" style="text-align:center;">
				<c:set var="maxWdth" value="88" />
				<c:set var="maxHght" value="110" />
				<u:set test="${cuPsnImgDVo != null}" var="imgPath" value="${_cxPth}${cuPsnImgDVo.imgPath}" elseValue="${_cxPth}/images/${_skin}/photo_noimg.png" />
				<u:set test="${cuPsnImgDVo != null && cuPsnImgDVo.imgWdth <= maxWdth}" var="imgWdth" value="${cuPsnImgDVo.imgWdth}" elseValue="${maxWdth}" />
				<u:set test="${cuPsnImgDVo != null && cuPsnImgDVo.imgHght <= maxHght}" var="imgHght" value="${cuPsnImgDVo.imgHght}" elseValue="${maxHght}" />
				<u:set test="${cuPsnImgDVo.imgWdth < cuPsnImgDVo.imgHght}" var="imgWdthHgth" value="height='${imgHght}'" elseValue="width='${imgWdth}'" />
				<c:if test="${!empty cuPsnImgDVo}">
					<a href="javascript:viewImagePop('${cuPsnCardBVo.cardNo }');" ><img src="${imgPath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' ${imgWdthHgth}></a>
				</c:if>
				<c:if test="${empty cuPsnImgDVo }"><img src="${imgPath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' ${imgWdthHgth}></c:if>
			</dd>
			</dl>
		</td>
		<td class="head_ct">부서명</td>
		<td colspan="4" class="body_ct"><u:out value="${formBodyXML.getAttr('body/dft.deptNm')}"/></td>
		<td colspan="2" class="head_ct" >입사일자</td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/dft.joinDt')}" type="date"/></td>
	</tr>
	<tr>
		<td class="head_ct">팀명</td>
		<td colspan="4" class="body_ct"><u:out value="${formBodyXML.getAttr('body/dft.teamNm')}"/></td>
		<td colspan="2" class="head_ct" >직위/직책</td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/dft.positNm')}"/></td>
	</tr>
	<tr>
		<td class="head_ct" rowspan="3">성명</td>
		<td class="head_ct">한글</td>
		<td colspan="6" class="body_lt"><u:out value="${formBodyXML.getAttr('body/dft.koNm')}"/></td>		
	</tr>
	<tr>
		<td class="head_ct">한문</td>
		<td colspan="6" class="body_lt"><u:out value="${formBodyXML.getAttr('body/dft.znNm')}"/></td>		
	</tr>
	<tr>
		<td class="head_ct">영문</td>
		<td colspan="6" class="body_lt"><u:out value="${formBodyXML.getAttr('body/dft.enNm')}"/></td>		
	</tr>
	<tr>
		<td colspan="2" class="head_ct">주민등록번호</td>
		<td colspan="4" class="body_lt"><u:out value="${!empty cuPsnCardBVo.ssn ? fn:substring(cuPsnCardBVo.ssn,0,6) : ''}"/>${!empty cuPsnCardBVo.ssn ? '-*******' : ''}</td>
		<td colspan="2" class="head_ct">생년월일</td>
		<td colspan="2" class="body_ct"><u:out value="${cuPsnCardBVo.birth}" type="date"/><c:if 
		test="${!empty cuPsnCardBVo.birth && !empty formBodyXML.getAttr('body/dft.solaLunaYn') && formBodyXML.getAttr('body/dft.solaLunaYn') eq 'N'}"> (음력)</c:if></td>
	</tr>
	<tr>
		<td colspan="2" class="head_ct">현주소</td>
		<td colspan="8" class="body_lt"><u:address id="excHome" alt="현주소" adrTitle="현주소" zipNoTitle="우편번호" adrStyle="width:94%" zipNoValue="${cuPsnCardBVo.homeZipNo }" adrValue="${cuPsnCardBVo.homeAdr }" readonly="Y" type="view"/></td>
	</tr>
	<tr>
		<td colspan="2" class="head_ct">본인의 휴대폰 번호</td>
		<td colspan="4" class="body_ct"><u:phone id="excMbno" value="${cuPsnCardBVo.mbno }" type="view"/></td>
		<td colspan="2" class="head_ct">긴급연락처</td>
		<td colspan="2" class="body_ct"><u:phone id="tel" value="${formBodyXML.getAttr('body/dft.tel')}" type="view"/></td>
	</tr>
	<tr>
		<td colspan="2" class="head_ct">장애여부</td>
		<td colspan="3" class="body_ct">
			<u:checkArea>
				<u:radio name="disabilityYn" value="Y" title="대상" checkValue="${formBodyXML.getAttr('body/dft.disabilityYn')}" alt="대상" disabled="Y"/>
				<u:radio name="disabilityYn" value="N" title="비대상" checkValue="${formBodyXML.getAttr('body/dft.disabilityYn')}" alt="비대상" disabled="Y"/>
			</u:checkArea>
		</td>
		<td class="head_ct">장애구분</td>
		<td colspan="2" class="body_ct"><u:out value="${formBodyXML.getAttr('body/dft.disabilityTyp')}"/></td>
		<td class="head_ct">장애등급</td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/dft.disabilityGrade')}"/> 급</td>
	</tr>
	<tr>
		<td colspan="2" class="head_ct">보훈여부</td>
		<td colspan="3" class="body_ct">
			<u:checkArea>
				<u:radio name="redmPatrtsmYn" value="Y" title="대상" checkValue="${formBodyXML.getAttr('body/dft.redmPatrtsmYn')}" alt="대상" disabled="Y"/>
				<u:radio name="redmPatrtsmYn" value="N" title="비대상" checkValue="${formBodyXML.getAttr('body/dft.redmPatrtsmYn')}" alt="비대상" disabled="Y"/>
			</u:checkArea>
		</td>
		<td class="head_ct">보훈번호</td>
		<td colspan="2" class="body_ct"><u:out value="${formBodyXML.getAttr('body/dft.redmPatrtsmNo')}"/></td>
		<td class="head_ct">관계</td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/dft.relation')}"/></td>
	</tr>
	<tr>
		<td class="head_ct" rowspan="${fn:length(formBodyXML.getChildList('body/fmys'))+1}">가족사항</td>
		<td colspan="2" class="head_ct" >성명</td>
		<td class="head_ct" >관계</td>
		<td colspan="2" class="head_ct" >생년월일</td>
		<td colspan="3" class="head_ct" >직업</td>
		<td class="head_ct" >기타</td>		
	</tr>
	<c:forEach items="${formBodyXML.getChildList('body/fmys')}" var="xmlMap" varStatus="status"
		><tr>
		<td colspan="2" class="body_ct"><u:out value="${xmlMap.getAttr('fmyNm')}"/></td>
		<td class="body_ct"><u:out value="${xmlMap.getAttr('fmyRelation')}"/></td>
		<td colspan="2" class="body_ct"><u:out value="${xmlMap.getAttr('fmyBirth')}" type="date"/><c:if 
		test="${!empty xmlMap.getAttr('fmyBirth') && !empty xmlMap.getAttr('fmySolaLunaYn') && xmlMap.getAttr('fmySolaLunaYn') eq 'N'}"> (음력)</c:if></td>
		<td colspan="3" class="body_ct"><u:out value="${xmlMap.getAttr('fmyJob')}"/></td>
		<td class="body_ct"><u:out value="${xmlMap.getAttr('fmyEtc')}"/></td>
	</tr>
	</c:forEach>
	<tr>
		<td class="head_ct" rowspan="${fn:length(formBodyXML.getChildList('body/edus'))+1}">사내/외<br />교육</td>
		<td colspan="3" class="head_ct" >교육명</td>
		<td colspan="2" class="head_ct" >교육기간</td>
		<td colspan="3" class="head_ct" >교육기관</td>
		<td class="head_ct" >비고</td>		
	</tr>
	<c:forEach items="${formBodyXML.getChildList('body/edus')}" var="xmlMap" varStatus="status"
		><tr>
		<td colspan="3" class="body_lt"><u:out value="${xmlMap.getAttr('eduNm')}"/></td>
		<td colspan="2" class="body_ct"><u:out value="${xmlMap.getAttr('eduStrt')}" type="date"/> ~ <u:out value="${xmlMap.getAttr('eduEnd')}" type="date"/></td>
		<td colspan="3" class="body_ct"><u:out value="${xmlMap.getAttr('eduOrgan')}"/></td>
		<td class="body_lt"><u:out value="${xmlMap.getAttr('eduEtc')}"/></td>
	</tr>
	</c:forEach>
	<c:if test="${custCode eq 'CD4B5E'}">
	<tr>
		<td class="head_ct" rowspan="${fn:length(formBodyXML.getChildList('body/tours'))+1}">와이너리<br />투어</td>
		<td colspan="3" class="head_ct" >와이너리명(국가, 지역)</td>
		<td colspan="2" class="head_ct" >투어기간</td>
		<td colspan="4" class="head_ct" >사유</td>
	</tr>
	<c:forEach items="${formBodyXML.getChildList('body/tours')}" var="xmlMap" varStatus="status"	><tr>
		<td colspan="3" class="body_ct"><u:out value="${xmlMap.getAttr('tourAreaNm')}"/></td>
		<td colspan="2" class="body_ct"><u:out value="${xmlMap.getAttr('tourStrt')}" type="date"/> ~ <u:out value="${xmlMap.getAttr('tourEnd')}" type="date"/></td>
		<td colspan="4" class="body_lt"><u:out value="${xmlMap.getAttr('tourReason')}"/></td>
	</tr>	
	</c:forEach>
	</c:if>
	<tr>
		<td class="head_ct" rowspan="${fn:length(formBodyXML.getChildList('body/licenses'))+1}">면허 및 자격</td>
		<td colspan="2" class="head_ct" >면허 및 자격내용</td>
		<td colspan="2" class="head_ct" >취득년월일</td>
		<td colspan="2" class="head_ct" >등록번호</td>
		<td colspan="2" class="head_ct" >등록연월일</td>
		<td class="head_ct" >발급처</td>
	</tr>
	<c:forEach items="${formBodyXML.getChildList('body/licenses')}" var="xmlMap" varStatus="status"
		><tr>
		<td colspan="2" class="body_lt"><u:out value="${xmlMap.getAttr('licenseCont')}"/></td>
		<td colspan="2" class="body_ct"><u:out value="${xmlMap.getAttr('licenseGetDt')}" type="date"/></td>
		<td colspan="2" class="body_ct"><u:out value="${xmlMap.getAttr('licenseRegNo')}"/></td>
		<td colspan="2" class="body_ct"><u:out value="${xmlMap.getAttr('licenseRegDt')}" type="date"/></td>
		<td class="body_ct"><u:out value="${xmlMap.getAttr('licenseIsuOrg')}"/></td>
	</tr>
	</c:forEach>
	<tr>
		<td class="head_ct" rowspan="${fn:length(formBodyXML.getChildList('body/prizes'))+1}">수상경력</td>
		<td colspan="2" class="head_ct" >수상 구분</td>
		<td colspan="2" class="head_ct" >연월일</td>
		<td colspan="2" class="head_ct" >내용</td>
		<td colspan="2" class="head_ct" >수상수여 기관/자</td>
		<td class="head_ct" >비고</td>
	</tr>
	<c:forEach items="${formBodyXML.getChildList('body/prizes')}" var="xmlMap" varStatus="status"
		><tr>
		<td colspan="2" class="body_lt"><u:out value="${xmlMap.getAttr('prizeTyp')}"/></td>
		<td colspan="2" class="body_ct"><u:out value="${xmlMap.getAttr('prizeDt')}"/></td>
		<td colspan="2" class="body_ct"><u:out value="${xmlMap.getAttr('prizeCont')}"/></td>
		<td colspan="2" class="body_ct"><u:out value="${xmlMap.getAttr('prizeOrgNm')}"/></td>
		<td class="body_ct"><u:out value="${xmlMap.getAttr('prizeEtc')}"/></td>
	</tr>
	</c:forEach>
	<tr>
		<td class="head_ct" rowspan="${fn:length(formBodyXML.getChildList('body/insas'))+1}">인사발령</td>
		<td class="head_ct" >종류</td>
		<td class="head_ct" >직위</td>
		<td class="head_ct" >직책</td>
		<td colspan="2" class="head_ct" >법인명/부서명/팀명</td>
		<td class="head_ct" >인사발령일</td>
		<td colspan="3" class="head_ct" >비고</td>
	</tr>
	<c:forEach items="${formBodyXML.getChildList('body/insas')}" var="xmlMap" varStatus="status"
		><tr>
		<td class="body_ct"><u:out value="${xmlMap.getAttr('insaKnd')}"/></td>
		<td class="body_ct"><u:out value="${xmlMap.getAttr('insaPositNm')}"/></td>
		<td class="body_ct"><u:out value="${xmlMap.getAttr('insaTitleNm')}"/></td>
		<td colspan="2" class="body_ct"><u:out value="${xmlMap.getAttr('insaCorpNm')}"/></td>
		<td class="body_ct"><u:out value="${xmlMap.getAttr('insaIssueDt')}"/></td>
		<td colspan="3" class="body_ct"><u:out value="${xmlMap.getAttr('insaEtc')}"/></td>
	</tr>
	</c:forEach>
</u:listArea>

<!-- <hr style="border: none;border-top: 2px dotted blue;color: #fff;background-color: #fff;height: 1px;width: 100%;display:block;"/> -->

<u:listArea id="detlArea" colgroup="8%,13%,13%,9%,13%,9%,9%,8%,8%,10%">
	<tr>
		<td class="head_ct">성별</td>
		<td colspan="2" class="body_ct">
			<u:checkArea>
				<u:radio name="gendar" value="M" title="남" checkValue="${formBodyXML.getAttr('body/unusual.gendar')}" alt="남" disabled="Y" />
				<u:radio name="gendar" value="F" title="여" checkValue="${formBodyXML.getAttr('body/unusual.gendar')}" alt="여" disabled="Y" />
			</u:checkArea>
		</td>
		<td class="head_ct">결혼기념일</td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/unusual.weddAnnvDt')}" type="date"/></td>
		<td class="head_ct">구분</td>
		<td colspan="4" class="body_ct">
			<u:checkArea>
				<u:radio name="positTypCd" value="R" title="정규직" checkValue="${formBodyXML.getAttr('body/unusual.positTypCd')}" alt="정규직" disabled="Y"/>
				<u:radio name="positTypCd" value="C" title="계약직" checkValue="${formBodyXML.getAttr('body/unusual.positTypCd')}" alt="계약직" disabled="Y"/>
				<u:radio name="positTypCd" value="T" title="임시직" checkValue="${formBodyXML.getAttr('body/unusual.positTypCd')}" alt="임시직" disabled="Y"/>
			</u:checkArea>
		</td>
	</tr>
	<tr>
		<td class="head_ct">결혼유무</td>
		<td colspan="2" class="body_ct">
			<u:checkArea>
				<u:radio name="weddYn" value="Y" title="기혼" checkValue="${formBodyXML.getAttr('body/unusual.weddYn')}" alt="기혼"  disabled="Y"/>
				<u:radio name="weddYn" value="N" title="미혼" checkValue="${formBodyXML.getAttr('body/unusual.weddYn')}" alt="미혼" disabled="Y"/>
			</u:checkArea>
		</td>
		<td class="head_ct">종교</td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/unusual.religion')}"/></td>
		<td class="head_ct">취미</td>
		<td colspan="2" class="body_ct"><u:out value="${formBodyXML.getAttr('body/unusual.hobby')}"/></td>
		<td class="head_ct">특기</td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/unusual.specialty')}"/></td>
	</tr>
	<tr>
		<td class="head_ct" rowspan="${fn:length(formBodyXML.getChildList('body/forinLangs'))+1}">외국어</td>
		<td colspan="2" class="head_ct">외국어명</td>
		<td class="head_ct">Speaking</td>
		<td class="head_ct">Writing</td>
		<td class="head_ct">Reading</td>
		<td colspan="2" class="head_ct">공인점수</td>
		<td colspan="2" class="head_ct">취득연월일</td>
	</tr>	
	<c:forEach items="${formBodyXML.getChildList('body/forinLangs')}" var="xmlMap" varStatus="status"
		><tr>
			<td colspan="2" class="body_ct"><u:out value="${xmlMap.getAttr('foreignLangNm')}"/></td>
			<td class="body_ct"><u:out value="${xmlMap.getAttr('speaking')}"/></td>
			<td class="body_ct"><u:out value="${xmlMap.getAttr('writing')}"/></td>
			<td class="body_ct"><u:out value="${xmlMap.getAttr('reading')}"/></td>
			<td colspan="2" class="body_ct"><u:out value="${xmlMap.getAttr('officialScore')}"/></td>
			<td colspan="2" class="body_ct"><u:out value="${xmlMap.getAttr('foreignLangDt')}"/></td>
		</tr>
	</c:forEach>
	<tr>
		<td class="head_ct" rowspan="${fn:length(formBodyXML.getChildList('body/achvmts'))+1}">학력<br />(해외연수 포함)</td>
		<td class="head_ct">연 / 월</td>
		<td class="head_ct">연 / 월</td>
		<td colspan="3" class="head_ct">학교명</td>
		<td colspan="2" class="head_ct">전공</td>
		<td class="head_ct">졸업여부</td>
		<td class="head_ct">비고</td>
	</tr>
	<c:forEach items="${formBodyXML.getChildList('body/achvmts')}" var="xmlMap" varStatus="status"
		><tr>
			<td class="body_ct"><u:out value="${xmlMap.getAttr('achvmtStrtYear') }"/> / <u:out value="${xmlMap.getAttr('achvmtStrtMonth') }"/></td>
			<td class="body_ct"><u:out value="${xmlMap.getAttr('achvmtEndYear') }"/> / <u:out value="${xmlMap.getAttr('achvmtEndMonth') }"/>	</td>
			<td colspan="3" class="body_ct"><u:out value="${xmlMap.getAttr('schoolNm')}"/></td>
			<td colspan="2" class="body_ct"><u:out value="${xmlMap.getAttr('majorNm')}"/></td>
			<td class="body_ct"><u:out value="${xmlMap.getAttr('graduation')}"/></td>
			<td class="body_lt"><u:out value="${xmlMap.getAttr('achvmtEtc')}"/></td>
		</tr>
	</c:forEach>
	<tr>
		<td class="head_ct" rowspan="2">병역</td>
		<td class="head_ct">군필여부</td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/armyServe.joinArmyYn')}"/></td>
		<td class="head_ct">군별</td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/armyServe.joinArmyTyp')}"/></td>
		<td class="head_ct">계급</td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/armyServe.joinArmyClass')}"/></td>
		<td class="head_ct">복무기간</td>
		<td colspan="2" >
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
				<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/armyServe.strtDt')}" type="date" /></td>
				<td class="search_body_ct">~</td>
				<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/armyServe.endDt')}" type="date" /></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="head_ct">병과</td>
		<td class="body_ct"><u:out value="${formBodyXML.getAttr('body/armyServe.armOfService')}"/></td>
		<td class="head_ct">미필사유</td>
		<td colspan="6" class="body_lt"><u:out value="${formBodyXML.getAttr('body/armyServe.notArmCont')}"/></td>
	</tr>
	<tr>
		<td class="head_ct" rowspan="${fn:length(formBodyXML.getChildList('body/careers'))+1}">경력 및 <br />중요업무 <br />실적</td>
		<td class="head_ct">연 / 월</td>
		<td class="head_ct">연 / 월</td>
		<td class="head_ct">근무년/개월수</td>
		<td colspan="2" class="head_ct">회사명</td>
		<td class="head_ct">부서</td>
		<td class="head_ct">직위</td>
		<td colspan="2" class="head_ct">담당업무</td>
	</tr>
	<c:forEach items="${formBodyXML.getChildList('body/careers')}" var="xmlMap" varStatus="status"
		><c:if test="${status.index==0 || status.index%2==0}">
		<tr>
		<td class="body_ct"><u:out value="${xmlMap.getAttr('careerStrtYear')}"/> / <u:out value="${xmlMap.getAttr('careerStrtMonth')}"/></td>
		<td class="body_ct"><u:out value="${xmlMap.getAttr('careerEndYear')}"/> / <u:out value="${xmlMap.getAttr('careerEndMonth')}"/></td>
		<td class="body_ct"><u:out value="${xmlMap.getAttr('careerTotalYear')}"/> / <u:out value="${xmlMap.getAttr('careerTotalMonth')}"/></td>
		<td colspan="2" class="body_ct"><u:out value="${xmlMap.getAttr('careerCompNm')}"/></td>
		<td class="body_ct"><u:out value="${xmlMap.getAttr('careerDeptNm')}"/></td>
		<td class="body_ct"><u:out value="${xmlMap.getAttr('careerPositNm')}"/></td>
		<td colspan="2" class="body_ct"><u:out value="${xmlMap.getAttr('careerPichNm')}"/></td>
	</tr></c:if><c:if test="${status.index==1 || status.index%2>0 || isLast}">	<tr>
		<td colspan="2" class="head_ct">업무</td>
		<td colspan="7" class="body_lt"><u:out value="${xmlMap.getAttr('careerDutyNm')}"/></td>
	</tr></c:if>
	</c:forEach>
	
	<tr>
		<td class="head_ct" rowspan="${fn:length(formBodyXML.getChildList('body/rewards'))+1}">상벌</td>
		<td colspan="2" class="head_ct" >표창/징계 구분</td>
		<td colspan="2" class="head_ct" >날짜</td>
		<td colspan="3" class="head_ct" >내용</td>
		<td colspan="2" class="head_ct" >상벌권자</td>
	</tr>
	<c:forEach items="${formBodyXML.getChildList('body/rewards')}" var="xmlMap" varStatus="status"
		><tr>
		<td colspan="2" class="body_ct"><u:out value="${xmlMap.getAttr('rewardGubun')}"/></td>
		<td colspan="2" class="body_ct"><u:out value="${xmlMap.getAttr('rewardDt')}"/></td>
		<td colspan="3" class="body_ct"><u:out value="${xmlMap.getAttr('rewardCont')}"/></td>
		<td colspan="2" class="body_ct"><u:out value="${xmlMap.getAttr('rewardUser')}"/></td>
	</tr>
	</c:forEach>
</u:listArea>	
</div>
<% // 하단 버튼 %>
<u:buttonArea topBlank="true" >
<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" auth="R" />
<c:if test="${isAdmin==true || (!empty isModify && isModify == true)}">
<u:button titleId="cm.btn.mod" onclick="setPsnCard();" alt="수정" auth="W"/>
<u:button titleId="cm.btn.del" alt="삭제" onclick="delPsnCard('${cuPsnCardBVo.cardNo }');" auth="W"/>
</c:if>
<c:if test="${isMgm==true }"><u:button titleId="cm.btn.list" alt="목록" onclick="goList();" /></c:if>
</u:buttonArea>
</c:if>	
