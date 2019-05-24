<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="nonPageParams" excludes="docId,pageNo,docPid,noCache"/>
<u:params var="viewPageParams" excludes="docId,docPid,noCache"/>
<u:set var="includeParams" test="${empty dmDocLVoMap.docId && !empty dmDocLVoMap.docPid}" value="&docId=${dmDocLVoMap.docPid }" elseValue="&docId=${dmDocLVoMap.docId }"/>
<script type="text/javascript">
//<![CDATA[
function getEditHtml(){
	return $('#bodyHtmlArea').html();
};

function setEditHtml(editHtml){
	$('#bodyHtmlArea').html(editHtml);
	$('#cont').html(editHtml);
};

function setFileInfo(id , va){
	$('#'+id+'Area').find('.filearea').each(function(){
		if(!$(this).hasClass('tmp')){
			$(this).remove();
		}
	});
	if(va == null ) {
		resetFileTag(id);
		return;
	};
	
	var $last = $('#'+id+'Area .filearea:last');
	var $clone = $last.clone();
	$last.removeClass('tmp');
	$last.show();
	
	var p = va.lastIndexOf('\\');
	if (p > 0) va = va.substring(p + 1);
	$last.find('#'+id+'_fileView').text(va).removeAttr('id');
	$clone.insertAfter($last);	
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
};<%// [버튼] 저장 %>
function save(){
	if (validator.validate('setForm')){
		$m.msg.confirmMsg("cm.cfrm.save", null, function(result){
			if(result){
				var $form = $('#setForm');
				// 추가항목 조회
				if($('#listAddItemArea').html() != ''){
					var arrs = getChkVal();
					if(arrs != null){
						$.each(arrs,function(index,vo){
							$form.find("[name='"+vo.name+"']").remove();
							$form.appendHidden({name:vo.name,value:vo.value});
						});	
					}
				}
				$form.attr('method','post');
				$form.attr('action','/dm/doc/${transPage}Post.do?menuId=${menuId}');
				
				$m.nav.post($form);
			}
		});
	}
}<%// [하단버튼:저장] %>
function saveDoc(){
	var $form = $("#setForm");
	$form.find("[name='setPage']").remove();
	save();
}<%// [하단버튼:계속등록] %>
function saveContinue(){
	var $form = $("#setForm");
	$form.find("[name='setPage']").remove();
	$form.appendHidden({name:'setPage',value:'/dm/doc/${setPage}.do?${nonPageParams}'});
	save();
}
<% //Select Option 클릭 %>
function setSelOptions(codeNm, code, value){
	var $form = $("#setForm");
	$form.find("input[name='"+codeNm+"']").val(code);
	$form.find("#"+codeNm+"Container #selectView span").text(value);
	$form.find("#"+codeNm+"Container #"+codeNm+"Open").hide();
}<% // [하단버튼:취소] %>
function cancelDoc(){
	history.go(-1);
}
var listAddItemArea = null;
<%// 폴더를 변경할 경우 페이지를 리로딩해준다.[폴더에 등록된 유형에 대한 추가항목 로드] %>
function setPageChk(prefix){
	if(prefix == 'cls') return;
	$m.ajax('${_cxPth}/dm/doc/listPsnAddItemAjx.do?menuId=${menuId}', {fldId:$('#fldId').val(),paramStorId:'${paramStorId}',docId:'${param.docId}'}, function(html){
		if(listAddItemArea==null) listAddItemArea = $('#section').find('#listAddItemArea');
		listAddItemArea.html(html);
	}, {mode:'HTML', async:true});
};<%// 분류,폴더 Prefix %>
function getTabPrefix(lstTyp){
	var prefix = "fld";
	if(lstTyp == 'C') prefix = "cls";
	return prefix;
}<%// [버튼] 분류,폴더 %>
function findFldPop(lstTyp){
	var prefix = getTabPrefix(lstTyp);
	var $area = $("#"+prefix+"InfoArea"), data = [];
	$area.find("input[id='"+prefix+"Id']").each(function(){
		data.push($(this).val());
	});
	var url = '/dm/doc/findFldSub.do?menuId=${menuId}&lstTyp='+lstTyp+"&fldId="+data+"&fncMul="+(lstTyp == 'C' ? 'Y':'N');
	$m.nav.next(null,url);
};<%// 분류,폴더 적용%>
function setFldInfos(arr, lstTyp){
	var prefix = getTabPrefix(lstTyp);
	$area = $('#'+prefix+'InfoArea');
	
	var buffer = [];
	var nms = '';
	arr.each(function(index, obj){
		buffer.push("<input type='hidden' id='"+prefix+"Id' name='"+prefix+"Id' value='"+obj.id+"'/>\n");
		nms+= nms == '' ? obj.nm : ','+obj.nm;
	});
	$area.find('#idArea').html('');
	$area.find('#idArea').html(buffer.join(''));
	$area.find('#nmArea input[id="'+prefix+'Nm"]').val(nms);
	setPageChk(prefix);
}
var holdHide = false, holdHide2 = false, holdHideExt = false;
$(document).ready(function() {
	$("input[type='text'], textarea").each(function(){
		$space.apply(this, {relative:30});
	});
	
	var bodyHtml = $("#lobHandlerArea").html();
	if(bodyHtml!=''){
		setEditHtml(bodyHtml);
	}
	$layout.adjustBodyHtml('bodyHtmlArea');
	<c:if test="${!empty param.docId}">setPageChk('fld');</c:if>
});
//]]>
</script>
<u:secu auth="W">	
<div class="btnarea">
    <div class="size">
        <dl>
			<u:secu auth="W">
				<c:if test="${empty param.docId }"><dd class="btn" onclick="saveContinue();"><u:msg titleId="dm.btn.registered.continued" alt="계속등록" /></dd></c:if>
				<dd class="btn" onclick="saveDoc();"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
			</u:secu>
			<dd class="btn" onclick="cancelDoc();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>
     </dl>
    </div>
