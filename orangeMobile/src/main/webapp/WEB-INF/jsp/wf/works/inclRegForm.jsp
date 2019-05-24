<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
	import="java.util.Date" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
// jstl 에서 스페이스 + 엔터 치환
pageContext.setAttribute("enter","\r\n");
pageContext.setAttribute("lineEnter",";"); %>
<fmt:formatDate var="today" value="<%=new Date() %>" type="date" pattern="yyyy-MM-dd"/>
<fmt:formatDate var="todaytime" value="<%=new Date() %>" type="date" pattern="yyyy-MM-dd HH:mm" />
<script src="/js/numeral.min.js" type="text/javascript"></script>
<script src="/js/jquery-calx-2.2.7.min.js" type="text/javascript"></script>

<script type="text/javascript">
//<![CDATA[
$.fn.serializefiles = function() {
    var obj = $(this);
    /* ADD FILE TO PARAM AJAX */
    var formData = new FormData();
    $.each($(obj).find("input[type='file']"), function(i, tag) {
        $.each($(tag)[0].files, function(i, file) {
            formData.append(tag.name, file);
        });
    });
    var params = $(obj).serializeArray();
    $.each(params, function (i, val) {
        formData.append(val.name, val.value);
    });
    return formData;
};

//file-tag 에서 사용
var gFileTagMap = {};
function resetFileTag(id){
	var html = gFileTagMap[id];
	if(html!=null){
		var $file = $("#"+id+"File");
		$file.before(html);
		$file.remove();
		$("#"+id+"FileView").val('');
	}
};
function setFileTag(id, value, handler, exts){
	var viewId = id+'FileView';
	if(value==null) value = "";
	else {
		var p = value.lastIndexOf('\\');
		if(p>0) value = value.substring(p+1);
	}
	var $view = $("#"+viewId);
	var oldValue = $view.val();
	$view.val(value);
	
	if(exts!=null && exts!="" && value!=""){
		if(oldValue!=value){//IE에서 클릭했을때 이벤트 타는 버그 고침
			var va = value.toLowerCase();
			var matched = false;
			extArr = exts.toLowerCase().split(",");
			extArr.each(function(index, ext){
				if(va.endsWith("."+ext.trim())){
					matched = true;
					return false;
				}
			});
			if(!matched){
				$m.msg.alertMsg("cm.msg.attach.not.support.ext",[exts]);
				resetFileTag(viewId.substring(0, viewId.length-8));
				if(handler!=null) handler(viewId.substring(0, viewId.length-8), null);
			} else {
				if(handler!=null) handler(viewId.substring(0, viewId.length-8), value);
			}
		}
	} else {
		if(handler!=null) handler(viewId.substring(0, viewId.length-8), value);
	}
};

function delFileInfo(checkedObj, id) {
	$m.msg.confirmMsg("cm.cfrm.del", null, function(result){ <% // cm.cfrm.del=삭제하시겠습니까 ? %>
		if(result){
			$area = $(checkedObj).parents('.filearea');
			if ($area.hasClass('tmp') == false) {
				$area.remove();
			}
			resetFileTag(id);
		}
	});
};

<% // 이미지 업로드 %>
function uploadImg(id, fileNm){
	if(fileNm==null) return;
	var formData = new FormData();
	formData.append(id, $('#'+id+'File')[0].files[0]);
	
	$.ajax({
        url: '/cm/transImgUploadAjx.do?module=wf&uploadId='+id,
        processData: false,
        contentType: false,
        data: formData,
        type: 'POST',
        dataType : "json",
        success: function(data){
        	if(data.model.message != null){
    			$m.dialog.alert(data.message);
    		}
        	if(data.model.imgData != null){
        		var imgData=data.model.imgData;
        		var jsonParams=JSON.parse(imgData);
        		setTmpImgId(id, jsonParams);
    		}
        	resetFileTag(id);
        }
    });
}

