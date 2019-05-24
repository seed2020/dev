<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

function selectTab(tabNoRT){
	if(tabNoRT == 0)
	{
		$("#bbListArea").show();
		$("#fncListArea").hide();
	}
	else
	{
		$("#bbListArea").hide();
		$("#fncListArea").show();
	}
}

<% // 트리 클릭 %>
function onTreeClick(id, name) {
}
<% // 메뉴 추가 %>
function addMnu() {
	var $checked = $('#bbListArea input:checkbox:checked');
	var brdIds = [];
	$checked.each(function() {
		if($(this).val() != "")
			brdIds.push($(this).val());
	});
	
	$checked = $('#fncListArea input:checkbox:checked');
	$checked.each(function() {
		if($(this).val() != "")
			brdIds.push($(this).val());
	});
	
	$checked = $('#allBbListArea input:checkbox:checked');
	$checked.each(function() {
		if($(this).val() != "")
			brdIds.push($(this).val());
	});
	
	if(brdIds.length == 0){
		<% // 선택한 항목이 없습니다. %>
		alertMsg('cm.msg.noSelect');
		return;
	}
	var sel = TREE.getTree('mnuTree').selected;
	if ($(sel).attr('id') == 'ROOTLI') {
		var exts = {mnuId: 'ROOT'};
	} else {
		var exts = TREE.getExtData('mnuTree', 'exts');
		if (exts['fldYn'] != 'Y') {
			alertMsg('cm.msg.noSelect');
			return;
		}
	}
	var mnuId = exts['mnuId'];
	if (confirmMsg("bb.msg.cfrm.mnu.add")) {<% // bb.msg.cfrm.mnu.add=선택한 게시판을 메뉴로 추가하시겠습니까? %>
		callAjax('./transBbMnuAjx.do?menuId=${menuId}&valUM=${valUM}', {brdIds:brdIds, mnuId: mnuId, mnuGrpId: '${ptMnuGrpBVo.mnuGrpId}'}, function (data) {
			if (data.message != null) {
				alert(data.message);
			} else {
				setMnu('${ptMnuGrpBVo.mnuGrpId}','${valUM}');
			}
		});
	}
}
<% // 메뉴 삭제 %>
function delMnu() {
	var sel = TREE.getTree('mnuTree').selected;
	if ($(sel).attr('id') == 'ROOTLI') {
		return;
	}
	var exts = TREE.getExtData('mnuTree', 'exts');
	if (exts['fldYn'] == 'Y') {
		return;
	}
	var mnuId = exts['mnuId'];

	if (confirmMsg("bb.msg.cfrm.mnu.del")) {<% // bb.msg.cfrm.mnu.del=선택한 메뉴를 삭제하시겠습니까? %>
		callAjax('./transBbMnuDelAjx.do?menuId=${menuId}&valUM=${valUM}', {mnuId: mnuId}, function (data) {
			if (data.message != null) {
				alert(data.message);
			} else {
				setMnu('${ptMnuGrpBVo.mnuGrpId}','${valUM}');
			}
		});
	}
}
<% // 메뉴 이동 - 기존 Ajax 방식 %>
function moveMnuTemp(direction) {
	var sel = TREE.getTree('mnuTree').selected;
	if (sel == null) {
		alertMsg('cm.msg.noSelect');
		<% // 선택한 항목이 없습니다. %>
	} else if ($(sel).attr('id') == 'ROOTLI') {
		alertMsg('cm.msg.move.root');
		<% // 최상위 항목은 이동 할 수 없습니다. %>
	} else if ((direction == 'up' || direction == 'tup') && $(sel).prev().length == 0) {
		alertMsg('cm.msg.move.first.up');
		<% // 맨 위의 항목 입니다. %>
	} else if ((direction == 'down' || direction == 'tdown') && $(sel).next().length == 0) {
		alertMsg('cm.msg.move.last.down');
		<% // 맨 아래의 항목 입니다. %>
	} else {
		var mnuPid, cd = $(sel).attr('id');
		if (cd.length > 2) {
			cd = cd.substring(0, cd.length - 2);
			mnuPid = TREE.getExtData('mnuTree', 'exts')['pid'];
			// alert("mnuIds:"+cd+" mnuPid:"+mnuPid+" direction:"+direction);
			callAjax('/bb/adm/transFldMoveAjx.do?menuId=${menuId}&valUM=${valUM}', {mnuIds: [cd], mnuPid: mnuPid, direction: direction}, function (data) {
				if (data.message != null) {
					alert(data.message);
				} else {
					if(direction=='up'){
						$(sel).prev().before(sel);
						$(sel).removeClass('end');
						$(sel).parent().children(":last-child").addClass("end");
					}else if(direction=='tup'){
						$(sel).parent().children(":first").before(sel);
						$(sel).removeClass('end');
						$(sel).parent().children(":last-child").addClass("end");
					}else if(direction=='tdown'){
						$(sel).parent().children(":last").removeClass('end');
						$(sel).parent().children(":last").after(sel);
						$(sel).addClass("end");
					}else if(direction=='down'){
						$(sel).next().removeClass('end');
						$(sel).next().after(sel);
						$(sel).parent().children(":last").addClass("end");
					}
				}
			});
		}
	}
}
<% // 폴더관리 %>
function mngFld(mode) {
	if (mode == 'del') {
		delFld();
	} else {
		var tree = getTreeData(mode);
		if (tree == null) {
			alertMsg('cm.msg.noSelect');
			<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
		} else if (tree.fldYn == 'N') {
			if (mode == 'reg') {
				alertMsg('pt.jsp.setMnu.msg1');
				<% // pt.jsp.setMnu.msg1=메뉴에는 하위 폴더를 등록 할 수 없습니다. %>
			} else if (mode == 'mod') {
				alertMsg('pt.jsp.setMnu.msg2');
				<% // pt.jsp.setMnu.msg2=메뉴는 오른쪽 상세설정에서 수정해 주십시요. %>
			}
		} else {
			var popTitle = (mode == 'reg') ? '<u:msg titleId="pt.jsp.setCd.regFld" alt="폴더등록"/>' : '<u:msg titleId="pt.jsp.setCd.modFld" alt="폴더수정"/>';
			var url = '/pt/adm/mnu/setFldPop.do?menuId=${menuId}&valUM=${valUM}&admMnuYn=N&mnuGrpId=' + tree.mnuGrpId;
			url += '&' + ((mode == 'reg') ? 'mnuPid' : 'mnuId') + '=' + tree.mnuId;
			dialog.open('setFldDialog', popTitle, url);
		}
	}
}
<% // 폴더 삭제 %>
function delFld() {
	var exts = TREE.getExtData('mnuTree', 'exts');
	if (exts == null) {
		alertMsg('cm.msg.noSelect');
		<% // 선택한 항목이 없습니다. %>
	} else if ('Y' == exts['sysYn'] || 'ROOT' == exts['id']) {
		alertMsg('pt.msg.not.del.mnu.sys');
		<% // 시스템 메뉴는 삭제 할 수 업습니다. %>
	} else if (confirmMsg("cm.cfrm.del")) {<% // cm.cfrm.del=삭제하시겠습니까 ? %>
		callAjax('/pt/adm/mnu/transFldDelAjx.do?menuId=${menuId}&valUM=${valUM}', {mode: 'delete', mnuIds: [exts['mnuId']]}, function (data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				setMnu('${ptMnuGrpBVo.mnuGrpId}','${valUM}');
			}
		});
	}
}
<% // 트리의 확장 데이터 리턴 %>
function getTreeData(forOption) {
	var sel = TREE.getTree('mnuTree').selected;
	if ($(sel).attr('id') == 'ROOTLI') {
		if (forOption == 'mod') {
			alertMsg('cm.msg.mod.root');
			<% // 최상위 항목은 수정 할 수 없습니다. %>
			return null;
		} else if (forOption == 'reg') {
			return {mnuId: 'ROOT', mnuGrpId: '${ptMnuGrpBVo.mnuGrpId}'};
		}
	} else {
		var exts = TREE.getExtData('mnuTree', 'exts');
		if (exts != null) {
			exts['mnuGrpId'] = '${ptMnuGrpBVo.mnuGrpId}';
		}
		return exts;
	}
}
<% // [팝업:폴더등록, 폴더수정] - 저장 버튼 %>
function saveFld(){
	if(validator.validate('setFldPop')){
		var $frm = $('#setFldPop');
		$frm.attr('action','/pt/adm/mnu/transFld.do?menuId=${menuId}&valUM=${valUM}');
		$frm.attr('target','dataframe');
		$frm.submit();
		dialog.close('setFldDialog');
	}
}
<% // [팝업:저장 후처리] - 트리 리로드 %>
function reloadTree(mnuPid, mnuId){
	setMnu('${ptMnuGrpBVo.mnuGrpId}','${valUM}');
}
<% // 메뉴 이동 %>
function moveMnu(direction) {
	var sel = TREE.getTree('mnuTree').selected;
	if (sel == null) {
		alertMsg('cm.msg.noSelect');
		<% // 선택한 항목이 없습니다. %>
	} else if ($(sel).attr('id') == 'ROOTLI') {
		alertMsg('cm.msg.move.root');
		<% // 최상위 항목은 이동 할 수 없습니다. %>
	} else if ((direction == 'up' || direction == 'tup') && $(sel).prev().length == 0) {
		alertMsg('cm.msg.move.first.up');
		<% // 맨 위의 항목 입니다. %>
	} else if ((direction == 'down' || direction == 'tdown') && $(sel).next().length == 0) {
		alertMsg('cm.msg.move.last.down');
		<% // 맨 아래의 항목 입니다. %>
	} else {
		if(direction=='up'){
			$(sel).prev().before(sel);
			$(sel).removeClass('end');
			$(sel).parent().children(":last-child").addClass("end");
		}else if(direction=='tup'){
			$(sel).parent().children(":first").before(sel);
			$(sel).removeClass('end');
			$(sel).parent().children(":last-child").addClass("end");
		}else if(direction=='tdown'){
			$(sel).parent().children(":last").removeClass('end');
			$(sel).parent().children(":last").after(sel);
			$(sel).addClass("end");
		}else if(direction=='down'){
			$(sel).next().removeClass('end');
			$(sel).next().after(sel);
			$(sel).parent().children(":last").addClass("end");
		}
	}
}
<% // 메뉴 전체 배열 %>
function getMnuList(mnuMap, $p){
	if($p.children().length>0 && mnuMap==null) mnuMap = new ParamMap();
	var map=null;
	var child=null;
	for( var i=0;i<$p.children().length;i++){
		var exts = $($p.children()[i]).attr("data-exts");
		var obj = $.parseJSON(exts);
		if(mnuMap.get(obj.mnuPid)!=null) map=mnuMap.get(obj.mnuPid);
		else map=[];
		map.push(obj.mnuId);
		mnuMap.put(obj.mnuPid, map);
		child=$($p.children()[i]).find("UL:first");
		if(child!=undefined && child.children().length>0) getMnuList(mnuMap, child);
	}
	return mnuMap;
}
<% // 메뉴 이동 저장 %>
function moveMnuSave() {
	var $node = $('#mnuTree').find('#ROOTLI'), $p;
	if($node.length>0){
		$p = $node.parent().find("UL:first");
		var mnuMap = getMnuList(null, $p);
		if(mnuMap==null) return;		
		var mnuList = JSON.stringify(mnuMap.toJSON());
		callAjax('/bb/adm/transFldMoveSaveAjx.do?menuId=${menuId}&valUM=${valUM}', {mnuList: mnuList, mnuGrpId: '${ptMnuGrpBVo.mnuGrpId}'}, function (data) {
			if (data.message != null) {
				alert(data.message);
			}
		});
	}
};
<% // TREE %>
$(document).ready(function() {
	var tree = TREE.create('mnuTree');
	tree.onclick = 'onTreeClick';
	tree.setRoot('${ptMnuGrpBVo.mnuGrpId}', '${ptMnuGrpBVo.rescNm}');
	tree.setSkin("${_skin}");
	<% // tree.add() param : pid, id, name, type, sortOrdr, rescId, exts %>
	<c:forEach items="${ptMnuDVoList}" var="ptMnuDVo" varStatus="status" >
	tree.add("${ptMnuDVo.mnuPid}","${ptMnuDVo.mnuId}","<u:out value='${ptMnuDVo.rescNm}' type='script' />","<u:decode srcValue='${ptMnuDVo.fldYn}' tgtValue='Y' value='F' elseValue='A' />","${ptMnuDVo.sortOrdr}","${ptMnuDVo.rescId}",{mnuId:"${ptMnuDVo.mnuId}",mnuPid:"${ptMnuDVo.mnuPid}",title:"${ptMnuDVo.mnuId}",rescId:"${ptMnuDVo.rescId}",sysYn:"${ptMnuDVo.sysMnuYn}",fldYn:"${ptMnuDVo.fldYn}"});
	</c:forEach>
	tree.draw();
	tree.selectTree("ROOT");
	changeTab('bbTab','0');
});
//-->
</script>
<div style="width:750px; height:450px; padding 10px;">

