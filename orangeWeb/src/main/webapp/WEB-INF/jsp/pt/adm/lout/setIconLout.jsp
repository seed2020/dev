<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<%// 최대 아이콘 인덱스 - 아이콘 추가용 %>
var gMaxIconIndex = 1;
<%// 선택된 아이콘 인덱스 - 카테고리 별 메뉴 그룹 관리에 사용 %>
var gSelectedIconIndex = null;
<%// 아이콘 그려지는 영역 %>
var $gIconArea = null;
<%// 아이콘 명 입력 영역 - 어권별 명 입력 %>
var $gLangTypArea = null;
<%// 카테고리 별 메뉴 그룹 영역 %>
var $gSubGrpArea = null;
<%// 소버튼 영역 %>
var $gSubBtnArea = null;
<%// 선택된 서브그룹 %>
var gSelectedSubGrp = {};
<%// 회사별 지원언어 %>
var gLangs = "${langs}".split(",");
<%// 임시 ID 체번용 - 토글에서 필요 %>
var gTempIdSeq = 1;
<%// 왼쪽메뉴(left), 오른쪽메뉴(right) - 중 선택 값 %>
var gCatAreaId = null;
<%// 삭제된 목록 %>
var gDelList = [];
<%// 메뉴조합 구성 - 조합메뉴의 오른편에 들어갈 텍스트 %>
var gCombTitle = '<u:msg titleId="pt.jsp.setIconLout.combMnu" alt="메뉴조합 구성" />';
<%// [소버튼: 추가/수정] 우상단 - 아이콘 선택할 수 있는 팝업 화면 open %>
function openIconPop(mode){
	if(mode=='mod' && gSelectedIconIndex==null){
		<%//pt.jsp.setIconLout.selectIconFirst=아이콘을 먼저 선택해 주십시요.%>
		alertMsg("pt.jsp.setIconLout.selectIconFirst");
		return;
	}
	dialog.open('setIconPopDialog','<u:msg titleId="pt.jsp.setIconLout.iconPop.title"/>','./setIconPop.do?menuId=${menuId}&mode='+mode);
	if(mode=='add'){
		var alreadys = getAlreadySetIcon(), clsNm;
		$("#chooseIconPop dd a").each(function(){
			clsNm = $(this).attr("class");
			if(alreadys.contains(clsNm)){
				$(this).attr("class", clsNm+"on");
			}
		});
	}
}
<%// [소버튼: 삭제] 우상단 - 아이콘 삭제 %>
function delIcon(){
	if(gSelectedIconIndex==null){
		<%//pt.jsp.setIconLout.selectIconFirst=아이콘을 먼저 선택해 주십시요.%>
		alertMsg("pt.jsp.setIconLout.selectIconFirst");
		return;
	}
	var $on = $gIconArea.find("div a[class$=on]");
	if($on.length>0 && confirmMsg("cm.cfrm.del")){<%//cm.cfrm.del=삭제하시겠습니까 ?%>
		$on.each(function(){<%// jquery - $= : endsWith %>
			var iconId = $(this).attr("class");
			iconId = iconId.substring(0, iconId.length-2);
			$(this).parent().remove();
			doIconRelated(iconId, 'del');
		});
	}
}
<%// [소버튼: 왼쪽으로이동/오른쪽으로이동] 우상단 - 아이콘을 좌우로 위치 변경 %>
function moveIcon(direction){
	$gIconArea.find("div a[class$=on]").each(function(){<%// jquery - $= : endsWith %>
		if(direction=='left'){
			var $prev = $(this).parent().prev();
			if($prev.length>0){
				$prev.before($(this).parent());
			}
		} else if(direction=='right'){
			var $next = $(this).parent().next();
			if($next.length>0){
				$next.after($(this).parent());
			}
		}
	});
}
<%// [팝업-아이콘 선택: 아이콘 클릭] - 아이콘 활성화/비활성화 함 %>
function activePopIcon(aObj, mode){
	if(mode=='add'){<%// 추가 %>
		var clsNm = $(aObj).attr("class");
		$(aObj).attr("class", clsNm.endsWith("on") ? clsNm.substring(0, clsNm.length-2) : clsNm+"on");
	} else if(mode=='mod'){<%// 변경 - 변경은 아이콘 클릭하면 바로 반영함 %>
		var clsNm = $(aObj).attr("class");
		if($gIconArea.find("a[class^="+clsNm+"]").length>0){
			<%//pt.jsp.setIconLout.alreadyAdded=이미 추가되어 있는 아이콘 입니다.%>
			alertMsg("pt.jsp.setIconLout.alreadyAdded");
		} else {
			$gIconArea.find("a[class$=on]:first").each(function(){
				var oldClsNm = $(this).attr("class");
				oldClsNm = oldClsNm.endsWith("on") ? oldClsNm.substring(0, oldClsNm.length-2) : oldClsNm;
				doIconRelated(clsNm, 'mod', oldClsNm);
				$(this).attr("class", clsNm+"on");
			});
			dialog.close('setIconPopDialog');
		}
	}
}
<%// [팝업-아이콘 선택: 확인버튼] - 선택된 아이콘을 세팅함 %>
function setIcons(){
	var arr = [], clsNm, alreadys = getAlreadySetIcon(), removeIds=[];
	$("#chooseIconPop dd a[class$=on]").each(function(){<%// jquery - $= : endsWith %>
		clsNm = $(this).attr("class");
		arr.push(clsNm.substring(0, clsNm.length-2));
	});
	<%// 삭제 된것 제거 %>
	alreadys.each(function(index, obj){
		if(!arr.contains(obj)){
			if($gIconArea.find("a."+obj).length>0){
				$gIconArea.find("a."+obj).parent().remove();
			} else {
				$gIconArea.find("a."+obj+"on").parent().remove();
			}
			removeIds.push(obj);
		}
	});
	<%// 추가 %>
	drawIcons(arr, getAlreadySetIcon(), 7);
	
	dialog.close('setIconPopDialog');
	<%// 삭제된 아이콘 후처리 %>
	removeIds.each(function(index, iconId){ doIconRelated(iconId,'del'); });
}
<%// 아이콘 더하기 - 선택된 아이콘 html을 아이콘 영역에 삽입 %>
function drawIcons(icons, alreadys, max, ids){
	var len = alreadys==null ? 0 : alreadys.length, mnuLoutId;
	icons.each(function(index, obj){
		if(alreadys==null || !alreadys.contains(obj)){
			if(max!=null && len>=max){
				<%// pt.jsp.setIconLout.maxCount=아이콘은 최대 {0}개 까지만 추가 가능 합니다.%>
				alertMsg("pt.jsp.setIconLout.maxCount", [max+""]);
				return false;
			}
			if(ids==null) mnuLoutId='';
			else mnuLoutId = ' data-mnuLoutId="'+ids[index]+'"';
			var cssFloat = (browser.ie && browser.ver<8) ? " float:left;" : "";
			$gIconArea.append('<div style="display:inline-block;'+cssFloat+' margin-right:5px;"><a href="javascript:void(0);" id="'+gMaxIconIndex+'"'+mnuLoutId+' onclick="activeIcon(this.className, this.getAttribute(\'id\'));" class="'+obj+'" onfocus="this.blur()"><span></span></a></div>');
			doIconRelated(obj, 'add');
			len++;
		}
	});
}
<%// 아이콘 더하기 후처리 - 아이콘과 관련된것 추가/변경/삭제 %>
function doIconRelated(iconId, mode, oldIconId){
	if(mode=='add'){
		<%// 아이콘 명 입력 - 추가 %>
		if($gLangTypArea.find("tr[data-imgKndVa='"+iconId+"']").length==0){<%// 해당 tr이 없으면 %>
			var $template = $gLangTypArea.find("tr:last");<%// 마지막 tr 은 아이콘 추가용 html 템플릿 %>
			var html = $template[0].outerHTML.replace(/NO/g, gMaxIconIndex);
			$template.before(html);
			var $prev = $template.prev();
			$prev.attr("data-imgKndVa", iconId);
			setJsUniform($prev[0]);
		}
		<%// 카테고리 별 메뉴 그룹 - 추가 %>
		if($gSubGrpArea.find("table[data-imgKndVa='"+iconId+"']").length==0){
			//var $template = $gSubGrpArea.find("table:last");<%// 마지막 tr 은 아이콘 추가용 html 템플릿 %>
			var $template = $gSubGrpArea.find("#subGrp_NO");<%// 마지막 tr 은 아이콘 추가용 html 템플릿 %>
			var html = $template[0].outerHTML.replace(/NO/g, gMaxIconIndex);
			$template.before(html);
			var $prev = $template.prev();
			$prev.attr("data-imgKndVa", iconId);
		}
		gMaxIconIndex++;
	} else if(mode=='mod'){
		<%// 아이콘 명 - 속성변경 : data-imgKndVa 를 선택한 iconId로 변경 %>
		$gLangTypArea.find("tr[data-imgKndVa='"+oldIconId+"']").attr("data-imgKndVa",iconId);
		<%// 카테고리 별 메뉴 그룹 - 속성변경 %>
		$gSubGrpArea.find("table[data-imgKndVa='"+oldIconId+"']").attr("data-imgKndVa",iconId);
		
	} else if(mode=='del'){
		<%// 아이콘 명 입력 - 삭제 %>
		$gLangTypArea.find("tr[data-imgKndVa='"+iconId+"']").remove();
		<%// 카테고리 별 메뉴 그룹 - 삭제 %>
		$gSubGrpArea.find("table[data-imgKndVa='"+iconId+"']").remove();
		<%// 선택된 아이콘 인덱스 - 지움 %>
		gSelectedIconIndex = null;
	}
	displaySubBtn();<%// 좌하단 소버튼의 보이기/숨기기 조절 %>
	if(browser.ie && browser.ver<8){<%// ie7 아이콘 중앙 배치 %>
		var gap = parseInt($gIconArea.width()/2,10) - ($gIconArea.children().length * 36);
		$gIconArea.css("padding-left", gap);
	}
}
<%// 좌하단 소버튼의 보이기/숨기기 조절 %>
function displaySubBtn(){
	var $tr = $gSubGrpArea.find("table#subGrp_"+gSelectedIconIndex+" td[class$=on]").parent();<%// jquery - $= : endsWith %>
	var mnuLoutKndCd = $tr.attr("data-mnuLoutKndCd");
	if(mnuLoutKndCd=='G'){
		$gSubBtnArea.find("#btnModComb, #btnDelComb").hide();
		$gSubBtnArea.find("#btnDelGrp").show();
	} else if(mnuLoutKndCd=='C'){
		$gSubBtnArea.find("#btnModComb, #btnDelComb").show();
		$gSubBtnArea.find("#btnDelGrp").hide();
	} else {
		$gSubBtnArea.find("#btnModComb, #btnDelComb, #btnDelGrp").hide();
	}
}
<%// 이미 세팅되어 있는 Icon 리턴 - 팝업용(중복 안되게 하기 위해) %>
function getAlreadySetIcon(){
	var arr = [], clsNm;
	$gIconArea.find("div a").each(function(){
		clsNm = $(this).attr("class");
		arr.push(clsNm.endsWith("on") ? clsNm.substring(0,clsNm.length-2) : clsNm);
	});
	return arr;
}
<%// [라디오] 아이콘 레이아웃 사용여부 - 에 따라서 하단 보여주기 토글 %>
function toggleArea(areaId, yn){
	if(yn=='Y'){
		$("#"+areaId).show();
	} else {
		$("#"+areaId).hide();
	}
}
<%// [아이콘 선택] - 아이콘 클릭 - 아이콘 활성화 함 %>
function activeIcon(clickedClsNm, iconIndex){
	gSelectedIconIndex = null;
	var clsNm, pureClsNm,  pureClickedClsNm = clickedClsNm.endsWith("on") ? clickedClsNm.substring(0, clickedClsNm.length-2) : clickedClsNm;
	$gIconArea.find("a").each(function(){
		clsNm = $(this).attr("class");
		pureClsNm = clsNm.endsWith("on") ? clsNm.substring(0, clsNm.length-2) : clsNm;
		if(pureClsNm==pureClickedClsNm){<%// 선택 활성화%>
			if(clsNm == pureClsNm){
				$(this).attr("class", pureClsNm+"on");<%// 아이콘 활성화 %>
				gSelectedIconIndex = iconIndex;<%// 아이콘 인덱스 저장 %>
				activeIconRelated(pureClsNm);<%// 아이콘명, 카테고리 별 권한그룹 - 아이콘에 해당하는 항목으로 display 함 %>
				$("#activeMnuLoutId").val($(this).attr("data-mnuLoutId"));<%//활성화된 메뉴레이아웃ID 세팅 %>
				$("#activeLoutLocCd").val("");<%//활성화된 지역 세팅 %>
			}
		} else {<%// 선택 비활성화%>
			if(clsNm != pureClsNm){
				$(this).attr("class", pureClsNm);
			}
		}
	});
	displaySubBtn();<%// 좌하단 소버튼의 보이기/숨기기 조절 %>
	if(clickedClsNm!=""){
		activeArea(null);<%// 좌측,우측 메뉴 영역 초기화 %>
	}
	
}
<%// [왼쪽/오른쪽 - 사이드 영역 선택] %>
function activeArea(areaId){
	gCatAreaId = areaId;
	var areas = ['home','left','right'], $area;
	if(areaId=='home' || areaId=='left' || areaId=='right'){
		areas.each(function(index, eachArea){
			$area = $("#"+eachArea+"Area");
			if(areaId==eachArea){
				$area.find("a:first").show();<%//on 이미지%>
				$area.find("a:last").hide();
			} else {
				$area.find("a:first").hide();<%//on 이미지%>
				$area.find("a:last").show();
			}
		});
		
		activeIcon("",null);
		activeIconRelated(null, areaId);
		gSelectedIconIndex = null;
		$("#activeMnuLoutId").val("");<%//활성화된 메뉴레이아웃ID 세팅 %>
		$("#activeLoutLocCd").val(areaId);<%//활성화된 지역 세팅 %>
	} else {
		areas.each(function(index, eachArea){
			$area = $("#"+eachArea+"Area");
			$area.find("a:first").hide();<%//on 이미지%>
			$area.find("a:last").show();
		});
	}
}
<%// [아이콘 선택 - 후처리] - [아이콘명] 입력부분, [카테고리 별 메뉴 그룹]의 목록 부분을 아이콘에 해당하는 것으로 변경 %>
function activeIconRelated(iconId, areaId){
	if(iconId!=null){
		<%// 아이콘명 입력박스 - 아이콘에 해당하는 입력박스 보여주기 %>
		$gLangTypArea.find("tr").each(function(){
			if($(this).attr("data-imgKndVa")==iconId){
				$(this).show();
			} else {
				$(this).hide();
			}
		});
		<%// 카테고리 별 메뉴 그룹 - 해당 그룹 보여주기 %>
		$gSubGrpArea.find("table").each(function(){
			if($(this).attr("data-imgKndVa")==iconId){
				$(this).show();
			} else {
				$(this).hide();
			}
		});
	} else {
		<%// 아이콘명 입력박스 - 아이콘에 해당하는 입력박스 보여주기 %>
		$gLangTypArea.find("tr").each(function(){
			$(this).hide();
		});
		<%// 카테고리 별 메뉴 그룹 - 해당 그룹 보여주기 %>
		var areaTabId = "subGrp_"+areaId;
		$gSubGrpArea.find("table").each(function(){
			if($(this).attr("id")==areaTabId){
				$(this).show();
			} else {
				$(this).hide();
			}
		});
	}
	
	dialog.closeAll();
}
<%// [카테고리 별 메뉴 그룹]의 클릭된 메뉴그룹명 하일라이트 시킴 %>
function toggleSub(toggleCatId, id){
	if(gSelectedIconIndex!=null || gCatAreaId!=null){
		var areaId = gSelectedIconIndex==null ? gCatAreaId : gSelectedIconIndex;
		$gSubGrpArea.find("table#subGrp_"+areaId+" tr").each(function(){
			if($(this).attr("data-"+toggleCatId)==id){
				$(this).find("a").parent().attr("class", "groupsmu_text_on");
			} else {
				$(this).find("a").parent().attr("class", "groupsmu_text");
			}
		});
		dialog.close('setCombNmDialog');<%// 메뉴조합 추가/수정%>
		dialog.close('setCombMnuPopDialog');<%// 메뉴조합 구성%>
	}
	displaySubBtn();
}
<%// 아이콘명 입력 체크 안할 조건 - u:input.tag 에서 사용 %>
function skipper(id){
	return id.endsWith("NO");
}
<%// 해당 어권의 명이 입력되지 않았을 경우 해당 어권을 보이게 함 %>
function changeLangSelectorIcon(id, va){
	if(va==''){
		var iconIndex = id.substring(id.lastIndexOf('_')+1);
		var imgKndVa = $gIconArea.find("a#"+iconIndex).attr("class");
		activeIcon(imgKndVa, iconIndex);<%// 해당 아이콘 활성화 %>
		
		var langSelector = $gLangTypArea.find('#langSelector_'+iconIndex);
		var nm = $("#langTypArea #"+id).attr("name");
		langSelector.val(nm.substring(nm.length-2));
		langSelector.trigger('click');
	}
}

