<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><script type="text/javascript" src="${_cxPth}/js/validator.js" charset="UTF-8"></script>
<script type="text/javascript">
<!--<%// [선택] 수동입력 %>
function docNoMnalYnChk(){
	$docNo = $('#setCfrmForm').find("[name='docNo']");
	$docNo.attr('disabled',$('#setCfrmForm').find('input[name="docNoMnalYn"]').is(':checked'));
}<%// [선택] 버전 %>
function verSelect(obj, verVa){
	if(obj == 'mnal' ){
		$('#setCfrmForm #verVa').val('${dmDocLVo.verVa}');
		$('#setCfrmForm #verVaTr').show();
	}else{
		$('#setCfrmForm #verVa').val(verVa);
		$('#setCfrmForm #verVaTr').hide();
	}
}<%// [확인] 저장 %>
function applyCfrm(){
	if(!validator.validate('setCfrmForm')) return;
	var arr=[];
	var isChk = true;
	$('#setCfrmForm').find("input[type='text'],input[type='radio']:checked").each(function(){
		if($(this).attr('disabled')!='disabled'){
			arr.push({name:$(this).attr('name'),value:$(this).val()});
		}
	});
	if(arr.length==0){
		return null;
	}
	if(!isChk) return;
	var win = $m.nav.getWin();
	win.saveCfrmOk(arr);
	$m.dialog.close('setCfrmDialog');
}
$(document).ready(function() {
});
//-->
</script>
<form id="setCfrmForm">
<!--entryzone S-->
<div class="entryzone">
    <div class="entryarea" id="cfrmArea">
    <dl>
    	<c:choose>
			<c:when test="${!empty param.docId && (dmDocLVo.statCd eq 'C' || dmDocLVo.statCd eq 'M')}">
				<c:if test="${!empty param.docNoMod }">
					<dd class="etr_bodytit"><u:msg titleId="dm.cols.docNo" alt="문서번호" /></dd>
			    	<dd class="etr_input">
			        <div class="etr_ipmany">
						<dl>
							<dd class="etr_ip_lt">
								<div class="ip_txt">
									<m:input type="text" id="docNo" titleId="dm.cols.docNo" value="${dmDocLVo.docNo }" mandatory="Y" maxByte="55"/>
								</div>
								<div class="ip_delete" onclick="$('#docNo').val('');"></div>
							</dd>
						</dl>
					   </div>
					</dd>
				</c:if>
				<dd class="etr_bodytit"><u:msg titleId="dm.cols.verNo" alt="버전번호" /></dd>
                <u:set var="verFrtTmp" test="${dmDocLVo.verVa%1 == 0}" value="${dmDocLVo.verVa+0.1 }" elseValue="${dmDocLVo.verVa }"/>
				<fmt:formatNumber var="verDft" value="${dmDocLVo.verVa}" type="pattern" pattern="0.0" />
				<fmt:formatNumber var="verFrt" value="${verFrtTmp+(1-(verFrtTmp%1))%1}" type="pattern" pattern="0.0" />
				<fmt:formatNumber var="verRear" value="${dmDocLVo.verVa+envConfigMap.verRear}" type="pattern" pattern="0.0" />
				<u:msg var="verDftTitle" titleId="dm.msg.serDoc.verDft" arguments="${verDft}"/>
				<u:msg var="verFrtTitle" titleId="dm.msg.serDoc.verFrt" arguments="${verFrt }"/>
				<u:msg var="verRearTitle" titleId="dm.msg.serDoc.verRear" arguments="${verRear }"/>
				<m:checkArea><m:check type="radio" id="verVaChkCurr" name="verVaChk" inputId="verVaChkCurr" value="curr" title="${verDftTitle }" areaId="cfrmArea" onclick="verSelect('curr','${verDft }');" checked="true"/></m:checkArea>
				<m:checkArea><m:check type="radio" id="verVaChkFrt" name="verVaChk" inputId="verVaChkFrt" value="frt" title="${verFrtTitle }" areaId="cfrmArea" onclick="verSelect('frt','${verFrt }');"/></m:checkArea>
				<m:checkArea><m:check type="radio" id="verVaChkRear" name="verVaChk" inputId="verVaChkRear" value="rear" title="${verRearTitle }" areaId="cfrmArea" onclick="verSelect('rear','${verRear }');"/></m:checkArea>
				<c:if test="${envConfigMap.verMnalYn eq 'Y' }">
					<m:checkArea><m:check type="radio" id="verVaChkMnal" name="verVaChk" inputId="verVaChkMnal" value="mnal" titleId="dm.cfg.verMnalYn" areaId="cfrmArea" onclick="verSelect('mnal');" /></m:checkArea>
				</c:if>
				<dd class="etr_input" ><div class="etr_ipmany" ><dl><dd class="etr_ip_lt" id="verVaTr" style="display:none;"><div class="ip_txt"
				><m:input type="text" id="verVa" titleId="dm.cfg.verNo" value="${dmDocLVo.verVa }" className="etr_iplt" mandatory="Y" valueOption="number" valueAllowed="." readonly="${envConfigMap.verMnalYn eq 'Y' ? 'N' : 'Y'}"  onblur="isChkFloat(this);" maxLength="5"/>
				<%-- <input type="text" name="verVa" id="verVa" class="etr_iplt" value="${dmDocLVo.verVa }" onblur="isChkFloat(this);" <c:if test="${envConfigMap.verMnalYn ne 'Y' }" >readonly="readonly"</c:if>/> --%></div
				><div class="ip_delete" onclick="$('#verVa').val('');"></div></dd></dl></div></dd>
			</c:when>
			<c:otherwise>
				<dd class="etr_bodytit"><u:msg titleId="dm.cols.docNo" alt="문서번호" /></dd>
				<dd class="etr_input">
			        <div class="etr_ipmany">
						<dl>
							<dd class="etr_ip_lt">
								<div class="ip_txt">
									<input type="text" name="docNo" id="docNo" class="etr_iplt" value="${dmDocLVo.docNo }" maxByte="55" disabled="disabled"/>
								</div>
								<div class="ip_delete" onclick="$('#docNo').val('');"></div>
							</dd>
							<c:if test="${envConfigMap.docNoMnalYn eq 'Y' }">
							<dd class="etr_se_rt" >
								<div class="etr_ipmany">
								<dl>
								<dd class="wblank5"></dd>
								<m:check type="checkbox" id="docNoMnalYnArea" name="docNoMnalYn" inputId="docNoMnalYnChk" value="Y" titleId="dm.cfg.docNoMnalYn" onclick="docNoMnalYnChk();"/>
								</dl>
								</div>
							</dd>
							</c:if>
						</dl>
					   </div>
					</dd>
				<dd class="etr_bodytit"><u:msg titleId="dm.cols.verNo" alt="버전번호" /></dd>
				<dd class="etr_input">
			        <div class="etr_ipmany">
						<dl>
							<dd class="etr_ip_lt">
								<div class="ip_txt">
									<m:input type="text" id="verVa" titleId="dm.cfg.verNo" value="${dmDocLVo.verVa }" className="etr_iplt" mandatory="Y" valueOption="number" valueAllowed="." readonly="${envConfigMap.verMnalYn eq 'Y' ? 'N' : 'Y'}"  onblur="isChkFloat(this);" maxLength="5"/>
								</div>
								<div class="ip_delete" onclick="$('#verVa').val('');"></div>
							</dd>
						</dl>
					   </div>
					</dd>
			</c:otherwise>
		</c:choose>
    </dl>
    </div>
</div>
<div class="popbtnarea">
<div class="btnarea">
	<div class="size">
	<dl>
		<u:secu auth="W"><dd class="btn" onclick="applyCfrm();"><u:msg titleId="cm.btn.confirm" alt="확인" /></dd></u:secu>
		<dd class="btn" onclick="$m.dialog.close('setCfrmDialog');"><u:msg titleId="cm.btn.close" alt="닫기" /></dd>
	</dl>
	</div>
</div>
</div>
</form>
