<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function preview(){
	var wdthVa=$('#setPopupForm input[name="wdthVa"]').val();
	if(wdthVa='') wdthVa=600;
	dialog.open('previewDialog', '<u:msg titleId="cm.btn.preview" alt="미리 보기" />', './viewSurvPop.do?menuId=${menuId}&survId=${param.survId}&popId=previewDialog&previewYn=Y&wdthVa='+wdthVa);
}
<% // [하단우측버튼] - 저장 %>
function save(){
	if (!validator.validate('setPopupForm'))
		return;
	var param=new ParamMap().getData('setPopupForm');
	callAjax('./transPopupAjx.do?menuId=${menuId}', param, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			dialog.close('setPopupDialog');
		}
	});
}
//-->
</script>
<div style="width:280px">
<form id="setPopupForm">
<u:input type="hidden" id="survId" value="${param.survId}" />
<u:listArea colgroup="30%,">
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg titleId="cols.wdthPx" alt="넓이픽셀" /></td>
		<td>
			<u:input id="wdthVa" value="${wvSurvPopupDVo.wdthVa}" titleId="cols.wdthPx"
			mandatory="Y" minInt="200" maxInt="800" valueOption="number" style="width:80px" />
		</td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
		<td>
			<select id="useYn" name="useYn" <u:elemTitle titleId="cols.useYn" />>
			<u:option value="Y" titleId="cm.option.use"  checkValue="${wvSurvPopupDVo.useYn}"/>
			<u:option value="N" titleId="cm.option.notUse"  checkValue="${wvSurvPopupDVo.useYn}"/>
			</select>
		</td>
	</tr>
</u:listArea>

<% // 하단 버튼 %>
<u:buttonArea>
<u:button titleId="bb.jsp.popup.preview" href="javascript:preview();" alt="미리보기"  />
<u:button titleId="cm.btn.save" href="javascript:save();" alt="저장"/>
<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>
