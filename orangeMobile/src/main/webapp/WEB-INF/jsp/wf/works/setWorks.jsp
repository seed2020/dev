<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="params" /><u:params var="paramsForList" excludes="workNo"/>
<script type="text/javascript">
//<![CDATA[
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
<% // 폼에 json으로 저장 %>
function setFormData(form){
	var data=[];
	form.find('div.loutFormArea').each(function(idx, lout){
		if($(this).html()=='') return true;
		$array=$(this).find(':input').serializeArray();
		if($array.length>0) $.merge(data, $array);
	});
	
	data=objToJson(data, false, true);
	form.find("input[name='jsonVa']").remove();
	form.appendHidden({name:'jsonVa',value:JSON.stringify(data)});
	
}
<% // [유효성검증:기타 - 체크박스,라디오,사용자,부서] %>
function validateEtc(id){
	var isValidate=true;
	var chkList=$('#'+id+' div[class="mandatory"]');
	console.log(chkList.length);
	if(chkList.length>0){
		var type;
		$.each(chkList, function(){
			type=$(this).attr('data-validateType');
			if(type){
				if(type=='check' || type=='radio'){
					$name=$(this).attr('data-name');
					if($name)
						$chkLen=$(this).find('input[name="'+$name+'"]:checked').length;
					else
						return true;
				}else if(type=='calendar'){
					$name=$(this).attr('data-name');
					if($name)
						$chkLen=$(this).find('input[name="'+$name+'"]').val()=='' ? 0 : 1;
					else
						return true;
				}else if(type=='ubox'){
					$chkLen=$(this).find('div[class="ubox"]').length;
				}else return true;
				
				if($chkLen==0){
					isValidate=false;
					$title=$(this).attr('data-title');
					if($title){
						alert($m.msg.callMsg('cm.input.check.mandatory',[$title]));
						return false;
					}else return true;
				}
				
			}
		});
	}
	return isValidate;
}
<% //[하단버튼:저장] - 저장 %>
function save(){	
	if (validator.validate('setForm') && validateEtc('setForm')) {
		
		var editors = $('textarea[id^=editor]');
		if(editors.length>0){
			var isOverSize=false;
			$.each(editors, function(){
				if (isInUtf8Length($(this).val(), 0, '${bodySize}') > 0) {
					$m.msg.alertMsg("cm.msg.attach.not.support.ext",[$(this).attr('data-title').attr('data-name'),'${bodySize}']);
					isOverSize=true;
					return false;
				}
			});
			if(isOverSize) return;
		}
		
		var $form = $('#setForm');		
		$form.attr('method','post');
		$form.attr('action','/wf/works/${transPage}Post.do?menuId=${menuId}');
		$form.attr('enctype','multipart/form-data');
		
		setFormData($form);
		
		$m.nav.post($form);
	}
}
<% // [하단버튼:취소] %>
function cancelWorks(){
	history.go(-1);
}
$(document).ready(function() {
	$("input[type='text'], textarea").each(function(){
		$space.apply(this, {relative:30});
	});
	
	$layout.adjustBodyHtml('bodyHtmlArea');	
});
//]]>
</script>
<u:secu auth="W">	
<div class="btnarea">
    <div class="size">
        <dl>
        	<dd class="btn" onclick="save();"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
        	<dd class="btn" onclick="cancelWorks();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>           	 
     </dl>
    </div>
</div>
</u:secu>
         
<section id="section">	
<jsp:include page="/WEB-INF/jsp/wf/works/inclTab.jsp" flush="false" /><!-- 탭 -->

    <div class="blankzone">
        <div class="blank20"></div>
    </div>
    
	<form id="setForm">
	<m:input type="hidden" id="menuId" value="${menuId}" />
	<m:input type="hidden" id="listPage" value="/wf/works/${listPage}.do?${paramsForList}" />
	<m:input type="hidden" id="viewPage" value="/wf/works/${viewPage}.do?${params}" />
	<m:input type="hidden" id="genId" value="${wfFormBVo.genId }" />
	<m:input type="hidden" id="formNo" value="${param.formNo }" />
	<c:if test="${!empty param.workNo }"><m:input type="hidden" id="workNo" value="${param.workNo }" /></c:if>
	
	<jsp:include page="/WEB-INF/jsp/wf/works/inclRegForm.jsp" flush="false" />
	
	</form>
	
	<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
 </section>
