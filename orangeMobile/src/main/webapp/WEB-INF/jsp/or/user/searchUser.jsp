<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[
<%
// 사용자 검색 %>
var userListArea = null;
function searchUser(param){
	$m.ajax('${_cxPth}/or/user/listUserAjx.do', param, function(html){
		$layout.tab.on('userArea');
		if(userListArea==null) userListArea = $('#section').children('#tabViewArea').children('#userArea').find('#userListArea');
		userListArea.html(html);
	}, {mode:'HTML', async:true});
}<%
// 사용자 명 검색 %>
function searchUserByName(e){
	if(e==null || e.keyCode == 13){
		var input = $("#searchArea input[name='userNm']");
		if(input.val()==''){
			//$m.msg.alertMsg('cm.input.check.mandatory', ['#cols.userNm']);
			searchUser({orgId:'${sessionScope.userVo.orgId}'});
			input.blur();
		} else {
			searchUser({userNm:input.val()});
			input.blur();
		}
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
	
	var param = ${not empty globalOrgChartEnable ? '{global:"Y"}' : 'null'};
	$m.ajax('${_cxPth}/or/user/treeOrgAjx.do', param, function(data){
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
	searchUser({orgId:orgId});
}

$(document).ready(function() {
	var section = $("#section");
	$layout.tab.init(section.find('.tabarea2 dl'), section.find('#tabViewArea'), 'tabbtn');
	<%// 자기부서 사용자 조회 %>
	searchUser({orgId:'${sessionScope.userVo.orgId}'});
	<%// 트리 초기화 %>
	initTree();
});
//]]>
</script>
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
		<div id="treeArea" class="unified_tree1" style="display:none;">
		<div class="unified_treearea">
			<div class="treearea"><div class="tree" id="orgTree"></div></div>
		</div>
		</div>
		
		<div id="userArea" class="listarea">
		<div class="blank5"></div>
		<article id="userListArea">
		</article>
		</div>
		</div>
		
	</section>