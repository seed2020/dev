<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
String apvLnOpt = request.getParameter("apvLnOpt");
if(apvLnOpt!=null){
	if("agr".equals(apvLnOpt)){
		request.setAttribute("makRole", "makAgr");
		request.setAttribute("byOneRole", "byOneAgr");
	} else if("makVw".equals(apvLnOpt) || "vw".equals(apvLnOpt)){
		request.setAttribute("makRole", "makVw");
		request.setAttribute("byOneRole", "byOne");
	} else {
		request.setAttribute("makRole", "mak");
		request.setAttribute("byOneRole", "byOne");
	}
}
%>
<script type="text/javascript">
//<![CDATA[<%
// 결재 옵션 - json %>
var gOptConfig = ${optConfigJson};<%
// 양식결재라인구분코드 %>
var gFormApvLnTypCd = "${param.formApvLnTypCd}";<%
//용어설정 목록 %>
var apTermResc = {};<c:forEach items="${apTermList}" var="apTerm">
apTermResc["${apTerm[0]}"] = "<u:out value="${apTerm[1]}" type="script" />";</c:forEach>
apTermResc["byOneAgr"] = apTermResc["byOne"];
var absRsonList = [];<c:forEach items="${absRsonCdList}" var="absRsonCd">
absRsonList.push({cd:"${absRsonCd.cd}",nm:"<u:out value="${absRsonCd.rescNm}" type="script" />"});</c:forEach>
<%
// 결재용 사용자 데이터로 전환 %>
function toApvObj(obj, opt){
	if(opt.type=='user'){
		var apvr = {};
		apvr["apvrRoleCd"] = opt.apvrRoleCd;<%// 결재구분 - 결재자역할코드 %>
		apvr["apvrRoleNm"] = apTermResc[opt.apvrRoleCd];
		apvr["dblApvTypCd"] = opt.dblApvTypCd;<%// 이중결재 : 신청부서 / 주관부서%>
		if(opt.dblApvTypCd != '') apvr["dblApvTypNm"] = apTermResc[opt.dblApvTypCd];
		apvr["apvrUid"] = obj.userUid;<%// 사용자UID%>
		apvr["apvrNm"] = obj.rescNm;<%// 사용자명%>
		apvr["apvrPositNm"] = obj.positNm==null ? "" : obj.positNm;<%// 직위%>
		apvr["apvrTitleNm"] = obj.titleNm==null ? "" : obj.titleNm;<%// 직책%>
		apvr["apvDeptId"] = obj.deptId;<%//부서ID%>
		apvr["apvDeptNm"] = obj.deptRescNm==null ? "" : obj.deptRescNm;<%//부서명%>
		apvr["apvDeptAbbrNm"] = "";<%//부서약어명%>
		apvr["absRsonCd"] = opt.absRsonCd==null ? "" : opt.absRsonCd;<%//부재사유코드%>
		apvr["absRsonNm"] = opt.absRsonNm==null ? "" : opt.absRsonNm;<%//부재사유명%>
		apvr["apvrDeptYn"] = "N";<%//결재자부서여부%>
		apvr["userUid"] = obj.userUid;
		apvr["rescNm"] = obj.rescNm;
		return apvr;
	} else if(opt.type=='dept'){
		apvr = {};
		apvr["apvrRoleCd"] = opt.apvrRoleCd;<%// 결재구분 - 결재자역할코드 %>
		apvr["apvrRoleNm"] = apTermResc[opt.apvrRoleCd];
		apvr["dblApvTypCd"] = opt.dblApvTypCd;<%// 이중결재 : 신청부서 / 주관부서%>
		apvr["apvrUid"] = "";<%// 사용자UID%>
		apvr["apvrNm"] = "";<%// 사용자명%>
		apvr["apvrPositNm"] = "";<%// 직위%>
		apvr["apvrTitleNm"] = "";<%// 직책%>
		apvr["apvDeptId"] = obj.deptId;<%//부서ID%>
		apvr["apvDeptNm"] = obj.rescNm;<%//부서명%>
		apvr["apvDeptAbbrNm"] = opt.orgAbbrRescNm==null ? "" : opt.orgAbbrRescNm;<%//부서약어명%>
		apvr["absRsonCd"] = "";<%//부재사유코드%>
		apvr["absRsonNm"] = "";<%//부재사유명%>
		apvr["apvrDeptYn"] = "Y";<%//결재자부서여부%>
		apvr["deptId"] = obj.deptId;
		apvr["rescNm"] = obj.rescNm;
		return apvr;
	}
}<%
// 결재 사용자 추가 %>
function addApvrToSelected(arr, type, dblApvTypCd){
	if(arr==null || arr.length==0) return true;<%
	// 결재 본문에서 넘겨온 데이터 세팅할때 %>
	if(type=='init'){
		addSelectedApvr(arr);<%
	// 사용자를 선택 한뒤 [+] 버튼 클릭 했을 때 %>
	} else if(type=='txt'){
		
		var aleadyHas, aleadys = getSelected(), obj, objArr = [], dupNms=[];
		arr.each(function(index, txt){
			obj = JSON.parse(txt);
			aleadyHas = false;
			aleadys.each(function(index, oldItem){
				if(obj.userUid==null){
					if(oldItem.apvDeptId==obj.deptId){
						aleadyHas = true;
						return false;
					}
				} else {
					if(oldItem.apvrUid==obj.userUid){
						aleadyHas = true;
						return false;
					}
				}
			});
			if(!aleadyHas){
				objArr.push(obj);
			} else {
				dupNms.push(obj.rescNm);
			}
		});
		if(objArr.length == 0){<%
			// map.apvLn.msg.dupRemoved=중복된 사용자는 추가 되지 않습니다.({0}) %>
			$m.msg.alertMsg('map.apvLn.msg.dupRemoved', [dupNms.join(', ')]);
			return false;
		}
		////////////////
		//TODO
		$m.dialog.openSelect({id:'roleCd', cdList:getApvLnTypList()}, function(role){
			if(role!=null){
				var opt = {};
				//TODO
				opt["type"] = 'user';
				opt["dblApvTypCd"] = dblApvTypCd==null ? '' : dblApvTypCd;
				
				if(role.cd.startsWith('abs')){
					opt["apvrRoleCd"] = 'abs';
					opt["apvrRoleNm"] = apTermResc['abs'];
					opt["absRsonCd"] = role.cd.substring(4);
					opt["absRsonNm"] = role.nm.substring(role.nm.indexOf('-')+2);
				} else {
					opt["apvrRoleCd"] = role.cd;
					opt["apvrRoleNm"] = apTermResc[role.cd];
					opt["absRsonCd"] = '';
					opt["absRsonNm"] = '';
				}
				addSelectedApvr(objArr, opt);
			}
		});
	}
	return true;
}<%
// 하단 사용자 모음에 더하기 %>
function addSelectedApvr(objArr, opt, withClear){
	var area = getSelectedListArea();
	if(withClear==true) area.html('');
	var empty = area.find('dd:first').length==0, apvr, txt, dispNm, stat, onTouch;
	objArr.each(function(index, obj){
		if(opt==null){
			if(obj.apvrRoleNm==null) obj.apvrRoleNm = apTermResc[obj.apvrRoleCd];
			if(obj.dblApvTypCd!='' && obj.dblApvTypNm==null) obj.dblApvTypNm = apTermResc[obj.dblApvTypCd];
			apvr = obj;
		} else {
			apvr = toApvObj(obj, opt);
		}
		txt = JSON.stringify(apvr);
		txt = txt.replace(/\'/g,"&apos;");
		
		if(index!=0 || !empty){
			area.append('<dd class="blank">l</dd>');
		}
		dispNm = (apvr.apvrNm==null || apvr.apvrNm=='') ? apvr.apvDeptNm : apvr.apvrNm;
		stat = apvr.apvStatCd;<% // befoApv:결재전, befoAgr:합의전, befoVw:공람전 %>
		if(stat==null || stat=='' || stat=='befoApv' || stat=='befoAgr' || stat=='befoVw'){
			onTouch = 'highlightAp(this);';
		} else if(stat=='apvd' || stat=='rejt' || stat=='cons' || stat=='pros' || stat=='cmplVw'){<%// apvd:승인, rejt:반려, cons:반대, pros:찬성, cmplVw:공람완료, %>
			onTouch = 'highlightAp(null);';
		} else {
			onTouch = 'highlightAp(this, true);';
		}
		area.append('<dd class="txt" onclick="'+onTouch+'" data-org=\''+txt+'\'>'+dispNm+'</dd>');
	});
}<%
// 결재 라인별 결재 라인 옵션 목록 %>
function getApvLnTypList(optRole){
	var arr=[], str;
	if(optRole == 'makAgr'){
		str = "makAgr,byOneAgr";
	} else if(optRole == 'mak'){
		str = "mak,byOne";
	} else if(optRole == 'makVw'){
		str = "makVw";
	} else if(optRole != null){
		str = "revw,apv,pred";
	} else if(apvLnOpt=='agr'){
		str = "revw,apv,pred,entu,abs";
	} else if(apvLnOpt=='makVw'){
		str = "fstVw,pubVw,paralPubVw";
	} else if(apvLnOpt=='reqDept' || apvLnOpt=='prcDept'){
		str = "revw,apv,pred,entu,postApvd,infm,abs";
	} else {
		return null;
	}
	str.split(",").each(function(index, role){
		if(role!='abs'){
			arr.push({cd:role,nm:apTermResc[role]});
		} else {
			var roleCd = role+'-';
			var roleNm = apTermResc[role]+' - ';
			absRsonList.each(function(index, abs){
				arr.push({cd:roleCd+abs.cd, nm:roleNm+abs.nm});
			});
		}
	});
	return arr;
}<%
// 결재용 터치 이벤트 처리 %>
var dblClickObj = null;
var dblClickTime = null;
function highlightAp(obj, isCurrApvr){
	highlightTouched(obj, isCurrApvr);
	var now = new Date().getTime();
	if(obj == dblClickObj){
		if(now - dblClickTime < 800){
			var txt = $(obj).attr('data-org');
			var apvr = JSON.parse(txt);
			var role = apvr.apvrRoleCd;
			var optRole = null;
			if(isCurrApvr){
				if(role=='byOneAgr'||role=='makAgr') optRole = 'makAgr';
				else if(role=='byOne'||role=='mak') optRole = 'mak';
				else optRole = role;
			}
			var selected = apvr.apvrRoleCd;
			if(selected=='abs') selected = 'abs-'+apvr.absRsonCd;
			$m.dialog.openSelect({id:'roleCd', cdList:getApvLnTypList(optRole), selected:selected}, function(role){
				if(role!=null){
					if(role.cd.startsWith('abs')){
						apvr.apvrRoleCd = 'abs';
						apvr.apvrRoleNm = apTermResc[apvr.apvrRoleCd];
						apvr.absRsonCd = role.cd.substring(4);
						apvr.absRsonNm = role.nm.substring(role.nm.indexOf('-')+2);
					} else {
						apvr.apvrRoleCd = role.cd;
						apvr.apvrRoleNm = apTermResc[apvr.apvrRoleCd];
						apvr.absRsonCd = '';
						apvr.absRsonNm = '';
					}
					$(dblClickObj).attr('data-org', JSON.stringify(apvr));
				}
			});
		}
	} else {
		dblClickObj = obj;
	}
	dblClickTime = now;
}<%
// 확인 버튼 %>
function setApvLn(){
	if(addApvrToSelected()==false) return;
	var arr = getSelected();
	if(!validateConfirm(arr)) return;
	if($m.user.callback(arr.length==0 ? null : arr) != false){
		history.back();
	}
}<%
// 상세보기 버튼 %>
function setApvLnDetl(apvList){
	if(apvList==null) apvList = getSelected();
	
	var html = [];
	html.push('<div class="blankzone"><div class="blank25"></div></div>');
	
	<%// 스크롤 영역 %>
	html.push('<div class="unified_listarea_pop">');
	html.push('<div class="listarea">');
	html.push('<article id="apvLnListArea">');
	
	var i, size = apvList.length;
	var apvr, txt, role, stat, posit, enabled, userClick;
	for(i=0; i<size; i++){
		apvr = apvList[i];
		txt = JSON.stringify(apvr);
		role = apvr.apvrRoleCd;
		stat = apvr.apvStatCd;<%// befoApv:결재전, befoAgr:합의전, befoVw:공람전 %>
		posit = apvr.positNm;

		if(stat==null || stat=='' || stat=='befoApv' || stat=='befoAgr' || stat=='befoVw'){
			enabled = true;
		} else {
			enabled = false;
		}
		txt = txt.replace(/\'/g,"&apos;");
		
		html.push('	<div class="listdiv_fixed" data-fixed="'+(enabled ? 'N' : 'Y')+'" data-org=\''+txt+'\'>');
		html.push('		<div class="listcheck_fixed">');
		html.push('		<dl>');
		if(enabled){
			html.push('		<dd class="check" onclick="$m.nav.getWin().popCheckClick(this);"></dd>');
		} else {
			html.push('		<dd class="check_disabled"></dd>');
		}
		html.push('		</dl>');
		html.push('		</div>');
		
		if(enabled){
			userClick = '$m.nav.getWin().popUserClick(this)';
		} else if(stat=='inApv'||stat=='inAgr'||stat=='hold'||stat=='cncl'||stat=='reRevw'){
			userClick = "$m.nav.getWin().popUserClick(this, true)";
		} else {
			userClick = '';
		}
		
		html.push('		<div class="list_fixed" onclick="'+userClick+';">');
		html.push('		<dl>');
		if(apvr.apvrUid!=null && apvr.apvrUid!=''){
			html.push('		<dd class="tit">'+apvr.apvrNm+' / '+apvr.apvDeptNm+(posit!=null && posit!='' ? ' / '+posit : '')+'</dd>');
			if(apvLnOpt=='reqDept' || apvLnOpt=='prcDept'){
				html.push('		<dd class="body">'+'['+apTermResc[apvr.dblApvTypCd]+'] '+apTermResc[role]+(role=='abs' ? ' ['+apvr.absRsonNm+']' : '')+'</dd>');
			} else {
				html.push('		<dd class="body">'+apTermResc[role]+(role=='abs' ? ' ['+apvr.absRsonNm+']' : '')+'</dd>');
			}
		} else {
			html.push('		<dd class="tit">'+apvr.apvDeptNm+' / '+apTermResc[role]+'</dd>');
		}
		
		html.push('		</dl>');
		html.push('		</div>');
		html.push('	</div>');
	}
	
	html.push('</article>');
	html.push('</div>');
	html.push('</div>');
	
	<%// 버튼 영역 %>
	html.push('<div class="unified_btn_pop">');
	html.push('<div class="btnarea">');
	html.push('<div class="size">');
	html.push('	<dl>');
	html.push('	<dd class="up" onclick="$m.nav.getWin().popMoveClick($(\'#apvLnListArea\'), \'up\')"></dd>');
	html.push('	<dd class="down" onclick="$m.nav.getWin().popMoveClick($(\'#apvLnListArea\'), \'down\')"></dd>');
	html.push('	<dd class="btn" onclick="$m.nav.getWin().popDeleteClick($(\'#apvLnListArea\'))"><u:msg titleId="cm.btn.del" alt="삭제"/></dd>');
	html.push('	<dd class="btn" onclick="$m.nav.getWin().popApplyClick($(\'#apvLnListArea\'))"><u:msg titleId="cm.btn.apply" alt="적용"/></dd>');
	html.push('	</dl>');
	html.push('</div>');
	html.push('</div>');
	html.push('</div>');
	
	$m.dialog.open({
		id:'apvLnDetl',
		title:'<u:msg titleId="map.apvLn.detlTitl" alt="결재 경로 상세 설정" />',
		html:html.join('\n')
	});
}<%
// [팝업:결재 경로 상세 설정] - check 클릭  %>
function popCheckClick(obj){
	var clsNm = $(obj).attr('class');
	if(clsNm.endsWith('_on')){
		$(obj).attr('class', clsNm.substring(0, clsNm.length-3));
	} else {
		$(obj).attr('class', clsNm+'_on');
	}
}<%
// [팝업:결재 경로 상세 설정] - 사용자 클릭 %>
function popUserClick(popClickObj, isCurrApvr){
	var attrObj = $(popClickObj).prev().parent();
	var apvr = JSON.parse(attrObj.attr('data-org'));
	var selected = apvr.apvrRoleCd;
	if(selected=='abs') selected = 'abs-'+apvr.absRsonCd;
	var optRole = null, role = apvr.apvrRoleCd;
	if(isCurrApvr){
		if(role=='byOneAgr'||role=='makAgr') optRole = 'makAgr';
		else if(role=='byOne'||role=='mak') optRole = 'mak';
		else optRole = role;
	}
	$m.dialog.openSelect({id:'roleCd', cdList:getApvLnTypList(optRole), selected:selected}, function(role){
		if(role!=null){
			if(role.cd.startsWith('abs')){
				apvr.apvrRoleCd = 'abs';
				apvr.apvrRoleNm = apTermResc[apvr.apvrRoleCd];
				apvr.absRsonCd = role.cd.substring(4);
				apvr.absRsonNm = role.nm.substring(role.nm.indexOf('-')+2);
				role.cd = apvr.apvrRoleCd;
			} else {
				apvr.apvrRoleCd = role.cd;
				apvr.apvrRoleNm = apTermResc[apvr.apvrRoleCd];
				apvr.absRsonCd = '';
				apvr.absRsonNm = '';
			}
			$(attrObj).attr('data-org', JSON.stringify(apvr));
			if(apvr.dblApvTypCd!=''){
				$(popClickObj).find('.body').text('['+apTermResc[apvr.dblApvTypCd]+'] '+apTermResc[role.cd]+(role.cd=='abs' ? ' ['+apvr.absRsonNm+']' : ''));
			} else {
				$(popClickObj).find('.body').text(apTermResc[role.cd]+(role.cd=='abs' ? ' ['+apvr.absRsonNm+']' : ''));
			}
			
		}
	});
}<%
// [팝업:결재 경로 상세 설정] - up, down 클릭 %>
function popMoveClick($area, direction){
	var i, arr = popGetCheckedArea($area), size = arr.length, curr, prev, next;
	if(size==0) return;
	if(direction=='up'){
		for(i=0;i<size;i++){
			curr = arr[i];
			prev = curr.prev();
			if(prev.length==0) continue;
			if(prev.attr('data-fixed')=='Y') continue;
			if(i!=0 && arr[i-1][0]==prev[0]) continue;
			curr.insertBefore(prev);
		}
	} else if(direction=='down'){
		arr = arr.reverse();
		for(i=0;i<size;i++){
			curr = arr[i];
			next = curr.next();
			if(next.length==0) continue;
			if(i!=0 && arr[i-1][0]==next[0]) continue;
			curr.insertAfter(next);
		}
	}
}<%
// [팝업:결재 경로 상세 설정] - 삭제 클릭 %>
function popDeleteClick($area){
	var arr = popGetCheckedArea($area);
	arr.each(function(index, obj){
		obj.remove();
	});
}<%
// [팝업:결재 경로 상세 설정] - 적용 클릭 %>
function popApplyClick($area){
	var apvrList = [];
	$area.children().each(function(){
		apvrList.push(JSON.parse($(this).attr('data-org')));
	});
	if(validateConfirm(apvrList)){
		addSelectedApvr(apvrList, null, true);
		$m.dialog.close('apvLnDetl');
	}
}<%
// [팝업:결재 경로 상세 설정] - 체크된 것의 영역 Element 배열 가져오기 %>
function popGetCheckedArea($area){
	var arr = [];
	$area.children().children('.listcheck_fixed').find('.check_on').each(function(){
		arr.push($(this).parent().parent().parent());
	});
	return arr;
}<%
// 결재경로 유효성 검증 %>
function validateConfirm(apvrList){<%
	// 사용자, 부서별로 나누어 담음 %>
	var users=[], depts=[];
	apvrList.each(function(index, apvr){
		if(apvr.apvrDeptYn == 'Y') depts.push(apvr);
		else users.push(apvr);
	});<%
	
	// 동일결재자/부서 체크용 배열
	//    apvrs:결재(검토~사후보고), infms:통보, agrs:합의 %>
	var apvrs=[], infms=[], agrs=[], msgs=[], msg;<%
	// 각 결재자 항목 별 임시 변수
	//    roleCd:역할코드, apvrUid:결재자UID, apvDeptId:결재자부서ID, 
	//    dblApvTypCd:이중결재구분코드(reqDept:신청부서, prcDept:처리부서) %>
	var roleCd, apvrUid, apvDeptId, dblApvTypCd;<%//
	// 결재역할 수 - 1명만 세팅 체크용
	//    makByOneCnt:(기안,1인결재)자 수, - 한명만(필수)
	//    apvPredCnt:(결재,전결)자 수, - [옵션]최종 결재자 필수 여부 에 따라서 필수
	//    predCnt:전결자 수, entuCnt:결재안함(위임)자 수
	//        - [결재안함(위임)]은 [전결] 뒤에만 올 수 있으면 1명만 가능
	//    fstVwCnt:선람자 수
	%>
	var makByOneCnt=0, apvPredCnt=0, predCnt=0, entuCnt=0, fstVwCnt=0;<%
	// prcDeptCnt:처리부서 수, 
	// prcDeptApvrCnt:처리부서의 사용자 수, 
	// validPrcDeptApvrCnt:처리부서의 사용자 수 - 공석 제외, 
	//    - 처리부서, 처리부서 사용장 - 동시에 지정 불가능, 처리부서 또는 처리부서 사용자 둘 중 하나 필수
	//
	// validNomlApvrCnt:유효한 일반결재 결재자 수(검토, 합의(순차/병렬, 개인/부서), 결재, 전결) - 공석 제외 %>
	var prcDeptCnt=0, prcDeptApvrCnt=0, validPrcDeptApvrCnt=0, validNomlApvrCnt=0;<%
	
	// 결재라인 최대수 체크용 유효 수
	//    apvCnt:결재, agrCnt:합의, reqCnt:신청, prcCnt:처리 %>
	var apvCnt=0, agrCnt=0, reqCnt=0, prcCnt=0;<%
	// makrDeptId:기안자부서ID, makrRoleCd:기안자역할코드(mak:기안,byOne:1인결재)  %>
	var makrDeptId = users[0].apvDeptId;
	var makrRoleCd = users[0].apvrRoleCd;<%
	
	// 사용자 중복 체크 %>
	users.each(function(index, apvr){
		roleCd = apvr.apvrRoleCd;
		apvrUid = apvr.apvrUid;
		dblApvTypCd = apvr.dblApvTypCd;<%
		// 1명 지정 체크 - byOne:1인결재, mak:기안 %>
		if(roleCd=="${byOneRole}" || roleCd=="${makRole}"){
			makByOneCnt++;
			if(makByOneCnt>1){<%
				//ap.apvLn.needOneUserNot2="{0}자", "{1}자" 중 한명을 지정해야 합니다.(둘 지정 불가) - [1인결재][기안]%>
				msg = $m.msg.callMsg('ap.apvLn.needOneUserNot2', ['#ap.term.byOne','#ap.term.${makRole}']);
				if(!msgs.contains(msg)) msgs.push(msg);
			}
		}<%
		// 1명 지정 체크 - apv:결재, pred:전결 %>
		if(roleCd=="apv" || roleCd=="pred"){
			apvPredCnt++;
			if(apvPredCnt>1){<%
				//ap.apvLn.needOneUserNot2="{0}자", "{1}자" 중 한명을 지정해야 합니다.(둘 지정 불가) - [결재][전결]%>
				msg = $m.msg.callMsg('ap.apvLn.needOneUserNot2', ['#ap.term.apv','#ap.term.pred']);
				if(!msgs.contains(msg)) msgs.push(msg);
			}<%
			// pred:전결 - 지정 수 확인 %>
			if(roleCd=="pred"){
				predCnt++;
			}
		}<%
		// 1명 지정 체크 - entu:결재안함(위임) %>
		if(roleCd=="entu"){
			entuCnt++;
			if(entuCnt>1){<%
				//ap.apvLn.needOneUser=한명의 "{0}자"를 지정해야 합니다. - [결재안함(위임)]%>
				msg = $m.msg.callMsg('ap.apvLn.needOneUser', ['#ap.term.entu']);
				if(!msgs.contains(msg)) msgs.push(msg);
			}
		}<%
		// 1명 지정 체크 - fstVw:선람 %>
		if(roleCd=="fstVw"){
			fstVwCnt++;
			if(fstVwCnt>1){<%
				//ap.apvLn.needOneUser=한명의 "{0}자"를 지정해야 합니다. - [선람]%>
				msg = $m.msg.callMsg('ap.apvLn.needOneUser', ['#ap.term.fstVw']);
				if(!msgs.contains(msg)) msgs.push(msg);
			}
		}<%
		// 동일 사용자 체크 - 결재 라인
		// byOne:1인결재, mak:기안, revw:검토, abs:공석, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, apv:결재, pred:전결,  entu:결재안함(위임)
		// makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람 %>
		if(roleCd=="${byOneRole}" || roleCd=="${makRole}" || roleCd=="revw" || roleCd=="abs" || roleCd=="psnOrdrdAgr" || roleCd=="psnParalAgr" || roleCd=="apv" || roleCd=="pred" || roleCd=="entu" || roleCd=="fstVw" || roleCd=="pubVw" || roleCd=="paralPubVw"){
			if(apvrs.contains(apvrUid)){<%
				//ap.apvLn.checkDupApvLn=결재라인에 중복된 {0}가 있습니다. [사용자]%>
				msg = $m.msg.callMsg('ap.apvLn.checkDupApvLn', ['#cols.user']) + " : " + apvr.apvrNm;
				if(!msgs.contains(msg)) msgs.push(msg);
			} else {
				apvrs.push(apvrUid);
			}<%
			// psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의 - 합의수 더하기 - 도장방 갯수보다 많을 경우 제어용 %>
			if(roleCd=="psnOrdrdAgr" || roleCd=="psnParalAgr"){
				agrCnt++;<%
			// entu:결재안함(위임) 이 아니면 - 결재수 더하기 - 도장방 갯수보다 많을 경우 제어용 %>
			} else if(roleCd!="entu"){
				apvCnt++;
			}
		}<%
		// 동일 사용자 체크 - psnInfm:개인통보, postApvd:사후보고(후열) %>
		if(roleCd=="psnInfm" || roleCd=="postApvd"){
			if(infms.contains(apvrUid)){<%
				//ap.apvLn.checkDupRole2="{0}"와(과) "{1}"에 중복된 {2}가 있습니다. [개인통보][사후보고(후열)][사용자]%>
				msg = $m.msg.callMsg('ap.apvLn.checkDupRole2', ['#ap.term.psnInfm','#ap.term.postApvd','#cols.user']) + " : " + apvr.apvrNm;
				if(!msgs.contains(msg)) msgs.push(msg);
			} else {
				infms.push(apvrUid);
			}
		}<%
		// 일반결재(이중결재, 1인결재 아님)에서 결재라인 추가 여부 체크 %>
		if(gFormApvLnTypCd!='apvLnDbl' && makrRoleCd=='${makRole}'){<%
			// 유효 결재 라인 - 카운트
			//    revw:검토, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, 
			//    deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, apv:결재, pred:전결
			//    fstVw:선람, pubVw:공람, paralPubVw:동시공람 - 문서유통 %>
			if(roleCd=="revw" || roleCd=="psnOrdrdAgr" || roleCd=="psnParalAgr" || roleCd=="deptOrdrdAgr" || roleCd=="deptParalAgr" || roleCd=="apv" || roleCd=="pred" || roleCd=="fstVw" || roleCd=="pubVw" || roleCd=="paralPubVw"){
				validNomlApvrCnt++;
			}
		}<%
		// "1인결재"에서 사용 못하는 기능 체크 %>
		if(makrRoleCd == '${byOneRole}'){<%
			// byOne:1인결재, postApvd:사후보고(후열), psnInfm:개인통보 - 가 아닌 경우 %>
			if(roleCd!="${byOneRole}" && roleCd!="postApvd" && roleCd!="psnInfm"){<%
				//ap.apvLn.notRoleAt="{0}"에서는 "{1}"을(를) 사용 할 수 없습니다. - [1인결재][..]%>
				msg = $m.msg.callMsg('ap.apvLn.notRoleAt', ['#ap.term.byOne','#ap.term.'+roleCd]) + " : " + apvr.apvrNm;
				if(!msgs.contains(msg)) msgs.push(msg);
			}
		}<%
		// 이중결재 "신청부서"에서 사용 못하는 기능 체크 %>
		if(dblApvTypCd == 'reqDept'){<%
			// mak:기안, revw:검토, abs:공석 - 가 아닌 경우 %>
			if(roleCd!="mak" && roleCd!="revw" && roleCd!="abs"){<%
				//ap.apvLn.notRoleAt="{0}"에서는 "{1}"을(를) 사용 할 수 없습니다. - [신청부서][..]%>
				msg = $m.msg.callMsg('ap.apvLn.notRoleAt', ['#ap.term.reqDept','#ap.term.'+roleCd]) + " : " + apvr.apvrNm;
				if(!msgs.contains(msg)) msgs.push(msg);
			}<%
			// 신청부서 더하기 - 도장방 갯수보다 많을 경우 제어용 %>
			reqCnt++;
		}<%
		// 이중결재 "처리부서"에서 사용 못하는 기능 체크 %>
		if(dblApvTypCd == 'prcDept'){<%
			// revw:검토, abs:공석, apv:결재, pred:전결, entu:결재안함(위임), postApvd:사후보고(후열) - 가 아닌경우 %>
			if(roleCd!="revw" && roleCd!="abs" && roleCd!="apv" && roleCd!="pred" && roleCd!="entu" && roleCd!="postApvd"){<%
				//ap.apvLn.notRoleAt="{0}"에서는 "{1}"을(를) 사용 할 수 없습니다. - [처리부서][..]%>
				msg = $m.msg.callMsg('ap.apvLn.notRoleAt', ['#ap.term.prcDept','#ap.term.'+roleCd]) + " : " + apvr.apvrNm;
				if(!msgs.contains(msg)) msgs.push(msg);
			}
		}<%
		// 처리부서 사용자 수 %>
		if(dblApvTypCd=='prcDept'){<%
			// 처리부서 사용자 카운트 - 처리부서 와 처리부서의 사용자가 같이 지정되는 것 방지 용%>
			prcDeptApvrCnt++;<%
			// 유효한 처리부서 사용자 카운트(공석, 결재안함, 사후보고 제외) - 처리부서의 결재자가 세팅 되었는지 체크용 %>
			if(roleCd!="abs" && roleCd!="entu" && roleCd!="postApvd"){
				validPrcDeptApvrCnt++;
			}<%
			// 신청부서 더하기 - 도장방 갯수보다 많을 경우 제어용 %>
			if(roleCd!="entu" && roleCd!="postApvd"){
				prcCnt++;
			}
		}
	});
	infms=[];<%// 개인통보 초기화 - 부서통보 체크용 %><%
	// 부서 중복 체크 %>
	depts.each(function(index, apvr){
		roleCd = apvr.apvrRoleCd;
		apvDeptId = apvr.apvDeptId;
		dblApvTypCd = apvr.dblApvTypCd;<%
		// deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의 %>
		if(roleCd=="deptOrdrdAgr" || roleCd=="deptParalAgr"){<%
			// 기안부서를 합의부서로 지정했는지 체크 %>
			if(makrDeptId == apvDeptId){<%
				//ap.apvLn.notRoleDeptWithRoleUser="{0}자"가 속한 부서를 "{1} 부서"로 지정 할 수 없습니다. - [기안][부서순차합의, 부서병렬합의]%>
				msg = $m.msg.callMsg('ap.apvLn.notRoleDeptWithRoleUser', ['#ap.term.mak','#ap.term.'+roleCd]) + " : " + apvr.apvDeptNm;
				if(!msgs.contains(msg)) msgs.push(msg);
			}<%
			// 동일 합의부서 체크 - deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의 %>
			if(agrs.contains(apvDeptId)){<%
				//ap.apvLn.checkDupRole2="{0}"와(과) "{1}"에 중복된 {2}가 있습니다. [부서순차합의][부서병렬합의][부서]%>
				msg = $m.msg.callMsg('ap.apvLn.checkDupRole2', ['#ap.term.deptOrdrdAgr','#ap.term.deptParalAgr','#cols.dept']) + " : " + apvr.apvDeptNm;
				if(!msgs.contains(msg)) msgs.push(msg);
			} else {
				agrs.push(apvDeptId);
			}<%
			// deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의 - 합의수 더하기 - 도장방 갯수보다 많을 경우 제어용 %>
			agrCnt++;
		}<%
		// prcDept:처리부서 %>
		if(roleCd=="prcDept"){<%
			// 1부서 지정 체크 - prcDept:처리부서 %>
			prcDeptCnt++;
			if(prcDeptCnt>1){<%
				//ap.apvLn.needOneDept=하나의 "{0}"를 지정해야 합니다. - [처리부서]%>
				msg = $m.msg.callMsg('ap.apvLn.needOneDept', ['#ap.term.prcDept']);
				if(!msgs.contains(msg)) msgs.push(msg);
			}<%
			// 기안부서를 처리부서로 지정했는지 체크 %>
			if(makrDeptId == apvDeptId){<%
				//ap.apvLn.notRoleDeptWithRoleUser="{0}자"가 속한 부서를 "{1} 부서"로 지정 할 수 없습니다. - [기안][부서순차합의, 부서병렬합의]%>
				msg = $m.msg.callMsg('ap.apvLn.notRoleDeptWithRoleUser', ['#ap.term.mak','#ap.term.'+roleCd]) + " : " + apvr.apvDeptNm;
				if(!msgs.contains(msg)) msgs.push(msg);
			}<%
			// prcDept:처리부서 - 처리부서수 더하기 - 도장방 갯수보다 많을 경우 제어용 %>
			prcCnt++;
		}<%
		// deptInfm:부서통보 %>
		if(roleCd=="deptInfm"){<%
			// 동일 통보부서 체크 - deptInfm:부서통보 %>
			if(infms.contains(apvDeptId)){<%
				//ap.apvLn.checkDupRole="{0}"에 중복된 {1}가 있습니다. [부서통보][부서]%>
				msg = $m.msg.callMsg('ap.apvLn.checkDupRole', ['#ap.term.deptInfm','#cols.dept']) + " : " + apvr.apvDeptNm;
				if(!msgs.contains(msg)) msgs.push(msg);
			} else {
				infms.push(apvDeptId);
			}
		}<%
		// "1인결재"에서 사용 못하는 기능 체크 %>
		if(makrRoleCd == '${byOneRole}'){<%
			// deptInfm:부서통보 - 가 아닌 경우 %>
			if(roleCd!="deptInfm"){<%
				//ap.apvLn.notRoleAt="{0}"에서는 "{1}"을(를) 사용 할 수 없습니다. - [1인결재][..]%>
				msg = $m.msg.callMsg('ap.apvLn.notRoleAt', ['#ap.term.byOne','#ap.term.'+roleCd]) + " : " + apvr.apvDeptNm;
				if(!msgs.contains(msg)) msgs.push(msg);
			}
		}<%
		// 이중결재 "신청부서"에서 사용 못하는 기능 체크 - 신청부서는 부서 지정 기능 없음 %>
		if(dblApvTypCd == 'reqDept'){<%
			//ap.apvLn.notRoleAt="{0}"에서는 "{1}"을(를) 사용 할 수 없습니다. - [1인결재][..]%>
			msg = $m.msg.callMsg('ap.apvLn.notRoleAt', ['#ap.term.reqDept','#ap.term.'+roleCd]) + " : " + apvr.apvDeptNm;
			if(!msgs.contains(msg)) msgs.push(msg);
		}<%
		// 이중결재 "처리부서"에서 사용 못하는 기능 체크 %>
		if(dblApvTypCd == 'prcDept'){<%
			// deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, deptInfm:부서통보 - 인 경우 %>
			if(roleCd=="deptOrdrdAgr" || roleCd=="deptParalAgr" || roleCd=="deptInfm"){<%
				//ap.apvLn.notRoleAt="{0}"에서는 "{1}"을(를) 사용 할 수 없습니다. - [처리부서][..]%>
				msg = $m.msg.callMsg('ap.apvLn.notRoleAt', ['#ap.term.prcDept','#ap.term.'+roleCd]) + " : " + apvr.apvDeptNm;
				if(!msgs.contains(msg)) msgs.push(msg);
			}
		}
	});<%
	// 기안자/1인결재자 지정 체크 %>
	if(makByOneCnt==0){<%
		//ap.apvLn.needOneUserOf2="{0}자", "{1}자" 중 한명을 지정해야 합니다. - [1인결재][기안]%>
		msg = $m.msg.callMsg('ap.apvLn.needOneUserOf2', ['#ap.term.byOne','#ap.term.${makRole}']);
		if(!msgs.contains(msg)) msgs.push(msg);
	}<%
	// 이중결재 일때 %>
	if(gFormApvLnTypCd == 'apvLnDbl'){<%
		// "처리부서" 와 "처리부서의 사용자(공석제외)"가 지정되지 않은 경우 %>
		if(prcDeptCnt==0 && validPrcDeptApvrCnt==0){<%
			// 공석만 지정된 경우 %>
			if(validPrcDeptApvrCnt != prcDeptApvrCnt){<%
				//ap.apvLn.empty2="{0}"에 유효한 결재자가 지정되지 않았습니다. [처리부서]%>
				msg = $m.msg.callMsg('ap.apvLn.empty2',['#ap.term.prcDept']);
				if(!msgs.contains(msg)) msgs.push(msg);<%
			// [옵션]처리부서 지정 - 가능 %>
			} else if(gOptConfig['prcDeptEnab']=='Y'){<%
				//ap.apvLn.needPrcDeptOrPrcDeptUser="{0}" 또는 "{0}의 사용자"를 지정 해야 합니다. [처리부서]%>
				msg = $m.msg.callMsg('ap.apvLn.needPrcDeptOrPrcDeptUser',['#ap.term.prcDept']);
				if(!msgs.contains(msg)) msgs.push(msg);<%
			// [옵션]처리부서 지정 - 불가능 %>
			} else {<%
				//ap.apvLn.needPrcDeptUser="{0}의 사용자"를 지정 해야 합니다. [처리부서]%>
				msg = $m.msg.callMsg('ap.apvLn.needPrcDeptUser',['#ap.term.prcDept']);
				if(!msgs.contains(msg)) msgs.push(msg);
			}<%
		// "처리부서"와 "처리부서의 사용자"를 같이 넣은 경우 %>
		} else if(prcDeptCnt>0 && prcDeptApvrCnt>0){<%
			//ap.apvLn.needOneOfPrc="{0}" 또는 "{0} 사용자"를 같이 지정할 수 없습니다. [처리부서]%>
			msg = $m.msg.callMsg('ap.apvLn.needOneOfPrc',['#ap.term.prcDept']);
			if(!msgs.contains(msg)) msgs.push(msg);
		}
	}<%
	// [옵션]최종 결재자(apv:결재, pred:전결) - 필수 %>
	if(gOptConfig['needLastApvr']=='Y' && makrRoleCd != '${byOneRole}' && makrRoleCd != 'makVw'){<%
		// 최종결재자 없고, 처리부서(이중결재의)가 없으며 - 처리부서 지정되면 처리부서에서 최종결재 지정 %>
		if(apvPredCnt==0 && prcDeptCnt==0){<%
			//ap.apvLn.needLastApvr=최종 결재자({0} 또는 {1})를 지정 해야 합니다. [결재][전결]%>
			msg = $m.msg.callMsg('ap.apvLn.needLastApvr',['#ap.term.apv','#ap.term.pred']);
			if(!msgs.contains(msg)) msgs.push(msg);
		}<%
	// [옵션]최종 결재자(apv:결재, pred:전결) - 필수 아님 %>
	} else {<%
		// 일반결재(이중결재, 1인결재 아님)에서 결재라인 추가 여부 체크 %>
		if(gFormApvLnTypCd!='apvLnDbl' && makrRoleCd=='${makRole}'){<%
			// 유효한결재자수 체크%>
			if(validNomlApvrCnt==0){<%
				//ap.apvLn.empty=유효한 결재자가 지정되지 않았습니다.%>
				msg = $m.msg.callMsg('ap.apvLn.empty');
				if(!msgs.contains(msg)) msgs.push(msg);
			}
		}
	}
	if(msgs.length>0){
		$m.dialog.alert(msgs.join('\n'));
		return false;
	}<%
	///////////////////////////
	//       순서 체크 
	%>
	var curOrder, maxOrder=-1, firstPrcDeptIndex=-1, orders=[], roles=[];<%
	// byOne:1인결재, mak:기안, makAgr:합의기안, makVw:담당 %>
	orders.push(["${byOneRole}","${makRole}"]);<%
	// revw:검토, abs:공석, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의 %>
	orders.push(["revw","abs","psnOrdrdAgr","psnParalAgr","deptOrdrdAgr","deptParalAgr"]);<%
	// apv:결재, pred:전결 %>
	orders.push(["apv","pred"]);<%
	// entu:결재안함(위임) %>
	orders.push(["entu"]);<%
	// postApvd:사후보고(후열) %>
	orders.push(["postApvd"]);<%
	// psnInfm:개인통보, deptInfm:부서통보  %>
	orders.push(["psnInfm","deptInfm"]);<%//fstVw:선람, pubVw:공람, paralPubVw:동시공람
	// fstVw:선람 %>
	orders.push(["fstVw"]);<%
	// pubVw:공람, paralPubVw:동시공람 %>
	orders.push(["pubVw","paralPubVw"]);
	orders.each(function(){ roles.push(''); });
	/* 
	apvrList.each(function(index, apvr){
		if(apvr.apvrDeptYn == 'Y') depts.push(apvr);
		else users.push(apvr);
	}); */
	//$inputs.each(function(inputIndex){
	apvrList.each(function(inputIndex, apvr){
		order = -1;
		roleCd = apvr.apvrRoleCd;
		apvrUid = apvr.apvrUid;
		dblApvTypCd = apvr.dblApvTypCd;<%
		// orders 의 몇 번째에 있는 roleCd 인지 찾기 - 결과저장 : order %>
		orders.each(function(index, arr){
			if(arr.contains(roleCd)){
				order = index;
				return false;
			}
		});<%
		// 이중결재 일때 - 신청부서가 처리부서 뒤에 오는지 체크 %>
		if(gFormApvLnTypCd == 'apvLnDbl' && firstPrcDeptIndex != 9999){
			if(dblApvTypCd=='prcDept'){<%
				// 처리부서면 처음의 처리부서 index를 firstPrcDeptIndex 에 저장 %>
				if(firstPrcDeptIndex==-1){
					firstPrcDeptIndex = inputIndex;
				}
			} else if(dblApvTypCd=='reqDept'){<%
				// 신청부서면 처리부서가 있으면 - 순서가 바뀐것
				// 다시 체크 안하도록 9999 세팅 %>
				if(firstPrcDeptIndex!=-1){
					firstPrcDeptIndex = 9999;<%
					//ap.apvLn.mustRoleAft="{0}"은(는) "{1}" 뒤에 사용 해야 합니다. [처리부서][신청부서]%>
					msg = $m.msg.callMsg('ap.apvLn.mustRoleAft',['#ap.term.prcDept','#ap.term.reqDept']);
					if(!msgs.contains(msg)) msgs.push(msg);
				}
			}
		}
		if(order>=0){<%
			// 순서가 뒤바뀐 경우 %>
			if(maxOrder>order){<%
				// ap.apvLn.mustNotRoleAft="{0}"은(는) "{1}" 뒤에 사용 할 수 없습니다. %>
				msg = $m.msg.callMsg('ap.apvLn.mustNotRoleAft',['#ap.term.'+roleCd,'#ap.term.'+roles[maxOrder]]) + " : " +(apvrUid=='' ? apvr.apvDeptNm : apvr.apvrNm);
				if(!msgs.contains(msg)) msgs.push(msg);
			} else {
				if(maxOrder<order){
					maxOrder = order;<%// 현재까지 가장 높은 순위 저장 %>
				}
				if(roles[order]==''){roles[order] = roleCd;}<%// 현재 결재 역할을 해당 순위 순번에 저장 %>
			}<%
			// entu:결재안함(위임)을 사용 할 때 - pred:전결 이 없을 경우 %>
			if(roleCd=='entu'){
				if(predCnt==0){<%
					//ap.apvLn.mustRoleAft="{0}"은(는) "{1}" 뒤에 사용 해야 합니다. - [결재안함(위임)][전결]%>
					msg = $m.msg.callMsg('ap.apvLn.mustRoleAft', ['#ap.term.entu','#ap.term.pred']);
					if(!msgs.contains(msg)) msgs.push(msg);
				}
			}
		}
	});
	if(msgs.length>0){
		$m.dialog.alert(msgs.join('\n'));
		return false;
	}
	var gApvLnMaxCnt = $m.nav.getWin(-1).gApvLnMaxCnt;
	<%
	// 결재라인 최대수 체크용 유효 수 체크
	//    gApvLnMaxCnt : apv:결재, agr:합의, req:신청, prc:처리 - 양식 설정 내용
	//    apvCnt:결재, agrCnt:합의, reqCnt:신청, prcCnt:처리 - 스크립트에서 설정 한 값
	//    gFormApvLnTypCd : 결재선 구분코드
	//        apvLn:결재(합의표시안함), apvLnMixd:결재(결재합의혼합)
	//        apvLn1LnAgr:결재+합의(1줄), apvLn2LnAgr:결재+합의(2줄)
	//        apvLnDbl:이중결재 %>
	if(apvLnOpt!='agr'){
		if(gFormApvLnTypCd=='apvLn'){<%// apvLn:결재(합의표시안함) %>
			if(apvCnt > gApvLnMaxCnt.apv){<%
				//ap.apvLn.exceedSignArea=양식에 표시할 수 있는 결재 수를 초과 하였습니다.(양식:{0}, 설정:{1}) %>
				msg = $m.msg.callMsg('ap.apvLn.exceedSignArea', [''+gApvLnMaxCnt.apv,''+apvCnt]);
				if(!msgs.contains(msg)) msgs.push(msg);
			}
		} else if(gFormApvLnTypCd=='apvLnMixd'){<%// apvLnMixd:결재(결재합의혼합) %>
			if((apvCnt + agrCnt) > gApvLnMaxCnt.apv){<%
				//ap.apvLn.exceedSignArea=양식에 표시할 수 있는 결재 수를 초과 하였습니다.(양식:{0}, 설정:{1}) %>
				msg = $m.msg.callMsg('ap.apvLn.exceedSignArea', [''+gApvLnMaxCnt.apv,''+(apvCnt + agrCnt)]);
				if(!msgs.contains(msg)) msgs.push(msg);
			}
		} else if(gFormApvLnTypCd=='apvLn1LnAgr' || gFormApvLnTypCd=='apvLn2LnAgr'){<%// apvLn1LnAgr:결재+합의(1줄), apvLn2LnAgr:결재+합의(2줄) %>
			if(apvCnt > gApvLnMaxCnt.apv){<%
				//ap.apvLn.exceedSignArea2=양식({2})에 표시할 수 있는 결재 수를 초과 하였습니다.(양식:{0}, 설정:{1}) - [][][결재] %>
				msg = $m.msg.callMsg('ap.apvLn.exceedSignArea2', [''+gApvLnMaxCnt.apv,''+apvCnt, '#ap.term.apv']);
				if(!msgs.contains(msg)) msgs.push(msg);
			}
			if(agrCnt > gApvLnMaxCnt.agr){<%
				//ap.apvLn.exceedSignArea2=양식({2})에 표시할 수 있는 결재 수를 초과 하였습니다.(양식:{0}, 설정:{1}) - [][][합의] %>
				msg = $m.msg.callMsg('ap.apvLn.exceedSignArea2', [''+gApvLnMaxCnt.agr,''+agrCnt, '#ap.term.agr']);
				if(!msgs.contains(msg)) msgs.push(msg);
			}
		} else if(gFormApvLnTypCd=='apvLnDbl'){<%// apvLnDbl:이중결재 %>
			if(reqCnt > gApvLnMaxCnt.req){<%
				//ap.apvLn.exceedSignArea2=양식({2})에 표시할 수 있는 결재 수를 초과 하였습니다.(양식:{0}, 설정:{1}) - [][][신청] %>
				msg = $m.msg.callMsg('ap.apvLn.exceedSignArea2', [''+gApvLnMaxCnt.req,''+reqCnt, '#ap.term.req']);
				if(!msgs.contains(msg)) msgs.push(msg);
			}
			if(prcCnt > gApvLnMaxCnt.prc){<%
				//ap.apvLn.exceedSignArea2=양식({2})에 표시할 수 있는 결재 수를 초과 하였습니다.(양식:{0}, 설정:{1}) - [][][합의] %>
				msg = $m.msg.callMsg('ap.apvLn.exceedSignArea2', [''+gApvLnMaxCnt.prc,''+prcCnt, '#ap.term.prc']);
				if(!msgs.contains(msg)) msgs.push(msg);
			}
		}
	}<%
	// 공석으로 결재라인 완결 금지 체크 
	// [히든옵션] lastAbsEnab=마지막 결재자 공석 허용 %>
	if(gOptConfig['lastAbsEnab']!='Y'){<%
		// abs:공석 뒤에 와야 하는 것
		//   revw:검토, apv:결재, pred:전결, prcDept:처리부서
		//   psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, 
		%>
		var lastAbs = null;
		apvrList.each(function(inputIndex, apvr){
			roleCd = apvr.apvrRoleCd;
			if(roleCd=='abs'){
				lastAbs = this;
			} else if(roleCd=='revw'||roleCd=='apv'||roleCd=='pred'||roleCd=='prcDept'||roleCd=='psnOrdrdAgr'||roleCd=='psnParalAgr'||roleCd=='deptOrdrdAgr'||roleCd=='deptParalAgr'){
				lastAbs = null;
			}
		});
		if(lastAbs != null){
			if(gFormApvLnTypCd != 'apvLnDbl'){<%
				// ap.apvLn.notEndWithAbs1="{0}"으로 결재라인을 완결 할 수 없습니다.
				//   \n공석 뒤에는 "{1}", "{2}", "{3}", "{4}" 중 하나가 와야 합니다.
				// %>
				msg = $m.msg.callMsg('ap.apvLn.notEndWithAbs1', ['#ap.term.abs', '#ap.term.revw', '#ap.term.apv', '#ap.term.pred', '#ap.term.agr']);
				msgs.push(msg);
			} else if($(lastAbs).attr('data-dblApvTypCd')=='reqDept'){<%
				// ap.apvLn.notEndWithAbs2="{0}"으로 결재라인을 완결 할 수 없습니다.
				//   \n공석 뒤에는 "{1}", "{2}" 중 하나가 와야 합니다.
				// %>
				msg = $m.msg.callMsg('ap.apvLn.notEndWithAbs2', ['#ap.term.abs', '#ap.term.revw', '#ap.term.prcDept']);
				msgs.push(msg);
			} else if($(lastAbs).attr('data-dblApvTypCd')=='prcDept'){<%
				// ap.apvLn.notEndWithAbs3="{0}"으로 결재라인을 완결 할 수 없습니다.
				//   \n공석 뒤에는 "{1}", "{2}", "{3}" 중 하나가 와야 합니다.
				// %>
				msg = $m.msg.callMsg('ap.apvLn.notEndWithAbs3', ['#ap.term.abs', '#ap.term.revw', '#ap.term.apv', '#ap.term.pred']);
				msgs.push(msg);
			}
		}
	}
	
	if(msgs.length>0){
		$m.dialog.alert(msgs.join('\n'));
		return false;
	}
	return true;
}

//]]>
</script>