<%// [아이콘] - 홈일 경우 메뉴그룹 체크 %>
function isHomeMnuCheck(arrLength ){
	var areaId = gSelectedIconIndex==null ? gCatAreaId : gSelectedIconIndex;
	if(areaId == 'home' && ((arrLength != null && arrLength > 0 ) || arrLength == null )){
		var homeMnuArrs = [];
		$gSubGrpArea.find("table#subGrp_"+areaId+" tbody tr").each(function(){
			if($(this).attr('data-line')!='Y'){
				homeMnuArrs.push($(this));
			}
		});
		if(homeMnuArrs.length == 1){
			alertMsg("pt.jsp.setBascLout.homeMnu.needOne");<%//pt.jsp.setBascLout.homeMnu.needOne="홈 아이콘"은 하나의 메뉴그룹을 지정 해야 합니다.%>
			return false;
		}
	}
	return true;
};

<%// [아이콘] - 메뉴그룹 추가 [<<], [<] %>
function addMnuGrp(mode){
	if(gSelectedIconIndex==null && gCatAreaId==null){
		<%//pt.jsp.setIconLout.selectIconOrAreaFirst=아이콘 또는 카테고리를 먼저 선택해 주십시요.%>
		alertMsg("pt.jsp.setIconLout.selectIconOrAreaFirst");
		return;
	}
	<%// 선택된 또는 전체의 메뉴그룹 배열 가지고 오기 %>
	var arr = getIframeContent('mnuGrpList').getCheckedList(mode);
	var areaId = gSelectedIconIndex==null ? gCatAreaId : gSelectedIconIndex;
	var buffer, $tbody = $gSubGrpArea.find("table#subGrp_"+areaId+" tbody");
	
	//홈일 경우 메뉴그룹 추가시 갯수를 체크한다.
	if(!isHomeMnuCheck(arr.length)) return;
	
	arr.each(function(index, obj){<%// value in obj : mnuGrpId, rescId, rescNm %>
		if($tbody.find("tr[data-mnuGrpId="+obj.mnuGrpId+"]").length==0){<%// 해당 메뉴그룹이 없으면 - 추가함 %>
			buffer = [];
			buffer.push('<tr data-mnuLoutId="" data-mnuGrpId="'+obj.mnuGrpId+'" data-mnuGrpRescId="'+obj.rescId+'" data-mnuGrpRescNm="'+escapeValue(obj.rescNm)+'" data-mnuLoutKndCd="G">');
			buffer.push('<td class="groupsmu_text">');
			buffer.push('<a href="javascript:toggleSub(\'mnuGrpId\', \''+obj.mnuGrpId+'\');">');
			buffer.push(escapeValue(obj.rescNm)+'</a></td></tr>\n');
			buffer.push('<tr data-line="Y"><td class="groupsmu_line"></td></tr>\n');
			$tbody.append(buffer.join(''));
		}
	});
}
<%// [아이콘] - 메뉴그룹 삭제 [>>], [>] %>
function removeMnuGrp(mode){
	if(gSelectedIconIndex==null && gCatAreaId==null){
		<%//pt.jsp.setIconLout.selectIconOrAreaFirst=아이콘 또는 카테고리를 먼저 선택해 주십시요.%>
		alertMsg("pt.jsp.setIconLout.selectIconOrAreaFirst");
		return;
	}
	var areaId = gSelectedIconIndex==null ? gCatAreaId : gSelectedIconIndex;
	var $tbody = $gSubGrpArea.find("table#subGrp_"+areaId+" tbody");
	if(mode=='all'){<%// 전체삭제: [>>] %>
		$combs = $tbody.find("tr[data-mnuLoutKndCd='C']");
		if($combs.length>0){
			<%// pt.cfm.del.related="{0}"의 선택된 {1}을 삭제 하시겠습니까 ?\n해당 {1}과 관련된 정보는 모두 삭제 됩니다.%>
			if(!confirmMsg("pt.cfm.del.related",["#pt.jsp.setIconLout.mnuGrpByIcon.subtitle","#cols.mnuComb"])){
				return;
			}
			var mnuLoutId;
			$combs.each(function(){
				mnuLoutId = $(this).attr("data-mnuLoutId");
				if(mnuLoutId!=null && mnuLoutId!=''){
					gDelList.push(mnuLoutId);<%// 삭제 목록 %>
				}
			});
		}
		$tbody.find("tr").remove();
	} else {<%// 선택삭제: [>] %>
		var $tr = $tbody.find("td[class$='on']").parent();<%// jquery - $= : endsWith %>
		if($tr.length>0){
			var mnuLoutKndCd = $tr.attr('data-mnuLoutKndCd');
			if(mnuLoutKndCd=='C'){<%// 메뉴조합일 경우%>
				<%// pt.cfm.del.related="{0}"의 선택된 {1}을 삭제 하시겠습니까 ?\n해당 {1}과 관련된 정보는 모두 삭제 됩니다.%>
				if(!confirmMsg("pt.cfm.del.related",["#pt.jsp.setIconLout.mnuGrpByIcon.subtitle","#cols.mnuComb"])){
					return;
				}
			}
			var mnuLoutId = $tr.attr("data-mnuLoutId");
			if(mnuLoutId!=null && mnuLoutId!=''){
				gDelList.push(mnuLoutId);<%// 삭제 목록 %>
			}
			<%// 다음 선택될 것을 찾음 - 다음것 있으면 다음것 없으면 이전것 %>
			var $next = $tr.next().next();
			if($next.length==0) $next = $tr.prev().prev();
			<%// 선택된 tr과, 라인 tr을 지움 %>
			$tr.next().remove();
			$tr.remove();
			if($next.length>0){
				var i, id, arr = ['mnuLoutId', 'mnuGrpId', 'tempId'];
				for(i=0;i<arr.length;i++){
					id = $next.attr("data-"+arr[i]);
					if(id!=null && id!=''){
						toggleSub(arr[i], id);
						break;
					}
				}
			}
		}
	}
}
<%// [아이콘][카테고리 별 메뉴 그룹 - 우측 상하 이동] - 메뉴그룹을 상하로 이동 %>
function moveMnuGrp(direction){
	if(gSelectedIconIndex==null && gCatAreaId==null){
		<%//pt.jsp.setIconLout.selectIconFirst=아이콘을 먼저 선택해 주십시요.%>
		alertMsg("pt.jsp.setIconLout.selectIconFirst");
		return;
	}
	var areaId = gSelectedIconIndex==null ? gCatAreaId : gSelectedIconIndex;
	var $tbody = $gSubGrpArea.find("table#subGrp_"+areaId+" tbody");
	var $tr = $tbody.find("td[class$='on']").parent();<%// jquery - $= : endsWith %>
	if($tr.length>0){
		if(direction=='up'){
			var $prev = $tr.prev();
			var $pprev = $prev.prev();
			if($pprev.length>0){
				$prev.after($pprev[0]);
				$prev.before($tr[0]);
			}
		} else if(direction=='down'){
			var $next = $tr.next();
			var $nnext = $next.next();
			if($nnext.length>0){
				$next.before($nnext[0]);
				$next.after($tr[0]);
			}
		}
	}
}
<%// [소버튼: 메뉴조합추가/메뉴조합변경/메뉴조합삭제] - 좌하단 %>
function mngComb(mode){
	if(gSelectedIconIndex==null && gCatAreaId==null){
		<%//pt.jsp.setIconLout.selectIconFirst=아이콘을 먼저 선택해 주십시요.%>
		alertMsg("pt.jsp.setIconLout.selectIconFirst");
		return;
	}
	
	if(mode=='add'){<%//추가%>
		//홈일 경우 메뉴그룹 추가시 갯수를 체크한다.
		if(!isHomeMnuCheck(null)) return;
		dialog.open('setCombNmDialog', $("#btnAddComb").text(), './setCombNmPop.do?menuId=${menuId}&compId=${compId}&mode=add');
	} else if(mode=='mod' || mode=='del'){
		var areaId = gSelectedIconIndex==null ? gCatAreaId : gSelectedIconIndex;
		var $tbody = $gSubGrpArea.find("table#subGrp_"+areaId+" tbody");
		var $tr = $tbody.find("td[class$='on']").parent();<%// jquery - $= : endsWith %>
		var mnuLoutKndCd = $tr.attr('data-mnuLoutKndCd');
		if($tr.length==0){
			<%// pt.jsp.setIconLout.selectItemFirst="카테고리 별 메뉴 그룹"을 선택해 주십시요.%>
			alertMsg("pt.jsp.setIconLout.selectItemFirst",["#pt.jsp.setIconLout.mnuGrpByIcon.subtitle"]);
		} else if(mnuLoutKndCd!='C'){
			<%// pt.jsp.setIconLout.noComb="카테고리 별 메뉴 그룹"에서 "메뉴조합"을 선택해야 합니다. %>
			alertMsg("pt.jsp.setIconLout.noComb",["#pt.jsp.setIconLout.mnuGrpByIcon.subtitle"]);
		} else {
			if(mode=='mod'){<%// 수정 %>
				var rescId = $tr.attr('data-rescId');
				if(rescId!=null && rescId!='') rescId = "&rescId="+rescId;
				else rescId = "";
				dialog.open('setCombNmDialog', $("#btnModComb").text(), './setCombNmPop.do?menuId=${menuId}&compId=${compId}&mode=mod'+rescId);
				if(rescId==""){<%// 저장 안하고 화면 UI상에만 값이 있는 상태 - 스크립트로 value 세팅해 줌 %>
					var $pop = $("#setCombNmPop"), rescVa;
					gLangs.each(function(index, lang){
						rescVa = $tr.attr("data-rescVa_"+lang);
						$pop.find("input[name='rescVa_"+lang+"']").val(rescVa);
					});
				}
			} else if(mode=='del'){<%//삭제 %>
				removeMnuGrp('selected');
			}
		}
	}
}
<%// [팝업-메뉴조합추가: 저장버튼] %>
function setCombNm(mode){
	if(validator.validate('setCombNmPop')){
		var areaId = gSelectedIconIndex==null ? gCatAreaId : gSelectedIconIndex;
		var $tbody = $gSubGrpArea.find("table#subGrp_"+areaId+" tbody");
		if(mode=='add'){
			var $pop = $("#setCombNmPop"), rescVa, curRescVa='';
			var buffer = [];
			buffer.push('<tr data-tempId="'+gTempIdSeq+'"');
			gLangs.each(function(index, lang){
				rescVa = $pop.find("input[name='rescVa_"+lang+"']").val();
				if(lang=='${_lang}') curRescVa=rescVa;
				buffer.push(' data-rescVa_'+lang+'="'+escapeValue(rescVa)+'"');
			});
			buffer.push(' data-mnuGrpRescNm="'+escapeValue(curRescVa)+'" data-mnuLoutKndCd="C">');
			buffer.push('<td class="groupsmu_text">');
			buffer.push('<a href="javascript:toggleSub(\'tempId\', \''+gTempIdSeq+'\');">');
			buffer.push(escapeValue(curRescVa)+'</a>');
			buffer.push('<a href="javascript:combMnu(\'tempId\',\''+gTempIdSeq+'\')" style="margin-left:15px;">('+gCombTitle+')</a>');
			buffer.push('</td></tr>\n');
			buffer.push('<tr data-line="Y"><td class="groupsmu_line"></td></tr>\n');
			$tbody.append(buffer.join(''));
			gTempIdSeq++;
		} else if(mode=='mod'){<%//수정의경우%>
			var $tr = $tbody.find("td[class$='on']").parent();<%// jquery - $= : endsWith %>
			var $pop = $("#setCombNmPop"), rescVa;
			gLangs.each(function(index, lang){
				rescVa = $pop.find("input[name='rescVa_"+lang+"']").val();
				$tr.attr("data-rescVa_"+lang, rescVa);
			});
			rescVa = $pop.find("input[name='rescVa_${_lang}']").val();//$tr.attr("data-rescVa_${_lang}");
			if(rescVa!=null && rescVa!=''){
				$tr.find("a:first").text(rescVa);
			}
		}
		dialog.close('setCombNmDialog');
	}
}
<%// 임시ID(저장전)용 번호 리턴 - 메뉴조합 구성에서 사용 %>
function getNextSeq(){
	var seq = gTempIdSeq;
	gTempIdSeq++;
	return seq;
}
<%// 메뉴조합 구성 %>
function combMnu(combCatId, id){
	toggleSub(combCatId, id);
	if(combCatId!='mnuLoutId'){
		<%//pt.jsp.setIconLout.combMnuAfterSaving=저장 후 "{0}"을 사용해 주십시요.%>
		alertMsg("pt.jsp.setIconLout.combMnuAfterSaving",["#pt.jsp.setIconLout.combMnu"]);
	} else {
		dialog.open('setCombMnuPopDialog',gCombTitle,'./setCombMnuPop.do?menuId=${menuId}&compId=${compId}&loutCatId=I&'+combCatId+'='+id);
	}
}
<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='input:radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
}
<%// [버튼] 저장 클릭 %>
function saveLout(){
	var $form = $("#useYnForm"), valid;
	if(!$form.find("#icoLoutUseYnN")[0].checked){
		<%// 아이콘명 입력 확인 %>
		if(!validator.validate('langTypArea')){ return; }
		var arr = [], subAttrs = ["mnuLoutId","mnuGrpId","mnuGrpRescId","mnuGrpRescNm","mnuLoutKndCd","rescId"];
		$gIconArea.find("a").each(function(){
			collecteJsonData(this, "icon", arr, subAttrs);
		});
		collecteJsonData(null, "left", arr, subAttrs);
		collecteJsonData(null, "right", arr, subAttrs);
		valid = collecteJsonData(null, "home", arr, subAttrs);
		if(!valid) return;
		
		$form = $("#loutForm");
		$form.find("#delList").val(gDelList.join(','));<%// 삭제 목록 %>
		$form.find("#dataString").val(JSON.stringify(arr));
		$form.attr('method','post');
		$form.attr('action','./transIconLout.do');
		$form.attr('target', 'dataframe');
		$form.submit();
		
	} else {
		$form.attr('action','./transIconLout.do');
		$form.attr('target', 'dataframe');
		$form.submit();
	}
}
<%// json 데이터로 데이터 변환 %>
function collecteJsonData(obj, loutLocCd, arr, subAttrs){
	var data, id, subLout, va, me, areaId;
	if(obj!=null){
		var iconIndex = $(obj).attr('id');
		var mnuLoutId = $(obj).attr('data-mnuLoutId');
		var rescId = $gLangTypArea.find("tr#langTyp_"+iconIndex+" input[id^='rescId']").val();
		var imgKndVa = $(obj).attr('class');
		if(imgKndVa!=null && imgKndVa.endsWith("on")) imgKndVa = imgKndVa.substring(0, imgKndVa.length-2);
		data = {mnuLoutId:mnuLoutId, imgKndVa:imgKndVa, rescId:rescId, loutLocCd:loutLocCd, rescs:[], subs:[]};
		arr.push(data);
		$gLangTypArea.find("tr#langTyp_"+iconIndex+" input[id^='rescVa']").each(function(){
			id = $(this).attr('id');
			data.rescs.push({langTypCd:id.substring(id.indexOf('_')+1, id.lastIndexOf('_')), rescVa:$(this).val()});
		});
		areaId = iconIndex;
	} else {
		data = {loutLocCd:loutLocCd, subs:[]};
		arr.push(data);
		areaId = loutLocCd;
	}
	
	<%// tr의 추가 컬럼에서 데이터를 꺼내서 sub에 세팅함 %>
	$gSubGrpArea.find("table#subGrp_"+areaId+" tbody tr").each(function(){
		if($(this).attr('data-line')!='Y'){
			me = this;
			subLout = {};
			subAttrs.each(function(index, attr){
				va = $(me).attr('data-'+attr);
				if(va!=null && va!='') subLout[attr] = va;
			});
			gLangs.each(function(index, lang){
				va = $(me).attr('data-rescVa_'+lang);
				if(va!=null && va!='') subLout['rescVa_'+lang] = va;
			});
			data.subs.push(subLout);
		}
	});
	if(loutLocCd=='home' && data.subs.length != 1){
		alertMsg("pt.jsp.setBascLout.homeMnu.needOne");<%//pt.jsp.setBascLout.homeMnu.needOne="홈 아이콘"은 하나의 메뉴그룹을 지정 해야 합니다.%>
		return false;
	}
	return true;
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setJsUniform($('#useYnForm')[0]);
	$("#langTypArea").children("tr[id!='langTyp_NO']").each(function(){
		setJsUniform(this);
	});
	<%// Javascript 전역 변수 초기화 %>
	$gIconArea = $("#iconArea");
	$gLangTypArea = $('#langTypArea');
	$gSubGrpArea = $('#subGrpArea');
	$gSubBtnArea = $('#subBtnArea');
	<%// 아이콘 그리기 - Javascript 로 그림 %>
	<c:if test="${not empty imgKndVas}" >drawIcons("${imgKndVas}".split(','), null, null, "${mnuLoutIds}".split(','));</c:if>
	<%// 초기 선택되는 아이콘 %>
	<c:if test="${not empty activeImgKndVa}" >activeIcon('${activeImgKndVa}','${activeImgSeq}');</c:if>
	<c:if test="${not empty param.loutLocCd}" >activeArea('${param.loutLocCd}');</c:if>
});
//-->
</script>

