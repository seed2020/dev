<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="contYn" test="${colmMap.cont.readDispYn eq 'Y' || (!empty isOnlyMd && isOnlyMd==true)}" value="Y" elseValue="N"/>
<u:secu auth="M" ownerUid="${bbBullLVo.regrUid}"><u:set test="${true}" var="modAuth" value="Y"/></u:secu>
<u:secu auth="A" ownerUid="${bbBullLVo.regrUid}"><u:set test="${true}" var="delAuth" value="Y"/></u:secu>
<u:secu auth="A"><u:set test="${true}" var="delCmtAuth" value="Y"/></u:secu>
<u:secu auth="W"><u:set test="${true}" var="replyAuth" value="Y"/></u:secu>
<script type="text/javascript">
//<![CDATA[


function fnTabOn(obj,val){
	$(".view").each(function(){$(this).hide();});
	$(".view."+val+"Cls").each(function(){$(this).show();});
	$(".tabarea dd").each(function(){$(this).attr("class", "tab");});
	$(obj).attr("class","tab_on");
};

<% // [하단버튼:답변] 답변 %>
function setReply() {
	$m.nav.next(null, '/bb/setReply.do?${params}&bullPid=${param.bullId}');
}
<% // [하단버튼:수정] 수정 %>
function modBull() {
	$m.nav.next(null, '/bb/setBull.do?${params}&bullId=${param.bullId}');
}<% // [등록:상단버튼] - 웹버전 등록화면 팝업 출력 %>
function modBullWeb() {
	var param={};
	param['mdCd']='BB'; // 모듈코드
	param['mdRefId']='${baBrdBVo.brdId}'; // 게시판ID
	param['mdNo']='${param.bullId}'; // 게시글번호
	param['mode']='set';
	$m.ajax('/bb/getWorksUrlAjx.do?menuId=${menuId}', param, function(data) {
		if(data.message !=null){
			$m.dialog.alert(data.message);
		}
		if(data.webUrl!=null) {
			var url=data.webUrl;
			url+=url.indexOf('?') > -1 ? "&" : "?";
			url+="isMobile=Y";
			window.open(url, "modBullWin");
		}
	});
}<% // [새로고침] - 팝업 등록화면에서 저장후 새로고침 %>
function reloadOpen(){
	$m.nav.reload();
}<% // [팝업] - 웹버전 보기 %>
function viewBullWeb(){
	var param={};
	param['mdCd']='BB'; // 모듈코드
	param['mdRefId']='${baBrdBVo.brdId}'; // 게시판ID
	param['mdNo']='${param.bullId}'; // 게시글번호
	$m.ajax('/bb/getWorksUrlAjx.do?menuId=${menuId}', param, function(data) {
		if(data.message !=null){
			$m.dialog.alert(data.message);
		}
		if(data.webUrl!=null) {
			var url=data.webUrl;
			url+=url.indexOf('?') > -1 ? "&" : "?";
			url+="isMobile=Y";
			window.open(url, "viewBullWin");
		}
	});
}
<% // [하단버튼:삭제] 삭제 %>
function delBull() {
		$m.ajax('/bb/transBullDelAjxChk.do?menuId=${menuId}&bullId=${param.bullId}&brdId=${baBrdBVo.brdId}', {brdId:'${baBrdBVo.brdId}', bullId:'${param.bullId}'}, function(data) {
		if (data.message != null) {
			$m.dialog.alert(data.message);
			return;
		}
		if (data.result == 'ok') {
			$m.dialog.confirm('<u:msg titleId="cm.cfrm.del" alt="삭제하시겠습니까?"  />', function(result){
				if(result){
					$m.ajax('/bb/transBullDelAjx.do?menuId=${menuId}&bullId=${param.bullId}&brdId=${baBrdBVo.brdId}', {brdId:'${baBrdBVo.brdId}', bullId:'${param.bullId}'}, function(data) {
						if (data.message != null) {
							$m.dialog.alert(data.message);
						}
						if (data.result == 'ok') {
							$m.nav.next(null, '/bb/${listPage}.do?${paramsForList}');
						}
					});
				}
			});
		}
	});
}
<% // [하단버튼:목록] 목록으로 이동 %>
function goList() {
	$m.nav.prev(null, '/bb/${listPage}.do?${paramsForList}');
}

