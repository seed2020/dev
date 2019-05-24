<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function cmplSurvClose(){
	dialog.close('${param.popId}');
}
function survSave(){
	getIframeContent('viewSurvFrm').formSubit();
}
//-->
</script>

<c:if test="${empty wvSurvPopupDVo}">
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
<c:if test="${wvSurvPopupDVo != null}">
<div style="width:${wvSurvPopupDVo.wdthVa}px">
<c:if test="${!empty param.survId }">
<iframe id="viewSurvFrm" name="viewSurvFrm" src="/cm/viewSurvFrm.do?survId=${param.survId }" style="width:100%; height:440px;" frameborder="0" marginheight="0" marginwidth="0" ></iframe>
</c:if>
<u:blank />
<u:buttonArea>
<c:if test="${empty param.previewYn || param.previewYn ne 'Y'}"><u:button titleId="cm.btn.save" alt="저장" href="javascript:survSave();"  /></c:if>
<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</div>	
</c:if>

