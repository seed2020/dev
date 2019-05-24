<%@ tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="java.util.HashSet,com.innobiz.orange.web.pt.secu.Browser"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"
%><%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"
%><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"
%><%@ attribute name="id"			required="false"
%><%@ attribute name="apvNo"		required="false"
%><%@ attribute name="attHstNo"		required="false"
%><%@ attribute name="module"		required="false"
%><%@ attribute name="fileTarget"		required="false"
%><%@ attribute name="mode"			required="true"
%><%@ attribute name="docFileInfoArea"  required="false"
%><%@ attribute name="docFileTagArea"  required="false"
%><%@ attribute name="onchange"  required="false"
%><%@ attribute name="exts"  required="false"
%><%@ attribute name="extsTyp"  required="false"
%><%@ attribute name="height"  required="false"
%><%@ attribute name="isHidden"  required="false"
%><u:set
	test="${optConfigMap.newLnEach eq 'Y'}" var="openNobr" value="" elseValue="<nobr>" /><u:set
	test="${optConfigMap.newLnEach eq 'Y'}" var="closeNobr" value="" elseValue="</nobr>" /><u:set
	test="${optConfigMap.newLnEach eq 'Y'}" var="newLnBr" value="<br/>" elseValue=" " /><%
/**
	=== 결재용 ===

파일 업로드/다운로드
설명		: 여러 개의 파일을 업로드/다운로드 한다.

id			: DIV 태그의 id로 (${id}FileArea) 사용 
module		: 파일 다운로드 URI를 호출할 때 다음과 같이 사용된다.
			 '/${module}/downFile.do'
mode		: 조회(view), 등록/수정(set)
*/

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
	
	if(id==null || id.isEmpty()) id = "ap";
	if(docFileInfoArea==null || docFileInfoArea.isEmpty()) docFileInfoArea = "docFileInfoArea";
	if(docFileTagArea==null || docFileTagArea.isEmpty()) docFileTagArea = "docFileTagArea";
	
	if(module==null) module = "ap/box";
	else module = module.replace('.','/');
	
	if(fileTarget==null) fileTarget = "";
	
	Browser browser = (Browser)request.getAttribute("browser");
	
	if(height==null) height="90";
	
