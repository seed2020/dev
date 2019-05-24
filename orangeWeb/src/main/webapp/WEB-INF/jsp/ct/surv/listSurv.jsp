<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

function authHaveNot(){
	alert("<u:msg titleId="wv.msg.set.noRight" alt="투표 및 조회권한이 없습니다."/>");
	return;
}


function viewSurv(survId){
	var $form = $('#listSurvAdmForm');
	
	$form.attr('method','post');
	$form.attr('action','./viewSurv.do?menuId=${menuId}&survId=' + survId + "&ctId=${ctId}");
	
	$form[0].submit();
	
}

function viewSurvRes(survId, survPrgStatCd, repetYn){
	var $form = $('#listSurvAdmForm');
	
	$form.attr('method','post');
	$form.attr('action','./viewSurvRes.do?menuId=${menuId}&survId='+survId+'&survStatCd='+survPrgStatCd + "&ctId=${ctId}&repetYn=" + repetYn);
	
	$form[0].submit();
}

function imsiViewSurv(survId, ctId){
	var $form = $('#listSurvAdmForm');
	
	$form.attr('method','post');
	//$form.attr('action','./setSurvQues.do?menuId=${menuId}&fnc=mod&survId='+survId+'&ctId='+ctId );
	$form.attr('action','./setSurv.do?menuId=${menuId}&fnc=mod&survId='+survId+'&ctId='+ctId );	
	$form[0].submit();
}

function survAdmDel(survId, ctId){
	
	 if(confirmMsg("cm.cfrm.del")) { <% // cm.cfrm.del=삭제하시겠습니까? %>
			callAjax('./transSetSurvDel.do?menuId=${menuId}', {survId:survId, ctId:ctId}, function(data) {
				if (data.message != null) {
					alert(data.message);
				}
				if (data.result == 'ok') {
					//dialog.close(this);
					location.href = './listSurv.do?menuId=${menuId}&ctId=' + ctId;
				}
			});
	 }
}


function setSurvPop(){
	var $form = $('#listSurvAdmForm');
	
	$form.attr('method','post');
	$form.attr('action','./setSurv.do?menuId=${menuId}&fnc=reg&ctId=${ctId}');
	
	$form[0].submit();
}