<u:title titleId="pt.jsp.setIconLout.title" alt="아이콘 레이아웃 설정" menuNameFirst="true" />

<form id="useYnForm">
<input type="hidden" name="menuId" value="${menuId}" />
<u:listArea>
	<tr>
		<td width="22%" class="head_lt"><u:msg titleId="pt.jsp.setIconLout.useYn" alt="아이콘 레이아웃 사용여부" /></td>
		<td class="bodybg_lt">
			<u:checkArea>
				<u:radio value="Y" name="icoLoutUseYn" alt="사용" onclick="toggleArea('iconUseArea', this.value)" checkValue="${layout.icoLoutUseYn}" titleId="cm.option.use" />
				<u:radio value="N" name="icoLoutUseYn" alt="사용안함" onclick="toggleArea('iconUseArea', this.value)" checkValue="${layout.icoLoutUseYn}" titleId="cm.option.notUse" />
			</u:checkArea>
		</td>
	</tr>
</u:listArea>
</form>

<form id="loutForm">
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="compId" value="${compId}" />
<input type="hidden" name="loutCatId" value="I" />
<input type="hidden" name="icoLoutUseYn" value="Y" />
<input type="hidden" name="delList" value="" id="delList" />
<input type="hidden" name="activeMnuLoutId" value="" id="activeMnuLoutId" />
<input type="hidden" name="activeLoutLocCd" value="" id="activeLoutLocCd" />
<input type="hidden" name="dataString" value="" id="dataString" />
</form>