<form id="combForm">
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="compId" value="${compId}" />
</form>

<u:boxArea className="wbox" noBottomBlank="true"
	style="float:left; width:50%;"
	outerStyle="height:400px;overflow:hidden;"
	innerStyle="width:96%; margin:0 auto 0 auto; padding:10px 0 0 0;">

	<div class="titlearea">
		<div class="tit_left">
		<dl>
		<dd class="title_s"><u:msg titleId="bb.jsp.setBbMnuPop.leftTitle" alt="메뉴 트리 구성" /></dd>
		</dl>
		</div>
		<div class="tit_right">
		<ul>
		<li class="ico">
			<select onchange="setMnu(this.value,'${valUM}');" style="width:140px;">
			<c:if test="${fn:length(ptMnuGrpBVoList) > 0}">
				<c:forEach items="${ptMnuGrpBVoList}" var="mnuGrpVo" varStatus="status">
				<u:option value="${mnuGrpVo.mnuGrpId}" title="${mnuGrpVo.rescNm}" checkValue="${ptMnuGrpBVo.mnuGrpId}" />
				</c:forEach>
			</c:if>
			</select>
			</li>
		<li class="ico">
			<u:titleIcon type="move.top" href="javascript:moveMnu('tup')" auth="A" />
			<u:titleIcon type="up" href="javascript:moveMnu('up')" auth="A" />
			<u:titleIcon type="down" href="javascript:moveMnu('down')" auth="A" />
			<u:titleIcon type="move.bottom" href="javascript:moveMnu('tdown')" auth="A" />
			</li>
		</ul>
		</div>
	</div>

	<u:titleArea outerStyle="height:325px; overflow-x:hidden; overflow-y:auto;" noBottomBlank="true" innerStyle="NO_INNER_IDV">
		<div id="mnuTree" class="tree"></div>
	</u:titleArea>
	
	<u:buttonArea topBlank="true">
		<c:if test="${valUM ne 'M' }">
			<u:buttonS alt="폴더등록" href="javascript:mngFld('reg');" titleId="pt.jsp.setCd.regFld" id="btnAddFld" auth="A" />
			<u:buttonS alt="폴더수정" href="javascript:mngFld('mod');" titleId="pt.jsp.setCd.modFld" id="btnModFld" auth="A" />
			<u:buttonS alt="폴더삭제" href="javascript:mngFld('del');" titleId="pt.jsp.setCd.delFld" id="btnDelFld" auth="A" />
		</c:if>
		<u:buttonS alt="순서저장" href="javascript:moveMnuSave();" titleId="bb.btn.ordr.save" auth="A" />
	</u:buttonArea>
