<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
var gSkipUinform = true;
var $gSetItemsPopForm = null;
<%// 줄 칸에 맞게 양식 그리기 - 항목지정의 줄(콤보), 칸(콤보) 변경시 호출 %>
function drawItems(){
	var row = $gSetItemsPopForm.find("#rowCnt").val();
	var col = $gSetItemsPopForm.find("#colCnt").val();
	if(row!='' && col!=''){
		row = parseInt(row, 10);<%// 항목지정 - 줄 으로 지정된 값 %>
		col = parseInt(col, 10);<%// 항목지정 - 칸 으로 지정된 값 %>
		var selectHtml = $("#docItemsPopHiddenArea").html();<%// 삽입할 html : select(항목) + select(칸) %>
		if(col!=3){<%// 항목지정의 칸수가 3이 아니면 추가되는 컬럼의 칸수에서 관련 없는것 제거 %>
			var p = selectHtml.lastIndexOf('<option value="'+(col+1)+'"');
			var q = selectHtml.indexOf('</select>',p+1);
			if(p>0 && q>0){
				selectHtml = selectHtml.substring(0,p)+selectHtml.substring(q);
			}
		}
		var r=0, c=0, tr, colspan, $modArea = $gSetItemsPopForm.find("#docItemsModArea"), $tbody = $modArea.find("tbody");
		<%// colgroup 조절%>
		if(col==3) $modArea.find("colgroup").html("<col width='33%'/><col width='34%'/><col width='33%'/>");
		else if(col==2) $modArea.find("colgroup").html("<col width='50%'/><col width='50%'/>");
		else if(col==1) $modArea.find("colgroup").html("<col width='100%'/>");
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
		dialog.resize("setDocItemsDialog");
	}
}
<%// 개별항목의 칸수 변경시 호출 - 칸수의 변경으로 colspan을 넣거나 제거하고, td를 넣거나 제거함 %>
function checkColspan(obj){
	var col = $gSetItemsPopForm.find("#colCnt").val();
	var td = getParentTag(obj,'td');
	$(td).attr('colspan', $(obj).val());
	if(col!=''){
		if($(obj).val()==col){<%// 총 칸수화 현재의 칸수가 같으면 나머지 TD 모두 제거함 %>
			if($(td).prev().prev().length!=0) $(td).prev().prev().remove();
			if($(td).prev().length!=0) $(td).prev().remove();
			if($(td).next().next().length!=0) $(td).next().next().remove();
			if($(td).next().length!=0) $(td).next().remove();
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
				for(;colSum<col;colSum++){
					$p.append("<td>"+selectHtml+"</td>");
					if(!gSkipUinform) $p.find("td:last select").uniform();
				}
			} else if(colSum>col){<%// 전체 칸수(sum)가 클 경우 - TD 제거 또는 colspan 제거 %>
				var $chs = $(td).parent().children();
				var $next = null, start = $chs.length-1;
				for(;start>=0;start--){
					if($chs[start] != td){
						$next = $($chs[start]);
						break;
					}
				}
				if($next != null){
					var colspan = $next.attr('colspan');
					if(colspan==null || colspan==''){
						$next.remove();
					} else {
						$next.find("[name='colspan']").val("1").trigger('click');
						$next.removeAttr("colspan");
					}
				}
			}
		}
	}
}
<%// 확인 버튼 클릭 %>
function setItems(){
	var va, param = new ParamMap();
	if((va = $gSetItemsPopForm.find("#rowCnt").val())==''){
		alertMsg("ap.cmpt.items.setRow");<%//ap.cmpt.items.setRow=항목지정의 줄을 선택해 주십시요.%>
		return;
	}
	param.put("rowCnt", va);
	if((va = $gSetItemsPopForm.find("#colCnt").val())==''){
		alertMsg("ap.cmpt.items.setCol");<%//ap.cmpt.items.setCol=항목지정의 칸을 선택해 주십시요.%>
		return;
	}
	param.put("colCnt", va);
	
	var row = 1, col, $opt, itemId, itemNm, colspan;
	$gSetItemsPopForm.find("#docItemsModArea tr").each(function(){
		col = 1;
		$(this).find("td").each(function(){
			$opt = $(this).find("#item option:selected");
			itemId = $opt.val();
			itemNm = itemId=='' ? '' : $opt.text();
			colspan = $(this).find("#colspan option:selected").val();
			param.put(row+"-"+col,itemId+"-"+colspan);
			param.put(row+"-"+col+"-nm",itemNm+"-"+colspan);
			col++;
		});
		row++;
	});
	setItemsData(param, "${param.seq}");
	dialog.close("setDocItemsDialog");
}
$(document).ready(function() {
	$gSetItemsPopForm = $("#setItemsPopForm");
	if('${param.seq}' != ''){
		var param = new ParamMap().getData($("#docArea #itemsArea[data-seq='${param.seq}']")[0], "items-${param.seq}-");
		param.setData($gSetItemsPopForm.find("#docItemsPopArea")[0]);
		drawItems();
		$gSetItemsPopForm.find("#docItemsModArea select").removeClass("skipThese");
		var keys, vals, $trs=$gSetItemsPopForm.find("#docItemsModArea tr"), $td;
		param.each(function(key, va){
			if(key!='rowCnt' && key!='colCnt'){
				keys = key.split('-');
				vals = va.split('-');
				$td = $trs.eq(parseInt(keys[0],10)-1).children().eq(parseInt(keys[1],10)-1);
				$td.find("#item").val(vals[0]);
				if(vals[1]!='1'){
					$td.find("#colspan").val(vals[1]);
					checkColspan($td.find("#colspan")[0]);
				}
			}
		});
		
	}
	gSkipUinform = false;
});
//-->
</script>
<div style="width:${_lang=='ko' or _lang=='ja' or _lang=='zh' ? '800' : '950'}px">
<form id="setItemsPopForm">
<u:listArea>

	<tr>
	<td width="20%" class="head_lt"><u:msg titleId="ap.cmpt.items" alt="항목지정" /></td>
	<td class="body_lt" id="docItemsPopArea"><select onchange="drawItems()"
		id="rowCnt" name="rowCnt"<u:elemTitle titleId="ap.cmpt.items.rows" alt="줄" />>
		<option value="">- <u:msg titleId="ap.cmpt.items.rows" alt="줄" /> -</option>
		<option value="1">1 <u:msg titleId="ap.cmpt.items.rows" alt="줄" /></option>
		<option value="2">2 <u:msg titleId="ap.cmpt.items.rows" alt="줄" /></option>
		<option value="3">3 <u:msg titleId="ap.cmpt.items.rows" alt="줄" /></option>
		<option value="4">4 <u:msg titleId="ap.cmpt.items.rows" alt="줄" /></option>
		<option value="5">5 <u:msg titleId="ap.cmpt.items.rows" alt="줄" /></option>
		<option value="6">6 <u:msg titleId="ap.cmpt.items.rows" alt="줄" /></option>
		<option value="7">7 <u:msg titleId="ap.cmpt.items.rows" alt="줄" /></option>
		<option value="8">8 <u:msg titleId="ap.cmpt.items.rows" alt="줄" /></option></select>
		<select onchange="drawItems()"
		id="colCnt" name="colCnt"<u:elemTitle titleId="ap.cmpt.items.cols" alt="칸" />>
		<option value="">- <u:msg titleId="ap.cmpt.items.cols" alt="칸" /> -</option>
		<option value="1">1 <u:msg titleId="ap.cmpt.items.cols" alt="칸" /></option>
		<option value="2">2 <u:msg titleId="ap.cmpt.items.cols" alt="칸" /></option>
		<option value="3">3 <u:msg titleId="ap.cmpt.items.cols" alt="칸" /></option></select></td>
	</tr>
	
