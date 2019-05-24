<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="params" />
<jsp:useBean id="now" class="java.util.Date" />
<script type="text/javascript">
<!--  

function imsiMod(survId){
	var $form = $('#listSurvForm');
	
	$form.attr('method','post');
	$form.attr('action','./adm/setSurv.do?menuId=${menuId}&fnc=mod&survId='+survId);
	
	$form[0].submit();
}
function setSurvPop(){
	
	var $form = $('#listSurvForm');
	
	$form.attr('method','post');
	$form.attr('action','./adm/setSurv.do?menuId=${menuId}&fnc=reg');
	
	$form[0].submit();
}

function resultViewNo(){
	alert("<u:msg titleId="wv.msg.set.notPub" alt="결과 비공개 설문입니다."/>");
	return;
}

function authHaveNot(){
	alert("<u:msg titleId="wv.msg.set.noRight" alt="투표 및 조회권한이 없습니다."/>");
	return;
}

function viewSurv(survId){
	var $form = $('#listSurvForm');
	
	$form.attr('method','post');
	$form.attr('action','./viewSurv.do?menuId=${menuId}&survId=' + survId);
	
	$form[0].submit();
	
}

function viewSurvRepet(survId){
	
	if(!confirm("<u:msg titleId="wv.msg.set.repetCfm" alt="재설문 하시겠습니까"/>"))
		return;
	
	var $form = $('#listSurvForm');
	
	$form.attr('method','post');
	$form.attr('action','./viewSurv.do?menuId=${menuId}&survId=' + survId);
	
	$form[0].submit();
	
}

function viewSurvRes(survId, repetYn, openYn){
	var $form = $('#listSurvForm');
	
	$form.attr('method','post');
	$form.attr('action','./viewSurvRes.do?menuId=${menuId}&survId=' + survId+'&repetYn='+repetYn + '&openYn=' + openYn);
	
	$form[0].submit();
	
}


$(document).ready(function() {
	setUniformCSS();
});
-->
</script>

<u:title titleId="wv.jsp.listSurv.title" alt="설문목록" menuNameFirst="true"/>

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listSurv.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="logUserUid" value="${logUserUid}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">
				<u:option value="survSubj" titleId="cols.subj" alt="제목" checkValue="${param.schCat}" />
				<u:option value="survItnt" titleId="cols.itnt" alt="취지" checkValue="${param.schCat}" />
			</select></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 300px" onkeydown="if (event.keyCode == 13) searchForm.submit();"/></td>

		<td class="width20"></td>
		<td class="search_tit"><u:msg titleId="cols.stat" alt="상태" /></td>
		<td>
			<select id="schCtStat" name="schCtStat" style="width:220px;">
				<u:option value="ALL" titleId="cm.option.all" alt="전체선택" selected="${param.schCtStat == 'ALL'}"/>
				<u:option value="3" titleId="wv.cols.ing" alt="진행중" selected="${param.schCtStat == '3' || param.schCtStat == null}"/>
				<u:option value="4" titleId="wv.cols.end" alt="마감" selected="${param.schCtStat == '4'}"/>
			</select>
		</td>
		
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	<!--  
	<div class="button_search"><ul><li class="search"><a href="javascript:document.searchForm.submit()"><span>검색</span></a></li></ul></div></td>
	-->
	</tr>
	</table>
	</form>
</u:searchArea>




<% // 목록 %>

