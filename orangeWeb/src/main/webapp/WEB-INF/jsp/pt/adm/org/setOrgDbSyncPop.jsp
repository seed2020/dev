<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%><script type="text/javascript">
<!--<%
// 저장 %>
function saveDbSync(){
	changeTab('orgDbSync',0);
	if(!validator.validate('orgDbSyncBasicArea')) return;
	
	var $form = $('#orgDbSyncForm');
	$form.attr("action","./transOrgDbSync.do?menuId=${menuId}");
	$form.attr("target","dataframe");
	$form.attr("method","post");
	$form.submit();
}<%
// 연결 테스트 %>
function testConn(){
	changeTab('orgDbSync',0);
	if(!validator.validate('connInfoArea')) return;
	var param = new ParamMap().getData('connInfoArea');
	callAjax("./testConnAjx.do?menuId=${menuId}", param.map, function(data){
		if(data.message != null) alert(data.message);
	});
}<%
// 지금 실행 %>
function runNow(){<%
	// or.cfm.syscRunAsSave=저장된 데이터 기반으로 동기화가 실행 됩니다.\\n동기화를 진행 하시겠습니까 ? %>
	if(!confirmMsg('or.cfm.syscRunAsSave')) return;
	var compId = $('#orgDbSyncBasicArea #compId').val();
	var param = compId==null ? {} : {compId:compId};
	callAjax("./runNow.do?menuId=${menuId}", param, function(data){
		if(data.message != null) alert(data.message);
	}, null, null, true);
}<%
// 조직도 열기 - 삭제된 조직을 이동할 조직ID / 삭제된 사용자를 이동할 조직ID / 부서 없음 조직ID %>
function selectOrgPop(which){
	var data = {orgId:$("#orgDbSyncConfigArea #"+which+"Id").val()};
	parent.searchOrgPop({data:data, withDel:true}, function(orgVo){
		if(orgVo!=null){
			var $area = $("#orgDbSyncConfigArea");
			$area.find("#"+which+"Id").val(orgVo.orgId);
			$area.find("#"+which+"Nm").val(orgVo.rescNm);
		}
	});
}
<%
// 조직도 삭제 - 삭제된 조직을 이동할 조직ID / 삭제된 사용자를 이동할 조직ID / 부서 없음 조직ID %>
function delSelectedOrg(which){
	$("#orgDbSyncConfigArea").find("#"+which+"Id, #"+which+"Nm").val('');
}
//-->
</script>
<div style="width:700px">

<u:tabGroup id="orgDbSync" noBottomBlank="true">
	<u:tab alt="기본 정보"   areaId="orgDbSyncBasicArea" id="orgDbSync" titleId="or.cols.basicInfo" on="${true}" /><c:if test="${sessionScope.userVo.userUid eq 'U0000001'}">
	<u:tab alt="코드 SQL"  areaId="cdSqlArea" id="orgDbSync" titleId="or.cols.cdSql" />
	<u:tab alt="조직도 SQL" areaId="orgSqlArea" id="orgDbSync" titleId="or.cols.orgSql" />
	<u:tab alt="사용자 SQL" areaId="userSqlArea" id="orgDbSync" titleId="or.cols.userSql" /></c:if>
</u:tabGroup>
<form id="orgDbSyncForm">
<u:tabArea outerStyle="min-height:390px; overflow-x:hidden; overflow-y:auto; padding:5px 7px 0 7px;" innerStyle="NO_INNER_IDV" >
<div id="orgDbSyncBasicArea">

<c:if
	test="${empty ptCompBVoList or not sessionScope.userVo.userUid eq 'U0000001'}"><u:title titleId="or.txt.conInfo" alt="연결정보" type="small" /></c:if><c:if
	test="${not empty ptCompBVoList and sessionScope.userVo.userUid eq 'U0000001'}">
