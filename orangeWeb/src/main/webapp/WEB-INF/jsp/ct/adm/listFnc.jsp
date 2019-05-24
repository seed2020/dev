<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

function selectTab(tabNoRT){
	if(tabNoRT == 0)
	{
		$("#fncListArea1").show();
		$("#fncListArea2").hide();
	}
	else
	{
		$("#fncListArea1").hide();
		$("#fncListArea2").show();
	}
}

function regFnc() {
	dialog.open('setFncPop','<u:msg titleId="ct.jsp.fnc.reg.title" alt="커뮤니티 기능 등록" />','./setFncPop.do?menuId=${menuId}&fnc=reg');
}

function modFnc(ctFncId, typ) {
	dialog.open('setFncPop','<u:msg titleId="ct.jsp.fnc.mod.title" alt="커뮤니티 기능 수정" />','./setFncPop.do?menuId=${menuId}&fnc=mod&ctFncId=' + ctFncId+'&typ='+typ);
}

<%// [팝업:커뮤니티 기능관리 등록, 수정] - 저장 버튼 %>
function saveFnc(){
	if(validator.validate('setFncForm')){
		var $frm = $('#setFncForm');
		$frm.attr('method','post');
		$frm.attr('action','./transFncSave.do');
		$frm.attr('target','dataframe');
		$frm.submit();
		
		//dialog.close('setFncPop');
	}
};

$(document).ready(function() {
	setUniformCSS();
	changeTab('ctTab','0');
});
//-->
</script>
<u:secu auth="SYS" ><c:set var="sysAuth" value="Y"/></u:secu>
<u:title titleId="ct.jsp.listFnc.title" alt="커뮤니티 기능관리" menuNameFirst="true" />

	<u:tabGroup id="ctTab" noBottomBlank="true">
		<u:tab id="ctTab" areaId="fncListArea1" titleId="ct.jsp.fnc.menu1.title" on="true" onclick="selectTab(0);"/>
		<u:tab id="ctTab" areaId="fncListArea2" titleId="ct.jsp.fnc.menu2.title" onclick="selectTab(1);"/>
	</u:tabGroup>
	<u:tabArea
		outerStyle = "height:387px; overflow-x:hidden; overflow-y:auto;"
		innerStyle = "height:367px; margin:5px;"
		noBottomBlank="true"
		>
	
		<div id="fncListArea1">
		

		<u:listArea id="listArea">
				<tr>
				<td class="head_ct"><u:msg titleId="cols.fncNm" alt="기능명" /></td>
				<td width="6%" class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
				</tr>
				
				<c:forEach items="${ctFncMngList1}" var="ctFncMngList1" varStatus="status">
					<c:if test="${ctFncMngList1.ctFncId != 'CTFNCFOLDER'}">
						<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
							<td class="body_lt">
								<c:choose>
									<c:when test="${!empty sysAuth && sysAuth eq 'Y' }"><a href="javascript:modFnc('${ctFncMngList1.ctFncId}','1');">${ctFncMngList1.ctFncNm}</a></c:when>
									<c:otherwise>${ctFncMngList1.ctFncNm}</c:otherwise>
								</c:choose>
								<input type="hidden"  id="ctFncId" name="ctFncId" value="${ctFncMngList1.ctFncId}" />
							</td>
							<td class="body_ct">${ctFncMngList1.useYn}</td>
						</tr>
					</c:if>
				</c:forEach>
				
				<c:if test="${fn:length(ctFncMngList1) == 0}">
					<tr>
					<td class="nodata" colspan="6"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
					</tr>
				</c:if>
				 
			</u:listArea>
			<c:if test="${!empty sysAuth && sysAuth eq 'Y' }">
				<div class="front">
				<div class="front_left">
					<table border="0" cellpadding="0" cellspacing="0"><tbody>
					<tr>
					<td class="color_txt">※ <u:msg titleId="ct.msg.fnc.menu.title" alt="수정이후에 개설되는 커뮤니티에 적용됩니다."/></td>
					</tr>
					</tbody></table>
				</div>
				</div>
			</c:if>
			
		</div>
		
		
		<div id="fncListArea2">
			<u:listArea id="listArea">
				<tr>
				<td class="head_ct"><u:msg titleId="cols.fncNm" alt="기능명" /></td>
				<td width="10%" class="head_ct"><u:msg titleId="cols.dftYn" alt="디폴트여부" /></td>
				<td width="10%" class="head_ct"><u:msg titleId="cols.mulChoiYn" alt="다중선택여부" /></td>
				<td width="6%" class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
				</tr>
				
				<c:forEach items="${ctFncMngList2}" var="ctFncMngList2" varStatus="status">
						<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
							<td class="body_lt"><a href="javascript:modFnc('${ctFncMngList2.ctFncId}','2');">${ctFncMngList2.ctFncNm}</a>
								<input type="hidden"  id="ctFncId" name="ctFncId" value="${ctFncMngList2.ctFncId}" />
							</td>
							<!-- <td class="body_ct">${ctFncMngList.ctFncTyp}</td> -->
							<td class="body_ct">${ctFncMngList2.dftYn}</td>
							<td class="body_ct">${ctFncMngList2.mulChoiYn}</td>
							<td class="body_ct">${ctFncMngList2.useYn}</td>
						
						</tr>
				</c:forEach>
				
				<c:if test="${fn:length(ctFncMngList2) == 0}">
					<tr>
					<td class="nodata" colspan="6"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
					</tr>
				</c:if>
				 
			</u:listArea>
			
			<u:pagination />
			
			<u:buttonArea>
				<u:button titleId="cm.btn.reg" alt="등록" auth="A" onclick="regFnc();" />
			</u:buttonArea>
		</div>

		
	</u:tabArea>

