<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="callback" test="${!empty param.callback }" value="${param.callback }" elseValue="saveSendOpt"/>
<u:set var="applyCfrm" test="${!empty param.callback }" value="applyJsonCfrm" elseValue="applyCfrm"/>
<u:set var="paramStorIdQueryString" test="${!empty paramStorId }" value="&paramStorId=${paramStorId }" elseValue=""/>
<script type="text/javascript">
<!--<%// 분류,폴더 Prefix %>
function getTabPrefix(lstTyp){
	var prefix = "fld";
	if(lstTyp == 'C') prefix = "cls";
	return prefix;
}<%// [버튼] 분류,폴더 %>
function findSelPop(lstTyp){
	var prefix = getTabPrefix(lstTyp);
	var $area = $("#optListArea #"+prefix+"InfoArea"), data = [];
	$area.find("input[id='"+prefix+"Id']").each(function(){
		data.push($(this).val());
	});
	var url = '/cm/doc/findSelPop.do?menuId=${menuId}${paramStorIdQueryString}&lstTyp='+lstTyp+"&selIds="+data+"&fncMul="+(lstTyp == 'C' ? 'Y':'N')+'&fldGrpId=${param.fldGrpId}';
	var msgTitle = lstTyp == 'C' ? '<u:msg titleId="dm.cols.listTyp.cls" alt="분류보기" />' : '<u:msg titleId="dm.cols.listTyp.fld" alt="폴더보기" />';
	dialog.open('findSelPop', msgTitle, url);
};<%// 분류,폴더 적용%>
function setSelInfos(arr, lstTyp){
	var prefix = getTabPrefix(lstTyp);
	$area = $('#optListArea #'+prefix+'InfoArea');
	
	var buffer = [];
	var nms = '';
	arr.each(function(index, obj){
		buffer.push("<input type='hidden' id='"+prefix+"Id' name='"+prefix+"Id' value='"+obj.id+"'/>\n");
		nms+= nms == '' ? obj.nm : ','+obj.nm;
	});
	$area.find('#idArea').html('');
	$area.find('#idArea').html(buffer.join(''));
	$area.find('#nmArea input[id="'+prefix+'Nm"]').val(nms);
	dialog.close('findSelPop');
}<%// 1명의 사용자 선택 %>
function openSingUser(id){
	var data = [];<%// data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	if($('#optListArea #'+id+'Uid').val() != '') data.push({userUid:$('#optListArea #'+id+'Uid').val()});
	<%// option : data, multi, withSub, titleId %>
	searchUserPop({data:data}, function(userVo){
		if(userVo!=null){
			$('#optListArea #'+id+'Uid').val(userVo.userUid);
			$('#optListArea #'+id+'Nm').val(userVo.rescNm);
		}
	});
};<%// [하단버튼:] 초기화 %>
function optReset(){
	var $area = $('#optListArea');
	$area.find("[name='docPid']").remove();
	$area.find('#fldSelectBtn').show();
	$area.find('#seculArea').show();
	$area.find('#keepPrdArea').show();
	$area.find("input,select").each(function(){
		if($(this).prop("tagName") == 'SELECT'){
			if($(this).find('option') != undefined)
				$(this).val($(this).find('option').eq(0).val());
		}else{
			$(this).val('');
		}
	});
	//기본값 초기화
	<c:if test="${!empty ownrUid}">$('#optListArea #ownrUid').val('${ownrUid}');</c:if>
	<c:if test="${!empty ownrNm}">$('#optListArea #ownrNm').val('${ownrNm}');</c:if>
	<c:if test="${!empty param.seculCd}">$('#optListArea #seculCd').val('${param.seculCd}');</c:if>
	<c:if test="${!empty param.docKeepPrdCd}">$('#optListArea #docKeepPrdCd').val('${param.docKeepPrdCd}');</c:if>
	applyUniform('optListArea');
	dialog.resize("setSendOptDialog");
}<%// [팝업] 하위문서 %>
function findOptDocPop(callback){
	var url = '/cm/doc/findDocPop.do?${paramsForList}&fncMul=N&tabId=doc';
	var selObj = $('#optListArea').find('input[name="fldId"]');
	if(selObj != undefined && selObj.val() == ''){
		alertMsg("dm.jsp.setDoc.not.fld");<%//dm.jsp.setDoc.not.fld='폴더'를 선택 후 사용해 주십시요.%>
		return;
	}
	url+= "&fldId="+selObj.val();
	if(callback != null) url+= "&callback="+callback;
	dialog.open('findDocPop', '<u:msg titleId="dm.jsp.search.doc.title" alt="문서조회" />', url);
};<%// 부모문서ID세팅 %>
function setDocPid(docPid){
	var $area = $('#optListArea');
	$area.find("[name='docPid']").remove();
	$area.appendHidden({name:'docPid',value:docPid});
	${applyCfrm}(false);
};<%// 부모문서정보세팅 %>
function setDocInfo(arr){
	if(arr == null) return;
	var $area = $('#optListArea');
	if(arr.length == 1){
		arr[0].each(function(key, va){
			if(key == 'docPid'){
				$area.find("[name='docPid']").remove();
				$area.appendHidden({name:key,value:va});
			}else if(key == 'clsList'){
				setSelInfos(va, 'C');
			}else{
				$area.find('#'+key).val(va);
			}
		});	
	}
	$area.find('#fldSelectBtn').hide();
	$area.find('#seculArea').hide();
	$area.find('#keepPrdArea').hide();
	applyUniform('optListArea');
	dialog.close('findDocPop');
	dialog.resize("setSendOptDialog");
};<%// [확인] 저장 %>
function applyCfrm(valid){
	if(!valid || validator.validate('optListArea')){
		var arr=[];
		$('#optListArea').find("input[type='hidden'],select").each(function(){
			if($(this).attr('disabled')!='disabled' && $(this).val() != ''){
				arr.push({name:$(this).attr('name'),value:$(this).val()});
			}
		});
		if(arr.length==0){
			return;
		}
		//saveSendOpt(arr);
		${callback}(arr);
	}
}<%// [확인] 저장 - JSON%>
function applyJsonCfrm(valid){
	if(!valid || validator.validate('optListArea')){
		var param = new ParamMap(),key,va;
		$('#optListArea').find("input[type='hidden'],input[type='text'],select").each(function(){
			if($(this).attr('disabled')!='disabled' && $(this).val() != ''){
				key = $(this).attr('name');
				va = param.get(key) != undefined ? param.get(key)+', '+$(this).val() : $(this).val();
				param.put(key,va);
			}
		});
		if(param == null){
			return;
		}
		var returnParam = JSON.stringify(param.toJSON());
		${callback}(returnParam);
		dialog.close('setSendOptDialog');
	}
}
$(document).ready(function() {
});
//-->
</script>
<c:set var="exKeys" value=""/><!-- 제외 key -->
<c:if test="${!empty param.mode && param.mode eq 'update'}">
<c:set var="optAtrbIds" value="fld,cls"/><!-- 옵션 속성ID - 폴더,분류,보안등급,보존연한,소유자-->
</c:if>
<c:if test="${empty param.mode || param.mode ne 'update'}">
<c:set var="optAtrbIds" value="fld,cls,secul,keepPrd,ownr"/><!-- 옵션 속성ID - 폴더,분류,보안등급,보존연한,소유자-->
</c:if>
<%-- <c:if test="${!empty fldId }"><c:set var="exKeys" value="fldId"/></c:if> --%>
<div style="width:600px;">
<!-- 필수여부 -->
<u:set var="mandatoryYn" test="${empty param.docTyp || param.docTyp ne 'doc'}" value="Y" elseValue="N"/>
<div id="optListArea">
	<c:if test="${!empty param.ownrUid}">
		<c:set var="exKeys" value="${exKeys }${empty exKeys ? '' : ',' }ownr"/>
	</c:if>
	<%-- <div>
		<c:forTokens var="atrbId" items="${optAtrbIds }" delims=",">	
			<u:checkbox name="${atrbId }ChkYn" value="Y" titleId="dm.cols.${atrbId }" alt="" onclick="optChk(this, '${atrbId }');"/>
		</c:forTokens>
	</div>
	<div class="blank" id="blank"></div> --%>
	<c:forTokens var="atrbId" items="${optAtrbIds }" delims="," varStatus="status">	
		<u:set var="atrbDisplay" test="${fn:contains(exKeys,atrbId) }" value="display:none" elseValue=";"/>
		<div class="groupdiv" id="${atrbId }Area" style="cursor:default;height: 30px;width:47%;float:left;margin-left:5px;margin-left:2px;margin-top:2px;${atrbDisplay}" >
			<dl>
			<dd class="group_txt">
				<c:if test="${atrbId eq 'fld' }"><div id="fldInfoArea" style="display:inline;">
			<div id="idArea" style="display:none;"><u:input type="hidden" id="fldId" value="${fldId }"/></div>
			<div id="nmArea" style="display:inline;"><u:mandatory display="${mandatoryYn == 'Y' }"/><u:input id="fldNm" titleId="cols.fld" value="${fldNm}" mandatory="Y" style="width:55%;" readonly="Y" /></div>
		</div><u:set var="fldDisplay" test="${empty fldId || fldId == 'null'}" value="display:inline;" elseValue="display:none;"/><u:buttonS id="fldSelectBtn" titleId="dm.btn.fldSel" alt="폴더 선택" onclick="findSelPop('F');" style="${fldDisplay }"/>
				</c:if>
				<c:if test="${atrbId eq 'cls' }"><div id="clsInfoArea" style="display:inline;">
		<div id="idArea" style="display:none;">
			<c:forEach var="clsVo" items="${dmClsBVoList }" varStatus="status">
				<u:input type="hidden" id="clsId" value="${clsVo.clsId }"/>
				<c:set var="clsNmTmp" value="${clsNmTmp}${status.count > 1 ? ',' : '' }${clsVo.clsNm }"/>
			</c:forEach>
		</div>
		<div id="nmArea" style="display:inline;"><u:mandatory display="${mandatoryYn == 'Y' }"/><u:input id="clsNm" titleId="cols.cls" value="${clsNmTmp}" mandatory="${mandatoryYn }" style="width:55%;" readonly="Y"/></div>
	</div><u:buttonS titleId="dm.btn.clsSel" alt="분류 선택" onclick="findSelPop('C');" />
				</c:if>
				<c:if test="${atrbId eq 'secul'}"><u:mandatory display="${mandatoryYn == 'Y' }"/><select id="seculCd" name="seculCd"<u:elemTitle titleId="dm.cols.secul" alt="보안등급" />><c:if test="${mandatoryYn eq 'N' }"><u:option value="" titleId="dm.cols.secul" /></c:if>
				<c:if test="${mandatoryYn eq 'Y' }"><option value="none" <c:if test="${!empty param.seculCd && param.seculCd eq cd.cdId }">selected="selected"</c:if>><u:msg titleId="cm.option.noSelect" alt="선택안함" /></option></c:if><c:forEach 
		items="${itemDispMap.seculNm.colmVo.cdList}" var="cd" varStatus="status">
		<option value="${cd.cdId}" <c:if test="${!empty param.seculCd && param.seculCd eq cd.cdId }">selected="selected"</c:if>>${cd.rescNm}</option></c:forEach>
		</select> <span class="color_txt"><u:msg titleId="dm.cols.secul" alt="보안등급"/></span>
				</c:if>
				<c:if test="${atrbId eq 'keepPrd'}"><u:mandatory display="${mandatoryYn == 'Y' }"/><select id="docKeepPrdCd" name="docKeepPrdCd"<u:elemTitle titleId="dm.cols.keepPrd" alt="보존연한" />><c:if test="${mandatoryYn eq 'N' }"><u:option value="" titleId="dm.cols.keepPrd" /></c:if>
		<c:forEach 
		items="${itemDispMap.docKeepPrdNm.colmVo.cdList}" var="cd" varStatus="status">
		<option value="${cd.cdId}" <c:if test="${!empty param.docKeepPrdCd && param.docKeepPrdCd eq cd.cdId }">selected="selected"</c:if>>${cd.rescNm}</option></c:forEach>
		</select> <span class="color_txt"><u:msg titleId="dm.cols.keepPrd" alt="보존연한"/></span>
				</c:if>
				<c:if test="${atrbId eq 'ownr'}"><u:input type="hidden" id="ownrUid" value="${ownrUid}"/>
			<c:if test="${empty ownrUid }"><u:mandatory display="${mandatoryYn == 'Y' }"/><u:input id="ownrNm" value="${ownrNm}" titleId="dm.cols.ownr" readonly="Y" mandatory="${mandatoryYn }"/>
			<u:buttonS href="javascript:;" titleId="dm.cols.ownr" alt="소유자" onclick="openSingUser('ownr');" /></c:if>
				</c:if>
			</dd>
			</dl>
		</div>
	</c:forTokens>
	<!-- 인수인계 -->
	<c:if test="${!empty param.mode && param.mode eq 'takovr'}">
		<div class="groupdiv" id="${atrbId }Area" style="cursor:default;height: 30px;width:47%;float:left;margin-left:5px;margin-left:2px;margin-top:2px;${atrbDisplay}" >
			<dl>
			<dd class="group_txt">
				<u:input type="hidden" id="regrUid" value="${param.regrUid}"/><u:mandatory display="${mandatoryYn == 'Y' }"/><u:input id="regrNm" value="" titleId="cols.regr" readonly="Y" mandatory="${mandatoryYn }"/>
				<u:buttonS href="javascript:;" titleId="cols.regr" alt="등록자" onclick="openSingUser('regr');" />
			</dd>
			</dl>
		</div>
	</c:if>
</div>
<c:if test="${mandatoryYn eq 'N' }">
<div class="color_txt">
	<u:msg titleId="dm.jsp.sendOpt.txt01" alt="* 수정하지 않으면 보내기할 문서의 기본 정보가 유지됩니다. "/>
</div>
</c:if>
<u:blank />
<u:buttonArea id="sendBtnArea">
	<c:if test="${(empty param.mode || param.mode ne 'update') && !empty param.callback}">
		<u:button id="subDocListBtn" titleId="dm.btn.sendSubDoc" alt="하위문서로 보내기" onclick="findOptDocPop('setDocInfo');" />
		<u:button titleId="dm.btn.subDoc.reset" alt="하위문서정보초기화" href="javascript:optReset();" />
	</c:if>
	<c:if test="${empty param.callback}"><u:button titleId="cm.btn.reset" alt="초기화" href="javascript:optReset();" /></c:if>
	<li style="float:left;width:20px;">&nbsp;</li>
	<u:button titleId="cm.btn.confirm" alt="확인" href="javascript:${applyCfrm }(true);"  style="margin-left:20px;"/>
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</div>