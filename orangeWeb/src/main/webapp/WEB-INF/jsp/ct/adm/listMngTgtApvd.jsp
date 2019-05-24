<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--


//커뮤니티 미리보기
function applyTooltip(){
	$('.ct_ctInfo').mouseover(function(event){
		$('#tooltip').css('top', event.pageY - 85);
		$('#tooltip').css('left', event.pageX - 230);
		$('#tooltip').show();
		
		var $ctObj = $("#"+$(this).attr("id")).parent().parent();
		var $tooltip_title=$("#tooltip_title");
		$tooltip_title.html($ctObj.find("#mngTgtCtNm").val());
		var $tooltip_basicInfo = $("#tooltip_basicInfo");
 		$tooltip_basicInfo.html($ctObj.find("#mngTgtRegDt").val()+','+$ctObj.find("#mngTgtExtnOpenYn").val()+','+$ctObj.find("#mngTgtJoinMet").val());
 		var $tooltip_ctItro = $("#tooltip_ctItro");
 		$tooltip_ctItro.html("<u:msg titleId="ct.cols.introCm" alt="커뮤니티 소개" /> : " +$ctObj.find("#mngTgtCtItro").val());
	});
	$('.ct_ctInfo').mouseout(function(event){
		$('#tooltip').hide();     
	});
	
}

