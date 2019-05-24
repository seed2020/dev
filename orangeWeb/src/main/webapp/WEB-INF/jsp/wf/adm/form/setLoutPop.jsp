<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
//request.setAttribute("widths", widths);
%>
<script type="text/javascript">
<!--
var gSkipUinform = true;
var $gSetItemsPopForm = null;
<%// 셀렉트 옵션 배열 변환후 복사 %>
function selectToArr(origin, copy, maxIdx){
	var colCntOpts = $(copy).find("option").not("[value='']");
	var optLen=colCntOpts.length;
	if(optLen>maxIdx){
		$.each(colCntOpts, function(i,o){
			if((i+1)>maxIdx)
				$(this).remove();
		});
	}else if(optLen<maxIdx){
		var arr=[];
		$.each($(origin).find("option"), function(i,o){
			if(i>=maxIdx) return false;
			var x = {};
			x["value"] = o.value;
			x["text"] = o.text;
			arr.push(x);
		});
		$.each(arr, function(i,o){
			if((i+1)<=optLen) return true;
			$(copy).append("<option value='" + o.value + "'>" + o.text + "</option>");
		});
	}
	
}
<%// 레이아웃 변경 %>
function chkLoutTyp(isInit){
	if(isInit){
		$('#docItemsModArea tr').remove();
		$gSetItemsPopForm.find("#rowCnt, #colCnt").val('');
		//$('#loutWdthsArea').html('');
		//$gSetItemsPopForm.find("#colCnt option").not("[value='']").remove();
	}
	var loutTyp = $gSetItemsPopForm.find('input[name="loutTyp"]:checked').val();
	var maxIdx=20; // 목록형은 최대 칸수 20칸
	if(loutTyp=='D') maxIdx=4; // 기본일 경우 최대 칸수는 4칸
	selectToArr($("#docItemsPopHiddenArea select#colspan"), $gSetItemsPopForm.find("#colCnt"), maxIdx);
	
	$('#docItemsPopArea').uniform.update();
}<%// 줄 칸에 맞게 양식 그리기 - 항목지정의 줄(콤보), 칸(콤보) 변경시 호출 %>
function drawItems(){
	var row = $gSetItemsPopForm.find("#rowCnt").val();
	var col = $gSetItemsPopForm.find("#colCnt").val();
	if(row!='' && col!=''){
		
		row = parseInt(row, 10);<%// 항목지정 - 줄 으로 지정된 값 %>
		col = parseInt(col, 10);<%// 항목지정 - 칸 으로 지정된 값 %>
		
		var selectHtml = $("#docItemsPopHiddenArea").html();<%// 삽입할 html : select(칸) %>
		
		if(col!=20){<%// 항목지정의 칸수가 4이 아니면 추가되는 컬럼의 칸수에서 관련 없는것 제거 %>
			var p = selectHtml.lastIndexOf('<option value="'+(col+1)+'"');
			var q = selectHtml.indexOf('</select>',p+1);
			if(p>0 && q>0){
				selectHtml = selectHtml.substring(0,p)+selectHtml.substring(q);
			}
		}
		
		var r=0, c=0, tr, colspan, $modArea = $gSetItemsPopForm.find("#docItemsModArea"), $tbody = $modArea.find("tbody");
		<%// colgroup 조절%>
		var colWdth = parseInt(100/col);
		var colRest=100-(colWdth*col);
		var colHtml='';
		for(var i=0;i<col;i++){
			if(i+1==col && colRest>0){
				colHtml+="<col width='"+(colWdth+colRest)+"%'/>";
				break;
			}
			colHtml+="<col width='"+colWdth+"%'/>";
		}
		$modArea.find("colgroup").html(colHtml);
		
		$tbody.find("tr").each(function(){
			r++;
			c = 0;
			<%// 줄수 이상의 TR 삭제 %>
			if(r>row) $(this).remove();
			else {
				tr = this;
				$(this).find("td").each(function(){
					c++;
					<%// 칸수 이상의 TD 삭제 %>
					if(c>col) $(this).remove();
					else {
						colspan = $(this).attr('colspan');
						colspan = colspan==null || colspan=='' ? 1 : parseInt(colspan, 10);
						<%// colspan + TD 수가 칸수를 초과하면 - colspan 제거 %>
						if(c + colspan -1 > col){
							colspan = col - c + 1;
							$(this).attr("colspan", colspan);
							$(this).find("[name='colspan']").val(colspan).trigger('click');
						}
						c += colspan -1;
					}
				});
				<%// 부족한 TD 넣기 %>
				for(;c<col;c++){
					$(tr).append("<td>"+selectHtml+"</td>");
					if(!gSkipUinform) $(tr).find("td:last select").uniform();
				}
			}
		});
		<%// 부족한 TR 넣기 %>
		var $tr;
		for(;r<row;r++){
			$tbody.append("<tr></tr>");
			$tr = $tbody.find("tr:last");
			for(c=0;c<col;c++){
				$tr.append("<td>"+selectHtml+"</td>");
				if(!gSkipUinform) $tr.find("td:last select").uniform();
			}
		}
		<%// 개별항목의 칸수 조절 %>
		var $opts = $modArea.find("[name='colspan']:first option");
		if($opts.length > col){<%// [개별항목 첫번째 칸수의 option 수] > [상단 항목지정 칸수의 옵션수] %>
			var optIndex;
			$modArea.find("[name='colspan']").each(function(){
				optIndex = 0;
				$(this).find("option").each(function(){
					if(optIndex>=col) { $(this).remove();<%// 옵션 삭제%>}
					else optIndex++;
				});
			});
		} else if($opts.length < col){<%// [개별항목 첫번째 칸수의 option 수] < [상단 항목지정 칸수의 옵션수] %>
			var optHtml = [], $copyOpts = $("#docItemsPopHiddenArea").find("[name='colspan'] option");
			for(var i=$opts.length;i<col;i++) optHtml.push($copyOpts[i].outerHTML);
			optHtml = optHtml.join('');
			$modArea.find("[name='colspan']").each(function(){
				if($(this).find("option").length<col){
					$(this).append(optHtml);
				}
			});
		}
		if($modArea.css('display')=='none') $modArea.show();
		dialog.resize("setLoutDialog");
	}
}
<%// 개별항목의 칸수 변경시 호출 - 칸수의 변경으로 colspan을 넣거나 제거하고, td를 넣거나 제거함 %>
function checkColspan(obj){
	var col = $gSetItemsPopForm.find("#colCnt").val();
	var td = getParentTag(obj,'td');
	$(td).attr('colspan', $(obj).val());
	if(col!=''){
		if($(obj).val()==col){<%// 총 칸수화 현재의 칸수가 같으면 나머지 TD 모두 제거함 %>
			if($(td).prevAll().length!=0) $(td).prevAll().remove();
			if($(td).nextAll().length!=0) $(td).nextAll().remove();
		} else {
			col = parseInt(col, 10);<%// 항목지정 - 칸 으로 지정된 값 %>
			var colSum = 0, colspan;<%// 전체 칸수(sum) 계산 %>
			$(td).parent().children().each(function(){
				colspan = $(this).attr('colspan');
				colSum += colspan==null || colspan=='' ? 1 : parseInt(colspan, 10);
			});
			if(colSum<col){<%// 전체 칸수(sum)가 작을 경우 - TD 추가 %>
				var $p = $(td).parent();
				var selectHtml = $("#docItemsPopHiddenArea").html();<%// 삽입할 html : select(항목) + select(칸) %>
				if(col!=20){<%// 항목지정의 칸수가 4이 아니면 추가되는 컬럼의 칸수에서 관련 없는것 제거 %>
					var p = selectHtml.lastIndexOf('<option value="'+(col+1)+'"');
					var q = selectHtml.indexOf('</select>',p+1);
					if(p>0 && q>0){
						selectHtml = selectHtml.substring(0,p)+selectHtml.substring(q);
					}
				}
				for(;colSum<col;colSum++){
					$p.append("<td>"+selectHtml+"</td>");
					if(!gSkipUinform) $p.find("td:last select").uniform();
				}
				$(td).attr('data-colspan', $(obj).val());
			} else if(colSum>col){<%// 전체 칸수(sum)가 클 경우 - TD 제거 또는 colspan 제거 %>
				var cnt=$(obj).val();
				$colspan = $(td).attr('data-colspan');
				if($colspan==null || $colspan=='') {
					$colspan=0;
					cnt--;
				}
				cnt=cnt-$colspan;
				var arrs = findNode(td, null, cnt, null);
				if(arrs!=null && arrs.length>0){
					$.each(arrs, function(){
						$(this).remove();
					});
					$(td).attr('data-colspan', $(obj).val());
				}else{
					$(td).find("[name='colspan']").val("1").trigger('click');
					$(td).removeAttr("colspan");
				}
			}
		}
	}
}
<%// 노드 찾기 %>
function findNode(td, obj, cnt, arrs, direction){
	if(cnt==0) {
		return arrs;
	}
	if(arrs==null) arrs=[];
	if(obj==null) obj=td;
	if(direction===undefined) direction='next';
	if(direction=='next'){
		if($(obj).next().length>0){
			$colspan = $(obj).next().attr('colspan');
			if($colspan==null || $colspan=='') $colspan=1;
			if($colspan>cnt) return findNode(td, null, cnt, arrs, 'prev');
			arrs.push($(obj).next());
			cnt=cnt-$colspan;
			return findNode(td, $(obj).next(), cnt, arrs, direction);
		}else return findNode(td, null, cnt, arrs, 'prev');
	}else if(direction=='prev'){
		if($(obj).prev().length>0){
			$colspan = $(obj).prev().attr('colspan');
			if($colspan==null || $colspan=='') $colspan=1;
			if($colspan>cnt) return null;
			arrs.push($(obj).prev());
			cnt=cnt-$colspan;
			return findNode(td, $(obj).prev(), cnt, arrs, direction);
		}else return null;
	}
}
<%// 노드 찾기 %>
function findNode2(obj, cnt, arrs, colCnt, direction){
	if(cnt==0) {
		return arrs;
	}
	if(direction=='next'){
		if($(obj).next().length>0){
			$colspan = $(obj).next().attr('colspan');
			if($colspan==null || $colspan=='') $colspan=1;
			if($colspan>(cnt)) return arrs;
			arrs.push($(obj).next());
			cnt=cnt-$colspan;
			findNode($(obj).next(), cnt, arrs, colCnt, direction);
		}else return arrs;
	}else if(direction=='prev'){
		if($(obj).prev().length>0){
			$colspan = $(obj).prev().attr('colspan');
			if($colspan==null || $colspan=='') $colspan=1;
			if($colspan>(cnt)) return arrs;
			arrs.push($(obj).prev());
			cnt=cnt-$colspan;
			findNode($(obj).prev(), cnt, arrs, colCnt, direction);
		}else return arrs;
	}
}
<%// 확인 버튼 클릭 %>
function setItems(){
	var param = new ParamMap().getData("listArea");
	if($gSetItemsPopForm.find("#rowCnt").val()==''){
		alertMsg("ap.cmpt.items.setRow");<%//ap.cmpt.items.setRow=항목지정의 줄을 선택해 주십시요.%>
		return;
	}
	if($gSetItemsPopForm.find("#colCnt").val()==''){
		alertMsg("ap.cmpt.items.setCol");<%//ap.cmpt.items.setCol=항목지정의 칸을 선택해 주십시요.%>
		return;
	}
	
	if($gSetItemsPopForm.find("#docItemsModArea tbody:first").children.length==0){
		alertMsg("wf.msg.empty.layout");<%//wf.msg.empty.layout=레이아웃을 설정해주세요.%>
		return;
	}
	
	var row = 1, col, $opt, colspan, rowspan;
	$gSetItemsPopForm.find("#docItemsModArea tr").each(function(){
		col = 1;
		$(this).find("td").each(function(){
			$opt = $(this).find("#item option:selected");
			rowspan = $(this).attr('rowspan');
			if(rowspan===undefined) rowspan='1';
			colspan = $(this).find("#colspan option:selected").val();
			param.put(row+"-"+col,rowspan+"-"+colspan);
			param.put(row+"-"+col+"-nm","-"+colspan);
			col++;
		});
		row++;
	});
	
	setItemsData('table', param, "${param.seq}");
}
<% // [select] - 셀 최대 가로 조절 추가 %>
function addMaxWdths(){
	$('#loutMaxWdthsArea').html('');
	
	var wdthsHtml = $("#loutMaxWdthsHiddenArea").html();<%// 삽입할 html : select(width) %>
	var wdthBuffer=[];
	wdthBuffer.push(wdthsHtml);
	wdthBuffer.push('</ul>');
	$('#loutMaxWdthsArea').html(wdthBuffer.join(''));
	$('#loutMaxWdthsArea').find("select").uniform();
}
<% // [select] - 셀 가로 조절 추가 %>
function addWdths(col){
	$('#loutWdthsArea').html('');
	
	var wdthsHtml = $("#loutWdthsHiddenArea").html();<%// 삽입할 html : select(width) %>
	var wdthBuffer=[];
	wdthBuffer.push('<ul class="attr_list">');
	for(var i=0;i<col;i++){
		wdthBuffer.push('<li data-index="'+i+'">');
		wdthBuffer.push(wdthsHtml);
		wdthBuffer.push('</li>');
	}
	wdthBuffer.push('</ul>');
	$('#loutWdthsArea').html(wdthBuffer.join(''));
	$('#loutWdthsArea').find("select").uniform();
}
<% // 셀 가로 길이 적용 %>
function setColWdth(obj){
	// 가로 100이 넘는지 체크
	var ul=$(obj).closest('ul');
	var selectList=$(ul).find('select > option:selected');
	var isChk=true, allWdth=0;
	$.each(selectList, function(){
		if($(this).val()=='') return true;
		allWdth+=parseInt($(this).val());
		if(allWdth>100){
			isChk=false;
			return false;
		}
	});
	
	if(!isChk){
		alertMsg('wf.msg.limit.width.colgroup'); // wf.msg.limit.width.colgroup=가로 길이가 100을 넘을 수 없습니다.
		$(obj).val('').trigger('click');
		return;
	}
	
	var li=$(obj).closest('li');
	var dataIdx = li.attr('data-index');
	if(dataIdx!=null){
		var wdth=$(obj).val();
		$colgroup = $gSetItemsPopForm.find("#docItemsModArea").find('colgroup');
		$colgroup.find('col').eq(dataIdx).attr('width', wdth);
	}
}
<% // 셀 최대 가로 길이 적용 %>
function setMaxWdths(maxWdth){
	addMaxWdths();
	
	// 가로 길이 조절 추가
	if(maxWdth!='')
		$('#loutMaxWdthsArea select').val(maxWdth);	
	
	$('#docItemsPopArea').uniform.update();
	
	// 미리보기 적용
	//$.each($('#loutWdthsArea select'), function(){
	//	setColWdth(this);	
	//});
}
<% // 셀 가로 길이 적용 %>
function setWdths(wdths){
	var jsonObj=JSON.parse(wdths);
	// 콤보 생성
	if(jsonObj!=null)
		addWdths(jsonObj.length);
	
	// select 적용
	$.each(jsonObj, function(index, json){
		$wdth=json.value;
		if($wdth==null) $wdth='';
		// 가로 길이 조절 추가
		if($('#loutWdthsArea select').eq(index)){
			$('#loutWdthsArea select').eq(index).val($wdth);	
		}
	});
	
	$('#docItemsPopArea').uniform.update();
	
	// 미리보기 적용
	//$.each($('#loutWdthsArea select'), function(){
	//	setColWdth(this);	
	//});
}
function setApply(pageTyp){
	if(pageTyp=='lout')
		setItems();
	else if(pageTyp=='table'){
		if($('#loutWdthsArea').html()!=''){
			var param = new ParamMap();
			var wdths=[];
			$gSetItemsPopForm.find("#loutWdthsArea select").each(function(){
				$val=$(this).val();
				if($val=='') $val=null;
				wdths.push({name:'width', value:$val});
			});
			param.put('wdths-list', JSON.stringify(wdths));
			setTables(param, "${param.seq}");
		}
		if($('#loutMaxWdthsArea').html()!=''){
			var param = new ParamMap();
			param.put('max-width', $('#loutMaxWdthsArea select').val());
			setTableLouts(param, "${param.seq}");
		}
		
	}else if(pageTyp=='title'){
		if($('#listArea input[name="rescUseYn"]:checked').val()=='Y' && !validator.validate('listArea')){
			return;
		}
		
		var param = new ParamMap().getData("listArea");
		setTitles(param, "${param.seq}");
	}
	dialog.close("setLoutDialog");
		
}

$(document).ready(function() {
	$gSetItemsPopForm = $("#setItemsPopForm");
	if('${param.seq}' != ''){
		var param = new ParamMap().getData($("#formArea #itemsArea[data-seq='${param.seq}']")[0], "items-${param.seq}-");
		<c:if test="${param.pageTyp eq 'lout' }">
		param.setData($gSetItemsPopForm.find("#docItemsPopArea")[0]);
		param.setData($('#listArea'));
		drawItems();
		$gSetItemsPopForm.find("#docItemsModArea select").removeClass("skipThese");
		var keys, vals, $trs=$gSetItemsPopForm.find("#docItemsModArea tr"), $td;
		param.each(function(key, va){
			if(key!='rowCnt' && key!='colCnt' && /\d-\d/.test(key)){
				keys = key.split('-');
				vals = va.split('-');
				$td = $trs.eq(parseInt(keys[0],10)-1).children().eq(parseInt(keys[1],10)-1);
				$td.find("#item").val(vals[0]);
				if(vals[1]!='1'){
					$td.find("#colspan").val(vals[1]);
					checkColspan($td.find("#colspan")[0]);
				}
			}
			// 가로 길이 조절
			//if(key=='wdths-list') setWdths(va);
		});
		chkLoutTyp(false);
		</c:if>
		<c:if test="${param.pageTyp eq 'table' }">
		var $itemArea=$("#formArea #itemsArea[data-seq='${param.seq}']");
		var colgroup=$itemArea.find('table:first colgroup')
		var wdths=[];
		colgroup.children().each(function(){
			$val=$(this).attr('width');
			if($val=='') $val=null;
			wdths.push({name:'width', value:$val});
		});
		param.put('wdths-list', JSON.stringify(wdths));
		
		//var maxWdth= $itemArea.css('max-width')!='none' ? $itemArea.css('max-width') : '';
		setMaxWdths($itemArea.css('max-width'));
		if(param.get('wdths-list')!=null) setWdths(param.get('wdths-list'));
		
		</c:if>
		<c:if test="${param.pageTyp eq 'title' }">param.setData($('#listArea'));</c:if>
	}
	
	gSkipUinform = false;
});
//-->
</script><c:set var="maxRows" value="20"/><c:set var="maxCols" value="20"/>
<div style="width:${_lang=='ko' or _lang=='ja' or _lang=='zh' ? '800' : '950'}px">
<form id="setItemsPopForm">
<c:if test="${param.pageTyp eq 'lout' }">
<u:listArea id="listArea">
<tr>
	<td width="20%" class="head_lt"><u:msg titleId="wf.cols.typ" alt="구분" /></td>
	<td class="body_lt" ><u:checkArea>
				<u:radio name="loutTyp" value="D" titleId="wf.option.dft" alt="기본"  inputClass="bodybg_lt" checked="true" onclick="chkLoutTyp(true);" />
				<%-- <u:radio name="loutTyp" value="F" titleId="wf.option.list.fixed" alt="목록(고정)"  inputClass="bodybg_lt" onclick="chkLoutTyp(true);" /> --%>
				<u:radio name="loutTyp" value="V" titleId="wf.option.list.variable" alt="목록(가변)"  inputClass="bodybg_lt" onclick="chkLoutTyp(true);" />
			</u:checkArea></td>
	</tr>
	<tr>
	<td width="20%" class="head_lt"><u:msg titleId="wf.cols.lout" alt="레이아웃" /></td>
	<td class="body_lt" id="docItemsPopArea"><select onchange="drawItems()"
		id="rowCnt" name="rowCnt"<u:elemTitle titleId="wf.cmpt.items.rows" alt="줄" />>
		<option value="">- <u:msg titleId="wf.cmpt.items.rows" alt="줄" /> -</option>
		<c:forEach var="row" begin="1" end="${maxRows }" step="1" varStatus="status">
		<option value="${row }">${row } <u:msg titleId="wf.cmpt.items.rows" alt="줄" /></option>
		</c:forEach></select>
		<select onchange="drawItems()"
		id="colCnt" name="colCnt"<u:elemTitle titleId="wf.cmpt.items.cols" alt="칸" />>
		<option value="">- <u:msg titleId="wf.cmpt.items.cols" alt="칸" /> -</option>
		<c:forEach var="col" begin="1" end="20" step="1" varStatus="status">
		<option value="${col }">${col } <u:msg titleId="wf.cmpt.items.cols" alt="칸" /></option>
		</c:forEach></select></td>
	</tr>
	
</u:listArea>
<div id="docItemsModArea" style="padding-bottom:10px; display:none;overflow-x:auto;white-space:nowrap;">
<table class="listtable" border="0" cellpadding="0" cellspacing="1">
<colgroup></colgroup>
<tbody></tbody>
</table>
</div>
</c:if><c:if test="${param.pageTyp eq 'title' }">
<u:listArea id="listArea">
<tr>
	<td width="20%" class="head_lt"><u:mandatory /><u:msg titleId="wf.cols.title" alt="제목" /></td>
	<td class="body_lt" ><table cellspacing="0" cellpadding="0" border="0">
			<tr>
			<td id="langTypArea">
				<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
					<u:convert srcId="${whMdBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
					<u:set test="${status.first}" var="style" value="width:200px;" elseValue="width:200px; display:none" />
					<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
					<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="wf.cols.title" value="${rescVa}" style="${style}"
						maxByte="120" validator="changeLangSelector('listArea', id, va)" mandatory="Y" />
				</c:forEach>
				<u:input type="hidden" id="rescId" value="${whMdBVo.rescId}" />
			</td>
			<td id="langTypOptions">
				<c:if test="${fn:length(_langTypCdListByCompId)>1}">
					<select id="langSelector" onchange="changeLangTypCd('listArea','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
					<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
					<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
					</c:forEach>
					</select>
				</c:if>
			</td><td class="width20"></td>
			<td class="body_lt">
				<u:checkArea>
					<u:radio name="rescUseYn" value="Y" titleId="cm.option.use" alt="사용" inputClass="bodybg_lt" />
					<u:radio name="rescUseYn" value="N" titleId="cm.option.notUse" alt="사용안함" inputClass="bodybg_lt" checked="true"/>
				</u:checkArea>
			</td>
			
			</tr>
			</table></td>
	</tr>
	
</u:listArea>
</c:if>
<c:if test="${param.pageTyp eq 'table' }"><div id="loutMaxWdthsArea"></div><div id="loutWdthsArea"></div></c:if>
</form>

<form id="docItemsPopHiddenArea" style="display:none;">
<%-- <select
	id="item" name="item" class="skipThese"<u:elemTitle titleId="ap.cmpt.items" alt="항목지정" />>
	<option value="">- <u:msg titleId="cm.btn.sel" alt="선택" /> -</option>
	<c:forEach
		items="${items}" var="item" >
	<option value="${item}"><u:msg titleId="wf.form.items.${item}" alt="Text/Text Area/.." /></option></c:forEach>
</select> --%>
<select onchange="checkColspan(this)"
	id="colspan" name="colspan" class="skipThese"<u:elemTitle titleId="wf.cmpt.items.cols" alt="칸" />>
	<c:forEach var="col" begin="1" end="${maxCols }" step="1" varStatus="status">
		<option value="${col }">${col } <u:msg titleId="wf.cmpt.items.cols" alt="칸" /></option>
		</c:forEach>
	</select>
</form>
<div id="loutMaxWdthsHiddenArea" style="display:none;">
<select
	id="maxWdthPerc" name="maxWdthPerc" class="skipThese"<u:elemTitle titleId="wf.cols.max.wdth" alt="전체넓이" /> >
			<option value="none"><u:msg titleId="wf.cols.max.wdth" alt="전체넓이"/></option><c:forEach begin="20" end="100" step="1"
			var="width" varStatus="widthStatus"><u:option value='${width}%' title='${width}%' selected="${widthStatus.last }"/></c:forEach></select>
</div>

<div id="loutWdthsHiddenArea" style="display:none;">
<select
	id="wdthPerc" name="wdthPerc" class="skipThese"<u:elemTitle titleId="cols.wdth" alt="넓이" /> >
			<option value=""> -- </option><c:forEach begin="3" end="100" step="1"
			var="width" varStatus="widthStatus"><u:option value='${width}%' title='${width}%' /></c:forEach></select>
</div>
<u:buttonArea>
	<u:button titleId="cm.btn.confirm" href="javascript:setApply('${param.pageTyp }');" alt="확인" auth="A" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>
</div>