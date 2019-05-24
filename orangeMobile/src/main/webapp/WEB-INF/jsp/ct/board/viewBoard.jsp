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

<% // [하단버튼:답변] 답변 %>
function setReply() {
	$m.nav.next(event, '/ct/board/setReply.do?menuId=${menuId}&ctId=${ctId}&bullPid=${ctBullMastBVo.bullId}');
}
<% // [하단버튼:수정] 수정 %>
function modBull() {
	$m.nav.next(event, '/ct/board/setBoard.do?menuId=${menuId}&ctId=${ctId}&bullId=${ctBullMastBVo.bullId}&bullPid=${ctBullMastBVo.bullPid}');
}
<% // [하단버튼:삭제] 삭제 %>
function delBull() {
		$m.dialog.confirm('<u:msg titleId="cm.cfrm.del" alt="삭제하시겠습니까?"  />', function(result){
			if(result){
				$m.ajax('/ct/board/transBullDel.do?menuId=${menuId}&ctId=${ctId}', {bullId : '${ctBullMastBVo.bullId}'}, function(data) {
					if (data.message != null) {
						$m.dialog.alert(data.message);
					}
					if (data.result == 'ok') {
						$m.nav.next(event, '/ct/board/listBoard.do?menuId=${menuId}&ctId=${ctId}');
					}
				});
			}
		});
}
<% // [하단버튼:목록] 목록으로 이동 %>
function goList() {
	$m.nav.prev(event, '/ct/board/listBoard.do?menuId=${menuId}&ctId=${ctId}');
}

<% // [등록] 한줄답변 등록 %>
function regCmt() {
	var cmt = $('#cmt').val();
	if ($.trim(cmt) == '') {
		$m.dialog.alert('<u:msg titleId="bb.msg.cmt.required" />');<% // bb.msg.cmt.required=한줄답변을 입력하세요. %>
		$('#cmt').focus();
		return;
	}
	$m.ajax('/ct/board/transCmtAjx.do?menuId=${menuId}&ctId=${ctId}&bullId=${param.bullId}', {bullId:'${param.bullId}', cmt:cmt}, function(data) {
		if (data.message != null) {
			$m.dialog.alert(data.message);
		}
		if (data.result == 'ok') {
			$('#cmt').val("");
			getCmtList();
		}
	});
}

function getCmtList(){
	$m.ajax('/ct/board/listCmtAjx.do?menuId=${menuId}&ctId=${ctId}&bullId=${param.bullId}', {bullId:'${param.bullId}'}, function(data) {
		$("#cmtList").html("");
		$("#cmtCnt").text(data.list.length);
		if(data.list.length>0){
			var cmtList = [];
			cmtList.push('<div class="listreplyarea">');
	    	 $.each(data.list , function(index, vo) {
	    		var obj = JSON.parse(JSON.stringify(vo)); 
	    		/* (data.list.length-obj.rnum+1) */
	            cmtList.push('<div class="listreply">');
	            cmtList.push('<dl>');
	            cmtList.push('<dd class="replytit"><a href="javascript:$m.user.viewUserPop(\''+obj.regrUid+'\');">'+obj.regrNm+'</a></dd>');
	            cmtList.push('<dd class="replybody">'+obj.cmt+'</dd>');
	            cmtList.push('<dd class="replyname">'+obj.regDt+'</dd>');
	            cmtList.push('<dd><div class="reply_btnarea">');
	            cmtList.push('        <div class="size">');
	            cmtList.push('        <dl>');
	            cmtList.push('        <dd class="btn" onclick="delCmt(\''+obj.cmtId+'\');"><u:msg titleId="cm.btn.del" alt="삭제" /></dd>');
	            cmtList.push('        </dl>');
	            cmtList.push('        </div>');
	            cmtList.push('    </div>');
	            cmtList.push('</dd>');
	            cmtList.push('</dl>');
	            cmtList.push('</div>');
	            cmtList.push('<div class="line"></div>'); 

	     	}); 
	    	 cmtList.push('</div>');
	    	 $("#cmtList").append(cmtList.join(''));
		}else{
        	$("#cmtList").append('<div class="entryzone"><div class="entryarea"><dl><dd class="etr_input"><div class="etr_body_gray">※한줄 답변이 없습니다.</div></dd></dl></div></div>');
		}
	});
}

