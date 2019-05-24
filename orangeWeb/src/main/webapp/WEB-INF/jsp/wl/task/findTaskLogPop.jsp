<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	String callback = (String)request.getAttribute("callback");
	if(callback==null || callback.isEmpty()) callback = "setLogList";
%><u:params var="paramsForList" excludes="logNo"/>
<script type="text/javascript">
<!--<% // 탭 클릭 - 일일:/주간:/월간:/연간: %>
function toggleTabBtn(typCd){
	getIframeContent('openListFrm').toggleTabBtn(typCd);
};<%// [순서조절:위로,아래로] 서버에 저장하지 않고 화면상에서만 순서 조정함 %>
function move(direction){
	var i, arr = getCheckedTrs("cm.msg.noSelect");
	if(arr==null) return;
	
	var $node, $prev, $next, $std;
	if(direction=='up'){
		$std = $('#headerTr');
		for(i=0;i<arr.length;i++){
			$node = $(arr[i]);
			$prev = $node.prev();
			if($prev[0]!=$std[0]){
				$prev.before($node);
			}
			$std = $node;
		}
	} else if(direction=='down'){
		$std = $('#hiddenTr');
		for(i=arr.length-1;i>=0;i--){
			$node = $(arr[i]);
			$next = $node.next();
			if($next[0]!=$std[0]){
				$next.after($node);
			}
			$std = $node;
		}
	}
}<%// [소버튼] +추가 %>
function addSelectedLog(){
	var frameId = "openListFrm";
	if(getIframeContent(frameId).location.href.indexOf("reloadable.do")<0){
		var arr = getIframeContent(frameId).getSelectedLogs(true);
		if(arr!=null) addLogs(arr);
	}
}
<%// 선택삭제%>
function delSelRow(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null){
		delRowInArr(arr);
	}
}
<%// 삭제 - 배열에 담긴 목록%>
function delRowInArr(rowArr){
	var delArr = [], $userUid;
	for(var i=0;i<rowArr.length;i++){
		$(rowArr[i]).remove();
	}
}
<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(noSelectMsg){
	var arr=[], id, obj;
	$("#taskLogArea tbody:first input[id='chkLog']:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
}
<%// [버튼] -확인 %>
function setCollectedLogs(isInit){
	var arr = getSelectedAllLogs();
	if(arr==null) return;
	parent.<%= callback%>(arr, isInit);
	dialog.close('findTaskLogDialog');
}<%//현재 등록된 id 목록 리턴 %>
function getSelectedAllLogs(){
	var arr=[], id, obj;
	$('#taskLogArea tbody:first').not('#headerTr, #hiddenTr').find('input[id="chkLog"]').each(function(){
		if($(this).val()!='')
			arr.push($(this).val());
	});
	if(arr.length==0){
		return null;
	}
	return arr;
}<%//선택된 일지 추가 %>
function addLogs(arr){
	if(arr!=null){
		var $tr, $hiddenTr = $("#taskLogArea tbody:first #hiddenTr");
		var html = $hiddenTr[0].outerHTML;
		
		var vas = getSelectedAllLogs();
		arr.each(function(index, vo){
			if(vas==null || !vas.contains(vo.logNo)){
				$hiddenTr.before(html);
				$tr = $hiddenTr.prev();
				$tr.attr('id','tr'+vo.logNo);
				$tr.find("input[id='chkLog']").val(vo.logNo);
				$tr.find("div#subj").text(vo.subj);
				$tr.find("td#regrNm").text(vo.regrNm);
				$tr.find("td#reprtDt").text(vo.reprtDt);
				
				$tr.show();
				setJsUniform($tr[0]);
			}
		});
	}
}<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
}
$(document).ready(function() {
});
//-->
</script>
<div style="width:700px;">
<%
	String multi = request.getParameter("multi");
	if(multi != null && "Y".equals(multi)){
		request.setAttribute("outHeight", "300px");
		request.setAttribute("inHeight", "290px");
		request.setAttribute("divHeight", "215px");
		request.setAttribute("frmHeight", "290px");
	} else {
		request.setAttribute("outHeight", "370px");
		request.setAttribute("inHeight", "350px");
		request.setAttribute("divHeight", "345px");
		request.setAttribute("frmHeight", "340px");
	}
%>
<c:if test="${isSelect eq true }">
<c:if test="${!empty tabList && fn:length(tabList) > 1}">
<u:tabGroup noBottomBlank="${true}">
	<c:forEach var="tab" items="${tabList }" varStatus="status">
		<u:tab alt="목록전체" titleId="wl.cols.typCd.${tab }"
		on="${(empty param.typCd && status.first) || param.typCd == tab}"
		onclick="toggleTabBtn('${tab }');" />
	</c:forEach>
</u:tabGroup>
<u:blank />
</c:if>

