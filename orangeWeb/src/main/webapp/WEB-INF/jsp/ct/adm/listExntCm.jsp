<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
//커뮤니티 평가코드 변경
function ctEvalCdCng(evalCd){
	var selectCtIds = [];
	var $checkedLength = $('input:checkbox:checked').length;
	var $ctListTbl = $("#listArea");
	if($checkedLength == '0'){
		alertMsg('cm.msg.noSelect');
	}else{
		$ctListTbl.find("tr[name='cmntEstbVo']").each(function(){
			var $selectedCtId = $(this).find("#cmntId").val();
			$(this).find("#checkFlag").each(function(){
				
				if($(this).is(":checked")||$(this).attr("checked") == "checked"){
					if($(this).attr("tr")!='headerTr'){
						selectCtIds.push($selectedCtId);
					}
				}
				
			});
		});
		
		//ct.cfrm.evalCd.change = 커뮤니티 평가 등급을 변경하시겠습니까?
		if (confirmMsg("ct.cfrm.evalCd.change")) {
			callAjax('./ajaxCtEvalCdCng.do?menuId=${menuId}', {evalCd:evalCd, selectCtIds:selectCtIds}, function(data){
				if (data.message != null) {
					alert(data.message);
				}
				if (data.result == 'ok') {
					location.href = './listExntCm.do?menuId=${menuId}';
				}
			});
		}else{
			return;
		}
	}
}


//체크박스 전체 선택
function checkAll(){
	if($("#checkFlagAll").is(":checked")){
		$("input[name='checkFlag']:checkbox").each(function(){
			if($(this).attr("disabled") != 'Y'){
				$(this).parent().attr("class", "checked");
				$(this).attr("checked","checked");
			}
		});
		
	}else{
		$("input[name='checkFlag']:checkbox").each(function(){
			if(!$(this).attr("disabled") != 'Y'){
				$(this).parent().attr("class", "");
				$(this).removeAttr("checked");
			}
		});
	}
}

function applyTooltip() {
	$('.ct_ctInfo').mouseover(function(event) {
		$('#tooltip').css('top', event.pageY - 85);
		$('#tooltip').css('left', event.pageX - 340);
		$('#tooltip').show();
		
		var $ctObj = $("#"+$(this).attr("id")).parent().parent();
		var $tooltip_title=$("#tooltip_title");
		$tooltip_title.html($ctObj.find("#cmntNm").val());
		var $tooltip_mbshScore = $("#tooltip_mbshScore");
		var $tooltip_bullScore = $("#tooltip_bullScore");
		var $tooltip_actScore = $("#tooltip_actScore");
		$tooltip_mbshScore.html($ctObj.find("#cmntMbshScore").val());
		$tooltip_bullScore.html($ctObj.find("#cmntBullScore").val());
		$tooltip_actScore.html($ctObj.find("#cmntBullActScore").val());
	});
	$('#listArea .body_ct').mouseout(function(event) {
		$('#tooltip').hide();
	});
}

$(document).ready(function() {
	setUniformCSS();
	applyTooltip();
});
//-->
</script>

<u:title titleId="ct.jsp.listExntCm.title" alt="우수커뮤니티 관리" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listExntCm.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="cols.evalGrad" alt="평가등급" /></td>
		<td><select id="schCondition" name="schCondition">
			<option value=""  <c:if test="${schCondition == '' || schCondition == null}">selected="selected" </c:if>><u:msg titleId="cm.option.all" alt="전체" /></option>
			<option value="C" <c:if test="${schCondition == 'C'}">selected="selected" </c:if>><u:msg titleId="ct.cols.norm" alt="일반" /></option>
			<option value="G" <c:if test="${schCondition == 'G'}">selected="selected" </c:if>><u:msg titleId="ct.cols.good" alt="장려" /></option>
			<option value="B" <c:if test="${schCondition == 'B'}">selected="selected" </c:if>><u:msg titleId="ct.cols.exnt" alt="우수" /></option>
			<option value="E" <c:if test="${schCondition == 'E'}">selected="selected" </c:if>><u:msg titleId="ct.cols.best" alt="최우수" /></option>
			</select></td>
		<td class="width20"></td>
		<td><select name="schCat">
				<u:option value="communityOpt" titleId="ct.cols.ctNm" alt="커뮤니티" checkValue="${param.schCat}" />
			</select></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 400px;"/></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<% // 목록 %>
