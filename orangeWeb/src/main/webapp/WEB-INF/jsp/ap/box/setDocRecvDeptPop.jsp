<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	//request.setAttribute("onTab", "dom");
	//request.setAttribute("onTab", "for");
	//request.setAttribute("onTab", "outOrg");
%>
<script type="text/javascript">
<!--<%
// 조직도 클릭시 호출 - 아무것도 안함 - 프레임간 오류 방지 %>
function setRecvDeptVo(){}<%
// 다중 선택일때 초기에 호출됨 - 아무것도 안함 - 프레임간 오류 방지 %>
function processSelect(){}<%
// 현재 선택된 탭 %>
var gRecvDeptTypCd = "${empty onTab ? 'dom' : onTab}";<%
// 수신처 지정에서 관리하는 데이터 %>
var gRecvDeptAttrNms = ["recvDeptSeq", "recvDeptTypCd", "recvDeptTypNm", "recvDeptId", "recvDeptNm", "refDeptId", "refDeptNm", "sendYn"];<%
// 수신처 그룹 구분 - pub:공용 / prv:개인 - 공용의 경우 관리자 만 수정 가능하게 함 %>
var gRecvGrpMode = null;<%
// 수신그룹 저장 - 버튼 보이기/숨기기 %>
function toggleSaveRecvGrpBtn(mode){
	if(mode!=null) gRecvGrpMode = mode;
	if(gRecvDeptTypCd != 'recvGrp' || gRecvGrpMode=='pub'){
		$("#saveRecvGrpBtn").hide();
	} else {
		$("#saveRecvGrpBtn").show();
	}
}<%
// 수신그룹 저장 버튼 %>
function saveRecvGrpDetl(){<%
	// 수신처 데이터 모으기 %>
	var arr = collectRecvDept();
	if(arr==null) return;
	getIframeContent("docRecvGrpFrm").saveRecvGrpDetl(arr);
}
<%
// 탭 선택 %>
function changeRecvDeptTab(tab){
	gRecvDeptTypCd = tab;
	if(tab=='outOrg'){
		$("#docRecvOutOrgArea #recvDeptNm").focus();
	}else if(tab=='recvGrp'){
		if(getIframeContent("docRecvGrpFrm").location.href.indexOf("reloadable.do")>0){
			reloadFrame("docRecvGrpFrm","./listRecvGrpFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&mode=prv");
		}
	}
	toggleSaveRecvGrpBtn();
}<%
// [>]추가 아이콘 클릭 %>
function addRecvDept(){
	if(gRecvDeptTypCd=='outOrg'){<%// 외부기관 %>
		var param = new ParamMap().getData('docRecvOutOrgArea').put('recvDeptTypCd','outOrg').put('recvDeptTypNm','<u:msg titleId="ap.jsp.recvDept.outOrg" alt="외부기관" />');
		if(param.get('recvDeptNm').trim()==''){<%
			// ap.msg.noTypeRecvDept=입력한 수신처가 없습니다. %>
			alertMsg("ap.msg.noTypeRecvDept");
			$("#docRecvOutOrgArea #recvDeptNm").focus();
		} else {
			var chkList = collectRecvDeptDupCheck(gRecvDeptTypCd), newArr=[];
			if(!isDupRecvDept(chkList, param.map)){
				newArr.push(param.map);
			}
			addRecvDeptData(newArr);
			$("#docRecvOutOrgArea input").val("");
		}
	} else if(gRecvDeptTypCd=='dom'){<%// 대내 %>
		var iwin = getIframeContent("docRecvDomFrm");
		var arr = iwin.getSelectedNodes(), newArr=[];
		var chkList = collectRecvDeptDupCheck(gRecvDeptTypCd), data;
		arr.each(function(idx, va){<%// 해당 명징으로 데이터 전환 - 부서(기관):수신처 %>
			data = {recvDeptId:va.orgId, recvDeptNm:va.rescNm, recvDeptTypCd:'dom', recvDeptTypNm:'<u:msg titleId="ap.jsp.recvDept.dom" alt="대내" />'};
			if(!isDupRecvDept(chkList, data)){
				newArr.push(data);
			}
		});
		addRecvDeptData(newArr);
		iwin.deSelectNodes();<%// 선택 해제 %>
	} else if(gRecvDeptTypCd=='for'){<%// 대외 %>
		var iwin = getIframeContent("docRecvForFrm");
		var arr = iwin.getSelectedNodesWithParentG(), newArr=[];
		var chkList = collectRecvDeptDupCheck(gRecvDeptTypCd), data;
		arr.each(function(idx, va){
			if(va.orgTypCd=='D'){<%// 부서선택 - 상위기관:수신처, 부서:참조처 %>
				data = {recvDeptId:va.gOrgId, recvDeptNm:va.gRescNm, refDeptId:va.orgId, refDeptNm:va.rescNm, recvDeptTypCd:'for', recvDeptTypNm:'<u:msg titleId="ap.jsp.recvDept.for" alt="대외" />'};
			} else {<%// 기관선택 - 수신처로 세팅 %>
				data = {recvDeptId:va.orgId, recvDeptNm:va.rescNm, refDeptId:'', refDeptNm:'', recvDeptTypCd:'for', recvDeptTypNm:'<u:msg titleId="ap.jsp.recvDept.for" alt="대외" />'};
			}
			if(!isDupRecvDept(chkList, data)){
				newArr.push(data);
			}
		});
		addRecvDeptData(newArr);
		iwin.deSelectNodes();<%// 선택 해제 %>
	} else if(gRecvDeptTypCd=='recvGrp'){<%// 수신그룹 %>
		var iwin = getIframeContent("docRecvGrpFrm");
		var arr = iwin.collectRecvDept(gRecvDeptAttrNms), newArr=[];
		var chkList = collectRecvDeptDupCheck();
		arr.each(function(idx, data){
			if(!isDupRecvDept(chkList, data)){
				newArr.push(data);
			}
		});
		addRecvDeptData(newArr);
		iwin.deSelectNodes();<%// 선택 해제 %>
	}
}<%
// [<]삭제 아이콘 클릭 - 선택 제거 %>
function removeRecvDept(){
	$("#recvDeptDataPopArea input[id!='checkHeader']:checked:visible").each(function(){
		$(getParentTag(this, 'tr')).remove();
	});
}<%
// 추가 데이터 배열로 각각 줄(TR) 생성 %>
function addRecvDeptData(arr){
	var $lastTr = $("#recvDeptDataPopArea tr:last"), $newTr, $check;
	arr.each(function(idx, obj){
		$newTr = $lastTr.clone();<%// 숨겨진 TR 복사 %>
		<%// checkbox 의 속성에 데이터 할당, 유니폼 적용 %>
		$check = $newTr.find("input[type='checkbox']");
		if(obj['sendYn']=='Y') $check[0].disabled = true;
		gRecvDeptAttrNms.each(function(idx, nm){
			$check.attr('data-'+nm, obj[nm]==null ? '' : obj[nm]);
		});
		$check.uniform();
		<%// 각 TD에 해당하는 데이터 입력 %>
		$newTr.find("td:eq(1)").text(obj["recvDeptNm"]==null ? '' : obj["recvDeptNm"]);
		$newTr.find("td:eq(2)").text(obj["refDeptNm"]==null ? '' : obj["refDeptNm"]);
		$newTr.find("td:eq(3)").text(obj["recvDeptTypNm"]==null ? '' : obj["recvDeptTypNm"]);
		$newTr.find("td:eq(4)").text("${param.sendMore == 'Y' ? 'Y' : ''}");
		$newTr.show();
		<%// TR 삽입 %>
		$newTr.insertBefore($lastTr);
	});
}<%
// 확인 버튼 %>
function confirmRecvDept(){<%
	// 수신처 데이터 모으기 %>
	var arr = collectRecvDept();
	setRecvDeptData(arr);<%// >> setDoc.jsp - 팝업 부모창 %>
	dialog.close("setDocRecvDeptDialog");
}<%
// 중복 체크 %>
function isDupRecvDept(chkList, data){
	if('outOrg'==data['recvDeptTypCd']){
		return chkList.contains(data['recvDeptTypCd']+"|"+data['recvDeptNm']+"|"+data['refDeptNm']);
	} else if('dom'==data['recvDeptTypCd']){
		return chkList.contains(data['recvDeptTypCd']+"|"+data['recvDeptId']);
	} else if('for'==data['recvDeptTypCd']){
		return chkList.contains(data['recvDeptTypCd']+"|"+data['recvDeptId']+"|"+data['refDeptId']);
	}
	return false;
}<%
// 중복 확인용 데이터 모으기 %>
function collectRecvDeptDupCheck(recvDeptTypCd){
	var arr = [];
	collectRecvDept().each(function(index, data){
		if(recvDeptTypCd==null || recvDeptTypCd==data['recvDeptTypCd']){
			if('outOrg'==data['recvDeptTypCd']){
				arr.push(data['recvDeptTypCd']+"|"+data['recvDeptNm']+"|"+data['refDeptNm']);
			} else if('dom'==data['recvDeptTypCd']){
				arr.push(data['recvDeptTypCd']+"|"+data['recvDeptId']);
			} else if('for'==data['recvDeptTypCd']){
				arr.push(data['recvDeptTypCd']+"|"+data['recvDeptId']+"|"+data['refDeptId']);
			}
		}
	});
	return arr;
}<%
// 수신처 데이터 모으기 %>
function collectRecvDept(){
	var arr = [], obj, $check, va;
	$("#recvDeptDataPopArea input[id!='checkHeader']:visible").each(function(){
		$check = $(this);
		obj = {};
		gRecvDeptAttrNms.each(function(idx, name){
			va = $check.attr("data-"+name);
			obj[name] = va==null ? '' : va;
		});
		arr.push(obj);
	});
	return arr;
}<%
// onload %>
$(document).ready(function() {
	var arr = [];
	$("#docDataArea #recvDeptArea input").each(function(){
		if(this.name=='recvDept'){
			arr.push(JSON.parse(this.value));
		}
	});
	if(arr.length>0) addRecvDeptData(arr);
});<%
// 저장 버튼 클릭 - 완결 후 수신처를 변경 할 때 %>
function sendMoreRecvDept(){<%
	// 수신처 데이터 모으기 %>
	var recvDeptList=[], buffer, result=false, moreCnt=0;
	collectRecvDept().each(function(index, inputData){
		if(inputData['sendYn']!='Y') moreCnt++;
		buffer = new StringBuffer();
		buffer.append("{");
		first = true;
		gRecvDeptAttrNms.each(function(idx, name){
			if(first) first = false;
			else buffer.append(',');
			buffer.append('"').append(name).append('":"').append(inputData[name]).append('"');
		});
		buffer.append("}");
		recvDeptList.push(buffer.toString());
	});
	if(moreCnt==0){<%
		// ap.msg.noPlaceToAddSend=추가발송할 수신처가 없습니다. %>
		alertMsg('ap.msg.noPlaceToAddSend');
		return;
	}
	callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {process:"sendMoreRecvDept",apvNo:"${param.apvNo}",recvDeptList:recvDeptList}, function(data){
		if(data.message != null) alert(data.message);
		result = data.result == 'ok';
	});
	if(result) dialog.close('setDocRecvDeptDialog');
}<%
// 시행범위 변경 - 라디오 클릭 %>
function checkChangeRecv(radio){
	var checkVa = radio.value=='dom' ? 'for' : radio.value=='for' ? 'dom' : null;
	var $enfcScopeCd = $("#recvDeptForm #enfcScopCd");
	if(checkVa != null){<%
		// valid : 발송된 것중 시행범위를 벗어난 것이 있는지 여부 - 변경불가 메세지
		// outOfScope : 발송 안된 것중 시행범위 벗어난게 있는지 여부 - 삭제 할 것인지 확인 %>
		var valid = true, outOfScope = false;
		collectRecvDept().each(function(index, obj){
			if(obj['recvDeptTypCd']==checkVa){
				if(obj['sendYn']=='Y'){
					valid = false;
					return false;
				}
				outOfScope = true;
			}
		});
		if(!valid){<%
			// ap.jsp.recvDept.notChangeToFor="대내"로 이미 발송되어 "대외"로 변경 할 수 없습니다.
			// ap.jsp.recvDept.notChangeToDom="대외"로 이미 발송되어 "대내"로 변경 할 수 없습니다. %>
			alertMsg(radio.value=='for' ? 'ap.jsp.recvDept.notChangeToFor' : 'ap.jsp.recvDept.notChangeToDom');<%
			// 이전에것 체크하도록 함 %>
			$("#enfcScopCdArea input[value='"+$enfcScopeCd.val()+"']").checkInput();
			return;
		} else if(outOfScope){<%
			// ap.jsp.recvDept.delCfrm=시행범위를 "{0}"로 변경하시면 "{1}" 수신처가 삭제 됩니다.\n시행범위를 변경 하시겠습니까 ?
			//    ap.jsp.recvDept.dom=대내
			//    ap.jsp.recvDept.for=대외 %>
			if(confirmMsg("ap.jsp.recvDept.delCfrm", radio.value=='for' ? ["#ap.jsp.recvDept.for","#ap.jsp.recvDept.dom"] : ["#ap.jsp.recvDept.dom","#ap.jsp.recvDept.for"])){<%
				// 해당 데이터 삭제 %>
				$("#recvDeptDataPopArea tr").each(function(){
					if($(this).find("input[data-recvDeptTypCd='"+checkVa+"']").length>0){
						$(this).remove();
					}
				});
			} else {<%
				// 이전에것 체크하도록 함 %>
				$("#enfcScopCdArea input[value='"+$enfcScopeCd.val()+"']").checkInput();
				return;
			}
		}
	}
	var $tabArea = $("#docRecvTab");
	if(radio.value=='for'){
		$tabArea.find("li[data-areaId='docRecvDomFrm']").hide();
	} else {
		$tabArea.find("li[data-areaId='docRecvDomFrm']").show();
	}
	if(radio.value=='dom'){
		$tabArea.find("li[data-areaId='docRecvForFrm']").hide();
	} else {
		$tabArea.find("li[data-areaId='docRecvForFrm']").show();
	}
	if(radio.value=='for' || radio.value=='dom'){
		changeTab('docRecvTab', radio.value=='for' ? 1 : 0);
	}
	changeRecvDeptTab(radio.value);
	$enfcScopeCd.val(radio.value);
}
//-->
</script>
<div style="width:900px">