//관리대상 승인/미승인
function ctMngTgtAppr(apprYn){
	var $selectCtMngTgt = $("#selectCtMngTgtList");
	var selectCtMngTgtCtIds = [];
	var $checkedLength = $('input:checkbox:checked').length;
	var $ctMngTgtListTbl = $("#listArea");
	
	if($checkedLength == '0'){
		if(apprYn == 'Y'){
			alert("<u:msg titleId="ct.msg.selectApprCm" alt="승인할 커뮤니티를 선택하십시오." />");
		}else{
			alert("<u:msg titleId="ct.msg.selectNotApprCm" alt="미승인할 커뮤니티를 선택하십시오." />");
		}
		
	}else{
		$ctMngTgtListTbl.find("tr[name='cmntMngTgtVo']").each(function(){
			var $selectedMngTgtCtId = $(this).find("#mngTgtCtId").val();
			$(this).find("#checkFlag").each(function(){
				if($(this).is(":checked")||$(this).attr("checked") == "checked"){
					if($(this).attr("tr")!='headerTr'){
						selectCtMngTgtCtIds.push($selectedMngTgtCtId);
					}
				}
			});

		});
		if(apprYn == 'Y'){
			if(confirmMsg("ct.cfrm.appr")){
				callAjax('./setMngTgtAppr.do?menuId=${menuId}', {selectCtMngTgtCtIds:selectCtMngTgtCtIds, signal:apprYn}, function(data){
					if (data.message != null) {
						alert(data.message);
					}
					if (data.result == 'ok') {
						location.href = './listMngTgtApvd.do?menuId=${menuId}';
					}
				});
			}
		}else{
			if(confirmMsg("ct.cfrm.rjt")){
				callAjax('./setMngTgtAppr.do?menuId=${menuId}', {selectCtMngTgtCtIds:selectCtMngTgtCtIds, signal:apprYn}, function(data){
					if (data.message != null) {
						alert(data.message);
					}
					if (data.result == 'ok') {
						location.href = './listMngTgtApvd.do?menuId=${menuId}';
					}
				});
			}
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

/*
function viewCm(fncUid,ctId) {
	location.href = "../viewCm.do?menuId=" + fncUid + "&ctId=" + ctId;
}
*/

$(document).ready(function() {
	setUniformCSS();
	applyTooltip();
});
//-->
</script>

<u:title titleId="ct.jsp.listMngTgtApvd.title" alt="관리대상 승인" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listMngTgtApvd.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">
				<u:option value="communityOpt" titleId="ct.cols.ctNm" alt="커뮤니티" checkValue="${param.schCat}" />
				<u:option value="masterOpt" titleId="ct.cols.mastNm" alt="마스터" checkValue="${param.schCat}" />
			</select></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 400px;" onkeydown="if (event.keyCode == 13) searchForm.submit();"/></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<form id = "selectCtMngTgtList">
</form>

<% // 목록 %>
<div id="listArea" class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="width:100%;table-layout:fixed;">
	<tr id="headerTr">
	<td width="3%" class="head_bg"><u:checkbox id="checkFlagAll" name="checkFlagAll" value="all" checked="false" onclick="javascript:checkAll();" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.cls" alt="분류" /></td>
	<td class="head_ct"><u:msg titleId="ct.cols.cm" alt="커뮤니티" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.mast" alt="마스터" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.mbshCnt" alt="회원수" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.createDt" alt="생성일시" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.mngTgtYn" alt="관리대상여부" /></td>
	</tr>
	
	<c:forEach var="ctMngTgtVo" items="${ctMngTgtMapList}" varStatus="status">
		<tr id="cmntMngTgtVo" name="cmntMngTgtVo" onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
		<td class="bodybg_ct"><u:checkbox id="checkFlag" name="checkFlag" value="${status.count}" checked="false" /></td>
		<td class="body_ct">${ctMngTgtVo.catNm}</td>
		<td class="body_lt">
			<span class="ct_ctInfo" id="ct_ctInfo${status.count}">
					<%-- <a href="javascript:viewCm('${ctMngTgtVo.ctFncUid}','${ctMngTgtVo.ctId}');"> --%>
						<u:out value="${ctMngTgtVo.ctNm}"/></a>
					<input id="mngTgtCtId" name="mngTgtCtId" type="hidden" value="${ctMngTgtVo.ctId}" />
					<input id="mngTgtCtNm" name="mngTgtCtNm" type="hidden" value="${ctMngTgtVo.ctNm}" />
					<c:if test="${ctMngTgtVo.extnOpenYn == 'Y'}">
						<input id="mngTgtExtnOpenYn" name="mngTgtExtnOpenYn" type="hidden" value="공개" />
					</c:if>
					<c:if test="${ctMngTgtVo.extnOpenYn == 'N'}">
						<input id="mngTgtExtnOpenYn" name="mngTgtExtnOpenYn" type="hidden" value="비공개" />
					</c:if>
					<c:if test="${ctMngTgtVo.joinMet == '1'}">
						<input id="mngTgtJoinMet" name="mngTgtJoinMet" type="hidden" value="즉시 가입" />
					</c:if>
					<c:if test="${ctMngTgtVo.joinMet == '2'}">
						<input id="mngTgtJoinMet" name="mngTgtJoinMet" type="hidden" value="마스터 승인 후 가입" />
					</c:if>
					<input id="mngTgtCtItro" name="mngTgtCtItro" type="hidden" value="${ctMngTgtVo.ctItro}" />
			</span>
		</td>
		<td class="body_ct"><a href="javascript:viewUserPop('${ctMngTgtVo.mastUid}');">${ctMngTgtVo.mastNm}</a></td>
		<td class="body_ct">${ctMngTgtVo.mbshCnt}</td>
		<td class="body_ct">
			<fmt:parseDate var="dateTempParse" value="${ctMngTgtVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
			<fmt:formatDate var="regDt" value="${dateTempParse}" pattern="yyyy-MM-dd"/>
			<c:out value="${regDt}" />
			<c:set var="setRegDt" value="${regDt}" />
			<input id="mngTgtRegDt" name="mngTgtRegDt" type="hidden" value="${setRegDt}" />
		</td>
		<td class="body_ct">
			<c:choose>
				<c:when test="${ctMngTgtVo.mngTgtYn eq 'N'}"><u:msg titleId="ct.cols.yes" alt="Yes" /> -> <u:msg titleId="ct.cols.no" alt="No" /></c:when>
				<c:otherwise><u:msg titleId="ct.cols.no" alt="No" /> -> <u:msg titleId="ct.cols.yes" alt="Yes" /></c:otherwise>
			</c:choose>
		</td>
	</c:forEach>
	<c:if test="${fn: length(ctMngTgtMapList) == 0}">
		<tr>
			<td class="nodata" colspan="7"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	
	</c:if>
	</table>
</div>
<u:blank />
<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:msg titleId="cm.msg.preparing" var="msg" alt="준비중.." />
	<u:button titleId="cm.btn.apvd" alt="승인" auth="W" onclick="javascript:ctMngTgtAppr('Y');" />
	<u:button titleId="cm.btn.napvd" alt="미승인" auth="W" onclick="javascript:ctMngTgtAppr('N');" />
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
				 <span id="tooltip_basicInfo"></span><br>
				 <span id="tooltip_ctItro"></span><br></li>
					
				</ul>
			</div>
		</div>
	</div>
</div>