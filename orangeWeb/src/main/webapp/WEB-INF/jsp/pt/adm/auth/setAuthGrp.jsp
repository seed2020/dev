<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%
//////////////////////////////////
//
//  왼쪽 - 권한그룹/상용자 그룹 관련 함수

%><%
// gAuthGrpTypCd - userGrpDetl:권한그룹구분코드 - G:사용자그룹, U:사용자권한그룹, A:관리자권한그룹, M:모바일권한그룹
// 사용자,관리자 에 따라 해당 프레임이 열리며, 사용자그룹 프레임은 같이 쓰임 %>
var gAuthGrpTypCd = '${authGrpTypCd}';
<%// 오른쪽 상세정보의 변경 여부 %>
var gDetlChanged = false;
<%// 왼쪽 텝으로 선택된 프레임 리턴 %>
function getLeftFrm(){
	return getIframeContent(gAuthGrpTypCd=='G' ? 'userGrpList' : 'authGrpList');
}
<%// [텝] 권한그룹/사용자그룹 - 클릭 %>
function onAuthGrpTabClick(typCd){
	if(dialog.isOpen('setAuthGrpDialog')) dialog.close('setAuthGrpDialog');
	if(dialog.isOpen('setAuthGrpDetlCdDialog')) dialog.close('setAuthGrpDetlCdDialog');
	if(gAuthGrpTypCd != typCd && saveChangedDetl()){<%// 상세설정의 변경사항이 있으면 저장할것인지 체크 %>
		gIgnoreTabChange = true;
		return;
	}
	<%// 사용자그룹이 선택되면 제외대상 숨김 %>
	displayTab("exclTab", "setExclDetl", typCd!='G');
	
	gAuthGrpTypCd = typCd;
	selected = getLeftFrm().getSeletedItem();
	openDetlFrm(selected);
}
<%// 변경사항 저장함 %>
function saveChangedDetl(){
	var selected = getLeftFrm().getSeletedItem();
	if(gDetlChanged && selected.grpId!=null){<%// 상세설정의 변경사항이 있으면 %>
		if(confirmMsg("cm.cfrm.saveChange")){<%//cm.cfrm.saveChange=변경사항이 있습니다. 저장하시겠습니까 ?%>
			saveAuthGrpDetl();
			return true;
		}
	}
	gDetlChanged = false;
	return false;
}
<%// 권한그룹/사용자그룹 클릭시 - 왼쪽 프레임에서 호출 %>
function onAuthGrpClick(selected){
	if(dialog.isOpen('setAuthGrpDialog')) dialog.close('setAuthGrpDialog');
	if(dialog.isOpen('setAuthGrpDetlCdDialog')) dialog.close('setAuthGrpDetlCdDialog');
	openDetlFrm(selected);
}
<%// [아이콘] 위로이동, 아래로이동 - (왼쪽)%>
function move(direction){
	getLeftFrm().move(direction);
}
<%// [버튼] 등록 - (왼쪽) 권한그룹 %>
function regAuthGrp(){
	var dialogTitle = gAuthGrpTypCd=='G' ? '<u:msg titleId="pt.jsp.setAuthGrp.regUserGrp" alt="사용자 그룹 등록" />' : '<u:msg titleId="pt.jsp.setAuthGrp.${regAuthGrp}" alt="사용자/관리자/모바일 권한 그룹 등록"/>';
	var catCd = getLeftFrm().getSelectedCatCd();
	var url = './setAuthGrpPop.do?menuId=${menuId}&compId=${compId}&authGrpTypCd='+gAuthGrpTypCd+"&authGrpCatCd="+(catCd==null ? '' : catCd);
	dialog.open('setAuthGrpDialog', dialogTitle, url);
}
<%// [버튼] 수정 - (왼쪽) 권한그룹 %>
function modAuthGrp(){
	var dialogTitle = gAuthGrpTypCd=='G' ? '<u:msg titleId="pt.jsp.setAuthGrp.modUserGrp" alt="사용자 그룹 수정" />' : '<u:msg titleId="pt.jsp.setAuthGrp.${modAuthGrp}" alt="사용자/관리자/모바일 그룹 수정"/>';
	var selected = getLeftFrm().getSeletedItem();
	if(selected.grpId==null){
		alert('<u:msg titleId="cm.msg.noSelect" alt="선택한 항목이 없습니다."/>');
	} else {
		dialog.open('setAuthGrpDialog', dialogTitle, './setAuthGrpPop.do?menuId=${menuId}&compId=${compId}&authGrpTypCd='+selected.typCd+"&authGrpId="+selected.grpId);
	}
}
<%// [버튼] 삭제 - (왼쪽) 권한그룹 %>
function delAuthGrp(){
	getLeftFrm().delAuthGrp();
}
<%// [버튼] 카테고리 - (왼쪽) 코드관리 페이지로 이동 %>
function goCatCd(){
	location.href="./setCatCd.do?menuId=${menuId}&clsCd=AUTH_GRP_CAT_CD&backTo="+ encodeURIComponent("/pt/adm/auth/set${authMdCd}AuthGrp.do?menuId=${menuId}");
}
<%// [버튼] 권한설정 - (왼쪽) 권한관리 페이지로 이동 %>
function goAuthSetup(){
	var selected = getLeftFrm().getSeletedItem();
	var url = '<u:authUrl url="/pt/adm/auth/set${authMdCd}Auth.do" />';
	location.href = url + (url.indexOf('?') > 0 ? '&' : '?') + (selected.grpId==null ? "" : "&authGrpId="+selected.grpId);
}
<%// [버튼] 선택삭제 (사용자) %>
function delUsers(){
	getRightFrm().delSelectedUser();
}
<%
//////////////////////////////////
//
//  팝업 - 권한그룹등록 (왼쪽)
%><%// [버튼] 권한그룹 - 저장 %>
function saveAuthGrp(){
	if(validator.validate('setAuthGrpPop')){
		var $frm = $('#setAuthGrpPop');
		$frm.attr('action','./transAuthGrp.do');
		$frm.attr('target','dataframe');
		$frm.submit();
	}
}
<%// [팝업] 저장후 리로드 %>
function reloadAuthGrp(typCd, catCd, grpId){
	dialog.close("setAuthGrpDialog");
	changeTab('listTab', typCd=='G' ? 1 : 0);
	getIframeContent(typCd=='G' ? 'userGrpList' : 'authGrpList').reloadSelf(typCd, catCd, grpId);
}
<%
//////////////////////////////////
//
//  오른쪽 - 권한그룹/상용자 그룹 - 상세 설정 관련 함수

