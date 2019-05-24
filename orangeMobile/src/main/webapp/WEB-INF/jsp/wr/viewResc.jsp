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

<% // [상단버튼:등록] 등록 %>
function setViewRezv(){
	var url = '/wr/setRezv.do?menuId=${menuId}&listPage=listResc';
	url+='&paramRescKndId=${wrRescMngBVo.rescKndId }';
	url+='&paramRescMngId=${wrRescMngBVo.rescMngId }';
	url+='&paramCompId=${param.paramCompId }';
	$m.nav.next(event, url);
};

function goList(){
	$m.nav.prev(event, '/wr/listResc.do?menuId=${menuId}&paramCompId=${param.paramCompId}');
};

jQuery.fn.center = function () {
    this.css("position","absolute");
    this.css("top", Math.max(0, (($(window).height() - $(this).outerHeight()) / 2) + $(window).scrollTop()) + "px");
    this.css("left", Math.max(0, (($(window).width() - $(this).outerWidth()) / 2) + $(window).scrollLeft()) + "px");
    return this;
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
           <dd class="btn" onclick="setViewRezv();"><u:msg titleId="wr.btn.rescRezv" alt="자원예약" /></dd>
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
            <dd class="tit">${wrRescMngBVo.rescNm }</dd>
            <dd class="name">${wrRescMngBVo.kndNm }</dd>
         </dl>
            </div>
        </div>
        <!--//titlezone E-->

         <!--tabarea S-->
         <div class="tabarea" id="tabBtnArea">
             <div class="tabsize">
                 <dl>
                 <dd class="tab_on" onclick="javascript:fnTabOn(this,'cont');"><u:msg titleId="cols.cont" alt="내용" /></dd>
                 <c:if test="${!empty wrRescMngBVo.wrRescImgDVo}">
                 	<dd class="tab" onclick="javascript:fnTabOn(this,'photo');"><u:msg titleId="cols.cmImg" alt="이미지" /></dd>
                 </c:if>
              </dl>
             </div>
			<div class="tab_icol" style="display:none" id="toLeft"></div>
			<div class="tab_icor" style="display:none" id="toRight"></div>
         </div>
         <!--//tabarea E-->

		<div id="tabViewArea">
	        <!--listtablearea S-->
	        <div  class="s_tablearea view contCls" >
	        	<div class="blank30"></div>
	            <table class="s_table">
	            <!-- <caption>타이틀</caption> -->
	            <colgroup>
	                <col width="33%"/>
	                <col width=""/>
	            </colgroup>
	            <tbody>
					<tr>
						<th class="shead_lt"><u:msg titleId="cols.rescLoc" alt="자원위치" /></th>
						<td class="shead_lt"><div class="ellipsis" title="${wrRescMngBVo.rescLoc }" >${wrRescMngBVo.rescLoc }</div></td>
					</tr>
					<tr>
						<th class="shead_lt"><u:msg titleId="cols.suplStat" alt="비품현황" /></th>
						<td class="shead_lt"><div style="overflow:auto;height:50px;">${wrRescMngBVo.suplStat }</div></td>
					</tr>
					<tr>
						<th class="shead_lt"><u:msg titleId="cols.rescAdm" alt="자원관리자" /></th>
						<td class="sbody_lt"><a href="javascript:$m.user.viewUserPop('${wrRescMngBVo.rescAdmUid }');">${wrRescMngBVo.rescAdmNm }</a></td>
					</tr>
					<tr>
						<th class="shead_lt"><u:msg titleId="wr.cols.discYn" alt="심의여부" /></th>
						<td class="shead_lt"><u:msg titleId="wr.option.disc${wrRescMngBVo.discYn }" alt="사용여부"/></td>
					</tr>
	            </tbody>
	            </table>
	        </div>
	        <!--//listtablearea E-->
	           
	         <c:if test="${!empty wrRescMngBVo.wrRescImgDVo}">
	           <div class="bodyzone view photoCls" style="display:none;">
	               <div class="bodyarea">
	               <dl>
	               <dd class="bodytxt" >
	               		<div id="userPhoto" onclick="$(this).hide()" style="background:url('${wrRescMngBVo.wrRescImgDVo.imgPath}') no-repeat 50% 0; background-size:contain; position:absolute; left:6px; right:6px; height:500px; display:none; z-index:999;"></div>
	               		<c:set var="wrRescImgDVo" value="${wrRescMngBVo.wrRescImgDVo}" />
						<c:set var="maxHght" value="125" />
						<u:set test="${wrRescImgDVo == null || wrRescImgDVo.imgHght > maxHght}" var="imgHght" value="${maxHght}" elseValue="${wrRescImgDVo.imgHght}" />
	               		<a href="javascript:$('#userPhoto').show();" style="border:none;">
						<c:if test="${wrRescImgDVo.imgWdth > 800}"	>
							<img src="${_cxPth}${wrRescImgDVo.imgPath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' height="${imgHght }px">
						</c:if>
						<c:if test="${wrRescImgDVo.imgWdth <= 800}">
							<img src="${_cxPth}${wrRescImgDVo.imgPath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' height="${imgHght }px">
						</c:if>
						</a>
	               </dd>
	            </dl>
	               </div>
	           </div>
			</c:if>
		</div>
		<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
</section>

