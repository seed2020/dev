<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%// 확인 버튼 클릭 %>
function setApvLn(){
	var param = new ParamMap().getData("setApvLnForm");
	setApvLnData(param, "${param.formApvLnTypCd}");<%// - setFormEdit.jsp %>
	dialog.close("setDocApvLnDialog");
}
<%// 결재방 모양별 최대 갯수 설정 -  %>
function checkMaxCell(obj){
	var signAreaMaxCnt = parseInt('${signAreaMaxCnt==8 ? 8 : signAreaMaxCnt-1}');

	var $form = $("#setApvLnForm");
	var $max1 = $form.find("[name='${apvLnTitlTypCd1}-maxCnt']");
	var $max2 = $form.find("[name='${apvLnTitlTypCd2}-maxCnt']");
	var va1 = parseInt($max1.find(":checked").val(),10);
	var va2 = parseInt($max2.find(":checked").val(),10);
	
	if(va1 + va2 > signAreaMaxCnt){
		if($(obj).attr("id")==$max1.attr("id")){
			//$max2.val(signAreaMaxCnt-va1).trigger('click');
			$max2.val(signAreaMaxCnt-va1).uniform.update();
		} else {
			//$max1.val(signAreaMaxCnt-va2).trigger('click');
			$max1.val(signAreaMaxCnt-va2).uniform.update();
		}
	}
}
<%// 기준위치 - 좌우정렬 - 한쪽이 좌정렬이면 다른쪽은 우정렬로 변경 %>
function checkAlign(obj){
	var $form = $("#setApvLnForm");
	var $aln1 = $form.find("[name='${apvLnTitlTypCd1}-alnVa']");
	var $aln2 = $form.find("[name='${apvLnTitlTypCd2}-alnVa']");
	if($aln1.find(":checked").val() == $aln2.find(":checked").val()){
		var va = ($(obj).find(":checked").val()=='left' ? 'right' : 'left');
		if($(obj).attr("id")==$aln1.attr("id")){
			$aln2.val(va).trigger('click');
		} else {
			$aln1.val(va).trigger('click');
		}
	}
}
$(document).ready(function() {
	var signAreaMaxCnt = parseInt('${signAreaMaxCnt==8 ? 8 : signAreaMaxCnt-1}');
	
	var param = new ParamMap();
	$("#docArea").children("#apvLnArea").each(function(){
		param.getData(this);
	});
	["apv","agr","req","prc"].each(function(index, va){<%//apv:결재, agr:합의, req:신청, prc:처리%>
		var maxCnt = param.get(va+"-maxCnt");
		if(maxCnt!=null && parseInt(maxCnt)>signAreaMaxCnt) {
			maxCnt = signAreaMaxCnt-1;
			param.put(va+"-maxCnt", maxCnt);
		} else {
			maxCnt = parseInt(maxCnt);
		}
		var hiddenMaxCnt = param.get((va=='apv' ? 'agr' : 'prc')+"-maxCnt");
		if(parseInt(maxCnt)+parseInt(hiddenMaxCnt)>signAreaMaxCnt){
			param.put((va=='apv' ? 'agr' : 'prc')+"-maxCnt", ""+(signAreaMaxCnt - parseInt(maxCnt)));
		}
		
	});
	param.removeEmpty().setData("setApvLnForm");
	checkAlign($("#${apvLnTitlTypCd1}-alnVa")[0]);
});
//-->
</script>

<div style="width:650px">
<form id="setApvLnForm">

<u:tabGroup id="apvLnTab" noBottomBlank="true">
	<u:tab id="apvLnTab" areaId="apvLnTab1" termId="ap.term.${apvLnTitlTypCd1}" on="true" />
	<u:tab id="apvLnTab" areaId="apvLnTab2" termId="ap.term.${apvLnTitlTypCd2}" on="false" />
</u:tabGroup>

