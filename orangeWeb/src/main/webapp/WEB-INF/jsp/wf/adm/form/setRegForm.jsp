<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.List,java.util.ArrayList, com.innobiz.orange.web.cm.utils.ArrayUtil"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><link rel="stylesheet" href="${_cxPth}/css/colorpicker/spectrum.css" type="text/css" />
<script src="/js/colorpicker/spectrum.min.js" type="text/javascript"></script>
<c:if test="${_lang eq 'ko' || _lang eq 'zh' }"><script src="/js/colorpicker/jquery.spectrum-${_lang eq 'ko' ? 'ko' : 'zh-cn'}.js" type="text/javascript"></script></c:if>
<u:params var="paramsForList" excludes="formNo,paramFormNo,paramGenId" 
/><u:params var="paramsForGen" excludes="genId,paramFormNo,paramGenId" />
<!-- 제외할 항목 -->
<u:set var="excludeItems" test="${!empty wfFormBVo.formTyp && wfFormBVo.formTyp eq 'R' }" value="file" elseValue="" 
/><u:set var="langExcludeItems" test="${_lang ne 'ko' }" value="addr" elseValue="" 
/><%
/*
tab: 탭, table:테이블, file:파일첨부, image:Image, dataLink:Data Link, 
editor:Editor, gap:공백, line:라인
*/

String excludeItem=(String)request.getAttribute("excludeItems");
String[] excludes = excludeItem!=null ? excludeItem.split(",") : null;
String langExcludeItems=(String)request.getAttribute("langExcludeItems");
String[] langExcludes = langExcludeItems!=null ? langExcludeItems.split(",") : null;

// 추후에 협의 후 개발할 컴포넌트 제외
String[] excludeItemTmps = new String[]{"currency", "dataLink"};

/** 레이아웃 */
List<String> loutList = new ArrayList<String>();
String[] louts = new String[]{"tab", "table", "label", "file", "dataLink", "editor", "gap", "line"};
for(String lout : louts){
	if(excludes!=null && ArrayUtil.isInArray(excludes, lout)) continue;
	if(excludeItemTmps!=null && ArrayUtil.isInArray(excludeItemTmps, lout)) continue;
	loutList.add(lout);
}
request.setAttribute("louts", loutList);

/*
text:Text, textarea:Text Area, button:버튼 
date:날짜, period:기간, time:시간, datetime:날짜와시간, number:숫자, 
currency:통화, user:사용자, dept:부서, file:파일첨부, select:Select Box, radio:Radio Box, 
check:Check Box, checkSingle:Check Box Single, label:Label, image:Image, dataLink:Data Link, calculate:계산, editor:Editor
*/
/** 항목 */
List<String> itemList = new ArrayList<String>();
String[] items = new String[]{
		"text", "textarea", "button",
		"date", "period", "time", "datetime", "number", 
		"currency", "addr", "user", "dept", "file", "select", "radio", "radioSingle", 
		"checkbox", "checkboxSingle", "label", "image", "dataLink", "calculate", "editor"};
