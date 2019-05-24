<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[<%
// -- searchParam --
// selection		- single:한명선택, multi:여러명 선택
// userStatCd		- 02:근무중
// downward			- 하위 부서 포함(부서ID)
// oneDeptId		- 해당 부서만(부서ID)
%>
var searchParam = {selection:'${param.selection}', userStatCd:'${param.userStatCd}', downward:'${param.downward}', oneDeptId:'${param.oneDeptId}', mdRid:'${not empty param.apvLnOpt ? "AP" : ""}', global:'${param.global}'};
var apvLnOpt = "${param.apvLnOpt}";
$(document).ready(function() {
	var section = $("#section");
	$layout.tab.init(section.find('.tabarea2 dl'), section.find('#tabViewArea'), 'tabbtn');
	if(userListArea==null) userListArea = $('#section').children('#tabViewArea').children('#userArea').find('#userListArea');
	for(var key in searchParam){ if(searchParam[key]=='') delete searchParam[key]; }
	<%// 자기 부서 사용자 조회 %>
	searchUser(new ParamMap(searchParam).extend({orgId:'${sessionScope.userVo.orgId}'}));
	<%// 선택 영역에 사용자 세팅 %>
	if($m.user.selected != null){
		if(apvLnOpt=='') addToSelected($m.user.selected);
		else addApvrToSelected($m.user.selected, 'init');
	}
	<%// 트리 초기화 %>
	initTree();
});<%
// 사용자 검색 %>
var userListArea = null;
function searchUser(param){
	$m.ajax('${_cxPth}/or/user/selectUserAjx.do', param, function(html){
		$layout.tab.on('userArea');
		userListArea.html(html);
	}, {mode:'HTML', async:true});
}<%
// 사용자 명 검색 %>
function searchUserByName(e){
	if(e==null || e.keyCode == 13){
		var input = $("#searchArea input[name='userNm']");
		if(input.val()==''){
			//$m.msg.alertMsg('cm.input.check.mandatory', ['#cols.userNm']);
			searchUser(new ParamMap(searchParam).extend({orgId:'${sessionScope.userVo.orgId}'}));
			input.blur();
		} else {
			searchUser(new ParamMap(searchParam).extend({userNm:input.val()}));
			input.blur();
		}
	}
}<%
// 확인 버튼 - 1명 %>
function setOneUser(){
	var ch = userListArea.find('input:checked:first');
	var userVo = ch.length==0 ? null : JSON.parse(ch.val());
	if($m.user.callback(userVo) != false){
		history.back();
	}
}<%
// 확인 버튼 - 여러명 %>
function setUsers(){
	addChecked();
	var arr = getSelected();
	if($m.user.callback(arr.length==0 ? null : arr) != false){
		history.back();
	}
}<%
// [+] 더하기 버튼 %>
function addChecked(dblApvTypCd){
	var arr = [];
	userListArea.find('input:checked').each(function(){
		arr.push($(this).val());
		$ui.apply($(this).parent()[0], false);
	});
	if(arr.length>0){
		if(apvLnOpt=='') addToSelected(arr.reverse(), 'txt');
		else addApvrToSelected(arr.reverse(), 'txt', dblApvTypCd);
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
				if(obj.userUid==null){
					if(oldItem.deptId==obj.deptId){
						aleadyHas = true;
						return false;
					}
				} else {
					if(oldItem.userUid==obj.userUid){
						aleadyHas = true;
						return false;
					}
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
function highlightTouched(obj, isCurrUser){
	var area = getSelectedListArea();
	area.find('dd.txt_on').attr('class','txt');
	area.find('dd.txt_gon').attr('class','txt');
	if(obj!=null){
		if(isCurrUser==true) $(obj).attr('class','txt_gon');
		else $(obj).attr('class','txt_on');
	}
}<%
// 트리용 Object %>
var tree = null;
function initTree(){
	tree = TREE.create('orgTree');
	tree.onclick = 'onTreeClick';
	tree.setIconTitle('${iconTitle}');
	tree.setRoot('ROOT', '<u:msg titleId="or.label.orgRootName" alt="조직도"/>');
	tree.setSkin("${_skin}");
	
	$m.ajax('${_cxPth}/or/user/treeOrgAjx.do', {downward:'${param.downward}', oneDeptId:'${param.oneDeptId}', global:'${param.global}'}, function(data){
		if(data.openLvl != null) tree.openLvl = data.openLvl;
		var i, org, orgArr = data.orOrgBVoList;
		for(i=0;i<orgArr.length;i++){
			org = orgArr[i];
			tree.add(org.orgPid, org.orgId, org.rescNm, org.orgTypCd, org.sortOrdr, org.rescId, {orgId:org.orgId, orgPid:org.orgPid, compId:org.compId, rescId:org.rescId, useYn:org.useYn, orgTypCd:org.orgTypCd, orgAbbrRescNm:org.orgAbbrRescNm});
		}
		tree.draw();
		tree.selectTree("${sessionScope.userVo.orgId}");
	}, {async:true});
}<%
// 트리 클릭 %>
function onTreeClick(orgId, orgNm){
	//TODO
	searchUser({orgId:orgId, selection:'${param.selection}'});
}<%
//페이징 %>
function goAjaxPage(pageNo){
	var input = $("#searchArea input[name='userNm']");
	var param;
	if(input.val()=='') param = new ParamMap(searchParam).extend({orgId:'${sessionScope.userVo.orgId}'});
	else param = new ParamMap(searchParam).extend({userNm:input.val()});
	$m.ajax('${_cxPth}/or/user/selectUserAjx.do?pageNo='+pageNo, param, function(html){
		userListArea.html(html);
	}, {mode:'HTML', async:true});
}
//]]>
</script><c:if
	test="${not empty param.apvLnOpt}"><jsp:include page="apvLnOpt.jsp" /></c:if><c:if
	test="${empty param.apvLnOpt}">
<script type="text/javascript">
//<![CDATA[<%
// 실제로 사용 안하는 소스 %>
function addApvrToSelected(arr, type){
	addToSelected(arr, type);
}
//]]>
</script>
</c:if>
	<header>
	<div class="unified_search" id="searchArea">
		<div class="back" onclick="history.back()"></div>
		<div class="searcharea">
			<dl>
				<dd class="unified_inputarea"><input type="text" class="unified_input" name="userNm" maxlength="20" onkeyup="searchUserByName(event)" /></dd>
				<dd class="unified_delete" onclick="$('#searchArea input').val('').focus();"></dd>
			</dl>
		</div>
		<div class="unified_search" onclick="searchUserByName(null);"></div>
	</div>
	</header>
	
	<section id="section">
		<div class="unified_tab">
		<div class="tabarea2">
			<dl>
			<dd class="tabsize2" onclick="$layout.tab.on($(this).children().attr('id'))"><div class="tabbtn" id="treeArea"><u:msg
				titleId="mor.tab.org" alt="조직도"/></div></dd>
			<dd class="line"></dd>
			<dd class="tabsize2" onclick="$layout.tab.on($(this).children().attr('id'))"><div class="tabbtn_on" id="userArea"><u:msg
				titleId="mor.tab.user" alt="임직원"/></div></dd>
			</dl>
		</div>
		</div>
		
		<div id="tabViewArea">
		
		<div id="treeArea" class="unified_tree2" style="${param.selection=='single' ? 'bottom:48px; ' : ''}display:none;">
		<div class="unified_treearea" style="overflow:auto;">
			<div class="treearea"><div class="tree" id="orgTree"></div></div>
		</div>
		</div>
		
		<div id="userArea" class="${param.selection=='single' ? 'unified_listarea2' : 'unified_listarea'}">
		<div class="listarea">
		<div class="blank5"></div>
		<article id="userListArea">
		</article>
		</div>
		</div>
		
		</div>
		<c:if
			
			test="${param.selection == 'single'}">
		<div class="unified_btn2">
		<div class="btnarea">
			<div class="size">
			<dl>
				<dd class="btn" onclick="setOneUser()"><u:msg titleId="mcm.btn.ok" alt="확인" /></dd>
			</dl>
			</div>
		</div>
		</div></c:if><c:if
			
			test="${param.selection != 'single'}"><c:if
				test="${empty param.apvLnOpt}"><u:cmt cmt="일반 사용자 선택" />
		<div class="unified_btn">
		<div class="btnarea">
			<div class="size">
			<dl>
				<dd class="plus" onclick="addChecked()"></dd>
				<dd class="minus" onclick="removeChecked()"></dd>
				<dd class="btn" onclick="setUsers()"><u:msg titleId="mcm.btn.ok" alt="확인" /></dd>
			</dl>
			</div>
		</div>
		</div></c:if><c:if
				test="${not empty param.apvLnOpt}"><u:cmt cmt="결재 경로 사용자 선택" />
		<div class="unified_btn">
		<div class="btnarea">
			<div class="size">
			<dl><c:if
					test="${param.apvLnOpt == 'reqDept'}">
				<dd class="btn_icoplus" onclick="addChecked('reqDept')"><u:term termId="ap.term.req" alt="신청" /></dd>
				<dd class="btn_icoplus" onclick="addChecked('prcDept')"><u:term termId="ap.term.prc" alt="처리" /></dd></c:if><c:if
					test="${param.apvLnOpt == 'prcDept'}">
				<dd class="plus" onclick="addChecked('prcDept')"></dd></c:if><c:if
					test="${param.apvLnOpt != 'reqDept' && param.apvLnOpt != 'prcDept'}">
				<dd class="plus" onclick="addChecked()"></dd></c:if>
				<dd class="minus" onclick="removeChecked()"></dd>
				<dd class="btn" onclick="setApvLnDetl()"><u:msg titleId="map.apvLn.detl" alt="상세설정" /></dd>
				<dd class="btn" onclick="setApvLn()"><u:msg titleId="mcm.btn.ok" alt="확인" /></dd>
			</dl>
			</div>
		</div>
		</div></c:if></c:if>
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