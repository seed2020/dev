<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
// 반려사유 보기(툴팁)
function applyTooltip(){
	$('.ct_rjtOp').mouseover(function(event){
		$('#tooltip').css('top', event.pageY - 85);
		$('#tooltip').css('left', event.pageX - 230);
		$('#tooltip').show();
		
		var $ctObj = $("#"+$(this).attr("id")).parent().parent();
		var $tooltip_title=$("#tooltip_title");
		$tooltip_title.html($ctObj.find("#reqsCtNm").val());
		var $tooltip_rjtOpinion = $("#tooltip_rjtOpinion");
 		$tooltip_rjtOpinion.html("<u:msg titleId="cols.rjtOpin" alt="반려의견"  /> : " + 	$ctObj.find("#reqsRjtOp").val());
	});
	$('.ct_rjtOp').mouseout(function(event){
		$('#tooltip').hide();     
	});
	
}

//신청중인 커뮤니티 삭제
function reqsCtDel(){
	var $delReqsCtForm = $("#delReqsCtListForm");
	var delReqsCtListInfo = [];
	var $checkedLength = $('input:checkbox:checked').length;
	var $reqsCtListTbl = $("#listArea");
	
	if($checkedLength == '0'){
		alert("<u:msg titleId="ct.msg.selectDelCm" alt="삭제할 커뮤니티를 선택해주시기 바랍니다."  />");
	}else{
		if (confirmMsg("cm.cfrm.del")){
			$reqsCtListTbl.find("tr[name='reqsCmntVo']").each(function(){
				var $selectedReqsCtId = $(this).find("#reqsCtId").val();
				$(this).find("#checkFlag").each(function(){
					if($(this).is(":checked")||$(this).attr("checked") == "checked"){
						if($(this).attr("tr")!='headerTr'){
							delReqsCtListInfo.push("<input id='delReqsCtId' name='delReqsCtId' type='hidden' style='width:80px' value='"+$selectedReqsCtId+"' />");
						}
					}
				});

			});
			delReqsCtListInfo.push("<input id='signal' name='signal' type='hidden' style='width:80px' value='del'/>");
			$delReqsCtForm.html(delReqsCtListInfo.join(''));
			$delReqsCtForm.attr('method', 'post');
			$delReqsCtForm.attr('action','./listCmReqs.do?menuId=${menuId}');
			$delReqsCtForm.submit();
		}else{
		    return;
		}

	}
	
	
		
	
}

//체크박스 전체 선택
function checkAll(){
	if($("#checkFlagAll").is(":checked")){
		$("input[name='checkFlag']:checkbox").each(function(){
			$(this).parent().attr("class", "checked");
			$(this).attr("checked","checked");
		});
		
	}else{
		$("input[name='checkFlag']:checkbox").each(function(){
			$(this).parent().attr("class", "");
			$(this).removeAttr("checked");
		});
	}
}

$(document).ready(function() {
	setUniformCSS();
	applyTooltip();
	
});
//-->
</script>

<u:title titleId="ct.jsp.listCmReqs.title" alt="신청중인 커뮤니티" menuNameFirst="true" />

<form id = "delReqsCtListForm">
</form>

<% // 목록 %>
<form id="reqsCtForm">
<u:listArea id="listArea">
	<tr id="headerTr">
	<td width="3%" class="head_bg"><u:checkbox id="checkFlagAll" name="checkFlagAll" value="all" checked="false" onclick="javascript:checkAll();"/></td>
	<td class="head_ct"><u:msg titleId="ct.cols.cm" alt="커뮤니티" /></td>
	<td width="35%" class="head_ct"><u:msg titleId="cols.rjtOpin" alt="반려의견" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.reqsDt" alt="신청일시" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.stat" alt="상태" /></td>
	
	</tr>
	
	<c:forEach var="reqsCtVo" items="${reqsCtMapList}" varStatus="status">
		<tr id="reqsCmntVo" name="reqsCmntVo" onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
			<td class="bodybg_ct"><u:checkbox id="checkFlag" name="checkFlag" value="${status.count}" checked="false" /></td>
			<td class="body_lt">
				<a href="./setCm.do?menuId=${menuId}&fnc=mod&ctId=${reqsCtVo.ctId}">${reqsCtVo.ctNm}</a>
				<input id="reqsCtId" name="reqsCtId" type="hidden" value="${reqsCtVo.ctId}"/>
				<input id="reqsCtNm" name="reqsCtNm" type="hidden" value="${reqsCtVo.ctNm}"/>
			</td>
			<td class="body_lt">
				<span class="ct_rjtOp" id="ct_rjtOp${status.count}">
					<u:out value="${reqsCtVo.rjtOpinCont}" maxLength="30"/>
					<input id="reqsRjtOp" name="reqsRjtOp" type="hidden" value="${reqsCtVo.rjtOpinCont}"/>
				</span>
			</td>
			<td class="body_ct">
				<fmt:parseDate var="dateTempParse" value="${reqsCtVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
			</td>
			<c:choose>
				<c:when test="${reqsCtVo.ctStat == 'S'}">
					<c:if test="${reqsCtVo.ctActStat == 'S'}" >
						<td class="body_ct"><u:msg titleId="ct.cols.stat.req" alt="승인대기" /></td>
					</c:if>
					<c:if test="${reqsCtVo.ctActStat == 'N'}">
						<td class="body_ct"><u:msg titleId="ct.cols.stat.nSav" alt="작성중" /></td>
					</c:if>
				</c:when>
				<c:otherwise>
					<td class="body_ct"><u:msg titleId="ct.cols.stat.rjt" alt="반려" /></td>
				</c:otherwise>
			</c:choose>
		</tr>
	</c:forEach>
	
	<c:if test="${fn:length(reqsCtMapList) == 0}">
			<tr>
				<td class="nodata" colspan="9"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
	</c:if>
	
</u:listArea>
</form>

<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:msg titleId="cm.msg.noSelect" alt="선택한 항목이 없습니다." var="msg" />
	<u:button titleId="cm.btn.del" alt="삭제" href="javascript:reqsCtDel();"/>
</u:buttonArea>


<% // TOOLTIP %>
<div id="tooltip" style="position:absolute; top:315px; left:90px; z-index:1; display: none;">
	<div class="tooltip">
		<div class="tooltip_arrow"><img src="${_ctx}/images/${_skin}/arrow_lt.png"></div>
		<div class="tooltip_body">
			<div class="tooltip_text" >
			
				<ul>
				<li ><strong  id="tooltip_title"></strong></li>
				<li class="blank_s2" ></li>
				<li class="tooltip_line"></li>
				<li class="blank_s5"></li>
				<li>
				 <span id="tooltip_rjtOpinion"></span><br>
				 <span id="tooltip_content"></span><br></li>
					
				</ul>
			</div>
		</div>
	</div>
</div>