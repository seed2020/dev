<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//1명의 사용자 선택
function openSingUser(){
	var data = [];<%// data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	if($('#rescAdmUid').val() != '') data.push({userUid:$('#rescAdmUid').val()});
	<%// option : data, multi, withSub, titleId %>
	searchUserPop({data:data}, function(userVo){
		if(userVo!=null){
			$('#rescAdmUid').val(userVo.userUid);
			$('#rescAdmNm').val(userVo.rescNm);
		}
	});
};

//심의여부 체크
function discChecked(){
	var $checked = $('#setRegForm input[name=discYn]:checked');
	if($checked.val() == 'Y' && $('#rescAdmNm').val() == ''){
		alert("심의가 있으면 자원관리자도 등록되어야 합니다.");
		return false;
	}
	
	return true;
};

<% // [팝업:저장] 테이블 저장 %>
function save() {
	if (validator.validate('setRegForm') ) {
		if(!discChecked()) return;
		
		var param = new ParamMap().getData('langTypArea');
		var langs = [];
		$('#langTypOptions select > option').each(function(){
			langs.push($(this).val());
		});
		if(langs.length==0) langs.push('${_lang}');
		param.put('prefix', 'rescVa_');
		param.put('langs', langs.join(','));
		param.put('actKey', 'resc');
		param.put('rescKndId', $('#setRegForm #rescKndId').val());
		param.put('seqId','${wrRescMngBVo.rescMngId}');
		callAjax('./chkDupRescAjx.do?menuId=${menuId}', param, function(data) {
			if (data.msgList==null) {
				if(confirmMsg("cm.cfrm.save")){
					var $form = $('#setRegForm');
					$form.attr('method','post');
					$form.attr('action','./transRescMng.do?menuId=${menuId}');
					$form.attr('enctype','multipart/form-data');
					$form.attr('target','dataframe');
					$form[0].submit();
				}else{
					setMessage(data.msgList, false);
				}
			}else setMessage(data.msgList, true);
		});
	}
};
<% // 메세지 처리 %>
function setMessage(msgList, yn){
	var msgBx = $('#setRegForm #msgBx');
	msgBx.html('');
	if(msgList!=null){
		var html = '';
		$.each(msgList, function(index, va){
			html+='<div class="red_txt" title="'+escapeValue(va)+'">';
			html+=escapeValue(va);
			html+='</div>';
		});
		if(html!='') msgBx.html(html);
	}
	if(yn) msgBx.show();
	else msgBx.hide();
};
</script>

