<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[
function fnChecked(dd, type, quesId, examOrdr){	
	var obj = $('input:'+(type=='check'?type+'box':type)+'[id="'+type+quesId+'"]:eq('+(examOrdr-1)+')');
	
	if(type == 'radio'){
		$('dd#radioDD'+quesId).each(function(){
			$(this).attr("class", type);
		});
	}
	
	if(type == 'radio')
		$(dd).attr("class", type+'_on');
	
	if(obj.is(":checked") == false){
		obj.prop('checked',true);
	}
	else{
		if(type == 'check'){
			obj.prop('checked',false);
		}
	}
}

function saveOpin(){
	if($("#opinSubj").val().trim()==''){
		// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [제목] %>
		$m.msg.alertMsg("cm.input.check.mandatory", ["#cols.subj"], function(){
			$("#setOpinForm input[name='opinSubj']").focus();
		});
		return;
	}
	
	$m.msg.confirmMsg("cm.cfrm.save", null, function(result){
		if(result){
			var $form = $('#setOpinForm');
			$form.attr('action','/ct/debr/transSaveOpinPost.do?menuId=${menuId}&ctId=${ctId}');
			$m.nav.post($form);
		}
	});
	
}

function getEditHtml(){
	return $('#bodyHtmlArea').html();
}
function setEditHtml(editHtml){
	$('#bodyHtmlArea').html(editHtml);
	$('#opin').html(editHtml);
}

$(document).ready(function() {
	$("input[type='text'], textarea").each(function(){
		$space.apply(this, {relative:30});
	});
	$layout.adjustBodyHtml('bodyHtmlArea');
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('list');
});

//]]>
</script>
<div class="btnarea">
    <div class="size">
        <dl>
        <dd class="btn" onclick="history.back();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>
        <dd class="btn" onclick="saveOpin();"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
     </dl>
    </div>
</div>
<section>
    <div class="blankzone">
        <div class="blank20"></div>
    </div>
    
	<form id="setOpinForm">
	<input type="hidden" id="menuId" name="menuId" value="${menuId}" />
	<input type="hidden" id="ctId" name="ctId" value="${ctId}" />
	<input type="hidden" id="debrId" name="debrId" value="${debrId}" />
	<input type="hidden" id="opinOrdr" name="opinOrdr" value="${opinOrdr}" />
	
    <textarea id="opin" name="opin" style="display:none">${ctDebrOpinDVo.opin}</textarea>
    <div class="entryzone">
        <div class="entryarea">
        <dl>
        <dd class="etr_tit"><u:msg titleId="ct.btn.opinReg" alt="의견 등록"/></dd>
        
        <dd class="etr_bodytit"><span><u:out value="${ctDebrBVo.subj}"/></span></dd>


        <dd class="etr_bodytit_asterisk"><u:msg titleId="cols.subj" alt="의견" /></dd>
        <dd class="etr_input"><div class="etr_inputin"><input type="text" id="opinSubj" name="opinSubj" class="etr_iplt" value="${ctDebrOpinDVo.subj}" /></div></dd>
	
		
		<dd class="etr_bodytit_asterisk"><u:msg titleId="cols.fna" alt="찬반" /></dd>
        <dd class="etr_input">
        		<div class="etr_ipmany">
					<dl>
				<c:if test="${ctDebrOpinDVo.prosConsCd == null || ctDebrOpinDVo.prosConsCd == 'A'}">
					<c:set var="prosCdA" value="true"/>
				</c:if>
				<c:if test="${ctDebrOpinDVo.prosConsCd == 'O'}">
					<c:set var="prosCdO" value="true"/>
				</c:if>
				<c:if test="${ctDebrOpinDVo.prosConsCd == 'E'}">
					<c:set var="prosCdE" value="true"/>
				</c:if>
				
         			<m:check type="radio" id="radioDDfna" name="fna" 
         			inputId="radiofna" value="A" 
         			onclick="fnChecked(this, 'radio','fna', 1);" 
         			checked="${prosCdA }"/>
					<dd class="etr_body"><u:msg titleId="ct.option.for" alt="찬성"/></dd>	
         			<m:check type="radio" id="radioDDfna" name="fna" 
         			inputId="radiofna" value="O" 
         			onclick="fnChecked(this, 'radio','fna', 2);" 
         			checked="${prosCdO }"/>
         			<dd class="etr_body"><u:msg titleId="ct.option.against" alt="반대"/></dd>	
         			<m:check type="radio" id="radioDDfna" name="fna" 
         			inputId="radiofna" value="E" 
         			onclick="fnChecked(this, 'radio','fna', 3);" 
         			checked="${prosCdE }"/>
         			<dd class="etr_body"><u:msg titleId="ct.option.etc" alt="기타"/></dd>	
						</dl>		
        </div></dd>
	
		
		</dl>
		</div>
	</div>

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
         <dd class="etr_input"><div class="etr_bodyline editor" id="bodyHtmlArea"><u:out value="${ctDebrOpinDVo.opin}" type="noscript" /></div></dd>


		</dl>
		</div>
	</div>
	
    <div class="blank25"></div>
    <div class="btnarea">
        <div class="size">
            <dl>
            <dd class="btn" onclick="history.back();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>
            <dd class="btn" onclick="saveOpin();"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
         </dl>
        </div>
    </div>

	
	</form>
<jsp:include page="/WEB-INF/jsp/ct/inc/footerInc.jsp" flush="false" />
 </section>