<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<div class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="wb.cols.bc" alt="명함" /></td>
		<td width="32%" class="body_lt"><div class="ellipsis" title="${wbBcMetngDVo.bcNm }"><a href="javascript:viewBc('${wbBcMetngDVo.bcId}');">${wbBcMetngDVo.bcNm}</a></div></td>
		<td width="18%" class="head_lt"><u:msg titleId="cols.comp" alt="회사" /></td>
		<td class="body_lt"><div class="ellipsis" title="${wbBcMetngDVo.compNm }">${wbBcMetngDVo.compNm}</div></td>
	</tr>
	
	<tr>
		<td class="head_lt"><u:msg titleId="cols.metngDt" alt="관련미팅일시" /></td>
		<td class="body_lt">${wbBcMetngDVo.metngYmd}</td>
		<td class="head_lt"><u:msg titleId="cols.secul" alt="보안등급" /></td>
		<td class="bodybg_lt"><u:msg titleId="cm.option.${wbBcMetngDVo.openYn eq 'Y' ? 'publ' : 'priv'}" alt="보안등급" /></td>
	</tr>
	
	<tr>
		<td class="head_lt"><u:msg titleId="wb.cols.cls" alt="분류" /></td>
		<td colspan="3" class="body_lt"><c:if test="${wbBcMetngDVo.metngClsCd eq 'none' || empty wbBcMetngDVo.metngClsNm}"><u:msg titleId="cm.option.none" alt="없음"/></c:if><c:if test="${wbBcMetngDVo.metngClsCd ne 'none' && !empty wbBcMetngDVo.metngClsNm}">${wbBcMetngDVo.metngClsNm }</c:if></td>
	</tr>
	
	<tr>
		<td class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
		<td colspan="3" class="body_lt"><div class="ellipsis" title="${wbBcMetngDVo.metngSubj }">${wbBcMetngDVo.metngSubj}</div></td>
	</tr>
	
	<tr>
		<td class="head_lt"><u:msg titleId="cols.guest" alt="참석자" /></td>
		<td colspan="3" class="body_lt">
			<div style="width:100%;height:160px;overflow-y:auto;">
				<div id="listArea" class="listarea" style="width:95%; padding:5px;">
					<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
						<tr id="headerTr">
							<th width="13%"  class="head_ct"><u:msg titleId="cols.nm" alt="이름" /></th>
							<th width="12%" class="head_ct"><u:msg titleId="wb.cols.emplTyp" alt="임직원구분" /></th>
							<%-- <th class="head_ct"><u:msg titleId="cols.compNm" alt="회사명" /></th> --%>
							<th width="15%"  class="head_ct"><u:msg titleId="cols.compPhon" alt="회사전화번호" /></th>
							<th width="15%"  class="head_ct"><u:msg titleId="cols.mbno" alt="휴대전화번호" /></th>
							<th width="17%" class="head_ct"><u:msg titleId="cols.email" alt="이메일" /></th>
						</tr>
						<c:if test="${!empty wbBcMetngAtndRVoList}">
							<c:forEach var="list" items="${wbBcMetngAtndRVoList}" varStatus="status">
								<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
									<td class="body_ct" id="emplNm">
										<c:choose>
											<c:when test="${list.emplTypCd eq 'EMPL' }"><a href="javascript:viewUserPop('${list.emplId}');">${list.emplNm}</a></c:when>
											<c:when test="${list.emplTypCd eq 'FRND' }"><a href="javascript:viewBc('${list.emplId }');">${list.emplNm}</a></c:when>
											<c:otherwise>${list.emplNm}</c:otherwise>
										</c:choose>
									</td>
									<td class="body_ct" id="emplTypNm"><u:msg titleId="${list.emplTypCd eq 'FRND' ? 'wc.option.frnd' : (list.emplTypCd eq 'EMPL' ? 'cm.option.empl' : 'ct.option.etc')}" alt="지인"/></td>
									<%-- <td class="body_ct" id="emplCompNm"><div class="ellipsis" title="${list.compNm }">${list.compNm}</div></td> --%>
									<td class="body_ct" id="emplCompPhon">${list.compPhon}</td>
									<td class="body_ct" id="emplMbno">${list.emplPhon}</td>
									<td class="body_ct" id="emplEmail"><a href="javascript:parent.mailToPop('${list.email }');" title="<u:msg titleId='or.jsp.viewUserPop.mailToPop' />">${list.email}</a></td>
								</tr>
							</c:forEach>
						</c:if>
					</table>
				</div>
			</div>
		</td>
	</tr>
	
	<tr>
		<td class="head_lt"><u:msg titleId="cols.cont" alt="내용" /></td>
		<td colspan="3" class="body_lt">${wbBcMetngDVo.metngCont}</td>
	</tr>
	
	<tr>
		<td class="head_lt"><u:msg titleId="cols.attFile" alt="첨부파일" /></td>
		<td colspan="3"><c:if test="${!empty fileVoList }"><u:files id="${filesId}" fileVoList="${fileVoList}" module="wb" mode="view" actionParam="metng"/></c:if></td>
	</tr>
</table>
</div>

