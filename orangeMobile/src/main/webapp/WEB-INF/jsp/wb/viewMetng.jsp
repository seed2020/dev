<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set var="agntParam" test="${!empty schBcRegrUid }" value="&schBcRegrUid=${schBcRegrUid }" elseValue=""/>
<u:secu auth="W" ownerUid="${listPage eq 'listMetng' ? wbBcMetngDVo.regrUid : ''}"><u:set test="${true}" var="writeAuth" value="Y"/></u:secu>
<script type="text/javascript">
//<![CDATA[
<% // 목록으로 이동 %>
function goList() {
	$m.nav.prev(event, '/wb/${listPage }.do?${paramsForList}${agntParam }');
};

function fnMod(){
	$m.nav.next(event, '/wb/${setPage }.do?${paramsForList}&bcMetngDetlId=${wbBcMetngDVo.bcMetngDetlId }${agntParam }');
};


//삭제
function fnDelete(){
	$m.msg.confirmMsg("cm.cfrm.del", null, function(result){
		if(result){
			$m.ajax('/wb/${transDelPage }.do?menuId=${menuId}', {delList:'${wbBcMetngDVo.bcMetngDetlId}', schBcRegrUid:'${schBcRegrUid }'}, function(data) {
				if (data.message != null) {
					$m.dialog.alert(data.message);
				}
				if (data.result == 'ok') {
					$m.nav.next(event, '/wb/${listPage}.do?${paramsForList}${agntParam }');
				}
			});
		}
	});
};

<% // 파일 다운로드 %>
function downFile(id,dispNm) {
	var ids = [];
	ids.push(id);
	var $form = $('<form>', {
			'method':'get',
			'action':'/wb/downFile.do',
			'target':$m.browser.mobile && $m.browser.safari ? '' : 'dataframe'
		}).append($('<input>', {
			'name':'menuId',
			'value':'${menuId}',
			'type':'hidden'
		})).append($('<input>', {
			'name':'actionParam',
			'value':'metng',
			'type':'hidden'
		})).append($('<input>', {
			'name':'bcMetngDetlId',
			'value':'${param.bcMetngDetlId}',
			'type':'hidden'
		})).append($('<input>', {
			'name':'fileIds',
			'value':ids,
			'type':'hidden'
		}));
	//if($m.browser.naver || $m.browser.daum){
	//	$form.append($('<input>', {'name':'fwd','value':$form.attr('action'),'type':'hidden'}));
	//	$form.attr('action', '/cm/download/wb/'+encodeURI(dispNm));
	//}
	
	$(top.document.body).append($form);
	$m.secu.set();
	$form.submit();
	$form.remove();
};
<c:if test="${viewYn eq 'Y'}">
<% // 문서뷰어 %>
function viewAttchFile(id) {
	var url = "/wb/attachViewSub.do?menuId=${menuId}&actionParam=metng&bcMetngDetlId=${param.bcMetngDetlId}";
	url+='&fileIds='+id;
	openViewAttchFile(url);
	//$m.nav.next(null, url);
}
</c:if>
function viewBc(bcId) {
	$m.nav.next(event, "/wb/viewBc.do?menuId=${menuId}&bcId="+bcId);
};

$(document).ready(function() {
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('tabview');
});

//]]>
</script>

