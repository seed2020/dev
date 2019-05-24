<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[
//$(document).ready(function() {
//});
//]]>
</script>
<div class="s_tablearea">
	<div class="blank15"></div><c:if
				test="${not empty orUserImgDVo.imgPath}">
	<div id="userPhoto" onclick="$(this).hide()" style="background:url('${orUserImgDVo.imgPath}') no-repeat 50% 0; background-size:contain; position:absolute; left:6px; right:6px; top:33px; height:500px; display:none; z-index:2"></div></c:if>
	<table class="s_table">
	<caption><u:msg titleId="or.jsp.setUserPop.basic" alt="기본정보"/></caption>
	<colgroup>
		<col width="33%"/>
		<col width=""/>
	</colgroup>
	<tbody>
		<tr><c:if
				test="${empty orUserImgDVo.imgPath}">
			<td rowspan="5" class="noimg"></td></c:if><c:if
				test="${not empty orUserImgDVo.imgPath}">
			<td rowspan="5" onclick="$('#userPhoto').show()" style="background:url('${orUserImgDVo.imgPath}') no-repeat 50% 0; background-size:contain;"></td></c:if>
			<td class="sbody_lt"><u:out value="${orUserBVo.rescNm}" nullValue="&nbsp;" /></td>
		</tr>
		<tr>
			<td class="sbody_lt"><u:out value="${orUserBVo.positNm}" nullValue="&nbsp;" /></td>
		</tr>
		<tr>
			<td class="sbody_lt"><u:out value="${orUserBVo.deptRescNm}" /></td>
		</tr>
		<tr>
			<td class="sbody_lt" style="position:relative;"><div style="position:absolute; right:3px; width:auto;"><m:sms
				value="${orUserPinfoDVo.mbno}" text="(SMS)"
				/></div><m:tel value="${orUserPinfoDVo.mbno}"
				/></td>
		</tr>
		<tr>
			<td class="sbody_lt"><m:email value="${orUserPinfoDVo.email}" /></td>
		</tr>
	</tbody>
	</table>
	
	<table class="s_table">
	<caption <c:if test="${sessionScope.userVo.userUid eq 'U0000001'}">onclick="$m.menu.switchUser(event, '${orUserBVo.userUid}');"</c:if>><u:msg titleId="or.jsp.setUserPop.ref" alt="참조정보"/></caption>
	<colgroup>
		<col width="33%"/>
		<col width=""/>
	</colgroup>
	<tbody><c:if
			test="${not empty showCompNmEnable}">
		<tr>
			<th class="shead_lt"><u:msg titleId="cols.comp" alt="회사"/></th>
			<td class="sbody_lt"><u:out value="${compNm}" /></td>
		</tr></c:if>
		<tr>
			<th class="shead_lt"><u:msg titleId="cols.compPhon" alt="회사전화번호"/></th>
			<td class="sbody_lt"><m:tel value="${orUserPinfoDVo.compPhon}" /></td>
		</tr>
		<tr>
			<th class="shead_lt"><u:msg titleId="cols.compFno" alt="회사팩스번호"/></th>
			<td class="sbody_lt"><u:out value="${orUserPinfoDVo.compFno}" nullValue="&nbsp;" /></td>
		</tr>
		<tr>
			<th class="shead_lt"><u:msg titleId="cols.compAdr" alt="회사주소"/></th>
			<td class="sbody_lt"><m:addr zipNo="${orUserPinfoDVo.compZipNo }" addr="${orUserPinfoDVo.compAdr}" /></td>
		</tr><c:if
		
			test="${empty showHomeInfoDisable}">
		<tr>
			<th class="shead_lt"><u:msg titleId="cols.homePhon" alt="자택전화번호"/></th>
			<td class="sbody_lt"><m:tel value="${orUserPinfoDVo.homePhon}" /></td>
		</tr>
		<tr>
			<th class="shead_lt"><u:msg titleId="cols.homeFno" alt="자택팩스번호"/></th>
			<td class="sbody_lt"><u:out value="${orUserPinfoDVo.homeFno}" nullValue="&nbsp;" /></td>
		</tr>
		<tr>
			<th class="shead_lt"><u:msg titleId="cols.homeAdr" alt="자택주소"/></th>
			<td class="sbody_lt"><m:addr zipNo="${orUserPinfoDVo.homeZipNo}" addr="${orUserPinfoDVo.homeAdr}" /></td>
		</tr></c:if><c:if
		
			test="${empty blockEinEnable}">
		<tr>
			<th class="shead_lt"><u:msg titleId="cols.ein" alt="사원번호"/></th>
			<td class="sbody_lt"><u:out value="${orOdurBVo.ein}" nullValue="&nbsp;" /></td>
		</tr></c:if>
		<tr>
			<th class="shead_lt"><u:term termId="or.term.title" alt="직책"/></th>
			<td class="sbody_lt"><u:out value="${orUserBVo.titleNm}" nullValue="&nbsp;" /></td>
		</tr>
		<tr>
			<th class="shead_lt"><u:msg titleId="or.cols.statCd" alt="상태코드" /></th>
			<td class="sbody_lt"><u:out value="${orOdurBVo.userStatNm}" nullValue="&nbsp;" /></td>
		</tr><c:if
			
			test="${not empty orUserPinfoDVo.hpageUrl}">
		<tr>
			<th class="shead_lt"><u:msg titleId="cols.hpageUrl" alt="홈페이지URL"/></th>
			<td class="sbody_lt"><a href="<u:out value="${orUserPinfoDVo.hpageUrl}" type="value" />" target="_blank"><u:out value="${orUserPinfoDVo.hpageUrl}" /></a></td>
		</tr></c:if><c:if
			
			test="${not empty orUserPinfoDVo.extnEmail}">
		<tr>
			<th class="shead_lt"><u:msg titleId="cols.extnEmail" alt="외부이메일"/></th>
			<td class="sbody_lt"><m:email value="${orUserPinfoDVo.extnEmail}" /></td>
		</tr></c:if><c:if
			
			test="${not empty orUserBVo.tichCont}">
		<tr>
			<th class="shead_lt"><u:msg titleId="or.cols.tich" alt="담당업무"/></th>
			<td class="sbody_lt"><u:out value="${orUserBVo.tichCont}" nullValue="&nbsp;" /></td>
		</tr></c:if>
	</tbody>
	</table>
	
</div>