<div class="front">
	<div class="front_left">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td style="padding:3px 4px 0 0px"><u:title titleId="or.txt.conInfo" alt="연결정보" type="small" /></td>
			<td class="width5"></td>
			<td class="frontinput notPrint">
				<select id="compId" name="compId" <u:elemTitle titleId="cols.comp" /> onchange="popOrgDbSync(this.value);">
				<c:forEach items="${ptCompBVoList}" var="ptCompBVo" varStatus="status">
					<u:option value="${ptCompBVo.compId}" title="${ptCompBVo.rescNm}" selected="${param.compId eq ptCompBVo.compId or (empty param.compId and sessionScope.userVo.compId eq ptCompBVo.compId)}"/>
				</c:forEach>
				</select>
			</td>
	 		</tr>
		</table>
	</div>
</div>
</c:if>

<u:listArea colgroup="30%,70%" id="connInfoArea">
	<tr>
	<td class="head_lt"><u:msg titleId="or.cols.acnt" alt="계정" /></td>
	<td><u:input id="dbUserId" value="${orDbIntgDVo.dbUserId}" titleId="or.cols.acnt" maxByte="30" mandatory="Y" style="width:180px" /></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="or.cols.pw" alt="비밀번호" /></td>
	<td><u:input id="dbPw" value="${orDbIntgDVo.dbPw}" titleId="or.cols.pw" maxByte="30" mandatory="Y" type="password" style="width:180px" /></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="or.cols.drv" alt="드라이버" /></td>
	<td><u:input id="dbDriver" value="${orDbIntgDVo.dbDriver}" titleId="or.cols.drv" maxByte="60" mandatory="Y" style="width:96%" /></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="or.cols.conUrl" alt="연결URL" /></td>
	<td><u:input id="dbUrl" value="${orDbIntgDVo.dbUrl}" titleId="or.cols.conUrl" maxByte="60" mandatory="Y" style="width:96%" /></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="or.cols.testSql" alt="테스트 SQL" /></td>
	<td><u:input id="dbTestSql" value="${orDbIntgDVo.dbTestSql}" titleId="or.cols.testSql" maxByte="60" mandatory="Y" style="width:96%" /></td>
	</tr>
