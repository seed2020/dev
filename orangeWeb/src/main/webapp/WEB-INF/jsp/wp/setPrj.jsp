<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%


request.setAttribute("role1Cds", new String[]{ "con", "dev", "all" });
request.setAttribute("conRole2Cds", new String[]{ "gpm", "pm", "a11", "b11","b12","b13", "b21","b22","b23", "c11" });
request.setAttribute("devRole2Cds", new String[]{ "d10","d20","d30" });

request.setAttribute("color1", "#F9FFFF");
request.setAttribute("color2", "#F4FFFF");

%><script type="text/javascript">
<!--<%--
[버튼]투입인력 - 임직원 / 외주 직원 --%>
function setManPower(role1Cd, mpTypCd){
	
	if(mpTypCd == 'emp'){

		var $tbody = $("#manPowerArea tbody:first");
		var data = [], trs = $tbody.children("[data-role1Cd='"+role1Cd+"']:visible");
		trs.find("input[type=hidden][name=mpId]").each(function(){
			if($(this).parent().find('input[name=mpTypCd]').val() == 'emp'){
				data.push({userUid:$(this).val()});
			}
		});
		searchUserPop({data:data, multi:true, mode:'search'}, function(arr){
			
			var delArr = [];
			var matched, uid, mpIdObj;
			trs.each(function(){
				matched = false;
				mpIdObj = $(this).find("input[type=hidden][name=mpId]");
				
				if(mpIdObj.parent().find('input[name=mpTypCd]').val() == 'emp'){
					uid = mpIdObj.val();
					if(arr!=null){
						arr.each(function(index, userVo){
							if(userVo!=null && uid == userVo.userUid){
								matched = true;
								arr[index] = null;
								return false;
							}
						});
					}
					if(!matched){
						delArr.push(this);
					}
				}
			});
			if(delArr.length > 0){
				delArr.each(function(index, trObj){
					$(trObj).remove();
				});
			}
			if(arr!=null){
				var addArr = [];
				arr.each(function(index, userVo){
					if(userVo != null){
						addArr.push({id:userVo.userUid, nm:userVo.rescNm, type:'emp'});
					}
				});
				appendMpTr(addArr, $tbody, role1Cd, mpTypCd);
			}
		});
		
	} else if(mpTypCd == 'out'){
		dialog.open("listOutsDialog", '<u:msg titleId="wp.outsourcing" alt="외주 직원" />', "./listOutsPop.do?menuId=${menuId}&cat=${param.cat}&role1Cd="+role1Cd+"&mode=multi");
	}
	
}<%--
[콜백] 외주인력 선택 후 확인 버튼 --%>
function setOutsourcingData(arr, role1Cd){
	
	var $tbody = $("#manPowerArea tbody:first");
	var oldIdArr = [];
	$tbody.children("[data-role1Cd='"+role1Cd+"']:visible").each(function(){
		$(this).find("input[type=hidden][name=mpId]").each(function(){
			oldIdArr.push($(this).val());
		});
	});
	
	var addArr = [];
	arr.each(function(index, vo){
		if(!oldIdArr.contains(vo.id)){
			addArr.push(vo);
		}
	});
	
	appendMpTr(addArr, $tbody, role1Cd, 'out');
	
}<%--
[기능] 투입인력 데이터 행 추가 --%>
function appendMpTr(arr, tbody, role1Cd, mpTypCd){
	
	var trHidden = (tbody==null ? $("#manPowerArea tbody:first") : tbody).children('[data-role1Cd='+role1Cd+'Sum]').prev();
	var html = trHidden.clone().wrapAll("<div/>").parent().html();
	
	arr.each(function(index, vo){
		if(vo != null){
			newTrObj = $(html).show();
			newTrObj.find('input[name=mpId]').val(vo.id);
			newTrObj.find('input[name=mpTypCd]').val(mpTypCd);
			newTrObj.find('input[name=prjRole1Cd]').val(role1Cd);
			newTrObj.find('#role1CdTd').html(role1Cd=='con' ? '<u:msg titleId="wp.prjRole1Cd.con" alt="컨설팅" />' : '<u:msg titleId="wp.prjRole1Cd.dev" alt="개발" />');
			newTrObj.find('#mpTypCdTd').html(mpTypCd=='emp' ? '<u:msg titleId="cm.option.empl" alt="임직원" />' : '<u:msg titleId="wp.outsourcing" alt="외주 직원" />');
			newTrObj.find('#mpNmTd').html(vo.nm);
			
			newTrObj.insertBefore(trHidden);
			setUniformCSS(newTrObj);
		}
	});
}<%--
[버튼] 투입인력 삭제--%>
function delManPower(){
	$("#manPowerArea input[type=checkbox]:checked:visible").not("#checkHeader").each(function(){
		$(getParentTag(this, 'tr')).remove();
	});
}<%--
[기능] 수주금액 - 블러이벤트 - 옆에 한글 석어서 표시 --%>
function dispPrjAmt(amt){
	var lang = '${sessionScope.userVo.langTypCd}';
	if(amt=='' || lang!='ko'){
		$("#prjAmtDisp").html("");
	} else {
		var len = amt.length;
		var mode =  parseInt((len-1)/4);
		var units = '경조억만원'.split('');
		var arr=[], single, singleInt;
		while(mode>=0){
			
			single = amt.substring(Math.max(0, len - (mode * 4) - 4), len - (mode * 4));
			singleInt = parseInt(single, 10);
			
			if(singleInt > 0){
				arr.push(singleInt);
				arr.push(units[4-mode]);
				if(mode>0) arr.push(' ');
			} else if(mode == 0){
				arr.push(units[4-mode]);
			}
			mode--;
		}
		$("#prjAmtDisp").html(arr.join(''));
	}
}<%--
[기능] 투입계획(M/D) - 합계 --%>
function processMdSum(){
	var sum = 0, va;
	$("#manPowerArea input[name=mpMd]").each(function(){
		va = $(this).val();
		if(va!=null && va!=''){
			va = parseInt(va, 10);
			if(!isNaN(va)) sum += va;
		}
	});
	$("#mdPlanSum").text(sum);
}<%--
[달력 변경] M/D 계산 --%>
function calcMd(date, opt){
	var start=date, end=date, mpId=opt.mpId;
	if(opt.end!=null){
		end = $("#"+opt.end).val();
		if(end!='' && start.localeCompare(end) > 0){
			end = start;
		}
	} else {
		start = $("#"+opt.start).val();
		if(start!='' && start.localeCompare(end) > 0){
			start = end;
		}
	}
	if(start!='' && end!=''){
		callAjax('./getMdBetweenAjx.do?menuId=${menuId}&cat=${param.cat}', {start:start, end:end}, function(data) {
			$("#tr"+opt.mpId+" input[name='mpMd']").val(data.md);
		});
	}
	return true;
}<%--
[버튼] 저장 --%>
function savePrj(modStatCd, opin){
	if(!validator.validate('prjManageFrm')){
		return;
	}
	var $form = $("#prjManageFrm");<%--
	// wp.msg.endDtMsg=종료일 등록 후에는 수정이나 공수 입력이 불가능 합니다.\\n저장 하시겠습니까 ? --%>
	if($form.find("#cmplYmd").val()!='' && !confirmMsg("wp.msg.endDtMsg")){
		return;
	}
	$form.find("#modStatCd").val(modStatCd);
	
	if(opin!=null && opin!=''){
		var opinArea = $form.find("#prjOpinArea");
		opinArea.html('');
		opinArea.appendHidden({'name':'modCont','value':opin});
	}
	
	$form.attr('action', './transPrj.do?menuId=${menuId}&cat=${param.cat}');
	$form.attr('target', 'dataframe');
	
	$form.attr('enctype','multipart/form-data');
	saveFileToForm('wpfiles', $form[0], null);<%-- submit 포함 --%>
	//$form.submit();
}<%--
[추가] 담당자 --%>
function processConfirm(actCd){
	if(actCd == 'askApv'){
		if(confirmMsg('wp.cfm.askApv')){<%-- wp.cfm.askApv=승인요청 하시겠습니까 ? --%>
			savePrj('askApv');
		}
	} else if(actCd == 'cancel'){
		if(confirmMsg('wp.cfm.cancel')){<%-- wp.cfm.cancel=승인요청을 취소 하시겠습니까 ? --%>
			processAct('cancel');
		}
	} else if(actCd == 'delVer'){
		if(confirmMsg('wp.cfm.devVer')){<%-- wp.cfm.devVer=해당 버전을 삭제 하시겠습니까 ? --%>
			processAct('delVer');
		}
	} else if(actCd == 'del'){
		if(confirmMsg('wp.cfm.dev')){<%-- wp.cfm.dev=삭제 하시겠습니까 ? --%>
			processAct('del');
		}
	}
	
}<%--
[추가] 담당자 --%>
function addPich(){
	var tbody = $("#pichArea tbody:first");
	var lastTr = tbody.children(':last');
	var trObj = $('<tr></tr>').html(lastTr.html());
	trObj.insertBefore(lastTr);
	setUniformCSS(trObj[0]);
}<%--
[삭제] 담당자 --%>
function delPich(){
	var arr = [], tr;
	$("#pichArea tbody:first input[type='checkbox']:checked").each(function(){
		tr = getParentTag(this, 'tr');
		if($(tr).attr('id')!='headerTr'){
			arr.push(tr);
		}
	});
	arr.each(function(index, obj){ $(obj).remove(); });
}<%--
[6개월 추가]--%>
function addMonth(count){
	var trs = $("#manPowerArea tbody:first tr");
	var headTd = $(trs[0]).find('td:last');
	var bodyTd = $(trs[1]).find('td:last');
	
	var no = parseInt(headTd.text().substring(2)), obj;
	var headHtml = headTd.clone().wrapAll("<div/>").parent().html();
	var bodyHtml = bodyTd.clone().wrapAll("<div/>").parent().html();
	var conHtml='', devHtml='', allHtml='';
	
	for(var i=0; i<trs.length; i++){
		if($(trs[i]).attr('data-role1Cd') == 'conSum'){
			conHtml = $(trs[i]).find('td:last').clone().wrapAll("<div/>").parent().html();
		} else if($(trs[i]).attr('data-role1Cd') == 'devSum'){
			devHtml = $(trs[i]).find('td:last').clone().wrapAll("<div/>").parent().html();
		} else if($(trs[i]).attr('data-role1Cd') == 'allSum'){
			allHtml = $(trs[i]).find('td:last').clone().wrapAll("<div/>").parent().html();
		}
	}
	
	var html, role1Cd;
	for(var i=0; i<trs.length; i++){
		
		if(i==0){
			for(var j=0; j<count; j++){
				obj = $(headHtml);
				obj.text('M+'+(no+j+1));
				$(trs[i]).append(obj);
			}
		} else {
			html=null, role1Cd=null;
			if(i==0){
				html = headHtml;
			} else if($(trs[i]).attr('data-role1Cd') == 'conSum'){
				html = conHtml;
				role1Cd = 'con';
			} else if($(trs[i]).attr('data-role1Cd') == 'devSum'){
				html = devHtml;
				role1Cd = 'dev';
			} else if($(trs[i]).attr('data-role1Cd') == 'allSum'){
				html = allHtml;
				role1Cd = 'all';
			}
			
			if(html != null){
				
				for(var j=0; j<count; j++){
					obj = $(bodyHtml);
					obj.attr('id', role1Cd+'M'+(no+j+1));
					obj.text('');
					$(trs[i]).append(obj);
				}
			} else {
				
				for(var j=0; j<count; j++){
					obj = $(bodyHtml);
					obj.find("input").attr('name', 'M'+(no+j+1));
					obj.find("input").val('');
					$(trs[i]).append(obj);
				}
			}
		}
	}
}<%--
[6개월 삭제]--%>
function delMonth(count){
	var trs = $("#manPowerArea tbody:first tr");
	var headTxt = $(trs[0]).find('td:last').text();
	if(!headTxt.startsWith('M+')) return;
	if(parseInt(headTxt.substring(2))<=6) return;
	
	var prevTd, lastTd;
	trs.each(function(){
		lastTd = $(this).find('td:last');
		for(var i=0;i<count;i++){
			prevTd = lastTd.prev();
			lastTd.remove();
			lastTd = prevTd;
		}
	});
}<%--
[기능] 숫자만 입력 가능 --%>
function checkNumberOnly(event, obj){
	var cd = event.keyCode, ck = event.ctrlKey;
	if(		(cd==37 || cd==38 || cd==39 || cd==40 || cd==190 || cd==110 || cd==8 || cd==46) || // left,right,up,down,point,del,back-del
			(ck && (cd==65 || cd==67 || cd==86 || cd==88 || cd==90)) //ctrl+a,c,v,x,z
		){
		return;
	}
	if((cd<48 || cd>57) && (cd<96 || cd>105)){
		event.preventDefault();
	}
}<%--
[기능] 숫자만 두고 나머지 제거 - ctrl+v 의 경우 처리 --%>
function makeNumberOnly(obj){
	var va=obj.value, arr=[], i, size = va.length, hasPoint=false, c;
	for(i=0;i<size;i++){
		c = va.charAt(i);
		if(c>='0' && c<='9'){
			arr.push(c);
		} else if(c=='.' && !hasPoint) {
			hasPoint = true;
			if(i==0) arr.push(0);
			arr.push(c);
		}
	}
	if(!hasPoint && arr.length>0) arr.push('.0');
	obj.value=arr.join('');
	calcSum(obj);
}<%--
[기능] 라인 합계, 소계, 총계 계산 --%>
function calcSum(obj){
	var tr = $(getParentTag(obj, 'tr')), va, sum=0.0;<%--
	(가로 합계 계산) --%>
	$(tr).find("input[name^='M']").each(function(){
		va = $(this).val();
		if(va!='' && !isNaN(va)){
			sum += parseFloat(va);
		}
	});
	$(tr).find('#sumTd').text(sum==0 ? '' : parseInt(sum) == sum ? sum+'.0' : sum);
	$(tr).find('input[name=mpMmSum]').val(sum);
	<%--
	
	(컨설팅 소계, 개발 소계, 총계 - 계산) --%>
	var nm = $(obj).attr("name");
	var conSum=0.0, devSum=0.0, role1Cd;
	var tbody = $("#manPowerArea tbody:first");
	tbody.find("input[name='"+nm+"']:visible").each(function(){
		role1Cd = $(getParentTag(this, 'tr')).attr('data-role1Cd');
		if(role1Cd == 'con'){
			va = $(this).val();
			if(va!='' && !isNaN(parseFloat(va))){
				conSum += parseFloat(va);
			}
		} else if(role1Cd == 'dev'){
			va = $(this).val();
			if(va!='' && !isNaN(parseFloat(va))){
				devSum += parseFloat(va);
			}
		}
	});
	
	tbody.children("[data-role1Cd=conSum]").find("#sum"+nm).text(conSum==0 ? '' : parseInt(conSum) == conSum ? conSum+'.0' : conSum);
	tbody.children("[data-role1Cd=devSum]").find("#sum"+nm).text(devSum==0 ? '' : parseInt(devSum) == devSum ? devSum+'.0' : devSum);
	tbody.children("[data-role1Cd=allSum]").find("#sum"+nm).text((conSum+devSum)==0 ? '' : parseInt(conSum+devSum) == (conSum+devSum) ? (conSum+devSum)+'.0' : (conSum+devSum));<%--
	
	합계의 (컨설팅 소계, 개발 소계, 총계 - 계산) --%>
	conSum=0.0, devSum=0.0;
	tbody.find("td[id='sumTd']:visible").each(function(){
		role1Cd = $(getParentTag(this, 'tr')).attr('data-role1Cd');
		if(role1Cd == 'con'){
			va = $(this).text().trim();
			if(va!='' && !isNaN(va)){
				conSum += parseFloat(va);
			}
		} else if(role1Cd == 'dev'){
			va = $(this).text().trim();
			if(va!='' && !isNaN(va)){
				devSum += parseFloat(va);
			}
		}
	});
	
	tbody.children("[data-role1Cd=conSum]").find("#sumTd").text(conSum==0 ? '' : parseInt(conSum) == conSum ? conSum+'.0' : conSum);
	tbody.children("[data-role1Cd=devSum]").find("#sumTd").text(devSum==0 ? '' : parseInt(devSum) == devSum ? devSum+'.0' : devSum);
	tbody.children("[data-role1Cd=allSum]").find("#sumTd").text((conSum+devSum)==0 ? '' : parseInt(conSum+devSum) == (conSum+devSum) ? (conSum+devSum)+'.0' : (conSum+devSum));
}<%--
[프로잭트 그룹] 추가/변경/삭제 --%>
function manageGrp(mode){
	if(mode=='add'){
		dialog.open("setPrjGrpDialog", '<u:msg titleId="wp.prjGrp" alt="프로잭트 그룹"  />', "./setPrjGrpPop.do?menuId=${menuId}&cat=${param.cat}");
	} else {
		var selObj = $("#prjArea #grpId");
		var grpId = selObj.val();
		if(grpId=='') return;
		if(mode=='mod'){
			dialog.open("setPrjGrpDialog", '<u:msg titleId="wp.prjGrp" alt="프로잭트 그룹"  />', "./setPrjGrpPop.do?menuId=${menuId}&cat=${param.cat}&grpId="+grpId);
		} else if(mode=='del'){
			callAjax('./transPrjGrpDelAjx.do?menuId=${menuId}&cat=${param.cat}', {grpId:grpId}, function(data) {
				if (data.message != null) {
					alert(data.message);
				}
				if (data.result == 'ok') {
					selObj.find('option[value='+grpId+']').remove();
					selObj.attr('selectedIndex',0);
					selObj.uniform.update();
				}
			});
		}
	}
}<%--
프로잭트 그룹 - 재설정 --%>
function reloadPrjGrp(grpId, arr){
	var selObj = $("#prjArea #grpId");
	selObj.find('option').each(function(){
		if($(this).val()!='') $(this).remove();
	});
	var i, obj, opObj;
	for(i=0; i<arr.length; i++){
		obj = arr[i];
		opObj = $('<option></option>');
		opObj.val(obj.grpId);
		opObj.text(obj.grpNm);
		if(grpId==obj.grpId){
			opObj.attr('selected', true);
		}
		selObj.append(opObj);
	}
	selObj.uniform.update();
}<%--
[승인, 반려] : 팝업 열기 --%>
function openProcPop(actCd, opin){
	var popTit = callMsg("wp.modStatCd."+actCd);
	dialog.open("setProcDialog", popTit, "./setProcPop.do?menuId=${menuId}&cat=${param.cat}&actCd="+actCd+(opin!=null ? '&opin='+opin : ''));
}<%--
[승인, 반려] : 처리--%>
function processAct(actCd, rejtRson){
	callAjax('./transProcessActAjx.do?menuId=${menuId}&cat=${param.cat}', {prjNo:'${wpPrjBVo.prjNo}', actCd:actCd, rejtRson:rejtRson}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			if(actCd=='cancel'){
				location.replace('./listPrj.do?menuId=${menuId}&cat=askApv');
			} else {
				location.replace('./listPrj.do?menuId=${menuId}&cat=${param.cat}');
			}
		}
	});
}<%--
[승인요청 변경] 프로잭트 코드,명,기간,금애,인원 변경 가능 하도록 --%>
function setForAskApv(){
	
	$area = $("#prjArea");
	$area.find("#prjCd, #prjNm, #prjAmt, #strtYmd, #endYmd").each(function(){
		setReadonly($(this), false);
	});
	$area.find("#strtYmdArea, #endYmdArea").find("a").show();
	
	$("#manPowerTitleBtnArea").show();
	
	$area = $("#manPowerArea");
	$area.find("select").each(function(){
		$(this).attr("onclick", "");
		$(this).attr("onchange", "");
	});
	$area.find("input[type='text']").each(function(){
		setReadonly($(this), false);
	});
	
	$("#btnSave, #btnSetForAskApv").hide();
	$("#btnAskApv").show();
}<%--
[버전 변경] : 버전탭 클릭 --%>
function viewVersion(ver){
	var maxVer = '${verWpPrjBVoList[0].ver}';
	location.href = "./viewPrj.do?menuId=${menuId}&cat=${param.cat}&prjNo=${wpPrjBVo.prjNo}"+(maxVer!=ver ? ('&ver='+ver) : '');
}<%--
[외주 공수 대리 입력]--%>
function openCoverOuts(){
	dialog.open("setCoverOutsDialog", '<u:msg titleId="wp.btn.coverOuts" alt="외주 공수 대리 입력" />', "./setCoverOutsPop.do?menuId=${menuId}&cat=${param.cat}&prjNo=${wpPrjBVo.prjNo}");
}
$(document).ready(function() {
	setUniformCSS($("#prjArea")[0]);
	$("#pichArea tbody:first").children(':visible').each(function(){
		setUniformCSS(this);
	});
	$("#manPowerArea tbody:first").children(':visible').each(function(){
		setUniformCSS(this);
	});
	setUniformCSS($("#attachArea")[0]);
});
//-->
</script><c:if
	test="${empty viewMode}">