%>
<style type="text/css">
<!--
.sizeZero { width: 0; height: 0; }
.dispNone { display: none; }
--> 
</style>
<!-- 멀티파일여부 -->
<u:set var="paramQueryString" test="${!empty param.bxId}" value="menuId=${menuId }&bxId=${param.bxId }" elseValue="menuId=${menuId }"/>
<c:set var="uploadUrl" value="./transFileUploadAjx.do?${paramQueryString }${strMnuParam}&module=${apFileModule }&uploadId=${id}File"/>
<script type="text/javascript">
<!--
var uploader='${mode}'=='set' && isMultiFile() ? newUploader('${id}', false) : null;
//var uploader=null;
var seqNo=0;
var tmpDelList=null;
<% // 확장자 배열 %>
var extArr = [];
<c:forEach items="${extSet}" var="ext" varStatus="status">extArr.push('${ext}');
</c:forEach><%
// 파일 추가 시퀀스 %>
var gMaxFileSeq = 0;
var gDocFileInfoArea = "<%= docFileInfoArea%>";
var gDocFileTagArea = "<%= docFileTagArea%>";
<% // 파일 초기화 %>
function resetFile(obj){
	$(obj).before($(obj).clone(false));
	$(obj).remove();
}
<% // 파일 추가 %>
function addApFile() {
	$('#${id}File').trigger('click');
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
<% // 파일 선택 %>
function setApFile(obj, exts, extsTyp) {
	var va, p;
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
			va = $(obj).val();
			p = va.lastIndexOf('\\');
			if (p > 0) va = va.substring(p + 1);<%// IE의 경우 경로뺀 순수 파일명 %>
			setApFileDisp(va, obj);
		}
	}else{
		var notFileList = [];
		var files = obj.files;
		if(files.length>0){
			$('#dragArea').hide();
			if(uploader.fileListMap==null) uploader.fileListMap = new ParamMap();
			var fileList = [];
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
				uploader.setMultiFileDisp(null, files[i]);
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
			resetFile($('#${id}File'));
		}
		
	}
}
<% // 파일 선택 %>
function setApFileDisp(va, obj, sizeZero) {
	var $fileArea = $("#${id}FileArea");<%
	
	// 화면 표시용 영역 + Hidden 테그
	//  - 화면 표시용 체크 + 파일명 표시하여 display 함
	//  - Hidden 테그 에 - attDispNm:표시명 저장
	%>
	var $last = $fileArea.find('.attacharea:last');<%// 숨겨진 파일 View 용 영역 %>
	var $clone = $last.clone();<%// 복사하여 $last 앞에 넣을 영역 - 첨부한 파일이 표시될 영역 %>
	if(sizeZero != null){<%// 이전에 저장된 파일 또는 이미 세팅된 파일 %>
		if($(sizeZero).find("[name='attUseYn']").val()!='N'){
			$clone.removeClass('dispNone');
		}
		var attIntgNo = $(sizeZero).find("[name='attIntgNo']").val();
		var attSeq = $(sizeZero).find("[name='attSeq']").val();
		if(attIntgNo!=null && attSeq!=null){<%// ERP 연계에 의한 파일 이면 %>
			var $a = $('<a href="javascript:downErpIntgAttchFileByPop(\''+attIntgNo+'\',\''+attSeq+'\');">'+va+'</a>');
			$a.insertBefore($clone.find('#FileView'));
			$clone.find('#FileView').remove();
		} else if(attSeq!=null){<%// attSeq 가 있으면 서버에 저장된 파일로 a 링크를 만듬 %>
			var $a = $('<a href="javascript:downFileByPop(\''+attSeq+'\');">'+va+'</a>');
			$a.insertBefore($clone.find('#FileView'));
			$clone.find('#FileView').remove();
		} else {
			$clone.find('#FileView').text(va).removeAttr('id');<%// 파일명만 표시 - html %>
		}
	} else {
		$clone.removeClass('dispNone');
		$clone.find('#FileView').text(va).removeAttr('id');<%// 파일명만 표시 - html %>
	}
	
	var p = va.lastIndexOf('.');
	if (p > 0) {<%// 해당 확장자 아이콘 으로 변경 %>
		var ext = va.substring(p + 1);
		if (extArr.contains(ext)) {
			var $img = $clone.find('.attach_img img');
			var src = $img.attr('src');
			$img.attr('src', src.replace('ico_file', 'ico_' + ext));
		}
	}
	if(obj!=null){<%// 파일추가에 의한것 - 파일 테그가 있으면%><%
		
		// data-addSeq : [확인]클릭해서 submit 영역으로 옮겨 졌을때 그곳의 파일테그를 찾아 지우기 위한 고유 시퀀스
		// value : 파일 표시명 - Hidden 테그 %>
		gMaxFileSeq++;
		$clone.find('input[name="attDispNm"]').attr('data-addSeq', gMaxFileSeq).val(va);
		$clone.insertBefore($last);<%// 화면에 표시 %>
		$clone.find("input[type='checkbox']").uniform().bind("keydown", function(event){
			if(event.which==13) $(this).trigger("click");
		});<%
		
		// file 테그 처리
		// 기존 파일테그(파일이 세팅된)을 화면표시용 영역으로 이동시키고
		// 기존 테그 위치에 새로운 파일 테그 넣음
		%>
		$(obj.outerHTML).insertBefore(obj);<%// file 테그 추가 %>
		$clone.find(".sizeZero").append(obj);<%// 기존 file 테그 - view 영역의 안보이는 위치로 이동 %>
		$(obj).removeAttr('id').attr('data-addSeq', gMaxFileSeq);<%// id 제거 - label의 이벤트의 동작을 위해 - for browsers except ie %>
		
	} else {<%// 문서에 저장된것 onload에 읽으면서 세팅하는 경우 - obj 는 없고 sizeZero 가 있음 %>
		$clone.find(".sizeZero").remove();
		$clone.find("dl").append($(sizeZero).clone());
		$clone.insertBefore($last);<%// 화면에 표시 %>
		$clone.find("input[type='checkbox']").uniform().bind("keydown", function(event){
			if(event.which==13) $(this).trigger("click");
		});
	}
	<%= (onchange!=null && !onchange.isEmpty() ? onchange : "")%>
}
<% // 파일 삭제 %>
function delApFile() {
	var $fileArea = $("#${id}FileArea");
	var $checked = $fileArea.find('.fileItem input:checked:visible');
	if ($checked.length == 0) {<%
		// cm.msg.noSelect=선택한 항목이 없습니다. %>
		alertMsg("cm.msg.noSelect");
		return;
	}
	var isConfirm = uploader==null;
	if ((isConfirm && confirmMsg("cm.cfrm.del")) || !isConfirm) { <% // cm.cfrm.del=삭제하시겠습니까 ? %>
		var $area, delFileArr = [];
		var $delSeqs = $fileArea.find("input[name='delSeqs']");
		$checked.each(function() {
			$area = $(this).parents('.fileItem');
			if ($area.hasClass('dispNone') == false) {<%
				// file 테그가 있으면 - 팝업에서 파일을 추가한것 - 해당 영역만 지우면 됨 %>
				if(uploader==null){
					if($area.find('input[name="file"]').length>0){
						$area.remove();<%
					// attSeq 가 있으면 이미 저장된 파일 - 사용여부 N 세팅하여 서버 사이드에서 지워 지도록 함 %>
					} else if($area.find('input[name="attSeq"]').length>0){
						$area.find('input[name="attUseYn"]').val('N');
						$area.hide();<%
					// 파일이 추가 되었으나, 확인 버튼으로 이미 파일 테그가 submit 영역으로 이동해 간것
					//  - 확인 버튼 클릭시 해당 file 테그 지워주기 위해 attSeq 관리 필요 %>
					} else {
						delFileArr.push($area.find('input[name="attDispNm"]').attr('data-addSeq'));
						$area.remove();
					}
				}else{
					if(uploader.delListMap==null) uploader.delListMap=new ParamMap();
					var grpNo = $area.find('input[name="${id}_fGrpNo"]').val();
					var seqNo = $area.find('input[name="${id}_fSeqNo"]').val();
					if(seqNo!='') {
						if(tmpDelList==null) tmpDelList=[];
						tmpDelList.push('fileList_'+grpNo+'-item'+seqNo);
						//uploader.delListMap.put('fileList_'+grpNo+'-item'+seqNo, true);
						var size = uploader.fileSizeMap.get('fileList_'+grpNo+'-item'+seqNo);
						if(size!=null) {
							uploader.fileSize-=sizeToBytes(bytesToSize(size));
							if(uploader.fileSize<0) uploader.fileSize=0;
							uploader.updateSize();
						}
					}
					if($area.find('input[name="attSeq"]').length>0){
						$area.find('input[name="attUseYn"]').val('N');
						//$area.hide();
						$area.addClass('dispNone');
					}else{
						delFileArr.push($area.find('input[name="attDispNm"]').attr('data-addSeq'));
						$area.remove();	
					}
				}
			}
		});
		if(delFileArr.length>0){<%// 파일 추가된 시퀀스 세팅 - 확인버튼 클릭 할때 - submit 영역의 file 테그 지워줌 %>
			if($delSeqs.val()=='') $delSeqs.val(delFileArr.join(','));
			else $delSeqs.val($delSeqs.val()+','+delFileArr.join(','));
		}
		checkAllCheckbox('${id}FileArea', false);
		<%= (onchange!=null && !onchange.isEmpty() ? onchange : "")%>
	}
	if(!isConfirm && $('#fileListArea .fileItem').not('.dispNone').length==0) $('#dragArea').show();
}
<% // 파일 다운로드 %>
function downFileByPop(id) {
	var ids = [];
	if (id == null) {
		$('.attacharea input:checked:visible').each(function() {
			if($(this).val()!=null && $(this).val()!=''){
				ids.push($(this).val());
			}
		});
		if (ids.length == 0) {<%
			// cm.msg.noSelect=선택한 항목이 없습니다. %>
			alertMsg("cm.msg.noSelect");
			return;
		}
	} else {
		ids.push(id);
	}
	var $form = $('<form>', {
			'method':'post','action':'/<%= module%>/down<%=fileTarget%>File.do','target':'dataframe'
		}).append($('<input>', {
			'name':'bxId','value':'${param.bxId}','type':'hidden'
		})).append($('<input>', {
			'name':'menuId','value':'${menuId}','type':'hidden'
		})).append($('<input>', {
			'name':'apvNo','value':'${apvNo}','type':'hidden'
		})).append($('<input>', {
			'name':'attHstNo','value':'${attHstNo}','type':'hidden'
		})).append($('<input>', {
			'name':'attSeq','value':ids.join(','),'type':'hidden'
		}));
	<c:forEach items="${arrMnuParam}" var="mnuParam">
	$form.append($('<input>', {'name':'${mnuParam[0]}','value':'${mnuParam[1]}','type':'hidden'}));</c:forEach>
	
	$(document.body).append($form);
	$form.submit();
	$form.remove();
}<%
// ERP 연계 파일 다운로드 %>
function downErpIntgAttchFileByPop(intgNo, seq) {
	var $form = $('<form>', {
			'method':'post','action':'/${apFileModule}/downFile.do','target':'dataframe'
		}).append($('<input>', {
			'name':'bxId','value':'${param.bxId}','type':'hidden'
		})).append($('<input>', {
			'name':'menuId','value':'${menuId}','type':'hidden'
		})).append($('<input>', {
			'name':'intgNo','value':intgNo,'type':'hidden'
		})).append($('<input>', {
			'name':'attSeq','value':seq,'type':'hidden'
		}));
	<c:forEach items="${arrMnuParam}" var="mnuParam">
	$form.append($('<input>', {'name':'${mnuParam[0]}','value':'${mnuParam[1]}','type':'hidden'}));</c:forEach>
	
	$(document.body).append($form);
	$form.submit();
	$form.remove();
}
<%// 문서에서 파일을 읽어서 팝업에 세팅 %>
function setFilesFromDoc(){
	var $docFileInfoArea = $("#"+gDocFileInfoArea), attDispNm;
	var maxFileSeq = $docFileInfoArea.find("input[name='maxFileSeq']").val();
	if(maxFileSeq!=null && maxFileSeq!=''){
		gMaxFileSeq = parseInt(maxFileSeq);
		var file=null,size=null,len,$a=null, isOld=false, grpNo, seqNo, addHtml=null ;
		//if(uploader!=null) uploader.init(); // 업로더 기본 데이터 초기화
		// 기존 파일이 있을경우 파일사이즈 초기화
		if(uploader!=null) uploader.fileSizeInit();
		
		$docFileInfoArea.find(".sizeZero").each(function(index){
			$a=null;
			attDispNm = $(this).find("input[name='attDispNm']").val();
			if(uploader!=null){
				addHtml = "";
				grpNo=$(this).find('input[name="${id}_fGrpNo"]').val();
				seqNo=$(this).find('input[name="${id}_fSeqNo"]').val();
				isOld = grpNo!=undefined && seqNo!=undefined;
				size= isOld ? uploader.fileSizeMap.get('fileList_'+grpNo+'-item'+seqNo) : $(this).find('input[name="fileKb"]').val();
				if(size==undefined || size=='') size='1 KB'; // 사이즈 1KB 로 초기화
				size=sizeToBytes(isOld ? bytesToSize(size) : size);
				var attUseYn = $(this).find("[name='attUseYn']").val();
				if(isOld){
					if(attUseYn!='N')uploader.fileSize+=size;
					file={size:size, grpNo:grpNo, seqNo:seqNo};
				}else{
					uploader.fileSize+=size;
					file={size:size};
				}
				
				var attIntgNo = $(this).find("[name='attIntgNo']").val();
				var attSeq = $(this).find("[name='attSeq']").val();
				if(attIntgNo!=null && attIntgNo!=undefined && attSeq!=undefined && attSeq!=null){<%// ERP 연계에 의한 파일 이면 %>
					$a = $('<a href="javascript:downErpIntgAttchFileByPop(\''+attIntgNo+'\',\''+attSeq+'\');">'+attDispNm+'</a>');
					addHtml+= '<input type="hidden" name="attIntgNo" value="'+attIntgNo+'">';
				} else if(attSeq!=undefined && attSeq!=null){<%// attSeq 가 있으면 서버에 저장된 파일로 a 링크를 만듬 %>
					$a = $('<a href="javascript:downFileByPop(\''+attSeq+'\');">'+attDispNm+'</a>');					
				}
				if(attSeq!=undefined && attSeq!=null && attSeq!='') addHtml+= '<input type="hidden" name="attSeq" value="'+(attSeq==null?'':attSeq)+'">';
				addHtml+= '<input type="hidden" name="attDispNm" value="'+(attDispNm==null?'':attDispNm)+'">';
				if(attUseYn!=undefined){
					addHtml+= '<input type="hidden" name="attUseYn" value="'+attUseYn+'">';
				}
				uploader.setMultiFileDisp(attDispNm, file, $a, isOld, addHtml, attUseYn!=undefined && attUseYn=='N');
				//파일사이즈맵 추가
				if(uploader.fileSizeMap==null) uploader.fileSizeMap = new ParamMap();
				// 파일 개별단위로 추가
				if(!isOld){
					uploader.fileSizeMap.put('fileList_'+uploader.fGrpNo+'-item'+uploader.fMaxNo, size);
					uploader.fMaxNo++;
					uploader.fGrpNo++;
				}
			}else setApFileDisp(attDispNm, null, this);
		});
		if(uploader!=null){
			if($('#fileListArea .fileItem').not('.dispNone').length>0){
				$('#dragArea').hide();
				uploader.updateSize();
				//uploader.fGrpNo++;
			}else{
				$('#dragArea').show();
			}
		}
	}
}

