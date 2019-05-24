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
<!--
<%// [아이콘] 선택추가 %>
function addCols(arr){
	var $tr, $hiddenTr = $("#listArea tbody:first #hiddenTr");
	var html = $hiddenTr[0].outerHTML;
	var vas = getAllCheckVas();
	arr.each(function(index, obj){
		if(vas==null || !vas.contains(obj.atrbId)){
			$hiddenTr.before(html);
			$tr = $hiddenTr.prev();
			$tr.attr('id','tr'+obj.atrbId);
			$tr.find("input[type='checkbox']").val(obj.atrbId).attr('id', obj.atrbId);
			$tr.find("input[name='atrbId']").val(obj.atrbId);
			$tr.find("input[name='msgId']").val(obj.msgId);
			$tr.find("input[name='sortOptVa']").val(obj.sortOptVa);
			$tr.find("td#rescNm").html("<label for='"+obj.atrbId+"'>"+obj.rescNm+"</label>");
			if(obj.sortOptVa=='none'){
				$tr.find("td#dataSortVaArea").html('<input type="hidden" name="dataSortVa" value="" />');
			}
			$tr.show();
			setJsUniform($tr[0]);
		}
	});
}
<%// [아이콘] 선택제거 %>
function removeCols(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr==null) return;
	arr.each(function(index, tr){
		$(tr).remove();
	}, true);
}
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
}
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
}
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
}<c:if test="${not run}">
function saveAsDefault(){<%// 기본값으로 저장하기 %>
	if(getAllCheckVas()!=null){
		var $form = $("#lstEnvForm");
		$form.attr('method','post');
		$form.attr('action','./transLstSetupAsDefault.do');
		$form.attr('target','dataframeForFrame');
		$form.submit();
	} else {
		alertMsg("cm.msg.nodata.toSave");<%//cm.msg.nodata.toSave=저장할 데이터가 없습니다.%>
	}
}
</c:if>
function setDefault(){<%// 기본값 설정 %>
	if(getAllCheckVas()!=null){
		var $form = $("#lstEnvForm");
		$form.attr('method','post');
		$form.attr('action','./transLstSetupDefault.do');
		$form.attr('target','dataframeForFrame');
		$form.submit();
	} else {
		alertMsg("cm.msg.nodata.toSave");<%//cm.msg.nodata.toSave=저장할 데이터가 없습니다.%>
	}
}
function saveLstEnv(){<%// 저장 %>
	if(getAllCheckVas()!=null){
		var $form = $("#lstEnvForm");
		$form.attr('method','post');
		$form.attr('action','./transLstSetup.do');
		$form.attr('target','dataframeForFrame');
		$form.submit();
	} else {
		alertMsg("cm.msg.nodata.toSave");<%//cm.msg.nodata.toSave=저장할 데이터가 없습니다.%>
	}
	
}
<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
}
$(document).ready(function() {
	<%// 행추가 영역 제외하고 uniform 적용%>
	$("#listArea tbody:first").children("[id!='hiddenTr']").each(function(){
		setJsUniform(this);
	});
});
//-->
</script>

<form id="lstEnvForm" style="padding:10px;">
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="lstSetupMetaId" value="${param.lstSetupMetaId}" />

<%// 목록 %>
<u:listArea id="listArea" noBottomBlank="true" >

	<tr id="headerTr">
		<th width="4%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></th>
		<th class="head_ct"><u:msg titleId="pt.jsp.setLstSetup.colNm" alt="컬럼명" /></th>
		<th width="20%" class="head_ct"><u:msg titleId="pt.jsp.setLstSetup.lrAlign" alt="좌우 정렬" /></th>
		<th width="15%" class="head_ct"><u:msg titleId="cols.wdth" alt="넓이" /></th>
		<th width="10%" class="head_ct"><u:msg titleId="cols.dispYn" alt="표시여부" /></th>
		<th width="20%" class="head_ct"><u:msg titleId="pt.jsp.setLstSetup.dataSort" alt="데이터 기본 정렬" /></th>
	</tr>

	<c:forEach items="${ptLstSetupDVoList}" var="ptLstSetupDVo" varStatus="status"
		><u:set test="${status.last}" var="trDisp" value="display:none"
		/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${ptLstSetupDVo.atrbId}"
		/>
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="${trId}" style="${trDisp}" >
		<td class="bodybg_ct"><input type="checkbox" value="${ptLstSetupDVo.atrbId}" id="${ptLstSetupDVo.atrbId}"
			/><input type="hidden" name="atrbId" value="${ptLstSetupDVo.atrbId}" 
			/><input type="hidden" name="msgId" value="${ptLstSetupDVo.msgId}" 
			/><input type="hidden" name="sortOptVa" value="${ptLstSetupDVo.sortOptVa}"
			/></td>
		<td class="body_lt" id="rescNm"><label for="${ptLstSetupDVo.atrbId}"><u:term termId="${ptLstSetupDVo.msgId}" /></label></td>
		<td class="bodybg_ct"><select id="alnVa" name="alnVa"<u:elemTitle titleId="pt.jsp.setLstSetup.lrAlign" alt="좌우 정렬" /> ><c:forEach
			items="${aligns}" var="align" varStatus="alignStatus">
			<u:option value="${align[0]}" title="${align[1]}" selected="${align[0] == ptLstSetupDVo.alnVa or (empty ptLstSetupDVo.alnVa and alignStatus.index==1)}"/></c:forEach></select>
		</td>
		<td class="bodybg_ct"><select id="wdthPerc" name="wdthPerc"<u:elemTitle titleId="cols.wdth" alt="넓이" /> >
			<option value=""> -- </option><c:forEach
			items="${widths}" var="width" varStatus="widthStatus"><u:set test="true" var="_width" value='${width.concat("%")}' />
			<u:option value='${_width}' title='${_width}' checkValue='${ptLstSetupDVo.wdthPerc}'/></c:forEach></select>
		</td>
		<td class="bodybg_ct"><select id="dispYn" name="dispYn"<u:elemTitle titleId="cols.dispYn" alt="표시여부" /> ><c:forEach
			items="${yns}" var="yn" varStatus="ynStatus">
			<u:option value='${yn}' title='${yn}' checkValue='${ptLstSetupDVo.dispYn}'/></c:forEach></select>
		</td>
		<td class="bodybg_ct" id="dataSortVaArea"><c:if
			test="${ptLstSetupDVo.sortOptVa == 'none'}"
			><input type="hidden" name="dataSortVa" value="${ptLstSetupDVo.dataSortVa}" /></c:if><c:if
			test="${ptLstSetupDVo.sortOptVa != 'none'}"
			><select id="dataSortVa" name="dataSortVa" onchange="unselectSort(this);"<u:elemTitle titleId="pt.jsp.setLstSetup.dataSort" alt="데이터 기본 정렬" /> >
			<option value=""> -- </option><c:forEach
			items="${sorts}" var="sort" varStatus="sortStatus">
			<u:option value="${sort[0]}" title="${sort[1]}" checkValue="${ptLstSetupDVo.dataSortVa}"/></c:forEach></select></c:if>
		</td>
	</tr>
	</c:forEach>

</u:listArea>
</form>