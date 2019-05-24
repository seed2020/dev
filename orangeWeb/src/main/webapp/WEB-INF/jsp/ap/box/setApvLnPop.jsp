<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.net.URLEncoder"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

	request.setAttribute("outHeight", "240px");
	request.setAttribute("inHeight", "230px");
	request.setAttribute("divHeight", "215px");
	request.setAttribute("frmHeight", "210px");

	String apvLnGrpId = request.getParameter("apvLnGrpId");
	String autoApvLnCd = request.getParameter("autoApvLnCd");
	String autoInit = request.getParameter("autoInit");
	
	String tabNo = request.getParameter("tabNo");
	if((apvLnGrpId!=null && !apvLnGrpId.isEmpty()) || (autoApvLnCd!=null && !autoApvLnCd.isEmpty())){
		tabNo = "2";
	} else if(tabNo==null || tabNo.isEmpty()){
		tabNo = "0";
	}
	request.setAttribute("tabNo", tabNo);
	
	String apvrRoleCd = request.getParameter("apvrRoleCd");
	java.util.Map<String, String> optConfigMap = (java.util.Map<String, String>)request.getAttribute("optConfigMap");
	
	// 접수 후 담당자가 결재선 지정할때
	if("makVw".equals(apvrRoleCd)){// makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람
		String[] apvrRoleCds = {"fstVw","pubVw","paralPubVw"};
		request.setAttribute("apvrRoleCds", apvrRoleCds);
	} else if("fstVw".equals(apvrRoleCd) || "pubVw".equals(apvrRoleCd)){// fstVw:선람, :공람, paralPubVw:동시공람
		String[] apvrRoleCds = {"pubVw","paralPubVw"};
		request.setAttribute("apvrRoleCds", apvrRoleCds);
	// 부서합의 결재라인 설정
	} else if(request.getParameter("apvLnPno") != null && !"0".equals(request.getParameter("apvLnPno"))){
		// 순차합의, 병렬합의, 사후보고(후열), 통보 - 제거
		String[] apvrRoleCds = {"revw","abs",
			"Y".equals(optConfigMap.get("revw2Enable")) ? "revw2" : null,
			"Y".equals(optConfigMap.get("revw3Enable")) ? "revw3" : null,
			"apv","pred","entu"};
		request.setAttribute("apvrRoleCds", apvrRoleCds);
	// 이중결재 결재라인 설정
	} else if("apvLnDbl".equals(request.getParameter("formApvLnTypCd"))
			|| "apvLnDblList".equals(request.getParameter("formApvLnTypCd"))){
		// 통보 제거, 처리부서 쪽으로 통보 이동
		String[] apvrRoleCds = {"revw","abs","ordrdAgr","paralAgr",
				"Y".equals(optConfigMap.get("revw2Enable")) ? "revw2" : null,
				"Y".equals(optConfigMap.get("revw3Enable")) ? "revw3" : null,
				"apv","pred","entu","postApvd"};
		request.setAttribute("apvrRoleCds", apvrRoleCds);
	// 일반 결재라인
	} else {
		String[] apvrRoleCds = {"revw","abs","ordrdAgr","paralAgr",
				"Y".equals(optConfigMap.get("revw2Enable")) ? "revw2" : null,
				"Y".equals(optConfigMap.get("revw3Enable")) ? "revw3" : null,
				"apv","pred","entu","postApvd","infm"};
		request.setAttribute("apvrRoleCds", apvrRoleCds);
	}
	
%><u:set
	test="${(param.formApvLnTypCd eq 'apvLnDbl' or param.formApvLnTypCd eq 'apvLnDblList')
		and param.makrRoleCd != 'makVw'}" var="isDblApvLn" value="Y" />
