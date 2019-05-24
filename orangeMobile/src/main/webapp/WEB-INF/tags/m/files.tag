<%@ tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="java.util.HashSet"
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
%>
<style type="text/css">
<!--
.sizeZero { width: 0; height: 0; }
.dispNone { display: none; }
--> 
</style>
<script type="text/javascript">
<!--
<% // 파일 초기화 %>
function resetFile(obj) {
	$(obj).before($(obj).clone());
	$(obj).remove();
}
function addFile(id) {
	$('#'+id+'_file').trigger('click');
}       
           
function setFile(obj, exts, extsTyp) {
	var va = $(obj).val();
	if (va != '') {
		if(exts!=null && exts!=""){
			var matched = false;
			extArr = exts.toLowerCase().split(",");
			extArr.each(function(index, ext){
				if(va.toLowerCase().endsWith("."+ext.trim())){
					matched = true;
					return false;
				}
			});
			if(!matched && extsTyp != undefined && extsTyp == 'A'){
				$m.msg.alertMsg('cm.msg.attach.not.support.ext',[exts]);
				resetFile(obj);
				return;
			}
			if(matched && extsTyp != undefined && extsTyp == 'L'){
				$m.msg.alertMsg('cm.msg.attach.not.limit.ext',[exts]);
				resetFile(obj);
				return;
			}
		}
		var $last = $('.filearea:last');
		var $clone = $last.clone();
		$last.removeClass('tmp');
		$last.show();
		var p = va.lastIndexOf('\\');
		if (p > 0) va = va.substring(p + 1);
		$last.find('#${id}_fileView').text(va).removeAttr('id');
		$last.find('input[name="${id}_valid"]').val('Y');
		
		$area = $(obj).parents('.${id}attachBtnArea');
		var $cloneArea = $('#${id}attachBtnAreaParent').clone();
		$cloneArea.replaceWith( $cloneArea.clone(true) );
		$cloneArea.insertBefore($area);

		var id = $area.attr('id');
		var fileSeq = id.substring(id.lastIndexOf('_')+1);
		$last.find('input[name="${id}_fileSeq"]').val(fileSeq);
		fileSeq++;
		$cloneArea.attr('id','${id}attachBtnArea_'+fileSeq);
		$clone.insertAfter($last);
	}
}      
           
function delFile(checkedObj) {
	$m.msg.confirmMsg("cm.cfrm.del", null, function(result){ <% // cm.cfrm.del=삭제하시겠습니까 ? %>
		if(result){
			$area = $(checkedObj).parents('.filearea');
			if ($area.hasClass('tmp') == false) {
				$area.find('input[name="${id}_useYn"]').val('N');
				var fileSeq = $area.find('input[name="${id}_fileSeq"]').val();
				if(fileSeq != undefined){
					$('#${id}attachBtnArea_'+fileSeq).remove();
				}
				$area.hide();
			}
		}
	});
}
//-->
</script>
<!--attachzone S-->
            
<div class="attachzone" id="${id}Area">
<div class="attacharea">

	<div id="${id}attachBtnArea_0" class="${id}attachBtnArea" style="display:none"><input type="file" id="${id}_file" name="${id}_file" onchange="setFile(this,'<%= (exts==null ? "" : exts)%>','<%= (extsTyp==null ? "" : extsTyp)%>');" /></div>
	<div id="${id}attachBtnAreaParent" class="${id}attachBtnArea" style="display:none"><input type="file" id="${id}_file" name="${id}_file" onchange="setFile(this,'<%= (exts==null ? "" : exts)%>','<%= (extsTyp==null ? "" : extsTyp)%>');" /></div>
	<c:if test="${fn:length(fileVoList) > 0}">
		<c:forEach items="${fileVoList}" var="fileVo" varStatus="status">
		<c:set var="fileSizeKB" value="${fileVo.fileSize/1024 }"/>
		<c:set var="fileSizeMB" value="${fileVo.fileSize/(1024*1024) }"/>
		<c:set var="fileSizeTemp" value="${fileSizeKB+(1-(fileSizeKB%1))%1}"/>
		<u:set var="fileSize" test="${fileSizeTemp > 999 }" value="${fileSizeMB+(1-(fileSizeMB%1))%1 }" elseValue="${fileSizeTemp }"/>
		<u:set var="fileSizeUnit" test="${fileSizeTemp > 999 }" value="MB" elseValue="KB"/>
		<u:set var="ctSendYn" test="${ctSendYn == 'Y' }" value="Y" elseValue="N"/>
			<div class="attachin filearea">
				<div class="attach" onclick="javascript:;">
					<div class="btn"></div>
					<div class="txt">${fileVo.dispNm}(<fmt:formatNumber value="${fileSize}" type="number"/>${fileSizeUnit })</div>
				</div>
				<div class="delete" onclick="javascript:delFile(this);"></div>
				<input type="hidden" name="${id}_fileId" value="${fileVo.fileId}" />
				<input type="hidden" name="${id}_valid" value="${ctSendYn}" />
				<input type="hidden" name="${id}_useYn" value="${fileVo.useYn}" />
				<input type="hidden" name="${id}_ctSendYn" value="${ctSendYn}" />
			</div>
		</c:forEach>
	</c:if>

	<div class="attachin filearea tmp" style="display:none">
	<div class="attach" >
		<div class="btn"></div>
		<div class="txt"><span id="${id}_fileView"></span></div>
	</div>
	<div class="delete" onclick="javascript:delFile(this);"></div>
	<input type="hidden" name="${id}_fileSeq" value="" />
	<input type="hidden" name="${id}_fileId" value="" />
	<input type="hidden" name="${id}_valid" value="N" />
	<input type="hidden" name="${id}_useYn" value="Y" />
	<input type="hidden" name="${id}_ctSendYn" value="" />
	</div>

</div>
</div>
<!--//attachzone E-->