<u:listArea id="listArea">
	<tr id="headerTr">
	<td width="3%" class="head_bg"><u:checkbox id="checkFlagAll" name="checkFlagAll" value="all" checked="false" onclick="javascript:checkAll();"/></td>
	<td class="head_ct"><u:msg titleId="cols.cmNm" alt="커뮤니티명" /></td>
	<td width="15%" class="head_ct"><u:msg titleId="cols.cls" alt="분류" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.mast" alt="마스터" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.stat" alt="상태" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.mbshCnt" alt="회원수" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.evalScre" alt="평가점수" /></td>
	</tr>
	<c:forEach var="ctEstbVo" items="${ctEstbList}" varStatus="status">
		<tr id="cmntEstbVo" name="cmntEstbVo">
			<td class="bodybg_ct"><u:checkbox id="checkFlag" name="checkFlag" value="${status.count}" checked="false" /></td>
			<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
				<tr>
				<td class="body_lt"><u:out value="${ctEstbVo.ctNm}"/>
					<input id="cmntId" name="cmntId" type="hidden" value="${ctEstbVo.ctId}" />
				</td>
				<td class="listicon_lt">
					<c:choose>
						<c:when test="${ctEstbVo.ctEvalCd == 'B'}">
							<u:icoBest icon="best" />
						</c:when>
						<c:when test="${ctEstbVo.ctEvalCd == 'E'}">
							<u:icoBest icon="excellent" />
						</c:when>
						<c:when test="${ctEstbVo.ctEvalCd == 'G'}">
							<u:icoBest icon="good" />
						</c:when>
						<c:otherwise>
							<u:icoBest icon="" />
						</c:otherwise>
					</c:choose>
					<c:if test="${ctEstbVo.recmdYn == 'Y'}">
					<u:icon type="recommend"/>
				</c:if>
				</td>
				</tr>
				</tbody></table></td>
			<td class="body_ct">${ctEstbVo.catNm}</td>
			<td class="body_ct"><a href="javascript:viewUserPop('${ctEstbVo.mastUid}');">${ctEstbVo.mastNm}</a></td>
			<td class="body_ct">
				<c:choose>
					<c:when test="${ctEstbVo.ctActStat == 'S'}">
						<u:msg titleId="ct.cols.ready" alt="준비중" />
					</c:when>
					<c:when test="${ctEstbVo.ctActStat == 'A'}">
						<u:msg titleId="ct.cols.act" alt="활동중" />
					</c:when>
					<c:when test="${ctEstbVo.ctActStat == 'C'}">
						<u:msg titleId="ct.cols.close" alt="폐쇄" />
					</c:when>
				</c:choose>
			</td>
			<td class="body_ct">${ctEstbVo.mbshCnt}</td>
			<td class="body_ct" >
				<span class="ct_ctInfo" id="ct_ctInfo${status.count}">
					${ctEstbVo.evalScore}
					<input id="cmntNm" name="cmntNm" type="hidden" value="${ctEstbVo.ctNm}"/>
					<input id="cmntMbshScore" name="cmntMbshScore" type="hidden" value="${ctEstbVo.mbshCntScore}"/>
					<input id="cmntBullScore" name="cmntBullScore" type="hidden" value="${ctEstbVo.bullCntScore}"/>
					<input id="cmntBullActScore" name="cmntBullActScore" type="hidden" value="${ctEstbVo.bullActUserCntScore}"/>
				</span>
			</td>
		</tr>
	</c:forEach>
	<c:if test="${fn:length(ctEstbList) == 0}">
			<tr>
				<td class="nodata" colspan="7"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
	</c:if>

</u:listArea>

<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:msg titleId="cm.msg.preparing" var="msg" alt="준비중.." />
	<u:button titleId="ct.btn.norm" alt="일반" href="javascript:ctEvalCdCng('C');" auth="W" />
	<u:button titleId="ct.btn.good" alt="장려" href="javascript:ctEvalCdCng('G');" auth="W" />
	<u:button titleId="ct.btn.exnt" alt="우수" href="javascript:ctEvalCdCng('E');" auth="W" />
	<u:button titleId="ct.btn.best" alt="최우수" href="javascript:ctEvalCdCng('B');" auth="W" />
</u:buttonArea>

<% // TOOLTIP %>
<div id="tooltip" style="position:absolute; top:315px; left:90px; z-index:1; display: none;">
<div class="tooltip">
	<div class="tooltip_arrow"><img src="${_ctx}/images/${_skin}/arrow_lt.png"></div>
	<div class="tooltip_body">
		<div class="tooltip_text">
		<ul>
		<li><strong><strong  id="tooltip_title"></strong></strong></li>
		<li class="blank_s2"></li>
		<li class="tooltip_line"></li>
		<li class="blank_s5"></li>
		<li>
			<u:msg titleId="ct.msg.mbshScore" alt="회원수 누진 점수"/> : <span id="tooltip_mbshScore"></span><u:msg titleId="ct.msg.score" alt="점"/><br/>
			<u:msg titleId="ct.msg.bullScore" alt="게시 활동회원 점수"/> : <span id="tooltip_bullScore"></span><u:msg titleId="ct.msg.score" alt="점"/><br/>
			<u:msg titleId="ct.msg.bullActMbshScore" alt="게시물 등록 점수"/>: <span id="tooltip_actScore"></span><u:msg titleId="ct.msg.score" alt="점"/><br/>
		</ul>
		</div>
	</div>
</div>
</div>
