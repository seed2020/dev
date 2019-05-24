<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:secu auth="W"><u:set test="${true}" var="writeAuth" value="Y"/></u:secu>
<script type="text/javascript">
//<![CDATA[

function fnTabOn(obj,val){
	$(".view").each(function(){$(this).hide();});
	$(".view."+val+"Cls").each(function(){$(this).show();});
	$(".tabarea dd").each(function(){$(this).attr("class", "tab");});
	$(obj).attr("class","tab_on");
};


<% // [하단버튼:승인] %>
function apvdBull() {
	submitForm('bb.cnfm.apvd','B');
}
<% // [하단버튼:반려] %>
function rjtBull() {
	if($('#rjtOpin').val() == ''){
		$m.msg.alertMsg('cm.input.check.mandatory','<u:msg titleId="cols.rjtOpin"  />');
		return;
	}
	submitForm('bb.cnfm.rjt','J');
}
<% // submit form %>
function submitForm(cnfmMsg,cd) {
	$m.msg.confirmMsg(cnfmMsg,null, function(result){
		if(result)
		{
			$m.ajax('/bb/transDiscBull.do?menuId=${menuId}', {bullId:'${param.bullId}',rjtOpin:$('#rjtOpin').val(),brdId:'${baBrdBVo.brdId}',bullStatCd:cd,bullRezvDt:'${bbBullLVo.bullRezvDt}'}, function(data) {
				if (data.message != null) {
					$m.dialog.alert(data.message);
				}
				if (data.result == 'ok') {
					$m.nav.next(null, '/bb/listDiscBull.do?${params}');
				}
			});
		}
	});
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
	var url = "/bb/attachViewSub.do?menuId=${menuId}&bullId=${param.bullId}";
	url+='&fileIds='+id;
	openViewAttchFile(url);
	//$m.nav.next(null, url);
}
</c:if>

<% // [이전글/다음글] 게시물 심의 %>
function discBull(id) {
	$m.nav.next(null, '/bb/setDiscBull.do?${params}&bullId=' + id);
}

<% // [하단버튼:목록] 목록으로 이동 %>
function goList() {
	$m.nav.prev(null, '/bb/listDiscBull.do?${params}');
}

$(document).ready(function() {
	$layout.adjustBodyHtml('bodyHtmlArea');
});
//]]>
</script>

