<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:secu auth="W" ownerUid="${listPage eq 'listBc' ? wbBcBVo.regrUid : ''}"><u:set test="${true}" var="writeAuth" value="Y"/></u:secu>
<u:set var="agntParam" test="${!empty schBcRegrUid }" value="&schBcRegrUid=${schBcRegrUid }" elseValue=""/>
<u:set var="urlPrefix" test="${listPage eq 'listPubBc'}" value="/wb/pub" elseValue="/wb"/>
<script type="text/javascript">
//<![CDATA[
//상세보기
function viewMetng(bcMetngDetlId) {
	$m.nav.next(event, "/wb/viewMetng.do?menuId=${menuId}${agntParam}&bcMetngDetlId="+bcMetngDetlId);
};           
           
function fnTabOn(obj,val){
	$(".view").each(function(){$(this).hide();});
	$(".view."+val+"Cls").each(function(){$(this).show();});
	$(".tabarea dd").each(function(){$(this).attr("class", "tab");});
	$(obj).attr("class","tab_on");
};     
           
function fnBumkUpdate(bumkYn){
	$m.msg.confirmMsg("wb.cfrm."+(bumkYn == 'Y' ? 'add' : 'del')+"Bumk", null , function(result){
		if(result)
		{
			$m.ajax('/wb/transBumkBc.do?menuId=${menuId}', {bcId:'${wbBcBVo.bcId}', bumkYn:bumkYn}, function(data) {
				if (data.message != null) {
					$m.dialog.alert(data.message);
				}
				if (data.result == 'ok') {
					$m.nav.next(event, '/wb/${viewPage}.do?${paramsForList }${agntParam}&bcId=${wbBcBVo.bcId}');
				}
			});
		}
	});
}


function fnDelete(){
	$m.msg.confirmMsg("cm.cfrm.del", null , function(result){
		if(result)
		{
			$m.ajax('${urlPrefix}/${transDelPage }.do?menuId=${menuId}', {bcId:'${wbBcBVo.bcId}', schBcRegrUid:'${schBcRegrUid }'}, function(data) {
				if (data.message != null) {
					$m.dialog.alert(data.message);
				}
				if (data.result == 'ok') {
					$m.nav.next(event, '${urlPrefix}/${listPage}.do?${paramsForList }${agntParam }');
				}
			});
		}
	});
}

<% // 목록으로 이동 %>
function goList() {
	$m.nav.prev(event, '${urlPrefix}/${listPage }.do?${paramsForList}${agntParam }');
}

function fnMod() {
	$m.nav.next(event, '${urlPrefix}/${setPage }.do?${paramsForList}&bcId=${wbBcBVo.bcId }${agntParam }');
}

