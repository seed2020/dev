<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<%// 서브 메뉴 옵션 %>
var gSubMnuOption = null;
<%// 추가용 시퀀스 %>
var gSeq = 1;
<%// 카테고리 별 메뉴 그룹 영역 %>
var $gSubGrpArea = null;
<%// 소버튼 영역 %>
var $gSubBtnArea = null;
<%// 선택된 메뉴 위치 : top, main, sub %>
var gSelectedAreaId = '${loutCatId=="A" ? "adm" : "main"}';
<%// 선택된 메뉴 위치 : top, main, sub or folderId/menuGroupId %>
var gSelectedTextId = '${loutCatId=="A" ? "adm" : "main"}';
<%// 선택된 메뉴 종류 : cat(카테고리):상단메뉴,메인메뉴,서브메뉴 의 경우,  mnuLoutId(메뉴레이아웃ID):저장 된 것 읽은 경우,  seq(일련번호):저장 되기 전 임시 %>
var gSelectedType = 'cat';
<%// 레이아웃 카테고리 코드 - B:기본, A:관리자, M:모바일 %>
var gLoutCatId = '${loutCatId}';
<%// 회사별 지원언어 %>
var gLangs = "${langs}".split(",");
<%// 삭제된 목록 %>
var gDelList = [];
<%// 메뉴조합 구성 - 조합메뉴의 오른편에 들어갈 텍스트 %>
var gCombTitle = '<u:msg titleId="pt.jsp.setIconLout.combMnu" alt="메뉴조합 구성" />';
<%// [라디오] 아이콘 레이아웃 사용여부 - 에 따라서 하단 보여주기 토글 %>
function setArea(areaObj, yn){
	$("#useYnForm input[name='bascLoutUseYn']").val(yn);
	toggleArea(areaObj, yn);
}
<%// 하단 보여주기 토글 %>
function toggleArea(areaObj, yn){
	if(typeof areaObj == 'string'){
		if(yn=='Y'){
			$("#"+areaObj).show();
		} else {
			$("#"+areaObj).hide();
		}
	} else if(areaObj.each != null){
		areaObj.each(function(index,obj){
			if(yn=='Y'){
				$("#"+obj).show();
			} else {
				$("#"+obj).hide();
			}
		});
	}
}
<%// [라디오] 기본 레이아웃 사용여부 - 클릭 %>
function setSubMnuOpt(opt){<%// opt: M:독립된 메뉴로 사용, S:메인 메뉴의 하위 메뉴로 사용, N:서브 메뉴 사용 안함 %>
	if(opt=='M'){
		$gSubGrpArea.find("#subMnuArea").show();<%// 서브메뉴 - 구성 하는 곳 %>
	} else {
		$gSubGrpArea.find("#subMnuArea").hide();<%// 서브메뉴 - 구성 하는 곳 %>
	}
	gSubMnuOption = opt;
	displaySubBtn();
}
<%// 버튼의 보이기 조절[왼쪽 하단] - 폴더(추가/수정/삭제), 메뉴조합(추가/수정/삭제), 메뉴그룹삭제 %>
function displaySubBtn(){
	if(gSelectedType=='cat'){<%// 카테고리(상단메뉴,메인메뉴,서브메뉴)가 선택 된 경우 %>
		if(gSelectedAreaId=='main' && gSubMnuOption=='S'){<%// 메인메뉴 & 라디오[메인 메뉴의 하위 메뉴로 사용] 이 선택 된 경우 %>
			$gSubBtnArea.find("#btnAddFld, #btnAddComb").show();
			$gSubBtnArea.find("#btnModFld, #btnDelFld, #btnModComb, #btnDelComb, #btnDelGrp").hide();
		} else if(gLoutCatId == 'M'){
			$gSubBtnArea.find("#btnAddFld, #btnModFld, #btnDelFld, #btnAddComb, #btnModComb, #btnDelComb, #btnDelGrp").hide();
		} else {
			$gSubBtnArea.find("#btnAddComb").show();
			$gSubBtnArea.find("#btnAddFld, #btnModFld, #btnDelFld, #btnModComb, #btnDelComb, #btnDelGrp").hide();
		}
	} else {<%// 카테고리(상단메뉴,메인메뉴,서브메뉴)가 "아닌" 폴더,조합,메뉴그룹이 선택 된 경우 %>
		var $tbody = checkSelectedTbody();
		if($tbody!=null){
			var $tr = $tbody.find("td[class$='on']:first").parent();
			var mnuLoutKndCd = $tr.attr("data-mnuLoutKndCd");
			if(mnuLoutKndCd=='C'){<%// 메뉴 조합의 경우 %>
				$gSubBtnArea.find("#btnModFld, #btnDelFld, #btnAddComb, #btnAddFld, #btnDelGrp").hide();
				$gSubBtnArea.find("#btnModComb, #btnDelComb").show();
			} else if(mnuLoutKndCd=='F'){<%// 폴더의 경우 %>
				$gSubBtnArea.find("#btnModFld, #btnDelFld, #btnAddComb").show();
				$gSubBtnArea.find("#btnModComb, #btnDelComb, #btnAddFld, #btnDelGrp").hide();
			} else {<%// 메뉴그룹의 경우 %>
				$gSubBtnArea.find("#btnDelGrp").show();
				$gSubBtnArea.find("#btnAddFld, #btnModFld, #btnDelFld, #btnAddComb, #btnModComb, #btnDelComb").hide();
			}
		}
	}
}
<%// [메뉴 위치별 메뉴 그룹][텍스트 왼쪽의 아이콘이미지] - 상단 메뉴,메인 메뉴,서브 메뉴 왼쪽의 아이콘 - 해당 부분 디스플레이 토글 %>
function displayCat(area, openYn){
	var $area = $gSubGrpArea.find("#"+area+"AreaDetlArea");
	if($area.css("display")=="none" || openYn == 'Y'){
		$area.show();
		$gSubGrpArea.find("#"+area+"AreaImg").attr("src","${_cxPth}/images/${_skin}/ico_group_open.png");
	} else {
		$area.hide();
		$gSubGrpArea.find("#"+area+"AreaImg").attr("src","${_cxPth}/images/${_skin}/ico_group_close.png");
	}
}
<%// [메뉴 위치별 메뉴 그룹][텍스트] - 상단 메뉴,메인 메뉴,서브 메뉴 텍스트 - 해당 부분 보이게, 클릭된 부분 선택되도록 %>
function toggleCat(areaId){
	displayCat(areaId, 'Y');<%// 해당 하위 보이게 %>
	toggleItem(areaId, areaId, 'cat');
}
<%// 카테고리 별 메뉴 그룹 의 - 클릭된 메뉴그룹명 하일라이트 시킴 %>
function toggleItem(areaId, textId, typeId){
	var changed = false;
	if(typeId=='cat'){<%// 카테고리(상단메뉴,메인메뉴,서브메뉴)가 선택 된 경우 %>
		var $areaObj = $gSubGrpArea.find("#"+areaId+"AreaText");
		var clsNm = $areaObj.attr("class");
		if(clsNm!=null&& !clsNm.endsWith("_ton")){
			deHighlight();
			$gSubGrpArea.find("#"+areaId+"AreaText").attr("class", "text_ton");<%// 선택 - 하일라이트 처리 %>
			changed = true;
		}
	} else {
		var $areaObj = $gSubGrpArea.find("#"+textId+"DetlText");
		var clsNm = $areaObj.attr("class");
		if(clsNm!=null&& !clsNm.endsWith("_on")){
			deHighlight();
			$areaObj.attr("class", clsNm+"_on");<%// 선택 - 하일라이트 처리 %>
			changed = true;
		}
	}
	if(changed){
		gSelectedAreaId = areaId;
		gSelectedTextId = textId;
		gSelectedType = typeId;
		dialog.closeAll();
	}
	displaySubBtn();
}
<%// 메뉴 위치별 메뉴 그룹의 선택 해제 %>
function deHighlight(){
	if(gSelectedTextId!=null){<%// 예전에 선택된 메뉴 위치 - 하일라이트 해제 %>
		if(gSelectedType=='cat'){
			$gSubGrpArea.find("#"+gSelectedTextId+"AreaText").attr("class", "text");
		} else {
			var $areaObj = $gSubGrpArea.find("#"+gSelectedTextId+"DetlText");
			var clsNm = $areaObj.attr("class");
			if(clsNm!=null && clsNm.length>3 && clsNm.endsWith("_on")){
				$areaObj.attr("class", clsNm.substring(0,clsNm.length-3));
			}
		}
	}
	gSelectedAreaId = null;
	gSelectedTextId = null;
	gSelectedType = null;
}
<%// 선택 되어 있는 것이 있는지 체크 %>
function checkSelectedTbody(){
	if(gSelectedTextId==null){
		<%// pt.jsp.setIconLout.selectItemFirst="메뉴 위치별 메뉴 그룹"을 선택해 주십시요.%>
		alertMsg("pt.jsp.setIconLout.selectItemFirst",["#pt.jsp.setBascLout.mnuGrpByMnuLoc"]);
		return null;
	}
	return $gSubGrpArea.find("table#"+gSelectedAreaId+"AreaDetlArea tbody");
}
<%// 이미 추가되어진 메뉴그룹 목록 리턴 %>
function getAlreadys($tbody){
	var mnuGrpId, alreadys=[];
	if(gSelectedType=='cat'){
		$tbody.find("tr[data-subYn='N']").each(function(){
			mnuGrpId = $(this).attr("data-mnuGrpId");
			if(mnuGrpId!=null && mnuGrpId!='') alreadys.push(mnuGrpId);
		});
	} else {
		$tbody.find("tr[data-mnuLoutPid='"+gSelectedTextId+"']").each(function(){
			mnuGrpId = $(this).attr("data-mnuGrpId");
			if(mnuGrpId!=null && mnuGrpId!='') alreadys.push(mnuGrpId);
		});
	}
	return alreadys;
}

