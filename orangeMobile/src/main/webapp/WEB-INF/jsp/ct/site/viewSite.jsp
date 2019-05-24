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

function modSite() {
	$m.nav.next(event, '/ct/site/setSite.do?menuId=${menuId}&ctId=${ctId}&siteId=${siteId}&catId=${catId}');
}

function delSite() {
	var selectCtSiteIds = [];
	selectCtSiteIds.push('${siteId}');
	$m.dialog.confirm('<u:msg titleId="cm.cfrm.del" alt="삭제하시겠습니까?"  />', function(result){
		if(result){
			$m.ajax('/ct/site/transSiteListDel.do?menuId=${menuId}&ctId=${ctId}', {selectCtSiteIds:selectCtSiteIds}, function(data) {
				if (data.message != null) {
					$m.dialog.alert(data.message);
				}
				if (data.result == 'ok') {
					$m.nav.next(event, '/ct/site/listSite.do?menuId=${menuId}&ctId=${ctId}');
				}
			});
		}
	});
}

function goList() {
	$m.nav.prev(event, '/ct/site/listSite.do?menuId=${menuId}&ctId=${ctId}');
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
				       	<dd class="btn" onclick="modSite();"><u:msg titleId="cm.btn.mod" alt="수정" /></dd>
				      	<dd class="btn" onclick="delSite();"><u:msg titleId="cm.btn.del" alt="삭제" /></dd>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${ctSiteBVo.regrUid == logUserUid}">
				       	<dd class="btn" onclick="modSite();"><u:msg titleId="cm.btn.mod" alt="수정" /></dd>
				      	<dd class="btn" onclick="delSite();"><u:msg titleId="cm.btn.del" alt="삭제" /></dd>						
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${!empty authChkD && authChkD == 'D' }">
									<dd class="btn" onclick="delSite();"><u:msg titleId="cm.btn.del" alt="삭제" /></dd>						
								</c:when>
							</c:choose>
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
            <dd class="tit">${ctSiteBVo.subj}</dd>
            <dd class="name">
            	<u:out value="${ctSiteBVo.catNm}"/> / ${ctSiteBVo.regrNm} /
				<fmt:parseDate var="dateTempParse" value="${ctSiteBVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
            </dd>
            <dd class="name"><a href="${ctSiteBVo.url}" target="_blank">${ctSiteBVo.url}</a></dd>
         	</dl>
            </div>
        </div>
        <!--//titlezone E-->

         <!--tabarea S-->
         <div class="tabarea" id="tabBtnArea">
             <div class="tabsize">
                 <dl>
                 <dd class="tab_on" onclick="javascript:fnTabOn(this,'cont');"><u:msg titleId="cols.body" alt="본문" /></dd>
				 <dd class="tab" onclick="javascript:fnTabOn(this,'detail');"><u:msg titleId="cm.btn.detl" alt="상세" /></dd>

              </dl>
             </div>
			<div class="tab_icol" style="display:none" id="toLeft"></div>
			<div class="tab_icor" style="display:none" id="toRight"></div>
         </div>
         <!--//tabarea E-->
		<div id="tabViewArea">	

            <!--bodyzone_scroll S-->
            <div class="bodyzone_scroll view contCls">
                <div class="bodyarea">
                <dl>
                <dd class="bodytxt_scroll"><div class="scroll editor" id="bodyHtmlArea">
                		<div id="zoom">
						<u:out value="${ctSiteBVo.cont}" type="noscript" />
						</div>
                </div>
                </dd>
	            </dl>
                </div>
            </div>
            <!--//bodyzone_scroll E-->
	       
	       <!--listtablearea S-->
        <div  class="s_tablearea view detailCls" style="display:none;">
        	<div class="blank30"></div>
            <table class="s_table">
            <!-- <caption>타이틀</caption> -->
            <colgroup>
                <col width="33%"/>
                <col width=""/>
            </colgroup>
            <tbody>
            	<tr>
            		<th class="shead_lt"><u:msg titleId="cols.siteNm" alt="사이트명" /></th>
                    <td class="shead_lt"><u:out value="${ctSiteBVo.subj}" /></td>
            	</tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.regr" alt="등록자" /></th>
                    <td class="shead_lt"><a href="javascript:$m.user.viewUserPop('${ctSiteBVo.regrUid}');"><u:out value="${ctSiteBVo.regrNm}" /></a></td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.cat" alt="카테고리" /></th>
                    <td class="shead_lt"><u:out value="${ctSiteBVo.catNm}" /></td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.siteUrl" alt="Site URL" /></th>
                    <td class="sbody_lt"><a href="${ctSiteBVo.url}" target="_blank"><u:out value="${ctSiteBVo.url}" /></a></td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.regDt" alt="등록일시" /></th>
                    <td class="shead_lt"><u:out value="${ctSiteBVo.regDt}" type="longdate" /></td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.modDt" alt="수정일시" /></th>
                    <td class="shead_lt"><u:out value="${ctSiteBVo.modDt}" type="longdate" /></td>
                </tr>
            </tbody>
            </table>
        </div>
        <!--//listtablearea E-->       

		</div>
<jsp:include page="/WEB-INF/jsp/ct/inc/footerInc.jsp" flush="false" />
    
</section>
<!--//section E-->

