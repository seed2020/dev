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
	
	request.setAttribute("run", ServerConfig.IS_RUN);// 실제 운영서버 여부
%>
<script type="text/javascript">
<!--<%// 상하 이동 %>
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
}
<% // [팝업:저장] 테이블 저장 %>
function saveDisp() {
	var $form = $('#setListOrdrForm');
	$form.attr('method','post');
	$form.attr('action','./transDisp.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form[0].submit();
}
//-->
</script>
<div style="width:600px">
<form id="setListOrdrForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<c:if test="${!empty param.catId }"><u:input type="hidden" id="catId" value="${param.catId}" /></c:if>
<c:if test="${!empty param.itemTypCd }"><u:input type="hidden" id="itemTypCd" value="${param.itemTypCd}" /></c:if>

<u:input type="hidden" id="dialog" value="setListOrdrPop" />
<c:set var="notAtrbIds" value="clsNm" /><!-- 정렬조건 제외할 속성명 -->
<u:listArea id="listArea" colgroup="6%,,20%,20%,30%">
	<tr id="headerTr">
	<th class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></th>
	<th class="head_ct"><u:msg titleId="cols.itemNm" alt="항목명" /></th>
	<th class="head_ct"><u:msg titleId="pt.jsp.setLstSetup.lrAlign" alt="좌우 정렬" /></th>
	<th class="head_ct"><u:msg titleId="cols.wdth" alt="넓이" /></th>
	<th class="head_ct"><u:msg titleId="pt.jsp.setLstSetup.dataSort" alt="데이터 기본 정렬" /></th>
	</tr>
	<c:forEach items="${itemDispList}" var="dispVo" varStatus="status">
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" >
		<td class="bodybg_ct"><input type="checkbox" value="${dispVo.itemId}" id="${dispVo.itemId}"
			/><input type="hidden" name="atrbId" value="${dispVo.atrbId}" 
			/><input type="hidden" name="sortOptVa" value="${dispVo.sortOptVa }"
			/><u:input type="hidden" id="itemId" name="itemId" value="${dispVo.itemId}" 
			/></td>
		<td class="body_lt">${dispVo.colmVo.itemDispNm}</td>
		<td class="bodybg_ct"><select id="alnVa" name="alnVa"<u:elemTitle titleId="pt.jsp.setLstSetup.lrAlign" alt="좌우 정렬" /> ><c:forEach
			items="${aligns}" var="align" varStatus="alignStatus">
			<u:option value="${align[0]}" title="${align[1]}" selected="${align[0] == dispVo.alnVa or (empty dispVo.alnVa and alignStatus.index==1)}"/></c:forEach></select>
		</td>
		<td class="bodybg_ct"><select id="wdthPerc" name="wdthPerc"<u:elemTitle titleId="cols.wdth" alt="넓이" /> >
			<option value=""> -- </option><c:forEach
			items="${widths}" var="width" varStatus="widthStatus"><u:set test="true" var="_width" value='${width.concat("%")}' />
			<u:option value='${_width}' title='${_width}' checkValue='${dispVo.wdthPerc}'/></c:forEach></select>
		</td>
		<td class="bodybg_ct" id="dataSortVaArea"><c:if
			test="${dispVo.sortOptVa == 'none' || fn:contains(notAtrbIds,dispVo.atrbId)}"
			><input type="hidden" name="dataSortVa" value="${dispVo.dataSortVa}" /></c:if><c:if
			test="${dispVo.sortOptVa != 'none' && !fn:contains(notAtrbIds,dispVo.atrbId)}"
			><select id="dataSortVa" name="dataSortVa" onchange="unselectSort(this);"<u:elemTitle titleId="pt.jsp.setLstSetup.dataSort" alt="데이터 기본 정렬" />>
			<option value=""> -- </option><c:forEach
			items="${sorts}" var="sort" varStatus="sortStatus">
			<u:option value="${sort[0]}" title="${sort[1]}" checkValue="${dispVo.dataSortVa}"/></c:forEach></select></c:if>
		</td>
		</tr>
	</c:forEach>
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="hiddenTr" style="display:none;" ></tr>
</u:listArea>

<u:blank />

<u:buttonArea>
	<u:button titleId="cm.btn.up" alt="위로이동" onclick="move('up');" auth="A" />
	<u:button titleId="cm.btn.down" alt="아래로이동" onclick="move('down');" auth="A" />
	<u:button titleId="cm.btn.save" onclick="saveDisp();" alt="저장" auth="A" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>