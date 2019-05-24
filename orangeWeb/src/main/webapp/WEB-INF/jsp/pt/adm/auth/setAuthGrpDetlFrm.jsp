<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%// [탭] 탭구분 - 사용자/권한조합 중 선택된 탭 : C:권한조합(composition), U:사용자(user) %>
var gUserComb = '${param.userCombCd==null ? "C" : param.userCombCd}';
var gSeqBoxId = null;
var gCurrSeq = parseInt('${seqCount==null ? 0 : seqCount}');<%// 데이터 조회 후 세팅 필요 //TODO%>
<%// [탭] 사용자/권한조합 - 클릭 %>
function onUserCombTabClick(userComb){
	if(gUserComb != userComb){
		if(userComb=='C'){
			$("#tabBtnAdd").show();
			$("#tabBtnDel").show();
		} else {
			$("#tabBtnAdd").hide();
			$("#tabBtnDel").hide();
		}
		gUserComb = userComb;
		parent.setUserCombCd(userComb);
	}
}
function getTabObj(withSeqBox){
	var obj = gUserComb=='C' ? $("#cmpsDetl") : $("#userDetl");
	if(withSeqBox) return obj.find("#"+gSeqBoxId);
	return obj;
}
<%// [소버튼] 서브그룹 - 추가/제거 %>
function modifySubGrp(cmd){
	if(cmd=='add'){
		var buffer = [];
		$cmps = $("#cmpsDetl");
		buffer.push('\n');
		if($cmps.find("div").length>0){
			buffer.push('<div id="blank" class="blank"></div>');
		}
		gCurrSeq++;
		buffer.push('<div id="seq'+gCurrSeq+'" class="groupdiv" style="cursor:hand; min-height:18px;" onclick="setSelected(\'cmpsDetl\',\'seq'+gCurrSeq+'\');" >');
		buffer.push('<dl>');
		buffer.push('</dl>');
		buffer.push('</div>\n');
		$cmps.append(buffer.join('\n'));
		setSelected('cmpsDetl','seq'+gCurrSeq);
	} else if(cmd=='del'){
		var $seqBox = null;
		if(gSeqBoxId!=null){
			$seqBox = $("#cmpsDetl #"+gSeqBoxId);
			gSeqBoxId = null;
		} else {
			$seqBox = $("#cmpsDetl div:last");
		}
		if($seqBox.length>0){
			if($seqBox.find("dd").length>0){<%// 설정된 내용이 있으면 - 변경 되었음 표기 %>
				parent.gDetlChanged = true;
			}
			$seqBox.prev().remove();
			$seqBox.remove();
		}
	}
}
<%// 현재 선택된 시퀀스( 탭:포함대상/제외대상 하위 탭:사용자/권한조합 하위의 박스 시퀀스)
  // - parent 에서 직위,직급 등의 팝업 호출할때 사용 %>
function getSelectedGrpIds(grpKndCd){
	if(grpKndCd != 'user'){<%//[텝]권한조합 의 경우%>
		if(gSeqBoxId==null) return null;<%// 선택된 것이 없으면 null 리턴%>
		var grpIdStr = $("#cmpsDetl #"+gSeqBoxId+" #"+grpKndCd).attr("data-grpIds");
		if(grpIdStr==null) return [];
		<%// 하위포함여부가 grpId와 결합되어 있으면 떼어 내고 리턴%>
		var grpIds = grpIdStr.split(",");
		if(grpKndCd!='dept'){
			return grpIds;
		} else {
			var depts = [];
			grpIds.each(function(index, value){
				if(value.endsWith(":Y")){
					depts.push({orgId:value.substring(0, value.length-2), withSub:'Y'});
				} else if(value.endsWith(":N")){
					depts.push({orgId:value.substring(0, value.length-2), withSub:'N'});
				} else {
					depts.push({orgId:value, withSub:'N'});
				}
			});
			return depts;
		}
	} else {<%//[텝]사용자 의 경우%>
		return getIframeContent('userSetupFrm').getUserUids('userUid');
	}
}
<%// 선택삭제 버튼(사용자 삭제) %>
function delSelectedUser(){
	getIframeContent('userSetupFrm').delSelectedUser();
}
<%// 서브그룹 박스 클릭 - 선택을 활성화 시킴
  // detlId : 사용자/권한조합(U/C) 텝 구분
  // seqBoxId : 사용자/권한조합 밑의 박스 - db:seq %>
