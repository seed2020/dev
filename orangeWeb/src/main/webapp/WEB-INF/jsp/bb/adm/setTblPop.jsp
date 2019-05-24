<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<% // 테이블명 변경 %>
function setTblNm(prefix) {
	$('#tblNmTxt').text(prefix + $('#tblNm').val().substring(1));
	$('#tblNm').val(prefix + $('#tblNm').val().substring(1));
}
<% // [기본테이블/확장테이블] 모드 변경 %>
function changeMode(ex) {
	$('#exYn').val(ex);

	if (ex == 'Y') {
		$('#exArea').show();
		$('#btnExTbl').hide();
		$('#btnDefTbl').show();
		$('#btnPlus').show();
		$('select[name="colmTyp"]').each(function() {
			changeColmTyp(this);
		});
		setTblNm('E');
		if (gMaxRow == 1) addRow();
	} else {
		$('#exArea').hide();
		$('#btnExTbl').show();
		$('#btnDefTbl').hide();
		$('#btnPlus').hide();
		while (trArray.length > 0) {
			trArray.pop().remove();
		}
		setTblNm('B');
		gMaxRow = 1;
	}
	dialog.resize('setTblPop');
}
<% // 다음 Row 번호 %>
var gMaxRow = parseInt('${fn:length(baTblBVo.colmVoList)}');
var gValidRow = parseInt('${fn:length(baTblBVo.colmVoList)}');
<% // Max Row 셑 %>
function initMaxRow(){
	<c:if test="${fn:length(baTblBVo.colmVoList)>0}">
	var lastId=null;
	$.each($('#exColArea input[name="colmNm"]'), function(){
		if($(this).val()=='' || $(this).val()=='C00') return true;
		lastId=$(this).val().replace(/[a-zA-Z]/,'');
	});
	if(lastId!=null){
		gMaxRow=parseInt(lastId)+1;
	}
	</c:if>
}
<% // 생성된 TR 배열 %>
var trArray = [];
<% // [목록버튼:추가] TR 추가 %>
function addRow() {
	if (gValidRow > 20) {
		alertMsg('bb.msg.exColm.limit.exceeded');
		<% // bb.msg.exColm.limit.exceeded=확장컬럼은 20개 까지만 생성할 수 있습니다. %>
		return;
	}
	restoreUniform('exArea');
	var $tr = $('#exArea #hiddenTr');
	var html = $tr[0].outerHTML;
	html = html.replace(/_NO/g,'_'+gMaxRow);
	var colmIdx = gMaxRow;
	colmIdx = colmIdx < 10 ? '0'+colmIdx : colmIdx;
	gMaxRow++;
	gValidRow++;
	$tr.before(html);
	$tr = $tr.prev();
	$tr.attr('id','');
	$tr.attr('style','');
	$tr.find('input[name="colmNm"]').val('C' + colmIdx);
	$tr.find('input[name="valid"]').val('Y');	
	applyUniform('exArea');
	trArray.push($tr);
	dialog.resize('setTblPop');
}
<% // [목록버튼:삭제] TR 삭제 %>
function delRow(btn) {
	if(confirmMsg("cm.cfrm.del")) {<% // cm.cfrm.del=삭제하시겠습니까 ? %>
		$tr = $(btn).parents('tr');
		var $colmId = $tr.find('input[name="colmId"]');
		var $valid = $tr.find('input[name="valid"]');
		var $deleted = $tr.find('input[name="deleted"]');
		// removeHandler
		var id = $colmId.attr('id');
		var idx = id.substring(id.lastIndexOf('_'));
		validator.removeHandler('colmNm' + idx);
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
		validator.removeHandler('rescVa_${langTypCdVo.cd}' + idx);
		</c:forEach>
		// valid
		if ($colmId.val() == '') $valid.val('');
		// deleted
		$deleted.val('Y')
		$tr.hide();
		dialog.resize('setTblPop');
		gValidRow--;
	}
}
<% // [팝업:저장] 테이블 저장 %>
function saveTbl() {
	// colmTypVal
	$('select[name="colmTyp"]').each(function() {
		$colmTypVal = $(this).parents('td:first').find('input[name="colmTypVal"]');
		if ($(this).val() == 'TEXTAREA') {
			$colmTypVal.val($(this).parents('td:first').siblings('.colmTyp_line').find('select').val());
		} else if ($(this).val().startsWith('CODE')) {
			$colmTypVal.val($(this).parents('td:first').siblings('.colmTyp_code').find('select').val());
		} else if ($(this).val().startsWith('CALENDAR')) {
			$colmTypVal.val($(this).parents('td:first').siblings('.colmTyp_calendar').find('select').val());
		} else {
			$colmTypVal.val('');
		}
	});

	validator.removeHandler('colmNm_NO');
	<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
	validator.removeHandler('rescVa_${langTypCdVo.cd}_NO');
	</c:forEach>

	if (validator.validate('setTblForm')) {
		var $form = $('#setTblForm');
		$form.attr('method','post');
		$form.attr('action','./transTbl.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
}
<% // 일괄 언어 선택 변경 %>
function changeLangTypCds(areaId){
	var $area = $("#"+areaId);
	<% // 변경할 언어 구하기 - 첫번째 언어 선택 select 의 다음 값 %>
	var langSel = $area.find("select[id^='langSelector']:first");
	if(langSel.length==0) return;
	var index = langSel[0].selectedIndex + 1;
	if(index>=langSel[0].options.length) index = 0;
	var langCd = langSel[0].options[index].value;
	var rescNm;
	<% // input - 어권에 맞게 show/hide %>
	$area.find("td[id^='langTyp'] input").each(function(){
		rescNm = $(this).attr('name');
		if(rescNm!=null && rescNm.endsWith(langCd)){
			$(this).show();
		} else { $(this).hide(); }
	});
	<% // select 변경 %>
	var selectors = $area.find("select[id^='langSelector']");
	selectors.val(langCd);
	selectors.uniform.update();
}
//- 해당 어권의 명이 입력되지 않았 을 경우 해당 어권을 보이게 함 %>
function changeLangSelectorColm(areaId, id, va){
	if(va==''){
		var langSelector = $('#'+areaId+' #langSelector'+id.substring(id.lastIndexOf('_')));
		var nm = $("#exArea #"+id).attr("name");
		langSelector.val(nm.substring(nm.length-2));
		langSelector.trigger('click');
	}
}
<% // 컬럼타입 변경 %>
function changeColmTyp(sel) {
	var val = $(sel).val();
	var $line = $(sel).parents('td:first').siblings('.colmTyp_line');
	var $code = $(sel).parents('td:first').siblings('.colmTyp_code');
	var $calendar = $(sel).parents('td:first').siblings('.colmTyp_calendar');
	$line.hide();
	$code.hide();
	$calendar.hide();
	if (val == 'TEXTAREA') {
		$line.show();
	}else if (val.startsWith('CODE')) {
		$code.show();
	}else if (val.startsWith('CALENDAR')) {
		$calendar.show();
	}
}

<% // 게시물이 있는 경우 %>
function setBullCnt() {
	$('#bullCntMsg').show();
	var exArea = $("#exArea");
	//exArea.find('select[name="colmTyp"]').each(function(idx) {
	//	if ((idx + 1) < gMaxRow) {
	//		setDisabled($(this), true);
	//	}
	//});
	//exArea.find('input[name="colmLen"]').each(function(idx) {
	//	if ((idx + 1) < gMaxRow) {
	//		setDisabled($(this), true);
	//	}
	//});
	exArea.find('.colmTyp_code select').each(function(idx) {
		if ((idx + 1) < gMaxRow) {
			setDisabled($(this), true);
		}
	});
}

$(document).ready(function() {
	<c:if test="${baTblBVo.exYn == 'Y'}">
	changeMode('Y');
	initMaxRow(); // maxRow 세팅
	</c:if>
	<c:if test="${baTblBVo.bullCnt != null && baTblBVo.bullCnt != 0}">
	setBullCnt();
	</c:if>
});
//-->
</script>

<div style="width:700px">
<form id="setTblForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="tblId" value="${baTblBVo.tblId}" />
<u:input type="hidden" id="bullCnt" value="${baTblBVo.bullCnt}" />
<u:input type="hidden" id="exYn" value="N" />
<u:input type="hidden" id="regDispYn" value="Y" />
<u:input type="hidden" id="modDispYn" value="Y" />
<u:input type="hidden" id="readDispYn" value="Y" />
<u:input type="hidden" id="listDispYn" value="Y" />

<% // 폼 필드 %>
<u:listArea>
	<%-- <u:secu auth="SYS">
	<tr>
	<td class="head_lt"><u:msg titleId="cols.comp" alt="회사" /></td>
	<td>
		<select id="compId" name="compId" <u:elemTitle titleId="cols.comp" /> <c:if test="${!empty baTblBVo.tblId }">disabled="disabled"</c:if>>
			<c:forEach items="${ptCompBVoList}" var="ptCompBVo" varStatus="status">
				<u:option value="${ptCompBVo.compId}" title="${ptCompBVo.rescNm}" checkValue="${baTblBVo.compId}"/>
			</c:forEach>
		</select>
	</td>
	</tr>
	</u:secu> --%>
	<tr>
	<td class="head_lt"><u:msg titleId="cols.dbTblNm" alt="DB 테이블명" /></td>
	<td><table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="body_lt">BB_<span id="tblNmTxt">${baTblBVo.tblNm}</span>_L<u:input type="hidden" id="tblNm" name="tblNm" value="${baTblBVo.tblNm}"/></td>
		<td><table align="right" border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			<c:if test="${baTblBVo.tblId == null || baTblBVo.tblId == ''}">
			<td id="btnExTbl" style="display: block;"><u:buttonS titleId="bb.btn.exTbl" alt="확장테이블" onclick="changeMode('Y');" /></td>
			<td id="btnDefTbl" style="display: none;"><u:buttonS titleId="bb.btn.defTbl" alt="기본테이블" onclick="changeMode('N');" /></td>
			</c:if>
			</tr>
			</tbody></table></td>
		</tr></table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.tblNm" alt="테이블명" /></td>
	<td><table cellspacing="0" cellpadding="0" border="0">
		<tr>
		<td id="langTypArea">
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:convert srcId="${baTblBVo.rescId}_${langTypCdVo.cd}" var="tblRescVa" />
			<u:set test="${status.first}" var="style" value="width:200px;" elseValue="width:200px; display:none" />
			<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
			<u:input id="tblRescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.tblNm" value="${tblRescVa}" style="${style}"
				maxByte="120" validator="changeLangSelector('setTblForm', id, va)" mandatory="Y" />
		</c:forEach>
		</td>
		<td>
		<c:if test="${fn:length(_langTypCdListByCompId)>1}">
			<select id="langSelector" onchange="changeLangTypCd('setTblForm','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
			</c:forEach>
			</select>
		</c:if>
		<u:input type="hidden" id="tblRescId" value="${baTblBVo.rescId}" />
		</td>
		</tr>
		</table></td>
	</tr>

</u:listArea>

<% // 확장테이블 %>
<u:set test="${param.fncEx == 'Y'}" var="style" value="display: block;" elseValue="display: none;" />
<div id="exArea" style="${style}">
	<div id="bullCntMsg" class="front" style="display: none;">
	<div class="front_left">
		<table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td class="color_stxt"><u:msg titleId="bb.jsp.setTblPop.tx01" alt="※ 테이블을 사용중인 게시판 데이터가 존재하므로, 컬럼 추가만 가능합니다." /></td>
		</tr>
		</tbody></table>
	</div>
	</div>

	<u:set test="${param.fncEx == 'Y'}" var="style" value="display: block;" elseValue="display: none;" />
	<u:listArea id="exColArea" colgroup="142,171,,97,74">
		<tr>
		<th class="head_ct"><u:msg titleId="cols.tblColmNm" alt="테이블컬럼명" /></th>
		<c:if test="${fn:length(_langTypCdListByCompId)>1}">
		<u:msg titleId="pt.jsp.terms.chgLangAll" alt="일괄 언어 변경" var="title" />
		<th class="head_ct"><u:mandatory /><a href="javascript:changeLangTypCds('setTblForm');" title="${title}"><u:msg titleId="cols.itemNm" alt="항목명" /></a></th>
		</c:if>
		<c:if test="${fn:length(_langTypCdListByCompId)<=1}">
		<th class="head_ct"><u:mandatory /><u:msg titleId="cols.itemNm" alt="항목명" /></th>
		</c:if>
		<th class="head_ct"><u:msg titleId="cols.va" alt="값" /></th>
		<th class="head_ct"><u:msg titleId="cols.size" alt="크기" /></th>
		<th class="head_ct"><u:msg titleId="cols.fnc" alt="기능" /></th>
		</tr>

		<c:forEach items="${baTblBVo.colmVoList}" var="colmVo" varStatus="status">
			<u:set test="${status.last}" var="trDisp" value="display: none;" elseValue="" />
			<u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${colmVo.colmId}" />
			<u:set test="${status.last}" var="colmIndex" value="_NO" elseValue="_${status.index+1}" />
			<u:set test="${status.last}" var="valid" value="" elseValue="Y" />
			<u:set test="${!status.last && baTblBVo.bullCnt != 0}" var="readonly" value="readonly" />
			<u:set test="${colmVo.colmId != null}" var="colmNm" value="${colmVo.colmNm}" elseValue="C00" />
		<tr id="${trId}" style="${trDisp}" onmouseover="this.className='trover'" onmouseout="this.className='trout'">
		<td class="bodybg_lt"><u:input id="colmNm${colmIndex}" name="colmNm" value="${colmNm}" titleId="cols.tblColmNm" readonly="readonly" maxByte="120" mandatory="Y" /></td>
		<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			<td id="langTyp${colmIndex}">
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
				<u:convert srcId="${colmVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
				<u:set test="${status.first}" var="style" value="width:100px;" elseValue="width:100px; display:none" />
				<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
				<u:input id="rescVa_${langTypCdVo.cd}${colmIndex}" name="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.colmDispNm" value="${rescVa}" style="${style}"
					maxByte="120" validator="changeLangSelectorColm('exArea', id, va)" mandatory="Y" />
			</c:forEach>
			</td>
			<td>
			<c:if test="${fn:length(_langTypCdListByCompId)>1}">
				<select id="langSelector${colmIndex}" onchange="changeLangTypCd('exArea','langTyp${colmIndex}',this.value)" <u:elemTitle titleId="cols.langTyp" />>
				<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
				<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
				</c:forEach>
				</select>
			</c:if>
			<u:input type="hidden" id="rescId${colmIndex}" name="rescId" value="${colmVo.rescId}" />
			</td>
			</tr>
			</tbody></table></td>
		<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			<td><u:set var="colmDisabled" test="${baTblBVo.bullCnt != null && baTblBVo.bullCnt != 0 && !empty colmVo.colmId }" value=" disabled=\"disabled\""
			/><select id="colmTyp${colmIndex}" name="colmTyp" onchange="changeColmTyp(this);"${colmDisabled }>				
				<u:option value="TEXT" titleId="bb.option.text" alt="텍스트" checkValue="${colmVo.colmTyp}" />
				<u:option value="TEXTAREA" titleId="bb.option.textarea" alt="텍스트영역" checkValue="${colmVo.colmTyp}" />
				<u:option value="PHONE" titleId="bb.option.phone" alt="전화번호" checkValue="${colmVo.colmTyp}" />
				<u:option value="CALENDAR" titleId="bb.option.date" alt="날짜" checkValue="${colmVo.colmTyp}" />
				<u:option value="CALENDARTIME" titleId="bb.option.dateTime" alt="날짜와시간" checkValue="${colmVo.colmTyp}" />
				<u:option value="CODE" titleId="bb.option.box.drop" alt="드롭박스" checkValue="${colmVo.colmTyp}" />
				<u:option value="CODECHK" titleId="bb.option.box.chk" alt="체크박스" checkValue="${colmVo.colmTyp}" />
				<u:option value="CODERADIO" titleId="bb.option.box.radio" alt="단일선택" checkValue="${colmVo.colmTyp}" />
				<u:option value="USER" titleId="bb.option.select.user" alt="사용자선택" checkValue="${colmVo.colmTyp}" />
				<u:option value="DEPT" titleId="bb.option.select.dept" alt="부서선택" checkValue="${colmVo.colmTyp}" />				
				</select>
				<u:input type="hidden" id="colmTypVal${colmIndex}" name="colmTypVal" value="${colmVo.colmTypVal}" />
				<c:if test="${baTblBVo.bullCnt != null && baTblBVo.bullCnt != 0 && !empty colmVo.colmId}"><u:input type="hidden" id="colmTyp${colmIndex}" name="colmTyp" value="${colmVo.colmTyp}" /></c:if>
				</td>
			<td class="colmTyp_line" style="display: none;"><select id="line${colmIndex}" name="line">
				<u:msg titleId="bb.cols.line" alt="라인" var="line" />
				<u:option value="2" title="2 ${line}" checkValue="${colmVo.colmTypVal}" />
				<u:option value="3" title="3 ${line}" checkValue="${colmVo.colmTypVal}" />
				<u:option value="4" title="4 ${line}" checkValue="${colmVo.colmTypVal}" />
				<u:option value="5" title="5 ${line}" checkValue="${colmVo.colmTypVal}" />
				</select></td>
			<td class="colmTyp_code" style="display: none;"><select id="code${colmIndex}" name="code">
				<c:if test="${fn:length(cdList) > 0}">
					<c:forEach items="${cdList}" var="cd" varStatus="status">
						<u:option value="${cd.cdGrpId}" title="${cd.rescNm}" checkValue="${colmVo.colmTypVal }"/>
					</c:forEach>
				</c:if>
				</select></td>
			<td class="colmTyp_calendar" <c:if test="${!fn:startsWith(colmVo.colmTyp,'CALENDAR')}">style="display: none;"</c:if>><select id="code${colmIndex}" name="calendar">
					<u:option value="" titleId="cm.option.default" alt="기본값" checkValue="${colmVo.colmTypVal}" />
					<u:option value="today" titleId="cm.option.currenttime" alt="현재시간" checkValue="${colmVo.colmTypVal}" />
				</select></td>
			</tr>
			</tbody></table>
			<u:input type="hidden" id="colmId${colmIndex}" name="colmId" value="${colmVo.colmId}" />
			<u:input type="hidden" id="dataTyp${colmIndex}" name="dataTyp" value="VARCHAR" />
			<u:input type="hidden" id="exColmYn${colmIndex}" name="exColmYn" value="Y" />
			<u:input type="hidden" id="deleted${colmIndex}" name="deleted" value="" />
			<u:input type="hidden" id="valid${colmIndex}" name="valid" value="${valid}" />
			</td>
		<u:set test="${colmVo.colmLen != null}" var="colmLen" value="${colmVo.colmLen}" elseValue="2000" />
		<td class="bodybg_lt"><c:if test="${!empty colmVo.colmId}"><div style="text-align:center;">${colmLen}</div><u:input type="hidden" id="colmLen${colmIndex}" name="colmLen" value="${colmLen}" /></c:if><c:if test="${empty colmVo.colmId}"><u:input id="colmLen${colmIndex}" name="colmLen" value="${colmLen}" titleId="cols.size" maxInt="2000" readonly="${readonlyColmNm}" style="width: 80px;" /></c:if></td>
		<td><table align="center" border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			<td id="delTd">
				<c:if test="${readonly == null || readonly != 'readonly'}">
				<u:buttonS titleId="cm.btn.del" alt="삭제" onclick="delRow(this);" auth="A" />
				</c:if>
				</td>
			</tr></tbody></table></td>
		</tr>
		</c:forEach>
	</u:listArea>

</div>

<u:buttonArea>
	<u:button id="btnPlus" titleId="cm.btn.plus" alt="행추가" onclick="addRow();" auth="A" style="display: none" />
	<u:button titleId="cm.btn.save" alt="저장" onclick="saveTbl();" auth="A" />
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>

</form>
</div>