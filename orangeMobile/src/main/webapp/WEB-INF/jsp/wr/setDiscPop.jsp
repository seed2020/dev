<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[
<% // [하단버튼:승인,반려] %>
function saveDisc(discStatCd) {
	if(discStatCd == null) return;
	var discCont = $('#discCont').val();
	if ($.trim(discCont) == '') {
		$m.msg.alertMsg('cm.input.check.mandatory','<u:msg titleId="cols.discOpin" alt="심의의견" />');
		$('#discCont').focus();
		return;
	}
	
	var resEmailYn = "N";
	if($("#resEmailYn").attr("class") == "check_on")
		resEmailYn = "Y";

	$m.msg.confirmMsg("cm.cfrm.save", null, function(result){
		if(result){
			$m.ajax('/wr/transRezvDisc.do?menuId=${menuId}', {rezvId:'${wrRezvBVo.rezvId}', discCont:discCont, resEmailYn:resEmailYn, discStatCd:discStatCd, rescMngId:'${wrRezvBVo.rescMngId}'}, function(data) {
				if (data.message != null) {
					$m.dialog.alert(data.message);
				}
				if (data.result == 'ok') {
					var win = $m.nav.getWin();
					if(win==null) return;
					win.goList();
					$m.dialog.close('setDiscPop');
				}
			});
		}
	});
}
//]]>
</script>


          <!--entryzone S-->
            <div class="entryzone view bodyCls">
            	<div class="blank30"></div>
            	
                <c:if test="${discYn eq 'Y' || wrRezvBVo.discStatCd eq 'J' || wrRezvBVo.discStatCd eq 'A' }">
                <div class="entryarea">
                <dl>
                <dd class="etr_input">
                    <div class="etr_ipmany">
                    <dl>
                   	<u:set test="${wrRezvBVo.resEmailYn == 'Y'}" var="checked" value="Y" elseValue="N"/>
                   	<u:set test="${wrRezvBVo.discStatCd eq 'J' || wrRezvBVo.discStatCd eq 'A'}" var="disabled" value="Y" elseValue="N"/>
                   	<u:set var="checked" test="${checked == 'Y' && disabled == 'Y' == 'Y'}" value="true" elseValue="false"/>
                   	<c:choose>
						<c:when test="${mailEnable == 'Y' }"><m:check type="checkbox" id="resEmailYn" name="resEmailYn" inputId="resEmailYn" value="Y" titleId="wr.option.resEmailYn" checked="${checked }" disabled="${disabled }"/></c:when>
						<c:otherwise><dd style="display:none;"><input type="checkbox" name="resEmailYn" id="resEmailYn" value="Y" /></dd></c:otherwise>
					</c:choose>
                    </dl>
                    </div>
                </dd>
	            </dl>
                </div>
                </c:if>     
				<c:if test="${discYn eq 'Y' && listPage eq 'listRezvDisc'}">
                <div class="entryarea">
                <dl>
                <dd class="etr_bodytit"><u:msg titleId="cols.discOpin" alt="심의의견" /></dd>
                <dd class="etr_input"><div class="etr_textareain"><textarea rows="5" class="etr_ta" id="discCont" name="discCont">${wrRezvBVo.discCont}</textarea></div></dd>
                </dl>
                </div>  
				</c:if>     
            </div>
            <!--//entryzone E-->
            
		<div class="blank20"></div>
        <div class="btnarea">
            <div class="size">
            <dl>
            <c:if test="${param.discStatCd == 'A'}">
		       <dd class="btn" onclick="saveDisc('A');"><u:msg titleId="cm.btn.apvd" alt="승인" /></dd>
		    </c:if>  
		    <c:if test="${param.discStatCd == 'J'}">
		       <dd class="btn" onclick="saveDisc('J');"><u:msg titleId="cm.btn.rjt" alt="반려" /></dd>
            </c:if>
            <dd class="btn" onclick="$m.dialog.close('setDiscPop')"><u:msg titleId="cm.btn.close" alt="닫기" /></dd>
         </dl>
            </div>
        </div>

