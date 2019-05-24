<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:secu
	auth="W" ><u:set test="${true}" var="writeAuth" value="Y"/></u:secu>
<script type="text/javascript">
//<![CDATA[
var holdSrchCatHide = false;
var holdRefVwStatCdHide = false;
$(document).ready(function() {
	$('body:first').on('click', function(){
		if(holdSrchCatHide) holdSrchCatHide = false;
		else $("#listsearch #schCatSelect").hide();
		if(holdRefVwStatCdHide) holdRefVwStatCdHide = false;
		else $("#listsearchHidden #refVwStatCdSelect").hide();
	});
	
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('list');
});<%
// 검색 클릭 %>
function searchApv(event){
	$m.nav.curr(event, './listApvBx.do?'+$('#searchForm').serialize());
}<%
// 검색 조건 클릭 %>
function setSearchCatOption(code, value){
	var $form = $("#searchForm");
	$form.find("input[name='schCat']").val(code);
	$form.find("#listsearch .listselect:first #selectView span").text(value);
	$form.find("#listsearch #schCatSelect").hide();
}<%
// 검색 조건 클릭 %>
function setRefVwStatCdOption(code, value){
	var $form = $("#searchForm");
	$form.find("input[name='refVwStatCd']").val(code);
	$form.find("#listsearchHidden .listselect:first #refVwStatCdView span").text(value);
	$form.find("#listsearchHidden #refVwStatCdSelect").hide();
}<%
// 검색 조건 열기%>
function toggleSearchCondition(){
	var $form = $("#searchForm");
	var $area = $('#listsearchHidden');
	$form.find("input[name='openCondExt']").val($area.is(":visible") ? "N" : "Y");
	$area.toggle();
}<%
// 일괄결재 %>
var gBulkApvObj = null;
function processBulkApv(){<%
	//ap.msg.bulkApvNotDoneYet=일괄결재가 진행중입니다. %>
	if(gBulkApvObj != null){
		$m.msg.alertMsg('ap.msg.bulkApvNotDoneYet');
		return;
	}
	gBulkApvObj = { objects:[], secuId:null, msgs:[], successCnt:0 };
	var roleCd, roleMsgArr=[], hasSecuDoc=false, apvObj;
	var roleMsgMap = {mak:'<u:term termId="ap.term.mak" />',makAgr:'<u:term termId="ap.term.makAgr" />',makVw:'<u:term termId="ap.term.makVw" />'};
	$("#apListArea input[type='checkbox']:checked").each(function(){
		apvObj = JSON.parse($(this).val());
		roleCd = apvObj.roleCd;
		if(roleCd=='mak' || roleCd=='makAgr' || roleCd=='makVw'){
			if(!roleMsgArr.contains(roleMsgMap[roleCd])) roleMsgArr.push(roleMsgMap[roleCd]);
		} else if(apvObj.docPw=='Y'){
			hasSecuDoc = true;
		} else {
			gBulkApvObj.objects.push(apvObj);
		}
	});
	
	var checkMsg = null;
	if(hasSecuDoc || roleMsgArr.length>0){<%
		// ap.msg.noBulkApvDocWithSecuAndRoles="보안문서"와 결재자 역할이 {0}인 문서는 일괄결재에서 제외 됩니다.
		// ap.msg.noBulkApvDocWithSecu="보안문서"는 일괄결재에서 제외 됩니다.
		// ap.msg.noBulkApvDocWithRoles=결재자 역할이 {0}인 문서는 일괄결재에서 제외 됩니다. %>
		var msgId = (hasSecuDoc && roleMsgArr.lenghth>0) ? 'ap.msg.noBulkApvDocWithSecuAndRoles' : hasSecuDoc ? 'ap.msg.noBulkApvDocWithSecu' : 'ap.msg.noBulkApvDocWithRoles';
		checkMsg = $m.msg.callMsg(msgId, ['\"'+roleMsgArr.join('\", \"')+'\"']);
	}
	
	if(gBulkApvObj.objects.length==0){
		if(checkMsg==null){<%
			// cm.msg.noSelectedItem=선택된 "{0}"(이)가 없습니다. - ap.jsp.doc=문서 %>
			$m.msg.alertMsg("cm.msg.noSelectedItem", ["#ap.jsp.doc"]);
		} else {<%
			// ap.msg.noBulkApvDoc=일괄결재 가능한 문서가 없습니다.%>
			var msg = $m.msg.callMsg('ap.msg.noBulkApvDoc');
			$m.dialog.alert(msg + "\n\n" + checkMsg);
		}
		gBulkApvObj = null;
		return;
	}<%
	// 도장 또는 서명이 설정 되었는지 체크 %>
	$m.ajax("${_cxPth}/ap/box/getStampInfoAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", null, function(data){
		if(data.message != null){
			$m.dialog.alert(data.message);
			gBulkApvObj = null;
		} else {
			processBulkApvStep2(checkMsg);
		}
	});
}
function processBulkApvStep2(checkMsg){
	if(checkMsg!=null){<%
		// ap.cfrm.bulkApv={0}건의 문서를 일괄결재 하시겠습니까 ? %>
		var msg = $m.msg.callMsg("ap.cfrm.bulkApv",[gBulkApvObj.objects.length+'']);
		$m.dialog.confirm(msg + "\n" + checkMsg+"\n", function(data){
			if(data==true){
				processBulkApvStep3();
			} else {
				gBulkApvObj = null;
			}
		});
	} else {
		processBulkApvStep3();
	}
}
function processBulkApvStep3(){
	if('${optConfigMap.notAlwApvPw}' != 'Y'){<%// 결재 비밀번호 사용함 - 비밀번호 팝업 %>
		$m.dialog.open({
			id:'setApvPwPop',
			title:'<u:msg titleId="ap.btn.bulkApv" alt="일괄결재" />',
			url:"/ap/box/setApvPwPop.do?menuId=${menuId}&bxId=${param.bxId}"
		}, function(){
			if(gBulkApvObj && gBulkApvObj.secuId == null) gBulkApvObj = null; 
		});
	} else {
		processBulkApvStep4(0);
	}
}
function callbackSecuId(apvStatCd, secuId){
	gBulkApvObj.secuId = secuId;
	processBulkApvStep4(0);
}
function processBulkApvStep4(index){
	
	if(index < gBulkApvObj.objects.length){
		var obj = gBulkApvObj.objects[index];
		var data = {process:"processBulkApv", apvNo:obj.apvNo, apvLnPno:obj.apvLnPno, apvLnNo:obj.apvLnNo, secuId:gBulkApvObj.secuId};
		$m.ajax("/ap/box/transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}", data, function(data){
			if(data.message != null){
				gBulkApvObj.msgs.push(data.message+" ("+obj.docSubj+")");
			}
			gBulkApvObj.secuId = data.secuId;
			if(data.result=='ok') gBulkApvObj.successCnt++;
			
			index++;
			if(index < gBulkApvObj.objects.length){
				window.setTimeout('processBulkApvStep4('+index+')', 5);
			} else {<%
				// ap.msg.bulkApvRslt={0}건의 문서를 일괄결재 하였습니다. %>
				gBulkApvObj.msgs.push($m.msg.callMsg('ap.msg.bulkApvRslt',[gBulkApvObj.successCnt+'']));
				$m.dialog.alert(gBulkApvObj.msgs.join('\n'));
				gBulkApvObj = null;
				$m.nav.reload();
			}
		});
	} else {
		gBulkApvObj = null;
	}
}<%
// 전체 선택 %>
function toggleChecks(){
	var hasUncheck = false;
	var chks = $("#apListArea input[type='checkbox']");
	chks.each(function(){
		if(!this.checked) hasUncheck = true;
	});
	chks.each(function(){
		if(hasUncheck){
			if(!this.checked){
				$ui.toggle($(this).parent()[0], '');
			}
		} else {
			if(this.checked){
				$ui.toggle($(this).parent()[0], '');
			}
		}
	});
}
//]]>
</script>
<section>
<c:if test="${param.bxId eq 'waitBx' and optConfigMap.mobBulkApv eq 'Y' and writeAuth eq 'Y'}">
	<div class="btnarea">
		<div class="size">
		<dl>
			<dd class="btn" onclick="javascript:toggleChecks();"><u:msg alt="전체선택" titleId="cm.check.all"/></dd>
			<dd class="btn" onclick="javascript:processBulkApv();"><u:msg alt="일괄결재" titleId="ap.btn.bulkApv"/></dd>
		</dl>
		</div>
	</div>
