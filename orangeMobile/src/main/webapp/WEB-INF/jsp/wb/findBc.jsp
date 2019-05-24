<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[
var searchParam = {selection:'${param.selection}', schCat:'bcNm',schWord:'${param.schWord}',detlViewType:'${empty param.detlViewType ? "bcList" : param.detlViewType}'};
$(document).ready(function() {
	var section = $("#section");
	$layout.tab.init(section.find('.tabarea2 dl'), section.find('#tabViewArea'), 'tabbtn');
	//if(bcListArea==null) bcListArea = $('#section').children('#tabViewArea').children('#bcArea').find('#bcListArea');
	for(var key in searchParam){ if(searchParam[key]=='') delete searchParam[key]; }
	//searchBc(new ParamMap(searchParam));
	tabSearch('bcList');
});

function tabSearch(detlViewType){
	var input = $("#searchArea input[name='schWord']");
	searchParam.detlViewType = detlViewType;
	if(input.val()==''){
		searchBc(new ParamMap(searchParam));
	}else{
		searchBc(new ParamMap(searchParam).extend({schWord:input.val()}));
	}
	
};
<%
// 사용자 검색 %>
var bcListArea = null;
function searchBc(param){
	$m.ajax('/wb/findBcAjx.do?menuId=${param.menuId}', param, function(html){
		if(searchParam.detlViewType != null && searchParam.detlViewType == 'bcOpenList') {
			bcListArea = $('#section').children('#tabViewArea').children('#bcOpenArea').find('#bcOpenListArea');
			$layout.tab.on('bcOpenArea');
		}else{
			bcListArea = $('#section').children('#tabViewArea').children('#bcArea').find('#bcListArea');
			$layout.tab.on('bcArea');
		}
		bcListArea.html(html);
	}, {mode:'HTML', async:true});
};<%
// 사용자 명 검색 %>
function searchBcByName(e){
	if(e==null || e.keyCode == 13){
		var input = $("#searchArea input[name='schWord']");
		if(input.val()==''){
			//$m.msg.alertMsg('cm.input.check.mandatory', ['#cols.schWord']);
			searchBc(new ParamMap(searchParam));
			input.blur();
		} else {
			searchBc(new ParamMap(searchParam).extend({schWord:input.val()}));
			input.blur();
		}
	}
}<%
// 확인 버튼 - 1명 %>
function setOneBc(){
	var ch = bcListArea.find('input:checked:first');
	var bcVo = ch.length==0 ? null : JSON.parse(ch.val());
	if(atndAdd(bcVo) != false){
		history.back();
	}
}<%
// 확인 버튼 - 여러명 %>
function setBcs(){
	addChecked();
	var arr = getSelected();
	if($m.nav.getWin(-1).atndAdd(arr.length==0 ? null : arr) != false){
		history.back();
	}
}<%
// [+] 더하기 버튼 %>
function addChecked(){
	var arr = [];
	bcListArea.find('input:checked').each(function(){
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
				if(oldItem.bcId==obj.bcId){
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
		area.append('<dd class="txt" onclick="highlightTouched(this)" data-org=\''+txt+'\'>'+(obj.bcNm==null ? 'NO-NAME' : obj.bcNm)+'</dd>');
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
	$m.ajax('${_cxPth}/wb/findBcAjx.do?menuId=${param.menuId}&pageNo='+pageNo, new ParamMap(searchParam), function(html){
		bcListArea.html(html);
	}, {mode:'HTML', async:true});
}
//]]>
</script>
	<header>
	<div class="unified_search" id="searchArea">
		<div class="back" onclick="history.back()"></div>
		<div class="searcharea">
			<dl>
				<dd class="unified_inputarea"><input type="text" class="unified_input" name="schWord" maxlength="20" onkeyup="searchBcByName(event)" /></dd>
				<dd class="unified_delete" onclick="$('#searchArea input').val('').focus();"></dd>
			</dl>
		</div>
		<div class="unified_search" onclick="searchBcByName(null);"></div>
	</div>
	</header>
	
	<section id="section">
		<div class="unified_tab">
		<div class="tabarea2">
			<dl>
			<dd class="tabsize2" onclick="tabSearch('bcList');"><div class="tabbtn" id="bcArea"><u:msg
				titleId="wb.jsp.findBcPop.tab.psnBc" alt="개인명함"/></div></dd>
			<dd class="line"></dd>
			<dd class="tabsize2" onclick="tabSearch('bcOpenList');"><div class="tabbtn_on" id="bcOpenArea"><u:msg
				titleId="wb.jsp.findBcPop.tab.publBc" alt="공개명함"/></div></dd>
			</dl>
		</div>
		</div>
		
		<div id="tabViewArea">
		
		<div id="bcArea" class="${param.selection=='single' ? 'unified_listarea2' : 'unified_listarea'}">
		<div class="listarea">
		<div class="blank5"></div>
		<article id="bcListArea">
		</article>
		</div>
		</div>
		<div id="bcOpenArea" class="${param.selection=='single' ? 'unified_listarea2' : 'unified_listarea'}">
		<div class="listarea">
		<div class="blank5"></div>
		<article id="bcOpenListArea">
		</article>
		</div>
		</div>
		</div>
		<c:if test="${param.selection == 'single'}">
		<div class="unified_btn2">
		<div class="btnarea">
			<div class="size">
			<dl>
				<dd class="btn" onclick="setOneBc()"><u:msg titleId="mcm.btn.ok" alt="확인" /></dd>
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
				<dd class="btn" onclick="setBcs()"><u:msg titleId="mcm.btn.ok" alt="확인" /></dd>
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