<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--
var imgObj=null, etcObj=null;
<%// [순서조절:위로,아래로] 서버에 저장하지 않고 화면상에서만 순서 조정함 %>
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
	$("#carListArea tbody:first input[type='checkbox']:checked").each(function(){
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
<%// 다음 Row 번호 %>
var gMaxRow = parseInt('${fn:length(waCarKndLVoList)}');
<%// 행추가%>
function addRow(){
	var $tr = $("#carListArea tbody:first #hiddenTr");
	var html = $tr[0].outerHTML;
	html = html.replace(/_NO/g,'_'+gMaxRow);
	gMaxRow++;
	$tr.before(html);
	$tr = $tr.prev();
	$tr.attr('id','');
	$tr.attr('style','');
	setJsUniform($tr[0]);
}
<%// 행삭제%>
function delRow(){
	var $tr = $("#carListArea tbody:first #hiddenTr").prev();
	delRowInArr([$tr[0]]);
}
<%// 선택삭제%>
function delSelRow(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) delRowInArr(arr);
}
<%// 삭제 - 배열에 담긴 목록%>
function delRowInArr(rowArr){
	var delVa = $("#delList").val(), delArr = [], $carKndNo;
	if(delVa!='') delArr.push(delVa);
	for(var i=0;i<rowArr.length;i++){
		$carKndNo = $(rowArr[i]).find("input[name='carKndNo']");
		if($carKndNo.val()!=''){
			delArr.push($carKndNo.val());
		}
		$(rowArr[i]).remove();
	}
	$("#delList").val(delArr.join(','));
}<%// 저장%>
function saveCar(){
	<%// trArr : 저장할 곳의 tr 테그 배열 %>
	var trArr=[], count=0, result;
	$("#carListArea tbody:first").children().each(function(){
		trArr.push(this);
	});
	
	<%// 값이 있는지 체크함%>
	for(var i=1;i<trArr.length-1;i++){
		result = checkVa(trArr[i], true);
		if(result<0) return;
		$(trArr[i]).find("[name='valid']").val( (result>0) ? 'Y' : '');
		count += result;
	}
	
	if(count==0 && $('#delList').val()==''){
		alertMsg("cm.msg.nodata.toSave");<%//cm.msg.nodata.toSave=저장할 데이터가 없습니다.%>
		return;
	}
	
	<%// 정렬순서 세팅%>
	var ordr = 1;
	$("#carListArea input[name='sortOrdr']").each(function(){
		if($(this).attr('id')!='sortOrdr_NO'){
			$(this).val(ordr++);
		}
	});
	<%// 서버 전송%>
	var $form = $("#siteListForm");
	$form.attr('method','post');
	$form.attr('action','./transCarList.do');
	$form.attr('target','dataframeForFrame');
	$form.submit();
	
}<%// input에 값이 있는지 체크%>
function checkVa(trObj, ignoreAllEmpty){
	var i, id, va, handler, arr=[];
	$(trObj).find("td[id='required'] input").each(function(){ arr.push(this); });
	
	if(ignoreAllEmpty){
		var hasNoVa=true;
		arr.each(function(index, obj){
			if($(obj).val()!=''){
				hasNoVa = false;
				return false;
			}
		});
		if(hasNoVa) return 0;
	}
	
	for(i=0;i<arr.length;i++){
		id = $(arr[i]).attr('id');
		va = $(arr[i]).val();
		handler = validator.getHandler(id);
		if(handler!=null && handler(id, va)==false){
			$(arr[i]).focus();
			return -1;
		}
	}
	return 1;
}

<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
}
<%// 저장 후 리로드%>
function afterTrans(id){
	parent.openSiteListFrm(id);
}<%// 체크된 목록 리턴 - 잘라내기용 %>
function getCheckedArray(){
	var arr = [], va;
	$("#carListArea input[type=checkbox]:checked").each(function(){
		va = $(this).val();
		if(va!=null && va!='') { arr.push(va); }
	});
	return arr.length==0 ? null : arr;
}<% //이미지 변경 - 팝업 오픈 %>
function setImagePop(obj, id){
	imgObj=obj;
	var url='./setImagePop.do?menuId=${menuId}';
	if(id!='') url+='&carKndNo='+id;
	parent.dialog.open('setImageDialog','<u:msg titleId="st.jsp.select.img" alt="이미지 선택" />', url);
}<% //이미지 상세보기 - 팝업 오픈 %>
function viewImagePop(id){
	var url='./viewImagePop.do?menuId=${menuId}';
	if(id!='') url+='&carKndNo='+id;
	parent.dialog.open('viewImageDialog','<u:msg titleId="st.cols.img" alt="이미지" />', url);
}<% //이미지 ID 세팅 %>
function setTmpImgId(id){
	if(imgObj==null) return;
	$tr = $(imgObj).parents('tr');
	if(id==null){
		$tr.find('input[name="tmpImgId"]').val('');
		return;
	}
	$tr.find('input[name="tmpImgId"]').val(id);
}<% //이미지 삭제 %>
function delImg(obj, id){
	if(id!=''){
		callAjax('./transCarImgDelAjx.do?menuId=${menuId}', {carKndNo:id}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
		});
	}else{
		$tr = $(imgObj).parents('tr');
		$tr.find('input[name="tmpImgId"]').val('');
		alertMsg('cm.msg.del.success'); // 삭제되었습니다.
		return;
	}
}
$(document).ready(function() {
	<c:if test="${mode eq 'list' }">
	$("#carListArea tbody:first").children().each(function(){
		<%// 행추가 영역 제외하고 uniform 적용%>
		if($(this).attr('id')!='hiddenTr'){
			setJsUniform(this);
		}
	});
	</c:if>
	<c:if test="${mode eq 'img' }">setUniformCSS();</c:if>
});
//-->
</script>
<!-- 사이트맵 일괄등록 목록 -->
<c:if test="${mode eq 'list' }">
<form id="siteListForm" style="padding:10px;">
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="corpNo" value="${param.corpNo}" />
<u:listArea id="carListArea" >

	<tr id="headerTr">
		<th width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('carListArea', this.checked);" value=""/></th>
		<th width="25%" class="head_ct"><u:mandatory /><u:msg titleId="wa.cols.carKndNm" alt="차종" /></th>
		<th width="25%" class="head_ct"><u:mandatory /><u:msg titleId="wa.cols.carRegNo" alt="자동차등록번호 " /></th>
		<th width="15%" class="head_ct"><u:msg titleId="st.cols.img" alt="이미지 " /></th>
		<th class="head_ct"><u:msg titleId="cols.note" alt="비고 " /></th>
	</tr>
	<c:forEach items="${waCarKndLVoList}" var="waCarKndLVo" varStatus="status"
			 ><u:set test="${status.last}" var="trDisp" value="display:none"
			/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${waCarKndLVo.carKndNo}"
			/><u:set test="${status.last}" var="carIndex" value="_NO" elseValue="_${status.index+1}"
			/>			 
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="${trId}" style="${trDisp}" >
		<td class="bodybg_ct"><input type="checkbox" value="${waCarKndLVo.carKndNo}"/>
			<u:input type="hidden" id="sortOrdr${carIndex}" name="sortOrdr" />
			<u:input type="hidden" id="valid${carIndex}" name="valid" />
			<u:input type="hidden" id="carKndNo${carIndex}" name="carKndNo" value="${waCarKndLVo.carKndNo }"/>
			<u:input type="hidden" id="tmpImgId${carIndex}" name="tmpImgId" value="" /></td>
		<td class="bodybg_ct" id="required"><u:input id="carKndNm" value="${waCarKndLVo.carKndNm}" titleId="wa.cols.carKndNm" maxByte="120" mandatory="Y" style="width:92%;"/></td>
		<td class="bodybg_ct" id="required"><u:input id="carRegNo" value="${waCarKndLVo.carRegNo}" titleId="wa.cols.carRegNo" maxByte="60" mandatory="Y" style="width:92%;"/></td>
		<td class="bodybg_ct"><u:buttonS alt="변경" titleId="cm.btn.chg" onclick="setImagePop(this, '${waCarKndLVo.carKndNo }');" popYn="Y" />
		<u:buttonS alt="삭제" titleId="cm.btn.del" onclick="delImg(this, '${waCarKndLVo.carKndNo }');" popYn="Y" /></td>
		<td class="bodybg_ct"><u:input id="cont" value="${waCarKndLVo.cont}" titleId="cols.cont" maxByte="300" style="width:92%;"/></td>
	</tr>
	</c:forEach>
	
