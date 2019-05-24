<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<u:set test="${!empty fnc}" var="fnc" value="${fnc}" elseValue="reg" />

<script type="text/javascript">
<!--
 
function ctCrtCancel(){
	callAjax('./ajaxCtCrtCancel.do?menuId=${menuId}&fnc=${fnc}', {menuId:"${menuId}", ctId:"${ctId}"} , function(data){
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ct') {
			//strtDt:strtDt, endDt:endDt, dateSelect:dateSelect  fncMng
			location.href = data.menuUrl;
		}else{
			location.href = data.menuUrl;
		}
	});
	
}


function subjNmChange(){
	$("#ctSubjResult").val("N");
}


function ctIdChk(){
	var subjArr = [];
	var subjLangCd = [];
	var emptyFlag = 0;
	var emptysubjLangCd = [];
	var subjNm = "";
	if("${fnc}" == 'reg'){
		subjNm = "ctSubjRescNm_";
	}else if("${fnc}" == 'mod'){
		subjNm = "rescVa_";
	}
	
	var rescId = $("#rescId").val();
	
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			
			if($("#"+ subjNm + "${langTypCdVo.cd}").val() != ""){
				subjArr.push($("#"+ subjNm + "${langTypCdVo.cd}").val());
				subjLangCd.push("${langTypCdVo.rescNm}");	
			}else{
				emptysubjLangCd.push("${langTypCdVo.rescNm}");
				emptyFlag += 1;
			}
			
		</c:forEach>
		if(emptyFlag == 0){
			callAjax('./transCtSubjRescNmChk.do?menuId=${menuId}&fnc=${fnc}', {subjArr:subjArr, subjLangCd:subjLangCd, rescId:rescId }, function(data) {
				if (data.message != null) {
					alert(data.message);
				}
				if (data.result == 'ok') {
					//dialog.close(this);
						//location.href = './listSchdl.do?fncCal=${fncCal}&menuId=${menuId}';
					$("#ctSubjResult").val("Y");
				}else{
					$("#ctSubjResult").val("N");
				}
			});	
		}else{
			alert(' [ ' + '"' + emptysubjLangCd.join('') + '" ] <u:msg titleId="ct.msg.reqs.inMngGrp" alt="메뉴그룹명을 입력해 주십시요."/>');
		}
}

function formSubit(){
	//editor('cmItro').prepare();
	
	//var size=500;
	
	if(validator.validate('setCmForm')){
		var fncVal = "${fnc}";
		if($("#ctSubjResult").val() != "Y"){
			alert("<u:msg titleId="ct.msg.reqs.chkDupCm" alt="커뮤니티명 중복확인을 해주십시요."/>");
		}else{
			//if (isInUtf8Length($('#cmItro').val(), 0, size) > 0) {
			//	alertMsg('cm.input.check.maxbyte', ['<u:msg titleId="cols.cmItro" />',size]);<% // cm.input.check.maxbyte="{0}"의 최대 바이트수 ({1} bytes)를 초과 하였습니다. %>
			//	return;
			//}
			editor('cmItro').prepare();
			
			var $form = $('#setCmForm');
		    $form.attr('method','post');
		    $form.attr('enctype','multipart/form-data');
		    $form.attr('action','./transSetCmFncNextSave.do?menuId=${menuId}&catId=${catId}&fnc='+ fncVal);
		    saveFileToForm('ctfiles', $form[0], null);
		    //$form.submit();

		}

	}

	
}

function infinity(){
	
	$("#attSizeLim").val("INFINITY");
}

<% // TREE %>
function catTreeClick(id, name) {
	if (id.length > 5){
		if($("#"+id).val() != "ROOT" && $('#allCompYn').val()!='Y' ){
			$('#catNm').val(name);
			$('#catId').val(id);
			if(sCatList==null) sCatList={};
			sCatList.name=name;
			sCatList.id=id;
		}
	}
}

