<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set var="agntParam" test="${!empty schBcRegrUid }" value="&schBcRegrUid=${schBcRegrUid }" elseValue=""/>
<u:params var="nonPageParams" excludes="schBcRegrUid,pageNo"/>
<script type="text/javascript">
<!--
<%// 명함 정보 추가 %>
function atndAdd(objArr){
	addCols(objArr,"FRND");
};

<%// 명함 조회 팝업 %>
function findBcPop(){
	dialog.open('findBcPop','<u:msg titleId="wb.jsp.findBcPop.title" alt="명함선택" />','./findBcPop.do?menuId=${menuId}${agntParam}');
};

<%// 선택추가 %>
function addCols(arr , emplTypCd){	
	if(arr==null) return;
	var $tr, $hiddenTr = $("#listArea tbody:first #hiddenTr"),emplId,emplNm;
	var html = $hiddenTr[0].outerHTML;
	var vas = emplTypCd == 'ETC' ? null : getAllVas(emplTypCd);
	arr.each(function(index, obj){
		emplId = emplTypCd == 'EMPL' ? obj.userUid : (emplTypCd == 'ETC' ? obj.etcId : obj.bcId )
		if(vas==null || !vas.contains(emplId)){
			$hiddenTr.before(html);
			$tr = $hiddenTr.prev();
			emplNm = emplTypCd == 'EMPL' ? obj.rescNm : (emplTypCd == 'ETC' ? obj.etcNm : obj.bcNm );
			$tr.attr('id','tr'+emplTypCd+emplId);
			$tr.find("input[type='checkbox']").val(emplId);
			$tr.find("input[type='checkbox']").attr("data-emplTypCd",emplTypCd);
			$tr.find("td#emplNm").text(emplNm);
			$tr.find("td#emplTypNm").text((emplTypCd == 'EMPL' ? '<u:msg titleId="cm.option.empl" alt="임직원" />' : (emplTypCd == 'ETC' ? '<u:msg titleId="cm.etc" alt="기타" />' :'<u:msg titleId="wc.option.frnd" alt="지인" />')));
			$tr.find("input[name='emplNm']").val(emplNm);
			$tr.find("input[name='emplTypCd']").val(emplTypCd);
			$tr.find("input[name='emplId']").val(emplId);
			$tr.find("td#emplCompNm div.ellipsis").text(obj.compNm);
			$tr.find("td#emplCompPhon").text(obj.compPhon);
			$tr.find("td#emplEmail").text(obj.email);
			$tr.find("td#emplMbno").text(obj.mbno);
			$tr.find("input[name='compNm']").val(obj.compNm);
			$tr.find("input[name='compPhon']").val(obj.compPhon);
			$tr.find("input[name='email']").val(obj.email);
			$tr.find("input[name='emplPhon']").val(obj.mbno);
			
			$tr.show();
			setJsUniform($tr[0]);
		}
	});
};

<%// 선택제거 %>
function removeCols(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr==null) return;
	arr.each(function(index, tr){
		$(tr).remove();
	}, true);
};