</u:listArea>
<input type="hidden" id="delList" name="delList" />
</form>
</c:if>
<!-- 사이트맵 이미지 목록 -->
<c:if test="${mode eq 'img' }">
<c:if test="${viewFrm == true }"><div style="padding:10px;"></c:if>
<u:title titleId="st.jsp.site.title" menuNameFirst="true"/>
<u:set var="urlSuffix" test="${viewFrm == true }" value="Frm" elseValue=""/>
<form id="searchForm" name="searchForm" action="./listSite${urlSuffix}.do" >
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="schCat" value="siteNm" />
<% // 검색영역 %>
<u:searchArea>
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="st.cols.siteNm" alt="사이트명" /></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 400px;" onkeydown="if (event.keyCode == 13) searchForm.submit();" /></td>		
		<!-- 카테고리 -->
		<td class="width20"></td>
		<td class="search_tit"><u:msg titleId="cols.cat" alt="카테고리" /></td>
		<td><u:set var="paramCatId" test="${!empty paramCatId }" value="${paramCatId }" elseValue="${param.catId }" 
		/><select name="catId" onchange="searchForm.submit();">
			<u:option value="" titleId="cm.option.all" alt="전체" checkValue="${paramCatId}" />
			<c:forEach items="${stCatBVoList}" var="catVo" varStatus="status">
			<u:option value="${catVo.catId}" title="${catVo.catNm}" checkValue="${paramCatId}" />
			</c:forEach>
			</select></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
