<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<c:set var="colSpan" value="3"/><!-- line css 를 주기 위한 colspan -->
<c:set var="headList" value="cols.subj,cols.finDt,cols.ans"/><!-- 컬럼명(컬럼명을 보여줄경우) -->
<u:authUrl var="viewUrl" url="/wv/listSurv.do"  authCheckUrl="/wv/listSurv.do"/><!-- view page 호출관련 url 조합(menuId추가) -->

<script type="text/javascript">
<!--
function imsiMod(survId){
	
	var authUrl = '${viewUrl}';
	var menuId = authUrl.substring(authUrl.length - 8, authUrl.length);
	top.location.href = '../adm/setSurv.do?menuId='+menuId+'&fnc=mod&survId='+survId;
}
function setSurvPop(){
	
	var authUrl = '${viewUrl}';
	var menuId = authUrl.substring(authUrl.length - 8, authUrl.length);

	top.location.href = '../adm/setSurv.do?menuId='+menuId+'&fnc=reg';
}

function resultViewNo(){
	alert("<u:msg titleId="wv.msg.set.notPub" alt="결과 비공개 설문입니다." />");
	return;
}

function authHaveNot(){
	alert("<u:msg titleId="wv.msg.set.noRight" alt="투표 및 조회권한이 없습니다." />");
	return;
}

function viewSurv(survId){
	var authUrl = '${viewUrl}';
	var menuId = authUrl.substring(authUrl.length - 8, authUrl.length);

	top.location.href ='../viewSurv.do?menuId='+menuId+'&survId=' + survId;
}

function viewSurvRes(survId, repetYn, openYn){
	var authUrl = '${viewUrl}';
	var menuId = authUrl.substring(authUrl.length - 8, authUrl.length);

	top.location.href ='../viewSurvRes.do?menuId='+menuId+ '&survId=' + survId+'&repetYn='+repetYn + '&openYn=' + openYn;
}

<%
//자동숨김 - 1
String tdWidths = "60,20,10";
request.setAttribute("tdWidths", tdWidths);
request.setAttribute("tdWidthArr", tdWidths.split(","));
%>
$(document).ready(function() {
	resizeForPltTable('ptltable', 190, '${tdWidths}');
	<%// 유니폼 적용 %>
	//setUniformCSS();
});