<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(noSelectMsg){
	var arr=[], id, obj;
	$("#listArea tbody:first input[type='checkbox']:checked").each(function(){
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

// 임직원, 지인 라디오 선택
function guestChange(obj){
	$('#setRegForm input[id="guest"]').val('');
};

//참석자 추가
function guestAdd(){
	var guestObj = $('#setRegForm input[id="guest"]');
	var guestTyp = $('#setRegForm input:radio[name="guestTyp"]:checked').val();
	if(guestTyp == 'ETC') {
		if(guestObj.val() == ''){
			alert("참석자를 입력해주세요.");
		}else{
			var arr = [];
			arr.push({etcId:'' , etcNm:guestObj.val()});
			addCols(arr , "ETC");
			guestObj.val('');
		}
	}else if(guestTyp == 'FRND'){
		if($('#guest').val()!=''){
			callAjax('./srchUserAjx.do?menuId=${menuId}&fncCal=${fncCal}', {srchTyp:'bc',srchName:$('#guest').val(), paramUserUid:'${schBcRegrUid }'}, function(data) {
				if (data.returnString != null) {
					var arr=[];
					arr.push($.parseJSON(data.returnString));
					atndAdd(arr);
					return;	
				}
				dialog.open('findBcPop','<u:msg titleId="wb.jsp.findBcPop.title" alt="명함선택" />','./findBcPop.do?menuId=${menuId}${agntParam}&callBack=atndAdd&fncMul=Y&schCat=bcNm&schWord='+encodeURIComponent($('#guest').val()));
			});
		}else{
			dialog.open('findBcPop','<u:msg titleId="wb.jsp.findBcPop.title" alt="명함선택" />','./findBcPop.do?menuId=${menuId}${agntParam}&callBack=atndAdd&fncMul=Y&schCat=bcNm&schWord='+encodeURIComponent($('#guest').val()));
		}		
	}else{
		if($('#guest').val()!=''){
			callAjax('./srchUserAjx.do?menuId=${menuId}&fncCal=${fncCal}', {srchTyp:'user',srchName:$('#guest').val()}, function(data) {
				if (data.returnString != null) {
					var arr=[];
					arr.push($.parseJSON(data.returnString));
					addCols(arr,"EMPL");
					return;
				}
				openMuiltiUser();
			});
		}else{
			openMuiltiUser();
		}
	}
};

//여러명의 사용자 선택
function openMuiltiUser(mode){
	var data = [];<%// data: 팝업 열때 오른쪽에 뿌릴 데이타 %>	
	/* var $view = $("#listArea");
		$view.find("tbody:first input[type='checkbox']").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr' && $(this).attr('data-emplTypCd') != null && $(this).attr('data-emplTypCd') == 'EMPL' ) {
			data.push({userUid:$(this).val()});
		};
	}); */
	
	var param={data:data, multi:true, mode:mode==null ?'search':'view' , userNm:encodeURIComponent($('#guest').val())};
	<c:if test="${!empty globalOrgChartEnable && globalOrgChartEnable==true}">
		param.global='Y';
	</c:if>
	
	<%// option : data, multi, titleId %>
	searchUserPop(param, function(arr){
		if(arr!=null){
			addCols(arr,"EMPL");
		}
	});
};

<%//현재 등록된 id 목록 리턴 %>
function getAllVas(emplTypCd){
	var arr=[];
	$('#listArea input[type="checkbox"]').each(function(){
		if($(this).attr("data-emplTypCd") == emplTypCd){
			arr.push($(this).val());
		}
	});
	if(arr.length==0){
		return null;
	}
	return arr;
};

//명함 선택
function fnBcSelect(detlViewType, callBack ){
	var objArr = getIframeContent(detlViewType+'Frm').fnSelArrs();
	if(objArr == null ) return;
	if(callBack != "" ){
		eval(callBack)(objArr);
	}else{
		objArr.each(function(index, obj){
			$("#setRegForm input[name=bcId]").val(obj.bcId);
			$("#setRegForm input[name=bcNm]").val(obj.bcNm);
			$("#setRegForm input[name=bcCompNm]").val(obj.compNm);
		});		
	}
	dialog.close('findBcPop');
};

<%// 저장 %>
function save(){
	<%// 서버 전송%>
	if (validator.validate('setRegForm') && true/*confirmMsg("cm.cfrm.save")*/ ) {
		var $form = $('#setRegForm');
		$form.attr('method','post');		
		$form.attr('action','./${transPage}.do?menuId=${menuId}');
		$form.attr('enctype','multipart/form-data');
		$form.attr('target','dataframe');
		saveFileToForm('${filesId}', $form[0], null);
		//$form[0].submit();
	}
};

<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
};

$(document).ready(function() {
setUniformCSS();
});
//-->
</script>

<u:title titleId="wb.jsp.setMetng.reg.title" alt="관련미팅정보 등록/관련미팅정보 수정" menuNameFirst="true"/>

<form id="setRegForm">
	<c:if test="${!empty wbBcMetngDVo.bcMetngDetlId }">
		<u:input type="hidden" id="bcMetngDetlId" value="${wbBcMetngDVo.bcMetngDetlId}" />
	</c:if>
	<c:if test="${!empty schBcRegrUid}">
		<u:input type="hidden" id="schBcRegrUid" value="${schBcRegrUid }"/>
	</c:if>
	
	<u:input type="hidden" id="listPage" value="./${listPage}.do?${nonPageParams}${agntParam}" />
	<u:input type="hidden" id="viewPage" value="./${viewPage}.do?${params}" />
	
	<% // 폼 필드 %>
	<div class="listarea">
		<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
		<tr>
			<td width="18%" class="head_lt"><u:msg titleId="wb.cols.bc" alt="명함" /></td>
			<td width="32%">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tbody>
					<tr>
						<td>
							<u:input id="bcNm" titleId="wb.cols.bc" value="${wbBcMetngDVo.bcNm}" maxByte="30" style="width: 96%;" readonly="readonly"/>
							<u:input type="hidden" id="bcId" name="bcId" value="${wbBcMetngDVo.bcId}" />
						</td>
						<td width="39"><u:buttonS titleId="cm.btn.search" alt="검색" href="javascript:findBcPop();" /></td>
					</tr>
					</tbody>
				</table>
			</td>
			<td width="18%" class="head_lt"><u:msg titleId="cols.comp" alt="회사" /></td>
			<td><u:input id="compNm" name="bcCompNm" titleId="cols.compNm" value="${wbBcMetngDVo.compNm}" maxByte="120" style="width: 96%;" readonly="readonly" /></td>
		</tr>
		
		<tr>
			<td class="head_lt"><u:mandatory /><u:msg titleId="cols.metngDt" alt="관련미팅일시" /></td>
			<td colspan="3">
				<table border="0" cellpadding="0" cellspacing="0">
					<tbody>
					<tr>
						<td><u:calendar id="metngYmd" value="${wbBcMetngDVo.metngYmd}" mandatory="Y" titleId="cols.metngDt"/></td>
					</tr>
					</tbody>
				</table>
			</td>
		</tr>
		
		<tr>
			<td class="head_lt"><u:mandatory /><u:msg titleId="cols.secul" alt="보안등급" /></td>
			<td class="bodybg_lt" colspan="3">
				<u:checkArea>
					<u:radio name="openYn" value="Y" titleId="cm.option.publ" alt="공개" checkValue="${wbBcMetngDVo.openYn }" checked="${empty wbBcMetngDVo.openYn }" inputClass="bodybg_lt"/>
					<u:radio name="openYn" value="N" titleId="cm.option.priv" alt="비공개" checkValue="${wbBcMetngDVo.openYn }" inputClass="bodybg_lt" />
				</u:checkArea>
			</td>
		</tr>
		
		<tr>
			<td class="head_lt"><u:msg titleId="wb.cols.cls" alt="분류" /></td>
			<td colspan="3">
				<select id="metngClsCd" name="metngClsCd" >
					<u:option value="none" titleId="cm.option.none" selected="${empty wbBcMetngDVo || wbBcMetngDVo.metngClsCd eq 'none'}"/>
					<c:forEach items="${wbMetngClsCdBVoList}" var="list" varStatus="status">
						<u:option value="${list.rescId}" title="${list.rescNm}" selected="${wbBcMetngDVo.metngClsCd == list.rescId}"/>
					</c:forEach>
				</select>
			</td>
		</tr>
		
		<tr>
			<td class="head_lt"><u:mandatory /><u:msg titleId="cols.subj" alt="제목" /></td>
			<td colspan="3"><u:input id="metngSubj" name="metngSubj" value="${wbBcMetngDVo.metngSubj}" titleId="cols.subj" style="width:98%;" maxByte="240" mandatory="Y"/></td>
		</tr>
		
		<tr>
			<td rowspan="2" class="head_lt"><u:msg titleId="cols.guestApnt" alt="참석자지정" /></td>
			<td colspan="3" class="bodybg_lt">
				<u:checkArea>
					<u:radio name="guestTyp" value="EMPL" titleId="cm.option.empl" alt="임직원" inputClass="bodybg_lt" onclick="guestChange(this);" checked="true"/>
					<u:radio name="guestTyp" value="FRND" titleId="wc.option.frnd" alt="지인" inputClass="bodybg_lt" onclick="guestChange(this);"/>
					<%-- <u:radio name="guestTyp" value="ETC" titleId="ct.option.etc" alt="기타"  inputClass="bodybg_lt" onclick="guestChange(this);" /> --%>
				<td><u:input id="guest" value="" titleId="cols.guest" style="width: 130px;" onkeydown="if (event.keyCode == 13){guestAdd();return false;}"/></td>
				<td><u:buttonS href="" titleId="wc.btn.guestAdd" alt="참석자추가" onclick="guestAdd();"/><u:buttonS href="javascript:removeCols();" titleId="wc.btn.guestDel" alt="참석자삭제" /></td>
				</u:checkArea>
			</td>
		</tr>
		
		<tr>
			<td colspan="3">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tbody>
					<tr>
						<td>
							<div style="width:100%;height:160px;overflow-y:auto;">
								<div id="listArea" class="listarea" style="width:95%; padding:5px;">
									<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
									<tr id="headerTr">
										<th width="36"  class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></th>
										<th width="15%"  class="head_ct"><u:msg titleId="cols.nm" alt="이름" /></th>
										<th width="12%" class="head_ct"><u:msg titleId="wb.cols.emplTyp" alt="임직원구분" /></th>
										<%-- <th class="head_ct"><u:msg titleId="cols.compNm" alt="회사명" /></th> --%>
										<th width="17%"  class="head_ct"><u:msg titleId="cols.compPhon" alt="회사전화번호" /></th>
										<th width="17%"  class="head_ct"><u:msg titleId="cols.mbno" alt="휴대전화번호" /></th>
										<th class="head_ct"><u:msg titleId="cols.email" alt="이메일" /></th>
									</tr>
									<c:if test="${!empty wbBcMetngAtndRVoList}">
										<c:forEach var="list" items="${wbBcMetngAtndRVoList}" varStatus="status">
											<u:set test="${status.last}" var="trDisp" value="display:none" />
											<u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${list.bcMetngAtndDetlId}" />
											<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="${trId}" style="${trDisp}">
												<td width="32" class="bodybg_ct">
													<input type="checkbox" value="${list.emplId}" data-emplTypCd="${list.emplTypCd }" />
													<input type="hidden" name="bcMetngAtndDetlId" value="${list.bcMetngAtndDetlId}" />
													<input type="hidden" name="emplNm" value="${list.emplNm}"/>
													<input type="hidden" name="emplTypCd" value="${list.emplTypCd}"/>
													<input type="hidden" name="emplPhon" value="${list.emplPhon}"/>
													<input type="hidden" name="emplId" value="${list.emplId}"/>
													<input type="hidden" name="compNm" value="${list.compNm}"/>
													<input type="hidden" name="compPhon" value="${list.compPhon}"/>
													<input type="hidden" name="email" value="${list.email}"/>
												</td>
												<td class="body_ct" id="emplNm">${list.emplNm}</td>
												<td class="body_ct" id="emplTypNm"><u:msg titleId="${list.emplTypCd eq 'FRND' ? 'wc.option.frnd' : (list.emplTypCd eq 'EMPL' ? 'cm.option.empl' : 'ct.option.etc')}" alt="지인"/></td>
												<%-- <td class="body_ct" id="emplCompNm"><div class="ellipsis" title="${list.compNm }">${list.compNm}</div></td> --%>
												<td class="body_ct" id="emplCompPhon">${list.compPhon}</td>
												<td class="body_ct" id="emplMbno">${list.emplPhon}</td>
												<td class="body_ct" id="emplEmail">${list.email}</td>
											</tr>
										</c:forEach>
									</c:if>
									</table>
								</div>
							</div>
						</td>
					</tr>
					</tbody>
				</table>
			</td>
		</tr>
		
		<tr>
			<td class="head_lt"><u:mandatory /><u:msg titleId="cols.cont" alt="내용" /></td>
			<td colspan="3">
				<u:textarea id="metngCont" name="metngCont" value="${wbBcMetngDVo.metngCont}" titleId="cols.cont" maxByte="1000" style="width:98%;" rows="4" mandatory="Y"/>
			</td>
		</tr>
	
		<tr>
			<td colspan="4">
				<u:files id="${filesId}" fileVoList="${fileVoList}" module="wb" mode="set" actionParam="metng" exts="${exts }" extsTyp="${extsTyp }"/>
			</td>
		</tr>
	</table>
	</div>

	<u:blank />
	<u:set var="historyPage" test="${!empty param.bcMetngDetlId}" value="./${viewPage }.do?${params }" elseValue="./${listPage }.do?${paramsForList }${agntParam }"/>
	<u:buttonArea>
		<u:button titleId="cm.btn.save" onclick="save();" alt="저장" auth="${listPage eq 'listAllMetng' ? 'A' : 'W'}" ownerUid="${listPage eq 'listMetng' ? wbBcMetngDVo.regrUid : ''}"/>
		<u:button titleId="cm.btn.cancel" href="${historyPage }" alt="취소" />
	</u:buttonArea>

</form>