<div style="width:650px">
<form id="setRegForm">
	<u:input type="hidden" name="menuId" value="${menuId}" />
	<u:input type="hidden" id="rescId" value="${wrRescMngBVo.rescId}" />
	<c:if test="${!empty wrRescMngBVo.rescMngId }">
		<u:input type="hidden" id="rescMngId" value="${wrRescMngBVo.rescMngId}" />
	</c:if>
	<c:if test="${!empty wrRescMngBVo.rescId }">
		<u:input type="hidden" id="rescId" value="${wrRescMngBVo.rescId }" />
	</c:if>
	<u:input type="hidden" id="listPage" value="./listRescMngFrm.do?${paramsForList}" />
	
	<% // 표 %>
	<u:listArea>
	<tr>
		<td width="22%" class="head_lt"><u:mandatory /><u:msg titleId="cols.rescKnd" alt="자원종류" /></td>
		<td width="78%">
			<select id="rescKndId" name="rescKndId" onchange="setMessage(null, false);">
				<c:forEach items="${wrRescKndBVoList}" var="list" varStatus="status">
					<u:option value="${list.rescKndId}" title="${list.kndNm}" selected="${wrRescMngBVo.rescKndId == list.rescKndId}"/>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg titleId="cols.rescNm" alt="자원명" /></td>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td id="langTypArea">
						<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:convert srcId="${wrRescMngBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
							<u:set test="${status.first}" var="style" value="width:200px;" elseValue="width:200px; display:none" />
							<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
							<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.rescNm" value="${rescVa}" style="${style}"
								maxByte="120" validator="changeLangSelector('setRegForm', id, va)" mandatory="Y" />
						</c:forEach>
					</td>
					<td id="langTypOptions">
						<c:if test="${fn:length(_langTypCdListByCompId)>1}">
							<select id="langSelector" onchange="changeLangTypCd('setRegForm','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
							<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
							</c:forEach>
							</select>
						</c:if>
					</td>
				</tr>
			</table>
		</td>
	</tr>

	<tr>
		<td class="head_lt"><u:msg titleId="cols.rescLoc" alt="자원위치" /></td>
		<td><u:input id="rescLoc" value="${wrRescMngBVo.rescLoc}" titleId="cols.rescLoc" style="width: 97%;" maxByte="240"/></td>
	</tr>

	<tr>
		<td class="head_lt"><u:msg titleId="cols.suplStat" alt="비품현황" /></td>
		<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tbody>
				<tr>
					<td>
						<u:textarea id="suplStat" value="${wrRescMngBVo.suplStat}" titleId="cols.suplStat" maxByte="240" style="width:97%" rows="4" /></td>
					</tr>

					<tr>
						<td class="body_lt"><u:msg titleId="wr.jsp.setRescPop.tx01" alt="* 240자 이내로 입력하십시오. (한글은 2자로 계산)" /></td>
					</tr>
					</tbody>
				</table>
			</td>
		</tr>

		<tr>
			<td class="head_lt"><u:msg titleId="cols.rescAdm" alt="자원관리자" /></td>
			<td>
				<table border="0" cellpadding="0" cellspacing="0">
					<tbody>
					<tr>
						<td>
							<u:input type="hidden" id="rescAdmUid" value="${wrRescMngBVo.rescAdmUid}"/>
							<u:input id="rescAdmNm" value="${wrRescMngBVo.rescAdmNm}" titleId="cols.rescAdm" readonly="Y" />							
						</td>
						<td><u:buttonS href="javascript:;" titleId="cm.btn.choice" alt="선택" onclick="openSingUser();" /></td>
						<td class="body_lt"><u:msg titleId="wr.jsp.setRescPop.tx02" alt="* 자원관리자는 한명만 등록됩니다." /></td>
					</tr>
					</tbody>
				</table>
			</td>
		</tr>

		<tr>
			<td class="head_lt"><u:mandatory /><u:msg titleId="wr.cols.discYn" alt="심의여부" /></td>
			<td class="bodybg_lt">
				<u:checkArea>
					<u:radio name="discYn" value="Y" titleId="wr.option.discY" alt="심의있음" inputClass="bodybg_lt" checkValue="${wrRescMngBVo.discYn }"/>
					<u:radio name="discYn" value="N" titleId="wr.option.discN" alt="심의없음" inputClass="bodybg_lt" checkValue="${wrRescMngBVo.discYn }" checked="${empty wrRescMngBVo.discYn}" />
				</u:checkArea>
			</td>
		</tr>
		<tr>
			<td class="head_lt"><u:msg titleId="cols.imgFile" alt="이미지파일" /></td>
			<td>				
				<u:file id="photo" titleId="or.jsp.setOrg.photoTitle" alt="사진 선택" exts="gif,jpg,jpeg,png,tif" />
				<c:if test="${!empty wrRescMngBVo.rescMngId && wrRescMngBVo.wrRescImgDVo != null}">
					<c:set var="wrRescImgDVo" value="${wrRescMngBVo.wrRescImgDVo}" />
					<c:set var="maxWdth" value="100" />
					<c:set var="maxHght" value="100" />
					<u:set test="${wrRescImgDVo != null && wrRescImgDVo.imgWdth <= maxWdth}" var="imgWdth" value="${wrRescImgDVo.imgWdth}" elseValue="${maxWdth}" />
					<u:set test="${wrRescImgDVo != null && wrRescImgDVo.imgHght <= maxHght}" var="imgHght" value="${wrRescImgDVo.imgHght}" elseValue="${maxHght}" />
					<u:set test="${wrRescImgDVo.imgWdth < wrRescImgDVo.imgHght}" var="imgWdthHgth" value="height='${imgHght}'" elseValue="width='${imgWdth}'" />
					<img src="${_cxPth}${wrRescImgDVo.imgPath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' ${imgWdthHgth}>
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
			<td>
				<u:checkArea>
					<u:radio id="useYn" name="useYn" value="Y" titleId="cm.option.use" alt="사용" checkValue="${wrRescMngBVo.useYn }"  inputClass="bodybg_lt" checked="${empty wrRescMngBVo.useYn }"/>
					<u:radio id="useYn" name="useYn" value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${wrRescMngBVo.useYn }" inputClass="bodybg_lt" />
				</u:checkArea>
			</td>
		</tr>
	</u:listArea>
	<div id="msgBx" class="ellipsis" style="float:left;display:none;width:70%;height:25px;overflow-y:auto;"></div>
	<% // 하단 버튼 %>
	<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="save();" alt="저장" auth="A" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
	</u:buttonArea>
<u:blank />
</form>
</div>
