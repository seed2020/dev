<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--

function popClose(pop) {
	if($('#doNotOpenCheck').is(':checked'))
	{
		callAjax('/cm/transSetViewPopupDisp.do?menuId=${menuId}', {brdId:'${baBrdBVo.brdId}', bullId:'${bbBullLVo.bullId}'}, function(data) {
			/*if (data.message != null) {
				alert(data.message);
			}
			 if (data.result == 'ok') {
				dialog.close(pop);
			} */
		});
	}

	dialog.close(pop);
}
//-->
</script>


<c:if test="${baBullPopupDVo == null}">
	<div style="width:350px">
		<u:listArea style="width:350px">
			<tr>
				<td class="body_ct" >
					<div style="overflow:auto;" class="editor">
							<u:msg titleId="bb.jsp.popup.viewErr" alt="팝업설정 저장 후 사용 가능합니다." />
					</div>
				</td>
			</tr>
		</u:listArea>	
		
		<u:buttonArea>
				<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
		</u:buttonArea>
	</div>
</c:if>
<c:if test="${baBullPopupDVo != null}">
	<div style="width:${baBullPopupDVo.width}px">
	<!-- SNS 사용 --><c:if test="${isSns==true }"><div style="height:30px;"><u:out var="snsText" value="${bbBullLVo.cont}" type="value"/>
<u:sns mode="view" text="${snsText }" snsParams="&brdId=${bbBullLVo.brdId }&bullId=${bbBullLVo.bullId }" urlPrefix="/cm/bb"/></div></c:if>
		<u:listArea style="width:${baBullPopupDVo.width}px">
				<tr>
				<td colspan="2"><div class="editor" style="max-height:550px; overflow:auto;">${bbBullLVo.cont}</div></td>
				</tr>
				<c:if test="${!empty fileVoList }">
					<tr>
						<td width="18%" class="head_lt"><u:msg titleId="cols.attFile" alt="첨부파일" /></td>
						<td>
							<u:files id="${filesId}" fileVoList="${fileVoList}" module="cm/bb" mode="view" />
						</td>
					</tr>
				</c:if>
		</u:listArea>
		
		<c:if test="${baBullPopupDVo.dispYn == 'Y'}">
			<div style="text-align:right;">
				<input type="checkbox" name="doNotOpenCheck" id="doNotOpenCheck"/>
				<label for="doNotOpenCheck"><u:msg titleId="bb.jsp.popup.notToday" alt="오늘하루 열지않음" /></label>
				<u:buttonS titleId="cm.btn.confirm" onclick="popClose(this);" alt="확인" />
			</div>
			<u:blank />
		</c:if>
		
	</div>	
</c:if>

