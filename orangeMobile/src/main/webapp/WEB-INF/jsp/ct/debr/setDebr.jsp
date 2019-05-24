<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[


function saveDebr(){
	if($("#topc").val().trim()==''){
		// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [주제] %>
		$m.msg.alertMsg("cm.input.check.mandatory", ["#cols.topc"], function(){
			$("#setDebrForm input[name='topc']").focus();
		});
		return;
	}
	
	if($("#itnt").val().trim()==''){
		// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [취지] %>
		$m.msg.alertMsg("cm.input.check.mandatory", ["#cols.itnt"], function(){
			$("#setDebrForm textarea[name='itnt']").focus();
		});
		return;
	}
	
	$m.msg.confirmMsg("cm.cfrm.save", null, function(result){
		if(result){
			$m.nav.curr(event, '/ct/debr/transSaveDebr.do?menuId=${menuId}&ctId=${ctId}&'+$('#setDebrForm').serialize());
		}
	});
}

$(document).ready(function() {
	$("input[type='text'], textarea").each(function(){
		$space.apply(this, {relative:30});
	});
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('list');
});

//]]>
</script>
<div class="btnarea">
    <div class="size">
        <dl>
        	<dd class="btn" onclick="history.back();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>           	 
           	<dd class="btn" onclick="saveDebr();"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
     </dl>
    </div>
</div>
<section>

    <div class="blankzone">
        <div class="blank20"></div>
    </div>
    
	<form id="setDebrForm">
	<input id="debrId" name="debrId" value="${ctDebrBVo.debrId}" type="hidden"/>
    
    <div class="entryzone">
        <div class="entryarea">
        <dl>
        <dd class="etr_tit"><u:msg titleId="ct.btn.debrReg" alt="토론방등록" /></dd>
        

        <dd class="etr_bodytit_asterisk"><u:msg titleId="cols.topc" alt="주제" /></dd>
        <dd class="etr_input"><div class="etr_inputin"><input type="text" id="topc" name="topc" class="etr_iplt" value="${ctDebrBVo.subj}" /></div></dd>
	
        <dd class="etr_bodytit_asterisk"><u:msg titleId="cols.itnt" alt="취지" /></dd>
        <dd class="etr_input"><div class="etr_textareain"><textarea rows="5" id="itnt" name="itnt" class="etr_ta">${ctDebrBVo.estbItnt}</textarea></div></dd>
	
	
		</dl>
		</div>
	</div>

   
    <div class="blank25"></div>
    <div class="btnarea">
        <div class="size">
            <dl>
            <dd class="btn" onclick="history.back();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>
            <dd class="btn" onclick="saveDebr();"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
         </dl>
        </div>
    </div>

	
	</form>
	
<jsp:include page="/WEB-INF/jsp/ct/inc/footerInc.jsp" flush="false" />
 </section>