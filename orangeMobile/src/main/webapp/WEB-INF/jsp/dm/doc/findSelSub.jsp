<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="treePage" test="${param.lstTyp eq 'C'}" value="treeDocClsAjx" elseValue="treeDocFldAjx"/>
<u:set var="treePageParam" test="${param.lstTyp eq 'C'}" value="&initSelect=Y" elseValue="&fldId=${param.selIds }&fldTyp=${param.fldTyp }"/>
<u:set var="paramStorIdQueryString" test="${!empty param.paramStorId }" value="&paramStorId=${param.paramStorId }" elseValue=""/><!-- 저장소ID -->
<u:set var="callback" test="${!empty param.callback }" value="${param.callback }" elseValue="setSelInfos"/>
<u:set test="${param.lstTyp eq 'C'}" var="atrbIdPrefix" value="cls" elseValue="fld"	/>
<script type="text/javascript">
<!--<%// 트리 클릭 - 오른쪽의 코드 목록을 오픈, ROOT의 경우 빈화면으로 대치, 하위폴더가 있어도 빈화면으로 대치 %>
function onTreeClick(id, name, rescId){
	if(id==null || id=='') return;
}<%// 트리의 확장 데이터 리턴 %>
function getTreeData(){
	var sel = TREE.getTree('selTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		return {id:'ROOT'};
	} else {
		var exts = TREE.getExtData('selTree','exts');
		if(exts == null || exts.fldGrpId == 'COMP' || exts.fldGrpId == 'DEPT' || exts.fldGrpId == 'NONE') return null;
		return exts;
	}
}<%// 트리의 선택 데이터 리턴 %>
function getTreeSelect(mode){
	var sel = TREE.getTree('selTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		return null;
	} else {
		var exts = TREE.getExtData('selTree','exts');
		if(exts == null || ( (mode == undefined || mode == '') && (exts.fldGrpId == 'COMP' || exts.fldGrpId == 'DEPT' || exts.fldGrpId == 'NONE'))) return null;
		return [{id:exts.id,nm:exts.nm,fldGrpId:exts.fldGrpId,fldTypCd:exts.fldTypCd}];
	}
}<%// 트리의 선택 ID 리턴 %>
function getSelectId(){
	var sel = TREE.getTree('selTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		return null;
	} else {
		var exts = TREE.getExtData('selTree','exts');
		if(exts == null || exts.fldGrpId == 'COMP' || exts.fldGrpId == 'DEPT' || exts.fldGrpId == 'NONE') return null;
		return exts.id;
	}
}<%// 트리의 선택 ID 리턴 %>
function getSelectAllId(){
	var sel = TREE.getTree('selTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		return null;
	} else {
		var exts = TREE.getExtData('selTree','exts');
		if(exts == null) return null;
		return exts.id;
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
	if(arr.length==0) return;
	if(win.${callback}(arr.length==0 ? null : arr,'${param.lstTyp}') != false){
		history.back();
	}
}<%// 선택목록 초기화 %>
function initSelectedArea(){
	<c:forEach var="map" items="${selList}" varStatus="status"><c:set var="map" value="${map}" scope="request"/>
		<u:convertMap var="idVal" srcId="map" attId="${atrbIdPrefix}Id" type="value"/>
		<u:convertMap var="idNm" srcId="map" attId="${atrbIdPrefix}Nm" type="value"	/>
		addChecked({id:'${idVal}',nm:'${idNm}'});
	</c:forEach>
}
<%// [+] 더하기 버튼 %>
function addChecked(selData){
	var arr = [];	
	var selObj = selData != undefined ? selData : getTreeData();
	if(selObj == null) return;
	arr.push({id:selObj.id,nm:selObj.nm});
	if(arr.length>0){
		addToSelected(arr.reverse(), null);
	}
}<%// 선택항목에 더하기 %>
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
}<%
//분류 - 트리 %>
function clsTree(tree){
	tree.setRoot('ROOT', '<u:msg titleId="cols.cls" alt="분류"/>');
	
	$m.ajax('${_cxPth}/dm/doc/${treePage}.do?menuId=${menuId}${treePageParam }&fldGrpId=${param.fldGrpId }${paramStorIdQueryString}', null, function(data){
		if(data.openLvl != null) tree.openLvl = data.openLvl;
		var i, objVo, objArr = data.dmClsBVoList;
		for(i=0;i<objArr.length;i++){
			objVo = objArr[i];
			tree.add(objVo.clsPid, objVo.clsId, objVo.clsNm, 'F', objVo.sortOrdr, objVo.rescId, {id:objVo.clsId, nm:objVo.clsNm, pid:objVo.clsPid, storId:objVo.storId, rescId:objVo.rescId, useYn:objVo.useYn, title:objVo.clsId});
		}
		tree.draw();
		tree.selectTree("${param.fldId}");
	}, {async:true});
}<%
//폴더 - 트리 %>
function fldTree(tree){
	tree.setRoot('ROOT', '<u:msg titleId="cols.fld" alt="폴더"/>');
	
	$m.ajax('${_cxPth}/dm/doc/${treePage}.do?menuId=${menuId}${treePageParam }&fldGrpId=${param.fldGrpId }${paramStorIdQueryString}', null, function(data){
		if(data.openLvl != null) tree.openLvl = data.openLvl;
		var i, objVo, objArr = data.dmFldBVoList;
		for(i=0;i<objArr.length;i++){
			objVo = objArr[i];
			tree.add(objVo.fldPid, objVo.fldId, objVo.fldNm, objVo.fldTypCd == '' ? 'F' : objVo.fldTypCd, (objVo.fldTypCd == 'F' ? '0' : '1')+objVo.sortOrdr, objVo.rescId, {id:objVo.fldId, nm:objVo.fldNm, pid:objVo.fldPid, storId:objVo.storId, rescId:objVo.rescId, useYn:objVo.useYn, title:objVo.fldId, fldGrpId:objVo.fldGrpId});
		}
		tree.draw();
		tree.selectTree("${param.fldId}");
	}, {async:true});
}
<% // 트리용 Object %>
var tree = null;
function initTree(lstTyp){
	tree = TREE.create('selTree');
	tree.onclick = 'onTreeClick';	
	tree.setSkin("${_skin}");
	<c:if test="${not empty openLvl}">tree.openLvl = ${openLvl};</c:if><%// 특정폴더 하위나 상위폴더 조회시 모든 트리를 펼쳐 보이게함 %>
	<c:if test="${not empty treeSelectOption}">TREE.selectOption = ${treeSelectOption};</c:if><%// 여러개 선택 가능 하도록 %>
	if(lstTyp != undefined && lstTyp == 'C') clsTree(tree);
	else fldTree(tree);
}
$(document).ready(function() {
	<%// 트리 초기화 %>
	initTree('${param.lstTyp}');
	<c:if test="${param.fncMul eq 'Y' && !empty selList}">
		<%// 선택목록 초기화 %>
		initSelectedArea();
	</c:if>
});
//-->
</script>
<section id="section">
<div id="treeArea" class="unified_tree2" style="${param.fncMul ne 'Y' ? 'bottom:48px; ' : ''};top:52px;">
<div class="unified_treearea" style="overflow:auto;">
	<div class="treearea"><div class="tree" id="selTree"></div></div>
</div>
</div>
<c:if test="${param.fncMul ne 'Y'}">
<div class="unified_btn2">
<div class="btnarea">
	<div class="size">
	<dl>
		<dd class="btn" onclick="history.back();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>
        <dd class="btn" onclick="applySelect();"><u:msg titleId="cm.btn.choice" alt="선택"/></dd>
	</dl>
	</div>
</div>
</div></c:if>
<c:if test="${param.fncMul eq 'Y'}">
<div class="unified_btn">
<div class="btnarea">
	<div class="size">
	<dl>
		<dd class="plus" onclick="addChecked()"></dd>
		<dd class="minus" onclick="removeChecked()"></dd>
        <dd class="btn" onclick="applySelects();"><u:msg titleId="mcm.btn.ok" alt="확인" /></dd>
        <dd class="btn" onclick="history.back();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>
	</dl>
	</div>
</div>
</div>
<div class="unified_btm" id="selectedArea">
	<div class="unified_btm_icol"></div>
	<div class="unified_btm_icor"></div>
	<div class="unified_btmin">
		<dl id="selectedListArea"></dl>
	</div>
</div>
</c:if>
</section>