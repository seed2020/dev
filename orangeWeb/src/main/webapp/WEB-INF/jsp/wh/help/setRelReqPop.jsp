<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="params" excludes="data"/>
<u:set var="callback" test="${!empty param.callback }" value="${param.callback }" elseValue="setRelReqList"/>
<script type="text/javascript">
<!--<%
// [삭제] 버튼 클릭 - 관련요청 %>
function deleteRelReq(){
	var trArr = [], tr, trId;
	$("#relReqListArea input:checked").each(function(){
		tr = getParentTag(this, 'tr');
		trId = $(tr).attr('id');
		if(trId!='hiddenTr' && trId!='titleTr'){
			trArr.push(tr);
		}
	});
	trArr.each(function(index, tr){
		$(tr).remove();
	});
}<%
// 팝업에서 [확인]버튼 클릭 - 관련요청 팝업에 데이터 세팅 함 %>
function setReqToRelReqPop(reqArrs){
	var $area = $("#relReqListArea");<%
	// 기존 데이터 제거 %>
	$area.find("tr").not("#titleTr").not("#hiddenTr").remove();
	var $lastTr = $area.find("tr:last"), $newTr, $check;
	var attrs = ["reqNo","subj","docNo","hdlrUid","hdlrNm","cmplDt"];
	reqArrs.each(function(idx, relReq){
		$newTr = $lastTr.clone();<%// 숨겨진 TR 복사 %>
		$newTr.attr("id","");
		<%// checkbox 의 속성에 데이터 할당, 유니폼 적용 %>
		$check = $newTr.find("input[type='checkbox']");
		attrs.each(function(index, attr){
			$check.attr("data-"+attr, relReq[attr]);
		});
		$check.uniform();
		<%// 각 TD에 해당하는 데이터 입력 %>
		$newTr.find("td:eq(1)").html("<div class=\"ellipsis\" title=\""+relReq["docNo"]+"\"><a href=\"javascript:openReqView('"+relReq["reqNo"]+"')\">"+relReq["docNo"]+"</a></div>");
		$newTr.find("td:eq(2)").html("<div class=\"ellipsis\" title=\""+relReq["subj"]+"\"><a href=\"javascript:openReqView('"+relReq["reqNo"]+"')\">"+relReq["subj"]+"</a></div>");
		$newTr.find("td:eq(3)").html("<a href=\"javascript:viewUserPop('"+relReq["hdlrUid"]+"')\">"+relReq["hdlrNm"]+"</a>");
		$newTr.find("td:eq(4)").text(relReq["cmplDt"]==null ? '' : relReq["cmplDt"]);
		$newTr.show();
		<%// TR 삽입 %>
		$newTr.insertBefore($lastTr);
	});
	dialog.close("listReqListDialog");
}<%
// 요청 데이터 모으기 %>
function collectReqToRelReq(){
	var attrs = ["reqNo","subj","docNo","hdlrUid","hdlrNm","cmplDt"], obj, $check, returnArr=[];
	$("#relReqListArea input[id!='checkHeader']").not(":last").each(function(){
		obj = {};
		$check = $(this);
		attrs.each(function(index, attr){
			obj[attr] = $check.attr("data-"+attr);
		});
		returnArr.push(obj);
	});
	return returnArr;
}<%
// 관련요청 - 위로이동, 아래로이동 %>
function moveRelReq(direction){
	var chks = $("#relReqListArea input[type='checkbox'][id!='checkHeader']:checked:visible");
	if(chks.length>0){
		var tr, prevTr;
		if(direction=='up'){
			chks.each(function(){
				tr = $(getParentTag(this,'tr'));
				prevTr = tr.prev();
				if(prevTr.attr('id')!='titleTr' && prevTr.find("input[type='checkbox']:checked").length==0){
					tr.insertBefore(prevTr);
				}
			});
		} else if(direction=='down'){
			var tr, nextTr;
			$(chks.get().reverse()).each(function(){
				tr = $(getParentTag(this,'tr'));
				nextTr = tr.next();
				if(nextTr.is(":visible") && nextTr.find("input[type='checkbox']:checked").length==0){
					tr.insertAfter(nextTr);
				}
			});
		}
	}
}<%// [확인] 저장 %>
function applyCfrms(){
	var arr = collectReqToRelReq();
	${callback}(arr);
	dialog.close('setRelReqDialog');
}
<% // [추가] - 관련요청 %>
function openReqList(){
	dialog.open('listReqListDialog','<u:msg titleId="wh.cols.req.relReqNm.select" alt="관련요청 선택" />','./findReqListPop.do?menuId=${param.menuId}');
	dialog.onClose("setRelReqDialog", function(){ dialog.close('listReqListDialog'); });
}<% // [팝업] - 요청 상세 %>
function openReqView(reqNo){
	dialog.open('openReqDialog','<u:msg titleId="wh.cols.req" alt="요청사항" />','./viewReqPop.do?menuId=${param.menuId}&reqNo='+reqNo);
	dialog.onClose("setRelReqDialog", function(){ dialog.close('openReqDialog'); });
}
//-->
</script>
<div style="width:650px">
<u:titleArea
	outerStyle="height:350px; overflow-x:hidden; overflow-y:auto;"
	innerStyle = "NO_INNER_IDV">
