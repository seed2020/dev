<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function fnSchValInit(id){
	$('#'+(id == undefined ? 'search_area_all' : id )+' input').val('');
};
$(document).ready(function() {
	setUniformCSS();
	$('#rescKndId').change(function(){
 		$('#rescMngId').find('option').each(function(){
 			$(this).remove();
 		});
 		$('#rescMngId').append('<u:option value="" titleId="cm.check.all" alt="전체선택"/>');
 		$("#rescMngId option:eq(0)").attr("selected", "selected"); 
 		$('#rescMngId').uniform();
 		if($(this).val() != ''){
 			$.ajax({
 		        url: './selectRescAjx.do?menuId=${menuId}',
 		        type: 'POST',
 		        data:{
 		        	rescKndId : $(this).val()
 		             },
 		        dataType : "json",
 		        success: function(data){
 		        	//if(data == null){}
 		        	
 		        	$.each(data.model.list , function(index, rescMngVo) {
 		        		$('#rescMngId').append('<u:option value="'+rescMngVo.rescMngId+'" title="'+rescMngVo.rescNm+'"/>');
 		        	});
 		        	$('#rescMngId').uniform();
 		        }
 			});
 		}
 	});
});
//-->
</script>
<u:searchArea>
<form name="searchForm" action="./listRezvStatFrm.do">
	<u:input type="hidden" id="menuId" value="${menuId }"/>
	<c:if test="${!empty param.paramCompId }"><u:input type="hidden" id="paramCompId" value="${param.paramCompId}" /></c:if>
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="search_tit"><u:msg titleId="cols.rescKnd" alt="자원종류" /></td>
					<td>
						<select id="rescKndId" name="rescKndId"  style="width:120px;">
							<u:option value="" titleId="cm.option.all" alt="전체선택"/>
							<c:forEach items="${wrRescKndBVoList}" var="list" varStatus="status">
								<u:option value="${list.rescKndId}" title="${list.kndNm}" selected="${param.rescKndId == list.rescKndId}"/>
							</c:forEach>
						</select>
					</td>
					<td class="width20"></td>
					<td class="search_tit"><u:msg titleId="cols.rescNm" alt="자원명" /></td>
					<td>
						<select id="rescMngId" name="rescMngId" style="width:200px;">
							<u:option value="" titleId="cm.option.all" alt="전체선택"/>
							<c:forEach items="${wrRescMngBVoList}" var="list" varStatus="status">
								<u:option value="${list.rescMngId}" title="${list.rescNm}" selected="${param.rescMngId == list.rescMngId}"/>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td colspan="5">
						<table id="search_area_all" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td class="search_tit" ><u:msg titleId="cols.rezvPrd" alt="예약기간" /></td>
								<td>
									<input type="hidden" name="durCat" value="fromYmd"/>
									<u:calendar id="durStrtDt" value="${param.durStrtDt}" />
								</td>
								<td><u:calendar id="durEndDt" value="${param.durEndDt}" /></td>
									<td class="width20"></td>
								<td><u:buttonS titleId="wb.btn.schOption.reset" alt="초기화" href="javascript:;" onclick="fnSchValInit('search_area_all');"/></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
		<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
</table>
</form>
</u:searchArea>
<% // 목록 %>
<u:listArea colgroup="10%,15%,*,17%,10%,10%">
	<tr>
		<td class="head_ct"><u:msg titleId="cols.rescKnd" alt="자원종류" /></td>
		<td class="head_ct"><u:msg titleId="cols.rescNm" alt="자원명" /></td>
		<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
		<td class="head_ct"><u:msg titleId="cols.rezvDt" alt="예약일시" /></td>
		<td class="head_ct"><u:msg titleId="cols.rezvr" alt="예약자" /></td>
		<td class="head_ct"><u:msg titleId="cols.note" alt="비고" /></td>
	</tr>
	<c:choose>
		<c:when test="${!empty wrRezvBVoList}">
			<c:forEach var="list" items="${wrRezvBVoList }" varStatus="status">
				<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
					<td class="body_ct">${list.kndNm }</td>
					<td class="body_ct"><a href="javascript:parent.viewRescInfoPop('${list.rescMngId }');">${list.rescNm }</a></td>
					<td class="body_lt">${list.subj }</td>
					<td class="body_ct"><u:out value="${list.rezvStrtDt }" type="longdate" /> ~ <u:out value="${list.rezvEndDt }" type="longdate" /></td>
					<td class="body_ct"><a href="javascript:parent.viewUserPop('${list.regrUid }');">${list.regrNm }</a></td>
					<td class="body_ct">
						<u:msg titleId="wr.jsp.discStatCd${list.discStatCd }.title" alt=""/>
					</td>
				</tr>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<tr>
				<td class="nodata" colspan="6"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
		</c:otherwise>
	</c:choose>
</u:listArea>
<u:pagination noTotalCount="true" />
