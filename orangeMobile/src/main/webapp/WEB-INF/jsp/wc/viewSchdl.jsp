<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:authUrl var="viewUrl" url="/wb/viewBc.do" authCheckUrl="/wb/listBc.do"/><!-- view page 호출관련 url 조합(menuId추가) -->
<script type="text/javascript">
//<![CDATA[
function fnTabOn(obj,val){
	$(".view").each(function(){$(this).hide();});
	$(".view."+val+"Cls").each(function(){$(this).show();});
	$(".tabarea dd").each(function(){$(this).attr("class", "tab");});
	$(obj).attr("class","tab_on");
};     

function fnDelete(flag,schdlId){
	$m.msg.confirmMsg("cm.cfrm.del", null , function(result){
		if(result){
			var transDelPage = flag == 'allDel' ? 'transSchdlAllDelAjx' : 'transSchdlDelAjx';
			$m.ajax('/wc/'+transDelPage+'.do?menuId=${menuId}&fncCal=${param.fncCal}', {schdlId:schdlId, fncCal:'${fncCal }'}, function(data) {
				if (data.message != null) {
					$m.dialog.alert(data.message);
				}
				if (data.result == 'ok') {
					$m.nav.next(event, '/wc/${listPage}.do?menuId=${menuId}&${paramsForList }');
				}
			});
		}
	});
}

<% // 목록으로 이동 %>
function goList() {
	$m.nav.prev(event, '/wc/${listPage }.do?menuId=${menuId}&${paramsForList}');
}

<% // 등록 %>
function setSchdl(schdlId){
	//$m.dialog.close('listSchdlPop');
	var url = "/wc/${setPage}.do?menuId=${menuId}";
	if(arguments.length > 1){
		var schdlStartDt = arguments[0]+''+getDayVal(arguments[1])+''+getDayVal(arguments[2]);
		if(arguments.length == 4) schdlStartDt+= ''+getDayVal(arguments[3].split(':')[0])+''+arguments[3].split(':')[1];
		url+= "&schdlStartDt="+schdlStartDt;
	}else{
		if(schdlId != null) {
			url+= "&schdlId="+schdlId;
		}	
	}
	if('${paramsForList}' != ''){
		url += "&${paramsForList}";	
	}
	$m.nav.next(event, url);
};

<% // 파일 다운로드 %>
function downFile(id,dispNm) {
	var ids = [];
	ids.push(id);
	var $form = $('<form>', {
			'method':'get',
			'action':'/wc/downFile.do',
			'target':$m.browser.mobile && $m.browser.safari ? '' : 'dataframe'
		}).append($('<input>', {
			'name':'menuId',
			'value':'${menuId}',
			'type':'hidden'
		})).append($('<input>', {
			'name':'fncCal',
			'value':'${param.fncCal}',
			'type':'hidden'
		})).append($('<input>', {
			'name':'schdlId',
			'value':'${wcSchdlBVo.schdlId}',
			'type':'hidden'
		})).append($('<input>', {
			'name':'fileIds',
			'value':ids,
			'type':'hidden'
		}));
	
	//if($m.browser.naver || $m.browser.daum){
	//	$form.append($('<input>', {'name':'fwd','value':$form.attr('action'),'type':'hidden'}));
	//	$form.attr('action', '/cm/download/wc/'+encodeURI(dispNm));
	//}
	
	$(top.document.body).append($form);
	$m.secu.set();
	$form.submit();
	$form.remove();
};
<c:if test="${viewYn eq 'Y'}">
<% // 문서뷰어 %>
function viewAttchFile(id) {
	var url = "/wc/attachViewSub.do?menuId=${menuId}&fncCal=${param.fncCal}&schdlId=${wcSchdlBVo.schdlId}";
	url+='&fileIds='+id;
	openViewAttchFile(url);
	//$m.nav.next(null, url);
}
</c:if>
//상세보기
function viewBc(bcId) {
	var authUrl = '${viewUrl}';
	var prefix = authUrl.indexOf('?') > -1 ? "&" : "?";
	authUrl+=prefix+"bcId="+bcId;
	$m.nav.next(event, authUrl);
};

