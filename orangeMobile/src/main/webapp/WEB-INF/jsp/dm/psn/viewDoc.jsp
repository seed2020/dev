<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="saveDisc" test="${!empty param.pltYn && param.pltYn eq 'Y' }" value="saveDiscAjx" elseValue="saveDisc"/>
<u:params var="nonPageParams" excludes="docId,pageNo,docGrpId,noCache"/>
<u:params var="viewPageParams" excludes="docId,docPid,docGrpId,noCache"/>
<u:set var="includeParams" test="${!empty docPid}" value="&docId=${docPid }" elseValue="&docId=${dmDocLVoMap.docId }"/>
<c:set var="exKeys" value="view,update,owner,disuse,saveDisc,keepDdln,seculCd,recycle,fld,cls,docNoMod,setSubDoc,bumk,download,print,email,openApv,version,print"/><!-- 하단 버튼 제외 key -->
<u:set var="adminYn" test="${!empty isAdmin && isAdmin == true }" value="Y" elseValue="N"/>
<script type="text/javascript">
//<![CDATA[
<% // 파일 다운로드 %>
function downFile(id,dispNm) {
	var ids = [];
	ids.push(id);
	var $form = $('<form>', {
			'method':'post',
			'action':'/dm/downFile.do',
			'target':$m.browser.mobile && $m.browser.safari ? '' : 'dataframe'
		}).append($('<input>', {
			'name':'menuId',
			'value':'${menuId}',
			'type':'hidden'
		})).append($('<input>', {
			'name':'actionParam',
			'value':'psn',
			'type':'hidden'
		})).append($('<input>', {
			'name':'fileIds',
			'value':ids,
			'type':'hidden'
		}));
	
	//if($m.browser.naver || $m.browser.daum){
	//	$form.append($('<input>', {'name':'fwd','value':$form.attr('action'),'type':'hidden'}));
	//	$form.attr('action', '/cm/download/dm/'+encodeURI(dispNm));
	//}
	
	$(top.document.body).append($form);
	$m.secu.set();
	$form.submit();
	$form.remove();
}
<c:if test="${viewYn eq 'Y'}">
<% // 문서뷰어 %>
function viewAttchFile(id) {
	var url = "/dm/attachViewSub.do?menuId=${menuId}&actionParam=psn";
	url+='&fileIds='+id;
	openViewAttchFile(url);
	//$m.nav.next(null, url);
}
</c:if>
<%// [팝업] 보내기 %>
function send(){
	var url = '/dm/doc/sendPsnSub.do?docTyp=psn&${params}';
	$m.nav.next(null,url);
};<% // [하단버튼:목록] %>
function listDoc(){
	$m.nav.prev(null, $('#listPage').val());
}<% // [목록:제목] 상세 조회 %>
function viewDoc(id,docGrpId,tgtId) {
	var url = '/dm/doc/${viewPage}.do?${paramsForList }${paramStorIdQueryString}&docId=' + id;
	if(tgtId != undefined) url+= '&tgtId='+tgtId;
	$m.nav.curr(null, url);
}<% // [하단버튼:수정] %>
function setDoc(){
	$m.nav.next(null,'/dm/doc/${setPage}.do?${paramsForList }&docId=${param.docId }');
}<% // [하단버튼:하위문서등록] %>
function setSubDoc(){
	$m.nav.next(null,'/dm/doc/${setPage}.do?${paramsForList }&docPid=${dmDocLVoMap.docGrpId }');
}<%// 상태코드별 메세지[복원,폐기,삭제] %>
function getMsgCd(statCd){
	var arr = [];
	if(statCd == 'C') {
		arr.push("dm.cfrm.recovery");
		arr.push("dm.msg.recovery.success");
		return arr;
	}else if(statCd == 'F'){
		arr.push("dm.cfrm.disuse");
		arr.push("");
		return arr;
	}
	arr.push("cm.cfrm.del");
	arr.push("");
	return arr;
}<% // [하단버튼:삭제] %>
function delDocTrans(param) {
	$m.msg.confirmMsg("cm.cfrm.del",null,function(result){
		if(!result) return;
		$m.ajax('/dm/doc/transPsnDocDelAjx.do?menuId=${menuId}', param, function(data) {
			if (data.message != null) {
				$m.dialog.alert(data.message);
			}
			if (data.result == 'ok') {
				$m.nav.curr(null, $('#listPage').val());
			}
		});
	})
}<% // [하단버튼:삭제] 문서%>
function delDoc(statCd) {
	delDocTrans({docId:$('#docId').val(),statCd:statCd});
}
$(document).ready(function() {
	<%// 본문의 넓이를 맞춤 %>
	$layout.adjustBodyHtml('bodyHtmlArea');
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('tabview');
});
//]]>
</script>