<% //이미지 상세보기 - 팝업 오픈 %>
function viewImagePop(id){
	var url='./viewImagePop.do?menuId=${menuId}&formNo=${param.formNo}&workNo=${param.workNo}';
	if(id!='') url+='&imgNo='+id;	
	parent.dialog.open('viewImageDialog','<u:msg titleId="st.cols.img" alt="이미지" />', url);
}<% //이미지 ID 세팅 %>
function setTmpImgId(id, params){
	$div = $('#'+id+'Area');
	if($div==null) return;
	$div.find('input[name="imgData"]').val('');
	if(id==null){
		$div.find('input[name^="image"]').eq(0).val('');
		return;
	}
	var tmpFileId=params['tmpFileId'];
	$div.find('input[type="hidden"]').eq(0).val(tmpFileId);
	previewImage($div, tmpFileId, params);
}<% //이미지 삭제 %>
function delImg(fileId){
	var target = $('#'+fileId+'Area');
	$img=target.find('img').eq(0);
	var id=$(target).attr('data-imgno');
	if(id===undefined || id=='') {
		target.find('input[name^="image"]').eq(0).val('');
		$img.attr("src", '/images/blue/photo_noimg.png');
		return;
	}
	$m.ajax('/wf/works/transImgDelAjx.do?menuId=${menuId}', {formNo:'${param.formNo}', workNo:'${param.workNo}', imgNo:id}, function(data){
		if(data.message !=null){
			$m.dialog.alert(data.message);
		}
		if(data.result == 'ok') {			
			target.find('input[name^="image"]').eq(0).val('');
			target.find('input[name="imgData"]').val('');
			$img=target.find('img#profileImg');
			$img.attr("src", '/images/blue/photo_noimg.png');
		}
	});
}<% //이미지 미리보기 %>
function previewImage(target, tmpFileId, jsonParams){
	//var jsonParams=JSON.parse(params);
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
function fnCalendar(id,opt,hm,hmId,handler){
	$m.dialog.open({
		id:id,
		noPopbody:true,
		url:'/cm/util/getCalendarPop.do?&id='+id+'&opt='+opt+'&val='+$('#'+id).val()+'&hm='+hm+'&hmId='+hmId+'&hmVal='+$('#'+hmId).val()+'&handler='+handler,
	});
}
<% // 날짜 및 시간 변경시 히든값 변경 %>
function chnDateTime(date, id){
	if(id!=undefined){
		id=id.replace('Ymd','');
		$('#'+id).val(date+':00');
	}
}
<% // 콤보박스 변경 이벤트 %>
function setSelOptions(codeNm, code, value){
	$("input[name='"+codeNm+"']").val(code);
	$("#"+codeNm+"Container #selectView span").text(value);
	$("#"+codeNm+"Open").hide();	
}
<% // [버튼클릭] - 사용자삭제  %>
function selectListDel(obj, componentId){
	if(obj===null) $('#selectListArea_'+componentId).html('');
	else $(obj).closest('div.ubox').remove();
};<% // [버튼클릭] - 사용자추가  %>
function addRowUser(componentId){
	var param = {};
	var containerId='selectListArea_'+componentId;
	var idVaList=getUserList(containerId, componentId, 'U');
	
	$m.user.selectUsers(param, function(arr){
		if(arr==null || arr.length==0) return false;
		setRowUser(containerId, componentId, arr, idVaList);
	});
	
};<% // 다중 부서 선택 %>
function addRowDept(componentId){
	var data=[];
	var containerId='selectListArea_'+componentId;
	var idVaList=getUserList(containerId, componentId, 'D');
	
	if(idVaList!=null && idVaList.length>0){
		$.each(idVaList, function(idx, id){
			data.push(JSON.parse('{"orgId":"'+id+'"}'));
		});
	}
	
	$m.org.selectOrgs({selected:data}, function(arr){
		if(arr!=null)
			setRowDept(containerId, componentId, arr, idVaList);
	});
}
<% // 사용자추가  %>
function setRowUser(containerId, componentId, arr, idVaList){	
	var buffer=[];
	arr.each(function(index, userVo){
		if($.inArray(userVo.userUid, idVaList)!=-1)
			return true;
		buffer.push(createUbox(userVo.userUid, userVo.rescNm, 'U', componentId));
	});
	if(buffer.length>0){
		$('#'+containerId).append(buffer.join(''));
	}
}<% // 부서추가  %>
function setRowDept(containerId, componentId, arr, idVaList){	
	var buffer=[];
	arr.each(function(index, orgVo){
		if($.inArray(orgVo.orgId, idVaList)!=-1)
			return true;
		buffer.push(createUbox(orgVo.orgId, orgVo.rescNm, 'D', componentId));
	});
	if(buffer.length>0){
		$('#'+containerId).append(buffer.join(''));
	}
}<% // 사용자 목록 생성  %>
function createUbox(idVa, text, pichTypCd, componentId){
	var buffer=[];
	buffer.push('<div class="ubox"><dl>');
	buffer.push('<dd class="title">');
	if(pichTypCd=='U') buffer.push('<a href="javascript:viewUserPop(\''+idVa+'\');">');
	buffer.push(text);
	if(pichTypCd=='U') buffer.push('</a>');
	buffer.push('<input type="hidden" id="'+componentId+'" value="'+idVa+'" />');
	buffer.push('</dd>');
	buffer.push('<dd class="btn" onclick="selectListDel(this);"></dd>');
	buffer.push('</dl></div>');
	return buffer.join('');
}
<% // 추가된 목록 리턴 %>
function getUserList(containerId, chkId){
	var idVaList=[];
	$('#'+containerId+' input[id="'+chkId+'"]').each(function(){
		idVaList.push($(this).val());	
	});
	return idVaList;
}
var editorId=null;
<% // 에디터(내용편집) 클릭시 에디터ID 세팅 및 편집창 실행 %>
function setEditor(id, handler){
	editorId=id;
	$m.openEditor();
}
function getEditHtml(){
	if(editorId!=null)
		return $('#'+editorId+'Area #bodyHtmlArea').html();
	return null;
};

function setEditHtml(editHtml){
	if(editorId!=null){
		$('#'+editorId+'Area #bodyHtmlArea').html(editHtml);
		$('#'+editorId).val(editHtml);
		editorId=null;
	}
};
//]]>
</script><c:if test="${!empty wfWorksLVoMap}"><u:convertJson var="dataToJson" value="${wfWorksLVoMap.jsonVa }"/></c:if>
<u:convertJson var="jsonVa" value="${wfFormRegDVo.attrVa }" 
/><!-- 모바일 목록 --><c:if test="${!empty wfFormMobDVo}"
><u:convertJson var="loutVa" value="${wfFormMobDVo.loutVa }" 
/><c:set var="loutList" value="${loutVa['loutList'] }"/><div id="tabViewArea"><c:forEach items="${loutList}" var="loutVo" varStatus="status" 
><div class="loutFormArea" id="${loutVo['loutId'] }" <c:if test="${status.index>0 }">style="display:none;"</c:if>>
	<c:if test="${!empty loutVo['list'] }">
	<c:set var="isEndYn" value="N"/>
	<div class="entryzone" ><div class="entryarea">
        <dl><c:forEach items="${loutVo['list']}" var="row" varStatus="rowStatus" >
		<c:set var="colmVo" value="${colmListMap[row.colmNm]}"
        /><c:set var="componentId" value="${colmVo.colmNm}"
        /><c:set var="colTyp" value="${colmVo.colmTypCd }" scope="request" 
        /><c:set var="jsonMap" value="${jsonVa[componentId] }" scope="request" 
        /><u:set var="emptyLangId" test="${colTyp eq 'label' }" value="label" elseValue="name"
		/><c:set var="langTitleId" value="${emptyLangId }RescVa_${_lang }" scope="request"
		/><u:set var="langTitle" test="${!empty jsonMap[langTitleId] }" value="${jsonMap[langTitleId] }" elseValue="${jsonMap[emptyLangId] }"
		/><u:set var="mandatoryYn" test="${!empty jsonMap['required'] && jsonMap['required'] eq 'Y' }" value="Y" elseValue="N"
        /><u:set var="dataVa" test="${!empty dataToJson }" value="${dataToJson[componentId] }" elseValue=""
        /><c:if test="${!empty cdListMap && fn:contains('user,dept,select,radio,check', colTyp)}"><c:set var="cdList" value="${cdListMap[componentId] }"
        /></c:if><c:if test="${isEndYn eq 'Y' }"><div class="entryzone" ><div class="entryarea"><dl><c:set var="isEndYn" value="N"/></c:if
        ><c:if test="${rowStatus.first }"><dd class="etr_tit"><u:msg title="${wfFormBVo.formNm }" alt="${wfFormBVo.formNm }" /></dd></c:if
        ><c:if test="${colTyp ne 'file' && colTyp ne 'editor' && colTyp ne 'image' && colTyp ne 'user' && colTyp ne 'dept'}">
        <c:if test="${colTyp ne 'radioSingle' && colTyp ne 'checkboxSingle' && colTyp ne 'addr' }"><dd class="etr_bodytit${mandatoryYn eq 'Y' ? '_asterisk' : ''}">${langTitle }</dd></c:if></c:if
        ><c:if test="${colTyp eq 'text' }">  
        <dd class="etr_input"><div class="etr_inputin"><c:set var="langPlaceholderId" value="placeholderRescVa_${_lang }" scope="request"
		/><u:set var="placeholder" test="${!empty jsonMap[langPlaceholderId] }" value="${jsonMap[langPlaceholderId] }" elseValue="${jsonMap['placeholder'] }"
		/><m:input id="${componentId }" title="${langTitle }" value="${!empty dataVa ? dataVa : jsonMap['startText'] }" style="${wdthStyle }" maxByte="${jsonMap['max'] }" minByte="${jsonMap['min'] }"
		mandatory="${jsonMap['required'] }" valueAllowed="${jsonMap['allow'] }" valueNotAllowed="${jsonMap['notAllow'] }" valueOption="${jsonMap['allowList'] }" readonly="${readonly }" placeholder="${placeholder }"
		/></div></dd></c:if><c:if test="${colTyp eq 'textarea' }"
		><dd class="etr_input"><div class="etr_textareain"><c:set var="langPlaceholderId" value="placeholderRescVa_${_lang }" scope="request"
		/><u:set var="placeholder" test="${!empty jsonMap[langPlaceholderId] }" value="${jsonMap[langPlaceholderId] }" elseValue="${jsonMap['placeholder'] }"
		/><m:textarea id="${componentId }" title="${langTitle }" className="etr_ta"  value="${!empty dataVa ? dataVa : jsonMap['startText'] }" style="${wdthStyle }${heightStyle }" placeholder="${placeholder }" maxByte="${jsonMap['max'] }" minByte="${jsonMap['min'] }"
		mandatory="${jsonMap['required'] }" valueAllowed="${jsonMap['allow'] }" valueNotAllowed="${jsonMap['notAllow'] }" valueOption="${jsonMap['allowList'] }" readonly="${readonly }" rows="${rows }"
		/></div></dd></c:if><c:if test="${colTyp eq 'tel' }"
		><dd class="etr_input"><div class="etr_inputin"><m:input id="${componentId }" title="${langTitle }" value="${dataVa}" maxByte="14" 
		mandatory="${jsonMap['required'] }" valueOption="number" 
		/></div></dd></c:if><c:if test="${colTyp eq 'date' }"
		><u:set var="option" test="${!empty jsonMap['allowAfterRegDt'] && jsonMap['allowAfterRegDt']=='Y' }" value="{checkHandler:onChgTodayChk}" elseValue="null"
		/><dd class="etr_select"><div <c:if test="${!empty jsonMap['required'] && jsonMap['required'] eq 'Y' }">class="mandatory" data-validateType="calendar" data-name="${componentId }" data-title="${langTitle}"</c:if>>
	             <div class="etr_calendar">
	             	<u:set var="dateVa" test="${!empty dataVa}" value="${dataVa }" elseValue="${!empty jsonMap['startText'] ? jsonMap['startText'] : !empty jsonMap['defaultRegDt'] ? today : '' }"/>
	             	<input id="${componentId}" name="${componentId}" value="${dateVa}" type="hidden" />
	                 <div class="select_in1" onclick="fnCalendar('${componentId}','','','');">
	                 <dl>
	                 <dd class="ctxt"><span id="${componentId}">${dateVa}</span></dd>
	                 <dd class="cbtn"></dd>
	                 </dl>
	                 </div>
	             </div></div>
	        </dd> 
		</c:if><c:if test="${colTyp eq 'period' }"
		><u:set var="option" test="${!empty jsonMap['allowAfterRegDt'] && jsonMap['allowAfterRegDt']=='Y' }" value="{checkHandler:onChgTodayChk}" elseValue="null"
		/><c:set var="periodStrtId" value="${componentId }-1"
		/><c:set var="periodEndId" value="${componentId }-2"
		/><u:convertMap var="periodVa1" srcId="dataToJson" attId="${periodStrtId }" 
		/><u:convertMap var="periodVa2" srcId="dataToJson" attId="${periodEndId }" 
		/><dd class="etr_select">
                <div class="etr_calendar_lt">
                    <div class="etr_calendar">
                    	<c:set var="periodVa1" value="${!empty periodVa1 ? periodVa1 : !empty jsonMap['startText1'] ? jsonMap['startText1'] : !empty jsonMap['defaultRegDt'] ? today : ''}"/>
                    	<input id="${periodStrtId}" name="${periodStrtId}" value="${periodVa1 }" type="hidden"/>
                        <div class="etr_calendarin">
                        <dl>
                        <dd class="ctxt" onclick="fnCalendar('${periodStrtId}','','','');"><span id="${periodStrtId}">${periodVa1 }</span></dd>
                        <dd class="cdelete" onclick="fnTxtDelete(this);"></dd>
                        <dd class="cbtn" onclick="fnCalendar('${periodStrtId}','','','');"></dd>
                        </dl>
                        </div>
                    </div>
                </div>
                <div class="etr_calendar_rt">
                    <div class="etr_calendar">
                    	<c:set var="periodVa2" value="${!empty periodVa2 ? periodVa2 : !empty jsonMap['startText2'] ? jsonMap['startText2'] : ''}"/>
                    	<input id="${periodEndId}" name="${periodEndId}" value="${periodVa2 }" type="hidden" />
                        <div class="etr_calendarin">
                        <dl>
                        <dd class="ctxt" onclick="fnCalendar('${periodEndId}','','','');"><span id="${periodEndId}">${periodVa2 }</span></dd>
                        <dd class="cdelete" onclick="fnTxtDelete(this);"></dd>
                        <dd class="cbtn" onclick="fnCalendar('${periodEndId}','','','');"></dd>
                        </dl>
                        </div>
                    </div>
                </div>
            </dd></c:if><c:if test="${colTyp eq 'time' }"
		><dd class="etr_input" style="width:100px;"><div class="etr_inputin"><m:time id="${componentId }" title="${langTitle }" /></div></dd
		></c:if><c:if test="${colTyp eq 'datetime' }"
		><dd class="etr_select"><div <c:if test="${!empty jsonMap['required'] && jsonMap['required'] eq 'Y' }">class="mandatory" data-validateType="calendar" data-name="${componentId }" data-title="${langTitle}"</c:if>>
             <div class="etr_calendar">
             	<u:set var="dateVa" test="${!empty dataVa}" value="${dataVa }" elseValue="${!empty jsonMap['startText'] ? jsonMap['startText'] : !empty jsonMap['defaultRegDt'] ? todaytime : '' }"/>             	
             	<u:out value="${dateVa}" type="date" var="dateYmd" />
				<u:out value="${dateVa}" type="hm" var="dateTime" />
             	<input id="${componentId}Hm" value="${dateTime}" type="hidden" />
             	<input id="${componentId}Ymd" value="${dateYmd}" type="hidden" />             	
             	<input type="hidden" name="${componentId}" id="${componentId}" value="${dateVa}" />
             	<div class="etr_calendarin">
                <dl>
                <dd class="ctxt" onclick="fnCalendar('${componentId}Ymd','','m','${componentId}Hm', 'chnDateTime');"><span id="${componentId}Ymd"><u:out value="${dateVa}" type="longdate" /></span></dd>
                <dd class="cdelete" onclick="fnTxtDelete(this);"></dd>
                <dd class="cbtn" onclick="fnCalendar('${componentId}Ymd','','m','${componentId}Hm', 'chnDateTime');"></dd>
                </dl>
                </div>
             </div></div>
        </dd></c:if><c:if test="${colTyp eq 'number' || colTyp eq 'calculate'}"
        ><u:set var="readonly" test="${colTyp eq 'calculate' }" value="Y" elseValue="${readonly }"
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
		/><dd class="etr_input"><div class="etr_inputin"><m:input id="${idPrefix }${componentId }" title="${langTitle }" value="${!empty dataVa ? dataVa : jsonMap['startText'] }" style="${wdthStyle }text-align:right;" placeholder="${placeholder }" maxByte="${jsonMap['max'] }" minByte="${jsonMap['min'] }"
		mandatory="${jsonMap['required'] }" valueOption="number" commify="${jsonMap['comma'] }" readonly="${readonly }" dataList="${dataCell }${dataFormat}${dataFormula }"
		/></div></dd></c:if><c:if test="${colTyp eq 'user' || colTyp eq 'dept'}"
		><dd class="etr_bodytit${mandatoryYn eq 'Y' ? '_asterisk' : '' }">
			<div class="icotit_dot${mandatoryYn eq 'Y' ? '_asterisk' : '' }">${langTitle}</div>
			<div id="deptBtnArea" class="icoarea" style="left:40%;">
				<dl>
				<c:if test="${colTyp == 'user' }"><dd class="btn" onclick="addRowUser('${componentId }');"><u:msg titleId="cm.btn.add" alt="추가"/></dd>
				<dd class="btn" onclick="selectListDel(null, '${componentId }');"><u:msg titleId="cm.btn.allDel" alt="전체삭제"/></dd></c:if>
				<c:if test="${colTyp == 'dept' }"><dd class="btn" onclick="addRowDept('${componentId }');"><u:msg titleId="cm.btn.add" alt="추가"/></dd>
				<dd class="btn" onclick="selectListDel(null, '${componentId }');"><u:msg titleId="cm.btn.allDel" alt="전체삭제"/></dd></c:if>
				</dl>
			</div>
			</dd><dd class="etr_input"><div <c:if test="${mandatoryYn eq 'Y' }">class="mandatory" data-validateType="ubox" data-title="${langTitle}"</c:if>
			><div class="etr_bodyline" id="selectListArea_${componentId }" style="min-height:40px;overflow-y:auto;" data-title="${langTitle}"
			><u:set var="davaIdList" test="${colTyp == 'dept' }" value="${!empty cdList ? cdList[0] : !empty jsonMap['defaultRegrUid'] ? sessionScope.userVo.deptId :  jsonMap['orgIds'] }" elseValue="${!empty cdList ? cdList[0] : !empty jsonMap['defaultRegrUid'] ? sessionScope.userVo.userUid :  jsonMap['userUids'] }"
			/><c:if test="${!empty davaIdList }"><u:set var="davaNmList" test="${colTyp == 'dept' }" value="${!empty cdList ? cdList[1] : !empty jsonMap['defaultRegrUid'] ? sessionScope.userVo.deptNm :  jsonMap['orgNms'] }" elseValue="${!empty cdList ? cdList[1] : !empty jsonMap['defaultRegrUid'] ? sessionScope.userVo.userNm :  jsonMap['userNms'] }"
			/><c:forTokens var="dataIds" items="${davaIdList }" delims="," varStatus="dataStatus"
			><div class="ubox"><dl><dd 
		class="title"><c:if test="${colTyp == 'user' }"><a href="javascript:viewUserPop('${dataIds[dataStatus.index] }');">${fn:split(davaNmList, ',')[dataStatus.index] }</a><input type="hidden" id="${componentId }" value='${dataIds[dataStatus.index] }' /></c:if
		><c:if test="${colTyp == 'dept' }">${fn:split(davaNmList, ',')[dataStatus.index] }<input type="hidden" id="${componentId }" value='${dataIds[dataStatus.index] }' /></c:if></dd>
		<dd class="btn" onclick="selectListDel(this);"></dd></dl></div
		></c:forTokens></c:if></div></div></dd
		></c:if><c:if test="${colTyp eq 'radio' }"
		><dd class="etr_input"><div <c:if test="${!empty jsonMap['required'] && jsonMap['required'] eq 'Y' }">class="mandatory" data-validateType="calendar" data-name="${componentId }" data-title="${langTitle}"</c:if>>
			<div class="etr_ipmany" id="${componentId }RadioArea" style="display:table-cell;">
                <dl><c:if test="${empty jsonMap['chkTypCd'] }"><c:forTokens var="chkOpt" items="${jsonMap['chkList'] }" delims="${enter }" varStatus="chkStatus"
                ><u:set var="chkVa" test="${fn:substring(chkOpt,0,1) eq lineEnter }" value="${fn:substringAfter(chkOpt,lineEnter) }" elseValue="${chkOpt }"
				/><m:check type="radio" id="${componentId }-${chkStatus.index}" name="${componentId }" inputId="${componentId }_${chkStatus.index}" areaId="${componentId }RadioArea" value="${chkVa }" checked="${(empty dataVa && chkStatus.first) || dataVa == chkVa }" title="${chkVa }" 
                /></c:forTokens>                	
				</c:if><c:if test="${!empty jsonMap['chkTypCd'] && !empty formCdListMap}"
				><u:convertMap srcId="formCdListMap" attId="CD_LIST_${jsonMap['chkTypCd']}" var="wfCdGrpBVoList" /><c:forEach var="wfCdGrpBVo" items="${wfCdGrpBVoList }" varStatus="chkStatus"
				><u:set var="checked" test="${!empty dataVa && fn:contains(dataVa, wfCdGrpBVo.cdId) }" value="Y" elseValue="N"
				/><m:check type="radio" id="${componentId }-${chkStatus.index}" name="${componentId }" inputId="${componentId }-${chkStatus.index}" areaId="${componentId }RadioArea" value="${wfCdGrpBVo.cdId }" checked="${(empty dataVa && chkStatus.first) || dataVa == wfCdGrpBVo.cdId }" title="${wfCdGrpBVo.cdRescNm }" 
				/></c:forEach><m:input type="hidden" id="${componentId }_cdYn" value="Y"/></c:if>
				</dl></div></div></dd></c:if
		><c:if test="${colTyp eq 'checkbox' }"
		><dd class="etr_input"><div <c:if test="${!empty jsonMap['required'] && jsonMap['required'] eq 'Y' }">class="mandatory" data-validateType="calendar" data-name="${componentId }" data-title="${langTitle}"</c:if>>
			<div class="etr_ipmany" style="display:table-cell;">
                <dl><c:if test="${empty jsonMap['chkTypCd'] }"><c:forTokens var="chkOpt" items="${jsonMap['chkList'] }" delims="${enter }" varStatus="chkStatus"
		><u:set var="chkVa" test="${fn:substring(chkOpt,0,1) eq lineEnter }" value="${fn:substringAfter(chkOpt,lineEnter) }" elseValue="${chkOpt }"
		/><u:set var="checked" test="${!empty dataVa && fn:contains(dataVa, chkVa) }" value="Y" elseValue="N"
		/><m:check type="checkbox" id="${componentId }-${chkStatus.index }" name="${componentId }" inputId="${componentId }" value="${chkVa}" checked="${checked=='Y' }" title="${chkVa}" 
		/></c:forTokens></c:if
		><c:if test="${!empty jsonMap['chkTypCd'] && !empty formCdListMap}"
		><u:convertMap srcId="formCdListMap" attId="CD_LIST_${jsonMap['chkTypCd']}" var="wfCdGrpBVoList" /><c:forEach var="wfCdGrpBVo" items="${wfCdGrpBVoList }" varStatus="chkStatus"
		><u:set var="checked" test="${!empty dataVa && fn:contains(dataVa, wfCdGrpBVo.cdId) }" value="Y" elseValue="N"
		/><m:check type="checkbox" id="${componentId }-${chkStatus.index }" name="${componentId }" inputId="${componentId }" value="${wfCdGrpBVo.cdId}" checked="${checked=='Y' }" title="${wfCdGrpBVo.cdRescNm}" 
		/></c:forEach><m:input type="hidden" id="${componentId }_cdYn" value="Y"/></c:if
		></dl></div></div></dd></c:if><c:if test="${colTyp eq 'select' }"
		><c:set var="selectNm" value=""/>
	    <c:set var="selectValue" value=""/>
	    <dd class="dd_blank5"></dd>
		<dd class="etr_input" id="${componentId }Container">
              <div class="etr_ipmany">
              <dl>
              <dd class="etr_se_lt">
                  <div class="etr_open2" id="${componentId }Open" style="display:none;">
                    <div class="open_in1">
                        <div class="open_div">
					        <dl><c:if test="${empty jsonMap['chkTypCd'] }"
					        ><c:forTokens var="chkOpt" items="${jsonMap['chkList'] }" delims="${enter }" varStatus="chkStatus"
							><c:if test="${(empty dataVa && chkStatus.first) || chkOpt == dataVa}"><c:set var="selectNm" value="${chkOpt }"/><c:set var="selectValue" value="${chkOpt }"/></c:if>
							<c:if test="${!chkStatus.first }"><dd class="line"></dd></c:if>
		                    <dd class="txt" onclick="setSelOptions('${componentId}',$(this).attr('data-code'),$(this).text());" data-code="${chkOpt}">${chkOpt }</dd
		                    ></c:forTokens></c:if><c:if test="${!empty jsonMap['chkTypCd'] && !empty formCdListMap}"
							><u:convertMap srcId="formCdListMap" attId="CD_LIST_${jsonMap['chkTypCd']}" var="wfCdGrpBVoList" 
							/><c:forEach var="wfCdGrpBVo" items="${wfCdGrpBVoList }" varStatus="chkStatus"
							><c:if test="${(empty dataVa && chkStatus.first) || wfCdGrpBVo.cdId == dataVa}"><c:set var="selectNm" value="${wfCdGrpBVo.cdRescNm }"/><c:set var="selectValue" value="${wfCdGrpBVo.cdId }"/></c:if>
							<c:if test="${!chkStatus.first }"><dd class="line"></dd></c:if>
		                    <dd class="txt" onclick="setSelOptions('${componentId}',$(this).attr('data-code'),$(this).text());" data-code="${wfCdGrpBVo.cdId}">${wfCdGrpBVo.cdRescNm }</dd
		                    ></c:forEach><m:input type="hidden" id="${componentId }_cdYn" value="Y"/></c:if>
					    	</dl>
                        </div>
                    </div>
                </div>
                <input type="hidden" name="${componentId }" value="${selectValue }" class="notChkCls"/>
                <div class="select_in1" onclick="$('#${componentId}Open').toggle();">
                <dl>
                <dd class="select_txt" id="selectView"><span>${selectNm }</span></dd>
                <dd class="select_btn"></dd>
                </dl>
                </div>
            </dd>
            </dl>
            </div>
        </dd><dd class="etr_blank1"></dd></c:if
        ><c:if test="${colTyp eq 'image' }"
		><u:set var="viewSizeTyp" test="${!empty jsonMap['viewSizeTyp'] && jsonMap['viewSizeTyp'] eq '%' }" value="per" elseValue="px" 
		/><u:set var="maxWdth" test="${!empty jsonMap['viewWdth'] }" value="${jsonMap['viewWdth'] }" elseValue="88" 
		/><u:set var="maxHght" test="${!empty jsonMap['viewHeight'] }" value="${jsonMap['viewHeight'] }" elseValue="${viewSizeTyp eq 'px' ? '110' : '100' }" 
		/><c:if test="${!empty wfWorksLVoMap && !empty wfWorksLVoMap.imgListMap[componentId] }"><c:set var="imgVo" value="${wfWorksLVoMap.imgListMap[componentId]}"
		/></c:if><c:if test="${!empty imgVo}"><u:set test="${!empty imgVo.imgPath}" var="imgPath" value="${_cxPth}${imgVo.imgPath}" elseValue="${_cxPth}/images/${_skin}/photo_noimg.png" 
		/><c:if test="${viewSizeTyp eq 'px'}"><u:set test="${imgVo.imgWdth <= maxWdth}" var="imgWdth" value="${imgVo.imgWdth}" elseValue="${maxWdth}" 
		/><u:set test="${imgVo.imgHght <= maxHght}" var="imgHght" value="${imgVo.imgHght}" elseValue="${maxHght}" 
		/><u:set test="${imgVo.imgWdth < imgVo.imgHght}" var="imgWdthHgth" value="height='${imgHght}'" elseValue="width='${imgWdth}'" 
		/></c:if><c:if test="${viewSizeTyp eq 'per'}"
		><u:set test="${maxWdth < maxHght}" var="imgWdthHgth" value="height='${maxHght}%'" elseValue="width='${maxWdth}%'" 
		/></c:if></c:if><dd class="etr_bodytit image_profile"><c:if test="${!empty imgVo}"><m:input type="hidden" id="imgNo" value="${imgVo.imgNo }"/></c:if
		><div class="icotit_dot">${langTitle }</div>
		<u:set var="exts" test="${!empty jsonMap['allowFile'] }" value="${jsonMap['allowFile'] }" elseValue="gif,jpg,jpeg,png,tif"
		/><u:set var="photoMsgId" test="${empty imgVo }" value="cm.btn.add" elseValue="cm.btn.chg"/>
        <m:fileBtn id="${componentId }Img" titleId="${photoMsgId }" alt="변경" exts="${exts }" onchange="uploadImg" delBtn="delImg" accept="image/*"/>
        </dd>
        <dd class="etr_body" ><u:set var="dataList" test="${!empty imgVo }" value=" data-imgno=\"${imgVo.imgNo }\"" elseValue=""
        	/><div id="${componentId }ImgArea" data-viewSizeTyp="${viewSizeTyp }" data-maxwidth="${maxWdth }" data-maxheight="${maxHght }"${dataList } style="padding:0 0 0 3px;"
        	><m:input type="hidden" id="${componentId }"
			/><m:input type="hidden" id="imgData" value=""
			/><c:if test="${!empty imgVo}"	><img id="profileImg" src="${imgPath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' ${imgWdthHgth} ></c:if
        	><c:if test="${empty imgVo }"><img id="profileImg" src="${_cxPth}/images/${_skin}/photo_noimg.png" width="88px"></c:if>
			</div>
        </dd><c:if test="${!empty jsonMap['reSizeText'] }" 
			><dd class="etr_body"><u:msg titleId="pt.jsp.setSkinImg.recommend" alt="권장사이즈"/> ${jsonMap['reSizeText'] }</dd></c:if></c:if>
		
		
		<c:if test="${colTyp eq 'editor' }">
        <dd class="etr_bodytit" ><textarea id="${componentId }" name="${componentId }" style="display:none" data-title="${langTitle }"><u:out value="${dataVa }" type="value"/></textarea>
         	<div class="icotit_dot">${langTitle }</div>
            <div class="icoarea">
            <dl>
            <dd class="btn" onclick="setEditor('${componentId }');"><u:msg titleId="mcm.title.editCont" alt="내용편집" /></dd>
            </dl>
            </div>
         </dd>
         <dd class="etr_input" id="${componentId }Area"><div class="etr_bodyline editor" id="bodyHtmlArea"><c:if test="${!empty dataVa }"><u:out value="${dataVa}" type="noscript" /></c:if></div></dd></c:if
         ><c:if test="${colTyp eq 'file' }"
         ><dd class="etr_bodytit">
               <div class="icotit_dot">${langTitle }</div>
               <div class="icoarea">
               <dl>
               <dd class="btn" onclick="addFile('${filesId}');"><u:msg titleId="cm.btn.fileAtt" alt="파일첨부"  /></dd>
               </dl>
               </div>
           </dd>
           <c:if test="${isEndYn eq 'N' }"></dl></div></div><m:files id="${filesId}" fileVoList="${fileVoList}" module="wf" mode="set" exts="${exts }" extsTyp="${extsTyp }"/><c:set var="isEndYn" value="Y"/></c:if>
           
           </c:if>
       </c:forEach><c:if test="${isEndYn eq 'N' }"></dl></div></div></c:if></c:if></div></c:forEach></div>
</c:if>