<% // [삭제] 한줄답변 삭제 %>
function delCmt(id) {
	<% // cm.cfrm.del=삭제하시겠습니까? %>
	$m.dialog.confirm("<u:msg titleId="cm.cfrm.del" />", function(result){
		if(result){
			$m.ajax('/ct/board/transCmtDelAjx.do?menuId=${menuId}&ctId=${ctId}&bullId=${param.bullId}', {bullId:'${param.bullId}', cmtId:id}, function(data) {
				if (data.message != null) {
					$m.dialog.alert(data.message);
				}
				if (data.result == 'ok') {
					getCmtList();
				}
			});
		}
	});
}

<% // 추천 %>
function recmdBull() {
	<% // bb.cfrm.recmd=추천하시겠습니까? %>
	$m.msg.confirmMsg("bb.cfrm.recmd", null, function(result){
		if(result)
		{
			$m.ajax('/ct/board/transBullRecmdAjx.do?menuId=${menuId}&ctId=${param.ctId}', {bullId:'${param.bullId}'}, function(data) {
				if (data.message != null) {
					$m.dialog.alert(data.message);
				}
				if (data.result == 'ok') {
					$('#recmdCntTd').html(data.recmdCnt);
				}
			});
		}
	});	
}


function saveScre() {
	if (${screHstExist}) {
		$m.msg.alertMsg("bb.msg.scre.already");<% // bb.msg.scre.already=이미 점수를 준 게시물입니다. %>
		return;
	}

	if ($('#scre').val() == '') {
		$m.msg.alertMsg("bb.msg.scre.notChecked");<% // bb.msg.scre.notChecked=점수를 선택하세요. %>
		return;
	}
	$m.ajax('/ct/board/transBullScreAjx.do?menuId=${menuId}&ctId=${param.ctId}', {bullId:'${param.bullId}', scre:$('#scre').val()}, function(data) {
		if (data.message != null) {
			$m.dialog.alert(data.message);
		}
	});
}

<% // 점수내역 %>
function viewScre() {
	$m.dialog.open({
		id:'viewScrePop',
		title:'<u:msg titleId="bb.jsp.viewScrePop.title" alt="점수내역" />',
		url:'/ct/board/viewScrePop.do?menuId=${menuId}&ctId=${param.ctId}&bullId=${param.bullId}&brdId=${param.brdId}',
	});
}

function fnScreSelect(){
	/* var screBoxPos = $(".view.etcCls dd.etr_se_lt").offset();
	var wrapScrollTop = parseInt($(".wrapscroll").scrollTop());
	var screSelectTop = parseInt(screBoxPos.top)-3 + (wrapScrollTop>0?wrapScrollTop+1:0);
	$(".view.etcCls div.etr_open2").css("top",screSelectTop+"px"); */
	$(".view.etcCls div.etr_open2").show();
}

function fnSetScre(cd)
{
	$('#scre').val(cd);
	$('.view.etcCls dd.etr_se_lt span').text($(".view.etcCls div.etr_open2 dd[data-schCd='"+cd+"']").text());
	$(".view.etcCls div.etr_open2").hide();
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
			'name':'ctId',
			'value':'${param.ctId}',
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
	//	$form.attr('action', '/cm/download/ct/'+encodeURI(dispNm));
	//}
	
	$(top.document.body).append($form);
	$m.secu.set();
	$form.submit();
	$form.remove();
}
<c:if test="${viewYn eq 'Y'}">
<% // 문서뷰어 %>
function viewAttchFile(id) {
	var url = "/ct/attachViewSub.do?menuId=${menuId}&ctId=${param.ctId}&bullId=${param.bullId}";
	url+='&fileIds='+id;
	openViewAttchFile(url);
	//$m.nav.next(null, url);
}
</c:if>
<% // [목록:제목] 게시물 조회 %>
function viewBull(id) {
	$m.nav.next(event, '/ct/board/viewBoard.do?menuId=${menuId}&ctId=${ctId}&bullId=' + id);
}

function readHst(id) {
	/* $m.dialog.open({
		id:'listReadHstPop',
		title:'<u:msg titleId="bb.jsp.listReadHstPop.title" alt="조회자 정보" />',
		url:'/ct/board/listReadHstPop.do?menuId=${menuId}&ctId=${param.ctId}&brdId=${param.brdId}&bullId=' + id,
	}); */
	$m.nav.next(event, '/ct/board/listReadHstSub.do?menuId=${menuId}&ctId=${param.ctId}&brdId=${param.brdId}&bullId=' + id);
}

