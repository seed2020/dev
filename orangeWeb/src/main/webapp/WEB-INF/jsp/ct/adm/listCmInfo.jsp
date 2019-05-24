<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%//추천취소%>
function fnRecmdCancel(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) {
		var selArr = selRowInArr(arr);
		if(selArr != null){
			$("#selCtId").val(selArr.join(','));
			if(confirmMsg("ct.cfrm.recmdCancel")) {
				var $form = $('#recmdCancelForm');
				$form.attr('method','post');
				$form.attr('action','./transRecmdCancelCt.do?menuId=${menuId}');
				$form.attr('target','dataframe');
				$form[0].submit();
			}			
		}
	}
};

<%// 선택삭제%>
function fnDelete(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) {
		var selArr = selRowInArr(arr);
		if(selArr != null){
			$("#delCtId").val(selArr.join(','));
			if(confirmMsg("cm.cfrm.del")) {
				var $form = $('#deleteForm');
				$form.attr('method','post');
				$form.attr('action','./transDelCt.do?menuId=${menuId}');
				$form.attr('target','dataframe');
				$form[0].submit();
			}			
		}
	}
};

<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(noSelectMsg){
	var arr=[], id, obj;
	$("#listArea tbody:first input[type='checkbox']:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
};

<%// 삭제 - 배열에 담긴 목록%>
function selRowInArr(rowArr){
	var selArr = [], $selId;
	//if(delVa!='') selArr.push(delVa);
	for(var i=0;i<rowArr.length;i++){
		$selId = $(rowArr[i]).find("input[name='cmntId']");
		if($selId.val()!=''){
			selArr.push($selId.val());
		}
	}
	if(selArr.length == 0 ) return null;
	return selArr;
};

function closeOpPop(){
	
}


//커뮤니티 폐쇄
function ctClose(){
	var $checkedLength = $('input:checkbox:checked').length;
	if($checkedLength == '0'){
		//ct.msg.noSelectCt = 커뮤니티를 선택해주시기 바랍니다.
		alertMsg("ct.msg.noSelectCt");
	}else{
		dialog.open('setCloseOpPop', '<u:msg titleId="ct.cols.closeOp" alt="폐쇄사유" />','./setCloseOpPop.do?menuId=${menuId}');
		
	}
}

//커뮤니티 추천
function ctRecomende(){
	var selectCtIds = [];
	var $checkedLength = $('input:checkbox:checked').length;
	var $ctListTbl = $("#listArea");
	if($checkedLength == '0'){
		//ct.msg.noSelectCt = 커뮤니티를 선택해주시기 바랍니다.
		alertMsg("ct.msg.noSelectCt");
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
		
		// ct.cfrm.recomende = 추천하시겠습니까?
		if (confirmMsg("ct.cfrm.recomende")) {
			callAjax('./ajaxCtRecomende.do?menuId=${menuId}', {selectCtIds:selectCtIds}, function(data){
				if (data.message != null) {
					alert(data.message);
				}
				if (data.result == 'ok') {
					location.href = './listCmInfo.do?menuId=${menuId}';
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

function setMast(ctFncUid, ctId, mastUid) {
	dialog.open('setMastPop','<u:msg titleId="ct.cols.mastChn" alt="마스터변경" />','./setMastPop.do?menuId='+ctFncUid+'&ctId='+ctId+'&mastUid='+mastUid+'&mnuId=${menuId}');
}

function viewCm(ctFncUid, ctId) {
	location.href = "../viewCm.do?menuId="+ctFncUid+"&ctId="+ctId+"&prevMenuId=${menuId}";
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="ct.jsp.listCmInfo.title" alt="커뮤니티 정보변경" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form id="searchForm" name="searchForm" action="./listCmInfo.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select id="schCondition" name="schCondition">
			<option value="" <c:if test="${schCondition == '' || schCondition == null}">selected="selected" </c:if>><u:msg titleId="ct.cols.act" alt="활동중"/></option>
			<option value="Y" <c:if test="${schCondition == 'Y'}">selected="selected" </c:if>><u:msg titleId="ct.cols.recomend" alt="추천"/></option>
			<%-- <option value="N" <c:if test="${schCondition == 'N'}">selected="selected" </c:if>><u:msg titleId="ct.cols.normal" alt="일반"/></option> --%>
			<option value="C" <c:if test="${schCondition == 'C'}">selected="selected" </c:if>><u:msg titleId="ct.cols.close" alt="폐쇄"/></option>
			</select></td>
		<td class="width20"></td>
		<td><select name="schCat">
				<u:option value="communityOpt" titleId="ct.cols.ctNm" alt="커뮤니티" checkValue="${param.schCat}" />
				<u:option value="masterOpt" titleId="ct.cols.mastNm" alt="마스터" checkValue="${param.schCat}" />
			</select></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 400px;"/></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<div class="front">
<div class="front_left">
	<table border="0" cellpadding="0" cellspacing="0"><tbody>
	<tr>
	<td class="color_txt">※ <u:msg titleId="ct.jsp.listCmInfo.tx01" alt="마스터(변경) 컬럼의 이름을 클릭하면 마스터를 변경할 수 있습니다." /></td>
	</tr>
	</tbody></table>
</div>
</div>

<% // 목록 %>
<u:listArea id="listArea">
	<tr id="headerTr">
	<td width="3%" class="head_bg"><u:checkbox id="checkFlagAll" name="checkFlagAll" value="all" checked="false" onclick="checkAllCheckbox('listArea', this.checked);"/></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.cls" alt="분류" /></td>
	<td class="head_ct"><u:msg titleId="ct.cols.cm" alt="커뮤니티" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="ct.cols.mastChn" alt="마스터(변경)" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.mbshCnt" alt="회원수" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.createDt" alt="생성일시" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.stat" alt="상태" /></td>
	</tr>
	
	<c:forEach var="ctEstbVo" items="${ctEstbList}" varStatus="status">
		<tr id="cmntEstbVo" name="cmntEstbVo">
		<td class="bodybg_ct"><u:checkbox id="checkFlag" name="checkFlag" value="${status.count}" checked="false" /></td>
		<td class="body_ct">${ctEstbVo.catNm}</td>
		<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			<td class="body_lt"><a href="javascript:viewCm('${ctEstbVo.ctFncUid}','${ctEstbVo.ctId}');"><u:out value="${ctEstbVo.ctNm}"/></a>
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
		<td class="body_ct"><a href="javascript:setMast('${ctEstbVo.ctFncUid}','${ctEstbVo.ctId}', '${ctEstbVo.mastUid}');">${ctEstbVo.mastNm}</a></td>
		<td class="body_ct">${ctEstbVo.mbshCnt}</td>
		<td class="body_ct">
			<fmt:parseDate var="dateTempParse" value="${ctEstbVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
			<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
		</td>
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
		</tr>
	
	</c:forEach>
	<c:if test="${fn:length(ctEstbList) == 0}">
			<tr>
				<td class="nodata" colspan="9"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
	</c:if>

</u:listArea>

<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea noBottomBlank="true">
	<c:if test="${schCondition eq 'C' }"><u:button titleId="cm.btn.del" alt="삭제" onclick="fnDelete();" auth="A"/></c:if>
	<c:if test="${empty schCondition || schCondition eq 'A' }">
		<u:button titleId="ct.btn.recmd" alt="추천" onclick="javascript:ctRecomende();" auth="A"/>
		<u:button titleId="ct.btn.closed" alt="폐쇄" onclick="javascript:ctClose();" auth="A"/>
	</c:if>
	<c:if test="${schCondition eq 'Y' }"><u:button titleId="ct.btn.recmdCancel" alt="추천취소" onclick="fnRecmdCancel();" auth="A"/></c:if>
<%-- 	<u:button titleId="ct.btn.close" alt="폐쇄" auth="W" onclick="javascript:ctClose();" /> --%>
</u:buttonArea>
<form id="deleteForm" name="deleteForm" >
	<u:input type="hidden" id="menuId" value="${menuId }"/>
	<u:input type="hidden" id="delCtId" />
	<u:input type="hidden" id="listPage" value="./listCmInfo.do?menuId=${menuId }&schCondition=${param.schCondition }&schCat=${param.schCat }&schWord=${param.schWord }"/>
</form>
<form id="recmdCancelForm" name="recmdCancelForm" >
	<u:input type="hidden" id="menuId" value="${menuId }"/>
	<u:input type="hidden" id="selCtId" />
	<u:input type="hidden" id="listPage" value="./listCmInfo.do?menuId=${menuId }&schCondition=${param.schCondition }&schCat=${param.schCat }&schWord=${param.schWord }"/>
</form>