</u:searchArea>

</form>

<% // 앨범형 목록 %>
<c:if test="${fn:length(waCarKndLVoList) == 0}">
	<u:listArea id="listArea">
		<tr>
		<td class="nodata" ><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</u:listArea>
</c:if>
<c:if test="${fn:length(waCarKndLVoList) > 0}">

	<u:titleArea outerStyle="overflow:hidden;" innerStyle="padding:0 0 0 10px;">
		<c:forEach items="${waCarKndLVoList}" var="waCarKndLVo" varStatus="status">
		<c:set var="stSiteImgDVo" value="${waCarKndLVo.stSiteImgDVo}" />
		<c:set var="maxWdth" value="100" />
		<c:set var="maxHght" value="110" />
		<u:set test="${stSiteImgDVo != null}" var="imgPath" value="${_cxPth}${stSiteImgDVo.imgPath}" elseValue="${_cxPth}/images/${_skin}/photo_noimg.png" />
		<u:set test="${stSiteImgDVo != null && stSiteImgDVo.imgWdth <= maxWdth}" var="imgWdth" value="${stSiteImgDVo.imgWdth}" elseValue="${maxWdth}" />
		<u:set test="${stSiteImgDVo != null && stSiteImgDVo.imgHght <= maxHght}" var="imgHght" value="${stSiteImgDVo.imgHght}" elseValue="${maxHght}" />
		<u:set test="${stSiteImgDVo.imgWdth < stSiteImgDVo.imgHght}" var="imgWdthHgth" value="height='${imgHght}'" elseValue="width='${imgWdth}'" />
		<div class="listarea" style="float:left; width:12.2%; padding:8px 8px 0 0;">
		<table class="listtable" border="0" cellpadding="0" cellspacing="1"><tbody>
		<tr>
		<td class="photo_ct" title="${waCarKndLVo.cont }">
			<c:if test="${!empty stSiteImgDVo}">
				<a href="javascript:viewImagePop('${waCarKndLVo.carKndNo }');" ><img src="${imgPath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' ${imgWdthHgth}></a>
			</c:if>
			<c:if test="${empty stSiteImgDVo }"><img src="${imgPath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' ${imgWdthHgth}></c:if>
		</td>
		</tr>
		<tr>
		<td style="padding: 4px;">
			<table border="0" cellpadding="0" cellspacing="0" style="width:100%;table-layout:fixed;"><tbody>
			<tr>
			<td class="body_ct">
				<div class="ellipsis" title="${waCarKndLVo.siteNm}" >
				<c:if test="${!empty waCarKndLVo.siteUrl }"><a href="<u:menuUrl url="${waCarKndLVo.siteUrl}" />" target="_blank"><u:out value="${waCarKndLVo.siteNm}" /></a></c:if>
				<c:if test="${empty waCarKndLVo.siteUrl }"><u:out value="${waCarKndLVo.siteNm}" /></c:if>
				</div>
				</td>
			</tr>
			</tbody></table></td>
		</tr>
		</tbody></table>
		</div>
		
		</c:forEach>
	
		<u:blank />
	</u:titleArea>

</c:if>

<u:pagination />
<c:if test="${viewFrm == true }"></div></c:if>
</c:if>