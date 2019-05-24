<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

/**

	2단 분할, 가운데 중심 3단 분할 - 중첩 안한 DIV
	
	3단 분할의
		1:1:2 또는 2:2:1 의 경우 중첩 DIV 필요

*/


%>
<script type="text/javascript">
<!--
<%// [아이콘] 이동 : direction - up,down,left,right %>
function movePlt(zoneCd, direction){
	var arr = [];
	$("#zoneCd"+zoneCd+" dd.texton").each(function(index, obj){
		arr.push(this);
	});
	if(arr.length==0){
		alertMsg("cm.msg.noSelectInArea");<%//cm.msg.noSelectInArea=해당하는 영역에서 선택한 항목이 없습니다%>
		return;
	}
	<%// move : left, right %>
	var targetZone = direction=="right" ? parseInt(zoneCd)+1 : direction=="left" ? parseInt(zoneCd)-1 : null;
	if(targetZone!=null){
		var $targetZone = $("#zoneCd"+targetZone);
		arr.each(function(index, obj){
			$targetZone.append(obj);
		});
		return;
	}
	<%// move : up %>
	if(direction=="up"){
		var cnt = 0, $up;
		arr.each(function(index, obj){
			$up = getNextNotSelected(obj, direction);
			if($up != null){
				$up.before(obj);
				cnt++;
			}
		});
		if(cnt==0){
			alertMsg("cm.msg.nodata.moveup");<%//cm.msg.nodata.moveup=위로 이동할 항목이 없습니다.%>
		}
	}
	<%// move : down %>
	if(direction=="down"){
		var cnt = 0, $down;
		arr.each(function(index, obj){
			$down = getNextNotSelected(obj, direction);
			if($down != null){
				$down.after(obj);
				cnt++;
			}
		}, true);
		if(cnt==0){
			alertMsg("cm.msg.nodata.movedown");<%//cm.msg.nodata.movedown=아래로 이동할 항목이 없습니다.%>
		}
	}
}
<%// 위,아래 이동용 - 다음 선택되지 않은 DD 객체 구하기 %>
function getNextNotSelected(obj, direction){
	var $next = $(obj);
	while(true){
		if(direction=="up"){
			$next = $next.prev();
		} else if(direction=="down"){
			$next = $next.next();
		}
		if($next.length==0) return null;
		if($next.attr('class')!='texton'){
			return $next;
		}
	}
}
<%// [아이콘] 포틀릿삭제 %>
function delPlt(zoneCd){
	var arr = [];
	$("#zoneCd"+zoneCd+" dd.texton").each(function(index, obj){
		arr.push(this);
	});
	if(arr.length==0){
		alertMsg("cm.msg.noSelect");<%//cm.msg.noSelect=선택한 항목이 없습니다.%>
	} else if(confirmMsg("pt.jsp.setPltStep2.del.cfrm")){<%//pt.jsp.setPltStep2.del.cfrm=선택한 포틀릿을 페이지에서 제거 하시겠습니까 ?%>
		arr.each(function(index, obj){
			$(obj).remove();
		});
	}
}
<%// [소버튼] 포틀릿추가 - 팝업 띄우기 %>
function addPlt(zoneCd, url){
	<%// 이미 포함된 포틀릿ID 배열을 가져옴 %>
	var alreadys = getAlreadySetPlts();
	
	if(url==null) url =  "./listPltPop.do?menuId=${menuId}&zoneCd="+zoneCd;
	dialog.open("selectPltDialog", "<u:msg titleId='pt.jsp.setPltStep2.add.plt' alt='포틀릿 추가'/>", url);
	<%// 팝업 닫힐때 초기화 - 선택된 id,rescId,rescNm %>
	dialog.onClose("selectPltDialog", function(){ gPltIds=[]; gPltRescIds=[]; gPltRescNms=[]; });
	
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
	var i, alreadys=[];
	for(i=1;i<=3;i++){
		$("#zoneCd"+i+" dd").each(function(){
			alreadys.push($(this).attr("id"));
		});
	}
	return alreadys;
}
<%// [팝업] 체크된 포틀릿의 데이터 배열 리턴 - value : id + rescId + rescNm %>
function getCheckedArray(areaId, noSelectMsgId){
	var arr = [], va, cnt=0;
	$("#"+areaId+" input[type='checkbox']:checked").each(function(){
		va = $(this).val();
		if(va!=''){
			arr.push({id:va, rescId:$(this).attr("data-rescId"), rescNm:$(this).attr("data-rescNm")});
		}
	});
	if(cnt==0 && noSelectMsgId!=null){
		alertMsg(noSelectMsgId);
	}
	return arr;
}
<%// [팝업] - 포틀릿선택 팝업에서 선택된것 모으는 배열, id, rescId, rescNm %>
var gPltIds = [];
var gPltRescIds = [];
var gPltRescNms = [];
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
			}
		});
	}
}
<%// [팝업] 포틀릿 선택 %>
function setCheckedPlt(zoneCd){
	updateCheckedPlt();
	if(gPltIds.length==0){
		alertMsg("cm.msg.noSelect");
	} else {
		var $zoneCd = $("#zoneCd"+zoneCd);
		var i, id, rescNm, rescId;
		<%// 이미 포함된 포틀릿ID 배열을 가져옴 %>
		var alreadys = getAlreadySetPlts();
		var maxCnt = parseInt('${pltPolc.maxSetupCnt}');
		var avblCnt = maxCnt - alreadys.length;
		for(i=0; i<gPltIds.length && i<avblCnt;i++){
			id = gPltIds[i];
			rescId = gPltRescIds[i];
			rescNm = gPltRescNms[i];
			$zoneCd.append('<dd id="'+id+'" data-rescId="'+rescId+'" class="text"><a href="javascript:togglePlt(\''+id+'\');">'+rescNm+'</a></dd>\r\n');
		}
		if(gPltIds.length > avblCnt){
			alertMsg("pt.jsp.setPltStep2.excd.maxCnt", ['${pltPolc.maxSetupCnt}']);<%//pt.jsp.setPltStep2.excd.maxCnt=설정 할 수 있는 포틀릿의 최대 갯수는 {0}개 입니다.%>
		}
	}
	dialog.close('selectPltDialog');
}
<%// 포틀릿 선택/선택해재 %>
function togglePlt(id){
	var $plt = $("#step2 #"+id);
	$plt.attr('class', $plt.attr('class')=='text' ? 'texton' : 'text');
}
<%// [버튼] 저장 %>
function savePlt(){
	for(var i=1;i<=3;i++) { collectPltData(i); }
	var $form = $("#step2");
	$form.attr("action","./transPltSetup.do");
	$form.attr("target","dataframe");
	$form.submit();
}
<%// 설정된 포틀릿 정보 모으기 %>
function collectPltData(no){
	var arr = [], param;
	$("#zoneCd"+no+" dd").each(function(){
		param = new ParamMap().put("pltId", $(this).attr("id")).put("rescId", $(this).attr("data-rescId"));
		arr.push(param.map);
	});
	$("#zoneData"+no).val(JSON.stringify(arr));
}
<%// 메뉴그룹 목록으로 돌아가기 %>
function goBack(){
	location.replace("${empty param.backTo ? './setMyMnu.do?menuId='.concat(menuId) : param.backTo}");
}
//-->
</script>

