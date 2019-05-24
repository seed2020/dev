<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<%// 분류,폴더 Prefix %>
<%// 분류,폴더 Prefix %>
function getTabPrefix(lstTyp){
	var prefix = "fld";
	if(lstTyp == 'C') prefix = "cls";
	return prefix;
}<%// [버튼] 분류,폴더 %>
function findFldPop(lstTyp){
	var prefix = getTabPrefix(lstTyp);
	var $area = $("#"+prefix+"InfoArea"), data = [];
	$area.find("input[id='"+prefix+"Id']").each(function(){
		data.push($(this).val());
	});
	var url = './findFldPop.do?menuId=${menuId}&lstTyp='+lstTyp+"&fldId="+data+"&fncMul="+(lstTyp == 'C' ? 'Y':'N');
	var msgTitle = lstTyp == 'C' ? '<u:msg titleId="dm.cols.listTyp.cls" alt="분류보기" />' : '<u:msg titleId="dm.cols.listTyp.fld" alt="폴더보기" />';
	dialog.open('findFldPop', msgTitle, url);
};<%// 분류,폴더 적용%>
function setFldInfos(arr, lstTyp){
	var prefix = getTabPrefix(lstTyp);
	$area = $('#'+prefix+'InfoArea');
	
	var buffer = [];
	var nms = '';
	arr.each(function(index, obj){
		buffer.push("<input type='hidden' id='"+prefix+"Id' name='"+prefix+"Id' value='"+obj.id+"'/>\n");
		nms+= nms == '' ? obj.nm : ','+obj.nm;
	});
	$area.find('#idArea').html('');
	$area.find('#idArea').html(buffer.join(''));
	$area.find('#nmArea input[id="'+prefix+'Nm"]').val(nms);
	dialog.close('findFldPop');
}<%// 1명의 사용자 선택 %>
function openSingUser(id){
	var data = [];<%// data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	if($('#'+id+'Uid').val() != '') data.push({userUid:$('#'+id+'Uid').val()});
	<%// option : data, multi, withSub, titleId %>
	searchUserPop({data:data,userStatCd:'999'}, function(userVo){
		if(userVo!=null){
			$('#'+id+'Uid').val(userVo.userUid);
			$('#'+id+'Nm').val(userVo.rescNm);
		}
	});
};
</script>
<% // 검색영역 %>
<u:searchArea style="position:relative; z-index:2;">
	<div id="searchArea1" style="<c:if test="${not empty param.srchDetl}">display:none;</c:if>">
	<form id="searchForm1" name="searchForm1" action="${_uri}" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="lstTyp" value="${lstTyp}" /><c:if
		test="${not empty param.pageRowCnt}">
	<u:input type="hidden" id="pageRowCnt" value="${param.pageRowCnt}" /></c:if>
	<table id="searchFormTbl1" class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">
		<u:option value="subj" titleId="cols.subj" alt="제목" checkValue="${param.schCat}" />
		<u:option value="cont" titleId="cols.cont" alt="내용" checkValue="${param.schCat}" />
		</select></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 100px;" onkeydown="if (event.keyCode == 13) searchForm1.submit();" /></td>
		<td class="width20"></td>
		<!-- 등록일시 -->
		<td class="search_tit"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
		<td><u:input type="hidden" id="durCat" name="durCat" value="regDt"/>
			<c:set var="srchYmdMap" value="${srchYmdMap }" scope="request"/>
			<u:convertMap var="srchYmdMap1" srcId="srchYmdMap" attId="regDt" />
			<u:convertMap var="strtValue1" srcId="srchYmdMap1" attId="durStrtDt" />
			<u:convertMap var="endValue1" srcId="srchYmdMap1" attId="durEndDt" />
			<table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td><u:calendar id="durStrtDt1" name="durStrtDt" option="{end:'durEndDt1'}" titleId="cols.strtYmd" value="${strtValue1}" /></td>
			<td class="search_body_ct"> ~ </td>
			<td><u:calendar id="durEndDt1" name="durEndDt" option="{start:'durStrtDt1'}" titleId="cols.endYmd" value="${endValue1}" /></td>
			</tr>
			</table></td>
		<td class="width20"><u:input type="hidden" id="durCat" name="durCat" value="regDt"/></td>
		<c:if test="${lstTyp eq 'L' }">
		<td class="search_tit"><u:msg titleId="dm.cols.fld" alt="폴더" /></td>
		<td>
			<div id="fldInfoArea" style="display:inline;">
				<div id="idArea" style="display:none;"><u:input type="hidden" id="fldId" value="${param.fldId }"/></div>
				<div id="nmArea" style="display:inline;"><u:input id="fldNm" titleId="cols.fld" value="${param.fldNm}" mandatory="Y" style="width:100px;" readonly="Y"/></div>
			</div>
			<u:buttonS titleId="dm.btn.fldSel" alt="폴더 선택" onclick="findFldPop('F');" />
		</td>
		<td class="width20"></td>
		</c:if>
		<c:if test="${isAdmin == true }">
		<!-- 등록자 -->
		<td class="search_tit"><u:msg titleId="cols.regr" alt="등록자" /></td>
		<td><table border="0" cellpadding="0" cellspacing="0">
			<tbody>
			<tr>
				<td>
					<u:input type="hidden" id="regrUid" value="${param.regrUid}"/>
					<u:input id="regrNm" titleId="cols.regr" readonly="Y" mandatory="Y" style="width:70px;" value="${param.regrNm }"/>							
				</td>
				<td><u:buttonS href="javascript:;" titleId="cm.btn.choice" alt="선택" onclick="openSingUser('regr');" /></td>
			</tr>
			</tbody>
		</table></td>
		</c:if>
		<tr>
			<td colspan="2"><u:buttonS href="javascript:;" onclick="valueReset('searchFormTbl1',['durCat']);" titleId="cm.btn.srch.reset" alt="검색조건 초기화" /></td>
			<td class="width20"></td>			
		</tr>
		</tr>
		</table>
	</td>
	<td><div class="button_search"><ul><li class="search"><a href="javascript:document.searchForm1.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form></div>
</u:searchArea>