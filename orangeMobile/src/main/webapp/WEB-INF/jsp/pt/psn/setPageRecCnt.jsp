<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[<%
// 페이지수 코드 목록 %>
var pgCntList = null;<%
// 페이지 수 클릭 %>
function setPageCnt(module){
	if(pgCntList==null){
		var arr = [];
		"5,10,15,20,25,30,35,40,45,50".split(',').each(function(index, no){ arr.push({cd:no, nm:no}); });
		pgCntList = arr;
	}
	if(module=='_ALL'){
		$m.dialog.openSelect({id:'roleCd', cdList:pgCntList}, function(obj){
			if(obj!=null){
				$('#settingArea a').text(obj.cd);
			}
		});
	} else {
		var moduleLink = $('#settingArea #'+module);
		$m.dialog.openSelect({id:'roleCd', cdList:pgCntList, selected:moduleLink.text()}, function(obj){
			if(obj!=null){
				moduleLink.text(obj.cd);
			}
		});
	}
}<%
// 저장 클릭 %>
function savePageCnt(){
	var param = {setupClsId:'mb.pageRecCnt', cacheYn:'Y'};
	$('#settingArea a').each(function(){
		param['mb.pageRecCnt.'+$(this).attr('id')] = $(this).text();
	});
	$m.ajax("/pt/psn/transPageRecCntAjx.do?menuId=${menuId}", param, function(data){
		var ajaxResult = (data.result=='ok');
		if(data.message) $m.dialog.alert(data.message, function(){
			if(ajaxResult) $m.nav.prev(null, true);
		});
	}, {paramAsIs:true});
}
//]]>
</script>
<section>

	<div class="blankzone">
		<div class="blank30"></div>
	</div>

	<div class="s_tablearea">
		<table class="s_table">
		<caption>${_all.rescNm}</caption>
		<colgroup>
			<col width="50%"/>
			<col width=""/>
		</colgroup>
		<tbody>
			<tr>
				<th class="shead_lt">${_all.rescNm}</th>
				<td class="sbody_lt" onclick="setPageCnt('_ALL');"><a href="javascript:;">${_all.rescNm}</a></td>
			</tr>
		</tbody>
		</table>
		
		<table class="s_table">
		<caption><u:msg titleId="mpt.label.byModule" alt="모듈별" /></caption>
		<colgroup>
			<col width="50%"/>
			<col width=""/>
		</colgroup>
		<tbody id="settingArea"><c:forEach
					items="${pageRecSetupCdList}" var="pageRecSetupCd" varStatus="status"><c:if
						test="${pageRecSetupCd.cd != '_ALL'}"><u:convertMap
							srcId="pageRecCntMap" attId="${pageRecSetupCd.cd}" var="storedCnt" />
			<tr>
				<th class="shead_lt">${pageRecSetupCd.rescNm}</th>
				<td class="sbody_lt" onclick="setPageCnt('${pageRecSetupCd.cd}');"><a href="javascript:;" id="${pageRecSetupCd.cd}">${not empty storedCnt ? storedCnt : _default}</a></td>
			</tr></c:if></c:forEach>
		</tbody>
		</table>
	</div>
		
		<div class="btnarea">
		<div class="blank5"></div>
		<div class="size">
			<dl>
			<dd class="btn" onclick="savePageCnt()"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
			</dl>
		</div>
		</div>

	<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
</section>