<div id="relReqBtnArea" class="resetFont" style="padding:5px 10px 0px 10px;">
<c:if test="${empty isView || isView==false }">
<div class="front">
<div class="front_right">
	<table border="0" cellpadding="0" cellspacing="0">
	<tr><td class="frontico"><u:buttonIcon titleId="cm.btn.up" href="javascript:moveRelReq('up')" /></td>
		<td class="frontico"><u:buttonIcon titleId="cm.btn.down" href="javascript:moveRelReq('down')" /></td>
		<td class="frontbtn"><u:buttonS titleId="cm.btn.add" alt="추가" onclick="openReqList();" /></td>
		<td class="frontbtn"><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="deleteRelReq();" /></td>
	</tr>
	</table>
</div>
</div></c:if>
<u:listArea id="relReqListArea">
	<tr id="titleTr">
		<c:if test="${empty isView || isView==false }"><td width="5%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all'
			/>" onclick="checkAllCheckbox('relReqListArea', this.checked);" value=""/></td></c:if>
		<th width="20%" class="head_ct"><u:msg titleId="wh.cols.docNo" alt="요청번호"/></th>	
		<th class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></th>
		<th width="15%" class="head_ct"><u:msg titleId="wh.cols.hdl.pich" alt="처리 담당자" /></th>
		<th width="15%" class="head_ct"><u:msg titleId="wh.cols.req.cmplDt" alt="완료일" /></th>
	</tr>
<c:if test="${fn:length(whReqBVoList)==1}">
	<tr id="relReqNoDataTr">
		<td class="nodata" colspan="${empty isView || isView==false ? 5 : 4 }"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:forEach items="${whReqBVoList}" var="whReqBVo" varStatus="status">
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'"<c:if
		test="${status.last}"> style="display:none;" id="hiddenTr"</c:if>>
		<c:if test="${empty isView || isView==false }"><td class="bodybg_ct"><input type="checkbox" name="reqNo"<c:if
				test="${status.last}"> class="skipThese"</c:if>
			value="${whReqBVo.reqNo
			}" data-reqNo="<u:out value="${whReqBVo.reqNo}" type="value"
			/>" data-docNo="<u:out value="${whReqBVo.docNo}" type="value"
			/>" data-subj="<u:out value="${whReqBVo.subj}" type="value"
			/>" data-hdlrUid="<u:out value="${whReqBVo.hdlrUid}" type="value"
			/>" data-hdlrNm="<u:out value="${whReqBVo.hdlrNm}" type="value"
			/>" data-cmplDt="<u:out value="${whReqBVo.cmplDt}" type="date"
			/>" /></td></c:if>
		<td class="body_lt" ><div class="ellipsis" title="${whReqBVo.docNo }"><a href="javascript:openReqView('${whReqBVo.reqNo }');"><u:out value="${whReqBVo.docNo }"/></a></div></td>
		<td class="body_lt"><div class="ellipsis" title="${whReqBVo.subj }"><a href="javascript:openReqView('${whReqBVo.reqNo }');">${whReqBVo.subj }</a></div></td>
		<td class="body_ct"><c:if
			test="${not empty whReqBVo.hdlrUid}"
			><a href="javascript:viewUserPop('${whReqBVo.hdlrUid}')"><u:out
				value="${whReqBVo.hdlrNm}" type="html" /></a></c:if><c:if
			test="${empty whReqBVo.hdlrUid}"
			><u:out value="${whReqBVo.hdlrNm}" type="html" /></c:if></td>
		<td class="body_ct"><u:out value="${whReqBVo.cmplDt}" type="date" /></td>
	</tr>
</c:forEach>
</u:listArea>
</div>
</u:titleArea>
<u:buttonArea>
	<c:if test="${empty isView || isView==false }"><u:button titleId="cm.btn.confirm" alt="확인" href="javascript:applyCfrms();" /></c:if>
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</div>