%><%// 제외대상 여부 %>
var gExcliYn = 'N';
<%// 오른쪽 텝으로 선택된 프레임 리턴
  // selected : 왼쪽 프레임 선택 정보 - selected.typCd, selected.catCd, selected.grpId %>
function getRightFrm(){
	return getIframeContent(gExcliYn=='N' ? 'setInclDetl' : 'setExclDetl');
}
<%// [텝] 포함대상/제외대상 - 클릭 (오른쪽) %>
function onExcliYnTabClick(yn){
	if(yn!=null) gExcliYn = yn;
	var selected = getLeftFrm().getSeletedItem();
	openDetlFrm(selected);
}
var opened = { typCd:null, grpId:null };
<%// 오른쪽 프레임에 해당하는 페이지 열기 %>
function openDetlFrm(selected){
	if(selected==null || selected.grpId==null){
		getIframeContent('setInclDetl').reload('./setAuthGrpDetlFrm.do?menuId=${menuId}&authGrpTypCd='+gAuthGrpTypCd+'&excliYn=N&userCombCd='+gUserCombCd);
		getIframeContent('setExclDetl').reload('./setAuthGrpDetlFrm.do?menuId=${menuId}&authGrpTypCd='+gAuthGrpTypCd+'&excliYn=Y&userCombCd='+gUserCombCd);
		opened.typCd = null;
		opened.grpId = null;
	} else {
		if(opened.typCd != selected.typCd || opened.grpId != selected.grpId){
			getIframeContent('setInclDetl').reload('./setAuthGrpDetlFrm.do?menuId=${menuId}&compId=${compId}&authGrpTypCd='+gAuthGrpTypCd+'&authGrpId='+selected.grpId+'&excliYn=N&userCombCd='+gUserCombCd);
			getIframeContent('setExclDetl').reload('./setAuthGrpDetlFrm.do?menuId=${menuId}&compId=${compId}&authGrpTypCd='+gAuthGrpTypCd+'&authGrpId='+selected.grpId+'&excliYn=Y&userCombCd='+gUserCombCd);
			opened.typCd = selected.typCd;
			opened.grpId = selected.grpId;
		}
	}
}
<%// [버튼] 직위,직급,직책,역할,보안등급 클릭 - 오른쪽 하단 %>
function setAuthGrpDetlPop(grpKndCd){
	var selected = getLeftFrm().getSeletedItem();
	if(selected.grpId==null){
		var grpNm = gAuthGrpTypCd == 'G' ? '<u:msg titleId="pt.jsp.setAuthGrp.userGrp" alt="사용자그룹"/>' : '<u:msg titleId="pt.jsp.setAuthGrp.authGrp" alt="권한그룹"/>';
		alertMsg('pt.jsp.setAuthGrp.select.grp.first', [grpNm]);<%// pt.jsp.setAuthGrp.select.grp.first={0}을 먼저 선택해 주십시요.%>
	} else {
		var grpIds = getIframeContent(gExcliYn=='N' ? 'setInclDetl' : 'setExclDetl').getSelectedGrpIds(grpKndCd);
		if(grpKndCd=='user'){<%// 사용자 %>
			selectUsers(grpIds);
		} else {
			if(grpIds!=null){
				if(grpKndCd=='dept'){<%// 부서 %>
					selectOrgWithSub(grpIds);
				} else if(grpKndCd=='userGrp'){<%// 사용자그룹 %>
					var title = '<u:msg titleId="pt.jsp.setAuthGrp.userGrp" alt="사용자그룹" />';
					openDetlPop(grpKndCd, title, grpIds);
				} else if(grpKndCd=='aduStat'){<%// 겸직 상태 %>
					var title = '<u:msg titleId="pt.sysopt.aduStat" alt="겸직 상태" />';
					openDetlPop(grpKndCd, title, grpIds);
				} else {<%//  직위, 직급, 직책, 역할, 보안등급 %>
					var title = callTerm("or.term."+grpKndCd);
					openDetlPop(grpKndCd, title, grpIds);
				}
			} else {
				alert('<u:msg titleId="pt.jsp.setAuthGrp.select.seq.first" alt="권한조합을 선택해 주십시요."/>');
			}
		}
	}
}
<%// 다중 부서 선택 - 하위부서 여부 포함 %>
function selectOrgWithSub(data){
	searchOrgPop({data:data, multi:true, withSub:true}, function(arr){
		var ids=[], rescIds=[], rescNms=[];//, p, extra;
		var withSubTxt = '<u:msg titleId="or.check.withSub.short" alt="하위포함"/>';
		if(arr!=null){
			arr.each(function(index, orgVo){
				ids.push(orgVo.orgId+':'+orgVo.withSub);
				rescIds.push(orgVo.rescId);
				rescNms.push(orgVo.withSub=='Y' ? orgVo.rescNm+'('+withSubTxt+')' : orgVo.rescNm);
			});
		}
		getRightFrm().setAuthGrpDetlSelected('dept', ids, rescIds, rescNms);
	});
}
<%// 사용자 선택 %>
function selectUsers(data){
	searchUserPop({data:data, multi:true}, function(arr){
		var users = [];
		if(arr!=null){
			arr.each(function(index, userVo){
				users.push(userVo.userUid);
			});
		}
		getRightFrm().setUsers(users);
	});
}
<%// 직위,직급,직책,역할,보안등급 - 팝업열기%>
function openDetlPop(grpKndCd, title, grpIds){
	<%// 다이얼로그 제목에 ~ "선택" 추가 %>
	<c:if test="${_lang == 'ko'}">title += ' <u:msg titleId="cm.checkbox.actname" />';</c:if>
	dialog.open('setAuthGrpDetlCdDialog', title, './setAuthGrpDetlPop.do?menuId=${menuId}&compId=${compId}&grpKndCd='+grpKndCd);
	<%// 이미 선택된것 체크 하기%>
	if(grpIds.length>0){
		$("#setAuthGrpDetlCdPop input[type='checkbox']").each(function(){
			if(grpIds.contains($(this).val())){
				$(this).trigger('click');
			}
		});
	}
}
<%// [버튼] 저장 클릭 - 오른쪽 하단 %>
function saveAuthGrpDetl(){
	var selected = getLeftFrm().getSeletedItem();
	if(selected.grpId==null){
		var grpNm = gAuthGrpTypCd == 'G' ? '<u:msg titleId="pt.jsp.setAuthGrp.userGrp" alt="사용자그룹"/>' : '<u:msg titleId="pt.jsp.setAuthGrp.authGrp" alt="권한그룹"/>';
		alertMsg('pt.jsp.setAuthGrp.select.grp.first', [grpNm]);<%// pt.jsp.setAuthGrp.select.grp.first={0}을 먼저 선택해 주십시요.%>
	} else {
		var param = new ParamMap({compId:'${compId}', authGrpTypCd:gAuthGrpTypCd, authGrpId:selected.grpId});
		getIframeContent('setInclDetl').collectDataToSave(param, 'N');
		getIframeContent('setExclDetl').collectDataToSave(param, 'Y');
		getRightFrm().saveAuthGrpDetl(param);
	}
	gDetlChanged = false;
}
<%// 사용자/권한조합 - 오른쪽 프레임 텝, U:사용자, C:권한조합 %>
var gUserCombCd = 'C';
<%// [텝] 사용자/권한조합 - 텝클릭 - 오른쪽 프레임에서 호출, U:사용자, C:권한조합 %>
function setUserCombCd(userCombCd){
	var id;
	$("#rightBtnArea li").each(function(){
		id = $(this).attr("id");
		if(userCombCd=='U'){
			if(id=='user'||id=='selDel'){
				$(this).show();
			} else if(id!='save') {
				$(this).hide();
			}
		} else {
			if(id=='user'||id=='selDel'){
				$(this).hide();
			} else if(id!='save') {
				$(this).show();
			}
		}
	});
	if(gAuthGrpTypCd!='G'){<%// 권한조합/사용자그룹 텝이 '사용자그룹'이면 - 사용자/권한조합 텝 선택 저장안함%>
		gUserCombCd = userCombCd;
	}
}
<%
//////////////////////////////////
//
// 팝업 - 권한등록 상세 설정 (직위,직급,직책...)
%><%// [팝업 버튼] 확인 - 권한그룹 상세 설정 저장 %>
function setAuthGrpDetlSelected(grpKndCd){
	<%// 직위, 직급, 직책, 역할, 보안등급, 사용자그룹 의 경우
		  // (grpKndCd=='posit' || grpKndCd=='grade' || grpKndCd=='grade' || grpKndCd=='role' || grpKndCd=='secul' || grpKndCd=='userGrp') %>
	var ids=[], rescIds=[], rescNms=[], p, extra;
	$("#setAuthGrpDetlCdPop input[type='checkbox']:checked").each(function(){
		if($(this).val()!=null && $(this).val()!=''){
			extra = $(this).attr('data-extra');
			p = extra.indexOf(',');
			if(p>0){
				ids.push($(this).val());
				rescIds.push(extra.substring(0,p));
				rescNms.push(extra.substring(p+1));
			}
		}
	});
	
	dialog.close('setAuthGrpDetlCdDialog');
	<%// 해당 프레임에 데이터 세팅 %>
	getRightFrm().setAuthGrpDetlSelected(grpKndCd, ids, rescIds, rescNms);
	gDetlChanged = true;<%// 상세설정 변경여부 %>
}
<%// 오른쪽 리로드 - 프레임이 2개 이어서 설정 상세(오른쪽 내용)를 저장하고 오른쪽 프레임에서 호출됨 %>
function reloadRight(){
	opened.typCd = null;
	opened.grpId = null;
	var selected = getLeftFrm().getSeletedItem();
	openDetlFrm(selected);
}

