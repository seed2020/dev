<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<div style="width:550px">
	<form id="setPltPop">
	<input type="hidden" name="menuId" value="${menuId}" />
	<u:input type="hidden" id="pltId" value="${ptPltDVo.pltId}" />

	<u:listArea style="width:550px">

	<tr>
		<td width="25%" class="head_lt"><u:mandatory /><u:msg titleId="cols.pltNm" alt="포틀릿명" /></td>
		<td>
			<table cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td id="langTypArea">
						<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:convert srcId="${baBrdBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
							<u:set test="${status.first}" var="style" value="width:200px;" elseValue="width:200px; display:none" />
							<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
							<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.pltNm" value="${rescVa}" style="${style}"
								maxByte="200" validator="changeLangSelector('setPltPop', id, va)" mandatory="Y" />
						</c:forEach>
						</td>
						<td>
						<c:if test="${fn:length(_langTypCdListByCompId)>1}">
							<select id="langSelector" onchange="changeLangTypCd('setPltPop','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
							<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
							</c:forEach>
							</select>
						</c:if>
						<u:input type="hidden" id="rescId" value="" />
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<u:decode srcId="_lang" tgtValue="ko" var="cdLoc" value="공통코드 : 포털 / 포털카테고리 / 포틀릿카테고리" elseValue="" />
		<td width="25%" class="head_lt" title="${cdLoc}"><u:msg titleId="cols.pltCat" alt="포틀릿카테고리" /></td>
		<td>
			<select id="pltCatCd" name="pltCatCd" <u:elemTitle titleId="cols.pltCat" />>
			<c:forEach items="${pltCatCdList}" var="pltCatCd" varStatus="status">
			<u:option value="${pltCatCd.cd}" title="${pltCatCd.rescNm}" checkValue="bb" />
			</c:forEach>
			</select>
		</td>
	</tr>

	<tr>
		<td width="25%" class="head_lt"><u:mandatory /><u:msg titleId="cols.pltUrl" alt="포틀릿URL" /></td>
		<td><u:input id="pltUrl" value="/bb/plt/listBullPlt.do?brdId=${baBrdBVo.brdId}" titleId="cols.pltUrl" maxByte="1000" style="width:95%" mandatory="Y" /></td>
	</tr>

	<tr>
		<td width="25%" class="head_lt"><u:msg titleId="cols.moreUrl" alt="모어URL" /></td>
		<td><u:input id="moreUrl" value="${ptPltDVo.moreUrl}" titleId="cols.moreUrl" maxByte="1000" style="width:95%" /></td>
	</tr>

	<tr>
		<td width="25%" class="head_lt"><u:msg titleId="cols.setupUrl" alt="설정URL" /></td>
		<td><u:input id="setupUrl" value="${ptPltDVo.setupUrl}" titleId="cols.setupUrl" maxByte="1000" style="width:95%" /></td>
	</tr>

	<tr>
		<td width="25%" class="head_lt"><u:mandatory /><u:msg titleId="cols.wdthPx" alt="넓이픽셀" /></td>
		<td>
			<u:input id="wdthPx" value="400" titleId="cols.wdthPx"
			mandatory="Y" minInt="200" maxInt="800" valueOption="number" style="width:95%" />
		</td>
	</tr>

	<tr>
		<td width="25%" class="head_lt"><u:mandatory /><u:msg titleId="cols.hghtPx" alt="높이픽셀" /></td>
		<td>
			<u:input id="hghtPx" value="300" titleId="cols.hghtPx"
			mandatory="Y" minInt="200" maxInt="800" valueOption="number" style="width:95%" />
		</td>
	</tr>

	<tr>
		<td width="25%" class="head_lt"><u:msg titleId="cols.pltDesc" alt="포틀릿설명" /></td>
		<td><u:textarea id="pltDesc" value="${ptPltDVo.pltDesc}" titleId="cols.pltDesc" maxByte="400" rows="3" style="width:95%" /></td>
	</tr>

	<tr>
		<td width="25%" class="head_lt"><u:msg titleId="cols.titlYn" alt="타이틀여부" /></td>
		<td>
			<select id="titlYn" name="titlYn" <u:elemTitle titleId="cols.titlYn" />
			>
			<u:option value="Y" titleId="cm.option.use" checkValue="${ptPltDVo.titlYn}" />
			<u:option value="N" titleId="cm.option.notUse" checkValue="${ptPltDVo.titlYn}" />
			</select>
		</td>
	</tr>

	<tr>
		<td width="25%" class="head_lt"><u:msg titleId="cols.bordYn" alt="보더여부" /></td>
		<td>
			<select id="bordYn" name="bordYn" <u:elemTitle titleId="cols.bordYn" />>
			<u:option value="Y" titleId="cm.option.use" checkValue="${ptPltDVo.useYn}" />
			<u:option value="N" titleId="cm.option.notUse" checkValue="${ptPltDVo.useYn}" />
			</select>
		</td>
	</tr>

	<tr>
		<td width="25%" class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
		<td>
			<select id="useYn" name="useYn" <u:elemTitle titleId="cols.useYn" />>
			<u:option value="Y" titleId="cm.option.use" checkValue="${ptPltDVo.useYn}" />
			<u:option value="N" titleId="cm.option.notUse" checkValue="${ptPltDVo.useYn}" />
			</select>
		</td>
	</tr>

	</u:listArea>
	
	<u:buttonArea>
		<u:button titleId="cm.btn.save" href="javascript:savePlt();" alt="저장" auth="A" />
		<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
	</u:buttonArea>

	</form>
</div>
