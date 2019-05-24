<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:tabGroup id="bcTab">
<u:tab id="bcTab" areaId="dftInfoArea" titleId="wb.jsp.setBc.tab.dftInfo" alt="기본정보" on="true" />
<u:tab id="bcTab" areaId="addInfoArea" titleId="wb.jsp.setBc.tab.addInfo" alt="추가정보" />
</u:tabGroup>

<% // LEFT %>
<div class="profile_left">
	<dl>
	<dd class="photo">
		<c:choose>
		<c:when test="${empty wbBcBVo.wbBcImgDVo.imgPath}"><img id="bcImage" src="${_cxPth}/images/${_skin}/photo_noimg.png" width="88px"/></c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${empty param.popYn }">
					<fmt:parseNumber var="imgWdth" type="number" value="${wbBcBVo.wbBcImgDVo.imgWdth}" />
					<c:if test="${imgWdth > 800}"	>
						<a href="${_cxPth}${wbBcBVo.wbBcImgDVo.imgPath}" target="viewPhotoWin"><img id="bcImage" src="${_cxPth}${wbBcBVo.wbBcImgDVo.imgPath}" width="88px" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"'/></a>
					</c:if>
					<c:if test="${imgWdth <= 800}">
						<a href="javascript:viewBcImageDetl('${wbBcBVo.bcId}');"><img id="bcImage" src="${_cxPth}${wbBcBVo.wbBcImgDVo.imgPath}" width="88px" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"'/></a>
					</c:if>
				</c:when>
				<c:otherwise><img id="bcImage" src="${_cxPth}${wbBcBVo.wbBcImgDVo.imgPath}" width="88px" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"'/></c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose>
	</dd>
	</dl>
</div>

<% // RIGHT %>
<div class="profile_right">

	<% // 기본정보 %>
