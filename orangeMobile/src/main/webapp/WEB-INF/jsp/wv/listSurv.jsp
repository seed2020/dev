<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set test="${param.schCat != null}" var="schCat" value="${param.schCat}" elseValue="survSubj" />
<u:set test="${param.schCtStat != null}" var="schCtStat" value="${param.schCtStat}" elseValue="3" />
<u:set test="${!empty param.schCtStat}" var="unfoldareaUsed" value="Y" elseValue="N" />
<script type="text/javascript">
//<![CDATA[
function resultViewNo(){
	$m.dialog.alert("<u:msg titleId="wv.msg.set.notPub" alt="결과 비공개 설문입니다."/>");
	return;
}

function authHaveNot(){
	$m.dialog.alert("<u:msg titleId="wv.msg.set.noRight" alt="투표 및 조회권한이 없습니다."/>");
	return;
}

function viewSurv(survId){
	$m.nav.next(event, '/wv/viewSurv.do?menuId=${menuId}&survId=' + survId);
}

function viewSurvRepet(survId){
	$m.dialog.confirm("<u:msg titleId="wv.msg.set.repetCfm" alt="재설문 하시겠습니까"/>", function(result){
		if(result){
			$m.nav.next(event, '/wv/viewSurv.do?menuId=${menuId}&survId=' + survId);
		}
	});
}

function viewSurvRes(survId, repetYn, openYn){
	$m.nav.next(event, '/wv/viewSurvRes.do?menuId=${menuId}&survId=' + survId+'&repetYn='+repetYn + '&openYn=' + openYn);
}

function searchList(event){
	$m.nav.curr(event, '/wv/listSurv.do?'+$('#searchForm').serialize());
}

function fnSetSchCd(cd)
{
	$('#schCat').val(cd);
	$('.schTxt1 span').text($(".schOpnLayer1 dd[data-schCd="+cd+"]").text());
	$('.schOpnLayer1').hide();
}

function fnSetCtStat(cd)
{
	$('#schCtStat').val(cd);
	$('.schTxt2 span').text($(".schOpnLayer2 dd[data-schCd='"+cd+"']").text());
	$('.schOpnLayer2').hide();
}

function fnUnfold(){
	if($('#unfold').attr("class") == "unfoldbtn")
		$('#unfold').attr("class", "unfoldbtn_on");
	else
		$('#unfold').attr("class", "unfoldbtn");
	$('.unfoldArea').toggle(); 
}

var holdHide = false, holdHide2 = false;
$(document).ready(function() {
	fnSetSchCd('${schCat}');
	fnSetCtStat('${schCtStat}');
	$('body:first').on('click', function(){
		if(holdHide) holdHide = false;
		else $(".schOpnLayer1").hide();
		if(holdHide2) holdHide2 = false;
		else $(".schOpnLayer2").hide();
	});
	
	if('${unfoldareaUsed}' == 'Y')
		fnUnfold();
	
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('list');
});
//]]>
</script>

<!--section S-->
<section>

	<!--listsearch S-->
	<form id="searchForm" name="searchForm" action="./listSurv.do" onsubmit="searchList(event);">
	<input type="hidden" name="menuId" value="${param.menuId}" />
	<input type="hidden" name="schCat" id="schCat" value="" />
	<input type="hidden" name="schCtStat" id="schCtStat" value="" />
	<div class="listsearch">
		<div class="listselect schTxt1">
		    <div class="open1 schOpnLayer1" style="display:none;">
		        <div class="open_in1">
		        	<div class="open_div">
				        <dl>
				        <dd class="txt" onclick="javascript:fnSetSchCd('survSubj');" data-schCd="survSubj"><u:msg titleId="cols.subj" alt="제목" /></dd>
				        <dd class="line"></dd>
				        <dd class="txt" onclick="javascript:fnSetSchCd('survItnt');" data-schCd="survItnt"><u:msg titleId="cols.itnt" alt="취지" /></dd>
				        <dd class="line"></dd>
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
			<dl>
				<dd class="input_left"></dd>
				<dd class="input_input"><input type="text" class="input_ip" name="schWord" maxlength="30" value="<u:out value="${param.schWord}" type="value" />" /></dd>
				<dd class="input_btn" onclick="searchList(event);"><div class="search"></div></dd>
			</dl>
			</div>
		</div>
		<div class="unfoldbtn" onclick="fnUnfold();" id="unfold"></div>
	</div>
	</form>
	<!--//listsearch E-->
	
	<div class="entryzone unfoldArea" style="display:none;">
      <div class="entryarea">
      <dl>
		<dd class="etr_blank"></dd>
		<dd class="etr_input">
              <div class="etr_ipmany">
              <dl>
              <dd class="etr_se_lt">
                  <div class="etr_open2 schOpnLayer2" style="display:none;">
                    <div class="open_in1">
                        <div class="open_div">
					        <dl>
					        <dd class="txt" onclick="javascript:fnSetCtStat('ALL');" data-schCd="ALL"><u:msg titleId="cm.option.all" alt="전체선택" /></dd>
					        <dd class="line"></dd>
					        <dd class="txt" onclick="javascript:fnSetCtStat('3');" data-schCd="3"><u:msg titleId="wv.cols.ing" alt="진행중" /></dd>
					        <dd class="line"></dd>
					        <dd class="txt" onclick="javascript:fnSetCtStat('4');" data-schCd="4"><u:msg titleId="wv.cols.end" alt="마감" /></dd>
					        <dd class="line"></dd>
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
            </dl>
            </div>
        </dd>
    	<dd class="etr_blank1"></dd>
 		</dl>
    	</div>
	</div>

	<div class="listarea">

		<article>
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
			<c:set var="valueW" value=""></c:set>
			<c:set var="valueR" value=""></c:set>
			
			
          <div class="listdiv" onclick="javascript:${viewFunction};">
              <div class="list">
              <dl>
              <dd class="tit">${wvSurvBVo.survSubj}</dd>
              <dd class="name">
				  	${wvSurvBVo.regrNm } |
				<fmt:parseDate var="dateTempParse" value="${wvSurvBVo.survStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="survStartDt" value="${dateTempParse}" pattern="yyyy-MM-dd"/>
				<u:out value="${survStartDt}"/> ~
				<fmt:parseDate var="endDtTempParse" value="${wvSurvBVo.survEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="survEndDt" value="${endDtTempParse}" pattern="yyyy-MM-dd"/>
				<u:out value="${survEndDt}"/>
              </dd>
           </dl>
              </div>
          </div>

		</c:forEach>
		<c:if test="${fn:length(wvSurvBMapList) == 0}">
             <div class="listdiv_nodata" >
             <dl>
             <dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd>
             </dl>
             </div>
		</c:if>
	
		</article>
    
	</div>
	<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
</section>
<!--//section E-->

