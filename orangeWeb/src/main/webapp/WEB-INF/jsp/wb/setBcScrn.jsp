<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.Locale,
			com.innobiz.orange.web.cm.utils.MessageProperties,
			com.innobiz.orange.web.cm.utils.SessionUtil,
			com.innobiz.orange.web.cm.config.ServerConfig"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	Locale locale = SessionUtil.getLocale(request);
	MessageProperties properties = MessageProperties.getInstance();
	String[][] aligns = {
		{"left", properties.getMessage("cm.aln.left", locale)},//좌측 정렬
		{"center", properties.getMessage("cm.aln.center", locale)},//중앙 정렬
		{"right", properties.getMessage("cm.aln.right", locale)}//우측 정렬
	};
	request.setAttribute("aligns", aligns);

	String[] widths = { "5", "7", "9", "10", "12", "15", "17", "20", "22", "25", "27", "30", "32", "35", "37", "40", "42", "45", "47", "50", "52", "55", "57", "60" };
	request.setAttribute("widths", widths);
	
	String[] yns = { "Y", "N" };
	request.setAttribute("yns", yns);
	
	//request.setAttribute("run", ServerConfig.IS_RUN);// 실제 운영서버 여부
%>
<script type="text/javascript">
<!--
<%// 체크된 목록 리턴 - uncheck : true 면 체크를 해제함 %>
function getChecked(uncheck){
	var va, arr = [];
	$("#listAreaFrom input:checked").each(function(){
		va = $(this).val();
		if(va!=null && va!=''){
			arr.push({atrbId:va,msgId:$(this).attr("data-msgId"),msgNm:$(this).attr("data-msgNm")});
		}
		if(uncheck){
			$(this).trigger('click');
		}
	});
	if(arr.length==0){
		alertMsg("cm.msg.noSelect");<%//cm.msg.noSelect=선택한 항목이 없습니다.%>
		return null;
	}
	return arr;
};

// 표시여부가 y로 selected
function getSelected( checkValue ){
	var va, arr = [];
	$("#listArea select[name='dispYn'] option:selected").each(function(){
		va = $(this).val();
		if(va!=null && va!='' && va == checkValue){
			arr.push(va);
		}
	});
	return arr;
};

