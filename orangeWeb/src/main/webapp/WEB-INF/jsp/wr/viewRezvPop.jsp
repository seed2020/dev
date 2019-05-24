<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set var="saveDisc" test="${!empty param.pltYn && param.pltYn eq 'Y' }" value="saveDiscAjx" elseValue="saveDisc"/>
<u:authUrl var="saveUrl" url="/wr/transRezvDiscAjx.do" authCheckUrl="/wr/listRezvDisc.do"/>
<script type="text/javascript">
<!--
//자원현황 팝업
function listRezvPop() {
	var url = './listRezvStatPop.do?menuId=${menuId}&rescKndId='+$("#setRezv #rescKndId").val()+'&rescMngId='+$("#setRezv #rescMngId").val();
	url+="&durCat=fromYmd";
	url+="&durStrtDt="+$('#rezvStrtYmd').val();
	url+="&durEndDt="+$('#rezvEndYmd').val();
	dialog.open('listRezvStatPop','<u:msg titleId="wr.btn.rezvStat" alt="예약현황"/>',url);
};

//삭제
function fnDelete(){
	if(confirmMsg("cm.cfrm.del")) {
		var $form = $('#deleteForm');
		$form.attr('method','post');
		$form.attr('target','dataframe');
		$form[0].submit();	
	}
};

<% // [하단버튼:승인,반려] %>
function saveDiscAjx(discStatCd) {
	if(discStatCd == null) return;
	$('#viewRezv #discStatCd').val(discStatCd);
	var param = $('#viewRezv').serialize();
	//var param = new ParamMap().getData('viewRezv');
	//param = JSON.stringify(param.toJSON())
	if(validator.validate('viewRezv') && confirmMsg("cm.cfrm.save")) {<%//cm.cfrm.save=저장 하시겠습니까? %>
		$.ajax({
	        url: '${saveUrl}',
	        type: 'POST',
	        data:param,
	        dataType:'json',
	        success: function(data){
	        	if(data.model.message!=null){
					alert(data.model.message);
				}
	        	if(data.model.result=='ok'){
	        		getIframeContent('PLT_${param.pltId}').getIframeContent('wrRescPltFrm').reload();
	        	}
	        	dialog.close("setRezvPop");
	        }
		});
	}
};

