<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set test="${param.schCat != null}" var="schCat" value="${param.schCat}" elseValue="mbshNmOpt" />
<script type="text/javascript">
//<![CDATA[

function apvd(mbshCtId,mbshId){
	$m.msg.confirmMsg("ct.cfrm.mbshApvd", null, function(result){
		if(result){
			$m.ajax('/ct/mng/transMbshApvd.do?menuId=${menuId}&ctId=${param.ctId}', {mbshCtId:mbshCtId, mbshId:mbshId}, function(data){
				if (data.message != null) {
					$m.dialog.alert(data.message);
				}
				if (data.result == 'ok') {
					$m.nav.next(event, '/ct/mng/listMngJoinReqs.do?menuId=${menuId}&ctId='+mbshCtId);
				}
			});
		}
	});
}

function napvd(mbshCtId,mbshId){
	$m.msg.confirmMsg("ct.cfrm.mbshNapvd", null, function(result){
		if(result){
			$m.ajax('/ct/mng/transMbshNapvd.do?menuId=${menuId}&ctId=${param.ctId}', {mbshCtId:mbshCtId, mbshId:mbshId}, function(data){
				if (data.message != null) {
					$m.dialog.alert(data.message);
				}
				if (data.result == 'ok') {
					$m.nav.next(event, '/ct/mng/listMngJoinReqs.do?menuId=${menuId}&ctId='+mbshCtId);
				}
			});
		}
	});
}

function fnSetSchCd(cd)
{
	$('#schCat').val(cd);
	$('.listselect span').text($(".open1 dd[data-schCd="+cd+"]").text());
	$('.open1').hide();
}

function searchList(event){
	$m.nav.curr(event, './listMngJoinReqs.do?'+$('#searchForm').serialize());
}

var holdHide = false;
$(document).ready(function() {
	fnSetSchCd('${schCat}');
	$('body:first').on('click', function(){
		if(holdHide) holdHide = false;
		else $(".open1").hide();
	});
	
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('list');
});
//]]>
</script>

<form id="searchForm" name="searchForm" action="./listMngJoinReqs.do" onsubmit="searchList(event);">
<input type="hidden" id="menuId" name="menuId" value="${menuId}" />
<input type="hidden" id="ctId" name="ctId" value="${ctId}"/>
<input type="hidden" id="schCat" name="schCat" value="" />
<div class="listsearch">
		<div class="listselect">
		    <div class="open1" style="display:none;">
		        <div class="open_in1">
		        	<div class="open_div">
				        <dl>       
				        <dd class="txt" onclick="javascript:fnSetSchCd('mbshNmOpt');" data-schCd="mbshNmOpt"><u:msg titleId="ct.cols.userNm" alt="이름" /></dd>
				        <dd class="line"></dd>
				        <dd class="txt" onclick="javascript:fnSetSchCd('mbshDeptNmOpt');" data-schCd="mbshDeptNmOpt"><u:msg titleId="ct.cols.deptNm" alt="부서" /></dd>
				        <dd class="line"></dd>
				    	</dl>
				    </div>	
		        </div>
		    </div>
			<div class="select1">
			<div class="select_in1" onclick="holdHide = true;$('.open1').toggle();">
			<dl>
				<dd class="select_txt1"><span></span></dd>
				<dd class="select_btn"></dd>
			</dl>
			</div>
			</div>
		</div>
    	<div class="listinput">
			<div class="input1">
			<dl>
				<dd class="input_left"></dd>
				<dd class="input_input"><input type="text" class="input_ip" name="schWord" maxlength="30" value="<u:out value="${param.schWord}" type="value" />" /></dd>
				<dd class="input_btn" onclick="searchList(event);"><div class="search"></div></dd>
			</dl>
			</div>
		</div>
</div>
</form>


<section>
<div class="listarea">
	<article>

	<c:forEach items="${ctMngJoinReqsList}" var="ctJoinList" varStatus="status" >
		<c:if test="${ctJoinList.joinStat == '1'}" >
        <div class="listdiv">
              <div class="list">
              <dl>
              <dd class="tit" >
				<a href="javascript:$m.user.viewUserPop('${ctJoinList.userUid}');">${ctJoinList.userNm}</a>
              </dd>
              <dd class="name">
				${ctJoinList.deptNm} ${!empty ctJoinList.deptNm ? '/' : ''}
				<c:choose>
					<c:when test="${ctJoinList.userSeculCd == 'S'}"> 
						<c:set var="joinState"	value="스텝"/>
					</c:when>
					<c:when test="${ctJoinList.userSeculCd == 'R'}"> 
						<c:set var="joinState"	value="정회원"/>
					</c:when>
					<c:when test="${ctJoinList.userSeculCd == 'A'}"> 
						<c:set var="joinState"	value="준회원"/>
					</c:when>
				</c:choose>
				${joinState} /
				<fmt:parseDate var="dateTempParse" value="${ctJoinList.joinDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
			  </dd>
			  <dd><div class="list_btnarea">
                      <div class="size">
                      <dl>
                      <dd class="btn" onclick="apvd('${ctJoinList.ctId}','${ctJoinList.userUid}');"><u:msg titleId="cm.btn.apvd" alt="승인"/></dd>
                      <dd class="btn" onclick="napvd('${ctJoinList.ctId}','${ctJoinList.userUid}');"><u:msg titleId="cm.btn.napvd" alt="미승인"/></dd>
                 	  </dl>
                      </div>
                      </div>
               </dd>
                 </dl>
              </div>
          </div>
		</c:if>
	</c:forEach>
	<c:if test="${fn:length(ctMngJoinReqsList) == 0}">
             <div class="listdiv_nodata" >
             <dl>
             <dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd>
             </dl>
             </div>
	</c:if>


	</article>
    
</div>
<m:pagination />
    
    <jsp:include page="/WEB-INF/jsp/ct/inc/footerInc.jsp" flush="false" />
</section>