<u:title alt="프로잭트 등록/프로잭트 수정"
	titleId="${empty wpPrjBVo ? 'wp.regPrj' : 'wp.modPrj' }" menuNameFirst="false" /></c:if><c:if
	test="${not empty viewMode}">
<u:title alt="프로잭트 관리 등" menuNameFirst="true" /></c:if><u:set
	test="${not (empty wpPrjBVo.modStatCd
				or wpPrjBVo.modStatCd eq 'temp'
				or (param.cat eq 'askApv' and (wpPrjBVo.modStatCd eq 'rejt' or wpPrjBVo.modStatCd eq 'cancel'))
				or (param.cat eq 'allPrj' and sessionScope.userVo.userUid eq 'U0000001')
			) }" var="noChange" value="Y" />

<c:if test="${not empty viewMode and not empty verWpPrjBVoList }">
<u:tabGroup>
	<c:forEach var="verVo" items="${verWpPrjBVoList}" varStatus="status">
		<u:tab alt="5.0, 4.0, ..." title="${verVo.ver}"
			on="${verVo.ver == wpPrjBVo.ver}"
			onclick="viewVersion('${verVo.ver}');" />
	</c:forEach>
</u:tabGroup>
</c:if>

<form id="prjManageFrm" enctype="multipart/form-data" method="post"><c:if
	test="${empty viewMode and not empty wpPrjBVo}">
