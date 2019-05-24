<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="params" />
<script type="text/javascript">
<!--
var gEmptyRight = true;

<%// 초기화%>
function doReset(){
	if(gEmptyRight){
		alertMsg("wb.msg.duplFrnd.msg1");<%//wb.msg.duplFrnd.msg1=왼쪽 명함을 선택 후 사용해 주십시요.%>
		return;
	}
	var param = getIframeContent('bcSubList').fnSelBc(false);
	if(param == null ){
		return;
	}
	for(var i=0;i<param.bcIds.length;i++){
		if(param.bcIds[i] == undefined){
			param.bcIds[i] = param.originalBcId;
		}
	}
	
	if(confirmMsg("wb.cfrm.duplFrnd.reset")) {<%//wb.cfrm.duplFrnd.reset=해당 명함의 메인설정을 초기화 하시겠습니까? %>
		callAjax('./transDuplMainResetAjx.do?menuId=${menuId}', {bcIds:param.bcIds, originalBcId:param.originalBcId}, function(data){
			if(data.message!=null){
				alert(data.message);
			}
			if(data.result=='ok'){				
				$("#bcMainList").attr('src', './listDuplMainFrm.do?menuId=${menuId}&schCat=bcNm&schWord=${param.schWord }');
				openBcList(param.originalBcId);
			}
		});
	}
};

//메인설정저장
function mainSave(){
	if(gEmptyRight){
		alertMsg("wb.msg.duplFrnd.msg1");<%//wb.msg.duplFrnd.msg1=왼쪽 명함을 선택 후 사용해 주십시요.%>
		return;
	}
	var param = getIframeContent('bcSubList').fnSelBc(true);
	if(param == null ){
		return;
	}
	if(confirmMsg("cm.cfrm.save")) {<%//cm.cfrm.save=저장하시겠습니까 ?%>
		callAjax('./transDuplMainAjx.do?menuId=${menuId}', {bcIds:param.bcIds, originalBcId:param.originalBcId}, function(data){
			if(data.message!=null){
				alert(data.message);
			}
			if(data.result=='ok'){
				$("#bcMainList").attr('src', './listDuplMainFrm.do?menuId=${menuId}&schCat=bcNm&schWord=${param.schWord }');
				openBcList(param.originalBcId);
				//getIframeContent('bcSubList').reload('./listDuplSubFrm.do?menuId=${menuId}&bcId='+param[0].bcId+'&originalBcId='+param[0].originalBcId);
			}
		});
	}
};

<%// [클릭] - 왼쪽 리스트 열기 %>
function openMainList(){
	$("#bcMainList").attr('src', './listDuplMainFrm.do?menuId=${menuId}&schCat=bcNm&schWord=${param.schWord }');
	$("#bcSubList").attr('src', 'about:blank');
	gEmptyRight = true;
};

<%// [클릭] - 오른쪽 리스트 열기 %>
function openBcList(id ){
	$("#bcSubList").attr('src', './listDuplSubFrm.do?menuId=${menuId}&bcId='+id);
	gEmptyRight = false;
};


//상세보기
function viewBc(bcId) {
	dialog.open('viewBcPop', '<u:msg titleId="wb.jsp.viewBcPop.title" alt="명함상세보기" />', './viewAllBcPop.do?menuId=${menuId}&bcId='+bcId);
};

$(document).ready(function() {
setUniformCSS();
});
//-->
</script>

<u:title titleId="wb.jsp.setDupFrnd.title" alt="중복지인설정" menuNameFirst="true"/>

<% // 검색영역 %>
<form name="searchForm" action="./setDupFrnd.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:searchArea id="searchArea">
		<table class="search_table" cellspacing="0" cellpadding="0" border="0">
			<tr>
				<td>
					<table  border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td>
								<table border="0" cellpadding="0" cellspacing="0">
									<tr>
										<td class="search_tit"><u:msg titleId="cols.nm" alt="이름" /><input type="hidden" name="schCat" value="bcNm"/></td>
										<td><u:input id="schWord" maxByte="50" name="schWord" value="${param.schWord}" titleId="cols.schWord" /></td>
										<td class="width30"></td>
										<td class="search_tit"><u:msg titleId="wb.cols.vip" alt="주요인사" /></td>
										<td><u:checkbox id="schIptfgYn" name="schIptfgYn" value="Y" titleId="wb.cols.vip" checkValue="${param.schIptfgYn}"/></td>
										<td class="width30"></td>
										<td class="search_tit"><u:msg titleId="cols.prd" alt="기간" /></td>
										<td>
											<input type="hidden" name="durCat" value="fromYmd"/>
											<u:calendar id="durStrtDt" value="${param.durStrtDt}" />
										</td>
										<td>~</td>
										<td><u:calendar id="durEndDt" value="${param.durEndDt}" /></td>
									</tr>
								</table>
							</td>
						</tr>
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
<!-- LEFT -->
<div style="float:left; width:49%;">
<u:titleArea frameId="bcMainList" frameSrc="./listDuplMainFrm.do?menuId=${menuId}&${params }"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:590px;" />
</div>

	
<!-- RIGHT -->
<div style="float:right; width:50%;">

<u:titleArea frameId="bcSubList" frameSrc="/cm/util/reloadable.do"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:590px;" />
	
</div>

<u:blank />

	<% // 하단 버튼 %>
	<u:buttonArea>
	<%-- <u:msg titleId="cm.msg.preparing" var="msg" alt="준비중.." />
	<u:button titleId="cm.btn.help" alt="도움말" href="javascript:void(alert('${msg}'));" auth="W" /> --%>
	<u:button titleId="cm.btn.reset" alt="초기화" onclick="doReset();" auth="W" />
	<u:button titleId="cm.btn.confirm" alt="확인" href="javascript:mainSave();" auth="W" />
	</u:buttonArea>
