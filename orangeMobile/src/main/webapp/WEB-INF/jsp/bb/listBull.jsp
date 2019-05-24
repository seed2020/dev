<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="faqYn" test="${baBrdBVo.brdTypCd == 'F' }" value="Y" elseValue="N"/>
<u:secu auth="W" ><u:set test="${true}" var="writeAuth" value="Y"/></u:secu>
<u:set test="${param.schCat != null}" var="schCat" value="${param.schCat}" elseValue="SUBJ" />
<u:set test="${param.catId != null}" var="catId" value="${param.catId}" elseValue="" />
<u:set test="${(param.catId != null && param.catId != '') || (param.strtYmd != null && param.strtYmd != '') || (param.endYmd != null && param.endYmd != '')}" var="unfoldareaUsed" value="Y" elseValue="N" />
<c:if test="${!empty faqYn && faqYn eq 'Y'}">
<style type="text/css">
div img{max-width:98%;}
tr.faqTitle div.number {
     display: block;
     width: 20px;
     height: 20px;
     border: 0px;
     border-radius: 2px;
     float:left;
     text-align:center;
     color:#fff;
     background: #848484;
     font-weight:bold;
     margin-right:3px;
     line-height:23px;
 }
tr.faqTitle td {
	font-size: 1.3em;
	color:#000;
    line-height: 23px;
    background: #fff;
    border-top: 1px solid #e6e6e6;
    text-align: center;
    vertical-align: top;
    padding: 8px 0 6px;
    text-align:left;
}
tr.faqDetail td {
    border-top: 1px solid #e6e6e6;
    background: #ffffff none;
}
</style></c:if>
<script type="text/javascript">
//<![CDATA[
function searchList(event){
	$m.nav.curr(event, './listBull.do?'+$('#searchForm').serialize());
}

<% // [목록:제목] 게시물 조회 %>
function viewBull(id) {
	$m.nav.next(null, '/bb/viewBull.do?${params}&bullId=' + id);
}
function fnSetSchCd(cd)
{
	$('#schCat').val(cd);
	$('.schTxt1 span').text($(".schOpnLayer1 dd[data-schCd="+cd+"]").text());
	$('.schOpnLayer1').hide();
}

function fnSetCatId(cd)
{
	$('#catId').val(cd);
	$('.schTxt2 span').text($(".schOpnLayer2 dd[data-schCd='"+cd+"']").text());
	$('.schOpnLayer2').hide();
}

var holdHide = false, holdHide2 = false;
$(document).ready(function() {
	<c:if test="${empty listCondApplyYn || listCondApplyYn == false }">
	fnSetSchCd('${schCat}');
	fnSetCatId('${catId}');
	$('body:first').on('click', function(){
		if(holdHide) holdHide = false;
		else $(".schOpnLayer1").hide();
		if(holdHide2) holdHide2 = false;
		else $(".schOpnLayer2").hide();
	});
	if('${unfoldareaUsed}' == 'Y')
		fnUnfold();
	</c:if>
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('list');
});

function fnUnfold(){
	if($('#unfold').attr("class") == "unfoldbtn")
		$('#unfold').attr("class", "unfoldbtn_on");
	else
		$('#unfold').attr("class", "unfoldbtn");
	$('.unfoldArea').toggle(); 
}

function fnCalendar(id,opt){
	$m.dialog.open({
		id:id,
		noPopbody:true,
		cld:true,
		url:'/cm/util/getCalendarPop.do?id='+id+'&opt='+opt+'&val='+$('#'+id).val()+'&calStyle=m',
	});
}

function readHst(id) {
	$m.dialog.open({
		id:'listReadHstPop',
		title:'<u:msg titleId="bb.jsp.listReadHstPop.title" alt="조회자 정보" />',
		url:'/bb/listReadHstPop.do?menuId=${menuId}&brdId=${param.brdId}&bullId=' + id,
	});
}