for(String item : items){
	if(excludes!=null && ArrayUtil.isInArray(excludes, item)) continue;
	if(langExcludes!=null && ArrayUtil.isInArray(langExcludes, item)) continue;
	if(excludeItemTmps!=null && ArrayUtil.isInArray(excludeItemTmps, item)) continue;
	itemList.add(item);
}
request.setAttribute("items", itemList);
%>
<script type="text/javascript" src="/js/field-selection.js" charset="UTF-8"></script>
<script type="text/javascript" src="/js/form-builder.js" charset="UTF-8"></script>
<script type="text/javascript" src="/js/jquery-ui.min.js" charset="UTF-8"></script>
<style type="text/css">
div.tooltip_area{position:absolute;width:100%;text-align:center;background-color:#a9d0f5;z-index:999;line-height:24px;font-weight:bold;cursor:pointer;}
.placeholder_sort {
display:inline-block !important;
border:1px dashed #AAA;
}
</style>
<script type="text/javascript">
<!--
var gCellObj=null, gTableObj<%
// 속성 %>
function setProp(obj, event){
	var colTyp=$(obj).attr('data-colTyp');
	if(colTyp!='cell'){
		$('div#setupTabArea li[data-areaid="propArea"] a')[0].click();
	}else{
		$('div#setupTabArea li[data-areaid="componentArea"] a')[0].click();
	}
	$("#formArea #itemsArea td.cell_on").removeClass('cell_on');
	var td=obj.tagName=='TD' ? $(obj) : $(obj).closest('td');
	$(td).addClass('cell_on');
	gCellObj=td;
	
	if(colTyp!='cell'){
		$formBuilder.createAttr('attrArea', obj, colTyp);
		$('#propBtnArea').show();
	}
	notEvtBubble(event);
}<% // %>
function setLoutProp(obj, event){
	var colTyp=$(obj).attr('data-colTyp');
	$('div#setupTabArea li[data-areaid="propArea"] a')[0].click();
	
	$formBuilder.createAttr('attrArea', obj, colTyp);
	$('#propBtnArea').show();
	notEvtBubble(event);
}

<%
// 컴포넌트 추가 %>
function setComponent(colTyp){
	if($formBuilder.addMultiCompo){
		var cellCheckList = $('#itemsArea div.check input[type="checkbox"]:checked');
		if(cellCheckList.length>0){
			var cell, coKey=null;
			$.each(cellCheckList, function(idx, check){
				cell=$(check).closest('td.cell_fix');
				if(colTyp=='radioSingle' || colTyp=='checkboxSingle')
					coKey=$formBuilder.addComponent(cell, colTyp, null, coKey);
				else
					$formBuilder.addComponent(cell, colTyp);
			});
			$tables.removeCellChk();
		}else{
			$formBuilder.addComponent(gCellObj, colTyp);
		}
	}else{
		$formBuilder.addComponent(gCellObj, colTyp);
	}
	unChkTds(); // 체크된 td 초기화
	unChkComponent(); // 선택한 컨포넌트 해제
}<%
// [팝업] - 테이블 추가 %>
function addLouts(id){	
	if(id=='table') setLoutPop(null, 'lout');
	else setItemsData(id, null, null);
}
<%
//[팝업] - 셀 설정 %>
function setCellPop(obj){
	var parent=$(obj).closest('div#itemsArea');
	gTableObj=parent.find('table:first');
	var checkList=$(gTableObj).find('div.check input[type="checkbox"]:checked');
	if(checkList.length==0) return;
	dialog.open('setCellDialog','<u:msg titleId="wf.jsp.set.item.title" alt="항목 구성" />','./setCellPop.do?menuId=${menuId}');
}<%
//[팝업] - 로우 설정 %>
function setRowPop(obj){
	var parent=$(obj).closest('div#itemsArea');
	gTableObj=parent.find('table:first');
	var checkList=$(gTableObj).find('div.check input[type="checkbox"]:checked');
	if(checkList.length==0) return;
	
	if(checkList.length>1) {
		alertMsg('wf.msg.select.one'); // wf.msg.select.one=1개만 선택 가능합니다.
		return;
	}
	dialog.open('setRowDialog','<u:msg titleId="wf.jsp.set.item.title" alt="항목 구성" />','./setRowPop.do?menuId=${menuId}');
}<%
// 셀 설정 적용%>
function applyCell(data){
	data=objToJson(data, true);
	if(data==null || data.length==0) return;
	if(gTableObj!=null){
		var checkList=$(gTableObj).find('div.check input[type="checkbox"]:checked');
		if(checkList.length>0){
			var clsNm;
			$.each(checkList, function(){
				$td=$(this).closest('td.cell_fix');
				if($td.length>0){
					$td.removeClass('body_lt body_ct body_rt head_lt head_ct head_rt');
					clsNm=data['headerYn']!=undefined && data['headerYn']=='Y' ? 'head' : 'body';
					clsNm+=data['alnVa']!=undefined && data['alnVa']!='' ? '_'+data['alnVa'] : '_ct';
					$td.addClass(clsNm);
				}
			});
		}
	}
}<%
// 컴포넌트 위 아래로 이동 %>
function moveComponent(obj, direction){
	var $area = $(obj).parents(".titlearea").parent();
	var isEditor = $area.attr('id')=='bodyHtmlArea' || ($area.find("iframe[id^='Namo']").length == 1);
	if(isEditor){
		if(direction=='up'){
			var $prev = $area.prev();
			if($prev.length>0 && $prev.is(":visible") != true) $prev = $prev.prev();
			if($prev.length>0 && $prev.is(":visible") != true) $prev = $prev.prev();
			if($prev.length>0){
				$prev.insertAfter($area);
			}
		} else if(direction=='down'){
			var $next = $area.next();
			if($next.length>0 && $next.is(":visible") != true) $next = $next.next();
			if($next.length>0 && $next.is(":visible") != true) $next = $next.next();
			if($next.length>0 && $next.is(':visible')){
				$next.insertBefore($area);
			}
		}
	} else {
		if(direction=='up'){
			var $prev = $area.prev();
			if($prev.length>0 && $prev.is(":visible") != true) $prev = $prev.prev();
			if($prev.length>0 && $prev.is(":visible") != true) $prev = $prev.prev();
			if($prev.length>0){
				$area.insertBefore($prev);
			}
		} else if(direction=='down'){
			var $next = $area.next();
			if($next.length>0 && $next.is(":visible") != true) $next = $next.next();
			if($next.length>0 && $next.is(":visible") != true) $next = $next.next();
			if($next.length>0 && $next.is(':visible')){
				$area.insertAfter($next);
			}
		}
	}
}<%
////////////////////////////////
//구성요소

//구성요소 팝업 열기 - 트리클리 or 설정 버튼 %>
var noBothBody = '${noBothBody}'=='true';
function setLoutPop(obj, pageTyp){
	var seq = null;
	if(obj!=null) seq = $(obj).closest('div#itemsArea').attr("data-seq");
	var popTitle=callMsg('wf.jsp.setup.title.'+pageTyp);
	dialog.open("setLoutDialog", popTitle, "./setLoutPop.do?menuId=${menuId}&pageTyp="+pageTyp+(seq!=null ? '&seq='+seq : ''));
}<%
// 구성요소 삭제 열기 - 삭제 버튼 %>
function delItems(obj){
	var itemsArea=$(obj).closest('div#itemsArea');
	$(itemsArea).remove();
	$("#formArea #itemsArea").removeClass('lout_on');
	gItemArea=null;
}
var gItemArea=null;
<%

//항목지정 - 새로운 항목 삽입용 시퀀스 %>
var gMaxItemSeq = 0, loutFromAreaId='loutFormArea0';<%

//항목지정 적용 %>
function setItemsData(id, param, seq){
	if(!$formBuilder.chkComponentCnt(id)) return;
	if(seq==null || seq=='') seq=++gMaxItemSeq;
	var $itemsArea = $("#formArea #"+loutFromAreaId).children("#itemsArea[data-seq='"+seq+"']");<%// 기존영역 %>
	var html = $("#itemsHiddenArea #itemsArea")[0].outerHTML;<%// 신규 삽입 html %>
	
	if($itemsArea.length>0){
		$(html).insertBefore($itemsArea[0]);<%// 신규 영역 insert %>
		var $prev = $itemsArea.prev();
		$itemsArea.remove();<%// 기존영역 삭제 %>
		$itemsArea = $prev;<%// $itemsArea 에 신규 삽입된 영역 할당 %>
		gItemArea=null;
	} else if(gItemArea!=null){
		$(html).insertAfter(gItemArea);<%// 선택된 영역 아래 insert %>
		var $next = gItemArea.next();
		$itemsArea = $next;<%// $itemsArea 에 신규 삽입된 영역 할당 %>
	} else {
		$("#formArea #"+loutFromAreaId).append(html);
		$itemsArea = $("#formArea #"+loutFromAreaId).find('div#itemsArea:last');<%// $itemsArea 에 신규 삽입된 영역 할당 %>
		gItemArea=null;
	}
	$itemsArea.attr("data-id", id);<%// id attr 설정 %>
	$itemsArea.attr("data-seq", seq);<%// seq attr 설정 %>
	$itemsArea.find("a#delBtn").attr("onclick","delItems(this)");<%// 삭제 버튼의 seq 적용 %>
	
	if(id=='gap' || id=='line' || id=='label'){ <%// 테이블이 아닐 경우 타이틀과 설정 버튼 제거 %>
		$itemsArea.find('div.tit_left').remove();
		$itemsArea.find('a').eq('#initBtn, #titleSetupBtn, #tableSetupBtn').remove();
		$itemsArea.find("#itemsViewArea table:first").remove();
	}
	if(id!='table'){ <%// 테이블이 아닐 경우 타이틀과 설정 버튼 제거 %>
		$itemsArea.find('a').eq('#initBtn, #tableSetupBtn').remove();
	}
	if(id=='label'){ // 레이블
		$component=componentList[id];
		$fields=$component.fields;
		var coId=id+''+$formBuilder.getComponentId(id);
		var dataName=$fields.label!=undefined ? ' data-name="'+$fields.label.value+'"' : '';
		
		// 언어별 이름
		var dataJson=$formBuilder.getNameRescJson($fields.label.value, true);
		
		var buffer=[];
		buffer.push('<div class="component_list label" id="'+coId+'Area" data-id="'+coId+'"'+dataName+dataJson+' data-coltyp="label" onclick="setProp(this, event);">');
		buffer.push('<div class="noSelect">');		
		buffer.push('<label for="'+coId+'" class="header">'+$fields.label.value+'</label>');
		buffer.push('</div></div>');
		
		$itemsArea.find('div#loutsViewArea').append(buffer.join(''));
		$itemsArea.find('div#itemsViewArea').remove();
		
	}else if(id=='gap'){ // 공백
		$itemsArea.find('div#loutsViewArea').append('<div class="blank"></div>');
	}else if(id=='line'){ // 라인
		$itemsArea.find('div#loutsViewArea').append('<div class="line"></div>');
	}else if(id=='editor' || id=='file'){ // 에디터 or 파일
		$itemsArea.find('#titleSetupBtn').show();
		$component=componentList[id];
		$fields=$component.fields;
		var coId=id+''+$formBuilder.getComponentId(id);
		var dataName=$fields.name!=undefined ? ' data-name="'+$fields.name.value+'"' : '';
		
		// 언어별 이름
		var dataJson=$formBuilder.getNameRescJson($fields.name.value, true);
		if(id=='editor'){
			$itemsArea.find('div#loutsViewArea').append('<div class="editor2 component_list" data-colTyp="editor" id="'+coId+'Area" data-id="'+coId+'"'+dataName+dataJson+' onclick="setLoutProp(this, event);"><div class="editor3"><img src="${_ctx}/images/etc/editor1.png"/></div></div>');	
		}else if(id=='file'){
			$itemsArea.find('div#loutsViewArea').append('<div class="file2 component_list" data-colTyp="file" id="'+coId+'Area" data-id="'+coId+'"'+dataName+dataJson+' onclick="setLoutProp(this, event);"><div class="file3"><img src="${_ctx}/images/etc/file1.png"/></div></div>');	
		}
	}else if(id=='table'){ // 테이블
		$itemsArea.find('div#loutsViewArea').remove();
		
		// 레이아웃 구분
		var loutTyp = param.get('loutTyp');
		if(loutTyp==null) loutTyp = 'D'; // 기본
		if(loutTyp!='D' ) // 목록형(가변)이 아닐경우
			$itemsArea.find('#cellSetupBtn, #cellMergeBtn, #cellSplitWBtn, #cellSplitHBtn, #rowAddBtn, #rowDelBtn, #colAddBtn, #colDelBtn' ).show();
		$itemsArea.find('#titleSetupBtn, #tableSetupBtn, #initBtn').show();
		var $itemTables = $itemsArea.find("#itemsViewArea table:first");
		$itemTables.css('table-layout', '');
		<%// colgroup 설정 %>
		var colCnt = parseInt(param.get("colCnt"), 10), $colgroup = $itemsArea.find("#itemsViewArea colgroup");
		if(loutTyp=='D'){
			if(colCnt==1) $colgroup.html('<col width="13%" /><col width="87%" />');
			if(colCnt==2) $colgroup.html('<col width="13%" /><col width="37%" /><col width="13%" /><col width="37%" />');
			if(colCnt==3) $colgroup.html('<col width="13%" /><col width="20%" /><col width="13%" /><col width="21%" /><col width="13%" /><col width="20%" />');
			if(colCnt==4) $colgroup.html('<col width="10%" /><col width="15%" /><col width="10%" /><col width="15%" /><col width="10%" /><col width="15%" /><col width="10%" /><col width="15%" />');
		}else{
			var wdthBuffer=[];
			$wdth=parseInt(100/colCnt);
			$addWdth=100-($wdth*colCnt);
			var wdthPer=0;
			for(var i=0;i<colCnt;i++){
				wdthPer=$wdth;
				if((i+1)==colCnt && $addWdth>0) wdthPer+=$addWdth;
				wdthBuffer.push('<col width="'+wdthPer+'%" />');
			}
			if(wdthBuffer.length>0)
				$colgroup.html(wdthBuffer.join(''));
			
		}
		<%// view 설정 %>
		var $tbody = $itemsArea.find("#itemsViewArea tbody");
		var tblId=$formBuilder.getComponentId('tblId');
		
		var tbl=$tbody.closest('table');
		tbl.attr('id', 'table'+tblId);
		
		$itemsArea.attr("data-loutTyp", loutTyp);<%// 테이블 레이아웃구분 설정 %>
		
		var row = '', keys, vals, $tr=null, arr, rowIdx=0, trId;
		param.each(function(key, va){
			if(key.endsWith("-nm")){
				trId='';
				keys = key.split('-');
				vals = va.split('-');
				vals[1] = vals[1]=='1' ? '' : ' colspan="'+(loutTyp=='D' ? (parseInt(vals[1])*2)-1 : parseInt(vals[1]))+'"';
				if(row!=keys[0]){
					row = keys[0];
					if(loutTyp=='V'){
						trId='id="row"';
						//if(rowIdx==0) trId='id="headerTr"';
						//else trId='id="row"';
					}
					$tbody.append("<tr "+trId+"></tr>");
					$tr = $tbody.find("tr:last");
					rowIdx++;
				}
				arr = [];
				if(loutTyp=='D'){
					arr.append('<td class="head_ct" data-colTyp="label" onclick="setProp(this, event);">').append(vals[0]).append('</td>');
					arr.append('<td class="body_lt" data-colTyp="cell" data-drag="Y" onclick="setProp(this, event);"').append(vals[1]).append('></td>');
				}else{
					arr.append('<td class="body_ct cell_fix" data-colTyp="cell" data-drag="Y" onclick="setProp(this, event);"').append(vals[1]).append('>');
					arr.append('<div class="check" '+vals[1]+'><input type="checkbox" onclick="cellChk(this, event);"/></div>');
					arr.append('</td>');
				}
				
				$tr.append(arr.join(''));
				setJsUniform($tr);
			}
		});
		$itemTables.css('table-layout', 'fixed');
		if(loutTyp=='D'){
			$tbody.find('td.head_ct').each(function(){
				$formBuilder.addComponent(this, 'label', false);	
			});
		}
		
		<%
		// hidden 설정%>
		var $dataArea = $itemsArea.find("#itemsDataArea");
		param.each(function(key, va){
			if(!key.endsWith("-nm")){
				if(key.endsWith("-list")){
					$dataArea.append("<input type='hidden' name='items-"+seq+"-"+key+"' value='"+va+"' />");
				}else{
					$dataArea.append("<input type='hidden' name='items-"+seq+"-"+key+"' value='"+escapeValue(va)+"' />");
				}
				
			}
		});
	}
	setItemAreaEvt($itemsArea);
}
<%// 테이블 레이아웃(최대 가로길이) 변경 %>
function setTableLouts(param, seq){
	var $itemsArea = $("#formArea #"+loutFromAreaId).children("#itemsArea[data-seq='"+seq+"']");<%// 기존영역 %>
	if($itemsArea==null || param==null || param.get('max-width')==null) return;
	
	var maxWdth=param.get('max-width');
	$itemsArea.css('max-width', maxWdth);
	unChkTds();
}
<%// 테이블 가로 길이 변경 %>
function setTables(param, seq){
	var $itemsArea = $("#formArea #"+loutFromAreaId).children("#itemsArea[data-seq='"+seq+"']");<%// 기존영역 %>
	if($itemsArea==null || param==null || param.get('wdths-list')==null) return;
	
	var wdths=param.get('wdths-list');
	
	if(wdths!=null){
		$colgroup = $itemsArea.find("#itemsViewArea colgroup");
		var jsonObj=JSON.parse(wdths);
		var wdthBuffer=[];
		$.each(jsonObj, function(index, json){
			$wdth=json.value;
			if($wdth==null) $wdth='';
			wdthBuffer.push('<col width="'+$wdth+'" />');
		});
		if(wdthBuffer.length>0)
			$colgroup.html(wdthBuffer.join(''));
		
		//var $dataArea = $itemsArea.find("#itemsDataArea");
		// 속성값
		//$dataArea.find("input[name='items-"+seq+"-wdths-list']").remove();
		//$dataArea.appendHidden({name:'items-'+seq+'-wdths-list',value:escapeValue(wdths)});
		
	}
	unChkTds();
}<%// 타이틀 삽입 %>
function setTitles(param, seq){
	var $itemsArea = $("#formArea #"+loutFromAreaId).children("#itemsArea[data-seq='"+seq+"']");<%// 기존영역 %>
	if($itemsArea==null) return;
	if(param==null) return;
	var rescUseYn=param.get('rescUseYn') || 'Y';
	var titleArea=$itemsArea.find('div.tit_left');
	if(rescUseYn=='Y'){
		if(titleArea.length==0){
			titleArea=$itemsArea.find('div.titlearea').eq(0);
			var buffer=[];
			buffer.push('<div class="tit_left"><dl><dd class="title_s">');
			buffer.push(param.get('rescVa_${_lang}'));
			buffer.push('</dd></dl></div>');
			titleArea.prepend(buffer.join(''));
		}else{
			titleArea.find('dd.title_s').text(param.get('rescVa_${_lang}'));
		}	
	}else{
		if(titleArea.length>0) titleArea.remove();
	}
	
	// hidden 설정%>
	var $dataArea = $itemsArea.find("#itemsDataArea");
	param.each(function(key, va){
		if(!key.endsWith("-nm")){
			$name="items-"+seq+"-"+key;
			// 속성값
			$dataArea.find("input[name='"+$name+"']").remove();
			$dataArea.appendHidden({name:$name,value:va});
		}
	});
	unChkTds();
}
<%// ItemArea 이벤트 추가 %>
function setItemAreaEvt($itemsArea){
	if($itemsArea===undefined) $itemsArea=$('#formArea #itemsArea');
	$itemsArea.on({
		mouseover:function(event){
			if(isViewAreaTrue()){
				$(this).addClass('selectItems');
			}
		},
		mouseout:function(event){
			$(this).removeClass('selectItems');
		},
		click:function(event){
			if(isViewAreaTrue()){
				if($(this).hasClass('lout_on')){
					$(this).removeClass('lout_on');
					gItemArea=null;
				}else{
					$("#formArea #itemsArea").removeClass('lout_on');
					$(this).addClass('class', 'lout_on');
					//$(this).attr('class', 'lout_on');
					gItemArea=$(this);
				}
				$("#formArea #itemsArea td.cell_on").removeClass('cell_on');
				gCellObj=null;
			}
		}
	});
}

<%// 클릭 이벤트 체크 %>
function isViewAreaTrue(){
	if(!$(event.target).closest('div#itemsViewArea, div#loutsViewArea').length &&
		       !$(event.target).is('div#itemsViewArea, div#loutsViewArea')) {
		return true;
	}
	return false;
}

<% // 탭 설정 해제 %>
function setTab(){
	if($("#topArea #tabArea").css("display") == "none"){	
		$('#topArea #tabArea').show();
		setTabEvt($('#topArea #tabArea li:first').find('div.tab1'));
		$('#topArea #tabArea li:first').attr('data-drag', 'Y');
		$('#formArea input[id="tabYn"]').val('Y');
	}else{
		$.each($('#topArea #tabArea li'), function(){
			if($(this).attr('data-areaid')!=undefined)
				offTabEvt($(this).find('div.tab1'));
		});
		$('#topArea #tabArea li').not('.basic_open,:last').remove();
		//$('#topArea #tabArea li:first').attr("class","basic_open");
		$('#topArea #tabArea li.basic_open dd.title span:first').text('<u:msg titleId="wf.jsp.form.tab.title" alt="새 탭"/>');
		var dataAreaId=$('#topArea #tabArea li.basic_open').attr('data-areaid');
		$('#formArea div[id^="loutFormArea"]').not('[id="'+dataAreaId+'"]').remove();
		$('#topArea #tabArea').hide();
		$('#formArea input[id="tabYn"]').val('N');
	}
	
}
<% // 탭 생성 %>
function createTab(areaId, no){
	var tabUl=$('div#'+areaId).find('ul:first');
	var tabObj=tabUl.find('li#tab_'+no);
	if(tabObj.hasClass('basic_open'))
		return;
	closeTabEvt(); // 탭이름 영역 변경 숨김
	unChkTds(); // 체크된 td 초기화
	unChkComponent(); // 선택한 컨포넌트 해제
	var dataAreaId='loutFormArea'+no;
	if(tabObj.attr('data-areaid')===undefined){
		var buffer=[];
		var newNo=parseInt(no)+1;
		tabObj.attr('data-drag', "Y");
		tabObj.attr('data-areaid', dataAreaId);
		tabObj.find('dd.title span:first').text('<u:msg titleId="wf.jsp.form.tab.title" alt="새 탭"/>');
		tabObj.find('dd.btn').show();
		buffer.push('<li class="basic" id="tab_'+newNo+'" style="display:inline-block;"><div class="tab1" onclick="createTab(\'tabArea\','+newNo+');"><div class="tab2"><dl><dd class="title">');
		buffer.push('<span>&nbsp;</span></dd><dd class="btn" style="display:none;"><a class="delete" onclick="deleteTab(event, \'tabArea\','+newNo+');" href="javascript:void(0);"><span>delete</span></a></dd>');
		buffer.push('</dl></div></div></li>');
		
		tabUl.append(buffer.join(''));
		$('#formArea').append('<div class="loutFormArea" id="'+dataAreaId+'"></div>');
		
		setTabEvt(tabObj.find('div.tab1'));
	}
	tabUl.find('li[data-areaid^="loutFormArea"]').attr("class","basic");
	$(tabObj).attr("class","basic_open");
	
	$('#formArea div[id^="loutFormArea"]').hide();
	
	$('#'+dataAreaId).show();
	loutFromAreaId=dataAreaId;
	gItemArea=null;
}<% // 탭삭제 %>
function deleteTab(event, areaId, no){
	var tabUl=$('div#'+areaId).find('ul:first');
	if(tabUl.find('li[data-areaid^="loutFormArea"]').length==1){
		alertMsg('wf.msg.not.delete'); // wf.msg.not.delete=더이상 삭제할 수 없습니다.
		return;
	}
	var tabObj=tabUl.find('li#tab_'+no);
	if(tabObj.hasClass('basic_open')){
		if(tabObj.prev().length>0){
			tabObj.prev().find('div.tab1').trigger('click');
		}else if(tabObj.next().length>0){
			tabObj.next().find('div.tab1').trigger('click');
		}	
	}
	var dataAreaId=tabObj.attr('data-areaid');
	$('#'+dataAreaId).remove();
	offTabEvt(tabObj.find('div.tab1'));
	tabObj.remove();
	notEvtBubble(event);
}<% // 탭이름 변경 화면 보임 %>
function setTabNm(){
	var tab=$('#topArea #tabArea li.basic_open:first');
	var text=$('#tabNmArea input[id="tabRescVa_${_lang}"]').val();
	if(text==''){
		alertMsg('wf.msg.name.empty'); // wf.msg.name.empty=이름은 필수 입력입니다.
		return;
	}
	
	var tabNmData={}, tabNm, langTypCd;
	$.each($('#tabNmArea input[id^=tabRescVa]'), function(){
		tabNm=$(this).val();
		if(tabNm=='') tabNm=text;
		langTypCd=$(this).attr('id').replace('tabRescVa_','');
		tabNmData[langTypCd]=tabNm;
	});
	//var rescId=$('#tabNmArea input[id="tabRescId"]').val();
	//if(rescId!='')
	//	tabNmData['rescId']=rescId;
	tab.find('span:first').text(text);
	tab.attr('data-json', JSON.stringify(tabNmData));
	closeTabEvt();
}

<% // [마우스 더블클릭] - 탭이름 변경%>
function setTabEvt(target){
	if(target===undefined) target=$('#topArea #tabArea li div.tab1');
	target.on('dblclick', function(){
		var tabNmArea=$('#tabNmArea');
		tabNmArea.show();
		var target=$(this).closest('li');
		var top=target.offset().top;
		var left=target.offset().left;
		var leftPlus=target.find('div.tab1').position().left;
		if(leftPlus>0) left+=leftPlus;
		tabNmArea.offset({top:top+30, left:left});
		if(target.closest('li').attr('data-json')!=undefined){
			var dataJson=JSON.parse(target.closest('li').attr('data-json'));
			if(dataJson!=undefined){
				$.each(dataJson, function(key, va){
					tabNmArea.find('input[id="tabRescVa_'+key+'"]').val(va);
				});
				var langSelect=tabNmArea.find('select#langSelector');
				var lang=langSelect.val();				
				if(lang != '${_lang}'){
					langSelect.val('${_lang}');
					langSelect.trigger('change');
				}
			}
		}
		
	});
}
<% // [마우스 더블클릭] - 이벤트 제거 %>
function offTabEvt(target){
	if(target===undefined) target=$('#topArea #tabArea li div.tab1');
	target.off('dblclick');
}
<% // 탭이름 변경 영역 숨김 %>
function closeTabEvt(){
	var tabNmArea=$('#tabNmArea');
	tabNmArea.find(':text').val('');
	tabNmArea.offset({top:0, left:0});
	tabNmArea.hide();
}

<% // [유효성 검증] %>
function formValidator(tabYn){
	var returnFlag=true;
	$('#itemsArea div.component_list').not('[id^="label"]').each(function(){
		if($(this).attr('data-name')===undefined){
			alertMsg('wf.msg.name.empty'); // wf.msg.name.empty=이름은 필수 입력입니다.
			if(tabYn=='Y'){
				var tabUl=$('#topArea #tabArea').find('ul:first');
				var areaId=$(this).closest('div.loutFormArea').attr('id');
				var tabLi=tabUl.find('li[data-areaid="'+areaId+'"]');
				if(!tabLi.hasClass('basic_open'))
					tabLi.find('div.tab1').trigger('click');
			}
			$(this).trigger('click');
			returnFlag=false;
			return false;
		}
	});
	return returnFlag;
}

<% // [하단버튼:저장] - 저장 %>
function save(){
	var tabYn=$('#formArea input[name="tabYn"]').val();
	if(formValidator(tabYn)){
		setFormData(true);
		var param = new ParamMap().getData('setForm');
		
		callAjax('./transFormAjx.do?menuId=${menuId}', param, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				reloadForm();
			}
		});
	}
}

