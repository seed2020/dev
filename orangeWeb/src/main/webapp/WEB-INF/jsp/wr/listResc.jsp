<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<% // 회사변경 %>
function chnComp(obj){
	var $form = $('#searchForm');	
	$form.find("input[name='paramCompId']").remove();
	if(obj.value!='')
		$form.appendHidden({name:'paramCompId',value:obj.value});
	$form.find("#rescKndId").remove();
	$form[0].submit();
}
<% // 엑셀 파일 다운로드 %>
function excelDownFile() {
	var $form = $('#excelForm');
	$form.attr('method','post');
	$form.attr('action','./excelDownLoad.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form[0].submit();
};
<% // 상세보기 %>
function viewRescMngPop(rescMngId) {
	dialog.open('setRescPop', '<u:msg titleId="wb.jsp.viewRescMngPop.title" alt="자원상세보기" />', './viewRescPop.do?${params}&rescMngId='+rescMngId);
};
<% // 등록 수정 팝업 %>
function setRezvPop(rezvId , obj) {
	var url = './setRezvPop.do?${params}&listPage=${listPage}';
	
	dialog.open('setRezvPop','<u:msg titleId="wr.jsp.setRezv.title" alt="자원예약"/>',url);
	dialog.onClose("setRezvPop", function(){ editor('cont').clean(); unloadEvent.removeEditor('cont'); });
};

$(document).ready(function() {
setUniformCSS();
});
//-->
</script>

<div class="front notPrint">
	<div class="front_left">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td style="padding:3px 4px 0 0px"><u:title titleId="wr.jsp.listResc.title" alt="자원현황" menuNameFirst="true" /></td>
			<c:if test="${!empty ptCompBVoList }">
				<td class="width5"></td>
				<td class="frontinput">
					<select name="paramCompId" <u:elemTitle titleId="cols.comp" /> style="min-width:100px;" onchange="chnComp(this);">
					<c:forEach items="${ptCompBVoList}" var="ptCompBVo" varStatus="status">
						<u:option value="${ptCompBVo.compId}" title="${ptCompBVo.rescNm}" checkValue="${param.paramCompId }"/>
					</c:forEach>
				</select>
				</td>
			</c:if>
	 		</tr>
		</table>
	</div>
</div>


<% // 검색영역 %>
<form name="searchForm" id="searchForm" action="./listResc.do" >
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="typ" value="${param.typ}" />
<c:if test="${!empty param.paramCompId }"><u:input type="hidden" id="paramCompId" value="${param.paramCompId}" /></c:if>

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
					<c:when test="${param.typ == 'L'}"><u:buttonS href="./listResc.do?${paramsForList }&typ=I" titleId="wr.btn.imgView" alt="이미지로보기" /></c:when>
					<c:otherwise><u:buttonS href="./listResc.do?${paramsForList }&typ=L" titleId="wr.btn.txtView" alt="텍스트로보기" /></c:otherwise>
				</c:choose>
				</td>
			</tr>
			</tbody>
		</table>
	</div>
</div>
</form>
<% // 목록1 %>
<c:choose>
	<c:when test="${param.typ == 'L'}"><% // 목록1 %>
		<div class="listarea">
			<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
			<colgroup><col width="22%"/><col width="22%"/><col width="*"/><col width="15%"/></colgroup>
			<tr>
				<td class="head_ct"><u:msg titleId="cols.rescKnd" alt="자원종류" /></td>
				<td class="head_ct"><u:msg titleId="cols.rescNm" alt="자원명" /></td>
				<td class="head_ct"><u:msg titleId="cols.rescLoc" alt="자원위치" /></td>
				<td class="head_ct"><u:msg titleId="cols.regDt" alt="등록일" /></td>
			</tr>
			<c:choose>
				<c:when test="${!empty wrRescMngBVoList}">
					<c:forEach var="list" items="${wrRescMngBVoList}" varStatus="status">
						<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
							<td class="body_ct"><div class="ellipsis" title="${list.kndNm }">${list.kndNm }</div></td>
							<td class="body_ct"><div class="ellipsis" title="${list.rescNm }"><a href="javascript:;" onclick="viewRescMngPop('${list.rescMngId}');">${list.rescNm }</a></div></td>
							<td class="body_lt"><div class="ellipsis" title="${list.rescLoc }">${list.rescLoc }</div></td>
							<td class="body_ct"><u:out value="${list.regDt }" type="date"/></td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr>
						<td class="nodata" colspan="4"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
					</tr>
				</c:otherwise>
			</c:choose>
			</table>
		</div>
	</c:when>
	<c:otherwise><% // 목록2 %>
		<c:choose>
			<c:when test="${!empty wrRescMngBVoList}">
				<u:titleArea outerStyle="overflow:hidden;" innerStyle="padding:0 0 0 10px;">
					<c:forEach var="list" items="${wrRescMngBVoList}" varStatus="status">
						<c:set var="wrRescImgDVo" value="${list.wrRescImgDVo}" />
						<c:set var="maxWdth" value="100" />
						<c:set var="maxHght" value="100" />
						<u:set test="${wrRescImgDVo != null && wrRescImgDVo.imgWdth <= maxWdth}" var="imgWdth" value="${wrRescImgDVo.imgWdth}" elseValue="${maxWdth}" />
						<u:set test="${wrRescImgDVo != null && wrRescImgDVo.imgHght <= maxHght}" var="imgHght" value="${wrRescImgDVo.imgHght}" elseValue="${maxHght}" />
						<u:set test="${wrRescImgDVo.imgWdth < wrRescImgDVo.imgHght}" var="imgWdthHgth" value="height='${imgHght}'" elseValue="width='${imgWdth}'" />
						<div class="listarea" style="float:left; width:19.2%; padding:8px 8px 0 0;height:180px;">
							<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
								<tbody>
								<tr>
									<td class="photo_ct" ><a href="javascript:;" onclick="viewRescMngPop('${list.rescMngId}');"><img src="${_cxPth}${list.wrRescImgDVo.imgPath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' ${imgWdthHgth}></a></td>
								</tr>
								<tr>
									<td class="head_ct">
										<div class="ellipsis" title="${list.rescNm }"><a href="javascript:;" onclick="viewRescMngPop('${list.rescMngId}');">${list.rescNm }</a></div>
										<div class="ellipsis" title="${list.kndNm }" style="font-weight:bold;">${list.kndNm }</div>
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
<u:blank />
<u:pagination />
<u:buttonArea>
<u:button titleId="cm.btn.excelDown" alt="엑셀다운" onclick="excelDownFile();" auth="R" />
<u:button titleId="wr.btn.rescRezv" alt="자원예약" href="javascript:;" onclick="setRezvPop();" auth="W" />
</u:buttonArea>
<form id="excelForm">
	<c:forEach items="${paramEntryList}" var="entry" varStatus="status">
		<u:input type="hidden" id="${entry.key}" value="${entry.value}" />
	</c:forEach>
	<u:input type="hidden" id="listPage" value="${listPage}" />
</form>