<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="params" />
<script type="text/javascript">
<!--
<% // [팝업:선택] 선택된 값 조회 %>
function selChecked() {
	var $checked = $('#findBcPwsmFrm').contents().find('input[name="bcNmCheck"]:checked');
	if ($checked.length == 0 || $checked.val() == '') {
		alertMsg('cm.msg.noSelect'); <% // cm.msg.noSelect=선택한 항목이 없습니다. %>
		return null;
	}
	return $checked.val();
};

<% // 동명이인 정보 선택 %>
function setChecked() {
	var value = selChecked();
	if( value != null) {
		fnPwsmSelect(value);
		dialog.close('findBcPwsmPop');	
	}
};

<% // 동명이인 원본정보 수정 %>
function setOriginalUpdate() {
	var value = selChecked();
	if( value != null) {
		fnPwsmUpdate(value);
		dialog.close('findBcPwsmPop');	
	}
};
//-->
</script>
<div style="width:500px">
<iframe id="findBcPwsmFrm" name="findBcPwsmFrm" src="./findBcPwsmFrm.do?schWord=${param.schWord }&schCat=${param.schCat }&schBcRegrUid=${param.schBcRegrUid }&menuId=${menuId }" style="width:100%; height:360px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
<% // 하단 버튼 %>
<u:buttonArea>
<u:button titleId="wb.btn.set.original" alt="원본수정" onclick="setOriginalUpdate();" auth="W" />
<u:button titleId="cm.btn.confirm" alt="확인" onclick="setChecked();" auth="W" />
<u:button href="javascript:void(0)" onclick="dialog.close(this);" alt="닫기" titleId="cm.btn.close" />
</u:buttonArea>
</div>