<script type="text/javascript">
<!--<%
// 결재 옵션 %>
var gApvLnOptConfig = ${optConfigJson};<%
// 선택된 탭번호 %>
var gApvLnTabNo = parseInt("${tabNo}");<%
// 사용자 부서 정보 %>
var gDeptId = "${sessionScope.userVo.deptId}";<%
// 양식 결재라인 타입 %>
var gFormApvLnTypCd = "${param.formApvLnTypCd}";<%
// 기안자 역할 코드 %>
var gMakrRoleCd = "${param.makrRoleCd}";<%
// 사용자 조회창 맨 앞으로 %>
function forwardSearchUserDialog(){
	if(dialog.isOpen('viewUserDialog')) dialog.forward('searchUserDialog');
}<%
// 조직도 클릭시 %>
function openApUserListFrm(id, name, rescId){
	if(gApvLnTabNo==0){
		reloadFrame("apvLnUserListFrm", "${_cxPth}/or/user/listUserFrm.do?orgId="+id+"&opt=multi");
		callFncAtApvLnFrm('deselect');
	} else if(gApvLnTabNo==3){
		reloadFrame("apvLnForeignUserListFrm", "${_cxPth}/or/user/listUserFrm.do?orgId="+id+"&opt=multi");
		callFncAtApvLnFrm('deselect');
	}
}<%
// [탭 클릭] %>
function apvLnUserTabClick(tabNo){
	gApvLnTabNo = tabNo;
	if(tabNo==0){<%// 조직도 탭 클릭 %>
		if(getIframeContent("apvLnOrgTreeFrm").location.href.indexOf("reloadable.do")>0){
			reloadFrame("apvLnOrgTreeFrm","./treeOrgFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}");
		}
	} else if(tabNo==1) {<%// 임직원 검색 탭 클릭 %>
		getIframeContent("apvLnUserSearchFrm").focusName();
	} else if(tabNo==2){<%// 경로그룹 탭 클릭 %>
		if(getIframeContent("apvLnGrpFrm").location.href.indexOf("reloadable.do")>0){
			reloadFrame("apvLnGrpFrm","./listApvLnGrpFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvLnGrpTypCd=prv");
		}
	} else if(tabNo==3){<%// 대외 조직도 탭 클릭 %>
		if(getIframeContent("apvLnForeignOrgTreeFrm").location.href.indexOf("reloadable.do")>0){
			reloadFrame("apvLnForeignOrgTreeFrm","./treeOrgFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&mode=foreign");
		}
	}
	toggleSaveApvLnGrpBtn();
}<%
// [소버튼] +추가 %>
function addSelectedUser(){
	if(gApvLnTabNo==2){
		addApvLnGrp();
		return;
	}
	var frameId = gApvLnTabNo==0 ? "apvLnUserListFrm" : gApvLnTabNo==1 ? "apvLnUserSearchFrm" : gApvLnTabNo==3 ? "apvLnForeignUserListFrm" : null;
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
		}<%
		// reverse : 조직도의 사용자는 높은 지위가 위에 위치하므로 결재라인는 높은 사람이 밑으로 가도록 뒤집어서 넣어줌 %>
		if(getIframeContent("listApvLnFrm").addSelected(arr.reverse(), opt)){<%
			// 선택 목록 프레임에 사용자 추가 후 - 성공하면 %>
			getIframeContent(frameId).deselectUsers();<%// 해당 프레임의 선택 해제%>
		}
	}
}<%
// 경로그룹 - 선택한 것 추가 - [+추가] 버튼 클릭 %>
function addApvLnGrp(){
	var frameId = "apvLnGrpDetlFrm";
	var arr = getIframeContent(frameId).getSelectedUsers();
	if(getIframeContent("listApvLnFrm").addApvLnGrp(arr)){<%
		// 선택 목록 프레임에 사용자 추가 후 - 성공하면 %>
		getIframeContent(frameId).deselectUsers();<%// 해당 프레임의 선택 해제%>
	}
}<%
// [소버튼] +부서추가 %>
function addSelectedDept(){
	var frmId = gApvLnTabNo==0 ? "apvLnOrgTreeFrm" : gApvLnTabNo==3 ? "apvLnForeignOrgTreeFrm" : null;
	var arr = (frmId==null) ? null : getIframeContent(frmId).getSelectedDept();
	if(arr!=null && arr.length>0){
		var $apvrRoleCdArea = $("#apvrRoleCdArea");
		var $apvLnSetupTypCdArea = $("#apvLnSetupTypCdArea");
		var apvrRoleCd = $apvrRoleCdArea.find("input[name='apvrRoleCd']:checked").val();
		var apvLnSetupTypCd = $apvLnSetupTypCdArea.find("input[name='apvLnSetupTypCd']:checked").val();
		apvrRoleCd = apvLnSetupTypCd=='prcDept' ? 'prcDept' : (apvLnSetupTypCd=='infm' || apvrRoleCd=='infm') ? 'deptInfm' : apvrRoleCd=='ordrdAgr' ? 'deptOrdrdAgr' : apvrRoleCd=='paralAgr' ? 'deptParalAgr' : apvrRoleCd;
		
		var opt = {type:'dept', apvrRoleCd:apvrRoleCd, apvLnSetupTypCd:apvLnSetupTypCd};
		getIframeContent("listApvLnFrm").addSelected(arr, opt);
	}
}<%
// [소버튼] - 삭제 %>
function delSelected(){
	getIframeContent("listApvLnFrm").delSelected();
}<%
// [아이콘] 위 아래 이동 %>
function moveLine(direction){
	callFncAtApvLnFrm('moveLine', direction);
}<%
// 이중결재 - 신청부서 / 처리부서 / 통보 - 클릭
//		type:dbl, roleCd:(reqDept:신청부서, prcDept:처리부서, infm:통보)
// 이중결재외 - 일반결재 / 1인결재 - 클릭
// 		type:noml, roleCd(revw:검토, abs:공석, ordrdAgr:순차합의, paralAgr:병렬합의, 
//						apv:결재, pred:전결, entu:결재안함(위임), postApvd:사후보고(후열), infm:통보)
//%>
function clickApvLnSetupTypCd(type, setupCd){
	if(setupCd=='') return;
	var $apvrRoleCdArea = $("#apvrRoleCdArea");
	var $apvLnSetupTypCdArea = $("#apvLnSetupTypCdArea");
	if(type=='dbl'){
		if(setupCd=='reqDept'){
			toggleArea(["ordrdAgr","paralAgr","apv","pred","entu","postApvd","infm"], $apvrRoleCdArea, 'hide');
			toggleArea(["revw","revw2","revw3","abs"], $apvrRoleCdArea, 'show');
			checkFirstVisible($apvrRoleCdArea);
		} else if(setupCd=='prcDept') {
			toggleArea(["ordrdAgr","paralAgr"], $apvrRoleCdArea, 'hide');
			toggleArea(["revw","revw2","revw3","abs","apv","pred","entu","postApvd","infm"], $apvrRoleCdArea, 'show');
			checkFirstVisible($apvrRoleCdArea);
		} else if(setupCd=='infm') {
			toggleArea(["revw","revw2","revw3","abs","ordrdAgr","paralAgr","apv","pred","entu","postApvd","infm"], $apvrRoleCdArea, 'hide');
		}
		var apvLnSetupTypCd = $apvLnSetupTypCdArea.find("input[name='apvLnSetupTypCd']:checked").val();
		var prcDeptYn = '${param.prcDeptYn}';
		if(gApvLnTabNo!=0 || prcDeptYn=='Y'){
			$("#addDeptBtn").hide();
		} else if(apvLnSetupTypCd=='prcDept' && gApvLnOptConfig['prcDeptEnab']=='Y'){
			$("#addDeptBtn").show();
		} else if(apvLnSetupTypCd=='infm' && gApvLnOptConfig['deptInfmEnab']=='Y'){
			$("#addDeptBtn").show();
		} else {
			$("#addDeptBtn").hide();
		}
		var roleCd = $apvrRoleCdArea.find("input[name='apvrRoleCd']:checked").val(), absRsonCd=null, absRsonNm=null;
		if(roleCd=='abs'){
			var $absRsonCd = $apvrRoleCdArea.find("#absRsonCd option:selected");
			absRsonCd = $absRsonCd.val();
			absRsonNm = $absRsonCd.text();
			$apvrRoleCdArea.find("#absRsonCdArea").show();
		} else {
			$apvrRoleCdArea.find("#absRsonCdArea").hide();
		}
		callFncAtApvLnFrm('setApvLnRoleCd', setupCd, roleCd, absRsonCd, absRsonNm);
	} else if(type=='noml'){
		if(setupCd=='nomlApv'){
			toggleArea(["revw","abs","ordrdAgr","apv","pred","entu","postApvd","infm"], $apvrRoleCdArea, 'show');
			toggleArea(["paralAgr"], $apvrRoleCdArea, gApvLnOptConfig['parlEnab']=='Y' ? 'show' : 'hide');
			checkFirstVisible($apvrRoleCdArea, "${param.makrRoleCd=='makVw' ? 'pubVw' : ''}");
		} else if(setupCd=='byOne') {
			toggleArea(["revw","abs","apv","pred","entu","ordrdAgr","paralAgr"], $apvrRoleCdArea, 'hide');
			toggleArea(["postApvd","infm"], $apvrRoleCdArea, 'show');
			checkFirstVisible($apvrRoleCdArea);
		}
		clickApvrRoleCd();
	}
}<%
// 기안,검토,결재 등의 radio 클릭 %>
function clickApvrRoleCd(roleCd, notCallFunc){
	var $apvrRoleCdArea = $("#apvrRoleCdArea");
	var $apvLnSetupTypCdArea = $("#apvLnSetupTypCdArea");
	if(roleCd==null) roleCd = $apvrRoleCdArea.find("input[name='apvrRoleCd']:checked").val();
	$apvrRoleCdArea.find("#absRsonCdArea").css("display", roleCd=='abs' ? '' : 'none');<%//공석사유 - 보이기/숨기기%><%
	// 이중결재:처리부서, 통보 이거나, 
	// ordrdAgr:순차합의, paralAgr:병렬합의, infm:통보 - 의 경우만 [부서추가] 버튼 보이게 %>
	var apvLnSetupTypCd = $apvLnSetupTypCdArea.find("input[name='apvLnSetupTypCd']:checked").val();
	if(apvLnSetupTypCd=='nomlApv' || apvLnSetupTypCd=='byOne'){<%
		// [부서추가] 버튼제어
		// 이중결재 아닐 경우 - nomlApv:일반결재, byOne:1인결재
		//    (순차합의, 병렬합의) + [옵션]부서합의 사용 : [부서추가] 버튼 보임 %>
		if((gApvLnTabNo==0 || gApvLnTabNo==3) && (roleCd=='ordrdAgr' || roleCd=='paralAgr') && gApvLnOptConfig['deptAgrEnab']=='Y'){
			$("#addDeptBtn").show();<%
		//    통보 + [옵션]부서통보 사용 : [부서추가] 버튼 보임 %>
		} else if((gApvLnTabNo==0 || gApvLnTabNo==3) && roleCd=='infm' && gApvLnOptConfig['deptInfmEnab']=='Y') {
			$("#addDeptBtn").show();
		} else {
			$("#addDeptBtn").hide();
		}
	}<%
	// 공석의 경우
	//    - 공석 사유 코드/텍스트 세팅
	//    - 공석 사유 select 보이기 설정 %>
	var absRsonCd=null, absRsonNm=null;
	if(roleCd=='abs'){
		var $absRsonCd = $apvrRoleCdArea.find("#absRsonCd option:selected");
		absRsonCd = $absRsonCd.val();
		absRsonNm = $absRsonCd.text();
		$apvrRoleCdArea.find("#absRsonCdArea").show();
	} else {
		$apvrRoleCdArea.find("#absRsonCdArea").hide();
	}<%
	// 하단 프레임 - 선택된 것 - 현재 클릭된 정보로 변경 %>
	if(notCallFunc != false){
		callFncAtApvLnFrm('setApvLnRoleCd', $apvLnSetupTypCdArea.find("input[name='apvLnSetupTypCd']:checked").val(), roleCd, absRsonCd, absRsonNm);
	}
}<%
// 해당영역 보이기 / 숨기기 %>
function toggleArea(arr, $apvrRoleCdArea, visible){
	var $check;
	arr.each(function(index, va){
		if(visible=='show'){
			$apvrRoleCdArea.find("#"+va+"Area").show();
			if(va=='abs') $apvrRoleCdArea.find("#absRsonCdArea").show();
		} else {
			$check = $apvrRoleCdArea.find("#"+va);
			if($check.is(":checked")) $check.checkInput(false);
			$apvrRoleCdArea.find("#"+va+"Area").hide();
			if(va=='abs') $apvrRoleCdArea.find("#absRsonCdArea").hide();
		}
	});
}<%
// 첫째 보이는 체크박스(검토, 결재 ..) 체크 %>
function checkFirstVisible($area, roleCd){
	if($area.find("input[type='radio']:checked:visible").length==0){
		var checked = false;
		if(roleCd != null && roleCd!=''){
			if($area.find("#"+roleCd).length>0){
				$area.find("#"+roleCd).checkInput(true);
				checked = true;
			}
		}
		if(!checked){
			$area.find("input[type='radio']:visible:first").checkInput(true);
		}
	}
}<%
// 조직도의 사용자 체크박스 클릭 or 임직원조회의 사용자 체크박스 클릭 %>
function clickUserCheck(userUid){
	callFncAtApvLnFrm('deselect');
}<%
// 결재선 목록 프레임의 함수 호출 - 아직 로드되기 전일때 스크립트 오류 방지 %>
function callFncAtApvLnFrm(func, va1, va2, va3, va4){
	var frm = getIframeContent("listApvLnFrm");
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
// 확인 버튼 클릭 %>
function collectApvLn(auto){<%
	// 해당 프레임에서 데이터를 모으는데 검증 과정에서 오류가 있으면 null 리턴 %>
	var arr = getIframeContent("listApvLnFrm").getConfirmList(auto);
	if(arr==null){<%// 실제로 해당 경우 없음, safe 코드 %>
		return;
	}<%
	
	// hidden data area : hidden tag 생성 %>
	var $area = $("#docDataArea #docApvLnArea");
	$area.html("");
	
	var $apvLnSetupTypCdArea = $("#apvLnSetupTypCdArea");
	var cd = $apvLnSetupTypCdArea.find("input[name='apvLnSetupTypCd']:checked").val();<%
	// 진행문서결재라인상세(AP_ONGD_APV_LN_D) - 결재라인구분코드 %><c:if test="${empty param.apvLnPno or param.apvLnPno == '0'}">
	var apvLnTypCd = cd=='byOne' ? 'byOne' : (cd=='reqDept' || cd=='prcDept') ? 'dblApv' : 'nomlApv';
	$area.append("<input name='apvLnTypCd' type='hidden' value='"+apvLnTypCd+"' />");</c:if>
	arr.each(function(index, data){
		$area.append("<input name='apvLn' type='hidden' value='"+escapeValue(JSON.stringify(data))+"' />");
	});<%
	// 서브라인(부서합의), 공람 에서는 결재방 표시 안함 %>
	var needSingArea = "${(empty param.apvLnPno or param.apvLnPno=='0') and param.makrRoleCd!='makVw' ? 'Y' : 'N'}";
	if(needSingArea=='Y') {<%
	
	//    gFormApvLnTypCd : 결재라인구분코드
	//        apvLn:결재(합의표시안함), apvLnMixd:결재(결재합의혼합)
	//        apvLn1LnAgr:결재+합의(1줄), apvLn2LnAgr:결재+합의(2줄)
	//        apvLnDbl:이중결재
	//
	//        apvLnList:리스트
	//        apvLnOneTopList:최종결재+리스트
	//        apvLnMultiTopList:서명+리스트
	// 결재방에 표시 %>
		var apvrRoleCd, apvLnTitlTypCd, apvLnDispTypCd, dblApvTypCd, apvStatCd, absRsonNm;
		var $me=null, $tr1=null, $tr2=null, $tr3=null;
		$("#docArea #apvLnArea div[id='apvLn']").each(function(){
			$me = $(this);
			apvLnTitlTypCd = $me.attr("data-apvLnTitlTypCd");
			apvLnDispTypCd = $me.attr("data-apvLnDispTypCd");<%
			// 서면결재 의 처리 - 제외 처리 %>
			if(gFormApvLnTypCd=='apvLnWrtn' && apvLnTitlTypCd=='prc'){
				return;
			}<%
			// apvStatCd(결재상태코드) : apvd:승인, rejt:반려, pros:찬성, cons:반대 %>
			var notRemove = "[data-apvStatCd='apvd'], [data-apvStatCd='rejt'], [data-apvStatCd='pros'], [data-apvStatCd='cons']";
			if(apvLnDispTypCd=='3row'){
				$tr1 = $me.find("tr:eq(0)");
				$tr2 = $me.find("tr:eq(1)");
				$tr3 = $me.find("tr:eq(2)");
				
				if($tr1.find("td").length > $tr2.find("td").length){<%// 도장방 타이틀(결재,합의) 사용할 경우 %>
					$tr1.find("td").not(":first").not(notRemove).remove();
				} else {
					$tr1.find("td").not(notRemove).remove();
				}
				$tr2.find("td").not(notRemove).remove();
				$tr3.find("td").not(notRemove).remove();
			} else if(apvLnDispTypCd=='2row'){
				$tr1 = $me.find("tr:eq(0)");
				$tr2 = $me.find("tr:eq(1)");
				$tr1.find("td.approval_body").not(notRemove).remove();
				$tr2.find("td.approval_body").not(notRemove).remove();
			} else if(apvLnDispTypCd=='1row'){
				$tr1 = $me.find("tr:eq(0)");
				$tr1.find("td.approval_body").not(notRemove).remove();
			}
			
			var wdth3row = get3RowWdth();
			
			var cellCnt = 0;
			arr.each(function(index, data){
				apvrRoleCd = data.apvrRoleCd;
				dblApvTypCd = data.dblApvTypCd;
				apvStatCd = data.apvStatCd;
				absRsonNm = data.absRsonNm;<%
				// 결재 한것 - 생략 - apvd:승인, rejt:반려, pros:찬성, cons:반대 %>
				if(apvStatCd=='apvd' || apvStatCd=='rejt' || apvStatCd=='pros' || apvStatCd=='cons'){
					cellCnt++;
					return;
				}<%
				// 도장방 표시 안함
				//    entu:결재안함(위임), postApvd:사후보고(후열), psnInfm:개인통보, deptInfm:부서통보 %>
				if(apvrRoleCd=='entu' || apvrRoleCd=='postApvd' || apvrRoleCd=='psnInfm' || apvrRoleCd=='deptInfm'){
					return;
				}<%
				// 결재라인구분코드
				//    - apvLn:결재(합의표시안함) - 합의 제거
				//    - apvLn1LnAgr:결재+합의(1줄), apvLn2LnAgr:결재+합의(2줄)
				//        - 결재 표시 칸이면 - 합의 제거
				%>
				if(gFormApvLnTypCd=='apvLn' || ((gFormApvLnTypCd=='apvLn1LnAgr' || gFormApvLnTypCd=='apvLn2LnAgr') && apvLnTitlTypCd=='apv')){<%
					// psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의 %>
					if(apvrRoleCd=='psnOrdrdAgr' || apvrRoleCd=='psnParalAgr' || apvrRoleCd=='deptOrdrdAgr' || apvrRoleCd=='deptParalAgr'){
						return;
					}
				}<%// 신청부서-신청부서 것만, 처리부서 - 처리부서 것만 %>
				if(gFormApvLnTypCd=='apvLnDbl' || gFormApvLnTypCd=='apvLnDblList'){
					if((apvLnTitlTypCd=='req' && dblApvTypCd!='reqDept') || (apvLnTitlTypCd=='prc' && dblApvTypCd!='prcDept')){
						return;
					}
				}<%// 합의표시 칸이면 - 합의 아닌것 제거%>
				if(apvLnTitlTypCd=='agr'){<%
					// psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의 %>
					if(apvrRoleCd!='psnOrdrdAgr' && apvrRoleCd!='psnParalAgr' && apvrRoleCd!='deptOrdrdAgr' && apvrRoleCd!='deptParalAgr'){
						return;
					}
				}
				
				cellCnt++;
				if(apvLnDispTypCd=='3row'){
					$tr1.append('<td class="approval_body" style="'+wdth3row+'"><div class="approval_bodyin">'+getSignAreaTitle(data)+'</div></td>');
					$tr2.append('<td class="approval_img" style="position:relative; font-weight:bold;'+wdth3row+'">'+absRsonNm+'</td>');
					$tr3.append('<td class="approval_body" style="'+wdth3row+'">'+getDateAreaDispVa(data)+'</td>');
				} else if(apvLnDispTypCd=='2row'){
					$tr1.append('<td class="approval_body" rowspan="2"></td><td class="approval_body"><div class="approval_bodyin">'+getSignAreaTitle(data)+'</div></td>');
					$tr2.append('<td class="approval_body" style="position:relative; font-weight:bold;">'+absRsonNm+'</td>');
				} else if(apvLnDispTypCd=='1row'){
					$tr1.append('<td class="approval_body"><div class="approval_bodyin">'+getSignAreaTitle(data)+'</div></td><td class="approval_body"></td><td class="approval_body" style="position:relative; font-weight:bold;">'+absRsonNm+'</td>');
				}
			});<%
			// 합의의 경우 - 합의 1줄에 분리 표시 - 합의자 없으면 숨김
			// gFormApvLnTypCd : 결재라인구분코드 - apvLn1LnAgr:결재+합의(1줄) %>
			if(gFormApvLnTypCd=='apvLn1LnAgr' && apvLnTitlTypCd=='agr'){
				if(cellCnt==0){
					$me.hide();
				} else {
					$me.show();
				}
			<%
			// 합의의 경우 - 합의 별도의 줄로 표시 - 합의자 없으면 숨김
			// gFormApvLnTypCd : 결재라인구분코드 - apvLn2LnAgr:결재+합의(2줄) %>
			} else if(gFormApvLnTypCd=='apvLn2LnAgr' && apvLnTitlTypCd=='agr'){
				if(cellCnt==0){
					$me.hide();
					$me.parent().find("div#apvLnAgrBlank").hide();
				} else {
					$me.show();
					$me.parent().find("div#apvLnAgrBlank").show();
				}
			}
		});<%
		// 서명+리스트 면 - 설정된 갯수 이상의 도장방 숨기기/보이기 %>
		if(gFormApvLnTypCd == 'apvLnMultiTopList'){
			[$tr1,$tr2,$tr3].each(function(index, $tr){
				var $tds = $tr.find("td");
				var start = (index==0 && $tr1.find("td").eq(0).attr('rowspan')=='3') ? 1 : 0;
				var size = $tds.length;
				var end = $tds.length - gApvLnMaxCnt.apv;
				for(var i=start;i<size;i++){
					if(i<end){
						$tds.eq(i).hide();
					} else {
						$tds.eq(i).show();
					}
				}
			});
		}
		<%
		// 결재자 리스트 - 리스트에 표시 제어 %>
		var $apvLnListArea = $("#docArea #apvLnArea div[id='apvLnListArea']");
		$apvLnListArea.each(function(apvLnIndex){<%
			//        apvLnList:리스트
			//        apvLnOneTopList:최종결재+리스트
			//        apvLnMultiTopList:서명+리스트
			//   gLstDupDispYn - setDoc.jsp : 44라인
			%>
			var $me = $(this);
			var skipFirst = 0;
			var skipLast = gFormApvLnTypCd=='apvLnList' || gFormApvLnTypCd=='apvLnDblList' || gLstDupDispYn =='Y' ? 0 : gFormApvLnTypCd=='apvLnOneTopList' ? 1 : gApvLnMaxCnt.apv;
			var removeList = [];<%
			// 결재선 변경에 따른 삭제할 TR 정리, 결재한것 제외 %>
			$me.find("tr").each(function(index){
				if(index==0) return;<%// 테이블 해더 %><%
				// 결재 한것 - 생략 - apvd:승인, rejt:반려, pros:찬성, cons:반대 %>
				var apvStatCd = $(this).attr('data-apvStatCd');
				if(apvStatCd == 'apvd' || apvStatCd == 'apvd' || apvStatCd == 'apvd' || apvStatCd == 'apvd'){
					skipFirst++;
				} else {
					removeList.push(this);
				}
			});<%
			// 결재 안한것 - 삭제 %>
			removeList.each(function(index, obj){ $(obj).remove(); });<%
			// 설정된 결재선 화면에 표시 %>
			var i, size, listDispArr = [];
			for(i=0;i<arr.length;i++){<%
				// psnInfm:개인통보, deptInfm:부서통보 %>
				if(arr[i].apvrRoleCd=='psnInfm' || arr[i].apvrRoleCd=='deptInfm'){
					continue;<%
				// gLstDupDispYn : setDoc.jsp - 서명란의 사용자 중복으로 목록에 표시
				// entu:결재안함(위임), postApvd:사후보고(후열) %>
				} else if((gFormApvLnTypCd=='apvLnOneTopList' || gFormApvLnTypCd=='apvLnMultiTopList') && gLstDupDispYn != 'Y' && (arr[i].apvrRoleCd=='entu' || arr[i].apvrRoleCd=='postApvd')){
					continue;
				}
				if(gFormApvLnTypCd=='apvLnDblList'){
					if(apvLnIndex==0 && arr[i].dblApvTypCd!='reqDept') continue;
					if(apvLnIndex==1 && arr[i].dblApvTypCd!='prcDept') continue;
				}
				listDispArr.push(arr[i]);
			}
			size = listDispArr.length - skipLast;
			var $apvLnListInsertObj = $me.find("tr:first").parent(), html;
			var apTermResc = getIframeContent("listApvLnFrm").apTermResc;
			for(i=skipFirst;i<size;i++){
				html = [];
				html.push('<tr>');
				html.push('<td class="body_ct">'+listDispArr[i].apvDeptNm+'</td>');<%//부서%>
				html.push('<td class="body_ct">'+listDispArr[i].apvrPositNm+'</td>');<%//직위%>
				html.push('<td class="body_ct">'+listDispArr[i].apvrNm+'</td>');<%//결재자%>
				html.push('<td class="body_ct">'+apTermResc[listDispArr[i].apvrRoleCd]+(listDispArr[i].apvrRoleCd=='abs' ? '['+listDispArr[i].absRsonNm+']' : '')+'</td>');<%//결재자역할%>
				html.push('<td class="body_ct">&nbsp;</td>');<%//결재일시%>
				html.push('<td style="text-align:center;">&nbsp;</td>');<%//의견%>
				html.push('</tr>');
				$apvLnListInsertObj.append(html.join('\r\n'));
			}
			if(gFormApvLnTypCd=='apvLnMultiTopList'){<%
				// 결재자 추가/삭제에 따른 - 도장방으로 올라간 결재자 안보이게 처리 / 도장방에서 내려온 사용자 보이게 처리 %>
				var showCnt = listDispArr.length - (gLstDupDispYn =='Y' ? 0 : gApvLnMaxCnt.apv);
				var $trs = $apvLnListInsertObj.find('tr');
				for(i=1;i<$trs.length;i++){
					if(i<=showCnt){
						$($trs[i]).show();
					} else {
						$($trs[i]).hide();
					}
				}<%
				// 리스트에 보일 사용자 없으면 리스트 숨기기 / 있으면 보이기 %>
				if($apvLnListInsertObj.find("tr:visible").length==1){<%// 타이틀 밖에 없을 때 %>
					$me.prev().hide();
					$me.hide();
				} else {
					$me.prev().show();
					$me.show();
				}
			} else if(gFormApvLnTypCd=='apvLnOneTopList'){
				<%
				// 결재자 리스트 - 에 표시할 항목이 있거나 없을때 - 보이기 제어 %>
				if($apvLnListInsertObj.find("tr").length==1){<%// 타이틀 밖에 없을 때 %>
					$me.prev().hide();
					$me.hide();
				} else {
					$me.prev().show();
					$me.show();
				}<%
				// 최종 결재자 직위 표시 변경 %>
				for(i = listDispArr.length-1;i>=0;i--){
					if(listDispArr[i].apvrRoleCd=='apv' || listDispArr[i].apvrRoleCd=='pred' || listDispArr[i].apvrRoleCd=='revw' || listDispArr[i].apvrRoleCd=='byOne'){
						$("#docArea #lastApvrPositDispVa").text(getSignAreaTitle(listDispArr[i]));
						break;
					}
				}
			}
		});
		
	}
	if(gFormApvLnTypCd=='apvLnDbl' || gFormApvLnTypCd=='apvLnDblList'){<%// 이중결재면 - 결재라인 세팅함 표시 %>
		gDblApvLnDone = true;
	}<%
	// 본문에 통보 삽입한 경우 처리 %>
	var $infmTd = $("#docArea #infmArea #infmTdArea");
	if($infmTd.length>0){
		var buffer=[];
		arr.each(function(index, data){
			if(data.apvrRoleCd=='psnInfm'){
				if(data.apvrUid==null || data.apvrUid==''){
					buffer.push('<nobr>'+data.apvrNm+'</nobr>');
				} else {
					buffer.push('<nobr><a href="javascript:viewUserPop(\''+data.apvrUid+'\')">'+data.apvrNm+'</a></nobr>');
				}
			}
			if(data.apvrRoleCd=='deptInfm'){
				buffer.push('<nobr>'+data.apvDeptNm+'</nobr>');
			}
		});
		if(buffer.lengh==0){
			$infmTd.html("");
		} else {
			$infmTd.html(buffer.join(", "));
		}
	}
	
	setAutoApvLnGrpDone = true;
	if(auto==true){
		window.setTimeout('dialog.close("setApvLnDialog")', 100);
	} else {
		dialog.close("setApvLnDialog");
	}
	if(auto!=null) return true;
}<%
// 저장 버튼 클릭 %>
function storeApvLn(){<%
	// 해당 프레임에서 데이터를 모으는데 검증 과정에서 오류가 있으면 null 리턴 %>
	var arr = getIframeContent("listApvLnFrm").getConfirmList();
	if(arr==null){<%// 실제로 해당 경우 없음, safe 코드 %>
		return;
	}<%
	
	// hidden data area : hidden tag 생성 %>
	var $adjustFrm = $("#apvLnAdjustFrm");
	if($adjustFrm.length == 0) return;
	$adjustFrm.html("");
	
	var myTurnObj = getMyTurnObj();
	$adjustFrm.append("<input name='apvNo' type='hidden' value='"+myTurnObj.apvNo+"' />");
	$adjustFrm.append("<input name='apvLnPno' type='hidden' value='"+myTurnObj.apvLnPno+"' />");
	$adjustFrm.append("<input name='apvLnNo' type='hidden' value='"+myTurnObj.apvLnNo+"' />");
	$adjustFrm.append("<input name='apvLnHstNo' type='hidden' value='"+myTurnObj.apvLnHstNo+"' />");
	$adjustFrm.append("<input name='process' type='hidden' value='apvLn' />");
	
	arr.each(function(index, data){
		$adjustFrm.append("<input name='apvLn' type='hidden' value='"+escapeValue(JSON.stringify(data))+"' />");
	});
	
	$adjustFrm.attr("action", "./transAdmDoc.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}");
	$adjustFrm.attr("method", "post");
	$adjustFrm.attr("target", "dataframe");
	$adjustFrm.submit();
	
}<%
// 결재방 표시 타이틀 %>
function getSignAreaTitle(data){
	if(data.apvrDeptYn != 'Y'){<%// 사용자%>
		var opt = gApvLnOptConfig.signAreaUserTitl;
		return getUserDispVaByOpt(data, opt);
	} else {
		<%// 부서 - 부서약어 있으면 부서약어로 없으면 부서명으로 %>
		var opt = gApvLnOptConfig.signAreaDeptTitl;
		var roleCd = data.apvrRoleCd;
		if(opt=='apvTyp' && (roleCd=="deptOrdrdAgr" || roleCd=="deptParalAgr")){
			return getIframeContent('listApvLnFrm').apTermResc['deptAgr'];
		} else {
			if(data.deptAbs==null || data.deptAbs=='') return data.apvDeptNm;
			return data.deptAbs;
		}
	}
	return null;
}<%
// 옵션에 따른 사용자 표시 값 %>
function getUserDispVaByOpt(data, opt){
	if(opt==null || opt=='') return ''; 
	if(opt=='posit') return data.apvrPositNm;
	if(opt=='title') return data.apvrTitleNm;
	if(opt=='name') return data.apvrNm;
	if(opt=='deptNm') return data.apvDeptNm;
	if(opt=='deptAbs') return data.apvDeptAbbrNm;
	if(opt=='namePosit') return data.apvrNm+(data.apvrPositNm=='' ? '' : "("+data.apvrPositNm+")");
	if(opt=='nameTitle') return data.apvrNm+(data.apvrTitleNm=='' ? '' : "("+data.apvrTitleNm+")");
	if(opt=='nameDeptNm') return data.apvrNm+(data.apvDeptNm=='' ? '' : "("+data.apvDeptNm+")");
	if(opt=='nameDeptAbs') return data.apvrNm+(data.deptAbs=='' ? "("+data.apvDeptNm+")" : "("+data.deptAbs+")");
	if(opt=='apvTyp'){
		var roleCd = data.apvrRoleCd;
		if(roleCd=="psnOrdrdAgr" || roleCd=="psnParalAgr") roleCd = "agr";<% // 개인순차합의, 개인병렬합의  - 합의 %>
		else if(roleCd=="deptOrdrdAgr" || roleCd=="deptParalAgr") roleCd = "deptAgr";<% // 부서순차합의, 부서병렬합의 - 부서합의 %>
		else if(roleCd=="pred") roleCd = "apv";<%// 전결 - 결재 %>
		else if(roleCd=="paralPubVw") roleCd = "pubVw";<%// 동시공람 - 공람 %>
		return getIframeContent('listApvLnFrm').apTermResc[roleCd];
	}
	return '';
}<%
// 날짜 표시칸에 표시 항목 %>
function getDateAreaDispVa(data){
	var opt = gApvLnOptConfig.signAreaDt2;
	if(gApvLnOptConfig.nameAtDateSignAreaEnable=='Y') opt = 'name';<%// [옵션] 날짜 표시란에 이름 표시 %>
	var va = getUserDispVaByOpt(data, opt);
	if(va!=null && va!='') return '&nbsp<br/>'+va;
	return '';
}<%
// 결재구분 라디오 초기 세팅 : listApvLnFrm.jsp 에서 호출 %>
function setApvLnSetTypCdByRole(apvrRoleCd){
	if(gMakrRoleCd!='makVw'){
		if(apvrRoleCd=='byOne'||apvrRoleCd=='byOneAgr'){
			$("#apvLnSetupTypCdArea #byOne").checkInput(true);
			clickApvLnSetupTypCd("noml","byOne");
		}
	}
}<%
// 경로그룹 클릭 %>
var gApvLnGrpTypCd_secu = null;
function clickApvLnGrp(apvLnGrpId, apvLnGrpTypCd){
	gApvLnGrpTypCd_secu = apvLnGrpTypCd; <u:secu auth="S">gApvLnGrpTypCd_secu = null;</u:secu>
	reloadFrame("apvLnGrpDetlFrm","./listApvLnGrpDetlFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvLnGrpId="+apvLnGrpId+"&formApvLnTypCd=${param.formApvLnTypCd}"+(apvLnGrpTypCd!='' ? '&apvLnGrpTypCd='+apvLnGrpTypCd : ''));
	toggleSaveApvLnGrpBtn();
}<%
// 경로그룹 저장 버튼 - 보이기 조절 %>
function toggleSaveApvLnGrpBtn(){
	if(gApvLnTabNo==2 && gApvLnGrpTypCd_secu!='pub'){
		$("#saveApvLnGrpBtn").show();
	} else {
		$("#saveApvLnGrpBtn").hide();
	}
	
	if(!(gApvLnTabNo==0 || gApvLnTabNo==3)){
		$("#addDeptBtn").hide();
	} else {
		var apvLnSetupTypCd = $("#apvLnSetupTypCdArea").find("input[name='apvLnSetupTypCd']:checked").val();
		var roleCd = $("#apvrRoleCdArea").find("input[name='apvrRoleCd']:checked").val();
		if(apvLnSetupTypCd=='prcDept' && gApvLnOptConfig['prcDeptEnab']=='Y'){
			$("#addDeptBtn").show();
		} else if(apvLnSetupTypCd=='infm' && gApvLnOptConfig['deptInfmEnab']=='Y'){
			$("#addDeptBtn").show();
		} else if(apvLnSetupTypCd=='nomlApv' || apvLnSetupTypCd=='byOne'){<%
			//    (순차합의, 병렬합의) + [옵션]부서합의 사용 : [부서추가] 버튼 보임 %>
			if((roleCd=='ordrdAgr' || roleCd=='paralAgr') && gApvLnOptConfig['deptAgrEnab']=='Y'){
				$("#addDeptBtn").show();<%
			//    통보 + [옵션]부서통보 사용 : [부서추가] 버튼 보임 %>
			} else if(roleCd=='infm' && gApvLnOptConfig['deptInfmEnab']=='Y') {
				$("#addDeptBtn").show();
			} else {
				$("#addDeptBtn").hide();
			}
		} else {
			$("#addDeptBtn").hide();
		}
	}
}<%
// 경로그룹 저장 버튼 %>
function saveApvLnGrpDetl(){<%
	// 해당 프레임에서 데이터를 모으는데 검증 과정에서 오류가 있으면 null 리턴 %>
	var arr = getIframeContent("listApvLnFrm").getConfirmList(true);
	if(arr==null) return;
	getIframeContent("apvLnGrpDetlFrm").saveApvLnGrpDetl(arr);
}<%
// 경로그룹 - 경로그룹 파라미터(경로그룹ID:apvLnGrpId)에 의해 자동 세팅 %>
function setAutoApvLnGrp(){
	if(!readyListApvLnFrm){
		window.setTimeout('setAutoApvLnGrp()', 50);
	}
	else setAutoApvLnGrpProcess();
}<%
// 하단의 결재자를 모으는 프레임이 로드 되었는지 여부 %>
var readyListApvLnFrm = false;<%
// 경로그룹 - 자동 설정 %>
function setAutoApvLnGrpProcess(){
	var frameId = "apvLnGrpDetlFrm";
	var arr = getIframeContent(frameId).getSelectedUsers();
	if(arr==null || arr.length==0){
		parent.setAutoApvLnGrpDone = true;
		dialog.close('setApvLnDialog');
		return;
	}<%
	// 경로그룹에 추가 %>
	getIframeContent("listApvLnFrm").addApvLnGrp(arr);<%
	// 해당 프레임의 선택 해제%>
	getIframeContent(frameId).deselectUsers();<%
	// 결재 경로를 팝업에서 부모창에 설정 %>
	collectApvLn(true);
}<%
// 자동 결재선 설정에서 - 결재선 지정이 안될때(결재선 지정 오류) 결재선 지정 팝업을 보이게 처리 %>
function showApvLnPop(){<%
	// 경로그룹[목록] - 빈 페이지로 전환 - 경로그룹[텝] 클릭시 리로드 하도록 %>
	getIframeContent("apvLnGrpFrm").location.replace("${_cxPth}/cm/util/reloadable.do");<%
	// 조직도[탭] 클릭 함수 %>
	changeTab('apvLnTab',0);
	apvLnUserTabClick(0);<%
	// 결재선지정[팝업]을 보이게 함 %>
	dialog.resize('setApvLnDialog', null, null, false);
}<%
// onload 시 - 이중결재(처리부서), 이중결재외(일반결재) 클릭한 함수 호출 - ui 정리를 위한 것 %>
$(document).ready(function() {
	gOptConfig = gApvLnOptConfig;<%//if(gMakrRoleCd=='makVw') checkFirstVisible($("#apvrRoleCdArea"));%>
	if(gFormApvLnTypCd=='apvLnDbl' || gFormApvLnTypCd=='apvLnDblList'){
		clickApvLnSetupTypCd('dbl','${param.prcDeptYn=="Y" ? "prcDept" : "reqDept"}');
	}
	else clickApvLnSetupTypCd('noml','nomlApv');
});
//-->
</script>

<div style="width:900px">

<u:tabGroup id="apvLnTab" noBottomBlank="true">
	<u:tab onclick="apvLnUserTabClick(0)" areaId="apvLnUserTreeArea" alt="조직도" titleId="or.jsp.setOrg.orgTreeTitle" id="apvLnTab" on="${tabNo == '0'}" /><c:if
		test="${not empty apvrFromOtherComp and (empty param.upward and empty param.downward and empty param.oneDeptId)}">
	<u:tab onclick="apvLnUserTabClick(3)" areaId="apvLnForeignUserTreeArea" alt="대외 조직도" titleId="ap.jsp.ForeignComp" id="apvLnTab" on="${tabNo == '3'}" /></c:if>
	<u:tab onclick="apvLnUserTabClick(1)" areaId="apvLnUserSchArea" alt="임직원 검색" titleId="pt.top.orgSrch" id="apvLnTab" on="${tabNo == '1'}" />
	<u:tab onclick="apvLnUserTabClick(2)" areaId="apvLnGrpArea" alt="경로그룹" titleId="ap.jsp.apvLnGrp" id="apvLnTab" on="${tabNo == '2'}" />
</u:tabGroup>

<u:tabArea
	outerStyle="height:${outHeight}; overflow-x:hidden; overflow-y:hidden;"
	innerStyle="height:${inHeight}; margin:10px;">

<u:cmt cmt="[조직도]탭 영역" />
<div id="apvLnUserTreeArea" <c:if test="${tabNo != '0'}">style="display:none"</c:if>>

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
<iframe id="apvLnOrgTreeFrm" name="apvLnOrgTreeFrm" src="${orgTreeUrl}" style="width:100%; height:${frmHeight};" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:titleArea>
</div>

<u:cmt cmt="[조직도] - (우)사용자 목록 영역" />
<div style="float:right; width:70%;">
<u:titleArea
	outerStyle="height:${divHeight}; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV">
<iframe id="apvLnUserListFrm" name="apvLnUserListFrm" src="${_cxPth}/cm/util/reloadable.do" style="width:100%; height:${frmHeight};" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:titleArea>
</div>

</div>

<c:if test="${not empty apvrFromOtherComp and (empty param.upward and empty param.downward and empty param.oneDeptId)}">
<u:cmt cmt="[대외 조직도]탭 영역" />
<div id="apvLnForeignUserTreeArea" <c:if test="${tabNo != '3'}">style="display:none"</c:if>>

<u:cmt cmt="[대외 조직도] - (좌)트리 영역" />
<div style="float:left; width:28.8%;">
<u:titleArea
	outerStyle="height:${divHeight}; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"><u:set
		test="${tabNo == '3'}" var="orgTreeUrl" value="./treeOrgFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam
		}&mode=foreign" elseValue="${_cxPth}/cm/util/reloadable.do" />
