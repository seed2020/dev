<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="params" /><u:params var="paramsForList" excludes="workNo"/>
<u:set var="openParams" test="${!empty isOpen && isOpen==true}" value="&isOpen=Y" elseValue=""
/><u:set var="openCallback" test="${!empty param.openCallback}" value="${param.openCallback }" elseValue="reloadOpen"/>
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
	
	var key, obj, trs, data, arrays, colTyp, dataId, refId, prefixId, newId;
	$(tbody).find('div.component_list[data-coltyp="button"]').each(function(){
		refId=$(this).attr('data-refid');
		if(refId===undefined) return true;
		
		obj=$(this).find('a#addRowBtn');
		// 행추가 가능한지 체크
		var isChk=chkAddRow($(tbody), obj);
		if(!isChk){
			return true;
		}
		trs=getTrList(tbody, obj);
		
		refId=$(this).attr('data-refid');		
		if(refId===undefined){
			key=$(this).attr('data-id');
		}else{
			key=refId;
		}
				
		if(dataMap.get(key)==null){
			dataMap.put(key, []);			
		}		
		
		data={};
		data['row']=trs.start+'-'+trs.end;
		
		var name;
		$.each(trs.list, function(idx, tr){
			$(this).find('div.component_list').not('[data-coltyp="button"]').each(function(){
				//arrayData=[];
				//dataId=$(this).attr('data-refid') || $(this).attr('data-id');
				colTyp=$(this).attr('data-coltyp');
				
				dataId=$(this).attr('data-id');
				refId=$(this).attr('data-refid');
				if(refId===undefined) return true;
				prefixId=(refId===undefined ? dataId : refId); 
				newId=prefixId+'-'+getUserComponentId(prefixId);
				
				if(colTyp=='image'){
					data[newId]=dataId;
					return true;
				}else if(colTyp=='checkbox'){
					arrays=[];
					$.each($(this).find('input[type="checkbox"]:checked'), function(){
						arrays.push($(this).val());
					});
					if(arrays.length>0) data[newId]=arrays.join(',');
					return true;
				}
				
				$(this).find(':input').each(function(){
					name=$(this).attr('name');
					
					if(name===undefined) return true;
					name=name.replace(dataId, newId);
					data[name]=$(this).val();
					
				});
			});
		});
		dataMap.get(key).push(data);
		
	});
	//return dataMap;
}
<% // 폼에 json으로 저장 %>
function setFormData(form){
	
	var data=[];
	
	var attrVa={};
	
	var loutList=[];// DB 테이블의 컬럼 목록
	var jsonList=null, dataId=null, id;//, dataMap;
	//var repetList=[];
	var repetMap=new ParamMap();
	
	var loutFormAreaId, title, itemStyles;
	form.find('div.loutFormArea').each(function(idx, lout){
		if($(this).html()=='') return true;
		loutFormAreaId='loutFormArea'+idx;
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
				title='';
				if($(this).find('div.tit_left').length>0){
					title=$(this).find('div.tit_left .title_s').text();
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
				jsonList.push({type:dataId, title:title, tblId:$($table).attr('id'), loutTyp:$(this).attr('data-loutTyp'), colgroup:$colgroup, row:$row, itemStyles:itemStyles});
			}else{
				id=$(this).find('div.component_list').eq(0).attr('data-id');
				$div=$(this).find('div.component_list');
				$itemNm=$div.attr('data-name');
				jsonList.push({type:dataId, jsonId:id, title:$(this).find('div.component_list').attr('data-name')});
			}			
		});
		loutList.push({loutId:loutFormAreaId, list:jsonList});
	});
	
	// 컴포넌트 ID 최대값
	attrVa['maxId']=getComponentMaxId();
	if(repetMap.keys.length>0) attrVa['repetData']=repetMap.toJSON();
	
	// 속성값
	form.find("input[name='attrVa']").remove();
	form.appendHidden({name:'attrVa',value:JSON.stringify(attrVa)});
	
	// 레이아웃값
	form.find("input[name='loutVa']").remove();
	if(loutList.length>0)	form.appendHidden({name:'loutVa',value:JSON.stringify(loutList)});
	else form.appendHidden({name:'loutVa',value:''});
	
	//var data = $('#setForm').serializeArray();
	data=objToJson(data, false, true);
	form.find("input[name='jsonVa']").remove();
	form.appendHidden({name:'jsonVa',value:JSON.stringify(data)});
	
}<% //[하단버튼:저장] - 저장 %>
function save(){
	var isValid=true;
	if($('#tabArea')){ // 탭이 있을경우
		var loutFormAreaId, tab;
		$('#setForm div.loutFormArea').each(function(idx, lout){ // 탭 Area 별로 유효성 검증
			if($(this).html()=='') return true;
			loutFormAreaId=$(this).attr('id');
			if(loutFormAreaId===undefined) return true;
			if(!validator.validate(loutFormAreaId)){
				isValid=false;
				tab=$('#tabArea li[data-areaid="'+loutFormAreaId+'"]');
				if(tab!=undefined && tab.attr('class')=='basic'){ // 탭 활성화
					tab.find('span').trigger('click');
				}
				return false;
			}
		});
	}else{
		isValid=validator.validate('setForm'); // 폼 전체 유효성 검증
	}
	
	if (isValid) {
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
		$form.attr('target',"${isOpen==true ? 'dataframeForFrame' : 'dataframe'}");
		setFormData($form);
		saveFileToForm('${filesId}', $form[0], null);
	}
}<% //[모듈별(결재..) 데이터 세팅] - 저장 %>
function setSaveData(form){	
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
		setFormData(form);
	}
}
<% //[하단버튼:저장] - 저장 %>
function listPage(){
	parent.location.href="./listWorks.do?${paramsForList}";
}<% //[팝업에서 저장 후 페이지 새로고침] %>
function reloadOpenHandler(mobileDomain, msgId){
	<c:if test="${param.isMobile=='Y'}">location.replace(mobileDomain+'/callback.do?func=${openCallback}&msgId='+msgId);</c:if>
	<c:if test="${empty param.isMobile}">
		if(window.opener){
			window.opener.${openCallback}(); window.open('about:blank','_self').close();
		}
	</c:if>
	
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
<% // 에디터 리사이즈 %>
function tabHandler(containerId){
	var target=containerId===undefined ? document : '#'+containerId;
	var dataId;
	$.each($(target).find('div.component_list[data-coltyp="editor"]'), function(idx, edt){
		dataId=$(edt).attr('data-id');
		editor(dataId).resize();
	});	
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<c:if test="${isOpen==true }"><c:set var="style" value=" style=\"min-width:1080px;padding:10px;\""/></c:if>
<div${style }>
<c:if test="${empty isOnlyMd || isOnlyMd==false}">
<u:title title="${wfFormBVo.formNm }" alt="${wfFormBVo.formNm }" menuNameFirst="true" />
</c:if>
<c:if test="${!empty notGenId && notGenId==true }"><div class="red_txt">※ <u:msg titleId="wf.msg.not.genId" alt="양식이 변경되었습니다. 하단 '기존양식으로 보기' 버튼으로 확인하세요." /></div><u:blank /></c:if>

<u:set var="idPrefix" test="${!empty idPrefix }" value="${idPrefix }" elseValue=""/>
<!-- 탭 -->
<jsp:include page="/WEB-INF/jsp/wf/works/inclTab.jsp" flush="false">
<jsp:param value="user" name="tabPage"/>
</jsp:include>
<form id="setForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="listPage" value="./${listPage}.do?${paramsForList}" />
<u:input type="hidden" id="viewPage" value="./${viewPage}.do?${params}" />
<u:input type="hidden" id="genId" value="${wfFormBVo.genId }" />
<u:input type="hidden" id="formNo" value="${param.formNo }" />
<c:if test="${!empty param.workNo }"><u:input type="hidden" id="workNo" value="${param.workNo }" /></c:if>
<div id="formArea">
<!-- 컴포넌트 영역 -->
<jsp:include page="/WEB-INF/jsp/wf/works/inclRegForm.jsp" flush="false">
<jsp:param value="user" name="page"/>
</jsp:include>
</div>
</form>
<!-- 모듈별 양식 화면은 버튼 제거 [해당 모듈의 기능으로 대체] -->
<c:if test="${empty isOnlyMd || isOnlyMd==false}">
<u:buttonArea topBlank="true">
	<c:if test="${!empty notGenId && notGenId==true }"><u:button titleId="wf.btn.not.genId" alt="기존양식으로 보기" onclick="viewWorksPop();" auth="W" /></c:if>
	<u:button titleId="cm.btn.save" alt="저장" onclick="save();" auth="W" />
	<c:if test="${!empty isOpen && isOpen==true }"><u:button titleId="cm.btn.close" alt="닫기" href="javascript:;" onclick="window.open('about:blank','_self').close();"/></c:if>
	<c:if test="${empty isOpen }"><u:button titleId="cm.btn.cancel" alt="취소" href="javascript:;" onclick="listPage();"/></c:if>
</u:buttonArea>
</c:if>
</div>

