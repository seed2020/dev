<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// 노트클릭 %>
var gClsId="${param.clsInfoId}", gClsNm="";
function clsTreeClick(id, name){
	gClsId=id;
	if(name==null){
		var sel = TREE.getTree("${empty treeId ? 'clsTree' : treeId}").selected;
		var selId = $(sel).attr('id');
		selId = selId.substring(0, selId.length-2);
		gClsNm = $(sel).find("nobr a:last").text();
	} else {
		gClsNm=name;
	}
	${not empty callback ? 'parent.'.concat(callback).concat('(gClsId, gClsNm);') : ''}
}<%
// 일괄 분류 등록, 분류 추가, 분류 수정, 분류 삭제 - addAllCls, addCls, modCls, delCls - 부모창에서 호출 함 %>
function manageCls(mode){
	var popTitle = callMsg("ap.btn."+mode);
	if(mode=='addAllCls'){
		
	} else {
		var sel = TREE.getTree("${empty treeId ? 'clsTree' : treeId}").selected;
		if(sel==null){
			alertMsg('cm.msg.noSelect');<%//선택한 항목이 없습니다.%>
		} else {
			var selId = $(sel).attr('id');
			selId = selId.substring(0, selId.length-2);
			if(mode=='addCls'){
				parent.dialog.open('setOrgClsDialog', popTitle,'./setOrgClsPop.do?menuId=${menuId}${orgParam}&clsInfoPid='+selId);
			} else if(mode=='modCls'){
				if(selId=='ROOT'){
					alertMsg('cm.msg.mod.root');<%//cm.msg.mod.root=최상위 항목은 수정 할 수 없습니다.%>
				} else {
					parent.dialog.open('setOrgClsDialog', popTitle,'./setOrgClsPop.do?menuId=${menuId}${orgParam}&clsInfoId='+selId);
				}
			} else if(mode=='delCls'){
				if(selId=='ROOT'){
					alertMsg('cm.msg.mod.root');<%//cm.msg.mod.root=최상위 항목은 수정 할 수 없습니다.%>
				} else if(confirmMsg("cm.cfrm.del")) {<%//cm.cfrm.del=삭제하시겠습니까 ?%>
					callAjax('./transOrgClsDelAjx.do?menuId=${menuId}${orgParam}', {clsInfoId:selId}, function(data){
						if(data.message!=null){
							alert(data.message);
						}
						parent.setDetl('Cls');
					});
				}
			} else if(mode=='moveCls'){
				if(selId=='ROOT'){
					alertMsg('cm.msg.move.root');<%//cm.msg.move.root=최상위 항목은 이동 할 수 없습니다.%>
					return;
				}
				parent.dialog.open("setMoveOrgClsDialog", popTitle, "./treeOrgClsPop.do?menuId=${menuId}${not empty param.orgId ? '&orgId='.concat(param.orgId): ''}&callback=callbackMoveCls&clsInfoId="+selId);
			}
		}
	}
}<%
// 트리의 항목 위로/아래로 이동 %>
function move(direction){
	var sel = TREE.getTree("${empty treeId ? 'clsTree' : treeId}").selected;
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
			callAjax('./transOrgClsMoveAjx.do?menuId=${menuId}${orgParam}', {clsInfoId:selId, direction:direction}, function(data){
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
// [확인] 버튼 클릭%>
function setSelectedOrgCls(){
	if(gClsId==null || gClsId=='ROOT'){
		alertMsg("cm.select.check.mandatory",["#ap.jsp.setOrgEnv.tab.deptCls"]);<%//cm.select.check.mandatory="{0}"(을)를 선택해 주십시요.%>
	} else {
		var $from = $("#${empty param.setArea ? 'setDocInfoForm' : param.setArea}");
		if($from.length>0){
			$from.find("#clsInfoId").val(gClsId);
			$from.find("#clsInfoNm").val(gClsNm);
			if(setDocNo!=null) setDocNo();
		}
		dialog.close('setClsInfoDialog');
	}
}<%
// [접수] 버튼 클릭%>
function processClsInfo(){
	var needCls = "${param.noCls=='Y' ? 'N' : 'Y'}";
	if(needCls!='N' && (gClsId==null || gClsId=='ROOT')){<%
		//cm.select.check.mandatory="{0}"(을)를 선택해 주십시요.%>
		alertMsg("cm.select.check.mandatory",["#ap.jsp.setOrgEnv.tab.deptCls"]);
	} else {<%
		// processRecv:setDoc.jsp, processBulkRecv:listApvBx.jsp %>
		var secul = $('#seculPopArea #seculCd').val();
		${empty param.callback ? 'processRecv' : param.callback}(needCls!='N' ? gClsId : null, secul);
	}
}<%
// onload %>
$(document).ready(function() {
	var tree = TREE.create("${empty treeId ? 'clsTree' : treeId}");
	tree.onclick = 'clsTreeClick';
	tree.setRoot('ROOT', '<u:msg titleId="ap.jsp.setOrgEnv.tab.deptCls" alt="분류 정보" />');
	tree.setSkin("${_skin}");
	tree.openLvl = 5;
	
	<c:forEach items="${apClsInfoDVoList}" var="apClsInfoDVo" varStatus="status" >
	tree.add("${apClsInfoDVo.clsInfoPid}","${apClsInfoDVo.clsInfoId}","<u:out value='${apClsInfoDVo.rescNm}' type='script' />","F","${apClsInfoDVo.sortOrdr}","${apClsInfoDVo.rescId}",{id:"${apClsInfoDVo.clsInfoPid}",pid:"${apClsInfoDVo.clsInfoId}",rescId:"${apClsInfoDVo.rescId}",useYn:"${apClsInfoDVo.useYn}",title:"${apClsInfoDVo.clsInfoId}"});</c:forEach>
	
	tree.draw();
	<c:if test="${fn:length(apClsInfoDVoList) == 0 }" >tree.selectTree("ROOT");</c:if><c:if
	test="${fn:length(apClsInfoDVoList) > 0 }"><c:if
		test="${not empty param.clsInfoId}"
		>tree.selectTree("${param.clsInfoId}");tree.selectTree("${param.clsInfoId}");</c:if><c:if
		test="${empty param.clsInfoId}"
		>tree.selectTree("ROOT");</c:if></c:if>
});
//-->
</script><c:if

	test="${not empty forRecLst}"><%// 등록대장, 접수대장 - 분류정보 %>
<div id="${empty treeId ? 'clsTree' : treeId}" class="tree"></div>
</c:if><c:if

	test="${not empty endUserPage}"><%// 사용자의 분류정보 선택 팝업 %>
<div style="width:400px;"><c:if
		test="${empty param.noCls}">
	<u:titleArea outerStyle="height:250px; overflow: auto;" innerStyle="NO_INNER_IDV">
		<div id="${empty treeId ? 'clsTree' : treeId}" class="tree"></div>
	</u:titleArea></c:if><c:if
		test="${not empty param.withSecul}">
	<u:listArea colgroup="30%,77%" id="seculPopArea">
	<tr>
	<td class="head_lt"><u:msg titleId="ap.doc.seculNm" alt="보안등급" /></td>
	<td colspan="3" class="body_lt"><select id="seculCd" name="seculCd"<u:elemTitle titleId="ap.doc.seculNm" alt="보안등급" />>
			<option value="none"><u:msg titleId="cm.option.noSelect" alt="선택안함" /></option><c:forEach
			items="${seculCdList}" var="seculCd">
			<u:option value="${seculCd.cd}" title="${seculCd.rescNm}" checkValue="${param.seculCd}" /></c:forEach>
			</select></td>
	</tr>
	</u:listArea>
	</c:if>
	<u:buttonArea><c:if
			test="${param.bxId == 'recvBx'}"><c:if
				test="${param.callback == 'processBulkRecv'}">
		<u:button titleId="ap.btn.bulkRecv" alt="일괄접수" href="javascript:processClsInfo();" /></c:if><c:if
				test="${param.callback != 'processBulkRecv'}">
		<u:button titleId="ap.btn.recv" alt="접수" href="javascript:processClsInfo();" /></c:if></c:if><c:if
			test="${param.bxId != 'recvBx'}">
		<u:button titleId="cm.btn.confirm" alt="확인" href="javascript:${empty param.callback ? 'setSelectedOrgCls' : param.callback }();" /></c:if>
		<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
	</u:buttonArea>
</div>
</c:if><c:if

	test="${empty endUserPage and empty forRecLst}"><%// 문서담당자 - 부서정보/[탭]분류정보, 관리자 - 포털/조직관리/조직도 사용자 관리/[탭]분류정보 %>
	<u:set test="${not empty adminPage}" var="areaHeight" value="557px" elseValue="386px" />
	<div style="padding:10px 10px 0px 10px; margin-bottom:-10px">
		<u:titleArea outerStyle="height: ${areaHeight}; overflow: auto;" innerStyle="NO_INNER_IDV" noBottomBlank="true">
			<div id="${empty treeId ? 'clsTree' : treeId}" class="tree"></div>
		</u:titleArea>
	</div>
</c:if>