</c:if>
	
	<form id="searchForm" name="searchForm" action="${_uri}" onsubmit="searchApv(event);" >
	<input type="hidden" name="menuId" value="${menuId}" />
	<input type="hidden" name="bxId" value="${param.bxId}" />
	<input type="hidden" name="schCat" value="${empty param.schCat ? 'docSubj' : param.schCat}" />
	<input type="hidden" name="refVwStatCd" value="${not empty param.refVwStatCd ? param.refVwStatCd : param.refVwStatCd eq '' ? '' : 'inRefVw'}" />
	<input type="hidden" name="openCondExt" value="${param.openCondExt}" />
	
	<div class="listsearch" id="listsearch">
		<div class="listselect">
			<div class="open1" id="schCatSelect" style="display:none">
				<div class="open_in1">
					<div class="open_div">
					<dl>
						<dd class="txt" onclick="javascript:setSearchCatOption($(this).attr('data-code'),$(this).text());" data-code="docSubj"><u:msg titleId="ap.doc.docSubj" alt="문서제목" /></dd>
						<dd class="line"></dd>
						<dd class="txt" onclick="javascript:setSearchCatOption($(this).attr('data-code'),$(this).text());" data-code="makrNm"><u:msg titleId="ap.doc.makrNm" alt="기안자" /></dd>
						<dd class="line"></dd>
						<dd class="txt" onclick="javascript:setSearchCatOption($(this).attr('data-code'),$(this).text());" data-code="bodyHtml"><u:msg titleId="ap.jsp.bodyHtml" alt="결재본문" /></dd>
					</dl>
					</div>
				</div>
			</div>
			
			<div class="select1">
				<div class="select_in1" onclick="holdSrchCatHide = true; $('#listsearch #schCatSelect').toggle();">
				<dl>
					<dd class="select_txt1" id="selectView"><span><u:msg titleId="${param.schCat=='makrNm' ? 'ap.doc.makrNm' : param.schCat=='bodyHtml' ? 'ap.jsp.bodyHtml' : 'ap.doc.docSubj'}" alt="문서제목/기안자/결재본문" /></span></dd>
					<dd class="select_btn"></dd>
				</dl>
				</div>
			</div>
			
		</div>
		<div class="${param.bxId eq 'refVwBx' ? 'listinput2' : 'listinput'}">
			<div class="input1">
			<dl>
				<dd class="input_left"></dd>
				<dd class="input_input"><input type="text" class="input_ip" name="schWord" maxlength="30" value="<u:out value="${param.schWord}" type="value" />" /></dd>
				<dd class="input_btn" onclick="javascript:searchApv(event);"><div class="search"></div></dd>
			</dl>
			</div>
		</div><c:if test="${param.bxId eq 'refVwBx'}">
		<div class="unfoldbtn" onclick="toggleSearchCondition()" id="unfold"></div></c:if>
	</div>
		<div class="listsearch" id="listsearchHidden" style="${param.openCondExt eq 'Y' ? '' : 'display:none'}">
		<div class="listselect">
			<div class="open1" id="refVwStatCdSelect" style="display:none">
				<div class="open_in1">
					<div class="open_div">
					<dl>
						<dd class="txt" onclick="javascript:setRefVwStatCdOption($(this).attr('data-code'),$(this).text());" data-code=""><u:msg titleId="cm.option.all" alt="전체" /></dd><c:forEach
							items="${refVwStatCdList}" var="refVwStatCd"><c:if test="${refVwStatCd.cd != 'befoRefVw'}">
						<dd class="line"></dd>
						<dd class="txt" onclick="javascript:setRefVwStatCdOption($(this).attr('data-code'),$(this).text());" data-code="${refVwStatCd.cd}">${refVwStatCd.rescNm}</dd>
						</c:if></c:forEach>
					</dl>
					</div>
				</div>
			</div>
			
			<div class="select1">
				<div class="select_in1" onclick="holdRefVwStatCdHide = true; $('#listsearchHidden #refVwStatCdSelect').toggle();">
				<dl>
					<dd class="select_txt1" id="refVwStatCdView"><span><c:if
						test="${param.refVwStatCd eq ''}"><u:msg titleId="cm.option.all" alt="전체" /></c:if><c:if
						test="${param.refVwStatCd ne ''}"><c:forEach
							items="${refVwStatCdList}" var="refVwStatCd" varStatus="refVwStatus"><c:if
								test="${param.refVwStatCd eq refVwStatCd.cd
									or (empty param.refVwStatCd and param.refVwStatCd ne '' and refVwStatCd.cd eq 'inRefVw')
									}">${refVwStatCd.rescNm}</c:if></c:forEach></c:if></span></dd>
					<dd class="select_btn"></dd>
				</dl>
				</div>
			</div>
		</div>
	</div>
	</form>

	<div class="listarea" id="apListArea">
	<article><c:forEach
	
		items="${apOngdBVoMapList}" var="apOngdBVoMap" varStatus="status"><c:if
			test="${param.bxId eq 'waitBx' and optConfigMap.mobBulkApv eq 'Y'}">
		<div class="listdiv_fixed">
			<div class="listcheck_fixed">
			<dl><u:out value="${apOngdBVoMap.docSubj}" type="jsonValue" var="_docSubj" />
				<m:check type="checkbox" 
					id="${apOngdBVoMap.apvNo}" name="apvNo" value='{"apvNo":"${apOngdBVoMap.apvNo
						}","apvLnPno":"${apOngdBVoMap.apvLnPno
						}","apvLnNo":"${apOngdBVoMap.apvLnNo
						}","docPw":"${not empty apOngdBVoMap.docPwEnc and apOngdBVoMap.makrUid != sessionScope.userVo.userUid ? "Y" : ""
						}","docStatCd":"${apOngdBVoMap.docStatCd
						}","apvrRoleCd":"${apOngdBVoMap.apvrRoleCd
						}","intgNo":"${apOngdBVoMap.intgNo
						}","docSubj":"${_docSubj
						}"}' />
			</dl>
			</div>
		
			<div class="list_fixed" onclick="$m.nav.next(event, '/ap/box/viewDoc.do?menuId=${menuId}&apvNo=${apOngdBVoMap.apvNo}&bxId=${param.bxId}&apvLnPno=${apOngdBVoMap.apvLnPno}&apvLnNo=${apOngdBVoMap.apvLnNo}')">
			<dl>
				<dd class="tit">
				<m:listTitle title="${apOngdBVoMap.docSubj}"
					ugnt="${apOngdBVoMap.ugntYn eq 'Y'}"
					secu="${apOngdBVoMap.secuYn eq 'Y'}"
					agnt=""
					notRead="${apOngdBVoMap.notReadYn eq 'Y'}" />
				</dd>
				<dd class="name"><u:out value="${apOngdBVoMap.makDeptNm}"
					/> ㅣ <u:out value="${apOngdBVoMap.makrNm}"
					/> ㅣ <c:if
						test="${param.bxId eq 'apvdBx' || param.bxId eq 'rejtBx' || param.bxId eq 'postApvdBx'}"
						><u:out value="${apOngdBVoMap.cmplDt}" type="date" /></c:if> <c:if
						test="${not (param.bxId eq 'apvdBx' || param.bxId eq 'rejtBx' || param.bxId eq 'postApvdBx')}"
						><u:out value="${apOngdBVoMap.makDt}" type="date" /></c:if
					> ㅣ <c:if
					test="${param.bxId eq 'waitBx' and apOngdBVoMap.apvStatCd eq 'hold'}"><u:msg
						titleId="ap.btn.hold" alt="보류" /></c:if><c:if
					test="${param.bxId eq 'waitBx' and apOngdBVoMap.apvStatCd eq 'cncl'}"><c:if
						test="${apOngdBVoMap.apvrRoleCd eq 'psnOrdrdAgr'
							or apOngdBVoMap.apvrRoleCd eq 'psnParalAgr'
							or apOngdBVoMap.apvLnPno != '0'}"><u:msg
								titleId="ap.btn.cancelAgr" alt="합의취소" /></c:if><c:if
						test="${not (apOngdBVoMap.apvrRoleCd eq 'psnOrdrdAgr'
							or apOngdBVoMap.apvrRoleCd eq 'psnParalAgr'
							or apOngdBVoMap.apvLnPno != '0')}"><u:msg
								titleId="ap.btn.cancelApv" alt="승인취소" /></c:if></c:if><c:if
					test="${param.bxId eq 'refVwBx'}">${apOngdBVoMap.refVwStatNm}</c:if><c:if
					test="${ not (param.bxId eq 'waitBx' and apOngdBVoMap.apvStatCd eq 'hold')
						and not (param.bxId eq 'waitBx' and apOngdBVoMap.apvStatCd eq 'cncl')
						and param.bxId ne 'refVwBx'}"><u:term
						termId="ap.term.${apOngdBVoMap.docStatCd}" /></c:if></dd>
			</dl>
			</div>
		</div></c:if><c:if
			test="${not (param.bxId eq 'waitBx' and optConfigMap.mobBulkApv eq 'Y')}">
		<div class="listdiv" onclick="$m.nav.next(event, '/ap/box/viewDoc.do?menuId=${menuId}&apvNo=${apOngdBVoMap.apvNo}&bxId=${param.bxId}&apvLnPno=${apOngdBVoMap.apvLnPno}&apvLnNo=${apOngdBVoMap.apvLnNo}')">
			<div class="list">
			<dl>
				<dd class="tit">
				<m:listTitle title="${apOngdBVoMap.docSubj}"
					ugnt="${apOngdBVoMap.ugntYn eq 'Y'}"
					secu="${apOngdBVoMap.secuYn eq 'Y'}"
					agnt=""
					notRead="${apOngdBVoMap.notReadYn eq 'Y'}" />
				</dd>
				<dd class="name"><u:out value="${apOngdBVoMap.makDeptNm}"
					/> ㅣ <u:out value="${apOngdBVoMap.makrNm}"
					/> ㅣ <c:if
						test="${param.bxId eq 'apvdBx' || param.bxId eq 'rejtBx' || param.bxId eq 'postApvdBx'}"
						><u:out value="${apOngdBVoMap.cmplDt}" type="date" /></c:if> <c:if
						test="${not (param.bxId eq 'apvdBx' || param.bxId eq 'rejtBx' || param.bxId eq 'postApvdBx')}"
						><u:out value="${apOngdBVoMap.makDt}" type="date" /></c:if
					> ㅣ <c:if
					test="${param.bxId eq 'waitBx' and apOngdBVoMap.apvStatCd eq 'hold'}"><u:msg
						titleId="ap.btn.hold" alt="보류" /></c:if><c:if
					test="${param.bxId eq 'waitBx' and apOngdBVoMap.apvStatCd eq 'cncl'}"><c:if
						test="${apOngdBVoMap.apvrRoleCd eq 'psnOrdrdAgr'
							or apOngdBVoMap.apvrRoleCd eq 'psnParalAgr'
							or apOngdBVoMap.apvLnPno != '0'}"><u:msg
								titleId="ap.btn.cancelAgr" alt="합의취소" /></c:if><c:if
						test="${not (apOngdBVoMap.apvrRoleCd eq 'psnOrdrdAgr'
							or apOngdBVoMap.apvrRoleCd eq 'psnParalAgr'
							or apOngdBVoMap.apvLnPno != '0')}"><u:msg
								titleId="ap.btn.cancelApv" alt="승인취소" /></c:if></c:if><c:if
					test="${param.bxId eq 'refVwBx'}">${apOngdBVoMap.refVwStatNm}</c:if><c:if
					test="${ not (param.bxId eq 'waitBx' and apOngdBVoMap.apvStatCd eq 'hold')
						and not (param.bxId eq 'waitBx' and apOngdBVoMap.apvStatCd eq 'cncl')
						and param.bxId ne 'refVwBx'}"><u:term
						termId="ap.term.${apOngdBVoMap.docStatCd}" /></c:if></dd>
			</dl>
			</div>
		</div></c:if></c:forEach><c:if
		
			test="${recodeCount == 0}">
		<div class="listdiv_nodata" onclick="javascript:;">
			<dl>
			<dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd>
			</dl>
		</div></c:if>
	</article>
	</div>
	
	<m:pagination />
	
	<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
</section>