<!--section S-->
<section>
	<c:set var="voMap" value="${dmDocLVoMap }" scope="request"/>
	<m:input type="hidden" name="menuId" value="${menuId}" />
	<m:input type="hidden" id="listPage" value="/dm/doc/${listPage}.do?${paramsForList}" />
	<m:input type="hidden" id="viewPage" value="/dm/doc/${viewPage}.do?${viewPageParams}${includeParams }" />
	<m:input type="hidden" id="viewRefPage" value="/dm/doc/${viewPage}.do?${paramsForList}&docId=${refDocId }" />
	<m:input type="hidden" id="docId" value="${dmDocLVoMap.docId }" />
	<m:input type="hidden" id="docGrpId" value="${dmDocLVoMap.docGrpId }" />
       <!--btnarea S-->
       <div class="btnarea" id="btnArea">
           <div class="size">
           <dl>
               <u:secu auth="W">
               <dd class="btn" onclick="send();"><u:msg titleId="dm.cols.auth.send" alt="보내기" /></dd>
               <dd class="btn" onclick="setDoc();"><u:msg titleId="cm.btn.mod" alt="수정" /></dd>
               <dd class="btn" onclick="delDoc('F');"><u:msg titleId="cm.btn.del" alt="삭제" /></dd>
               </u:secu>
               <dd class="btn" onclick="listDoc();"><u:msg titleId="cm.btn.list" alt="목록" /></dd>
               <%-- <dd class="btn" onclick="history.go(-1);"><u:msg titleId="cm.btn.back" alt="뒤로" /></dd> --%>
        </dl>
      	 </div>
       </div>
		<!--titlezone S-->
        <div class="titlezone">
            <div class="titarea">
            <dl>
            <dd class="tit">${dmDocLVoMap.subj}</dd>
            <dd class="name">
            	<u:out value="${dmDocLVoMap.fldNm}" />ㅣ<u:out value="${dmDocLVoMap.regDt}" type="longdate" />
            </dd>
         	</dl>
            </div>
        </div>
        <!--//titlezone E-->
        
         <!--tabarea S-->
         <div class="tabarea" id="tabBtnArea">
             <div class="tabsize">
                 <dl>
                 <dd class="tab_on" onclick="$layout.tab.on($(this).attr('id'))" id="body"><u:msg titleId="cols.body" alt="본문" /></dd>
                 <dd class="tab" onclick="$layout.tab.on($(this).attr('id'))" id="detl"><u:msg titleId="cm.btn.detl" alt="상세" /></dd>
                 <c:if test="${!empty fileVoList }">
                 	<dd class="tab" onclick="$layout.tab.on($(this).attr('id'))" id="attch"><u:msg titleId="cols.att" alt="첨부" /></dd>
                 </c:if>
              </dl>
             </div>
			<div class="tab_icol" style="display:none" id="toLeft"></div>
			<div class="tab_icor" style="display:none" id="toRight"></div>
         </div>
         <!--//tabarea E-->
			
		<div id="tabViewArea">
            <!--bodyzone_scroll S-->
            <div class="bodyzone_scroll" id="body">
                <div class="bodyarea">
                <dl>
                <dd class="bodytxt_scroll"><div class="scroll editor" id="bodyHtmlArea">
                	<div id="zoom"><u:clob lobHandler="${lobHandler }"/></div>
                </div>
                </dd>
	            </dl>
                </div>
            </div>
            <!--//bodyzone_scroll E-->

        <!--listtablearea S-->
        <div  class="s_tablearea" id="detl" style="display:none;">
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
                    <td class="shead_lt"><u:out value="${dmDocLVoMap.subj}" /></td>
            	</tr>
            	<tr>
            		<th class="shead_lt"><u:msg titleId="cols.fld" alt="폴더" /></th>
                    <td class="shead_lt"><c:if test="${empty dmDocLVoMap.fldNm }"><u:msg titleId="dm.msg.not.save.emptyCls" alt="미분류"/></c:if>${dmDocLVoMap.fldNm}</td>
            	</tr>
            	
				<c:if test="${!empty itemDispList }">
					<!-- 확장컬럼 -->
					<c:forEach var="dispVo" items="${itemDispList }" varStatus="status">
						<c:set var="colmVo" value="${dispVo.colmVo}" />
						<c:set var="itemTyp" value="${colmVo.itemTyp}" />
						<c:set var="itemNm" value="${dispVo.atrbId}" />
						<u:convertMap var="docVal" srcId="voMap" attId="${itemNm }" />
						<tr>
							<th class="shead_lt">${colmVo.itemDispNm }</th>
							<td class="shead_lt">
								<c:if test="${itemTyp != 'CODE'}">${docVal }</c:if>
								<c:if test="${itemTyp == 'CODE'}">
									<c:forEach items="${colmVo.cdList}" var="cd" varStatus="status">
										<c:if test="${cd.cdId == docVal}">${cd.rescNm}</c:if>
									</c:forEach>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</c:if>
				<tr>
                    <th class="shead_lt"><u:msg titleId="cols.regDt" alt="등록일시" /></th>
                    <td class="shead_lt"><u:out value="${dmDocLVoMap.regDt}" type="longdate" /></td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="cols.modDt" alt="수정일시" /></th>
                    <td class="shead_lt"><u:out value="${dmDocLVoMap.modDt}" type="longdate" /></td>
                </tr>
            </tbody>
            </table>
        </div>
        <!--//listtablearea E-->        
		
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
		<% // 이전글 다음글 %>
       	<div class="prevnextarea">
               <div class="prev">
               	<dl>
				<c:if test="${prevVo == null}">
					<dd class="tit"><u:msg titleId="cm.ico.prev" alt="이전글" /></dd>
					<dd class="body"><u:msg titleId="bb.jsp.viewBull.prevNotExists" alt="이전글이 존재하지 않습니다." /></dd>
				</c:if>
				<c:if test="${prevVo != null}">
					<dd class="tit"><u:msg titleId="cm.ico.prev" alt="이전글" /></dd>
					<dd class="body" onclick="viewDoc('${prevVo.docId}');">
					<u:out value="${prevVo.subj}" maxLength="80" />
					</dd>
				</c:if>
				</dl>
			</div>
			<div class="next">
				<dl>
				<c:if test="${nextVo == null}">
					<dd class="tit"><u:msg titleId="cm.ico.next" alt="다음글" /></dd>
					<dd class="body"><u:msg titleId="bb.jsp.viewBull.nextNotExists" alt="다음글이 존재하지 않습니다." /></dd>
				</c:if>
				<c:if test="${nextVo != null}">
					<dd class="tit"><u:msg titleId="cm.ico.next" alt="다음글" /></dd>
					<dd class="body" onclick="viewDoc('${nextVo.docId}');">
					<u:out value="${nextVo.subj}" maxLength="80" />
					</dd>
				</c:if>
				</dl>
			</div>
      	 	</div>

		<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
    
</section>
<!--//section E-->

