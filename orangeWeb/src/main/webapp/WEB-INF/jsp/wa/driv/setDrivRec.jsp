<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.Date" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="paramsForList" excludes="recNo" />
<c:set var="drivInitCnt" value="0"/><!-- 초기 row수 -->
<c:set var="maxRow" value="${fn:length(formBodyXML.getChildList('body/drivs')) }"/><!-- 전체 Row수 -->
<u:set var="drivStrtCnt" test="${empty formBodyXML.getChildList('body/drivs') }" value="${drivInitCnt }" elseValue="0"/><!-- 추가 Row수 -->
<u:set var="pageRowCnt" test="${empty param.pageRowCnt }" value="10" elseValue="${param.pageRowCnt }"/><!-- 목록 페이징 건수 -->
<u:set var="pageNo" test="${empty param.pageNo }" value="1" elseValue="${param.pageNo }"/>
<u:params var="viewParams" excludes="menuId"/>
<c:set var="date" value="<%=new Date() %>"/>
<fmt:formatDate var="today" value="${date}" type="date" dateStyle="default"/>
<style type="text/css">
tr.hiddenCls{display:none;}
@media print {
tr.hiddenCls{display:table-row;}
}
</style>
<script type="text/javascript" src="${_cxPth}/js/calendar/moment.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/calendar/locales.min.js" charset="UTF-8"></script>
<script type="text/javascript">
<!--<% // 더보기 %>
function moreList(isAll){
	var divTrs=$('#xml-drivs\\/driv tr.hiddenCls');
	//loading('moreContainer', true);
	$.each(divTrs, function(index){
		if(isAll || (index+1) <= Number('${pageRowCnt}')){
			$(this).show();
			$(this).attr('class','');
			setJsUniform(this);
		}
	});
	//loading('moreContainer', false);
	if($('#xml-drivs\\/driv tr.hiddenCls').length==0){
		$('#moreContainer').find('a#moreBtn, a#moreAllBtn').hide();
	}
}
<% // 페이징 %>
function goDrivPage(pageNo){
	var divTrs=$('#xml-drivs\\/driv tr#driv');
	divTrs.hide();
	$.each(divTrs, function(index){
		if((index+1) > (pageNo-1)*Number('${pageRowCnt}') && (index+1) <= pageNo*Number('${pageRowCnt}'))
			$(this).show();
	});
	
	//alert($('#xml-drivs\\/driv tr#driv').length);
}<% //이미지 상세보기 - 팝업 오픈 %>
function viewImagePop(id){
	var url='./viewImagePop.do?menuId=${menuId}';
	if(id!='') url+='&carKndNo='+id;
	parent.dialog.open('viewImageDialog','<u:msg titleId="st.cols.img" alt="이미지" />', url);
};<% // [하단버튼:삭제] %>
function delDrivRec(recNo) {
	var arr = [];
	arr.push(recNo);
	if (arr != null && confirmMsg("cm.cfrm.del")) {<% // 삭제하시겠습니까? %>
		callAjax('./transDrivRecDelAjx.do?menuId=${menuId}', {recNos:arr}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.replace('./listDrivRec.do?${paramsForList }');
			}
		});
	}
};<% // [하단버튼:수정] 수정화면으로 이동 %>
function setDrivRec(){
	location.href = './setDrivRec.do?${paramsForList}&recNo=${param.recNo}';
}
<% // [하단버튼:목록] 목록으로 이동 %>
function goList() {
	location.href = './listDrivRec.do?${paramsForList}';
}
<c:if test="${formBodyMode ne 'view'}">
<% // [콤보박스] - 법인선택시 차종목록 조회 %>
function setSelectCorpNo(){
	$('#corpNo').change(function(){
 		$('#carKndNo').find('option').each(function(){
 			$(this).remove();
 		}); 		
 		if($(this).val() != ''){
 			callAjax('./getCarKndListAjx.do?menuId=${menuId}', {corpNo:$(this).val()}, function(data) {
 				if (data.message != null) {
 					alert(data.message);
 					return;
 				}
 				if(data.waCarKndLVoList != null){
 					$.each(data.waCarKndLVoList , function(index, carVo) {
 						$vo=carVo.map;
 		        		$('#carKndNo').append('<u:option value="'+$vo.carKndNo+'" title="'+$vo.carKndNm+'('+$vo.carRegNo+')"/>');
 		        	});
 					$("#carKndNo option:eq(0)").attr("selected", "selected"); 
 			 		$('#carKndNo').uniform();
 				}
 			});
 		}
 	});
}<%// 선택삭제%>
function delSelRow(conId){
	var arr = getCheckedTrs(conId, "cm.msg.noSelect");
	if(arr!=null) {
		delRowInArr(arr);
	}
}<%// 삭제 - 배열에 담긴 목록%>
function delRowInArr(rowArr){
	for(var i=0;i<rowArr.length;i++){
		$(rowArr[i]).remove();
	}
}<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(conId, noSelectMsg){
	var arr=[], id, obj;
	conId=conId.replace('/','\\/');
	$("#"+conId+" tbody:first input[type='checkbox']:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr'){
			arr.push(obj);
			$rowspan=$(obj).find('td:first').attr('rowspan');
			if($rowspan!=undefined && $rowspan>1){
				for(var i=0;i<$rowspan-1;i++){
					arr.push($(obj).next());
				}
			}
		}
		
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
}<% // [버튼클릭] - 사용자추가  %>
function addRowUser(){
	searchUserPop({data:null, multi:true, mode:'search'}, function(arr){
		if(arr!=null){
			var param=null;
			arr.each(function(index, userVo){
				param=new ParamMap();
				param.put('deptNm', userVo.deptRescNm);
				param.put('userNm', userVo.rescNm);
				addRow('xml-drivs/driv', 'driv', '${drivStrtCnt }', param);
			});
		}
	});
};
var maxRow="${maxRow}"; // 최대 Row수
// 일자
//var useYmd=maxRow==0 ? moment().format('YYYY-MM-DD') : moment("${formBodyXML.getChildList('body/drivs')[maxRow-1].getAttr('useYmd')}").isValid() ? 
//		moment("${formBodyXML.getChildList('body/drivs')[maxRow-1].getAttr('useYmd')}").add(1,'d').format('YYYY-MM-DD') : moment().format('YYYY-MM-DD');
<%// 행추가%>
function addRow(conId, trId, strtCnt, param){
	if(param==null){
		param= new ParamMap();
		param.put('deptNm', '${sessionScope.userVo.deptNm}');
		param.put('userNm', '${sessionScope.userVo.userNm}');
	}	
	if(strtCnt==undefined) strtCnt = 0;
	strtCnt++;
	conId=conId.replace('/','\\/');
	var $tr = $("#"+conId+" tbody:first #hiddenTr");
	var html = $tr[0].outerHTML;
	//var today=moment().format('YYYY-MM-DD');
	var useYmd=getLastDay(conId, trId);
	if(maxRow==null) maxRow=0;
	var firstTr=$("#"+conId+" tbody:first #"+trId+":first");
	if(firstTr[0]===undefined) firstTr=$tr;
	html = html.replace(/_NO/g,'_'+(parseInt(maxRow)+parseInt(strtCnt)));
	maxRow++;
	firstTr.before(html);
	$tr = firstTr.prev();
	$tr.attr('id',trId);
	$tr.attr('style','');
	if(param!=undefined){
		if(param.get('deptNm')) $tr.find('#deptNm').val(param.get('deptNm'));
		if(param.get('userNm')) $tr.find('#userNm').val(param.get('userNm'));
	}
	$tr.find("input[id^='useYmd']").val(useYmd);
	console.log(firstTr.find('#drivNextDstcVa').val());
	if(firstTr.find("input[id^='drivNextDstcVa']").val()!='') $tr.find("input[id^='drivPrevDstcVa']").val(firstTr.find("input[id^='drivNextDstcVa']").val());
	setJsUniform($tr[0]);
	useYmd=moment(useYmd).add(1,'d').format('YYYY-MM-DD');
}<%
// 마지막 일자 조회 %>
function getLastDay(conId, trId){
	var trList = $("#"+conId+" tbody:first #"+trId);
	if(trList===undefined || trList.length==0) return moment().format('YYYY-MM-DD');
	var returnDay=null;
	//for(var i=trList.length-1;i>=0;i--){
	for(var i=0;i<trList.length;i--){
		if($(trList[i]).find("input[id^='useYmd']")===undefined || $(trList[i]).find("input[id^='useYmd']").val()=='' || !moment($(trList[i]).find("input[id^='useYmd']").val()).isValid()) continue;
		returnDay=moment($(trList[i]).find("input[id^='useYmd']").val()).add(1,'d').format('YYYY-MM-DD');
		break;
	}
	if(returnDay==null) return moment().format('YYYY-MM-DD');
	return returnDay;
}
<%
//(formBodyXML) 관련 %>
function getFormBodyXML(){
	var xmlDoc = createXMLParser("<formBodyXML></formBodyXML>");
	var root = xmlDoc.getElementsByTagName("formBodyXML")[0];
	
	var head = xmlDoc.createElement("head");
	root.appendChild(head);
	
	var body = xmlDoc.createElement("body");
	root.appendChild(body);
	
	var $area = $("#xmlArea");
	$area.find("[id='xml-head'] :input").each(function(){
		$(head).attr($(this).attr('name'), $(this).val());
	});
	
	var elemId, loopId=null, p;
	$area.find("[id='xml-body'] [id^='xml-']").each(function(){
		elemId = $(this).attr('id').substring(4);
		p = elemId.indexOf('/');
		if(p>0){
			loopId = elemId.substring(p+1);
			elemId = elemId.substring(0,p);
			
			var pElem = xmlDoc.createElement(elemId), elem;
			body.appendChild(pElem);
			
			$(this).find("[id='"+loopId+"']").each(function(){
				//console.log('rowspan : '+$(this).children('td:first').attr('rowspan'));
				var elem = xmlDoc.createElement(loopId), tp, attNm, attVa;
				var valid = true;
				$(this).find(":input").each(function(){
					//if($(this).attr('data-exclude')=='Y') return true;
					if(valid){
						tp = $(this).attr('type');
						tp = tp==null ? '' : tp.toLowerCase();
						if(tp=='radio'||tp=='checkbox'){
							if(!this.checked) return;
						}
						attNm = $(this).attr('name');
						attVa = $(this).val();
						if(attNm!=null && !attNm.startsWith('exc')){
							$(elem).attr($(this).attr('name'), attVa);
							if($(this).attr('data-validation')=='Y' && attVa==''){
								valid = false;
							}
						}
					}
				});
				if(valid){
					pElem.appendChild(elem);
				}
			});
		} else {
			var elem = xmlDoc.createElement(elemId);
			body.appendChild(elem);
			var tp, attNm, attVa;
			$(this).find(":input").each(function(){
				//if($(this).attr('data-exclude')=='Y') return true;
				tp = $(this).attr('type');
				tp = tp==null ? '' : tp.toLowerCase();
				if(tp=='radio'||tp=='checkbox'){
					if(!this.checked) return;
				}
				attNm = $(this).attr('name');
				attVa = $(this).val();
				if(attNm!=null && !attNm.startsWith('exc')){
					$(elem).attr($(this).attr('name'), attVa);
					if($(this).attr('data-validation')=='Y' && attVa==''){
						valid = false;
					}
					if(this.tagName.toLowerCase()=='select'){
						$(elem).attr(attNm+'Nm', $(this).text());
					}
				}
			});
		}
	});
	
	return serializeXML(xmlDoc);
}
function createXMLParser(xmlString){
	if(window.DOMParser){
		var parser = new DOMParser();
		return parser.parseFromString(xmlString, "text/xml");
	} else {
		var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
		xmlDoc.async = false;
		xmlDoc.loadXML(xmlString);
     return xmlDoc;
	}
}
function serializeXML(xmlDoc){
	if(window.XMLSerializer){
		var serializer = new XMLSerializer();
		return serializer.serializeToString(xmlDoc);
	} else {
		return xmlDoc.xml;
	}
}
<%
//xml 히든값 세팅 및, html 변환 세팅 %>
function setXMLByValue(formBodyXML, applyHtmlArea){
	var $xmlDataArea = $("#formBodyXMLDataArea");
	$xmlDataArea.html('');
	$xmlDataArea.appendHidden({'name':'bodyXml','value':formBodyXML});
	
	var array = $('#dftArea :input').serializeArray();
    $.each( array, function(index, field ) {
    	$xmlDataArea.appendHidden({'name':field.name,'value':field.value});
    });

}
<%
//저장 - xml 세팅 %>
function save(){
	var $xmlArea = $("#xmlArea");
	if($xmlArea.length>0){
		var formBodyXML = '';
		if(window.collectFormBodyXML){
			formBodyXML = collectFormBodyXML();
			if(formBodyXML==null) return false;
		} else if(window.checkFormBodyXML && checkFormBodyXML()==false){
			return false;
		} else {
			formBodyXML = getFormBodyXML();
		}
		if(formBodyXML!=''){
			//loading('xml-body', true);
			setXMLByValue(formBodyXML, true);
			var $form = $('#setForm');
			$form.attr('method','post');
			$form.attr('action','./transDrivRec.do?menuId=${menuId}');
			$form.attr('target','dataframe');		
			$form[0].submit();
			//loading('xml-body', false);
		}
	}
}

