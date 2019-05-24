<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set var="frmYn" test="${!empty pageSuffix && pageSuffix == 'Frm' }" value="Y" elseValue="N"/>

<u:set var="agntParam" test="${!empty schBcRegrUid }" value="&schBcRegrUid=${schBcRegrUid }" elseValue=""/>
<u:params var="nonPageParams" excludes="schBcRegrUid,pageNo"/>
<u:set var="copyBcPage" test="${listPage eq 'listAllBc' }" value="transAllBcContensCopy" elseValue="transBcContensCopy"/>
<u:params var="nonInitialParams" excludes="schInitial, pageNo"/>

<u:set test="${param.typ eq 'L' || (empty param.typ && wbBcUserScrnSetupRVo.lstTypCd eq 'LIST') || listPage eq 'listAllBc'}" var="getCheckedTrs" value="getCheckedTrs" elseValue="getCheckedDivs"/>

<script type="text/javascript">
<% // 엑셀 파일 다운로드 %>
function excelDownFile() {
	var $form = $('#excelForm');
	$form.attr('method','post');
	$form.attr('action','./excelDownLoad.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form[0].submit();
};

//관리자 
<%// 검색조건 초기화 %>
function fnSchValInit(id){
	$('#'+(id == undefined ? 'search_area_all' : id )+' input').val('');
};

//1명의 사용자 선택
function schUserPop(){
	var data = {};<%// 팝업 열때 선택될 데이타 %>
	<%// option : data, multi, withSub, titleId %>
	searchUserPop({data:data}, function(userVo){
		$('#schRegrUid').val(userVo.userUid);
		$('#schRegrNm').val(userVo.rescNm);
	});
};

//대리명함 select 선택
function fnAgntSch(obj){
	$('#schFldNm').val('');
	$('#schFldId').val('');
	searchForm.submit();
};

<%// 삭제 - 배열에 담긴 목록%>
function selRowInArr(rowArr){
	var selArr = [], $bcId;
	//if(delVa!='') delArr.push(delVa);
	for(var i=0;i<rowArr.length;i++){
		$bcId = $(rowArr[i]).find("input[name='bcId']");
		if($bcId.val()!=''){
			selArr.push($bcId.val());
		}
	}
	return selArr;
};

//이메일 발송
function setEmailSend(){
	var arr = ${getCheckedTrs}("cm.msg.noSelect");
	var bcIds = "";
	if(arr!=null) bcIds = selRowInArr(arr); else return;
	
	parent.emailSendPop({bcIds:bcIds},'${menuId }');
};

<%// 복사 - 배열에 담긴 목록%>
function copyRowInArr(rowArr){
	var copyArr = [], $bcId;
	//if(delVa!='') copyArr.push(delVa);
	for(var i=0;i<rowArr.length;i++){
		$bcId = $(rowArr[i]).find("input[name='bcId']");
		if($bcId.val()!=''){
			copyArr.push($bcId.val());
		}
		//$(rowArr[i]).remove();
	}
	return copyArr;
};

//1명의 사용자 선택
function copyBcToUser(){
	var arr = ${getCheckedTrs}("cm.msg.noSelect");
	if(arr == null) return;
	var bcArr = copyRowInArr(arr);
	var data = {};<%// 팝업 열때 선택될 데이타 %>
	var userArr = [];
	<%// option : data, multi, withSub, titleId %>
	parent.searchUserPop({data:data}, function(userVo){
		if(userVo!=null){
			//대리자 체크
			if(userVo.userUid == '${schBcRegrUid}'){
				alertMsg("wb.msg.copy.noDupUserUid");
				return;
			}
			userArr.push(userVo.userUid);
		}
		if(userArr.length > 0){
			//$("#copyUserList").val(userArr.join(','));	
			if (true/*confirmMsg("cm.cfrm.save")*/ ) {
				callAjax('./${copyBcPage}.do?menuId=${menuId}', {mode:"copyBc", copyBcIds:bcArr, copyRegrUids:userArr}, function(data){
					if(data.message!=null){
						alert(data.message);
					}
					if(data.result =='ok'){
						$("#bcListArea tbody:first input[type='checkbox']:checked").each(function(){
							obj = getParentTag(this, 'tr');
							id = $(obj).attr('id');
							if(id!='headerTr' && id!='hiddenTr') $(this).trigger('click');
						});
						<c:if test="${listPage eq 'listAllBc'}">location.replace(location.href);</c:if>//관리자
					}
				});
			}
		}
	});
};

//명함 복사
function copyBc() {
	var objArr = fnSelBc();
	if(objArr != null){
		selArrs= {mode:'copyBc',toBcIds : objArr};
		parent.dialog.open('findBcFldPop','<u:msg titleId="wb.btn.fldChoi" alt="폴더 선택" />','./findBcFldPop.do?menuId=${menuId}');
	}
};

// 명함 이동
function moveBc() {
	var objArr = fnSelBc();
	if(objArr != null){
		selArrs= {mode:'moveBc',toBcIds : objArr};
		parent.dialog.open('findBcFldPop','<u:msg titleId="wb.btn.fldChoi" alt="폴더 선택" />','./findBcFldPop.do?menuId=${menuId}');
	}
};

<%// 선택목록 리턴 %>
function fnSelBc(){
	var arr = ${getCheckedTrs}("cm.msg.noSelect");
	if(arr!=null) return selRowInArr(arr);
	else return null;
};

// 우선연락처 세팅
function fnCntcTypCd( obj ){
	$("#setRegForm input[id='dftCntcTypCd']").val(obj.value);
};

// 사진 삭제후 페이지 리로드
function pageReload(){
	location.replace('./${listPage}.do?${params}');
};
//사진보기
function viewBcImageDetl(bcId){
	dialog.open('viewImageDialog', '<u:msg titleId="or.jsp.setOrg.viewImageTitle" alt="이미지 보기" />', './viewImagePop.do?menuId=${menuId}&bcId='+bcId);
};
<%// 검색조건 초기화 %>
fnSearchInit = function(){
	$("input#schFldId").val("");
	$checkboxs = $("#searchArea input[type='checkbox']");
	$checkboxs.each(function(){
		this.checked = false;
	});
	$("#searchArea input[type='text']").val("");
	$radios = $('#searchArea input:radio[name=schFldTypYn]');
	$radios.each(function(){
		if($(this).val() == 'A'){
			this.checked = true;	
		}
	});
	$checkboxs.uniform.update();
};

//폴더 팝업
function openFldChoi(schBcRegrUid){
	if(schBcRegrUid == null || schBcRegrUid == '') {
		alertMsg("wb.msg.noAgnt");
		return;
	}
	dialog.open('findBcFldPop','<u:msg titleId="wb.jsp.findBcFldPop.title" alt="폴더 선택" />','./findBcFldPop.do?menuId=${menuId}${agntParam}');	
};

// 폴더선택
fnFldSelect = function(){
	var tree = getIframeContent('findBcFldFrm').getTreeData();
	if(tree.id == 'ROOT'){
		$('#schFldNm').val('');
		$('#schFldId').val('');
	}else{
		$('#schFldId').val(tree.id);
		$('#schFldNm').val(tree.fldNm);
	}
	dialog.close('findBcFldPop');
};

<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(noSelectMsg){
	var arr=[], id, obj;
	$("#bcListArea tbody:first input[type='checkbox']:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
};

<%//checkbox 가 선택된 div 테그 목록 리턴 %>
function getCheckedDivs(noSelectMsg){
	var arr=[], id, obj;
	$("#bcListArea input[type='checkbox']:checked").each(function(){
		obj = getParentTag(this, 'dd');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
};


<%// 선택삭제%>
function fnDelete(){
	var arr = ${getCheckedTrs}("cm.msg.noSelect");
	if(arr!=null) delRowInArr(arr);
};

<%// 삭제 - 배열에 담긴 목록%>
function delRowInArr(rowArr){
	var delArr = [], $bcId;
	//if(delVa!='') delArr.push(delVa);
	for(var i=0;i<rowArr.length;i++){
		$bcId = $(rowArr[i]).find("input[name='bcId']");
		if($bcId.val()!=''){
			delArr.push($bcId.val());
		}
		//$(rowArr[i]).remove();
	}
	$("#delList").val(delArr.join(','));
	
	if(confirmMsg("cm.cfrm.del")) {
		var $form = $('#deleteForm');
		$form.attr('method','post');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
};

// 상세보기
function viewBc(bcId) {
	location.href="./${viewPage}${pageSuffix}.do?${paramsForList }&typ=${param.typ}${agntParam}&bcId="+bcId;
};

<%// [아이콘 선택] - 아이콘 클릭 - 아이콘 활성화 함 %>
function activeIcon(obj){
	$gIconArea = $("#iconArea");
	$gIconArea.find("dd#iconArea_sub").each(function(){
	$(this).attr("class", "cardtxt");
	});
	$(obj).parent().attr("class", "cardtxton");
};

<%// 빠른 추가%>
function saveQuic(){
	if (validator.validate('setRegForm') && true/*confirmMsg("cm.cfrm.save")*/ ) {
		var $form = $('#setRegForm');
		$form.attr('method','post');
		$form.attr('action','./${transPage}.do?menuId=${menuId}');
		$form.attr('enctype','multipart/form-data');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
};
///////////////////////////////////////////

<% // [하단버튼:배열] %>
function getRightBtnList(){
	var $area = $("#rightBtnArea");
	return $area.find('ul')[0].outerHTML;
}
function initialSearch(obj, va){
	activeIcon(obj);
	location.href = './${listPage }${pageSuffix }.do?${nonInitialParams}&schInitial='+encodeURIComponent(va);
}

<%// 등록 수정 %>
function setBc(){
	location.href = './${setPage}${pageSuffix}.do?${paramsForList}${agntParam }&typ=${param.typ}';
}
<%// 보기 형식 변경 %>
function listTypChn(typ){
	<c:if test="${frmYn eq 'Y' }">parent.setSrchTyp(typ);</c:if>
	location.href='./${listPage }${pageSuffix }.do?${paramsForList }${agntParam }&typ='+typ;
}
<%// 저장, 삭제시 리로드 %>
function reloadBcFrm(url, dialogId){
	//팝업 닫기
	if(dialogId != undefined && dialogId != null && dialogId !='') {
		if(dialogId == 'all') parent.dialog.closeAll();
		else parent.dialog.close(dialogId);
	}
	if(url != undefined && url != null) location.replace(url);
	else location.replace(location.href);
};

<%// [POPUP] 조직 사용자 추가 - 멀티 %>
function addBcMultiPop(fldId){
	var data = [];<%// data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	var param={data:data, multi:true, mode:'search'};
	<c:if test="${!empty globalOrgChartEnable && globalOrgChartEnable==true}">
		param.global='Y';
	</c:if>
	
	<%// option : data, multi, titleId %>
	parent.searchUserPop(param, function(arr){
		if(arr!=null){
			var addList = [];
			arr.each(function(index, userVo){
				addList.push(userVo.userUid);
			});
			callAjax('./transBcUserListAjx.do?menuId=${menuId}${agntParam}', {fldId: fldId, addList: addList}, function(data){
				if(data.message!=null){
					alert(data.message);
				}
				if(data.result=='ok'){
					location.reload();
				}
			});
		}
	});
};
$(document).ready(function() {
setUniformCSS();
<c:if test="${pageSuffix eq 'Frm'}">parent.applyDocBtn();</c:if>
});
</script>
<c:if test="${empty pageSuffix }">
<% // 검색영역 %>
<div class="listarea" id="searchArea">
	<form name="searchForm" id="searchForm" action="./${listPage }${pageSuffix }.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="typ" value="${param.typ}" />
	<u:input type="hidden" id="schInitial" value="${param.schInitial}" />
	<u:input type="hidden" id="listPage" value="${listPage}" />
	
	<!-- 검색 페이지 -->
	<jsp:include page="/WEB-INF/jsp/wb/listBcSearch.jsp" />
	</form>
</div>
<u:blank />
</c:if>
<u:set var="style" test="${pageSuffix eq 'Frm' }" value="style='padding:10px;'" elseValue="style='padding-top:10px;'"/>
<div ${style }>
<c:if test="${listPage ne 'listAllBc' }">
	<div class="front notPrint">
		<c:if test="${_lang eq 'ko'}">
			<div class="front_left">
				<table border="0" cellpadding="0" cellspacing="0">
					<tbody>
						<tr>
							<td style="float:left; padding:2px 0 0 3px; margin:0 0 0 -3px;">
								<c:set var="initialList" value="전체,ㄱ,ㄴ,ㄷ,ㄹ,ㅁ,ㅂ,ㅅ,ㅇ,ㅈ,ㅊ,ㅋ,ㅌ,ㅍ,ㅎ,A~Z,123"/>
								<div class="cardarea" id="iconArea" style="float:left; padding:4px 0 0 3px; margin:0 0 0 -3px;">
									<dl>
									<c:forTokens var="initialList" items="${initialList }" delims="," varStatus="status">
									<dd id="iconArea_sub" class="${(status.count == 1 && empty param.schInitial) || initialList == param.schInitial  ? 'cardtxton' : 'cardtxt'}"><a href="javascript:;" onclick="initialSearch(this, '${initialList == '전체' ? '' : initialList}');">${initialList }</a></dd>
									</c:forTokens>
									</dl>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</c:if>
		<c:if test="${_lang ne 'ko'}">
			<div class="front_left">
				<table border="0" cellpadding="0" cellspacing="0">
					<tbody>
						<tr>
							<td style="float:left; padding:2px 0 0 3px; margin:0 0 0 -3px;">
								<c:set var="initialList" value="ALL,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,123"/>
								<div class="cardarea" id="iconArea" style="float:left; padding:4px 0 0 3px; margin:0 0 0 -3px;">
									<dl>
									<c:forTokens var="initialList" items="${initialList }" delims="," varStatus="status">
									<dd id="iconArea_sub" class="${(status.count == 1 && empty param.schInitial) || initialList == param.schInitial  ? 'cardtxton' : 'cardtxt'}"><a href="javascript:;" onclick="initialSearch(this, '${initialList == 'ALL' ? '' : initialList}');">${initialList }</a></dd>
									</c:forTokens>
									</dl>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</c:if>
		<div class="front_right">
			<table border="0" cellpadding="0" cellspacing="0">
				<tbody>
					<tr>
						<td class="frontbtn">
							<c:choose>
								<c:when test="${param.typ eq 'C' || ( empty wbBcUserScrnSetupRVo && empty param.typ )|| ( empty param.typ && wbBcUserScrnSetupRVo.lstTypCd eq 'BC' )}"><u:buttonS href="javascript:listTypChn('L');" titleId="wb.btn.txtView" alt="리스트로보기" /></c:when>
								<c:otherwise><u:buttonS href="javascript:listTypChn('C');" titleId="wb.btn.bcView" alt="명함으로보기" /></c:otherwise>
							</c:choose>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</c:if>
<% // 목록1 %>
<c:choose>
<c:when test="${listPage eq 'listAllBc' || param.typ eq 'L' || ( empty param.typ && wbBcUserScrnSetupRVo.lstTypCd eq 'LIST' )}">
	<div id="bcListArea" class="listarea">
		<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
		<tr id="headerTr">
			<td width="4%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('bcListArea', this.checked);" value=""/></td>
			<c:forEach	items="${listPage eq 'listAllBc' ? allVoList : wbBcUserLstSetupRVoList}" var="wbBcUserLstSetupRVo" varStatus="status">
				<td width="${wbBcUserLstSetupRVo.wdthPerc}" class="head_ct"><u:msg titleId="${wbBcUserLstSetupRVo.msgId }" alt="이름" /></td>
			</c:forEach>
			<c:if test="${listPage eq 'listAllBc' }">
				<td width="10%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
				<td width="10%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
			</c:if>
		</tr>
		<c:choose>
			<c:when test="${!empty wbBcBMapList}">
				<c:forEach var="wbBcBMap" items="${wbBcBMapList}" varStatus="status">
					<c:set var="wbBcBMap" value="${wbBcBMap}" scope="request" />
					<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
						<td class="bodybg_ct"><u:checkbox name="bcCheck" value="${wbBcBMap.bcId }" checked="false" /><u:input type="hidden" name="bcId" value="${wbBcBMap.bcId }"/></td>
						<c:forEach	items="${listPage eq 'listAllBc' ? allVoList : wbBcUserLstSetupRVoList}" var="wbBcUserLstSetupRVo" varStatus="status">
							<td class="body_lt" align="${wbBcUserLstSetupRVo.alnVa}">
								<c:choose>
									<c:when test="${wbBcUserLstSetupRVo.atrbId eq 'bcNm' }"><div class="ellipsis" title="<u:convertMap srcId="wbBcBMap" attId="${wbBcUserLstSetupRVo.atrbId}" type="html" />"><a href="javascript:viewBc('${wbBcBMap.bcId }');"><u:convertMap srcId="wbBcBMap" attId="${wbBcUserLstSetupRVo.atrbId}" type="html" /></a></div></c:when>
									<c:when test="${wbBcUserLstSetupRVo.atrbId eq 'email' }">
										<div class="ellipsis" title="<u:convertMap srcId="wbBcBMap" attId="${wbBcUserLstSetupRVo.atrbId}" type="html" />">
										<a href="javascript:parent.mailToPop('<u:convertMap srcId="wbBcBMap" attId="${wbBcUserLstSetupRVo.atrbId}" type="script" />')" title="<u:msg titleId='or.jsp.viewUserPop.mailToPop' />">
											<u:convertMap srcId="wbBcBMap" attId="${wbBcUserLstSetupRVo.atrbId}" type="html" />
										</a>
										</div>
									</c:when>
									<c:otherwise><div class="ellipsis" title="<u:convertMap srcId="wbBcBMap" attId="${wbBcUserLstSetupRVo.atrbId}" type="html" />"><u:convertMap srcId="wbBcBMap" attId="${wbBcUserLstSetupRVo.atrbId}" type="html" /></div></c:otherwise>
								</c:choose>
							</td>
						</c:forEach>
						<c:if test="${listPage eq 'listAllBc' }">
							<td class="bodybg_ct"><a href="javascript:viewUserPop('${wbBcBMap.regrUid}');"><div class="ellipsis" title="${wbBcBMap.regrNm }" type="html" />${wbBcBMap.regrNm }</div></a></td>
							<td class="bodybg_ct">${wbBcBMap.regDt }</td>
						</c:if>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<td class="nodata" colspan="${listPage eq 'listAllBc' ? fn:length(allVoList) +3 : fn:length(wbBcUserLstSetupRVoList) +1}"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
				</tr>
			</c:otherwise>
		</c:choose>
		</table>
	</div>
</c:when>
<c:otherwise>
<% // 목록2 %>
<c:choose>
	<c:when test="${!empty wbBcBMapList}">
		<div id="bcListArea" style="position:relative; width:100%; height:450px; background:#ffffff; border:1px solid #bfc8d2; overflow-y:auto; overflow-x:auto;">
			<c:forEach var="wbBcBMap" items="${wbBcBMapList}" varStatus="status">
				<c:set var="wbBcBMap" value="${wbBcBMap}" scope="request" />
				<div class="cardbox" style="width:${frmYn eq 'Y' ? 47.5 : 31.7}%;">
					<div class="ptltit">
						<dl>
						<dd class="title" id="aaa"><u:checkbox name="bcCheck" value="${wbBcBMap.bcId }" checked="false" /><u:input type="hidden" name="bcId" value="${wbBcBMap.bcId }"/><a href="javascript:viewBc('${wbBcBMap.bcId }');">${wbBcBMap.bcNm }</a>
						<c:if test="${wbBcBMap.publTypCd eq 'priv' }"><u:icon type="lock" /></c:if>
						</dd>
						</dl>
					</div>
					<div class="ptlbody">
						<table class="ptltable" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed;">														
							<c:forEach	items="${wbBcUserLstSetupRVoList}" var="wbBcUserLstSetupRVo" varStatus="status">
								<c:if test="${wbBcUserLstSetupRVo.dispYn eq 'Y'}">
									<c:if test="${status.count > 1 }"><tr><td colspan="2" class="line"></td></tr></c:if>
									<tr>
										<td width="30%" class="head_lt" ><c:if test="${( wbBcUserLstSetupRVo.atrbId eq 'compPhon' || wbBcUserLstSetupRVo.atrbId eq 'homePhon' || wbBcUserLstSetupRVo.atrbId eq 'mbno') && wbBcBMap.dftCntcTypCd eq wbBcUserLstSetupRVo.atrbId }"><u:mandatory /></c:if><u:msg titleId="${wbBcUserLstSetupRVo.msgId }"/></td>
										<td class="body_lt">
											<c:choose>
												<c:when test="${wbBcUserLstSetupRVo.atrbId eq 'email' }">
													<div class="ellipsis" title="<u:convertMap srcId="wbBcBMap" attId="${wbBcUserLstSetupRVo.atrbId}" type="html" />">
													<a href="javascript:parent.mailToPop('<u:convertMap srcId="wbBcBMap" attId="${wbBcUserLstSetupRVo.atrbId}" type="script" />')" title="<u:msg titleId='or.jsp.viewUserPop.mailToPop' />">
														<u:convertMap srcId="wbBcBMap" attId="${wbBcUserLstSetupRVo.atrbId}" type="html" />
													</a>
													</div>
												</c:when>
												<c:otherwise><div class="ellipsis" title="<u:convertMap srcId="wbBcBMap" attId="${wbBcUserLstSetupRVo.atrbId}" type="html" />"><u:convertMap srcId="wbBcBMap" attId="${wbBcUserLstSetupRVo.atrbId}" type="html" /></div></c:otherwise>
											</c:choose>
										</td>
									</tr>
								</c:if>
							</c:forEach>
						</table>
					</div>
				</div>
			</c:forEach>
		</div>
	</c:when>
	<c:otherwise>
		<u:listArea id="listArea">
			<tr>
			<td class="nodata" ><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
		</u:listArea>
	</c:otherwise>
</c:choose>
</c:otherwise>
</c:choose>
</div>
<u:blank />
<c:choose>
<c:when test="${frmYn eq 'Y' }"><div style="padding:10px;"><u:pagination /></div></c:when>
<c:otherwise><u:pagination /></c:otherwise>
</c:choose>

<u:blank />
<u:set var="rightBtnDisplay" test="${frmYn eq 'Y' }" value="display:none;" elseValue=""/>
<% // 하단 버튼 %>
<u:buttonArea id="rightBtnArea" style="${rightBtnDisplay }">
	<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" auth="R" />
	<u:button titleId="cm.btn.excelDown" alt="엑셀다운" onclick="excelDownFile();" auth="R"/>
	
	<c:if test="${listPage ne 'listAllBc' && mailEnable == 'Y'}">
		<u:button titleId="cm.btn.emailSend" alt="이메일발송" href="javascript:setEmailSend();" auth="R"/>
	</c:if>
	<c:if test="${listPage eq 'listBc' || ( listPage eq 'listAgntBc' && wbBcAgntAdmBVo.authCd eq 'RW' ) || listPage eq 'listAllBc'}">
		<u:button titleId="cm.btn.send" alt="보내기" href="javascript:copyBcToUser();" auth="${listPage eq 'listAllBc' ? 'A' : 'W'}" />
		<u:button titleId="cm.btn.add" alt="추가" href="javascript:addBcMultiPop();" auth="W" />
		<c:if test="${listPage ne 'listAllBc' }"><u:button titleId="cm.btn.reg" alt="등록" href="javascript:setBc();" auth="W" /></c:if>
		<u:button titleId="cm.btn.del" alt="삭제" href="javascript:fnDelete();" auth="${listPage eq 'listAllBc' ? 'A' : 'W'}" />
	</c:if>
	<c:if test="${listPage eq 'listPubBc'}">
		<u:button titleId="cm.btn.add" alt="추가" href="javascript:addBcMultiPop();" auth="W" />
		<u:button titleId="cm.btn.reg" alt="등록" href="javascript:setBc();" auth="W" />
		<u:button titleId="cm.btn.del" alt="삭제" href="javascript:fnDelete();" auth="W" />
	</c:if>
	<c:if test="${listPage eq 'listBc' || listPage eq 'listPubBc'}">
		<%-- <u:button titleId="wb.btn.bcCopy" alt="명함복사" onclick="copyBc();" auth="A" /> --%>
		<u:button titleId="cm.btn.move" alt="명함이동" onclick="moveBc();" auth="W" />
	</c:if>
</u:buttonArea>

<form id="deleteForm" name="deleteForm" action="./${transDelPage }${pageSuffix }.do">
	<u:input type="hidden" name="menuId" value="${menuId}" />
	<u:input type="hidden" name="typ" value="${param.typ}" />
	<u:input type="hidden" name="bcId"  id="delList"/>
	<c:if test="${!empty schBcRegrUid}">
		<u:input type="hidden" id="schBcRegrUid" value="${schBcRegrUid }"/>
	</c:if>
	<u:input type="hidden" id="listPage" value="./${listPage}${pageSuffix }.do?${nonPageParams }${agntParam }" />
</form>
<form id="copyBcForm" name="copyBcForm" action="./${listPage eq 'listAllBc' ? 'transAllBcCopy' : 'transBcCopy'}.do">
	<u:input type="hidden" name="menuId" value="${menuId}" />
	<u:input type="hidden" name="typ" value="${param.typ}" />
	<u:input type="hidden" name="copyBcId"  id="copyBcList"/>
	<u:input type="hidden" name="copyRegrUid"  id="copyUserList"/>
	<c:if test="${!empty schBcRegrUid}">
		<u:input type="hidden" id="schBcRegrUid" name="schBcRegrUid" value="${schBcRegrUid }"/>
	</c:if>
</form>
<u:blank />
<form id="excelForm">
	<c:forEach items="${paramEntryList}" var="entry" varStatus="status">
		<u:input type="hidden" id="${entry.key}" value="${entry.value}" />
	</c:forEach>
	<u:input type="hidden" id="listPage" value="${listPage}" />
</form>


