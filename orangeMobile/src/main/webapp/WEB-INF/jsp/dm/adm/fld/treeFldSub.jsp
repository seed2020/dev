<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="callback" test="${!empty param.callback }" value="${param.callback }" elseValue="setSelInfos"/>
<script type="text/javascript">
<!--<%// 트리 클릭 - 오른쪽의 코드 목록을 오픈, ROOT의 경우 빈화면으로 대치, 하위폴더가 있어도 빈화면으로 대치 %>
function onTreeClick(id, name, rescId){
	if(id==null || id=='') return;
}<%// html reload %>
function reoladFrame(fldId){
	var href = './treeFldFrm.do?menuId=${menuId}';
	if(fldId!=null && fldId!='') href = href + "&fldId="+fldId;
	location.replace(href);
}<%// 트리의 확장 데이터 리턴 %>
function getTreeData(){
	var sel = TREE.getTree('fldTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		return {id:'ROOT'};
	} else {
		var exts = TREE.getExtData('fldTree','exts');
		if(exts == null || exts.fldGrpId == 'COMP' || exts.fldGrpId == 'DEPT' || exts.fldGrpId == 'NONE') return null;
		return exts;
	}
}<%// 트리의 선택 데이터 리턴 %>
function getTreeSelect(mode){
	var sel = TREE.getTree('fldTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		return null;
	} else {
		var exts = TREE.getExtData('fldTree','exts');
		if(exts == null || ( (mode == undefined || mode == '') && (exts.fldGrpId == 'COMP' || exts.fldGrpId == 'DEPT' || exts.fldGrpId == 'NONE'))) return null;
		return [{id:exts.fldId,nm:exts.fldNm,fldGrpId:exts.fldGrpId,fldTypCd:exts.fldTypCd}];
	}
}<%// 트리의 선택 ID 리턴 %>
function getSelectId(){
	var sel = TREE.getTree('fldTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		return null;
	} else {
		var exts = TREE.getExtData('fldTree','exts');
		if(exts == null || exts.fldGrpId == 'COMP' || exts.fldGrpId == 'DEPT' || exts.fldGrpId == 'NONE') return null;
		return exts.fldId;
	}
}<%// 트리의 선택 ID 리턴 %>
function getSelectAllId(){
	var sel = TREE.getTree('fldTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		return null;
	} else {
		var exts = TREE.getExtData('fldTree','exts');
		if(exts == null) return null;
		return exts.fldId;
	}
}<%// 확인 버튼 클릭[한개] %>
function applySelect(){	
	var win = $m.nav.getWin(-1);
	if(win==null) return;
	var arr = getTreeSelect('${param.mode}');
	if(arr == null) {
		var msgSuffix = '${param.lstTyp}' == 'C' ? 'cls' : 'fldSub';
		$m.msg.alertMsg("dm.jsp.setDoc.not."+msgSuffix);<% // dm.jsp.setDoc.not.='분류' or '폴더'를 선택 후 사용해 주십시요. %>
		return;
	}
	if(arr != null) win.${callback}(arr, '${param.lstTyp}');
	history.back();
	
}<%// 확인 버튼 클릭[여러개] %>
function applySelects(){
	var win = $m.nav.getWin(-1);
	if(win==null) return;
	var arr = getSelected();
	if(win.${callback}(arr.length==0 ? null : arr,'${param.lstTyp}') != false){
		history.back();
	}
}<%
// [+] 더하기 버튼 %>
function addChecked(){
	var arr = [];	
	var selObj = getTreeSelect('${param.mode}');
	if(selObj == null) return;
	arr.push(selObj);
	if(arr.length>0){
		addToSelected(arr.reverse(), null);
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
		
		if(type!='txt'){
			aleadyHas = false;
			aleadys.each(function(index, oldItem){
				if(oldItem.id==obj.id){
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
		area.append('<dd class="txt" onclick="highlightTouched(this)" data-tree=\''+txt+'\'>'+(obj.nm==null ? 'NO-NAME' : obj.nm)+'</dd>');
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
		arr.push(JSON.parse($(this).attr('data-tree')));
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
}
<%// tree 스크립트로 생성 %>
$(document).ready(function() {
	
	var tree = TREE.create('fldTree');
	tree.onclick = 'onTreeClick';
	tree.setRoot('ROOT', '<u:msg titleId="cols.fld" alt="폴더"/>');
	tree.setSkin("${_skin}");
	
	<%// tree.add() param : fldPid, id, name, type, sortOrdr %>
	<c:forEach items="${dmFldBVoList}" var="dmFldBVo" varStatus="status" >
	tree.add("${dmFldBVo.fldPid}","${dmFldBVo.fldId}","<u:out value='${dmFldBVo.fldNm}' type='script' />","${empty dmFldBVo.fldTypCd ? 'F' : dmFldBVo.fldTypCd}","${dmFldBVo.fldTypCd eq 'F' ? '0' : '1'}${dmFldBVo.sortOrdr}","${dmFldBVo.rescId}",{fldId:"${dmFldBVo.fldId}",fldNm:"${dmFldBVo.fldNm}",fldPid:"${dmFldBVo.fldPid}",storId:"${dmFldBVo.storId}",rescId:"${dmFldBVo.rescId}",useYn:"${dmFldBVo.useYn}",title:"${dmFldBVo.fldId}",fldGrpId:"${dmFldBVo.fldGrpId}"});
	</c:forEach>
	tree.draw();<c:if test="${empty noInitSelect}"><c:if
		test="${not empty param.fldId}"><%// 파라미터 fldId 가 선택 되도록함 %>
	tree.selectTree("${param.fldId}");
	onTreeClick("${param.fldId}");</c:if><c:if
		test="${empty param.fldId}"><%// 사용자가 속한 부서가 선택 되도록 함 %>
	tree.selectTree("${sessionScope.userVo.orgId}");
	onTreeClick("${sessionScope.userVo.orgId}");</c:if>
	</c:if>
});
//-->
</script>
<div id="treeArea" class="unified_tree2" style="${param.fncMul ne 'Y' ? 'bottom:48px; ' : ''};top:52px;">
<div class="unified_treearea" style="overflow:auto;">
	<div class="treearea"><div class="tree" id="fldTree"></div></div>
</div>
</div>
<div class="unified_btn2">
    <div class="btnarea">
        <div class="size">
        <dl>
        <dd class="btn" onclick="history.back();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>
        <c:if test="${param.fncMul eq 'Y'}"><dd class="btn" onclick="applySelects();"><u:msg titleId="cm.btn.choice" alt="선택"/></dd></c:if>
		<c:if test="${empty param.fncMul || param.fncMul eq 'N'}"><dd class="btn" onclick="applySelect();"><u:msg titleId="cm.btn.choice" alt="선택"/></dd></c:if>
     </dl>
        </div>
    </div>
</div>
