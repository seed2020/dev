<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="paramsForList" excludes="workNo" />
<style type="text/css">
ul.img_list{list-style:none; padding:0; margin:0; }
ul.img_list li.list_content{margin:3px;padding:2px;border:1px solid #bfc8d2;width:19.2%;float:left;}
ul.img_list li.item_photo{border-bottom:1px solid #bfc8d2;line-height:125px;text-align: center;padding: 7px 0 7px 0;margin: 0 auto 0 auto;}
ul.img_list div.img_content{position:relative; height:125px; text-align:center;}
ul.img_list div.img_content img {position:absolute;max-width:100%; max-height:100%;margin:auto;top:0; bottom:0; left:0; right:0; }
ul.img_list li.item_title{color:#454545;padding:8px 3px 0 4px;height:22px;}
ul.img_list li.item_content{float:left;color:#454545;height:22px;padding:2px 3px 0 4px;width:45%;}
        
</style>
<script type="text/javascript">
<!--
<% //이미지 상세보기 - 팝업 오픈 %>
function viewImagePop(workNo, id){
	var url='./viewImagePop.do?menuId=${menuId}&formNo=${param.formNo}&workNo='+workNo;
	if(id!='') url+='&imgNo='+id;		
	parent.dialog.open('viewImageDialog','<u:msg titleId="st.cols.img" alt="이미지" />', url);
}
<%// 상세보기 %>
function viewWorks(workNo){
	var url="./viewWorks.do?${paramsForList}";
	url+="&workNo="+workNo;
	parent.location.href=url;
}
<% // [하단버튼:삭제] 삭제 %>
function delWorks() {
	var arr = getCheckedValue("listArea", "cm.msg.noSelect");<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
	if (arr != null && confirmMsg("cm.cfrm.del")) {<% // 삭제하시겠습니까? %>
		callAjax('./transWorksDelAjx.do?menuId=${menuId}', {formNo:'${param.formNo}', workNos:arr}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.href = '${_uri}?${paramsForList}';
			}
		});
	}
}
<% // [하단버튼:배열] %>
function getRightBtnList(){
	var $area = $("#rightBtnArea");
	return $area.find('ul')[0].outerHTML;
}<% // [목록:등록화면버튼] - 등록화면 구성으로 이동 %>
function setRegWorks(formNo){
	parent.location.href="./setWorks.do?${paramsForList}";
}<% // [목록:목록화면버튼] - 목록화면 구성으로 이동 %>
function setListForm(formNo){
	parent.location.href="./listWorks.do?${paramsForList}";
}<% // [팝업] 파일목록 조회 %>
function viewFileListPop(workNo) {
	var url = './viewFileListPop.do?${paramsForList }&workNo='+workNo;
	parent.dialog.open('viewFileListDialog','<u:msg titleId="cols.att" alt="첨부" />', url);
}<% // [목록:조회수] 조회이력 %>
function listReadHstPop(id) {
	dialog.open('listReadHstDialog','<u:msg titleId="bb.jsp.listReadHstPop.title" alt="조회자 정보" />','./listReadHstPop.do?menuId=${menuId}&formNo=${param.formNo}&workNo='+id);
}

$(document).ready(function() {
	$('#listArea tbody:first tr, #listArea #tr').find('a, input[type="checkbox"]').click(function(event){
		if(event.stopPropagation) event.stopPropagation(); //MOZILLA
		else event.cancelBubble = true; //IE
	});
	setUniformCSS();
	<c:if test="${!empty pageSuffix && pageSuffix eq 'Frm' }">parent.applyFormBtn();</c:if>
});
//-->
</script>

<u:set var="pageStyle" test="${!empty pageSuffix && pageSuffix eq 'Frm' }" value=" style=\"padding:10px;\"" elseValue=""/>

<div${pageStyle }>

<u:title title="${wfFormBVo.formNm }" alt="${wfFormBVo.formNm }" menuNameFirst="true" />

<!-- 검색 영역 -->
<jsp:include page="/WEB-INF/jsp/wf/works/listWorksSrch.jsp" />

<c:if test="${!empty lstDispList }"><u:convertJson var="jsonVa" value="${wfFormRegDVo.attrVa }" 
/><c:if test="${lstTypCd eq 'D' }"><div class="listarea" id="listArea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" id="table-1" style="table-layout:fixed;">
	<colgroup>
		<col width="3%"/>
		<c:forEach items="${lstDispList}" var="wfFormLstDVo" varStatus="status"><col width="${wfFormLstDVo.wdthPerc }"></c:forEach>
	</colgroup>
	<tr id="headerTr">
		<th class="head_ct"></th>
		<c:forEach var="wfFormLstDVo" items="${lstDispList }" varStatus="status">
		<th class="head_ct"><u:out value="${wfFormLstDVo.itemNm }"/></th>
		</c:forEach>
	</tr>
	<c:if test="${fn:length(mapList) == 0}">
		<tr>
		<td class="nodata" colspan="${fn:length(lstDispList)+1 }"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
	<c:forEach items="${mapList}" var="map" varStatus="listStatus"><c:set var="map" value="${map }" scope="request"/>
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" onclick="viewWorks('${map.workNo}');">
		<td class="bodybg_ct"><c:if test="${map.isChk ne 'N' }"><input type="checkbox" value="${map.workNo}"/></c:if></td>
		<c:forEach var="wfFormLstDVo" items="${lstDispList }" varStatus="status"
		><c:set var="colmId" value="${wfFormLstDVo.colmId }"
		/><c:set var="colmTyp" value="${wfFormLstDVo.colmTyp }"
		/><c:set var="jsonMap" value="${jsonVa[wfFormLstDVo.colmNm] }" scope="request"
		/><td class="body_${wfFormLstDVo.alnVa eq 'center' ? 'ct' : wfFormLstDVo.alnVa eq 'right' ? 'rt' : 'lt'}"
		><c:set var="dataVa" value="${map[wfFormLstDVo.colmNm] }"
		/><c:choose><c:when test="${colmTyp eq 'user' || colmTyp eq 'dept'}"
		><c:set var="dataVa" value="${map.cdListMap[wfFormLstDVo.colmNm] }"
		/><c:if test="${!empty dataVa }"><c:forEach var="codeVo" items="${dataVa }" varStatus="codeStatus"
		><c:if test="${!codeStatus.first }">, </c:if><c:if test="${colmTyp eq 'user' }"><a href="javascript:;" onclick="viewUserPop('${codeVo.cdVa}')">${codeVo.cdNm }</a></c:if
		><c:if test="${colmTyp ne 'user' }">${codeVo.cdNm }</c:if></c:forEach></c:if></c:when
		><c:when test="${colmTyp eq 'select' || colmTyp eq 'radio' || colmTyp eq 'checkbox'}"
		><c:if test="${empty jsonMap['chkTypCd'] }"><u:out value="${dataVa }"/></c:if
		><c:if test="${!empty jsonMap['chkTypCd'] }"><c:set var="dataVa" value="${map.cdListMap[wfFormLstDVo.colmNm] }"
		/><c:if test="${!empty dataVa }"><c:forEach var="codeVo" items="${dataVa }" varStatus="codeStatus"
		><c:if test="${!codeStatus.first }">, </c:if>${codeVo.cdNm }</c:forEach></c:if></c:if></c:when
		><c:when test="${colmTyp eq 'file'}"
		><c:if test="${map.fileCnt>0}"><a href="javascript:viewFileListPop('${map.workNo}');"><u:icon type="att" /></a></c:if></c:when
		><c:when test="${colmTyp eq 'image'}"
		><u:set var="viewSizeTyp" test="${!empty jsonMap['viewSizeTyp'] && jsonMap['viewSizeTyp'] eq '%' }" value="per" elseValue="px" 
		/><u:set var="maxWdth" test="${!empty jsonMap['viewWdth'] }" value="${jsonMap['viewWdth'] }" elseValue="88" 
		/><u:set var="maxHght" test="${!empty jsonMap['viewHeight'] }" value="${jsonMap['viewHeight'] }" elseValue="${viewSizeTyp eq 'px' ? '110' : '100' }" 
		/><c:set var="imgVo" value="${!empty map ? map.imgListMap[wfFormLstDVo.colmNm] : ''}"
		/><c:if test="${!empty imgVo}"><u:set test="${!empty imgVo.imgPath}" var="imgPath" value="${_cxPth}${imgVo.imgPath}" elseValue="${_cxPth}/images/${_skin}/photo_noimg.png" 
		/><c:if test="${viewSizeTyp eq 'px'}"><u:set test="${imgVo.imgWdth <= maxWdth}" var="imgWdth" value="${imgVo.imgWdth}" elseValue="${maxWdth}" 
		/><u:set test="${imgVo.imgHght <= maxHght}" var="imgHght" value="${imgVo.imgHght}" elseValue="${maxHght}" 
		/><u:set test="${imgVo.imgWdth < imgVo.imgHght}" var="imgWdthHgth" value="height='${imgHght}'" elseValue="width='${imgWdth}'" 
		/></c:if><c:if test="${viewSizeTyp eq 'per'}"
		><u:set test="${maxWdth < maxHght}" var="imgWdthHgth" value="height='${maxHght}%'" elseValue="width='${maxWdth}%'" 
		/></c:if></c:if><div class="image_profile"><dl><dd class="photo"><c:if test="${!empty imgVo}"><a href="javascript:viewImagePop('${map.workNo }', '${imgVo.imgNo }');" 
		><img src="${imgPath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' ${imgWdthHgth}
		></a></c:if><c:if test="${empty imgVo }"><img src="${_cxPth}/images/${_skin}/photo_noimg.png" width="88px"></c:if></dd></dl></div
		></c:when><c:when test="${colmTyp eq 'period'}"
		><u:convertMap var="periodVa1" srcId="map" attId="${wfFormLstDVo.colmNm }_1" 
		/><c:if test="${!empty periodVa1 }">${periodVa1 }</c:if
		> ~ <u:convertMap var="periodVa2" srcId="map" attId="${wfFormLstDVo.colmNm }_2" 
		/><c:if test="${!empty periodVa2 }">${periodVa2 }</c:if></c:when><c:when test="${colmTyp eq 'regrNm' || colmTyp eq 'modrNm'}"
		><a href="javascript:;" onclick="viewUserPop('${map[colmId]}')">${map[colmTyp]}</a></c:when
		><c:when test="${colmTyp eq 'regDt' || colmTyp eq 'modDt'}"
		><u:out value="${map[colmTyp] }" type="longdate"/></c:when
		><c:when test="${colmTyp eq 'editor'}"
		><div class="ellipsis" style="max-height:300px;"><u:out value="${dataVa }" type="value"/></div></c:when
		><c:when test="${colmTyp eq 'readCnt'}"
		><a href="javascript:;" onclick="listReadHstPop('${map.workNo}')"><u:out value="${dataVa }" /></a></c:when
		><c:otherwise><div class="ellipsis" title="${dataVa }">${dataVa }</div></c:otherwise>
		</c:choose></td></c:forEach>
	</tr>
	</c:forEach>
	
</table>
</div></c:if><c:if test="${lstTypCd eq 'I' }"
><% // 앨범형 목록 %>
<c:if test="${fn:length(mapList) == 0}">
	<u:listArea id="listArea">
		<tr>
		<td class="nodata" ><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</u:listArea>
</c:if>
<c:if test="${fn:length(mapList) > 0}">
	<c:set var="maxCnt" value="2"/>
	<u:titleArea outerStyle="overflow:hidden;" innerStyle="padding:0 0 0 10px;">
		<ul class="img_list" id="listArea">
			<c:forEach items="${mapList}" var="map" varStatus="listStatus"><c:set var="map" value="${map }" scope="request"
			/><c:set var="listCnt" value="0"/>
			<li id="tr" class="list_content" onclick="viewWorks('${map.workNo}');" style="cursor:pointer;">
				<div>
					<ul class="img_list"><c:forEach var="wfFormLstDVo" items="${lstDispList }" varStatus="status"
					><c:set var="colmId" value="${wfFormLstDVo.colmId }"
					/><c:set var="colmTyp" value="${wfFormLstDVo.colmTyp }"
					/><c:set var="jsonMap" value="${jsonVa[wfFormLstDVo.colmNm] }" scope="request"
					/><c:set var="dataVa" value="${map[wfFormLstDVo.colmNm] }"
					/><c:if test="${status.first}">
					<li class="item_photo" ><c:set var="maxWdth" value="100" />
					<c:set var="maxHght" value="100" 
					/><c:set var="imgVo" value="${!empty map ? map.imgListMap[wfFormLstDVo.colmNm] : ''}"
					/><div class="img_content"><c:if test="${!empty imgVo}"><u:set test="${!empty imgVo.imgPath}" var="imgPath" value="${_cxPth}${imgVo.imgPath}" elseValue="${_cxPth}/images/${_skin}/photo_noimg.png" 
					/><u:set test="${imgVo.imgWdth <= maxWdth}" var="imgWdth" value="${imgVo.imgWdth}" elseValue="${maxWdth}" 
					/><u:set test="${imgVo.imgHght <= maxHght}" var="imgHght" value="${imgVo.imgHght}" elseValue="${maxHght}" 
					/><u:set test="${imgVo.imgWdth < imgVo.imgHght}" var="imgWdthHgth" value="height='${imgHght}'" elseValue="width='${imgWdth}'" 
					/><a href="javascript:viewImagePop('${map.workNo }', '${imgVo.imgNo }');"><img src="${imgPath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' ${imgWdthHgth}
					></a></c:if><c:if test="${empty imgVo }"><img src="${_cxPth}/images/${_skin}/photo_noimg.png" width="88px"></c:if></div></li></c:if>
					<c:if test="${!status.first && colmTyp ne 'editor'}"
					><u:set var="titleYn" test="${status.index==1 }" value="Y" elseValue="N"
					/><u:set var="itemNm" test="${titleYn ne 'Y' }" value="${wfFormLstDVo.itemNm }" elseValue=""
					/><u:set var="contIcon" test="${listCnt>0 && (listCnt < maxCnt || listCnt%maxCnt>0)}" value="|" elseValue=""/><c:if test="${titleYn eq 'N' }"><c:set var="listCnt" value="${listCnt+1 }"
					/></c:if><li class="${titleYn eq 'Y' ? 'item_title' : 'item_content'}">
					<c:choose><c:when test="${colmTyp eq 'user' || colmTyp eq 'dept'}"
					><c:set var="dataVa" value="${map.cdListMap[wfFormLstDVo.colmNm] }"
					/><c:if test="${!empty dataVa }">${contIcon} ${itemNm} <c:forEach var="codeVo" items="${dataVa }" varStatus="codeStatus"
					><c:if test="${!codeStatus.first }">, </c:if><c:if test="${colmTyp eq 'user' }"><a href="javascript:;" onclick="viewUserPop('${codeVo.cdVa}')">${codeVo.cdNm }</a></c:if
					><c:if test="${colmTyp ne 'user' }">${codeVo.cdNm }</c:if></c:forEach></c:if></c:when
					><c:when test="${colmTyp eq 'select' || colmTyp eq 'radio' || colmTyp eq 'checkbox'}"
					><c:if test="${empty jsonMap['chkTypCd'] }">${contIcon} ${itemNm} <u:out value="${dataVa }"/></c:if
					><c:if test="${!empty jsonMap['chkTypCd'] }"><c:set var="dataVa" value="${map.cdListMap[wfFormLstDVo.colmNm] }"
					/><c:if test="${!empty dataVa }"><c:forEach var="codeVo" items="${dataVa }" varStatus="codeStatus"
					><c:if test="${!codeStatus.first }">, </c:if>${codeVo.cdNm }</c:forEach></c:if></c:if></c:when
					><c:when test="${colmTyp eq 'file'}"
					>${contIcon} ${itemNm} <c:if test="${map.fileCnt>0}"><a href="javascript:viewFileListPop('${map.workNo}');"><u:icon type="att" /></a></c:if></c:when
					><c:when test="${component['coltyp'] eq 'radioSingle' || component['coltyp'] eq 'checkboxSingle' }"
					><c:if test="${!empty jsonMap['singleId'] && !empty map[jsonMap['singleId']]}"
					><c:set var="chkList" value="${map[jsonMap['singleId']] }" 
					/><c:forTokens var="chkOpt" items="${chkList }" delims="," varStatus="chkStatus"
					><c:if test="${chkOpt == componentId }"><c:set var="langDispNameId" value="dispNameRescVa_${_lang }" scope="request"
					/><u:set var="langDispNameTitle" test="${!empty jsonMap[langDispNameId] }" value="${jsonMap[langDispNameId] }" elseValue="${jsonMap['dispName'] }"
					/>${langDispNameTitle }</c:if></c:forTokens></c:if>
					</c:when><c:when test="${colmTyp eq 'image'}"
					><c:set var="maxWdth" value="100" />
					<c:set var="maxHght" value="100"
					/><c:set var="imgVo" value="${!empty map ? map.imgListMap[wfFormLstDVo.colmNm] : ''}"
					/><c:if test="${!empty imgVo}"><u:set test="${!empty imgVo.imgPath}" var="imgPath" value="${_cxPth}${imgVo.imgPath}" elseValue="${_cxPth}/images/${_skin}/photo_noimg.png"
					/><u:set test="${imgVo.imgWdth <= maxWdth}" var="imgWdth" value="${imgVo.imgWdth}" elseValue="${maxWdth}"
					/><u:set test="${imgVo.imgHght <= maxHght}" var="imgHght" value="${imgVo.imgHght}" elseValue="${maxHght}"
					/><u:set test="${imgVo.imgWdth < imgVo.imgHght}" var="imgWdthHgth" value="height='${imgHght}'" elseValue="width='${imgWdth}'"
					/><a href="javascript:viewImagePop('${map.workNo }', '${imgVo.imgNo }');"
					><img src="${imgPath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' ${imgWdthHgth}
					></a></c:if><c:if test="${empty imgVo }"><img src="${_cxPth}/images/${_skin}/photo_noimg.png" width="88px"></c:if></c:when><c:when test="${colmTyp eq 'period'}"
					><u:convertMap var="periodVa1" srcId="map" attId="${wfFormLstDVo.colmNm }_1"
					/><c:if test="${!empty periodVa1 }">${periodVa1 }</c:if
					> ~ <u:convertMap var="periodVa2" srcId="map" attId="${wfFormLstDVo.colmNm }_2"
					/><c:if test="${!empty periodVa2 }">${periodVa2 }</c:if></c:when><c:when test="${colmTyp eq 'regrNm' || colmTyp eq 'modrNm'}"
					><a href="javascript:;" onclick="viewUserPop('${map[colmId]}')">${map[colmTyp]}</a></c:when
					><c:when test="${colmTyp eq 'regDt' || colmTyp eq 'modDt'}"
					><u:out value="${map[colmTyp] }" type="longdate"/></c:when
					><c:when test="${colmTyp eq 'editor'}"
					><div class="ellipsis" style="max-height:300px;">${contIcon} ${itemNm} <u:out value="${dataVa }" type="value"/></div></c:when
					><c:when test="${colmTyp eq 'readCnt'}"
					><a href="javascript:;" onclick="listReadHstPop('${map.workNo}')">${contIcon} ${itemNm} <u:out value="${dataVa }" /></a></c:when
					><c:otherwise><div class="ellipsis" title="${dataVa }">${contIcon} ${itemNm} ${dataVa }</div></c:otherwise>
					</c:choose>
					</li></c:if>
					</c:forEach>
					</ul>
				</div>
			</li></c:forEach>
 		</ul>
		<u:blank />
	</u:titleArea>
  
</c:if>

</c:if>
<u:blank />
<u:pagination />
</c:if>
</div>

<u:set var="btnDisplay" test="${!empty pageSuffix && pageSuffix eq 'Frm' }" value="display:none;" elseValue=""/>
<% // 하단 버튼 %>
<u:buttonArea id="rightBtnArea" style="${btnDisplay }" topBlank="true">
<c:if test="${!empty lstDispList }">
	<u:button id="btnReg" href="javascript:setRegWorks();" titleId="cm.btn.write" alt="등록" auth="A" />
	<c:if test="${lstTypCd eq 'D' }"><u:button id="btnDel" href="javascript:delWorks();" titleId="cm.btn.del" alt="삭제" auth="A" /></c:if>
	</c:if>
</u:buttonArea>
