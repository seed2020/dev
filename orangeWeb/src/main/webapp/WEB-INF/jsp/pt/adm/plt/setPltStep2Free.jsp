<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--
<%// [팝업] - 포틀릿선택 팝업에서 선택된것 모으는 배열, id, rescId, rescNm %>
var gPltIds = [];
var gPltRescIds = [];
var gPltRescNms = [];
var gPltWdthPxs = [];
var gPltHghtPxs = [];
<%// [팝업] 페이지 이동 - 기존에 선택한것 유지%>
function onPageChange(pageNo){
	updateCheckedPlt();
	var param = new ParamMap().getData('paginationForm');
	addPlt(param.get("zoneCd"), "./listPltPop.do?"+param.toQueryString());
	return false;
}
<%// [팝업] 검색 %>
function searchPlt(){
	var param = new ParamMap().getData('searchForm');
	addPlt(param.get("zoneCd"), "./listPltPop.do?"+param.toQueryString());
}
<%// [팝업] 체크된 포틀릿 목록 업데이트 - from : setCheckedPlt(포틀릿팝업에서 확인버튼), onPageChange(페이지이동) %>
function updateCheckedPlt(){
	var arr = getCheckedArray("listArea");
	if(arr.length>0){
		arr.each(function(index, plt){
			if(!gPltIds.contains(plt.id)){
				gPltIds.push(plt.id);
				gPltRescIds.push(plt.rescId);
				gPltRescNms.push(plt.rescNm);
				gPltWdthPxs.push(plt.wdthPx);
				gPltHghtPxs.push(plt.hghtPx);
			}
		});
	}
}
<%// [팝업-확인버튼] 포틀릿 선택 %>
function setCheckedPlt(zoneCd){
	updateCheckedPlt();
	if(gPltIds.length==0){
		alertMsg("cm.msg.noSelect");
	} else {
		var alreadys = getAlreadySetPlts();
		var maxCnt = parseInt('${pltPolc.maxSetupCnt}');
		var avblCnt = maxCnt - alreadys.length;
		for(var i=0; i<gPltIds.length && i<avblCnt;i++){
			pltDialog.open('pltArea', gPltIds[i], gPltRescNms[i], null, null, parseInt(gPltWdthPxs[i],10), parseInt(gPltHghtPxs[i],10), gPltRescIds[i]);
		}
		if(gPltIds.length > avblCnt){
			alertMsg("pt.jsp.setPltStep2.excd.maxCnt", ['${pltPolc.maxSetupCnt}']);<%//pt.jsp.setPltStep2.excd.maxCnt=설정 할 수 있는 포틀릿의 최대 갯수는 {0}개 입니다.%>
		}
	}
	dialog.close('selectPltDialog');
}
<%// [소버튼] 포틀릿추가 - 팝업 띄우기 %>
function addPlt(zoneCd, url){
	<%// 이미 포함된 포틀릿ID 배열을 가져옴 %>
	var alreadys = getAlreadySetPlts();
	if(url==null) url =  "./listPltPop.do?menuId=${menuId}&zoneCd="+zoneCd;
	dialog.open("selectPltDialog", "<u:msg titleId='pt.jsp.setPltStep2.add.plt' alt='포틀릿 추가'/>", url);
	<%// 팝업 닫힐때 초기화 - 선택된 id,rescId,rescNm %>
	dialog.onClose("selectPltDialog", function(){ gPltIds=[]; gPltRescIds=[]; gPltRescNms=[]; gPltWdthPxs=[]; gPltHghtPxs=[];});
	
	<%// 이미 포함된 포틀릿을 disable 처리함 
	// 페이지 이동시 전에 체크된 체크박스가 해당 페이지에 오면 체크 되도록 %>
	var $checkboxes = $("#listArea input[type='checkbox']");
	$checkboxes.each(function(){
		<%// disable %>
		if(alreadys.contains($(this).val())){
			$(this).attr("disabled", true);
		}
		<%// checked %>
		if(gPltIds.contains($(this).val())){
			this.checked = true;
		}
	});
	$checkboxes.uniform.update();
}
<%// 이미 선택되어 설정되어 있는 포틀릿 목록 리턴 %>
function getAlreadySetPlts(){
	var alreadys=[];
	$("#pltArea div.portlet").each(function(){
		alreadys.push($(this).attr("id"));
	});
	return alreadys;
}
<%// [팝업] 체크된 포틀릿의 데이터 배열 리턴 - value : id + rescId + rescNm %>
function getCheckedArray(areaId, noSelectMsgId){
	var arr = [], va, cnt=0;
	$("#"+areaId+" input[type='checkbox']:checked").each(function(){
		va = $(this).val();
		if(va!=''){
			arr.push({id:va, rescId:$(this).attr("data-rescId"), rescNm:$(this).attr("data-rescNm"), wdthPx:$(this).attr("data-wdthPx"), hghtPx:$(this).attr("data-hghtPx")});
		}
	});
	if(cnt==0 && noSelectMsgId!=null){
		alertMsg(noSelectMsgId);
	}
	return arr;
}
<%// 픽셀 이동 간격 변경 - select %>
function setMovePx(value){
	pltDialog.movePx = parseInt(value);
}
<%// [팝업-저장 버튼] - 팝업 결과 세팅 %>
function saveLine(type, px){
	if(validator.validate('setLinePop')){
		guideLine.draw('pltArea', type, px);
		dialog.close("selectLineDialog");
	}
}
<%// [버튼] 저장 %>
function savePlt(){
	collectPltData();
	var $form = $("#step2");
	$form.attr("action","./transPltSetup.do");
	$form.attr("target","dataframe");
	$form.submit();
}
<%// 설정된 포틀릿 정보 모으기 - submit 용 %>
function collectPltData(){
	var arr = [], param, zidx=[];
	$("#pltArea div.portlet").each(function(){
		param = new ParamMap().put("pltId", $(this).attr("id")).put("rescId", $(this).attr("data-rescId"));
		param.put("topPx", parseInt($(this).css("top")));
		param.put("leftPx", parseInt($(this).css("left")));
		param.put("wdthPx", parseInt($(this).css("width")));
		param.put("hghtPx", parseInt($(this).css("height")));
		param.put("zidx", parseInt($(this).css("z-index")));
		zidx.push(parseInt($(this).css("z-index"), 10));
		arr.push(param.map);
	});
	<%// z-index 소트 %>
	zidx.sort();
	arr.each(function(index, obj){
		zidx.each(function(indexZ, objZ){
			if(objZ==obj.zidx){
				obj.zidx = indexZ+1;
				return false;
			}
		});
	});
	
	$("#zoneData1").val(JSON.stringify(arr));
}
<%// 메뉴그룹 목록으로 돌아가기 %>
function goBack(){
	location.replace("${empty param.backTo ? './setMyMnu.do?menuId='.concat(menuId) : param.backTo}");
}
$(document).ready(function() {
	<%// 포틀릿 다이얼로그 기본 설정 %>
	pltDialog.movePx = parseInt('${pltPolc.freeSetupMovePx}');
	pltDialog.skin = '${_skin}';
	<%// 유니폼 적용 %>
	setUniformCSS();
	<%// 환경설정 라인 그리기 %>
	var hLine = '${pltPolc.defaultHLineTop}';
	var vLine = '${pltPolc.defaultVLineLeft}';
	if(hLine!='') guideLine.draw('pltArea', 'H', hLine);
	if(vLine!='') guideLine.draw('pltArea', 'V', vLine);
	<%// 설정된 포틀릿 그리기 %>
	<c:forEach items="${ptPltSetupRVoList}" var="ptPltSetupRVo" varStatus="status">
	pltDialog.open('pltArea', '${ptPltSetupRVo.pltId}', '${ptPltSetupRVo.rescNm}', parseInt('${ptPltSetupRVo.topPx}'), parseInt('${ptPltSetupRVo.leftPx}'), parseInt('${ptPltSetupRVo.wdthPx}'), parseInt('${ptPltSetupRVo.hghtPx}'), '${ptPltSetupRVo.rescId}');
	</c:forEach>
});
//-->
</script>

