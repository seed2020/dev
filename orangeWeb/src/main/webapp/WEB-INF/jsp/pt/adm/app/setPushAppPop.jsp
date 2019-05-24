<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// 저장 %>
function saveAppleApp(){
	if(validator.validate('setAppleAppForm')){
		var $form = $("#setAppleAppForm");
		$form.attr("action","./transPushApp.do?menuId=${menuId}");
		$form.attr("target","dataframe");
		$form.submit();
	}
}<%
// 이력 변경 %>
function changeSeq(seq){
	mngApp('${ptPushAppDVo.custCd}', seq);
}<%
// 이력 삭제 %>
function deleteAppleAppHis(){
	if(confirmMsg("cm.cfrm.del")) {<%//cm.cfrm.del=삭제하시겠습니까 ?%>
		callAjax('./transPushAppAjx.do?menuId=${menuId}', {cmd:'deleteHis', custCd:'${ptPushAppDVo.custCd}', seq:'${ptPushAppDVo.seq}'}, function(data){
			if(data.message!=null){
				alert(data.message);
			}
			if(data.result=='ok'){
				dialog.close('setAppleAppDialog');
			}
		});
	}
}
//-->
</script>


<div style="width:550px">

<c:if test="${not empty ptPushAppDVoList and fn:length(ptPushAppDVoList) > 1}">
<u:listArea style="width:550px">
	<tr>
	<td width="25%" class="head_lt"><u:msg titleId="cols.his" alt="이력" /></td>
	<td><select id="his" name="his" onchange="changeSeq($(this).val())" <u:elemTitle titleId="cols.his" />><c:forEach
			items="${ptPushAppDVoList}" var="ptPushAppDVo">
		<u:option value="${ptPushAppDVo.seq}" title="${ptPushAppDVo.regDt}" selected="${seq eq ptPushAppDVo.seq}" />
		</c:forEach></select></td>
	</tr>
</u:listArea>
</c:if>

<form id="setAppleAppForm" method="post" enctype="multipart/form-data">
<u:listArea style="width:550px">

	<tr>
	<td width="25%" class="head_lt"><u:mandatory /><u:msg titleId="cols.custCd" alt="고객코드" /></td>
	<td><u:input id="custCd" value="${ptPushAppDVo.custCd}" titleId="cols.custCd" maxByte="30" style="width:95%" mandatory="Y" readonly="${not empty ptPushAppDVo ? 'Y' : ''}" /></td>
	</tr>
	
	<tr>
	<td width="25%" class="head_lt"><u:mandatory /><u:msg titleId="cols.custNm" alt="고객명" /></td>
	<td><u:input id="custNm" value="${ptPushAppDVo.custNm}" titleId="cols.custNm" maxByte="200" style="width:95%" mandatory="Y" /></td>
	</tr>
	
	<tr>
	<td width="25%" class="head_lt"><u:mandatory /><u:msg titleId="cols.dispNm" alt="표시명" /></td>
	<td><u:input id="dispNm" value="${ptPushAppDVo.dispNm}" titleId="cols.dispNm" maxByte="200" style="width:95%" mandatory="Y" /></td>
	</tr>
	
	<tr>
	<td width="25%" class="head_lt"><u:mandatory /><u:msg titleId="cols.strtYmd" alt="시작일" /></td>
	<td><u:calendar id="strtYmd" option="{end:'endYmd'}" value="${ptPushAppDVo.strtYmd}" mandatory="Y" /></td>
	</tr>
	
	<tr>
	<td width="25%" class="head_lt"><u:mandatory /><u:msg titleId="cols.endYmd" alt="종료일" /></td>
	<td><u:calendar id="endYmd" option="{start:'strtYmd'}" value="${ptPushAppDVo.endYmd}" mandatory="Y" /></td>
	</tr>
	
	<tr>
	<td width="25%" class="head_lt"><u:msg titleId="cols.ipaFile" alt="IPA 파일" /></td>
	<td><u:file id="ipaFile" titleId="cols.ipaFile" alt="IPA 파일" exts="ipa" /></td>
	</tr>
	
	<tr>
	<td width="25%" class="head_lt"><u:msg titleId="cols.apkFile" alt="APK 파일" /></td>
	<td><u:file id="apkFile" titleId="cols.apkFile" alt="APK 파일" exts="apk" /></td>
	</tr>

	<tr>
	<td width="25%" class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
	<td><select id="useYn" name="useYn" <u:elemTitle titleId="cols.useYn" />>
		<u:option value="Y" titleId="cm.option.use" checkValue="${ptPushAppDVo.useYn}" />
		<u:option value="N" titleId="cm.option.notUse" checkValue="${ptPushAppDVo.useYn}" />
		</select></td>
	</tr>
	
	<tr>
	<td width="25%" class="head_lt"><u:msg titleId="cols.desc" alt="설명" /></td>
	<td><u:textarea id="descCont" value="${ptPushAppDVo.descCont}" titleId="cols.desc" maxByte="1000" rows="6" style="width:95%" /></td>
	</tr>
	
	<c:if test="${not empty ptPushAppDVo}">
	<tr>
	<td width="25%" class="head_lt"><u:msg titleId="cols.modDt" alt="수정일시" /></td>
	<td class="body_lt"><u:out value="${ptPushAppDVo.regDt}" type="longdate" /></td>
	</tr>
	</c:if>	
	
	<c:if test="${not empty appleLink}">
	<tr>
	<td width="25%" class="head_lt">IOS URL</td>
	<td class="body_lt"><u:out value="${appleLink}" /></td>
	</tr>
	</c:if>	
	
	<c:if test="${not empty androidLink}">
	<tr>
	<td width="25%" class="head_lt">Android URL</td>
	<td class="body_lt"><u:out value="${androidLink}" /></td>
	</tr>
	</c:if>
	
</u:listArea>
</form>

<u:buttonArea><c:if
		test="${empty param.seq or param.seq eq '0'}">
	<u:button titleId="cm.btn.save" href="javascript:saveAppleApp();" alt="저장" auth="A" /></c:if><c:if
		test="${not(empty param.seq or param.seq eq '0')}">
	<u:button titleId="cm.btn.del" href="javascript:deleteAppleAppHis();" alt="삭제" auth="A" /></c:if>
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>


</div>