<div id="iconUseArea"<c:if test="${layout.icoLoutUseYn == 'N'}" > style="display:none"</c:if>>
<u:title titleId="pt.jsp.setIconLout.selectIcon.subtitle" alt="카테고리 관리(아이콘)" type="small" >
	<u:titleButton href="javascript:openIconPop('add');" alt="추가" titleId="cm.btn.add" auth="A"
	/><u:titleButton href="javascript:openIconPop('mod');" alt="수정" titleId="cm.btn.mod" auth="A"
	/><u:titleButton href="javascript:delIcon();" alt="삭제" titleId="cm.btn.del" auth="A"
	/><u:titleButton href="javascript:moveIcon('left');" alt="왼쪽으로이동" titleId="cm.btn.left" auth="A"
	/><u:titleButton href="javascript:moveIcon('right');" alt="오른쪽으로이동" titleId="cm.btn.right" auth="A" />
</u:title>

<u:listArea>
	<tr>
	<td colspan="2">
	<div id="header_${_skin}" style="text-align:center; height:73px; background:#FFFFFF; position:relative;">
		<div style="width:10%; height:66px; text-align:left; position:absolute; left:0px; top:4px;"
			><div id="homeArea" style="padding-left:4px;">
			<a href="javascript:void(0);" style="display:none;"><img src="${_cxPth}/images/${_skin}/header_icon/homemu.gif"></a>
			<a href="javascript:void(0);" onclick="activeArea('home');" ><img src="${_cxPth}/images/${_skin}/header_icon/homemu_on.gif"></a></div></div>
		<div style="width:15%; height:66px; text-align:left; position:absolute; left:10%; top:4px; border:1 solid red"
			><div id="leftArea" style="padding-left:4px; border:1 solid blue">
			<a href="javascript:void(0);" style="display:none;"><img src="${_cxPth}/images/${_skin}/header_icon/leftmu.png"></a>
			<a href="javascript:void(0);" onclick="activeArea('left');" ><img src="${_cxPth}/images/${_skin}/header_icon/leftmu_on.png"></a></div></div>
		<div style="width:60%; height:66px; text-align:center; position:absolute; left:25%; top:4px;"
			><div id="iconArea" style="width:100%;"></div></div>
		<div style="width:15%; height:66px; text-align:right; position:absolute; left:85%; top:4px;"
			><div id="rightArea" style="padding-right:4px;">
			<a href="javascript:void(0);" style="display:none;"><img src="${_cxPth}/images/${_skin}/header_icon/rightmu.png"></a>
			<a href="javascript:void(0);" onclick="activeArea('right');"><img src="${_cxPth}/images/${_skin}/header_icon/rightmu_on.png"></a></div></div>
	</div>
	</td>
	</tr>
	<tr>
		<td width="18%" class="head_lt"><u:mandatory></u:mandatory><u:msg titleId="pt.jsp.setIconLout.iconNm" alt="아이콘 명" /></td>
		<td class="bodybg_lt" style="height:24px;">
		<form>
		<table border="0" cellpadding="0" cellspacing="0">
			<tbody id="langTypArea">
		<c:forEach items="${ptMnuLoutDVoList}" var="ptMnuLoutDVo" varStatus="status">
			<u:set test="${status.last}" var="iconIndex" value="NO" elseValue="${status.index + 1}" />
			<tr id="langTyp_${iconIndex}" data-imgKndVa="${ptMnuLoutDVo.imgKndVa}" style="display:none">
			<td>
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="langStatus">
			<u:convert srcId="${ptMnuLoutDVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
			<u:set test="${langStatus.first}" var="style" value="" elseValue="display:none;" />
			<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
			<u:input id="rescVa_${langTypCdVo.cd}_${iconIndex}" name="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="pt.cols.icoNm" value="${rescVa}" style="${style}"
				maxByte="200" validator="changeLangSelectorIcon(id, va)" mandatory="Y" skipper="skipper(id)" />
			</c:forEach>
			</td>
			<td>
			<c:if test="${fn:length(_langTypCdListByCompId)>1}">
				<select id="langSelector_${iconIndex}" onchange="changeLangTypCd('langTypArea','langTyp_${iconIndex}', this.value)" <u:elemTitle titleId="cols.langTyp" />>
				<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="langStatus">
				<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
				</c:forEach>
				</select>
			</c:if>
			<u:input type="hidden" id="rescId_${iconIndex}" name="rescId" value="${ptMnuLoutDVo.rescId}" />
			</td>
			</tr>
			
		</c:forEach>
			</tbody>
		</table>
		</form>
		</td>
	</tr>
