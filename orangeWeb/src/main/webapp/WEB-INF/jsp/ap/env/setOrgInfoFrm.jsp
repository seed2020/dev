<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// 조직 선택[문서과, 대리문서과] - 하위부서 열기 %>
function openSubOrg(orgId, prefix){
	var data = {orgId:$("#"+prefix+"OrgId").val()};
	if(prefix == 'agnCrd') orgId = $("#crdOrgId").val();
	parent.searchOrgPop({data:data, downward:orgId }, function(orgVo){<%// downward - 하위부서 시작 조직ID %>
		if(orgVo!=null){
			if(prefix=='crd' && $("#"+prefix+"OrgId").val() != orgVo.orgId){<%//문서과 선택일때, 문서과가 예전의 문서과와 다르면 대리문서과 삭제 %>
				delSelectedOrg('agnCrd');
			}
			$("#"+prefix+"RescNm").val(orgVo.rescNm);
			$("#"+prefix+"OrgId").val(orgVo.orgId);
			$("#"+prefix+"RescId").val(orgVo.rescId);
		}
	});
}<%
// 조직 선택 삭제[문서과, 대리문서과] %>
function delSelectedOrg(prefix){
	$("#"+prefix+"RescNm").val("");
	$("#"+prefix+"OrgId").val("");
	$("#"+prefix+"RescId").val("");
	if(prefix=='crd'){<%// 문서과 정보를 삭제 하면 대리문서과 정보 삭제%>
		delSelectedOrg('agnCrd');
	}
}<%
// 부서이력 - 부서 선택 %>
function openOrgHisPop(){
	var data = [];
	var ids = $("#orgHisOrgArea input[name=orgHisOrgIds]").val();
	if(ids!=''){
		ids.split(',').each(function(index, va){
			data.push({orgId:va});
		});
	}
	<%// option : data, multi, withSub, titleId %>
	parent.searchOrgPop({data:data, multi:true, withDel:true, mode:'search'}, function(arr){
		var orgIds='', rescNms='', rescIds='';
		if(arr!=null){
			var orgIdArr=[], rescNmArr=[], rescIdArr=[];
			arr.each(function(index, orgVo){
				orgIdArr.push(orgVo.orgId);
				rescNmArr.push(orgVo.rescNm);
				rescIdArr.push(orgVo.rescId);
			});
			orgIds = orgIdArr.join(',');
			rescNms = rescNmArr.join(', ');
			rescIds = rescIdArr.join(',');
		}
		var $area = $("#orgHisOrgArea");
		$area.find('input[name=orgHisOrgIds]').val(orgIds);
		$area.find('#orgHisOrgNmArea').text(rescNms);
		$area.find('input[name=orgHisRescIds]').val(rescIds);
	});
}<%
// 부서정보[탭] - 저장%>
function saveOrgInfo(){
	if(validator.validate('deptInfoForm')){
		var $form = $('#deptInfoForm');
		$form.attr('action','./transOrgInfo.do');
		$form.attr('target','dataframeForFrame');
		$form.submit();
	}
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<% // 부서정보 %>
<form id="deptInfoForm" method="post" style="padding:10px;">
<input type="hidden" name="menuId" value="${menuId}" /><c:if test="${not empty param.orgId}">
<input type="hidden" name="orgId" value="${param.orgId}" /></c:if>

	<u:listArea colgroup="18%,32%,18%,32%">
		<tr>
		<td class="head_lt"><c:if
			test="${not empty adminPage}"><u:msg titleId="or.cols.deptNm" alt="부서명"/> / <u:msg titleId="or.cols.deptId" alt="부서 ID"/></c:if><c:if
			test="${empty adminPage}"><u:msg titleId="or.cols.deptNm" alt="부서명"/> / <u:msg titleId="or.cols.deptId" alt="부서 ID"/> / <u:msg titleId="cols.orgTyp" alt="조직구분" /></c:if></td>
		<td class="body_lt"><c:if
			test="${not empty adminPage}">${orOrgBVo.rescNm} / ${orOrgBVo.orgId}</c:if><c:if
			test="${empty adminPage}">${orOrgBVo.rescNm} / ${orOrgBVo.orgId} / ${orOrgBVo.orgTypNm}</c:if></td>
		<td class="head_lt"><u:msg titleId="cols.hodpPosit" alt="부서장직위"/></td>
		<td id="hodpPositRescArea"><table cellspacing="0" cellpadding="0" border="0">
			<tr>
			<td id="langTypArea">
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:convert srcId="${orOrgApvDVo.hodpPositRescId}_${langTypCdVo.cd}" var="rescVa" />
			<u:set test="${status.first}" var="style" value="" elseValue="display:none" />
			<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
			<u:input id="hodpPositRescNm_${langTypCdVo.cd}" name="hodpPositRescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.hodpPosit" value="${rescVa}" style="${style}"
				maxByte="200" validator="changeLangSelector('hodpPositRescArea', id, va)"
				mandatory="Y" mandatorySkipper="skipMandatory('hodpPositRescArea', 'hodpPositRescNm_')" />
			</c:forEach>
			</td>
			<td>
			<c:if test="${fn:length(_langTypCdListByCompId)>1}">
				<select id="langSelector" onchange="changeLangTypCd('hodpPositRescArea','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
				<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
				<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
				</c:forEach>
				</select>
			</c:if>
			<u:input type="hidden" id="hodpPositRescId" value="${orOrgApvDVo.hodpPositRescId}" />
			</td>
			</tr>
			</table></td>
		</tr>

		<tr>
		<td class="head_lt"><u:msg titleId="cols.orgAbbr" alt="조직약어"/></td>
		<td id="orgAbbrRescArea"><table cellspacing="0" cellpadding="0" border="0">
			<tr>
			<td id="langTypArea">
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:convert srcId="${orOrgBVo.orgAbbrRescId}_${langTypCdVo.cd}" var="rescVa" />
			<u:set test="${status.first}" var="style" value="" elseValue="display:none" />
			<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
			<u:input id="orgAbbrRescNm_${langTypCdVo.cd}" name="orgAbbrRescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.orgAbbr" value="${rescVa}" style="${style}"
				maxByte="200" validator="changeLangSelector('orgAbbrRescArea', id, va)"
				mandatory="Y" mandatorySkipper="skipMandatory('orgAbbrRescArea', 'orgAbbrRescNm_')" />
			</c:forEach>
			</td>
			<td>
			<c:if test="${fn:length(_langTypCdListByCompId)>1}">
				<select id="langSelector" onchange="changeLangTypCd('orgAbbrRescArea','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
				<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
				<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
				</c:forEach>
				</select>
			</c:if>
			<u:input type="hidden" id="orgAbbrRescId" value="${orOrgBVo.orgAbbrRescId}" />
			</td>
			</tr>
			</table></td>
		<td class="head_lt"><u:msg titleId="cols.sendrNm" alt="발신명의"/></td>
		<td id="sendrNmArea"><table cellspacing="0" cellpadding="0" border="0">
			<tr>
			<td id="langTypArea">
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:convert srcId="${orOrgApvDVo.sendrNmRescId}_${langTypCdVo.cd}" var="rescVa" />
			<u:set test="${status.first}" var="style" value="" elseValue="display:none" />
			<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
			<u:input id="sendrNmRescNm_${langTypCdVo.cd}" name="sendrNmRescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.sendrNm" value="${rescVa}" style="${style}"
				maxByte="200" validator="changeLangSelector('sendrNmArea', id, va)"
				mandatory="Y" mandatorySkipper="skipMandatory('sendrNmArea', 'sendrNmRescNm_')" />
			</c:forEach>
			</td>
			<td>
			<c:if test="${fn:length(_langTypCdListByCompId)>1}">
				<select id="langSelector" onchange="changeLangTypCd('sendrNmArea','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
				<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
				<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
				</c:forEach>
				</select>
			</c:if>
			<u:input type="hidden" id="sendrNmRescId" value="${orOrgApvDVo.sendrNmRescId}" />
			</td>
			</tr>
			</table></td>
		</tr>

		<c:if test="${not empty adminPage and (orOrgBVo.orgTypCd =='C' or orOrgBVo.orgTypCd =='G')}">
		<tr>
		<td class="head_lt"><u:msg titleId="cols.crd" alt="문서과"/></td>
		<td><table cellspacing="0" cellpadding="0" border="0">
			<tr><td><u:input id="crdRescNm" value="${orOrgApvDVo.crdRescNm}" titleId="cols.crd" readonly="Y" />
					<u:input type="hidden" name="crdOrgId" value="${orOrgApvDVo.crdOrgId}" />
					<u:input type="hidden" name="crdRescId" value="${orOrgApvDVo.crdRescId}" /></td>
				<td><u:buttonS titleId="cm.btn.sel" popYn="Y" alt="선택" onclick="openSubOrg('${orOrgBVo.orgId}', 'crd');"
					/><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="delSelectedOrg('crd');" /></td>
			</tr></table></td>
		<td class="head_lt"><u:msg titleId="cols.agnCrd" alt="대리문서과"/></td>
		<td><table cellspacing="0" cellpadding="0" border="0">
			<tr><td><u:input id="agnCrdRescNm" value="${orOrgApvDVo.agnCrdRescNm}" titleId="cols.agnCrd" readonly="Y" />
					<u:input type="hidden" name="agnCrdOrgId" value="${orOrgApvDVo.agnCrdOrgId}" />
					<u:input type="hidden" name="agnCrdRescId" value="${orOrgApvDVo.agnCrdRescId}" /></td>
				<td><u:buttonS titleId="cm.btn.sel" popYn="Y" alt="선택" onclick="openSubOrg('${orOrgBVo.orgId}', 'agnCrd');"
					/><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="delSelectedOrg('agnCrd');" /></td>
			</tr></table></td>
		</tr>
		</c:if>

		<tr>
		<td class="head_lt"><u:msg titleId="cols.phon" alt="전화번호"/></td>
		<td><u:phone id="phon" value="${orOrgCntcDVo.phon}" titleId="cols.phon" /></td>
		<td class="head_lt"><u:msg titleId="cols.email" alt="이메일"/></td>
		<td><u:input id="repEmail" value="${orOrgCntcDVo.repEmail}" titleId="cols.email" valueOption="email" maxByte="200" style="width:94%"
	validator="checkMail(inputTitle, va)" /></td>
		</tr>

		<tr>
		<td class="head_lt"><u:msg titleId="cols.fno" alt="팩스번호"/></td>
		<td><u:phone id="fno" value="${orOrgCntcDVo.fno}" titleId="cols.fno" /></td>
		<td class="head_lt"><u:msg titleId="cols.hpageUrl" alt="홈페이지URL"/></td>
		<td><u:input id="repHpageUrl" value="${orOrgCntcDVo.repHpageUrl}" titleId="cols.hpageUrl" maxByte="200" style="width:94%"
	valueOption="alpha,number" valueAllowed=".:/?&-_" /></td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.adr" alt="주소"/></td>
		<td colspan="3"><u:address id="" alt="주소" adrStyle="width:94%" zipNoValue="${orOrgCntcDVo.zipNo }" adrValue="${orOrgCntcDVo.adr }" readonly="Y" frameId="deptInfoFrame"/></td>
		</tr><c:if
			test="${not empty orgSyncEnable}">
		
		<tr>
		<td class="head_lt"><u:msg titleId="or.txt.noSync" alt="동기화 제외 대상"/></td>
		<td colspan="3" class="bodybg_lt"><u:checkArea><u:checkbox value="Y" name="sysOrgYn" checked="${orOrgBVo.sysOrgYn == 'Y'}"
			titleId="pt.jsp.setAuthGrp.excTgt" alt="제외대상" /></u:checkArea></td>
		</tr></c:if><c:if
			test="${empty orgSyncEnable}"><input type="hidden" name="sysOrgYn" value="${orOrgBVo.sysOrgYn}" /></c:if><c:if
			
			test="${not empty adminPage}">

		<tr>
		<td class="head_lt"><u:msg titleId="or.txt.orgHis" alt="부서 이력"/></td>
		<td colspan="3"><table cellspacing="0" cellpadding="0" border="0" style="width:100%" id="orgHisOrgArea">
			<tr><td id="orgHisOrgNmArea" style="padding-left:3px">${orgHisOrgNms}</td>
				<td width="40px"><u:buttonS titleId="cm.btn.sel" popYn="Y" alt="선택" onclick="openOrgHisPop();"
					/><input type="hidden" name="orgHisOrgIds" value="${orgHisOrgIds}" /><input type="hidden" name="orgHisRescIds" value="${orgHisRescIds}" /></td>
			</tr></table></td>
		</tr></c:if>

	</u:listArea>

</form>
