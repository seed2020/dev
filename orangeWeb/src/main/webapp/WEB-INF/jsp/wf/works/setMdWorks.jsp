<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><c:set var="loutPrefix" value="mdForm"/>
<u:params var="params" /><u:params var="paramsForList" excludes="workNo"/>
<script type="text/javascript">
<!-- 
var userComponentIdMap=null;

function getUserComponentId(id){ // 컴포넌트 ID
	if(userComponentIdMap==null) userComponentIdMap=new ParamMap();
	var attrId = userComponentIdMap.get(id);
	if(attrId==null) attrId=1;
	userComponentIdMap.put(id, parseInt(attrId)+1);
	return attrId;
}

//serializeArray To Object
function objToJson(data, nullValue, isArray, exclude){
	if(data==null) return null;
	var jsonObj={};
	$.each(data, function(index, obj){
		if(nullValue && this.value=='' || (exclude!=undefined && exclude.startsWith(this.name))) return true;
		if(isArray && jsonObj[this.name]!=undefined) jsonObj[this.name]=jsonObj[this.name]+','+this.value;
		else jsonObj[this.name]=this.value;
	});
	return jsonObj;
}
<% // 반복한 행의 데이터를 배열로 리턴 %>
function getRepetData(tbody, dataMap){
	//  데이터맵
	//var dataMap=new ParamMap();
	
	var key, obj, trs, data, arrays;
	$(tbody).find('div.component_list[data-coltyp="button"]').each(function(){
		$refId=$(this).attr('data-refid');
		if($refId===undefined) return true;
		
		obj=$(this).find('a#addRowBtn');
		// 행추가 가능한지 체크
		var isChk=chkAddRow($(tbody), obj);
		if(!isChk){
			return true;
		}
		trs=getTrList(tbody, obj);
		
		$refId=$(this).attr('data-refid');		
		if($refId===undefined){
			key=$(this).attr('data-id');
		}else{
			key=$refId;
		}
		
		//key=tblId+'-row-'+trs.start+'-'+trs.end;
				
		if(dataMap.get(key)==null){
			dataMap.put(key, []);			
		}		
		
		data={};
		data['row']=trs.start+'-'+trs.end;
		$.each(trs.list, function(idx, tr){
			$(this).find('div.component_list').not('[data-coltyp="button"]').each(function(){
				//arrayData=[];
				//$id=$(this).attr('data-refid') || $(this).attr('data-id');
				$colTyp=$(this).attr('data-coltyp');
				
				$id=$(this).attr('data-id');
				$refId=$(this).attr('data-refid');
				if($refId===undefined) return true;
				$prefixId=($refId===undefined ? $id : $refId); 
				$newId=$prefixId+'_'+getUserComponentId($prefixId);
				
				if($colTyp=='image'){
					data[$newId]=$id;
					return true;
				}else if($colTyp=='checkbox'){
					arrays=[];
					$.each($(this).find('input[type="checkbox"]:checked'), function(){
						arrays.push($(this).val());
					});
					if(arrays.length>0) data[$newId]=arrays.join(',');
					return true;
				}
				
				$(this).find(':input').each(function(){
					$name=$(this).attr('name');
					
					if($name===undefined) return true;
					$name=$name.replace($id, $newId);
					data[$name]=$(this).val();
					
				});
			});
		});
		dataMap.get(key).push(data);
		
	});
	//return dataMap;
}
<% // 폼에 json으로 저장 %>
function setFormData(form, appendArea){
	
	if(appendArea==null) appendArea=form;
	var data=[];
	
	var attrVa={};
	
	var loutList=[];// DB 테이블의 컬럼 목록
	var jsonList=null, dataId=null, id;//, dataMap;
	//var repetList=[];
	var repetMap=new ParamMap();
	
	var itemStyles;
	form.find('div.loutFormArea').each(function(idx, lout){
		if($(this).html()=='') return true;
		$loutFormAreaId='loutFormArea'+idx;
		jsonList=[];
		// json 데이터 저장
		$(this).find('div#itemsArea').each(function(){
			dataId=$(this).attr('data-id');
			if(dataId!='file'){
				$array=$(this).find(':input').serializeArray();
				if($array.length>0) $.merge(data, $array);
			}
			
			itemStyles={}; // 항목 스타일
			
			itemStyles['maxWidth']=$(this).css('max-width');
			
			if(dataId=='table'){
				// 타이틀 
				$title='';
				if($(this).find('div.tit_left').length>0){
					$title=$(this).find('div.tit_left .title_s').text();
				}
				
				$colgroup=[]; // colgroup
				$(this).find('table:first colgroup > col').each(function(){
					$colgroup.push($(this).attr('width'));
				});
				$row=[];
				$tbody=$(this).find('tbody:first');
				
				getRepetData($tbody, repetMap);// 반복행 데이터
				//if(!dataMap.isEmpty())
					//repetList.push(dataMap.toJSON());
				$tbody.children().each(function(){
					$cell=[];
					$(this).children().each(function(){
						$components=[];
						$(this).find('div.component_list').each(function(){
							$itemNm=$(this).attr('data-name');
							$items={id:$(this).attr('data-id'), coltyp:$(this).attr('data-colTyp'), title:$itemNm, refId:$(this).attr('data-refid')};
							if($(this).attr('data-colTyp')=='calculate'){
								$input=$(this).find('input[id="'+$items['id']+'"]');
								if($input.attr('data-formula')!=undefined) $items['formula']=$input.attr('data-formula');
							}
							$components.push($items);
						});
						$cell.push({colspan: $(this).attr('colspan'), rowspan: $(this).attr('rowspan'), colTyp:$(this).attr('data-colTyp'), clsnm: $(this).attr('class').replace(/(\s|)cell-selected|(\s|)cell_on|/g, ''), components:$components});
					});
					$row.push({id:$(this).attr('id'), groupId: $(this).attr('data-groupid'), cell:$cell});
				});
				$table=$($tbody).closest('table');
				jsonList.push({type:dataId, title:$title, tblId:$($table).attr('id'), loutTyp:$(this).attr('data-loutTyp'), colgroup:$colgroup, row:$row, itemStyles:itemStyles});
			}else{
				id=$(this).find('div.component_list').eq(0).attr('data-id');
				$div=$(this).find('div.component_list');
				$itemNm=$div.attr('data-name');
				jsonList.push({type:dataId, jsonId:id, title:$(this).find('div.component_list').attr('data-name')});
			}			
		});
		loutList.push({loutId:$loutFormAreaId, list:jsonList});
	});
	
	// 컴포넌트 ID 최대값
	attrVa['maxId']=getComponentMaxId();
	
	if(repetMap.keys.length>0) attrVa['repetData']=repetMap.toJSON();
	
	// 속성값
	appendArea.find("input[name='attrVa']").remove();
	appendArea.appendHidden({name:'attrVa',value:JSON.stringify(attrVa)});
	
	// 레이아웃값
	appendArea.find("input[name='loutVa']").remove();
	if(loutList.length>0)	appendArea.appendHidden({name:'loutVa',value:JSON.stringify(loutList)});
	else appendArea.appendHidden({name:'loutVa',value:''});
	
	//var data = $('#setForm').serializeArray();
	data=objToJson(data, false, true);
	appendArea.find("input[name='jsonVa']").remove();
	appendArea.appendHidden({name:'jsonVa',value:JSON.stringify(data)});
	
}<% //[하단버튼:저장] - 저장 %>
function save(){	
	if (validator.validate('setForm')) {
		
		var editors = $('div#editorArea');
		if(editors.length>0){
			var isOverSize=false;
			$.each(editors, function(){
				$editor=$(this).find('textarea[id^=editor]').eq(0);
				if (isInUtf8Length($editor.val(), 0, '${bodySize}') > 0) {
					alertMsg('cm.input.check.maxbyte', [$editor.closest('div#editorArea').attr('data-name'),'${bodySize}']);<% // cm.input.check.maxbyte="{0}"의 최대 바이트수 ({1} bytes)를 초과 하였습니다. %>
					isOverSize=true;
					return false;
				}
				$editorId=$(this).find('textarea[id^=editor]').attr('id');
				editor($editorId).prepare();
			});
			if(isOverSize) return;
		}
		
		var $form = $('#setForm');		
		$form.attr('method','post');
		$form.attr('action','./${transPage}.do?menuId=${menuId}');
		$form.attr('enctype','multipart/form-data');
		$form.attr('target','dataframe');
		
		setFormData($form);
		//return;
		saveFileToForm('${filesId}', $form[0], null);
	}
}<% //[모듈별(결재..) 데이터 세팅] - 저장 %>
function setSaveData(formArea, appendArea, isValidate){
	if(isValidate==null || isValidate===undefined)
		isValidate=validator.validate('formArea');
	if (isValidate) {
		var editors = $('div#editorArea');
		if(editors.length>0){
			var isOverSize=false;
			$.each(editors, function(){
				$editor=$(this).find('textarea[id^=editor]').eq(0);
				if (isInUtf8Length($editor.val(), 0, '${bodySize}') > 0) {
					alertMsg('cm.input.check.maxbyte', [$editor.closest('div#editorArea').attr('data-name'),'${bodySize}']);<% // cm.input.check.maxbyte="{0}"의 최대 바이트수 ({1} bytes)를 초과 하였습니다. %>
					isOverSize=true;
					return false;
				}
				$editorId=$(this).find('textarea[id^=editor]').attr('id');
				editor($editorId).prepare();
			});
			if(isOverSize) return false;
		}
		setFormData(formArea, appendArea);
	}
	
	return isValidate;
}<% //[업무번호 생성] - 저장 (mdCd:AP, formNo:양식번호-업무관리)%>
function saveToWorkNo(apParams, $formArea, $appendArea){
	if(apParams==null) return null;
	
	if(!setSaveData($formArea, $appendArea)){
		return null;
	}
	var param = {};
	param['loutVa']=$appendArea.find('input[name="loutVa"]').val();
	param['jsonVa']=$appendArea.find('input[name="jsonVa"]').val();
	param['attrVa']=$appendArea.find('input[name="attrVa"]').val();
	param['mdCd']='${mdCd}';
	param['formNo']='${wfFormBVo.formNo}';
	
	if(apParams['workNo']!='')
		param['workNo']=apParams['workNo'];
	if(apParams['genId']!='')
		param['genId']=apParams['genId'];
	//else
	//	param['genId']='${wfFormBVo.genId}';
	
	var returnParams=null;
	callAjax('./transWorkNoAjx.do?${wfMenuParams}', param, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok' && data.returnMap!=null) {
			returnParams=data.returnMap.map;
		}
	});
	
	return returnParams;
	
}
<% //[하단버튼:저장] - 저장 %>
function listPage(){
	parent.location.href="./listWorks.do?${paramsForList}";
}
<% //[하단버튼:기존양식으로 보기] - 팝업 %>
function viewWorksPop(){
	openWindow('./viewWorksPop.do?menuId=${menuId}&formNo=${param.formNo}&workNo=${param.workNo}', 'viewWorksDialog', 870, 770);
	//dialog.open('viewWorksDialog','<u:msg titleId="wf.btn.not.genId" alt="기존양식으로 보기" />','./viewWorksPop.do?menuId=${menuId}&formNo=${param.formNo}&workNo=${param.workNo}');
}
<% // 이미지 목록 맵 세팅 %>
var imgListMap=null;
function setImgListMap(){
	<c:if test="${!empty wfWorksLVoMap.imgListMap}">
	imgListMap=new ParamMap();
	<c:forEach var="map" items="${wfWorksLVoMap.imgListMap }" varStatus="status">
	imgListMap.put('${map.key}', {imgNo:'${map.value.imgNo}', imgPath:'${map.value.imgPath}', width:'${map.value.imgWdth}', height:'${map.value.imgHght}'});
	</c:forEach>
	</c:if>
}
<% // 코드 목록 맵 세팅 %>
var cdListMap=null;
function setCdListMap(){
	<c:if test="${!empty cdListMap}">
	cdListMap=new ParamMap();
	<c:forEach var="map" items="${cdListMap }" varStatus="status">
		cdListMap.put('${map.key}', {value:'${map.value[0]}', title:'${map.value[1]}'});
	</c:forEach>
	</c:if>
}
<% // 이미지 세팅(반복행) %>
function setRepetImage(target, key, colmNm){
	if(imgListMap==null) return;
	
	var jsonParams=imgListMap.get(key);
	$img=target.find('img').eq(0);
	$img.attr("src", jsonParams['imgPath']);
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
	
	if(jsonParams['imgNo']!=undefined){
		var imgNo=jsonParams['imgNo'];
		var imgData={imgNo:imgNo, colmNm:colmNm};
		target.find('input[name="imgData"]').val(JSON.stringify(imgData));
		target.find('input[name="imgNo"]').val(imgNo);
		target.find('a#setImgBtn').attr('data-imgno', imgNo);
		target.find('a#delImgBtn').attr('data-imgno', imgNo);
	}
}
<% // 반복행의 반복 갯수 변경(양식이 변경되었을 경우 기존 반복행의 데이터를 신규 양식에 반영)  %>
function setRepetInit(jsonVa){
	if(jsonVa['repetData']!=undefined){
		setCdListMap(); // 코드 목록맵 세팅
		setImgListMap(); // 이미지 목록맵 세팅
		var obj, rowLen, i, tbody, row, start, end, tr, target, btnList, firstBtn, isChk, areaId='setForm';
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
				for(i=0;i<rowLen;i++){
					//$(obj).trigger('click');
					obj = addRow(obj);
				}
			});
			$.each(va, function(idx, json){
				if(json['row']===undefined) return true;
				row=json['row'].split('-')[0];
				start=row[0];
				end=row[1];
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
					$('#'+areaId).find(':text[name="'+name+'"], :hidden[name="'+name+'"], select[name="'+name+'"], textarea[name="'+name+'"]').val(value);
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
}<% // 에디터 리사이즈 %>
function tabHandlermdForm(containerId){
	var target=containerId===undefined ? document : '#'+containerId;
	$.each($(target).find('div.component_list[data-coltyp="editor"]'), function(idx, edt){
		$id=$(edt).attr('data-id');
		editor($id).resize();
	});	
}
<% // 에디터 ID 목록 %>
var editorList=[]; 
<% // 에디터 클리어 %>
function clearWfEditor(){
	editorList.each(function(index, id){editor(id).clean();	unloadEvent.removeEditor(id);});
}
$(document).ready(function() {
	<c:if test="${empty isOnlyMd || isOnlyMd==false}">
	setUniformCSS();
	</c:if>
	<c:if test="${wfFormBVo.formTyp ne 'R' }">alertMsg('wf.msg.form.not.insertBody'); // wf.msg.form.not.insertBody=해당 결재 양식은 본문삽입용이 아닙니다. </c:if>
	$.each($('#formArea').find('div.component_list[data-coltyp="editor"]'), function(idx, edt){
		editorList.push($(edt).attr('data-id'));
	});
});
//-->
</script>
<c:if test="${wfFormBVo.formTyp ne 'R' }"><div style="width:100%;height:400px;background-color:#fafafa;"></div></c:if>
<c:if test="${wfFormBVo.formTyp eq 'R' }">
<u:set var="idPrefix" test="${!empty idPrefix }" value="${idPrefix }" elseValue=""/>
<!-- 탭 -->
<jsp:include page="/WEB-INF/jsp/wf/works/inclTab.jsp" flush="false">
<jsp:param value="user" name="tabPage"/>
<jsp:param value="${loutPrefix }" name="loutPrefix"/>
</jsp:include>
<u:input type="hidden" id="genId" value="${wfFormBVo.genId }" />
<u:input type="hidden" id="formNo" value="${wfFormBVo.formNo }" />
<!-- 컴포넌트 영역 -->
<div id="formArea">
<jsp:include page="/WEB-INF/jsp/wf/works/inclRegForm.jsp" flush="false">
<jsp:param value="user" name="page"/>
<jsp:param value="${loutPrefix }" name="loutPrefix"/>
</jsp:include>
</div>
</c:if>