<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// 일괄 분류 등록, 분류 추가, 분류 수정, 분류 삭제 - addAllCls, addCls, modCls, delCls - 부모창에서 호출 함 %>
function managePsnCls(mode){
	var popTitle = callMsg("ap.btn."+mode);
	if(mode=='addAllCls'){
		
	} else {
		var sel = TREE.getTree("clsTree").selected;
		if(sel==null){
			alertMsg('cm.msg.noSelect');<%//선택한 항목이 없습니다.%>
		} else {
			var selId = $(sel).attr('id');
			selId = selId.substring(0, selId.length-2);
			if(mode=='addCls'){
				parent.dialog.open('setPsnClsDialog', popTitle,'./setPsnClsPop.do?menuId=${menuId}&bxId=${param.bxId}&psnClsInfoPid='+selId);
			} else if(mode=='modCls'){
				if(selId=='ROOT'){
					alertMsg('cm.msg.mod.root');<%//cm.msg.mod.root=최상위 항목은 수정 할 수 없습니다.%>
				} else {
					parent.dialog.open('setPsnClsDialog', popTitle,'./setPsnClsPop.do?menuId=${menuId}&bxId=${param.bxId}&psnClsInfoId='+selId);
				}
			} else if(mode=='delCls'){
				if(selId=='ROOT'){
					alertMsg('cm.msg.mod.root');<%//cm.msg.mod.root=최상위 항목은 수정 할 수 없습니다.%>
				} else if(confirmMsg("cm.cfrm.del")) {<%//cm.cfrm.del=삭제하시겠습니까 ?%>
					callAjax('./transPsnClsDelAjx.do?menuId=${menuId}&bxId=${param.bxId}', {psnClsInfoId:selId}, function(data){
						if(data.message!=null){
							alert(data.message);
						}
						if(data.result=='ok'){
							parent.reloadTree(null, 'psn');
						}
					});
				}
			} else if(mode=='moveCls'){
				if(selId=='ROOT'){
					alertMsg('cm.msg.move.root');<%//cm.msg.move.root=최상위 항목은 이동 할 수 없습니다.%>
					return;
				}
				parent.dialog.open("setMovePsnClsDialog", popTitle, "./treePsnClsPop.do?menuId=${menuId}&bxId=${param.bxId}&callback=callbackMovePsnCls&psnClsInfoId="+selId);
			}
		}
	}
}<%
// 트리의 항목 위로/아래로 이동 %>
function move(direction){
	var sel = TREE.getTree('clsTree').selected;
	if(sel==null){
		alertMsg('cm.msg.noSelect');<%//선택한 항목이 없습니다.%>
	} else if($(sel).attr('id')=='ROOTLI'){
		alertMsg('cm.msg.move.root');<%//최상위 항목은 이동 할 수 없습니다.%>
	} else if(direction=='up' && $(sel).prev().length==0){
		alertMsg('cm.msg.move.first.up');<%//맨 위의 항목 입니다.%>
	} else if(direction=='down' && $(sel).next().length==0){
		alertMsg('cm.msg.move.last.down');<%//맨 아래의 항목 입니다.%>
	} else {
		
		var selId = $(sel).attr('id');
		if(selId.length>2){
			selId = selId.substring(0, selId.length-2);
			callAjax('./transPsnClsMoveAjx.do?menuId=${menuId}&bxId=${param.bxId}', {psnClsInfoId:selId, direction:direction}, function(data){
				if(data.message!=null){
					alert(data.message);
				} else {
					if(direction=='up'){
						$(sel).prev().before(sel);
						$(sel).removeClass('end');
						$(sel).parent().children(":last-child").addClass("end");
					} else if(direction=='down'){
						$(sel).next().removeClass('end');
						$(sel).next().after(sel);
						$(sel).parent().children(":last").addClass("end");
					}
				}
			});
		}
	}
}<%
// 노트클릭 %>
var gClsId=null, gClsNm=null;
function clsTreeClick(id, name){
	gClsId=id;
	if(name==null){
		var sel = TREE.getTree('clsTree').selected;
		var selId = $(sel).attr('id');
		selId = selId.substring(0, selId.length-2);
		gClsNm = $(sel).find("nobr a:last").text();
	} else {
		gClsNm=name;
	}
	${not empty callback ? 'parent.'.concat(callback).concat('(gClsId, gClsNm);') : ''}
}<%
// [확인] 버튼 클릭%>
function setSelectedOrgCls(){
	if(gClsId==null || gClsId=='ROOT'){
		alertMsg("cm.select.check.mandatory",["#ap.jsp.setOrgEnv.tab.deptCls"]);<%//cm.select.check.mandatory="{0}"(을)를 선택해 주십시요.%>
	} else {
		var $from = $("#${empty param.setArea ? 'setDocInfoForm' : param.setArea}");
		if($from.length>0){
			$from.find("#psnClsInfoId").val(gClsId);
			$from.find("#clsInfoNm").val(gClsNm);
			if(setDocNo!=null) setDocNo();
		}
		dialog.close('setClsInfoDialog');
	}
}<%
// onload %>
$(document).ready(function() {
	var tree = TREE.create('clsTree');
	tree.onclick = 'clsTreeClick';
	tree.setRoot('ROOT', '<u:msg titleId="ap.jsp.setOrgEnv.tab.deptCls" alt="분류 정보" />');
	tree.setSkin("${_skin}");
	tree.openLvl = 5;
	
	<c:forEach items="${apPsnClsInfoDVoList}" var="apPsnClsInfoDVo" varStatus="status" >
	tree.add("${apPsnClsInfoDVo.psnClsInfoPid}","${apPsnClsInfoDVo.psnClsInfoId}","<u:out value='${apPsnClsInfoDVo.psnClsInfoNm}' type='script' />","F","${apPsnClsInfoDVo.sortOrdr}","",{id:"${apPsnClsInfoDVo.psnClsInfoId}",pid:"${apPsnClsInfoDVo.psnClsInfoPid}",useYn:"${apPsnClsInfoDVo.useYn}",title:"${apPsnClsInfoDVo.psnClsInfoId}"});</c:forEach>
	tree.add("ROOT","Uncategorized",'<u:msg titleId="cm.msg.uncategorized" alt="미분류" type="script" />',"F","9999","",{id:"Uncategorized",pid:"ROOT",useYn:"Y",title:"Uncategorized"});
	tree.draw();
	<c:if test="${fn:length(apPsnClsInfoDVoList) == 0 }" >tree.selectTree("ROOT");</c:if><c:if
	test="${fn:length(apPsnClsInfoDVoList) > 0 }"><c:if
		test="${not empty param.psnClsInfoId}"
		>tree.selectTree("${param.psnClsInfoId}");tree.selectTree("${param.psnClsInfoId}");</c:if><c:if
		test="${empty param.psnClsInfoId}"
		>tree.selectTree("ROOT");</c:if></c:if>
});
//-->
</script><c:if

	test="${empty isPop}"><%// 기안함 - 분류정보 %>
<div id="clsTree" class="tree"></div>
</c:if><c:if

	test="${not empty isPop}"><%// 기안함 - 분류변경 [팝업] %>
<div style="width:400px;">
	<u:titleArea outerStyle="height:250px; overflow: auto;" innerStyle="NO_INNER_IDV">
		<div id="clsTree" class="tree"></div>
	</u:titleArea>
	<u:buttonArea>
		<u:button titleId="cm.btn.confirm" alt="확인" href="javascript:${empty param.callback ? 'setSelectedOrgCls' : param.callback }();" />
		<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
	</u:buttonArea>
</div>
</c:if>