<% // [하단버튼:승인,반려] %>
function saveDisc(discStatCd) {
	if(discStatCd == null) return;
	$('#viewRezv #discStatCd').val(discStatCd);
	if (validator.validate('viewRezv') && confirmMsg("cm.cfrm.save")) {
		var $form = $('#viewRezv');
		$form.attr('method','post');
		$form.attr('action','./transRezvDisc.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
};

//-->
</script>
<div style="width:700px;">
<form id="viewRezv">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="rezvId" value="${wrRezvBVo.rezvId}" />
<u:input type="hidden" id="listPage" value="./${listPage }.do?${paramsForList}" />
<u:input type="hidden" id="rescMngId" value="${wrRezvBVo.rescMngId}" />
<u:input type="hidden" id="discStatCd" />
<% // 폼 필드 %>
<div class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
		<colgroup><col width="18%"/><col width="*"/></colgroup>
		<tr>
			<td class="head_lt"><u:msg titleId="cols.rescKnd" alt="자원종류" /></td>
			<td class="body_lt">${wrRezvBVo.kndNm }</td>
		</tr>
		
		<tr>
			<td class="head_lt"><u:msg titleId="cols.rescNm" alt="자원명" /></td>
			<td class="body_lt"><div class="ellipsis" title="${wrRezvBVo.rescNm }">${wrRezvBVo.rescNm }</div></td>
		</tr>
		
		<tr>
			<td class="head_lt"><u:msg titleId="cols.rezvDt" alt="예약일시" /></td>
			<td class="body_lt"><u:out value="${wrRezvBVo.rezvStrtDt }" type="longdate" /> ~ <u:out value="${wrRezvBVo.rezvEndDt }" type="longdate" /></td>
		</tr>
		<tr>
			<td class="head_lt"><u:msg titleId="cols.rezvr" alt="예약자" /></td>
			<td>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tbody>
					<tr>
						<td class="body_lt" width="20%"><a href="javascript:viewUserPop('${wrRezvBVo.regrUid }');">${wrRezvBVo.regrNm }</a></td>
						<td class="body_lt">
							<c:if test="${(discYn eq 'Y' && listPage eq 'listRezvDisc') || wrRezvBVo.discStatCd eq 'J' || wrRezvBVo.discStatCd eq 'A' }">
								<u:checkArea>
									<c:choose>
										<c:when test="${mailEnable == 'Y' }"><u:checkbox id="resEmailYn" name="resEmailYn" value="Y" titleId="wr.option.resEmailYn" alt="결과메일여부" checkValue="${wrRezvBVo.resEmailYn }" disabled="${wrRezvBVo.discStatCd eq 'J' || wrRezvBVo.discStatCd eq 'A' ? 'Y' : 'N' }" checked="${empty wrRezvBVo.resEmailYn }"/></c:when>
										<c:otherwise><input type="checkbox" name="resEmailYn" id="resEmailYn" value="Y" style="display:none;"/></c:otherwise>
									</c:choose>
								</u:checkArea>
							</c:if>
						</td>		
					</tr>
				</tbody>
				</table>
			</td>
		</tr>
		<tr>
			<td class="head_lt"><u:msg titleId="wr.cols.rezvStat" alt="예약상태" /></td>
			<td class="body_lt"><u:msg titleId="wr.jsp.discStatCd${wrRezvBVo.discStatCd }.title" alt="예약상태"/></td>
		</tr>
		<tr>
			<td class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
			<td class="body_lt"><div class="ellipsis" title="${wrRezvBVo.subj }">${wrRezvBVo.subj }</div></td>
		</tr>
		<c:if test="${envConfigMap.tgtUseYn eq 'Y' && !empty wrRezvBVo.schdlKndCd}"><tr>
			<td class="head_lt"><u:msg titleId="wc.cols.schdlKndCd" alt="일정대상"/></td>
			<td class="body_lt"><c:choose><c:when test="${wrRezvBVo.schdlKndCd eq '1' }"><u:msg titleId="wc.jsp.listPsnSchdl.psn.title" alt="개인일정"
			/></c:when><c:when test="${wrRezvBVo.schdlKndCd eq '3' }"><u:msg titleId="wc.jsp.listPsnSchdl.dept.title" alt="부서일정"
			/></c:when><c:when test="${wrRezvBVo.schdlKndCd eq '4' }"><u:msg titleId="wc.jsp.listPsnSchdl.comp.title" alt="회사일정"
			/></c:when></c:choose></td></tr></c:if>
		<c:if test="${envConfigMap.guestUseYn eq 'Y' && !empty wcPromGuestDVoList}">
		<tr><td class="head_lt"><u:msg titleId="cols.guest" alt="참석자" /></td><td>
				<div style="width:100%;height:160px;overflow-y:auto;">
					<div id="listArea" class="listarea" style="width:95%; padding:5px;">
						<table class="listtable" border="0" cellpadding="0" cellspacing="1" >
							<tr id="headerTr">
								<th width="17%"  class="head_ct"><u:msg titleId="cols.nm" alt="이름" /></th>
								<th width="18%" class="head_ct"><u:msg titleId="wb.cols.emplTyp" alt="임직원구분" /></th>
								<th class="head_ct"><u:msg titleId="cols.comp" alt="회사" />/<u:msg titleId="cols.dept" alt="부서" /></th>
								<th width="17%" class="head_ct"><u:msg titleId="cols.email" alt="이메일" /></th>
								<c:if test="${!empty mailAcceptMap }"><th width="10%" class="head_ct"><u:msg titleId="wc.cols.guest.accept" alt="수락" /></th></c:if>
							</tr>
							<c:if test="${!empty wcPromGuestDVoList}">
								<c:forEach var="list" items="${wcPromGuestDVoList}" varStatus="status">
									<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
										<td class="body_ct" id="emplNm">
											<c:choose>
												<c:when test="${list.guestEmplYn eq 'Y' }"><a href="javascript:viewUserPop('${list.guestUid}');">${list.guestNm}</a></c:when>
												<c:when test="${list.guestEmplYn eq 'N' }"><a href="javascript:viewBc('${list.guestUid }');">${list.guestNm}</a></c:when>
												<c:otherwise>${list.emplNm}</c:otherwise>
											</c:choose>
										</td>
										<td class="body_ct" id="emplTypNm"><u:msg titleId="${list.guestEmplYn eq 'Y' ? 'cm.option.empl' : 'wc.option.frnd'}" alt="지인"/></td>
										<td class="body_ct" id="emplCompNm"><div class="ellipsis" title="${list.guestCompNm }">${list.guestCompNm}</div></td>
										<td class="body_ct" id="emplEmail"><a href="javascript:parent.mailToPop('${list.email }');" title="<u:msg titleId='or.jsp.viewUserPop.mailToPop' />">${list.email}</a></td>
										<c:if test="${!empty mailAcceptMap }"><td class="body_ct" ><c:if test="${list.guestEmplYn eq 'Y'}"
											><u:convertMap srcId="mailAcceptMap" attId="${list.guestUid}" var="acptMap" 
											/><div class="ellipsis" title="<u:msg titleId="cols.regDt" alt="등록일시"/> : <u:out value="${acptMap.regDt}" type="longdate"/>">
											${acptMap.acptYn }</div></c:if><c:if test="${list.guestEmplYn ne 'Y'}">X</c:if></td>
										</c:if>
									</tr>
								</c:forEach>
							</c:if>
						</table>
					</div>
				</div>
			</td>
		</tr>
		</c:if>
		<tr>
			<td class="body_lt" colspan="2"><div style="overflow:auto;height:200px;">${wrRezvBVo.cont }</div></td>
		</tr>
		<c:if test="${discYn eq 'Y' && listPage eq 'listRezvDisc'}">
		<tr>
			<td class="head_lt"><u:mandatory /><u:msg titleId="cols.discOpin" alt="심의의견" /></td>
			<td class="body_lt"><u:textarea id="discCont" value="${wrRezvBVo.discCont}" titleId="cols.discOpin"	 maxByte="240" style="width:97%" rows="4" mandatory="Y"/></td>
		</tr>
		</c:if>
		<c:if test="${wrRezvBVo.discStatCd eq 'J' || wrRezvBVo.discStatCd eq 'A'}">
		<tr>
			<td class="head_lt"><u:msg titleId="wr.cols.discrNm" alt="심의자" /></td>
			<td class="body_lt"><a href="javascript:viewUserPop('${wrRezvBVo.discrUid }');">${wrRezvBVo.discrNm }</a></td>
		</tr>
		<tr>
			<td class="head_lt"><u:msg titleId="cols.discOpin" alt="심의의견" /></td>
			<td class="body_lt"><div style="overflow:auto;height:50px;">${wrRezvBVo.discCont}</div></td>
		</tr>
		</c:if>
	</table>
</div>

<u:blank />

<u:buttonArea>
	<%-- <u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" auth="R" /> --%>
	<c:if test="${discYn eq 'Y' && listPage eq 'listRezvDisc'}">
		<u:button titleId="cm.btn.apvd" href="javascript:;" onclick="${saveDisc }('A');" alt="승인" auth="W" />
		<u:button titleId="cm.btn.rjt" href="javascript:;" onclick="${saveDisc }('J');" alt="반려" auth="W" />
	</c:if>
	<c:if test="${empty param.pltYn || param.pltYn eq 'N' }">
	<c:if test="${!empty wrRezvBVo.rezvId && (wrRezvBVo.discStatCd eq 'B' || wrRezvBVo.discStatCd eq 'R') }">
		<u:button titleId="cm.btn.mod" alt="수정" href="javascript:;" onclick="setRezvPop('${wrRezvBVo.rezvId }');" auth="M" ownerUid="${wrRezvBVo.regrUid }"/>
		<u:button titleId="cm.btn.del" href="javascript:;" onclick="fnDelete();" alt="삭제" auth="A" ownerUid="${wrRezvBVo.regrUid }"/>
	</c:if>
	</c:if>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>
<form id="deleteForm" name="deleteForm" action="./transRezvDel.do">
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="rezvId" value="${wrRezvBVo.rezvId}" />
	<u:input type="hidden" id="listPage" value="./${listPage }.do?${paramsForList}" />
</form>