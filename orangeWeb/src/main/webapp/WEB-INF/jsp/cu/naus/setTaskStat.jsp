<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.Date,java.util.List,java.util.ArrayList" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
//코드1 초기 row수
request.setAttribute("cd1InitCnt", "1");

//코드2 초기 row수
request.setAttribute("cd2InitCnt", "1");

///코드3 초기 row수
request.setAttribute("cd3InitCnt", "1");

//코드4 초기 row수
request.setAttribute("cd4InitCnt", "1");

//코드5 초기 row수
request.setAttribute("cd5InitCnt", "1");

//코드6 초기 row수
request.setAttribute("cd6InitCnt", "1");

%>
<!-- 코드 목록 포함 -->
<jsp:include page="/WEB-INF/jsp/cu/naus/inclCdList.jsp" />
<script type="text/javascript">
<!--
<% // [하단버튼:삭제] %>
function delTaskStat(statNo) {
	if (statNo != null && confirmMsg("cm.cfrm.del")) {<% // 삭제하시겠습니까? %>
		callAjax('./transTaskStatDelAjx.do?menuId=${menuId}', {statNo:statNo}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.replace('./listTaskStat.do?${paramsForList }');
			}
		});
	}
};
<% // [하단버튼:수정] 수정화면으로 이동 %>
function setTaskStat(){
	location.href = './setTaskStat.do?${paramsForList}&statNo=${param.statNo}';
}<% // [하단버튼:목록] 목록으로 이동 %>
function goList() {
	location.href = './listTaskStat.do?${paramsForList}';
}

<%// 선택삭제%>
function delSelRow(conId){
	var arr = getCheckedTrs(conId, "cm.msg.noSelect");
	if(arr!=null) {
		conId=conId.replace('/','\\/');
		var len=$("#"+conId+" tbody:first tr").not('#headerTr, #hiddenTr').length;
		if(len-arr.length==0){
			alert('최소 1줄 이상 입력해야 합니다.');
			return;
		}
		delRowInArr(arr);
	}
}
<%// 행삭제%>
function delRow(conId){
	conId=conId.replace('/','\\/');
	var len=$("#"+conId+" tbody:first tr").not('#headerTr, #hiddenTr').length;
	if(len<2){
		alert('최소 1줄 이상 입력해야 합니다.');
		return;
	}
	var $tr = $("#"+conId+" tbody:first #hiddenTr").prev();
	delRowInArr([$tr[0]]);
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
		if(id!='headerTr' && id!='hiddenTr')
			arr.push(obj);
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
	var mapId=conId.substring(conId.indexOf('-')+1,conId.indexOf('/'));
	conId=conId.replace('/','\\/');
	var $tr = $("#"+conId+" tbody:first #hiddenTr");
	if(browser.ie && browser.ver<8)
		removeJqueryId($tr);
	var html = $tr[0].outerHTML;
	$maxRow=maxRowMap.get(mapId);
	if($maxRow==null) $maxRow=0;
	html = html.replace(/_NO/g,'_'+(parseInt($maxRow)+parseInt(strtCnt)));
	$maxRow++;
	maxRowMap.put(mapId, $maxRow);
	$tr.before(html);
	$tr = $tr.prev();
	$tr.attr('id',trId);
	$tr.attr('style','');
	setJsUniform($tr[0]);
}

