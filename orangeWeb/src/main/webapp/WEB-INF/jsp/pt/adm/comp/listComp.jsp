<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<%// 언어 체크 박스 클릭시 - 회사명 받을 수 있는 칸 늘려주고 다이얼로그 높이 조절함 %>
function toggleRescVa(ckeck){
	var $dialog = $("#setCompDialog");
	var count = $dialog.find("input:checked").length;
	if(count==0){
		alertMsg('pt.msg.atleast.one.lang');<%//pt.msg.atleast.one.lang=최소한 하나의 언어는 선택해야 합니다.%>
		ckeck.checked = true;
	} else {
		var value = ckeck.value;
		var checked = ckeck.checked;
		$dialog.find("#setCompPop #lang_"+value).css("display", checked ? "block" : "none");
		$dialog.height($dialog.height() + (checked ? 24 : -24));
	}
}
<%// [팝업]회사정보 저장 %>
function saveComp(){
	var $dialog = $("#setCompDialog");
	var valid = true, $rescVa;
	$dialog.find("input:checked").each(function(){
		if(valid){
			$rescVa = $dialog.find("#rescVa_"+$(this).val());
			if($rescVa.val().trim()==''){
				alertMsg('cm.input.check.mandatory',[validator.getTitle("rescVa_"+$(this).val())]);
				$rescVa.focus();
				valid = false;
			}
		}
	});
	if(valid){
		var $form = $("#setCompPop");
		if(validator.validate('setCompPop')){
			$form.attr("action","./transComp.do");
			$form.attr("target","dataframe");
			$form.submit();
		}
	}
}
<%// 위로, 아래로 이동%>
function move(direction){
	var arr = getCheckedValue("listArea", "pt.jsp.listComp.noSelect");<%//pt.jsp.listComp.noSelect=선택된 회사가 없습니다.%>
	if(arr!=null){
		callAjax('./transCompMoveAjx.do?menuId=${menuId}', {direction:direction, compIds:arr}, function(data){
			if(data.message!=null){
				alert(data.message);
			}
			if(data.compIds!=null){
				var $tbody = $("#listArea tbody:first");
				var $next = $tbody.find("tr:first-child"), $toMove;
				for(var i=0;i<data.compIds.length;i++){
					$toMove = $tbody.find("#tr"+data.compIds[i]);
					$next.after($toMove);
					$next = $toMove;
				}
			}
		});
	}
}
<%// 관리자 메뉴 초기화 %>
function initAdminMnu(){
	var arr = getCheckedValue("listArea", "pt.jsp.listComp.noSelect");<%//pt.jsp.listComp.noSelect=선택된 회사가 없습니다.%>
	if(arr!=null){
		callAjax('./transInitAdmMnuAjx.do?menuId=${menuId}', {compIds:arr}, function(data){
			if(data.message!=null){
				alert(data.message);
			}
		});
	}
}
<%// 회사 관리자 설정 %>
function setCompAdm(){
	var arr = getCheckedValue("listArea", "pt.jsp.listComp.noSelect");<%//pt.jsp.listComp.noSelect=선택된 회사가 없습니다.%>
	if(arr==null) return;
	if(arr.length>1){
		alertMsg("pt.jsp.listComp.selectOne");<%//pt.jsp.listComp.selectOne=하나의 회사를 선택해 주십시요.%>
		return;
	}
	var compId = arr[0], users = [];
	callAjax('./getCompAdmAjx.do?menuId=${menuId}', {compId:compId}, function(data){
		if(data.userUids!=null && data.userUids!=''){
			data.userUids.split(',').each(function(index, uid){
				users.push({userUid:uid});
			});
		}
	});
	searchUserPop({data:users, compId:compId, multi:true, titleId:'pt.jsp.listComp.setCompAdm'}, function(arr){
		var uids = [];
		if(arr!=null){
			arr.each(function(index, userVo){
				uids.push(userVo.userUid);
			});
			callAjax('./transCompAdmAjx.do?menuId=${menuId}', {compId:compId, userUids:uids.join(',')}, function(data){
				if(data.message!=null){
					alert(data.message);
				}
			});
		}
	});
}
<%// 시스템 관리자 지정 %>
function setSysAdm(){
	var users = [];
	callAjax('./getSysAdmAjx.do?menuId=${menuId}', null, function(data){
		if(data.userUids!=null && data.userUids!=''){
			data.userUids.split(',').each(function(index, uid){
				users.push({userUid:uid});
			});
		}
	});
	searchUserPop({data:users, multi:true, global:'Y', titleId:'pt.jsp.listComp.setSysAdm'}, function(arr){
		var uids = [];
		if(arr!=null){
			arr.each(function(index, userVo){
				uids.push(userVo.userUid);
			});
			callAjax('./transSysAdmAjx.do?menuId=${menuId}', {userUids:uids.join(',')}, function(data){
				if(data.message!=null){
					alert(data.message);
				}
			});
		} else {
			alertMsg("pt.jsp.listComp.setSysAdm.needOne");<%//pt.jsp.listComp.setSysAdm.needOne=시스템 관리자는 최소 1명은 지정 되어야 합니다.%>
		}
	});
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>

<u:title titleId="pt.jsp.listComp.title" alt="회사 관리" menuNameFirst="true" />

<%// 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listComp.do">
	<input type="hidden" name="menuId" value="${menuId}" />
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="cols.compNm" alt="회사명" /></td>
		<td><u:input id="schWord" titleId="cm.schWord" style="width:300px;" value="${param.schWord}" maxByte="50" /></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<%// 목록 %>
<u:listArea id="listArea">

	<tr>
	<td width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td>
	<td width="8%" class="head_ct"><u:msg titleId="cols.compId" alt="회사ID" /></td>
	<td class="head_ct"><u:msg titleId="cols.compNm" alt="회사명" /></td>
	<td width="15%" class="head_ct"><u:msg titleId="cols.rcd" alt="참조코드" /></td>
	<td width="20%" class="head_ct"><u:msg titleId="pt.cols.useLang" alt="사용언어" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
	</tr>

<c:if test="${fn:length(ptCompBVoList)==0}" >
	<tr>
	<td class="nodata" colspan="8"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(ptCompBVoList) >0}" >
	<c:forEach items="${ptCompBVoList}" var="ptCompBVo" varStatus="status">
	<tr id="tr${ptCompBVo.compId}" onmouseover="this.className='trover'" onmouseout="this.className='trout'">
	<td class="bodybg_ct"><input type="checkbox" id="contactBack" value="${ptCompBVo.compId}"/></td>
	<td class="body_ct">${ptCompBVo.compId}</td>
	<td class="body_lt"><a href="javascript:dialog.open('setCompDialog','<u:msg titleId="pt.jsp.listComp.title"/>','./setCompPop.do?menuId=${menuId}&compId=${ptCompBVo.compId}');" title="<u:msg titleId="pt.jsp.listComp.title"/> - <u:msg titleId="cm.pop"/>"><u:out value="${ptCompBVo.rescNm}" /></a></td>
	<td class="body_ct"><u:out value="${ptCompBVo.rcd}" /></td>
	<td class="body_ct">
		<u:convert srcId="lang${ptCompBVo.compId}" var="langList" />
		<c:forEach items="${langList}" var="ptCdBVo" varStatus="langStatus">
		<u:out value="${ptCdBVo.rescNm}" /><c:if test="${not langStatus.last }">,</c:if>
		</c:forEach>
	</td>
	<td class="body_ct">${ptCompBVo.regrNm}</td>
	<td class="body_ct"><u:out value="${ptCompBVo.regDt}" type="date" /></td>
	<td class="body_ct"><u:yn value="${ptCompBVo.useYn}" yesId="cm.option.use" noId="cm.option.notUse" alt="사용/사용안함" /></td>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>

<u:msg titleId="pt.jsp.listComp.reg" var="addTitle" alt="회사 등록" />
<u:buttonArea>
	<u:button titleId="pt.jsp.listComp.setSysAdm" alt="시스템 관리자 지정" href="javascript:setSysAdm();" auth="SYS" />
	<u:button titleId="pt.jsp.listComp.setCompAdm" alt="회사 관리자 지정" href="javascript:setCompAdm();" auth="SYS" /><!--
	<u:button titleId="pt.jsp.listComp.initAdmMnu" alt="관리자 메뉴 초기화" href="javascript:initAdminMnu();" auth="SYS" />-->
	<u:button titleId="cm.btn.reg" alt="등록" href="javascript:dialog.open('setCompDialog','${addTitle}','./setCompPop.do?menuId=${menuId}');" popYn="Y" auth="SYS" />
	<u:button titleId="cm.btn.up" alt="위로이동" href="javascript:move('up');" auth="SYS" />
	<u:button titleId="cm.btn.down" alt="아래로이동" href="javascript:move('down');" auth="SYS" />
</u:buttonArea>