<!--section S-->
<section>

    <!--btnarea S-->
    <div class="btnarea" id="btnArea">
        <div class="size">
        <dl>
        <c:if test="${writeAuth == 'Y' }">
	        <dd class="btn" onclick="apvdBull();"><u:msg titleId="cm.btn.apvd" alt="승인" /></dd>
	        <dd class="btn" onclick="rjtBull();"><u:msg titleId="cm.btn.rjt" alt="반려" /></dd>
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
        <dd class="tit">${bbBullLVo.subj}</dd>
        <dd class="name">
        	<u:out value="${bbBullLVo.deptNm}" />ㅣ<u:out value="${bbBullLVo.regrNm}" />ㅣ<u:out value="${bbBullLVo.regDt}" type="longdate" />
        </dd>
     	</dl>
        </div>
    </div>
    <!--//titlezone E-->
        
     <!--tabarea S-->
     <div class="tabarea" id="tabBtnArea">
     	 <div class="blank25"></div>
         <div class="tabsize">
             <dl>
             <dd class="tab_on" onclick="javascript:fnTabOn(this,'cont');"><u:msg titleId="cols.body" alt="본문" /></dd>
             <dd class="tab" onclick="javascript:fnTabOn(this,'detail');"><u:msg titleId="cm.btn.detl" alt="상세" /></dd>
             <c:if test="${!empty fileVoList }">
             	<dd class="tab" onclick="javascript:fnTabOn(this,'attch');"><u:msg titleId="cols.att" alt="첨부" /></dd>
             </c:if>
          </dl>
         </div>
		<div class="tab_icol" style="display:none" id="toLeft"></div>
		<div class="tab_icor" style="display:none" id="toRight"></div>
     </div>
     <!--//tabarea E-->
    

     <div class="bodyzone_scroll view contCls">
	     <div class="bodyarea">
		     <dl>
		     <dd class="bodytxt_scroll"><div class="scroll editor" id="bodyHtmlArea">
		     	<div>
				<!-- 게시물사진 -->
				<c:if test="${baBrdBVo.photoYn == 'Y'}">
				<c:if test="${bbBullLVo.photoVo != null}">
				<c:set var="maxWdth" value="100" />
				<u:set test="${bbBullLVo.photoVo.imgWdth <= maxWdth}" var="imgWdth" value="${bbBullLVo.photoVo.imgWdth}" elseValue="${maxWdth}" />
				<img src="${_cxPth}${bbBullLVo.photoVo.savePath}" width="${imgWdth}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"'>
				</c:if>
				</c:if>
				<u:clob lobHandler="${lobHandler }"/>
				</div>
		     </div></dd>
		     </dl>
		 </div>    
     </div>
     
    <!--entryzone S-->
    <div class="entryzone view contCls">
    <div class="blank30"></div>
        <div class="entryarea">
        <dl>
        <dd class="etr_bodytit"><u:msg titleId="cols.rjtOpin" alt="반려의견" /></dd>
        <dd class="etr_input"><div class="etr_textareain"><textarea id="rjtOpin" class="etr_ta" rows="3" /></textarea></div></dd>
        </dl>
        </div>      
    </div>
    <!--//entryzone E-->

     <!--listtablearea S-->
     <div class="s_tablearea view detailCls" style="display:none;">
     	<div class="blank30"></div>
         <table class="s_table">
         <!-- <caption>타이틀</caption> -->
         <colgroup>
             <col width="33%"/>
             <col width=""/>
         </colgorup>
         <tbody>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.regr" alt="등록자" /></th>
                    <td class="shead_lt"><a href="javascript:$m.user.viewUserPop('${bbBullLVo.regrUid}');"><u:out value="${bbBullLVo.regrNm}" /></a></td>
                </tr>
           		<u:set test="${brdNms != null}" var="brdNms" value="${brdNms}" elseValue="${baBrdBVo.rescNm}" />
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.bb" alt="게시판" /></th>
                    <td class="shead_lt"> ${brdNms}</td>
                </tr>
				
				<c:if test="${baBrdBVo.catYn == 'Y'}">
	                <tr>
	                    <th class="shead_lt"><u:msg titleId="cols.cat" alt="카테고리" /></th>
	                    <td class="shead_lt"> ${bbBullLVo.catNm}</td>
	                </tr>
				</c:if>
	
	             <tr>
                    <th class="shead_lt"><u:msg titleId="bb.cols.bbOpt" alt="게시옵션" /></th>
                    <td class="shead_lt"> 
						<%-- <u:checkArea>
						<u:checkbox name="secuYn" value="Y" titleId="bb.option.secu" alt="보안" inputClass="bodybg_lt" checkValue="${bbBullLVo.secuYn}" disabled="Y" />
						<u:checkbox name="ugntYn" value="Y" titleId="bb.option.ugnt" alt="긴급" inputClass="bodybg_lt" checkValue="${bbBullLVo.ugntYn}" disabled="Y" />
						<u:checkbox name="notcYn" value="Y" titleId="bb.option.notc" alt="공지" inputClass="bodybg_lt" checkValue="${bbBullLVo.notcYn}" disabled="Y" />
						<u:input type="hidden" id="secuYn" value="${bbBullLVo.secuYn}" />
						<u:input type="hidden" id="ugntYn" value="${bbBullLVo.ugntYn}" />
						<u:input type="hidden" id="notcYn" value="${bbBullLVo.notcYn}" />
						</u:checkArea> --%>
                    </td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.bullRezvDt" alt="게시예약일" /></th>
                    <td class="shead_lt"> <u:out value="${bbBullLVo.bullRezvDt}" type="longdate" /></td>
                </tr>
                
            </tbody>
            </table>
        </div>
        <!--//listtablearea E-->
                
			<div class="attachzone view attchCls"  style="display:none;">
				<div class="attacharea">
					<c:if test="${!empty fileVoList }">
						<c:forEach items="${fileVoList}" var="fileVo" varStatus="status">
							<m:attach fileName="${fileVo.dispNm}" fileKb="${fileVo.fileSize/1024 }" downFnc="downFile('${fileVo.fileId}','${fileVo.dispNm}');" viewFnc="viewAttchFile('${fileVo.fileId }');"/>
						</c:forEach>
					</c:if>
				</div>
          	</div>  
                

     
		<% // 이전글 다음글 %>
		<c:if test="${prevBullVo != null || nextBullVo != null}">
        	<div class="prevnextarea">
                <div class="prev">
                	<dl>
					<c:if test="${prevBullVo == null}">
						<dd class="tit"><u:msg titleId="cm.ico.prev" alt="이전글" /></dd>
						<dd class="body"><u:msg titleId="bb.jsp.viewBull.prevNotExists" alt="이전글이 존재하지 않습니다." /></dd>
					</c:if>
					<c:if test="${prevBullVo != null}">
						<dd class="tit"><u:msg titleId="cm.ico.prev" alt="이전글" /></dd>
						<dd class="body" onclick="javascript:discBull('${prevBullVo.bullId}');">
						<u:out value="${prevBullVo.subj}" maxLength="80" />
							<%-- <c:if test="${baBrdBVo.cmtYn == 'Y'}">(<u:out value="${prevBullVo.cmtCnt}" type="number" />)</c:if>
						<a href="javascript:viewUserPop('${prevBullVo.regrUid}');">${prevBullVo.regrNm}</a>
						<u:out value="${prevBullVo.regDt}" type="longdate" /> --%>
						</dd>
					</c:if>
					</dl>
				</div>
				<div class="next">
					<dl>
					<c:if test="${nextBullVo == null}">
						<dd class="tit"><u:msg titleId="cm.ico.next" alt="다음글" /></dd>
						<dd class="body"><u:msg titleId="bb.jsp.viewBull.nextNotExists" alt="다음글이 존재하지 않습니다." /></dd>
					</c:if>
					<c:if test="${nextBullVo != null}">
						<dd class="tit"><u:msg titleId="cm.ico.next" alt="다음글" /></dd>
						<dd class="body" onclick="javascript:discBull('${nextBullVo.bullId}');">
						<u:out value="${nextBullVo.subj}" maxLength="80" />
							<%-- <c:if test="${baBrdBVo.cmtYn == 'Y'}">(<u:out value="${nextBullVo.cmtCnt}" type="number" />)</c:if>
						<a href="javascript:viewUserPop('${nextBullVo.regrUid}');">${nextBullVo.regrNm}</a>
						<u:out value="${nextBullVo.regDt}" type="longdate" /> --%>
						</dd>
					</c:if>
					</dl>
				</div>
       	 	</div>
		</c:if>
		
	 <jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
    
</section>
<!--//section E-->