function setSelected(detlId, seqBoxId){
	$("#"+detlId+" div").each(function(){
		if($(this).attr("id")=="blank") return;
		if(seqBoxId==$(this).attr("id")){
			$(this).attr("class", "groupdiv_on");
			gSeqBoxId = seqBoxId;
		} else {
			$(this).attr("class", "groupdiv");
		}
	});
}
<%// [팝업 버튼] 확인 - 직위,직급 등의 팝업에서 확인 버튼 클릭시 %>
function setAuthGrpDetlSelected(grpKndCd, ids, rescIds, rescNms){
	var $dl = getTabObj(grpKndCd).find("dl");
	var $dd = $dl.find("#"+grpKndCd);
	if($dd.length==0){
		var subTitle = '';
		if(grpKndCd=='userGrp'){ subTitle = '<u:msg titleId="pt.jsp.setAuthGrp.userGrp" alt="사용자그룹" />'; }
		else if(grpKndCd=='user'){ subTitle = '<u:msg titleId="pt.btn.dept" alt="사용자그룹" />'; }
		else if(grpKndCd=='dept'){ subTitle = '<u:msg titleId="pt.btn.dept" alt="부서" />'; }
		else if(grpKndCd=='aduStat'){ subTitle = '<u:msg titleId="pt.sysopt.aduStat" alt="겸직 상태" />'; }
		else { subTitle = callTerm("or.term."+grpKndCd); }
		<%//html : <dd id="secul" class="group_txt" data-ids="D01:S,D02" data-rescIds="D01:S,D02">[보안등급] 1,2,3,4</dd> %>
		var html = '\n<dd id="'+grpKndCd+'" class="group_txt" data-grpIds="'+ids.join(',')+'" data-rescIds="'+rescIds.join(',')+'"><a href="javascript:modifySettings(\''+gSeqBoxId+'\',\''+grpKndCd+'\')">['+subTitle+'] '+rescNms.join(', ')+'</a></dd>';
		$dl.append(html).trigger('create');
	} else {
		if(ids.length==0){
			$dd.remove();
		} else {
			$dd.attr("data-grpIds", ids.join(','));
			$dd.attr("data-rescIds",rescIds.join(','));
			var $a = $dd.find("a");
			var txt = $a.text();
			txt = txt.substring(0, txt.indexOf(']')+2) + rescNms.join(', ');
			$a.text(txt);
		}
	}
}<%// 사용자 리로드 - 사용자 선택 팝업에서 확인버튼 클릭 %>
function setUsers(users){
	reloadFrame("userSetupFrm", "/or/user/listSeltdUserFrm.do?menuId=${menuId}&noPad=Y&lstSetupMetaId=OR_SETUP&opt=multi&userUids="+users);
}
<%// 저장용 데이터 모으기 - parent 에서 호출, 프레임이 포함대상,제외대상 2개 이어서 각가 호출해서 param 에 AJAX용 데이터를 모음
  // excliYn : 제외대상 여부 %>
function collectDataToSave(param, excliYn){
	var prefix = param.get('compId')+'-'+param.get('authGrpTypCd')+'-'+param.get('authGrpId')+'-'+excliYn+'-';
	
	var seq = 1, id, grpIds, rescIds, hasValue;
	$("#cmpsDetl div").each(function(){
		if($(this).attr("id")=="blank") return;
		
		hasValue = false;
		$(this).find("dd").each(function(){
			id = $(this).attr('id');
			grpIds = $(this).attr('data-grpIds');
			rescIds = $(this).attr('data-rescIds');
			if(grpIds!=null && grpIds.length>0){
				hasValue = true;
				param.put(prefix+seq+'-'+id+'-grpIds', grpIds);
				param.put(prefix+seq+'-'+id+'-rescIds', rescIds);
			}
		});
		
		if(hasValue){
			seq++;
		}
	});
	<%// 사용자 데이터 모으기 %>
	var users = getIframeContent('userSetupFrm').getAllUsers();
	if(users!=null){
		grpIds = [];
		rescIds = [];
		users.each(function(index, userVo){
			grpIds.push(userVo.userUid);
			rescIds.push(userVo.rescId);
		});
		param.put(prefix+seq+'-user-grpIds', grpIds.join(','));
		param.put(prefix+seq+'-user-rescIds', rescIds.join(','));
	}
}
<%// 데이터 저장 - submit %>
function saveAuthGrpDetl(param){
	callAjax('./transAuthGrpDetlAjx.do?menuId=${menuId}', param, function(data){
		if(data.message!=null){
			alert(data.message);
		}
		if(data.result=="ok"){
			parent.reloadRight();
		}
	});
}
<%// 목록 클릭 - 직급,보안등급 등의 설정된 줄 클릭 %>
function modifySettings(seqId, grpKndCd){
	setSelected('cmpsDetl', seqId);
	parent.setAuthGrpDetlPop(grpKndCd);
}
$(document).ready(function() {
	<%// 설정된 시퀀스박스가 없으면 디폴트로 하나 추가함 %>
	<c:if test="${seqCount == 0}" >modifySubGrp('add');</c:if>
	parent.setUserCombCd("${param.authGrpTypCd=='G' or param.userCombCd == 'U' ? 'U' : 'C'}");
});
//-->
</script>
<c:if test="${not empty param.authGrpId}">
<form id="setAuthGrpDetlForm" style="padding:10px 10px 0px 10px;">

