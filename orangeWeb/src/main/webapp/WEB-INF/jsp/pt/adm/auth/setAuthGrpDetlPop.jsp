<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<div style="width:400px">
<form id="setAuthGrpDetlCdPop">
<u:listArea>

<c:if test="${param.grpKndCd =='userGrp'}" >
	<tr id="headerTr">
		<th width="2%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('setAuthGrpDetlCdPop', this.checked);" value=""/></th>
		<th class="head_ct"><u:msg titleId="pt.jsp.setAuthGrp.userGrp" alt="사용자그룹" /></th>
		<th width="35%" class="head_ct"><u:msg titleId="pt.btn.cat" alt="카테고리" /></th>
	</tr>
	<c:if test="${fn:length(ptAuthGrpBVoList)==0}" >
	<tr>
		<td class="nodata" colspan="3"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
	</c:if>
	<c:forEach items="${ptAuthGrpBVoList}" var="ptAuthGrpBVo" varStatus="status">
	<tr>
		<td class="bodybg_ct"><input id="${ptAuthGrpBVo.authGrpId}" value="${ptAuthGrpBVo.authGrpId}" data-extra="${ptAuthGrpBVo.rescId},${ptAuthGrpBVo.rescNm}" type="checkbox"<u:elemTitle title="${ptAuthGrpBVo.rescNm}" type="checkbox" /> /></td>
		<td class="body_lt"><label for="${ptAuthGrpBVo.authGrpId}">${ptAuthGrpBVo.rescNm}</label></td>
		<td class="body_ct"><label for="${ptAuthGrpBVo.authGrpId}">${ptAuthGrpBVo.authGrpCatNm}</label></td>
	</tr>
	</c:forEach>
</c:if>
<c:if test="${param.grpKndCd !='userGrp'}" >
	<tr id="headerTr">
		<th width="2%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('setAuthGrpDetlCdPop', this.checked);" value=""/></th>
		<th class="head_ct">
			<c:if test="${param.grpKndCd =='posit'}"><u:term termId="or.term.posit" alt="직위" /></c:if>
			<c:if test="${param.grpKndCd =='grade'}"><u:term termId="or.term.grade" alt="직급" /></c:if>
			<c:if test="${param.grpKndCd =='title'}"><u:term termId="or.term.title" alt="직책" /></c:if>
			<c:if test="${param.grpKndCd =='role'}"><u:term termId="or.term.role" alt="역할" /></c:if>
			<c:if test="${param.grpKndCd =='secul'}"><u:term termId="or.term.secul" alt="보안등급" /></c:if>
			<c:if test="${param.grpKndCd =='aduStat'}"><u:msg titleId="pt.sysopt.aduStat" alt="겸직 상태" /></c:if>
		</th>
	</tr>
	<c:if test="${fn:length(grpKndCdList)==0}" >
	<tr>
		<td class="nodata" colspan="2"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
	</c:if>
	<c:forEach items="${grpKndCdList}" var="grpKndCd" varStatus="status">
	<tr>
		<u:checkbox name="grpId" value="${grpKndCd.cd}" title="${grpKndCd.rescNm}"
			inputClass="bodybg_ct" textClass="body_lt" noSpaceTd="true"
			extraData="${grpKndCd.rescId},${grpKndCd.rescNm}" />
	</tr>
	</c:forEach>
</c:if>

</u:listArea>
</form>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" href="javascript:setAuthGrpDetlSelected('${param.grpKndCd}');" alt="확인" auth="A" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</div>