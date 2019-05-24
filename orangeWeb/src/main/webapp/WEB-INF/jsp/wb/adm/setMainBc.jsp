<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
var gEmptyRight = true;
//메인설정저장
function mainSave(){
	if(gEmptyRight){
		alertMsg("wb.msg.duplFrnd.msg1");<%//wb.msg.duplFrnd.msg1=왼쪽 명함을 선택 후 사용해 주십시요.%>
		return;
	}
	var param = getIframeContent('bcSubList').fnSelBc();
	if(param == null ){
		return;
	}
	if(confirmMsg("cm.cfrm.save")) {<%//cm.cfrm.save=저장하시겠습니까 ?%>
		callAjax('./transBcMainAjx.do?menuId=${menuId}', {bcId:param[0].bcId, originalBcId:param[0].originalBcId}, function(data){
			if(data.message!=null){
				alert(data.message);
			}
			if(data.result=='ok'){
				openBcList(param[0].bcId , param[0].originalBcId);
				//getIframeContent('bcSubList').reload('./listBcSubFrm.do?menuId=${menuId}&bcId='+param[0].bcId+'&originalBcId='+param[0].originalBcId);
				var mainListType = $('#searchArea input:radio[name="mainListType"]:checked').val();
				openMainList(mainListType);
			}
		});
	}
};

<%// [클릭] - 왼쪽 리스트 열기 %>
function openMainList(mainListType){
	$("#bcMainList").attr('src', './listBcMainFrm.do?menuId=${menuId}&schCat=bcNm&schWord=${param.schWord }&mainListType='+mainListType);
	$("#bcSubList").attr('src', 'about:blank');
	gEmptyRight = true;
};

<%// [클릭] - 오른쪽 리스트 열기 %>
function openBcList(id , originalBcId){
	$("#bcSubList").attr('src', './listBcSubFrm.do?menuId=${menuId}&bcId='+id+'&originalBcId='+originalBcId);
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

<u:title titleId="wb.jsp.setMainBc.title" alt="메인명함 설정" menuNameFirst="true"/>

<% // 검색영역 %>
<form name="searchForm" action="./setMainBc.do" >
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
										<td class="search_tit"><u:msg titleId="cols.listChoi" alt="리스트선택" /></td>
										<td>
											<u:checkArea>
												<u:radio name="mainListType" value="wait" titleId="wb.option.mainWaitList" alt="대기목록" checkValue="${param.mainListType }" checked="${empty param.mainListType }" onclick="openMainList(this.value);"/>
												<u:radio name="mainListType" value="result" titleId="wb.option.mainResultList" alt="결과목록" checkValue="${param.mainListType }" onclick="openMainList(this.value);"/>
												<u:radio name="mainListType" value="all" titleId="wb.option.allList" alt="전체목록" checkValue="${param.mainListType }" onclick="openMainList(this.value);"/>
											</u:checkArea>
										</td>
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

<u:titleArea frameId="bcMainList" frameSrc="./listBcMainFrm.do?menuId=${menuId}&schCat=bcNm&schWord=${param.schWord }&mainListType=${param.mainListType }"
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
	<u:msg titleId="cm.msg.preparing" var="msg" alt="준비중.." />
	<u:button titleId="cm.btn.help" alt="도움말" href="javascript:void(alert('${msg}'));" auth="W" />
	<u:button titleId="cm.btn.confirm" alt="확인" href="javascript:mainSave();" auth="W" />
	</u:buttonArea>