<% // [등록] 한줄답변 등록 %>
function regCmt() {
	var cmt = $('#cmt').val();
	if ($.trim(cmt) == '') {
		$m.dialog.alert('<u:msg titleId="bb.msg.cmt.required" />');<% // bb.msg.cmt.required=한줄답변을 입력하세요. %>
		$('#cmt').focus();
		return;
	}
	$m.ajax('/bb/transCmtAjx.do?menuId=${menuId}&brdId=${param.brdId}&bullId=${param.bullId}', {bullId:'${param.bullId}', cmt:cmt}, function(data) {
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
	$m.ajax('/bb/listCmtAjx.do?menuId=${menuId}&brdId=${param.brdId}&bullId=${param.bullId}', {bullId:'${param.bullId}'}, function(data) {
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
	            
	           var delCmtAuth = '${delCmtAuth}';
	           var userUid = '${sessionScope.userVo.userUid}';
	           if(delCmtAuth == 'Y' || userUid == obj.regrUid){
		            cmtList.push('<dd><div class="reply_btnarea">');
		            cmtList.push('        <div class="size">');
		            cmtList.push('        <dl>');
		            cmtList.push('        <dd class="btn" onclick="delCmt(\''+obj.cmtId+'\');"><u:msg titleId="cm.btn.del" alt="삭제" /></dd>');
		            cmtList.push('        </dl>');
		            cmtList.push('        </div>');
		            cmtList.push('    </div>');
		            cmtList.push('</dd>');
	           }
	            
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
			$m.ajax('/bb/transCmtDelAjx.do?menuId=${menuId}&brdId=${param.brdId}&bullId=${param.bullId}', {bullId:'${param.bullId}', cmtId:id}, function(data) {
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
<c:if test="${baBrdBVo.recmdUseYn == 'Y'}">
function recmdBull() {
	<% // bb.cfrm.recmd=추천하시겠습니까? %>
	$m.msg.confirmMsg("bb.cfrm.recmd", null, function(result){
		if(result)
		{
			$m.ajax('/bb/transBullRecmdAjx.do?menuId=${menuId}&brdId=${param.brdId}&bullId=${param.bullId}', {brdId:'${baBrdBVo.brdId}', bullId:'${param.bullId}'}, function(data) {
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
</c:if>

<% // 찬반투표 %>
<c:if test="${baBrdBVo.favotYn == 'Y'}">
function favotBull(favotVa) {
	<% // bb.cfrm.favot=투표하시겠습니까? %>
	$m.msg.confirmMsg("bb.cfrm.favot", null, function(result){
		if(result)
		{
			$m.ajax('/bb/transBullFavotAjx.do?menuId=${menuId}&brdId=${param.brdId}&bullId=${param.bullId}', {brdId:'${baBrdBVo.brdId}', bullId:'${param.bullId}', favotVa:favotVa}, function(data) {
				if (data.message != null) {
					$m.dialog.alert(data.message);
				}
				if (data.result == 'ok') {
					if (favotVa == 'F') $('#prosCntSpan').html(data.prosCnt);
					if (favotVa == 'A') $('#consCntSpan').html(data.consCnt);
				}
			});
		}
	});
}
</c:if>

<% // 점수주기 %>
<c:if test="${baBrdBVo.screUseYn == 'Y'}">
function saveScre() {
	if (${screHstExist}) {
		$m.msg.alertMsg("bb.msg.scre.already");<% // bb.msg.scre.already=이미 점수를 준 게시물입니다. %>
		return;
	}

	if ($('#scre').val() == '') {
		$m.msg.alertMsg("bb.msg.scre.notChecked");<% // bb.msg.scre.notChecked=점수를 선택하세요. %>
		return;
	}
	$m.ajax('/bb/transBullScreAjx.do?menuId=${menuId}&bullId=${param.bullId}&brdId=${baBrdBVo.brdId}', {brdId:'${baBrdBVo.brdId}', bullId:'${param.bullId}', scre:$('#scre').val()}, function(data) {
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
		url:'/bb/viewScrePop.do?menuId=${menuId}&bullId=${param.bullId}&brdId=${param.brdId}',
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
</c:if>

<% // 파일 다운로드 %>
function downFile(id) {
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
	var url = "/bb/attachViewAjx.do?menuId=${menuId}&brdId=${param.brdId}&bullId=${param.bullId}";
	url+='&fileIds='+id;
	openViewAttchFile(url);
	//$m.nav.next(null, url);
}
</c:if>

//게시판 ID를 param에 세팅
function setBrdId(brdId){
	var queryParams = '${params}'.split('&');
	var params = "",tmpParams;
	for(var i=0;i<queryParams.length;i++){
		tmpParams = queryParams[i].split('=');
		if(tmpParams[0] == 'brdId') queryParams[i] = tmpParams[0] + "=" + brdId;
		params += params == '' ? queryParams[i] : '&' + queryParams[i]; 
	}
	
	return params;
};

<% // [목록:제목] 게시물 조회 %>
function viewBull(id , brdId) {
	if(brdId == undefined ) brdId = '${baBrdBVo.brdId}';
	params = setBrdId(brdId);
	$m.nav.curr(null, '/bb/${viewPage}.do?'+params+'&bullId=' + id);
}

function readHst(id) {
	/* $m.dialog.open({
		id:'listReadHstPop',
		title:'<u:msg titleId="bb.jsp.listReadHstPop.title" alt="조회자 정보" />',
		url:'/bb/listReadHstPop.do?menuId=${menuId}&brdId=${param.brdId}&bullId=' + id,
	}); */
	$m.nav.next(null, '/bb/listReadHstSub.do?menuId=${menuId}&brdId=${param.brdId}&bullId=' + id);
}

var holdHide = false;
$(document).ready(function() {
	$("input[type='text'], textarea").each(function(){
		$space.apply(this, {relative:30});
	});
	<c:if test="${baBrdBVo.screUseYn == 'Y'}">fnSetScre('');</c:if>
	$('body:first').on('click', function(){
		if(holdHide) holdHide = false;
		else $(".view.etcCls div.etr_open2").hide();
	});
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
			<c:if test="${listPage == 'listBull'}">
				<c:if test="${baBrdBVo.replyYn == 'Y'}">
					 <c:if test="${replyAuth == 'Y'}">
	      			 <dd class="btn" onclick="setReply();"><u:msg titleId="bb.btn.reply" alt="답변" /></dd>
	      			 </c:if>
				</c:if>
			</c:if>
		   <c:if test="${modAuth == 'Y'}">
		   <c:if test="${empty isOnlyMd || isOnlyMd==false}"><dd class="btn" onclick="modBull();"><u:msg titleId="cm.btn.mod" alt="수정" /></dd></c:if>
				<c:if test="${!empty isOnlyMd && isOnlyMd==true}"><dd class="btn" onclick="modBullWeb();"><u:msg titleId="cm.btn.mod" alt="수정" /></dd></c:if>	       	
	       </c:if>
	       <c:if test="${delAuth == 'Y'}">
	      	 <dd class="btn" onclick="delBull();"><u:msg titleId="cm.btn.del" alt="삭제" /></dd>
	       </c:if>
	       <dd class="btn" onclick="goList();"><u:msg titleId="cm.btn.list" alt="목록"  /></dd>
           <dd class="open" id="moreBtn" style="display:none" onclick="$layout.toggleBtns()"></dd>
        </dl>
      	 </div>
       </div>
       <!--//btnarea E-->
       <u:set var="viewTitle" test="${!empty bbBullLVo.viewTitle }" value="${bbBullLVo.viewTitle }" elseValue="${bbBullLVo.subj}"/>
		<!-- SNS 사용 -->
		<c:if test="${!empty param.brdId && !empty baBrdBVo && !empty param.bullId}">
		<u:out var="snsText" value="${viewTitle}" type="value"/>
		<m:sns mode="view" text="${snsText }" snsParams="&brdId=${param.brdId }&bullId=${param.bullId }" module="bb"/>
		</c:if>

		<!--titlezone S-->
        <div class="titlezone">
            <div class="titarea">
            <dl>
            <dd class="tit"><u:out value="${viewTitle}" type="html"/></dd>
            <dd class="name">
            	<c:if test="${!empty bbBullLVo.deptNm }"><u:out value="${bbBullLVo.deptNm}" />ㅣ</c:if><u:out value="${bbBullLVo.regrNm}" />ㅣ<u:out value="${bbBullLVo.regDt}" type="longdate" /><c:if test="${baColmDispDVoMap['READ_CNT'].readDispYn == 'Y'}">ㅣ
				<u:out value="${bbBullLVo.readCnt}" type="number" /></c:if>
            </dd>
            <c:if test="${baBrdBVo.optMap.privUseYn eq 'Y' && !empty privYn && privYn eq 'Y'}">
            <dd>* <u:msg titleId="bb.msg.view.priv" alt="비공개로 등록된 문서입니다." /></dd></c:if>
         	</dl>
            </div>
        </div>
        <!--//titlezone E-->
         <!--tabarea S-->
         <div class="tabarea" id="tabBtnArea">
             <div class="tabsize">
                 <dl>
                 <c:if test="${contYn eq 'Y' }"><dd class="tab_on" onclick="javascript:fnTabOn(this,'cont');"><u:msg titleId="cols.body" alt="본문" /></dd></c:if>
                 <dd class="tab${contYn ne 'Y' ? '_on' : ''}" onclick="javascript:fnTabOn(this,'detail');"><u:msg titleId="cm.btn.detl" alt="상세" /></dd>
                 <c:if test="${!empty fileVoList }">
                 	<dd class="tab" onclick="javascript:fnTabOn(this,'attch');"><u:msg titleId="cols.att" alt="첨부" /></dd>
                 </c:if>
				 <c:if test="${baBrdBVo.cmtYn == 'Y'}">
					<dd class="tab" onclick="javascript:fnTabOn(this,'cmt');"><u:msg titleId="cols.cmt" alt="한줄답변" />(<span id="cmtCnt">${empty bbBullLVo.cmtCnt ? 0 : bbBullLVo.cmtCnt}</span>)</dd>
				 </c:if>
				 <c:if test="${baBrdBVo.replyYn == 'Y' && fn:length(replyBullList) > 1}">
					<dd class="tab" onclick="javascript:fnTabOn(this,'rel');"><u:msg titleId="mbb.rel" alt="관련글" /></dd>
				 </c:if>
				 <c:if test="${baBrdBVo.recmdUseYn == 'Y' || baBrdBVo.favotYn == 'Y' || baBrdBVo.screUseYn == 'Y'}">
					<dd class="tab" onclick="javascript:fnTabOn(this,'etc');"><u:msg titleId="mbb.etc" alt="기타" /></dd>
				 </c:if>
              </dl>
             </div>
			<div class="tab_icol" style="display:none" id="toLeft"></div>
			<div class="tab_icor" style="display:none" id="toRight"></div>
         </div>
         <!--//tabarea E-->
			
		<div id="tabViewArea">
			<c:if test="${contYn eq 'Y' }">
            <!--bodyzone_scroll S-->
            <div class="bodyzone_scroll view contCls">
                <div class="bodyarea">
                <dl>
                <dd class="bodytxt_scroll"><div class="scroll editor" id="bodyHtmlArea">
                		<div id="zoom">
						<!-- 게시물사진 -->
						<c:if test="${baBrdBVo.photoYn == 'Y'}">
							<c:if test="${bbBullLVo.photoVo != null}">
							<c:set var="maxWdth" value="800" />
							<u:set test="${bbBullLVo.photoVo.imgWdth <= maxWdth}" var="imgWdth" value="${bbBullLVo.photoVo.imgWdth}" elseValue="${maxWdth}" />
								<img src="${_cxPth}${bbBullLVo.photoVo.savePath}" width="${imgWdth}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"'>
							</c:if>
						</c:if>
						<c:if test="${empty isOnlyMd || isOnlyMd==false}"><u:clob lobHandler="${lobHandler }"/></c:if>
						<c:if test="${!empty isOnlyMd && isOnlyMd==true}">
						<!--btnarea S-->
				       <%-- <div class="btnarea" ><div class="size"><dl><dd class="btn" onclick="viewBullWeb();"><u:msg titleId="wf.btn.cont.view" alt="본문 보기" /></dd></dl></div></div> --%>
						<jsp:include page="/WEB-INF/jsp/wf/web/viewWorks.jsp" flush="false" />
						</c:if>
						</div>
                </div>
                </dd>
	            </dl>
                </div>
            </div>
            <!--//bodyzone_scroll E-->
	       </c:if>

			<div class="attachzone view attchCls"  style="display:none;">
			<div class="blank30"></div>
				<div class="attacharea">
					<c:if test="${!empty fileVoList }">
						<c:forEach items="${fileVoList}" var="fileVo" varStatus="status">
							<m:attach fileName="${fileVo.dispNm}" fileKb="${fileVo.fileSize/1024 }" downFnc="downFile('${fileVo.fileId}');" viewFnc="viewAttchFile('${fileVo.fileId}');"/>
						</c:forEach>
					</c:if>
				</div>
          	</div>  
           
           
			<% // 한줄답변 %>
		<c:if test="${baBrdBVo.cmtYn == 'Y'}">
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
					<c:if test="${fn:length(baCmtDVoList) == 0}">
			            <div class="entryzone">
			                <div class="entryarea">
			                <dl>		                
			                <dd class="etr_input"><div class="etr_body_gray">※<u:msg titleId="bb.msg.noCmt" alt="한줄답변이 없습니다." /></div></dd>
			                </dl>
			                </div>                
			            </div>
					</c:if>
					<c:if test="${fn:length(baCmtDVoList) > 0}">
					<div class="listreplyarea">
					<c:forEach items="${baCmtDVoList}" var="baCmtDVo" varStatus="status">
						<%-- <u:out value="${recodeCount - baCmtDVo.rnum + 1}" type="number" /> --%>
		                <div class="listreply">
		                <dl>
		                <dd class="replytit"><a href="javascript:$m.user.viewUserPop('${baCmtDVo.regrUid}');"><u:out value="${baCmtDVo.regrNm}" /></a></dd>
		                <dd class="replybody"><u:out value="${baCmtDVo.cmt}" /></dd>
		                <dd class="replyname"><u:out value="${baCmtDVo.regDt}" type="longdate" /></dd>
		                <c:if test="${delCmtAuth == 'Y' || baCmtDVo.regrUid == sessionScope.userVo.userUid}">
		                <dd><div class="reply_btnarea">
		                        <div class="size">
		                        <dl>
		                        <dd class="btn" onclick="delCmt('${baCmtDVo.cmtId}');"><u:msg titleId="cm.btn.del" alt="삭제" /></dd>
			                    </dl>
		                        </div>
		                    </div>
		                </dd>
		                </c:if>
			            </dl>
		                </div>
						<div class="line"></div>
					</c:forEach>
					</div>
					</c:if>
			</div>	
		</div>
		</c:if>

        <!--listtablearea S-->
        <div  class="s_tablearea view detailCls" <c:if test="${contYn eq 'Y' }">style="display:none;"</c:if>>
        	<div class="blank30"></div>
            <table class="s_table" style="table-layout:fixed;">
            <!-- <caption>타이틀</caption> -->
            <colgroup>
                <col width="33%"/>
                <col width=""/>
            </colgroup>
            <tbody>
            	<c:if test="${baColmDispDVoMap['SUBJ'].readDispYn == 'Y' }">
            	<tr>
            		<th class="shead_lt"><u:msg titleId="cols.subj" alt="제목" /></th>
                    <td class="shead_lt wordbreak"><u:out value="${bbBullLVo.subj}" /></td>
            	</tr>
            	</c:if>
            	<c:if test="${baColmDispDVoMap['REGR_UID'].readDispYn == 'Y'}">
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.regr" alt="등록자" /></th>
                    <td class="sbody_lt"><a href="javascript:$m.user.viewUserPop('${bbBullLVo.regrUid}');"><u:out value="${bbBullLVo.regrNm}" /></a></td>
                </tr>
                </c:if>
                <c:if test="${baColmDispDVoMap['READ_CNT'].readDispYn == 'Y'}">
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.readCnt" alt="조회수" /></th>
                    <td class="sbody_lt"><a href="javascript:readHst('${bbBullLVo.bullId}');"><u:out value="${bbBullLVo.readCnt}" type="number" /></a></td>
                </tr>
                </c:if>
                <c:if test="${baBrdBVo.catYn == 'Y' && baColmDispDVoMap['CAT_ID'].readDispYn == 'Y'}">
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.cat" alt="카테고리" /></th>
                    <td class="shead_lt"> ${bbBullLVo.catNm}</td>
                </tr>
				</c:if>
				<c:if test="${baColmDispDVoMap['REZV_DT'].readDispYn == 'Y'}">
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.bullRezvDt" alt="게시예약일" /></th>
                    <td class="shead_lt"><u:out value="${bbBullLVo.bullRezvDt}" type="longdate" /></td>
                </tr>
				</c:if>
				<c:if test="${baColmDispDVoMap['REG_DT'].readDispYn == 'Y'}">
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.regDt" alt="등록일시" /></th>
                    <td class="shead_lt"><u:out value="${bbBullLVo.regDt}" type="longdate" /></td>
                </tr>
				</c:if>
				<c:if test="${baColmDispDVoMap['MOD_DT'].readDispYn == 'Y'}">
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.modDt" alt="수정일시" /></th>
                    <td class="shead_lt"><u:out value="${bbBullLVo.modDt}" type="longdate" /></td>
                </tr>
				</c:if>
				<c:if test="${bbBullLVo.replyOrdr == 0  }"><c:set var="map" value="${bbBullLVo.exColMap}" scope="request" />
					<!-- 확장컬럼 -->
					<c:forEach items="${baColmDispDVoList}" var="baColmDispDVo" varStatus="status">
					<c:set var="colmVo" value="${baColmDispDVo.colmVo}" />
					<c:set var="colmNm" value="${colmVo.colmNm.toLowerCase()}" />
					<c:set var="colmTyp" value="${colmVo.colmTyp}" />
					<c:set var="colmTypVal" value="${colmVo.colmTypVal}" />
					<c:if test="${baColmDispDVo.useYn == 'Y' && colmVo.exColmYn == 'Y'}">
					<c:if test="${baColmDispDVo.readDispYn == 'Y'}">
					<tr>
						<th class="shead_lt">${colmVo.rescNm}</th>
						<td class="shead_lt wordbreak">
						<c:if test="${colmTyp == 'TEXT' || colmTyp == 'TEXTAREA' || colmTyp == 'PHONE' || colmTyp == 'CALENDAR'}">
							<u:out value="${bbBullLVo.getExColm(colmVo.colmNm)}" />
						</c:if>
						<c:if test="${colmTyp == 'CALENDARTIME'}">
							<u:out value="${bbBullLVo.getExColm(colmVo.colmNm)}" type="longdate"/>
						</c:if>
						<c:if test="${fn:startsWith(colmTyp,'CODE')}">
							<u:set test="${cdListIndex == null}" var="cdListIndex" value="0" elseValue="${cdListIndex + 1}" />
							<c:if test="${colmTyp == 'CODE' || colmTyp == 'CODERADIO'}">								
								<c:forEach items="${cdList[cdListIndex]}" var="cd" varStatus="status">
								<c:if test="${cd.cdId == bbBullLVo.getExColm(colmVo.colmNm)}">${cd.rescNm}</c:if>
								</c:forEach>
							</c:if>
							<c:if test="${colmTyp == 'CODECHK'}"><c:set var="chkIndex"/>
								<c:forEach items="${cdList[cdListIndex]}" var="cd" varStatus="status"><c:set var="checked" value="N" 
							/><c:forTokens var="chkId" items="${bbBullLVo.getExColm(colmVo.colmNm)}" delims=","><c:if test="${chkId==cd.cdId }"><c:set var="checked" value="Y" 
							/></c:if></c:forTokens><c:if test="${checked eq 'Y'}"><c:set var="chkIndex" value="${empty chkIndex ? 0 : chkIndex+1 }"/><c:if test="${chkIndex>0 }">,</c:if>${cd.rescNm}</c:if></c:forEach></c:if>
						</c:if>
						<c:if test="${colmTyp == 'USER' || colmTyp == 'DEPT'}"><div id="selectListArea_${colmNm }" style="min-height:40px;"><u:convertMap srcId="map" attId="${colmNm }MapList" var="mapList" /><c:if test="${!empty mapList }"><c:forEach 
							var="mapVo" items="${mapList }" varStatus="status">
							<div class="ubox"><dl><c:if test="${colmTyp == 'USER' }"><dd class="title_view" onclick="$m.user.viewUserPop('${mapVo.id }');">${mapVo.rescNm }</dd></c:if
							><c:if test="${colmTyp == 'DEPT' }"><dd class="title_view">${mapVo.rescNm }</dd></c:if>
							</dl></div>
							</c:forEach></c:if></div></c:if>
						</td>
					</c:if>
					</c:if>
					</c:forEach>
				</c:if>		
            </tbody>
            </table>
        </div>
        <!--//listtablearea E-->        

		 <c:if test="${baBrdBVo.replyYn == 'Y' && fn:length(replyBullList) > 1}">
			<div class="listarea view relCls" style="display:none;">
				<div class="blank30"></div>
				<article>
	            <c:forEach items="${replyBullList}" var="bullVo" varStatus="status">
	            	<u:set test="${bbBullLVo.bullId == bullVo.bullId}" var="listdiv" value="listdivline" elseValue="listdiv"/>
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
		 
		 <c:if test="${baBrdBVo.recmdUseYn == 'Y' || baBrdBVo.favotYn == 'Y' || baBrdBVo.screUseYn == 'Y'}">
            <!--entryzone S-->
            <div class="entryzone view etcCls" style="display:none;">
                <div class="entryarea">
                <dl>
	            	<c:if test="${baBrdBVo.recmdUseYn == 'Y'}">
		                <dd class="etr_tit"><u:msg titleId="cols.recmdCnt" alt="추천수" /></dd>
		                <dd class="etr_input">
		                    <div class="etr_ipmany">
		                    <dl>
		                    <dd class="wblank5"></dd>
		                    <dd class="etr_body"><strong><span id="recmdCntTd">${bbBullLVo.recmdCnt}</span></strong></dd>
		                    <dd class="wblank3"></dd>
		                    <c:if test="${recmdHstExist == false}">
		                    	<dd onclick="javascript:recmdBull();"><div class="etr_btn"><u:msg titleId="bb.btn.recmd" alt="추천" /></div></dd>
		                    </c:if>
		                    </dl>
		                    </div>
		                </dd>	
	                </c:if>
	            	<c:if test="${baBrdBVo.favotYn == 'Y'}">
		                <dd class="etr_tit"><u:msg titleId="cols.favot" alt="찬반투표" /></dd>
		                <dd class="etr_input">
		                    <div class="etr_ipmany">
		                    <dl>
		                    <dd class="wblank5"></dd>
		                    <dd class="etr_body"><u:msg titleId="cols.prosCnt" alt="찬성수" />:<strong><span id="prosCntSpan">${bbBullLVo.prosCnt}</span></strong></dd>
		                    <dd class="wblank3"></dd>
		                    <c:if test="${favotHstExist == false}">
		                    	<dd onclick="javascript:favotBull('F');"><div class="etr_btn"><u:msg titleId="bb.btn.pros" alt="찬성" /></div></dd>
		                    </c:if>
		                    <dd class="wblank10"></dd>
		                    <dd class="etr_body"><u:msg titleId="cols.consCnt" alt="반대수" />:<strong><span id="consCntSpan">${bbBullLVo.consCnt}</span></strong></dd>
		                    <dd class="wblank3"></dd>
		                    <c:if test="${favotHstExist == false}">
		                   	 <dd onclick="javascript:favotBull('A');"><div class="etr_btn"><u:msg titleId="bb.btn.cons" alt="반대" /></div></dd>
		                    </c:if>
		                    </dl>
		                    </div>
		                </dd>
	                </c:if>
	                <c:if test="${baBrdBVo.screUseYn == 'Y'}">
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
	      			 </c:if>
                
	            </dl>
                </div>
			</div>
		</c:if>
		</div>
		<% // 이전글 다음글 %>
		<c:if test="${prevNextYn == true}">
        	<div class="prevnextarea">
                <div class="prev">
                	<dl>
					<c:if test="${prevBullVo == null}">
						<dd class="tit"><u:msg titleId="cm.ico.prev" alt="이전글" /></dd>
						<dd class="body"><u:msg titleId="bb.jsp.viewBull.prevNotExists" alt="이전글이 존재하지 않습니다." /></dd>
					</c:if>
					<c:if test="${prevBullVo != null}">
						<dd class="tit"><u:msg titleId="cm.ico.prev" alt="이전글" /></dd>
						<dd class="body" onclick="javascript:viewBull('${prevBullVo.bullId}','${prevBullVo.brdId }');">
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
						<dd class="body" onclick="javascript:viewBull('${nextBullVo.bullId}','${nextBullVo.brdId }');">
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