</u:listArea>

<u:boxArea className="wbox" style="float:left; width:36%;"
	outerStyle="height:430px; overflow:hidden;"
	innerStyle="width:96%; margin:0 auto 0 auto; padding:10px 0 0 0;" noBottomBlank="true" >
	
	<u:title titleId="pt.jsp.setIconLout.mnuGrpByIcon.subtitle" alt="카테고리 별 메뉴 그룹" type="small" >
		<u:titleIcon type="up" href="javascript:moveMnuGrp('up')" auth="A" />
		<u:titleIcon type="down" href="javascript:moveMnuGrp('down')" auth="A" />
	</u:title>
	
	<div id="subGrpArea" class="div_border" style="height:365px; overflow:auto">
	<c:forEach items="${ptMnuLoutDVoList}" var="ptMnuLoutDVo" varStatus="outerStatus">
		<u:set test="${outerStatus.last}" var="iconIndex" value="NO" elseValue="${outerStatus.index + 1}"
		/><u:convert srcId="sub_${ptMnuLoutDVo.mnuLoutId}" var="subPtMnuLoutDVoList" />
		<table id="subGrp_${iconIndex}" data-imgKndVa="${ptMnuLoutDVo.imgKndVa}" style="width:100%; display:none;" border="0" cellpadding="0" cellspacing="0">
			<tbody>
			<c:forEach items="${subPtMnuLoutDVoList}" var="subPtMnuLoutDVo" varStatus="status"><u:set
				test="${not empty subPtMnuLoutDVo.mnuGrpRescNm}" var="rescNm" value="${subPtMnuLoutDVo.mnuGrpRescNm}" elseValue="${subPtMnuLoutDVo.rescNm}" />
			<tr data-mnuLoutId="${subPtMnuLoutDVo.mnuLoutId}" data-mnuGrpId="${subPtMnuLoutDVo.mnuGrpId}" data-mnuLoutKndCd="${subPtMnuLoutDVo.mnuLoutKndCd}"
				data-rescId="${subPtMnuLoutDVo.rescId}" data-rescNm="<u:out value="${subPtMnuLoutDVo.rescNm}" type="value" />"
				data-mnuGrpRescId="${subPtMnuLoutDVo.mnuGrpRescId}" data-mnuGrpRescNm="<u:out value="${subPtMnuLoutDVo.mnuGrpRescNm}" type="value" />"
				><td class="groupsmu_text"><a href="javascript:toggleSub('mnuLoutId','${subPtMnuLoutDVo.mnuLoutId}');"><u:out value="${rescNm}" /></a><c:if
				test="${subPtMnuLoutDVo.mnuLoutKndCd == 'C'}"><a href="javascript:combMnu('mnuLoutId','${subPtMnuLoutDVo.mnuLoutId}')" style="margin-left:15px;">(<u:msg titleId="pt.jsp.setIconLout.combMnu" alt="메뉴조합 구성" />)</a></c:if></td></tr>
			<tr data-line="Y"><td class="groupsmu_line"></td></tr>
			</c:forEach>
			</tbody>
		</table>
	</c:forEach>
	<c:forEach items="${areaCds}" var="areaCd" varStatus="outerStatus">
	<u:convert srcId="sub_${areaCd}" var="subPtMnuLoutDVoList" />
		<table id="subGrp_${areaCd}" style="width:100%; display:none;" border="0" cellpadding="0" cellspacing="0">
			<tbody>
			<c:forEach items="${subPtMnuLoutDVoList}" var="subPtMnuLoutDVo" varStatus="status"><u:set
				test="${not empty subPtMnuLoutDVo.mnuGrpRescNm}" var="rescNm" value="${subPtMnuLoutDVo.mnuGrpRescNm}" elseValue="${subPtMnuLoutDVo.rescNm}" />
			<tr data-mnuLoutId="${subPtMnuLoutDVo.mnuLoutId}" data-mnuGrpId="${subPtMnuLoutDVo.mnuGrpId}" data-mnuLoutKndCd="${subPtMnuLoutDVo.mnuLoutKndCd}"
				data-rescId="${subPtMnuLoutDVo.rescId}" data-rescNm="<u:out value="${subPtMnuLoutDVo.rescNm}" type="value" />"
				data-mnuGrpRescId="${subPtMnuLoutDVo.mnuGrpRescId}" data-mnuGrpRescNm="<u:out value="${subPtMnuLoutDVo.mnuGrpRescNm}" type="value" />"
				><td class="groupsmu_text"><a href="javascript:toggleSub('mnuLoutId','${subPtMnuLoutDVo.mnuLoutId}');"><u:out value="${rescNm}" /></a><c:if
				test="${subPtMnuLoutDVo.mnuLoutKndCd == 'C'}"><a href="javascript:combMnu('mnuLoutId','${subPtMnuLoutDVo.mnuLoutId}')" style="margin-left:15px;">(<u:msg titleId="pt.jsp.setIconLout.combMnu" alt="메뉴조합 구성" />)</a></c:if></td></tr>
			<tr data-line="Y"><td class="groupsmu_line"></td></tr>
			</c:forEach>
			</tbody>
		</table>
	</c:forEach>
	</div>
	
	<u:buttonArea id="subBtnArea" style="margin-top:5px">
		<u:buttonS href="javascript:mngComb('add');" alt="메뉴조합추가" id="btnAddComb" titleId="pt.jsp.setIconLout.addMnuComb" auth="A" />
		<u:buttonS href="javascript:mngComb('mod');" alt="메뉴조합수정" id="btnModComb" titleId="pt.jsp.setIconLout.modMnuComb" auth="A" />
		<u:buttonS href="javascript:mngComb('del');" alt="메뉴조합삭제" id="btnDelComb" titleId="pt.jsp.setIconLout.delMnuComb" auth="A" />
		<u:buttonS href="javascript:removeMnuGrp('selected');" alt="메뉴그룹삭제" id="btnDelGrp" titleId="pt.jsp.setIconLout.delMnuGrp" auth="A" style="display:none" />
	</u:buttonArea>
