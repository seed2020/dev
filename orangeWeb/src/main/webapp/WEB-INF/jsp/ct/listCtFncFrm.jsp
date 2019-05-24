<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

<%// 트리의 항목 위로/아래로 이동 %>
function move(direction, ctId){
	var sel = TREE.getTree('fncTree').selected;
	var exts = TREE.getExtData('fncTree','exts');
	if(direction == 'up'){
		var prevCtHome = $(sel).prev().attr('data-exts');
		if(prevCtHome != null){
		var objCtHome = JSON.parse(prevCtHome);
			if(objCtHome.fncId == 'CTHOME'){
				alertMsg('cm.msg.move.first.up');<%//맨 위의 항목 입니다.%>
				return null;
			}
		}
	}
	if(sel==null){
		alertMsg('cm.msg.noSelect');<%//선택한 항목이 없습니다.%>
	} else if($(sel).attr('id')=='ROOTLI'){
		alertMsg('cm.msg.move.root');<%//최상위 항목은 이동 할 수 없습니다.%>
	} else if('CTHOME' == exts['fncId']){
		alertMsg('cm.msg.home.root');<%//HOME 항목은 이동 할 수 없습니다.%>
		return null;
	} else if(direction=='up' && $(sel).prev().length==0 ){
		alertMsg('cm.msg.move.first.up');<%//맨 위의 항목 입니다.%>
	} else if(direction=='down' && $(sel).next().length==0){
		alertMsg('cm.msg.move.last.down');<%//맨 아래의 항목 입니다.%>
	} else {
		var fncPid, selExts, selFncUid = $(sel).attr('id'); 
		var selFncOrdr = $(exts).attr('fncOrdr');
		if(direction == 'up'){
			selExts = $(sel).prev().attr('data-exts');
		}else if(direction == 'down'){
			selExts = $(sel).next().attr('data-exts');
		}
		var objExt = JSON.parse(selExts);
		
		if(selFncUid.length>2){
			selFncUid = selFncUid.substring(0, selFncUid.length-2);
			//alert('selFncUid = ' + selFncUid + ' selFncOrdr = ' + selFncOrdr +  ' selPrevUid = ' + objExt.id + ' selPrevOrdr = ' + objExt.fncOrdr );
					
			callAjax('./transFncMove.do?menuId=${menuId}', {selFncUid:selFncUid, selFncOrdr:selFncOrdr, selChangeUid:objExt.id, selChangeOrdr:objExt.fncOrdr, ctId:ctId}, function(data){
				if(data.message!=null){
					alert(data.message);
				} else {
					parent.reloadTree(selFncUid);
					/*if(direction=='up'){
						$(sel).prev().before(sel);
						$(sel).removeClass('end');
						$(sel).parent().children(":last-child").addClass("end");
						parent.reloadTree("");
					} else if(direction=='down'){
						$(sel).next().removeClass('end');
						$(sel).next().after(sel);
						$(sel).parent().children(":last").addClass("end");
						parent.reloadTree("");
					}*/
				}
			});
		}
	}
};

<%// 트리 클릭 - 오른쪽의 코드 목록을 오픈, ROOT의 경우 빈화면으로 대치, 하위폴더가 있어도 빈화면으로 대치 %>
function onFncTreeClick(id, name){
	//var hasChild = $('#'+id+'LI').find("ul:first").children().length > 0;
	//parent.openCdList(hasChild ? 'ROOT' : id);
	parent.openCdList(id);
	
};

// 트리 선택
function onSelectFld(id){
	var tree = TREE.getTree('fncTree');
	tree.selectTree(id);
	
};

<%// 폴더 전체 삭제%>
function delAllFld(ctId,fnc,catId){
	var exts = TREE.getExtData('fncTree','exts');
	if(exts==null){
		alertMsg('cm.msg.noSelect');<%//선택한 항목이 없습니다.%>
	} else if('ROOT'==exts['id']){
		alertMsg('pt.msg.not.del.cd.sys');<%//pt.msg.not.del.cd.sys=시스템 코드는 삭제 할 수 없습니다.%>
	} else if('CTHOME' == exts['fncId']
			||'CTGUESTSEARCH' == exts['fncId']
			||'CTMANAGEMENT' == exts['fncId']
			||'CTSECESSION' == exts['fncId']){
		
		alertMsg('pt.msg.not.del.cd.sys');<%//pt.msg.not.del.cd.sys=시스템 코드는 삭제 할 수 없습니다.%>
	}else if('3' == exts['fldTyp'] && 'CTFNCFOLDER' == exts['fncId']){
			if(confirmMsg("ct.cfrm.allDel.warning")) {<%//ct.cfrm.allDel.warning=폴더의 하위 분류 까지 삭제 됩니다.\n 삭제하시겠습니까?%>
			callAjax('./transCtFncUidAllDelAjx.do?menuId=${menuId}&fnc='+fnc+'&catId='+catId, {mode:'delete',ctId:ctId, ctFncUid:exts['id']}, function(data){
				if(data.message!=null){
					alert(data.message);
				}
				if(data.result=='ok'){
						parent.reload('./setCmFnc.do?menuId=${menuId}&ctId='+ctId+'&fnc='+fnc+'&catId='+catId);
						//reloadTree(exts['id']);
				}else{
					return null;
				}
			});
		}
	}else{
		alertMsg('ct.msg.notFld.del');<%//폴더가 아닌 기능은 삭제 할 수 없습니다.%>
	}
};