<iframe id="apvLnForeignOrgTreeFrm" name="apvLnForeignOrgTreeFrm" src="${orgTreeUrl}" style="width:100%; height:${frmHeight};" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:titleArea>
</div>

<u:cmt cmt="[대외 조직도] - (우)사용자 목록 영역" />
<div style="float:right; width:70%;">
<u:titleArea
	outerStyle="height:${divHeight}; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV">
<iframe id="apvLnForeignUserListFrm" name="apvLnForeignUserListFrm" src="${_cxPth}/cm/util/reloadable.do" style="width:100%; height:${frmHeight};" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:titleArea>
</div>

</div></c:if>

<u:cmt cmt="[임직원 검색]탭 영역" />
<div id="apvLnUserSchArea" <c:if test="${tabNo != '1'}">style="display:none"</c:if>>
<iframe id="apvLnUserSearchFrm" name="apvLnUserSearchFrm" src="${_cxPth}/or/user/searchUserFrm.do?opt=multi&userStatCd=02${
	empty apvrFromOtherComp ? '&compId='.concat(sessionScope.userVo.compId) : ''
	}${not empty param.upward ? '&upward='.concat(param.upward) : ''
	}${not empty param.downward ? '&downward='.concat(param.downward) : ''
	}${not empty param.oneDeptId ? '&oneDeptId='.concat(param.oneDeptId) : ''
	}" style="width:100%; height:${inHeight};" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</div>