<input type="hidden" name="prjNo" value="${wpPrjBVo.prjNo}" /></c:if>
<input type="hidden" id="modStatCd" name="modStatCd" value="${wpPrjBVo.modStatCd}" />
<u:listArea id="prjArea" colgroup="13%,37%,13%,37%">
<tr>
	<td class="head_ct"><u:msg titleId="wp.prjCd" alt="프로잭트 코드" /><c:if test="${empty viewMode}"><u:mandatory /></c:if></td>
	<td class="${empty viewMode ? '' : 'body_lt'}"><u:input id="prjCd" name="prjCd" maxByte="30" type="${empty viewMode ? '' : 'view'}"
		value="${wpPrjBVo.prjCd}" titleId="wp.prjCd" mandatory="Y"
		readonly="${not empty noChange ? 'Y' : ''}" /></td>
	<td class="head_ct"><u:msg titleId="wp.prjNm" alt="프로잭트 명" /><c:if test="${empty viewMode}"><u:mandatory /></c:if></td>
	<td class="${empty viewMode ? '' : 'body_lt'}"><u:input id="prjNm" name="prjNm" maxByte="100" type="${empty viewMode ? '' : 'view'}"
		value="${wpPrjBVo.prjNm}" titleId="wp.prjNm" mandatory="Y"
		readonly="${not empty noChange ? 'Y' : ''}" style="width:96%" /></td>