$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title title="${menuTitle }" alt="투표목록" menuNameFirst="true"/>

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listSurv.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="ctId" value="${ctId}" />
	

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td>
			<select name="schCat">
				<u:option value="subj" titleId="cols.subj" alt="제목" checkValue="${param.schCat}" />
				<u:option value="survItnt" titleId="cols.itnt" alt="취지" checkValue="${param.schCat}" />
			</select>
		</td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 300px" onkeydown="if (event.keyCode == 13) searchForm.submit();"/></td>
		<td class="width20"></td>
		<td><select id="schCtStat" name="schCtStat" >
			<option value=""  <c:if test="${schCtStat == '0'}">selected="selected"</c:if>><u:msg titleId="wv.cols.stat" alt="상태"/></option>
			<option value="1" <c:if test="${schCtStat == '1'}">selected="selected"</c:if>><u:msg titleId="wv.cols.ready" alt="준비중"/></option>
			<%-- <option value="2" <c:if test="${schCtStat == '2'}">selected="selected"</c:if>><u:msg titleId="wv.cols.appr" alt="승인중" /></option> --%>
			<option value="3" <c:if test="${schCtStat == '3'}">selected="selected"</c:if>><u:msg titleId="wv.cols.ing" alt="진행중"/></option>
			<option value="4" <c:if test="${schCtStat == '4'}">selected="selected"</c:if>><u:msg titleId="wv.cols.end" alt="마감"/></option>
			<%-- <option value="5" <c:if test="${schCtStat == '5'}">selected="selected"</c:if>><u:msg titleId="wv.cols.notsave" alt="미저장"/></option> --%>
			<option value="6" <c:if test="${schCtStat == '6'}">selected="selected"</c:if>><u:msg titleId="wv.cols.tempSave" alt="임시저장"/></option>
			<%-- <option value="9" <c:if test="${schCtStat == '9'}">selected="selected"</c:if>><u:msg titleId="wv.cols.rjt" alt="반려"/></option> --%>
			</select></td>
		</tr>
		 
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<% // 목록 %>
<div class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="width:100%;table-layout:fixed;">
		<form id="listSurvAdmForm" name="listSurvAdmForm" >
			<tr>
				<td width="50" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
				<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
				<td width="200" class="head_ct"><u:msg titleId="cols.tgt" alt="대상" /></td>
				<td width="80" class="head_ct"><u:msg titleId="cols.stat" alt="상태" /></td>
				<td width="80" class="head_ct"><u:msg titleId="cols.finDt" alt="마감일시" /></td>
				<td width="80" class="head_ct"><u:msg titleId="cols.ansCnt" alt="참여인원" /></td>
				<td width="70" class="head_ct"><u:msg titleId="cols.ansYn" alt="응답여부" /></td>
				<td width="90" class="head_ct"><u:msg titleId="cols.mng" alt="관리" /></td>
			</tr>
		
			<c:forEach var="ctSurvBVo" items ="${ctSurvBMapList}" varStatus="status">
				
				<u:set test="${!empty ctSurvBVo.survTgtM}" var="tgtM" value="${ctSurvBVo.survTgtM}" elseValue="N"></u:set>
				<u:set test="${!empty ctSurvBVo.survTgtS}" var="tgtS" value="${ctSurvBVo.survTgtS}" elseValue="N"></u:set>
				<u:set test="${!empty ctSurvBVo.survTgtR}" var="tgtR" value="${ctSurvBVo.survTgtR}" elseValue="N"></u:set>
				<u:set test="${!empty ctSurvBVo.survTgtA}" var="tgtA" value="${ctSurvBVo.survTgtA}" elseValue="N"></u:set>
				
				<u:set test="${tgtM == ctSurvBVo.ctMyAuth}" var="myAuthChkM"  value="Y" elseValue="N" />
				<u:set test="${tgtS == ctSurvBVo.ctMyAuth}" var="myAuthChkS"  value="Y" elseValue="N" />
				<u:set test="${tgtR == ctSurvBVo.ctMyAuth}" var="myAuthChkR"  value="Y" elseValue="N" />
				<u:set test="${tgtA == ctSurvBVo.ctMyAuth}" var="myAuthChkA"  value="Y" elseValue="N" />
				
				<u:set test="${ctSurvBVo.regrUid == logUserUid || ctSurvBVo.modrUid == logUserUid }" var="regrAuth"  value="Y" elseValue="N" />	
				
				<c:choose>
					
					<c:when test="${regrAuth == 'Y' || myAuthChkM == 'Y' || myAuthChkS == 'Y' || myAuthChkR == 'Y' || myAuthChkA == 'Y'}">
						<c:choose>
							<c:when test="${ctSurvBVo.survPrgStatCd == '6'}">
								<c:set var="viewFunction"	value= "imsiViewSurv('${ctSurvBVo.survId}','${ctSurvBVo.ctId}')" />
							</c:when>
							<c:when test="${ctSurvBVo.survPrgStatCd == '4'}">
								<c:set var="viewFunction"	value= "viewSurvRes('${ctSurvBVo.survId}' , '${ctSurvBVo.survPrgStatCd}','${ctSurvBVo.repetSurvYn}')" />
							</c:when>
							<c:when test="${ctSurvBVo.survPrgStatCd == '3'}">
								<c:choose>
									<c:when test="${ctSurvBVo.replyYn == 'O'}">
										<c:set var="viewFunction"	value= "viewSurvRes('${ctSurvBVo.survId}' , '${ctSurvBVo.survPrgStatCd}','${ctSurvBVo.repetSurvYn}')" />
									</c:when>
									<c:when test="${ctSurvBVo.replyYn == 'X'}">
										<c:set var="viewFunction"	value= "viewSurv('${ctSurvBVo.survId}')" />
									</c:when>
								</c:choose>
							</c:when>
						</c:choose>
						
					</c:when>
					<c:otherwise>
						<c:set var="viewFunction"	value= "authHaveNot()" />
					</c:otherwise>
				</c:choose>
				
				<input type="hidden" id="openYn" name="openYn" value="${ctSurvBVo.openYn}"	/>
				<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
				<td class="body_ct"><u:out value="${recodeCount - ctSurvBVo.rnum + 1}" type="number" /></td>
				<td class="body_lt">
					<div class="ellipsis" title="${ctSurvBVo.subj}">
						<a href="javascript:${viewFunction}"><u:out value="${ctSurvBVo.subj}" /></a>
					</div>
				</td>
				
				<u:set test="${!empty ctSurvBVo.survTgtM}" var="tgtM" value="마스터" elseValue=""></u:set>
				<u:set test="${!empty ctSurvBVo.survTgtS}" var="tgtS" value="스텝" elseValue=""></u:set>
				<u:set test="${!empty ctSurvBVo.survTgtR}" var="tgtR" value="정회원" elseValue=""></u:set>
				<u:set test="${!empty ctSurvBVo.survTgtA}" var="tgtA" value="준회원" elseValue=""></u:set>
			
				
				
				<td class="body_ct">
					<c:if test="${!empty tgtM}">
						<u:msg titleId="ct.cols.mastNm" alt="마스터"/>
						<c:if test="${!empty tgtS || !empty tgtR || !empty tgtA}">
							/
						</c:if>
					</c:if>
					<c:if test="${!empty tgtS}">
						<u:msg titleId="ct.option.mbshLev1" alt="스텝"/>
						<c:if test="${!empty tgtR || !empty tgtA}">
							/
						</c:if>
					</c:if>
					<c:if test="${!empty tgtR}">
						<u:msg titleId="ct.option.mbshLev2" alt="정회원"/>
						<c:if test="${!empty tgtA}">
							/
						</c:if>
					</c:if>
					<c:if test="${!empty tgtA}">
						<u:msg titleId="ct.option.mbshLev3" alt="준회원"/>
					</c:if>
				</td>
					
				<c:choose>
					<c:when test="${ctSurvBVo.survPrgStatCd == '1'}">
						<td class="body_ct"><u:msg titleId="wv.cols.ready" alt="준비중"/></td>
					</c:when>
						<c:when test="${ctSurvBVo.survPrgStatCd == '3'}">
						<td class="body_ct"><u:msg titleId="wv.cols.ing" alt="진행중"/></td>
					</c:when>
					<c:when test="${ctSurvBVo.survPrgStatCd == '4'}">
						<td class="body_ct"><u:msg titleId="wv.cols.end" alt="마감"/></td>
					</c:when>
					<c:when test="${ctSurvBVo.survPrgStatCd == '6'}">
						<td class="body_ct"><u:msg titleId="wv.cols.tempSave" alt="임시저장"/></td>
					</c:when>
				</c:choose>
				<td class="body_ct">
					<fmt:parseDate var="dateTempParse" value="${ctSurvBVo.survEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
					<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
				</td>
				<c:if test="${ctSurvBVo.joinUserCnt != null}">
					<td class="body_ct">${ctSurvBVo.joinUserCnt}</td>
				</c:if>
				<c:if test="${ctSurvBVo.joinUserCnt == null}">
					<td class="body_ct">0</td>
				</c:if>
				
				<td class="body_ct">${ctSurvBVo.replyYn}</td>
				<td><table align="center" border="0" cellpadding="0" cellspacing="0"><tbody>
					<tr>
					<c:choose>
						<c:when test="${ctSurvBVo.regrUid == logUserUid || ctSurvBVo.modrUid == logUserUid || ctSurvBVo.ctMyAuth == 'M'}">
							<c:choose>
								<c:when test="${ctSurvBVo.survPrgStatCd == '3'}">
								</c:when>
								<c:when test="${ctSurvBVo.survPrgStatCd == '4'}">
									<td><u:buttonS  href="javascript:survAdmDel('${ctSurvBVo.survId}','${ctSurvBVo.ctId}');" titleId="cm.btn.del" alt="삭제" /></td>
								</c:when>
								<c:otherwise>
									<td><u:buttonS href="./setSurv.do?menuId=${menuId}&survId=${ctSurvBVo.survId}&fnc=mod&ctId=${ctId}" titleId="cm.btn.mod" alt="수정"/></td>
									<td><u:buttonS  href="javascript:survAdmDel('${ctSurvBVo.survId}','${ctSurvBVo.ctId}');" titleId="cm.btn.del" alt="삭제" /></td>
								</c:otherwise>
							</c:choose>
						</c:when>
					</c:choose>
					</tr>
					</tbody></table></td>
				</tr>
				
			</c:forEach>
			<c:if test="${fn:length(ctSurvBMapList) == 0}">
					<tr>
						<td class="nodata" colspan="8"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
					</tr>
			</c:if>
		</form>
	</table>
</div>
<u:blank />

<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
	<c:if test="${!empty authChkW && authChkW == 'W' }">
		<u:button titleId="cm.btn.reg" alt="등록" href="javascript:setSurvPop();"  />
	</c:if>
</u:buttonArea>