<u:cmt cmt="[경로 룹]탭 영역" />
<div id="apvLnGrpArea" <c:if test="${tabNo != '2'}">style="display:none"</c:if>>
<u:cmt cmt="[경로그룹 목록] - (좌) 영역" />
<div style="float:left; width:28.8%;">
<u:titleArea
	outerStyle="height:${divHeight}; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"><u:set
		test="${tabNo == '2'}" var="apvLnGrpUrl"
		value="./listApvLnGrpFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam
			}&apvLnGrpTypCd=${empty param.apvLnGrpTypCd ? 'prv' : param.apvLnGrpTypCd
			}&apvLnGrpId=${param.apvLnGrpId
			}${empty param.fixdApvrYn ? '' : '&fixdApvrYn='.concat(param.fixdApvrYn)}&noInit=Y"
		elseValue="${_cxPth}/cm/util/reloadable.do" />
<iframe id="apvLnGrpFrm" name="apvLnGrpFrm" src="${apvLnGrpUrl}" style="width:100%; height:${frmHeight};" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:titleArea>
</div>

<u:cmt cmt="[경로그룹 별 사용자] - (우)" />
<div style="float:right; width:70%;">
<u:titleArea
	outerStyle="height:${divHeight}; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"><u:set test="${tabNo == '2'}"
		var="listApvLnGrpDetlFrmUrl"
		value="${_cxPth}/ap/box/listApvLnGrpDetlFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam
			}&apvLnGrpTypCd=${empty param.apvLnGrpTypCd ? 'prv' : param.apvLnGrpTypCd
			}&apvLnGrpId=${param.apvLnGrpId
			}&initAct=auto&formApvLnTypCd=${param.formApvLnTypCd
			}&autoApvLnCd=${param.autoApvLnCd
			}${empty param.fixdApvrYn ? '' : '&fixdApvrYn='.concat(param.fixdApvrYn)}"
		elseValue="${_cxPth}/cm/util/reloadable.do" />