<%//xml 수집 전에 호출함 %>
function checkFormBodyXML(){
	return validator.validate('dftArea');
}
<%//초기화 %>
function reset(){
	location.replace(location.href);
}
var isAutoCalculate=true; // 자동계산여부
<%//자동계산 초기화 %>
function setCalculate(){
	var tr, name, va1, va2, va3, obj1, obj2, obj3;
	$('tr#driv').each(function(){
		$(this).find("[id^='drivPrevDstcVa'], [id^='drivNextDstcVa'], [id^='drivTotalDstcVa'], [id^='comteDstcVa'], [id^='gnrlDstcVa']").change(function(){
			tr=$(this).closest('tr');
			name=$(this).attr('name');
			if((name=='drivPrevDstcVa' && $(tr).find('input[name="drivNextDstcVa"]').val()!='') || 
					(name=='drivNextDstcVa' && $(tr).find('input[name="drivPrevDstcVa"]').val()!='')){ // 주행전 주행후 거리 변경시
				obj1=tr.find('input[name="drivPrevDstcVa"]');
				obj2=tr.find('input[name="drivNextDstcVa"]');
				obj3=$(tr).find('input[name="drivTotalDstcVa"]');
				va1=removeComma(obj1.val());
				va2=removeComma(obj2.val());
				if(va1=='' || isNaN(va1) || va2=='' || isNaN(va2)) return;
				if(parseInt(va1)>parseInt(va2)){
					alert('주행 전 계기판의 거리가 더 클 수 없습니다.');
					obj1.val('0');
					obj2.val('0');
					obj3.val('0');
					return;
				}
				obj3.val(addComma(String(parseInt(va2)-parseInt(va1))));
				obj3.trigger('change'); // 이벤트 발생
			}
			if(name=='drivTotalDstcVa'){ // 주행거리
				obj1=$(this).closest('tbody');
				va2=0;
				obj1.find('tr#driv input[name="drivTotalDstcVa"]').each(function(){					
					va1=removeComma($(this).val());
					if($(this).val()=='' || isNaN(va1)) return true; 
					va2+=parseInt(va1);
				});
				$('#xml-total input[name="totalDstcVa"]').val(addComma(String(va2)));
				$('#xml-total input[name="totalDstcVa"]').trigger('change');
			}
			
			if(name=='comteDstcVa' || name=='gnrlDstcVa' ){ // 업무용 사용거리
				obj1=$(this).closest('tbody');
				va2=0;
				obj1.find('tr#driv input[name="comteDstcVa"], tr#driv input[name="gnrlDstcVa"]').each(function(){					
					va1=removeComma($(this).val());
					if($(this).val()=='' || isNaN(va1)) return true;
					va2+=parseInt(va1);
				});
				$('#xml-total input[name="bizDstcVa"]').val(addComma(String(va2)));
				$('#xml-total input[name="bizDstcVa"]').trigger('change');
			}
		});
	});
	
	$('#xml-total').find('input[name="totalDstcVa"], input[name="bizDstcVa"]').change(function(){
		obj3=$(this).closest('tbody');
		obj1=obj3.find('input[name="totalDstcVa"]');
		obj2=obj3.find('input[name="bizDstcVa"]');
		if(obj1.val()=='' || obj2.val()=='') return false;
		va1=removeComma(obj1.val());
		va2=removeComma(obj2.val());
		va3=parseInt(va2)/parseInt(va1);
		if(isNaN(va3)) return false;
		var totalPer=((parseInt(va2)/parseInt(va1))*100);
		if(totalPer>100){
			alert('업무사용비율이 100%가 넘을 수 없습니다.');
			obj2.val('');
			obj3.find('input[name="bizRateVa"]').val('');
			return false;
		}
		obj3.find('input[name="bizRateVa"]').val(totalPer.toFixed(1));
	});
}
<%//자동계산 변경 %>
function autoCalculate(obj){
	var title='';
	if(isAutoCalculate){
		isAutoCalculate=false;
		title='자동계산사용';
		$('tr#driv').find("[id^='drivPrevDstcVa'], [id^='drivNextDstcVa'], [id^='drivTotalDstcVa'], [id^='comteDstcVa'], [id^='gnrlDstcVa']").off('change');
		$('#xml-total').find('input[name="totalDstcVa"], input[name="bizDstcVa"]').off('change');
	}else{
		isAutoCalculate=true;
		title='자동계산안함';
		setCalculate();
		$('tr#driv').find("[id^='drivPrevDstcVa'], [id^='drivNextDstcVa'], [id^='drivTotalDstcVa'], [id^='comteDstcVa'], [id^='gnrlDstcVa']").trigger('change');
		$('#xml-total').find('input[name="totalDstcVa"], input[name="bizDstcVa"]').trigger('change');
	}
	$(obj).attr('title', title);
	$(obj).find('span').text(title);
}
</c:if>