<u:tabArea id="apvLnTab1" outerStyle="" innerStyle="margin: 0px 10px 0px 10px; padding-top:10px;" noBottomBlank="true">
	<div style="float:left; width: 4%; text-align: center;"><u:radio name="${apvLnTitlTypCd1}-apvLnDispTypCd" value="3row" checked="true" /></div>
	<div style="float:right; width:95%;">
		<label for="${apvLnTitlTypCd1}-apvLnDispTypCd3row">
		<table class="approvaltable" border="0" cellpadding="0" cellspacing="1"><tbody>
		<tr>
			<td rowspan="3" class="approval_head"><u:msg titleId="ap.signArea.${apvLnTitlTypCd1}" charSeperator="<br/>" alt="결<br/>재" /></td>
			<td class="approval_body"><u:msg titleId="ap.cmpt.signArea.sampleTitle" alt="과장" /></td>
		</tr>
		<tr>
			<td class="approval_img"><img src="${_ctx}/images/etc/etc_s.png"></td>
		</tr>
		<tr>
			<td class="approval_body"><u:msg titleId="ap.cmpt.signArea.sampleDate" alt="12/25" /></td>
		</tr>
		</tbody></table>
		</label>
	</div>
	<u:blank />
	
	<u:listArea>
		<tr>
		<td width="100" class="head_ct"><u:msg titleId="cm.option.config" alt="설정" /></td>
		<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			<td class="width12"></td>
			<td class="bodyip_lt"><u:msg titleId="ap.cmpt.maxCnt" alt="최대갯수" /></td>
			<td class="width10"></td>
			<td><select id="${apvLnTitlTypCd1}-maxCnt" name="${apvLnTitlTypCd1}-maxCnt" onchange="checkMaxCell(this);"<u:elemTitle titleId="ap.cmpt.maxCnt" alt="최대갯수" />>
				<c:forEach begin="1" end="${signAreaMaxCnt==8 ? 7 : signAreaMaxCnt-2}" var="no"><option value="${no}">${no}</option></c:forEach>
				</select></td>
			<td class="width12"></td>
			<td class="bodyip_lt"><u:msg titleId="ap.cmpt.align" alt="기준위치" /></td>
			<td class="width10"></td>
			<td><select id="${apvLnTitlTypCd1}-alnVa" name="${apvLnTitlTypCd1}-alnVa" onchange="checkAlign(this);"<u:elemTitle titleId="ap.cmpt.align" alt="기준위치" />>
				<option value="right"><u:msg titleId="cm.aln.right" alt="우측 정렬" /></option>
				<option value="left" selected="selected"><u:msg titleId="cm.aln.left" alt="좌측 정렬" /></option>
				</select></td>
			<td class="width13"></td>
			</tr>
			<tr>
			<td class="width12"></td>
			<td class="bodyip_lt"><u:msg titleId="ap.cmpt.border" alt="테두리선" /></td>
			<td class="width10"></td>
			<td><select id="${apvLnTitlTypCd1}-bordUseYn" name="${apvLnTitlTypCd1}-bordUseYn"<u:elemTitle titleId="ap.cmpt.border" alt="테두리선" />>
				<option value="Y"><u:msg titleId="cm.option.use" alt="사용" /></option>
				<option value="N"><u:msg titleId="cm.option.notUse" alt="사용안함" /></option>
				</select></td>
			<td class="width12"></td>
			<td class="bodyip_lt"><u:msg titleId="ap.cmpt.title" alt="타이틀" /></td>
			<td class="width10"></td>
			<td><select id="${apvLnTitlTypCd1}-titlUseYn" name="${apvLnTitlTypCd1}-titlUseYn"<u:elemTitle titleId="ap.cmpt.title" alt="타이틀" />>
				<option value="Y"><u:msg titleId="cm.option.use" alt="사용" /></option>
				<option value="N"><u:msg titleId="cm.option.notUse" alt="사용안함" /></option>
				</select></td>
			<td class="width13"></td>
			</tr>
			</tbody></table></td>
		</tr>
	</u:listArea>
</u:tabArea>