</u:listArea>

<div id="docItemsModArea" style="padding-bottom:10px; display:none;">
<table class="listtable" border="0" cellpadding="0" cellspacing="1">
<colgroup></colgroup>
<tbody></tbody>
</table>
</div>
</form>

<form id="docItemsPopHiddenArea" style="display:none;">
<select
	id="item" name="item" class="skipThese"<u:elemTitle titleId="ap.cmpt.items" alt="항목지정" />>
	<option value="">- <u:msg titleId="cm.btn.sel" alt="선택" /> -</option>
	<c:forEach
		items="${items}" var="item" >
	<option value="${item}"><u:msg titleId="ap.doc.${item}" alt="기안자/기안부서/.." /></option></c:forEach>
</select>
<select onchange="checkColspan(this)"
	id="colspan" name="colspan" class="skipThese"<u:elemTitle titleId="ap.cmpt.items.cols" alt="칸" />>
	<option value="1">1 <u:msg titleId="ap.cmpt.items.cols" alt="칸" /></option>
	<option value="2">2 <u:msg titleId="ap.cmpt.items.cols" alt="칸" /></option>
	<option value="3">3 <u:msg titleId="ap.cmpt.items.cols" alt="칸" /></option></select>
</form>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" href="javascript:setItems();" alt="확인" auth="A" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>
</div>