<% // 폼에 json으로 저장 %>
function setFormData(isSave){
	var $form=$('#setForm');
	
	// html 저장[폼수정용, 사용자 화면용]
	var formHtml=$('#formArea').html();
	$('#htmlHiddenArea').html(formHtml);
	
	var attrVa={}, attrMobVa={}, singleListVa={};
	var index=0;
	
	var loutList=[], colmList=[], loutMobList=[]; // DB 테이블의 컬럼 목록
	var jsonList=null, jsonMobList=null, dataId=null, dataSeq, id, itemsArea;
	
	var tabYn=$('#formArea input[name="tabYn"]').val();
	var tabList=[];
	var loutFindList=[]; // 레이아웃 순서 배열
	var tabMobVa={}, tabMobList=[], tabMobAttr={};
	
	if(tabYn=='Y'){ // json으로 탭 데이터 저장		
		var loutId=null;
		var tabs=null, dataJsonStr=null, dataJson;
		$.each($('#topArea div#tabArea li[data-areaid^="loutFormArea"]'), function(idx, tab){
			loutId='loutFormArea'+idx;
			tabs={loutId:loutId, title:$(this).find('dd.title span:first').text()};
			dataJsonStr=$(this).attr('data-json');
			if(dataJsonStr!=undefined){
				dataJson=JSON.parse(dataJsonStr);
				tabs['dataJson']=dataJson;
			}
			tabList.push(tabs);
			loutFindList.push($('#htmlHiddenArea div#'+$(this).attr('data-areaid')));
			tabMobList.push({loutId:loutId, title:$(this).find('dd.title span:first').text()});
			tabMobAttr[loutId]="Y";
		});
		tabMobVa['tabList']=tabMobList;
		tabMobVa['tabAttr']=tabMobAttr;
	}
	if(loutFindList.length==0) loutFindList=$('#htmlHiddenArea div.loutFormArea');
	
	var componentId, colTyp, singleId, itemStyles;
	loutFindList.each(function(idx, lout){
		//if($(lout).html()=='') return true;
		
		// 중복ID 체크 맵
		var dupChkIdMap=new Map();
		
		$loutFormAreaId='loutFormArea'+idx;
		jsonList=[];
		jsonMobList=[];
		// json 데이터 저장
		$(lout).find('div#itemsArea').each(function(){
			itemsArea=this;
			$(this).find('div.component_list').each(function(){
				if($(this).attr('data-json')===undefined) return true;
				var jsonStr=$(this).attr('data-json');
				if(jsonStr==undefined || jsonStr=='') return true;
				//jsonStr=jsonStr.replaceAll('&#13;&#10;','\\r\\n');
				var json=JSON.parse(jsonStr);
				if(json['name']==null){
					$itemNm=$(this).attr('data-name');
					if($itemNm!=undefined) json['name']=$itemNm;
				}
				singleId=$(this).attr('data-singleid');
				if(singleId!=undefined)
					json['singleId']=singleId;
				var va = $(this).attr('data-id').replaceAll('"','\\"');
				attrVa[va]=json;
				index++;
			});
			
			itemStyles={}; // 항목 스타일
			
			itemStyles['maxWidth']=$(this).css('max-width');
			
			dataId=$(this).attr('data-id');
			if(dataId=='table'){
				// 타이틀 
				/* $title='';
				if($(this).find('div.tit_left').length>0){
					$title=$(this).find('div.tit_left .title_s').text();
				}
				 */
				
				$colgroup=[]; // colgroup
				$(this).find('table:first colgroup > col').each(function(){
					$colgroup.push($(this).attr('width'));
				});
				$row=[];
				$tbody=$(this).find('tbody:first');
				$notTrColmList=getTableTrList($tbody);
				$tbody.children().each(function(){
					$trIndex=$(this).index(); // tr의 인덱스
					$isColm=true;
					// 반복으로 지정된 행의 컬럼 수집 제외
					if($notTrColmList!=null && $notTrColmList.length>0){
						$.each($notTrColmList, function(){
							if(this['start']<=$trIndex && this['end']>=$trIndex){
								$isColm=false;
								return false;
							}
						});
					}
					//if($(this).attr('id')=='hiddenTr') return true;
					$cell=[];
					$(this).children().each(function(){
						$components=[];
						$(this).find('div.component_list').each(function(){
							componentId=$(this).attr('data-id');
							colTyp=$(this).attr('data-colTyp');
							$itemNm=$(this).attr('data-name');
							$componentsData={id:componentId, coltyp:colTyp, title:$itemNm};							
							$components.push($componentsData);							
							if(isSave && $isColm && !($(this).attr('data-colTyp')=='label' || $(this).attr('data-colTyp')=='button')){ // 반복행의 컬럼목록은 수집하지 않음.
								singleId=$(this).attr('data-singleid');
								if((singleId===undefined && colTyp != 'radioSingle' && colTyp != 'checkboxSingle') || (singleId!=undefined && dupChkIdMap.get(singleId)==null)){
									colmList.push({colmId:$(this).attr('data-colmId'), colmTypCd:$(this).attr('data-colTyp'), colmNm:$(this).attr('data-id'), itemNm:$itemNm}); // 저장여부가 true면 컬럼목록에 추가
									jsonMobList.push({colmNm:$(this).attr('data-id'), useYn:'Y'}); // 모바일 컬럼 목록
									attrMobVa[$(this).attr('data-id')]='Y';
									if(singleId!=undefined && dupChkIdMap.get(singleId)==null){
										dupChkIdMap.put(singleId, 'Y');
									}
								}
							}
						});
						$cell.push({colspan: $(this).attr('colspan'), rowspan: $(this).attr('rowspan'), colTyp:$(this).attr('data-colTyp'), clsnm: $(this).attr('class').replace(/(\s|)cell-selected|(\s|)cell_on|/g, ''), components:$components});
					});
					$row.push({id:$(this).attr('id'), groupId: $(this).attr('data-groupid'), cell:$cell});
				});
				$titles={}; // 제목 목록
				$itemData=[]; // 테이블 설정 히든값
				if($(this).find('#itemsDataArea input[type="hidden"]').length>0){
					dataSeq=$(this).attr('data-seq');
					$.each($(this).find('#itemsDataArea input[type="hidden"]'), function(){
						$itemId=$(this).attr('name').replace('items-'+dataSeq+'-','');
						$itemData.push({name:$itemId, value:escapeValue($(this).val())});
						if($itemId.startsWith('resc')){
							$titles[$itemId]=escapeValue($(this).val());
						}
					});
				}
				$table=$($tbody).closest('table');
				
				jsonList.push({type:dataId, titles:$titles, tblId:$($table).attr('id'), loutTyp:$(this).attr('data-loutTyp'), colgroup:$colgroup, row:$row, itemData:$itemData, itemStyles:itemStyles});
			}else{
				id=$(this).find('div.component_list').eq(0).attr('data-id');
				$div=$(this).find('div.component_list');
				$itemNm=$div.attr('data-name');
				
				$titles={}; // 제목 목록
				$itemData=[]; // 테이블 설정 히든값
				if($(this).find('#itemsDataArea input[type="hidden"]').length>0){
					dataSeq=$(this).attr('data-seq');
					$.each($(this).find('#itemsDataArea input[type="hidden"]'), function(){
						$itemId=$(this).attr('name').replace('items-'+dataSeq+'-','');
						$itemData.push({name:$itemId, value:escapeValue($(this).val())});
						if($itemId.startsWith('resc')){
							$titles[$itemId]=escapeValue($(this).val());
						}
					});
				}
				
				jsonList.push({type:dataId, jsonId:id, titles:$titles, itemData:$itemData});
				if(isSave && $itemNm!=null && !($div.attr('data-colTyp')=='label')){
					colmList.push({colmId:$div.attr('data-colmId'), colmTypCd:$div.attr('data-colTyp'), colmNm:id, itemNm:$itemNm}); // 저장여부가 true면 컬럼목록에 추가
					jsonMobList.push({colmNm:id, useYn:'Y'}); // 모바일 컬럼 목록
					attrMobVa[id]='Y';
				}
			}			
		});
		loutList.push({loutId:$loutFormAreaId, list:jsonList});
		if(jsonMobList.length>0)
			loutMobList.push({loutId:$loutFormAreaId, list:jsonMobList});
	});
	
	// 컴포넌트 ID 최대값
	attrVa['maxId']=$formBuilder.getComponentMaxId();
	
	//console.log('maxId : '+attrVa['maxId']);
	attrVa['tabYn']=tabYn;
	
	attrVa['singleList']=singleListVa;
	
	// 속성값
	$form.find("input[name='attrVa']").remove();
	$form.appendHidden({name:'attrVa',value:JSON.stringify(attrVa)});
	
	// 레이아웃값
	$form.find("input[name='loutVa']").remove();
	if(loutList.length>0)	$form.appendHidden({name:'loutVa',value:JSON.stringify(loutList)});
	else $form.appendHidden({name:'loutVa',value:''});
	
	// 레이아웃값(모바일)
	$form.find("input[name='loutMobVa']").remove();
	var loutMobVa={};
	if(loutMobList.length>0) loutMobVa['loutList']=loutMobList;
	loutMobVa['loutAttr']=attrMobVa;
	$form.appendHidden({name:'loutMobVa',value:JSON.stringify(loutMobVa)});
	
	// 탭 목록
	$form.find("input[name='tabVa']").remove();	
	if(tabList.length>0) $form.appendHidden({name:'tabVa',value:JSON.stringify(tabList)});
	else $form.appendHidden({name:'tabVa',value:''});
	
	// 탭 목록(모바일)
	$form.find("input[name='tabMobVa']").remove();	
	$form.appendHidden({name:'tabMobVa',value:JSON.stringify(tabMobVa)});
	
	// 컬럼 목록
	$form.find("input[name='colmList']").remove();
	if(isSave && colmList.length>0){
		$form.appendHidden({name:'colmList',value:JSON.stringify(colmList)});
	}
	
	//console.log('loutList : '+JSON.stringify(loutList));
	//console.log('attrVa : '+JSON.stringify(attrVa));
	//console.log('tabList : '+JSON.stringify(tabList));
	//console.log('colmList : '+JSON.stringify(colmList));
	
	$('#htmlHiddenArea').html('');
}

