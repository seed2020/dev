<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.net.URLEncoder"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

	request.setAttribute("outHeight", "240px");
	request.setAttribute("inHeight", "230px");
	request.setAttribute("divHeight", "215px");
	request.setAttribute("frmHeight", "210px");

	String apvLnGrpId = request.getParameter("apvLnGrpId");
	String tabNo = request.getParameter("tabNo");
	if(apvLnGrpId!=null && !apvLnGrpId.isEmpty()) tabNo = "2";
	else if(tabNo==null || tabNo.isEmpty()) tabNo = "0";
	request.setAttribute("tabNo", tabNo);

%><script type="text/javascript">
<!--<%
// 선택된 탭번호 %>
var gRefVwTabNo = parseInt("${tabNo}");<%
// 사용자 조회창 맨 앞으로 %>
function forwardSearchUserDialog(){
	if(dialog.isOpen('viewUserDialog')) dialog.forward('searchUserDialog');
}<%
// 조직도 클릭시 %>
function openApUserListFrm(id, name, rescId){
	if(gRefVwTabNo==0){
		reloadFrame("refVwUserListFrm", "${_cxPth}/or/user/listUserFrm.do?orgId="+id+"&opt=multi&clickCallback=");
		callFncAtRefVwFrm('deselect');
	} else if(gRefVwTabNo==3){
		reloadFrame("refVwForeignUserListFrm", "${_cxPth}/or/user/listUserFrm.do?orgId="+id+"&opt=multi");
		callFncAtRefVwFrm('deselect');
	}
}<%
// 결재선 목록 프레임의 함수 호출 - 아직 로드되기 전일때 스크립트 오류 방지 %>
function callFncAtRefVwFrm(func, va1, va2, va3, va4){
	var frm = getIframeContent("listRefVwFrm");
	if(frm!=null){
		if(func=='deselect'){
			if(frm.deselectAll != null) frm.deselectAll();
		} else if(func=='setApvLnRoleCd'){
			if(frm.setApvLnRoleCd != null) frm.setApvLnRoleCd(va1, va2, va3, va4);
		} else if(func=='moveLine'){
			if(frm.moveLine != null) frm.moveLine(va1);
		}
	}
}<%
// [탭 클릭] %>
function refVwTabClick(tabNo){
	gRefVwTabNo = tabNo;
	if(tabNo==0){<%// 조직도 탭 클릭 %>
		if(getIframeContent("refVwOrgTreeFrm").location.href.indexOf("reloadable.do")>0){
			reloadFrame("refVwOrgTreeFrm","./treeOrgFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}");
		}
	} else if(tabNo==1) {<%// 임직원 검색 탭 클릭 %>
		getIframeContent("refVwUserSearchFrm").focusName();
	} else if(tabNo==2){<%// 경로그룹 탭 클릭 %>
		if(getIframeContent("refVwGrpFrm").location.href.indexOf("reloadable.do")>0){
			reloadFrame("refVwGrpFrm","./listRefVwGrpFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvLnGrpTypCd=prvRef");
		}
	} else if(tabNo==3){<%// 대외 조직도 탭 클릭 %>
		if(getIframeContent("refVwForeignOrgTreeFrm").location.href.indexOf("reloadable.do")>0){
			reloadFrame("refVwForeignOrgTreeFrm","./treeOrgFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&mode=foreign");
		}
	}
	toggleSaveRefVwGrpBtn();
}<%
// 경로그룹 클릭 %>
var gRefVwGrpTypCd_secu = null;
function clickRefVwGrp(apvLnGrpId, apvLnGrpTypCd){
	gRefVwGrpTypCd_secu = apvLnGrpTypCd; <u:secu auth="S">gRefVwGrpTypCd_secu = null;</u:secu>
	reloadFrame("refVwGrpDetlFrm","./listApvLnGrpDetlFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvLnGrpId="+apvLnGrpId+"&formApvLnTypCd=${param.formApvLnTypCd}&apvLnGrpTypCd="+apvLnGrpTypCd);
	toggleSaveRefVwGrpBtn();
}<%
// 경로그룹 저장 버튼 - 보이기 조절 %>
function toggleSaveRefVwGrpBtn(){
	if(gRefVwTabNo==2 && gRefVwGrpTypCd_secu!='pubRef'){
		$("#saveRefVwGrpBtn").show();
	} else {
		$("#saveRefVwGrpBtn").hide();
	}
}<%
// [소버튼] +추가 %>
function addSelectedRefVw(){
	if(gRefVwTabNo==2){
		addRefVwGrp();
		return;
	}
	var frameId = gRefVwTabNo==0 ? "refVwUserListFrm" : gRefVwTabNo==1 ? "refVwUserSearchFrm" : gRefVwTabNo==3 ? "refVwForeignUserListFrm" : null;
	if(frameId==null) return;
	var arr = getIframeContent(frameId).getSelectedUsers();
	if(arr==null || arr.length==0){
		alertMsg("cm.msg.noSelectedItem",["#cols.user"]);
	} else {<%
		// 서브라인(부서합의)에서 1인결재면 - 사용자 추가 못하게 막음 %>
		if($("#apvLnSetupTypCdArea #byOne").is(":checked") && "${empty param.apvLnPno ? '0' : param.apvLnPno}"!="0"){<%
			// ap.apvLn.noAddApvrAt={0}에서는 결재자를 추가 할 수 없습니다. - ap.term.byOne:1인결재 %>
			alertMsg("ap.apvLn.noAddApvrAt",["#ap.term.byOne"]);
			return;
		}
		var $apvrRoleCdArea = $("#apvrRoleCdArea");
		var $apvLnSetupTypCdArea = $("#apvLnSetupTypCdArea");
		var apvrRoleCd = $apvrRoleCdArea.find("input[name='apvrRoleCd']:checked").val();<%
		// 결재라인설정구분코드 - 이중결재의 경우 통보가 이곳에 위치함 %>
		var apvLnSetupTypCd = $apvLnSetupTypCdArea.find("input[name='apvLnSetupTypCd']:checked").val();
		<%// infm:통보>개인통보, ordrdAgr:순차합의>개인순차합의, paralAgr:병렬합의>개인병렬합의 %>
		apvrRoleCd = (apvLnSetupTypCd=='infm' || apvrRoleCd=='infm') ? 'psnInfm' : apvrRoleCd=='ordrdAgr' ? 'psnOrdrdAgr' : apvrRoleCd=='paralAgr' ? 'psnParalAgr' : apvrRoleCd;
		<%// type:user/dept, apvrRoleCd:결재자역할코드, apvLnSetupTypCd:(이중결재-신청부서:reqDept,처리부서:prcDept), absRsonCd:부재사유코드, absRsonNm:부재사유명 %>
		var opt = {type:'user', apvrRoleCd:apvrRoleCd, apvLnSetupTypCd:apvLnSetupTypCd};
		if(apvrRoleCd=='abs'){
			var $absRsonCd = $apvrRoleCdArea.find("#absRsonCd option:selected");
			opt['absRsonCd'] = $absRsonCd.val();
			opt['absRsonNm'] = $absRsonCd.text();
		}
		if(getIframeContent("listRefVwFrm").addSelected(arr, opt)){<%
			// 선택 목록 프레임에 사용자 추가 후 - 성공하면 %>
			getIframeContent(frameId).deselectUsers();<%// 해당 프레임의 선택 해제%>
		}
	}
}<%
// 경로그룹 - 선택한 것 추가 - [+추가] 버튼 클릭 %>
function addRefVwGrp(){
	var frameId = "refVwGrpDetlFrm";
	var arr = getIframeContent(frameId).getSelectedUsers();
	if(getIframeContent("listRefVwFrm").addRefVwGrp(arr)){<%
		// 선택 목록 프레임에 사용자 추가 후 - 성공하면 %>
		getIframeContent(frameId).deselectUsers();<%// 해당 프레임의 선택 해제%>
	}
}<%
// [소버튼] - 삭제 %>
function delSelectedRefVw(){
	getIframeContent("listRefVwFrm").delSelected();
}<%
// [아이콘] 위 아래 이동 %>
function moveRefVw(direction){
	callFncAtRefVwFrm('moveLine', direction);
}<%
// 경로그룹 저장 버튼 %>
function saveRefVwGrpDetl(){<%
	// 해당 프레임에서 데이터를 모으는데 검증 과정에서 오류가 있으면 null 리턴 %>
	var arr = getIframeContent("listRefVwFrm").getConfirmList(true);
	if(arr==null) return;
	getIframeContent("refVwGrpDetlFrm").saveApvLnGrpDetl(arr);
}<%
// [확인] 버튼 클릭 %>
function collectRefVw(){<%
	// 해당 프레임에서 데이터를 모으는데 검증 과정에서 오류가 있으면 null 리턴 %>
	var arr = getIframeContent("listRefVwFrm").getConfirmList();
	if(arr==null) return;<%
	
	// hidden data area : hidden tag 생성 %>
	var $area = $("#docDataArea #docRefVwArea");
	$area.html("");
	$area.attr("data-modified","Y");
	$area.append("<input type='hidden' name='refVwModified' value='Y' />");
	arr.each(function(index, data){
		$area.append("<input name='refVw' type='hidden' value='"+escapeValue(JSON.stringify(data))+"' />");
	});<%
	// 화면에 표시 %>
	drawRefVw(arr);
	dialog.close("setRefVwDialog");
}<%
// [저장] 버튼 클릭 %>
function saveRefVw(){<%
	// 해당 프레임에서 데이터를 모으는데 검증 과정에서 오류가 있으면 null 리턴 %>
	var arr = getIframeContent("listRefVwFrm").getConfirmList();
	if(arr==null) return;
	
	var refVwList = [];
	arr.each(function(index, obj){
		refVwList.push(JSON.stringify(obj));
	});
	
	callAjax('./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}', {process:'setRefVw',apvNo:'${param.apvNo}', refVwList:refVwList}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if(data.result == 'ok'){<%
			// 화면에 표시 %>
			drawRefVw(arr);
		}
	});
	dialog.close("setRefVwDialog");
}<%
// 참조열람 - 화면에 표시 %>
function drawRefVw(arr){
	var $area = $("#docArea #refVwArea");
	if($area.length==0) return;
	var $tbody = $area.find("tbody:first");
	$tbody.find("tr[id!='refVwListHeader']").remove();
	if(arr.length==0){
		var statCd = "${param.statCd}";
		if(statCd == 'mak'){
			$tbody.append('<tr><td class="body_ct"></td><td class="body_ct"></td><td class="body_ct"></td><td class="body_ct"></td><td style="text-align: center;"></td></tr>');
		} else {
			$area.hide();
		}
	} else {
		arr.each(function(index, data){
			$tbody.append('<tr><td class="body_ct"></td><td class="body_ct"></td><td class="body_ct"></td><td class="body_ct"></td><td style="text-align: center;"></td></tr>');
			$tbody.find("tr:last td").each(function(index){
				if(index == 0){
					if(data.refVwrDeptNm != null) $(this).text(data.refVwrDeptNm);
				} else if(index == 1){
					if(data.refVwrPositNm != null) $(this).text(data.refVwrPositNm);
				} else if(index == 2){
					if(data.refVwrPositNm != null){
						if(data.refVwrUid!=null && data.refVwrUid!=''){
							$(this).html("<a href=\"javascript:viewUserPop('"+data.refVwrUid+"')\">"+data.refVwrNm+"</a>");
						} else {
							$(this).text(data.refVwrNm);
						}
					}
				} else if(index == 3){
					if(data.refVwDt != null){
						$(this).text(data.refVwDt);
					}
				} else if(index == 4){
					if(data.hasOpin == 'Y'){
						$(this).html("<a href=\"javascript:openDetl('refVw')\"><img alt=\"ballen\" src=\"/images/cm/ballon.gif\" /></a>");
					}
				}
			});
		});
		$area.show();
	}
}
function setApvLnSetTypCdByRole(apvrRoleCd){}
//-->
</script>
<div style="width:900px">