<iframe id="apvLnGrpDetlFrm" name="apvLnGrpDetlFrm" src="${listApvLnGrpDetlFrmUrl}" style="width:100%; height:${frmHeight};" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:titleArea>
</div>
</div>

</u:tabArea>


<u:titleArea
	outerStyle="padding:3px 0px 1px 5px;"
	innerStyle="NO_INNER_IDV"><u:cmt
	
	cmt="이중결재가 아니고, 두번째 결재자 부터, 이중결재의 처리부서인 경우, 유통의 경우 - 결재구문(일반결재/1인결재) 보이지 않게함" />
<u:checkArea id="apvLnSetupTypCdArea" style="${ 
	(param.formApvLnTypCd != 'apvLnDbl' and param.formApvLnTypCd != 'apvLnDblList' and param.apvLnNo != '1')
	or param.makrRoleCd=='makVw' ? 'display:none;' : ''}">
<u:cmt cmt="결재구분  : 신청부서 / 처리부서 / 통보" />
<td class="body_lt"><strong><u:msg titleId="ap.jsp.apvTyp" alt="결재구분" /></strong></td>
<td class="width10"></td><c:if
	test="${isDblApvLn == 'Y'}"><c:if
		test="${param.prcDeptYn!='Y'}">
<u:radio value="reqDept" name="apvLnSetupTypCd" id="reqDept" termId="ap.term.reqDept" onclick="clickApvLnSetupTypCd('dbl', this.value)" checked="${param.prcDeptYn != 'Y'}" /></c:if>
<u:radio value="prcDept" name="apvLnSetupTypCd" id="prcDept" termId="ap.term.prcDept" onclick="clickApvLnSetupTypCd('dbl', this.value)" checked="${param.prcDeptYn == 'Y'}" />
<u:radio value="infm" name="apvLnSetupTypCd" id="infm" termId="ap.term.infm" onclick="clickApvLnSetupTypCd('dbl', this.value)" /></c:if><c:if
	test="${isDblApvLn != 'Y'}">