<%// 폴더 삭제%>
function delFld(ctId){
	var exts = TREE.getExtData('fncTree','exts');
	if(exts==null){
		alertMsg('cm.msg.noSelect');<%//선택한 항목이 없습니다.%>
	} else if('ROOT'==exts['id']){
		alertMsg('pt.msg.not.del.cd.sys');<%//pt.msg.not.del.cd.sys=시스템 코드는 삭제 할 수 없습니다.%>
	} else if('3' == exts['fldTyp'] && 'CTFNCFOLDER' == exts['fncId']){
		alertMsg('cm.msg.del.folder');<%//폴더는 삭제 할 수 없습니다.%>
	} else if('CTHOME' == exts['fncId']
			||'CTGUESTSEARCH' == exts['fncId']
			||'CTMANAGEMENT' == exts['fncId']
			||'CTSECESSION' == exts['fncId']){
		
		alertMsg('pt.msg.not.del.cd.sys');<%//pt.msg.not.del.cd.sys=시스템 코드는 삭제 할 수 없습니다.%>
	
	//} else if(confirmMsg("cm.cfrm.del")) {<%//cm.cfrm.del=삭제하시겠습니까 ?%>
	}else{
		callAjax('./transCtFncUidDelAjx.do?menuId=${menuId}', {mode:'delete',ctId:ctId, ctFncUid:exts['id']}, function(data){
			if(data.message!=null){
				alert(data.message);
			}
			if(data.result=='ok'){
					parent.reload('./setCmFnc.do?menuId=${menuId}&ctId='+ctId);
					//reloadTree(exts['id']);
			}else{
				return null;
			}
		});
	}
};

<%// 트리의 확장 데이터 리턴 %>
function getTreeData(forOption, regCls){
	var sel = TREE.getTree('fncTree').selected;
	var exts = TREE.getExtData('fncTree','exts');
	if($(sel).attr('id')=='ROOTLI'){
		if(forOption=='mod'){
			alertMsg('cm.msg.modNm.root');<%//최상위 항목은 이름을 변경 할 수 없습니다.%>
			return null;
		
		} else if(regCls=='cls'){
			alertMsg('ct.msg.cls.reg.root');<%//분류 등록은 최상위 항목에 등록 할 수 없습니다.%>
		} else if(forOption=='reg'){
			return {id:'ROOT'};
		}
	} 
	if(forOption=='mod'){
		if('CTHOME' == exts['fncId']
		||'CTGUESTSEARCH' == exts['fncId']
		||'CTMANAGEMENT' == exts['fncId']
		||'CTSECESSION' == exts['fncId']){
			alertMsg('cm.msg.modCd.sys');<%//시스템 코드는 이름을 변경 할 수 없습니다.%>
			return null;
		}
	}
	return {id:TREE.getExtData('fncTree','exts').id, fncRescId:TREE.getExtData('fncTree','exts').fncRescId};
};

<%// 트리의 확장 데이터 리턴 %>
function getTreeRightToLeftData(forOption, regCls){
	
	var sel = TREE.getTree('fncTree').selected;
	if(TREE.getExtData('fncTree','exts').fldTyp == '2'){
		if(forOption=='reg'){
			alertMsg('ct.msg.reg.root');<%//최상위 항목은 수정 할 수 없습니다.%>
			return {id:'fail'};
		} 
		//else if(forOption=='reg'){
		//	return {id:'ROOT'};
		//}
	}
	return {id:TREE.getExtData('fncTree','exts').id, fldTyp:TREE.getExtData('fncTree','exts').fldTyp};
};




<% // TREE %>
$(document).ready(function() {
	var tree = TREE.create('fncTree');
	tree.onclick = 'onFncTreeClick';
	tree.setRoot('ROOT', 'Category');
	tree.setSkin("${_skin}");
	//tree.openLvl = 1;
	
	<%// tree.add() param : pid, id, name, type, sortOrdr %>
	<c:forEach items="${ctFncDList}" var="list" varStatus="status" >
		tree.add("${list.ctFncPid}","${list.ctFncUid}","<u:out value='${list.ctFncNm}' type='script' />","${list.ctFncTyp}" == "2"?"D":"F","${list.ctFncOrdr}",null,{pid:"${list.ctFncPid}",id:"${list.ctFncUid}",fldNm:"${list.ctFncNm}",fldTyp:"${list.ctFncTyp}",fncId:"${list.ctFncId}", fncRescId:"${list.ctFncSubjRescId}", fncOrdr:"${list.ctFncOrdr}"});
	</c:forEach>
	tree.draw();
	<u:set test="${empty param.ctFncUid}" var="selectedNode" value="ROOT" elseValue="${param.ctFncUid}" />
	tree.selectTree("${selectedNode}");
	
	//onTreeClick("${selectedNode}");
	
	setUniformCSS();
});

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<div id="fncTree" class="tree"></div>
