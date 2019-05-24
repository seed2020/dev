<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="paramsForList" excludes="workNo,noCache"/>
<script type="text/javascript">
//<![CDATA[
<c:if test="${mobRegTypCd ne 'U' }">           
<% // [버튼:수정] %>
function setWorks(workNo){
	$m.nav.next(null,'/wf/works/${setPage}.do?${paramsForList }&workNo='+workNo);
}<% // 파일 다운로드 %>
function downFile(id) {
	var ids = [];
	ids.push(id);
	var $form = $('<form>', {
			'method':'get',
			'action':'/wf/works/downFile.do',
			'target':$m.browser.mobile && $m.browser.safari ? '' : 'dataframe'
		}).append($('<input>', {
			'name':'menuId',
			'value':'${menuId}',
			'type':'hidden'
		})).append($('<input>', {
			'name':'formNo',
			'value':'${param.formNo}',
			'type':'hidden'
		})).append($('<input>', {
			'name':'workNo',
			'value':'${param.workNo}',
			'type':'hidden'
		})).append($('<input>', {
			'name':'fileIds',
			'value':ids,
			'type':'hidden'
		}));
	
	$(top.document.body).append($form);
	$m.secu.set();
	$form.submit();
	$form.remove();
}
</c:if>
<c:if test="${mobRegTypCd eq 'U' }">
<% // [등록:상단버튼] - 웹버전 등록화면 팝업 출력 %>
function setWorksWeb() {
	var param={};
	param['formNo']='${param.formNo}';
	param['mode']='set';
	param['workNo']='${param.workNo}';
	$m.ajax('/wf/works/getWorksUrlAjx.do?menuId=${menuId}', param, function(data) {
		if(data.message !=null){
			$m.dialog.alert(data.message);
		}
		if(data.webUrl!=null) {
			window.open(data.webUrl, "setWorksWin");
		}
	});
}<% // [새로고침] - 팝업 등록화면에서 저장후 새로고침 %>
function reloadOpen(){
	$m.nav.reload();
}
</c:if>
<% // [버튼:삭제] 삭제 %>
function delWorks() {
	$m.dialog.confirm('<u:msg titleId="cm.cfrm.del" alt="삭제하시겠습니까?"  />', function(result){
		if(result){
			$m.ajax('/wf/works/transWorksDelAjx.do?menuId=${menuId}', {formNo:'${param.formNo}', workNos:['${param.workNo}']}, function(data) {
				if(data.message !=null){
					$m.dialog.alert(data.message);
				}
				if(data.result == 'ok') {			
					$m.nav.curr(null, '/wf/works/listWorks.do?${paramsForList}');
				}
			});
		}
	});
}<% // [버튼:목록] %>
function listWorks(){
	$m.nav.prev(null, $('#listPage').val());
}<% // 에디터 보기/숨기기 %>
function openArea(id){
	if($('#'+id).css("display") == "none"){
		$('#'+id).show();
	}else{
		$('#'+id).hide();
	}
}
<c:if test="${mobRegTypCd eq 'D' }">
<% // [팝업] - 웹버전 보기 %>
function viewWorksWeb(){
	var param={};
	param['formNo']='${param.formNo}';
	param['workNo']='${param.workNo}';
	$m.ajax('/wf/works/getWorksUrlAjx.do?menuId=${menuId}', param, function(data) {
		if(data.message !=null){
			$m.dialog.alert(data.message);
		}
		if(data.webUrl!=null) {
			var url=data.webUrl;
			url+=url.indexOf('?') > -1 ? "&" : "?";
			url+="isMobile=Y";
			window.open(url, "viewWorksWin");
		}
	});
}
</c:if>

$(document).ready(function() {
	<%// 본문의 넓이를 맞춤 %>
	$layout.adjustBodyHtml('bodyHtmlArea');
	<c:if test="${mobRegTypCd ne 'U' }">
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('tabview');
	</c:if>
	//window.setTimeout("$('#viewApDocBtn').trigger('click')", 600);
});
//]]>
</script>
<u:secu auth="W">	
<div class="btnarea">
    <div class="size">
        <dl>
        	<c:if test="${mobRegTypCd eq 'D' }"><dd class="btn" onclick="viewWorksWeb();"><u:msg titleId="wf.btn.web.view" alt="웹버전 보기" /></dd></c:if>
        	<c:if test="${wfWorksLVoMap.isChk ne 'N' }">
        	<c:if test="${mobRegTypCd ne 'U' }"><dd class="btn" onclick="setWorks('${param.workNo}');"><u:msg titleId="cm.btn.mod" alt="수정" /></dd></c:if>
        	<c:if test="${mobRegTypCd eq 'U' }"><dd class="btn" onclick="setWorksWeb();"><u:msg titleId="cm.btn.mod" alt="수정" /></dd></c:if>
        	<dd class="btn" onclick="delWorks();"><u:msg titleId="cm.btn.del" alt="삭제" /></dd></c:if>
        	<dd class="btn" onclick="listWorks();"><u:msg titleId="cm.btn.list" alt="목록" /></dd>           	 
     </dl>
    </div>
