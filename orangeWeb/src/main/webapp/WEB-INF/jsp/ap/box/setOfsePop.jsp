<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.net.URLEncoder"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--<%
// 조직도 트리 선택 - 관인(서명인), 발신명의 조회 %>
function clickApTree(orgId, rescNm){
	callAjax("./listOfcSealAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {orgId:orgId,ofseTypCd:"${param.ofseTypCd}",docLangTypCd:'${param.docLangTypCd}'}, function(data){
		if(data.message != null) alert(data.message);
		var arr = data.orOfseDVoList;
		var $area = $("#ofseByOrgIdArea");<%
		// 발신명의 세팅%>
		var $sendrNm = $area.find("#sendrNmRescNmTd");
		$sendrNm.attr('data-sendrNmRescId', data.sendrNmRescId);
		$sendrNm.text(data.sendrNmRescNm);<%
		// select-option 에 조회된 이미지 데이터 넣기%>
		var $sel = $area.find("#ofseLst"), selected, vo, selVo=null;
		$sel.find("option").remove();
		if(arr!=null){
			for(var i=0;i<arr.length;i++){
				vo = arr[i];
				if(i==0 || vo.get('dftOfseYn')=='Y') selVo = vo;
				selected = vo.get('dftOfseYn')=='Y' ? ' selected="selected"' : '';
				$sel.append('<option value="'+vo.get('seq')+'" data-imgPath="'+vo.get('imgPath')+'"'+selected+'>'+vo.get('rescNm')+'</option>');
			}
		}
		var $imgArea = $area.find("#ofseImgArea");
		if(selVo!=null){
			$imgArea.html('<img height="80px" alt="'+escapeValue(selVo.get('rescNm'))+'" src="'+selVo.get('imgPath')+'" />');
			$imgArea.attr('data-noImage', '');
		} else {
			$imgArea.html('');
			$imgArea.attr('data-noImage', '');
		}
		$sel.uniform.update();
	});
}<%
// 선택 - select 변경 %>
function changeOfse(){
	var $area = $("#ofseByOrgIdArea");
	var $opt = $area.find("#ofseLst option:selected");
	var $imgArea = $area.find("#ofseImgArea");
	if($opt.length==0){
		$imgArea.html('');
		$imgArea.attr('data-noImage', '');
	} else {
		$imgArea.html('<img height="80px" alt="'+escapeValue($opt.text())+'" src="'+$opt.attr('data-imgPath')+'" />');
		$imgArea.attr('data-noImage', '');
	}
}<%
// 서명인 생략 버튼 - [서명인 생략] 이미지로 대체 %>
function setNoSignImage(){
	var $imgArea = $("#ofseByOrgIdArea #ofseImgArea");
	$imgArea.html('<img height="26px" src="${_cxPth}/images/etc/no_sign.png" />');
}<%
// 날인 생략 버튼 %>
function setNoImage(){
	var $imgArea = $("#ofseByOrgIdArea #ofseImgArea");
	$imgArea.html('');
	$imgArea.attr('data-noImage', 'Y');
}<%
// 확인 버튼 클릭 %>
function setDocOfse(){
	// sendrNmRescNm
	var $area = $("#ofseByOrgIdArea");<%
	// 발신명의 확인 %>
	var $sendrNm = $area.find("#sendrNmRescNmTd");
	var sendrNmRescNm = $sendrNm.text();
	var sendrNmRescId = $sendrNm.attr('data-sendrNmRescId');
	if(sendrNmRescNm==null || sendrNmRescNm==''){<%
		// ap.msg.notWithoutSendrNm=발신명의가 지정되지 않아서 진행 할 수 없습니다.%>
		alertMsg('ap.msg.notWithoutSendrNm');
		return;
	}
	<%
	// 이미지 데이터 화면에서 가져오기 %>
	var $magArea = $area.find("#ofseImgArea");
	var $img = $magArea.find("img");
	if($img.length==1){
		var src = $img.attr("src");
		var height = $img.attr("height");
		if(height.endsWith('px')) height = height.substring(0, height.length-2);
		var $area = $("#senderArea #stampArea");
		$area.attr('data-noImage', '');
		
		var $stamp = $area.find("img");
		if($stamp.length==0){
			$area.append('<img src="'+src+'" height="'+height+'px" />');
			$stamp = $area.find("img");
		} else {
			$stamp.attr('src', src);
			$stamp.attr('height', height+'px');
			$stamp.show();
		}
		if(height!='80'){
			var marginTop = (20 - parseInt(parseInt(height, 10)/2, 10));
			$area.css('margin-top', marginTop+'px');
		} else {
			$area.css('margin-top', '-20px');
		}
		
		$("#docDiv #senderArea #docSenderViewArea").text(sendrNmRescNm);
		
		$area = $("#docDataArea #senderStampArea");
		$area.html('');
		$area.append('<input type="hidden" name="ofsePath" value="'+src+'" />');
		$area.append('<input type="hidden" name="ofseHghtPx" value="'+height+'" />');
		$area.append('<input type="hidden" name="sendrNmRescId" value="'+sendrNmRescId+'" />');
		$area.append('<input type="hidden" name="sendrNmRescNm" value="'+escapeValue(sendrNmRescNm)+'" />');
		dialog.close("setOfseDialog");
		
	} else if($magArea.attr('data-noImage')=='Y'){<%// 날인생략 - 오프라인에서 찍기 위한것 %>
		var $stampArea = $("#senderArea #stampArea");
		$stampArea.attr('data-noImage', 'Y');
		$stampArea.find("img").remove();
		$("#docDiv #senderArea #docSenderViewArea").text(sendrNmRescNm);
		
		$area = $("#docDataArea #senderStampArea");
		$area.html('');
		$area.append('<input type="hidden" name="sendrNmRescId" value="'+sendrNmRescId+'" />');
		$area.append('<input type="hidden" name="sendrNmRescNm" value="'+escapeValue(sendrNmRescNm)+'" />');
		dialog.close("setOfseDialog");
	} else {<%
		//ap.msg.noSeal=선택된 관인이 없습니다.
		//ap.msg.noSign=선택된 서명인이 없습니다.
		%>
		alertMsg("${param.ofseTypCd=='01' ? 'ap.msg.noSeal' : 'ap.msg.noSign'}");
	}
}
//-->
</script>
<div style="width:550px">