<u:radio value="nomlApv" name="apvLnSetupTypCd" id="nomlApv" titleId="ap.jsp.nomlApv" onclick="clickApvLnSetupTypCd('noml', this.value)" checked="true" />
<u:radio value="byOne" name="apvLnSetupTypCd" id="byOne" termId="ap.term.byOne" onclick="clickApvLnSetupTypCd('noml', this.value)" /></c:if>
</u:checkArea>
<u:cmt cmt="결재 기능  : 검토, 공석, 결재 ..." />
<div id="apvrRoleCdArea" style="overflow:auto; min-height:25px;"><c:forEach
	items="${apvrRoleCds}" var="apvrRoleCd" varStatus="status"><c:if
		test="${not empty apvrRoleCd}">
<u:checkArea style="float:left; height:25px;" id="${apvrRoleCd}Area"><u:radio noBr="${true}"
	value="${apvrRoleCd}" name="apvrRoleCd" id="${apvrRoleCd}" termId="ap.term.${apvrRoleCd}"
	onclick="clickApvrRoleCd(this.value)" checkValue="${param.makrRoleCd=='makVw' ? 'fstVw' : 'revw'}"
/></u:checkArea><c:if
	test="${apvrRoleCd == 'abs'}">
<u:checkArea style="float:left; height:25px; display:none;" id="absRsonCdArea">
<td>(</td>
<td><select id="absRsonCd" name="absRsonCd" onchange="clickApvrRoleCd('abs');" ><c:forEach
	items="${absRsonCdList}" var="absRsonCd">
	<option value="${absRsonCd.cd}"><u:out value="${absRsonCd.rescNm}" /></option></c:forEach>
</select></td><td style="padding-left:2px">)</td><td class="width10"></td></u:checkArea></c:if></c:if>
</c:forEach>
</div>

