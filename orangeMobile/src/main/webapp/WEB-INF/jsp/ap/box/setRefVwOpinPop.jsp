<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:secu
	auth="W"><u:set test="${true}" var="writeAuth" value="Y"
/></u:secu>
<script type="text/javascript">
//<![CDATA[
function saveRefVwOpin(){
	var opin = $("#setRefVwOpinForm #opin").val();
	$m.ajax("${_cxPth}/ap/box/transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}", {process:"processRefVw", apvNo:"${param.apvNo}", refVwOpinCont:opin}, function(data){
		if(data.message != null){
			$m.dialog.alert(data.message, function(){
				if(data.result == 'ok'){
					$m.dialog.close('apvRefVwStatPop');
					$m.nav.prev(null, true);
				}
			});
		} else {
			if(data.result == 'ok'){
				$m.dialog.close('apvRefVwStatPop');
				$m.nav.prev(null, true);
			}
		}
	});
}
//]]>
</script>

<form id="setRefVwOpinForm">
<div class="blankzone"><div class="blank15"></div></div>

<div class="pop_entryzone" >
<div class="entryarea">
	<dl>
		<dd class="etr_blank"></dd>
		<dd class="etr_tit"><u:msg titleId="ap.doc.opin" alt="의견" /></dd>
		<dd class="etr_input"><div class="etr_textareain"><textarea id="opin" name="opin" rows="4" class="etr_ta"></textarea></div></dd>
	</dl>
</div>
</div>

<div class="popbtnarea">
<div class="btnarea">
	<div class="size">
	<dl><u:secu auth="W">
		<dd class="btn" onclick="saveRefVwOpin()"><u:msg titleId="ap.term.cfrmRefVw" alt="열람확인" /></dd>
		</u:secu>
		<dd class="btn" onclick="$m.dialog.close('apvStatPop')"><u:msg titleId="cm.btn.close" alt="닫기" /></dd>
	</dl>
	</div>
</div>
</div>
</form>