<%@ tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="java.util.HashSet,com.innobiz.orange.web.pt.secu.Browser"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"
%><%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"
%><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"
%><%@ attribute name="id"			required="true" rtexprvalue="true"
%><%@ attribute name="module"		required="true"
%><%@ attribute name="mode"			required="true"
%><%@ attribute name="actionParam"			required="false"
%><%@ attribute name="fileVoList"	 required="false" type="java.util.List"
%><%@ attribute name="urlParam"	 required="false" 
%><%@ attribute name="ctSendYn"	 required="false" 
%><%@ attribute name="exts"  required="false"
%><%@ attribute name="extsTyp"  required="false"
%><%@ attribute name="height"  required="false"
%><%@ attribute name="urlSuffix"  required="false"
%><%@ attribute name="fileSizeModule"  required="false"
%><%

/**
파일 업로드/다운로드
설명		: 여러 개의 파일을 업로드/다운로드 한다.

id			: DIV 태그의 id로 (${id}Area) 사용되고, 
			 hidden/file 태그의 접두어로 사용된다. (${id}_fileId, ${id}_valid, ${id}_useYn, ${id}_file)
module		: 파일 다운로드 URI를 호출할 때 다음과 같이 사용된다.
			 '/${module}/downFile.do'
mode		: 조회(view), 등록/수정(set)
fileVoList	: CommonFileVo 리스트, 조회나 수정 화면의 기등록 파일 목록.
exts : 확장자명
extsTyp : 허용 또는 제한여부
*/

	// fileVoList attribute가 있으면 request scope에 있는 fileVoList를 무시함.
	if (fileVoList != null) {
		request.setAttribute("fileVoList", fileVoList);
	}
	// 파일 확장자 Set
	HashSet<String> extSet = new HashSet<String>();
	extSet.add("zip");
	extSet.add("egg");
	extSet.add("pptx");
	extSet.add("ppt");
	extSet.add("doc");
	extSet.add("xls");
	extSet.add("xlsx");
	extSet.add("pdf");
	extSet.add("psd");
	extSet.add("bmp");
	extSet.add("png");
	extSet.add("jpg");
	extSet.add("gif");
	extSet.add("html");
	extSet.add("txt");
	extSet.add("file");
	request.setAttribute("extSet", extSet);
	
	if(urlParam==null) urlParam = "";
	
	Browser browser = (Browser)request.getAttribute("browser");
	
	if(height==null) height="90";
	
	if(fileSizeModule==null || fileSizeModule.isEmpty()) fileSizeModule=module;
%>
<style type="text/css">
<!--
.sizeZero { width: 0; height: 0; }
.dispNone { display: none; }
--> 
</style>
<!-- 멀티파일여부 -->
<u:set var="paramQueryString" test="${!empty urlParam}" value="menuId=${empty menuId ? param.menuId : menuId }&${urlParam }" elseValue="menuId=${empty menuId ? param.menuId : menuId }"/>
<c:set var="uploadUrl" value="/cm/transFileUploadAjx.do?${paramQueryString }&module=${module }&uploadId=${id}_file"/>
<%-- <c:set var="downYn" value="<%=downYn %>"/>
<c:set var="viewYn" value="<%=viewYn %>"/> --%>
<script type="text/javascript">
<!--
var uploader='${mode}'=='set' && isMultiFile() ? newUploader('${id}', true) : null;
//var uploader=null;
var seqNo=0;
<% // 확장자 배열 %>
var extArr = [];
<c:forEach items="${extSet}" var="ext" varStatus="status">extArr.push('${ext}');</c:forEach>
<% // 파일 초기화 %>
function resetFile(obj){
	if(obj==undefined) return;
	var inputFile=$(obj).clone(false);
	inputFile.val('');
	$(obj).before(inputFile);
	$(obj).remove();
}
<% // 파일 추가 %>
function addFile() {
	$('#${id}_file').trigger('click');
}