</u:boxArea>

<div style="float:left; width:4%; text-align:center; margin:175px 0 0 0;">
	<table style="margin:0 auto 0 auto;" border="0" cellpadding="0" cellspacing="0">
		<tr><td><a href="javascript:addMnuGrp('all');"<u:elemTitle titleId="cm.btn.allAdd" alt="전체추가" type="image" />><img src="${_cxPth}/images/${_skin}/ico_allleft.png" width="20" height="20" /></a></td></tr>
		<tr><td class="height5"></td></tr>
		<tr><td><a href="javascript:addMnuGrp('selected');"<u:elemTitle titleId="cm.btn.selAdd" alt="선택추가" type="image" />><img src="${_cxPth}/images/${_skin}/ico_left.png" width="20" height="20" /></a></td></tr>
		<tr><td class="height5"></td></tr>
		<tr><td><a href="javascript:removeMnuGrp('selected');"<u:elemTitle titleId="cm.btn.selDel" alt="선택삭제" type="image" />><img src="${_cxPth}/images/${_skin}/ico_right.png" width="20" height="20" /></a></td></tr>
		<tr><td class="height5"></td></tr>
		<tr><td><a href="javascript:removeMnuGrp('all');"<u:elemTitle titleId="cm.btn.allDel" alt="전체삭제" type="image" />><img src="${_cxPth}/images/${_skin}/ico_allright.png" width="20" height="20" /></a></td></tr>
	</table>
</div>

<u:boxArea className="wbox" style="float:right; width:60%;"
	outerStyle="height:430px;overflow:hidden;"
	innerStyle="width:98%; margin:0 auto 0 auto; padding:10px 0 0 0;" noBottomBlank="true" >
	
	<iframe id="mnuGrpList" name="mnuGrpList" src="./listMnuGrpFrm.do?menuId=${menuId}&compId=${compId}&uesYn=Y"
		style="width:100%; height:410px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
	
</u:boxArea>

</div>

<u:buttonArea topBlank="true">
	<u:button href="javascript:saveLout()" titleId="cm.btn.save" alt="저장" auth="A" />
</u:buttonArea>