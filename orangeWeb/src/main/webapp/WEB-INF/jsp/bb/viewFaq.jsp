<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<% // [하단버튼:수정] 수정 %>
function modBull(id) {
	parent.location.href = './${setPage}.do?${params}&bullId='+id;
}
<% // [하단버튼:삭제] 삭제 %>
function delBull(id) {
	parent.delBull(id);
}
<% // 점수주기 %>
<c:if test="${baBrdBVo.screUseYn == 'Y'}">
function saveScre() {
	if (${screHstExist}) {
		alertMsg("bb.msg.scre.already");<% // bb.msg.scre.already=이미 점수를 준 게시물입니다. %>
		return;
	}
	var $checked = $('input[type=radio][name=scre]:checked');
	if ($checked.length == 0) {
		alertMsg("bb.msg.scre.notChecked");<% // bb.msg.scre.notChecked=점수를 선택하세요. %>
		return;
	}
	callAjax('./transBullScreAjx.do?menuId=${menuId}&bullId=${param.bullId}&brdId=${baBrdBVo.brdId}', {brdId:'${baBrdBVo.brdId}', bullId:'${param.bullId}', scre:$checked.val()}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
	});
}
</c:if>
<% // 점수내역 %>
<c:if test="${baBrdBVo.screUseYn == 'Y'}">
function viewScre() {
	parent.dialog.open('viewScrePop','<u:msg titleId="bb.jsp.viewScrePop.title" alt="점수내역" />','./viewScrePop.do?menuId=${menuId}&bullId=${param.bullId}&brdId=${param.brdId}');
}
</c:if>
$(document).ready(function() {
	setUniformCSS();
});

//-->
</script>
<c:if test="${!empty fileVoList }"><u:files id="${filesId}" fileVoList="${fileVoList}" module="bb" mode="view" urlParam="brdId=${brdId}" /></c:if>
<u:blank />
<% // 점수주기 %>
<c:if test="${baBrdBVo.screUseYn == 'Y' && colmMap.scre.readDispYn=='Y'}">
<u:boxArea className="gbox" outerStyle="height:47px;overflow:hidden;" innerStyle="NO_INNER_IDV" noBottomBlank="true">
<table border="0" cellpadding="0" cellspacing="0"><tbody>
<tr>
<td colspan="2" class="body_lt">※ <u:msg titleId="bb.cols.saveScre" alt="점수주기" /> - <u:msg titleId="bb.jsp.viewBull.saveScre" alt="이 글에 대한 점수를 남겨 주세요." /></td>
</tr>

<tr>
<td style="padding: 0 0 0 15px;"><u:checkArea>
	<u:radio name="scre" value="1" title="★☆☆☆☆" inputClass="bodybg_lt" textStyle="color: #777;" />
	<u:radio name="scre" value="2" title="★★☆☆☆" inputClass="bodybg_lt" textStyle="color: #777;" />
	<u:radio name="scre" value="3" title="★★★☆☆" inputClass="bodybg_lt" textStyle="color: #777;" />
	<u:radio name="scre" value="4" title="★★★★☆" inputClass="bodybg_lt" textStyle="color: #777;" />
	<u:radio name="scre" value="5" title="★★★★★" inputClass="bodybg_lt" textStyle="color: #777;" />
	</u:checkArea></td>
<td>
	<c:if test="${screHstExist == false}">
	<u:buttonS titleId="bb.btn.saveScre" alt="점수주기" onclick="saveScre();" auth="R" />
	</c:if>
</td>
<td><u:buttonS titleId="bb.btn.viewScre" alt="점수내역" onclick="viewScre();" /></td>
</tr>
</tbody></table>
</u:boxArea>
</c:if>

<% // 한줄답변 %>
<c:if test="${baBrdBVo.cmtYn == 'Y'}">
	<div class="blank"></div>
	<div style="height: 25px;"><a href="javascript:" onclick="$('#listCmtFrm').toggle();"><u:msg titleId="cols.cmt" alt="한줄답변" /> (${bbBullLVo.cmtCnt})</a></div>
	<iframe id="listCmtFrm" name="listCmtFrm" src="./listCmtFrm.do?menuId=${menuId}&brdId=${brdId}&bullId=${param.bullId}" style="width:100%;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</c:if>
