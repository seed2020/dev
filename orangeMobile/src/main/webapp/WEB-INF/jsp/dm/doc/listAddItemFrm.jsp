<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<% //값이 등록된 항목을 조회 %>
function getChkVal(){
	var arr=[];
	$('#listArea').find("input[type='text'],input[type='hidden'],textarea,select").each(function(){
		arr.push({name:$(this).attr('name'),value:$(this).val()});
	});
	if(arr.length==0){
		return null;
	}
	return arr;
}<% //심의여부 리턴 %>
function getDiscYn(){
	return $('#listArea #discYn').val();
}
//-->
</script>
	<div class="entryarea" id="listArea">
	<input type="hidden" id="discYn" value="${discYn }"/>
	<dl>
		<c:if test="${!empty itemDispList }">
			<c:if test="${!empty dmDocLVoMap }"><c:set var="voMap" value="${dmDocLVoMap }" scope="request"/></c:if>
			<!-- 확장컬럼 -->
			<c:forEach var="dispVo" items="${itemDispList }" varStatus="status">
				<c:set var="colmVo" value="${dispVo.colmVo}" />
				<c:set var="itemTyp" value="${colmVo.itemTyp}" />
				<c:set var="itemNm" value="${dispVo.atrbId}" />
				<u:convertMap var="docVal" srcId="voMap" attId="${itemNm }" />
				<dd class="etr_bodytit">${colmVo.itemDispNm }</dd>
				<c:if test="${itemTyp == 'TEXT' || itemTyp == 'PHONE'}"><dd class="etr_input"><div class="etr_inputin"><input type="text" id="${itemNm }" name="${itemNm }" class="etr_iplt" value="${docVal}" maxlength="110"/></div></dd></c:if>
				<c:if test="${itemTyp == 'TEXTAREA'}"><dd class="etr_input"><div class="etr_textareain"><textarea name="${itemNm}" rows="${colmVo.itemTypVa}" class="etr_ta">${docVal }</textarea></div></dd></c:if>
				<c:if test="${itemTyp == 'CALENDAR'}">
					<dd class="etr_select">
	                    <div class="etr_calendar">
	                    	<input id="${itemNm }" name="${itemNm }" value="${docVal }" type="hidden" />
	                        <div class="etr_calendarin">
	                        <dl>
	                        <dd class="ctxt" onclick="fnCalendar('${itemNm }');"><span id="${itemNm }">${docVal }</span></dd>
	                        <dd class="cdelete" onclick="fnTxtDelete(this,'${itemNm }');"></dd>
	                        <dd class="cbtn" onclick="fnCalendar('${itemNm }');"></dd>
	                        </dl>
	                        </div>
	                    </div>
	                </dd>
				</c:if>
				<c:if test="${itemTyp == 'CODE'}">
					<dd class="etr_select" id="${itemNm }Container">
		        	<c:set var="codeNm" value=""/>
		        	<c:set var="codeVa" value=""/>
		            <div class="etr_open1" id="${itemNm }Open" style="display:none">
		                <div class="open_in1">
		                    <div class="open_div">
		                    <dl>
		                    <c:forEach items="${colmVo.cdList}" var="cd" varStatus="status">
		                    <c:if test="${(empty docVal && status.count == 1) || (cd.cdId == docVal)}"><c:set var="codeNm" value="${cd.rescNm }"/><c:set var="codeVa" value="${cd.cdId }"/></c:if>
							<c:if test="${status.count>1 }"><dd class="line"></dd></c:if>
		                    <dd class="txt" onclick="setSelOptions('${itemNm }',$(this).attr('data-code'),$(this).text());" data-code="${cd.cdId}">${cd.rescNm }</dd>
		                    </c:forEach>
		                 </dl>
		                    </div>
		                </div>
		            </div>
		            <m:input type="hidden" id="${itemNm }" name="${itemNm }" value="${codeVa }"/>
		            <div class="etr_ipmany">
		                <div class="select_in1" onclick="holdHide = true; $('#${itemNm }Container #${itemNm }Open').toggle();">
		                <dl>
		                <dd class="select_txt" id="selectView"><span>${codeNm }</span></dd>
		                <dd class="select_btn"></dd>
		                </dl>
		                </div>
		            </div>
		        </dd>
				</c:if>
			</c:forEach>
		</c:if>		
	</dl>
	</div>
