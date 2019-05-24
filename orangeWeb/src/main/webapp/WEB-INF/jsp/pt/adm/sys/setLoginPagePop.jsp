<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function saveLginPage(){
	if(validator.validate('setLginPageForm')){
		var $form = $("#setLginPageForm");
		$form.attr('method','post');
		$form.attr('action','./transLoginPage.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form.submit();
	}
}
//-->
</script>
<div style="width:550px">
<form id="setLginPageForm" method="post" enctype="multipart/form-data"><c:if
	test="${not empty ptLginImgDVo.lginImgId}">
<u:input type="hidden" id="lginImgId" value="${ptLginImgDVo.lginImgId}" /></c:if>

<u:listArea>

	<tr>
	<td width="25%" class="head_lt"><u:mandatory /><u:msg titleId="cols.lginPageNm" alt="로그인 페이지명" /></td>
	<td>
		<table cellspacing="0" cellpadding="0" border="0">
		<tr>
		<td id="langTypArea">
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
		<u:convert srcId="${ptLginImgDVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
		<u:set test="${status.first}" var="style" value="" elseValue="display:none" />
		<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
		<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.lginPageNm" value="${rescVa}" style="${style}"
			maxByte="200" validator="changeLangSelector('setLginPageForm', id, va)" mandatory="Y" />
		</c:forEach>
		</td>
		<td>
		<c:if test="${fn:length(_langTypCdListByCompId)>1}">
			<select id="langSelector" onchange="changeLangTypCd('setLginPageForm','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.cdVa}" />
			</c:forEach>
			</select>
		</c:if>
		<u:input type="hidden" id="rescId" value="${ptLginImgDVo.rescId}" />
		</td>
		</tr>
		</table>
	</td>
	</tr>

	<tr>
	<td class="head_lt"><c:if
		test="${empty ptLginImgDVo.lginImgId}"><u:mandatory /></c:if><u:msg titleId="cols.bgImgFile" alt="배경 이미지" /></td>
	<td><u:file id="bgImgFile" exts="jpg,gif,png" recomend="756 X 465"
		mandatory="${empty ptLginImgDVo.lginImgId ? 'Y' : ''}" /></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.logoImgFile" alt="로고 이미지" /></td>
	<td><u:file id="logoImgFile" exts="jpg,gif,png" recomend="193 X 63" /></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="ap.cols.prd" alt="기간" /></td>
	<td><table cellspacing="0" cellpadding="0" border="0"><tr>
		<td><u:calendar id="strtYmd" value="${ptLginImgDVo.strtYmd}" option="{end:'endYmd'}" /></td>
		<td style="padding: 0 4px 0 6px">~</td>
		<td><u:calendar id="endYmd" value="${ptLginImgDVo.endYmd}" option="{start:'strtYmd'}" /></td>
	</tr></table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
	<td><select id="useYn" name="useYn" <u:elemTitle titleId="cols.useYn" />>
		<u:option value="Y" titleId="cm.option.use" checkValue="${ptLginImgDVo.useYn}" />
		<u:option value="N" titleId="cm.option.notUse" checkValue="${ptLginImgDVo.useYn}" />
		</select></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.dftSetupYn" alt="기본설정여부" /></td>
	<td style="padding-left:1px"><u:checkArea>
		<u:checkbox value="Y" name="dftImgYn" titleId="cols.dftSetupYn" alt="기본설정여부" checked="${ptLginImgDVo.dftImgYn == 'Y'}" />
	</u:checkArea></td>
	</tr>

</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" href="javascript:saveLginPage();" alt="저장" auth="A" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</form>
</div>