<% // 파일 다운로드 %>
function downFile(id,dispNm) {
	var ids = [];
	ids.push(id);
	var $form = $('<form>', {
			'method':'get',
			'action':'${urlPrefix}/downFile.do',
			'target':$m.browser.mobile && $m.browser.safari ? '' : 'dataframe'
		}).append($('<input>', {
			'name':'menuId',
			'value':'${menuId}',
			'type':'hidden'
		})).append($('<input>', {
			'name':'typ',
			'value':'${param.typ}',
			'type':'hidden'
		})).append($('<input>', {
			'name':'bcId',
			'value':'${wbBcBVo.bcId}',
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
	var url = "/wb/attachViewSub.do?menuId=${menuId}&typ=${param.typ}&bcId=${wbBcBVo.bcId}";
	url+='&fileIds='+id;
	openViewAttchFile(url);
	//$m.nav.next(null, url);
}
</c:if>

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
           <c:if test="${listPage ne 'listPubBc' }">
           <c:if test="${listPage eq 'listBc' || ( listPage ne 'listBc' && wbBcAgntAdmBVo.authCd eq 'RW' )}">
			<c:if test="${writeAuth == 'Y'}">
				<c:if test="${listPage eq 'listBc'}">
					<dd class="btn" onclick="fnBumkUpdate('${wbBcBVo.bumkYn eq 'Y' ? 'N' : 'Y' }')"><u:msg titleId="cm.btn.${wbBcBVo.bumkYn eq 'Y' ? 'delBumk' : 'addBumk' }" alt="즐겨찾기에서${wbBcBVo.bumkYn eq 'Y' ? '제거' : '추가' }" />
				</c:if>
		       <dd class="btn" onclick="fnMod();"><u:msg titleId="cm.btn.mod" alt="수정" /></dd>
		       <dd class="btn" onclick="fnDelete();"><u:msg titleId="cm.btn.del" alt="삭제" /></dd>
	       </c:if>
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
            <dd class="tit">${wbBcBVo.bcNm}</dd>
            <dd class="name">${wbBcBVo.compNm}${!empty wbBcBVo.dftCntc ? 'ㅣ':''}${wbBcBVo.dftCntc}</dd>
         </dl>
            </div>
        </div>
        <!--//titlezone E-->

         <!--tabarea S-->
         <div class="tabarea" id="tabBtnArea">
             <div class="tabsize">
                 <dl>
                 <dd class="tab_on" onclick="javascript:fnTabOn(this,'cont');"><u:msg titleId="wb.jsp.setBc.tab.dftInfo" alt="기본정보" /></dd>
                 <dd class="tab" onclick="javascript:fnTabOn(this,'detail');"><u:msg titleId="wb.jsp.setBc.tab.addInfo" alt="추가정보" /></dd>
                 <c:if test="${!empty fileVoList }">
                 	<dd class="tab" onclick="javascript:fnTabOn(this,'attch');"><u:msg titleId="cols.att" alt="첨부" /></dd>
                 </c:if>
                 <c:if test="${!empty wbBcBVo.wbBcImgDVo.imgPath}">
                 	<dd class="tab" onclick="javascript:fnTabOn(this,'photo');"><u:msg titleId="or.txt.photo" alt="사진" /></dd>
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
                    <th class="shead_lt">
                    	<u:msg titleId="cols.mob" alt="휴대" />
                    	<u:msg titleId="cols.phon" alt="전화번호" />
                    </th>
                    <td class="sbody_lt">
						<c:forEach var="list" items="${wbBcBVo.wbBcCntcDVo }" varStatus="status">
							<c:if test="${list.cntcTypCd eq 'mbno'}">
								<u:set var="mbnoCnt" test="${empty mbnoCnt }" value="0" elseValue="1"/>
								<c:if test="${ mbnoCnt > 0 }"><div class="blank10"></div></c:if><m:tel value="${list.cntcCont }"/><br/>
							</c:if>
						</c:forEach>
                    </td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.email" alt="이메일" /></th>
                    <td class="sbody_lt">
						<c:forEach var="list" items="${wbBcBVo.wbBcEmailDVo }" varStatus="status">
							<u:set var="emailCnt" test="${empty emailCnt }" value="0" elseValue="1"/>
							<c:if test="${ emailCnt > 0 }"><div class="blank10"></div></c:if><m:email value="${list.cntcCont }"/><br/>
						</c:forEach>
                    </td>
                </tr>
                <c:forTokens var="phonCntcTypCd" items="compPhon,homePhon" delims="," varStatus="cdStatus">
                <tr>
                    <th class="shead_lt">
                    	<u:msg titleId="cols.${phonCntcTypCd eq 'homePhon' ? 'home': 'comp' }" alt="회사" />
                    	<u:msg titleId="cols.phon" alt="전화번호" />
                    </th>
                    <td class="sbody_lt">
                    	<c:set var="compPhonCnt" value="0"/>
                    	<c:set var="homePhonCnt" value="0"/>
						<c:forEach var="list" items="${wbBcBVo.wbBcCntcDVo }" varStatus="status">
							<c:if test="${list.cntcTypCd eq phonCntcTypCd}">
								<c:if test="${list.cntcTypCd eq 'compPhon' }"><c:set var="compPhonCnt" value="${compPhonCnt+1 }"/></c:if>
								<c:if test="${list.cntcTypCd eq 'homePhon' }"><c:set var="homePhonCnt" value="${homePhonCnt+1 }"/></c:if>
								<c:if test="${ (list.cntcTypCd eq 'compPhon' && compPhonCnt > 1) || ( list.cntcTypCd eq 'homePhon' && homePhonCnt > 1 ) }"><div class="blank10"></div></c:if><m:tel value="${list.cntcCont }"/><br/>
							</c:if>
						</c:forEach>
                    </td>
                </tr>
                </c:forTokens>
                
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.comp" alt="회사" /></th>
                    <td class="shead_lt">${wbBcBVo.compNm}</td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.dept" alt="부서" /></th>
                    <td class="shead_lt">${wbBcBVo.deptNm}${!empty wbBcBVo.deptNm && !empty wbBcBVo.gradeNm ? '/' : ''}${wbBcBVo.gradeNm}</td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.tich" alt="담당업무" /></th>
                    <td class="shead_lt">${wbBcBVo.tichCont}</td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.fno" alt="팩스번호" /></th>
                    <td class="shead_lt">${wbBcBVo.fno}</td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.compAdr" alt="회사주소"/></th>
                    <td class="shead_lt">${wbBcBVo.compZipNo } ${wbBcBVo.compAdr }</td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:term termId="wb.cols.enNm" alt="영문이름" /></th>
                    <td class="shead_lt">${wbBcBVo.bcEnNm}</td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.homeAdr" alt="자택주소"/></th>
                    <td class="shead_lt">${wbBcBVo.homeZipNo } ${wbBcBVo.homeAdr }</td>
                </tr>
                <c:if test="${listPage ne 'listPubBc' }">
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.publYn" alt="공개여부" /></th>
                    <td class="shead_lt">
						<c:choose>
							<c:when test="${wbBcBVo.publTypCd eq 'allPubl' }"><u:msg titleId="cm.option.allPubl" alt="전체공개"/></c:when>
							<c:when test="${wbBcBVo.publTypCd eq 'deptPubl' }"><u:msg titleId="cm.option.deptPubl" alt="부서공개"/></c:when>
							<c:when test="${wbBcBVo.publTypCd eq 'apntPubl' }">
								<u:msg titleId="wb.jsp.viewBc.apntr" arguments="${wbBcBVo.wbBcApntrRVoList[0].userNm },${fn:length(wbBcBVo.wbBcApntrRVoList) -1}"/>						
							</c:when>
							<c:otherwise><u:msg titleId="cm.option.priv" alt="비공개"/></c:otherwise>
						</c:choose>
                    </td>
                </tr>
                </c:if>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.fldNm" alt="폴더명" /></th>
                    <td class="shead_lt">${wbBcBVo.fldId eq 'NONE' ? '' : wbBcBVo.fldNm}</td>
                </tr>
            </tbody>
            </table>
        </div>
        <c:if test="${listPage ne 'listPubBc' }">
        <!--listtablearea S-->
        <div  class="s_tablearea view contCls" >
        	<div class="blank10"></div>
            <table class="s_table" style="table-layout:fixed;">
            <caption><u:msg titleId="wb.jsp.viewBc.metng" alt="관련미팅" /></caption>
            <colgroup>
                <col width="67%"/>
                <col width="33%"/>
            </colgroup>
              <thead>
                  <tr>
                  <th class="shead_ct"><u:msg titleId="cols.subj" alt="제목" /></th>
                  <th class="shead_ct"><u:msg titleId="cols.metngDt" alt="관련미팅일시" /></th>
                  </tr>
              </thead>
            <tbody>
			<c:if test="${!empty wbBcMetngDVoList }">
				<c:forEach var="list" items="${wbBcMetngDVoList }" varStatus="status">
					<tr>
	                    <td class="sbody_lt"><div class="ellipsis" style="width:95%;"><a href="javascript:viewMetng('${list.bcMetngDetlId }');">${list.metngSubj }${list.metngSubj }</a></div></td>
	                    <td class="sbody_ct">${list.metngYmd }</td>
	                </tr>
				</c:forEach>
			</c:if>
            </tbody>
            </table>
        </div>
        </c:if>
        
        <!--//listtablearea E-->   

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
                    <th class="shead_lt"><u:msg titleId="cols.gen" alt="성별" /></th>
                    <td class="shead_lt">${wbBcBVo.genNm }</td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.birth" alt="생년월일" /></th>
                    <td class="shead_lt">
						<c:if test="${!empty wbBcBVo.birth}">(<u:msg titleId="${wbBcBVo.birthSclcCd eq 'LUNA' ? 'cols.luna' : 'cols.sola'}" />)</c:if>
						<u:out value="${wbBcBVo.birth}" type="date"/>
                    </td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.psnHpage" alt="개인홈페이지" /></th>
                    <td class="shead_lt">
						${wbBcBVo.psnHpageUrl}
                    </td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.compHpage" alt="회사홈페이지" /></th>
                    <td class="shead_lt">
						${wbBcBVo.compHpageUrl}
                    </td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.bhist" alt="약력" /></th>
                    <td class="shead_lt">
						${wbBcBVo.bhistCont}
                    </td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.spect" alt="특기사항" /></th>
                    <td class="shead_lt">
						${wbBcBVo.spectCont}
                    </td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.note" alt="비고" /></th>
                    <td class="shead_lt">
						${wbBcBVo.noteCont}
                    </td>
                </tr>
            </tbody>
            </table>
        </div>
        <!--//listtablearea E-->   
        
        <c:if test="${!empty fileVoList }">
		<div class="attachzone view attchCls"  style="display:none;">
		<div class="blank30"></div>
			<div class="attacharea">
				
					<c:forEach items="${fileVoList}" var="fileVo" varStatus="status">
						<m:attach fileName="${fileVo.dispNm}" fileKb="${fileVo.fileSize/1024 }" downFnc="downFile('${fileVo.fileId}','${fileVo.dispNm}');" viewFnc="viewAttchFile('${fileVo.fileId}');"/>
					</c:forEach>
				
			</div>
         </div>  
         </c:if>
         
         <c:if test="${!empty wbBcBVo.wbBcImgDVo.imgPath}">         	
           <div class="bodyzone view photoCls" style="display:none;">
               <div class="bodyarea">
               <dl>
               <dd class="bodytxt" >
               		<div id="userPhoto" onclick="$(this).hide()" style="background:url('${wbBcBVo.wbBcImgDVo.imgPath}') no-repeat 50% 0; background-size:contain; position:absolute; left:6px; right:6px; height:500px; display:none; z-index:2"></div>
					 <c:choose>
						<c:when test="${empty param.popYn }">
							<fmt:parseNumber var="imgWdth" type="number" value="${wbBcBVo.wbBcImgDVo.imgWdth}" />
							<c:if test="${imgWdth > 800}"	>
								<img id="bcImage" src="${_cxPth}${wbBcBVo.wbBcImgDVo.imgPath}" width="88px" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' onclick="$('#userPhoto').show();"/>
							</c:if>
							<c:if test="${imgWdth <= 800}">
								<img id="bcImage" src="${_cxPth}${wbBcBVo.wbBcImgDVo.imgPath}" width="88px" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' onclick="$('#userPhoto').show();"/>
							</c:if>
						</c:when>
						<c:otherwise><img id="bcImage" src="${_cxPth}${wbBcBVo.wbBcImgDVo.imgPath}" width="88px" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' onclick="$('#userPhoto').show();"/></c:otherwise>
					</c:choose>
               </dd>
            </dl>
               </div>
           </div>
		</c:if>
       </div>
       <jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />      
</section>


