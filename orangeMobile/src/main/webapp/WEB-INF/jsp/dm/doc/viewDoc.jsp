<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="nonPageParams" excludes="docId,pageNo,docGrpId,tgtId,noCache"/>
<u:params var="viewPageParams" excludes="docId,docPid,docGrpId,noCache"/>
<u:set var="includeParams" test="${!empty docPid}" value="&docId=${docPid }" elseValue="&docId=${dmDocLVoMap.docId }"/>
<c:set var="exKeys" value="view,update,owner,disuse,saveDisc,keepDdln,seculCd,recycle,fld,cls,docNoMod,setSubDoc,bumk,download,print,email,openApv,version,print"/><!-- 하단 버튼 제외 key -->
<u:set var="adminYn" test="${!empty isAdmin && isAdmin == true }" value="Y" elseValue="N"/>
<u:set var="paramStorIdQueryString" test="${!empty paramStorId }" value="&paramStorId=${paramStorId }" elseValue=""/>
<script type="text/javascript">
//<![CDATA[
<% // 파일 다운로드 %>
function downFile(id,dispNm) {
	var ids = [];
	ids.push(id);
	var $form = $('<form>', {
			'method':'post',
			'action':'/dm/downFile.do',
			'target':$m.browser.mobile && $m.browser.safari ? '' : 'dataframe'
		}).append($('<input>', {
			'name':'menuId',
			'value':'${menuId}',
			'type':'hidden'
		})).append($('<input>', {
			'name':'paramStorId',
			'value':'${paramStorId}',
			'type':'hidden'
		})).append($('<input>', {
			'name':'docGrpId',
			'value':'${dmDocLVoMap.docGrpId}',
			'type':'hidden'
		})).append($('<input>', {
			'name':'fileIds',
			'value':ids,
			'type':'hidden'
		}));
	
	//if($m.browser.naver || $m.browser.daum){
	//	$form.append($('<input>', {'name':'fwd','value':$form.attr('action'),'type':'hidden'}));
	//	$form.attr('action', '/cm/download/dm/'+encodeURI(dispNm));
	//}
	
	$(top.document.body).append($form);
	$m.secu.set();
	$form.submit();
	$form.remove();
}
<c:if test="${viewYn eq 'Y'}">
<% // 문서뷰어 %>
function viewAttchFile(id) {
	var url = "/dm/attachViewSub.do?menuId=${menuId}&paramStorId=${paramStorId}&docGrpId=${dmDocLVoMap.docGrpId}";
	url+='&fileIds='+id;
	openViewAttchFile(url);
	//$m.nav.next(null, url);
}
</c:if>
<%// [버튼] 열람취소 - 삭제 %>
function openCancel(){
	var arrs = '${dmDocLVoMap.docGrpId}';
	if(arrs == '') return;
	var arrs2 = '${dmDocLVoMap.tgtId}';
	if(arrs2 == '') return;
	$m.ajax('/dm/doc/transOpenCancelAjx.do?menuId=${menuId}', {docGrpId:arrs,tgtId:arrs2}, function(data) {
		if (data.message != null) {
			$m.dialog.alert(data.message);
		}
		if (data.result == 'ok') {
			$m.nav.curr(null, $('#listPage').val());
		}
	});
}<%// [저장] 열람요청승인 - 개별 %>
function saveOpenApv(param){
	var arrs = '${dmDocLVoMap.docGrpId}';
	if(arrs == '') return;
	var arrs2 = '${param.tgtId}';
	if(arrs2 == '') return;
	param.put('docGrpId',arrs);
	param.put('tgtId',arrs2);
	$m.ajax('/dm/doc/transOpenApvAjx.do?menuId=${menuId}', param, function(data) {
		if (data.message != null) {
			$m.dialog.alert(data.message);
		}
		if (data.result == 'ok') {
			$m.nav.curr(null, $('#listPage').val());
		}
	});
}<%// [버튼] 열람승인 %>
function openApv(){
	$m.dialog.open({
		id:'openApvDialog',
		title:'<u:msg titleId="dm.jsp.dtlView.approved.title" alt="열람요청승인" />',
		url:'/dm/doc/setOpenApvPop.do?menuId=${menuId}',
	});
}<%// 문서열람요청 %>
function saveRequest(param){
	$m.ajax('/dm/doc/transViewDocReqAjx.do?menuId=${menuId}', param, function(data) {
		if (data.message != null) {
			$m.dialog.alert(data.message);
		}
		if (data.result == 'ok') {
			$m.dialog.close('viewDocReqCfrmDialog'); 
		}
	});
}<%// [팝업] 문서이력 %>
function docHst(srchCd){
	var url = '/dm/doc/listDocTaskSub.do?menuId=${menuId}&docId=${dmDocLVoMap.docId}&dialog=docHstPop${paramStorIdQueryString}';
	if(srchCd != undefined) url+='&single=Y&srchCd=view';
	$m.nav.next(null, url);
};<%// [팝업] 보내기 %>
function send(){
	var url = '/dm/doc/sendSub.do?docTyp=doc&listPage=${listPage}&refTyp=${dmDocLVoMap.refTyp}&dialog=sendDialog&${params}';
	$m.nav.next(null,url);
};<%// [팝업] 이메일 %>
function email(){
	emailSendPop({docId:'${param.docId}',docTyp:'${param.docTyp }'},'${menuId }');
};<%// [팝업] 이동 %>
function move(){
	var url = '/dm/doc/sendSub.do?mode=move&listPage=${listPage}&${params}';
	$m.nav.next(null,url);
};<% //즐겨찾기 목록 %>
function getBumkList(){
	var arrs = [];
	<c:forEach var="bumkVo" items="${dmBumkBVoList }" varStatus="status">
	arrs.push({cd:'${bumkVo.bumkId}', nm:'${bumkVo.bumkNm}'});
    </c:forEach>
	return arrs; 
};<% // [즐겨찾기 저장] %>
function saveBumk(bumkId, mode){
	if(mode == undefined || mode == null) mode = 'insert';
	$m.ajax('/dm/doc/transBumkDocAjx.do?menuId=${menuId}', {bumkId:bumkId,mode:mode,regCat:'D',catVa:'${dmDocLVoMap.docGrpId}'}, function(data) {
		if (data.message != null) {
			$m.dialog.alert(data.message);
		}
		if (data.result == 'ok') {
			if(mode == 'del') $m.nav.prev(null, true);
			else $m.dialog.close('selectBumkPop');
		}
	});
}<% // [하단버튼:즐겨찾기] %>
function bumkDoc(){
	var bumkList = getBumkList();
	if(bumkList.length==0){
		$m.msg.alertMsg('dm.msg.empty.bumkList');<% //dm.msg.empty.bumkList=즐겨찾기가 없습니다.%>
		return;
	}
	$m.dialog.openSelect({id:'selectBumkPop', cdList:bumkList, selected:''}, function(selObj){
		if(selObj!=null){
			saveBumk(selObj.cd);
		}
	});
}<% // [하단버튼:뒤로|취소] %>
function cancelDoc(){
	history.go(-1);
}<% // [하단버튼:제외키] %>
function getExKeys(){
	var exKeys = '${exKeys}'.split(',');
	var returnAttrs = [];
	for(var i=0;i<exKeys.length;i++){
		returnAttrs.push(exKeys[i]);
	}
	return returnAttrs;
}<% // [하단버튼:배열] %>
function getRightBtnList(){
	var $area = $("#rightBtnArea");
	return $area.find('ul')[0].outerHTML;
}<% // [하단버튼:복원] %>
function recovery() {
	<% // dm.cfrm.recovery=복원하시겠습니까? %>
	delDocTrans({docGrpId:$('#docGrpId').val(),statCd:'C',msgCode:'dm.msg.recovery.success'},'dm.cfrm.recovery');
}<% // [하단버튼:반려] - 팝업%>
function saveDiscPop() {
	$m.dialog.open({
		id:'saveDiscDialog',
		title:'<u:msg titleId="dm.cols.discDoc.rjt" alt="심의문서(반려)" />',
		url:'/dm/doc/saveDiscPop.do?menuId=${menuId}',
	});
}<% // [하단버튼:반려] - 팝업%>
function setDiscInfo(arrs) {
	var $form = $("#setForm");
	$.each(arrs,function(index,vo){
		$form.appendHidden({name:vo.name,value:vo.value});
	});	
	saveDisc('R');
}<% // [하단버튼:승인,반려] %>
function saveDisc(discStatCd) {
	if(discStatCd == null) return;
	var $form = $("#setForm");
	if(discStatCd == 'R'){
		if($('#rjtOpin').val() == ''){
			$m.msg.alertMsg('cm.input.check.mandatory',['<u:msg titleId="cols.rjtOpin" alt="반려의견" />']);
			return;
		}
		$form.find("#rjtOpin").remove();
		$form.appendHidden({name:'rjtOpin',value:$('#rjtOpin').val()});
	}
	$m.msg.confirmMsg("cm.cfrm.save",null,function(result){
		if(!result) return;
		$form.find("#statCd").remove();
		$form.appendHidden({name:'statCd',value:discStatCd});
		$form.attr('method','post');
		$form.attr('action','/dm/doc/transDiscDocPost.do?menuId=${menuId}');
		$m.nav.post($form);
		
	});
};<% // [하단버튼:목록] %>
function listDoc(){
	$m.nav.prev(null, $('#listPage').val());
}<% // [목록:제목] 상세 조회 %>
function viewDoc(id,docGrpId,tgtId) {
	$m.ajax('/dm/doc/getViewOptAjx.do?menuId=${menuId}${paramStorIdQueryString}', {docGrpId:docGrpId,viewPage:'${viewPage}'}, function(data) {
		if(data.message != null){
			$m.dialog.alert(data.message);
		}
		if ((data.popYn == null || data.popYn == 'N') && data.messageCd != null) {
			$m.msg.alertMsg(data.messageCd);
		}
		if (data.popYn != null && data.popYn == 'Y' && data.messageCd != null) {
			$m.msg.confirmMsg(data.messageCd, null, function(result){
				if(result){
					$m.dialog.open({
						id:'viewDocReqCfrmDialog',
						title:'<u:msg titleId="dm.jsp.dtlView.request.title" alt="문서열람요청" />',
						url:"/dm/doc/viewDocReqCfrmPop.do?menuId=${menuId}&docGrpId="+docGrpId,
					});
				}
			});
		}
		if (data.result == 'ok') {
			var url = '/dm/doc/${viewPage}.do?${paramsForList }&docId=' + id;
			$m.nav.curr(null, url);
		}
	});
}<% // [하단버튼:수정] %>
function setDoc(){
	$m.nav.next(null,'/dm/doc/${setPage}.do?${paramsForList }&docId=${param.docId }');
}<% // [하단버튼:하위문서등록] %>
function setSubDoc(){
	$m.nav.next(null,'/dm/doc/${setPage}.do?${paramsForList }&docPid=${dmDocLVoMap.docGrpId }');
}<%// 상태코드별 메세지[복원,폐기,삭제] %>
function getMsgCd(statCd){
	var arr = [];
	if(statCd == 'C') {
		arr.push("dm.cfrm.recovery");
		arr.push("dm.msg.recovery.success");
		return arr;
	}else if(statCd == 'F'){
		arr.push("dm.cfrm.disuse");
		arr.push("");
		return arr;
	}
	arr.push("cm.cfrm.del");
	arr.push("");
	return arr;
}<% // [하단버튼:삭제] %>
function delDocTrans(param, msgCd) {
	if('${adminYn}' == 'Y' && param.statCd == 'F' && param.docGrpId != undefined){
		delDocList(param.docGrpId,param.statCd);
	}else{
		if(msgCd == null) msgCd = 'cm.cfrm.del';<% // cm.cfrm.del=삭제하시겠습니까? %>
		$m.msg.confirmMsg(msgCd,null,function(result){
			if(!result) return;
			$m.ajax('/dm/doc/transDocDelAjx.do?menuId=${menuId}${paramStorIdQueryString}', param, function(data) {
				if (data.message != null) {
					$m.dialog.alert(data.message);
				}
				if (data.result == 'ok') {
					$m.nav.curr(null, $('#listPage').val());					
					/* <c:if test="${empty refDocId && empty docPid}">$m.nav.curr(null, $('#listPage').val());</c:if>
					<c:if test="${empty refDocId && !empty docPid}">$m.nav.curr(null, $('#viewPage').val());</c:if>
					<c:if test="${!empty refDocId}">$m.nav.curr(null, $('#viewRefPage').val());</c:if> */
				}
			});
		})
	}
	
}<% // [하단버튼:삭제] %>
function delDocList(arrs, statCd) {
	if(arrs == null) return;
	var msgCd = getMsgCd(statCd);
	$m.ajax('/dm/doc/transDocDelListAjx.do?menuId=${menuId}${paramStorIdQueryString}', {docGrpId:arrs,statCd:statCd,msgCode:msgCd[1]}, function(data) {
		if (data.message != null) {
			$m.dialog.alert(data.message);
		}
		if (data.result == 'ok') {
			$m.nav.curr(null, $('#listPage').val());
			/* <c:if test="${empty refDocId && empty docPid}">$m.nav.curr(null, $('#listPage').val());</c:if>
			<c:if test="${empty refDocId && !empty docPid}">$m.nav.curr(null, $('#viewPage').val());</c:if>
			<c:if test="${!empty refDocId}">$m.nav.curr(null, $('#viewRefPage').val());</c:if> */
		}
	});
}<% // [하단버튼:삭제] 문서%>
function delDoc(statCd) {
	delDocTrans({docId:$('#docId').val(),statCd:statCd},null);
}<% // [하단버튼:삭제] 문서그룹전체%>
function delDocGrp(statCd) {
	<% // dm.cfrm.del.with=하위 버전이 있을 경우 함께 삭제됩니다\n그래도 하시겠습니까? %>
	delDocTrans({docGrpId:$('#docGrpId').val(),statCd:statCd},'dm.cfrm.del.with');
}<%// 탭 클릭 - 문서버전 %>
function toggleTabBtn(seq){
	$m.nav.curr(null, "/dm/doc/${viewPage}.do?${paramsForList }&docId="+seq);
}<%// 팝업 - url형 문서보기 %>
function refUrlPop(url, title){
	$m.nav.next(null, url.replace('viewApDocPop','viewApDocSub'));
}<%// 저장, 삭제시 리로드 %>
function reloadDocFrm(url, dialogId){
	//팝업 닫기
	if(dialogId != undefined && dialogId != null && dialogId !='') {
		if(dialogId == 'all') $m.dialog.closeAll();
		else $m.dialog.close(dialogId);
	}
	
	if(url != undefined && url != null) $m.nav.curr(null, url);
	else $m.nav.curr(null, location.href);
};
<%
//Select Option 클릭 %>
function setSelOptions(codeNm, code, value){
	$("#"+codeNm+"Container #selectView span").text(value);
	$("#"+codeNm+"Container #"+codeNm+"Open").hide();
}
$(document).ready(function() {
	<%// 본문의 넓이를 맞춤 %>
	$layout.adjustBodyHtml('bodyHtmlArea');
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('tabview');
	//window.setTimeout("$('#viewApDocBtn').trigger('click')", 600);
});
//]]>
</script>