$(document).ready(function() {
	$layout.adjustBodyHtml('bodyHtmlArea');
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('tabview');
});

//]]>
</script>
<c:choose>
	<c:when test="${userAuth == 'fail' }">
	<c:set var="promMod"	value= "A" />
	<c:set var="promDel"	value= "A" />
	</c:when>
	<c:when test="${userAuth == 'pass' }">
	<c:set var="promMod"	value= "W" />
	<c:set var="promDel"	value= "W" />
	</c:when>
	</c:choose>
<section>

       <!--btnarea S-->
       <div class="btnarea" id="btnArea">
           <div class="size">
           <dl>
			       <u:secu auth="${promMod }"><dd class="btn" onclick="setSchdl('${wcSchdlBVo.schdlId }');"><u:msg titleId="cm.btn.mod" alt="수정" /></dd></u:secu>
			       <u:secu auth="${promDel }"><dd class="btn" onclick="fnDelete('del','${wcSchdlBVo.schdlId }');"><u:msg titleId="cm.btn.del" alt="삭제" /></dd></u:secu>
			       <c:if test="${scdPidCount > 1 && wcSchdlBVo.repetYn eq 'Y'}">
			       	   <u:secu auth="${promDel }"><dd class="btn" onclick="fnDelete('allDel','${wcSchdlBVo.schdlId }');"><u:msg titleId="cm.btn.allDel" alt="전체삭제" /></dd></u:secu>
			       </c:if>
	       <dd class="btn" onclick="goList();"><u:msg titleId="cm.btn.list" alt="목록"  /></dd>
           <dd class="open" id="moreBtn" style="display:none" onclick="$layout.toggleBtns()"></dd>
        </dl>
      	 </div>
       </div>
       <!--//btnarea E-->
       
       <!--titlezone S-->
        <div class="titlezone">
            <div class="titarea">
            <dl>
            <dd class="tit">${wcSchdlBVo.subj}</dd>
            <dd class="name">
            	<c:if test="${wcSchdlBVo.solaLunaYn == 'N' }">(<u:msg titleId="wc.option.luna" alt="음력"/>)</c:if>
	        	<c:choose>
					<c:when test="${wcSchdlBVo.alldayYn eq 'Y' }">
						(<u:msg titleId="wc.cols.wholeDay" alt="종일일정"/>)
						<u:out value="${wcSchdlBVo.schdlStartDt }" type="date"/>~
						<u:out value="${wcSchdlBVo.schdlEndDt }" type="date"/>
					</c:when>
					<c:otherwise>
						<u:out value="${wcSchdlBVo.schdlStartDt }" type="longdate"/>~
						<u:out value="${wcSchdlBVo.schdlEndDt }" type="longdate"/>
					</c:otherwise>
				</c:choose>
            </dd>
         </dl>
            </div>
        </div>
        <!--//titlezone E-->

         <!--tabarea S-->
         <div class="tabarea" id="tabBtnArea">
             <div class="tabsize">
                 <dl>
                 <dd class="tab_on" onclick="$layout.tab.on($(this).attr('id'));" id="cont"><u:msg titleId="cols.body" alt="본문" /></dd>
                 <dd class="tab" onclick="$layout.tab.on($(this).attr('id'));" id="detail"><u:msg titleId="cm.btn.detl" alt="상세" /></dd>
                 <dd class="tab" onclick="$layout.tab.on($(this).attr('id'));" id="guest"><u:msg titleId="cols.guest" alt="참석자" /></dd>
                 <c:if test="${!empty fileVoList }">
                 	<dd class="tab" onclick="$layout.tab.on($(this).attr('id'));" id="attch"><u:msg titleId="cols.att" alt="첨부" /></dd>
                 </c:if>
              </dl>
             </div>
			<div class="tab_icol" style="display:none" id="toLeft"></div>
			<div class="tab_icor" style="display:none" id="toRight"></div>
         </div>
         <!--//tabarea E-->
		<div id="tabViewArea">
			<!--bodyzone_scroll S-->
			<div class="bodyzone_scroll" id="cont">
				<div class="bodyarea">
					<dl>
						<dd class="bodytxt_scroll"><div class="scroll editor" id="bodyHtmlArea"><div id="zoom"><u:out value="${wcSchdlBVo.cont}" type="noscript" /></div></div>
						</dd>
					</dl>
				</div>
			</div>
			<!--//bodyzone_scroll E-->
			<!--listtablearea S-->
			<div  class="s_tablearea" id="detail" style="display:none;">
				<div class="blank30"></div>
				<table class="s_table">
					<!-- <caption>타이틀</caption> -->
					<colgroup>
					<col width="33%"/>
					<col width=""/>
					</colgroup>
					<tbody>
					<tr>
						<th class="shead_lt"><u:msg titleId="cols.schdlKnd" alt="일정종류"/></th>
						<td class="shead_lt"><u:out value="${wcSchdlBVo.schdlTypNm }" /></td>
					</tr>
					<tr>
						<th class="shead_lt"><u:msg titleId="wc.cols.schdlKndCd" alt="일정대상"/></th>
						<td class="shead_lt">
							<c:choose>
								<c:when test="${wcSchdlBVo.schdlKndCd eq '1' }"><u:msg titleId="wc.jsp.listPsnSchdl.psn.title" alt="개인일정"/></c:when>
								<c:when test="${wcSchdlBVo.schdlKndCd eq '2' }">${wcSchdlBVo.grpNm }(<u:msg titleId="wc.jsp.listPsnSchdl.grp.title" alt="그룹일정"/>)</c:when>
								<c:when test="${wcSchdlBVo.schdlKndCd eq '3' }"><u:msg titleId="wc.jsp.listPsnSchdl.dept.title" alt="부서일정"/></c:when>
								<c:when test="${wcSchdlBVo.schdlKndCd eq '4' }"><u:msg titleId="wc.jsp.listPsnSchdl.comp.title" alt="회사일정"/></c:when>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="shead_lt"><u:msg titleId="cols.subj" alt="제목"/></th>
						<td class="shead_lt"><u:out value="${wcSchdlBVo.subj }" /></td>
					</tr>
					<tr>
						<th class="shead_lt"><u:msg titleId="cols.loc" alt="장소" /></th>
						<td class="shead_lt"><u:out value="${wcSchdlBVo.locNm }" /></td>
					</tr>
					<tr>
						<th class="shead_lt"><u:msg titleId="wc.cols.schdlPriod" alt="기간"/></th>
						<td class="shead_lt">
							<c:choose>
								<c:when test="${wcSchdlBVo.alldayYn eq 'Y' }">
									<u:out value="${wcSchdlBVo.schdlStartDt }" type="date"/>~
									<u:out value="${wcSchdlBVo.schdlEndDt }" type="date"/>
								</c:when>
								<c:otherwise>
									<u:out value="${wcSchdlBVo.schdlStartDt }" type="longdate"/>~
									<u:out value="${wcSchdlBVo.schdlEndDt }" type="longdate"/>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="shead_lt"><u:msg titleId="cols.publYn" alt="공개여부" /></th>
						<td class="shead_lt">
							<c:choose>
								<c:when test="${wcSchdlBVo.openGradCd eq '2'}"><u:msg titleId="cm.option.apntPubl" alt="지정인공개"/></c:when>
								<c:when test="${wcSchdlBVo.openGradCd eq '3'}"><u:msg titleId="cm.option.priv" alt="비공개"/></c:when>
								<c:when test="${wcSchdlBVo.openGradCd eq '4'}"><u:msg titleId="wc.cols.sel.dept" alt="부서선택"/></c:when>
								<c:otherwise><u:msg titleId="cm.option.publ" alt="공개"/></c:otherwise>
							</c:choose>
						</td>
					</tr>
					<c:if test="${param.fncCal eq 'dept' && !empty wcSchdlBVo.openGradCd && wcSchdlBVo.openGradCd eq '4'}">
					<tr>
						<th class="shead_lt"><u:msg titleId="wc.cols.sel.dept" alt="부서선택" /></th>
						<td class="shead_lt"><div id="deptSelectArea" style="min-height:40px;overflow-y:auto;"><c:forEach 
							var="wcSchdlDeptRVo" items="${wcSchdlDeptRVoList }" varStatus="status">
							<div class="ubox"><dl><dd 	class="title">${wcSchdlDeptRVo.orgNm }</dd></dl></div>
							</c:forEach></div></td>
					</tr>
					</c:if>
					<c:if test="${wcSchdlBVo.repetYn eq 'Y' && !empty wcRepetSetupDVo  }">
						<tr>
							<th class="shead_lt"><u:msg titleId="cols.repetKnd" alt="반복종류" /></th>
							<td class="shead_lt">
								<c:choose>
									<c:when test="${wcRepetSetupDVo.repetPerdCd eq 'EV_DY'}">
										<u:msg titleId="wc.option.daly" alt="일일" /> -
										<u:msg titleId="wc.jsp.setRepetPop.tx02" alt="매" /> ${wcRepetSetupDVo.repetDd} <u:msg titleId="wc.jsp.setRepetPop.tx03" alt="일마다" />
									</c:when>
									<c:when test="${wcRepetSetupDVo.repetPerdCd eq 'EV_WK'}">
										<u:msg titleId="wc.option.wely" alt="주간" /> -
										<u:msg titleId="wc.jsp.setRepetPop.tx02" alt="매" /> ${wcRepetSetupDVo.repetWk} <u:msg titleId="wc.jsp.setRepetPop.tx04" alt="주마다" />
										<c:if test="${!empty wcRepetSetupDVo.apntDy }">
											<c:if test="${fn:contains(wcRepetSetupDVo.apntDy,'SUN') }"><u:msg titleId="wc.option.sun" alt="일"/></c:if>
											<c:if test="${fn:contains(wcRepetSetupDVo.apntDy,'MON') }"><u:msg titleId="wc.option.mon" alt="월"/></c:if>
											<c:if test="${fn:contains(wcRepetSetupDVo.apntDy,'TUE') }"><u:msg titleId="wc.option.tue" alt="화"/></c:if>
											<c:if test="${fn:contains(wcRepetSetupDVo.apntDy,'WED') }"><u:msg titleId="wc.option.wed" alt="수"/></c:if>
											<c:if test="${fn:contains(wcRepetSetupDVo.apntDy,'THU') }"><u:msg titleId="wc.option.thu" alt="목"/></c:if>
											<c:if test="${fn:contains(wcRepetSetupDVo.apntDy,'FRI') }"><u:msg titleId="wc.option.fri" alt="금"/></c:if>
											<c:if test="${fn:contains(wcRepetSetupDVo.apntDy,'SAT') }"><u:msg titleId="wc.option.sat" alt="토"/></c:if>
										</c:if>
									</c:when>
									<c:when test="${wcRepetSetupDVo.repetPerdCd eq 'EV_DY_MY'}">
										<u:msg titleId="wc.option.moly" alt="월간" />
										<u:msg titleId="wc.jsp.setRepetPop.tx02" alt="매" />${wcRepetSetupDVo.repetMm }<u:msg titleId="wc.jsp.setRepetPop.tx05" alt="개월마다" />
										${wcRepetSetupDVo.apntDd }<u:msg titleId="wc.jsp.setRepetPop.tx07" alt="일" />
									</c:when>
									<c:when test="${wcRepetSetupDVo.repetPerdCd eq 'EV_WK_MT'}">
										<u:msg titleId="wc.option.moly" alt="월간" />
										<u:msg titleId="wc.jsp.setRepetPop.tx02" alt="매" />${wcRepetSetupDVo.repetMm }<u:msg titleId="wc.jsp.setRepetPop.tx05" alt="개월마다" />
										<c:choose>
											<c:when test="${wcRepetSetupDVo.apntWk eq '1'}"><u:msg titleId="wc.cols.firstWeek" alt="첫째주" /></c:when>
											<c:when test="${wcRepetSetupDVo.apntWk eq '2'}"><u:msg titleId="wc.cols.secondWeek" alt="둘째주" /></c:when>
											<c:when test="${wcRepetSetupDVo.apntWk eq '3'}"><u:msg titleId="wc.cols.thirdWeek" alt="셋째주" /></c:when>
											<c:when test="${wcRepetSetupDVo.apntWk eq '4'}"><u:msg titleId="wc.cols.fourWeek" alt="넷째주" /></c:when>
											<c:when test="${wcRepetSetupDVo.apntWk eq '5'}"><u:msg titleId="wc.cols.fiveWeek" alt="다섯째주" /></c:when>
										</c:choose>
										<c:choose>
											<c:when test="${wcRepetSetupDVo.apntDy eq '1'}"><u:msg titleId="wc.cols.sun" alt="일" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
											<c:when test="${wcRepetSetupDVo.apntDy eq '2'}"><u:msg titleId="wc.cols.mon" alt="월" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
											<c:when test="${wcRepetSetupDVo.apntDy eq '3'}"><u:msg titleId="wc.cols.tue" alt="화" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
											<c:when test="${wcRepetSetupDVo.apntDy eq '4'}"><u:msg titleId="wc.cols.wed" alt="수" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
											<c:when test="${wcRepetSetupDVo.apntDy eq '5'}"><u:msg titleId="wc.cols.thu" alt="목" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
											<c:when test="${wcRepetSetupDVo.apntDy eq '6'}"><u:msg titleId="wc.cols.fri" alt="금" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
											<c:when test="${wcRepetSetupDVo.apntDy eq '7'}"><u:msg titleId="wc.cols.sat" alt="토" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
										</c:choose>
									</c:when>
									<c:when test="${wcRepetSetupDVo.repetPerdCd eq 'EV_DY_YR'}">
										<u:msg titleId="wc.option.yely" alt="연간" />
										<u:msg titleId="wc.jsp.setRepetPop.tx08" alt="매년" />${wcRepetSetupDVo.repetMm }<u:msg titleId="wc.jsp.setRepetPop.tx06" alt="월" />
										${wcRepetSetupDVo.apntDd }<u:msg titleId="wc.jsp.setRepetPop.tx07" alt="일" />
									</c:when>
									<c:when test="${wcRepetSetupDVo.repetPerdCd eq 'EV_WK_YR'}">
										<u:msg titleId="wc.option.yely" alt="연간" />
										<u:msg titleId="wc.jsp.setRepetPop.tx08" alt="매년" />${wcRepetSetupDVo.repetMm }<u:msg titleId="wc.jsp.setRepetPop.tx06" alt="월" />
										<c:choose>
											<c:when test="${wcRepetSetupDVo.apntWk eq '1'}"><u:msg titleId="wc.cols.firstWeek" alt="첫째주" /></c:when>
											<c:when test="${wcRepetSetupDVo.apntWk eq '2'}"><u:msg titleId="wc.cols.secondWeek" alt="둘째주" /></c:when>
											<c:when test="${wcRepetSetupDVo.apntWk eq '3'}"><u:msg titleId="wc.cols.thirdWeek" alt="셋째주" /></c:when>
											<c:when test="${wcRepetSetupDVo.apntWk eq '4'}"><u:msg titleId="wc.cols.fourWeek" alt="넷째주" /></c:when>
											<c:when test="${wcRepetSetupDVo.apntWk eq '5'}"><u:msg titleId="wc.cols.fiveWeek" alt="다섯째주" /></c:when>
										</c:choose>
										<c:choose>
											<c:when test="${wcRepetSetupDVo.apntDy eq '1'}"><u:msg titleId="wc.cols.sun" alt="일" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
											<c:when test="${wcRepetSetupDVo.apntDy eq '2'}"><u:msg titleId="wc.cols.mon" alt="월" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
											<c:when test="${wcRepetSetupDVo.apntDy eq '3'}"><u:msg titleId="wc.cols.tue" alt="화" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
											<c:when test="${wcRepetSetupDVo.apntDy eq '4'}"><u:msg titleId="wc.cols.wed" alt="수" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
											<c:when test="${wcRepetSetupDVo.apntDy eq '5'}"><u:msg titleId="wc.cols.thu" alt="목" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
											<c:when test="${wcRepetSetupDVo.apntDy eq '6'}"><u:msg titleId="wc.cols.fri" alt="금" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
											<c:when test="${wcRepetSetupDVo.apntDy eq '7'}"><u:msg titleId="wc.cols.sat" alt="토" /><u:msg titleId="wc.cols.dayOfWeek" alt="요일" /></c:when>
										</c:choose>
									</c:when>
								</c:choose>
							</td>
						</tr>
						<tr>
							<th class="shead_lt"><u:msg titleId="cols.repetPrd" alt="반복기간" /></th>
							<td class="shead_lt"><u:out value="${wcRepetSetupDVo.repetStartDt }" type="date"/>~<u:out value="${wcRepetSetupDVo.repetEndDt }" type="date"/>	</td>
						</tr>
					</c:if>
				</tbody>
			</table>
		</div>
		
		<div class="listarea" id="guest" style="display:none;"><!-- 참석자 목록 -->
			<div class="blank30"></div>
			<article>
				<c:choose>
					<c:when test="${!empty wcPromGuestDVoList }">
						<c:forEach var="list" items="${wcPromGuestDVoList}" varStatus="status">
							<u:set var="viewFunc" test="${list.guestEmplYn eq 'Y' }" value="$m.user.viewUserPop" elseValue="viewBc"/>
							<div class="listdiv" onclick="${viewFunc }('${list.guestUid }');">
		                        <div class="list">
		                        <dl>
		                        <dd class="tit"><u:out value="${list.guestNm}" /> | <u:msg titleId="${list.guestEmplYn eq 'Y' ? 'cm.option.empl' : 'wc.option.frnd'}" alt="지인"/></dd>
		                        <dd class="body">${list.guestCompNm } ${!empty list.guestCompNm && !empty list.email ? '|' : ''} ${list.email }</dd>
			                    </dl>
		                        </div>
		                    </div>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<div class="listdiv_nodata" >
	                    <dl>
	                    <dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd>
	                    </dl>
	                    </div>
					</c:otherwise>
				</c:choose>
			</article>
		</div>
		
		<!--//listtablearea E-->
        <c:if test="${!empty fileVoList }">
		<div class="attachzone" id="attch" style="display:none;">
		<div class="blank30"></div>
			<div class="attacharea">
				<c:forEach items="${fileVoList}" var="fileVo" varStatus="status">
					<m:attach fileName="${fileVo.dispNm}" fileKb="${fileVo.fileSize/1024 }" downFnc="downFile('${fileVo.fileId}','${fileVo.dispNm}');" viewFnc="viewAttchFile('${fileVo.fileId}');"/>
				</c:forEach>
			</div>
         </div>  
         </c:if>
       </div>
       <jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />      
</section>