</u:boxArea>

<div style="float:left; width:6%; height:400px;">
	<table height="100%" style="margin:0 auto 0 auto;" border="0" cellpadding="0" cellspacing="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><u:buttonIcon href="javascript:" image="ico_left.png" titleId="cm.btn.selAdd" alt="선택추가" onclick="addMnu();" /></td>
		</tr>
	
		<tr>
		<td class="height5"></td>
		</tr>
	
		<tr>
		<td><u:buttonIcon href="javascript:" image="ico_right.png" titleId="cm.btn.selDel" alt="선택삭제" onclick="delMnu();" /></td>
		</tr>
		</tbody></table></td>
	</tr>
	</table>
</div>

<u:boxArea className="wbox" noBottomBlank="true"
	style="float:right; width:40%;"
	outerStyle="height:400px;overflow:hidden;"
	innerStyle="width:96%; margin:0 auto 0 auto; padding:10px 0 0 0;">

	<u:tabGroup id="bbTab" noBottomBlank="true">
		<u:tab id="bbTab" areaId="bbListArea" titleId="bb.jsp.setBbMnuPop.rightTitle" on="true" />
		<u:tab id="bbTab" areaId="fncListArea" titleId="bb.jsp.setBbMnuPop.rightTitle2" />
		<u:tab id="bbTab" areaId="allBbListArea" titleId="bb.jsp.tab.allCompBrd" />
	</u:tabGroup>
	<u:tabArea
		outerStyle = "height:357px; overflow-x:hidden; overflow-y:auto;"
		innerStyle = "height:337px; margin:5px;"
		noBottomBlank="true"
		>

		<u:listArea id="bbListArea" noBottomBlank="true" >
			<tr>
			<td class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('bbListArea', this.checked);" value=""/></td>
			<td class="head_ct"><u:msg titleId="cols.bbNm" alt="게시판명" /></td>
			</tr>
		<c:if test="${fn:length(baBrdBVoLlist) == 0}">
			<tr>
			<td class="nodata" colspan="2"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
		</c:if>
		<c:if test="${fn:length(baBrdBVoLlist) > 0}">
			<c:forEach items="${baBrdBVoLlist}" var="baBrdBVo" varStatus="status">
			<tr>
			<td width="30" class="bodybg_ct"><input type="checkbox" value="${baBrdBVo.brdId}" /></td>
			<td class="body_lt">${baBrdBVo.rescNm}</td>
			</tr>
			</c:forEach>
		</c:if>
		</u:listArea>
		
		<u:listArea id="fncListArea" noBottomBlank="true" style="display: none;">
			<tr>
			<td class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('fncListArea', this.checked);" value=""/></td>
			<td class="head_ct"><u:msg titleId="cols.bbNm" alt="게시판명" /></td>
			</tr>
		<c:if test="${fn:length(brdFncList) == 0}">
			<tr>
			<td class="nodata" colspan="2"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
		</c:if>
		<c:if test="${fn:length(brdFncList) > 0}">
			<c:forEach items="${brdFncList}" var="brdFncVo" varStatus="status">
			<tr>
			<td width="30" class="bodybg_ct"><input type="checkbox" value="${brdFncVo.cd}" /></td>
			<td class="body_lt">${brdFncVo.rescNm}</td>
			</tr>
			</c:forEach>
		</c:if>
		</u:listArea>
		
		<u:listArea id="allBbListArea" noBottomBlank="true" >
			<tr>
			<td class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('allBbListArea', this.checked);" value=""/></td>
			<td class="head_ct"><u:msg titleId="cols.bbNm" alt="게시판명" /></td>
			</tr>
		<c:if test="${fn:length(allCompBrdList) == 0}">
			<tr>
			<td class="nodata" colspan="2"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
		</c:if>
		<c:if test="${fn:length(allCompBrdList) > 0}">
			<c:forEach items="${allCompBrdList}" var="baBrdBVo" varStatus="status">
			<tr>
			<td width="30" class="bodybg_ct"><input type="checkbox" value="${baBrdBVo.brdId}" /></td>
			<td class="body_lt">${baBrdBVo.rescNm}</td>
			</tr>
			</c:forEach>
		</c:if>
		</u:listArea>
		
	</u:tabArea>

</u:boxArea>

<u:buttonArea topBlank="true">
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>

</div>
