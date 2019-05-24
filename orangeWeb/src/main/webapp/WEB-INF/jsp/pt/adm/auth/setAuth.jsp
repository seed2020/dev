<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
// [사용안함] : setAuthByAuthGrpPop.jsp 로 대체
%>
<script type="text/javascript">
<!--
<%// (select)권한그룹, (select)메뉴그룹 / 포틀릿 - 변경시 %>
function reloadSelf(){
	var url = "./set${authMdCd}Auth.do?menuId=${menuId}${param.authScopCd eq 'IP_EX' ? '&authScopCd=IP_EX' : ''}";
	var $form = $("#setAuthForm");
	var authGrpId = $form.find("#authGrpId").val();
	var mnuGrpId = $form.find("#mnuGrpId").val();
	if(authGrpId != null && mnuGrpId != null){
		url += "&authGrpId="+authGrpId+"&mnuGrpId="+mnuGrpId;
		location.replace(url);
	}
	
}
<%// 테이블헤더의 제목 클릭 - 전체선택%>
function checkAll(authCd){
	$radios = $("#authByAuthGrpForm input[type='radio']");
	$radios.each(function(){
		<%// 라디오중 값이 클릭한 것과 같고, 메뉴 없는 폴더가 아니면 %> 
		if($(this).val()==authCd && !gOnlyFldTrs.contains($(this).attr('data-extra'))){
			this.checked = true;
		}
	});
	$radios.uniform.update();
}
<%// 라이오 클릭 - 메뉴그룹의 경우만 해당, -- 맨상위의 메뉴 그룹은 따로 설정 가능하도록 이벤트 연동하지 않음(나머지 권한설정을 건들이지 않고 임시로 전체 권한을 제거할때 사용 목적)
  // 1. 폴더클릭 - 폴더아래 모든것 클릭된 조건으로
  // 2. (권한)없음 이 아닌것 클릭 - 폴더에 권한이 없으면 조회 권한 부여
  // 3. (권한)없음 클릭 - 해당 폴더가 아무 권한도 없으면 폴더에 (권한)없음 부여 %>