<c:if test="${false}"><!-- 시행범위 변경 기능 제거 -->
<u:listArea colgroup="18%,82%" id="enfcScopCdArea" style="display:none;">
	<td class="head_lt"><u:msg titleId="cols.enfcScop" alt="시행범위" /></td>
	<td><u:checkArea><c:forEach
		items="${enfcScopCdList}" var="enfcScopCd">
		<u:radio name="enfcScopCd" value="${enfcScopCd.cd}" title="${enfcScopCd.rescNm}"
			inputClass="bodybg_lt" checked="${enfcScopCd.cd == param.enfcScopCd}" onclick="checkChangeRecv(this);" /></c:forEach>
		</u:checkArea></td>
</u:listArea>
</c:if>

<u:tabGroup id="docRecvTab" noBottomBlank="true">
	<u:tab id="docRecvTab" alt="대내" areaId="docRecvDomFrm" titleId="ap.jsp.recvDept.dom"
		onclick="changeRecvDeptTab('dom');" on="${onTab == 'dom' or empty onTab}"
		style="${param.enfcScopCd == 'for' ? 'display:none;' : ''}" />
	<u:tab id="docRecvTab" alt="대외" areaId="docRecvForFrm" titleId="ap.jsp.recvDept.for"
		onclick="changeRecvDeptTab('for');" on="${onTab == 'for'}"
		style="${param.enfcScopCd == 'dom' ? 'display:none;' : ''}" /><c:if
	test="${optConfigMap.regOuter == 'Y'}">
	<u:tab id="docRecvTab" alt="외부기관" areaId="docRecvOutOrgArea" titleId="ap.jsp.recvDept.outOrg"
		onclick="changeRecvDeptTab('outOrg');" on="${onTab == 'outOrg'}" /></c:if>
	<u:tab id="docRecvTab" alt="수신그룹" areaId="docRecvGrpFrm" titleId="ap.jsp.recvGrp"
		onclick="changeRecvDeptTab('recvGrp');" on="${onTab == 'recvGrp'}" />