<u:tabGroup id="refVwTab" noBottomBlank="true">
	<u:tab onclick="refVwTabClick(0)" areaId="refVwUserTreeArea" alt="조직도" titleId="or.jsp.setOrg.orgTreeTitle" id="refVwTab" on="${tabNo == '0'}" /><c:if
		test="${not empty apvrFromOtherComp and (empty param.upward and empty param.downward and empty param.oneDeptId)}">
	<u:tab onclick="refVwTabClick(3)" areaId="refVwForeignUserTreeArea" alt="대외 조직도" titleId="ap.jsp.ForeignComp" id="refVwTab" on="${tabNo == '3'}" /></c:if>
	<u:tab onclick="refVwTabClick(1)" areaId="refVwUserSchArea" alt="임직원 검색" titleId="pt.top.orgSrch" id="refVwTab" on="${tabNo == '1'}" />
	<u:tab onclick="refVwTabClick(2)" areaId="refVwGrpArea" alt="경로그룹" titleId="ap.jsp.apvLnGrp" id="refVwTab" on="${tabNo == '2'}" />
</u:tabGroup>

<u:tabArea
	outerStyle="height:${outHeight}; overflow-x:hidden; overflow-y:hidden;"
	innerStyle="height:${inHeight}; margin:10px;">

<u:cmt cmt="[조직도]탭 영역" />
<div id="refVwUserTreeArea" <c:if test="${tabNo != '0'}">style="display:none"</c:if>>