<u:tabGroup id="detlTab" noBottomBlank="true"><u:set test="${param.authGrpTypCd == 'G' or param.userCombCd == 'U'}" var="tabOn" value="true" elseValue="false" />
	<u:tab id="detlTab" alt="사용자" onclick="onUserCombTabClick('U');" areaId="userDetl" titleId="pt.jsp.setAuthGrp.user" on="${tabOn}" />
	<c:if test="${param.authGrpTypCd != 'G'}" ><u:set test="${param.authGrpTypCd != 'G' and param.userCombCd != 'U'}" var="tabOn" value="true" elseValue="false" />
	<u:tab id="detlTab" alt="권한조합" onclick="onUserCombTabClick('C');" areaId="cmpsDetl" titleId="pt.jsp.setAuthGrp.authComb" on="${tabOn}" />
	<u:set test="${param.userCombCd == 'U'}" var="tabBtnStyle" value="display:none;" elseValue="" />
	<u:tabButton alt="추가" titleId="cm.btn.add" href="javascript:modifySubGrp('add');" id="tabBtnAdd" auth="A" img="ico_add.png" style="${tabBtnStyle}" />
	<u:tabButton alt="삭제" titleId="cm.btn.del" href="javascript:modifySubGrp('del');" id="tabBtnDel" auth="A" img="ico_minus.png" style="${tabBtnStyle}" />
	</c:if>
</u:tabGroup>

<u:tabArea outerStyle="height:532px;overflow-x:hidden; overflow-y:auto;" innerStyle="height:502px; margin:10px; border:1" noBottomBlank="true">

	<u:set test="${param.authGrpTypCd == 'G' or param.userCombCd == 'U'}" var="divStyle" value="display:none;" elseValue="" />
	<div id="cmpsDetl" style="${divStyle}">
	<c:forEach begin="1" end="${seqCount}" step="1" var="no" varStatus="outerStatus">
		<c:if test="${not outerStatus.first}"><div id="blank" class="blank"></div></c:if>
		<div id="seq${no}" class="groupdiv" style="cursor:hand; min-height:18px;" onclick="setSelected('cmpsDetl','seq${no}');">
		<dl>
		<c:forEach items="${grpKndCds}" var="grpKndCd" varStatus="status">
			<u:convertMap srcId="grpKndCdMap${no}" attId="${grpKndCd}GrpIds" var="grpIds"
			/><u:convertMap srcId="grpKndCdMap${no}" attId="${grpKndCd}RescIds" var="rescIds"
			/><u:convertMap srcId="grpKndCdMap${no}" attId="${grpKndCd}RescNms" var="rescNms"
			/><u:convertMap srcId="grpKndCdMap${no}" attId="${grpKndCd}RescTerm" var="rescTerm"
			/>
			<c:if test="${not empty grpIds}"><dd id="${grpKndCd}" class="group_txt" data-grpIds="${grpIds}" data-rescIds="${rescIds}"><a href="javascript:modifySettings('seq${no}','${grpKndCd}')">[${rescTerm}] ${rescNms}</a></dd></c:if>
		</c:forEach>
		</dl>
		</div>
	</c:forEach>
	</div>
	
	<u:set test="${param.authGrpTypCd == 'G' or param.userCombCd == 'U'}" var="divStyle" value="" elseValue="display:none;" />
	<div id="userDetl" style="${divStyle}">
	<iframe id="userSetupFrm" name="userSetupFrm" src="/or/user/listSeltdUserFrm.do?menuId=${menuId}&noPad=Y&lstSetupMetaId=OR_SETUP&opt=multi&userUids=${userGrpIds}" style="width:100%; height:518px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
	</div>

</u:tabArea>

</form>
</c:if>