<%// jquery id 속성 제거[ie8--] %>
function removeJqueryId(obj){
	obj.find('input[type="text"]', 'textarea').each(function(){
		var attrs = this.attributes;
		for (attr in attrs) {
			if(attrs[attr]==null || attrs[attr].name==null) continue;
			if(attrs[attr].name.toLowerCase().startsWith('jquery'))
				$(this).removeAttr(attrs[attr].name);
		}
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
}
<%
//xml 히든값 세팅 및, html 변환 세팅 %>
function setXMLByValue(form, formBodyXML, applyHtmlArea){
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
	
	var array = $('#dftArea :input').serializeArray();
    $.each( array, function(index, field ) {
    	form.find("input[name='"+field.name+"']").remove();
    	form.appendHidden({'name':field.name,'value':field.value});
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
			if (isInUtf8Length($('#cont').val(), 0, '${bodySize}') > 0) {
				alertMsg('cm.input.check.maxbyte', ['<u:msg titleId="cols.cont" />','${bodySize}']);<% // cm.input.check.maxbyte="{0}"의 최대 바이트수 ({1} bytes)를 초과 하였습니다. %>
				return;
			}
			
			var $form = $('#setForm');
			//loading('xml-body', true);
			setXMLByValue($form, formBodyXML, true);
			//return;
			$form.attr('method','post');
			$form.attr('action','./transTaskStat.do?menuId=${menuId}');
			$form.attr('enctype','multipart/form-data');
			$form.attr('target','dataframe');
			editor('cont').prepare();
			saveFileToForm('${filesId}', $form[0], null);
			//loading('xml-body', false);
		}
	}
}

<%//xml 수집 전에 호출함 %>
function checkFormBodyXML(){
	return validator.validate('dftArea');
}
<%//초기화 %>
function reset(){
	location.replace(location.href);
}
</c:if>
$(document).ready(function() {
	<c:if test="${formBodyMode eq 'view'}">setUniformCSS();</c:if>	
	<c:if test="${formBodyMode ne 'view'}">
	setUniformCSS($('#dftArea'));
	<%// 행추가 영역 제외하고 uniform 적용%>
	$("#xml-body tbody").children("[id!='hiddenTr']").each(function(){
		setJsUniform(this);
	});
	
	<%// 다음 Row 번호 %>
	maxRowMap=new ParamMap();
	// 초기 Row 세팅 
	maxRowMap.put('data1', "${fn:length(formBodyXML.getChildList('body/data1'))}");
	maxRowMap.put('data2', "${fn:length(formBodyXML.getChildList('body/data2'))}");
	maxRowMap.put('data3', "${fn:length(formBodyXML.getChildList('body/data3'))}");
	maxRowMap.put('data4', "${fn:length(formBodyXML.getChildList('body/data4'))}");
	maxRowMap.put('data5', "${fn:length(formBodyXML.getChildList('body/data5'))}");
	maxRowMap.put('data6', "${fn:length(formBodyXML.getChildList('body/data6'))}");
	
	</c:if>
});

//-->
</script>
<u:title menuNameFirst="true"/>
<% // 탭 %>
<c:if
	test="${formBodyMode ne 'view'}">
<% // 하단 버튼 %>
<u:buttonArea>
<u:button titleId="cm.btn.reset" alt="초기화" href="javascript:reset();" auth="W" />
<u:button titleId="cm.btn.save" onclick="save();" alt="저장" auth="W" />
<u:button titleId="cm.btn.cancel" alt="취소" href="javascript:history.go(-1);" />
</u:buttonArea>	
<div id="xmlArea">
<div id="dftArea">
<div class="front">
	<div class="front_left">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr><td class="color_stxt"><input type="checkbox" id="privYn" name="privYn" value="Y" <c:if test="${cuTaskStatBVo.privYn eq 'Y' }">checked="checked"</c:if>/><label for="privYn"><u:msg titleId="bb.msg.save.priv" alt="비공개로 등록합니다." /></label></td></tr>
		</table>
	</div>
</div>
<u:listArea colgroup="12%," >
	<tr>
	<td class="head_lt"><u:mandatory />날짜</td>
	<td><u:calendartime id="reprtDt" value="${empty cuTaskStatBVo.reprtDt ? 'today' : cuTaskStatBVo.reprtDt}" title="일자" type="calendar" mandatory="Y"/></td>
	</tr><tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.subj" alt="제목" /></td>
	<td><u:input id="subj" titleId="cols.subj" style="width:98%;" value="${cuTaskStatBVo.subj }" mandatory="Y" maxByte="240" /></td>
	</tr>
</u:listArea></div>

<div id="xml-body">
<u:set var="cd1StrtCnt" test="${empty formBodyXML.getChildList('body/data1') }" value="${cd1InitCnt }" elseValue="0"/><!-- 추가 Row수 -->
<u:title title="생산기계설비점검" type="small" alt="생산기계설비점검">
<u:titleButton title="추가" onclick="addRow('xml-data1/row', 'row', '${cd1StrtCnt }');" alt="추가"/><u:buttonS title="행삭제" onclick="delRow('xml-data1/row');" alt="행삭제"/><u:titleButton title="선택삭제" onclick="delSelRow('xml-data1/row');" alt="선택삭제"/>
</u:title>
<u:listArea id="xml-data1/row" colgroup="3%,10%,22%,21%,21%,23%">
	<tr id="headerTr">
		<td class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('xml-data1\\/row', this.checked);" value=""/></td>
		<td class="head_ct" >생산팀</td>
		<td class="head_ct" >실시사항</td>
		<td class="head_ct" >업체명</td>
		<td class="head_ct" >예정사항</td>
		<td class="head_ct" >업체명</td>
	</tr><c:forEach
			items="${formBodyXML.getChildList('body/data1', 1, 1)}" var="row" varStatus="status" 
			><u:set test="${status.last}" var="trDisp" value="display:none"
		/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="row"
		/><u:set test="${status.last}" var="statusIndex" value="_NO" elseValue="_${status.index+1}"
		/>
	<tr id="${trId}" style="${trDisp}">
	<td class="bodybg_ct"><input type="checkbox" value="" /></td>
	<td class="body_ct"><select id="cd1_value1${statusIndex}" name="value1" ><u:option value="" title="선택" />
			<c:forEach items="${cd1List}" var="list" varStatus="listStatus">
				<u:option value="${list[0]}" title="${list[1]}" checkValue="${row.getAttr('value1')}" />
			</c:forEach>
		</select></td>
	<td class="body_ct"><u:input id="cd1_value2${statusIndex}" name="value2" value="${row.getAttr('value2')}" title="실시사항" style="width:90%;" maxByte="100" /></td>
	<td class="body_ct"><u:input id="cd1_value3${statusIndex}" name="value3" value="${row.getAttr('value3')}" title="업체명" style="width:90%;" maxByte="200" /></td>
	<td class="body_ct"><u:input id="cd1_value4${statusIndex}" name="value4" value="${row.getAttr('value4')}" title="예정사항" style="width:90%;" maxByte="100" /></td>
	<td class="body_ct"><u:input id="cd1_value5${statusIndex}" name="value5" value="${row.getAttr('value5')}" title="업체명" style="width:90%;" maxByte="100" /></td>
	</tr>
	</c:forEach>
</u:listArea>

<u:set var="cd2StrtCnt" test="${empty formBodyXML.getChildList('body/data2') }" value="${cd2InitCnt }" elseValue="0"/><!-- 추가 Row수 -->
<u:title title="일반설비(전기/기계)" type="small" alt="일반설비(전기/기계)">
<u:titleButton title="추가" onclick="addRow('xml-data2/row', 'row', '${cd2StrtCnt }');" alt="추가"/><u:buttonS title="행삭제" onclick="delRow('xml-data2/row');" alt="행삭제"/><u:titleButton title="선택삭제" onclick="delSelRow('xml-data2/row');" alt="선택삭제"/>
</u:title>
<u:listArea id="xml-data2/row" colgroup="3%,10%,22%,21%,21%,23%">
	<tr id="headerTr">
		<td class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('xml-data2\\/row', this.checked);" value=""/></td>
		<td class="head_ct" >장소</td>
		<td class="head_ct" >실시사항</td>
		<td class="head_ct" >업체명</td>
		<td class="head_ct" >예정사항</td>
		<td class="head_ct" >업체명</td>
	</tr><c:forEach
			items="${formBodyXML.getChildList('body/data2', 1, 1)}" var="row" varStatus="status" 
			><u:set test="${status.last}" var="trDisp" value="display:none"
		/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="row"
		/><u:set test="${status.last}" var="statusIndex" value="_NO" elseValue="_${status.index+1}"
		/>
	<tr id="${trId}" style="${trDisp}">
	<td class="bodybg_ct"><input type="checkbox" value="" /></td>
	<td class="body_ct"><select id="cd2_value1${statusIndex}" name="value1" ><u:option value="" title="선택" />
			<c:forEach items="${cd2List}" var="list" varStatus="listStatus">
				<u:option value="${list[0]}" title="${list[1]}" checkValue="${row.getAttr('value1')}" />
			</c:forEach>
		</select></td>
	<td class="body_ct"><u:input id="cd2_value2${statusIndex}" name="value2" value="${row.getAttr('value2')}" title="실시사항" style="width:90%;" maxByte="100" /></td>
	<td class="body_ct"><u:input id="cd2_value3${statusIndex}" name="value3" value="${row.getAttr('value3')}" title="업체명" style="width:90%;" maxByte="200" /></td>
	<td class="body_ct"><u:input id="cd2_value4${statusIndex}" name="value4" value="${row.getAttr('value4')}" title="예정사항" style="width:90%;" maxByte="100" /></td>
	<td class="body_ct"><u:input id="cd2_value5${statusIndex}" name="value5" value="${row.getAttr('value5')}" title="업체명" style="width:90%;" maxByte="100" /></td>
	</tr>
	</c:forEach>
</u:listArea>

<div style="width:100%;display:inline-block;">
<!-- 좌측 테이블 -->
<div style="width:48%;float:left;">
<u:set var="cd3StrtCnt" test="${empty formBodyXML.getChildList('body/data3') }" value="${cd3InitCnt }" elseValue="0"/><!-- 추가 Row수 -->
<u:title title="1.옵셋인쇄사무실(5일)" type="small" alt="1.옵셋인쇄사무실(5일)">
<u:titleButton title="추가" onclick="addRow('xml-data3/row', 'row', '${cd3StrtCnt }');" alt="추가"/><u:buttonS title="행삭제" onclick="delRow('xml-data3/row');" alt="행삭제"/><u:titleButton title="선택삭제" onclick="delSelRow('xml-data3/row');" alt="선택삭제"/>
</u:title>
<u:listArea id="xml-data3/row" colgroup="3%,10%,32%,">
	<tr id="headerTr">
		<td class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('xml-data3\\/row', this.checked);" value=""/></td>
		<td class="head_ct" >드롭박스</td>
		<td class="head_ct" >계량기</td>
		<td class="head_ct" >기타</td>
	</tr><c:forEach
			items="${formBodyXML.getChildList('body/data3', 1, 1)}" var="row" varStatus="status" 
			><u:set test="${status.last}" var="trDisp" value="display:none"
		/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="row"
		/><u:set test="${status.last}" var="statusIndex" value="_NO" elseValue="_${status.index+1}"
		/>
	<tr id="${trId}" style="${trDisp}">
	<td class="bodybg_ct"><input type="checkbox" value="" /></td>
	<td class="body_ct"><select id="cd3_value1${statusIndex}" name="value1" ><u:option value="" title="선택" />
			<c:forEach items="${cd3List}" var="list" varStatus="listStatus">
				<u:option value="${list[0]}" title="${list[1]}" checkValue="${row.getAttr('value1')}" />
			</c:forEach>
		</select></td>
	<td class="body_ct"><u:input id="cd3_value2${statusIndex}" name="value2" value="${row.getAttr('value2')}" title="실시사항" style="width:90%;" maxByte="100" /></td>
	<td class="body_ct"><u:input id="cd3_value3${statusIndex}" name="value3" value="${row.getAttr('value3')}" title="업체명" style="width:90%;" maxByte="200" /></td>
	</tr>
	</c:forEach>
</u:listArea>

</div>
<div style="width:2%;float:left;"></div><!-- 중앙 -->
<!-- 우측 테이블 -->
<div style="width:48%;float:right;">
<u:set var="cd4StrtCnt" test="${empty formBodyXML.getChildList('body/data4') }" value="${cd4InitCnt }" elseValue="0"/><!-- 추가 Row수 -->
<u:title title="2.옵셋인쇄기계(5일)" type="small" alt="2.옵셋인쇄기계(5일)">
<u:titleButton title="추가" onclick="addRow('xml-data4/row', 'row', '${cd4StrtCnt }');" alt="추가"/><u:buttonS title="행삭제" onclick="delRow('xml-data4/row');" alt="행삭제"/><u:titleButton title="선택삭제" onclick="delSelRow('xml-data4/row');" alt="선택삭제"/>
</u:title>
<u:listArea id="xml-data4/row" colgroup="3%,10%,32%,">
	<tr id="headerTr">
		<td class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('xml-data4\\/row', this.checked);" value=""/></td>
		<td class="head_ct" >드롭박스</td>
		<td class="head_ct" >계량기</td>
		<td class="head_ct" >텍스트</td>
	</tr><c:forEach
			items="${formBodyXML.getChildList('body/data4', 1, 1)}" var="row" varStatus="status" 
			><u:set test="${status.last}" var="trDisp" value="display:none"
		/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="row"
		/><u:set test="${status.last}" var="statusIndex" value="_NO" elseValue="_${status.index+1}"
		/>
	<tr id="${trId}" style="${trDisp}">
	<td class="bodybg_ct"><input type="checkbox" value="" /></td>
	<td class="body_ct"><select id="cd4_value1${statusIndex}" name="value1" ><u:option value="" title="선택" />
			<c:forEach items="${cd4List}" var="list" varStatus="listStatus">
				<u:option value="${list[0]}" title="${list[1]}" checkValue="${row.getAttr('value1')}" />
			</c:forEach>
		</select></td>
	<td class="body_ct"><u:input id="cd4_value2${statusIndex}" name="value2" value="${row.getAttr('value2')}" title="실시사항" style="width:90%;" maxByte="100" /></td>
	<td class="body_ct"><u:input id="cd4_value3${statusIndex}" name="value3" value="${row.getAttr('value3')}" title="업체명" style="width:90%;" maxByte="200" /></td>
	</tr>
	</c:forEach>
</u:listArea>
</div>
</div>

<div style="width:100%;display:inline-block;">
<!-- 좌측 테이블 -->
<div style="width:48%;float:left;">
<u:set var="cd5StrtCnt" test="${empty formBodyXML.getChildList('body/data5') }" value="${cd5InitCnt }" elseValue="0"/><!-- 추가 Row수 -->
<u:title title="3.사무동식당(25일)" type="small" alt="3.사무동식당(25일">
<u:titleButton title="추가" onclick="addRow('xml-data5/row', 'row', '${cd5StrtCnt }');" alt="추가"/><u:buttonS title="행삭제" onclick="delRow('xml-data5/row');" alt="행삭제"/><u:titleButton title="선택삭제" onclick="delSelRow('xml-data5/row');" alt="선택삭제"/>
</u:title>
<u:listArea id="xml-data5/row" colgroup="3%,10%,32%,">
	<tr id="headerTr">
		<td class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('xml-data5\\/row', this.checked);" value=""/></td>
		<td class="head_ct" >드롭박스</td>
		<td class="head_ct" >계량기</td>
		<td class="head_ct" >기타</td>
	</tr><c:forEach
			items="${formBodyXML.getChildList('body/data5', 1, 1)}" var="row" varStatus="status" 
			><u:set test="${status.last}" var="trDisp" value="display:none"
		/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="row"
		/><u:set test="${status.last}" var="statusIndex" value="_NO" elseValue="_${status.index+1}"
		/>
	<tr id="${trId}" style="${trDisp}">
	<td class="bodybg_ct"><input type="checkbox" value="" /></td>
	<td class="body_ct"><select id="cd5_value1${statusIndex}" name="value1" ><u:option value="" title="선택" />
			<c:forEach items="${cd5List}" var="list" varStatus="listStatus">
				<u:option value="${list[0]}" title="${list[1]}" checkValue="${row.getAttr('value1')}" />
			</c:forEach>
		</select></td>
	<td class="body_ct"><u:input id="cd5_value2${statusIndex}" name="value2" value="${row.getAttr('value2')}" title="실시사항" style="width:90%;" maxByte="100" /></td>
	<td class="body_ct"><u:input id="cd5_value3${statusIndex}" name="value3" value="${row.getAttr('value3')}" title="업체명" style="width:90%;" maxByte="200" /></td>
	</tr>
	</c:forEach>
</u:listArea>

</div>
<div style="width:2%;float:left;"></div><!-- 중앙 -->
<!-- 우측 테이블 -->
<div style="width:48%;float:right;">
<u:set var="cd6StrtCnt" test="${empty formBodyXML.getChildList('body/data6') }" value="${cd6InitCnt }" elseValue="0"/><!-- 추가 Row수 -->
<u:title title="테이블 이름" type="small" alt="테이블 이름">
<u:titleButton title="추가" onclick="addRow('xml-data6/row', 'row', '${cd6StrtCnt }');" alt="추가"/><u:buttonS title="행삭제" onclick="delRow('xml-data6/row');" alt="행삭제"/><u:titleButton title="선택삭제" onclick="delSelRow('xml-data6/row');" alt="선택삭제"/>
</u:title>
<u:listArea id="xml-data6/row" colgroup="3%,10%,32%,">
	<tr id="headerTr">
		<td class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('xml-data6\\/row', this.checked);" value=""/></td>
		<td class="head_ct" >드롭박스</td>
		<td class="head_ct" >내역</td>
		<td class="head_ct" >텍스트</td>
	</tr><c:forEach
			items="${formBodyXML.getChildList('body/data6', 1, 1)}" var="row" varStatus="status" 
			><u:set test="${status.last}" var="trDisp" value="display:none"
		/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="row"
		/><u:set test="${status.last}" var="statusIndex" value="_NO" elseValue="_${status.index+1}"
		/>
	<tr id="${trId}" style="${trDisp}">
	<td class="bodybg_ct"><input type="checkbox" value="" /></td>
	<td class="body_ct"><select id="cd6_value1${statusIndex}" name="value1" ><u:option value="" title="선택" />
			<c:forEach items="${cd6List}" var="list" varStatus="listStatus">
				<u:option value="${list[0]}" title="${list[1]}" checkValue="${row.getAttr('value1')}" />
			</c:forEach>
		</select></td>
	<td class="body_ct"><u:input id="cd6_value2${statusIndex}" name="value2" value="${row.getAttr('value2')}" title="실시사항" style="width:90%;" maxByte="100" /></td>
	<td class="body_ct"><u:input id="cd6_value3${statusIndex}" name="value3" value="${row.getAttr('value3')}" title="업체명" style="width:90%;" maxByte="200" /></td>
	</tr>
	</c:forEach>
</u:listArea>


</div>
</div>

<u:title title="온도, 조도" type="small" alt="온도, 조도" />
<u:listArea id="xml-data7/row" colgroup="10%,18%,18%,18%,18%">
	<c:forEach
			items="${formBodyXML.getChildList('body/data7', 9)}" var="row" varStatus="status" >
	<c:if test="${status.index==0 }">
		<tr id="row">
		<td class="head_ct" ></td>
		<td class="head_ct" >온도</td>
		<td class="head_ct" >조도</td>
		<td class="head_ct" ><u:input id="cd7_col3${statusIndex}" name="value3" value="${row.getAttr('value3')}" title="컬럼3" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct" ><u:input id="cd7_col4${statusIndex}" name="value4" value="${row.getAttr('value4')}" title="컬럼4" style="width:90%;" maxByte="100" /></td>
		<td class="head_ct" ><u:input id="cd7_col5${statusIndex}" name="value5" value="${row.getAttr('value5')}" title="컬럼5" style="width:90%;" maxByte="100" /></td>
	</tr>
	</c:if>
	<c:if test="${status.index>0 }">
	<tr id="row">
	<td class="bodybg_ct"><c:if test="${status.index<7 }">${status.index }팀</c:if
	><c:if test="${status.index>=7 }"><u:input id="cd7_value1${statusIndex}" name="value1" value="${row.getAttr('value1')}" title="실시사항" style="width:85%;" maxByte="100" /></c:if></td>
	<td class="body_ct"><u:input id="cd7_value2${statusIndex}" name="value2" value="${row.getAttr('value2')}" title="온도" style="width:90%;" maxByte="100" /></td>
	<td class="body_ct"><u:input id="cd7_value3${statusIndex}" name="value3" value="${row.getAttr('value3')}" title="조도" style="width:90%;" maxByte="200" /></td>
	<td class="body_ct"><u:input id="cd7_value4${statusIndex}" name="value4" value="${row.getAttr('value4')}" title="컬럼3" style="width:90%;" maxByte="100" /></td>
	<td class="body_ct"><u:input id="cd7_value5${statusIndex}" name="value5" value="${row.getAttr('value5')}" title="컬럼4" style="width:90%;" maxByte="100" /></td>
	<td class="body_ct"><u:input id="cd7_value6${statusIndex}" name="value6" value="${row.getAttr('value6')}" title="컬럼5" style="width:90%;" maxByte="100" /></td>
	</tr>
	</c:if>
	</c:forEach>
</u:listArea>

</div></div>


<!-- xml body End -->

<u:params var="viewParams"/>
<u:blank />
<form id="setForm" name="setForm">
<u:input type="hidden" id="menuId" value="${menuId }"/>
<u:input type="hidden" id="listPage" value="./listTaskStat.do?${paramsForList }"/>
<u:input type="hidden" id="viewPage" value="./viewTaskStat.do?${viewParams }"/>
<c:if test="${!empty cuTaskStatBVo && !empty cuTaskStatBVo.statNo}"><u:input type="hidden" id="statNo" value="${cuTaskStatBVo.statNo }"/></c:if>
<div id="formBodyXMLDataArea"></div>
<div idf="etcArea" style="width:100%;display:inline-block;">
<%
	com.innobiz.orange.web.cu.vo.CuTaskStatBVo cuTaskStatBVo = (com.innobiz.orange.web.cu.vo.CuTaskStatBVo)request.getAttribute("cuTaskStatBVo");
	if(cuTaskStatBVo != null){
		if(request.getAttribute("namoEditorEnable")==null){
			String _bodyHtml = com.innobiz.orange.web.cm.utils.EscapeUtil.escapeWriteableHtml(cuTaskStatBVo.getCont());
			request.setAttribute("_bodyHtml", _bodyHtml);
		} else {
			request.setAttribute("_bodyHtml", cuTaskStatBVo.getCont());
		}
	}
%>
<u:listArea>
<tr><td class="head_ct">내용</td></tr>
<tr><td class="body_lt"><u:editor id="cont" width="100%" height="400px" module="cu" value="${_bodyHtml}" padding="2" /></td></tr>
</u:listArea>
<u:listArea>
	<tr>
	<td><u:files id="${filesId}" fileVoList="${fileVoList}" module="cu" mode="set" exts="${exts }" extsTyp="${extsTyp }"/></td>
	</tr>
</u:listArea>
</div>
</form>
<% // 하단 버튼 %>
<u:buttonArea topBlank="true">
<u:button titleId="cm.btn.reset" alt="초기화" href="javascript:reset();" auth="W" />
<u:button titleId="cm.btn.save" onclick="save();" alt="저장" auth="W" />
<u:button titleId="cm.btn.cancel" alt="취소" href="javascript:history.go(-1);" />
</u:buttonArea>
</c:if><c:if
	test="${formBodyMode eq 'view'}">
<% // 하단 버튼 %>
<u:buttonArea>
<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" auth="R" />
<u:button titleId="cm.btn.mod" onclick="setTaskStat();" alt="수정" auth="M" ownerUid="${cuTaskStatBVo.regrUid }"/>
<u:button titleId="cm.btn.del" alt="삭제" onclick="delTaskStat('${cuTaskStatBVo.statNo }');" auth="M" ownerUid="${cuTaskStatBVo.regrUid }"/>
<u:button titleId="cm.btn.list" alt="목록" onclick="goList();" />
</u:buttonArea>
	
<div id="xml-body">

<c:if test="${cuTaskStatBVo.privYn eq 'Y'}">
<div class="front">
	<div class="front_left">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr><td class="color_stxt">* <u:msg titleId="bb.msg.view.priv" alt="비공개로 등록된 문서입니다." /></td></tr>
		</table>
	</div>
</div>
</c:if>	
<u:listArea colgroup="12%," >
	<tr>
	<td class="head_lt">날짜</td>
	<td class="body_lt"><u:out value="${cuTaskStatBVo.reprtDt }" type="date"/></td>
	</tr><tr>
	<td class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td class="body_lt"><u:out value="${cuTaskStatBVo.subj }"/></td>
	</tr>
</u:listArea>
<u:title title="생산기계설비점검" type="small" alt="생산기계설비점검" />
<u:listArea id="xml-data1/row" colgroup="6%,31%,16%,31%,16%">
	<tr id="headerTr">
		<td class="head_ct" >생산팀</td>
		<td class="head_ct" >실시사항</td>
		<td class="head_ct" >업체명</td>
		<td class="head_ct" >예정사항</td>
		<td class="head_ct" >업체명</td>
	</tr><c:forEach
			items="${formBodyXML.getChildList('body/data1', 1)}" var="row" varStatus="status" >
	<tr>
	<td class="body_ct"><c:forEach items="${cd1List}" var="list" varStatus="listStatus">
				<c:if test="${list[0] eq row.getAttr('value1')}">${list[1]}</c:if>
			</c:forEach></td>
	<td class="body_lt"><u:out value="${row.getAttr('value2')}" /></td>
	<td class="body_lt"><u:out value="${row.getAttr('value3')}" /></td>
	<td class="body_lt"><u:out value="${row.getAttr('value4')}" /></td>
	<td class="body_lt"><u:out value="${row.getAttr('value5')}" /></td>
	</tr>
	</c:forEach>
</u:listArea>

<u:title title="일반설비(전기/기계)" type="small" alt="일반설비(전기/기계)" />
<u:listArea id="xml-data2/row" colgroup="6%,31%,16%,31%,16%">
	<tr id="headerTr">
		<td class="head_ct" >장소</td>
		<td class="head_ct" >실시사항</td>
		<td class="head_ct" >업체명</td>
		<td class="head_ct" >예정사항</td>
		<td class="head_ct" >업체명</td>
	</tr><c:forEach
			items="${formBodyXML.getChildList('body/data2', 1)}" var="row" varStatus="status" >
	<tr>
	<td class="body_ct"><c:forEach items="${cd2List}" var="list" varStatus="listStatus">
				<c:if test="${list[0] eq row.getAttr('value1')}">${list[1]}</c:if>
			</c:forEach></td>
	<td class="body_lt"><u:out value="${row.getAttr('value2')}" /></td>
	<td class="body_lt"><u:out value="${row.getAttr('value3')}" /></td>
	<td class="body_lt"><u:out value="${row.getAttr('value4')}" /></td>
	<td class="body_lt"><u:out value="${row.getAttr('value5')}" /></td>
	</tr>
	</c:forEach>
</u:listArea>

<div style="width:100%;display:inline-block;">
<!-- 좌측 테이블 -->
<div style="width:48%;float:left;">
<u:title title="1.옵셋인쇄사무실(5일)" type="small" alt="1.옵셋인쇄사무실(5일)" />
<u:listArea id="xml-data3/row" colgroup="23%,28%,">
	<tr id="headerTr">
		<td class="head_ct" >드롭박스</td>
		<td class="head_ct" >계량기</td>
		<td class="head_ct" >기타</td>
	</tr><c:forEach
			items="${formBodyXML.getChildList('body/data3', 1)}" var="row" varStatus="status" >
	<tr>
	<td class="body_ct"><c:forEach items="${cd3List}" var="list" varStatus="listStatus">
				<c:if test="${list[0] eq row.getAttr('value1')}">${list[1]}</c:if>
			</c:forEach></td>
	<td class="body_lt"><u:out value="${row.getAttr('value2')}" /></td>
	<td class="body_lt"><u:out value="${row.getAttr('value3')}" /></td>
	</tr>
	</c:forEach>
</u:listArea>

</div>
<div style="width:2%;float:left;"></div><!-- 중앙 -->
<!-- 우측 테이블 -->
<div style="width:48%;float:right;">
<u:title title="2.옵셋인쇄기계(5일)" type="small" alt="2.옵셋인쇄기계(5일)" />
<u:listArea id="xml-data4/row" colgroup="23%,28%,">
	<tr id="headerTr">
		<td class="head_ct" >드롭박스</td>
		<td class="head_ct" >계량기</td>
		<td class="head_ct" >텍스트</td>
	</tr><c:forEach
			items="${formBodyXML.getChildList('body/data4', 1)}" var="row" varStatus="status" >
	<tr>
	<td class="body_ct"><c:forEach items="${cd4List}" var="list" varStatus="listStatus">
				<c:if test="${list[0] eq row.getAttr('value1')}">${list[1]}</c:if>
			</c:forEach></td>
	<td class="body_lt"><u:out value="${row.getAttr('value2')}" /></td>
	<td class="body_lt"><u:out value="${row.getAttr('value3')}" /></td>
	</tr>
	</c:forEach>
</u:listArea>


</div>
</div>

<div style="width:100%;display:inline-block;">
<!-- 좌측 테이블 -->
<div style="width:48%;float:left;">
<u:title title="3.사무동식당(25일)" type="small" alt="3.사무동식당(25일" />
<u:listArea id="xml-data5/row" colgroup="23%,28%,">
	<tr id="headerTr">
		<td class="head_ct" >드롭박스</td>
		<td class="head_ct" >계량기</td>
		<td class="head_ct" >기타</td>
	</tr><c:forEach
			items="${formBodyXML.getChildList('body/data5', 1)}" var="row" varStatus="status" >
	<tr>
	<td class="body_ct"><c:forEach items="${cd5List}" var="list" varStatus="listStatus">
				<c:if test="${list[0] eq row.getAttr('value1')}">${list[1]}</c:if>
			</c:forEach></td>
	<td class="body_lt"><u:out value="${row.getAttr('value2')}" /></td>
	<td class="body_lt"><u:out value="${row.getAttr('value3')}" /></td>
	</tr>
	</c:forEach>
</u:listArea>

</div>
<div style="width:2%;float:left;"></div><!-- 중앙 -->
<!-- 우측 테이블 -->
<div style="width:48%;float:right;">
<u:title title="테이블 이름" type="small" alt="테이블 이름" />
<u:listArea id="xml-data6/row" colgroup="23%,28%,">
	<tr id="headerTr">
		<td class="head_ct" >드롭박스</td>
		<td class="head_ct" >내역</td>
		<td class="head_ct" >텍스트</td>
	</tr><c:forEach
			items="${formBodyXML.getChildList('body/data6', 1)}" var="row" varStatus="status" >
	<tr>
	<td class="body_ct"><c:forEach items="${cd6List}" var="list" varStatus="listStatus">
				<c:if test="${list[0] eq row.getAttr('value1')}">${list[1]}</c:if>
			</c:forEach></td>
	<td class="body_lt"><u:out value="${row.getAttr('value2')}" /></td>
	<td class="body_lt"><u:out value="${row.getAttr('value3')}" /></td>
	</tr>
	</c:forEach>
</u:listArea>

</div></div>

<u:title title="온도, 조도" type="small" alt="온도, 조도" />
<u:listArea id="xml-data7/row" colgroup="10%,18%,18%,18%,18%">
	<c:forEach
			items="${formBodyXML.getChildList('body/data7', 8)}" var="row" varStatus="status" >
	<c:if test="${status.index==0 }">
		<tr id="row">
		<td class="head_ct" ></td>
		<td class="head_ct" >온도</td>
		<td class="head_ct" >조도</td>
		<td class="head_ct" ><u:out value="${row.getAttr('value3')}" /></td>
		<td class="head_ct" ><u:out value="${row.getAttr('value4')}" /></td>
		<td class="head_ct" ><u:out value="${row.getAttr('value5')}" /></td>
	</tr>
	</c:if>
	<c:if test="${status.index>0 }">
	<tr id="row">
	<td class="bodybg_ct"><c:if test="${status.index<7 }">${status.index }팀</c:if
	><c:if test="${status.index>=7 }"><u:out value="${row.getAttr('value1')}" /></c:if></td>
	<td class="body_ct"><u:out value="${row.getAttr('value2')}" /></td>
	<td class="body_ct"><u:out value="${row.getAttr('value3')}" /></td>
	<td class="body_ct"><u:out value="${row.getAttr('value4')}" /></td>
	<td class="body_ct"><u:out value="${row.getAttr('value5')}" /></td>
	<td class="body_ct"><u:out value="${row.getAttr('value6')}" /></td>
	</tr>
	</c:if>
	</c:forEach>
</u:listArea>

<div idf="etcArea" style="width:100%;display:inline-block;">
<u:listArea>
<tr><td class="head_ct"><u:msg titleId="cols.cont" alt="내용"/></td></tr>
<tr><td class="body_lt editor">${cuTaskStatBVo.cont }</td></tr>
</u:listArea>

<c:if test="${!empty fileVoList }">
<u:listArea>
	<tr>
	<td><u:files id="${filesId}" fileVoList="${fileVoList}" module="cu" mode="view" urlSuffix="/task"/></td>
	</tr>
</u:listArea></c:if>
</div>
</div>
<% // 하단 버튼 %>
<u:buttonArea topBlank="true" >
<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" auth="R" />
<u:button titleId="cm.btn.mod" onclick="setTaskStat();" alt="수정" auth="M" ownerUid="${cuTaskStatBVo.regrUid }"/>
<u:button titleId="cm.btn.del" alt="삭제" onclick="delTaskStat('${cuTaskStatBVo.statNo }');" auth="M" ownerUid="${cuTaskStatBVo.regrUid }"/>
<u:button titleId="cm.btn.list" alt="목록" onclick="goList();" />
</u:buttonArea>
</c:if>	
