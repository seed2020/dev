<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<!-- 컴포넌트 -->
<u:set var="loutList" test="${!empty wfWorksLVoMap && !empty wfWorksLVoMap.loutVa}" value="${wfWorksLVoMap.loutVa }" elseValue="${wfFormRegDVo.loutVa }"/>
<c:if test="${!empty loutList }">
<u:convertJson var="dataToJson" value="${wfWorksLVoMap.jsonVa }"
/><u:convertJson var="jsonLoutList" value="${loutList }"
/><u:convertJson var="jsonVa" value="${wfFormRegDVo.attrVa }" 
/><c:forEach items="${jsonLoutList}" var="json" varStatus="status">
<div class="loutFormArea" id="${idPrefix }${json['loutId'] }" <c:if test="${status.index>0 }">style="display:none;"</c:if>>
<c:forEach items="${json['list']}" var="list" varStatus="listStatus"><c:if test="${list['type'] eq 'table' }"></c:if>
<div id="itemsArea" data-id="${list['type'] }"><!-- itemsArea -->

<c:if test="${list['type'] eq 'table' }">
<c:if test="${(!empty list['title'] || (!empty list['loutTyp'] && list['loutTyp'] eq 'V'))}"><u:title title="${list['title'] }" type="small" alt="${list['title'] }" notPrint="true" /></c:if>
<div class="listarea" id="itemsViewArea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
	<c:if test="${!empty list['colgroup'] }">
	<colgroup>
		<c:forEach items="${list['colgroup']}" var="col" varStatus="colStatus"><col width="${col }"></c:forEach>
	</colgroup>
	</c:if>
	<tbody style="border:0">
	<c:if test="${!empty list['row'] }">
		<c:forEach items="${list['row']}" var="row" varStatus="rowStatus">
			<tr id="${row['id'] }" <c:if test="${row['id']=='hiddenTr' }">style="display:none;"</c:if>>
				<c:if test="${!empty row['cell'] }">
					<c:forEach items="${row['cell']}" var="cell" varStatus="cellStatus">
						<td class="${cell['clsnm'] }"<c:if test="${!empty cell['colspan'] && cell['colspan']>1}"> colspan="${cell['colspan'] }"</c:if
						><c:if test="${!empty cell['rowspan'] && cell['rowspan']>1}"> rowspan="${cell['rowspan'] }"</c:if>>
							<c:if test="${!empty cell['components'] }">
								<c:forEach items="${cell['components']}" var="component" varStatus="componentStatus"
								><c:set var="colmTyp" value="${component['coltyp'] }"
								/>	<c:set var="componentId" value="${component['id']}"/>
									<c:set var="dataVa" value="${dataToJson[componentId] }" />
									<c:set var="jsonMap" value="${!empty component['refId'] ? jsonVa[component['refId']] : jsonVa[componentId] }" scope="request"
									/><u:set var="emptyLangId" test="${component['coltyp'] eq 'label' }" value="label" elseValue="name"
									/><c:set var="langTitleId" value="${component['coltyp'] }RescVa_${_lang }" scope="request"
									/><u:set var="langTitle" test="${!empty jsonMap[langTitleId] }" value="${jsonMap[langTitleId] }" elseValue="${jsonMap[emptyLangId] }"
									/><div class="component_list" id="${componentId }Area" data-id="${componentId }" data-coltyp="${colmTyp }">
									<c:if test="${isEdit eq 'Y' && colmTyp eq 'number' }"><div class="tooltip_area" style="display:none;">${componentId }</div></c:if>
									<div<c:if test="${colmTyp eq 'label' && !empty jsonMap['labelAlign'] }"> style="text-align:${jsonMap['labelAlign'] }"</c:if>>
									<c:if test="${colmTyp eq 'label' }"
									><c:if test="${!empty jsonMap['required'] && jsonMap['required']=='Y' }"></c:if><label for="${jsonVa[componentId] }" class="header">${langTitle }</label></c:if
									><c:if test="${colmTyp eq 'text' }"
									><u:out value="${dataVa }"/></c:if><c:if test="${colmTyp eq 'textarea' }"
									><u:out value="${dataVa }"/></c:if><c:if test="${colmTyp eq 'editor' }"
									><u:out value="${dataVa }" type="value"/></c:if><c:if test="${colmTyp eq 'date' }"
									><u:out value="${dataVa }" type="date" /></c:if><c:if test="${colmTyp eq 'period' }"
									><u:convertMap var="periodVa1" srcId="dataToJson" attId="${componentId }-1" 
									/><c:if test="${!empty periodVa1 }">${periodVa1 }</c:if
									> ~ <u:convertMap var="periodVa2" srcId="dataToJson" attId="${componentId }-2" 
									/><c:if test="${!empty periodVa2 }">${periodVa2 }</c:if></c:if><c:if test="${colmTyp eq 'time' }"
									><u:out value="${dataVa }"/></c:if><c:if test="${colmTyp eq 'datetime' }"
									><u:out value="${dataVa }"/></c:if><c:if test="${colmTyp eq 'number' || colmTyp eq 'calculate'}"
									><u:out value="${dataVa }"/></c:if><c:if test="${colmTyp eq 'user' || colmTyp eq 'dept' || colmTyp eq 'code' }"
									><c:set var="dataVa" value="${wfWorksLVoMap.cdListMap[componentId] }"
									/><c:if test="${!empty dataVa }"><c:forEach var="codeVo" items="${dataVa }" varStatus="codeStatus"
									><c:if test="${!codeStatus.first }">, </c:if><c:if test="${colmTyp eq 'user' }"><a href="javascript:;" onclick="viewUserPopOpen('${codeVo.cdVa}')">${codeVo.cdNm }</a></c:if
									><c:if test="${colmTyp ne 'user' }">${codeVo.cdNm }</c:if></c:forEach></c:if></c:if
									><c:if test="${colmTyp eq 'file' }"
									><a href="javascript:void(0)" onclick="viewFileListPop('${param.workNo}');"><u:msg titleId="cols.attFile" alt="파일목록보기"/></a></c:if
									><c:if test="${colmTyp eq 'checkbox' || colmTyp eq 'select' || colmTyp eq 'radio'}"
									><c:if test="${empty jsonMap['chkTypCd'] }"><u:out value="${dataVa }"/></c:if
									><c:if test="${!empty jsonMap['chkTypCd'] }"><c:set var="dataVa" value="${wfWorksLVoMap.cdListMap[componentId] }"
									/><c:if test="${!empty dataVa }"><c:forEach var="codeVo" items="${dataVa }" varStatus="codeStatus"
									><c:if test="${!codeStatus.first }">, </c:if>${codeVo.cdNm }</c:forEach></c:if></c:if
									></c:if><c:if test="${colmTyp eq 'image' }"
									><u:set var="viewSizeTyp" test="${!empty jsonMap['viewSizeTyp'] && jsonMap['viewSizeTyp'] eq '%' }" value="per" elseValue="px" 
									/><u:set var="maxWdth" test="${!empty jsonMap['viewWdth'] }" value="${jsonMap['viewWdth'] }" elseValue="88" 
									/><u:set var="maxHght" test="${!empty jsonMap['viewHeight'] }" value="${jsonMap['viewHeight'] }" elseValue="${viewSizeTyp eq 'px' ? '110' : '100' }" 
									/><c:set var="imgVo" value="${!empty wfWorksLVoMap ? wfWorksLVoMap.imgListMap[componentId] : ''}"
									/><c:if test="${!empty imgVo}"><u:set test="${!empty imgVo.imgPath}" var="imgPath" value="${_cxPth}${imgVo.imgPath}" elseValue="${_cxPth}/images/${_skin}/photo_noimg.png" 
									/><c:if test="${viewSizeTyp eq 'px'}"><u:set test="${imgVo.imgWdth <= maxWdth}" var="imgWdth" value="${imgVo.imgWdth}" elseValue="${maxWdth}" 
									/><u:set test="${imgVo.imgHght <= maxHght}" var="imgHght" value="${imgVo.imgHght}" elseValue="${maxHght}" 
									/><u:set test="${imgVo.imgWdth < imgVo.imgHght}" var="imgWdthHgth" value="height='${imgHght}'" elseValue="width='${imgWdth}'" 
									/></c:if><c:if test="${viewSizeTyp eq 'per'}"
									><u:set test="${maxWdth < maxHght}" var="imgWdthHgth" value="height='${maxHght}%'" elseValue="width='${maxWdth}%'" 
									/></c:if></c:if><div class="image_profile"><dl><dd class="photo"><c:if test="${!empty imgVo}"><a href="javascript:viewImagePop('${imgVo.imgNo }');" 
									><img src="${imgPath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' ${imgWdthHgth}
									></a></c:if><c:if test="${empty imgVo }"><img src="${_cxPth}/images/${_skin}/photo_noimg.png" width="88px"></c:if></dd></dl></div
									></c:if>
									</div></div>
								</c:forEach>
							</c:if>
						</td>
					</c:forEach>
				</c:if>
			</tr>
		</c:forEach>
	</c:if>
	</tbody>
	</table>
</div>
<u:blank />
</c:if>
<c:set var="jsonMap" value="${jsonVa[list['jsonId']] }" scope="request"/>
<c:if test="${list['type'] eq 'file' && !empty fileVoList}"><div class="listarea"><table class="listtable" border="0" cellpadding="0" cellspacing="1"><tr><td></td></tr></table></div
></c:if><c:if test="${list['type'] eq 'gap' }">
<div class="blank"></div>
</c:if><c:if test="${list['type'] eq 'line' }">
<div class="line"></div>
</c:if><c:if test="${list['type'] eq 'editor' }"><div style="overflow:auto;" class="editor printNoScroll" id="snsCont"><u:out value="${dataToJson[list['jsonId']] }" type="value"/></div></c:if>
</div><!-- itemArea -->
</c:forEach>
</div>

</c:forEach>
</c:if>