<u:cmt cmt="[조직도] - (좌)트리 영역" />
<div style="float:left; width:28.8%;">
<u:titleArea
	outerStyle="height:${divHeight}; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"><u:set
		test="${tabNo == '0'}" var="orgTreeUrl" value="./treeOrgFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam
		}${not empty param.upward ? '&upward='.concat(param.upward) : ''
		}${not empty param.downward ? '&downward='.concat(param.downward) : ''
		}${not empty param.oneDeptId ? '&oneDeptId='.concat(param.oneDeptId) : ''
		}${not empty param.selOrgId ? '&orgId='.concat(param.selOrgId) : ''
		}" elseValue="${_cxPth}/cm/util/reloadable.do" />
<iframe id="refVwOrgTreeFrm" name="refVwOrgTreeFrm" src="${orgTreeUrl}" style="width:100%; height:${frmHeight};" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:titleArea>
</div>

<u:cmt cmt="[조직도] - (우)사용자 목록 영역" />
<div style="float:right; width:70%;">
<u:titleArea
	outerStyle="height:${divHeight}; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV">
<iframe id="refVwUserListFrm" name="refVwUserListFrm" src="${_cxPth}/cm/util/reloadable.do" style="width:100%; height:${frmHeight};" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:titleArea>
</div>

</div>