</div>
</u:secu>
<section id="section">

    <div class="blankzone">
        <div class="blank20"></div>
    </div>
    
	<form id="setForm" name="setForm" enctype="multipart/form-data" action="/dm/${transPage}.do?menuId=${menuId}">
	<input type="hidden" name="menuId" value="${menuId}" />
	<m:input type="hidden" id="listPage" value="./${listPage}.do?${nonPageParams}" />
	<m:input type="hidden" id="viewPage" value="./${viewPage}.do?${viewPageParams}&docId=${dmDocLVoMap.docId }" />
	<m:input type="hidden" id="docId" value="${dmDocLVoMap.docId }" />
	
	<textarea id="cont" name="cont" style="display:none"></textarea>
	
    <div class="entryzone">
        <div class="entryarea">
        <dl>
        <dd class="etr_tit"><u:msg titleId="dm.jsp.setDoc.${!empty dmDocLVoMap.docId ? 'mod' : 'reg'}.title" alt="문서등록/문서수정" /></dd>
        <dd class="etr_bodytit_asterisk"><u:msg titleId="cols.subj" alt="제목" /></dd>
        <dd class="etr_input"><div class="etr_inputin"><m:input id="subj" titleId="cols.subj" value="${dmDocLVoMap.subj}" maxByte="240" mandatory="Y" style="width:95%;"/></div></dd>
		
			<dd class="etr_bodytit_asterisk"><u:msg titleId="cols.fld" alt="폴더" /></dd>
			<dd class="etr_input">
				<div class="etr_ipmany">
				<dl>
				<dd class="etr_ip_lt">
					<div id="fldInfoArea" >
						<div id="idArea" style="display:none;"><input type="hidden" id="fldId" name="fldId" value="${dmDocLVoMap.fldId}" /></div>
						<div id="nmArea" style="display:inline;"><m:input type="text" id="fldNm" name="fldNm" className="etr_iplt" value="${dmDocLVoMap.fldNm}" style="width:55%;" readonly="Y" mandatory="Y"/></div>
	              	</div>
				</dd>
				<dd class="etr_se_rt" ><div class="etr_btn" onclick="findFldPop('F');"><u:msg titleId="dm.btn.fldSel" alt="폴더선택"/></div></dd>
				</dl>
				</div>
			</dd>
		</dl>
		</div>
	</div>
	<div class="entryzone" id="listAddItemArea"></div>
	
    <div class="blankzone">
        <div class="blank25"></div>
        <div class="line1"></div>
        <div class="line8"></div>
        <div class="line1"></div>
        <div class="blank25"></div>
    </div>

    <div class="entryzone">
        <div class="entryarea">
        <dl>

         <dd class="etr_bodytit">
         	<div class="icotit_dot"><u:msg titleId="cols.cont" alt="내용" /></div>
            <div class="icoarea">
            <dl>
            <dd class="btn" onclick="$m.openEditor();"><u:msg titleId="mcm.title.editCont" alt="내용편집" /></dd>
            </dl>
            </div>
         </dd>
         <dd class="etr_input"><div class="etr_bodyline editor" id="bodyHtmlArea"><c:if test="${!empty dmDocLVoMap.cont }"><u:out value="${dmDocLVoMap.cont}" type="noscript" /></c:if></div></dd>

		</dl>
		</div>
	</div>
       <!--entryzone S-->
       <div class="entryzone">
           <div class="blank20"></div> 
           <div class="entryarea">
           <dl>
           <dd class="etr_bodytit">
               <div class="icotit_dot"><u:msg titleId="cols.attFile" alt="첨부파일" /></div>
               <div class="icoarea">
               <dl>
               <dd class="btn" onclick="addFile('${filesId}');"><u:msg titleId="cm.btn.fileAtt" alt="파일첨부"  /></dd>
               </dl>
               </div>
           </dd>
           </dl>
           </div>
       </div>
       <!--//entryzone E-->
       <m:files id="${filesId}" fileVoList="${fileVoList}" module="dm" mode="set" exts="${exts }" extsTyp="${extsTyp }"/>
	
    <div class="blank25"></div>
    <u:secu auth="W">
    <div class="btnarea">
        <div class="size">
            <dl>
            	<u:secu auth="W">
					<c:if test="${empty param.docId }"><dd class="btn" onclick="saveContinue();"><u:msg titleId="dm.btn.registered.continued" alt="계속등록" /></dd></c:if>
					<dd class="btn" onclick="saveDoc();"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
				</u:secu>
				<dd class="btn" onclick="cancelDoc();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>
         </dl>
        </div>
    </div>
	</u:secu>
	
	</form>
	
	<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
 </section>
 <div id="lobHandlerArea" style="display:none;"><c:if test="${!empty dmDocLVoMap.cont }">${dmDocLVoMap.cont }</c:if><u:clob lobHandler="${lobHandler }"/></div>