</u:tabGroup>
<u:cmt

	cmt="[왼쪽] 대내/대외/외부기관 용 분할"/>
<u:tabArea outerStyle="height:370px; overflow-x:hidden; overflow-y:auto;" innerStyle="margin:10px 10px 0px 10px;" >

<u:boxArea className="wbox" style="height:350px; width:40%; float:left"
	outerStyle="height:346px;overflow:hidden;"
	innerStyle="NO_INNER_IDV" noBottomBlank="true" ><u:cmt

	cmt="대내 조직도용 프레임"/>
<iframe id="docRecvDomFrm" style="width:100%; height:344px; border:0px;<c:if test="${onTab != 'dom' and not empty onTab}"> display:none;</c:if>"
	src="./treeRecvDeptFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&recvDeptTypCd=dom" ></iframe><u:cmt

	cmt="대외 조직도용 프레임"/>
<iframe id="docRecvForFrm" style="width:100%; height:344px; border:0px;<c:if test="${onTab != 'for'}"> display:none;</c:if>"
	src="./treeRecvDeptFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&recvDeptTypCd=for" ></iframe><c:if
		test="${optConfigMap.regOuter == 'Y'}"><u:cmt

	cmt="외부기관용 DIV"/>
<div id="docRecvOutOrgArea" style="width:95%; height:344px; text-align:center; border:0px;<c:if test="${onTab != 'outOrg'}"> display:none;</c:if>">