<% // 파일 이름 생성 [extsTyp='A':허용,extsTyp='L':제한]%>
function setFileDisp(obj, va){
	if (va==undefined || va=='') return;	
	
	//restoreUniform('${id}Area');
	var $last = $('.attacharea:last');
	var $clone = $last.clone();
	$last.removeClass('dispNone');
	var p = va.lastIndexOf('\\');
	if (p > 0) va = va.substring(p + 1);
	$last.find('#${id}_fileView').text(va).removeAttr('id');
	//$last.find('#${id}_file').removeAttr('id');
	$last.find('input[name="${id}_valid"]').val('Y');
	p = va.lastIndexOf('.');
	if (p > 0) {
		var ext = va.substring(p + 1);
		if (extArr.contains(ext)) {
			var img = $last.find('.attach_img img');
			var src = img.attr('src');
			img.attr('src', src.replace('ico_file', 'ico_' + ext));
		}
	}
	$last.find('input[name="${id}_fGrpNo"]').val(seqNo);
	if(obj.fileId!=undefined)
		$last.appendHidden({name:'fileId',value:obj.fileId});
	$clone.insertAfter($last);
	$("#${id}Area").trigger($.Event('resize'));
	setUniformCSS($last);
	//applyUniform('${id}Area');
}

<% // 확장자 체크 %>
function notChkFiles(va, extsList, extsTyp){
	if(extsList==null || extsList=='') return;
	var matched = false;
	extsList.each(function(index, ext){
		if(va.toLowerCase().endsWith("."+ext.trim())){
			matched = true;
			return false;
		}
	});
	if(!matched && extsTyp != undefined && extsTyp == 'A'){
		return true;
	}
	if(matched && extsTyp != undefined && extsTyp == 'L'){
		return true;
	}
	return false;
}

<% // 파일 객체 생성 %>
function addInputFile(obj){
	$area = $(obj).parents('.${id}attachBtnArea');
	//var $cloneArea = $area.clone();
	var $cloneArea = $('#${id}attachBtnAreaParent').clone();
	$cloneArea.replaceWith( $cloneArea.clone(true) );
	$cloneArea.insertBefore($area);
	$cloneArea.attr('style',$area.attr('style'));
	var id = $area.attr('id');
	var seqNo = id.substring(id.lastIndexOf('_')+1);
	//$last.find('input[name="${id}_fGrpNo"]').val(fGrpNo);
	var cloneSeq=parseInt(seqNo)+1;
	$cloneArea.attr('id','${id}attachBtnArea_'+cloneSeq);
	
	var inputFile=$cloneArea.find('input[type="file"]');
	inputFile.attr('id', '${id}_file');
	inputFile.attr('name', '${id}_file');
	$area.addClass('dispNone');
	//$clone.insertAfter($last);
	//$("#${id}Area").trigger($.Event('resize'));
	//setUniformCSS($last);
	return seqNo;
}

