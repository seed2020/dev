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

	String[] widths = { "5", "7", "9", "10", "12", "15", "17", "20", "22", "25", "27", "30", "32", "35", "37", "40", "42", "45", "47", "50", "52", "55", "57", "60" };
	request.setAttribute("widths", widths);
	
	String[] yns = { "Y", "N" };
	request.setAttribute("yns", yns);
	
	String[] dtlWidths = { "0.5", "1"};
	request.setAttribute("dtlWidths", dtlWidths);
	
	request.setAttribute("run", ServerConfig.IS_RUN);// 실제 운영서버 여부
%>
<script type="text/javascript">
<!--
<%// [순서조절:위로,아래로] 서버에 저장하지 않고 화면상에서만 순서 조정함 %>
function moveDisp(direction){
	var i, arr = getCheckedTrs("cm.msg.noSelect"); <% // cm.msg.noSelect=선택한 항목이 없습니다. %>
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
}<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
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
}<%// [select] 넓이가 바뀌면 이미지 변경 %>
function chnWdth(obj){
	var parent=$(obj).closest('td');
	parent.find('div[id^="layout"]').hide();
	if($(obj).val()=='0.5')
		parent.find('#layout1').show();
	else if($(obj).val()=='1')
		parent.find('#layout2').show();
	
}
<% // [팝업:저장] 테이블 저장 %>
function saveDisp() {
	var idPrefix='';
	<c:if test="${empty isItemDtl }">idPrefix='list';</c:if>
	<c:if test="${!empty isItemDtl && isItemDtl==true}">idPrefix='read';</c:if>
	if(idPrefix!=''){
		$('input[name="'+idPrefix+'DispOrdr"]').each(function(idx) {
			var ordr = idx + 1;
			$(this).val(ordr);
		});	
	}
	var $form = $('#setListOrdrForm');
	$form.attr('method','post');
	$form.attr('action','./transDisp.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form[0].submit();
}
//-->
</script>
<div style="width:${!empty isItemDtl && isItemDtl==true ? 300 : 450}px">
<form id="setListOrdrForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="brdId" value="${param.brdId}" />
<c:if test="${empty isItemDtl }"><u:input type="hidden" id="dialog" value="setListOrdrPop" /></c:if>
<c:if test="${!empty isItemDtl && isItemDtl==true}"><u:input type="hidden" id="dialog" value="setDtlOrdrDialog" /></c:if>
<c:if test="${empty isItemDtl }">
<c:if test="${!empty isListCondApply && isListCondApply==true }"><u:input type="hidden" id="listCondApplyYn" value="Y" /></c:if>
<%-- <c:set var="notColmTyps" value="USER,DEPT" /> --%><!-- 정렬조건 제외할 속성명 -->
<u:set var="colgroup" test="${!empty isListCondApply && isListCondApply==true }" value="6%,,20%,15%,25%" elseValue="6%,,20%,15%"/>
<u:listArea id="listArea" colgroup="${colgroup }">
<tr id="headerTr">
	<td class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td>
	<th class="head_ct"><u:msg titleId="cols.itemNm" alt="항목명" /></th>
	<th class="head_ct"><u:msg titleId="pt.jsp.setLstSetup.lrAlign" alt="좌우 정렬" /></th>
	<th class="head_ct"><u:msg titleId="cols.wdth" alt="넓이" /></th>	
	<%-- <th class="head_ct"><u:msg titleId="pt.jsp.setLstSetup.dataSort" alt="데이터 기본 정렬" /></th> --%>
	<c:if test="${!empty isListCondApply && isListCondApply==true }"><th class="head_ct"><u:msg titleId="bb.cols.title.mobile" alt="모바일(대표)컬럼" /></th></c:if>
	</tr>
<c:if test="${fn:length(baColmDispDVoList) == 0}">
	<tr>
	<td class="nodata" colspan="${!empty isListCondApply && isListCondApply==true ? 5 : 4}"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(baColmDispDVoList) > 0}">
	<c:forEach items="${baColmDispDVoList}" var="dispVo" varStatus="status">
	<c:set var="colmIndex" value="_${status.index+1}" />
	<%-- <c:set var="colmVo" value="${dispVo.colmVo}" />
	<c:set var="colmTyp" value="${colmVo.colmTyp}" /> --%>
	<tr id="tr${dispVo.colmId}">
	<td class="bodybg_ct"><input type="checkbox" value="${dispVo.colmId}" />
		<u:input type="hidden" id="colmId${colmIndex}" name="colmId" value="${dispVo.colmId}" 
		/><u:input type="hidden" id="sortOptVa${colmIndex}" name="sortOptVa" value="${dispVo.sortOptVa}" 
		/><u:input type="hidden" id="listDispOrdr${colmIndex}" name="listDispOrdr" value="${dispVo.listDispOrdr}" />
		</td>
	<td class="body_lt">${dispVo.colmVo.rescNm}</td>
	<td class="bodybg_ct"><select id="alnVa" name="alnVa"<u:elemTitle titleId="pt.jsp.setLstSetup.lrAlign" alt="좌우 정렬" /> ><c:forEach
			items="${aligns}" var="align" varStatus="alignStatus">
			<u:option value="${align[0]}" title="${align[1]}" selected="${align[0] == dispVo.alnVa or (empty dispVo.alnVa and alignStatus.index==1)}"/></c:forEach></select>
		</td>
		<td class="bodybg_ct"><select id="wdthPerc" name="wdthPerc"<u:elemTitle titleId="cols.wdth" alt="넓이" /> >
			<option value=""> -- </option><c:forEach
			items="${widths}" var="width" varStatus="widthStatus"><u:set test="true" var="_width" value='${width.concat("%")}' />
			<u:option value='${_width}' title='${_width}' checkValue='${dispVo.wdthPerc}'/></c:forEach></select>
		</td>
		<%-- <td class="bodybg_ct" id="dataSortVaArea"><c:if
			test="${dispVo.sortOptVa == 'none' || fn:contains(notColmTyps,colmTyp)}"
			><input type="hidden" name="dataSortVa" value="${dispVo.dataSortVa}" /></c:if><c:if
			test="${dispVo.sortOptVa != 'none' && !fn:contains(notColmTyps,colmTyp)}"
			><select id="dataSortVa" name="dataSortVa" onchange="unselectSort(this);"<u:elemTitle titleId="pt.jsp.setLstSetup.dataSort" alt="데이터 기본 정렬" />>
			<option value=""> -- </option><c:forEach
			items="${sorts}" var="sort" varStatus="sortStatus">
			<u:option value="${sort[0]}" title="${sort[1]}" checkValue="${dispVo.dataSortVa}"/></c:forEach></select></c:if>
		</td> --%>
		<c:if test="${!empty isListCondApply && isListCondApply==true }"><td class="bodybg_ct"><u:radio name="titleColmId" value="${dispVo.colmId}" titleId="bb.cols.title.mobile" alt="모바일(대표)컬럼" inputClass="bodybg_ct" noLabel="true" checked="${(empty dispVo.titlColYn && status.first) || (!empty dispVo.titlColYn && dispVo.titlColYn eq 'Y') }"/></td></c:if>
	</tr>
	</c:forEach>
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="hiddenTr" style="display:none;" ></tr>
</c:if>
</u:listArea>
</c:if>
<c:if test="${!empty isItemDtl && isItemDtl==true}">
<u:listArea id="listArea" colgroup="6%,,40%">
<tr id="headerTr">
	<td class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td>
	<th class="head_ct"><u:msg titleId="cols.itemNm" alt="항목명" /></th>
	<th class="head_ct"><u:msg titleId="cols.wdth" alt="넓이" /></th>
	</tr>