<%// 팝업에 설정된 파일을 문서에 세팅 %>
function setFilesToDoc(){
	var $docFileInfoArea = $("#"+gDocFileInfoArea);
	var $docFileTagArea = $("#"+gDocFileTagArea);	
	var $file, $fileArea = $("#${id}FileArea");
	var buffer = [], attSeq;	
	if(uploader!=null){ // 멀티업로드가 활성화 되었을 경우 - 추가
		// 팝업에서 파일을 삭제하고 확인 버튼을 눌렀을 때 실제 파일을 삭제한다.
		if(tmpDelList==null) tmpDelList=[];
		if(tmpDelList!=null){
			$.each(tmpDelList, function(){
				uploader.delListMap.put($(this), true);
			});
		}
		var maxNo = $('#fileListArea .fileItem').not('.dispNone').length;
		$docFileInfoArea.html("<input type='hidden' name='maxFileSeq' value='"+maxNo+"' /><input type='hidden' name='attFileModified' value='Y' />");
		var grpNo, seqNo, sizeZero;
		$fileArea.find("#fileListArea .fileItem").not(':last').each(function(){
			sizeZero=[];
			sizeZero.push('<dd class="sizeZero fileItem" style="display:none" id="'+$(this).attr('id')+'">');
			if($(this).find("input[name='attUseYn']").val()!='N'){
				attSeq = $(this).find("input[name='attSeq']").val();
				if(attSeq==undefined || attSeq==null){
					buffer.push('${openNobr}'+$(this).find('.title').text()+'${closeNobr}');
				} else {
					buffer.push('${openNobr}<a href="javascript:downAttchFile(\''+attSeq+'\')">'+$(this).find('.title').text()+'</a>${closeNobr}');					
				}
			}
			var attUseYn =  $(this).find('input[name="attUseYn"]').val();
			if(attUseYn!=undefined) sizeZero.push('<input type="hidden" name="attUseYn" value="'+attUseYn+'">');
			var attSeq =  $(this).find('input[name="attSeq"]').val();
			if(attSeq!=undefined && attSeq!=null && attSeq!='') sizeZero.push('<input type="hidden" name="attSeq" value="'+(attSeq==null?'':attSeq)+'">');
			
			grpNo = $(this).find('input[name="${id}_fGrpNo"]').val();
			seqNo = $(this).find('input[name="${id}_fSeqNo"]').val();
			
			sizeZero.push('<input type="hidden" name="attDispNm" value="'+$(this).find('.title').text().trim()+'" data-addSeq="'+seqNo+'">');
			sizeZero.push('<input type="hidden" name="tmpFileId" value="" />');
			
			var attIntgNo = $(this).find("[name='attIntgNo']").val();
			if(attIntgNo!=null && attIntgNo!=undefined){<%// ERP 연계에 의한 파일 이면 %>
				sizeZero.push('<input type="hidden" name="attIntgNo" value="'+attIntgNo+'">');
			}
			
			if(grpNo!=undefined && seqNo!=undefined){
				var size = uploader.fileSizeMap.get('fileList_'+grpNo+'-item'+seqNo);
				if(size!=null) {
					sizeZero.push('<input type="hidden" name="fileKb" value="'+bytesToSize(size)+'">');					
				}
				sizeZero.push('<input type="hidden" name="${id}_fGrpNo" value="'+grpNo+'">');
				sizeZero.push('<input type="hidden" name="${id}_fSeqNo" value="'+seqNo+'">');
			}
			sizeZero.push('</dd>');
			$docFileInfoArea.append(sizeZero.join(''));
		});
	}else{
		$docFileInfoArea.html("<input type='hidden' name='maxFileSeq' value='"+gMaxFileSeq+"' /><input type='hidden' name='attFileModified' value='Y' />");
		$fileArea.find(".attacharea").not(":last").find(".sizeZero").each(function(){
			$file = $(this).find("input[type='file']");
			if($file.length>0){
				$docFileTagArea.append($file);
			}
			attUseYn = $(this).find("input[name='attUseYn']").val();
			if($(this).find("input[name='attUseYn']").val()!='N'){
				attSeq = $(this).find("input[name='attSeq']").val();
				if(attSeq==null){
					buffer.push('${openNobr}'+$(this).find("input[name='attDispNm']").val()+'${closeNobr}');
				} else {
					buffer.push('${openNobr}<a href="javascript:downAttchFile(\''+attSeq+'\')">'+$(this).find("input[name='attDispNm']").val()+'</a>${closeNobr}');
				}
			}
			$docFileInfoArea.append(this);
		});
	}
	var $delSeqs = $fileArea.find("input[name='delSeqs']");
	if($delSeqs.val()!=''){
		$delSeqs.val().split(',').each(function(index, va){
			$docFileTagArea.find("input[data-addSeq='"+va+"']").remove();
		});
	}
	$("#docArea div[data-name='itemsArea'] td#attFileView").html(buffer.join(",${newLnBr}"));
	$("#docDataArea #docFileInfoArea").attr('data-modified','Y');
}
<c:if test="${viewYn eq 'Y'}">
<% // 미리보기 %>
function attachViewPop(id) {
	var url = './attachView.do?menuId=${empty menuId ? param.menuId : menuId}'
	url+='&bxId=${param.bxId}';
	url+='&apvNo=${apvNo}';
	url+='&attHstNo=${attHstNo}';
	url+='&attSeq='+id;
	window.open(url);
}
</c:if>