<%
//자동숨김 - 3
//포틀릿 테이블 - 테이블ID %>
var gPltTableId = null;<%
//포틀릿 테이블 - 첫째 컬럼 최소 가로 길이(타입:number, 단위:px) %>
var gPltTableMinFirst = null;<%
//포틀릿 테이블 - 각 컬럼 비율 (예: "40,20,20,20") %>
var gPltTableRatio = null;<%
//포틀릿 테이블 - 가로폭에 따른 컬럼 숨기기, 보이기 %>
function resizeForPltTable(tableClass, minFirst, ratio){
	gPltTableId = tableClass;
	gPltTableMinFirst = minFirst;
	gPltTableRatio = [];
	ratio.split(',').each(function(index, va){
		gPltTableRatio.push(parseInt(va, 10));
	});
	onResizeForPltTable();
	$(window).resize(onResizeForPltTable);
}<%
//포틀릿 테이블 - 가로폭에 따른 컬럼 숨기기, 보이기 - 실제 함수 %>
function onResizeForPltTable(){
	if(gPltTableId==null) return;
	var $table = $("#"+gPltTableId), tableWidth = $table.width(), showIndex=0, ratioSum=0;
	while(true){
		ratioSum += gPltTableRatio[showIndex];
		if(parseInt((gPltTableRatio[0]/ratioSum) * tableWidth) < gPltTableMinFirst){
			break;
		}
		if(showIndex==gPltTableRatio.length-1) break;
		showIndex++;
	}
	var $colgroup = $table.find("colgroup");
	var i, colCnt = $colgroup.find("col").length, percent, percentSum=0;
	for(i=0;i<colCnt;i++){
		if(i==showIndex){
			percent = 100 - percentSum;
			$colgroup.find("col:eq("+i+")").attr("width", percent+"%");
		} else if(i<showIndex){
			percent = parseInt((gPltTableRatio[i]/ratioSum)*100 ,10);
			percentSum += percent;
			$colgroup.find("col:eq("+i+")").attr("width", percent+"%");
		} else {
			$colgroup.find("col:eq("+i+")").attr("width", "0%").hide();
		}
	}
	var $trs = $table.find("tbody tr[id!='lineTr']");
	for(i=0;i<colCnt;i++){
		if(i<=showIndex){
			$trs.find("td:eq("+i+")").show();
		} else {
			$trs.find("td:eq("+i+")").hide();
		}
	}
}
//-->
</script>
<% // 목록 %>
<table class="ptltable" id="ptltable" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed;"><%
	// 자동숨김 - 5 - colgroup %>
	<colgroup><c:forEach items="${tdWidthArr}" var="tdWidth">
		<col width="${tdWidth}%" /></c:forEach>
	</colgroup>
	
	<tbody>
		<tr>
		  <td colspan="${colSpan}" class="line"></td>
		</tr>
	<c:if test="${param.colYn eq 'Y'}">
		<tr id="headerTr">
			<c:forTokens	items="${headList}" var="colId" varStatus="colStatus" delims=",">
				<td class="head_ct" id="head_${fn:split(colId,'.')[1] }" style="display:none;"><div class="ellipsis" title="<u:msg titleId="${colId}" alt="이름" />"><u:msg titleId="${colId}" alt="이름" /></div></td>
			</c:forTokens>
		</tr>
		<tr>
		  <td colspan="${colSpan}" class="line"></td>
		</tr>
	</c:if>

	<c:choose>
		<c:when test="${!empty rsltMapList}">
			<c:forEach var="mapList" items="${rsltMapList}" varStatus="status">
				<c:set var="mapList" value="${mapList}" scope="request" />
				
				<!-- 
				authTgtTypCd : "D(dept) = 부서" , "U(user) = 사용자" 
			 	authGradCd   : "W(write)= 투표" , "R(read) = 조회" 	 
		-->
			<c:forEach var="wvSurvAuthList" items ="${mapList.survAuthList}" varStatus="authState">
				<c:set var="survW" value=""></c:set>
				<c:set var="survR" value=""></c:set>
				<!-- authTgtTypCd : "D(dept) = 부서" 일 때 -->
					<c:if test="${wvSurvAuthList.authTgtTypCd == 'D'}">
						<c:if test="${wvSurvAuthList.authTgtUid == logUserDeptId}">
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
			<c:if test="${mapList.regrUid == logUserUid || mapList.modrUid == logUserUid}">
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
			<c:if test="${fn:length(mapList.survAuthList) == 0 }">
					<c:set var="valueR" value="NR"></c:set>
					<c:set var="valueW" value="NW"></c:set>
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
			<c:if test="${mapList.survPrgStatCd == '3'}">
				<c:choose>
					<c:when test="${RW == 'R'}">
						<c:set var="viewFunction"	value= "viewSurvRes('${mapList.survId}', 'N', '${mapList.openYn}')" />
					</c:when>
					<c:when test="${RW == 'W'}">
						<c:choose>
							<c:when test="${mapList.replyYn == 'O'}">
								<c:choose>
									<c:when test="${mapList.repetSurvYn == 'Y'}">
										<c:set var="viewFunction"	value= "viewSurv('${mapList.survId}')" />
									</c:when>
									<c:when test="${mapList.repetSurvYn == 'N'}">
											<c:set var="viewFunction"	value= "resultViewNo()" />
									</c:when>
								</c:choose>
							</c:when>
							<c:when test="${mapList.replyYn == 'X'}">
								<c:set var="viewFunction"	value= "viewSurv('${mapList.survId}')" />
							</c:when>
						</c:choose>
					</c:when>
					<c:when test="${RW == 'RW'}">
						<c:choose>
							<c:when test="${mapList.replyYn == 'O'}">
								<c:choose>
									<c:when test="${mapList.repetSurvYn == 'Y'}">
										<c:set var="viewFunction"	value= "viewSurvRes('${mapList.survId}', 'Y', '${mapList.openYn}')" />
									</c:when>
									<c:when test="${mapList.repetSurvYn == 'N'}">
										<c:set var="viewFunction"	value= "viewSurvRes('${mapList.survId}', 'N', '${mapList.openYn}')" />
									</c:when>
								</c:choose>
							</c:when>
							<c:when test="${mapList.replyYn == 'X'}">
								<c:set var="viewFunction"	value= "viewSurv('${mapList.survId}')" />
							</c:when>
						</c:choose>
					</c:when>
					<c:when test="${RW == 'NRNW'}">
						<c:choose>
							<c:when test="${mapList.replyYn == 'O'}">
								<c:choose>
									<c:when test="${mapList.repetSurvYn == 'Y'}">
										<c:set var="viewFunction"	value= "viewSurvRes('${mapList.survId}', 'Y', '${mapList.openYn}')" />
									</c:when>
									<c:when test="${mapList.repetSurvYn == 'N'}">
										<c:choose>
											<c:when test="${mapList.openYn == 'Y'}">
												<c:set var="viewFunction"	value= "viewSurvRes('${mapList.survId}', 'N', '${mapList.openYn}')" />
											</c:when>
											<c:when test="${mapList.openYn == 'N'}">
												<c:set var="viewFunction"	value= "resultViewNo()" />
											</c:when>
										</c:choose>
									</c:when>
								</c:choose>
							</c:when>
							<c:when test="${mapList.replyYn == 'X'}">
								<c:set var="viewFunction"	value= "viewSurv('${mapList.survId}')" />
							</c:when>
						</c:choose>
					</c:when>
					<c:otherwise>
						<c:set var="viewFunction"	value= "authHaveNot()" />
					</c:otherwise>
				</c:choose> 
				
			</c:if>
			<c:if test="${mapList.survPrgStatCd == '4'}">
				<c:choose>
					<c:when test="${RW == 'R'}">
						<c:set var="viewFunction"	value= "viewSurvRes('${mapList.survId}', 'N','${mapList.openYn}')" />
					</c:when>
					<c:when test="${RW == 'W'}">
						<%-- <c:set var="viewFunction"	value= "resultViewNo()" /> --%>
						<c:set var="viewFunction"	value= "viewSurvRes('${mapList.survId}', 'N','${mapList.openYn}')" />
					</c:when>
					<c:when test="${RW == 'RW'}">
						<c:set var="viewFunction"	value= "viewSurvRes('${mapList.survId}', 'N' , '${mapList.openYn}')" />
					</c:when>
					<c:when test="${RW == 'NRNW'}">
						<c:choose>
							<c:when test="${mapList.openYn == 'Y'}">
								<c:set var="viewFunction"	value= "viewSurvRes('${mapList.survId}', 'N', '${mapList.openYn}')" />
							</c:when>
							<c:when test="${mapList.openYn == 'N'}">
								<c:set var="viewFunction"	value= "resultViewNo()" />
							</c:when>
						</c:choose>
					</c:when>
					<c:otherwise>
						<c:set var="viewFunction"	value= "authHaveNot()" />
					</c:otherwise>
				</c:choose>
			</c:if>
			<c:if test="${mapList.survPrgStatCd == '6'}">
				<c:if test="${mapList.regrUid == logUserUid || mapList.modrUid == logUserUid}">
					<c:set var="viewFunction"	value= "imsiMod('${mapList.survId}')" />
				</c:if>
			</c:if>
			<c:set var="valueW" value=""></c:set>
			<c:set var="valueR" value=""></c:set>
				<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
					<td class="body_lt" >
						<div class="ellipsis" title="<u:convertMap srcId="mapList" attId="survSubj" type="html" />"><a href="javascript:${viewFunction}"><u:convertMap srcId="mapList" attId="survSubj" type="html" /></a></div>
					</td>
					<td class="body_ct" >
						<div class="ellipsis" title="<u:convertMap srcId="mapList" attId="survEndDt" type="html" />"><fmt:parseDate var="endDtTempParse" value="${mapList.survEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/><fmt:formatDate var="survEndDt" value="${endDtTempParse}" pattern="yyyy-MM-dd"/><u:out value="${survEndDt}"/></div>
					</td>
					<td class="body_ct" >
						<div class="ellipsis" title="<u:convertMap srcId="mapList" attId="replyYn" type="html" />"><u:convertMap srcId="mapList" attId="replyYn" type="html" /></div>
					</td>
				</tr>
				<tr id="headerTr">
				  <td colspan="${colSpan}" class="line"></td>
				</tr>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<tr>
				<td class="nodata" colspan="${colSpan}"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
			<tr id="lineTr"><td colspan="${colSpan}" class="line"></td></tr>
		</c:otherwise>
	</c:choose>
	
	</tbody>
</table>
<u:blank />
<u:pagination noTotalCount="true" noBottomBlank="true" pltBlock="true"/>