<% // 파일 선택 %>
function setFile(obj, exts, extsTyp) {
	// 파일객체 생성
	var extsList = exts.toLowerCase().split(",");
	if(uploader==null){
		if(notChkFiles($(obj).val(), extsList, extsTyp)) {
			if(extsTyp != undefined && extsTyp == 'A'){
				alertMsg('cm.msg.attach.not.support.ext',[exts]);
			}else if(extsTyp != undefined && extsTyp == 'L'){
				alertMsg('cm.msg.attach.not.limit.ext',[exts]);
			} 
			resetFile(obj);
		}else{
			seqNo=addInputFile(obj);
			setFileDisp(obj, $(obj).val());
		}
	}else{
		var notFileList = [];
		var files = obj.files;
		if(files.length>0){
			$('#dragArea').hide();
			if(uploader.fileListMap==null) uploader.fileListMap = new ParamMap();
			var fileList = [];
			//fileListMap.put('fileList_'+fGrpNo, files); // 전체를 넣는게 아니라 용량 초과된 객체는 제거 필요
			for(var i=0;i<files.length;i++){
				if(notChkFiles(files[i].name, extsList, extsTyp)){
					notFileList.push(files[i].name);
					continue;
				}
				
				if(uploader.maxSize!=null && uploader.maxSize<uploader.fileSize+files[i].size){
					alertMsg('cm.msg.upload.totalFileLimit', [bytesToSize(uploader.maxSize, false)]); // cm.msg.upload.totalFileLimit=전체 파일의 첨부 용량을 초과 하였습니다.({0} M-Bytes)
					break;
				}
				uploader.fileSize+=sizeToBytes(bytesToSize(files[i].size));
				uploader.setMultiFileDisp(null, files[i], null);
				fileList.push(files[i]);
				uploader.fMaxNo++;
			}
			if(fileList.length>0){
				uploader.fileListMap.put('fileList_'+uploader.fGrpNo, fileList);
				uploader.updateSize();
				uploader.fGrpNo++;
			}
			if(notFileList!=null && notFileList.length>0){
				if(extsTyp != undefined && extsTyp == 'A'){
					alertMsg('cm.msg.attach.not.support.ext',[exts]);
				}else if(extsTyp != undefined && extsTyp == 'L'){
					alertMsg('cm.msg.attach.not.limit.ext',[exts]);
				}				
			}
			resetFile($('#${id}_file'));
		}
	}
	
}
<% // 파일 삭제 %>
function delFile(isAll) {
	var $checked = isAll!=undefined && isAll ? $('.fileItem input[name="fileChk"]') : $('.fileItem input:checked');
	
	if (isAll===undefined && $checked.length == 0) {
		<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
		alertMsg("cm.msg.noSelect");
		return;
	}
	var isUploader = uploader!=null;
	var grpNo, seqNo;
	$checked.each(function() {
		$area = $(this).parents('.fileItem');			
		grpNo = $area.find('input[name="${id}_fGrpNo"]').val();
		if ($area.hasClass('dispNone') == false) {
			$area.find('input[name="${id}_useYn"]').val('N');
			if(uploader==null){							
				$area.find('input[type="file"]').remove();
				if(grpNo != undefined){
					$('#${id}attachBtnArea_'+grpNo).remove();
				}
			}else{
				if(uploader.delListMap==null) uploader.delListMap=new ParamMap();
				seqNo = $area.find('input[name="${id}_fSeqNo"]').val();
				if(seqNo!='') {
					uploader.delListMap.put('fileList_'+grpNo+'-item'+seqNo, true);
					
					var size = uploader.fileSizeMap.get('fileList_'+grpNo+'-item'+seqNo);
					if(size!=null) {
						uploader.fileSize-=sizeToBytes(bytesToSize(size));
						if(uploader.fileSize<0) uploader.fileSize=0;
						uploader.updateSize();
					}
				}
			}
			$area.addClass('dispNone');
			//$area.hide();
			
		}
	});
	checkAllCheckbox('${id}Area', false);
	if(isUploader && $('.fileItem').not('.dispNone').length==0) $('#dragArea').show();
}
  
<% // 파일 다운로드 %>
function downFile(id) {
	var ids = [];
	if (id == undefined) {
		var $checked = $('.fileItem input:checked');
		if ($checked.length == 0) {
			<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
			alertMsg("cm.msg.noSelect");
			return;
		}
		$checked.each(function() {
			ids.push($(this).val());
		});
	} else {
		ids.push(id);
	}
	var $form = $('<form>', {
			'method':'post',
			'action':'/${module}${urlSuffix}/downFile.do?${urlParam}',
			'target':'dataframe'
		}).append($('<input>', {
			'name':'menuId',
			'value':'${empty menuId ? param.menuId : menuId}',
			'type':'hidden'
		})).append($('<input>', {
			'name':'fileIds',
			'value':ids,
			'type':'hidden'
		}));
	if('${actionParam}' != null) $form.append($('<input>',{'name':'actionParam','value':'${actionParam}','type':'hidden'}));
	$('form[action="/${module}${urlSuffix}/downFile.do"]').remove();
	$(document.body).append($form);
	$form.submit();
}
<c:if test="${viewYn eq 'Y'}">
<% // 미리보기 %>
function attachViewPop(id, fileExt) {
	if(fileExt==undefined || fileExt=='') return;
	var url = './attachViewAjx.do?menuId=${empty menuId ? param.menuId : menuId}'
	url+='&fileIds='+id;
	if('${urlParam}' != '') url+= '&${urlParam}';
	callAjax(url, null, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.fn != null && data.rs != null) {
			var url = '/viewer/skin/doc.html?fn='+data.fn+'&rs=/'+data.rs;
			window.open(url);
		}
	});
}
</c:if>