<% // 버튼이 추가되어 있는 tr 영역 %>
function getTableTrList(tbody){
	var arrs=[];
	tbody.children().each(function(){
		$buttonList=$(this).find('div.component_list[data-coltyp="button"]');
		if($buttonList.length>0){
			$.each($buttonList, function(){				
				arrs.push(getTrList(tbody, $(this)));
			});
		}
	});
	return arrs;
}<% // [하단버튼:이력보기] - 이력보기 팝업%>
function viewRegFormPop(){
	dialog.open('viewRegFormDialog','<u:msg titleId="wf.btn.hst.regForm" alt="이력보기" />','./viewRegFormPop.do?menuId=${menuId}&formNo=${param.formNo}');
}<% // [하단버튼:전체양식보기] - 전체양식보기 팝업%>
function allRegFormPop(){
	dialog.open('allRegFormDialog','<u:msg titleId="wf.btn.hst.regForm" alt="이력보기" />','./allRegFormPop.do?${paramsForList}&formNo=${param.formNo}');
}<% // [하단버튼:미리보기] - 미리보기 팝업%>
function previewPop(){
	setFormData(false); // json 저장
	
	var param = new ParamMap().getData('setForm');
	
	callAjax('./previewFormAjx.do?menuId=${menuId}', param, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			dialog.open('previewFormDialog','<u:msg titleId="cm.btn.preview" alt="미리보기" />','./previewRegFormPop.do?menuId=${menuId}&formNo=${param.formNo}');
		}
	});
}<% // [하단버튼:화면구성(모바일)] - 모바일 화면구성 팝업 %>
function setMobRegPop(){
	dialog.open('setMobRegDialog','<u:msg titleId="wf.btn.set.lout.mob" alt="화면구성(모바일)" />','./setMobRegPop.do?menuId=${menuId}&formNo=${param.formNo}');
}
<% // [하단버튼:취소] - 목록화면으로 이동 %>
function listForm(){
	parent.location.href="./listForm.do?${paramsForList}";
}
function setFormMod(option){
	<c:if test="${!empty wfFormRegDVo.loutVa }">
	var tabYn=$('#formArea input[name="tabYn"]').val(); // 탭 사용여부
	if(tabYn=='Y'){ // 탭 더블클릭 이벤트[탭이름 변경]
		$.each($('#topArea div#tabArea li[data-areaid^="loutFormArea"]'), function(){
			setTabEvt($(this).find('div.tab1'));
		});	
	}
	
	setItemAreaEvt(); // ItemArea 이벤트
	var selectItemsArea = $("#formArea #"+loutFromAreaId).children("#itemsArea:last"); // 마지막 Item 클릭 이벤트
	if(selectItemsArea.length>0){
		selectItemsArea.trigger('click');
	}
	</c:if>
	<c:if test="${!empty wfFormRegDVo.attrVa}">
		// 컴포넌트ID 값 세팅
		var attrVa='<u:out value="${wfFormRegDVo.attrVa}" type="script"/>';
		attrVa=replaceJsonString(attrVa);
		//attrVa=attrVa.replaceAll('\r\n', '\\r\\n');
		var jsonVa=JSON.parse(attrVa);
		if(jsonVa['maxId']!=undefined){
			option['maxIdList']=jsonVa['maxId'];
		}
		
	</c:if>
}

