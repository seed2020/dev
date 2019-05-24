<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="params"/>
<script type="text/javascript">
<!--
//1명의 사용자 선택
function schUserPop(prefix){
	var data = {};<%// 팝업 열때 선택될 데이타 %>
	<%// option : data, multi, withSub, titleId %>
	searchUserPop({data:data}, function(userVo){
		$('#'+prefix+'Id').val(userVo.userUid);
		$('#'+prefix+'Nm').val(userVo.rescNm);
	});
};

//여러명의 사용자 선택
function openMuiltiUser(prefix , mode){
	var data = [];<%// data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	if($('#recvId').val() != null && $('#recvId').val() != ''){
		var recvIds = $('#recvId').val().split(',');
		for(var i=0;i<recvIds.length;i++){
			data.push({userUid:recvIds[i]});
		}		
	}
	<%// option : data, multi, titleId %>
	searchUserPop({data:data, multi:true, mode:mode==null ?'search':'view'}, function(arr){
		if(arr!=null){
			var userUids = [];
			var userNms = [];
			arr.each(function(index, userVo){
				userUids.push(userVo.userUid);
				userNms.push(userVo.rescNm);
			});
			$('#'+prefix+'Id').val(userUids.join(','));
			$('#'+prefix+'Nm').val(userNms.join(','));
		}
	});
};

<%// 저장 %>
function save(){
	<%// 서버 전송%>
	if (validator.validate('setRegForm') && confirmMsg("cm.cfrm.save")) {
		
		var $form = $('#setRegForm');
		$form.attr('method','post');		
		$form.attr('action','./transMsgNotice.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		editor('contents').prepare();
		$form[0].submit();
	}
};

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="em.jsp.msg.notice.title" alt="메신저알림" menuNameFirst="true"/>

<form id="setRegForm">
	<u:input type="hidden" id="listPage" value="./setMsgNotice.do?${params }" />
	<u:input type="hidden" name="msgKey" value="msgKey"/>
	<% // 폼 필드 %>
	<div class="listarea">
		<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
		<colgroup>
			<col width="18%"/>
			<col width="32%"/>
			<col width="18%"/>
			<col width="32%"/>
		</colgroup>
		<tbody>
		<tr>
			<td class="head_lt"><u:mandatory /><u:msg titleId="cols.subj" alt="제목" /></td>
			<td><u:input id="subj" name="subj" maxByte="120" style="width: 96%;" mandatory="Y"/></td>
			<td class="head_lt"><u:mandatory /><u:msg titleId="em.cols.msg.systemName" alt="업무명" /></td>
			<td>
				<u:input type="text" id="catNm" name="catNm" titleId="em.cols.msg.systemName" maxByte="60" mandatory="Y"/>
			</td>
		</tr>
		<tr>
			<td class="head_lt"><u:mandatory /><u:msg titleId="em.cols.msg.recvNm" alt="수신자명" /></td>
			<td>
				<u:input type="hidden" id="recvId" name="recvId"/>
				<u:input id="recvNm" name="recvNm" titleId="em.cols.msg.recvNm" style="width:78%;" mandatory="Y" readonly="Y"/>
				<u:buttonS titleId="cm.btn.search" alt="검색" href="javascript:;" onclick="openMuiltiUser('recv');"/>
			</td>
			<td class="head_lt"><u:msg titleId="em.cols.msg.sendNm" alt="발신자명" /></td>
			<td>
				<u:input type="hidden" id="sendId" name="sendId"/>
				<u:input id="sendNm" name="sendNm" titleId="em.cols.msg.sendNm" style="width:100px;" readonly="Y"/>
				<u:buttonS titleId="cm.btn.search" alt="검색" href="javascript:;" onclick="schUserPop('send');"/>
			</td>
		</tr>
		<tr>
			<td class="head_lt"><u:msg titleId="em.cols.msg.url" alt="링크URL" /></td>
			<td colspan="3">
				<u:input id="url" name="url" titleId="em.cols.msg.url" style="width:96%;" />
			</td>
		</tr>
		</tbody>
	</table>
	<u:editor id="contents" width="100%" height="400px" module="em" padding="3" />
	</div>

	<u:blank />
	<u:buttonArea>
		<u:button titleId="cm.btn.save" onclick="save();" alt="저장" auth="A" />
		<u:button titleId="cm.btn.cancel" href="./setMsgNotice.do?${params }" alt="취소" />
	</u:buttonArea>

</form>