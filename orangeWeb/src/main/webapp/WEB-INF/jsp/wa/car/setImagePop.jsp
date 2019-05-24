<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="callback" test="${!empty param.callback }" value="${param.callback }" elseValue="setTmpImgId"/>
<script type="text/javascript">
<!--
function uploadImg(id, fileNm){
	if(fileNm==null) return;
	var $form = $("#imgUploadForm");
	$form.attr('action','/cm/transImgUpload.do?module=st&callback=setTmpImg&uploadId='+id);
	$form.attr('target','dataframe');
	$form[0].submit();
}
function setImage(tmpFileId, wdth, hght, size){
	var $img = $("#tmpImage");
	$img.attr("src", '/cm/viewTmpImage.do?tmpFileId='+tmpFileId);
	var maxWdth = 144;
	var maxhght = 180;
	var width = wdth <= maxWdth ? wdth : maxWdth;
	var height = hght <= maxhght ? hght : maxhght;
	$img.removeAttr('width');
	$img.removeAttr('height');
	if(wdth < hght) $img.attr('height', height);
	else $img.attr('width', width);
	$('#tmpFileId').val(tmpFileId);
}
function saveImg(){
	var $form = $("#imgUploadForm");
	var tmpFileId = $form.find('input[id="tmpFileId"]').val();	
	parent.${callback}(tmpFileId=='' ? null : tmpFileId);
	dialog.close('setImageDialog');
}
//-->
</script>
<div style="width:200px;">
<form id="imgUploadForm" name="imgUploadForm" method="post" enctype="multipart/form-data" >
	<input id="tmpFileId" type="hidden"/>
	<c:set var="maxWdth" value="144" />
	<c:set var="maxHght" value="180" />
	<u:set test="${!empty waCarImgDVo}" var="imgPath" value="${_cxPth}${waCarImgDVo.imgPath}" elseValue="${_cxPth}/images/${_skin}/photo_noimg_180.png" />
	<u:set test="${!empty waCarImgDVo && waCarImgDVo.imgWdth <= maxWdth}" var="imgWdth" value="${waCarImgDVo.imgWdth}" elseValue="${maxWdth}" />
	<u:set test="${!empty waCarImgDVo && waCarImgDVo.imgHght <= maxHght}" var="imgHght" value="${waCarImgDVo.imgHght}" elseValue="${maxHght}" />
	<u:set test="${waCarImgDVo.imgWdth < waCarImgDVo.imgHght}" var="imgWdthHgth" value="height='${imgHght}'" elseValue="width='${imgWdth}'" />
	<div class="listarea" style="width:100%;text-align:center;">
		<table class="listtable" border="0" cellpadding="0" cellspacing="1"><tbody>
		<tr>
		<td class="photo_ct" style="height:180px;">
			<c:if test="${waCarImgDVo.imgWdth > 800}"	>
				<a href="${_ctx}${waCarImgDVo.imgPath}" target="viewPhotoWin"><img id="tmpImage" src="${imgPath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg_180.png"' ${imgWdthHgth}></a>
			</c:if>
			<c:if test="${empty waCarImgDVo || waCarImgDVo.imgWdth <= 800}">
				<img id="tmpImage" src="${imgPath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg_180.png"' ${imgWdthHgth}>
			</c:if>
		</td>
		</tr>
		</tbody></table>
	</div>
	<c:if test="${!empty isView && isView == false}">
	<div><u:file id="photo" titleId="st.btn.changeImg" alt="이미지 변경"
			mandatory="Y" exts="gif,jpg,jpeg,png,tif"  onchange="uploadImg" />
	</div>
	<p style="float:left;"><u:mandatory /><u:msg titleId="pt.jsp.setSkinImg.recommend" alt="권장사이즈"/>(180 X 180)</p>
	</c:if>
</form>
<u:blank />
<u:buttonArea>
	<c:if test="${!empty isView && isView == false}">
	<u:button titleId="cm.btn.confirm" alt="확인" href="javascript:saveImg();" auth="W" />
	</c:if>
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>

</div>