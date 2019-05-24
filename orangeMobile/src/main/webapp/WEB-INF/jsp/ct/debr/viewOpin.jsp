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
}

function goList() {
	$m.nav.prev(event, '/ct/debr/listOpin.do?menuId=${menuId}&ctId=${ctId}&debrId=${ctDebrOpinDVo.debrId}');
}

<%// 의견삭제 %>
function delOpin(debrId, opinOrdr) {
	$m.dialog.confirm('<u:msg titleId="cm.cfrm.del" alt="삭제하시겠습니까?"  />', function(result){
		if(result){
			$m.ajax('/ct/debr/transOpinDel.do?menuId=${menuId}&ctId=${ctId}', {debrId:debrId, opinOrdr:opinOrdr}, function(data) {
				if (data.message != null) {
					$m.dialog.alert(data.message);
				}
				if (data.result == 'ok') {
					$m.nav.next(event, '/ct/debr/listOpin.do?menuId=${menuId}&ctId=${ctId}&debrId='+debrId);
				}
			});
		}
	});
}

<%// 의견수정 %>
function modOpin(debrId, opinOrdr) {
	$m.nav.next(event, '/ct/debr/setOpin.do?menuId=${menuId}&ctId=${ctId}&debrId='+debrId+'&opinOrdr='+opinOrdr);
}

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
           <div class="size">
           <dl>
           
			<c:choose>
				<c:when test="${!empty myAuth && myAuth == 'M' }">
					<dd class="btn" onclick="modOpin('${ctDebrOpinDVo.debrId}','${ctDebrOpinDVo.opinOrdr}');"><u:msg titleId="cm.btn.mod" alt="수정"  /></dd>
					<dd class="btn" onclick="delOpin('${ctDebrOpinDVo.debrId}','${ctDebrOpinDVo.opinOrdr}');"><u:msg titleId="cm.btn.del" alt="삭제"  /></dd>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${ctDebrOpinDVo.regrUid == logUserUid}">
					<dd class="btn" onclick="modOpin('${ctDebrOpinDVo.debrId}','${ctDebrOpinDVo.opinOrdr}');"><u:msg titleId="cm.btn.mod" alt="수정"  /></dd>
					<dd class="btn" onclick="delOpin('${ctDebrOpinDVo.debrId}','${ctDebrOpinDVo.opinOrdr}');"><u:msg titleId="cm.btn.del" alt="삭제"  /></dd>
						</c:when>
						<c:otherwise>
							<c:if test="${!empty authChkD && authChkD == 'D' }">
								<dd class="btn" onclick="delOpin('${ctDebrOpinDVo.debrId}','${ctDebrOpinDVo.opinOrdr}');"><u:msg titleId="cm.btn.del" alt="삭제"  /></dd>
							</c:if>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>

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
            <dd class="tit"><u:out value="${ctDebrOpinDVo.subj}"/></dd>
            <dd class="name">
            	<c:choose>
					<c:when test="${ctDebrOpinDVo.prosConsCd == 'A'}">
						<u:msg titleId="ct.option.for" alt="찬성" />
					</c:when>
					<c:when test="${ctDebrOpinDVo.prosConsCd == 'O'}">
						<u:msg titleId="ct.option.against" alt="반대" />
					</c:when>
					<c:when test="${ctDebrOpinDVo.prosConsCd == 'E'}">
						<u:msg titleId="ct.option.etc" alt="기타" />
					</c:when>
				</c:choose>
            </dd>
         </dl>
            </div>
        </div>
        <!--//titlezone E-->
        
        <div class="blank10"></div>
        <!--//titarea E-->
        <div class="entryzone">
        <div class="entryarea">
        <dl>
        <dd class="etr_tit"><u:msg titleId="cols.cont" alt="내용" /></dd>
        <dd class="etr_input"><div class="etr_bodyline scroll editor" id="bodyHtmlArea"><div id="zoom"><u:out value="${ctDebrOpinDVo.opin}" type="noscript" /></div></div></dd>
    	</dl>
    	</div>
    	<div class="blank5"></div>                
    </div>
    <!--//entryzone E-->
         
<jsp:include page="/WEB-INF/jsp/ct/inc/footerInc.jsp" flush="false" />
    
</section>
