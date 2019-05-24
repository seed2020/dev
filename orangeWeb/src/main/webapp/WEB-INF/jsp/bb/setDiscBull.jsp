<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="params" excludes="bullId" />
<script type="text/javascript">
<!--
<% // [이전글/다음글] 게시물 심의 %>
function discBull(id) {
	location.href = './${setPage}.do?${params}&bullId=' + id;
}

<% // 게시대상 부서 표시 %>
function setBbTgtDept(len) {
	var $nameTd = $("#bbTgtDept");
	if (len > 0) {
		$nameTd.html('<u:msg titleId="cols.dept" alt="부서" /> ' + len);
		$nameTd.show();
	} else {
		$nameTd.html('');
		$nameTd.hide();
	}
}
<% // 게시대상 사용자 표시 %>
function setBbTgtUser(len) {
	var $nameTd = $("#bbTgtUser");
	if (len > 0) {
		$nameTd.html('<u:msg titleId="cols.user" alt="사용자" /> ' + len);
		$nameTd.show();
	} else {
		$nameTd.html('');
		$nameTd.hide();
	}
}
<% // 다중 부서 선택 - 하위부서 여부 포함 %>
function openMuiltiOrgWithSub(mode){
	var $inputTd = $("#bbTgtDeptHidden"), data = [];<% // data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	var $subs = $inputTd.find("input[name='withSubYn']");
	$inputTd.find("input[name='orgId']").each(function(index){
		data.push({orgId:$(this).val(), withSub:$($subs[index]).val()});
	});
	<% // option : data, multi, withSub, titleId %>
	searchOrgPop({data:data, multi:true, withSub:true, mode:mode==null ?'search':'view'}, function(arr){
		if(arr!=null){
			var buffer = [];
			arr.each(function(index, orgVo){
				buffer.push("<input name='orgId' type='hidden' value='"+orgVo.orgId+"' />\n");
				buffer.push("<input name='withSubYn' type='hidden' value='"+orgVo.withSub+"' />\n");
				buffer.push("\n");
			});
			$inputTd.html(buffer.join(''));
			setBbTgtDept(arr.length);
		} else {
			$inputTd.html('');
			setBbTgtDept(0);
		}
		//return false;// 창이 안닫힘
	});
}
<% // 여러명의 사용자 선택 %>
function openMuiltiUser(mode){
	var $inputTd = $("#bbTgtUserHidden"), data = [];<% // data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	$inputTd.find("input[name='userUid']").each(function(){
		data.push({userUid:$(this).val()});
	});
	<% // option : data, multi, titleId %>
	searchUserPop({data:data, multi:true, mode:mode==null ?'search':'view'}, function(arr){
		if(arr!=null){
			var buffer = [];
			arr.each(function(index, userVo){
				buffer.push("<input name='userUid' type='hidden' value='"+userVo.userUid+"' />\n");
				buffer.push("\n");
			});
			$inputTd.html(buffer.join(''));
			setBbTgtUser(arr.length);
		} else {
			$inputTd.html('');
			setBbTgtUser(0);
		}
	});
}

