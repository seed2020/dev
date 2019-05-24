<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<c:forEach
	
		items="${wcUserGrpBVoList}" var="wcUserGrpBVo" varStatus="status">
	<div class="listdiv_fixed">
		<div class="listcheck_fixed">
		<dl>
		<m:check type="${selection=='single' ? 'radio' : 'checkbox'}" 
			id="grpChk_${wcUserGrpBVo.userGrpId}" name="grpChk" value='{"userGrpId":"${wcUserGrpBVo.userGrpId
				}","rescNm":"${wcUserGrpBVo.rescNm}"}' areaId="userGrpArea" />
		</dl>
		</div>
		
		<div class="list_fixed" >
		<dl>
		<dd class="tit">${wcUserGrpBVo.rescNm}</dd>
		</dl>
		</div>
	</div></c:forEach><c:if
		
		test="${fn:length(wcUserGrpBVoList) == 0}">
		<div class="listdiv_nodata">
		<dl><dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd></dl>
		</div>
	</c:if><c:if
	
		test="${not empty recodeCount and recodeCount != 0}">
	<m:pagination javascriptFunction="goAjaxPage"/>
	<div class="blank20"></div>
	</c:if>