<div id="dftInfoArea" class="inner">

	<div class="front">
		<c:if test="${listPage ne 'listPubBc' }">
		<div class="front_left">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="color_stxt">
						<u:msg titleId="${wbBcBVo.publTypCd eq 'priv' ? 'wb.jsp.viewBc.txPriv' : 'wb.jsp.viewBc.txPubl'}" alt="* 본 명함은 공개되어 있지 않습니다." />
					</td>
				</tr>
			</table>
		</div>
		</c:if>
		<div class="front_right">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="color_stxt">
						<u:msg titleId="wb.jsp.viewBc.txFld" alt="* [{0}] 폴더에 지정되어 있습니다."
						arguments="${wbBcBVo.fldId eq 'NONE' ? '#dm.cols.emptyCls' : wbBcBVo.fldNm}" /></td>
					</tr>
				</table>
			</div>
		</div>

		<div class="listarea">
			<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<c:if test="${listPage ne 'listPubBc' }">
				<tr>
					<td width="18%" class="head_lt"><u:msg titleId="cols.publYn" alt="공개여부" /></td>
					<td class="bodybg_lt" colspan="3">
						<c:choose>
							<c:when test="${wbBcBVo.publTypCd eq 'allPubl' }"><span class="color_stxt" style="margin-left:5px;font-weight:bold;"><u:msg titleId="cm.option.allPubl" alt="전체공개"/></span></c:when>
							<c:when test="${wbBcBVo.publTypCd eq 'deptPubl' }"><span class="color_stxt" style="margin-left:5px;font-weight:bold;"><u:msg titleId="cm.option.deptPubl" alt="부서공개"/></span></c:when>
							<c:when test="${wbBcBVo.publTypCd eq 'apntPubl' }">
								<span id="apntrUserMsg" class="color_stxt" style="margin-left:5px;font-weight:bold;display:${!empty wbBcBVo.wbBcApntrRVoList ? '' : 'none' }"><u:msg titleId="wb.jsp.viewBc.apntr" arguments="${wbBcBVo.wbBcApntrRVoList[0].userNm },${fn:length(wbBcBVo.wbBcApntrRVoList) -1}"/></span>
								<u:buttonS href="javascript:;" titleId="cm.option.apntPubl" alt="지정인공개" onclick="openMuiltiUser('view');"/>
								<div id="apntrList" style="display:none;">
									<c:forEach var="list" items="${wbBcBVo.wbBcApntrRVoList }" varStatus="status">
										<u:input type="text" id="userUid" name="bcApntrUid" value="${list.bcApntrUid }"/>
									</c:forEach>
								</div>									
							</c:when>
							<c:otherwise><span class="color_stxt" style="margin-left:5px;font-weight:bold;"><u:msg titleId="cm.option.priv" alt="비공개"/></span></c:otherwise>
						</c:choose>
					</td>
				</tr>
				</c:if>
				<tr>
					<td width="18%" class="head_lt"><u:msg titleId="cols.fldNm" alt="폴더명" /></td>
					<td><span id="fldNm" class="color_stxt" style="margin-left:5px;font-weight:bold;">${wbBcBVo.fldId eq 'NONE' ? '' : wbBcBVo.fldNm}</span></td>
				</tr>
			</table>
			<u:blank />
			<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<tr>
					<td width="18%" class="head_lt"><u:msg titleId="cols.nm" alt="이름" /></td>
					<td width="32%" class="body_lt">${wbBcBVo.bcNm}</td>
					<td width="18%" class="head_lt"><u:term termId="wb.cols.enNm" alt="영문이름" /></td>
					<td width="32%" class="body_lt">${wbBcBVo.bcEnNm}</td>
				</tr>
			</table>

			<u:blank />
			
			<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<c:forTokens var="phonCntcTypCd" items="compPhon,homePhon,mbno" delims="," varStatus="cdStatus">
					<tr>
						<c:if test="${cdStatus.count == 1 }">
							<td width="9%" rowspan="3" class="head_lt"><u:msg titleId="cols.phon" alt="전화번호" /></td>
						</c:if>
						<td width="9%" class="head_lt" ><c:if test="${wbBcBVo.dftCntcTypCd eq phonCntcTypCd }"><u:mandatory /></c:if><u:msg titleId="cols.${phonCntcTypCd eq 'mbno' ? 'mob' : ( phonCntcTypCd eq 'homePhon' ? 'home': 'comp' )}" alt="회사" /></td>
						<td class="body_lt">
							<ul style="list-style:none;margin:0;padding-left:2px;float:left;">
								<c:forEach var="list" items="${wbBcBVo.wbBcCntcDVo }" varStatus="status">
									<c:if test="${list.cntcTypCd eq phonCntcTypCd}">
									<%-- <li style="float:left;width:120px;">${list.cntcCont } </li>  --%>
									${list.cntcCont }<br/>
									</c:if>
								</c:forEach>
							</ul>
						</td>
					</tr>
				</c:forTokens>
			</table>

			<u:blank />

			<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<tr>
					<td width="18%" class="head_lt"><u:msg titleId="cols.comp" alt="회사" /></td>
					<td colspan="3" class="body_lt">${wbBcBVo.compNm}</td>
				</tr>

				<tr>
					<td width="18%" class="head_lt"><u:msg titleId="cols.dept" alt="부서" /></td>
					<td width="32%" class="body_lt">${wbBcBVo.deptNm}</td>
					<td width="18%" class="head_lt"><u:term termId="or.term.grade" alt="직급" /></td>
					<td class="body_lt">${wbBcBVo.gradeNm}</td>
				</tr>

				<tr>
					<td class="head_lt"><u:msg titleId="cols.tich" alt="담당업무" /></td>
					<td class="body_lt">${wbBcBVo.tichCont}</td>
					<td class="head_lt"><u:msg titleId="wb.cols.vip" alt="주요인사" /></td>
					<td class="body_lt"><u:checkbox name="" value="Y" checkValue="${wbBcBVo.iptfgYn}" disabled="Y"/></td>
				</tr>

				<tr>
					<td width="18%" class="head_lt"><u:msg titleId="cols.fno" alt="팩스번호" /></td>
					<td colspan="3" class="body_lt">${wbBcBVo.fno}</td>
				</tr>
				<tr>
					<td width="18%" class="head_lt"><u:msg titleId="cols.email" alt="이메일" /></td>
					<td colspan="3" class="body_lt">
						<ul style="list-style:none;margin:0;padding-left:2px;float:left;">
							<c:forEach var="list" items="${wbBcBVo.wbBcEmailDVo }" varStatus="status">
								<%-- <li style="float:left;width:150px;"><a href="javascript:parent.mailToPop('${list.cntcCont }')" title="<u:msg titleId='or.jsp.viewUserPop.mailToPop' />">${list.cntcCont }</a></li>								 --%>
								<a href="javascript:parent.mailToPop('${list.cntcCont }')" title="<u:msg titleId='or.jsp.viewUserPop.mailToPop' />">${list.cntcCont }</a><br/>
							</c:forEach>
						</ul>
					</td>
				</tr>
			</table>

			<u:blank />

			<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<tr>
					<td width="18%"  class="head_lt"><u:msg titleId="cols.compAdr" alt="회사주소"/></td>
					<td class="body_lt"><u:address id="comp" alt="회사주소" adrStyle="width:94%" zipNoValue="${wbBcBVo.compZipNo }" adrValue="${wbBcBVo.compAdr }" type="view"/></td>
				</tr>
				<tr>
					<td class="head_lt"><u:msg titleId="cols.homeAdr" alt="자택주소"/></td>
					<td class="body_lt"><u:address id="home" alt="자택주소" adrStyle="width:94%" zipNoValue="${wbBcBVo.homeZipNo }" adrValue="${wbBcBVo.homeAdr }" type="view"/></td>
				</tr>
			</table>
			<c:if test="${listPage ne 'listPubBc' }">
			<u:blank />
			
			<u:title titleId="wb.jsp.viewBc.metng" type="small" alt="관련미팅" />

			<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<tr>
					<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
					<td class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
					<td class="head_ct"><u:msg titleId="cols.metngDt" alt="관련미팅일시" /></td>
					<td class="head_ct"><u:msg titleId="cols.grad" alt="등급" /></td>
				</tr>
				<c:choose>
					<c:when test="${!empty wbBcMetngDVoList }">
						<c:forEach var="list" items="${wbBcMetngDVoList }" varStatus="status">
							<tr>
								<td class="body_lt"><a href="javascript:parent.viewMetngPop('${list.bcMetngDetlId }');">${list.metngSubj }</a></td>
								<td class="body_ct"><a href="javascript:viewUserPop('${list.regrUid}');">${list.regrNm }</a></td>
								<td class="body_ct">${list.metngYmd }</td>
								<td class="body_ct"><u:msg titleId="cm.option.${list.openYn eq 'Y' ? 'publ' : 'priv'}" alt="보안등급" /></td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td class="nodata" colspan="4"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
						</tr>
					</c:otherwise>
				</c:choose>
			</table>
			</c:if>
		</div>
	</div>

	<% // 추가정보 %>
	<div id="addInfoArea" class="inner" style="display: none;">

		<div class="listarea">
			<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<tr>
					<td width="18%" class="head_lt"><u:msg titleId="cols.gen" alt="성별" /></td>
					<td width="32%" class="body_lt">${wbBcBVo.genNm }</td>
					<td width="18%" class="head_lt"><u:msg titleId="cols.naty" alt="국적" /></td>
					<td width="32%" class="body_lt">${wbBcBVo.natyNm }</td>
				</tr>

				<tr>
					<td class="head_lt"><u:msg titleId="cols.birth" alt="생년월일" /></td>
					<td class="body_lt">
						<c:if test="${!empty wbBcBVo.birth}">(<u:msg titleId="${wbBcBVo.birthSclcCd eq 'LUNA' ? 'cols.luna' : 'cols.sola'}" />)</c:if>
						<u:out value="${wbBcBVo.birth}" type="date"/>
					</td>
					<td class="head_lt"><u:msg titleId="cols.weddAnnv" alt="결혼기념일" /></td>
					<td class="body_lt">
						<c:if test="${!empty wbBcBVo.weddAnnv}">(<u:msg titleId="${wbBcBVo.weddAnnvSclcCd eq 'LUNA' ? 'cols.luna' : 'cols.sola'}" />)</c:if>
						<u:out value="${wbBcBVo.weddAnnv}" type="date"/>
					</td>
				</tr>

				<tr>
					<td class="head_lt"><u:msg titleId="cols.psnHpage" alt="개인홈페이지" /></td>
					<td colspan="3" class="body_lt">${wbBcBVo.psnHpageUrl}</td>
				</tr>

				<tr>
					<td class="head_lt"><u:msg titleId="cols.compHpage" alt="회사홈페이지" /></td>
					<td colspan="3" class="body_lt">${wbBcBVo.compHpageUrl}</td>
				</tr>
			</table>

			<u:blank />

			<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<tr>
					<td width="18%" class="head_lt"><u:msg titleId="cols.hoby" alt="취미" /></td>
					<td width="32%" class="body_lt">${wbBcBVo.hobyCont}</td>
					<td width="18%" class="head_lt"><u:msg titleId="cols.spect" alt="특기사항" /></td>
					<td class="body_lt">${wbBcBVo.spectCont}</td>
				</tr>

				<tr>
					<td class="head_lt"><u:msg titleId="cols.eschl" alt="초등학교" /></td>
					<td class="body_lt">${wbBcBVo.eschlNm}</td>
					<td rowspan="3" class="head_lt"><u:msg titleId="cols.univGschl" alt="대학교/대학원" /></td>
					<td rowspan="3" class="body_lt">${wbBcBVo.univCont}</td>
				</tr>

				<tr>
					<td class="head_lt"><u:msg titleId="cols.mschl" alt="중학교" /></td>
					<td class="body_lt">${wbBcBVo.mschlNm}</td>
				</tr>

				<tr>
					<td class="head_lt"><u:msg titleId="cols.hschl" alt="고등학교" /></td>
					<td class="body_lt">${wbBcBVo.hschlNm}</td>
				</tr>
			</table>

			<u:blank />

			<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<tr>
					<td width="18%" class="head_lt"><u:msg titleId="cols.bhist" alt="약력" /></td>
					<td class="body_lt">${wbBcBVo.bhistCont}</td>
				</tr>
			</table>

			<u:blank />

			<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<colgroup><col width="18%"/><col width="*"/></colgroup>
				<c:if test="${listPage ne 'listPubBc' }">
				<tr>
					<td class="head_lt"><u:msg titleId="cols.clns" alt="친밀도" /></td>
					<td class="body_lt">${wbBcBVo.clnsNm}</td>
				</tr>
				</c:if>
				<tr>
					<td class="head_lt"><u:msg titleId="cols.note" alt="비고" /></td>
					<td class="body_lt">${wbBcBVo.noteCont}</td>
				</tr>

				<tr>
					<td class="head_lt"><u:msg titleId="cols.attFile" alt="첨부파일" /></td>
					<td class="body_lt">
						<c:if test="${!empty fileVoList }"><u:files id="${filesId}" fileVoList="${fileVoList}" module="wb" mode="view" /></c:if>
					</td>
				</tr>
			</table>
		</div>
	</div>
</div>