<u:listArea style="padding:10px; margin-top:134px;" colgroup="30%,70%">
	<tr><td class="head_ct"><u:msg titleId="cols.recvDept" alt="수신처" /></td>
		<td><u:input id="recvDeptNm" titleId="cols.recvDept" style="width:90%;" maxByte="100" /></td>
	</tr>
	<tr><td class="head_ct"><u:msg titleId="cols.refDept" alt="참조처" /></td>
		<td><u:input id="refDeptNm" titleId="cols.refDept" style="width:90%;" maxByte="100" /></td>
	</tr>
</u:listArea>

</div></c:if><u:cmt

	cmt="수신그룹 용 프레임"/>
<iframe id="docRecvGrpFrm" style="width:100%; height:344px; border:0px; overflow:hidden; <c:if test="${onTab != 'recvGrp'}"> display:none;</c:if>"
	src="${_cxPth}/cm/util/reloadable.do" ></iframe>
	
</u:boxArea>

<u:cmt


	cmt="[가운데] [>], [<] 선택추가, 선택삭제 용"/>
<div style="width:5%; float:left; text-align:center; margin:148px 0 0 0;">
	<table style="margin:0 auto 0 auto;" border="0" cellpadding="0" cellspacing="0">
		<tr><td><a href="javascript:addRecvDept();"<u:elemTitle titleId="cm.btn.selAdd" alt="선택추가" type="image" />><img src="${_cxPth}/images/${_skin}/ico_right.png" width="20" height="20" /></a></td></tr>
		<tr><td class="height5"></td></tr>
		<tr><td><a href="javascript:removeRecvDept();"<u:elemTitle titleId="cm.btn.selDel" alt="선택삭제" type="image" />><img src="${_cxPth}/images/${_skin}/ico_left.png" width="20" height="20" /></a></td></tr>
	</table>