//-->
</script>

<u:title alt="사용자 / 관리자 / 모바일 권한 그룹 관리" menuNameFirst="true" /><u:msg
	var="authGrpTitle" titleId="pt.jsp.setAuthGrp.${authGrpTypCd=='A' ? 'admAuthGrp' : authGrpTypCd=='M' ? 'mobileAuthGrp' : 'usrAuthGrp'}" />

<%// 왼쪽 : 탭 - 권한그룹,사용자그룹 %>
<div class="left" style="float:left; width:28.5%;">
	<u:tabGroup id="listTab" noBottomBlank="true">
		<u:tab id="listTab" alt="사용자/관리자/모바일 권한그룹" onclick="onAuthGrpTabClick('${authGrpTypCd}');" title="${authGrpTitle}" areaId="authGrpList" on="${authGrpTypCd != 'G'}" />
		<u:tab id="listTab" alt="사용자그룹" onclick="onAuthGrpTabClick('G');" titleId="pt.jsp.setAuthGrp.userGrp" areaId="userGrpList" on="${authGrpTypCd == 'G'}" />
		<u:tabIcon type="up" href="javascript:move('up')" auth="A" />
		<u:tabIcon type="down" href="javascript:move('down')" auth="A" />
	</u:tabGroup>
	
	<u:tabArea outerStyle="height:580px; overflow-x:hidden; overflow-y:auto;" innerStyle="NO_INNER_IDV">
	<iframe id="authGrpList" name="authGrpList" src="./listAuthGrpFrm.do?menuId=${menuId}&compId=${compId}&authGrpTypCd=${authGrpTypCd}" style="width:100%; height:570px;<c:if test="${authGrpTypCd == 'G'}"> display:none;</c:if>" frameborder="0" marginheight="0" marginwidth="0"></iframe>
	<iframe id="userGrpList" name="userGrpList" src="./listAuthGrpFrm.do?menuId=${menuId}&compId=${compId}&authGrpTypCd=G" style="width:100%; height:570px;<c:if test="${authGrpTypCd != 'G'}"> display:none;</c:if>" frameborder="0" marginheight="0" marginwidth="0"></iframe>
	</u:tabArea>
	
	<u:buttonArea>
		<u:button href="javascript:goCatCd()" titleId="pt.btn.cat" alt="카테고리" auth="A" />
		<u:button href="javascript:regAuthGrp()" titleId="cm.btn.reg" alt="등록" auth="A" popYn="Y" />
		<u:button href="javascript:modAuthGrp()" titleId="cm.btn.mod" alt="수정" auth="A" popYn="Y" />
		<u:button href="javascript:delAuthGrp()" titleId="cm.btn.del" alt="삭제" auth="A" />
		<u:button href="javascript:goAuthSetup()" titleId="pt.btn.auth" alt="권한설정" auth="A" />
	</u:buttonArea>