<u:tabArea id="apvLnTab2" style="display:none" outerStyle="" innerStyle="margin: 0px 10px 0px 10px; padding-top:10px;" noBottomBlank="true">
	<div style="float:left; width: 4%; text-align: center;"><u:radio name="${apvLnTitlTypCd2}-apvLnDispTypCd" value="3row" checked="true" /></div>
	<div style="float:right; width:95%;">
		<label for="${apvLnTitlTypCd2}-apvLnDispTypCd3row">
		<table class="approvaltable" border="0" cellpadding="0" cellspacing="1"><tbody>
		<tr>
			<td rowspan="3" class="approval_head"><u:msg titleId="ap.signArea.${apvLnTitlTypCd2}" charSeperator="<br/>" alt="결<br/>재" /></td>
			<td class="approval_body"><span style="${param.formApvLnTypCd=='apvLnWrtn' ? 'display:none;' : ''}"><u:msg titleId="ap.cmpt.signArea.sampleTitle" alt="과장" /></span></td>
		</tr>
		<tr>
			<td class="approval_img"><img src="${_ctx}/images/etc/etc_s.png" style="${param.formApvLnTypCd=='apvLnWrtn' ? 'display:none;' : ''}"></td>
		</tr>
		<tr>
			<td class="approval_body"><span style="${param.formApvLnTypCd=='apvLnWrtn' ? 'display:none;' : ''}"><u:msg titleId="ap.cmpt.signArea.sampleDate" alt="12/25" /></span></td>
		</tr>
		</tbody></table>
		</label>
	</div>
	<u:blank />
	
	<u:listArea>
		<tr>
		<td width="100" class="head_ct"><u:msg titleId="cm.option.config" alt="설정" /></td>
		<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			<td class="width12"></td>
			<td class="bodyip_lt"><u:msg titleId="ap.cmpt.maxCnt" alt="최대갯수" /></td>
			<td class="width10"></td>
			<td><select id="${apvLnTitlTypCd2}-maxCnt" name="${apvLnTitlTypCd2}-maxCnt" onchange="checkMaxCell(this);"<u:elemTitle titleId="ap.cmpt.maxCnt" alt="최대갯수" />>
				<c:forEach begin="1" end="${signAreaMaxCnt==8 ? 7 : signAreaMaxCnt-2}" var="no"><option value="${no}">${no}</option></c:forEach>
				</select></td>
			<td class="width12"></td>
			<td class="bodyip_lt"><u:msg titleId="ap.cmpt.align" alt="기준위치" /></td>
			<td class="width10"></td>
			<td><select id="${apvLnTitlTypCd2}-alnVa" name="${apvLnTitlTypCd2}-alnVa" onchange="checkAlign(this);"<u:elemTitle titleId="ap.cmpt.align" alt="기준위치" />>
				<option value="right"><u:msg titleId="cm.aln.right" alt="우측 정렬" /></option>
				<option value="left"><u:msg titleId="cm.aln.left" alt="좌측 정렬" /></option>
				</select></td>
			<td class="width13"></td>
			</tr>
			<tr>
			<td class="width12"></td>
			<td class="bodyip_lt"><u:msg titleId="ap.cmpt.border" alt="테두리선" /></td>
			<td class="width10"></td>
			<td><select id="${apvLnTitlTypCd2}-bordUseYn" name="${apvLnTitlTypCd2}-bordUseYn"<u:elemTitle titleId="ap.cmpt.border" alt="테두리선" />>
				<option value="Y"><u:msg titleId="cm.option.use" alt="사용" /></option>
				<option value="N"><u:msg titleId="cm.option.notUse" alt="사용안함" /></option>
				</select></td>
			<td class="width12"></td>
			<td class="bodyip_lt"><u:msg titleId="ap.cmpt.title" alt="타이틀" /></td>
			<td class="width10"></td>
			<td><select id="${apvLnTitlTypCd2}-titlUseYn" name="${apvLnTitlTypCd2}-titlUseYn"<u:elemTitle titleId="ap.cmpt.title" alt="타이틀" />>
				<option value="Y"><u:msg titleId="cm.option.use" alt="사용" /></option>
				<option value="N"><u:msg titleId="cm.option.notUse" alt="사용안함" /></option>
				</select></td>
			<td class="width13"></td>
			</tr>
			</tbody></table></td>
		</tr>
	</u:listArea>
</u:tabArea>

<u:blank />

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" href="javascript:setApvLn();" alt="확인" auth="W" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>