<% // 페이지 리로드 %>
function reloadForm(url, dialogId){
	if(dialogId!=undefined){
		dialog.close(dialogId);
	}
	parent.location.href="./setRegForm.do?${paramsForList}&formNo=${param.formNo}";
}
<% // 전체양식 선택 - 페이지 변경 %>
function setRegPage(formNo, genId){
	parent.location.href="./setRegForm.do?${paramsForGen}&paramFormNo="+formNo+"&paramGenId="+genId;
}<% // 코드그룹 목록 조회 %>
function getCdGrpListAjx(){
	var cdGrpJsonString=null;
	callAjax("./getCdGrpListAjx.do?menuId=${menuId}", null, function(data){
		if (data.message != null) {
			alert(data.message);
		}
		if (data.cdGrpJsonString!=null) {
			cdGrpJsonString=data.cdGrpJsonString;
		}	
	}, null, null, false); // 동기화
	return cdGrpJsonString;
}
<% // 컴포넌트 드래그앤 드랍 및 정렬 %>
function setEvtDragComporent(target){
	//$("td[data-drag='Y']").sortable('destroy');
	if(target===undefined) target=$("td[data-drag='Y']");
	// [우측] - 컴포넌트 정렬
	target.sortable({
		accurateIntersection: false,
		forcePlaceholderSize:true,
		cursor:"move",
		placeholder: 'placeholder_sort',
		revert: true,
		helper:'clone',
		//axis:'x',
		start: function(event, ui){
		    $wdth=ui.helper.width();
		    ui.helper.css("width", $wdth+'px');
	        ui.placeholder.css('height', ui.helper.css('height'));
	        ui.placeholder.css('width', ui.helper.css('width'));
	       
	    },
	    connectWith: "td[data-drag='Y']",
	    over: function(event, ui){
	    	//console.log('=================end================');
	    	/* if($(event.target).closest('li.basic').length || $(event.target).is('li.basic')) {
   		        console.log('===========555================');
   		    } */
	    },
		items: "div.component_list"
    });
	$("td[data-drag='Y']").disableSelection();
};
<% // 탭 드래그앤 드랍 및 정렬 %>
function setEvtDragTab(target){
	// 탭
	$("#tabGroupArea").sortable({
		accurateIntersection: false,
		forcePlaceholderSize:true,
		cursor:"move",
		placeholder: 'placeholder_sort',
		revert: true,
		helper:'clone',
		//axis:'x',
		start: function(event, ui){			
	         ui.placeholder.css('height', ui.helper.css('height'));
	         ui.placeholder.css('width', ui.helper.css('width'));
	         ui.helper.css("width", "auto");	         
	    },		
		items: "li[data-drag='Y']"
	});
	$("#tabGroupArea").disableSelection();
}
<% // 드래그앤 드랍 및 정렬 %>
function setDragAndSort(){
	
	// 탭 이벤트 삽입
	setEvtDragTab();
	
	// 컴포넌트 이벤트 삽입
	setEvtDragComporent();
	
	// [우측] - 레이아웃 정렬
	/*$("div.loutFormArea").sortable({
		accurateIntersection: false,
		forcePlaceholderSize:true,
		cursor:"move",
		placeholder: 'placeholder_sort',
		revert: true,
		helper:'clone',
		start: function(event, ui){
		    $wdth=ui.helper.width();
		    ui.helper.css("width", $wdth+'px');
	        ui.placeholder.css('height', '30px');
	        ui.placeholder.css('width', ui.helper.css('width'));
	       
	    },
	    items: "div#itemsArea"
    });*/
	
	// [좌측] - 컴포넌트
	/* $("#componentArea div.ubox").draggable({
	    connectToSortable: "td.body_lt",
	    helper: "clone",
	    revert: "invalid",
	    stop: function(event, ui){
	    	console.log(ui.helper.attr('data-typ'));
	    }
	}); */
}