</tr>
<tr>
	<td class="head_ct"><u:msg titleId="wp.prjAmt" alt="수주 금액"  /><c:if test="${empty viewMode}"><u:mandatory /></c:if></td>
	<td class="${empty viewMode ? '' : 'body_lt'}"><u:input id="prjAmt" value="${wpPrjBVo.prjAmt}" titleId="wp.prjAmt" valueOption="number" maxLength="16"
		onfocus="this.value = removeComma(this.value)" type="${empty viewMode ? '' : 'view'}"
		onblur="this.value = addComma(this.value)" mandatory="Y"
		readonly="${not empty noChange ? 'Y' : ''}"/><span id="prjAmtDisp" style="padding-left:10"></span></td>
	<td class="head_ct"><u:msg titleId="wp.prjGrp" alt="프로잭트 그룹"  /></td>
	<td class="${empty viewMode ? '' : 'body_lt'}"><c:if
			test="${empty viewMode}"><table border="0" cellpadding="0" cellspacing="0"><tr>
		<td><select name="grpId" id="grpId">
			<u:option value="" titleId="cm.option.noSelect" alt="선택안함" /><c:forEach
				items="${wpPrjGrpBVoList}" var="wpPrjGrpBVo">
			<u:option value="${wpPrjGrpBVo.grpId}" title="${wpPrjGrpBVo.grpNm}" checkValue="${wpPrjBVo.grpId}" /></c:forEach></select></td>
		<td style="padding-left:6px"></td>
		<td><u:buttonS titleId="cm.btn.add" alt="추가" onclick="manageGrp('add')" /></td>
		<td><u:buttonS titleId="cm.btn.chg" alt="변경" onclick="manageGrp('mod')" /></td>
		<td><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="manageGrp('del')" /></td>
		</tr></table></c:if><c:if
			test="${not empty viewMode}"><c:forEach
				items="${wpPrjGrpBVoList}" var="wpPrjGrpBVo"><c:if
					test="${wpPrjGrpBVo.grpId eq wpPrjBVo.grpId}">${wpPrjGrpBVo.grpNm}</c:if></c:forEach></c:if></td>
