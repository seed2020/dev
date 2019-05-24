<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%// 확인 버튼 클릭 %>
function setApvLnOneTop(){
	var param = new ParamMap();
	var $form = $("#setApvLnOneTopForm");
	["rowCnt","colCnt"].each(function(index,va){
		param.put(va, $form.find('#'+va).val());
	});
	var nm, itemId, colspan = 1;
	$form.find("select option:selected").each(function(){
		nm = $(this).parent().attr('name');
		itemId = $(this).val();
		if(itemId != ''){
			param.put(nm,itemId+"-"+colspan);
			param.put(nm+"-nm",$(this).text());
		}
	});
	if($form.find("input[name='apv-lstDupDispYn']")[0].checked){
		param.put("apv-lstDupDispYn", "Y");
	}
	
	setApvLnData(param, "${param.formApvLnTypCd}");<%// - setFormEdit.jsp %>
	dialog.close("setDocApvLnDialog");
}

$(document).ready(function() {
	var param = new ParamMap();
	$("#docArea").children("#apvLnArea").each(function(){
		param.getData(this);
	});
	param.each(function(k, v){
		if(k.startsWith("items-99999-")){
			param.remove(k);
			k = k.substring(12);
			if(k.endsWith("-1") && v.length > 2){
				v = v.substring(0, v.length - 2);
			}
			param.put(k, v);
		}
	});
	param.removeEmpty().setData("setApvLnOneTopForm");
});
//charSeperator="<br/>"
//-->
</script>
<div style="width:500px">
<form id="setApvLnOneTopForm">
<input type="hidden" id="rowCnt" name="rowCnt" value="5" />
<input type="hidden" id="colCnt" name="colCnt" value="1" />
<u:listArea colgroup="25%,75%">

	<tr>
	<td class="head_ct"><u:msg titleId="ap.cmpt.items" alt="항목지정" /></td>
	<td class="body_lt">
		<select id="item1" name="1-1"<u:elemTitle titleId="ap.cmpt.items" alt="항목지정" />>
			<option value="">- <u:msg titleId="cm.btn.sel" alt="선택" /> -</option>
			<c:forEach
				items="${items}" var="item" >
			<option value="${item}"><u:msg titleId="ap.doc.${item}" alt="기안자/기안부서/.." /></option></c:forEach>
		</select><br/>
		<select id="item2" name="2-1"<u:elemTitle titleId="ap.cmpt.items" alt="항목지정" />>
			<option value="">- <u:msg titleId="cm.btn.sel" alt="선택" /> -</option>
			<c:forEach
				items="${items}" var="item" >
			<option value="${item}"><u:msg titleId="ap.doc.${item}" alt="기안자/기안부서/.." /></option></c:forEach>
		</select><br/>
		<select id="item3" name="3-1"<u:elemTitle titleId="ap.cmpt.items" alt="항목지정" />>
			<option value="">- <u:msg titleId="cm.btn.sel" alt="선택" /> -</option>
			<c:forEach
				items="${items}" var="item" >
			<option value="${item}"><u:msg titleId="ap.doc.${item}" alt="기안자/기안부서/.." /></option></c:forEach>
		</select><br/>
		<select id="item4" name="4-1"<u:elemTitle titleId="ap.cmpt.items" alt="항목지정" />>
			<option value="">- <u:msg titleId="cm.btn.sel" alt="선택" /> -</option>
			<c:forEach
				items="${items}" var="item" >
			<option value="${item}"><u:msg titleId="ap.doc.${item}" alt="기안자/기안부서/.." /></option></c:forEach>
		</select><br/>
		<select id="item5" name="5-1"<u:elemTitle titleId="ap.cmpt.items" alt="항목지정" />>
			<option value="">- <u:msg titleId="cm.btn.sel" alt="선택" /> -</option>
			<c:forEach
				items="${items}" var="item" >
			<option value="${item}"><u:msg titleId="ap.doc.${item}" alt="기안자/기안부서/.." /></option></c:forEach>
		</select>
	</td>
	</tr>
	<tr>
	<td class="head_ct"><u:msg titleId="cm.option.config" alt="설정" /></td>
	<td class="body_lt">
		<table border="0" cellpadding="0" cellspacing="0" style="margin-left:1px"><tbody>
		<tr>
		<u:checkbox value="Y" name="apv-lstDupDispYn" titleId="ap.jsp.setFormEdit.showApvrInListYn" alt="서명란 결재자 목록에 표시 여부" />
		</tr>
		</tbody></table>
	</td>
	</tr>
	
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" href="javascript:setApvLnOneTop();" alt="확인" auth="A" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</form>
</div>