<% // [하단버튼:승인] %>
function apvdBull() {
	$('#bullStatCd').val('B');
	submitForm('bb.cnfm.apvd');
}
<% // [하단버튼:반려] %>
function rjtBull() {
	$('#bullStatCd').val('J');
	if($('#rjtOpin').val() == ''){
		alertMsg('cm.input.check.mandatory','<u:msg titleId="cols.rjtOpin"  />');
		return;
	}
	submitForm('bb.cnfm.rjt');
}
<% // submit form %>
function submitForm(cnfmMsg) {
	if (validator.validate('setDiscBullForm') && confirmMsg(cnfmMsg)) {
		var $form = $('#setDiscBullForm');
		$form.attr('method','post');
		$form.attr('action','./transDiscBull.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
}
<% // [하단버튼:목록] 목록으로 이동 %>
function goList() {
	location.href = './${listPage}.do?${params}';
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="bb.jsp.viewDiscBull.title" alt="심의함 게시물 조회" menuNameFirst="true"/>

<form id="setDiscBullForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="params" value="${params}" />
<u:input type="hidden" id="listPage" value="./${listPage}.do?${params}" />
<u:input type="hidden" id="brdId" value="${baBrdBVo.brdId}" />
<u:input type="hidden" id="bullId" value="${param.bullId}" />
<u:input type="hidden" id="bullStatCd" value="S" />
<u:input type="hidden" id="bullRezvDt" value="${bbBullLVo.bullRezvDt}" />

<% // 폼 필드 %>
<u:listArea colgroup="18%,32%,18%,32%">
	<tr>
	<td class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td colspan="3" class="body_lt"><u:out value="${bbBullLVo.subj}" /></td>
	</tr>

	<u:set test="${brdNms != null}" var="brdNms" value="${brdNms}" elseValue="${baBrdBVo.rescNm}" />
	<tr>
	<td class="head_lt"><u:msg titleId="cols.bb" alt="게시판" /></td>
	<td colspan="3"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="body_lt"><span id="brdNms">${brdNms}</span></td>
		</tr>
		</table></td>
	</tr>

	<c:if test="${baBrdBVo.catYn == 'Y'}">
	<tr>
	<td class="head_lt"><u:msg titleId="cols.cat" alt="카테고리" /></td>
	<td colspan="3" class="body_lt">${bbBullLVo.catNm}</td>
	</tr>
	</c:if>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.regr" alt="등록자" /></td>
	<td class="body_lt">
		<c:if test="${baBrdBVo.brdTypCd == 'N'}">
		<a href="javascript:viewUserPop('${bbBullLVo.regrUid}');"><u:out value="${bbBullLVo.regrNm}" /></a>
		</c:if>
		<c:if test="${baBrdBVo.brdTypCd == 'A'}">
		<u:out value="${bbBullLVo.anonRegrNm}" />
		</c:if>
		</td>
	<td class="head_lt"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	<td class="body_lt"><u:out value="${bbBullLVo.regDt}" type="date" /></td>
	</tr>
	
	<tr>
	<c:if test="${baBrdBVo.allCompYn != 'Y'}">
	<td class="head_lt"><u:msg titleId="bb.cols.bbTgt" alt="게시대상" /></td>
	<td><%-- <u:buttonS titleId="bb.cols.bbTgt" alt="게시대상" href="javascript:dialog.open('setBbAuthPop', '권한 설정', '/bb/setBbAuthPop.do?menuId=${menuId}');" /> --%>
		<table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td id="bbTgtDeptHidden">
			<c:if test="${bullTgtDeptVoList != null}">
				<c:forEach items="${bullTgtDeptVoList}" var="baBullTgtDVo" varStatus="status">
				<input name="orgId" type="hidden" value="${baBullTgtDVo.tgtId}" />
				<input name="withSubYn" type="hidden" value="${baBullTgtDVo.withSubYn}" />
				</c:forEach>
			</c:if>
			</td>
		<td id="bbTgtDept" style="display: none;" class="body_lt"></td>
		<td><u:buttonS titleId="bb.btn.dept" alt="부서" onclick="openMuiltiOrgWithSub('view')" /></td>
		<td id="bbTgtUserHidden">
			<c:if test="${bullTgtUserVoList != null}">
				<c:forEach items="${bullTgtUserVoList}" var="baBullTgtDVo" varStatus="status">
				<input name="userUid" type="hidden" value="${baBullTgtDVo.tgtId}" />
				</c:forEach>
			</c:if>
			</td>
		<td id="bbTgtUser" style="display: none;" class="body_lt"></td>
		<td><u:buttonS titleId="bb.btn.user" alt="사용자" onclick="openMuiltiUser('view')" /></td>
		</tr>
		</tbody></table>
	</td>
	</c:if>
	<u:set test="${baBrdBVo.allCompYn != 'Y'}" var="colspan" value="1" elseValue="3" />
	<td class="head_lt"><u:msg titleId="bb.cols.bbOpt" alt="게시옵션" /></td>
	<td colspan="${colspan}"><u:checkArea>
		<u:checkbox name="secuYn" value="Y" titleId="bb.option.secu" alt="보안" inputClass="bodybg_lt" checkValue="${bbBullLVo.secuYn}" disabled="Y" />
		<u:checkbox name="ugntYn" value="Y" titleId="bb.option.ugnt" alt="긴급" inputClass="bodybg_lt" checkValue="${bbBullLVo.ugntYn}" disabled="Y" />
		<u:checkbox name="notcYn" value="Y" titleId="bb.option.notc" alt="공지" inputClass="bodybg_lt" checkValue="${bbBullLVo.notcYn}" disabled="Y" />
		<u:input type="hidden" id="secuYn" value="${bbBullLVo.secuYn}" />
		<u:input type="hidden" id="ugntYn" value="${bbBullLVo.ugntYn}" />
		<u:input type="hidden" id="notcYn" value="${bbBullLVo.notcYn}" />
		</u:checkArea></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.bullRezvDt" alt="게시예약일" /></td>
	<td colspan="3" class="body_lt"><u:out value="${bbBullLVo.bullRezvDt}" type="longdate" /></td>
	</tr>
	
	<!-- 게시물사진 -->
	<c:if test="${baBrdBVo.photoYn == 'Y'}">
	<tr>
	<td class="head_lt"><u:msg alt="사진 선택" titleId="bb.cols.photo" /></td>
	<td colspan="3">
		<c:if test="${bbBullLVo.photoVo != null}">
		<c:set var="maxWdth" value="800" />
		<u:set test="${bbBullLVo.photoVo.imgWdth <= maxWdth}" var="imgWdth" value="${bbBullLVo.photoVo.imgWdth}" elseValue="${maxWdth}" />
		<div style="padding: 4px;"><img src="${_cxPth}${bbBullLVo.photoVo.savePath}" width="${imgWdth}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"'></div>
		</c:if>
		</td>
	</tr>
	</c:if>
	
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.rjtOpin" alt="반려의견" /></td>
	<td colspan="3" class="body_lt"><u:textarea id="rjtOpin" value="" titleId="cols.rjtOpin" maxByte="1000" style="width:98%" rows="3" /></td>
	</tr>

	<tr>
	<td colspan="4" class="body_lt"><u:clob lobHandler="${lobHandler }"/></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="cols.attFile" alt="첨부파일" /></td>
	<td colspan="3">
		<c:if test="${!empty fileVoList }"><u:files id="${filesId}" fileVoList="${fileVoList}" module="bb" mode="view" /></c:if>
	</td>
	</tr>
</u:listArea>

<% // 이전글 다음글 %>
<u:listArea>
<c:if test="${prevBullVo == null}">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td width="6%" class="listicon_ct"><u:icon type="prev" /></td>
	<td class="body_lt"><u:msg titleId="bb.jsp.viewBull.prevNotExists" alt="이전글이 존재하지 않습니다." /></td>
	<td class="body_ct">&nbsp;</td>
	<td width="10%" class="body_ct">&nbsp;</td>
	</tr>
</c:if>
<c:if test="${prevBullVo != null}">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td width="6%" class="listicon_ct"><a href="javascript:discBull('${prevBullVo.bullId}');"><u:icon type="prev" /></a></td>
	<td class="body_lt"><a href="javascript:discBull('${prevBullVo.bullId}');" title="${prevBullVo.subj}"><u:out value="${prevBullVo.subj}" maxLength="80" /></a></td>
	<td class="body_ct"><a href="javascript:viewUserPop('${prevBullVo.regrUid}');">${prevBullVo.regrNm}</a></td>
	<td width="10%" class="body_ct"><u:out value="${prevBullVo.regDt}" type="date" /></td>
	</tr>
</c:if>

<c:if test="${nextBullVo == null}">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td width="6%" class="listicon_ct"><u:icon type="next" /></td>
	<td class="body_lt"><u:msg titleId="bb.jsp.viewBull.nextNotExists" alt="다음글이 존재하지 않습니다." /></td>
	<td class="body_ct">&nbsp;</td>
	<td width="10%" class="body_ct">&nbsp;</td>
	</tr>
</c:if>
<c:if test="${nextBullVo != null}">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td width="6%" class="listicon_ct"><a href="javascript:discBull('${nextBullVo.bullId}');"><u:icon type="next" /></a></td>
	<td class="body_lt"><a href="javascript:discBull('${nextBullVo.bullId}');" title="${nextBullVo.subj}"><u:out value="${nextBullVo.subj}" maxLength="80" /></a></td>
	<td class="body_ct"><a href="javascript:viewUserPop('${nextBullVo.regrUid}');">${nextBullVo.regrNm}</a></td>
	<td width="10%" class="body_ct"><u:out value="${nextBullVo.regDt}" type="date" /></td>
	</tr>
</c:if>
</u:listArea>

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.apvd" alt="승인" onclick="apvdBull();" auth="W" />
	<u:button titleId="cm.btn.rjt" alt="반려" onclick="rjtBull();" auth="W" />
	<u:button titleId="cm.btn.list" alt="목록" onclick="goList();" />
</u:buttonArea>

</form>

