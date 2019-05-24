<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%// 확인(신규) or 양식 편집 - 양식 편집 화면으로 이동 %>
function getStyleData(formTxtTypCd){
	var param = new ParamMap().getData("setStylePopArea");
	var styles=[], txtStylVa = param.get("txtStylVa");
	if(txtStylVa!=null){
		if(txtStylVa.indexOf('bold')>=0) styles.push("font-weight:bold;");
		if(txtStylVa.indexOf('italic')>=0) styles.push("font-style:italic;");
		if(txtStylVa.indexOf('underline')>=0) styles.push("text-decoration:underline;");
		if(styles.length>0) param.put("txtStylVa", styles.join(" "));
	} else {
		param.put("txtStylVa", "");
	}
	return param;
}
<%// 부모창의 데이터를 param 으로 받아 현재 스타일 창의 화면 UI에 세팅함 %>
function setStyleData(param){
	var va, $popArea = $("#setStylePopArea");
	['txtFontVa','txtSize', 'txtColrVa', 'txtStylVa'].each(function(index, key){
		va = param.get(key);
		if(va!=null && va!=''){
			if(key=='txtFontVa' || key=='txtSize'){
				$popArea.find("#"+key+" option[value='"+va+"']").attr("selected","selected");
			} else if(key=='txtColrVa'){
				$popArea.find("#"+key).val(va);
			} else if(key=='txtStylVa'){
				va = va.toLowerCase();
				if(va.indexOf('bold')>=0){
					$popArea.find("input[value='bold']").attr("checked","checked");
				}
				if(va.indexOf('italic')>=0){
					$popArea.find("input[value='italic']").attr("checked","checked");
				}
				if(va.indexOf('underline')>=0){
					$popArea.find("input[value='underline']").attr("checked","checked");
				}
			}
		}
	});
	//$popArea.uniform.update();
}

$(document).ready(function() {
	var param = new ParamMap().getData("${param.formTxtTypCd}PopArea", "${param.formTxtTypCd}-");
	setStyleData(param);
});
//-->
</script>
<div style="width:700px">
<form id="setStylePopArea">

<% // 폼 필드 %>
<u:listArea>
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="ap.cmpt.fontFamily" alt="글꼴" /></td>
	<td width="32%"><select id="txtFontVa" name="txtFontVa"<u:elemTitle titleId="ap.cmpt.fontFamily" alt="글꼴" />><c:forEach
		items="${fontFamilies}" var="font" varStatus="status"><u:set
			test="${fn:indexOf(font,',')<0}" var="fontNm" value="${font}" elseValue="${fn:substring(font,0, fn:indexOf(font,','))}"
			/><u:set
			test="${fn:substring(fontNm,0,1)>='a' and fn:substring(fontNm,0,1)<='z' }"
			var="fontNm"
			value="${fn:toUpperCase(fn:substring(fontNm,0,1))}${fn:substring(fontNm,1,fn:length(fontNm))}"
			elseValue="${fontNm}"
			/>
		<u:option value="${font}" title="${fontNm}" /></c:forEach>
		</select></td>
	<td width="18%" class="head_lt"><u:msg titleId="cols.size" alt="크기" /></td>
	<td width="32%"><select id="txtSize" name="txtSize"<u:elemTitle titleId="cols.size" alt="크기" />><c:forEach
		items="${fontSizes}" var="size" varStatus="status">
		<u:option value="${size}" /></c:forEach>
		</select></td>
	</tr>
	
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="ap.cmpt.fontStyle" alt="폰트 스타일" /></td>
	<td width="32%"><u:checkArea>
		<u:checkbox name="txtStylVa" value="bold" titleId="ap.cols.bold" alt="굵게" checked="false" inputClass="bodybg_lt" />
		<u:checkbox name="txtStylVa" value="italic" titleId="ap.cols.italic" alt="기울임" checked="false" inputClass="bodybg_lt" />
		<u:checkbox name="txtStylVa" value="underline" titleId="ap.cols.underline" alt="밑줄" checked="false" inputClass="bodybg_lt" />
		</u:checkArea></td>
	<td width="18%" class="head_lt"><u:msg titleId="ap.cmpt.color" alt="색깔" /></td>
	<td width="32%"><u:color id="txtColrVa" /></td>
	</tr>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" onclick="applyStylePop('${param.formTxtTypCd}'); dialog.close(this);" alt="확인" auth="A" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</form>
</div>