<div class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="width:100%;table-layout:fixed;">
		<form id="listSurvForm" name="listSurvForm" >
			<tr>
				<td class="head_ct" width="5%"><u:msg titleId="cols.no" alt="번호" /></td>
				<td class="head_ct" width="45%"><u:msg titleId="cols.subj" alt="제목" /></td>
				<td class="head_ct" width="10%"><u:msg titleId="cols.strtDt" alt="시작일시" /></td>
				<td class="head_ct" width="10%"><u:msg titleId="cols.finDt" alt="마감일시" /></td>
				<td class="head_ct" width="10%"><u:msg titleId="cols.ansCnt" alt="참여인원" /></td>
				<td class="head_ct" width="10%"><u:msg titleId="cols.stat" alt="상태" /></td>
				<td class="head_ct" width="10%"><u:msg titleId="wv.cols.ansYn" alt="응답여부" /></td>
				<!-- 
				<td class="head_ct"><u:msg titleId="cols.mng" alt="관리" /></td>
				 -->
			</tr>
			<!-- 
					authTgtTypCd : "D(dept) = 부서" , "U(user) = 사용자" 
				 	authGradCd   : "W(write)= 투표" , "R(read) = 조회" 	 
			-->
			<c:forEach var="wvSurvBVo" items ="${wvSurvBMapList}" varStatus="status">
				<c:set var="survW" value=""></c:set>
				<c:set var="survR" value=""></c:set>
				<c:forEach var="wvSurvAuthList" items ="${wvSurvBVo.survAuthList}" varStatus="authState">
					<!-- authTgtTypCd : "D(dept) = 부서" 일 때 -->
						<c:if test="${wvSurvAuthList.authTgtTypCd == 'D'}">
							<c:if test="${wvSurvAuthList.authTgtUid == logUserDeptId || ( !empty wvSurvAuthList.authInclYn && wvSurvAuthList.authInclYn eq 'Y' && !empty orgPidsToString && fn:contains(orgPidsToString, wvSurvAuthList.authTgtUid))}">
								<c:if test="${wvSurvAuthList.authGradCd == 'W'}">
									<c:if test="${wvSurvAuthList.authTgtUid != ''}">
										<c:set var="chkDW" value="Y"></c:set>
										<c:set var="valueW" value="W"></c:set>
									</c:if>
								</c:if>
								<c:if test="${wvSurvAuthList.authGradCd == 'R'}">
									<c:if test="${wvSurvAuthList.authTgtUid != ''}">
										<c:set var="chkDR" value="Y"></c:set>
										<c:set var="valueR" value="R"></c:set>
									</c:if>
								</c:if>
							</c:if>
						</c:if>
						<!-- authTgtTypCd : "U(user) = 사용자" 일 때 -->
						<c:if test="${wvSurvAuthList.authTgtTypCd == 'U'}">
							<c:if test="${wvSurvAuthList.authTgtUid == logUserUid}">
								<c:if test="${wvSurvAuthList.authGradCd == 'W'}">
									<c:if test="${wvSurvAuthList.authTgtUid != ''}">
										<c:set var="chkUW" value="Y"></c:set>
										<c:set var="valueW" value="W"></c:set>
									</c:if>
								</c:if>
								<c:if test="${wvSurvAuthList.authGradCd == 'R'}">
									<c:if test="${wvSurvAuthList.authTgtUid != ''}">
										<c:set var="chkUR" value="Y"></c:set>
										<c:set var="valueR" value="R"></c:set>
									</c:if>
								</c:if>
							</c:if>	
						</c:if>
						
						<c:if test="${wvSurvAuthList.authGradCd == 'W'}">
							<c:set var="survW" value="Y"></c:set>
						</c:if>
						<c:if test="${wvSurvAuthList.authGradCd == 'R'}">
							<c:set var="survR" value="Y"></c:set>
						</c:if>
				</c:forEach>
				
				<c:set var="checkDR" value="${chkDR}"></c:set>
				<c:set var="checkDW" value="${chkDW}"></c:set>
				<c:set var="checkUR" value="${chkUR}"></c:set>
				<c:set var="checkUW" value="${chkUW}"></c:set>
				<!-- 권한 테이블에 권한 등록이 없거나 등록자가 본인 이거나 하면 RW의 권한을 갖는다. -->
				<c:if test="${wvSurvBVo.regrUid == logUserUid || wvSurvBVo.modrUid == logUserUid}">
					<c:if test="${checkDR eq null}">
						<c:set var="valueR" value="R"></c:set>
					</c:if>
					<c:if test="${checkDW eq null}">
						<c:set var="valueW" value="W"></c:set>
					</c:if>
					<c:if test="${checkUR eq null}">
						<c:set var="valueR" value="R"></c:set>
					</c:if>
					<c:if test="${checkUW eq null}">
						<c:set var="valueW" value="W"></c:set>
					</c:if>
				</c:if>
				<c:if test="${fn:length(wvSurvBVo.survAuthList) == 0 }">
						<c:set var="valueR" value="NR"></c:set>
						<c:set var="valueW" value="NW"></c:set>
				</c:if>
				
			 	<c:set var="R" value="${valueR}"></c:set>
				<c:set var="W" value="${valueW}"></c:set>
				<c:set var="RW" value="${valueR}${valueW}"></c:set>
				
				
				<c:choose>
					<c:when test="${RW == 'R' }"> <% // 사용자권한이 조회만 존재시 설문에 투표권한 여부확인, 설문에 설정이 안되었다면 투표는 공개임. %>
						<c:if test="${survW != 'Y'}">
							<c:set var="RW" value="RW"></c:set>
						</c:if>
					</c:when>  
					<c:when test="${RW == 'W'}"> <% // 사용자권한이 투표만 존재시 설문에 조회권한 여부확인, 설문에 설정이 안되었다면 조회는 공개임. %>
						<c:if test="${survR != 'Y'}">
							<c:set var="RW" value="RW"></c:set>
						</c:if>
					</c:when>
					<c:when test="${RW == ''}"> <% // 사용자권한이 전혀 없을때 %>
						<c:if test="${survW == 'Y' && survR != 'Y'}"> <% // 설문에 조회권한설정이 안되었다면, 조회는 공개임 %>
							<c:set var="RW" value="R"></c:set>
						</c:if>
						<c:if test="${survW != 'Y' && survR == 'Y'}"> <% // 설문에 투표권한설정이 안되었다면, 투표는 공개임 %>
							<c:set var="RW" value="W"></c:set>
						</c:if>
					</c:when>
				</c:choose>
				

				<!-- 
						openYn : "결과 공개 여부"
						repetSurvYn : "재설문 여부" 
						replyYn: "응답 여부"
				-->
				<c:if test="${wvSurvBVo.survPrgStatCd == '3'}">
					<c:choose>
						<c:when test="${RW == 'R'}">
							<c:set var="viewFunction"	value= "viewSurvRes('${wvSurvBVo.survId}', 'N', '${wvSurvBVo.openYn}')" />
						</c:when>
						<c:when test="${RW == 'W'}">
							<c:choose>
								<c:when test="${wvSurvBVo.replyYn == 'O'}">
									<c:choose>
										<c:when test="${wvSurvBVo.repetSurvYn == 'Y'}">
											<c:set var="viewFunction"	value= "viewSurvRepet('${wvSurvBVo.survId}')" />
										</c:when>
										<c:when test="${wvSurvBVo.repetSurvYn == 'N'}">
												<c:set var="viewFunction"	value= "resultViewNo()" />
										</c:when>
									</c:choose>
								</c:when>
								<c:when test="${wvSurvBVo.replyYn == 'X'}">
									<c:set var="viewFunction"	value= "viewSurv('${wvSurvBVo.survId}')" />
								</c:when>
							</c:choose>
						</c:when>
						<c:when test="${RW == 'RW'}">
							<c:choose>
								<c:when test="${wvSurvBVo.replyYn == 'O'}">
									<c:choose>
										<c:when test="${wvSurvBVo.repetSurvYn == 'Y'}">
											<c:set var="viewFunction"	value= "viewSurvRes('${wvSurvBVo.survId}', 'Y', '${wvSurvBVo.openYn}')" />
										</c:when>
										<c:when test="${wvSurvBVo.repetSurvYn == 'N'}">
											<c:set var="viewFunction"	value= "viewSurvRes('${wvSurvBVo.survId}', 'N', '${wvSurvBVo.openYn}')" />
										</c:when>
									</c:choose>
								</c:when>
								<c:when test="${wvSurvBVo.replyYn == 'X'}">
									<c:set var="viewFunction"	value= "viewSurv('${wvSurvBVo.survId}')" />
								</c:when>
							</c:choose>
						</c:when>
						<c:when test="${RW == 'NRNW'}">
							<c:choose>
								<c:when test="${wvSurvBVo.replyYn == 'O'}">
									<c:choose>
										<c:when test="${wvSurvBVo.repetSurvYn == 'Y'}">
											<c:set var="viewFunction"	value= "viewSurvRes('${wvSurvBVo.survId}', 'Y', '${wvSurvBVo.openYn}')" />
										</c:when>
										<c:when test="${wvSurvBVo.repetSurvYn == 'N'}">
											<c:choose>
												<c:when test="${wvSurvBVo.openYn == 'Y'}">
													<c:set var="viewFunction"	value= "viewSurvRes('${wvSurvBVo.survId}', 'N', '${wvSurvBVo.openYn}')" />
												</c:when>
												<c:when test="${wvSurvBVo.openYn == 'N'}">
													<c:set var="viewFunction"	value= "resultViewNo()" />
												</c:when>
											</c:choose>
										</c:when>
									</c:choose>
								</c:when>
								<c:when test="${wvSurvBVo.replyYn == 'X'}">
									<c:set var="viewFunction"	value= "viewSurv('${wvSurvBVo.survId}')" />
								</c:when>
							</c:choose>
						</c:when>
						<c:otherwise>
							<c:set var="viewFunction"	value= "authHaveNot()" />
						</c:otherwise>
					</c:choose> 
					
				</c:if>
				<c:if test="${wvSurvBVo.survPrgStatCd == '4'}">
					<c:choose>
						<c:when test="${RW == 'R'}">
							<c:set var="viewFunction"	value= "viewSurvRes('${wvSurvBVo.survId}', 'N','${wvSurvBVo.openYn}')" />
						</c:when>
						<c:when test="${RW == 'W'}">
							<%-- <c:set var="viewFunction"	value= "resultViewNo()" /> --%>
							<c:set var="viewFunction"	value= "viewSurvRes('${wvSurvBVo.survId}', 'N','${wvSurvBVo.openYn}')" />
						</c:when>
						<c:when test="${RW == 'RW'}">
							<c:set var="viewFunction"	value= "viewSurvRes('${wvSurvBVo.survId}', 'N' , '${wvSurvBVo.openYn}')" />
						</c:when>
						<c:when test="${RW == 'NRNW'}">
							<c:choose>
								<c:when test="${wvSurvBVo.openYn == 'Y'}">
									<c:set var="viewFunction"	value= "viewSurvRes('${wvSurvBVo.survId}', 'N', '${wvSurvBVo.openYn}')" />
								</c:when>
								<c:when test="${wvSurvBVo.openYn == 'N'}">
									<c:set var="viewFunction"	value= "resultViewNo()" />
								</c:when>
							</c:choose>
						</c:when>
						<c:otherwise>
							<c:set var="viewFunction"	value= "authHaveNot()" />
						</c:otherwise>
					</c:choose>
				</c:if>
				<c:if test="${wvSurvBVo.survPrgStatCd == '6'}">
					<c:if test="${wvSurvBVo.regrUid == logUserUid || wvSurvBVo.modrUid == logUserUid}">
						<c:set var="viewFunction"	value= "imsiMod('${wvSurvBVo.survId}')" />
					</c:if>
				</c:if>
				<c:set var="valueW" value=""></c:set>
				<c:set var="valueR" value=""></c:set>
				<tr onmouseover='this.className="trover"' id="${wvSurvBVo.survId}" onmouseout='this.className="trout"'>
				  <td class="body_ct"><u:out value="${recodeCount - wvSurvBVo.rnum + 1}" type="number" /></td>
				<td class="body_lt">
					<div class="ellipsis" title="${wvSurvBVo.survSubj}">
						<a href="javascript:${viewFunction}"><u:out value="${wvSurvBVo.survSubj}"/></a>
					</div>
				</td>
				<td class="body_ct">
				<fmt:parseDate var="dateTempParse" value="${wvSurvBVo.survStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
				</td>
				<td class="body_ct">
				<fmt:parseDate var="endDtTempParse" value="${wvSurvBVo.survEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="survEndDt" value="${endDtTempParse}" pattern="yyyy-MM-dd"/>
				<u:out value="${survEndDt}"/>
				
				</td>
				<c:if test="${wvSurvBVo.joinUserCnt != null}">
					<td class="body_ct">${wvSurvBVo.joinUserCnt}</td>
				</c:if>
				<c:if test="${wvSurvBVo.joinUserCnt == null}">
					<td class="body_ct">0</td>
				</c:if>
				
			<c:choose>
				<c:when test="${wvSurvBVo.survPrgStatCd == '1'}">
					<td class="body_ct"><u:msg titleId="wv.cols.ready" alt="준비중"/></td>
				</c:when>
				<c:when test="${wvSurvBVo.survPrgStatCd == '2'}">
					<td class="body_ct"><u:msg titleId="wv.cols.appr" alt="승인중" /></td>
				</c:when>
				<c:when test="${wvSurvBVo.survPrgStatCd == '3'}">
					<td class="body_ct"><u:msg titleId="wv.cols.ing" alt="진행중"/></td>
				</c:when>
				<c:when test="${wvSurvBVo.survPrgStatCd == '4'}">
					<td class="body_ct"><u:msg titleId="wv.cols.end" alt="마감"/></td>
				</c:when>
				<c:when test="${wvSurvBVo.survPrgStatCd == '5'}">
					<td class="body_ct"><u:msg titleId="wv.cols.notsave" alt="미저장"/></td>
				</c:when>
				<c:when test="${wvSurvBVo.survPrgStatCd == '6'}">
					<td class="body_ct"><u:msg titleId="wv.cols.tempSave" alt="임시저장"/></td>
				</c:when>
				<c:otherwise>
					<td class="body_ct"><u:msg titleId="wv.cols.rjt" alt="반려"/></td>
				</c:otherwise>
			</c:choose>
				<td class="body_ct">
					<c:choose>
						<c:when test="${RW == 'R'}">
							<u:msg titleId="wv.cols.replyImpossible" alt="응답불가"/>
						</c:when>
						<c:otherwise>
							${wvSurvBVo.replyYn}
						</c:otherwise>
					</c:choose>
					
				</td>
				</tr>
			</c:forEach>
			<c:if test="${fn:length(wvSurvBMapList) == 0}">
				<tr>
					<td class="nodata" colspan="7"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
				</tr>
			</c:if>
		
			
		</form>
	</table>
</div>
<u:blank />
<u:pagination />