</tr>
<tr>
	<td class="head_ct"><u:msg titleId="wp.pred" alt="기간" /><c:if test="${empty viewMode}"><u:mandatory /></c:if></td>
	<td class="${empty viewMode ? '' : 'body_lt'}"><c:if
			test="${empty viewMode}">
		<table border="0" cellpadding="0" cellspacing="0"><tr>
		<td>
		<u:calendar
				titleId="cm.cal.startDd" alt="시작일자"
				id="strtYmd" value="${wpPrjBVo.strtYmd}" option="{end:'endYmd'}" mandatory="Y"
				readonly="${not empty noChange ? 'Y' : ''}" /></td>
		<td style="padding-left:8px; padding-right:5px;">~</td>
		<td><u:calendar
				titleId="cm.cal.endDd" alt="종료일자"
				id="endYmd" value="${wpPrjBVo.endYmd}" option="{start:'strtYmd'}" mandatory="Y"
				readonly="${not empty noChange ? 'Y' : ''}" /></td>
		</tr></table></c:if><c:if
			test="${not empty viewMode}"><u:out
				value="${wpPrjBVo.strtYmd}" type="shortdate"/> ~ <u:out
				value="${wpPrjBVo.endYmd}" type="shortdate"/></c:if></td>
	<td class="head_ct"><u:msg titleId="wp.endDt" alt="종료일" /></td>
	<td class="${empty viewMode ? '' : 'body_lt'}"><c:if
			test="${empty viewMode}"><u:calendar
				titleId="wp.endDt" alt="종료일"
				id="cmplYmd" value="${wpPrjBVo.cmplYmd}" /></c:if><c:if
			test="${not empty viewMode}"><u:out
				value="${wpPrjBVo.cmplYmd}" type="shortdate"/></c:if></td>
</tr>
<tr>
	<td class="head_ct"><u:msg titleId="wp.summary" alt="개요" /></td>
	<td class="${empty viewMode ? '' : 'body_lt'}" colspan="3"><u:textarea id="smry" value="${wpPrjBVo.smry}" titleId="wp.summary" rows="3"
		style="width:98.2%" type="${empty viewMode ? '' : 'view'}" /></td>
</tr>
<tr>
	<td class="head_ct"><u:msg titleId="wp.custNm" alt="고객명" /></td>
	<td class="${empty viewMode ? '' : 'body_lt'}" colspan="3"><u:input name="custNm" value="${wpPrjBVo.custNm}" titleId="wp.custNm" maxByte="60" type="${empty viewMode ? '' : 'view'}" /></td>
</tr>
<tr>
	<td class="head_ct"><u:msg titleId="cols.custAdr" alt="고객주소" /></td>
	<td class="${empty viewMode ? '' : 'body_lt'}" colspan="3"><u:address id="cust" alt="고객주소" adrStyle="width:98.2%" zipNoValue="${wpPrjBVo.custZipNo }" adrValue="${wpPrjBVo.custAdr }" readonly="Y"
		 type="${empty viewMode ? '' : 'view'}" /></td>
</tr><c:if

	test="${not empty viewMode and not empty wpPrjBVo.modStatCd and wpPrjBVo.modStatCd ne 'apvd'}">
<tr>
	<td class="head_ct"><u:msg titleId="wp.verStat" alt="버전 상태" /></td>
	<td class="body_lt" colspan="3"><u:msg titleId="wp.modStatCd.${wpPrjBVo.modStatCd}" /></td>
</tr></c:if>
</u:listArea>

<u:title titleId="wp.pich" alt="담당자" type="small" ><c:if
	test="${empty viewMode}">
<u:titleButton titleId="cm.btn.add" alt="추가" onclick="addPich();"/>
<u:titleButton titleId="cm.btn.del" alt="삭제" onclick="delPich();"/></c:if></u:title>

