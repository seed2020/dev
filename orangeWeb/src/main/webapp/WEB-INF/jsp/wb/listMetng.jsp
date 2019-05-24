<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set var="agntParam" test="${!empty schBcRegrUid }" value="&schBcRegrUid=${schBcRegrUid }" elseValue=""/>
<u:params var="nonPageParams" excludes="schBcRegrUid,pageNo"/>
<script type="text/javascript">
<!--
<% // 엑셀 파일 다운로드 %>
function excelDownFile() {
	var $form = $('#excelForm');
	$form.attr('method','post');
	$form.attr('action','./excelDownLoadMetng.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form[0].submit();
};

<%// 검색조건 초기화 %>
function fnSchValInit(id){
	$('#'+(id == undefined ? 'search_area_all' : id )+' input').val('');
};

//1명의 사용자 선택
function schUserPop(){
	var data = {};<%// 팝업 열때 선택될 데이타 %>
	<%// option : data, multi, withSub, titleId %>
	searchUserPop({data:data}, function(userVo){
		$('#schRegrUid').val(userVo.userUid);
		$('#schRegrNm').val(userVo.rescNm);
	});
};

<%// 명함 조회 팝업 %>
function findBcPop(){
	dialog.open('findBcPop','<u:msg titleId="wb.jsp.findBcPop.title" alt="명함선택" />','./findBcPop.do?menuId=${menuId}&listPage=${listPage}');
};

//명함 선택
function fnBcSelect(detlViewType, callBack ){
	var objArr = getIframeContent(detlViewType+'Frm').fnSelArrs();
	if(objArr == null ) return;
	if(callBack != "" ){
		eval(callBack)(objArr);
	}else{
		objArr.each(function(index, obj){
			$("#search_area_all2 input[name=schBcId]").val(obj.bcId);
			$("#search_area_all2 input[name=schBcNm]").val(obj.bcNm);
		});		
	}
	dialog.close('findBcPop');
};

//대리명함 select 선택
function fnAgntSch(obj){
	$('#schFldNm').val('');
	$('#schFldId').val('');
	searchForm.submit();
};

//미팅 상세보기
function viewMetngPop(bcMetngDetlId) {
	dialog.open('viewMetngPop', '<u:msg titleId="wb.jsp.viewMetngPop.title" alt="미팅상세보기" />', './viewMetngPop.do?menuId=${menuId}&bcMetngDetlId='+bcMetngDetlId);
};

//상세보기
function viewMetng(bcMetngDetlId) {
	location.href="./${viewPage}.do?${paramsForList }&schOpenYn=${param.schOpenYn}${agntParam}&bcMetngDetlId="+bcMetngDetlId;
};

//상세보기
function viewBc(bcId) {
	dialog.open('viewBcPop', '<u:msg titleId="wb.jsp.viewBcPop.title" alt="명함상세보기" />', './viewBcPop.do?menuId=${menuId}&bcId='+bcId+'${agntParam}');
};

<%// 선택삭제%>
function fnDelete(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) delRowInArr(arr);
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
function delRowInArr(rowArr){
	var delArr = [], $bcId;
	//if(delVa!='') delArr.push(delVa);
	for(var i=0;i<rowArr.length;i++){
		$bcId = $(rowArr[i]).find("input[name='bcMetngDetlId']");
		if($bcId.val()!=''){
			delArr.push($bcId.val());
		}
	}
	$("#delList").val(delArr.join(','));
	
	if(confirmMsg("cm.cfrm.del")) {
		var $form = $('#deleteForm');
		$form.attr('method','post');
		$form.attr('action','./${transDelPage}.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
};

$(document).ready(function() {
setUniformCSS();
});
//-->
</script>

<form name="searchForm" action="./${listPage}.do" >
<u:input type="hidden" id="menuId" value="${menuId}" />
<div class="front">
	<div class="front_left">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td style="padding:3px 4px 0 0px">
				<c:choose>
					<c:when test="${listPage eq 'listAgntMetng' }"><u:title titleId="wb.jsp.listAgntMetng.title" alt="대리관련미팅" menuNameFirst="true" /></c:when>
					<c:when test="${listPage eq 'listAllMetng' }"><u:title titleId="wb.jsp.listAllMetng.title" alt="전체관련미팅" menuNameFirst="true" /></c:when>
					<c:otherwise><u:title titleId="wb.jsp.listMetng.title" alt="관련미팅" menuNameFirst="true" /></c:otherwise>
				</c:choose>
			</td>
			<c:if test="${listPage eq 'listAgntMetng' }">
				<td class="width5"></td>
				<td class="frontinput">
					<select id="bcRegrUid" name="schBcRegrUid" <u:elemTitle titleId="wb.jsp.setAgntAdm.tab.agntBc" alt="대리인기본값설정" /> onchange="fnAgntSch(this);">
						<u:option value="" titleId="wb.jsp.setAgntAdm.select.agntBc" selected="${empty agntSetupList }"/>
						<c:forEach items="${agntSetupList}" var="agntVo" varStatus="status">
							<u:option value="${agntVo.regrUid}" title="${agntVo.userNm}" checkValue="${schBcRegrUid }"/>
						</c:forEach>
					</select>
				</td>
			</c:if>
	 		</tr>
		</table>
	</div>
</div>
<% // 검색영역 %>
<u:searchArea>

<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<select name="schCat" style="width:120px;">
							<u:option value="metngSubj" titleId="cols.subj" alt="제목" checkValue="${param.schCat}" />
							<u:option value="metngCont" titleId="cols.cont" alt="내용" checkValue="${param.schCat}" />
							<%-- <u:option value="regrUid" titleId="cols.regr" alt="등록자" checkValue="${param.schCat}" /> --%>
						</select>
					</td>
					<td><u:input id="schWord" maxByte="50" name="schWord" value="${param.schWord}" titleId="cols.schWord" style="width:200px;" onkeydown="if (event.keyCode == 13) searchForm.submit();"/></td>
					<td class="width30"></td>
					<td>
						<select name="schMetngClsCd" <u:elemTitle titleId="wb.cols.cls" />>
							<option value=""> -- </option>
							<c:forEach items="${wbMetngClsCdBVoList}" var="list" varStatus="status">
								<u:option value="${list.rescId}" title="${list.rescNm}" checkValue="${param.schMetngClsCd }"/>
							</c:forEach>
						</select>
					</td>
					<td class="width10"></td>
					<%-- <td>
						<u:checkArea>
							<u:radio name="schOpenYn" value="" titleId="cm.option.all" alt="전체" checkValue="${param.schOpenYn }" checked="${empty param.schOpenYn }" inputClass="bodybg_lt"/>
							<u:radio name="schOpenYn" value="Y" titleId="cm.option.publ" alt="공개" checkValue="${param.schOpenYn }"  inputClass="bodybg_lt"/>
							<u:radio name="schOpenYn" value="N" titleId="cm.option.priv" alt="비공개" checkValue="${param.schOpenYn }" inputClass="bodybg_lt" />
						</u:checkArea>
					</td> --%>
				</tr>
				<c:if test="${listPage eq 'listAllMetng' }">
					<tr>
						<td colspan="6">
							<table id="search_area_all" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td>
										<u:input type="hidden" id="schRegrUid" value="${param.schRegrUid}" />
										<u:input id="schRegrNm" name="schRegrNm" value="${param.schRegrNm}" titleId="cols.regr" style="width:100px;" readonly="Y"/>
									</td>
									<td width="60px"><u:buttonS titleId="cols.regr" alt="등록자" href="javascript:;" onclick="schUserPop();"/></td>
									<td class="width30"></td>
									<td class="search_tit" width="80px"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
									<td>
										<input type="hidden" name="durCat" value="fromYmd"/>
										<u:calendar id="durStrtDt" value="${param.durStrtDt}" />
									</td>
									<td class="search_titx">&nbsp;&nbsp;~</td>
									<td><u:calendar id="durEndDt" value="${param.durEndDt}" /></td>
									<td class="width30"></td>
									<td><u:buttonS titleId="wb.btn.schOption.reset" alt="초기화" href="javascript:;" onclick="fnSchValInit('search_area_all');"/></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="6">
							<table id="search_area_all2" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td>
										<u:input type="hidden" id="schBcId" value="${param.schBcId}" />
										<u:input id="schBcNm" name="schBcNm" value="${param.schBcNm}" titleId="wb.cols.bc" style="width:100px;" readonly="Y"/>
									</td>
									<td width="60px"><u:buttonS titleId="wb.btn.bc.search" alt="명함조회" href="javascript:;" onclick="findBcPop();"/></td>
									<td class="width30"></td>
									<td class="search_tit" width="80px"><u:msg titleId="cols.metngDt" alt="관련미팅일시" /></td>
									<td>
										<u:calendar id="metngStrtDt" value="${param.metngStrtDt}" titleId="cols.metngDt"/>
									</td>
									<td class="search_titx">&nbsp;&nbsp;~</td>
									<td><u:calendar id="metngEndDt" value="${param.metngEndDt}" titleId="cols.metngDt"/></td>
									<td class="width30"></td>
									<td><u:buttonS titleId="wb.btn.schOption.reset" alt="초기화" href="javascript:;" onclick="fnSchValInit('search_area_all2');"/></td>
								</tr>
							</table>
						</td>
					</tr>
				</c:if>
			</table>
		</td>
		<td>
			<div class="button_search">
				<ul>
					<li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li>
				</ul>
			</div>
		</td>
	</tr>
</table>
</u:searchArea>
</form>
	<div class="front">
		<c:if test="${listPage ne 'listAgntMetng' && listPage ne 'listAllMetng'}">
			<div class="front_right">
				<table border="0" cellpadding="0" cellspacing="0">
					<tbody>
					<tr>
						<td class="frontbtn">
						<c:choose>
							<c:when test="${empty param.schOpenYn || param.schOpenYn eq 'N'}"><u:buttonS href="./${listPage}.do?${paramsForList }&schOpenYn=Y${agntParam}" titleId="wb.btn.publData" alt="공개데이터" /></c:when>
							<c:otherwise><u:buttonS href="./${listPage}.do?${paramsForList }" titleId="wb.btn.myData" alt="본인데이터" /></c:otherwise>
						</c:choose>
					</tr>
					</tbody>
				</table>
			</div>			
		</c:if>
	</div>
<% // 목록 %>
<div id="listArea" class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
	<tr id="headerTr">
		<td width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td>
		<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
		<td width="10%" class="head_ct"><u:msg titleId="wb.cols.bc" alt="명함" /></td>
		<td width="10%" class="head_ct"><u:msg titleId="cols.comp" alt="회사" /></td>
		<td width="10%" class="head_ct"><u:msg titleId="cols.metngDt" alt="관련미팅일시" /></td>
		<td width="10%" class="head_ct"><u:msg titleId="wb.cols.cls" alt="분류" /></td>
		<td width="8%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>		
		<td width="10%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
		<td width="8%" class="head_ct"><u:msg titleId="cols.readCnt" alt="조회수" /></td>
		<td width="6%" class="head_ct"><u:msg titleId="cols.att" alt="첨부" /></td>
		<c:if test="${listPage eq 'listAllMetng' }"><td width="6%" class="head_ct"><u:msg titleId="cols.secul" alt="보안등급" /></td></c:if>
	</tr>
	<c:choose>
		<c:when test="${!empty wbBcMetngDVoList}">
			<c:forEach var="list" items="${wbBcMetngDVoList}" varStatus="status">
				<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
					<td class="bodybg_ct"><u:checkbox name="metngCheck" value="${list.bcMetngDetlId }" checked="false" /><u:input type="hidden" name="bcMetngDetlId" value="${list.bcMetngDetlId }"/></td>
					<td class="body_lt"><div class="ellipsis" title="${list.metngSubj }"><a href="javascript:viewMetng('${list.bcMetngDetlId }');">${list.metngSubj }</a></div></td>
					<td class="body_ct"><div class="ellipsis" title="${list.bcNm }"><a href="javascript:viewBc('${list.bcId }');">${list.bcNm }</a></div></td>
					<td class="body_ct"><div class="ellipsis" title="${list.compNm }">${list.compNm }</div></td>
					<td class="body_ct">${list.metngYmd }</td>
					<td class="body_ct">${list.metngClsNm }</td>
					<td class="body_ct"><a href="javascript:viewUserPop('${list.regrUid}');">${list.regrNm }</a></td>
					<td class="body_ct">${list.regDt }</td>
					<td class="body_ct">${list.readCnt }</td>
					<td class="body_ct"><c:if test="${list.fileCnt > 0}"><u:icon type="att" /></c:if></td>
					<c:if test="${listPage eq 'listAllMetng' }"><td class="body_ct"><u:msg titleId="${list.openYn eq 'Y' ? 'cm.option.publ' : 'cm.option.priv'}" alt="공개" /></td></c:if>
				</tr>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<tr>
				<td class="nodata" colspan="${listPage eq 'listAllMetng' ? '11' : '10' }"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
		</c:otherwise>
	</c:choose>
	</table>
</div>
<u:blank />

<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" auth="R" />
	<u:button titleId="cm.btn.excelDown" alt="엑셀다운" onclick="excelDownFile();" auth="R" />
	<c:if test="${( listPage eq 'listMetng' && ( empty param.schOpenYn || param.schOpenYn eq 'N' ) ) || listPage eq 'listAllMetng' || ( listPage eq 'listAgntMetng' && wbBcAgntAdmBVo.authCd eq 'RW' )}">
		<c:if test="${listPage ne 'listAllMetng' }"><u:button titleId="cm.btn.reg" alt="등록" href="./${setPage }.do?${paramsForList }&schOpenYn=${param.schOpenYn}${agntParam}" auth="W" /></c:if>
		<u:button titleId="cm.btn.del" alt="삭제" href="javascript:fnDelete();" auth="${listPage eq 'listAllMetng' ? 'A' : 'W' }" />
	</c:if>
</u:buttonArea>
<form id="deleteForm" >
	<u:input type="hidden" name="schOpenYn" value="${param.schOpenYn}" />
	<u:input type="hidden" name="delList"  id="delList"/>
	<c:if test="${!empty schBcRegrUid}">
		<u:input type="hidden" id="schBcRegrUid" value="${schBcRegrUid }"/>
	</c:if>
	<u:input type="hidden" id="listPage" value="./${listPage}.do?${nonPageParams }${agntParam}" />
</form>
<form id="excelForm">
	<c:forEach items="${paramEntryList}" var="entry" varStatus="status">
		<u:input type="hidden" id="${entry.key}" value="${entry.value}" />
	</c:forEach>
	<u:input type="hidden" id="listPage" value="${listPage}" />
</form>