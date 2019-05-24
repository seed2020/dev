<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<!-- 링크 css 개별 추가 -->
<style type="text/css">
span.viewSkinImg{cursor:pointer;}
span.viewSkinImg:link    { color:#454545; text-decoration:none; }
span.viewSkinImg:visited { color:#454545; text-decoration:none; }
span.viewSkinImg:hover   { color:#454545; text-decoration:underline; }
span.viewSkinImg:active  { color:#454545; text-decoration:underline; }
</style>
<script type="text/javascript">
<!--
<%// 초기화%>
function doReset(){
	callAjax('./transSkinImgResetAjx.do?menuId=${menuId}', null, function(data){
		if(data.message!=null){
			alert(data.message);
		}
		if(data.result=='ok'){
			reload();
		}
	});
}
<%// 저장%>
function doSave(){
	if(validator.validate('skinImgForm')){
		var $form = $("#skinImgForm");
		$form.attr("action","./transSkinImg.do?menuId=${menuId}");
		$form.attr('target','dataframe');
		$form.submit();
	}
}
<%// 이미지 보기%>
function viewSkinImgPop(setupId){
	dialog.open("viewSkinImgDialog", '<u:msg titleId="cols.cmImg" alt="이미지" />', "./viewSkinImgPop.do?menuId=${menuId}&setupId="+setupId);
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
	// 클릭이벤트 bind
	$('#skinImgForm').find('span.viewSkinImg').on('click', function(){
		$tr = $(this).parents('tr');
		$setupId = $tr.find('input[type="file"]').attr('name');
		viewSkinImgPop($setupId);
	});
});
//-->
</script>

<u:title titleId="pt.jsp.setSkinImg.title" alt="스킨 이미지 설정" menuNameFirst="true" />

<form id="skinImgForm" method="post" enctype="multipart/form-data">
<u:listArea>
	<c:if test="${layout.icoLoutUseYn=='Y'}">
	<tr>
		<td width="18%" class="head_lt" rowspan="4"><u:msg titleId="pt.jsp.setSkin.iconLout" alt="아이콘 레이아웃" /></td>
		<td width="14%" class="body_lt" rowspan="4"><u:msg titleId="pt.jsp.setSkinImg.logoImg" alt="로고 이미지" /></td><u:msg
			titleId="pt.jsp.setSkinImg.logoImg" alt="로고 이미지" var="logoImg" />
		<td class="body_lt"><span style="color:#1c7cff; font-weight:bold;" class="viewSkinImg">Blue</span></td>
		<td class="body_lt"><u:file id="blueIconLogo" title="Blue ${logoImg}" exts="jpg,gif,png,tif" recomend="193 x 63" /></td>
	</tr>
	<tr>
		<td class="body_lt"><span style="color:#1e9828; font-weight:bold;" class="viewSkinImg">Green</span></td>
		<td class="body_lt"><u:file id="greenIconLogo" title="Green ${logoImg}" exts="jpg,gif,png,tif" recomend="193 x 63" /></td>
	</tr>
	<tr>
		<td class="body_lt"><span style="color:#dc485e; font-weight:bold;" class="viewSkinImg">Pink</span></td>
		<td class="body_lt"><u:file id="pinkIconLogo" title="Pink ${logoImg}" exts="jpg,gif,png,tif" recomend="193 x 63" /></td>
	</tr>
	<tr>
		<td class="body_lt"><span style="color:#ffc555; font-weight:bold;" class="viewSkinImg">Yellow</span></td>
		<td class="body_lt"><u:file id="yellowIconLogo" title="Yellow ${logoImg}" exts="jpg,gif,png,tif" recomend="193 x 63" /></td>
	</tr></c:if>
	<c:if test="${layout.bascLoutUseYn=='Y'}">
	<tr>
		<td width="18%" class="head_lt" rowspan="8"><u:msg titleId="pt.jsp.setSkin.bascLout" alt="기본 레이아웃" /></td>
		<td class="body_lt" rowspan="4"><u:msg titleId="pt.jsp.setSkinImg.logoImg" alt="로고 이미지" /></td>
		<td class="body_lt"><span style="color:#1c7cff; font-weight:bold;" class="viewSkinImg">Blue</span></td>
		<td class="body_lt"><u:file id="blueBascLogo" title="Blue ${logoImg}" exts="jpg,gif,png,tif" recomend="193 x 63" /></td>
	</tr>
	<tr>
		<td class="body_lt"><span style="color:#1e9828; font-weight:bold;" class="viewSkinImg">Green</span></td>
		<td class="body_lt"><u:file id="greenBascLogo" title="Green ${logoImg}" exts="jpg,gif,png,tif" recomend="193 x 63" /></td>
	</tr>
	<tr>
		<td class="body_lt"><span style="color:#dc485e; font-weight:bold;" class="viewSkinImg">Pink</span></td>
		<td class="body_lt"><u:file id="pinkBascLogo" title="Pink ${logoImg}" exts="jpg,gif,png,tif" recomend="193 x 63" /></td>
	</tr>
	<tr>
		<td class="body_lt"><span style="color:#ffc555; font-weight:bold;" class="viewSkinImg">Yellow</span></td>
		<td class="body_lt"><u:file id="yellowBascLogo" title="Yellow ${logoImg}" exts="jpg,gif,png,tif" recomend="193 x 63" /></td>
	</tr>
	<tr>
		<td class="body_lt" rowspan="4"><u:msg titleId="pt.jsp.setSkinImg.topbgImg" alt="상단 배경 이미지" /></td><u:msg
			titleId="pt.jsp.setSkinImg.topbgImg" alt="상단 배경 이미지" var="topbgImg" />
		<td class="body_lt"><span style="color:#1c7cff; font-weight:bold;" class="viewSkinImg">Blue</span></td>
		<td class="body_lt"><u:file id="blueBascBg" title="Blue ${topbgImg}" exts="jpg,gif,png,tif" recomend="2560 x 63" /></td>
	</tr>
	<tr>
		<td class="body_lt"><span style="color:#1e9828; font-weight:bold;" class="viewSkinImg">Green</span></td>
		<td class="body_lt"><u:file id="greenBascBg" title="Green ${topbgImg}" exts="jpg,gif,png,tif" recomend="2560 x 63" /></td>
	</tr>
	<tr>
		<td class="body_lt"><span style="color:#dc485e; font-weight:bold;" class="viewSkinImg">Pink</span></td>
		<td class="body_lt"><u:file id="pinkBascBg" title="Pink ${topbgImg}" exts="jpg,gif,png,tif" recomend="2560 x 63" /></td>
	</tr>
	<tr>
		<td class="body_lt"><span style="color:#ffc555; font-weight:bold;" class="viewSkinImg">Yellow</span></td>
		<td class="body_lt"><u:file id="yellowBascBg" title="Yellow ${topbgImg}" exts="jpg,gif,png,tif" recomend="2560 x 63" /></td>
	</tr></c:if>
</u:listArea>
</form>

<u:buttonArea>
	<u:button titleId="cm.btn.reset" alt="초기화" onclick="doReset()" auth="A" />
	<u:button titleId="cm.btn.save" alt="저장" onclick="doSave()" auth="A" />
</u:buttonArea>
