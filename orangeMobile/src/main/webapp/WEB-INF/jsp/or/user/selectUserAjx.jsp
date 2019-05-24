<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<c:forEach
	
		items="${orUserMapList}" var="orUserMap" varStatus="status">
	<div class="listdiv_fixed">
		<div class="listcheck_fixed">
		<dl>
		<m:check type="${selection=='single' ? 'radio' : 'checkbox'}" 
			id="${orUserMap.userUid}" name="user" value='{"userUid":"${orUserMap.userUid
				}","userUid":"${orUserMap.userUid
				}","odurUid":"${orUserMap.odurUid
				}","rescNm":"${orUserMap.rescNm
				}","compId":"${orUserMap.compId
				}","orgId":"${orUserMap.orgId
				}","orgRescNm":"${orUserMap.orgRescNm
				}","deptId":"${orUserMap.deptId
				}","deptRescNm":"${orUserMap.deptRescNm
				}","positCd":"${orUserMap.positCd
				}","positNm":"${orUserMap.positNm
				}","titleCd":"${orUserMap.titleCd
				}","titleNm":"${orUserMap.titleNm
				}","userStatCd":"${orUserMap.userStatCd
				}","userStatNm":"${orUserMap.userStatNm
				}","mbno":"${orUserMap.mbno
				}","compPhon":"${orUserMap.compPhon
				}","email":"${orUserMap.email
				}"}' areaId="userListArea" />
		</dl>
		</div>
		
		<div class="list_fixed" onclick="$m.user.viewUserPop('${orUserMap.userUid}')">
		<dl>
		<dd class="tit"><u:out value="${orUserMap.rescNm}" /> / <u:out value="${orUserMap.deptRescNm}" 
			/><c:if test="${not empty orUserMap.positNm}"> / <u:out value="${orUserMap.positNm}" /></c:if></dd>
		<dd class="body"><c:if
			test="${empty orUserMap.tichCont}"><u:msg titleId="mor.lable.NoTichCont" alt="담당 업무 미지정" /></c:if><c:if
			test="${not empty orUserMap.tichCont}"><u:out value="${orUserMap.tichCont}" /></c:if></dd>
		</dl>
		</div>
	</div></c:forEach><c:if
		
		test="${fn:length(orUserMapList) == 0}">
		<div class="listdiv_nodata">
		<dl><dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd></dl>
		</div>
	</c:if><c:if
	
		test="${not empty recodeCount and recodeCount != 0}">
	<m:pagination javascriptFunction="goAjaxPage"/>
	<div class="blank20"></div>
	</c:if>
