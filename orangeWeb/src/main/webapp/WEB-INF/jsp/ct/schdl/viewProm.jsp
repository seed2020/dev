<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="wc.jsp.viewProm.title" alt="약속 조회" menuNameFirst="true"/>

<form id="viewProm">
<u:input type="hidden" id="menuId" value="${menuId}" />

<% // 폼 필드 %>
<u:listArea>

	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.promNm" alt="약속명" /></td>
	<td class="body_lt">연말콘서트관람</td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.loc" alt="장소" /></td>
	<td class="body_lt">LG아트센터</td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.promTm" alt="약속시간" /></td>
	<td class="body_lt">2013.12.01 19:00 ~ 20:00</td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.publYn" alt="공개여부" /></td>
	<td class="body_lt"><u:msg titleId="cm.option.apntPubl" alt="지정인공개" /></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.guest" alt="참석자" /></td>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td class="body_lt">영업부</td>
		<td class="body_lt"><a href="javascript:viewUserPop('U0000003');">윤도현</a></td>
		</tr>

		<tr>
		<td class="body_lt">기술부</td>
		<td class="body_lt"><a href="javascript:viewUserPop('U0000003');">장혜진</a></td>
		</tr>
		</tbody></table></td>
	</tr>

	<tr>
	<td colspan="2" class="body_lt">박정현 콘서트...<br>박정현 콘서트...</td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.attFile" alt="첨부파일" /></td>
	<td colspan="3"><div class="attachbtn">
		<dl>
		<dd class="attachbtn_check"><input type="checkbox" id="contactBack"/></dd>
		<dd><a href="javascript:" class="sbutton button_small"><span>다운로드</span></a></dd>
		</dl>
		</div>

		<div class="attacharea">
		<dl>
		<dd class="attach_check"><input type="checkbox" id="contactBack"/></dd>
		<dd class="attach_img"><img src="${_cxPth}/images/cm/ico_zip.png"/></dd>
		<dd class="attach"><a href="javascript:">이노비즈 그룹웨어 스토리보드스토리보드스토리보드스토리보드스토리보드스토리보드.zip</a></dd>
		</dl>
		</div>

		<div class="attacharea">
		<dl>
		<dd class="attach_check"><input type="checkbox" id="contactBack"/></dd>
		<dd class="attach_img"><img src="${_cxPth}/images/cm/ico_egg.png"/></dd>
		<dd class="attach"><a href="javascript:">이노비즈 그룹웨어 스토리보드스토리보드스토리보드스토리보드스토리보드스토리보드.egg</a></dd>
		</dl>
		</div></td>
	</tr>

</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.mod" href="javascript:" alt="수정" auth="W" />
	<u:button titleId="cm.btn.del" href="javascript:" alt="삭제" auth="W" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</form>