$(document).ready(function() {
	var tree = TREE.create('catTree');
	tree.onclick = 'catTreeClick';
	tree.setRoot('ROOT', '<u:msg titleId="ct.msg.reqs.cat" alt="카테고리"/>');
	tree.setSkin("${_skin}");

	<%// tree.add() param : pid, id, name, type, sortOrdr %>
	<c:forEach items="${ctCatFldBVoList}" var="list" varStatus="status" >
		<c:if test="${list.extnOpenYn == 'Y'}">
			tree.add("${list.catPid}","${list.catId}","<u:out value='${list.catNm}' type='script' />","${list.fldTypCd}" == "C"?"D":"F","${list.catOrdr}",null,{pid:"${list.catPid}",id:"${list.catId}",fldNm:"${list.catNm}"});
		</c:if>
	</c:forEach>
	tree.draw();
	<u:set test="${empty catId}" var="selectedNode" value="ROOT" elseValue="${catId}" />
	
	tree.selectTree("${selectedNode}");
	
	
	<% if ("mod".equals(request.getParameter("fnc"))) { %>
	tree.selectTree("${ctEstbBVo.catId}");
	<% } else { %>
	tree.selectTree("ROOT");
	<% } %>
	
	if("${fnc}" == 'mod'){
		$("#ctSubjResult").val("Y");
		$("#mngTgtYn").val("${ctEstbBVo.mngTgtYn}");
		$("#mngTgtYn").trigger('click');
	}else{
		$("#mngTgtYn").val("N");
		$("#mngTgtYn").trigger('click');
	}
	
	if($('#allCompYn').val()=='Y'){
		allCompChange('Y');
	}
	
	<c:if test="${!empty ctEstbBVo && !empty ctEstbBVo.catNm && !empty ctEstbBVo.catId && ctEstbBVo.catId ne 'ROOT'}">
	if(sCatList==null) sCatList={};
	sCatList.name='${ctEstbBVo.catNm}';
	sCatList.id='${ctEstbBVo.catId}';
	</c:if>
	
	setUniformCSS();

});
var sCatList=null;
<% // 전사여부 변경 %>
function allCompChange(val) {
	if(val=='Y'){
		loading('catTree', true, true, 'curtain');
		$('#catNm').val('<u:msg titleId="ct.msg.reqs.cat" alt="카테고리"/>');
		$('#catId').val('ROOT');	
	}else{
		loading('catTree', false, null, 'curtain');
		var catNm='', catId='';
		if(sCatList!=null){
			catNm=sCatList.name;
			catId=sCatList.id;
		}
		$('#catNm').val(catNm);
		$('#catId').val(catId);	
	}	
}

//-->
</script>

<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />
<u:set test="${fnc == 'mod'}" var="catNm" value="프로그래밍" elseValue="" />
<u:set test="${fnc == 'mod'}" var="cmNm" value="JQuery 연구 모임" elseValue="" />
<u:set test="${fnc == 'mod'}" var="cmItro" value="" elseValue="" />
<u:set test="${fnc == 'mod'}" var="kwd" value="Javascript, JQuery, html, css, internet" elseValue="" />
<u:set test="${fnc == 'mod'}" var="attSizeLim" value="10240000" elseValue="" />

<u:title title="${menuTitle }" alt="커뮤니티 개설 신청" menuNameFirst="true"/>
<form id="setCmForm">
<div class="front">
	<div class="front_left">
	<table border="0" cellpadding="0" cellspacing="0">
	<tr>
	<td class="red_stxt">* <u:msg titleId="ct.jsp.setCm.tx01" alt="커뮤니티를 만들기 위한 기본정보들을 입력하십시오" /></td>
	</tr>
	</table>
	</div>