</div>
</u:secu>
         
<section id="section">	
<c:if test="${mobRegTypCd ne 'U' }">
<jsp:include page="/WEB-INF/jsp/wf/works/inclTab.jsp" flush="false" >
<jsp:param value="${wfWorksLVoMap.mobTabVa }" name="tabList"/>
<jsp:param value="view" name="page"/>
</jsp:include><!-- 탭 -->

    <div class="blankzone">
        <div class="blank20"></div>
    </div>
    
	<m:input type="hidden" id="menuId" value="${menuId}" />
	<m:input type="hidden" id="listPage" value="/wf/works/${listPage}.do?${paramsForList}" />
	<m:input type="hidden" id="viewPage" value="/wf/works/${viewPage}.do?${params}" />
	<m:input type="hidden" id="genId" value="${wfFormBVo.genId }" />
	<m:input type="hidden" id="formNo" value="${param.formNo }" />
	<c:if test="${!empty param.workNo }"><m:input type="hidden" id="workNo" value="${param.workNo }" /></c:if>
	<u:convertJson var="dataToJson" value="${wfWorksLVoMap.jsonVa }"
	/><u:convertJson var="jsonVa" value="${wfFormRegDVo.attrVa }" 
	/><u:set var="loutList" test="${!empty param.workNo && !empty wfWorksLVoMap.mobLoutVa}" value="${wfWorksLVoMap.mobLoutVa }" elseValue="${wfFormMobDVo.loutVa }"
	/><u:convertJson var="loutVa" value="${loutList }" 
	/><c:set var="loutList" value="${loutVa['loutList'] }"
	/><div id="tabViewArea"
	><c:forEach items="${loutList}" var="loutVo" varStatus="status" 
	><div class="s_tablearea" id="${loutVo['loutId'] }" <c:if test="${status.index>0 }">style="display:none;"</c:if>>
			<c:set var="isEndYn" value="N"/>
            <div class="entryzone" ><div class="entryarea">
        <dl><c:forEach items="${loutVo['list']}" var="row" varStatus="rowStatus" 
            ><c:set var="colmVo" value="${colmListMap[row.colmNm]}"
	        /><c:set var="componentId" value="${colmVo.colmNm}"
	        /><c:set var="colmTyp" value="${colmVo.colmTypCd }" scope="request" 
	        /><c:set var="itemNm" value="${colmVo.itemNm }" scope="request" 
	        /><c:set var="jsonMap" value="${jsonVa[componentId] }" scope="request" 
       		/><u:set var="emptyLangId" test="${colmTyp eq 'label' }" value="label" elseValue="name"
			/><c:set var="langTitleId" value="${emptyLangId }RescVa_${_lang }" scope="request"
			/><u:set var="langTitle" test="${!empty jsonMap[langTitleId] }" value="${jsonMap[langTitleId] }" elseValue="${itemNm }"
			/><c:set var="dataVa" value="${dataToJson[componentId] }" 
	        /><u:set var="openArea" test="${colmTyp eq 'editor' }" value=" onclick=\"openArea('${componentId }Area');\""
	        /> <c:if test="${isEndYn eq 'Y' }"><div class="entryzone" ><div class="entryarea"><dl><c:set var="isEndYn" value="N"/></c:if
	        ><c:if test="${colmTyp ne 'radioSingle' && colmTyp ne 'checkboxSingle' && colmTyp ne 'addr' }"><dd class="etr_bodytit"${openArea }>${langTitle }</dd></c:if>                    
			<c:if test="${colmTyp ne 'file' && colmTyp ne 'editor' }"><dd class="dd_blank5"></dd><dd class="etr_input" style="min-height:17px;" id="${componentId }Area"></c:if>
			<c:if test="${colmTyp eq 'text' }"
			><u:out value="${dataVa }"/></c:if><c:if test="${colmTyp eq 'textarea' }"
			><u:out value="${dataVa }"/></c:if><c:if test="${colmTyp eq 'tel' }"
			><u:out value="${dataVa }"/></c:if><c:if test="${colmTyp eq 'editor' }"
			><c:if test="${isEndYn eq 'N' }"></dl></div></div><c:set var="isEndYn" value="Y"
			/></c:if><div class="bodyzone_scroll" id="${componentId }Area">
            <div class="bodyarea"><dl><dd class="bodytxt_scroll"><div class="scroll editor" id="bodyHtmlArea"><div id="zoom"><u:out value="${dataVa }" type="value"/></div></div>
            </dd></dl></div>
            </div></c:if><c:if test="${colmTyp eq 'date' }"
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
			><c:if test="${!codeStatus.first }">, </c:if><c:if test="${colmTyp eq 'user' }"><a href="javascript:;" onclick="viewUserPop('${codeVo.cdVa}')">${codeVo.cdNm }</a></c:if
			><c:if test="${colmTyp ne 'user' }">${codeVo.cdNm }</c:if></c:forEach></c:if></c:if
			><c:if test="${colmTyp eq 'file' }"
			><c:if test="${isEndYn eq 'N' }"></dl></div></div><c:set var="isEndYn" value="Y"
			/></c:if><div class="attachzone">
			<div class="attacharea">
				<c:if test="${!empty fileVoList }">
					<c:forEach items="${fileVoList}" var="fileVo" varStatus="status">
						<m:attach fileName="${fileVo.dispNm}" fileKb="${fileVo.fileSize/1024 }" downFnc="downFile('${fileVo.fileId}');" viewFnc="viewAttchFile('${fileVo.fileId}');"/>
					</c:forEach>
				</c:if>
			</div></div>
			</c:if><c:if test="${colmTyp eq 'checkbox' || colmTyp eq 'select' || colmTyp eq 'radio'}"
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
			/><div id="${componentId }_imgArea" onclick="$(this).hide()" style="background:url('${imgPath}') no-repeat 50% 0; background-size:contain; position:absolute; left:6px; right:6px; height:500px; display:none; z-index:2"></div
			><c:if test="${viewSizeTyp eq 'px'}"><u:set test="${imgVo.imgWdth <= maxWdth}" var="imgWdth" value="${imgVo.imgWdth}" elseValue="${maxWdth}" 
			/><u:set test="${imgVo.imgHght <= maxHght}" var="imgHght" value="${imgVo.imgHght}" elseValue="${maxHght}" 
			/><u:set test="${imgVo.imgWdth < imgVo.imgHght}" var="imgWdthHgth" value="height='${imgHght}'" elseValue="width='${imgWdth}'" 
			/></c:if><c:if test="${viewSizeTyp eq 'per'}"
			><u:set test="${maxWdth < maxHght}" var="imgWdthHgth" value="height='${maxHght}%'" elseValue="width='${maxWdth}%'" 
			/></c:if></c:if><div class="image_profile"><dl><dd class="photo"><c:if test="${!empty imgVo}"
			><img src="${imgPath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' ${imgWdthHgth} onclick="$('#${componentId }_imgArea').show();"
			></c:if><c:if test="${empty imgVo }"><img src="${_cxPth}/images/${_skin}/photo_noimg.png" width="88px"></c:if></dd></dl></div
			></c:if><c:if test="${colmTyp ne 'file' && colmTyp ne 'editor' }"></dd></c:if><dd class="etr_blank1"></dd></c:forEach>
			<c:if test="${isEndYn eq 'N' }"></dl></div></div></c:if>
		</div></c:forEach></div>
	
	<!-- <a style="display:scroll; position:fixed; bottom:2px; right:15px;opacity: 1; display: inline; zoom: 1;" title="Top" href="#"><img src="/images/blue/btn_up_s.png" ></a> -->
	</c:if><c:if test="${mobRegTypCd eq 'U' }"><div class="bodyzone_scroll view contCls">
                <div class="bodyarea"><dl><dd class="bodytxt_scroll"><div class="scroll editor" id="bodyHtmlArea">
                <div id="zoom"><jsp:include page="/WEB-INF/jsp/wf/web/viewWorks.jsp" flush="false" 
                ><jsp:param value="Y" name="isWeb"/> </jsp:include></div></div></dd></dl></div></div>
	</c:if>
	<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
	
 </section>