var holdHide = false;
$(document).ready(function() {
	fnSetScre('');
	$("input[type='text'], textarea").each(function(){
		$space.apply(this, {relative:30});
	});
	$layout.adjustBodyHtml('bodyHtmlArea');
	$('body:first').on('click', function(){
		if(holdHide) holdHide = false;
		else $(".view.etcCls div.etr_open2").hide();
	});
	
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
				      	<dd class="btn" onclick="setReply();"><u:msg titleId="bb.btn.reply" alt="답변" /></dd>
				       	<dd class="btn" onclick="modBull();"><u:msg titleId="cm.btn.mod" alt="수정" /></dd>
				      	<dd class="btn" onclick="delBull();"><u:msg titleId="cm.btn.del" alt="삭제" /></dd>
				    </c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${!empty authChkD && authChkD == 'D' }">
								<dd class="btn" onclick="setReply();"><u:msg titleId="bb.btn.reply" alt="답변" /></dd>
								<c:if test="${ctBullMastBVo.regrUid != logUserUid}">
									<dd class="btn" onclick="delBull();"><u:msg titleId="cm.btn.del" alt="삭제" /></dd>
								</c:if>
							</c:when>
							<c:otherwise>
								<c:if test="${!empty authChkW && authChkW == 'W' }">
									<dd class="btn" onclick="setReply();"><u:msg titleId="bb.btn.reply" alt="답변" /></dd>
								</c:if>
							</c:otherwise>
						</c:choose>
						<c:if test="${ctBullMastBVo.regrUid == logUserUid}">
				       	<dd class="btn" onclick="modBull();"><u:msg titleId="cm.btn.mod" alt="수정" /></dd>
				      	<dd class="btn" onclick="delBull();"><u:msg titleId="cm.btn.del" alt="삭제" /></dd>
						</c:if>
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
            <dd class="tit">${ctBullMastBVo.subj}</dd>
            <dd class="name">
            	<u:out value="${ctBullMastBVo.regrNm}" />ㅣ<u:out value="${ctBullMastBVo.regDt}" type="longdate" />ㅣ
				<u:out value="${ctBullMastBVo.readCnt}" type="number" />
            </dd>
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
                 <c:if test="${!empty fileVoList }">
                 	<dd class="tab" onclick="javascript:fnTabOn(this,'attch');"><u:msg titleId="cols.att" alt="첨부" /></dd>
                 </c:if>
					<dd class="tab" onclick="javascript:fnTabOn(this,'cmt');"><u:msg titleId="cols.cmt" alt="한줄답변" />(<span id="cmtCnt">${ctBullMastBVo.cmtCnt}</span>)</dd>
				 <c:if test="${fn:length(replyBullList) > 1}">
					<dd class="tab" onclick="javascript:fnTabOn(this,'rel');"><u:msg titleId="mbb.rel" alt="관련글" /></dd>
				 </c:if>
					<dd class="tab" onclick="javascript:fnTabOn(this,'etc');"><u:msg titleId="mbb.etc" alt="기타" /></dd>

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
						<!-- 게시물사진 -->
						<c:if test="${baBrdBVo.photoYn == 'Y'}">
							<c:if test="${ctBullMastBVo.photoVo != null}">
							<c:set var="maxWdth" value="800" />
							<u:set test="${ctBullMastBVo.photoVo.imgWdth <= maxWdth}" var="imgWdth" value="${ctBullMastBVo.photoVo.imgWdth}" elseValue="${maxWdth}" />
								<img src="${_cxPth}${ctBullMastBVo.photoVo.savePath}" width="${imgWdth}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"'>
							</c:if>
						</c:if>
						<u:clob lobHandler="${lobHandler }"/>
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
           
           

		<div class="view cmtCls" style="display:none;">
           <div class="blank20"></div>
           <div class="entryzone">
                <div class="entryarea">
                <dl>
                <dd class="etr_bodytit">
                    <div class="icotit_dot"><u:msg titleId="cols.cmt" alt="한줄답변" /></div>
                    <div class="icoarea">
                    <dl>
                    <dd class="btn" onclick="regCmt();"><u:msg titleId="cm.btn.reg" alt="등록" /></dd>
                    </dl>
                    </div>
                </dd>
                <dd class="etr_input"><div class="etr_textareain"><textarea rows="5" class="etr_ta" id="cmt"></textarea></div></dd>
                </dl>
                </div>                
            </div>


            <div class="blankzone">
                <div class="blank25"></div>
                <div class="line1"></div>
                <div class="line8"></div>
                <div class="line1"></div>
                <div class="blank13"></div>
            </div>

			<div id="cmtList">
					<c:if test="${fn:length(ctCmtDVoList) == 0}">
			            <div class="entryzone">
			                <div class="entryarea">
			                <dl>		                
			                <dd class="etr_input"><div class="etr_body_gray">※<u:msg titleId="bb.msg.noCmt" alt="한줄답변이 없습니다." /></div></dd>
			                </dl>
			                </div>                
			            </div>
					</c:if>
					<c:if test="${fn:length(ctCmtDVoList) > 0}">
					<div class="listreplyarea">
					<c:forEach items="${ctCmtDVoList}" var="baCmtDVo" varStatus="status">
						<%-- <u:out value="${recodeCount - baCmtDVo.rnum + 1}" type="number" /> --%>
		                <div class="listreply">
		                <dl>
		                <dd class="replytit"><a href="javascript:$m.user.viewUserPop('${baCmtDVo.regrUid}');"><u:out value="${baCmtDVo.regrNm}" /></a></dd>
		                <dd class="replybody"><u:out value="${baCmtDVo.cmt}" /></dd>
		                <dd class="replyname"><u:out value="${baCmtDVo.regDt}" type="longdate" /></dd>
		                <dd><div class="reply_btnarea">
		                        <div class="size">
		                        <dl>
		                        <dd class="btn" onclick="delCmt('${baCmtDVo.cmtId}');"><u:msg titleId="cm.btn.del" alt="삭제" /></dd>
			                    </dl>
		                        </div>
		                    </div>
		                </dd>
			            </dl>
		                </div>
						<div class="line"></div>
					</c:forEach>
					</div>
					</c:if>
			</div>	
		</div>


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
            		<th class="shead_lt"><u:msg titleId="cols.subj" alt="제목" /></th>
                    <td class="shead_lt"><u:out value="${ctBullMastBVo.subj}" /></td>
            	</tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.regr" alt="등록자" /></th>
                    <td class="sbody_lt"><a href="javascript:$m.user.viewUserPop('${ctBullMastBVo.regrUid}');"><u:out value="${ctBullMastBVo.regrNm}" /></a></td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.readCnt" alt="조회수" /></th>
                    <td class="sbody_lt"><a href="javascript:readHst('${ctBullMastBVo.bullId}');"><u:out value="${ctBullMastBVo.readCnt}" type="number" /></a></td>
                </tr>      

                <tr>
                    <th class="shead_lt"><u:msg titleId="ct.cols.exprDt" alt="만료일시" /></th>
                    <td class="shead_lt">
						<fmt:parseDate var="dateTempParse" value="${ctBullMastBVo.bullExprDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
						<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd HH:mm:ss"/>
                    </td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.regDt" alt="등록일시" /></th>
                    <td class="shead_lt"><u:out value="${ctBullMastBVo.regDt}" type="longdate" /></td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.modDt" alt="수정일시" /></th>
                    <td class="shead_lt"><u:out value="${ctBullMastBVo.modDt}" type="longdate" /></td>
                </tr>
            </tbody>
            </table>
        </div>
        <!--//listtablearea E-->        

		 <c:if test="${fn:length(replyBullList) > 1}">
			<div class="listarea view relCls" style="display:none;">
				<div class="blank30"></div>
				<article>
	            <c:forEach items="${replyBullList}" var="bullVo" varStatus="status">
	            	<u:set test="${ctBullMastBVo.bullId == bullVo.bullId}" var="listdiv" value="listdivline" elseValue="listdiv"/>
					<div class="${listdiv }">
					<u:set var="replyYn" test="${bullVo.replyDpth > 0 }" value="Y" elseValue="N"/>
	          	    <c:if test="${replyYn eq 'Y' }"><div class="listcheck_comment"><dl><dd class="comment"></dd></dl></div></c:if>
	                <div class="list${replyYn eq 'Y' ? '_comment' : '' }">
					<dl>
						<dd class="tit" onclick="javascript:viewBull('${bullVo.bullId}');">
							<u:out value="${bullVo.subj}" maxLength="80" />
							<c:if test="${baBrdBVo.cmtYn == 'Y'}">(<u:out value="${bullVo.cmtCnt}" type="number" />)</c:if>
						</dd>
						<dd class="name"><a href="javascript:$m.user.viewUserPop('${bullVo.regrUid}');">${bullVo.regrNm}</a> / <u:out value="${bullVo.regDt}" type="longdate" /></dd>
					</dl>
					</div>
					</div>
                 </c:forEach>
	           </article>
	        </div>
		 </c:if>
		 

            <!--entryzone S-->
            <div class="entryzone view etcCls" style="display:none;">
                <div class="entryarea">
                <dl>
	            	
		                <dd class="etr_tit"><u:msg titleId="cols.recmdCnt" alt="추천수" /></dd>
		                <dd class="etr_input">
		                    <div class="etr_ipmany">
		                    <dl>
		                    <dd class="wblank5"></dd>
		                    <dd class="etr_body"><strong><span id="recmdCntTd">${ctBullMastBVo.recmdCnt}</span></strong></dd>
		                    <dd class="wblank3"></dd>
		                    <c:if test="${recmdHstExist == false}">
		                    	<dd onclick="javascript:recmdBull();"><div class="etr_btn"><u:msg titleId="bb.btn.recmd" alt="추천" /></div></dd>
		                    </c:if>
		                    </dl>
		                    </div>
		                </dd>	
	              
	            	
	                
		                <dd class="etr_tit"><u:msg titleId="bb.cols.saveScre" alt="점수주기" /></dd>
		                <dd class="etr_input">
		                    <div class="etr_ipmany">
		                    <dl>
		                    <dd class="etr_se_lt">
		                    
		                        <div class="select_in1" onclick="holdHide = true;fnScreSelect();">
		                        <dl>
		                        <dd class="select_txt_o"><span></span></dd>
		                        <dd class="select_btn"></dd>
		                        </dl>
		                        </div>

		                        <input type="hidden" name="scre" id="scre" value=""/>
		                        <div class="etr_open2" style="display:none">
		                            <div class="open_in1">
		                                <div class="open_div">
		                                <dl>
						                    <dd class="txt_o" onclick="javascript:fnSetScre('');" data-schCd=""><u:msg titleId="bb.msg.scre.notChecked" alt="점수를 선택하세요."/></dd>
						                    <dd class="line"></dd>
						                    <dd class="txt_o" onclick="javascript:fnSetScre('1');" data-schCd="1">★ ☆ ☆ ☆ ☆</dd>
						                    <dd class="line"></dd>
						                    <dd class="txt_o" onclick="javascript:fnSetScre('2');" data-schCd="2">★ ★ ☆ ☆ ☆</dd>
						                    <dd class="line"></dd>
						                    <dd class="txt_o" onclick="javascript:fnSetScre('3');" data-schCd="3">★ ★ ★ ☆ ☆</dd>
						                    <dd class="line"></dd>
						                    <dd class="txt_o" onclick="javascript:fnSetScre('4');" data-schCd="4">★ ★ ★ ★ ☆</dd>
						                    <dd class="line"></dd>
						                    <dd class="txt_o" onclick="javascript:fnSetScre('5');" data-schCd="5">★ ★ ★ ★ ★</dd>
						                    <dd class="line"></dd>
			                            </dl>
		                                </div>
		                            </div>
		                        </div>

		                    </dd>
		                    <dd class="etr_se_rt">
		                    	<c:if test="${screHstExist == false}">
		                    	<div class="etr_btn" onclick="saveScre();"><u:msg titleId="cm.btn.save" alt="저장" /></div>
		                    	</c:if>
		                    	<div class="etr_btn" onclick="viewScre();"><u:msg titleId="bb.btn.viewScre" alt="점수내역" /></div>
		                    </dd>
		                    </dl>
		                    </div>
		                  </dd>
	      			 
                
	            </dl>
                </div>
			</div>
		</div>

		<% // 이전글 다음글 %>
        	<div class="prevnextarea">
                <div class="prev">
                	<dl>
					<c:if test="${prevBullVo == null}">
						<dd class="tit"><u:msg titleId="cm.ico.prev" alt="이전글" /></dd>
						<dd class="body"><u:msg titleId="bb.jsp.viewBull.prevNotExists" alt="이전글이 존재하지 않습니다." /></dd>
					</c:if>
					<c:if test="${prevBullVo != null}">
						<dd class="tit"><u:msg titleId="cm.ico.prev" alt="이전글" /></dd>
						<dd class="body" onclick="javascript:viewBull('${prevBullVo.bullId}');">
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
						<dd class="body" onclick="javascript:viewBull('${nextBullVo.bullId}');">
						<u:out value="${nextBullVo.subj}" maxLength="80" />
							<%-- <c:if test="${baBrdBVo.cmtYn == 'Y'}">(<u:out value="${nextBullVo.cmtCnt}" type="number" />)</c:if>
						<a href="javascript:viewUserPop('${nextBullVo.regrUid}');">${nextBullVo.regrNm}</a>
						<u:out value="${nextBullVo.regDt}" type="longdate" /> --%>
						</dd>
					</c:if>
					</dl>
				</div>
       	 	</div>


<jsp:include page="/WEB-INF/jsp/ct/inc/footerInc.jsp" flush="false" />
    
</section>
<!--//section E-->

