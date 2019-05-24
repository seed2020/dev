<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
// jstl 에서 스페이스 + 엔터 치환
pageContext.setAttribute("enter","\r\n");
pageContext.setAttribute("lineEnter",";"); %>
<link rel="stylesheet" href="${_cxPth}/css/formbuilder.css" type="text/css" />
<u:set var="isEdit" test="${param.page=='system' }" value="Y" elseValue="N"/>
<c:if test="${isEdit ne 'Y' }">
<script src="/js/numeral.min.js" type="text/javascript"></script>
<script src="/js/jquery-calx-2.2.7.min.js" type="text/javascript"></script>
</c:if>

<script type="text/javascript">
<!--
// -------------------
//input 데이터 가져오기
function getHiddenData(areaId, name){
	var hiddenData=$('#'+areaId).find('input[name="'+name+'"]').val();
	var data=[];
	if(hiddenData!=null){
		var arrs=hiddenData.trim().split(',');
		arrs.each(function(idx, va){
			data.push(va);
		});
	}
	return data;
}
<% // [버튼클릭] - 사용자/부서 삭제  %>
function selectListDel(containerId){
	var divArea=containerId+'Area';
	$('#'+divArea+' span#idArea').find("input[name='"+containerId+"']").val('');
	$('#'+divArea+' span#nmArea').text('');
};

//사용자 조회
function srchUserPop(areaId){
	var data=[];
	var divArea=areaId+'Area';
	var hiddenArrs=getHiddenData(divArea, areaId);
	if(hiddenArrs!=null && hiddenArrs.length>0){
		hiddenArrs.each(function(idx, va){
			data.push({userUid:va});
		});
	}
	searchUserPop({data:data, multi:true, mode:'search'}, function(arr){
		if(arr!=null){
			var txt='',va='';
			arr.each(function(index, vo){
				if(index>0){
					txt+=','; 
					va+=','; 
				}
				txt+=vo.rescNm; 
				va+=vo.userUid;
			});
			$('#'+divArea+' span#idArea').find("input[name='"+areaId+"']").val(va);
			$('#'+divArea+' span#nmArea').text(txt);
		}
	});
}
//조직도 조회
function srchOrgPop(areaId){
	var data=[];
	var divArea=areaId+'Area';
	var hiddenArrs=getHiddenData(divArea, areaId);
	if(hiddenArrs!=null && hiddenArrs.length>0){
		hiddenArrs.each(function(idx, va){
			data.push({orgId:va});
		});
	}
	searchOrgPop({data:data, multi:true, withSub:false, mode:'search'}, function(arr){
		if(arr!=null){
			var txt='',va='';
			arr.each(function(index, vo){
				if(index>0){
					txt+=','; 
					va+=','; 
				}
				txt+=vo.rescNm; 
				va+=vo.orgId;
			});
			$('#'+divArea+' span#idArea').find("input[name='"+areaId+"']").val(va);
			$('#'+divArea+' span#nmArea').text(txt);
		}
	});
}
<% // 일시 replace %>
function getDayString(date , regExp){
	return date.replace(regExp,'');
};<% // 일자 체크 - 오늘 날짜 이후로만 %>
function onChgTodayChk(date){
	var regExp = /[^0-9]/g;
	var today = getDayString(getToday(),regExp);
	var setday = getDayString(date,regExp);
	if(today > setday){
		alertMsg('cm.calendar.check.dateAI');
		return true;
	}
	return false;
}
<%// 행삭제%>
function delRow(obj){
	
	var parent=$(obj).closest('div#itemsArea');
	var table=parent.find('tbody:first');
	
	var trs=getTrList(table, obj);
	
	var calMap = new ParamMap();
	var arrs=[];
	var imgNoList=[];
	$.each(trs.list, function(idx, obj){
		arrs.push(this);
		$(this).find('div.component_list[data-coltyp="number"]').each(function(){
			if(calMap.get($(this).attr('data-id'))==null){
				calMap.put($(this).attr('data-id'), null);
			}
		});	
		$(this).find('div.component_list[data-coltyp="image"]').each(function(){
			$imgNo=$(this).find('input[name="imgNo"]').val();
			if($imgNo!=undefined && $imgNo!=''){
				imgNoList.push($imgNo);
			}
		});	
	});
	
	if(arrs.length>0){
		delRowInArr(arrs);
		removeFormula(table, calMap);
		// 계산 업데이트
		$('div.loutFormArea').calx('update');
		
		if(imgNoList.length>0){
			var delList=$('#delImgNoList').val();
			if(delList!='') delList+=',';
			delList+=imgNoList.join(',');
			$('#delImgNoList').val(delList);
		}
	}
	
	<c:if test="${isEdit eq 'Y'}">$tables.tableIndex();</c:if>
}
<%// 삭제 - 배열에 담긴 목록%>
function delRowInArr(rowArr){
	for(var i=0;i<rowArr.length;i++){
		$(rowArr[i]).remove();
	}
	<c:if test="${param.page eq 'preview'}">dialog.resize('previewFormDialog');</c:if>
}

<% // 행그룹 가져오기%>
function getTrList(table, obj){
	// 버튼을 포함하고 있는 로우
	var tr=$(obj).closest('tr#row');
	
	// 복사할 tr의 기준 인덱스
	var start=tr.index();
	// tr의 td 중에 rowspan
	var maxNo=1,rowspan;
	$(tr).children().each(function(){
		rowspan=$(this).attr('rowspan') || 1;
		if(rowspan>maxNo) maxNo=rowspan;
	});
	// 마지막 tr 인덱스
	var end=parseInt(start)+(parseInt(maxNo)-1);
	if(start<end) tr=table.find('tr#row:lt('+(parseInt(end)+1)+'):gt('+(parseInt(start)-1)+')');
	else tr=table.find('tr#row:eq('+parseInt(start)+')');
	
	/* console.log(start+'-'+end);
	var arrs=[];
	console.log('len : '+table.children().length);
	$.each(table.children(), function(){
		$index=$(this).index();
		console.log('index : '+$index);
		if(start<end){
			if($index>=start && $index<=end) arrs.push(this);
		}else if(start==end && $index==start){
			arrs.push(this);
			return false;
		}
	});
	console.log('arrs : '+arrs.length); */
	return {list:tr, start:start, end:end};
}

var maxRowMap=null;

<% // 행추가 가능여부 체크 %>
function chkAddRow(table, obj){
	var rowIndex,rowspan,tr,td,row,va;
	var rowspanList=[];
	// tr rowspan 배열
	table.children().each(function(){
		rowIndex=$(this).index();
		$(this).children().each(function(){
			if($(this).attr('rowspan')!=undefined) rowspanList.push(rowIndex+'-'+$(this).index()+'-'+$(this).attr('rowspan'));
		});
	});
	
	// 체크된 tr rowspan 배열
	tr=$(obj).closest('tr');
	rowIndex=tr.index();
	td=$(this).closest('td');
	rowspan=td.attr('rowspan') || 1;
	va=rowIndex+'-'+td.index()+'-'+rowspan;
	
	// 체크 여부
	var isChk=true;
	
	var chk=va.split('-');
	$.each(rowspanList, function(idx2, va2){
		if(va==va2) return true;
		row=va2.split('-');
		if((parseInt(chk[0])>parseInt(row[0]) && parseInt(chk[0])<=parseInt(row[0])+(parseInt(row[2])-1))){
			//alertMsg('wf.msg.not.repet'); // wf.msg.not.repet=반복할 행과 크기가 다른 행이 있어 지정할 수 없습니다.
			isChk=false;
			return isChk;
		}
	});
	
	return isChk;
}
<%// 배열요소의 중복만 제거해서 배열로 반환 %>
function uniqArr(arr) {
	var chk = [];
	for (var i = 0; i < arr.length; i++) {
		if (chk.length == 0) {
			chk.push(arr[i]);
		} else {
			var flg = true;
			for (var j = 0; j < chk.length; j++) {
				if (chk[j] == arr[i]) {
					flg = false;
					break;
				}
			}
			if (flg) {
				chk.push(arr[i]);
			}
		}
	}
	return chk;
}
<%// 복사할 html의 id 및 name 변경 %>
function replaceHtml(html, id, newId){
	var regExp = new RegExp('('+id+'|'+id+'-[0-9]{1,})(_|"|\'|Area"|Dt"|Dt\'|Tm"|Tm\'|_cdYn"|CalArea"|DtCalArea"|CalBtn"|DtCalBtn"|,|\\)|$)', "g");
	var findResult = html.match(regExp);
	if(findResult!=null){
		//console.log('id : '+id+' -- newId : '+newId);
		//findResult = findResult.reduce(function(a,b){if(a.indexOf(b)<0)a.push(b);return a;},[]);
		findResult = uniqArr(findResult); // 중복값 제거
		//console.log('findResult : '+findResult);
		var result=null;
	 	for(var i=0;i<findResult.length;i++){
	 		result=findResult[i].replace(')', '\\)');
	 		regExp = new RegExp(result, "g");
	 		//console.log('regExp : '+regExp);
	 		//console.log('findResult[i].replace(id, newId) : '+findResult[i].replace(id, newId));
			html=html.replace(regExp, findResult[i].replace(id, newId));
		}
	}
	return html;
}