<c:if test="${not empty apvrFromOtherComp and (empty param.upward and empty param.downward and empty param.oneDeptId)}">
<u:cmt cmt="[대외 조직도]탭 영역" />
<div id="refVwForeignUserTreeArea" <c:if test="${tabNo != '3'}">style="display:none"</c:if>>

<u:cmt cmt="[대외 조직도] - (좌)트리 영역" />
<div style="float:left; width:28.8%;">
<u:titleArea
	outerStyle="height:${divHeight}; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"><u:set
		test="${tabNo == '3'}" var="orgTreeUrl" value="./treeOrgFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam
		}&mode=foreign" elseValue="${_cxPth}/cm/util/reloadable.do" />
<iframe id="refVwForeignOrgTreeFrm" name="refVwForeignOrgTreeFrm" src="${orgTreeUrl}" style="width:100%; height:${frmHeight};" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:titleArea>
</div>

<u:cmt cmt="[대외 조직도] - (우)사용자 목록 영역" />
<div style="float:right; width:70%;">
<u:titleArea
	outerStyle="height:${divHeight}; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV">
<iframe id="refVwForeignUserListFrm" name="refVwForeignUserListFrm" src="${_cxPth}/cm/util/reloadable.do" style="width:100%; height:${frmHeight};" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:titleArea>
</div>

</div></c:if>