<div style="width:100%">
<div style="float:left; width:44.1%; margin:0px; padding:0px;">
<u:titleArea
	outerStyle="height:281px; overflow-x:hidden; overflow-y:hidden;"
	innerStyle="NO_INNER_IDV">
<iframe id="apvLnOrgTreeFrm" name="apvLnOrgTreeFrm" src="./treeUpFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}${upParam}" style="width:100%; height:280px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:titleArea>
</div>
<div style="float:right; width:54.1%;">
<u:titleArea id="ofseByOrgIdArea"
	outerStyle="height:281px; overflow-x:hidden; overflow-y:hidden;"
	innerStyle="NO_INNER_IDV">
<div style="height:330px; padding:8px;">

<u:listArea>
	<tr>
	<td width="30%" class="head_lt"><u:msg titleId="ap.cmpt.sender" alt="발신명의" /></td>
	<td width="80%" class="body_lt" id="sendrNmRescNmTd"></td>
	</tr>
	<tr>
	<td width="30%" class="head_lt"><u:msg titleId="${param.ofseTypCd=='02' ? 'ap.btn.sign' : 'ap.btn.ofcSeal'}" alt="관인 or 서명인" /></td>
	<td width="80%"><select id="ofseLst" onchange="changeOfse();"></select></td>
	</tr>
</u:listArea>

<u:listArea>
	<tr>
	<td height="160px" id="ofseImgArea" style="text-align:center;"></td>
	</tr>
</u:listArea>

<div>
<table border="0" cellpadding="0" cellspacing="1" style="float:right"><tbody><tr>
	<td><u:buttonS alt="날인 생략" titleId="ap.btn.noStampImg" onclick="setNoImage()" /></td><c:if
		test="${param.ofseTypCd=='02' and optConfigMap.alwIntroNoSign == 'Y' }">
	<td><u:buttonS alt="서명인생략" titleId="ap.btn.noSign" onclick="setNoSignImage()" /></td></c:if>
</tr></tbody></table>
</div>

</div>
</u:titleArea>
</div>
</div>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" onclick="setDocOfse();;" alt="확인" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>