<c:if test="${fn:length(baColmDispDVoList) == 0}">
	<tr>
	<td class="nodata" colspan="3"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(baColmDispDVoList) > 0}">
	<c:forEach items="${baColmDispDVoList}" var="dispVo" varStatus="status">
	<c:set var="colmIndex" value="_${status.index+1}" />
	<tr id="tr${dispVo.colmId}">
	<td class="bodybg_ct"><input type="checkbox" value="${dispVo.colmId}" />
		<u:input type="hidden" id="colmId${colmIndex}" name="colmId" value="${dispVo.colmId}" />
		<u:input type="hidden" id="readDispOrdr${colmIndex}" name="readDispOrdr" value="${dispVo.readDispOrdr}" />
		</td>
	<td class="body_lt">${dispVo.colmVo.rescNm}</td>
		<td class="bodybg_ct"><select id="colsWdthVa" name="colsWdthVa"<u:elemTitle titleId="cols.wdth" alt="넓이" /> onchange="chnWdth(this);"><c:forEach
			items="${dtlWidths}" var="width" varStatus="widthStatus">
			<u:option value='${width}' title='${width}' selected="${empty dispVo.colsWdthVa && widthStatus.index==0}" checkValue='${dispVo.colsWdthVa}'/></c:forEach></select>
			<div id="layout1" style="float:right;padding:3px;${!empty dispVo.colsWdthVa && dispVo.colsWdthVa ne '0.5' ? 'display:none;' : ''}"><img src="${_cxPth}/images/${_skin}/table_layout1.png" alt="" title="" height="18px" /></div>
			<div id="layout2" style="float:right;padding:3px;${empty dispVo.colsWdthVa || dispVo.colsWdthVa ne '1' ? 'display:none;' : ''}"><img src="${_cxPth}/images/${_skin}/table_layout2.png" alt="" title="" height="18px" /></div>
		</td>
	</tr>
	</c:forEach>
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="hiddenTr" style="display:none;" ></tr>
</c:if>
</u:listArea>
</c:if>
<u:blank />

<u:buttonArea>
	<u:button titleId="cm.btn.up" alt="위로이동" onclick="moveDisp('up');" auth="A" />
	<u:button titleId="cm.btn.down" alt="아래로이동" onclick="moveDisp('down');" auth="A" />
	<u:button titleId="cm.btn.save" onclick="saveDisp();" alt="저장" auth="A" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>