</u:listArea>
<u:title titleId="or.txt.optInfo" alt="설정정보" type="small" />
<u:listArea noBottomBlank="${true}" colgroup="30%,70%" id="orgDbSyncConfigArea">
	<tr>
	<td class="head_lt"><u:msg titleId="or.cols.delCd" alt="삭제된 코드" /></td>
	<td class="bodybg_lt">
	<u:checkArea><u:radio
		value="del" name="cdDelCd" titleId="or.syncCd.del" alt="삭제" checked="${orDbIntgDVo.cdDelCd == 'del'}" /><u:radio
		value="stat" name="cdDelCd" titleId="or.syncCd.stat" alt="삭제 상태" checked="${orDbIntgDVo.cdDelCd != 'del'}" /></u:checkArea></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="or.cols.delOrg" alt="삭제된 조직" /></td>
	<td class="bodybg_lt">
	<u:checkArea><u:radio
		value="del" name="orgDelCd" titleId="or.syncCd.del" alt="삭제" checked="${orDbIntgDVo.orgDelCd == 'del'}" /><u:radio
		value="stat" name="orgDelCd" titleId="or.syncCd.stat" alt="삭제 상태" checked="${orDbIntgDVo.orgDelCd != 'del' and rDbIntgDVo.orgDelCd != 'move'}" /><u:radio
		value="move" name="orgDelCd" titleId="or.syncCd.move" alt="삭제 상태 후 이동" checked="${orDbIntgDVo.orgDelCd == 'move'}" /></u:checkArea></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="or.cols.delUser" alt="삭제된 사용자" /></td>
	<td class="bodybg_lt">
	<u:checkArea><u:radio
		value="del" name="userDelCd" titleId="or.syncCd.del" alt="삭제" checked="${orDbIntgDVo.userDelCd == 'del'}" /><u:radio
		value="stat" name="userDelCd" titleId="or.syncCd.stat" alt="삭제 상태" checked="${orDbIntgDVo.userDelCd != 'del' and rDbIntgDVo.userDelCd != 'move'}" /><u:radio
		value="move" name="userDelCd" titleId="or.syncCd.move" alt="삭제 상태 후 이동" checked="${orDbIntgDVo.userDelCd == 'move'}" /></u:checkArea></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="or.cols.delOrgMoveOrgId" alt="삭제된 조직을 이동할 조직ID" /></td>
	<td><table cellspacing="0" cellpadding="0" border="0">
			<tr><td><u:input id="delOrgMoveOrgNm" value="${delOrgMoveOrgNm}" titleId="or.cols.delOrgMoveOrgId" style="width:130px" readonly="Y" />
					<u:input id="delOrgMoveOrgId" value="${orDbIntgDVo.delOrgMoveOrgId}" type="hidden" /></td>
				<td><u:buttonS titleId="cm.btn.sel" popYn="Y" alt="선택" onclick="selectOrgPop('delOrgMoveOrg');"
					/><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="delSelectedOrg('delOrgMoveOrg');" /></td>
			</tr></table></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="or.cols.delUserMoveOrgId" alt="삭제된 사용자를 이동할 조직ID" /></td>
	<td><table cellspacing="0" cellpadding="0" border="0">
			<tr><td><u:input id="delUserMoveOrgNm" value="${delUserMoveOrgNm}" titleId="or.cols.delUserMoveOrgId" style="width:130px" readonly="Y" />
					<u:input id="delUserMoveOrgId" value="${orDbIntgDVo.delUserMoveOrgId}" type="hidden" /></td>
				<td><u:buttonS titleId="cm.btn.sel" popYn="Y" alt="선택" onclick="selectOrgPop('delUserMoveOrg');"
					/><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="delSelectedOrg('delUserMoveOrg');" /></td>
			</tr></table></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="or.cols.noDeptOrgId" alt="부서 없음 조직ID" /></td>
	<td><table cellspacing="0" cellpadding="0" border="0">
			<tr><td><u:input id="noDeptOrgNm" value="${noDeptOrgNm}" titleId="or.cols.noDeptOrgId" style="width:130px" readonly="Y" />
					<u:input id="noDeptOrgId" value="${orDbIntgDVo.noDeptOrgId}" type="hidden" /></td>
				<td><u:buttonS titleId="cm.btn.sel" popYn="Y" alt="선택" onclick="selectOrgPop('noDeptOrg');"
					/><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="delSelectedOrg('noDeptOrg');" /></td>
			</tr></table></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="or.cols.execTime" alt="실행 시간(HHMM)" /></td>
	<td><u:input id="intgTm" value="${orDbIntgDVo.intgTm}" titleId="or.cols.execTime" maxByte="30" mandatory="Y" style="width:180px" maxLength="4" valueOption="number" /></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
	<td class="bodybg_lt">
	<u:checkArea><u:checkbox name="useYn" value="N" checked="${orDbIntgDVo.useYn=='N'}" titleId="cm.option.notUse" alt="사용안함" /></u:checkArea></td>
	</tr>
</u:listArea>
</div><c:if test="${sessionScope.userVo.userUid eq 'U0000001'}">
<u:textarea id="cdSqlArea" alt="코드 SQL" name="cdSql" style="width:670px; display:none;" rows="27" value="${orDbIntgDVo.cdSql}" titleId="or.cols.cdSql" />
<u:textarea id="orgSqlArea" alt="조직도 SQL" name="orgSql" style="width:670px; display:none;" rows="27" value="${orDbIntgDVo.orgSql}" titleId="or.cols.orgSql" />
<u:textarea id="userSqlArea" alt="사용자 SQL" name="userSql" style="width:670px; display:none;" rows="27" value="${orDbIntgDVo.userSql}" titleId="or.cols.userSql" /></c:if>
</u:tabArea>
</form>
<u:buttonArea id="setUserPopBtnArea">
	<u:button href="javascript:testConn();" id="saveDbSyncBtn" alt="연결 테스트" titleId="or.btn.testConn" auth="A" />
	<u:button href="javascript:runNow();" id="saveDbSyncBtn" alt="지금실행" titleId="or.btn.runNow" auth="A" />
	<u:button href="javascript:saveDbSync();" id="saveDbSyncBtn" alt="저장" titleId="cm.btn.save" auth="A" />
	<u:button onclick="dialog.close(this);" alt="닫기" titleId="cm.btn.close" />
</u:buttonArea>

</div>