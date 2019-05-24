<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="params" excludes="menuId"/>
<u:params var="paramsForList" excludes="menuId, typ"/>
<script type="text/javascript">
<!--
<% // 엑셀 파일 다운로드 %>
function excelDownFile() {
	var $form = $('#excelForm');
	$form.attr('method','post');
	$form.attr('action','./excelDownLoad.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form[0].submit();
};

function getTyp(){
	return $('#typ').val() == '' ? 'I' : $('#typ').val();
};

<% // 순서 저장 %>
function moveSave() {
	var arr=[], id, obj;
	$("#listContentsArea tbody:first input[type='checkbox']").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push($(this).val());
	});
	if(arr.length==0){
		alertMsg('cm.msg.nodata.toSave'); // cm.msg.nodata.toSave=저장할 데이터가 없습니다.
		return;
	}else{
		callAjax('./transRescMngMoveSaveAjx.do?menuId=${menuId}', {rescList: arr, rescKndId:'${param.rescKndId}'}, function (data) {
			if (data.message != null) {
				alert(data.message);
			}
		});
	}
};
<%// [순서조절:위로,아래로] 서버에 저장하지 않고 화면상에서만 순서 조정함 %>
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
}
<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(noSelectMsg){
	var arr=[], id, obj;
	$("#listContentsArea tbody:first input[type='checkbox']:checked").each(function(){
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

$(document).ready(function() {
	var display='none';
	<c:if test="${param.typ eq 'L' && !empty param.rescKndId}">display='show';</c:if>
	parent.loadMoveBtn(display);
setUniformCSS();
});
//-->
</script>

<% // 검색영역 %>
<div id="searchArea" style="padding:10px;">
<form name="searchForm" action="./listRescMngFrm.do" >
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="typ" value="${param.typ}" />

<div class="front">
	<div class="front_left">
		<table border="0" cellpadding="0" cellspacing="0">
			<tbody>
			<tr>
				<td class="fronttit"><u:msg titleId="cols.rescKnd" alt="자원종류" /></td>
				<td class="frontinput">
					<select id="rescKndId" name="rescKndId" onchange="searchForm.submit();">
						<u:option value="" titleId="cm.option.all" alt="전체선택"/>
						<c:forEach items="${wrRescKndBVoList}" var="list" varStatus="status">
							<u:option value="${list.rescKndId}" title="${list.kndNm}" selected="${param.rescKndId == list.rescKndId}"/>
						</c:forEach>
					</select>
				</td>
				<td class="width20"></td>
				<td class="fronttit"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
				<td class="frontinput">
					<u:checkArea>
						<u:radio name="useYn" value="" titleId="cm.option.all" alt="전체" checkValue="${param.useYn }" checked="${empty param.useYn }" inputClass="bodybg_lt" onclick="searchForm.submit();"/>
						<u:radio name="useYn" value="Y" titleId="cm.option.use" alt="사용" checkValue="${param.useYn }"  inputClass="bodybg_lt" onclick="searchForm.submit();"/>
						<u:radio name="useYn" value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${param.useYn }" inputClass="bodybg_lt" onclick="searchForm.submit();"/>
					</u:checkArea>
				</td>
			</tr>
			</tbody>
		</table>
	</div>
	<div class="front_right">
		<table border="0" cellpadding="0" cellspacing="0">
			<tbody>
			<tr>
				<td class="frontbtn">
				<c:choose>
					<c:when test="${param.typ == 'L'}"><u:buttonS href="./listRescMngFrm.do?menuId=${menuId }&typ=I&${paramsForList }" titleId="wr.btn.imgView" alt="이미지로보기" /></c:when>
					<c:otherwise><u:buttonS href="./listRescMngFrm.do?menuId=${menuId }&typ=L&${paramsForList }" titleId="wr.btn.txtView" alt="텍스트로보기" /></c:otherwise>
				</c:choose>
				</td>
			</tr>
			</tbody>
		</table>
	</div>
</div>
</form>
</div>
<% // 목록1 %>
<div id="listContentsArea" style="padding:10px;">
<c:choose>
	<c:when test="${param.typ == 'L'}"><% // 목록1 %>
		<div class="listarea">
			<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
			<colgroup><col width="25px"/><col width="22%"/><col width="22%"/><col width="*"/><col width="10%"/><col width="15%"/></colgroup>
			<tr id="headerTr">
				<td class="head_ct">&nbsp;</td>
				<td class="head_ct"><u:msg titleId="cols.rescKnd" alt="자원종류" /></td>
				<td class="head_ct"><u:msg titleId="cols.rescNm" alt="자원명" /></td>
				<td class="head_ct"><u:msg titleId="cols.rescLoc" alt="자원위치" /></td>
				<td class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
				<td class="head_ct"><u:msg titleId="cols.regDt" alt="등록일" /></td>
			</tr>
			<c:choose>
				<c:when test="${!empty wrRescMngBVoList}">
					<c:forEach var="list" items="${wrRescMngBVoList}" varStatus="status">
						<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
							<td class="bodybg_ct"><input type="checkbox" value="${list.rescMngId }"/></td>
							<td class="body_ct"><div class="ellipsis" title="${list.kndNm }">${list.kndNm }</div></td>
							<td class="body_ct"><div class="ellipsis" title="${list.rescNm }"><a href="javascript:;" onclick="parent.viewRescMngPop('${list.rescMngId}','${params }');">${list.rescNm }</a></div></td>
							<td class="body_lt"><div class="ellipsis" title="${list.rescLoc }">${list.rescLoc }</div></td>
							<td class="body_ct"><u:msg titleId="cm.option.${list.useYn eq 'Y' ? 'use' : 'notUse' }" alt="사용여부"/></td>
							<td class="body_ct"><u:out value="${list.regDt }" type="date"/></td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr>
						<td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
					</tr>
				</c:otherwise>
			</c:choose>
			</table>
		</div>
	</c:when>
	<c:otherwise><% // 목록2 %>
		<c:choose>
			<c:when test="${!empty wrRescMngBVoList}">
				<u:titleArea outerStyle="overflow:hidden;" innerStyle="padding:0 0 0 10px;" >
					<c:forEach var="list" items="${wrRescMngBVoList}" varStatus="status">
						<c:set var="wrRescImgDVo" value="${list.wrRescImgDVo}" />
						<c:set var="maxWdth" value="100" />
						<c:set var="maxHght" value="100" />
						<u:set test="${wrRescImgDVo != null && wrRescImgDVo.imgWdth <= maxWdth}" var="imgWdth" value="${wrRescImgDVo.imgWdth}" elseValue="${maxWdth}" />
						<u:set test="${wrRescImgDVo != null && wrRescImgDVo.imgHght <= maxHght}" var="imgHght" value="${wrRescImgDVo.imgHght}" elseValue="${maxHght}" />
						<u:set test="${wrRescImgDVo.imgWdth < wrRescImgDVo.imgHght}" var="imgWdthHgth" value="height='${imgHght}'" elseValue="width='${imgWdth}'" />
		
						<div class="listarea" style="float:left; width:23.6%; padding:8px 8px 0 0;height:180px;">
							<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
								<tbody>
								<tr>
									<td class="photo_ct"><a href="javascript:;" onclick="parent.viewRescMngPop('${list.rescMngId}','${params }');"><img src="${_cxPth}${list.wrRescImgDVo.imgPath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' ${imgWdthHgth }></a></td>
								</tr>
								<tr>
									<td class="head_ct">
										<div class="ellipsis" title="${list.rescNm }"><a href="javascript:;" onclick="parent.viewRescMngPop('${list.rescMngId}','${params }');">${list.rescNm }</a></div>
										<div class="ellipsis" title="${list.kndNm }" style="font-weight:bold;color:${list.bgcolCd};">${list.kndNm }</div>
									</td>
								</tr>
								</tbody>
							</table>
						</div>
					</c:forEach>
				</u:titleArea>
			</c:when>
			<c:otherwise>
				<u:listArea id="listArea">
					<tr>
					<td class="nodata" ><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
					</tr>
				</u:listArea>
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>
<%-- <u:blank />
<u:pagination /> --%>
<%-- <u:blank />
<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.reg" alt="등록" onclick="parent.setRescPop(null,'${params }');" auth="R" />
</u:buttonArea> --%>
</div>
<form id="excelForm">
	<c:forEach items="${paramEntryList}" var="entry" varStatus="status">
		<u:input type="hidden" id="${entry.key}" value="${entry.value}" />
	</c:forEach>
	<u:input type="hidden" id="listPage" value="${listPage}" />
</form>

