<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="paramsForList" excludes="formNo" />
<script type="text/javascript">
<!--
<%// [아이콘] 상하 이동 %>
function move(direction, areaId){
	var i, arr = getCheckedTrs("cm.msg.noSelect", areaId);
	if(arr==null) return;
	
	var $node, $prev, $next, $std;
	if(direction=='up'){
		$std = $('#'+areaId+'ListArea #headerTr');
		for(i=0;i<arr.length;i++){
			$node = $(arr[i]);
			$prev = $node.prev();
			if($prev[0]!=$std[0]){
				$prev.before($node);
			}
			$std = $node;
		}
	} else if(direction=='down'){
		$std = $('#'+areaId+'ListArea #hiddenTr');
		for(i=arr.length-1;i>=0;i--){
			$node = $(arr[i]);
			$next = $node.next();
			if($next[0]!=$std[0]){
				$next.after($node);
			}
			$std = $node;
		}
	}
};
<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(noSelectMsg, areaId){
	var arr=[], id, obj;
	$("#"+areaId+"ListArea tbody:first input[type='checkbox']:checked").each(function(){
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

<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
};

//이벤트 버블 방지
function notEvtBubble(event){
	if(event.stopPropagation) event.stopPropagation(); //MOZILLA
	else event.cancelBubble = true; //IE
}
<%// 탭 사용여부 체크 %>
function checkTab(event, obj){
	var btnObj=$(obj);
	var tabObj=btnObj.closest('li');
	if(btnObj.hasClass('check_on')){
		btnObj.removeClass('check_on');
		btnObj.addClass('check');
		tabObj.attr('data-useyn', 'N')
	}else{
		btnObj.removeClass('check');
		btnObj.addClass('check_on');
		tabObj.attr('data-useyn', 'Y');
	}
	
	notEvtBubble(event);
}
<% // 모바일 화면 데이터 리턴 %>
function getMobData(){
	var tabYn=$('#colmListArea input[name="tabYn"]').val();
	var tabVa={}, tabList=[], tabAttr={};
	var loutFindList=[], loutId; // 레이아웃 순서 배열
	if(tabYn=='Y'){ // json으로 탭 데이터 저장	
		$('#tabGroupArea').children().each(function(){
			if($(this).attr('data-useyn')!='Y') return true;
			loutId=$(this).attr('data-areaid');
			tabList.push({loutId:loutId, title:$(this).find('dd.title span:first').text()});
			tabAttr[loutId]="Y";
			loutFindList.push($('#loutContainer div#'+$(this).attr('data-areaid')));
		});
		tabVa['tabList']=tabList;
		tabVa['tabAttr']=tabAttr;
	}
	if(loutFindList.length==0) loutFindList=$('#loutContainer div.loutFormArea');
	
	var target, tbody, colmId, colmNm, useYn;
	var loutVa={}, loutList=[], loutAttr={};
	loutFindList.each(function(idx, lout){
		loutId=$(lout).attr('id');
		target=$('#'+loutId+'ListArea');
		if(target.length==0) return true;
		tbody=target.find('tbody:first');
		$row=[];
		tbody.children().not('#headerTr').each(function(){
			colmId=$(this).find('input[type="checkbox"]').eq(0).val();
			colmNm=$(this).find('input[name="colmNm"]').val();
			useYn=$(this).find('select[name="useYn"]').val();
			$row.push({colmId:colmId, colmNm:colmNm, useYn:useYn});
			loutAttr[colmNm]=useYn;
		});
		loutList.push({loutId:loutId, list:$row});
	});
	if(loutList.length>0) loutVa['loutList']=loutList;
	loutVa['loutAttr']=loutAttr;
	
	var returnList={};
	returnList['tabVa']=JSON.stringify(tabVa);
	returnList['loutVa']=JSON.stringify(loutVa);
	
	var mobRegTypCd=$('#mobSelectArea input[name="mobRegTypCd"]:checked').val();
	returnList['mobRegTypCd']=mobRegTypCd;
	
	var saveOptCd=$('#mobOptArea input[name="saveOptCd"]:checked').val();
	returnList['saveOptCd']=saveOptCd;
	
	return returnList;
	
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="wf.cols.mob.dispSelect" type="small" alt="모바일화면 선택" />
<% // 폼 필드 %>
	<u:listArea id="mobSelectArea" colgroup="20%,">
	<tr><td><u:checkArea><u:radio name="mobRegTypCd" value="U" titleId="wf.cols.mob.dispSelect.web" alt="웹(PC) 화면과 동일하게 사용" checkValue="${wfFormRegDVo.mobRegTypCd }"  inputClass="bodybg_lt" checked="${empty wfFormRegDVo.mobRegTypCd }"
		/><u:radio name="mobRegTypCd" value="M" titleId="wf.cols.mob.dispSelect.mob" alt="모바일 화면 구성 사용(하단)" checkValue="${wfFormRegDVo.mobRegTypCd }" inputClass="bodybg_lt" 
		/></u:checkArea></td></tr><tr><td><u:checkArea><u:radio name="mobRegTypCd" value="D" titleId="wf.cols.mob.dispSelect.mobWeb" alt="모바일 화면 구성 사용(하단)+웹버전보기" checkValue="${wfFormRegDVo.mobRegTypCd }" inputClass="bodybg_lt" 
		/></u:checkArea></td></tr>
</u:listArea>
<u:blank />
<u:title titleId="wf.cols.mob.opt.save" type="small" alt="저장 옵션" />
<% // 폼 필드 %>
	<u:listArea id="mobOptArea" colgroup="20%,">
	<tr><td><u:checkArea><u:radio name="saveOptCd" value="S" titleId="wf.cols.mob.opt.save.only" alt="화면구성만 저장" inputClass="bodybg_lt" checked="true"
		/><u:radio name="saveOptCd" value="D" titleId="wf.cols.mob.opt.save.current" alt="현재 버전에 반영" inputClass="bodybg_lt" /></u:checkArea></td></tr>
</u:listArea>
<u:blank />
<c:set var="loutList" value="${wfFormRegDVo.loutVa }"/>
<u:title titleId="wb.jsp.setBcScrn.subtitle02" alt="선택할 항목" type="small"  />
<u:titleArea
	outerStyle="height:414px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV">
	<div id="colmListArea" style="padding:10px;"><c:set var="loutPrefix" value=""/>
		<!-- 탭 -->
		<jsp:include page="/WEB-INF/jsp/wf/works/inclTab.jsp" flush="false">
		<jsp:param value="mob" name="tabPage"/>
		<jsp:param value="${loutPrefix }" name="loutPrefix"/>
		</jsp:include>
		<u:blank />
		<c:if test="${!empty loutList }"><c:if test="${!empty wfFormMobDVo }"><u:convertJson var="loutVa" value="${wfFormMobDVo.loutVa }" /></c:if>
		<div id="loutContainer">
		<u:convertJson var="jsonLoutList" value="${loutList }" 
			/><u:convertJson var="jsonVa" value="${wfFormRegDVo.attrVa }" 
			/><u:input type="hidden" id="tabYn" value="${empty jsonVa['tabYn'] ? 'Y' : jsonVa['tabYn']}" 
			/><c:forEach items="${jsonLoutList}" var="json" varStatus="status"
			><div class="loutFormArea" id="${loutPrefix }${json['loutId'] }" <c:if test="${status.index>0 }">style="display:none;"</c:if>><%-- <u:title titleId="wf.jsp.codeList.title" type="small" alt="코드목록" >
		<u:titleIcon type="up" onclick="move('up', '${loutPrefix }${json['loutId'] }');" auth="W" />
		<u:titleIcon type="down" onclick="move('down', '${loutPrefix }${json['loutId'] }');" auth="W" />
		</u:title> --%>
			<u:listArea id="${loutPrefix }${json['loutId'] }ListArea" colgroup="6%,,30%,25%">
			<tr id="headerTr">
				<th class="head_bg">&nbsp;</th>
				<th class="head_ct"><u:msg titleId="cols.itemNm" alt="항목명" /></th>
				<th class="head_ct"><u:msg titleId="wf.cols.typ" alt="구분" /></th>
				<th class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></th>
			</tr>
				<c:forEach items="${json['list']}" var="list" varStatus="listStatus">
					<c:if test="${list['type'] eq 'table' }"><c:if test="${!empty list['row'] }">
						<c:forEach items="${list['row']}" var="row" varStatus="rowStatus"><c:if test="${!empty row['cell'] }">
						<c:forEach items="${row['cell']}" var="cell" varStatus="cellStatus">
							<c:if test="${!empty cell['components'] }">
								<c:forEach items="${cell['components']}" var="component" varStatus="componentStatus"><c:set var="componentId" value="${component['id']}"
								/><c:if test="${!empty wfFormColmLVoMap[componentId] }"
								><u:set var="colmUseYn" test="${!empty loutVa && !empty loutVa['loutAttr'] && !empty loutVa['loutAttr'][componentId]}" value="${loutVa['loutAttr'][componentId] }" elseValue="Y"
								/><tr><td width="32" class="bodybg_ct"><input type="checkbox" value="${wfFormColmLVoMap[componentId]}" 
									/><u:input type="hidden" id="colmId" value="${wfFormColmLVoMap[componentId]}" 
									/><u:input type="hidden" id="colmNm" value="${componentId}" /></td>
									<td class="body_lt"><u:out value="${component['title'] }"/></td>
									<td class="body_lt"><u:msg titleId="wf.form.items.${component['coltyp'] }"/></td>
									<td class="body_lt"><select id="useYn" name="useYn" <u:elemTitle titleId="cols.useYn" />
									><u:option value="Y" titleId="cm.option.use" alt="사용" checkValue="${colmUseYn }"/>	<u:option value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${colmUseYn }"/></select></td>
									</tr></c:if>
								</c:forEach>
							</c:if>
						</c:forEach></c:if></c:forEach>
					</c:if></c:if><c:if test="${list['type'] eq 'file' || list['type'] eq 'editor'}"><c:set var="jsonMap" value="${jsonVa[list['jsonId']] }" scope="request"
					/><u:set var="colmUseYn" test="${!empty loutVa && !empty loutVa['loutAttr'] && !empty loutVa['loutAttr'][list['jsonId']]}" value="${loutVa['loutAttr'][list['jsonId']] }" elseValue="Y"
								/><tr><td width="32" class="bodybg_ct"><input type="checkbox" value="${list['jsonId']}" 
									/><u:input type="hidden" id="colmId" value="${wfFormColmLVoMap[list['jsonId']]}" 
									/><u:input type="hidden" id="colmNm" value="${list['jsonId']}" /></td>
									<td class="body_lt"><u:out value="${jsonMap['name'] }"/></td>
									<td class="body_lt"><u:msg titleId="wf.form.items.${list['type'] }"/></td>
									<td class="body_lt"><select id="useYn" name="useYn" <u:elemTitle titleId="cols.useYn" />
									><u:option value="Y" titleId="cm.option.use" alt="사용" checkValue="${colmUseYn }"/>	<u:option value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${colmUseYn }"/></select></td>
									</tr>
					</c:if>
				</c:forEach>
				</u:listArea>
				</div>
				</c:forEach>
			</div></c:if>
	</div>
</u:titleArea>
<div class="color_txt"><img src="${_cxPth}/images/${_skin}/scd_all_on.png"/><u:msg titleId="wf.form.louts.tab" alt="탭 설정(해제)"/></div>