<u:cmt cmt="[임직원 검색]탭 영역" />
<div id="refVwUserSchArea" <c:if test="${tabNo != '1'}">style="display:none"</c:if>>
<iframe id="refVwUserSearchFrm" name="refVwUserSearchFrm" src="${_cxPth}/or/user/searchUserFrm.do?opt=multi&userStatCd=02${
	empty apvrFromOtherComp ? '&compId='.concat(sessionScope.userVo.compId) : ''
	}${not empty param.upward ? '&upward='.concat(param.upward) : ''
	}${not empty param.downward ? '&downward='.concat(param.downward) : ''
	}${not empty param.oneDeptId ? '&oneDeptId='.concat(param.oneDeptId) : ''
	}" style="width:100%; height:${inHeight};" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</div>

<u:cmt cmt="[경로 룹]탭 영역" />
<div id="refVwGrpArea" <c:if test="${tabNo != '2'}">style="display:none"</c:if>>
<u:cmt cmt="[경로그룹 목록] - (좌) 영역" />
<div style="float:left; width:28.8%;">
<u:titleArea
	outerStyle="height:${divHeight}; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"><u:set
		test="${tabNo == '2'}" var="apvLnGrpUrl" value="./listRefVwGrpFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvLnGrpId=${param.apvLnGrpId}&apvLnGrpTypCd=prvRef${empty param.fixdApvrYn ? '' : '&fixdApvrYn='.concat(param.fixdApvrYn)}" elseValue="${_cxPth}/cm/util/reloadable.do" />
<iframe id="refVwGrpFrm" name="refVwGrpFrm" src="${apvLnGrpUrl}" style="width:100%; height:${frmHeight};" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:titleArea>
</div>

<u:cmt cmt="[경로그룹 별 사용자] - (우)" />
<div style="float:right; width:70%;">
<u:titleArea
	outerStyle="height:${divHeight}; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"><u:set test="${empty param.apvLnGrpId}"
		var="listApvLnGrpDetlFrmUrl"
		value="${_cxPth}/cm/util/reloadable.do"
		elseValue="${_cxPth}/ap/box/listApvLnGrpDetlFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvLnGrpTypCd=prvRef&apvLnGrpId=${param.apvLnGrpId}&initAct=auto" />
<iframe id="refVwGrpDetlFrm" name="refVwGrpDetlFrm" src="${listApvLnGrpDetlFrmUrl}" style="width:100%; height:${frmHeight};" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:titleArea>
</div>
</div>

</u:tabArea>

<u:title alt="열람자" titleId="ap.term.refVwr" >
	<u:titleButton titleId="cm.btn.add" alt="추가" id="addUserBtn" img="ico_add.png" href="javascript:addSelectedRefVw()" />
	<u:titleButton titleId="cm.btn.del" alt="삭제" id="delBtn" img="ico_minus.png" href="javascript:delSelectedRefVw()" />
	<u:titleIcon type="up" href="javascript:moveRefVw('up')" />
	<u:titleIcon type="down" href="javascript:moveRefVw('down')" />
</u:title>

<u:titleArea
	outerStyle="height:${divHeight}; overflow-x:hidden; overflow-y:auto; padding:0px"
	innerStyle="NO_INNER_IDV">
<iframe id="listRefVwFrm" name="listRefVwFrm" src="./listRefVwFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam
		}<c:if
		test="${not empty param.apvNo}">&apvNo=${param.apvNo}</c:if><c:if
		test="${not empty param.modified}">&modified=${param.modified}</c:if><c:if
		test="${tabNo == '2'}">&noInit=Y</c:if>" style="width:100%; height:${frmHeight};" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:titleArea>

<u:buttonArea>
	<u:button titleId="ap.btn.saveApvLnGrp" href="javascript:saveRefVwGrpDetl();" alt="경로그룹 저장"
		id="saveRefVwGrpBtn" style="${tabNo != '2' ? 'display:none;' : ''}" /><c:if
			test="${param.statCd eq 'mak'}">
	<u:button titleId="cm.btn.confirm" href="javascript:collectRefVw();" alt="확인" /></c:if><c:if
			test="${param.statCd ne 'mak'}">
	<u:button titleId="cm.btn.save" href="javascript:saveRefVw();" alt="저장" /></c:if>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>