<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, button, select").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
};
$(document).ready(function() {
	<c:if test="${formBodyMode ne 'view'}">
	setSelectCorpNo(); // 법인선택 차종 조회
	setCalculate(); // 자동계산
	$("#dftArea tbody:first, #xml-drivs\\/driv tbody:first, #xml-total tbody:first").children().each(function(){
		<%// 행추가 영역 제외하고 uniform 적용%>
		if($(this).attr('id')!='hiddenTr' && !$(this).hasClass('hiddenCls')){
			setJsUniform(this);
		}
	});
	setJsUniform($('#dftArea'));
	setJsUniform($('#paginationForm'));
	//setJsUniform($('#xml-body'));
	</c:if>
	<c:if test="${formBodyMode eq 'view'}">setUniformCSS();</c:if>
});

//-->
</script>
<u:title titleId="wa.title.driv.rec" menuNameFirst="true" alt="운행기록부"/>
<% // 탭 %>
<c:if
	test="${formBodyMode ne 'view'}">
<% // 하단 버튼 %>
<u:buttonArea>
<u:button titleId="cm.btn.reset" alt="초기화" href="javascript:reset();" auth="A" />
<u:button titleId="cm.btn.save" onclick="save();" alt="저장" auth="A" />
<u:button titleId="cm.btn.cancel" alt="취소" href="javascript:history.go(-1);" />
</u:buttonArea>	
<div id="xmlArea">
<div id="xml-body">
<u:title title="기본정보" type="small" alt="기본정보" />
<u:listArea id="dftArea" colgroup="15%,35%,15%,35%">
	<tr>
		<td class="head_ct"><u:mandatory />사업연도</td>
		<td colspan="3">
			<table border="0" cellpadding="0" cellspacing="0">
				<tbody>
				<tr>
					<td width="80px"><u:calendar id="strtDt" option="{end:'endDt'}" titleId="cols.strtYmd" alt="시작일" mandatory="Y" value="${waDrivRecBVo.strtDt }"/></td>
					<td class="body_lt">~</td>
					<td width="80px"><u:calendar id="endDt" option="{start:'strtDt'}" titleId="cols.endYmd" alt="종료일" mandatory="Y" value="${waDrivRecBVo.endDt }"/></td>
				</tbody>
			</table>
		</td>		
	</tr>
	<tr>
		<td class="head_ct"><u:mandatory />법인명(사업자등록번호)</td>
		<td><select id="corpNo" name="corpNo" style="min-width:100px;">
					<c:forEach var="vo" items="${waCorpBVoList }" varStatus="status">
						<u:option value="${vo.corpNo }" title="${vo.corpNm }(${vo.corpRegNo })" alt="${vo.corpNm }" checkValue="${waCorpBVo.corpNo}" />
					</c:forEach></select></td>
		<td class="head_ct"><u:mandatory />①(②) 차종(자동차등록번호)</td>
		<td><select id="carKndNo" name="carKndNo" style="min-width:150px;">
					<c:forEach var="vo" items="${waCarKndLVoList }" varStatus="status">
						<u:option value="${vo.carKndNo }" title="${vo.carKndNm }(${vo.carRegNo })" alt="${vo.carKndNm }" checkValue="${waCarKndLVo.carKndNo}" />
					</c:forEach></select></td>
	</tr>
