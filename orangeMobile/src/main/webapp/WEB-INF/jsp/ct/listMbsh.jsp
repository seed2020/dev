<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set test="${param.schCat != null}" var="schCat" value="${param.schCat}" elseValue="mbshNmOpt" />
<script type="text/javascript">
//<![CDATA[
function fnSetSchCd(cd)
{
	$('#schCat').val(cd);
	$('.listselect span').text($(".open1 dd[data-schCd="+cd+"]").text());
	$('.open1').hide();
}

function searchList(event){
	$m.nav.curr(event, './listMbsh.do?'+$('#searchForm').serialize());
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

<form id="searchForm" name="searchForm" action="./listMbsh.do" onsubmit="searchList(event);">
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
	
	<c:forEach var="ctMbshVo" items="${ctMbshList}" varStatus="status">
          <div class="listdiv" onclick="javascript:$m.user.viewUserPop('${ctMbshVo.userUid}');">
              <div class="list">
              <dl>
              <dd class="tit">
				${ctMbshVo.userNm}
              </dd>
              <dd class="name">
				  	<u:out value="${ctMbshVo.deptNm}" />ㅣ<u:out value="${bbBullLVo.regrNm}" />ㅣ<u:out value="${ctMbshVo.joinDt}" type="longdate" /> ㅣ 
					<c:choose>
						<c:when test="${ctMbshVo.userSeculCd == 'M'}">
							<u:msg titleId="ct.option.mbshLev0" alt="마스터"/>
						</c:when>
						<c:when test="${ctMbshVo.userSeculCd == 'S'}">
							<u:msg titleId="ct.option.mbshLev1" alt="스텝"/>
						</c:when>
						<c:when test="${ctMbshVo.userSeculCd == 'A'}">
							<u:msg titleId="ct.option.mbshLev3" alt="준회원"/>
						</c:when>
						<c:when test="${ctMbshVo.userSeculCd == 'R'}">
							<u:msg titleId="ct.option.mbshLev2" alt="정회원"/>
						</c:when>
					</c:choose>		  	
              </dd>
           </dl>
              </div>
          </div>
	</c:forEach>
	<c:if test="${fn:length(ctMbshList) == 0}">
             <div class="listdiv_nodata" >
             <dl>
             <dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd>
             </dl>
             </div>
	</c:if>
	
	</article>
    
</div>
<m:pagination />
    
    <jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
</section>