var timer;
var delay = 1000;

function tabOpenEvt(){
	var tabObj;
	$('#topArea #tabArea li[data-areaid^="loutFormArea"]').hover(function(){
		tabObj=$(this);
	    timer = setTimeout(function() {
	    	tabObj.find('div.tab1').trigger('click');
	    }, delay);
	}, function() {
	    // on mouse out, cancel the timer
	    clearTimeout(timer);
	});
}
<% // 컨텍스트 메뉴 대상 %>
var contextTarget=null;
<% // 컨텍스트 메뉴 이벤트 세팅 %>
function setContextMnu(target){
	if(target===undefined) target=$('#formArea');
	target.on('contextmenu', function(){
		if(!$(event.target).closest('td.cell_fix').length &&
			       !$(event.target).is('td.cell_fix')) { // 테이블의 td 영역이 아닌경우 return
			contextTarget=null;
			return true;
		}
		// Avoid the real one
	    event.preventDefault();

		var contextArea=$('#contextMnuHiddenArea');
		contextArea.show();
		var top=event.pageY;
		var left=event.pageX;
		contextArea.offset({top:top+10, left:left});
		contextTarget=$(event.target).is('td.cell_fix') ? $(event.target) : $(event.target).closest('td.cell_fix');
		
	});
	
	$(document.body).on('click', function(evt){
		if(!$(evt.target).closest('div.contextArea').length &&
			       !$(evt.target).is('div.contextArea')) { // 테이블의 td 영역이 아닌경우 return
			closeContextMnu();
			contextTarget=null;
		}
	});
};
<% // 컨텍스트 메뉴 숨기기 %>
function closeContextMnu(){
	$('#contextMnuHiddenArea').hide();
};
$.fn.column = function() {
    return $(this)
      .filter('th, td')
      .filter(':not([colspan])')
      .closest('table')
      .find('tr')
      .filter(':not(:has([colspan]))')
      .children(':nth-child(' + ($(this).index()+1) + ')');
  }
