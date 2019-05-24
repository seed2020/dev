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
	String[][] sorts = {
		{"asc", properties.getMessage("cm.sort.asc", locale)},//내림 차순
		{"desc", properties.getMessage("cm.sort.desc", locale)}//오름 차순
	};
	request.setAttribute("sorts", sorts);

	//String[] widths = { "5", "7", "9", "10", "12", "15", "17", "20", "22", "25", "27", "30", "32", "35", "37", "40", "42", "45", "47", "50", "52", "55", "57", "60" };
	//request.setAttribute("widths", widths);
	
	String[] yns = { "Y", "N" };
	request.setAttribute("yns", yns);
	
	//request.setAttribute("run", ServerConfig.IS_RUN);// 실제 운영서버 여부
%><u:params var="paramsForList" excludes="formNo,mdCd" />
<script type="text/javascript">
<!--
<%// 체크된 목록 리턴 - uncheck : true 면 체크를 해제함 %>
function getChecked(uncheck){
	var va, arr = [];
	$("#listAreaFrom input:checked").each(function(){
		va = $(this).val();
		if(va!=null && va!=''){
			arr.push({colmId:va,colmNm:$(this).attr("data-colmNm"),itemNm:$(this).attr("data-itemNm")});
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

<%// [아이콘] 선택추가 %>
function addCols(){	
	var arr = getChecked(true);
	if(arr==null) return;
	var $tr, $hiddenTr = $("#listArea tbody:first #hiddenTr");
	var html = $hiddenTr[0].outerHTML;
	var vas = getAllCheckVas();
	arr.each(function(index, obj){
		if(vas==null || !vas.contains(obj.colmId)){
			$hiddenTr.before(html);
			$tr = $hiddenTr.prev();
			$tr.attr('id','tr'+obj.colmId);
			$tr.find("input[type='checkbox']").val(obj.colmId);
			$tr.find("input[name='colmId']").val(obj.colmId);
			$tr.find("input[name='colmNm']").val(obj.colmNm);
			$tr.find("td#itemNm").text(obj.itemNm);
			$tr.show();
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

<%// 테이블헤더의 제목 클릭 - 전체선택%>
function selectAll(obj){
	var selected = $(obj).attr('data-selected') == 'N' ? true : false;
	var value=selected ? 'Y' : 'N';
	$(obj).attr('data-selected', value);
	$chks = $("#colmListArea td.colmSelect select").not(':disabled');
	$chks.each(function(){
		if($(this).val()!=value) $(this).val(value);
	});
	$('colmListArea td.colmSelect select').uniform.update();
}
var changeOn = false;
<%// [select] 데이터 기본 정렬 변경 - 나머지 정렬 없게 설정함 %>
function unselectSort(obj){
	if(!changeOn){
		changeOn = true;
		$("#listArea select[name='dataSortVa']").each(function(){
			if(this!=obj){
				this.selectedIndex = 0;
				$(this).trigger("click");
			}
		});
		changeOn = false;
	}
}

<% // [하단버튼:취소] - 목록화면으로 이동 %>
function listForm(){
	parent.location.href="./listForm.do?${paramsForList}";
}

<% // [하단버튼:목록화면] - 목록화면 구성으로 이동 %>
function setListForm(mdCd){
	location.replace("./setListForm.do?${paramsForList}&formNo=${param.formNo}&mdCd="+mdCd);
}

<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
};

<%// 저장 %>
function saveColmList(){
	var $form = $("#setColmListForm");
	$form.attr('method','post');
	$form.attr('action','./transColmList.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form.submit();	
};

<%// 저장 %>
function save(){
	var $form = $("#setForm");
	$form.attr('method','post');
	$form.attr('action','./transListForm.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form.submit();	
};

$(document).ready(function() {
	setUniformCSS($('#selectMdCdArea'));
	<%// 행추가 영역 제외하고 uniform 적용%>
	$("#listArea tbody:first, #listAreaFrom tbody:first").children("[id!='hiddenTr']").each(function(){
		setJsUniform(this);
	});
});
//-->
</script>
<!-- 등록상세 json으로 변환 -->
<u:convertJson var="jsonVa" value="${wfFormRegDVo.attrVa }" />
<div id="selectMdCdArea" class="front notPrint">
	<div class="front_left">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td style="padding:3px 4px 0 0px"><u:title titleId="wf.jsp.setup.list.title" alt="목록 설정" menuNameFirst="true"/></td>
			<td class="width5"></td>
				<td class="frontinput">
					<select id="mdCd" name="mdCd" <u:elemTitle titleId="wf.cols.set.list" alt="목록구성" /> onchange="setListForm(this.value);">
						<u:option value="U" titleId="wf.option.list.dft" checkValue="${param.mdCd }" selected="${empty param.mdCd }"
						/><c:if test="${mobileEnable == 'Y' }"><u:option value="M" titleId="wf.option.list.mob" checkValue="${param.mdCd }"
						/></c:if><u:option value="P" titleId="wf.option.list.plt" checkValue="${param.mdCd }" />
					</select>
				</td>
	 		</tr>
		</table>
	</div>
</div>
<u:set var="isDftListYn" test="${empty param.mdCd || param.mdCd eq 'U' }" value="N" elseValue="N"/>
<div style="float:left; width:25%;">
<form id="setColmListForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="formNo" value="${param.formNo}" />
<u:title titleId="wb.jsp.setBcScrn.subtitle02" alt="선택할 항목" type="small"  />
<u:titleArea
	outerStyle="height:514px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV">
	<div id="colmListArea" style="padding:10px;">
		<u:set var="colgroup" test="${isDftListYn eq 'Y' }" value="5%,,25%" elseValue="5%,"/>
		<u:listArea id="listAreaFrom" colgroup="${colgroup }">
			<tr>
				<th width="36" class="head_rd"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listAreaFrom', this.checked);" value=""/></th>
				<th class="head_ct"><u:msg titleId="cols.itemNm" alt="항목명" /></th>
				<c:if test="${isDftListYn eq 'Y'}"><th class="head_ct"><a href="javascript:;" onclick="selectAll(this);"><u:msg titleId="wf.cols.lst.item" alt="목록항목" /></a></th></c:if>
			</tr>
			<c:if test="${!empty wfFormColmLVoList}">
				<c:forEach var="wfFormColmLVo" items="${wfFormColmLVoList}" varStatus="status"
				><c:set var="jsonMap" value="${jsonVa[wfFormColmLVo.colmNm] }" scope="request" 
				/><u:set var="emptyLangId" test="${wfFormColmLVo.colmTypCd eq 'label' }" value="label" elseValue="name"
				/><c:set var="langTitleId" value="${emptyLangId }RescVa_${_lang }" scope="request"
				/><u:set var="langTitle" test="${!empty jsonMap[langTitleId] }" value="${jsonMap[langTitleId] }" elseValue="${wfFormColmLVo.itemNm}"
				/><tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
						<td width="32" class="bodybg_ct"><input type="checkbox" value="${wfFormColmLVo.colmId}" data-colmNm="${wfFormColmLVo.colmNm }" data-itemNm="${langTitle }"
						/><u:input type="hidden" id="colmId" value="${wfFormColmLVo.colmId}" /></td>
						<td class="body_lt"><u:out value="${langTitle }"/></td>
						<c:if test="${isDftListYn eq 'Y'}"><td class="body_ct colmSelect"><select id="lstYn" name="lstYn" <u:elemTitle titleId="wf.cols.lst.item" /><c:if test="${wfFormColmLVo.deployYn eq 'Y' && wfFormColmLVo.lstYn eq 'N'}"> disabled="disabled"</c:if>>
						<u:option value="Y" titleId="cm.option.use" checkValue="${wfFormColmLVo.lstYn }" selected="${empty wfFormColmLVo.lstYn }"
						/><u:option value="N" titleId="cm.option.notUse" checkValue="${wfFormColmLVo.lstYn }" selected="${empty wfFormColmLVo.lstYn }"/></select
						><u:input type="hidden" id="lstYn" value="${wfFormColmLVo.lstYn }"/></td></c:if>
					</tr>
				</c:forEach>
			</c:if>
		</u:listArea>
	</div>
</u:titleArea>
</form>
<c:if test="${isDftListYn eq 'Y'}">
<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:;" onclick="saveColmList();" auth="A" />
</u:buttonArea>
<%-- <div class="color_txt">※ <u:msg titleId="wf.msg.column.maxDeploy" alt="배포 컬럼은 최대 30개 까지 설정 가능하고 한번 설정된 컬럼은 변경할 수 없습니다." /></div> --%>
</c:if>	
</div>

<div style="float:left; width:4%; text-align:center; margin:250px 0 0 0;">
	<table style="margin:0 auto 0 auto;" border="0" cellpadding="0" cellspacing="0">
		<tr><td><a href="javascript:addCols();"<u:elemTitle titleId="cm.btn.selAdd" alt="선택추가" type="image" />><img src="${_cxPth}/images/${_skin}/ico_right.png" width="20" height="20" /></a></td></tr>
		<tr><td class="height5"></td></tr>
		<tr><td><a href="javascript:removeCols();"<u:elemTitle titleId="cm.btn.selDel" alt="선택삭제" type="image" />><img src="${_cxPth}/images/${_skin}/ico_left.png" width="20" height="20" /></a></td></tr>
	</table>
</div>

<div style="float:right; width:71%;">
<form id="setForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="formNo" value="${param.formNo}" />
<u:input type="hidden" id="mdCd" value="${!empty mdCd ? mdCd : param.mdCd}" />
<u:title titleId="wb.jsp.setBcScrn.subtitle03" alt="선택된 항목" type="small" >
	<u:titleIcon type="up" href="javascript:move('up')" auth="A" />
	<u:titleIcon type="down" href="javascript:move('down')" auth="A" />
</u:title>
<u:titleArea
	outerStyle="height:514px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV">
	<div style="padding:10px;">
		<div class="listarea" id="listArea">
		<table class="listtable" border="0" cellpadding="0" cellspacing="1" >
		<colgroup>
			<col width="6%" /><col width="*" /><c:if test="${param.mdCd ne 'M' }"
			><col width="17%" /><col width="15%" 
			/></c:if><col width="20%" /><c:if test="${param.mdCd ne 'P' }"><col width="13%" /></c:if>
		</colgroup>
		<tbody style="border:0">
			<tr id="headerTr">
			<th class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></th>
			<th class="head_ct"><u:msg titleId="cols.itemNm" alt="항목명" /></th>
			<c:if test="${param.mdCd ne 'M' }">
			<th class="head_ct"><u:msg titleId="pt.jsp.setLstSetup.lrAlign" alt="좌우 정렬" /></th>
			<th class="head_ct"><u:msg titleId="cols.wdth" alt="넓이" /></th></c:if>
			<th class="head_ct"><u:msg titleId="pt.jsp.setLstSetup.dataSort" alt="데이터 기본 정렬" /></th>
			<c:if test="${param.mdCd ne 'P' }"><th class="head_ct"><u:msg titleId="cols.search" alt="검색" /></th></c:if>
			</tr>
			<c:forEach items="${wfFormLstDVoList}" var="wfFormLstDVo" varStatus="status"
			><c:set var="jsonMap" value="${jsonVa[wfFormLstDVo.colmNm] }" scope="request" 
			/><u:set var="emptyLangId" test="${wfFormColmLVo.colmTypCd eq 'label' }" value="label" elseValue="name"
			/><c:set var="langTitleId" value="${emptyLangId }RescVa_${_lang }" scope="request"
			/><u:set var="langTitle" test="${!empty jsonMap[langTitleId] }" value="${jsonMap[langTitleId] }" elseValue="${wfFormLstDVo.itemNm}"
			/><u:set test="${status.last}" var="trDisp" value="display:none"
			/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${wfFormLstDVo.colmId}"
			/><tr id="${trId}" style="${trDisp}" onmouseover="this.className='trover'" onmouseout="this.className='trout'" >
				<td class="bodybg_ct"><input type="checkbox" value="${wfFormLstDVo.colmId}" id="${wfFormLstDVo.colmId}"
					/><input type="hidden" name="sortOptVa" value="${wfFormLstDVo.sortOptVa }"
					/><u:input type="hidden" id="colmId" name="colmId" value="${wfFormLstDVo.colmId}" 
					/><u:input type="hidden" id="colmNm" name="colmNm" value="${wfFormLstDVo.colmNm}" 
					/></td>
				<td class="body_lt" id="itemNm">${langTitle}</td><c:if test="${param.mdCd ne 'M' }">
				<td class="bodybg_ct"><select id="alnVa" name="alnVa"<u:elemTitle titleId="pt.jsp.setLstSetup.lrAlign" alt="좌우 정렬" /> ><c:forEach
					items="${aligns}" var="align" varStatus="alignStatus">
					<u:option value="${align[0]}" title="${align[1]}" selected="${align[0] == wfFormLstDVo.alnVa or (empty wfFormLstDVo.alnVa and alignStatus.index==1)}"/></c:forEach></select>
				</td>
				<td class="body_ct"><select id="wdthPerc" name="wdthPerc"<u:elemTitle titleId="cols.wdth" alt="넓이" /> >
					<option value=""> -- </option><c:forEach begin="3" end="100" step="1"
					var="width" varStatus="widthStatus">
					<u:option value='${width}%' title='${width}%' checkValue='${wfFormLstDVo.wdthPerc}'/></c:forEach></select>
				</td></c:if>
				<td class="body_ct" id="dataSortVaArea"><c:if
					test="${wfFormLstDVo.sortOptVa == 'none'}"
					><input type="hidden" name="dataSortVa" value="${wfFormLstDVo.dataSortVa}" /></c:if><c:if
					test="${wfFormLstDVo.sortOptVa != 'none' }"
					><select id="dataSortVa" name="dataSortVa" onchange="unselectSort(this);"<u:elemTitle titleId="pt.jsp.setLstSetup.dataSort" alt="데이터 기본 정렬" />>
					<option value=""> -- </option><c:forEach
					items="${sorts}" var="sort" varStatus="sortStatus">
					<u:option value="${sort[0]}" title="${sort[1]}" checkValue="${wfFormLstDVo.dataSortVa}"/></c:forEach></select></c:if>
				</td><c:if test="${param.mdCd ne 'P' }"><td class="body_ct"><select id="srchYn" name="srchYn"<u:elemTitle titleId="cols.search" alt="검색" /> 
				><u:option value="Y" titleId="cm.option.use" alt="사용" checkValue="${wfFormLstDVo.srchYn }" selected="${empty wfFormLstDVo.srchYn}"
				/><u:option value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${wfFormLstDVo.srchYn }" /></select>
				</td></c:if>
				</tr>
			</c:forEach>
		</tbody></table></div>
	</div>
</u:titleArea>
</form>
<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:;" onclick="save();" auth="A" />
	<u:button titleId="cm.btn.cancel" alt="취소" href="javascript:;" onclick="listForm();"/>
</u:buttonArea>

</div>