<% // 파일목록 로드 %>
function loadFileList(){
	var fileList=[];
	<c:forEach items="${fileVoList}" var="fileVo" varStatus="status">
		fileList.push({
			dispNm:'<u:out value="${fileVo.dispNm}" type="value"/>',
			fileId:'${fileVo.fileId}',
			useYn:'${fileVo.useYn}',
			fileSize:'${fileVo.fileSize}',
			fileExt:'${fileVo.fileExt}'
	    });
	</c:forEach>
	return fileList;
}
<% // 파일목록 세팅 %>
function addMultiArea(buffer, index, fileVo, fileSizeUnit){
	buffer.push('<li id="fileList" class="fileItem'+(fileVo==null ? ' dispNone' : '')+'">');
	buffer.push('<span class="file_select"><input type="checkbox" name="fileChk"></span>');
	buffer.push('<span class="file_name"><label for="file_name_'+(index!=null ? index : 0)+'">');
	if(fileVo!=null){
		buffer.push('<span class="file_icon '+(extArr.contains(fileVo.fileExt) ? fileVo.fileExt : 'file')+'"></span><span class="title">'+fileVo.dispNm+'</span></label>');
		buffer.push('</span>');
		buffer.push('<span class="status"><u:msg titleId="cm.msg.complete" alt="완료"/></span>');
	}else{
		buffer.push('<span class="file_icon"></span><span class="title"></span></label>');
		buffer.push('</span>');
		buffer.push('<span class="status progress" ><img src="${_ctx}/images/${_skin}/pollbar.png" width="0%" height="10"></span>');
	}
	
	buffer.push('<span class="file_size">'+(fileSizeUnit!=null ? fileSizeUnit : '')+'</span>');
	buffer.push('<input type="hidden" name="${id}_fGrpNo" value="'+(fileVo!=null ? uploader.fGrpNo : '')+'" />');
	buffer.push('<input type="hidden" name="${id}_fSeqNo" value="'+(fileVo!=null ? index : '')+'" />');
	buffer.push('<input type="hidden" name="${id}_fileId" value="'+(fileVo!=null ? fileVo.fileId : '')+'" />');
	if(fileVo!=null){
		<u:set var="ctSendYn" test="${ctSendYn == 'Y' }" value="Y" elseValue="N"/>
		buffer.push('<input type="hidden" name="${id}_valid" value="${ctSendYn}" />');
		buffer.push('<input type="hidden" name="${id}_useYn" value="'+fileVo.useYn+'" />');
		buffer.push('<input type="hidden" name="${id}_ctSendYn" value="${ctSendYn}" />');	
	}else{
		buffer.push('<input type="hidden" name="${id}_valid" value="N" />');
		buffer.push('<input type="hidden" name="${id}_useYn" value="Y" />');
		buffer.push('<input type="hidden" name="${id}_ctSendYn" value="" />');
	}
	
	buffer.push('<input type="hidden" name="tmpFileId" value="" />');
	buffer.push('</li>');
}

