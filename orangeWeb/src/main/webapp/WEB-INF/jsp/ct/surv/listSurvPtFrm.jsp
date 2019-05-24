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
	/*var $form = $('#listSurvAdmForm');
	
	$form.attr('method','post');
	$form.attr('action','./viewSurv.do?menuId=${menuId}&survId=' + survId + "&ctId=${ctId}");
	
	$form[0].submit();*/
	parent.view('./viewSurv.do?menuId=${menuId}&survId=' + survId + "&ctId=${ctId}");
	
}

function viewSurvRes(survId, survPrgStatCd){
	/*var $form = $('#listSurvAdmForm');
	
	$form.attr('method','post');
	$form.attr('action','./viewSurvRes.do?menuId=${menuId}&survId='+survId+'&survStatCd='+survPrgStatCd + "&ctId=${ctId}");
	
	$form[0].submit();*/
	parent.view('./viewSurvRes.do?menuId=${menuId}&survId='+survId+'&survStatCd='+survPrgStatCd + "&ctId=${ctId}");
}

function imsiViewSurv(survId, ctId){
	/*var $form = $('#listSurvAdmForm');
	
	$form.attr('method','post');
	$form.attr('action','./setSurvQues.do?menuId=${menuId}&fnc=mod&survId='+survId+'&ctId='+ctId );
	
	$form[0].submit();*/
	
	parent.view('./setSurvQues.do?menuId=${menuId}&fnc=mod&survId='+survId+'&ctId='+ctId );
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

<% // 목록 %>
<table class="ptltable" id="ptltable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
	<tbody>
		<form id="listSurvAdmForm" name="listSurvAdmForm" >
		<tr>
			<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
			<td width="200" class="head_ct"><u:msg titleId="cols.tgt" alt="대상" /></td>
			<td width="80" class="head_ct"><u:msg titleId="cols.stat" alt="상태" /></td>
			<td width="80" class="head_ct"><u:msg titleId="cols.finDt" alt="마감일시" /></td>
		</tr>
	
		<c:forEach var="ctSurvBVo" items ="${ctSurvBMapList}" varStatus="status">
			
			<u:set test="${!empty ctSurvBVo.survTgtM}" var="tgtM" value="${ctSurvBVo.survTgtM}" elseValue="N"></u:set>
			<u:set test="${!empty ctSurvBVo.survTgtS}" var="tgtS" value="${ctSurvBVo.survTgtS}" elseValue="N"></u:set>
			<u:set test="${!empty ctSurvBVo.survTgtR}" var="tgtR" value="${ctSurvBVo.survTgtR}" elseValue="N"></u:set>
			<u:set test="${!empty ctSurvBVo.survTgtA}" var="tgtA" value="${ctSurvBVo.survTgtA}" elseValue="N"></u:set>
			
			<u:set test="${tgtM == myAuth}" var="myAuthChkM"  value="Y" elseValue="N" />
			<u:set test="${tgtS == myAuth}" var="myAuthChkS"  value="Y" elseValue="N" />
			<u:set test="${tgtR == myAuth}" var="myAuthChkR"  value="Y" elseValue="N" />
			<u:set test="${tgtA == myAuth}" var="myAuthChkA"  value="Y" elseValue="N" />
			
			<u:set test="${ctSurvBVo.regrUid == logUserUid}" var="regrAuth"  value="Y" elseValue="N" />	
			
			<c:choose>
				<c:when test="${regrAuth == 'Y' || myAuthChkM == 'Y' || myAuthChkS == 'Y' || myAuthChkR == 'Y' || myAuthChkA == 'Y'}">
					<c:choose>
						<c:when test="${ctSurvBVo.survPrgStatCd == '6'}">
							<c:set var="viewFunction"	value= "imsiViewSurv('${ctSurvBVo.survId}','${ctSurvBVo.ctId}')" />
						</c:when>
						<c:when test="${ctSurvBVo.replyYn == 'O'}">
							<c:set var="viewFunction"	value= "viewSurvRes('${ctSurvBVo.survId}' , '${ctSurvBVo.survPrgStatCd}')" />
						</c:when>
						<c:when test="${ctSurvBVo.replyYn == 'X'}">
							<c:set var="viewFunction"	value= "viewSurv('${ctSurvBVo.survId}')" />
						</c:when>
					</c:choose>
					
				</c:when>
				<c:otherwise>
					<c:set var="viewFunction"	value= "authHaveNot()" />
				</c:otherwise>
			</c:choose>
			
			<input type="hidden" id="openYn" name="openYn" value="${ctSurvBVo.openYn}"	/>
			<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
			<td class="body_lt">
				<div class="ellipsis" title="<u:out value="${ctSurvBVo.subj}" />">
					<a href="javascript:${viewFunction}"><u:out value="${ctSurvBVo.subj}" /></a>
				</div>
			</td>
			
			<u:set test="${!empty ctSurvBVo.survTgtM}" var="tgtM" value="마스터" elseValue=""></u:set>
			<u:set test="${!empty ctSurvBVo.survTgtS}" var="tgtS" value="스텝" elseValue=""></u:set>
			<u:set test="${!empty ctSurvBVo.survTgtR}" var="tgtR" value="정회원" elseValue=""></u:set>
			<u:set test="${!empty ctSurvBVo.survTgtA}" var="tgtA" value="준회원" elseValue=""></u:set>
		
			
			
			<td class="body_ct">
				<c:if test="${!empty tgtM}"><u:msg titleId="ct.cols.mastNm" alt="마스터"/>
					<c:if test="${!empty tgtS || !empty tgtR || !empty tgtA}">
						/
					</c:if>
				</c:if>
				<c:if test="${!empty tgtS}"><u:msg titleId="ct.option.mbshLev1" alt="스텝"/>
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
				<c:when test="${ctSurvBVo.survPrgStatCd == '5'}">
					<td class="body_ct"><u:msg titleId="wv.cols.notsave" alt="미저장"/></td>
				</c:when>
				<c:when test="${ctSurvBVo.survPrgStatCd == '6'}">
					<td class="body_ct"><u:msg titleId="wv.cols.tempSave" alt="임시저장"/></td>
				</c:when>
			</c:choose>
			<td class="body_ct">
				<fmt:parseDate var="dateTempParse" value="${ctSurvBVo.survEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
			</td>
			
			</tr>
		
			
		</c:forEach>
		<c:if test="${fn:length(ctSurvBMapList) == 0}">
				<tr>
					<td class="nodata" colspan="4"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
				</tr>
		</c:if>
		</form>
	</tbody>
</table>
<u:listArea/>
<u:pagination noTotalCount="true"/>

