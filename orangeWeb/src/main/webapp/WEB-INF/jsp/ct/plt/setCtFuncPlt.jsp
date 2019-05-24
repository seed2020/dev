<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">

	
	function submitForm(){
	
		var $form = $('#setCtFuncForm');	
		$form.attr('method','post');
		$form.attr('action','/ct/plt/transCtFuncPlt.do?menuId=${menuId}');		
		$form.attr('target','dataframe');
		$form[0].submit();		
	}
	
	$(function() {
		
		var evt = document.createEvent('MouseEvents');
		evt.initMouseEvent('click', true, false,  document, 0, 0, 0, 0, 0, false, 
		      false, false, false, 0, null);
		
		$('input:checkbox[name="ctId"]').each(function() {			
			<c:forEach var="ctPltSetupVo" items ="${ctPltsetupList}" varStatus="status">
			if(this.value=='${ctPltSetupVo.ctId}'){
				
				this.dispatchEvent(evt);
				this.checked=true;
			} 
			</c:forEach>
		});

		
	});
	
	

</script>



	<form id="setCtFuncForm" name="setCtFuncForm" >


	<table >
		<tr>
			<td>
				<u:title titleId="ct.jsp.perm.title" alt="기간설정" />
			</td>
			<td >
				<div class="titlearea">
					<div class="tit_left">
					<dl>
						<dd >
						
							<select name="perd" id="perd" valign="absmiddle" value=''>
								
									<option value="7" <c:if test="${perd==7}">selected</c:if>>7<u:msg titleId="wc.option.sun" alt="일." /></option>
									<option value="15" <c:if test="${perd==15}">selected</c:if>>15<u:msg titleId="wc.option.sun" alt="일." /></option>
									<option value="30" <c:if test="${perd==30}">selected</c:if>>30<u:msg titleId="wc.option.sun" alt="일." /></option>
									<option value="60" <c:if test="${perd==60}">selected</c:if>>60<u:msg titleId="wc.option.sun" alt="일." /></option>
									
								
							</select>
						</dd>
					</dl>
					</div>
				</div>
				
			</td>
		</tr>
	</table>
	
	
	
	
	<% // 목록 %>

	<u:input type="hidden" name="ctFncId"  value="${ctFncId}"/>
	<u:listArea>
		
		<tr>
			<td width="50" class="head_ct"><u:msg titleId="cols.no" alt="" /></td>
			<td class="head_ct"><u:msg titleId="ct.jsp.choi" alt="커뮤니티 선택" /></td>
		</tr>
	
		<c:forEach var="ctEstbVo" items ="${myCtVoList}" varStatus="status">
			
			
			<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
				<td class="bodybg_ct"><input type="checkbox" id="ctId" name="ctId" value="${ctEstbVo.ctId}"/></td>
				<td class="body_lt" width="500px">
					<a href="javascript:${viewFunction}"><u:out value="${ctEstbVo.ctNm}" maxLength="80" /></a>
				</td>
			</tr>
		
			
		</c:forEach>
		<c:if test="${fn:length(myCtVoList) == 0}">
				<tr>
					<td class="nodata" colspan="9"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
				</tr>
		</c:if>
		
	</u:listArea>
	</form>
	<u:buttonArea>	
	<u:button titleId="cm.btn.save" onclick="submitForm();" alt="저장" auth="R" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
	</u:buttonArea>