<%// [아이콘] 선택추가 %>
function addCols(){	
	var arr = getChecked(true);
	if(arr==null) return;
	var $tr, $hiddenTr = $("#listArea tbody:first #hiddenTr");
	var html = $hiddenTr[0].outerHTML;
	var vas = getAllCheckVas();
	var dispYnCnt = getSelected('Y').length;
	arr.each(function(index, obj){
		if(vas==null || !vas.contains(obj.atrbId)){
			$hiddenTr.before(html);
			$tr = $hiddenTr.prev();
			$tr.attr('id','tr'+obj.atrbId);
			$tr.find("input[type='checkbox']").val(obj.atrbId);
			$tr.find("input[name='atrbId']").val(obj.atrbId);
			$tr.find("input[name='msgId']").val(obj.msgId);
			$tr.find("td#msgNm").text(obj.msgNm);
			$tr.show();
			if(dispYnCnt+index > 6 ){
				$tr.find("select[name='dispYn']").val('N');
			}
			setJsUniform($tr[0]);
		}
	});
};
<%// [아이콘] 선택제거 %>
function removeCols(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr==null) return;
	arr.each(function(index, tr){
		$(tr).remove();
	}, true);
};
<%// [아이콘] 상하 이동 %>
function move(direction){
	var i, arr = getCheckedTrs("cm.msg.noSelect");
	if(arr==null) return;
	
	var $node, $prev, $next, $std;
	if(direction=='up'){
		$std = $('#headerTr');
		for(i=0;i<arr.length;i++){
			$node = $(arr[i]);
			$prev = $node.prev();
			if($prev[0]!=$std[0]){
				$prev.before($node);
			}
			$std = $node;
		}
	} else if(direction=='down'){
		$std = $('#hiddenTr');
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
<%//checkbox 가 선택된 id 목록 리턴 %>
function getAllCheckVas(){
	var arr=[], va;
	$("#listArea tbody:first input[type='checkbox']").each(function(){
		va = $(this).val();
		if(va!='' && va!=null) arr.push(va);
	});
	if(arr.length==0){
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

//넓이가 지정되지 않은 목록 체크
function fnWdthSelCheck(){
	var cnt = 0;
	$('#listArea tbody:first tr').each(function(){
		id = $(this).attr('id');
		if(id!='hiddenTr') {
			if($(this).find('select[name=wdthPerc]').val() == '') cnt++;
		}
	});
	return cnt;
};

<%// 저장 %>
function save(){
	var cnt = 0;
	$("#listArea select[name=dispYn]").each(function(){
		if($(this).val() == "Y") cnt++;
	});
	if(cnt > 7 ){
		alertMsg("wb.jsp.setBcScrn.tx01");//명함표시여부는 최대 6개까지만 설정이 가능합니다.
		return;
	}	
	
	var etcValidator = false;
	if(fnWdthSelCheck() > 1 ){
		etcValidator = true;
	};
	
	<%// 서버 전송%>
	if ( ( etcValidator && confirmMsg("wb.cfrm.setBcScrn.wdth") ) || ( !etcValidator && true/*confirmMsg("cm.cfrm.save")*/  ) ) {
		var $form = $('#setBcScrn');
		$form.attr('method','post');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
};

$(document).ready(function() {
	<%// 행추가 영역 제외하고 uniform 적용%>
	$("#listArea tbody:first").children("[id!='hiddenTr']").each(function(){
		setJsUniform(this);
	});
	$("#listAreaFrom tbody:first").children("[id!='hiddenTr']").each(function(){
		setJsUniform(this);
	});
	$("#listScrnArea tbody:first").children("[id!='hiddenTr']").each(function(){
		setJsUniform(this);
	});
});
//-->
</script>

<u:title titleId="wb.jsp.setBcScrn.title" alt="명함 화면 설정" menuNameFirst="true"/>

<form id="setBcScrn" action="./transBcScrn.do">
<u:input type="hidden" id="menuId" value="${menuId}" />
<% // 폼 필드 %>
<u:listArea id="listScrnArea">
<tr>
	<td width="27%" class="head_lt"><u:msg titleId="wb.jsp.setScrn.posQuickAdd" alt="빠른추가 창 위치"/></td>
	<td width="73%">
		<u:checkArea>
			<u:radio name="inputWndLocCd" value="UP" titleId="wb.option.up" inputClass="bodybg_lt" checkValue="${wbBcUserScrnSetupRVo.inputWndLocCd }" />
			<u:radio name="inputWndLocCd" value="DOWN" titleId="wb.option.down" inputClass="bodybg_lt" checkValue="${wbBcUserScrnSetupRVo.inputWndLocCd }" checked="${empty wbBcUserScrnSetupRVo.inputWndLocCd }" />
			<u:radio name="inputWndLocCd" value="HIDE" titleId="wb.option.hide" inputClass="bodybg_lt" checkValue="${wbBcUserScrnSetupRVo.inputWndLocCd }" />
		</u:checkArea>
	</td>
</tr>

<tr>
	<td class="head_lt"><u:msg titleId="wb.jsp.setScrn.viewTyp" alt="보기 형식"/></td>
	<td>
		<u:checkArea>
			<u:radio name="lstTypCd" value="LIST" titleId="wb.option.viewList" inputClass="bodybg_lt" checkValue="${wbBcUserScrnSetupRVo.lstTypCd }"/>
			<u:radio name="lstTypCd" value="BC" titleId="wb.option.viewBc" inputClass="bodybg_lt" checkValue="${wbBcUserScrnSetupRVo.lstTypCd }" checked="${empty wbBcUserScrnSetupRVo.lstTypCd }"/>
		</u:checkArea>
	</td>
</tr>
</u:listArea>

<% // SUBTITLE %>
<div class="titlearea">
	<div class="tit_left">
		<dl>
		<dd class="title_s"><u:msg titleId="wb.jsp.setBcScrn.subtitle01" alt="목록 항목 선택" /></dd>
		</dl>
	</div>
</div>

<div style="float:left; width:48%;">
	<u:title titleId="wb.jsp.setBcScrn.subtitle03" alt="선택된 항목" type="small" >
		<u:titleIcon type="up" href="javascript:move('up')" auth="A" />
		<u:titleIcon type="down" href="javascript:move('down')" auth="A" />
	</u:title>
	<u:titleArea
		outerStyle="height:514px; overflow-x:hidden; overflow-y:auto;"
		innerStyle="NO_INNER_IDV">
		<div id="setBcScrnDiv" style="padding:10px;">
			<u:listArea id="listArea" >
				<tr id="headerTr">
					<th width="36" class="head_rd"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></th>
					<th class="head_ct"><u:msg titleId="cols.itemNm" alt="항목명" /></th>
					<th width="15%" class="head_ct"><u:msg titleId="pt.jsp.setLstSetup.lrAlign" alt="좌우 정렬" /></th>
					<th width="12%" class="head_ct"><u:msg titleId="cols.wdth" alt="넓이" /></th>
					<th width="22%" class="head_ct"><u:msg titleId="wb.cols.dispYn" alt="명함표시여부" /></th>
				</tr>
				<c:if test="${!empty wbBcUserLstSetupRVoList}">
					<c:forEach var="list" items="${wbBcUserLstSetupRVoList}" varStatus="status">
						<u:set test="${status.last}" var="trDisp" value="display:none" />
						<u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${list.atrbId}" />
						<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="${trId}" style="${trDisp}">
							<td width="32" class="bodybg_ct"><input type="checkbox" value="${list.atrbId}" />
								<input type="hidden" name="atrbId" value="${list.atrbId}" />
								<input type="hidden" name="msgId" value="${list.msgId}" />
							</td>
							<td class="body_lt" id="msgNm"><c:if test="${not empty list.msgId}"><u:msg titleId="${list.msgId}" /></c:if></td>
							<td class="bodybg_ct">
								<select id="alnVa" name="alnVa"<u:elemTitle titleId="pt.jsp.setLstSetup.lrAlign" alt="좌우 정렬" /> >
									<c:forEach items="${aligns}" var="align" varStatus="alignStatus">
										<u:option value="${align[0]}" title="${align[1]}" selected="${align[0] == list.alnVa || (empty list.alnVa and alignStatus.index==1)}"/>
									</c:forEach>
								</select>
							</td>
							<td class="bodybg_ct">
								<select id="wdthPerc" name="wdthPerc"<u:elemTitle titleId="cols.wdth" alt="넓이" /> >
									<option value=""> -- </option>
									<c:forEach	items="${widths}" var="width" varStatus="widthStatus"><u:set test="true" var="_width" value='${width.concat("%")}' />
										<u:option value='${_width}' title='${_width}' checkValue='${list.wdthPerc}'/>
									</c:forEach>
								</select>
							</td>
							<td class="bodybg_ct">
								<select id="dispYn" name="dispYn"<u:elemTitle titleId="cols.dispYn" alt="표시여부" /> >
									<c:forEach	items="${yns}" var="yn" varStatus="ynStatus">
										<u:option value='${yn}' title='${yn}' checkValue='${list.dispYn}'/>
									</c:forEach>
								</select>
							</td>
						</tr>
					</c:forEach>
				</c:if>
			</u:listArea>
		</div>
	</u:titleArea>
	<div class="color_txt">※ <u:msg titleId="wb.jsp.setBcScrn.tx01" alt="명함보기에서는 최대 6개까지 표시가 가능합니다." /></div>
</div>

<div style="float:left; width:4%; text-align:center; margin:250px 0 0 0;">
	<table style="margin:0 auto 0 auto;" border="0" cellpadding="0" cellspacing="0">
		<tr><td><a href="javascript:removeCols();"<u:elemTitle titleId="cm.btn.selDel" alt="선택삭제" type="image" />><img src="${_cxPth}/images/${_skin}/ico_right.png" width="20" height="20" /></a></td></tr>
		<tr><td class="height5"></td></tr>
		<tr><td><a href="javascript:addCols();"<u:elemTitle titleId="cm.btn.selAdd" alt="선택추가" type="image" />><img src="${_cxPth}/images/${_skin}/ico_left.png" width="20" height="20" /></a></td></tr>
	</table>
</div>

<div style="float:right; width:48%;">
<u:title titleId="wb.jsp.setBcScrn.subtitle02" alt="선택할 항목" type="small"  />
<u:titleArea
	outerStyle="height:514px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV">
	<div id="setFromBcBumk" style="padding:10px;">
		<u:listArea id="listAreaFrom" >
			<tr id="headerTr">
				<th width="36" class="head_rd"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listAreaFrom', this.checked);" value=""/></th>
				<th class="head_ct"><u:msg titleId="cols.itemNm" alt="항목명" /></th>
			</tr>
			<c:if test="${!empty wbBcLstSetupBVoList}">
				<c:forEach var="list" items="${wbBcLstSetupBVoList}" varStatus="status">
					<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
						<td width="32" class="bodybg_ct"><input type="checkbox" value="${list.atrbId}" data-msgId="${list.msgId }" data-msgNm="<u:term termId='${list.msgId}' />"/></td>
						<td class="body_lt"><u:term termId="${list.msgId}" /></td>
					</tr>
				</c:forEach>
			</c:if>
		</u:listArea>
	</div>
</u:titleArea>

</div>

<u:blank />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:;" onclick="save();" auth="R" />
</u:buttonArea>

</form>