function fnPichPop(){
	$m.dialog.open({
		id:'viewBullPich',
		title:'<u:msg titleId="bb.jsp.listBull.pich.title" alt="게시판 담당자" />',
		url:'/bb/viewBullPichPop.do?${params}',
	});
}

<% // [상단버튼:등록] 등록 %>
function regBull() {
	$m.nav.next(null, '/bb/setBull.do?${params}');
}<% // [등록:상단버튼] - 웹버전 등록화면 팝업 출력 %>
function regBullWeb() {
	var param={};
	param['mdCd']='BB'; // 모듈코드
	param['mdRefId']='${baBrdBVo.brdId}'; // 게시판ID
	param['mode']='set';
	$m.ajax('/bb/getWorksUrlAjx.do?menuId=${menuId}', param, function(data) {
		if(data.message !=null){
			$m.dialog.alert(data.message);
		}
		if(data.webUrl!=null) {
			var url=data.webUrl;
			url+=url.indexOf('?') > -1 ? "&" : "?";
			url+="isMobile=Y";
			window.open(url, "regBullWin");
		}
	});
}<% // [새로고침] - 팝업 등록화면에서 저장후 새로고침 %>
function reloadOpen(){
	$m.nav.reload();
}
<c:if test="${!empty faqYn && faqYn eq 'Y'}">
<% // [AJAX] 파일목록 추가 %>
function getFaqFileList(fileList){
	var buffer=[], fileName=null;//, fileKb;
	buffer.push('<div class="attachzone view attchCls" style="height:auto;"><div class="blank10"></div><div class="attacharea">');
	$.each(fileList, function(index, fileVo){
		fileName=fileVo.dispNm;
		//fileKb=fileVo.fileSize/1024;
		//if(fileKb!=null) fileName = fileName + " ("+addComma(fileKb)+" KB)";
		buffer.push('<div class="attachin">');
		buffer.push('<div class="attach" onclick="downFile(\''+fileVo.fileId+'\',\''+fileVo.dispNm+'\');">');
		buffer.push('<div class="btn"></div>');
		buffer.push('<div class="txt">'+fileName+'</div>');
		buffer.push('</div>');
		buffer.push('<div class="down" onclick="downFile(\''+fileVo.fileId+'\',\''+fileVo.dispNm+'\');"></div>');
		buffer.push('</div>');
	});
	buffer.push('</div></div>');
	return buffer.join('');
}