</u:titleArea>

<u:title alt="결재 경로" titleId="ap.jsp.apvLn" >
	<u:titleButton titleId="ap.btn.deptAdd" alt="부서추가" id="addDeptBtn" img="ico_add.png" href="javascript:addSelectedDept()" style="display:none;" />
	<u:titleButton titleId="cm.btn.add" alt="추가" id="addUserBtn" img="ico_add.png" href="javascript:addSelectedUser()" />
	<u:titleButton titleId="cm.btn.del" alt="삭제" id="delBtn" img="ico_minus.png" href="javascript:delSelected()" />
	<u:titleIcon type="up" href="javascript:moveLine('up')" />
	<u:titleIcon type="down" href="javascript:moveLine('down')" />
</u:title>

<u:titleArea
	outerStyle="height:${divHeight}; overflow-x:hidden; overflow-y:auto; padding:0px"
	innerStyle="NO_INNER_IDV">
<iframe id="listApvLnFrm" name="listApvLnFrm" src="./listApvLnFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam
		}&formApvLnTypCd=${param.makrRoleCd=='makVw' and (param.formApvLnTypCd=='apvLnDbl' or param.formApvLnTypCd=='apvLnDblList') ? 'apvLn' : param.formApvLnTypCd}<c:if
		test="${not empty param.apvNo}">&apvNo=${param.apvNo}</c:if><c:if
		test="${not empty param.apvLnPno}">&apvLnPno=${param.apvLnPno}</c:if><c:if
		test="${not empty param.apvLnNo}">&apvLnNo=${param.apvLnNo}</c:if><c:if
		test="${not empty param.apvLnHstNo}">&apvLnHstNo=${param.apvLnHstNo}</c:if><c:if
		test="${not empty param.makrRoleCd}">&makrRoleCd=${param.makrRoleCd}</c:if><c:if
		test="${not empty param.modified}">&modified=${param.modified}</c:if><c:if
		test="${tabNo == '2'}">&noInit=Y</c:if>" style="width:100%; height:${frmHeight};" frameborder="0" marginheight="0" marginwidth="0"></iframe>
<c:if test="${param.bxId eq 'admOngoBx'}"><form id="apvLnAdjustFrm" style="display:none"></form></c:if>
</u:titleArea>

<u:buttonArea>
	<u:button titleId="ap.btn.saveApvLnGrp" href="javascript:saveApvLnGrpDetl();" alt="경로그룹 저장"
		id="saveApvLnGrpBtn" style="${tabNo != '2' ? 'display:none;' : ''}" /><c:if
		test="${param.bxId eq 'admOngoBx'}">
	<u:button titleId="cm.btn.save" href="javascript:storeApvLn();" alt="저장" /></c:if><c:if
		test="${param.bxId ne 'admOngoBx'}">
	<u:button titleId="cm.btn.confirm" href="javascript:collectApvLn();" alt="확인" /></c:if>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>