<% // td 의 인덱스 배열 %>  
function getCellIdxList(target){
	var matrix = [], matrixrow, lookup = {}, cell,ri, rowspan, colspan, firstAvailCol=0, k, l;
	$.each(target.children(), function(trIdx){
		$.each($(this).children(), function(tdIdx){	
			cell=$(this);
			ri = trIdx;
			rowspan = cell.attr('rowspan') || 1;
			colspan = cell.attr('colspan') || 1;
			matrix[ri] = matrix[ri] || [];
			for (k = 0; k < matrix[ri].length + 1; k++) {
				if (typeof(matrix[ri][k]) === 'undefined') {
					firstAvailCol = k;
					break;
				}
			}
			// set cell coordinates and reference to the table cell
			lookup[ri + '-' + firstAvailCol] = cell;
			console.log(ri + '-' + firstAvailCol);
			for (k = ri; k < ri + rowspan; k++) {
				matrix[k] = matrix[k] || [];
				matrixrow = matrix[k];
				for (l = firstAvailCol; l < firstAvailCol + parseInt(colspan); l++) {
					matrixrow[l] = 'x';
				}
			}
			
		});
	});
	return lookup;
}
<% // td 체크 %>
function selectTdChk(typ, checked){
	if(contextTarget==null) return;
	if(checked===undefined) checked=true;
	var list=[];
	if(typ==null){
		list=$(contextTarget).closest('div#itemsViewArea').find('div.check input[type="checkbox"]');
	}else if(typ=='select'){
		
		var itemArea=contextTarget.closest('div#itemsViewArea');
		
		// 선택된 체크박스[시작,종료]
		var checkList=itemArea.find('div.check input[type="checkbox"]:checked');
		if(checkList.length<2) return;
		
		var tbody=itemArea.find('tbody:first');
		
		// 셀에 인덱스 부여
		$tables.cellIndex(true, tbody);
		
		var start=null,end=null, startRow,startCell, endRow, endCell;
		
		$.each(checkList, function(){
			if($(this).prop('checked')){
				if(start==null) start=$(this).closest('td').attr('data-index');
				else if(end==null) end=$(this).closest('td').attr('data-index');
			}
			if(start!=null && end!=null) return false;
		});
		startRow=start.split('-')[0];
		startCell=start.split('-')[1];
		
		endRow=end.split('-')[0];
		endCell=end.split('-')[1];
		
		var dataIndex, dataRow, dataCell;
		$.each(tbody.children(), function(){ // tr 목록
			$.each($(this).children(), function(){ // tr 목록
				dataIndex=$(this).attr('data-index');
				dataRow=dataIndex.split('-')[0];
				dataCell=dataIndex.split('-')[1];
				if(dataRow>=startRow && dataRow<=endRow && dataCell >= startCell && dataCell <= endCell){
					list.push($(this).find('div.check input[type="checkbox"]'));
				}
			});
		});
	}
	
	
	if(list.length>0){
		$.each(list, function(){
			if($(this).prop('checked')!=checked){
				$(this).trigger('click');	
			}
		});
		closeContextMnu(); // 컨텍스트 레이어 닫기
	}
};<% // 체크된 td의 컨포넌트 삭제 %>
function delChkComponent(){
	var list=$(contextTarget).closest('div#itemsViewArea').find('div.check input[type="checkbox"]:checked');
	if(list.length==0) return;
	$.each(list, function(){
		$(this).closest('td.cell_fix').find('div.component_list a.close-thik').trigger('click');
	});
};<% // 체크된 td의 초기화 %>
function unChkTds(){
	$('#formArea div.check input[type="checkbox"]:checked').trigger('click');
}<% // 선택한 컨포넌트 해제 %>
function unChkComponent(){
	$("#formArea #itemsArea td.cell_on").removeClass('cell_on');
	$('div#setupTabArea li[data-areaid="componentArea"] a')[0].click();
	gCellObj=null;
}<%
//[팝업] - 양식 업로드 %>
function setFormUploadPop(){
	dialog.open('setFormUploadDialog','<u:msg titleId="wf.btn.form.upload" alt="양식 업로드" />','./setFormUploadPop.do?menuId=${menuId}&formNo=${param.formNo}');
}
<% // 양식 다운로드 %>
function formDownload() {
	var $form = $('<form>', {
			'method':'post',
			'action':'./formDownload.do?menuId=${menuId}',
			'target':'dataframe'
		}).append($('<input>', {
			'name':'menuId',
			'value':'${empty menuId ? param.menuId : menuId}',
			'type':'hidden'
		})).append($('<input>', {
			'name':'formNo',
			'value':'${param.formNo}',
			'type':'hidden'
		}));
	$('form[action="./formDownload.do"]').remove();
	$(document.body).append($form);
	$form.submit();
}
<% // 양식 옵션 초기화 %>
function setFormOptions(option){
	// 글꼴
	<c:if test="${!empty fontFamilies }">
	var fontFamilies=[];
	<c:forEach items="${fontFamilies}" var="font" varStatus="status"><u:set
	test="${fn:indexOf(font,',')<0}" var="fontNm" value="${font}" elseValue="${fn:substring(font,0, fn:indexOf(font,','))}"
		/><u:set
		test="${fn:substring(fontNm,0,1)>='a' and fn:substring(fontNm,0,1)<='z' }"
		var="fontNm"
		value="${fn:toUpperCase(fn:substring(fontNm,0,1))}${fn:substring(fontNm,1,fn:length(fontNm))}"
		elseValue="${fontNm}"
		/>
	fontFamilies.push({value:'${font}', title:'${fontNm}'});
	
	</c:forEach>
	option['fontFamilies']=fontFamilies;
	</c:if>
	
	// 폰트 사이즈
	<c:if test="${!empty fontSizes }">
	var fontSizes=[];
	<c:forEach
	items="${fontSizes}" var="size" varStatus="status">
	fontSizes.push({value:'${size}', title:'${size}'});
	</c:forEach>
	option['fontSizes']=fontSizes;
	</c:if>
	
	// 폰트 스타일[굵게, 기울임, 밑줄]
	var fontStyle=[{titleId:'ap.cols.bold', name:'fontWeight', value:'bold', title:'굵게'}, {titleId:'ap.cols.italic', name:'fontStyle', value:'italic', title:'기울임'}, {titleId:'ap.cols.underline', name:'textDecoration', value:'underline', title:'밑줄'}]
	option['fontStyle']=fontStyle;
}
$(document).ready(function() {
	if(browser.ie && browser.ver<11){ // IE 면서 11버전 미만인경우 홈으로 이동
		alertMsg('wf.msg.not.support.browser'); // wf.msg.not.support.browser=해당 브라우저를 지원하지 않습니다.
		location.href="/wf/adm/form/listForm.do?menuId=${menuId}";
		return;
	}
	setUniformCSS();
	//setTabEvt();
	var option={langTypCd:'${_lang}', skin:'${_skin}'};
	var langList=[];
	<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
	langList.push('${langTypCdVo.cd}');
	</c:forEach>
	option['langList']=langList;
	<c:if test="${!empty wfFormRegDVo.formNo }">
		setFormMod(option);
	</c:if>
	gMaxItemSeq=$('#formArea input[id="dataSeq"]').val();
	
	// 제너레이터 옵션 설정
	setFormOptions(option);
	
	$formBuilder.init(option);
	
	// 컨텍스트 메뉴
	setContextMnu();
	
	$( window ).resize(function() {
		closeTabEvt();
		closeContextMnu();
	});
	setDragAndSort();
	
});
//-->
</script>

<u:title titleId="wf.jsp.set.form.title" alt="양식관리" menuNameFirst="true" />
<!-- LEFT -->
<div style="float:left; width:21.8%;">

<u:title titleId="cm.option.config" type="small" alt="설정" />

<u:titleArea frameId="propFrm" 
	innerStyle="padding:5px;"
	frameStyle="width:100%; height:590px;" >
	<u:tabGroup id="setupTabArea" noBottomBlank="">
		<u:tab id="setupTabArea" areaId="loutArea" titleId="wf.jsp.tab.lout.title" alt="레이아웃" on="true"
		/><u:tab id="setupTabArea" areaId="componentArea" titleId="wf.jsp.tab.component.title" alt="컴포넌트" on="false"
		/><u:tab id="setupTabArea" areaId="propArea" titleId="wf.jsp.tab.attr.title" alt="속성" on="false"/>
	</u:tabGroup>
	<div id="propArea" style="display:none;">
	<u:buttonArea topBlank="true" id="propBtnArea" style="display:none;">
		<u:buttonS alt="적용" href="javascript:$formBuilder.saveAttr();" titleId="cm.btn.apply"/>
	</u:buttonArea>
	<div id="attrArea"></div>
	<%-- <u:buttonArea topBlank="true" id="propBtnArea">
		<u:buttonS alt="적용" href="javascript:$formBuilder.saveAttr();" titleId="cm.btn.apply" />
	</u:buttonArea> --%>
	</div>
	<div id="componentArea" style="display:none;"><c:forEach
		items="${items}" var="lout" >
		<div class="ubox" style="width:98%;cursor:pointer;" data-typ="${lout}" onclick="setComponent('${lout}');"><dl><dd 
		class="title"><%-- <input type="checkbox" value="${lout}"/> --%><u:msg titleId="wf.form.items.${lout}" alt="Table/File/.." /></dd>
		<dd class="btn" style="float:right;"><a class="add" href="javascript:void(0);"><span>add</span></a></dd></dl></div>
</c:forEach></div>
	<div id="loutArea"><c:forEach
		items="${louts}" var="lout" ><u:set var="onclick" test="${lout eq 'tab' }" value="setTab();" elseValue="addLouts('${lout}');"/>
		<div class="ubox" style="width:98%;cursor:pointer;" onclick="${onclick}"><dl><dd 
		class="title"><u:msg titleId="wf.form.louts.${lout}" alt="Table/File/.." /></dd>
		<dd class="btn" style="float:right;"><a class="add" href="javascript:void(0);"><span>add</span></a></dd></dl></div>
</c:forEach></div>
	</u:titleArea>
</div>

	
<!-- RIGHT -->
<div style="float:right; width:77%;">

