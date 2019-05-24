<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// openStylePop() - setFormEdit.jsp

// 스타일 소버튼 후 저장 %>
function applyStylePop(formTxtTypCd){
	var param = getStyleData();<%// getStyleData() - setDocStylePop.jsp %>
	param.setData(formTxtTypCd+"PopArea", formTxtTypCd+"-");
}<%
// 머리글 (텍스트)/머리글 (이미지) - 체크박스 클릭 %>
function clickHeaderCheck(obj){
	if(obj==null) return;
	var id = $(obj).attr('id');
	if(id=='docHeader-useYn'){<%// 머리글 (텍스트) %>
		if(obj.checked){
			var $img = $("#setHeaderPopForm #docHeaderImg-useYn");
			if($img.length == 1){
				$img[0].checked = false;
				$img.uniform.update();
			}
		} else {
			$('#setHeaderPopForm #docHeader-txtCont').val('');
		}
	} else if(id=='docHeaderImg-useYn'){<%// 머리글 (이미지) %>
		if(obj.checked){
			var $img = $("#setHeaderPopForm #docHeader-useYn");
			if($img.length == 1){
				$img[0].checked = false;
				$img.uniform.update();
			}
		}
	}
}<%
// 파일테그 이미지 선택 - 확장자 체크후 사용여부 체크함 %>
function setImageUseYn(id, va){
	var useYnId = id.substring(0,id.indexOf('-'))+"-useYn";
	var useYn = $("#"+useYnId)[0];
	if(va!=null && !useYn.checked){
		$(useYn).trigger('click');
	}
}<%
// 양식명 사용여부 체크박스 클릭 - 필수입력 alert %>
function checkDocNameCheckbox(){
	var useYn = $("#setHeaderPopForm #docName-useYn")[0];
	if(!useYn.checked){
		if('${optConfigMap.notMandFormNm}' != 'Y'){
			alertMsg('cm.noti.mandatory', ['#cols.formNm']);
			useYn.checked = false;
			$("#setHeaderPopForm #docName-useYn").trigger("click");
		} else {
			$("#setHeaderPopForm #docName-txtCont").val('');
		}
	}/* 
	if(!useYn.checked && '${optConfigMap.notMandFormNm}' != 'Y'){
		alertMsg('cm.noti.mandatory', ['#cols.formNm']);
		useYn.checked = false;
		$("#setHeaderPopForm #docName-useYn").trigger("click");		
	} */
}<%
// 텍스트 입력값이 없으면 사용여부 언체크 함 %>
function onTxtContBlur(obj){
	obj.value = obj.value.trim();
	if(obj.value!=''){
		var id = $(obj).attr("id");
		var $useYn = $("#setHeaderPopForm #"+(id.substring(0, id.indexOf('-')))+"-useYn");
		if(!$useYn[0].checked) $useYn.trigger('click');
	}
}<%
// 확인 버튼 클릭 %>
function setHeader(){
	if('${optConfigMap.notMandFormNm}' != 'Y' && !validator.validate("docNamePopArea")){
		return;
	}
	
	var $form = $("#setHeaderPopForm");
	if($form.find("#docLogo-imageFile").val()!='' || $form.find("#docSymbol-imageFile").val()!='' || $form.find("#docHeaderImg-imageFile").val()!=''){
		$form.attr('action','./transDocHeaderImage.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form.submit();<%// callback : processHeaderData %>
	} else {
		processHeaderData();
	}
}<%
// [callback] - 로고/심볼 이미지 업로드 후  %>
function processHeaderData(object){
	var $form = $("#setHeaderPopForm");
	var txtValid = false, txt, imgValid = false, img;
	["docHeader","docName"].each(function(index, name){
		if($form.find("#"+name+"-useYn")[0].checked){
			txt = $form.find("#"+name+"-txtCont").val();
			if(txt != null) txt = txt.trim();
			if(txt != null && txt != '') txtValid = true;
		}
	});
	$dataArea = $("#headerArea #headerDataArea");
	["docHeaderImg","docLogo","docSymbol"].each(function(index, name){
		if($form.find("#"+name+"-useYn")[0].checked){
			img = $form.find("#"+name+"-imageFile").val();
			if(img != null) img = img.trim();
			if(img != null && img != ''){
				imgValid = true;
			} else {
				img = $dataArea.find("input[name='"+name+"-imgPath']").val();
				if(img != null && img != ''){
					imgValid = true;
				}
			}
		}
	});
	if(!txtValid && !imgValid){
		alertMsg("ap.cfg.noValidVa");<%//ap.cfg.noValidVa=설정된 값이 없습니다.%>
		return;
	}
	
	var param = new ParamMap(object);
	["docHeaderImg","docLogo","docSymbol"].each(function(index, name){
		param.put(name+"-useYn", $form.find("#"+name+"-useYn")[0].checked ? "Y" : "N");
	});
	param.getData("docHeaderPopArea").getData("docNamePopArea");
	setHeaderData(param);<%// - setFormEdit.jsp %>
	dialog.close("setDocHeaderDialog");
}
$(document).ready(function() {
	var param = new ParamMap().getData("headerDataArea");
	if(!param.isEmpty()){
		["docHeader","docName"].each(function(index, name){
			if(param.get(name+"-useYn")!='Y') param.put(name+"-txtCont", "");
		});
	}
	param.setData("setHeaderPopForm");
});
//-->
</script>
<div style="width:650px">
<form id="setHeaderPopForm" method="post" enctype="multipart/form-data">
<u:listArea>

	<tr>
	<td width="20%" class="head_lt"><u:msg titleId="ap.cmpt.headerTxt" alt="머리글 (텍스트)" /></td>
	<td class="body_lt" id="docHeaderPopArea"><u:checkArea>
		<u:checkbox value="Y" id="docHeader-useYn" name="docHeader-useYn" titleId="cols.useYn" onclick="clickHeaderCheck(this);" alt="사용여부" checked="${not empty docHeader.txtCont}" />
		<td><u:input id="docHeader-txtCont" titleId="ap.cmpt.header" value="${docHeader.txtCont}" style="width:200px" maxByte="200" onblur="onTxtContBlur(this);" />
			<u:input type="hidden" id="docHeader-txtFontVa" value="${docHeader.txtFontVa}" />
			<u:input type="hidden" id="docHeader-txtStylVa" value="${docHeader.txtStylVa}" />
			<u:input type="hidden" id="docHeader-txtSize" value="${docHeader.txtSize}" />
			<u:input type="hidden" id="docHeader-txtColrVa" value="${docHeader.txtColrVa}" /></td>
		<td><u:buttonS titleId="ap.cmpt.style" alt="스타일" onclick="openStylePop('docHeader', 'ap.cmpt.header');" /></td>
	</u:checkArea></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="ap.cmpt.headerImg" alt="머리글 (이미지)" /></td>
	<td class="body_lt"><u:checkArea>
		<u:checkbox value="Y" id="docHeaderImg-useYn" name="docHeaderImg-useYn" titleId="cols.useYn" onclick="clickHeaderCheck(this);" alt="사용여부" checked="${not empty docHeaderImg.imgPath}" />
		<td><u:file id="docHeaderImg-image" titleId="ap.cmpt.headerImg" alt="머리글 (이미지)" exts="gif,jpg,jpeg,png,tif" onchange="setImageUseYn" />
			<u:input type="hidden" id="docHeaderImg-imgPath" value="${docHeaderImg.imgPath}" />
			<u:input type="hidden" id="docHeaderImg-imgWdth" value="${docHeaderImg.imgWdth}" />
			<u:input type="hidden" id="docHeaderImg-imgHght" value="${docHeaderImg.imgHght}" /></td>
	</u:checkArea></td>
	</tr>
	
	<tr>
	<td class="head_lt"><c:if test="${optConfigMap.notMandFormNm ne 'Y'}"><u:mandatory/></c:if><u:msg titleId="cols.formNm" alt="양식명" /></td>
	<td class="body_lt" id="docNamePopArea"><u:checkArea>
		<u:checkbox value="Y" id="docName-useYn" name="docName-useYn" titleId="cols.useYn" onclick="window.setTimeout('checkDocNameCheckbox()', 20);" alt="사용여부" checked="true" />
		<td><u:input id="docName-txtCont" titleId="cols.formNm" value="${docName.txtCont}" style="width:200px" maxByte="200" onblur="onTxtContBlur(this);" mandatory="Y" />
			<u:input type="hidden" id="docName-txtFontVa" value="${docName.txtFontVa}" />
			<u:input type="hidden" id="docName-txtStylVa" value="${docName.txtStylVa}" />
			<u:input type="hidden" id="docName-txtSize" value="${docName.txtSize}" />
			<u:input type="hidden" id="docName-txtColrVa" value="${docName.txtColrVa}" />
		</td>
		<td><u:buttonS titleId="ap.cmpt.style" alt="스타일" onclick="openStylePop('docName', 'cols.formNm');" /></td>
	</u:checkArea></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="ap.cmpt.logo" alt="로고 (좌)" /></td>
	<td class="body_lt"><u:checkArea>
		<u:checkbox value="Y" id="docLogo-useYn" name="docLogo-useYn" titleId="cols.useYn" alt="사용여부" checked="${not empty docLogo.imgPath}" />
		<td><u:file id="docLogo-image" titleId="ap.cmpt.logo" alt="로고" exts="gif,jpg,jpeg,png,tif" onchange="setImageUseYn" />
			<u:input type="hidden" id="docLogo-imgPath" value="${docLogo.imgPath}" />
			<u:input type="hidden" id="docLogo-imgWdth" value="${docLogo.imgWdth}" />
			<u:input type="hidden" id="docLogo-imgHght" value="${docLogo.imgHght}" /></td>
	</u:checkArea></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="ap.cmpt.symbol" alt="심볼 (우)" /></td>
	<td class="body_lt"><u:checkArea>
		<u:checkbox value="Y" id="docSymbol-useYn" name="docSymbol-useYn" titleId="cols.useYn" alt="사용여부" checked="${not empty docSymbol.imgPath}" />
		<td><u:file id="docSymbol-image" titleId="ap.cmpt.symbol" alt="심볼" exts="gif,jpg,jpeg,png,tif" onchange="setImageUseYn" />
			<u:input type="hidden" id="docSymbol-imgPath" value="${docSymbol.imgPath}" />
			<u:input type="hidden" id="docSymbol-imgWdth" value="${docSymbol.imgWdth}" />
			<u:input type="hidden" id="docSymbol-imgHght" value="${docSymbol.imgHght}" /></td>
	</u:checkArea></td>
	</tr>
	
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" href="javascript:setHeader();" alt="확인" auth="A" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</form>
</div>