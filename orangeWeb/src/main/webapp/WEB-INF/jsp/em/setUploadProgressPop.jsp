<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<link rel="stylesheet" href="/css/upload.css" type="text/css" />
<script type="text/javascript">
<!--
$(document).ready(function() {
	var uploader = getUploader('${param.uploaderId}');
	if(uploader!=null){
		if(uploader.isHidden){
			$('#docFileInfoArea').find(".fileItem").each(function(index){
				var attUseYn = $(this).find("[name='attUseYn']").val();
				var attSeq = $(this).find("[name='attSeq']").val();
				if((attUseYn!=undefined && attUseYn=='N') || (attSeq!=undefined && attSeq!='')){
					return true;
				}
				
				va = $(this).find("input[name='attDispNm']").val();
				size=$(this).find('input[name="fileKb"]').val();
				
				var $last = $('#progressArea').find('.fileItem:last');
				var $clone = $last.clone();
				
				var p = va.lastIndexOf('\\');
				if (p > 0) va = va.substring(p + 1);
				$last.find('.title').text(va);
				p = va.lastIndexOf('.');
				if (p > 0) {
					var ext = va.substring(p + 1);
					if (extArr.contains(ext)) {
						var ico = $last.find('.file_icon');
						ico.addClass(ext);
					}
				}
				if(size!=undefined) $last.find('.file_size').text(size);
				
				$last.attr('id', $(this).attr('id'));
				$clone.insertAfter($last);
				$last.show();
			});
		}else{
			var $area=$('#progressArea');
			$area.find('#progressList').html('');
			var html=uploader.getFileListHtml();
			$area.find('#progressList').append(html);
			$area.find('#progressList').find('span.progress').show();	
			
			$area.find('#progressList li.fileItem').each(function(){				
				if($(this).css('display')!='none' && $(this).find('span.progress img').length==0)
					$(this).insertAfter("#progressList .fileItem:last");
					
			});
			
		}
		uploader.fileUpload();
	}
});
//-->
</script>
<div style="width:600px">
<div id="progressArea" class="upldr" style="height:300px;">
	<div class="upload_header">
		<span class="file_select dispNone" ></span>
		<span class="file_name"><u:msg titleId="cols.file.nm" alt="파일명"/></span>
		<span class="status"><u:msg titleId="cols.file.status" alt="업로드상태"/></span>
		<span class="file_size"><u:msg titleId="cols.file.capacity" alt="용량"/></span>
	</div>
	<div class="upload_list dropZone" id="progressList" style="height:270px;"><ul>
		<li id="fileList" class="fileItem" style="display:none;">
			<span class="file_select"><input type="checkbox" name="fileChk"></span>
			<span class="file_name"><label for="file_name_0">
				<span class="file_icon"></span><span class="title"></span></label>
			</span>
			<span class="status progress" ><img src="${_ctx}/images/${_skin}/pollbar.png" width="0%" height="10"></span>
			<span class="file_size"></span>
	</ul></div>
</div>
<u:blank />
<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.cancel" alt="취소" onclick="parent.uploadAbort(this);" auth="W" />
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>

</div>
