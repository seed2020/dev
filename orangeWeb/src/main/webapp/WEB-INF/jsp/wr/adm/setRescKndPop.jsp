<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<% // [팝업:저장] 테이블 저장 %>
function saveKnd() {
	if (validator.validate('setRescKndForm')) {
		var param = new ParamMap().getData('langTypArea');
		var langs = [];
		$('#langTypOptions select > option').each(function(){
			langs.push($(this).val());
		});
		if(langs.length==0) langs.push('${_lang}');
		param.put('prefix', 'rescVa_');
		param.put('langs', langs.join(','));
		param.put('actKey', 'knd');
		param.put('seqId','${wrRescKndBVo.rescKndId}');
		callAjax('./chkDupRescAjx.do?menuId=${menuId}', param, function(data) {
			if (data.msgList==null) {
				if(confirmMsg("cm.cfrm.save")){
					var $form = $('#setRescKndForm');
					$form.attr('method','post');
					$form.attr('action','./transRescKnd.do?menuId=${menuId}');
					$form.attr('target','dataframe');
					$form[0].submit();
				}else{
					setMessage(data.msgList, false);
				}
			}else setMessage(data.msgList, true);
		});
	}
};

//삭제
function fnKndDelete(rescKndId){
	if(rescKndId == null ) return;
	//$('#rescKndId').val(rescKndId);
	if(confirmMsg("wr.cfrm.rescKnd.del")) {
		var $form = $('#deleteKndForm');
		$form.attr('method','post');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
};

<% // 메세지 처리 %>
function setMessage(msgList, yn){
	var msgBx = $('#setRescKndForm #msgBx');
	msgBx.html('');
	if(msgList!=null){
		var html = '';
		$.each(msgList, function(index, va){
			html+='<div class="red_txt" title="'+escapeValue(va)+'">';
			html+=escapeValue(va);
			html+='</div>';
		});
		if(html!='') msgBx.html(html);
	}
	if(yn) msgBx.show();
	else msgBx.hide();
};

</script>
<div style="width:400px">
<form id="setRescKndForm">
	<u:input type="hidden" name="menuId" value="${menuId}" />
	<u:input type="hidden" id="rescId" value="${wrRescKndBVo.rescId}" />
	<c:if test="${!empty wrRescKndBVo.rescKndId }">
		<u:input type="hidden" id="rescKndId" value="${wrRescKndBVo.rescKndId}" />
	</c:if>
	<u:input type="hidden" id="listPage" value="./listRescKndFrm.do?${paramsForList}" />
	<% // 폼 필드 %>
	<u:listArea>
	<tr>
		<td width="32%" class="head_lt"><u:mandatory /><u:msg titleId="cols.clsNm" alt="분류명" /></td>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tbody>
				<tr>
					<td id="langTypArea">
						<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:convert srcId="${wrRescKndBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
							<u:set test="${status.first}" var="style" value="width:200px;" elseValue="width:200px; display:none" />
							<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
							<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.clsNm" value="${rescVa}" style="${style}"
								maxByte="120" validator="changeLangSelector('setRescKndForm', id, va)" mandatory="Y" />
						</c:forEach>
					</td>
					<td id="langTypOptions">
						<c:if test="${fn:length(_langTypCdListByCompId)>1}">
							<select id="langSelector" onchange="changeLangTypCd('setRescKndForm','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
							<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
							</c:forEach>
							</select>
						</c:if>
					</td>
				</tr>
				</tbody>
			</table>
		</td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
		<td>
			<u:checkArea>
				<u:radio name="useYn" value="Y" titleId="cm.option.use" alt="사용" checkValue="${wrRescKndBVo.useYn }"  inputClass="bodybg_lt" checked="${empty wrRescKndBVo.useYn }"/>
				<u:radio name="useYn" value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${wrRescKndBVo.useYn }" inputClass="bodybg_lt" />
			</u:checkArea>
		</td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="wr.cols.bgCol" alt="배경색상"/></td>
		<td><u:color id="bgcolCd" value="${empty wrRescKndBVo.bgcolCd ? '#7745ff' : wrRescKndBVo.bgcolCd }"/></td>
	</tr>
</u:listArea>
<div id="msgBx" class="ellipsis" style="float:left;display:none;width:62%;height:25px;overflow-y:auto;"></div>
<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="saveKnd();" alt="저장" auth="A" />
	<c:if test="${!empty wrRescKndBVo.rescKndId }"><u:button href="javascript:fnKndDelete('${wrRescKndBVo.rescKndId }');" titleId="cm.btn.del" alt="삭제" auth="A"/></c:if>	
	<u:button href="javascript:void(0)" onclick="dialog.close(this);" alt="닫기" titleId="cm.btn.close" />
</u:buttonArea>

</form>
</div>
<form id="deleteKndForm" name="deleteKndForm" action="./transRescKndDel.do">
	<u:input type="hidden" name="menuId" value="${menuId}" />
	<u:input type="hidden" name="rescKndId"  id="rescKndId" value="${wrRescKndBVo.rescKndId }"/>
	<u:input type="hidden" id="listPage" value="./listRescKndFrm.do?${paramsForList}" />
</form>