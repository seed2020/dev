<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(noSelectMsg){
	var arr=[], id, obj;
	$("#bcListArea tbody:first input[type=${fncMul == 'Y' ? 'checkbox' : 'radio' }]:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
};

<%// 선택목록ID 리턴 %>
function fnSelBc(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) return selRowInArr(arr);
	else return null;
};
<%// 배열에 담긴 목록%>
function selRowInArr(rowArr){
	var objArr = [], $bcId;
	//if(delVa!='') objArr.push(delVa);
	for(var i=0;i<rowArr.length;i++){
		$bcId = $(rowArr[i]).find("input[name='bcId']");
		if($bcId.val()!=''){
			objArr.push({bcId : $bcId.val() , originalBcId : $bcId.attr('data-originalBcId')});
		}
	}
	return objArr;
	//$("#delList").val(objArr.join(','));
};

$(document).ready(function() {
	setUniformCSS();
});
</script>
<div style="padding:5px">
<u:listArea id="bcDetail">
	<tr>
		<td class="head_lt"><u:msg titleId="cols.nm" alt="이름" /></td>
		<td colspan="3" class="body_lt"><a href="javascript:parent.viewBc('${wbBcBVo.bcId }');">${wbBcBVo.bcNm }</a></td>
	</tr>

	<tr>
		<td class="head_lt"><u:msg titleId="cols.comp" alt="회사" /></td>
		<td class="body_lt">${wbBcBVo.compNm }</td>
		<td class="head_lt"><u:msg titleId="cols.dept" alt="부서" /></td>
		<td class="body_lt">${wbBcBVo.deptNm }</td>
	</tr>

	<tr>
		<td class="head_lt"><u:msg titleId="wb.cols.vip" alt="주요인사" /></td>
		<td class="body_lt">${wbBcBVo.iptfgYn}</td>
		<td class="head_lt"><u:msg titleId="cols.grade" alt="직급" /></td>
		<td class="body_lt">${wbBcBVo.gradeNm}</td>
	</tr>

	<tr>
		<td class="head_lt"><u:msg titleId="cols.compPhon" alt="회사전화" /></td>
		<td class="body_lt">
			<select id="cntcTypCd" name="cntcTypCd" style="width:120px;">
				<c:forEach var="list" items="${wbBcBVo.wbBcCntcDVo }" varStatus="status">
					<c:if test="${list.cntcTypCd eq 'compPhon' && !empty list.cntcCont}">
						<u:option value="${list.cntcCont }" title="${list.cntcCont }" />
					</c:if>
				</c:forEach>
			</select>
		</td>
		<td class="head_lt"><u:msg titleId="cols.tich" alt="담당업무" /></td>
		<td class="body_lt">${wbBcBVo.tichCont}</td>
	</tr>

	<tr>
		<td class="head_lt"><u:msg titleId="cols.homePhon" alt="자택전화" /></td>
		<td class="body_lt">
			<select id="cntcTypCd" name="cntcTypCd" style="width:120px;">
				<c:forEach var="list" items="${wbBcBVo.wbBcCntcDVo }" varStatus="status">
					<c:if test="${list.cntcTypCd eq 'homePhon' && !empty list.cntcCont}">
						<u:option value="${list.cntcCont }" title="${list.cntcCont }" />
					</c:if>
				</c:forEach>
			</select>
		</td>
		<td class="head_lt"><u:msg titleId="cols.email" alt="이메일" /></td>
		<td class="body_lt">
			<ul style="list-style:none;margin:0;padding-left:2px;float:left;">
				<c:forEach var="list" items="${wbBcBVo.wbBcEmailDVo }" varStatus="status">
					<li style="float:left;width:150px;"><a href="javascript:parent.mailToPop('${list.cntcCont }')" title="<u:msg titleId='or.jsp.viewUserPop.mailToPop' />">${list.cntcCont }</a></li>								
				</c:forEach>
			</ul>
		</td>
	</tr>

	<tr>
		<td class="head_lt"><u:msg titleId="cols.mobPhon" alt="휴대전화" /></td>
		<td class="body_lt">
			<select id="cntcTypCd" name="cntcTypCd" style="width:120px;">
				<c:forEach var="list" items="${wbBcBVo.wbBcCntcDVo }" varStatus="status">
					<c:if test="${list.cntcTypCd eq 'mbno' && !empty list.cntcCont}">
						<u:option value="${list.cntcCont }" title="${list.cntcCont }" />
					</c:if>
				</c:forEach>
			</select>
		</td>
		<td class="head_lt"><u:msg titleId="cols.fax" alt="팩스" /></td>
		<td class="body_lt">${wbBcBVo.fno}</td>
	</tr>

	<tr>
		<td class="head_lt"><u:msg titleId="cols.compAdr" alt="회사주소" /></td>
		<td colspan="3" class="body_lt">&nbsp;</td>
	</tr>

	<tr>
		<td class="head_lt"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
		<td class="body_lt">${wbBcBVo.regDt}</td>
		<td class="head_lt"><u:msg titleId="cols.regr" alt="등록자" /></td>
		<td class="body_lt"><a href="javascript:viewUserPop('${wbBcBVo.regrUid}');">${wbBcBVo.regrNm}</a></td>
	</tr>
	</u:listArea>
	<u:listArea id="bcListArea">
		<tr id="headerTr">
			<td width="5%" class="head_ct">
				<c:choose>
					<c:when test="${param.fncMul == 'Y'}"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('bcListArea', this.checked);" value=""/></c:when>
					<c:otherwise>&nbsp;</c:otherwise>
				</c:choose>
			</td>
			<td width="15%" class="head_ct"><u:msg titleId="cols.nm" alt="이름" /></td>
			<td class="head_ct"><u:msg titleId="wb.cols.compDept" alt="회사/부서" /></td>
			<td width="15%" class="head_ct"><u:msg titleId="wb.cols.main" alt="Main" /></td>
		</tr>
		<c:choose>
			<c:when test="${!empty wbBcBVoList}">
				<c:forEach var="list" items="${wbBcBVoList}" varStatus="status">
					<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
						<td class="bodybg_ct">
							<c:choose>
								<c:when test="${param.fncMul == 'Y'}"><u:checkbox id="check_${list.bcId }" name="bcCheck" value="${list.bcId }" checked="false" /><input type="hidden" name="bcId" value="${list.bcId }" data-bcNm="${list.bcNm }" data-compNm="${list.compNm }" data-originalBcId="${list.originalBcId }"/></c:when>
								<c:otherwise><u:radio id="check_${list.bcId }" name="bcCheck" value="${list.bcId }" checked="false" /><input type="hidden" name="bcId" value="${list.bcId }" data-bcNm="${list.bcNm }" data-compNm="${list.compNm }" data-originalBcId="${list.originalBcId }"/></c:otherwise>
							</c:choose>
						</td>
						<td class="body_ct"><a href="javascript:parent.viewBc('${list.bcId }');">${list.bcNm }</a></td>
						<td class="body_ct">${list.compNm }/${list.deptNm }</td>
						<td class="body_ct">${empty list.mainSetupYn ? 'N' : list.mainSetupYn }</td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
				</tr>
			</c:otherwise>
		</c:choose>
	</u:listArea>
	<div class="color_txt"><u:msg titleId="wb.jsp.duplFrnd.tx02" alt="※ Main"/></div>
	<div class="color_txt"><u:msg titleId="wb.jsp.duplFrnd.tx03" alt="'O' : 복사된 이력이 없는 원본명함(Original)"/></div>
	<div class="color_txt"><u:msg titleId="wb.jsp.duplFrnd.tx04" alt="'C' : 복사된 이력을 가진 명함의 원본(Copied)"/></div>
	<div class="color_txt"><u:msg titleId="wb.jsp.duplFrnd.tx05" alt="'D' : 복사된 명함의 사본(Duplicate)"/></div>
	<div class="color_txt"><u:msg titleId="wb.jsp.duplFrnd.tx06" alt="’Y’ : 중복지인관리에서 관리자가 메인으로 설정한 공개명함"/></div>
	<div class="color_txt"><u:msg titleId="wb.jsp.duplFrnd.tx07" alt="’N’ : 메인으로 설정되지 않은 동일인 공개명함(메인여부가 No)"/></div>
</div>