</div>
<input id="ctSubjResult" type="hidden" value="N" />
<input id="ctId" name="ctId"  type="hidden" value="${ctEstbBVo.ctId}" />
<input id="catId" name="catId"  type="hidden" value="${catId}" />
<% // 폼 필드 %>
<div class="listarea">
<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
<colgroup><col width="12%" /><col width="15%" /><col width="15%" /><col width="*" /></colgroup>
	<c:forEach items="${ctCatFldBVoList}" var="list" varStatus="status" >
		<input id="${list.catId}" type="hidden" value="${list.catPid}"/>
	</c:forEach>
	
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.cat" alt="카테고리" /></td>
	<td>
		<u:input id="catNm" value="${ctEstbBVo.catNm}" titleId="cols.cat" readonly="Y" style="width:85%;" mandatory="Y"/>
		<u:input id="catId" name="catId" value="${ctEstbBVo.catId}" type="hidden"/>
	</td>
	
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.cmNm" alt="커뮤니티명" /></td>
	<td id="ctRescArea"><table border="0" cellpadding="0" cellspacing="0">
		<tbody>
			<tr>
				<td id="langTypArea">
					
					<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
						<u:convert srcId="${ctEstbBVo.ctSubjResc}_${langTypCdVo.cd}" var="rescVa" />
						<u:set test="${status.first}" var="style" value="" elseValue="display:none" />
						<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
						<u:set test="${fnc == 'mod'}" var="rescId" value="rescVa_" elseValue="ctSubjRescNm_" />
						<u:set test="${fnc == 'mod'}" var="rescNm" value="rescVa_" elseValue="ctSubjRescVa_" />
						<u:input id="${rescId}${langTypCdVo.cd}" name="${rescNm}${langTypCdVo.cd}"  titlePrefix="${titlePrefix}" titleId="cols.cmNm" value="${rescVa}" style="${style}"
							maxByte="200" validator="changeLangSelector('setCm', id, va)" mandatory="Y" onkeydown="javascript:subjNmChange()" />
					</c:forEach>
				</td>
				<td>
					<c:if test="${fn:length(_langTypCdListByCompId)>1}">
						<select id="langSelector" onchange="changeLangTypCd('ctRescArea','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
							<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
								<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
							</c:forEach>
						</select>
					</c:if>
					<u:input type="hidden" id="rescId" value="${ctEstbBVo.ctSubjResc}" />
				</td>
			<td><u:buttonS href="javascript:ctIdChk();" titleId="cm.btn.dupCnfm" alt="중복확인" /></td>
		</tr>
		</tbody>
		</table></td>
	</tr>

	<tr>
	<td colspan="2" rowspan="7" valign="top">
		<div id="catTree" class="tree" style="height:440px; overflow:auto;"></div></td>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.cmItro" alt="커뮤니티 소개글" /></td>
	
	<td>
		<u:editor id="cmItro" height="300px" module="ct" value="${ctEstbBVo.ctItro}" namoToolbar="wcPop" width="100%" />
	</td>
	</tr>

	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="ct.cols.allCompYn" alt="전사여부" /></td>
	<td<c:if test="${!empty ctEstbBVo && ctEstbBVo.allCompYn eq 'Y'}"
	> class="body_lt"</c:if>><c:if test="${!empty ctEstbBVo && ctEstbBVo.allCompYn eq 'Y'}"
	><u:msg titleId="cm.option.use" alt="사용"/><u:input type="hidden" id="allCompYn" value="${ctEstbBVo.allCompYn}"/></c:if
	><c:if test="${empty ctEstbBVo || empty ctEstbBVo.allCompYn || ctEstbBVo.allCompYn eq 'N'}"
	><select id="allCompYn" name="allCompYn" title="<u:elemTitle titleId="cols.useYn" />" onchange="allCompChange(this.value);">
			<u:option value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${ctEstbBVo.allCompYn}" selected="${empty ctEstbBVo.allCompYn }"/>
			<u:option value="Y" titleId="cm.option.use" alt="사용" checkValue="${ctEstbBVo.allCompYn}" />
			</select></c:if>
	</td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.kwd" alt="키워드" /></td>
	<td><u:input id="kwd" name="kwd" value="${ctEstbBVo.ctKwd}" titleId="cols.kwd" maxLength="15" style="width: 96%;" mandatory="Y"/></td>
	</tr>
		
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.mngTgtYn" alt="관리대상 여부" /></td>
		
	<td><select id="mngTgtYn" name="mngTgtYn">
		<option value="Y"  selected="selected" >YES</option>
		<option value="N" selected="selected" >NO</option>
		</select></td>
	</tr>

	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.joinMet" alt="가입방법" /></td>
	<td><select id="joinMet" name="joinMet">
		<option value="1" <c:if test="${ctEstbBVo.joinMet == '1'}" > selected="selected" </c:if>><u:msg titleId="ct.option.joinMet.join01" alt="즉시가입" /></option>
		<option value="2" <c:if test="${ctEstbBVo.joinMet == '2'}" > selected="selected" </c:if>><u:msg titleId="ct.option.joinMet.join02" alt="마스터 승인 후 가입" /></option>
		</select></td>
	</tr>

	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.dftAuth" alt="기본권한" /></td>
	<td><select id="dftAuth" name="dftAuth">
		<option value="A" <c:if test="${ctEstbBVo.dftAuth == 'A'}" > selected="selected" </c:if>><u:msg titleId="ct.cols.mbshLev3" alt="준회원" /></option>
		<option value="R" <c:if test="${ctEstbBVo.dftAuth == 'R'}" > selected="selected" </c:if>><u:msg titleId="ct.cols.mbshLev2" alt="정회원" /></option>
		<option value="S" <c:if test="${ctEstbBVo.dftAuth == 'S'}" > selected="selected" </c:if>><u:msg titleId="ct.cols.mbshLev1" alt="스텝" /></option>
		</select></td>
	</tr>

	<%-- <tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.uathdPublYn" alt="비인증 공개여부" /></td>
	<td><select id="extnOpenYn" name="extnOpenYn">
		<option value="Y" <c:if test="${ctEstbBVo.extnOpenYn =='Y'}" > selected="selected" </c:if>>YES</option>
		<option value="N" <c:if test="${ctEstbBVo.extnOpenYn =='N'}" > selected="selected" </c:if>>NO</option>
		</select></td>
	</tr> --%>

	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.attSizeLim" alt="첨부용량 제한" />(MB)</td>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:set test="${ctEstbBVo.attLimSize == -1}" var="attSizeLim" value="INFINITY" elseValue="${ctEstbBVo.attLimSize}" />
		<td><u:input id="attSizeLim" name="attSizeLim" value="${attSizeLim}" titleId="cols.attSizeLim" mandatory="Y" valueOption="number" /></td>
		<td><u:buttonS onclick="javacrtipt:infinity()" titleId="cm.btn.unlim" alt="무제한" /></td>
		</tr>
		</table></td>
	</tr>

	<tr>
		<td colspan="4"><u:files id="ctfiles" fileVoList="${fileVoList}" module="ct" mode="set" exts="${exts }" extsTyp="${extsTyp }"/></td>
	</tr>
</table>
</div>


<% // 하단 버튼 %>
<u:buttonArea>
	<c:set var="paramFnc" value="&fnc=${fnc}" />
	<u:button titleId="cm.btn.next" alt="다음" href="javascript:formSubit();" />
	<u:button titleId="cm.btn.cancel" alt="취소" href="javascript:ctCrtCancel();" />
</u:buttonArea>
</form>
<u:blank />