<section>

       <!--btnarea S-->
       <div class="btnarea" id="btnArea">
           <div class="size">
           <dl>
           <c:if test="${( listPage eq 'listMetng' && ( empty param.schOpenYn || param.schOpenYn eq 'N' ) ) || wbBcAgntAdmBVo.authCd eq 'RW'}">
				<c:if test="${writeAuth == 'Y'}">
			       <dd class="btn" onclick="fnMod();"><u:msg titleId="cm.btn.mod" alt="수정" /></dd>
			       <dd class="btn" onclick="fnDelete();"><u:msg titleId="cm.btn.del" alt="삭제" /></dd>
		       </c:if>
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
            <dd class="tit">${wbBcMetngDVo.metngSubj}</dd>
            <dd class="name">${wbBcMetngDVo.metngYmd}${!empty wbBcMetngDVo.metngClsNm ? 'ㅣ':''}${wbBcMetngDVo.metngClsNm}</dd>
         </dl>
            </div>
        </div>
        <!--//titlezone E-->
        
         <!--tabarea S-->
         <div class="tabarea" id="tabBtnArea">
             <div class="tabsize">
                 <dl>
                 <dd class="tab_on" onclick="$layout.tab.on($(this).attr('id'));" id="cont"><u:msg titleId="wb.jsp.setBc.tab.dftInfo" alt="기본정보" /></dd>
                 <dd class="tab" onclick="$layout.tab.on($(this).attr('id'));" id="detail"><u:msg titleId="cols.guest" alt="참석자" /></dd>
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
        <!--listtablearea S-->
        <div  class="s_tablearea" id="cont">
        	<div class="blank30"></div>
            <table class="s_table">
            <!-- <caption>타이틀</caption> -->
            <colgroup>
                <col width="33%"/>
                <col width=""/>
            </colgroup>
            <tbody>
                <tr>
                    <th class="shead_lt"><u:msg titleId="wb.cols.bc" alt="명함" /></th>
                    <td class="sbody_lt">
                    	<a href="javascript:viewBc('${wbBcMetngDVo.bcId}');">${wbBcMetngDVo.bcNm}</a>
					</td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.metngDt" alt="관련미팅일시" /></th>
                    <td class="shead_lt">
                    	${wbBcMetngDVo.metngYmd}
					</td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.comp" alt="회사" /></th>
                    <td class="shead_lt">
                    	${wbBcMetngDVo.compNm}
					</td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.secul" alt="보안등급" /></th>
                    <td class="shead_lt">
                    	<u:msg titleId="cm.option.${wbBcMetngDVo.openYn eq 'Y' ? 'publ' : 'priv'}" alt="보안등급" />
					</td>
                </tr>
                 <tr>
                    <th class="shead_lt"><u:msg titleId="wb.cols.cls" alt="분류" /></th>
                    <td class="shead_lt">
                    	${wbBcMetngDVo.metngClsNm }
					</td>
                </tr>
                 <tr>
                    <th class="shead_lt"><u:msg titleId="cols.cont" alt="내용" /></th>
                    <td class="shead_lt">
                    	${wbBcMetngDVo.metngCont}
					</td>
                </tr>
            </tbody>
            </table>
        </div>
        <!--//listtablearea E-->   
         
         <div class="listarea" id="detail" style="display:none;"><!-- 참석자 목록 -->
			<div class="blank30"></div>
			<article id="apvLnUiArea">
				<c:choose>
					<c:when test="${!empty wbBcMetngAtndRVoList }">
						<c:forEach var="list" items="${wbBcMetngAtndRVoList}" varStatus="status">
							<u:set var="viewFunc" test="${list.emplTypCd eq 'EMPL' }" value="$m.user.viewUserPop" elseValue="viewBc"/>
							<div class="listdiv" onclick="${viewFunc }('${list.emplId }');">
		                        <div class="list">
		                        <dl>
		                        <dd class="tit"><u:out value="${list.emplNm}" /> | <u:msg titleId="${list.emplTypCd eq 'FRND' ? 'wc.option.frnd' : (list.emplTypCd eq 'EMPL' ? 'cm.option.empl' : 'ct.option.etc')}" alt="지인"/></dd>		                        
		                        <dd class="body">${list.compNm } ${!empty list.compNm && !empty list.emplPhon ? '|' : ''} <u:out value="${list.emplPhon }"/></dd>
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
        
		<div class="attachzone"  id="attch" style="display:none;">
			<div class="blank30"></div>
			<div class="attacharea">
				<c:if test="${!empty fileVoList }">
					<c:forEach items="${fileVoList}" var="fileVo" varStatus="status">
						<m:attach fileName="${fileVo.dispNm}" fileKb="${fileVo.fileSize/1024 }" downFnc="downFile('${fileVo.fileId}','${fileVo.dispNm}');" viewFnc="viewAttchFile('${fileVo.fileId}');"/>
					</c:forEach>
				</c:if>
			</div>
         </div>  
       </div> 
         
    <jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
    
</section>
<!--//section E-->
        
        