</div>

<%// 오른쪽쪽 : 탭 - 포함대상,제외대상 %>
<div class="right" style="float:right; width:70%;"><u:set test="${false}" var="tabSubType" />
	<u:tabGroup id="exclTab" noBottomBlank="true">
		<u:tab id="exclTab" alt="포함대상" onclick="onExcliYnTabClick('N');" titleId="pt.jsp.setAuthGrp.incTgt" areaId="setInclDetl" on="true" />
		<u:tab id="exclTab" alt="제외대상" onclick="onExcliYnTabClick('Y');" titleId="pt.jsp.setAuthGrp.excTgt" areaId="setExclDetl" />
	</u:tabGroup>

	<u:tabArea outerStyle="height:580px; overflow:hidden;" innerStyle="NO_INNER_IDV" >
	<iframe id="setInclDetl" name="setInclDetl" src="./setAuthGrpDetlFrm.do?menuId=${menuId}" style="width:100%; height:578px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
	<iframe id="setExclDetl" name="setExclDetl" src="./setAuthGrpDetlFrm.do?menuId=${menuId}" style="width:100%; height:578px; display:none;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
	</u:tabArea>
	
	<u:buttonArea id="rightBtnArea">
		<u:button href="javascript:setAuthGrpDetlPop('userGrp');" alt="사용자그룹" titleId="pt.jsp.setAuthGrp.userGrp" id="userGrp" auth="A" popYn="Y" />
		<u:button href="javascript:setAuthGrpDetlPop('user');" alt="사용자" titleId="pt.btn.user" id="user" auth="A" popYn="Y" style="display:none;" />
		<u:button href="javascript:delUsers();" alt="선택삭제" titleId="cm.btn.selDel" id="selDel" auth="A" style="display:none;" />
		<u:button href="javascript:setAuthGrpDetlPop('dept');" alt="부서" titleId="pt.btn.dept" id="dept" auth="A" />
		<u:button href="javascript:setAuthGrpDetlPop('posit');" alt="직위" termId="or.term.posit" id="posit" auth="A" />
		<u:button href="javascript:setAuthGrpDetlPop('grade');" alt="직급" termId="or.term.grade" id="grade" auth="A" />
		<u:button href="javascript:setAuthGrpDetlPop('title');" alt="직책" termId="or.term.title" id="title" auth="A" />
		<u:button href="javascript:setAuthGrpDetlPop('duty');" alt="직책" termId="or.term.duty" id="duty" auth="A" />
		<u:button href="javascript:setAuthGrpDetlPop('role');" alt="역할" termId="or.term.role" id="role" auth="A" />
		<u:button href="javascript:setAuthGrpDetlPop('secul');" alt="보안등급" termId="or.term.secul" id="secul" auth="A" /><c:if
			test="${sysPlocMap.useAduStatAuth eq 'Y'}">
		<u:button href="javascript:setAuthGrpDetlPop('aduStat');" alt="겸직 상태" titleId="pt.sysopt.aduStat" id="aduStat" auth="A" /></c:if>
		<u:button href="javascript:saveAuthGrpDetl()" alt="저장" titleId="cm.btn.save" id="save" auth="A" />
	</u:buttonArea>
</div>