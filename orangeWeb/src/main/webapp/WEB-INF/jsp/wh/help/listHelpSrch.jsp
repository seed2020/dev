<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><style>
ul.selectList{list-style:none;float:left;margin:0px;padding:0px;}
ul.selectList li{float:left;}
ul.selectList li.optionList{padding-left:5px;}
</style>
<script type="text/javascript">
<!--
<% // 검색조건 등록자 선택 %>
function schUserPop() {
	var $view = $("#searchForm2");
	var data = {userUid:$view.find("#schOptWord").val()};<% // 팝업 열때 선택될 데이타 %>
	var param={data:data};
	/* <c:if test="${isAdmin==false && path eq 'req'}">param['downward']='${sessionScope.userVo.orgId}';</c:if> */
	<% // option : data, multi, withSub, titleId %>
	parent.searchUserPop(param, function(userVo){
		if(userVo!=null){
			$view.find("#schOptWord").val(userVo.userUid);
			$view.find("#schUserNm").val(userVo.rescNm);
		}else{
			return false;
		}
	});
}<%// 부서 선택 %>
function schOrgPop(){
	var data = [];
	parent.searchOrgPop({data:data}, function(orgVo){
		if(orgVo!=null){
			$('#deptId').val(orgVo.orgId);
			$('#deptNm').val(orgVo.rescNm);
		}
	});
}<% // 시스템 모듈 선택 %>
function selectSysMdList(obj){
	var target=$(obj).closest('li');
	$(target).nextAll().remove();
	// 담당자 select
	var pichSelect=$('#mdPichContainer select').eq(0);
	pichSelect.find('option').not(':first').remove(); // 선택을 제외하고 삭제
	setJsUniform($('#mdPichContainer'));
	
	// 처리유형 select
	var catSelect=$('#mdCatContainer select').eq(0);
	catSelect.find('option').not(':first').remove(); // 선택을 제외하고 삭제
	setJsUniform($('#mdCatContainer'));
	
	if(obj.value=='' || obj.value===undefined) return;
	callAjax('./getSysMdListAjx.do?menuId=${menuId}', {mdPid:obj.value}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			addSysMdList(target, data.whMdBVoList, obj.value, pichSelect, catSelect);
		}
		if (data.result == 'end') {
			setMdDtlList(obj.value, pichSelect, catSelect);
		}
	});
}<% // 담당자 | 유형  조회 %>
function setMdDtlList(mdId, pichSelect, catSelect){
	callAjax('./getMdPichListAjx.do?menuId=${menuId}', {mdId:mdId}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') { // 담당자 추가
			addMdPichList(pichSelect, data.whMdPichLVoList);
		}
	});
	callAjax('./getMdCatListAjx.do?menuId=${menuId}', {mdId:mdId}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') { // 담당자 추가
			addMdCatList(catSelect, data.whCatGrpLVoList);
		}
	});
}<% // 시스템 모듈 추가 %>
function addSysMdList(target, whMdBVoList, mdId, pichSelect, catSelect){
	if(whMdBVoList==null || whMdBVoList.length==0) return;
	var buffer=[];
	var parent=$('<li></li>');
	buffer.push('<select onchange="selectSysMdList(this);" style="min-width:100px;">');
	var whMdBVo;
	buffer.push('<option value="">'+callMsg('cm.select.actname')+'</option>');
	$.each(whMdBVoList, function(index, item){
		whMdBVo=item.map;
		buffer.push('<option value="'+whMdBVo.mdId+'">'+whMdBVo.mdNm+'</option>');
	});
	buffer.push('</select>');
	
	parent.append($(buffer.join('')));
	
	if(target!=undefined){
		restoreUniform('sysMdContainer');
		$(target).after(parent);		
		//setJsUniform(parent);
		var container=$('#sysMdContainer');
		if(container.scrollTop()>0){
			container.css('height', (container.height()+container.scrollTop()+5)+'px');
		}
		applyUniform('sysMdContainer');
	}
}<% // 시스템 모듈 담당자 추가 %>
function addMdPichList(target, whMdPichLVoList){
	if(whMdPichLVoList==null || whMdPichLVoList.length==0) return;
	$.each(whMdPichLVoList, function(index, item){
		whMdPichLVo=item.map;
		target.append('<option value="'+whMdPichLVo.idVa+'">'+whMdPichLVo.pichNm+'</option>');
	});
	
}<% // 시스템 모듈 처리유형 추가 %>
function addMdCatList(target, whCatGrpLVoList){
	if(whCatGrpLVoList==null || whCatGrpLVoList.length==0) return;
	$.each(whCatGrpLVoList, function(index, item){
		whCatGrpLVo=item.map;
		target.append('<option value="'+whCatGrpLVo.catNo+'">'+whCatGrpLVo.catNm+'</option>');
	});
}<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
}<% // 조회 %>
function searchForm(){
	var $form = $('#searchForm2');	
	var mdSelect = $('#sysMdContainer select:last');
	var mdId=mdSelect.val();
	if(mdId==''){
		var obj=$(mdSelect).closest('li');
		var val=null;
		$.each($(obj).prevAll(), function(){
			val=$(this).find('select > option:selected').val();
			if(val!=''){
				mdId=val;
				return false;
			}
		});
	} 
	$form.find("input[name='mdId']").remove();
	if(mdId!='')
		$form.appendHidden({name:'mdId',value:mdId});
	$form[0].submit();
}<% // 검색 조건 초기화 %>
function searchReset(){	
	valueReset('searchFormTbl2',['durCat','schOptWord']);
	selectSysMdList($('#sysMdContainer select:first'));
}
//-->
</script>
<u:set var="path" test="${!empty path }" value="${path }" elseValue="${param.path }"/>
<u:searchArea>
	<c:if test="${path ne 'dashbrd' }">
	<div id="searchArea1" style="<c:if test="${not empty param.srchDetl}">display:none;</c:if>">
	<form id="searchForm1" name="searchForm1" action="${_uri}" >
	<u:input type="hidden" id="menuId" value="${menuId}" /><c:if
	test="${not empty param.pageRowCnt}">
	<u:input type="hidden" id="pageRowCnt" value="${param.pageRowCnt}" /></c:if>
	<u:set var="paramStatCd" test="${!empty paramStatCd }" value="${paramStatCd }" elseValue="${param.statCd }"/>
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
		<tr>
			<td>
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td><u:buttonIcon alt="검색 조건 펼치기" titleId="cm.ico.showCondi" image="ico_wdown.png" onclick="$('#searchArea1').toggle(); $('#searchArea2').toggle();" /></td>
						<td><select name="schCat" style="min-width:60px;">
							<u:option value="docNo" titleId="wh.cols.docNo" alt="요청번호" checkValue="${param.schCat}" selected="${!empty param.docNo }"/>
							<c:if test="${path ne 'req' || isAdmin == true}"><u:option value="reqrNm" titleId="wh.cols.req.reqr" alt="요청자" checkValue="${param.schCat}" /></c:if>
							<u:option value="subj" titleId="cols.subj" alt="제목" checkValue="${param.schCat}" selected="${!empty param.subj }"/>
							<u:option value="cont" titleId="cols.cont" alt="내용" checkValue="${param.schCat}" />
							<c:if test="${path eq 'hdl' && param.statCd eq 'C'}"><u:option value="hdlCont" titleId="wh.cols.hdl.hdlCont" alt="완료처리사항" checkValue="${param.schCat}" /></c:if>							
							</select>
						</td>
						<td><u:set var="paramSchWord" test="${!empty param.subj }" value="${param.subj }" elseValue="${param.schWord }"/><u:input id="schWord" maxByte="50" value="${paramSchWord}" titleId="cols.schWord" style="width: 200px;" onkeydown="if (event.keyCode == 13) searchForm1.submit();" /></td>
						<td class="width20"></td>
						<td class="search_tit"><u:msg titleId="cols.prd" alt="기간" /></td>
						<td>
							<table border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td>
										<select name="durCat" style="min-width:70px;">
										<u:option value="reqDt" titleId="wh.cols.reqYmd" alt="요청일" checkValue="${param.durCat}"  selected="${empty param.durCat }"
										/><c:if test="${path ne 'recv'}"><u:option value="recvDt" titleId="wh.cols.recv.recvYmd" alt="접수일" checkValue="${param.durCat}"
										/></c:if><u:option value="cmplDt" titleId="wh.cols.req.cmplDt" alt="완료일" checkValue="${param.durCat}" />
										</select>
									</td>
									<td><u:calendar id="durStrtDt" option="{end:'durEndDt'}" value="${param.durStrtDt}" /></td>
									<td class="search_body_ct"> ~ </td>
									<td><u:calendar id="durEndDt" option="{start:'durStrtDt'}" value="${param.durEndDt}" /></td>
								</tr>
							</table>
						</td>
						<td class="width20"></td>						
						<c:if test="${path ne 'dashbrd' }">
						<td class="search_tit"><u:msg titleId="cols.prgStat" alt="진행상태" /></td>
						<td><select name="statCd" style="min-width:70px;">
							<u:option value="" titleId="cm.option.all" alt="전체" selected="${empty paramStatCd }"
							/><c:if test="${path eq 'req' || path eq 'recv'}"><u:option value="R" titleId="wh.option.statCdR" alt="요청" checkValue="${paramStatCd }"
							/><u:option value="G" titleId="wh.option.statCdG" alt="반려" checkValue="${paramStatCd }"
							/></c:if><u:option value="A" titleId="wh.option.statCdA" alt="접수" checkValue="${paramStatCd }"
							/><c:if test="${path ne 'recv' }"><u:option value="P" titleId="wh.option.statCdP" alt="처리중" checkValue="${paramStatCd }"
							/><c:if test="${path ne 'hdl' }"><u:option value="C" titleId="wh.option.statCdC" alt="처리완료" checkValue="${paramStatCd }"
							/></c:if></c:if>
							</select>
						</td><td class="width20"></td>
						</c:if>
						<c:if test="${path eq 'hdl' }">
						<td><u:checkArea><u:checkbox id="cmplInYn" name="cmplInYn" value="Y" titleId="wh.cols.cmpl.incl" checkValue="${param.cmplInYn}"/></u:checkArea></td>
						</c:if>
					</tr>
				</table>
			</td>
			<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm1.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
		</tr>
	</table>
	</form>
	</div></c:if>
	<div id="searchArea2" style="<c:if test="${path ne 'dashbrd' && empty param.srchDetl}">display:none;</c:if>">
	<form id="searchForm2" name="searchForm2" action="${_uri}" >
	<u:input type="hidden" id="menuId" value="${menuId}" /><c:if
	test="${not empty param.pageRowCnt}">
	<u:input type="hidden" id="pageRowCnt" value="${param.pageRowCnt}" /></c:if>
	<u:input type="hidden" id="srchDetl" value="Y" />
	<c:if test="${!empty param.listTyp && param.listTyp eq 'popup' }">
	<c:if test="${!empty param.page}"><u:input type="hidden" id="page" value="${param.page}" /></c:if>
	<c:if test="${!empty param.statCd}"><u:input type="hidden" id="statCd" value="${param.statCd}" /></c:if>
	</c:if>
	<table id="searchFormTbl2" class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td>
		<c:if test="${path ne 'dashbrd' }">
		<div style="float:left; padding: 2px 10px 0px 0px;">
			<u:buttonIcon alt="검색 조건 숨기기" titleId="cm.ico.hideCondi" image="ico_wup.png" onclick="$('#searchArea1').toggle(); $('#searchArea2').toggle();" />
		</div></c:if>		
		<table border="0" cellpadding="0" cellspacing="0">
		<c:if test="${path ne 'dashbrd' }">
		<tr>
			<td class="search_tit"><u:msg titleId="wh.cols.docNo" alt="요청번호" /></td>
			<td><u:set var="paramDocNo" test="${empty param.docNo && !empty param.schCat && param.schCat eq 'docNo' && !empty param.schWord}" value="${param.schWord }" elseValue="${param.docNo }"/><u:input id="docNo" maxByte="50" value="${paramDocNo}" titleId="cols.schWord" style="width: 200px;" onkeydown="if (event.keyCode == 13) searchForm2.submit();" /></td>
			<td class="width20"></td>
			<td class="search_tit"><u:msg titleId="cols.subj" alt="제목" /></td>
			<td><u:set var="paramSubj" test="${empty param.subj && !empty param.schCat && param.schCat eq 'subj' && !empty param.schWord}" value="${param.schWord }" elseValue="${param.subj }"/><u:input id="subj" maxByte="50" value="${paramSubj}" titleId="cols.schWord" style="width: 200px;" onkeydown="if (event.keyCode == 13) searchForm2.submit();" /></td>
			<td class="width20"></td>
		</tr><tr>
			<td class="search_tit"><u:msg titleId="cols.cont" alt="내용" /></td>
			<td><u:set var="paramCont" test="${empty param.cont && !empty param.schCat && param.schCat eq 'cont' && !empty param.schWord}" value="${param.schWord }" elseValue="${param.cont }"/><u:input id="cont" maxByte="100" value="${paramCont}" titleId="cols.schWord" style="width: 200px;" onkeydown="if (event.keyCode == 13) searchForm2.submit();" /></td>
			<td class="width20"></td>
			<c:if test="${path eq 'hdl' && param.statCd eq 'C'}">
			<td class="search_tit"><u:msg titleId="wh.cols.hdl.hdlCont" alt="완료처리사항" /></td>
			<td><u:set var="paramHdlCont" test="${empty param.hdlCont && !empty param.schCat && param.schCat eq 'hdlCont' && !empty param.schWord}" value="${param.schWord }" elseValue="${param.hdlCont }"/><u:input id="hdlCont" maxByte="100" value="${paramHdlCont}" titleId="cols.schWord" style="width: 200px;" onkeydown="if (event.keyCode == 13) searchForm2.submit();" /></td>
			<td class="width20"></td></c:if>
		</tr>
		</c:if><tr>
			<td class="search_tit"><u:msg titleId="cols.prd" alt="기간" /></td>
			<td>
				<table border="0" cellpadding="0" cellspacing="0">
					<tr><c:if test="${path ne 'dashbrd' }">
						<td>
							<select name="durCat" style="min-width:70px;">
							<u:option value="reqDt" titleId="wh.cols.reqYmd" alt="요청일" checkValue="${param.durCat}"  selected="${empty param.durCat }"
							/><c:if test="${path ne 'recv'}"><u:option value="recvDt" titleId="wh.cols.recv.recvYmd" alt="접수일" checkValue="${param.durCat}"
							/></c:if><u:option value="cmplDt" titleId="wh.cols.req.cmplDt" alt="완료일" checkValue="${param.durCat}" />
							</select>
						</td></c:if>
						<td><c:if test="${path eq 'dashbrd' }"><u:input type="hidden" id="durCat" value="reqDt"/></c:if><u:calendar id="durStrtDt" option="{end:'durEndDt'}" value="${!empty durStrtDt ? durStrtDt : param.durStrtDt}" /></td>
						<td class="search_body_ct"> ~ </td>
						<td><u:calendar id="durEndDt" option="{start:'durStrtDt'}" value="${param.durEndDt}" /></td>
					</tr>
				</table>
			</td>
			<td class="width20"></td>
			<td class="search_tit"><u:msg titleId="wh.jsp.sysMd.title" alt="시스템모듈" /></td>
			<td>
				<div id="sysMdContainer" style="width:100%;">
					<ul id="sysMdArea" class="selectList">
						<c:forEach items="${paramMdList}" var="whMdBVoList" varStatus="paramStatus"
						><li>	<select onchange="selectSysMdList(this);" style="min-width:60px;">
						<u:option value="" titleId="cm.select.actname" alt="선택"
						/><c:forEach items="${whMdBVoList}" var="whMdBVoVo" varStatus="status"><u:option value="${whMdBVoVo.mdId }" title="${whMdBVoVo.mdNm }" checkValue="${empty paramMdIds ? param.mdId : paramMdIds[paramStatus.index]}" /></c:forEach>
						</select>
						</li>
						</c:forEach>
					</ul>
				</div>
			</td>
			<td class="width20"></td>
		</tr><tr><td class="search_tit"><u:msg titleId="wh.cols.hdl.pich" alt="처리 담당자" /></td>
			<td>
				<div id="mdPichContainer">
					<select name="pichUid" style="min-width:100px;">
					<u:option value="" titleId="cm.select.actname" alt="선택"
					/><c:if test="${!empty whMdPichLVoList }"><c:forEach var="whMdPichLVo" items="${whMdPichLVoList }" varStatus="status"><u:option value="${whMdPichLVo.idVa }" title="${whMdPichLVo.pichNm }" checkValue="${param.pichUid }"/></c:forEach></c:if>
					</select>
				</div>
			</td>
			<td class="width20"></td>
			<td class="search_tit"><u:msg titleId="wh.cols.hdl.typ" alt="처리유형" /></td>
			<td>
				<div id="mdCatContainer">
					<select name="catNo" style="min-width:100px;" <u:elemTitle titleId="wh.cols.hdl.typ" />
					><u:option value="" titleId="cm.select.actname" alt="선택" selected="${empty param.catNo }"
					/><c:forEach var="whCatGrpLVo" items="${whCatGrpLVoList }" varStatus="status"
					><u:option value="${whCatGrpLVo.catNo}" title="${whCatGrpLVo.catNm}" checkValue="${param.catNo }"/></c:forEach>
					</select>
				</div>
			</td>
			<td class="width20"></td>
		</tr><c:if test="${path ne 'req' || isAdmin==true }"><tr><td class="search_tit"><u:msg titleId="cols.user" alt="사용자" /></td>
			<td>
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>
							<select name="schOptCat" style="min-width:90px;">
							<u:option value="regrUid" titleId="wh.cols.req.reqr" alt="요청자" checkValue="${param.schOptCat}"
							/><u:option value="recvUid" titleId="wh.cols.recv.recvr" alt="접수자" checkValue="${param.schOptCat}" 	
							/><c:if test="${path eq 'hdl' }"><u:option value="pichUid" titleId="wh.cols.recv.devPich" alt="개발담당자" checkValue="${param.schOptCat}" /></c:if>
							</select>
						</td>
						<td>
							<u:input id="schUserNm" value="${param.schUserNm}" titleId="cols.pich" readonly="Y" />
							<u:input type="hidden" id="schOptWord" value="${param.schOptWord}" />
						</td>
						<td><u:buttonS titleId="cm.btn.read" alt="조회" onclick="schUserPop();" /></td>
					</tr>
				</table>
			</td>
			<td class="width20"></td>
			<td class="search_tit"><u:msg titleId="wh.cols.req.deptNm" alt="요청부서" /></td>
			<td>
				<table border="0" cellpadding="0" cellspacing="0">
					<tbody>
					<tr>
						<td>
							<u:input id="deptNm" value="${param.deptNm}" titleId="cols.pich" readonly="Y" />
							<u:input type="hidden" id="deptId" value="${param.deptId}" />
						</td>
						<td><u:buttonS titleId="cm.btn.read" alt="조회" onclick="schOrgPop();" /></td>
					</tr>
					</tbody>
				</table>
			</td><td class="width20"></td>
		</tr></c:if>
		<c:if test="${path ne 'dashbrd' }">
		<tr>
			<td class="search_tit"><u:msg titleId="cols.prgStat" alt="진행상태" /></td>
			<td><table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>
							<select name="statCd" style="min-width:100px;">
								<u:option value="" titleId="cm.option.all" alt="전체" selected="${empty paramStatCd }"
								/><c:if test="${path eq 'req' || path eq 'recv'}"><u:option value="R" titleId="wh.option.statCdR" alt="요청" checkValue="${paramStatCd }"
								/><u:option value="G" titleId="wh.option.statCdG" alt="반려" checkValue="${paramStatCd }"
								/></c:if><u:option value="A" titleId="wh.option.statCdA" alt="접수" checkValue="${paramStatCd }"
								/><c:if test="${path ne 'recv' }"><u:option value="P" titleId="wh.option.statCdP" alt="처리중" checkValue="${paramStatCd }"
								/><c:if test="${path ne 'hdl' }"><u:option value="C" titleId="wh.option.statCdC" alt="처리완료" checkValue="${paramStatCd }"
								/></c:if></c:if></select>
						</td>
						<c:if test="${path eq 'hdl' }">
						<td class="width10"></td>
						<td><u:checkArea><u:checkbox id="cmplInYn2" name="cmplInYn" value="Y" titleId="wh.cols.cmpl.incl" checkValue="${param.cmplInYn}"/></u:checkArea></td>
						</c:if>
					</tr>
				</table>
			</td><c:if test="${!empty envConfigMap.resEvalUseYn && envConfigMap.resEvalUseYn eq 'Y'}"><td class="width20"></td>
			<td class="search_tit"><u:msg titleId="wh.cols.req.eval" alt="평가" /></td>
			<td><select name="evalYn" style="min-width:100px;">
				<u:option value="" titleId="cm.select.actname" alt="선택" selected="${empty param.evalYn }"
				/><u:option value="Y" titleId="wh.option.evalY" alt="평가됨" checkValue="${param.evalYn }"
				/><u:option value="N" titleId="wh.option.evalN" alt="미평가" checkValue="${param.evalYn }"
				/></select></td>
			<td class="width20"></td>
			</c:if>
		</tr></c:if>
		<tr>
			<td colspan="2"><u:buttonS href="javascript:;" onclick="searchReset();" titleId="cm.btn.srch.reset" alt="검색조건 초기화" /></td>
			<td class="width20"></td>			
		</tr>
	</table>
	</td>
	<td><div class="button_search"><ul><li class="search"><a href="javascript:searchForm();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
	</div>
</u:searchArea>