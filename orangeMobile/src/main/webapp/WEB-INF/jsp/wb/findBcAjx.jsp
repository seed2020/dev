<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<c:forEach
	
		items="${wbBcBMapList}" var="wbBcBMap" varStatus="status">
	<div class="listdiv_fixed">
		<div class="listcheck_fixed">
		<dl>
		<m:check type="${selection=='single' ? 'radio' : 'checkbox'}" 
			id="${wbBcBMap.bcId}" name="bcCheck" value='{"bcId":"${wbBcBMap.bcId
				}","bcNm":"${wbBcBMap.bcNm
				}","compNm":"${wbBcBMap.compNm
				}","compPhon":"${wbBcBMap.compPhon
				}","mbno":"${wbBcBMap.mbno
				}","email":"${wbBcBMap.email
				}"}' areaId="${!empty param.detlViewType && param.detlViewType eq 'bcOpenList' ? 'bcOpenListArea' : 'bcListArea' }" />
		</dl>
		</div>
		
		<div class="list_fixed" >
		<dl>
		<dd class="tit">${wbBcBMap.bcNm}<c:if test="${!empty wbBcBMap.dftCntc}">(${wbBcBMap.dftCntc})</c:if></dd>
		<dd class="body">${wbBcBMap.compNm} 
             <c:if test="${!empty wbBcBMap.deptNm}">/ ${wbBcBMap.deptNm}</c:if>
             <c:if test="${!empty wbBcBMap.gradeNm}">/ ${wbBcBMap.gradeNm}</c:if></dd>
		</dl>
		</div>
	</div></c:forEach><c:if
		
		test="${fn:length(wbBcBMapList) == 0}">
		<div class="listdiv_nodata">
		<dl><dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd></dl>
		</div>
	</c:if><c:if
	
		test="${not empty recodeCount and recodeCount != 0}">
	<m:pagination javascriptFunction="goAjaxPage"/>
	<div class="blank20"></div>
	</c:if>