<% // [AJAX] FAQ 상세보기 %>
function openFaqView(id, obj){
	$('#listArea tr[id^="detail_"]').not('#detail_'+id).hide();
	$('#listArea tr[id^="title_"] div').css('font-weight','');
	
	var target=$('#detail_'+id);
	var isEmpty=target.attr('data-load')=='N';
	if(isEmpty){
		$m.ajax('/bb/getBullHtmlAjx.do?menuId=${menuId}&brdId=${baBrdBVo.brdId}', {brdId:'${baBrdBVo.brdId}', bullId:id}, function(data) {
			if (data.message != null) {
				$m.dialog.alert(data.message);
			}
			if (data.result == 'ok') {
				if(data.bodyHtml!=null)
					target.find('div#cont').html(data.bodyHtml);
				<c:if test="${baBrdBVo.optMap.fileUploadYn eq 'Y'}">
				if(data.fileVoList!=null){
					target.find('div#file').html(getFaqFileList(data.fileVoList));
				}
				</c:if>
				target.attr('data-load', 'Y');
			}
			
		});
		target.show();
	}else
		target.toggle();
	if(target.css("display") != 'none')
		$('#listArea tr[id="title_'+id+'"] div').css('font-weight','bold');
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
</c:if>
//]]>
</script>

<section>
<c:if test="${empty faqYn || faqYn ne 'Y'}">
<c:if test="${baBrdBVo.pichDispYn == 'Y' || writeAuth == 'Y'}">
<div class="btnarea">
    <div class="size">
        <dl>
           	 <c:if test="${baBrdBVo.pichDispYn == 'Y'}"><dd class="btn" onclick="javascript:fnPichPop();"><u:msg titleId="cols.pich" alt="담당자"/></dd></c:if>
           	 <c:if test="${writeAuth == 'Y'}"><c:if test="${!empty baBrdBVo.optMap.wfFormNo}"><dd class="btn" onclick="regBullWeb();"><u:msg titleId="cm.btn.write" alt="등록" /></dd></c:if
           	 ><c:if test="${empty baBrdBVo.optMap.wfFormNo}"><dd class="btn" onclick="regBull();"><u:msg titleId="cm.btn.write" alt="등록" /></dd></c:if
           	 ></c:if>
     </dl>
    </div>
</div>
</c:if>
</c:if>
<!--listsearch S-->
<c:if test="${empty listCondApplyYn || listCondApplyYn == false }">
<form id="searchForm" name="searchForm" action="./listBull.do" onsubmit="searchList(event);">
<input type="hidden" name="menuId" value="${param.menuId}" />
<input type="hidden" name="brdId" value="${param.brdId}" />
<input type="hidden" name="schCat" id="schCat" value="" />
<input type="hidden" name="catId" id="catId" value="" />
<div class="listsearch">
		<div class="listselect schTxt1">
		    <div class="open1 schOpnLayer1" style="display:none;">
		        <div class="open_in1">
		        	<div class="open_div">
				        <dl>
				        <dd class="txt" onclick="javascript:fnSetSchCd('SUBJ');" data-schCd="SUBJ"><u:msg titleId="cols.subj" alt="제목" /></dd>
				        <dd class="line"></dd>
				        <dd class="txt" onclick="javascript:fnSetSchCd('CONT');" data-schCd="CONT"><u:msg titleId="cols.cont" alt="내용" /></dd>
				        <dd class="line"></dd>
						<c:if test="${baBrdBVo.brdTypCd == 'N'}">
							<dd class="txt" onclick="javascript:fnSetSchCd('REGR_NM');" data-schCd="REGR_NM"><u:msg titleId="cols.regr" alt="등록자" /></dd>
						</c:if>
						<c:if test="${baBrdBVo.brdTypCd == 'A'}">
							<dd class="txt" onclick="javascript:fnSetSchCd('ANON_REGR_NM');" data-schCd="ANON_REGR_NM"><u:msg titleId="cols.regr" alt="등록자" /></dd>
						</c:if>
				    	</dl>
				    </div>	
		        </div>
		    </div>
			<div class="select1">
			<div class="select_in1" onclick="holdHide = true;$('.schOpnLayer1').toggle();">
			<dl>
				<dd class="select_txt1"><span></span></dd>
				<dd class="select_btn"></dd>
			</dl>
			</div>
			</div>
		</div>
    	<div class="listinput2">
			<div class="input1">
			<dl><dd class="input_left"></dd><dd class="input_input"><input type="text" class="input_ip" name="schWord" maxlength="30" value="<u:out value="${param.schWord}" type="value" />" /></dd><dd class="input_btn" onclick="searchList(event);"><div class="search"></div></dd></dl>
			</div>
		</div>
		<div class="unfoldbtn" onclick="fnUnfold();" id="unfold"></div>
</div>


<div class="entryzone unfoldArea" style="display:none;">
      <div class="entryarea">
      <dl>
		<dd class="etr_blank"></dd>
		
	      <dd class="etr_select">
		  	  <div class="etr_calendar_lt">
			      <div class="etr_calendar">
				  <input id="strtYmd" name="strtYmd" value="${param.strtYmd}" type="hidden" />
				  <div class="etr_calendarin">
					   <dl>
					   <dd class="ctxt" onclick="fnCalendar('strtYmd','{end:\'endYmd\'}');"><span id="strtYmd">${param.strtYmd}</span></dd>
					   <dd class="cdelete" onclick="fnTxtDelete(this,'strtYmd');"></dd>
					   <dd class="cbtn" onclick="fnCalendar('strtYmd','{end:\'endYmd\'}');"></dd>
					   </dl>
				   </div>
				</div>
			</div>
		
	        <div class="etr_calendar_rt">
	            <div class="etr_calendar">
	            	<input id="endYmd" name="endYmd" value="${param.endYmd}" type="hidden" />
	            	<div class="etr_calendarin">
	                    <dl>
	                    <dd class="ctxt" onclick="fnCalendar('endYmd','{start:\'strtYmd\'}');"><span id="endYmd">${param.endYmd}</span></dd>
	                    <dd class="cdelete" onclick="fnTxtDelete(this,'endYmd');"></dd>
	                    <dd class="cbtn" onclick="fnCalendar('endYmd','{start:\'strtYmd\'}');"></dd>
                    </dl>
                    </div>
	            </div>
	        </div>
	    </dd>
    
    
      <dd class="dd_blank5"></dd>
		<dd class="etr_input">
              <div class="etr_ipmany">
              <dl>
              <c:if test="${baBrdBVo.catYn == 'Y'}">
              <dd class="etr_se_lt">
                  <div class="etr_open2 schOpnLayer2" style="display:none;">
                    <div class="open_in1">
                        <div class="open_div">
					        <dl>
					        <dd class="txt" onclick="javascript:fnSetCatId('');" data-schCd=""><u:msg titleId="cm.option.all" alt="전체" /></dd>
					        <dd class="line"></dd>
					        <c:forEach items="${baCatDVoList}" var="catVo" varStatus="status">
						        <dd class="txt" onclick="javascript:fnSetCatId('${catVo.catId}');" data-schCd="${catVo.catId}">${catVo.rescNm}</dd>
						        <dd class="line"></dd>
							</c:forEach>
					    	</dl>
                        </div>
                    </div>
                </div>   
                <div class="select_in1 schTxt2" onclick="holdHide2 = true;$('.schOpnLayer2').toggle();">
                <dl>
                <dd class="select_txt"><span></span></dd>
                <dd class="select_btn"></dd>
                </dl>
                </div>
            </dd>
            </c:if>

            </dl>
            </div>
        </dd><dd class="etr_blank1"></dd>
 		</dl>
    </div> 
</div>
</form>
</c:if>
<!--//listsearch E-->
<c:if test="${!empty listCondApplyYn && listCondApplyYn == true }">
<% // 검색영역 %>
<jsp:include page="/WEB-INF/jsp/bb/listBbSrch.jsp" />
</c:if>

<!--section S-->

<div class="listarea" id="listArea">
	<article>
	<c:if test="${empty faqYn || faqYn ne 'Y'}">
	<!-- 공지 목록 -->
	<c:if test="${empty listCondApplyYn || listCondApplyYn == false }">
	<c:if test="${fn:length(notcBullList) > 0}">
	<c:forEach items="${notcBullList}" var="bbBullLVo" varStatus="status">
		<div class="listdiv" onclick="viewBull('${bbBullLVo.bullId}');">
			<u:set var="replyYn" test="${bbBullLVo.replyDpth > 0 }" value="Y" elseValue="N"/>
          	<c:if test="${replyYn eq 'Y' }"><div class="listcheck_comment"><dl><dd class="comment"></dd></dl></div></c:if>
	        <div class="list${replyYn eq 'Y' ? '_comment' : '' }">
	        	<dl>
                    <dd class="tit">
                    	<div class="notice${bbBullLVo.newYn == 'Y' ? 'new' : '' }"></div>
                    	<c:if test="${bbBullLVo.readYn != 'Y'}"><strong></c:if>
                    	<c:if test="${bbBullLVo.ugntYn == 'Y'}"><span class="ctxt1">[<u:msg titleId="bb.option.ugnt" alt="긴급" />]</span></c:if><c:if test="${bbBullLVo.secuYn == 'Y'}"><span class="ctxt2">[<u:msg titleId="bb.option.secu" alt="보안" />]</span></c:if>
						${bbBullLVo.subj}<c:if test="${baBrdBVo.cmtYn == 'Y'}">(<u:out value="${bbBullLVo.cmtCnt}" type="number" />)</c:if>
						<c:if test="${bbBullLVo.readYn != 'Y'}"></strong></c:if>
                    </dd>
                    <dd class="name"><u:out value="${bbBullLVo.deptNm}" />ㅣ<u:out value="${bbBullLVo.regrNm}" />ㅣ<u:out value="${bbBullLVo.regDt}" type="longdate" />ㅣ
					<u:out value="${bbBullLVo.readCnt}" type="number" /></dd>
                </dl>
	        </div>
	    </div>
	</c:forEach>
	</c:if></c:if><c:if test="${!empty listCondApplyYn && listCondApplyYn == true }">
	<c:forEach var="map" items="${notcBullListMap}" varStatus="listStatus">
		<c:set var="cdListIndex" value="${null }" />
		<c:set var="map" value="${map }" scope="request"/>
		<div class="listdiv" onclick="viewBull('${map.bullId}');">
			<u:set var="replyYn" test="${bbBullLVo.replyDpth > 0 }" value="Y" elseValue="N"/>
          	<c:if test="${replyYn eq 'Y' }"><div class="listcheck_comment"><dl><dd class="comment"></dd></dl></div></c:if>
	        <div class="list${replyYn eq 'Y' ? '_comment' : '' }">
	        	<dl>
	        		<c:forEach items="${baColmDispDVoList}" var="dispVo" varStatus="colStatus">
	        		<c:set var="etcValue" value="" />
	        		<c:set var="colmVo" value="${dispVo.colmVo}" />
	        		<c:set var="colmTyp" value="${colmVo.colmTyp}" />
	        		<u:convertMap var="value" srcId="map" attId="${dispVo.atrbId}" type="html" />
	        		<c:if test="${colmTyp == 'CALENDAR'}"><u:out var="value" value="${value}" type="date" /></c:if>
	        		<c:if test="${fn:endsWith(dispVo.atrbId, 'Dt') || colmTyp == 'CALENDARTIME'}"><u:out var="value" value="${value}" type="longdate" /></c:if>
	        		<c:if test="${fn:endsWith(dispVo.atrbId, 'Cnt')}"><u:out var="value" value="${value}" type="number" /></c:if>
	        		<c:if test="${fn:endsWith(dispVo.atrbId, 'Uid')}"><u:convertMap var="value" srcId="map" attId="${fn:replace(dispVo.atrbId,'Uid','')}Nm" type="html" /></c:if>
	        		<c:if test="${dispVo.atrbId == 'scre'}"><c:forEach begin="1" end="5" step="1" varStatus="status"
	        		><u:set test="${status.count <= map.scre}" var="star" value="★" elseValue="☆" 
	        		/><c:set var="etcValue" value="${etcValue }${star }"
	        		/></c:forEach><u:out var="value" value="${etcValue}" /></c:if>
	        		<c:if test="${fn:startsWith(colmTyp,'CODE')}">
					<u:set test="${cdListIndex == null}" var="cdListIndex" value="0" elseValue="${cdListIndex + 1}" />
					<c:if test="${colmTyp == 'CODE' || colmTyp == 'CODERADIO'}">					
						<c:forEach items="${cdList[cdListIndex]}" var="cd" varStatus="status">
						<c:if test="${cd.cdId == value}"><u:out var="value" value="${cd.rescNm}"/></c:if>
						</c:forEach>
					</c:if><c:if test="${colmTyp == 'CODECHK'}"><c:set var="chkIndex"/>
						<c:forEach items="${cdList[cdListIndex]}" var="cd" varStatus="status"><c:set var="checked" value="N" 
					/><c:forTokens var="chkId" items="${value}" delims=","><c:if test="${chkId==cd.cdId }"><c:set var="checked" value="Y" 
					/></c:if></c:forTokens><c:if test="${checked eq 'Y'}"
					><c:set var="chkIndex" value="${empty chkIndex ? 0 : chkIndex+1 }"
					/><c:set var="etcValue" value="${etcValue }${chkIndex>0 ? ',' : ''}${cd.rescNm}"
	        		/></c:if></c:forEach><u:out var="value" value="${etcValue}" /></c:if>
				</c:if><c:if test="${colmTyp == 'USER' || colmTyp == 'DEPT'}"><c:set var="exColMap" value="${map.exColMap}" scope="request" 
				/><u:convertMap srcId="exColMap" attId="${fn:toLowerCase(colmVo.colmNm)}MapList" var="mapList" /><c:if test="${!empty mapList }"><c:forEach 
			var="mapVo" items="${mapList }" varStatus="mapStatus"><c:set var="etcValue" value="${etcValue }${mapStatus.index>0 ? ',' : ''}${mapVo.rescNm }"
	        		/></c:forEach><u:out var="value" value="${etcValue}" /></c:if></c:if>
					<c:if test="${colmVo.colmNm == 'CAT_ID'}"><u:out var="value" value="${map.catNm}" /></c:if>
	        		<c:if test="${colStatus.first }">
	        		<dd class="tit">
                    	<div class="notice${bbBullLVo.newYn == 'Y' ? 'new' : '' }"></div>
                    	<c:if test="${bbBullLVo.readYn != 'Y'}"><strong></c:if>
						<c:if test="${bbBullLVo.ugntYn == 'Y'}"><span class="ctxt1">[<u:msg titleId="bb.option.ugnt" alt="긴급" />]</span></c:if><c:if test="${bbBullLVo.secuYn == 'Y'}"><span class="ctxt2">[<u:msg titleId="bb.option.secu" alt="보안" />]</span></c:if>
						${value }
						<c:if test="${bbBullLVo.readYn != 'Y'}"></strong></c:if>
                    </dd></c:if>
                    <c:if test="${!colStatus.first}">
                    <dd class="name">${colmVo.rescNm} : ${value}</dd></c:if>
					</c:forEach>
                </dl>
	        </div>
	    </div>
	</c:forEach>
	</c:if>
	<!-- 게시물 목록 -->
	<c:if test="${fn:length(bbBullLVoList) == 0}">
     <div class="listdiv_nodata" ><dl><dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd></dl></div>
	</c:if>
	<c:if test="${empty listCondApplyYn || listCondApplyYn == false }">
	<c:if test="${fn:length(bbBullLVoList) > 0}">
	<u:set test="${faqYn eq 'Y'}" var="viewBull" value="openFaqView" elseValue="viewBull" />
	<c:forEach items="${bbBullLVoList}" var="bbBullLVo" varStatus="status">		  
          <div class="listdiv" onclick="${viewBull }('${bbBullLVo.bullId}');">
          	  <u:set var="replyYn" test="${bbBullLVo.replyDpth > 0 }" value="Y" elseValue="N"/>
          	  <c:if test="${replyYn eq 'Y' }"><div class="listcheck_comment"><dl><dd class="comment"></dd></dl></div></c:if>
              <div class="list${replyYn eq 'Y' ? '_comment' : '' }">
              <dl>
              	   <dd class="tit">
                    	<c:if test="${bbBullLVo.newYn == 'Y'}"><div class="new"></div></c:if>
                    	<c:if test="${bbBullLVo.readYn != 'Y'}"><strong></c:if>
						<c:if test="${bbBullLVo.ugntYn == 'Y'}"><span class="ctxt1">[<u:msg titleId="bb.option.ugnt" alt="긴급" />]</span></c:if><c:if test="${bbBullLVo.secuYn == 'Y'}"><span class="ctxt2">[<u:msg titleId="bb.option.secu" alt="보안" />]</span></c:if>
						${bbBullLVo.subj}<c:if test="${baBrdBVo.cmtYn == 'Y'}">(<u:out value="${bbBullLVo.cmtCnt}" type="number" />)</c:if>
						<c:if test="${bbBullLVo.readYn != 'Y'}"></strong></c:if>
                    </dd>
                    <c:if test="${empty faqYn || faqYn ne 'Y'}"><dd class="name"><u:out value="${bbBullLVo.deptNm}" />ㅣ<u:out value="${bbBullLVo.regrNm}" />ㅣ<u:out value="${bbBullLVo.regDt}" type="longdate" />ㅣ
					<u:out value="${bbBullLVo.readCnt}" type="number" /></dd></c:if>
           	  </dl>
              </div>
              
          </div>
	</c:forEach>
	</c:if></c:if><c:if test="${!empty listCondApplyYn && listCondApplyYn == true }">
	<c:forEach var="map" items="${bbBullLVoListMap}" varStatus="listStatus">
		<c:set var="cdListIndex" value="${null }" />
		<c:set var="map" value="${map }" scope="request"/>
		<div class="listdiv" onclick="viewBull('${map.bullId}');">
			<u:set var="replyYn" test="${bbBullLVo.replyDpth > 0 }" value="Y" elseValue="N"/>
          	<c:if test="${replyYn eq 'Y' }"><div class="listcheck_comment"><dl><dd class="comment"></dd></dl></div></c:if>
	        <div class="list${replyYn eq 'Y' ? '_comment' : '' }">
	        	<dl>
	        		<c:forEach items="${baColmDispDVoList}" var="dispVo" varStatus="colStatus">
	        		<c:set var="etcValue" value="" />
	        		<c:set var="colmVo" value="${dispVo.colmVo}" />
	        		<c:set var="colmTyp" value="${colmVo.colmTyp}" />
	        		<u:convertMap var="value" srcId="map" attId="${dispVo.atrbId}" type="html" />
	        		<c:if test="${colmTyp == 'CALENDAR'}"><u:out var="value" value="${value}" type="date" /></c:if>
	        		<c:if test="${fn:endsWith(dispVo.atrbId, 'Dt') || colmTyp == 'CALENDARTIME'}"><u:out var="value" value="${value}" type="longdate" /></c:if>
	        		<c:if test="${fn:endsWith(dispVo.atrbId, 'Cnt')}"><u:out var="value" value="${value}" type="number" /></c:if>
	        		<c:if test="${fn:endsWith(dispVo.atrbId, 'Uid')}"><u:convertMap var="value" srcId="map" attId="${fn:replace(dispVo.atrbId,'Uid','')}Nm" type="html" /></c:if>
	        		<c:if test="${dispVo.atrbId == 'scre'}"><c:forEach begin="1" end="5" step="1" varStatus="status"
	        		><u:set test="${status.count <= map.scre}" var="star" value="★" elseValue="☆" 
	        		/><c:set var="etcValue" value="${etcValue }${star }"
	        		/></c:forEach><u:out var="value" value="${etcValue}" /></c:if>
	        		<c:if test="${fn:startsWith(colmTyp,'CODE')}">
					<u:set test="${cdListIndex == null}" var="cdListIndex" value="0" elseValue="${cdListIndex + 1}" />
					<c:if test="${colmTyp == 'CODE' || colmTyp == 'CODERADIO'}">					
						<c:forEach items="${cdList[cdListIndex]}" var="cd" varStatus="status">
						<c:if test="${cd.cdId == value}"><u:out var="value" value="${cd.rescNm}"/></c:if>
						</c:forEach>
					</c:if><c:if test="${colmTyp == 'CODECHK'}"><c:set var="chkIndex"/>
						<c:forEach items="${cdList[cdListIndex]}" var="cd" varStatus="status"><c:set var="checked" value="N" 
					/><c:forTokens var="chkId" items="${value}" delims=","><c:if test="${chkId==cd.cdId }"><c:set var="checked" value="Y" 
					/></c:if></c:forTokens><c:if test="${checked eq 'Y'}"
					><c:set var="chkIndex" value="${empty chkIndex ? 0 : chkIndex+1 }"
					/><c:set var="etcValue" value="${etcValue }${chkIndex>0 ? ',' : ''}${cd.rescNm}"
	        		/></c:if></c:forEach><u:out var="value" value="${etcValue}" /></c:if>
				</c:if><c:if test="${colmTyp == 'USER' || colmTyp == 'DEPT'}"><c:set var="exColMap" value="${map.exColMap}" scope="request" 
				/><u:convertMap srcId="exColMap" attId="${fn:toLowerCase(colmVo.colmNm)}MapList" var="mapList" /><c:if test="${!empty mapList }"><c:forEach 
			var="mapVo" items="${mapList }" varStatus="mapStatus"><c:set var="etcValue" value="${etcValue }${mapStatus.index>0 ? ',' : ''}${mapVo.rescNm }"
	        		/></c:forEach><u:out var="value" value="${etcValue}" /></c:if></c:if
	        		><c:if test="${colmVo.colmNm == 'CAT_ID'}"><u:out var="value" value="${map.catNm}" /></c:if>
		
	        		<c:if test="${colStatus.first }">
	        		<dd class="tit">
                    	<c:if test="${bbBullLVo.newYn == 'Y'}"><div class="new"></div></c:if>
                    	<c:if test="${bbBullLVo.readYn != 'Y'}"><strong></c:if>
						<c:if test="${bbBullLVo.ugntYn == 'Y'}"><span class="ctxt1">[<u:msg titleId="bb.option.ugnt" alt="긴급" />]</span></c:if><c:if test="${bbBullLVo.secuYn == 'Y'}"><span class="ctxt2">[<u:msg titleId="bb.option.secu" alt="보안" />]</span></c:if>
						${value }
						<c:if test="${bbBullLVo.readYn != 'Y'}"></strong></c:if>
                    </dd></c:if>
                    <c:if test="${!colStatus.first}">
                    <dd class="name">${colmVo.rescNm} : ${value}</dd></c:if>
					</c:forEach>
                </dl>
	        </div>
	    </div>
	</c:forEach>
	</c:if>
	
	</c:if>
	<c:if test="${faqYn eq 'Y'}">
	<c:if test="${fn:length(bbBullLVoList) == 0}">
     <div class="listdiv_nodata" ><dl><dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd></dl></div>
	</c:if>
	<div class="list" id="listArea" style="margin:5px 0 0 0;">
	<table border="0" cellspacing="0" cellpadding="0" style="width:100%;">
	<c:forEach items="${bbBullLVoList}" var="bbBullLVo" varStatus="status">	
	<tr class="faqTitle" id="title_${bbBullLVo.bullId }">
	<td><div class="number"><u:out value="${bbBullLVo.rnum}" type="number" /></div><div style="width:100%;" onclick="openFaqView('${bbBullLVo.bullId}');">${bbBullLVo.subj}</div></td>
	</tr>
	<tr class="faqDetail" id="detail_${bbBullLVo.bullId }" data-load="N" style="display:none;">
	<td><div id="cont" style="max-width:100%;padding:5px 2px 5px 2px;"></div><div id="file" style="height:auto;"></div><div class="blank10"></div></td>
	</tr>
	</c:forEach>
</table></div><div class="blank10" style="border-top:1px solid silver;"></div></c:if>	
	 </article>
    
</div>
<m:pagination />
    
<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
</section>
<!--//section E-->