<%// 복사할 html의 계산식 변경 %>
function replaceFormula(table, calMap){
	if(calMap!=null && calMap.keys.length>0){
		// 반복행을 제외한 tr의 index 추출
		var start=1, end=1, trs, regExp;
		table.find('div.component_list[data-coltyp="button"]').each(function(){
			trs=getTrList(table, this);
			if(start>trs.start) start=trs.start;
			if(end<trs.end) end=trs.end;
		});
		var formula=null, addList;		
		table.children().each(function(){
			// 반복행에 포함 안된 계산식만 변경해준다.
			if($(this).index()>=start && $(this).index()<=end) return true;
			$(this).find('input[id^="calculate"]').each(function(idx, input){
				formula=$(this).attr('data-formula');
				if(formula===undefined) return true;
				calMap.each(function(key, list){
					regExp = new RegExp('(SUM|AVG)(.*)(,|\\+|\\()('+key.toUpperCase()+')(,|\\+|\\))', "g");
					if(regExp.test(formula)){
						//if(key.toUpperCase()!=RegExp.$4) return true;
						var semi='';
						if(RegExp.$1!='') semi=',';
						if(semi=='' && RegExp.$3!='' && RegExp.$3!='(') semi=RegExp.$3;
						if(semi=='' && RegExp.$5!='' && RegExp.$5!=')') semi=RegExp.$5;
						addList=list.join(semi);
						//console.log('key : '+key+'---'+formula+ '===addList : '+addList+'++++++ : '+RegExp.$3);
						formula=formula.replace(regExp, RegExp.$1+RegExp.$2+RegExp.$3+addList+semi+RegExp.$4+RegExp.$5);
						$(input).attr('data-formula', formula);
						return true;
					}
				});
			});
		});
		
		//calMap.each(function(idx, obj2){
			//console.log(idx+'-'+obj2);
		//});
		
	}
}

<%// 삭제할 html의 계산식 변경 %>
function removeFormula(table, calMap){
	if(calMap!=null && calMap.keys.length>0){
		// 반복행을 제외한 tr의 index 추출
		var start=1, end=1, trs, regExp;
		table.find('div.component_list[data-coltyp="button"]').each(function(){
			trs=getTrList(table, this);
			if(start>trs.start) start=trs.start;
			if(end<trs.end) end=trs.end;
		});
		var formula=null;		
		table.children().each(function(){
			// 반복행에 포함 안된 계산식만 변경해준다.
			if($(this).index()>=start && $(this).index()<=end) return true;
			$(this).find('input[id^="calculate"]').each(function(idx, input){
				formula=$(this).attr('data-formula');
				if(formula===undefined) return true;
				calMap.each(function(key, list){
					regExp = new RegExp('(SUM|AVG)(.*)(,|\\+|\\()('+key.toUpperCase()+')(,|\\+|\\))', "g");
					if(regExp.test(formula)){
						var replaceData=RegExp.$4;
						var semi='';
						if(RegExp.$3!='' && RegExp.$3!='('){
							replaceData=RegExp.$1+RegExp.$2+RegExp.$5;
						}else if(RegExp.$5!='' && RegExp.$5!=')')
							replaceData=RegExp.$1+RegExp.$2+RegExp.$3;
						formula=formula.replace(regExp, replaceData);
						$(input).attr('data-formula', formula);
						return true;
					}
				});
			});
		});
		
		//calMap.each(function(idx, obj2){
			//console.log(idx+'-'+obj2);
		//});
		
	}
}

<%// 행추가%>
function addRow(obj, table){
	if(table===undefined){
		var parent=$(obj).closest('div#itemsArea');
		table=parent.find('tbody:first');	
	}
	
	// 행추가 가능한지 체크
	var isChk=chkAddRow(table, obj);
	if(!isChk){
		alertMsg('wf.msg.not.repet'); // wf.msg.not.repet=반복할 행과 크기가 다른 행이 있어 지정할 수 없습니다.
		return;
	}
	
	// 반복버튼을 클릭한 상위ID
	var parentId=$(obj).closest('div.component_list').attr('data-id');
	
	var trs=getTrList(table, obj);
	// 복사한 tr을 삽입할 대상
	var target=table.find('tr#row:eq('+trs.end+')');
	
	// 레퍼런스id 패턴
	var regExp = null;
	
	// 달력 닫기
	calendar.close();
	
	// 계산식맵
	var calMap=new ParamMap();
	
	var skips=['file', 'editor'];
	var isSkip=false;
	for(var i=0;i<skips.length;i++){
		isSkip=trs.list.find('div.component_list[data-coltyp="'+skips[i]+'"]').length>0;
		if(isSkip){
			alertMsg('wf.msg.limit.cnt.'+skips[i]); // wf.msg.limit.cnt.file=파일은 최대 1개만 추가 가능합니다.  // wf.msg.limit.cnt.editor=에디터는 최대 1개만 추가 가능합니다.
			break;
		}
	}
	
	if(isSkip) return;
	var colTyp, dataId, refId, prefixId, newId;
	
	// 반복행의 추가된 컴포넌트 이름 suffix[ex) NUMBER100001]
	var seperator='0000';
	$.each(trs.list, function(idx, obj){
		$.uniform.restore($(this).find('input, textarea, select, button')); // 복사할 대상의 uniform 해제(해제 안하고 복사 할 경우 복사한 대상이 uniform 제대로 적용이 안되는 문제)
		html=$(this)[0].outerHTML;
		$(this).find("input, textarea, select, button").uniform(); // 복사할 대상의 uniform 적용
		$htmlObj=$.parseHTML(html);
		$componentlist=$($htmlObj).find('div.component_list');
		$.each($componentlist, function(){
			colTyp=$(this).attr('data-coltyp');
			dataId=$(this).attr('data-id');
			refId=$(this).attr('data-refid');
			prefixId=(refId===undefined ? dataId : refId); 
			newId=prefixId+seperator+getComponentId(prefixId);
			//console.log('dataId : '+dataId+'-newId : '+newId);
			html=replaceHtml(html, dataId, newId); // 소문자 이름 일괄변경
			html=replaceHtml(html, dataId.toUpperCase(), newId.toUpperCase()); // 대문자 이름 일괄변경
			
			// 레퍼런스 ID 삽입
			if(refId===undefined){
				regExp = new RegExp('(<div class="component_list".*data-id="'+newId+'")', "g");
				if(regExp.test(html))
					html=html.replace(regExp, RegExp.$1 +' data-refid="'+dataId+'"');
				refId=dataId;
			}else{
				regExp = new RegExp('(<div class="component_list".*data-refid="'+newId+'")', "g");
				if(regExp.test(html))
					html=html.replace(regExp, RegExp.$1 +' data-refid="'+refId+'"');
			}
			
			if(colTyp=='number'){
				if(calMap.get(refId)==null){
					calMap.put(refId, new Array());
				}
				
				$array=calMap.get(refId);
				$array.push(newId.toUpperCase());
			}
			
			// 버튼 클릭 순서 속성 저장(양식이 변경되었을때 사용자가 입력한 반복행을 복원하기 위해서 순서를 저장)
			if(colTyp=='button'){
				regExp = new RegExp('(<div class="component_list".*data-id="'+newId+'")', "g");
				if(regExp.test(html))
					html=html.replace(regExp, RegExp.$1 +' data-parentid="'+parentId+'"');
			}
		});
		target.after(html);
		target=target.next();
		
		// 추가된 행의 컴포넌트 value 초기화
		$(target).find(':text, input[type="hidden"]:not([name$="cdYn"])').val('');
		$(target).find('select option:eq(0)').attr("selected", "selected");
		$(target).find(':checkbox').prop('checked', false);
		$(target).find('textarea, span#nmArea').text('');
		
		$(target).find('img#profileImg').attr('src', '/images/blue/photo_noimg.png') // 이미지 초기화
		
		target.find('a#delRowBtn').show();
		
		target.find('a#setImgBtn').removeAttr('data-imgno');
		target.find('a#delImgBtn').removeAttr('data-imgno');
		
		$(target).find('input, textarea, select, button').uniform();
	});
	
	// 계산식 변경
	replaceFormula(table, calMap);
	
	// 계산 업데이트
	$('div.loutFormArea').calx('update');
	
	<c:if test="${param.page eq 'preview'}">dialog.resize('previewFormDialog');</c:if>
	
	return target;
}

<c:if test="${isEdit ne 'Y'}">