function onRadioClick(path, authCd){
	var starts = path+"_";
	$trs = $("#authByAuthGrpForm tr");
	<%// 폴더 클릭 했을때 - 하위 모두 같은 권한 주기 %>
	$trs.each(function(){
		if($(this).attr("id").startsWith(starts)){
			$(this).find("input[type='radio']").each(function(){
				if($(this).val()==authCd && !gOnlyFldTrs.contains($(this).attr('data-extra'))){
					this.checked = true;
				}
			});
		}
	});
	<%// (권한)없음 이 아닌것 클릭 - 폴더에 권한이 없으면 조회 권한 부여 %>
	if(authCd!='N'){
		var pPath = path, p, $checked, $authR;
		while((p = pPath.lastIndexOf('_'))>0){
			pPath = pPath.substring(0,p);
			$trs.each(function(){
				if($(this).attr("id")==pPath){
					$checked = $(this).find("input[type='radio']:checked");
					if($checked.length==0 || $checked.val()=='N'){
						$authR = $(this).find("input[value='R']");
						if($authR.length>0 && !gOnlyFldTrs.contains($authR.attr('data-extra'))){
							$authR[0].checked = true;
						}
					}
				}
			});
		}
	}
	<%// (권한)없음 클릭 - 해당 폴더가 아무 권한도 없으면 폴더에 (권한)없음 부여 %>
	if(authCd=='N'){
		var flds = [], id, p, $authN;
		<%// 권한있다고 체크된 것의 폴더 id를 flds 배열에 모음 %>
		$trs.each(function(){
			if($(this).attr("data-fldYn")=="N"){<%// tr중 폴더 인것만 %>
				$(this).find("input[type='radio'][value!='N']:checked").each(function(){
					id = $(this).attr("data-extra");
					while(true){
						if(!flds.contains(id)) flds.push(id);
						if((p = id.lastIndexOf('_'))<2) break;
						id = id.substring(0,p);
					}
				});
			}
		});
		<%// (권한)없음 처리함 %>
		$trs.each(function(){
			if($(this).attr("data-fldYn")=="Y"){<%// tr중 폴더 인것만 %>
				if(!flds.contains($(this).attr("id"))){<%// 폴더의 id가 권한있는 폴더목록(flds)에 없으면 %>
					if($(this).find("input[type='radio']:checked").length>0){<%// tr에 체크된 radio 가 있으면 %>
						$authN = $(this).find("input[value='N']");
						if($authN.length>0 && !gOnlyFldTrs.contains($authN.attr('data-extra'))){
							$authN[0].checked = true;
						}
					}
				}
			}
		});
	}
	<%// 그룹의 tr 과 권한코드를 찾음 %>
	var $grp = null, grpAuthCd=null;
	$trs.each(function(){
		if($(this).attr('id')=='GRP'){
			$grp = $(this);
			grpAuthCd = $(this).find("input[type='radio']:checked").val();
			if(grpAuthCd==null) grpAuthCd = 'N';
		}
	});
	<%// 그룹이 있는 경우에 %>
	if(grpAuthCd != null){
		if(authCd=='N' && grpAuthCd!='N'){<%// 권한없음 클릭 했을 때 그룹권한이 있으면 - 권한이 있는 곳이 있는지 찾아 본뒤 없으면 - 권한없음 처리%>
			var hasAuth = false, eachAuthCd;
			$trs.each(function(){
				if($(this).attr('id')!='GRP'){
					eachAuthCd = $(this).find("input[type='radio']:checked").val();
					if(eachAuthCd!=null && eachAuthCd!='N'){
						hasAuth = true;
					}
				}
			});
			if(!hasAuth){
				$grp.find("input[type='radio'][value='N']")[0].checked = true;
			}
		} else if(authCd!='N' && grpAuthCd=='N'){<%// 특정 권한을 클릭 했을때 그룹권한이 없으면 - 그룹권한을 R로 세팅 %>
			$grp.find("input[type='radio'][value='R']")[0].checked = true;
		}
	}
	
	$("#authByAuthGrpForm input[type='radio']").uniform.update();
}
<%// [버튼] 저장 %>
function saveAuth(){
	var $form = $("#authByAuthGrpForm");
	$form.attr('method','post');
	$form.attr('action','/pt/adm/auth/transAuth.do');
	$form.attr('target','dataframe');
	$form.submit();
}
<%// [버튼] 취소 %>
function goBack(){
	location.href = "./set${authMdCd}AuthGrp.do?menuId=${menuId}&authGrpId=${authGrpId}";
}
<%// 메뉴없이 폴더로만 구성된 tr의 id 목록 %>
var gOnlyFldTrs = [];
<%// 메뉴없이 폴더로만 구성된 tr의 radio 를 disable 시키고, 전역변수 onlyFldTrs에 할당 > 선택변경시 스크립트에서 제외 처리 %>
function setOnlyFldTrs(){
	var $trs = $("#authByAuthGrpForm tr");
	var flds = [], p, id;
	<%// 폴더로만 구성된 tr의 id를 flds에 세팅함 %>
	$trs.each(function(){
		id = $(this).attr("id");
		if($(this).attr("data-fldYn")=="Y"){<%// tr중 폴더면 %>
			flds.push(id);<%// 폴더 id를 flds에 저장 %>
		} else {<%// tr중 폴더가 아니면 %>
			if(id!=null){
				while((p = id.lastIndexOf('_'))>1){<%// '_' 를 하나씩 뒤어서 부터 제거 하면서 배열에서 제거함 %>
					id = id.substring(0,p);
					flds = flds.removeValues(id);
				}
			}
		}
	});
	<%// 폴더로만 구성된 tr의 radio를 disable 시킴 %>
	$trs.each(function(){
		if(flds.contains($(this).attr("id"))){
			$(this).find("input[type='radio']").each(function(){ this.disabled = true; });
		}
	});
	<%// 폴더로만 구성된 tr목록을 전역변수 gOnlyFldTrs에 저장 %>
	gOnlyFldTrs = flds;
}
$(document).ready(function() {
	<%// 메뉴없이 폴더로만 구성된 tr의 radio 를 disable 시키고, 전역변수 onlyFldTrs에 할당 - 선택변경시 스크립트에서 제외 처리 %>
	<c:if test="${mnuGrpId != 'PORTLET'}">setOnlyFldTrs();</c:if>
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>
<c:if
	test="${authMdCd eq 'User' and secuPolc.lginIpPolc eq 'authByIpRangePloc'}"><u:msg titleId="${param.authScopCd eq 'IP_EX' ? 'pt.jsp.setIpPloc.ipRangeEx' : 'pt.jsp.setIpPloc.ipRangeIn'}" var="titleSubfix" />
<u:title alt="사용자/관리자/모바일 권한 그룹 별 권한 관리" menuNameFirst="true" titleSuffix="${titleSubfix}" /></c:if><c:if
	test="${not(authMdCd eq 'User' and secuPolc.lginIpPolc eq 'authByIpRangePloc')}">
<u:title alt="사용자/관리자/모바일 권한 그룹 별 권한 관리" menuNameFirst="true" /></c:if>

<!--
//authByIpRange
-->
<div class="front">
	<form id="setAuthForm" >
	<input type="hidden" name="menuId" value="${menuId}" />
	<input type="hidden" name="authGrpTypCd" value="${authGrpTypCd}" />
	<div class="front_left">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td class="fronttit"><u:msg titleId="pt.jsp.setAuthGrp.authGrp" alt="권한그룹" /></td>
			<td class="width5"></td>
			<td class="frontinput"><select id="authGrpId" name="authGrpId" onchange="reloadSelf()"<u:elemTitle titleId="pt.jsp.setAuthGrp.authGrp" alt="권한그룹" />>
			<c:forEach items="${ptAuthGrpBVoList}" var="ptAuthGrpBVo" varStatus="status">
			<c:if test="${status.first}"><u:set
					test="true" value="<optgroup label='${ptAuthGrpBVo.authGrpCatNm}'>"/><c:set
					var="prevAuthGrpCatCd" value="${ptAuthGrpBVo.authGrpCatCd}"
			/></c:if><c:if test="${not status.first and prevAuthGrpCatCd != ptAuthGrpBVo.authGrpCatCd}"><u:set
					test="true" value="</optgroup><optgroup label='${ptAuthGrpBVo.authGrpCatNm}'>"/><c:set
					var="prevAuthGrpCatCd" value="${ptAuthGrpBVo.authGrpCatCd}"
			/></c:if>
			<u:option value="${ptAuthGrpBVo.authGrpId}" title="${ptAuthGrpBVo.rescNm}" checkValue="${param.authGrpId}" />
			<c:if test="${status.last}"><u:set test="true" value="</optgroup>" /></c:if>
			</c:forEach></select></td>
  		</tr>
		</table>
		
	</div>
	<div class="front_right">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td class="fronttit"><u:msg titleId="cm.gubun" alt="구분" /></td>
			<td class="width3"></td>
			<td class="frontinput"><select id="mnuGrpId" name="mnuGrpId" onchange="reloadSelf()"<u:elemTitle titleId="cm.gubun" alt="구분" /> >
			<optgroup label='<u:msg titleId="cols.mnuGrp" alt="메뉴그룹" />'>
				<c:forEach items="${ptMnuGrpBVoList}" var="ptMnuGrpBVo" varStatus="status">
				<u:option value="${ptMnuGrpBVo.mnuGrpId}" title="${ptMnuGrpBVo.rescNm}" checkValue="${mnuGrpId}" />
				</c:forEach>
			</optgroup><c:if
			
				test="${not empty ptMnuLoutDVoListA}">
			<optgroup label='<u:msg titleId="pt.cols.mnuComb" alt="메뉴조합" />'>
				<c:forEach items="${ptMnuLoutDVoListA}" var="ptMnuLoutDVo" varStatus="status">
				<u:option value="${ptMnuLoutDVo.mnuLoutId}" title="${ptMnuLoutDVo.rescNm}" checkValue="${mnuGrpId}" /></c:forEach>
			</optgroup></c:if><c:if
			
				test="${not empty ptMnuLoutDVoListB}">
			<optgroup label='<u:msg titleId="pt.cols.mnuCombB" alt="메뉴조합(기본 레이아웃)" />'>
				<c:forEach items="${ptMnuLoutDVoListB}" var="ptMnuLoutDVo" varStatus="status">
				<u:option value="${ptMnuLoutDVo.mnuLoutId}" title="${ptMnuLoutDVo.rescNm}" checkValue="${mnuGrpId}" /></c:forEach>
			</optgroup></c:if><c:if
			
				test="${not empty ptMnuLoutDVoListI}">
			<optgroup label='<u:msg titleId="pt.cols.mnuCombI" alt="메뉴조합(아이콘 레이아웃)" />'>
				<c:forEach items="${ptMnuLoutDVoListI}" var="ptMnuLoutDVo" varStatus="status">
				<u:option value="${ptMnuLoutDVo.mnuLoutId}" title="${ptMnuLoutDVo.rescNm}" checkValue="${mnuGrpId}" /></c:forEach>
			</optgroup></c:if><c:if
			
				test="${authGrpTypCd == 'U'}">
			<optgroup label='<u:msg titleId="cols.plt" alt="포틀릿" />'>
				<u:option value="PORTLET" titleId="cols.plt" alt="포틀릿" checkValue="${mnuGrpId}" />
			</optgroup></c:if>
			</select></td>
		</tr>
		</table>
	</div>
	</form>
</div>

<form id="authByAuthGrpForm">
<input type="hidden" name="menuId" value="${menuId}" /><c:if test="${param.authScopCd eq 'IP_EX'}">
<input type="hidden" name="authScopCd" value="${param.authScopCd}" /></c:if>
<input type="hidden" name="authGrpTypCd" value="${authGrpTypCd}" />
<input type="hidden" name="authGrpId" value="${authGrpId}" />
<input type="hidden" name="mnuPltTypCd" value="${mnuPltTypCd}" />
<input type="hidden" name="mnuGrpId" value="${mnuGrpId}" />

<u:listArea>

<c:if test="${mnuGrpId != 'PORTLET'}">
	<tr id="R">
		<td class="head_ct"><u:msg titleId="cols.mnu" alt="메뉴" /></td>
		<td width="8%" class="head_ct"><a href="javascript:checkAll('A')" title="<u:msg titleId='cm.check.all' />"><u:msg titleId="pt.jsp.setAuthByAuthGrp.authA" alt="관리" /></a></td>
		<td width="8%" class="head_ct"><a href="javascript:checkAll('M')" title="<u:msg titleId='cm.check.all' />"><u:msg titleId="pt.jsp.setAuthByAuthGrp.authM" alt="수정" /></a></td>
		<td width="8%" class="head_ct"><a href="javascript:checkAll('W')" title="<u:msg titleId='cm.check.all' />"><u:msg titleId="pt.jsp.setAuthByAuthGrp.authW" alt="등록" /></a></td>
		<td width="8%" class="head_ct"><a href="javascript:checkAll('R')" title="<u:msg titleId='cm.check.all' />"><u:msg titleId="pt.jsp.setAuthByAuthGrp.authR" alt="조회" /></a></td>
		<td width="8%" class="head_ct"><a href="javascript:checkAll('N')" title="<u:msg titleId='cm.check.all' />"><u:msg titleId="pt.jsp.setAuthByAuthGrp.authN" alt="없음" /></a></td>
	</tr>
	<tr id="GRP"><u:convertMap srcId="authCdMap" attId="${mnuGrpId}" var="authCd" hash="true" />
		<td class="body_lt" style="padding:2px 0 0 5px;"><img src="${_cxPth}/images/${_skin}/tree_folder_close.gif"/>[<u:msg titleId="cm.gubun" alt="구분" />] ${mnuGrpNm}</td>
		<td class="center"><u:radio name="grp_${mnuGrpId}" value="A" checked="${authCd == 'A'}" titleId="pt.jsp.setAuthByAuthGrp.authA" alt="관리" extraData="${ptMnuDVo.mnuPath}" noLabel="true" /></td>
		<td class="center"><u:radio name="grp_${mnuGrpId}" value="M" checked="${authCd == 'M'}" titleId="pt.jsp.setAuthByAuthGrp.authM" alt="수정" extraData="${ptMnuDVo.mnuPath}" noLabel="true" /></td>
		<td class="center"><u:radio name="grp_${mnuGrpId}" value="W" checked="${authCd == 'W'}" titleId="pt.jsp.setAuthByAuthGrp.authW" alt="등록" extraData="${ptMnuDVo.mnuPath}" noLabel="true" /></td>
		<td class="center"><u:radio name="grp_${mnuGrpId}" value="R" checked="${authCd == 'R'}" titleId="pt.jsp.setAuthByAuthGrp.authR" alt="조회" extraData="${ptMnuDVo.mnuPath}" noLabel="true" /></td>
		<td class="center"><u:radio name="grp_${mnuGrpId}" value="N" checked="${empty authCd}" titleId="pt.jsp.setAuthByAuthGrp.authN" alt="없음" extraData="${ptMnuDVo.mnuPath}" noLabel="true" /></td>
	</tr>
	<c:forEach items="${ptMnuDVoList}" var="ptMnuDVo" varStatus="status">
	<tr id="${ptMnuDVo.mnuPath}" data-fldYn="${ptMnuDVo.fldYn}" class="<u:set test="${ptMnuDVo.fldYn == 'Y'}" value="folder"
			/>" onmouseover="this.className='trover'" onmouseout="this.className='trout'"><u:set test="${ptMnuDVo.fldYn == 'Y'}" var="icoImg" value="tree_folder_close.gif" elseValue="ico_document.png"
			/><u:convertMap srcId="authCdMap" attId="${ptMnuDVo.mnuId}" var="authCd" hash="true" />
		<td class="body_lt" style="padding:2px 0 0 ${(ptMnuDVo.mnuLvl-1) * 15 + 5}px;"><img src="${_cxPth}/images/${_skin}/${icoImg}"/>${ptMnuDVo.rescNm}</td>
		<td class="center"><u:radio name="mnu_${ptMnuDVo.mnuId}" value="A" onclick="onRadioClick('${ptMnuDVo.mnuPath}','A');" checked="${authCd == 'A'}" titleId="pt.jsp.setAuthByAuthGrp.authA" alt="관리" extraData="${ptMnuDVo.mnuPath}" noLabel="true" /></td>
		<td class="center"><u:radio name="mnu_${ptMnuDVo.mnuId}" value="M" onclick="onRadioClick('${ptMnuDVo.mnuPath}','M');" checked="${authCd == 'M'}" titleId="pt.jsp.setAuthByAuthGrp.authM" alt="수정" extraData="${ptMnuDVo.mnuPath}" noLabel="true" /></td>
		<td class="center"><u:radio name="mnu_${ptMnuDVo.mnuId}" value="W" onclick="onRadioClick('${ptMnuDVo.mnuPath}','W');" checked="${authCd == 'W'}" titleId="pt.jsp.setAuthByAuthGrp.authW" alt="등록" extraData="${ptMnuDVo.mnuPath}" noLabel="true" /></td>
		<td class="center"><u:radio name="mnu_${ptMnuDVo.mnuId}" value="R" onclick="onRadioClick('${ptMnuDVo.mnuPath}','R');" checked="${authCd == 'R'}" titleId="pt.jsp.setAuthByAuthGrp.authR" alt="조회" extraData="${ptMnuDVo.mnuPath}" noLabel="true" /></td>
		<td class="center"><u:radio name="mnu_${ptMnuDVo.mnuId}" value="N" onclick="onRadioClick('${ptMnuDVo.mnuPath}','N');" checked="${empty authCd}" titleId="pt.jsp.setAuthByAuthGrp.authN" alt="없음" extraData="${ptMnuDVo.mnuPath}" noLabel="true" 
		/><c:if test="${mnuPltTypCd == 'L'}"><input type="hidden" name="mnug_${ptMnuDVo.mnuId}" value="${ptMnuDVo.mnuGrpId}" /></c:if></td>
	</tr>
	</c:forEach>
</c:if>

<c:if test="${mnuGrpId == 'PORTLET'}">
	<tr>
		<td width="20%" class="head_ct"><u:msg titleId="pt.btn.cat" alt="카테고리" /></td>
		<td class="head_ct"><u:msg titleId="cols.plt" alt="포틀릿" /></td>
		<td width="8%" class="head_ct"><a href="javascript:checkAll('A')" title="<u:msg titleId='cm.check.all' />"><u:msg titleId="pt.jsp.setAuthByAuthGrp.authA" alt="관리" /></a></td>
		<td width="8%" class="head_ct"><a href="javascript:checkAll('M')" title="<u:msg titleId='cm.check.all' />"><u:msg titleId="pt.jsp.setAuthByAuthGrp.authM" alt="수정" /></a></td>
		<td width="8%" class="head_ct"><a href="javascript:checkAll('W')" title="<u:msg titleId='cm.check.all' />"><u:msg titleId="pt.jsp.setAuthByAuthGrp.authW" alt="등록" /></a></td>
		<td width="8%" class="head_ct"><a href="javascript:checkAll('R')" title="<u:msg titleId='cm.check.all' />"><u:msg titleId="pt.jsp.setAuthByAuthGrp.authR" alt="조회" /></a></td>
		<td width="8%" class="head_ct"><a href="javascript:checkAll('N')" title="<u:msg titleId='cm.check.all' />"><u:msg titleId="pt.jsp.setAuthByAuthGrp.authN" alt="없음" /></a></td>
	</tr>
	<c:if test="${fn:length(ptPltDVoList)==0}" >
	<tr>
		<td class="nodata" colspan="8"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
	</c:if>
	<c:forEach items="${ptPltDVoList}" var="ptPltDVo" varStatus="status">
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'"><u:convertMap srcId="authCdMap" attId="${ptPltDVo.pltId}" var="authCd" hash="true" />
		<td class="center" style="padding:2px 0 0 2px;">${ptPltDVo.pltCatNm}</td>
		<td class="body_lt" style="padding:2px 0 0 5px;"><img src="${_cxPth}/images/${_skin}/ico_document.png"/>${ptPltDVo.rescNm}</td>
		<td class="center"><u:radio name="plt_${ptPltDVo.pltId}" value="A" checked="${authCd == 'A'}" titleId="pt.jsp.setAuthByAuthGrp.authA" alt="관리" noLabel="true" /></td>
		<td class="center"><u:radio name="plt_${ptPltDVo.pltId}" value="M" checked="${authCd == 'M'}" titleId="pt.jsp.setAuthByAuthGrp.authM" alt="수정" noLabel="true" /></td>
		<td class="center"><u:radio name="plt_${ptPltDVo.pltId}" value="W" checked="${authCd == 'W'}" titleId="pt.jsp.setAuthByAuthGrp.authW" alt="등록" noLabel="true" /></td>
		<td class="center"><u:radio name="plt_${ptPltDVo.pltId}" value="R" checked="${authCd == 'R'}" titleId="pt.jsp.setAuthByAuthGrp.authR" alt="조회" noLabel="true" /></td>
		<td class="center"><u:radio name="plt_${ptPltDVo.pltId}" value="N" checked="${empty authCd}" titleId="pt.jsp.setAuthByAuthGrp.authN" alt="없음" noLabel="true" /></td>
	</tr>
	</c:forEach>
</c:if>

</u:listArea>
</form>

<u:buttonArea><u:secu auth="A">
	<c:if
	test="${authMdCd eq 'User' and secuPolc.lginIpPolc eq 'authByIpRangePloc'}">
	<u:button href="${_uri}?authScopCd=${param.authScopCd eq 'IP_EX' ? 'IP_IN' : 'IP_EX'}&menuId=${menuId}"
		alt="외부망/내부망"
		titleId="${param.authScopCd eq 'IP_EX' ? 'pt.jsp.setIpPloc.ipRangeIn' : 'pt.jsp.setIpPloc.ipRangeEx'}" auth="A" /></c:if>
	</u:secu>
	<c:if test="${not empty authGrpId and not empty mnuGrpId}">
	<u:button href="javascript:saveAuth()" alt="저장" titleId="cm.btn.save" auth="A" />
	</c:if>
	<u:button href="javascript:goBack()" alt="취소" titleId="cm.btn.cancel" />
</u:buttonArea>