<u:tabArea
	outerStyle="height:${outHeight}; overflow-x:hidden; overflow-y:hidden;"
	innerStyle="height:${inHeight};margin:10px;">
<u:set
		test="${!empty wlTaskLogBVoList}" var="userGrpUrl" value="./listRecvFrm.do?${paramsForList }&pageRowCnt=5" elseValue="/cm/util/reloadable.do" />
<iframe id="openListFrm" name="openListFrm" src="${userGrpUrl }" style="width:100%; height:${frmHeight};" frameborder="0" marginheight="0" marginwidth="0"></iframe>

</u:tabArea>

<c:if test="${param.multi == 'Y'}">

<u:title alt="선택된 리스트" titleId="cm.selectedList" >
	<u:titleButton titleId="cm.btn.add" alt="추가" img="ico_add.png" href="javascript:addSelectedLog();" />
	<u:titleButton titleId="cm.btn.del" alt="삭제" img="ico_minus.png" href="javascript:delSelRow();" />
	<u:titleIcon type="up" onclick="move('up')" id="moveUpBtn" auth="W" />
	<u:titleIcon type="down" onclick="move('down')" id="moveDownBtn" auth="W" />
</u:title>
</c:if>
</c:if>

<u:titleArea
	outerStyle="height:${divHeight}; overflow-x:hidden; overflow-y:auto; padding:5px"
	innerStyle="NO_INNER_IDV">

<c:set var="chkYn" value="N"/>
<c:set var="colspan" value="3"/>
<c:set var="colgroup" value=",15%,15%"/>
<c:if test="${isSelect eq true && param.multi=='Y'}">
<c:set var="chkYn" value="Y"/>
<c:set var="colspan" value="${colspan+1 }"/>
<c:set var="colgroup" value="3%,${colgroup }"/>
</c:if>
<c:if test="${fileYn eq 'Y' }">
<c:set var="colspan" value="${colspan+1 }"/>
<c:set var="colgroup" value="${colgroup },10%"/>
</c:if>
<u:listArea id="taskLogArea" colgroup="${colgroup }">
	<tr id="headerTr">
		<c:if test="${chkYn eq 'Y'}"><td class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('taskLogArea', this.checked);" value=""/></td></c:if>
		<th class="head_ct"><u:msg titleId="cols.subj" alt="제목"/></th>
		<th class="head_ct"><u:msg titleId="cols.regr" alt="등록자"/></th>
		<th class="head_ct"><u:msg titleId="wl.cols.reprtDt" alt="보고일자"/></th>
		<c:if test="${fileYn eq 'Y' }"><th class="head_ct"><u:msg titleId="cols.att" alt="첨부"/></th></c:if>
	</tr>
	<c:if test="${fn:length(wlTaskLogBVoList)>0}">
		<c:forEach items="${wlTaskLogBVoList}" var="wtTaskLogBVo" varStatus="status"
		><u:set test="${isSelect eq true && status.last}" var="trDisp" value="display:none"
			/><u:set test="${isSelect eq true && status.last}" var="trId" value="hiddenTr" elseValue="tr${wtTaskLogBVo.logNo}"
			/><u:set test="${isSelect eq true && status.last}" var="clsIndex" value="_NO" elseValue="_${status.index+1}"
			/>
			<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="${trId}" style="${trDisp}" >
				<c:if test="${isSelect eq true && param.multi=='Y'}"><td class="bodybg_ct"><input type="checkbox" id="chkLog" value="${wtTaskLogBVo.logNo }" /></td></c:if>
				<td class="body_lt"><div class="ellipsis" id="subj"><u:out value="${wtTaskLogBVo.subj }"/></div></td>
				<td class="body_ct" id="regrNm"><a href="javascript:viewUserPop('${wtTaskLogBVo.regrUid }');"><u:out value="${wtTaskLogBVo.regrNm }" /></a></td>
				<td class="body_ct" id="reprtDt"><u:out value="${wtTaskLogBVo.reprtDt }" type="date"/></td>
				<c:if test="${fileYn eq 'Y' }"><td class="body_ct"><c:if test="${wtTaskLogBVo.fileCnt>0}"><a href="javascript:viewFileListPop('${wtTaskLogBVo.logNo}');"><u:icon type="att" /></a></c:if></td></c:if>
			</tr>
		</c:forEach>	
	</c:if>
	
</u:listArea>

</u:titleArea>

<u:buttonArea><c:if test="${isSelect eq true }"><c:if
		test="${param.multi!='Y'}">
	<u:button titleId="cm.btn.confirm" onclick="setLogs();" alt="확인" /></c:if><c:if
		test="${param.multi=='Y'}">
	<u:button titleId="wl.btn.consol.new" onclick="setCollectedLogs(true);" alt="비우고 새로 취합" />
	<u:button titleId="wl.btn.consol.merge" onclick="setCollectedLogs(false);" alt="합치기" /></c:if></c:if>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>


</div>