var componentIdMap=new ParamMap();
function setComponentId(id, va){ // 컴포넌트 ID 세팅
	var attrId = componentIdMap.get(id);
	if(attrId==null){
		componentIdMap.put(id, va);
	}
}
function getComponentId(id){ // 컴포넌트 ID
	var attrId = componentIdMap.get(id);
	if(attrId==null) attrId=1;
	componentIdMap.put(id, parseInt(attrId)+1);
	return attrId;
}
function getComponentMaxId(){ // 컴포넌트 ID최대값
	var returnId={};
	componentIdMap.each(function(id, va){
		returnId[id]=va;
	});
	
	return returnId;
}
var imgObj=null;
<% //이미지 변경 - 팝업 오픈 %>
function setImagePop(obj, params){
	imgObj=obj;
	var url='./setImagePop.do?menuId=${menuId}&formNo=${param.formNo}&workNo=${param.workNo}${openParams}';
	var colmNm=null;
	if(params!=undefined){
		colmNm=params['refNm'].replace(/\[|\]/g,'');
	}
	if(colmNm!=null && colmNm!='') url+='&colmNm='+colmNm;
	var id=$(obj).attr('data-imgno');
	if(id!=undefined) url+='&imgNo='+id;
	<c:if test="${!empty param.previewYn}">url+='&previewYn=${param.previewYn}';</c:if>
	parent.dialog.open('setImageDialog','<u:msg titleId="st.jsp.select.img" alt="이미지 선택" />', url);
}<% //이미지 상세보기 - 팝업 오픈 %>
function viewImagePop(id){
	var url='./viewImagePop.do?menuId=${menuId}&formNo=${param.formNo}&workNo=${param.workNo}';
	if(id!='') url+='&imgNo='+id;	
	parent.dialog.open('viewImageDialog','<u:msg titleId="st.cols.img" alt="이미지" />', url);
}<% //이미지 ID 세팅 %>
function setTmpImgId(id, params){
	if(imgObj==null) return;
	$div = $(imgObj).closest('div.image_profile');
	$div.find('input[name="imgData"]').val('');
	if(id==null){
		$div.find('input[name^="image"]').eq(0).val('');
		return;
	}
	$div.find('input[type="hidden"]').eq(0).val(id);
	previewImage($div, id, params);
}<% //이미지 삭제 %>
function delImg(obj){
	var target = $(obj).closest('div.image_profile');
	var id=$(obj).attr('data-imgno');
	var imgObj=target.find('img#profileImg');
	if(id===undefined || id=='') {
		target.find('input[name^="image"]').eq(0).val('');
		imgObj.attr("src", '/images/blue/photo_noimg.png');
		return;
	}
	callAjax('./transImgDelAjx.do?menuId=${menuId}', {formNo:'${param.formNo}', workNo:'${param.workNo}', imgNo:id}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if(data.result == 'ok') {			
			target.find('input[name^="image"]').eq(0).val('');
			target.find('input[name="imgData"]').val('');
			imgObj.attr("src", '/images/blue/photo_noimg.png');
		}
	});
}<% //이미지 미리보기 %>
function previewImage(target, tmpFileId, params){
	var jsonParams=JSON.parse(params);
	$img=target.find('img').eq(0);
	$img.attr("src", '/cm/viewTmpImage.do?tmpFileId='+tmpFileId);
	var viewSizeTyp = $(target).attr('data-viewSizeTyp') || 'px';
	var maxWdth = $(target).attr('data-maxwidth') || 100;
	var maxHght = $(target).attr('data-maxheight') || 100;
	$img.removeAttr('width');
	$img.removeAttr('height');
	if(viewSizeTyp=='per'){
		if(maxWdth < maxHght) $img.attr('height', maxHght+'%');
		else $img.attr('width', maxWdth+'%');			
	}else{
		var wdth=jsonParams['wdth'];
		var hght=jsonParams['hght'];
		var width = wdth <= maxWdth ? wdth : maxWdth;
		var height = hght <= maxHght ? hght : maxHght;
		if(wdth < hght) $img.attr('height', height);
		else $img.attr('width', width);
	}
	
}
<%//반복행의 초기화 %>
function setInitRepeat(){
	var parent, obj, initRepetCnt, i, refId, dataId, itemCnt;
	$('div.component_list[data-coltyp="button"]').each(function(){
		//refId=$(this).attr('data-refid');
		//if(refId!=undefined) return true;
		obj=$(this).find('a#addRowBtn');
		initRepetCnt=$(obj).attr('data-initrepetcnt');
		if(initRepetCnt===undefined) return true;
		//dataId=$(this).attr('data-id');
		//console.log('dataId : '+dataId);
		//itemCnt=$('div.component_list[data-coltyp="button"][data-refid="'+dataId+'"]').length;
		parent=$(obj).closest('div#itemsArea');
		table=parent.find('tbody:first');	
		//console.log('itemCnt : '+itemCnt);
		// 행추가 가능한지 체크
		var isChk=chkAddRow(table, obj);
		if(!isChk){
			return true;
		}
		//if(initRepetCnt>itemCnt){
			for(i=0;i<(parseInt(initRepetCnt)-1);i++){
				addRow(obj, table);
			}	
		//}
		
	});
}<% // 반복행의 반복 갯수 변경(양식이 변경되었을 경우 기존 반복행의 데이터를 신규 양식에 반영)  %>
function setRepetInit(jsonVa){
	if(jsonVa['repetData']!=undefined){
		setCdListMap(); // 코드 목록맵 세팅
		setImgListMap(); // 이미지 목록맵 세팅
		var obj, rowLen, i, tbody, row, start, end, tr, target, btnList, firstBtn, isChk, itemCnt, areaId='formArea';
		$.each(jsonVa['repetData'], function(key, va){
			rowLen=va.length;
			if(rowLen==0) return true;
			btnList=$('div.component_list[data-coltyp="button"][data-id="'+key+'"]');
			if(btnList.length==0) return true;
			firstBtn=btnList.eq(0);
			obj=$(firstBtn).find('a#addRowBtn');
			tbody=$(obj).closest('tbody');
			// 행추가 가능한지 체크
			isChk=chkAddRow($(tbody), obj);
			if(!isChk) return true;
			
			// 순차적으로 줄 추가
			$.each(btnList, function(keyIdx){
				itemCnt=$('div.component_list[data-coltyp="button"][data-refid="'+key+'"]').length;
				//console.log('rowLen :'+rowLen+' itemCnt : '+itemCnt);
				if(rowLen>itemCnt){
					for(i=0;i<(parseInt(rowLen-itemCnt));i++){
						obj = addRow(obj);
					}
				}
			});
			$.each(va, function(idx, json){
				if(json['row']===undefined) return true;
				row=json['row'].split('-')[0];
				start=row[0];
				end=row[1];
				//console.log('start :'+start+' end : '+end);
				if(start<end) tr=tbody.find('tr#row:lt('+(parseInt(end)+1)+'):gt('+(parseInt(start)-1)+')');
				else tr=tbody.find('tr#row:eq('+parseInt(start)+')');
				
				$.each(json, function(name, value){
					if(cdListMap!=null && (name.startsWith('user') || name.startsWith('dept'))){
						$('div.component_list[data-id="'+name+'"] span#nmArea').text(cdListMap.get(name).title);
					}else if(name.startsWith('image')){
						target=$('div.component_list[data-id="'+name+'"] div.image_profile').eq(0);
						setRepetImage(target, value, name);
						return true;
					}
					$('#'+areaId).find('input[name="'+name+'"], select[name="'+name+'"], textarea[name="'+name+'"]').val(value);
					$('#'+areaId).find(':radio[name="'+name+'"][value="'+value+'"]').prop('checked', true);
					$('#'+areaId).find(':checkbox[name="'+name+'"]').val(value.split(','));
					$('#'+areaId).find('textarea[name="'+name+'"]').text(value);
					if($('#'+areaId).find('select[name="'+name+'"]').length>0 && $('#'+areaId).find('select[name="'+name+'"]').attr('onchange')){
						$('#'+areaId).find('select[name="'+name+'"]').trigger('change');
					}
				});
			});
		});
	}
}
<% // 반복행의 컴포넌트ID 최대값 초기화 %>
function setMaxIdInit(jsonVa){
	if(jsonVa['maxId']!=undefined){
		$.each(jsonVa['maxId'], function(key, va){
			setComponentId(key, va);
		});
	}
}
</c:if>
<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
}