<u:listArea id="pichArea" colgroup="${empty viewMode ? '3%,14%,14%,14%,20%,35%' : '14%,14%,14%,20%,38%'}">
<tr id="headerTr"><c:if
		test="${empty viewMode}">
	<td class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('pichArea', this.checked);" value=""/></td></c:if>
	<td class="head_ct"><u:msg titleId="cols.nm" alt="이름" /><c:if test="${empty viewMode}"><u:mandatory /></c:if></td>
	<td class="head_ct"><u:msg titleId="cols.grade" alt="직급" /></td>
	<td class="head_ct"><u:msg titleId="cols.phon" alt="전화번호" /></td>
	<td class="head_ct"><u:msg titleId="cols.email" alt="이메일" /></td>
	<td class="head_ct"><u:msg titleId="cols.note" alt="비고" /></td>
</tr><c:if test="${fn:length(wpPrjPichBVoList)==0}" >
<tr>
	<td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
</tr>
</c:if><c:forEach items="${wpPrjPichBVoList}" var="wpPrjPichBVo" varStatus="status" >
<tr style="${empty viewMode and status.last ? 'display:none' : ''}"><c:if
		test="${empty viewMode}">
	<td class="bodybg_ct"><input type="checkbox" /></td></c:if>
	<td class="${empty viewMode ? '' : 'body_ct'}"><u:input id="pichNm" titleId="cols.nm" value="${wpPrjPichBVo.pichNm}" style="width:90%" maxByte="30" type="${empty viewMode ? '' : 'view'}" /></td>
	<td class="${empty viewMode ? '' : 'body_ct'}"><u:input id="pichGrade" titleId="cols.grade" value="${wpPrjPichBVo.pichGrade}" style="width:90%" maxByte="50" type="${empty viewMode ? '' : 'view'}" /></td>
	<td class="${empty viewMode ? '' : 'body_ct'}"><u:input id="pichPhon" titleId="cols.phon" value="${wpPrjPichBVo.pichPhon}" style="width:90%" maxByte="50" type="${empty viewMode ? '' : 'view'}" /></td>
	<td class="${empty viewMode ? '' : 'body_ct'}"><u:input id="pichEmail" titleId="cols.email" value="${wpPrjPichBVo.pichEmail}" style="width:93%" maxByte="100" type="${empty viewMode ? '' : 'view'}" /></td>
	<td class="${empty viewMode ? '' : 'body_lt'}"><u:input id="note" titleId="cols.note" value="${wpPrjPichBVo.note}" style="width:95.5%" type="${empty viewMode ? '' : 'view'}" /></td>
</tr></c:forEach>
</u:listArea>

<u:title titleId="wp.manPowerIn" alt="투입 인력" type="small" id="manPowerTitle" hideButtons="${not empty noChange ? true : false}"><c:if
	test="${empty viewMode}">
<u:titleButton titleId="wp.btn.conEmp" alt="컨설팅 임직원" onclick="setManPower('con', 'emp');"/>
<u:titleButton titleId="wp.btn.conOut" alt="컨설팅 외주" onclick="setManPower('con', 'out');"/>
<u:titleButton titleId="wp.btn.devEmp" alt="개발 임직원" onclick="setManPower('dev', 'emp');"/>
<u:titleButton titleId="wp.btn.devOut" alt="개발 외주" onclick="setManPower('dev', 'out');"/>
<u:titleButton titleId="cm.btn.del" alt="삭제" onclick="delManPower();"/>
<u:titleButton titleId="wp.btn.add3m" alt="3개월 추가" onclick="addMonth(3);"/>
<u:titleButton titleId="wp.btn.del3m" alt="3개월 삭제" onclick="delMonth(3);"/></c:if></u:title>

<div style="overflow-y:show; overflow-x:auto; width:100%">
<u:listArea id="manPowerArea" tableStyle="width:${517 -(empty viewMode ? 0 : 60) + 36 + maxMCount * 50}px; table-layout: fixed">
<tr><c:if
		test="${empty viewMode}">
	<td class="head_bg" style="width:27px"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('manPowerArea', this.checked);" value=""/></td></c:if>
	<td class="head_ct" style="width:80px"><u:msg titleId="wp.prjRole1Cd" alt="역할분류" /></td>
	<td class="head_ct" style="width:${empty viewMode ? '190' : '130'}px"><u:msg titleId="wp.prjRole1Cd" alt="역할" /></td>
	<td class="head_ct" style="width:80px"><u:msg titleId="cm.gubun" alt="구분" /></td>
	<td class="head_ct" style="width:80px"><u:msg titleId="cols.nm" alt="이름" /></td>
	<td class="head_ct" style="width:60px"><u:msg titleId="wp.sum" alt="합계" /></td><c:forEach
		begin="1" end="${maxMCount}" step="1" var="mNo" varStatus="status">
	<td class="head_ct" style="width:50px">M+${mNo}</td>
	</c:forEach>
</tr><c:forEach items="${role1Cds}" var="cd1"><c:if

	test="${cd1 eq 'con' or cd1 eq 'dev'}"><u:convert
		srcId="${cd1}WpPrjMpPlanDVoList" var="wpPrjMpPlanDVoList" /><c:forEach
		items="${wpPrjMpPlanDVoList}" var="wpPrjMpPlanDVo" varStatus="trStatus">
