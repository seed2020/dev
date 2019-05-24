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
	$("#siteListArea tbody:first input[type='checkbox']:checked").each(function(){
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
var gMaxRow = parseInt('${fn:length(stSiteBVoList)}');
<%// 행추가%>
function addRow(){
	var $tr = $("#siteListArea tbody:first #hiddenTr");
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
	var $tr = $("#siteListArea tbody:first #hiddenTr").prev();
	delRowInArr([$tr[0]]);
}
<%// 선택삭제%>
function delSelRow(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) delRowInArr(arr);
}
<%// 삭제 - 배열에 담긴 목록%>
function delRowInArr(rowArr){
	var delVa = $("#delList").val(), delArr = [], $siteId;
	if(delVa!='') delArr.push(delVa);
	for(var i=0;i<rowArr.length;i++){
		$siteId = $(rowArr[i]).find("input[name='siteId']");
		if($siteId.val()!=''){
			delArr.push($siteId.val());
		}
		$(rowArr[i]).remove();
	}
	$("#delList").val(delArr.join(','));
}
<%
//- 해당 어권의 명이 입력되지 않았 을 경우 해당 어권을 보이게 함 %>
function changeLangSelectorSite(areaId, id, va){
	if(va==''){
		var langSelector = $('#'+areaId+' #langSelector'+id.substring(id.lastIndexOf('_')));
		var nm = $("#siteListArea #"+id).attr("name");
		langSelector.val(nm.substring(nm.length-2));
		langSelector.trigger('click');
	}
}
<%// 저장%>
function saveSite(){
	<%// trArr : 저장할 곳의 tr 테그 배열 %>
	var trArr=[], count=0, result;
	$("#siteListArea tbody:first").children().each(function(){
		trArr.push(this);
	});
	
	<%// 어권별 리소스가 있는지 체크함%>
	for(var i=1;i<trArr.length-1;i++){
		result = checkRescVa(trArr[i], true);
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
	$("#siteListArea input[name='sortOrdr']").each(function(){
		if($(this).attr('id')!='sortOrdr_NO'){
			$(this).val(ordr++);
		}
	});
	<%// disable 된 select disable 해제 %>
	var arr = releaseDisable($('#siteListForm select'));
	
	<%// 서버 전송%>
	var $form = $("#siteListForm");
	$form.attr('method','post');
	$form.attr('action','./transSiteList.do');
	$form.attr('target','dataframeForFrame');
	$form.submit();
	
	<%// select disable 다시 적용 %>
	unreleaseDisable(arr);
}

<%//어권별 리소스 input에 값이 있는지 체크%>
function checkRescVa(trObj, ignoreAllEmpty){
	var i, id, va, handler, arr=[];
	$(trObj).find("td[id^='langTyp'] input").each(function(){ arr.push(this); });
	
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
}
<%// 일괄 언어 선택 변경%>
function changeLangTypCds(areaId){
	var $area = $("#"+areaId);
	<%// 변경할 언어 구하기 - 첫번째 언어 선택 select 의 다음 값 %>
	var langSel = $area.find("select[id^='langSelector']:first");
	if(langSel.length==0) return;
	var index = langSel[0].selectedIndex + 1;
	if(index>=langSel[0].options.length) index = 0;
	var langCd = langSel[0].options[index].value;
	var rescNm;
	<%// input - 어권에 맞게 show/hide %>
	$area.find("td[id^='langTyp'] input").each(function(){
		rescNm = $(this).attr('name');
		if(rescNm!=null && rescNm.endsWith(langCd)){
			$(this).show();
		} else { $(this).hide(); }
	});
	<%// select 변경%>
	var selectors = $area.find("select[id^='langSelector']");
	selectors.val(langCd);
	selectors.uniform.update();
}<%// 체크된 목록 리턴 - 잘라내기용 %>
function getCheckedArray(){
	var arr = [], va;
	$("#siteListArea input[type=checkbox]:checked").each(function(){
		va = $(this).val();
		if(va!=null && va!='') { arr.push(va); }
	});
	return arr.length==0 ? null : arr;
}<% //이미지 변경 - 팝업 오픈 %>
function setImagePop(obj, id){
	imgObj=obj;
	var url='./setImagePop.do?menuId=${menuId}';
	if(id!='') url+='&siteId='+id;
	parent.dialog.open('setImageDialog','<u:msg titleId="st.jsp.select.img" alt="이미지 선택" />', url);
}<% //이미지 상세보기 - 팝업 오픈 %>
function viewImagePop(id){
	var url='/cm/site/viewImagePop.do?menuId=${menuId}';
	if(id!='') url+='&siteId='+id;	
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
}<% //내용 변경 - 팝업 오픈 %>
function setEtcPop(obj, id){
	etcObj=obj;
	var url='./setEtcPop.do?menuId=${menuId}';
	if(id!='') url+='&siteId='+id;
	parent.dialog.open('setEtcDialog','<u:msg titleId="cols.cont" alt="내용" />', url);
}<% //내용 삽입 %>
function setSiteCont(arr){
	if(etcObj==null) return;
	$tr = $(etcObj).parents('tr');
	if(arr==undefined || arr.cont==''){
		$tr.find('input[name="cont"]').val('');
		return;
	}
	$tr.find('input[name="cont"]').val(arr.cont);
}<% //내용 가져오기 %>
function getSiteCont(){
	if(etcObj==null) return;
	$tr = $(etcObj).parents('tr');
	return $tr.find('input[name="cont"]').val();
}
<% //이미지 삭제 %>
function delImg(obj, id){
	if(id!=''){
		callAjax('./transSiteImgDelAjx.do?menuId=${menuId}', {siteId:id}, function(data) {
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
	$("#siteListArea tbody:first").children().each(function(){
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
<input type="hidden" name="catId" value="${param.catId}" />
<u:listArea id="siteListArea" >

	<tr id="headerTr">
		<th width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('siteListArea', this.checked);" value=""/></th>
		<th width="35%" class="head_ct"><u:msg titleId="st.cols.siteNm" alt="사이트명" /></th>
		<th class="head_ct"><u:msg titleId="st.cols.siteUrl" alt="사이트URL" /></th>
		<th width="15%" class="head_ct"><u:msg titleId="st.cols.img" alt="이미지 " /></th>
		<th width="10%" class="head_ct"><u:msg titleId="cols.note" alt="비고 " /></th>
	</tr>
	<c:forEach items="${stSiteBVoList}" var="stSiteBVo" varStatus="status"
			 ><u:set test="${status.last}" var="trDisp" value="display:none"
			/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${stSiteBVo.siteId}"
			/><u:set test="${status.last}" var="siteIndex" value="_NO" elseValue="_${status.index+1}"
			/>			 
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="${trId}" style="${trDisp}" >
		<td class="bodybg_ct"><input type="checkbox" value="${stSiteBVo.siteId}"/>
			<u:input type="hidden" id="sortOrdr${siteIndex}" name="sortOrdr" />
			<u:input type="hidden" id="valid${siteIndex}" name="valid" />
			<u:input type="hidden" id="siteId${siteIndex}" name="siteId" value="${stSiteBVo.siteId }"/></td>
		<td><table cellspacing="0" cellpadding="0" border="0">
			<tr>
			<td id="langTyp${siteIndex}">
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="langStatus">
			<u:convert srcId="${stSiteBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
			<u:set test="${langStatus.first}" var="style" value="" elseValue="display:none;" />
			<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
			<u:input id="rescVa_${langTypCdVo.cd}${siteIndex}" name="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="st.cols.siteNm" value="${rescVa}" style="${style}"
				maxByte="200" validator="changeLangSelectorSite('siteListArea', id, va)" mandatory="Y" />
			</c:forEach>
			</td>
			<td>
			<c:if test="${fn:length(_langTypCdListByCompId)>1}">
				<select id="langSelector${siteIndex}" onchange="changeLangTypCd('siteListArea','langTyp${siteIndex}', this.value)" <u:elemTitle titleId="cols.langTyp" />>
				<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="langStatus">
				<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
				</c:forEach>
				</select>
			</c:if>
			<u:input type="hidden" id="rescId${siteIndex}" name="rescId" value="${stSiteBVo.rescId}" />
			<u:input type="hidden" id="tmpImgId${siteIndex}" name="tmpImgId" value="" />
			<u:input type="hidden" id="cont${siteIndex}" name="cont" value="${stSiteBVo.cont }" />
			</td>
			</tr>
			</table>
		
		</td>
		<td class="bodybg_ct"><u:input id="siteUrl" value="${stSiteBVo.siteUrl}" titleId="st.cols.siteUrl" style="width:95.5%;" maxByte="240"/></td>
		<td class="bodybg_ct"><u:buttonS alt="변경" titleId="cm.btn.chg" onclick="setImagePop(this, '${stSiteBVo.siteId }');" popYn="Y" />
		<u:buttonS alt="삭제" titleId="cm.btn.del" onclick="delImg(this, '${stSiteBVo.siteId }');" popYn="Y" /></td>
		<td class="bodybg_ct"><u:buttonS alt="내용" titleId="cols.cont" onclick="setEtcPop(this, '${stSiteBVo.siteId }');" popYn="Y" /></td>
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
<c:if test="${fn:length(stSiteBVoList) == 0}">
	<u:listArea id="listArea">
		<tr>
		<td class="nodata" ><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</u:listArea>
</c:if>
<c:if test="${fn:length(stSiteBVoList) > 0}">

	<u:titleArea outerStyle="overflow:hidden;" innerStyle="padding:0 0 0 10px;">
		<c:forEach items="${stSiteBVoList}" var="stSiteBVo" varStatus="status">
		<c:set var="stSiteImgDVo" value="${stSiteBVo.stSiteImgDVo}" />
		<c:set var="maxWdth" value="100" />
		<c:set var="maxHght" value="110" />
		<u:set test="${stSiteImgDVo != null}" var="imgPath" value="${_cxPth}${stSiteImgDVo.imgPath}" elseValue="${_cxPth}/images/${_skin}/photo_noimg.png" />
		<u:set test="${stSiteImgDVo != null && stSiteImgDVo.imgWdth <= maxWdth}" var="imgWdth" value="${stSiteImgDVo.imgWdth}" elseValue="${maxWdth}" />
		<u:set test="${stSiteImgDVo != null && stSiteImgDVo.imgHght <= maxHght}" var="imgHght" value="${stSiteImgDVo.imgHght}" elseValue="${maxHght}" />
		<u:set test="${stSiteImgDVo.imgWdth < stSiteImgDVo.imgHght}" var="imgWdthHgth" value="height='${imgHght}'" elseValue="width='${imgWdth}'" />
		<div class="listarea" style="float:left; width:12.2%; padding:8px 8px 0 0;">
		<table class="listtable" border="0" cellpadding="0" cellspacing="1"><tbody>
		<tr>
		<td class="photo_ct" title="${stSiteBVo.cont }">
			<c:if test="${!empty stSiteImgDVo}">
				<a href="javascript:viewImagePop('${stSiteBVo.siteId }');" ><img src="${imgPath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' ${imgWdthHgth}></a>
			</c:if>
			<c:if test="${empty stSiteImgDVo }"><img src="${imgPath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' ${imgWdthHgth}></c:if>
		</td>
		</tr>
		<tr>
		<td style="padding: 4px;">
			<table border="0" cellpadding="0" cellspacing="0" style="width:100%;table-layout:fixed;"><tbody>
			<tr>
			<td class="body_ct">
				<div class="ellipsis" title="${stSiteBVo.siteNm}" >
				<c:if test="${!empty stSiteBVo.siteUrl }"><a href="<u:menuUrl url="${stSiteBVo.siteUrl}" />" target="_blank"><u:out value="${stSiteBVo.siteNm}" /></a></c:if>
				<c:if test="${empty stSiteBVo.siteUrl }"><u:out value="${stSiteBVo.siteNm}" /></c:if>
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