<u:title titleId="pt.jsp.setPltStep2.title" alt="포틀릿 설정 2단계" />

<u:boxArea className="gbox" outerStyle="height:595px;padding:9px 10px 0 10px;" innerStyle="NO_INNER_IDV">
<form id="step2" method="post">
<input type="hidden" name="menuId" value="${menuId}" /><c:if test="${not empty param.mnuGrpId}">
<input type="hidden" name="mnuGrpId" value="${param.mnuGrpId}" /></c:if><c:if test="${not empty param.backTo}">
<input type="hidden" name="backTo" value="${param.backTo}" /></c:if><c:if test="${not empty param.useMnu}">
<input type="hidden" name="useMnu" value="${param.useMnu}" /></c:if><c:if test="${not empty param.usePlt}">
<input type="hidden" name="usePlt" value="${param.usePlt}" /></c:if>
<input type="hidden" name="pltLoutCd" value="${param.pltLoutCd}" />
<input type="hidden" name="zoneData1" id="zoneData1" value="" />
<input type="hidden" name="zoneData2" id="zoneData2" value="" />
<input type="hidden" name="zoneData3" id="zoneData3" value="" />

	<c:forEach var="no" begin="1" end="${divCount}" step="1" varStatus="outerStatus">
	<u:convert srcId='subTitle${no}' var="divTitle" />
	<u:pltLayout outer="true" no="${no}" pltLoutCd="${param.pltLoutCd}">
	<u:pltLayout outer="false" no="${no}" pltLoutCd="${param.pltLoutCd}">
	
			<div class="pagetit">${divTitle}</div>
			<div class="pagediv">
				<dl id="zoneCd${no}">
				<u:convert srcId="ptPltSetupRVoList${no}" var="ptPltSetupRVoList" />
				<c:forEach items="${ptPltSetupRVoList}" var="ptPltSetupRVo" varStatus="status">
				<dd id="${ptPltSetupRVo.pltId}" data-rescId="${ptPltSetupRVo.rescId}" class="text"><a href="javascript:togglePlt('${ptPltSetupRVo.pltId}');">${ptPltSetupRVo.rescNm}</a></dd>
				</c:forEach>
				</dl>
			</div>

			<div class="blank_s"></div>

			<div class="front">
				<div class="front_left">
				<table border="0" cellpadding="0" cellspacing="0">
				<tr>
				<td class="frontico"><u:buttonIcon href="javascript:movePlt('${no}','up');" titleId="cm.btn.up" alt="위로이동" /></td>
				<td class="frontico"><u:buttonIcon href="javascript:movePlt('${no}','down');" titleId="cm.btn.down" alt="아래로이동" /></td>
				<c:if test="${not outerStatus.first}">
				<td class="frontico"><u:buttonIcon href="javascript:movePlt('${no}','left');" titleId="cm.btn.left" alt="왼쪽으로이동" /></td>
				</c:if>
				<c:if test="${not outerStatus.last}">
				<td class="frontico"><u:buttonIcon href="javascript:movePlt('${no}','right');" titleId="cm.btn.right" alt="오른쪽으로이동" /></td>
				</c:if>
				<td class="frontico"><u:buttonIcon href="javascript:delPlt('${no}');" titleId="cm.btn.del" alt="삭제" /></td>
				</tr>
				</table>
				</div>
				<div class="front_right">
				<table border="0" cellpadding="0" cellspacing="0">
				<tr>
				<td class="frontbtn"><u:buttonS href="javascript:addPlt('${no}');" titleId="pt.jsp.setPltStep2.add.plt" alt="포틀릿 추가" /></td>
				</tr>
				</table>
				</div>
			</div>
	
	</u:pltLayout>
	</u:pltLayout>
	</c:forEach>

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