<tr data-role1Cd="${cd1}" style="${empty viewMode and trStatus.last ? 'display:none;' : ''}"><c:if
		test="${empty viewMode}">
	<td class="bodybg_ct" style="width:27px"><input type="checkbox" value="" /><input
		type="hidden" name="mpId" value="${wpPrjMpPlanDVo.mpId}" /><input
		type="hidden" name="mpTypCd" value="${wpPrjMpPlanDVo.mpTypCd}" /><input
		type="hidden" name="prjRole1Cd" value="${wpPrjMpPlanDVo.prjRole1Cd}" /><input
		type="hidden" name="mpMmSum" value="${wpPrjMpPlanDVo.mpMmSum}" /></td></c:if>
	<td class="body_ct" style="width:80px" id="role1CdTd"><c:if
		test="${not empty wpPrjMpPlanDVo.prjRole1Cd}"><u:msg
			titleId="wp.prjRole1Cd.${wpPrjMpPlanDVo.prjRole1Cd}" /></c:if></td>
	<td class="body_ct" style="width:190px" id="role2CdTd"><c:if
		test="${empty viewMode}"><u:convert
			srcId="${cd1}Role2Cds" var="roleCd2"/><select name="prjRole2Cd" onclick="${
				not empty noChange ? 'noChangeSelect(this)' : ''}" onchange="${
				not empty noChange ? 'noChangeSelect(this)' : ''}"><c:forEach
			items="${roleCd2}" var="cd2">
		<u:option value="${cd2}" titleId="wp.prjRole2Cd.${cd2}"
			checkValue="${not empty wpPrjMpPlanDVo.prjRole2Cd ? wpPrjMpPlanDVo.prjRole2Cd : 
				cd1 eq 'con' ? 'a11' : 'd10'}" /></c:forEach></select></c:if><c:if
		test="${not empty viewMode and not empty wpPrjMpPlanDVo.prjRole2Cd}"><u:msg titleId="wp.prjRole2Cd.${wpPrjMpPlanDVo.prjRole2Cd}" /></c:if></td>
	<td class="body_ct" style="width:80px" id="mpTypCdTd"><c:if
		test="${not empty wpPrjMpPlanDVo.mpTypCd}"><u:msg
			titleId="wp.mpTypCd.${wpPrjMpPlanDVo.mpTypCd}" /></c:if></td>
	<td class="body_ct" style="width:80px" id="mpNmTd"><u:out value="${wpPrjMpPlanDVo.mpNm}" /></td>
	<td class="body_ct" style="width:60px" id="sumTd"><u:out value="${wpPrjMpPlanDVo.mpMmSum}" /></td><u:convertMap
		srcId="wpPrjMpPlanLVoListMap" attId="${wpPrjMpPlanDVo.mpId}" var="wpPrjMpPlanLVoList" /><c:forEach
		begin="1" end="${maxMCount}" step="1" var="mNo" varStatus="status">
	<td class="body_ct" style="width:50px"><c:if
		test="${empty viewMode}"><input name="M${status.index}" value="${wpPrjMpPlanLVoList[mNo-1].planMm}" type="text"
		maxlength="5" style="width:35px; text-align:center;" ${not empty noChange and not trStatus.last ? 'readonly="readonly"' : ''} class="${not empty noChange and not trStatus.last ? 'input_disabled' : ''}"
		onkeydown="checkNumberOnly(event, this)" onchange="makeNumberOnly(this)" /></c:if><c:if
		test="${not empty viewMode}">${wpPrjMpPlanLVoList[mNo-1].planMm}</c:if></td>
	</c:forEach>
</tr></c:forEach></c:if>
<tr data-role1Cd="${cd1}Sum"><u:convert
		srcId="${cd1}Sum" var="sumArray"/>
	<td class="body_ct" colspan="${empty viewMode ? 5 : 4}" style="background-color:${empty viewMode ? '' : cd1 eq 'all' ? color2 : color1
		};"><u:msg titleId="wp.${cd1}Sum" alt="컨설팅 소계"/></td>
	<td class="body_ct" id="sumTd" style="width:60px; background-color:${empty viewMode ? '' : cd1 eq 'all' ? color2 : color1
		};">${sumArray[0] eq 0 ? '' : sumArray[0]}</td><c:forEach
		begin="1" end="${maxMCount}" step="1" var="mNo" varStatus="status">
	<td class="body_ct" id="sumM${status.index}" style="width:50px; background-color:${empty viewMode ? '' : cd1 eq 'all' ? color2 : color1
		};">${sumArray[mNo] eq 0 ? '' : sumArray[mNo]}</td>
	</c:forEach>
</tr></c:forEach>
</u:listArea>
</div>

<div class="blank"></div>

<u:title alt="첨부파일" titleId="cm.ico.att" type="small" menuNameFirst="false" />
<u:listArea id="attachArea">
<tr>
<td>
	<u:files id="wpfiles" fileVoList="${fileVoList}" module="wp" mode="${empty viewMode ? 'set' : 'view'}"
		exts="${exts}" extsTyp="${extsTyp}" actionParam="${wpPrjBVo.prjNo}" height="55" urlParam="cat=${param.cat}"/>
</td>
</tr>
</u:listArea>

<div class="blank"></div>

<c:if test="${not empty viewMode and (
	not empty wpPrjBVo.modCont
	or (not empty wpPrjBVo.rejtRson and 
		(wpPrjBVo.modStatCd eq 'apvd' or wpPrjBVo.modStatCd eq 'rejt')))}">
<u:title alt="의견" titleId="ap.doc.opin" type="small" menuNameFirst="false" />
<u:listArea id="prjArea" colgroup="13%,87%"><c:if
	test="${not empty wpPrjBVo.modCont}">
<tr>
	<td class="head_ct"><u:msg titleId="wp.opinAskApv" alt="변경 내용 및 사유" /></td>
	<td class="body_lt"><u:out value="${wpPrjBVo.modCont}" /></td>
</tr></c:if><c:if
	test="${not empty wpPrjBVo.rejtRson and 
		(wpPrjBVo.modStatCd eq 'apvd' or wpPrjBVo.modStatCd eq 'rejt')}">
<tr>
	<td class="head_ct"><c:if
		test="${wpPrjBVo.modStatCd eq 'apvd'}"><u:msg titleId="wp.opinApvd" alt="승인 의견" /></c:if><c:if
		test="${wpPrjBVo.modStatCd eq 'rejt'}"><u:msg titleId="wp.opinRejt" alt="반려 의견" /></c:if></td>
	<td class="body_lt"><u:out value="${wpPrjBVo.rejtRson}" /></td>
</tr></c:if>
</u:listArea>
<div class="blank"></div>
</c:if>

