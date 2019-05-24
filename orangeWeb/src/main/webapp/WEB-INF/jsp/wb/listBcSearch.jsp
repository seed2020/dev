<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<div class="front notPrint">
	<div class="front_left">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td style="padding:3px 4px 0 0px">
				<c:choose>
					<c:when test="${listPage eq 'listAgntBc' }"><u:title titleId="wb.jsp.listBc.agnt.title" alt="대리명함" menuNameFirst="true" /></c:when>
					<c:when test="${listPage eq 'listOpenBc' }"><u:title titleId="wb.jsp.listOpenBc.title" alt="공개명함" menuNameFirst="true" /></c:when>
					<c:when test="${listPage eq 'listAllBc' }"><u:title titleId="wb.jsp.listAllBc.title" alt="전체명함조회" menuNameFirst="true" /></c:when>
					<c:when test="${listPage eq 'listPubBc' }"><u:title titleId="wb.jsp.listPubBc.title" alt="공유명함" menuNameFirst="true" /></c:when>
					<c:otherwise><u:title titleId="wb.jsp.listBc.title" alt="개인명함" menuNameFirst="true" /></c:otherwise>
				</c:choose>
			</td>
			<c:if test="${listPage eq 'listAgntBc' }">
				<td class="width5"></td>
				<td class="frontinput">
					<select id="bcRegrUid" name="schBcRegrUid" <u:elemTitle titleId="wb.jsp.setAgntAdm.tab.agntBc" alt="대리인기본값설정" /> onchange="fnAgntSch(this);">
						<u:option value="" titleId="wb.jsp.setAgntAdm.select.agntBc" selected="${empty agntSetupList }"/>
						<c:forEach items="${agntSetupList}" var="agntVo" varStatus="status">
							<u:option value="${agntVo.regrUid}" title="${agntVo.userNm}" checkValue="${schBcRegrUid }"/>
						</c:forEach>
					</select>
				</td>
			</c:if>
	 		</tr>
		</table>
	</div>
</div>
<u:searchArea>
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
		<tr>
			<td>
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td>
										<select name="schCat" style="width:120px;">
											<c:forEach	items="${wbBcUserLstSetupRVoList}" var="wbBcUserLstSetupRVo" varStatus="status">
												<u:option value="${wbBcUserLstSetupRVo.atrbId }" titleId="${wbBcUserLstSetupRVo.msgId }" alt="이름" checkValue="${param.schCat}" />
											</c:forEach>
											<%-- <c:if test="${listPage eq 'listAllBc' }">
												<u:option value="regrNm" titleId="cols.regr" alt="등록자" checkValue="${param.schCat}" />
											</c:if> --%>
										</select>
									</td>
									<td><u:input id="schWord" maxByte="50" name="schWord" value="${param.schWord}" titleId="cols.schWord" style="width:200px;" onkeydown="if (event.keyCode == 13) searchForm.submit();"/></td>
								</tr>
							</table>
						</td>
						<td class="width30"></td>
						<c:choose>
							<c:when test="${listPage eq 'listOpenBc' }">
								<td>
									<c:forEach var="list" items="${schOpenTypCds }" varStatus="status">
										<c:if test="${list eq 'allPubl' }"><c:set var="allPublCheck" value="allPubl"/></c:if>
										<c:if test="${list eq 'deptPubl' }"><c:set var="deptPublCheck" value="deptPubl"/></c:if>
										<c:if test="${list eq 'apntrPubl' }"><c:set var="apntrPublCheck" value="apntrPubl"/></c:if>
									</c:forEach>
									<u:checkArea>
										<u:checkbox name="schOpenTypCd" value="allPubl" titleId="cm.option.allPubl" alt="전체공개" checkValue="${allPublCheck }"/>
										<u:checkbox name="schOpenTypCd" value="deptPubl" titleId="cm.option.deptPubl" alt="부서공개" checkValue="${deptPublCheck }"/>
										<u:checkbox name="schOpenTypCd" value="apntrPubl" titleId="cm.option.apntPubl" alt="지정인공개" checkValue="${apntrPublCheck}"/>
									</u:checkArea>
								</td>
								<td class="width30"></td>
							</c:when>
							<c:when test="${listPage eq 'listAllBc' }"></c:when>
							<c:otherwise>
								<%-- <td>
									<table border="0" cellpadding="0" cellspacing="0">
										<tr>
											<td>
												<u:input id="schFldNm" name="schFldNm" value="${param.schFldNm }" titleId="cols.fldNm" readonly="readonly" />
												<u:input type="hidden" id="schFldId" name="schFldId" value="${param.schFldId}" />
											</td>
											<td>
												<u:buttonS titleId="wb.btn.fldChoi" alt="${fldSelTitle }" href="javascript:dialog.open('findBcFldPop','${fldSelTitle }','./findBcFldPop.do?menuId=${menuId}${agntParam }');" />
												<u:buttonS titleId="wb.btn.fldChoi.reset" alt="${fldSelTitle }" href="javascript:;" onclick="$('#schFldNm').val('');$('#schFldId').val('');"/>
											</td>
											<td class="width10"></td>
											<td><u:checkbox id="schFldTypYnH" name="schFldTypYn" value="A" checkValue="${param.schFldTypYn}" titleId="wb.cols.subFldIncl" checked="${empty param.schFldTypYn }"/></td>
										</tr>
									</table>
								</td>
								<td class="width30"></td> --%>
							</c:otherwise>
						</c:choose>
						<td><u:checkArea><u:checkbox id="schIptfgYn" name="schIptfgYn" value="Y" titleId="wb.cols.vip" checkValue="${param.schIptfgYn}"/></u:checkArea></td>
						<%-- <td class="width30"></td>
						<td>
							<u:buttonS href="javascript:;" onclick="fnSearchInit();" titleId="cm.btn.reset" alt="초기화" />
						</td> --%>
					</tr>
					<c:if test="${listPage eq 'listAllBc' }">
						<tr>
							<td colspan="3">
								<table id="search_area_all" border="0" cellpadding="0" cellspacing="0">
									<tr>
										<td>
											<u:input type="hidden" id="schRegrUid" value="${param.schRegrUid}" />
											<u:input id="schRegrNm" name="schRegrNm" value="${param.schRegrNm}" titleId="cols.regr" style="width:100px;" readonly="Y"/>
										</td>
										<td width="60px"><u:buttonS titleId="cols.regr" alt="등록자" href="javascript:;" onclick="schUserPop();"/></td>
										<td class="width30"></td>
										<td class="search_tit" width="80px"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
										<td>
											<input type="hidden" name="durCat" value="fromYmd"/>
											<u:calendar id="durStrtDt" value="${param.durStrtDt}" />
										</td>
										<td class="search_titx">&nbsp;&nbsp;~</td>
										<td><u:calendar id="durEndDt" value="${param.durEndDt}" /></td>
										<td class="width30"></td>
										<td><u:buttonS titleId="wb.btn.schOption.reset" alt="초기화" href="javascript:;" onclick="fnSchValInit();"/></td>
									</tr>
								</table>
							</td>
						</tr>
					</c:if>
				</table>
			</td>
			<td>
				<div class="button_search">
					<ul>
						<li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li>
					</ul>
				</div>
			</td>
		</tr>
	</table>
</u:searchArea>