<% // json string replace %>
function replaceJsonString(va){
	if(va===undefined || va=='') return '';
	va = va.replaceAll('\r\n', '\\r\\n');
	va = va.replace(/\\n/g, "\\n")
	.replace(/\\'/g, "\\'")
	.replace(/\\"/g, '\\"')
	.replace(/\\&/g, "\\&")
	.replace(/\\r/g, "\\r")
	.replace(/\\t/g, "\\t")
	.replace(/\\b/g, "\\b")
	.replace(/\\f/g, "\\f");
	return va;
}

$(document).ready(function() {	
	<%// 다음 Row 번호 %>
	maxRowMap=new ParamMap();
	<c:if test="${isEdit ne 'Y'}">
	$('div.loutFormArea').calx(); // 계산식 적용
	<c:if test="${empty wfWorksLVoMap || (!empty notGenId && notGenId==true)}">setInitRepeat();</c:if> // 초기 반복행수 적용
	
	<c:if test="${(!empty wfWorksLVoMap && !empty wfWorksLVoMap.attrVa) || (!empty wfMdFormDVo && !empty wfMdFormDVo.attrVa)}">
	// 컴포넌트ID 값 세팅
	var attrVa=null;
	
	<c:if test="${!empty wfMdFormDVo && !empty wfMdFormDVo.attrVa}">attrVa='<u:out value="${wfMdFormDVo.attrVa}" type="script"/>';</c:if>
	if(attrVa==null) attrVa='<u:out value="${wfWorksLVoMap.attrVa}" type="script"/>';
	
	if(attrVa!=null){
		attrVa=replaceJsonString(attrVa);
		//attrVa=attrVa.replaceAll('\r\n', '\\r\\n');
		var jsonVa=JSON.parse(attrVa);
		<c:if test="${empty notGenId || notGenId==false }">setMaxIdInit(jsonVa);</c:if>
		// 양식이 변경되었을 경우 기존 반복행의 데이터를 신규 양식에 반영
		<c:if test="${!empty notGenId && notGenId==true }">setRepetInit(jsonVa);</c:if>
	}
	
	</c:if>
	
	</c:if>
	
});
//-->
</script>
<c:set var="textMaxByte" value="2000"/>
<c:set var="dataSeq" value="0" />
<u:set var="readonly" test="${isEdit eq 'Y' }" value="N" elseValue="N"/>
<u:set var="loutList" test="${empty notGenId && !empty wfWorksLVoMap && !empty wfWorksLVoMap.loutVa}" value="${wfFormRegDVo.loutVa }" elseValue="${wfFormRegDVo.loutVa }"/>
<c:if test="${!empty loutList }">
<c:if test="${empty notGenId && !empty wfWorksLVoMap && !empty wfWorksLVoMap.loutVa}">
<u:convertJson var="userJsonLoutList" value="${wfWorksLVoMap.loutVa }"
/></c:if>
<c:if test="${param.page eq 'user' && !empty wfWorksLVoMap}"><u:convertJson var="dataToJson" value="${wfWorksLVoMap.jsonVa }"/></c:if>
<c:if test="${param.page eq 'user' && empty wfWorksLVoMap && !empty wfMdFormDVo}"><u:convertJson var="dataToJson" value="${wfMdFormDVo.jsonVa }"/></c:if>
<u:convertJson var="jsonLoutList" value="${loutList }"
/><u:convertJson var="jsonVa" value="${wfFormRegDVo.attrVa }" 
/><u:input type="hidden" id="tabYn" value="${empty jsonVa['tabYn'] ? 'Y' : jsonVa['tabYn']}"
/><c:forEach items="${jsonLoutList}" var="json" varStatus="status">
<c:if test="${!empty userJsonLoutList}"><c:set var="userJsonData" value="${userJsonLoutList[status.index] }"/></c:if>
<div class="loutFormArea" id="${param.loutPrefix}${idPrefix }${json['loutId'] }" <c:if test="${status.index>0 }">style="display:none;"</c:if>>
<c:forEach items="${json['list']}" var="list" varStatus="listStatus"><c:set var="dataSeq" value="${dataSeq+1 }"/>
<c:if test="${!empty userJsonData}"><c:set var="userJsonList" value="${userJsonData['list'][listStatus.index] }"/></c:if>
<u:set var="itemStyles" test="${!empty list['itemStyles'] }" value=" style=\"max-width:${list['itemStyles']['maxWidth'] }\""/>
<div id="itemsArea" data-id="${list['type'] }" data-seq="${dataSeq }"<c:if test="${list['type'] eq 'table' }"> data-loutTyp="${list['loutTyp'] }"</c:if>${itemStyles }><!-- itemsArea -->

<c:if test="${param.page=='system' }">
<u:set test="${(list['type'] ne 'line' && list['type'] ne 'gap' && list['type'] ne 'label') && !empty list['titles'] && list['titles']['rescUseYn'] eq 'Y' }" var="titleKey" value="rescVa_${_lang}" elseValue=""/>
<u:title title="${!empty titleKey ? list['titles'][titleKey] : ''}" type="small" alt="${!empty titleKey ? list['titles'][titleKey] : ''}" notPrint="true" >

	<li class="dropdown">
    <a id="titleSetupBtn" href="javascript:void(0);" class="sbutton button_small"  title="기능선택"><span><u:msg titleId="wf.btn.form.select.func" alt="기능선택"
	/></span></a>
    <div class="dropdown-content">
      <c:if test="${list['type'] ne 'line' && list['type'] ne 'gap' && list['type'] ne 'label' }"
	><a id="titleSetupBtn" href="javascript:void(0);" onclick="setLoutPop(this, 'title')" title="제목설정"><span><u:msg titleId="wf.btn.setup.title" alt="제목설정"
	/></span></a></c:if><c:if test="${list['type'] eq 'table' && !empty list['loutTyp'] }"
	><a id="tableSetupBtn" href="javascript:void(0);" onclick="setLoutPop(this, 'table')" title="크기설정"><span><u:msg titleId="wf.btn.setup.table" alt="크기설정"
	/></span></a><c:if test="${list['loutTyp'] ne 'D'}"><a id="cellSetupBtn" href="javascript:void(0);" onclick="setCellPop(this);" title="셀설정"><span><u:msg titleId="wf.btn.cell.setup" alt="셀설정"
	/></span></a><a id="cellMergeBtn" href="javascript:void(0);" onclick="$tables.mergeAll();" title="셀합치기"><span><u:msg titleId="wf.btn.cell.merge" alt="셀합치기"
	/></span></a><a id="cellSplitWBtn" href="javascript:void(0);" onclick="$tables.split('h');" title="셀분할(가로)"><span><u:msg titleId="wf.btn.cell.splitW" alt="셀분할(가로)"
	/></span></a><a id="cellSplitHBtn" href="javascript:void(0);" onclick="$tables.split('v');" title="셀분할(세로)"><span><u:msg titleId="wf.btn.cell.splitH" alt="셀분할(세로)"
	/></span></a><a id="rowAddBtn" href="javascript:void(0);" onclick="$tables.row('insert');" title="삽입(줄)"><span><u:msg titleId="wf.btn.add.row" alt="삽입(줄)"
	/></span></a><a id="rowDelBtn" href="javascript:void(0);" onclick="$tables.row('delete');" title="삭제(줄)"><span><u:msg titleId="wf.btn.del.row" alt="삭제(줄)"
	/></span></a><a id="colAddBtn" href="javascript:void(0);" onclick="$tables.column('insert');" title="삽입(열)"><span><u:msg titleId="wf.btn.add.col" alt="삽입(열)"
	/></span></a><a id="colDelBtn" href="javascript:void(0);" onclick="$tables.column('delete');" title="삭제(열)"><span><u:msg titleId="wf.btn.del.col" alt="삭제(열)"
	/></span></a></c:if><a id="initBtn" href="javascript:void(0);" onclick="setLoutPop(this, 'lout')" title="초기화"><span><u:msg titleId="cm.btn.reset" alt="초기화"
	/></span></a></c:if><a id="delBtn" href="javascript:void(0);" onclick="delItems(this);" title="삭제"><span><u:msg titleId="cm.btn.del" alt="삭제"/></span></a>
    </div>
  </li>
	<u:titleIcon type="up" onclick="moveComponent(this,'up')" auth="A" />
	<u:titleIcon type="down" onclick="moveComponent(this,'down')" auth="A" 
	/>
</u:title>
<div id="mnuArea" style="position:absolute;width:100%;"></div>
</c:if>

<u:set test="${(list['type'] ne 'line' && list['type'] ne 'gap' ) && !empty list['titles'] && list['titles']['rescUseYn'] eq 'Y' }" var="titleKey" value="rescVa_${_lang}" elseValue=""/>
<c:if test="${param.page ne 'system' && !empty titleKey}"><u:title title="${list['titles'][titleKey] }" type="small" alt="${list['titles'][titleKey] }" notPrint="true" /></c:if>
<c:if test="${list['type'] eq 'table' }">
<div class="listarea" id="itemsViewArea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" id="${list['tblId'] }" style="table-layout:fixed;">
	<c:if test="${!empty list['colgroup'] }">
	<colgroup>
		<c:forEach items="${list['colgroup']}" var="col" varStatus="colStatus"><col width="${col }"></c:forEach>
	</colgroup>
	</c:if>
	<tbody style="border:0">
	<c:set var="rowList" value="${!empty userJsonList ? userJsonList['row'] : list['row'] }"/>
	<c:if test="${!empty rowList }">
		<c:forEach items="${rowList}" var="row" varStatus="rowStatus">
			<tr id="${row['id'] }" <c:if test="${!empty row['groupId'] }"> data-groupId="${row['groupId'] }"</c:if
			><c:if test="${row['id']=='hiddenTr'}"> style="display:none;"</c:if
			><c:if test="${!empty row['dataRepet']}"> data-repet="${row['dataRepet'] }"</c:if
			>><c:if test="${!empty row['cell'] }">
					<c:forEach items="${row['cell']}" var="cell" varStatus="cellStatus">
						<u:set var="cellClsNms" test="${param.page ne 'system' }" value="${fn:replace(cell['clsnm'], ' cell_repet','') }" elseValue="${cell['clsnm'] }"/>
						<td class="${cellClsNms }"<c:if test="${!empty cell['colspan'] && cell['colspan']>1}"> colspan="${cell['colspan'] }"</c:if
						><c:if test="${!empty cell['rowspan'] && cell['rowspan']>1}"> rowspan="${cell['rowspan'] }"</c:if
						><c:if test="${isEdit eq 'Y' && fn:indexOf(cellClsNms, 'head')==-1}"> data-drag="Y"</c:if
						><c:if test="${isEdit eq 'Y' }"> data-colTyp="${cell['colTyp'] }" onclick="setProp(this, event);"</c:if>
						><c:if test="${isEdit eq 'Y' && fn:contains(cell['clsnm'], 'cell_fix') }"><div class="check"><input type="checkbox" onclick="cellChk(this, event);"/></div></c:if>
							<c:if test="${!empty cell['components'] }">
								<c:forEach items="${cell['components']}" var="component" varStatus="componentStatus">									
									<c:set var="componentId" value="${component['id']}"/>
									<u:set var="dataVa" test="${param.page eq 'user' }" value="${dataToJson[componentId] }" elseValue=""/>
									<c:set var="jsonMap" value="${!empty component['refId'] ? jsonVa[component['refId']] : jsonVa[componentId] }" scope="request"
									/><u:set var="emptyLangId" test="${component['coltyp'] eq 'label' }" value="label" elseValue="name"
									/><c:set var="langTitleId" value="${emptyLangId }RescVa_${_lang }" scope="request"
									/><u:set var="langTitle" test="${!empty jsonMap[langTitleId] }" value="${jsonMap[langTitleId] }" elseValue="${jsonMap[emptyLangId] }"
									/><u:set var="maxByte" test="${!empty jsonMap['max'] }" value="${jsonMap['max'] }" elseValue="${textMaxByte }"
									/><u:convertJsonString var="dataJson" value="${jsonMap }"
									/><u:out var="dataJson" value="${dataJson }" type="value"/>
									<c:if test="${param.page eq 'user' && !empty cdListMap && fn:contains('user,dept,select,radio,check', component['coltyp'])}"><c:set var="cdListArray" value="${cdListMap[componentId] }"/></c:if>
									<u:set var="useCombClass" test="${!empty jsonMap['useCombYn'] }" value=" component_comb" elseValue=""
									/><c:set var="componentStyle"/><!-- 컴포넌트 스타일 -->
									<c:if test="${!empty jsonMap['fontFamilies'] && jsonMap['fontFamilies'] ne 'none'}"><c:set var="componentStyle" value="${componentStyle}font-family:${jsonMap['fontFamilies']};"
									/></c:if><c:if test="${!empty jsonMap['fontSizes']}"><c:set var="componentStyle" value="${componentStyle}font-size:${jsonMap['fontSizes']};"
									/></c:if><c:if test="${!empty jsonMap['fontWeight']}"><c:set var="componentStyle" value="${componentStyle}font-weight:${jsonMap['fontWeight']};"
									/></c:if><c:if test="${!empty jsonMap['fontStyle']}"><c:set var="componentStyle" value="${componentStyle}font-style:${jsonMap['fontStyle']};"
									/></c:if><c:if test="${!empty jsonMap['textDecoration']}"><c:set var="componentStyle" value="${componentStyle}text-decoration:${jsonMap['textDecoration']};"
									/></c:if><c:if test="${!empty jsonMap['fontColor']}"><c:set var="componentStyle" value="${componentStyle}color:${jsonMap['fontColor']};"
									/></c:if><c:if test="${!empty componentStyle }"><c:set var="componentStyle" value=" style=\"${componentStyle }\""
									/></c:if><c:set var="langHelptextId" value="helptextRescVa_${_lang }" scope="request"
									/><u:set var="helptext" test="${!empty jsonMap[langHelptextId] }" value="${jsonMap[langHelptextId] }" elseValue="${jsonMap['helptext'] }"
									/><div class="component_list${isEdit eq 'Y' ? ' component_area' : '' }${useCombClass}" id="${componentId }Area" data-id="${componentId }" data-coltyp="${component['coltyp'] }" 
									<c:if test="${isEdit ne 'Y' && !empty helptext }"> data-tooltip-text="${helptext }"</c:if
									><c:if test="${isEdit eq 'Y' }"> data-name="${langTitle }" data-tooltip-text="${componentId }" onclick="setProp(this, event);" data-json='${dataJson }'</c:if
									><c:if test="${isEdit eq 'Y' && !empty jsonMap['singleId']}"> data-singleid='${jsonMap['singleId'] }'</c:if
									><c:if test="${isEdit eq 'Y' && !empty wfFormColmLVoMap}"> data-colmId='${wfFormColmLVoMap[componentId] }'</c:if
									><c:if test="${isEdit ne 'Y' && !empty component['refId']}"> data-refid='${component['refId'] }'</c:if
									>${componentStyle }><c:if test="${isEdit eq 'Y' && component['coltyp'] eq 'number' }"><div class="tooltip_area" style="display:none;">${fn:toUpperCase(componentId) }</div></c:if>
									<div<c:if test="${isEdit eq 'Y' }"> class="noSelect"</c:if><c:if test="${component['coltyp'] eq 'label' && !empty jsonMap['labelAlign'] }"> style="text-align:${jsonMap['labelAlign'] }"</c:if>>
									<c:if test="${component['coltyp'] eq 'label' }"
									><c:if test="${!empty jsonMap['required'] && jsonMap['required']=='Y' }"><u:mandatory 
									/></c:if><label for="${componentId }" class="header"
									>${langTitle }</label></c:if
									><c:if test="${component['coltyp'] eq 'button' }"
									><div style="margin-top:2px;"><u:set var="btnExtData" test="${!empty jsonMap['initRepetCnt'] && jsonMap['initRepetCnt']>1}" value="initRepetCnt=${jsonMap['initRepetCnt'] }" elseValue=""
									/><u:buttonIcon titleId="cm.btn.plus" alt="행추가" onclick="${isEdit ne 'Y' ? 'addRow(this)' : 'return false' };" id="addRowBtn" extData="${btnExtData }"
									/><u:buttonIcon id="delRowBtn" titleId="cm.btn.minus" alt="행삭제" onclick="${isEdit ne 'Y' ? 'delRow(this)' : 'return false' };"  style="${isEdit ne 'Y' && empty component['refId'] ? 'display:none;' : ''}"
									/></div></c:if
									><c:if test="${component['coltyp'] eq 'text' }"
									><u:set var="wdthStyle" test="${!empty jsonMap['wdth'] }" value="width:${jsonMap['wdth'] }${jsonMap['wdthTyp'] };" elseValue="width:90%;"
									/><u:set var="validator" test="${!empty jsonMap['validatorOpt'] }" value="checkValidator('${jsonMap['validatorOpt'] }', inputTitle, va)" elseValue=""
									/><c:set var="langPlaceholderId" value="placeholderRescVa_${_lang }" scope="request"
									/><u:set var="placeholder" test="${!empty jsonMap[langPlaceholderId] }" value="${jsonMap[langPlaceholderId] }" elseValue="${jsonMap['placeholder'] }"
									/><u:input id="${idPrefix }${componentId }" title="${langTitle }" value="${!empty dataVa ? dataVa : jsonMap['startText'] }" style="${wdthStyle }" placeholder="${placeholder }" maxByte="${maxByte }" minByte="${jsonMap['min'] }"
									mandatory="${empty formEdit ? jsonMap['required'] : ''}" valueAllowed="${jsonMap['allow'] }" valueNotAllowed="${jsonMap['notAllow'] }" valueOption="${jsonMap['allowList'] }" readonly="${readonly }" validator="${validator }" 
									onblur="${!empty jsonMap['validatorOpt'] && jsonMap['validatorOpt'] eq 'TEL' ? 'fnPhoneInput(this);' : '' }" onfocus="${!empty jsonMap['validatorOpt'] && jsonMap['validatorOpt'] eq 'TEL' ? 'fnPhoneUnInput(this);' : '' }"
									/></c:if><c:if test="${component['coltyp'] eq 'textarea' }"
									><u:set var="wdthStyle" test="${!empty jsonMap['wdth'] }" value="width:${jsonMap['wdth'] }${jsonMap['wdthTyp'] };" elseValue="width:95.5%;"
									/><u:set var="heightStyle" test="${!empty jsonMap['height'] && jsonMap['heightTyp'] eq 'px'}" value="height:${jsonMap['height'] }px;" elseValue=""
									/><u:set var="rows" test="${!empty jsonMap['height'] && jsonMap['heightTyp'] eq 'row'}" value="${jsonMap['height'] }" elseValue=""
									/><c:set var="langPlaceholderId" value="placeholderRescVa_${_lang }" scope="request"
									/><u:set var="placeholder" test="${!empty jsonMap[langPlaceholderId] }" value="${jsonMap[langPlaceholderId] }" elseValue="${jsonMap['placeholder'] }"
									/><u:textarea id="${idPrefix }${componentId }" title="${langTitle }" value="${!empty dataVa ? dataVa : jsonMap['startText'] }" style="${wdthStyle }${heightStyle }" placeholder="${placeholder }" maxByte="${maxByte }" minByte="${jsonMap['min'] }"
									mandatory="${empty formEdit ? jsonMap['required'] : ''}" valueAllowed="${jsonMap['allow'] }" valueNotAllowed="${jsonMap['notAllow'] }" valueOption="${jsonMap['allowList'] }" readonly="${readonly }" rows="${rows }"
									/></c:if><c:if test="${component['coltyp'] eq 'addr' }"
									><u:convertMap var="addrVa1" srcId="dataToJson" attId="${componentId }_1" 
									/><u:convertMap var="addrVa2" srcId="dataToJson" attId="${componentId }_2" 
									/><u:address id="${idPrefix }${componentId }" zipNoName="${idPrefix }${componentId }_1" adrName="${idPrefix }${componentId }_2" adrTitle="${langTitle }" zipNoTitle="${langTitle }" alt="${langTitle }" adrStyle="width:94%" zipNoValue="${addrVa1 }" adrValue="${addrVa2 }" readonly="Y"
									/></c:if><c:if test="${component['coltyp'] eq 'tel' }"
									><c:if test="${!empty param.workNo && empty dataVa}"
									><c:forEach var="telNo" begin="1" end="3" step="1"
									><c:set var="telId" value="${componentId }${telNo }"
									/><c:if test="${!empty dataToJson[telId] }"><c:set var="dataVa" value="${dataVa }${dataToJson[telId] }"
									/></c:if></c:forEach></c:if><u:phone id="${idPrefix }${componentId }" value="${dataVa}" title="${langTitle }" mandatory="${empty formEdit ? jsonMap['required'] : ''}"
									/></c:if><c:if test="${component['coltyp'] eq 'editor' }"
									><c:if test="${param.page eq 'user' }"
									><div id="editorArea" data-name="${langTitle }"><c:if test="${isOnlyMd==true}"><c:set var="editorAreaId" value="${componentId }LoutArea"/><div id="${componentId }LoutArea" style="height:400px;"></div></c:if
									><u:editor id="${componentId }" width="100%" height="400px" module="wf" areaId="${editorAreaId }" value="${!empty dataVa ? dataVa : jsonMap['startText']}" 
									/></div></c:if><c:if test="${param.page ne 'user'  }"
									><div class="editor2" data-colTyp="editor" id="${idPrefix }${componentId }"><div class="editor3"><img src="${_ctx}/images/etc/editor1.png"/></div></div></c:if
									></c:if><c:if test="${component['coltyp'] eq 'date' }"
									><u:set var="option" test="${!empty jsonMap['allowAfterRegDt'] && jsonMap['allowAfterRegDt']=='Y' }" value="{checkHandler:onChgTodayChk}" elseValue="null"
									/><c:set var="langPlaceholderId" value="placeholderRescVa_${_lang }" scope="request"
									/><u:set var="placeholder" test="${!empty jsonMap[langPlaceholderId] }" value="${jsonMap[langPlaceholderId] }" elseValue="${jsonMap['placeholder'] }"
									/><u:calendartime id="${idPrefix }${componentId }" title="${langTitle }" value="${!empty dataVa ? dataVa : !empty jsonMap['startText'] ? jsonMap['startText'] : !empty jsonMap['defaultRegDt'] ? 'today' : ''}" type="calendar" option="${option }" mandatory="${empty formEdit ? jsonMap['required'] : ''}" placeholder="${placeholder }"
									/></c:if><c:if test="${component['coltyp'] eq 'period' }"
									><ul class="attr_list"><li><u:convertMap var="periodVa1" srcId="dataToJson" attId="${componentId }_1" 
									/><u:calendartime id="${idPrefix }${componentId }_1" title="${langTitle }" value="${!empty periodVa1 ? periodVa1 : !empty jsonMap['startText1'] ? jsonMap['startText1'] : !empty jsonMap['defaultRegDt'] ? 'today' : ''}" type="calendar" mandatory="${empty formEdit ? jsonMap['required'] : ''}"
									/></li><li>-</li><li><u:convertMap var="periodVa2" srcId="dataToJson" attId="${componentId }_2" 
									/><u:calendartime id="${idPrefix }${componentId }_2" title="${langTitle }" value="${!empty periodVa2 ? periodVa2 : !empty jsonMap['startText2'] ? jsonMap['startText2'] : ''}" type="calendar" 
									/></li></c:if><c:if test="${component['coltyp'] eq 'time' }"
									><c:set var="langPlaceholderId" value="placeholderRescVa_${_lang }" scope="request"
									/><u:set var="placeholder" test="${!empty jsonMap[langPlaceholderId] }" value="${jsonMap[langPlaceholderId] }" elseValue="${jsonMap['placeholder'] }"
									/><u:calendartime id="${idPrefix }${componentId }" title="${langTitle }" value="${!empty dataVa ? dataVa : !empty jsonMap['startText'] ? jsonMap['startText'] : !empty jsonMap['defaultRegDt'] ? 'today' : ''}" type="time" mandatory="${empty formEdit ? jsonMap['required'] : ''}" placeholder="${placeholder }"
									/></c:if><c:if test="${component['coltyp'] eq 'datetime' }"
									><u:set var="value" test="${!empty jsonMap['startDate'] && !empty jsonMap['startTime'] }" value="${jsonMap['startDate'] } ${jsonMap['startTime'] }" elseValue="${!empty jsonMap['defaultRegDt'] ? 'today' : '' }"
									/><c:set var="langPlaceholderId" value="placeholderRescVa_${_lang }" scope="request"
									/><u:set var="placeholder" test="${!empty jsonMap[langPlaceholderId] }" value="${jsonMap[langPlaceholderId] }" elseValue="${jsonMap['placeholder'] }"
									/><u:calendartime id="${idPrefix }${componentId }" title="${langTitle }" value="${!empty dataVa ? dataVa : !empty value ? value : !empty jsonMap['defaultRegDt'] ? 'today' : ''}" type="calendartime" mandatory="${empty formEdit ? jsonMap['required'] : ''}" placeholder="${placeholder }"
									/></c:if><c:if test="${component['coltyp'] eq 'number' || component['coltyp'] eq 'calculate'}"
									><u:set var="wdthStyle" test="${!empty jsonMap['wdth'] }" value="width:${jsonMap['wdth'] }${jsonMap['wdthTyp'] };" elseValue="width:90%;"
									/><u:set var="readonly" test="${empty readonly && component['coltyp'] eq 'calculate' }" value="Y" elseValue="${readonly }"
									/><c:set var="dataCell" value="data-cell=\"${fn:toUpperCase(componentId) }\""
									/><u:set var="dataFormula"  test="${component['coltyp'] eq 'calculate' && (!empty jsonMap['formula'] || !empty component['formula'])}" value=" data-formula=\"${!empty component['formula'] ? component['formula'] : jsonMap['formula'] }\"" elseValue=""
									/><c:if test="${!empty jsonMap['decimal'] }"><fmt:formatNumber var="decimalSuffix" minIntegerDigits="${jsonMap['decimal'] }" value="0" type="number"
									/></c:if><u:set var="decimal" test="${!empty decimalSuffix }" value="[.]${decimalSuffix }" elseValue=""
									/><u:set var="prefixAffix" test="${!empty jsonMap['affix'] && jsonMap['affixTyp'] eq 'prefix' }" value="${jsonMap['affix'] } " elseValue=""
									/><u:set var="suffixAffix" test="${!empty jsonMap['affix'] && jsonMap['affixTyp'] eq 'suffix'}" value=" ${jsonMap['affix'] }" elseValue=""
									/><u:set var="comma" test="${!empty jsonMap['comma'] }" value=",0" elseValue=""
									/><c:set var="dataFormat" value=" data-format=\"${prefixAffix }0${comma}${decimal}${suffixAffix}\""
									/><c:set var="langPlaceholderId" value="placeholderRescVa_${_lang }" scope="request"
									/><u:set var="placeholder" test="${!empty jsonMap[langPlaceholderId] }" value="${jsonMap[langPlaceholderId] }" elseValue="${jsonMap['placeholder'] }"
									/><u:input id="${idPrefix }${componentId }" title="${langTitle }" value="${!empty dataVa ? dataVa : jsonMap['startText'] }" style="${wdthStyle }text-align:right;" placeholder="${placeholder }" maxByte="${maxByte }" minByte="${jsonMap['min'] }"
									mandatory="${empty formEdit ? jsonMap['required'] : ''}" valueOption="number" commify="${jsonMap['comma'] }" readonly="${readonly }" dataList="${dataCell }${dataFormat}${dataFormula }"
									/></c:if><c:if test="${component['coltyp'] eq 'user' }"
									><div id="${componentId}Area"><div class="title" style="float:left;"><span id="nmArea">${!empty cdListArray ? cdListArray[1] : !empty jsonMap['defaultRegrUid'] ? sessionScope.userVo.userNm :  jsonMap['userNms'] }</span
									></div><div style="display: block;float:right;margin:2px 7px 0 0;"
									><a title="<u:msg titleId="cols.user" alt="사용자"/>" class="sbutton button_small" href="javascript:;" <c:if test="${param.page eq 'user' }"> onclick="srchUserPop('${componentId}');"</c:if>
									><span id="idArea"><u:msg titleId="cols.user" alt="사용자"/><u:input type="hidden" id="${componentId}" value="${!empty cdListArray ? cdListArray[0] : !empty jsonMap['defaultRegrUid'] ? sessionScope.userVo.userUid :  jsonMap['userUids'] }"
									/></span></a><a title="전체삭제" class="sbutton button_small" href="javascript:;" <c:if test="${param.page eq 'user' }"> onclick="selectListDel('${componentId}');"</c:if>><span><u:msg titleId="cm.btn.allDel" alt="전체삭제"/></span></a
									></div>
									</div></c:if><c:if test="${component['coltyp'] eq 'dept' }"
									><div id="${componentId}Area"><div class="title" style="float:left;"><span id="nmArea">${!empty cdListArray ? cdListArray[1] : !empty jsonMap['defaultRegrUid'] ? sessionScope.userVo.deptNm :  jsonMap['orgNms'] }</span
									></div><div style="display: block;float:right;margin:2px 7px 0 0;"
									><a title="<u:msg titleId="cols.dept" alt="부서"/>" class="sbutton button_small" href="javascript:;" <c:if test="${param.page eq 'user' }"> onclick="srchOrgPop('${componentId}');"</c:if>
									><span id="idArea"><u:msg titleId="cols.dept" alt="부서"/><u:input type="hidden" id="${componentId}" value="${!empty cdListArray ? cdListArray[0] : !empty jsonMap['defaultRegrUid'] ? sessionScope.userVo.deptId :  jsonMap['orgIds'] }"
									/></span></a><a title="전체삭제" class="sbutton button_small" href="javascript:;" <c:if test="${param.page eq 'user' }"> onclick="selectListDel('${componentId}');"</c:if>><span><u:msg titleId="cm.btn.allDel" alt="전체삭제"/></span></a
									></div>
									</div></c:if><c:if test="${component['coltyp'] eq 'file' }"
									><c:if test="${param.page eq 'user' && empty isOnlyMd}"><u:files id="${filesId}" fileVoList="${fileVoList}" module="wf" mode="set" exts="${exts }" extsTyp="${extsTyp }" fileSizeModule="${mdTypCd }" /></c:if
									><c:if test="${param.page ne 'user' }"><div class="file2" data-colTyp="file" id="${list['jsonId'] }" onclick="setLoutProp(this, event);"><div class="file3"><img src="${_ctx}/images/etc/file1.png"/></div></div></c:if></c:if
									><c:if test="${component['coltyp'] eq 'radio' }"
									><ul class="attr_list"><c:if test="${empty jsonMap['chkTypCd'] }"><c:forTokens var="chkOpt" items="${jsonMap['chkList'] }" delims="${enter }" varStatus="chkStatus"
									><u:set var="chkVa" test="${fn:substring(chkOpt,0,1) eq lineEnter }" value="${fn:substringAfter(chkOpt,lineEnter) }" elseValue="${chkOpt }"
									/><li class="attr_check${fn:substring(chkOpt,0,1) eq lineEnter ? ' line_enter' : '' }"${jsonMap['lout']=='height' ? ' style="width:98%;"' : ''}><div style="float:left;"><span><input name="${idPrefix }${componentId }" id="${idPrefix }${componentId }_${chkStatus.index}" value="${chkVa }" type="radio" <c:if test="${chkStatus.first }">checked="checked"</c:if>
									></span></div><span style="line-height:20px;"><label for="${idPrefix }${componentId }_${chkStatus.index}">${chkVa }</label></span></li></c:forTokens></c:if>
									<c:if test="${!empty jsonMap['chkTypCd'] && !empty formCdListMap}"
									><u:convertMap srcId="formCdListMap" attId="CD_LIST_${jsonMap['chkTypCd']}" var="wfCdGrpBVoList" /><c:forEach var="wfCdGrpBVo" items="${wfCdGrpBVoList }" varStatus="chkStatus"
									><u:set var="checked" test="${!empty dataVa && fn:contains(dataVa, wfCdGrpBVo.cdId) }" value="Y" elseValue="N"/><li class="attr_check"${jsonMap['lout']=='height' ? ' style="width:98%;"' : ''}><div style="float:left;"><span><input name="${idPrefix }${componentId }" id="${idPrefix }${componentId }_${chkStatus.index}" value="${wfCdGrpBVo.cdId }" type="radio" <c:if test="${checked eq 'Y'}">checked="checked"</c:if>
									></span></div><span style="line-height:20px;"><label for="${idPrefix }${componentId }_${chkStatus.index}">${wfCdGrpBVo.cdRescNm }</label></span></li></c:forEach><u:input type="hidden" id="${idPrefix }${componentId }_cdYn" value="Y"/></c:if>									
									</ul></c:if><c:if test="${component['coltyp'] eq 'checkbox' }"
									><ul class="attr_list"><c:if test="${empty jsonMap['chkTypCd'] }"><c:forTokens var="chkOpt" items="${jsonMap['chkList'] }" delims="${enter }" varStatus="chkStatus"
									><u:set var="chkVa" test="${fn:substring(chkOpt,0,1) eq lineEnter }" value="${fn:substringAfter(chkOpt,lineEnter) }" elseValue="${chkOpt }"
									/><u:set var="checked" test="${!empty dataVa && fn:contains(dataVa, chkVa) }" value="Y" elseValue="N"/><li class="attr_check${fn:substring(chkOpt,0,1) eq lineEnter ? ' line_enter' : '' }"${jsonMap['lout']=='height' ? ' style="width:98%;"' : ''}><div style="float:left;"><span><input name="${idPrefix }${componentId }" id="${idPrefix }${componentId }-${chkStatus.index}" value="${chkVa }" type="checkbox" <c:if test="${checked eq 'Y'}">checked="checked"</c:if>
									></span></div><span style="line-height:20px;"><label for="${idPrefix }${componentId }-${chkStatus.index}">${chkVa }</label></span></li></c:forTokens></c:if
									><c:if test="${!empty jsonMap['chkTypCd'] && !empty formCdListMap}"
									><u:convertMap srcId="formCdListMap" attId="CD_LIST_${jsonMap['chkTypCd']}" var="wfCdGrpBVoList" /><c:forEach var="wfCdGrpBVo" items="${wfCdGrpBVoList }" varStatus="chkStatus"
									><u:set var="checked" test="${!empty dataVa && fn:contains(dataVa, wfCdGrpBVo.cdId) }" value="Y" elseValue="N"/><li class="attr_check"${jsonMap['lout']=='height' ? ' style="width:98%;"' : ''}><div style="float:left;"><span><input name="${idPrefix }${componentId }" id="${idPrefix }${componentId }-${chkStatus.index}" value="${wfCdGrpBVo.cdId }" type="checkbox" <c:if test="${checked eq 'Y'}">checked="checked"</c:if>
									></span></div><span style="line-height:20px;"><label for="${idPrefix }${componentId }-${chkStatus.index}">${wfCdGrpBVo.cdRescNm }</label></span></li></c:forEach><u:input type="hidden" id="${idPrefix }${componentId }_cdYn" value="Y"/></c:if>
									</ul></c:if><c:if test="${component['coltyp'] eq 'radioSingle' || component['coltyp'] eq 'checkboxSingle' }"
									><c:set var="checked" value="N"
									/><c:if test="${!empty jsonMap['singleId'] && !empty dataToJson[jsonMap['singleId']]}"
									><c:set var="chkList" value="${dataToJson[jsonMap['singleId']] }" 
									/><c:forTokens var="chkOpt" items="${chkList }" delims="," varStatus="chkStatus"
									><c:if test="${chkOpt == componentId }"><c:set var="checked" value="Y"
									/></c:if></c:forTokens></c:if><c:if test="${component['coltyp'] eq 'radioSingle' && empty firstChecked }"><c:set var="checked" value="Y"
									/><c:set var="firstChecked" value="Y" /></c:if
									><div class="attr_check"><input type="${fn:replace(component['coltyp'], 'Single', '') }" id="${componentId }" name="${jsonMap['singleId'] }" value="${componentId }" title="${langTitle }"<c:if test="${checked eq 'Y'}"> checked="checked"</c:if>
									/></div></c:if><c:if test="${component['coltyp'] eq 'select' }"
									><c:if test="${empty jsonMap['chkTypCd'] }"><select id="${idPrefix }${componentId }" name="${idPrefix }${componentId }" title="${langTitle }"
									><c:forTokens var="chkOpt" items="${jsonMap['chkList'] }" delims="${enter }" varStatus="chkStatus"
									><u:option value="${chkOpt }" title="${chkOpt }" alt="${chkOpt }"	
									/></c:forTokens></select></c:if><c:if test="${!empty jsonMap['chkTypCd'] && !empty formCdListMap}"
									><select id="${idPrefix }${componentId }" name="${idPrefix }${componentId }" title="${langTitle }"
									><u:convertMap srcId="formCdListMap" attId="CD_LIST_${jsonMap['chkTypCd']}" var="wfCdGrpBVoList" /><c:forEach var="wfCdGrpBVo" items="${wfCdGrpBVoList }" varStatus="chkStatus"
									><u:option value="${wfCdGrpBVo.cdId }" title="${wfCdGrpBVo.cdRescNm }" alt="${wfCdGrpBVo.cdRescNm }" checkValue="${dataVa }"
									/></c:forEach></select><u:input type="hidden" id="${idPrefix }${componentId }_cdYn" value="Y"/></c:if>
									</c:if><c:if test="${component['coltyp'] eq 'image' }"
									><c:if test="${isEdit ne 'Y' }"><u:set var="viewSizeTyp" test="${!empty jsonMap['viewSizeTyp'] && jsonMap['viewSizeTyp'] eq '%' }" value="per" elseValue="px" 
									/><u:set var="maxWdth" test="${!empty jsonMap['viewWdth'] }" value="${jsonMap['viewWdth'] }" elseValue="88" 
									/><u:set var="maxHght" test="${!empty jsonMap['viewHeight'] }" value="${jsonMap['viewHeight'] }" elseValue="${viewSizeTyp eq 'px' ? '110' : '100' }" 
									/><c:if test="${!empty wfWorksLVoMap && !empty wfWorksLVoMap.imgListMap[componentId] }"><c:set var="imgVo" value="${wfWorksLVoMap.imgListMap[componentId]}"
									/></c:if><c:if test="${!empty imgVo}"><u:set test="${!empty imgVo.imgPath}" var="imgPath" value="${_cxPth}${imgVo.imgPath}" elseValue="${_cxPth}/images/${_skin}/photo_noimg.png" 
									/><c:if test="${viewSizeTyp eq 'px'}"><u:set test="${imgVo.imgWdth <= maxWdth}" var="imgWdth" value="${imgVo.imgWdth}" elseValue="${maxWdth}" 
									/><u:set test="${imgVo.imgHght <= maxHght}" var="imgHght" value="${imgVo.imgHght}" elseValue="${maxHght}" 
									/><u:set test="${imgVo.imgWdth < imgVo.imgHght}" var="imgWdthHgth" value="height='${imgHght}'" elseValue="width='${imgWdth}'" 
									/></c:if><c:if test="${viewSizeTyp eq 'per'}"
									><u:set test="${maxWdth < maxHght}" var="imgWdthHgth" value="height='${maxHght}%'" elseValue="width='${maxWdth}%'" 
									/></c:if></c:if><div class="image_profile" data-viewSizeTyp="${viewSizeTyp }" data-maxwidth="${maxWdth }" data-maxheight="${maxHght }"><u:input type="hidden" id="${componentId }"
									/><u:input type="hidden" id="imgData" value=""
									/><c:if test="${!empty imgVo}"><u:input type="hidden" id="imgNo" value="${imgVo.imgNo }"/></c:if><dl><dd class="photo" style="overflow-x:auto;"><c:if test="${!empty imgVo}"
									><img id="profileImg" src="${imgPath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' ${imgWdthHgth}
									></c:if><c:if test="${empty imgVo }"><img id="profileImg" src="${_cxPth}/images/${_skin}/photo_noimg.png" width="88px"></c:if></dd><dd class="photo_btn"><u:set var="dataList" test="${!empty imgVo }" value="data-imgno=\"${imgVo.imgNo }\"" elseValue=""
									/><u:buttonS id="setImgBtn" alt="변경" titleId="cm.btn.chg" onclick="setImagePop(this, {refNm:'[${!empty component['refId'] ? component['refId'] : componentId }]'});" popYn="Y"  dataList="${dataList }"
									/><u:set test="${param.page eq 'user' }" var="delBtnClick" value="delImg(this);" elseValue=""/><u:buttonS id="delImgBtn" alt="삭제" titleId="cm.btn.del" onclick="${delBtnClick }" popYn="Y" dataList="${dataList }" /></dd></dl></div
									></c:if><c:if test="${isEdit eq 'Y' }"
									><div class="image_profile"><dl><dd class="photo"><img id="bcImage" src="${_cxPth}/images/${_skin}/photo_noimg.png" width="88px"
									/></dd><dd class="photo_btn"><u:buttonS alt="변경" titleId="cm.btn.chg" popYn="Y" 
									/><u:buttonS alt="삭제" titleId="cm.btn.del" popYn="Y" /></dd></dl></div
									></c:if>
									</c:if><!-- conponent end --></div><c:if test="${isEdit eq 'Y' && cell['colTyp'] ne 'label'}"><a class="close-thik" onclick="$formBuilder.delComponent(this, event);" href="#"></a></c:if>
									</div>
								</c:forEach>
							</c:if>
						</td>
					</c:forEach>
				</c:if>
			</tr>
		</c:forEach>
	</c:if>
	</tbody>
	</table>
</div>
<u:blank />
</c:if>
<c:set var="jsonMap" value="${jsonVa[list['jsonId']] }" scope="request"
/><u:convertJsonString var="dataJson" value="${jsonMap }"/>
<u:out var="dataJson" value="${dataJson }" type="value"/>
<c:if test="${list['type'] eq 'file' }"><c:set var="langTitleId" value="nameRescVa_${_lang }" scope="request" 
/><u:set var="langTitle" test="${!empty jsonMap[langTitleId] }" value="${jsonMap[langTitleId] }" elseValue="${jsonMap['name'] }"	
/><c:if test="${param.page eq 'user' && empty isOnlyMd}"><u:listArea><tr><td><u:files id="${filesId}" fileVoList="${fileVoList}" module="wf" mode="set" exts="${exts }" extsTyp="${extsTyp }" fileSizeModule="${mdTypCd }"/></td></tr></u:listArea></c:if
><c:if test="${param.page ne 'user' }"><div class="file2 component_list" id="${list['jsonId'] }Area" data-colTyp="file" data-id="${list['jsonId'] }" <c:if test="${isEdit eq 'Y' }"> data-name="${langTitle }" onclick="setLoutProp(this, event);" data-json='${dataJson }' </c:if><c:if test="${isEdit eq 'Y' && !empty wfFormColmLVoMap}"> data-colmId='${wfFormColmLVoMap[list['jsonId']] }'</c:if>><div class="file3"><img src="${_ctx}/images/etc/file1.png"/></div></div><u:blank /></c:if>
</c:if><c:if test="${list['type'] eq 'gap' }">
<div class="blank"></div>
</c:if><c:if test="${list['type'] eq 'line' }">
<div class="line"></div>
</c:if><c:if test="${list['type'] eq 'label' }"><c:set var="langTitleId" value="nameRescVa_${_lang }" scope="request" 
/><u:set var="langTitle" test="${!empty jsonMap[langTitleId] }" value="${jsonMap[langTitleId] }" elseValue="${jsonMap['name'] }"	
/><c:set var="componentStyle"/><!-- 컴포넌트 스타일 -->
<c:if test="${!empty jsonMap['fontFamilies']}"><c:set var="componentStyle" value="${componentStyle}font-family:${jsonMap['fontFamilies']};"
/></c:if><c:if test="${!empty jsonMap['fontSizes']}"><c:set var="componentStyle" value="${componentStyle}font-size:${jsonMap['fontSizes']};"
/></c:if><c:if test="${!empty jsonMap['fontWeight']}"><c:set var="componentStyle" value="${componentStyle}font-weight:${jsonMap['fontWeight']};"
/></c:if><c:if test="${!empty jsonMap['fontStyle']}"><c:set var="componentStyle" value="${componentStyle}font-style:${jsonMap['fontStyle']};"
/></c:if><c:if test="${!empty jsonMap['textDecoration']}"><c:set var="componentStyle" value="${componentStyle}text-decoration:${jsonMap['textDecoration']};"
/></c:if><c:if test="${!empty jsonMap['fontColor']}"><c:set var="componentStyle" value="${componentStyle}color:${jsonMap['fontColor']};"
/></c:if><c:if test="${!empty componentStyle }"><c:set var="componentStyle" value=" style=\"${componentStyle }\""
/></c:if><div class="component_list label" id="${list['jsonId'] }Area" data-id="${list['jsonId'] }" data-colTyp="label" <c:if test="${isEdit eq 'Y' }"> data-name="${langTitle }" data-tooltip-text="${list['jsonId'] }" onclick="setLoutProp(this, event);" data-json='${dataJson }' </c:if
>${componentStyle }><div class="noSelect"<c:if test="${!empty jsonMap['labelAlign'] }"> style="text-align:${jsonMap['labelAlign'] }"</c:if>><label for="${list['jsonId'] }" class="header">${langTitle }</label></div></div>
</c:if><c:if test="${list['type'] eq 'editor' }"><c:set var="langTitleId" value="nameRescVa_${_lang }" scope="request" 
/><u:set var="langTitle" test="${!empty jsonMap[langTitleId] }" value="${jsonMap[langTitleId] }" elseValue="${jsonMap['name'] }"	
/><c:if test="${param.page eq 'user' }"><div id="editorArea" class="component_list" data-name="${langTitle }" data-colTyp="editor" data-id="${list['jsonId'] }" style="width:100%;"
><c:if test="${isOnlyMd==true && mdCd=='AP' }"><c:set var="editorAreaId" value="${componentId }LoutArea"/><div id="${componentId }LoutArea" style="height:400px;"></div></c:if><u:editor id="${list['jsonId'] }" width="100%" height="400px" module="wf" areaId="${editorAreaId }" value="${!empty dataToJson[list['jsonId']] ? dataToJson[list['jsonId']] : !empty jsonMap['startText'] ? jsonMap['startText'] : ''}" 
/></div></c:if><c:if test="${param.page ne 'user' }"><div class="editor2 component_list" id="${list['jsonId'] }Area" data-colTyp="editor" data-id="${list['jsonId'] }" <c:if test="${isEdit eq 'Y' }"> data-name="${langTitle }" onclick="setLoutProp(this, event);" data-json='${dataJson }' </c:if><c:if test="${isEdit eq 'Y' && !empty wfFormColmLVoMap}"> data-colmId='${wfFormColmLVoMap[list['jsonId']] }'</c:if>><div class="editor3"><img src="${_ctx}/images/etc/editor1.png"/></div></div><u:blank /></c:if>
</c:if>
<div style="display:none" id="itemsDataArea"
><c:if test="${!empty list['itemData'] }"><c:forEach var="itemData" items="${list['itemData'] }" varStatus="itemStatus"><input type="hidden" name="items-${dataSeq }-${itemData['name']}" value="${itemData['value']}"/></c:forEach></c:if></div>
</div><!-- itemArea -->
</c:forEach>
</div>

</c:forEach>
<u:input type="hidden" id="dataSeq" value="${dataSeq }"
/><u:input type="hidden" id="delImgNoList" value=""
/></c:if><c:if test="${empty loutList }"><u:input type="hidden" id="dataSeq" value="${dataSeq }"/><div class="loutFormArea" id="loutFormArea0"></div></c:if>
<div id="dummyArea" style="display:none;"></div>