<%// [아이콘] - 홈일 경우 메뉴그룹 체크 %>
function isHomeMnuCheck(arrLength ){
	var catId = gSelectedAreaId == gSelectedTextId ? gSelectedAreaId : null;
	if(catId == 'home' && ((arrLength != null && arrLength > 0 ) || arrLength == null )){
		$tbody = $gSubGrpArea.find("table#"+catId+"AreaDetlArea tbody");
		$tr = $tbody.find("tr[data-subYn='N']");
		if($tr.length == 1){
			alertMsg("pt.jsp.setBascLout.homeMnu.needOne");<%//pt.jsp.setBascLout.homeMnu.needOne="홈 아이콘"은 하나의 메뉴그룹을 지정 해야 합니다.%>
			return false;
		}
	}
	return true;
};

<%// [아이콘] - 메뉴그룹 추가 <<, < %>
function addMnuGrp(mode){
	var $tbody = checkSelectedTbody();
	if($tbody==null) return;
	
	<%// 선택된 또는 전체의 메뉴그룹 배열 가지고 오기 %>
	var arr = getIframeContent('mnuGrpList').getCheckedList(mode);
	var buffer = [], first = false, extraAttr='', clsNm="htext", alreadys = getAlreadys($tbody);
	if(gSelectedAreaId == gSelectedTextId){<%// 카테고리에 더하는 경우 %>
		first = $tbody.children().length==0;
		extraAttr = ' data-mnuLoutPid=\"'+gSelectedTextId+'" data-subYn="N"';
	} else {<%// 카테고리 외의 경우 %>
		$tr = $tbody.find("td[class$='on']:first").parent();<%// jquery - $= : endsWith %>
		if($tr.attr("data-mnuLoutKndCd")!='F'){
			<%// pt.jsp.setBascLout.msg1='메뉴그룹'에 '메뉴그룹'을 추가 할 수 없습니다. ('카테고리' 또는 '폴더'에 추가 가능)%>
			alertMsg("pt.jsp.setBascLout.msg1");
			return;
		}
		extraAttr = ' data-mnuLoutPid=\"'+gSelectedTextId+'" data-subYn="Y"';
		clsNm="htexts";
	}
	
	//홈일 경우 메뉴그룹 추가시 갯수를 체크한다.
	if(!isHomeMnuCheck(arr.length)) return;
	
	arr.each(function(index, obj){<%// values in obj : mnuGrpId, rescId, rescNm %>
		if(!alreadys.contains(obj.mnuGrpId)){
			if(first){ first = false; }
			else { buffer.push('<tr><td class="line"></td></tr>'); }
			buffer.push('<tr data-seq="'+gSeq+'" data-mnuGrpId="'+obj.mnuGrpId+'" data-mnuGrpRescId="'+obj.rescId+'" data-mnuGrpRescNm="'+escapeValue(obj.rescNm)+'" data-mnuLoutKndCd="G"'+extraAttr+'>');
			buffer.push('<td id="'+gSeq+'DetlText" class="'+clsNm+'">');
			buffer.push('<a href="javascript:toggleItem(\''+gSelectedAreaId+'\',\''+gSeq+'\',\'seq\');">'+escapeValue(obj.rescNm)+'</a></td></tr>');
			gSeq++;
		}
	});
	if(buffer.length>0){
		if(gSelectedAreaId == gSelectedTextId){
			$tbody.append(buffer.join(''));
		} else {
			var $tr = $tbody.find("tr[data-mnuLoutPid='"+gSelectedTextId+"']:last");
			if($tr.length>0){
				$tr.after(buffer.join(''));
			} else {
				$tr = $tbody.find("td[class$='on']:first").parent();<%// jquery - $= : endsWith %>
				$tr.after(buffer.join(''));
			}
		}
	}
}
<%// [아이콘] - 메뉴그룹 삭제 >>, > %>
function removeMnuGrp(mode){
	var $tbody = checkSelectedTbody();
	if($tbody==null) return;
	<%// 상단메뉴,메인메뉴,서브메뉴 를 선택한 경우 전체 삭제 %>
	if(gSelectedAreaId!=null && gSelectedAreaId==gSelectedTextId){
		mode = 'all';
	}
	if(mode=='all'){<%// 전체삭제[>>] %>
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
		toggleCat(gSelectedAreaId);
	} else {<%// 선택삭제[>] %>
		var $tr = $tbody.find("td[class$='on']").parent();<%// jquery - $= : endsWith %>
		if($tr.length>0){
			
			var $subTrs=null, mnuLoutKndCd = $tr.attr('data-mnuLoutKndCd');
			if(mnuLoutKndCd=='C' || $tbody.find("tr[data-mnuLoutPid='"+gSelectedTextId+"'][data-mnuLoutKndCd='C']").length>0){
				<%// pt.cfm.del.related="{0}"의 선택된 {1}을 삭제 하시겠습니까 ?\n해당 {1}과 관련된 정보는 모두 삭제 됩니다.%>
				if(!confirmMsg("pt.cfm.del.related",["#pt.jsp.setIconLout.mnuGrpByIcon.subtitle","#cols.mnuComb"])){
					return;
				}
			}
			<%// 폴더면 하위목록 생성 %>
			if(mnuLoutKndCd=='F'){
				$subTrs = $tbody.find("tr[data-mnuLoutPid='"+gSelectedTextId+"']");
			}
			<%// 선택된것 삭제 목록에 추가 %>
			var mnuLoutId = $tr.attr("data-mnuLoutId");
			if(mnuLoutId!=null && mnuLoutId!=''){
				gDelList.push(mnuLoutId);<%// 삭제 목록 %>
			}
			<%// 서브 메뉴그룹/조합 삭제 목록에 추가 %>
			if($subTrs!=null){<%// 폴더 선택의 경우 폴더 하위의 목록 - 삭제목록에 더함 %>
				$subTrs.each(function(){
					mnuLoutId = $(this).attr("data-mnuLoutId");
					if(mnuLoutId!=null && mnuLoutId!=''){
						gDelList.push(mnuLoutId);<%// 삭제 목록 %>
					}
				});
			}
			
			<%// 다음 선택될 것을 찾음 - 다음것 있으면 다음것 없으면 이전것 %>
			var $next=null, mnuLoutPid = $tr.attr("data-mnuLoutPid");
			if(mnuLoutKndCd=='F' && $subTrs.length>0){<%// 폴더고 하위가 있을 경우 - 하위 마지막의 다음거, 없으면 이전거 %>
				$next = $($subTrs[$subTrs.length-1]).next().next();
				if($next.length==0) $next = $tr.prev().prev();
			} else if(mnuLoutPid!=null && mnuLoutPid!=''){<%// 폴더 밑에 있는 경우 - 하위 마지막의 다음거, 없으면 이전거 %>
				$next = $tr.next().next();
				if($next.length==0 || $next.attr("data-mnuLoutPid") != mnuLoutPid) $next = $tr.prev().prev();
			} else {
				$next = $tr.next().next();
				if($next.length==0) $next = $tr.prev().prev();
			}
			<%// 서브 메뉴그룹/조합 삭제 %>
			if($subTrs!=null){<%// 폴더 선택의 경우 폴더 하위의 목록 %>
				$subTrs.each(function(){
					if($(this).prev().length>0){
						$(this).prev().remove();
					} else {
						$(this).next().remove();
					}
					$(this).remove();
				});
			}
			<%// 서브 폴더/메뉴그룹/조합 삭제 %>
			var $next = $tr.next().next();<%// 삭제 후 선택 할 tr %>
			if($next.length==0) $next = $tr.prev().prev();
			<%// 선택된 tr과, 라인 tr을 지움 %>
			if($tr.prev().length>0){<%// tr 위 라인이 있으면 윗라인 삭제 %>
				$tr.prev().remove();
			} else {<%// tr 위 라인이 없으면 아랫라인 삭제 %>
				$tr.next().remove();
			}
			$tr.remove();<%// 선택된 tr 삭제 %>
			<%// 다음것 선택 %>
			if($next.length>0){
				var i, id, arr = ['mnuLoutId', 'seq'];
				for(i=0;i<arr.length;i++){
					id = $next.attr("data-"+arr[i]);
					if(id!=null && id!=''){
						toggleItem(gSelectedAreaId, id, arr[i]);
						break;
					}
				}
			} else {
				toggleCat(gSelectedAreaId);
			}
		}
	}
}
<%// [아이콘] 메뉴그룹 상하 이동 - (메뉴 위치별 메뉴 그룹) 우측 %>
function moveMnuGrp(direction){
	var $tbody = checkSelectedTbody();
	if($tbody==null) return;
	var $tr = $tbody.find("td[class$='on']").parent();<%// jquery - $= : endsWith %>
	
	if($tr.length>0){
		if(direction=='up'){
			<%// 이동 위치 계산 %>
			var $tgt = $tr.prev().prev();<%// 중간에 라인용 tr이 들어 감 %>
			if($tr.attr("data-subYn")=='N'){
				if($tgt.length>0 && $tgt.attr("data-subYn")=='Y'){<%//선택된것 폴더, 앞에거 폴더밑서브 일 경우 - 해당 폴더를 $prev에 설정 %>
					var mnuLoutPid = $tgt.attr("data-mnuLoutPid");
					$tgt = $tbody.find("tr[data-seq='"+mnuLoutPid+"'], tr[data-mnuLoutId='"+mnuLoutPid+"']");
				}
			} else {
				<%// 선택된 것은 1레벨이고 이동할 곳이 2레벨이면 서로 다른 폴더의 것이므로 이동 안함 %>
				if($tgt.attr("data-subYn")=='N') $tgt = null;
			}
			
			if($tgt!=null && $tgt.length>0){<%// 옮길 곳이 있으면 %>
				<%// 옮길 것 목록 - 폴더 하위 포함 %>
				var $line;
				$tbody.find("tr[data-mnuLoutPid='"+gSelectedTextId+"'], tr[data-seq='"+gSelectedTextId+"'], tr[data-mnuLoutId='"+gSelectedTextId+"']").each(function(){
					$line = $(this).prev();
					$tgt.before(this);
					$tgt.before($line[0]);
				});
			}
			
		} else if(direction=='down'){
			<%// 현재가 폴더면 마지막 서브 tr 구함 %>
			var $tgt = getLastSubTr($tbody, $tr);
			<%// 옮겨갈 곳의 마지막 서브 tr 구함 %>
			$tgt = getLastSubTr($tbody, $tgt.next().next());
			
			if($tgt!=null && $tgt.length>0){<%// 옮길 곳이 있으면 %>
				<%// 옮길 것 목록 - 폴더 하위 포함 %>
				var $line, moves=[];
				$tbody.find("tr[data-mnuLoutPid='"+gSelectedTextId+"'], tr[data-seq='"+gSelectedTextId+"'], tr[data-mnuLoutId='"+gSelectedTextId+"']").each(function(){
					moves.push($(this));
				});
				moves.reverse();
				moves.each(function(index, $obj){
					$line = $obj.next();
					$tgt.after($obj[0]);
					$tgt.after($line[0]);
				});
			}
		}
		
	}
}
<%// 폴더 하위의 마지막 tr 리턴 %>
function getLastSubTr($tbody, $tr){
	var $lastSub = null;
	if($tr.attr("data-mnuLoutKndCd")=='F'){
		var pid = $tr.attr("data-seq") != null ? $tr.attr("data-seq") : $tr.attr("data-mnuLoutId");
		$lastSub = $tbody.find("tr[data-mnuLoutPid='"+pid+"']:last");
	}
	
	if($lastSub!=null && $lastSub.length>0){
		return $lastSub;
	} else {
		return $tr;
	}
}
<%// [소버튼:메뉴조합추가/메뉴조합변경/메뉴조합삭제] - 좌하단 %>
function mngComb(mode){
	var $tbody = checkSelectedTbody();
	if($tbody==null) return;
	
	if(mode=='add'){<%// 추가 %>
		var $tr = $tbody.find("td[class$='on']").parent();
		if(gSelectedType!='cat' && $tr.attr("data-mnuLoutKndCd")!='F'){
			<%// pt.jsp.setBascLout.msg2='메뉴그룹'에 '메뉴조합'을 추가 할 수 없습니다. ('카테고리' 또는 '폴더'에 추가 가능)%>
			alertMsg("pt.jsp.setBascLout.msg2");
			return;
		}
		//홈일 경우 메뉴그룹 추가시 갯수를 체크한다.
		if(!isHomeMnuCheck(null)) return;
		dialog.open('setCombNmDialog', $("#btnAddComb").text(), './setCombNmPop.do?menuId=${menuId}&compId=${compId}&mode=add');
	} else if(mode=='mod' || mode=='del'){
		var $tr = $tbody.find("td[class$='on']").parent();<%// jquery - $= : endsWith %>
		var mnuLoutKndCd = $tr.attr('data-mnuLoutKndCd');
		if($tr.length==0){
			<%// pt.jsp.setIconLout.selectItemFirst="메뉴 위치별 메뉴 그룹"(을)를 선택해 주십시요..%>
			alertMsg("pt.jsp.setIconLout.selectItemFirst",["#pt.jsp.setBascLout.mnuGrpByMnuLoc"]);
		} else if(mnuLoutKndCd!='C'){
			<%// pt.jsp.setIconLout.noFld="메뉴 위치별 메뉴 그룹"에서 "폴더"을 선택해야 합니다.%>
			alertMsg("pt.jsp.setIconLout.noFld",["#pt.jsp.setBascLout.mnuGrpByMnuLoc"]);
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
			} else if(mode=='del'){<%// 삭제 %>
				removeMnuGrp('selected');
			}
		}
	}
}
<%// [소버튼:폴더추가/폴더변경/폴더삭제] - 좌하단 %>
function mngFld(mode){
	var $tbody = checkSelectedTbody();
	if($tbody==null) return;
	
	if(mode=='add'){<%// 추가 %>
		if(gSelectedType!='cat'){
			<%// pt.jsp.setBascLout.msg3='카테고리'에만 '폴더'을 추가 할 수 있습니다.%>
			alertMsg("pt.jsp.setBascLout.msg3");
			return;
		}
		dialog.open('setFldDialog', $("#btnAddFld").text(), './setFldPop.do?menuId=${menuId}&compId=${compId}&mode=add');
	} else if(mode=='mod' || mode=='del'){
		var $tr = $tbody.find("td[class$='on']").parent();<%// jquery - $= : endsWith %>
		var mnuLoutKndCd = $tr.attr('data-mnuLoutKndCd');
		if($tr.length==0){
			<%// pt.jsp.setIconLout.selectItemFirst="메뉴 위치별 메뉴 그룹"(을)를 선택해 주십시요..%>
			alertMsg("pt.jsp.setIconLout.selectItemFirst",["#pt.jsp.setBascLout.mnuGrpByMnuLoc"]);
		} else if(mnuLoutKndCd!='F'){
			<%// pt.jsp.setIconLout.noFld="메뉴 위치별 메뉴 그룹"에서 "폴더"를 선택해야 합니다.%>
			alertMsg("pt.jsp.setIconLout.noFld",["#pt.jsp.setBascLout.mnuGrpByMnuLoc"]);
		} else {
			if(mode=='mod'){<%// 수정 %>
				var rescId = $tr.attr('data-rescId');
				if(rescId!=null && rescId!='') rescId = "&mnuYn=N&rescId="+rescId;
				else {
					rescId = $tr.attr('data-mnuRescId');
					if(rescId!=null && rescId!='') rescId = "&mnuYn=Y&rescId="+rescId;
					else rescId = "";
				}
				dialog.open('setFldDialog', $("#btnModFld").text(), './setFldPop.do?menuId=${menuId}&compId=${compId}&mode=mod'+rescId);
				if(rescId==""){<%// 저장 안하고 화면 UI상에만 값이 있는 상태 - 스크립트로 value 세팅해 줌 %>
					var $pop = $("#setFldPop"), rescVa;
					gLangs.each(function(index, lang){
						rescVa = $tr.attr("data-rescVa_"+lang);
						$pop.find("input[name='rescVa_"+lang+"']").val(rescVa);
					});
				}
			} else if(mode=='del'){<%// 삭제 %>
				removeMnuGrp('selected');
			}
		}
	}
}
<%// [팝업:폴더추가/수정 - 저장 버튼] %>
function setFld(mode){
	if(validator.validate('setFldPop')){
		var $tbody = checkSelectedTbody();
		if($tbody==null) return;
		
		if(mode=='add'){
			var $pop = $("#setFldPop"), rescVa, curRescVa='';
			var buffer = [];
			<%// 자식 노드가 있으면 - 밑줄 추가 후 해당 데이터 넣기 %>
			if($tbody.children().length>0) { buffer.push('<tr><td class="line"></td></tr>'); }
			buffer.push('<tr data-seq="'+gSeq+'"');
			gLangs.each(function(index, lang){
				rescVa = $pop.find("input[name='rescVa_"+lang+"']").val();
				if(lang=='${_lang}') curRescVa=rescVa;
				buffer.push(' data-rescVa_'+lang+'="'+escapeValue(rescVa)+'"');
			});
			buffer.push(' data-mnuGrpRescNm="'+escapeValue(curRescVa)+'" data-mnuLoutKndCd="F" data-mnuLoutPid=\"'+gSelectedTextId+'" data-subYn="N">');
			buffer.push('<td id="'+gSeq+'DetlText" class="ftext">');
			buffer.push('<a href="javascript:toggleItem(\''+gSelectedAreaId+'\', \''+gSeq+'\', \'seq\');">');
			buffer.push(escapeValue(curRescVa)+'</a>');
			buffer.push('</td></tr>\n');
			$tbody.append(buffer.join(''));
			gSeq++;
		} else if(mode=='mod'){<%//수정의경우%>
			var $tr = $tbody.find("td[class$='on']").parent();<%// jquery - $= : endsWith %>
			var $pop = $("#setFldPop"), rescVa;
			gLangs.each(function(index, lang){
				rescVa = $pop.find("input[name='rescVa_"+lang+"']").val();
				$tr.attr("data-rescVa_"+lang, rescVa);
			});
			rescVa = $pop.find("input[name='rescVa_${_lang}']").val();
			if(rescVa!=null && rescVa!=''){
				$tr.find("a:first").text(rescVa);
			}
		}
		dialog.close('setFldDialog');
	}
}
<%// [팝업:메뉴조합추가/수정 - 저장 버튼] %>
function setCombNm(mode){
	if(validator.validate('setCombNmPop')){
		var $tbody = $gSubGrpArea.find("table#"+gSelectedAreaId+"AreaDetlArea tbody");
		if(mode=='add'){
			
			var extraAttr='', clsNm='htext', $lastTr;
			if(gSelectedAreaId == gSelectedTextId){<%// 카테고리에 더하는 경우 %>
				extraAttr = ' data-mnuLoutPid=\"'+gSelectedTextId+'" data-subYn="N"';
			} else {<%// 카테고리 외의 경우 %>
				var $tr = $tbody.find("td[class$='on']:first").parent();<%// jquery - $= : endsWith %>
				extraAttr = ' data-mnuLoutPid=\"'+gSelectedTextId+'" data-subYn="Y"';
				clsNm="htexts";
				$lastTr = $tbody.find("tr[data-mnuLoutPid='"+gSelectedTextId+"']:last");
				if($lastTr.length==0) $lastTr = $tr;
			}
			
			var $pop = $("#setCombNmPop"), rescVa, curRescVa='';
			var buffer = [];
			<%// 자식 노드가 있으면 - 밑줄 추가 후 해당 데이터 넣기 %>
			if($tbody.children().length>0) { buffer.push('<tr><td class="line"></td></tr>'); }
			buffer.push('<tr data-seq="'+gSeq+'"');
			gLangs.each(function(index, lang){
				rescVa = $pop.find("input[name='rescVa_"+lang+"']").val();
				if(lang=='${_lang}') curRescVa=rescVa;
				buffer.push(' data-rescVa_'+lang+'="'+escapeValue(rescVa)+'"');
			});
			buffer.push(' data-mnuGrpRescNm="'+escapeValue(curRescVa)+'" data-mnuLoutKndCd="C"'+extraAttr+'>');
			buffer.push('<td id="'+gSeq+'DetlText" class="'+clsNm+'">');
			buffer.push('<a href="javascript:toggleItem(\''+gSelectedAreaId+'\', \''+gSeq+'\', \'seq\');">');
			buffer.push(escapeValue(curRescVa)+'</a>');
			buffer.push('<a href="javascript:combMnu(\''+gSelectedAreaId+'\', \''+gSeq+'\', \'seq\')" style="margin-left:15px;">('+gCombTitle+')</a>');
			buffer.push('</td></tr>\n');
			if(gSelectedAreaId == gSelectedTextId){<%// 카테고리에 더하는 경우 %>
				$tbody.append(buffer.join(''));
			} else {<%// 카테고리 외의 경우 %>
				$lastTr.after(buffer.join(''));
			}
			gSeq++;
			//<tr><td class="ftext_on"><a href="javascript:">시스템</a></td></tr>
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
<%// [팝업] 메뉴조합 구성 %>
function combMnu(areaId, textId, typeId){
	toggleItem(areaId, textId, typeId);
	if(typeId!='mnuLoutId'){
		<%//pt.jsp.setIconLout.combMnuAfterSaving=저장 후 "{0}"을 사용해 주십시요.%>
		alertMsg("pt.jsp.setIconLout.combMnuAfterSaving",["#pt.jsp.setIconLout.combMnu"]);
	} else {
		dialog.open('setCombMnuPopDialog',gCombTitle,'./setCombMnuPop.do?menuId=${menuId}&compId=${compId}&loutCatId=${loutCatId}&mnuLoutId='+textId);
	}
}
<%// 임시ID(저장전)용 번호 리턴 - 메뉴조합 구성에서 사용 %>
function getNextSeq(){
	var seq = gSeq;
	gSeq++;
	return seq;
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
	var $form = $("#useYnForm");
	if(gLoutCatId!='B' || $form.find("input[name='bascLoutUseYn']").val()=='Y'){
		$form = $("#loutForm");
		<%	// gSubMnuOption: M:독립된 메뉴로 사용, S:메인 메뉴의 하위 메뉴로 사용, N:서브 메뉴 사용 안함
			// 저장할 카테고리 - 레이아웃위치코드 - 아이콘레이아웃(icon:아이콘), 기본레이아웃(top:상단, main:메인, sub:서브), 관리자레이아웃(adm:관리자)
		%>
		var catIds = gLoutCatId=='A' ? ['adm'] : gLoutCatId=='M' ? ['mobile','bottom'] : (gSubMnuOption == 'N') ? ['home','top','main'] : ['home','top','main','sub'];
		var arr = [], $tbody, lout, subLout, id, va, me, subMe;
		var subAttrs = ["mnuLoutId","mnuGrpId","mnuGrpRescId","mnuGrpRescNm","mnuLoutKndCd","rescId"];
		var $tr, valid = true;
		
		catIds.each(function(index, catId){
			$tbody = $gSubGrpArea.find("table#"+catId+"AreaDetlArea tbody");
			$tr = $tbody.find("tr[data-subYn='N']");
			if(gLoutCatId=='B' && catId=='home' && $tr.length != 1){
				alertMsg("pt.jsp.setBascLout.homeMnu.needOne");<%//pt.jsp.setBascLout.homeMnu.needOne="홈 아이콘"은 하나의 메뉴그룹을 지정 해야 합니다.%>
				valid = false;
				return false;
			}
			$tr.each(function(){
				me = this;
				lout = {loutLocCd:catId};
				subAttrs.each(function(index, attr){
					va = $(me).attr('data-'+attr);
					if(va!=null && va!='') lout[attr] = va;
				});
				gLangs.each(function(index, lang){
					va = $(me).attr('data-rescVa_'+lang);
					if(va!=null && va!='') lout['rescVa_'+lang] = va;
				});
				if(lout['mnuLoutKndCd']=='F'){
					id = $(me).attr('data-seq');
					if(id==null || id=='') id = $(me).attr('data-mnuLoutId');
					$tbody.find("tr[data-mnuLoutPid='"+id+"']").each(function(){
						subMe = this;
						subLout = {loutLocCd:catId};
						subAttrs.each(function(index, attr){
							va = $(subMe).attr('data-'+attr);
							if(va!=null && va!='') subLout[attr] = va;
						});
						gLangs.each(function(index, lang){
							va = $(subMe).attr('data-rescVa_'+lang);
							if(va!=null && va!='') subLout['rescVa_'+lang] = va;
						});
						if(lout['children']==null) lout['children'] = [];
						if(gSubMnuOption=='S'){<%// opt: M:독립된 메뉴로 사용, S:메인 메뉴의 하위 메뉴로 사용, N:서브 메뉴 사용 안함 %>
							lout['children'].push(subLout);
						} else {
							arr.push(subLout);
						}
					});
				}
				if(lout['mnuLoutKndCd']!='F' || gSubMnuOption=='S'){<%// opt: M:독립된 메뉴로 사용, S:메인 메뉴의 하위 메뉴로 사용, N:서브 메뉴 사용 안함 %>
					arr.push(lout);
				}
			});
		});
		if(valid){
			$form.find("#delList").val(gDelList.join(','));<%// 삭제 목록 %>
			$form.find("#dataString").val(JSON.stringify(arr));
			$form.attr('method','post');
			$form.attr('action','./${loutCatId=="A" ? "transAdmLout.do" : loutCatId=="M" ? "transMobileLout.do" : "transBascLout.do"}');
			$form.attr('target', 'dataframe');
			$form.submit();
		}
	} else {
		$form.attr('action','./${loutCatId=="A" ? "transAdmLout.do" : loutCatId=="M" ? "transMobileLout.do" : "transBascLout.do"}');
		$form.attr('target', 'dataframe');
		$form.submit();
	}
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setJsUniform($('#settingArea')[0]);
	
	<%// Javascript 전역 변수 초기화 %>
	$gSubGrpArea = $('#subGrpArea');
	$gSubBtnArea = $('#subBtnArea');
	setSubMnuOpt('${layout.subMnuOption}');
	if(gLoutCatId=='M') toggleCat('mobile');
});
//-->
</script>
<u:title alt="기본 / 관리자 / 모바일 레이아웃 설정" menuNameFirst="true" />

<form id="useYnForm">
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="bascLoutUseYn" value="${layout.bascLoutUseYn}" />
</form>

<form id="loutForm">
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="compId" value="${compId}" />
<input type="hidden" name="loutCatId" value="${loutCatId}" />
<input type="hidden" name="delList" value="" id="delList" />
<input type="hidden" name="activeMnuLoutId" value="" id="activeMnuLoutId" />
<input type="hidden" name="dataString" value="" id="dataString" />

<c:if test="${loutCatId == 'B'}">
<div id="settingArea">
<u:listArea>
	<tr id="settingOpt1">
		<td width="22%" class="head_lt"><u:msg titleId="pt.jsp.setBascLout.useYn" alt="기본 레이아웃 사용여부" /></td>
		<td class="bodybg_lt">
			<u:checkArea>
				<u:radio value="Y" name="bascLoutUseYn" alt="사용" onclick="setArea(['bascUseArea','settingOpt2','settingOpt3'], this.value)" checkValue="${layout.bascLoutUseYn}" titleId="cm.option.use" />
				<u:radio value="N" name="bascLoutUseYn" alt="사용안함" onclick="setArea(['bascUseArea','settingOpt2','settingOpt3'], this.value)" checkValue="${layout.bascLoutUseYn}" titleId="cm.option.notUse" />
			</u:checkArea>
		</td>
	</tr>
	<tr id="settingOpt2">
		<td class="head_lt"><u:msg titleId="pt.jsp.setBascLout.subMnuOptionDesc" alt="서브 메뉴 옵션" /></td>
		<td class="bodybg_lt">
			<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<u:radio value="M" name="subMnuOption" alt="독립된 메뉴로 사용" onclick="setSubMnuOpt(this.value)" checkValue="${layout.subMnuOption}" titleId="pt.jsp.setBascLout.subMnuOptionM" />
			</tr>
			<tr>
				<u:radio value="S" name="subMnuOption" alt="메인 메뉴의 하위 메뉴로 사용" onclick="setSubMnuOpt(this.value)" checkValue="${layout.subMnuOption}" titleId="pt.jsp.setBascLout.subMnuOptionS" />
			</tr>
			<tr>
				<u:radio value="N" name="subMnuOption" alt="서브 메뉴 사용 안함" onclick="setSubMnuOpt(this.value)" checkValue="${layout.subMnuOption}" titleId="pt.jsp.setBascLout.subMnuOptionN" />
			</tr>
			</table>
		</td>
	</tr>
	<tr id="settingOpt3">
		<td class="head_lt"><u:msg titleId="pt.jsp.setBascLout.mainMnuMaxCntByLang" alt="어권별 메인메뉴 최대 갯수" /></td>
		<td class="bodybg_lt">
			<table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
				<td>${langTypCdVo.rescNm}</td><td style="padding-right:15px;"><select name="${langTypCdVo.cd}MainMnuMaxCnt"><u:convertMap
					srcId="layout" attId="${langTypCdVo.cd}MainMnuMaxCnt" var="maxCnt" /><u:set
					test="${empty maxCnt}" var="maxCnt" value="10" elseValue="${maxCnt}" />
					<c:forEach begin="5" end="20" step="1" var="no"><u:option value="${no}" title="${no}" checkValue="${maxCnt}" /></c:forEach></select></td>
			</c:forEach>
			</tr>
			</table>
		</td>
	</tr>
</u:listArea>
</div>
</c:if>
</form>

<div id="bascUseArea" <c:if test="${layout.bascLoutUseYn == 'N' and loutCatId == 'B'}" >style="display:none"</c:if>>

<u:title titleId="pt.jsp.setBascLout.setMnuGrpByMnuLoc" alt="메뉴 위치별 메뉴 그룹 설정 " type="small" />

<u:boxArea className="wbox" style="float:left; width:36%;"
	outerStyle="height:430px; overflow:hidden;"
	innerStyle="width:96%; margin:0 auto 0 auto; padding:10px 0 0 0;" noBottomBlank="true" >
	
	<u:title titleId="pt.jsp.setBascLout.mnuGrpByMnuLoc" alt="메뉴 위치별 메뉴 그룹" type="small" >
		<u:titleIcon type="up" href="javascript:moveMnuGrp('up')" auth="A" />
		<u:titleIcon type="down" href="javascript:moveMnuGrp('down')" auth="A" />
	</u:title>
	
	<div id="subGrpArea" style="height:365px; overflow:auto">
	<c:forEach items="${areas}" var="area" varStatus="areaStatus"><u:convert
			srcId="${area}PtMnuLoutDVoList" var="ptMnuLoutDVoList" /><c:if
				test="${empty ptMnuLoutDVoList}"><u:set
				test="${area == 'main' or area == 'adm'}" var="areaImgOpen" value="open" elseValue="close" /><u:set
				test="${area == 'main' or area == 'adm'}" var="textStyle" value="text_ton" elseValue="text" /><u:set
				test="${area == 'main' or area == 'adm'}" var="detlStyle" value="" elseValue="display:none;"
			/></c:if><c:if
				test="${fn:length(ptMnuLoutDVoList) > 0}"><u:set
				test="${true}" var="areaImgOpen" value="open" elseValue="close" /><u:set
				test="${area == 'main' or area == 'adm'}" var="textStyle" value="text_ton" elseValue="text" /><u:set
				test="${true}" var="detlStyle" value="" elseValue="display:none;"
			/></c:if>
		
		<c:if test="${not areaStatus.first}"><div class="blank_s3"></div></c:if>
		<div id="${area}MnuArea">
		<table class="group_mu" border="0" cellpadding="0" cellspacing="0">
		<tbody>
		<tr>
		<td class="group_ico"><a href="javascript:displayCat('${area}');"><img id="${area}AreaImg" src="${_cxPth}/images/${_skin}/ico_group_${areaImgOpen}.png" width="24" height="22" /></a></td>
		<td id="${area}AreaText" class="${textStyle}"><a href="javascript:toggleCat('${area}');"><u:msg alt="상단메뉴 or 메인메뉴 or 서브메뉴" titleId="pt.jsp.setBascLout.${area}Mnu" /></a></td>
		</tr>
		</tbody>
		</table>
		<table id="${area}AreaDetlArea" class="group_smu" style="${detlStyle}" border="0" cellpadding="0" cellspacing="0">
		<tbody>
		<c:forEach items="${ptMnuLoutDVoList}" var="ptMnuLoutDVo" varStatus="status">
			<c:if test="${not status.first}"><tr><td class="line"></td></tr></c:if><u:set
				test="${ptMnuLoutDVo.mnuLoutKndCd == 'F'}" var="itemClass" value="ftext" elseValue="htext" /><u:set
				test="${area == 'main' and ptMnuLoutDVo.mnuLoutPid != 'main'}" var="itemClass" value="htexts" elseValue="${itemClass}" />
			<tr data-mnuLoutId="${ptMnuLoutDVo.mnuLoutId}" data-mnuGrpId="${ptMnuLoutDVo.mnuGrpId}" data-mnuLoutKndCd="${ptMnuLoutDVo.mnuLoutKndCd}"
				data-rescId="${ptMnuLoutDVo.rescId}" data-rescNm="<u:out value="${ptMnuLoutDVo.rescNm}" type="value" />"
				data-mnuGrpRescId="${ptMnuLoutDVo.mnuGrpRescId}" data-mnuGrpRescNm="<u:out value="${ptMnuLoutDVo.mnuGrpRescNm}" type="value" />"
				data-mnuLoutPid="${ptMnuLoutDVo.mnuLoutPid}" data-subYn="<u:set test="${area!=ptMnuLoutDVo.mnuLoutPid}" value="Y" elseValue="N" />"
				><td id="${ptMnuLoutDVo.mnuLoutId}DetlText" class="${itemClass}"><a href="javascript:toggleItem('${area}','${ptMnuLoutDVo.mnuLoutId}','mnuLoutId');"><u:out value="${ptMnuLoutDVo.rescNm}" nullValue="${ptMnuLoutDVo.mnuGrpRescNm}" /></a><c:if
				test="${ptMnuLoutDVo.mnuLoutKndCd == 'C'}"><a href="javascript:combMnu('${area}','${ptMnuLoutDVo.mnuLoutId}','mnuLoutId')" style="margin-left:15px;">(<u:msg titleId="pt.jsp.setIconLout.combMnu" alt="메뉴조합 구성" />)</a></c:if></td></tr>
		</c:forEach>
		</tbody>
		</table>
		</div>
	</c:forEach>
	</div>
	
	<u:buttonArea id="subBtnArea" style="margin-top:5px">
		<u:buttonS href="javascript:mngFld('add');" alt="폴더등록" id="btnAddFld" titleId="pt.jsp.setCd.regFld" auth="A" />
		<u:buttonS href="javascript:mngFld('mod');" alt="폴더수정" id="btnModFld" titleId="pt.jsp.setCd.modFld" auth="A" />
		<u:buttonS href="javascript:mngFld('del');" alt="폴더삭제" id="btnDelFld" titleId="pt.jsp.setCd.delFld" auth="A" style="display:none" />
		<u:buttonS href="javascript:mngComb('add');" alt="메뉴조합추가" id="btnAddComb" titleId="pt.jsp.setIconLout.addMnuComb" auth="A" />
		<u:buttonS href="javascript:mngComb('mod');" alt="메뉴조합수정" id="btnModComb" titleId="pt.jsp.setIconLout.modMnuComb" auth="A" />
		<u:buttonS href="javascript:mngComb('del');" alt="메뉴조합삭제" id="btnDelComb" titleId="pt.jsp.setIconLout.delMnuComb" auth="A" style="display:none" />
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
	
	<iframe id="mnuGrpList" name="mnuGrpList" src="./listMnuGrpFrm.do?menuId=${menuId}&compId=${compId}${mnuGrpMdParam}&uesYn=Y"
		style="width:100%; height:410px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
	
</u:boxArea>

</div>

<u:buttonArea topBlank="true">
	<u:button href="javascript:saveLout()" titleId="cm.btn.save" alt="저장" auth="A" />
</u:buttonArea>