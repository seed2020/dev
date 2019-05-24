<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:secu auth="M" ownerUid="${bbBullLVo.regrUid}"><u:set test="${true}" var="modAuth" value="Y"/></u:secu>
<u:secu auth="A" ownerUid="${bbBullLVo.regrUid}"><u:set test="${true}" var="delAuth" value="Y"/></u:secu>
<script type="text/javascript">
//<![CDATA[

function fnTabOn(obj,val){
	$(".view").each(function(){$(this).hide();});
	$(".view."+val+"Cls").each(function(){$(this).show();});
	$(".tabarea dd").each(function(){$(this).attr("class", "tab");});
	$(obj).attr("class","tab_on");
};

<% // [하단버튼:수정] 수정 %>
function modBull() {
	$m.nav.next(event, '/ct/pr/setPr.do?menuId=${menuId}&bullId=${ctPrBVo.bullId}');
}
<% // [하단버튼:삭제] 삭제 %>
function delBull() {
	$m.dialog.confirm('<u:msg titleId="cm.cfrm.del" alt="삭제하시겠습니까?"  />', function(result){
		if(result){
			$m.ajax('/ct/pr/transPrDel.do?menuId=${menuId}&bullId=${ctPrBVo.bullId}', {bullId:'${ctPrBVo.bullId}'}, function(data) {
				if (data.message != null) {
					$m.dialog.alert(data.message);
				}
				if (data.result == 'ok') {
					$m.nav.next(event, '/ct/pr/listPr.do?menuId=${menuId}');
				}
			});
		}
	});
}
<% // [하단버튼:목록] 목록으로 이동 %>
function goList() {
	$m.nav.prev(event, '/ct/pr/listPr.do?menuId=${menuId}');
}

<% // 파일 다운로드 %>
function downFile(id,dispNm) {
	var ids = [];
	ids.push(id);
	var $form = $('<form>', {
			'method':'get',
			'action':'/ct/downFile.do',
			'target':$m.browser.mobile && $m.browser.safari ? '' : 'dataframe'
		}).append($('<input>', {
			'name':'menuId',
			'value':'${menuId}',
			'type':'hidden'
		})).append($('<input>', {
			'name':'bullId',
			'value':'${ctPrBVo.bullId}',
			'type':'hidden'
		})).append($('<input>', {
			'name':'fileIds',
			'value':ids,
			'type':'hidden'
		}));
	
	//if($m.browser.naver || $m.browser.daum){
	//	$form.append($('<input>', {'name':'fwd','value':$form.attr('action'),'type':'hidden'}));
	//	$form.attr('action', '/cm/download/bb/'+encodeURI(dispNm));
	//}
	
	$(top.document.body).append($form);
	$m.secu.set();
	$form.submit();
	$form.remove();
}

<c:if test="${viewYn eq 'Y'}">
<% // 문서뷰어 %>
function viewAttchFile(id) {
	var url = "/ct/attachViewSub.do?menuId=${menuId}&bullId=${param.bullId}";
	url+='&fileIds='+id;
	openViewAttchFile(url);
	//$m.nav.next(null, url);
}
</c:if>

$(document).ready(function() {
	$layout.adjustBodyHtml('bodyHtmlArea');
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('tabview');
});
//]]>
</script>


<!--section S-->
<section>

       <!--btnarea S-->
       <div class="btnarea" id="btnArea">
       	   <div class="blank25"></div>
           <div class="size">
           <dl>
			<c:if test="${prRegr == 'prRegr' || admin == 'admin'}">
	       	<dd class="btn" onclick="modBull();"><u:msg titleId="cm.btn.mod" alt="수정" /></dd>
	      	<dd class="btn" onclick="delBull();"><u:msg titleId="cm.btn.del" alt="삭제" /></dd>
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
            <dd class="tit">${ctPrBVo.subj}</dd>
            <dd class="name"><a href="javascript:$m.user.viewUserPop('${ctPrBVo.regrUid}');"><u:out value="${ctPrBVo.regrNm}" /></a>ㅣ<u:out value="${ctPrBVo.regDt}" type="longdate" />ㅣ<u:out value="${ctPrBVo.readCnt}" type="number" /></dd>
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
                <dd class="bodytxt_scroll"><div class="scroll editor" id="bodyHtmlArea">
                		<div id="zoom">
						<u:out value="${ctPrBVo.cont}" type="noscript" />
						</div>
                </div>
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
						<th class="shead_lt"><u:msg titleId="cols.subj" alt="제목"/></th>
						<td class="sbody_lt"><u:out value="${ctPrBVo.subj}" /></td>
					</tr>
					<tr>
						<th class="shead_lt"><u:msg titleId="ct.cols.cm" alt="커뮤니티" /></th>
						<td class="sbody_lt"><u:out value="${ctEstbBVo.ctNm}" /></td>
					</tr>
					<tr>
						<th class="shead_lt"><u:msg titleId="cols.regDt" alt="등록일시" /></th>
						<td class="sbody_lt"><u:out value="${ctPrBVo.regDt}" type="longdate" /></td>
					</tr>
					<tr>
						<th class="shead_lt"><u:msg titleId="cols.modDt" alt="수정일시" /></th>
						<td class="sbody_lt"><u:out value="${ctPrBVo.modDt}" type="longdate" /></td>
					</tr>
					</tbody>
				</table>
			</div>
			<!--listtablearea E-->
		
			<div class="attachzone" id="attch" style="display:none;">
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