</u:listArea>
<u:title title="업무용 사용비율 계산" type="small" alt="업무용 사용비율 계산">
<u:titleButton title="자동계산안함" onclick="autoCalculate(this);" alt="자동계산안함"/><u:titleButton title="사용자추가" onclick="addRowUser();" alt="추가"/><u:titleButton title="추가" onclick="addRow('xml-drivs/driv', 'driv', '${drivStrtCnt }', null);" alt="추가"/><u:titleButton title="선택삭제" onclick="delSelRow('xml-drivs/driv');" alt="선택삭제"/>
</u:title>
<u:listArea id="xml-drivs/driv" colgroup="3%,13%,15%,7%,9%,9%,9%,9%,9%,">
	<tr id="headerTr">
		<td rowspan="3" class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('xml-drivs\\/driv', this.checked);" value=""/></td>
		<td rowspan="3" class="head_ct" >③사용일자</td>
		<td colspan="2" class="head_ct" >④사용자</td>
		<td colspan="6" class="head_ct" >운행내역</td>
	</tr>
	<tr id="headerTr">
		<td rowspan="2" class="head_ct" >부서</td>
		<td rowspan="2" class="head_ct" >성명</td>
		<td rowspan="2" class="head_ct" >⑤주행 전 계기판의 거리(km)</td>
		<td rowspan="2" class="head_ct" >⑥주행 후 계기판의 거리(km)</td>
		<td rowspan="2" class="head_ct" >⑦주행거리(km)</td>
		<td colspan="2" class="head_ct" >업무용 사용거리(km)</td>
		<td rowspan="2" class="head_ct" >⑩비고</td>
	</tr>
	<tr id="headerTr">
		<td class="head_ct" >⑧출ㆍ퇴근용(㎞)</td>
		<td class="head_ct" >⑨일반 업무용(㎞)</td>
	</tr>
	<c:forEach begin="0" end="${fn:length(formBodyXML.getChildList('body/drivs'))+drivStrtCnt}" var="index" varStatus="status"
		><c:set var="xmlMap" value="${status.last ? '' : formBodyXML.getChildList('body/drivs')[index]}"
		/><u:set test="${status.last}" var="trDisp" value="display:none"
		/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="driv"
		/><u:set test="${status.last}" var="statusIndex" value="_NO" elseValue="_${status.index+1}"
		/><u:set test="${!status.last && !(status.count > (pageNo-1)*pageRowCnt && status.count <= pageNo*pageRowCnt)}" var="trCls" value="hiddenCls"
		/>
	<tr id="${trId}" style="${trDisp}" class="${trCls }">
		<td class="body_ct"><input type="checkbox" value="" /></td>
		<td class="body_ct"><u:calendar id="useYmd${statusIndex }" title="사용일자" name="useYmd" value="${empty xmlMap ? '' :  xmlMap.getAttr('useYmd') }" /></td>
		<td class="body_ct"><u:input id="deptNm" titleId="cols.dept" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('deptNm') }" maxByte="100"/></td>
		<td class="body_ct"><u:input id="userNm" titleId="cols.user" style="width:80%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('userNm') }" maxByte="50"/></td>
		<td class="body_ct"><u:input id="drivPrevDstcVa${statusIndex }" name="drivPrevDstcVa" title="주행전계기판의거리" style="width:85%;text-align:right;" value="${empty xmlMap ? '0' :  xmlMap.getAttr('drivPrevDstcVa') }" maxLength="7" valueOption="number" commify="Y"/></td>
		<td class="body_ct"><u:input id="drivNextDstcVa${statusIndex }" name="drivNextDstcVa" title="주행후계기판의거리" style="width:85%;text-align:right;" value="${empty xmlMap ? '0' :  xmlMap.getAttr('drivNextDstcVa') }" maxLength="7" valueOption="number" commify="Y"/></td>
		<td class="body_ct"><u:input id="drivTotalDstcVa${statusIndex }" name="drivTotalDstcVa" title="주행거리" style="width:85%;text-align:right;" value="${empty xmlMap ? '0' :  xmlMap.getAttr('drivTotalDstcVa') }" maxLength="7" valueOption="number" commify="Y"/></td>
		<td class="body_ct"><u:input id="comteDstcVa${statusIndex }" name="comteDstcVa" title="출퇴근용" style="width:85%;text-align:right;" value="${empty xmlMap ? '0' :  xmlMap.getAttr('comteDstcVa') }" maxLength="7" valueOption="number" commify="Y"/></td>
		<td class="body_ct"><u:input id="gnrlDstcVa${statusIndex }" name="gnrlDstcVa" title="일반업무용" style="width:85%;text-align:right;" value="${empty xmlMap ? '0' :  xmlMap.getAttr('gnrlDstcVa') }" maxLength="7" valueOption="number" commify="Y"/></td>
		<td class="body_ct"><div id="notBizDstcVa" style="position:absolute;width:50px;z-index:999;line-height:26px;text-align:left;"><strong></strong></div><u:input id="note" name="note" titleId="cols.note" style="width:85%;" value="${empty xmlMap ? '' :  xmlMap.getAttr('note') }" maxByte="100"/></td>
	</tr>
	</c:forEach>
	<c:if test="${fn:length(formBodyXML.getChildList('body/drivs'))>pageRowCnt }"><tr><td colspan="10"><div id="moreContainer" style="padding:10px;text-align:center;position:relative;"><u:buttonS id="moreBtn" title="더보기" onclick="moreList(false);"/><u:buttonS id="moreAllBtn" title="전체보기" onclick="moreList(true);"/></div></td></tr></c:if>
