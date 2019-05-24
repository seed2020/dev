<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set test="${param.schCat != null}" var="schCat" value="${param.schCat}" elseValue="subj" />
<u:set test="${param.schCtStat != null}" var="schCtStat" value="${param.schCtStat}" elseValue="" />
<script type="text/javascript">
//<![CDATA[

function authHaveNot(){
	$m.dialog.alert("<u:msg titleId="wv.msg.set.noRight" alt="투표 및 조회권한이 없습니다."/>");
	return;
}

function viewSurv(survId){
	$m.nav.next(event, '/ct/surv/viewSurv.do?menuId=${menuId}&survId=' + survId + '&ctId=${param.ctId}');
}

function viewSurvRes(survId, survPrgStatCd, repetYn){
	$m.nav.next(event, '/ct/surv/viewSurvRes.do?menuId=${menuId}&survId=' + survId+'&survStatCd='+survPrgStatCd + "&ctId=${param.ctId}&repetYn=" + repetYn);
}

function searchList(event){
	$m.nav.curr(event, '/ct/surv/listSurv.do?menuId=${menuId}&ctId=${param.ctId}&'+$('#searchForm').serialize());
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

function fnUnfold(obj){
	if($(obj).attr("class") == "unfoldbtn")
		$(obj).attr("class", "unfoldbtn_on");
	else
		$(obj).attr("class", "unfoldbtn");
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
	<input type="hidden" name="ctId" id="ctId" value="${param.ctId}" />
	
	<div class="listsearch">
		<div class="listselect schTxt1">
		    <div class="open1 schOpnLayer1" style="display:none;">
		        <div class="open_in1">
		        	<div class="open_div">
				        <dl>
				        <dd class="txt" onclick="javascript:fnSetSchCd('subj');" data-schCd="subj"><u:msg titleId="cols.subj" alt="제목" /></dd>
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
					        <dd class="txt" onclick="javascript:fnSetCtStat('');" data-schCd=""><u:msg titleId="cm.option.all" alt="전체선택" /></dd>
					        <dd class="line"></dd>
					        <%-- <dd class="txt" onclick="javascript:fnSetCtStat('1');" data-schCd="1"><u:msg titleId="wv.cols.ready" alt="준비중" /></dd>
					        <dd class="line"></dd> --%>
					        <dd class="txt" onclick="javascript:fnSetCtStat('3');" data-schCd="3"><u:msg titleId="wv.cols.ing" alt="진행중" /></dd>
					        <dd class="line"></dd>
					        <dd class="txt" onclick="javascript:fnSetCtStat('4');" data-schCd="4"><u:msg titleId="wv.cols.end" alt="마감" /></dd>					        
					        <%-- <dd class="txt" onclick="javascript:fnSetCtStat('6');" data-schCd="6"><u:msg titleId="wv.cols.tempSave" alt="임시저장" /></dd>
					        <dd class="line"></dd> --%>
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
				

				<u:set test="${!empty ctSurvBVo.survTgtM}" var="tgtM" value="마스터" elseValue=""></u:set>
				<u:set test="${!empty ctSurvBVo.survTgtS}" var="tgtS" value="스텝" elseValue=""></u:set>
				<u:set test="${!empty ctSurvBVo.survTgtR}" var="tgtR" value="정회원" elseValue=""></u:set>
				<u:set test="${!empty ctSurvBVo.survTgtA}" var="tgtA" value="준회원" elseValue=""></u:set>
			
	          <div class="listdiv" onclick="javascript:${viewFunction};">
	              <div class="list">
	              <dl>
	              <dd class="tit">${ctSurvBVo.subj}</dd>
	              <dd class="name">
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
					|
					<c:choose>
						<c:when test="${ctSurvBVo.survPrgStatCd == '1'}">
							<u:msg titleId="wv.cols.ready" alt="준비중"/>
						</c:when>
							<c:when test="${ctSurvBVo.survPrgStatCd == '3'}">
							<u:msg titleId="wv.cols.ing" alt="진행중"/>
						</c:when>
						<c:when test="${ctSurvBVo.survPrgStatCd == '4'}">
							<u:msg titleId="wv.cols.end" alt="마감"/>
						</c:when>
						<c:when test="${ctSurvBVo.survPrgStatCd == '6'}">
							<u:msg titleId="wv.cols.tempSave" alt="임시저장"/>
						</c:when>
					</c:choose>
					|
					<fmt:parseDate var="dateTempParse" value="${ctSurvBVo.survEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
					<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
					<c:if test="${ctSurvBVo.joinUserCnt != null}">
						| ${ctSurvBVo.joinUserCnt}
					</c:if>
						| 	${ctSurvBVo.replyYn}
						
	              </dd>
	           </dl>
	              </div>
	          </div>
			</c:forEach>
			<c:if test="${fn:length(ctSurvBMapList) == 0}">
	             <div class="listdiv_nodata" >
	             <dl>
	             <dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd>
	             </dl>
	             </div>
			</c:if>

		</article>
    
	</div>
	<jsp:include page="/WEB-INF/jsp/ct/inc/footerInc.jsp" flush="false" />
</section>
<!--//section E-->