<!--section S-->
<section>
	<c:set var="voMap" value="${dmDocLVoMap }" scope="request"/>
	<m:input type="hidden" name="menuId" value="${menuId}" />
	<m:input type="hidden" id="listPage" value="/dm/doc/${listPage}.do?${nonPageParams}" />
	<m:input type="hidden" id="viewPage" value="/dm/doc/${viewPage}.do?${viewPageParams}${includeParams }" />
	<m:input type="hidden" id="viewRefPage" value="/dm/doc/${viewPage}.do?${paramsForList}&docId=${refDocId }" />
	<m:input type="hidden" id="docId" value="${dmDocLVoMap.docId }" />
	<m:input type="hidden" id="docGrpId" value="${dmDocLVoMap.docGrpId }" />
       <!--btnarea S-->
       <div class="btnarea" id="btnArea">
           <div class="size">
           <dl>
               <c:if test="${dmDocLVoMap.dftYn eq 'Y' }">
				<c:if test="${!empty authMap.update}">
					<dd class="btn" onclick="setSubDoc();"><u:msg titleId="dm.cols.setSubDoc" alt="하위문서작성" /></dd
					><dd class="btn" onclick="setDoc();"><u:msg titleId="cm.btn.mod" alt="수정" /></dd>
				</c:if>
				<c:forEach items="${authMap}" var="cdVo" varStatus="status">
					<c:if test="${!fn:contains(exKeys,cdVo.key) }">
						<u:msg var="btnTitle" titleId="dm.cols.auth.${cdVo.value }"/>
						<u:secu auth="W"><dd class="btn" onclick="${cdVo.key }();"><u:msg titleId="dm.cols.auth.${cdVo.value }" alt="${btnTitle }" /></dd></u:secu>
					</c:if>
				</c:forEach>
				<u:secu auth="W">
				<c:if test="${!empty authMap.bumk}">
					<dd class="btn" onclick="bumkDoc();"><u:msg titleId="dm.cols.bumk" alt="즐겨찾기" /></dd>
				</c:if>
				<c:if test="${!empty param.bumkId && isBumkSave == true}">
					<dd class="btn" onclick="saveBumk('${param.bumkId }','del');"><u:msg titleId="dm.btn.delete.bumk" alt="즐겨찾기삭제" /></dd>
				</c:if>
				<c:if test="${!empty authMap.saveDisc}">
					<dd class="btn" onclick="saveDisc('A');"><u:msg titleId="cm.btn.apvd" alt="승인" /></dd>
					<dd class="btn" onclick="saveDiscPop();"><u:msg titleId="cm.btn.rjt" alt="반려" /></dd>
				</c:if>
				<c:if test="${!empty authMap.recycle}">
					<c:if test="${fn:length(dmDocVerLVoList) > 1 }"><dd class="btn" onclick="delDoc('F');"><u:msg titleId="dm.cols.auth.verDel" alt="버전삭제" /></dd></c:if>
					<c:if test="${dmDocLVoMap.statCd eq 'C' && fn:length(dmDocVerLVoList) == 1}"><dd class="btn" onclick="delDocGrp('D');"><u:msg titleId="dm.cols.auth.recycle" alt="휴지통" /></dd></c:if>
				</c:if>
				<c:if test="${!empty authMap.disuse}">
					<dd class="btn" onclick="delDocGrp('F');"><u:msg titleId="dm.cols.auth.disuse" alt="완전삭제" /></dd>
				</c:if>
				</u:secu>
				<u:secu auth="A">
				<c:if test="${!empty authMap.openApv }">
					<c:if test="${empty dmDocLVoMap.viewReqStatCd || dmDocLVoMap.viewReqStatCd eq 'S'}"><dd class="btn" onclick="openApv();"><u:msg titleId="dm.cols.auth.openApv" alt="열람요청승인" /></dd></c:if>
					<c:if test="${!empty dmDocLVoMap.viewReqStatCd && dmDocLVoMap.viewReqStatCd eq 'A'}"><dd class="btn" onclick="openCancel();"><u:msg titleId="dm.btn.dtlView.cancel" alt="열람취소" /></dd></c:if>
					<c:if test="${!empty dmDocLVoMap.viewReqStatCd && dmDocLVoMap.viewReqStatCd eq 'R'}"><dd class="btn" onclick="openCancel();"><u:msg titleId="cm.btn.del" alt="삭제" /></dd></c:if>
				</c:if>
				</u:secu>
				<c:if test="${dmDocLVoMap.dftYn eq 'Y' && dmDocLVoMap.subYn eq 'Y'}">
					<dd class="btn" onclick="history.go(-1);"><u:msg titleId="cm.btn.back" alt="뒤로" /></dd>
				</c:if>
				<c:if test="${dmDocLVoMap.dftYn eq 'Y' && dmDocLVoMap.subYn ne 'Y'}">
					<dd class="btn" onclick="listDoc();"><u:msg titleId="cm.btn.list" alt="목록" /></dd>
				</c:if>
			</c:if>
			<c:if test="${dmDocLVoMap.dftYn ne 'Y' }">
				<u:secu auth="W">
				<c:if test="${!empty authMap.recycle && fn:length(dmDocVerLVoList) > 1}">
					<dd class="btn" onclick="delDoc('F');"><u:msg titleId="dm.cols.auth.verDel" alt="버전삭제" /></dd>
				</c:if>
				</u:secu>
				<dd class="btn" onclick="history.go(-1);"><u:msg titleId="cm.btn.back" alt="뒤로" /></dd>
			</c:if>
           <dd class="open" id="moreBtn" style="display:none" onclick="$layout.toggleBtns()"></dd>
        </dl>
      	 </div>
       </div>
		<!--titlezone S-->
        <div class="titlezone">
            <div class="titarea">
            <dl>
            <dd class="tit">${dmDocLVoMap.subj}</dd>
            <dd class="name">
            	<u:out value="${dmDocLVoMap.fldNm}" />ㅣ<u:out value="${dmDocLVoMap.regrNm}" />ㅣ<u:out value="${dmDocLVoMap.regDt}" type="longdate" />ㅣ
				<u:out value="${dmDocLVoMap.readCnt}" type="number" />
            </dd>
         	</dl>
            </div>
        </div>
        <!--//titlezone E-->
		
		<!--//btnarea E-->
		<c:if test="${fn:length(dmDocVerLVoList)>1 }">
			<div class="entryzone" >
				<div class="entryarea">
                <dl>
                	<dd class="etr_blank"></dd>
                	<dd class="etr_input">
	                    <div class="etr_ipmany" id="schVerVaContainer">
	                    <dl>
	                    <dd class="etr_se_rt">
	                        <div class="etr_open2" id="schVerVaOpen" style="display:none">
	                            <div class="open_in1">
	                                <div class="open_div">
	                                <dl>
	                                <c:forEach var="verVo" items="${dmDocVerLVoList }" varStatus="status">
	                                	<c:if test="${status.count>1 }"><dd class="line"></dd></c:if>
	                            		<dd class="txt" onclick="toggleTabBtn('${verVo.docId}');" data-code="${verVo.verVa }">${verVo.verVa }</dd>
									</c:forEach>
		                            </dl>
	                                </div>
	                            </div>
	                        </div>
	                        
	                        <div class="select_in1" onclick="$('#schVerVaContainer #schVerVaOpen').toggle();">
	                        <dl>
	                         <dd class="select_txt" id="selectView"><span>${dmDocLVoMap.verVa }</span></dd>
	                        <dd class="select_btn"></dd>
	                        </dl>
	                        </div>
	                    </dd>
	                    </dl>
	                    </div>
                    </dd>
                </dl>
                </div>
			</div>
		</c:if>
		
         <!--tabarea S-->
         <div class="tabarea" id="tabBtnArea">
             <div class="tabsize">
                 <dl>
                 <dd class="tab_on" onclick="$layout.tab.on($(this).attr('id'))" id="body"><u:msg titleId="cols.body" alt="본문" /></dd>
                 <dd class="tab" onclick="$layout.tab.on($(this).attr('id'))" id="detl"><u:msg titleId="cm.btn.detl" alt="상세" /></dd>
                 <c:if test="${!empty fileVoList && itemDispMap.fileCnt.readDispYn eq 'Y' && !empty authMap.download}">
                 	<dd class="tab" onclick="$layout.tab.on($(this).attr('id'))" id="attch"><u:msg titleId="cols.att" alt="첨부" /></dd>
                 </c:if>
				 <c:if test="${dmDocLVoMap.dftYn eq 'Y' && subDocListYn == true && fn:length(subDocList)>1}">
					<dd class="tab" onclick="$layout.tab.on($(this).attr('id'))" id="rel"><u:msg titleId="dm.cols.relDoc" alt="관련문서" />(<u:out value="${fn:length(subDocList)-1}" type="number" />)</dd>
				 </c:if>
				 <c:if test="${dmDocLVoMap.statCd eq 'R' && !empty dmSubmLVo}">
                 	<dd class="tab" onclick="$layout.tab.on($(this).attr('id'))" id="rjtOpin"><u:msg titleId="cols.rjtOpin" alt="반려의견" /></dd>
                 </c:if>
              </dl>
             </div>
			<div class="tab_icol" style="display:none" id="toLeft"></div>
			<div class="tab_icor" style="display:none" id="toRight"></div>
         </div>
         <!--//tabarea E-->
			
		<div id="tabViewArea">
            <!--bodyzone_scroll S-->
            <div class="bodyzone_scroll" id="body">
            	<c:if test="${dmDocLVoMap.statCd eq 'C' && dmDocLVoMap.refTyp eq 'ap' && !empty dmDocLVoMap.refUrl }">
            		<div class="btnarea"><div class="size"><dl
            		><dd class="btn" id="viewApDocBtn" onclick="refUrlPop('${dmDocLVoMap.refUrl}&menuId=${menuId}','${refPopTitle }');"><u:msg titleId="dm.msg.viewDoc.popTitle" alt="[0] 정보" arguments="#dm.cols.send.${dmDocLVoMap.refTyp }" 
            		/></dd></dl></div></div>
            	</c:if>
                <div class="bodyarea">
                <dl>
                <dd class="bodytxt_scroll"><div class="scroll editor" id="bodyHtmlArea">
                	<div id="zoom"><u:clob lobHandler="${lobHandler }"/></div>
                </div>
                </dd>
	            </dl>
                </div>
            </div>
            <!--//bodyzone_scroll E-->

        <!--listtablearea S-->
        <div  class="s_tablearea" id="detl" style="display:none;">
        	<div class="blank30"></div>
            <table class="s_table" style="table-layout:fixed;word-wrap:break-word;">
            <!-- <caption>타이틀</caption> -->
            <colgroup>
                <col width="33%"/>
                <col width=""/>
            </colgroup>
            <tbody>
            	<c:if test="${dmDocLVoMap.statCd eq 'C'}">
            	<c:if test="${itemDispMap.docNo.readDispYn eq 'Y'}">
            	<tr>
            		<th class="shead_lt"><u:msg titleId="dm.cols.docNo" alt="문서번호" /></th>
                    <td class="shead_lt"><u:out value="${dmDocLVoMap.docNo}" /></td>
            	</tr>
            	</c:if>
            	<c:if test="${itemDispMap.verVa.readDispYn eq 'Y'}">
            	<tr>
            		<th class="shead_lt"><u:msg titleId="dm.cols.verNo" alt="버전" /></th>
                    <td class="shead_lt"><u:out value="${dmDocLVoMap.verVa}" /></td>
            	</tr>
            	</c:if>
            	</c:if>
            	<c:if test="${itemDispMap.regrNm.readDispYn eq 'Y'}">
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.regr" alt="등록자" /></th>
                    <td class="sbody_lt"><a href="javascript:$m.user.viewUserPop('${dmDocLVoMap.regrUid}');"><u:out value="${dmDocLVoMap.regrNm}" /></a></td>
                </tr>
                </c:if>
                <c:if test="${itemDispMap.regDt.readDispYn eq 'Y'}">
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.regDt" alt="등록일시" /></th>
                    <td class="shead_lt"><u:out value="${dmDocLVoMap.regDt}" type="longdate" /></td>
                </tr>
                </c:if>
                <c:if test="${itemDispMap.modrNm.readDispYn eq 'Y'}">
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.modr" alt="수정자" /></th>
                    <td class="sbody_lt"><c:if test="${!empty dmDocLVoMap.modrNm }"><a href="javascript:$m.user.viewUserPop('${dmDocLVoMap.modrUid}');"><u:out value="${dmDocLVoMap.modrNm}" /></a></c:if></td>
                </tr>
                </c:if>
                <c:if test="${itemDispMap.modDt.readDispYn eq 'Y'}">
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.modDt" alt="수정일시" /></th>
                    <td class="shead_lt"><u:out value="${dmDocLVoMap.modDt}" type="longdate" /></td>
                </tr>
                </c:if>
                <c:if test="${dmDocLVoMap.statCd eq 'C' && itemDispMap.cmplDt.readDispYn eq 'Y'}">
                <tr>
                    <th class="shead_lt"><u:msg titleId="dm.cols.cmplDt" alt="완료일시" /></th>
                    <td class="shead_lt"><u:out value="${dmDocLVoMap.cmplDt }" type="longdate" /></td>
                </tr>
                </c:if>
                <c:if test="${dmDocLVoMap.statCd eq 'C' && itemDispMap.readCnt.readDispYn eq 'Y'}">
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.readCnt" alt="조회수" /></th>
                    <td class="shead_lt"><c:if test="${!empty authMap.docHst }"><a href="javascript:docHst('view');"><u:out value="${dmDocLVoMap.readCnt }" type="html" /></a></c:if><c:if test="${empty authMap.docHst }"><u:out value="${dmDocLVoMap.readCnt }" type="html" /></c:if></td>
                </tr>
                </c:if>
                <tr>
            		<th class="shead_lt"><u:msg titleId="cols.subj" alt="제목" /></th>
                    <td class="shead_lt" ><u:out value="${dmDocLVoMap.subj}" /></td>
            	</tr>
            	<c:if test="${dmDocLVoMap.dftYn eq 'Y'}">
				<c:if test="${itemDispMap.fldNm.readDispYn eq 'Y' || itemDispMap.clsNm.readDispYn eq 'Y'}">
					<c:if test="${itemDispMap.fldNm.readDispYn eq 'Y' }">
					<tr>
	            		<th class="shead_lt"><u:msg titleId="cols.fld" alt="폴더" /></th>
	                    <td class="shead_lt"><c:if test="${empty dmDocLVoMap.fldNm }"><u:msg titleId="dm.msg.not.save.emptyCls" alt="미분류"/></c:if>${dmDocLVoMap.fldNm}</td>
	            	</tr>
	            	</c:if>
	            	<c:if test="${itemDispMap.clsNm.readDispYn eq 'Y' }">
	            	<tr>
	            		<th class="shead_lt"><u:msg titleId="cols.cls" alt="분류" /></th>
	                    <td class="shead_lt"><c:set var="clsNmTmp" />
						<c:forEach var="clsVo" items="${dmClsBVoList }" varStatus="status">
							<c:set var="clsNmTmp" value="${clsNmTmp}${status.count > 1 ? ',' : '' }${clsVo.clsNm }"/>
						</c:forEach>
						${clsNmTmp }</td>
	            	</tr>
	            	</c:if>
				</c:if>
				</c:if>
            	<tr>
            		<th class="shead_lt"><u:msg titleId="dm.cols.kwd" alt="키워드" /></th>
                    <td class="shead_lt"><c:set var="kwnNms" />
					<c:forEach var="kwdVo" items="${dmKwdLVoList }" varStatus="status"><c:set var="kwnNms" value="${kwnNms }${status.count > 1 ? ',' : ''}${kwdVo.kwdNm }"/></c:forEach>
					${kwnNms}</td>
            	</tr>
            	<c:if test="${itemDispMap.docKeepPrdNm.readDispYn eq 'Y' || itemDispMap.seculNm.readDispYn eq 'Y'}">
            		<c:if test="${itemDispMap.docKeepPrdNm.readDispYn eq 'Y' }">
            			<tr>
		            		<th class="shead_lt"><u:msg titleId="dm.cols.keepPrd" alt="보존연한" /></th>
		                    <td class="shead_lt">${dmDocLVoMap.docKeepPrdNm }<c:if test="${!empty dmDocLVoMap.keepDdlnDt }"> (<u:out value="${dmDocLVoMap.keepDdlnDt }" type="date"/>)</c:if></td>
		            	</tr>
            		</c:if>
            		<c:if test="${itemDispMap.seculNm.readDispYn eq 'Y' }">
            			<tr>
		            		<th class="shead_lt"><u:msg titleId="dm.cols.secul" alt="보안등급" /></th>
		                    <td class="shead_lt">${dmDocLVoMap.seculNm }</td>
		            	</tr>
            		</c:if>
            	</c:if>
            	<c:if test="${itemDispMap.ownrNm.readDispYn eq 'Y'}">
            		<tr>
            		<th class="shead_lt"><u:msg titleId="dm.cols.ownr" alt="소유자" /></th>
                    <td class="shead_lt"><a href="javascript:$m.user.viewUserPop('${dmDocLVoMap.ownrUid}');"><u:out value="${dmDocLVoMap.ownrNm}" /></a></td>
            	</tr>
            	</c:if>
            	<c:if test="${dmDocLVoMap.dftYn eq 'Y' && empty dmDocLVoMap.docId}">
				<tr>
					<td class="shead_lt"><u:msg titleId="dm.cols.bumk" alt="즐겨찾기" /></td>
					<td class="shead_lt" >${dmDocLVoMap.bumkNm }</td>
				</tr>
				<tr>
					<td class="shead_lt"><u:msg titleId="dm.cols.auth.psn" alt="개인폴더" /></td>
					<td class="shead_lt" >${dmDocLVoMap.psnFldNm }</td>
				</tr>
				</c:if>
            	
				<c:if test="${!empty itemDispList }">
					<!-- 확장컬럼 -->
					<c:forEach var="dispVo" items="${itemDispList }" varStatus="status">
						<c:set var="colmVo" value="${dispVo.colmVo}" />
						<c:set var="itemTyp" value="${colmVo.itemTyp}" />
						<c:set var="itemNm" value="${dispVo.atrbId}" />
						<u:convertMap var="docVal" srcId="voMap" attId="${itemNm }" />
						<tr>
							<th class="shead_lt">${colmVo.itemDispNm }</th>
							<td class="shead_lt">
								<c:if test="${itemTyp != 'CODE'}">${docVal }</c:if>
								<c:if test="${itemTyp == 'CODE'}">
									<c:forEach items="${colmVo.cdList}" var="cd" varStatus="status">
										<c:if test="${cd.cdId == docVal}">${cd.rescNm}</c:if>
									</c:forEach>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</c:if>		
            </tbody>
            </table>
        </div>
        <c:if test="${dmDocLVoMap.statCd eq 'R' && !empty dmSubmLVo}">
        <!--//listtablearea E-->
        <div class="entryzone" id="rjtOpin" style="display:none;">
        <div class="blank30"></div>
        <div class="entryarea">
        <dl><dd class="etr_input"><div class="etr_inputin"><input type="text" class="etr_iplt" value="${dmSubmLVo.rjtOpin}" readonly="readonly"/></div></dd></dl>        
		</div>
		</div>
		</c:if>
		<c:if test="${!empty fileVoList && itemDispMap.fileCnt.readDispYn eq 'Y' && !empty authMap.download }">
		<div class="attachzone" id="attch" style="display:none;">
		<div class="blank30"></div>
		<div class="attacharea">
			<c:forEach items="${fileVoList}" var="fileVo" varStatus="status">
				<m:attach fileName="${fileVo.dispNm}" fileKb="${fileVo.fileSize/1024 }" downFnc="downFile('${fileVo.fileId}','${fileVo.dispNm}');" viewFnc="viewAttchFile('${fileVo.fileId}');"/>
			</c:forEach>
		</div>
        </div>  
         </c:if> 	
		 <c:if test="${dmDocLVoMap.dftYn eq 'Y' && subDocListYn == true}">
			<div class="listarea" id="rel" style="display:none;">
				<div class="blank30"></div>
				<article>
	            <c:forEach items="${subDocList}" var="subDocVo" varStatus="status">
	            	<u:set var="currYn" test="${dmDocLVoMap.docId == subDocVo.docId}" value="Y" elseValue="N"/>
	            	<u:set test="${currYn eq 'Y'}" var="listdiv" value="listdivline" elseValue="listdiv"/>
					<div class="${listdiv }">
		          	    <c:if test="${subDocVo.subYn eq 'Y' }"><div class="listcheck_comment"><dl><dd class="comment"></dd></dl></div></c:if>
		                <div class="list${subDocVo.subYn eq 'Y' ? '_comment' : '' }">
						<dl>
							<dd class="tit" <c:if test="${currYn eq 'N' }">onclick="javascript:viewDoc('${subDocVo.docId}','${subDocVo.docGrpId }','${subDocVo.tgtId }');"</c:if>>
								<u:out value="${subDocVo.subj}" maxLength="80" />
							</dd>
							<dd class="name"><a href="javascript:$m.user.viewUserPop('${subDocVo.regrUid}');">${subDocVo.regrNm}</a> / <u:out value="${subDocVo.regDt}" type="longdate" /></dd>
						</dl>
						</div>
					</div>
                 </c:forEach>
	           </article>
	        </div>
		 </c:if>
		</div>
		<% // 이전글 다음글 %>
       	<div class="prevnextarea">
               <div class="prev">
               	<dl>
				<c:if test="${prevVo == null}">
					<dd class="tit"><u:msg titleId="cm.ico.prev" alt="이전글" /></dd>
					<dd class="body"><u:msg titleId="bb.jsp.viewBull.prevNotExists" alt="이전글이 존재하지 않습니다." /></dd>
				</c:if>
				<c:if test="${prevVo != null}">
					<dd class="tit"><u:msg titleId="cm.ico.prev" alt="이전글" /></dd>
					<dd class="body" onclick="viewDoc('${prevVo.docId}','${prevVo.docGrpId}','${prevVo.tgtId}');">
					<u:out value="${prevVo.subj}" maxLength="80" />
					</dd>
				</c:if>
				</dl>
			</div>
			<div class="next">
				<dl>
				<c:if test="${nextVo == null}">
					<dd class="tit"><u:msg titleId="cm.ico.next" alt="다음글" /></dd>
					<dd class="body"><u:msg titleId="bb.jsp.viewBull.nextNotExists" alt="다음글이 존재하지 않습니다." /></dd>
				</c:if>
				<c:if test="${nextVo != null}">
					<dd class="tit"><u:msg titleId="cm.ico.next" alt="다음글" /></dd>
					<dd class="body" onclick="viewDoc('${nextVo.docId}','${nextVo.docGrpId}','${nextVo.tgtId}');">
					<u:out value="${nextVo.subj}" maxLength="80" />
					</dd>
				</c:if>
				</dl>
			</div>
      	 	</div>

		<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
    
</section>
<!--//section E-->
<form id="setForm" name="setForm">
<m:input type="hidden" id="listPage" value="/dm/doc/${listPage}.do?${nonPageParams}" />
<m:input type="hidden" id="viewPage" value="/dm/doc/${viewPage}.do?${params}" />
<m:input type="hidden" id="viewRefPage" value="/dm/doc/${viewPage}.do?${paramsForList}&docId=${refDocId }" />
<m:input type="hidden" id="docId" value="${dmDocLVoMap.docId }" />
<m:input type="hidden" id="docGrpId" value="${dmDocLVoMap.docGrpId }" />
<!-- 저장소ID -->
<c:if test="${!empty paramStorId }">
<m:input type="hidden" id="paramStorId" value="${paramStorId}" />
</c:if>
</form>
