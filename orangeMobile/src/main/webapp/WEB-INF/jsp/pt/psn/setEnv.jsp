<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[
<%// 숫자만 입력 %>
function numberOnly(event){
	if(event.charCode < 48 || event.charCode > 57){
		if(event.preventDefault) event.preventDefault();
	}
}<%
// 이메일 체크 %>
function isEmail(email){
	if(email==null || email=='') return false;
	var regExp = /([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
	return regExp.test(email);
}<%
// 전화번호 체크 %>
function isPhone(phone){
	if(phone==null || phone=='') return false;
	//var regExp = /^(\()?\d{3}(\))?(-|\s)?\d{3}(-|\s)\d{4}$/;
	var regExp = /^\(\d{3}\)\s*\d{3}(?:-|\s*)\d{4}$/;
	return regExp.test(phone);
}<%
// 파라미터에 전화번호 세팅 %>
function setPhoneNumber(inputs, name, param){
	var arr = [inputs.get(name+'1'), inputs.get(name+'2'), inputs.get(name+'3')];
	if(arr[0]=='' && arr[1]=='' && arr[2]==''){
		param[name] = '';
	} else if(arr[0].length<2 || isNaN(arr[0]) || arr[1].length<3 || isNaN(arr[1]) || arr[2].length<4 || isNaN(arr[2])){
		<%// cm.input.check.notValid="{0}"(은)는 유효하지 않은 값입니다.%>
		$m.msg.alertMsg('cm.input.check.notValid',['#cols.'+name]);
		return false;
	} else {
		param[name] =  arr.join('-');
	}
	return true;
}<%
// 암호화 전송 %>
function sendSecure(param, post){
	var encrypted = null;
	$m.ajax("/cm/login/createSecuSessionAjx.do", null, function(data){
		var key = $m.rsa.getKey(data.e, data.m);
		encrypted = $m.rsa.encrypt(key, JSON.stringify(param.toJSON ? param.toJSON() : param));
	});
	if(encrypted==null) return;
	if(post){
		$m.ajax('/pt/psn/transEnvAjxPost.do?menuId=${menuId}', {secu:encrypted}, function(data){
			var secureResult = data.result == 'ok';
			if(data.message) $m.dialog.alert(data.message, function(){
				if(secureResult) history.back();
			});
		}, {paramAsIs:true, method:'post'});
	} else {
		$m.ajax('/pt/psn/transEnvAjx.do?menuId=${menuId}&secu='+encrypted, null, function(data){
			var secureResult = data.result == 'ok';
			if(data.message) $m.dialog.alert(data.message, function(){
				if(secureResult) history.back();
			});
		});
	}
}<%
// 전송 %>
function sendAjax(param){
	$m.ajax("/pt/psn/transEnvAjx.do?menuId=${menuId}", param, function(data){
		var ajaxResult = (data.result=='ok');
		if(data.message) $m.dialog.alert(data.message, function(){
			if(ajaxResult){
				// 메뉴 아이콘 표시
				var fixedMenuIcon = param['fixedMenuIcon'];
				if(fixedMenuIcon!=null){
					parent.$('#fixedMenuIcon').css('display', fixedMenuIcon=='Y' ? 'block' : 'none');
				}
				if(param.which=='defUserUid'){
					var countArea = parent.$('#countArea');
					if(param['useOdurApvCnt'] == 'Y'){
						countArea.find('#topSchdlCnt').parent().hide();
						countArea.find('#topAdditionalCnt').parent().show();
					} else {
						countArea.find('#topSchdlCnt').parent().show();
						countArea.find('#topAdditionalCnt').parent().hide();
					}
				}
				history.back();
			}
		});
	});
}<%
// 개인정보 저장 %>
function savePsnInfo(){
	var inputs = new ParamMap().getData('psnInfoArea'), param = {which:'psnInfo'};
	
	if(!setPhoneNumber(inputs, "mbno", param)) return;
	if(!setPhoneNumber(inputs, "compPhon", param)) return;
	var extnEmail = inputs.get('extnEmail');
	if(extnEmail=='' || isEmail(extnEmail)) param['extnEmail'] = extnEmail;
	else {<%// cm.input.check.notValid="{0}"(은)는 유효하지 않은 값입니다.%>
		$m.msg.alertMsg('cm.input.check.notValid',['#cols.extnEmail']);
		return;
	}
	param['tichCont'] = inputs.get('tichCont');
	sendSecure(param, true);
}<%
// 비밀번호 저장 - 로그인 / 결재 %>
function savePw(which){
	var inputs = new ParamMap().getData(which+'Area'), param = {which:which};
	if(inputs.get('sysPw')==''){<%// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. %>
		$m.msg.alertMsg('cm.input.check.mandatory',[which=='sysPw' ? '#pt.jsp.setPw.orgPw' : '#pt.jsp.setPw.typeSys']);
		return;
	}
	if(inputs.get('newPw1')==''){<%// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. %>
		$m.msg.alertMsg('cm.input.check.mandatory',['#pt.jsp.setPw.newPw1']);
		return;
	}
	if(inputs.get('newPw2')==''){<%// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. %>
		$m.msg.alertMsg('cm.input.check.mandatory',['#pt.jsp.setPw.newPw2']);
		return;
	}
	if(inputs.get('newPw1')!=inputs.get('newPw2')){<%// pt.jsp.setPw.not.chg2="새로운 비밀번호"와 "새로운 비밀번호 확인"이 같지 않습니다.%>
		$m.msg.alertMsg('pt.jsp.setPw.not.chg2');
		return;
	}
	if(inputs.get('sysPw')==inputs.get('newPw1')){
		if(which=='sysPw'){
			$m.msg.alertMsg('pt.jsp.setPw.not.chg1');<%// pt.jsp.setPw.not.chg1=기존 비밀번호와 동일한 비밀번호로 변경 할 수 없습니다.%>
		} else {
			$m.msg.alertMsg('pt.jsp.setPsnEnv.pw.sameWithLgin');<%// pt.jsp.setPsnEnv.pw.sameWithLgin=결재 비밀번호는 로그인 비밀번호와 같게 설정 할 수 없습니다.%>
		}
		return;
	}
	if(which=='apvPw' && inputs.get('newPw1').length<6){
		$m.msg.alertMsg('cm.input.check.minlength', ['#pt.jsp.setPw.typeApp','6']);<%// cm.input.check.minlength="{0}"(은)는 최소 {1}자리 입력해야 합니다. %>
		return;
	}
	param['sysPw'] = inputs.get('sysPw');
	param['newPw'] = inputs.get('newPw1');
	sendSecure(param);
}<%
// 결재 서명 방법 / 초기 페이지 설정 %>
function saveOption(which){
	var inputs = new ParamMap().getData(which+'Area'), param = {which:which}, value='';
	if(which=='signMthd'){
		value = inputs.get('signMthdCd');
		if(value==null || value==''){<%// cm.select.check.mandatory="{0}"(을)를 선택해 주십시요.%>
			$m.msg.alertMsg('cm.select.check.mandatory',['#mor.label.signMthd']);
			return;
		}
	} else if(which=='initPage'){
		value = inputs.get('initPage');
		if(value==null || value==''){<%// cm.select.check.mandatory="{0}"(을)를 선택해 주십시요.%>
			$m.msg.alertMsg('cm.select.check.mandatory',['#mor.label.initPage']);
			return;
		}
		
		var menuOpenYn = inputs.get('menuOpenYn');
		if(menuOpenYn==null || menuOpenYn=='') menuOpenYn = 'N';
		param['menuOpenYn'] = menuOpenYn;
		
		var fixedMenuIcon = inputs.get('fixedMenuIcon');
		if(fixedMenuIcon==null || fixedMenuIcon=='') fixedMenuIcon = 'N';
		param['fixedMenuIcon'] = fixedMenuIcon;
	} else if(which=='defUserUid'){
		
		value = inputs.get('useOdurApvCnt');
		if(value==null || value=='') value = 'N';
		param['useOdurApvCnt'] = value;
		
		value = inputs.get('defUserUid');
		param['defUserUid'] = value;
	}
	
	param[which] = value;
	sendAjax(param);
}
$(document).ready(function() {
	$("input[type='text'], input[type='password'], textarea").each(function(){
		$space.apply(this, {relative:90});
	});
});
//]]>
</script>
<section>

	<div class="blankzone">
		<div class="blank30"></div>
	</div>

	<div class="entryzone" id="entryzone">
	
<c:if test="${empty blockPnsInfoEnable}">
		<div class="entryarea" id="psnInfoArea">
		<dl>
			<dd class="etr_tit"><u:msg titleId="mor.label.psnInfo" alt="개인정보" /></dd>
			<dd class="etr_bodytit"><u:msg titleId="cols.mbno" alt="휴대전화번호" /></dd>
			<dd class="etr_input">
			<div class="etr_ipmany">
				<dl>
				<dd class="etr_ipmany1"><input name="mbno1" value="${mbno1}" type="text" class="etr_ipct" onkeypress="numberOnly(event)" maxlength="4"/></dd>
				<dd class="etr_ipmany2"> - </dd>
				<dd class="etr_ipmany3"><input name="mbno2" value="${mbno2}" type="text" class="etr_ipct" onkeypress="numberOnly(event)" maxlength="4"/></dd>
				<dd class="etr_ipmany4"> - </dd>
				<dd class="etr_ipmany5"><input name="mbno3" value="${mbno3}" type="text" class="etr_ipct" onkeypress="numberOnly(event)" maxlength="4"/></dd>
				</dl>
			</div>
			</dd>
			<dd class="etr_bodytit"><u:msg titleId="cols.compPhon" alt="회사전화번호" /></dd>
			<dd class="etr_input">
			<div class="etr_ipmany">
				<dl>
				<dd class="etr_ipmany1"><input name="compPhon1" value="${compPhon1}" type="text" class="etr_ipct" onkeypress="numberOnly(event)" maxlength="4"/></dd>
				<dd class="etr_ipmany2"> - </dd>
				<dd class="etr_ipmany3"><input name="compPhon2" value="${compPhon2}" type="text" class="etr_ipct" onkeypress="numberOnly(event)" maxlength="4"/></dd>
				<dd class="etr_ipmany4"> - </dd>
				<dd class="etr_ipmany5"><input name="compPhon3" value="${compPhon3}" type="text" class="etr_ipct" onkeypress="numberOnly(event)" maxlength="4"/></dd>
				</dl>
			</div>
			</dd>
			<dd class="etr_bodytit"><u:msg titleId="cols.extnEmail" alt="외부이메일" /></dd>
			<dd class="etr_input"><div class="etr_inputin"><input name="extnEmail" value="${extnEmail}" type="text" class="etr_iplt" maxlength="30"/></div></dd>
			<dd class="etr_bodytit"><u:msg titleId="or.cols.tich" alt="담당업무" /></dd>
			<dd class="etr_input"><div class="etr_textareain"><textarea name="tichCont" rows="3" class="etr_ta" maxlength="80"><u:out value="${tichCont}" nullValue="" type="textarea"/></textarea></div></dd>
		</dl>
		</div>
		
		<div class="btnarea">
		<div class="blank5"></div>
		<div class="size">
			<dl>
			<dd class="btn" onclick="savePsnInfo()"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
			</dl>
		</div>
		</div>
		
		<div class="blankzone">
			<div class="blank25"></div>
			<div class="line1"></div>
			<div class="line8"></div>
			<div class="line1"></div>
			<div class="blank25"></div>
		</div>
</c:if>
		
		<div class="entryarea" id="sysPwArea">
		<dl>
			<dd class="etr_tit"><u:msg titleId="pt.jsp.setPw.typeSys" alt="로그인 비밀번호" /></dd>
			<dd class="etr_bodytit"><u:msg titleId="pt.jsp.setPw.orgPw" alt="기존 비밀번호" /></dd>
			<dd class="etr_input"><div class="etr_inputin"><input name="sysPw" type="password" class="etr_iplt" maxlength="30"/></div></dd>
			<dd class="etr_bodytit"><u:msg titleId="pt.jsp.setPw.newPw1" alt="새로운 비밀번호" /></dd>
			<dd class="etr_input"><div class="etr_inputin"><input name="newPw1" type="password" class="etr_iplt" maxlength="30"/></div></dd>
			<dd class="etr_bodytit"><u:msg titleId="pt.jsp.setPw.newPw2" alt="새로운 비밀번호 확인" /></dd>
			<dd class="etr_input"><div class="etr_inputin"><input name="newPw2" type="password" class="etr_iplt" maxlength="30"/></div></dd>
		</dl>
		</div>
		
		<div class="btnarea">
		<div class="blank5"></div>
		<div class="size">
			<dl>
			<dd class="btn" onclick="savePw('sysPw')"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
			</dl>
		</div>
		</div>
		
		<div class="blankzone">
			<div class="blank25"></div>
			<div class="line1"></div>
			<div class="line8"></div>
			<div class="line1"></div>
			<div class="blank25"></div>
		</div>
		
		
		
		<div class="entryarea" id="apvPwArea">
		<dl>
			<dd class="etr_tit"><u:msg titleId="pt.jsp.setPw.typeApp" alt="결재 비밀번호" /></dd>
			<dd class="etr_bodytit"><u:msg titleId="pt.jsp.setPw.typeSys" alt="로그인 비밀번호" /></dd>
			<dd class="etr_input"><div class="etr_inputin"><input name="sysPw" type="password" class="etr_iplt" maxlength="30"/></div></dd>
			<dd class="etr_bodytit"><u:msg titleId="pt.jsp.setPw.newPw1" alt="새로운 비밀번호" /></dd>
			<dd class="etr_input"><div class="etr_inputin"><input name="newPw1" type="password" class="etr_iplt" maxlength="30"/></div></dd>
			<dd class="etr_bodytit"><u:msg titleId="pt.jsp.setPw.newPw2" alt="새로운 비밀번호 확인" /></dd>
			<dd class="etr_input"><div class="etr_inputin"><input name="newPw2" type="password" class="etr_iplt" maxlength="30"/></div></dd>
		</dl>
		</div>
		
		<div class="btnarea">
		<div class="blank5"></div>
		<div class="size">
			<dl>
			<dd class="btn" onclick="savePw('apvPw')"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
			</dl>
		</div>
		</div>
		
		<div class="blankzone">
			<div class="blank25"></div>
			<div class="line1"></div>
			<div class="line8"></div>
			<div class="line1"></div>
			<div class="blank25"></div>
		</div>
		
		
		
		<div class="entryarea">
		<dl>
			<dd class="etr_tit"><u:msg titleId="mor.label.signMthd" alt="결재 서명 방법"/></dd>
			<dd class="etr_input" style="padding-top:5px;" id="signMthdArea"><c:if
				test="${optConfigMap.signAreaSign == 'psn'}"
				><u:set test="${not empty signMthdCd}" var="signAreaSign" value="${signMthdCd}" elseValue="${deftSignMthdCd}"
				/></c:if><c:if
				test="${optConfigMap.signAreaSign != 'psn'}"
				><u:set test="${not empty optConfigMap.signAreaSign}" var="signAreaSign" value="${optConfigMap.signAreaSign}" elseValue="${deftSignMthdCd}"
				/></c:if><c:forEach
					items="${signMthdCdList}" var="signMthdCd" varStatus="status">
			<div class="etr_ipmany">
				<dl>
				<m:check name="signMthdCd" value="${signMthdCd.cd}" title="${signMthdCd.rescNm}" type="radio" areaId="signMthdArea"
					checked="${signMthdCd.cd == signAreaSign}" disabled="${optConfigMap.signAreaSign != 'psn'}" />
				</dl>
			</div></c:forEach>
			</dd>
		</dl>
		</div>
		
		<c:if
				test="${optConfigMap.signAreaSign == 'psn'}">
		<div class="btnarea">
		<div class="blank5"></div>
		<div class="size">
			<dl>
			<dd class="btn" onclick="saveOption('signMthd')"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
			</dl>
		</div>
		</div></c:if>
		
		<div class="blankzone">
			<div class="blank25"></div>
			<div class="line1"></div>
			<div class="line8"></div>
			<div class="line1"></div>
			<div class="blank25"></div>
		</div>
		
		
		
		<div class="entryarea">
		<dl>
		
			<dd class="etr_tit"><u:msg titleId="mor.label.initPage" alt="초기 페이지 " /></dd>
			<dd class="etr_input" style="padding-top:5px;" id="initPageArea"><c:forEach
				items="${mobileList}" var="ptMnuLoutDVo" varStatus="status"><c:if
					test="${ptMnuLoutDVo.mnuGrpTypCd != '11' and ptMnuLoutDVo.mnuGrpTypCd != '12'}">
			<div class="etr_ipmany">
				<dl>
				<m:check name="initPage" value="${ptMnuLoutDVo.mnuLoutId}" title="${ptMnuLoutDVo.rescNm}" type="radio" areaId="initPageArea"
					checked="${ptMnuLoutDVo.mnuLoutId == loginMap.initPage}" />
				</dl>
			</div></c:if></c:forEach>
			
			<div class="blank15"></div>
			<div class="etr_ipmany">
				<dl>
				<m:check name="menuOpenYn" id="menuOpenYn" value="Y" titleId="mpt.label.menuOpenYn" alt="로그인 후 메뉴 열기" type="check"
					checked="${loginMap.menuOpenYn == 'Y'}" />
				</dl>
			</div>
			<div class="blank1"></div>
			<div class="etr_ipmany">
				<dl>
				<m:check name="fixedMenuIcon" id="fixedMenuIcon" value="Y" titleId="mpt.label.fixedMenuIcon" alt="메뉴 아이콘 표시" type="check"
					checked="${loginMap.fixedMenuIcon == 'Y'}" />
				</dl>
			</div>
			
			</dd>
		</dl>
		</div>
		
		<div class="btnarea">
		<div class="blank5"></div>
		<div class="size">
			<dl>
			<dd class="btn" onclick="saveOption('initPage')"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
			</dl>
		</div>
		</div>
		
<c:if test="${not empty sessionScope.userVo.adurs}">
		<div class="blankzone">
			<div class="blank25"></div>
			<div class="line1"></div>
			<div class="line8"></div>
			<div class="line1"></div>
			<div class="blank25"></div>
		</div>
		
		
		
		<div class="entryarea">
		<dl>
		
			<dd class="etr_tit"><u:msg titleId="pt.jsp.setInitUser" alt="로그인 사용자 선택" /></dd>
			<dd class="etr_input" style="padding-top:5px;" id="defUserUidArea"><c:forEach
				items="${sessionScope.userVo.adurs}" var="adurs" varStatus="status">
			<div class="etr_ipmany">
				<dl>
				<m:check name="defUserUid" value="${adurs[1]}" title="${adurs[0]}" type="radio" areaId="defUserUidArea"
					checked="${defUserUid == adurs[1]}" />
				</dl>
			</div></c:forEach>
			<c:if
			
				test="${empty adurMergLst}">
			<div class="blank15"></div>
			<div class="etr_ipmany">
				<dl>
				<m:check name="useOdurApvCnt" id="useOdurApvCnt" value="Y" titleId="mpt.label.useOdurApvCnt" alt="일정 대신 겸직 결재 표시" type="check"
					checked="${useOdurApvCnt == 'Y'}" />
				</dl>
			</div>
			</c:if>
			</dd>
		</dl>
		</div>
		
		<div class="btnarea">
		<div class="blank5"></div>
		<div class="size">
			<dl>
			<dd class="btn" onclick="saveOption('defUserUid')"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
			</dl>
		</div>
		</div>
</c:if>
	</div>

	<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
</section>