</u:listArea>
<u:listArea id="xml-total" colgroup="33%,33%,">
<tr id="headerTr">
		<td class="head_ct" >⑪사업연도 총주행 거리(㎞)</td>
		<td class="head_ct" >⑫사업연도 업무용 사용거리(㎞)</td>
		<td class="head_ct" >⑬업무사용비율(⑫/⑪)</td>
	</tr>
	<tr>
		<td class="body_ct"><u:input id="totalDstcVa" title="사업연도 총주행 거리(㎞)" style="width:55%;text-align:right;" value="${empty formBodyXML ? '' :  formBodyXML.getAttr('body/total.totalDstcVa') }" maxLength="7" valueOption="number" commify="Y"/></td>
		<td class="body_ct"><u:input id="bizDstcVa" title="사업연도 업무용 사용거리(㎞)" style="width:55%;text-align:right;" value="${empty formBodyXML ? '' :  formBodyXML.getAttr('body/total.bizDstcVa') }" maxLength="7" valueOption="number" commify="Y"/></td>
		<td class="body_ct" ><u:input id="bizRateVa" title="업무사용비율(⑫/⑪)" style="width:55%;text-align:right;" value="${empty formBodyXML ? '' :  formBodyXML.getAttr('body/total.bizRateVa') }" maxLength="7" valueOption="number" commify="Y"/>%</td>
	</tr>