<div id="prjOpinArea" style="display:none;"><c:if
		test="${not empty wpPrjBVo.modCont and wpPrjBVo.modStatCd ne 'apvd'}">
<input type="hidden" name="modCont" value="${wpPrjBVo.modCont}" /></c:if></div>

<u:secu auth="A"><u:set test="${true}" var="isAdm" value="Y" /></u:secu>
<u:buttonArea><c:if
	test="${empty viewMode}"><%-- [수정 모드] --%><c:if
		test="${empty wpPrjBVo or wpPrjBVo.modStatCd eq 'temp'}"><%-- [신규 프로잭트, 임시저장 프로잭트] --%>
	<u:button titleId="cm.btn.tmpSave" alt="임시저장" onclick="savePrj('temp')" />
	<u:button titleId="wp.btn.askApv" alt="승인요청" onclick="openProcPop('askApv', 'N')" /><c:if
		test="${wpPrjBVo.modStatCd eq 'temp'}">
	<u:button titleId="cm.btn.del" alt="삭제" onclick="processConfirm('del')" /></c:if></c:if><c:if
	
		test="${param.cat eq 'prj' and wpPrjBVo.pmId eq sessionScope.userVo.userUid}"><%-- [프로잭트관리] --%>
	<u:button titleId="cm.btn.save" alt="저장" onclick="savePrj('apvd')" id="btnSave" />
	<u:button titleId="wp.btn.modForApv" alt="승인요청 변경" onclick="setForAskApv()" id="btnSetForAskApv" />
	<u:button titleId="wp.btn.askApv" alt="승인요청" onclick="openProcPop('askApv')" id="btnAskApv" style="display:none;" />
	</c:if><c:if
	
		test="${param.cat eq 'askApv' and (wpPrjBVo.modStatCd eq 'rejt' or wpPrjBVo.modStatCd eq 'cancel')
			and wpPrjBVo.pmId eq sessionScope.userVo.userUid}"><%-- [승인요청] --%>
	<u:button titleId="wp.btn.askApv" alt="승인요청" onclick="openProcPop('askApv')" id="btnAskApv" />
	</c:if><c:if
	
		test="${param.cat eq 'allPrj' and sessionScope.userVo.userUid eq 'U0000001'}"><%-- [프로잭트 관리(전체)] --%>
	<u:button titleId="cm.btn.save" alt="저장" onclick="savePrj('apvd')" id="btnSave" /></c:if><c:if
	
	
		test="${empty wpPrjBVo}">
	<u:button titleId="cm.btn.cancel" alt="취소" href="./listPrj.do?menuId=${menuId}&cat=${param.cat}" /></c:if><c:if
		test="${not empty wpPrjBVo}">
	<u:button titleId="cm.btn.cancel" alt="취소" href="./viewPrj.do?menuId=${menuId}&cat=${param.cat}&prjNo=${wpPrjBVo.prjNo}" /></c:if>
</c:if><c:if
	test="${not empty viewMode}"><%-- [조회 모드] --%><c:if
	
		test="${param.cat eq 'apv' and not empty isAdm}"><%-- [승인대기함] --%>
	<u:button titleId="cm.btn.apvd" alt="승인" onclick="openProcPop('apvd')" />
	<u:button titleId="cm.btn.rjt" alt="반려" onclick="openProcPop('rejt')" /></c:if><c:if
	
	
		test="${param.cat eq 'askApv' and wpPrjBVo.pmId eq sessionScope.userVo.userUid}"><%-- [승인요청함] --%><c:if
			test="${wpPrjBVo.modStatCd eq 'askApv'}">
	<u:button titleId="wp.btn.cancelApv" alt="승인요청 취소" onclick="processConfirm('cancel')" /></c:if><c:if
			test="${wpPrjBVo.modStatCd eq 'rejt' or wpPrjBVo.modStatCd eq 'cancel'}"><c:if
			test="${verWpPrjBVoList[0].ver eq wpPrjBVo.ver}"><%-- [반려 상태, 승인요청 취소 상태] --%>
	<u:button titleId="cm.btn.mod" alt="수정" href="./setPrj.do?menuId=${menuId}&cat=${param.cat}&prjNo=${wpPrjBVo.prjNo}" /><c:if
		test="${verWpPrjBVoList[0].ver eq '1.0'}">
	<u:button titleId="cm.btn.del" alt="삭제" onclick="processConfirm('del')" /></c:if><c:if
		test="${verWpPrjBVoList[0].ver ne '1.0'}">
	<u:button titleId="dm.cols.auth.verDel" alt="버전삭제" onclick="processConfirm('delVer')" /></c:if></c:if></c:if></c:if><c:if
	
	
		test="${param.cat eq 'prj' and wpPrjBVo.pmId eq sessionScope.userVo.userUid}"><%-- [프로잭트관리] --%><c:if
			test="${not empty hasOuts}">
		<u:button titleId="wp.btn.coverOuts" alt="외주 공수 대리 입력" onclick="openCoverOuts()" /></c:if><c:if
			test="${verWpPrjBVoList[0].ver eq wpPrjBVo.ver}">
	<u:button titleId="cm.btn.mod" alt="수정" href="./setPrj.do?menuId=${menuId}&cat=${param.cat}&prjNo=${wpPrjBVo.prjNo}" /></c:if></c:if><c:if
	
	
		test="${param.cat eq 'allPrj'}"><%-- [프로잭트 관리(전체)] --%><c:if
			test="${verWpPrjBVoList[0].ver eq wpPrjBVo.ver and sessionScope.userVo.userUid eq 'U0000001'}">
	<u:button titleId="cm.btn.mod" alt="수정" href="./setPrj.do?menuId=${menuId}&cat=${param.cat}&prjNo=${wpPrjBVo.prjNo}" auth="A" /></c:if></c:if>
	
	<u:button titleId="cm.btn.close" alt="닫기" href="./listPrj.do?menuId=${menuId}&cat=${param.cat}" />
</c:if>
</u:buttonArea>

</form>