<c:if test="${mode == 'set'}">

function setUploadFileList(){
	if(uploader.isHidden){
		if($("#docArea #multiFileList") !=undefined) $('#fileListArea').html($("#docArea #multiFileList").html());
		uploader.updateSize();
	}
}

function setFile(obj, exts, extsTyp){
	setApFile(obj, exts, extsTyp);
} 

<% // 파일목록 로드 %>
function loadFileList(){
	var fileList=[];
	<c:forEach items="${apOngdAttFileLVoList}" var="fileVo" varStatus="status">
		fileList.push({
			dispNm:'<u:out value="${fileVo.attDispNm}" type="value"/>',
			fileId:'${fileVo.attSeq}',
			useYn:'Y',
			fileSize:'${fileVo.fileKb}',
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
		buffer.push('<span class="file_icon '+(extArr.contains(fileVo.fileExt) ? fileVo.fileExt : 'file')+'"></span><span class="title">');
		<c:if test="${(empty downYn || downYn eq 'Y')}">
			buffer.push('<a href="javascript:downFileByPop('+fileVo.fileId+');">'+fileVo.dispNm+'</a>');
		</c:if>
		<c:if test="${!empty downYn && downYn ne 'Y' }">buffer.push(fileVo.dispNm);</c:if>
		buffer.push('</span></label></span>');
		buffer.push('<span class="status"><u:msg titleId="cm.msg.complete" alt="완료"/></span>');
	}else{
		buffer.push('<span class="file_icon"></span><span class="title"></span></label>');
		buffer.push('</span>');
		buffer.push('<span class="status progress" style="display:none;"><img src="${_ctx}/images/${_skin}/pollbar.png" width="0%" height="10"></span>');
	}
	
	buffer.push('<span class="file_size">'+(fileSizeUnit!=null ? fileSizeUnit : '')+'</span>');
	if(fileVo!=null) {
		buffer.push('<input type="hidden" name="${id}_fGrpNo" value="'+uploader.fGrpNo+'" />');
		buffer.push('<input type="hidden" name="${id}_fSeqNo" value="'+index+'" />');
		buffer.push('<input type="hidden" name="attSeq" value="'+fileVo.fileId+'" />');
		buffer.push('<input type="hidden" name="attDispNm" value="'+fileVo.dispNm+'" />');
		buffer.push('<input type="hidden" name="attUseYn" value="Y" />');
	}else{
		buffer.push('<input type="hidden" name="${id}_fGrpNo" value="" />');
		buffer.push('<input type="hidden" name="${id}_fSeqNo" value="" />');
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
		buffer.push('<a href="javascript:downFileByPop('+fileVo.fileId+');">'+fileVo.dispNm+'</a>');
		</c:if><c:if test="${!empty downYn && downYn ne 'Y' }">
		buffer.push(fileVo.dispNm+'('+fileSizeUnit+')';
		</c:if>
	}else buffer.push('<span id="FileView"></span>');
	buffer.push('</dd>');
	buffer.push('<dd class="sizeZero" style="display:none">');
	if(fileVo!=null){
		buffer.push('<input type="hidden" name="attSeq" value="'+fileVo.fileId+'" />');
		buffer.push('<input type="hidden" name="attDispNm" value="'+fileVo.dispNm+'" />');
		buffer.push('<input type="hidden" name="attUseYn" value="Y" />');
	}else{
		buffer.push('<input type="hidden" name="attDispNm" value="" />');
	}
	buffer.push('</dd>');
	buffer.push('</dl>');
	buffer.push('</div>');
}
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
			var isHidden = <%= isHidden==null || "Y".equals(isHidden) ? true : false %>;
			loadCSS('/css/upload.css'); // 업로드CSS 로드
			$('#fileSizeLimit').show();
			var height = '<%=height%>';
			
			buffer.push('<div id="uploadArea" class="upldr" style="display:block;">');
			buffer.push('<div class="upload_header">');
			buffer.push('<span class="file_select" ><label><input type="checkbox" onclick="checkAllCheckbox(\'${id}FileArea\', this.checked);" title="전체 첨부파일 선택"></label></span>');
			buffer.push('<span class="file_name"><u:msg titleId="cols.file.nm" alt="파일명"/></span>');
			buffer.push('<span class="status"><u:msg titleId="cols.file.status" alt="업로드상태"/></span>');
			buffer.push('<span class="file_size"><u:msg titleId="cols.file.capacity" alt="용량"/></span>');
			buffer.push('</div>');
			buffer.push('<div class="upload_list dropZone" id="fileListArea" style="height:'+height+'px;">');
			buffer.push('<ul>');
			// 기존 파일이 있을경우 파일사이즈 초기화
			if(fileList.length>0) uploader.fileSizeInit();
			$.each(fileList, function(index, fileVo){
				size=fileVo.fileSize;
				if(size==undefined || size=='') size='1'; // 사이즈 1KB 로 초기화
				fileSizeUnit=bytesToSize(parseInt(fileVo.fileSize)*1024);
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
			$('#${id}FileArea').append(buffer.join(''));
			uploader.init({
				id:'${id}', fileId:'${id}File', drag:true, 
				uploadUrl:'${uploadUrl}',
				maxSizeUrl:'./getAttachSizeAjx.do?${paramQueryString }${strMnuParam}', 
				module:'ap', isUniform:true, isPop:true, paramQueryString:'${paramQueryString}', 
				isHidden:isHidden,
				fileListArea:isHidden ? 'docFileInfoArea' : 'fileListArea'
			});
			if(fileList.length>0){
				uploader.updateSize();
				uploader.fGrpNo++;
			}
		}else{
			$('#attachbtnAllCheck').show();
			if(fileList.length>0){
				$.each(fileList, function(index, fileVo){
					size='${apOngdAttFileLVo.fileKb}';
					if(size==undefined || size=='') size='1 KB'; // 사이즈 1KB 로 초기화
					size=sizeToBytes(size);
					fileSizeUnit=bytesToSize(size);
					addSingleArea(buffer, index, fileVo, fileSizeUnit);
				});
			}
			addSingleArea(buffer, null, null, null);
			$('#${id}FileArea').append(buffer.join(''));
			$("#${id}FileArea, .fileItem").children().each(function(){
				if($(this).hasClass('dispNone') == false){
					setUniformCSS(this);
				}
			});
		}
	</c:if>
	<c:if test="${mode!='set'}">
		$('#attachbtnAllCheck').show();
	</c:if>
	setFilesFromDoc();
});
</c:if>
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
<div id="${id}FileArea" <c:if test="${!empty isHidden && isHidden=='N'}">class="fileUploadArea"</c:if>>
<c:if test="${mode ne 'view' || (mode eq 'view' && (empty downYn || downYn eq 'Y'))}">
<input type="hidden" name="delSeqs" />
<div class="attachbtn">
<dl>
<dd class="attachbtn_check" id="attachbtnAllCheck" style="${mode ne 'view' ? 'display:none;' : ''}"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('${id}FileArea', this.checked);" value=""/></dd>
<c:if test="${mode == 'set'}">
<dd><div id="attachBtnArea" style="<%= divStyle%>" ><input multiple="multiple" type="file" id="${id}File" name="${id}File" class="skipThese" style="height:20px;margin-left:-165px" onchange="setApFile(this,'<%= (exts==null ? "" : exts)%>','<%= (extsTyp==null ? "" : extsTyp)%>');" /></div>
<%
	if(browser.isIe() && browser.getVer() < 11){
	%><label for="${id}File"><u:buttonS 
		titleId="cm.btn.fileAtt" alt="파일첨부" /></label><u:buttonS 
		titleId="cm.btn.del" alt="삭제" onclick="delApFile();" img="ico_delete.png" imgW="11" imgH="9" /><%
	} else {
	%><u:buttonS
		titleId="cm.btn.fileAtt" alt="파일첨부" onclick="addApFile();" /><u:buttonS 
		titleId="cm.btn.del" alt="삭제" onclick="delApFile();" img="ico_delete.png" imgW="11" imgH="9" /><%
	}
%>
<c:if test="${mode=='set'}"><div id="fileSizeLimit" style="float:right;display:none;"><u:msg titleId="cols.attCapaLim" alt="첨부용량제한"/><span class="color_txt" id="fileSizeMsg" style="margin-left:10px;">0MB</span>/<span class="red_txt" id="maxSizeMsg">0MB</span></div></c:if>
</dd>
</c:if>
<c:if test="${mode == 'view'}">
<dd><u:buttonS titleId="cm.btn.download" alt="다운로드" onclick="downFileByPop();" /></dd>
</c:if>
</dl>
</div>
</c:if>
<c:if test="${mode == 'view' && fn:length(apOngdAttFileLVoList) > 0}">
<c:forEach items="${apOngdAttFileLVoList}" var="apOngdAttFileLVo" varStatus="status">
	<u:set test="${extSet.contains(apOngdAttFileLVo.fileExt)}" var="ext" value="${apOngdAttFileLVo.fileExt}" elseValue="file" />
	<div class="attacharea">
	<dl>
	<c:if test="${mode ne 'view' || (mode eq 'view' && (empty downYn || downYn eq 'Y'))}"><dd class="attach_check"><input type="checkbox" name="fileChk" value="${apOngdAttFileLVo.attSeq}" /></dd></c:if>
	<dd class="attach_img"><c:if test="${viewYn eq 'Y' }"><a href="javascript:attachViewPop('${apOngdAttFileLVo.attSeq}');"><img src="${_cxPth}/images/cm/ico_${ext}.png"/></a></c:if><c:if test="${viewYn ne 'Y' }"><img src="${_cxPth}/images/cm/ico_${ext}.png"/></c:if></dd>
	<dd class="attach"><c:if test="${(empty downYn || downYn eq 'Y')}"><a href="javascript:downFileByPop('${apOngdAttFileLVo.attSeq}');">${apOngdAttFileLVo.attDispNm}</a></c:if
	><c:if test="${!empty downYn && downYn ne 'Y' }">${apOngdAttFileLVo.attDispNm}</c:if></dd>
	<dd class="sizeZero" style="display:none">
		<input type="hidden" name="attSeq" value="${apOngdAttFileLVo.attSeq}" />
		<input type="hidden" name="attDispNm" value="${apOngdAttFileLVo.attDispNm}" />
		<input type="hidden" name="attUseYn" value="Y" />		
	</dd>
	</dl>
	</div>
</c:forEach>
</c:if>
</div>