</div>
<u:cmt


	cmt="[오른쪽] 추가된 수신처 정보용"/>
<u:boxArea className="wbox" style="height:350px; width:55%; float:right"
	outerStyle="height:346px; overflow:hidden;"
	innerStyle="NO_INNER_IDV" noBottomBlank="true" >
	
<u:listArea id="recvDeptDataPopArea" colgroup="${param.sendMore == 'Y' ? '4%,33%,33%,15%,15%' : '4%,35%,35%,26%'}" style="height:332px; padding:10px 8px 2px 10px; width:96%; overflow:auto;">
	<tr id="titleTr"><td class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all'
		/>" onclick="checkAllCheckbox('recvDeptDataPopArea', this.checked);" value=""/></td>
		<td class="head_ct"><u:msg titleId="cols.recvDept" alt="수신처" /></td>
		<td class="head_ct"><u:msg titleId="cols.refDept" alt="참조처" /></td>
		<td class="head_ct"><u:msg titleId="ap.jsp.recvDept.recvTyp" alt="수신구분" /></td><c:if
			test="${param.sendMore == 'Y'}">
		<td class="head_ct"><u:msg titleId="ap.btn.sendAddDoc" alt="추가발송" /></td></c:if>
	</tr>
<c:forEach items="${apOngdRecvDeptLVoList}" var="apOngdRecvDeptLVo" varStatus="status">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'<c:if
		test="${status.last}"> style="display:none;" id="hiddenTr"</c:if>>
		<td class="bodybg_ct"><input type="checkbox" name="apvLnCheck"<c:if
				test="${status.last}"> class="skipThese"</c:if><c:if
				test="${apOngdRecvDeptLVo.sendYn=='Y'}"> disabled="disabled"</c:if>
			data-recvDeptSeq="${apOngdRecvDeptLVo.recvDeptSeq}"
			data-recvDeptTypCd="${apOngdRecvDeptLVo.recvDeptTypCd}"
			data-recvDeptTypNm="${apOngdRecvDeptLVo.recvDeptTypNm}"
			data-recvDeptId="${apOngdRecvDeptLVo.recvDeptId}"
			data-recvDeptNm="<u:out value="${apOngdRecvDeptLVo.recvDeptNm}" type="value" />"
			data-refDeptId="${apOngdRecvDeptLVo.refDeptId}"
			data-refDeptNm="<u:out value="${apOngdRecvDeptLVo.refDeptNm}" type="value" />"
			data-sendYn="<u:out value="${apOngdRecvDeptLVo.sendYn}" type="value" />"
			/></td>
		<td class="body_ct"><u:out value="${apOngdRecvDeptLVo.recvDeptNm}" /></td>
		<td class="body_ct"><u:out value="${apOngdRecvDeptLVo.refDeptNm}" /></td>
		<td class="body_ct"><u:out value="${apOngdRecvDeptLVo.recvDeptTypNm}" /></td><c:if
			test="${param.sendMore == 'Y'}">
		<td class="body_ct">${apOngdRecvDeptLVo.addSendYn == 'Y' ? 'Y' : ''}</td></c:if>
	</tr>
</c:forEach>
</u:listArea>

</u:boxArea>
</u:tabArea><c:if
	test="${param.forSending == 'Y'}">
<form id="recvDeptForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" name="apvNo" value="${param.apvNo}" />
<u:input type="hidden" name="enfcScopCd" value="${param.enfcScopCd}" />
</form></c:if>

<u:buttonArea>
	<u:button titleId="ap.btn.saveRecvGrp" href="javascript:saveRecvGrpDetl();" alt="수신그룹 저장"
		id="saveRecvGrpBtn" style="${onTab == 'recvGrp' ? '' : 'display:none;'}" /><c:if
	test="${param.sendMore == 'Y'}">
	<u:button titleId="ap.btn.sendAddDoc" href="javascript:sendMoreRecvDept();" alt="추가발송" /></c:if><c:if
	test="${param.sendMore != 'Y'}">
	<u:button titleId="cm.btn.confirm" href="javascript:confirmRecvDept();" alt="확인" />	</c:if>
	<u:button onclick="dialog.close(this);" alt="닫기" titleId="cm.btn.close" />
</u:buttonArea>
</div>