<% // 파일목록 세팅 %>
function addSingleArea(buffer, index, fileVo, fileSizeUnit){
	buffer.push('<div class="attacharea fileItem'+(fileVo==null ? ' dispNone' : '')+'">');
	buffer.push('<dl>');
	<c:if test="${mode ne 'view' || (mode eq 'view' && (empty downYn || downYn eq 'Y'))}">
	buffer.push('<dd class="attach_check"><input type="checkbox" name="fileChk" value="'+(fileVo!=null ? fileVo.fileId : '')+'" /></dd>');
	</c:if>
	buffer.push('<dd class="attach_img">');
	if(fileVo!=null){
		<c:if test="${viewYn eq 'Y' }">
		buffer.push('<a href="javascript:attachViewPop('+fileVo.fileId+');"><img src="${_cxPth}/images/cm/ico_'+(extArr.contains(fileVo.fileExt) ? fileVo.fileExt : 'file')+'.png"/></a>');
		</c:if>
		<c:if test="${viewYn ne 'Y' }">
		buffer.push('<img src="${_cxPth}/images/cm/ico_'+(extArr.contains(fileVo.fileExt) ? fileVo.fileExt : 'file')+'.png"/>');
		</c:if>
	}else buffer.push('<img src="${_cxPth}/images/cm/ico_file.png"/>');
	buffer.push('</dd>');
	buffer.push('<dd class="attach">');
	if(fileVo!=null){	
		<c:if test="${empty downYn || downYn eq 'Y'}">
		buffer.push('<a href="javascript:downFile('+fileVo.fileId+');">'+fileVo.dispNm+'('+fileSizeUnit+')</a>');
		</c:if><c:if test="${!empty downYn && downYn ne 'Y' }">
		buffer.push(fileVo.dispNm+'('+fileSizeUnit+')';
		</c:if>
	}else buffer.push('<span id="${id}_fileView"></span>');
	buffer.push('</dd>');
	buffer.push('<dd class="sizeZero">');
	buffer.push('<input type="hidden" name="${id}_fileId" value="'+(fileVo!=null ? fileVo.fileId : '')+'" />');
	if(fileVo!=null){
		<u:set var="ctSendYn" test="${ctSendYn == 'Y' }" value="Y" elseValue="N"/>		
		buffer.push('<input type="hidden" name="${id}_valid" value="${ctSendYn}" />');
		buffer.push('<input type="hidden" name="${id}_useYn" value="${fileVo.useYn}" />');
		buffer.push('<input type="hidden" name="${id}_ctSendYn" value="${ctSendYn}" />');
	}else{
		buffer.push('<input type="hidden" name="${id}_fGrpNo" value="" />');
		buffer.push('<input type="hidden" name="${id}_valid" value="N" />');
		buffer.push('<input type="hidden" name="${id}_useYn" value="Y" />');
		buffer.push('<input type="hidden" name="${id}_ctSendYn" value="" />');
	}
	buffer.push('</dd>');
	buffer.push('</dl>');
	buffer.push('</div>');
}<% // 파일 목록 추가 %>
function setFileList(files){
	var exts='${exts}';
	var extsTyp='${extsTyp}';
	if(uploader != null){
		// 파일객체 생성
		var extsList = exts.toLowerCase().split(",");
		var notFileList = [];
		if(uploader.fileListMap==null) uploader.fileListMap = new ParamMap();
		var fileList = [];
		//fileListMap.put('fileList_'+fGrpNo, files); // 전체를 넣는게 아니라 용량 초과된 객체는 제거 필요
		for(var i=0;i<files.length;i++){
			if(notChkFiles(files[i].name, extsList, extsTyp)){
				notFileList.push(files[i].name);
				continue;
			}
			if(uploader.maxSize!=null && uploader.maxSize<Number(uploader.fileSize)+Number(files[i].size)){
				alertMsg('cm.msg.upload.totalFileLimit', [bytesToSize(uploader.maxSize, false)]); // cm.msg.upload.totalFileLimit=전체 파일의 첨부 용량을 초과 하였습니다.({0} M-Bytes)
				break;
			}
			uploader.fileSize+=sizeToBytes(bytesToSize(files[i].size));
			uploader.setMultiFileDisp(null, files[i], null);
			fileList.push(files[i]);
			uploader.fMaxNo++;
		}
		if(files.length>0){
			uploader.fileListMap.put('fileList_'+uploader.fGrpNo, fileList);
			uploader.updateSize();
			uploader.fGrpNo++;
			$('#dragArea').hide();
		}
		if(notFileList!=null && notFileList.length>0){
			if(extsTyp != undefined && extsTyp == 'A'){
				alertMsg('cm.msg.attach.not.support.ext',[exts]);
			}else if(extsTyp != undefined && extsTyp == 'L'){
				alertMsg('cm.msg.attach.not.limit.ext',[exts]);
			}				
		}
		
	}else{
		for(var i=0;i<files.length;i++){
			if(notChkFiles(files[i].name, extsList, extsTyp)) {
				if(extsTyp != undefined && extsTyp == 'A'){
					alertMsg('cm.msg.attach.not.support.ext',[exts]);
				}else if(extsTyp != undefined && extsTyp == 'L'){
					alertMsg('cm.msg.attach.not.limit.ext',[exts]);
				} 
				//resetFile(obj);
			}else{
				//seqNo=addInputFile(obj);
				setFileDisp(files[i], files[i].name);
			}
		}
	}
};
<% // 드래그 이벤트로 인한 파일 추가 %>
function setMultiFileList(obj){
	setFile(obj, '<%= (exts==null ? "" : exts)%>','<%= (extsTyp==null ? "" : extsTyp)%>');
}

$(document).ready(function() {
	<c:if test="${mode=='set'}">
		var buffer=[];
		// 파일목록
		var fileList = loadFileList();
		var size, fileSizeUnit;
		if(uploader != null){
			loadCSS('/css/upload.css'); // 업로드CSS 로드
			$('#fileSizeLimit').show();
			var height = '<%=height%>';
			
			buffer.push('<div id="uploadArea" class="upldr" style="display:block;">');
			buffer.push('<div class="upload_header">');
			buffer.push('<span class="file_select" ><label><input type="checkbox" onclick="checkAllCheckbox(\'${id}Area\', this.checked);" title="전체 첨부파일 선택"></label></span>');
			buffer.push('<span class="file_name"><u:msg titleId="cols.file.nm" alt="파일명"/></span>');
			buffer.push('<span class="status"><u:msg titleId="cols.file.status" alt="업로드상태"/></span>');
			buffer.push('<span class="file_size"><u:msg titleId="cols.file.capacity" alt="용량"/></span>');
			buffer.push('</div>');
			buffer.push('<div class="upload_list dropZone" id="fileListArea" style="height:'+height+'px;">');
			buffer.push('<ul>');
			
			$.each(fileList, function(index, fileVo){
				fileSizeUnit=bytesToSize(parseInt(fileVo.fileSize));
				size=sizeToBytes(fileSizeUnit);
				uploader.fileSize+=size;
				uploader.fMaxNo++;
				//파일사이즈맵 추가
				if(uploader.fileSizeMap==null) uploader.fileSizeMap = new ParamMap();
				// 파일 개별단위로 추가
				uploader.fileSizeMap.put('fileList_'+uploader.fGrpNo+'-item'+parseInt(index), size);
				addMultiArea(buffer, index, fileVo, fileSizeUnit);
				
			});
			var dragAreaStyle = "line-height:"+(parseInt(height)-31)+"px;";
			if(fileList.length>0) dragAreaStyle+="display: none;";
			addMultiArea(buffer, null, null, null);
			buffer.push('</ul>');
			buffer.push('<p id="dragArea" class="dragArea" style="'+dragAreaStyle+'"><span class="drag_icon"></span><u:msg titleId="cm.msg.upload.drag" alt="마우스로 파일을 끌어오세요."/></p>');
			buffer.push('</div>');
			buffer.push('</div>');
			$('#${id}Area').append(buffer.join(''));
			uploader.init({
				id:'${id}', fileId:'${id}_file', drag:true,
				uploadUrl:'${uploadUrl}',
				maxSizeUrl:'/cm/getAttachSizeAjx.do?${paramQueryString }', 
				module:'<%=fileSizeModule%>', isUniform:true, isPop:true, paramQueryString:'${paramQueryString}'
			});
			if(fileList.length>0){
				uploader.updateSize();
				uploader.fGrpNo++;
			}
		}else{
			$('#${id}Area').css('height', '52px');
			$('#${id}Area').css('overflow-y', 'auto');
			$('#attachbtnAllCheck').show();
			$.each(fileList, function(index, fileVo){
				fileSizeUnit=bytesToSize(parseInt(fileVo.fileSize));
				addSingleArea(buffer, index, fileVo, fileSizeUnit);
			});
			addSingleArea(buffer, null, null, null);
			$('#${id}Area').append(buffer.join(''));
			$("#${id}Area, .fileItem").children().each(function(){
				if($(this).hasClass('dispNone') == false){
					setUniformCSS(this);
				}
			});
		}
	</c:if>
	<c:if test="${mode!='set'}">
		//$('#${id}Area').css('height', '52px');
		//$('#${id}Area').css('overflow-y', 'auto');
		$('#attachbtnAllCheck').show();
	</c:if>
	
});

//-->
</script>
<%
String divStyle="", tdStyle="";
if(browser.isIe() && browser.getVer() < 9){
	divStyle = "position:absolute; margin-left:3px; width:57px; overflow:hidden; filter:alpha(opacity:0); z-index:1;";
	tdStyle = (browser.getVer() == 7)
		? "position:relative; padding-top:3px;"
		: "position:relative";
} else {
	divStyle = "width:0px; height:0px; overflow:hidden;";
}

%>
<div id="${id}Area" class="fileUploadArea" >
<c:if test="${mode ne 'view' || (mode eq 'view' && (empty downYn || downYn eq 'Y'))}">
<div class="attachbtn">
<dl>
<dd class="attachbtn_check" id="attachbtnAllCheck" style="display:none;"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('${id}Area', this.checked);" value=""/></dd>
<c:if test="${mode == 'set'}">
<dd><div id="${id}attachBtnArea_0" class="${id}attachBtnArea" style="<%= divStyle%>" ><input multiple="multiple" type="file" id="${id}_file" name="${id}_file" class="skipThese" style="height:20px;margin-left:-165px" onchange="setFile(this,'<%= (exts==null ? "" : exts)%>','<%= (extsTyp==null ? "" : extsTyp)%>');" /></div>
	<%
		if(browser.isIe() && browser.getVer()<11){
		%><label for="${id}_file"><u:buttonS titleId="cm.btn.fileAtt" alt="파일첨부"  /></label><%
		} else {
		%><u:buttonS titleId="cm.btn.fileAtt" alt="파일첨부" onclick="addFile();" /><%
		}
		%>
	<u:buttonS titleId="cm.btn.del" alt="삭제" onclick="delFile();" img="ico_delete.png" imgW="11" imgH="9" />
	<c:if test="${mode=='set'}"><div id="fileSizeLimit" style="float:right;display:none;"><u:msg titleId="cols.attCapaLim" alt="첨부용량제한"/><span class="color_txt" id="fileSizeMsg" style="margin-left:10px;">0MB</span>/<span class="red_txt" id="maxSizeMsg">0MB</span></div></c:if>
</dd>
</c:if>
<c:if test="${mode == 'view'}">
<dd><u:buttonS titleId="cm.btn.download" alt="다운로드" onclick="downFile();" /></dd>
</c:if>
</dl>
</div>
</c:if>
<c:if test="${mode == 'view' && fn:length(fileVoList) > 0}">
	<c:forEach items="${fileVoList}" var="fileVo" varStatus="status">
	<u:set test="${extSet.contains(fileVo.fileExt)}" var="ext" value="${fileVo.fileExt}" elseValue="file" />
	<div class="attacharea fileItem">
	<dl>
	<c:if test="${mode ne 'view' || (mode eq 'view' && (empty downYn || downYn eq 'Y'))}"><dd class="attach_check"><input type="checkbox" name="fileChk" value="${fileVo.fileId}" /></dd></c:if>
	<dd class="attach_img"><c:if test="${viewYn eq 'Y' }"><a href="javascript:attachViewPop('${fileVo.fileId}', '${fileVo.fileExt }');"><img src="${_cxPth}/images/cm/ico_${ext}.png"/></a></c:if><c:if test="${viewYn ne 'Y' }"><img src="${_cxPth}/images/cm/ico_${ext}.png"/></c:if></dd>
	<c:set var="fileSizeKB" value="${fileVo.fileSize/1024 }"/>
	<c:set var="fileSizeMB" value="${fileVo.fileSize/(1024*1024) }"/>
	<c:set var="fileSizeTemp" value="${fileSizeKB+(1-(fileSizeKB%1))%1}"/>
	<u:set var="fileSize" test="${fileSizeTemp > 999 }" value="${fileSizeMB+(1-(fileSizeMB%1))%1 }" elseValue="${fileSizeTemp }"/>
	<u:set var="fileSizeUnit" test="${fileSizeTemp > 999 }" value="MB" elseValue="KB"/>
	<u:set var="ctSendYn" test="${ctSendYn == 'Y' }" value="Y" elseValue="N"/>
	<dd class="attach"><c:if test="${empty downYn || downYn eq 'Y'}"
	><a href="javascript:downFile('${fileVo.fileId}');"><u:out value="${fileVo.dispNm}" type="value"/>(<fmt:formatNumber value="${fileSize}" type="number"/>${fileSizeUnit })</a></c:if
	><c:if test="${!empty downYn && downYn ne 'Y' }"><u:out value="${fileVo.dispNm}" type="value"/>(<fmt:formatNumber value="${fileSize}" type="number"/>${fileSizeUnit })</c:if
	></dd>
	<dd class="sizeZero">
		<input type="hidden" name="${id}_fileId" value="${fileVo.fileId}" />
		<input type="hidden" name="${id}_valid" value="${ctSendYn}" />
		<input type="hidden" name="${id}_useYn" value="${fileVo.useYn}" />
		<input type="hidden" name="${id}_ctSendYn" value="${ctSendYn}" />
		</dd>
	</dl>
	</div>
	</c:forEach>
</c:if>

</div>
<div id="${id}attachBtnAreaParent" class="${id}attachBtnArea" style="display:none;" ><input multiple="multiple" type="file" class="skipThese" style="height:20px;margin-left:-165px" onchange="setFile(this,'<%= (exts==null ? "" : exts)%>','<%= (extsTyp==null ? "" : extsTyp)%>');" /></div>