<u:title titleId="wf.jsp.set.lout.title" type="small" alt="화면 구성" />
	<%-- <u:titleButton titleId="wf.btn.add.line" alt="라인 추가" auth="A" />
	<u:titleButton titleId="wf.btn.add.gap" alt="공백 추가" auth="A" />
	<u:titleButton titleId="wf.btn.add.lout" onclick="setLoutPop();" alt="레이아웃 추가" auth="A" />
</u:title> --%>

<u:titleArea frameId="formFrm" 
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:590px;" >

<!-- 탭 -->
<jsp:include page="/WEB-INF/jsp/wf/works/inclTab.jsp" flush="false">
<jsp:param value="system" name="tabPage"/>
</jsp:include>

<!-- 컴포넌트 영역 -->
<div id="formArea" style="padding:5px;">
<jsp:include page="/WEB-INF/jsp/wf/works/inclRegForm.jsp" flush="false">
<jsp:param value="system" name="page"/>
</jsp:include>
</div>
	
<div id="itemsHiddenArea" style="display:none;">
<div id="itemsArea">
<u:title title="" type="small" alt="항목지정" notPrint="true" >
	<li class="dropdown">
    <a id="titleSetupBtn" href="javascript:void(0);" class="sbutton button_small"  title="기능선택"><span><u:msg titleId="wf.btn.form.select.func" alt="기능선택"	/></span></a>
    <div class="dropdown-content"><a id="titleSetupBtn" href="javascript:void(0);" onclick="setLoutPop(this, 'title')" title="제목설정" style="display:none;" ><span><u:msg titleId="wf.btn.setup.title" alt="제목설정"
	/></span></a><a id="tableSetupBtn" href="javascript:void(0);" onclick="setLoutPop(this, 'table')" title="크기설정" style="display:none;" ><span><u:msg titleId="wf.btn.setup.table" alt="크기설정"
	/></span></a><a id="cellSetupBtn" href="javascript:void(0);" onclick="setCellPop(this);" title="셀설정" style="display:none;" ><span><u:msg titleId="wf.btn.cell.setup" alt="셀설정"
	/></span></a><a id="cellMergeBtn" href="javascript:void(0);" onclick="$tables.mergeAll();" title="셀합치기" style="display:none;" ><span><u:msg titleId="wf.btn.cell.merge" alt="셀합치기"
	/></span></a><a id="cellSplitWBtn" href="javascript:void(0);" onclick="$tables.split('h');" title="셀분할(가로)" style="display:none;" ><span><u:msg titleId="wf.btn.cell.splitW" alt="셀분할(가로)"
	/></span></a><a id="cellSplitHBtn" href="javascript:void(0);" onclick="$tables.split('v');" title="셀분할(세로)" style="display:none;" ><span><u:msg titleId="wf.btn.cell.splitH" alt="셀분할(세로)"
	/></span></a><a id="rowAddBtn" href="javascript:void(0);" onclick="$tables.row('insert');" title="삽입(줄)" style="display:none;" ><span><u:msg titleId="wf.btn.add.row" alt="삽입(줄)"
	/></span></a><a id="rowDelBtn" href="javascript:void(0);" onclick="$tables.row('delete');" title="삭제(줄)" style="display:none;" ><span><u:msg titleId="wf.btn.del.row" alt="삭제(줄)"
	/></span></a><a id="colAddBtn" href="javascript:void(0);" onclick="$tables.column('insert');" title="삽입(열)" style="display:none;" ><span><u:msg titleId="wf.btn.add.col" alt="삽입(열)"
	/></span></a><a id="colDelBtn" href="javascript:void(0);" onclick="$tables.column('delete');" title="삭제(열)" style="display:none;" ><span><u:msg titleId="wf.btn.del.col" alt="삭제(열)"
	/></span></a><a id="initBtn" href="javascript:void(0);" onclick="setLoutPop(this, 'lout')" title="초기화" style="display:none;" ><span><u:msg titleId="cm.btn.reset" alt="초기화"
	/></span></a><a id="delBtn" href="javascript:void(0);" onclick="delItems(this);" title="삭제"><span><u:msg titleId="cm.btn.del" alt="삭제"/></span></a>
    </div>
  </li>
	<u:titleIcon type="up" onclick="moveComponent(this,'up')" auth="A" />
	<u:titleIcon type="down" onclick="moveComponent(this,'down')" auth="A" 
	/>
</u:title>
<div id="loutsViewArea"></div>
<u:listArea id="itemsViewArea" colgroup="15%" noBottomBlank="true"></u:listArea>
<div class="blank"></div>
<div style="display:none" id="itemsDataArea"></div>
</div>

</div>
	
</u:titleArea>

<div id="tabNmArea" class="input_tab" style="display:none;"><u:listArea>
	<tr>
		<td class="body_lt">
			<table border="0" cellpadding="0" cellspacing="0">
				<tbody>
				<tr>
					<td id="tabLangTypArea">
						<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:convert srcId="${wfFormBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
							<u:set test="${status.first}" var="style" value="width:100px;" elseValue="width:100px; display:none" />
							<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
							<u:input id="tabRescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="wf.cols.tab.nm" value="${rescVa}" style="${style}"
								maxByte="120" validator="changeLangSelector('tabNmArea', id, va)" mandatory="Y" />
						</c:forEach>
						<u:input type="hidden" id="tabRescId" value="${wfFormBVo.rescId}" />
					</td>
					<td id="langTypOptions">
						<c:if test="${fn:length(_langTypCdListByCompId)>1}">
							<select id="langSelector" onchange="changeLangTypCd('tabNmArea','tabLangTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
							<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
							</c:forEach>
							</select>
						</c:if>
					</td>
				</tr>
				</tbody>
			</table>
		</td>
	</tr></u:listArea><div style="float:right;"><u:buttonS alt="적용" href="javascript:setTabNm();" titleId="cm.btn.apply"
/><u:buttonS alt="닫기" href="javascript:closeTabEvt();" titleId="cm.btn.close"/></div></div>

<u:buttonArea>	
	<u:button titleId="cm.btn.preview" alt="미리보기" onclick="previewPop();" auth="A" />
	<c:if test="${mobileEnable eq 'Y' && !empty param.formNo }"><u:button titleId="wf.btn.set.lout.mob" alt="화면구성(모바일)" onclick="setMobRegPop();" auth="A" /></c:if>
	<u:button titleId="wf.btn.hst.regFormAll" alt="전체양식보기" onclick="allRegFormPop();" auth="A" />
	<c:if test="${hstBtn==true }"><u:button titleId="wf.btn.hst.regForm" alt="이력보기" onclick="viewRegFormPop();" auth="A" /></c:if>
	<u:button titleId="wf.btn.form.download" alt="양식다운로드" onclick="formDownload();" auth="A" />
	<u:button titleId="wf.btn.form.upload" alt="양식업로드" onclick="setFormUploadPop();" auth="A" />
	<u:button titleId="cm.btn.save" alt="저장" onclick="save();" auth="A" />
	<u:button titleId="cm.btn.cancel" alt="취소" href="javascript:;" onclick="listForm();"/>		
</u:buttonArea>
	
</div>
<div id="btnHiddenArea" style="display:none;"></div>
<div id="langHiddenArea" style="display:none;" data-uniform="N"><div id="langTypListArea"><table border="0" cellpadding="0" cellspacing="0" style="width:100%;">
	<tbody>
	<tr>
		<td id="langTypArea">
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
				<u:set test="${status.first}" var="style" value="width:93%;" elseValue="width:93%; display:none" />
				<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
				<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="wf.form.attr.fields.name" style="${style}"
					maxByte="200" validator="changeLangSelector('langTypListArea', id, va)" mandatory="Y" dataList="data-required=\"Y\""/>
			</c:forEach>
		</td>
		<td id="langTypOptions">
			<c:if test="${fn:length(_langTypCdListByCompId)>1}">
				<select id="langSelector" onchange="changeLangTypCd('langTypListArea','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
				<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
				<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
				</c:forEach>
				</select>
			</c:if>
		</td>
	</tr>
	</tbody>
</table></div></div><!-- context menu Area -->
<div id="contextMnuHiddenArea" class="contextArea dropdown-content" style="display:none;"
><a href="javascript:selectTdChk(null);"><u:msg titleId="cm.check.all" alt="전체선택"/></a>
<a href="javascript:selectTdChk(null, false);"><u:msg titleId="wf.check.all.clear" alt="전체해제"/></a>
<a href="javascript:selectTdChk('select', true);"><u:msg titleId="cm.btn.choice" alt="선택"/></a>
<a href="javascript:delChkComponent();"><u:msg titleId="cm.btn.del" alt="삭제"/></a>
</div>
<u:blank />
<form id="setForm"
><u:input type="hidden" id="formNo" value="${param.formNo }"
/></form>
<div id="htmlHiddenArea" style="display:none;"></div>