</u:listArea>
<%-- <u:pagination pageRowCnt="${pageRowCnt }" /> --%>
</div>
</div>
<!-- <hr style="border: none;border-top: 2px dotted blue;color: #fff;background-color: #fff;height: 1px;width: 100%;display:block;"/> -->
<u:blank />
<form id="setForm">
<u:input type="hidden" id="menuId" value="${menuId }"/>
<u:input type="hidden" id="listPage" value="./listDrivRec.do?${paramsForList }"/>
<u:input type="hidden" id="viewPage" value="./viewDrivRec.do?${viewParams }"/>
<c:if test="${!empty param.recNo }"><u:input type="hidden" id="recNo" value="${param.recNo }"/></c:if>
<div id="formBodyXMLDataArea"></div>
</form>
<% // 하단 버튼 %>
<u:buttonArea topBlank="true">
<u:button titleId="cm.btn.reset" alt="초기화" href="javascript:reset();" auth="A" />
<u:button titleId="cm.btn.save" onclick="save();" alt="저장" auth="A" />
<u:button titleId="cm.btn.cancel" alt="취소" href="javascript:history.go(-1);" />
</u:buttonArea>
</c:if><c:if
	test="${formBodyMode eq 'view'}">
<% // 하단 버튼 %>
<u:buttonArea>
<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" auth="R" />
<u:button titleId="cm.btn.mod" onclick="setDrivRec();" alt="수정" auth="A" />
<u:button titleId="cm.btn.del" alt="삭제" onclick="delDrivRec('${waDrivRecBVo.recNo }');" auth="W" ownerUid="${waDrivRecBVo.regrUid }"/>
<u:button titleId="cm.btn.list" alt="목록" onclick="goList();" />
</u:buttonArea>
	
