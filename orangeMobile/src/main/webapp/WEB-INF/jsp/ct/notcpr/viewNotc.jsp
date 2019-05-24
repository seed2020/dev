<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[

function fnTabOn(obj,val){
	$(".view").each(function(){$(this).hide();});
	$(".view."+val+"Cls").each(function(){$(this).show();});
	$(".tabarea dd").each(function(){$(this).attr("class", "tab");});
	$(obj).attr("class","tab_on");
};


<% // [하단버튼:목록] 목록으로 이동 %>
function goList() {
	$m.nav.prev(event, '/ct/notc/listNotc.do?menuId=${menuId}');
}

<% // 파일 다운로드 %>
function downFile(id,dispNm) {
	var ids = [];
	ids.push(id);
	var $form = $('<form>', {
			'method':'get',
			'action':'/bb/downFile.do',
			'target':$m.browser.mobile && $m.browser.safari ? '' : 'dataframe'
		}).append($('<input>', {
			'name':'menuId',
			'value':'${menuId}',
			'type':'hidden'
		})).append($('<input>', {
			'name':'brdId',
			'value':'${param.brdId}',
			'type':'hidden'
		})).append($('<input>', {
			'name':'bullId',
			'value':'${param.bullId}',
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
	var url = "/ct/attachViewSub.do?menuId=${menuId}&brdId=${param.brdId}&bullId=${param.bullId}";
	url+='&fileIds='+id;
	openViewAttchFile(url);
	//$m.nav.next(null, url);
}
</c:if>
$(document).ready(function() {
	$layout.adjustBodyHtml('bodyHtmlArea');
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
            <dd class="tit">${ctAdmNotcBVo.subj}</dd>
            <dd class="name"><a href="javascript:$m.user.viewUserPop('${ctAdmNotcBVo.regrUid}');"><u:out value="${ctAdmNotcBVo.regrNm}" /></a>ㅣ<u:out value="${ctAdmNotcBVo.regDt}" type="longdate" />ㅣ<u:out value="${ctAdmNotcBVo.bullExprDt}" type="longdate" />ㅣ<u:out value="${ctAdmNotcBVo.readCnt}" type="number" /></dd>
         </dl>
            </div>
        </div>
        <!--//titlezone E-->

         <!--tabarea S-->
         <div class="tabarea" id="tabBtnArea">
             <div class="tabsize">
                 <dl>
                 <dd class="tab_on" onclick="javascript:fnTabOn(this,'cont');"><u:msg titleId="cols.body" alt="본문" /></dd>
                 <c:if test="${!empty fileVoList }">
                 	<dd class="tab" onclick="javascript:fnTabOn(this,'attch');"><u:msg titleId="cols.att" alt="첨부" /></dd>
                 </c:if>
              </dl>
             </div>
			<div class="tab_icol" style="display:none" id="toLeft"></div>
			<div class="tab_icor" style="display:none" id="toRight"></div>
         </div>
         <!--//tabarea E-->
			

            <!--bodyzone_scroll S-->
            <div class="bodyzone_scroll view contCls">
                <div class="bodyarea">
                <dl>
                <dd class="bodytxt_scroll"><div class="scroll editor" id="bodyHtmlArea">
                		<div id="zoom">
							<u:out value="${ctAdmNotcBVo.cont}" type="noscript" />
						</div>
                </div>
                </dd>
	            </dl>
                </div>
            </div>
            <!--//bodyzone_scroll E-->
	       

			<div class="attachzone view attchCls"  style="display:none;">
			<div class="blank30"></div>
				<div class="attacharea">
					<c:if test="${!empty fileVoList }">
						<c:forEach items="${fileVoList}" var="fileVo" varStatus="status">
							<m:attach fileName="${fileVo.dispNm}" fileKb="${fileVo.fileSize/1024 }" downFnc="downFile('${fileVo.fileId}','${fileVo.dispNm}');" viewFnc="viewAttchFile('${fileVo.fileId}');"/>
						</c:forEach>
					</c:if>
				</div>
          	</div>  
           
<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
    
</section>
<!--//section E-->

