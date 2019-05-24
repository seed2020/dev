<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[
var searchParam = {selection:'${param.selection}', paramUserUid:'${param.paramUserUid}'};
var contentArea = null;
$(document).ready(function() {
	var section = $("#section");
	$layout.tab.init(section.find('.tabarea2 dl'), section.find('#tabViewArea'), 'tabbtn');
	for(var key in searchParam){ if(searchParam[key]=='') delete searchParam[key]; }
	
	contentArea = $('#section').children('#tabViewArea').find('#userGrpArea');
	grpSearch();
});

function grpSearch(){
	var input = $("#searchArea input[name='schWord']");
	if(input.val()==''){
		searchGrp(new ParamMap(searchParam));
	}else{
		searchGrp(new ParamMap(searchParam).extend({schWord:input.val()}));
	}
	
};
<%
// 그룹 검색 %>
function searchGrp(param){
	$m.ajax('/wc/findUserGrpAjx.do?menuId=${param.menuId}&fncCal=${param.fncCal}', param, function(html){
		contentArea.html(html);
	}, {mode:'HTML', async:true});
};<%
// 그룹 명 검색 %>
function searchGrpByName(e){
	if(e==null || e.keyCode == 13){
		var input = $("#searchArea input[name='schWord']");
		if(input.val()==''){
			//$m.msg.alertMsg('cm.input.check.mandatory', ['#cols.schWord']);
			searchGrp(new ParamMap(searchParam));
			input.blur();
		} else {
			searchGrp(new ParamMap(searchParam).extend({schWord:input.val()}));
			input.blur();
		}
	}
}<%
// 확인 버튼 - 1명 %>
function setOneGrp(){
	var ch = contentArea.find('input:checked:first');
	var bcVo = ch.length==0 ? null : JSON.parse(ch.val());
	if(atndAdd(bcVo) != false){
		history.back();
	}
}<%
// 확인 버튼 - 여러명 %>
function setGrps(){
	addChecked();
	var arr = getSelected();
	if(arr.length>0){
		var userGrpId = [];
		$.each(arr, function(index, obj){
			userGrpId.push(obj.userGrpId);
		});
		$m.ajax('/wc/listUserGrpDtlListAjx.do?menuId=${param.menuId}&fncCal=${param.fncCal}', {userGrpId:userGrpId.join(','), paramUserUid:'${param.paramUserUid}'}, function(data) {
			if(data.userInfoList==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				$m.msg.alertMsg('cm.msg.noData');
			}
			if(data.userInfoList!=null && $m.nav.getWin(-1).setUserGrpList(JSON.parse(data.userInfoList)) != false){
				history.back();
			}
		});
	}else{
		if($m.nav.getWin(-1).setUserGrpList(null) != false){
			history.back();
		}
	}
	
}<%
// [+] 더하기 버튼 %>
function addChecked(){
	var arr = [];
	contentArea.find('input:checked').each(function(){
		arr.push($(this).val());
		$ui.apply($(this).parent()[0], false);
	});
	if(arr.length>0){
		addToSelected(arr.reverse(), 'txt');
	}
}<%
// 선택항목에 더하기 %>
function addToSelected(arr, type){
	if(arr==null || arr.length==0) return;
	var obj, txt;
	
	var aleadyHas, aleadys = getSelected();
	
	var area = getSelectedListArea();
	var empty = area.find('dd:first').length==0;
	arr.each(function(index, item){
		if(type=='txt'){
			txt = item;
			obj = JSON.parse(item);
		} else {
			txt = JSON.stringify(item);
			obj = item;
		}
		
		if(type=='txt'){
			aleadyHas = false;
			aleadys.each(function(index, oldItem){
				if(oldItem.userGrpId==obj.userGrpId){
					aleadyHas = true;
					return false;
				}
			});
			if(aleadyHas) return;
		}
		
		if(index!=0 || !empty){
			area.append('<dd class="blank">l</dd>');
		}
		txt = txt.replace(/\'/g,"&apos;");
		area.append('<dd class="txt" onclick="highlightTouched(this)" data-org=\''+txt+'\'>'+(obj.rescNm==null ? 'NO-NAME' : obj.rescNm)+'</dd>');
	});
}<%
// [-] 빼기기 버튼 %>
function removeChecked(){
	var selected = getSelectedListArea().children("dd[class='txt_on']");
	if(selected.length>0){
		var prev = selected.prev();
		if(prev.attr('class')=='blank') prev.remove();
		selected.remove();
	}
}<%
// 선택항목 - obj 배열로 리턴 %>
function getSelected(){
	var arr = [];
	getSelectedListArea().children("dd[class!='blank']").each(function(){
		arr.push(JSON.parse($(this).attr('data-org')));
	});
	return arr;
}<%
// 선택 영역 리턴 %>
var selectedListArea = null;
function getSelectedListArea(){
	if(selectedListArea==null){
		selectedListArea = $('#section').children('#selectedArea').find('#selectedListArea');
	}
	return selectedListArea;
}<%
// 터치한것 선택 표시 %>
function highlightTouched(obj, isCurrBc){
	var area = getSelectedListArea();
	area.find('dd.txt_on').attr('class','txt');
	area.find('dd.txt_gon').attr('class','txt');
	if(obj!=null){
		if(isCurrBc==true) $(obj).attr('class','txt_gon');
		else $(obj).attr('class','txt_on');
	}
}<%
//페이징 %>
function goAjaxPage(pageNo){
	$m.ajax('${_cxPth}/wc/findUserGrpAjx.do?menuId=${param.menuId}&fncCal=${param.fncCal}&pageNo='+pageNo, new ParamMap(searchParam), function(html){
		contentArea.html(html);
	}, {mode:'HTML', async:true});
}
//]]>
</script>
	<header>
	<div class="unified_search" id="searchArea">
		<div class="back" onclick="history.back()"></div>
		<div class="searcharea">
			<dl>
				<dd class="unified_inputarea"><input type="text" class="unified_input" name="schWord" maxlength="20" onkeyup="searchGrpByName(event)" /></dd>
				<dd class="unified_delete" onclick="$('#searchArea input').val('').focus();"></dd>
			</dl>
		</div>
		<div class="unified_search" onclick="searchGrpByName(null);"></div>
	</div>
	</header>
	
	<section id="section">
		<div id="tabViewArea">
		
		<div class="${param.selection=='single' ? 'unified_listarea2' : 'unified_listarea'}" style="top:55px;">
			<div class="listarea">
				<div class="blank5"></div>
				<article id="userGrpArea">
				</article>
				</div>
			</div>
		</div>
		<c:if test="${param.selection == 'single'}">
		<div class="unified_btn2">
		<div class="btnarea">
			<div class="size">
			<dl>
				<dd class="btn" onclick="setOneGrp()"><u:msg titleId="mcm.btn.ok" alt="확인" /></dd>
			</dl>
			</div>
		</div>
		</div></c:if><c:if test="${param.selection != 'single'}">
		<div class="unified_btn">
		<div class="btnarea">
			<div class="size">
			<dl>
				<dd class="plus" onclick="addChecked()"></dd>
				<dd class="minus" onclick="removeChecked()"></dd>
				<dd class="btn" onclick="setGrps()"><u:msg titleId="mcm.btn.ok" alt="확인" /></dd>
			</dl>
			</div>
		</div>
		</div></c:if>
		<c:if
			
			test="${param.selection != 'single'}"><u:cmt cmt="선택한 사용자 모으는 영역" />
		<div class="unified_btm" id="selectedArea">
			<div class="unified_btm_icol"></div>
			<div class="unified_btm_icor"></div>
			<div class="unified_btmin">
				<dl id="selectedListArea"></dl>
			</div>
		</div>
		</c:if>
		
	</section>