<u:title title="기본정보" type="small" alt="기본정보" />
<u:listArea id="dftArea" colgroup="15%,35%,15%,35%">
	<tr>
		<td class="head_ct">사업연도</td>
		<td class="body_lt" colspan="3"><u:out value="${waDrivRecBVo.strtDt }" type="date"/> ~ <u:out value="${waDrivRecBVo.endDt }" type="date"/></td>		
	</tr>
	<tr>
		<td class="head_ct">법인명(사업자등록번호)</td>
		<td class="body_lt"><u:out value="${waDrivRecBVo.corpNm }" /> (<u:out value="${waDrivRecBVo.corpRegNo }"/>)</td>
		<td class="head_ct">①(②) 차종(자동차등록번호)</td>
		<td class="body_lt"><a href="javascript:viewImagePop('${waDrivRecBVo.carKndNo }');"><u:out value="${waDrivRecBVo.carKndNm }" /> (<u:out value="${waDrivRecBVo.carRegNo }"/>)</a></td>
	</tr>
</u:listArea>
<u:title title="업무용 사용비율 계산" type="small" alt="업무용 사용비율 계산" />
<u:listArea id="xml-drivs/driv" colgroup="13%,15%,7%,9%,9%,9%,9%,9%*">
	<tr id="headerTr">
		<td rowspan="3" class="head_ct" >③사용일자</td>
		<td colspan="2" class="head_ct" >④사용자</td>
		<td colspan="6" class="head_ct" >운행내역</td>
	</tr>
	<tr id="headerTr">
		<td rowspan="2" class="head_ct" >부서</td>
		<td rowspan="2" class="head_ct" >성명</td>
		<td rowspan="2" class="head_ct" >⑤주행 전 계기판의 거리(km)</td>
		<td rowspan="2" class="head_ct" >⑥주행 후 계기판의 거리(km)</td>
		<td rowspan="2" class="head_ct" >⑦주행거리(km)</td>
		<td colspan="2" class="head_ct" >업무용 사용거리(km)</td>
		<td rowspan="2" class="head_ct" >⑩비고</td>
	</tr>
	<tr id="headerTr">
		<td class="head_ct" >⑧출ㆍ퇴근용(㎞)</td>
		<td class="head_ct" >⑨일반 업무용(㎞)</td>
	</tr>
	<c:forEach begin="0" end="${fn:length(formBodyXML.getChildList('body/drivs'))+drivStrtCnt}" var="index" varStatus="status"
		><c:set var="xmlMap" value="${status.last ? '' : formBodyXML.getChildList('body/drivs')[index]}"
		/><u:set test="${status.last}" var="trDisp" value="display:none"
		/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="driv"
		/><u:set test="${status.last}" var="statusIndex" value="_NO" elseValue="_${status.index+1}"
		/><u:set test="${!status.last && !(status.count > (pageNo-1)*pageRowCnt && status.count <= pageNo*pageRowCnt)}" var="trCls" value="hiddenCls"
		/>
	<tr id="${trId}" style="${trDisp}" class="${trCls }">
		<td class="body_ct"><u:out value="${empty xmlMap ? '' :  xmlMap.getAttr('useYmd') }" type="date"/></td>
		<td class="body_lt"><u:out value="${empty xmlMap ? '' :  xmlMap.getAttr('deptNm') }" /></td>
		<td class="body_ct"><u:out value="${empty xmlMap ? '' :  xmlMap.getAttr('userNm') }" /></td>
		<td class="body_rt"><u:out value="${empty xmlMap ? '' :  xmlMap.getAttr('drivPrevDstcVa') }" /></td>
		<td class="body_rt"><u:out value="${empty xmlMap ? '' :  xmlMap.getAttr('drivNextDstcVa') }" /></td>
		<td class="body_rt"><u:out value="${empty xmlMap ? '' :  xmlMap.getAttr('drivTotalDstcVa') }" /></td>
		<td class="body_rt"><u:out value="${empty xmlMap ? '' :  xmlMap.getAttr('comteDstcVa') }" /></td>
		<td class="body_rt"><u:out value="${empty xmlMap ? '' :  xmlMap.getAttr('gnrlDstcVa') }" /></td>
		<td class="body_lt"><u:out value="${empty xmlMap ? '' :  xmlMap.getAttr('note') }" /></td>
	</tr>
	</c:forEach>
	<c:if test="${fn:length(formBodyXML.getChildList('body/drivs'))>pageRowCnt }"><tr><td colspan="9"><div id="moreContainer" style="padding:10px;text-align:center;position:relative;"><u:buttonS id="moreBtn" title="더보기" onclick="moreList(false);"/><u:buttonS id="moreAllBtn" title="전체보기" onclick="moreList(true);"/></div></td></tr></c:if>
	<tr id="headerTr">
		<td class="head_ct" colspan="3" rowspan="2"></td>
		<td class="head_ct" colspan="3">⑪사업연도 총주행 거리(㎞)</td>
		<td class="head_ct" colspan="2">⑫사업연도 업무용 사용거리(㎞)</td>
		<td class="head_ct" >⑬업무사용비율(⑫/⑪)</td>
	</tr>
	<tr id="headerTr">
		<td class="body_ct" colspan="3"><u:out value="${empty formBodyXML ? '' :  formBodyXML.getAttr('body/total.totalDstcVa') }" /></td>
		<td class="body_ct" colspan="2"><u:out value="${empty formBodyXML ? '' :  formBodyXML.getAttr('body/total.bizDstcVa') }" /></td>
		<td class="body_ct" ><u:out value="${empty formBodyXML ? '' :  formBodyXML.getAttr('body/total.bizRateVa') }" />%</td>
	</tr>
</u:listArea>
<%-- <u:pagination pageRowCnt="${pageRowCnt }" /> --%>
<% // 하단 버튼 %>
<u:buttonArea topBlank="true" >
<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" auth="R" />
<u:button titleId="cm.btn.mod" onclick="setDrivRec();" alt="수정" auth="A"/>
<u:button titleId="cm.btn.del" alt="삭제" onclick="delDrivRec('${waDrivRecBVo.recNo }');" auth="W" ownerUid="${waDrivRecBVo.regrUid }"/>
<u:button titleId="cm.btn.list" alt="목록" onclick="goList();" />
</u:buttonArea>
</c:if>	
