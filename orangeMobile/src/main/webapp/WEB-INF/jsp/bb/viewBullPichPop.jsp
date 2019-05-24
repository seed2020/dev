<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[
$(document).ready(function() {

});
//]]>
</script>
<div class="s_tablearea">
	<div class="blank15"></div>


	<table class="s_table">
	<%-- <caption><u:msg titleId="or.jsp.setUserPop.ref" alt="참조정보"/></caption> --%>
	<colgroup>
		<col width="33%"/>
		<col width=""/>
	</colgroup>
	<tbody>
		<tr>
			<th class="shead_lt"><u:msg titleId="cols.pichNm" alt="담당자" /></th>
			<td class="sbody_lt"><a href="javascript:;" onclick="$m.user.viewUserPop('${baBrdBVo.pichUid}');">${baBrdBVo.pichVo.rescNm}</a></td>
		</tr>
		<tr>
			<th class="shead_lt"><u:msg titleId="cols.dept" alt="부서" /></th>
			<td class="shead_lt">${baBrdBVo.pichVo.deptRescNm}</td>
		</tr>
		<tr>
			<th class="shead_lt"><u:msg titleId="cols.phon" alt="전화번호" /></th>
			<td class="sbody_lt"><m:tel value="${baBrdBVo.pichPinfoVo.mbno}"/></td>
		</tr>
		<tr>
			<th class="shead_lt"><u:msg titleId="cols.email" alt="이메일" /></th>
			<td class="sbody_lt"><m:email value="${baBrdBVo.pichPinfoVo.email}"/></td>
		</tr>

	</tbody>
	</table>
	
</div>

		<div class="blank20"></div>
        <div class="btnarea">
            <div class="size">
            <dl>
            <dd class="btn" onclick="$m.dialog.close('viewBullPich')"><u:msg titleId="cm.btn.close" alt="닫기" /></dd>
         </dl>
            </div>
        </div>
