<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

//게시물 삭제
function delBull(){
	if (confirmMsg("cm.cfrm.del")) {
		callAjax('./transBullDel.do?menuId=${menuId}&ctId=${ctId}', {bullId : '${ctRecMastBVo.bullId}'}, function(data){
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.href = './listPds.do?menuId=${menuId}&ctId=${ctId}';
			}
		});
	}
}

//이메일 전송
function sendEmail(regrUid) {
	var $bullCont = $("#bullCont").val();
	var recvIds = [];
	recvIds.push(regrUid);
	
	if(recvIds == ''){
		callAjax('./sendMailBull.do?menuId=${menuId}&ctId=${ctId}', {bullId:'${ctRecMastBVo.bullId}', bullCont:$bullCont}, function(data){
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				dialog.open('sendEmailPop','메일보내기','/cm/sendEmailPop.do?menuId=${menuId}&emailId='+data.emailId);
			}
		});
	}else{
		callAjax('./sendMailBull.do?menuId=${menuId}&ctId=${ctId}', {recvId:recvIds, bullCont:$bullCont}, function(data){
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				dialog.open('sendEmailPop','메일보내기','/cm/zmailPop.do?menuId=${menuId}&emailId='+data.emailId);
			}
		});
	}
	
	
	//dialog.open('sendEmailPop','이메일 발송','../sendEmailPop.do?menuId=${menuId}');
}

//보내기 팝업
function sendTo() {
	dialog.open('sendToPop','보내기','../sendToPop.do?menuId=${menuId}&ctId=${ctId}&bullId=${ctRecMastBVo.bullId}');
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<c:set var="subj" value="Java Virtual Machine Specification" />
<c:set var="regr" value="이채린" />
<c:set var="regDt" value="2014-01-20" />
<c:set var="readCnt" value="1" />
<c:set var="cont" value="JVM 규격 문서입니다. 참고하시기 바랍니다. 감사합니다." />

<u:title titleId="ct.jsp.viewPds.title" alt="자료실 조회" menuNameFirst="true"/>

<form id="viewPdsForm">
<u:input type="hidden" id="menuId" value="${menuId}" />

<% // 폼 필드 %>
<u:listArea>
	<tr>
	<td class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td colspan="3" class="body_lt">${ctRecMastBVo.subj}</td>
	</tr>
	
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.regr" alt="등록자" /></td>
	<td width="32%" class="body_lt">
		<a href="javascript:sendEmail('${ctRecMastBVo.regrUid}');">${ctRecMastBVo.regrNm}
	</td>
	<td width="18%" class="head_lt"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	<td width="32%" class="body_lt">
		<fmt:parseDate var="dateTempParse" value="${ctRecMastBVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
		<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
	</td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.readCnt" alt="조회수" /></td>
	<td colspan="3" class="body_lt">${ctRecMastBVo.readCnt}</td>
	</tr>
	
	<tr>
	<td colspan="4" class="body_lt">${ctRecMastBVo.cont}
		<input id="bullCont" name="bullCont" type="hidden" value="${ctRecMastBVo.cont}"/>
	</td>
	</tr>
	
	<tr>
		<td class="head_lt"><u:msg titleId="cols.attFile" alt="첨부파일" /></td>
		<td colspan="3">
			<c:if test="${!empty fileVoList }"><u:files id="${filesId}" fileVoList="${fileVoList}" module="ct" mode="view" /></c:if>
		<!-- 
			<div class="attachbtn">
			<dl>
			<dd class="attachbtn_check"><input type="checkbox" id="contactBack"/></dd>
			<dd><a href="javascript:" class="sbutton button_small"><span>다운로드</span></a></dd>
			</dl>
			</div>
	
			<div class="attacharea">
			<dl>
			<dd class="attach_check"><input type="checkbox" id="contactBack"/></dd>
			<dd class="attach_img"><img src="${_cxPth}/images/cm/ico_zip.png"/></dd>
			<dd class="attach"><a href="javascript:">JVMSpec1.zip</a></dd>
			</dl>
			</div>
			
			<div class="attacharea">
			<dl>
			<dd class="attach_check"><input type="checkbox" id="contactBack"/></dd>
			<dd class="attach_img"><img src="${_cxPth}/images/cm/ico_zip.png"/></dd>
			<dd class="attach"><a href="javascript:">JVMSpec2.zip</a></dd>
			</dl>
			</div>
			 -->
		</td>
	</tr>
</u:listArea>
<div style="height: 25px;"><a href="javascript:" onclick="$('#listCmtFrm').toggle();"><u:msg titleId="cols.cmt" alt="한줄답변" /> (${ctRecMastBVo.cmtCnt})</a></div>
<iframe id="listCmtFrm" name="listCmtFrm" src="./listCmtFrm.do?menuId=${menuId}&ctId=${ctId}&bullId=${ctRecMastBVo.bullId}" style="width:100%;" frameborder="0" marginheight="0" marginwidth="0"></iframe>


<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.send" alt="보내기" href="javascript:sendTo();" auth="R" />
	<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" auth="R" />
	<u:button titleId="cm.btn.mod" alt="수정" href="./setPds.do?menuId=${menuId}&ctId=${ctId}&bullId=${ctRecMastBVo.bullId}" auth="M" />
	<u:msg titleId="cm.cfrm.del" alt="삭제하시겠습니까?" var="msg" />
	<u:button titleId="cm.btn.del" alt="삭제" href="javascript:delBull();" auth="W" />
	<u:button titleId="cm.btn.list" alt="목록" auth="R" href="./listPds.do?menuId=${menuId}&ctId=${ctId}" />
</u:buttonArea>

</form>

