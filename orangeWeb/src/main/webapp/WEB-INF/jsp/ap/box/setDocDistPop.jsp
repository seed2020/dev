<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--<%
// 조직도 클릭시 호출 - 아무것도 안함 - 프레임간 오류 방지 %>
function setDistDeptVo(){}<%
// 다중 선택일때 초기에 호출됨 - 아무것도 안함 - 프레임간 오류 방지 %>
function processSelect(){}<%
%>
var gDistDeptAttrNms = ["sendSeq","recvDeptId","recvDeptNm"];<%
// [>]추가 아이콘 클릭 %>
function addDistDept(){
	var iwin = getIframeContent("distOrgTreeArea");
	var arr = iwin.getSelectedNodes(), newArr=[];
	var alreadys = collectDistDept('recvDeptId');
	arr.each(function(idx, va){<%// 해당 명징으로 데이터 전환 - 부서(기관):수신처 %>
		if(va.orgId!='ROOT' && !alreadys.contains(va.orgId)){
			newArr.push({recvDeptId:va.orgId, recvDeptNm:va.rescNm});
		}
	});
	addDistDeptData(newArr);
	iwin.deSelectNodes();<%// 선택 해제 %>
}<%
// [<]삭제 아이콘 클릭 - 선택 제거 %>
function removeDistDept(){
	$("#distDeptDataPopArea input[id!='checkHeader']:checked:visible").each(function(){
		$(getParentTag(this, 'tr')).remove();
	});
}<%
// 추가 데이터 배열로 각각 줄(TR) 생성 %>
function addDistDeptData(arr){
	var $lastTr = $("#distDeptDataPopArea tr:last"), $newTr, $check;
	arr.each(function(idx, obj){
		$newTr = $lastTr.clone();<%// 숨겨진 TR 복사 %>
		$check = $newTr.find("input[type='checkbox']");<%// checkbox 의 속성에 데이터 할당 %>
		gDistDeptAttrNms.each(function(idx, nm){
			$check.attr('data-'+nm, obj[nm]==null ? '' : obj[nm]);
		});
		$check.uniform();<%// 유니폼 적용 %>
		<%// 각 TD에 해당하는 데이터 입력 %>
		$newTr.find("td:eq(1)").text(obj["recvDeptNm"]==null ? '' : obj["recvDeptNm"]);
		$newTr.show();
		<%// TR 삽입 %>
		$newTr.insertBefore($lastTr);
	});
}<%
// 접수처 부서ID 데이터 모으기 %>
function collectDistDeptIds(){
	return collectDistDept('recvDeptId');
}<%
// 접수처 데이터 모으기 - setDoc.jsp 에서도 호출함 %>
function collectDistDept(attr, checkNotDist){
	var arr = [], obj, $check, va, addedCnt=0;
	$("#distDeptDataPopArea input[id!='checkHeader']:visible").each(function(){
		if(attr==null){
			$check = $(this);
			obj = {};
			gDistDeptAttrNms.each(function(idx, name){
				va = $check.attr("data-"+name);
				obj[name] = va==null ? '' : va;
			});
			if(checkNotDist==true && $check.attr('disabled')!='disabled'){
				addedCnt++;
			}
			arr.push(obj);
		} else {
			va = $(this).attr("data-"+attr);
			arr.push(va);
		}
	});
	if(checkNotDist==true){
		if(addedCnt==0){<%
			// ap.msg.noPlaceToDist=배부 할 수신처가 없습니다. %>
			alertMsg('ap.msg.noPlaceToDist');
			return null;
		}
		return arr;
	} else {
		return arr;
	}
}
//-->
</script>
<div style="width:600px">

<u:tabArea outerStyle="height:370px; overflow-x:hidden; overflow-y:auto;" innerStyle="margin:10px 10px 0px 10px;" ><u:cmt


	cmt="[왼쪽] 조직도"/>
<u:boxArea className="wbox" style="height:350px; width:46%; float:left"
	outerStyle="height:346px;overflow:hidden;"
	innerStyle="NO_INNER_IDV" noBottomBlank="true" >
<iframe id="distOrgTreeArea" style="width:100%; height:344px; border:0px;<c:if test="${onTab != 'dom' and not empty onTab}"> display:none;</c:if>"
	src="./treeDistDeptFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}" ></iframe>	
</u:boxArea><u:cmt


	cmt="[가운데] [>], [<] 선택추가, 선택삭제 용"/>
<div style="width:7%; float:left; text-align:center; margin:148px 0 0 0;">
	<table style="margin:0 auto 0 auto;" border="0" cellpadding="0" cellspacing="0">
		<tr><td><a href="javascript:addDistDept();"<u:elemTitle titleId="cm.btn.selAdd" alt="선택추가" type="image" />><img src="${_cxPth}/images/${_skin}/ico_right.png" width="20" height="20" /></a></td></tr>
		<tr><td class="height5"></td></tr>
		<tr><td><a href="javascript:removeDistDept();"<u:elemTitle titleId="cm.btn.selDel" alt="선택삭제" type="image" />><img src="${_cxPth}/images/${_skin}/ico_left.png" width="20" height="20" /></a></td></tr>
	</table>
</div>
<u:cmt


	cmt="[오른쪽] 추가된 배부처 정보용"/>
<u:boxArea className="wbox" style="height:350px; width:47%; float:right"
	outerStyle="height:346px; overflow:hidden;"
	innerStyle="NO_INNER_IDV" noBottomBlank="true" >
	
<u:listArea id="distDeptDataPopArea" colgroup="6%,94%" style="height:332px; padding:10px 8px 2px 10px; width:92.6%; overflow:auto;">
	<tr id="titleTr"><td class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all'
		/>" onclick="checkAllCheckbox('distDeptDataPopArea', this.checked);" value=""/></td>
		<td class="head_ct"><u:msg titleId="cols.recvDept" alt="수신처" /></td>
	</tr>
<c:forEach items="${apOngdSendDVoList}" var="apOngdSendDVo" varStatus="status">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'<c:if
		test="${status.last}"> style="display:none;" id="hiddenTr"</c:if>>
		<td class="bodybg_ct"><input type="checkbox" name="apvLnCheck"<c:if
				test="${status.last}"> class="skipThese"</c:if>
			data-sendSeq="${apOngdSendDVo.sendSeq
			}" data-recvDeptId="${apOngdSendDVo.recvDeptId
			}" data-recvDeptNm="<u:out value="${apOngdSendDVo.recvDeptNm}" type="value" 
			/>"<c:if test="${not status.last}"> disabled="disabled"</c:if>
			/></td>
		<td class="body_ct"><u:out value="${apOngdSendDVo.recvDeptNm}" /></td>
	</tr>
</c:forEach>
</u:listArea>

</u:boxArea>
</u:tabArea>

<u:buttonArea><c:if
		test="${param.callback=='processBulkDist'}">
	<u:button titleId="ap.btn.bulkDist" alt="일괄배부" href="javascript:processBulkDist();" auth="A" />
	</c:if><c:if
		test="${param.callback!='processBulkDist'}">
	<u:button titleId="ap.btn.dist" alt="배부" href="javascript:processDist();" auth="A" />
	</c:if>
	<u:button onclick="dialog.close(this);" alt="닫기" titleId="cm.btn.close" />
</u:buttonArea>
</div>