<u:title titleId="pt.jsp.setPltStep2.title" alt="포틀릿 설정 2단계" />

<u:boxArea className="gbox" outerStyle="height:588px;padding:9px 12px 0 10px;" innerStyle="NO_INNER_IDV">
<form id="step2" method="post">
<input type="hidden" name="menuId" value="${menuId}" /><c:if test="${not empty param.mnuGrpId}">
<input type="hidden" name="mnuGrpId" value="${param.mnuGrpId}" /></c:if><c:if test="${not empty param.backTo}">
<input type="hidden" name="backTo" value="${param.backTo}" /></c:if><c:if test="${not empty param.useMnu}">
<input type="hidden" name="useMnu" value="${param.useMnu}" /></c:if><c:if test="${not empty param.usePlt}">
<input type="hidden" name="usePlt" value="${param.usePlt}" /></c:if>
<input type="hidden" name="pltLoutCd" value="${param.pltLoutCd}" />
<input type="hidden" name="zoneData1" id="zoneData1" value="" />

	<!-- 포틀릿 영역 S -->
	<div id="pltArea" style="position:relative; width:100%; height:552px; background:#ffffff; border:1px solid #bfc8d2; overflow-y:auto; overflow-x:auto;">
	
	</div>
	<!--// 포틀릿 영역 E -->
	
	<!--버튼영역 S--> 
	<div style="position:relative; width:100%; top:5px;">
	<div class="front">
	<div class="front_left">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="frontbtn"><u:buttonS href="javascript:addPlt('1');" alt="포틀릿 추가" titleId="pt.jsp.setPltStep2.add.plt" /></td>
		<td class="frontbtn"><u:buttonS href="javascript:guideLine.openPop('H','${menuId}');" alt="가로선 추가" titleId="pt.jsp.setPltStep2.add.hline" /></td>
		<td class="frontbtn"><u:buttonS href="javascript:guideLine.openPop('V','${menuId}');" alt="세로선 추가" titleId="pt.jsp.setPltStep2.add.vline" /></td>
		<td class="width5"></td>
		<td class="fronttitc"><u:msg titleId="pt.pltPolc.freeSetupMovePx" alt="픽셀 이동 간격"/></td>
		<td class="frontinput"><select id="freeSetupMovePx" name="pt.pltPolc.freeSetupMovePx" onchange="setMovePx(this.value);"<u:elemTitle titleId="pt.pltPolc.freeSetupMovePx" />>
			<c:forEach begin="1" end="10" step="1" var="no" varStatus="status">
			<u:option value="${no}" title="${no}" selected="${pltPolc.freeSetupMovePx == no}"/>
			</c:forEach>
		</select></td>
		</tr>
		</table>
	</div>
	</div>
	</div>
	<!--//버튼영역 E-->

</form>
</u:boxArea>

<u:buttonArea><c:if
		test="${not empty param.mnuGrpId}">
	<u:button href="javascript:savePlt();" titleId="cm.btn.save" alt="저장" auth="A" />
	<u:button href="javascript:history.go(-1);" titleId="cm.btn.cancel" alt="취소" />
	<u:button href="javascript:goBack();" titleId="cm.btn.list" alt="목록" /></c:if><c:if
		test="${empty param.mnuGrpId}">
	<u:button href="javascript:savePlt();" titleId="cm.btn.save" alt="저장" auth="W" />
	<u:button href="/pt/psn/my/setMyMnu.do?menuId=${menuId}" titleId="cm.btn.cancel" alt="취소" /></c:if>
</u:buttonArea>